package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 积分历史
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JifenHistory {
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
	* 积分人主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 积分项主键
	*/
	@Filed
	private Integer configId;
	/** 
	* 积分变化
	*/
	@Filed
	private Integer jifenChange;
	/** 
	* 总积分
	*/
	@Filed
	private Integer allScore;
	/** 
	* 积分描述，针对主表
	*/
	@Filed
	private String content;
	/** 
	* 模块主键，暂时只针对任务委托
	*/
	@Filed
	private Integer modId;

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
	* 积分人主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 积分人主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 积分项主键
	* @param configId
	*/
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	/** 
	* 积分项主键
	* @return
	*/
	public Integer getConfigId() {
		return configId;
	}

	/** 
	* 积分变化
	* @param jifenChange
	*/
	public void setJifenChange(Integer jifenChange) {
		this.jifenChange = jifenChange;
	}

	/** 
	* 积分变化
	* @return
	*/
	public Integer getJifenChange() {
		return jifenChange;
	}

	/** 
	* 总积分
	* @param allScore
	*/
	public void setAllScore(Integer allScore) {
		this.allScore = allScore;
	}

	/** 
	* 总积分
	* @return
	*/
	public Integer getAllScore() {
		return allScore;
	}

	/** 
	* 积分描述，针对主表
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 积分描述，针对主表
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 模块主键，暂时只针对任务委托
	* @param modId
	*/
	public void setModId(Integer modId) {
		this.modId = modId;
	}

	/** 
	* 模块主键，暂时只针对任务委托
	* @return
	*/
	public Integer getModId() {
		return modId;
	}
}
