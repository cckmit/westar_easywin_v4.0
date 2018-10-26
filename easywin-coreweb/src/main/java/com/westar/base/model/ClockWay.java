package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 定时提醒方式
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ClockWay {
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
	* 闹钟提醒方式
	*/
	@Filed
	private Integer clockId;
	/** 
	* 提醒方式 默认0即时通讯
	*/
	@Filed
	private String sendWay;
	/** 
	* 模块主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 模块类型 见系统常量
	*/
	@Filed
	private String busType;

	/****************以上主要为系统表字段********************/
	/** 
	* 是否选中
	*/
	private String checkState;
	/** 
	* 名称
	*/
	private String sendWayV;

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
	* 闹钟提醒方式
	* @param clockId
	*/
	public void setClockId(Integer clockId) {
		this.clockId = clockId;
	}

	/** 
	* 闹钟提醒方式
	* @return
	*/
	public Integer getClockId() {
		return clockId;
	}

	/** 
	* 提醒方式 默认0即时通讯
	* @param sendWay
	*/
	public void setSendWay(String sendWay) {
		this.sendWay = sendWay;
	}

	/** 
	* 提醒方式 默认0即时通讯
	* @return
	*/
	public String getSendWay() {
		return sendWay;
	}

	/** 
	* 是否选中
	* @return
	*/
	public String getCheckState() {
		return checkState;
	}

	/** 
	* 是否选中
	* @param checkState
	*/
	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	/** 
	* 名称
	* @return
	*/
	public String getSendWayV() {
		return sendWayV;
	}

	/** 
	* 名称
	* @param sendWayV
	*/
	public void setSendWayV(String sendWayV) {
		this.sendWayV = sendWayV;
	}

	/** 
	* 模块主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 模块主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 模块类型 见系统常量
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 模块类型 见系统常量
	* @return
	*/
	public String getBusType() {
		return busType;
	}
}
