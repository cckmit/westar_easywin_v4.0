package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 直接销账表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FeeDirectBalance {
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
	* 申请记录主键
	*/
	@Filed
	private Integer feeBudgetId;
	/** 
	* 销账人
	*/
	@Filed
	private Integer creator;
	/** 
	* 销账说明
	*/
	@Filed
	private String content;

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
	* 申请记录主键
	* @param feeBudgetId
	*/
	public void setFeeBudgetId(Integer feeBudgetId) {
		this.feeBudgetId = feeBudgetId;
	}

	/** 
	* 申请记录主键
	* @return
	*/
	public Integer getFeeBudgetId() {
		return feeBudgetId;
	}

	/** 
	* 销账人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 销账人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 销账说明
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 销账说明
	* @return
	*/
	public String getContent() {
		return content;
	}
}
