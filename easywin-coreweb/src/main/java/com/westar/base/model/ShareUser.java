package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 信息分享人员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ShareUser {
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
	* 分享信息主键
	*/
	@Filed
	private Integer msgId;
	/** 
	* 分享人员主键
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 分享人员名称
	*/
	private String userName;

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
	* 分享信息主键
	* @param msgId
	*/
	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	/** 
	* 分享信息主键
	* @return
	*/
	public Integer getMsgId() {
		return msgId;
	}

	/** 
	* 分享人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 分享人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 分享人员名称
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 分享人员名称
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
