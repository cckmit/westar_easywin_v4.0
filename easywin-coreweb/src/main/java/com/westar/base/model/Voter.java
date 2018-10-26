package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 投票人
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Voter {
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
	* 所属投票
	*/
	@Filed
	private Integer voteId;
	/** 
	* 所投票
	*/
	@Filed
	private Integer chooseId;
	/** 
	* 投票人
	*/
	@Filed
	private Integer voter;

	/****************以上主要为系统表字段********************/
	/** 
	* 投票人的姓名
	*/
	private String voterName;
	/** 
	* 小图片信息
	*/
	private String smImgUuid;
	private String smImgName;
	private String voterGender;

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
	* 所属投票
	* @param voteId
	*/
	public void setVoteId(Integer voteId) {
		this.voteId = voteId;
	}

	/** 
	* 所属投票
	* @return
	*/
	public Integer getVoteId() {
		return voteId;
	}

	/** 
	* 所投票
	* @param chooseId
	*/
	public void setChooseId(Integer chooseId) {
		this.chooseId = chooseId;
	}

	/** 
	* 所投票
	* @return
	*/
	public Integer getChooseId() {
		return chooseId;
	}

	/** 
	* 投票人
	* @param voter
	*/
	public void setVoter(Integer voter) {
		this.voter = voter;
	}

	/** 
	* 投票人
	* @return
	*/
	public Integer getVoter() {
		return voter;
	}

	/** 
	* 投票人的姓名
	* @return
	*/
	public String getVoterName() {
		return voterName;
	}

	/** 
	* 投票人的姓名
	* @param voterName
	*/
	public void setVoterName(String voterName) {
		this.voterName = voterName;
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

	public String getVoterGender() {
		return voterGender;
	}

	public void setVoterGender(String voterGender) {
		this.voterGender = voterGender;
	}
}
