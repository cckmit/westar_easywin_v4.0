package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 流程步骤间关系表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowStepRelation {
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
	* 关联流程主键
	*/
	@Filed
	private Integer flowId;
	/** 
	* 当前流程步骤主键
	*/
	@Filed
	private Integer curStepId;
	/** 
	* 下步流程步骤主键
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

	/****************以上主要为系统表字段********************/
	/** 
	* 原关系表中curStepId主键
	*/
	private Integer oldCurStepId;
	/** 
	* 分支
	*/
	public static String WAY_BY_BRANCH = "branch";
	/** 
	* 并行
	*/
	public static String WAY_BY_PARALLEL = "parallel";

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
	* 当前流程步骤主键
	* @param curStepId
	*/
	public void setCurStepId(Integer curStepId) {
		this.curStepId = curStepId;
	}

	/** 
	* 当前流程步骤主键
	* @return
	*/
	public Integer getCurStepId() {
		return curStepId;
	}

	/** 
	* 下步流程步骤主键
	* @param nextStepId
	*/
	public void setNextStepId(Integer nextStepId) {
		this.nextStepId = nextStepId;
	}

	/** 
	* 下步流程步骤主键
	* @return
	*/
	public Integer getNextStepId() {
		return nextStepId;
	}

	/** 
	* 原关系表中curStepId主键
	* @return
	*/
	public Integer getOldCurStepId() {
		return oldCurStepId;
	}

	/** 
	* 原关系表中curStepId主键
	* @param oldCurStepId
	*/
	public void setOldCurStepId(Integer oldCurStepId) {
		this.oldCurStepId = oldCurStepId;
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
}
