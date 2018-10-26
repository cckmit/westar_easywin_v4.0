package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 企业信息表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Organic {
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
	private Integer orgNum;
	/** 
	* 企业名称
	*/
	@Filed
	private String orgName;
	/** 
	* Logo地址
	*/
	@Filed
	private String logo;
	/** 
	* 通讯地址
	*/
	@Filed
	private String address;
	/** 
	* 启用状态经过邮箱验证后可用
	*/
	@Filed
	private String enabled;

	/****************以上主要为系统表字段********************/
	/** 
	* LOGO图片信息
	*/
	private String logoUuid;
	private String logoName;
	/** 
	* 团队注册人员名称
	*/
	private String linkerName;
	/** 
	* 团队注册人员邮箱
	*/
	private String linkerEmail;
	/** 
	* 注册人移动电话
	*/
	private String linkerMovePhone;
	/** 
	* 是否为系统用户
	*/
	private String isSysUser;
	/** 
	* 是正在申请
	*/
	private String isApplying;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 团队注册人员id
	*/
	private Integer userId;
	/** 
	* 团队激活成员数目
	*/
	private Integer members;
	/** 
	* 团队成员上限数目
	*/
	private Integer usersUpperLimit;
	private Integer inService;
	/** 
	* boolean标识
	*/
	private boolean canDo;
	/** 
	* 提示信息
	*/
	private String msg;

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
	* @param orgNum
	*/
	public void setOrgNum(Integer orgNum) {
		this.orgNum = orgNum;
	}

	/** 
	* 企业编号
	* @return
	*/
	public Integer getOrgNum() {
		return orgNum;
	}

	/** 
	* 企业名称
	* @param orgName
	*/
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/** 
	* 企业名称
	* @return
	*/
	public String getOrgName() {
		return orgName;
	}

	/** 
	* Logo地址
	* @param logo
	*/
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/** 
	* Logo地址
	* @return
	*/
	public String getLogo() {
		return logo;
	}

	/** 
	* 通讯地址
	* @param address
	*/
	public void setAddress(String address) {
		this.address = address;
	}

	/** 
	* 通讯地址
	* @return
	*/
	public String getAddress() {
		return address;
	}

	/** 
	* 启用状态经过邮箱验证后可用
	* @param enabled
	*/
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/** 
	* 启用状态经过邮箱验证后可用
	* @return
	*/
	public String getEnabled() {
		return enabled;
	}

	/** 
	* LOGO图片信息
	* @return
	*/
	public String getLogoUuid() {
		return logoUuid;
	}

	/** 
	* LOGO图片信息
	* @param logoUuid
	*/
	public void setLogoUuid(String logoUuid) {
		this.logoUuid = logoUuid;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	/** 
	* 是否为系统用户
	* @return
	*/
	public String getIsSysUser() {
		return isSysUser;
	}

	/** 
	* 是否为系统用户
	* @param isSysUser
	*/
	public void setIsSysUser(String isSysUser) {
		this.isSysUser = isSysUser;
	}

	/** 
	* 是正在申请
	* @return
	*/
	public String getIsApplying() {
		return isApplying;
	}

	/** 
	* 是正在申请
	* @param isApplying
	*/
	public void setIsApplying(String isApplying) {
		this.isApplying = isApplying;
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
	* 团队注册人员id
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 团队注册人员id
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 团队注册人员名称
	* @return
	*/
	public String getLinkerName() {
		return linkerName;
	}

	/** 
	* 团队注册人员名称
	* @param linkerName
	*/
	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
	}

	/** 
	* 团队注册人员邮箱
	* @return
	*/
	public String getLinkerEmail() {
		return linkerEmail;
	}

	/** 
	* 团队注册人员邮箱
	* @param linkerEmail
	*/
	public void setLinkerEmail(String linkerEmail) {
		this.linkerEmail = linkerEmail;
	}

	/** 
	* 注册人移动电话
	* @return
	*/
	public String getLinkerMovePhone() {
		return linkerMovePhone;
	}

	/** 
	* 注册人移动电话
	* @param linkerMovePhone
	*/
	public void setLinkerMovePhone(String linkerMovePhone) {
		this.linkerMovePhone = linkerMovePhone;
	}

	/** 
	* 团队激活成员数目
	* @return
	*/
	public Integer getMembers() {
		return members;
	}

	/** 
	* 团队激活成员数目
	* @param members
	*/
	public void setMembers(Integer members) {
		this.members = members;
	}

	/** 
	* 团队成员上限数目
	* @return
	*/
	public Integer getUsersUpperLimit() {
		return usersUpperLimit;
	}

	/** 
	* 团队成员上限数目
	* @param usersUpperLimit
	*/
	public void setUsersUpperLimit(Integer usersUpperLimit) {
		this.usersUpperLimit = usersUpperLimit;
	}

	public Integer getInService() {
		return inService;
	}

	public void setInService(Integer inService) {
		this.inService = inService;
	}

	public boolean isCanDo() {
		return canDo;
	}

	/** 
	* boolean标识
	* @param canDo
	*/
	public void setCanDo(boolean canDo) {
		this.canDo = canDo;
	}

	/** 
	* 提示信息
	* @return
	*/
	public String getMsg() {
		return msg;
	}

	/** 
	* 提示信息
	* @param msg
	*/
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
