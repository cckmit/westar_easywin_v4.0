package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 模块属性变更审批
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ModuleChangeExamine {
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
	* 模块区分，列值于BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String moduleType;
	/** 
	* 对应需要审批字段
	*/
	@Filed
	private String field;
	/** 
	* 启用状态 ;0是关闭；1是启用
	*/
	@Filed
	private Integer enabled;

	/****************以上主要为系统表字段********************/
	/** 
	* 操作字段集合
	*/
	private String[] fields;

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
	* 模块区分，列值于BusinessTypeConstant常量一一对应
	* @param moduleType
	*/
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	/** 
	* 模块区分，列值于BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getModuleType() {
		return moduleType;
	}

	/** 
	* 对应需要审批字段
	* @param field
	*/
	public void setField(String field) {
		this.field = field;
	}

	/** 
	* 对应需要审批字段
	* @return
	*/
	public String getField() {
		return field;
	}

	/** 
	* 启用状态 ;0是关闭；1是启用
	* @param enabled
	*/
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	/** 
	* 启用状态 ;0是关闭；1是启用
	* @return
	*/
	public Integer getEnabled() {
		return enabled;
	}

	/** 
	* 操作字段集合
	* @return
	*/
	public String[] getFields() {
		return fields;
	}

	/** 
	* 操作字段集合
	* @param fields
	*/
	public void setFields(String[] fields) {
		this.fields = fields;
	}
}
