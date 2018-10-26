package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 交易订单表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Orders {
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
	* 企业编号 0为系统默认
	*/
	@Filed
	private Integer comId;
	/** 
	* 是否需要发票；1-需要，0-不需要
	*/
	@Filed
	private Integer bill;
	/** 
	* 商品数量
	*/
	@Filed
	private Integer productNum;
	/** 
	* 原单价
	*/
	@Filed
	private Integer originalPrice;
	/** 
	* 折扣;取值范围0~1
	*/
	@Filed
	private Float discount;
	/** 
	* 联系人姓名
	*/
	@Filed
	private String linkerName;
	/** 
	* 联系人电话
	*/
	@Filed
	private String linkerPhone;
	/** 
	* 订单类型  参见数据字典orderType
	*/
	@Filed
	private String orderType;
	/** 
	* 交易状态；0-未支付；1-已经支付
	*/
	@Filed
	private Integer status;
	/** 
	* 支付方式；参见字典表datadic中paidWay
	*/
	@Filed
	private Integer paidWay;
	/** 
	* 支付时间
	*/
	@Filed
	private String paidTime;
	/** 
	* 订单号；当前时间+订单主键
	*/
	@Filed
	private String orderTradeNo;
	/** 
	* 实际交易价格
	*/
	@Filed
	private Integer transactionMoney;

	/****************以上主要为系统表字段********************/
	private Integer years;
	/** 
	* 用户数
	*/
	private Integer usersNum;
	/** 
	* 原始总价
	*/
	private Integer originalTotalPrice;
	private Integer orderCost;
	/** 
	* 折扣优惠
	*/
	private Integer discountPrice;
	private Integer orgBalanceMoney;
	/** 
	* 交易类型名称
	*/
	private String orderTypeName;
	/** 
	* 超期天数
	*/
	private Integer overDates;
	/** 
	* 检索开始日期
	*/
	private String startDate;
	/** 
	* 检索结束日期
	*/
	private String endDate;
	/** 
	* 团队名称
	*/
	private String orgName;
	/** 
	* 支付方式名称
	*/
	private String paidWayName;
	/** 
	* 折扣标准主键
	*/
	private Integer discountStandardId;
	private Integer actualPrice;
	/** 
	* 收费标准比较值
	*/
	private Integer chargingstandard;
	/** 
	* 折扣描述
	*/
	private String discountDescrible;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;

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
	* 企业编号 0为系统默认
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 企业编号 0为系统默认
	* @return
	*/
	public Integer getComId() {
		return comId;
	}

	/** 
	* 是否需要发票；1-需要，0-不需要
	* @param bill
	*/
	public void setBill(Integer bill) {
		this.bill = bill;
	}

	/** 
	* 是否需要发票；1-需要，0-不需要
	* @return
	*/
	public Integer getBill() {
		return bill;
	}

	/** 
	* 商品数量
	* @param productNum
	*/
	public void setProductNum(Integer productNum) {
		this.productNum = productNum;
	}

	/** 
	* 商品数量
	* @return
	*/
	public Integer getProductNum() {
		return productNum;
	}

	/** 
	* 原单价
	* @param originalPrice
	*/
	public void setOriginalPrice(Integer originalPrice) {
		this.originalPrice = originalPrice;
	}

	/** 
	* 原单价
	* @return
	*/
	public Integer getOriginalPrice() {
		return originalPrice;
	}

	public void setActualPrice(Integer actualPrice) {
		this.actualPrice = actualPrice;
	}

	public Integer getActualPrice() {
		return actualPrice;
	}

	/** 
	* 折扣;取值范围0~1
	* @param discount
	*/
	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	/** 
	* 折扣;取值范围0~1
	* @return
	*/
	public Float getDiscount() {
		return discount;
	}

	/** 
	* 联系人姓名
	* @param linkerName
	*/
	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
	}

	/** 
	* 联系人姓名
	* @return
	*/
	public String getLinkerName() {
		return linkerName;
	}

	/** 
	* 联系人电话
	* @param linkerPhone
	*/
	public void setLinkerPhone(String linkerPhone) {
		this.linkerPhone = linkerPhone;
	}

	/** 
	* 联系人电话
	* @return
	*/
	public String getLinkerPhone() {
		return linkerPhone;
	}

	/** 
	* 订单类型  参见数据字典orderType
	* @param orderType
	*/
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/** 
	* 订单类型  参见数据字典orderType
	* @return
	*/
	public String getOrderType() {
		return orderType;
	}

	/** 
	* 交易状态；0-未支付；1-已经支付
	* @param status
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	* 交易状态；0-未支付；1-已经支付
	* @return
	*/
	public Integer getStatus() {
		return status;
	}

	/** 
	* 支付方式；参见字典表datadic中paidWay
	* @param paidWay
	*/
	public void setPaidWay(Integer paidWay) {
		this.paidWay = paidWay;
	}

	/** 
	* 支付方式；参见字典表datadic中paidWay
	* @return
	*/
	public Integer getPaidWay() {
		return paidWay;
	}

	public Integer getYears() {
		return years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}

	/** 
	* 用户数
	* @return
	*/
	public Integer getUsersNum() {
		return usersNum;
	}

	/** 
	* 用户数
	* @param usersNum
	*/
	public void setUsersNum(Integer usersNum) {
		this.usersNum = usersNum;
	}

	/** 
	* 交易类型名称
	* @return
	*/
	public String getOrderTypeName() {
		return orderTypeName;
	}

	/** 
	* 交易类型名称
	* @param orderTypeName
	*/
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	/** 
	* 超期天数
	* @return
	*/
	public Integer getOverDates() {
		return overDates;
	}

	/** 
	* 超期天数
	* @param overDates
	*/
	public void setOverDates(Integer overDates) {
		this.overDates = overDates;
	}

	/** 
	* 检索开始日期
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 检索开始日期
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 检索结束日期
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 检索结束日期
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 团队名称
	* @return
	*/
	public String getOrgName() {
		return orgName;
	}

	/** 
	* 团队名称
	* @param orgName
	*/
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/** 
	* 支付方式名称
	* @return
	*/
	public String getPaidWayName() {
		return paidWayName;
	}

	/** 
	* 支付方式名称
	* @param paidWayName
	*/
	public void setPaidWayName(String paidWayName) {
		this.paidWayName = paidWayName;
	}

	/** 
	* 支付时间
	* @param paidTime
	*/
	public void setPaidTime(String paidTime) {
		this.paidTime = paidTime;
	}

	/** 
	* 支付时间
	* @return
	*/
	public String getPaidTime() {
		return paidTime;
	}

	/** 
	* 折扣标准主键
	* @return
	*/
	public Integer getDiscountStandardId() {
		return discountStandardId;
	}

	/** 
	* 折扣标准主键
	* @param discountStandardId
	*/
	public void setDiscountStandardId(Integer discountStandardId) {
		this.discountStandardId = discountStandardId;
	}

	/** 
	* 收费标准比较值
	* @return
	*/
	public Integer getChargingstandard() {
		return chargingstandard;
	}

	/** 
	* 收费标准比较值
	* @param chargingstandard
	*/
	public void setChargingstandard(Integer chargingstandard) {
		this.chargingstandard = chargingstandard;
	}

	/** 
	* 折扣描述
	* @return
	*/
	public String getDiscountDescrible() {
		return discountDescrible;
	}

	/** 
	* 折扣描述
	* @param discountDescrible
	*/
	public void setDiscountDescrible(String discountDescrible) {
		this.discountDescrible = discountDescrible;
	}

	/** 
	* 原始总价
	* @return
	*/
	public Integer getOriginalTotalPrice() {
		return originalTotalPrice;
	}

	/** 
	* 原始总价
	* @param originalTotalPrice
	*/
	public void setOriginalTotalPrice(Integer originalTotalPrice) {
		this.originalTotalPrice = originalTotalPrice;
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
	* 订单号；当前时间+订单主键
	* @param orderTradeNo
	*/
	public void setOrderTradeNo(String orderTradeNo) {
		this.orderTradeNo = orderTradeNo;
	}

	/** 
	* 订单号；当前时间+订单主键
	* @return
	*/
	public String getOrderTradeNo() {
		return orderTradeNo;
	}

	public Integer getOrgBalanceMoney() {
		return orgBalanceMoney;
	}

	public void setOrgBalanceMoney(Integer orgBalanceMoney) {
		this.orgBalanceMoney = orgBalanceMoney;
	}

	/** 
	* 折扣优惠
	* @return
	*/
	public Integer getDiscountPrice() {
		return discountPrice;
	}

	/** 
	* 折扣优惠
	* @param discountPrice
	*/
	public void setDiscountPrice(Integer discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Integer getOrderCost() {
		return orderCost;
	}

	public void setOrderCost(Integer orderCost) {
		this.orderCost = orderCost;
	}

	@Override
	public String toString() {
		return "Orders [id=" + id + ", recordCreateTime=" + recordCreateTime + ", comId=" + comId + ", bill=" + bill
				+ ", productNum=" + productNum + ", originalPrice=" + originalPrice + ", discount=" + discount
				+ ", linkerName=" + linkerName + ", linkerPhone=" + linkerPhone + ", orderType=" + orderType
				+ ", status=" + status + ", paidWay=" + paidWay + ", paidTime=" + paidTime + ", orderTradeNo="
				+ orderTradeNo + ", years=" + years + ", usersNum=" + usersNum + ", originalTotalPrice="
				+ originalTotalPrice + ", orderCost=" + orderCost + ", discountPrice=" + discountPrice
				+ ", orgBalanceMoney=" + orgBalanceMoney + ", orderTypeName=" + orderTypeName + ", overDates="
				+ overDates + ", startDate=" + startDate + ", endDate=" + endDate + ", orgName=" + orgName
				+ ", paidWayName=" + paidWayName + ", discountStandardId=" + discountStandardId + ", actualPrice="
				+ actualPrice + ", chargingstandard=" + chargingstandard + ", discountDescrible=" + discountDescrible
				+ ", succ=" + succ + ", promptMsg=" + promptMsg + "]";
	}

	/** 
	* 实际交易价格
	* @param transactionMoney
	*/
	public void setTransactionMoney(Integer transactionMoney) {
		this.transactionMoney = transactionMoney;
	}

	/** 
	* 实际交易价格
	* @return
	*/
	public Integer getTransactionMoney() {
		return transactionMoney;
	}
}
