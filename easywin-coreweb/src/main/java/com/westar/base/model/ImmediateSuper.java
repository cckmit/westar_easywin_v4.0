package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 个人直属上司设定表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ImmediateSuper {
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
	* 设定人主键
	*/
	@Filed
	private Integer creator;
	/** 
	* 直属领导主键
	*/
	@Filed
	private Integer leader;

	/****************以上主要为系统表字段********************/
	/** 
	* 直属上级集合
	*/
	private List<UserInfo> listUserInfo;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 附件名称
	*/
	private String fileName;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 直属上级名称
	*/
	private String leaderName;

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
	* 设定人主键
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 设定人主键
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 直属领导主键
	* @param leader
	*/
	public void setLeader(Integer leader) {
		this.leader = leader;
	}

	/** 
	* 直属领导主键
	* @return
	*/
	public Integer getLeader() {
		return leader;
	}

	/** 
	* 直属上级集合
	* @return
	*/
	public List<UserInfo> getListUserInfo() {
		return listUserInfo;
	}

	/** 
	* 直属上级集合
	* @param listUserInfo
	*/
	public void setListUserInfo(List<UserInfo> listUserInfo) {
		this.listUserInfo = listUserInfo;
	}

	public boolean isSucc() {
		return succ;
	}

	/** 
	* boolean标识
	* @param succ
	*/
	public void setSucc(boolean succ) {
		this.succ = succ;
	}

	/** 
	* 提示信息
	* @return
	*/
	public String getPromptMsg() {
		return promptMsg;
	}

	/** 
	* 提示信息
	* @param promptMsg
	*/
	public void setPromptMsg(String promptMsg) {
		this.promptMsg = promptMsg;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 附件名称
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	* 附件UUID
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 附件UUID
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 0女1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 直属上级名称
	* @return
	*/
	public String getLeaderName() {
		return leaderName;
	}

	/** 
	* 直属上级名称
	* @param leaderName
	*/
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
}
