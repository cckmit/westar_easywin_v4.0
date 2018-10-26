package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.UserHeadImg;
import com.westar.core.web.DataDicContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 复职管理
 */
@Table
@JsonInclude(Include.NON_NULL)
public class RsdaResume extends UserHeadImg {
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
	* 复职人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 担任职务
	*/
	@Filed
	private String userDuty;
	/** 
	* 拟复职日期
	*/
	@Filed
	private String resumeDate;
	/** 
	* 复职类型
	*/
	@Filed
	private String resumeType;
	/** 
	* 复职部门
	*/
	@Filed
	private Integer resumeDepId;
	/** 
	* 复职说明
	*/
	@Filed
	private String remark;

	/****************以上主要为系统表字段********************/
	/** 
	* 复职部门信息
	*/
	private String resumeDepName;
	/** 
	* 复职部门信息
	*/
	private String resumeTypeName;
	/** 
	* 复职信息的附件
	*/
	private List<RsdaResumeFile> listResumeFiles;
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
	* 复职人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 复职人员主键
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
	* 拟复职日期
	* @param resumeDate
	*/
	public void setResumeDate(String resumeDate) {
		this.resumeDate = resumeDate;
	}

	/** 
	* 拟复职日期
	* @return
	*/
	public String getResumeDate() {
		return resumeDate;
	}

	/** 
	* 复职类型
	* @param resumeType
	*/
	public void setResumeType(String resumeType) {
		this.resumeType = resumeType;
	}

	/** 
	* 复职类型
	* @return
	*/
	public String getResumeType() {
		return resumeType;
	}

	/** 
	* 复职部门
	* @param resumeDepId
	*/
	public void setResumeDepId(Integer resumeDepId) {
		this.resumeDepId = resumeDepId;
	}

	/** 
	* 复职部门
	* @return
	*/
	public Integer getResumeDepId() {
		return resumeDepId;
	}

	/** 
	* 复职说明
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	* 复职说明
	* @return
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 复职信息的附件
	* @return
	*/
	public List<RsdaResumeFile> getListResumeFiles() {
		return listResumeFiles;
	}

	/** 
	* 复职信息的附件
	* @param listResumeFiles
	*/
	public void setListResumeFiles(List<RsdaResumeFile> listResumeFiles) {
		this.listResumeFiles = listResumeFiles;
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
	* 复职部门信息
	* @return
	*/
	public String getResumeDepName() {
		return resumeDepName;
	}

	/** 
	* 复职部门信息
	* @param resumeDepName
	*/
	public void setResumeDepName(String resumeDepName) {
		this.resumeDepName = resumeDepName;
	}

	/** 
	* 复职部门信息
	* @return
	*/
	public String getResumeTypeName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("resumeType", resumeType);
	}

	/** 
	* 复职部门信息
	* @param resumeTypeName
	*/
	public void setResumeTypeName(String resumeTypeName) {
		this.resumeTypeName = resumeTypeName;
	}
}
