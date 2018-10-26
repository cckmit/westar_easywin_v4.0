package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 文档评论
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FileTalk {
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
	* 评论人主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 关联upfiles主键
	*/
	@Filed
	private Integer fileId;
	/** 
	* 模块主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 模块类型 见系统常量
	*/
	@Filed
	private String busType;
	/** 
	* 评论内容
	*/
	@Filed
	private String talkContent;
	/** 
	* 讨论父ID
	*/
	@Filed
	private Integer parentId;

	/****************以上主要为系统表字段********************/
	/** 
	* 讨论人姓名
	*/
	private String talkerName;
	/** 
	* 讨论人小头像
	*/
	private String talkerSmlImgUuid;
	private String talkerSmlImgName;
	/** 
	* 讨论人性别
	*/
	private String talkerGender;
	/** 
	* 父讨论人姓名
	*/
	private String ptalkerName;
	private Integer isLeaf;

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
	* 评论人主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 评论人主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 关联upfiles主键
	* @param fileId
	*/
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	/** 
	* 关联upfiles主键
	* @return
	*/
	public Integer getFileId() {
		return fileId;
	}

	/** 
	* 模块主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 模块主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 模块类型 见系统常量
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 模块类型 见系统常量
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 评论内容
	* @param talkContent
	*/
	public void setTalkContent(String talkContent) {
		this.talkContent = talkContent;
	}

	/** 
	* 评论内容
	* @return
	*/
	public String getTalkContent() {
		return talkContent;
	}

	/** 
	* 讨论父ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 讨论父ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 讨论人姓名
	* @return
	*/
	public String getTalkerName() {
		return talkerName;
	}

	/** 
	* 讨论人姓名
	* @param talkerName
	*/
	public void setTalkerName(String talkerName) {
		this.talkerName = talkerName;
	}

	/** 
	* 讨论人小头像
	* @return
	*/
	public String getTalkerSmlImgUuid() {
		return talkerSmlImgUuid;
	}

	/** 
	* 讨论人小头像
	* @param talkerSmlImgUuid
	*/
	public void setTalkerSmlImgUuid(String talkerSmlImgUuid) {
		this.talkerSmlImgUuid = talkerSmlImgUuid;
	}

	public String getTalkerSmlImgName() {
		return talkerSmlImgName;
	}

	public void setTalkerSmlImgName(String talkerSmlImgName) {
		this.talkerSmlImgName = talkerSmlImgName;
	}

	/** 
	* 讨论人性别
	* @return
	*/
	public String getTalkerGender() {
		return talkerGender;
	}

	/** 
	* 讨论人性别
	* @param talkerGender
	*/
	public void setTalkerGender(String talkerGender) {
		this.talkerGender = talkerGender;
	}

	/** 
	* 父讨论人姓名
	* @return
	*/
	public String getPtalkerName() {
		return ptalkerName;
	}

	/** 
	* 父讨论人姓名
	* @param ptalkerName
	*/
	public void setPtalkerName(String ptalkerName) {
		this.ptalkerName = ptalkerName;
	}

	public Integer getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
}
