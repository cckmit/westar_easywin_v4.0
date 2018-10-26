package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 表单布局版本
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FormLayout {
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
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 布局版本
	*/
	@Filed
	private Integer version;
	/** 
	* 布局状态替换
	*/
	@Filed
	private Integer formState;

	/****************以上主要为系统表字段********************/
	private String layoutDetail;
	private FormLayoutHtml formLayoutHtml;

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
	* 人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	public String getLayoutDetail() {
		return layoutDetail;
	}

	public void setLayoutDetail(String layoutDetail) {
		this.layoutDetail = layoutDetail;
	}

	/** 
	* 布局版本
	* @param version
	*/
	public void setVersion(Integer version) {
		this.version = version;
	}

	/** 
	* 布局版本
	* @return
	*/
	public Integer getVersion() {
		return version;
	}

	public FormLayoutHtml getFormLayoutHtml() {
		return formLayoutHtml;
	}

	public void setFormLayoutHtml(FormLayoutHtml formLayoutHtml) {
		this.formLayoutHtml = formLayoutHtml;
	}

	/** 
	* 布局状态替换
	* @param formState
	*/
	public void setFormState(Integer formState) {
		this.formState = formState;
	}

	/** 
	* 布局状态替换
	* @return
	*/
	public Integer getFormState() {
		return formState;
	}
}
