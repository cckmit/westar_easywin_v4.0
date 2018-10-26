package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 任务协办记录表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class TaskHandOver {
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
	* 移交人ID
	*/
	@Filed
	private Integer fromUser;
	/** 
	* 接收人ID
	*/
	@Filed
	private Integer toUser;
	/** 
	* 办理时限
	*/
	@Filed
	private String handTimeLimit;
	/** 
	* 步骤是否是当前步骤步骤标识，0不是，1是
	*/
	@Filed
	private Integer curStep;
	/** 
	* 结束时间
	*/
	@Filed
	private String endTime;
	/** 
	* 前一步骤排序信息
	*/
	@Filed
	private Integer preStep;
	/** 
	* 实际办理人员0不是，1是 
	*/
	@Filed
	private Integer actHandleState;
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

	/****************以上主要为系统表字段********************/

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
	* 移交人ID
	* @param fromUser
	*/
	public void setFromUser(Integer fromUser) {
		this.fromUser = fromUser;
	}

	/** 
	* 移交人ID
	* @return
	*/
	public Integer getFromUser() {
		return fromUser;
	}

	/** 
	* 接收人ID
	* @param toUser
	*/
	public void setToUser(Integer toUser) {
		this.toUser = toUser;
	}

	/** 
	* 接收人ID
	* @return
	*/
	public Integer getToUser() {
		return toUser;
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
	* 步骤是否是当前步骤步骤标识，0不是，1是
	* @param curStep
	*/
	public void setCurStep(Integer curStep) {
		this.curStep = curStep;
	}

	/** 
	* 步骤是否是当前步骤步骤标识，0不是，1是
	* @return
	*/
	public Integer getCurStep() {
		return curStep;
	}

	/** 
	* 结束时间
	* @param endTime
	*/
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/** 
	* 结束时间
	* @return
	*/
	public String getEndTime() {
		return endTime;
	}

	/** 
	* 前一步骤排序信息
	* @param preStep
	*/
	public void setPreStep(Integer preStep) {
		this.preStep = preStep;
	}

	/** 
	* 前一步骤排序信息
	* @return
	*/
	public Integer getPreStep() {
		return preStep;
	}

	/** 
	* 实际办理人员0不是，1是 
	* @param actHandleState
	*/
	public void setActHandleState(Integer actHandleState) {
		this.actHandleState = actHandleState;
	}

	/** 
	* 实际办理人员0不是，1是 
	* @return
	*/
	public Integer getActHandleState() {
		return actHandleState;
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
}
