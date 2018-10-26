package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 办理时限报延申请
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DelayApply {
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
	* 所属任务主键
	*/
	@Filed
	private Integer taskId;
	/** 
	* 报延人ID
	*/
	@Filed
	private Integer fromUser;
	/** 
	* 审核人ID
	*/
	@Filed
	private Integer toUser;
	/** 
	* 报延日期
	*/
	@Filed
	private String limitDate;
	/** 
	* 报延原因
	*/
	@Filed
	private String reason;
	/** 
	* 0-不同意，1-同意
	*/
	@Filed
	private Integer status;
	/** 
	* 审批意见
	*/
	@Filed
	private String spOpinion;

	/****************以上主要为系统表字段********************/
	/** 
	* 任务名称
	*/
	private String taskName;
	private String fromUserName;
	/** 
	* 申请人0女1男
	*/
	private String fromUserGender;
	/** 
	* 申请人附件UUID
	*/
	private String fromUserUuid;
	/** 
	* 申请人附件名称
	*/
	private String fromUserFileName;
	private String toUserName;
	/** 
	* 审核人0女1男
	*/
	private String toUserGender;
	/** 
	* 审核人附件UUID
	*/
	private String toUserUuid;
	/** 
	* 审核人附件名称
	*/
	private String toUserFileName;
	/** 
	* 任务版本
	*/
	private String taskVersion;

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
	* 所属任务主键
	* @param taskId
	*/
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	/** 
	* 所属任务主键
	* @return
	*/
	public Integer getTaskId() {
		return taskId;
	}

	/** 
	* 报延人ID
	* @param fromUser
	*/
	public void setFromUser(Integer fromUser) {
		this.fromUser = fromUser;
	}

	/** 
	* 报延人ID
	* @return
	*/
	public Integer getFromUser() {
		return fromUser;
	}

	/** 
	* 审核人ID
	* @param toUser
	*/
	public void setToUser(Integer toUser) {
		this.toUser = toUser;
	}

	/** 
	* 审核人ID
	* @return
	*/
	public Integer getToUser() {
		return toUser;
	}

	/** 
	* 报延日期
	* @param limitDate
	*/
	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}

	/** 
	* 报延日期
	* @return
	*/
	public String getLimitDate() {
		return limitDate;
	}

	/** 
	* 报延原因
	* @param reason
	*/
	public void setReason(String reason) {
		this.reason = reason;
	}

	/** 
	* 报延原因
	* @return
	*/
	public String getReason() {
		return reason;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	/** 
	* 申请人0女1男
	* @return
	*/
	public String getFromUserGender() {
		return fromUserGender;
	}

	/** 
	* 申请人0女1男
	* @param fromUserGender
	*/
	public void setFromUserGender(String fromUserGender) {
		this.fromUserGender = fromUserGender;
	}

	/** 
	* 申请人附件UUID
	* @return
	*/
	public String getFromUserUuid() {
		return fromUserUuid;
	}

	/** 
	* 申请人附件UUID
	* @param fromUserUuid
	*/
	public void setFromUserUuid(String fromUserUuid) {
		this.fromUserUuid = fromUserUuid;
	}

	/** 
	* 申请人附件名称
	* @return
	*/
	public String getFromUserFileName() {
		return fromUserFileName;
	}

	/** 
	* 申请人附件名称
	* @param fromUserFileName
	*/
	public void setFromUserFileName(String fromUserFileName) {
		this.fromUserFileName = fromUserFileName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	/** 
	* 审核人0女1男
	* @return
	*/
	public String getToUserGender() {
		return toUserGender;
	}

	/** 
	* 审核人0女1男
	* @param toUserGender
	*/
	public void setToUserGender(String toUserGender) {
		this.toUserGender = toUserGender;
	}

	/** 
	* 审核人附件UUID
	* @return
	*/
	public String getToUserUuid() {
		return toUserUuid;
	}

	/** 
	* 审核人附件UUID
	* @param toUserUuid
	*/
	public void setToUserUuid(String toUserUuid) {
		this.toUserUuid = toUserUuid;
	}

	/** 
	* 审核人附件名称
	* @return
	*/
	public String getToUserFileName() {
		return toUserFileName;
	}

	/** 
	* 审核人附件名称
	* @param toUserFileName
	*/
	public void setToUserFileName(String toUserFileName) {
		this.toUserFileName = toUserFileName;
	}

	/** 
	* 任务名称
	* @return
	*/
	public String getTaskName() {
		return taskName;
	}

	/** 
	* 任务名称
	* @param taskName
	*/
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/** 
	* 0-不同意，1-同意
	* @param status
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	* 0-不同意，1-同意
	* @return
	*/
	public Integer getStatus() {
		return status;
	}

	/** 
	* 审批意见
	* @param spOpinion
	*/
	public void setSpOpinion(String spOpinion) {
		this.spOpinion = spOpinion;
	}

	/** 
	* 审批意见
	* @return
	*/
	public String getSpOpinion() {
		return spOpinion;
	}

	/** 
	* 任务版本
	* @return
	*/
	public String getTaskVersion() {
		return taskVersion;
	}

	/** 
	* 任务版本
	* @param taskVersion
	*/
	public void setTaskVersion(String taskVersion) {
		this.taskVersion = taskVersion;
	}
}
