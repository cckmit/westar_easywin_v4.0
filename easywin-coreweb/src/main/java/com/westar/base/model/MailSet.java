package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 邮件设置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MailSet {
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
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 所用的邮箱
	*/
	@Filed
	private String email;
	/** 
	* 发送服务器smtp
	*/
	@Filed
	private String serverHost;
	/** 
	* 发送服务器端口smtp
	*/
	@Filed
	private Integer serverPort;
	/** 
	* 账户
	*/
	@Filed
	private String account;
	/** 
	* 密码
	*/
	@Filed
	private String passwd;
	/** 
	* 身份验证
	*/
	@Filed
	private String isValidate;
	/** 
	* SSL身份验证
	*/
	@Filed
	private String smtpSSL;
	/** 
	* 服务器pop
	*/
	@Filed
	private String serverPopHost;
	/** 
	* 服务器端口pop
	*/
	@Filed
	private Integer serverPopPort;
	/** 
	* 服务器Imap
	*/
	@Filed
	private String serverImapHost;
	/** 
	* 服务器端口Imap
	*/
	@Filed
	private Integer serverImapPort;

	/****************以上主要为系统表字段********************/
	/** 
	* 邮件主题
	*/
	public String subject;
	/** 
	* 邮件内容
	*/
	public String body;
	/** 
	* 邮件接收对象
	*/
	private List<String> mailTos;
	/** 
	* 上传文件
	*/
	private List<Upfiles> upfiles;
	/** 
	* 获取邮箱夹类型
	*/
	private String folder;
	/** 
	* 发送时间
	*/
	private String sendTime;

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
	* 所用的邮箱
	* @param email
	*/
	public void setEmail(String email) {
		this.email = email;
	}

	/** 
	* 所用的邮箱
	* @return
	*/
	public String getEmail() {
		return email;
	}

	/** 
	* 发送服务器smtp
	* @param serverHost
	*/
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	/** 
	* 发送服务器smtp
	* @return
	*/
	public String getServerHost() {
		return serverHost;
	}

	/** 
	* 发送服务器端口smtp
	* @param serverPort
	*/
	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	/** 
	* 发送服务器端口smtp
	* @return
	*/
	public Integer getServerPort() {
		return serverPort;
	}

	/** 
	* 账户
	* @param account
	*/
	public void setAccount(String account) {
		this.account = account;
	}

	/** 
	* 账户
	* @return
	*/
	public String getAccount() {
		return account;
	}

	/** 
	* 密码
	* @param passwd
	*/
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/** 
	* 密码
	* @return
	*/
	public String getPasswd() {
		return passwd;
	}

	/** 
	* 身份验证
	* @param isValidate
	*/
	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
	}

	/** 
	* 身份验证
	* @return
	*/
	public String getIsValidate() {
		return isValidate;
	}

	/** 
	* SSL身份验证
	* @param smtpSSL
	*/
	public void setSmtpSSL(String smtpSSL) {
		this.smtpSSL = smtpSSL;
	}

	/** 
	* SSL身份验证
	* @return
	*/
	public String getSmtpSSL() {
		return smtpSSL;
	}

	/** 
	* 邮件主题
	* @return
	*/
	public String getSubject() {
		return subject;
	}

	/** 
	* 邮件主题
	* @param subject
	*/
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/** 
	* 邮件内容
	* @return
	*/
	public String getBody() {
		return body;
	}

	/** 
	* 邮件内容
	* @param body
	*/
	public void setBody(String body) {
		this.body = body;
	}

	/** 
	* 邮件接收对象
	* @return
	*/
	public List<String> getMailTos() {
		return mailTos;
	}

	/** 
	* 邮件接收对象
	* @param mailTos
	*/
	public void setMailTos(List<String> mailTos) {
		this.mailTos = mailTos;
	}

	/** 
	* 服务器pop
	* @return
	*/
	public String getServerPopHost() {
		return serverPopHost;
	}

	/** 
	* 服务器pop
	* @param serverPopHost
	*/
	public void setServerPopHost(String serverPopHost) {
		this.serverPopHost = serverPopHost;
	}

	/** 
	* 服务器端口pop
	* @return
	*/
	public Integer getServerPopPort() {
		return serverPopPort;
	}

	/** 
	* 服务器端口pop
	* @param serverPopPort
	*/
	public void setServerPopPort(Integer serverPopPort) {
		this.serverPopPort = serverPopPort;
	}

	/** 
	* 上传文件
	* @return
	*/
	public List<Upfiles> getUpfiles() {
		return upfiles;
	}

	/** 
	* 上传文件
	* @param upfiles
	*/
	public void setUpfiles(List<Upfiles> upfiles) {
		this.upfiles = upfiles;
	}

	/** 
	* 服务器Imap
	* @return
	*/
	public String getServerImapHost() {
		return serverImapHost;
	}

	/** 
	* 服务器Imap
	* @param serverImapHost
	*/
	public void setServerImapHost(String serverImapHost) {
		this.serverImapHost = serverImapHost;
	}

	/** 
	* 服务器端口Imap
	* @return
	*/
	public Integer getServerImapPort() {
		return serverImapPort;
	}

	/** 
	* 服务器端口Imap
	* @param serverImapPort
	*/
	public void setServerImapPort(Integer serverImapPort) {
		this.serverImapPort = serverImapPort;
	}

	/** 
	* 获取邮箱夹类型
	* @return
	*/
	public String getFolder() {
		return folder;
	}

	/** 
	* 获取邮箱夹类型
	* @param folder
	*/
	public void setFolder(String folder) {
		this.folder = folder;
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
}
