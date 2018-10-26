package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.KeyValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 报销说明记录表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FeeLoanReport {
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
	* 流程实例化主键
	*/
	@Filed
	private Integer instanceId;
	/** 
	* 申请记录主键
	*/
	@Filed
	private Integer feeBudgetId;
	/** 
	* 汇报人
	*/
	@Filed
	private Integer creator;
	/** 
	* 是否出差申请；0-不是；1是；
	*/
	@Filed
	private Integer busTripState;
	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据
	*/
	@Filed
	private Integer status;
	/** 
	* 报告开始时间
	*/
	@Filed
	private String startTime;
	/** 
	* 报告结束时间
	*/
	@Filed
	private String endTime;

	/****************以上主要为系统表字段********************/
	/** 
	* 借款审批流程名称
	*/
	private String flowName;
	/** 
	* 创建人名称
	*/
	private String creatorName;
	/** 
	* 汇报审批力流程
	*/
	private Integer flowState;
	/** 
	* 当前审批人主键
	*/
	private Integer executor;
	/** 
	* 审批人姓名
	*/
	private String executorName;

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
	* 流程实例化主键
	* @param instanceId
	*/
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	/** 
	* 流程实例化主键
	* @return
	*/
	public Integer getInstanceId() {
		return instanceId;
	}

	/** 
	* 申请记录主键
	* @param feeBudgetId
	*/
	public void setFeeBudgetId(Integer feeBudgetId) {
		this.feeBudgetId = feeBudgetId;
	}

	/** 
	* 申请记录主键
	* @return
	*/
	public Integer getFeeBudgetId() {
		return feeBudgetId;
	}

	/** 
	* 汇报人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 汇报人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 是否出差申请；0-不是；1是；
	* @param busTripState
	*/
	public void setBusTripState(Integer busTripState) {
		this.busTripState = busTripState;
	}

	/** 
	* 是否出差申请；0-不是；1是；
	* @return
	*/
	public Integer getBusTripState() {
		return busTripState;
	}

	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据
	* @param status
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据
	* @return
	*/
	public Integer getStatus() {
		return status;
	}

	/** 
	* 借款审批流程名称
	* @return
	*/
	public String getFlowName() {
		return flowName;
	}

	/** 
	* 借款审批流程名称
	* @param flowName
	*/
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/** 
	* 创建人名称
	* @return
	*/
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 创建人名称
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 汇报审批力流程
	* @return
	*/
	public Integer getFlowState() {
		return flowState;
	}

	/** 
	* 汇报审批力流程
	* @param flowState
	*/
	public void setFlowState(Integer flowState) {
		this.flowState = flowState;
	}

	/** 
	* 当前审批人主键
	* @return
	*/
	public Integer getExecutor() {
		return executor;
	}

	/** 
	* 当前审批人主键
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
	* 报告开始时间
	* @param startTime
	*/
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/** 
	* 报告开始时间
	* @return
	*/
	public String getStartTime() {
		return startTime;
	}

	/** 
	* 报告结束时间
	* @param endTime
	*/
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/** 
	* 报告结束时间
	* @return
	*/
	public String getEndTime() {
		return endTime;
	}
}
