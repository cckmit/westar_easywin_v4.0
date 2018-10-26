package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 变更管理过程
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ModifyPm {
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
	* 流程实例化主键
	*/
	@Filed
	private Integer instanceId;
	/** 
	* 人员主键
	*/
	@Filed
	private Integer creator;
	/** 
	* 变更ID
	*/
	@Filed
	private String modifyId;
	/** 
	* 请求人姓名
	*/
	@Filed
	private String applicantName;
	/** 
	* 请求人所在公司
	*/
	@Filed
	private String applicantCom;
	/** 
	* 请求人所在部门
	*/
	@Filed
	private String applicantDepName;
	/** 
	* 请求人email
	*/
	@Filed
	private String applicantEmail;
	/** 
	* 请求人办公电话
	*/
	@Filed
	private String applicantLinePhone;
	/** 
	* 请求人移动电话
	*/
	@Filed
	private String applicantMovePhone;
	/** 
	* （变更）标题
	*/
	@Filed
	private String modifyTitle;
	/** 
	* 变更来源
	*/
	@Filed
	private String modifySource;
	/** 
	* 关联的事件单号
	*/
	@Filed
	private String modifyRelateEvent;
	/** 
	* 关联的问题单号
	*/
	@Filed
	private String modifyRelateIssue;
	/** 
	* 变更类型
	*/
	@Filed
	private String modifySpace;
	/** 
	* 风险等级
	*/
	@Filed
	private String modifyRiskSpace;
	/** 
	* 变更分类
	*/
	@Filed
	private String modifyType;
	/** 
	* （变更）描述
	*/
	@Filed
	private String modifyRemark;
	/** 
	* 变更影响应用系统
	*/
	@Filed
	private String modifyInSys;
	/** 
	* 变更影响客户部门
	*/
	@Filed
	private String modifyInDep;
	/** 
	* 变更是否中断业务
	*/
	@Filed
	private String modifyPauseState;
	/** 
	* 变更是否需要测试
	*/
	@Filed
	private String modifyTestState;
	/** 
	* 需要通知的客户名称
	*/
	@Filed
	private String notifyCrmName;
	/** 
	* 变更状态
	*/
	@Filed
	private String modifyStatus;
	/** 
	* 变更主管
	*/
	@Filed
	private String modifyCharge;
	/** 
	* 变更主管接受变更时间
	*/
	@Filed
	private String modifyChargeTime;
	/** 
	* 变更计划开始时间
	*/
	@Filed
	private String modifyPlanStartTimes;
	/** 
	* 变更计划完成时间
	*/
	@Filed
	private String modifyPlanEndTimes;
	/** 
	* 变更实际开始时间
	*/
	@Filed
	private String modifyStartTimes;
	/** 
	* 变更实际完成时间
	*/
	@Filed
	private String modifyEndTimes;
	/** 
	* 变更实施记录
	*/
	@Filed
	private String modifyEffectRemark;
	/** 
	* 变更测试记录
	*/
	@Filed
	private String modifyTestRemark;
	/** 
	* 变更观察记录
	*/
	@Filed
	private String modifyViewRemark;
	/** 
	* 中断时长
	*/
	@Filed
	private String modifyPauseTime;
	/** 
	* 回顾意见
	*/
	@Filed
	private String modifyReviewRemark;
	/** 
	* 回顾代码
	*/
	@Filed
	private String modifyReviewCode;
	/** 
	* 变更结束代码
	*/
	@Filed
	private String modifyEndCode;
	/** 
	* 变更关闭人
	*/
	@Filed
	private String modifyCloseUser;
	/** 
	* 关闭的时间
	*/
	@Filed
	private String modifyCloseDate;
	/** 
	* 关联的发布单号
	*/
	@Filed
	private String modifyRelateModify;
	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据
	*/
	@Filed
	private Integer status;
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
	* 业务名称
	*/
	@Filed
	private String busModName;

	/****************以上主要为系统表字段********************/
	/** 
	* 查询的创建人信息
	*/
	private List<UserInfo> listCreator;
	/** 
	* 查询的开始时间
	*/
	private String startDate;
	/** 
	* 查询的截止时间
	*/
	private String endDate;
	/** 
	* 创建人名字
	*/
	private String creatorName;
	/** 
	* 流程名称
	*/
	private String flowName;
	/** 
	* 流程状态
	*/
	private Integer flowState;
	/** 
	* 年份
	*/
	private Integer year;
	/** 
	* 非法变更标识
	*/
	private String illegalState;
	/** 
	* 按计划完成1是0否
	*/
	private String scheduleState;

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
	* 流程实例化主键
	* @param instanceId
	*/
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	/** 
	* 流程实例化主键
	* @return
	*/
	public Integer getInstanceId() {
		return instanceId;
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
	* 变更ID
	* @param modifyId
	*/
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

	/** 
	* 变更ID
	* @return
	*/
	public String getModifyId() {
		return modifyId;
	}

	/** 
	* 请求人姓名
	* @param applicantName
	*/
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	/** 
	* 请求人姓名
	* @return
	*/
	public String getApplicantName() {
		return applicantName;
	}

	/** 
	* 请求人所在公司
	* @param applicantCom
	*/
	public void setApplicantCom(String applicantCom) {
		this.applicantCom = applicantCom;
	}

	/** 
	* 请求人所在公司
	* @return
	*/
	public String getApplicantCom() {
		return applicantCom;
	}

	/** 
	* 请求人所在部门
	* @param applicantDepName
	*/
	public void setApplicantDepName(String applicantDepName) {
		this.applicantDepName = applicantDepName;
	}

	/** 
	* 请求人所在部门
	* @return
	*/
	public String getApplicantDepName() {
		return applicantDepName;
	}

	/** 
	* 请求人email
	* @param applicantEmail
	*/
	public void setApplicantEmail(String applicantEmail) {
		this.applicantEmail = applicantEmail;
	}

	/** 
	* 请求人email
	* @return
	*/
	public String getApplicantEmail() {
		return applicantEmail;
	}

	/** 
	* 请求人办公电话
	* @param applicantLinePhone
	*/
	public void setApplicantLinePhone(String applicantLinePhone) {
		this.applicantLinePhone = applicantLinePhone;
	}

	/** 
	* 请求人办公电话
	* @return
	*/
	public String getApplicantLinePhone() {
		return applicantLinePhone;
	}

	/** 
	* 请求人移动电话
	* @param applicantMovePhone
	*/
	public void setApplicantMovePhone(String applicantMovePhone) {
		this.applicantMovePhone = applicantMovePhone;
	}

	/** 
	* 请求人移动电话
	* @return
	*/
	public String getApplicantMovePhone() {
		return applicantMovePhone;
	}

	/** 
	* （变更）标题
	* @param modifyTitle
	*/
	public void setModifyTitle(String modifyTitle) {
		this.modifyTitle = modifyTitle;
	}

	/** 
	* （变更）标题
	* @return
	*/
	public String getModifyTitle() {
		return modifyTitle;
	}

	/** 
	* 变更来源
	* @param modifySource
	*/
	public void setModifySource(String modifySource) {
		this.modifySource = modifySource;
	}

	/** 
	* 变更来源
	* @return
	*/
	public String getModifySource() {
		return modifySource;
	}

	/** 
	* 关联的事件单号
	* @param modifyRelateEvent
	*/
	public void setModifyRelateEvent(String modifyRelateEvent) {
		this.modifyRelateEvent = modifyRelateEvent;
	}

	/** 
	* 关联的事件单号
	* @return
	*/
	public String getModifyRelateEvent() {
		return modifyRelateEvent;
	}

	/** 
	* 关联的问题单号
	* @param modifyRelateIssue
	*/
	public void setModifyRelateIssue(String modifyRelateIssue) {
		this.modifyRelateIssue = modifyRelateIssue;
	}

	/** 
	* 关联的问题单号
	* @return
	*/
	public String getModifyRelateIssue() {
		return modifyRelateIssue;
	}

	/** 
	* 变更类型
	* @param modifySpace
	*/
	public void setModifySpace(String modifySpace) {
		this.modifySpace = modifySpace;
	}

	/** 
	* 变更类型
	* @return
	*/
	public String getModifySpace() {
		return modifySpace;
	}

	/** 
	* 风险等级
	* @param modifyRiskSpace
	*/
	public void setModifyRiskSpace(String modifyRiskSpace) {
		this.modifyRiskSpace = modifyRiskSpace;
	}

	/** 
	* 风险等级
	* @return
	*/
	public String getModifyRiskSpace() {
		return modifyRiskSpace;
	}

	/** 
	* 变更分类
	* @param modifyType
	*/
	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	/** 
	* 变更分类
	* @return
	*/
	public String getModifyType() {
		return modifyType;
	}

	/** 
	* （变更）描述
	* @param modifyRemark
	*/
	public void setModifyRemark(String modifyRemark) {
		this.modifyRemark = modifyRemark;
	}

	/** 
	* （变更）描述
	* @return
	*/
	public String getModifyRemark() {
		return modifyRemark;
	}

	/** 
	* 变更影响应用系统
	* @param modifyInSys
	*/
	public void setModifyInSys(String modifyInSys) {
		this.modifyInSys = modifyInSys;
	}

	/** 
	* 变更影响应用系统
	* @return
	*/
	public String getModifyInSys() {
		return modifyInSys;
	}

	/** 
	* 变更影响客户部门
	* @param modifyInDep
	*/
	public void setModifyInDep(String modifyInDep) {
		this.modifyInDep = modifyInDep;
	}

	/** 
	* 变更影响客户部门
	* @return
	*/
	public String getModifyInDep() {
		return modifyInDep;
	}

	/** 
	* 变更是否中断业务
	* @param modifyPauseState
	*/
	public void setModifyPauseState(String modifyPauseState) {
		this.modifyPauseState = modifyPauseState;
	}

	/** 
	* 变更是否中断业务
	* @return
	*/
	public String getModifyPauseState() {
		return modifyPauseState;
	}

	/** 
	* 变更是否需要测试
	* @param modifyTestState
	*/
	public void setModifyTestState(String modifyTestState) {
		this.modifyTestState = modifyTestState;
	}

	/** 
	* 变更是否需要测试
	* @return
	*/
	public String getModifyTestState() {
		return modifyTestState;
	}

	/** 
	* 需要通知的客户名称
	* @param notifyCrmName
	*/
	public void setNotifyCrmName(String notifyCrmName) {
		this.notifyCrmName = notifyCrmName;
	}

	/** 
	* 需要通知的客户名称
	* @return
	*/
	public String getNotifyCrmName() {
		return notifyCrmName;
	}

	/** 
	* 变更状态
	* @param modifyStatus
	*/
	public void setModifyStatus(String modifyStatus) {
		this.modifyStatus = modifyStatus;
	}

	/** 
	* 变更状态
	* @return
	*/
	public String getModifyStatus() {
		return modifyStatus;
	}

	/** 
	* 变更主管
	* @param modifyCharge
	*/
	public void setModifyCharge(String modifyCharge) {
		this.modifyCharge = modifyCharge;
	}

	/** 
	* 变更主管
	* @return
	*/
	public String getModifyCharge() {
		return modifyCharge;
	}

	/** 
	* 变更主管接受变更时间
	* @param modifyChargeTime
	*/
	public void setModifyChargeTime(String modifyChargeTime) {
		this.modifyChargeTime = modifyChargeTime;
	}

	/** 
	* 变更主管接受变更时间
	* @return
	*/
	public String getModifyChargeTime() {
		return modifyChargeTime;
	}

	/** 
	* 变更计划开始时间
	* @param modifyPlanStartTimes
	*/
	public void setModifyPlanStartTimes(String modifyPlanStartTimes) {
		this.modifyPlanStartTimes = modifyPlanStartTimes;
	}

	/** 
	* 变更计划开始时间
	* @return
	*/
	public String getModifyPlanStartTimes() {
		return modifyPlanStartTimes;
	}

	/** 
	* 变更计划完成时间
	* @param modifyPlanEndTimes
	*/
	public void setModifyPlanEndTimes(String modifyPlanEndTimes) {
		this.modifyPlanEndTimes = modifyPlanEndTimes;
	}

	/** 
	* 变更计划完成时间
	* @return
	*/
	public String getModifyPlanEndTimes() {
		return modifyPlanEndTimes;
	}

	/** 
	* 变更实际开始时间
	* @param modifyStartTimes
	*/
	public void setModifyStartTimes(String modifyStartTimes) {
		this.modifyStartTimes = modifyStartTimes;
	}

	/** 
	* 变更实际开始时间
	* @return
	*/
	public String getModifyStartTimes() {
		return modifyStartTimes;
	}

	/** 
	* 变更实际完成时间
	* @param modifyEndTimes
	*/
	public void setModifyEndTimes(String modifyEndTimes) {
		this.modifyEndTimes = modifyEndTimes;
	}

	/** 
	* 变更实际完成时间
	* @return
	*/
	public String getModifyEndTimes() {
		return modifyEndTimes;
	}

	/** 
	* 变更实施记录
	* @param modifyEffectRemark
	*/
	public void setModifyEffectRemark(String modifyEffectRemark) {
		this.modifyEffectRemark = modifyEffectRemark;
	}

	/** 
	* 变更实施记录
	* @return
	*/
	public String getModifyEffectRemark() {
		return modifyEffectRemark;
	}

	/** 
	* 变更测试记录
	* @param modifyTestRemark
	*/
	public void setModifyTestRemark(String modifyTestRemark) {
		this.modifyTestRemark = modifyTestRemark;
	}

	/** 
	* 变更测试记录
	* @return
	*/
	public String getModifyTestRemark() {
		return modifyTestRemark;
	}

	/** 
	* 变更观察记录
	* @param modifyViewRemark
	*/
	public void setModifyViewRemark(String modifyViewRemark) {
		this.modifyViewRemark = modifyViewRemark;
	}

	/** 
	* 变更观察记录
	* @return
	*/
	public String getModifyViewRemark() {
		return modifyViewRemark;
	}

	/** 
	* 中断时长
	* @param modifyPauseTime
	*/
	public void setModifyPauseTime(String modifyPauseTime) {
		this.modifyPauseTime = modifyPauseTime;
	}

	/** 
	* 中断时长
	* @return
	*/
	public String getModifyPauseTime() {
		return modifyPauseTime;
	}

	/** 
	* 回顾意见
	* @param modifyReviewRemark
	*/
	public void setModifyReviewRemark(String modifyReviewRemark) {
		this.modifyReviewRemark = modifyReviewRemark;
	}

	/** 
	* 回顾意见
	* @return
	*/
	public String getModifyReviewRemark() {
		return modifyReviewRemark;
	}

	/** 
	* 回顾代码
	* @param modifyReviewCode
	*/
	public void setModifyReviewCode(String modifyReviewCode) {
		this.modifyReviewCode = modifyReviewCode;
	}

	/** 
	* 回顾代码
	* @return
	*/
	public String getModifyReviewCode() {
		return modifyReviewCode;
	}

	/** 
	* 变更结束代码
	* @param modifyEndCode
	*/
	public void setModifyEndCode(String modifyEndCode) {
		this.modifyEndCode = modifyEndCode;
	}

	/** 
	* 变更结束代码
	* @return
	*/
	public String getModifyEndCode() {
		return modifyEndCode;
	}

	/** 
	* 变更关闭人
	* @param modifyCloseUser
	*/
	public void setModifyCloseUser(String modifyCloseUser) {
		this.modifyCloseUser = modifyCloseUser;
	}

	/** 
	* 变更关闭人
	* @return
	*/
	public String getModifyCloseUser() {
		return modifyCloseUser;
	}

	/** 
	* 关闭的时间
	* @param modifyCloseDate
	*/
	public void setModifyCloseDate(String modifyCloseDate) {
		this.modifyCloseDate = modifyCloseDate;
	}

	/** 
	* 关闭的时间
	* @return
	*/
	public String getModifyCloseDate() {
		return modifyCloseDate;
	}

	/** 
	* 关联的发布单号
	* @param modifyRelateModify
	*/
	public void setModifyRelateModify(String modifyRelateModify) {
		this.modifyRelateModify = modifyRelateModify;
	}

	/** 
	* 关联的发布单号
	* @return
	*/
	public String getModifyRelateModify() {
		return modifyRelateModify;
	}

	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据
	* @param status
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	* 业务状态标识符；0-默认状态；1审核通过；-1驳回数据
	* @return
	*/
	public Integer getStatus() {
		return status;
	}

	/** 
	* 创建人名字
	* @return
	*/
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 创建人名字
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 流程名称
	* @return
	*/
	public String getFlowName() {
		return flowName;
	}

	/** 
	* 流程名称
	* @param flowName
	*/
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/** 
	* 流程状态
	* @return
	*/
	public Integer getFlowState() {
		return flowState;
	}

	/** 
	* 流程状态
	* @param flowState
	*/
	public void setFlowState(Integer flowState) {
		this.flowState = flowState;
	}

	/** 
	* 年份
	* @return
	*/
	public Integer getYear() {
		return year;
	}

	/** 
	* 年份
	* @param year
	*/
	public void setYear(Integer year) {
		this.year = year;
	}

	/** 
	* 查询的创建人信息
	* @return
	*/
	public List<UserInfo> getListCreator() {
		return listCreator;
	}

	/** 
	* 查询的创建人信息
	* @param listCreator
	*/
	public void setListCreator(List<UserInfo> listCreator) {
		this.listCreator = listCreator;
	}

	/** 
	* 查询的开始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询的开始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询的截止时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的截止时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 非法变更标识
	* @return
	*/
	public String getIllegalState() {
		return illegalState;
	}

	/** 
	* 非法变更标识
	* @param illegalState
	*/
	public void setIllegalState(String illegalState) {
		this.illegalState = illegalState;
	}

	/** 
	* 按计划完成1是0否
	* @return
	*/
	public String getScheduleState() {
		return scheduleState;
	}

	/** 
	* 按计划完成1是0否
	* @param scheduleState
	*/
	public void setScheduleState(String scheduleState) {
		this.scheduleState = scheduleState;
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
	* 业务名称
	* @param busModName
	*/
	public void setBusModName(String busModName) {
		this.busModName = busModName;
	}

	/** 
	* 业务名称
	* @return
	*/
	public String getBusModName() {
		return busModName;
	}
}
