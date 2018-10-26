package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 任务执行人
 */
@Table
@JsonInclude(Include.NON_NULL)
public class TaskExecutor implements Serializable {
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
	* 任务主键
	*/
	@Filed
	private Integer taskId;
	/** 
	* 任务执行人
	*/
	@Filed
	private Integer executor;
	/** 
	* 任务进度
	*/
	@Filed
	private Integer taskProgress;
	/** 
	* 任务状态(0待认领 1进行中；3挂起中；4完成。)
	*/
	@Filed
	private Integer state;
	/** 
	* 办理时限
	*/
	@Filed
	private String handTimeLimit;
	/** 
	* 任务推送人
	*/
	@Filed
	private Integer pushUser;
	/** 
	* 步骤节点标识
	*/
	@Filed
	private String stepTag;
	/** 
	* 任务办理时限小时数
	*/
	@Filed
	private String expectTime;
	/** 
	* 任务已经办理小时数
	*/
	@Filed
	private String costTime;
	/** 
	* 任务实际开始时间
	*/
	@Filed
	private String curStartTime;

	/****************以上主要为系统表字段********************/
	/** 
	* 任务执行人名称
	*/
	private String executorName;
	/** 
	* 附件名称
	*/
	private String fileName;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 任务推送人
	*/
	private String pushUserName;

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
	* 任务主键
	* @param taskId
	*/
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	/** 
	* 任务主键
	* @return
	*/
	public Integer getTaskId() {
		return taskId;
	}

	/** 
	* 任务执行人
	* @param executor
	*/
	public void setExecutor(Integer executor) {
		this.executor = executor;
	}

	/** 
	* 任务执行人
	* @return
	*/
	public Integer getExecutor() {
		return executor;
	}

	/** 
	* 任务进度
	* @param taskProgress
	*/
	public void setTaskProgress(Integer taskProgress) {
		this.taskProgress = taskProgress;
	}

	/** 
	* 任务进度
	* @return
	*/
	public Integer getTaskProgress() {
		return taskProgress;
	}

	/** 
	* 任务状态(0待认领 1进行中；3挂起中；4完成。)
	* @param state
	*/
	public void setState(Integer state) {
		this.state = state;
	}

	/** 
	* 任务状态(0待认领 1进行中；3挂起中；4完成。)
	* @return
	*/
	public Integer getState() {
		return state;
	}

	/** 
	* 任务执行人名称
	* @return
	*/
	public String getExecutorName() {
		return executorName;
	}

	/** 
	* 任务执行人名称
	* @param executorName
	*/
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 附件名称
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	* 附件UUID
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 附件UUID
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 0女1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 办理时限
	* @param handTimeLimit
	*/
	public void setHandTimeLimit(String handTimeLimit) {
		this.handTimeLimit = handTimeLimit;
	}

	/** 
	* 办理时限
	* @return
	*/
	public String getHandTimeLimit() {
		return handTimeLimit;
	}

	/** 
	* 任务推送人
	* @param pushUser
	*/
	public void setPushUser(Integer pushUser) {
		this.pushUser = pushUser;
	}

	/** 
	* 任务推送人
	* @return
	*/
	public Integer getPushUser() {
		return pushUser;
	}

	/** 
	* 任务推送人
	* @return
	*/
	public String getPushUserName() {
		return pushUserName;
	}

	/** 
	* 任务推送人
	* @param pushUserName
	*/
	public void setPushUserName(String pushUserName) {
		this.pushUserName = pushUserName;
	}

	/** 
	* 步骤节点标识
	* @param stepTag
	*/
	public void setStepTag(String stepTag) {
		this.stepTag = stepTag;
	}

	/** 
	* 步骤节点标识
	* @return
	*/
	public String getStepTag() {
		return stepTag;
	}

	/** 
	* 任务办理时限小时数
	* @param expectTime
	*/
	public void setExpectTime(String expectTime) {
		this.expectTime = expectTime;
	}

	/** 
	* 任务办理时限小时数
	* @return
	*/
	public String getExpectTime() {
		return expectTime;
	}

	/** 
	* 任务已经办理小时数
	* @param costTime
	*/
	public void setCostTime(String costTime) {
		this.costTime = costTime;
	}

	/** 
	* 任务已经办理小时数
	* @return
	*/
	public String getCostTime() {
		return costTime;
	}

	/** 
	* 任务实际开始时间
	* @param curStartTime
	*/
	public void setCurStartTime(String curStartTime) {
		this.curStartTime = curStartTime;
	}

	/** 
	* 任务实际开始时间
	* @return
	*/
	public String getCurStartTime() {
		return curStartTime;
	}
}
