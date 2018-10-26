package com.westar.base.pojo;

public class TargetReportSup {
	 
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

	 /*超期未汇报数量*/
	 private int redNum;
	 
	 /*预警未汇报数量*/
	 private int yellowNum;
	 
	 /*正常未汇报*/
	 private int greenNum;
	 
	 /*正在汇报（汇报审核中和审核未通过）*/
	 private int doingNum;
	 
	 /*已汇报数量*/
	 private int doneNum;
	 
	 /*结束汇报数量*/
	 private int endNum;
	 
	 /*目标值完成整体进度*/
	 private double rateProgress;

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

	public int getRedNum() {
		return redNum;
	}

	public void setRedNum(int redNum) {
		this.redNum = redNum;
	}

	public int getYellowNum() {
		return yellowNum;
	}

	public void setYellowNum(int yellowNum) {
		this.yellowNum = yellowNum;
	}

	public int getGreenNum() {
		return greenNum;
	}

	public void setGreenNum(int greenNum) {
		this.greenNum = greenNum;
	}

	public int getDoneNum() {
		return doneNum;
	}

	public void setDoneNum(int doneNum) {
		this.doneNum = doneNum;
	}

	public double getRateProgress() {
		return rateProgress;
	}

	public void setRateProgress(double rateProgress) {
		this.rateProgress = rateProgress;
	}

	public int getDoingNum() {
		return doingNum;
	}

	public void setDoingNum(int doingNum) {
		this.doingNum = doingNum;
	}

	public int getEndNum() {
		return endNum;
	}

	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}
}
