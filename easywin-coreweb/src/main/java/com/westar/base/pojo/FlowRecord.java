package com.westar.base.pojo;

/**
 * 对象移交记录
 * @author zzq
 *
 */
public class FlowRecord {
	
	//接收人主键
	private Integer userId;
	
	//接收人姓名
	private String userName;
	
	//接收人性别
	private String gender;
	
	//接收人头像
	private String uuid;
	
	//接收人头像名
	private String filename;
	
	//接收时间
	private String acceptDate;
	
	//对象状态 1已经结束 0 未结束
	private String state;
	
	//结束时间
	private String endTime;
	
	//超出时限
	private Long overTime;
	
	//耗时
	private Long useTime;
	
	//时限
	private String handTimeLimit;
		
	//移交人主键
	private Integer fromUser;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(String acceptDate) {
		this.acceptDate = acceptDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getFromUser() {
		return fromUser;
	}

	public void setFromUser(Integer fromUser) {
		this.fromUser = fromUser;
	}

	public Long getOverTime() {
		return overTime;
	}

	public void setOverTime(Long overTime) {
		this.overTime = overTime;
	}

	public String getHandTimeLimit() {
		return handTimeLimit;
	}

	public void setHandTimeLimit(String handTimeLimit) {
		this.handTimeLimit = handTimeLimit;
	}

	public Long getUseTime() {
		return useTime;
	}

	public void setUseTime(Long useTime) {
		this.useTime = useTime;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
