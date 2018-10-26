package com.westar.core.web.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.westar.base.cons.SessionKeyConstant;
import com.westar.base.model.JoinRecord;
import com.westar.base.model.JoinTemp;
import com.westar.base.model.Organic;
import com.westar.base.model.PassYzm;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.CusAccessObjectUtil;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.Encodes;
import com.westar.base.util.MessageSender;
import com.westar.base.util.PublicConfig;
import com.westar.base.util.StringUtil;
import com.westar.base.util.UUIDGenerator;
import com.westar.core.service.JiFenService;
import com.westar.core.service.LoginMacService;
import com.westar.core.service.OrganicService;
import com.westar.core.service.RegistService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.thread.MailSendThread;
import com.westar.core.web.PaginationContext;
import com.westar.core.web.SessionContext;

/**
 * 由于注册
 * @author H87
 *
 */
@Controller
public class RegistController extends BaseController {
	
	private static final Log loger = LogFactory.getLog(RegistController.class);

	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	RegistService registService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	JiFenService jifenService;
	
	@Autowired
	LoginMacService loginMacService;
	
	@Autowired
	OrganicService organicService;
	
	
	/**
	 * 用户邮箱激活验证
	 * @param session
	 * @param confirmId 确认码
	 * @return
	 */
	@RequestMapping("/registe")
	public ModelAndView registe(HttpSession session,String confirmId,HttpServletRequest request) {
		//操作IP
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		
		ModelAndView mav = new ModelAndView();
		if(null==confirmId||"".equals(confirmId)){
			mav.setViewName("redirect:/login.jsp");
			this.setNotification(Notification.ERROR, "链接失效,请直接登陆");
		}else{
			//链接的有效性判断
			JoinRecord joinRecord = userInfoService.justConfirmId(confirmId);
			//链接(已激活或失效)，跳转到登陆界面
			if(null==joinRecord||"1".equals(joinRecord.getState())){
				mav.setViewName("redirect:/login.jsp");
				this.setNotification(Notification.ERROR, "链接失效,请直接登陆");
			}else if("2".equals(joinRecord.getCheckState())){//已拒绝的账号
				mav.setViewName("redirect:/login.jsp");
				this.setNotification(Notification.ERROR, "链接失效,请直接登陆");
			}else{
				//是注册的
				if("0".equals(joinRecord.getJoinType())){
					mav.setViewName("redirect:/registe/finishInfoPage");
					
					mav.addObject("confirmId", confirmId);
				}else{
					//TODO 验证账号类型
					//验证系统中是否有该用户
					UserInfo userA = userInfoService.getUserInfoByAccount(joinRecord.getAccount().toLowerCase());
					//验证加入的企业是否有该账号
					UserInfo userTemp = userInfoService.getUserInfoByType(joinRecord.getAccount().toLowerCase(), joinRecord.getComId(),ConstantInterface.GET_BY_EMAIL);
					if(null==userA){//系统中该用户不存在
						mav.setViewName("redirect:/registe/finishInfoPage");
						mav.addObject("confirmId", confirmId);
					}else if(null!=userTemp){//账号已在该企业
						//删除加入记录
						userInfoService.delJoinRecord(joinRecord.getId());
						
						mav.setViewName("redirect:/login.jsp");
						this.setNotification(Notification.ERROR, "链接失效,请直接登陆");
					}else{//系统中已存在用户，但是在新的企业中没有，邮箱激活，直接跳转
						UserInfo user = new UserInfo();
						String account = joinRecord.getAccount();
						if(account.indexOf("@")>0){
							//用户的邮箱
							user.setEmail(account);
						}else{
							//用户联系方式
							user.setMovePhone(account);
						}
						//用户密码
						user.setPassword(userA.getPassword());
						//企业编号
						user.setComId(joinRecord.getComId());
						//用户姓名
						user.setUserName(userA.getUserName());
						//先完善用户信息
						userInfoService.finishInfo(user,joinRecord.getId(),joinRecord.getJoinType(),joinRecord.getUserId(),optIP);
						//后登陆
						UserInfo userInfo = userInfoService.userAuth(account.toLowerCase(), userA.getPassword(),joinRecord.getComId()+"");
						if (userInfo != null) {
							userInfo.setOptIP(optIP);
							String sid = this.setSessionObj(SessionKeyConstant.USER_CONTEXT, userInfo);
							SessionContext.removeSessionUser(userInfo.getId());
							SessionContext.addSessionUser(userInfo.getId(), sid, session);
							ModelAndView view = new ModelAndView("/registe/toIndex", "sid", sid);
							view.addObject("userInfo", userInfo);
							//邀请的账号 
							view.addObject("loginName", Encodes.encodeBase64(account).trim());
							
							return view;
						} else {
							this.setNotification(Notification.ERROR, "登录失败，请检查你的账号和密码。");
							return new ModelAndView("redirect:/index.jsp");
						}
					}
				}
			}
		}
		return mav;
	}
	/**
	 * 用户邮箱激活信息填写页面跳转
	 * @param confirmId
	 * @return
	 */
	@RequestMapping("/registe/finishInfoPage")
	public ModelAndView finishInfoPage(String confirmId,String userName) {
		ModelAndView mav = new ModelAndView();
		if(null==confirmId||"".equals(confirmId)){
			mav.setViewName("redirect:/login.jsp");
			this.setNotification(Notification.ERROR, "链接失效,请直接登陆");
		}else{
			//链接的有效性判断
			JoinRecord joinRecord = userInfoService.justConfirmId(confirmId);
			if(null==joinRecord){
				mav.setViewName("redirect:/login.jsp");
				this.setNotification(Notification.ERROR, "链接失效,请直接登陆");
				return mav;
			}
			UserInfo userInfo = userInfoService.getUserInfoByAccount(joinRecord.getAccount().toLowerCase());
			if(null==userInfo){//可以设置密码
				mav.addObject("status", "y");
			}else if(null== userInfo.getPassword()){
				mav.addObject("status", "y");
			}
			mav.setViewName("/registe/finishInfo");
			mav.addObject("joinRecord", joinRecord);
			mav.addObject("userInfo", userInfo);
			mav.addObject("userName", userName);
		}
		return mav;
	}
	/**
	  * 用户邮箱激活
	 * @param session
	 * @param user 新的用户信息
	 * @param recordId 加入记录ID
	 * @param joinType 加入类型
	 * @param confirmId 确认码
	 * @param userId 邀请人主键
	 * @param session
	 * @param yzm
	 * @param requestURI
	 * @param redirectPage
	 * @param account 使用账号
	 * @return
	 */
	@RequestMapping(value="/registe/finishInfo/{requestURI}",method=RequestMethod.POST)
	public ModelAndView finishInfo(HttpSession session,HttpServletRequest request,UserInfo user,Integer recordId,String joinType,
			String confirmId,Integer userId,String yzm,@PathVariable String requestURI,String redirectPage,String account) {
		//操作IP
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		//后台验证码
		String rand = (String) session.getAttribute(requestURI+"_rand");
		//清除验证码
		session.removeAttribute(requestURI+"_rand");
		//后台再次验证
		if(yzm.equalsIgnoreCase(rand)){
			UserInfo userA = userInfoService.getUserInfoByAccount(account.toLowerCase());
			if(null!=userA){
				//设置密码
				user.setPassword(userA.getPassword());
				//设置用户名
				user.setUserName(userA.getUserName());
				
			}else{
				//加密
				String passwordMD5 = Encodes.encodeMd5(user.getPassword());
				user.setPassword(passwordMD5);
			}
			if(account.indexOf("@")>0){
				//用户的邮箱
				user.setEmail(account);
			}else{
				//用户联系方式
				user.setMovePhone(account);
			}
			//先完善用户信息
			userInfoService.finishInfo(user,recordId,joinType,userId,optIP);
			//后登陆
			UserInfo userInfo = userInfoService.userAuth(account.toLowerCase(), user.getPassword(),user.getComId()+"");
			if (userInfo != null) {
				userInfo.setOptIP(optIP);
				String sid = this.setSessionObj(SessionKeyConstant.USER_CONTEXT, userInfo);
				SessionContext.removeSessionUser(userInfo.getId());
				SessionContext.addSessionUser(userInfo.getId(), sid, session);
				ModelAndView view = new ModelAndView("/registe/toIndex", "sid", sid);
				
				view.addObject("userInfo", userInfo);
				//邀请的账号 
				view.addObject("loginName", Encodes.encodeBase64(account).trim());
				
				return view;
			} else {
				this.setNotification(Notification.ERROR, "登录失败，请检查你的账号和密码。");
				return new ModelAndView("redirect:/index.jsp");
			}
		}else{
			this.setNotification(Notification.ERROR, "验证码过期!");
			return new ModelAndView("redirect:/registe/finishInfoPage?confirmId="+confirmId,"userName",user.getUserName());
		}
	}
	/**
	 * 加入邮箱用户验证后告知
	 * @param joinType 加入方式
	 * @param checkState 审核状态
	 * @return
	 */
	@RequestMapping("/registe/joinSuc")
	public ModelAndView joinSuc(JoinRecord joinrecord) {
		if(null!=joinrecord && null!=joinrecord.getJoinType() 
				&& "2".equals(joinrecord.getJoinType())){//加入的为邀请人员,可进入邮箱
			return new ModelAndView("/registe/joinSucInv");
		}else{
			return new ModelAndView("/registe/joinSuc");
		}
	}
	
	/**
	 * 验证邮箱密码
	 * @param param
	 * @param account
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/checkPassword")
	public Map<String, Object> checkPassword(String param,String account){
		Map<String, Object> map = new HashMap<String, Object>();
		//查看是否当前邮箱已经注册并设置了姓名和密码，若设置了姓名，则统一密码。
		UserInfo userInfo = userInfoService.getUserInfoByAccount(account.toLowerCase());
		String passwordMD5 = Encodes.encodeMd5(param);
		if(null==userInfo){//可以设置密码
			map.put("status", "y");
		}else if(null== userInfo.getPassword()){
			map.put("status", "y");
		}else if(passwordMD5.equals(userInfo.getPassword())){
			map.put("status", "y");
		}else{
			map.put("status", "f");
			map.put("info", "检测到已加入系统，请统一密码");
		}
		return map;
	}
	
	/**
	 * 用户加入企业跳转页面
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/registe/joinOrgPage")
	public ModelAndView joinOrgPage(JoinRecord joinRecord) {
		ModelAndView mav = new ModelAndView("/registe/joinOrg");
		return mav;
	}
	/**
	 * 登录注册跳转页面
	 * @param sid
	 * @return
	 */
	@RequestMapping("/registe/toIndexPage")
	public ModelAndView toIndexPage(String sid) {
		ModelAndView mav = new ModelAndView("/registe/toIndex");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		mav.addObject("sid", sid);
		return mav;
	}
	
	
	/**
	 * 验证码
	 * @param request
	 * @param param
	 * @param name
	 * @param requestURI
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/checkYzm/{requestURI}")
	public Map<String, Object> checkYzm(HttpServletRequest request,String param, String name, @PathVariable String requestURI) {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		String rand = (String) session.getAttribute(requestURI+"_rand");
		if(null==rand){
			map.put("status", "f");
			map.put("info", "验证码过期！");
		}else if (param.equalsIgnoreCase(rand)) {
			map.put("status", "y");
		} else {
			map.put("status", "f");
			map.put("info", "验证码输入错误！");
		}
		return map;
	}
	
	/**
	 * 显示图片用的
	 * @param request
	 * @param response
	 * @param requestURI
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/registe/AuthImage/{requestURI}")
	public ModelAndView authImage(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String requestURI)
			throws Exception {
		// 清空response
		response.reset();
		response.setHeader("Connection", "close");

		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// 表明生成的响应是图片
		response.setContentType("application/octet-stream");
		
		OutputStream out = response.getOutputStream();
        try {
        	BufferedImage img = new BufferedImage(110, 20,BufferedImage.TYPE_INT_RGB);  
			// 得到该图片的绘图对象    
			Graphics g = img.getGraphics();  
  
			Random r = new Random();  
  
			Color c = new Color(200, 150, 255);  
  
			g.setColor(c);  
  
			// 填充整个图片的颜色    
			g.fillRect(0, 0, 110, 20);  
  
			// 向图片中输出数字和字母    
			StringBuffer sRand = new StringBuffer();  
  
			char[] ch = "ABCDEFGHJKLNPRSTUVXYZ23456789".toCharArray();  
  
			int index, len = ch.length;  
  
			for (int i = 0; i < 4; i++) {  
  
			    index = r.nextInt(len);  
  
			    g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt  
  
			    (255)));  
  
			    g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));  
			    // 输出的  字体和大小                      
  
			    g.drawString("" + ch[index], (i * 25) + 8, 18);  
			    //写什么数字，在图片 的什么位置画    
  
			    sRand.append(ch[index]);  
			}
			HttpSession session = request.getSession(true);
			session.setAttribute(requestURI+"_rand", sRand.toString());
			g.dispose();
			ImageIO.write(img, "JPEG", out);
		} catch (Exception e) {
			throw e;
		} finally{
			if(null!=out){
				out.close();
			}
		}

		return null;
	}

	/**
	 * 用户加入企业
	 * @param session
	 * @param userInfo
	 * @param yzm
	 * @param requestURI
	 * @return
	 */
	@RequestMapping(value = "/registe/joinOrg/{requestURI}",method=RequestMethod.POST)
	public ModelAndView joinOrg(HttpSession session,HttpServletRequest request,JoinRecord joinRecord,String yzm,
			@PathVariable String requestURI,String redirectPage) {
		
		loger.info("暂未使用");
		ModelAndView mav = new ModelAndView("redirect:/registe/joinSuc");
		//后台验证码
		String rand = (String) session.getAttribute(requestURI+"_rand");
		//清除验证码
		session.removeAttribute(requestURI+"_rand");
		//去掉空格
		String account = StringUtil.trim(joinRecord.getAccount());
		joinRecord.setAccount(account);
		//后台再次验证
		if(yzm.equalsIgnoreCase(rand)){
			
			//后台再次验证企业的名称
			Organic org = organicService.getOrganicByNum(joinRecord.getComId());
			if(null==org){
				this.setNotification(Notification.ERROR, "企业不存在!");
				return new ModelAndView("redirect:/registe/joinOrgPage").addObject("account", joinRecord.getAccount());		
			}else{
				//判断用户是否有一个激活码，有就用原来的，没有就重新生成一个
				JoinRecord  obj =registService.getConfirmId(joinRecord.getAccount().toLowerCase(),joinRecord.getComId());
				
				//激活码
				String confirmId = UUIDGenerator.getUUID();
				//ip链接
				String url = PublicConfig.SERVER_URL.get("serverOutIP");  
				String basePath = request.getScheme()+"://"+url+":"+request.getServerPort();
				//邮件发送准备
				MessageSender sender = MessageSender.getMessageSender();
				//取得企业的管理人员
				List<UserInfo> shares = userInfoService.listOrgAdmin(joinRecord.getComId());
				UserInfo userInfo = new UserInfo();
				userInfo.setComId(joinRecord.getComId());
				userInfo.setId(0);
			
				if(null!=obj){//以前就有加入或是邀请记录
					if("1".equals(obj.getJoinType()) && !"0".equals(obj.getCheckState())){//申请加入已审核
						obj.setConfirmId(confirmId);
						obj.setCheckState("0");
						//加入留言
						obj.setJoinNote(joinRecord.getJoinNote());
						//更新加入记录
						registService.updateJoinRecord(obj,null);//修改确认码以及审核状态
						obj = null;
					}else{
						//若是申请加入的或是已经邀请的直接发邮件
						confirmId = obj.getConfirmId();
						obj.setRecordCreateTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
						//加入留言
						obj.setJoinNote(joinRecord.getJoinNote());
						registService.updateJoinRecord(obj,null);
					}
					//添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo,null, obj.getId(), "申请加入企业:"+joinRecord.getJoinNote(), 
							ConstantInterface.TYPE_APPLY, shares, null);
				}else{//没有加入记录
					//加入方式
					joinRecord.setJoinType(ConstantInterface.JOIN_APPLY.toString());
					//未激活
					joinRecord.setState("0");
					//激活码
					joinRecord.setConfirmId(confirmId);
					//未审核
					joinRecord.setCheckState("0");
					Integer id =  registService.addJoinRecord(joinRecord);
					
					//添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo,null, id, "申请加入企业:"+joinRecord.getJoinNote(), 
							ConstantInterface.TYPE_APPLY, shares, null);
				}
				
				if(obj!=null && "2".equals(obj.getJoinType())){//若此账号经过邀请了，可以直接激活
					//标题
					String subject ="申请加入企业"; 
					//内容
					String body = "<div>"+joinRecord.getAccount()+"您好！<div><br>"
					+"欢迎使用我们的系统！ <br>"
					+"企业("+org.getOrgName()+")管理员已邀请您加入系统 ,您可以点击以下链接激活账号<br>" 
					+ "<a href = "+basePath+"/registe?confirmId="+obj.getConfirmId()+">"+basePath+"/registe?confirmId="+obj.getConfirmId()+"</a><br>"
					+ "</a>" 
					+"(如果点击链接无反应，请复制链接到浏览器里直接打开)";
					//发送邮件
					new Thread(new MailSendThread(sender, joinRecord.getAccount(), subject, body)).start();
					//审核状态，或是邀请
					mav.addObject("checkState", "1");
					//加入方式，申请还是邀请
					mav.addObject("joinType",obj.getJoinType());
					//确认码
					mav.addObject("confirmId",confirmId);
					//邮件
					mav.addObject("email",joinRecord.getAccount());
				}else{
					//标题
					String subject ="申请加入企业"; 
					//内容
					String body = "<h1>申请加入"+org.getOrgName()+"<h1><br>"
						+joinRecord.getAccount()+"您好！ <br>"
						+"欢迎使用我们的系统！ <br>"
						+"您提交的申请加入"+org.getOrgName()+"，公司管理员将尽快审核，请耐心等候 <br>";
					//发送邮件
					new Thread(new MailSendThread(sender, joinRecord.getAccount(), subject, body)).start();
				}
				//用户加入注册信息邮件重发
				return mav;
		        
			}
		}else{
			this.setNotification(Notification.ERROR, "验证码过期!");
			return new ModelAndView("redirect:"+redirectPage).addObject("account", joinRecord.getAccount())
			.addObject("comId", joinRecord.getComId())
			.addObject("joinNote", joinRecord.getJoinNote());		
		}
	}
	
	
	/**
	 * 验证企业是否存在，再验证这个企业是否已有这个账号(没有使用)
	 * @param param
	 * @param email
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/checkOrgNum")
	public Map<String, Object> checkOrgNum(Integer comId,String email){
		loger.info("暂未使用");
		
		Map<String, Object> map = new HashMap<String, Object>();
		Organic org = organicService.getOrganicByNum(comId);
		if(null==org){
			map.put("status", "f");
			map.put("info", "该企业不存在！");
		}else{
			email = StringUtil.trim(email);
			//用户信息
			UserInfo user = userInfoService.getUserInfoByType(email.toLowerCase(), comId,ConstantInterface.GET_BY_EMAIL);
			//用户信息不为空
			if(null!=user){
				if("0".equals(user.getEnabled())){//账号禁用
					map.put("status", "f");
					map.put("info", "该账号已被该企业禁用！");
				}else{
					map.put("status", "f");
					map.put("info", "该账号已在该企业！");
				}
			}else{
				map.put("status", "y");
			}
		}
		return map;
	}
	
	/**
	 * 登录（此方法没有使用）
	 * @param session
	 * @param email
	 * @param password
	 * @param comId
	 * @return
	 */
	@RequestMapping("/registe/login")
	public ModelAndView login(HttpSession session, String email, String password,String comId,String admin) {
		loger.info("暂未使用");
//		String passwordMD5 = Encodes.encodeMd5(password);
//		UserInfo userInfo = userInfoService.userAuth(email, passwordMD5,comId);
//		if (userInfo != null) {
//			String sid = this.setSessionObj(SessionKeyConstant.USER_CONTEXT, userInfo);
//			SessionContext.removeSessionUser(userInfo.getId());
//			SessionContext.addSessionUser(userInfo.getId(), sid, session);
			ModelAndView view = new ModelAndView("/registe/login");
//			view.addObject("userInfo", userInfo);
//			//用户主键
//			view.addObject("id", Encodes.encodeBase64(userInfo.getId().toString()).trim());
//			view.addObject("autoLogin", autoLogin);
//			
//			//设置了保存登录
//			if("1".equals(autoLogin)){
//				//登录名称
//				view.addObject("loginName", Encodes.encodeBase64(userInfo.getEmail()).trim());
//				//企业号
//				view.addObject("comId", Encodes.encodeBase64(userInfo.getComId().toString()).trim());
//				//用户加密后的密码
//				view.addObject("password", Encodes.encodeBase64(userInfo.getPassword()).trim());
//			}
//			
//			view.addObject("status", "y");
//			return view;
//		} else {
//			this.setNotification(Notification.ERROR, "登录失败，请检查你的账号和密码。");
//			return new ModelAndView("redirect:index.jsp");
//		}
		return view;
	}
	
	
	/*******************找回密码********************/
	
	/**
	 * 用户找回密码信息填写页面
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/registe/updatePasswordPage")
	public ModelAndView findPasswordPage() {
		ModelAndView mav = new ModelAndView("/registe/updatePasswordPage");
		return mav;
	}
	/**
	 * 企业是否存在验证的邮箱用户
	 * @param param
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/checkAccountForFindPass")
	public Map<String, Object> checkAccountForFindPass(String param){
		
		Map<String, Object> map = new HashMap<String, Object>();
		//去掉空格
		String account = StringUtil.trim(param);
		UserInfo user = userInfoService.checkInputAccount(account.toLowerCase());//检测帐号是否注册
		if(null==user){//没有用户
			map.put("status", "f");
			map.put("info", "账号不存在！");
		}else{
			map.put("status", "y");
		}
		return map;
	}
	/**
	 *  验证修改密码的账号
	 * @param session
	 * @param request
	 * @param email 账号
	 * @param yzm 验证码
	 * @param requestURI
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/checkAccount/{requestURI}",method=RequestMethod.POST)
	public Map<String, Object> checkAccount(HttpSession session,HttpServletRequest request,String account,String yzm,
			@PathVariable String requestURI) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==account || "".equals(account.trim())){
			map.put("status", "f");
			map.put("info", "验证帐号不能为空！");
		}
		//后台验证码
		String rand = (String) session.getAttribute(requestURI+"_rand");
		//清除验证码
		session.removeAttribute(requestURI+"_rand");
		//后台再次验证
		if(yzm.equalsIgnoreCase(rand)){
			UserInfo user = userInfoService.getUserInfoByAccount(account.toLowerCase());
			if(null==user){
				map.put("status", "f");
				map.put("info", "账号不存在！");
			}else{
				map.put("status", "y");
			}
		}else{
			map.put("status", "f");
			map.put("info", "验证码失效！");
		}
		return map;
		
	}
	/**
	 * 邮箱验证码
	 * @param length
	 * @return
	 */
	@SuppressWarnings("unused")
	private String passYzm(int length){
		Integer key = new CommonUtil().randomNum(4);
        if(registService.passYzmCheck(Integer.parseInt(key.toString()))){
        	return this.passYzm(length);
        }else{
        	return key.toString();
        }
	}
	
	/**
	 * 验证邮箱验证码
	 * @param request
	 * @param param
	 * @param account
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/checkPassYzm")
	public Map<String, Object> checkPassYzm(HttpServletRequest request,String param,String account) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==account || "".equals(account.trim())){
			this.setNotification(Notification.ERROR, "验证帐号为空，请联系管理员。");
			map.put("status", "f");
			map.put("info", "验证帐号为空，请联系管理员。");
			return map;
		}
		PassYzm passYzm = null;
		UserInfo curUser = userInfoService.getUserInfoByAccount(account);//获取当前操作人信息
		if(CommonUtil.isEmail(account)){
			passYzm = registService.getPassYzm(curUser.getEmail());
		}else if(CommonUtil.isPhone(account)){
			passYzm = registService.getPassYzm(curUser.getMovePhone());
		}
		
		if(null==passYzm ){
			map.put("status", "f");
			map.put("info", "验证码失效！");
			
		}else if (passYzm.getEnabled()==0) {//验证码过期
			map.put("status", "f");
			map.put("info", "验证码过期！");
		}else if(param.equalsIgnoreCase(passYzm.getPassYzm())) {
			map.put("status", "y");
		} else {
			map.put("status", "f");
			map.put("info", "验证码输入错误！");
		}
		return map;
	}
	/**
	 * 获取权限验证码 
	 * @param account 获取帐号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/sendPassYzmAccount")
	public Map<String, Object> sendPassYzmAccount(String account){
		if(null==account || "".equals(account.trim())){
			Map<String, Object> map = new HashMap<String, Object>();
			//告知方式未指定
			map.put("status", "n");
			map.put("info", "帐号不能为空！");
			return map;
		}else{
			Map<String, Object> map = null;
			UserInfo curUser = userInfoService.getUserInfoByAccount(account);//获取当前操作人信息
			curUser.setComId(-1);//默认为系统免费短信
			if(CommonUtil.isEmail(account)){
				map = userInfoService.doSendPassYzm(curUser,"email");
			}else if(CommonUtil.isPhone(account)){
				map = userInfoService.doSendPassYzm(curUser,"phone");
			}else{
				map = new HashMap<String, Object>();
				//告知方式未指定
				map.put("status", "n");
				map.put("info", "帐号不正确！");
			}
			return map;
		}
	}
	
	/**
	 * 安全验证，是否找到邮箱的验证码
	 * @param email
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/sendAccountForAqyz")
	public Map<String, Object> sendAccountForAqyz(PassYzm passYzm){
		Map<String, Object> map = new HashMap<String, Object>();
		//再次判断用户是否存在
		UserInfo user = userInfoService.getUserInfoByAccount(passYzm.getAccount().toLowerCase());
		if(null==user){
			map.put("status", "f");
			map.put("info", "账号不存在！");
		}else{
			//判断验证码的有效性
			PassYzm passYzmObj = registService.getPassYzm(passYzm.getAccount().toLowerCase());
			if(null==passYzmObj || !passYzmObj.getPassYzm().equals(passYzmObj.getPassYzm())){
				map.put("status", "f");
				map.put("info", "验证码失效！");
			}else{
				map.put("status", "y");
			}
			
		}
		return map;
	}
	/**
	 * 修改密码
	 * @param account
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/updatePassword")
	public Map<String, Object> updatePassword(String account,String password){
		Map<String, Object> map = new HashMap<String, Object>();
		//再次判断用户是否存在
		UserInfo user = userInfoService.getUserInfoByAccount(account.toLowerCase());
		if(null==user){
			map.put("status", "f");
			map.put("info", "账号不存在！");
		}else{
			//判断验证码的有效性
			PassYzm passYzmObj = registService.getPassYzm(account.toLowerCase());
			if(null!=passYzmObj){//删除验证码
				userInfoService.delPassYzmById(passYzmObj.getId());
			}
			user.setPassword(Encodes.encodeMd5(password));
			// 通过帐号修改用户
			userInfoService.updateUserInfoByAccount(user);
			map.put("status", "y");
		}
		return map;
	}
	
	
	/**************************用户申请加入*********************************/
	
	/**
	 * 用户创建企业跳转页面
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/registe/registeInfoPage")
	public ModelAndView registeInfoPage(JoinTemp joinTemp) {
		ModelAndView mav = new ModelAndView("/registe/registeInfo");
		return mav;
	}
	/**
	 * 验证邮箱用户企业注册
	 * @param account 在界面上需要先判断account的正则
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/checkAccount",method=RequestMethod.POST)
	public Map<String, Object> checkAccount(String account){
		
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = new UserInfo();
		if(account.indexOf("@")>0){//使用邮箱注册的
			user = userInfoService.getUserInfoByAccount(account);
		}else{//使用手机注册的
			user = userInfoService.getUserInfoByAccount(account);
		}
		map.put("user", user);
		return map;
	}
	/**
	 * 验证邮箱用户企业注册
	 * @param account 在界面上需要先判断account的正则
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/checkRegistPasswd",method=RequestMethod.POST)
	public Map<String, Object> checkRegistPasswd(String account,String passwd){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user =  userInfoService.getUserInfoByAccount(account.toLowerCase());
		//默认输入密码错误
		map.put("status", "f");
		if(null!=user){
			String passwordMD5 = Encodes.encodeMd5(passwd);
			if(passwordMD5.equals(user.getPassword())){//两次密码相同，输入正确
				map.put("status", "y");
			}
		}
		return map;
	}
	
	/**
	 * 加入临时用户
	 * @param session
	 * @param request
	 * @param password
	 * @param account
	 * @param userName
	 * @param yzm
	 * @param requestURI
	 * @param redirectPage
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/registeOrg/{requestURI}",method=RequestMethod.POST)
	public Map<String,Object> registeOrg(HttpSession session,HttpServletRequest request,
			String password,String account,String userName,
			String yzm,@PathVariable String requestURI,String redirectPage) {
		//操作IP
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		
		Map<String,Object> map = new HashMap<String, Object>();
		//后台随机码
		String rand = (String) session.getAttribute(requestURI+"_rand");
		//清除随机码
		session.removeAttribute(requestURI+"_rand");
		
		//后台再次验证
		if(yzm.equalsIgnoreCase(rand)){
			String passwordMD5 = Encodes.encodeMd5(password);
			//向后台插入数据，并生成验证码
			UserInfo user = null;
			if(account.indexOf("@")>0){//使用邮箱注册的
				user = userInfoService.getUserInfoByAccount(account);
			}else{//使用手机注册的
				user = userInfoService.getUserInfoByAccount(account);
			}
			//是否为系统人员
			boolean isSysUser = false;
			if(null!=user){//系统有该账号
				isSysUser = true;
				//人员名称
				userName = user.getUserName();
				if(!user.getPassword().equals(passwordMD5)){
					map.put("status", "f1");
					return map;//密码输入错误!
				}
				map.put("isSysUser", "yes");
			}
			Integer joinTempId =  registService.addJoinTemp(account.toLowerCase(),userName,passwordMD5,isSysUser);
			map.put("status", "y");
			map.put("account", account);
			map.put("joinTempId", joinTempId);
			
			if(!isSysUser){//不是系统用户先发邮件
				registService.addRegistYzm(account,joinTempId,optIP);
			}
			return map;
			
		}else{
			map.put("status", "f2");//验证码过期!
			return map;
		}
	}
	/**
	 * 账号验证界面
	 * @param account 账号
	 * @param joinTempId 用户临时表的主键
	 * @return
	 */
	@RequestMapping("/registe/registeCheckPage")
	public ModelAndView registeCheckPage(String account,Integer joinTempId) {
		ModelAndView mav = new ModelAndView("/registe/registeCheck");
		JoinTemp joinTemp = registService.getJoInTempById(joinTempId);
		if(null!=joinTemp){
			//账号小写
			account = account.toLowerCase();
			
			String accountStr = joinTemp.getAccount().toLowerCase();
			if(!account.equals(accountStr)){
				//TODO 需要处理
			}
			mav.addObject("account",account);
			mav.addObject("joinTempId",joinTempId);
		}else{
			//TODO 需要处理
		}
		return mav;
	}
	
	/**
	 * 发送注册验证码
	 * @param account
	 * @param joinTempId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/sendRegistYzm")
	public Map<String, Object> sendRegistYzm(HttpServletRequest request,String account,Integer joinTempId){
		//操作IP
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		
		Map<String, Object> map = registService.addRegistYzm(account,joinTempId,optIP);
		return map;
	}
	/**
	 * 验证验证码
	 * @param account 账号
	 * @param joinTempId 用户临时表的主键
	 * @param yzm 用户验证码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/checkRegistYzm")
	public Map<String, Object> checkRegistYzm(String account,Integer joinTempId,String yzm) {
		Map<String, Object> map = new HashMap<String, Object>();
		JoinTemp joinTemp = registService.getJoInTempById(joinTempId);
		if(null==joinTemp){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//账号小写
		account = account.toLowerCase();
		
		String accountStr = joinTemp.getAccount().toLowerCase();
		if(!account.equals(accountStr)){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//链接的有效性判断
		PassYzm passYzmObj = registService.getPassYzm(account);
		
		if(null == passYzmObj){//验证码不存在
			map.put("status", "f");
			map.put("info", "验证码已失效，请重新获取！");
		}else if( passYzmObj.getEnabled()==0){//验证码过期
			map.put("status", "f");
			map.put("info", "验证码已失效，请重新获取！");
		}else{
			if(passYzmObj.getPassYzm().equals(yzm)){
				//删除验证码
				userInfoService.delPassYzmById(passYzmObj.getId());
				map.put("status", "y");
			}else{
				map.put("status", "f");
				map.put("info", "验证码错误！");
			}
		}
		return map;
	}
	/**
	 * 注册的第三步
	 * @param account 账号
	 * @param joinTempId 用户临时表主键
	 * @return
	 */
	@RequestMapping("/registe/registeWayPage")
	public ModelAndView registeWay(String account,Integer joinTempId) {
		ModelAndView mav = new ModelAndView("/registe/registeWay");
		JoinTemp joinTemp = registService.getJoInTempById(joinTempId);
		if(null==joinTemp){
			//TODO 需要处理
		}else{
			//账号小写
			account = account.toLowerCase();
			
			String accountStr = joinTemp.getAccount().toLowerCase();
			if(!account.equals(accountStr)){
				//TODO 需要处理
			}
			account = account.toLowerCase();
			mav.addObject("joinTemp",joinTemp);
			mav.addObject("isCreator",false);
			
			//查询该账号的所加入的所有团队
			List<Organic> listUserAllOrg = organicService.listUserAllOrg(account);
			if(null!=listUserAllOrg && !listUserAllOrg.isEmpty()){
				String isSysUser = listUserAllOrg.get(0).getIsSysUser();
				if(isSysUser.equals(ConstantInterface.USER_ROLE_MANAGER_SUP)){//是超级管理员
					mav.addObject("isCreator",true);
				}
			}
			mav.addObject("listUserAllOrg",listUserAllOrg);
			
		}
		return mav;
	}
	
	/**
	 * 创建团队
	 * @param account 账号
	 * @param joinTempId 用户临时表
	 * @param orgName 企业名称
	 * @param session 当前session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/createOrg")
	public Map<String, Object> createOrg(HttpSession session,HttpServletRequest request,String account,Integer joinTempId,String orgName) {
		Map<String, Object> map = new HashMap<String, Object>();
		//取得
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		JoinTemp joinTemp = registService.getJoInTempById(joinTempId);
		if(null==joinTemp){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//账号小写
		account = account.toLowerCase();
		
		String accountStr = joinTemp.getAccount().toLowerCase();
		if(!account.equals(accountStr)){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//开始注册
		UserInfo userInfo = registService.addOrgAndUser(joinTemp,orgName,optIP);
		userInfo.setOptIP(optIP);
		String sid = this.setSessionObj(SessionKeyConstant.USER_CONTEXT, userInfo);
		SessionContext.removeSessionUser(userInfo.getId());
		SessionContext.addSessionUser(userInfo.getId(), sid, session);
		
		//登录名
		map.put("account", Encodes.encodeBase64(account).trim());
		map.put("sid", sid);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 查询加入企业名称的模糊查询
	 * @param account
	 * @param joinTempId
	 * @param searchName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/searchOrgList",method=RequestMethod.POST)
	public Map<String,Object> searchOrgList(String account,Integer joinTempId,String searchName){
		Map<String, Object> map = new HashMap<String, Object>();
		JoinTemp joinTemp = registService.getJoInTempById(joinTempId);
		if(null==joinTemp){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//账号小写
		account = account.toLowerCase();
		
		String accountStr = joinTemp.getAccount().toLowerCase();
		if(!account.equals(accountStr)){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		Integer pageNum = 8;
		//一次加载行数
		PaginationContext.setPageSize(pageNum);
		//取得查询企业名称
		List<Organic> orgList = organicService.listSearchOrg(account,searchName);
		Integer resultCount = PaginationContext.getTotalCount();
		
		map.put("status", "y");
		map.put("orgList", orgList);
		if(null!=orgList && resultCount>pageNum){
			map.put("resultMore", "yes");
		}else{
			map.put("resultMore", "no");
		}
		return map;
	}
	/**
	 * 申请加入或取消申请加入团队
	 * @param account 账号
	 * @param joinTempId 用户临时表主键
	 * @param comId 申请的团队
	 * @param applyState 0申请加入 1取消申请
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/applyInOrg",method=RequestMethod.POST)
	public Map<String,Object> applyInOrg(String account,Integer joinTempId,Integer comId,Integer applyState){
		Map<String, Object> map = new HashMap<String, Object>();
		JoinTemp joinTemp = registService.getJoInTempById(joinTempId);
		if(null==joinTemp){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//账号小写
		account = account.toLowerCase();
		
		String accountStr = joinTemp.getAccount().toLowerCase();
		if(!account.equals(accountStr)){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//后台再次验证企业的名称
		Organic org = organicService.getOrganicByNum(comId);
		if(null==org){
			map.put("status", "f");
			map.put("info","企业不存在!");
		}else{
			map.putAll(registService.updateJoinTempType(account,comId,applyState,joinTemp));
		}
		
		return map;
	}
	/**
	 * 登陆企业
	 * @param account 账号
	 * @param joinTempId 用户临时表主键
	 * @param comId 企业号
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/redirectLogIn",method=RequestMethod.POST)
	public Map<String,Object> redirectLogIn(String account,Integer joinTempId,Integer comId,
			HttpSession session,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		JoinTemp joinTemp = registService.getJoInTempById(joinTempId);
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		if(null==joinTemp){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//账号小写
		account = account.toLowerCase();
		
		String accountStr = joinTemp.getAccount().toLowerCase();
		if(!account.equals(accountStr)){
			map.put("status", "f");
			map.put("info", "页面已失效，请重新注册！");
			return map;
		}
		//后台再次验证企业的名称
		Organic org = organicService.getOrganicByNum(comId);
		if(null==org){
			map.put("status", "f");
			map.put("info","企业不存在!");
		}else{
			//开始注册
			UserInfo userInfo = userInfoService.userAuth(account, joinTemp.getPasswd(),comId+"");
			if(null!=userInfo){
				userInfo.setOptIP(optIP);
				String sid = this.setSessionObj(SessionKeyConstant.USER_CONTEXT, userInfo);
				SessionContext.removeSessionUser(userInfo.getId());
				SessionContext.addSessionUser(userInfo.getId(), sid, session);
				map.put("sid", sid);
				map.put("status", "y");
			}else{
				map.put("status", "f");
				map.put("info","账号失效!");
			}
		}
		map.put("status", "y");
		
		return map;
	}
	/**
	 * 拒绝加入团队
	 * @param account
	 * @param comId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/rejectInOrg",method=RequestMethod.POST)
	public Map<String,Object> rejectInOrg(String account,Integer comId){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("status", "y");
		registService.rejectInOrg(account,comId,"yes");
		
		return map;
	}
	/**
	 * 激活邀请码并加入团队
	 * @param confirmCodeParam 邀请码
	 * @param comId 团队号
	 * @param joinTempId 临时数据主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/registe/agreeInOrg",method=RequestMethod.POST)
	public Map<String,Object> agreeInOrg(HttpServletRequest request,HttpSession session,String confirmCodeParam,Integer comIdParam,
			Integer joinTempId){
		Map<String,Object> map = new HashMap<String, Object>();
		//操作IP
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		map = registService.agreeInOrg(confirmCodeParam,comIdParam,joinTempId,null,optIP);
		
		UserInfo userInfo = (UserInfo) map.get("userInfo");
		if(null != userInfo){
			userInfo.setOptIP(optIP);
			//缓存用户信息
			String sid = this.setSessionObj(SessionKeyConstant.USER_CONTEXT, userInfo);
			SessionContext.removeSessionUser(userInfo.getId());
			SessionContext.addSessionUser(userInfo.getId(), sid, session);
			
			map.put("sid",sid);
			Integer count = userInfoService.countUserIn(userInfo.getId());
			map.put("userId",userInfo.getId());
			map.put("count",count);
			map.put("userInfo",userInfo);
		}
		return map;
	}
	/**
	 * 完善邀请人员的信息
	 * @param joinTemp
	 * @return
	 */
	@RequestMapping(value="/registe/finishInvInfoPage")
	public ModelAndView finishInvInfoPage(Integer joinTempId){
		ModelAndView mav = new ModelAndView("/registe/finishInvInfo");
		
		JoinTemp joinTemp = registService.getJoInTempById(joinTempId);
		Organic organic = organicService.getOrganicByNum(joinTemp.getComId());
		mav.addObject("organic", organic);
		mav.addObject("joinTemp", joinTemp);
		return mav;
	}
	

}