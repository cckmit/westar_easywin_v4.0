package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 问题管理过程
 */
@Table
@JsonInclude(Include.NON_NULL)
public class IssuePm {
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
	* 问题ID
	*/
	@Filed
	private String issueId;
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
	* 问题发生的地点
	*/
	@Filed
	private String issueAddress;
	/** 
	* 问题记录时间
	*/
	@Filed
	private String issueTime;
	/** 
	* 问题来源
	*/
	@Filed
	private String issueSource;
	/** 
	* 服务（问题）优先级
	*/
	@Filed
	private String issuePriorityDegree;
	/** 
	* 问题分类
	*/
	@Filed
	private String issueType;
	/** 
	* （问题）标题
	*/
	@Filed
	private String issueTitle;
	/** 
	* （问题）描述
	*/
	@Filed
	private String issueRemark;
	/** 
	* 变通方法
	*/
	@Filed
	private String issueReplace;
	/** 
	* 问题原因
	*/
	@Filed
	private String issueCause;
	/** 
	* 重复问题标记
	*/
	@Filed
	private String issueRepetitionMark;
	/** 
	* 问题状态
	*/
	@Filed
	private String issueStatus;
	/** 
	* 问题日志
	*/
	@Filed
	private String issueLogs;
	/** 
	* 记录事件状态到分析中的时间
	*/
	@Filed
	private String issueStartTimes;
	/** 
	* 记录事件已有解决方案的时间
	*/
	@Filed
	private String issueEndTimes;
	/** 
	* 解决方案
	*/
	@Filed
	private String issueSolution;
	/** 
	* 问题结束代码
	*/
	@Filed
	private String issueEndCode;
	/** 
	* 问题无法解决原因
	*/
	@Filed
	private String issueStillReason;
	/** 
	* 记录问题的配置项代码
	*/
	@Filed
	private String issueBugRemark;
	/** 
	* 关联的事件单号
	*/
	@Filed
	private String issueRelateEvent;
	/** 
	* 关联的变更单号
	*/
	@Filed
	private String issueRelateModify;
	/** 
	* 记录问题对应的知识单号
	*/
	@Filed
	private String issueRelateStock;
	/** 
	* 关闭的时间
	*/
	@Filed
	private String issueCloseDate;
	/** 
	* 问题所影响的客户
	*/
	@Filed
	private String crmName;
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
	* 查询解决完成开始时间
	*/
	private String startResolveDate;
	/** 
	* 查询解决完成结束时间
	*/
	private String endResolveDate;
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
	* 查询的年份
	*/
	private Integer year;

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
	* 问题ID
	* @param issueId
	*/
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	/** 
	* 问题ID
	* @return
	*/
	public String getIssueId() {
		return issueId;
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
	* 问题发生的地点
	* @param issueAddress
	*/
	public void setIssueAddress(String issueAddress) {
		this.issueAddress = issueAddress;
	}

	/** 
	* 问题发生的地点
	* @return
	*/
	public String getIssueAddress() {
		return issueAddress;
	}

	/** 
	* 问题记录时间
	* @param issueTime
	*/
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	/** 
	* 问题记录时间
	* @return
	*/
	public String getIssueTime() {
		return issueTime;
	}

	/** 
	* 问题来源
	* @param issueSource
	*/
	public void setIssueSource(String issueSource) {
		this.issueSource = issueSource;
	}

	/** 
	* 问题来源
	* @return
	*/
	public String getIssueSource() {
		return issueSource;
	}

	/** 
	* 服务（问题）优先级
	* @param issuePriorityDegree
	*/
	public void setIssuePriorityDegree(String issuePriorityDegree) {
		this.issuePriorityDegree = issuePriorityDegree;
	}

	/** 
	* 服务（问题）优先级
	* @return
	*/
	public String getIssuePriorityDegree() {
		return issuePriorityDegree;
	}

	/** 
	* 问题分类
	* @param issueType
	*/
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	/** 
	* 问题分类
	* @return
	*/
	public String getIssueType() {
		return issueType;
	}

	/** 
	* （问题）标题
	* @param issueTitle
	*/
	public void setIssueTitle(String issueTitle) {
		this.issueTitle = issueTitle;
	}

	/** 
	* （问题）标题
	* @return
	*/
	public String getIssueTitle() {
		return issueTitle;
	}

	/** 
	* （问题）描述
	* @param issueRemark
	*/
	public void setIssueRemark(String issueRemark) {
		this.issueRemark = issueRemark;
	}

	/** 
	* （问题）描述
	* @return
	*/
	public String getIssueRemark() {
		return issueRemark;
	}

	/** 
	* 变通方法
	* @param issueReplace
	*/
	public void setIssueReplace(String issueReplace) {
		this.issueReplace = issueReplace;
	}

	/** 
	* 变通方法
	* @return
	*/
	public String getIssueReplace() {
		return issueReplace;
	}

	/** 
	* 问题原因
	* @param issueCause
	*/
	public void setIssueCause(String issueCause) {
		this.issueCause = issueCause;
	}

	/** 
	* 问题原因
	* @return
	*/
	public String getIssueCause() {
		return issueCause;
	}

	/** 
	* 重复问题标记
	* @param issueRepetitionMark
	*/
	public void setIssueRepetitionMark(String issueRepetitionMark) {
		this.issueRepetitionMark = issueRepetitionMark;
	}

	/** 
	* 重复问题标记
	* @return
	*/
	public String getIssueRepetitionMark() {
		return issueRepetitionMark;
	}

	/** 
	* 问题状态
	* @param issueStatus
	*/
	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}

	/** 
	* 问题状态
	* @return
	*/
	public String getIssueStatus() {
		return issueStatus;
	}

	/** 
	* 问题日志
	* @param issueLogs
	*/
	public void setIssueLogs(String issueLogs) {
		this.issueLogs = issueLogs;
	}

	/** 
	* 问题日志
	* @return
	*/
	public String getIssueLogs() {
		return issueLogs;
	}

	/** 
	* 记录事件状态到分析中的时间
	* @param issueStartTimes
	*/
	public void setIssueStartTimes(String issueStartTimes) {
		this.issueStartTimes = issueStartTimes;
	}

	/** 
	* 记录事件状态到分析中的时间
	* @return
	*/
	public String getIssueStartTimes() {
		return issueStartTimes;
	}

	/** 
	* 记录事件已有解决方案的时间
	* @param issueEndTimes
	*/
	public void setIssueEndTimes(String issueEndTimes) {
		this.issueEndTimes = issueEndTimes;
	}

	/** 
	* 记录事件已有解决方案的时间
	* @return
	*/
	public String getIssueEndTimes() {
		return issueEndTimes;
	}

	/** 
	* 解决方案
	* @param issueSolution
	*/
	public void setIssueSolution(String issueSolution) {
		this.issueSolution = issueSolution;
	}

	/** 
	* 解决方案
	* @return
	*/
	public String getIssueSolution() {
		return issueSolution;
	}

	/** 
	* 问题结束代码
	* @param issueEndCode
	*/
	public void setIssueEndCode(String issueEndCode) {
		this.issueEndCode = issueEndCode;
	}

	/** 
	* 问题结束代码
	* @return
	*/
	public String getIssueEndCode() {
		return issueEndCode;
	}

	/** 
	* 问题无法解决原因
	* @param issueStillReason
	*/
	public void setIssueStillReason(String issueStillReason) {
		this.issueStillReason = issueStillReason;
	}

	/** 
	* 问题无法解决原因
	* @return
	*/
	public String getIssueStillReason() {
		return issueStillReason;
	}

	/** 
	* 记录问题的配置项代码
	* @param issueBugRemark
	*/
	public void setIssueBugRemark(String issueBugRemark) {
		this.issueBugRemark = issueBugRemark;
	}

	/** 
	* 记录问题的配置项代码
	* @return
	*/
	public String getIssueBugRemark() {
		return issueBugRemark;
	}

	/** 
	* 关联的事件单号
	* @param issueRelateEvent
	*/
	public void setIssueRelateEvent(String issueRelateEvent) {
		this.issueRelateEvent = issueRelateEvent;
	}

	/** 
	* 关联的事件单号
	* @return
	*/
	public String getIssueRelateEvent() {
		return issueRelateEvent;
	}

	/** 
	* 关联的变更单号
	* @param issueRelateModify
	*/
	public void setIssueRelateModify(String issueRelateModify) {
		this.issueRelateModify = issueRelateModify;
	}

	/** 
	* 关联的变更单号
	* @return
	*/
	public String getIssueRelateModify() {
		return issueRelateModify;
	}

	/** 
	* 记录问题对应的知识单号
	* @param issueRelateStock
	*/
	public void setIssueRelateStock(String issueRelateStock) {
		this.issueRelateStock = issueRelateStock;
	}

	/** 
	* 记录问题对应的知识单号
	* @return
	*/
	public String getIssueRelateStock() {
		return issueRelateStock;
	}

	/** 
	* 关闭的时间
	* @param issueCloseDate
	*/
	public void setIssueCloseDate(String issueCloseDate) {
		this.issueCloseDate = issueCloseDate;
	}

	/** 
	* 关闭的时间
	* @return
	*/
	public String getIssueCloseDate() {
		return issueCloseDate;
	}

	/** 
	* 问题所影响的客户
	* @param crmName
	*/
	public void setCrmName(String crmName) {
		this.crmName = crmName;
	}

	/** 
	* 问题所影响的客户
	* @return
	*/
	public String getCrmName() {
		return crmName;
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
	* 查询的年份
	* @return
	*/
	public Integer getYear() {
		return year;
	}

	/** 
	* 查询的年份
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
	* 查询解决完成开始时间
	* @return
	*/
	public String getStartResolveDate() {
		return startResolveDate;
	}

	/** 
	* 查询解决完成开始时间
	* @param startResolveDate
	*/
	public void setStartResolveDate(String startResolveDate) {
		this.startResolveDate = startResolveDate;
	}

	/** 
	* 查询解决完成结束时间
	* @return
	*/
	public String getEndResolveDate() {
		return endResolveDate;
	}

	/** 
	* 查询解决完成结束时间
	* @param endResolveDate
	*/
	public void setEndResolveDate(String endResolveDate) {
		this.endResolveDate = endResolveDate;
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
