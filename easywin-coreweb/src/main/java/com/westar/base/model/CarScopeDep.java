package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 车辆申请部门范围
 */
@Table
@JsonInclude(Include.NON_NULL)
public class CarScopeDep {
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
	* 车辆主键
	*/
	@Filed
	private Integer carId;
	/** 
	* 部门主键
	*/
	@Filed
	private Integer depId;
	/** 
	* 添加人
	*/
	@Filed
	private Integer creator;

	/****************以上主要为系统表字段********************/
	/** 
	* 部门名称
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
	* 车辆主键
	* @param carId
	*/
	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	/** 
	* 车辆主键
	* @return
	*/
	public Integer getCarId() {
		return carId;
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
	* 添加人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 添加人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 部门名称
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 部门名称
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}
}
