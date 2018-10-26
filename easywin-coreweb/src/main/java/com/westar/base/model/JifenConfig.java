package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 积分项设置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JifenConfig {
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
	* 积分项类别
	*/
	@Filed
	private String title;
	/** 
	* 积分项代码
	*/
	@Filed
	private String jifenCode;
	/** 
	* 类别分值
	*/
	@Filed
	private Integer typeScore;
	/** 
	* 每日分值封顶
	*/
	@Filed
	private Integer dayMaxScore;
	/** 
	* 系统分值封顶
	*/
	@Filed
	private Integer sysMaxScore;
	/** 
	* 积分项描述
	*/
	@Filed
	private String content;
	/** 
	* 积分项排序
	*/
	@Filed
	private Integer orderNo;
	/** 
	* 积分项类别（系统任务，日常任务，无限制任务）
	*/
	@Filed
	private String type;

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
	* 积分项类别
	* @param title
	*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** 
	* 积分项类别
	* @return
	*/
	public String getTitle() {
		return title;
	}

	/** 
	* 积分项代码
	* @param jifenCode
	*/
	public void setJifenCode(String jifenCode) {
		this.jifenCode = jifenCode;
	}

	/** 
	* 积分项代码
	* @return
	*/
	public String getJifenCode() {
		return jifenCode;
	}

	/** 
	* 类别分值
	* @param typeScore
	*/
	public void setTypeScore(Integer typeScore) {
		this.typeScore = typeScore;
	}

	/** 
	* 类别分值
	* @return
	*/
	public Integer getTypeScore() {
		return typeScore;
	}

	/** 
	* 每日分值封顶
	* @param dayMaxScore
	*/
	public void setDayMaxScore(Integer dayMaxScore) {
		this.dayMaxScore = dayMaxScore;
	}

	/** 
	* 每日分值封顶
	* @return
	*/
	public Integer getDayMaxScore() {
		return dayMaxScore;
	}

	/** 
	* 系统分值封顶
	* @param sysMaxScore
	*/
	public void setSysMaxScore(Integer sysMaxScore) {
		this.sysMaxScore = sysMaxScore;
	}

	/** 
	* 系统分值封顶
	* @return
	*/
	public Integer getSysMaxScore() {
		return sysMaxScore;
	}

	/** 
	* 积分项描述
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 积分项描述
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 积分项排序
	* @param orderNo
	*/
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/** 
	* 积分项排序
	* @return
	*/
	public Integer getOrderNo() {
		return orderNo;
	}

	/** 
	* 积分项类别（系统任务，日常任务，无限制任务）
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 积分项类别（系统任务，日常任务，无限制任务）
	* @return
	*/
	public String getType() {
		return type;
	}
}
