package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 与会人员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MeetPerson {
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
	* 会议主键
	*/
	@Filed
	private Integer meetingId;
	/** 
	* 与会人主键
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 与会人员的姓名
	*/
	private String personName;
	/** 
	* 与会人员性别
	*/
	private String personGender;
	/** 
	* 与会人员的头像的uuid
	*/
	private String personImgUuid;
	/** 
	* 与会人员的头像的name
	*/
	private String personImgName;

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
	* 会议主键
	* @param meetingId
	*/
	public void setMeetingId(Integer meetingId) {
		this.meetingId = meetingId;
	}

	/** 
	* 会议主键
	* @return
	*/
	public Integer getMeetingId() {
		return meetingId;
	}

	/** 
	* 与会人主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 与会人主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 与会人员的姓名
	* @return
	*/
	public final String getPersonName() {
		return personName;
	}

	/** 
	* 与会人员的姓名
	* @param personName
	*/
	public final void setPersonName(String personName) {
		this.personName = personName;
	}

	/** 
	* 与会人员性别
	* @return
	*/
	public final String getPersonGender() {
		return personGender;
	}

	/** 
	* 与会人员性别
	* @param personGender
	*/
	public final void setPersonGender(String personGender) {
		this.personGender = personGender;
	}

	/** 
	* 与会人员的头像的uuid
	* @return
	*/
	public final String getPersonImgUuid() {
		return personImgUuid;
	}

	/** 
	* 与会人员的头像的uuid
	* @param personImgUuid
	*/
	public final void setPersonImgUuid(String personImgUuid) {
		this.personImgUuid = personImgUuid;
	}

	/** 
	* 与会人员的头像的name
	* @return
	*/
	public final String getPersonImgName() {
		return personImgName;
	}

	/** 
	* 与会人员的头像的name
	* @param personImgName
	*/
	public final void setPersonImgName(String personImgName) {
		this.personImgName = personImgName;
	}
}
