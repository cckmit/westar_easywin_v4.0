package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 模块最新动态
 */
@Table
@JsonInclude(Include.NON_NULL)
public class NewsInfo {
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
	* 模块主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 模块类型 见系统常量
	*/
	@Filed
	private String busType;
	/** 
	* 更新内容
	*/
	@Filed
	private String content;
	/** 
	* 更新人
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/

	/****************以上为自己添加字段********************/
	public NewsInfo() {
	}

	public NewsInfo(Integer comId, Integer busId, String busType, String content, Integer userId) {
		super();
		this.comId = comId;
		this.busId = busId;
		this.busType = busType;
		this.content = content;
		this.userId = userId;
	}

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
	* 模块主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 模块主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 模块类型 见系统常量
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 模块类型 见系统常量
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 更新内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 更新内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 更新人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 更新人
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}
}
