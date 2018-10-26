package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 节假日维护
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FestMod {
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
	* 企业编号 0为系统默认
	*/
	@Filed
	private Integer comId;
	/** 
	* 年
	*/
	@Filed
	private Integer year;
	/** 
	* 节日名称
	*/
	@Filed
	private String festName;
	/** 
	* 描述
	*/
	@Filed
	private String describe;
	/** 
	* 节日时间
	*/
	@Filed
	private String festival;

	/****************以上主要为系统表字段********************/
	/** 
	* 工作状态1休息日2工作日
	*/
	private String status;
	/** 
	* 节假日日期
	*/
	private List<FestModDate> listFestModDates;
	/** 
	* 日期开始
	*/
	private String dayTimeS;
	/** 
	* 日期结束
	*/
	private String dayTimeE;

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
	* 企业编号 0为系统默认
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 企业编号 0为系统默认
	* @return
	*/
	public Integer getComId() {
		return comId;
	}

	/** 
	* 年
	* @param year
	*/
	public void setYear(Integer year) {
		this.year = year;
	}

	/** 
	* 年
	* @return
	*/
	public Integer getYear() {
		return year;
	}

	/** 
	* 节日名称
	* @param festName
	*/
	public void setFestName(String festName) {
		this.festName = festName;
	}

	/** 
	* 节日名称
	* @return
	*/
	public String getFestName() {
		return festName;
	}

	/** 
	* 描述
	* @param describe
	*/
	public void setDescribe(String describe) {
		this.describe = describe;
	}

	/** 
	* 描述
	* @return
	*/
	public String getDescribe() {
		return describe;
	}

	/** 
	* 节假日日期
	* @return
	*/
	public List<FestModDate> getListFestModDates() {
		return listFestModDates;
	}

	/** 
	* 节假日日期
	* @param listFestModDates
	*/
	public void setListFestModDates(List<FestModDate> listFestModDates) {
		this.listFestModDates = listFestModDates;
	}

	/** 
	* 节日时间
	* @param festival
	*/
	public void setFestival(String festival) {
		this.festival = festival;
	}

	/** 
	* 节日时间
	* @return
	*/
	public String getFestival() {
		return festival;
	}

	/** 
	* 工作状态1休息日2工作日
	* @return
	*/
	public String getStatus() {
		return status;
	}

	/** 
	* 工作状态1休息日2工作日
	* @param status
	*/
	public void setStatus(String status) {
		this.status = status;
	}

	/** 
	* 日期开始
	* @return
	*/
	public String getDayTimeS() {
		return dayTimeS;
	}

	/** 
	* 日期开始
	* @param dayTimeS
	*/
	public void setDayTimeS(String dayTimeS) {
		this.dayTimeS = dayTimeS;
	}

	/** 
	* 日期结束
	* @return
	*/
	public String getDayTimeE() {
		return dayTimeE;
	}

	/** 
	* 日期结束
	* @param dayTimeE
	*/
	public void setDayTimeE(String dayTimeE) {
		this.dayTimeE = dayTimeE;
	}
}
