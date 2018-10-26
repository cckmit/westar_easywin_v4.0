package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 告知人员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class NoticePerson {
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
	* 发起人主键
	*/
	@Filed
	private Integer meetingId;
	/** 
	* 发起人主键
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 告知人员的姓名
	*/
	private String userName;
	/** 
	* 告知人员性别
	*/
	private String userGender;
	/** 
	* 告知人员的头像的uuid
	*/
	private String userImgUuid;
	/** 
	* 告知人员的头像的name
	*/
	private String userImgName;

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
	* 发起人主键
	* @param meetingId
	*/
	public void setMeetingId(Integer meetingId) {
		this.meetingId = meetingId;
	}

	/** 
	* 发起人主键
	* @return
	*/
	public Integer getMeetingId() {
		return meetingId;
	}

	/** 
	* 发起人主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 发起人主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 告知人员的姓名
	* @return
	*/
	public final String getUserName() {
		return userName;
	}

	/** 
	* 告知人员的姓名
	* @param userName
	*/
	public final void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 告知人员性别
	* @return
	*/
	public final String getUserGender() {
		return userGender;
	}

	/** 
	* 告知人员性别
	* @param userGender
	*/
	public final void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	/** 
	* 告知人员的头像的uuid
	* @return
	*/
	public final String getUserImgUuid() {
		return userImgUuid;
	}

	/** 
	* 告知人员的头像的uuid
	* @param userImgUuid
	*/
	public final void setUserImgUuid(String userImgUuid) {
		this.userImgUuid = userImgUuid;
	}

	/** 
	* 告知人员的头像的name
	* @return
	*/
	public final String getUserImgName() {
		return userImgName;
	}

	/** 
	* 告知人员的头像的name
	* @param userImgName
	*/
	public final void setUserImgName(String userImgName) {
		this.userImgName = userImgName;
	}
}
