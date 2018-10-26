package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 日程关联模块
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ScheRelateMod {
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
	* 日程主键
	*/
	@Filed
	private Integer scheduleId;
	/** 
	* 模块主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 模块类型
	*/
	@Filed
	private String busType;

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
	* 日程主键
	* @param scheduleId
	*/
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	/** 
	* 日程主键
	* @return
	*/
	public Integer getScheduleId() {
		return scheduleId;
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
}
