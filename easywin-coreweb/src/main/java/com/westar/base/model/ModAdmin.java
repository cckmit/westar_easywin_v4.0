package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 模块管理员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ModAdmin {
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
	* 模块代码
	*/
	@Filed
	private String busType;
	/** 
	* 关联userInfo主键
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 管理员名称
	*/
	private String adminName;
	/** 
	* 管理员性别
	*/
	private String adminGender;
	/** 
	* 管理员头像uuid
	*/
	private String adminUuid;
	/** 
	* 管理员头像名称
	*/
	private String adminImgName;

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
	* 模块代码
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 模块代码
	* @return
	*/
	public String getBusType() {
		return busType;
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
	* 管理员名称
	* @return
	*/
	public String getAdminName() {
		return adminName;
	}

	/** 
	* 管理员名称
	* @param adminName
	*/
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	/** 
	* 管理员性别
	* @return
	*/
	public String getAdminGender() {
		return adminGender;
	}

	/** 
	* 管理员性别
	* @param adminGender
	*/
	public void setAdminGender(String adminGender) {
		this.adminGender = adminGender;
	}

	/** 
	* 管理员头像uuid
	* @return
	*/
	public String getAdminUuid() {
		return adminUuid;
	}

	/** 
	* 管理员头像uuid
	* @param adminUuid
	*/
	public void setAdminUuid(String adminUuid) {
		this.adminUuid = adminUuid;
	}

	/** 
	* 管理员头像名称
	* @return
	*/
	public String getAdminImgName() {
		return adminImgName;
	}

	/** 
	* 管理员头像名称
	* @param adminImgName
	*/
	public void setAdminImgName(String adminImgName) {
		this.adminImgName = adminImgName;
	}
}
