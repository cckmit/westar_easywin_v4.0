package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.util.ConstantInterface;
import com.westar.core.web.DataDicContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 工作动态分享表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MsgShare {
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
	* 分享信息内容
	*/
	@Filed
	private String content;
	/** 
	* 信息查看地址
	*/
	@Filed
	private String action;
	/** 
	* 分享信息类型
	*/
	@Filed
	private String type;
	/** 
	* 人员主键
	*/
	@Filed
	private Integer creator;
	/** 
	* 分享范围类型 若是轨迹则为-1
	*/
	@Filed
	private Integer scopeType;
	/** 
	* 模块ID
	*/
	@Filed
	private Integer modId;
	/** 
	* 工作类型 0分享 1轨迹
	*/
	@Filed
	private Integer traceType;
	/** 
	* 是否公开 默认公开
	*/
	@Filed
	private Integer isPub;

	/****************以上主要为系统表字段********************/
	private String creatorName;
	private String createDate;
	private Integer backObjId;
	private Integer nextObjId;
	private List<ShareGroup> shareGroup;
	/** 
	* 分享人员
	*/
	private List<ShareUser> listShareUser;
	private Integer dateScope;
	/** 
	* 大图片信息
	*/
	private String bigImgUuid;
	private String bigImgName;
	/** 
	* 中图片信息
	*/
	private String midImgUuid;
	private String midImgName;
	/** 
	* 小图片信息
	*/
	private String smImgUuid;
	private String smImgName;
	/** 
	* 性别
	*/
	private String gender;
	private Vote vote;
	@SuppressWarnings("unused")
	private String typeName;
	private Integer totalTalks;
	/** 
	* 关注状态0未关注1已关注
	*/
	private String attentionState;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 创建人类型0自己1下属
	*/
	private String creatorType;
	/** 
	* 对象是否被删除1不存在0存在
	*/
	private String isDel;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 留言集合
	*/
	private List<MsgShareTalk> listMsgShareTalk;
	/** 
	* 留言总数
	*/
	private Integer talkSum;
	/** 
	* 用于分页
	*/
	private Integer rowno;
	/** 
	* 分享表内容
	*/
	private Daily daily;

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
	* 分享信息内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 分享信息内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 信息查看地址
	* @param action
	*/
	public void setAction(String action) {
		this.action = action;
	}

	/** 
	* 信息查看地址
	* @return
	*/
	public String getAction() {
		return action;
	}

	/** 
	* 分享信息类型
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 分享信息类型
	* @return
	*/
	public String getType() {
		return type;
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

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getBackObjId() {
		return backObjId;
	}

	public void setBackObjId(Integer backObjId) {
		this.backObjId = backObjId;
	}

	public Integer getNextObjId() {
		return nextObjId;
	}

	public void setNextObjId(Integer nextObjId) {
		this.nextObjId = nextObjId;
	}

	/** 
	* 分享范围类型 若是轨迹则为-1
	* @param scopeType
	*/
	public void setScopeType(Integer scopeType) {
		this.scopeType = scopeType;
	}

	/** 
	* 分享范围类型 若是轨迹则为-1
	* @return
	*/
	public Integer getScopeType() {
		return scopeType;
	}

	public List<ShareGroup> getShareGroup() {
		return shareGroup;
	}

	public void setShareGroup(List<ShareGroup> shareGroup) {
		this.shareGroup = shareGroup;
	}

	/** 
	* 大图片信息
	* @return
	*/
	public String getBigImgUuid() {
		return bigImgUuid;
	}

	/** 
	* 大图片信息
	* @param bigImgUuid
	*/
	public void setBigImgUuid(String bigImgUuid) {
		this.bigImgUuid = bigImgUuid;
	}

	public String getBigImgName() {
		return bigImgName;
	}

	public void setBigImgName(String bigImgName) {
		this.bigImgName = bigImgName;
	}

	/** 
	* 中图片信息
	* @return
	*/
	public String getMidImgUuid() {
		return midImgUuid;
	}

	/** 
	* 中图片信息
	* @param midImgUuid
	*/
	public void setMidImgUuid(String midImgUuid) {
		this.midImgUuid = midImgUuid;
	}

	public String getMidImgName() {
		return midImgName;
	}

	public void setMidImgName(String midImgName) {
		this.midImgName = midImgName;
	}

	/** 
	* 小图片信息
	* @return
	*/
	public String getSmImgUuid() {
		return smImgUuid;
	}

	/** 
	* 小图片信息
	* @param smImgUuid
	*/
	public void setSmImgUuid(String smImgUuid) {
		this.smImgUuid = smImgUuid;
	}

	public String getSmImgName() {
		return smImgName;
	}

	public void setSmImgName(String smImgName) {
		this.smImgName = smImgName;
	}

	/** 
	* 性别
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 性别
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 模块ID
	* @param modId
	*/
	public void setModId(Integer modId) {
		this.modId = modId;
	}

	/** 
	* 模块ID
	* @return
	*/
	public Integer getModId() {
		return modId;
	}

	public Vote getVote() {
		return vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}

	public Integer getDateScope() {
		return dateScope;
	}

	public void setDateScope(Integer dateScope) {
		this.dateScope = dateScope;
	}

	public String getTypeName() {
		if (null != type && ConstantInterface.TYPE_USER.equals(type)) {
			return "个人";
		} else if (null != type && ConstantInterface.TYPE_ORG.equals(type)) {
			return "企业";
		} else if (null != type && !"1".equals(type)) {
			return DataDicContext.getInstance().getCurrentPathZvalue("moduleType", type).substring(0, 2);
		} else {
			return "信息";
		}
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getTotalTalks() {
		return totalTalks;
	}

	public void setTotalTalks(Integer totalTalks) {
		this.totalTalks = totalTalks;
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
	* 工作类型 0分享 1轨迹
	* @param traceType
	*/
	public void setTraceType(Integer traceType) {
		this.traceType = traceType;
	}

	/** 
	* 工作类型 0分享 1轨迹
	* @return
	*/
	public Integer getTraceType() {
		return traceType;
	}

	/** 
	* 是否公开 默认公开
	* @param isPub
	*/
	public void setIsPub(Integer isPub) {
		this.isPub = isPub;
	}

	/** 
	* 是否公开 默认公开
	* @return
	*/
	public Integer getIsPub() {
		return isPub;
	}

	/** 
	* 创建人类型0自己1下属
	* @return
	*/
	public String getCreatorType() {
		return creatorType;
	}

	/** 
	* 创建人类型0自己1下属
	* @param creatorType
	*/
	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}

	/** 
	* 对象是否被删除1不存在0存在
	* @return
	*/
	public String getIsDel() {
		return isDel;
	}

	/** 
	* 对象是否被删除1不存在0存在
	* @param isDel
	*/
	public void setIsDel(String isDel) {
		this.isDel = isDel;
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
	* 留言集合
	* @return
	*/
	public List<MsgShareTalk> getListMsgShareTalk() {
		return listMsgShareTalk;
	}

	/** 
	* 留言集合
	* @param listMsgShareTalk
	*/
	public void setListMsgShareTalk(List<MsgShareTalk> listMsgShareTalk) {
		this.listMsgShareTalk = listMsgShareTalk;
	}

	/** 
	* 留言总数
	* @return
	*/
	public Integer getTalkSum() {
		return talkSum;
	}

	/** 
	* 留言总数
	* @param talkSum
	*/
	public void setTalkSum(Integer talkSum) {
		this.talkSum = talkSum;
	}

	/** 
	* 分享人员
	* @return
	*/
	public List<ShareUser> getListShareUser() {
		return listShareUser;
	}

	/** 
	* 分享人员
	* @param listShareUser
	*/
	public void setListShareUser(List<ShareUser> listShareUser) {
		this.listShareUser = listShareUser;
	}

	/** 
	* 用于分页
	* @return
	*/
	public Integer getRowno() {
		return rowno;
	}

	/** 
	* 用于分页
	* @param rowno
	*/
	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}

	/** 
	* 分享表内容
	* @return
	*/
	public Daily getDaily() {
		return daily;
	}

	/** 
	* 分享表内容
	* @param daily
	*/
	public void setDaily(Daily daily) {
		this.daily = daily;
	}
}
