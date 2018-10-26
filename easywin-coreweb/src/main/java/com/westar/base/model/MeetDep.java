package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 与会部门
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MeetDep {
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
	* 会议主键
	*/
	@Filed
	private Integer meetingId;
	/** 
	* 与会部门主键
	*/
	@Filed
	private Integer depId;

	/****************以上主要为系统表字段********************/
	/** 
	* 部门名称
	*/
	private String depName;

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
	* 会议主键
	* @param meetingId
	*/
	public void setMeetingId(Integer meetingId) {
		this.meetingId = meetingId;
	}

	/** 
	* 会议主键
	* @return
	*/
	public Integer getMeetingId() {
		return meetingId;
	}

	/** 
	* 与会部门主键
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	/** 
	* 与会部门主键
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 部门名称
	* @return
	*/
	public final String getDepName() {
		return depName;
	}

	/** 
	* 部门名称
	* @param depName
	*/
	public final void setDepName(String depName) {
		this.depName = depName;
	}
}
