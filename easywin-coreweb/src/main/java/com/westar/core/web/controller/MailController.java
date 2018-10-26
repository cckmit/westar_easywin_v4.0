package com.westar.core.web.controller;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.Mail;
import com.westar.base.model.MailSet;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.core.service.MailService;
import com.westar.core.service.MailSetService;

@Controller
@RequestMapping("/mail")
public class MailController extends BaseController{

	@Autowired
	MailService mailService;
	
	@Autowired
	MailSetService mailSetService;
	
	/**
	 * 分页查询邮件
	 * @author hcj 
	 * @param mail
	 * @return 
	 * @date 2018年9月18日 下午1:14:04
	 */
	@RequestMapping("/listPagedReceiveMail")
	public ModelAndView listPagedReceiveMail(Mail mail) {
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		UserInfo userInfo = this.getSessionUser();
		MailSet mailSet = new MailSet();
		mailSet.setUserId(userInfo.getId());
		List<MailSet> listMs = mailSetService.listMailSet(mailSet);
		mail.setFolder("INBOX");
		List<Mail> lists = mailService.listPagedMail(mail,userInfo);
		mav.addObject("listMs", listMs);
		mav.addObject("userInfo", userInfo);
		mav.addObject("mail", mail);
		mav.addObject("lists", lists);
		return mav;
	}
	
	/**
	 * 分页查询已发送邮件
	 * @author hcj 
	 * @param mail
	 * @return 
	 * @date 2018年9月18日 下午1:16:08
	 */
	@RequestMapping("/listPagedSendMail")
	public ModelAndView listPagedSendMail(Mail mail) {
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		UserInfo userInfo = this.getSessionUser();
		MailSet mailSet = new MailSet();
		mailSet.setUserId(userInfo.getId());
		List<MailSet> listMs = mailSetService.listMailSet(mailSet);
		mail.setFolder("已发送");
		List<Mail> lists = mailService.listPagedMail(mail,userInfo);
		mav.addObject("listMs", listMs);
		mav.addObject("userInfo", userInfo);
		mav.addObject("mail", mail);
		mav.addObject("lists", lists);
		return mav;
	}
	
	/**
	 * 查看邮件详情
	 * @author hcj 
	 * @param mail
	 * @return 
	 * @date 2018年9月21日 下午7:26:29
	 */
	@RequestMapping("/viewOnlineMail")
	public ModelAndView viewOnlineMail(Mail mail) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/mail/viewMail");
		mail = mailService.getMailDetail(mail);
		mav.addObject("mail", mail);
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
	/**
	 * 查看发送邮件详情
	 * @author hcj 
	 * @param mail
	 * @return 
	 * @date 2018年9月26日 上午10:11:52
	 */
	@RequestMapping("/viewMail")
	public ModelAndView viewMail(Mail mail) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/mail/viewMail");
		mail = mailService.getMailDetailById(mail.getId());
		mav.addObject("mail", mail);
		mav.addObject("type", "sendMail");
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
	/**
	 * 添加邮箱页面
	 * @author hcj 
	 * @param mail
	 * @param type（标记是正常添加还是转发回复）
	 * @return 
	 * @date 2018年9月26日 下午3:01:10
	 */
	@RequestMapping("/addMailPage")
	public ModelAndView addMailPage(Mail mail,String type) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/mail/addMail");
		MailSet mailSet = new MailSet();
		mailSet.setUserId(userInfo.getId());
		List<MailSet> listMs = mailSetService.listMailSet(mailSet);
		//转发
		if(!CommonUtil.isNull(type) && "sendTurn".equals(type)) {
			mail = mailService.getMailDetailById(mail.getId());
			mail.setRecipients("");
		}else if(!CommonUtil.isNull(type) && "back".equals(type)){
			String body = "<br><br><br><br>-------原始邮件---------<br>发送人：";
			Mail newMail = mailService.getMailDetailById(mail.getId());
			body += newMail.getFromAddress()+"<br>发送时间："+(newMail.getSendTime()==null?"":newMail.getSendTime())+"<br>主题："+newMail.getSubject();
			mail.setBody(body);
			mail.setRecipients(newMail.getFromAddress());
			mail.setAccountId(newMail.getAccountId());
			mail.setSubject("回复："+newMail.getSubject());
		}
		mav.addObject("userInfo", userInfo);
		mav.addObject("listMs", listMs);
		mav.addObject("mail", mail);
		return mav;
	}
	
	/**
	 * 添加新邮件
	 * @author hcj 
	 * @param mail
	 * @return 
	 * @date 2018年9月25日 下午5:56:38
	 */
	@RequestMapping("/addMail")
	public ModelAndView addMail(Mail mail){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/mail/addMail");
		try {
			mailService.addMail(mail,userInfo);
			view.addObject("addState","1");
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
			view.addObject("addState","0");
		}
		return view;
	}
	
	/**
	 * 删除本地发送记录
	 * @author hcj 
	 * @param ids
	 * @param redirectPage
	 * @return 
	 * @date 2018年9月26日 下午2:19:40
	 */
	@RequestMapping("/delSendMail")
	public ModelAndView delSendMail(Integer[] ids,String redirectPage){
		UserInfo userInfo = this.getSessionUser();
		try {
			//删除消费记录
			mailService.delSendMail(userInfo,ids);
			this.setNotification(Notification.SUCCESS, "删除成功");
		} catch (Exception e) {
			this.setNotification(Notification.ERROR, "删除失败");
		}
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	/**
	 * 邮件同步
	 * @author hcj 
	 * @param mail
	 * @return 
	 * @date 2018年9月28日 上午10:52:20
	 */
	@ResponseBody
	@RequestMapping(value = "/addPulldata")
	public String addPulldata(Mail mail){
		UserInfo userInfo = this.getSessionUser();
		try {
			mailService.addPulldata(mail, userInfo);
			return "同步成功";
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
			return "同步失败";
		} 
	}
	
}