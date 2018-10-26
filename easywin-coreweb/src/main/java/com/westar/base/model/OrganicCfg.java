package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 企业配置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class OrganicCfg {
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
	* 配置类型
	*/
	@Filed
	private String cfgType;
	/** 
	* 配置数值
	*/
	@Filed
	private String cfgValue;

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
	* 配置类型
	* @param cfgType
	*/
	public void setCfgType(String cfgType) {
		this.cfgType = cfgType;
	}

	/** 
	* 配置类型
	* @return
	*/
	public String getCfgType() {
		return cfgType;
	}

	/** 
	* 配置数值
	* @param cfgValue
	*/
	public void setCfgValue(String cfgValue) {
		this.cfgValue = cfgValue;
	}

	/** 
	* 配置数值
	* @return
	*/
	public String getCfgValue() {
		return cfgValue;
	}
}
