package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 表单组件
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FormCompon {
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
	* 父ID
	*/
	@Filed
	private Integer parentId;
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
	* 布局组件类型
	*/
	@Filed
	private String componentKey;
	/** 
	* 标题
	*/
	@Filed
	private String title;
	/** 
	* 排序
	*/
	@Filed
	private String orderNo;
	/** 
	* 组件字段标识
	*/
	@Filed
	private Integer fieldId;

	/****************以上主要为系统表字段********************/
	private List<FormCompon> formConfLeaf;
	private Integer size;
	private List<FormConf> formConfList;
	/** 
	* 是否为叶子点1是0不是
	*/
	private Integer isLeaf;
	/** 
	* 是否选中标识符
	*/
	private Integer checked;
	/** 
	* 子表单主键
	*/
	private Integer subFormId;
	/** 
	* 子表单序号
	*/
	private Integer subFormIndex;
	/** 
	* 临时主键
	*/
	private String tempId;
	private String fromFormControlKey;
	/** 
	* 是否需要填充数据
	*/
	private Integer isFill;

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
	* 父ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 父ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
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
	* 布局组件类型
	* @param componentKey
	*/
	public void setComponentKey(String componentKey) {
		this.componentKey = componentKey;
	}

	/** 
	* 布局组件类型
	* @return
	*/
	public String getComponentKey() {
		return componentKey;
	}

	/** 
	* 标题
	* @param title
	*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** 
	* 标题
	* @return
	*/
	public String getTitle() {
		return title;
	}

	/** 
	* 排序
	* @param orderNo
	*/
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/** 
	* 排序
	* @return
	*/
	public String getOrderNo() {
		return orderNo;
	}

	public List<FormCompon> getFormConfLeaf() {
		return formConfLeaf;
	}

	public void setFormConfLeaf(List<FormCompon> formConfLeaf) {
		this.formConfLeaf = formConfLeaf;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public List<FormConf> getFormConfList() {
		return formConfList;
	}

	public void setFormConfList(List<FormConf> formConfList) {
		this.formConfList = formConfList;
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

	/** 
	* 是否为叶子点1是0不是
	* @return
	*/
	public Integer getIsLeaf() {
		return isLeaf;
	}

	/** 
	* 是否为叶子点1是0不是
	* @param isLeaf
	*/
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	/** 
	* 是否选中标识符
	* @return
	*/
	public Integer getChecked() {
		return checked;
	}

	/** 
	* 是否选中标识符
	* @param checked
	*/
	public void setChecked(Integer checked) {
		this.checked = checked;
	}

	/** 
	* 子表单主键
	* @return
	*/
	public Integer getSubFormId() {
		return subFormId;
	}

	/** 
	* 子表单主键
	* @param subFormId
	*/
	public void setSubFormId(Integer subFormId) {
		this.subFormId = subFormId;
	}

	/** 
	* 子表单序号
	* @return
	*/
	public Integer getSubFormIndex() {
		return subFormIndex;
	}

	/** 
	* 子表单序号
	* @param subFormIndex
	*/
	public void setSubFormIndex(Integer subFormIndex) {
		this.subFormIndex = subFormIndex;
	}

	public String getFromFormControlKey() {
		return fromFormControlKey;
	}

	public void setFromFormControlKey(String fromFormControlKey) {
		this.fromFormControlKey = fromFormControlKey;
	}

	/** 
	* 临时主键
	* @return
	*/
	public String getTempId() {
		return tempId;
	}

	/** 
	* 临时主键
	* @param tempId
	*/
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	/** 
	* 是否需要填充数据
	* @return
	*/
	public Integer getIsFill() {
		return isFill;
	}

	/** 
	* 是否需要填充数据
	* @param isFill
	*/
	public void setIsFill(Integer isFill) {
		this.isFill = isFill;
	}
}
