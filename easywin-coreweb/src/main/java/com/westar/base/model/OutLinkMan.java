package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 外部联系人
 */
@Table
@JsonInclude(Include.NON_NULL)
public class OutLinkMan {
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
	/** 
	* 公开或私有 0私有 （指定范围）1公开
	*/
	@Filed
	private Integer pubState;
	/** 
	* 人员主键
	*/
	@Filed
	private Integer creator;
	/** 
	* 偏好
	*/
	@Filed
	private String hobby;
	/** 
	* 备注
	*/
	@Filed
	private String remarks;
	/** 
	* 0女 1男
	*/
	@Filed
	private String gender;

	/****************以上主要为系统表字段********************/
	/** 
	* 开始时间
	*/
	private String startDate;
	/** 
	* 结束时间
	*/
	private String endDate;
	/** 
	* 私有人员范围集合
	*/
	private List<OutLinkManRange> listRangeUser;
	/** 
	* 客户id
	*/
	private Integer customerId;
	/** 
	* 判断是否有关联 0无 1有
	*/
	private Integer used;
	/** 
	* 联系方式集合
	*/
	private List<OlmContactWay> listContactWay;
	/** 
	* 联系地址集合
	*/
	private List<OlmAddress> listAddress;
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
	* 私有人员范围集合
	* @return
	*/
	public List<OutLinkManRange> getListRangeUser() {
		return listRangeUser;
	}

	/** 
	* 私有人员范围集合
	* @param listRangeUser
	*/
	public void setListRangeUser(List<OutLinkManRange> listRangeUser) {
		this.listRangeUser = listRangeUser;
	}

	/** 
	* 开始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 开始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 结束时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 结束时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	* 公开或私有 0私有 （指定范围）1公开
	* @param pubState
	*/
	public void setPubState(Integer pubState) {
		this.pubState = pubState;
	}

	/** 
	* 公开或私有 0私有 （指定范围）1公开
	* @return
	*/
	public Integer getPubState() {
		return pubState;
	}

	/** 
	* 人员主键
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 客户id
	* @return
	*/
	public Integer getCustomerId() {
		return customerId;
	}

	/** 
	* 客户id
	* @param customerId
	*/
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/** 
	* 偏好
	* @param hobby
	*/
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	/** 
	* 偏好
	* @return
	*/
	public String getHobby() {
		return hobby;
	}

	/** 
	* 备注
	* @param remarks
	*/
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/** 
	* 备注
	* @return
	*/
	public String getRemarks() {
		return remarks;
	}

	/** 
	* 0女 1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 0女 1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 联系方式集合
	* @return
	*/
	public List<OlmContactWay> getListContactWay() {
		return listContactWay;
	}

	/** 
	* 联系方式集合
	* @param listContactWay
	*/
	public void setListContactWay(List<OlmContactWay> listContactWay) {
		this.listContactWay = listContactWay;
	}

	/** 
	* 联系地址集合
	* @return
	*/
	public List<OlmAddress> getListAddress() {
		return listAddress;
	}

	/** 
	* 联系地址集合
	* @param listAddress
	*/
	public void setListAddress(List<OlmAddress> listAddress) {
		this.listAddress = listAddress;
	}

	/** 
	* 判断是否有关联 0无 1有
	* @return
	*/
	public Integer getUsed() {
		return used;
	}

	/** 
	* 判断是否有关联 0无 1有
	* @param used
	*/
	public void setUsed(Integer used) {
		this.used = used;
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
}
