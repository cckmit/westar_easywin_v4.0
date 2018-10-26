package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 表单组件当前标识
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FormComponMaxField {
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
	* 模板版本主键
	*/
	@Filed
	private Integer formModId;
	/** 
	* 组件字段标识
	*/
	@Filed
	private Integer maxFieldId;

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
	* 模板版本主键
	* @param formModId
	*/
	public void setFormModId(Integer formModId) {
		this.formModId = formModId;
	}

	/** 
	* 模板版本主键
	* @return
	*/
	public Integer getFormModId() {
		return formModId;
	}

	/** 
	* 组件字段标识
	* @param maxFieldId
	*/
	public void setMaxFieldId(Integer maxFieldId) {
		this.maxFieldId = maxFieldId;
	}

	/** 
	* 组件字段标识
	* @return
	*/
	public Integer getMaxFieldId() {
		return maxFieldId;
	}
}
