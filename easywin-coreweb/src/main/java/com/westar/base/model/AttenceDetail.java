package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 考勤记录按天
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AttenceDetail {
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
	* 人员编号
	*/
	@Filed
	private Integer userId;
	/** 
	* 关联记录主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 关联记录类型
	*/
	@Filed
	private String busType;
	/** 
	* 考勤日期
	*/
	@Filed
	private String attencDate;
	/** 
	* 考勤记录按天开始
	*/
	@Filed
	private String attenceStart;
	/** 
	* 考勤记录按天结束
	*/
	@Filed
	private String attenceEnd;
	/** 
	* 核实时间小时
	*/
	@Filed
	private String attencHour;

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
	* 人员编号
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 人员编号
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 关联记录主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 关联记录主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 关联记录类型
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 关联记录类型
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 考勤日期
	* @param attencDate
	*/
	public void setAttencDate(String attencDate) {
		this.attencDate = attencDate;
	}

	/** 
	* 考勤日期
	* @return
	*/
	public String getAttencDate() {
		return attencDate;
	}

	/** 
	* 考勤记录按天开始
	* @param attenceStart
	*/
	public void setAttenceStart(String attenceStart) {
		this.attenceStart = attenceStart;
	}

	/** 
	* 考勤记录按天开始
	* @return
	*/
	public String getAttenceStart() {
		return attenceStart;
	}

	/** 
	* 考勤记录按天结束
	* @param attenceEnd
	*/
	public void setAttenceEnd(String attenceEnd) {
		this.attenceEnd = attenceEnd;
	}

	/** 
	* 考勤记录按天结束
	* @return
	*/
	public String getAttenceEnd() {
		return attenceEnd;
	}

	/** 
	* 核实时间小时
	* @param attencHour
	*/
	public void setAttencHour(String attencHour) {
		this.attencHour = attencHour;
	}

	/** 
	* 核实时间小时
	* @return
	*/
	public String getAttencHour() {
		return attencHour;
	}
}
