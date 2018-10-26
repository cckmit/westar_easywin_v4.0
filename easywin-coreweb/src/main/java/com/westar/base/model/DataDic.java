package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 系统字典表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DataDic {
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
	* 父级Id
	*/
	@Filed
	private Integer parentId;
	/** 
	* 是否可维护（标识是否是系统字典表）
	*/
	@Filed
	private String maintainable;
	/** 
	* 类型
	*/
	@Filed
	private String type;
	/** 
	* 字典代码
	*/
	@Filed
	private String code;
	/** 
	* 字典值
	*/
	@Filed
	private String zvalue;
	/** 
	* 描述
	*/
	@Filed
	private String zdescribe;

	/****************以上主要为系统表字段********************/
	private Integer level;
	private String pathZvalue;
	/** 
	* 父节点的code
	*/
	private String parentCode;

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
	* 父级Id
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 父级Id
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 是否可维护（标识是否是系统字典表）
	* @param maintainable
	*/
	public void setMaintainable(String maintainable) {
		this.maintainable = maintainable;
	}

	/** 
	* 是否可维护（标识是否是系统字典表）
	* @return
	*/
	public String getMaintainable() {
		return maintainable;
	}

	/** 
	* 类型
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 类型
	* @return
	*/
	public String getType() {
		return type;
	}

	/** 
	* 字典代码
	* @param code
	*/
	public void setCode(String code) {
		this.code = code;
	}

	/** 
	* 字典代码
	* @return
	*/
	public String getCode() {
		return code;
	}

	/** 
	* 字典值
	* @param zvalue
	*/
	public void setZvalue(String zvalue) {
		this.zvalue = zvalue;
	}

	/** 
	* 字典值
	* @return
	*/
	public String getZvalue() {
		return zvalue;
	}

	/** 
	* 描述
	* @param zdescribe
	*/
	public void setZdescribe(String zdescribe) {
		this.zdescribe = zdescribe;
	}

	/** 
	* 描述
	* @return
	*/
	public String getZdescribe() {
		return zdescribe;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getPathZvalue() {
		return pathZvalue;
	}

	public void setPathZvalue(String pathZvalue) {
		this.pathZvalue = pathZvalue;
	}

	/** 
	* 父节点的code
	* @return
	*/
	public String getParentCode() {
		return parentCode;
	}

	/** 
	* 父节点的code
	* @param parentCode
	*/
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}
