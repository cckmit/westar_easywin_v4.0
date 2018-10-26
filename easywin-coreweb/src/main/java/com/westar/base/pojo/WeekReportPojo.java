package com.westar.base.pojo;

import java.util.List;

import com.westar.base.model.Department;
import com.westar.base.model.UserInfo;


/**
 *周报产看保留的查看条件
 */

public class WeekReportPojo {
	//汇报人的企业号
	private Integer comId;
	//汇报人的姓名
	private String userName;
	//汇报人的主键
	private Integer weekerId;
	//部门的主键
	private Integer depId;
	//部门的名称
	private String depName;
	//查询时间起
	private String startDate;
	//查询时间止
	private String endDate;
	//查询周报名称
	private String weekName;
	//汇报人类型 0自己 1下属 
	private String weekerType;
	//到底是写还是查看 0 写 1 查看
	private Integer viewType;
	//本周已汇报0 本周未汇报 1
	private String weekDoneState;
	//汇报人
	 private List<UserInfo> listOwner;
	 //本部门和下级部门
	 private Integer[] listTreeDeps;
	//查询的周数
	 private Integer weekNum ;
	//查询的年
	 private Integer weekYear;
	 
	//发布状态 0未发布 1 已发布 2延迟发布
	 private Integer submitState;
	 
	 private List<Department> listDep;
	 
	 
	public Integer getWeekerId() {
		return weekerId;
	}
	public void setWeekerId(Integer weekerId) {
		this.weekerId = weekerId;
	}
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getWeekName() {
		return weekName;
	}
	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}
	public Integer getViewType() {
		return viewType;
	}
	public void setViewType(Integer viewType) {
		this.viewType = viewType;
	}
	public Integer getComId() {
		return comId;
	}
	public void setComId(Integer comId) {
		this.comId = comId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getWeekerType() {
		return weekerType;
	}
	public void setWeekerType(String weekerType) {
		this.weekerType = weekerType;
	}
	public String getWeekDoneState() {
		return weekDoneState;
	}
	public void setWeekDoneState(String weekDoneState) {
		this.weekDoneState = weekDoneState;
	}
	public List<UserInfo> getListOwner() {
		return listOwner;
	}
	public void setListOwner(List<UserInfo> listOwner) {
		this.listOwner = listOwner;
	}
	public Integer getWeekYear() {
		return weekYear;
	}
	public void setWeekYear(Integer weekYear) {
		this.weekYear = weekYear;
	}
	public Integer getSubmitState() {
		return submitState;
	}
	public void setSubmitState(Integer submitState) {
		this.submitState = submitState;
	}
	public List<Department> getListDep() {
		return listDep;
	}
	public void setListDep(List<Department> listDep) {
		this.listDep = listDep;
	}
	public Integer getWeekNum() {
		return weekNum;
	}
	public void setWeekNum(Integer weekNum) {
		this.weekNum = weekNum;
	}
	public Integer[] getListTreeDeps() {
		return listTreeDeps;
	}
	public void setListTreeDeps(Integer[] listTreeDeps) {
		this.listTreeDeps = listTreeDeps;
	}

}
