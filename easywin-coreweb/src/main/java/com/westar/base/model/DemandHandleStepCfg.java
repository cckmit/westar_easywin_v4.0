package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 需求处理模板步骤
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DemandHandleStepCfg {
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
	* 模板步骤名称
	*/
	@Filed
	private String stepName;
	/** 
	* 处理类型
	*/
	@Filed
	private String stepType;
	/** 
	* 排序号
	*/
	@Filed
	private String stepOrder;
	/** 
	* 模板主键
	*/
	@Filed
	private Integer demandModuleCfgId;

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
	* 模板步骤名称
	* @param stepName
	*/
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	/** 
	* 模板步骤名称
	* @return
	*/
	public String getStepName() {
		return stepName;
	}

	/** 
	* 处理类型
	* @param stepType
	*/
	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	/** 
	* 处理类型
	* @return
	*/
	public String getStepType() {
		return stepType;
	}

	/** 
	* 排序号
	* @param stepOrder
	*/
	public void setStepOrder(String stepOrder) {
		this.stepOrder = stepOrder;
	}

	/** 
	* 排序号
	* @return
	*/
	public String getStepOrder() {
		return stepOrder;
	}

	/** 
	* 模板主键
	* @param demandModuleCfgId
	*/
	public void setDemandModuleCfgId(Integer demandModuleCfgId) {
		this.demandModuleCfgId = demandModuleCfgId;
	}

	/** 
	* 模板主键
	* @return
	*/
	public Integer getDemandModuleCfgId() {
		return demandModuleCfgId;
	}
}
