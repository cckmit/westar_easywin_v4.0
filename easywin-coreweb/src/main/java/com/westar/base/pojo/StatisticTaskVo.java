package com.westar.base.pojo;

import java.util.List;

import com.westar.base.model.Department;

/**
 * 
 * 描述:平台任务统计
 * @author zzq
 * @date 2018年5月22日 下午1:42:52
 */
public class StatisticTaskVo {
	//部门主键
	private Integer depId;
	//部门名称
	private String depName;
	//人员主键
	private Integer userId;
	//人员名称
	private String userName;
	//在办数
	private Integer doingTotalNum;
	//逾期在办
	private Integer doingYesterdayNum;
	//今日在办
	private Integer doingTodayNum;
	//明日在办
	private Integer doingTomarrowNum;
	//后天在办
	private Integer doingAfterNum;
	//将来在办
	private Integer doingFutureNum;
	//总任务
	private Integer totalNum;
	//个人积分
	private String totalJifen;
	//部门积分总数
	private String totalDepjifen;
	//部门人员数
	private String depUserNum;
	//年份
	private String year;
	
	//任务创建数
	private Integer createNum;
	//任务办结数
	private Integer finishNum;
	
	
	//任务类别
	private String grade;
	//任务类别名称
	private String gradeName;
	//逾期数
	private Integer overNums;
	
	
	//部门信息
	private List<Department> listDeps;
	
	public Integer getDepId() {
		return depId;
	}
	public void setDepId(Integer depId) {
		this.depId = depId;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
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
	public Integer getDoingTotalNum() {
		return doingTotalNum;
	}
	public void setDoingTotalNum(Integer doingTotalNum) {
		this.doingTotalNum = doingTotalNum;
	}
	public Integer getDoingYesterdayNum() {
		return doingYesterdayNum;
	}
	public void setDoingYesterdayNum(Integer doingYesterdayNum) {
		this.doingYesterdayNum = doingYesterdayNum;
	}
	public Integer getDoingTodayNum() {
		return doingTodayNum;
	}
	public void setDoingTodayNum(Integer doingTodayNum) {
		this.doingTodayNum = doingTodayNum;
	}
	public Integer getDoingTomarrowNum() {
		return doingTomarrowNum;
	}
	public void setDoingTomarrowNum(Integer doingTomarrowNum) {
		this.doingTomarrowNum = doingTomarrowNum;
	}
	public Integer getDoingAfterNum() {
		return doingAfterNum;
	}
	public void setDoingAfterNum(Integer doingAfterNum) {
		this.doingAfterNum = doingAfterNum;
	}
	public Integer getDoingFutureNum() {
		return doingFutureNum;
	}
	public void setDoingFutureNum(Integer doingFutureNum) {
		this.doingFutureNum = doingFutureNum;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public String getTotalJifen() {
		return totalJifen;
	}
	public void setTotalJifen(String totalJifen) {
		this.totalJifen = totalJifen;
	}
	public String getTotalDepjifen() {
		return totalDepjifen;
	}
	public void setTotalDepjifen(String totalDepjifen) {
		this.totalDepjifen = totalDepjifen;
	}
	public String getDepUserNum() {
		return depUserNum;
	}
	public void setDepUserNum(String depUserNum) {
		this.depUserNum = depUserNum;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public List<Department> getListDeps() {
		return listDeps;
	}
	public void setListDeps(List<Department> listDeps) {
		this.listDeps = listDeps;
	}
	public Integer getCreateNum() {
		return createNum;
	}
	public void setCreateNum(Integer createNum) {
		this.createNum = createNum;
	}
	public Integer getFinishNum() {
		return finishNum;
	}
	public void setFinishNum(Integer finishNum) {
		this.finishNum = finishNum;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public Integer getOverNums() {
		return overNums;
	}
	public void setOverNums(Integer overNums) {
		this.overNums = overNums;
	}
	
	
}
