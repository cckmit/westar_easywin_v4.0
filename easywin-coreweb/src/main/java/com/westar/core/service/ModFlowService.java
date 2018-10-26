package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.westar.base.enums.ActLinkTypeEnum;
import com.westar.base.enums.ActMultLoopEnum;
import com.westar.base.enums.ModSpStateEnum;
import com.westar.base.model.Announcement;
import com.westar.base.model.Consume;
import com.westar.base.model.ConsumeUpfile;
import com.westar.base.model.ForMeDo;
import com.westar.base.model.MeetSummary;
import com.westar.base.model.Meeting;
import com.westar.base.model.SpFlowCurExecutor;
import com.westar.base.model.SpFlowHiExecutor;
import com.westar.base.model.SpFlowHiStep;
import com.westar.base.model.SpFlowHiStepRelation;
import com.westar.base.model.SpFlowHuiQianInfo;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.SpFlowModel;
import com.westar.base.model.SpFlowNoticeUser;
import com.westar.base.model.SpFlowRelateData;
import com.westar.base.model.SpFlowStepConditions;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.ModFormStepData;
import com.westar.base.pojo.ModSpConf;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.ModFlowDao;
import com.westar.core.thread.sendPhoneMsgThread;

@Service
public class ModFlowService {
	@Autowired
	ModFlowDao modFlowDao;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	FlowDesignService flowDesignService;
	
	@Autowired
	CusActivitService cusActivitService;
	
	@Autowired
	PhoneMsgService phoneMsgService;
	
	@Autowired
	UserConfService userConfService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	ForMeDoService forMeDoService;
	
	@Autowired
	MeetingService meetingService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ConsumeService consumeService;

	private static final Log loger = LogFactory.getLog(ModFlowService.class);

	/**
	 * 添加审批的告知人员
	 * @param sessionUser 当前操作人员
	 * @param busId 业务主键
	 * @param noticeUsers 告知人员集合
	 * @param msgSendFlag 是否发送关心标识
	 * @param exectors 执行人信息
	 * @param busType 业务类型
	 * @param msgContent 发送消息内容
	 */
	public void addSpFlowNoticeUser(UserInfo sessionUser,Integer busId,
			List<SpFlowNoticeUser> noticeUsers,
			String msgSendFlag, Set<Integer> exectors,String busType,String msgContent) {
		if(null!=noticeUsers && !noticeUsers.isEmpty()){
			//设置与会人员信息
			Set<Integer> noticeRepUsers = new HashSet<Integer>();
			//代理人员集合
			List<ForMeDo> insteadUsers = forMeDoService.listInsteadUser(sessionUser.getComId());
			//离岗人员
			Map<Integer,ForMeDo> repUserMap = new HashMap<>();
			if(null!=insteadUsers && !insteadUsers.isEmpty()){
				for (ForMeDo insteadUser : insteadUsers) {
					repUserMap.put(insteadUser.getUserId(), insteadUser);
				}
			}
			//创建待办事项
			List<UserInfo> shares = new ArrayList<UserInfo>();
			for (SpFlowNoticeUser noticeUser : noticeUsers) {
				//本次告知人员
				Integer noticeUserId = noticeUser.getNoticeUserId();
				//告知人员是执行人，则跳过
				if(exectors.contains(noticeUserId)){
					continue;
				}
				//代理人员
				ForMeDo insteadUser = repUserMap.get(noticeUserId);
				if(null!=insteadUser){
					Integer repUserId = insteadUser.getUserId();
					if(!noticeRepUsers.contains(repUserId)){
						SpFlowNoticeUser repNoticeUser = new SpFlowNoticeUser();
						repNoticeUser.setNoticeUserId(repUserId);
						
						noticeUser.setComId(sessionUser.getComId());
						noticeUser.setBusId(busId);
						noticeUser.setBusType(busType);
						//删除原有的告知人员
						modFlowDao.delByField("spFlowNoticeUser", new String[]{"comId","busId","noticeUserId","busType"},
								new Object[]{noticeUser.getComId(),noticeUser.getBusId(),repUserId,busType});
						//添加本次的告知人员
						modFlowDao.add(noticeUser);
					}
				}
				//添加告知人员
				if(!noticeRepUsers.contains(noticeUserId)){
					noticeUser.setComId(sessionUser.getComId());
					noticeUser.setBusId(busId);
					noticeUser.setBusType(busType);
					//删除原有的告知人员
					modFlowDao.delByField("spFlowNoticeUser", new String[]{"comId","busId","noticeUserId","busType"},
							new Object[]{noticeUser.getComId(),noticeUser.getBusId(),noticeUser.getNoticeUserId(),busType});
					//添加本次的告知人员
					modFlowDao.add(noticeUser);
					UserInfo userInfo = userInfoService.getUserBaseInfo(noticeUser.getComId(), noticeUser.getNoticeUserId());
 					shares.add(userInfo);
					noticeRepUsers.add(noticeUser.getNoticeUserId());
					
				}
			}
			
			//先删除所有待办事项，添加普通提醒通知
			todayWorksService.addTodayWorks(sessionUser.getComId(), busType, busId, msgContent, shares, sessionUser.getId(), 0);
			
			//需要设置短信
			if(!StringUtils.isEmpty(msgSendFlag) 
					&& msgSendFlag.equals(ConstantInterface.MSG_SEND_YES)){
				//单线程池
				ExecutorService pool = ThreadPoolExecutor.getInstance();
				//跟范围人员发送通知消息
				pool.execute(new sendPhoneMsgThread(phoneMsgService, sessionUser.getComId(), shares, 
						new Object[]{msgContent}, ConstantInterface.MSG_FLOW_SP_NOTICE,sessionUser.getOptIP()));
			}
		}
	}
	
	/**
	 * 记录发起审批节点(开始节点)
	 * @param userInfo 当前操作人信息
	 * @param busId 流程实例主键
	 */
//	public void addSpFlowRunStepInfo(UserInfo userInfo, Integer busId,String busType) {
//		//获取流程实例步骤第一步
//		SpFlowHiStep spFlowHiStep = modFlowDao.querySpFlowHiStepOfStart(userInfo.getComId(),busId,busType);
//		//添加流程扭转过后节点信息记录
//		SpFlowHiExecutor spFlowRunStepInfo = new SpFlowHiExecutor();
//		spFlowRunStepInfo.setComId(userInfo.getComId());
//		spFlowRunStepInfo.setBusId(busId);
//		spFlowRunStepInfo.setBusType(busType);
//		spFlowRunStepInfo.setStepId(spFlowHiStep.getStepId());
//		spFlowRunStepInfo.setExecutor(userInfo.getId());
//		modFlowDao.add(spFlowRunStepInfo);
//	}
	
	
	


	/***************************固定流程选择******************************************/
	
	/**
	 * 列表选择模块使用的固定流程
	 * @param sessionUser 当前操作人员
	 * @param busType 模块
	 * @return
	 */
	public List<SpFlowModel> listModSpFlow(UserInfo sessionUser, String busType) {
		return modFlowDao.listModSpFlow(sessionUser,busType);
	}
	
	/**
	 * 初始化流程信息
	 * @param modFlowConfStr
	 * @return
	 */
	public Map<String, Object> constrModFlowConf(String modFlowConfStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isEmpty(modFlowConfStr)){
			map.put("spType", "0");
			return map;
		}
		JSONObject modFlowConf = JSONObject.parseObject(modFlowConfStr);
		String relateSpMod = modFlowConf.getString("relateSpMod");
		if(relateSpMod.equals("false")){//普通模块，非审批
			map.put("spType", "0");
			return map;
		}
		//审批
		map.put("spType", "1");
		//流程类型
		String flowType = modFlowConf.getString("flowType");
		map.put("flowType", flowType);
		if(flowType.equals("1")){//固定流程
			Integer spFlowModId = modFlowConf.getInteger("spFlowModId");
			
			String modFormStepDataStr = modFlowConf.getString("modFormStepData");
			JSONObject modFormStepDataObj = JSONObject.parseObject(modFormStepDataStr);
			ModFormStepData modFormStepData = (ModFormStepData)JSONObject.toJavaObject(modFormStepDataObj, ModFormStepData.class);
			
			
			SpFlowModel spFlowMod = (SpFlowModel) modFlowDao.objectQuery(SpFlowModel.class, spFlowModId);
			map.put("act_deployment_id", spFlowMod.getAct_deployment_id());//流程部署主键
			map.put("flowId", spFlowMod.getId());//流程主键
			map.put("sonFlowId", spFlowMod.getSonFlowId());//子流程主键
			map.put("modFormStepData", modFormStepData);//子流程主键
			return map; 
		}else{//自由流程
			List<UserInfo> stepUsers = new ArrayList<UserInfo>();
			JSONArray spUsers = modFlowConf.getJSONArray("spUsers");
			for (Object object : spUsers) {
				Integer spUserId = ((JSONObject)object).getInteger("spUserId");
				String spUserName = ((JSONObject)object).getString("spUserName");
				
				UserInfo stepUser = new UserInfo();
				stepUser.setId(spUserId);
				stepUser.setUserName(spUserName);
				stepUsers.add(stepUser);
			}
			map.put("stepUsers", stepUsers);
		}
				
		return map;
	}

	/**
	 * 复制信息
	 * @param userInfo
	 * @param busId
	 * @param flowId
	 * @param sonFlowId
	 * @param busType
	 */
	public void addCopeConf(UserInfo userInfo, Integer busId, Integer flowId,
			Integer sonFlowId, String busType) {
		//拷贝流程变量；流程分支时用
		modFlowDao.initSpFlowRunVariableKey(userInfo.getComId(),busId,flowId,busType);
		//拷贝流程步骤表单授权
		modFlowDao.initSpFlowRunStepFormControl(userInfo.getComId(),busId,flowId,busType);
		//拷贝流程实例步骤
		modFlowDao.initSpFlowHiStep(userInfo.getComId(),busId,flowId,busType);
		//流程实例步骤审批人
		modFlowDao.initSpFlowHiStepExecutor(userInfo.getComId(),busId,flowId,busType);
		//流程实例步骤间关系表
		modFlowDao.initSpFlowHiStepRelation(userInfo.getComId(),busId,flowId,busType);
		//子流程实例化后配置表
		modFlowDao.initSpFlowRunRelevanceCfg(userInfo.getComId(),busId,flowId,sonFlowId,busType);
	}
	
	/**
	 * 复制参数并添加步骤
	 * @param userInfo
	 * @param busId
	 * @param flowId
	 * @param sonFlowId
	 * @param busType
	 */
	public void addSpFlowRunStepInfoAndCopy(UserInfo userInfo, Integer busId, Integer flowId,
			Integer sonFlowId, String busType){
		//复制信息
		this.addCopeConf(userInfo, busId, flowId, sonFlowId, busType);
		//添加起始步骤
//		this.addSpFlowRunStepInfo(userInfo, busId, busType);
		
	}

	/**
	 * 添加自定义流程
	 * @param curUser
	 * @param spUser
	 * @param busId
	 * @param busType
	 * @param nextStepId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> initFreeFlowStartConf(UserInfo curUser, List<UserInfo> spUser,
			Integer busId,String busType) throws Exception {
		Map<String,Object> result = new HashMap<String,Object>();
		String act_deployment_id = null;
		
		//自定义审批流程；先定义流程审批过程
		String processInstanceId = cusActivitService.startActiviti(curUser,busId,act_deployment_id);
		result.put("actInstaceId", processInstanceId);
		
		//1、流程引擎向下走一步
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(processInstanceId, curUser);
		if(null==curTask || null==curTask.getTaskDefinitionKey()){//如果流程步骤信息为NULL时，直接返回
			curTask = cusActivitService.queryCurStepCandiateTask(processInstanceId, curUser);
			if(null==curTask || null==curTask.getTaskDefinitionKey()){
				throw new IllegalArgumentException();
			}
		}
		Integer curStepId = null;//当前步骤主键声明
		String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
		if(actTaskDefinitionKey.contains("_") && CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])){
			curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);
		}
		List<SpFlowHiStepRelation> nextStepList = modFlowDao.querySpFlowHiStepRelation(curUser.getComId(),busId,curStepId,busType);
		//下步步骤信息
		SpFlowHiStep nextStepInfo = modFlowDao.querySpFlowNextStepInfo(curUser.getComId(),busId,nextStepList.get(0).getNextStepId(),busType);
		//步骤办理候选人集合
		List<UserInfo> listSpFlowHiExecutor = this.listSpFlowHiExecutor(curUser,busId,nextStepInfo,busType,curUser.getId());
		result.put("nextStepUsers", listSpFlowHiExecutor);
		result.put("nextStepId", nextStepList.get(0).getNextStepId());
		return result;
		
	}
	
	/**
	 * 根据配置获取候选人信息
	 * @param userInfo
	 * @param instanceId 流程实例主键
	 * @param curStepInfo 步骤主键
	 * @return
	 */
	public List<UserInfo> listSpFlowHiExecutor(UserInfo userInfo,Integer busId,
			SpFlowHiStep curStepInfo,String busType,Integer creator) {
		List<UserInfo> listSpFlowHiExecutor = new ArrayList<UserInfo>();
		if(ConstantInterface.EXECUTOR_BY_SELF.equals(curStepInfo.getExecutorWay())){//发起人自己
			UserInfo creatorSelf = userInfoService.getUserBaseInfo(userInfo.getComId(),creator);
			listSpFlowHiExecutor.add(creatorSelf);
		}else if(ConstantInterface.EXECUTOR_BY_DIRECT.equals(curStepInfo.getExecutorWay())){//发起人直属上级
			UserInfo creatorSelf = userInfoService.getUserBaseInfo(userInfo.getComId(),creator);
			UserInfo directLeaderInfo = userInfoService.queryDirectLeaderInfo(creatorSelf);
			listSpFlowHiExecutor.add(directLeaderInfo);
		}else{//根据配置获取候选人信息
			listSpFlowHiExecutor = modFlowDao.listSpFlowHiExecutor(userInfo.getComId(),busId,curStepInfo.getStepId(),busType);
		}
		return listSpFlowHiExecutor;
	}
	
	/**
	 * 流程步骤主键信息
	 * @param actTaskDefinitionKey
	 * @return
	 */
	private Map<String,Integer> conStrCurStepId(String actTaskDefinitionKey) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("curStepId", null);
		map.put("stepAddNum", 0);
		Integer curStepId = null;
		if(actTaskDefinitionKey.contains("_") && CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])){
			curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);
			map.put("curStepId", curStepId);
		}else if(actTaskDefinitionKey.contains("_") && actTaskDefinitionKey.contains(":")){//此时是加签的审批流程
			//步骤的实际
			String stepStr = actTaskDefinitionKey.split(":")[1];
			curStepId = Integer.parseInt(stepStr.split("_")[1]);
			map.put("curStepId", curStepId);
			String stepAddNum = actTaskDefinitionKey.split(":")[2];
			map.put("stepAddNum", Integer.parseInt(stepAddNum.split("-")[1])+1);
		}
		return map;
	}

	/**
	 * 查询流程当前执行人
	 * @param sessionUser 当前操作人员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	public SpFlowCurExecutor querySpFlowCurExecutorV2(UserInfo sessionUser,
			Integer busId, String busType) {
		return modFlowDao.querySpFlowCurExecutorV2(sessionUser, busId, busType);
	}

	/**
	 * 取得流程模板当前步骤和下一步骤集合
	 * @param sessionUser 当前操作人员
	 * @param flowId 流程主键
	 * @return
	 */
	public SpFlowHiStep queryFlowStartNextStep(UserInfo sessionUser, Integer flowId) {
		SpFlowHiStep stepInfo = new SpFlowHiStep();
		//开始步骤
		SpFlowHiStep startStep = modFlowDao.queryFlowStartStep(sessionUser.getComId(),flowId);
		stepInfo.setCurStepInfo(startStep);
		
		//根据当前步骤主键以及步骤间关系获取下步步骤主键
		List<SpFlowHiStepRelation> nextStepList = modFlowDao.querySpFlowStepRelation(sessionUser.getComId(),startStep.getStepId(),flowId);
		if(null!=nextStepList && !nextStepList.isEmpty()){
			//开始步骤的数量
			Integer nextStepNum = nextStepList.size();
			List<SpFlowHiStep> nextStepInfoList = new ArrayList<SpFlowHiStep>();
			for (SpFlowHiStepRelation nextStep : nextStepList) {
				//下步步骤信息
				SpFlowHiStep nextStepInfo = modFlowDao.queryModFlowNextStepInfo(sessionUser.getComId(),flowId,nextStep.getNextStepId());
				//候选人
				List<UserInfo> listSpFlowHiExecutor = this.listFlowStepExecutor(sessionUser,flowId,nextStepInfo);
				nextStepInfo.setListSpFlowHiExecutor(listSpFlowHiExecutor);//步骤办理候选人集合
				if(nextStepNum>1){
					//步骤条件信息
					List<SpFlowStepConditions> listStepConditions = modFlowDao.listSpFlowStepByFlowConditions(sessionUser.getComId(),flowId,nextStep.getNextStepId());
					nextStepInfo.setListStepConditions(listStepConditions);
				}
				nextStepInfoList.add(nextStepInfo);
			}
			stepInfo.setNextStepInfoList(nextStepInfoList);
		}
		return stepInfo;
	}
	/**
	 * 取得流程模板当前步骤和下一步骤集合
	 * @param sessionUser 当前操作人员
	 * @param flowId 流程主键
	 * @return
	 */
	public SpFlowHiStep querySpFlowNextStepInfo(UserInfo userInfo,Integer busId,String busType,String actInstaceId) {
		SpFlowHiStep stepInfo = new SpFlowHiStep();
		
		//1、流程引擎向下走一步
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(actInstaceId, userInfo);
		
		if(null==curTask || null==curTask.getTaskDefinitionKey()){//如果流程步骤信息为NULL时，直接返回
			
			curTask = cusActivitService.queryCurStepCandiateTask(actInstaceId, userInfo);
			if(null==curTask || null==curTask.getTaskDefinitionKey()){//如果流程步骤信息为NULL时，直接返回
				throw new IllegalArgumentException();
			}
		}
		
		Map<String,Integer> stepInfoMap = this.conStrCurStepId(curTask.getTaskDefinitionKey());
		//当前步骤主键声明
		Integer curStepId = stepInfoMap.get("curStepId");
		if(cusActivitService.haveActSonTask(actInstaceId,curTask)){
			stepInfo.setStepType("huiqianing");
			return stepInfo;
		}else{
			 List<Task> listCurTask = cusActivitService.listCurTask(actInstaceId);
			 if(null!=listCurTask && listCurTask.size()>1){
				stepInfo.setStepType("multExecute");
				return stepInfo;
			 }
		}
		//当前步步骤信息
		SpFlowHiStep curStepInfo = modFlowDao.querySpFlowNextStepInfo(userInfo.getComId(),busId,curStepId,busType);
		stepInfo.setCurStepInfo(curStepInfo);
		
		//根据当前步骤主键以及步骤间关系获取下步步骤主键
		List<SpFlowHiStepRelation> nextStepList = modFlowDao.querySpFlowHiStepRelation(userInfo.getComId(),busId,curStepId,busType);
		if(null!=nextStepList && !nextStepList.isEmpty()){
			//开始步骤的数量
			Integer nextStepNum = nextStepList.size();
			List<SpFlowHiStep> nextStepInfoList = new ArrayList<SpFlowHiStep>();
			for (SpFlowHiStepRelation nextStep : nextStepList) {
				//下步步骤信息
				SpFlowHiStep nextStepInfo = modFlowDao.querySpFlowNextStepInfo(userInfo.getComId(),busId,nextStep.getNextStepId(),busType);
				//候选人
				List<UserInfo> listSpFlowHiExecutor = this.listSpFlowHiExecutor(userInfo,busId,nextStepInfo,busType,userInfo.getId());
				nextStepInfo.setListSpFlowHiExecutor(listSpFlowHiExecutor);//步骤办理候选人集合
				if(nextStepNum>1){
					//步骤条件信息
					List<SpFlowStepConditions> listStepConditions = modFlowDao.listSpFlowStepConditions(userInfo.getComId(),busId,busType,nextStep.getNextStepId());
					nextStepInfo.setListStepConditions(listStepConditions);
				}
				
				nextStepInfoList.add(nextStepInfo);
			}
			stepInfo.setNextStepInfoList(nextStepInfoList);
		}
		stepInfo.setActivitiTaskId(curTask.getId());
		return stepInfo;
	}
	
	/**
	 * 查询流程模板的步骤执行人
	 * @param userInfo 当前操作人员
	 * @param flowId 流程主键
	 * @param curStepInfo 当前步骤
	 * @return
	 */
	public List<UserInfo> listFlowStepExecutor(UserInfo userInfo,Integer flowId,
			SpFlowHiStep curStepInfo) {
		List<UserInfo> listSpFlowHiExecutor = new ArrayList<UserInfo>();
		if(ConstantInterface.EXECUTOR_BY_SELF.equals(curStepInfo.getExecutorWay())){//发起人自己
			UserInfo creatorSelf = userInfoService.getUserBaseInfo(userInfo.getComId(),userInfo.getId());
			listSpFlowHiExecutor.add(creatorSelf);
		}else if(ConstantInterface.EXECUTOR_BY_DIRECT.equals(curStepInfo.getExecutorWay())){//发起人直属上级
			UserInfo directLeaderInfo = userInfoService.queryDirectLeaderInfo(userInfo);
			listSpFlowHiExecutor.add(directLeaderInfo);
		}else{//根据配置获取候选人信息
			listSpFlowHiExecutor = modFlowDao.listModFlowStepExecutor(userInfo.getComId(),flowId,curStepInfo.getStepId());
		}
		return listSpFlowHiExecutor;
	}
	/**
	 * 初始化模块流程信息
	 * @param userInfo
	 * @param map
	 * @param busId
	 * @param busType
	 * @return
	 */
	public String initModFlowStartConf(UserInfo userInfo, Map<String, Object> map,
			Integer busId, String busType) {
		//流程主键
		Integer flowId = (Integer) map.get("flowId");
		//关联流程主键
		Integer sonFlowId = (Integer) map.get("sonFlowId");
		//拷贝赋值并记录发起审批节点(开始节点)
		this.addSpFlowRunStepInfoAndCopy(userInfo,busId,flowId,sonFlowId,busType);
		
		String act_deployment_id = (String) map.get("act_deployment_id");
		//启动流程
		String processInstanceId = cusActivitService.startActiviti(userInfo, busId,act_deployment_id);
		return processInstanceId;
	}

	/**
	 * 设置流程的下一步骤
	 * @param sessionUser
	 * @param paramMap
	 * @param busId
	 * @param busType
	 * @throws Exception 
	 */
	public Map<String,Object> updateModFlowNextStep(UserInfo sessionUser, Map<String, Object> paramMap, 
			Integer busId, String busType) throws Exception{
		//需要返回的结果信息
		Map<String,Object> result = new HashMap<String,Object>();
		/*
		 * 取得的步骤配置信息，主要包括
		 * 1、流程下一步主键 nextStepId
		 * 2、流程引擎部署主键 actInstaceId
		 * 3、流程下一步骤的办理人员
		 * 4、审批结果 spState
		 */
		ModFormStepData modFormStepData = (ModFormStepData) paramMap.get("modFormStepData");
		//下一步骤的步骤主键
		Integer nextStepId = modFormStepData.getNextStepId();
//		//替岗人员设定
//		if(null!=modFormStepData.getNextStepUsers() && !modFormStepData.getNextStepUsers().isEmpty()){
//			List<UserInfo> nextStepUsers = insteadUserService.queryInsteadUser(sessionUser.getComId(),modFormStepData.getNextStepUsers());
//			modFormStepData.setNextStepUsers(nextStepUsers);
//		}
		//流程引擎部署主键
		String actInstaceId = (String) paramMap.get("actInstaceId");
		//1、流程当前事项信息
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(actInstaceId, sessionUser);
		if(null==curTask || null==curTask.getTaskDefinitionKey()){//如果流程步骤信息为NULL时，直接返回
			loger.error("流程："+actInstaceId+"的当前操作人员", new IllegalArgumentException());
			throw new IllegalArgumentException();
		}
		//conStrCurStepId
		Map<String,Integer> stepInfoMap = this.conStrCurStepId(curTask.getTaskDefinitionKey());
		//当前步骤主键声明
		Integer curStepId = stepInfoMap.get("curStepId");
		/***********************判断当前activiti 事项是否有子事项对象未完成***************************/
		if(cusActivitService.haveActSonTask(actInstaceId,curTask)){
			result.put("status", "f");
			result.put("info", "会签完成后，才能继续提交。");
			return result;
		}
		
		//记录流程当前审批人
		SpFlowHiExecutor spFlowHiExecutor = new SpFlowHiExecutor();
		spFlowHiExecutor.setComId(sessionUser.getComId());
		spFlowHiExecutor.setBusId(busId);
		spFlowHiExecutor.setBusType(busType);
		spFlowHiExecutor.setStepId(curStepId);
		spFlowHiExecutor.setExecutor(sessionUser.getId());
		modFlowDao.add(spFlowHiExecutor);
		
		/*********************流程扭转到下一节点开始***************************/
		
		//更新审批步骤用时以及审批意见
		cusActivitService.addComment(curTask.getId(), actInstaceId, null == modFormStepData.getSpIdea() ? ""
				: modFormStepData.getSpIdea());
		
		Map<String, Object> variables = new HashMap<String, Object>();
		List<UserInfo> nextStepUsersA = modFormStepData.getNextStepUsers();
		if(null!=nextStepUsersA && !nextStepUsersA.isEmpty()){
			variables.put("var_"+ConstantInterface.EXECUTOR_BY_APPOINT,
					CommonUtil.buildFlowExetutor(sessionUser.getComId(),nextStepUsersA.get(0).getId()));
		}else{
			variables.put("var_"+ConstantInterface.EXECUTOR_BY_APPOINT,
					CommonUtil.buildFlowExetutor(sessionUser.getComId(),sessionUser.getId()));
		}
		
		//审批状态 0 不同意 1同意
		Integer spState = modFormStepData.getSpState();
		if(null!=spState && 0==spState){//审批状态 0 不同意时
			cusActivitService.initEndProcess(curTask.getId());//中止流程
			result.put("status", "y");
			result.put("spState", ModSpStateEnum.REFUSE.getValue());
		}else{
			//获取执行中非会签步骤信息
			ActivityImpl nextActivity = cusActivitService.findActivitiImplByFlowStepId(curTask.getId(),nextStepId.toString());
			if(null == nextActivity){
				cusActivitService.initCommitProcess(curTask.getId(), variables,null);//引擎事项扭转
			}else{
				cusActivitService.initCommitProcess(curTask.getId(), variables,nextActivity.getId());//引擎事项扭转
			}
			
			List<UserInfo> userInfos = modFormStepData.getNextStepUsers();
			if(null!=userInfos && userInfos.size()==1){
				Integer newAssignerId = userInfos.get(0).getId();
				cusActivitService.updateSpInsAssignByProcessInstanceId(sessionUser,actInstaceId, newAssignerId);
				
			}
			
		}
		/*********************流程扭转到下一节点结束***************************/
		
		
		
		//更新步骤审批人员
		//首先删除当前办理人员
		SpFlowCurExecutor curExecutor = this.querySpFlowCurExecutorV2(sessionUser,busId,busType);
		String executeType = ActLinkTypeEnum.ASSIGNEE.getValue();
		if(null!=curExecutor){
			executeType = curExecutor.getExecuteType();
			modFlowDao.delById(SpFlowCurExecutor.class, curExecutor.getId());
		}
		
		//查询流程是否办结
		ProcessInstance pi = cusActivitService.queryProcessInstance(actInstaceId);
		if(pi==null){//流程结束了
			if(null!=result.get("status") && result.get("status").equals("y")){
				return result; 
			}
			result.put("status", "y");
			result.put("spState", ModSpStateEnum.FIHISH.getValue());
			return result;
		}else{
			//是办理人标识
			Boolean assignState = true;
			//多人审批办理状态
			String multLoopState = modFormStepData.getMultLoopState();
			if(StringUtils.isEmpty(multLoopState)){
				//默认认领
				multLoopState = ActMultLoopEnum.SINGLE.getValue();
				modFormStepData.setMultLoopState(multLoopState);
			}
			//下一步骤的执行人员
			List<UserInfo> nextStepUsers = modFormStepData.getNextStepUsers();
			if(assignState){//非候选
				if(!executeType.equals(ActLinkTypeEnum.HUIQIAN.getValue())){//当前执行步骤不是会签
					Set<Integer> exectors = null;
					//获取当前实例化流程的执行事项集合
					List<Task> listNextTask = cusActivitService.listCurTask(actInstaceId);
					if(null!=nextStepUsers && nextStepUsers.size()>1 && multLoopState.equals(ActMultLoopEnum.MULT.getValue())){
						//添加协同办理人员
						exectors = this.addSpFlowCurMultExecutor(listNextTask,sessionUser,busId,busType);//添加实例化流程审批人、并创建待办事项
					}else{
						//添加办理人
						exectors = this.addSpFlowCurExecutor(listNextTask,sessionUser,busId,busType);
					}
					result.put("status", "y");
					result.put("spState", ModSpStateEnum.NEXT.getValue());
					result.put("exectors", exectors);
				}else{
					////会签步骤删除
					result.put("status", "y");
					result.put("spState", ModSpStateEnum.HUIQIAN.getValue());
				}
			}
		}
		return result;
	}
	
	/**
	 * 添加实例化流程审批人
	 * @param listNextTask
	 * @param sessionUser
	 * @param busId
	 * @param busType
	 * @return 
	 */
	private Set<Integer> addSpFlowCurMultExecutor(List<Task> listNextTask,
			UserInfo sessionUser, Integer busId, String busType) {
		List<UserInfo> userInfos = modFlowDao.listSpFlowCurExecutor(sessionUser, busId,busType);
		Set<String> assignees = new HashSet<>();
		if(null!=userInfos && !userInfos.isEmpty()){
			for (UserInfo assignee : userInfos) {
				assignees.add(sessionUser.getComId()+":"+assignee.getId());
			}
		}
		
		Set<Integer> exectors = new HashSet<>();
		if(null!=listNextTask && !listNextTask.isEmpty()){
			for (Task task : listNextTask) {
				//办理人主键
				String assignee = task.getAssignee();
				if(sessionUser.getComId().toString().equals(assignee.split(":")[0].toString())){
					Integer executor = Integer.parseInt(assignee.split(":")[1]);
					exectors.add(executor);
					
					if(!assignees.contains(assignee)){
						//设置流程执行人
						SpFlowCurExecutor spFlowCurExecutor = new SpFlowCurExecutor();
						spFlowCurExecutor.setComId(sessionUser.getComId());
						spFlowCurExecutor.setBusId(busId);
						spFlowCurExecutor.setBusType(busType);
						spFlowCurExecutor.setExecutor(executor);
						spFlowCurExecutor.setExecuteType(ActLinkTypeEnum.ASSIGNEE.getValue());
						modFlowDao.add(spFlowCurExecutor);
					}
				}
			}
		}
		return exectors;
	}
	
	/**
	 * 添加流程办理人员
	 * @param listNextTask
	 * @param sessionUser
	 * @param busId
	 * @param busType
	 * @return
	 */
	private Set<Integer> addSpFlowCurExecutor(List<Task> listNextTask,
			UserInfo sessionUser, Integer busId, String busType) {
		List<UserInfo> userInfos = modFlowDao.listSpFlowCurExecutor(sessionUser, busId,busType);
		Set<String> assignees = new HashSet<>();
		if(null!=userInfos && !userInfos.isEmpty()){
			for (UserInfo assignee : userInfos) {
				assignees.add(sessionUser.getComId()+":"+assignee.getId());
			}
		}
		Set<Integer> exectors = new HashSet<>();
		if(null!=listNextTask && !listNextTask.isEmpty()){
			for (Task nextTask : listNextTask) {
				String assignee = nextTask.getAssignee();
				if(sessionUser.getComId().toString().equals(nextTask.getAssignee().split(":")[0].toString())){
					Integer executor = Integer.parseInt(nextTask.getAssignee().split(":")[1]);
					exectors.add(executor);
					
					if(!assignees.contains(assignee)){
						//设置流程执行人
						SpFlowCurExecutor spFlowCurExecutor = new SpFlowCurExecutor();
						spFlowCurExecutor.setComId(sessionUser.getComId());
						spFlowCurExecutor.setBusId(busId);
						spFlowCurExecutor.setBusType(busType);
						spFlowCurExecutor.setExecutor(executor);
						spFlowCurExecutor.setExecuteType(ActLinkTypeEnum.ASSIGNEE.getValue());
						modFlowDao.add(spFlowCurExecutor);
					}
					
				}
			}
		}
		return exectors;
	}
	/**
	 * 添加流程待办
	 * @param msgContent 消息内容
	 * @param userInfo 当前操作员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param map 
	 * @param exectors 执行人
	 * @throws Exception
	 */
	public void addModFlowTodo(String msgContent, UserInfo userInfo,
			Integer busId,String busType, Map<String, Object> map,
			Set<Integer> exectors) throws Exception {
		//发送告知人员
		ModFormStepData modFormStepData = (ModFormStepData) map.get("modFormStepData");
		String msgSendFlag = modFormStepData.getMsgSendFlag();
		
		//待办事项更新为普通消息0
		todayWorksService.updateTodayWorksBusSpecTo0(busType, busId);
		
		for (Integer executor : exectors) {
			//创建待办事项
			List<UserInfo> shares = new ArrayList<UserInfo>();
			UserInfo share = userInfoService.getUserBaseInfo(userInfo.getComId(),executor);
			shares.add(share);
			
			//先删除所有待办事项，添加普通提醒通知
			todayWorksService.addTodayWorks(userInfo.getComId(), busType, busId, msgContent, executor, userInfo.getId(), 1);
			
			//需要设置短信
			if(!StringUtils.isEmpty(msgSendFlag) 
					&& msgSendFlag.equals(ConstantInterface.MSG_SEND_YES)){
				//单线程池
				ExecutorService pool = ThreadPoolExecutor.getInstance();
				//跟范围人员发送通知消息
				pool.execute(new sendPhoneMsgThread(phoneMsgService, userInfo.getComId(), shares, 
						new Object[]{msgContent}, ConstantInterface.MSG_JOB_TO_DO, userInfo.getOptIP()));
			}
		}
		//发送告知消息
		List<SpFlowNoticeUser> noticeUsers = modFormStepData.getNoticeUsers();
		//添加告知人员并发送消息
		this.addSpFlowNoticeUser(userInfo, busId, noticeUsers, msgSendFlag,
				exectors,busType,msgContent);
	}

	/**
	 * 取得步骤审批信息
	 * @param userInfo
	 * @param busId
	 * @param busType
	 * @param string 
	 * @return
	 */
	public ModSpConf queryModSpConf(UserInfo userInfo, Integer busId,
			String busType, String actInstaceId) {
		ModSpConf modSpConf = new ModSpConf();
		modSpConf.setStepType(ActLinkTypeEnum.ASSIGNEE.getValue());
		//默认会签记录
		modSpConf.setHasHuiqianState(ConstantInterface.STATUS_N);
		Integer executor = 0;
		
		boolean needHuiqianContent = false;
		//审批流程办理人员
		List<SpFlowCurExecutor> listSpFlowCurExecutor = modFlowDao.listSpFlowCurExecutor(userInfo.getComId(),busId,busType,null);
		if(null!=listSpFlowCurExecutor && listSpFlowCurExecutor.size()==1){//获取当前流程的审批人
			executor = listSpFlowCurExecutor.get(0).getExecutor();
		}else if(null!=listSpFlowCurExecutor && !listSpFlowCurExecutor.isEmpty()){//有多个办理人员
			for (SpFlowCurExecutor spFlowCurExecutor : listSpFlowCurExecutor) {//遍历办理人员信息
				Integer curExecutor = spFlowCurExecutor.getExecutor();
				if(curExecutor.equals(userInfo.getId())){//是当前操作员
					if(spFlowCurExecutor.getExecuteType().equals(ActLinkTypeEnum.CANDIDATE.getValue())){
						executor = -1;
						modSpConf.setStepType(ActLinkTypeEnum.CANDIDATE.getValue());
					}else if(spFlowCurExecutor.getExecuteType().equals(ActLinkTypeEnum.ASSIGNEE.getValue())){
						executor = userInfo.getId();
					}else if(spFlowCurExecutor.getExecuteType().equals(ActLinkTypeEnum.HUIQIAN.getValue())){
						executor = userInfo.getId();
						needHuiqianContent = true;
					}else{
						executor = userInfo.getId();
					}
					break;
				}
			}
		}
		if(needHuiqianContent){
			SpFlowHuiQianInfo spFlowHuiQianInfo = modFlowDao.queryUserHuiQianInfo(userInfo,busId,busType,actInstaceId);
			if(null!=spFlowHuiQianInfo){
				modSpConf.setContent(spFlowHuiQianInfo.getContent());
			}
		}
		
		
		String stepType = "";
		if(executor.equals(userInfo.getId())){
			Task curTask = cusActivitService.queryCurTaskByTaskAssignee(actInstaceId, userInfo);
			if (null != curTask && null != curTask.getTaskDefinitionKey()) {// 如果流程步骤信息为NULL时，直接返回
				String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
				if (actTaskDefinitionKey.contains("_")
						&& CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])) {
					Integer curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);
					SpFlowHiStep spFlowHiStep = modFlowDao.querySpFlowForStepType(userInfo.getComId(),
							busId, busType,curStepId);
					stepType = spFlowHiStep.getStepType();
				}
			}
		}
		//设置步骤类型
		modSpConf.setStepType(stepType);
		
		modSpConf.setHasHuiqianState(ConstantInterface.STATUS_N);
		
		modSpConf.setExecutor(executor);
		return modSpConf;
	}

	/**
	 * 转办
	 * @param userInfo
	 * @param busId
	 * @param busType
	 * @param actInstaceId
	 * @param newAssignerId
	 * @param modFormStepDataStr
	 * @return 
	 */
	public Map<String, Object> updateSpInsAssign(UserInfo userInfo, Integer busId,
			String busType, String actInstaceId,String modFormStepDataStr) {
		JSONObject modFormStepDataObj = JSONObject.parseObject(modFormStepDataStr);
		ModFormStepData modFormStepData = (ModFormStepData)JSONObject.toJavaObject(modFormStepDataObj,
				ModFormStepData.class);
		Integer curStepId = null;// 当前步骤主键声明
		
		UserInfo newAssigner = modFormStepData.getNewAssigner();
		Integer newAssignerId  = newAssigner.getId();
		// 1、流程引擎向下走一步
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(actInstaceId,userInfo);
		if (null == curTask || null == curTask.getTaskDefinitionKey()) {// 如果流程步骤信息为NULL时，直接返回
			throw new IllegalArgumentException();
		}
		String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
		if (actTaskDefinitionKey.contains("_") && CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])) {
			curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);
		}
		Map<String,Object> map = cusActivitService.updateSpInsAssignByTask(userInfo,curTask,newAssignerId);
		if(map.get("status").equals("y")){
			map.put("info", "转办成功！");
			//当前节点的实际办理人员
			cusActivitService.addComment(curTask.getId(), actInstaceId, 
					"流程节点转办给【"+newAssigner.getUserName()+"】"+(StringUtils.isEmpty(modFormStepData.getContent())?"":("，转办意见:"+modFormStepData.getContent())));
			//修改审批事项办理人员
			List<SpFlowCurExecutor> curExecutors = modFlowDao.listSpFlowCurExecutor(userInfo.getComId(), busId,busType,null);
			if(null!=curExecutors && !curExecutors.isEmpty()){
				for (SpFlowCurExecutor spFlowCurExecutor : curExecutors) {
					Integer exector = spFlowCurExecutor.getExecutor();
					if(exector.equals(userInfo.getId())){
						spFlowCurExecutor.setExecutor(newAssignerId);
						modFlowDao.update(spFlowCurExecutor);
						break;
					}
				}
			}
			
//			SpFlowRunStepInfo spFlowRunStepInfo = modFlowDao.querySpFlowRunStepInfoOfEndTimeIsNull(userInfo.getComId(),busId,curStepId,newAssignerId,busType);
//			if(null==spFlowRunStepInfo){//记录步骤信息为NULL，则添加
//				//添加流程扭转过后节点信息记录
//				SpFlowRunStepInfo runStepInfo = new SpFlowRunStepInfo();
//				runStepInfo.setComId(userInfo.getComId());
//				runStepInfo.setBusId(busId);
//				runStepInfo.setBusType(busType);
//				runStepInfo.setStepId(curStepId);
//				runStepInfo.setAssignee(newAssignerId);
//				runStepInfo.setStartTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
//				runStepInfo.setPstepId(ConstantInterface.COMMON_ROOT);
//				modFlowDao.add(runStepInfo);
//			}
			
			// 记录流程当前审批人
			SpFlowHiExecutor spFlowHiExecutor = new SpFlowHiExecutor();
			spFlowHiExecutor.setComId(userInfo.getComId());
			spFlowHiExecutor.setBusId(busId);
			spFlowHiExecutor.setBusType(busType);
			spFlowHiExecutor.setStepId(curStepId);
			spFlowHiExecutor.setExecutor(userInfo.getId());
			modFlowDao.add(spFlowHiExecutor);
			
			List<SpFlowUpfile> spFlowUpfiles = modFormStepData.getSpFlowUpfiles();
			//添加附件信息
			this.addSpFlowUpfile(userInfo, spFlowUpfiles, curStepId, busId,busType);
			// 持久化审批意见
			if(StringUtils.isNotEmpty(modFormStepData.getSpIdea())){
				cusActivitService.addComment(curTask.getId(), actInstaceId, null == modFormStepData.getSpIdea() ? ""
						: modFormStepData.getSpIdea());
			}
			// 先删除所有待办事项，添加普通提醒通知
			todayWorksService.updateWorkToNormal(busId, userInfo.getComId(), userInfo.getId(), busType);
			todayWorksService.addTodayWorks(userInfo.getComId(), busType, busId, this.conStrMsg(busId, busType),
					newAssignerId, userInfo.getId(), 1);
			
		}
		return map;
	}
	/**
	 * 流程回退
	 * @param userInfo 当前操作员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param actInstaceId 流程以实例化主键
	 * @param modFormStepDataStr 本次需要处理的参数
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateSpInsBack(UserInfo userInfo, Integer busId,
			String busType, String actInstaceId, String modFormStepDataStr) throws Exception {
		Map<String, Object> map = new  HashMap<String, Object>();
		JSONObject modFormStepDataObj = JSONObject.parseObject(modFormStepDataStr);
		Map<String,Object> paramMap = new HashMap<>();
		ModFormStepData modFormStepData = (ModFormStepData)JSONObject.toJavaObject(modFormStepDataObj,
				ModFormStepData.class);
		paramMap.put("modFormStepData", modFormStepData);
		paramMap.put("actInstaceId", actInstaceId);
		//回退步骤主键
		String activitiTaskId = modFormStepData.getActivitiTaskId();
		if(null==activitiTaskId || "".equals(activitiTaskId.trim())){
			throw new Exception("流程回退主键activitiTaskId："+activitiTaskId);
		}
		//1、流程引擎向下走一步
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(actInstaceId, userInfo);
		
		if(null==curTask || null==curTask.getTaskDefinitionKey()){//如果流程步骤信息为NULL时，直接返回
			 throw new IllegalArgumentException();
		}
		String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
		Map<String,Integer> stepInfoMap = this.conStrCurStepId(actTaskDefinitionKey);
		Integer curStepId = stepInfoMap.get("curStepId");//当前步骤主键声明

		/***********************判断当前activiti 事项是否有子事项对象未完成***************************/
		//记录流程当前审批人
		SpFlowHiExecutor spFlowHiExecutor = new SpFlowHiExecutor();
		spFlowHiExecutor.setComId(userInfo.getComId());
		spFlowHiExecutor.setBusId(busId);
		spFlowHiExecutor.setBusType(busType);
		spFlowHiExecutor.setStepId(curStepId);
		spFlowHiExecutor.setExecutor(userInfo.getId());
		modFlowDao.add(spFlowHiExecutor);
		
		cusActivitService.addComment(curTask.getId(), actInstaceId, null == modFormStepData.getSpIdea() ? ""
				: modFormStepData.getSpIdea());
		
		//获取已执行步骤配置信息
		SpFlowHiStep hisStep = modFlowDao.querySpFlowHisStep(userInfo.getComId(),busId,busType,actInstaceId,activitiTaskId);
		//查询执行人的代理人员
		ForMeDo insteadUser = forMeDoService.queryInsteadUser(userInfo.getComId(),Integer.parseInt(hisStep.getAssignee().split(":")[1]));
		Map<String, Object> variables = new  HashMap<String, Object>();
		if(null!=insteadUser){
			if(hisStep.getStepType().equals("start")){
				variables.put("var_"+ConstantInterface.EXECUTOR_BY_SELF,
						CommonUtil.buildFlowExetutor(userInfo.getComId(),insteadUser.getUserId()));
			}else{
				variables.put("var_"+ConstantInterface.EXECUTOR_BY_APPOINT,
						CommonUtil.buildFlowExetutor(userInfo.getComId(),insteadUser.getUserId()));
			}
		}else{
			variables.put("var_"+ConstantInterface.EXECUTOR_BY_APPOINT,hisStep.getAssignee());//指定返回步骤执行人员
		}
		//跳转到指定步骤了
		cusActivitService.initCommitProcess(curTask.getId(), variables, activitiTaskId);//回退到指定事项活动对象
		Integer executor = Integer.parseInt(hisStep.getAssignee().split(":")[1]);
		cusActivitService.updateSpInsAssignByProcessInstanceId(userInfo, actInstaceId, executor);
		
		//2、变更实例化流程当前办理人
		modFlowDao.delByField("spFlowCurExecutor", 
				new String[] { "comId","busId","busType" }, 
				new Object[] { userInfo.getComId(),busId,busType});// 删除实例化流程当前审批人
		//获取当前实例化流程的执行事项集合
		List<Task> listNextTask = cusActivitService.listCurTask(actInstaceId);
		Set<Integer> exectors = this.addSpFlowCurExecutor(listNextTask,userInfo,busId,busType);//添加实例化流程审批人、并创建待办事项
		String msgContent = busType+"流程待办信息";
		if(busType.equalsIgnoreCase(ConstantInterface.TYPE_ANNOUNCEMENT)){
			Announcement announ = (Announcement) modFlowDao.objectQuery(Announcement.class, busId);
			msgContent= announ.getTitle();
		}
		this.addModFlowTodo(msgContent, userInfo, busId,busType, paramMap, exectors);
		map.put("status", "y");
		return map;
		
	}
	
	/**
	 * 审批模块信息，通过，驳回以及回退
	 * @param userInfo 当前操作人员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param actInstaceId 流程实例化主键
	 * @param modFormStepDataStr 流程流转配置数据信息
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> updateSpModFlow(UserInfo userInfo, Integer busId,
			String busType, String actInstaceId,
			String modFormStepDataStr) throws Exception {
		
		Map<String,Object> paramMap = new HashMap<>();
		
		JSONObject modFormStepDataObj = JSONObject.parseObject(modFormStepDataStr);
		ModFormStepData modFormStepData = 
				(ModFormStepData)JSONObject.toJavaObject(modFormStepDataObj,ModFormStepData.class);
		
		paramMap.put("modFormStepData", modFormStepData);
		paramMap.put("actInstaceId", actInstaceId);
		
		Map<String, Object> result = this.updateModFlowNextStep(userInfo,paramMap,busId,busType);
		if(result.get("status").toString().equals("f")){
			throw new Exception(result.get("info").toString());
		}else{
			
			//审批结果
			Integer spState = Integer.parseInt(result.get("spState").toString());
			if(spState.equals(ModSpStateEnum.REFUSE.getValue())){//流程终止
				//删除所有审批待办
				todayWorksService.updateTodayWorksBusSpecTo0(busType, busId);
				this.updateModFinish(userInfo,busId,busType,ModSpStateEnum.REFUSE.getValue());
			}else if(spState.equals(ModSpStateEnum.FIHISH.getValue())){//流程正常办结
				//删除所有审批待办
				todayWorksService.updateTodayWorksBusSpecTo0(busType, busId);
				//完结信息
				this.updateModFinish(userInfo,busId,busType,ModSpStateEnum.FIHISH.getValue());
			}else if(spState.equals(ModSpStateEnum.NEXT.getValue())){//流程正常办下一步骤
				//删除所有审批待办
				todayWorksService.updateTodayWorksBusSpecTo0(busType, busId);
				String msgContent = this.conStrMsg(busId,busType);
				if(null == msgContent){
					msgContent = busType+"流程待办信息";
				}
				Set<Integer> exectors = (Set<Integer>) result.get("exectors");
				this.addModFlowTodo(msgContent, userInfo, busId,busType, paramMap, exectors);
				//发送告知消息
				List<SpFlowNoticeUser> noticeUsers = modFormStepData.getNoticeUsers();
				//添加告知人员并发送消息
				this.addSpFlowNoticeUser(userInfo, busId, noticeUsers, modFormStepData.getMsgSendFlag(),
						exectors,busType,msgContent);
			}else if(spState.equals(ModSpStateEnum.HUIQIANING.getValue())){//流程是会签
				todayWorksService.delTodoWork(busId, userInfo.getComId(), userInfo.getId(), busType, "1");
			}
		}
		
				
		return result;
	}
	/**
	 * 消息通知信息
	 * @param busId
	 * @param busType
	 * @return
	 */
	private String conStrMsg(Integer busId, String busType) {
		String msgContent = null;
		if(busType.equalsIgnoreCase(ConstantInterface.TYPE_ANNOUNCEMENT)){
			Announcement announ = (Announcement) modFlowDao.objectQuery(Announcement.class, busId);
			msgContent= announ.getTitle();
		}else if(busType.equalsIgnoreCase(ConstantInterface.TYPE_FLOW_SP)){
			SpFlowInstance spFlowInstance = (SpFlowInstance) modFlowDao.objectQuery(SpFlowInstance.class, busId);
			msgContent= spFlowInstance.getFlowName();
		}else if(busType.equalsIgnoreCase(ConstantInterface.TYPE_MEETING_SP)){
			Meeting meeting = (Meeting) modFlowDao.objectQuery(Meeting.class, busId);
			msgContent= meeting.getTitle();
		}else if(busType.equalsIgnoreCase(ConstantInterface.TYPE_MEET_SUMMARY)){
			MeetSummary meetSummary = (MeetSummary) modFlowDao.objectQuery(MeetSummary.class, busId);
			Meeting meeting = (Meeting) modFlowDao.objectQuery(Meeting.class, meetSummary.getMeetingId());
			msgContent= meeting.getTitle();
		}else{
			msgContent = "未设定模块";
		}
		return msgContent;
	}

	/**
	 * 结束流程
	 * @param userInfo 
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param spState 审批办结状态
	 */
	private void updateModFinish(UserInfo userInfo, Integer busId, String busType,Integer spState) {
		if(ConstantInterface.TYPE_MEETING_SP.equalsIgnoreCase(busType)){
			Meeting meeting = new Meeting();
			meeting.setId(busId);
			meeting.setSpState(spState);
			modFlowDao.update(meeting);
			//需要申请会议室
			meetingService.updateFinishSpMeeting(userInfo,busId);
		}else if(ConstantInterface.TYPE_MEET_SUMMARY.equalsIgnoreCase(busType)){
			MeetSummary meetSummary = new MeetSummary();
			meetSummary.setId(busId);
			meetSummary.setSpState(spState);
			modFlowDao.update(meetSummary);
		}else{
			loger.info(busType+"___"+busId+"_____"+spState+",审批办结状态未修改");
		}
		//删除临时变量
		this.delModSpFlow(userInfo, busId, busType);
	}
	
	/**
	 * 删除流程审批信息
	 * @param userInfo 当前操作员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 */
	public void delModSpFlow(UserInfo userInfo, Integer busId, String busType){
		//删除流程实例流程当前审批人
		modFlowDao.delByField("spFlowCurExecutor", new String[] {"comId",
		"busId","busType" }, new Object[] { userInfo.getComId(),busId,busType});
		//删除流程实例变量表
		modFlowDao.delByField("spFlowRunVariableKey", new String[] {"comId",
		"busId","busType" }, new Object[] { userInfo.getComId(),busId,busType});
		//删除流程实例对象步骤授权
		modFlowDao.delByField("spFlowRunStepFormControl", new String[] {"comId",
		"busId","busType" }, new Object[] { userInfo.getComId(),busId,busType});
		//删除子流程实例化后配置
		modFlowDao.delByField("spFlowRunRelevanceCfg", new String[] {"comId",
		"busId","busType" }, new Object[] { userInfo.getComId(),busId,busType});
	}
	/**
	 * 添加步骤信息
	 * @param userInfo 当前操作员
	 * @param actUserTaskId
	 * @param actUserTaskId
	 * @param instanceId
	 */
	private void addSpFlowUpfile(UserInfo userInfo, List<SpFlowUpfile> spFlowUpfiles,
			Integer actUserTaskId, Integer busId,String busType) {
		// 流程实例化附件信息
		if (null != spFlowUpfiles && !spFlowUpfiles.isEmpty()) {
			for (SpFlowUpfile spFlowUpfile : spFlowUpfiles) {
				// 设置团队号
				spFlowUpfile.setComId(userInfo.getComId());
				// 设置流程实例化主键
				spFlowUpfile.setBusId(busId);
				spFlowUpfile.setBusType(busType);
				// 附件上传人
				spFlowUpfile.setUserId(userInfo.getId());
				// 步骤节点标识
				spFlowUpfile.setActUserTaskId(actUserTaskId == null ? "" : actUserTaskId.toString());
				modFlowDao.add(spFlowUpfile);

			}
		}
	}
	
	/**
	 * 删除个人以前的会签信息
	 * @param curStepId
	 * @param userId
	 * @param busId
	 * @param busType
	 */
	private void delPreHqStepInfo(Integer comId,Integer curStepId, Integer userId, Integer busId,
			String busType) {
		modFlowDao.delByField("spFlowRunStepInfo", new String[]{"comId","busId","busType","pstepId","assignee"},
				new Object[]{comId,busId,busType,curStepId,userId});
	}

	/**
	 * 拾取审批事项
	 * @param sessionUser 当前操作员
	 * @param busId 流程实例化主键
	 */
	public Map<String,Object> updatePickSpFlowTask(UserInfo sessionUser,Integer busId,String busType,
			String actInstaceId) {
		Map<String,Object> map = cusActivitService.updatePickSpFlowTask(sessionUser,actInstaceId);
		if(map.get("status").equals("y")){
			//删除事项的其他执行人
			List<SpFlowCurExecutor> curExecutors = modFlowDao.listSpFlowCurExecutor(sessionUser.getComId(), busId,busType,null);
			if(null!=curExecutors && curExecutors.size()>1){
				for (SpFlowCurExecutor spFlowCurExecutor : curExecutors) {
					Integer exector = spFlowCurExecutor.getExecutor();
					if(!exector.equals(sessionUser.getId())){
						//删除审批人员
						modFlowDao.delById(SpFlowCurExecutor.class, spFlowCurExecutor.getId());
						//删除待办人员的审批事项
						todayWorksService.delTodoWork(busId, sessionUser.getComId(), 
								spFlowCurExecutor.getExecutor(), ConstantInterface.TYPE_FLOW_SP, "1");
					}else{
						spFlowCurExecutor.setExecuteType(ActLinkTypeEnum.ASSIGNEE.getValue());
						modFlowDao.update(spFlowCurExecutor);
					}
				}
			}
//			//查询当前步骤的办理信息
//			List<SpFlowRunStepInfo> listSpFlowRunStepInfo = modFlowDao.listSpFlowRunStepInfo(sessionUser,busId,busType);
//			if(null!=listSpFlowRunStepInfo && !listSpFlowRunStepInfo.isEmpty()){
//				for (SpFlowRunStepInfo spFlowRunStepInfo : listSpFlowRunStepInfo) {
//					Integer exector = spFlowRunStepInfo.getAssignee();
//					if(!exector.equals(sessionUser.getId())){
//						//删除审批人员
//						modFlowDao.delById(SpFlowRunStepInfo.class, spFlowRunStepInfo.getId());
//					}
//				}
//			}
			
		}
		return map;
		
	}
	/**
	 * 获取已执行的审批步骤集合
	 * @param userInfo 当前操作人信息
	 * @param instanceId 流程实例主键
	 * @return
	 */
	public List<SpFlowHiStep> listHistorySpStep(UserInfo userInfo, 
			Integer busId, String busType, String actInstaceId) {
		return modFlowDao.listHistorySpStep(userInfo.getComId(),busId,busType,actInstaceId);
	} 
	
	/**
	 * 获取流程实例扭转历史步骤信息
	 * @param userInfo 当前操作人员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	public List<SpFlowHiStep> listSpFlowHiStep(UserInfo userInfo,Integer busId, String busType){
		return modFlowDao.listSpFlowHiStep(userInfo,busId,busType);
	}
	
	/**
	 * 分页查询流程关联附件
	 * @param instanceId 流程实例化主键
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public List<SpFlowUpfile> listPagedSpFiles(UserInfo userInfo,Integer busId, String busType) {
		return modFlowDao.listPagedSpFiles(userInfo,busId,busType);
	}
	
	/**
	 * 取得流程当前执行人
	 * @param comId 团队号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param executeType 人员类型
	 * @return
	 */
	public List<SpFlowCurExecutor> listSpFlowCurExecutor(Integer comId,
			Integer busId,String busType,String executeType){
		 List<SpFlowCurExecutor> curExecutors = modFlowDao.listSpFlowCurExecutor(comId, busId, busType,executeType);
		 return curExecutors;
	}
	
	/**
	 * 重置模块未销账数据
	 * @param flowRelateDatas
	 */
	public void updateResetData(List<SpFlowRelateData> flowRelateDatas){
		for (SpFlowRelateData spFlowRelateData : flowRelateDatas) {
			//关联模块的业务类型
			String busType = spFlowRelateData.getBusType();
			//关联数据主键
			Integer busId = spFlowRelateData.getBusId();
			
			if(ConstantInterface.TYPE_CONSUME.equals(busType)){//消费数据信息
				Consume consume = new Consume();
				consume.setId(busId);
				consume.setStatus(0);
				modFlowDao.update(consume);
			}
		}
	}

	/**
	 * 修改关联数据
	 * @param instance 流程数据
	 * @param userInfo 当前操作人员
	 * @param flowRelateDatas 需要修改的数据信息
	 * @param curStepId 
	 */
	public void updateRelateTableInfo(SpFlowInstance instance,
			UserInfo userInfo, List<SpFlowRelateData> flowRelateDatas, Integer curStepId) {
		//流程审批状态
		Integer spState = instance.getSpState();
		//流程状态
		Integer flowState = instance.getFlowState();
		//默认关联数据为处理中的状态
		Integer modDataState = 1;
		if(null!=spState && ConstantInterface.SP_STATE_PASS.equals(spState) //审批通过
				&& (null!=flowState && ConstantInterface.SP_STATE_FINISH.equals(flowState))){//流程办结
			//模块数据已处理
			modDataState = 2;
		}else if(null!=spState && ConstantInterface.SP_STATE_REFUSE.equals(spState) //审批未通过
				&& (null!=flowState && ConstantInterface.SP_STATE_FINISH.equals(flowState))){//流程办结
			//模块数据未处理
			modDataState = 0;
		}
		if(null!= flowRelateDatas && !flowRelateDatas.isEmpty()){
			for (SpFlowRelateData spFlowRelateData : flowRelateDatas) {
				//关联模块的业务类型
				String busType = spFlowRelateData.getBusType();
				//关联数据主键
				Integer busId = spFlowRelateData.getBusId();
				
				if(ConstantInterface.TYPE_CONSUME.equals(busType)){//消费数据信息
					Consume consume = new Consume();
					consume.setId(busId);
					consume.setStatus(modDataState);
					
					modFlowDao.update(consume);
					
					List<ConsumeUpfile> consumeUpfiles = consumeService.listConsumeUpfile(userInfo.getComId(), busId);
					if(null != consumeUpfiles){
						this.addConsumeUpfile(userInfo, consumeUpfiles, curStepId, instance.getId());
					}
				}
			}
		}
		
	}
	
	/**
	 * 添加消费记录附件信息
	 * @param userInfo 当前操作人员
	 * @param consumeUpfiles 记账的附件信息
	 * @param actUserTaskId 流程步骤主键
	 * @param instanceId 流程审批主键
	 */
	private void addConsumeUpfile(UserInfo userInfo,
			List<ConsumeUpfile> consumeUpfiles, Integer actUserTaskId,
			Integer instanceId){
		    //流程实例化附件信息
			if (null != consumeUpfiles && !consumeUpfiles.isEmpty()) {
				for (ConsumeUpfile consumeUpfile : consumeUpfiles) {
					SpFlowUpfile spFlowUpfile  = new SpFlowUpfile();
					// 设置团队号
					spFlowUpfile.setComId(userInfo.getComId());
					// 设置流程实例化主键
					spFlowUpfile.setBusId(instanceId);
					spFlowUpfile.setBusType(ConstantInterface.TYPE_FLOW_SP);
					//附件主键
					spFlowUpfile.setUpfileId(consumeUpfile.getUpfileId());
					// 附件上传人
					spFlowUpfile.setUserId(consumeUpfile.getUserId());
					// 步骤节点标识
					spFlowUpfile.setActUserTaskId(actUserTaskId == null ? "" : actUserTaskId.toString());
					spFlowUpfile.setAddType(3);
					modFlowDao.add(spFlowUpfile);
				}
			}
	}
	
}
