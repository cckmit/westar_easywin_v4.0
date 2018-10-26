package com.westar.base.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.ModSpConf;

/** 
 * 会议
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Meeting {
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
	* 会议名称
	*/
	@Filed
	private String title;
	/** 
	* 发起人主键
	*/
	@Filed
	private Integer organiser;
	/** 
	* 主持人主键
	*/
	@Filed
	private Integer presenter;
	/** 
	* 会议室类型 0公司会议室 1外部会议室
	*/
	@Filed
	private String roomType;
	/** 
	* 会议地点主键 外部的为0
	*/
	@Filed
	private Integer meetingAddrId;
	/** 
	* 会议地点名称
	*/
	@Filed
	private String meetingAddrName;
	/** 
	* 会议开始时间
	*/
	@Filed
	private String startDate;
	/** 
	* 会议结束时间
	*/
	@Filed
	private String endDate;
	/** 
	* 会议描述
	*/
	@Filed
	private String content;
	/** 
	* 会议状态0未发布1已发布
	*/
	@Filed
	private Integer meetingState;
	/** 
	* 会议类型0单次 1每天2每周3每月
	*/
	@Filed
	private String meetingType;
	/** 
	* 记录员主键
	*/
	@Filed
	private Integer recorder;
	/** 
	* 提前提醒时间
	*/
	@Filed
	private String aheadTime;
	/** 
	* 发送短信告知0不发送1发送
	*/
	@Filed
	private String sendPhoneMsg;
	/** 
	* 关联流程主键
	*/
	@Filed
	private Integer flowId;
	/** 
	* activity实例化主键
	*/
	@Filed
	private String actInstaceId;
	/** 
	* 审核标识：0 不审核 1审核中 -1不通过 3通过
	*/
	@Filed
	private Integer spState;

	/****************以上主要为系统表字段********************/
	/** 
	* 与会人员
	*/
	List<MeetPerson> listMeetPerson;
	/** 
	* 与会人员
	*/
	List<MeetDep> listMeetDep;
	/** 
	* 告知人员
	*/
	List<NoticePerson> listNoticePerson;
	/** 
	* 会议纪要
	*/
	MeetSummary meetSummary;
	/** 
	* 会议纪要
	*/
	List<SummaryFile> listSummaryFile;
	/** 
	* 主持人姓名
	*/
	private String presenterName;
	/** 
	* 主持人性别
	*/
	private String presenterGender;
	/** 
	* 主持人头像uuid
	*/
	private String presenterImgUuid;
	/** 
	* 主持人头像name
	*/
	private String presenterImgName;
	/** 
	* 记录员姓名
	*/
	private String recorderName;
	/** 
	* 记录员性别
	*/
	private String recorderGender;
	/** 
	* 记录员头像uuid
	*/
	private String recorderImgUuid;
	/** 
	* 记录员头像name
	*/
	private String recorderImgName;
	/** 
	* 是否过期0已结束1正在进行2未开始
	*/
	private String timeOut;
	/** 
	* 会议发起人姓名
	*/
	private String organiserName;
	/** 
	* 是否有会议纪要
	*/
	private String summaryState;
	/** 
	* 是否为审批的会议纪要
	*/
	private Integer summarySpState;
	/** 
	* 是否为审批的会议纪要
	*/
	private String summaryNeedSpState;
	/** 
	* 会议室申请状态
	*/
	private String applyState;
	/** 
	* 会议确认状态
	*/
	private String checkState;
	/** 
	* 会议周期
	*/
	private MeetRegular meetRegular;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 会议纪要
	*/
	String summary;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	private List<UserInfo> listCreator;
	/** 
	* 审批配置
	*/
	private ModSpConf modSpConf;
	/** 
	* 审批流程配置信息
	*/
	private String modFlowConfStr;
	/** 
	* 审批办理人员
	*/
	private Integer spExecutorId;
	private String spExecutorName;
	/** 
	* 会议学习人集合
	*/
	private List<MeetLearn> listMeetLearn;
	/** 
	* 会议血虚人json字符串
	*/
	private String sharerJson;
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
	* 会议名称
	* @param title
	*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** 
	* 会议名称
	* @return
	*/
	public String getTitle() {
		return title;
	}

	/** 
	* 发起人主键
	* @param organiser
	*/
	public void setOrganiser(Integer organiser) {
		this.organiser = organiser;
	}

	/** 
	* 发起人主键
	* @return
	*/
	public Integer getOrganiser() {
		return organiser;
	}

	/** 
	* 主持人主键
	* @param presenter
	*/
	public void setPresenter(Integer presenter) {
		this.presenter = presenter;
	}

	/** 
	* 主持人主键
	* @return
	*/
	public Integer getPresenter() {
		return presenter;
	}

	/** 
	* 会议室类型 0公司会议室 1外部会议室
	* @param roomType
	*/
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	/** 
	* 会议室类型 0公司会议室 1外部会议室
	* @return
	*/
	public String getRoomType() {
		return roomType;
	}

	/** 
	* 会议地点主键 外部的为0
	* @param meetingAddrId
	*/
	public void setMeetingAddrId(Integer meetingAddrId) {
		this.meetingAddrId = meetingAddrId;
	}

	/** 
	* 会议地点主键 外部的为0
	* @return
	*/
	public Integer getMeetingAddrId() {
		return meetingAddrId;
	}

	/** 
	* 会议地点名称
	* @param meetingAddrName
	*/
	public void setMeetingAddrName(String meetingAddrName) {
		this.meetingAddrName = meetingAddrName;
	}

	/** 
	* 会议地点名称
	* @return
	*/
	public String getMeetingAddrName() {
		return meetingAddrName;
	}

	/** 
	* 会议开始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 会议开始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 会议结束时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 会议结束时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 会议描述
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 会议描述
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 会议状态0未发布1已发布
	* @param meetingState
	*/
	public void setMeetingState(Integer meetingState) {
		this.meetingState = meetingState;
	}

	/** 
	* 会议状态0未发布1已发布
	* @return
	*/
	public Integer getMeetingState() {
		return meetingState;
	}

	/** 
	* 会议类型0单次 1每天2每周3每月
	* @param meetingType
	*/
	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	/** 
	* 会议类型0单次 1每天2每周3每月
	* @return
	*/
	public String getMeetingType() {
		return meetingType;
	}

	/** 
	* 会议纪要
	* @param summary
	*/
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/** 
	* 会议纪要
	* @return
	*/
	public String getSummary() {
		return summary;
	}

	/** 
	* 与会人员
	* @return
	*/
	public List<MeetPerson> getListMeetPerson() {
		return listMeetPerson;
	}

	/** 
	* 与会人员
	* @param listMeetPerson
	*/
	public void setListMeetPerson(List<MeetPerson> listMeetPerson) {
		this.listMeetPerson = listMeetPerson;
	}

	/** 
	* 与会人员
	* @return
	*/
	public List<MeetDep> getListMeetDep() {
		return listMeetDep;
	}

	/** 
	* 与会人员
	* @param listMeetDep
	*/
	public void setListMeetDep(List<MeetDep> listMeetDep) {
		this.listMeetDep = listMeetDep;
	}

	/** 
	* 告知人员
	* @return
	*/
	public List<NoticePerson> getListNoticePerson() {
		return listNoticePerson;
	}

	/** 
	* 告知人员
	* @param listNoticePerson
	*/
	public void setListNoticePerson(List<NoticePerson> listNoticePerson) {
		this.listNoticePerson = listNoticePerson;
	}

	/** 
	* 记录员主键
	* @param recorder
	*/
	public void setRecorder(Integer recorder) {
		this.recorder = recorder;
	}

	/** 
	* 记录员主键
	* @return
	*/
	public Integer getRecorder() {
		return recorder;
	}

	/** 
	* 主持人姓名
	* @return
	*/
	public final String getPresenterName() {
		return presenterName;
	}

	/** 
	* 主持人姓名
	* @param presenterName
	*/
	public final void setPresenterName(String presenterName) {
		this.presenterName = presenterName;
	}

	/** 
	* 主持人性别
	* @return
	*/
	public final String getPresenterGender() {
		return presenterGender;
	}

	/** 
	* 主持人性别
	* @param presenterGender
	*/
	public final void setPresenterGender(String presenterGender) {
		this.presenterGender = presenterGender;
	}

	/** 
	* 主持人头像uuid
	* @return
	*/
	public final String getPresenterImgUuid() {
		return presenterImgUuid;
	}

	/** 
	* 主持人头像uuid
	* @param presenterImgUuid
	*/
	public final void setPresenterImgUuid(String presenterImgUuid) {
		this.presenterImgUuid = presenterImgUuid;
	}

	/** 
	* 主持人头像name
	* @return
	*/
	public final String getPresenterImgName() {
		return presenterImgName;
	}

	/** 
	* 主持人头像name
	* @param presenterImgName
	*/
	public final void setPresenterImgName(String presenterImgName) {
		this.presenterImgName = presenterImgName;
	}

	/** 
	* 记录员姓名
	* @return
	*/
	public final String getRecorderName() {
		return recorderName;
	}

	/** 
	* 记录员姓名
	* @param recorderName
	*/
	public final void setRecorderName(String recorderName) {
		this.recorderName = recorderName;
	}

	/** 
	* 记录员性别
	* @return
	*/
	public final String getRecorderGender() {
		return recorderGender;
	}

	/** 
	* 记录员性别
	* @param recorderGender
	*/
	public final void setRecorderGender(String recorderGender) {
		this.recorderGender = recorderGender;
	}

	/** 
	* 记录员头像uuid
	* @return
	*/
	public final String getRecorderImgUuid() {
		return recorderImgUuid;
	}

	/** 
	* 记录员头像uuid
	* @param recorderImgUuid
	*/
	public final void setRecorderImgUuid(String recorderImgUuid) {
		this.recorderImgUuid = recorderImgUuid;
	}

	/** 
	* 记录员头像name
	* @return
	*/
	public final String getRecorderImgName() {
		return recorderImgName;
	}

	/** 
	* 记录员头像name
	* @param recorderImgName
	*/
	public final void setRecorderImgName(String recorderImgName) {
		this.recorderImgName = recorderImgName;
	}

	/** 
	* 会议纪要
	* @return
	*/
	public final List<SummaryFile> getListSummaryFile() {
		return listSummaryFile;
	}

	/** 
	* 会议纪要
	* @param listSummaryFile
	*/
	public final void setListSummaryFile(List<SummaryFile> listSummaryFile) {
		this.listSummaryFile = listSummaryFile;
	}

	/** 
	* 是否过期0已结束1正在进行2未开始
	* @return
	*/
	public String getTimeOut() {
		return timeOut;
	}

	/** 
	* 是否过期0已结束1正在进行2未开始
	* @param timeOut
	*/
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	/** 
	* 会议发起人姓名
	* @return
	*/
	public String getOrganiserName() {
		return organiserName;
	}

	/** 
	* 会议发起人姓名
	* @param organiserName
	*/
	public void setOrganiserName(String organiserName) {
		this.organiserName = organiserName;
	}

	/** 
	* 是否有会议纪要
	* @return
	*/
	public String getSummaryState() {
		return summaryState;
	}

	/** 
	* 是否有会议纪要
	* @param summaryState
	*/
	public void setSummaryState(String summaryState) {
		this.summaryState = summaryState;
	}

	/** 
	* 会议室申请状态
	* @return
	*/
	public String getApplyState() {
		return applyState;
	}

	/** 
	* 会议室申请状态
	* @param applyState
	*/
	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}

	/** 
	* 提前提醒时间
	* @param aheadTime
	*/
	public void setAheadTime(String aheadTime) {
		this.aheadTime = aheadTime;
	}

	/** 
	* 提前提醒时间
	* @return
	*/
	public String getAheadTime() {
		return aheadTime;
	}

	/** 
	* 会议周期
	* @return
	*/
	public MeetRegular getMeetRegular() {
		return meetRegular;
	}

	/** 
	* 会议周期
	* @param meetRegular
	*/
	public void setMeetRegular(MeetRegular meetRegular) {
		this.meetRegular = meetRegular;
	}

	/** 
	* 会议确认状态
	* @return
	*/
	public String getCheckState() {
		return checkState;
	}

	/** 
	* 会议确认状态
	* @param checkState
	*/
	public void setCheckState(String checkState) {
		this.checkState = checkState;
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

	public List<UserInfo> getListCreator() {
		return listCreator;
	}

	public void setListCreator(List<UserInfo> listCreator) {
		this.listCreator = listCreator;
	}

	/** 
	* 发送短信告知0不发送1发送
	* @param sendPhoneMsg
	*/
	public void setSendPhoneMsg(String sendPhoneMsg) {
		this.sendPhoneMsg = sendPhoneMsg;
	}

	/** 
	* 发送短信告知0不发送1发送
	* @return
	*/
	public String getSendPhoneMsg() {
		return sendPhoneMsg;
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
	* 审核标识：0 不审核 1审核中 -1不通过 3通过
	* @param spState
	*/
	public void setSpState(Integer spState) {
		this.spState = spState;
	}

	/** 
	* 审核标识：0 不审核 1审核中 -1不通过 3通过
	* @return
	*/
	public Integer getSpState() {
		return spState;
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

	/** 
	* 审批流程配置信息
	* @return
	*/
	public String getModFlowConfStr() {
		return modFlowConfStr;
	}

	/** 
	* 审批流程配置信息
	* @param modFlowConfStr
	*/
	public void setModFlowConfStr(String modFlowConfStr) {
		this.modFlowConfStr = modFlowConfStr;
	}

	/** 
	* 审批办理人员
	* @return
	*/
	public Integer getSpExecutorId() {
		return spExecutorId;
	}

	/** 
	* 审批办理人员
	* @param spExecutorId
	*/
	public void setSpExecutorId(Integer spExecutorId) {
		this.spExecutorId = spExecutorId;
	}

	public String getSpExecutorName() {
		return spExecutorName;
	}

	public void setSpExecutorName(String spExecutorName) {
		this.spExecutorName = spExecutorName;
	}

	/** 
	* 会议纪要
	* @return
	*/
	public MeetSummary getMeetSummary() {
		return meetSummary;
	}

	/** 
	* 会议纪要
	* @param meetSummary
	*/
	public void setMeetSummary(MeetSummary meetSummary) {
		this.meetSummary = meetSummary;
	}

	/** 
	* 是否为审批的会议纪要
	* @return
	*/
	public Integer getSummarySpState() {
		return summarySpState;
	}

	/** 
	* 是否为审批的会议纪要
	* @param summarySpState
	*/
	public void setSummarySpState(Integer summarySpState) {
		this.summarySpState = summarySpState;
	}

	/** 
	* 是否为审批的会议纪要
	* @return
	*/
	public String getSummaryNeedSpState() {
		return summaryNeedSpState;
	}

	/** 
	* 是否为审批的会议纪要
	* @param summaryNeedSpState
	*/
	public void setSummaryNeedSpState(String summaryNeedSpState) {
		this.summaryNeedSpState = summaryNeedSpState;
	}

	/** 
	* 会议学习人集合
	* @return
	*/
	public List<MeetLearn> getListMeetLearn() {
		return listMeetLearn;
	}

	/** 
	* 会议学习人集合
	* @param listMeetLearn
	*/
	public void setListMeetLearn(List<MeetLearn> listMeetLearn) {
		this.listMeetLearn = listMeetLearn;
	}

	/** 
	* 会议血虚人json字符串
	* @return
	*/
	public String getSharerJson() {
		return sharerJson;
	}

	/** 
	* 会议血虚人json字符串
	* @param sharerJson
	*/
	public void setSharerJson(String sharerJson) {
		this.sharerJson = sharerJson;
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
