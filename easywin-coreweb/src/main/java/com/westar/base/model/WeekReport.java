package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 周报
 */
@Table
@JsonInclude(Include.NON_NULL)
public class WeekReport {
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
	* 周报名称
	*/
	@Filed
	private String weekRepName;
	/** 
	* 周数
	*/
	@Filed
	private Integer weekNum;
	/** 
	* 年
	*/
	@Filed
	private Integer year;
	/** 
	* 是否填写下周计划1是0否
	*/
	@Filed
	private String hasPlan;
	/** 
	* 状态 0表示已完成 1表示待办
	*/
	@Filed
	private String state;
	/** 
	* 一周的第一天
	*/
	@Filed
	private String weekS;
	/** 
	* 一周的最后天
	*/
	@Filed
	private String weekE;
	/** 
	* 使用模板版本
	*/
	@Filed
	private Integer version;

	/****************以上主要为系统表字段********************/
	/** 
	* 所选日期
	*/
	private String chooseDate;
	/** 
	* 周报条目
	*/
	private List<WeekReportQ> weekReportQs;
	/** 
	* 下周计划
	*/
	private List<WeekReportPlan> weekReportPlans;
	/** 
	* 周报条目值
	*/
	private List<WeekReportVal> weekReportVals;
	/** 
	* 周报附件
	*/
	private List<WeekRepUpfiles> weekReportFiles;
	/** 
	* 周报分享范围
	*/
	private List<ShareGroup> shareGroup;
	/** 
	* 周报已填写条数
	*/
	private Integer countVal;
	/** 
	* 周报已初始化条目数
	*/
	private Integer countQues;
	/** 
	* 周报提交人
	*/
	private String userName;
	/** 
	* 周报提交人所在部门
	*/
	private Integer depId;
	/** 
	* 周报提交人所在部门
	*/
	private String depName;
	/** 
	* 附件名称
	*/
	private String ImgName;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;
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
	* 周报留言集合
	*/
	private List<WeekRepTalk> listWeekTalk;
	/** 
	* 周报浏览的人员集合
	*/
	private List<ViewRecord> listViewRecord;
	/** 
	* 周报日志集合
	*/
	private List<WeekRepLog> listWeekRepLog;
	/** 
	* 本周已回报数
	*/
	private Integer repNum;
	/** 
	* 本周未回报数
	*/
	private Integer unRepNum;
	/** 
	* 周报数
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
	* 周报汇报内容
	*/
	private String content;
	/** 
	* 留言总数
	*/
	private Integer allMsgNum;
	/** 
	* 本周做了什么
	*/
	private String weeklyDone;
	/** 
	* 所遇到的麻烦
	*/
	private String weeklyTrouble;
	/** 
	* 周计划是什么
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
	* 留言总数（显示数量用）
	*/
	private Integer talkNum;
	/** 
	* 附件总数（显示数量用）
	*/
	private Integer fileNum;

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
	* 周报名称
	* @param weekRepName
	*/
	public void setWeekRepName(String weekRepName) {
		this.weekRepName = weekRepName;
	}

	/** 
	* 周报名称
	* @return
	*/
	public String getWeekRepName() {
		return weekRepName;
	}

	/** 
	* 周数
	* @param weekNum
	*/
	public void setWeekNum(Integer weekNum) {
		this.weekNum = weekNum;
	}

	/** 
	* 周数
	* @return
	*/
	public Integer getWeekNum() {
		return weekNum;
	}

	/** 
	* 年
	* @param year
	*/
	public void setYear(Integer year) {
		this.year = year;
	}

	/** 
	* 年
	* @return
	*/
	public Integer getYear() {
		return year;
	}

	/** 
	* 是否填写下周计划1是0否
	* @param hasPlan
	*/
	public void setHasPlan(String hasPlan) {
		this.hasPlan = hasPlan;
	}

	/** 
	* 是否填写下周计划1是0否
	* @return
	*/
	public String getHasPlan() {
		return hasPlan;
	}

	/** 
	* 周报条目
	* @return
	*/
	public List<WeekReportQ> getWeekReportQs() {
		return weekReportQs;
	}

	/** 
	* 周报条目
	* @param weekReportQs
	*/
	public void setWeekReportQs(List<WeekReportQ> weekReportQs) {
		this.weekReportQs = weekReportQs;
	}

	/** 
	* 下周计划
	* @return
	*/
	public List<WeekReportPlan> getWeekReportPlans() {
		return weekReportPlans;
	}

	/** 
	* 下周计划
	* @param weekReportPlans
	*/
	public void setWeekReportPlans(List<WeekReportPlan> weekReportPlans) {
		this.weekReportPlans = weekReportPlans;
	}

	/** 
	* 周报已填写条数
	* @return
	*/
	public Integer getCountVal() {
		return countVal;
	}

	/** 
	* 周报已填写条数
	* @param countVal
	*/
	public void setCountVal(Integer countVal) {
		this.countVal = countVal;
	}

	/** 
	* 周报已初始化条目数
	* @return
	*/
	public Integer getCountQues() {
		return countQues;
	}

	/** 
	* 周报已初始化条目数
	* @param countQues
	*/
	public void setCountQues(Integer countQues) {
		this.countQues = countQues;
	}

	/** 
	* 周报条目值
	* @return
	*/
	public List<WeekReportVal> getWeekReportVals() {
		return weekReportVals;
	}

	/** 
	* 周报条目值
	* @param weekReportVals
	*/
	public void setWeekReportVals(List<WeekReportVal> weekReportVals) {
		this.weekReportVals = weekReportVals;
	}

	/** 
	* 周报附件
	* @return
	*/
	public List<WeekRepUpfiles> getWeekReportFiles() {
		return weekReportFiles;
	}

	/** 
	* 周报附件
	* @param weekReportFiles
	*/
	public void setWeekReportFiles(List<WeekRepUpfiles> weekReportFiles) {
		this.weekReportFiles = weekReportFiles;
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

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}

	public Integer getShareType() {
		return shareType;
	}

	/** 
	* 周报提交人
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 周报提交人
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 周报提交人所在部门
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 周报提交人所在部门
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
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
	* 附件名称
	* @return
	*/
	public String getImgName() {
		return ImgName;
	}

	/** 
	* 附件名称
	* @param ImgName
	*/
	public void setImgName(String imgName) {
		ImgName = imgName;
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
	* 一周的第一天
	* @param weekS
	*/
	public void setWeekS(String weekS) {
		this.weekS = weekS;
	}

	/** 
	* 一周的第一天
	* @return
	*/
	public String getWeekS() {
		return weekS;
	}

	/** 
	* 一周的最后天
	* @param weekE
	*/
	public void setWeekE(String weekE) {
		this.weekE = weekE;
	}

	/** 
	* 一周的最后天
	* @return
	*/
	public String getWeekE() {
		return weekE;
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

	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}

	public Integer getIsShare() {
		return isShare;
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
	* 周报分享范围
	* @return
	*/
	public List<ShareGroup> getShareGroup() {
		return shareGroup;
	}

	/** 
	* 周报分享范围
	* @param shareGroup
	*/
	public void setShareGroup(List<ShareGroup> shareGroup) {
		this.shareGroup = shareGroup;
	}

	public List<ShareGroup> getShareGroups() {
		return shareGroup;
	}

	public void setShareGroups(List<ShareGroup> shareGroups) {
		this.shareGroup = shareGroups;
	}

	/** 
	* 周报提交人所在部门
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 周报提交人所在部门
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
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
	* 周报留言集合
	* @return
	*/
	public List<WeekRepTalk> getListWeekTalk() {
		return listWeekTalk;
	}

	/** 
	* 周报留言集合
	* @param listWeekTalk
	*/
	public void setListWeekTalk(List<WeekRepTalk> listWeekTalk) {
		this.listWeekTalk = listWeekTalk;
	}

	/** 
	* 周报浏览的人员集合
	* @return
	*/
	public List<ViewRecord> getListViewRecord() {
		return listViewRecord;
	}

	/** 
	* 周报浏览的人员集合
	* @param listViewRecord
	*/
	public void setListViewRecord(List<ViewRecord> listViewRecord) {
		this.listViewRecord = listViewRecord;
	}

	/** 
	* 周报日志集合
	* @return
	*/
	public List<WeekRepLog> getListWeekRepLog() {
		return listWeekRepLog;
	}

	/** 
	* 周报日志集合
	* @param listWeekRepLog
	*/
	public void setListWeekRepLog(List<WeekRepLog> listWeekRepLog) {
		this.listWeekRepLog = listWeekRepLog;
	}

	/** 
	* 本周已回报数
	* @return
	*/
	public Integer getRepNum() {
		return repNum;
	}

	/** 
	* 本周已回报数
	* @param repNum
	*/
	public void setRepNum(Integer repNum) {
		this.repNum = repNum;
	}

	/** 
	* 本周未回报数
	* @return
	*/
	public Integer getUnRepNum() {
		return unRepNum;
	}

	/** 
	* 本周未回报数
	* @param unRepNum
	*/
	public void setUnRepNum(Integer unRepNum) {
		this.unRepNum = unRepNum;
	}

	/** 
	* 周报数
	* @return
	*/
	public Integer getAllRepNum() {
		return allRepNum;
	}

	/** 
	* 周报数
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
	* 周报汇报内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 周报汇报内容
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
	* 本周做了什么
	* @return
	*/
	public String getWeeklyDone() {
		return weeklyDone;
	}

	/** 
	* 本周做了什么
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
	* 周计划是什么
	* @return
	*/
	public String getWeeklyPlan() {
		return weeklyPlan;
	}

	/** 
	* 周计划是什么
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
}
