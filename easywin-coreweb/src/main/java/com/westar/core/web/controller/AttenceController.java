package com.westar.core.web.controller;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.AttenceConnSet;
import com.westar.base.model.AttenceRecord;
import com.westar.base.model.AttenceRecordUpload;
import com.westar.base.model.AttenceRule;
import com.westar.base.model.AttenceType;
import com.westar.base.model.AttenceUser;
import com.westar.base.model.Leave;
import com.westar.base.model.OverTime;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.ZkemSDK;
import com.westar.core.service.AttenceService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.UserInfoService;
import com.westar.core.web.FreshManager;

/**
 * 
 * 描述:考勤的操作类
 * @author zzq
 * @date 2018年8月25日 上午11:54:59
 */
@Controller
@RequestMapping("/attence")
public class AttenceController extends BaseController{

	@Autowired
	AttenceService attenceService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	UserInfoService userInfoService;
	/**
	 * 打卡状况(日期)
	 * @param request
	 * @param attenceRecord
	 * @return
	 */
	@RequestMapping("/listAttenceStateByDay")
	public ModelAndView listAttenceStateByDay(HttpServletRequest request,AttenceRecord attenceRecord){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/attence/attenceCenter");
		UserInfo curUser = this.getSessionUser();
		
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_ATTENCE);
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		if(null != attenceRecord && null!= attenceRecord.getSearchDate()){
		}else{
			attenceRecord.setSearchDate(nowDate);
		}
		List<UserInfo> listAttence  = attenceService.listUserAttenceState(curUser,attenceRecord,isForceIn);
		view.addObject("listAttence",listAttence);
		view.addObject("attenceRecord",attenceRecord);
		view.addObject("nowDate",nowDate);
		view.addObject("userInfo",curUser);
		view.addObject("isForceIn",isForceIn);
		view.addObject("homeFlag",ConstantInterface.TYPE_ATTENCE);
		return view;
	}
	/**
	 * 考勤记录
	 * @param request
	 * @param activityMenu
	 * @param attenceRecord
	 * @return
	 */
	@RequestMapping("/listAttenceRecord")
	public ModelAndView listAttenceRecord(HttpServletRequest request,AttenceRecord attenceRecord){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/attence/attenceCenter");
		UserInfo curUser = this.getSessionUser();
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_ATTENCE);
		if(null == attenceRecord ||null == attenceRecord.getUserId()){
			attenceRecord.setUserId(curUser.getId());
			attenceRecord.setUserName(curUser.getUserName());
		}
		if(!userInfoService.authorCheck(curUser, attenceRecord.getUserId(), isForceIn)){
			this.setNotification(Notification.ERROR,"抱歉，你没有查看该人员记录权限");
			attenceRecord.setUserId(curUser.getId());
			attenceRecord.setUserName(curUser.getUserName());
		}
		if(null == attenceRecord ||null == attenceRecord.getOrderBy()){
			attenceRecord.setOrderBy("desc");
		}
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		if(null == attenceRecord ||null == attenceRecord.getStartDate() || null == attenceRecord.getEndDate()){//默认查询日期
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
			// 获取前月的第一天
			Calendar first = Calendar.getInstance();// 获取当前日期
			first.add(Calendar.MONTH, -1);
			first.set(Calendar.DAY_OF_MONTH, 1);
			String firstDay = format.format(first.getTime());
			// 获取前月的最后一天
			Calendar last = Calendar.getInstance();
			last.set(Calendar.DAY_OF_MONTH, 0);
			String lastDay = format.format(last.getTime());
			attenceRecord.setStartDate(firstDay);
			attenceRecord.setEndDate(lastDay);
		}
		
		List<AttenceRecord> listAttenceRecord = attenceService.listAttenceRecordOfSelf(curUser,attenceRecord);
		view.addObject("listAttenceRecord",listAttenceRecord);
		view.addObject("nowDate",nowDate);
		view.addObject("userInfo",curUser);
		view.addObject("attenceRecord",attenceRecord);
		view.addObject("homeFlag",ConstantInterface.TYPE_ATTENCE);
		return view;
		
	}
	/**
	 * 考勤统计
	 * @param request
	 * @param activityMenu
	 * @param attenceRecord
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/attenceStatistics")
	public ModelAndView attenceStatistics(HttpServletRequest request,String activityMenu,AttenceRecord attenceRecord){
		
		Long startTime = System.currentTimeMillis();
		
		
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/attence/attenceCenter");
		UserInfo curUser = this.getSessionUser();
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		if(null == attenceRecord ||null == attenceRecord.getStartDate() || null == attenceRecord.getEndDate()){//默认查询日期
			 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
			// 获取前月的第一天
			Calendar first = Calendar.getInstance();// 获取当前日期
			first.add(Calendar.MONTH, -1);
			first.set(Calendar.DAY_OF_MONTH, 1);
			String firstDay = format.format(first.getTime());
			// 获取前月的最后一天
			Calendar last = Calendar.getInstance();
			last.set(Calendar.DAY_OF_MONTH, 0);
			String lastDay = format.format(last.getTime());
			attenceRecord.setStartDate(firstDay);
			attenceRecord.setEndDate(lastDay);
		}
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_ATTENCE);
		List<UserInfo> list = attenceService.attenceStatistics(curUser, attenceRecord,isForceIn);
		view.addObject("listUserInfo",list);//筛选参数
		view.addObject("userInfo",curUser);
		view.addObject("isForceIn",isForceIn);
		view.addObject("attenceRecord",attenceRecord);
		view.addObject("nowDate",nowDate);
		view.addObject("homeFlag",ConstantInterface.TYPE_ATTENCE);
		
		Long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
		return view;
	}
	/**
	 * 所有考勤记录
	 * @param request
	 * @param activityMenu
	 * @param attenceRecord
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listAttenceRecordOfSelf")
	public ModelAndView listAttenceRecordOfSelf(HttpServletRequest request,String activityMenu,AttenceRecord attenceRecord) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/attence/attenceCenter");
		UserInfo curUser = this.getSessionUser();
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		if(null == attenceRecord || null == attenceRecord.getStartDate()){
			attenceRecord.setStartDate(DateTimeUtil.getMonthFristDay(DateTimeUtil.yyyy_MM_dd));
		}
		if(null == attenceRecord || null == attenceRecord.getEndDate()){
			attenceRecord.setEndDate(nowDate);
		}
		if(null == attenceRecord ||null == attenceRecord.getOrderBy()){
			attenceRecord.setOrderBy("desc");
		}
		List<AttenceRecord> listAttenceRecord = attenceService.listAttenceRecordOfSelf(curUser,attenceRecord);
		view.addObject("listAttenceRecord",listAttenceRecord);
		view.addObject("nowDate",nowDate);
		view.addObject("userInfo",curUser);
		view.addObject("homeFlag",ConstantInterface.TYPE_ATTENCE);
		return view;
	}
	/**
	 * 获取个人请假记录列表
	 * @param request
	 * @param activityMenu
	 * @param leave
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listLeave")
	public ModelAndView listLeave(HttpServletRequest request,String activityMenu,Leave leave) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/attence/attenceCenter");
		UserInfo curUser = this.getSessionUser();
		leave.setCreator(curUser.getId());
		PageBean<Leave> pageBean = attenceService.listPagedLeave(curUser,leave);
		view.addObject("listLeave", pageBean.getRecordList());
		view.addObject("userInfo",curUser);
		view.addObject("leave",leave);//筛选参数
		view.addObject("homeFlag",ConstantInterface.TYPE_ATTENCE);
		return view;
	}
	/**
	 * 获取某时间段个人请假记录
	 * @param leave
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewListLeave")
	public ModelAndView viewListLeave(Leave leave){
		ModelAndView view = new ModelAndView("/attence/viewListLeave");
		UserInfo curUser = this.getSessionUser();
		//验证当前登录人是否是督察人员
		PageBean<Leave> pageBean = attenceService.listPagedLeave(curUser,leave);
		view.addObject("listLeave", pageBean.getRecordList());
		view.addObject("userInfo",curUser);
		view.addObject("leave",leave);//筛选参数
		return view;
	}
	/**
	 * 获取个人加班记录列表
	 * @param request
	 * @param activityMenu
	 * @param overTime
	 * @return
	 */
	@RequestMapping("/listOverTime")
	public ModelAndView listOverTime(HttpServletRequest request,String activityMenu,OverTime overTime){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/attence/attenceCenter");
		UserInfo curUser = this.getSessionUser();
		overTime.setCreator(curUser.getId());
		PageBean<OverTime> pageBean = attenceService.listPagedOverTime(curUser,overTime);
		view.addObject("listOverTime", pageBean.getRecordList());
		view.addObject("userInfo",curUser);
		view.addObject("overTime",overTime);//筛选参数
		view.addObject("homeFlag",ConstantInterface.TYPE_ATTENCE);
		return view;
	}
	/**
	 * 获取加班记录列表
	 * @param request
	 * @param activityMenu
	 * @param overTime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewListOverTime")
	public ModelAndView viewListOverTime(HttpServletRequest request,String activityMenu,OverTime overTime){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/attence/viewListOverTime");
		UserInfo curUser = this.getSessionUser();
		PageBean<OverTime> pageBean = attenceService.listPagedOverTime(curUser,overTime);
		view.addObject("listOverTime", pageBean.getRecordList());
		view.addObject("userInfo",curUser);
		view.addObject("overTime",overTime);//筛选参数
		return view;
	}
	/**
	 * 查看团队考勤设置
	 * @return
	 */
	@RequestMapping("/viewAttenceSet")
	public ModelAndView viewAttenceSet() {
		
		UserInfo sessionUser = this.getSessionUser();
		
		AttenceRule attenceRule = attenceService.getAttenceRule(null,sessionUser.getComId());
		
		ModelAndView view = new ModelAndView("/adminCfg/adminCfg_center");
		view.addObject("toPage","../attence/viewAttenceSet.jsp");
		view.addObject("userInfo", sessionUser);
		view.addObject("attenceRule", attenceRule);
		
		return view;
	}
	/**
	 * 查看团队考勤设置
	 * @return
	 */
	@RequestMapping("/attenceRuleSetPage")
	public ModelAndView attenceRuleSetPage() {
		ModelAndView mav = new ModelAndView("/attence/attenceRuleSet");
		return mav;
	}
	/**
	 * 取得考勤的规则类型集合
	 * @param attenceRuleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxListAttenceType")
	public Map<String, Object> ajaxListAttenceType(Integer attenceRuleId){
		
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO,CommonConstant.OFF_LINE_INFO);
			return map;
		}
		List<AttenceType> listAttenceType = attenceService.listAttenceTypes(userInfo.getComId(),attenceRuleId);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		map.put("listAttenceType",listAttenceType);
		
		return map;
	}
	
	/**
	 * 异步修改考勤规则
	 * @param attenceRuleStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxUpdateAttenceRule")
	public Map<String, Object> ajaxUpdateAttenceRule(String attenceRuleStr){
		
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO,CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//布局详情json转对象
		AttenceRule attenceRule = JSONObject.parseObject(attenceRuleStr, AttenceRule.class);
		
		attenceRule.setComId(userInfo.getComId());
		attenceService.updateAttenceRule(attenceRule);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 查看团队考勤机设置
	 * @return
	 */
	@RequestMapping("/attenceConnSet")
	public ModelAndView attenceConnSet() {
		ModelAndView mav = new ModelAndView("/adminCfg/adminCfg_center");
		UserInfo userInfo = this.getSessionUser();
		AttenceConnSet attenceConnSet = attenceService.queryAttenceConnSet(userInfo);
		if(null == attenceConnSet){
			mav.addObject("action", "add");
		}else{
			mav.addObject("action", "update");
		}
		mav.addObject("toPage","../attence/attenceConnSet.jsp");
		mav.addObject("attenceConnSet",attenceConnSet);
		mav.addObject("userInfo",userInfo);
		return mav;
	}
	/**
	 * 添加团队考勤机设置
	 * @return
	 */
	@RequestMapping("/addAttenceConnSet")
	public ModelAndView addAttenceConnSet(HttpServletRequest request,AttenceConnSet attenceConnSet) {
		ModelAndView mav = new ModelAndView("/adminCfg/adminCfg_center");
		UserInfo userInfo = this.getSessionUser();
		Integer id = attenceService.addAttenceConnSet(userInfo,attenceConnSet);
		attenceConnSet.setId(id);
		this.setNotification(Notification.SUCCESS, "保存成功!");
		mav.addObject("attenceConnSet",attenceConnSet);
		mav.addObject("userInfo",userInfo);
		mav.addObject("action", "update");
		mav.addObject("toPage","../attence/attenceConnSet.jsp");
		return mav;
	}
	/**
	 * 修改团队考勤机设置
	 * @return
	 */
	@RequestMapping("/updateAttenceConnSet")
	public ModelAndView updateAttenceConnSet(HttpServletRequest request,AttenceConnSet attenceConnSet) {
		ModelAndView mav = new ModelAndView("/adminCfg/adminCfg_center");
		UserInfo userInfo = this.getSessionUser();
		attenceService.updateAttenceConnSet(userInfo,attenceConnSet);
		this.setNotification(Notification.SUCCESS, "保存成功!");
		mav.addObject("attenceConnSet",attenceConnSet);
		mav.addObject("userInfo",userInfo);
		mav.addObject("action", "update");
		mav.addObject("toPage","../attence/attenceConnSet.jsp");
		return mav;
	}
	/**
	 * 检查考勤机链接设置
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/checkAttenceConntSet")
	public Map<String, Object> checkAttenceConntSet(){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_F);
			map.put(ConstantInterface.TYPE_INFO,CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		AttenceConnSet attenceConnSet = attenceService.queryAttenceConnSet(userInfo);
		if(null!=attenceConnSet){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		}else{
			map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_N);
		}
		return map;
	}
	
	/**
	 * 同步考勤记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/uploadAttenceRecord")
	public Map<String, Object> uploadAttenceRecord(){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_F);
			map.put(ConstantInterface.TYPE_INFO,CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//连接考勤机
		ZkemSDK sdk = attenceService.getConnect(userInfo);
		if(null!=sdk){
			String lastTime = "";
			//上传同步上传
			AttenceRecordUpload lastUpload =  attenceService.queryLastUpload(userInfo);
			if(null == lastUpload || null == lastUpload.getLastTime()){
				lastTime = "2015-01-01 00:00:00";
			}else{
				lastTime = lastUpload.getLastTime();
			}
			Date lasDate = DateTimeUtil.parseDate(lastTime, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
			
			if(sdk.readLastestLogData(lasDate)){
				//读取成功  
				List<Map<String,Object>> listLogData  = sdk.getGeneralLogData("");
				//同步至数据库中
				attenceService.uploadAttenceRecord(listLogData, userInfo);
				map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_Y);
				map.put(ConstantInterface.TYPE_INFO,"同步成功！");
				sdk.disConnect();
			}else{
				//断开连接
				sdk.disConnect();
				map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_N);
				map.put(ConstantInterface.TYPE_INFO,"读取考勤机数据失败或者暂无更新数据,请稍后重试！");
			}
		}else{
			map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO,"连接考勤机失败,请检查配置信息是否正确！");
		}
		return map;
	}
	
	/**
	 * 同步考勤记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/uploadAttenceUser")
	public Map<String, Object> uploadAttenceUser(){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_F);
			map.put(ConstantInterface.TYPE_INFO,CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//连接考勤机
		ZkemSDK sdk = attenceService.getConnect(userInfo);
		if(null!=sdk){
			attenceService.uploadAttenceUser(sdk.getAllUserInfo(),userInfo);	
			map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_Y);
			map.put(ConstantInterface.TYPE_INFO,"同步成功！");
			sdk.disConnect();
		}else{
			map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO,"连接考勤机失败,请检查配置信息是否正确！");
		}
		return map;
	}
	/**
	 * 分页查询  考勤人员
	 * @param request
	 * @param attenceUser
	 * @return
	 */
	@RequestMapping("/listAttenceUser")
	public ModelAndView listAttenceUser(HttpServletRequest request,AttenceUser attenceUser) {
		ModelAndView mav = new ModelAndView("/attence/listAttenceUser");
		UserInfo userInfo = this.getSessionUser();
		List<AttenceUser> listAttenceUser = attenceService.listPageAttenceUser(attenceUser, userInfo);
		mav.addObject("listAttenceUser",listAttenceUser);
		mav.addObject("attenceUser",attenceUser);
		mav.addObject("userInfo",userInfo);
		return mav;
	}
}