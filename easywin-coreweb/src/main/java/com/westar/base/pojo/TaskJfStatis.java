package com.westar.base.pojo;
/**
 * 任务积分统计
 * @author xsl
 *
 */
public class TaskJfStatis {
	//部门名
	private String depName;
	//部门主键
	private Integer depId;
	//用户主键
	private Integer userId;
	//用户名
	private String userName;
	//任务总数
	private Integer taskTotal;
	//已评分任务数量
	private Integer jfTaskNum;
	//积分总数 
	private float scoreTotal;
	
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public Integer getDepId() {
		return depId;
	}
	public void setDepId(Integer depId) {
		this.depId = depId;
	}
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
	public Integer getTaskTotal() {
		return taskTotal;
	}
	public void setTaskTotal(Integer taskTotal) {
		this.taskTotal = taskTotal;
	}
	public Integer getJfTaskNum() {
		return jfTaskNum;
	}
	public void setJfTaskNum(Integer jfTaskNum) {
		this.jfTaskNum = jfTaskNum;
	}
	public float getScoreTotal() {
		return scoreTotal;
	}
	public void setScoreTotal(float scoreTotal) {
		this.scoreTotal = scoreTotal;
	}
}
