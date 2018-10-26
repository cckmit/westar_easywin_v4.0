package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 模块属性变更申请
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ModuleChangeApply {
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
	* 模块区分，列值于BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String moduleType;
	/** 
	* 关联模块主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 变更人ID
	*/
	@Filed
	private Integer creator;
	/** 
	* 对应需要审批字段
	*/
	@Filed
	private String field;
	/** 
	* 变更前字段值
	*/
	@Filed
	private String oldValue;
	/** 
	* 申请变更后字段值
	*/
	@Filed
	private String newValue;
	/** 
	* 申请原因、说明
	*/
	@Filed
	private String content;
	/** 
	* 0-不同意，1-同意
	*/
	@Filed
	private Integer status;
	/** 
	* 审批意见
	*/
	@Filed
	private String spOpinion;
	/** 
	* 关联模块名
	*/
	@Filed
	private String busName;

	/****************以上主要为系统表字段********************/
	/** 
	* 创建人姓名
	*/
	private String creatorName;
	/** 
	* 变更前的名字
	*/
	private String oldName;
	/** 
	* 变更后名字
	*/
	private String newName;
	/** 
	* 是否短信通知 1是
	*/
	private String usePhone;

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
	* 模块区分，列值于BusinessTypeConstant常量一一对应
	* @param moduleType
	*/
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	/** 
	* 模块区分，列值于BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getModuleType() {
		return moduleType;
	}

	/** 
	* 关联模块主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 关联模块主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 变更人ID
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 变更人ID
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 对应需要审批字段
	* @param field
	*/
	public void setField(String field) {
		this.field = field;
	}

	/** 
	* 对应需要审批字段
	* @return
	*/
	public String getField() {
		return field;
	}

	/** 
	* 变更前字段值
	* @param oldValue
	*/
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	/** 
	* 变更前字段值
	* @return
	*/
	public String getOldValue() {
		return oldValue;
	}

	/** 
	* 申请变更后字段值
	* @param newValue
	*/
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	/** 
	* 申请变更后字段值
	* @return
	*/
	public String getNewValue() {
		return newValue;
	}

	/** 
	* 申请原因、说明
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 申请原因、说明
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 0-不同意，1-同意
	* @param status
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	* 0-不同意，1-同意
	* @return
	*/
	public Integer getStatus() {
		return status;
	}

	/** 
	* 审批意见
	* @param spOpinion
	*/
	public void setSpOpinion(String spOpinion) {
		this.spOpinion = spOpinion;
	}

	/** 
	* 审批意见
	* @return
	*/
	public String getSpOpinion() {
		return spOpinion;
	}

	/** 
	* 创建人姓名
	* @return
	*/
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 创建人姓名
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 关联模块名
	* @return
	*/
	public String getBusName() {
		return busName;
	}

	/** 
	* 关联模块名
	* @param busName
	*/
	public void setBusName(String busName) {
		this.busName = busName;
	}

	/** 
	* 变更前的名字
	* @return
	*/
	public String getOldName() {
		return oldName;
	}

	/** 
	* 变更前的名字
	* @param oldName
	*/
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	/** 
	* 变更后名字
	* @return
	*/
	public String getNewName() {
		return newName;
	}

	/** 
	* 变更后名字
	* @param newName
	*/
	public void setNewName(String newName) {
		this.newName = newName;
	}

	/** 
	* 是否短信通知 1是
	* @return
	*/
	public String getUsePhone() {
		return usePhone;
	}

	/** 
	* 是否短信通知 1是
	* @param usePhone
	*/
	public void setUsePhone(String usePhone) {
		this.usePhone = usePhone;
	}
}
