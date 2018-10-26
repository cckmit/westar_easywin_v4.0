package com.westar.base.model;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
/**
*事件管理过程
*/


@Table
public class EventProManage{

/*主键id*/
 @Identity
 private Integer id;

/*记录创建时间*/
 @DefaultFiled
 private String recordCreateTime;
 /*企业编号*/
 @Filed
 private Integer comId;
 /*人员主键*/
 @Filed
 private Integer creator;
 /*事件ID*/
 @Filed
 private String eventId;
 /*客户名称*/
 @Filed
 private String crmName;
 /*请求人姓名*/
 @Filed
 private String applicantName;
 /*请求人所在公司*/
 @Filed
 private String applicantCom;
 /*请求人所在部门*/
 @Filed
 private String applicantDepName;
 /*请求人email*/
 @Filed
 private String applicantEmail;
 /*请求人办公电话*/
 @Filed
 private String applicantLinePhone;
 /*请求人移动电话*/
 @Filed
 private String applicantMovePhone;
 /*事件发生的地点*/
 @Filed
 private String eventAddress;
 /*事件发生时间*/
 @Filed
 private String eventTime;
 /*事件性质*/
 @Filed
 private String eventAttr;
 /*事件来源*/
 @Filed
 private String eventSource;
 /*服务（事件）影响度*/
 @Filed
 private String eventInfluenceDegree;
 /*服务（事件）优先级*/
 @Filed
 private String eventPriorityDegree;
 /*所属系统类型*/
 @Filed
 private String sysType;
 /*事件分类*/
 @Filed
 private String eventType;
 /*（事件）标题*/
 @Filed
 private String eventTitle;
 /*（事件）描述*/
 @Filed
 private String eventRemark;
 /*事件解决人*/
 @Filed
 private String eventTerminator;
 /*事件状态*/
 @Filed
 private String eventStatus;
 /*事件解决方案*/
 @Filed
 private String eventSolution ;
 /*事件结束代码*/
 @Filed
 private String eventEndCode;
 /*重复事件标记*/
 @Filed
 private String eventRepetitionMark;
 /*记录事件状态到处理中的时间（系统自动产生）*/
 @Filed
 private Integer eventStartTimes;
 /*记录事件已解决的时间（系统自动产生）*/
 @Filed
 private Integer eventEndTimes;
 /*记录出现故障的配置项代码*/
 @Filed
 private String eventBugRemark;
 /*业务类型，列值与BusinessTypeConstant常量一一对应*/
 @Filed
 private String busType;
 /*业务主键（关联的变更单号）*/
 @Filed
 private Integer busId;
 /*事件完成期限*/
 @Filed
 private String eventLimitDate;

/****************以上主要为系统表字段********************/



/****************以上为自己添加字段********************/

 /**
 * 主键id
 * @param id
 */
 public void setId(Integer id) {
 	this.id=id;
 }

 /**
 * 主键id
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
 	this.recordCreateTime=recordCreateTime;
 }

 /**
 * 记录创建时间
 * @return
 */
 public String getRecordCreateTime() {
 	return recordCreateTime;
 }

 /**
 *企业编号
 * @param comId
 */
 public void setComId(Integer comId) {
 	this.comId = comId;
 }

 /**
 *企业编号
 * @return
 */
 public Integer getComId() {
 	return comId;
 }

 /**
 *人员主键
 * @param creator
 */
 public void setCreator(Integer creator) {
 	this.creator = creator;
 }

 /**
 *人员主键
 * @return
 */
 public Integer getCreator() {
 	return creator;
 }

 /**
 *事件ID
 * @param eventId
 */
 public void setEventId(String eventId) {
 	this.eventId = eventId;
 }

 /**
 *事件ID
 * @return
 */
 public String getEventId() {
 	return eventId;
 }

 /**
 *客户名称
 * @param crmName
 */
 public void setCrmName(String crmName) {
 	this.crmName = crmName;
 }

 /**
 *客户名称
 * @return
 */
 public String getCrmName() {
 	return crmName;
 }

 /**
 *请求人姓名
 * @param applicantName
 */
 public void setApplicantName(String applicantName) {
 	this.applicantName = applicantName;
 }

 /**
 *请求人姓名
 * @return
 */
 public String getApplicantName() {
 	return applicantName;
 }

 /**
 *请求人所在公司
 * @param applicantCom
 */
 public void setApplicantCom(String applicantCom) {
 	this.applicantCom = applicantCom;
 }

 /**
 *请求人所在公司
 * @return
 */
 public String getApplicantCom() {
 	return applicantCom;
 }

 /**
 *请求人所在部门
 * @param applicantDepName
 */
 public void setApplicantDepName(String applicantDepName) {
 	this.applicantDepName = applicantDepName;
 }

 /**
 *请求人所在部门
 * @return
 */
 public String getApplicantDepName() {
 	return applicantDepName;
 }

 /**
 *请求人email
 * @param applicantEmail
 */
 public void setApplicantEmail(String applicantEmail) {
 	this.applicantEmail = applicantEmail;
 }

 /**
 *请求人email
 * @return
 */
 public String getApplicantEmail() {
 	return applicantEmail;
 }

 /**
 *请求人办公电话
 * @param applicantLinePhone
 */
 public void setApplicantLinePhone(String applicantLinePhone) {
 	this.applicantLinePhone = applicantLinePhone;
 }

 /**
 *请求人办公电话
 * @return
 */
 public String getApplicantLinePhone() {
 	return applicantLinePhone;
 }

 /**
 *请求人移动电话
 * @param applicantMovePhone
 */
 public void setApplicantMovePhone(String applicantMovePhone) {
 	this.applicantMovePhone = applicantMovePhone;
 }

 /**
 *请求人移动电话
 * @return
 */
 public String getApplicantMovePhone() {
 	return applicantMovePhone;
 }

 /**
 *事件发生的地点
 * @param eventAddress
 */
 public void setEventAddress(String eventAddress) {
 	this.eventAddress = eventAddress;
 }

 /**
 *事件发生的地点
 * @return
 */
 public String getEventAddress() {
 	return eventAddress;
 }

 /**
 *事件发生时间
 * @param eventTime
 */
 public void setEventTime(String eventTime) {
 	this.eventTime = eventTime;
 }

 /**
 *事件发生时间
 * @return
 */
 public String getEventTime() {
 	return eventTime;
 }

 /**
 *事件性质
 * @param eventAttr
 */
 public void setEventAttr(String eventAttr) {
 	this.eventAttr = eventAttr;
 }

 /**
 *事件性质
 * @return
 */
 public String getEventAttr() {
 	return eventAttr;
 }

 /**
 *事件来源
 * @param eventSource
 */
 public void setEventSource(String eventSource) {
 	this.eventSource = eventSource;
 }

 /**
 *事件来源
 * @return
 */
 public String getEventSource() {
 	return eventSource;
 }

 /**
 *服务（事件）影响度
 * @param eventInfluenceDegree
 */
 public void setEventInfluenceDegree(String eventInfluenceDegree) {
 	this.eventInfluenceDegree = eventInfluenceDegree;
 }

 /**
 *服务（事件）影响度
 * @return
 */
 public String getEventInfluenceDegree() {
 	return eventInfluenceDegree;
 }

 /**
 *服务（事件）优先级
 * @param eventPriorityDegree
 */
 public void setEventPriorityDegree(String eventPriorityDegree) {
 	this.eventPriorityDegree = eventPriorityDegree;
 }

 /**
 *服务（事件）优先级
 * @return
 */
 public String getEventPriorityDegree() {
 	return eventPriorityDegree;
 }

 /**
 *所属系统类型
 * @param sysType
 */
 public void setSysType(String sysType) {
 	this.sysType = sysType;
 }

 /**
 *所属系统类型
 * @return
 */
 public String getSysType() {
 	return sysType;
 }

 /**
 *事件分类
 * @param eventType
 */
 public void setEventType(String eventType) {
 	this.eventType = eventType;
 }

 /**
 *事件分类
 * @return
 */
 public String getEventType() {
 	return eventType;
 }

 /**
 *（事件）标题
 * @param eventTitle
 */
 public void setEventTitle(String eventTitle) {
 	this.eventTitle = eventTitle;
 }

 /**
 *（事件）标题
 * @return
 */
 public String getEventTitle() {
 	return eventTitle;
 }

 /**
 *（事件）描述
 * @param eventRemark
 */
 public void setEventRemark(String eventRemark) {
 	this.eventRemark = eventRemark;
 }

 /**
 *（事件）描述
 * @return
 */
 public String getEventRemark() {
 	return eventRemark;
 }

 /**
 *事件解决人
 * @param eventTerminator
 */
 public void setEventTerminator(String eventTerminator) {
 	this.eventTerminator = eventTerminator;
 }

 /**
 *事件解决人
 * @return
 */
 public String getEventTerminator() {
 	return eventTerminator;
 }

 /**
 *事件状态
 * @param eventStatus
 */
 public void setEventStatus(String eventStatus) {
 	this.eventStatus = eventStatus;
 }

 /**
 *事件状态
 * @return
 */
 public String getEventStatus() {
 	return eventStatus;
 }

 /**
 *事件解决方案
 * @param eventSolution 
 */
 public void setEventSolution (String eventSolution ) {
 	this.eventSolution  = eventSolution ;
 }

 /**
 *事件解决方案
 * @return
 */
 public String getEventSolution () {
 	return eventSolution ;
 }

 /**
 *事件结束代码
 * @param eventEndCode
 */
 public void setEventEndCode(String eventEndCode) {
 	this.eventEndCode = eventEndCode;
 }

 /**
 *事件结束代码
 * @return
 */
 public String getEventEndCode() {
 	return eventEndCode;
 }

 /**
 *重复事件标记
 * @param eventRepetitionMark
 */
 public void setEventRepetitionMark(String eventRepetitionMark) {
 	this.eventRepetitionMark = eventRepetitionMark;
 }

 /**
 *重复事件标记
 * @return
 */
 public String getEventRepetitionMark() {
 	return eventRepetitionMark;
 }

 /**
 *记录事件状态到处理中的时间（系统自动产生）
 * @param eventStartTimes
 */
 public void setEventStartTimes(Integer eventStartTimes) {
 	this.eventStartTimes = eventStartTimes;
 }

 /**
 *记录事件状态到处理中的时间（系统自动产生）
 * @return
 */
 public Integer getEventStartTimes() {
 	return eventStartTimes;
 }

 /**
 *记录事件已解决的时间（系统自动产生）
 * @param eventEndTimes
 */
 public void setEventEndTimes(Integer eventEndTimes) {
 	this.eventEndTimes = eventEndTimes;
 }

 /**
 *记录事件已解决的时间（系统自动产生）
 * @return
 */
 public Integer getEventEndTimes() {
 	return eventEndTimes;
 }

 /**
 *记录出现故障的配置项代码
 * @param eventBugRemark
 */
 public void setEventBugRemark(String eventBugRemark) {
 	this.eventBugRemark = eventBugRemark;
 }

 /**
 *记录出现故障的配置项代码
 * @return
 */
 public String getEventBugRemark() {
 	return eventBugRemark;
 }

 /**
 *业务类型，列值与BusinessTypeConstant常量一一对应
 * @param busType
 */
 public void setBusType(String busType) {
 	this.busType = busType;
 }

 /**
 *业务类型，列值与BusinessTypeConstant常量一一对应
 * @return
 */
 public String getBusType() {
 	return busType;
 }

 /**
 *业务主键（关联的变更单号）
 * @param busId
 */
 public void setBusId(Integer busId) {
 	this.busId = busId;
 }

 /**
 *业务主键（关联的变更单号）
 * @return
 */
 public Integer getBusId() {
 	return busId;
 }

 /**
 *事件完成期限
 * @param eventLimitDate
 */
 public void setEventLimitDate(String eventLimitDate) {
 	this.eventLimitDate = eventLimitDate;
 }

 /**
 *事件完成期限
 * @return
 */
 public String getEventLimitDate() {
 	return eventLimitDate;
 }

}
