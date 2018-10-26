package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 流程实例步骤
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowHiStep {
	/** 
	* id主键
	*/
	@Identity
	private Integer id;
	/** 
	* 记录创建时间
	*/
	@DefaultFiled
	private String recordCreateTime;
	/** 
	* 企业编号
	*/
	@Filed
	private Integer comId;
	/** 
	* 步骤名称
	*/
	@Filed
	private String stepName;
	/** 
	* 步骤类型
	*/
	@Filed
	private String stepType;
	/** 
	* 步骤主键
	*/
	@Filed
	private Integer stepId;
	/** 
	* 流程步骤办理人指定方式
	*/
	@Filed
	private String executorWay;
	/** 
	* 审批权限验证
	*/
	@Filed
	private String spCheckCfg;
	/** 
	* 条件比对表达式
	*/
	@Filed
	private String conditionExp;
	/** 
	* 流程实例化主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 业务类型
	*/
	@Filed
	private String busType;

	/****************以上主要为系统表字段********************/
	/** 
	* 默认步骤
	*/
	private Integer defaultStepState;
	/** 
	* 当前审批主键
	*/
	private Integer executor;
	/** 
	* 审批人姓名
	*/
	private String executorName;
	/** 
	* 性别
	*/
	private Integer gender;
	/** 
	* uuid
	*/
	private String uuid;
	/** 
	* 文件名称
	*/
	private String fileName;
	/** 
	* 步骤开始时间
	*/
	private String startTime;
	/** 
	* 步骤审批完成时间
	*/
	private String endTime;
	/** 
	* 审批用时
	*/
	private Long usedTime;
	/** 
	* 审批意见
	*/
	private String spMsg;
	/** 
	* activitiTaskId(部署时主键)
	*/
	private String activitiTaskId;
	/** 
	* activitiRunTaskId(执行时任务主键)
	*/
	private String activitiRunTaskId;
	/** 
	* 步骤执行人
	*/
	private String assignee;
	/** 
	* 当前步步骤信息
	*/
	private SpFlowHiStep curStepInfo;
	/** 
	* 下一步骤信息
	*/
	private SpFlowHiStep nextStepInfo;
	/** 
	* 下一步骤集合
	*/
	private List<SpFlowHiStep> nextStepInfoList;
	/** 
	* 步骤操作办理人信息
	*/
	private List<UserInfo> listSpFlowHiExecutor;
	/** 
	* 步骤条件信息
	*/
	private List<SpFlowStepConditions> listStepConditions;

	/****************以上为自己添加字段********************/
	/** 
	* id主键
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/** 
	* id主键
	* @return
	*/
	public Integer getId() {
		return id;
	}

	/** 
	* 记录创建时间
	* @param recordCreateTime
	*/
	public void setRecordCreateTime(String recordCreateTime) {
		this.recordCreateTime = recordCreateTime;
	}

	/** 
	* 记录创建时间
	* @return
	*/
	public String getRecordCreateTime() {
		return recordCreateTime;
	}

	/** 
	* 企业编号
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 企业编号
	* @return
	*/
	public Integer getComId() {
		return comId;
	}

	/** 
	* 步骤名称
	* @param stepName
	*/
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	/** 
	* 步骤名称
	* @return
	*/
	public String getStepName() {
		return stepName;
	}

	/** 
	* 步骤类型
	* @param stepType
	*/
	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	/** 
	* 步骤类型
	* @return
	*/
	public String getStepType() {
		return stepType;
	}

	/** 
	* 步骤主键
	* @param stepId
	*/
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	/** 
	* 步骤主键
	* @return
	*/
	public Integer getStepId() {
		return stepId;
	}

	/** 
	* 当前审批主键
	* @return
	*/
	public Integer getExecutor() {
		return executor;
	}

	/** 
	* 当前审批主键
	* @param executor
	*/
	public void setExecutor(Integer executor) {
		this.executor = executor;
	}

	/** 
	* 审批人姓名
	* @return
	*/
	public String getExecutorName() {
		return executorName;
	}

	/** 
	* 审批人姓名
	* @param executorName
	*/
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	/** 
	* 性别
	* @return
	*/
	public Integer getGender() {
		return gender;
	}

	/** 
	* 性别
	* @param gender
	*/
	public void setGender(Integer gender) {
		this.gender = gender;
	}

	/** 
	* uuid
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* uuid
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** 
	* 文件名称
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 文件名称
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	* 步骤开始时间
	* @return
	*/
	public String getStartTime() {
		return startTime;
	}

	/** 
	* 步骤开始时间
	* @param startTime
	*/
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/** 
	* 步骤审批完成时间
	* @return
	*/
	public String getEndTime() {
		return endTime;
	}

	/** 
	* 步骤审批完成时间
	* @param endTime
	*/
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/** 
	* 审批用时
	* @return
	*/
	public Long getUsedTime() {
		return usedTime;
	}

	/** 
	* 审批用时
	* @param usedTime
	*/
	public void setUsedTime(Long usedTime) {
		this.usedTime = usedTime;
	}

	/** 
	* 审批意见
	* @return
	*/
	public String getSpMsg() {
		return spMsg;
	}

	/** 
	* 审批意见
	* @param spMsg
	*/
	public void setSpMsg(String spMsg) {
		this.spMsg = spMsg;
	}

	/** 
	* activitiTaskId(部署时主键)
	* @return
	*/
	public String getActivitiTaskId() {
		return activitiTaskId;
	}

	/** 
	* activitiTaskId(部署时主键)
	* @param activitiTaskId
	*/
	public void setActivitiTaskId(String activitiTaskId) {
		this.activitiTaskId = activitiTaskId;
	}

	/** 
	* 步骤执行人
	* @return
	*/
	public String getAssignee() {
		return assignee;
	}

	/** 
	* 步骤执行人
	* @param assignee
	*/
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	/** 
	* 流程步骤办理人指定方式
	* @param executorWay
	*/
	public void setExecutorWay(String executorWay) {
		this.executorWay = executorWay;
	}

	/** 
	* 流程步骤办理人指定方式
	* @return
	*/
	public String getExecutorWay() {
		return executorWay;
	}

	/** 
	* 审批权限验证
	* @param spCheckCfg
	*/
	public void setSpCheckCfg(String spCheckCfg) {
		this.spCheckCfg = spCheckCfg;
	}

	/** 
	* 审批权限验证
	* @return
	*/
	public String getSpCheckCfg() {
		return spCheckCfg;
	}

	/** 
	* 当前步步骤信息
	* @return
	*/
	public SpFlowHiStep getCurStepInfo() {
		return curStepInfo;
	}

	/** 
	* 当前步步骤信息
	* @param curStepInfo
	*/
	public void setCurStepInfo(SpFlowHiStep curStepInfo) {
		this.curStepInfo = curStepInfo;
	}

	/** 
	* 下一步骤信息
	* @return
	*/
	public SpFlowHiStep getNextStepInfo() {
		return nextStepInfo;
	}

	/** 
	* 下一步骤信息
	* @param nextStepInfo
	*/
	public void setNextStepInfo(SpFlowHiStep nextStepInfo) {
		this.nextStepInfo = nextStepInfo;
	}

	/** 
	* 下一步骤集合
	* @return
	*/
	public List<SpFlowHiStep> getNextStepInfoList() {
		return nextStepInfoList;
	}

	/** 
	* 下一步骤集合
	* @param nextStepInfoList
	*/
	public void setNextStepInfoList(List<SpFlowHiStep> nextStepInfoList) {
		this.nextStepInfoList = nextStepInfoList;
	}

	/** 
	* 步骤操作办理人信息
	* @return
	*/
	public List<UserInfo> getListSpFlowHiExecutor() {
		return listSpFlowHiExecutor;
	}

	/** 
	* 步骤操作办理人信息
	* @param listSpFlowHiExecutor
	*/
	public void setListSpFlowHiExecutor(List<UserInfo> listSpFlowHiExecutor) {
		this.listSpFlowHiExecutor = listSpFlowHiExecutor;
	}

	/** 
	* 条件比对表达式
	* @param conditionExp
	*/
	public void setConditionExp(String conditionExp) {
		this.conditionExp = conditionExp;
	}

	/** 
	* 条件比对表达式
	* @return
	*/
	public String getConditionExp() {
		return conditionExp;
	}

	/** 
	* 步骤条件信息
	* @return
	*/
	public List<SpFlowStepConditions> getListStepConditions() {
		return listStepConditions;
	}

	/** 
	* 步骤条件信息
	* @param listStepConditions
	*/
	public void setListStepConditions(List<SpFlowStepConditions> listStepConditions) {
		this.listStepConditions = listStepConditions;
	}

	/** 
	* 流程实例化主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 流程实例化主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 业务类型
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 业务类型
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* activitiRunTaskId(执行时任务主键)
	* @return
	*/
	public String getActivitiRunTaskId() {
		return activitiRunTaskId;
	}

	/** 
	* activitiRunTaskId(执行时任务主键)
	* @param activitiRunTaskId
	*/
	public void setActivitiRunTaskId(String activitiRunTaskId) {
		this.activitiRunTaskId = activitiRunTaskId;
	}

	/** 
	* 默认步骤
	* @return
	*/
	public Integer getDefaultStepState() {
		return defaultStepState;
	}

	/** 
	* 默认步骤
	* @param defaultStepState
	*/
	public void setDefaultStepState(Integer defaultStepState) {
		this.defaultStepState = defaultStepState;
	}
}
