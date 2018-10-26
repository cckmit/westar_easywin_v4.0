package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.ModSpConf;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 会议纪要
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MeetSummary {
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
	* 会议主键
	*/
	@Filed
	private Integer meetingId;
	/** 
	* 会议纪要
	*/
	@Filed
	private String summary;
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
	* 会议纪要
	*/
	List<SummaryFile> listSummaryFile;
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
	* 会议主键
	* @param meetingId
	*/
	public void setMeetingId(Integer meetingId) {
		this.meetingId = meetingId;
	}

	/** 
	* 会议主键
	* @return
	*/
	public Integer getMeetingId() {
		return meetingId;
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
	* 会议纪要
	* @return
	*/
	public List<SummaryFile> getListSummaryFile() {
		return listSummaryFile;
	}

	/** 
	* 会议纪要
	* @param listSummaryFile
	*/
	public void setListSummaryFile(List<SummaryFile> listSummaryFile) {
		this.listSummaryFile = listSummaryFile;
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
}
