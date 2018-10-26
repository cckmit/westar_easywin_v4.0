package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 积分评定模块
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JfMod {
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
	* 类型排序
	*/
	@Filed
	private Integer busId;
	/** 
	* 类型名称
	*/
	@Filed
	private String busType;

	/****************以上主要为系统表字段********************/
	/** 
	* 模块名称
	*/
	private String modName;
	/** 
	* 模块创建时间爱你
	*/
	private String modDateTime;
	/** 
	* 模块发布时间
	*/
	private String modReaseDate;
	/** 
	* 查询年份
	*/
	private String searchYear;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 积分评定集合信息
	*/
	private List<JfScore> listJfScores;
	/** 
	* 评分人员信息
	*/
	private Integer pfUserId;
	private String pfUserName;
	private String pfUserGender;
	/** 
	* 得分人员信息
	*/
	private Integer dfUserId;
	private String dfUserName;
	private String dfUserGender;
	private String jfzbTypeName;
	private String leveTwo;

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
	* 类型排序
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 类型排序
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 类型名称
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 类型名称
	* @return
	*/
	public String getBusType() {
		return busType;
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
	* 积分评定集合信息
	* @return
	*/
	public List<JfScore> getListJfScores() {
		return listJfScores;
	}

	/** 
	* 积分评定集合信息
	* @param listJfScores
	*/
	public void setListJfScores(List<JfScore> listJfScores) {
		this.listJfScores = listJfScores;
	}

	/** 
	* 得分人员信息
	* @return
	*/
	public Integer getDfUserId() {
		return dfUserId;
	}

	/** 
	* 得分人员信息
	* @param dfUserId
	*/
	public void setDfUserId(Integer dfUserId) {
		this.dfUserId = dfUserId;
	}

	public String getDfUserName() {
		return dfUserName;
	}

	public void setDfUserName(String dfUserName) {
		this.dfUserName = dfUserName;
	}

	public String getDfUserGender() {
		return dfUserGender;
	}

	public void setDfUserGender(String dfUserGender) {
		this.dfUserGender = dfUserGender;
	}

	/** 
	* 评分人员信息
	* @return
	*/
	public Integer getPfUserId() {
		return pfUserId;
	}

	/** 
	* 评分人员信息
	* @param pfUserId
	*/
	public void setPfUserId(Integer pfUserId) {
		this.pfUserId = pfUserId;
	}

	public String getPfUserName() {
		return pfUserName;
	}

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
	* 模块创建时间爱你
	* @return
	*/
	public String getModDateTime() {
		return modDateTime;
	}

	/** 
	* 模块创建时间爱你
	* @param modDateTime
	*/
	public void setModDateTime(String modDateTime) {
		this.modDateTime = modDateTime;
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
