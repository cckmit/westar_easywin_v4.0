package com.westar.core.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.AttenceRule;
import com.westar.base.model.AttenceType;
import com.westar.base.model.Customer;
import com.westar.base.model.DataDic;
import com.westar.base.model.DelayApply;
import com.westar.base.model.FestModDate;
import com.westar.base.model.Item;
import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.PersonAttention;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.Task;
import com.westar.base.model.TaskExecutor;
import com.westar.base.model.TaskLog;
import com.westar.base.model.TaskTalk;
import com.westar.base.model.TaskTalkUpfile;
import com.westar.base.model.TaskUpfile;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.RelateModeVo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.RequestContextHolderUtil;
import com.westar.base.util.StringUtil;
import com.westar.base.util.TaskItemStageExport;
import com.westar.core.service.AttenceService;
import com.westar.core.service.ClockService;
import com.westar.core.service.CrmService;
import com.westar.core.service.FestModService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.ItemService;
import com.westar.core.service.JiFenService;
import com.westar.core.service.ModOptConfService;
import com.westar.core.service.PersonAttentionService;
import com.westar.core.service.TaskPromoteService;
import com.westar.core.service.TaskService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.service.WorkFlowService;
import com.westar.core.web.DataDicContext;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

/**
 * 任务模块
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/task")
public class TaskController extends BaseController {
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	TaskPromoteService taskPromoteService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	CrmService crmService;
	
	@Autowired
	JiFenService jifenService;
	
	@Autowired
	ModOptConfService modOptConfService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ClockService clockService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	FestModService festModService;
	
	@Autowired
	AttenceService attenceService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	PersonAttentionService personAttentionService;
	/**
	 * 导出任务项目统计全部数据
	 * @param request
	 * @param response
	 * @param fileName
	 * @param item
	 * @param listOwnerStr
	 * @return
	 */
	@RequestMapping("/exportTaskItem")
	public ModelAndView excelExportCrmList(HttpServletRequest request, HttpServletResponse response,String fileName,Item item,
			String listOwnerStr){
		UserInfo userInfo = this.getSessionUser();
		if(null != listOwnerStr && !listOwnerStr.isEmpty()){
			Gson gson = new Gson();
			Item owner = gson.fromJson(listOwnerStr, Item.class);
			if(null != owner.getListOwner() && !owner.getListOwner().isEmpty()){
				item.setListOwner(owner.getListOwner());
			}
		}
		
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);
		List<Item> listItemOfAll = itemService.listItemOfAll(item, userInfo, isForceIn);
		if(!CommonUtil.isNull(listItemOfAll)){
			AttenceRule attenceRule = attenceService.getAttenceRule(null,userInfo.getComId());//考勤规则
			List<FestModDate> listFestModDateOfTeam = festModService.listFestModDateOfTeam(userInfo.getComId());//获取当前团队下所有的节假日维护信息
			//团队的工作时段
			List<AttenceType> listAttenceTypes = attenceService.listAttenceTypes(userInfo.getComId(), attenceRule.getId());
			for (Item itemVo : listItemOfAll) {
				taskService.itemStageInfoCount(userInfo, attenceRule,listFestModDateOfTeam,listAttenceTypes, itemVo);//项目阶段信息计算
			}
		}
		List<String> headTitle = new ArrayList<String>();
		headTitle.add("序号");
		headTitle.add("项目名称");
		headTitle.add("负责人");
		headTitle.add("创建于");
		headTitle.add("项目阶段");
		headTitle.add("任务数");
		headTitle.add("已耗时（小时）");
		headTitle.add("超时（小时）");
		
		TaskItemStageExport.exportExcel(listItemOfAll, fileName, headTitle, response, request);
		
		return null;
	}
	
	/**
	 * 跳转我权限下的所有任务任务列表页面
	 * @return
	 */
	@RequestMapping("/listTaskOfAllPage")
	public ModelAndView listTaskOfAll(HttpServletRequest request,Task task){

		ModelAndView view = new ModelAndView("/task/taskCenter");
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
	    //执行人类别
	    String execuType = task.getExecuType();
	    //若是没有下属，则没有负责人类别一说
	    if(userInfo.getCountSub()<=0 && null != execuType && "1".equals(execuType)){
	    	task.setExecuType(null);
	    }
	    
	    //没有关联
  		String relateModType = task.getRelateModType();
  		List<RelateModeVo> modeVos = task.getListRelateModes();
  		if(StringUtils.isEmpty(relateModType) 
  				|| null==modeVos || modeVos.isEmpty() ){
  			task.setRelateModType(null);
  			task.setListRelateModes(null);
  		}
		List<Task> list = null;
		
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && "2".equals(version)){
			list = taskPromoteService.listPageTask(task,userInfo);
		}else{
			task.setVersion("1");
			list = taskService.listPageTask(task,userInfo);
		}


		view.addObject("userInfo",userInfo);
		view.addObject("list",list);
		view.addObject("task",task);

		//获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_TASK);
		if(null!=listModuleOperateConfig){
			for(ModuleOperateConfig vo:listModuleOperateConfig){
				view.addObject(vo.getOperateType(),ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_TASK);
		return view;
	}
	/**
	 * 跳转我权限下的所有任务任务列表页面
	 * @return
	 */
	@RequestMapping("/listPagedTaskForSupView")
	public ModelAndView listPagedTaskForSupView(HttpServletRequest request,Task task){
		
		ModelAndView view = new ModelAndView("/task/taskCenter");
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		//执行人类别
		String execuType = task.getExecuType();
		//若是没有下属，则没有负责人类别一说
		if(userInfo.getCountSub()<=0 && null!=execuType && "1".equals(execuType)){
			task.setExecuType(null);
		}
		//没有关联
		String relateModType = task.getRelateModType();
		List<RelateModeVo> modeVos = task.getListRelateModes();
		if(StringUtils.isEmpty(relateModType) 
				|| null==modeVos || modeVos.isEmpty() ){
			task.setRelateModType(null);
			task.setListRelateModes(null);
		}
		if(CommonUtil.isNull(task.getVersion()) || !"2".equals(task.getVersion())){
			task.setVersion("1");
			List<Task> list = taskService.listPagedTaskForSupView(task,userInfo);
			view.addObject("list",list);
		}else{
			List<Task> list = taskPromoteService.listPagedTaskForSupView(task,userInfo);
			view.addObject("list",list);
		}
		
		view.addObject("userInfo",userInfo);
		view.addObject("task",task);
		
		//获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_TASK);
		if(null!=listModuleOperateConfig){
			for(ModuleOperateConfig vo:listModuleOperateConfig){
				view.addObject(vo.getOperateType(),ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_TASK);
		return view;
	}
	/**
	 * 任务中心
	 * @param request
	 * @param task
	 * @return
	 */
	@RequestMapping("/taskCenter")
	public ModelAndView taskCenter(HttpServletRequest request,Task task){
		ModelAndView view = new ModelAndView("/task/taskCenter");
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		if(task.getOffset()!=0){
			PaginationContext.setOffset(task.getOffset());
		}
		//初始化执行人为当前操作人
		task.setExecutor(userInfo.getId());
		//任务状态标记为执行状态
		task.setState(1);
		List<Task> list = taskService.taskToDoList(task,userInfo);
		
		view.addObject("userInfo",userInfo);
		view.addObject("list",list);
		view.addObject("task",task);
		

		//获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_TASK);
		if(null!=listModuleOperateConfig){
			for(ModuleOperateConfig vo:listModuleOperateConfig){
				view.addObject(vo.getOperateType(),ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_TASK);
		return view;
	}
	/**
	 * 跳转待办任务列表页面
	 * @param task
	 * @return
	 */
	@RequestMapping("/listTaskToDoPage")
	public ModelAndView listTaskToDo(HttpServletRequest request,Task task){

		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/task/taskCenter");
		if(task.getOffset()!=0){
			PaginationContext.setOffset(task.getOffset());
		}
		//任务执行人
		List<UserInfo> exectors = new ArrayList<UserInfo>(); 
		exectors.add(userInfo);
		task.setListExecutor(exectors);

		//任务状态标记为执行状态
		//初始化逾期状态
		if(task.getCountType()!=null && task.getCountType().length()>0){
			//初始化逾期状态
			if("over".equals(task.getCountType())){
				task.setOverdue(true);
			}else{
				task.setOverdue(false);
			}
		}
		
		 //没有关联
  		String relateModType = task.getRelateModType();
  		List<RelateModeVo> modeVos = task.getListRelateModes();
  		if(StringUtils.isEmpty(relateModType) 
  				|| null==modeVos || modeVos.isEmpty() ){
  			task.setRelateModType(null);
  			task.setListRelateModes(null);
  		}
		List<Task> list = null;
		//版本
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			list = taskPromoteService.taskToDoList(task,userInfo);
		}else{
			task.setVersion("1");
			list = taskService.taskToDoList(task,userInfo);
		}
		
		view.addObject("userInfo",userInfo);
		view.addObject("list",list);
		view.addObject("task",task);

		//获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_TASK);
		if(null!=listModuleOperateConfig){
			for(ModuleOperateConfig vo:listModuleOperateConfig){
				view.addObject(vo.getOperateType(),ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_TASK);
		
		//总待办数
		Integer count = PaginationContext.getTotalCount();
		view.addObject("countTodo", count);
		return view;
	}
	
	/**
	 * 异步查询所有待办任务
	 * @param task
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListTaskToDoPage")
	public Map<String,Object> ajaxListTaskToDoPage(Task task,Integer pageNum,Integer pageSize){
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
		//一次加载行数
		PaginationContext.setPageSize(pageSize);
		//列表数据起始索引位置
		PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
		
		//初始化执行人为当前操作人
		List<UserInfo> exectors = new ArrayList<UserInfo>();
		exectors.add(userInfo);
		task.setListExecutor(exectors);
		//任务状态标记为执行状态
		task.setState(1);
		
		//初始化逾期状态
		if(task.getCountType()!=null && task.getCountType().length()>0){
			//初始化逾期状态
			if("over".equals(task.getCountType())){
				task.setOverdue(true);
			}else{
				task.setOverdue(false);
			}
		}
		
		//没有关联
		String relateModType = task.getRelateModType();
		List<RelateModeVo> modeVos = task.getListRelateModes();
		if(StringUtils.isEmpty(relateModType) 
				|| null==modeVos || modeVos.isEmpty() ){
			task.setRelateModType(null);
			task.setListRelateModes(null);
		}
		
		List<Task> list = taskService.taskToDoList(task,userInfo);
		map.put("list",list);
		map.put("status", "y");
		
		//统计下属的待办情况
		if(userInfo.getCountSub()>0 ){
			List<UserInfo> subUser = taskService.listSubTaskCount(userInfo,task);
			map.put("subUser",subUser);
			map.put("countType", 1);
		}else{
			//待办任务统计 列表查询条件改了，这也需要修改！
			List<Task> taskToDoCount = taskService.taskToDoCount(task, userInfo);
			map.put("toDoCount",taskToDoCount);
			map.put("countType", 2);
		}
		
		//待办数
		Integer todoNum = PaginationContext.getTotalCount();
		map.put("todoNum", todoNum);
		
		return map;
	}
	/**
	 * 异步加载待办任务
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxToDoTaskCount")
	public Map<String,Object> ajaxToDoCount(HttpServletRequest request,Task task){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
				
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();

		//待办任务的右边统计只显示个人的待办任务统计
//		List<Task> taskToDoCount = taskService.taskToDoCount(task, userInfo);
//		map.put("toDoCount",taskToDoCount);
//		map.put("countType", 2);

		//统计下属的待办情况
		if(userInfo.getCountSub()>0 ){
			List<UserInfo> subUser = taskService.listSubTaskCount(userInfo,task);
			map.put("subUser",subUser);
			map.put("countType", 1);
		}else{
			//待办任务统计 列表查询条件改了，这也需要修改！
			List<Task> taskToDoCount = taskService.taskToDoCount(task, userInfo);
			map.put("toDoCount",taskToDoCount);
			map.put("countType", 2);
		}
		return map;
	}
	/**
	 * 异步加载所有任务	
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAllTaskCount")
	public Map<String,Object> ajaxAllTaskCount(HttpServletRequest request,Task task){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		//验证当前登录人是否是督察人员
	    boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
	    if(null==task.getVersion() || !"2".equals(task.getVersion())){
	    	task.setVersion("1");
	    }
		//下属任务办理情况
		if(userInfo.getCountSub()>0 ){
			//显示下属统计时屏蔽掉所有的筛选条件
			task.setAttentionState(null);
			task.setListOwner(null);
			task.setListOperator(null);
			task.setState(null);
			task.setOperator(null);
			task.setListExecutor(null);
			task.setStartDate(null);
			task.setEndDate(null);
			task.setCreator(null);
			task.setGrade(null);
			task.setOwner(null);
			task.setRelateModType(null);
			List<UserInfo> subUser = taskService.listSubTaskCount(userInfo,task);
			map.put("subUser",subUser);
			map.put("countType", 1);
			List<PersonAttention> attentions = personAttentionService.listPersonAttention(userInfo);
			map.put("attentions", attentions);
		}else{
			//任务紧急度统计
			List<Task> allTaskCount = null;
			int entry = Integer.parseInt(request.getParameter("entry"));
			switch (entry){
				case 0 :
					//所有任务
					allTaskCount = taskService.allTaskCount(task,userInfo,isForceIn);
					break;
				case 1 :
					//待办
					if("2".equals(task.getVersion())){
						allTaskCount = taskPromoteService.taskToDoCount(task, userInfo);
					}else{
						allTaskCount = taskService.taskToDoCount(task, userInfo);
					}
					break;
				case 3 :
					//关注
					task.setAttentionState("1");
					allTaskCount = taskService.allTaskCount(task,userInfo,isForceIn);
					break;
				case 4 :
					//我负责的
					allTaskCount = taskService.chargeTaskCount(task, userInfo);
					break;
				default :
					//所有任务
					allTaskCount = taskService.allTaskCount(task,userInfo,isForceIn);
					break;

			}
			map.put("allTaskCount",allTaskCount);
			map.put("countType", 2);
		}
		if("2".equals(task.getVersion())){
			try {
				task = taskPromoteService.countUserTask(userInfo,task);
				map.put("todoNum",task.getToDoNum());
				map.put("doneTime", task.getExecuteTime());
				map.put("doneNum", task.getWjNum());
			} catch (ParseException e) {
				e.printStackTrace();
				map.put("todoNum",0);
				map.put("doneTime", 0);
				map.put("doneNum", 0);
			}
		}
		return map;
	}
	/**
	 * 异步我负责得任务
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxChargeTaskCount")
	public Map<String,Object> ajaxChargeTaskCount(HttpServletRequest request,Task task){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);

				
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();

		List<UserInfo> owners = new ArrayList<UserInfo>();
		owners.add(userInfo);
		//初始化执行人为当前操作人
		task.setListOwner(owners);
		
		//下属任务办理情况
		if(userInfo.getCountSub()>0 ){
			List<UserInfo> subUser = taskService.listSubTaskCount(userInfo,task);
			map.put("subUser",subUser);
			map.put("countType", 1);
		}else{
			//我负责的任务紧急度统计
			List<Task> chargeTaskCount = taskService.chargeTaskCount(task, userInfo);
			map.put("chargeCount",chargeTaskCount);
			map.put("countType", 2);
		}
		return map;
	}

	/**
	 * 获取关注列表
	 * @param request
 * @param task
	 * @return org.springframework.web.servlet.ModelAndView
	 * @author LiuXiaoLin
	 * @date 2018/5/16 0016 10:19
	 */
	@RequestMapping("/listTaskOfAttenPage")
	public ModelAndView listTaskOfAttenPage(HttpServletRequest request,Task task){

		ModelAndView view = new ModelAndView("/task/taskCenter");
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		//执行人类别
		String execuType = task.getExecuType();
		//若是没有下属，则没有负责人类别一说
		if(userInfo.getCountSub()<=0 && null!=execuType && "1".equals(execuType)){
			task.setExecuType(null);
		}

		//没有关联
		String relateModType = task.getRelateModType();
		List<RelateModeVo> modeVos = task.getListRelateModes();
		if(StringUtils.isEmpty(relateModType)
				|| null==modeVos || modeVos.isEmpty() ){
			task.setRelateModType(null);
			task.setListRelateModes(null);
		}
		List<Task> list = null;
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			list = taskPromoteService.listPageTask(task,userInfo);
		}else{
			task.setVersion("1");
			list = taskService.listPageTask(task,userInfo);
		}
		view.addObject("userInfo",userInfo);
		view.addObject("list",list);
		view.addObject("task",task);

		//获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_TASK);
		if(null!=listModuleOperateConfig){
			for(ModuleOperateConfig vo:listModuleOperateConfig){
				view.addObject(vo.getOperateType(),ConstantInterface.MOD_OPT_STATE_YES);
			}
		}

		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_TASK);
		return view;
	}
	
	/**
	 * 跳转我负责的任务列表页面
	 * @param task
	 * @return
	 */
	@RequestMapping("/listTaskOfMinePage")
	public ModelAndView listTaskOfMine(HttpServletRequest request,Task task){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/task/taskCenter");
		if(task.getOffset()!=0){
			PaginationContext.setOffset(task.getOffset());
		}
		
		List<UserInfo> owners = new ArrayList<UserInfo>();
		owners.add(userInfo);
		//初始化负责人为当前操作人
		task.setListOwner(owners);

		//执行人类别
	    String execuType = task.getExecuType();
	    //若是没有下属，则没有负责人类别一说
	    if(userInfo.getCountSub()<=0 && null!=execuType && "1".equals(execuType)){
	    	task.setExecuType(null);
	    }
	  //没有关联
  		String relateModType = task.getRelateModType();
  		List<RelateModeVo> modeVos = task.getListRelateModes();
  		if(StringUtils.isEmpty(relateModType) 
  				|| null==modeVos || modeVos.isEmpty() ){
  			task.setRelateModType(null);
  			task.setListRelateModes(null);
  		}
	  		
		//分页查询负责的任务
		List<Task> list = null;
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			list = taskPromoteService.listPageChargeTask(task,userInfo);
		}else{
			task.setVersion("1");
			list = taskService.listPageChargeTask(task,userInfo);
		}
		view.addObject("userInfo",userInfo);
		view.addObject("list",list);
		view.addObject("task",task);

		//获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_TASK);
		if(null!=listModuleOperateConfig){
			for(ModuleOperateConfig vo:listModuleOperateConfig){
				view.addObject(vo.getOperateType(),ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_TASK);
		return view;
	}
	/**
	 * 跳转任务添加页面
	 * @return
	 */
	@RequestMapping("/addTaskPage")
	public ModelAndView newTask(String version){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/task/addTask");
		view.addObject("userInfo",userInfo);
		
		if(StringUtils.isNotEmpty(version) && "2".equals(version)){
			view.setViewName("/task/promote/addTask");
		}
		//消息推送方式
		List<DataDic> listClockWay=DataDicContext.getInstance().listTreeDataDicByType("clockWay");
		view.addObject("listClockWay", listClockWay);
		
		return view;
	}

	/**
	 * 弹出任务复制
	 * @param taskId 任务主键
	 * @return org.springframework.web.servlet.ModelAndView
	 * @author LiuXiaoLin
	 * @date 2018/6/20 0020 13:41
	 */
	@RequestMapping("/copyTaskPage")
	public ModelAndView copyTaskPage(Integer taskId){
		UserInfo userInfo = this.getSessionUser();

		//获取被复制的任务信息
		Task task = taskService.getTaskForCopy(taskId,userInfo);
		
		//升级版本号
		String version = task.getVersion();
		
		ModelAndView view = null;

		if(task.getBusType().equals("0")){//普通任务
			view = new ModelAndView("/task"+(StringUtils.isNotEmpty(version) && version.equals("2")?"/promote":"")+"/copyTask");
		}else{//关联任务
			view = new ModelAndView("/task"+(StringUtils.isNotEmpty(version) && version.equals("2")?"/promote":"")+"/copyTaskWithBus");
			view.addObject("busType",task.getBusType());
		}

		view.addObject("userInfo",userInfo);
		view.addObject("task", task);

		//消息推送方式
		List<DataDic> listClockWay=DataDicContext.getInstance().listTreeDataDicByType("clockWay");
		view.addObject("listClockWay", listClockWay);

		return view;
	}

	/**
	 * 获取任务执行人，用于任务复制
	 * @param taskId 任务主键
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @author LiuXiaoLin
	 * @date 2018/6/20 0020 13:41
	 */
	@ResponseBody
	@RequestMapping(value="/listExecutorsForCopy")
	public Map<String,Object> listExecutorsForCopy(Integer taskId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//取得常用人员列表
		List<UserInfo> executors = taskService.listExecutorsForCopy(taskId,sessionUser);
		map.put("status", "y");
		map.put("executors", executors);
		return map;
	}

	/**
	 * 头部快速发布任务
	 * @return
	 */
	@RequestMapping("/addTaskBySimple")
	public ModelAndView addTaskBySimple(){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/task/addTaskBySimple");
		view.addObject("userInfo",userInfo);
		
		//消息推送方式
		List<DataDic> listClockWay=DataDicContext.getInstance().listTreeDataDicByType("clockWay");
		view.addObject("listClockWay", listClockWay);
		return view;
	}
	/**
	 * 跳转任务分解页面
	 * @return
	 */
	@RequestMapping("/resolveTaskPage")
	public ModelAndView resolveTaskPage(Task task){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/task/resolveTask");
		
		//取得父任务的关联项目以及项目阶段名称
		Task ptask = taskService.getTaskBusInfo(task.getParentId(),userInfo);
		//审计版本需要重新指定笤帚页面
		String version  = ptask.getVersion();
		task.setVersion(version);
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			view.setViewName("/task/promote/resolveTask");
		}
		
		view.addObject("userInfo",userInfo);
		task.setTaskName(ptask.getTaskName()+"-子任务");
		view.addObject("task",task);
		view.addObject("ptask",ptask);
		
		
		List<TaskUpfile> taskUpfileList = taskService.listTaskUpfile(task.getParentId(),userInfo.getComId());
		view.addObject("taskUpfileList",taskUpfileList);
		return view;
	}
	/**
	 * 跳转项目阶段添加任务页面
	 * @return
	 */
	@RequestMapping("/addBusTaskPage")
	public ModelAndView addBusTaskPage(Task task){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/task/addBusTask");
		if(StringUtils.isNotEmpty(task.getVersion()) && "2".equals(task.getVersion())){
			view.setViewName("/task/promote/addBusTask");
		}
		task.setBusId("-1".equals(task.getBusId().toString())?null:task.getBusId());//如果为默认值，则赋值为NULL
		view.addObject("userInfo",userInfo);
		if(!CommonUtil.isNull(task.getBusId()) && task.getBusType().equals(ConstantInterface.TYPE_ITEM)){
			//取得任务所在的项目
			Item item = itemService.getItemById(task.getBusId());
			
			task.setBusName(item.getItemName());
			
			if(CommonUtil.isNull(task.getStagedItemId())){
				Integer stageItemId = itemService.getStageItemId(userInfo, task.getBusId());
				//任务所在的项目阶段主键
				item.setStagedItemId(stageItemId);
			}else{
				//任务所在的项目阶段主键
				item.setStagedItemId(task.getStagedItemId());
			}
		
			
			view.addObject("item",item);
		}else if(!CommonUtil.isNull(task.getBusId()) && task.getBusType().equals(ConstantInterface.TYPE_CRM)){
			//取得任务所在的客户
			Customer crm = crmService.getCrmById(task.getBusId());
			task.setBusName(crm.getCustomerName());
		}
		
		//取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		view.addObject("listUsed",listOwners);
		
		//消息推送方式
		List<DataDic> listClockWay=DataDicContext.getInstance().listTreeDataDicByType("clockWay");
		view.addObject("listClockWay", listClockWay);
		return view;
	}
	/**
	 * 创建新任务
	 * @param task 任务信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addTask")
	public ModelAndView addTask(Task task,String[] sendWay,Integer[] copyFilesId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		//取出复制过来的附件放入任务附件集合
		if(null != copyFilesId && copyFilesId.length > 0){//有复制的附件
			if(null == task.getListUpfiles() || task.getListUpfiles().size() <= 0){//未上传新附件
				//新建list集合
				task.setListUpfiles(new ArrayList<Upfiles>());
				//新建附件对象然后添加
				for(int i=0;i<copyFilesId.length;i++){
					Upfiles copyFile = new Upfiles();
					copyFile.setId(copyFilesId[i]);
					task.getListUpfiles().add(copyFile);
				}
			}else{//有新上传的附件
				for(int i=0;i<copyFilesId.length;i++){
					//用于判断是否包含风附件
					boolean hasIn = false;
					//对比复制的附件和新上传的附件是否有包含关系
					for(int j=0;j<task.getListUpfiles().size();j++){
						if(task.getListUpfiles().get(j).getId() == copyFilesId[i]){
							hasIn = true;
							break;
						}
					}
					//不包含时添加
					if(!hasIn){
						Upfiles copyFile = new Upfiles();
						copyFile.setId(copyFilesId[i]);
						task.getListUpfiles().add(copyFile);
					}
				}
			}
		}
		taskService.addTask(task,null,userInfo,sendWay);
		this.setNotification(Notification.SUCCESS, "添加成功!");

		ModelAndView view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);

		return view;
	}
	/**
	 * 任务分解
	 * @param task 子任务信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/resolveTask",method=RequestMethod.POST)
	public Map<String,Object> resolveTask(Task task,String[] sendWay) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		/*//获取被分解的任务主键
		int parentId = task.getParentId();*/
		//企业号
		task.setComId(userInfo.getComId());
		//创建人
		task.setCreator(userInfo.getId());
		//负责人
		task.setOwner(userInfo.getId());
		//操作人
		task.setOperator(userInfo.getId());
		//删除标识(正常)
		task.setDelState(0);
		//任务状态标识
		task.setState(1);
		Integer taskId = taskService.addTask(task,null,userInfo,sendWay);
		Task sonTask = taskService.getTask(taskId,userInfo);//获取任务配置信息
		String sonTaskJson = this.sonTaskJson(sonTask);
		map.put("status", "y");
		map.put("sonTaskJson", sonTaskJson);
		return map;
	}
	/**
	 * 构建子任务Json
	 * @param sonTask
	 * @return
	 */
	private String sonTaskJson(Task sonTask){
		//生成任务参与人JSon字符串
		StringBuffer sonTaskJson = new StringBuffer();
		if(!CommonUtil.isNull(sonTask)){
			sonTaskJson.append("{'taskId':'"+sonTask.getId()+"','taskName':'"+sonTask.getTaskName()+"','executorGender':'"+sonTask.getExecutorGender()+"','executorUuid':'"+sonTask.getExecutorUuid()+"','executorFileName':'"+sonTask.getExecutorFileName()+"','executorName':'"+sonTask.getExecutorName()+"'");
			sonTaskJson.append(",'gender':'"+sonTask.getGender()+"','state':'"+sonTask.getState()+"','uuid':'"+sonTask.getUuid()+"','filename':'"+sonTask.getFilename()+"','ownerName':'"+sonTask.getOwnerName()+"'}");
		}
		return sonTaskJson.toString();
	}
	/**
	 * 核对任务是否启动，如果否，则直接删除掉
	 * @param taskId
	 * @throws Exception
	 */
	@RequestMapping("/checkTaskState")
	public void checkTaskState(Integer taskId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		Task task = taskService.getTaskById(taskId, userInfo);
		if(null!= task && "-1".equals(task.getState().toString())){
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(task.getId());
			taskService.delTask(ids,userInfo);
		}
	}

	/**
	 * 项目阶段内发布任务方法
	 * @param task 任务信息
	 * @param idType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/addBusTask",method=RequestMethod.POST)
	public Map<String,Object> addBusTask(Task task, String idType, String[] sendWay, Integer[] copyFilesId) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//企业号
		task.setComId(userInfo.getComId());
		//创建人
		task.setCreator(userInfo.getId());
		//负责人
		task.setOperator(userInfo.getId());
		//删除标识(正常)
		task.setDelState(0);
		//任务状态标识
		task.setState(1);

		//取出复制过来的附件放入任务附件集合
		if(null != copyFilesId && copyFilesId.length > 0){//有复制的附件
			if(null == task.getListUpfiles() || task.getListUpfiles().size() <= 0){//未上传新附件
				//新建list集合
				task.setListUpfiles(new ArrayList<Upfiles>());
				//新建附件对象然后添加
				for(int i=0;i<copyFilesId.length;i++){
					Upfiles copyFile = new Upfiles();
					copyFile.setId(copyFilesId[i]);
					task.getListUpfiles().add(copyFile);
				}
			}else{//有新上传的附件
				for(int i=0;i<copyFilesId.length;i++){
					//用于判断是否包含风附件
					boolean hasIn = false;
					//对比复制的附件和新上传的附件是否有包含关系
					for(int j=0;j<task.getListUpfiles().size();j++){
						if(task.getListUpfiles().get(j).getId() == copyFilesId[i]){
							hasIn = true;
							break;
						}
					}
					//不包含时添加
					if(!hasIn){
						Upfiles copyFile = new Upfiles();
						copyFile.setId(copyFilesId[i]);
						task.getListUpfiles().add(copyFile);
					}
				}
			}
		}

		taskService.addTask(task,null,userInfo,sendWay);
		map.put("status", "y");
		this.setNotification(Notification.SUCCESS, "发布成功!");
		return map;
	}
	
	/**
	 * 删除任务
	 * @param ids
	 * @param redirectPage
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/delTask")
	public ModelAndView delTask(Integer[] ids,String redirectPage) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		//添加日志
		if(null!=ids && ids.length>0){
			//删除任务
			taskService.delPreTask(ids, userInfo);
		}
		ModelAndView mav = new ModelAndView();
		this.setNotification(Notification.SUCCESS, "删除成功!");
		mav.setViewName("redirect:"+redirectPage);
		return mav;
	}
	/**
	 * 查看任务
	 * @param id 任务主键
	 * @param parameters 筛选参数
	 * @return
	 */
	@RequestMapping(value="/viewTask")
	public ModelAndView viewTask(HttpServletRequest request,Integer id,String redirectPage,Task parameters,Integer clockId){
		
		ModelAndView view = null;
		UserInfo userInfo = this.getSessionUser();
		//查看权限验证
		if(taskService.authorCheck(userInfo,id,clockId)){
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASK, id);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				viewRecordService.addViewRecord(userInfo,viewRecord);
			}
			Task task = taskService.getTask(id,userInfo);
			//判断任务查看人是否是任务负责人 && 任务是正常执行的
			boolean editFlag = (task.getState()==1 || task.getState()==0) && (userInfo.getId().equals(task.getOwner()));
			String version = task.getVersion();
			if(editFlag){
				if(StringUtils.isNotEmpty(version) && version.equals("2")){
					//任务未办结，且为创建人才能编辑
					view = new ModelAndView("/task/promote/editTask");
				}else{
					//任务未办结，且为创建人才能编辑
					view = new ModelAndView("/task/editTask");
				}
				//取得父任务的项目以及阶段信息
				Task ptask = taskService.getTaskBusInfo(task.getParentId(), userInfo);
				view.addObject("ptask",ptask);
				
				//判断是否有编辑权限
				ModuleOperateConfig modOptConf = modOptConfService.getModuleOperateConfig(userInfo.getComId(),
						ConstantInterface.TYPE_TASK, "update");
				if(null!=modOptConf){
					view.addObject("editTask","no");
				}
				
			}else{
				if(StringUtils.isNotEmpty(version) && version.equals("2")){
					view = new ModelAndView("/task/promote/viewTask");
				}else{
					view = new ModelAndView("/task/viewTask");
					
				}
			}
			
			
			//求情来自于标识from 连接来自何处标识符；taskListPage表示来自我参与的列表，taskToDoListPage表示来自待办任务列表
			view.addObject("from","taskListPage");
			view.addObject("parameters",parameters);
			view.addObject("task",task);
			
			view.addObject("listTaskSharer",task.getListTaskSharer());
			view.addObject("listSonTask",task.getListSonTask());
			view.addObject("userInfo",userInfo);
			
			//默认不是执行人
			boolean executeState = false;
			List<TaskExecutor> listTaskExecutor = task.getListTaskExecutor();
			if(null != listTaskExecutor && !listTaskExecutor.isEmpty()){
				for (TaskExecutor taskExecutor : listTaskExecutor) {
					if(taskExecutor.getExecutor().equals(userInfo.getId())){
						//是执行人
						executeState = true;
						break;
					}
				}
			}
			//不是执行人，则将待办数据移除
			if(!executeState){
				todayWorksService.updateTodayWorksBusSpecTo0(ConstantInterface.TYPE_TASK, task.getId(), userInfo.getId());
			}
			
		}else{
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有查看权限");
			
		}
		//查看任务，删除消息提醒
		todayWorksService.updateTodoWorkRead(id,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASK,clockId);
		return view;
	}
	/**
	 * 异步取得任务的执行人员集合
	 * @param taskId 任务主键
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxListTaskExecutors")
	public Map<String, Object> ajaxListTaskExecutors(Integer taskId){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessioUser = this.getSessionUser();
		List<TaskExecutor> listTaskExecutor = taskService.listTaskExecutor(taskId, sessioUser);
		map.put("list", listTaskExecutor);
		return map;
	}
	/**
	 * 认领任务信息
	 * @param taskId 任务主键信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/acceptTask")
	public Map<String, Object> acceptTask(Integer taskId,String version){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessioUser = this.getSessionUser();
		if(StringUtils.isNotEmpty(version) && version.equals("2")) {
			map = taskPromoteService.updateAcceptTask(taskId, sessioUser);
		}else {
			map = taskService.updateAcceptTask(taskId, sessioUser);
		}
		return map;
	}
	
	/**
	 * 汇报任务进度
	 */
	@ResponseBody
	@RequestMapping(value="/taskProgressReport")
	public Map<String, Object> taskProgressReport(Task task){
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		task.setComId(userInfo.getComId());
		Map<String, Object> map = taskService.updateTaskProgress(task,userInfo);
		return map;
	}
	/**
	 * 汇报任务进度
	 */
	@ResponseBody
	@RequestMapping(value="/taskGradeUpdate")
	public Map<String, Object> taskGradeUpdate(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		task.setComId(userInfo.getComId());
		String gradeName = DataDicContext.getInstance().getCurrentPathZvalue("grade", task.getGrade());
		boolean succ = taskService.updateTaskGrade(task,userInfo,gradeName);
		Map<String, Object> map = new HashMap<String, Object>();
		if(succ){
			map.put("status", "y");
			map.put("info", "更新成功");
		}else{
			map.put("status", "f");
			map.put("info", "更新失败");
		}
		return map;
	}
	/**
	 * 完成时限变更
	 */
	@ResponseBody
	@RequestMapping(value="/taskDealTimeLimitUpdate")
	public String taskDealTimeLimitUpdate(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		task.setComId(userInfo.getComId());
		boolean succ = taskService.updateTaskDealTimeLimit(task,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 * 任务名称变更
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/taskExpectTimeUpdate")
	public HttpResult<String> taskExpectTimeUpdate(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		task.setComId(userInfo.getComId());
		boolean succ = taskService.updateTaskExpectTime(task,userInfo);
		if(succ){
			return new HttpResult<String>().ok("更新成功");
		}else{
			return new HttpResult<String>().error("更新失败");
		}
	}
	
	/**
	 * 任务名称变更
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/taskNameUpdate")
	public String taskNameUpdate(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		task.setComId(userInfo.getComId());
		boolean succ = taskService.updateTaskName(task,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 * 任务说明变更
	 */
	@ResponseBody
	@RequestMapping(value="/taskTaskRemarkUpdate")
	public String taskTaskRemarkUpdate(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			return "连接已断开，请重新登录";
		}
		task.setComId(userInfo.getComId());
		boolean succ = taskService.updateTaskTaskRemark(task,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 * 任务母任务关联
	 * @param task
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/taskParentIdUpdate")
	public Map<String, Object> taskParentIdUpdate(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		//查询任务当前设置父任务的父节点，包括父任务自己
		List<Task> parentTasks = taskService.listTaskOfAllParent(task.getParentId(),userInfo.getComId());
		if(null!=parentTasks && parentTasks.size()>0){
			//验证是否为自己的子任务
			for (Task taskTemp : parentTasks) {
				if(taskTemp.getId().equals(task.getId())){
					map.put("status", "n");
					map.put("info", "不能关联子任务");
					return map;
				}
			}
		}
		task.setComId(userInfo.getComId());
		//父任务
		Task ptask = taskService.getTaskBusInfo(task.getParentId(), userInfo);
		boolean succ = taskService.updateTaskParentId(task,userInfo,parentTasks,ptask);
		if(succ){
			map.put("status", "y");
			map.put("info", "关联成功");
			//取得父任务是否关联有项目
			map.put("ptask", ptask);
			//用户返回页面信息
			Task retTask = taskService.getTaskBusInfo(task.getId(), userInfo);
			map.put("task", retTask);
			this.setNotification(Notification.SUCCESS, "关联成功！");
		}else{
			map.put("status", "n");
			map.put("info", "关联失败");
		}
		return map;
	}

	/**
	 * 解除与母任务之间的关联关系
	 * @param task
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/delpTaskRelation")
	public Task delpTaskRelation(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		task.setComId(userInfo.getComId());
		
		
		boolean succ = taskService.delpTaskRelation(task,userInfo);
		
		//用于返回客户端
		Task retRask = taskService.getTaskBusInfo(task.getId(), userInfo);
		if(succ){
			retRask.setSucc(true);
			retRask.setPromptMsg("解除关联任务成功");
			this.setNotification(Notification.SUCCESS, "解除关联任务成功！");
		}else{
			retRask.setSucc(false);
			retRask.setPromptMsg("解除关联任务失败");
		}
		return retRask;
	}

	/**
	 * 项目关联
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/taskBusIdUpdate")
	public Map<String,Object> taskBusIdUpdate(Task task) throws Exception{
		//TODO 检查一下任务添加关联模块类型是否还有漏洞
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		task.setComId(userInfo.getComId());
		
		boolean succ = taskService.updateTaskBusId(task,userInfo);
		if(succ){
			map.put("info", "操作成功");
			if(task.getBusType().equals(ConstantInterface.TYPE_ITEM)){
				Task taskT = taskService.getTaskBusInfo(task.getId(), userInfo);
				map.put("stagedItemId", taskT.getStagedItemId());
				map.put("stagedItemName", taskT.getStagedItemName());
				
			}
		}else{
			map.put("info", "操作失败");
		}
		map.put("status", "y");
		return map;
	}
	/**
	 * 删除项目关联
	 * @param task
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/delTaskBusRelation")
	public Map<String,Object> delTaskBusRelation(Task task) throws Exception{
		//TODO 检查一下任务添加关联模块类型是否还有漏洞
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		task.setComId(userInfo.getComId());
		
		
		boolean succ = taskService.delTaskBusRelation(task,userInfo);
		if(succ){
			map.put("info","操作成功");
		}else{
			map.put("info","操作失败");
		}
		map.put("status", "y");
		return map;
	}
	/**
	 * 项目阶段关联
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/taskStageIdUpdate")
	public Map<String,Object> taskStageIdUpdate(Task task) throws Exception{
		//TODO 检查一下任务添加关联模块类型是否还有漏洞
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		taskService.updateStagedId(task,userInfo);
		map.put("status", "y");
		map.put("info", "操作成功!");
		return map;
	}
	/**
	 * 任务负责人变更
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/taskOwnerUpdate")
	public String taskOwnerUpdate(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		//企业编号
		task.setComId(userInfo.getComId());
		//当前操作人员
		task.setOperator(userInfo.getId());
		UserInfo ownerInfo = userInfoService.getUserInfo(userInfo.getComId(),task.getOwner());
		task.setOwnerName(ownerInfo.getUserName());
		boolean succ = taskService.updateTaskOwner(task,userInfo);
		if(succ){
			//模块日志添加
			taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务负责人变更为\""+ownerInfo.getUserName()+"\"成功");
			return "变更成功";
		}else{
			//模块日志添加
			taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务负责人变更为\""+ownerInfo.getUserName()+"\"失败");
			return "变更失败";
		}
	}
	/**
	 * 任务执行人更新（暂时没有使用）
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/taskExecutorUpdate")
	public String taskExecutorUpdate(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		//企业编号
		task.setComId(userInfo.getComId());
		//当前操作员
		task.setOperator(userInfo.getId());
		UserInfo executorInfo = userInfoService.getUserInfo(userInfo.getComId(),task.getExecutor());
		task.setExecutorName(executorInfo.getUserName());
		boolean succ = taskService.updateTaskExecutor(task,userInfo);
		if(succ){
			//模块日志添加
			taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务执行人变更为\""+executorInfo.getUserName()+"\"成功");
			return "变更成功";
		}else{
			//模块日志添加
			taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务执行人变更为\""+executorInfo.getUserName()+"\"失败");
			return "变更失败";
		}
	}
	/**
	 * 任务参与人变更
	 * @param taskId
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/taskSharerUpdate")
	public String taskSharerUpdate(Integer taskId,Integer[] userIds) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		boolean succ = taskService.updateTaskSharer(userInfo.getComId(),taskId,userIds,userInfo);
		if(succ){
			return "变更成功";
		}else{
			return "变更失败";
		}
	}
	/**
	 * 删除任务参与人
	 * @param taskId
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delTaskSharer")
	public String delTaskSharer(Integer taskId,Integer userId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		UserInfo sharerInfo = userInfoService.getUserInfo(userInfo.getComId(),userId);
		boolean succ = taskService.delTaskSharer(userInfo.getComId(),taskId,userId,userInfo,sharerInfo.getUserName());
		if(succ){
			//模块日志添加
			taskService.addTaskLog(userInfo.getComId(),taskId, userInfo.getId(), userInfo.getUserName(), "删除任务参与人\""+sharerInfo.getUserName()+"\"成功");
			return "删除成功";
		}else{
			//模块日志添加
			taskService.addTaskLog(userInfo.getComId(),taskId, userInfo.getId(), userInfo.getUserName(), "删除任务参与人\""+sharerInfo.getUserName()+"\"失败");
			return "删除失败";
		}
	}
	/**
	 * 任务标记
	 */
	@ResponseBody
	@RequestMapping(value="/remarkTaskState")
	public Task remarkTaskState(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		task.setComId(userInfo.getComId());
		//改变任务执行状态为4完成状态
		String stateName =(4==task.getState()?"完成":(3==task.getState()?"暂停":(1==task.getState()?"执行":"未知")));
		//默认操作成功
		boolean succ = true;
		
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){//操作升级任务
			if(4==task.getState()){//完成。
				succ = taskPromoteService.updateFinishTask(task,userInfo,stateName);
			}else if(1==task.getState()){//重启-- 
				succ = taskPromoteService.updateRestarTask(task,userInfo,stateName);
			}else if(3==task.getState()){//暂停
				succ = taskPromoteService.updatePauseTask(task,userInfo,stateName);
			}
		}else{
			if(4==task.getState()){//完成。
				succ = taskService.updateFinishTask(task,userInfo,stateName);
			}else if(1==task.getState()){//重启-- 
				succ = taskService.updateRestarTask(task,userInfo,stateName);
			}else if(3==task.getState()){//暂停
				succ = taskService.updatePauseTask(task,userInfo,stateName);
			}
		}
		
		
		
		if(succ){
			Task backTask = new Task();
			backTask.setSucc(true);
			backTask.setPromptMsg("任务标记为“"+stateName+"”成功！");
			return backTask;
		}else{
			Task backTask = new Task();
			backTask.setSucc(false);
			backTask.setPromptMsg("更新失败");
			return backTask;
		}
	}
	/**
	 * 任务标记(用于升级任务)
	 */
	@ResponseBody
	@RequestMapping(value="/remarkTaskExecuteState")
	public HttpResult<String> remarkTaskExecuteState(Task task) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		task.setComId(userInfo.getComId());
		//改变任务执行状态为4完成状态
		String stateName =(4==task.getState()?"完成":(3==task.getState()?"暂停":(1==task.getState()?"执行":"未知")));
		//默认操作成功
		boolean succ = true;
		
		String version = task.getVersion();
		if(StringUtils.isEmpty(version) || !version.equals("2")) {
			return new HttpResult<String>().error("更新失败");
		}
		
		if(1==task.getState()){//重启-- 
			succ = taskPromoteService.updateStarExecuteTask(task.getId(), userInfo, stateName);
		}else if(3==task.getState()){//暂停
			succ = taskPromoteService.updatePauseExecuteTask(task.getId(), userInfo, stateName);
		}
		
		if(succ){
			return new HttpResult<String>().ok("任务标记为“"+stateName+"”成功！");
		}else{
			return new HttpResult<String>().error("更新失败");
		}
	}
	/**
	 * 任务留言页面
	 * @param taskTalk
	 * @return
	 */
	@RequestMapping(value="/taskTalkPage")
	public ModelAndView taskTalk(TaskTalk taskTalk){
		ModelAndView view = new ModelAndView("/task/taskTalk");
		UserInfo userInfo = this.getSessionUser();
		//查看任务讨论，删除消息提醒
		todayWorksService.updateTodoWorkRead(taskTalk.getTaskId(),userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASK,0);

		view.addObject("userInfo", userInfo);
		List<TaskTalk> listTaskTalk = taskService.listTaskTalk(taskTalk.getTaskId(), userInfo.getComId());
		view.addObject("listTaskTalk",listTaskTalk);

		view.addObject("taskTalk",taskTalk);

		//获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_TASK);
		if(null!=listModuleOperateConfig){
			for(ModuleOperateConfig vo:listModuleOperateConfig){
				view.addObject(vo.getOperateType(),ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		return view;
	}
	/**
	 * ajax添加任务留言
	 * @param taskTalk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddTaskTalk")
	public TaskTalk ajaxAddTaskTalk(TaskTalk taskTalk) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		taskTalk.setComId(userInfo.getComId());
		taskTalk.setSpeaker(userInfo.getId());
		Integer id = taskService.addTaskTalk(taskTalk,userInfo);
		taskTalk = taskService.queryTaskTalk(id, userInfo.getComId());
		//自己添加的，当前自己肯定是叶子
		taskTalk.setIsLeaf(1);
		String taskTalkDivString = replyTalkDivString(taskTalk,"listUpfiles_"+taskTalk.getId()+".upfileId","filename","otherTaskAttrIframe",userInfo.getComId());
		taskTalk.setTaskTalkDivString(taskTalkDivString);
		//模块日志添加
		taskService.addTaskLog(userInfo.getComId(),taskTalk.getTaskId(), userInfo.getId(), userInfo.getUserName(), "添加留言");
		return taskTalk;
	}
	/**
	 * 回复留言
	 * @param taskTalk
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/replyTalk")
	public TaskTalk replyTalk(TaskTalk taskTalk) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			return null;
		}
		taskTalk.setComId(userInfo.getComId());
		taskTalk.setSpeaker(userInfo.getId());
		Integer id = taskService.addTaskTalk(taskTalk,userInfo);
		taskTalk = taskService.queryTaskTalk(id, userInfo.getComId());
		//自己添加的，当前自己肯定是叶子
		taskTalk.setIsLeaf(1);
		String taskTalkDivString = replyTalkDivString(taskTalk,"listUpfiles_"+taskTalk.getId()+".upfileId","filename","otherTaskAttrIframe",userInfo.getComId());
		taskTalk.setTaskTalkDivString(taskTalkDivString);
		//模块日志添加
		taskService.addTaskLog(userInfo.getComId(),taskTalk.getTaskId(), userInfo.getId(), userInfo.getUserName(), "添加留言");
		return taskTalk;
	}
	/**
	 * 讨论回复DIV字符串生存
	 * @param taskTalk
	 * @param uploadFileName
	 * @param uploadFileShowName
	 * @param pifreamId
	 * @param comId
	 * @return
	 */
	private String replyTalkDivString(TaskTalk taskTalk,String uploadFileName,String uploadFileShowName,String pifreamId,Integer comId){

		if(null==taskTalk) {
			return null;
		}
		//是子节点
		taskTalk.setIsLeaf(1);
		String sid = RequestContextHolderUtil.getRequest().getParameter("sid");
		UserInfo userInfo = this.getSessionUser();
		StringBuffer divString = new StringBuffer();
		
		if(taskTalk.getParentId().equals(-1)){//是留言
			divString.append("\n <div id='talk_"+taskTalk.getId()+"' class='ws-shareBox'>");
		}else{//是回复
			divString.append("\n <div id='talk_"+taskTalk.getId()+"' class='ws-shareBox ws-shareBox2'>");
		}
		//头像
		divString.append("\n <div class='shareHead' data-container='body' data-toggle='popover'data-user='"+userInfo.getId()+"' data-busId='"+taskTalk.getTaskId()+"' data-busType='"+ConstantInterface.TYPE_TASK+"'>");
		
		divString.append("<img src=\"/downLoad/userImg/"+taskTalk.getComId()+"/"+taskTalk.getSpeaker()+"?sid="+sid+"\" title=\""+taskTalk.getSpeakerName()+"\"></img>");
		divString.append("\n </div>");
		//头像结束
		
		divString.append("\n <div class='shareText'>");
		divString.append("\n 	<span class='ws-blue'>"+taskTalk.getSpeakerName()+"</span>");
		if(!taskTalk.getParentId().equals(-1)){//是留言
			divString.append("\n<r>回复</r>");
			divString.append("\n<span class='ws-blue'>"+taskTalk.getpSpeakerName()+"</span>");
			
		}
		divString.append("\n<p class='ws-texts'>"+StringUtil.toHtml(taskTalk.getContent())+"</p>");
		
		//附件
		List<TaskTalkUpfile> list = taskTalk.getListTaskTalkFile();
		if(null!=list && list.size()>0){
			divString.append("<div class=\"file_div\">");
			for (int i=0;i<list.size();i++) {
				TaskTalkUpfile upfiles = list.get(i);
				if("1".equals(upfiles.getIsPic())){
					divString.append("<p class=\"p_text\">");
					divString.append("附件（"+(i+1)+"）：");
					divString.append("<img onload=\"AutoResizeImage(350,0,this,'otherTaskAttrIframe')\"");
					divString.append("src=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid="+this.getSid()+"\" />");
					divString.append("&nbsp;<a class=\"fa fa-download\" title=\"下载\" href=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid="+this.getSid()+"\"></a>");
					divString.append("&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"showPic('/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"','"+this.getSid()+"','"+upfiles.getUpfileId()+"','"+ConstantInterface.TYPE_TASK+"','"+taskTalk.getTaskId()+"')\"></a>");
					divString.append("</p>");
				}else{
					divString.append("<p class=\"p_text\">");
					divString.append("附件（"+(i+1)+"）：");
					divString.append(upfiles.getFilename());
					if(upfiles.getFileExt().equals("doc")||
						upfiles.getFileExt().equals("docx")||
						upfiles.getFileExt().equals("xls")||
						upfiles.getFileExt().equals("xlsx")||
						upfiles.getFileExt().equals("ppt")||
						upfiles.getFileExt().equals("pptx")){
						divString.append("&nbsp;<a class=\"fa fa-download\" href=\"javascript:void(0);\" title=\"下载\" onclick=\"downLoad('"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+this.getSid()+"')\"></a>");
						divString.append("&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"viewOfficePage('"+upfiles.getUpfileId()+"','"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+upfiles.getFileExt()+"','"+this.getSid()+"','"+ConstantInterface.TYPE_TASK+"','"+taskTalk.getTaskId()+"')\"></a>");
					}else if(upfiles.getFileExt().equals("pdf")||upfiles.getFileExt().equals("txt")){
						divString.append("&nbsp;<a class=\"fa fa-download\" title=\"下载\" href=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid="+this.getSid()+"\"></a>");
						divString.append("&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"viewOfficePage('"+upfiles.getUpfileId()+"','"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+upfiles.getFileExt()+"','"+this.getSid()+"','"+ConstantInterface.TYPE_TASK+"','"+taskTalk.getTaskId()+"')\"></a>");
					}else{
						divString.append("&nbsp;<a class=\"fa fa-download\" href=\"javascript:void(0);\" title=\"下载\" onclick=\"downLoad('"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+this.getSid()+"')\"></a>");
					}
				}
				divString.append("</p>");
			}
			divString.append("</div>");
		}
		divString.append("\n 	<div class='ws-type'>");
		//判断是否有编辑权限
		ModuleOperateConfig modOptConf = modOptConfService.getModuleOperateConfig(userInfo.getComId(),
				ConstantInterface.TYPE_TASK, "delete");
		//发言人可以删除自己的发言
		if(userInfo.getId().equals(taskTalk.getSpeaker()) && null==modOptConf){
			divString.append("\n 	<a href='javascript:void(0);' id=\"delOpt_"+taskTalk.getId()+"\" class='fa fa-trash-o' title='删除' onclick=\"delTalk('"+taskTalk.getId()+"','1')\"></a>");
		}
		//项目没有办结可以讨论
		divString.append("\n 	<a id=\"img_"+taskTalk.getId()+"\" name=\"replyImg\" href=\"javascript:void(0);\" class=\"fa fa-comment-o\" title=\"回复\" onclick=\"showArea('"+taskTalk.getId()+"')\"></a>");
		divString.append("\n 		<time>"+taskTalk.getRecordCreateTime()+"</time>");
		divString.append("\n 		</div>");
		divString.append("\n 	</div>");
		divString.append("\n 	<div class=\"ws-clear\"></div>");
		divString.append("\n </div>");
		//回复层
		divString.append("\n <div id=\"reply_"+taskTalk.getId()+"\" name=\"replyTalk\" style=\"display:none;\" class=\"ws-shareBox ws-shareBox2 ws-shareBox3\">");
		divString.append("\n 	<div class=\"shareText\">");
		divString.append("\n 		<div class=\"ws-textareaBox\" style=\"margin-top:10px;\">");
		divString.append("\n 			<textarea id=\"operaterReplyTextarea_"+taskTalk.getId()+"\" name=\"operaterReplyTextarea_"+taskTalk.getId()+"\" class=\"form-control\" placeholder=\"回复……\"></textarea>");
		divString.append("\n 			<div class=\"ws-otherBox\">");
		divString.append("\n 				<div class=\"ws-meh\">");
		//表情
		divString.append("\n 					<a href=\"javascript:void(0);\" class=\"fa fa-meh-o tigger\" id=\"biaoQingSwitch_"+taskTalk.getId()+"\" onclick=\"addBiaoQingObj('biaoQingSwitch_"+taskTalk.getId()+"','biaoQingDiv_"+taskTalk.getId()+"','operaterReplyTextarea_"+taskTalk.getId()+"');\"></a>");
		//表情DIV层
		divString.append("\n 					<div id=\"biaoQingDiv_"+taskTalk.getId()+"\" class=\"blk\" style=\"display:none;position:absolute;width:200px;top:100px;z-index:99;left: 15px\">");
		divString.append("\n 						<div class=\"main\">");
		divString.append("\n 							<ul style=\"padding: 0px\">");
		divString.append(CommonUtil.biaoQingStr());	
		divString.append("\n 							</ul>");
		divString.append("\n 						</div>");
		divString.append("\n 					</div>");
		divString.append("\n 				</div>");
		//常用意见
		divString.append("\n 				<div class=\"ws-plugs\">");
		divString.append("\n 					<a href=\"javascript:void(0);\" class=\"fa fa-comments-o\" onclick=\"addIdea('operaterReplyTextarea_"+taskTalk.getId()+"','"+sid+"');\" title=\"常用意见\"></a>");
		divString.append("\n 				</div>");
		//@机制
		divString.append("\n				<div class=\"ws-plugs\">");
		divString.append("\n					<a class=\"btn-icon\" title=\"告知人员\"  href=\"javascript:void(0)\" data-todoUser=\"yes\" data-relateDiv=\"todoUserDiv_" +taskTalk.getId()+ "\">@</a>");
		divString.append("\n				</div>");
//		//提醒方式
//		divString.append("\n 				<div class=\"ws-remind\">");
//		divString.append("\n					<span class=\"ws-remindTex\">提醒方式：</span>");
//		divString.append("\n					<div class=\"ws-checkbox\">");
//		divString.append("\n						<label class=\"checkbox-inline\">");
//		divString.append("\n							<input id=\"inlineCheckbox1\" type=\"checkbox\" value=\"option1\">短信</label>");
//		divString.append("\n						<label class=\"checkbox-inline\">");
//		divString.append("\n							<input id=\"inlineCheckbox1\" type=\"checkbox\" value=\"option1\">邮件</label>");
//		divString.append("\n						<label class=\"checkbox-inline\">");
//		divString.append("\n							<input id=\"inlineCheckbox1\" type=\"checkbox\" value=\"option1\">桌面推送</label>");
//		divString.append("\n					</div>");
//		divString.append("\n				</div>");
		//分享按钮
		divString.append("\n				<div class=\"ws-share\">");
		divString.append("\n					<button type=\"button\" class=\"btn btn-info ws-btnBlue\" data-relateTodoDiv=\"todoUserDiv_" + taskTalk.getId() + "\" onclick=\"replyTalk(" + taskTalk.getTaskId() + "," + taskTalk.getId()+",this)\">回复</button>");
		divString.append("\n				</div>");
		divString.append("\n				<div style=\"clear: both;\"></div>");
		//@机制
		divString.append("\n				<div id=\"todoUserDiv_" + taskTalk.getId() + "\" class=\"padding-top-10\"> ");
		divString.append("\n        		</div>");
		divString.append("\n				<div class=\"ws-notice\">");
		divString.append("\n 			<div class=\"ws-notice\">");
		//构建附件上传控件
		divString.append(CommonUtil.uploadFileTagStr(uploadFileName,uploadFileShowName,pifreamId,userInfo.getComId(),sid));
		divString.append("\n 			</div>");
		divString.append("\n 		</div>");
		divString.append("\n 	</div>");
		divString.append("\n </div>");
		divString.append("\n</div>");
		return divString.toString();
	}
	/**
	 * 删除任务留言
	 * @param taskTalk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/ajaxDelTaskTalk")
	public String ajaxDelTaskTalk(TaskTalk taskTalk) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		taskTalk.setComId(userInfo.getComId());
		taskService.delTaskTalk(taskTalk,userInfo);
		//模块日志添加
		taskService.addTaskLog(userInfo.getComId(),taskTalk.getTaskId(), userInfo.getId(), userInfo.getUserName(), "删除留言");
		return "删除成功！";
	}
	/**
	 * 查看任务日志
	 * @param taskLog
	 * @return
	 */
	@RequestMapping("/taskLogPage")
	public ModelAndView taskLogPage(TaskLog taskLog){
		ModelAndView view = new ModelAndView("/task/taskLog");
		UserInfo userInfo = this.getSessionUser();
		//查看任务日志，删除消息提醒
		todayWorksService.updateTodoWorkRead(taskLog.getTaskId(),userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASK,0);
		
		List<TaskLog> listTaskLog = taskService.listTaskLog(taskLog.getTaskId(), userInfo.getComId());
		view.addObject("listTaskLog",listTaskLog);
		return view;
	}
	/**
	 * 查看任务移交记录
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/taskFlowRecord")
	public ModelAndView taskFlowRecord(Integer taskId){
		ModelAndView view = new ModelAndView("/task/taskFlowRecord");
		UserInfo userInfo = this.getSessionUser();
		
		//查看移交记录，删除消息提醒
		todayWorksService.updateTodoWorkRead(taskId,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASK,0);
		
		//移交记录
		List<FlowRecord> listFlowRecord = new ArrayList<FlowRecord>();
		
		listFlowRecord = taskService.listFlowRecord(taskId, userInfo.getComId());
		//任务详情状态
		Task task = null;
		if(null==listFlowRecord || listFlowRecord.isEmpty()){
			task=taskService.getTaskById(taskId, userInfo);
			
			FlowRecord flowRecord = new FlowRecord();
			flowRecord.setUserId(task.getCreator());
			flowRecord.setAcceptDate(task.getRecordCreateTime());
			flowRecord.setUserName(task.getOwnerName());
			flowRecord.setUuid(task.getUuid());
			flowRecord.setGender(task.getGender());
			listFlowRecord.add(flowRecord);
		}else{
			task=taskService.getTaskById(taskId, userInfo);
		}
		//任务办结
		if(task.getState()==4){
			//倒叙
			Collections.reverse(listFlowRecord);
			
			FlowRecord flowRecord = listFlowRecord.remove(0);
			flowRecord.setState(ConstantInterface.FINISHED_YES);
			listFlowRecord.add(0,flowRecord);
		}
		view.addObject("task",task);
		view.addObject("listFlowRecord",listFlowRecord);
		return view;
	}

	/**
	 * 查看任务移交记录
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/taskViewRecord")
	public ModelAndView taskViewRecord(Integer taskId){
		ModelAndView view = new ModelAndView("/task/taskViewRecord");
		UserInfo userInfo = this.getSessionUser();
		//浏览的人员
		List<ViewRecord> listViewRecord = viewRecordService.listViewRecord(userInfo,ConstantInterface.TYPE_TASK,taskId);
		view.addObject("listViewRecord", listViewRecord);
		return view;
	}
	/**
	 * 获取此任务以及此任务后代任务以外的任务JSON字符串
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/parentTaskJson")
	public String parentTaskJson(Task task){
		UserInfo userInfo = this.getSessionUser();
		task.setComId(userInfo.getComId());
		String strJson = taskService.taskStrJson(task);
		return strJson;
	}
	/**
	 * 获取任务附件
	 * @param taskUpfile
	 * @return
	 */
	@RequestMapping("/taskUpfilePage")
	public ModelAndView taskUpfilePage(TaskUpfile taskUpfile){
		ModelAndView view = new ModelAndView("/task/taskUpfile");
		UserInfo userInfo = this.getSessionUser();
		
		//查看任务附件，删除消息提醒
		todayWorksService.updateTodoWorkRead(taskUpfile.getTaskId(),userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASK,0);
		
		List<TaskUpfile> listTaskUpfile = taskService.listPagedTaskUpfile(taskUpfile, userInfo.getComId());
		view.addObject("listTaskUpfile",listTaskUpfile);
		view.addObject("taskUpfile", taskUpfile);
		view.addObject("userInfo", userInfo);
		if(null!=listTaskUpfile && listTaskUpfile.size()>0){
			//获取模块操作权限
			List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_TASK);
			if(null!=listModuleOperateConfig){
				for(ModuleOperateConfig vo:listModuleOperateConfig){
					view.addObject(vo.getOperateType(),ConstantInterface.MOD_OPT_STATE_YES);
				}
			}
		}
		return view;
	}
	/**
	 * 获取可关联项目JSON
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/itemJson")
	public String itemJson(Task task){
		//TODO 检查一下任务添加关联模块类型是否还有漏洞
		UserInfo userInfo = this.getSessionUser();
		Item item = new Item();
		item.setComId(userInfo.getComId());
		item.setId(task.getBusId());
		item.setpItemName(task.getBusName());
		String strJson = itemService.itemStrJson(item);
		return strJson;
	}
	/**
	 * 删除任务留言和任务附件
	 * @param taskId 任务转
	 * @param taskFileId 附件关联主键
	 * @param type 类型 task talk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/delTaskUpfile")
	public Map<String, Object> delTaskUpfile(Integer taskId,Integer taskFileId,String type) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		taskService.delTaskUpfile(taskFileId,type,userInfo,taskId);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 跳转任务协同配置页面
	 * @param task
	 * @return
	 */
	@RequestMapping("/handOverTaskPage")
	public ModelAndView handOverTaskPage(Task task,String redirectPage){
		ModelAndView view = new ModelAndView("/task/handOverTask");
		UserInfo userInfo = this.getSessionUser();
		//任务进度
		Integer progress = task.getTaskProgress();
		
		task = taskService.getTaskById(task.getId(),userInfo);
		
		//是升级版本的任务
		String version  = task.getVersion();
		task.setVersion(version);
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			view.setViewName("/task/promote/handOverTask");
		}
		
		String dealTimeLimit = task.getDealTimeLimit();
		
		if(StringUtils.isNotEmpty(dealTimeLimit)){
			Date dealTime = DateTimeUtil.parseDate(dealTimeLimit, DateTimeUtil.yyyy_MM_dd);
			String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			Date nowDate =  DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd);
			if(dealTime.getTime() <= nowDate.getTime()){
				task.setDealTimeLimit(nowDateStr);
			}
		}
		
		
		//若是没有进度，则从数据库取数据
		Integer taskProgress = (null==progress?task.getTaskProgress():progress);
		task.setTaskProgress(taskProgress);
		if(null!=task && 4==task.getState()){
			this.setNotification(Notification.ERROR,"任务已经结束！");
			view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		}else{
			view.addObject("userInfo",userInfo);
			view.addObject("task", task);
			
			view.addObject("redirectPage", redirectPage);
			
			//取得常用人员列表
			List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),4);
			view.addObject("listUsed",listOwners);
			
		}
		//是否能指定下一步骤的人员信息，默认能够
		Integer nextStepUserState = ConstantInterface.ENABLED_YES;
		//任务的执行人信息
		List<TaskExecutor> listTaskExecutor = task.getListTaskExecutor();
		
		//任务执行人已经办结任务信息
		if(null != listTaskExecutor && !listTaskExecutor.isEmpty()){//处理自己是否已经办结
			if(listTaskExecutor.size()>1){//只有一个办理人员，则可以指定
				//任务的推送人员
				Integer pushUser = listTaskExecutor.get(0).getPushUser();
				
				for (TaskExecutor taskExecutor : listTaskExecutor) {
					//任务执行人
					Integer executor = taskExecutor.getExecutor();
					Integer executeState = taskExecutor.getState();
					if(!executor.equals(userInfo.getId()) //除开自己，其他办理人员是否完成
							&& !executeState.equals(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DONE))){
						//有一个人没有完成则不能指定人员
						nextStepUserState = ConstantInterface.ENABLED_NO;
						break;
					}else if(!pushUser.equals(userInfo.getId())){//当前人员不是任务的推送人员，则不可以流转步骤
						nextStepUserState = ConstantInterface.ENABLED_NO;
						break;
					}
				}
				if(nextStepUserState.equals(ConstantInterface.ENABLED_NO)){
					UserInfo pushUserInfo = userInfoService.getUserBaseInfo(userInfo.getComId(), pushUser);
					view.addObject("pushUserInfo",pushUserInfo);
				}
				
			}
			
		}
		view.addObject("nextStepUserState",nextStepUserState);
		return view;
	}
	
	/**
	 * 跳转任务协同配置页面
	 * @param task
	 * @return
	 */
	@RequestMapping("/nextExecutorPage")
	public ModelAndView nextExecutor(Task task,String redirectPage){
		ModelAndView view = new ModelAndView("/task/nextExecutor");
		UserInfo userInfo = this.getSessionUser();
		//任务进度
		Integer progress = task.getTaskProgress();
		
		task = taskService.getTaskById(task.getId(),userInfo);
		
		
		String dealTimeLimit = task.getDealTimeLimit();
		
		if(StringUtils.isNotEmpty(dealTimeLimit)){
			Date dealTime = DateTimeUtil.parseDate(dealTimeLimit, DateTimeUtil.yyyy_MM_dd);
			String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			Date nowDate =  DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd);
			if(dealTime.getTime() <= nowDate.getTime()){
				task.setDealTimeLimit(nowDateStr);
			}
		}
		
		
		//若是没有进度，则从数据库取数据
		Integer taskProgress = (null==progress?task.getTaskProgress():progress);
		task.setTaskProgress(taskProgress);
		if(null!=task && 4==task.getState()){
			this.setNotification(Notification.ERROR,"任务已经结束！");
			view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		}else{
			view.addObject("userInfo",userInfo);
			view.addObject("task", task);
			
			view.addObject("redirectPage", redirectPage);
			
			//取得常用人员列表
			List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),4);
			view.addObject("listUsed",listOwners);
			
		}
		//是否能指定下一步骤的人员信息，默认能够
		Integer nextStepUserState = ConstantInterface.ENABLED_YES;
		//任务的执行人信息
		List<TaskExecutor> listTaskExecutor = task.getListTaskExecutor();
		
		//任务执行人已经办结任务信息
		if(null != listTaskExecutor && !listTaskExecutor.isEmpty()){//处理自己是否已经办结
			if(listTaskExecutor.size()>1){//只有一个办理人员，则可以指定
				//任务的推送人员
				Integer pushUser = listTaskExecutor.get(0).getPushUser();
				
				for (TaskExecutor taskExecutor : listTaskExecutor) {
					//任务执行人
					Integer executor = taskExecutor.getExecutor();
					Integer executeState = taskExecutor.getState();
					if(!executor.equals(userInfo.getId()) //除开自己，其他办理人员是否完成
							&& !executeState.equals(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DONE))){
						//有一个人没有完成则不能指定人员
						nextStepUserState = ConstantInterface.ENABLED_NO;
						break;
					}else if(!pushUser.equals(userInfo.getId())){//当前人员不是任务的推送人员，则不可以流转步骤
						nextStepUserState = ConstantInterface.ENABLED_NO;
						break;
					}
				}
				if(nextStepUserState.equals(ConstantInterface.ENABLED_NO)){
					UserInfo pushUserInfo = userInfoService.getUserBaseInfo(userInfo.getComId(), pushUser);
					view.addObject("pushUserInfo",pushUserInfo);
				}
				
			}
			
		}
		view.addObject("nextStepUserState",nextStepUserState);
		return view;
	}
	/**
	 * 跳转任务协同配置页面
	 * @param task
	 * @return
	 */
	@RequestMapping("/turnBackPage")
	public ModelAndView turnBackPage(Task task,String redirectPage){
		ModelAndView view = new ModelAndView("/task/turnBackTask");
		UserInfo userInfo = this.getSessionUser();
		//任务进度
		Integer progress = task.getTaskProgress();
		
		task = taskService.getTaskById(task.getId(),userInfo);
		
		
		String dealTimeLimit = task.getDealTimeLimit();
		
		if(StringUtils.isNotEmpty(dealTimeLimit)){
			Date dealTime = DateTimeUtil.parseDate(dealTimeLimit, DateTimeUtil.yyyy_MM_dd);
			String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			Date nowDate =  DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd);
			if(dealTime.getTime() <= nowDate.getTime()){
				task.setDealTimeLimit(nowDateStr);
			}
		}
		
		
		//若是没有进度，则从数据库取数据
		Integer taskProgress = (null==progress?task.getTaskProgress():progress);
		task.setTaskProgress(taskProgress);
		if(null!=task && 4==task.getState()){
			this.setNotification(Notification.ERROR,"任务已经结束！");
			view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		}else{
			view.addObject("userInfo",userInfo);
			view.addObject("task", task);
			
			view.addObject("redirectPage", redirectPage);
			
			//取得常用人员列表
			List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),4);
			view.addObject("listUsed",listOwners);
			
		}
		//是否能指定下一步骤的人员信息，默认能够
		Integer nextStepUserState = ConstantInterface.ENABLED_YES;
		//任务的执行人信息
		List<TaskExecutor> listTaskExecutor = task.getListTaskExecutor();
		
		//任务执行人已经办结任务信息
		if(null != listTaskExecutor && !listTaskExecutor.isEmpty()){//处理自己是否已经办结
			if(listTaskExecutor.size()>1){//只有一个办理人员，则可以指定
				//任务的推送人员
				Integer pushUser = listTaskExecutor.get(0).getPushUser();
				
				for (TaskExecutor taskExecutor : listTaskExecutor) {
					//任务执行人
					Integer executor = taskExecutor.getExecutor();
					Integer executeState = taskExecutor.getState();
					if(!executor.equals(userInfo.getId()) //除开自己，其他办理人员是否完成
							&& !executeState.equals(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DONE))){
						//有一个人没有完成则不能指定人员
						nextStepUserState = ConstantInterface.ENABLED_NO;
						break;
					}else if(!pushUser.equals(userInfo.getId())){//当前人员不是任务的推送人员，则不可以流转步骤
						nextStepUserState = ConstantInterface.ENABLED_NO;
						break;
					}
				}
				if(nextStepUserState.equals(ConstantInterface.ENABLED_NO)){
					UserInfo pushUserInfo = userInfoService.getUserBaseInfo(userInfo.getComId(), pushUser);
					view.addObject("pushUserInfo",pushUserInfo);
				}
				
			}
			
		}
		view.addObject("nextStepUserState",nextStepUserState);
		return view;
	}
	/**
	 * 任务状态验证
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkTaskStateForNextExecutor")
	public Task checkTaskStateForNextExecutor(Task task){
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			task.setSucc(false);
			task.setPromptMsg("登录已失效，请重新登录！");
			return task;
		}
		
		task = taskService.getTaskById(task.getId(),userInfo);
		if(null!=task && 4==task.getState()){
			task.setSucc(false);
			task.setPromptMsg("任务已经结束！");
		}else{
			//默认可以委托
			task.setSucc(true);
			
			//其他人员是否没有办结
			List<TaskExecutor> listTaskExecutor = task.getListTaskExecutor();
			if(null!=listTaskExecutor && !listTaskExecutor.isEmpty()){
				for (TaskExecutor taskExecutor : listTaskExecutor) {
					Integer executor = taskExecutor.getExecutor();
					Integer pushUser = taskExecutor.getPushUser();
					if(!executor.equals(userInfo.getId())//除开自己，别人已经全部完成，并把任务返回回来了
							&& !taskExecutor.getState().toString().equals(ConstantInterface.STATIS_TASK_STATE_DONE)
							&& pushUser.equals(userInfo.getId())){
						task.setPromptMsg("任务的其他办理人没有办结！不能委托");
						task.setSucc(false);
						break;
					}
				}
			}
		}
		return task;
	}
	/**
	 * 任务协同配置
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/taskCooperateConfig")
	public Map<String,Object> taskCooperateConfig(Task task,String idType,String redirectPage,String[] sendWay){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		UserInfo userInfo = this.getSessionUser();
		Task taskCheck = taskService.getTaskById(task.getId());
		if(taskCheck.getState().toString().equals(ConstantInterface.STATIS_TASK_STATE_DONE)){
			map.put("status", "f1");
			map.put("info", "任务已办结不能委托!");
			return map;
		}
		
		task.setComId(userInfo.getComId());
		task.setOperator(userInfo.getId());
		
		//任务转办
		map = taskService.updateTaskCooperateConfig(task,userInfo,sendWay);
		if(null!=map.get("status")){
			return map;
		}
		
		map.put("status", "y");
		map.put("info", "任务协同成功!");
		
		//查询任务的执行人员
		Integer executorId = 0;
		List<TaskExecutor> taskExecutors = task.getListTaskExecutor();
		if(null!=taskExecutors && !taskExecutors.isEmpty()){
			for (TaskExecutor taskExecutor : taskExecutors) {
				//任务的执行人
				Integer executor = taskExecutor.getExecutor();
				if(executor.equals(userInfo.getId())){
					executorId = userInfo.getId();
					break;
				}
			}
		}
		map.put("executor", executorId);
		return map;
	}
	/**
	 * 任务协同配置
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/taskTurnBack")
	public Map<String,Object> taskTurnBack(Task task,String idType,String redirectPage,String[] sendWay){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		UserInfo userInfo = this.getSessionUser();
		Task taskCheck = taskService.getTaskById(task.getId());
		if(taskCheck.getState().toString().equals(ConstantInterface.STATIS_TASK_STATE_DONE)){
			map.put("status", "f1");
			map.put("info", "任务已办结不能委托!");
			return map;
		}
		
		task.setComId(userInfo.getComId());
		task.setOperator(userInfo.getId());
		taskService.updateTaskTurnBack(task,userInfo,sendWay);
		
		
		map.put("status", "y");
		map.put("info", "任务协同成功!");
		
		//查询任务的执行人员
		Integer executorId = 0;
		List<TaskExecutor> taskExecutors = task.getListTaskExecutor();
		if(null!=taskExecutors && !taskExecutors.isEmpty()){
			for (TaskExecutor taskExecutor : taskExecutors) {
				//任务的执行人
				Integer executor = taskExecutor.getExecutor();
				if(executor.equals(userInfo.getId())){
					executorId = userInfo.getId();
					break;
				}
			}
		}
		map.put("executor", executorId);
		return map;
	}
	
	/**
	 * 任务协同配置
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/handOverTask")
	public Map<String,Object> handOverTask(Task task,String idType,String redirectPage,String[] sendWay){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		UserInfo userInfo = this.getSessionUser();
		Task taskCheck = taskService.getTaskById(task.getId());
		if(taskCheck.getState().toString().equals(ConstantInterface.STATIS_TASK_STATE_DONE)){
			map.put("status", "f1");
			map.put("info", "任务已办结不能委托!");
			return map;
		}
		
		task.setComId(userInfo.getComId());
		task.setOperator(userInfo.getId());
		
		taskService.updateHandOverTask(task,userInfo,sendWay);
		map.put("status", "y");
		map.put("info", "任务协同成功!");
		
		//查询任务的执行人员
		Integer executorId = 0;
		List<TaskExecutor> taskExecutors = task.getListTaskExecutor();
		if(null!=taskExecutors && !taskExecutors.isEmpty()){
			for (TaskExecutor taskExecutor : taskExecutors) {
				//任务的执行人
				Integer executor = taskExecutor.getExecutor();
				if(executor.equals(userInfo.getId())){
					executorId = userInfo.getId();
					break;
				}
			}
		}
		map.put("executor", executorId);
		return map;
	}
	
	/**
	 * 获取你权限范围下的任务列表
	 * @param task
	 * @return
	 */
	@RequestMapping(value="/listTaskForRelevance")
	public ModelAndView listTaskForRelevance(Task task){
		ModelAndView view = new ModelAndView("/task/listTaskForRelevance");
		UserInfo userInfo = this.getSessionUser();
		List<Task> listTask = taskService.listPageTask(task,userInfo);
		//初始化负责人名称
		if(null!=task.getOwner() && task.getOwner()!=0){
			task.setOwnerName(userInfoService.getUserInfo(userInfo.getComId(),task.getOwner()).getUserName());
		}
		view.addObject("task",task);
		view.addObject("listTask",listTask);
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 验证与验证任务名称相似的任务
	 * @param taskName 需要匹配的任务名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/checkSimiTaskByTaskName")
	public int checkSimiTaskByTaskName(String taskName){
		if(null==taskName || "".equals(taskName.trim())){return 0;}
		taskService.listSimiTasksByTaskName(taskName,this.getSessionUser().getComId());
		return PaginationContext.getTotalCount();
	}
	/**
	 * 跳转显示相似任务列表页面
	 * @param taskName 需要匹配的任务名称
	 * @return
	 */
	@RequestMapping("/similarTasksPage")
	public ModelAndView similarTasksPage(String taskName){
		ModelAndView view = new ModelAndView("/task/listSimilarTask");
		UserInfo userInfo = this.getSessionUser();
		if(null!=taskName && !"".equals(taskName.trim())){
			List<Task> listTask = taskService.listSimiTasksByTaskName(taskName,userInfo.getComId());
			view.addObject("listTask",listTask);
		}
		return view;
	}
	
	/**
	 *  合并任务信息前验证或是提示跳转页面
	 * @param redirectPage 跳转页面
	 * @param ids 参与合并的任务
	 * @param sourcePage 来源页面
	 * @return
	 */
	@RequestMapping(value="taskCompressPage")
	public ModelAndView taskCompressPage(String redirectPage,Integer[] ids,String sourcePage){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/task/taskCompress");
		Integer index = 0;
		//存放需要合并的任务主键
		Set<Integer> taskIds = new HashSet<Integer>();
		if(null!=ids){
			for (Integer taskId : ids) {
				taskIds.add(taskId);
			}
			//遍历待合并的任务主键
			for (Integer taskId : ids) {
				//查询任务详情
				Task task = taskService.getTaskById(taskId, userInfo);
				//查询任务当前设置父节点的父节点
				List<Task> parentTasks = taskService.getAllParentTask(taskId,userInfo.getComId());
				
				//默认可以合并保留
				Integer flag = 1;
				//该任务有父节点
				if(null!=parentTasks && parentTasks.size()>0){
					for (Task parentTask : parentTasks) {
						if(taskIds.contains(parentTask.getId())){//该任务是子任务,只能合并到其他任务中
							flag = 0;
							break;
						}
					}
				}
				view.addObject("taskComPress"+index, flag);
				view.addObject("task"+index, task);
				index++;
			}
		}
		view.addObject("redirectPage", redirectPage);
		view.addObject("userInfo", userInfo);
		view.addObject("sourcePage", sourcePage);
		view.addObject("index", index);
		view.addObject("ids", ids);
		return view;
	}
	/**
	 * 合并任务信息前验证或是提示
	 * @param redirectPage 跳转页面
	 * @param task 合并后任务信息
	 * @param ids 参与合并的任务主键
	 * @param taskIdAndBusId 参与合并的任务主键与项目主键对
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/taskCompress")
	public ModelAndView taskCompress(String redirectPage,Task task,Integer[] ids,String sourcePage,String taskIdAndBusId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		
		taskService.updateTaskForCompress(task,ids,userInfo,taskIdAndBusId);
		this.setNotification(Notification.SUCCESS, "合并成功");
//		String sid = this.getSid();
		ModelAndView view = new ModelAndView("/refreshParent");
//		if(null!=sourcePage && "charge".equals(sourcePage)){
//			view.addObject("redirectPage","/task/viewChargeTask?sid="+sid+"&id="+task.getId()+"&redirectPage="+redirectPage);
//		}else{
//			view.addObject("redirectPage","/task/viewTask?sid="+sid+"&id="+task.getId()+"&redirectPage="+redirectPage);
//		}
		return view;
	}
	
	/**
	 * 异步获取任务详情
	 * @param taskId 任务主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/taskDetailByAjax")
	public Map<String,Object> taskDetailByAjax(Integer taskId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(!CommonUtil.isNull(taskId)){
			Task task = taskService.getTask(taskId,userInfo);
			map.put("data",task);
			map.put("status", "y");
		}else{
			map.put("status", "f");
			map.put("info","数据异常，请联系系统管理员。");
		}
		return map;
	}
	/**
	 * 跳转任务报延页面
	 * @param taskId 任务主键
	 * @return
	 */
	@RequestMapping(value="/delayApplyPage")
	public ModelAndView delayApplyPage(Integer taskId){
		ModelAndView view = new ModelAndView("/task/apply/delayApply");
		UserInfo userInfo = this.getSessionUser();
		Task task = taskService.getTaskById(taskId, userInfo);
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			view.setViewName("/task/apply/promote/delayApply");
		}
		view.addObject("task",task);
		return view;
	}
	
	/**
	 * 任务办理时限报延申请
	 * @param delayApply
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delayApply",method=RequestMethod.POST)
	public Map<String,Object> delayApply(DelayApply delayApply){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo || CommonUtil.isNull(delayApply.getTaskId())){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		taskService.initDelayApply(delayApply,userInfo);
		map.put("status", "y");
		this.setNotification(Notification.SUCCESS, "申请成功!");
		return map;
	}
	
	/**
	 * 报延申请详情
	 * @param applyId
	 * @return
	 */
	@RequestMapping(value="/delayApplyDetail")
	public ModelAndView delayApplyDetail(Integer applyId){
		ModelAndView view = new ModelAndView("/task/apply/delayApplyDetail");
		UserInfo userInfo = this.getSessionUser();
		DelayApply delayApply = taskService.queryDelayApply(applyId,userInfo);
		
		String taskVersion = delayApply.getTaskVersion();
		if(StringUtils.isNotEmpty(taskVersion) && taskVersion.equals("2")){
			view.setViewName("/task/apply/promote/delayApplyDetail");
		}
		todayWorksService.updateTodayWorksReadStateTo1(ConstantInterface.TYPE_DELAYAPPLY, applyId, userInfo.getId());
		view.addObject("delayApply",delayApply);
		view.addObject("userInfo",userInfo);
		return view;
	}

	/**
	 * 报延审批
	 * @param delayApply
	 * @return
	 */
	@RequestMapping(value="/updateDelayApply")
	public ModelAndView updateDelayApply(DelayApply delayApply){
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		taskService.updateDelayApply(delayApply,userInfo);
		this.setNotification(Notification.SUCCESS, "审核完成!");
		return view;
	}
	
	/**
	 * 发起审批关联任务业务
	 * @return
	 */
	@RequestMapping("/addTaskOfMod")
	public ModelAndView addTaskOfSpFlow(Task task,String checkids){
		UserInfo userInfo = this.getSessionUser();
		String version = task.getVersion();
		ModelAndView view = new ModelAndView("/task"+(StringUtils.isNotEmpty(version) && version.equals("2")?"/promote":"")+"/addTaskBySimple");//任务安排
		view.addObject("userInfo",userInfo);
		//模块名称
		String busName = task.getBusName();
		//任务描述
		String taskRemark = task.getTaskRemark();
		//业务类型
		String busType = task.getBusType();
		if(ConstantInterface.TYPE_FLOW_SP.equals(busType) //审批
				&& StringUtils.isNotEmpty(busName) ){//模块名称不为空
			if(StringUtils.isEmpty(taskRemark)){
				task.setTaskRemark("关于“"+busName+"”的落实");
			}
			String taskName = task.getTaskName();
			if(StringUtils.isEmpty(taskName)){
				task.setTaskName("关于“"+busName+"”的落实");
			}
		}
		view.addObject("task",task);
		//消息推送方式
		List<DataDic> listClockWay=DataDicContext.getInstance().listTreeDataDicByType("clockWay");
		view.addObject("listClockWay", listClockWay);
		
		List<SpFlowUpfile> listSpFiles = null;
		if(ConstantInterface.TYPE_FLOW_SP.equals(busType)){
			listSpFiles = workFlowService.listSpFiles(task.getBusId(),userInfo);
			//判断是否选中文件
			if(listSpFiles != null && listSpFiles.size() > 0 && checkids != null && checkids.length() > 0) {
				String[] ids = checkids.split(",");
				for (int i = listSpFiles.size() -1; i >= 0 ; i--) {
					int count = 0;
					if(ids != null && ids.length > 0) {
						for (int j = 0; j < ids.length; j++) {
							if(ids[j].equals(listSpFiles.get(i).getUpfileId() + "")) {
								count ++;
							}
						}
					}
					if(count < 1) {
						listSpFiles.remove(listSpFiles.get(i));
					}
				}
			}else if(checkids == null || checkids.length() < 1){
				listSpFiles = null;
			}
		}
		
		
		view.addObject("taskUpfileList",listSpFiles);
		view.addObject("checkids",checkids);
		return view;
	}
	
	/**
	 * 客户关联的任务
	 * 
	 * @param task
	 *            已含有业务类型和业务主键
	 * @return
	 */
	@RequestMapping(value = "/busModTaskList")
	public ModelAndView crmTaskListPage(Task task, String redirectPage) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/task/busModTaskList");
		// 列出客户关联的任务
		List<Task> taskList = taskService.listPagedBusTask(task, userInfo);

		view.addObject("taskList", taskList);
		view.addObject("userInfo", userInfo);
		return view;
	}
}
