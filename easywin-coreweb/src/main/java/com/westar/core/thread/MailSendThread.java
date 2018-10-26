package com.westar.core.thread;

import com.westar.base.util.MessageSender;

/**
 * 另起线程，发送邮件，不影响主线程的运行时间
 * @author H87
 *
 */
public class MailSendThread implements Runnable {

	private MessageSender sender;
	
	private String userEmail;
	
	private String subject;
	
	private String body;

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		try {
			sender.sendEmail(userEmail, subject, body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param sender 发送器
	 * @param userEmail 发送对象
	 * @param subject 主题
	 * @param body 内容
	 */
	public MailSendThread(MessageSender sender, String userEmail,
			String subject, String body) {
		super();
		this.sender = sender;
		this.userEmail = userEmail;
		this.subject = subject;
		this.body = body;
	}

	public MessageSender getSender() {
		return sender;
	}

	public void setSender(MessageSender sender) {
		this.sender = sender;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
