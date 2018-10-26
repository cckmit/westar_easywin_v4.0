package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 系统日志表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SystemLog {
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
	* 业务类别 字典表
	*/
	@Filed
	private String businessType;
	/** 
	* 操作者id
	*/
	@Filed
	private Integer userId;
	/** 
	* 操作内容
	*/
	@Filed
	private String content;
	/** 
	* 操作时间
	*/
	@Filed
	private String recordDateTime;
	/** 
	* 模块ID
	*/
	@Filed
	private Integer busId;
	/** 
	* 操作IP
	*/
	@Filed
	private String optIP;

	/****************以上主要为系统表字段********************/
	/** 
	* 部门主键
	*/
	private Integer depId;
	/** 
	* 部门名称
	*/
	private String depName;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	private String userName;
	/** 
	* 附件名称
	*/
	private String imgName;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
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
	* 业务类别 字典表
	* @param businessType
	*/
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	/** 
	* 业务类别 字典表
	* @return
	*/
	public String getBusinessType() {
		return businessType;
	}

	/** 
	* 操作者id
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 操作者id
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 操作内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 操作内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	/** 
	* 操作时间
	* @param recordDateTime
	*/
	public void setRecordDateTime(String recordDateTime) {
		this.recordDateTime = recordDateTime;
	}

	/** 
	* 操作时间
	* @return
	*/
	public String getRecordDateTime() {
		return recordDateTime;
	}

	/** 
	* 部门主键
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 部门主键
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	/** 
	* 部门名称
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 部门名称
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/** 
	* 查询的时间起
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询的时间起
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询的时间止
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的时间止
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getImgName() {
		return imgName;
	}

	/** 
	* 附件名称
	* @param imgName
	*/
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	/** 
	* 附件UUID
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 附件UUID
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 0女1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 模块ID
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 模块ID
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 操作IP
	* @param optIP
	*/
	public void setOptIP(String optIP) {
		this.optIP = optIP;
	}

	/** 
	* 操作IP
	* @return
	*/
	public String getOptIP() {
		return optIP;
	}
}
