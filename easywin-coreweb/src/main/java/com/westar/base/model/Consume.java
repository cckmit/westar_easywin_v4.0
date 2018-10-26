package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 消费记录表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Consume {
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
	* 费用类型
	*/
	@Filed(formColumn = true)
	private Integer type;
	/** 
	* 消费金额
	*/
	@Filed(formColumn = true)
	private String amount;
	/** 
	* 消费描述
	*/
	@Filed(formColumn = true)
	private String describe;
	/** 
	* 消费开始日期
	*/
	@Filed(formColumn = true)
	private String startDate;
	/** 
	* 消费结束日期
	*/
	@Filed(formColumn = true)
	private String endDate;
	/** 
	* 消费归属
	*/
	@Filed(formColumn = true)
	private String belong;
	/** 
	* 消费人数
	*/
	@Filed(formColumn = true)
	private Integer consumePersonNum;
	/** 
	* 出发地
	*/
	@Filed(formColumn = true)
	private String leavePlace;
	/** 
	* 目的地
	*/
	@Filed(formColumn = true)
	private String arrivePlace;
	/** 
	* 发票数量
	*/
	@Filed(formColumn = true)
	private Integer invoiceNum;
	/** 
	* 状态  0待报销  1报销中 2已报销
	*/
	@Filed
	private Integer status;
	/** 
	* 创建人
	*/
	@Filed
	private Integer creator;

	/****************以上主要为系统表字段********************/
	/** 
	* 关联流程主键
	*/
	private Integer instanceId;
	/** 
	* 关联流程名称
	*/
	private String spFlowName;
	/** 
	* 类型名
	*/
	private String typeName;
	/** 
	* 费用类型
	*/
	private List<ConsumeType> listConsumeType;
	/** 
	* 附件集合
	*/
	private List<ConsumeUpfile> listUpfiles;
	/** 
	* 消费时间开始查询
	*/
	private String consumeStartDate;
	/** 
	* 消费时间结束查询
	*/
	private String consumeEndDate;
	/** 
	* 创建时间开始查询
	*/
	private String createStartDate;
	/** 
	* 创建时间结束查询
	*/
	private String createEndDate;
	/** 
	* 结算支付方式
	*/
	private String payType;
	/** 
	* 结算人
	*/
	private String balanceUserName;
	/** 
	* 结算时间
	*/
	private String balanceTime;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 消费次数
	*/
	private Integer consumeTimes;
	/** 
	* 是否是当前日期的第一条数据 1 是 0 不是
	*/
	private Integer firstData;

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
	* 费用类型
	* @param type
	*/
	public void setType(Integer type) {
		this.type = type;
	}

	/** 
	* 费用类型
	* @return
	*/
	public Integer getType() {
		return type;
	}

	/** 
	* 消费金额
	* @param amount
	*/
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/** 
	* 消费金额
	* @return
	*/
	public String getAmount() {
		return amount;
	}

	/** 
	* 消费描述
	* @param describe
	*/
	public void setDescribe(String describe) {
		this.describe = describe;
	}

	/** 
	* 消费描述
	* @return
	*/
	public String getDescribe() {
		return describe;
	}

	/** 
	* 消费开始日期
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 消费开始日期
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 消费结束日期
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 消费结束日期
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 消费归属
	* @param belong
	*/
	public void setBelong(String belong) {
		this.belong = belong;
	}

	/** 
	* 消费归属
	* @return
	*/
	public String getBelong() {
		return belong;
	}

	/** 
	* 消费人数
	* @param consumePersonNum
	*/
	public void setConsumePersonNum(Integer consumePersonNum) {
		this.consumePersonNum = consumePersonNum;
	}

	/** 
	* 消费人数
	* @return
	*/
	public Integer getConsumePersonNum() {
		return consumePersonNum;
	}

	/** 
	* 出发地
	* @param leavePlace
	*/
	public void setLeavePlace(String leavePlace) {
		this.leavePlace = leavePlace;
	}

	/** 
	* 出发地
	* @return
	*/
	public String getLeavePlace() {
		return leavePlace;
	}

	/** 
	* 目的地
	* @param arrivePlace
	*/
	public void setArrivePlace(String arrivePlace) {
		this.arrivePlace = arrivePlace;
	}

	/** 
	* 目的地
	* @return
	*/
	public String getArrivePlace() {
		return arrivePlace;
	}

	/** 
	* 发票数量
	* @param invoiceNum
	*/
	public void setInvoiceNum(Integer invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	/** 
	* 发票数量
	* @return
	*/
	public Integer getInvoiceNum() {
		return invoiceNum;
	}

	/** 
	* 状态  0待报销  1报销中 2已报销
	* @param status
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	* 状态  0待报销  1报销中 2已报销
	* @return
	*/
	public Integer getStatus() {
		return status;
	}

	/** 
	* 创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 类型名
	* @return
	*/
	public String getTypeName() {
		return typeName;
	}

	/** 
	* 类型名
	* @param typeName
	*/
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/** 
	* 费用类型
	* @return
	*/
	public List<ConsumeType> getListConsumeType() {
		return listConsumeType;
	}

	/** 
	* 费用类型
	* @param listConsumeType
	*/
	public void setListConsumeType(List<ConsumeType> listConsumeType) {
		this.listConsumeType = listConsumeType;
	}

	/** 
	* 附件集合
	* @return
	*/
	public List<ConsumeUpfile> getListUpfiles() {
		return listUpfiles;
	}

	/** 
	* 附件集合
	* @param listUpfiles
	*/
	public void setListUpfiles(List<ConsumeUpfile> listUpfiles) {
		this.listUpfiles = listUpfiles;
	}

	/** 
	* 消费时间开始查询
	* @return
	*/
	public String getConsumeStartDate() {
		return consumeStartDate;
	}

	/** 
	* 消费时间开始查询
	* @param consumeStartDate
	*/
	public void setConsumeStartDate(String consumeStartDate) {
		this.consumeStartDate = consumeStartDate;
	}

	/** 
	* 消费时间结束查询
	* @return
	*/
	public String getConsumeEndDate() {
		return consumeEndDate;
	}

	/** 
	* 消费时间结束查询
	* @param consumeEndDate
	*/
	public void setConsumeEndDate(String consumeEndDate) {
		this.consumeEndDate = consumeEndDate;
	}

	/** 
	* 创建时间开始查询
	* @return
	*/
	public String getCreateStartDate() {
		return createStartDate;
	}

	/** 
	* 创建时间开始查询
	* @param createStartDate
	*/
	public void setCreateStartDate(String createStartDate) {
		this.createStartDate = createStartDate;
	}

	/** 
	* 创建时间结束查询
	* @return
	*/
	public String getCreateEndDate() {
		return createEndDate;
	}

	/** 
	* 创建时间结束查询
	* @param createEndDate
	*/
	public void setCreateEndDate(String createEndDate) {
		this.createEndDate = createEndDate;
	}

	/** 
	* 关联流程主键
	* @return
	*/
	public Integer getInstanceId() {
		return instanceId;
	}

	/** 
	* 关联流程主键
	* @param instanceId
	*/
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	/** 
	* 关联流程名称
	* @return
	*/
	public String getSpFlowName() {
		return spFlowName;
	}

	/** 
	* 关联流程名称
	* @param spFlowName
	*/
	public void setSpFlowName(String spFlowName) {
		this.spFlowName = spFlowName;
	}

	/** 
	* 结算支付方式
	* @return
	*/
	public String getPayType() {
		return payType;
	}

	/** 
	* 结算支付方式
	* @param payType
	*/
	public void setPayType(String payType) {
		this.payType = payType;
	}

	/** 
	* 结算人
	* @return
	*/
	public String getBalanceUserName() {
		return balanceUserName;
	}

	/** 
	* 结算人
	* @param balanceUserName
	*/
	public void setBalanceUserName(String balanceUserName) {
		this.balanceUserName = balanceUserName;
	}

	/** 
	* 结算时间
	* @return
	*/
	public String getBalanceTime() {
		return balanceTime;
	}

	/** 
	* 结算时间
	* @param balanceTime
	*/
	public void setBalanceTime(String balanceTime) {
		this.balanceTime = balanceTime;
	}

	public boolean isSucc() {
		return succ;
	}

	/** 
	* boolean标识
	* @param succ
	*/
	public void setSucc(boolean succ) {
		this.succ = succ;
	}

	/** 
	* 提示信息
	* @return
	*/
	public String getPromptMsg() {
		return promptMsg;
	}

	/** 
	* 提示信息
	* @param promptMsg
	*/
	public void setPromptMsg(String promptMsg) {
		this.promptMsg = promptMsg;
	}

	/** 
	* 消费次数
	* @return
	*/
	public Integer getConsumeTimes() {
		return consumeTimes;
	}

	/** 
	* 消费次数
	* @param consumeTimes
	*/
	public void setConsumeTimes(Integer consumeTimes) {
		this.consumeTimes = consumeTimes;
	}

	/** 
	* 是否是当前日期的第一条数据 1 是 0 不是
	* @return
	*/
	public Integer getFirstData() {
		return firstData;
	}

	/** 
	* 是否是当前日期的第一条数据 1 是 0 不是
	* @param firstData
	*/
	public void setFirstData(Integer firstData) {
		this.firstData = firstData;
	}
}
