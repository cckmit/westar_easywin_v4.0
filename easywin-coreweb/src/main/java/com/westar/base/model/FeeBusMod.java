package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 借款申请模块业务表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FeeBusMod {
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
	* 申请记录主键
	*/
	@Filed
	private Integer feeBudgetId;
	/** 
	* 关联业务主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 关联业务类型，列值与BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String busType;
	/** 
	* 关联模块数据
	*/
	@Filed
	private String tripBusName;
	/** 
	* 借款方式 1额度借款 2直接借款
	*/
	@Filed
	private String loanWay;

	/****************以上主要为系统表字段********************/

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
	* 关联业务主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 关联业务主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 关联业务类型，列值与BusinessTypeConstant常量一一对应
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 关联业务类型，列值与BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 关联模块数据
	* @param tripBusName
	*/
	public void setTripBusName(String tripBusName) {
		this.tripBusName = tripBusName;
	}

	/** 
	* 关联模块数据
	* @return
	*/
	public String getTripBusName() {
		return tripBusName;
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
}
