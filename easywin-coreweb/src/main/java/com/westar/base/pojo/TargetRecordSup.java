package com.westar.base.pojo;

public class TargetRecordSup {
	 
	 /*单位id*/
	 private Integer orgId;

	 /*单位名称*/
	 private String orgName;
	
	 /*单位分组名称*/
	 private String groupName;
	
	 /*单位指标数量*/
	 private int depTargetNum;
	 
	 /*共性指标数量*/
	 private int pubTargetNum;
	 
	 /*指标总分数*/
	 private int totalScore;
	 
	 /*审核中数量*/
	 private int veringNum;
	 
	 /*审核通过*/
	 private int passNum;
	 
	 /*审核不通过*/
	 private int noPassNum;

	 /*未认定数量*/
	 private int noConfirmNum;
     
	 /*认定通过数量*/
	 private int passConfirmNum;
	 
	 /*认定不通过数量*/
	 private int noPassConfirmNum;
	 
	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getDepTargetNum() {
		return depTargetNum;
	}

	public void setDepTargetNum(int depTargetNum) {
		this.depTargetNum = depTargetNum;
	}

	public int getPubTargetNum() {
		return pubTargetNum;
	}

	public void setPubTargetNum(int pubTargetNum) {
		this.pubTargetNum = pubTargetNum;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	
	public int getNoConfirmNum() {
		return noConfirmNum;
	}

	public void setNoConfirmNum(int noConfirmNum) {
		this.noConfirmNum = noConfirmNum;
	}

	

	public int getVeringNum() {
		return veringNum;
	}

	public void setVeringNum(int veringNum) {
		this.veringNum = veringNum;
	}

	public int getPassNum() {
		return passNum;
	}

	public void setPassNum(int passNum) {
		this.passNum = passNum;
	}

	public int getNoPassNum() {
		return noPassNum;
	}

	public void setNoPassNum(int noPassNum) {
		this.noPassNum = noPassNum;
	}

	public int getPassConfirmNum() {
		return passConfirmNum;
	}

	public void setPassConfirmNum(int passConfirmNum) {
		this.passConfirmNum = passConfirmNum;
	}

	public int getNoPassConfirmNum() {
		return noPassConfirmNum;
	}

	public void setNoPassConfirmNum(int noPassConfirmNum) {
		this.noPassConfirmNum = noPassConfirmNum;
	}

	
}
