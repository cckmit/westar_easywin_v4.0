package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 流程步骤
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowStep {
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
	* 关联流程主键
	*/
	@Filed
	private Integer flowId;
	/** 
	* 步骤类型
	*/
	@Filed
	private String stepType;
	/** 
	* 流程步骤办理人指定方式
	*/
	@Filed
	private String executorWay;
	/** 
	* 条件比对表达式
	*/
	@Filed
	private String conditionExp;
	/** 
	* 审批权限验证
	*/
	@Filed
	private String spCheckCfg;

	/****************以上主要为系统表字段********************/
	private String nextStepWay;
	private Integer defaultStepId;
	private Integer defaultStep;
	/** 
	* 父步骤主键
	*/
	private Integer pstepId;
	/** 
	* 父步骤名称
	*/
	private String pstepName;
	/** 
	* 层级关系
	*/
	private Integer stepLevel;
	/** 
	* 流程下一步步骤集合
	*/
	private List<SpFlowStep> listNextStep;
	/** 
	* 流程步骤办理人指定方式
	*/
	private String stepExecutorWay;
	/** 
	* 消息告知方式（除了系统通知以外的方式）
	*/
	private String[] noticeWay;
	/** 
	* 下一步步骤主键数组
	*/
	private Integer[] nextStepIds;
	/** 
	* 步骤审批人集合
	*/
	private List<UserInfo> listExecutor;
	/** 
	* 是否是自己的步骤标识；0-不是，1-是
	*/
	private Integer isMine;
	/** 
	* 授权表单控件主键数组
	*/
	private String[] formComponIds;
	/** 
	* 步骤授权表单控件集合
	*/
	private List<SpStepFormControl> listFormCompon;
	/** 
	* 步骤条件数据集
	*/
	private List<SpStepConditions> listSpStepCondition;
	private String stepWay;

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
	* 关联流程主键
	* @param flowId
	*/
	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}

	/** 
	* 关联流程主键
	* @return
	*/
	public Integer getFlowId() {
		return flowId;
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

	public String getNextStepWay() {
		return nextStepWay;
	}

	public void setNextStepWay(String nextStepWay) {
		this.nextStepWay = nextStepWay;
	}

	/** 
	* 父步骤主键
	* @return
	*/
	public Integer getPstepId() {
		return pstepId;
	}

	/** 
	* 父步骤主键
	* @param pstepId
	*/
	public void setPstepId(Integer pstepId) {
		this.pstepId = pstepId;
	}

	/** 
	* 父步骤名称
	* @return
	*/
	public String getPstepName() {
		return pstepName;
	}

	/** 
	* 父步骤名称
	* @param pstepName
	*/
	public void setPstepName(String pstepName) {
		this.pstepName = pstepName;
	}

	/** 
	* 层级关系
	* @return
	*/
	public Integer getStepLevel() {
		return stepLevel;
	}

	/** 
	* 层级关系
	* @param stepLevel
	*/
	public void setStepLevel(Integer stepLevel) {
		this.stepLevel = stepLevel;
	}

	/** 
	* 流程下一步步骤集合
	* @return
	*/
	public List<SpFlowStep> getListNextStep() {
		return listNextStep;
	}

	/** 
	* 流程下一步步骤集合
	* @param listNextStep
	*/
	public void setListNextStep(List<SpFlowStep> listNextStep) {
		this.listNextStep = listNextStep;
	}

	/** 
	* 流程步骤办理人指定方式
	* @return
	*/
	public String getStepExecutorWay() {
		return stepExecutorWay;
	}

	/** 
	* 流程步骤办理人指定方式
	* @param stepExecutorWay
	*/
	public void setStepExecutorWay(String stepExecutorWay) {
		this.stepExecutorWay = stepExecutorWay;
	}

	/** 
	* 消息告知方式（除了系统通知以外的方式）
	* @return
	*/
	public String[] getNoticeWay() {
		return noticeWay;
	}

	/** 
	* 消息告知方式（除了系统通知以外的方式）
	* @param noticeWay
	*/
	public void setNoticeWay(String[] noticeWay) {
		this.noticeWay = noticeWay;
	}

	/** 
	* 下一步步骤主键数组
	* @return
	*/
	public Integer[] getNextStepIds() {
		return nextStepIds;
	}

	/** 
	* 下一步步骤主键数组
	* @param nextStepIds
	*/
	public void setNextStepIds(Integer[] nextStepIds) {
		this.nextStepIds = nextStepIds;
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

	public Integer getDefaultStepId() {
		return defaultStepId;
	}

	public void setDefaultStepId(Integer defaultStepId) {
		this.defaultStepId = defaultStepId;
	}

	/** 
	* 步骤审批人集合
	* @return
	*/
	public List<UserInfo> getListExecutor() {
		return listExecutor;
	}

	/** 
	* 步骤审批人集合
	* @param listExecutor
	*/
	public void setListExecutor(List<UserInfo> listExecutor) {
		this.listExecutor = listExecutor;
	}

	/** 
	* 是否是自己的步骤标识；0-不是，1-是
	* @return
	*/
	public Integer getIsMine() {
		return isMine;
	}

	/** 
	* 是否是自己的步骤标识；0-不是，1-是
	* @param isMine
	*/
	public void setIsMine(Integer isMine) {
		this.isMine = isMine;
	}

	public Integer getDefaultStep() {
		return defaultStep;
	}

	public void setDefaultStep(Integer defaultStep) {
		this.defaultStep = defaultStep;
	}

	/** 
	* 授权表单控件主键数组
	* @return
	*/
	public String[] getFormComponIds() {
		return formComponIds;
	}

	/** 
	* 授权表单控件主键数组
	* @param formComponIds
	*/
	public void setFormComponIds(String[] formComponIds) {
		this.formComponIds = formComponIds;
	}

	/** 
	* 步骤授权表单控件集合
	* @return
	*/
	public List<SpStepFormControl> getListFormCompon() {
		return listFormCompon;
	}

	/** 
	* 步骤授权表单控件集合
	* @param listFormCompon
	*/
	public void setListFormCompon(List<SpStepFormControl> listFormCompon) {
		this.listFormCompon = listFormCompon;
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
	* 步骤条件数据集
	* @return
	*/
	public List<SpStepConditions> getListSpStepCondition() {
		return listSpStepCondition;
	}

	/** 
	* 步骤条件数据集
	* @param listSpStepCondition
	*/
	public void setListSpStepCondition(List<SpStepConditions> listSpStepCondition) {
		this.listSpStepCondition = listSpStepCondition;
	}

	public String getStepWay() {
		return stepWay;
	}

	public void setStepWay(String stepWay) {
		this.stepWay = stepWay;
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
}
