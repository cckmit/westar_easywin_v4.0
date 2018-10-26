package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 分享
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Daily {
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
	* 汇报人
	*/
	@Filed
	private Integer reporterId;
	/** 
	* 分享名称
	*/
	@Filed
	private String dailyName;
	/** 
	* 是否填写今日计划1是0否
	*/
	@Filed
	private String hasPlan;
	/** 
	* 状态 0表示已完成 1表示待办
	*/
	@Filed
	private String state;
	/** 
	* 分享日期
	*/
	@Filed
	private String dailyDate;
	/** 
	* 使用模板版本
	*/
	@Filed
	private Integer version;
	/** 
	* 状态 0:公开;1:自定义;2:私有
	*/
	@Filed
	private Integer scopeType;

	/****************以上主要为系统表字段********************/
	/** 
	* 所选日期
	*/
	private String chooseDate;
	/** 
	* 分享条目
	*/
	private List<DailyQ> dailyQs;
	/** 
	* 下日计划
	*/
	private List<DailyPlan> dailyPlans;
	/** 
	* 分享条目值
	*/
	private List<DailyVal> dailyVals;
	/** 
	* 分享分享范围
	*/
	private List<ShareGroup> shareGroup;
	/** 
	* 分享已填写条数
	*/
	private Integer countVal;
	/** 
	* 分享已初始化条目数
	*/
	private Integer countQues;
	/** 
	* 分享提交人
	*/
	private String userName;
	/** 
	* 分享提交人所在部门
	*/
	private Integer depId;
	/** 
	* 分享提交人所在部门名称
	*/
	private String depName;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 分享类型
	*/
	private Integer shareType;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	private Integer isShare;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 是否已经查看0，没有，1已查看
	*/
	private Integer isRead;
	/** 
	* 前一条数据的Id
	*/
	private Integer preId;
	/** 
	* 后一条数据的Id
	*/
	private Integer nextId;
	/** 
	* 分享留言集合
	*/
	private List<DailyTalk> listWeekTalk;
	/** 
	* 分享浏览的人员集合
	*/
	private List<ViewRecord> listViewRecord;
	/** 
	* 分享日志集合
	*/
	private List<DailyLog> listDailyLog;
	/** 
	* 本日已回报数
	*/
	private Integer repNum;
	/** 
	* 本日未回报数
	*/
	private Integer unRepNum;
	/** 
	* 分享数
	*/
	private Integer allRepNum;
	/** 
	* 我的汇报数
	*/
	private Integer myRepNum;
	/** 
	* 下属汇报数
	*/
	private Integer subRepNum;
	/** 
	* 分享汇报内容
	*/
	private String content;
	/** 
	* 留言总数
	*/
	private Integer allMsgNum;
	/** 
	* 本日做了什么
	*/
	private String weeklyDone;
	/** 
	* 所遇到的麻烦
	*/
	private String weeklyTrouble;
	/** 
	* 日计划是什么
	*/
	private String weeklyPlan;
	/** 
	* 验证结果（f1上级和是否f2为新入职的）
	*/
	private List<String> validateStatus;
	/** 
	* 汇报人
	*/
	private List<UserInfo> listOwner;
	/** 
	* 发布状态0未发布1已发布2延迟发布
	*/
	private Integer submitState;
	private String createDate;
	/** 
	* 分享附件
	*/
	private List<DailyUpfiles> dailyFiles;
	/** 
	* 分享组
	*/
	private List<DailyShareGroup> listDailyShareGroup;
	/** 
	* 分享组名称字符串
	*/
	private String grpNameStr;
	/** 
	* 分享组id字符串
	*/
	private String grpIdStr;
	/** 
	* 分享 类型字符串
	*/
	private String scopeStr;
	/** 
	* 留言总数（显示数量用）
	*/
	private Integer talkNum;
	/** 
	* 附件总数（显示数量用）
	*/
	private Integer fileNum;
	/** 
	* 个人组值
	*/
	private String scopeTypeStr;
	/** 
	* 个人分组显示字符串
	*/
	private String scopeTypeSel;
	/** 
	* 关注状态
	*/
	private String attentionState;
	/** 
	* 本日已汇报0本日未汇报1
	*/
	private String dailyDoneState;
	/** 
	* 本部门和下级部门
	*/
	private Integer[] listTreeDeps;
	/** 
	* 查询的年
	*/
	private Integer dailyYear;
	/** 
	* 部门集合
	*/
	private List<Department> listDep;
	/** 
	* 汇报人数组
	*/
	private Integer[] ownerArray;

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
	* 汇报人
	* @param reporterId
	*/
	public void setReporterId(Integer reporterId) {
		this.reporterId = reporterId;
	}

	/** 
	* 汇报人
	* @return
	*/
	public Integer getReporterId() {
		return reporterId;
	}

	/** 
	* 分享名称
	* @param dailyName
	*/
	public void setDailyName(String dailyName) {
		this.dailyName = dailyName;
	}

	/** 
	* 分享名称
	* @return
	*/
	public String getDailyName() {
		return dailyName;
	}

	/** 
	* 是否填写今日计划1是0否
	* @param hasPlan
	*/
	public void setHasPlan(String hasPlan) {
		this.hasPlan = hasPlan;
	}

	/** 
	* 是否填写今日计划1是0否
	* @return
	*/
	public String getHasPlan() {
		return hasPlan;
	}

	/** 
	* 状态 0表示已完成 1表示待办
	* @param state
	*/
	public void setState(String state) {
		this.state = state;
	}

	/** 
	* 状态 0表示已完成 1表示待办
	* @return
	*/
	public String getState() {
		return state;
	}

	/** 
	* 分享日期
	* @param dailyDate
	*/
	public void setDailyDate(String dailyDate) {
		this.dailyDate = dailyDate;
	}

	/** 
	* 分享日期
	* @return
	*/
	public String getDailyDate() {
		return dailyDate;
	}

	/** 
	* 使用模板版本
	* @param version
	*/
	public void setVersion(Integer version) {
		this.version = version;
	}

	/** 
	* 使用模板版本
	* @return
	*/
	public Integer getVersion() {
		return version;
	}

	/** 
	* 所选日期
	* @return
	*/
	public String getChooseDate() {
		return chooseDate;
	}

	/** 
	* 所选日期
	* @param chooseDate
	*/
	public void setChooseDate(String chooseDate) {
		this.chooseDate = chooseDate;
	}

	/** 
	* 分享条目
	* @return
	*/
	public List<DailyQ> getDailyQs() {
		return dailyQs;
	}

	/** 
	* 分享条目
	* @param dailyQs
	*/
	public void setDailyQs(List<DailyQ> dailyQs) {
		this.dailyQs = dailyQs;
	}

	/** 
	* 下日计划
	* @return
	*/
	public List<DailyPlan> getDailyPlans() {
		return dailyPlans;
	}

	/** 
	* 下日计划
	* @param dailyPlans
	*/
	public void setDailyPlans(List<DailyPlan> dailyPlans) {
		this.dailyPlans = dailyPlans;
	}

	/** 
	* 分享条目值
	* @return
	*/
	public List<DailyVal> getDailyVals() {
		return dailyVals;
	}

	/** 
	* 分享条目值
	* @param dailyVals
	*/
	public void setDailyVals(List<DailyVal> dailyVals) {
		this.dailyVals = dailyVals;
	}

	/** 
	* 分享分享范围
	* @return
	*/
	public List<ShareGroup> getShareGroup() {
		return shareGroup;
	}

	/** 
	* 分享分享范围
	* @param shareGroup
	*/
	public void setShareGroup(List<ShareGroup> shareGroup) {
		this.shareGroup = shareGroup;
	}

	/** 
	* 分享已填写条数
	* @return
	*/
	public Integer getCountVal() {
		return countVal;
	}

	/** 
	* 分享已填写条数
	* @param countVal
	*/
	public void setCountVal(Integer countVal) {
		this.countVal = countVal;
	}

	/** 
	* 分享已初始化条目数
	* @return
	*/
	public Integer getCountQues() {
		return countQues;
	}

	/** 
	* 分享已初始化条目数
	* @param countQues
	*/
	public void setCountQues(Integer countQues) {
		this.countQues = countQues;
	}

	/** 
	* 分享提交人
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 分享提交人
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 分享提交人所在部门
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 分享提交人所在部门
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	/** 
	* 分享提交人所在部门名称
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 分享提交人所在部门名称
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
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
	* 分享类型
	* @return
	*/
	public Integer getShareType() {
		return shareType;
	}

	/** 
	* 分享类型
	* @param shareType
	*/
	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}

	/** 
	* 标识，作用暂不明
	* @return
	*/
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

	public Integer getIsShare() {
		return isShare;
	}

	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
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
	* 是否已经查看0，没有，1已查看
	* @return
	*/
	public Integer getIsRead() {
		return isRead;
	}

	/** 
	* 是否已经查看0，没有，1已查看
	* @param isRead
	*/
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	/** 
	* 前一条数据的Id
	* @return
	*/
	public Integer getPreId() {
		return preId;
	}

	/** 
	* 前一条数据的Id
	* @param preId
	*/
	public void setPreId(Integer preId) {
		this.preId = preId;
	}

	/** 
	* 后一条数据的Id
	* @return
	*/
	public Integer getNextId() {
		return nextId;
	}

	/** 
	* 后一条数据的Id
	* @param nextId
	*/
	public void setNextId(Integer nextId) {
		this.nextId = nextId;
	}

	/** 
	* 分享留言集合
	* @return
	*/
	public List<DailyTalk> getListWeekTalk() {
		return listWeekTalk;
	}

	/** 
	* 分享留言集合
	* @param listWeekTalk
	*/
	public void setListWeekTalk(List<DailyTalk> listWeekTalk) {
		this.listWeekTalk = listWeekTalk;
	}

	/** 
	* 分享浏览的人员集合
	* @return
	*/
	public List<ViewRecord> getListViewRecord() {
		return listViewRecord;
	}

	/** 
	* 分享浏览的人员集合
	* @param listViewRecord
	*/
	public void setListViewRecord(List<ViewRecord> listViewRecord) {
		this.listViewRecord = listViewRecord;
	}

	/** 
	* 分享日志集合
	* @return
	*/
	public List<DailyLog> getListDailyLog() {
		return listDailyLog;
	}

	/** 
	* 分享日志集合
	* @param listDailyLog
	*/
	public void setListDailyLog(List<DailyLog> listDailyLog) {
		this.listDailyLog = listDailyLog;
	}

	/** 
	* 本日已回报数
	* @return
	*/
	public Integer getRepNum() {
		return repNum;
	}

	/** 
	* 本日已回报数
	* @param repNum
	*/
	public void setRepNum(Integer repNum) {
		this.repNum = repNum;
	}

	/** 
	* 本日未回报数
	* @return
	*/
	public Integer getUnRepNum() {
		return unRepNum;
	}

	/** 
	* 本日未回报数
	* @param unRepNum
	*/
	public void setUnRepNum(Integer unRepNum) {
		this.unRepNum = unRepNum;
	}

	/** 
	* 分享数
	* @return
	*/
	public Integer getAllRepNum() {
		return allRepNum;
	}

	/** 
	* 分享数
	* @param allRepNum
	*/
	public void setAllRepNum(Integer allRepNum) {
		this.allRepNum = allRepNum;
	}

	/** 
	* 我的汇报数
	* @return
	*/
	public Integer getMyRepNum() {
		return myRepNum;
	}

	/** 
	* 我的汇报数
	* @param myRepNum
	*/
	public void setMyRepNum(Integer myRepNum) {
		this.myRepNum = myRepNum;
	}

	/** 
	* 下属汇报数
	* @return
	*/
	public Integer getSubRepNum() {
		return subRepNum;
	}

	/** 
	* 下属汇报数
	* @param subRepNum
	*/
	public void setSubRepNum(Integer subRepNum) {
		this.subRepNum = subRepNum;
	}

	/** 
	* 分享汇报内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 分享汇报内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 留言总数
	* @return
	*/
	public Integer getAllMsgNum() {
		return allMsgNum;
	}

	/** 
	* 留言总数
	* @param allMsgNum
	*/
	public void setAllMsgNum(Integer allMsgNum) {
		this.allMsgNum = allMsgNum;
	}

	/** 
	* 本日做了什么
	* @return
	*/
	public String getWeeklyDone() {
		return weeklyDone;
	}

	/** 
	* 本日做了什么
	* @param weeklyDone
	*/
	public void setWeeklyDone(String weeklyDone) {
		this.weeklyDone = weeklyDone;
	}

	/** 
	* 所遇到的麻烦
	* @return
	*/
	public String getWeeklyTrouble() {
		return weeklyTrouble;
	}

	/** 
	* 所遇到的麻烦
	* @param weeklyTrouble
	*/
	public void setWeeklyTrouble(String weeklyTrouble) {
		this.weeklyTrouble = weeklyTrouble;
	}

	/** 
	* 日计划是什么
	* @return
	*/
	public String getWeeklyPlan() {
		return weeklyPlan;
	}

	/** 
	* 日计划是什么
	* @param weeklyPlan
	*/
	public void setWeeklyPlan(String weeklyPlan) {
		this.weeklyPlan = weeklyPlan;
	}

	/** 
	* 验证结果（f1上级和是否f2为新入职的）
	* @return
	*/
	public List<String> getValidateStatus() {
		return validateStatus;
	}

	/** 
	* 验证结果（f1上级和是否f2为新入职的）
	* @param validateStatus
	*/
	public void setValidateStatus(List<String> validateStatus) {
		this.validateStatus = validateStatus;
	}

	/** 
	* 汇报人
	* @return
	*/
	public List<UserInfo> getListOwner() {
		return listOwner;
	}

	/** 
	* 汇报人
	* @param listOwner
	*/
	public void setListOwner(List<UserInfo> listOwner) {
		this.listOwner = listOwner;
	}

	/** 
	* 发布状态0未发布1已发布2延迟发布
	* @return
	*/
	public Integer getSubmitState() {
		return submitState;
	}

	/** 
	* 发布状态0未发布1已发布2延迟发布
	* @param submitState
	*/
	public void setSubmitState(Integer submitState) {
		this.submitState = submitState;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/** 
	* 分享附件
	* @return
	*/
	public List<DailyUpfiles> getDailyFiles() {
		return dailyFiles;
	}

	/** 
	* 分享附件
	* @param dailyFiles
	*/
	public void setDailyFiles(List<DailyUpfiles> dailyFiles) {
		this.dailyFiles = dailyFiles;
	}

	/** 
	* 分享组
	* @return
	*/
	public List<DailyShareGroup> getListDailyShareGroup() {
		return listDailyShareGroup;
	}

	/** 
	* 分享组
	* @param listDailyShareGroup
	*/
	public void setListDailyShareGroup(List<DailyShareGroup> listDailyShareGroup) {
		this.listDailyShareGroup = listDailyShareGroup;
	}

	/** 
	* 分享组名称字符串
	* @return
	*/
	public String getGrpNameStr() {
		return grpNameStr;
	}

	/** 
	* 分享组名称字符串
	* @param grpNameStr
	*/
	public void setGrpNameStr(String grpNameStr) {
		this.grpNameStr = grpNameStr;
	}

	/** 
	* 分享组id字符串
	* @return
	*/
	public String getGrpIdStr() {
		return grpIdStr;
	}

	/** 
	* 分享组id字符串
	* @param grpIdStr
	*/
	public void setGrpIdStr(String grpIdStr) {
		this.grpIdStr = grpIdStr;
	}

	/** 
	* 分享 类型字符串
	* @return
	*/
	public String getScopeStr() {
		return scopeStr;
	}

	/** 
	* 分享 类型字符串
	* @param scopeStr
	*/
	public void setScopeStr(String scopeStr) {
		this.scopeStr = scopeStr;
	}

	/** 
	* 留言总数（显示数量用）
	* @return
	*/
	public Integer getTalkNum() {
		return talkNum;
	}

	/** 
	* 留言总数（显示数量用）
	* @param talkNum
	*/
	public void setTalkNum(Integer talkNum) {
		this.talkNum = talkNum;
	}

	/** 
	* 附件总数（显示数量用）
	* @return
	*/
	public Integer getFileNum() {
		return fileNum;
	}

	/** 
	* 附件总数（显示数量用）
	* @param fileNum
	*/
	public void setFileNum(Integer fileNum) {
		this.fileNum = fileNum;
	}

	/** 
	* 状态 0:公开;1:自定义;2:私有
	* @param scopeType
	*/
	public void setScopeType(Integer scopeType) {
		this.scopeType = scopeType;
	}

	/** 
	* 状态 0:公开;1:自定义;2:私有
	* @return
	*/
	public Integer getScopeType() {
		return scopeType;
	}

	/** 
	* 个人组值
	* @return
	*/
	public String getScopeTypeStr() {
		return scopeTypeStr;
	}

	/** 
	* 个人组值
	* @param scopeTypeStr
	*/
	public void setScopeTypeStr(String scopeTypeStr) {
		this.scopeTypeStr = scopeTypeStr;
	}

	/** 
	* 个人分组显示字符串
	* @return
	*/
	public String getScopeTypeSel() {
		return scopeTypeSel;
	}

	/** 
	* 个人分组显示字符串
	* @param scopeTypeSel
	*/
	public void setScopeTypeSel(String scopeTypeSel) {
		this.scopeTypeSel = scopeTypeSel;
	}

	/** 
	* 关注状态
	* @return
	*/
	public String getAttentionState() {
		return attentionState;
	}

	/** 
	* 关注状态
	* @param attentionState
	*/
	public void setAttentionState(String attentionState) {
		this.attentionState = attentionState;
	}

	/** 
	* 本日已汇报0本日未汇报1
	* @return
	*/
	public String getDailyDoneState() {
		return dailyDoneState;
	}

	/** 
	* 本日已汇报0本日未汇报1
	* @param dailyDoneState
	*/
	public void setDailyDoneState(String dailyDoneState) {
		this.dailyDoneState = dailyDoneState;
	}

	/** 
	* 本部门和下级部门
	* @return
	*/
	public Integer[] getListTreeDeps() {
		return listTreeDeps;
	}

	/** 
	* 本部门和下级部门
	* @param listTreeDeps
	*/
	public void setListTreeDeps(Integer[] listTreeDeps) {
		this.listTreeDeps = listTreeDeps;
	}

	/** 
	* 查询的年
	* @return
	*/
	public Integer getDailyYear() {
		return dailyYear;
	}

	/** 
	* 查询的年
	* @param dailyYear
	*/
	public void setDailyYear(Integer dailyYear) {
		this.dailyYear = dailyYear;
	}

	/** 
	* 部门集合
	* @return
	*/
	public List<Department> getListDep() {
		return listDep;
	}

	/** 
	* 部门集合
	* @param listDep
	*/
	public void setListDep(List<Department> listDep) {
		this.listDep = listDep;
	}

	/** 
	* 汇报人数组
	* @return
	*/
	public Integer[] getOwnerArray() {
		return ownerArray;
	}

	/** 
	* 汇报人数组
	* @param ownerArray
	*/
	public void setOwnerArray(Integer[] ownerArray) {
		this.ownerArray = ownerArray;
	}
}
