package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 借款详情
 */
@Table
@JsonInclude(Include.NON_NULL)
public class LoanDetail {
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
	* 借款方式 1额度借款 2直接借款
	*/
	@Filed
	private String loanWay;
	/** 
	* 借款关联主键 loanApply和loan
	*/
	@Filed
	private Integer loanBusId;
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
	/** 
	* 企业编号
	*/
	@Filed
	private Integer comId;
	/** 
	* 出差人
	*/
	@Filed
	private Integer creator;

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
	* 借款关联主键 loanApply和loan
	* @param loanBusId
	*/
	public void setLoanBusId(Integer loanBusId) {
		this.loanBusId = loanBusId;
	}

	/** 
	* 借款关联主键 loanApply和loan
	* @return
	*/
	public Integer getLoanBusId() {
		return loanBusId;
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
}
