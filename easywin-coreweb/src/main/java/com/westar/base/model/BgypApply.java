package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.UserHeadImg;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 用品申领
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BgypApply extends UserHeadImg {
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
	* 申领类型
	*/
	@Filed
	private String applyType;
	/** 
	* 申领人员主键
	*/
	@Filed
	private Integer applyUserId;
	/** 
	* 备注
	*/
	@Filed
	private String remark;
	/** 
	* 申领审核
	*/
	@Filed
	private String applyCheckState;
	/** 
	* 申领时间
	*/
	@Filed
	private String applyDate;
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
	* 申领详情
	*/
	private List<BgypApplyDetail> listBgypApplyDetails;
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
	* 查询的开始时间
	*/
	private String startDate;
	/** 
	* 查询的结束时间
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
	* 申领类型
	* @param applyType
	*/
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	/** 
	* 申领类型
	* @return
	*/
	public String getApplyType() {
		return applyType;
	}

	/** 
	* 申领人员主键
	* @param applyUserId
	*/
	public void setApplyUserId(Integer applyUserId) {
		this.applyUserId = applyUserId;
	}

	/** 
	* 申领人员主键
	* @return
	*/
	public Integer getApplyUserId() {
		return applyUserId;
	}

	/** 
	* 备注
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	* 备注
	* @return
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 申领详情
	* @return
	*/
	public List<BgypApplyDetail> getListBgypApplyDetails() {
		return listBgypApplyDetails;
	}

	/** 
	* 申领详情
	* @param listBgypApplyDetails
	*/
	public void setListBgypApplyDetails(List<BgypApplyDetail> listBgypApplyDetails) {
		this.listBgypApplyDetails = listBgypApplyDetails;
	}

	/** 
	* 申领审核
	* @param applyCheckState
	*/
	public void setApplyCheckState(String applyCheckState) {
		this.applyCheckState = applyCheckState;
	}

	/** 
	* 申领审核
	* @return
	*/
	public String getApplyCheckState() {
		return applyCheckState;
	}

	/** 
	* 申领时间
	* @param applyDate
	*/
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	/** 
	* 申领时间
	* @return
	*/
	public String getApplyDate() {
		return applyDate;
	}

	/** 
	* 查询的开始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询的开始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询的结束时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的结束时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
