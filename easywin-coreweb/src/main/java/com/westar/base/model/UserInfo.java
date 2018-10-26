package com.westar.base.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.core.web.DataDicContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 人员信息表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 4034583568024738594L;
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
	* 人员名称
	*/
	@Filed
	private String userName;
	/** 
	* 拼音 全拼
	*/
	@Filed
	private String allSpelling;
	/** 
	* 拼音 首字母
	*/
	@Filed
	private String firstSpelling;
	/** 
	* 登陆密码
	*/
	@Filed
	private String password;
	/** 
	* 联系人固定电话
	*/
	@Filed
	private String linePhone;
	/** 
	* 移动电话
	*/
	@Filed
	private String movePhone;
	/** 
	* 电子邮件
	*/
	@Filed
	private String email;
	/** 
	* 微信
	*/
	@Filed
	private String wechat;
	/** 
	* 0女1男
	*/
	@Filed
	private String gender;
	/** 
	* 生日
	*/
	@Filed
	private String birthday;
	/** 
	* 登录别名
	*/
	@Filed
	private String nickname;
	/** 
	* qq
	*/
	@Filed
	private String qq;
	/** 
	* 个性签名
	*/
	@Filed
	private String selfIntr;

	/****************以上主要为系统表字段********************/
	
	/**
	 * 无参构造
	 */
	public UserInfo() {}
	public UserInfo(Integer comId,Integer userId) {
		this.comId = comId;
		this.id = userId;
	}
	
	private String enrollNumber;
	private Integer comId;
	private String enabled;
	private String remark;
	private String lastOnlineTime;
	private String admin;
	private String job;
	private Integer depId;
	private String checkState;
	private String bigHeadPortrait;
	private String mediumHeadPortrait;
	private String smallHeadPortrait;
	private Integer jifenScore;
	//操作的IP地址
	private String optIP;
	/** 
	* 是否在线0否1是2表示查询所有
	*/
	private String ifOnline;
	/** 
	* 性别
	*/
	private String genderName;
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
	* LOGO图片信息
	*/
	private String logoUuid;
	private String logoName;
	/** 
	* 部门名称
	*/
	private String depName;
	/** 
	* 模糊查询的条件
	*/
	private String condition;
	private String orgName;
	private List<Organic> listOrg;
	/** 
	* 根查询部门ID
	*/
	private Integer anyDepId;
	
	/**
	 * 是否只查询下属信息
	 */
	private String onlySubState;
	/** 
	* 邀请人的邮箱
	*/
	private String[] invUsers;
	/** 
	* 模糊查询条件，也可以是机构名或者拼音
	*/
	private String anyNameLike;
	/** 
	* 等级名称
	*/
	private String levName;
	/** 
	* 下一阶段的最低分数
	*/
	private Integer nextLevJifen;
	/** 
	* 本阶段最少分数
	*/
	private Integer minLevJifen;
	/** 
	* 用户企业关系表的主键
	*/
	private Integer userOrganicId;
	/** 
	* 个人积分与下一等级间百分比
	*/
	private String jiFenPercent;
	/** 
	* 个人总积分
	*/
	private Integer totalJifen;
	/** 
	* 个人总积分排名
	*/
	private Integer totalRank;
	/** 
	* 个人总积分
	*/
	private Integer monthJifen;
	/** 
	* 个人总积分排名
	*/
	private Integer monthRank;
	/** 
	* 个人总积分
	*/
	private Integer weekJifen;
	/** 
	* 个人总积分排名
	*/
	private Integer weekRank;
	/** 
	* 所属私有组主键
	*/
	private Integer grpId;
	/** 
	* 直属下属成员数
	*/
	private Integer countSub;
	private String autoEject;
	/** 
	* 把用户集合封装到user对象里面
	*/
	private List<UserInfo> listUser;
	/** 
	* 把用户分组封装到用户对象里面
	*/
	private List<SelfGroup> listGroup;
	/** 
	* 把团队集合封装到用户对象里面
	*/
	private List<Department> listDep;
	/** 
	* 总提醒数
	*/
	private Integer noReadNums;
	/** 
	* 总待办事项数
	*/
	private Integer toDoNums;
	/** 
	* 总关注未读消息数
	*/
	private Integer attenNums;
	/** 
	* 个人所有的待办任务
	*/
	private Integer taskToDoNum;
	/** 
	* 个人已完成任务
	*/
	private Integer taskDoneNum;
	/** 
	* 我的客户
	*/
	private Integer myCrmNum;
	/** 
	* 我的项目
	*/
	private Integer myItemNum;
	/** 
	* 我的汇报
	*/
	private Integer myWeeklyRepNum;
	private String alterInfo;
	/** 
	* 个人直属上级人员集合
	*/
	private List<UserInfo> listUserInfo;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String msg;
	/** 
	* 入职时间
	*/
	private String hireDate;
	/** 
	* 上次登录时间
	*/
	private String lastLoginTime;
	/** 
	* 登录次数
	*/
	private Integer loginTimes;
	/** 
	* 是否为首席默认不是
	*/
	private String isChief;
	/** 
	* 用户加入的所有团队个数
	*/
	private Integer userInOrgNums;
	/**
	 * 是否过期
	 */
	private Integer inService;
	/** 
	* 请假集合
	*/
	private List<Leave> listLive;
	/** 
	* 请假
	*/
	private Leave leave;
	/** 
	* 加班
	*/
	private OverTime overTime;
	/** 
	* 请假次数
	*/
	private Integer leaveTimeT;
	/** 
	* 请假总时长
	*/
	private String leaveTotal;
	/** 
	* 加班次数
	*/
	private Integer overTimeT;
	/** 
	* 加班总时长
	*/
	private String overTimeTotal;
	/** 
	* 考勤记录
	*/
	private AttenceRecord attenceRecord;
	/** 
	* 迟到次数
	*/
	private Integer lateTime;
	/** 
	* 早退次数
	*/
	private Integer leaveEarlyTime;
	/** 
	* 异常次数
	*/
	private Integer unusualTime;
	/** 
	* 旷工次数
	*/
	private Integer absentTime;
	/** 
	* 是否查看
	*/
	private String isView;
	/** 
	* 查看时间
	*/
	private String viewTime;
	/** 
	* 个人默认显示组主键
	*/
	private Integer defShowGrpId;
	/**
	 * 代理人主键
	 */
	private Integer forMeDoUserId;
	/**
	 * 代理人名称
	 */
	private String forMeDoUserName;
	/**
	 * 所属角色列表用逗号连接的字符串
	 */
	private String roles;

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

	public void setComId(Integer comId) {
		this.comId = comId;
	}

	public Integer getComId() {
		return comId;
	}

	/** 
	* 人员名称
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 人员名称
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 拼音 全拼
	* @param allSpelling
	*/
	public void setAllSpelling(String allSpelling) {
		this.allSpelling = allSpelling;
	}

	/** 
	* 拼音 全拼
	* @return
	*/
	public String getAllSpelling() {
		return allSpelling;
	}

	/** 
	* 拼音 首字母
	* @param firstSpelling
	*/
	public void setFirstSpelling(String firstSpelling) {
		this.firstSpelling = firstSpelling;
	}

	/** 
	* 拼音 首字母
	* @return
	*/
	public String getFirstSpelling() {
		return firstSpelling;
	}

	/** 
	* 登陆密码
	* @param password
	*/
	public void setPassword(String password) {
		this.password = password;
	}

	/** 
	* 登陆密码
	* @return
	*/
	public String getPassword() {
		return password;
	}

	/** 
	* 联系人固定电话
	* @param linePhone
	*/
	public void setLinePhone(String linePhone) {
		this.linePhone = linePhone;
	}

	/** 
	* 联系人固定电话
	* @return
	*/
	public String getLinePhone() {
		return linePhone;
	}

	/** 
	* 移动电话
	* @param movePhone
	*/
	public void setMovePhone(String movePhone) {
		this.movePhone = movePhone;
	}

	/** 
	* 移动电话
	* @return
	*/
	public String getMovePhone() {
		return movePhone;
	}

	/** 
	* 电子邮件
	* @param email
	*/
	public void setEmail(String email) {
		this.email = email;
	}

	/** 
	* 电子邮件
	* @return
	*/
	public String getEmail() {
		return email;
	}

	/** 
	* 微信
	* @param wechat
	*/
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	/** 
	* 微信
	* @return
	*/
	public String getWechat() {
		return wechat;
	}

	public void setBigHeadPortrait(String bigHeadPortrait) {
		this.bigHeadPortrait = bigHeadPortrait;
	}

	public String getBigHeadPortrait() {
		return bigHeadPortrait;
	}

	public void setMediumHeadPortrait(String mediumHeadPortrait) {
		this.mediumHeadPortrait = mediumHeadPortrait;
	}

	public String getMediumHeadPortrait() {
		return mediumHeadPortrait;
	}

	public void setSmallHeadPortrait(String smallHeadPortrait) {
		this.smallHeadPortrait = smallHeadPortrait;
	}

	public String getSmallHeadPortrait() {
		return smallHeadPortrait;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setLastOnlineTime(String lastOnlineTime) {
		this.lastOnlineTime = lastOnlineTime;
	}

	public String getLastOnlineTime() {
		return lastOnlineTime;
	}

	/** 
	* 是否在线0否1是2表示查询所有
	* @return
	*/
	public String getIfOnline() {
		return ifOnline;
	}

	/** 
	* 是否在线0否1是2表示查询所有
	* @param ifOnline
	*/
	public void setIfOnline(String ifOnline) {
		this.ifOnline = ifOnline;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getAdmin() {
		return admin;
	}

	/** 
	* 0女1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 生日
	* @param birthday
	*/
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/** 
	* 生日
	* @return
	*/
	public String getBirthday() {
		return birthday;
	}

	/** 
	* 登录别名
	* @param nickname
	*/
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/** 
	* 登录别名
	* @return
	*/
	public String getNickname() {
		return nickname;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getJob() {
		return job;
	}

	/** 
	* 性别
	* @return
	*/
	public String getGenderName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("gender", gender);
	}

	/** 
	* 性别
	* @param genderName
	*/
	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	/** 
	* qq
	* @param qq
	*/
	public void setQq(String qq) {
		this.qq = qq;
	}

	/** 
	* qq
	* @return
	*/
	public String getQq() {
		return qq;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public Integer getDepId() {
		return depId;
	}

	/** 
	* 部门名称
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 部门名称
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/** 
	* 模糊查询的条件
	* @return
	*/
	public String getCondition() {
		return condition;
	}

	/** 
	* 模糊查询的条件
	* @param condition
	*/
	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List<Organic> getListOrg() {
		return listOrg;
	}

	public void setListOrg(List<Organic> listOrg) {
		this.listOrg = listOrg;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public String getCheckState() {
		return checkState;
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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/** 
	* 根查询部门ID
	* @return
	*/
	public Integer getAnyDepId() {
		return anyDepId;
	}

	/** 
	* 根查询部门ID
	* @param anyDepId
	*/
	public void setAnyDepId(Integer anyDepId) {
		this.anyDepId = anyDepId;
	}

	/** 
	* 邀请人的邮箱
	* @return
	*/
	public String[] getInvUsers() {
		return invUsers;
	}

	/** 
	* 邀请人的邮箱
	* @param invUsers
	*/
	public void setInvUsers(String[] invUsers) {
		this.invUsers = invUsers;
	}

	/** 
	* 模糊查询条件，也可以是机构名或者拼音
	* @return
	*/
	public String getAnyNameLike() {
		return anyNameLike;
	}

	/** 
	* 模糊查询条件，也可以是机构名或者拼音
	* @param anyNameLike
	*/
	public void setAnyNameLike(String anyNameLike) {
		this.anyNameLike = anyNameLike;
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

	public void setJifenScore(Integer jifenScore) {
		this.jifenScore = jifenScore;
	}

	public Integer getJifenScore() {
		return jifenScore;
	}

	/** 
	* 等级名称
	* @return
	*/
	public String getLevName() {
		return levName;
	}

	/** 
	* 等级名称
	* @param levName
	*/
	public void setLevName(String levName) {
		this.levName = levName;
	}

	/** 
	* 下一阶段的最低分数
	* @return
	*/
	public Integer getNextLevJifen() {
		return nextLevJifen;
	}

	/** 
	* 下一阶段的最低分数
	* @param nextLevJifen
	*/
	public void setNextLevJifen(Integer nextLevJifen) {
		this.nextLevJifen = nextLevJifen;
	}

	/** 
	* 本阶段最少分数
	* @return
	*/
	public Integer getMinLevJifen() {
		return minLevJifen;
	}

	/** 
	* 本阶段最少分数
	* @param minLevJifen
	*/
	public void setMinLevJifen(Integer minLevJifen) {
		this.minLevJifen = minLevJifen;
	}

	/** 
	* 用户企业关系表的主键
	* @return
	*/
	public Integer getUserOrganicId() {
		return userOrganicId;
	}

	/** 
	* 用户企业关系表的主键
	* @param userOrganicId
	*/
	public void setUserOrganicId(Integer userOrganicId) {
		this.userOrganicId = userOrganicId;
	}

	/** 
	* 个人积分与下一等级间百分比
	* @return
	*/
	public String getJiFenPercent() {
		return jiFenPercent;
	}

	/** 
	* 个人积分与下一等级间百分比
	* @param jiFenPercent
	*/
	public void setJiFenPercent(String jiFenPercent) {
		this.jiFenPercent = jiFenPercent;
	}

	/** 
	* 个人总积分
	* @return
	*/
	public Integer getTotalJifen() {
		return totalJifen;
	}

	/** 
	* 个人总积分
	* @param totalJifen
	*/
	public void setTotalJifen(Integer totalJifen) {
		this.totalJifen = totalJifen;
	}

	/** 
	* 个人总积分排名
	* @return
	*/
	public Integer getTotalRank() {
		return totalRank;
	}

	/** 
	* 个人总积分排名
	* @param totalRank
	*/
	public void setTotalRank(Integer totalRank) {
		this.totalRank = totalRank;
	}

	/** 
	* 个人总积分
	* @return
	*/
	public Integer getMonthJifen() {
		return monthJifen;
	}

	/** 
	* 个人总积分
	* @param monthJifen
	*/
	public void setMonthJifen(Integer monthJifen) {
		this.monthJifen = monthJifen;
	}

	/** 
	* 个人总积分排名
	* @return
	*/
	public Integer getMonthRank() {
		return monthRank;
	}

	/** 
	* 个人总积分排名
	* @param monthRank
	*/
	public void setMonthRank(Integer monthRank) {
		this.monthRank = monthRank;
	}

	/** 
	* 个人总积分
	* @return
	*/
	public Integer getWeekJifen() {
		return weekJifen;
	}

	/** 
	* 个人总积分
	* @param weekJifen
	*/
	public void setWeekJifen(Integer weekJifen) {
		this.weekJifen = weekJifen;
	}

	/** 
	* 个人总积分排名
	* @return
	*/
	public Integer getWeekRank() {
		return weekRank;
	}

	/** 
	* 个人总积分排名
	* @param weekRank
	*/
	public void setWeekRank(Integer weekRank) {
		this.weekRank = weekRank;
	}

	/** 
	* 所属私有组主键
	* @return
	*/
	public Integer getGrpId() {
		return grpId;
	}

	/** 
	* 所属私有组主键
	* @param grpId
	*/
	public void setGrpId(Integer grpId) {
		this.grpId = grpId;
	}

	public void setAutoEject(String autoEject) {
		this.autoEject = autoEject;
	}

	public String getAutoEject() {
		return autoEject;
	}

	/** 
	* 直属下属成员数
	* @return
	*/
	public Integer getCountSub() {
		return countSub;
	}

	/** 
	* 直属下属成员数
	* @param countSub
	*/
	public void setCountSub(Integer countSub) {
		this.countSub = countSub;
	}

	/** 
	* 把用户集合封装到user对象里面
	* @return
	*/
	public List<UserInfo> getListUser() {
		return listUser;
	}

	/** 
	* 把用户集合封装到user对象里面
	* @param listUser
	*/
	public void setListUser(List<UserInfo> listUser) {
		this.listUser = listUser;
	}

	/** 
	* 把用户分组封装到用户对象里面
	* @return
	*/
	public List<SelfGroup> getListGroup() {
		return listGroup;
	}

	/** 
	* 把用户分组封装到用户对象里面
	* @param listGroup
	*/
	public void setListGroup(List<SelfGroup> listGroup) {
		this.listGroup = listGroup;
	}

	/** 
	* 把团队集合封装到用户对象里面
	* @return
	*/
	public List<Department> getListDep() {
		return listDep;
	}

	/** 
	* 把团队集合封装到用户对象里面
	* @param listDep
	*/
	public void setListDep(List<Department> listDep) {
		this.listDep = listDep;
	}

	/** 
	* 总提醒数
	* @return
	*/
	public Integer getNoReadNums() {
		return noReadNums;
	}

	/** 
	* 总提醒数
	* @param noReadNums
	*/
	public void setNoReadNums(Integer noReadNums) {
		this.noReadNums = noReadNums;
	}

	/** 
	* 总关注未读消息数
	* @return
	*/
	public Integer getAttenNums() {
		return attenNums;
	}

	/** 
	* 总关注未读消息数
	* @param attenNums
	*/
	public void setAttenNums(Integer attenNums) {
		this.attenNums = attenNums;
	}

	/** 
	* 个人所有的待办任务
	* @return
	*/
	public Integer getTaskToDoNum() {
		return taskToDoNum;
	}

	/** 
	* 个人所有的待办任务
	* @param taskToDoNum
	*/
	public void setTaskToDoNum(Integer taskToDoNum) {
		this.taskToDoNum = taskToDoNum;
	}

	/** 
	* 我的客户
	* @return
	*/
	public Integer getMyCrmNum() {
		return myCrmNum;
	}

	/** 
	* 我的客户
	* @param myCrmNum
	*/
	public void setMyCrmNum(Integer myCrmNum) {
		this.myCrmNum = myCrmNum;
	}

	/** 
	* 我的项目
	* @return
	*/
	public Integer getMyItemNum() {
		return myItemNum;
	}

	/** 
	* 我的项目
	* @param myItemNum
	*/
	public void setMyItemNum(Integer myItemNum) {
		this.myItemNum = myItemNum;
	}

	/** 
	* 我的汇报
	* @return
	*/
	public Integer getMyWeeklyRepNum() {
		return myWeeklyRepNum;
	}

	/** 
	* 我的汇报
	* @param myWeeklyRepNum
	*/
	public void setMyWeeklyRepNum(Integer myWeeklyRepNum) {
		this.myWeeklyRepNum = myWeeklyRepNum;
	}

	/** 
	* 总待办事项数
	* @return
	*/
	public Integer getToDoNums() {
		return toDoNums;
	}

	/** 
	* 总待办事项数
	* @param toDoNums
	*/
	public void setToDoNums(Integer toDoNums) {
		this.toDoNums = toDoNums;
	}

	public String getAlterInfo() {
		return alterInfo;
	}

	public void setAlterInfo(String alterInfo) {
		this.alterInfo = alterInfo;
	}

	/** 
	* 个人直属上级人员集合
	* @return
	*/
	public List<UserInfo> getListUserInfo() {
		return listUserInfo;
	}

	/** 
	* 个人直属上级人员集合
	* @param listUserInfo
	*/
	public void setListUserInfo(List<UserInfo> listUserInfo) {
		this.listUserInfo = listUserInfo;
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

	/** 
	* 个性签名
	* @param selfIntr
	*/
	public void setSelfIntr(String selfIntr) {
		this.selfIntr = selfIntr;
	}

	/** 
	* 个性签名
	* @return
	*/
	public String getSelfIntr() {
		return selfIntr;
	}

	/** 
	* 入职时间
	* @return
	*/
	public String getHireDate() {
		return hireDate;
	}

	/** 
	* 入职时间
	* @param hireDate
	*/
	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	/** 
	* 上次登录时间
	* @return
	*/
	public String getLastLoginTime() {
		return lastLoginTime;
	}

	/** 
	* 上次登录时间
	* @param lastLoginTime
	*/
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/** 
	* 登录次数
	* @return
	*/
	public Integer getLoginTimes() {
		return loginTimes;
	}

	/** 
	* 登录次数
	* @param loginTimes
	*/
	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	/**
	* 是否为首席默认不是
	* @return
	*/
	public String getIsChief() {
		return isChief;
	}

	/** 
	* 是否为首席默认不是
	* @param isChief
	*/
	public void setIsChief(String isChief) {
		this.isChief = isChief;
	}

	/** 
	* 用户加入的所有团队个数
	* @return
	*/
	public Integer getUserInOrgNums() {
		return userInOrgNums;
	}

	/** 
	* 用户加入的所有团队个数
	* @param userInOrgNums
	*/
	public void setUserInOrgNums(Integer userInOrgNums) {
		this.userInOrgNums = userInOrgNums;
	}

	/**
	 * 是否过期
	 * @return
	 */
	public Integer getInService() {
		return inService;
	}

	/**
	 * 是否过期
	 * @param inService
	 */
	public void setInService(Integer inService) {
		this.inService = inService;
	}

	/** 
	* 个人已完成任务
	* @return
	*/
	public Integer getTaskDoneNum() {
		return taskDoneNum;
	}

	/** 
	* 个人已完成任务
	* @param taskDoneNum
	*/
	public void setTaskDoneNum(Integer taskDoneNum) {
		this.taskDoneNum = taskDoneNum;
	}

	public String getEnrollNumber() {
		return enrollNumber;
	}

	public void setEnrollNumber(String enrollNumber) {
		this.enrollNumber = enrollNumber;
	}

	/** 
	* 请假集合
	* @return
	*/
	public List<Leave> getListLive() {
		return listLive;
	}

	/** 
	* 请假集合
	* @param listLive
	*/
	public void setListLive(List<Leave> listLive) {
		this.listLive = listLive;
	}

	/** 
	* 加班
	* @return
	*/
	public OverTime getOverTime() {
		return overTime;
	}

	/** 
	* 加班
	* @param overTime
	*/
	public void setOverTime(OverTime overTime) {
		this.overTime = overTime;
	}

	/** 
	* 请假
	* @return
	*/
	public Leave getLeave() {
		return leave;
	}

	/** 
	* 请假
	* @param leave
	*/
	public void setLeave(Leave leave) {
		this.leave = leave;
	}

	/** 
	* 请假次数
	* @return
	*/
	public Integer getLeaveTimeT() {
		return leaveTimeT;
	}

	/** 
	* 请假次数
	* @param leaveTimeT
	*/
	public void setLeaveTimeT(Integer leaveTimeT) {
		this.leaveTimeT = leaveTimeT;
	}

	/** 
	* 请假总时长
	* @return
	*/
	public String getLeaveTotal() {
		return leaveTotal;
	}

	/** 
	* 请假总时长
	* @param leaveTotal
	*/
	public void setLeaveTotal(String leaveTotal) {
		this.leaveTotal = leaveTotal;
	}

	/** 
	* 加班次数
	* @return
	*/
	public Integer getOverTimeT() {
		return overTimeT;
	}

	/** 
	* 加班次数
	* @param overTimeT
	*/
	public void setOverTimeT(Integer overTimeT) {
		this.overTimeT = overTimeT;
	}

	/** 
	* 加班总时长
	* @return
	*/
	public String getOverTimeTotal() {
		return overTimeTotal;
	}

	/** 
	* 加班总时长
	* @param overTimeTotal
	*/
	public void setOverTimeTotal(String overTimeTotal) {
		this.overTimeTotal = overTimeTotal;
	}

	/** 
	* 考勤记录
	* @return
	*/
	public AttenceRecord getAttenceRecord() {
		return attenceRecord;
	}

	/** 
	* 考勤记录
	* @param attenceRecord
	*/
	public void setAttenceRecord(AttenceRecord attenceRecord) {
		this.attenceRecord = attenceRecord;
	}

	/** 
	* 迟到次数
	* @return
	*/
	public Integer getLateTime() {
		return lateTime;
	}

	/** 
	* 迟到次数
	* @param lateTime
	*/
	public void setLateTime(Integer lateTime) {
		this.lateTime = lateTime;
	}

	/** 
	* 早退次数
	* @return
	*/
	public Integer getLeaveEarlyTime() {
		return leaveEarlyTime;
	}

	/** 
	* 早退次数
	* @param leaveEarlyTime
	*/
	public void setLeaveEarlyTime(Integer leaveEarlyTime) {
		this.leaveEarlyTime = leaveEarlyTime;
	}

	/** 
	* 异常次数
	* @return
	*/
	public Integer getUnusualTime() {
		return unusualTime;
	}

	/** 
	* 异常次数
	* @param unusualTime
	*/
	public void setUnusualTime(Integer unusualTime) {
		this.unusualTime = unusualTime;
	}

	/** 
	* 旷工次数
	* @return
	*/
	public Integer getAbsentTime() {
		return absentTime;
	}

	/** 
	* 旷工次数
	* @param absentTime
	*/
	public void setAbsentTime(Integer absentTime) {
		this.absentTime = absentTime;
	}

	/** 
	* 是否查看
	* @return
	*/
	public String getIsView() {
		return isView;
	}

	/** 
	* 是否查看
	* @param isView
	*/
	public void setIsView(String isView) {
		this.isView = isView;
	}

	/** 
	* 查看时间
	* @return
	*/
	public String getViewTime() {
		return viewTime;
	}

	/** 
	* 查看时间
	* @param viewTime
	*/
	public void setViewTime(String viewTime) {
		this.viewTime = viewTime;
	}

	/** 
	* 个人默认显示组主键
	* @return
	*/
	public Integer getDefShowGrpId() {
		return defShowGrpId;
	}

	/** 
	* 个人默认显示组主键
	* @param defShowGrpId
	*/
	public void setDefShowGrpId(Integer defShowGrpId) {
		this.defShowGrpId = defShowGrpId;
	}

	public Integer getForMeDoUserId() {
		return forMeDoUserId;
	}

	public void setForMeDoUserId(Integer forMeDoUserId) {
		this.forMeDoUserId = forMeDoUserId;
	}

	public String getForMeDoUserName() {
		return forMeDoUserName;
	}

	public void setForMeDoUserName(String forMeDoUserName) {
		this.forMeDoUserName = forMeDoUserName;
	}

	public String getOnlySubState() {
		return onlySubState;
	}

	public void setOnlySubState(String onlySubState) {
		this.onlySubState = onlySubState;
	}

	public String getOptIP() {
		return optIP;
	}

	public void setOptIP(String optIP) {
		this.optIP = optIP;
	}

	/**
	 * 所属角色列表，用逗号连接的字符串
	 * @return
	 */
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserInfo{" + "id=" + id + ", recordCreateTime='" + recordCreateTime + '\'' + ", userName='" + userName + '\'' + ", allSpelling='" + allSpelling + '\'' + ", firstSpelling='" + firstSpelling + '\'' + ", password='" + password + '\'' + ", linePhone='" + linePhone + '\'' + ", movePhone='" + movePhone + '\'' + ", email='" + email + '\'' + ", wechat='" + wechat + '\'' + ", gender='" + gender + '\'' + ", birthday='" + birthday + '\'' + ", nickname='" + nickname + '\'' + ", qq='" + qq + '\'' + ", selfIntr='" + selfIntr + '\'' + ", enrollNumber='" + enrollNumber + '\'' + ", comId=" + comId + ", enabled='" + enabled + '\'' + ", remark='" + remark + '\'' + ", lastOnlineTime='" + lastOnlineTime + '\'' + ", admin='" + admin + '\'' + ", job='" + job + '\'' + ", depId=" + depId + ", checkState='" + checkState + '\'' + ", bigHeadPortrait='" + bigHeadPortrait + '\'' + ", mediumHeadPortrait='" + mediumHeadPortrait + '\'' + ", smallHeadPortrait='" + smallHeadPortrait + '\'' + ", jifenScore=" + jifenScore + ", optIP='" + optIP + '\'' + ", ifOnline='" + ifOnline + '\'' + ", genderName='" + genderName + '\'' + ", bigImgUuid='" + bigImgUuid + '\'' + ", bigImgName='" + bigImgName + '\'' + ", midImgUuid='" + midImgUuid + '\'' + ", midImgName='" + midImgName + '\'' + ", smImgUuid='" + smImgUuid + '\'' + ", smImgName='" + smImgName + '\'' + ", logoUuid='" + logoUuid + '\'' + ", logoName='" + logoName + '\'' + ", depName='" + depName + '\'' + ", condition='" + condition + '\'' + ", orgName='" + orgName + '\'' + ", listOrg=" + listOrg + ", anyDepId=" + anyDepId + ", onlySubState='" + onlySubState + '\'' + ", invUsers=" + Arrays.toString(invUsers) + ", anyNameLike='" + anyNameLike + '\'' + ", levName='" + levName + '\'' + ", nextLevJifen=" + nextLevJifen + ", minLevJifen=" + minLevJifen + ", userOrganicId=" + userOrganicId + ", jiFenPercent='" + jiFenPercent + '\'' + ", totalJifen=" + totalJifen + ", totalRank=" + totalRank + ", monthJifen=" + monthJifen + ", monthRank=" + monthRank + ", weekJifen=" + weekJifen + ", weekRank=" + weekRank + ", grpId=" + grpId + ", countSub=" + countSub + ", autoEject='" + autoEject + '\'' + ", listUser=" + listUser + ", listGroup=" + listGroup + ", listDep=" + listDep + ", noReadNums=" + noReadNums + ", toDoNums=" + toDoNums + ", attenNums=" + attenNums + ", taskToDoNum=" + taskToDoNum + ", taskDoneNum=" + taskDoneNum + ", myCrmNum=" + myCrmNum + ", myItemNum=" + myItemNum + ", myWeeklyRepNum=" + myWeeklyRepNum + ", alterInfo='" + alterInfo + '\'' + ", listUserInfo=" + listUserInfo + ", succ=" + succ + ", msg='" + msg + '\'' + ", hireDate='" + hireDate + '\'' + ", lastLoginTime='" + lastLoginTime + '\'' + ", loginTimes=" + loginTimes + ", isChief='" + isChief + '\'' + ", userInOrgNums=" + userInOrgNums + ", inService=" + inService + ", listLive=" + listLive + ", leave=" + leave + ", overTime=" + overTime + ", leaveTimeT=" + leaveTimeT + ", leaveTotal='" + leaveTotal + '\'' + ", overTimeT=" + overTimeT + ", overTimeTotal='" + overTimeTotal + '\'' + ", attenceRecord=" + attenceRecord + ", lateTime=" + lateTime + ", leaveEarlyTime=" + leaveEarlyTime + ", unusualTime=" + unusualTime + ", absentTime=" + absentTime + ", isView='" + isView + '\'' + ", viewTime='" + viewTime + '\'' + ", defShowGrpId=" + defShowGrpId + ", forMeDoUserId=" + forMeDoUserId + ", forMeDoUserName='" + forMeDoUserName + '\'' + '}';
	}
}
