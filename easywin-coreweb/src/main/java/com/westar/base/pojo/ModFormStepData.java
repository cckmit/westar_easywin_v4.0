package com.westar.base.pojo;

import java.io.Serializable;
import java.util.List;

import com.westar.base.model.SpFlowNoticeUser;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.UserInfo;

/**
 * 表单数据对象
 */
public class ModFormStepData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6549909747194678728L;
	// 提交状态
	private String dataStatus;
	// 审批流程创建人
	private Integer creator;
	// 步骤
	private Integer stepId;
	private Integer curStepId ;//当前流程审批步骤主键
	
	//关联附件
	private List<SpFlowUpfile> spFlowUpfiles;
	
	//会签人员集合
	private List<UserInfo> jointProcessUsers;
	
	//关注状态 0未关注 1已关注
	private String attentionState;

	// 审批意见
	private String spIdea;
	// 审批标识 0不同意 1同意 -1回退
	private Integer spState;

	// 流程模型主键（启动流程时需使用）
	private Integer flowId;
	
	//流程定义时配置文件中配置的主键
	private String activitiTaskId;
	
	//审批的验证码
	private String spYzm;
	
	//审批公文正文的附件Id
	private Integer officalDocFileId;
	
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
	
	//会签说明
	private String content;
	
	//审批转办的人员的新的执行人
	private UserInfo newAssigner;
	
	//是否覆盖会签标识
	private String cover;


	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
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

	public Integer getOfficalDocFileId() {
		return officalDocFileId;
	}

	public void setOfficalDocFileId(Integer officalDocFileId) {
		this.officalDocFileId = officalDocFileId;
	}

	public List<UserInfo> getJointProcessUsers() {
		return jointProcessUsers;
	}

	public void setJointProcessUsers(List<UserInfo> jointProcessUsers) {
		this.jointProcessUsers = jointProcessUsers;
	}

	public Integer getCurStepId() {
		return curStepId;
	}

	public void setCurStepId(Integer curStepId) {
		this.curStepId = curStepId;
	}

	public List<UserInfo> getNextStepUsers() {
		return nextStepUsers;
	}

	public void setNextStepUsers(List<UserInfo> nextStepUsers) {
		this.nextStepUsers = nextStepUsers;
	}

	public String getMsgSendFlag() {
		return msgSendFlag;
	}

	public void setMsgSendFlag(String msgSendFlag) {
		this.msgSendFlag = msgSendFlag;
	}

	public Integer getNextStepId() {
		return nextStepId;
	}

	public void setNextStepId(Integer nextStepId) {
		this.nextStepId = nextStepId;
	}

	public List<SpFlowNoticeUser> getNoticeUsers() {
		return noticeUsers;
	}

	public void setNoticeUsers(List<SpFlowNoticeUser> noticeUsers) {
		this.noticeUsers = noticeUsers;
	}

	public String getMultLoopState() {
		return multLoopState;
	}

	public void setMultLoopState(String multLoopState) {
		this.multLoopState = multLoopState;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UserInfo getNewAssigner() {
		return newAssigner;
	}

	public void setNewAssigner(UserInfo newAssigner) {
		this.newAssigner = newAssigner;
	}

	/**
	 * @return the cover
	 */
	public String getCover() {
		return cover;
	}

	/**
	 * @param cover the cover to set
	 */
	public void setCover(String cover) {
		this.cover = cover;
	}
	

}
