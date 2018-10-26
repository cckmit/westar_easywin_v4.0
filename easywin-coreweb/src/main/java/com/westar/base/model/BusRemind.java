package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 催办
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BusRemind {
	/** 
	* 主键id
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
	* 业务主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String busType;
	/** 
	* 操作者id
	*/
	@Filed
	private Integer userId;
	/** 
	* 催办信息
	*/
	@Filed
	private String content;
	/** 
	* 催办模块名称
	*/
	@Filed
	private String busModName;

	/****************以上主要为系统表字段********************/
	/** 
	* 操作人名称
	*/
	private String userName;
	/** 
	* 是否短信通知
	*/
	private String msgSendFlag;
	/** 
	* 提醒人集合
	*/
	private List<BusRemindUser> listBusRemindUser;
	/** 
	* 提醒人所在部门集合
	*/
	private List<Department> listBusRemindDep;
	/** 
	* 被催办人主键
	*/
	private Integer remindUserId;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;

	/****************以上为自己添加字段********************/
	/** 
	* 主键id
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/** 
	* 主键id
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
	* 业务主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 业务主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 操作者id
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 操作者id
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 催办信息
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 催办信息
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 催办模块名称
	* @param busModName
	*/
	public void setBusModName(String busModName) {
		this.busModName = busModName;
	}

	/** 
	* 催办模块名称
	* @return
	*/
	public String getBusModName() {
		return busModName;
	}

	/** 
	* 操作人名称
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 操作人名称
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 是否短信通知
	* @return
	*/
	public String getMsgSendFlag() {
		return msgSendFlag;
	}

	/** 
	* 是否短信通知
	* @param msgSendFlag
	*/
	public void setMsgSendFlag(String msgSendFlag) {
		this.msgSendFlag = msgSendFlag;
	}

	/** 
	* 提醒人集合
	* @return
	*/
	public List<BusRemindUser> getListBusRemindUser() {
		return listBusRemindUser;
	}

	/** 
	* 提醒人集合
	* @param listBusRemindUser
	*/
	public void setListBusRemindUser(List<BusRemindUser> listBusRemindUser) {
		this.listBusRemindUser = listBusRemindUser;
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
	* 被催办人主键
	* @return
	*/
	public Integer getRemindUserId() {
		return remindUserId;
	}

	/** 
	* 被催办人主键
	* @param remindUserId
	*/
	public void setRemindUserId(Integer remindUserId) {
		this.remindUserId = remindUserId;
	}

	/** 
	* 提醒人所在部门集合
	* @return
	*/
	public List<Department> getListBusRemindDep() {
		return listBusRemindDep;
	}

	/** 
	* 提醒人所在部门集合
	* @param listBusRemindDep
	*/
	public void setListBusRemindDep(List<Department> listBusRemindDep) {
		this.listBusRemindDep = listBusRemindDep;
	}
}
