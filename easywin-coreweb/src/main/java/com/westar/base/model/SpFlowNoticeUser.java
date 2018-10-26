package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 流程的告知人员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowNoticeUser {
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
	* 模块主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 模块类型
	*/
	@Filed
	private String busType;
	/** 
	* 人员主键
	*/
	@Filed
	private Integer noticeUserId;

	/****************以上主要为系统表字段********************/
	/** 
	* 人员名称
	*/
	private String noticeUserName;

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
	* 模块类型
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 模块类型
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 人员主键
	* @param noticeUserId
	*/
	public void setNoticeUserId(Integer noticeUserId) {
		this.noticeUserId = noticeUserId;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getNoticeUserId() {
		return noticeUserId;
	}

	/** 
	* 人员名称
	* @return
	*/
	public String getNoticeUserName() {
		return noticeUserName;
	}

	/** 
	* 人员名称
	* @param noticeUserName
	*/
	public void setNoticeUserName(String noticeUserName) {
		this.noticeUserName = noticeUserName;
	}
}
