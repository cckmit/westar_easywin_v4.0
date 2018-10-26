package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 车辆使用记录
 */
@Table
@JsonInclude(Include.NON_NULL)
public class CarUseRecord {
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
	* 车辆主键
	*/
	@Filed
	private Integer carId;
	/** 
	* 开始日期
	*/
	@Filed
	private String startDate;
	/** 
	* 结束日期
	*/
	@Filed
	private String endDate;
	/** 
	* 事由
	*/
	@Filed
	private String reason;
	/** 
	* 目的地
	*/
	@Filed
	private String destination;
	/** 
	* 预算里程--km
	*/
	@Filed
	private String predictJourney;
	/** 
	* 实际里程--km
	*/
	@Filed
	private String realJourney;
	/** 
	* 耗油量--l
	*/
	@Filed
	private String oilConsumption;
	/** 
	* 状态0申请/1使用中/2已归还/3申请拒绝/4撤回申请
	*/
	@Filed
	private Integer state;
	/** 
	* 申请人
	*/
	@Filed
	private Integer applyer;
	/** 
	* 否决原因
	*/
	@Filed
	private String voteReason;

	/****************以上主要为系统表字段********************/
	/** 
	* 车辆名称
	*/
	private String carName;
	/** 
	* 车牌号
	*/
	private String carNum;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 委派人姓名
	*/
	private String applyerName;
	/** 
	* 上传人头像uuid
	*/
	private String userUuid;
	/** 
	* 上传人头像名称
	*/
	private String userFileName;
	/** 
	* 搜索关键字
	*/
	private String searchLike;

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
	* 车辆主键
	* @param carId
	*/
	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	/** 
	* 车辆主键
	* @return
	*/
	public Integer getCarId() {
		return carId;
	}

	/** 
	* 开始日期
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 开始日期
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 结束日期
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 结束日期
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 事由
	* @param reason
	*/
	public void setReason(String reason) {
		this.reason = reason;
	}

	/** 
	* 事由
	* @return
	*/
	public String getReason() {
		return reason;
	}

	/** 
	* 目的地
	* @param destination
	*/
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/** 
	* 目的地
	* @return
	*/
	public String getDestination() {
		return destination;
	}

	/** 
	* 预算里程--km
	* @param predictJourney
	*/
	public void setPredictJourney(String predictJourney) {
		this.predictJourney = predictJourney;
	}

	/** 
	* 预算里程--km
	* @return
	*/
	public String getPredictJourney() {
		return predictJourney;
	}

	/** 
	* 实际里程--km
	* @param realJourney
	*/
	public void setRealJourney(String realJourney) {
		this.realJourney = realJourney;
	}

	/** 
	* 实际里程--km
	* @return
	*/
	public String getRealJourney() {
		return realJourney;
	}

	/** 
	* 耗油量--l
	* @param oilConsumption
	*/
	public void setOilConsumption(String oilConsumption) {
		this.oilConsumption = oilConsumption;
	}

	/** 
	* 耗油量--l
	* @return
	*/
	public String getOilConsumption() {
		return oilConsumption;
	}

	/** 
	* 状态0申请/1使用中/2已归还/3申请拒绝/4撤回申请
	* @param state
	*/
	public void setState(Integer state) {
		this.state = state;
	}

	/** 
	* 状态0申请/1使用中/2已归还/3申请拒绝/4撤回申请
	* @return
	*/
	public Integer getState() {
		return state;
	}

	/** 
	* 申请人
	* @param applyer
	*/
	public void setApplyer(Integer applyer) {
		this.applyer = applyer;
	}

	/** 
	* 申请人
	* @return
	*/
	public Integer getApplyer() {
		return applyer;
	}

	/** 
	* 车辆名称
	* @return
	*/
	public String getCarName() {
		return carName;
	}

	/** 
	* 车辆名称
	* @param carName
	*/
	public void setCarName(String carName) {
		this.carName = carName;
	}

	/** 
	* 车牌号
	* @return
	*/
	public String getCarNum() {
		return carNum;
	}

	/** 
	* 车牌号
	* @param carNum
	*/
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 0女1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 委派人姓名
	* @return
	*/
	public String getApplyerName() {
		return applyerName;
	}

	/** 
	* 委派人姓名
	* @param applyerName
	*/
	public void setApplyerName(String applyerName) {
		this.applyerName = applyerName;
	}

	/** 
	* 上传人头像uuid
	* @return
	*/
	public String getUserUuid() {
		return userUuid;
	}

	/** 
	* 上传人头像uuid
	* @param userUuid
	*/
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	/** 
	* 上传人头像名称
	* @return
	*/
	public String getUserFileName() {
		return userFileName;
	}

	/** 
	* 上传人头像名称
	* @param userFileName
	*/
	public void setUserFileName(String userFileName) {
		this.userFileName = userFileName;
	}

	/** 
	* 否决原因
	* @param voteReason
	*/
	public void setVoteReason(String voteReason) {
		this.voteReason = voteReason;
	}

	/** 
	* 否决原因
	* @return
	*/
	public String getVoteReason() {
		return voteReason;
	}

	/** 
	* 搜索关键字
	* @return
	*/
	public String getSearchLike() {
		return searchLike;
	}

	/** 
	* 搜索关键字
	* @param searchLike
	*/
	public void setSearchLike(String searchLike) {
		this.searchLike = searchLike;
	}
}
