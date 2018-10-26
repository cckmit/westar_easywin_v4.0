package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 客户联系人
 */
@Table
@JsonInclude(Include.NON_NULL)
public class LinkMan {
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
	* 客户主键
	*/
	@Filed
	private Integer customerId;
	/** 
	* 联系人姓名
	*/
	@Filed
	private String linkManName;
	/** 
	* 职务
	*/
	@Filed
	private String post;
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
	* qq
	*/
	@Filed
	private String qq;
	/** 
	* 座机
	*/
	@Filed
	private String linePhone;

	/****************以上主要为系统表字段********************/
	private String ownerName;
	/** 
	* 附件名称
	*/
	private String fileName;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 创建人主键
	*/
	private Integer creatorId;
	/** 
	* 删除成功与否标识符
	*/
	private boolean delSucc;
	/** 
	* 返回提示信息
	*/
	private String promptMsg;
	/** 
	* 外部联系人集合
	*/
	private List<OlmContactWay> contactWays;
	/** 
	* 外部联系人Id
	*/
	private Integer outLinkManId;
	/** 
	* 联系方式详情
	*/
	private String contactWay;
	/** 
	* 联系方式code值
	*/
	private String contactWayValue;

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
	* 客户主键
	* @param customerId
	*/
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/** 
	* 客户主键
	* @return
	*/
	public Integer getCustomerId() {
		return customerId;
	}

	/** 
	* 联系人姓名
	* @param linkManName
	*/
	public void setLinkManName(String linkManName) {
		this.linkManName = linkManName;
	}

	/** 
	* 联系人姓名
	* @return
	*/
	public String getLinkManName() {
		return linkManName;
	}

	/** 
	* 职务
	* @param post
	*/
	public void setPost(String post) {
		this.post = post;
	}

	/** 
	* 职务
	* @return
	*/
	public String getPost() {
		return post;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 附件名称
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	* 附件UUID
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 附件UUID
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public boolean isDelSucc() {
		return delSucc;
	}

	/** 
	* 删除成功与否标识符
	* @param delSucc
	*/
	public void setDelSucc(boolean delSucc) {
		this.delSucc = delSucc;
	}

	/** 
	* 返回提示信息
	* @return
	*/
	public String getPromptMsg() {
		return promptMsg;
	}

	/** 
	* 返回提示信息
	* @param promptMsg
	*/
	public void setPromptMsg(String promptMsg) {
		this.promptMsg = promptMsg;
	}

	/** 
	* 座机
	* @param linePhone
	*/
	public void setLinePhone(String linePhone) {
		this.linePhone = linePhone;
	}

	/** 
	* 座机
	* @return
	*/
	public String getLinePhone() {
		return linePhone;
	}

	/** 
	* 外部联系人Id
	* @return
	*/
	public Integer getOutLinkManId() {
		return outLinkManId;
	}

	/** 
	* 外部联系人Id
	* @param outLinkManId
	*/
	public void setOutLinkManId(Integer outLinkManId) {
		this.outLinkManId = outLinkManId;
	}

	/** 
	* 创建人主键
	* @return
	*/
	public Integer getCreatorId() {
		return creatorId;
	}

	/** 
	* 创建人主键
	* @param creatorId
	*/
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	/** 
	* 联系方式详情
	* @return
	*/
	public String getContactWay() {
		return contactWay;
	}

	/** 
	* 联系方式详情
	* @param contactWay
	*/
	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	/** 
	* 联系方式code值
	* @return
	*/
	public String getContactWayValue() {
		return contactWayValue;
	}

	/** 
	* 联系方式code值
	* @param contactWayValue
	*/
	public void setContactWayValue(String contactWayValue) {
		this.contactWayValue = contactWayValue;
	}

	/** 
	* 外部联系人集合
	* @return
	*/
	public List<OlmContactWay> getContactWays() {
		return contactWays;
	}

	/** 
	* 外部联系人集合
	* @param contactWays
	*/
	public void setContactWays(List<OlmContactWay> contactWays) {
		this.contactWays = contactWays;
	}
}
