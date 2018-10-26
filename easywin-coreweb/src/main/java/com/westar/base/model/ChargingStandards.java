package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 收费标准表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ChargingStandards {
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
	* 单位：/人/年
	*/
	@Filed
	private Integer price;
	/** 
	* 单位：G/人/年
	*/
	@Filed
	private Integer storageSpace;
	/** 
	* 收费标准
	*/
	@Filed
	private Integer chargingStandard;
	/** 
	* 收费类型
	*/
	@Filed
	private String chargingType;

	/****************以上主要为系统表字段********************/
	private Integer userScope;

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

	public void setUserScope(Integer userScope) {
		this.userScope = userScope;
	}

	public Integer getUserScope() {
		return userScope;
	}

	/** 
	* 单位：/人/年
	* @param price
	*/
	public void setPrice(Integer price) {
		this.price = price;
	}

	/** 
	* 单位：/人/年
	* @return
	*/
	public Integer getPrice() {
		return price;
	}

	/** 
	* 单位：G/人/年
	* @param storageSpace
	*/
	public void setStorageSpace(Integer storageSpace) {
		this.storageSpace = storageSpace;
	}

	/** 
	* 单位：G/人/年
	* @return
	*/
	public Integer getStorageSpace() {
		return storageSpace;
	}

	/** 
	* 收费标准
	* @param chargingStandard
	*/
	public void setChargingStandard(Integer chargingStandard) {
		this.chargingStandard = chargingStandard;
	}

	/** 
	* 收费标准
	* @return
	*/
	public Integer getChargingStandard() {
		return chargingStandard;
	}

	/** 
	* 收费类型
	* @param chargingType
	*/
	public void setChargingType(String chargingType) {
		this.chargingType = chargingType;
	}

	/** 
	* 收费类型
	* @return
	*/
	public String getChargingType() {
		return chargingType;
	}
}
