package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 日程参与人员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ScheUser {
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
	* 日程主键
	*/
	@Filed
	private Integer scheduleId;
	/** 
	* 参与人员主键
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 姓名
	*/
	private String userName;
	/** 
	* 小图片信息
	*/
	private String imgUuid;
	private String imgName;
	private String gender;

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
	* 日程主键
	* @param scheduleId
	*/
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	/** 
	* 日程主键
	* @return
	*/
	public Integer getScheduleId() {
		return scheduleId;
	}

	/** 
	* 参与人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 参与人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 小图片信息
	* @return
	*/
	public String getImgUuid() {
		return imgUuid;
	}

	/** 
	* 小图片信息
	* @param imgUuid
	*/
	public void setImgUuid(String imgUuid) {
		this.imgUuid = imgUuid;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
