package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 表单模板归类
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FormModSort {
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
	* 归类名称
	*/
	@Filed
	private String sortName;
	/** 
	* 排序
	*/
	@Filed
	private Integer orderNo;

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
	* 归类名称
	* @param sortName
	*/
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	/** 
	* 归类名称
	* @return
	*/
	public String getSortName() {
		return sortName;
	}

	/** 
	* 排序
	* @param orderNo
	*/
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/** 
	* 排序
	* @return
	*/
	public Integer getOrderNo() {
		return orderNo;
	}
}
