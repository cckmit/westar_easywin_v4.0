package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 周报内容要求
 */
@Table
@JsonInclude(Include.NON_NULL)
public class WeekReportQ {
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
	* 周报主键
	*/
	@Filed
	private Integer weekReportId;
	/** 
	* 周报要求
	*/
	@Filed
	private String modReportName;
	/** 
	* 字段名称，用于取值DATA_${year}${week}_${id}
	*/
	@Filed
	private String reportName;
	/** 
	* 是否必填，默认不是
	*/
	@Filed
	private String isRequire;

	/****************以上主要为系统表字段********************/
	/** 
	* 周报条目值
	*/
	private String reportVal;

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
	* 周报主键
	* @param weekReportId
	*/
	public void setWeekReportId(Integer weekReportId) {
		this.weekReportId = weekReportId;
	}

	/** 
	* 周报主键
	* @return
	*/
	public Integer getWeekReportId() {
		return weekReportId;
	}

	/** 
	* 周报要求
	* @param modReportName
	*/
	public void setModReportName(String modReportName) {
		this.modReportName = modReportName;
	}

	/** 
	* 周报要求
	* @return
	*/
	public String getModReportName() {
		return modReportName;
	}

	/** 
	* 字段名称，用于取值DATA_${year}${week}_${id}
	* @param reportName
	*/
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/** 
	* 字段名称，用于取值DATA_${year}${week}_${id}
	* @return
	*/
	public String getReportName() {
		return reportName;
	}

	/** 
	* 周报条目值
	* @return
	*/
	public String getReportVal() {
		return reportVal;
	}

	/** 
	* 周报条目值
	* @param reportVal
	*/
	public void setReportVal(String reportVal) {
		this.reportVal = reportVal;
	}

	/** 
	* 是否必填，默认不是
	* @param isRequire
	*/
	public void setIsRequire(String isRequire) {
		this.isRequire = isRequire;
	}

	/** 
	* 是否必填，默认不是
	* @return
	*/
	public String getIsRequire() {
		return isRequire;
	}
}
