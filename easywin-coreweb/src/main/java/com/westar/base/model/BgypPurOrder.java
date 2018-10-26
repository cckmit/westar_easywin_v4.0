package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 用品采购清单总
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BgypPurOrder {
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
	* 采购人
	*/
	@Filed
	private Integer purUserId;
	/** 
	* 用品价格
	*/
	@Filed
	private String bgypTotalPrice;
	/** 
	* 采购时间
	*/
	@Filed
	private String bgypPurDate;
	/** 
	* 采购单状态 0录入 1送审 2通过 3未通过 
	*/
	@Filed
	private String purOrderState;
	/** 
	* 记录员
	*/
	@Filed
	private Integer purRecorder;
	/** 
	* 备注
	*/
	@Filed
	private String content;
	/** 
	* 采购单编号
	*/
	@Filed
	private String purOrderNum;
	/** 
	* 审批人主键
	*/
	@Filed
	private Integer spUserId;
	/** 
	* 审批意见
	*/
	@Filed
	private String spContent;

	/****************以上主要为系统表字段********************/
	/** 
	* 采购人性别
	*/
	private String purUserName;
	/** 
	* 采购人性别
	*/
	private String purUserGender;
	/** 
	* 采购人头像文件
	*/
	private String purUserImgUuid;
	/** 
	* 采购人头像文件
	*/
	private String purUserImgName;
	/** 
	* 用品采购清单
	*/
	private List<BgypPurDetail> listBgypPurDetails;
	/** 
	* 采购单附件
	*/
	private List<BgypPurFile> listBgypPurFiles;
	/** 
	* 审核人性别
	*/
	private String spUserName;
	/** 
	* 审核人性别
	*/
	private String spUserGender;
	/** 
	* 审核人头像文件
	*/
	private String spUserImgUuid;
	/** 
	* 审核人头像文件
	*/
	private String spUserImgName;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;

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
	* 采购人
	* @param purUserId
	*/
	public void setPurUserId(Integer purUserId) {
		this.purUserId = purUserId;
	}

	/** 
	* 采购人
	* @return
	*/
	public Integer getPurUserId() {
		return purUserId;
	}

	/** 
	* 用品价格
	* @param bgypTotalPrice
	*/
	public void setBgypTotalPrice(String bgypTotalPrice) {
		this.bgypTotalPrice = bgypTotalPrice;
	}

	/** 
	* 用品价格
	* @return
	*/
	public String getBgypTotalPrice() {
		return bgypTotalPrice;
	}

	/** 
	* 采购人性别
	* @return
	*/
	public String getPurUserGender() {
		return purUserGender;
	}

	/** 
	* 采购人性别
	* @param purUserGender
	*/
	public void setPurUserGender(String purUserGender) {
		this.purUserGender = purUserGender;
	}

	/** 
	* 采购人头像文件
	* @return
	*/
	public String getPurUserImgUuid() {
		return purUserImgUuid;
	}

	/** 
	* 采购人头像文件
	* @param purUserImgUuid
	*/
	public void setPurUserImgUuid(String purUserImgUuid) {
		this.purUserImgUuid = purUserImgUuid;
	}

	/** 
	* 采购人头像文件
	* @return
	*/
	public String getPurUserImgName() {
		return purUserImgName;
	}

	/** 
	* 采购人头像文件
	* @param purUserImgName
	*/
	public void setPurUserImgName(String purUserImgName) {
		this.purUserImgName = purUserImgName;
	}

	/** 
	* 采购时间
	* @param bgypPurDate
	*/
	public void setBgypPurDate(String bgypPurDate) {
		this.bgypPurDate = bgypPurDate;
	}

	/** 
	* 采购时间
	* @return
	*/
	public String getBgypPurDate() {
		return bgypPurDate;
	}

	/** 
	* 采购人性别
	* @return
	*/
	public String getPurUserName() {
		return purUserName;
	}

	/** 
	* 采购人性别
	* @param purUserName
	*/
	public void setPurUserName(String purUserName) {
		this.purUserName = purUserName;
	}

	/** 
	* 用品采购清单
	* @return
	*/
	public List<BgypPurDetail> getListBgypPurDetails() {
		return listBgypPurDetails;
	}

	/** 
	* 用品采购清单
	* @param listBgypPurDetails
	*/
	public void setListBgypPurDetails(List<BgypPurDetail> listBgypPurDetails) {
		this.listBgypPurDetails = listBgypPurDetails;
	}

	/** 
	* 采购单状态 0录入 1送审 2通过 3未通过 
	* @param purOrderState
	*/
	public void setPurOrderState(String purOrderState) {
		this.purOrderState = purOrderState;
	}

	/** 
	* 采购单状态 0录入 1送审 2通过 3未通过 
	* @return
	*/
	public String getPurOrderState() {
		return purOrderState;
	}

	/** 
	* 记录员
	* @param purRecorder
	*/
	public void setPurRecorder(Integer purRecorder) {
		this.purRecorder = purRecorder;
	}

	/** 
	* 记录员
	* @return
	*/
	public Integer getPurRecorder() {
		return purRecorder;
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
	* 采购单附件
	* @return
	*/
	public List<BgypPurFile> getListBgypPurFiles() {
		return listBgypPurFiles;
	}

	/** 
	* 采购单附件
	* @param listBgypPurFiles
	*/
	public void setListBgypPurFiles(List<BgypPurFile> listBgypPurFiles) {
		this.listBgypPurFiles = listBgypPurFiles;
	}

	/** 
	* 备注
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 备注
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 采购单编号
	* @param purOrderNum
	*/
	public void setPurOrderNum(String purOrderNum) {
		this.purOrderNum = purOrderNum;
	}

	/** 
	* 采购单编号
	* @return
	*/
	public String getPurOrderNum() {
		return purOrderNum;
	}

	/** 
	* 审批人主键
	* @param spUserId
	*/
	public void setSpUserId(Integer spUserId) {
		this.spUserId = spUserId;
	}

	/** 
	* 审批人主键
	* @return
	*/
	public Integer getSpUserId() {
		return spUserId;
	}

	/** 
	* 审批意见
	* @param spContent
	*/
	public void setSpContent(String spContent) {
		this.spContent = spContent;
	}

	/** 
	* 审批意见
	* @return
	*/
	public String getSpContent() {
		return spContent;
	}

	/** 
	* 审核人性别
	* @return
	*/
	public String getSpUserName() {
		return spUserName;
	}

	/** 
	* 审核人性别
	* @param spUserName
	*/
	public void setSpUserName(String spUserName) {
		this.spUserName = spUserName;
	}

	/** 
	* 审核人性别
	* @return
	*/
	public String getSpUserGender() {
		return spUserGender;
	}

	/** 
	* 审核人性别
	* @param spUserGender
	*/
	public void setSpUserGender(String spUserGender) {
		this.spUserGender = spUserGender;
	}

	/** 
	* 审核人头像文件
	* @return
	*/
	public String getSpUserImgUuid() {
		return spUserImgUuid;
	}

	/** 
	* 审核人头像文件
	* @param spUserImgUuid
	*/
	public void setSpUserImgUuid(String spUserImgUuid) {
		this.spUserImgUuid = spUserImgUuid;
	}

	/** 
	* 审核人头像文件
	* @return
	*/
	public String getSpUserImgName() {
		return spUserImgName;
	}

	/** 
	* 审核人头像文件
	* @param spUserImgName
	*/
	public void setSpUserImgName(String spUserImgName) {
		this.spUserImgName = spUserImgName;
	}
}
