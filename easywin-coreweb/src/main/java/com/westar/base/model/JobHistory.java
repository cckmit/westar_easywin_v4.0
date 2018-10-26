package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.UserHeadImg;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 工作经历
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JobHistory extends UserHeadImg {
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
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 担任职务
	*/
	@Filed
	private String dutyed;
	/** 
	* 所在部门
	*/
	@Filed
	private String depName;
	/** 
	* 证明人
	*/
	@Filed
	private String retereName;
	/** 
	* 开始时间
	*/
	@Filed
	private String startDate;
	/** 
	* 结束时间
	*/
	@Filed
	private String endDate;
	/** 
	* 公司名称
	*/
	@Filed
	private String componName;
	/** 
	* 主要工作
	*/
	@Filed
	private String achievement;
	/** 
	* 备注
	*/
	@Filed
	private String remark;

	/****************以上主要为系统表字段********************/
	/** 
	* 工作经历附件证明
	*/
	private List<JobHisFile> listJobHisFiles;

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
	* 人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 担任职务
	* @param dutyed
	*/
	public void setDutyed(String dutyed) {
		this.dutyed = dutyed;
	}

	/** 
	* 担任职务
	* @return
	*/
	public String getDutyed() {
		return dutyed;
	}

	/** 
	* 所在部门
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/** 
	* 所在部门
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 证明人
	* @param retereName
	*/
	public void setRetereName(String retereName) {
		this.retereName = retereName;
	}

	/** 
	* 证明人
	* @return
	*/
	public String getRetereName() {
		return retereName;
	}

	/** 
	* 开始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 开始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 结束时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 结束时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 公司名称
	* @param componName
	*/
	public void setComponName(String componName) {
		this.componName = componName;
	}

	/** 
	* 公司名称
	* @return
	*/
	public String getComponName() {
		return componName;
	}

	/** 
	* 主要工作
	* @param achievement
	*/
	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}

	/** 
	* 主要工作
	* @return
	*/
	public String getAchievement() {
		return achievement;
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
	* 工作经历附件证明
	* @return
	*/
	public List<JobHisFile> getListJobHisFiles() {
		return listJobHisFiles;
	}

	/** 
	* 工作经历附件证明
	* @param listJobHisFiles
	*/
	public void setListJobHisFiles(List<JobHisFile> listJobHisFiles) {
		this.listJobHisFiles = listJobHisFiles;
	}
}
