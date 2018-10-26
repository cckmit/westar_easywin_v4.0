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
 * 人事档案奖惩
 */
@Table
@JsonInclude(Include.NON_NULL)
public class RsdaIncentive extends UserHeadImg {
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
	* 档案人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 奖惩类型
	*/
	@Filed
	private String incentiveType;
	/** 
	* 奖惩日期
	*/
	@Filed
	private String incentiveDate;
	/** 
	* 奖惩说明
	*/
	@Filed
	private String remark;
	/** 
	* 奖惩项目
	*/
	@Filed
	private String incName;

	/****************以上主要为系统表字段********************/
	/** 
	* 奖惩机制的附件集合
	*/
	private List<RsdaIncFile> listRsdaIncFiles;
	/** 
	* 奖惩类型名称
	*/
	private String incTypeName;
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
	* 档案人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 档案人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 奖惩类型
	* @param incentiveType
	*/
	public void setIncentiveType(String incentiveType) {
		this.incentiveType = incentiveType;
	}

	/** 
	* 奖惩类型
	* @return
	*/
	public String getIncentiveType() {
		return incentiveType;
	}

	/** 
	* 奖惩日期
	* @param incentiveDate
	*/
	public void setIncentiveDate(String incentiveDate) {
		this.incentiveDate = incentiveDate;
	}

	/** 
	* 奖惩日期
	* @return
	*/
	public String getIncentiveDate() {
		return incentiveDate;
	}

	/** 
	* 奖惩说明
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	* 奖惩说明
	* @return
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 奖惩机制的附件集合
	* @return
	*/
	public List<RsdaIncFile> getListRsdaIncFiles() {
		return listRsdaIncFiles;
	}

	/** 
	* 奖惩机制的附件集合
	* @param listRsdaIncFiles
	*/
	public void setListRsdaIncFiles(List<RsdaIncFile> listRsdaIncFiles) {
		this.listRsdaIncFiles = listRsdaIncFiles;
	}

	/** 
	* 奖惩项目
	* @param incName
	*/
	public void setIncName(String incName) {
		this.incName = incName;
	}

	/** 
	* 奖惩项目
	* @return
	*/
	public String getIncName() {
		return incName;
	}

	/** 
	* 奖惩类型名称
	* @return
	*/
	public String getIncTypeName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("incType", incentiveType);
	}

	/** 
	* 奖惩类型名称
	* @param incTypeName
	*/
	public void setIncTypeName(String incTypeName) {
		this.incTypeName = incTypeName;
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
}
