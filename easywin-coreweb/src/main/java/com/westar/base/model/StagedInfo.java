package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 项目阶段明细
 */
@Table
@JsonInclude(Include.NON_NULL)
public class StagedInfo {
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
	* 项目主键
	*/
	@Filed
	private Integer itemId;
	/** 
	* 项目阶段主键
	*/
	@Filed
	private Integer stagedItemId;
	/** 
	* 关联模块主键
	*/
	@Filed
	private Integer moduleId;
	/** 
	* 关联模块类型
	*/
	@Filed
	private String moduleType;
	/** 
	* 创建人
	*/
	@Filed
	private Integer creator;

	/****************以上主要为系统表字段********************/
	/** 
	* 业务名称
	*/
	private String moduleName;
	/** 
	* 项目阶段附件
	*/
	private List<Upfiles> listFiles;
	/** 
	* 负责人名称
	*/
	private String userName;
	/** 
	* 负责人头像标识
	*/
	private String userUuid;
	/** 
	* 负责人头像附件名称
	*/
	private String headImgName;
	/** 
	* 负责人头像性别
	*/
	private String gender;
	/** 
	* 附件主键
	*/
	private Integer upfileId;
	/** 
	* 附件名称
	*/
	private String fileName;
	/** 
	* 附件uuid
	*/
	private String fileUuid;
	/** 
	* 附件后缀
	*/
	private String fileExt;
	private Integer infoId;
	/** 
	* 任务状态
	*/
	private Integer taskState;

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
	* 项目主键
	* @param itemId
	*/
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/** 
	* 项目主键
	* @return
	*/
	public Integer getItemId() {
		return itemId;
	}

	/** 
	* 项目阶段主键
	* @param stagedItemId
	*/
	public void setStagedItemId(Integer stagedItemId) {
		this.stagedItemId = stagedItemId;
	}

	/** 
	* 项目阶段主键
	* @return
	*/
	public Integer getStagedItemId() {
		return stagedItemId;
	}

	/** 
	* 关联模块主键
	* @param moduleId
	*/
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	/** 
	* 关联模块主键
	* @return
	*/
	public Integer getModuleId() {
		return moduleId;
	}

	/** 
	* 关联模块类型
	* @param moduleType
	*/
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	/** 
	* 关联模块类型
	* @return
	*/
	public String getModuleType() {
		return moduleType;
	}

	/** 
	* 创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 业务名称
	* @return
	*/
	public String getModuleName() {
		return moduleName;
	}

	/** 
	* 业务名称
	* @param moduleName
	*/
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/** 
	* 项目阶段附件
	* @return
	*/
	public List<Upfiles> getListFiles() {
		return listFiles;
	}

	/** 
	* 项目阶段附件
	* @param listFiles
	*/
	public void setListFiles(List<Upfiles> listFiles) {
		this.listFiles = listFiles;
	}

	/** 
	* 负责人名称
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 负责人名称
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 负责人头像标识
	* @return
	*/
	public String getUserUuid() {
		return userUuid;
	}

	/** 
	* 负责人头像标识
	* @param userUuid
	*/
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	/** 
	* 负责人头像附件名称
	* @return
	*/
	public String getHeadImgName() {
		return headImgName;
	}

	/** 
	* 负责人头像附件名称
	* @param headImgName
	*/
	public void setHeadImgName(String headImgName) {
		this.headImgName = headImgName;
	}

	/** 
	* 负责人头像性别
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 负责人头像性别
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
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
	* 附件uuid
	* @return
	*/
	public String getFileUuid() {
		return fileUuid;
	}

	/** 
	* 附件uuid
	* @param fileUuid
	*/
	public void setFileUuid(String fileUuid) {
		this.fileUuid = fileUuid;
	}

	/** 
	* 附件后缀
	* @return
	*/
	public String getFileExt() {
		return fileExt;
	}

	/** 
	* 附件后缀
	* @param fileExt
	*/
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	/** 
	* 附件主键
	* @return
	*/
	public Integer getUpfileId() {
		return upfileId;
	}

	/** 
	* 附件主键
	* @param upfileId
	*/
	public void setUpfileId(Integer upfileId) {
		this.upfileId = upfileId;
	}

	public Integer getInfoId() {
		return infoId;
	}

	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}

	/** 
	* 任务状态
	* @return
	*/
	public Integer getTaskState() {
		return taskState;
	}

	/** 
	* 任务状态
	* @param taskState
	*/
	public void setTaskState(Integer taskState) {
		this.taskState = taskState;
	}
}
