package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.core.web.DataDicContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 关注信息
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Attention {
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
	* 操作人
	*/
	@Filed
	private Integer userId;
	/** 
	* 模块类型 见系统常量
	*/
	@Filed
	private String busType;
	/** 
	* 模块主键
	*/
	@Filed
	private Integer busId;

	/****************以上主要为系统表字段********************/
	/** 
	* 模块创建时间
	*/
	private String modTime;
	/** 
	* 模块负责人图片信息
	*/
	private String imgUuid;
	private String imgName;
	/** 
	* 模块负责人性别
	*/
	private String gender;
	/** 
	* 模块负责人
	*/
	private Integer owner;
	/** 
	* 模块负责人姓名
	*/
	private String userName;
	/** 
	* 模块内容
	*/
	private String modTitle;
	private String busTypeName;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 最新动态
	*/
	private String content;
	/** 
	* 模块更新时间
	*/
	private String modifTime;
	/** 
	* 模块更新人姓名
	*/
	private String modiferName;
	/** 
	* 是否查看过0没有
	*/
	private String isRead;
	/** 
	* 更新人主键
	*/
	private Integer modifyer;
	/** 
	* 附件名称
	*/
	private String fileName;
	/** 
	* 更新人uuid
	*/
	private String modifyerUuid;
	/** 
	* 更新人附件名称
	*/
	private String modifyerFileName;
	/** 
	* 分享信息msgShare表id
	*/
	private Integer msgId;
	/** 
	* 状态
	*/
	private Integer status;
	/** 
	* 执行人
	*/
	private Integer executor;
	/** 
	* 客户类型主键
	*/
	private Integer customerTypeId;

	/****************以上为自己添加字段********************/
	/** 
	* 更新人附件名称
	* @return
	*/
	public String getModifyerFileName() {
		return modifyerFileName;
	}

	/** 
	* 更新人附件名称
	* @param modifyerFileName
	*/
	public void setModifyerFileName(String modifyerFileName) {
		this.modifyerFileName = modifyerFileName;
	}

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
	* 操作人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 操作人
	* @return
	*/
	public Integer getUserId() {
		return userId;
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
	* 模块创建时间
	* @return
	*/
	public String getModTime() {
		return modTime;
	}

	/** 
	* 模块创建时间
	* @param modTime
	*/
	public void setModTime(String modTime) {
		this.modTime = modTime;
	}

	/** 
	* 模块负责人图片信息
	* @return
	*/
	public String getImgUuid() {
		return imgUuid;
	}

	/** 
	* 模块负责人图片信息
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

	/** 
	* 模块负责人性别
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 模块负责人性别
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 模块负责人姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 模块负责人姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 模块内容
	* @return
	*/
	public String getModTitle() {
		return modTitle;
	}

	/** 
	* 模块内容
	* @param modTitle
	*/
	public void setModTitle(String modTitle) {
		this.modTitle = modTitle;
	}

	public String getBusTypeName() {
		if (this.busType.equals("1")) {
			return "分享";
		} else {
			return DataDicContext.getInstance().getCurrentPathZvalue("moduleType", busType).substring(0, 2);
		}
	}

	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
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
	* 最新动态
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 最新动态
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 模块负责人
	* @return
	*/
	public Integer getOwner() {
		return owner;
	}

	/** 
	* 模块负责人
	* @param owner
	*/
	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	/** 
	* 模块更新人姓名
	* @return
	*/
	public String getModiferName() {
		return modiferName;
	}

	/** 
	* 模块更新人姓名
	* @param modiferName
	*/
	public void setModiferName(String modiferName) {
		this.modiferName = modiferName;
	}

	/** 
	* 模块更新时间
	* @return
	*/
	public String getModifTime() {
		return modifTime;
	}

	/** 
	* 模块更新时间
	* @param modifTime
	*/
	public void setModifTime(String modifTime) {
		this.modifTime = modifTime;
	}

	/** 
	* 是否查看过0没有
	* @return
	*/
	public String getIsRead() {
		return isRead;
	}

	/** 
	* 是否查看过0没有
	* @param isRead
	*/
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	/** 
	* 更新人主键
	* @return
	*/
	public Integer getModifyer() {
		return modifyer;
	}

	/** 
	* 更新人主键
	* @param modifyer
	*/
	public void setModifyer(Integer modifyer) {
		this.modifyer = modifyer;
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
	* 更新人uuid
	* @return
	*/
	public String getModifyerUuid() {
		return modifyerUuid;
	}

	/** 
	* 更新人uuid
	* @param modifyerUuid
	*/
	public void setModifyerUuid(String modifyerUuid) {
		this.modifyerUuid = modifyerUuid;
	}

	/** 
	* 分享信息msgShare表id
	* @return
	*/
	public Integer getMsgId() {
		return msgId;
	}

	/** 
	* 分享信息msgShare表id
	* @param msgId
	*/
	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	/** 
	* 状态
	* @return
	*/
	public Integer getStatus() {
		return status;
	}

	/** 
	* 状态
	* @param status
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	* 执行人
	* @return
	*/
	public Integer getExecutor() {
		return executor;
	}

	/** 
	* 执行人
	* @param executor
	*/
	public void setExecutor(Integer executor) {
		this.executor = executor;
	}

	/** 
	* 客户类型主键
	* @return
	*/
	public Integer getCustomerTypeId() {
		return customerTypeId;
	}

	/** 
	* 客户类型主键
	* @param customerTypeId
	*/
	public void setCustomerTypeId(Integer customerTypeId) {
		this.customerTypeId = customerTypeId;
	}
}
