package com.westar.core.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.westar.base.cons.CommonConstant;
import com.westar.base.enums.ActLinkTypeEnum;
import com.westar.base.enums.ModSpModTypeEnum;
import com.westar.base.enums.ModSpStateEnum;
import com.westar.base.enums.WrokFlowOptEnum;
import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.SpFlowCurExecutor;
import com.westar.base.model.SpFlowHiStep;
import com.westar.base.model.SpFlowHuiQianInfo;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.SpFlowModel;
import com.westar.base.model.SpFlowTalk;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.StagedItem;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.pojo.FormData;
import com.westar.base.pojo.ModSpConf;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.FlowDesignService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.ItemService;
import com.westar.core.service.ModFlowService;
import com.westar.core.service.ModOptConfService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.service.WorkFlowService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

/**
 * 工作流
 * @author lj
 *
 */
@Controller
@RequestMapping("/workFlow")
public class WorkFlowController extends BaseController {
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	FlowDesignService flowDesignService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	ModOptConfService modOptConfService;
	
	@Autowired
	ModFlowService modFlowService;
	
	/***
	 * 进入审批中心
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sp_center")
	public ModelAndView spCenter(HttpServletRequest request,String activityMenu) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/sp/sp_center");
		view.addObject("activityMenu",activityMenu);
		UserInfo userInfo = this.getSessionUser();
		List<SpFlowInstance> listSp = workFlowService.listSpTodDo(userInfo,new SpFlowInstance());
		view.addObject("listSp", listSp);
		view.addObject("userInfo", this.getSessionUser());
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_FLOW_SP);
		return view;
	}
	
	/**
	 * 获取自己的待办审批工作
	 * @return
	 */
	@RequestMapping("/listSpToDo")
	public ModelAndView listSpToDo(HttpServletRequest request,String activityMenu,SpFlowInstance instance){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
				
		ModelAndView view = new ModelAndView("/sp/sp_center");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		List<SpFlowInstance> listSp = workFlowService.listSpTodDo(userInfo,instance);
		view.addObject("listSp", listSp);
		view.addObject("activityMenu",activityMenu);
		view.addObject("instance",instance);
		//取得常用人员列表
		List<UserInfo> listHourlyUser = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		view.addObject("listHourlyUser",listHourlyUser);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_FLOW_SP);
		return view;
	}
	
	/**
	 * 获取个人的流程实例
	 * @param activityMenu
	 * @param instance
	 * @return
	 */
	@RequestMapping("/listSpFlowOfMine")
	public ModelAndView listSpFlowOfMine(HttpServletRequest request,String activityMenu,SpFlowInstance instance){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		ModelAndView view = new ModelAndView("/sp/sp_center");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		List<SpFlowInstance> listSp = workFlowService.listSpFlowOfMine(userInfo,instance);
		view.addObject("listSp", listSp);
		view.addObject("activityMenu",activityMenu);
		view.addObject("instance",instance);
		//取得常用人员列表
		List<UserInfo> listHourlyUser = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		view.addObject("listHourlyUser",listHourlyUser);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_FLOW_SP);
		return view;
	}
	
	/**
	 * 查询个人关注的审批流程
	 * @param request
	 * @param activityMenu
	 * @param instance
	 * @return
	 */
	@RequestMapping("/listSpFlowOfAtten")
	public ModelAndView listSpFlowOfAtten(HttpServletRequest request,String activityMenu,SpFlowInstance instance){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
				
		ModelAndView view = new ModelAndView("/sp/sp_center");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		instance.setAttentionState(ConstantInterface.ATTENTION_STATE_YES);
		//isForceInPersion 当前操作人是否是模块监督人员
		List<SpFlowInstance> listSp = workFlowService.listSpFlowOfAll(userInfo,instance);
		view.addObject("listSp", listSp);
		view.addObject("activityMenu",activityMenu);
		view.addObject("instance",instance);
		//取得常用人员列表
		List<UserInfo> listHourlyUser = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		view.addObject("listHourlyUser",listHourlyUser);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_FLOW_SP);
		return view;
	}
	/**
	 * 获取个人权限下所有的流程
	 * @param activityMenu
	 * @param instance
	 * @return
	 */
	@RequestMapping("/listSpFlowOfAll")
	public ModelAndView listSpFlowOfAll(HttpServletRequest request,String activityMenu,SpFlowInstance instance){
		
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
				
		ModelAndView view = new ModelAndView("/sp/sp_center");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		//isForceInPersion 当前操作人是否是模块监督人员
		
		List<SpFlowInstance> listSp = workFlowService.listSpFlowOfAll(userInfo,instance);
		view.addObject("listSp", listSp);
		view.addObject("activityMenu",activityMenu);
		view.addObject("instance",instance);
		//取得常用人员列表
		List<UserInfo> listHourlyUser = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		view.addObject("listHourlyUser",listHourlyUser);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_FLOW_SP);
		return view;
	}
	
	/***************************流程实例化******************************/
	/**
	 * 发起流程
	 * @param flowId 流程主键
	 * @return
	 */
	@RequestMapping(value="/startSpFlow")
	public ModelAndView startSpFlow(Integer flowId){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(flowId,userInfo);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(0);
		spFlowInstance.setBusType("0");
		
		view.addObject("spFlowInstance",spFlowInstance);
		
		view.addObject("nowTimeLong",System.currentTimeMillis());
		return view;
	}
	
	/**
	 * 发起用户自定义审批
	 * @param formId 表单主键
	 * @return
	 */
	@RequestMapping(value="/startSpFlowByUserDefined")
	public ModelAndView startSpFlowByUserDefined(Integer formId){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = workFlowService.initSpFlowByUserDefined(formId,userInfo);
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(0);
		spFlowInstance.setBusType("0");
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	
	/**
	 * 查看流程
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	@RequestMapping(value="/viewSpFlow")
	public ModelAndView viewSpFlow(HttpServletRequest request,Integer instanceId){
		ModelAndView view = new ModelAndView();
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = workFlowService.getSpFlowInstanceById(instanceId,userInfo);
		
		//是草稿且是自己的
		boolean spflowAddFlag = null!=spFlowInstance && 
				(spFlowInstance.getFlowState().equals(2) || spFlowInstance.getFlowState().equals(0) )
				&& spFlowInstance.getCreator().toString().equals(userInfo.getId().toString());
		
		if(spflowAddFlag){
			//审批关联附件
			List<SpFlowUpfile> spFlowUpfiles = workFlowService.listSpFiles(spFlowInstance.getId(), userInfo);
			if(null!=spFlowUpfiles && !spFlowUpfiles.isEmpty()){
				spFlowInstance.setSpFlowUpfiles(spFlowUpfiles);
			}
			Integer formLayoutState = spFlowInstance.getFormLayoutState();
			if(null != formLayoutState  && formLayoutState==1){
				view.setViewName("/sp/form/addFormDataDev");
			}else{
				view.setViewName("/sp/form/addFormData");
			}
		}else{
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_FLOW_SP, instanceId);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				viewRecordService.addViewRecord(userInfo,viewRecord);
			}
			
			//只能使用会签的内容
			ModSpConf modSpConf = modFlowService.queryModSpConf(userInfo,spFlowInstance.getId(),ConstantInterface.TYPE_FLOW_SP,spFlowInstance.getActInstaceId());
			if(null != modSpConf){
				view.addObject("huiqianContent",modSpConf.getContent());
			}
			
			Integer formLayoutState = spFlowInstance.getFormLayoutState();
			if(null != formLayoutState  && formLayoutState==1){
				view.setViewName("/sp/form/viewFormDataDev");

			}else{
				view.setViewName("/sp/form/viewFormData");
			}
			if(spFlowInstance.getFlowState().equals(ModSpStateEnum.FIHISH.getValue())){
				todayWorksService.updateTodoWorkRead(instanceId,userInfo.getComId(), 
						userInfo.getId(), ConstantInterface.TYPE_SP_END,0);
			}else{
				todayWorksService.updateTodoWorkRead(instanceId,userInfo.getComId(), 
						userInfo.getId(), ConstantInterface.TYPE_FLOW_SP,0);
			}
			
		}
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	
	
	/**
	 * 发起流程 (一般审批流程)
	 * @param actionId 流程主键
	 * @return
	 */
	@RequestMapping(value="/startMobSpFlow")
	public ModelAndView startMobSpFlow(Integer actionId,String spModType){
		ModelAndView view = new ModelAndView("/sp/form/addModFormDataDev");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = null;
		if(spModType.equals(ModSpModTypeEnum.FREE.getValue())){
			
			spFlowInstance = workFlowService.initSpFlowByUserDefined(actionId,userInfo);
			if(null == spFlowInstance){
				view.setViewName("/refreshParent");
				this.setNotification(Notification.ERROR, "未设置表单布局！");
				return view;
			}
			//初始化审批关联
			spFlowInstance.setStagedItemId(0);
			spFlowInstance.setBusId(0);
			spFlowInstance.setBusType("0");
		}else if(spModType.equals(ModSpModTypeEnum.MOD.getValue())){
			
			spFlowInstance = workFlowService.initSpFlow(actionId,userInfo);
			//初始化审批关联
			spFlowInstance.setStagedItemId(0);
			spFlowInstance.setBusId(0);
		}else if(spModType.equals(ModSpModTypeEnum.DRAFT.getValue())){
			spFlowInstance = workFlowService.getSpFlowInstanceById(actionId,userInfo);
			//审批关联附件
			List<SpFlowUpfile> spFlowUpfiles = workFlowService.listSpFiles(spFlowInstance.getId(), userInfo);
			if(null!=spFlowUpfiles && !spFlowUpfiles.isEmpty()){
				spFlowInstance.setSpFlowUpfiles(spFlowUpfiles);
			}
		}
		view.addObject("spFlowInstance",spFlowInstance);
		Gson gson = new Gson();
		view.addObject("spFlowInsStr",gson.toJson(spFlowInstance));
		
		return view;
	}
	/**
	 * 查看流程
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	@RequestMapping(value="/viewMobSpFlow")
	public ModelAndView viewMobSpFlow(HttpServletRequest request,Integer instanceId){
		ModelAndView view = new ModelAndView();
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = workFlowService.getSpFlowInstanceById(instanceId,userInfo);

		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_FLOW_SP, instanceId);
		//取得是否添加浏览记录
		boolean bool = FreshManager.checkOpt(request, viewRecord);
		if(bool){
			//添加查看记录
			viewRecordService.addViewRecord(userInfo,viewRecord);
		}
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState == 1){
			view.setViewName("/sp/form/viewMobFormDataDev");
		}else{
			view.setViewName("/sp/form/viewMobFormData");
		}
		if(spFlowInstance.getFlowState().equals(ConstantInterface.SP_STATE_FINISH)){
			todayWorksService.updateTodoWorkRead(instanceId,userInfo.getComId(), 
					userInfo.getId(), ConstantInterface.TYPE_SP_END,0);
		}else{
			todayWorksService.updateTodoWorkRead(instanceId,userInfo.getComId(), 
					userInfo.getId(), ConstantInterface.TYPE_FLOW_SP,0);
		}
		view.addObject("spFlowInstance",spFlowInstance);
		
		return view;
	}
	
	/**
	 * 查询流程关联附件
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	@RequestMapping(value="/listSpFiles")
	public ModelAndView listSpFiles(Integer instanceId){
		ModelAndView view = new ModelAndView("/sp/workFlow/listSpFiles");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		//查询流程关联附件
		List<SpFlowUpfile> listSpFiles = workFlowService.listPagedSpFiles(instanceId,userInfo);
		view.addObject("listSpFiles",listSpFiles);
		return view;
	}
	/**
	 * 查询流程关联附件
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	@RequestMapping(value="/listSpHistory")
	public ModelAndView listSpHistory(Integer instanceId){
		ModelAndView view = new ModelAndView("/sp/workFlow/listSpHistory");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		
		SpFlowInstance instance= workFlowService.getSpFlowInstanceById(instanceId, userInfo);
		view.addObject("instance",instance);
		
		//流程审批历史信息
		List<SpFlowHiStep> listSpFlowHiStep = workFlowService.listSpFlowHiStep(instanceId,userInfo);
		
		if(null!= listSpFlowHiStep && !listSpFlowHiStep.isEmpty() 
				&& instance.getFlowState()!=ModSpStateEnum.FIHISH.getValue()){//未完结,倒叙
			Collections.reverse(listSpFlowHiStep);
		}
		view.addObject("listSpFlowHiStep",listSpFlowHiStep);
		
		return view;
	}
	/**
	 *异步取得打印流程信息
	 * @param instanceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxListSpHistory")
	public Map<String,Object> ajaxListSpHistory(Integer instanceId){
		Map<String,Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		SpFlowInstance instance= workFlowService.getSpFlowInstanceById(instanceId, userInfo);
		
		//流程审批历史信息
		List<SpFlowHiStep> listSpFlowHiStep = workFlowService.listSpFlowHiStep(instanceId,userInfo);
		
		if(null!= listSpFlowHiStep && !listSpFlowHiStep.isEmpty() 
				&& instance.getFlowState() != ModSpStateEnum.FIHISH.getValue()){//未完结,倒叙
			Collections.reverse(listSpFlowHiStep);
		}
		map.put("status", "y");
		map.put("listSpFlowHiStep", listSpFlowHiStep);
		
		return map;
	}
	
	/**
	 * 保存保单数据
	 * @param formDataStr 数据对象字符串
	 * @param saveType 保存类型 "add" 与"update"
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/addFormData")
	public Map<String,Object> addFormData(String formDataStr,String saveType) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		//发起审批流程
		if(WrokFlowOptEnum.ADD.getValue().equalsIgnoreCase(saveType.trim())){
			workFlowService.addFormData(formDataStr,userInfo);
		}
		//流程审批
		else if(WrokFlowOptEnum.UPDATE.getValue().equalsIgnoreCase(saveType.trim())){
			
			Map<String,Object> resultMap = workFlowService.updateSpFlow(formDataStr,userInfo);
			if(null!=resultMap && resultMap.get(ConstantInterface.TYPE_STATUS).equals(ConstantInterface.TYPE_STATUS_F)){
				return resultMap;
			}
		}else if(WrokFlowOptEnum.BACK.getValue().equalsIgnoreCase(saveType.trim())){
			
			//流程回退
			Map<String,Object> resultMap = workFlowService.spFlowTurnBackTo(formDataStr,userInfo);
			if(null!=resultMap && resultMap.get(ConstantInterface.TYPE_STATUS).equals(ConstantInterface.TYPE_STATUS_F)){
				return resultMap;
			}
		}
		this.setNotification(Notification.SUCCESS, "操作成功!");
		map.put("status", "y");
		return map;
	}
	/**
	 * 修改审批关联
	 * @param optType 操作类型
	 * @param instanceId 流程实例化主键
	 * @param busType 关联业务类型
	 * @param busId 审理关联主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateSpRelate")
	public Map<String,Object> updateSpRelate(Integer optType,FormData formData,String isStage){
		Map<String,Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//当前关联业务主键
		Integer busId = formData.getBusId();
		//是修改数据的
		if(optType.equals(1)){
			//关联的是项目
			boolean relateItemFlag = (null==isStage || "false".equals(isStage)) 
					&&  formData.getBusType().equals(ConstantInterface.TYPE_ITEM);
			if(relateItemFlag){
				StagedItem stagedItem = itemService.queryTheLatestStagedItem(
						userInfo.getComId(),busId );
				map.put("stagedItem", stagedItem);
			}
			Integer stagedItemId = workFlowService.updateSpFlowRelate(userInfo, formData,isStage);
			map.put("stagedItemId", stagedItemId);
		}else{//删除审批关联
			workFlowService.delSpFlowInstRelate(formData.getInstanceId(),userInfo);
		}
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 删除审批流程
	 * @param ids 删除所需的流程主键数组
	 * @param redirectPage 重定向地址
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delSpFlow")
	public ModelAndView delSpFlow(Integer[] ids,String redirectPage) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		if(null!=ids && ids.length>0){
			//删除任务
			workFlowService.delSpFlow(ids, userInfo);
		}
		ModelAndView mav = new ModelAndView();
		this.setNotification(Notification.SUCCESS, "删除成功!");
		mav.setViewName("redirect:"+redirectPage);
		return mav;
	}

	
	/****************************表单实例化*****************************/
	
	
	/**
	 * 分页查询流程表单
	 * @param spFlowInstance
	 * @return
	 */
	@RequestMapping(value="/listPagedWorkFlow")
	public ModelAndView listPagedWorkFlow(SpFlowInstance spFlowInstance){
		ModelAndView view = new ModelAndView("/sp/form/formCenter");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		spFlowInstance.setFlowState(1);
		List<SpFlowInstance> listSpFlows = workFlowService.listPagedWorkFlow(userInfo,spFlowInstance);
		view.addObject("listSpFlows",listSpFlows);
		return view;
	}
	
	/**
	 * 渲染表单数据
	 * @param dataId 流程实例主键
	 * @param layoutId 表单布局主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findFormData")
	public Map<String,Object> findFormData(Integer dataId,Integer layoutId){
		Map<String,Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		map.putAll(workFlowService.findFormData(null,dataId,layoutId,userInfo));
		return map;
	}
	/**
	 * 渲染表单数据
	 * @param instanceId 流程实例主键
	 * @param layoutId 表单布局主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findFormDataDev")
	public Map<String,Object> findFormDataDev(Integer instanceId,Integer layoutId){
		Map<String,Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		map.putAll(workFlowService.findFormDataDev(null,instanceId,layoutId,userInfo));
		return map;
	}
	
	/***************************表单实例化******************************/
	
	/**
	 * 获取已执行的审批步骤集合
	 * @param instanceId 流程实例主键
	 * @return
	 */
	@RequestMapping("/listHistorySpStep")
	public ModelAndView listHistorySpStep(Integer instanceId){
		ModelAndView view = new ModelAndView("/sp/workFlow/listHistorySpStep");
		UserInfo userInfo = this.getSessionUser();
		List<SpFlowHiStep> listHistorySpStep = workFlowService.listHistorySpStep(userInfo,instanceId);
		view.addObject("listHistorySpStep", listHistorySpStep);
		return view;
	}
	/**
	 * 获取已执行的审批步骤集合
	 * @param instanceId 流程实例主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListHistorySpStep")
	public Map<String,Object> ajaxListHistorySpStep(Integer instanceId){
		Map<String,Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		List<SpFlowHiStep> listHistorySpStep = workFlowService.listHistorySpStep(userInfo,instanceId);
		map.put("listHistorySpStep", listHistorySpStep);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 审批查看权限验证
	 * @param instanceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/authorCheck")
	public SpFlowInstance authorCheck(Integer instanceId) {
		UserInfo userInfo = this.getSessionUser();
		SpFlowInstance spFlowInstance = new SpFlowInstance();
		if (null == userInfo) {
			spFlowInstance.setSucc(false);
			spFlowInstance.setPromptMsg("服务已关闭，请稍后重新操作！");
			return spFlowInstance;
		}
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_FLOW_SP);
		//是监督人员
		if (isForceIn) {
			spFlowInstance.setSucc(true);
		}
		//有查看权限
		else if (workFlowService.authorCheck(userInfo.getComId(),instanceId,
				userInfo.getId())) {
			spFlowInstance.setSucc(true);
		} 
		//没有查看权限
		else {
			// 查看验证，删除客户提醒
			todayWorksService.delTodoWork(instanceId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_FLOW_SP, null);

			spFlowInstance.setSucc(false);
			spFlowInstance.setPromptMsg("抱歉，你没有查看权限");
		}
		return spFlowInstance;
	}
	
	/**
	 * 如果流程步骤有直属上级审批配置，则验证当前操作人是否设置了直属上级
	 * @param flowId 发起流程主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/haveSetDirectLeader")
	public SpFlowModel haveSetDirectLeader(Integer flowId) {
		UserInfo userInfo = this.getSessionUser();
		SpFlowModel spFlowModel = new SpFlowModel();
		if (null == userInfo) {
			spFlowModel.setSucc(false);
			spFlowModel.setPromptMsg("服务已关闭，请稍后重新操作！");
			return spFlowModel;
		}
		if(workFlowService.toSetDirectLeader(userInfo,flowId)){
			//需要设置直属上级
			spFlowModel.setSucc(true);
		}else{
			//不需要设置直属上级
			spFlowModel.setSucc(false);
		}
		return spFlowModel;
	}
	/**
	 * 验证步骤配置信息
	 * @param actInstaceId activity实例化主键
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkStepCfg")
	public Map<String,Object> checkStepCfg(String actInstaceId,Integer instanceId) {
		
		Map<String,Object> map = new HashMap<String, Object>(2);
		
		UserInfo userInfo = this.getSessionUser();
		
		List<String> result = new ArrayList<String>();
		
		if (null == userInfo) {
			result.add("f");
			map.put("status",result);
			
			map.put("info",CommonConstant.OFF_LINE_INFO);
			return map;
		}
		SpFlowHiStep spFlowHiStep = flowDesignService.checkStepCfg(userInfo,actInstaceId,instanceId);
		if(null == spFlowHiStep){
			result.add("f");
			map.put("status", result);
			map.put("info", "步骤信息配置错误");
		}else{
			//需要配置
			if(!StringUtils.isEmpty(spFlowHiStep.getSpCheckCfg()) 
					&& spFlowHiStep.getSpCheckCfg().equals(ConstantInterface.SPSTEP_CHECK_YES)){
				//需要配置
				result.add("f1");
				String movePhone = userInfo.getMovePhone();
				//没有设定手机号
				if(StringUtils.isEmpty(movePhone)){
					result.add("f2");
				}else{
					map.put("movePhone", movePhone);
				}
				map.put("status", result);
			}else{
				//不需要配置
				result.add("y");
				map.put("status", result);
			}
		}
		return map;
	}
	
	/**
	 * 获取已执行的审批步骤集合
	 * @param instanceId 流程实例主键
	 * @return
	 */
	@RequestMapping("/sendSpYzmPage")
	public ModelAndView sendSpYzmPage(){
		ModelAndView view = new ModelAndView("/sp/workFlow/sendSpYzm");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	
	/**
	 * 拾取审批任务
	 * @param instanceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pickSpInstance")
	public Map<String,Object> pickSpInstance(Integer instanceId,String actInstanceId){
		Map<String,Object> map = new HashMap<String,Object>(2);
		UserInfo userInfo = this.getSessionUser();
		workFlowService.updatePickSpInstance(userInfo,instanceId,actInstanceId);
		map.put("stauts", "y");
		return map;
	}
	
	/**
	 * 修改审批的办理人员
	 * @param instanceId 流程的办理人员
	 * @param actInstanceId
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/updateSpInsAssignV2")
	public Map<String,Object> updateSpInsAssignV2(Integer instanceId,String actInstanceId,
			Integer newAssignerId,String formDataStr) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>(2);
		UserInfo userInfo = this.getSessionUser();
		workFlowService.updateSpInsAssignV2(userInfo,instanceId,actInstanceId,newAssignerId,formDataStr);
		map.put("status", "y");
		return map;
	}
	/**
	 * 修改审批的办理人员
	 * @param instanceId 流程的办理人员
	 * @param actInstanceId
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/updateSpInsAssign")
	public Map<String,Object> updateSpInsAssign(Integer instanceId,String actInstanceId,
			Integer newAssignerId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>(2);
		UserInfo userInfo = this.getSessionUser();
		if(newAssignerId.equals(userInfo.getId())){
			map.put("status", "f");
			map.put("info", "不能转办给自己！");
			return map;
		}
		workFlowService.updateSpInsAssign(userInfo,instanceId,actInstanceId,newAssignerId);
		map.put("status", "y");
		this.setNotification(Notification.SUCCESS, "转办成功！");
		return map;
	}
	
	/**
	 * 跳转流程提交展示界面
	 * @return
	 */
	@RequestMapping(value="/spFlowIdeaPage")
	public ModelAndView spFlowIdeaPage() {
		ModelAndView view = new ModelAndView("/sp/workFlow/spFlowIdea");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 跳转流程提交展示界面
	 * @return
	 */
	@RequestMapping(value="/spFlowNextStepPage")
	public ModelAndView spFLowNextStepPage() {
		ModelAndView view = new ModelAndView("/sp/workFlow/spFlowNextStep");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	
	/**
	 * 异步取得流程数据信息
	 * @param instanceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxSpFlowNextStep",method = RequestMethod.POST)
	public Map<String,Object> ajaxSpFLowNextStep(Integer instanceId){
	
		Map<String,Object> map = new HashMap<String,Object>(2);
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//获取流程下一步步骤信息
		SpFlowHiStep stepInfo = workFlowService.querySpFlowNextStepInfo(instanceId, userInfo);
		map.put("stepInfo",stepInfo);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 获取项目列表FOR关联
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listMoreSpFlowForRelevance")
	public ModelAndView listMoreSpFlowForRelevance() {
		ModelAndView view = new ModelAndView("/sp/workFlow/listMoreSpFlowForRelevance");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	
	/**
	 * 异步取得项目分页数
	 * @param spFlowInstance
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListSpFlowForSelect")
	public Map<String, Object> ajaxListSpFlowForSelect(SpFlowInstance spFlowInstance, Integer pageNum, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		spFlowInstance.setFlowState(4);
		spFlowInstance.setSpState(1);
		PageBean<SpFlowInstance> pageBean = workFlowService.listPagedSpFlowForSelect(userInfo, spFlowInstance);
		map.put("pageBean", pageBean);
		
		map.put("status", "y");
		return map;
	}
	
	/**
	 * ajax查询审批附件
	 * @param spId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectSpFiles")
	public List<SpFlowUpfile> selectSpFiles(Integer spId) {
		UserInfo userInfo = this.getSessionUser();
		List<SpFlowUpfile> listSpFiles = workFlowService.listSpFiles(spId,userInfo);
		return listSpFiles;
	}
	
	/**
	 * 删除审批文档
	 * @param busId
	 * @param upfileId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delSpUpfile")
	public Map<String, Object> delSpUpfile(Integer busId,Integer upfileId,Integer addType) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		workFlowService.delTaskUpfile(upfileId,userInfo,busId,addType);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 审批会签配置
	 * @param instaceId 流程实例主键
	 * @return
	 */
	@RequestMapping("/spHuiQian")
	public ModelAndView jointProcessUserSetPage(String instaceId){
		ModelAndView view = new ModelAndView("/sp/workFlow/spHuiQian");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		return view;
	}
	
	/**
	 * 会签操作
	 * @param instanceId 流程部署主键
	 * @param modFormStepDataStr 会签参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSpHuiQian")
	public Map<String,Object> updateModJoinProcess(Integer instanceId,String modFormStepDataStr){
		Map<String,Object> map = new HashMap<String,Object>(2);
		UserInfo userInfo = this.getSessionUser();
		try {
			//审批会签
			workFlowService.initSpHuiQian(instanceId,modFormStepDataStr,userInfo);
		} catch (Exception e) {
			map.put("info","流程参数错误！");
			map.put("status","f");
		}
		return map;
	}
	
	/**
	 * 审批会签进度
	 * @param instaceId 流程实例主键
	 * @return
	 */
	@RequestMapping("/spHuiQianProcess")
	public ModelAndView spHuiQianProcess(Integer instanceId){
		ModelAndView view = new ModelAndView("/sp/workFlow/spHuiQianProcess");
		UserInfo userInfo = this.getSessionUser();
		List<SpFlowHuiQianInfo> listSpFlowHuiQianInfo = workFlowService.listSpHuiQianProcess(instanceId,userInfo);
		view.addObject("listSpFlowHuiQianInfo",listSpFlowHuiQianInfo);
		view.addObject("userInfo",userInfo);
		List<SpFlowCurExecutor> listSpFlowCurExecutor = workFlowService.getSpFlowCurExecutorByExecuteType(userInfo.getComId(),instanceId,ActLinkTypeEnum.ASSIGNEE.getValue());
		view.addObject("SpFlowCurExecutor",CommonUtil.isNull(listSpFlowCurExecutor)?null:listSpFlowCurExecutor.get(0));
		return view;
	}
	
	/**
	 * 撤销会签
	 * @param actTaskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/cancelHuiQian")
	public Map<String,Object> cancelHuiQian(String actTaskId,Integer huiQianId){
		Map<String,Object> map = new HashMap<String,Object>(2);
		try {
			//撤销会签
			workFlowService.delHuiQian(actTaskId,huiQianId);
			map.put("status", "y");
			map.put("info", "操作成功！");
		} catch (Exception e) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
		}
		return map;
	}
	
	/**
	 * 审批会签配置
	 * @param instanceId 流程实例主键
	 * @return
	 */
	@RequestMapping("/subSpHuiQianPage")
	public ModelAndView subSpHuiQianPage(Integer instanceId){
		ModelAndView view = new ModelAndView("/sp/workFlow/subSpHuiQian");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowHuiQianInfo spFlowHuiQianInfo = workFlowService.querySpFlowHuiQianInfo(instanceId,userInfo);
		view.addObject("spFlowHuiQianInfo",spFlowHuiQianInfo);
		return view;
	}
	/**
	 * 会签操作
	 * @param spFlowHuiQianInfo 会签参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/subSpHuiQian")
	public Map<String,Object> subSpHuiQian(SpFlowHuiQianInfo spFlowHuiQianInfo){
		Map<String,Object> map = new HashMap<String,Object>(2);
		UserInfo userInfo = this.getSessionUser();
		try {
			workFlowService.initEndHuiQian(spFlowHuiQianInfo,userInfo);
			this.setNotification(Notification.SUCCESS, "会签已反馈。");
		} catch (Exception e) {
			map.put("info","流程参数错误！");
			map.put("status","f");
		}
		return map;
	}
	/**
	 * 跳转审批留言页面
	 * @param spFlowTalk
	 * @return
	 */
	@RequestMapping(value="/spFlowTalkPage")
	public ModelAndView spFlowTalk(SpFlowTalk spFlowTalk){
		ModelAndView view = new ModelAndView("/sp/workFlow/spFlowTalk");
		UserInfo userInfo = this.getSessionUser();
		SpFlowInstance spFlowInstance = workFlowService.getSpFlowInstanceById(spFlowTalk.getBusId(),userInfo);
		view.addObject("spFlowInstance",spFlowInstance);
		//查看任务讨论，删除消息提醒
		todayWorksService.updateTodoWorkRead(spFlowTalk.getBusId(),userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_FLOW_SP,0);

		view.addObject("userInfo", userInfo);
		List<SpFlowTalk> listSpFlowTalk = workFlowService.listSpFlowTalk(spFlowTalk.getBusId(), userInfo.getComId());
		view.addObject("listSpFlowTalk",listSpFlowTalk);

		view.addObject("spFlowTalk",spFlowTalk);

		//获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_FLOW_SP);
		if(null!=listModuleOperateConfig){
			for(ModuleOperateConfig vo:listModuleOperateConfig){
				view.addObject(vo.getOperateType(),ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		return view;
	}
	/**
	 * ajax添加审批留言
	 * @param spFlowTalk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddSpFlowTalk")
	public SpFlowTalk ajaxAddSpFlowTalk(SpFlowTalk spFlowTalk) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		spFlowTalk.setComId(userInfo.getComId());
		spFlowTalk.setSpeaker(userInfo.getId());
		Integer id = workFlowService.addSpFlowTalk(spFlowTalk,userInfo);
		spFlowTalk = workFlowService.querySpFlowTalk(id, userInfo.getComId());
		//自己添加的，当前自己肯定是叶子
		spFlowTalk.setIsLeaf(1);
		String spFlowTalkDivString = workFlowService.replyTalkDivString(spFlowTalk,
				"listUpfiles_"+spFlowTalk.getId()+".upfileId","filename","otherTaskAttrIframe",userInfo,this.getSid());
		spFlowTalk.setSpFlowTalkDivString(spFlowTalkDivString);
		//模块日志添加
//		workFlowService.addTaskLog(userInfo.getComId(),spFlowTalk.getBusId()(), userInfo.getId(), userInfo.getUserName(), "添加留言");
		return spFlowTalk;
	}
	/**
	 * 回复留言
	 * @param spFlowTalk
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/replyTalk")
	public SpFlowTalk replyTalk(SpFlowTalk spFlowTalk) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			return null;
		}
		spFlowTalk.setComId(userInfo.getComId());
		spFlowTalk.setSpeaker(userInfo.getId());
		Integer id = workFlowService.addSpFlowTalk(spFlowTalk,userInfo);
		spFlowTalk = workFlowService.querySpFlowTalk(id, userInfo.getComId());
		//自己添加的，当前自己肯定是叶子
		spFlowTalk.setIsLeaf(1);
		String spFlowTalkDivString = workFlowService.replyTalkDivString(spFlowTalk,
				"listUpfiles_"+spFlowTalk.getId()+".upfileId","filename","otherTaskAttrIframe",userInfo,this.getSid());
		spFlowTalk.setSpFlowTalkDivString(spFlowTalkDivString);
		//模块日志添加
//		workFlowService.addTaskLog(userInfo.getComId(),spFlowTalk.getTaskId(), userInfo.getId(), userInfo.getUserName(), "添加留言");
		return spFlowTalk;
	}
	
	/**
	 * 删除审批留言
	 * @param spFlowTalk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/ajaxDelSpFlowTalk")
	public String ajaxDelSpFlowTalk(SpFlowTalk spFlowTalk) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		spFlowTalk.setComId(userInfo.getComId());
		workFlowService.delSpFlowTalk(spFlowTalk,userInfo);
		//模块日志添加
//		workFlowService.addTaskLog(userInfo.getComId(),spFlowTalk.getBusId(), userInfo.getId(), userInfo.getUserName(), "删除留言");
		return "删除成功！";
	}
	
	/**
	 * 分页查询模块关联的审批数据信息
	 * @param flowInstance
	 * @return
	 */
	@RequestMapping(value = "/busModSpflowList")
	public ModelAndView busModSpflowList(SpFlowInstance flowInstance) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/sp/workFlow/listPagedBusModSpflow");
		//分页查询模块关联的审批数据信息
		PageBean<SpFlowInstance> pageBean = workFlowService.listPagedBusSpflow(userInfo,flowInstance);
		view.addObject("pageBean", pageBean);
		view.addObject("userInfo", userInfo);
		return view;
	}
}
