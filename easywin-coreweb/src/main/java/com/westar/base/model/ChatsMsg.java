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
public class ChatsMsg {
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
	* 聊天内容
	*/
	@Filed
	private String content;
	/** 
	* 聊天发送人员主键
	*/
	@Filed
	private Integer msgFrom;
	/** 
	* 聊天接收对象主键
	*/
	@Filed
	private Integer msgTo;

	/****************以上主要为系统表字段********************/
	/** 
	* 发言人性别
	*/
	private String gender;
	/** 
	* 发言人名字
	*/
	private String userName;
	/** 
	* 发言人头像uuid
	*/
	private String uuid;
	/** 
	* 发言人头像文件名
	*/
	private String fileName;

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
	* 聊天内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 聊天内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 聊天发送人员主键
	* @param msgFrom
	*/
	public void setMsgFrom(Integer msgFrom) {
		this.msgFrom = msgFrom;
	}

	/** 
	* 聊天发送人员主键
	* @return
	*/
	public Integer getMsgFrom() {
		return msgFrom;
	}

	/** 
	* 聊天接收对象主键
	* @param msgTo
	*/
	public void setMsgTo(Integer msgTo) {
		this.msgTo = msgTo;
	}

	/** 
	* 聊天接收对象主键
	* @return
	*/
	public Integer getMsgTo() {
		return msgTo;
	}

	/** 
	* 发言人性别
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 发言人性别
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 发言人名字
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 发言人名字
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 发言人头像uuid
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 发言人头像uuid
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** 
	* 发言人头像文件名
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 发言人头像文件名
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
