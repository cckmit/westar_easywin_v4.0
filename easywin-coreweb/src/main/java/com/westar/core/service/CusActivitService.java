package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.SpFlowModel;
import com.westar.base.model.SpFlowRunVariableKey;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FormData;
import com.westar.base.pojo.FormDataDetails;
import com.westar.base.pojo.FormDataOption;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.IdGenerator;

@Service
public class CusActivitService {
	private static Logger log = Logger.getLogger(CusActivitService.class);

	@Autowired
	RuntimeService runtimeService;
	@Autowired
	RepositoryService repositoryService;
	
	@Autowired
	HistoryService historyService;
	
	@Autowired
	TaskService actTaskService;
	
	@Autowired
	IdentityService identityService;

	@Autowired
	FlowDesignService flowDesignService;

	@Autowired
	UserInfoService userInfoService;

	/**
	 * 启动流程
	 * 
	 * @param userInfo
	 * @param flowModel
	 * @param instanceId
	 * @return
	 */
	public String startActiviti(UserInfo userInfo, SpFlowModel flowModel,
			Integer instanceId,String act_deployment_id) {
		identityService.setAuthenticatedUserId(CommonUtil.buildFlowExetutor(
				userInfo.getComId(), userInfo.getId()));// 用来设置启动流程的人员ID，引擎自动把用户ID保存到activiti:initiator中

		Map<String, Object> variables = new HashMap<String, Object>();
		UserInfo leader = userInfoService.queryDirectLeaderInfo(userInfo);// 获取直属上级信息
		variables.put(
				"var_" + ConstantInterface.EXECUTOR_BY_DIRECT,
				null == leader ? null : CommonUtil.buildFlowExetutor(
						userInfo.getComId(), leader.getId()));// 直属上级
		variables.put(
				"var_" + ConstantInterface.EXECUTOR_BY_SELF,
				CommonUtil.buildFlowExetutor(userInfo.getComId(),
						userInfo.getId()));// 创建人自己

		// 启动流程引擎并关联业务key
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(act_deployment_id,
						instanceId.toString(), variables);

		return processInstance.getId();

	}

	/**
	 * 取得启动流程引擎并关联业务key
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param formData
	 *            页面表单数据信息
	 * @param instanceId
	 *            流程实例化主键信息
	 * @param instance
	 *            流程数据库信息
	 * @return
	 */
	public String initActInstaceId(UserInfo userInfo, FormData formData,
			Integer instanceId, SpFlowInstance instance,
			List<SpFlowRunVariableKey> listSpFlowRunVariableKey) {
		identityService.setAuthenticatedUserId(CommonUtil.buildFlowExetutor(
				userInfo.getComId(), userInfo.getId()));// 用来设置启动流程的人员ID，引擎自动把用户ID保存到activiti:initiator中

		// 自定义审批流程；先定义流程审批过程
		String act_deployment_id = flowDesignService
				.initSpFlowDeployByUserDefined(userInfo, formData.getSpUser(),
						instanceId);
		// 流程参数
		Map<String, Object> variables = this.initSpVariableMap(userInfo,
				instance, formData, listSpFlowRunVariableKey);
		UserInfo leader = userInfoService.queryDirectLeaderInfo(userInfo);// 获取直属上级信息
		variables.put(
				"var_" + ConstantInterface.EXECUTOR_BY_DIRECT,
				null == leader ? null : CommonUtil.buildFlowExetutor(
						userInfo.getComId(), leader.getId()));// 直属上级
		variables.put(
				"var_" + ConstantInterface.EXECUTOR_BY_SELF,
				CommonUtil.buildFlowExetutor(userInfo.getComId(),
						userInfo.getId()));// 创建人自己

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(act_deployment_id,
						instanceId.toString(), variables);// 启动流程引擎并关联业务key
		return processInstance.getId();
	}

	/**
	 * 删除实例化的流程
	 * 
	 * @param spFlowInstance
	 */
	public void delActivitiInfo(SpFlowInstance spFlowInstance) {
		try {
			// 在删除数据相关表后，删除activity相关引擎表数据
			if (null != spFlowInstance
					&& !CommonUtil.isNull(spFlowInstance.getActInstaceId())) {
				runtimeService.deleteProcessInstance(
						spFlowInstance.getActInstaceId(), "无效的。");
				historyService.deleteHistoricProcessInstance(spFlowInstance
						.getActInstaceId());
			}
		} catch (Exception e) {
			log.error("Activiti删除流程数据出错。");
			e.printStackTrace();
		}

	}

	/**
	 * 构建流程变量Map
	 * 
	 * @param userInfo当前操作人信息
	 * @param instance
	 *            流程实例化对象
	 * @param formData
	 *            表单值对象
	 * @return
	 */
	public Map<String, Object> initSpVariableMap(UserInfo userInfo,
			SpFlowInstance instance, FormData formData,
			List<SpFlowRunVariableKey> listSpFlowRunVariableKey) {
		Map<String, Object> variables = new HashMap<String, Object>();
		Map<String, Object> valueMap = new HashMap<String, Object>();
		if (null != listSpFlowRunVariableKey&& !listSpFlowRunVariableKey.isEmpty()) {
			List<FormDataDetails> dataDetails = formData.getDataDetails();
			if (null != dataDetails && !dataDetails.isEmpty()) {
				for (FormDataDetails formCol : dataDetails) {
					if (formCol.getFormField().getComponentKey().equals("Text")
							|| formCol.getFormField().getComponentKey().equals("DateComponent")
							|| formCol.getFormField().getComponentKey().equals("DateInterval")
							|| formCol.getFormField().getComponentKey().equals("Monitor")
							|| formCol.getFormField().getComponentKey().equals("NumberComponent")) {
						if (CommonUtil.isNumeric(formCol.getContent())) {// 判断是整数
							valueMap.put(formCol.getFormField().getId().toString(),Long.parseLong(formCol.getContent()));
						} else if (CommonUtil.isFloat(formCol.getContent())) {// 判断是浮点数
							valueMap.put(formCol.getFormField().getId().toString(),Double.parseDouble(formCol.getContent()));
						} else {// 否则为文本
							valueMap.put(formCol.getFormField().getId().toString(), formCol.getContent());
						}
					} else if (formCol.getFormField().getComponentKey().equals("TextArea")) {
						valueMap.put(formCol.getFormField().getId().toString(),formCol.getDataText().getContent());
					} else {
						List<FormDataOption> listOptions = formCol.getDataOptions();
						if (null != listOptions && !listOptions.isEmpty()) {
							StringBuffer ops = new StringBuffer();
							for (FormDataOption formDataOption : listOptions) {
								ops.append(formDataOption.getContent() + ",");
							}
							ops = new StringBuffer(ops.subSequence(0,ops.length() - 1));
							valueMap.put(formCol.getFormField().getId().toString(), ops.toString());
						}
					}
				}
			}
		}
		for (SpFlowRunVariableKey spFlowRunVariableKey : listSpFlowRunVariableKey) {
			if (ConstantInterface.FILEID_CREATOR.toString().equals(spFlowRunVariableKey.getVariableKey())) {
				variables.put("var_" + ConstantInterface.FILEID_CREATOR,instance.getCreatorName());// 创建人变量
			} else if (ConstantInterface.FILEID_DEP.toString().equals(spFlowRunVariableKey.getVariableKey())) {
				variables.put("var_" + ConstantInterface.FILEID_DEP,instance.getDepName());// 创建人所在部门变量
			} else {
				variables.put("var_" + spFlowRunVariableKey.getVariableKey(),valueMap.get(spFlowRunVariableKey.getVariableKey()));// 创建人所在部门变量
			}
		}
		return variables;
	}

	/**
	 * 根据任务ID获取流程定义
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(
			String taskId) throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(findTaskById(taskId)
						.getProcessDefinitionId());

		if (processDefinition == null) {
			throw new Exception("流程定义未找到!");
		}

		return processDefinition;
	}

	/**
	 * 根据任务ID获得任务实例
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	private TaskEntity findTaskById(String taskId) throws Exception {
		TaskEntity task = (TaskEntity) actTaskService.createTaskQuery()
				.taskId(taskId).singleResult();
		if (task == null) {
			throw new Exception("任务实例未找到!");
		}
		return task;
	}

	/**
	 * 根据任务ID和节点ID获取活动节点 <br>
	 * 
	 * @param taskId
	 *            任务ID
	 * @param activityId
	 *            活动节点ID <br>
	 *            如果为null或""，则默认查询当前活动节点 <br>
	 *            如果为"end"，则查询结束节点 <br>
	 * 
	 * @return
	 * @throws Exception
	 */
	private ActivityImpl findActivitiImpl(String taskId, String activityId)
			throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

		// 获取当前活动节点ID
		if (CommonUtil.isNull(activityId)) {
			activityId = this.findTaskById(taskId).getTaskDefinitionKey();
		}

		// 根据流程定义，获取该流程实例的结束节点
		if (activityId.toUpperCase().equals("END")) {
			for (ActivityImpl activityImpl : processDefinition.getActivities()) {
				List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
				if (pvmTransitionList.isEmpty()) {
					return activityImpl;
				}
			}
		}

		// 根据节点ID，获取对应的活动节点
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);

		return activityImpl;
	}

	/**
	 * 中止流程(特权人直接审批通过等)
	 * 
	 * @param taskId
	 */
	public void initEndProcess(String taskId) throws Exception {
		ActivityImpl endActivity = this.findActivitiImpl(taskId, "end");
		this.initCommitProcess(taskId, null, endActivity.getId());
	}

	/**
	 * @param taskId
	 *            当前任务ID
	 * @param variables
	 *            流程变量
	 * @param activityId
	 *            流程转向执行任务节点ID<br>
	 *            此参数为空，默认为提交操作
	 * @throws Exception
	 */
	public void initCommitProcess(String taskId, Map<String, Object> variables,
			String activityId) throws Exception {
		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		// 跳转节点为空，默认提交操作
		if (CommonUtil.isNull(activityId)) {
			actTaskService.complete(taskId, variables);
		} else {// 流程转向操作
			this.initTurnTransition(taskId, activityId, variables);
		}
	}

	/**
	 * 流程转向操作
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            目标节点任务ID
	 * @param variables
	 *            流程变量
	 * @throws Exception
	 */
	private void initTurnTransition(String taskId, String activityId,
			Map<String, Object> variables) throws Exception {
		// 当前节点
		ActivityImpl currActivity = this.findActivitiImpl(taskId, null);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = this.initClearTransition(currActivity);

		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = this.findActivitiImpl(taskId, activityId);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);

		// 执行转向任务
		actTaskService.complete(taskId, variables);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);

		// 还原以前流向
		this.restoreTransition(currActivity, oriPvmTransitionList);
	}

	/**
	 * 清空指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
	private List<PvmTransition> initClearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl
				.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
	}

	/**
	 * 还原指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
	private void restoreTransition(ActivityImpl activityImpl,
			List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl
				.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}

	/**
	 * 删除个人为无效流程
	 * 
	 * @param listMyUselessSpFlowInstance
	 */
	public void delUnStartSpFlow(
			List<SpFlowInstance> listMyUselessSpFlowInstance) {
		// 在删除数据相关表后，删除activity相关引擎表数据
		if (null != listMyUselessSpFlowInstance
				&& !listMyUselessSpFlowInstance.isEmpty()) {
			for (SpFlowInstance spFlowInstance : listMyUselessSpFlowInstance) {
				if (null != spFlowInstance.getActInstaceId()
						&& !"".equals(spFlowInstance.getActInstaceId().trim())) {
					ProcessInstance pi = runtimeService
							.createProcessInstanceQuery()//
							.processInstanceId(
									spFlowInstance.getActInstaceId().trim())// 使用流程实例ID查询
							.singleResult();
					// 流程结束了
					if (pi == null) {
						historyService
								.deleteHistoricProcessInstance(spFlowInstance
										.getActInstaceId());
					} else {
						runtimeService.deleteProcessInstance(
								spFlowInstance.getActInstaceId(), "无效的。");
					}
				}
			}
		}

	}

	/**
	 * 使用流程实例ID查询
	 * 
	 * @param actInstaceId
	 * @return
	 */
	public ProcessInstance queryProcessInstance(String actInstaceId) {
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
				.processInstanceId(actInstaceId)// 使用流程实例ID查询
				.singleResult();
		return pi;
	}

	/**
	 * 持久化审批意见
	 * 
	 * @param taskId
	 * @param processInstanceId
	 * @param message
	 */
	public void addComment(String taskId, String processInstanceId,
			String message) {
		actTaskService.addComment(taskId, processInstanceId, message);
	}

	/**
	 * 获取当前操作任务步骤信息
	 * 
	 * @param actInstaceId
	 * @param userInfo
	 * @return
	 */
	public Task queryCurTask(String actInstaceId, UserInfo userInfo) {
		// 1、流程引擎向下走一步
		// 获取当前实例下的当前办理人办理的任务
		Task curTask = actTaskService.createTaskQuery()
				.processInstanceId(actInstaceId)
				.taskAssignee(userInfo.getComId() + ":" + userInfo.getId())
				.active().singleResult();
		return curTask;
	}
	/**
	 * 流程引擎向下走一步
	 * 
	 * @param actInstaceId
	 * @param userInfo
	 * @return
	 */
	public Task updateNextStepUser(String actInstaceId, String userId) {
		// 1、流程引擎向下走一步
		// 获取当前实例下的当前办理人办理的任务
		Task curTask = actTaskService.createTaskQuery()
				.processInstanceId(actInstaceId)
				.taskAssignee(userId)
				.active().singleResult();
		return curTask;
	}

	/**
	 * 回退步骤均执行activiti数据更新
	 * 
	 * @param actInstaceId
	 * @param activitiTaskId
	 * @return
	 */
	public Task updateBackStep(String actInstaceId, String activitiTaskId) {
		// 回退步骤均执行activiti数据更新
		Task pointTask = actTaskService.createTaskQuery()
				.processInstanceId(actInstaceId)
				.taskDefinitionKey(activitiTaskId).singleResult();
		return pointTask;
	}

	/**
	 * 获取当前实例化流程的执行任务集合
	 * 
	 * @param actInstaceId
	 * @return
	 */
	public List<Task> listNextTask(String actInstaceId) {
		// 获取当前实例化流程的执行任务集合
		List<Task> listNextTask = actTaskService.createTaskQuery()
				.processInstanceId(actInstaceId).active().list();
//		findMyPersonTask();
		return listNextTask;
	}
	
	public void findMyPersonTask(){  
		
		 List<IdentityLink> aa = actTaskService.getIdentityLinksForTask("352610");
		  
	     String assignee = "411867:130";  
	  
	     List<Task>taskList = actTaskService//获取任务service  
	  
	             .createTaskQuery()//创建查询对象  
	  
	             //.taskAssignee(assignee)//指定查询人  
	  
	             .taskCandidateUser(assignee)//参与者，组任务查询  
	  
	             .list();  
	  
	     if(taskList.size()>0){  
	  
	         for (Task task : taskList) {  
	  
	             System.out.println("代办任务ID:"+task.getId());  
	  
	             System.out.println("代办任务name:"+task.getName());  
	  
	             System.out.println("代办任务创建时间:"+task.getCreateTime());  
	  
	             System.out.println("代办任务办理人:"+task.getAssignee());  
	  
	             System.out.println("流程实例ID:"+task.getProcessInstanceId());  
	  
	             System.out.println("执行对象ID:"+task.getExecutionId());  
	  
	         }  
	  
	     }  
	  
	  }  

	/**
	 * activity提供的查询方式
	 * 
	 * @return
	 */
	public List<ProcessDefinition> listProcessDefinition() {
		return repositoryService.createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().desc().list();
	}

	/**
	 * 删除流程模型后，删除activity部署信息
	 * 
	 * @param delSpFlowModel
	 */
	public void delFlowMod(SpFlowModel delSpFlowModel) {
		if (null != delSpFlowModel.getAct_deployment_id()
				&& !"".equals(delSpFlowModel.getAct_deployment_id().trim())) {
			List<Deployment> listDep = repositoryService
					.createDeploymentQuery()
					.processDefinitionKey(delSpFlowModel.getAct_deployment_id())
					.list();
			if (null != listDep && !listDep.isEmpty()) {
				repositoryService
						.deleteDeployment(listDep.get(0).getId(), true);// 级联删除activity数据
			}
		}

	}

	/**
	 * 部署流程
	 * 
	 * @param flowName
	 * @param fileName
	 * @param flowBpmnXml
	 */
	public void createDeployment(String flowName, String fileName,
			String flowBpmnXml) {
		repositoryService.createDeployment().name(flowName)
				.addString(fileName, flowBpmnXml).deploy();
	}
	/**
	 * 转办流程
	 * 
	 * @param taskId
	 *            当前任务节点ID
	 * @param userCode
	 *            被转办人Code
	 */
	public void transferAssignee(String taskId, String userCode) {
		actTaskService.setAssignee(taskId, userCode);
	}

	/**
	 * 迭代循环流程树结构，查询当前节点可驳回的任务节点
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param currActivity
	 *            当前活动节点
	 * @param rtnList
	 *            存储回退节点集合
	 * @param tempList
	 *            临时存储节点集合（存储一次迭代过程中的同级userTask节点）
	 * @return 回退节点集合
	 */
	private List<ActivityImpl> iteratorBackActivity(String taskId,
			ActivityImpl currActivity, List<ActivityImpl> rtnList,
			List<ActivityImpl> tempList) throws Exception {
		// 查询流程定义，生成流程树结构
		ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);

		// 当前节点的流入来源
		List<PvmTransition> incomingTransitions = currActivity
				.getIncomingTransitions();
		// 条件分支节点集合，userTask节点遍历完毕，迭代遍历此集合，查询条件分支对应的userTask节点
		List<ActivityImpl> exclusiveGateways = new ArrayList<ActivityImpl>();
		// 并行节点集合，userTask节点遍历完毕，迭代遍历此集合，查询并行节点对应的userTask节点
		List<ActivityImpl> parallelGateways = new ArrayList<ActivityImpl>();
		// 遍历当前节点所有流入路径
		for (PvmTransition pvmTransition : incomingTransitions) {
			TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
			ActivityImpl activityImpl = transitionImpl.getSource();
			String type = (String) activityImpl.getProperty("type");
			/**
			 * 并行节点配置要求：<br>
			 * 必须成对出现，且要求分别配置节点ID为:XXX_start(开始)，XXX_end(结束)
			 */
			if ("parallelGateway".equals(type)) {// 并行路线
				String gatewayId = activityImpl.getId();
				String gatewayType = gatewayId.substring(gatewayId
						.lastIndexOf("_") + 1);
				if ("START".equals(gatewayType.toUpperCase())) {// 并行起点，停止递归
					return rtnList;
				} else {// 并行终点，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
					parallelGateways.add(activityImpl);
				}
			} else if ("startEvent".equals(type)) {// 开始节点，停止递归
				return rtnList;
			} else if ("userTask".equals(type)) {// 用户任务
				tempList.add(activityImpl);
			} else if ("exclusiveGateway".equals(type)) {// 分支路线，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
				currActivity = transitionImpl.getSource();
				exclusiveGateways.add(currActivity);
			}
		}

		/**
		 * 迭代条件分支集合，查询对应的userTask节点
		 */
		for (ActivityImpl activityImpl : exclusiveGateways) {
			iteratorBackActivity(taskId, activityImpl, rtnList, tempList);
		}

		/**
		 * 迭代并行集合，查询对应的userTask节点
		 */
		for (ActivityImpl activityImpl : parallelGateways) {
			iteratorBackActivity(taskId, activityImpl, rtnList, tempList);
		}

		/**
		 * 根据同级userTask集合，过滤最近发生的节点
		 */
		currActivity = filterNewestActivity(processInstance, tempList);
		if (currActivity != null) {
			// 查询当前节点的流向是否为并行终点，并获取并行起点ID
			String id = findParallelGatewayId(currActivity);
			if (StringUtils.isEmpty(id)) {// 并行起点ID为空，此节点流向不是并行终点，符合驳回条件，存储此节点
				rtnList.add(currActivity);
			} else {// 根据并行起点ID查询当前节点，然后迭代查询其对应的userTask任务节点
				currActivity = findActivitiImpl(taskId, id);
			}

			// 清空本次迭代临时集合
			tempList.clear();
			// 执行下次迭代
			iteratorBackActivity(taskId, currActivity, rtnList, tempList);
		}
		return rtnList;
	}

	/**
	 * 根据流入任务集合，查询最近一次的流入任务节点
	 * 
	 * @param processInstance
	 *            流程实例
	 * @param tempList
	 *            流入任务集合
	 * @return
	 */
	private ActivityImpl filterNewestActivity(ProcessInstance processInstance,
			List<ActivityImpl> tempList) {
		while (tempList.size() > 0) {
			ActivityImpl activity_1 = tempList.get(0);
			HistoricActivityInstance activityInstance_1 = findHistoricUserTask(
					processInstance, activity_1.getId());
			if (activityInstance_1 == null) {
				tempList.remove(activity_1);
				continue;
			}

			if (tempList.size() > 1) {
				ActivityImpl activity_2 = tempList.get(1);
				HistoricActivityInstance activityInstance_2 = findHistoricUserTask(
						processInstance, activity_2.getId());
				if (activityInstance_2 == null) {
					tempList.remove(activity_2);
					continue;
				}

				if (activityInstance_1.getEndTime().before(
						activityInstance_2.getEndTime())) {
					tempList.remove(activity_1);
				} else {
					tempList.remove(activity_2);
				}
			} else {
				break;
			}
		}
		if (tempList.size() > 0) {
			return tempList.get(0);
		}
		return null;
	}

	/**
	 * 查询指定任务节点的最新记录
	 * 
	 * @param processInstance
	 *            流程实例
	 * @param activityId
	 * @return
	 */
	private HistoricActivityInstance findHistoricUserTask(
			ProcessInstance processInstance, String activityId) {
		HistoricActivityInstance rtnVal = null;
		// 查询当前流程实例审批结束的历史节点
		List<HistoricActivityInstance> historicActivityInstances = historyService
				.createHistoricActivityInstanceQuery().activityType("userTask")
				.processInstanceId(processInstance.getId())
				.activityId(activityId).finished()
				.orderByHistoricActivityInstanceEndTime().desc().list();
		if (historicActivityInstances.size() > 0) {
			rtnVal = historicActivityInstances.get(0);
		}

		return rtnVal;
	}

	/**
	 * 根据任务ID获取对应的流程实例
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	private ProcessInstance findProcessInstanceByTaskId(String taskId)
			throws Exception {
		// 找到流程实例
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(findTaskById(taskId).getProcessInstanceId())
				.singleResult();
		if (processInstance == null) {
			throw new Exception("流程实例未找到!");
		}
		return processInstance;
	}

	/**
	 * 根据当前节点，查询输出流向是否为并行终点，如果为并行终点，则拼装对应的并行起点ID
	 * 
	 * @param activityImpl
	 *            当前节点
	 * @return
	 */
	private String findParallelGatewayId(ActivityImpl activityImpl) {
		List<PvmTransition> incomingTransitions = activityImpl
				.getOutgoingTransitions();
		for (PvmTransition pvmTransition : incomingTransitions) {
			TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
			activityImpl = transitionImpl.getDestination();
			String type = (String) activityImpl.getProperty("type");
			if ("parallelGateway".equals(type)) {// 并行路线
				String gatewayId = activityImpl.getId();
				String gatewayType = gatewayId.substring(gatewayId
						.lastIndexOf("_") + 1);
				if ("END".equals(gatewayType.toUpperCase())) {
					return gatewayId.substring(0, gatewayId.lastIndexOf("_"))
							+ "_start";
				}
			}
		}
		return null;
	}

	/**
	 * 获得任务中的办理候选人
	 * @param taskId
	 * @return
	 */
	public Set<String> listTaskCandidate(String taskId) {
		Set<String> users = new HashSet<String>();
		List<IdentityLink> identityLinkList = actTaskService.getIdentityLinksForTask(taskId);
		if (identityLinkList != null && !identityLinkList.isEmpty()) {
			for (IdentityLink identityLink : identityLinkList) {
				users.add(identityLink.getUserId());
			}
		}
		return users;
	}

	private User getUser(String userId) {
		User user = (User) identityService.createUserQuery().userId(userId)
				.singleResult();
		return user;
	}

	/**
	 * 拾取流程信息
	 * @param sessionUser 当前操作人员
	 * @param actInstanceId 
	 */
	public Map<String,Object> updatePickSpInstance(UserInfo sessionUser, String actInstanceId) {
		
		Map<String,Object> map = new HashMap<>();
		Task curTask = actTaskService.createTaskQuery()
				.processInstanceId(actInstanceId).active().singleResult();
		if(StringUtils.isEmpty(curTask.getAssignee())){
			String userId =CommonUtil.buildFlowExetutor(sessionUser.getComId(), sessionUser.getId());
			actTaskService.claim(curTask.getId(), userId);
			map.put("status", "y");
		}else{
			map.put("status", "f1");
			map.put("info", "流程已被拾取！");
		}
		return map;
	}

	/**
	 * 根据activities实例主键更改流程办理人员
	 * @param userInfo
	 * @param actInstanceId
	 * @param newAssignerId
	 * @return
	 */
	public Map<String, Object> updateSpInsAssignByProcessInstanceId(UserInfo sessionUser,String actInstanceId, Integer newAssignerId) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		Task curTask = actTaskService.createTaskQuery().processInstanceId(actInstanceId).active().singleResult();
		//原!StringUtils.isEmpty(curTask.getAssignee())
		if(!StringUtils.isEmpty(curTask)){
			String userId =CommonUtil.buildFlowExetutor(sessionUser.getComId(), newAssignerId);
			actTaskService.setAssignee(curTask.getId(), userId);
			map.put("status", "y");
		}else{
			map.put("status", "f1");
			map.put("info", "流程未处于办理状态，不能转办！");
		}
		return map;
	}
	
	/**
	 * 根据activiti的task更改流程办理人员
	 * @param userInfo
	 * @param curTask
	 * @param newAssignerId
	 * @return
	 */
	public Map<String, Object> updateSpInsAssignByTask(UserInfo sessionUser,Task curTask, Integer newAssignerId) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if(!StringUtils.isEmpty(curTask)){
			String userId =CommonUtil.buildFlowExetutor(sessionUser.getComId(), newAssignerId);
			actTaskService.setAssignee(curTask.getId(), userId);
			map.put("status", "y");
		}else{
			map.put("status", "f1");
			map.put("info", "流程未处于办理状态，不能转办！");
		}
		return map;
	}
	
	
	/**
	 * 流程引擎向下走一步
	 * 
	 * @param actInstaceId
	 * @param userInfo
	 * @return
	 */
	public Task queryCurTaskByTaskAssignee(String actInstaceId, UserInfo userInfo) {
		// 1、流程引擎向下走一步
		// 获取当前实例下的当前办理人办理的事项
		Task curTask = actTaskService.createTaskQuery()
				.processInstanceId(actInstaceId)
				.taskAssignee(userInfo.getComId() + ":" + userInfo.getId())
				.active().singleResult();
		return curTask;
	}
	
	/**
	 * 根据流程步骤主键获取activit对应的事项配置信息
	 * 
	 * @param taskId
	 *            activiti 事项主键
	 * @param stepId
	 *            流程步骤配置主键
	 * @return
	 * @throws Exception
	 */
	public ActivityImpl findActivitiImplByFlowStepId(String taskId,
			String stepId) throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);
		// 根据流程步骤主键，获取该流程实例的对应activiti配置步骤信息
		for (ActivityImpl activityImpl : processDefinition.getActivities()) {
			if (activityImpl.getId().split("_")[1].equals(stepId)) {
				return activityImpl;
			}
		}
		return null;
	}

	/**
	 * 查询当前步骤的事项信息
	 * @param actInstaceId
	 * @param userId
	 * @return
	 */
	public List<Task> queryCurTaskByProcessInstanceId(String actInstaceId) {
		// 1、流程引擎向下走一步
		// 获取当前实例下的当前办理人办理的事项
		List<Task> curTasklist = actTaskService.createTaskQuery()
				.processInstanceId(actInstaceId)
				.active().list();
		return curTasklist;
	}

	/**
	 * 拾取流程信息
	 * 
	 * @param sessionUser
	 *            当前操作人员
	 * @param actInstanceId
	 */
	public Map<String, Object> updatePickSpFlowTask(UserInfo sessionUser,
			String actInstanceId) {

		Map<String, Object> map = new HashMap<>();
		Task curTask = actTaskService.createTaskQuery()
				.processInstanceId(actInstanceId).active().singleResult();
		if (StringUtils.isEmpty(curTask.getAssignee())) {
			String userId = CommonUtil.buildFlowExetutor(
					sessionUser.getComId(), sessionUser.getId());
			actTaskService.claim(curTask.getId(), userId);
			map.put("status", "y");
			map.put("info", "拾取成功！");
		} else {
			map.put("status", "f1");
			map.put("info", "流程已被拾取！");
		}
		return map;
	}
	/**
	 * 检测当前activiti引擎任务是否还有子任务未完成
	 * 
	 * @param instance
	 *            流程实例对象
	 * @param curTask
	 *            activiti当前执行任务对象
	 * @return
	 */
	public boolean haveActSonTask(String actInstaceId, Task curTask) {
		List<Task> tasks = actTaskService.createTaskQuery()
				.processInstanceId(actInstaceId).list();
		Map<String, Task> sonActTask = new HashMap<String, Task>();
		for (Task task : tasks) {// 级联结束本节点发起的会签任务
			if (null != task.getParentTaskId()) {
				sonActTask.put(task.getParentTaskId(), task);
				break;
			}
		}
		if (!sonActTask.isEmpty() && sonActTask.containsKey(curTask.getId())) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 启动流程
	 * 
	 * @param userInfo
	 * @param flowModel
	 * @param instanceId
	 * @param act_deployment_id
	 *            // activity部署主键
	 * @return
	 */
	public String startActiviti(UserInfo userInfo, Integer instanceId,
			String act_deployment_id) {
		identityService.setAuthenticatedUserId(CommonUtil.buildFlowExetutor(
				userInfo.getComId(), userInfo.getId()));// 用来设置启动流程的人员ID，引擎自动把用户ID保存到activiti:initiator中

		Map<String, Object> variables = new HashMap<String, Object>();
		UserInfo leader = userInfoService.queryDirectLeaderInfo(userInfo);// 获取直属上级信息
		variables.put(
				"var_" + ConstantInterface.EXECUTOR_BY_DIRECT,
				null == leader ? null : CommonUtil.buildFlowExetutor(
						userInfo.getComId(), leader.getId()));// 直属上级
		variables.put(
				"var_" + ConstantInterface.EXECUTOR_BY_SELF,
				CommonUtil.buildFlowExetutor(userInfo.getComId(),
						userInfo.getId()));// 创建人自己

		Authentication.setAuthenticatedUserId(userInfo.getComId() + ":"
				+ userInfo.getId());
		// 启动流程引擎并关联业务key
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(act_deployment_id,
						instanceId.toString(), variables);

		return processInstance.getId();

	}
	
	
	/**
	 * 获取当前实例化流程的执行事项集合
	 * 
	 * @param actInstaceId
	 * @return
	 */
	public List<Task> listCurTask(String actInstaceId) {
		// 获取当前实例化流程的执行事项集合
		List<Task> listCurTask = actTaskService.createTaskQuery()
				.processInstanceId(actInstaceId).list();
		return listCurTask;
	}
	
	/**
	 * 流程引擎向下走一步
	 * 
	 * @param actInstaceId
	 * @param userInfo
	 * @return
	 */
	public Task queryCurStepCandiateTask(String actInstaceId, UserInfo userInfo) {
		// 1、流程引擎向下走一步
		// 获取当前实例下的当前办理人办理的事项
		Task curTask = actTaskService
				.createTaskQuery()
				.processInstanceId(actInstaceId)
				.taskCandidateUser(userInfo.getComId() + ":" + userInfo.getId())
				.active().singleResult();
		return curTask;
	}

	/**
	 * 会签
	 * @param taskId
	 * @param userCodes
	 * @throws Exception
	 */
	public void jointProcess(String taskId, List<String> userCodes) throws Exception {
		for (String userCode : userCodes) {  
			TaskEntity task = (TaskEntity) actTaskService.newTask(IdGenerator.get());  
			task.setAssignee(userCode);  
			task.setName(findTaskById(taskId).getName() + "-会签");  
			task.setProcessDefinitionId(findProcessDefinitionEntityByTaskId(taskId).getId());  
			task.setProcessInstanceId(findProcessInstanceByTaskId(taskId).getId());  
			task.setParentTaskId(taskId);  
			task.setDescription("jointProcess");  
			actTaskService.saveTask(task);
		}
	}

	/**
	 * 删除任务
	 * @param taskId
	 */
	public void deleteTask(String taskId) {
		actTaskService.deleteTask(taskId);
	}
	
	/**
	 * 根据activities实例主键获取会签任务集合
	 * @param actInstaceId
	 * @return
	 */
	public List<Task> querySonTaskList(String actInstaceId){
		// 查询本节点发起的会签任务
		return actTaskService.createTaskQuery().processInstanceId(actInstaceId).taskDescription("jointProcess").list(); 
	}
	/**
	 * 添加activiti任务记录日志
	 * @param assignee 操作人
	 * @param curTaskId 任务主键
	 * @param stepName 步骤名称
	 * @param processInstanceId activities实例主键
	 * @param message 操作日志描述
	 * @throws Exception
	 */
	public void addTaskForLogs(String assignee,String curTaskId,String stepName,String processInstanceId,
			String message) throws Exception {
		TaskEntity task = (TaskEntity) actTaskService.newTask(IdGenerator.get());  
		task.setAssignee(assignee);  
		task.setName(stepName);  
		task.setProcessDefinitionId(findProcessDefinitionEntityByTaskId(curTaskId).getId());  
		task.setProcessInstanceId(findProcessInstanceByTaskId(curTaskId).getId());  
		task.setParentTaskId(curTaskId);  
		task.setDescription("jointProcess");  
		actTaskService.saveTask(task);
		this.addComment(task.getId(),processInstanceId,message);
		this.initCommitProcess(task.getId(), null, null);
	}
}
