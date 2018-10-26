package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 文档夹
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FileClassify {
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
	* 分类创建人主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 分类名称
	*/
	@Filed
	private String typeName;
	/** 
	* 分类父ID
	*/
	@Filed
	private Integer parentId;
	/** 
	* 分类
	*/
	@Filed
	private String type;
	/** 
	* 是否为系统文件夹，默认是
	*/
	@Filed
	private String isSys;
	/** 
	* 公开或私有标记 1公开  0私有 
	*/
	@Filed
	private Integer pubState;

	/****************以上主要为系统表字段********************/
	private String uuid;
	/** 
	* 上传人
	*/
	private String userName;
	/** 
	* 上传人小头像
	*/
	private String userSmlImgUuid;
	private String userSmlImgName;
	/** 
	* 上传人性别
	*/
	private String userGender;
	private Integer level;

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
	* 分类创建人主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 分类创建人主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 分类名称
	* @param typeName
	*/
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/** 
	* 分类名称
	* @return
	*/
	public String getTypeName() {
		return typeName;
	}

	/** 
	* 分类父ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 分类父ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 分类
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 分类
	* @return
	*/
	public String getType() {
		return type;
	}

	/** 
	* 是否为系统文件夹，默认是
	* @param isSys
	*/
	public void setIsSys(String isSys) {
		this.isSys = isSys;
	}

	/** 
	* 是否为系统文件夹，默认是
	* @return
	*/
	public String getIsSys() {
		return isSys;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** 
	* 上传人
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 上传人
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 上传人小头像
	* @return
	*/
	public String getUserSmlImgUuid() {
		return userSmlImgUuid;
	}

	/** 
	* 上传人小头像
	* @param userSmlImgUuid
	*/
	public void setUserSmlImgUuid(String userSmlImgUuid) {
		this.userSmlImgUuid = userSmlImgUuid;
	}

	public String getUserSmlImgName() {
		return userSmlImgName;
	}

	public void setUserSmlImgName(String userSmlImgName) {
		this.userSmlImgName = userSmlImgName;
	}

	/** 
	* 上传人性别
	* @return
	*/
	public String getUserGender() {
		return userGender;
	}

	/** 
	* 上传人性别
	* @param userGender
	*/
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	/** 
	* 公开或私有标记 1公开  0私有 
	* @param pubState
	*/
	public void setPubState(Integer pubState) {
		this.pubState = pubState;
	}

	/** 
	* 公开或私有标记 1公开  0私有 
	* @return
	*/
	public Integer getPubState() {
		return pubState;
	}
}
