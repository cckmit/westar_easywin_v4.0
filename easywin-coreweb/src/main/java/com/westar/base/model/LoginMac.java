package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 登录mac地址
 */
@Table
@JsonInclude(Include.NON_NULL)
public class LoginMac {
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
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 登录的mac地址
	*/
	@Filed
	private String mac;
	/** 
	* 登录IP
	*/
	@Filed
	private String ip;

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

	/** 
	* 登录的mac地址
	* @param mac
	*/
	public void setMac(String mac) {
		this.mac = mac;
	}

	/** 
	* 登录的mac地址
	* @return
	*/
	public String getMac() {
		return mac;
	}

	/** 
	* 登录IP
	* @param ip
	*/
	public void setIp(String ip) {
		this.ip = ip;
	}

	/** 
	* 登录IP
	* @return
	*/
	public String getIp() {
		return ip;
	}
}
