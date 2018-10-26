package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 审批留言
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowTalk {
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
	private Integer busId;
	/** 
	* 业务类型
	*/
	@Filed
	private String busType;
	/** 
	* 讨论父ID
	*/
	@Filed
	private Integer parentId;
	/** 
	* 讨论内容
	*/
	@Filed
	private String content;
	/** 
	* 发言人
	*/
	@Filed
	private Integer speaker;

	/****************以上主要为系统表字段********************/
	private String speakerName;
	/** 
	* 附件集合
	*/
	private List<SpFlowTalkUpfile> listSpFlowTalkUpfile;
	/** 
	* 附件名称
	*/
	private String filename;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 节点下回复集合
	*/
	private List<SpFlowTalk> listReplySpFlowTalk;
	/** 
	* 父节点发起人
	*/
	private Integer pSpeaker;
	/** 
	* 父节点发起人姓名
	*/
	private String pSpeakerName;
	/** 
	* 父节点说的内容
	*/
	private String pContent;
	/** 
	* 页面展示所需DIV字符串
	*/
	private String spFlowTalkDivString;
	/** 
	* 判断讨论是否是叶子节点
	*/
	private Integer isLeaf;
	/** 
	* 是否删除节点回复
	*/
	private String delChildNode;
	/** 
	* 任务状态表示符
	*/
	private Integer taskState;
	/** 
	* 附件主键
	*/
	private Integer[] upfilesId;
	/** 
	* 上传附件名称数组
	*/
	private String[] filesName;
	/** 
	* 留言回复提醒人员
	*/
	private List<SpFlowNoticeUser> listSpFlowNoticeUser;

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
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 流程实例化主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 业务类型
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 业务类型
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 讨论父ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 讨论父ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 讨论内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 讨论内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 发言人
	* @param speaker
	*/
	public void setSpeaker(Integer speaker) {
		this.speaker = speaker;
	}

	/** 
	* 发言人
	* @return
	*/
	public Integer getSpeaker() {
		return speaker;
	}

	public String getSpeakerName() {
		return speakerName;
	}

	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}

	/** 
	* 附件集合
	* @return
	*/
	public List<SpFlowTalkUpfile> getListSpFlowTalkUpfile() {
		return listSpFlowTalkUpfile;
	}

	/** 
	* 附件集合
	* @param listSpFlowTalkUpfile
	*/
	public void setListSpFlowTalkUpfile(List<SpFlowTalkUpfile> listSpFlowTalkUpfile) {
		this.listSpFlowTalkUpfile = listSpFlowTalkUpfile;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFilename() {
		return filename;
	}

	/** 
	* 附件名称
	* @param filename
	*/
	public void setFilename(String filename) {
		this.filename = filename;
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
	* 节点下回复集合
	* @return
	*/
	public List<SpFlowTalk> getListReplySpFlowTalk() {
		return listReplySpFlowTalk;
	}

	/** 
	* 节点下回复集合
	* @param listReplySpFlowTalk
	*/
	public void setListReplySpFlowTalk(List<SpFlowTalk> listReplySpFlowTalk) {
		this.listReplySpFlowTalk = listReplySpFlowTalk;
	}

	public Integer getpSpeaker() {
		return pSpeaker;
	}

	public void setpSpeaker(Integer pSpeaker) {
		this.pSpeaker = pSpeaker;
	}

	public String getpSpeakerName() {
		return pSpeakerName;
	}

	public void setpSpeakerName(String pSpeakerName) {
		this.pSpeakerName = pSpeakerName;
	}

	public String getpContent() {
		return pContent;
	}

	public void setpContent(String pContent) {
		this.pContent = pContent;
	}

	/** 
	* 判断讨论是否是叶子节点
	* @return
	*/
	public Integer getIsLeaf() {
		return isLeaf;
	}

	/** 
	* 判断讨论是否是叶子节点
	* @param isLeaf
	*/
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	/** 
	* 是否删除节点回复
	* @return
	*/
	public String getDelChildNode() {
		return delChildNode;
	}

	/** 
	* 是否删除节点回复
	* @param delChildNode
	*/
	public void setDelChildNode(String delChildNode) {
		this.delChildNode = delChildNode;
	}

	/** 
	* 任务状态表示符
	* @return
	*/
	public Integer getTaskState() {
		return taskState;
	}

	/** 
	* 任务状态表示符
	* @param taskState
	*/
	public void setTaskState(Integer taskState) {
		this.taskState = taskState;
	}

	/** 
	* 附件主键
	* @return
	*/
	public Integer[] getUpfilesId() {
		return upfilesId;
	}

	/** 
	* 附件主键
	* @param upfilesId
	*/
	public void setUpfilesId(Integer[] upfilesId) {
		this.upfilesId = upfilesId;
	}

	/** 
	* 上传附件名称数组
	* @return
	*/
	public String[] getFilesName() {
		return filesName;
	}

	/** 
	* 上传附件名称数组
	* @param filesName
	*/
	public void setFilesName(String[] filesName) {
		this.filesName = filesName;
	}

	/** 
	* 页面展示所需DIV字符串
	* @return
	*/
	public String getSpFlowTalkDivString() {
		return spFlowTalkDivString;
	}

	/** 
	* 页面展示所需DIV字符串
	* @param spFlowTalkDivString
	*/
	public void setSpFlowTalkDivString(String spFlowTalkDivString) {
		this.spFlowTalkDivString = spFlowTalkDivString;
	}

	/** 
	* 留言回复提醒人员
	* @return
	*/
	public List<SpFlowNoticeUser> getListSpFlowNoticeUser() {
		return listSpFlowNoticeUser;
	}

	/** 
	* 留言回复提醒人员
	* @param listSpFlowNoticeUser
	*/
	public void setListSpFlowNoticeUser(List<SpFlowNoticeUser> listSpFlowNoticeUser) {
		this.listSpFlowNoticeUser = listSpFlowNoticeUser;
	}
}
