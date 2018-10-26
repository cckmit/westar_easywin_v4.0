package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 会议室
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MeetingRoom {
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
	* 会议室名称
	*/
	@Filed
	private String roomName;
	/** 
	* 会议室地点
	*/
	@Filed
	private String roomAddress;
	/** 
	* 会议室大小
	*/
	@Filed
	private Integer containMax;
	/** 
	* 会议室图片主键
	*/
	@Filed
	private Integer roomPicId;
	/** 
	* 会议室管理人员id
	*/
	@Filed
	private Integer mamager;
	/** 
	* 会议室描述
	*/
	@Filed
	private String content;
	/** 
	* 是否为默认会议室 0不是 1是
	*/
	@Filed
	private String isDefault;

	/****************以上主要为系统表字段********************/
	/** 
	* 管理员姓名
	*/
	private String mamagerName;
	/** 
	* 附件名称
	*/
	private String mamagerImgName;
	/** 
	* 附件UUID
	*/
	private String mamagerUuid;
	/** 
	* 0女1男
	*/
	private String mamagerGender;
	/** 
	* 会议室附件UUID
	*/
	private String roomPicUuid;
	/** 
	* 会议室附件名称
	*/
	private String roomPicName;

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
	* 会议室名称
	* @param roomName
	*/
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	/** 
	* 会议室名称
	* @return
	*/
	public String getRoomName() {
		return roomName;
	}

	/** 
	* 会议室地点
	* @param roomAddress
	*/
	public void setRoomAddress(String roomAddress) {
		this.roomAddress = roomAddress;
	}

	/** 
	* 会议室地点
	* @return
	*/
	public String getRoomAddress() {
		return roomAddress;
	}

	/** 
	* 会议室大小
	* @param containMax
	*/
	public void setContainMax(Integer containMax) {
		this.containMax = containMax;
	}

	/** 
	* 会议室大小
	* @return
	*/
	public Integer getContainMax() {
		return containMax;
	}

	/** 
	* 会议室图片主键
	* @param roomPicId
	*/
	public void setRoomPicId(Integer roomPicId) {
		this.roomPicId = roomPicId;
	}

	/** 
	* 会议室图片主键
	* @return
	*/
	public Integer getRoomPicId() {
		return roomPicId;
	}

	/** 
	* 会议室管理人员id
	* @param mamager
	*/
	public void setMamager(Integer mamager) {
		this.mamager = mamager;
	}

	/** 
	* 会议室管理人员id
	* @return
	*/
	public Integer getMamager() {
		return mamager;
	}

	/** 
	* 会议室描述
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 会议室描述
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 是否为默认会议室 0不是 1是
	* @param isDefault
	*/
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	/** 
	* 是否为默认会议室 0不是 1是
	* @return
	*/
	public String getIsDefault() {
		return isDefault;
	}

	/** 
	* 管理员姓名
	* @return
	*/
	public String getMamagerName() {
		return mamagerName;
	}

	/** 
	* 管理员姓名
	* @param mamagerName
	*/
	public void setMamagerName(String mamagerName) {
		this.mamagerName = mamagerName;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getMamagerImgName() {
		return mamagerImgName;
	}

	/** 
	* 附件名称
	* @param mamagerImgName
	*/
	public void setMamagerImgName(String mamagerImgName) {
		this.mamagerImgName = mamagerImgName;
	}

	/** 
	* 附件UUID
	* @return
	*/
	public String getMamagerUuid() {
		return mamagerUuid;
	}

	/** 
	* 附件UUID
	* @param mamagerUuid
	*/
	public void setMamagerUuid(String mamagerUuid) {
		this.mamagerUuid = mamagerUuid;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getMamagerGender() {
		return mamagerGender;
	}

	/** 
	* 0女1男
	* @param mamagerGender
	*/
	public void setMamagerGender(String mamagerGender) {
		this.mamagerGender = mamagerGender;
	}

	/** 
	* 会议室附件UUID
	* @return
	*/
	public String getRoomPicUuid() {
		return roomPicUuid;
	}

	/** 
	* 会议室附件UUID
	* @param roomPicUuid
	*/
	public void setRoomPicUuid(String roomPicUuid) {
		this.roomPicUuid = roomPicUuid;
	}

	/** 
	* 会议室附件名称
	* @return
	*/
	public String getRoomPicName() {
		return roomPicName;
	}

	/** 
	* 会议室附件名称
	* @param roomPicName
	*/
	public void setRoomPicName(String roomPicName) {
		this.roomPicName = roomPicName;
	}
}
