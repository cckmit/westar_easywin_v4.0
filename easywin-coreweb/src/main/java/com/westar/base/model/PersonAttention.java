package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 个人关注人员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class PersonAttention {
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
	* 创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 关联userInfo主键
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 关注人姓名
	*/
	private String userName;
	/** 
	* 关注人手机
	*/
	private String movePhone;
	/** 
	* 关注人email
	*/
	private String email;
	/** 
	* 关注人部门
	*/
	private String depName;
	/** 
	* 关注人职务
	*/
	private String job;
	/** 
	* 关注人部门ID
	*/
	private Integer depId;

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
	* 创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 关联userInfo主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 关联userInfo主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 关注人姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 关注人姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 关注人手机
	* @return
	*/
	public String getMovePhone() {
		return movePhone;
	}

	/** 
	* 关注人手机
	* @param movePhone
	*/
	public void setMovePhone(String movePhone) {
		this.movePhone = movePhone;
	}

	/** 
	* 关注人email
	* @return
	*/
	public String getEmail() {
		return email;
	}

	/** 
	* 关注人email
	* @param email
	*/
	public void setEmail(String email) {
		this.email = email;
	}

	/** 
	* 关注人部门
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 关注人部门
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/** 
	* 关注人职务
	* @return
	*/
	public String getJob() {
		return job;
	}

	/** 
	* 关注人职务
	* @param job
	*/
	public void setJob(String job) {
		this.job = job;
	}

	/** 
	* 关注人部门ID
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 关注人部门ID
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
	}
}
