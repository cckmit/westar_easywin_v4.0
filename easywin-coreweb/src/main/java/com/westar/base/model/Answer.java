package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 问答中心回答
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Answer {
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
	* 所属提问
	*/
	@Filed
	private Integer quesId;
	/** 
	* 回答人主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 回答内容
	*/
	@Filed
	private String content;

	/****************以上主要为系统表字段********************/
	/** 
	* 回答的附件
	*/
	private List<AnsFile> listAnsFiles;
	/** 
	* 回答人的名字
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
	* 问题
	*/
	private String quesTitle;
	/** 
	* 回答的评论
	*/
	private List<AnsTalk> listAnsTalks;
	/** 
	* 采纳标识
	*/
	private String cnFlag;

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
	* 所属提问
	* @param quesId
	*/
	public void setQuesId(Integer quesId) {
		this.quesId = quesId;
	}

	/** 
	* 所属提问
	* @return
	*/
	public Integer getQuesId() {
		return quesId;
	}

	/** 
	* 回答人主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 回答人主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 回答内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 回答内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 回答的附件
	* @return
	*/
	public List<AnsFile> getListAnsFiles() {
		return listAnsFiles;
	}

	/** 
	* 回答的附件
	* @param listAnsFiles
	*/
	public void setListAnsFiles(List<AnsFile> listAnsFiles) {
		this.listAnsFiles = listAnsFiles;
	}

	/** 
	* 回答人的名字
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 回答人的名字
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 问题
	* @return
	*/
	public String getQuesTitle() {
		return quesTitle;
	}

	/** 
	* 问题
	* @param quesTitle
	*/
	public void setQuesTitle(String quesTitle) {
		this.quesTitle = quesTitle;
	}

	/** 
	* 回答的评论
	* @return
	*/
	public List<AnsTalk> getListAnsTalks() {
		return listAnsTalks;
	}

	/** 
	* 回答的评论
	* @param listAnsTalks
	*/
	public void setListAnsTalks(List<AnsTalk> listAnsTalks) {
		this.listAnsTalks = listAnsTalks;
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
	* 采纳标识
	* @return
	*/
	public String getCnFlag() {
		return cnFlag;
	}

	/** 
	* 采纳标识
	* @param cnFlag
	*/
	public void setCnFlag(String cnFlag) {
		this.cnFlag = cnFlag;
	}
}
