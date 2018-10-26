package com.westar.base.model;
/**
 * 积分
 * @author H87
 *
 */
public class Jifen{
	
	//积分
	private Integer jifenScore;
	
	//积分排名
	private Integer jifenOrder;
	
	//用户主键
	private String userId;
	//姓名
	private String userName;
	
	//部门
	private String depName;

	public Integer getJifenScore() {
		return jifenScore;
	}

	public void setJifenScore(Integer jifenScore) {
		this.jifenScore = jifenScore;
	}

	public Integer getJifenOrder() {
		return jifenOrder;
	}

	public void setJifenOrder(Integer jifenOrder) {
		this.jifenOrder = jifenOrder;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	

	
}