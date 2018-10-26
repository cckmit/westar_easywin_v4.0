package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 发布管理过程
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ReleasePm {
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
	* 发布ID
	*/
	@Filed
	private String releaseId;
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
	* （发布）标题
	*/
	@Filed
	private String releaseTitle;
	/** 
	* 发布来源
	*/
	@Filed
	private String releaseSource;
	/** 
	* 关联的事件单号
	*/
	@Filed
	private String releaseRelateEvent;
	/** 
	* 关联的问题单号
	*/
	@Filed
	private String releaseRelateIssue;
	/** 
	* 发布类型
	*/
	@Filed
	private String releaseSpace;
	/** 
	* 风险等级
	*/
	@Filed
	private String releaseRiskSpace;
	/** 
	* 发布分类
	*/
	@Filed
	private String releaseType;
	/** 
	* （发布）描述
	*/
	@Filed
	private String releaseRemark;
	/** 
	* 发布影响应用系统
	*/
	@Filed
	private String releaseInSys;
	/** 
	* 发布影响客户部门
	*/
	@Filed
	private String releaseInDep;
	/** 
	* 需要通知的部门名称
	*/
	@Filed
	private String notifyDepName;
	/** 
	* 发布是否中断业务
	*/
	@Filed
	private String releasePauseState;
	/** 
	* 发布是否需要系统测试
	*/
	@Filed
	private String releaseSysTestState;
	/** 
	* 发布是否需要客户测试
	*/
	@Filed
	private String releaseCrmTestState;
	/** 
	* 发布状态
	*/
	@Filed
	private String releaseStatus;
	/** 
	* 发布主管
	*/
	@Filed
	private String releaseCharge;
	/** 
	* 发布主管接受发布时间
	*/
	@Filed
	private String releaseChargeTime;
	/** 
	* 发布计划开始时间
	*/
	@Filed
	private String releasePlanStartTimes;
	/** 
	* 发布计划完成时间
	*/
	@Filed
	private String releasePlanEndTimes;
	/** 
	* 发布实际开始时间
	*/
	@Filed
	private String releaseStartTimes;
	/** 
	* 发布实际完成时间
	*/
	@Filed
	private String releaseEndTimes;
	/** 
	* 发布实施记录
	*/
	@Filed
	private String releaseEffectRemark;
	/** 
	* 发布系统测试结果
	*/
	@Filed
	private String releaseSysTestRemark;
	/** 
	* 发布客户测试结果
	*/
	@Filed
	private String releaseCrmTestRemark;
	/** 
	* 发布观察记录
	*/
	@Filed
	private String releaseViewRemark;
	/** 
	* 中断时长
	*/
	@Filed
	private String releasePauseTime;
	/** 
	* 发布工作单关闭代码
	*/
	@Filed
	private String releaseCloseCode;
	/** 
	* 发布工作单状态
	*/
	@Filed
	private String releaseState;
	/** 
	* 发布结束代码
	*/
	@Filed
	private String releaseEndCode;
	/** 
	* 发布关闭人
	*/
	@Filed
	private String releaseCloseUser;
	/** 
	* 关闭的时间
	*/
	@Filed
	private String releaseCloseDate;
	/** 
	* 备注
	*/
	@Filed
	private String otherRemark;
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
	* 发布ID
	* @param releaseId
	*/
	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}

	/** 
	* 发布ID
	* @return
	*/
	public String getReleaseId() {
		return releaseId;
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
	* （发布）标题
	* @param releaseTitle
	*/
	public void setReleaseTitle(String releaseTitle) {
		this.releaseTitle = releaseTitle;
	}

	/** 
	* （发布）标题
	* @return
	*/
	public String getReleaseTitle() {
		return releaseTitle;
	}

	/** 
	* 发布来源
	* @param releaseSource
	*/
	public void setReleaseSource(String releaseSource) {
		this.releaseSource = releaseSource;
	}

	/** 
	* 发布来源
	* @return
	*/
	public String getReleaseSource() {
		return releaseSource;
	}

	/** 
	* 关联的事件单号
	* @param releaseRelateEvent
	*/
	public void setReleaseRelateEvent(String releaseRelateEvent) {
		this.releaseRelateEvent = releaseRelateEvent;
	}

	/** 
	* 关联的事件单号
	* @return
	*/
	public String getReleaseRelateEvent() {
		return releaseRelateEvent;
	}

	/** 
	* 关联的问题单号
	* @param releaseRelateIssue
	*/
	public void setReleaseRelateIssue(String releaseRelateIssue) {
		this.releaseRelateIssue = releaseRelateIssue;
	}

	/** 
	* 关联的问题单号
	* @return
	*/
	public String getReleaseRelateIssue() {
		return releaseRelateIssue;
	}

	/** 
	* 发布类型
	* @param releaseSpace
	*/
	public void setReleaseSpace(String releaseSpace) {
		this.releaseSpace = releaseSpace;
	}

	/** 
	* 发布类型
	* @return
	*/
	public String getReleaseSpace() {
		return releaseSpace;
	}

	/** 
	* 风险等级
	* @param releaseRiskSpace
	*/
	public void setReleaseRiskSpace(String releaseRiskSpace) {
		this.releaseRiskSpace = releaseRiskSpace;
	}

	/** 
	* 风险等级
	* @return
	*/
	public String getReleaseRiskSpace() {
		return releaseRiskSpace;
	}

	/** 
	* 发布分类
	* @param releaseType
	*/
	public void setReleaseType(String releaseType) {
		this.releaseType = releaseType;
	}

	/** 
	* 发布分类
	* @return
	*/
	public String getReleaseType() {
		return releaseType;
	}

	/** 
	* （发布）描述
	* @param releaseRemark
	*/
	public void setReleaseRemark(String releaseRemark) {
		this.releaseRemark = releaseRemark;
	}

	/** 
	* （发布）描述
	* @return
	*/
	public String getReleaseRemark() {
		return releaseRemark;
	}

	/** 
	* 发布影响应用系统
	* @param releaseInSys
	*/
	public void setReleaseInSys(String releaseInSys) {
		this.releaseInSys = releaseInSys;
	}

	/** 
	* 发布影响应用系统
	* @return
	*/
	public String getReleaseInSys() {
		return releaseInSys;
	}

	/** 
	* 发布影响客户部门
	* @param releaseInDep
	*/
	public void setReleaseInDep(String releaseInDep) {
		this.releaseInDep = releaseInDep;
	}

	/** 
	* 发布影响客户部门
	* @return
	*/
	public String getReleaseInDep() {
		return releaseInDep;
	}

	/** 
	* 需要通知的部门名称
	* @param notifyDepName
	*/
	public void setNotifyDepName(String notifyDepName) {
		this.notifyDepName = notifyDepName;
	}

	/** 
	* 需要通知的部门名称
	* @return
	*/
	public String getNotifyDepName() {
		return notifyDepName;
	}

	/** 
	* 发布是否中断业务
	* @param releasePauseState
	*/
	public void setReleasePauseState(String releasePauseState) {
		this.releasePauseState = releasePauseState;
	}

	/** 
	* 发布是否中断业务
	* @return
	*/
	public String getReleasePauseState() {
		return releasePauseState;
	}

	/** 
	* 发布是否需要系统测试
	* @param releaseSysTestState
	*/
	public void setReleaseSysTestState(String releaseSysTestState) {
		this.releaseSysTestState = releaseSysTestState;
	}

	/** 
	* 发布是否需要系统测试
	* @return
	*/
	public String getReleaseSysTestState() {
		return releaseSysTestState;
	}

	/** 
	* 发布是否需要客户测试
	* @param releaseCrmTestState
	*/
	public void setReleaseCrmTestState(String releaseCrmTestState) {
		this.releaseCrmTestState = releaseCrmTestState;
	}

	/** 
	* 发布是否需要客户测试
	* @return
	*/
	public String getReleaseCrmTestState() {
		return releaseCrmTestState;
	}

	/** 
	* 发布状态
	* @param releaseStatus
	*/
	public void setReleaseStatus(String releaseStatus) {
		this.releaseStatus = releaseStatus;
	}

	/** 
	* 发布状态
	* @return
	*/
	public String getReleaseStatus() {
		return releaseStatus;
	}

	/** 
	* 发布主管
	* @param releaseCharge
	*/
	public void setReleaseCharge(String releaseCharge) {
		this.releaseCharge = releaseCharge;
	}

	/** 
	* 发布主管
	* @return
	*/
	public String getReleaseCharge() {
		return releaseCharge;
	}

	/** 
	* 发布主管接受发布时间
	* @param releaseChargeTime
	*/
	public void setReleaseChargeTime(String releaseChargeTime) {
		this.releaseChargeTime = releaseChargeTime;
	}

	/** 
	* 发布主管接受发布时间
	* @return
	*/
	public String getReleaseChargeTime() {
		return releaseChargeTime;
	}

	/** 
	* 发布计划开始时间
	* @param releasePlanStartTimes
	*/
	public void setReleasePlanStartTimes(String releasePlanStartTimes) {
		this.releasePlanStartTimes = releasePlanStartTimes;
	}

	/** 
	* 发布计划开始时间
	* @return
	*/
	public String getReleasePlanStartTimes() {
		return releasePlanStartTimes;
	}

	/** 
	* 发布计划完成时间
	* @param releasePlanEndTimes
	*/
	public void setReleasePlanEndTimes(String releasePlanEndTimes) {
		this.releasePlanEndTimes = releasePlanEndTimes;
	}

	/** 
	* 发布计划完成时间
	* @return
	*/
	public String getReleasePlanEndTimes() {
		return releasePlanEndTimes;
	}

	/** 
	* 发布实际开始时间
	* @param releaseStartTimes
	*/
	public void setReleaseStartTimes(String releaseStartTimes) {
		this.releaseStartTimes = releaseStartTimes;
	}

	/** 
	* 发布实际开始时间
	* @return
	*/
	public String getReleaseStartTimes() {
		return releaseStartTimes;
	}

	/** 
	* 发布实际完成时间
	* @param releaseEndTimes
	*/
	public void setReleaseEndTimes(String releaseEndTimes) {
		this.releaseEndTimes = releaseEndTimes;
	}

	/** 
	* 发布实际完成时间
	* @return
	*/
	public String getReleaseEndTimes() {
		return releaseEndTimes;
	}

	/** 
	* 发布实施记录
	* @param releaseEffectRemark
	*/
	public void setReleaseEffectRemark(String releaseEffectRemark) {
		this.releaseEffectRemark = releaseEffectRemark;
	}

	/** 
	* 发布实施记录
	* @return
	*/
	public String getReleaseEffectRemark() {
		return releaseEffectRemark;
	}

	/** 
	* 发布系统测试结果
	* @param releaseSysTestRemark
	*/
	public void setReleaseSysTestRemark(String releaseSysTestRemark) {
		this.releaseSysTestRemark = releaseSysTestRemark;
	}

	/** 
	* 发布系统测试结果
	* @return
	*/
	public String getReleaseSysTestRemark() {
		return releaseSysTestRemark;
	}

	/** 
	* 发布客户测试结果
	* @param releaseCrmTestRemark
	*/
	public void setReleaseCrmTestRemark(String releaseCrmTestRemark) {
		this.releaseCrmTestRemark = releaseCrmTestRemark;
	}

	/** 
	* 发布客户测试结果
	* @return
	*/
	public String getReleaseCrmTestRemark() {
		return releaseCrmTestRemark;
	}

	/** 
	* 发布观察记录
	* @param releaseViewRemark
	*/
	public void setReleaseViewRemark(String releaseViewRemark) {
		this.releaseViewRemark = releaseViewRemark;
	}

	/** 
	* 发布观察记录
	* @return
	*/
	public String getReleaseViewRemark() {
		return releaseViewRemark;
	}

	/** 
	* 中断时长
	* @param releasePauseTime
	*/
	public void setReleasePauseTime(String releasePauseTime) {
		this.releasePauseTime = releasePauseTime;
	}

	/** 
	* 中断时长
	* @return
	*/
	public String getReleasePauseTime() {
		return releasePauseTime;
	}

	/** 
	* 发布工作单关闭代码
	* @param releaseCloseCode
	*/
	public void setReleaseCloseCode(String releaseCloseCode) {
		this.releaseCloseCode = releaseCloseCode;
	}

	/** 
	* 发布工作单关闭代码
	* @return
	*/
	public String getReleaseCloseCode() {
		return releaseCloseCode;
	}

	/** 
	* 发布工作单状态
	* @param releaseState
	*/
	public void setReleaseState(String releaseState) {
		this.releaseState = releaseState;
	}

	/** 
	* 发布工作单状态
	* @return
	*/
	public String getReleaseState() {
		return releaseState;
	}

	/** 
	* 发布结束代码
	* @param releaseEndCode
	*/
	public void setReleaseEndCode(String releaseEndCode) {
		this.releaseEndCode = releaseEndCode;
	}

	/** 
	* 发布结束代码
	* @return
	*/
	public String getReleaseEndCode() {
		return releaseEndCode;
	}

	/** 
	* 发布关闭人
	* @param releaseCloseUser
	*/
	public void setReleaseCloseUser(String releaseCloseUser) {
		this.releaseCloseUser = releaseCloseUser;
	}

	/** 
	* 发布关闭人
	* @return
	*/
	public String getReleaseCloseUser() {
		return releaseCloseUser;
	}

	/** 
	* 关闭的时间
	* @param releaseCloseDate
	*/
	public void setReleaseCloseDate(String releaseCloseDate) {
		this.releaseCloseDate = releaseCloseDate;
	}

	/** 
	* 关闭的时间
	* @return
	*/
	public String getReleaseCloseDate() {
		return releaseCloseDate;
	}

	/** 
	* 备注
	* @param otherRemark
	*/
	public void setOtherRemark(String otherRemark) {
		this.otherRemark = otherRemark;
	}

	/** 
	* 备注
	* @return
	*/
	public String getOtherRemark() {
		return otherRemark;
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
