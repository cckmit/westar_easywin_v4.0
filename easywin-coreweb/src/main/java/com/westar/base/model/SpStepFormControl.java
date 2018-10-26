package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 步骤权限控制
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpStepFormControl {
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
	* 流程步骤主键
	*/
	@Filed
	private Integer stepId;
	/** 
	* 表单控件主键
	*/
	@Filed
	private String formControlKey;
	/** 
	* 关联流程主键
	*/
	@Filed
	private Integer flowId;
	/** 
	* 是否填充,0:否;1:是;-1:不可用
	*/
	@Filed
	private Integer isFill;

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
	* @param formControlKey
	*/
	public void setFormControlKey(String formControlKey) {
		this.formControlKey = formControlKey;
	}

	/** 
	* 表单控件主键
	* @return
	*/
	public String getFormControlKey() {
		return formControlKey;
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
	* 是否填充,0:否;1:是;-1:不可用
	* @return
	*/
	public Integer getIsFill() {
		return isFill;
	}

	/** 
	* 是否填充,0:否;1:是;-1:不可用
	* @param isFill
	*/
	public void setIsFill(Integer isFill) {
		this.isFill = isFill;
	}
}
