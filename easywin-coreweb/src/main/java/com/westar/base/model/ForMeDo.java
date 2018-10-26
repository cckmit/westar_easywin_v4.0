package com.westar.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;

/** 
 * 替岗人员设定表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ForMeDo {
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
	* 替岗人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 离岗人员主键
	*/
	@Filed
	private Integer creator;

	/****************以上主要为系统表字段********************/
	/** 
	* 创建人名称
	*/
	private String creatorName;
	/** 
	* 创建人所在部门主键
	*/
	private Integer creatorDepId;
	/** 
	* 代理人姓名
	*/
	private String userName;
	/** 
	* 代理人所在部门主键
	*/
	private Integer userDepId;

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
	* 替岗人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 替岗人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 离岗人员主键
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 离岗人员主键
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 创建人名称
	* @return
	*/
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 创建人名称
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 创建人所在部门主键
	* @return
	*/
	public Integer getCreatorDepId() {
		return creatorDepId;
	}

	/** 
	* 创建人所在部门主键
	* @param creatorDepId
	*/
	public void setCreatorDepId(Integer creatorDepId) {
		this.creatorDepId = creatorDepId;
	}

	/** 
	* 代理人姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 代理人姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 代理人所在部门主键
	* @return
	*/
	public Integer getUserDepId() {
		return userDepId;
	}

	/** 
	* 代理人所在部门主键
	* @param userDepId
	*/
	public void setUserDepId(Integer userDepId) {
		this.userDepId = userDepId;
	}
}
