package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 周报附件
 */
@Table
@JsonInclude(Include.NON_NULL)
public class WeekRepUpfiles {
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
	* 周报主键
	*/
	@Filed
	private Integer weekReportId;
	/** 
	* 关联upfiles主键
	*/
	@Filed
	private Integer upfileId;
	/** 
	* 附件上传人
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 附件名称
	*/
	private String fileName;
	/** 
	* 附件UUID
	*/
	private String fileUuid;
	/** 
	* 附件上传人
	*/
	private String username;
	/** 
	* 附件上传时间
	*/
	private String upTime;
	/** 
	* 头像名称
	*/
	private String ImgName;
	/** 
	* 头像UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 来源类别
	*/
	private String type;
	/** 
	* 文件后缀
	*/
	private String fileExt;

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
	* 周报主键
	* @param weekReportId
	*/
	public void setWeekReportId(Integer weekReportId) {
		this.weekReportId = weekReportId;
	}

	/** 
	* 周报主键
	* @return
	*/
	public Integer getWeekReportId() {
		return weekReportId;
	}

	/** 
	* 关联upfiles主键
	* @param upfileId
	*/
	public void setUpfileId(Integer upfileId) {
		this.upfileId = upfileId;
	}

	/** 
	* 关联upfiles主键
	* @return
	*/
	public Integer getUpfileId() {
		return upfileId;
	}

	/** 
	* 附件上传人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 附件上传人
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 附件上传人
	* @return
	*/
	public String getUsername() {
		return username;
	}

	/** 
	* 附件上传人
	* @param username
	*/
	public void setUsername(String username) {
		this.username = username;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 附件名称
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	* 附件上传时间
	* @return
	*/
	public String getUpTime() {
		return upTime;
	}

	/** 
	* 附件上传时间
	* @param upTime
	*/
	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	/** 
	* 头像名称
	* @return
	*/
	public String getImgName() {
		return ImgName;
	}

	/** 
	* 头像名称
	* @param ImgName
	*/
	public void setImgName(String imgName) {
		ImgName = imgName;
	}

	/** 
	* 头像UUID
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 头像UUID
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
	* 附件UUID
	* @return
	*/
	public String getFileUuid() {
		return fileUuid;
	}

	/** 
	* 附件UUID
	* @param fileUuid
	*/
	public void setFileUuid(String fileUuid) {
		this.fileUuid = fileUuid;
	}

	/** 
	* 来源类别
	* @return
	*/
	public String getType() {
		return type;
	}

	/** 
	* 来源类别
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 文件后缀
	* @return
	*/
	public String getFileExt() {
		return fileExt;
	}

	/** 
	* 文件后缀
	* @param fileExt
	*/
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
}
