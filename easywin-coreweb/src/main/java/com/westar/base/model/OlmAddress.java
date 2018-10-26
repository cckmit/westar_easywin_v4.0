package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 外部联系人联系地址表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class OlmAddress {
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
	* 联系地址标识 0办公地址 1家庭地址
	*/
	@Filed
	private String addressCode;
	/** 
	* 联系地址
	*/
	@Filed
	private String address;
	/** 
	* 创建人员主键
	*/
	@Filed
	private Integer creator;

	/****************以上主要为系统表字段********************/
	/** 
	* 联系地址Code值
	*/
	private String addressValue;

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
	* 联系地址标识 0办公地址 1家庭地址
	* @param addressCode
	*/
	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

	/** 
	* 联系地址标识 0办公地址 1家庭地址
	* @return
	*/
	public String getAddressCode() {
		return addressCode;
	}

	/** 
	* 联系地址
	* @param address
	*/
	public void setAddress(String address) {
		this.address = address;
	}

	/** 
	* 联系地址
	* @return
	*/
	public String getAddress() {
		return address;
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
	* 联系地址Code值
	* @return
	*/
	public String getAddressValue() {
		return addressValue;
	}

	/** 
	* 联系地址Code值
	* @param addressValue
	*/
	public void setAddressValue(String addressValue) {
		this.addressValue = addressValue;
	}
}
