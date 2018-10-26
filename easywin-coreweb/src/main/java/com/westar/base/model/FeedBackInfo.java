package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 反馈信息
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FeedBackInfo {
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
	* 客户主键
	*/
	@Filed
	private Integer customerId;
	/** 
	* 反馈信息父ID
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
	private Integer userId;
	/** 
	* 反馈类型主键
	*/
	@Filed
	private Integer feedBackTypeId;

	/****************以上主要为系统表字段********************/
	/** 
	* 是否分享信息
	*/
	private String shareMsg;
	/** 
	* 私有组ID和组类型组合字符串
	*/
	private String groupIdAndType;
	/** 
	* 附件集合
	*/
	private List<FeedInfoFile> listfeedInfoFiles;
	private String speakerName;
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
	private String feedBackInfoDivString;
	/** 
	* 判断讨论是否是叶子节点
	*/
	private Integer isLeaf;
	/** 
	* 是否删除节点回复
	*/
	private String delChildNode;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 反馈类名称
	*/
	private String typeName;
	private Integer[] upfilesId;
	/** 
	* 上传附件名称数组
	*/
	private String[] filesName;
	/** 
	* 客户维护记录分享人员列表
	*/
	private List<CustomerSharer> listCustomerSharers;

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
	* 客户主键
	* @param customerId
	*/
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/** 
	* 客户主键
	* @return
	*/
	public Integer getCustomerId() {
		return customerId;
	}

	/** 
	* 反馈信息父ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 反馈信息父ID
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
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 发言人
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 反馈类型主键
	* @param feedBackTypeId
	*/
	public void setFeedBackTypeId(Integer feedBackTypeId) {
		this.feedBackTypeId = feedBackTypeId;
	}

	/** 
	* 反馈类型主键
	* @return
	*/
	public Integer getFeedBackTypeId() {
		return feedBackTypeId;
	}

	/** 
	* 是否分享信息
	* @return
	*/
	public String getShareMsg() {
		return shareMsg;
	}

	/** 
	* 是否分享信息
	* @param shareMsg
	*/
	public void setShareMsg(String shareMsg) {
		this.shareMsg = shareMsg;
	}

	/** 
	* 私有组ID和组类型组合字符串
	* @return
	*/
	public String getGroupIdAndType() {
		return groupIdAndType;
	}

	/** 
	* 私有组ID和组类型组合字符串
	* @param groupIdAndType
	*/
	public void setGroupIdAndType(String groupIdAndType) {
		this.groupIdAndType = groupIdAndType;
	}

	/** 
	* 附件集合
	* @return
	*/
	public List<FeedInfoFile> getListfeedInfoFiles() {
		return listfeedInfoFiles;
	}

	/** 
	* 附件集合
	* @param listfeedInfoFiles
	*/
	public void setListfeedInfoFiles(List<FeedInfoFile> listfeedInfoFiles) {
		this.listfeedInfoFiles = listfeedInfoFiles;
	}

	public String getSpeakerName() {
		return speakerName;
	}

	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
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
	* 页面展示所需DIV字符串
	* @return
	*/
	public String getFeedBackInfoDivString() {
		return feedBackInfoDivString;
	}

	/** 
	* 页面展示所需DIV字符串
	* @param feedBackInfoDivString
	*/
	public void setFeedBackInfoDivString(String feedBackInfoDivString) {
		this.feedBackInfoDivString = feedBackInfoDivString;
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
	* 反馈类名称
	* @return
	*/
	public String getTypeName() {
		return typeName;
	}

	/** 
	* 反馈类名称
	* @param typeName
	*/
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer[] getUpfilesId() {
		return upfilesId;
	}

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
	* 客户维护记录分享人员列表
	* @return
	*/
	public List<CustomerSharer> getListCustomerSharers() {
		return listCustomerSharers;
	}

	/** 
	* 客户维护记录分享人员列表
	* @param listCustomerSharers
	*/
	public void setListCustomerSharers(List<CustomerSharer> listCustomerSharers) {
		this.listCustomerSharers = listCustomerSharers;
	}
}
