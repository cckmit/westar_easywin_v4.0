package com.westar.base.pojo;

import java.util.List;

import com.westar.base.model.UserInfo;

/** 
 * 
 * 描述:公共回复留言类
 * @author zzq
 * @date 2018年10月12日 下午1:24:47
 */
public class BaseTalk {
	/** 
	* id主键
	*/
	private Integer id;
	/** 
	* 记录创建时间
	*/
	private String recordCreateTime;
	/** 
	* 企业编号
	*/
	private Integer comId;
	/** 
	* 所属模块主键
	*/
	private Integer busId;
	/** 
	 * 所属模块类型
	 */
	private String busType;
	/** 
	* 讨论父ID
	*/
	private Integer parentId;
	/** 
	* 讨论内容
	*/
	private String content;
	/** 
	* 发言人
	*/
	private Integer speaker;

	/****************以上主要为系统表字段********************/
	private String speakerName;
	/** 
	* 附件集合
	*/
	private List<BaseUpfile> listTalkFile;
	
	/** 
	* 节点下回复集合
	*/
	private List<BaseTalk> listReplyTalk;
	/** 
	* 父节点发起人
	*/
	private Integer pSpeaker;
	/** 
	* 父节点发起人姓名
	*/
	private String pSpeakerName;
	/** 
	* 判断讨论是否是叶子节点
	*/
	private Integer isLeaf;
	/** 
	* 是否删除节点回复
	*/
	private String delChildNode;
	/** 
	* 分享人员
	*/
	private List<UserInfo> listSharer;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRecordCreateTime() {
		return recordCreateTime;
	}
	public void setRecordCreateTime(String recordCreateTime) {
		this.recordCreateTime = recordCreateTime;
	}
	public Integer getComId() {
		return comId;
	}
	public void setComId(Integer comId) {
		this.comId = comId;
	}
	public Integer getBusId() {
		return busId;
	}
	public void setBusId(Integer busId) {
		this.busId = busId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getSpeaker() {
		return speaker;
	}
	public void setSpeaker(Integer speaker) {
		this.speaker = speaker;
	}
	public String getSpeakerName() {
		return speakerName;
	}
	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}
	public List<BaseUpfile> getListTalkFile() {
		return listTalkFile;
	}
	public void setListTalkFile(List<BaseUpfile> listTalkFile) {
		this.listTalkFile = listTalkFile;
	}
	public List<BaseTalk> getListReplyTalk() {
		return listReplyTalk;
	}
	public void setListReplyTalk(List<BaseTalk> listReplyTalk) {
		this.listReplyTalk = listReplyTalk;
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
	public Integer getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
	public String getDelChildNode() {
		return delChildNode;
	}
	public void setDelChildNode(String delChildNode) {
		this.delChildNode = delChildNode;
	}
	public List<UserInfo> getListSharer() {
		return listSharer;
	}
	public void setListSharer(List<UserInfo> listSharer) {
		this.listSharer = listSharer;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	
}
