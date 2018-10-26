package com.westar.base.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;

/** 
 * 报销记录表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FeeLoanOff {
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
	* 流程实例化主键
	*/
	@Filed
	private Integer instanceId;
	/** 
	* 借款关联主键 loanApply和loan
	*/
	@Filed
	private Integer feeBudgetId;
	/** 
	* 报销说明记录表主键
	*/
	@Filed
	private Integer loanReportId;
	/** 
	* 汇报人
	*/
	@Filed
	private Integer creator;
	/** 
	* 销账金额
	*/
	@Filed
	private String loanOffBalance;
	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据 
	*/
	@Filed
	private Integer status;
	/** 
	* 报销金额
	*/
	@Filed
	private String loanOffItemFee;
	/** 
	* 借款方式 1额度借款 2直接借款
	*/
	@Filed
	private String loanWay;
	/** 
	* 报销方式 1汇报后报销 2直接报销 3关联模块报销
	*/
	@Filed
	private String loanReportWay;
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
	* 是否完成结算 1是；
	*/
	@Filed
	private Integer balanceState;
	/** 
	* 支付方式0现金1银联转账
	*/
	@Filed
	private String payType;
	/** 
	* 结算时间
	*/
	@Filed
	private String balanceTime;
	/** 
	* 结算出款人
	*/
	@Filed
	private Integer balanceUserId;
	/** 
	* 预报销金额
	*/
	@Filed
	private String loanOffPreFee;

	/****************以上主要为系统表字段********************/
	/** 
	* 是否能够继续借款 0 不能 1可以
	*/
	private Integer reLoanState;
	/** 
	* 关联出差审批事项名称
	*/
	private String loanApplyName;
	/** 
	* 总借款
	*/
	private String loanFeeTotal;
	/** 
	* 关联出差审批事项主键
	*/
	private Integer loanApplyInsId;
	/** 
	* 是否为初始化数据
	*/
	private Integer apyInitStatus;
	/** 
	* 汇报的名称
	*/
	private String loanReportName;
	/** 
	* 汇报人姓名
	*/
	private String loanReporterName;
	/** 
	* 汇报的流程主键
	*/
	private Integer loanRepInsId;
	/** 
	* 汇报的状态
	*/
	private Integer loanRepStatus;
	/** 
	* 汇报的流程状态
	*/
	private Integer loanRepFlowStatus;
	/** 
	* 汇报的时间
	*/
	private String loanRepDate;
	/** 
	* 汇报的开始时间
	*/
	private String loanRepStartDate;
	/** 
	* 汇报单号
	*/
	private String repSerialNumber;
	private String loanRepEndDate;
	/** 
	* 报销审批流程名称
	*/
	private String loanOffName;
	/** 
	* 创建人名称
	*/
	private String creatorName;
	/** 
	* 报销流程状态
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
	* 发起人集合
	*/
	private List<UserInfo> listCreator;
	/** 
	* 报销到了第几步；0；未开始；1；说明；2；报销中
	*/
	private Integer stepth;
	/** 
	* 报销说明流程主键
	*/
	private Integer loanRepFlowId;
	/** 
	* 单号
	*/
	private String flowserialNumber;
	/** 
	* 已完成的报销次数
	*/
	private Integer loanOffDoneTimes;
	/** 
	* 已完成的报销总额
	*/
	private Integer loanDoneItemTotal;
	/** 
	* 已完成的销账总额
	*/
	private Integer loanOffDoneTotal;
	/** 
	* 报销中的次数
	*/
	private Integer loanOffDoingTimes;
	/** 
	* 报销中的金额
	*/
	private Integer loanDoingItemTotal;
	/** 
	* 报销中的销账金额
	*/
	private Integer loanOffDoingTotal;
	/** 
	* 报销进度标识
	*/
	private String stepType;
	/** 
	* 结算人姓名
	*/
	private String balanceUserName;
	/** 
	* 是否是出差报销失败未重新报销的0不是，大于0是
	*/
	private String canReApply;

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
	* 借款关联主键 loanApply和loan
	* @param feeBudgetId
	*/
	public void setFeeBudgetId(Integer feeBudgetId) {
		this.feeBudgetId = feeBudgetId;
	}

	/** 
	* 借款关联主键 loanApply和loan
	* @return
	*/
	public Integer getFeeBudgetId() {
		return feeBudgetId;
	}

	/** 
	* 报销说明记录表主键
	* @param loanReportId
	*/
	public void setLoanReportId(Integer loanReportId) {
		this.loanReportId = loanReportId;
	}

	/** 
	* 报销说明记录表主键
	* @return
	*/
	public Integer getLoanReportId() {
		return loanReportId;
	}

	/** 
	* 汇报人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 汇报人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 销账金额
	* @param loanOffBalance
	*/
	public void setLoanOffBalance(String loanOffBalance) {
		this.loanOffBalance = loanOffBalance;
	}

	/** 
	* 销账金额
	* @return
	*/
	public String getLoanOffBalance() {
		return loanOffBalance;
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
	* 报销流程状态
	* @return
	*/
	public Integer getFlowState() {
		return flowState;
	}

	/** 
	* 报销流程状态
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

	public Integer getSpState() {
		return spState;
	}

	public void setSpState(Integer spState) {
		this.spState = spState;
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
	* 汇报的名称
	* @return
	*/
	public String getLoanReportName() {
		return loanReportName;
	}

	/** 
	* 汇报的名称
	* @param loanReportName
	*/
	public void setLoanReportName(String loanReportName) {
		this.loanReportName = loanReportName;
	}

	/** 
	* 关联出差审批事项名称
	* @return
	*/
	public String getLoanApplyName() {
		return loanApplyName;
	}

	/** 
	* 关联出差审批事项名称
	* @param loanApplyName
	*/
	public void setLoanApplyName(String loanApplyName) {
		this.loanApplyName = loanApplyName;
	}

	/** 
	* 报销审批流程名称
	* @return
	*/
	public String getLoanOffName() {
		return loanOffName;
	}

	/** 
	* 报销审批流程名称
	* @param loanOffName
	*/
	public void setLoanOffName(String loanOffName) {
		this.loanOffName = loanOffName;
	}

	/** 
	* 汇报的流程主键
	* @return
	*/
	public Integer getLoanRepInsId() {
		return loanRepInsId;
	}

	/** 
	* 汇报的流程主键
	* @param loanRepInsId
	*/
	public void setLoanRepInsId(Integer loanRepInsId) {
		this.loanRepInsId = loanRepInsId;
	}

	/** 
	* 汇报人姓名
	* @return
	*/
	public String getLoanReporterName() {
		return loanReporterName;
	}

	/** 
	* 汇报人姓名
	* @param loanReporterName
	*/
	public void setLoanReporterName(String loanReporterName) {
		this.loanReporterName = loanReporterName;
	}

	/** 
	* 汇报的状态
	* @return
	*/
	public Integer getLoanRepStatus() {
		return loanRepStatus;
	}

	/** 
	* 汇报的状态
	* @param loanRepStatus
	*/
	public void setLoanRepStatus(Integer loanRepStatus) {
		this.loanRepStatus = loanRepStatus;
	}

	/** 
	* 汇报的流程状态
	* @return
	*/
	public Integer getLoanRepFlowStatus() {
		return loanRepFlowStatus;
	}

	/** 
	* 汇报的流程状态
	* @param loanRepFlowStatus
	*/
	public void setLoanRepFlowStatus(Integer loanRepFlowStatus) {
		this.loanRepFlowStatus = loanRepFlowStatus;
	}

	/** 
	* 汇报的开始时间
	* @return
	*/
	public String getLoanRepStartDate() {
		return loanRepStartDate;
	}

	/** 
	* 汇报的开始时间
	* @param loanRepStartDate
	*/
	public void setLoanRepStartDate(String loanRepStartDate) {
		this.loanRepStartDate = loanRepStartDate;
	}

	public String getLoanRepEndDate() {
		return loanRepEndDate;
	}

	public void setLoanRepEndDate(String loanRepEndDate) {
		this.loanRepEndDate = loanRepEndDate;
	}

	/** 
	* 汇报的时间
	* @return
	*/
	public String getLoanRepDate() {
		return loanRepDate;
	}

	/** 
	* 汇报的时间
	* @param loanRepDate
	*/
	public void setLoanRepDate(String loanRepDate) {
		this.loanRepDate = loanRepDate;
	}

	/** 
	* 报销金额
	* @param loanOffItemFee
	*/
	public void setLoanOffItemFee(String loanOffItemFee) {
		this.loanOffItemFee = loanOffItemFee;
	}

	/** 
	* 报销金额
	* @return
	*/
	public String getLoanOffItemFee() {
		return loanOffItemFee;
	}

	/** 
	* 报销到了第几步；0；未开始；1；说明；2；报销中
	* @return
	*/
	public Integer getStepth() {
		return stepth;
	}

	/** 
	* 报销到了第几步；0；未开始；1；说明；2；报销中
	* @param stepth
	*/
	public void setStepth(Integer stepth) {
		this.stepth = stepth;
	}

	/** 
	* 报销说明流程主键
	* @return
	*/
	public Integer getLoanRepFlowId() {
		return loanRepFlowId;
	}

	/** 
	* 报销说明流程主键
	* @param loanRepFlowId
	*/
	public void setLoanRepFlowId(Integer loanRepFlowId) {
		this.loanRepFlowId = loanRepFlowId;
	}

	/** 
	* 借款方式 1额度借款 2直接借款
	* @param loanWay
	*/
	public void setLoanWay(String loanWay) {
		this.loanWay = loanWay;
	}

	/** 
	* 借款方式 1额度借款 2直接借款
	* @return
	*/
	public String getLoanWay() {
		return loanWay;
	}

	/** 
	* 报销方式 1汇报后报销 2直接报销 3关联模块报销
	* @param loanReportWay
	*/
	public void setLoanReportWay(String loanReportWay) {
		this.loanReportWay = loanReportWay;
	}

	/** 
	* 报销方式 1汇报后报销 2直接报销 3关联模块报销
	* @return
	*/
	public String getLoanReportWay() {
		return loanReportWay;
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
	* 是否完成结算 1是；
	* @param balanceState
	*/
	public void setBalanceState(Integer balanceState) {
		this.balanceState = balanceState;
	}

	/** 
	* 是否完成结算 1是；
	* @return
	*/
	public Integer getBalanceState() {
		return balanceState;
	}

	/** 
	* 单号
	* @return
	*/
	public String getFlowserialNumber() {
		return flowserialNumber;
	}

	/** 
	* 单号
	* @param flowserialNumber
	*/
	public void setFlowserialNumber(String flowserialNumber) {
		this.flowserialNumber = flowserialNumber;
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
	* 结算时间
	* @param balanceTime
	*/
	public void setBalanceTime(String balanceTime) {
		this.balanceTime = balanceTime;
	}

	/** 
	* 结算时间
	* @return
	*/
	public String getBalanceTime() {
		return balanceTime;
	}

	/** 
	* 结算出款人
	* @param balanceUserId
	*/
	public void setBalanceUserId(Integer balanceUserId) {
		this.balanceUserId = balanceUserId;
	}

	/** 
	* 结算出款人
	* @return
	*/
	public Integer getBalanceUserId() {
		return balanceUserId;
	}

	/** 
	* 预报销金额
	* @param loanOffPreFee
	*/
	public void setLoanOffPreFee(String loanOffPreFee) {
		this.loanOffPreFee = loanOffPreFee;
	}

	/** 
	* 预报销金额
	* @return
	*/
	public String getLoanOffPreFee() {
		return loanOffPreFee;
	}

	/** 
	* 已完成的报销次数
	* @return
	*/
	public Integer getLoanOffDoneTimes() {
		return loanOffDoneTimes;
	}

	/** 
	* 已完成的报销次数
	* @param loanOffDoneTimes
	*/
	public void setLoanOffDoneTimes(Integer loanOffDoneTimes) {
		this.loanOffDoneTimes = loanOffDoneTimes;
	}

	/** 
	* 已完成的报销总额
	* @return
	*/
	public Integer getLoanDoneItemTotal() {
		return loanDoneItemTotal;
	}

	/** 
	* 已完成的报销总额
	* @param loanDoneItemTotal
	*/
	public void setLoanDoneItemTotal(Integer loanDoneItemTotal) {
		this.loanDoneItemTotal = loanDoneItemTotal;
	}

	/** 
	* 报销中的次数
	* @return
	*/
	public Integer getLoanOffDoingTimes() {
		return loanOffDoingTimes;
	}

	/** 
	* 报销中的次数
	* @param loanOffDoingTimes
	*/
	public void setLoanOffDoingTimes(Integer loanOffDoingTimes) {
		this.loanOffDoingTimes = loanOffDoingTimes;
	}

	/** 
	* 报销中的金额
	* @return
	*/
	public Integer getLoanDoingItemTotal() {
		return loanDoingItemTotal;
	}

	/** 
	* 报销中的金额
	* @param loanDoingItemTotal
	*/
	public void setLoanDoingItemTotal(Integer loanDoingItemTotal) {
		this.loanDoingItemTotal = loanDoingItemTotal;
	}

	/** 
	* 报销中的销账金额
	* @return
	*/
	public Integer getLoanOffDoingTotal() {
		return loanOffDoingTotal;
	}

	/** 
	* 报销中的销账金额
	* @param loanOffDoingTotal
	*/
	public void setLoanOffDoingTotal(Integer loanOffDoingTotal) {
		this.loanOffDoingTotal = loanOffDoingTotal;
	}

	/** 
	* 已完成的销账总额
	* @return
	*/
	public Integer getLoanOffDoneTotal() {
		return loanOffDoneTotal;
	}

	/** 
	* 已完成的销账总额
	* @param loanOffDoneTotal
	*/
	public void setLoanOffDoneTotal(Integer loanOffDoneTotal) {
		this.loanOffDoneTotal = loanOffDoneTotal;
	}

	/** 
	* 关联出差审批事项主键
	* @return
	*/
	public Integer getLoanApplyInsId() {
		return loanApplyInsId;
	}

	/** 
	* 关联出差审批事项主键
	* @param loanApplyInsId
	*/
	public void setLoanApplyInsId(Integer loanApplyInsId) {
		this.loanApplyInsId = loanApplyInsId;
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
	* 报销进度标识
	* @return
	*/
	public String getStepType() {
		return stepType;
	}

	/** 
	* 报销进度标识
	* @param stepType
	*/
	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	/** 
	* 汇报单号
	* @return
	*/
	public String getRepSerialNumber() {
		return repSerialNumber;
	}

	/** 
	* 汇报单号
	* @param repSerialNumber
	*/
	public void setRepSerialNumber(String repSerialNumber) {
		this.repSerialNumber = repSerialNumber;
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
	* 总借款
	* @return
	*/
	public String getLoanFeeTotal() {
		return loanFeeTotal;
	}

	/** 
	* 总借款
	* @param loanFeeTotal
	*/
	public void setLoanFeeTotal(String loanFeeTotal) {
		this.loanFeeTotal = loanFeeTotal;
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
	* 是否是出差报销失败未重新报销的0不是，大于0是
	* @return
	*/
	public String getCanReApply() {
		return canReApply;
	}

	/** 
	* 是否是出差报销失败未重新报销的0不是，大于0是
	* @param canReApply
	*/
	public void setCanReApply(String canReApply) {
		this.canReApply = canReApply;
	}
}
