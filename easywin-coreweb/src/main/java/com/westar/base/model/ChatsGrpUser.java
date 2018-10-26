package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 聊天成员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ChatsGrpUser {
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
	* 聊天群
	*/
	@Filed
	private Integer chatsGrpId;
	/** 
	* 群组成员
	*/
	@Filed
	private Integer memberId;

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
	* 聊天群
	* @param chatsGrpId
	*/
	public void setChatsGrpId(Integer chatsGrpId) {
		this.chatsGrpId = chatsGrpId;
	}

	/** 
	* 聊天群
	* @return
	*/
	public Integer getChatsGrpId() {
		return chatsGrpId;
	}

	/** 
	* 群组成员
	* @param memberId
	*/
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	/** 
	* 群组成员
	* @return
	*/
	public Integer getMemberId() {
		return memberId;
	}
}
