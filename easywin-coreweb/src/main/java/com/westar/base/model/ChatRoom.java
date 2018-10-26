package com.westar.base.model;

import java.util.List;
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
public class ChatRoom {
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
	* 模块类型 见系统常量
	*/
	@Filed
	private String busType;
	/** 
	* 创建人员
	*/
	@Filed
	private Integer creater;
	/** 
	* 聊天成员主键集合
	*/
	@Filed
	private String chater;
	/** 
	* 聊天类型 1群聊 0单聊
	*/
	@Filed
	private String chatType;

	/****************以上主要为系统表字段********************/
	/** 
	* 聊天成员
	*/
	List<ChatUser> chaters;
	/** 
	* 个人消息未读数
	*/
	private Integer noReadNum;

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
	* 聊天成员主键集合
	* @param chater
	*/
	public void setChater(String chater) {
		this.chater = chater;
	}

	/** 
	* 聊天成员主键集合
	* @return
	*/
	public String getChater() {
		return chater;
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
	* 聊天成员
	* @return
	*/
	public List<ChatUser> getChaters() {
		return chaters;
	}

	/** 
	* 聊天成员
	* @param chaters
	*/
	public void setChaters(List<ChatUser> chaters) {
		this.chaters = chaters;
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

	/** 
	* 个人消息未读数
	* @return
	*/
	public Integer getNoReadNum() {
		return noReadNum;
	}

	/** 
	* 个人消息未读数
	* @param noReadNum
	*/
	public void setNoReadNum(Integer noReadNum) {
		this.noReadNum = noReadNum;
	}
}
