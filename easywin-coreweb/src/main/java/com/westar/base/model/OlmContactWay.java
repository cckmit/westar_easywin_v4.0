package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 外部联系人联系方式表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class OlmContactWay {
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
	* 外部联系人主键
	*/
	@Filed
	private Integer outLinkManId;
	/** 
	* 联系方式标识 与字典表contactWay一一对应
	*/
	@Filed
	private String contactWayCode;
	/** 
	* 联系方式
	*/
	@Filed
	private String contactWay;
	/** 
	* 创建人员主键
	*/
	@Filed
	private Integer creator;

	/****************以上主要为系统表字段********************/
	/** 
	* 联系方式code值
	*/
	private String contactWayValue;

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
	* 外部联系人主键
	* @param outLinkManId
	*/
	public void setOutLinkManId(Integer outLinkManId) {
		this.outLinkManId = outLinkManId;
	}

	/** 
	* 外部联系人主键
	* @return
	*/
	public Integer getOutLinkManId() {
		return outLinkManId;
	}

	/** 
	* 联系方式标识 与字典表contactWay一一对应
	* @param contactWayCode
	*/
	public void setContactWayCode(String contactWayCode) {
		this.contactWayCode = contactWayCode;
	}

	/** 
	* 联系方式标识 与字典表contactWay一一对应
	* @return
	*/
	public String getContactWayCode() {
		return contactWayCode;
	}

	/** 
	* 联系方式
	* @param contactWay
	*/
	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	/** 
	* 联系方式
	* @return
	*/
	public String getContactWay() {
		return contactWay;
	}

	/** 
	* 创建人员主键
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 创建人员主键
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 联系方式code值
	* @return
	*/
	public String getContactWayValue() {
		return contactWayValue;
	}

	/** 
	* 联系方式code值
	* @param contactWayValue
	*/
	public void setContactWayValue(String contactWayValue) {
		this.contactWayValue = contactWayValue;
	}
}
