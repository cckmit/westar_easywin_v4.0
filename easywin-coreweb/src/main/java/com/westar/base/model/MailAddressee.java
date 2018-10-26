package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 邮件收件人
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MailAddressee {
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
	* 创建人员
	*/
	@Filed
	private Integer userId;
	/** 
	* 邮件id
	*/
	@Filed
	private Integer mailId;
	/** 
	* 收件人类型
	*/
	@Filed
	private String type;
	/** 
	* 收件人邮箱
	*/
	@Filed
	private String address;
	/** 
	* 收件人昵称
	*/
	@Filed
	private String personal;

	/****************以上主要为系统表字段********************/

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
	* 创建人员
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 创建人员
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 邮件id
	* @param mailId
	*/
	public void setMailId(Integer mailId) {
		this.mailId = mailId;
	}

	/** 
	* 邮件id
	* @return
	*/
	public Integer getMailId() {
		return mailId;
	}

	/** 
	* 收件人类型
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 收件人类型
	* @return
	*/
	public String getType() {
		return type;
	}

	/** 
	* 收件人邮箱
	* @param address
	*/
	public void setAddress(String address) {
		this.address = address;
	}

	/** 
	* 收件人邮箱
	* @return
	*/
	public String getAddress() {
		return address;
	}

	/** 
	* 收件人昵称
	* @param personal
	*/
	public void setPersonal(String personal) {
		this.personal = personal;
	}

	/** 
	* 收件人昵称
	* @return
	*/
	public String getPersonal() {
		return personal;
	}
}
