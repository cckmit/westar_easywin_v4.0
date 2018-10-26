package com.westar.base.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;

/** 
 * 借款记录表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FeeLoan {
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
	* 核实借款金额
	*/
	@Filed
	private String borrowingBalance;
	/** 
	* 流程实例化主键
	*/
	@Filed
	private Integer instanceId;
	/** 
	* 借款人
	*/
	@Filed
	private Integer creator;
	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据
	*/
	@Filed
	private Integer status;
	/** 
	* 申请记录主键
	*/
	@Filed
	private Integer feeBudgetId;
	/** 
	* 剩余额度
	*/
	@Filed
	private String allowedQuota;
	/** 
	* 申请借款中金额
	*/
	@Filed
	private String loanMoney;
	/** 
	* 是否出差申请；0-不是；1是；
	*/
	@Filed
	private Integer isBusinessTrip;
	/** 
	* 是否发送过领款通知 1是；
	*/
	@Filed
	private Integer sendNotice;
	/** 
	* 是否完成借款 1是；
	*/
	@Filed
	private Integer balanceState;
	/** 
	* 支付方式0现金1银联转账
	*/
	@Filed
	private String payType;
	/** 
	* 完成借款时间
	*/
	@Filed
	private String balanceTime;
	/** 
	* 借款出款人
	*/
	@Filed
	private Integer balanceUserId;
	/** 
	* 是否为初始化
	*/
	@Filed
	private Integer initStatus;

	/****************以上主要为系统表字段********************/
	/** 
	* 是否能够继续借款 0 不能 1可以
	*/
	private Integer reLoanState;
	/** 
	* 借款审批流程名称
	*/
	private String flowName;
	/** 
	* 借款流程状态
	*/
	private Integer flowState;
	/** 
	* 借款审批状态
	*/
	private Integer spState;
	/** 
	* 创建人名称
	*/
	private String creatorName;
	/** 
	* 当前审批人主键
	*/
	private Integer executor;
	/** 
	* 审批人姓名
	*/
	private String executorName;
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
	/** 
	* 发起人集合
	*/
	private List<UserInfo> listCreator;
	/** 
	* 出差申请状态
	*/
	private Integer loanApplyState;
	/** 
	* 出差申请流程状态
	*/
	private Integer loanApplyFlowState;
	/** 
	* 出差申请审批状态
	*/
	private Integer loanApplySpState;
	/** 
	* 出差申请流程主键
	*/
	private Integer loanApplyInsId;
	/** 
	* 是否为初始化数据
	*/
	private Integer apyInitStatus;
	/** 
	* 出差申请流程名称
	*/
	private String loanApplyInsName;
	/** 
	* 出差申请开始时间
	*/
	private String loanApplyStartDate;
	/** 
	* 出差申请结束时间
	*/
	private String loanApplyEndDate;
	/** 
	* 进度
	*/
	private String progress;
	/** 
	* 单号
	*/
	private String flowSerialNumber;
	/** 
	* 借款方式
	*/
	private String loanWay;
	/** 
	* 申请借款中次数
	*/
	private Integer feeLoanDoingTimes;
	/** 
	* 申请预借款金额
	*/
	private Integer loanmoneyDoingTotal;
	/** 
	* 成功借款次数
	*/
	private Integer feeLoanDoneTimes;
	/** 
	* 成功借款金额
	*/
	private Integer borrowingBalanceDoneTotal;
	/** 
	* 结算人姓名
	*/
	private String balanceUserName;
	/** 
	* 是否可以不报销直接销账
	*/
	private Integer canDirectBalance;
	/** 
	* 直接销账状态1是0否
	*/
	private Integer directBalanceState;

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
	* 核实借款金额
	* @param borrowingBalance
	*/
	public void setBorrowingBalance(String borrowingBalance) {
		this.borrowingBalance = borrowingBalance;
	}

	/** 
	* 核实借款金额
	* @return
	*/
	public String getBorrowingBalance() {
		return borrowingBalance;
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
	* 借款人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 借款人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 借款审批流程名称
	* @return
	*/
	public String getFlowName() {
		return flowName;
	}

	/** 
	* 借款审批流程名称
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
	* 借款流程状态
	* @return
	*/
	public Integer getFlowState() {
		return flowState;
	}

	/** 
	* 借款流程状态
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

	/** 
	* 借款审批状态
	* @return
	*/
	public Integer getSpState() {
		return spState;
	}

	/** 
	* 借款审批状态
	* @param spState
	*/
	public void setSpState(Integer spState) {
		this.spState = spState;
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
	* 申请记录主键
	* @param feeBudgetId
	*/
	public void setFeeBudgetId(Integer feeBudgetId) {
		this.feeBudgetId = feeBudgetId;
	}

	/** 
	* 申请记录主键
	* @return
	*/
	public Integer getFeeBudgetId() {
		return feeBudgetId;
	}

	/** 
	* 出差申请流程主键
	* @return
	*/
	public Integer getLoanApplyInsId() {
		return loanApplyInsId;
	}

	/** 
	* 出差申请流程主键
	* @param loanApplyInsId
	*/
	public void setLoanApplyInsId(Integer loanApplyInsId) {
		this.loanApplyInsId = loanApplyInsId;
	}

	/** 
	* 出差申请流程名称
	* @return
	*/
	public String getLoanApplyInsName() {
		return loanApplyInsName;
	}

	/** 
	* 出差申请流程名称
	* @param loanApplyInsName
	*/
	public void setLoanApplyInsName(String loanApplyInsName) {
		this.loanApplyInsName = loanApplyInsName;
	}

	/** 
	* 出差申请开始时间
	* @return
	*/
	public String getLoanApplyStartDate() {
		return loanApplyStartDate;
	}

	/** 
	* 出差申请开始时间
	* @param loanApplyStartDate
	*/
	public void setLoanApplyStartDate(String loanApplyStartDate) {
		this.loanApplyStartDate = loanApplyStartDate;
	}

	/** 
	* 出差申请结束时间
	* @return
	*/
	public String getLoanApplyEndDate() {
		return loanApplyEndDate;
	}

	/** 
	* 出差申请结束时间
	* @param loanApplyEndDate
	*/
	public void setLoanApplyEndDate(String loanApplyEndDate) {
		this.loanApplyEndDate = loanApplyEndDate;
	}

	/** 
	* 剩余额度
	* @param allowedQuota
	*/
	public void setAllowedQuota(String allowedQuota) {
		this.allowedQuota = allowedQuota;
	}

	/** 
	* 剩余额度
	* @return
	*/
	public String getAllowedQuota() {
		return allowedQuota;
	}

	/** 
	* 申请借款中金额
	* @param loanMoney
	*/
	public void setLoanMoney(String loanMoney) {
		this.loanMoney = loanMoney;
	}

	/** 
	* 申请借款中金额
	* @return
	*/
	public String getLoanMoney() {
		return loanMoney;
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
	* 出差申请流程状态
	* @return
	*/
	public Integer getLoanApplyFlowState() {
		return loanApplyFlowState;
	}

	/** 
	* 出差申请流程状态
	* @param loanApplyFlowState
	*/
	public void setLoanApplyFlowState(Integer loanApplyFlowState) {
		this.loanApplyFlowState = loanApplyFlowState;
	}

	/** 
	* 出差申请审批状态
	* @return
	*/
	public Integer getLoanApplySpState() {
		return loanApplySpState;
	}

	/** 
	* 出差申请审批状态
	* @param loanApplySpState
	*/
	public void setLoanApplySpState(Integer loanApplySpState) {
		this.loanApplySpState = loanApplySpState;
	}

	/** 
	* 出差申请状态
	* @return
	*/
	public Integer getLoanApplyState() {
		return loanApplyState;
	}

	/** 
	* 出差申请状态
	* @param loanApplyState
	*/
	public void setLoanApplyState(Integer loanApplyState) {
		this.loanApplyState = loanApplyState;
	}

	/** 
	* 是否发送过领款通知 1是；
	* @param sendNotice
	*/
	public void setSendNotice(Integer sendNotice) {
		this.sendNotice = sendNotice;
	}

	/** 
	* 是否发送过领款通知 1是；
	* @return
	*/
	public Integer getSendNotice() {
		return sendNotice;
	}

	/** 
	* 进度
	* @return
	*/
	public String getProgress() {
		return progress;
	}

	/** 
	* 进度
	* @param progress
	*/
	public void setProgress(String progress) {
		this.progress = progress;
	}

	/** 
	* 是否完成借款 1是；
	* @param balanceState
	*/
	public void setBalanceState(Integer balanceState) {
		this.balanceState = balanceState;
	}

	/** 
	* 是否完成借款 1是；
	* @return
	*/
	public Integer getBalanceState() {
		return balanceState;
	}

	/** 
	* 支付方式0现金1银联转账
	* @param payType
	*/
	public void setPayType(String payType) {
		this.payType = payType;
	}

	/** 
	* 支付方式0现金1银联转账
	* @return
	*/
	public String getPayType() {
		return payType;
	}

	/** 
	* 完成借款时间
	* @param balanceTime
	*/
	public void setBalanceTime(String balanceTime) {
		this.balanceTime = balanceTime;
	}

	/** 
	* 完成借款时间
	* @return
	*/
	public String getBalanceTime() {
		return balanceTime;
	}

	/** 
	* 借款出款人
	* @param balanceUserId
	*/
	public void setBalanceUserId(Integer balanceUserId) {
		this.balanceUserId = balanceUserId;
	}

	/** 
	* 借款出款人
	* @return
	*/
	public Integer getBalanceUserId() {
		return balanceUserId;
	}

	/** 
	* 单号
	* @return
	*/
	public String getFlowSerialNumber() {
		return flowSerialNumber;
	}

	/** 
	* 单号
	* @param flowSerialNumber
	*/
	public void setFlowSerialNumber(String flowSerialNumber) {
		this.flowSerialNumber = flowSerialNumber;
	}

	/** 
	* 借款方式
	* @return
	*/
	public String getLoanWay() {
		return loanWay;
	}

	/** 
	* 借款方式
	* @param loanWay
	*/
	public void setLoanWay(String loanWay) {
		this.loanWay = loanWay;
	}

	/** 
	* 申请借款中次数
	* @return
	*/
	public Integer getFeeLoanDoingTimes() {
		return feeLoanDoingTimes;
	}

	/** 
	* 申请借款中次数
	* @param feeLoanDoingTimes
	*/
	public void setFeeLoanDoingTimes(Integer feeLoanDoingTimes) {
		this.feeLoanDoingTimes = feeLoanDoingTimes;
	}

	/** 
	* 申请预借款金额
	* @return
	*/
	public Integer getLoanmoneyDoingTotal() {
		return loanmoneyDoingTotal;
	}

	/** 
	* 申请预借款金额
	* @param loanmoneyDoingTotal
	*/
	public void setLoanmoneyDoingTotal(Integer loanmoneyDoingTotal) {
		this.loanmoneyDoingTotal = loanmoneyDoingTotal;
	}

	/** 
	* 成功借款次数
	* @return
	*/
	public Integer getFeeLoanDoneTimes() {
		return feeLoanDoneTimes;
	}

	/** 
	* 成功借款次数
	* @param feeLoanDoneTimes
	*/
	public void setFeeLoanDoneTimes(Integer feeLoanDoneTimes) {
		this.feeLoanDoneTimes = feeLoanDoneTimes;
	}

	/** 
	* 成功借款金额
	* @return
	*/
	public Integer getBorrowingBalanceDoneTotal() {
		return borrowingBalanceDoneTotal;
	}

	/** 
	* 成功借款金额
	* @param borrowingBalanceDoneTotal
	*/
	public void setBorrowingBalanceDoneTotal(Integer borrowingBalanceDoneTotal) {
		this.borrowingBalanceDoneTotal = borrowingBalanceDoneTotal;
	}

	/** 
	* 是否为初始化数据
	* @return
	*/
	public Integer getApyInitStatus() {
		return apyInitStatus;
	}

	/** 
	* 是否为初始化数据
	* @param apyInitStatus
	*/
	public void setApyInitStatus(Integer apyInitStatus) {
		this.apyInitStatus = apyInitStatus;
	}

	/** 
	* 是否能够继续借款 0 不能 1可以
	* @return
	*/
	public Integer getReLoanState() {
		return reLoanState;
	}

	/** 
	* 是否能够继续借款 0 不能 1可以
	* @param reLoanState
	*/
	public void setReLoanState(Integer reLoanState) {
		this.reLoanState = reLoanState;
	}

	/** 
	* 结算人姓名
	* @return
	*/
	public String getBalanceUserName() {
		return balanceUserName;
	}

	/** 
	* 结算人姓名
	* @param balanceUserName
	*/
	public void setBalanceUserName(String balanceUserName) {
		this.balanceUserName = balanceUserName;
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
}
