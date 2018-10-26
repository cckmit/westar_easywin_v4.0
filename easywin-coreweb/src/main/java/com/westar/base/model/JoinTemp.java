package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 加入申请
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JoinTemp {
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
	* 使用账号
	*/
	@Filed
	private String account;
	/** 
	* 用户名称
	*/
	@Filed
	private String userName;
	/** 
	* 密码
	*/
	@Filed
	private String passwd;
	/** 
	* 加入方式 -1未操作 0注册 1申请 2 邀请
	*/
	@Filed
	private Integer joinType;

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
	* 使用账号
	* @param account
	*/
	public void setAccount(String account) {
		this.account = account;
	}

	/** 
	* 使用账号
	* @return
	*/
	public String getAccount() {
		return account;
	}

	/** 
	* 用户名称
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 用户名称
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 密码
	* @param passwd
	*/
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/** 
	* 密码
	* @return
	*/
	public String getPasswd() {
		return passwd;
	}

	/** 
	* 加入方式 -1未操作 0注册 1申请 2 邀请
	* @param joinType
	*/
	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}

	/** 
	* 加入方式 -1未操作 0注册 1申请 2 邀请
	* @return
	*/
	public Integer getJoinType() {
		return joinType;
	}
}
