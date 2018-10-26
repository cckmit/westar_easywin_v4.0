package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 周报模板内容所属部门
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ModContDep {
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
	* 模板编号
	*/
	@Filed
	private Integer modId;
	/** 
	* 模板内容主键
	*/
	@Filed
	private Integer modContId;
	/** 
	* 小组的主键 关联部门
	*/
	@Filed
	private Integer depId;
	/** 
	* 是否必填，默认不是
	*/
	@Filed
	private String isRequire;

	/****************以上主要为系统表字段********************/
	/** 
	* 部门名称
	*/
	private String depName;

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
	* 模板编号
	* @param modId
	*/
	public void setModId(Integer modId) {
		this.modId = modId;
	}

	/** 
	* 模板编号
	* @return
	*/
	public Integer getModId() {
		return modId;
	}

	/** 
	* 模板内容主键
	* @param modContId
	*/
	public void setModContId(Integer modContId) {
		this.modContId = modContId;
	}

	/** 
	* 模板内容主键
	* @return
	*/
	public Integer getModContId() {
		return modContId;
	}

	/** 
	* 小组的主键 关联部门
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	/** 
	* 小组的主键 关联部门
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 部门名称
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 部门名称
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/** 
	* 是否必填，默认不是
	* @param isRequire
	*/
	public void setIsRequire(String isRequire) {
		this.isRequire = isRequire;
	}

	/** 
	* 是否必填，默认不是
	* @return
	*/
	public String getIsRequire() {
		return isRequire;
	}
}
