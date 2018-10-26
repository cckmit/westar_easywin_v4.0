package com.westar.core.web.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.JfMod;
import com.westar.base.model.JfScore;
import com.westar.base.model.JfSubUserScope;
import com.westar.base.model.Task;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.TaskJfStatis;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.TaskJfExport;
import com.westar.core.service.DepartmentService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.JfModService;
import com.westar.core.service.TaskService;
import com.westar.core.web.PaginationContext;

@Controller
@RequestMapping("/jfMod")
public class JfModController extends BaseController{

	@Autowired
	JfModService jfModService;
	@Autowired
	TaskService taskService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	ForceInPersionService forceInService;
	
	/**
	 * 分页查询需要打分的
	 * @param jfMod
	 * @return
	 */
	@RequestMapping("/task/listPagedTaskToJf")
	public ModelAndView listPagedTaskToJf(JfMod jfMod){
		ModelAndView view = new ModelAndView("/jfMod/jfModCenter");
		UserInfo sessionUser = this.getSessionUser();
		//设置默认年份
		String searchYear = jfMod.getSearchYear();
		if(StringUtils.isEmpty(searchYear)){
			jfMod.setSearchYear(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy));
		}
		
		//设置开始时间
		String startDate = jfMod.getStartDate();
		if(!StringUtils.isEmpty(startDate)){
			startDate = searchYear+startDate.substring(4,startDate.length());
			jfMod.setStartDate(startDate);
		}
		//设置结束时间
		String endDate = jfMod.getEndDate();
		if(!StringUtils.isEmpty(endDate)){
			endDate = searchYear+endDate.substring(4,endDate.length());
			jfMod.setEndDate(endDate);
		}
		List<JfMod> list = jfModService.listPagedTaskToJf(jfMod,sessionUser);
		
		view.addObject("userInfo",sessionUser);
		view.addObject("list",list);
		boolean isForceIn = forceInService.isForceInPersion(sessionUser, ConstantInterface.TYPE_TASK);
		view.addObject("isForceIn",isForceIn);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_JFMOD);
		return view;
	}
	
	/**
	 * 分页查询自己的任务评分
	 * @param jfScore
	 * @return
	 */
	@RequestMapping("/task/listPagedMineTaskToJf")
	public ModelAndView listPagedMineTaskToJf(JfScore jfScore){
		ModelAndView view = new ModelAndView("/jfMod/jfModCenter");
		UserInfo sessionUser = this.getSessionUser();
		
		//设置默认年份
		String searchYear = jfScore.getSearchYear();
		if(StringUtils.isEmpty(searchYear)){
			jfScore.setSearchYear(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy));
		}
		//设置开始时间
		String startDate = jfScore.getStartDate();
		if(!StringUtils.isEmpty(startDate)){
			startDate = searchYear+startDate.substring(4,startDate.length());
			jfScore.setStartDate(startDate);
		}
		//设置结束时间
		String endDate = jfScore.getEndDate();
		if(!StringUtils.isEmpty(endDate)){
			endDate = searchYear+endDate.substring(4,endDate.length());
			jfScore.setEndDate(endDate);
		}
		boolean isForceIn = forceInService.isForceInPersion(sessionUser, ConstantInterface.TYPE_TASK);
		view.addObject("isForceIn",isForceIn);
		List<JfScore> list = jfModService.listPagedMineTaskToJf(jfScore,sessionUser);
		view.addObject("userInfo",sessionUser);
		view.addObject("list",list);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_JFMOD);
		return view;
	}

	/**
	 * 跳转到任务统计界面
	 * @return
	 */
	@RequestMapping(value = "/task/statisticsTaskJfPage")
	public ModelAndView statisticsTaskPage(JfScore jfScore) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/jfMod/jfModCenter");
		view.addObject("userInfo", userInfo);
		//设置默认年份
		String searchYear = jfScore.getSearchYear();
		if(StringUtils.isEmpty(searchYear)){
			jfScore.setSearchYear(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy));
		}
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
		view.addObject("isForceIn",isForceIn);
		view.addObject("jfScore",jfScore);
		return view;
	}
	/**
	 * 评分统计功能
	 * @param jfScore
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/statisticsTaskJf")
	public Map<String, Object> statisticsTaskJf(JfScore jfScore,Integer pageNum, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		//设置默认年份
		String searchYear = jfScore.getSearchYear();
		if(StringUtils.isEmpty(searchYear)){
			jfScore.setSearchYear(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy));
		}
		//设置开始时间
		String startDate = jfScore.getStartDate();
		if(!StringUtils.isEmpty(startDate)){
			startDate = searchYear+startDate.substring(4,startDate.length());
			jfScore.setStartDate(startDate);
		}
		//设置结束时间
		String endDate = jfScore.getEndDate();
		if(!StringUtils.isEmpty(endDate)){
			endDate = searchYear+endDate.substring(4,endDate.length());
			jfScore.setEndDate(endDate);
		}
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
		PageBean<TaskJfStatis> result = jfModService.listTaskJfStatis(jfScore,userInfo,isForceIn);
		map.put("pageBean", result);
		map.put("jfScore", jfScore);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 导出所有下属积分统计
	 * @param request
	 * @param response
	 * @param fileName
	 * @param jfScore
	 * @return
	 */
	@RequestMapping("/exportTaskJfStatistics")
	public ModelAndView excelExportList(HttpServletRequest request, HttpServletResponse response,String fileName,JfScore jfScore){
		UserInfo userInfo = this.getSessionUser();
		List<String> headTitle = new ArrayList<String>();
		//设置默认年份
		String searchYear = jfScore.getSearchYear();
		if(StringUtils.isEmpty(searchYear)){
			jfScore.setSearchYear(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy));
		}
		//设置开始时间
		String startDate = jfScore.getStartDate();
		if(!StringUtils.isEmpty(startDate)){
			startDate = searchYear+startDate.substring(4,startDate.length());
			jfScore.setStartDate(startDate);
		}
		//设置结束时间
		String endDate = jfScore.getEndDate();
		if(!StringUtils.isEmpty(endDate)){
			endDate = searchYear+endDate.substring(4,endDate.length());
			jfScore.setEndDate(endDate);
		}
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);	
		List<TaskJfStatis> list = jfModService.listTaskJfStatisOfAll(jfScore, userInfo,isForceIn);
		
		headTitle.add("序号");
		headTitle.add("部门名");
		headTitle.add("人员名称");
		headTitle.add("总任务数");
		headTitle.add("已评任务数");
		headTitle.add("总得分");
		
		TaskJfExport.exportExcel(list, fileName, headTitle, response, request);
		return null;
	}
	/**
	 * 跳转到添加任务评分界面
	 * @param jfMod
	 * @return
	 */
	@RequestMapping("/task/addTaskJfPage")
	public ModelAndView addTaskJfPage(JfMod jfMod){
		ModelAndView view = new ModelAndView("/jfMod/task/addTaskJf");
		UserInfo sessionUser = this.getSessionUser();
		
		Task task = taskService.getTask(jfMod.getBusId(),sessionUser);
		view.addObject("task",task);
		
		//取得需要评分的数据
		List<JfScore> listJfScores = jfModService.listTaskJfScore(jfMod,sessionUser);
		
		List<JfScore> result = new ArrayList<JfScore>();
		if(sessionUser.getCountSub()>0){//上级查看所有和自己的
			for (JfScore jfScore : listJfScores) {
				String dirLeaderState = jfScore.getDirLeaderState();
				Integer defUserId = jfScore.getDfUserId();
				if(dirLeaderState.equals("1") //是上级
						||defUserId.equals(sessionUser.getId())){//是自己
					result.add(jfScore);
				}
			}
		}else{//查看自己的
			for (JfScore jfScore : listJfScores) {
				Integer defUserId = jfScore.getDfUserId();
				if(defUserId.equals(sessionUser.getId())){//是自己
					result.add(jfScore);
				}
			}
		}
		view.addObject("listJfScores",result);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_JFMOD);
		view.addObject("userInfo",sessionUser);
		return view;
	}
	/**
	 * 任务打分
	 * @param jfMod
	 * @return
	 */
	@RequestMapping("/task/addBusJf")
	public ModelAndView addBusJf(JfMod jfMod){
		ModelAndView view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		UserInfo sessionUser = this.getSessionUser();
		//设置积分模块类型
		jfMod.setBusType(ConstantInterface.TYPE_TASK);
		
		jfModService.addBusJf(jfMod,sessionUser);
		this.setNotification(Notification.SUCCESS, "评分成功");
		return view;
	}
	
	/**
	 * 跳转到常规评分界面
	 * @param jfMod
	 * @return
	 */
	@RequestMapping("/normal/addNormalJfPage")
	public ModelAndView addNormalJfPage(JfMod jfMod){
		ModelAndView view = new ModelAndView("/jfMod/normal/addNormalJf");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo", sessionUser);
		return view;
	}
	/**
	 * 添加常规评分信息
	 * @param jfMod
	 * @return
	 */
	@RequestMapping("/normal/addNormalJf")
	public ModelAndView addNormalJf(JfMod jfMod){
		ModelAndView view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		UserInfo sessionUser = this.getSessionUser();
		jfModService.addNormalJf(jfMod,sessionUser);
		this.setNotification(Notification.SUCCESS, "评分成功");
		return view;
	}
	
	
	/**
	 * 查看任务得分信息
	 * @param jfMod
	 * @return
	 */
	@RequestMapping("/task/viewTaskJfPage")
	public ModelAndView viewTaskJfPage(JfMod jfMod){
		ModelAndView view = new ModelAndView("/jfMod/task/viewTaskJf");
		UserInfo sessionUser = this.getSessionUser();
		
		Task task = taskService.getTask(jfMod.getBusId(),sessionUser);
		view.addObject("task",task);
		
		//取得需要评分的数据
		List<JfScore> listJfScores = jfModService.listTaskJfScore(jfMod,sessionUser);
		
		//数据处理
		List<JfScore> result = new ArrayList<JfScore>();
		if(!sessionUser.getAdmin().equals("0")){//监督人员，查看所有
			result.addAll(listJfScores);
		}else if(sessionUser.getCountSub()>0){//上级查看所有和自己的
			for (JfScore jfScore : listJfScores) {
				String dirLeaderState = jfScore.getDirLeaderState();
				Integer defUserId = jfScore.getDfUserId();
				if(dirLeaderState.equals("1") //是上级
						||defUserId.equals(sessionUser.getId())){//是自己
					result.add(jfScore);
				}
			}
		}else{//查看自己的
			for (JfScore jfScore : listJfScores) {
				Integer defUserId = jfScore.getDfUserId();
				if(defUserId.equals(sessionUser.getId())){//是自己
					result.add(jfScore);
				}
			}
		}
		
		
		
		view.addObject("listJfScores",result);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_JFMOD);
		view.addObject("userInfo",sessionUser);
		return view;
	}
	/**
	 * 跳转到评分下属设置界面
	 * @return
	 */
	@RequestMapping(value="/editJfSubScopePage")
	public ModelAndView editJfSubScope(){
		ModelAndView view = new ModelAndView("/jfMod/editJfSubScope");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxListJfSubScope")
	public Map<String,Object> ajaxListJfSubScope(){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//取得模块的监督人员
		List<JfSubUserScope> listJfSubScope = jfModService.listJfSubScope(sessionUser);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		map.put("listJfSubScope", listJfSubScope);
		return map;
	}
	/**
	 * 修改积分下属范围
	 * @param jfSubScopeStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateJfSubScope")
	public Map<String,Object> updateJfSubScope(String jfSubScopeStr,String needScore){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		List<JfSubUserScope> jfSubScopeList = JSONArray.parseArray(jfSubScopeStr, JfSubUserScope.class);
		if(null!=jfSubScopeList && !jfSubScopeList.isEmpty()){
			for (JfSubUserScope subUserScope : jfSubScopeList) {
				subUserScope.setComId(sessionUser.getComId());
			}
		}
		jfModService.updateJfSubScope(sessionUser,jfSubScopeList,needScore);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		
		return map;
	}
	/**
	 * 删除强制参与人
	 * @param jfSubUserScope
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delJfSubUserScope")
	public Map<String,Object> delJfSubUserScope(JfSubUserScope jfSubUserScope){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		jfModService.delJfSubUserScope(sessionUser,jfSubUserScope);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
}