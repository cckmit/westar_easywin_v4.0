package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 打卡记录按天
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AttenceRecordDetail {
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
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 人员编号
	*/
	@Filed
	private String enrollNumber;
	/** 
	* 考勤日期
	*/
	@Filed
	private String attencDate;
	/** 
	* 考勤类型-1未打卡/0正常/1加班/2请假/3出差/4迟到/5早退/6旷工
	*/
	@Filed
	private String recordType;
	/** 
	* 异常数据处理次数
	*/
	@Filed
	private Integer times;

	/****************以上主要为系统表字段********************/
	/** 
	* 打卡时间
	*/
	private String time;

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
	* 人员编号
	* @param enrollNumber
	*/
	public void setEnrollNumber(String enrollNumber) {
		this.enrollNumber = enrollNumber;
	}

	/** 
	* 人员编号
	* @return
	*/
	public String getEnrollNumber() {
		return enrollNumber;
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
	* 考勤类型-1未打卡/0正常/1加班/2请假/3出差/4迟到/5早退/6旷工
	* @param recordType
	*/
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	/** 
	* 考勤类型-1未打卡/0正常/1加班/2请假/3出差/4迟到/5早退/6旷工
	* @return
	*/
	public String getRecordType() {
		return recordType;
	}

	/** 
	* 打卡时间
	* @return
	*/
	public String getTime() {
		return time;
	}

	/** 
	* 打卡时间
	* @param time
	*/
	public void setTime(String time) {
		this.time = time;
	}

	/** 
	* 异常数据处理次数
	* @param times
	*/
	public void setTimes(Integer times) {
		this.times = times;
	}

	/** 
	* 异常数据处理次数
	* @return
	*/
	public Integer getTimes() {
		return times;
	}
}
