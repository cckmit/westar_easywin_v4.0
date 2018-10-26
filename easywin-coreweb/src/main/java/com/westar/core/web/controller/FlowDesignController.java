package com.westar.core.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.FormCompon;
import com.westar.base.model.SpFlowModel;
import com.westar.base.model.SpFlowRelevanceCfg;
import com.westar.base.model.SpFlowStep;
import com.westar.base.model.SpFlowType;
import com.westar.base.model.SpStepConditions;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.FlowDesignService;
import com.westar.core.service.UserInfoService;
import com.westar.core.web.PaginationContext;

/**
 * 流程步骤配置
 * @author lj
 *
 */
@Controller
@RequestMapping("/flowDesign")
public class FlowDesignController extends BaseController {
	
	@Autowired
	FlowDesignService flowDesignService;
	
	@Autowired
	UserInfoService userInfoService;
	
	/**
	 * 跳转新增流程页面
	 * @param activityMenu 当前活动菜单
	 * @return
	 */
	@RequestMapping("/addFlowPage")
	public ModelAndView addFlowPage(String activityMenu){
		ModelAndView view = new ModelAndView("/sp/flowDesign/addFlow");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		view.addObject("activityMenu",activityMenu);
		List<SpFlowType> listSpFlowType = flowDesignService.listSpFlowType(this.getSessionUser());
		view.addObject("listSpFlowType",listSpFlowType);
		return view;
	}
	
	/**
	 * 新增流程配置
	 * @param flowConfig 流程配置信息
	 * @return
	 */
	@RequestMapping("/addFlow")
	public ModelAndView addFlow(SpFlowModel flowConfig){
		ModelAndView view = new ModelAndView("/refreshParent");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		UserInfo userInfo = this.getSessionUser();
		flowDesignService.addFlow(userInfo,flowConfig);
		view.addObject("userInfo",userInfo);
		return view;
	}
	
	/**
	 * 跳转流程配置详情页面
	 * @param flowId
	 * @return
	 */
	@RequestMapping("/editFlowPage")
	public ModelAndView editFlowPage(Integer flowId,String activityMenu){
		ModelAndView view = new ModelAndView("/sp/sp_center");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		if(!userInfoService.isAdmin(this.getSessionUser())){
			view = new ModelAndView("redirect:/flowDesign/listFlowModel?sid="+this.getSid()+"&activityMenu=sp_m_2.2");
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		view.addObject("activityMenu",activityMenu);
		SpFlowModel flowConfig = flowDesignService.querySpFlowModel(userInfo, flowId);
		view.addObject("flowConfig",flowConfig);
		return view;
	}
	
	/**
	 * 跳转流程配置详情页面
	 * @param flowId
	 * @return
	 */
	@RequestMapping("/editFlowBaseInfo")
	public ModelAndView editFlowBaseInfo(Integer flowId,String activityMenu){
		ModelAndView view = new ModelAndView("/sp/flowDesign/editFlowBaseInfo");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		if(!userInfoService.isAdmin(this.getSessionUser())){
			view = new ModelAndView("redirect:/flowDesign/listFlowModel?sid="+this.getSid()+"&activityMenu=sp_m_2.2");
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		view.addObject("activityMenu",activityMenu);
		SpFlowModel flowConfig = flowDesignService.querySpFlowModel(userInfo, flowId);
		view.addObject("flowConfig",flowConfig);
		List<SpFlowType> listSpFlowType = flowDesignService.listSpFlowType(this.getSessionUser());
		view.addObject("listSpFlowType",listSpFlowType);
		return view;
	}
	
	/**
	 * 跳转流程步骤更新页面
	 * @param flowId
	 * @param activityMenu
	 * @return
	 */
	@RequestMapping("/editFlowStepConfig")
	public ModelAndView editFlowStepConfig(Integer flowId,String activityMenu){
		ModelAndView view = new ModelAndView("/sp/flowDesign/editFlowStepConfig");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		if(!userInfoService.isAdmin(this.getSessionUser())){
			view = new ModelAndView("redirect:/flowDesign/listFlowModel?sid="+this.getSid()+"&activityMenu=sp_m_2.2");
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		view.addObject("activityMenu",activityMenu);
		SpFlowModel flowConfig = flowDesignService.querySpFlowModel(userInfo, flowId);
		view.addObject("flowConfig",flowConfig);
		return view;
	}
	
	/**
	 * 更新流程属性
	 * @param flowId 流程主键
	 * @param attrType 更新属性类型
	 * @param attrValue 新属性值
	 * @return
	 */
	@RequestMapping("/updateFlowAttr")
	public ModelAndView updateFlowAttr(String attrType,SpFlowModel flowModel){
		ModelAndView view = new ModelAndView("redirect:/flowDesign/editFlowBaseInfo?sid="+this.getSid()+"&activityMenu=sp_m_2.2&flowId="+flowModel.getId());
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		if(!userInfoService.isAdmin(this.getSessionUser())){
			view = new ModelAndView("redirect:/flowDesign/listFlowModel?sid="+this.getSid()+"&activityMenu=sp_m_2.2");
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}else if(null ==attrType || "".equals(attrType.trim())){
			this.setNotification(Notification.ERROR,"更新失败！流程attrType="+attrType);
			return view;
		}
		if("status".equals(attrType)){
			view = new ModelAndView("redirect:/flowDesign/listFlowModel?sid="+this.getSid()+"&activityMenu=sp_m_2.2");
		}
		flowDesignService.updateFlowAttr(userInfo,attrType,flowModel);
		SpFlowModel flowConfig = flowDesignService.querySpFlowModel(userInfo, flowModel.getId());
		view.addObject("flowConfig",flowConfig);
		this.setNotification(Notification.SUCCESS,"更新成功！");
		return view; 
	}
	
	/**
	 * 删除部署流程模型
	 * @param flowId 主键
	 * @param activityMenu
	 * @return
	 */
	@RequestMapping("/delFlow")
	public ModelAndView delFlow(Integer flowId,String activityMenu){
		ModelAndView view = new ModelAndView("redirect:/flowDesign/listFlowModel?pager.pageSize="+PaginationContext.getPageSize()+"&sid="+this.getSid()+"&activityMenu="+activityMenu);
		if(!userInfoService.isAdmin(this.getSessionUser())){
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		if(null==flowId || flowId==0){
			this.setNotification(Notification.ERROR,"删除失败！flowId="+flowId);
			return view;
		}
		UserInfo userInfo = this.getSessionUser();
		boolean isSucc = flowDesignService.delFlow(userInfo, flowId);
		if(isSucc){
			this.setNotification(Notification.SUCCESS,"删除成功！");
		}else{
			this.setNotification(Notification.ERROR,"删除失败！");
		}
		return view;
	}
	
	/**
	 * 跳转流程新增页面
	 * @param flowId 流程主键
	 * @param pstepId 父步骤主键
	 * @return
	 */
	@RequestMapping("/addFlowStepPage")
	public ModelAndView addFlowStepPage(Integer flowId,Integer pstepId){
		ModelAndView view = new ModelAndView("/sp/flowDesign/addFlowStep");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		if(null==flowId || flowId==0){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"新增失败！flowId="+flowId);
			return view;
		}
		List<SpFlowStep> listNextStepForSelect = flowDesignService.listOtherFlowStep(//获取父步骤的所有后代步骤
				this.getSessionUser().getComId(),flowId,pstepId);
		view.addObject("listNextStepForSelect", listNextStepForSelect);
		view.addObject("flowId", flowId);
		SpFlowStep pStepVo = flowDesignService.queryFlowStepAllInfo(this.getSessionUser().getComId(), flowId, pstepId);//父步骤基本信息
		view.addObject("pStepVo", pStepVo);
		//根据flowId查询流程模型信息
		SpFlowModel spFlowModel = flowDesignService.querySpFlowModel(this.getSessionUser(), flowId);
		view.addObject("spFlowModel",spFlowModel);
		return view;
	}
	
	/**
	 * 新增流程步骤
	 * @param stepVo
	 * @return
	 */
	@RequestMapping("/addFlowStep")
	public ModelAndView addFlowStep(SpFlowStep stepVo){
		ModelAndView view = new ModelAndView("/refreshParent");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		if(null==stepVo.getFlowId() || stepVo.getFlowId()==0){
			this.setNotification(Notification.ERROR,"新增失败！flowId="+stepVo.getFlowId());
			return view;
		}
		if(StringUtils.isEmpty(stepVo.getSpCheckCfg())){
			stepVo.setSpCheckCfg(ConstantInterface.SPSTEP_CHECK_NO);
		}
		flowDesignService.addFlowStep(stepVo,this.getSessionUser());//新增步骤
		return view;
	}
	
	/**
	 * 删除流程步骤
	 * @param flowId 流程主键
	 * @param stepId 流程步骤主键
	 * @return
	 */
	@RequestMapping("/delFlowStep")
	public ModelAndView delFlowStep(Integer flowId,Integer stepId,String activityMenu){
		ModelAndView view = new ModelAndView("redirect:/flowDesign/editFlowStepConfig?sid="+this.getSid()+"&activityMenu=sp_m_2.2&flowId="+flowId);
		if(!userInfoService.isAdmin(this.getSessionUser())){
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		if(null==flowId || flowId==0){
			this.setNotification(Notification.ERROR,"删除失败！flowId="+flowId);
			return view;
		}else if(null==stepId || stepId==0){
			this.setNotification(Notification.ERROR,"删除失败！stepId="+stepId);
			return view;
		}
		flowDesignService.deleteFlowStep(this.getSessionUser(),flowId,stepId);//删除步骤
		return view;
	}
	
	/**
	 * 流程步骤编辑
	 * @param flowId 流程主键
	 * @param stepId 流程步骤主键
	 * @param nextStepWay 流程下步骤扭转方式
	 * @return
	 */
	@RequestMapping("/editFlowStepPage")
	public ModelAndView editFlowStepPage(Integer flowId,Integer stepId,String nextStepWay){
		ModelAndView view = new ModelAndView("/sp/flowDesign/editFlowStep");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}else if(null==flowId || flowId==0){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"编辑失败！flowId="+flowId);
			return view;
		}else if(null==stepId || stepId==0){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"编辑失败！stepId="+stepId);
			return view;
		}
		SpFlowStep stepVo = flowDesignService.queryFlowStepAllInfo(this.getSessionUser().getComId(), flowId, stepId);//基本信息
		stepVo.setNextStepWay(nextStepWay);//设置流程下步骤扭转方式
		List<SpFlowStep> listNextStepForSelect = flowDesignService.listOtherFlowStep(//获取其它步骤
				this.getSessionUser().getComId(),flowId,stepId);
		view.addObject("listNextStepForSelect", listNextStepForSelect);
		view.addObject("stepVo", stepVo);
		//构建步骤执行人JSON
		Gson gson = new Gson();
		String executorJson = gson.toJson(stepVo.getListExecutor());
		view.addObject("executorJson",executorJson);
		//构建下步步骤候选JSON
		String nextStepForSelectJson = gson.toJson(listNextStepForSelect);
		view.addObject("nextStepJson",nextStepForSelectJson);
		//构建下步步骤JSON
		String nextStepJson = gson.toJson(stepVo.getListNextStep());
		view.addObject("nextStepForDefaultJson",nextStepJson);
		//根据flowId查询流程模型信息
		SpFlowModel spFlowModel = flowDesignService.querySpFlowModel(this.getSessionUser(), flowId);
		view.addObject("spFlowModel",spFlowModel);
		return view;
	}
	
	/**
	 * 更新流程步骤
	 * @param stepVo 步骤配置信息
	 * @return
	 */
	@RequestMapping("/editFlowStep")
	public ModelAndView editFlowStep(SpFlowStep stepVo){
		ModelAndView view = new ModelAndView("/refreshParent");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}else if(null==stepVo.getId() || stepVo.getId()==0){
			this.setNotification(Notification.ERROR,"更新失败！步骤主键id="+stepVo.getId());
			return view;
		}else if(null==stepVo.getFlowId() || stepVo.getFlowId()==0){
			this.setNotification(Notification.ERROR,"更新失败！flowId="+stepVo.getFlowId());
			return view;
		}
		if(StringUtils.isEmpty(stepVo.getSpCheckCfg())){
			stepVo.setSpCheckCfg(ConstantInterface.SPSTEP_CHECK_NO);
		}
		// 更新步骤
		Map<String,Object> map = flowDesignService.updateFlowStep(stepVo, this.getSessionUser());
		if(map.get("status").equals("y")){
			this.setNotification(Notification.SUCCESS, "更新成功！");
		}else{
			this.setNotification(Notification.ERROR, map.get("info").toString());
		}
		return view;
	}
	
	/**
	 * 跳转流程步骤条件设置页面
	 * @param flowId
	 * @param stepId
	 * @return
	 */
	@RequestMapping("/addFlowStepConditionsPage")
	public ModelAndView addFlowStepConditionsPage(Integer flowId,Integer stepId){
		ModelAndView view = new ModelAndView("/sp/flowDesign/addFlowStepConditions");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}else if(null==flowId || flowId==0){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"编辑失败！flowId="+flowId);
			return view;
		}else if(null==stepId || stepId==0){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"编辑失败！stepId="+stepId);
			return view;
		}
		SpFlowStep stepVo = flowDesignService.queryFlowStep(this.getSessionUser().getComId(), flowId, stepId);//基本信息
		view.addObject("stepVo", stepVo);
		List<FormCompon> listGlobalVar = new ArrayList<FormCompon>();
		FormCompon userC = new FormCompon();
		userC.setTitle("发起人");
		userC.setFieldId(ConstantInterface.FILEID_CREATOR);//自定义的控件ID
		listGlobalVar.add(userC);
		FormCompon userD = new FormCompon();
		userD.setTitle("发起人所在部门");
		userD.setFieldId(ConstantInterface.FILEID_DEP);//自定义的控件ID
		listGlobalVar.add(userD);
		List<FormCompon> listFormCompont = flowDesignService.listAllFormCompont(this.getSessionUser(),flowId);//获取表单控件
		if(null!=listFormCompont && !listFormCompont.isEmpty()){
			listGlobalVar.addAll(listFormCompont);
		}
		view.addObject("listFormCompont",listGlobalVar);
		List<SpStepConditions> listSpStepCondition = flowDesignService.listFlowStepConditions(this.getSessionUser(), flowId, stepId);//获取表单条件数据集
		view.addObject("listSpStepCondition", listSpStepCondition);
		return view;
	}
	
	/**
	 * 添加步骤条件
	 * @param stepVo 步骤配置信息
	 * @return
	 */
	@RequestMapping("/addFlowStepConditions")
	public ModelAndView addFlowStepConditions(SpFlowStep stepVo){
		ModelAndView view = new ModelAndView("/refreshParent");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		if(null==stepVo.getFlowId() || stepVo.getFlowId()==0){
			this.setNotification(Notification.ERROR,"新增失败！flowId="+stepVo.getFlowId());
			return view;
		}else if(null==stepVo.getId() || stepVo.getId()==0){
			this.setNotification(Notification.ERROR,"新增失败！stepId="+stepVo.getId());
			return view;
		}
		flowDesignService.addFlowStepConditions(stepVo,this.getSessionUser());//添加步骤条件
		return view;
	}
	
	/**
	 * 流程部署
	 * @param flowId
	 * @return
	 */
	@RequestMapping("/spFlowDeploy")
	public ModelAndView spFlowDeploy(Integer flowId){
		ModelAndView view = new ModelAndView("redirect:/flowDesign/listFlowModel?sid="+this.getSid()+"&activityMenu=sp_m_2.2");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}else if(null==flowId || flowId==0){
			this.setNotification(Notification.ERROR,"编辑失败！flowId="+flowId);
			return view;
		}
		flowDesignService.initSpFlowDeploy(this.getSessionUser(),flowId);//流程部署
		return view;
	}
	
	/**
	 * 获取审批流程数据集
	 * @param spFlowModel
	 * @return
	 */
	@RequestMapping("/listSpFlow")
	public ModelAndView listSpFlow(SpFlowModel spFlowModel){
//		ModelAndView view = new ModelAndView("/sp/sp_center");
		ModelAndView view = new ModelAndView("/sp/workFlow/newSpFlowFor");
		UserInfo userInfo = this.getSessionUser();
//		List<SpFlowModel> listSpFlow = flowDesignService.listSpFlowForSp(userInfo,spFlowModel);
//		view.addObject("listSpFlow",listSpFlow);
		view.addObject("userInfo",userInfo);
		view.addObject("spFlowModel",spFlowModel);
//		List<SpFlowType> listSpFlowType = flowDesignService.listSpFlowType(this.getSessionUser());
//		view.addObject("listSpFlowType",listSpFlowType);
		List<SpFlowType> listSpFlowType = flowDesignService.listSpFlowTypeOfFlowModel(userInfo);
		view.addObject("listSpFlowType",listSpFlowType);
		return view;
	}
	
	/**
	 * 跳转流程分类维护页面
	 * @return
	 */
	@RequestMapping("/listSpFlowType")
	public ModelAndView listSpFlowType(){
		ModelAndView view = new ModelAndView("/sp/flowDesign/listSpFlowType");
		view.addObject("userInfo",this.getSessionUser());
		return view;
	}
	
	/**
	 * 添加流程分类
	 * @param formSort
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxListSpFlowType")
	public Map<String,Object> listSpFlowType(SpFlowType spFlowType){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		List<SpFlowType> listSpFlowType = flowDesignService.listSpFlowType(userInfo);
		map.put("status", "y");
		map.put("listSpFlowType", listSpFlowType);
		return map;
	}
	/**
	 * 修改流程分类属性
	 * @param spFlowType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateSpFlowType")
	public Map<String,Object> updateSpFlowType(SpFlowType spFlowType)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		flowDesignService.updateSpFlowType(spFlowType,userInfo);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 删除流程分类
	 * @param spFlowTypeId 流程分类主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delSpFlowType")
	public Map<String,Object> delSpFlowType(Integer spFlowTypeId)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		boolean falg = flowDesignService.delSpFlowType(spFlowTypeId, userInfo);
		if(falg){
			map.put("status", "y");
		}else{
			map.put("status", "f");
			map.put("info", "流程分类不存在");
		}
		return map;
	}
	
	/**
	 * 获取流程分类最大排序序号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetSpFlowTypeOrder")
	public Map<String, Object> ajaxGetSpFlowTypeOrder() {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Integer orderNum = flowDesignService.querySpFlowTypeOrderMax(sessionUser.getComId());
		// 取出 排序号
		map.put("orderNum", orderNum);

		map.put("status", "y");
		return map;
	}
	
	/**
	 * 添加流程分类
	 * @param spFlowType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addSpFlowType")
	public Map<String,Object> addSpFlowType(SpFlowType spFlowType){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Integer id = flowDesignService.addSpFlowType(spFlowType,userInfo);
		spFlowType.setId(id);
		map.put("status", "y");
		map.put("spFlowType", spFlowType);
		return map;
	}
	
	/**
	 * 流程克隆
	 * @param flowId 被克隆流程主键
	 * @return
	 */
	@RequestMapping("/cloneFlowModel")
	public ModelAndView cloneFlowModel(Integer flowId){
		ModelAndView view = new ModelAndView("redirect:/flowDesign/listFlowModel?pager.pageSize=10&sid="+this.getSid()+"&activityMenu=sp_m_2.2");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}else if(CommonUtil.isNull(flowId)){
			this.setNotification(Notification.ERROR,"克隆主键不能空NULL。");
			return view;
		}
		UserInfo userInfo = this.getSessionUser();
		flowDesignService.initCloneFlowModel(userInfo,flowId);
		view.addObject("userInfo",userInfo);
		return view;
	}
		
	/***************************************2016-12-07之前*********************************************************/
	
	@RequestMapping("/flowCfg")
	public ModelAndView flowStepCfg(String activityMenu){
		ModelAndView view = new ModelAndView("/sp/flowDesign/flowCfg");
		String process = flowDesignService.orgFlowStepInit();
		view.addObject("process",process.toString());
		view.addObject("activityMenu",activityMenu);
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 获取流程模型集合
	 * @return
	 */
	@RequestMapping("/listFlowModel")
	public ModelAndView listFlowModel(String activityMenu,SpFlowModel spFlowModel){
		ModelAndView view = new ModelAndView("/sp/sp_center");
		view.addObject("activityMenu",activityMenu);
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		List<SpFlowModel> listFlowModel = flowDesignService.listFlowModel(this.getSessionUser(),spFlowModel);
		view.addObject("listFlowModel",listFlowModel);
		List<SpFlowType> listSpFlowType = flowDesignService.listSpFlowType(this.getSessionUser());
		view.addObject("listSpFlowType",listSpFlowType);
		view.addObject("spFlowModel",spFlowModel);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_FLOW_SP);
		return view;
	}
	
	/**
	 * 流程模型分页列表
	 * @param spFlowModel 筛选参数对象
	 * @return
	 */
	@RequestMapping("/listFlowModelForSelect")
	public ModelAndView listFlowModelForSelect(SpFlowModel spFlowModel){
		ModelAndView view = new ModelAndView("/sp/flowDesign/listFlowModelForSelect");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		List<SpFlowModel> listFlowModel = flowDesignService.listFlowModel(this.getSessionUser(),spFlowModel);
		view.addObject("listFlowModel",listFlowModel);
		List<SpFlowType> listSpFlowType = flowDesignService.listSpFlowType(this.getSessionUser());
		view.addObject("listSpFlowType",listSpFlowType);
		view.addObject("spFlowModel",spFlowModel);
		return view;
	}
	
	/**
	 * 打开子流程关联配置页面
	 * @param pFlowId 主流程主键
	 * @return
	 */
	@RequestMapping("/spFlowModelRelevancePage")
	public ModelAndView spFlowModelRelevance(Integer pFlowId){
		ModelAndView view = new ModelAndView("/sp/flowDesign/spFlowModelRelevance");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowModel spFlowModelRelevanceCfg = flowDesignService.querySpFlowModelRelevanceCfg(pFlowId,userInfo);//主流程配置信息
		view.addObject("spFlowModelRelevanceCfg",spFlowModelRelevanceCfg);
		List<FormCompon> listSonFlowFormCompons = null;//子流程表单控件
		if(null!=spFlowModelRelevanceCfg && null!=spFlowModelRelevanceCfg.getSonFlowId() 
				&& spFlowModelRelevanceCfg.getSonFlowId()>0){
			listSonFlowFormCompons = flowDesignService.listFlowFormCompons(spFlowModelRelevanceCfg.getSonFlowId(),userInfo);
			flowDesignService.flowComponChecked(pFlowId, userInfo, listSonFlowFormCompons);
		}
		view.addObject("listSonFlowFormCompons",listSonFlowFormCompons);
		return view;
	}
	
	/**
	 * 根据流程主键获取表单控件数据集
	 * @param flowId 流程主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/formCompons")
	public Map<String,Object> listSonFlowFormCompons(Integer flowId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		List<FormCompon> listSonFlowFormCompons = flowDesignService.listFlowFormCompons(flowId,userInfo);
		map.put("status", "y");
		map.put("formCompons",listSonFlowFormCompons);
		return map;
	}
	
	/**
	 * 流程关联关系添加
	 * @param spFlowRelevanceCfg 关联配置关系
	 * @return
	 */
	@RequestMapping("/initFlowModelRelevance")
	public ModelAndView initFlowModelRelevance(SpFlowRelevanceCfg spFlowRelevanceCfg){
		ModelAndView view = new ModelAndView("/refreshParent");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		UserInfo userInfo = this.getSessionUser();
		flowDesignService.initFlowModelRelevance(userInfo,spFlowRelevanceCfg);//流程关联关系添加
		view.addObject("userInfo",userInfo);
		this.setNotification(Notification.SUCCESS,"流程关联关系添加成功。");
		return view;
	}
	
	/**
	 * 清除流程关联关系
	 * @param pflowId 流程主键
	 * @return
	 */
	@RequestMapping("/delFlowModelRelevance")
	public ModelAndView delFlowModelRelevance(Integer pflowId){
		ModelAndView view = new ModelAndView("/refreshParent");
		if(!userInfoService.isAdmin(this.getSessionUser())){
			this.setNotification(Notification.ERROR,"只有管理员才能管理流程！");
			return view;
		}
		UserInfo userInfo = this.getSessionUser();
		flowDesignService.delFlowModelRelevance(userInfo,pflowId);//清除流程关联关系
		view.addObject("userInfo",userInfo);
		this.setNotification(Notification.SUCCESS,"清除流程关联关系成功。");
		return view;
	}
}
