package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 每年的日期表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SysAllDate {
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
	* 年份
	*/
	@Filed
	private String year;
	/** 
	* 年份的每一天的日期
	*/
	@Filed
	private String yearDate;
	/** 
	* 年的第几天
	*/
	@Filed
	private Integer dateOfYear;

	/****************以上主要为系统表字段********************/

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
	* 年份
	* @param year
	*/
	public void setYear(String year) {
		this.year = year;
	}

	/** 
	* 年份
	* @return
	*/
	public String getYear() {
		return year;
	}

	/** 
	* 年份的每一天的日期
	* @param yearDate
	*/
	public void setYearDate(String yearDate) {
		this.yearDate = yearDate;
	}

	/** 
	* 年份的每一天的日期
	* @return
	*/
	public String getYearDate() {
		return yearDate;
	}

	/** 
	* 年的第几天
	* @param dateOfYear
	*/
	public void setDateOfYear(Integer dateOfYear) {
		this.dateOfYear = dateOfYear;
	}

	/** 
	* 年的第几天
	* @return
	*/
	public Integer getDateOfYear() {
		return dateOfYear;
	}
}
