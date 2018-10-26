package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 聊天内容
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ChatMsg {
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
	* 聊天内容
	*/
	@Filed
	private String chatContent;
	/** 
	* 是否已生成日志
	*/
	@Filed
	private Integer isLogged;

	/****************以上主要为系统表字段********************/
	public ChatMsg() {
	}

	public ChatMsg(Integer comId, Integer roomId, Integer userId, String chatContent, Integer isLogged) {
		super();
		this.comId = comId;
		this.roomId = roomId;
		this.userId = userId;
		this.chatContent = chatContent;
		this.isLogged = isLogged;
	}

	/** 
	* 成员名称
	*/
	private String userName;
	/** 
	* 是否为今天的数据1是，0不是
	*/
	private String isToday;

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
	* 聊天内容
	* @param chatContent
	*/
	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}

	/** 
	* 聊天内容
	* @return
	*/
	public String getChatContent() {
		return chatContent;
	}

	/** 
	* 成员名称
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 成员名称
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 是否为今天的数据1是，0不是
	* @return
	*/
	public String getIsToday() {
		return isToday;
	}

	/** 
	* 是否为今天的数据1是，0不是
	* @param isToday
	*/
	public void setIsToday(String isToday) {
		this.isToday = isToday;
	}

	/** 
	* 是否已生成日志
	* @param isLogged
	*/
	public void setIsLogged(Integer isLogged) {
		this.isLogged = isLogged;
	}

	/** 
	* 是否已生成日志
	* @return
	*/
	public Integer getIsLogged() {
		return isLogged;
	}
}
