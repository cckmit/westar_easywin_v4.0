package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 客户最新动态
 */
@Table
@JsonInclude(Include.NON_NULL)
public class CustomerNewtestInfo {
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
	* 客户主键
	*/
	@Filed
	private Integer customerId;
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
	* 客户主键
	* @param customerId
	*/
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/** 
	* 客户主键
	* @return
	*/
	public Integer getCustomerId() {
		return customerId;
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
