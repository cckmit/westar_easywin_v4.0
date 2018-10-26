package com.westar.core.web.controller;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.cons.OrgCfgConstant;
import com.westar.base.cons.SessionKeyConstant;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.Organic;
import com.westar.base.model.OrganicCfg;
import com.westar.base.model.OrganicSpaceCfg;
import com.westar.base.model.PassYzm;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.Encodes;
import com.westar.core.service.OrganicService;
import com.westar.core.service.RecycleBinService;
import com.westar.core.service.RegistService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TaskService;
import com.westar.core.service.UserInfoService;
import com.westar.core.web.SessionContext;

@Controller
@RequestMapping("/organic")
public class OrganicController extends BaseController{

	@Autowired
	OrganicService organicService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	RegistService registService;
	
	@Autowired
	TaskService taskService;

	@Autowired
	RecycleBinService recycleBinService;
	/**
	 * 企业信息修改界面
	 * @param id 要查询的人员的主键ID
	 * @return
	 */
	@RequestMapping("/organicInfo")
	public ModelAndView organicInfo() {
		Organic organic = organicService.getOrgInfo(this.getSessionUser().getComId());
		ModelAndView mav = new ModelAndView("/organic/organicCenter", "organic", organic);
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 企业信息修改
	 * @param organic
	 * @return
	 */
	@RequestMapping("/updateOrganic")
	public ModelAndView updateOrganic(Organic organic,String redirectPage) {
		
		UserInfo sessionUser = this.getSessionUser();
		if(sessionUser.getAdmin().equals("0")){//非管理人员不得操作
			ModelAndView mav = new ModelAndView("/refreshSelf");
			mav.addObject("redirectPage",redirectPage);
			this.setNotification(Notification.ERROR, "抱歉，你没有修改权限！");
			return mav;
		}else{
			//修改企业信息
			organicService.updateOrganic(organic);
			
			//添加日志记录 
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "修改企业信息",ConstantInterface.TYPE_ORG,
					sessionUser.getComId(),sessionUser.getOptIP());
			
			//操作人员的企业名称或许修改过
			//修改session中的用户信息
			UserInfo user = userInfoService.getUserInfo(sessionUser.getComId(),sessionUser.getId());
			//设置下级个数
			user.setCountSub(userInfoService.countSubUser(sessionUser.getId(), sessionUser.getComId()));
			
			this.updateSessionUser(user);
			this.setNotification(Notification.SUCCESS, "修改成功");
			return new ModelAndView("redirect:"+redirectPage);
			
		}
	}
	/**
	 * 团退解散页面跳转
	 * @return
	 */
	@RequestMapping("/organicManagePage")
	public ModelAndView organicManagePage() {
		
		ModelAndView mav = new ModelAndView();
		UserInfo sessionUser = this.getSessionUser();
		Organic organic = organicService.getOrgInfo(sessionUser.getComId());
		
		if(ConstantInterface.USER_ROLE_MANAGER_SUP.equals(sessionUser.getAdmin())){
			mav.setViewName("/organic/organicCenter");
			mav.addObject("userInfo", sessionUser);
			mav.addObject("organic", organic);
			
		}else{
			mav.setViewName("redirect:/index?sid="+this.getSid());
			this.setNotification(Notification.ERROR, "非注册账号,权限限制!");
		}
		return mav;
	}
	
	/**
	 * 解散团队
	 * @param organic
	 * @param sid
	 * @param yzm
	 * @param redirectPage
	 * @param noticeType
	 * @return
	 */
	@RequestMapping("/disMissOrg/{requestURI}")
	public ModelAndView disMissOrg(Organic organic,String sid,String yzm,String redirectPage,String noticeType) {
		ModelAndView mav = new ModelAndView();
		if(null==noticeType || "".equals(noticeType.trim())){
			this.setNotification(Notification.ERROR, "验证类型标识为空了，请联系管理员。");
			mav.setViewName("redirect:"+redirectPage);
			return mav;
		}
		
		final UserInfo sessionUser = this.getSessionUser();
		boolean isAdministrator = organicService.isAdministrator(sessionUser);
		if(!isAdministrator){//当前人员不是超级管理人员
			mav.setViewName("redirect:/index?sid="+sid);
			this.setNotification(Notification.ERROR, "非注册账号,权限限制!");
			return mav;
		}
		
		PassYzm passYzm = null;
		UserInfo curUser = this.getSessionUser();
		if("phone".equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getMovePhone());
			organicService.delPassYzm(curUser.getMovePhone());//删除验证码
		}else if("email".equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getEmail());
			organicService.delPassYzm(curUser.getEmail());//删除验证码
		}
		
		if(null==passYzm || null==passYzm.getPassYzm() || "".equals(passYzm.getPassYzm())){//session验证码过期
			this.setNotification(Notification.ERROR, "验证码失效！");
			mav.setViewName("redirect:"+redirectPage);
		}else if(null==yzm || "".equals(yzm)){//输入验证码为空
			this.setNotification(Notification.ERROR, "验证码不能为空！");
			mav.setViewName("redirect:"+redirectPage);
		}else if(!yzm.equalsIgnoreCase(passYzm.getPassYzm())){//验证码错误
			this.setNotification(Notification.ERROR, "验证码输入错误！");
			mav.setViewName("redirect:"+redirectPage);
		}else{
			//修改企业信息
			organicService.updateOrganic(organic);
			//选出企业所有的成员，不考虑是否在线
			List<UserInfo> listUsers = userInfoService.listUser(sessionUser.getComId());
			for (UserInfo userInfo : listUsers) {
				//移除企业登录成员;同userId的用户,登录不同的企业，可以正常操作
				SessionContext.removeSessionUser(userInfo.getId(),sessionUser.getComId());
			}
			new Thread(new Runnable() {
				public void run() {
					try {
						//删除企业所有信息
						organicService.delOrgAllInfo(sessionUser.getComId());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}).start();
			this.setNotification(Notification.SUCCESS, "已成功解散团队！");
			this.logOut(sid);
			mav.setViewName("redirect:/login.jsp");
		}
		
		return mav;
	}
	
	/**
	 * 团队冻结
	 * @param organic
	 * @param sid
	 * @param yzm
	 * @param redirectPage
	 * @param noticeType
	 * @return
	 */
	@RequestMapping("/disableOrg/{requestURI}")
	public ModelAndView disableOrg(Organic organic,String sid,String yzm,String redirectPage,String noticeType) {
		ModelAndView mav = new ModelAndView();
		if(null==noticeType || "".equals(noticeType.trim())){
			this.setNotification(Notification.ERROR, "验证类型标识为空了，请联系管理员。");
			mav.setViewName("redirect:"+redirectPage);
			return mav;
		}
		
		final UserInfo sessionUser = this.getSessionUser();
		boolean isAdministrator = organicService.isAdministrator(sessionUser);
		if(!isAdministrator){//当前人员不是超级管理人员
			mav.setViewName("redirect:/index?sid="+sid);
			this.setNotification(Notification.ERROR, "非注册账号,权限限制!");
			return mav;
		}
		
		PassYzm passYzm = null;
		UserInfo curUser = this.getSessionUser();
		if("phone".equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getMovePhone());
			organicService.delPassYzm(curUser.getMovePhone());//删除验证码
		}else if("email".equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getEmail());
			organicService.delPassYzm(curUser.getEmail());//删除验证码
		}
		
		if(null==passYzm || null==passYzm.getPassYzm() || "".equals(passYzm.getPassYzm())){//session验证码过期
			this.setNotification(Notification.ERROR, "验证码失效！");
			mav.setViewName("redirect:"+redirectPage);
		}else if(null==yzm || "".equals(yzm)){//输入验证码为空
			this.setNotification(Notification.ERROR, "验证码不能为空！");
			mav.setViewName("redirect:"+redirectPage);
		}else if(!yzm.equalsIgnoreCase(passYzm.getPassYzm())){//验证码错误
			this.setNotification(Notification.ERROR, "验证码输入错误！");
			mav.setViewName("redirect:"+redirectPage);
		}else{
			//选出企业所有的成员，不考虑是否在线
			List<UserInfo> listUsers = userInfoService.listUser(sessionUser.getComId());
			organic.setEnabled("0");
			//修改企业信息
			organicService.updateOrganic(organic);
			for (UserInfo userInfo : listUsers) {
				//移除企业登录成员;同userId的用户,登录不同的企业，可以正常操作
				if(userInfo.getId().equals(sessionUser.getId())){
					continue;
				}
				SessionContext.removeSessionUser(userInfo.getId(),sessionUser.getComId());
			}
			
			this.setNotification(Notification.SUCCESS, "操作成功");
		}
		return new ModelAndView("redirect:"+redirectPage);
	}
	/**
	 * 团队解冻
	 * @param request
	 * @param organic
	 * @param sid
	 * @param requestURI
	 * @param yzm
	 * @param redirectPage
	 * @param noticeType
	 * @return
	 */
	@RequestMapping("/enableOrg/{requestURI}")
	public ModelAndView enableOrg(HttpServletRequest request,Organic organic,String sid,
			@PathVariable String requestURI,String yzm,String redirectPage,String noticeType) {
		
		ModelAndView mav = new ModelAndView();
		if(null==noticeType || "".equals(noticeType.trim())){
			this.setNotification(Notification.ERROR, "验证类型标识为空了，请联系管理员。");
			mav.setViewName("redirect:"+redirectPage);
			return mav;
		}
		
		final UserInfo sessionUser = this.getSessionUser();
		boolean isAdministrator = organicService.isAdministrator(sessionUser);
		if(!isAdministrator){//当前人员不是超级管理人员
			mav.setViewName("redirect:/index?sid="+sid);
			this.setNotification(Notification.ERROR, "非注册账号,权限限制!");
			return mav;
		}
		
		PassYzm passYzm = null;
		UserInfo curUser = this.getSessionUser();
		if("phone".equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getMovePhone());
			organicService.delPassYzm(curUser.getMovePhone());//删除验证码
		}else if("email".equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getEmail());
			organicService.delPassYzm(curUser.getEmail());//删除验证码
		}
		
		if(null==passYzm || null==passYzm.getPassYzm() || "".equals(passYzm.getPassYzm())){//session验证码过期
			this.setNotification(Notification.ERROR, "验证码失效！");
			mav.setViewName("redirect:"+redirectPage);
		}else if(null==yzm || "".equals(yzm)){//输入验证码为空
			this.setNotification(Notification.ERROR, "验证码不能为空！");
			mav.setViewName("redirect:"+redirectPage);
		}else if(!yzm.equalsIgnoreCase(passYzm.getPassYzm())){//验证码错误
			this.setNotification(Notification.ERROR, "验证码输入错误！");
			mav.setViewName("redirect:"+redirectPage);
		}else{
			organic.setEnabled(ConstantInterface.ENABLED_YES.toString());
			//修改企业信息
			organicService.updateOrganic(organic);
			
			this.setNotification(Notification.SUCCESS, "操作成功");
		}
			
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	/**
	 * 完成注册发送邮件
	 * @param noticeType 接收方式标识
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sendPassYzm/{requestURI}")
	public Map<String, Object> sendPassYzm(String noticeType){
		if(null==noticeType || "".equals(noticeType.trim())){
			Map<String, Object> map = new HashMap<String, Object>();
			//告知方式未指定
			map.put("status", "n");
			return map;
		}else{
			Map<String, Object> map = null;
			//获取当前操作人对象
			UserInfo curUser = this.getSessionUser();
			boolean isAdministrator = organicService.isAdministrator(curUser);
			if(isAdministrator){
				map = userInfoService.doSendPassYzm(curUser,noticeType);
			}else{
				map = new HashMap<String, Object>();
				//告知方式未指定
				map.put("status", "n");
			}
			return map;
		}
	}
	/**
	 * 修改团队配置
	 * @param organicCfg
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateOrgCfg")
	public Map<String, Object> updateOrgCfg(OrganicCfg organicCfg){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//修改配置
		organicService.updateOrgCfg(sessionUser,organicCfg);
		map.put("status", "y");
		map.put("info", "操作成功！");
		return map;
	}
	/**
	 * 列表查询团队配置
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listOrgCfg")
	public Map<String, Object> listOrgCfg(){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//修改配置
		List<OrganicCfg> list = organicService.listOrgCfg(sessionUser.getComId());
		map.put("list", list);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 验证码验证
	 * @param param
	 * @param noticeType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkYzm/{requestURI}")
	public Map<String, Object> checkYzm(String param,String noticeType) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==noticeType || "".equals(noticeType.trim())){
			this.setNotification(Notification.ERROR, "验证类型标识为空了，请联系管理员。");
			map.put("status", "f");
			map.put("info", "验证类型标识为空了，请联系管理员。");
			return map;
		}
		PassYzm passYzm = null;
		UserInfo curUser = this.getSessionUser();
		if("phone".equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getMovePhone());
		}else if("email".equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getEmail());
		}

		if(null==passYzm ){
			map.put("status", "f");
			map.put("info", "验证码失效！");

		}else if(param.equalsIgnoreCase(passYzm.getPassYzm())) {
			map.put("status", "y");
		} else {
			map.put("status", "f");
			map.put("info", "验证码输入错误！");
		}
		return map;
	}
	/**
	 * 团队索引承重建
	 * @param sid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/indexRebuild")
	public ModelAndView indexRebuild(String sid) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		organicService.indexRebuild(userInfo);
		return new ModelAndView("redirect:/index?sid="+sid);
	}
	/**
	 * 打开二维码扫描页面
	 * @return
	 */
	@RequestMapping(value="/erWeiMa")
	public ModelAndView erWeiMa(){
		ModelAndView view = new ModelAndView("/erWeiMa");
		//取得App下载地址
		String appLoadUrl = CommonUtil.getAppLoadUrl();
		appLoadUrl = appLoadUrl.substring(0,appLoadUrl.indexOf("/", 10));
		view.addObject("appLoadUrl", appLoadUrl);
		return view;
	}
	
	/**
	 * 获取该用户所在企业
	 * @param account 登录名
	 * @return
	 */
	@RequestMapping(value="/listChooseOrganic")
	public ModelAndView listChooseOrganic(String account){
		ModelAndView mav = new ModelAndView("organic/listChooseOrg");
		UserInfo userInfo = this.getSessionUser();
		List<Organic> list = null;
		//获取登录名
		if(null== account || "".equals(account)){
			//登录名 赋值为邮箱
			account = userInfo.getEmail();
			if(null ==account || "".equals(account)){//不为邮箱
				//手机
				account = userInfo.getMovePhone();
				list = organicService.listUserOrg(account,userInfo.getPassword());
			}else{
				//获取用户所在团队列表
				list = organicService.listUserOrg(account.toLowerCase(),userInfo.getPassword());
			}
		}else{
			//获取用户所在团队列表
			list = organicService.listUserOrg(Encodes.decodeBase64(account).toLowerCase(),userInfo.getPassword());
		}
		
		mav.addObject("list",list);
		mav.addObject("comId",userInfo.getComId());
		return mav;
	}
	/**
	 * 团队切换
	 * @param session
	 * @param comId 企业编号
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/changeOrg")
	public ModelAndView changeOrg(HttpSession session, String comId, HttpServletRequest request,String account){
		//得到Session中用户
		UserInfo userInfo = this.getSessionUser();
		if(null== account || "".equals(account)){
			account = userInfo.getEmail();
			if(null!=account){
				account = account.toLowerCase();
			}else{
				account = userInfo.getMovePhone();
			}
		}else{
			account = Encodes.decodeBase64(account);
		}
		//通过用户ID和新的团队编号得到新的用户信息
		 final UserInfo newUser = userInfoService.userAuth(account, userInfo.getPassword(), comId);
		//移除旧用户信息
		SessionContext.removeSessionUser(userInfo.getId());
		//设置操作IP
		newUser.setOptIP(userInfo.getOptIP());
		String sid = this.setSessionObj(SessionKeyConstant.USER_CONTEXT,newUser);
		//Session 中添加新的用户信息
		SessionContext.addSessionUser(newUser.getId(), sid, session);
		ModelAndView mav = new ModelAndView("/toIndex", "sid", sid);
		mav.addObject("userInfo", newUser);
		// 用户主键
		mav.addObject("id", Encodes.encodeBase64(newUser.getId().toString()).trim());
		// 登录名称
		mav.addObject("loginName", Encodes.encodeBase64(account).trim());
		// 删除回收箱中超过三天的数据(数据量不大，不需要开启新的线程)
		new Thread(new Runnable() {
			public void run() {
				try {
					recycleBinService.delAllOverTri(newUser);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
		// 删除自己的无效任务
		taskService.delUnusedTask(newUser);
		// 添加日志记录
		systemLogService.addSystemLog(newUser.getId(), newUser.getUserName(), "切换团队", 
				ConstantInterface.TYPE_LOGIN,newUser.getComId(),newUser.getOptIP());
					
		//判断是否需要开启上下级关系
		OrganicCfg organicCfg = organicService.getOrganicCfg(newUser.getComId(),OrgCfgConstant.LEADERCFG);
		if(null == organicCfg || ConstantInterface.MOD_OPT_STATE_YES.equals(organicCfg.getCfgValue())){//默认开启需要进行上下级验证
			//部门
			Integer depId = newUser.getDepId();
			
			//获取个人直属上级集合
			List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(newUser);
			if("0".equals(newUser.getIsChief()) && (
					null==listImmediateSuper || listImmediateSuper.isEmpty()
					|| null==depId || depId==0
					)){//不是老板。没有设定上下级
				mav.addObject("addInfo", "y");
			}else{
				mav.addObject("addInfo", "n");
			}
		}else{//团队设定时没有上下级
			mav.addObject("addInfo", "n");
		}
		
		mav.addObject("isSysUser", ConstantInterface.USER_INSERVICE_YES.toString());
		return mav;
	}
	
	/**
	 * 核实现团队可激活人数信息
	 * @param enabled 激活状态标识符
	 * @param inService 是否在服务中标识符
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkOrgUsersInService")
	public Organic checkOrgUsersInService(Integer enabled,Integer inService) {
		UserInfo curUser = this.getSessionUser();//当前操作人信息
		Organic organic = organicService.checkOrgUsersInService(curUser.getComId(),enabled,inService);
		return organic;
	}
	
	/**
	 * 检查团队使用空间情况
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkOrgUsersFreeSpace")
	public Organic checkOrgUsersFreeSpace() {
		UserInfo curUser = this.getSessionUser();//当前操作人信息
		Organic organic = organicService.getOrgInfo(curUser.getComId());
		return organic;
	}
	
	/**
	 * 查看团队服务信息
	 * @return
	 */
	@RequestMapping(value = "/organicSpaceCfgInfo")
	public ModelAndView organicSpaceCfgInfo() {
		ModelAndView view = new ModelAndView("organic/organicSpaceCfgInfo");
		UserInfo curUser = this.getSessionUser();//当前操作人信息
		OrganicSpaceCfg organicSpaceCfg = organicService.getOrganicSpaceCfgInfo(curUser.getComId());
		view.addObject("organicSpaceCfg",organicSpaceCfg);
		return view;
	}
	
	/**
	 * 异步取得团队信息
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping(value="/findOrgInfo",method = RequestMethod.GET)
	public Map<String, Object> findOrgInfo(Integer weekYear,Integer weekNum) throws ParseException{
		Map<String, Object> map = new HashMap<>();
		Organic organic = organicService.getOrgInfo(this.getSessionUser().getComId());
		map.put("organic", organic);
		//注册时间
		String registDateTime = organic.getRecordCreateTime();
		
		//注册年份
		String registYear = registDateTime.substring(0,4);
		map.put("registYear", registYear);
		//注册时所在周数
		Integer registWeekNum = DateTimeUtil.getWeekOfYear(registDateTime,DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		map.put("registWeekNum", registWeekNum);
		
		//当前时间
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		//当前年份
		Integer nowYear = Integer.parseInt(nowDateTime.substring(0,4));
		map.put("nowYear", nowYear);
		
		Integer nowWeekNum = DateTimeUtil.getWeekOfYear(nowDateTime,DateTimeUtil.yyyy_MM_dd);
		map.put("nowWeekNum", nowWeekNum);
		return map;
	}
	
	/**
	 * 权限移交
	 * @param transferUserId 移交对象id
	 * @param transferDes 移交说明
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/transferAuthority")
	public Map<String,Object> transferAuthority(Integer transferUserId,String transferDes,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info",CommonConstant.OFF_LINE_INFO);
			return map;
		}

		if(CommonUtil.isNull(transferUserId) || CommonUtil.isNull(transferDes)){
			map.put("status", "f");
			map.put("info","移交信息未完整填写！");
			return map;
		}
		//修改管理权限
		organicService.updateOrgAdmin(userInfo,transferUserId,transferDes);

		//修改
		SessionContext.grantAndRevokeAdmin(userInfo.getId(), userInfo.getComId(), false);
		SessionContext.grantAndRevokeAdmin(transferUserId, userInfo.getComId(), true);

		map.put("status", "y");
		return map;
	}
}