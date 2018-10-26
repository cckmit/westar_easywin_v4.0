package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 积分标准范围
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JfzbDepScope {
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
	* 积分标准
	*/
	@Filed
	private Integer jfzbId;
	/** 
	* 适用部门
	*/
	@Filed
	private Integer depId;

	/****************以上主要为系统表字段********************/
	/** 
	* 适用的部门名称
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
	* 积分标准
	* @param jfzbId
	*/
	public void setJfzbId(Integer jfzbId) {
		this.jfzbId = jfzbId;
	}

	/** 
	* 积分标准
	* @return
	*/
	public Integer getJfzbId() {
		return jfzbId;
	}

	/** 
	* 适用部门
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	/** 
	* 适用部门
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 适用的部门名称
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 适用的部门名称
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}
}
