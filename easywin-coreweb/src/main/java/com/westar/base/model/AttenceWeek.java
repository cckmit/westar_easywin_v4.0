package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 工作日设定
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AttenceWeek {
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
	* 关联attenceRule主键
	*/
	@Filed
	private Integer attenceRuleId;
	/** 
	* 关联attenceType主键
	*/
	@Filed
	private Integer attenceTypeId;
	/** 
	* 工作日
	*/
	@Filed
	private Integer weekDay;

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
	* 关联attenceRule主键
	* @param attenceRuleId
	*/
	public void setAttenceRuleId(Integer attenceRuleId) {
		this.attenceRuleId = attenceRuleId;
	}

	/** 
	* 关联attenceRule主键
	* @return
	*/
	public Integer getAttenceRuleId() {
		return attenceRuleId;
	}

	/** 
	* 关联attenceType主键
	* @param attenceTypeId
	*/
	public void setAttenceTypeId(Integer attenceTypeId) {
		this.attenceTypeId = attenceTypeId;
	}

	/** 
	* 关联attenceType主键
	* @return
	*/
	public Integer getAttenceTypeId() {
		return attenceTypeId;
	}

	/** 
	* 工作日
	* @param weekDay
	*/
	public void setWeekDay(Integer weekDay) {
		this.weekDay = weekDay;
	}

	/** 
	* 工作日
	* @return
	*/
	public Integer getWeekDay() {
		return weekDay;
	}
}
