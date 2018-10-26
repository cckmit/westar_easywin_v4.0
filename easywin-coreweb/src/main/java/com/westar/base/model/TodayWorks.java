package com.westar.base.model;

import org.apache.commons.lang3.StringUtils;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.core.web.DataDicContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 数据更新记录列表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class TodayWorks {
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
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 更新人员主键
	*/
	@Filed
	private Integer modifyer;
	/** 
	* 更新内容
	*/
	@Filed
	private String content;
	/** 
	* 业务类别 0普通 1审批 2聊天
	*/
	@Filed
	private Integer busSpec;
	/** 
	* 是否已读 0未读 1已读
	*/
	@Filed
	private Integer readState;
	/** 
	* 是否闹铃发送 0不是 1是
	*/
	@Filed
	private Integer isClock;
	/** 
	* 闹铃主键
	*/
	@Filed
	private Integer clockId;
	/** 
	* 聊天室主键
	*/
	@Filed
	private Integer roomId;

	/****************以上主要为系统表字段********************/
	/** 
	* 业务名称
	*/
	private String busTypeName;
	/** 
	* 模块名称
	*/
	@SuppressWarnings("unused")
	private String moduleTypeName;
	/** 
	* 合计
	*/
	private int sum;
	private String modifyerName;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 附件UUID
	*/
	private String modifyerUuid;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 任务办理时限
	*/
	private String dealTimeLimit;
	/** 
	* 关注状态0未关注1已关注
	*/
	private String attentionState;
	/** 
	* 附件名
	*/
	private String fileName;
	/** 
	* 下一条的主键
	*/
	private Integer nextObjId;
	/** 
	* 任务的紧急程度
	*/
	private String grade;
	/** 
	* 页面已经展示的代办主键
	*/
	private String todoIds;
	/** 
	* 消息的所属人员姓名
	*/
	private String userName;
	/** 
	* 消息人员所属部门
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
	* @return
	*/
	public String getBusTypeName() {
		return busTypeName;
	}

	/** 
	* 业务名称
	* @param busTypeName
	*/
	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}

	/** 
	* 合计
	* @return
	*/
	public int getSum() {
		return sum;
	}

	/** 
	* 合计
	* @param sum
	*/
	public void setSum(int sum) {
		this.sum = sum;
	}

	/** 
	* 人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 模块名称
	* @return
	*/
	public String getModuleTypeName() {
		if (StringUtils.isEmpty(moduleTypeName)) {
			return DataDicContext.getInstance().getCurrentPathZvalue("moduleType", busType);
		} else {
			return moduleTypeName;
		}
	}

	/** 
	* 模块名称
	* @param moduleTypeName
	*/
	public void setModuleTypeName(String moduleTypeName) {
		this.moduleTypeName = moduleTypeName;
	}

	/** 
	* 更新人员主键
	* @param modifyer
	*/
	public void setModifyer(Integer modifyer) {
		this.modifyer = modifyer;
	}

	/** 
	* 更新人员主键
	* @return
	*/
	public Integer getModifyer() {
		return modifyer;
	}

	/** 
	* 更新内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 更新内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	public String getModifyerName() {
		return modifyerName;
	}

	public void setModifyerName(String modifyerName) {
		this.modifyerName = modifyerName;
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
	* 附件UUID
	* @return
	*/
	public String getModifyerUuid() {
		return modifyerUuid;
	}

	/** 
	* 附件UUID
	* @param modifyerUuid
	*/
	public void setModifyerUuid(String modifyerUuid) {
		this.modifyerUuid = modifyerUuid;
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
	* 业务类别 0普通 1审批 2聊天
	* @param busSpec
	*/
	public void setBusSpec(Integer busSpec) {
		this.busSpec = busSpec;
	}

	/** 
	* 业务类别 0普通 1审批 2聊天
	* @return
	*/
	public Integer getBusSpec() {
		return busSpec;
	}

	/** 
	* 是否已读 0未读 1已读
	* @param readState
	*/
	public void setReadState(Integer readState) {
		this.readState = readState;
	}

	/** 
	* 是否已读 0未读 1已读
	* @return
	*/
	public Integer getReadState() {
		return readState;
	}

	/** 
	* 任务办理时限
	* @return
	*/
	public String getDealTimeLimit() {
		return dealTimeLimit;
	}

	/** 
	* 任务办理时限
	* @param dealTimeLimit
	*/
	public void setDealTimeLimit(String dealTimeLimit) {
		this.dealTimeLimit = dealTimeLimit;
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
	* 附件名
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 附件名
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	* 下一条的主键
	* @return
	*/
	public Integer getNextObjId() {
		return nextObjId;
	}

	/** 
	* 下一条的主键
	* @param nextObjId
	*/
	public void setNextObjId(Integer nextObjId) {
		this.nextObjId = nextObjId;
	}

	/** 
	* 任务的紧急程度
	* @return
	*/
	public String getGrade() {
		return grade;
	}

	/** 
	* 任务的紧急程度
	* @param grade
	*/
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/** 
	* 是否闹铃发送 0不是 1是
	* @param isClock
	*/
	public void setIsClock(Integer isClock) {
		this.isClock = isClock;
	}

	/** 
	* 是否闹铃发送 0不是 1是
	* @return
	*/
	public Integer getIsClock() {
		return isClock;
	}

	/** 
	* 闹铃主键
	* @param clockId
	*/
	public void setClockId(Integer clockId) {
		this.clockId = clockId;
	}

	/** 
	* 闹铃主键
	* @return
	*/
	public Integer getClockId() {
		return clockId;
	}

	/** 
	* 聊天室主键
	* @param roomId
	*/
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	/** 
	* 聊天室主键
	* @return
	*/
	public Integer getRoomId() {
		return roomId;
	}

	/** 
	* 页面已经展示的代办主键
	* @return
	*/
	public String getTodoIds() {
		return todoIds;
	}

	/** 
	* 页面已经展示的代办主键
	* @param todoIds
	*/
	public void setTodoIds(String todoIds) {
		this.todoIds = todoIds;
	}

	/** 
	* 消息的所属人员姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 消息的所属人员姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 消息人员所属部门
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 消息人员所属部门
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}
}
