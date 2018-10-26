package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 表单布局配置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FormConf {
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
	* 表单模板主键
	*/
	@Filed
	private Integer formModId;
	/** 
	* 模板版本主键
	*/
	@Filed
	private Integer formLayoutId;
	/** 
	* 组件字段标识
	*/
	@Filed
	private Integer fieldId;
	/** 
	* 配置名称
	*/
	@Filed
	private String confName;
	/** 
	* 配置项值
	*/
	@Filed
	private String confValue;

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
	* 表单模板主键
	* @param formModId
	*/
	public void setFormModId(Integer formModId) {
		this.formModId = formModId;
	}

	/** 
	* 表单模板主键
	* @return
	*/
	public Integer getFormModId() {
		return formModId;
	}

	/** 
	* 模板版本主键
	* @param formLayoutId
	*/
	public void setFormLayoutId(Integer formLayoutId) {
		this.formLayoutId = formLayoutId;
	}

	/** 
	* 模板版本主键
	* @return
	*/
	public Integer getFormLayoutId() {
		return formLayoutId;
	}

	/** 
	* 配置名称
	* @param confName
	*/
	public void setConfName(String confName) {
		this.confName = confName;
	}

	/** 
	* 配置名称
	* @return
	*/
	public String getConfName() {
		return confName;
	}

	/** 
	* 配置项值
	* @param confValue
	*/
	public void setConfValue(String confValue) {
		this.confValue = confValue;
	}

	/** 
	* 配置项值
	* @return
	*/
	public String getConfValue() {
		return confValue;
	}

	/** 
	* 组件字段标识
	* @param fieldId
	*/
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	/** 
	* 组件字段标识
	* @return
	*/
	public Integer getFieldId() {
		return fieldId;
	}
}
