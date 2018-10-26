package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 聊天室
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ChatsRoom {
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
	* 聊天类型 1群聊 0单聊
	*/
	@Filed
	private String chatType;
	/** 
	* 创建人员
	*/
	@Filed
	private Integer creater;

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
	* 聊天类型 1群聊 0单聊
	* @param chatType
	*/
	public void setChatType(String chatType) {
		this.chatType = chatType;
	}

	/** 
	* 聊天类型 1群聊 0单聊
	* @return
	*/
	public String getChatType() {
		return chatType;
	}

	/** 
	* 创建人员
	* @param creater
	*/
	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	/** 
	* 创建人员
	* @return
	*/
	public Integer getCreater() {
		return creater;
	}
}
