package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 服务器MAC绑定
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BandMAC {
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
	* 服务器MAC
	*/
	@Filed
	private String macName;
	/** 
	* 授权码
	*/
	@Filed
	private String licenseCode;
	/** 
	* 使用期限
	*/
	@Filed
	private String serviceDate;

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
	* 服务器MAC
	* @param macName
	*/
	public void setMacName(String macName) {
		this.macName = macName;
	}

	/** 
	* 服务器MAC
	* @return
	*/
	public String getMacName() {
		return macName;
	}

	/** 
	* 授权码
	* @param licenseCode
	*/
	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

	/** 
	* 授权码
	* @return
	*/
	public String getLicenseCode() {
		return licenseCode;
	}

	/** 
	* 使用期限
	* @param serviceDate
	*/
	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	/** 
	* 使用期限
	* @return
	*/
	public String getServiceDate() {
		return serviceDate;
	}
}
