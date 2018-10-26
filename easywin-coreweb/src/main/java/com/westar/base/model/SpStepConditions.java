package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 步骤条件描述
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpStepConditions {
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
	* 流程步骤主键
	*/
	@Filed
	private Integer stepId;
	/** 
	* 表单控件主键
	*/
	@Filed
	private String conditionVar;
	/** 
	* 条件运算符
	*/
	@Filed
	private String conditionType;
	/** 
	* 条件比较值
	*/
	@Filed
	private String conditionValue;
	/** 
	* 条件序号
	*/
	@Filed
	private String conditionNum;

	/****************以上主要为系统表字段********************/
	/** 
	* 变量显示名
	*/
	private String conditionVarName;

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
	* 流程步骤主键
	* @param stepId
	*/
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	/** 
	* 流程步骤主键
	* @return
	*/
	public Integer getStepId() {
		return stepId;
	}

	/** 
	* 表单控件主键
	* @param conditionVar
	*/
	public void setConditionVar(String conditionVar) {
		this.conditionVar = conditionVar;
	}

	/** 
	* 表单控件主键
	* @return
	*/
	public String getConditionVar() {
		return conditionVar;
	}

	/** 
	* 条件运算符
	* @param conditionType
	*/
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	/** 
	* 条件运算符
	* @return
	*/
	public String getConditionType() {
		return conditionType;
	}

	/** 
	* 条件比较值
	* @param conditionValue
	*/
	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	/** 
	* 条件比较值
	* @return
	*/
	public String getConditionValue() {
		return conditionValue;
	}

	/** 
	* 条件序号
	* @param conditionNum
	*/
	public void setConditionNum(String conditionNum) {
		this.conditionNum = conditionNum;
	}

	/** 
	* 条件序号
	* @return
	*/
	public String getConditionNum() {
		return conditionNum;
	}

	/** 
	* 变量显示名
	* @return
	*/
	public String getConditionVarName() {
		return conditionVarName;
	}

	/** 
	* 变量显示名
	* @param conditionVarName
	*/
	public void setConditionVarName(String conditionVarName) {
		this.conditionVarName = conditionVarName;
	}
}
