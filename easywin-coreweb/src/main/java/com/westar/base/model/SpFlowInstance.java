package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.FormData;
import com.westar.base.pojo.ModSpConf;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 流程实例化对象表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowInstance {
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
	* 流程实例化名称
	*/
	@Filed
	private String flowName;
	/** 
	* 人员主键
	*/
	@Filed
	private Integer creator;
	/** 
	* 表单模板主键
	*/
	@Filed
	private Integer formKey;
	/** 
	* 流程状态 0开始 1提交 2保存
	*/
	@Filed
	private Integer flowState;
	/** 
	* activity实例化主键
	*/
	@Filed
	private String actInstaceId;
	/** 
	* 布局版本
	*/
	@Filed
	private Integer formVersion;
	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String busType;
	/** 
	* 业务主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 关联流程主键
	*/
	@Filed
	private Integer flowId;
	/** 
	* 审批状态；1是通过，0是驳回
	*/
	@Filed
	private Integer spState;
	/** 
	* 流程编号；当前时间+流程实例主键
	*/
	@Filed
	private String flowSerialNumber;
	/** 
	* 关联流程主键
	*/
	@Filed
	private Integer sonFlowId;
	/** 
	* 子流程流程实例化主键
	*/
	@Filed
	private Integer sonInstanceId;

	/****************以上主要为系统表字段********************/
	/** 
	* 关注状态0未关注1已关注
	*/
	private String attentionState;
	/** 
	* 部门名称
	*/
	private String depName;
	/** 
	* 人员名称
	*/
	private String creatorName;
	/** 
	* 布局主键
	*/
	private Integer layoutId;
	/** 
	* 表单是否替换
	*/
	private Integer formLayoutState;
	/** 
	* 审批管理模块名称
	*/
	private String busName;
	/** 
	* 使用的表单模块名称
	*/
	private String formModName;
	/** 
	* 项目阶段主键
	*/
	private Integer stagedItemId;
	/** 
	* 项目阶段名称
	*/
	private String stagedItemName;
	/** 
	* 当前审批人主键
	*/
	private Integer executor;
	/** 
	* 审批人类型
	*/
	private String executeType;
	/** 
	* 审批人姓名
	*/
	private String executorName;
	/** 
	* 性别
	*/
	private Integer gender;
	/** 
	* uuid
	*/
	private String uuid;
	/** 
	* 文件名称
	*/
	private String fileName;
	/** 
	* 流程审批人
	*/
	private List<SpFlowCurExecutor> listSpFlowCurExecutor;
	/** 
	* 当前步骤授权集合
	*/
	private List<SpFlowRunStepFormControl> listSpFlowRunStepFormControl;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 列表排序规则
	*/
	private String orderBy;
	/** 
	* 流程审批扭转数据集
	*/
	private List<SpFlowHiStep> listSpFlowHiStep;
	/** 
	* 审批关联附件
	*/
	private List<SpFlowUpfile> spFlowUpfiles;
	/** 
	* 布局信息
	*/
	private FormLayout formLayout;
	/** 
	* 表单数据
	*/
	private FormData formData;
	/** 
	* 创建人性别
	*/
	private Integer creatorGender;
	/** 
	* 创建人uuid
	*/
	private String creatorUuid;
	/** 
	* 创建人文件名称
	*/
	private String creatorFileName;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 子流程流程单号
	*/
	private String sonFlowSerialNum;
	/** 
	* 子流程名称
	*/
	private String sonFlowName;
	private List<UserInfo> listCreator;
	/** 
	* 当前审批
	*/
	private List<UserInfo> listExecutor;
	/** 
	* 审批文档总数
	*/
	private Integer upfileNum;
	/** 
	* 关联任务总数
	*/
	private Integer taskNum;
	/** 
	* 会签记录集合
	*/
	private List<SpFlowHuiQianInfo> listSpFlowHuiQianInfo;
	/** 
	* 审批留言
	*/
	private List<SpFlowTalk> listSpFlowTalk;
	/** 
	* 审批留言数
	*/
	private Integer spFlowTalkNum;
	/** 
	* 审批会签数
	*/
	private Integer huiQianNum;
	/** 
	* 审批配置
	*/
	private ModSpConf modSpConf;

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
	* 流程实例化名称
	* @param flowName
	*/
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/** 
	* 流程实例化名称
	* @return
	*/
	public String getFlowName() {
		return flowName;
	}

	/** 
	* 人员主键
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 表单模板主键
	* @param formKey
	*/
	public void setFormKey(Integer formKey) {
		this.formKey = formKey;
	}

	/** 
	* 表单模板主键
	* @return
	*/
	public Integer getFormKey() {
		return formKey;
	}

	/** 
	* 流程状态 0开始 1提交 2保存
	* @param flowState
	*/
	public void setFlowState(Integer flowState) {
		this.flowState = flowState;
	}

	/** 
	* 流程状态 0开始 1提交 2保存
	* @return
	*/
	public Integer getFlowState() {
		return flowState;
	}

	/** 
	* activity实例化主键
	* @param actInstaceId
	*/
	public void setActInstaceId(String actInstaceId) {
		this.actInstaceId = actInstaceId;
	}

	/** 
	* activity实例化主键
	* @return
	*/
	public String getActInstaceId() {
		return actInstaceId;
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
	* 布局主键
	* @return
	*/
	public Integer getLayoutId() {
		return layoutId;
	}

	/** 
	* 布局主键
	* @param layoutId
	*/
	public void setLayoutId(Integer layoutId) {
		this.layoutId = layoutId;
	}

	/** 
	* 布局版本
	* @param formVersion
	*/
	public void setFormVersion(Integer formVersion) {
		this.formVersion = formVersion;
	}

	/** 
	* 布局版本
	* @return
	*/
	public Integer getFormVersion() {
		return formVersion;
	}

	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 业务主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 业务主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 审批管理模块名称
	* @return
	*/
	public String getBusName() {
		return busName;
	}

	/** 
	* 审批管理模块名称
	* @param busName
	*/
	public void setBusName(String busName) {
		this.busName = busName;
	}

	/** 
	* 使用的表单模块名称
	* @return
	*/
	public String getFormModName() {
		return formModName;
	}

	/** 
	* 使用的表单模块名称
	* @param formModName
	*/
	public void setFormModName(String formModName) {
		this.formModName = formModName;
	}

	/** 
	* 项目阶段主键
	* @return
	*/
	public Integer getStagedItemId() {
		return stagedItemId;
	}

	/** 
	* 项目阶段主键
	* @param stagedItemId
	*/
	public void setStagedItemId(Integer stagedItemId) {
		this.stagedItemId = stagedItemId;
	}

	/** 
	* 项目阶段名称
	* @return
	*/
	public String getStagedItemName() {
		return stagedItemName;
	}

	/** 
	* 项目阶段名称
	* @param stagedItemName
	*/
	public void setStagedItemName(String stagedItemName) {
		this.stagedItemName = stagedItemName;
	}

	/** 
	* 关联流程主键
	* @param flowId
	*/
	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}

	/** 
	* 关联流程主键
	* @return
	*/
	public Integer getFlowId() {
		return flowId;
	}

	/** 
	* 审批人姓名
	* @return
	*/
	public String getExecutorName() {
		return executorName;
	}

	/** 
	* 审批人姓名
	* @param executorName
	*/
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	/** 
	* 性别
	* @return
	*/
	public Integer getGender() {
		return gender;
	}

	/** 
	* 性别
	* @param gender
	*/
	public void setGender(Integer gender) {
		this.gender = gender;
	}

	/** 
	* uuid
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* uuid
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** 
	* 文件名称
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 文件名称
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	* 流程审批人
	* @return
	*/
	public List<SpFlowCurExecutor> getListSpFlowCurExecutor() {
		return listSpFlowCurExecutor;
	}

	/** 
	* 流程审批人
	* @param listSpFlowCurExecutor
	*/
	public void setListSpFlowCurExecutor(List<SpFlowCurExecutor> listSpFlowCurExecutor) {
		this.listSpFlowCurExecutor = listSpFlowCurExecutor;
	}

	/** 
	* 当前步骤授权集合
	* @return
	*/
	public List<SpFlowRunStepFormControl> getListSpFlowRunStepFormControl() {
		return listSpFlowRunStepFormControl;
	}

	/** 
	* 当前步骤授权集合
	* @param listSpFlowRunStepFormControl
	*/
	public void setListSpFlowRunStepFormControl(List<SpFlowRunStepFormControl> listSpFlowRunStepFormControl) {
		this.listSpFlowRunStepFormControl = listSpFlowRunStepFormControl;
	}

	/** 
	* 当前审批人主键
	* @return
	*/
	public Integer getExecutor() {
		return executor;
	}

	/** 
	* 当前审批人主键
	* @param executor
	*/
	public void setExecutor(Integer executor) {
		this.executor = executor;
	}

	/** 
	* 查询的时间起
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询的时间起
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询的时间止
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的时间止
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	/** 
	* 人员名称
	* @return
	*/
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 人员名称
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 关注状态0未关注1已关注
	* @return
	*/
	public String getAttentionState() {
		return attentionState;
	}

	/** 
	* 关注状态0未关注1已关注
	* @param attentionState
	*/
	public void setAttentionState(String attentionState) {
		this.attentionState = attentionState;
	}

	/** 
	* 流程审批扭转数据集
	* @return
	*/
	public List<SpFlowHiStep> getListSpFlowHiStep() {
		return listSpFlowHiStep;
	}

	/** 
	* 流程审批扭转数据集
	* @param listSpFlowHiStep
	*/
	public void setListSpFlowHiStep(List<SpFlowHiStep> listSpFlowHiStep) {
		this.listSpFlowHiStep = listSpFlowHiStep;
	}

	/** 
	* 审批关联附件
	* @return
	*/
	public List<SpFlowUpfile> getSpFlowUpfiles() {
		return spFlowUpfiles;
	}

	/** 
	* 审批关联附件
	* @param spFlowUpfiles
	*/
	public void setSpFlowUpfiles(List<SpFlowUpfile> spFlowUpfiles) {
		this.spFlowUpfiles = spFlowUpfiles;
	}

	/** 
	* 布局信息
	* @return
	*/
	public FormLayout getFormLayout() {
		return formLayout;
	}

	/** 
	* 布局信息
	* @param formLayout
	*/
	public void setFormLayout(FormLayout formLayout) {
		this.formLayout = formLayout;
	}

	/** 
	* 表单数据
	* @return
	*/
	public FormData getFormData() {
		return formData;
	}

	/** 
	* 表单数据
	* @param formData
	*/
	public void setFormData(FormData formData) {
		this.formData = formData;
	}

	/** 
	* 创建人性别
	* @return
	*/
	public Integer getCreatorGender() {
		return creatorGender;
	}

	/** 
	* 创建人性别
	* @param creatorGender
	*/
	public void setCreatorGender(Integer creatorGender) {
		this.creatorGender = creatorGender;
	}

	/** 
	* 创建人uuid
	* @return
	*/
	public String getCreatorUuid() {
		return creatorUuid;
	}

	/** 
	* 创建人uuid
	* @param creatorUuid
	*/
	public void setCreatorUuid(String creatorUuid) {
		this.creatorUuid = creatorUuid;
	}

	/** 
	* 创建人文件名称
	* @return
	*/
	public String getCreatorFileName() {
		return creatorFileName;
	}

	/** 
	* 创建人文件名称
	* @param creatorFileName
	*/
	public void setCreatorFileName(String creatorFileName) {
		this.creatorFileName = creatorFileName;
	}

	/** 
	* 审批状态；1是通过，0是驳回
	* @param spState
	*/
	public void setSpState(Integer spState) {
		this.spState = spState;
	}

	/** 
	* 审批状态；1是通过，0是驳回
	* @return
	*/
	public Integer getSpState() {
		return spState;
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
	* 流程编号；当前时间+流程实例主键
	* @param flowSerialNumber
	*/
	public void setFlowSerialNumber(String flowSerialNumber) {
		this.flowSerialNumber = flowSerialNumber;
	}

	/** 
	* 流程编号；当前时间+流程实例主键
	* @return
	*/
	public String getFlowSerialNumber() {
		return flowSerialNumber;
	}

	/** 
	* 关联流程主键
	* @param sonFlowId
	*/
	public void setSonFlowId(Integer sonFlowId) {
		this.sonFlowId = sonFlowId;
	}

	/** 
	* 关联流程主键
	* @return
	*/
	public Integer getSonFlowId() {
		return sonFlowId;
	}

	/** 
	* 子流程流程实例化主键
	* @param sonInstanceId
	*/
	public void setSonInstanceId(Integer sonInstanceId) {
		this.sonInstanceId = sonInstanceId;
	}

	/** 
	* 子流程流程实例化主键
	* @return
	*/
	public Integer getSonInstanceId() {
		return sonInstanceId;
	}

	/** 
	* 子流程流程单号
	* @return
	*/
	public String getSonFlowSerialNum() {
		return sonFlowSerialNum;
	}

	/** 
	* 子流程流程单号
	* @param sonFlowSerialNum
	*/
	public void setSonFlowSerialNum(String sonFlowSerialNum) {
		this.sonFlowSerialNum = sonFlowSerialNum;
	}

	/** 
	* 子流程名称
	* @return
	*/
	public String getSonFlowName() {
		return sonFlowName;
	}

	/** 
	* 子流程名称
	* @param sonFlowName
	*/
	public void setSonFlowName(String sonFlowName) {
		this.sonFlowName = sonFlowName;
	}

	public List<UserInfo> getListCreator() {
		return listCreator;
	}

	public void setListCreator(List<UserInfo> listCreator) {
		this.listCreator = listCreator;
	}

	/** 
	* 当前审批
	* @return
	*/
	public List<UserInfo> getListExecutor() {
		return listExecutor;
	}

	/** 
	* 当前审批
	* @param listExecutor
	*/
	public void setListExecutor(List<UserInfo> listExecutor) {
		this.listExecutor = listExecutor;
	}

	/** 
	* 表单是否替换
	* @return
	*/
	public Integer getFormLayoutState() {
		return formLayoutState;
	}

	/** 
	* 表单是否替换
	* @param formLayoutState
	*/
	public void setFormLayoutState(Integer formLayoutState) {
		this.formLayoutState = formLayoutState;
	}

	/** 
	* 审批文档总数
	* @return
	*/
	public Integer getUpfileNum() {
		return upfileNum;
	}

	/** 
	* 审批文档总数
	* @param upfileNum
	*/
	public void setUpfileNum(Integer upfileNum) {
		this.upfileNum = upfileNum;
	}

	/** 
	* 关联任务总数
	* @return
	*/
	public Integer getTaskNum() {
		return taskNum;
	}

	/** 
	* 关联任务总数
	* @param taskNum
	*/
	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
	}

	/** 
	* 会签记录集合
	* @return
	*/
	public List<SpFlowHuiQianInfo> getListSpFlowHuiQianInfo() {
		return listSpFlowHuiQianInfo;
	}

	/** 
	* 会签记录集合
	* @param listSpFlowHuiQianInfo
	*/
	public void setListSpFlowHuiQianInfo(List<SpFlowHuiQianInfo> listSpFlowHuiQianInfo) {
		this.listSpFlowHuiQianInfo = listSpFlowHuiQianInfo;
	}

	/** 
	* 审批人类型
	* @return
	*/
	public String getExecuteType() {
		return executeType;
	}

	/** 
	* 审批人类型
	* @param executeType
	*/
	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}

	/** 
	* 审批留言
	* @return
	*/
	public List<SpFlowTalk> getListSpFlowTalk() {
		return listSpFlowTalk;
	}

	/** 
	* 审批留言
	* @param listSpFlowTalk
	*/
	public void setListSpFlowTalk(List<SpFlowTalk> listSpFlowTalk) {
		this.listSpFlowTalk = listSpFlowTalk;
	}

	/** 
	* 审批留言数
	* @return
	*/
	public Integer getSpFlowTalkNum() {
		return spFlowTalkNum;
	}

	/** 
	* 审批留言数
	* @param spFlowTalkNum
	*/
	public void setSpFlowTalkNum(Integer spFlowTalkNum) {
		this.spFlowTalkNum = spFlowTalkNum;
	}

	/** 
	* 审批会签数
	* @return
	*/
	public Integer getHuiQianNum() {
		return huiQianNum;
	}

	/** 
	* 审批会签数
	* @param huiQianNum
	*/
	public void setHuiQianNum(Integer huiQianNum) {
		this.huiQianNum = huiQianNum;
	}

	/** 
	* 审批配置
	* @return
	*/
	public ModSpConf getModSpConf() {
		return modSpConf;
	}

	/** 
	* 审批配置
	* @param modSpConf
	*/
	public void setModSpConf(ModSpConf modSpConf) {
		this.modSpConf = modSpConf;
	}
}
