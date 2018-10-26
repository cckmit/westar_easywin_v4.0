package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 加入的激活记录
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JoinRecord {
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
	* 使用账号
	*/
	@Filed
	private String account;
	/** 
	* 加入方式  1申请 2 邀请
	*/
	@Filed
	private String joinType;
	/** 
	* 邮箱是否激活了，0待激活 1激活 2已确认
	*/
	@Filed
	private String state;
	/** 
	* uuid，用于邮箱激活的确认码
	*/
	@Filed
	private String confirmId;
	/** 
	* 审核状态 0 未审核 1 审核通过，2，审核未通过 系统管理员默认通过,邀请的默认通过,加入的默认未通过
	*/
	@Filed
	private String checkState;
	/** 
	* 申请的留言
	*/
	@Filed
	private String joinNote;
	/** 
	* 邀请人员
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	public JoinRecord() {
	}

	/** 
	* * 	 * @param comId	 *            企业编号	 * @param email	 *            使用邮箱	 * @param joinType	 *            加入方式 0注册 1申请 2 邀请	 * @param state	 *            邮箱是否激活了，0待激活 1激活 2已确认	 * @param confirmId	 *            邮箱激活的确认码	 
	*/
	public JoinRecord(Integer comId, String account, String joinType, String state, String confirmId) {
		super();
		this.comId = comId;
		this.account = account;
		this.joinType = joinType;
		this.state = state;
		this.confirmId = confirmId;
	}

	/** 
	* 企业名称
	*/
	private String comName;
	/** 
	* 拒绝的意见(只用于发送邮件)
	*/
	private String rejectReson;

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
	* 加入方式  1申请 2 邀请
	* @param joinType
	*/
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	/** 
	* 加入方式  1申请 2 邀请
	* @return
	*/
	public String getJoinType() {
		return joinType;
	}

	/** 
	* 邮箱是否激活了，0待激活 1激活 2已确认
	* @param state
	*/
	public void setState(String state) {
		this.state = state;
	}

	/** 
	* 邮箱是否激活了，0待激活 1激活 2已确认
	* @return
	*/
	public String getState() {
		return state;
	}

	/** 
	* uuid，用于邮箱激活的确认码
	* @param confirmId
	*/
	public void setConfirmId(String confirmId) {
		this.confirmId = confirmId;
	}

	/** 
	* uuid，用于邮箱激活的确认码
	* @return
	*/
	public String getConfirmId() {
		return confirmId;
	}

	/** 
	* 企业名称
	* @return
	*/
	public String getComName() {
		return comName;
	}

	/** 
	* 企业名称
	* @param comName
	*/
	public void setComName(String comName) {
		this.comName = comName;
	}

	/** 
	* 审核状态 0 未审核 1 审核通过，2，审核未通过 系统管理员默认通过,邀请的默认通过,加入的默认未通过
	* @param checkState
	*/
	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	/** 
	* 审核状态 0 未审核 1 审核通过，2，审核未通过 系统管理员默认通过,邀请的默认通过,加入的默认未通过
	* @return
	*/
	public String getCheckState() {
		return checkState;
	}

	/** 
	* 申请的留言
	* @param joinNote
	*/
	public void setJoinNote(String joinNote) {
		this.joinNote = joinNote;
	}

	/** 
	* 申请的留言
	* @return
	*/
	public String getJoinNote() {
		return joinNote;
	}

	/** 
	* 拒绝的意见(只用于发送邮件)
	* @return
	*/
	public String getRejectReson() {
		return rejectReson;
	}

	/** 
	* 拒绝的意见(只用于发送邮件)
	* @param rejectReson
	*/
	public void setRejectReson(String rejectReson) {
		this.rejectReson = rejectReson;
	}

	/** 
	* 邀请人员
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 邀请人员
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 使用账号
	* @param account
	*/
	public void setAccount(String account) {
		this.account = account;
	}

	/** 
	* 使用账号
	* @return
	*/
	public String getAccount() {
		return account;
	}
}
