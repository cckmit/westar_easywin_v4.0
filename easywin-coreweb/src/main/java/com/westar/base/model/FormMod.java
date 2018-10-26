package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 企业表单模板
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FormMod {
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
	* 表单名称
	*/
	@Filed
	private String modName;
	/** 
	* 表单描述
	*/
	@Filed
	private String modDescrib;
	/** 
	* 表单模板分类
	*/
	@Filed
	private Integer formSortId;
	/** 
	* 表单启用,默认开启
	*/
	@Filed
	private String enable;
	/** 
	* 默认云表单模板
	*/
	@Filed
	private Integer cloudModId;

	/****************以上主要为系统表字段********************/
	private FormLayout formLayout;
	/** 
	* 使用的布局版本
	*/
	private Integer version;
	/** 
	* 1已替换0未替换
	*/
	private Integer formState;
	/** 
	* 布局主键
	*/
	private Integer layoutId;
	/** 
	* 分类名称
	*/
	private String modSortName;

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
	* 表单名称
	* @param modName
	*/
	public void setModName(String modName) {
		this.modName = modName;
	}

	/** 
	* 表单名称
	* @return
	*/
	public String getModName() {
		return modName;
	}

	/** 
	* 表单描述
	* @param modDescrib
	*/
	public void setModDescrib(String modDescrib) {
		this.modDescrib = modDescrib;
	}

	/** 
	* 表单描述
	* @return
	*/
	public String getModDescrib() {
		return modDescrib;
	}

	/** 
	* 表单模板分类
	* @param formSortId
	*/
	public void setFormSortId(Integer formSortId) {
		this.formSortId = formSortId;
	}

	/** 
	* 表单模板分类
	* @return
	*/
	public Integer getFormSortId() {
		return formSortId;
	}

	/** 
	* 表单启用,默认开启
	* @param enable
	*/
	public void setEnable(String enable) {
		this.enable = enable;
	}

	/** 
	* 表单启用,默认开启
	* @return
	*/
	public String getEnable() {
		return enable;
	}

	public FormLayout getFormLayout() {
		return formLayout;
	}

	public void setFormLayout(FormLayout formLayout) {
		this.formLayout = formLayout;
	}

	/** 
	* 使用的布局版本
	* @return
	*/
	public Integer getVersion() {
		return version;
	}

	/** 
	* 使用的布局版本
	* @param version
	*/
	public void setVersion(Integer version) {
		this.version = version;
	}

	/** 
	* 布局主键
	* @return
	*/
	public Integer getLayoutId() {
		return layoutId;
	}

	/** 
	* 布局主键
	* @param layoutId
	*/
	public void setLayoutId(Integer layoutId) {
		this.layoutId = layoutId;
	}

	/** 
	* 分类名称
	* @return
	*/
	public String getModSortName() {
		return modSortName;
	}

	/** 
	* 分类名称
	* @param modSortName
	*/
	public void setModSortName(String modSortName) {
		this.modSortName = modSortName;
	}

	/** 
	* 默认云表单模板
	* @param cloudModId
	*/
	public void setCloudModId(Integer cloudModId) {
		this.cloudModId = cloudModId;
	}

	/** 
	* 默认云表单模板
	* @return
	*/
	public Integer getCloudModId() {
		return cloudModId;
	}

	/** 
	* 1已替换0未替换
	* @return
	*/
	public Integer getFormState() {
		return formState;
	}

	/** 
	* 1已替换0未替换
	* @param formState
	*/
	public void setFormState(Integer formState) {
		this.formState = formState;
	}
}
