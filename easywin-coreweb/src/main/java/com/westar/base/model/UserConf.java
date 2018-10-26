package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 个人配置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class UserConf {
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
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 配置是否开启，0未开启
	*/
	@Filed
	private Integer openState;
	/** 
	* 系统配置
	*/
	@Filed
	private String sysConfCode;
	/** 
	* 配置类型
	*/
	@Filed
	private String type;

	/****************以上主要为系统表字段********************/
	/** 
	* 配置项名称
	*/
	private String sysConfigName;

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
	* 配置是否开启，0未开启
	* @param openState
	*/
	public void setOpenState(Integer openState) {
		this.openState = openState;
	}

	/** 
	* 配置是否开启，0未开启
	* @return
	*/
	public Integer getOpenState() {
		return openState;
	}

	/** 
	* 配置项名称
	* @return
	*/
	public String getSysConfigName() {
		return sysConfigName;
	}

	/** 
	* 配置项名称
	* @param sysConfigName
	*/
	public void setSysConfigName(String sysConfigName) {
		this.sysConfigName = sysConfigName;
	}

	/** 
	* 系统配置
	* @param sysConfCode
	*/
	public void setSysConfCode(String sysConfCode) {
		this.sysConfCode = sysConfCode;
	}

	/** 
	* 系统配置
	* @return
	*/
	public String getSysConfCode() {
		return sysConfCode;
	}

	/** 
	* 配置类型
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 配置类型
	* @return
	*/
	public String getType() {
		return type;
	}
}
