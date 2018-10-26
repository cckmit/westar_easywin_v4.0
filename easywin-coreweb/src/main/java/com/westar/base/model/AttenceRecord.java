package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 考勤记录
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AttenceRecord {
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
	private String enrollNumber;
	/** 
	* 考勤时间
	*/
	@Filed
	private String time;
	/** 
	* 考勤模式 0为密码验证/1为指纹验证/2为卡验证
	*/
	@Filed
	private Integer verifyMode;
	/** 
	* 考勤类型 
	*/
	@Filed
	private Integer inOutMode;
	/** 
	* 考勤状态 
	*/
	@Filed
	private Integer state;

	/****************以上主要为系统表字段********************/
	/** 
	* 人名
	*/
	private String userName;
	/** 
	* 人员主键
	*/
	private Integer userId;
	/** 
	* 签到时间
	*/
	private String dayTimeS = "";
	/** 
	* 签退时间
	*/
	private String dayTimeE = "";
	/** 
	* 记录类型0未打卡/1加班/2请假/3出差/4休假
	*/
	private Integer recordType;
	/** 
	* 考勤类型1有记录/0无记录
	*/
	private Integer attenceType;
	/** 
	* 异常类型0未同步数据/1签到时间异常/2签退时间异常
	*/
	private Integer unusualType;
	/** 
	* 查询开始日期
	*/
	private String startDate;
	/** 
	* 查询截止日期
	*/
	private String endDate;
	/** 
	* 考勤人员集合
	*/
	private List<UserInfo> listCreator;
	/** 
	* 查询日期
	*/
	private String searchDate;
	/** 
	* 查询部门
	*/
	private List<Department> listDep;
	/** 
	* 时间排序
	*/
	private String orderBy;

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
	* 考勤时间
	* @param time
	*/
	public void setTime(String time) {
		this.time = time;
	}

	/** 
	* 考勤时间
	* @return
	*/
	public String getTime() {
		return time;
	}

	/** 
	* 考勤模式 0为密码验证/1为指纹验证/2为卡验证
	* @param verifyMode
	*/
	public void setVerifyMode(Integer verifyMode) {
		this.verifyMode = verifyMode;
	}

	/** 
	* 考勤模式 0为密码验证/1为指纹验证/2为卡验证
	* @return
	*/
	public Integer getVerifyMode() {
		return verifyMode;
	}

	/** 
	* 考勤类型 
	* @param inOutMode
	*/
	public void setInOutMode(Integer inOutMode) {
		this.inOutMode = inOutMode;
	}

	/** 
	* 考勤类型 
	* @return
	*/
	public Integer getInOutMode() {
		return inOutMode;
	}

	/** 
	* 人名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 人名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 记录类型0未打卡/1加班/2请假/3出差/4休假
	* @return
	*/
	public Integer getRecordType() {
		return recordType;
	}

	/** 
	* 记录类型0未打卡/1加班/2请假/3出差/4休假
	* @param recordType
	*/
	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}

	/** 
	* 考勤类型1有记录/0无记录
	* @return
	*/
	public Integer getAttenceType() {
		return attenceType;
	}

	/** 
	* 考勤类型1有记录/0无记录
	* @param attenceType
	*/
	public void setAttenceType(Integer attenceType) {
		this.attenceType = attenceType;
	}

	/** 
	* 签到时间
	* @return
	*/
	public String getDayTimeS() {
		return dayTimeS;
	}

	/** 
	* 签到时间
	* @param dayTimeS
	*/
	public void setDayTimeS(String dayTimeS) {
		this.dayTimeS = dayTimeS;
	}

	/** 
	* 签退时间
	* @return
	*/
	public String getDayTimeE() {
		return dayTimeE;
	}

	/** 
	* 签退时间
	* @param dayTimeE
	*/
	public void setDayTimeE(String dayTimeE) {
		this.dayTimeE = dayTimeE;
	}

	/** 
	* 异常类型0未同步数据/1签到时间异常/2签退时间异常
	* @return
	*/
	public Integer getUnusualType() {
		return unusualType;
	}

	/** 
	* 异常类型0未同步数据/1签到时间异常/2签退时间异常
	* @param unusualType
	*/
	public void setUnusualType(Integer unusualType) {
		this.unusualType = unusualType;
	}

	/** 
	* 查询开始日期
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询开始日期
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询截止日期
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询截止日期
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 考勤人员集合
	* @return
	*/
	public List<UserInfo> getListCreator() {
		return listCreator;
	}

	/** 
	* 考勤人员集合
	* @param listCreator
	*/
	public void setListCreator(List<UserInfo> listCreator) {
		this.listCreator = listCreator;
	}

	/** 
	* 查询日期
	* @return
	*/
	public String getSearchDate() {
		return searchDate;
	}

	/** 
	* 查询日期
	* @param searchDate
	*/
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}

	/** 
	* 查询部门
	* @return
	*/
	public List<Department> getListDep() {
		return listDep;
	}

	/** 
	* 查询部门
	* @param listDep
	*/
	public void setListDep(List<Department> listDep) {
		this.listDep = listDep;
	}

	/** 
	* 时间排序
	* @return
	*/
	public String getOrderBy() {
		return orderBy;
	}

	/** 
	* 时间排序
	* @param orderBy
	*/
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/** 
	* 考勤状态 
	* @param state
	*/
	public void setState(Integer state) {
		this.state = state;
	}

	/** 
	* 考勤状态 
	* @return
	*/
	public Integer getState() {
		return state;
	}

	@Override
	public String toString() {
		return "AttenceRecord [id=" + id + ", recordCreateTime=" + recordCreateTime + ", comId=" + comId
				+ ", enrollNumber=" + enrollNumber + ", time=" + time + ", verifyMode=" + verifyMode + ", inOutMode="
				+ inOutMode + ", state=" + state + ", userName=" + userName + ", userId=" + userId + ", dayTimeS="
				+ dayTimeS + ", dayTimeE=" + dayTimeE + ", recordType=" + recordType + ", attenceType=" + attenceType
				+ ", unusualType=" + unusualType + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", listCreator=" + listCreator + ", searchDate=" + searchDate + ", listDep=" + listDep + ", orderBy="
				+ orderBy + "]";
	}
}
