package com.westar.base.pojo;

/**
 * 
 * 描述:问题过程管理分析
 * @author zzq
 * @date 2018年5月24日 下午8:47:11
 */
public class ModifyPMTable {
	//已执行的变更总数
	private Integer totalDoneNum;
	//失败的变更数量
	private Integer failNum;
	//变更请求总数
	private Integer totalNum;
	//取消的变更数量
	private Integer cancelNum;
	//非法变更的数量
	private Integer illegalNum;
	//季度名称
	private String quarterName;
	//季度排序
	private Integer quarterLevel;
	
	public Integer getTotalDoneNum() {
		return totalDoneNum;
	}
	public void setTotalDoneNum(Integer totalDoneNum) {
		this.totalDoneNum = totalDoneNum;
	}
	public Integer getFailNum() {
		return failNum;
	}
	public void setFailNum(Integer failNum) {
		this.failNum = failNum;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getCancelNum() {
		return cancelNum;
	}
	public void setCancelNum(Integer cancelNum) {
		this.cancelNum = cancelNum;
	}
	public Integer getIllegalNum() {
		return illegalNum;
	}
	public void setIllegalNum(Integer illegalNum) {
		this.illegalNum = illegalNum;
	}
	public String getQuarterName() {
		return quarterName;
	}
	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}
	public Integer getQuarterLevel() {
		return quarterLevel;
	}
	public void setQuarterLevel(Integer quarterLevel) {
		this.quarterLevel = quarterLevel;
	}
	
}
