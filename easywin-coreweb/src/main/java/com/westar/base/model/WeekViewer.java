package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 周报查看人员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class WeekViewer {
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
	* 设定人
	*/
	@Filed
	private Integer userId;
	/** 
	* 个人分组主键
	*/
	@Filed
	private Integer viewerId;

	/****************以上主要为系统表字段********************/
	/** 
	* 查看人员姓名
	*/
	private String viewerName;
	/** 
	* 附件名称
	*/
	private String fileName;
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
	* 设定人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 设定人
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 个人分组主键
	* @param viewerId
	*/
	public void setViewerId(Integer viewerId) {
		this.viewerId = viewerId;
	}

	/** 
	* 个人分组主键
	* @return
	*/
	public Integer getViewerId() {
		return viewerId;
	}

	/** 
	* 查看人员姓名
	* @return
	*/
	public String getViewerName() {
		return viewerName;
	}

	/** 
	* 查看人员姓名
	* @param viewerName
	*/
	public void setViewerName(String viewerName) {
		this.viewerName = viewerName;
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
}
