package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 考勤规则设定
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AttenceRule {
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
	* 规则名称
	*/
	@Filed
	private String ruleName;
	/** 
	* 规则类型 dataDic
	*/
	@Filed
	private String ruleType;
	/** 
	* 默认是系统 0不是 1是
	*/
	@Filed
	private Integer isSystem;

	/****************以上主要为系统表字段********************/
	/** 
	* 工作日类型（考虑到单双周）
	*/
	private List<AttenceType> listAttenceTypes;

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
	* 规则名称
	* @param ruleName
	*/
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/** 
	* 规则名称
	* @return
	*/
	public String getRuleName() {
		return ruleName;
	}

	/** 
	* 规则类型 dataDic
	* @param ruleType
	*/
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	/** 
	* 规则类型 dataDic
	* @return
	*/
	public String getRuleType() {
		return ruleType;
	}

	/** 
	* 默认是系统 0不是 1是
	* @param isSystem
	*/
	public void setIsSystem(Integer isSystem) {
		this.isSystem = isSystem;
	}

	/** 
	* 默认是系统 0不是 1是
	* @return
	*/
	public Integer getIsSystem() {
		return isSystem;
	}

	/** 
	* 工作日类型（考虑到单双周）
	* @return
	*/
	public List<AttenceType> getListAttenceTypes() {
		return listAttenceTypes;
	}

	/** 
	* 工作日类型（考虑到单双周）
	* @param listAttenceTypes
	*/
	public void setListAttenceTypes(List<AttenceType> listAttenceTypes) {
		this.listAttenceTypes = listAttenceTypes;
	}
}
