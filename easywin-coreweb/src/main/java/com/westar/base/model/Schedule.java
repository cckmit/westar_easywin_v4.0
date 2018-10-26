package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 日程
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Schedule {
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
	* 日程标题
	*/
	@Filed
	private String title;
	/** 
	* 日程设置时间起
	*/
	@Filed
	private String scheStartDate;
	/** 
	* 日程设置时间止
	*/
	@Filed
	private String scheEndDate;
	/** 
	* 是否为全天 默认是
	*/
	@Filed
	private Integer isAllDay;
	/** 
	* 是否重复 默认不是
	*/
	@Filed
	private Integer isRepeat;
	/** 
	* 重复类型
	*/
	@Filed
	private String repType;
	/** 
	* 重复时间
	*/
	@Filed
	private String repDate;
	/** 
	* 日程结束类型
	*/
	@Filed
	private String repEndType;
	/** 
	* 日程结束时间
	*/
	@Filed
	private String repEndDate;
	/** 
	* 日程地点
	*/
	@Filed
	private String address;
	/** 
	* 日程详情
	*/
	@Filed
	private String content;
	/** 
	* 公开程度
	*/
	@Filed
	private Integer publicType;
	/** 
	* 日程执行时间起
	*/
	@Filed
	private String dataTimeS;
	/** 
	* 日程执行时间止
	*/
	@Filed
	private String dataTimeE;

	/****************以上主要为系统表字段********************/
	/** 
	* 日程创建人姓名
	*/
	private String userName;
	/** 
	* 业务主键
	*/
	private Integer busId;
	/** 
	* 业务类型
	*/
	private String busType;

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
	* 日程标题
	* @param title
	*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** 
	* 日程标题
	* @return
	*/
	public String getTitle() {
		return title;
	}

	/** 
	* 日程设置时间起
	* @param scheStartDate
	*/
	public void setScheStartDate(String scheStartDate) {
		this.scheStartDate = scheStartDate;
	}

	/** 
	* 日程设置时间起
	* @return
	*/
	public String getScheStartDate() {
		return scheStartDate;
	}

	/** 
	* 日程设置时间止
	* @param scheEndDate
	*/
	public void setScheEndDate(String scheEndDate) {
		this.scheEndDate = scheEndDate;
	}

	/** 
	* 日程设置时间止
	* @return
	*/
	public String getScheEndDate() {
		return scheEndDate;
	}

	/** 
	* 是否为全天 默认是
	* @param isAllDay
	*/
	public void setIsAllDay(Integer isAllDay) {
		this.isAllDay = isAllDay;
	}

	/** 
	* 是否为全天 默认是
	* @return
	*/
	public Integer getIsAllDay() {
		return isAllDay;
	}

	/** 
	* 是否重复 默认不是
	* @param isRepeat
	*/
	public void setIsRepeat(Integer isRepeat) {
		this.isRepeat = isRepeat;
	}

	/** 
	* 是否重复 默认不是
	* @return
	*/
	public Integer getIsRepeat() {
		return isRepeat;
	}

	/** 
	* 重复类型
	* @param repType
	*/
	public void setRepType(String repType) {
		this.repType = repType;
	}

	/** 
	* 重复类型
	* @return
	*/
	public String getRepType() {
		return repType;
	}

	/** 
	* 重复时间
	* @param repDate
	*/
	public void setRepDate(String repDate) {
		this.repDate = repDate;
	}

	/** 
	* 重复时间
	* @return
	*/
	public String getRepDate() {
		return repDate;
	}

	/** 
	* 日程结束类型
	* @param repEndType
	*/
	public void setRepEndType(String repEndType) {
		this.repEndType = repEndType;
	}

	/** 
	* 日程结束类型
	* @return
	*/
	public String getRepEndType() {
		return repEndType;
	}

	/** 
	* 日程结束时间
	* @param repEndDate
	*/
	public void setRepEndDate(String repEndDate) {
		this.repEndDate = repEndDate;
	}

	/** 
	* 日程结束时间
	* @return
	*/
	public String getRepEndDate() {
		return repEndDate;
	}

	/** 
	* 日程地点
	* @param address
	*/
	public void setAddress(String address) {
		this.address = address;
	}

	/** 
	* 日程地点
	* @return
	*/
	public String getAddress() {
		return address;
	}

	/** 
	* 日程详情
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 日程详情
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 日程创建人姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 日程创建人姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 业务类型
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 业务类型
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 公开程度
	* @param publicType
	*/
	public void setPublicType(Integer publicType) {
		this.publicType = publicType;
	}

	/** 
	* 公开程度
	* @return
	*/
	public Integer getPublicType() {
		return publicType;
	}

	/** 
	* 日程执行时间起
	* @param dataTimeS
	*/
	public void setDataTimeS(String dataTimeS) {
		this.dataTimeS = dataTimeS;
	}

	/** 
	* 日程执行时间起
	* @return
	*/
	public String getDataTimeS() {
		return dataTimeS;
	}

	/** 
	* 日程执行时间止
	* @param dataTimeE
	*/
	public void setDataTimeE(String dataTimeE) {
		this.dataTimeE = dataTimeE;
	}

	/** 
	* 日程执行时间止
	* @return
	*/
	public String getDataTimeE() {
		return dataTimeE;
	}

	/** 
	* 业务主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 业务主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}
}
