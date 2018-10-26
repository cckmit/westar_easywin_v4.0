package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 分享内容取值
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DailyVal {
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
	* 分享内容要求
	*/
	@Filed
	private Integer questionId;
	/** 
	* 分享主键
	*/
	@Filed
	private Integer dailyId;
	/** 
	* 分享值
	*/
	@Filed
	private String dailyValue;

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
	* 分享内容要求
	* @param questionId
	*/
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	/** 
	* 分享内容要求
	* @return
	*/
	public Integer getQuestionId() {
		return questionId;
	}

	/** 
	* 分享主键
	* @param dailyId
	*/
	public void setDailyId(Integer dailyId) {
		this.dailyId = dailyId;
	}

	/** 
	* 分享主键
	* @return
	*/
	public Integer getDailyId() {
		return dailyId;
	}

	/** 
	* 分享值
	* @param dailyValue
	*/
	public void setDailyValue(String dailyValue) {
		this.dailyValue = dailyValue;
	}

	/** 
	* 分享值
	* @return
	*/
	public String getDailyValue() {
		return dailyValue;
	}
}
