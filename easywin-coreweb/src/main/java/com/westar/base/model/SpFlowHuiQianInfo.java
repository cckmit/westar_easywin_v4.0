package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 会签说明表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowHuiQianInfo {
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
	* 流程实例主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String busType;
	/** 
	* 会签步骤主键
	*/
	@Filed
	private Integer stepId;
	/** 
	* 审批人
	*/
	@Filed
	private Integer assignee;
	/** 
	* 会签说明
	*/
	@Filed
	private String content;
	/** 
	* 会签意见
	*/
	@Filed
	private String huiQianContent;
	/** 
	* 完成时间
	*/
	@Filed
	private String endTime;

	/****************以上主要为系统表字段********************/
	/** 
	* activities任务主键
	*/
	private String actTaskId;
	/** 
	* 人员名字
	*/
	private String userName;
	/** 
	* 流程名称
	*/
	private String flowName;
	/** 
	* 发送短信标识
	*/
	private String msgSendFlag;

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
	* 流程实例主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 流程实例主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 会签步骤主键
	* @param stepId
	*/
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	/** 
	* 会签步骤主键
	* @return
	*/
	public Integer getStepId() {
		return stepId;
	}

	/** 
	* 审批人
	* @param assignee
	*/
	public void setAssignee(Integer assignee) {
		this.assignee = assignee;
	}

	/** 
	* 审批人
	* @return
	*/
	public Integer getAssignee() {
		return assignee;
	}

	/** 
	* 会签说明
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 会签说明
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 会签意见
	* @return
	*/
	public String getHuiQianContent() {
		return huiQianContent;
	}

	/** 
	* 会签意见
	* @param huiQianContent
	*/
	public void setHuiQianContent(String huiQianContent) {
		this.huiQianContent = huiQianContent;
	}

	/** 
	* 完成时间
	* @return
	*/
	public String getEndTime() {
		return endTime;
	}

	/** 
	* 完成时间
	* @param endTime
	*/
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/** 
	* activities任务主键
	* @return
	*/
	public String getActTaskId() {
		return actTaskId;
	}

	/** 
	* activities任务主键
	* @param actTaskId
	*/
	public void setActTaskId(String actTaskId) {
		this.actTaskId = actTaskId;
	}

	/** 
	* 人员名字
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 人员名字
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 流程名称
	* @return
	*/
	public String getFlowName() {
		return flowName;
	}

	/** 
	* 流程名称
	* @param flowName
	*/
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/** 
	* 发送短信标识
	* @return
	*/
	public String getMsgSendFlag() {
		return msgSendFlag;
	}

	/** 
	* 发送短信标识
	* @param msgSendFlag
	*/
	public void setMsgSendFlag(String msgSendFlag) {
		this.msgSendFlag = msgSendFlag;
	}
}
