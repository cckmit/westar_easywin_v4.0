package com.westar.core.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.MailSet;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ValiMailUsrPw;
import com.westar.core.service.MailSetService;

@Controller
@RequestMapping("/mailSet")
public class MailSetController extends BaseController {

	@Autowired
	MailSetService mailSetService;

	/**
	 * 查询个人邮箱设置
	 * 
	 * @return
	 */
	@RequestMapping("/listMailSet")
	public ModelAndView listMailSet(MailSet mailSet) {
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		UserInfo user = this.getSessionUser();
		mailSet.setUserId(user.getId());
		List<MailSet> list = mailSetService.listMailSet(mailSet);
		mav.addObject("list", list);
		mav.addObject("userInfo", user);
		return mav;
	}
	
	/**
	 * 分页查询个人邮箱设置
	 * 
	 * @return
	 */
	@RequestMapping("/listPagedMailSet")
	public ModelAndView listPagedMailSet(MailSet mailSet) {
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		UserInfo user = this.getSessionUser();
		mailSet.setUserId(user.getId());
		List<MailSet> list = mailSetService.listPagedMailSet(mailSet);
		mav.addObject("list", list);
		mav.addObject("userInfo", user);
		return mav;
	}

	/**
	 * 添加邮箱设置页面
	 * 
	 * @return
	 */
	@RequestMapping("/addMailSetPage")
	public ModelAndView addMailSetPage() {
		return new ModelAndView("/userInfo/selfCenter").addObject("userInfo", this.getSessionUser());
	}

	/**
	 * 添加邮箱设置
	 * 
	 * @return
	 */
	@RequestMapping("/addMailSet")
	public ModelAndView addMailSet(MailSet mailSet, String redirectPage) {
		ModelAndView mav = new ModelAndView("redirect:" + redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		mailSet.setUserId(sessionUser.getId());
		if (null == mailSet.getSmtpSSL()) {
			mailSet.setSmtpSSL("0");
		}
		// 默认不需要身份验证（暂时没有用处）
		mailSet.setIsValidate("0");
		// 添加邮箱设置
		mailSetService.addMailSet(mailSet);
		mav.addObject("userInfo", sessionUser);

		return mav;
	}

	/**
	 * 查询个人邮箱设置
	 * 
	 * @return
	 */
	@RequestMapping("/updateMailSetPage")
	public ModelAndView updateMailSetPage(Integer id) {
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		// 查询邮箱设置
		MailSet mailSet = mailSetService.getMailSetById(id);
		// 取得当前操作人员
		UserInfo user = this.getSessionUser();

		mav.addObject("mailSet", mailSet);
		mav.addObject("userInfo", user);
		return mav;
	}

	/**
	 * 修改邮箱设置
	 * 
	 * @return
	 */
	@RequestMapping("/updateMailSet")
	public ModelAndView updateMailSet(MailSet mailSet, String redirectPage) {
		ModelAndView mav = new ModelAndView("redirect:" + redirectPage);

		if (null == mailSet.getSmtpSSL()) {
			mailSet.setSmtpSSL("0");
		}
		// 修改邮箱设置
		mailSetService.updateMailSet(mailSet);
		this.setNotification(Notification.SUCCESS, "修改成功");
		return mav;
	}

	/**
	 * 删除人员邮箱配置
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delMailSet")
	public ModelAndView delMailSet(Integer id, String redirectPage) {
		ModelAndView mav = new ModelAndView("redirect:" + redirectPage);
		// 删除邮箱设置
		Integer count = mailSetService.delMailSet(id);
		if(count < 1) {
			this.setNotification(Notification.SUCCESS, "删除成功");
		}else {
			this.setNotification(Notification.ERROR, "删除失败，请先删除本地关联邮件记录！");
		}
		return mav;
	}

	/**
	 * 验证邮箱设置是否正确
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/valiMail")
	public Map<String, Object> valiMail(MailSet mailSet) {
		Map<String, Object> map = new HashMap<String, Object>();
		ValiMailUsrPw valiMailUsrPw = new ValiMailUsrPw();
		UserInfo sessionUser = this.getSessionUser();

		mailSet.setSubject("企业\"" + sessionUser.getOrgName() + "\"配置推送邮箱");
		mailSet.setBody("企业\"" + sessionUser.getOrgName() + "\"配置推送邮箱");

		List<String> mailTos = new ArrayList<String>();
		mailTos.add(mailSet.getAccount());
		mailSet.setMailTos(mailTos);
		Session session = valiMailUsrPw.getSmtpSession(mailSet.getSmtpSSL());

		try {
			valiMailUsrPw.valiDate(session, mailSet);
			map.put("status", "y");
		} catch (AddressException e) {
			map.put("status", "n");
		} catch (AuthenticationFailedException e) {// 密码错误
			map.put("status", "n1");
		} catch (MessagingException e) {// 端口号错误
			map.put("status", "n2");
		} catch (Exception e) {
			map.put("status", "n");
		}
		return map;
	}

//	public static void main(String[] args) throws Exception {
//		// 定义连接POP3服务器的属性信息
//		String pop3Server = "pop.qq.com";
//		String username = "账号";
//		String password = "授权码"; // QQ邮箱的SMTP的授权码，什么是授权码，它又是如何设置？
//		
//		ValiMailUsrPw valiMailUsrPw = new ValiMailUsrPw();
//		// 获取连接
//		Session session = valiMailUsrPw.getPopSession("1");
//		// 获取Store对象
//		Store store = session.getStore();
//		store.connect(pop3Server, username, password); // POP3服务器的登陆认证
//		// 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
//		Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
//		folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限
//
//		Message[] messages = folder.getMessages(folder.getMessageCount()-9,folder.getMessageCount());// 得到邮箱帐户中的所有邮件
//		System.out.println(messages.length);
//		for (Message message : messages) {
//			String subject = message.getSubject();// 获得邮件主题
//			Address from = (Address) message.getFrom()[0];// 获得发送者地址
//			System.out.println("邮件的主题为: " + subject + "\t发件人地址为: " + from);
//			System.out.println("邮件的内容为：");
//			message.writeTo(System.out);// 输出邮件内容到控制台
//		}
//
//		folder.close(false);// 关闭邮件夹对象
//		store.close(); // 关闭连接对象
//	}

}