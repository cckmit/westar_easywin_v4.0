package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 数据映射关系权限控制表（以部门为单位）
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BusMapFlowAuthDep {
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
	* 模块操作与审批流程之间关联主键
	*/
	@Filed
	private Integer busMapFlowId;
	/** 
	* 部门主键
	*/
	@Filed
	private Integer depId;

	/****************以上主要为系统表字段********************/
	/** 
	* 授权部门名称
	*/
	private String depName;

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
	* 模块操作与审批流程之间关联主键
	* @param busMapFlowId
	*/
	public void setBusMapFlowId(Integer busMapFlowId) {
		this.busMapFlowId = busMapFlowId;
	}

	/** 
	* 模块操作与审批流程之间关联主键
	* @return
	*/
	public Integer getBusMapFlowId() {
		return busMapFlowId;
	}

	/** 
	* 部门主键
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	/** 
	* 部门主键
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 授权部门名称
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 授权部门名称
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}
}
