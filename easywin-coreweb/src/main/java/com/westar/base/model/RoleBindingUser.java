package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 角色人员表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class RoleBindingUser {
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
	* 角色id
	*/
	@Filed
	private Integer roleId;
	/** 
	* 模块
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 人员姓名
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
	* 角色id
	* @param roleId
	*/
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	/** 
	* 角色id
	* @return
	*/
	public Integer getRoleId() {
		return roleId;
	}

	/** 
	* 模块
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 模块
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 人员姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 人员姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
