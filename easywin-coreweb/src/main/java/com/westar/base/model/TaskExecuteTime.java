package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 任务实际执行时间表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class TaskExecuteTime {
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
	* 结束时间
	*/
	@Filed
	private String endTime;
	/** 
	* 步骤节点标识
	*/
	@Filed
	private String stepTag;

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
}
