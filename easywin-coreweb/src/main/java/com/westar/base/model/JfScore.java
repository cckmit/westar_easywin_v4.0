package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 积分评定
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JfScore {
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
	* 评分模块
	*/
	@Filed
	private Integer jfModId;
	/** 
	* 评分人员
	*/
	@Filed
	private Integer pfUserId;
	/** 
	* 得分人员
	*/
	@Filed
	private Integer dfUserId;
	/** 
	* 积分指标主键
	*/
	@Filed
	private Integer jfzbId;
	/** 
	* 得分
	*/
	@Filed
	private String score;
	/** 
	* 说明
	*/
	@Filed
	private String remark;

	/****************以上主要为系统表字段********************/
	/** 
	* 得分人员信息
	*/
	private String dfUserName;
	private String dfUserGender;
	/** 
	* 模块发布时间
	*/
	private String modReaseDate;
	/** 
	* 查询年份
	*/
	private String searchYear;
	/** 
	* 业务主键
	*/
	private Integer busId;
	/** 
	* 业务类型
	*/
	private String busType;
	/** 
	* 模块名称
	*/
	private String modName;
	/** 
	* 得分状态0未评分1不评分2已评分
	*/
	private String scoreState;
	/** 
	* 是否为直属上级
	*/
	private String dirLeaderState;
	private String jfTop;
	private String jfBottom;
	/** 
	* 评分人员信息
	*/
	private String pfUserName;
	private String pfUserGender;
	private String jfzbTypeName;
	private String leveTwo;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;

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
	* 评分模块
	* @param jfModId
	*/
	public void setJfModId(Integer jfModId) {
		this.jfModId = jfModId;
	}

	/** 
	* 评分模块
	* @return
	*/
	public Integer getJfModId() {
		return jfModId;
	}

	/** 
	* 评分人员
	* @param pfUserId
	*/
	public void setPfUserId(Integer pfUserId) {
		this.pfUserId = pfUserId;
	}

	/** 
	* 评分人员
	* @return
	*/
	public Integer getPfUserId() {
		return pfUserId;
	}

	/** 
	* 得分人员
	* @param dfUserId
	*/
	public void setDfUserId(Integer dfUserId) {
		this.dfUserId = dfUserId;
	}

	/** 
	* 得分人员
	* @return
	*/
	public Integer getDfUserId() {
		return dfUserId;
	}

	/** 
	* 积分指标主键
	* @param jfzbId
	*/
	public void setJfzbId(Integer jfzbId) {
		this.jfzbId = jfzbId;
	}

	/** 
	* 积分指标主键
	* @return
	*/
	public Integer getJfzbId() {
		return jfzbId;
	}

	/** 
	* 得分
	* @param score
	*/
	public void setScore(String score) {
		this.score = score;
	}

	/** 
	* 得分
	* @return
	*/
	public String getScore() {
		return score;
	}

	/** 
	* 说明
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	* 说明
	* @return
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 得分人员信息
	* @return
	*/
	public String getDfUserName() {
		return dfUserName;
	}

	/** 
	* 得分人员信息
	* @param dfUserName
	*/
	public void setDfUserName(String dfUserName) {
		this.dfUserName = dfUserName;
	}

	public String getDfUserGender() {
		return dfUserGender;
	}

	public void setDfUserGender(String dfUserGender) {
		this.dfUserGender = dfUserGender;
	}

	public String getJfTop() {
		return jfTop;
	}

	public void setJfTop(String jfTop) {
		this.jfTop = jfTop;
	}

	public String getJfBottom() {
		return jfBottom;
	}

	public void setJfBottom(String jfBottom) {
		this.jfBottom = jfBottom;
	}

	/** 
	* 业务主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 业务主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 业务类型
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 业务类型
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 模块名称
	* @return
	*/
	public String getModName() {
		return modName;
	}

	/** 
	* 模块名称
	* @param modName
	*/
	public void setModName(String modName) {
		this.modName = modName;
	}

	/** 
	* 得分状态0未评分1不评分2已评分
	* @return
	*/
	public String getScoreState() {
		return scoreState;
	}

	/** 
	* 得分状态0未评分1不评分2已评分
	* @param scoreState
	*/
	public void setScoreState(String scoreState) {
		this.scoreState = scoreState;
	}

	/** 
	* 评分人员信息
	* @return
	*/
	public String getPfUserName() {
		return pfUserName;
	}

	/** 
	* 评分人员信息
	* @param pfUserName
	*/
	public void setPfUserName(String pfUserName) {
		this.pfUserName = pfUserName;
	}

	public String getPfUserGender() {
		return pfUserGender;
	}

	public void setPfUserGender(String pfUserGender) {
		this.pfUserGender = pfUserGender;
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
	* 是否为直属上级
	* @return
	*/
	public String getDirLeaderState() {
		return dirLeaderState;
	}

	/** 
	* 是否为直属上级
	* @param dirLeaderState
	*/
	public void setDirLeaderState(String dirLeaderState) {
		this.dirLeaderState = dirLeaderState;
	}

	/** 
	* 模块发布时间
	* @return
	*/
	public String getModReaseDate() {
		return modReaseDate;
	}

	/** 
	* 模块发布时间
	* @param modReaseDate
	*/
	public void setModReaseDate(String modReaseDate) {
		this.modReaseDate = modReaseDate;
	}

	public String getJfzbTypeName() {
		return jfzbTypeName;
	}

	public void setJfzbTypeName(String jfzbTypeName) {
		this.jfzbTypeName = jfzbTypeName;
	}

	public String getLeveTwo() {
		return leveTwo;
	}

	public void setLeveTwo(String leveTwo) {
		this.leveTwo = leveTwo;
	}

	/** 
	* 查询年份
	* @return
	*/
	public String getSearchYear() {
		return searchYear;
	}

	/** 
	* 查询年份
	* @param searchYear
	*/
	public void setSearchYear(String searchYear) {
		this.searchYear = searchYear;
	}
}
