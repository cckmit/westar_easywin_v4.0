package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 问答中心提问
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Question {
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
	* 创建人
	*/
	@Filed
	private Integer userId;
	/** 
	* 问题 有字数限制便于查询
	*/
	@Filed
	private String title;
	/** 
	* 提问是否关闭 0关闭 1开启
	*/
	@Filed
	private String state;
	/** 
	* 问题补充
	*/
	@Filed
	private String content;
	/** 
	* 采纳的答案
	*/
	@Filed
	private Integer cnAns;
	/** 
	* 删除标识 0未删除，1预删除
	*/
	@Filed
	private Integer delState;

	/****************以上主要为系统表字段********************/
	/** 
	* 查询人员
	*/
	private Integer sessionUser;
	/** 
	* 问题的追问对应的图片
	*/
	private List<QuesFile> listQuesFiles;
	/** 
	* 问题的回答
	*/
	private List<Answer> listAns;
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
	private String userName;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 发起人小头像
	*/
	private String imgUuid;
	private String imgName;
	/** 
	* 回答总数
	*/
	private Integer ansTotal;
	/** 
	* 当前操作员回答数
	*/
	private Integer ansNum;
	/** 
	* 是否已读
	*/
	private Integer isRead;
	/** 
	* 采纳的答案
	*/
	private String cnAnsContent;
	/** 
	* 答案提交时间
	*/
	private String ansDate;
	/** 
	* 答案提交人
	*/
	private String ansUserName;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 分享信息
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
	* 问题所有的回答数
	*/
	private Integer allAnswers;
	/** 
	* 附件总数
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
	* 创建人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 问题 有字数限制便于查询
	* @param title
	*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** 
	* 问题 有字数限制便于查询
	* @return
	*/
	public String getTitle() {
		return title;
	}

	/** 
	* 提问是否关闭 0关闭 1开启
	* @param state
	*/
	public void setState(String state) {
		this.state = state;
	}

	/** 
	* 提问是否关闭 0关闭 1开启
	* @return
	*/
	public String getState() {
		return state;
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
	* 问题的回答
	* @return
	*/
	public List<Answer> getListAns() {
		return listAns;
	}

	/** 
	* 问题的回答
	* @param listAns
	*/
	public void setListAns(List<Answer> listAns) {
		this.listAns = listAns;
	}

	/** 
	* 问题补充
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 问题补充
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 问题的追问对应的图片
	* @return
	*/
	public List<QuesFile> getListQuesFiles() {
		return listQuesFiles;
	}

	/** 
	* 问题的追问对应的图片
	* @param listQuesFiles
	*/
	public void setListQuesFiles(List<QuesFile> listQuesFiles) {
		this.listQuesFiles = listQuesFiles;
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
	* 发起人姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 发起人姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 发起人小头像
	* @return
	*/
	public String getImgUuid() {
		return imgUuid;
	}

	/** 
	* 发起人小头像
	* @param imgUuid
	*/
	public void setImgUuid(String imgUuid) {
		this.imgUuid = imgUuid;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	/** 
	* 回答总数
	* @return
	*/
	public Integer getAnsTotal() {
		return ansTotal;
	}

	/** 
	* 回答总数
	* @param ansTotal
	*/
	public void setAnsTotal(Integer ansTotal) {
		this.ansTotal = ansTotal;
	}

	/** 
	* 当前操作员回答数
	* @return
	*/
	public Integer getAnsNum() {
		return ansNum;
	}

	/** 
	* 当前操作员回答数
	* @param ansNum
	*/
	public void setAnsNum(Integer ansNum) {
		this.ansNum = ansNum;
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
	* 采纳的答案
	* @param cnAns
	*/
	public void setCnAns(Integer cnAns) {
		this.cnAns = cnAns;
	}

	/** 
	* 采纳的答案
	* @return
	*/
	public Integer getCnAns() {
		return cnAns;
	}

	/** 
	* 采纳的答案
	* @return
	*/
	public String getCnAnsContent() {
		return cnAnsContent;
	}

	/** 
	* 采纳的答案
	* @param cnAnsContent
	*/
	public void setCnAnsContent(String cnAnsContent) {
		this.cnAnsContent = cnAnsContent;
	}

	/** 
	* 答案提交时间
	* @return
	*/
	public String getAnsDate() {
		return ansDate;
	}

	/** 
	* 答案提交时间
	* @param ansDate
	*/
	public void setAnsDate(String ansDate) {
		this.ansDate = ansDate;
	}

	/** 
	* 答案提交人
	* @return
	*/
	public String getAnsUserName() {
		return ansUserName;
	}

	/** 
	* 答案提交人
	* @param ansUserName
	*/
	public void setAnsUserName(String ansUserName) {
		this.ansUserName = ansUserName;
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
	* 分享信息
	* @return
	*/
	public String getShareMsg() {
		return shareMsg;
	}

	/** 
	* 分享信息
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
	* 问题所有的回答数
	* @return
	*/
	public Integer getAllAnswers() {
		return allAnswers;
	}

	/** 
	* 问题所有的回答数
	* @param allAnswers
	*/
	public void setAllAnswers(Integer allAnswers) {
		this.allAnswers = allAnswers;
	}

	/** 
	* 附件总数
	* @return
	*/
	public Integer getFileNum() {
		return fileNum;
	}

	/** 
	* 附件总数
	* @param fileNum
	*/
	public void setFileNum(Integer fileNum) {
		this.fileNum = fileNum;
	}
}
