package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 车辆申请人员范围
 */
@Table
@JsonInclude(Include.NON_NULL)
public class CarScopeUser {
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
	* 车辆主键
	*/
	@Filed
	private Integer carId;
	/** 
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 添加人
	*/
	@Filed
	private Integer creator;

	/****************以上主要为系统表字段********************/
	/** 
	* 名称
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
	* 车辆主键
	* @param carId
	*/
	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	/** 
	* 车辆主键
	* @return
	*/
	public Integer getCarId() {
		return carId;
	}

	/** 
	* 人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 添加人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 添加人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 名称
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 名称
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
