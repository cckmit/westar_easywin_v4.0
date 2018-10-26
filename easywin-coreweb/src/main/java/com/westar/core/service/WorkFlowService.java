package com.westar.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.westar.base.enums.ActLinkTypeEnum;
import com.westar.base.enums.LoanReportWayEnum;
import com.westar.base.enums.LoanWayEnum;
import com.westar.base.model.Attention;
import com.westar.base.model.BusAttrMapFormColTemp;
import com.westar.base.model.BusMapFlow;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.Consume;
import com.westar.base.model.EventPm;
import com.westar.base.model.FeeBudget;
import com.westar.base.model.FeeLoan;
import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.FeeLoanReport;
import com.westar.base.model.ForMeDo;
import com.westar.base.model.FormCompon;
import com.westar.base.model.FormLayout;
import com.westar.base.model.FormMod;
import com.westar.base.model.IssuePm;
import com.westar.base.model.Leave;
import com.westar.base.model.ModAdmin;
import com.westar.base.model.ModifyPm;
import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.OverTime;
import com.westar.base.model.PassYzm;
import com.westar.base.model.ReleasePm;
import com.westar.base.model.SpFlowCurExecutor;
import com.westar.base.model.SpFlowHiExecutor;
import com.westar.base.model.SpFlowHiStep;
import com.westar.base.model.SpFlowHiStepExecutor;
import com.westar.base.model.SpFlowHiStepRelation;
import com.westar.base.model.SpFlowHuiQianInfo;
import com.westar.base.model.SpFlowInputData;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.SpFlowModel;
import com.westar.base.model.SpFlowNoticeUser;
import com.westar.base.model.SpFlowOptData;
import com.westar.base.model.SpFlowRelateData;
import com.westar.base.model.SpFlowRunRelevanceCfg;
import com.westar.base.model.SpFlowRunStepFormControl;
import com.westar.base.model.SpFlowRunVariableKey;
import com.westar.base.model.SpFlowStep;
import com.westar.base.model.SpFlowStepConditions;
import com.westar.base.model.SpFlowTalk;
import com.westar.base.model.SpFlowTalkUpfile;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.SpFlowUsedTimes;
import com.westar.base.model.SpSerialNumRel;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FormData;
import com.westar.base.pojo.FormDataDetails;
import com.westar.base.pojo.FormDataOption;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.pojo.LayoutDetail;
import com.westar.base.pojo.ModFormStepData;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.WorkFlowData;
import com.westar.base.util.BeanRefUtil;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.WorkFlowDao;
import com.westar.core.thread.IndexUpdateThread;
import com.westar.core.thread.sendPhoneMsgThread;
import com.westar.core.web.PaginationContext;

/**
 * 
 * 描述: 审批流程业务逻辑处理
 * @author zzq
 * @date 2018年8月29日 上午10:30:59
 */
@Service
public class WorkFlowService {
	private static Logger log = Logger.getLogger(WorkFlowService.class);
	@Autowired
	WorkFlowDao workFlowDao;
	@Autowired
	ItemService itemService;
	@Autowired
	CusActivitService cusActivitService;
	@Autowired
	com.westar.core.service.FormService formService;
	@Autowired
	FlowDesignService flowDesignService;
	@Autowired
	TodayWorksService todayWorksService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	SystemLogService systemLogService;
	@Autowired
	AttentionService attentionService;
	@Autowired
	IndexService indexService;
	@Autowired
	RegistService registService;
	@Autowired
	FormColTempService formColTempService;
	@Autowired
	AdminCfgService adminCfgService;
	@Autowired
	PhoneMsgService phoneMsgService;
	@Autowired
	ForceInPersionService forceInService;
	@Autowired
	SerialNumService serialNumService;
	@Autowired
	ModFlowService modFlowService;
	@Autowired
	TaskService taskService;
	@Autowired
	UploadService uploadService;
	@Autowired
	FileCenterService fileCenterService;
	@Autowired
	JiFenService jifenService;
	@Autowired
	ModOptConfService modOptConfService;
	@Autowired
	ViewRecordService viewRecordService;
	@Autowired
	BusRelateSpFlowService busRelateSpFlowService;
	@Autowired
	ConsumeService consumeService;
	@Autowired
	ModAdminService modAdminService;

	/**
	 * 获取自己的待办审批审批
	 *
	 * @param userInfo
	 * @return
	 */
	public List<SpFlowInstance> listSpTodDo(UserInfo userInfo,
			SpFlowInstance instance) {
		/**
		 * 从引擎中获取待办审批
		 *
		 * //直接分配给当前人或已经签收的审批 List<Task> doingTask =
		 * actTaskService.createTaskQuery
		 * ().taskAssignee(userInfo.getComId()+":"+userInfo.getId()).list();
		 * //等待签收的审批 List<Task> waitingClmTask =
		 * actTaskService.createTaskQuery()
		 * .taskCandidateUser(userInfo.getComId()+":"+userInfo.getId()).list();
		 * //合并审批 List<Task> allTask = new ArrayList<Task>();
		 * allTask.addAll(doingTask); allTask.addAll(waitingClmTask);
		 */
		List<SpFlowInstance> listSp = workFlowDao.listSpTodDo(userInfo,
				instance);
//		if (null != listSp && !listSp.isEmpty()) {// 获取当前审批流程审批候选人数据集
//			for (SpFlowInstance sp : listSp) {
//				List<SpFlowCurExecutor> listSpFlowCurExecutor = workFlowDao
//						.listSpFlowCurExecutor(userInfo.getComId(), sp.getId());
//				sp.setListSpFlowCurExecutor(listSpFlowCurExecutor);
//			}
//		}
		return listSp;
	}

	/**
	 * 启动流程
	 *
	 * @param flowId
	 *            流程主键
	 * @return
	 */
	public SpFlowInstance initSpFlow(Integer flowId, UserInfo userInfo) {
		this.delUnStartSpFlow(userInfo);// 删除个人为无效流程
		FormMod formMod = null;// 表单配置
		Integer formKey = null;// 表单主键
		Integer layoutId = null;// 表单布局主键

		Integer formLayoutState = null;

		SpFlowModel flowModel = flowDesignService.querySpFlowModel(userInfo,flowId);// 获取流程基本信息
		if (StringUtils.isNotEmpty(flowModel.getFormKey())) {
			formKey = Integer.parseInt(flowModel.getFormKey());// 获取关联表单主键
			formMod = formService.getFormModVersion(formKey,
					userInfo.getComId());// 取得模板信息
			layoutId = formMod.getLayoutId();// 表单布局主键

			formLayoutState = formMod.getFormState();
		}
		// 固定流程
		SpFlowInstance spFlowInstance = new SpFlowInstance();// 实例化对象
		spFlowInstance.setFlowId(flowId);// 保存流程模型主键；激活流程时，启动审批过程
		spFlowInstance.setSonFlowId(flowModel.getSonFlowId());// 初始化关联子流程
		spFlowInstance.setComId(userInfo.getComId());// 团队号
		spFlowInstance.setCreator(userInfo.getId());// 发起人
		spFlowInstance.setFormKey(formKey);// 表单主键
		spFlowInstance.setFormVersion(null == formMod ? null : formMod.getVersion());// 表单模板版本
		spFlowInstance.setFlowState(ConstantInterface.SP_STATE_UNSTART);// 默认流程状态标识

		// 流程名称
		String flowName = CommonUtil.conStrFlowName(userInfo, flowModel);

		spFlowInstance.setFlowName(flowName);// 流程名称
		spFlowInstance.setFormModName(flowModel.getFlowName());// 使用的表单模板名称
		spFlowInstance.setSpState(ConstantInterface.SP_STATE_PASS);// 默认流程审批状态

		Integer instanceId = workFlowDao.add(spFlowInstance);// 添加实例化信息
		spFlowInstance.setId(instanceId);
		spFlowInstance.setRecordCreateTime(DateTimeUtil
				.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		spFlowInstance.setLayoutId(layoutId);
		spFlowInstance.setFormLayoutState(formLayoutState);

		workFlowDao.initSpFlowRunVariableKey(userInfo.getComId(), instanceId,flowId);// 拷贝流程变量；流程分支时用
		workFlowDao.initSpFlowRunStepFormControl(userInfo.getComId(),instanceId, flowId);// 拷贝流程步骤表单授权
		workFlowDao.initSpFlowHiStep(userInfo.getComId(), instanceId, flowId);// 拷贝流程实例步骤
		workFlowDao.initSpFlowStepConditions(userInfo.getComId(), instanceId, flowId);// 拷贝流程实例步骤条件

		workFlowDao.initSpFlowHiStepExecutor(userInfo.getComId(), instanceId, flowId);// 拷贝流程实例步骤

		workFlowDao.initSpFlowHiStepRelation(userInfo.getComId(), instanceId,flowId);// 流程实例步骤间关系表
		workFlowDao.initSpFlowRunRelevanceCfg(userInfo.getComId(), instanceId,flowId, flowModel.getSonFlowId());// 子流程实例化后配置表

		this.initSpFlowUsedTimes(userInfo, flowId);// 流程使用次数记录
		// 启动流程引擎
		String actDeploymentId = flowModel.getAct_deployment_id();// activity部署主键
		String actInstaceId = cusActivitService.startActiviti(userInfo,
				flowModel, instanceId, actDeploymentId);
		spFlowInstance.setActInstaceId(actInstaceId);// 引擎实例主键关联

		workFlowDao.update("update spFlowInstance a set a.actInstaceId=:actInstaceId where a.comid=:comId and a.id=:id",
						spFlowInstance);// 关联流程引擎主键
		String flowSerialNumber = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyyMMddHHmmss) + instanceId;// 生成流程实例流水号
		spFlowInstance.setFlowSerialNumber(flowSerialNumber);
		workFlowDao.update("update spFlowInstance a set a.flowSerialNumber=:flowSerialNumber where a.comid=:comId and a.id=:id",
						spFlowInstance);// 关联流程实例流水号

		return spFlowInstance;
	}

	/**
	 * 流程使用次数记录
	 *
	 * @param curuser
	 *            当前操作人
	 * @param flowId
	 *            流程主键
	 */
	private void initSpFlowUsedTimes(UserInfo curuser, Integer flowId) {
		SpFlowUsedTimes spFlowUsedTimes = workFlowDao.querySpFlowUsedTimes(
				curuser, flowId);
		if (null == spFlowUsedTimes) {
			spFlowUsedTimes = new SpFlowUsedTimes();
			spFlowUsedTimes.setComId(curuser.getComId());
			spFlowUsedTimes.setFlowId(flowId);
			spFlowUsedTimes.setUserId(curuser.getId());
			spFlowUsedTimes.setTimes(1);
			workFlowDao.add(spFlowUsedTimes);
		} else {
			spFlowUsedTimes.setTimes(spFlowUsedTimes.getTimes() + 1);
			workFlowDao
					.update("update spFlowUsedTimes a set a.times=:times where a.comid=:comId and a.id=:id",
							spFlowUsedTimes);
		}

	}

	/**
	 * 删除个人为无效流程
	 *
	 * @param userInfo
	 */
	public void delUnStartSpFlow(UserInfo userInfo) {
		List<SpFlowInstance> listMyUselessSpFlowInstance = workFlowDao.listMyUselessSpFlowInstance(userInfo);// 获取自己无效的流程数据集
		// 删除流程实例审批附件
		workFlowDao.delRelateTableWithBusId(SpFlowUpfile.class, userInfo);
		// 删除流程实例步骤间关系表
		workFlowDao.delRelateTableWithBusId(SpFlowHiStepRelation.class, userInfo);
		// 删除流程实例步骤
		workFlowDao.delRelateTableWithBusId(SpFlowHiStep.class, userInfo);
		// 删除流程实例步骤条件
		workFlowDao.delRelateTableWithBusId(SpFlowStepConditions.class, userInfo);
		//删除流程实例步骤审批人
		workFlowDao.delRelateTableWithBusId(SpFlowHiStepExecutor.class, userInfo);

		// 删除流程表单多数据
		workFlowDao.delRelateTableWithInsId(SpFlowOptData.class, userInfo);
		// 删除流程表单关联数据
		workFlowDao.delRelateTableWithInsId(SpFlowRelateData.class, userInfo);
		// 删除流程表单数据
		workFlowDao.delRelateTableWithInsId(SpFlowInputData.class, userInfo);

		//删除序列编号使用记录
		workFlowDao.delRelateTableWithBusId(SpSerialNumRel.class, userInfo);
		// 删除流程变量主键
		workFlowDao.delRelateTableWithBusId(SpFlowRunVariableKey.class, userInfo);
		//流程实例对象步骤授权
		workFlowDao.delRelateTableWithBusId(SpFlowRunStepFormControl.class, userInfo);
		// 删除流程当前审批人
		workFlowDao.delRelateTableWithBusId(SpFlowCurExecutor.class, userInfo);
		// 删除流程实例化对象表67
		workFlowDao.delRelateTableWithBusId(SpFlowRunRelevanceCfg.class, userInfo);
		// 删除流程实例与模块关联关系业务数据映射关系临时表
		workFlowDao.delRelateTableWithInsId(BusAttrMapFormColTemp.class, userInfo);


		//删除模块数据信息
		busRelateSpFlowService.delBusDataForNewSp(userInfo);

		workFlowDao.delSpFlowInstance(userInfo);// 删除流程实例化对象表


		// 删除个人为无效流程
		cusActivitService.delUnStartSpFlow(listMyUselessSpFlowInstance);

	}

	/**
	 * 发起审批
	 *
	 * @param formDataStr
	 *            数据对象字符串
	 * @param userInfo
	 *            当前操作人员
	 * @throws Exception
	 */
	public void addFormData(String formDataStr, UserInfo userInfo)
			throws Exception {
		// 获取流程表单数据
		FormData formData = json2FormData(formDataStr);

		// 获取实例化对象主键
		Integer instanceId = formData.getInstanceId();
		// 获取流程实例化对象信息
		SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(instanceId,userInfo);
		// 保存或修改数据
		// 修改审批表达关联和状态
		this.updateSpFlowRelateAndState(userInfo, formData, "add");

		// 提交的数据才修改
		String dataStatus = formData.getDataStatus();
		if (null != dataStatus && dataStatus.startsWith("submit")) {
			String nowDateTime = DateTimeUtil
					.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);

			if (CommonUtil.isNull(instance.getFlowId())) {
				List<SpFlowRunVariableKey> listSpFlowRunVariableKey = workFlowDao
						.listSpFlowRunVariableKey(userInfo.getComId(),
								instanceId);
				// 取得启动流程引擎并关联业务key
				String actInstaceId = cusActivitService.initActInstaceId(
						userInfo, formData, instanceId, instance,
						listSpFlowRunVariableKey);
				instance.setActInstaceId(actInstaceId);// 引擎实例主键关联
				instance.setRecordCreateTime(nowDateTime);
				workFlowDao
						.update("update spFlowInstance a set a.actInstaceId=:actInstaceId,recordCreateTime=:recordCreateTime where a.comid=:comId and a.id=:id",
								instance);
			} else {
				// 修改审批提交时间
				instance.setRecordCreateTime(nowDateTime);
				workFlowDao.update("update spFlowInstance a set recordCreateTime=:recordCreateTime where a.comid=:comId and a.id=:id",
								instance);
			}
			// 流程审批
			this.updateSpFlow(formDataStr, userInfo);
			// 添加审批实例索引
			this.updateSpFlowInstanceIndex(instanceId, userInfo, "add");
		} else if (null != dataStatus && dataStatus.startsWith("temp")) {
			// 获取开始步骤授权集合
			List<SpFlowRunStepFormControl> listCheckedFormCompon = this
					.listSpFlowRunStepFormControl(instance, userInfo, 0);
			// 授权空集合
			Set<Integer> authorFieldSet = new HashSet<Integer>();
			// 意见填充集合
			Set<Integer> fillField = new HashSet<Integer>();
			if (null != listCheckedFormCompon
					&& !listCheckedFormCompon.isEmpty()) {
				for (SpFlowRunStepFormControl spStepFormControl : listCheckedFormCompon) {
					// 需要授权的组件
					authorFieldSet.add(Integer.parseInt(spStepFormControl.getFormControlKey()));

					if (spStepFormControl.getIsFill() == 1) {
						// 需要填充意见的组件
						fillField.add(Integer.parseInt(spStepFormControl.getFormControlKey()));
					}
				}
			}
			// 保存或修改数据
			if (dataStatus.equalsIgnoreCase("tempCheckIn")) {
				initSaveFormData(userInfo, authorFieldSet, fillField, formData,null);// 表单数据保存
			} else {
				initSaveFormData(userInfo, authorFieldSet, fillField, formData,null);// 表单数据保存
			}

		}

		// 触发关联模块业务引擎
		if (!CommonUtil.isNull(formData.getBusType()) && null != dataStatus
				&& dataStatus.startsWith("submit")) {
			SpFlowInstance latestInstance = workFlowDao.getSpFlowInstanceById(instanceId, userInfo);// 获取流程实例化后，最新配置信息
			updatetBusEngineWhenFlowStart(latestInstance, userInfo);// 业务数据回掉初始化
		}

	}

	/**
	 * 流程审批
	 *
	 * @param formDataStr
	 *            表单数据对象
	 * @param userInfo
	 *            当前操作人信息
	 * @throws Exception
	 */
	public Map<String, Object> updateSpFlow(String formDataStr,
			UserInfo userInfo) throws Exception {
		FormData formData = json2FormData(formDataStr);// 获取流程表单数据
		// 验证码验证
		Map<String, Object> map = this.recheckSpYzm(formData, userInfo);
		if (map.get("status").equals("f")) {
			return map;
		}

		Integer instanceId = formData.getInstanceId();// 获取实例化对象主键
		SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(instanceId,userInfo);// 获取流程实例化对象信息
		// 1、流程引擎向下走一步
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(instance.getActInstaceId(), userInfo);
		if (null == curTask || null == curTask.getTaskDefinitionKey()) {// 如果流程步骤信息为NULL时，直接返回
			throw new IllegalArgumentException();
		}
		Integer curStepId = null;// 当前步骤主键声明

		String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
		if (actTaskDefinitionKey.contains("_")
				&& CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])) {
			curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);
		}

		// runtimeService.
		// 记录流程当前审批人
		SpFlowHiExecutor spFlowHiExecutor = new SpFlowHiExecutor();
		spFlowHiExecutor.setComId(userInfo.getComId());
		spFlowHiExecutor.setBusId(instanceId);
		spFlowHiExecutor.setBusType(ConstantInterface.TYPE_FLOW_SP);
		spFlowHiExecutor.setStepId(curStepId);
		spFlowHiExecutor.setExecutor(userInfo.getId());
		workFlowDao.add(spFlowHiExecutor);

		// 获取开始步骤授权集合
		List<SpFlowRunStepFormControl> listCheckedFormCompon = this
				.listSpFlowRunStepFormControl(instance, userInfo, curStepId);

		Set<Integer> authorFieldSet = new HashSet<Integer>();
		Set<Integer> fillField = new HashSet<Integer>();
		if (null != listCheckedFormCompon && !listCheckedFormCompon.isEmpty()) {
			for (SpFlowRunStepFormControl spStepFormControl : listCheckedFormCompon) {
				authorFieldSet.add(Integer.parseInt(spStepFormControl.getFormControlKey()));
				if (spStepFormControl.getIsFill() == 1) {
					fillField.add(Integer.parseInt(spStepFormControl.getFormControlKey()));
				}
			}
		}
		// 保存或修改数据
		this.updateSpFlowRelateAndState(userInfo, formData, "update");// 修改审批表达关联和状态

		//重置默认未销账
		List<SpFlowRelateData> preFlowRelateDatas = workFlowDao.listRelateDataForChangeState(instanceId, userInfo.getComId());
		if(null!=preFlowRelateDatas && !preFlowRelateDatas.isEmpty()){
			modFlowService.updateResetData(preFlowRelateDatas);

		}
		// 表单数据保存
		this.initSaveFormData(userInfo, authorFieldSet, fillField, formData,curStepId);

		List<SpFlowRunVariableKey> listSpFlowRunVariableKey = workFlowDao
				.listSpFlowRunVariableKey(userInfo.getComId(), instanceId);
		// 流程变量赋值
		Map<String, Object> variables = cusActivitService.initSpVariableMap(userInfo, instance, formData, listSpFlowRunVariableKey);


		//转办给指定人员
		List<UserInfo> userInfos = formData.getNextStepUsers();
		//指定人员的那种
		if(null!=userInfos && !userInfos.isEmpty()){
			variables.put("var_"+ConstantInterface.EXECUTOR_BY_APPOINT,
					CommonUtil.buildFlowExetutor(userInfo.getComId(),userInfos.get(0).getId()));
		}else{
			variables.put("var_"+ConstantInterface.EXECUTOR_BY_APPOINT,
					CommonUtil.buildFlowExetutor(userInfo.getComId(),userInfo.getId()));
		}
		// UserInfo leader =
		// userInfoService.queryDirectLeaderInfo(userInfo);//获取直属上级信息
		// variables.put("var_"+BusinessTypeConstant.EXECUTOR_BY_DIRECT,null==leader?null:CommonUtil.buildFlowExetutor(userInfo.getComId(),leader.getId()));//直属上级

		// 持久化审批意见
		String content = null == formData.getSpIdea() ? "": formData.getSpIdea();
		cusActivitService.addComment(curTask.getId(), instance.getActInstaceId(), content);

		// 审批状态 0 不同意 1同意
		Integer spState = formData.getSpState();
		if (null != spState && 0 == spState) {// 审批状态 0 不同意时
			//如果当前步骤存在会签，则需要删除会签信息
			this.delSPFlowHuiQian(instanceId,userInfo);
			cusActivitService.initEndProcess(curTask.getId());// 中止流程
			// 更新流程实例为结束状态4
			instance.setSpState(ConstantInterface.SP_STATE_REFUSE);
			workFlowDao.update("update spFlowInstance a set a.spState=:spState where a.comid=:comId and a.id=:id",instance);
		} else if (1 == spState) {
			//选中的步骤主键
			Integer nextStepId = formData.getNextStepId();
			if(null == nextStepId){
				cusActivitService.initCommitProcess(curTask.getId(), variables,null);//引擎事项扭转
			}else{
				ActivityImpl nextActivity = cusActivitService.findActivitiImplByFlowStepId(curTask.getId(),nextStepId.toString());
				if(null == nextActivity){
					cusActivitService.initCommitProcess(curTask.getId(), variables,null);//引擎事项扭转
				}else{
					cusActivitService.initCommitProcess(curTask.getId(), variables,nextActivity.getId());//引擎事项扭转
				}
			}
			//转办给指定人员
			if(null!=userInfos && userInfos.size()==1){
				Integer newAssignerId = userInfos.get(0).getId();
				// 获取当前实例化流程的执行审批集合
				List<Task> listNextTask = cusActivitService.listNextTask(instance.getActInstaceId());
				Set<String> userIds = cusActivitService.listTaskCandidate(listNextTask.get(0).getId());
				if(null!=userIds && !userIds.isEmpty()){
					String userComId = userIds.iterator().next();
					UserInfo executor = new UserInfo();
					executor.setId(Integer.parseInt(userComId.split(":")[1]));
					executor.setComId(userInfo.getComId());
					cusActivitService.updatePickSpInstance(executor, instance.getActInstaceId());
				}
				cusActivitService.updateSpInsAssignByProcessInstanceId(
						userInfo, instance.getActInstaceId(), newAssignerId);

			}
		}

		//依据人员删除当前流程审批人
		this.delSpFlowCurExecutor(userInfo.getComId(), instance.getId(),ConstantInterface.TYPE_FLOW_SP,userInfo.getId(),ActLinkTypeEnum.ASSIGNEE.getValue());
		//删除个人待办提醒工作
		todayWorksService.delTodayWorksByOwner(userInfo.getComId(),userInfo.getId(),instance.getId(),ConstantInterface.TYPE_FLOW_SP);


		/**
		 * 5：在完成审批之后，判断流程是否结束 如果流程结束了，更新请假单表的状态从1变成2（审核中-->审核完成）
		 */
		ProcessInstance pi = cusActivitService.queryProcessInstance(instance.getActInstaceId());
		// 流程结束了
		if (pi == null) {
			this.initEndSpFlow(userInfo, instance);// 判断流程是否结束;结束时，更新相关表数据、为创办人创建待办事项
			/***************** 更新业务数据 ******************/
			// 触发关联模块业务引擎//模块因为更新，需要根据流程状态作为依据，所以，只能放在倒数第二
			if (null != instance.getSpState()
					&& !CommonUtil.isNull(instance.getBusType())) {
				this.initBusEngineData(instance, userInfo);
			}
			//修改关联数据表信息
			this.updateRelateData(userInfo, instanceId, instance, curStepId);

			this.delSpFlowRunningTempData(userInfo.getComId(), instance.getId(),instance.getBusType());// 删除流程临时数据；用了才能删，所以你最后
		} else {
			String msgSendFlag = formData.getMsgSendFlag();//下一步骤是否需要发送短信
			// 获取当前实例化流程的执行审批集合
			List<Task> listNextTask = cusActivitService.listNextTask(instance.getActInstaceId());
			this.addSpFlowCurExecutor(listNextTask, userInfo, instance,formData.getNoticeUsers(),msgSendFlag);// 添加实例化流程审批人、并创建待办事项
			/***************** 更新业务数据 ******************/
			// 触发关联模块业务引擎//模块因为更新，需要根据流程状态作为依据，所以，只能放在倒数第二
			if (null != instance.getSpState()
					&& !CommonUtil.isNull(instance.getBusType())) {
				this.initBusEngineData(instance, userInfo);
			}
			//修改关联数据表信息
			this.updateRelateData(userInfo, instanceId, instance, curStepId);
		}
		return null;
	}

	/**
	 * 根据流程主键，删除会签信息
	 * @param instanceId
	 * @param userInfo
	 */
	private void delSPFlowHuiQian(Integer instanceId, UserInfo userInfo) {
		// 如果当前步骤存在会签，则需要删除会签信息
		List<SpFlowHuiQianInfo> listSpHuiQianProcess = this.listSpHuiQianProcess(instanceId, userInfo);
		if(!CommonUtil.isNull(listSpHuiQianProcess)) {
			for (SpFlowHuiQianInfo spFlowHuiQianInfo : listSpHuiQianProcess) {
				this.delHuiQian(spFlowHuiQianInfo.getActTaskId(), spFlowHuiQianInfo.getId());
			}
		}
	}

	/**
	 * @param userInfo
	 * @param instanceId
	 * @param instance
	 * @param curStepId
	 */
	private void updateRelateData(UserInfo userInfo, Integer instanceId,
			SpFlowInstance instance, Integer curStepId) {
		//删除附件关联信息
		workFlowDao.delByField("spFlowUpfile", new String[]{"comId","busId","busType","addType"},
				new Object[]{userInfo.getComId(),instance.getId(),ConstantInterface.TYPE_FLOW_SP,3});

		List<SpFlowRelateData> flowRelateDatas = workFlowDao.listRelateDataForChangeState(instanceId, userInfo.getComId());
		if(null!=flowRelateDatas && !flowRelateDatas.isEmpty()){
			//修改子表单关联数据信息
			modFlowService.updateRelateTableInfo(instance, userInfo ,flowRelateDatas,curStepId);
		}
	}

	/**
	 * 验证审批是否需要验证码
	 *
	 * @param formData
	 * @param userInfo
	 */
	private Map<String, Object> recheckSpYzm(FormData formData,
			UserInfo userInfo) {
		Map<String, Object> map = new HashMap<String, Object>(2);

		Integer instanceId = formData.getInstanceId();
		String actInstaceId = ((SpFlowInstance) workFlowDao.objectQuery(
				SpFlowInstance.class, instanceId)).getActInstaceId();
		SpFlowHiStep spFlowHiStep = flowDesignService.checkStepCfg(userInfo,
				actInstaceId, instanceId);
		if (null == spFlowHiStep) {
			map.put("status", "f");
			map.put("info", "步骤信息配置错误");
		} else if(formData.getSpState()!=1) {
			// 不需要配置
			map.put("status", "y");
		} else {
			if (!StringUtils.isEmpty(spFlowHiStep.getSpCheckCfg())
					&& spFlowHiStep.getSpCheckCfg().equals(
							ConstantInterface.SPSTEP_CHECK_YES)) {// 需要配置
				String account = userInfo.getMovePhone();

				PassYzm passYzm = registService.getPassYzm(account);
				if (null == passYzm) {
					map.put("status", "f");
					map.put("info", "验证码失效！");

				} else if (formData.getSpYzm().equalsIgnoreCase(
						passYzm.getPassYzm())) {
					map.put("status", "y");
					workFlowDao.delById(PassYzm.class, passYzm.getId());
				} else {
					map.put("status", "f");
					map.put("info", "验证码输入错误！");
					workFlowDao.delById(PassYzm.class, passYzm.getId());
				}
			} else {
				// 不需要配置
				map.put("status", "y");
			}
		}
		return map;
	}

	/**
	 * 流程回退
	 *
	 * @param formDataStr
	 *            表单数据对象
	 * @param userInfo
	 *            当前操作人信息
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> spFlowTurnBackTo(String formDataStr,
			UserInfo userInfo) throws Exception {
		FormData formData = json2FormData(formDataStr);// 获取流程表单数据

		// 验证码验证
		Map<String, Object> map = this.recheckSpYzm(formData, userInfo);
		if (map.get("status").equals("f")) {
			return map;
		}
		// 回退步骤主键
		String activitiTaskId = formData.getActivitiTaskId();
		if (null == activitiTaskId || "".equals(activitiTaskId.trim())) {
			throw new Exception("流程回退主键activitiTaskId：" + activitiTaskId);
		}

		// 获取实例化对象主键
		Integer instanceId = formData.getInstanceId();
		// 获取流程实例化对象信息
		SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(instanceId,userInfo);

		// 1、流程引擎向下走一步
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(instance.getActInstaceId(), userInfo);
		// 如果流程步骤信息为NULL时，直接返回
		if (null == curTask || null == curTask.getTaskDefinitionKey()) {
			throw new IllegalArgumentException();
		}
		Integer curStepId = null;// 当前步骤主键声明

		String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
		if (actTaskDefinitionKey.contains("_")
				&& CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])) {
			curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);
		}

		// runtimeService.
		// 记录流程当前审批人
		SpFlowHiExecutor spFlowHiExecutor = new SpFlowHiExecutor();
		spFlowHiExecutor.setComId(userInfo.getComId());
		spFlowHiExecutor.setBusId(instanceId);
		spFlowHiExecutor.setBusType(ConstantInterface.TYPE_FLOW_SP);
		spFlowHiExecutor.setStepId(curStepId);
		spFlowHiExecutor.setExecutor(userInfo.getId());
		workFlowDao.add(spFlowHiExecutor);

		// 获取开始步骤授权集合
		List<SpFlowRunStepFormControl> listCheckedFormCompon = new ArrayList<SpFlowRunStepFormControl>();
		listCheckedFormCompon = workFlowDao.listSpFlowRunStepFormControl(
				userInfo, instance.getId(), curStepId);

		Set<Integer> authorFieldSet = new HashSet<Integer>();
		Set<Integer> fillField = new HashSet<Integer>();
		if (null != listCheckedFormCompon && listCheckedFormCompon.size() > 0) {
			for (SpFlowRunStepFormControl spStepFormControl : listCheckedFormCompon) {
				authorFieldSet.add(Integer.parseInt(spStepFormControl
						.getFormControlKey()));
				if (spStepFormControl.getIsFill() == 1) {
					fillField.add(Integer.parseInt(spStepFormControl
							.getFormControlKey()));
				}
			}
		}
		// 保存或修改数据
		this.updateSpFlowRelateAndState(userInfo, formData, "update");// 修改审批表达关联和状态

		//重置默认未销账
		List<SpFlowRelateData> preFlowRelateDatas = workFlowDao.listRelateDataForChangeState(instanceId, userInfo.getComId());
		if(null!=preFlowRelateDatas && !preFlowRelateDatas.isEmpty()){
			modFlowService.updateResetData(preFlowRelateDatas);

		}
		// 表单数据保存
		this.initSaveFormData(userInfo, authorFieldSet, fillField, formData,curStepId);

		List<SpFlowRunVariableKey> listSpFlowRunVariableKey = workFlowDao
				.listSpFlowRunVariableKey(userInfo.getComId(), instanceId);
		// 流程变量赋值
		Map<String, Object> variables = cusActivitService.initSpVariableMap(
				userInfo, instance, formData, listSpFlowRunVariableKey);
		// UserInfo leader =
		// userInfoService.queryDirectLeaderInfo(userInfo);//获取直属上级信息
		// variables.put("var_"+BusinessTypeConstant.EXECUTOR_BY_DIRECT,null==leader?null:CommonUtil.buildFlowExetutor(userInfo.getComId(),leader.getId()));//直属上级

		//如果当前步骤存在会签，则需要删除会签信息
		this.delSPFlowHuiQian(instanceId,userInfo);
		// 持久化审批意见
		cusActivitService.addComment(curTask.getId(), instance.getActInstaceId(),
				null == formData.getSpIdea() ? "": formData.getSpIdea());

		cusActivitService.initCommitProcess(curTask.getId(), variables,activitiTaskId);// 回退到指定审批活动对象

//
		// 2、变更实例化流程当前办理人
		//依据人员删除当前流程审批人
		this.delSpFlowCurExecutor( userInfo.getComId(), instance.getId(),ConstantInterface.TYPE_FLOW_SP,userInfo.getId(),ActLinkTypeEnum.ASSIGNEE.getValue());
		/**
		 * 5：在完成审批之后，判断流程是否结束 如果流程结束了，更新请假单表的状态从1变成2（审核中-->审核完成）
		 */

		ProcessInstance pi = cusActivitService.queryProcessInstance(instance.getActInstaceId());
		// 流程结束了
		if (pi == null) {
			this.initEndSpFlow(userInfo, instance);// 判断流程是否结束;结束时，更新相关表数据
			//修改关联数据表信息
			this.updateRelateData(userInfo, instanceId, instance, curStepId);
		} else {
			this.addSpFlowCurExecutor(userInfo, instance, activitiTaskId);// 添加实例化流程审批人、并创建待办事项
			//修改关联数据表信息
			this.updateRelateData(userInfo, instanceId, instance, curStepId);
		}

		return null;

	}

	/**
	 * 判断流程是否结束;结束时，更新相关表数据
	 *
	 * @param userInfo
	 *            当前操作人信息
	 * @param instance
	 *            流程实例对象
	 * @throws Exception
	 */
	private void initEndSpFlow(UserInfo userInfo, SpFlowInstance instance)
			throws Exception {
		// 删除当前人待办信息
		List<UserInfo> curExecutors = new ArrayList<UserInfo>();
		curExecutors.add(userInfo);
		todayWorksService.addTodayWorks(userInfo, -1, instance.getId(),
				instance.getFlowName(), ConstantInterface.TYPE_FLOW_SP,
				curExecutors, null);

		// 向发起人发送消息
		List<UserInfo> creators = new ArrayList<UserInfo>();
		UserInfo creator = userInfoService.getUserBaseInfo(userInfo.getComId(),
				instance.getCreator());
		creators.add(creator);
		todayWorksService.addTodayWorks(userInfo, creator.getId(),
				instance.getId(), instance.getFlowName() + "已经完结！",
				ConstantInterface.TYPE_SP_END, creators, null);


		//判断是否为借款或者报销业务，通知财务人员完成借款、结算操作
		
		boolean finicalFlag = ConstantInterface.SP_STATE_PASS.equals(instance.getSpState()) 
				&& (ConstantInterface.TYPE_LOAN_TRIP.equals(instance.getBusType()) 
						|| ConstantInterface.TYPE_LOAN_DAYLY.equals(instance.getBusType())
						|| ConstantInterface.TYPE_LOANOFF_TRIP.equals(instance.getBusType())
						|| ConstantInterface.TYPE_LOANOFF_DAYLY.equals(instance.getBusType()));
		if(finicalFlag) {
			//取得财务模块的人员
			List<ModAdmin> listModAdmin = modAdminService.listModAdmin(userInfo.getComId(),ConstantInterface.TYPE_FINALCIAL_OFFICE);
			if(!CommonUtil.isNull(listModAdmin)) {
				List<UserInfo> noticeUsers = new ArrayList<UserInfo>();
				for (ModAdmin modAdmin : listModAdmin) {
					// 向发起人发送消息
					UserInfo user = userInfoService.getUserBaseInfo(userInfo.getComId(),
							modAdmin.getUserId());
					noticeUsers.add(user);
				}
				//添加待办
				todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_FINALCIAL_BALANCE, instance.getId(), instance.getFlowName()+ "已审批通过，请完成相关财务结算操作！", noticeUsers, userInfo.getId(), 1);
			}

		}


		// 更新流程实例为结束状态4
		instance.setFlowState(ConstantInterface.SP_STATE_FINISH);
		workFlowDao.update("update spFlowInstance a set a.flowState=:flowState where a.comid=:comId and a.id=:id",instance);

		SpFlowInstance latestInfo = workFlowDao.getSpFlowInstanceById(instance.getId(), userInfo);// 重新获取流程实例信息
		// 触发子流程
		if (null != latestInfo.getSpState()
				&& ConstantInterface.SP_STATE_PASS == latestInfo.getSpState() // 流程审批状态为通过
				&& null != latestInfo.getSonFlowId()
				&& latestInfo.getSonFlowId() > 0) {// 并有有效的子流程主键
			UserInfo newUser = new UserInfo();
			newUser.setComId(userInfo.getComId());
			newUser.setId(latestInfo.getCreator());
			this.initSonFlowInstance(newUser, latestInfo);// 实例化子流程
		}



	}

	/**
	 * 删除流程临时数据
	 *
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程主键
	 * @param bustype
	 */
	private void delSpFlowRunningTempData(Integer comId, Integer instanceId, String bustype) {
		/******************************** 以下删除临时数据 *****************************************/
		// 删除流程实例流程当前审批人
		workFlowDao.delByField("spFlowCurExecutor",
				new String[] { "comId","busId","busType" },
				new Object[] { comId, instanceId,ConstantInterface.TYPE_FLOW_SP });
		// 删除流程实例变量表
		workFlowDao.delByField("spFlowRunVariableKey",
				new String[] { "comId","busId","busType" },
				new Object[] { comId, instanceId,ConstantInterface.TYPE_FLOW_SP });
		// 删除流程实例条件
		workFlowDao.delByField("SPFLOWSTEPCONDITIONS",
				new String[] { "comId","busId","busType" },
				new Object[] { comId, instanceId,ConstantInterface.TYPE_FLOW_SP });
		// 删除流程实例拷贝模板的初始办理人员
		workFlowDao.delByField("SPFLOWHISTEPEXECUTOR",
				new String[] { "comId","busId","busType" },
				new Object[] { comId, instanceId,ConstantInterface.TYPE_FLOW_SP });
		// 删除流程实例对象步骤授权
		workFlowDao.delByField("spFlowRunStepFormControl",
				new String[] {"comId", "busId", "busType" },
				new Object[] { comId,instanceId, ConstantInterface.TYPE_FLOW_SP });
		// 删除子流程实例化后配置
		workFlowDao.delByField("spFlowRunRelevanceCfg",
				new String[] { "comId","busId","busType" },
				new Object[] { comId, instanceId,ConstantInterface.TYPE_FLOW_SP });
		//TODO 删除配置关系数据在点击确认后
		
		boolean relateModFlag = null!= bustype && !(
				ConstantInterface.TYPE_LOAN_DAYLY.equals(bustype)
				|| ConstantInterface.TYPE_LOAN_TRIP.equals(bustype)
				|| ConstantInterface.TYPE_LOANOFF_TRIP.equals(bustype)
				|| ConstantInterface.TYPE_LOANOFF_DAYLY.equals(bustype)
				);
		
		if(relateModFlag){
			// 删除流程实例与模块关联关系业务数据映射关系临时表
			workFlowDao.delByField("busAttrMapFormColTemp",
					new String[] { "comId","instanceId" },
					new Object[] { comId, instanceId });
		}
	}

	/**
	 * 实例化子流程
	 *
	 * @param userInfo
	 *            当前用户信息
	 * @param pSpFlowInstance
	 *            主流程实例信息
	 * @throws Exception
	 */
	private void initSonFlowInstance(UserInfo userInfo,
			SpFlowInstance pSpFlowInstance) throws Exception {
		UserInfo creatorInfo = userInfoService.getUserBaseInfo(
				userInfo.getComId(), pSpFlowInstance.getCreator());// 获取主流程创建人信息
		//前置的流程
		String busType = pSpFlowInstance.getBusType();

		SpFlowInstance sonFlowInstance = this.initSpFlow(pSpFlowInstance.getSonFlowId(), creatorInfo);// 以主流程创建人发起子流程
		// 建立主子实例流程关系
		pSpFlowInstance.setSonInstanceId(sonFlowInstance.getId());
		workFlowDao.update("update spFlowInstance a set a.sonInstanceId=:sonInstanceId where a.comid=:comId and a.id=:id",
				pSpFlowInstance);

		FormData sonFormData = null;
		List<SpFlowRunRelevanceCfg> listSpFlowRunRelevanceCfg = workFlowDao
				.listSpFlowRunRelevanceCfg(creatorInfo.getComId(),pSpFlowInstance.getId());// 获取子流程实例化后映射配置集合

		Map<String, String> map = new HashMap<String, String>(14);

		if (null != listSpFlowRunRelevanceCfg && !listSpFlowRunRelevanceCfg.isEmpty()) {
			for (SpFlowRunRelevanceCfg spFlowRunRelevanceCfg : listSpFlowRunRelevanceCfg) {
				map.put(spFlowRunRelevanceCfg.getFromFormControlKey(),
						spFlowRunRelevanceCfg.getToFormControlKey());
			}

		}

		if (ConstantInterface.TYPE_FEE_APPLY_DAYLY.equals(busType)) {//常规费用申请完成

			Integer busMapFlowId = null;
			List<BusMapFlow> listBusMapFlowByAuth = adminCfgService.listBusMapFlowByAuth(creatorInfo, ConstantInterface.TYPE_LOAN_DAYLY);
			if(null!=listBusMapFlowByAuth && !listBusMapFlowByAuth.isEmpty()){
				for (BusMapFlow busMapFlow : listBusMapFlowByAuth) {
					if(busMapFlow.getFlowId().equals(pSpFlowInstance.getSonFlowId())){
						busMapFlowId = busMapFlow.getId();
						break;
					}
				}
			}
			if(null!=busMapFlowId){
				//添加借款业务数据
				FeeLoan feeLoan = new FeeLoan();
				feeLoan.setComId(creatorInfo.getComId());
				feeLoan.setInstanceId(sonFlowInstance.getId());
				Integer applyId = pSpFlowInstance.getBusId();

				feeLoan.setFeeBudgetId(applyId);//借款申请记录主键
				feeLoan.setLoanMoney("0");//默认借款0元
				feeLoan.setBorrowingBalance("0");
				feeLoan.setCreator(creatorInfo.getId());
				feeLoan.setAllowedQuota("0");
				feeLoan.setStatus(ConstantInterface.COMMON_DEF);
				//设置
				feeLoan.setIsBusinessTrip(0);
				feeLoan.setInitStatus(0);
				Integer loanId = workFlowDao.add(feeLoan);

				if (null != listSpFlowRunRelevanceCfg && !listSpFlowRunRelevanceCfg.isEmpty()) {
					sonFormData = this.initSonLoanFormData(sonFlowInstance, map, creatorInfo,pSpFlowInstance,loanId,ConstantInterface.TYPE_LOAN_DAYLY);
				}
				//创建临时流程实例与模块关联关系业务数据映射关系临时表
				formColTempService.addBusAttrMapFormColTemp(busMapFlowId,sonFlowInstance.getId(),userInfo.getComId(),loanId,ConstantInterface.TYPE_LOAN_DAYLY);

			}


		}else if(ConstantInterface.TYPE_REPORT_TRIP.equals(busType)){//差旅汇报完成

			Integer busMapFlowId = null;
			List<BusMapFlow> listBusMapFlowByAuth = adminCfgService.listBusMapFlowByAuth(creatorInfo, ConstantInterface.TYPE_LOANOFF_TRIP);
			if(null!=listBusMapFlowByAuth && !listBusMapFlowByAuth.isEmpty()){
				for (BusMapFlow busMapFlow : listBusMapFlowByAuth) {
					if(busMapFlow.getFlowId().equals(pSpFlowInstance.getSonFlowId())){
						busMapFlowId = busMapFlow.getId();
						break;
					}
				}
			}

			if(null!=busMapFlowId){

				FeeLoanReport loanReport = (FeeLoanReport) workFlowDao.objectQuery(FeeLoanReport.class, pSpFlowInstance.getBusId());
				//添加出差业务数据
				FeeLoanOff loanOff = new FeeLoanOff();
				loanOff.setComId(userInfo.getComId());
				loanOff.setCreator(userInfo.getId());
				loanOff.setInstanceId(sonFlowInstance.getId());
				loanOff.setStatus(ConstantInterface.COMMON_DEF);
				loanOff.setLoanWay(LoanWayEnum.QUOTA.getValue());
				loanOff.setFeeBudgetId(loanReport.getFeeBudgetId());//费用申请主键
				loanOff.setLoanReportId(pSpFlowInstance.getBusId());//报销说明记录主键
				loanOff.setLoanReportWay(LoanReportWayEnum.AFTERREPORT.getValue());
				//是否为差旅报销
				Integer isBusinessTrip = ConstantInterface.TYPE_REPORT_TRIP.equals(busType)?1:0;
				loanOff.setIsBusinessTrip(isBusinessTrip);
				//报销金额
				loanOff.setLoanOffItemFee("0");
				//销账金额
				loanOff.setLoanOffBalance("0");

				Integer loanOffId = workFlowDao.add(loanOff);

				if (null != listSpFlowRunRelevanceCfg && !listSpFlowRunRelevanceCfg.isEmpty()) {
					sonFormData = this.initSonLoanFormData(sonFlowInstance, map, creatorInfo,pSpFlowInstance,loanOffId,ConstantInterface.TYPE_LOANOFF_TRIP);
				}
				//创建临时流程实例与模块关联关系业务数据映射关系临时表
				formColTempService.addBusAttrMapFormColTemp(busMapFlowId,sonFlowInstance.getId(),userInfo.getComId(),loanOffId,ConstantInterface.TYPE_LOANOFF_TRIP);
			}


		}else{
			if (null != listSpFlowRunRelevanceCfg && !listSpFlowRunRelevanceCfg.isEmpty()) {
				sonFormData = this.initSonFormData(sonFlowInstance, map, creatorInfo,pSpFlowInstance);
			}
		}
		if (null != sonFormData) {
			WorkFlowData workFlowData = new WorkFlowData();
			workFlowData.setClientSource("pc");
			workFlowData.setFormData(sonFormData);
			workFlowData.setModule("freeform");
			workFlowData.setIsDel(0);
			Gson gson = new Gson();
			addFormData(gson.toJson(workFlowData), creatorInfo);
		}

	}

	/**
	 * 初始化构建子表单
	 *
	 * @param sonFlowInstance
	 * @param mapCfg
	 * @param userInfo
	 * @param pFlowInstanceId
	 * @return
	 */
	private FormData initSonFormData(SpFlowInstance sonFlowInstance,
			Map<String, String> mapCfg, UserInfo userInfo,
			SpFlowInstance pFlowInstanceId) {
		// 表单数据
		FormData formData = new FormData();
		Integer instanceId = sonFlowInstance.getId();
		// 设置表单数据主键
		formData.setInstanceId(instanceId);

		// 表单布局
		FormLayout formLayout = new FormLayout();
		Integer layoutId = sonFlowInstance.getLayoutId();
		// 表单布局主键
		formLayout.setId(layoutId);
		formData.setFormLayout(formLayout);

		// 表单模板
		FormMod form = new FormMod();
		Integer formKey = sonFlowInstance.getFormKey();
		// 模板主键
		form.setId(formKey);
		formData.setForm(form);

		// 表单数据详情
		List<FormDataDetails> dataDetails = this.getSonFormDataDetails(
				instanceId, layoutId, formKey, userInfo, mapCfg,
				pFlowInstanceId.getId());
		formData.setDataDetails(dataDetails);

		formData.setBusId(0);
		formData.setBusType("0");
		formData.setBusName("");
		formData.setStagedItemId(0);
		formData.setPreStageItemId(0);
		formData.setStagedItemName("");
		formData.setDataStatus("submit");
		formData.setCreator(pFlowInstanceId.getCreator());
		// 固定流程需要的
		formData.setFlowId(sonFlowInstance.getFlowId());

		// 发起流程默认审批通过
		formData.setSpState(1);
		return formData;
	}
	private FormData initSonLoanFormData(SpFlowInstance sonFlowInstance,
			Map<String, String> mapCfg, UserInfo userInfo,
			SpFlowInstance pFlowInstanceId, Integer busId, String busType) {
		// 表单数据
		FormData formData = new FormData();
		Integer instanceId = sonFlowInstance.getId();
		// 设置表单数据主键
		formData.setInstanceId(instanceId);

		// 表单布局
		FormLayout formLayout = new FormLayout();
		Integer layoutId = sonFlowInstance.getLayoutId();
		// 表单布局主键
		formLayout.setId(layoutId);
		formData.setFormLayout(formLayout);

		// 表单模板
		FormMod form = new FormMod();
		Integer formKey = sonFlowInstance.getFormKey();
		// 模板主键
		form.setId(formKey);
		formData.setForm(form);

		// 表单数据详情
		List<FormDataDetails> dataDetails = this.getSonFormDataDetails(
				instanceId, layoutId, formKey, userInfo, mapCfg,
				pFlowInstanceId.getId());
		formData.setDataDetails(dataDetails);

		formData.setBusId(busId);
		formData.setBusType(busType);
		formData.setBusName("");
		formData.setStagedItemId(0);
		formData.setPreStageItemId(0);
		formData.setStagedItemName("");
		formData.setDataStatus("tempCheckIn");
		formData.setCreator(pFlowInstanceId.getCreator());
		// 固定流程需要的
		formData.setFlowId(sonFlowInstance.getFlowId());

		// 发起流程默认审批通过
		formData.setSpState(1);
		return formData;
	}

	/**
	 * 取得子流程数据
	 *
	 * @param sonInstanceId
	 *            子流程实例化主键
	 * @param layoutId
	 *            子流程布局主键
	 * @param formKey
	 *            子流程关联的
	 * @param userInfo
	 * @param pFlowInstanceId
	 * @return
	 */
	private List<FormDataDetails> getSonFormDataDetails(Integer sonInstanceId,
			Integer layoutId, Integer formKey, UserInfo userInfo,
			Map<String, String> mapCfg, Integer pFlowInstanceId) {

		List<FormDataDetails> dataDetails = new ArrayList<FormDataDetails>();

		List<SpFlowInputData> spFlowInputDatas = workFlowDao.listSpFormDatas(
				pFlowInstanceId, userInfo.getComId());

		if (null != spFlowInputDatas && !spFlowInputDatas.isEmpty()) {
			// 构建表单数据详情
			for (SpFlowInputData spFlowInputData : spFlowInputDatas) {

				String componentKey = spFlowInputData.getComponentKey();
				// 默认布不继续执行
				boolean flag = false;
				// 组件字段标识
				Integer pFieldId = spFlowInputData.getFieldId();
				String sonFieldId = mapCfg.get(pFieldId.toString());

				if (componentKey.equals("DateComponent")
						|| componentKey.equals("TextArea")
						|| componentKey.equals("Employee")
						|| componentKey.equals("Department")
						|| componentKey.equals("RelateMod")
						|| componentKey.equals("RelateItem")
						|| componentKey.equals("RelateCrm")
						|| componentKey.equals("NumberComponent")
						|| componentKey.equals("Text")
						|| componentKey.equals("SerialNum")
						|| componentKey.equals("Monitor")) {
					if (!StringUtils.isEmpty(sonFieldId)) {// 没有指定的类型
						flag = true;
					}
				}
				if (!flag) {//可以继续执行
					continue;
				}
				// 表单数据详情
				FormDataDetails formDataDetails = new FormDataDetails();
				// 动态数据行号
				formDataDetails.setDataIndex(spFlowInputData.getDataIndex());
				//动态表单行关联的数据表
				formDataDetails.setBusTableId(spFlowInputData.getBusTableId());
				formDataDetails.setBusTableType(spFlowInputData.getBusTableType());

				// 数据内容
				formDataDetails.setContent(spFlowInputData.getContent());

				// 表单数据对象
				FormDataOption formField = new FormDataOption();

				Integer fieldId = Integer.parseInt(sonFieldId);
				formField.setId(fieldId);
				formDataDetails.setFormField(formField);
				formField.setComponentKey(spFlowInputData.getComponentKey());

				FormDataOption dataText = new FormDataOption();

				if ("TextArea".equals(componentKey)) {// 构建文本框数据
					List<SpFlowOptData> spFlowOptDatas = workFlowDao.listSpFormOpts(pFlowInstanceId, spFlowInputData.getId(), userInfo.getComId());
					if (null != spFlowOptDatas && spFlowOptDatas.size() > 0) {
						dataText.setContent(spFlowOptDatas.get(0).getContent());
						formDataDetails.setDataText(dataText);
					}
				}else if("Employee".equals(componentKey)
						|| "Department".equals(componentKey)
						|| "RelateMod".equals(componentKey)
						|| "RelateItem".equals(componentKey)
						|| "RelateCrm".equals(componentKey)){
					List<SpFlowOptData> spFlowOptDatas = workFlowDao.listSpFormOpts(pFlowInstanceId, spFlowInputData.getId(), userInfo.getComId());
					if (null != spFlowOptDatas && !spFlowOptDatas.isEmpty()) {
						List<FormDataOption> dataOptions = new ArrayList<FormDataOption>();
						for (SpFlowOptData spFlowOptData : spFlowOptDatas) {
							FormDataOption formDataOption = new FormDataOption();
							formDataOption.setOptionId(spFlowOptData.getOptionId());
							formDataOption.setContent(spFlowOptData.getContent());
							dataOptions.add(formDataOption);
						}
						formDataDetails.setDataOptions(dataOptions);
					}


				}
				dataDetails.add(formDataDetails);

			}
		}

		return dataDetails;
	}

	/**
	 * 添加实例化流程审批人、并创建待办事项
	 * @param listNextTask
	 * @param userInfo
	 * @param instance
	 * @param noticeUsers
	 * @param msgSendFlag
	 */
	private void addSpFlowCurExecutor(List<Task> listNextTask,UserInfo userInfo,
			 SpFlowInstance instance,List<SpFlowNoticeUser> noticeUsers,String msgSendFlag) {
		Set<Integer> exectors = new HashSet<>();

		if (null != listNextTask && !listNextTask.isEmpty()) {
			for (Task nextTask : listNextTask) {
//				String taskDefinitionKey = nextTask.getTaskDefinitionKey();

				// 下一步骤的候选人
				String nextTaskAssignee = nextTask.getAssignee();
				if (StringUtils.isNotEmpty(nextTaskAssignee)) {// 有下一步执行人
					if (userInfo.getComId().toString()
							.equals(nextTask.getAssignee().split(":")[0].toString())) {
						Integer executor = Integer.parseInt(nextTask.getAssignee().split(":")[1]);
						// 验证办理人是否设置了工作代理；如果设置了，则由代理完成工作审批
						ForMeDo forMeDo = userInfoService.queryFnialForMeDo(
								userInfo.getComId(), executor);
						if (!CommonUtil.isNull(forMeDo)) {
							executor = this.updateActParams(userInfo,
									instance.getActInstaceId(), executor,forMeDo.getUserId());// 更新activiti相关参数信息
						}

						exectors.add(executor);

						// 设置流程执行人
						SpFlowCurExecutor spFlowCurExecutor = new SpFlowCurExecutor();
						spFlowCurExecutor.setComId(userInfo.getComId());
						spFlowCurExecutor.setBusId(instance.getId());
						spFlowCurExecutor.setBusType(ConstantInterface.TYPE_FLOW_SP);
						spFlowCurExecutor.setExecutor(executor);
						spFlowCurExecutor.setExecuteType(ActLinkTypeEnum.ASSIGNEE.getValue());
						workFlowDao.add(spFlowCurExecutor);
						// 创建待办事项
						List<UserInfo> shares = new ArrayList<UserInfo>();
						UserInfo share = userInfoService.getUserBaseInfo(
								userInfo.getComId(), executor);
						shares.add(share);
						// 先删除所有待办事项，添加普通提醒通知
						todayWorksService.addTodayWorks(userInfo, executor,
								instance.getId(), instance.getFlowName(),
								ConstantInterface.TYPE_FLOW_SP, shares, null);
						//需要设置短信
						if(!StringUtils.isEmpty(msgSendFlag)
								&& msgSendFlag.equals(ConstantInterface.MSG_SEND_YES)){
							if(!userInfo.getId().equals(executor)){
								//单线程池
								ExecutorService pool = ThreadPoolExecutor.getInstance();
								//跟范围人员发送通知消息
								pool.execute(new sendPhoneMsgThread(phoneMsgService, userInfo.getComId(), shares,
										new Object[]{instance.getFlowName()}, ConstantInterface.MSG_JOB_TO_DO,userInfo.getOptIP()));
							}
						}

					}

				} else {
					Set<String> userIds = cusActivitService.listTaskCandidate(nextTask.getId());
					for (String candidateUser : userIds) {
						Integer candidateUserId = Integer.parseInt(candidateUser.split(":")[1]);

						// 验证办理人是否设置了工作代理；如果设置了，则由代理完成工作审批
						ForMeDo forMeDo = userInfoService.queryFnialForMeDo(
								userInfo.getComId(), candidateUserId);
						if (!CommonUtil.isNull(forMeDo)) {
							candidateUserId = this.updateActParams(userInfo,
									instance.getActInstaceId(),
									candidateUserId, forMeDo.getUserId());//更新activiti相关参数信息
						}
						// 设置流程执行人
						SpFlowCurExecutor spFlowCurExecutor = new SpFlowCurExecutor();
						spFlowCurExecutor.setComId(userInfo.getComId());
						spFlowCurExecutor.setBusId(instance.getId());
						spFlowCurExecutor.setBusType(ConstantInterface.TYPE_FLOW_SP);
						spFlowCurExecutor.setExecutor(candidateUserId);
						spFlowCurExecutor.setExecuteType(ActLinkTypeEnum.CANDIDATE.getValue());
						workFlowDao.add(spFlowCurExecutor);
						// 创建待办事项
						List<UserInfo> shares = new ArrayList<UserInfo>();
						UserInfo share = userInfoService.getUserBaseInfo(
								userInfo.getComId(), candidateUserId);
						shares.add(share);
					}

				}
			}
		}

		modFlowService.addSpFlowNoticeUser(userInfo, instance.getId(), noticeUsers, msgSendFlag,
				exectors,ConstantInterface.TYPE_FLOW_SP,instance.getFlowName());
	}

	/**
	 * 更新activiti相关参数信息
	 *
	 * @param userInfo
	 * @param actInstaceId
	 * @param oldExecutor
	 * @param newExecutor
	 * @return
	 */
	private Integer updateActParams(UserInfo userInfo, String actInstaceId,
			Integer oldExecutor, Integer newExecutor) {
		Task curTask = cusActivitService.updateNextStepUser(actInstaceId,
				userInfo.getComId() + ":" + oldExecutor);
		if (null == curTask || null == curTask.getTaskDefinitionKey()) {// 如果流程步骤信息为NULL时，直接返回
			throw new IllegalArgumentException();
		}
		Integer executor = newExecutor;
		String actTaskId = curTask.getId();
		String assignee = userInfo.getComId() + ":" + executor;
		workFlowDao.update_act_ru_task(actTaskId, actInstaceId, assignee);
		workFlowDao.update_act_hi_taskinst(actTaskId, actInstaceId, assignee);
		workFlowDao.update_act_hi_actinst(actTaskId, actInstaceId, assignee);
		return executor;
	}

	/**
	 * 为返回步骤创建相关待办工作信息
	 *
	 * @param userInfo
	 *            当前 操作人信息
	 * @param instance
	 *            流程实例对象
	 * @param activitiTaskId
	 *            部署文件中审批活动主键
	 * @throws Exception
	 */
	private void addSpFlowCurExecutor(UserInfo userInfo,
			SpFlowInstance instance, String activitiTaskId) throws Exception {
		// 回退步骤均执行activiti数据更新
		Task pointTask = cusActivitService.updateBackStep(instance.getActInstaceId(), activitiTaskId);
		if (null == pointTask || null == pointTask.getTaskDefinitionKey()) {// 如果流程步骤信息为NULL时，直接返回
			throw new IllegalArgumentException();
		}
		//获取默认设置
		Integer executor = CommonUtil.isNull(pointTask.getAssignee())?0:(Integer.parseInt(pointTask.getAssignee().split(":")[1]));

		Integer curStepId = 0;
		String actTaskDefinitionKey = pointTask.getTaskDefinitionKey();
		if (actTaskDefinitionKey.contains("_")
				&& CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])) {
			curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);

			//取得上次最近执行人
			List<SpFlowHiExecutor> executors = workFlowDao.listSpFlowHiExecutorForBack(
					userInfo.getComId(), instance.getId(), curStepId);
			SpFlowHiExecutor flowHiExecutor = null;
			if(null != executors && !executors.isEmpty()){
				flowHiExecutor = executors.get(0);
			}
			if(null!=flowHiExecutor && !flowHiExecutor.getExecutor().equals(executor)){
				executor = flowHiExecutor.getExecutor();
				cusActivitService.updateSpInsAssignByProcessInstanceId(
						userInfo, instance.getActInstaceId(), flowHiExecutor.getExecutor());
			}
		}



		// 验证办理人是否设置了工作代理；如果设置了，则由代理完成工作审批
		ForMeDo forMeDo = userInfoService.queryFnialForMeDo(userInfo.getComId(), executor);
		if (!CommonUtil.isNull(forMeDo)) {
			executor = updateActParams(userInfo, instance.getActInstaceId(),
					executor, forMeDo.getUserId());// 更新activiti相关参数信息
		}
		// 设置流程执行人
		SpFlowCurExecutor spFlowCurExecutor = new SpFlowCurExecutor();
		spFlowCurExecutor.setComId(userInfo.getComId());
		spFlowCurExecutor.setBusId(instance.getId());
		spFlowCurExecutor.setBusType(ConstantInterface.TYPE_FLOW_SP);
		spFlowCurExecutor.setExecutor(executor);
		spFlowCurExecutor.setExecuteType(ActLinkTypeEnum.ASSIGNEE.getValue());
		workFlowDao.add(spFlowCurExecutor);
		// 创建待办事项
		List<UserInfo> shares = new ArrayList<UserInfo>();
		UserInfo share = userInfoService.getUserBaseInfo(userInfo.getComId(),executor);
		shares.add(share);
		// 先删除所有待办事项，添加普通提醒通知
		todayWorksService.addTodayWorks(userInfo, executor, instance.getId(),
				instance.getFlowName(), ConstantInterface.TYPE_FLOW_SP, shares,null);
	}

	/**
	 * 把表单数据JSON字符串转化成FormData数据对象
	 *
	 * @param formDataStr
	 * @return
	 */
	private FormData json2FormData(String formDataStr) {
		JSONObject layoutDetailObj = JSONObject.parseObject(formDataStr);
		WorkFlowData workFlowData = (WorkFlowData) com.alibaba.fastjson.JSONObject
				.toJavaObject(layoutDetailObj, WorkFlowData.class);
		FormData formData = workFlowData.getFormData();// 取得流程表单数据对象
		formData.setClientSource(workFlowData.getClientSource());
		// JSONObject layoutDetailObj = JSONObject.fromObject(formDataStr);
		// @SuppressWarnings("rawtypes")
		// Map<String, Class> classMap = new HashMap<String, Class>();
		// classMap.put("formData", FormData.class);
		// classMap.put("dataDetails", FormDataDetails.class);
		// classMap.put("dataOptions", FormDataOption.class);
		// classMap.put("formField", FormDataOption.class);
		// classMap.put("subForm", FormDataOption.class);
		// classMap.put("form", FormMod.class);
		// classMap.put("formLayout", FormLayout.class);
		// classMap.put("subFormLogs", FormDataOption.class);
		// classMap.put("spFlowUpfiles", SpFlowUpfile.class);
		// classMap.put("jointProcessUsers", UserInfo.class);
		// classMap.put("nextStepUsers", UserInfo.class);
		// classMap.put("noticeUsers", SpFlowNoticeUser.class);
		// WorkFlowData workFlowData = (WorkFlowData)
		// JSONObject.toBean(layoutDetailObj,WorkFlowData.class,classMap);
		return formData;
	}

	/**
	 * 表单数据保存
	 *
	 * @param userInfo
	 *            当前操作人信息
	 * @param authorFieldSet
	 *            表单权限
	 * @param formData
	 *            表单数据
	 */
	private void initSaveFormData(UserInfo userInfo,
			Set<Integer> authorFieldSet, Set<Integer> fillField,
			FormData formData, Integer actUserTaskId) {
		Integer comId = userInfo.getComId();// 团队主键
		Integer instanceId = formData.getInstanceId();// 流程实例化主键
		Integer formModId = formData.getForm().getId();// 表单模板主键
		Integer layoutId = formData.getFormLayout().getId();// 表单布局主键
		List<FormDataDetails> formDataDetails = formData.getDataDetails();
		List<FormDataOption> subFormLogs = formData.getSubFormLogs();

		if (null != subFormLogs && !subFormLogs.isEmpty()) {

			FormLayout formLayout = (FormLayout) workFlowDao.objectQuery(
					FormLayout.class, layoutId);
			// 是否替换过
			Integer formState = formLayout.getFormState();
			if (null != formState && formState == 1) {
				for (FormDataOption subFormLog : subFormLogs) {

					Integer subFormId = subFormLog.getSubFormId();

					FormCompon dateFormCompon = formService.getFormComponById(subFormId);

					Integer dataFieldId = dateFormCompon.getFieldId();

					List<FormCompon> listFormCompon = formService.listTreeCompon(formModId, layoutId, userInfo,subFormId);

					Boolean autheState = false;

					if (null != listFormCompon && !listFormCompon.isEmpty()) {
						for (FormCompon formCompon : listFormCompon) {
							// 组件标识
							Integer fieldId = formCompon.getFieldId();
							if (authorFieldSet.contains(fieldId)) {
								autheState = true;
								break;
							}
						}
					}

					if (authorFieldSet.contains(dataFieldId) || autheState) {// 有权限修改数据
						if (null != listFormCompon && !listFormCompon.isEmpty()) {
							for (FormCompon formCompon : listFormCompon) {
								// 组件标识
								Integer fieldId = formCompon.getFieldId();
								if (!authorFieldSet.contains(fieldId)) {
									continue;
								}
								// 根据权限存取数据
								List<SpFlowInputData> spFlowInputDatas = workFlowDao
										.getSpFormDatas(comId, instanceId,
												fieldId);
								if (null != spFlowInputDatas
										&& !spFlowInputDatas.isEmpty()) {
									for (SpFlowInputData spFlowInputData : spFlowInputDatas) {
										Integer formFlowDataId = spFlowInputData.getId();
										//删除多行数据
										workFlowDao.delByField("spFlowOptData",
												new String[] { "comId","instanceId","formFlowDataId" },
												new Object[] { comId,instanceId,formFlowDataId });
										//删除关联数据
										workFlowDao.delByField("spFlowRelateData",
												new String[] { "comId","instanceId","formFlowDataId" },
												new Object[] { comId,instanceId,formFlowDataId });
										//删除单行数据
										workFlowDao.delById(SpFlowInputData.class,formFlowDataId);
									}
								}
							}
						}
					}

				}
			} else {
				for (FormDataOption subFormLog : subFormLogs) {

					Integer subFormId = subFormLog.getSubFormId();

					FormCompon dateFormCompon = formService.getFormComponById(subFormId);

					Integer dataFieldId = dateFormCompon.getFieldId();
					if (authorFieldSet.contains(dataFieldId)) {// 有权限修改数据
						Integer removeRowNumber = subFormLog.getRemoveRowNumber();
						// 删除该子表的所有数据
						List<FormCompon> listFormCompon = formService
								.listTreeCompon(formModId, layoutId, userInfo,
										subFormId);
						if (null != listFormCompon && listFormCompon.size() > 0) {
							for (FormCompon formCompon : listFormCompon) {
								// 组件标识
								Integer fieldId = formCompon.getFieldId();
								authorFieldSet.add(fieldId);
								if (null != removeRowNumber
										&& removeRowNumber > 0) {// 有删除数据
									// 根据权限存取数据
									List<SpFlowInputData> spFlowInputDatas = workFlowDao
											.getSpFormDatas(comId, instanceId,fieldId);
									if (null != spFlowInputDatas
											&& !spFlowInputDatas.isEmpty()) {
										for (SpFlowInputData spFlowInputData : spFlowInputDatas) {
											Integer formFlowDataId = spFlowInputData.getId();

											workFlowDao.delByField("spFlowOptData",
													new String[] { "comId","instanceId","formFlowDataId" },
													new Object[] { comId,instanceId,formFlowDataId });
											//删除关联数据
											workFlowDao.delByField("spFlowRelateData",
													new String[] { "comId","instanceId","formFlowDataId" },
													new Object[] { comId,instanceId,formFlowDataId });

											workFlowDao.delById(SpFlowInputData.class,formFlowDataId);
										}
									}
								}
							}
						}
					}

				}
			}
		}
		FormLayout formLayout = null;
		if (null != formDataDetails && !formDataDetails.isEmpty()) {
			Map<Integer, Integer> pComponeFieldIdMap = new HashMap<Integer, Integer>(14);
			for (FormDataDetails formDataDetail : formDataDetails) {
				FormDataOption formField = formDataDetail.getFormField();
				// 组件类型
				String componentKey = formField.getComponentKey();
				// 组件标识
				Integer fieldId = formField.getId();

				Integer pComponeFieldId = 0;
				if (componentKey.equals("DateInterval")) {// 是日期区间
					if (pComponeFieldIdMap.containsKey(fieldId)) {
						pComponeFieldId = pComponeFieldIdMap.get(fieldId);
					} else {
						FormCompon formCompone = formService.getParentCompon(
								comId, formModId, layoutId, fieldId);
						pComponeFieldId = formCompone.getFieldId();
					}
				} else if (componentKey.equals("RelateFile")) {// 附件
					List<FormDataOption> dataOptions = formDataDetail
							.getDataOptions();
					// 添加附件信息
					this.addDataOptionUpfile(userInfo, dataOptions,
							actUserTaskId, instanceId,fieldId);
				} else if (componentKey.equalsIgnoreCase("Monitor")//计算控件需要全部授权
						|| componentKey.equalsIgnoreCase("MoneyComponent")){//大写金额控件需要全部授权
					authorFieldSet.add(fieldId);
				}
				// 该组件没有填写权限
				if (!authorFieldSet.contains(fieldId)
						&& !authorFieldSet.contains(pComponeFieldId)) {
					continue;
				}
				// 本次填写的内容
				String content = formDataDetail.getContent();
				// 上次填写的内容
				String oldContent = formDataDetail.getOldContent();
				// 动态表单数据索引
				Integer dataIndex = formDataDetail.getDataIndex();
				//动态表单行关联的数据表
				Integer busTableId = formDataDetail.getBusTableId();
				String busTableType = formDataDetail.getBusTableType();

				// 流程表单数据主键
				Integer formFlowDataId = 0;
				// 取得该组件的数据库信息
				SpFlowInputData spFlowInputData = workFlowDao
						.getSpFlowInputFieldData(comId, instanceId, fieldId,
								dataIndex);
				if (null != spFlowInputData) {// 原有数据
					// 重置流程表单数据主键
					formFlowDataId = spFlowInputData.getId();
					oldContent = StringUtil.delNull(oldContent);
					content = StringUtil.delNull(content);
					if (!oldContent.equals(content)) {// 两次的数据不同，修改数据
						spFlowInputData.setContent(content);
						workFlowDao.update(spFlowInputData);
					}
				} else {
					// 流程表单数据，用于添加
					spFlowInputData = new SpFlowInputData();
					// 设置团队号
					spFlowInputData.setComId(comId);
					// 设置流程实例化主键
					spFlowInputData.setInstanceId(instanceId);
					// 设置组件标识
					spFlowInputData.setFieldId(fieldId);
					// 设置组件类型
					spFlowInputData.setComponentKey(componentKey);
					// 设置数据内容
					spFlowInputData.setContent(content);
					// 设置动态表单的行号
					spFlowInputData.setDataIndex(dataIndex);
					// 添加表达数据
					formFlowDataId = workFlowDao.add(spFlowInputData);
				}
				// 删除上次流程表单关联数据
				workFlowDao.delByField("spFlowRelateData", new String[] { "comId",
						"instanceId", "formFlowDataId" }, new Object[] { comId,
						instanceId, formFlowDataId });
				// 删除上次的选项数据以及文本框
				workFlowDao.delByField("spFlowOptData", new String[] { "comId",
						"instanceId", "formFlowDataId" }, new Object[] { comId,
						instanceId, formFlowDataId });
				//修改序列编号关联信息
				if(componentKey.equalsIgnoreCase("SerialNum")){
					serialNumService.updateSerialNum(userInfo,instanceId,ConstantInterface.TYPE_FLOW_SP,content);
				}

				//重新设置关联关系
				if(null != busTableId && StringUtils.isNotEmpty(busTableType)){
					SpFlowRelateData spFlowRelateData = new SpFlowRelateData();
					// 设置单位号
					spFlowRelateData.setComId(comId);
					// 设置流程实例化主键
					spFlowRelateData.setInstanceId(instanceId);
					// 设置关联的数据
					spFlowRelateData.setFormFlowDataId(formFlowDataId);
					spFlowRelateData.setBusType(busTableType);
					spFlowRelateData.setBusId(busTableId);;
							//动态表单行关联的数据表
					workFlowDao.add(spFlowRelateData);

				}

				// 文本域信息
				FormDataOption dataText = formDataDetail.getDataText();
				if (null != dataText) {
					// 附件内容信息
					String dataTextStr = dataText.getContent();

					dataTextStr = StringUtils.isEmpty(dataTextStr) ? ""
							: dataTextStr;

					// 临时解决审批意见填入
					String clientSource = formData.getClientSource();
					if (null != clientSource && clientSource.equals("mobile")
							&& componentKey.equals("TextArea")) {
						if (null == formLayout && layoutId > 0) {
							formLayout = (FormLayout) workFlowDao.objectQuery(
									FormLayout.class, layoutId);
						}
						if (null != formLayout
								&& null != formLayout.getFormState()
								&& formLayout.getFormState() == 1
								&& authorFieldSet.contains(fieldId)) {
							String spIdea = formData.getSpIdea();
							if (null != spIdea && fillField.contains(fieldId)) {
								if (dataTextStr == null || dataTextStr == "") {
									dataTextStr = dataTextStr + spIdea;
								} else {
									dataTextStr = dataTextStr + "\r\n" + spIdea;
								}
							}
						}

					}

					// 文本域内容
					StringBuffer dataTextContent = new StringBuffer(dataTextStr);
					// 文本域对象
					SpFlowOptData spFlowTextAreaData = new SpFlowOptData();
					// 设置单位号
					spFlowTextAreaData.setComId(comId);
					// 非选项，没有ID
					spFlowTextAreaData.setOptionId(0);
					// 设置流程实例化主键
					spFlowTextAreaData.setInstanceId(instanceId);
					// 设置关联的数据
					spFlowTextAreaData.setFormFlowDataId(formFlowDataId);
					// 设置内容
					spFlowTextAreaData.setContentMore(dataTextContent
							.toString());
					// 设置文本valueType
					spFlowTextAreaData.setValType("1");
					// 保存表单的文本框
					workFlowDao.add(spFlowTextAreaData);
				}

				// 选项信息
				List<FormDataOption> formDataOptions = formDataDetail
						.getDataOptions();
				if (null != formDataOptions && !formDataOptions.isEmpty()) {// 存的数据为多个
					for (FormDataOption formDataOption : formDataOptions) {
						// 选项标识
						Integer optionId = formDataOption.getOptionId();
						// 选项内容
						String optContent = formDataOption.getContent();
						// 选项对象
						SpFlowOptData spFlowOptData = new SpFlowOptData();
						// 设置团队号
						spFlowOptData.setComId(comId);
						// 设置流程实例化主键
						spFlowOptData.setInstanceId(instanceId);
						// 设置关联数据主键
						spFlowOptData.setFormFlowDataId(formFlowDataId);
						// 设置选项标识
						spFlowOptData.setOptionId(optionId);
						// 设置选项内容
						spFlowOptData.setContent(optContent);
						//关联模块类型
						spFlowOptData.setDataBustype(formDataOption.getDataBustype());

						spFlowOptData.setValType("0");
						// 保存表单选中的选项数据
						workFlowDao.add(spFlowOptData);
					}

				}
			}
		}
		List<SpFlowUpfile> spFlowUpfiles = formData.getSpFlowUpfiles();
		// 添加附件信息
		this.addSpFlowUpfile(userInfo, spFlowUpfiles, actUserTaskId, instanceId);
	}

	/**
	 * 添加表单内附件信息
	 *
	 * @param userInfo
	 * @param dataOptions
	 * @param actUserTaskId
	 * @param instanceId
	 */
	public void addDataOptionUpfile(UserInfo userInfo,
			List<FormDataOption> dataOptions, Integer actUserTaskId,
			Integer instanceId,Integer formColKey) {
		ArrayList<Integer> upfileIds = new ArrayList<>();
		for (FormDataOption formDataOption : dataOptions) {
			SpFlowUpfile spFlowUpfile = workFlowDao.getOptionUpfile(userInfo,
					instanceId, formDataOption.getOptionId(),formColKey);
			if (CommonUtil.isNull(spFlowUpfile)) {
				SpFlowUpfile spFlowUpfileT = new SpFlowUpfile();
				spFlowUpfileT.setUpfileId(formDataOption.getOptionId());
				spFlowUpfileT.setFilename(formDataOption.getContent());
				// 设置团队号
				spFlowUpfileT.setComId(userInfo.getComId());
				// 设置流程实例化主键
				spFlowUpfileT.setBusId(instanceId);
				spFlowUpfileT.setBusType(ConstantInterface.TYPE_FLOW_SP);
				// 附件上传人
				spFlowUpfileT.setUserId(userInfo.getId());
				// 步骤节点标识
				spFlowUpfileT.setActUserTaskId(actUserTaskId == null ? ""
						: actUserTaskId.toString());
				spFlowUpfileT.setAddType(1);
				spFlowUpfileT.setFormColKey(formColKey);
				workFlowDao.add(spFlowUpfileT);
			}
			upfileIds.add(formDataOption.getOptionId());
		}
		
		//文件归档
		SpFlowInstance spFlowInstance = (SpFlowInstance) workFlowDao.objectQuery(SpFlowInstance.class, instanceId);
		fileCenterService.addModFile(userInfo, upfileIds, spFlowInstance.getFlowName());
		
		// 删除表单已删除的附件
		workFlowDao.delOptionUpfiles(userInfo, instanceId, upfileIds.toArray(),formColKey);
	}

	/**
	 * 添加步骤信息
	 *
	 * @param userInfo
	 *            当前操作员
	 * @param actUserTaskId
	 * @param instanceId
	 */
	private void addSpFlowUpfile(UserInfo userInfo,
			List<SpFlowUpfile> spFlowUpfiles, Integer actUserTaskId,
			Integer instanceId) {
		// 流程实例化附件信息
		if (null != spFlowUpfiles && !spFlowUpfiles.isEmpty()) {
			for (SpFlowUpfile spFlowUpfile : spFlowUpfiles) {
				// 设置团队号
				spFlowUpfile.setComId(userInfo.getComId());
				// 设置流程实例化主键
				spFlowUpfile.setBusId(instanceId);
				spFlowUpfile.setBusType(ConstantInterface.TYPE_FLOW_SP);
				// 附件上传人
				spFlowUpfile.setUserId(userInfo.getId());
				// 步骤节点标识
				spFlowUpfile.setActUserTaskId(actUserTaskId == null ? ""
						: actUserTaskId.toString());
				spFlowUpfile.setAddType(0);
				workFlowDao.add(spFlowUpfile);
			}
		}
	}

	/**
	 * 修改审批表达关联和状态
	 *
	 * @param userInfo
	 * @param formData
	 * @param saveType
	 *            保存类型 "add" 与"update"
	 */
	private void updateSpFlowRelateAndState(UserInfo userInfo,
			FormData formData, String saveType) {
		// 提交状态
		String dataStatus = formData.getDataStatus();
		// 流程表单主键
		Integer instanceId = formData.getInstanceId();
		// 流程实例化对象表
		SpFlowInstance spFlowInstance = new SpFlowInstance();
		// 审批关联类型
		String busType = formData.getBusType();
		Integer creator = formData.getCreator();

		// 是否需要修改
		Boolean flag = false;
		// 当前操作人是流程表单创建人
		if (userInfo.getId().equals(creator)) {
			// 删除项目阶段数据
			workFlowDao.delByField("stagedInfo", new String[] { "comId",
					"moduleId", "moduleType" },
					new Object[] { userInfo.getComId(), instanceId,
							ConstantInterface.STAGED_FLOW_SP });

			if (null != busType && !"".equals(busType) && !"0".equals(busType)) {// 有审批关联
				Integer busId = formData.getBusId();
				if (null != busId && busId > 0) {// 设置关联类型和关联模块主键
					// 设置关联审批
					spFlowInstance.setBusType(busType);
					spFlowInstance.setBusId(busId);
					flag = true;
					if (ConstantInterface.TYPE_ITEM.equals(busType)) {// 若是项目，则需要设置关联的项目阶段
						// 本次关联项目阶段主键
						Integer stagedItemId = formData.getStagedItemId();
						if (null == stagedItemId || stagedItemId == 0) {// 若是本次关联项目阶段主键为空，则使用最近一次项目阶段
							// 取得最近
							stagedItemId = itemService.getStageItemId(userInfo,
									busId);
						}
						// 项目阶段添加审批关联
						itemService.addStageRelateMod(userInfo, busId,
								stagedItemId, instanceId,
								ConstantInterface.TYPE_FLOW_SP);
					}
				}
			} else {// 没有关联审批模块
				spFlowInstance.setBusType("");
				spFlowInstance.setBusId(0);
				flag = true;
			}
		}
		if (null != saveType && "add".equals(saveType)) {// 是第一步
			spFlowInstance.setFlowName(formData.getInstanceName());
			// 删除所有附件
			workFlowDao.delByField("spFlowUpfile", new String[] { "comId","userId", "busId","busType" },
					new Object[] { userInfo.getComId(), userInfo.getId(),instanceId,ConstantInterface.TYPE_FLOW_SP});
			if (null != dataStatus && dataStatus.startsWith("submit")) {
				spFlowInstance.setFlowState(ConstantInterface.SP_STATE_EXE);
				// 标识关注
				if (null != formData.getAttentionState()
						&& formData.getAttentionState().equals(ConstantInterface.ATTENTION_STATE_YES)) {
					Attention atten = attentionService.getAtten(busType,instanceId, userInfo);
					if (null == atten) {
						attentionService.addAtten(ConstantInterface.TYPE_FLOW_SP, instanceId,userInfo);
					}
				} else {
					workFlowDao.delByField("attention", new String[] { "comId","userId", "busType", "busId" },
							new Object[] {userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_FLOW_SP, instanceId });
				}
			} else {
				spFlowInstance.setFlowState(ConstantInterface.SP_STATE_SAVE);

				workFlowDao.delByField("attention", new String[] { "comId","userId", "busType", "busId" },
						new Object[] { userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_FLOW_SP, instanceId });
			}
			flag = true;

		}
		if (flag) {
			// 设置流程表单主键
			spFlowInstance.setId(instanceId);
			workFlowDao.update(spFlowInstance);
		}

	}

	/**
	 * 分页查询流程表单
	 *
	 * @param userInfo
	 * @param spFlowInstance
	 * @return
	 */
	public List<SpFlowInstance> listPagedWorkFlow(UserInfo userInfo,
			SpFlowInstance spFlowInstance) {
		return workFlowDao.listPagedWorkFlow(userInfo, spFlowInstance);
	}

	/**
	 * 取得实例化对象数据信息
	 *
	 * @param instanceId
	 *            //实例化对象主键
	 * @param userInfo
	 * @return
	 */
	public SpFlowInstance getSpFlowInstanceById(Integer instanceId,
			UserInfo userInfo) {
		SpFlowInstance spFlowInstance = workFlowDao.getSpFlowInstanceById(instanceId, userInfo);

		//设置总审批数和文档数
		List<SpFlowUpfile> upfiles = workFlowDao.listSpFiles(instanceId, userInfo);
		if(upfiles != null && upfiles.size() > 0) {
			spFlowInstance.setUpfileNum(upfiles.size());
		}
		com.westar.base.model.Task task = new com.westar.base.model.Task();
		task.setBusType(ConstantInterface.TYPE_FLOW_SP);
		task.setBusId(instanceId);
		Integer taskNum = taskService.countTasks(task,userInfo);
		spFlowInstance.setTaskNum(taskNum);
		//审批留言
		Integer spFlowTalkNum = workFlowDao.spFlowTalkCount(instanceId,userInfo.getComId());
		spFlowInstance.setSpFlowTalkNum(spFlowTalkNum);
		//会签集合
		Integer huiQianNum = workFlowDao.spHuiQianProcessCount(instanceId,userInfo);
		spFlowInstance.setHuiQianNum(huiQianNum);

		//根据流程实例主键，查询当前操作人是否是审核人员（审批人、会签人）
		SpFlowCurExecutor spFlowCurExecutor = workFlowDao.getSpFlowCurExecutor(userInfo,spFlowInstance.getId());
		spFlowInstance.setExecutor(CommonUtil.isNull(spFlowCurExecutor)?0:spFlowCurExecutor.getExecutor());
		spFlowInstance.setExecuteType(CommonUtil.isNull(spFlowCurExecutor)?null:spFlowCurExecutor.getExecuteType());
		if(CommonUtil.isNull(spFlowCurExecutor)) {//如果为NULL，则表示当前查看人不是审批人，直接删除待办工作提醒
			todayWorksService.delTodayWorksByOwner(userInfo.getComId(),userInfo.getId(),
					spFlowInstance.getId(),ConstantInterface.TYPE_FLOW_SP);
		}
		return spFlowInstance;
	}

	/**
	 * 取得步骤权限
	 *
	 * @param spFlowInstance
	 *            流程实例
	 * @param userInfo
	 *            当前操作人员
	 * @param stepId
	 *            步骤信息
	 * @return
	 */
	public List<SpFlowRunStepFormControl> listSpFlowRunStepFormControl(
			SpFlowInstance spFlowInstance, UserInfo userInfo, Integer stepId) {
		List<SpFlowRunStepFormControl> listSpFlowRunStepFormControl = new ArrayList<SpFlowRunStepFormControl>();
		if (null != stepId && stepId == 0) {
			listSpFlowRunStepFormControl = workFlowDao.listSpFlowRunStepFormControl(userInfo,spFlowInstance.getId(), 0);
		} else {
			List<SpFlowCurExecutor> listSpFlowCurExecutor =
					workFlowDao.listSpFlowCurExecutor(userInfo.getComId(),spFlowInstance.getId());
			if (null != listSpFlowCurExecutor
					&& listSpFlowCurExecutor.size() == 1) {
				if (!listSpFlowCurExecutor.get(0).getExecutor().equals(userInfo.getId())) {// 不是当前操作人员
					return listSpFlowRunStepFormControl;
				}
			} else {
				// TODO 有多个执行人
			}
			// 获取当前activity引擎步骤
			Task curTask = cusActivitService.queryCurTaskByTaskAssignee(
					spFlowInstance.getActInstaceId(), userInfo);
			if (null != curTask && null != curTask.getTaskDefinitionKey()) {
				String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
				if (actTaskDefinitionKey.contains("_")
						&& CommonUtil
								.isNumeric(actTaskDefinitionKey.split("_")[1])) {
					stepId = Integer
							.parseInt(actTaskDefinitionKey.split("_")[1]);
				}
				listSpFlowRunStepFormControl = workFlowDao
						.listSpFlowRunStepFormControl(userInfo,
								spFlowInstance.getId(), stepId);
			}
		}
		return listSpFlowRunStepFormControl;
	}

	/**
	 * 取得表单数据
	 *
	 * @param spFlowInstance
	 * @param instanceId
	 *            //实例化对象主键
	 * @param layoutId
	 * @param userInfo
	 * @return
	 */
	public Map<String, Object> findFormData(SpFlowInstance spFlowInstance,
			Integer instanceId, Integer layoutId, UserInfo userInfo) {
		Map<String, Object> map = new HashMap<String, Object>(2);

		if (null == spFlowInstance) {
			spFlowInstance = workFlowDao.getSpFlowInstanceById(instanceId,
					userInfo);
		}
		if (CommonUtil.isNull(spFlowInstance.getFlowId())) {

		}
		// 取得表单布局，组件以及配置
		FormLayout formLayout = formService.getWorkFlowComp(
				spFlowInstance.getFormKey(), spFlowInstance.getLayoutId(),
				instanceId, userInfo);
		map.put("formLayout", formLayout);

		// 取得表单数据
		FormData formData = this.getWorkFlowData(instanceId, layoutId,
				spFlowInstance.getFormKey(), userInfo);
		map.put("formData", formData);
		return map;
	}

	/**
	 * 取得表单数据
	 *
	 * @param spFlowInstance
	 * @param instanceId
	 *            //实例化对象主键
	 * @param layoutId
	 * @param userInfo
	 * @return
	 */
	public Map<String, Object> findFormDataDev(SpFlowInstance spFlowInstance,
			Integer instanceId, Integer layoutId, UserInfo userInfo) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		if (null == spFlowInstance) {
			spFlowInstance = workFlowDao.getSpFlowInstanceById(instanceId,
					userInfo);
		}
		if (CommonUtil.isNull(spFlowInstance.getFlowId())) {

		}
		// 取得表单布局，组件以及配置
		FormLayout formLayout = formService.queryWorkFlowCompDev(
				spFlowInstance.getFormKey(), spFlowInstance.getLayoutId(),
				instanceId, userInfo);
		map.put("formLayout", formLayout);
		// 取得表单数据
		FormData formData = this.getWorkFlowData(instanceId, layoutId,
				spFlowInstance.getFormKey(), userInfo);
		map.put("formData", formData);
		return map;
	}

	/**
	 * 构建表单数据格式
	 *
	 * @param instanceId
	 *            流程实例主键
	 * @param layoutId
	 *            布局主键
	 * @param formKey
	 *            表单模板主键
	 * @param userInfo
	 *            当前操作员
	 * @return
	 */
	private FormData getWorkFlowData(Integer instanceId, Integer layoutId,
			Integer formKey, UserInfo userInfo) {
		// 表单数据
		FormData formData = new FormData();
		// 设置表单数据主键
		formData.setInstanceId(instanceId);

		// 表单布局
		FormLayout formLayout = new FormLayout();
		// 表单布局主键
		formLayout.setId(layoutId);
		formData.setFormLayout(formLayout);

		// 表单模板
		FormMod form = new FormMod();
		// 模板主键
		form.setId(formKey);
		formData.setForm(form);

		// 表单数据详情
		List<FormDataDetails> dataDetails = this.getFormDataDetails(instanceId,
				layoutId, formKey, userInfo, formData);
		formData.setDataDetails(dataDetails);
		return formData;
	}

	/**
	 * 表单数据构建
	 *
	 * @param instanceId
	 * @param layoutId
	 * @param formKey
	 * @param userInfo
	 * @param formData
	 * @return
	 */
	private List<FormDataDetails> getFormDataDetails(Integer instanceId,
			Integer layoutId, Integer formKey, UserInfo userInfo,
			FormData formData) {
		List<FormDataDetails> dataDetails = new ArrayList<FormDataDetails>(14);
		// fieldId对应componId
		Map<Integer, Integer> fieldIdMap = new HashMap<Integer, Integer>(14);
		// componId对应父compon
		Map<Integer, FormCompon> pComponMap = new HashMap<Integer, FormCompon>(14);
		// componId对应fieldId，只需要一个
		Map<Integer, Integer> subFormIdMap = new HashMap<Integer, Integer>(14);
		// fieldId对应maxIndex
		Map<Integer, Integer> maxIndexMap = new HashMap<Integer, Integer>(14);

		// 查询表单所有数据
		List<SpFlowInputData> spFlowInputDatas = workFlowDao.listSpFormDatas(
				instanceId, userInfo.getComId());
		if (null != spFlowInputDatas && !spFlowInputDatas.isEmpty()) {
			// 构建表单数据详情
			for (SpFlowInputData spFlowInputData : spFlowInputDatas) {
				// 表单数据详情
				FormDataDetails formDataDetails = new FormDataDetails();
				// 动态数据行号
				formDataDetails.setDataIndex(spFlowInputData.getDataIndex());
				//动态表单行关联的数据表
				formDataDetails.setBusTableId(spFlowInputData.getBusTableId());
				formDataDetails.setBusTableType(spFlowInputData.getBusTableType());

				// 数据内容
				formDataDetails.setContent(spFlowInputData.getContent());

				// 表单数据对象
				FormDataOption formField = new FormDataOption();
				// 组件字段标识
				Integer fieldId = spFlowInputData.getFieldId();
				formField.setId(fieldId);
				formDataDetails.setFormField(formField);
				formField.setComponentKey(spFlowInputData.getComponentKey());

				FormDataOption dataText = new FormDataOption();
				String componentKey = spFlowInputData.getComponentKey();
				// 父组件主键
				Integer pFormComponId = fieldIdMap.get(spFlowInputData
						.getFieldId());
				if (null == pFormComponId) {
					// 取得父组件信息
					FormCompon pFormCompon = formService.getParentCompon(
							userInfo.getComId(), formKey, layoutId, fieldId);
					if (null != pFormCompon) {// 已有父组件
						String pKeyType = pFormCompon.getComponentKey();
						if (pKeyType.equals("DataTable")) { // 是动态表
							if (!pComponMap.containsKey(pFormCompon.getId())) {// 缓存中没有该组件信息
								// 存放该组件的父组件信息
								subFormIdMap.put(pFormCompon.getId(), fieldId);
								// 动态表单行数
								maxIndexMap.put(fieldId, 1);
							}
							// 设置组件字段标识
							FormDataOption subForm = new FormDataOption();
							subForm.setId(pFormCompon.getId());
							formDataDetails.setSubForm(subForm);
						}
						// 缓存字段与组件关系
						fieldIdMap.put(fieldId, pFormCompon.getId());
						// 缓存主键信息
						pComponMap.put(pFormCompon.getId(), pFormCompon);
					}
				} else {// 已查询
					FormCompon pFormCompon = pComponMap.get(pFormComponId);
					if (null != pFormCompon) {
						String pKeyType = pFormCompon.getComponentKey();
						if (pKeyType.equals("DataTable")) {// 是动态表

							// 设置组件字段标识
							FormDataOption subForm = new FormDataOption();
							subForm.setId(pFormCompon.getId());
							formDataDetails.setSubForm(subForm);
							if (maxIndexMap.containsKey(fieldId)) {// 只统计同一个字段行数
								Integer maxIndex = maxIndexMap.get(fieldId) + 1;
								maxIndexMap.put(fieldId, maxIndex);
							}

						}
					}

				}

				if ("TextArea".equals(componentKey)) {// 构建文本框数据
					List<SpFlowOptData> spFlowOptDatas = workFlowDao
							.listSpFormOpts(instanceId,
									spFlowInputData.getId(),
									userInfo.getComId());
					if (null != spFlowOptDatas && !spFlowOptDatas.isEmpty()) {
						dataText.setContent(spFlowOptDatas.get(0)
								.getContentMore());
						formDataDetails.setDataText(dataText);
					}
				}

				// 流程表单数据的多选信息
				List<FormDataOption> dataOptions = new ArrayList<FormDataOption>();

				// 流程表单数据
				List<SpFlowOptData> spFlowOptDatas = workFlowDao
						.listSpFormOpts(instanceId, spFlowInputData.getId(),
								userInfo.getComId());
				if (null != spFlowOptDatas && spFlowOptDatas.size() > 0) {
					for (SpFlowOptData spFlowOptData : spFlowOptDatas) {
						// 构建流程表单数据
						FormDataOption formDataOption = new FormDataOption();
						// 设置选项标识
						formDataOption.setOptionId(spFlowOptData.getOptionId());
						// 设置选项内容
						formDataOption.setContent(spFlowOptData.getContent());
						dataOptions.add(formDataOption);
					}
				}
				formDataDetails.setDataOptions(dataOptions);

				dataDetails.add(formDataDetails);
			}
		}
		// 构建动态表单
		if (null != subFormIdMap && !subFormIdMap.isEmpty()) {

			List<FormDataOption> maxSubFormIndexs = new ArrayList<FormDataOption>();
			for (Integer subFormId : subFormIdMap.keySet()) {
				// 构建动态表单的行数
				FormDataOption formDataOption = new FormDataOption();
				// 构建动态表单的主键
				formDataOption.setSubFormId(subFormId);

				Integer fieldId = subFormIdMap.get(subFormId);
				// 构建动态表单的行数
				formDataOption.setMaxIndex(maxIndexMap.get(fieldId));

				maxSubFormIndexs.add(formDataOption);
			}
			formData.setMaxSubFormIndexs(maxSubFormIndexs);
		} else {
			// 取得子表单信息
			List<FormDataOption> maxSubFormIndexs = formService.listDataTables(
					userInfo.getComId(), layoutId);
			formData.setMaxSubFormIndexs(maxSubFormIndexs);
		}
		return dataDetails;
	}

	/**
	 * 获取个人权限的流程实例
	 *
	 * @param instance
	 *            筛选参数
	 * @return
	 */
	public List<SpFlowInstance> listSpFlowOfMine(UserInfo userInfo,
			SpFlowInstance instance) {
		List<SpFlowInstance> listSp = workFlowDao.listSpFlowOfMine(userInfo,
				instance);
//		if (null != listSp && !listSp.isEmpty()) {// 获取当前审批流程审批候选人数据集
//			for (SpFlowInstance sp : listSp) {
//				List<SpFlowCurExecutor> listSpFlowCurExecutor = workFlowDao
//						.listSpFlowCurExecutor(userInfo.getComId(), sp.getId());
//				sp.setListSpFlowCurExecutor(listSpFlowCurExecutor);
//			}
//		}
		return listSp;
	}

	/**
	 * 获取流程步骤表单权限数据集
	 *
	 * @param userInfo
	 *            当前操作人
	 * @param instanceId
	 *            流程实例主键
	 * @param stepId
	 *            流程步骤主键,0表示开始步骤
	 * @return
	 */
	public List<SpFlowRunStepFormControl> listSpFlowRunStepFormControl(
			UserInfo userInfo, Integer instanceId, Integer stepId) {
		return workFlowDao.listSpFlowRunStepFormControl(userInfo, instanceId,
				stepId);
	}

	/**
	 * 获取个人权限下的所有审批流程
	 *
	 * @param instance
	 *            筛选参数
	 *            是否是模块监督人员
	 * @return
	 */
	public List<SpFlowInstance> listSpFlowOfAll(UserInfo userInfo,
			SpFlowInstance instance) {
		boolean isForceInPersion = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_FLOW_SP);
		List<SpFlowInstance> listSp = workFlowDao.listSpFlowOfAll(userInfo,instance, isForceInPersion);
		return listSp;
	}

	/**
	 * 分页查询流程关联附件
	 *
	 * @param instanceId
	 *            流程实例化主键
	 * @param userInfo
	 *            当前操作人员
	 * @return
	 */
	public List<SpFlowUpfile> listPagedSpFiles(Integer instanceId,
			UserInfo userInfo) {
		return workFlowDao.listPagedSpFiles(instanceId, userInfo);
	}

	/**
	 * 查询流程关联附件
	 *
	 * @param instanceId
	 *            流程实例化主键
	 * @param userInfo
	 *            当前操作人员
	 * @return
	 */
	public List<SpFlowUpfile> listSpFiles(Integer instanceId, UserInfo userInfo) {
		return workFlowDao.listSpFiles(instanceId, userInfo);
	}

	/**
	 * 获取流程实例扭转历史步骤信息
	 *
	 * @param instanceId
	 *            流程实例化主键
	 * @param userInfo
	 *            当前操作人员
	 * @return
	 */
	public List<SpFlowHiStep> listSpFlowHiStep(Integer instanceId,
			UserInfo userInfo) {
		return workFlowDao.listSpFlowHiStep(instanceId, userInfo);
	}

	/**
	 * 删除流程实例化的审理关联
	 *
	 * @param instanceId
	 * @param userInfo
	 */
	public void delSpFlowInstRelate(Integer instanceId, UserInfo userInfo) {
		SpFlowInstance instance = new SpFlowInstance();
		instance.setId(instanceId);
		instance.setBusId(0);
		instance.setBusType("0");
		workFlowDao.update(instance);
		// 删除项目阶段数据
		workFlowDao.delByField("stagedInfo", new String[] { "comId",
				"moduleId", "moduleType" }, new Object[] { userInfo.getComId(),
				instanceId, ConstantInterface.STAGED_FLOW_SP });

	}

	/**
	 * 修改审批关联
	 *
	 * @param userInfo
	 *            当前操作人员
	 * @param formData
	 *            审批关联数据
	 */
	public Integer updateSpFlowRelate(UserInfo userInfo, FormData formData,
			String isStage) {
		SpFlowInstance spFlowInstance = new SpFlowInstance();

		Integer instanceId = formData.getInstanceId();
		spFlowInstance.setId(instanceId);
		String busType = formData.getBusType();
		// 上次关联的项目阶段主键
		// Integer preStageItemId = formData.getPreStageItemId();

		Integer busId = formData.getBusId();
		if (null != busId && busId > 0) {// 设置关联类型和关联模块主键
			// 设置关联审批
			spFlowInstance.setBusType(busType);
			spFlowInstance.setBusId(busId);
		}
		if (ConstantInterface.TYPE_ITEM.equals(busType)) {// 若是项目，则需要设置关联的项目阶段
			// 本次关联项目阶段主键
			Integer stagedItemId = formData.getStagedItemId();
			// 删除项目阶段数据
			workFlowDao.delByField("stagedInfo", new String[] { "comId",
					"moduleId", "moduleType" },
					new Object[] { userInfo.getComId(), instanceId,
							ConstantInterface.STAGED_FLOW_SP });
			if (null == stagedItemId || stagedItemId == 0) {// 若是本次关联项目阶段主键为空，则使用最近一次项目阶段
				// 取得最近
				stagedItemId = itemService.getStageItemId(userInfo, busId);
			}
			// 项目阶段添加审批关联
			itemService.addStageRelateMod(userInfo, busId, stagedItemId,
					instanceId, ConstantInterface.TYPE_FLOW_SP);
			if (null != isStage && "true".equals(isStage)) {
				return stagedItemId;
			}
		} else {// 关联的不是项目，则需要删除以前的
				// 删除项目阶段数据
			workFlowDao.delByField("stagedInfo", new String[] { "comId","moduleId", "moduleType" },
					new Object[] { userInfo.getComId(), instanceId,ConstantInterface.STAGED_FLOW_SP });
		}

		workFlowDao.update(spFlowInstance);
		return 0;
	}

	/**
	 * 删除流程
	 *
	 * @param ids
	 *            删除所需的流程主键数组
	 * @param userInfo
	 *            当前操作人信息
	 */
	public void delSpFlow(Integer[] ids, UserInfo userInfo) {
		if (null != ids && ids.length > 0) {
			for (Integer id : ids) {
				// 获取流程实例对象
				SpFlowInstance spFlowInstance = workFlowDao.getSpFlowInstanceById(id, userInfo);
				// 删除流程实例审批附件
				workFlowDao.delByField("spFlowUpfile", new String[] { "comId","busId","busType" },
						new Object[] { userInfo.getComId(), id,ConstantInterface.TYPE_FLOW_SP });
				// 删除流程实例步骤间关系表
				workFlowDao.delByField("spFlowHiStepRelation",new String[] {"comId", "busId","busType" },
						new Object[] { userInfo.getComId(), id,ConstantInterface.TYPE_FLOW_SP  });
				// 删除流程实例步骤
				workFlowDao.delByField("spFlowHiStep",new String[] { "comId","busId","busType" },
						new Object[] { userInfo.getComId(), id,ConstantInterface.TYPE_FLOW_SP  });
				// 删除流程实例步骤条件
				workFlowDao.delByField("spFlowStepConditions",new String[] { "comId","busId","busType" },
						new Object[] { userInfo.getComId(), id,ConstantInterface.TYPE_FLOW_SP  });
				// 删除流程实例步骤
				workFlowDao.delByField("spFlowHiStepExecutor", new String[] { "comId","busId","busType" },
						new Object[] { userInfo.getComId(), id,ConstantInterface.TYPE_FLOW_SP  });
				// 删除流程表单多数据
				workFlowDao.delByField("spFlowOptData",new String[] { "comId","instanceid" },
						new Object[] { userInfo.getComId(), id });
				// 删除流程表单数据
				workFlowDao.delByField("spFlowInputData", new String[] {"comId", "instanceid" },
						new Object[] { userInfo.getComId(), id });
				// 删除流程变量主键
				workFlowDao.delByField("spFlowRunVariableKey",new String[] {"comId", "busId","busType"},
						new Object[] { userInfo.getComId(), id,ConstantInterface.TYPE_FLOW_SP  });
				// 流程实例对象步骤授权
				workFlowDao.delByField("spFlowRunStepFormControl",new String[] { "comId", "busId", "busType" },
						new Object[] { userInfo.getComId(), id,ConstantInterface.TYPE_FLOW_SP });
				// 删除流程当前审批人
				workFlowDao.delByField("spFlowCurExecutor",new String[] {"comId", "busId", "busType" },
						new Object[] { userInfo.getComId(), id,ConstantInterface.TYPE_FLOW_SP  });
				// 删除子流程实例化后配置
				workFlowDao.delByField("spFlowRunRelevanceCfg",new String[] {"comId", "busId", "busType" },
						new Object[] { userInfo.getComId(), id ,ConstantInterface.TYPE_FLOW_SP });
				// 删除流程实例与模块关联关系业务数据映射关系临时表
				workFlowDao.delByField("busAttrMapFormColTemp",new String[] {"comId", "instanceid" },
						new Object[] { userInfo.getComId(), id });

				// 删除加班业务记录
				workFlowDao.delByField("overTime",new String[] { "comId","instanceId" },
						new Object[] { userInfo.getComId(), id });
				// 删除请假业务记录
				workFlowDao.delByField("leave", new String[] { "comId","instanceId" },
						new Object[] { userInfo.getComId(), id });

				// 审批序列号使用记录
				workFlowDao.delByField("spSerialNumRel",new String[] { "comId", "busId", "busType" },
						new Object[] { userInfo.getComId(), id,ConstantInterface.TYPE_FLOW_SP });

				// 删除事件管理过程记录
				workFlowDao.delByField("eventPm",new String[] { "comId","instanceId" },
						new Object[] { userInfo.getComId(), id });
				// 删除问题管理过程记录
				workFlowDao.delByField("issuePm",new String[] { "comId","instanceId" },
						new Object[] { userInfo.getComId(), id });
				// 删除变更管理过程记录
				workFlowDao.delByField("modifyPm",new String[] { "comId","instanceId" },
						new Object[] { userInfo.getComId(), id });
				// 删除发布管理过程记录
				workFlowDao.delByField("releasePm",new String[] { "comId","instanceId" },
						new Object[] { userInfo.getComId(), id });

				// 删除汇报业务记录
				workFlowDao.delByField("feeLoanOff",new String[] {"comId", "instanceId" },
						new Object[] { userInfo.getComId(), id });
				// 删除报销业务记录
				workFlowDao.delByField("feeLoanReport",new String[] { "comId","instanceId" },
						new Object[] { userInfo.getComId(), id });
				// 删除借款业务记录
				workFlowDao.delByField("feeLoan", new String[] { "comId","instanceid" },
						new Object[] { userInfo.getComId(), id });
				// 删除出差业务记录
				workFlowDao.delByField("feeBudget",new String[] { "comId","instanceid" },
						new Object[] { userInfo.getComId(), id });
				// 删除流程实例化对象表
				workFlowDao.delByField("spFlowInstance",new String[] {"comId", "id" },
						new Object[] { userInfo.getComId(), id });
				// 删除相关流程引擎
				cusActivitService.delActivitiInfo(spFlowInstance);

				// 删除数据更新记录
				workFlowDao.delByField("todayWorks", new String[] { "comId","busType", "busId" },
						new Object[] { userInfo.getComId(),ConstantInterface.TYPE_FLOW_SP, id });
				// 删除回收箱数据
				workFlowDao.delByField("recycleBin",new String[] { "comId", "busType", "busId", "userId" },
						new Object[] { userInfo.getComId(),ConstantInterface.TYPE_FLOW_SP, id,userInfo.getId() });
			}
		}
	}

	/**
	 * 配置自定义审批配置信息
	 *
	 * @param formId
	 *            表单主键
	 * @param userInfo
	 *            当前操作人
	 * @return
	 */
	public SpFlowInstance initSpFlowByUserDefined(Integer formId,
			UserInfo userInfo) {
		delUnStartSpFlow(userInfo);// 删除个人为无效流程
		FormMod formMod = null;// 表单配置
		Integer layoutId = null;// 表单布局主键
		Integer formLayoutState = null;
		if (null != formId && 0 != formId) {
			formMod = formService
					.getFormModVersion(formId, userInfo.getComId());// 取得模板信息
			layoutId = formMod.getLayoutId();// 表单布局主键
			formLayoutState = formMod.getFormState();

		}

		Integer formState = formMod.getFormState();
		formState = formState == null ? 0 : formState;

		SpFlowInstance spFlowInstance = new SpFlowInstance();// 实例化对象
		spFlowInstance.setComId(userInfo.getComId());// 团队号
		spFlowInstance.setCreator(userInfo.getId());// 发起人
		spFlowInstance.setFormKey(formId);// 表单主键
		spFlowInstance.setFormLayoutState(formState);
		spFlowInstance.setFormVersion(null == formMod ? null : formMod
				.getVersion());// 表单模板版本
		spFlowInstance.setFlowState(ConstantInterface.SP_STATE_UNSTART);// 默认流程状态标识
		spFlowInstance.setFormModName(formMod.getModName());// 使用的表单模板名称
		spFlowInstance.setSpState(ConstantInterface.SP_STATE_PASS);// 默认流程审批状态

		String flowName = userInfo.getUserName() + formMod.getModName() + "-"
				+ DateTimeUtil.getNowDateStr(5);// 流程名称形如：{user}请假单-{year}年{month}月{day}日
		spFlowInstance.setFlowName(flowName);// 流程名称
		spFlowInstance.setRecordCreateTime(DateTimeUtil
				.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));

		Integer instanceId = workFlowDao.add(spFlowInstance);// 添加实例化信息
		spFlowInstance.setId(instanceId);
		spFlowInstance.setRecordCreateTime(DateTimeUtil
				.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		spFlowInstance.setLayoutId(layoutId);
		spFlowInstance.setFormLayoutState(formLayoutState);
		// 初始化开始步骤节点的表单控制权限
		// 取得表单布局，组件以及配置
		List<FormCompon> listAllFormCompont = formService.listAllFormCompont(
				userInfo.getComId(), formId, layoutId, null, null);
		if (!CommonUtil.isNull(listAllFormCompont)) {
			SpFlowRunStepFormControl spFlowRunStepFormControl = new SpFlowRunStepFormControl();
			spFlowRunStepFormControl.setComId(userInfo.getComId());
			spFlowRunStepFormControl.setBusId(instanceId);
			spFlowRunStepFormControl.setBusType(ConstantInterface.TYPE_FLOW_SP);
			spFlowRunStepFormControl.setStepId(1);// 默认起始步骤主键为1
			spFlowRunStepFormControl.setStepType("start");
			spFlowRunStepFormControl.setIsFill(0);
			for (FormCompon formCompon2 : listAllFormCompont) {
				spFlowRunStepFormControl.setFormControlKey(formCompon2
						.getFieldId().toString());
				workFlowDao.add(spFlowRunStepFormControl);
			}
		}

		return spFlowInstance;
	}

	/**
	 * 获取已执行的审批步骤集合
	 *
	 * @param userInfo
	 *            当前操作人信息
	 * @param instanceId
	 *            流程实例主键
	 * @return
	 */
	public List<SpFlowHiStep> listHistorySpStep(UserInfo userInfo,
			Integer instanceId) {
		SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(instanceId,userInfo);// 获取流程实例化对象信息
		// 1、流程引擎向下走一步
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(instance.getActInstaceId(), userInfo);
		if (null == curTask || null == curTask.getTaskDefinitionKey()) {// 如果流程步骤信息为NULL时，直接返回
			throw new IllegalArgumentException();
		}

		String taskDefKey = curTask.getTaskDefinitionKey();
		return workFlowDao.listHistorySpStep(userInfo.getComId(),instanceId,taskDefKey);
	}

	/**
	 * 审批索引维护
	 *
	 * @param instanceId
	 *            流程实例主键
	 * @param userInfo
	 *            当前操作人信息
	 * @param opType
	 *            操作类型 ；add 添加索引、del 根据主键删除索引、update 根据主键更新索引（更新）
	 * @throws Exception
	 */
	public void updateSpFlowInstanceIndex(Integer instanceId,
			UserInfo userInfo, String opType) throws Exception {
		// 更新审批实例索引
		SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(instanceId,
				userInfo);// 获取流程实例化对象信息
		if (null == instance) {
			return;
		}
		// 再添加新索引数据
		// 把bean非空属性值连接策划那个字符串
		// 不检索字段设置为null
		StringBuffer attStr = new StringBuffer(instance.getFlowName());
		// 单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String indexKey = userInfo.getComId() + "_"
				+ ConstantInterface.TYPE_FLOW_SP + "_" + instanceId;
		// 为审批实例创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(indexKey,
				userInfo.getComId(), instanceId,
				ConstantInterface.TYPE_FLOW_SP, instance.getFlowName(),
				attStr.toString(),
				DateTimeUtil.parseDate(instance.getRecordCreateTime(), 0));
		if (null != listIndexDoc) {
			// 根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType, indexService, userInfo,
					listIndexDoc, indexKey));
		}

	}

	/**
	 * 获取团队下所有有效的审批流程
	 *
	 * @param userInfo
	 *            当前操作人
	 * @return
	 */
	public List<SpFlowInstance> listSpFlowInstanceOfOrg(UserInfo userInfo) {
		return workFlowDao.listSpFlowInstanceOfOrg(userInfo);
	}

	/**
	 * 审批流程实例查看权限验证
	 *
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param userId
	 *            验证人主键
	 * @return
	 */
	public boolean authorCheck(Integer comId, Integer instanceId, Integer userId) {
		SpFlowInstance spFlowInstance = workFlowDao.authorCheck(comId,instanceId, userId);
		if (null != spFlowInstance) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 审批流程实例查看权限验证
	 *
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param userId
	 *            验证人主键
	 * @return
	 */
	public boolean authorBaseCheck(Integer comId, Integer instanceId, Integer userId,Integer taskId) {
		SpFlowInstance spFlowInstance = workFlowDao.authorBaseCheck(comId,instanceId, userId,taskId);
		if (null != spFlowInstance) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证当前流程是否有需要直属上级审批的步骤，如果有；则验证当前操作人是否设置了。
	 *
	 * @param userInfo
	 *            当前操作人信息
	 * @param flowId
	 *            发起流程主键
	 * @return
	 */
	public boolean toSetDirectLeader(UserInfo userInfo, Integer flowId) {
		SpFlowStep spFlowStep = workFlowDao.haveDirectLeaderToSpStep(
				userInfo.getComId(), flowId);
		if (null != spFlowStep) {
			UserInfo directLeader = userInfoService
					.queryDirectLeaderInfo(userInfo);
			if (null != directLeader) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}


	/**
	 * 取得需要映射的数据
	 * @param userInfo
	 * @param instanceId
	 * @return
	 */
	public Map<String,String> mapForBean(UserInfo userInfo, Integer instanceId){
		//根据流程实例主键获取业务、审批表关联关系
		List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp = formColTempService.listBusAttrMapFormColTemp(userInfo.getComId(),instanceId);
		//获取当前审批流程对应表单的控件对象MAP
		Map<String,SpFlowInputData> formDataMap = this.querySpFlowInputDataMap(instanceId,userInfo.getComId());
		Map<String,String> mapForBean = this.constrBean(userInfo,instanceId,listBusAttrMapFormColTemp,formDataMap);
		return mapForBean;
	}

	/**
	 * 触发关联模块业务引擎
	 *
	 * @param instance
	 *            流程实例
	 * @param userInfo
	 *            当前操作人信息
	 */
	private void initBusEngineData(SpFlowInstance instance, UserInfo userInfo) {
		//根据流程实例主键获取业务、审批表关联关系
		List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp = formColTempService.listBusAttrMapFormColTemp(userInfo.getComId(),instance.getId());
		//获取当前审批流程对应表单的控件对象MAP
		Map<String,SpFlowInputData> formDataMap = this.querySpFlowInputDataMap(instance.getId(),userInfo.getComId());
		Map<String,String> mapForBean = null;
		if (ConstantInterface.TYPE_ITOM_EVENTPM.equals(instance.getBusType())// IT运维管理-事件管理过程
				|| ConstantInterface.TYPE_ITOM_ISSUEPM.equals(instance.getBusType())// IT运维管理-问题管理过程
				|| ConstantInterface.TYPE_ITOM_MODIFYPM.equals(instance.getBusType())// IT运维管理-变更管理过程
				|| ConstantInterface.TYPE_ITOM_RELEASEPM.equals(instance.getBusType())// IT运维管理-发布管理过程
				){
			mapForBean = this.constrBean(userInfo,instance.getId(),listBusAttrMapFormColTemp,formDataMap);
		}
		//映射数据信息
		busRelateSpFlowService.initBusEngineData(instance, userInfo,listBusAttrMapFormColTemp,formDataMap,mapForBean);

	}
	/**
	 * 构建实体映射数据
	 * @param userInfo
	 * @param listBusAttrMapFormColTemp
	 * @param formDataMap
	 * @return
	 */
	public Map<String, String> constrBean(
			UserInfo userInfo, Integer instanceId,
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp,
			Map<String, SpFlowInputData> formDataMap) {
		Map<String,String> map = new HashMap<String, String>(4);
		if(null!=listBusAttrMapFormColTemp && !listBusAttrMapFormColTemp.isEmpty()){
			for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
				SpFlowInputData inputData = formDataMap.get(busAttrMapFormColTemp.getFormCol());
				if(null==inputData){continue;}
				String busAttr = busAttrMapFormColTemp.getBusAttr();
				if("busModName".equalsIgnoreCase(busAttr)){
					Object obj = this.getFormComponentValue(inputData.getComponentKey(),inputData.getId(),userInfo.getComId(),instanceId);
					if(obj instanceof List) {
						@SuppressWarnings("unchecked")
						List<SpFlowOptData> listData = (List<SpFlowOptData>)obj;
						if(!CommonUtil.isNull(listData)) {
							StringBuffer dataVal = new StringBuffer();
							SpFlowOptData defSpFlowOptData = listData.get(0);
							for (SpFlowOptData spFlowOptData : listData) {
								dataVal.append(spFlowOptData.getContent() + "，");
							}
							dataVal = new StringBuffer(dataVal.substring(0,dataVal.lastIndexOf("，")));
							String value = dataVal.toString();

							map.put(busAttrMapFormColTemp.getBusAttr(),value);
							map.put("busType",defSpFlowOptData.getDataBustype());
							map.put("busId",defSpFlowOptData.getOptionId().toString());
						}
					}
				}else{
					String value = this.formComponentValueToString(inputData.getComponentKey(),inputData.getId(),userInfo.getComId(),instanceId);
					map.put(busAttrMapFormColTemp.getBusAttr(),value);

				}
			}
		}
		return map;
	}

	/**
	 * 触发关联模块业务引擎 只在流程正式发起时调用
	 *
	 * @param instance
	 *            流程实例
	 * @param userInfo
	 *            当前操作人信息
	 */
	private void updatetBusEngineWhenFlowStart(SpFlowInstance instance,
			UserInfo userInfo) {

		String busType = instance.getBusType();
		Class<?> clz = null;
		if (ConstantInterface.TYPE_FEE_APPLY_TRIP.equals(busType)//费用额度申请
				|| ConstantInterface.TYPE_FEE_APPLY_DAYLY.equals(busType)) {//费用额度申请
			clz = FeeBudget.class;
		}else if (ConstantInterface.TYPE_LOAN_TRIP.equals(busType)
				|| ConstantInterface.TYPE_LOAN_DAYLY.equals(busType)) {//借款（出差借款、一般借款）申请流程
			clz = FeeLoan.class;
		}else if (ConstantInterface.TYPE_REPORT_TRIP.equals(busType)//工作汇报申请
				|| ConstantInterface.TYPE_REPORT_DAYLY.equals(busType)) {//工作汇报申请
			clz = FeeLoanReport.class;
		}else if (ConstantInterface.TYPE_LOANOFF_DAYLY.equals(busType)//费用报销申请
				|| ConstantInterface.TYPE_LOANOFF_TRIP.equals(busType)) {//费用报销申请
			clz = FeeLoanOff.class;
		}else if (ConstantInterface.TYPE_ITOM_EVENTPM.equals(busType)) {//IT运维管理-事件管理过程
			clz = EventPm.class;
		}else if(ConstantInterface.TYPE_ITOM_ISSUEPM.equals(busType)){//IT运维管理-问题管理过程
			clz = IssuePm.class;
		}else if(ConstantInterface.TYPE_ITOM_MODIFYPM.equals(busType)){//IT运维管理-变更管理过程
			clz = ModifyPm.class;
		}else if(ConstantInterface.TYPE_ITOM_RELEASEPM.equals(busType)){//IT运维管理-发布管理过程
			clz = ReleasePm.class;
		}else if(ConstantInterface.TYPE_OVERTIME.equals(busType)){
			clz = OverTime.class;
		}else if(ConstantInterface.TYPE_LEAVE.equals(busType)){
			clz = Leave.class;
		}

		if(null!=clz){
			busRelateSpFlowService.initBusModStatus(instance.getBusType(),instance.getBusId(),clz);
		}
	}

	/**
	 * 获取表单对应值
	 *
	 * @param componentKey
	 *            控件类型
	 * @param formFlowDataId
	 *            流程表单数据spFlowInputData主键
	 * @param comId
	 *            单位主键
	 * @param instanceId
	 *            流程实例主键
	 * @return
	 */
	public Object getFormComponentValue(String componentKey,
			Integer formFlowDataId, Integer comId, Integer instanceId) {
		if ("Employee".equalsIgnoreCase(componentKey)
				|| "Department".equalsIgnoreCase(componentKey)
				|| "RelateMod".equalsIgnoreCase(componentKey)
				|| "RelateItem".equalsIgnoreCase(componentKey)
				|| "RelateCrm".equalsIgnoreCase(componentKey)) {
			return querySpFlowOptDataByMore(formFlowDataId, comId, instanceId);
		} else if ("CheckBox".equalsIgnoreCase(componentKey)
				|| "Select".equalsIgnoreCase(componentKey)
				|| "TextArea".equalsIgnoreCase(componentKey)
				|| "RadioBox".equalsIgnoreCase(componentKey)) {
			List<SpFlowOptData> listSpFlowOptData = workFlowDao
					.querySpFlowOptDataByMore(formFlowDataId, comId, instanceId);
			if (null != listSpFlowOptData && !listSpFlowOptData.isEmpty()) {
				StringBuffer value = new StringBuffer();
				for (SpFlowOptData spFlowOptData : listSpFlowOptData) {
					String content = spFlowOptData.getContent();
					if(StringUtils.isEmpty(content)){
						content = spFlowOptData.getContentMore();
					}
					value.append(content + "，");
				}
				value = new StringBuffer(value.substring(0,value.lastIndexOf("，")));
				return value.toString();
			}
		} else {
			return workFlowDao.querySpFlowOptDataByOne(formFlowDataId, comId,
					instanceId).getContent();
		}
		return null;
	}

	/**
	 * 获取控件选项为多选，最终值为可能为多个的控件值对象
	 *
	 * @param formFlowDataId
	 *            流程表单数据spFlowInputData主键
	 * @param comId
	 *            单位主键
	 * @param instanceId
	 *            流程实例主键
	 * @return
	 */
	public List<SpFlowOptData> querySpFlowOptDataByMore(Integer formFlowDataId,
			Integer comId, Integer instanceId) {
		return workFlowDao.querySpFlowOptDataByMore(formFlowDataId, comId,
				instanceId);
	}

	/**
	 * 获取控件选项为多选，最终值为一个的控件值对象
	 *
	 * @param formFlowDataId
	 *            流程表单数据spFlowInputData主键
	 * @param comId
	 *            单位主键
	 * @param instanceId
	 *            流程实例主键
	 * @return
	 */
	public SpFlowOptData querySpFlowOptDataByOne(Integer formFlowDataId,
			Integer comId, Integer instanceId) {
		return workFlowDao.querySpFlowOptDataByOne(formFlowDataId, comId,
				instanceId);
	}

	/**
	 * 获取流程实例表单控件主键与控件值对象MAP
	 *
	 * @param instanceId
	 *            流程实例主键
	 * @param comId
	 *            单位主键
	 * @return
	 */
	public Map<String, SpFlowInputData> querySpFlowInputDataMap(
			Integer instanceId, Integer comId) {
		Map<String, SpFlowInputData> dataMap = new HashMap<String, SpFlowInputData>(14);
		List<SpFlowInputData> spFlowInputDatas = workFlowDao.listSpFormDatas(instanceId, comId);
		if (null != spFlowInputDatas && !spFlowInputDatas.isEmpty()) {
			for (SpFlowInputData spFlowInputData : spFlowInputDatas) {
				dataMap.put(spFlowInputData.getFieldId().toString(),spFlowInputData);
			}
		}
		return dataMap;
	}

	/**
	 * 拾取审批审批
	 *
	 * @param sessionUser
	 *            当前操作员
	 * @param instanceId
	 *            流程实例化主键
	 */
	public Map<String, Object> updatePickSpInstance(UserInfo sessionUser,
			Integer instanceId, String actInstanceId) {
		Map<String, Object> map = cusActivitService.updatePickSpInstance(
				sessionUser, actInstanceId);
		if (map.get("status").equals("y")) {
			// 删除审批的其他执行人
			List<SpFlowCurExecutor> curExecutors = workFlowDao
					.listSpFlowCurExecutor(sessionUser.getComId(), instanceId);
			if (null != curExecutors && curExecutors.size() > 1) {
				for (SpFlowCurExecutor spFlowCurExecutor : curExecutors) {
					Integer exector = spFlowCurExecutor.getExecutor();
					if (!exector.equals(sessionUser.getId())) {
						workFlowDao.delById(SpFlowCurExecutor.class,
								spFlowCurExecutor.getId());
					}
				}
			}
		}
		return map;

	}

	/**
	 * 修改审批的办理人员
	 *
	 * @param sessionUser
	 * @param instanceId
	 * @param actInstanceId
	 * @param newAssignerId
	 * @param formDataStr
	 * @throws Exception
	 */
	public Map<String, Object> updateSpInsAssignV2(UserInfo sessionUser,
			Integer instanceId, String actInstanceId, Integer newAssignerId,
			String formDataStr) throws Exception {
		JSONObject layoutDetailObj = JSONObject.parseObject(formDataStr);
		FormData formData = (FormData) com.alibaba.fastjson.JSONObject
				.toJavaObject(layoutDetailObj, FormData.class);

		Integer curStepId = null;// 当前步骤主键声明

		// 1、流程引擎向下走一步
		Task curTask = cusActivitService.updateNextStepUser(actInstanceId,
				sessionUser.getComId() + ":" + sessionUser.getId());
		if (null == curTask || null == curTask.getTaskDefinitionKey()) {// 如果流程步骤信息为NULL时，直接返回
			throw new IllegalArgumentException();
		}
		String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
		if (actTaskDefinitionKey.contains("_")
				&& CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])) {
			curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);
		}

		Map<String, Object> map = cusActivitService.updateSpInsAssignByTask(
				sessionUser, curTask, newAssignerId);
		if (map.get("status").equals("y")) {

			// 修改审批审批办理人员
			List<SpFlowCurExecutor> curExecutors = workFlowDao
					.listSpFlowCurExecutor(sessionUser.getComId(), instanceId);
			if (null != curExecutors && !curExecutors.isEmpty()) {
				for (SpFlowCurExecutor spFlowCurExecutor : curExecutors) {
					Integer exector = spFlowCurExecutor.getExecutor();
					if (exector.equals(sessionUser.getId())) {
						spFlowCurExecutor.setExecutor(newAssignerId);
						workFlowDao.update(spFlowCurExecutor);
						break;
					}
				}
			}

			// 记录流程当前审批人
			SpFlowHiExecutor spFlowHiExecutor = new SpFlowHiExecutor();
			spFlowHiExecutor.setComId(sessionUser.getComId());
			spFlowHiExecutor.setBusId(instanceId);
			spFlowHiExecutor.setBusType(ConstantInterface.TYPE_FLOW_SP);
			spFlowHiExecutor.setStepId(curStepId);
			spFlowHiExecutor.setExecutor(sessionUser.getId());
			workFlowDao.add(spFlowHiExecutor);

			List<SpFlowUpfile> spFlowUpfiles = formData.getSpFlowUpfiles();
			// 添加附件信息
			this.addSpFlowUpfile(sessionUser, spFlowUpfiles, curStepId,
					instanceId);
			// 持久化审批意见
//			if (StringUtils.isNotEmpty(formData.getSpIdea())) {
//				cusActivitService.addComment(
//						curTask.getId(),
//						actInstanceId,
//						null == formData.getSpIdea() ? "" : formData
//								.getSpIdea());
//			}
			SpFlowInstance instance = (SpFlowInstance) workFlowDao.objectQuery(
					SpFlowInstance.class, instanceId);
			//转办审批过程中记录
			UserInfo  newAssigner = userInfoService.getUserInfo(sessionUser.getComId(),newAssignerId);
			//日志描述记录
			String spMsg = CommonUtil.isNull(formData.getSpIdea())?("审批转办由“"+newAssigner.getUserName()+"”处理。")
					:(formData.getSpIdea()+";审批转办由“"+newAssigner.getUserName()+"”处理。");
			this.addActTaskForLogs(CommonUtil.buildFlowExetutor(sessionUser.getComId(),sessionUser.getId()),
					curTask.getId(),curTask.getName()+"-转办",instance.getActInstaceId(),spMsg);

		}
		return map;
	}

	/**
	 * 修改审批的办理人员
	 *
	 * @param sessionUser
	 * @param instanceId
	 * @param actInstanceId
	 * @param newAssignerId
	 * @throws Exception
	 */
	public Map<String, Object> updateSpInsAssign(UserInfo sessionUser,
			Integer instanceId, String actInstanceId, Integer newAssignerId) throws Exception {
		Integer curStepId = null;// 当前步骤主键声明

		// 1、流程引擎向下走一步
		Task curTask = cusActivitService.updateNextStepUser(actInstanceId,
				sessionUser.getComId() + ":" + sessionUser.getId());
		if (null == curTask || null == curTask.getTaskDefinitionKey()) {// 如果流程步骤信息为NULL时，直接返回
			throw new IllegalArgumentException();
		}
		String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
		if (actTaskDefinitionKey.contains("_")
				&& CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])) {
			curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);
		}

		Map<String, Object> map = cusActivitService.updateSpInsAssignByTask(
				sessionUser,curTask, newAssignerId);
		if (map.get("status").equals("y")) {

			// 修改审批审批办理人员
			List<SpFlowCurExecutor> curExecutors = workFlowDao
					.listSpFlowCurExecutor(sessionUser.getComId(), instanceId);
			if (null != curExecutors && !curExecutors.isEmpty()) {
				for (SpFlowCurExecutor spFlowCurExecutor : curExecutors) {
					Integer exector = spFlowCurExecutor.getExecutor();
					if (exector.equals(sessionUser.getId())) {
						spFlowCurExecutor.setExecutor(newAssignerId);
						workFlowDao.update(spFlowCurExecutor);
						break;
					}
				}
			}
			// 记录流程当前审批人
			SpFlowHiExecutor spFlowHiExecutor = new SpFlowHiExecutor();
			spFlowHiExecutor.setComId(sessionUser.getComId());
			spFlowHiExecutor.setBusId(instanceId);
			spFlowHiExecutor.setBusType(ConstantInterface.TYPE_FLOW_SP);
			spFlowHiExecutor.setStepId(curStepId);
			spFlowHiExecutor.setExecutor(sessionUser.getId());
			workFlowDao.add(spFlowHiExecutor);

			// 创建待办事项
			List<UserInfo> shares = new ArrayList<UserInfo>();
			UserInfo share = userInfoService.getUserBaseInfo(
					sessionUser.getComId(), newAssignerId);
			shares.add(share);

			SpFlowInstance instance = (SpFlowInstance) workFlowDao.objectQuery(
					SpFlowInstance.class, instanceId);
			// 先删除所有待办事项，添加普通提醒通知
			todayWorksService.addTodayWorks(sessionUser, newAssignerId,
					instanceId, instance.getFlowName(),
					ConstantInterface.TYPE_FLOW_SP, shares, null);
			//转办审批过程中记录
			UserInfo  newAssigner = userInfoService.getUserInfo(sessionUser.getComId(),newAssignerId);
			this.addActTaskForLogs(CommonUtil.buildFlowExetutor(sessionUser.getComId(),sessionUser.getId()),
					curTask.getId(),curTask.getName()+"-转办",instance.getActInstaceId(),"审批转办由“"+newAssigner.getUserName()+"”处理。");
		}
		return map;
	}
	/**
	 * 出差汇报完成后自动关联数据
	 *
	 * @param userInfo
	 * @param spFlowInstance
	 */
	public void addLoanOffDraft(UserInfo userInfo, SpFlowInstance spFlowInstance) {

		FormData sonFormData = this.initLoanDraft(spFlowInstance, userInfo);

		if (null != sonFormData) {
			WorkFlowData workFlowData = new WorkFlowData();
			workFlowData.setClientSource("pc");
			workFlowData.setFormData(sonFormData);
			workFlowData.setModule("freeform");
			workFlowData.setIsDel(0);
			Gson gson = new Gson();
			try {
				this.addFormData(gson.toJson(workFlowData), userInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private FormData initLoanDraft(SpFlowInstance instance, UserInfo userInfo) {
		// 表单数据
		FormData formData = new FormData();
		Integer instanceId = instance.getId();
		// 设置表单数据主键
		formData.setInstanceId(instanceId);

		// 表单布局
		FormLayout formLayout = new FormLayout();
		Integer layoutId = instance.getLayoutId();
		// 表单布局主键
		formLayout.setId(layoutId);
		formData.setFormLayout(formLayout);

		// 表单模板
		FormMod form = new FormMod();
		Integer formKey = instance.getFormKey();
		// 模板主键
		form.setId(formKey);
		formData.setForm(form);

		// 表单数据详情
		List<FormDataDetails> dataDetails = null;
		// List<FormDataDetails> dataDetails =
		// this.getCheckInFormDataDetails(instanceId,layoutId,
		// formKey,userInfo,null);
		formData.setDataDetails(dataDetails);

		formData.setBusId(instance.getBusId());
		formData.setBusType(instance.getBusType());
		formData.setStagedItemId(0);
		formData.setPreStageItemId(0);
		formData.setStagedItemName("");
		formData.setDataStatus("tempCheckIn");
		formData.setCreator(instance.getCreator());
		// 固定流程需要的
		formData.setFlowId(instance.getFlowId());

		// 发起流程默认审批通过
		formData.setSpState(1);
		return formData;
	}


	/**
	 * 获取流程下一步步骤信息
	 * @param instanceId 流程实例化主键
	 * @param userInfo
	 * @return
	 */
	public SpFlowHiStep querySpFlowNextStepInfo(Integer instanceId,
			UserInfo userInfo) {
		SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(instanceId, userInfo);//获取流程实例化对象信息

		SpFlowHiStep stepInfo = new SpFlowHiStep();


		//1、流程引擎向下走一步
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(instance.getActInstaceId(), userInfo);

		if(null==curTask || null==curTask.getTaskDefinitionKey()){//如果流程步骤信息为NULL时，直接返回
			throw new IllegalArgumentException();
		}
		//提交前，验证当前activities审批是否有子审批；如果有，则不能提交
		if(!CommonUtil.isNull(this.queryActivitiesSonTaskList(curTask.getId()))) {
			stepInfo.setStepType(ActLinkTypeEnum.HUIQIANING.getValue());//会签中
			return stepInfo;
		}

		Map<String,Integer> stepInfoMap = this.conStrCurStepId(curTask.getTaskDefinitionKey());
		//当前步骤主键声明
		Integer curStepId = stepInfoMap.get("curStepId");

		//当前步步骤信息
		SpFlowHiStep curStepInfo = workFlowDao.querySpFlowNextStepInfo(userInfo.getComId(),instanceId,curStepId);
		stepInfo.setCurStepInfo(curStepInfo);

		//根据当前步骤主键以及步骤间关系获取下步步骤主键
		List<SpFlowHiStepRelation> nextStepList = workFlowDao.querySpFlowHiStepRelation(userInfo.getComId(),instanceId,curStepId);
		if(null!=nextStepList && !nextStepList.isEmpty()){
			Integer nextStepNum = nextStepList.size();
			List<SpFlowHiStep> nextStepInfoList = new ArrayList<SpFlowHiStep>();
			for (SpFlowHiStepRelation nextStep : nextStepList) {
				//下步步骤信息
				SpFlowHiStep nextStepInfo = workFlowDao.querySpFlowNextStepInfo(userInfo.getComId(),instanceId,nextStep.getNextStepId());
				//候选人
				List<UserInfo> listSpFlowHiExecutor = this.listSpFlowHiExecutor(userInfo,instanceId,nextStepInfo);
				nextStepInfo.setListSpFlowHiExecutor(listSpFlowHiExecutor);//步骤办理候选人集合
				if(nextStepNum>1){
					nextStepInfo.setDefaultStepState(nextStep.getDefaultStep());
					//步骤条件信息
					List<SpFlowStepConditions> listStepConditions = workFlowDao.listSpFlowStepConditions(userInfo.getComId(),instanceId,nextStep.getNextStepId());
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
	 * 流程步骤主键信息
	 * @param actTaskDefinitionKey
	 * @return
	 */
	private Map<String,Integer> conStrCurStepId(String actTaskDefinitionKey) {
		Map<String,Integer> map = new HashMap<String,Integer>(4);
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
	 * 根据配置获取候选人信息
	 * @param userInfo
	 * @param instanceId 流程实例主键
	 * @param curStepInfo 步骤主键
	 * @return
	 */
	public List<UserInfo> listSpFlowHiExecutor(UserInfo userInfo,Integer instanceId,
			SpFlowHiStep curStepInfo) {
		List<UserInfo> listSpFlowHiExecutor = new ArrayList<UserInfo>();
		if(ConstantInterface.EXECUTOR_BY_SELF.equals(curStepInfo.getExecutorWay())){//发起人自己
			SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(instanceId, userInfo);//获取流程实例化对象信息
			UserInfo creatorSelf = userInfoService.getUserBaseInfo(userInfo.getComId(),instance.getCreator());
			listSpFlowHiExecutor.add(creatorSelf);
		}else if(ConstantInterface.EXECUTOR_BY_DIRECT.equals(curStepInfo.getExecutorWay())){//发起人直属上级
			SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(instanceId, userInfo);//获取流程实例化对象信息
			UserInfo creatorSelf = userInfoService.getUserBaseInfo(userInfo.getComId(),instance.getCreator());
			UserInfo directLeaderInfo = userInfoService.queryDirectLeaderInfo(creatorSelf);
			listSpFlowHiExecutor.add(directLeaderInfo);
		}else{//根据配置获取候选人信息
			listSpFlowHiExecutor = workFlowDao.listSpFlowHiExecutor(userInfo.getComId(),instanceId,curStepInfo.getStepId());
		}
		return listSpFlowHiExecutor;
	}
	/**
	 * 分页查询数据用于选择
	 * @param userInfo
	 * @param instance
	 * @return
	 */
	public PageBean<SpFlowInstance> listPagedSpFlowForSelect(UserInfo userInfo,
			SpFlowInstance instance) {
		boolean isForceInPersion = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_FLOW_SP);
		List<SpFlowInstance> listSp = workFlowDao.listSpFlowOfAll(userInfo,instance, isForceInPersion);

		PageBean<SpFlowInstance> pageBean = new PageBean<SpFlowInstance>();
		pageBean.setRecordList(listSp);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	/**
	 * 审批催办
	 * @param userInfo 当前操作元员
	 * @param busId 业务主键
	 * @return
	 */
	public Map<String, Object> queryRemindConf(UserInfo userInfo,
			Integer busId,String busType) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		SpFlowInstance flowInstance = (SpFlowInstance) workFlowDao.objectQuery(SpFlowInstance.class, busId);
		map.put("busModName", flowInstance.getFlowName());
		if(flowInstance.getFlowState().equals(ConstantInterface.SP_STATE_FINISH)){
			map.put("status", "f");
			map.put("info","事项审批已经结束！");
		}else{
			map.put("status", "y");
			String defMsg = "请尽快办理“"+flowInstance.getFlowName()+"”事项审批！";
			map.put("defMsg", defMsg);

			//事项的执行人员信息
			List<BusRemindUser> listReminderUser = this.listSpFlowRemindExecutor(userInfo,busId,busType);
			map.put("listRemindUser", listReminderUser);
		}
		return map;
	}
	/**
	 * 审批批量催办
	 * @param userInfo 当前操作元员
	 * @param busId 业务主键
	 * @return
	 */
	public List<BusRemindUser> queryRemindsConf(UserInfo userInfo,
											   Integer busId,String busType) {
		List<BusRemindUser> listReminderUser = new ArrayList<BusRemindUser>();

		//事项的执行人员信息
		listReminderUser = this.listSpFlowRemindExecutor(userInfo,busId,busType);
		if(null == listReminderUser || listReminderUser.isEmpty()){
			return null;
		}else{
			return listReminderUser;
		}
	}
	/**
	 * 获取可以催办的事项执行人
	 * @param userInfo 当前操作人员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	public List<BusRemindUser> listSpFlowRemindExecutor(UserInfo userInfo,Integer busId,String busType){
		return workFlowDao.listSpFlowRemindExecutor(userInfo,busId,busType);
	}

	/**
	 * 表单控件值转字符串
	 *
	 * @param componentKey
	 *            控件类型
	 * @param formFlowDataId
	 *            流程表单数据spFlowInputData主键
	 * @param comId
	 *            单位主键
	 * @param instanceId
	 *            流程实例主键
	 * @return
	 */
	public String formComponentValueToString(String componentKey,
			Integer formFlowDataId, Integer comId, Integer instanceId) {
		String value = null;
		try {
			Object obj = this.getFormComponentValue(componentKey,formFlowDataId,comId,instanceId);
			if(obj instanceof List) {
				@SuppressWarnings("unchecked")
				List<SpFlowOptData> listData = (List<SpFlowOptData>)obj;
				if(!CommonUtil.isNull(listData)) {
					StringBuffer dataVal = new StringBuffer();
					for (SpFlowOptData spFlowOptData : listData) {
						if(null == spFlowOptData.getOptionId()
								|| spFlowOptData.getOptionId() == 0 ){
							continue;
						}
						dataVal.append(spFlowOptData.getContent() + "，");
					}
					dataVal = new StringBuffer(dataVal.substring(0,dataVal.lastIndexOf("，")));
					value = dataVal.toString();
				}
			}else if(obj instanceof SpFlowOptData) {
				SpFlowOptData data =  (SpFlowOptData)obj;
				value = data.getContent();
				if(StringUtils.isEmpty(value)){
					value = data.getContentMore();
				}
			}else if(obj instanceof String) {
				value = (String) obj;
			}
		} catch (Exception e) {
			log.error("审批数据转换业务数据出错！",e);
		}
		return value;
	}

	/**
	 * 删除审批文档
	 * @param userInfo
	 * @param busId
	 * @throws Exception
	 */
	public void delTaskUpfile(Integer upfileId, UserInfo userInfo, Integer busId,Integer addType) throws Exception {

		Upfiles file = (Upfiles)workFlowDao.objectQuery(Upfiles.class,upfileId);
		// 获取流程实例化对象信息
		SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(busId,userInfo);
		// 1、流程引擎向下走一步
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(instance.getActInstaceId(), userInfo);
		if (null != curTask && null != curTask.getTaskDefinitionKey()) {
			//日志描述记录
			String spMsg = "删除文档“"+file.getFilename()+"”。";
			this.addActTaskForLogs(CommonUtil.buildFlowExetutor(userInfo.getComId(),userInfo.getId()),
					curTask.getId(),curTask.getName()+"-删除文档",instance.getActInstaceId(),spMsg);
		}
		if(addType.equals(2)) {//删除留言附件
			workFlowDao.delByField("spFlowTalkUpfile", new String[] { "comId", "busId","busType","upfileId","userId"},
					new Object[] {userInfo.getComId(),busId,ConstantInterface.TYPE_FLOW_SP,upfileId,userInfo.getId()});
		}else {//删除审批附件
			workFlowDao.delByField("spFlowUpfile", new String[] { "comId", "busId","busType","upfileId","userId","addType"},
					new Object[] {userInfo.getComId(),busId,ConstantInterface.TYPE_FLOW_SP,upfileId,userInfo.getId(),addType});
		}
	}

	/**
	 * 实例化会签信息
	 * @param instanceId 流程实例主键
	 * @param modFormStepDataStr 会签配置信息
	 * @param userInfo
	 * @throws Exception
	 */
	public void initSpHuiQian(Integer instanceId,String modFormStepDataStr, UserInfo userInfo) throws Exception {
		// 验证码验证
		JSONObject modFormStepDataObj = JSONObject.parseObject(modFormStepDataStr);
		ModFormStepData modFormStepData = (ModFormStepData)JSONObject.toJavaObject(modFormStepDataObj,
				ModFormStepData.class);

		// 获取流程实例化对象信息
		SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(instanceId,userInfo);
		// 1、流程引擎向下走一步
		Task curTask = cusActivitService.queryCurTaskByTaskAssignee(instance.getActInstaceId(), userInfo);
		// 如果流程步骤信息为NULL时，直接返回
		if (null == curTask || null == curTask.getTaskDefinitionKey()) {
			throw new IllegalArgumentException();
		}
		Integer curStepId = null;// 当前步骤主键声明

		String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
		if (actTaskDefinitionKey.contains("_")
				&& CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])) {
			curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);
		}
		
		//会签人员
		List<UserInfo> jointProcessUsers = modFormStepData.getJointProcessUsers();
		Set<Integer> jointProcessUserSet = new HashSet<Integer>();
		if(null!=jointProcessUsers && !jointProcessUsers.isEmpty()){
			for (UserInfo user : jointProcessUsers) {
				if(!user.getId().equals(userInfo.getId())) {//会签人员不能是自己，默认排除掉
					//添加会签人员；防重
					jointProcessUserSet.add(user.getId());
				}
			}
			List<String> users = new ArrayList<String>();
			List<UserInfo> userListOfHuiQian = new ArrayList<UserInfo>();
			UserInfo huiQianUser = null;
			StringBuilder noticeUserNames = new StringBuilder();
			for (Integer userId : jointProcessUserSet) {
				//同一审批节点，同一人只能有一个会签记录（在办的）；
				huiQianUser = new UserInfo(userInfo.getComId(),userId);
				SpFlowHuiQianInfo existedHuiQianInfo = this.querySpFlowHuiQianInfo(instanceId, huiQianUser);
				if(!CommonUtil.isNull(existedHuiQianInfo)) {//去重
					delHuiQian(existedHuiQianInfo.getActTaskId(),existedHuiQianInfo.getId());
				}

				String userIdStr =CommonUtil.buildFlowExetutor(userInfo.getComId(),userId);
				users.add(userIdStr);
				UserInfo share = userInfoService.getUserBaseInfo(userInfo.getComId(),userId);
				userListOfHuiQian.add(share);
				
				noticeUserNames.append("@");
				noticeUserNames.append(share.getUserName());

				//设置流程执行人
				SpFlowCurExecutor spFlowCurExecutor = new SpFlowCurExecutor();
				spFlowCurExecutor.setComId(userInfo.getComId());
				spFlowCurExecutor.setBusId(instance.getId());
				spFlowCurExecutor.setBusType(ConstantInterface.TYPE_FLOW_SP);
				spFlowCurExecutor.setExecutor(userId);
				spFlowCurExecutor.setExecuteType(ActLinkTypeEnum.HUIQIAN.getValue());
				workFlowDao.add(spFlowCurExecutor);
				//添加会签会签信息
				SpFlowHuiQianInfo huiQianInfo = new SpFlowHuiQianInfo();
				huiQianInfo.setBusId(instanceId);
				huiQianInfo.setBusType(ConstantInterface.TYPE_FLOW_SP);
				huiQianInfo.setComId(userInfo.getComId());
				huiQianInfo.setAssignee(userId);
				huiQianInfo.setStepId(curStepId);
				huiQianInfo.setContent(modFormStepData.getContent());
				workFlowDao.add(huiQianInfo);
			}
			//activities创建会签审批
			cusActivitService.jointProcess(curTask.getId(),users);

			// 创建待办事项
			todayWorksService.addTodayWorks(userInfo.getComId(),ConstantInterface.TYPE_FLOW_SP,instanceId,instance.getFlowName(),userListOfHuiQian,userInfo.getId(),1) ;

			//发送手机短信提醒
			if(!StringUtils.isEmpty(modFormStepData.getMsgSendFlag()) && modFormStepData.getMsgSendFlag().equals(ConstantInterface.MSG_SEND_YES)){
				//单线程池
				ExecutorService pool = ThreadPoolExecutor.getInstance();
				//跟范围人员发送通知消息
				pool.execute(new sendPhoneMsgThread(phoneMsgService, userInfo.getComId(),userListOfHuiQian,
						new Object[]{instance.getFlowName()}, ConstantInterface.MSG_JOB_TO_DO,userInfo.getOptIP()));
			}
			if(null!=userListOfHuiQian && !userListOfHuiQian.isEmpty()){
				//添加审批留言
				SpFlowTalk spFlowTalk = new SpFlowTalk();
				spFlowTalk.setBusId(instanceId);
				spFlowTalk.setBusType(ConstantInterface.TYPE_FLOW_SP);
				spFlowTalk.setParentId(-1);
				spFlowTalk.setContent(modFormStepData.getContent()+ "\r\n"+noticeUserNames);
				spFlowTalk.setSpeaker(userInfo.getId());
				spFlowTalk.setComId(userInfo.getComId());
				this.addSpFlowTalk(spFlowTalk, userInfo);
			}
		}
		
	}

	/**
	 * 审批会签
	 * @param spFlowHuiQianInfo
	 * @param userInfo
	 */
	public void initEndHuiQian(SpFlowHuiQianInfo spFlowHuiQianInfo, UserInfo userInfo) {
		SpFlowInstance instance = workFlowDao.getSpFlowInstanceById(spFlowHuiQianInfo.getBusId(),userInfo);// 获取流程实例化对象信息
		String huiQianContent = spFlowHuiQianInfo.getHuiQianContent();
		SpFlowHuiQianInfo huiQianInfo = this.querySpFlowHuiQianInfo(spFlowHuiQianInfo.getBusId(), userInfo);
		String taskId = huiQianInfo.getActTaskId();
		huiQianInfo.setEndTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		huiQianInfo.setHuiQianContent(huiQianContent);
		try {
			//删除个人待办提醒工作
			todayWorksService.delTodayWorksByOwner(userInfo.getComId(),userInfo.getId(),instance.getId(),ConstantInterface.TYPE_FLOW_SP);
			//变更实例化流程当前办理人
			//依据人员删除当前流程审批人
			this.delSpFlowCurExecutor( userInfo.getComId(), instance.getId(),ConstantInterface.TYPE_FLOW_SP,userInfo.getId(),ActLinkTypeEnum.HUIQIAN.getValue());
			workFlowDao.update("update spFlowHuiQianInfo a set a.huiQianContent=:huiQianContent,a.endTime=:endTime where a.comid=:comId and a.id=:id",
					huiQianInfo);
			cusActivitService.addComment(taskId,instance.getActInstaceId(),huiQianContent);
			cusActivitService.initCommitProcess(taskId, null,null);
		} catch (Exception e) {
			e.printStackTrace();
		}//引擎事项扭转
	}

	/**
	 * 删除activities审批
	 * @param taskId
	 */
	public void delHuiQian(String taskId,Integer huiQianId) {
		SpFlowHuiQianInfo huiQianInfo = (SpFlowHuiQianInfo) workFlowDao.objectQuery(SpFlowHuiQianInfo.class,huiQianId);
		//依据人员删除当前流程审批人
		this.delSpFlowCurExecutor(huiQianInfo.getComId(),huiQianInfo.getBusId(),huiQianInfo.getBusType(),huiQianInfo.getAssignee(),ActLinkTypeEnum.HUIQIAN.getValue());
		workFlowDao.delById(SpFlowHuiQianInfo.class,huiQianId);
		cusActivitService.deleteTask(taskId);
	}

	/**
	 * 依据人员删除当前流程审批人
	 * @param comId 团队主键
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param executor 审批人
	 * @param executeType 审批类型
	 */
	public void delSpFlowCurExecutor(Integer comId,Integer busId,String busType,Integer executor,String executeType) {
		workFlowDao.delByField("spFlowCurExecutor", new String[] { "comId", "busId","busType","executor","executeType"},
				new Object[] {comId,busId,busType,executor,executeType});// 删除实例化流程当前审批人
	}
	/**
	 * 根据activities审批主键获取会签审批集合
	 * @param taskId
	 * @return
	 */
	public List<TaskEntity> queryActivitiesSonTaskList(String taskId){
		// 查询本节点发起的会签审批
		return workFlowDao.queryActivitiesSonTaskList(taskId);
	}

	/**
	 * 获取会签进度集合
	 * @param instaceId
	 * @param userInfo
	 * @return
	 */
	public List<SpFlowHuiQianInfo> listSpHuiQianProcess(Integer instaceId, UserInfo userInfo) {
		return workFlowDao.listSpHuiQianProcess(instaceId,userInfo);
	}

	/**
	 * 查询个人会签信息
	 * @param instanceId
	 * @param userInfo
	 * @return
	 */
	public SpFlowHuiQianInfo querySpFlowHuiQianInfo(Integer instanceId,UserInfo userInfo) {
		return workFlowDao.querySpFlowHuiQianInfo(instanceId,userInfo);
	}
	/**
	 * 根据审批类型获取当前审批人集合
	 * @param comId 团队主键
	 * @param instanceId 流程实例主键
	 * @param executeType 办理类型
	 * @return
	 */
	public List<SpFlowCurExecutor> getSpFlowCurExecutorByExecuteType(Integer comId,Integer instanceId,String executeType) {
		return workFlowDao.getSpFlowCurExecutorByExecuteType(comId, instanceId, executeType);
	}

	/**
	 * 获取所有审批留言
	 * @param instanceId 流程实例主键
	 * @param comId
	 * @return
	 */
	public List<SpFlowTalk> listSpFlowTalk(Integer instanceId, Integer comId) {
		//审批的分页留言信息
		List<SpFlowTalk> listSpFlowTalk = workFlowDao.listSpFlowTalk(instanceId,comId);
		//需要返回的结果信息
		List<SpFlowTalk> list = new ArrayList<SpFlowTalk>();
		if(null!=listSpFlowTalk && !listSpFlowTalk.isEmpty()){
			//查询留言的所有附件信息
			List<SpFlowTalkUpfile> talkUpfiles = workFlowDao.listSpFlowTalkFile(comId, instanceId, null);
			//缓存留言与附件关系集合
			Map<Integer, List<SpFlowTalkUpfile>> map = new HashMap<Integer, List<SpFlowTalkUpfile>>(14);

			//存在附件信息
			if(null!=talkUpfiles && !talkUpfiles.isEmpty()){
				//遍历附件集合，缓存数据
				for(SpFlowTalkUpfile spFlowTalkUpfile:talkUpfiles){
					//留言主键
					Integer spFlowTalkId = spFlowTalkUpfile.getSpFlowTalkId();
					//取得对应的集合信息
					List<SpFlowTalkUpfile> spFlowTalkUpfiles = map.get(spFlowTalkId);
					if(null == spFlowTalkUpfiles){
						spFlowTalkUpfiles = new ArrayList<SpFlowTalkUpfile>();
					}
					//集合添加数据
					spFlowTalkUpfiles.add(spFlowTalkUpfile);
					//缓存数据
					map.put(spFlowTalkId, spFlowTalkUpfiles);
				}
			}
			//遍历留言信息
			for (SpFlowTalk spFlowTalk : listSpFlowTalk) {
				//留言设置附件关联
				spFlowTalk.setListSpFlowTalkUpfile(map.get(spFlowTalk.getId()));
				list.add(spFlowTalk);
			}
		}
		return list;
	}

	/**
	 * 添加审批留言
	 * @param spFlowTalk
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public Integer addSpFlowTalk(SpFlowTalk spFlowTalk, UserInfo userInfo) throws Exception {
		spFlowTalk.setBusType(ConstantInterface.TYPE_FLOW_SP);
		Integer spFlowTalkId = workFlowDao.add(spFlowTalk);
		//查询审批流程实例信息
		SpFlowInstance spFlowInstance = workFlowDao.getSpFlowInstanceById(spFlowTalk.getBusId(), userInfo);

		Integer[] upfilesId = spFlowTalk.getUpfilesId();
		
		List<SpFlowTalkUpfile> flowTalkUpfiles = spFlowTalk.getListSpFlowTalkUpfile();
		
		if(null!=upfilesId){
			for (Integer upfileId : upfilesId) {
				SpFlowTalkUpfile spFlowTalkUpfile = new SpFlowTalkUpfile();
				//企业编号
				spFlowTalkUpfile.setComId(spFlowTalk.getComId());
				//审批主键
				spFlowTalkUpfile.setBusId(spFlowTalk.getBusId());
				spFlowTalkUpfile.setBusType(ConstantInterface.TYPE_FLOW_SP);
				//留言的主键
				spFlowTalkUpfile.setSpFlowTalkId(spFlowTalkId);
				//上传人
				spFlowTalkUpfile.setUserId(spFlowTalk.getSpeaker());
				//文件主键
				spFlowTalkUpfile.setUpfileId(upfileId);

				workFlowDao.add(spFlowTalkUpfile);
				//为附件创建索引
				uploadService.updateUpfileIndex(upfileId,userInfo,"add",spFlowTalk.getBusId(),ConstantInterface.TYPE_FLOW_SP);
			}
			//添加到文档中心
			fileCenterService.addModFile(userInfo, Arrays.asList(upfilesId),spFlowInstance.getFlowName());
		}else if(null!=flowTalkUpfiles && !flowTalkUpfiles.isEmpty()){
			
			List<Integer> fileList = new ArrayList<>();
			
			for (SpFlowTalkUpfile spFlowTalkUpfile : flowTalkUpfiles) {
				Integer upfileId = spFlowTalkUpfile.getUpfileId();
				fileList.add(upfileId);
				//企业编号
				spFlowTalkUpfile.setComId(spFlowTalk.getComId());
				//审批主键
				spFlowTalkUpfile.setBusId(spFlowTalk.getBusId());
				spFlowTalkUpfile.setBusType(ConstantInterface.TYPE_FLOW_SP);
				//留言的主键
				spFlowTalkUpfile.setSpFlowTalkId(spFlowTalkId);
				//上传人
				spFlowTalkUpfile.setUserId(spFlowTalk.getSpeaker());
				//文件主键
				spFlowTalkUpfile.setUpfileId(upfileId);

				workFlowDao.add(spFlowTalkUpfile);
				//为附件创建索引
				uploadService.updateUpfileIndex(upfileId,userInfo,"add",spFlowTalk.getBusId(),ConstantInterface.TYPE_FLOW_SP);
			}
			//添加到文档中心
			fileCenterService.addModFile(userInfo, fileList,spFlowInstance.getFlowName());
		}

		//需发待办事项的人员集合
		List<UserInfo> toDoUsers = new ArrayList<UserInfo>();
		//@添加信息分享人员
		List<SpFlowNoticeUser> listSpFlowNoticeUser = spFlowTalk.getListSpFlowNoticeUser();
		Set<Integer> pushUserIdSet = new HashSet<Integer>();
		if(null != listSpFlowNoticeUser && !listSpFlowNoticeUser.isEmpty()){
			for (SpFlowNoticeUser spFlowNoticeUser : listSpFlowNoticeUser) {
				//把@人员添加到需办理事项的人员集合中
				toDoUsers.add(new UserInfo(userInfo.getComId(),spFlowNoticeUser.getNoticeUserId()));
				//人员主键
				Integer userId = spFlowNoticeUser.getNoticeUserId();
				pushUserIdSet.add(userId);
				//删除上次的分享人员
				workFlowDao.delByField("spFlowNoticeUser", new String[]{"comId","busId","busType","noticeUserId"},
						new Object[]{userInfo.getComId(),spFlowTalk.getBusId(),ConstantInterface.TYPE_FLOW_SP,userId});
				spFlowNoticeUser.setBusId(spFlowTalk.getBusId());
				spFlowNoticeUser.setBusType(ConstantInterface.TYPE_FLOW_SP);
				spFlowNoticeUser.setComId(userInfo.getComId());
                workFlowDao.add(spFlowNoticeUser);
			}
		}

		if (!CommonUtil.isNull(spFlowInstance)) {
			//查询消息的推送人员
			List<UserInfo> relatedUsers = workFlowDao.listSpFlowUserForMsg(userInfo.getComId(), spFlowTalk.getBusId(),true);
			//关系人合并
			toDoUsers.addAll(relatedUsers);
			Iterator<UserInfo> userids =  toDoUsers.iterator();
			for(;userids.hasNext();){
				UserInfo user = userids.next();
				if(user.getId().equals(userInfo.getId())){
					userids.remove();
					toDoUsers.remove(user);
				}
			}
			// 添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_FLOW_SP, spFlowTalk.getBusId(), "参与审批讨论:" + spFlowTalk.getContent(),
					toDoUsers, userInfo.getId(), 1);
			//清除上次查看记录
			viewRecordService.delViewRecord(userInfo,toDoUsers,spFlowTalk.getBusId(),ConstantInterface.TYPE_FLOW_SP);
		}

		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_FLOW_SPTALK,"添加审批留言",spFlowTalk.getBusId());
		//更新审批索引
		this.updateSpFlowInstanceIndex(spFlowTalk.getBusId(),userInfo,"update");
		return spFlowTalkId;
	}

	/**
	 * 获取审批留言信息（单条）
	 * @param id
	 * @param comId
	 * @return
	 */
	public SpFlowTalk querySpFlowTalk(Integer id, Integer comId) {
		SpFlowTalk spFlowTalk = workFlowDao.querySpFlowTalk(id,comId);
		//审批留言的附件
		if(null!=spFlowTalk){
			spFlowTalk.setListSpFlowTalkUpfile(workFlowDao.listSpFlowTalkFile(comId,spFlowTalk.getBusId(),id));
		}
		return spFlowTalk;
	}

	/**
	 * 删除审批留言
	 * @param spFlowTalk
	 * @param userInfo
	 * @throws Exception
	 */
	public void delSpFlowTalk(SpFlowTalk spFlowTalk, UserInfo userInfo) throws Exception {
		//查询审批流程实例信息
		SpFlowInstance spFlowInstance = workFlowDao.getSpFlowInstanceById(spFlowTalk.getBusId(), userInfo);
		if("yes".equals(spFlowTalk.getDelChildNode())){
			//留言的附件
			List<SpFlowTalk> spFlowTalks = workFlowDao.listSpFlowTalk(spFlowTalk.getBusId(),spFlowTalk.getComId());
			for (SpFlowTalk spFlowTalkSingle : spFlowTalks) {
				//删除留言附件
				workFlowDao.delByField("spFlowTalkUpfile", new String[]{"comId","spFlowTalkId"},
						new Object[]{spFlowTalkSingle.getComId(),spFlowTalkSingle.getId()});
			}
			//删除当前节点及其子节点回复
			workFlowDao.delSpFlowTalk(spFlowTalk.getId(),spFlowTalk.getComId());
		}else{
			//删除留言附件
			workFlowDao.delByField("spFlowTalkUpfile", new String[]{"comId","spFlowTalkId"},
					new Object[]{spFlowTalk.getComId(),spFlowTalk.getId()});

			//把子节点的parentId向上提一级
			workFlowDao.updateSpFlowTalkParentId(spFlowTalk.getId(),spFlowTalk.getComId());
			//删除当前节点
			workFlowDao.delByField("spFlowTalk", new String[]{"comId","id"},new Integer[]{spFlowTalk.getComId(),spFlowTalk.getId()});
		}

		//审批的负责人，执行人以及关注人员
		List<UserInfo> shares = workFlowDao.listSpFlowUserForMsg(userInfo.getComId(), spFlowTalk.getBusId(),true);
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,spFlowInstance.getExecutor(), spFlowTalk.getBusId(), "删除留言", ConstantInterface.TYPE_FLOW_SP, shares,null);

		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_TALKDEL,
				"删除审批留言",spFlowTalk.getBusId());
		//更新审批索引
		this.updateSpFlowInstanceIndex(spFlowTalk.getBusId(),userInfo,"update");
	}

	/**
	 * 讨论回复DIV字符串生存
	 * @param spFlowTalk
	 * @param uploadFileName
	 * @param uploadFileShowName
	 * @param pifreamId
	 * @return
	 */
	public String replyTalkDivString(SpFlowTalk spFlowTalk,String uploadFileName,String uploadFileShowName,String pifreamId,UserInfo userInfo,String sid){
		if(null==spFlowTalk){
			return null;
		}
		//是子节点
		spFlowTalk.setIsLeaf(1);
		StringBuffer divString = new StringBuffer();

		if(spFlowTalk.getParentId().equals(-1)){//是留言
			divString.append("\n <div id='talk_"+spFlowTalk.getId()+"' class='ws-shareBox'>");
		}else{//是回复
			divString.append("\n <div id='talk_"+spFlowTalk.getId()+"' class='ws-shareBox ws-shareBox2'>");
		}
		//头像
		divString.append("\n <div class='shareHead' data-container='body' data-toggle='popover'data-user='"+userInfo.getId()+"' data-busId='"+spFlowTalk.getBusId()+"' data-busType='"+ConstantInterface.TYPE_FLOW_SP+"'>");

		divString.append("<img src=\"/downLoad/userImg/"+spFlowTalk.getComId()+"/"+spFlowTalk.getSpeaker()+"?sid="+sid+"\" title=\""+spFlowTalk.getSpeakerName()+"\"></img>");
		divString.append("\n </div>");
		//头像结束

		divString.append("\n <div class='shareText'>");
		divString.append("\n 	<span class='ws-blue'>"+spFlowTalk.getSpeakerName()+"</span>");
		if(!spFlowTalk.getParentId().equals(-1)){//是留言
			divString.append("\n<r>回复</r>");
			divString.append("\n<span class='ws-blue'>"+spFlowTalk.getpSpeakerName()+"</span>");

		}
		divString.append("\n<p class='ws-texts'>"+StringUtil.toHtml(spFlowTalk.getContent())+"</p>");

		//附件
		List<SpFlowTalkUpfile> list = spFlowTalk.getListSpFlowTalkUpfile();
		if(null!=list && list.size()>0){
			divString.append("<div class=\"file_div\">");
			for (int i=0;i<list.size();i++) {
				SpFlowTalkUpfile upfiles = list.get(i);
				if("1".equals(upfiles.getIsPic())){
					divString.append("<p class=\"p_text\">");
					divString.append("附件（"+(i+1)+"）：");
					divString.append("<img onload=\"AutoResizeImage(350,0,this,'otherTaskAttrIframe')\"");
					divString.append("src=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid="+sid+"\" />");
					divString.append("&nbsp;<a class=\"fa fa-download\" title=\"下载\" href=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid="+sid+"\"></a>");
					divString.append("&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"showPic('/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"','"+sid+"','"+upfiles.getUpfileId()+"','"+ConstantInterface.TYPE_FLOW_SP+"','"+spFlowTalk.getBusId()+"')\"></a>");
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
						divString.append("&nbsp;<a class=\"fa fa-download\" href=\"javascript:void(0);\" title=\"下载\" onclick=\"downLoad('"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+sid+"')\"></a>");
						divString.append("&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"viewOfficePage('"+upfiles.getUpfileId()+"','"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+upfiles.getFileExt()+"','"+sid+"','"+ConstantInterface.TYPE_FLOW_SP+"','"+spFlowTalk.getBusId()+"')\"></a>");
					}else if(upfiles.getFileExt().equals("pdf")||upfiles.getFileExt().equals("txt")){
						divString.append("&nbsp;<a class=\"fa fa-download\" title=\"下载\" href=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid="+sid+"\"></a>");
						divString.append("&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"viewOfficePage('"+upfiles.getUpfileId()+"','"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+upfiles.getFileExt()+"','"+sid+"','"+ConstantInterface.TYPE_FLOW_SP+"','"+spFlowTalk.getBusId()+"')\"></a>");
					}else{
						divString.append("&nbsp;<a class=\"fa fa-download\" href=\"javascript:void(0);\" title=\"下载\" onclick=\"downLoad('"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+sid+"')\"></a>");
					}
				}
				divString.append("</p>");
			}
			divString.append("</div>");
		}
		divString.append("\n 	<div class='ws-type'>");
		//判断是否有编辑权限
		ModuleOperateConfig modOptConf = modOptConfService.getModuleOperateConfig(userInfo.getComId(),
				ConstantInterface.TYPE_FLOW_SP, "delete");
		//发言人可以删除自己的发言
		if(userInfo.getId().equals(spFlowTalk.getSpeaker()) && null==modOptConf){
			divString.append("\n 	<a href='javascript:void(0);' id=\"delOpt_"+spFlowTalk.getId()+"\" class='fa fa-trash-o' title='删除' onclick=\"delTalk('"+spFlowTalk.getId()+"','1')\"></a>");
		}
		//项目没有办结可以讨论
		divString.append("\n 	<a id=\"img_"+spFlowTalk.getId()+"\" name=\"replyImg\" href=\"javascript:void(0);\" class=\"fa fa-comment-o\" title=\"回复\" onclick=\"showArea('"+spFlowTalk.getId()+"')\"></a>");
		divString.append("\n 		<time>"+spFlowTalk.getRecordCreateTime()+"</time>");
		divString.append("\n 		</div>");
		divString.append("\n 	</div>");
		divString.append("\n 	<div class=\"ws-clear\"></div>");
		divString.append("\n </div>");
		//回复层
		divString.append("\n <div id=\"reply_"+spFlowTalk.getId()+"\" name=\"replyTalk\" style=\"display:none;\" class=\"ws-shareBox ws-shareBox2 ws-shareBox3\">");
		divString.append("\n 	<div class=\"shareText\">");
		divString.append("\n 		<div class=\"ws-textareaBox\" style=\"margin-top:10px;\">");
		divString.append("\n 			<textarea id=\"operaterReplyTextarea_"+spFlowTalk.getId()+"\" name=\"operaterReplyTextarea_"+spFlowTalk.getId()+"\" class=\"form-control\" placeholder=\"回复……\"></textarea>");
		divString.append("\n 			<div class=\"ws-otherBox\">");
		divString.append("\n 				<div class=\"ws-meh\">");
		//表情
		divString.append("\n 					<a href=\"javascript:void(0);\" class=\"fa fa-meh-o tigger\" id=\"biaoQingSwitch_"+spFlowTalk.getId()+"\" onclick=\"addBiaoQingObj('biaoQingSwitch_"+spFlowTalk.getId()+"','biaoQingDiv_"+spFlowTalk.getId()+"','operaterReplyTextarea_"+spFlowTalk.getId()+"');\"></a>");
		//表情DIV层
		divString.append("\n 					<div id=\"biaoQingDiv_"+spFlowTalk.getId()+"\" class=\"blk\" style=\"display:none;position:absolute;width:200px;top:100px;z-index:99;left: 15px\">");
		divString.append("\n 						<div class=\"main\">");
		divString.append("\n 							<ul style=\"padding: 0px\">");
		divString.append(CommonUtil.biaoQingStr());
		divString.append("\n 							</ul>");
		divString.append("\n 						</div>");
		divString.append("\n 					</div>");
		divString.append("\n 				</div>");
		//常用意见
		divString.append("\n 				<div class=\"ws-plugs\">");
		divString.append("\n 					<a href=\"javascript:void(0);\" class=\"fa fa-comments-o\" onclick=\"addIdea('operaterReplyTextarea_"+spFlowTalk.getId()+"','"+sid+"');\" title=\"常用意见\"></a>");
		divString.append("\n 				</div>");
		//@机制
		divString.append("\n				<div class=\"ws-plugs\">");
		divString.append("\n					<a class=\"btn-icon\" title=\"告知人员\"  href=\"javascript:void(0)\" data-todoUser=\"yes\" data-relateDiv=\"todoUserDiv_" +spFlowTalk.getId()+ "\">@</a>");
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
		divString.append("\n					<button type=\"button\" class=\"btn btn-info ws-btnBlue\" data-relateTodoDiv=\"todoUserDiv_" + spFlowTalk.getId() + "\" onclick=\"replyTalk(" + spFlowTalk.getBusId() + "," + spFlowTalk.getId()+",this)\">回复</button>");
		divString.append("\n				</div>");
		divString.append("\n				<div style=\"clear: both;\"></div>");
		//@机制
		divString.append("\n				<div id=\"todoUserDiv_" + spFlowTalk.getId() + "\" class=\"padding-top-10\"> ");
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
	 * 查询数据
	 * @param userInfo
	 * @param instanceId
	 */
	public List<Consume> listLayoutDetailForConsume(UserInfo userInfo, Integer instanceId) {
		List<Consume> consumes = new ArrayList<Consume>();
		List<LayoutDetail> layoutDetails = formService.listLayoutDetailForConsume(userInfo,instanceId);
		if(null!=layoutDetails && !layoutDetails.isEmpty()){
            Map<Integer,LayoutDetail> listFieldId = new HashMap<Integer,LayoutDetail>(14);
			for (LayoutDetail layoutDetail : layoutDetails) {
				listFieldId.put(layoutDetail.getFieldId(),layoutDetail);
			}

			//遍历spFlowRelateData 存formFlowDataId
			//重置默认未销账
			Set<Integer> formFlowDataIdSet = new HashSet<>();
			Set<Integer> consumeIds = new HashSet<>();
			List<SpFlowRelateData> preFlowRelateDatas = workFlowDao.listRelateDataForConsume(instanceId, userInfo.getComId());
			for(SpFlowRelateData spFlowRelateData : preFlowRelateDatas){
				Integer busId = spFlowRelateData.getBusId();
				if(!consumeIds.contains(busId)){
					Consume consume = consumeService.getConsumeById(spFlowRelateData.getBusId());
					consumes.add(consume);
					consumeIds.add(busId);
				}
				formFlowDataIdSet.add(spFlowRelateData.getFormFlowDataId());
			}


			//表单所有数据
			List<SpFlowInputData> spFlowInputDatas = workFlowDao.listSpFormDatas(
					instanceId, userInfo.getComId());
			if (null != spFlowInputDatas && !spFlowInputDatas.isEmpty()) {
				Integer preIndex = -1;
				// 构建表单数据详情
				Map<String,String> column = CommonUtil.getSysColumn("Consume");
				
				Map<Integer,Consume> map = new HashMap<Integer,Consume>();
				//对比完后是否需要添加consume
				Integer subIncex = 0;
				for (SpFlowInputData spFlowInputData : spFlowInputDatas) {
					Integer fieldId = spFlowInputData.getFieldId();

					LayoutDetail layoutDetail = listFieldId.get(fieldId);
					if(null == layoutDetail){
						continue;
					}
					if(formFlowDataIdSet.contains(spFlowInputData.getId())){
						continue;
					}
					
					Consume consume = map.get(subIncex);
					if(null == consume) {
						consume = new Consume();
					}
					
					String content = spFlowInputData.getContent() == null ? "" : spFlowInputData.getContent();

					String componentKey = spFlowInputData.getComponentKey();
					if ("TextArea".equals(componentKey)) {// 构建文本框数据
						List<SpFlowOptData> spFlowOptDatas = workFlowDao.listSpFormOpts(instanceId,spFlowInputData.getId(),userInfo.getComId());
						if (null != spFlowOptDatas && !spFlowOptDatas.isEmpty()) {
							content = spFlowOptDatas.get(0).getContentMore();
						}
					}else{
						// 流程表单数据
						List<SpFlowOptData> spFlowOptDatas = workFlowDao
								.listSpFormOpts(instanceId, spFlowInputData.getId(),
										userInfo.getComId());
						if (null != spFlowOptDatas && spFlowOptDatas.size() > 0) {
							for (SpFlowOptData spFlowOptData : spFlowOptDatas) {
								// 设置选项内容
									content = content + spFlowOptData.getContent();
							}
						}
					}
					Map<String,String> columns = new HashMap<>(14);
					String tableColumn = layoutDetail.getTableColumn().toLowerCase();
					if(tableColumn.equals("type")){
						consume.setTypeName(content);
					}else{
						columns.put(column.get(tableColumn),content);
						BeanRefUtil.setFieldValue(consume,columns);
					}
					
					map.put(subIncex, consume);

					Integer dataIndex = spFlowInputData.getDataIndex();
					if(!preIndex.equals(dataIndex) ){
						subIncex ++;
						preIndex = dataIndex ;
					}
				}
				
				for (int index = 0 ;index<=subIncex;index++) {
					Consume consume = map.get(index);
					if(null!= consume && StringUtils.isNotEmpty(consume.getTypeName())) {
						consumes.add(consume);
					}
					
				}
			}
		}
		return consumes;
	}
	/**
	 * 添加activities任务留痕
	 * @param assignee
	 * @param curTaskId
	 * @param stepName
	 * @param processInstanceId
	 * @param message
	 * @throws Exception
	 */
	public void addActTaskForLogs(String assignee,String  curTaskId,String  stepName,
			String processInstanceId,String  message) throws Exception {
		cusActivitService.addTaskForLogs(assignee, curTaskId, stepName, processInstanceId, message);
	}

	/**
	 * 根据id获取spfflowmodel
	 * @param id
	 * @return
	 */
	public SpFlowModel getSpFlowModelById(Integer id){
		return (SpFlowModel) workFlowDao.objectQuery(SpFlowModel.class,id);
	}

	/**
	 * 分页查询模块关联的审批数据信息
	 * @param sessionUser 当前操作人员
	 * @param flowInstance 流程查询条件
	 * @return
	 */
	public PageBean<SpFlowInstance> listPagedBusSpflow(UserInfo sessionUser,
			SpFlowInstance flowInstance) {
		return workFlowDao.listPagedBusSpflow(sessionUser,flowInstance);
	}
}
