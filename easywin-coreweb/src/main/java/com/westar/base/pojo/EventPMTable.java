package com.westar.base.pojo;

/**
 * 
 * 描述:事件过程管理分析
 * @author zzq
 * @date 2018年5月24日 下午6:53:26
 */
public class EventPMTable {
	//事件级别
	private String degreeLevel;
	//事件级别
	private String priorityDegree;
	//该级别的统计数
	private Integer degreeNum;
	//该级别的解决数
	private Integer resolveNum;
	//该级别的总的响应时间
	private Long totalResponeTime;
	//该级别的总的解决时间
	private Long totalResoveTime;
	public String getPriorityDegree() {
		return priorityDegree;
	}
	public void setPriorityDegree(String priorityDegree) {
		this.priorityDegree = priorityDegree;
	}
	public Integer getDegreeNum() {
		return degreeNum;
	}
	public void setDegreeNum(Integer degreeNum) {
		this.degreeNum = degreeNum;
	}
	public Integer getResolveNum() {
		return resolveNum;
	}
	public void setResolveNum(Integer resolveNum) {
		this.resolveNum = resolveNum;
	}
	public Long getTotalResponeTime() {
		return totalResponeTime;
	}
	public void setTotalResponeTime(Long totalResponeTime) {
		this.totalResponeTime = totalResponeTime;
	}
	public Long getTotalResoveTime() {
		return totalResoveTime;
	}
	public void setTotalResoveTime(Long totalResoveTime) {
		this.totalResoveTime = totalResoveTime;
	}
	public String getDegreeLevel() {
		return degreeLevel;
	}
	public void setDegreeLevel(String degreeLevel) {
		this.degreeLevel = degreeLevel;
	}
	

}
