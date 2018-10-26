package com.westar.core.web.controller;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.OrgCfgConstant;
import com.westar.base.cons.SessionKeyConstant;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.JoinTemp;
import com.westar.base.model.Organic;
import com.westar.base.model.OrganicCfg;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.CusAccessObjectUtil;
import com.westar.base.util.Encodes;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.service.BandMACService;
import com.westar.core.service.JiFenService;
import com.westar.core.service.OrganicService;
import com.westar.core.service.RecycleBinService;
import com.westar.core.service.RegistService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TaskService;
import com.westar.core.service.UserInfoService;
import com.westar.core.thread.OrganicServiceCheckThread;
import com.westar.core.web.SessionContext;

@Controller
public class LoginController extends BaseController {

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	SystemLogService systemLogService;

	@Autowired
	TaskService taskService;

	@Autowired
	RecycleBinService recycleBinService;

	@Autowired
	OrganicService organicService;

	@Autowired
	JiFenService jifenService;
	
	@Autowired
	RegistService registService;
	
	@Autowired
	BandMACService bandMACService;

	/**
	 * 登录
	 * @param session 浏览器session
	 * @param loginName 账号
	 * @param isSysUser 是否为已有的系统成员 1是0否
	 * @param password 密码
	 * @param comId 团队号
	 * @param request 本次访问请求
	 * @param cookieType 缓存类型
	 * @param yzm 验证码
	 * @param autoLogin 是否为自动登录
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView login(HttpSession session, String loginName,String isSysUser,
			String password, String comId, HttpServletRequest request,
			String cookieType,String yzm,String autoLogin) {
		//操作的IP
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		
		String passwordMD5 = password;
		// 没有采用客户端数据
		if (StringUtil.isBlank(cookieType) || !"no".equals(cookieType)) {
			String rand = (String) session.getAttribute("login_rand");
			if(null!=rand && null!=yzm && yzm.equalsIgnoreCase(rand) ){
				session.removeAttribute("login_rand");
			}else{
				this.setNotification(Notification.ERROR, "验证码失效，请重新登录");
				return new ModelAndView("redirect:/login.jsp");
			}
			passwordMD5 = Encodes.encodeMd5(password);
		}
		if(null != isSysUser && "0".equals(isSysUser)){//是受到邀请成员
			ModelAndView view = new ModelAndView("/toIndex");
			//判断用户是否被邀请过
			JoinTemp joinTemp = registService.getJoInTempByAccount(loginName,Integer.parseInt(comId),ConstantInterface.JOIN_INVITE);
			if(null == joinTemp){
				registService.rejectInOrg(loginName,Integer.parseInt(comId),null);
				this.setNotification(Notification.ERROR, "邀请码失效，请联系管理员重新邀请");
				return new ModelAndView("redirect:/login.jsp");
			}else{
				view.addObject("joinTemp", joinTemp);
				view.addObject("isSysUser", "0");
			}
			
			return view;
		}else{//是已有的成员系统
			final UserInfo userInfo = userInfoService.userAuth(loginName
					.toLowerCase(), passwordMD5, comId);
			if (userInfo != null && userInfo.getInService()==ConstantInterface.USER_INSERVICE_YES) {
				ExecutorService pool = ThreadPoolExecutor.getInstance();
				pool.execute(new OrganicServiceCheckThread(organicService,Integer.parseInt(comId)));//对团队可使用配置验证
				//设置操作IP
				userInfo.setOptIP(optIP);
				
				String sid = this.setSessionObj(SessionKeyConstant.USER_CONTEXT,
						userInfo);
				SessionContext.removeSessionUser(userInfo.getId());
				SessionContext.addSessionUser(userInfo.getId(), sid, session);
				ModelAndView view = new ModelAndView("/toIndex", "sid", sid);
				view.addObject("userInfo", userInfo);
				
				view.addObject("autoLogin", autoLogin);
				// 用户主键
				view.addObject("id", Encodes.encodeBase64(
						userInfo.getId().toString()).trim());
				// 登录名称
				view.addObject("loginName", Encodes.encodeBase64(loginName).trim());
				
				if (StringUtil.isBlank(cookieType) || !"no".equals(cookieType)) {//没有采用客户端本地保存的数据
					//设置了保存登录
					if("yes".equals(autoLogin)){
						// 企业号
						view.addObject("comId", Encodes.encodeBase64(comId).trim());
						//自动登录加密
						view.addObject("autoLoginP", Encodes.encodeBase64("yes").trim());
						//自动登录没有加密
						view.addObject("autoLogin", "yes");
						// 用户加密后的密码
						view
						.addObject("password", Encodes.encodeBase64(passwordMD5)
								.trim());
						
					}else{
						//不自动登录
						view.addObject("autoLoginP", Encodes.encodeBase64("no").trim());
					}
				}
				//若是自动登录的，则不用再重新设置客户端保存本地的数据
				view.addObject("cookieType", cookieType);
				// 删除回收箱中超过三天的数据(数据量不大，不需要开启新的线程)
				new Thread(new Runnable() {
					public void run() {
						try {
							recycleBinService.delAllOverTri(userInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}).start();
				// 删除自己的无效任务
				taskService.delUnusedTask(userInfo);
				// 添加日志记录
				systemLogService.addSystemLog(userInfo.getId(), userInfo
						.getUserName(), "进入平台", ConstantInterface.TYPE_LOGIN,
						userInfo.getComId(),optIP);
				// 每日登录积分
				jifenService.addJifen(userInfo.getComId(), userInfo.getId(),
						ConstantInterface.TYPE_LOGIN, "每日登录系统", 0);
				
				//判断是否需要开启上下级关系
				OrganicCfg organicCfg = organicService.getOrganicCfg(userInfo.getComId(),OrgCfgConstant.LEADERCFG);
				if(null == organicCfg || ConstantInterface.MOD_OPT_STATE_YES.equals(organicCfg.getCfgValue())){//默认开启需要进行上下级验证
					//部门
					Integer depId = userInfo.getDepId();
					
					//获取个人直属上级集合
					List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(userInfo);
					if(null==userInfo.getIsChief() || "0".equals(userInfo.getIsChief()) && (
							null==listImmediateSuper || listImmediateSuper.isEmpty()
							|| null==depId || depId==0
							)){//不是老板。没有设定上下级
						view.addObject("addInfo", "y");
					}else{
						view.addObject("addInfo", "n");
					}
				}else{//团队设定时没有上下级
					view.addObject("addInfo", "n");
				}
				view.addObject("isSysUser", ConstantInterface.USER_INSERVICE_YES.toString());
				return view;
			}else if(userInfo != null && userInfo.getInService()==ConstantInterface.USER_INSERVICE_NO){
				this.setNotification(Notification.ERROR, "登录失败，你的账号超出团队人数上限，请联系你们的团队管理员。");
				return new ModelAndView("redirect:/login.jsp");
			} else {
				this.setNotification(Notification.ERROR, "登录失败，请检查你的账号和密码。");
				return new ModelAndView("redirect:/login.jsp");
			}
		}

	}

	/**
	 * 注销
	 * 
	 * @return
	 */
	@RequestMapping("/exit")
	public ModelAndView exit(HttpSession session, String sid) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("redirect:/logout.jsp");
		if(null!=userInfo){
			String optIP = userInfo.getOptIP();
			// 添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo
					.getUserName(), "退出平台", ConstantInterface.TYPE_LOGIN,
					userInfo.getComId(),optIP);
			try {
				this.logOut(sid);
			} catch (Exception e) {
			}
		}
		view.addObject("sid", sid);
		return view;
	}
	/**
	 * 注销
	 * 
	 * @return
	 */
	@RequestMapping("/login/logout4MAC")
	public ModelAndView logout4MAC(HttpSession session, String sid) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("redirect:/logout4MAC.jsp");
		if(null!=userInfo){
			String optIP = userInfo.getOptIP();
			// 添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo
					.getUserName(), "退出平台", ConstantInterface.TYPE_LOGIN,
					userInfo.getComId(),optIP);
			try {
				this.logOut(sid);
			} catch (Exception e) {
			}
		}
		view.addObject("sid", sid);
		return view;
	}

	/**
	 * 跳转注册页面(此方法暂是不用)
	 * 
	 * @return
	 */
	@RequestMapping("/login/newUser")
	public ModelAndView newUser() {
		return new ModelAndView("redirect:/newUser.jsp");
	}
	/**
	 * 检测系统中设置了自动登录后选择企业的情况
	 * @param request
	 * @param l_c_i_p_m
	 *            加密后的登录名称
	 * @param c_i_p_m_l
	 *            加密后的企业号
	 * @param i_p_m_l_c
	 *            加密后的用户主键
	 * @param p_m_l_c_i
	 *            加密后的密码
	 * @param autologin
	 *            設置自動登錄沒有
	 * @return
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/login/checkCookies")
	public Map<String, Object> checkCookies(HttpServletRequest request,
			String l_c_i_p_m, String c_i_p_m_l, String i_p_m_l_c,
			String p_m_l_c_i,String autologin) throws SocketException, UnknownHostException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != l_c_i_p_m && null != c_i_p_m_l && null != i_p_m_l_c && null!=autologin) {
			if(!Encodes.decodeBase64(autologin).equalsIgnoreCase("yes")){//没有设置自动登录
				map.put("state", "n");
				return map;
			}
			
			Map<String, Object> mapCheck = bandMACService.macValidatCheck();
			if(!mapCheck.get("status").equals("y")){
				map.put("state", "valiDate");
				return map;
			}
			
			// 邮箱或是别名
			String account = Encodes.decodeBase64(l_c_i_p_m);
			// 企业号
			String comId = Encodes.decodeBase64(c_i_p_m_l);
			// 用户主键
			String id = Encodes.decodeBase64(i_p_m_l_c);
			// 加密后的密码
			String password = Encodes.decodeBase64(p_m_l_c_i);
			
			UserInfo userInfo = userInfoService.getUserInfoById(id);
			// 查询用户所在的激活企业
			List<Organic> listOrg = new ArrayList<Organic>();
			if(null!=userInfo){//用户允许自动登录
				listOrg = organicService.listUserOrg(account.toLowerCase(),
						password);
			}

			if (null != listOrg && listOrg.size() > 0) {
				map.put("list", listOrg);

				map.put("email", account);
				map.put("comId", comId);
				map.put("password", password);
				map.put("cookieType", "no");

				map.put("state", "y");
			} else {
				map.put("state", "n");
			}

		}

		return map;
	}

	/**
	 * 验证账号是否存在，以及密码是否正确
	 * 
	 * @param email
	 *            账号
	 * @param password
	 *            密码
	 * @return 用户账号已启用的企业
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	@ResponseBody
	@RequestMapping(value = "/login/listOrg")
	public Map<String, Object> listOrg(HttpSession session,String email, String passwordMD5,String yzm)
			throws SocketException, UnknownHostException {
		Map<String, Object> map = new HashMap<String, Object>();
		String rand = (String) session.getAttribute("login_rand");
		if(null==rand  ){//验证码失效
			map.put("status", "n");
			map.put("info", "验证码失效！");
			map.put("input", "yzmF");
			return map;
		}else if(!yzm.equalsIgnoreCase(rand) ){//验证码失效或错误
			map.put("status", "n");
			map.put("info", "验证码输入错误！");
			map.put("input", "yzmF");
			return map;
		}
		
		Map<String, Object> mapCheck = bandMACService.macValidatCheck();
		if(!mapCheck.get("status").equals("y")){
			map.put("status", "n");
			map.put("input", "licDate");
			return map;
		}
		

		// 查询用户所在的激活企业
		List<Organic> listOrg = organicService.listUserOrg(email.toLowerCase(),
				passwordMD5);
		if (null == listOrg || listOrg.size() == 0) {// 没有激活的
			UserInfo user = userInfoService.getUserInfoByAccount(email
					.toLowerCase());
			if (null == user) {//没有系统用户
				//查询临时用户的集合
				List<JoinTemp> joinTemps = registService.getJoInTemp4Login(email.toLowerCase());
				
				if(null == joinTemps || joinTemps.isEmpty()){//也没有
					map.put("status", "n");
					map.put("info", "账号不存在！");
					map.put("input", "emailF");
					return map;
				}else{
					map.put("status", "n");
					map.put("info", "密码错误！");
					map.put("input", "passF");
					return map;
				}
			} else {
				// 验证用户登陆信息
				if (!passwordMD5.equals(user.getPassword())) {
					map.put("status", "n");
					map.put("info", "密码错误！");
					map.put("input", "passF");
					return map;
				} else {
					map.put("status", "n");
					map.put("info", "您的账号没有激活,或是团队号停用!");
					map.put("input", "emailF");
					return map;
				}
			}
		} else {
			map.put("status", "y");
			map.put("list", listOrg);
		}
		return map;
	}

	/**
	 * 用于登陆选择企业
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login/listChooseOrg")
	public ModelAndView listChooseOrg() {
		ModelAndView mav = new ModelAndView("listChooseOrg");
		return mav;
	}
	
	/**
	 * 登录
	 * @param session 浏览器session
	 * @param request 本次访问请求
	 * @param loginName 账号
	 * @param password 密码
	 * @param comId 团队号
	 * @param bustype 业务类型
	 * @param busId 业务主键
	 * @return
	 */
	@RequestMapping("/loginFromCs")
	public ModelAndView loginFromCs(HttpSession session, HttpServletRequest request, String account,
			String password, String comId,String busType,Integer busId) {
		//操作的IP
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		//密码加密
		String passwordMD5 = Encodes.encodeMd5(password);
		
		//是已有的成员系统
		final UserInfo userInfo = userInfoService.userAuth(account.toLowerCase(), passwordMD5, comId);
		if (userInfo != null && userInfo.getInService()==ConstantInterface.USER_INSERVICE_YES) {
			ExecutorService pool = ThreadPoolExecutor.getInstance();
			pool.execute(new OrganicServiceCheckThread(organicService,Integer.parseInt(comId)));//对团队可使用配置验证
			//设置操作IP
			userInfo.setOptIP(optIP);
			
			String sid = this.setSessionObj(SessionKeyConstant.USER_CONTEXT,userInfo);
			SessionContext.removeSessionUser(userInfo.getId());
			SessionContext.addSessionUser(userInfo.getId(), sid, session);
			String toUrl = this.goWhere(busType);
			ModelAndView view = new ModelAndView("/toIndex", "sid", sid);
			view.addObject("toUrl", toUrl);
			view.addObject("userInfo", userInfo);
			
			// 用户主键
			view.addObject("id", Encodes.encodeBase64(userInfo.getId().toString()).trim());
			// 登录名称
			view.addObject("loginName", Encodes.encodeBase64(account).trim());
			
			// 删除回收箱中超过三天的数据(数据量不大，不需要开启新的线程)
			new Thread(new Runnable() {
				public void run() {
					try {
						recycleBinService.delAllOverTri(userInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}).start();
			// 删除自己的无效任务
			taskService.delUnusedTask(userInfo);
			// 添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo
					.getUserName(), "进入平台", ConstantInterface.TYPE_LOGIN,
					userInfo.getComId(),optIP);
			// 每日登录积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(),
					ConstantInterface.TYPE_LOGIN, "每日登录系统", 0);
			
			//判断是否需要开启上下级关系
			OrganicCfg organicCfg = organicService.getOrganicCfg(userInfo.getComId(),OrgCfgConstant.LEADERCFG);
			if(null == organicCfg || ConstantInterface.MOD_OPT_STATE_YES.equals(organicCfg.getCfgValue())){//默认开启需要进行上下级验证
				//部门
				Integer depId = userInfo.getDepId();
				
				//获取个人直属上级集合
				List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(userInfo);
				if(null==userInfo.getIsChief() || "0".equals(userInfo.getIsChief()) && (
						null==listImmediateSuper || listImmediateSuper.isEmpty()
						|| null==depId || depId==0
						)){//不是老板。没有设定上下级
					view.addObject("addInfo", "y");
				}else{
					view.addObject("addInfo", "n");
				}
			}else{//团队设定时没有上下级
				view.addObject("addInfo", "n");
			}
			view.addObject("isSysUser", ConstantInterface.USER_INSERVICE_YES.toString());
			return view;
		}else if(userInfo != null && userInfo.getInService()==ConstantInterface.USER_INSERVICE_NO){
			this.setNotification(Notification.ERROR, "登录失败，你的账号超出团队人数上限，请联系你们的团队管理员。");
			return new ModelAndView("redirect:/login.jsp");
		} else {
			this.setNotification(Notification.ERROR, "登录失败，请检查你的账号和密码。");
			return new ModelAndView("redirect:/login.jsp");
		}

	}

	/**
	 * 辨析跳转URL
	 * @param busType
	 * @param busId
	 * @return
	 */
	private String goWhere(String busType) {
		String toUrl="/msgShare/toDoJobs/jobsCenter?";
		if(CommonUtil.isNull(busType)) {
			toUrl="/msgShare/toDoJobs/jobsCenter?1=1";
		} else if(busType.equals("003")){
			toUrl=toUrl+"modTypes=003&busType=003&activityMenu=job_m_1.2";
		}else if(busType.equals("022")){
			toUrl=toUrl+"modTypes=022&busType=022&activityMenu=job_m_1.3";
		}else if(busType.equals("006")){
			toUrl=toUrl+"modTypes=006&busType=006&activityMenu=job_m_1.7";
		}else if(busType.equals("017")){
			toUrl=toUrl+"modTypes=017&busType=017&activityMenu=job_m_1.9";
		}else if(busType.equals("050")){
			toUrl=toUrl+"modTypes=050&busType=050&activityMenu=job_m_1.6";
		}else if(busType.equals("015")){
			toUrl=toUrl+"modTypes=015&busType=015&activityMenu=job_m_1.12";
		}else if(busType.equals("080")){
			toUrl=toUrl+"modTypes=080&busType=080&activityMenu=job_m_1.13";
		}else{
			toUrl=toUrl+"modTypes=9999&busType=9999&activityMenu=job_m_1.18";
		}
		return toUrl;
	}
	
}