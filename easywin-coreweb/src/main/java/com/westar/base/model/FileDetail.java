package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.core.web.DataDicContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 文档
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FileDetail {
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
	* 文档上传人主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 关联upfiles主键
	*/
	@Filed
	private Integer fileId;
	/** 
	* 范围类型 0所有人，2自己，1自定义
	*/
	@Filed
	private Integer scopeType;
	/** 
	* 文档夹主键
	*/
	@Filed
	private Integer classifyId;
	/** 
	* 文件描述，其他模块的是名称
	*/
	@Filed
	private String fileDescribe;
	/** 
	* 所属模块 1周报 2 任务 3项目 4客户 5 知识 6问 7答
	*/
	@Filed
	private String moduleType;
	/** 
	* 公开或私有标记 1公开  0私有 
	*/
	@Filed
	private Integer pubState;
	/** 
	* 修改时间
	*/
	@Filed
	private String modifyTime;

	/****************以上主要为系统表字段********************/
	/** 
	* 当前人员的主键
	*/
	private Integer sessionUser;
	private String fileExt;
	private String fileName;
	private String sizem;
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
	/** 
	* 上传时间
	*/
	private String fileDate;
	/** 
	* 条件总的范围
	*/
	private String searchAll;
	/** 
	* 条件查看权限
	*/
	private String searchMe;
	/** 
	* 查询类型
	*/
	private String searchType;
	/** 
	* 所属模块名称
	*/
	private String moduleTypeName;
	/** 
	* 查看范围
	*/
	private List<FileViewScope> fileViewScopes;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 附件集合（用于添加的时候）
	*/
	private List<Upfiles> listUpfiles;
	/** 
	* 是否为批量操作不是批量操作则不用设置
	*/
	private String isBatch;
	private List<FileViewScopeDep> scopeDep;
	/** 
	* 文件分享范围
	*/
	private List<FileShare> listFileShare;

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
	* 文档上传人主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 文档上传人主键
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
	* 范围类型 0所有人，2自己，1自定义
	* @param scopeType
	*/
	public void setScopeType(Integer scopeType) {
		this.scopeType = scopeType;
	}

	/** 
	* 范围类型 0所有人，2自己，1自定义
	* @return
	*/
	public Integer getScopeType() {
		return scopeType;
	}

	/** 
	* 文档夹主键
	* @param classifyId
	*/
	public void setClassifyId(Integer classifyId) {
		this.classifyId = classifyId;
	}

	/** 
	* 文档夹主键
	* @return
	*/
	public Integer getClassifyId() {
		return classifyId;
	}

	/** 
	* 当前人员的主键
	* @return
	*/
	public Integer getSessionUser() {
		return sessionUser;
	}

	/** 
	* 当前人员的主键
	* @param sessionUser
	*/
	public void setSessionUser(Integer sessionUser) {
		this.sessionUser = sessionUser;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSizem() {
		return sizem;
	}

	public void setSizem(String sizem) {
		this.sizem = sizem;
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
	* 上传时间
	* @return
	*/
	public String getFileDate() {
		return fileDate;
	}

	/** 
	* 上传时间
	* @param fileDate
	*/
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
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

	/** 
	* 条件总的范围
	* @return
	*/
	public String getSearchAll() {
		return searchAll;
	}

	/** 
	* 条件总的范围
	* @param searchAll
	*/
	public void setSearchAll(String searchAll) {
		this.searchAll = searchAll;
	}

	/** 
	* 条件查看权限
	* @return
	*/
	public String getSearchMe() {
		return searchMe;
	}

	/** 
	* 条件查看权限
	* @param searchMe
	*/
	public void setSearchMe(String searchMe) {
		this.searchMe = searchMe;
	}

	/** 
	* 查询类型
	* @return
	*/
	public String getSearchType() {
		return searchType;
	}

	/** 
	* 查询类型
	* @param searchType
	*/
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/** 
	* 所属模块名称
	* @return
	*/
	public String getModuleTypeName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("moduleType", moduleType);
	}

	/** 
	* 所属模块名称
	* @param moduleTypeName
	*/
	public void setModuleTypeName(String moduleTypeName) {
		this.moduleTypeName = moduleTypeName;
	}

	/** 
	* 查看范围
	* @return
	*/
	public List<FileViewScope> getFileViewScopes() {
		return fileViewScopes;
	}

	/** 
	* 查看范围
	* @param fileViewScopes
	*/
	public void setFileViewScopes(List<FileViewScope> fileViewScopes) {
		this.fileViewScopes = fileViewScopes;
	}

	/** 
	* 文件描述，其他模块的是名称
	* @param fileDescribe
	*/
	public void setFileDescribe(String fileDescribe) {
		this.fileDescribe = fileDescribe;
	}

	/** 
	* 文件描述，其他模块的是名称
	* @return
	*/
	public String getFileDescribe() {
		return fileDescribe;
	}

	/** 
	* 所属模块 1周报 2 任务 3项目 4客户 5 知识 6问 7答
	* @param moduleType
	*/
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	/** 
	* 所属模块 1周报 2 任务 3项目 4客户 5 知识 6问 7答
	* @return
	*/
	public String getModuleType() {
		return moduleType;
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
	* 附件集合（用于添加的时候）
	* @return
	*/
	public List<Upfiles> getListUpfiles() {
		return listUpfiles;
	}

	/** 
	* 附件集合（用于添加的时候）
	* @param listUpfiles
	*/
	public void setListUpfiles(List<Upfiles> listUpfiles) {
		this.listUpfiles = listUpfiles;
	}

	/** 
	* 是否为批量操作不是批量操作则不用设置
	* @return
	*/
	public String getIsBatch() {
		return isBatch;
	}

	/** 
	* 是否为批量操作不是批量操作则不用设置
	* @param isBatch
	*/
	public void setIsBatch(String isBatch) {
		this.isBatch = isBatch;
	}

	public List<FileViewScopeDep> getScopeDep() {
		return scopeDep;
	}

	public void setScopeDep(List<FileViewScopeDep> scopeDep) {
		this.scopeDep = scopeDep;
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

	/** 
	* 文件分享范围
	* @return
	*/
	public List<FileShare> getListFileShare() {
		return listFileShare;
	}

	/** 
	* 文件分享范围
	* @param listFileShare
	*/
	public void setListFileShare(List<FileShare> listFileShare) {
		this.listFileShare = listFileShare;
	}

	/** 
	* 修改时间
	* @param modifyTime
	*/
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/** 
	* 修改时间
	* @return
	*/
	public String getModifyTime() {
		return modifyTime;
	}
}
