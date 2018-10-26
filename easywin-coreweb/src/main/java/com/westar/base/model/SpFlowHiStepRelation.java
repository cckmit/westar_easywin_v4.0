package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 流程实例步骤间关系表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowHiStepRelation {
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
	* 
	*/
	@Filed
	private Integer curStepId;
	/** 
	* 
	*/
	@Filed
	private Integer nextStepId;
	/** 
	* 流程步骤扭转方式
	*/
	@Filed
	private String stepWay;
	/** 
	* 0-不是，1-是；同时只有同级步骤stepWay为分支branch时，起作用
	*/
	@Filed
	private Integer defaultStep;

 /*业务类型*/
 @Filed
 private String busType;

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
	* 流程实例主键
	* @param instanceId
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
	* 
	* @param curStepId
	*/
	public void setCurStepId(Integer curStepId) {
		this.curStepId = curStepId;
	}

	/** 
	* 
	* @return
	*/
	public Integer getCurStepId() {
		return curStepId;
	}

	/** 
	* 
	* @param nextStepId
	*/
	public void setNextStepId(Integer nextStepId) {
		this.nextStepId = nextStepId;
	}

	/** 
	* 
	* @return
	*/
	public Integer getNextStepId() {
		return nextStepId;
	}

	/** 
	* 流程步骤扭转方式
	* @param stepWay
	*/
	public void setStepWay(String stepWay) {
		this.stepWay = stepWay;
	}

	/** 
	* 流程步骤扭转方式
	* @return
	*/
	public String getStepWay() {
		return stepWay;
	}

	/** 
	* 0-不是，1-是；同时只有同级步骤stepWay为分支branch时，起作用
	* @param defaultStep
	*/
	public void setDefaultStep(Integer defaultStep) {
		this.defaultStep = defaultStep;
	}

	/** 
	* 0-不是，1-是；同时只有同级步骤stepWay为分支branch时，起作用
	* @return
	*/
	public Integer getDefaultStep() {
		return defaultStep;
	}
 /**
 *业务类型
 * @param busType
 */
 public void setBusType(String busType) {
 	this.busType = busType;
 }

 /**
 *业务类型
 * @return
 */
 public String getBusType() {
 	return busType;
 }

}
