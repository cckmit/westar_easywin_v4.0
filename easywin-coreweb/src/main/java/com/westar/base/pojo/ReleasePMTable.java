package com.westar.base.pojo;

/**
 * 
 * 描述:发布过程管理分析
 * @author zzq
 * @date 2018年5月25日 上午9:04:03
 */
public class ReleasePMTable {
	//发布总数
	private Integer totalNum;
	//发布成功的数量
	private Integer totalDoneNum;
	//按计划完成的发布数量
	private Integer scheduleRNum;
	//应执行的配置更行次数
	private Integer shouldNum;
	//按时执行的配置更新次数
	private Integer scheduleMNum;
	
	//季度名称
	private String quarterName;
	//季度排序
	private Integer quarterLevel;
	
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getTotalDoneNum() {
		return totalDoneNum;
	}
	public void setTotalDoneNum(Integer totalDoneNum) {
		this.totalDoneNum = totalDoneNum;
	}
	public Integer getScheduleRNum() {
		return scheduleRNum;
	}
	public void setScheduleRNum(Integer scheduleRNum) {
		this.scheduleRNum = scheduleRNum;
	}
	public Integer getShouldNum() {
		return shouldNum;
	}
	public void setShouldNum(Integer shouldNum) {
		this.shouldNum = shouldNum;
	}
	public Integer getScheduleMNum() {
		return scheduleMNum;
	}
	public void setScheduleMNum(Integer scheduleMNum) {
		this.scheduleMNum = scheduleMNum;
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
