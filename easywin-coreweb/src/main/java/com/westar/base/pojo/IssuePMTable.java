package com.westar.base.pojo;

/**
 * 
 * 描述:问题过程管理分析
 * @author zzq
 * @date 2018年5月24日 下午8:47:11
 */
public class IssuePMTable {
	//问题总数
	private Integer totalNum;
	//关闭问题数量
	private Integer closeNum;
	//问题成功数量
	private Integer resolveDoneNum;
	//问题成功数量
	private Long resolveTimes;
	//解决完成问题的数量
	private Integer resolveNum;
	
	//月份ID
	private Integer monthOrder;
	//月份名称
	private String monthName;
	//月份名称
	private String yearMonth;
	
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getCloseNum() {
		return closeNum;
	}
	public void setCloseNum(Integer closeNum) {
		this.closeNum = closeNum;
	}
	public Integer getResolveDoneNum() {
		return resolveDoneNum;
	}
	public void setResolveDoneNum(Integer resolveDoneNum) {
		this.resolveDoneNum = resolveDoneNum;
	}
	public Long getResolveTimes() {
		return resolveTimes;
	}
	public void setResolveTimes(Long resolveTimes) {
		this.resolveTimes = resolveTimes;
	}
	public Integer getResolveNum() {
		return resolveNum;
	}
	public void setResolveNum(Integer resolveNum) {
		this.resolveNum = resolveNum;
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
	
	

}
