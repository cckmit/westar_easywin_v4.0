package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 投票说明
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Vote {
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
	* 投票发起人
	*/
	@Filed
	private Integer owner;
	/** 
	* 最大可选个数
	*/
	@Filed
	private Integer maxChoose;
	/** 
	* 截止时间
	*/
	@Filed
	private String finishTime;
	/** 
	* 投票范围类型 0所有人；1自定义分组；2自己
	*/
	@Filed
	private Integer scopeType;
	/** 
	* 投票说明
	*/
	@Filed
	private String voteContent;
	/** 
	* 匿名类型 0实名 1匿名 
	*/
	@Filed
	private String voteType;
	/** 
	* 删除标识 0未删除，1预删除
	*/
	@Filed
	private Integer delState;

	/****************以上主要为系统表字段********************/
	/** 
	* 投票的选项
	*/
	private List<VoteChoose> voteChooses;
	/** 
	* 投票范围
	*/
	private List<VoteScope> voteScopes;
	/** 
	* 投票讨论
	*/
	private List<VoteTalk> voteTalks;
	/** 
	* 查询人员
	*/
	private Integer sessionUser;
	/** 
	* 条件总的范围
	*/
	private String searchAll;
	/** 
	* 条件查看权限
	*/
	private String searchMe;
	/** 
	* 查询排序
	*/
	private String orderBy;
	/** 
	* 发起人姓名
	*/
	private String ownerName;
	/** 
	* 发起人小头像
	*/
	private String ownerSmlImgUuid;
	private String ownerSmlImgName;
	/** 
	* 是否投票0未投票非0已投票
	*/
	private Integer voterChooseNum;
	/** 
	* 总的投票数
	*/
	private Integer voteTotal;
	/** 
	* 回复的投票数
	*/
	private Integer sumtalks;
	/** 
	* 发起人性别
	*/
	private String ownerGender;
	/** 
	* 是否过期（1可投票0不可投票）
	*/
	private String enabled;
	/** 
	* 选项投票最多的
	*/
	private List<VoteChoose> mostChooses;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 推送信息
	*/
	private String shareMsg;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 关注状态0未关注1已关注
	*/
	private String attentionState;
	/** 
	* 是否已读
	*/
	private Integer isRead;
	/** 
	* 附件数量
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
	* 投票发起人
	* @param owner
	*/
	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	/** 
	* 投票发起人
	* @return
	*/
	public Integer getOwner() {
		return owner;
	}

	/** 
	* 最大可选个数
	* @param maxChoose
	*/
	public void setMaxChoose(Integer maxChoose) {
		this.maxChoose = maxChoose;
	}

	/** 
	* 最大可选个数
	* @return
	*/
	public Integer getMaxChoose() {
		return maxChoose;
	}

	/** 
	* 截止时间
	* @param finishTime
	*/
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	/** 
	* 截止时间
	* @return
	*/
	public String getFinishTime() {
		return finishTime;
	}

	/** 
	* 投票范围类型 0所有人；1自定义分组；2自己
	* @param scopeType
	*/
	public void setScopeType(Integer scopeType) {
		this.scopeType = scopeType;
	}

	/** 
	* 投票范围类型 0所有人；1自定义分组；2自己
	* @return
	*/
	public Integer getScopeType() {
		return scopeType;
	}

	/** 
	* 投票说明
	* @param voteContent
	*/
	public void setVoteContent(String voteContent) {
		this.voteContent = voteContent;
	}

	/** 
	* 投票说明
	* @return
	*/
	public String getVoteContent() {
		return voteContent;
	}

	/** 
	* 匿名类型 0实名 1匿名 
	* @param voteType
	*/
	public void setVoteType(String voteType) {
		this.voteType = voteType;
	}

	/** 
	* 匿名类型 0实名 1匿名 
	* @return
	*/
	public String getVoteType() {
		return voteType;
	}

	/** 
	* 投票的选项
	* @return
	*/
	public List<VoteChoose> getVoteChooses() {
		return voteChooses;
	}

	/** 
	* 投票的选项
	* @param voteChooses
	*/
	public void setVoteChooses(List<VoteChoose> voteChooses) {
		this.voteChooses = voteChooses;
	}

	/** 
	* 投票范围
	* @return
	*/
	public List<VoteScope> getVoteScopes() {
		return voteScopes;
	}

	/** 
	* 投票范围
	* @param voteScopes
	*/
	public void setVoteScopes(List<VoteScope> voteScopes) {
		this.voteScopes = voteScopes;
	}

	/** 
	* 查询人员
	* @return
	*/
	public Integer getSessionUser() {
		return sessionUser;
	}

	/** 
	* 查询人员
	* @param sessionUser
	*/
	public void setSessionUser(Integer sessionUser) {
		this.sessionUser = sessionUser;
	}

	/** 
	* 发起人姓名
	* @return
	*/
	public String getOwnerName() {
		return ownerName;
	}

	/** 
	* 发起人姓名
	* @param ownerName
	*/
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/** 
	* 查询排序
	* @return
	*/
	public String getOrderBy() {
		return orderBy;
	}

	/** 
	* 查询排序
	* @param orderBy
	*/
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/** 
	* 条件总的范围
	* @return
	*/
	public String getSearchAll() {
		return searchAll;
	}

	/** 
	* 条件总的范围
	* @param searchAll
	*/
	public void setSearchAll(String searchAll) {
		this.searchAll = searchAll;
	}

	/** 
	* 条件查看权限
	* @return
	*/
	public String getSearchMe() {
		return searchMe;
	}

	/** 
	* 条件查看权限
	* @param searchMe
	*/
	public void setSearchMe(String searchMe) {
		this.searchMe = searchMe;
	}

	/** 
	* 发起人小头像
	* @return
	*/
	public String getOwnerSmlImgUuid() {
		return ownerSmlImgUuid;
	}

	/** 
	* 发起人小头像
	* @param ownerSmlImgUuid
	*/
	public void setOwnerSmlImgUuid(String ownerSmlImgUuid) {
		this.ownerSmlImgUuid = ownerSmlImgUuid;
	}

	public String getOwnerSmlImgName() {
		return ownerSmlImgName;
	}

	public void setOwnerSmlImgName(String ownerSmlImgName) {
		this.ownerSmlImgName = ownerSmlImgName;
	}

	/** 
	* 总的投票数
	* @return
	*/
	public Integer getVoteTotal() {
		return voteTotal;
	}

	/** 
	* 总的投票数
	* @param voteTotal
	*/
	public void setVoteTotal(Integer voteTotal) {
		this.voteTotal = voteTotal;
	}

	/** 
	* 发起人性别
	* @return
	*/
	public String getOwnerGender() {
		return ownerGender;
	}

	/** 
	* 发起人性别
	* @param ownerGender
	*/
	public void setOwnerGender(String ownerGender) {
		this.ownerGender = ownerGender;
	}

	/** 
	* 回复的投票数
	* @return
	*/
	public Integer getSumtalks() {
		return sumtalks;
	}

	/** 
	* 回复的投票数
	* @param sumtalks
	*/
	public void setSumtalks(Integer sumtalks) {
		this.sumtalks = sumtalks;
	}

	/** 
	* 投票讨论
	* @return
	*/
	public List<VoteTalk> getVoteTalks() {
		return voteTalks;
	}

	/** 
	* 投票讨论
	* @param voteTalks
	*/
	public void setVoteTalks(List<VoteTalk> voteTalks) {
		this.voteTalks = voteTalks;
	}

	/** 
	* 是否过期（1可投票0不可投票）
	* @return
	*/
	public String getEnabled() {
		return enabled;
	}

	/** 
	* 是否过期（1可投票0不可投票）
	* @param enabled
	*/
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/** 
	* 是否投票0未投票非0已投票
	* @return
	*/
	public Integer getVoterChooseNum() {
		return voterChooseNum;
	}

	/** 
	* 是否投票0未投票非0已投票
	* @param voterChooseNum
	*/
	public void setVoterChooseNum(Integer voterChooseNum) {
		this.voterChooseNum = voterChooseNum;
	}

	/** 
	* 选项投票最多的
	* @return
	*/
	public List<VoteChoose> getMostChooses() {
		return mostChooses;
	}

	/** 
	* 选项投票最多的
	* @param mostChooses
	*/
	public void setMostChooses(List<VoteChoose> mostChooses) {
		this.mostChooses = mostChooses;
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
	* 推送信息
	* @return
	*/
	public String getShareMsg() {
		return shareMsg;
	}

	/** 
	* 推送信息
	* @param shareMsg
	*/
	public void setShareMsg(String shareMsg) {
		this.shareMsg = shareMsg;
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
	* 删除标识 0未删除，1预删除
	* @param delState
	*/
	public void setDelState(Integer delState) {
		this.delState = delState;
	}

	/** 
	* 删除标识 0未删除，1预删除
	* @return
	*/
	public Integer getDelState() {
		return delState;
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
	* 是否已读
	* @return
	*/
	public Integer getIsRead() {
		return isRead;
	}

	/** 
	* 是否已读
	* @param isRead
	*/
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	/** 
	* 附件数量
	* @return
	*/
	public Integer getFileNum() {
		return fileNum;
	}

	/** 
	* 附件数量
	* @param fileNum
	*/
	public void setFileNum(Integer fileNum) {
		this.fileNum = fileNum;
	}
}
