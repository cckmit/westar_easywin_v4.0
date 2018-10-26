package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.UserHeadImg;
import com.westar.core.web.DataDicContext;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 离职管理
 */
@Table
@JsonInclude(Include.NON_NULL)
public class RsdaLeave extends UserHeadImg {
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
	* 调动人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 担任职务
	*/
	@Filed
	private String userDuty;
	/** 
	* 合同到期日
	*/
	@Filed
	private String pactEndDate;
	/** 
	* 拟离职日期
	*/
	@Filed
	private String leaveDate;
	/** 
	* 离职类型
	*/
	@Filed
	private String leaveType;
	/** 
	* 备注
	*/
	@Filed
	private String remark;
	/** 
	* 离职原因
	*/
	@Filed
	private String leaveReason;

	/****************以上主要为系统表字段********************/
	/** 
	* 奖惩机制的附件集合
	*/
	private List<RsdaLeaveFile> listRsdaLeaveFiles;
	/** 
	* 奖惩类型名称
	*/
	private String leaveTypeName;
	/** 
	* 查询的开始时间
	*/
	private String startDate;
	/** 
	* 查询的截止时间
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
	* 调动人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 调动人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 担任职务
	* @param userDuty
	*/
	public void setUserDuty(String userDuty) {
		this.userDuty = userDuty;
	}

	/** 
	* 担任职务
	* @return
	*/
	public String getUserDuty() {
		return userDuty;
	}

	/** 
	* 合同到期日
	* @param pactEndDate
	*/
	public void setPactEndDate(String pactEndDate) {
		this.pactEndDate = pactEndDate;
	}

	/** 
	* 合同到期日
	* @return
	*/
	public String getPactEndDate() {
		return pactEndDate;
	}

	/** 
	* 拟离职日期
	* @param leaveDate
	*/
	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	/** 
	* 拟离职日期
	* @return
	*/
	public String getLeaveDate() {
		return leaveDate;
	}

	/** 
	* 离职类型
	* @param leaveType
	*/
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	/** 
	* 离职类型
	* @return
	*/
	public String getLeaveType() {
		return leaveType;
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
	* 奖惩机制的附件集合
	* @return
	*/
	public List<RsdaLeaveFile> getListRsdaLeaveFiles() {
		return listRsdaLeaveFiles;
	}

	/** 
	* 奖惩机制的附件集合
	* @param listRsdaLeaveFiles
	*/
	public void setListRsdaLeaveFiles(List<RsdaLeaveFile> listRsdaLeaveFiles) {
		this.listRsdaLeaveFiles = listRsdaLeaveFiles;
	}

	/** 
	* 奖惩类型名称
	* @return
	*/
	public String getLeaveTypeName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("leaveType", leaveType);
	}

	/** 
	* 奖惩类型名称
	* @param leaveTypeName
	*/
	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
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
	* 查询的截止时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的截止时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 离职原因
	* @param leaveReason
	*/
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	/** 
	* 离职原因
	* @return
	*/
	public String getLeaveReason() {
		return leaveReason;
	}
}
