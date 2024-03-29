package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 周报模板
 */
@Table
@JsonInclude(Include.NON_NULL)
public class WeekReportMod {
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
	* 创建人
	*/
	@Filed
	private Integer cereaterId;
	/** 
	* 模板名称
	*/
	@Filed
	private String modName;
	/** 
	* 是否填写下周计划1是0否
	*/
	@Filed
	private String hasPlan;
	/** 
	* 模板版本
	*/
	@Filed
	private Integer version;

	/****************以上主要为系统表字段********************/
	/** 
	* 团队级要求
	*/
	private List<WeekRepModContent> contentTeamLevs;
	/** 
	* 部门级要求
	*/
	private List<WeekRepModContent> contentGroupLevs;
	/** 
	* 成员级要求
	*/
	private List<WeekRepModContent> contentMemLevs;

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
	* 创建人
	* @param cereaterId
	*/
	public void setCereaterId(Integer cereaterId) {
		this.cereaterId = cereaterId;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getCereaterId() {
		return cereaterId;
	}

	/** 
	* 团队级要求
	* @return
	*/
	public List<WeekRepModContent> getContentTeamLevs() {
		return contentTeamLevs;
	}

	/** 
	* 团队级要求
	* @param contentTeamLevs
	*/
	public void setContentTeamLevs(List<WeekRepModContent> contentTeamLevs) {
		this.contentTeamLevs = contentTeamLevs;
	}

	/** 
	* 部门级要求
	* @return
	*/
	public List<WeekRepModContent> getContentGroupLevs() {
		return contentGroupLevs;
	}

	/** 
	* 部门级要求
	* @param contentGroupLevs
	*/
	public void setContentGroupLevs(List<WeekRepModContent> contentGroupLevs) {
		this.contentGroupLevs = contentGroupLevs;
	}

	/** 
	* 成员级要求
	* @return
	*/
	public List<WeekRepModContent> getContentMemLevs() {
		return contentMemLevs;
	}

	/** 
	* 成员级要求
	* @param contentMemLevs
	*/
	public void setContentMemLevs(List<WeekRepModContent> contentMemLevs) {
		this.contentMemLevs = contentMemLevs;
	}

	/** 
	* 模板名称
	* @param modName
	*/
	public void setModName(String modName) {
		this.modName = modName;
	}

	/** 
	* 模板名称
	* @return
	*/
	public String getModName() {
		return modName;
	}

	/** 
	* 是否填写下周计划1是0否
	* @param hasPlan
	*/
	public void setHasPlan(String hasPlan) {
		this.hasPlan = hasPlan;
	}

	/** 
	* 是否填写下周计划1是0否
	* @return
	*/
	public String getHasPlan() {
		return hasPlan;
	}

	/** 
	* 模板版本
	* @param version
	*/
	public void setVersion(Integer version) {
		this.version = version;
	}

	/** 
	* 模板版本
	* @return
	*/
	public Integer getVersion() {
		return version;
	}
}
