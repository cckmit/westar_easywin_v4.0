package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 定时提醒
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Clock {
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
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 提醒的内容
	*/
	@Filed
	private String content;
	/** 
	* 开始日期
	*/
	@Filed
	private String clockDate;
	/** 
	* 下一次开始日期
	*/
	@Filed
	private String clockNextDate;
	/** 
	* 开始时间
	*/
	@Filed
	private String clockTime;
	/** 
	* 重复类型
	*/
	@Filed
	private String clockRepType;
	/** 
	* 重复时间
	*/
	@Filed
	private String clockRepDate;
	/** 
	* 是否开启 0未开启 1已开启
	*/
	@Filed
	private String clockIsOn;
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
	/** 
	* 执行情况 0未执行 1待执行 2 已执行
	*/
	@Filed
	private String executeState;
	/** 
	* 闹铃消息类型 0普通消息 1待办事项
	*/
	@Filed
	private String clockMsgType;

	/****************以上主要为系统表字段********************/
	/** 
	* 提醒方式
	*/
	private List<ClockWay> listClockWay;
	/** 
	* 提醒对象
	*/
	private List<ClockPerson> clockPersons;
	/** 
	* 是否过期
	*/
	private String clockState;
	/** 
	* 查询时间起
	*/
	private String startDate;
	/** 
	* 查询时间止
	*/
	private String endDate;
	/** 
	* 模块名称
	*/
	private String entyName;

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
	* 人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 提醒的内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 提醒的内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 开始日期
	* @param clockDate
	*/
	public void setClockDate(String clockDate) {
		this.clockDate = clockDate;
	}

	/** 
	* 开始日期
	* @return
	*/
	public String getClockDate() {
		return clockDate;
	}

	/** 
	* 下一次开始日期
	* @param clockNextDate
	*/
	public void setClockNextDate(String clockNextDate) {
		this.clockNextDate = clockNextDate;
	}

	/** 
	* 下一次开始日期
	* @return
	*/
	public String getClockNextDate() {
		return clockNextDate;
	}

	/** 
	* 开始时间
	* @param clockTime
	*/
	public void setClockTime(String clockTime) {
		this.clockTime = clockTime;
	}

	/** 
	* 开始时间
	* @return
	*/
	public String getClockTime() {
		return clockTime;
	}

	/** 
	* 重复类型
	* @param clockRepType
	*/
	public void setClockRepType(String clockRepType) {
		this.clockRepType = clockRepType;
	}

	/** 
	* 重复类型
	* @return
	*/
	public String getClockRepType() {
		return clockRepType;
	}

	/** 
	* 重复时间
	* @param clockRepDate
	*/
	public void setClockRepDate(String clockRepDate) {
		this.clockRepDate = clockRepDate;
	}

	/** 
	* 重复时间
	* @return
	*/
	public String getClockRepDate() {
		return clockRepDate;
	}

	/** 
	* 是否开启 0未开启 1已开启
	* @param clockIsOn
	*/
	public void setClockIsOn(String clockIsOn) {
		this.clockIsOn = clockIsOn;
	}

	/** 
	* 是否开启 0未开启 1已开启
	* @return
	*/
	public String getClockIsOn() {
		return clockIsOn;
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

	/** 
	* 提醒方式
	* @return
	*/
	public List<ClockWay> getListClockWay() {
		return listClockWay;
	}

	/** 
	* 提醒方式
	* @param listClockWay
	*/
	public void setListClockWay(List<ClockWay> listClockWay) {
		this.listClockWay = listClockWay;
	}

	/** 
	* 执行情况 0未执行 1待执行 2 已执行
	* @param executeState
	*/
	public void setExecuteState(String executeState) {
		this.executeState = executeState;
	}

	/** 
	* 执行情况 0未执行 1待执行 2 已执行
	* @return
	*/
	public String getExecuteState() {
		return executeState;
	}

	/** 
	* 是否过期
	* @return
	*/
	public String getClockState() {
		return clockState;
	}

	/** 
	* 是否过期
	* @param clockState
	*/
	public void setClockState(String clockState) {
		this.clockState = clockState;
	}

	/** 
	* 查询时间起
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询时间起
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询时间止
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询时间止
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 模块名称
	* @return
	*/
	public String getEntyName() {
		return entyName;
	}

	/** 
	* 模块名称
	* @param entyName
	*/
	public void setEntyName(String entyName) {
		this.entyName = entyName;
	}

	/** 
	* 闹铃消息类型 0普通消息 1待办事项
	* @param clockMsgType
	*/
	public void setClockMsgType(String clockMsgType) {
		this.clockMsgType = clockMsgType;
	}

	/** 
	* 闹铃消息类型 0普通消息 1待办事项
	* @return
	*/
	public String getClockMsgType() {
		return clockMsgType;
	}

	/** 
	* 提醒对象
	* @return
	*/
	public List<ClockPerson> getClockPersons() {
		return clockPersons;
	}

	/** 
	* 提醒对象
	* @param clockPersons
	*/
	public void setClockPersons(List<ClockPerson> clockPersons) {
		this.clockPersons = clockPersons;
	}
}
