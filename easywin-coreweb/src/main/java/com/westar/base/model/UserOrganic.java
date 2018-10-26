package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 企业人员表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class UserOrganic {
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
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 职位
	*/
	@Filed
	private String job;
	/** 
	* 隶属部门
	*/
	@Filed
	private Integer depId;
	/** 
	* 是否是管理人员
	*/
	@Filed
	private String admin;
	/** 
	* 启用状态  参见数据字典enabled
	*/
	@Filed
	private String enabled;
	/** 
	* 记录最后一次在线时间
	*/
	@Filed
	private String lastOnlineTime;
	/** 
	* 积分
	*/
	@Filed
	private Integer jifenScore;
	/** 
	* 大头像地址
	*/
	@Filed
	private String bigHeadPortrait;
	/** 
	* 中头像地址
	*/
	@Filed
	private String mediumHeadPortrait;
	/** 
	* 小头像地址
	*/
	@Filed
	private String smallHeadPortrait;
	/** 
	* 是否完善信息 默认不需要
	*/
	@Filed
	private String alterInfo;
	/** 
	* 是否为首席 默认不是
	*/
	@Filed
	private String isChief;
	/** 
	* 是否在团队允许服务内标识；0不是，1是；  参见常量内种inService
	*/
	@Filed
	private Integer inService;
	/** 
	* 人员编号
	*/
	@Filed
	private String enrollNumber;
	/** 
	* 个人默认显示组主键
	*/
	@Filed
	private Integer defShowGrpId;

	/****************以上主要为系统表字段********************/

	/****************以上为自己添加字段********************/
	public UserOrganic() {
	}

	public UserOrganic(Integer id, Integer comId) {
		super();
		this.id = id;
		this.comId = comId;
	}

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
	* 人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 职位
	* @param job
	*/
	public void setJob(String job) {
		this.job = job;
	}

	/** 
	* 职位
	* @return
	*/
	public String getJob() {
		return job;
	}

	/** 
	* 隶属部门
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	/** 
	* 隶属部门
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 是否是管理人员
	* @param admin
	*/
	public void setAdmin(String admin) {
		this.admin = admin;
	}

	/** 
	* 是否是管理人员
	* @return
	*/
	public String getAdmin() {
		return admin;
	}

	/** 
	* 启用状态  参见数据字典enabled
	* @param enabled
	*/
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/** 
	* 启用状态  参见数据字典enabled
	* @return
	*/
	public String getEnabled() {
		return enabled;
	}

	/** 
	* 记录最后一次在线时间
	* @param lastOnlineTime
	*/
	public void setLastOnlineTime(String lastOnlineTime) {
		this.lastOnlineTime = lastOnlineTime;
	}

	/** 
	* 记录最后一次在线时间
	* @return
	*/
	public String getLastOnlineTime() {
		return lastOnlineTime;
	}

	/** 
	* 积分
	* @param jifenScore
	*/
	public void setJifenScore(Integer jifenScore) {
		this.jifenScore = jifenScore;
	}

	/** 
	* 积分
	* @return
	*/
	public Integer getJifenScore() {
		return jifenScore;
	}

	/** 
	* 大头像地址
	* @param bigHeadPortrait
	*/
	public void setBigHeadPortrait(String bigHeadPortrait) {
		this.bigHeadPortrait = bigHeadPortrait;
	}

	/** 
	* 大头像地址
	* @return
	*/
	public String getBigHeadPortrait() {
		return bigHeadPortrait;
	}

	/** 
	* 中头像地址
	* @param mediumHeadPortrait
	*/
	public void setMediumHeadPortrait(String mediumHeadPortrait) {
		this.mediumHeadPortrait = mediumHeadPortrait;
	}

	/** 
	* 中头像地址
	* @return
	*/
	public String getMediumHeadPortrait() {
		return mediumHeadPortrait;
	}

	/** 
	* 小头像地址
	* @param smallHeadPortrait
	*/
	public void setSmallHeadPortrait(String smallHeadPortrait) {
		this.smallHeadPortrait = smallHeadPortrait;
	}

	/** 
	* 小头像地址
	* @return
	*/
	public String getSmallHeadPortrait() {
		return smallHeadPortrait;
	}

	/** 
	* 是否完善信息 默认不需要
	* @param alterInfo
	*/
	public void setAlterInfo(String alterInfo) {
		this.alterInfo = alterInfo;
	}

	/** 
	* 是否完善信息 默认不需要
	* @return
	*/
	public String getAlterInfo() {
		return alterInfo;
	}

	/** 
	* 是否为首席 默认不是
	* @param isChief
	*/
	public void setIsChief(String isChief) {
		this.isChief = isChief;
	}

	/** 
	* 是否为首席 默认不是
	* @return
	*/
	public String getIsChief() {
		return isChief;
	}

	/** 
	* 是否在团队允许服务内标识；0不是，1是；  参见常量内种inService
	* @param inService
	*/
	public void setInService(Integer inService) {
		this.inService = inService;
	}

	/** 
	* 是否在团队允许服务内标识；0不是，1是；  参见常量内种inService
	* @return
	*/
	public Integer getInService() {
		return inService;
	}

	@Override
	public String toString() {
		return "UserOrganic [id=" + id + ", recordCreateTime=" + recordCreateTime + ", comId=" + comId + ", userId="
				+ userId + ", job=" + job + ", depId=" + depId + ", admin=" + admin + ", enabled=" + enabled
				+ ", lastOnlineTime=" + lastOnlineTime + ", jifenScore=" + jifenScore + ", bigHeadPortrait="
				+ bigHeadPortrait + ", mediumHeadPortrait=" + mediumHeadPortrait + ", smallHeadPortrait="
				+ smallHeadPortrait + ", alterInfo=" + alterInfo + ", isChief=" + isChief + ", inService=" + inService
				+ "]";
	}

	/** 
	* 人员编号
	* @param enrollNumber
	*/
	public void setEnrollNumber(String enrollNumber) {
		this.enrollNumber = enrollNumber;
	}

	/** 
	* 人员编号
	* @return
	*/
	public String getEnrollNumber() {
		return enrollNumber;
	}

	/** 
	* 个人默认显示组主键
	* @param defShowGrpId
	*/
	public void setDefShowGrpId(Integer defShowGrpId) {
		this.defShowGrpId = defShowGrpId;
	}

	/** 
	* 个人默认显示组主键
	* @return
	*/
	public Integer getDefShowGrpId() {
		return defShowGrpId;
	}
}
