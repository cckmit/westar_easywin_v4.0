package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 邮件表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Mail {
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
	* 账号
	*/
	@Filed
	private Integer accountId;
	/** 
	* 主题
	*/
	@Filed
	private String subject;
	/** 
	* 内容
	*/
	@Filed
	private String body;
	/** 
	* 邮箱类型
	*/
	@Filed
	private String folder;
	/** 
	* 发送时间
	*/
	@Filed
	private String sendTime;
	/** 
	* 发件人昵称
	*/
	@Filed
	private String personal;
	/** 
	* 发件人邮箱
	*/
	@Filed
	private String fromAddress;

	/****************以上主要为系统表字段********************/
	/** 
	* 账号
	*/
	private String account;
	/** 
	* 上传文件
	*/
	private List<MailUpfile> upfiles;
	/** 
	* 阅读情况
	*/
	private Boolean readState;
	/** 
	* 邮件详情附件信息
	*/
	private List<String> files;
	/** 
	* 接收人id
	*/
	private Integer recipientId;
	/** 
	* 抄送账号
	*/
	private List<String> copyAccounts;
	/** 
	* 收件人
	*/
	private String recipients;
	/** 
	* 收件人集合
	*/
	private List<MailAddressee> listMailAddressee;
	/** 
	* 收件人
	*/
	private List<MailAddressee> listAddresseeTo;
	/** 
	* 抄送人
	*/
	private List<MailAddressee> listAddresseeCc;
	/** 
	* 密送人
	*/
	private List<MailAddressee> listAddresseeBcc;
	/** 
	* 同步类型 1同步所有
	*/
	private String pullType;

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
	* 账号
	* @param accountId
	*/
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	/** 
	* 账号
	* @return
	*/
	public Integer getAccountId() {
		return accountId;
	}

	/** 
	* 主题
	* @param subject
	*/
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/** 
	* 主题
	* @return
	*/
	public String getSubject() {
		return subject;
	}

	/** 
	* 内容
	* @param body
	*/
	public void setBody(String body) {
		this.body = body;
	}

	/** 
	* 内容
	* @return
	*/
	public String getBody() {
		return body;
	}

	/** 
	* 收件人
	* @param recipients
	*/
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	/** 
	* 收件人
	* @return
	*/
	public String getRecipients() {
		return recipients;
	}

	/** 
	* 账号
	* @return
	*/
	public String getAccount() {
		return account;
	}

	/** 
	* 账号
	* @param account
	*/
	public void setAccount(String account) {
		this.account = account;
	}

	/** 
	* 上传文件
	* @return
	*/
	public List<MailUpfile> getUpfiles() {
		return upfiles;
	}

	/** 
	* 上传文件
	* @param upfiles
	*/
	public void setUpfiles(List<MailUpfile> upfiles) {
		this.upfiles = upfiles;
	}

	/** 
	* 阅读情况
	* @return
	*/
	public Boolean getReadState() {
		return readState;
	}

	/** 
	* 阅读情况
	* @param readState
	*/
	public void setReadState(Boolean readState) {
		this.readState = readState;
	}

	/** 
	* 邮件详情附件信息
	* @return
	*/
	public List<String> getFiles() {
		return files;
	}

	/** 
	* 邮件详情附件信息
	* @param files
	*/
	public void setFiles(List<String> files) {
		this.files = files;
	}

	/** 
	* 接收人id
	* @return
	*/
	public Integer getRecipientId() {
		return recipientId;
	}

	/** 
	* 接收人id
	* @param recipientId
	*/
	public void setRecipientId(Integer recipientId) {
		this.recipientId = recipientId;
	}

	/** 
	* 发件人昵称
	* @return
	*/
	public String getPersonal() {
		return personal;
	}

	/** 
	* 发件人昵称
	* @param personal
	*/
	public void setPersonal(String personal) {
		this.personal = personal;
	}

	/** 
	* 抄送账号
	* @return
	*/
	public List<String> getCopyAccounts() {
		return copyAccounts;
	}

	/** 
	* 抄送账号
	* @param copyAccounts
	*/
	public void setCopyAccounts(List<String> copyAccounts) {
		this.copyAccounts = copyAccounts;
	}

	/** 
	* 邮箱类型
	* @param folder
	*/
	public void setFolder(String folder) {
		this.folder = folder;
	}

	/** 
	* 邮箱类型
	* @return
	*/
	public String getFolder() {
		return folder;
	}

	/** 
	* 收件人集合
	* @return
	*/
	public List<MailAddressee> getListMailAddressee() {
		return listMailAddressee;
	}

	/** 
	* 收件人集合
	* @param listMailAddressee
	*/
	public void setListMailAddressee(List<MailAddressee> listMailAddressee) {
		this.listMailAddressee = listMailAddressee;
	}

	/** 
	* 发送时间
	* @return
	*/
	public String getSendTime() {
		return sendTime;
	}

	/** 
	* 发送时间
	* @param sendTime
	*/
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	/** 
	* 发件人邮箱
	* @return
	*/
	public String getFromAddress() {
		return fromAddress;
	}

	/** 
	* 发件人邮箱
	* @param fromAddress
	*/
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	/** 
	* 收件人
	* @return
	*/
	public List<MailAddressee> getListAddresseeTo() {
		return listAddresseeTo;
	}

	/** 
	* 收件人
	* @param listAddresseeTo
	*/
	public void setListAddresseeTo(List<MailAddressee> listAddresseeTo) {
		this.listAddresseeTo = listAddresseeTo;
	}

	/** 
	* 抄送人
	* @return
	*/
	public List<MailAddressee> getListAddresseeCc() {
		return listAddresseeCc;
	}

	/** 
	* 抄送人
	* @param listAddresseeCc
	*/
	public void setListAddresseeCc(List<MailAddressee> listAddresseeCc) {
		this.listAddresseeCc = listAddresseeCc;
	}

	/** 
	* 密送人
	* @return
	*/
	public List<MailAddressee> getListAddresseeBcc() {
		return listAddresseeBcc;
	}

	/** 
	* 密送人
	* @param listAddresseeBcc
	*/
	public void setListAddresseeBcc(List<MailAddressee> listAddresseeBcc) {
		this.listAddresseeBcc = listAddresseeBcc;
	}

	/** 
	* 同步类型 1同步所有
	* @return
	*/
	public String getPullType() {
		return pullType;
	}

	/** 
	* 同步类型 1同步所有
	* @param pullType
	*/
	public void setPullType(String pullType) {
		this.pullType = pullType;
	}
}
