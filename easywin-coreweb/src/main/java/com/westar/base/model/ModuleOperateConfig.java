package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 业务模块配置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ModuleOperateConfig {
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
	* 强制参与类型，列值于BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String moduleType;
	/** 
	* 操作类型
	*/
	@Filed
	private String operateType;
	/** 
	* 启用状态 ;0是关闭；1是启用
	*/
	@Filed
	private Integer enabled;

	/****************以上主要为系统表字段********************/
	/** 
	* 操作类型数组
	*/
	private String[] opTypes;
	/** 
	* 操作类型名称
	*/
	private String moduleTypeName;

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
	* 强制参与类型，列值于BusinessTypeConstant常量一一对应
	* @param moduleType
	*/
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	/** 
	* 强制参与类型，列值于BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getModuleType() {
		return moduleType;
	}

	/** 
	* 操作类型
	* @param operateType
	*/
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	/** 
	* 操作类型
	* @return
	*/
	public String getOperateType() {
		return operateType;
	}

	/** 
	* 启用状态 ;0是关闭；1是启用
	* @param enabled
	*/
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	/** 
	* 启用状态 ;0是关闭；1是启用
	* @return
	*/
	public Integer getEnabled() {
		return enabled;
	}

	/** 
	* 操作类型数组
	* @return
	*/
	public String[] getOpTypes() {
		return opTypes;
	}

	/** 
	* 操作类型数组
	* @param opTypes
	*/
	public void setOpTypes(String[] opTypes) {
		this.opTypes = opTypes;
	}

	/** 
	* 操作类型名称
	* @return
	*/
	public String getModuleTypeName() {
		return moduleTypeName;
	}

	/** 
	* 操作类型名称
	* @param moduleTypeName
	*/
	public void setModuleTypeName(String moduleTypeName) {
		this.moduleTypeName = moduleTypeName;
	}
}
