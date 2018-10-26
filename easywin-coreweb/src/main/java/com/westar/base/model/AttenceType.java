package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 工作日类型
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AttenceType {
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
	* 周数类型 -1不考虑周数 0双周 1单周
	*/
	@Filed
	private Integer weekType;

	/****************以上主要为系统表字段********************/
	/** 
	* 工作日设定
	*/
	private List<AttenceWeek> listAttenceWeeks;
	/** 
	* 工作时段设定
	*/
	private List<AttenceTime> listAttenceTimes;

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
	* 周数类型 -1不考虑周数 0双周 1单周
	* @param weekType
	*/
	public void setWeekType(Integer weekType) {
		this.weekType = weekType;
	}

	/** 
	* 周数类型 -1不考虑周数 0双周 1单周
	* @return
	*/
	public Integer getWeekType() {
		return weekType;
	}

	/** 
	* 工作日设定
	* @return
	*/
	public List<AttenceWeek> getListAttenceWeeks() {
		return listAttenceWeeks;
	}

	/** 
	* 工作日设定
	* @param listAttenceWeeks
	*/
	public void setListAttenceWeeks(List<AttenceWeek> listAttenceWeeks) {
		this.listAttenceWeeks = listAttenceWeeks;
	}

	/** 
	* 工作时段设定
	* @return
	*/
	public List<AttenceTime> getListAttenceTimes() {
		return listAttenceTimes;
	}

	/** 
	* 工作时段设定
	* @param listAttenceTimes
	*/
	public void setListAttenceTimes(List<AttenceTime> listAttenceTimes) {
		this.listAttenceTimes = listAttenceTimes;
	}
}
