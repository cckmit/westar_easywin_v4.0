package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 积分等级设置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JifenLev {
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
	* 等级名称
	*/
	@Filed
	private String levName;
	/** 
	* 等级最小的分值
	*/
	@Filed
	private Integer levMinScore;

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
	* 等级名称
	* @param levName
	*/
	public void setLevName(String levName) {
		this.levName = levName;
	}

	/** 
	* 等级名称
	* @return
	*/
	public String getLevName() {
		return levName;
	}

	/** 
	* 等级最小的分值
	* @param levMinScore
	*/
	public void setLevMinScore(Integer levMinScore) {
		this.levMinScore = levMinScore;
	}

	/** 
	* 等级最小的分值
	* @return
	*/
	public Integer getLevMinScore() {
		return levMinScore;
	}
}
