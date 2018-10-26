package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 任务催办提醒人
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BusRemindUser {
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
	* 任务催办id
	*/
	@Filed
	private Integer busRemindId;
	/** 
	* 提醒人
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 提醒人姓名
	*/
	private String userName;
	/** 
	* 当前审批的名称
	*/
	private String flowName;

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
	* 任务催办id
	* @param busRemindId
	*/
	public void setBusRemindId(Integer busRemindId) {
		this.busRemindId = busRemindId;
	}

	/** 
	* 任务催办id
	* @return
	*/
	public Integer getBusRemindId() {
		return busRemindId;
	}

	/** 
	* 提醒人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 提醒人
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 提醒人姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 提醒人姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 当前审批的名称
	* @return
	*/
	public String getFlowName() {
		return flowName;
	}

	/** 
	* 当前审批的名称
	* @param flowName
	*/
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
}
