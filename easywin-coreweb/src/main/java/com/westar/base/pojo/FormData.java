package com.westar.base.pojo;

import java.io.Serializable;
import java.util.List;

import com.westar.base.model.FormLayout;
import com.westar.base.model.FormMod;
import com.westar.base.model.SpFlowNoticeUser;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.UserInfo;

/**
 * 表单数据对象
 */
public class FormData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6549909747194678728L;
	// 流程实例化主键
	private Integer instanceId;
	// 流程模板
	private FormMod form;
	// 流程布局
	private FormLayout formLayout;
	// 花费的时间
	private String spentTime;
	// 提交状态
	private String dataStatus;
	// 表单数据详情
	private List<FormDataDetails> dataDetails;
	// 动态数据是否有变动
	private List<FormDataOption> subFormLogs;
	// 动态表单构建的行数
	private List<FormDataOption> maxSubFormIndexs;
	/* 关联模块主键 */
	private Integer busId;
	/* 关联模块类型 */
	private String busType;
	// 关联模块名称
	private String busName;
	// 项目阶段主键
	private Integer stagedItemId;
	// 项目阶段名称
	private String stagedItemName;
	// 审批流程创建人
	private Integer creator;
	// 步骤
	private Integer stepId;

	// 表单填写前次关联的项目阶段主键
	private Integer preStageItemId;
	
	//关联附件
	private List<SpFlowUpfile> spFlowUpfiles;
	
	//关注状态 0未关注 1已关注
	private String attentionState;

	// 审批意见
	private String spIdea;
	// 审批标识 0不同意 1同意 -1回退
	private Integer spState;

	// 流程模型主键（启动流程时需使用）
	private Integer flowId;
	//流程实例化名称
	private String instanceName;
	
	//步骤审批人数组
	private Integer[] spUser;
	
	//流程定义时配置文件中配置的主键
	private String activitiTaskId;
	//审批的验证码
	private String spYzm;
	
	//下一步骤业务处理人员集合
	private List<UserInfo> nextStepUsers;
	
	//下一步骤业务告知处理人员集合
	private List<SpFlowNoticeUser> noticeUsers;
	
	//下一步骤主键信息
	private Integer nextStepId;
	
	//发送短信标识0不发送 1发送
	private String msgSendFlag;
	
	//并联审批 1多人协同办理 0多人认领办理
	private String multLoopState;

	//设备标识
	private String clientSource;

	public FormMod getForm() {
		return form;
	}

	public void setForm(FormMod form) {
		this.form = form;
	}

	public FormLayout getFormLayout() {
		return formLayout;
	}

	public void setFormLayout(FormLayout formLayout) {
		this.formLayout = formLayout;
	}

	public String getSpentTime() {
		return spentTime;
	}

	public void setSpentTime(String spentTime) {
		this.spentTime = spentTime;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public List<FormDataDetails> getDataDetails() {
		return dataDetails;
	}

	public void setDataDetails(List<FormDataDetails> dataDetails) {
		this.dataDetails = dataDetails;
	}

	public List<FormDataOption> getSubFormLogs() {
		return subFormLogs;
	}

	public void setSubFormLogs(List<FormDataOption> subFormLogs) {
		this.subFormLogs = subFormLogs;
	}

	public List<FormDataOption> getMaxSubFormIndexs() {
		return maxSubFormIndexs;
	}

	public void setMaxSubFormIndexs(List<FormDataOption> maxSubFormIndexs) {
		this.maxSubFormIndexs = maxSubFormIndexs;
	}

	public Integer getBusId() {
		return busId;
	}

	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public Integer getStagedItemId() {
		return stagedItemId;
	}

	public void setStagedItemId(Integer stagedItemId) {
		this.stagedItemId = stagedItemId;
	}

	public String getStagedItemName() {
		return stagedItemName;
	}

	public void setStagedItemName(String stagedItemName) {
		this.stagedItemName = stagedItemName;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Integer getStepId() {
		return stepId;
	}

	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	public Integer getPreStageItemId() {
		return preStageItemId;
	}

	public void setPreStageItemId(Integer preStageItemId) {
		this.preStageItemId = preStageItemId;
	}

	public String getSpIdea() {
		return spIdea;
	}

	public void setSpIdea(String spIdea) {
		this.spIdea = spIdea;
	}

	public Integer getSpState() {
		return spState;
	}

	public void setSpState(Integer spState) {
		this.spState = spState;
	}

	public Integer getFlowId() {
		return flowId;
	}

	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}

	public Integer getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	public List<SpFlowUpfile> getSpFlowUpfiles() {
		return spFlowUpfiles;
	}

	public void setSpFlowUpfiles(List<SpFlowUpfile> spFlowUpfiles) {
		this.spFlowUpfiles = spFlowUpfiles;
	}

	public String getAttentionState() {
		return attentionState;
	}

	public void setAttentionState(String attentionState) {
		this.attentionState = attentionState;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public Integer[] getSpUser() {
		return spUser;
	}

	public void setSpUser(Integer[] spUser) {
		this.spUser = spUser;
	}

	public String getActivitiTaskId() {
		return activitiTaskId;
	}

	public void setActivitiTaskId(String activitiTaskId) {
		this.activitiTaskId = activitiTaskId;
	}

	public String getSpYzm() {
		return spYzm;
	}

	public void setSpYzm(String spYzm) {
		this.spYzm = spYzm;
	}

	public String getClientSource() {
		return clientSource;
	}

	public void setClientSource(String clientSource) {
		this.clientSource = clientSource;
	}

	public List<UserInfo> getNextStepUsers() {
		return nextStepUsers;
	}

	public void setNextStepUsers(List<UserInfo> nextStepUsers) {
		this.nextStepUsers = nextStepUsers;
	}

	public Integer getNextStepId() {
		return nextStepId;
	}

	public void setNextStepId(Integer nextStepId) {
		this.nextStepId = nextStepId;
	}

	public String getMsgSendFlag() {
		return msgSendFlag;
	}

	public void setMsgSendFlag(String msgSendFlag) {
		this.msgSendFlag = msgSendFlag;
	}

	public String getMultLoopState() {
		return multLoopState;
	}

	public void setMultLoopState(String multLoopState) {
		this.multLoopState = multLoopState;
	}

	public List<SpFlowNoticeUser> getNoticeUsers() {
		return noticeUsers;
	}

	public void setNoticeUsers(List<SpFlowNoticeUser> noticeUsers) {
		this.noticeUsers = noticeUsers;
	}

}
