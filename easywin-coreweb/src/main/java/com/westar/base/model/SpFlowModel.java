package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 流程定义表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowModel {
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
	* 流程名称
	*/
	@Filed
	private String flowName;
	/** 
	* 流程命名规则
	*/
	@Filed
	private String titleRule;
	/** 
	* 流程说明
	*/
	@Filed
	private String remark;
	/** 
	* 关联表单主键
	*/
	@Filed
	private String formKey;
	/** 
	* activity部署主键；只有此字段不能null时，流程才是启用状态
	*/
	@Filed
	private String act_deployment_id;
	/** 
	* 创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 流程是否启用
	*/
	@Filed
	private Integer status;
	/** 
	* 是否部署
	*/
	@Filed
	private Integer deployed;
	/** 
	* 流程类型
	*/
	@Filed
	private Integer spFlowTypeId;
	/** 
	* 子流程主键
	*/
	@Filed
	private Integer sonFlowId;
	/** 
	* 流程模块
	*/
	@Filed
	private String flowModBusType;

	/****************以上主要为系统表字段********************/
	private String creatorName;
	/** 
	* 附件名称
	*/
	private String fileName;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 流程步骤集合
	*/
	private List<SpFlowStep> listFlowSteps;
	/** 
	* 关联表单名称
	*/
	private String formName;
	/** 
	* 分类名称
	*/
	private String spFlowTypeName;
	/** 
	* 根据部门设置流程发起范围
	*/
	private Integer[] depIds;
	/** 
	* 根据人员设置流程发起范围
	*/
	private Integer[] userIds;
	/** 
	* 查询流程根据部门设置的发起权限集合
	*/
	private List<SpFlowScopeByDep> listSpFlowScopeByDep;
	/** 
	* 查询流程根据人员设置的发起权限集合
	*/
	private List<SpFlowScopeByUser> listSpFlowScopeByUser;
	/** 
	* 列表排序规则
	*/
	private String orderBy;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 关联子流程名称
	*/
	private String sonFlowName;
	/** 
	* 主、子表单控件映射关系
	*/
	private List<FormCompon> listFlowFormCompons;
	/** 
	* 1已替换0未替换
	*/
	private Integer formState;

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
	* 流程名称
	* @param flowName
	*/
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/** 
	* 流程名称
	* @return
	*/
	public String getFlowName() {
		return flowName;
	}

	/** 
	* 流程说明
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	* 流程说明
	* @return
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 关联表单主键
	* @param formKey
	*/
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	/** 
	* 关联表单主键
	* @return
	*/
	public String getFormKey() {
		return formKey;
	}

	/** 
	* activity部署主键；只有此字段不能null时，流程才是启用状态
	* @param act_deployment_id
	*/
	public void setAct_deployment_id(String act_deployment_id) {
		this.act_deployment_id = act_deployment_id;
	}

	/** 
	* activity部署主键；只有此字段不能null时，流程才是启用状态
	* @return
	*/
	public String getAct_deployment_id() {
		return act_deployment_id;
	}

	/** 
	* 创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 附件名称
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	* 附件UUID
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 附件UUID
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 0女1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 流程步骤集合
	* @return
	*/
	public List<SpFlowStep> getListFlowSteps() {
		return listFlowSteps;
	}

	/** 
	* 流程步骤集合
	* @param listFlowSteps
	*/
	public void setListFlowSteps(List<SpFlowStep> listFlowSteps) {
		this.listFlowSteps = listFlowSteps;
	}

	/** 
	* 关联表单名称
	* @return
	*/
	public String getFormName() {
		return formName;
	}

	/** 
	* 关联表单名称
	* @param formName
	*/
	public void setFormName(String formName) {
		this.formName = formName;
	}

	/** 
	* 流程是否启用
	* @param status
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	* 流程是否启用
	* @return
	*/
	public Integer getStatus() {
		return status;
	}

	/** 
	* 是否部署
	* @param deployed
	*/
	public void setDeployed(Integer deployed) {
		this.deployed = deployed;
	}

	/** 
	* 是否部署
	* @return
	*/
	public Integer getDeployed() {
		return deployed;
	}

	/** 
	* 流程类型
	* @param spFlowTypeId
	*/
	public void setSpFlowTypeId(Integer spFlowTypeId) {
		this.spFlowTypeId = spFlowTypeId;
	}

	/** 
	* 流程类型
	* @return
	*/
	public Integer getSpFlowTypeId() {
		return spFlowTypeId;
	}

	/** 
	* 分类名称
	* @return
	*/
	public String getSpFlowTypeName() {
		return spFlowTypeName;
	}

	/** 
	* 分类名称
	* @param spFlowTypeName
	*/
	public void setSpFlowTypeName(String spFlowTypeName) {
		this.spFlowTypeName = spFlowTypeName;
	}

	/** 
	* 根据部门设置流程发起范围
	* @return
	*/
	public Integer[] getDepIds() {
		return depIds;
	}

	/** 
	* 根据部门设置流程发起范围
	* @param depIds
	*/
	public void setDepIds(Integer[] depIds) {
		this.depIds = depIds;
	}

	/** 
	* 根据人员设置流程发起范围
	* @return
	*/
	public Integer[] getUserIds() {
		return userIds;
	}

	/** 
	* 根据人员设置流程发起范围
	* @param userIds
	*/
	public void setUserIds(Integer[] userIds) {
		this.userIds = userIds;
	}

	/** 
	* 查询流程根据部门设置的发起权限集合
	* @return
	*/
	public List<SpFlowScopeByDep> getListSpFlowScopeByDep() {
		return listSpFlowScopeByDep;
	}

	/** 
	* 查询流程根据部门设置的发起权限集合
	* @param listSpFlowScopeByDep
	*/
	public void setListSpFlowScopeByDep(List<SpFlowScopeByDep> listSpFlowScopeByDep) {
		this.listSpFlowScopeByDep = listSpFlowScopeByDep;
	}

	/** 
	* 查询流程根据人员设置的发起权限集合
	* @return
	*/
	public List<SpFlowScopeByUser> getListSpFlowScopeByUser() {
		return listSpFlowScopeByUser;
	}

	/** 
	* 查询流程根据人员设置的发起权限集合
	* @param listSpFlowScopeByUser
	*/
	public void setListSpFlowScopeByUser(List<SpFlowScopeByUser> listSpFlowScopeByUser) {
		this.listSpFlowScopeByUser = listSpFlowScopeByUser;
	}

	/** 
	* 列表排序规则
	* @return
	*/
	public String getOrderBy() {
		return orderBy;
	}

	/** 
	* 列表排序规则
	* @param orderBy
	*/
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isSucc() {
		return succ;
	}

	/** 
	* boolean标识
	* @param succ
	*/
	public void setSucc(boolean succ) {
		this.succ = succ;
	}

	/** 
	* 提示信息
	* @return
	*/
	public String getPromptMsg() {
		return promptMsg;
	}

	/** 
	* 提示信息
	* @param promptMsg
	*/
	public void setPromptMsg(String promptMsg) {
		this.promptMsg = promptMsg;
	}

	/** 
	* 子流程主键
	* @param sonFlowId
	*/
	public void setSonFlowId(Integer sonFlowId) {
		this.sonFlowId = sonFlowId;
	}

	/** 
	* 子流程主键
	* @return
	*/
	public Integer getSonFlowId() {
		return sonFlowId;
	}

	/** 
	* 关联子流程名称
	* @return
	*/
	public String getSonFlowName() {
		return sonFlowName;
	}

	/** 
	* 关联子流程名称
	* @param sonFlowName
	*/
	public void setSonFlowName(String sonFlowName) {
		this.sonFlowName = sonFlowName;
	}

	/** 
	* 主、子表单控件映射关系
	* @return
	*/
	public List<FormCompon> getListFlowFormCompons() {
		return listFlowFormCompons;
	}

	/** 
	* 主、子表单控件映射关系
	* @param listFlowFormCompons
	*/
	public void setListFlowFormCompons(List<FormCompon> listFlowFormCompons) {
		this.listFlowFormCompons = listFlowFormCompons;
	}

	/** 
	* 流程命名规则
	* @param titleRule
	*/
	public void setTitleRule(String titleRule) {
		this.titleRule = titleRule;
	}

	/** 
	* 流程命名规则
	* @return
	*/
	public String getTitleRule() {
		return titleRule;
	}

	/** 
	* 1已替换0未替换
	* @return
	*/
	public Integer getFormState() {
		return formState;
	}

	/** 
	* 1已替换0未替换
	* @param formState
	*/
	public void setFormState(Integer formState) {
		this.formState = formState;
	}

	/** 
	* 流程模块
	* @param flowModBusType
	*/
	public void setFlowModBusType(String flowModBusType) {
		this.flowModBusType = flowModBusType;
	}

	/** 
	* 流程模块
	* @return
	*/
	public String getFlowModBusType() {
		return flowModBusType;
	}
}
