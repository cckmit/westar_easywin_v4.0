package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 考勤状态记录
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AttenceState {
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
	* 修改人
	*/
	@Filed
	private Integer updater;
	/** 
	* 考勤状态 
	*/
	@Filed
	private String state;
	/** 
	* 员工主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 考勤时间
	*/
	@Filed
	private String attenceDate;
	/** 
	* 修改备注
	*/
	@Filed
	private String remark;
	/** 
	* 企业编号
	*/
	@Filed
	private Integer comId;
	/** 
	* 签到时间
	*/
	@Filed
	private String signInTime;
	/** 
	* 签退时间
	*/
	@Filed
	private String signOutTime;

	/****************以上主要为系统表字段********************/
	/** 
	* 考勤人姓名
	*/
	private String userName;
	/** 
	* 修改人姓名
	*/
	private String updaterName;
	/** 
	* 考勤状态字符串
	*/
	private String stateString;
	/** 
	* 请假类型	  			301:换休	  			302:事假	  			303:病假	  			304:婚假	  			305:丧假	  			306:年休假	  			307:陪护假	  			308:产假
	*/
	private Integer leaveType;
	/** 
	* 请假类型字符串
	*/
	private String leaveString;

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
	* 修改人
	* @param updater
	*/
	public void setUpdater(Integer updater) {
		this.updater = updater;
	}

	/** 
	* 修改人
	* @return
	*/
	public Integer getUpdater() {
		return updater;
	}

	/** 
	* 考勤状态 
	* @param state
	*/
	public void setState(String state) {
		this.state = state;
	}

	/** 
	* 考勤状态 
	* @return
	*/
	public String getState() {
		return state;
	}

	/** 
	* 员工主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 员工主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 考勤时间
	* @param attenceDate
	*/
	public void setAttenceDate(String attenceDate) {
		this.attenceDate = attenceDate;
	}

	/** 
	* 考勤时间
	* @return
	*/
	public String getAttenceDate() {
		return attenceDate;
	}

	/** 
	* 修改备注
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	* 修改备注
	* @return
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 考勤人姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 考勤人姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 修改人姓名
	* @return
	*/
	public String getUpdaterName() {
		return updaterName;
	}

	/** 
	* 修改人姓名
	* @param updaterName
	*/
	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	/** 
	* 考勤状态字符串
	* @return
	*/
	public String getStateString() {
		return stateString;
	}

	/** 
	* 考勤状态字符串
	* @param stateString
	*/
	public void setStateString(String stateString) {
		this.stateString = stateString;
	}

	/** 
	* 请假类型	  			301:换休	  			302:事假	  			303:病假	  			304:婚假	  			305:丧假	  			306:年休假	  			307:陪护假	  			308:产假
	* @return
	*/
	public Integer getLeaveType() {
		return leaveType;
	}

	/** 
	* 请假类型	  			301:换休	  			302:事假	  			303:病假	  			304:婚假	  			305:丧假	  			306:年休假	  			307:陪护假	  			308:产假
	* @param leaveType
	*/
	public void setLeaveType(Integer leaveType) {
		this.leaveType = leaveType;
	}

	/** 
	* 请假类型字符串
	* @return
	*/
	public String getLeaveString() {
		return leaveString;
	}

	/** 
	* 请假类型字符串
	* @param leaveString
	*/
	public void setLeaveString(String leaveString) {
		this.leaveString = leaveString;
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
	* 签到时间
	* @return
	*/
	public String getSignInTime() {
		return signInTime;
	}

	/** 
	* 签到时间
	* @param signInTime
	*/
	public void setSignInTime(String signInTime) {
		this.signInTime = signInTime;
	}

	/** 
	* 签退时间
	* @return
	*/
	public String getSignOutTime() {
		return signOutTime;
	}

	/** 
	* 签退时间
	* @param signOutTime
	*/
	public void setSignOutTime(String signOutTime) {
		this.signOutTime = signOutTime;
	}
}
