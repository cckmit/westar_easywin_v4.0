package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 聊天未读
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ChatsNoRead {
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
	* 聊天室主键
	*/
	@Filed
	private Integer roomId;
	/** 
	* 成员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 消息未读条数
	*/
	@Filed
	private Integer noReadNum;

	/****************以上主要为系统表字段********************/
	/** 
	* 用于页面初始化数据的消息发送放
	*/
	private String fromUser;
	/** 
	* 聊天类型
	*/
	private String chatType;

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
	* 聊天室主键
	* @param roomId
	*/
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	/** 
	* 聊天室主键
	* @return
	*/
	public Integer getRoomId() {
		return roomId;
	}

	/** 
	* 成员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 成员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 消息未读条数
	* @param noReadNum
	*/
	public void setNoReadNum(Integer noReadNum) {
		this.noReadNum = noReadNum;
	}

	/** 
	* 消息未读条数
	* @return
	*/
	public Integer getNoReadNum() {
		return noReadNum;
	}

	/** 
	* 用于页面初始化数据的消息发送放
	* @return
	*/
	public String getFromUser() {
		return fromUser;
	}

	/** 
	* 用于页面初始化数据的消息发送放
	* @param fromUser
	*/
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	/** 
	* 聊天类型
	* @return
	*/
	public String getChatType() {
		return chatType;
	}

	/** 
	* 聊天类型
	* @param chatType
	*/
	public void setChatType(String chatType) {
		this.chatType = chatType;
	}
}
