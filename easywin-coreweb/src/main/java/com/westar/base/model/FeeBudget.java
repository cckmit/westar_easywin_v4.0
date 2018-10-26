package com.westar.base.model;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.util.DateTimeUtil;

/** 
 * 费用预算
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FeeBudget {
	/** 
	* id主键
	*/
	@Identity
	private Integer id;
	/** 
	* 记录创建时间
	*/
	@DefaultFiled
	private String recordCreateTime;
	/** 
	* 企业编号
	*/
	@Filed
	private Integer comId;
	/** 
	* 出差开始时间
	*/
	@Filed
	private String startTime;
	/** 
	* 出差结束时间
	*/
	@Filed
	private String endTime;
	/** 
	* 流程实例化主键
	*/
	@Filed
	private Integer instanceId;
	/** 
	* 出差人
	*/
	@Filed
	private Integer creator;
	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据
	*/
	@Filed
	private Integer status;
	/** 
	* 出差地点
	*/
	@Filed
	private String tripPlace;
	/** 
	* 审批批准借款金额
	*/
	@Filed
	private String allowedQuota;
	/** 
	* 是否出差申请；0-不是；1是；
	*/
	@Filed
	private Integer isBusinessTrip;
	/** 
	* 借款方式 1额度借款 2直接借款 3张总借款
	*/
	@Filed
	private String loanWay;
	/** 
	* 总借款
	*/
	@Filed
	private String loanFeeTotal;
	/** 
	* 总销账
	*/
	@Filed
	private String loanOffTotal;
	/** 
	* 总报销
	*/
	@Filed
	private String loanItemTotal;
	/** 
	* 销账状态
	*/
	@Filed
	private Integer loanOffState;
	/** 
	* 是否为初始化
	*/
	@Filed
	private Integer initStatus;

	/****************以上主要为系统表字段********************/
	/** 
	* 出差审批流程名称
	*/
	private String flowName;
	/** 
	* 创建人名称
	*/
	private String creatorName;
	/** 
	* 流程状态
	*/
	private Integer flowState;
	/** 
	* 当前审批人主键
	*/
	private Integer executor;
	/** 
	* 审批人姓名
	*/
	private String executorName;
	/** 
	* 当前审批人性别
	*/
	private Integer executorGender;
	private Integer busId;
	private String busType;
	private String tripBusName;
	/** 
	* 关联模块集合
	*/
	private List<FeeBusMod> listRelateMods;
	/** 
	* 审批人uuid
	*/
	private String executorUuid;
	/** 
	* 审批人fileName
	*/
	private String executorFileName;
	/** 
	* 当前审批
	*/
	private List<UserInfo> listExecutor;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 列表排序规则
	*/
	private String orderBy;
	private Integer spState;
	/** 
	* 针对出差的借款记录
	*/
	private List<FeeLoan> listFeeLoan;
	/** 
	* 出差成果报告和一般性说明
	*/
	private List<FeeLoanReport> listLoanReport;
	/** 
	* 出差成果报告和一般性说明
	*/
	private List<FeeLoanOff> listLoanOff;
	/** 
	* 发起人集合
	*/
	private List<UserInfo> listCreator;
	/** 
	* 预计花的时间
	*/
	private Integer budgetCostDay;
	/** 
	* 总借款
	*/
	private Integer totalBorrow;
	/** 
	* 总报销
	*/
	private Integer totalOff;
	/** 
	* 报销业务
	*/
	private FeeLoanOff feeLoanOff;
	/** 
	* 借款业务
	*/
	private FeeLoan feeLoan;
	/** 
	* 次数
	*/
	private Integer times;
	/** 
	* 未销账金额
	*/
	private Integer unLoanOffTotal;
	/** 
	* 标记是否可以提交报销单的报告id
	*/
	private Integer canLoanOffRepId;
	/** 
	* 是否可以不报销直接销账
	*/
	private Integer canDirectBalance;
	/** 
	* 直接销账状态1是0否
	*/
	private Integer directBalanceState;
	/** 
	* 直接销账人
	*/
	private String directBalanceUserName;
	/** 
	* 直接销账时间
	*/
	private String directBalanceTime;
	/** 
	* 销账说明
	*/
	private String content;

	/****************以上为自己添加字段********************/
	/** 
	* id主键
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/** 
	* id主键
	* @return
	*/
	public Integer getId() {
		return id;
	}

	/** 
	* 记录创建时间
	* @param recordCreateTime
	*/
	public void setRecordCreateTime(String recordCreateTime) {
		this.recordCreateTime = recordCreateTime;
	}

	/** 
	* 记录创建时间
	* @return
	*/
	public String getRecordCreateTime() {
		return recordCreateTime;
	}

	/** 
	* 企业编号
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 企业编号
	* @return
	*/
	public Integer getComId() {
		return comId;
	}

	/** 
	* 出差开始时间
	* @param startTime
	*/
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/** 
	* 出差开始时间
	* @return
	*/
	public String getStartTime() {
		return startTime;
	}

	/** 
	* 出差结束时间
	* @param endTime
	*/
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/** 
	* 出差结束时间
	* @return
	*/
	public String getEndTime() {
		return endTime;
	}

	/** 
	* 流程实例化主键
	* @param instanceId
	*/
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	/** 
	* 流程实例化主键
	* @return
	*/
	public Integer getInstanceId() {
		return instanceId;
	}

	/** 
	* 出差人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 出差人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 出差审批流程名称
	* @return
	*/
	public String getFlowName() {
		return flowName;
	}

	/** 
	* 出差审批流程名称
	* @param flowName
	*/
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/** 
	* 创建人名称
	* @return
	*/
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 创建人名称
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 流程状态
	* @return
	*/
	public Integer getFlowState() {
		return flowState;
	}

	/** 
	* 流程状态
	* @param flowState
	*/
	public void setFlowState(Integer flowState) {
		this.flowState = flowState;
	}

	/** 
	* 当前审批人主键
	* @return
	*/
	public Integer getExecutor() {
		return executor;
	}

	/** 
	* 当前审批人主键
	* @param executor
	*/
	public void setExecutor(Integer executor) {
		this.executor = executor;
	}

	/** 
	* 当前审批
	* @return
	*/
	public List<UserInfo> getListExecutor() {
		return listExecutor;
	}

	/** 
	* 当前审批
	* @param listExecutor
	*/
	public void setListExecutor(List<UserInfo> listExecutor) {
		this.listExecutor = listExecutor;
	}

	/** 
	* 查询的时间起
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询的时间起
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询的时间止
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的时间止
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 列表排序规则
	* @return
	*/
	public String getOrderBy() {
		return orderBy;
	}

	/** 
	* 列表排序规则
	* @param orderBy
	*/
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getSpState() {
		return spState;
	}

	public void setSpState(Integer spState) {
		this.spState = spState;
	}

	/** 
	* 审批人姓名
	* @return
	*/
	public String getExecutorName() {
		return executorName;
	}

	/** 
	* 审批人姓名
	* @param executorName
	*/
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	/** 
	* 当前审批人性别
	* @return
	*/
	public Integer getExecutorGender() {
		return executorGender;
	}

	/** 
	* 当前审批人性别
	* @param executorGender
	*/
	public void setExecutorGender(Integer executorGender) {
		this.executorGender = executorGender;
	}

	/** 
	* 审批人uuid
	* @return
	*/
	public String getExecutorUuid() {
		return executorUuid;
	}

	/** 
	* 审批人uuid
	* @param executorUuid
	*/
	public void setExecutorUuid(String executorUuid) {
		this.executorUuid = executorUuid;
	}

	/** 
	* 审批人fileName
	* @return
	*/
	public String getExecutorFileName() {
		return executorFileName;
	}

	/** 
	* 审批人fileName
	* @param executorFileName
	*/
	public void setExecutorFileName(String executorFileName) {
		this.executorFileName = executorFileName;
	}

	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据
	* @param status
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据
	* @return
	*/
	public Integer getStatus() {
		return status;
	}

	/** 
	* 发起人集合
	* @return
	*/
	public List<UserInfo> getListCreator() {
		return listCreator;
	}

	/** 
	* 发起人集合
	* @param listCreator
	*/
	public void setListCreator(List<UserInfo> listCreator) {
		this.listCreator = listCreator;
	}

	/** 
	* 审批批准借款金额
	* @param allowedQuota
	*/
	public void setAllowedQuota(String allowedQuota) {
		this.allowedQuota = allowedQuota;
	}

	/** 
	* 审批批准借款金额
	* @return
	*/
	public String getAllowedQuota() {
		return allowedQuota;
	}

	/** 
	* 是否出差申请；0-不是；1是；
	* @param isBusinessTrip
	*/
	public void setIsBusinessTrip(Integer isBusinessTrip) {
		this.isBusinessTrip = isBusinessTrip;
	}

	/** 
	* 是否出差申请；0-不是；1是；
	* @return
	*/
	public Integer getIsBusinessTrip() {
		return isBusinessTrip;
	}

	/** 
	* 出差成果报告和一般性说明
	* @return
	*/
	public List<FeeLoanReport> getListLoanReport() {
		return listLoanReport;
	}

	/** 
	* 出差成果报告和一般性说明
	* @param listLoanReport
	*/
	public void setListLoanReport(List<FeeLoanReport> listLoanReport) {
		this.listLoanReport = listLoanReport;
	}

	/** 
	* 预计花的时间
	* @return
	*/
	public Integer getBudgetCostDay() {
		if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
			List<String> aa = DateTimeUtil.getTimeZones(startTime, endTime);
			budgetCostDay = aa.size();
		}
		return budgetCostDay;
	}

	/** 
	* 预计花的时间
	* @param budgetCostDay
	*/
	public void setBudgetCostDay(Integer budgetCostDay) {
		this.budgetCostDay = budgetCostDay;
	}

	/** 
	* 出差成果报告和一般性说明
	* @return
	*/
	public List<FeeLoanOff> getListLoanOff() {
		return listLoanOff;
	}

	/** 
	* 出差成果报告和一般性说明
	* @param listLoanOff
	*/
	public void setListLoanOff(List<FeeLoanOff> listLoanOff) {
		this.listLoanOff = listLoanOff;
	}

	/** 
	* 出差地点
	* @param tripPlace
	*/
	public void setTripPlace(String tripPlace) {
		this.tripPlace = tripPlace;
	}

	/** 
	* 出差地点
	* @return
	*/
	public String getTripPlace() {
		return tripPlace;
	}

	/** 
	* 总借款
	* @return
	*/
	public Integer getTotalBorrow() {
		return totalBorrow;
	}

	/** 
	* 总借款
	* @param totalBorrow
	*/
	public void setTotalBorrow(Integer totalBorrow) {
		this.totalBorrow = totalBorrow;
	}

	/** 
	* 总报销
	* @return
	*/
	public Integer getTotalOff() {
		return totalOff;
	}

	/** 
	* 总报销
	* @param totalOff
	*/
	public void setTotalOff(Integer totalOff) {
		this.totalOff = totalOff;
	}

	/** 
	* 关联模块集合
	* @return
	*/
	public List<FeeBusMod> getListRelateMods() {
		return listRelateMods;
	}

	/** 
	* 关联模块集合
	* @param listRelateMods
	*/
	public void setListRelateMods(List<FeeBusMod> listRelateMods) {
		this.listRelateMods = listRelateMods;
	}

	public Integer getBusId() {
		return busId;
	}

	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getTripBusName() {
		return tripBusName;
	}

	public void setTripBusName(String tripBusName) {
		this.tripBusName = tripBusName;
	}

	/** 
	* 借款方式 1额度借款 2直接借款 3张总借款
	* @param loanWay
	*/
	public void setLoanWay(String loanWay) {
		this.loanWay = loanWay;
	}

	/** 
	* 借款方式 1额度借款 2直接借款 3张总借款
	* @return
	*/
	public String getLoanWay() {
		return loanWay;
	}

	/** 
	* 总借款
	* @param loanFeeTotal
	*/
	public void setLoanFeeTotal(String loanFeeTotal) {
		this.loanFeeTotal = loanFeeTotal;
	}

	/** 
	* 总借款
	* @return
	*/
	public String getLoanFeeTotal() {
		return loanFeeTotal;
	}

	/** 
	* 总销账
	* @param loanOffTotal
	*/
	public void setLoanOffTotal(String loanOffTotal) {
		this.loanOffTotal = loanOffTotal;
	}

	/** 
	* 总销账
	* @return
	*/
	public String getLoanOffTotal() {
		return loanOffTotal;
	}

	/** 
	* 总报销
	* @param loanItemTotal
	*/
	public void setLoanItemTotal(String loanItemTotal) {
		this.loanItemTotal = loanItemTotal;
	}

	/** 
	* 总报销
	* @return
	*/
	public String getLoanItemTotal() {
		return loanItemTotal;
	}

	/** 
	* 销账状态
	* @param loanOffState
	*/
	public void setLoanOffState(Integer loanOffState) {
		this.loanOffState = loanOffState;
	}

	/** 
	* 销账状态
	* @return
	*/
	public Integer getLoanOffState() {
		return loanOffState;
	}

	/** 
	* 是否为初始化
	* @param initStatus
	*/
	public void setInitStatus(Integer initStatus) {
		this.initStatus = initStatus;
	}

	/** 
	* 是否为初始化
	* @return
	*/
	public Integer getInitStatus() {
		return initStatus;
	}

	/** 
	* 针对出差的借款记录
	* @return
	*/
	public List<FeeLoan> getListFeeLoan() {
		return listFeeLoan;
	}

	/** 
	* 针对出差的借款记录
	* @param listFeeLoan
	*/
	public void setListFeeLoan(List<FeeLoan> listFeeLoan) {
		this.listFeeLoan = listFeeLoan;
	}

	/** 
	* 报销业务
	* @return
	*/
	public FeeLoanOff getFeeLoanOff() {
		return feeLoanOff;
	}

	/** 
	* 报销业务
	* @param feeLoanOff
	*/
	public void setFeeLoanOff(FeeLoanOff feeLoanOff) {
		this.feeLoanOff = feeLoanOff;
	}

	/** 
	* 借款业务
	* @return
	*/
	public FeeLoan getFeeLoan() {
		return feeLoan;
	}

	/** 
	* 借款业务
	* @param feeLoan
	*/
	public void setFeeLoan(FeeLoan feeLoan) {
		this.feeLoan = feeLoan;
	}

	/** 
	* 次数
	* @return
	*/
	public Integer getTimes() {
		return times;
	}

	/** 
	* 次数
	* @param times
	*/
	public void setTimes(Integer times) {
		this.times = times;
	}

	/** 
	* 未销账金额
	* @return
	*/
	public Integer getUnLoanOffTotal() {
		return unLoanOffTotal;
	}

	/** 
	* 未销账金额
	* @param unLoanOffTotal
	*/
	public void setUnLoanOffTotal(Integer unLoanOffTotal) {
		this.unLoanOffTotal = unLoanOffTotal;
	}

	/** 
	* 标记是否可以提交报销单的报告id
	* @return
	*/
	public Integer getCanLoanOffRepId() {
		return canLoanOffRepId;
	}

	/** 
	* 标记是否可以提交报销单的报告id
	* @param canLoanOffRepId
	*/
	public void setCanLoanOffRepId(Integer canLoanOffRepId) {
		this.canLoanOffRepId = canLoanOffRepId;
	}

	/** 
	* 是否可以不报销直接销账
	* @return
	*/
	public Integer getCanDirectBalance() {
		return canDirectBalance;
	}

	/** 
	* 是否可以不报销直接销账
	* @param canDirectBalance
	*/
	public void setCanDirectBalance(Integer canDirectBalance) {
		this.canDirectBalance = canDirectBalance;
	}

	/** 
	* 直接销账状态1是0否
	* @return
	*/
	public Integer getDirectBalanceState() {
		return directBalanceState;
	}

	/** 
	* 直接销账状态1是0否
	* @param directBalanceState
	*/
	public void setDirectBalanceState(Integer directBalanceState) {
		this.directBalanceState = directBalanceState;
	}

	/** 
	* 直接销账人
	* @return
	*/
	public String getDirectBalanceUserName() {
		return directBalanceUserName;
	}

	/** 
	* 直接销账人
	* @param directBalanceUserName
	*/
	public void setDirectBalanceUserName(String directBalanceUserName) {
		this.directBalanceUserName = directBalanceUserName;
	}

	/** 
	* 直接销账时间
	* @return
	*/
	public String getDirectBalanceTime() {
		return directBalanceTime;
	}

	/** 
	* 直接销账时间
	* @param directBalanceTime
	*/
	public void setDirectBalanceTime(String directBalanceTime) {
		this.directBalanceTime = directBalanceTime;
	}

	/** 
	* 销账说明
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 销账说明
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}
}
