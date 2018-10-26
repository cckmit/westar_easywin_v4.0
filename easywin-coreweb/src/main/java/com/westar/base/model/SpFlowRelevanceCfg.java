package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 子流程配置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowRelevanceCfg {
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
	* 关联流程主键
	*/
	@Filed
	private Integer pflowId;
	/** 
	* 表单控件主键
	*/
	@Filed
	private String fromFormControlKey;
	/** 
	* 关联流程主键
	*/
	@Filed
	private Integer sonFlowId;
	/** 
	* 表单控件主键
	*/
	@Filed
	private String toFormControlKey;

	/****************以上主要为系统表字段********************/
	/** 
	* 映射关系数组
	*/
	private String[] keyValueArray;

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
	* 表单控件主键
	* @param fromFormControlKey
	*/
	public void setFromFormControlKey(String fromFormControlKey) {
		this.fromFormControlKey = fromFormControlKey;
	}

	/** 
	* 表单控件主键
	* @return
	*/
	public String getFromFormControlKey() {
		return fromFormControlKey;
	}

	/** 
	* 关联流程主键
	* @param sonFlowId
	*/
	public void setSonFlowId(Integer sonFlowId) {
		this.sonFlowId = sonFlowId;
	}

	/** 
	* 关联流程主键
	* @return
	*/
	public Integer getSonFlowId() {
		return sonFlowId;
	}

	/** 
	* 表单控件主键
	* @param toFormControlKey
	*/
	public void setToFormControlKey(String toFormControlKey) {
		this.toFormControlKey = toFormControlKey;
	}

	/** 
	* 表单控件主键
	* @return
	*/
	public String getToFormControlKey() {
		return toFormControlKey;
	}

	/** 
	* 映射关系数组
	* @return
	*/
	public String[] getKeyValueArray() {
		return keyValueArray;
	}

	/** 
	* 映射关系数组
	* @param keyValueArray
	*/
	public void setKeyValueArray(String[] keyValueArray) {
		this.keyValueArray = keyValueArray;
	}

	/** 
	* 关联流程主键
	* @return
	*/
	public Integer getPflowId() {
		return pflowId;
	}

	/** 
	* 关联流程主键
	* @param pflowId
	*/
	public void setPflowId(Integer pflowId) {
		this.pflowId = pflowId;
	}
}
