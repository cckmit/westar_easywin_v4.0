package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 会议室申请
 */
@Table
@JsonInclude(Include.NON_NULL)
public class RoomApply {
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
	* 会议室主键
	*/
	@Filed
	private Integer roomId;
	/** 
	* 会议主键
	*/
	@Filed
	private Integer meetingId;
	/** 
	* 会议室申请开始时间
	*/
	@Filed
	private String startDate;
	/** 
	* 会议室申请结束时间
	*/
	@Filed
	private String endDate;
	/** 
	* 会议室申请状态 1审核通过 0申请中
	*/
	@Filed
	private String state;
	/** 
	* 会议室申请人员id
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 会议室名称
	*/
	private String roomName;
	/** 
	* 会议名称
	*/
	private String meetTitle;
	/** 
	* 管理员姓名
	*/
	private String managerName;
	/** 
	* 申请人姓名
	*/
	private String userName;
	/** 
	* 1不是本人添加的时间,2是本人添加,3其他情况
	*/
	private String status;
	/** 
	* 是否过期2会议已开始3会议已结束
	*/
	private Integer timeOut;

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
	* 会议室主键
	* @param roomId
	*/
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	/** 
	* 会议室主键
	* @return
	*/
	public Integer getRoomId() {
		return roomId;
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
	* 会议室申请开始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 会议室申请开始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 会议室申请结束时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 会议室申请结束时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 会议室申请状态 1审核通过 0申请中
	* @param state
	*/
	public void setState(String state) {
		this.state = state;
	}

	/** 
	* 会议室申请状态 1审核通过 0申请中
	* @return
	*/
	public String getState() {
		return state;
	}

	/** 
	* 会议室申请人员id
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 会议室申请人员id
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 申请人姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 申请人姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 1不是本人添加的时间,2是本人添加,3其他情况
	* @return
	*/
	public String getStatus() {
		return status;
	}

	/** 
	* 1不是本人添加的时间,2是本人添加,3其他情况
	* @param status
	*/
	public void setStatus(String status) {
		this.status = status;
	}

	/** 
	* 是否过期2会议已开始3会议已结束
	* @return
	*/
	public Integer getTimeOut() {
		return timeOut;
	}

	/** 
	* 是否过期2会议已开始3会议已结束
	* @param timeOut
	*/
	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}

	/** 
	* 管理员姓名
	* @return
	*/
	public String getManagerName() {
		return managerName;
	}

	/** 
	* 管理员姓名
	* @param managerName
	*/
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	/** 
	* 会议室名称
	* @return
	*/
	public String getRoomName() {
		return roomName;
	}

	/** 
	* 会议室名称
	* @param roomName
	*/
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	/** 
	* 会议名称
	* @return
	*/
	public String getMeetTitle() {
		return meetTitle;
	}

	/** 
	* 会议名称
	* @param meetTitle
	*/
	public void setMeetTitle(String meetTitle) {
		this.meetTitle = meetTitle;
	}
}
