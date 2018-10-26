package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 流程表单数据
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowInputData {
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
	* 组件字段标识
	*/
	@Filed
	private Integer fieldId;
	/** 
	* 布局组件类型
	*/
	@Filed
	private String componentKey;
	/** 
	* 内容
	*/
	@Filed
	private String content;
	/** 
	* 多行标识
	*/
	@Filed
	private Integer dataIndex;
	/** 
	* 流程实例化主键
	*/
	@Filed
	private Integer instanceId;

	/****************以上主要为系统表字段********************/
	private String busTableType;
	private Integer busTableId;

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
	* 内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 多行标识
	* @param dataIndex
	*/
	public void setDataIndex(Integer dataIndex) {
		this.dataIndex = dataIndex;
	}

	/** 
	* 多行标识
	* @return
	*/
	public Integer getDataIndex() {
		return dataIndex;
	}

	/** 
	* 流程实例化主键
	* @param instanceId
	*/
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	/** 
	* 流程实例化主键
	* @return
	*/
	public Integer getInstanceId() {
		return instanceId;
	}

	public void setBusTableId(Integer busTableId) {
		this.busTableId = busTableId;
	}

	public Integer getBusTableId() {
		return busTableId;
	}

	public String getBusTableType() {
		return busTableType;
	}

	public void setBusTableType(String busTableType) {
		this.busTableType = busTableType;
	}
}
