package com.westar.base.pojo;

import java.util.List;

import com.westar.base.model.Department;
import com.westar.base.model.UserInfo;

/**
 * 
 * 描述:平台任务统计
 * @author zzq
 * @date 2018年5月22日 下午1:42:52
 */
public class StatisticFeeCrmVo {
	//客户主键
	private Integer depId;
	//客户名称
	private String depName;
	//客户类别主键
	private Integer userId; 
	//客户类别名称
	private String userName; 
	//至今毫秒级未更新数目
	private String itemFee1;
	//至今毫秒级未更新数目
	private String itemFee2;
	//至今毫秒级未更新数目
	private String itemFee3;
	//至今毫秒级未更新数目
	private String itemFee4;
	//至今毫秒级未更新数目
	private String itemFee5;
	//至今毫秒级未更新数目
	private String itemFee6;
	//至今毫秒级未更新数目
	private String itemFee7;
	//至今毫秒级未更新数目
	private String itemFee8;
	//至今毫秒级未更新数目
	private String itemFee9;
	//至今毫秒级未更新数目
	private String itemFee10;
	//至今毫秒级未更新数目
	private String itemFee11;
	//至今毫秒级未更新数目
	private String itemFee12;
	//月份ID
	private Integer monthOrder;
	//月份名称
	private String monthName;
	//月份名称
	private String yearMonth;
	//查询的年份
	private String year;
	//部门集合
	private List<Department> listDeps;
	//人员集合
	private List<UserInfo> listUsers;
	
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
	public Integer getMonthOrder() {
		return monthOrder;
	}
	public void setMonthOrder(Integer monthOrder) {
		this.monthOrder = monthOrder;
	}
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getItemFee1() {
		return itemFee1;
	}
	public void setItemFee1(String itemFee1) {
		this.itemFee1 = itemFee1;
	}
	public String getItemFee2() {
		return itemFee2;
	}
	public void setItemFee2(String itemFee2) {
		this.itemFee2 = itemFee2;
	}
	public String getItemFee3() {
		return itemFee3;
	}
	public void setItemFee3(String itemFee3) {
		this.itemFee3 = itemFee3;
	}
	public String getItemFee4() {
		return itemFee4;
	}
	public void setItemFee4(String itemFee4) {
		this.itemFee4 = itemFee4;
	}
	public String getItemFee5() {
		return itemFee5;
	}
	public void setItemFee5(String itemFee5) {
		this.itemFee5 = itemFee5;
	}
	public String getItemFee6() {
		return itemFee6;
	}
	public void setItemFee6(String itemFee6) {
		this.itemFee6 = itemFee6;
	}
	public String getItemFee7() {
		return itemFee7;
	}
	public void setItemFee7(String itemFee7) {
		this.itemFee7 = itemFee7;
	}
	public String getItemFee8() {
		return itemFee8;
	}
	public void setItemFee8(String itemFee8) {
		this.itemFee8 = itemFee8;
	}
	public String getItemFee9() {
		return itemFee9;
	}
	public void setItemFee9(String itemFee9) {
		this.itemFee9 = itemFee9;
	}
	public String getItemFee10() {
		return itemFee10;
	}
	public void setItemFee10(String itemFee10) {
		this.itemFee10 = itemFee10;
	}
	public String getItemFee11() {
		return itemFee11;
	}
	public void setItemFee11(String itemFee11) {
		this.itemFee11 = itemFee11;
	}
	public String getItemFee12() {
		return itemFee12;
	}
	public void setItemFee12(String itemFee12) {
		this.itemFee12 = itemFee12;
	}
	public List<Department> getListDeps() {
		return listDeps;
	}
	public void setListDeps(List<Department> listDeps) {
		this.listDeps = listDeps;
	}
	public List<UserInfo> getListUsers() {
		return listUsers;
	}
	public void setListUsers(List<UserInfo> listUsers) {
		this.listUsers = listUsers;
	}
}
