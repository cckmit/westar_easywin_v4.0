package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 回答讨论
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AnsTalk {
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
	* 讨论父ID
	*/
	@Filed
	private Integer parentId;
	/** 
	* 企业编号
	*/
	@Filed
	private Integer comId;
	/** 
	* 所属回答
	*/
	@Filed
	private Integer ansId;
	/** 
	* 回复人
	*/
	@Filed
	private Integer talker;
	/** 
	* 发言人
	*/
	@Filed
	private Integer ptalker;
	/** 
	* 所属提问
	*/
	@Filed
	private Integer quesId;
	/** 
	* 讨论内容
	*/
	@Filed
	private String talkContent;

	/****************以上主要为系统表字段********************/
	/** 
	* 讨论人姓名
	*/
	private String talkerName;
	/** 
	* 讨论人小头像
	*/
	private String talkerSmlImgUuid;
	private String talkerSmlImgName;
	/** 
	* 讨论人性别
	*/
	private String talkerGender;
	/** 
	* 父讨论人姓名
	*/
	private String ptalkerName;
	/** 
	* 父讨论人姓名
	*/
	private String pcontent;
	private Integer isLeaf;
	/** 
	* 附件
	*/
	private List<QasTalkFile> listQasTalkFile;

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
	* 所属回答
	* @param ansId
	*/
	public void setAnsId(Integer ansId) {
		this.ansId = ansId;
	}

	/** 
	* 所属回答
	* @return
	*/
	public Integer getAnsId() {
		return ansId;
	}

	/** 
	* 回复人
	* @param talker
	*/
	public void setTalker(Integer talker) {
		this.talker = talker;
	}

	/** 
	* 回复人
	* @return
	*/
	public Integer getTalker() {
		return talker;
	}

	/** 
	* 发言人
	* @param ptalker
	*/
	public void setPtalker(Integer ptalker) {
		this.ptalker = ptalker;
	}

	/** 
	* 发言人
	* @return
	*/
	public Integer getPtalker() {
		return ptalker;
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
	* 讨论内容
	* @param talkContent
	*/
	public void setTalkContent(String talkContent) {
		this.talkContent = talkContent;
	}

	/** 
	* 讨论内容
	* @return
	*/
	public String getTalkContent() {
		return talkContent;
	}

	/** 
	* 讨论人姓名
	* @return
	*/
	public String getTalkerName() {
		return talkerName;
	}

	/** 
	* 讨论人姓名
	* @param talkerName
	*/
	public void setTalkerName(String talkerName) {
		this.talkerName = talkerName;
	}

	/** 
	* 讨论人小头像
	* @return
	*/
	public String getTalkerSmlImgUuid() {
		return talkerSmlImgUuid;
	}

	/** 
	* 讨论人小头像
	* @param talkerSmlImgUuid
	*/
	public void setTalkerSmlImgUuid(String talkerSmlImgUuid) {
		this.talkerSmlImgUuid = talkerSmlImgUuid;
	}

	public String getTalkerSmlImgName() {
		return talkerSmlImgName;
	}

	public void setTalkerSmlImgName(String talkerSmlImgName) {
		this.talkerSmlImgName = talkerSmlImgName;
	}

	/** 
	* 讨论人性别
	* @return
	*/
	public String getTalkerGender() {
		return talkerGender;
	}

	/** 
	* 讨论人性别
	* @param talkerGender
	*/
	public void setTalkerGender(String talkerGender) {
		this.talkerGender = talkerGender;
	}

	/** 
	* 父讨论人姓名
	* @return
	*/
	public String getPtalkerName() {
		return ptalkerName;
	}

	/** 
	* 父讨论人姓名
	* @param ptalkerName
	*/
	public void setPtalkerName(String ptalkerName) {
		this.ptalkerName = ptalkerName;
	}

	/** 
	* 父讨论人姓名
	* @return
	*/
	public String getPcontent() {
		return pcontent;
	}

	/** 
	* 父讨论人姓名
	* @param pcontent
	*/
	public void setPcontent(String pcontent) {
		this.pcontent = pcontent;
	}

	public Integer getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	/** 
	* 附件
	* @return
	*/
	public List<QasTalkFile> getListQasTalkFile() {
		return listQasTalkFile;
	}

	/** 
	* 附件
	* @param listQasTalkFile
	*/
	public void setListQasTalkFile(List<QasTalkFile> listQasTalkFile) {
		this.listQasTalkFile = listQasTalkFile;
	}
}
