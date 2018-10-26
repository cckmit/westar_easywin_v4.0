package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 团队使用空间配置表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class OrganicSpaceCfg {
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
	* 激活状态的使用人临界值
	*/
	@Filed
	private Integer usersNum;
	/** 
	* 单位：G
	*/
	@Filed
	private Integer storageSpace;
	/** 
	* 开始日期
	*/
	@Filed
	private String startDate;
	/** 
	* 到期日期
	*/
	@Filed
	private String endDate;
	/** 
	* 关联orders主键
	*/
	@Filed
	private Integer orderId;

	/****************以上主要为系统表字段********************/
	/** 
	* 团队名称
	*/
	private String orgName;
	/** 
	* 团队购买服务状态
	*/
	private Integer orgServiceStatus;
	private Integer transactionMoney;
	private Integer orderCost;
	/** 
	* 团队结余
	*/
	private Integer orgBalanceMoney;

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
	* 激活状态的使用人临界值
	* @param usersNum
	*/
	public void setUsersNum(Integer usersNum) {
		this.usersNum = usersNum;
	}

	/** 
	* 激活状态的使用人临界值
	* @return
	*/
	public Integer getUsersNum() {
		return usersNum;
	}

	/** 
	* 单位：G
	* @param storageSpace
	*/
	public void setStorageSpace(Integer storageSpace) {
		this.storageSpace = storageSpace;
	}

	/** 
	* 单位：G
	* @return
	*/
	public Integer getStorageSpace() {
		return storageSpace;
	}

	/** 
	* 开始日期
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 开始日期
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 到期日期
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 到期日期
	* @return
	*/
	public String getEndDate() {
		return endDate;
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
	* 团队购买服务状态
	* @return
	*/
	public Integer getOrgServiceStatus() {
		return orgServiceStatus;
	}

	/** 
	* 团队购买服务状态
	* @param orgServiceStatus
	*/
	public void setOrgServiceStatus(Integer orgServiceStatus) {
		this.orgServiceStatus = orgServiceStatus;
	}

	/** 
	* 关联orders主键
	* @param orderId
	*/
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/** 
	* 关联orders主键
	* @return
	*/
	public Integer getOrderId() {
		return orderId;
	}

	public Integer getTransactionMoney() {
		return transactionMoney;
	}

	public void setTransactionMoney(Integer transactionMoney) {
		this.transactionMoney = transactionMoney;
	}

	public Integer getOrderCost() {
		return orderCost;
	}

	public void setOrderCost(Integer orderCost) {
		this.orderCost = orderCost;
	}

	/** 
	* 团队结余
	* @return
	*/
	public Integer getOrgBalanceMoney() {
		return orgBalanceMoney;
	}

	/** 
	* 团队结余
	* @param orgBalanceMoney
	*/
	public void setOrgBalanceMoney(Integer orgBalanceMoney) {
		this.orgBalanceMoney = orgBalanceMoney;
	}
}
