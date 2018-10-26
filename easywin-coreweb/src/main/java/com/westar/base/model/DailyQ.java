package com.westar.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;

/** 
 * 分享内容要求
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DailyQ {
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
	* 分享主键
	*/
	@Filed
	private Integer dailyId;
	/** 
	* 分享要求
	*/
	@Filed
	private String dailyName;
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
	/** 
	* 是否为系统模板 0 不是 1是
	*/
	@Filed
	private Integer sysState;

	/****************以上主要为系统表字段********************/
	/** 
	* 分享条目值
	*/
	private String dailyVal;

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
	* 分享主键
	* @param dailyId
	*/
	public void setDailyId(Integer dailyId) {
		this.dailyId = dailyId;
	}

	/** 
	* 分享主键
	* @return
	*/
	public Integer getDailyId() {
		return dailyId;
	}

	/** 
	* 分享要求
	* @param dailyName
	*/
	public void setDailyName(String dailyName) {
		this.dailyName = dailyName;
	}

	/** 
	* 分享要求
	* @return
	*/
	public String getDailyName() {
		return dailyName;
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

	/** 
	* 分享条目值
	* @return
	*/
	public String getDailyVal() {
		return dailyVal;
	}

	/** 
	* 分享条目值
	* @param dailyVal
	*/
	public void setDailyVal(String dailyVal) {
		this.dailyVal = dailyVal;
	}

	/** 
	* 是否为系统模板 0 不是 1是
	* @param sysState
	*/
	public void setSysState(Integer sysState) {
		this.sysState = sysState;
	}

	/** 
	* 是否为系统模板 0 不是 1是
	* @return
	*/
	public Integer getSysState() {
		return sysState;
	}
}
