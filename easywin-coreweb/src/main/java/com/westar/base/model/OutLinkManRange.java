package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 外部联系人私有范围
 */
@Table
@JsonInclude(Include.NON_NULL)
public class OutLinkManRange {
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
	* 私有范围人主键
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 用户名
	*/
	private String userName;

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
	* 私有范围人主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 私有范围人主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 用户名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 用户名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
