package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 模块引导
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Intro {
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
	* 模块类型
	*/
	@Filed
	private String busType;
	/** 
	* 引导状态0已完成引导
	*/
	@Filed
	private Integer introState;
	/** 
	* 用户主键
	*/
	@Filed
	private Integer userId;

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
	* 模块类型
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 模块类型
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 引导状态0已完成引导
	* @param introState
	*/
	public void setIntroState(Integer introState) {
		this.introState = introState;
	}

	/** 
	* 引导状态0已完成引导
	* @return
	*/
	public Integer getIntroState() {
		return introState;
	}

	/** 
	* 用户主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 用户主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}
}
