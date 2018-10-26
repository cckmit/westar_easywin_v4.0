package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 会议周期
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MeetRegular {
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
	* 会议主键
	*/
	@Filed
	private Integer meetingId;
	/** 
	* 重复时间
	*/
	@Filed
	private String regularDate;
	/** 
	* 会议开始时间
	*/
	@Filed
	private String startDate;
	/** 
	* 会议结束时间
	*/
	@Filed
	private String endDate;

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
	* 会议主键
	* @param meetingId
	*/
	public void setMeetingId(Integer meetingId) {
		this.meetingId = meetingId;
	}

	/** 
	* 会议主键
	* @return
	*/
	public Integer getMeetingId() {
		return meetingId;
	}

	/** 
	* 重复时间
	* @param regularDate
	*/
	public void setRegularDate(String regularDate) {
		this.regularDate = regularDate;
	}

	/** 
	* 重复时间
	* @return
	*/
	public String getRegularDate() {
		return regularDate;
	}

	/** 
	* 会议开始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 会议开始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 会议结束时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 会议结束时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}
}
