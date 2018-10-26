package com.westar.core.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.westar.base.model.Mail;
import com.westar.base.model.MailAddressee;
import com.westar.base.model.MailSet;
import com.westar.base.model.MailUpfile;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.Encodes;
import com.westar.base.util.FileUtil;
import com.westar.base.util.ValiMailUsrPw;
import com.westar.core.dao.MailDao;

@Service
public class MailService {
	@Autowired
	MailDao mailDao;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	MailSetService mailSetService;
	
	/**
	 * 接收邮件
	 * @author hcj 
	 * @param mail
	 * @return 
	 * @date 2018年9月19日 上午9:24:11
	 */
	public List<Mail> listPagedReceiveMail(Mail mail) {
		List<Mail> lists = new ArrayList<>();
		if(!CommonUtil.isNull(mail) && !CommonUtil.isNull(mail.getAccountId())) {
			MailSet mailSet = (MailSet) mailDao.objectQuery(MailSet.class, mail.getAccountId());
			ValiMailUsrPw valiMailUsrPw = new ValiMailUsrPw();
			lists = valiMailUsrPw.getMails(mailSet);
			
		}
		return lists;
	}
	
	/**
	 * 分页查询邮件
	 * @author hcj 
	 * @param mail
	 * @param userInfo 
	 * @return 
	 * @date 2018年9月18日 下午1:17:20
	 */
	public List<Mail> listPagedMail(Mail mail, UserInfo userInfo) {
		List<Mail> lists = mailDao.listPagedMail(mail,userInfo);
		if(!CommonUtil.isNull(lists)) {
			for (Mail newMail : lists) {
				List<MailAddressee> listAddressee = this.listMailAddressee(newMail.getId());
				if(!CommonUtil.isNull(listAddressee)) {
					newMail.setListMailAddressee(listAddressee);
				}
			}
		}
		return lists;
	}
	
	/**
	 * 通过mailId获取收件人
	 * @author hcj 
	 * @param mailId
	 * @return 
	 * @date 2018年9月28日 上午10:17:02
	 */
	private List<MailAddressee> listMailAddressee(Integer mailId) {
		return mailDao.listMailAddressee(mailId);
	}

	/**
	 * 查看邮件详情
	 * @author hcj 
	 * @param mail
	 * @return 
	 * @date 2018年9月25日 上午9:18:49
	 */
	public Mail getMailDetail(Mail mail) {
		MailSet mailSet = (MailSet) mailDao.objectQuery(MailSet.class, mail.getAccountId());
		ValiMailUsrPw valiMailUsrPw = new ValiMailUsrPw();
		mail = valiMailUsrPw.getMailDetail(mailSet,mail.getId());
		return mail;
	}
	
	/**
	 * 添加新邮件
	 * @author hcj 
	 * @param mail
	 * @param userInfo 
	 * @throws MessagingException 
	 * @throws IOException 
	 * @date 2018年9月25日 下午5:58:12
	 */
	public void addMail(Mail mail, UserInfo userInfo) throws MessagingException, IOException {
		//发送邮件
		this.sendMail(mail);
		//获取服务器邮件同步到本地
		//mail.setFolder("已发送");
		//this.addPulldata(mail,userInfo);
	}
	
	/**
	 * 查询最大发送时间
	 * @author hcj 
	 * @param mail
	 * @return 
	 * @date 2018年9月27日 下午4:09:56
	 */
	private Mail queryMaxSenTime(Mail mail) {
		return mailDao.queryMaxSenTime(mail);
	}

	/**
	 * 获取服务器邮件同步到本地
	 * @author hcj 
	 * @param mail 
	 * @param userInfo 
	 * @throws IOException 
	 * @throws MessagingException 
	 * @date 2018年9月27日 下午1:47:53
	 */
	public void addPulldata(Mail mail, UserInfo userInfo) throws MessagingException, IOException {
		List<MailSet> listsMailSet = new ArrayList<>();
		MailSet mailSets = new MailSet();
		mailSets.setUserId(userInfo.getId());
		//是否同步的是某一个账号
		if(!CommonUtil.isNull(mail.getAccountId())){
			MailSet queryMailSet = (MailSet) mailDao.objectQuery(MailSet.class, mail.getAccountId());
			listsMailSet.add(queryMailSet); 
		}else{
			listsMailSet = mailSetService.listMailSet(mailSets);
		}
		if(!CommonUtil.isNull(listsMailSet)) {
			for (MailSet mailSet : listsMailSet) {
				mailSet.setFolder(mail.getFolder());
				//查询最大的时间记录
				mail.setAccountId(mailSet.getId());
				// 获取连接
				Properties props = new Properties();
				props.setProperty("mail.store.protocol", "imap"); // 使用的协议（JavaMail规范要求）
				if(!CommonUtil.isNull(mailSet.getSmtpSSL()) && "1".equals(mailSet.getSmtpSSL())) {
					props.put("mail.imap.ssl.enable", "true");
				}
				// 获取连接
				Session session = Session.getDefaultInstance(props);
				Message[] messages  = new Message[0];
				try {
					// 获取Store对象
					Store store = session.getStore();
					store.connect(mailSet.getServerImapHost(), mailSet.getAccount(), Encodes.decodeBase64(mailSet.getPasswd()));
					// 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
					Folder folder = store.getFolder(mailSet.getFolder());// 获得用户的邮件帐户
					folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限
					
					//判断是否是同步所有邮件
					if(!CommonUtil.isNull(mail.getPullType()) && "1".equals(mail.getPullType())){
						messages = folder.getMessages();// 得到邮箱帐户中的所有邮件
					}else{
						Mail maxTimeMail = this.queryMaxSenTime(mail);
						if(!CommonUtil.isNull(maxTimeMail)){
							Date date = DateTimeUtil.parseDate(maxTimeMail.getSendTime(), DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
							SearchTerm st = new SentDateTerm(6,date);//6 代表大于等于这时间之后           
							messages = folder.search(st);
						}else {
							messages = folder.getMessages();// 得到邮箱帐户中的所有邮件
						}
					}
					if(!CommonUtil.isNull(messages)) {
						for (int i = 0; i < messages.length; i++) {
							//处理邮件信息
							Mail newMail = new Mail();
							newMail.setSubject(messages[i].getSubject());
							if(!CommonUtil.isNull(messages[i].getSentDate())) {
								newMail.setSendTime(DateTimeUtil.formatDate(messages[i].getSentDate(), DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
							}
							if(!CommonUtil.isNull(messages[i].getFrom())) {
								InternetAddress address = (InternetAddress) messages[i].getFrom()[0];
								if("INBOX".equals(mailSet.getFolder())) {
									newMail.setFromAddress(address.getAddress());
									newMail.setPersonal(address.getPersonal());
								}else if("已发送".equals(mailSet.getFolder())){
									newMail.setFromAddress(mailSet.getAccount());
								}
							}
							newMail.setFolder(mailSet.getFolder());
							StringBuffer content = new StringBuffer();
							String contentType = messages[i].getContentType();
							ValiMailUsrPw.getMailTextContent(messages[i], content,contentType.startsWith("text/plain"));
							//当content不存在时换一种方式查询
							if(CommonUtil.isNull(content.toString())) {
								ValiMailUsrPw.getMailTextContent(messages[i], content);
							}
							newMail.setBody(content.toString());
							newMail.setUserId(mailSet.getUserId());
							newMail.setAccountId(mailSet.getId());
							newMail.setComId(userInfo.getComId());
							Integer count = this.querCountMail(newMail);
							if(count < 1) {
								Integer mailId = mailDao.add(newMail);
								//处理附件
								List<Upfiles> upfiles = ValiMailUsrPw.saveAttachment(messages[i],userInfo.getComId());
								if(!CommonUtil.isNull(upfiles)) {
									for (Upfiles file : upfiles) {
										Integer upfileId = mailDao.add(file);
										MailUpfile mailUpfile = new MailUpfile();
										mailUpfile.setComId(userInfo.getComId());
										mailUpfile.setMailId(mailId);
										mailUpfile.setUpfileId(upfileId);
										mailUpfile.setUserId(userInfo.getId());
										mailDao.add(mailUpfile);
									}
								}
								//处理收件人
								List<MailAddressee> listTO = ValiMailUsrPw.getReceiveAddress((MimeMessage)messages[i],Message.RecipientType.TO);
								List<MailAddressee> listCC = ValiMailUsrPw.getReceiveAddress((MimeMessage)messages[i],Message.RecipientType.CC);
								List<MailAddressee> listBCC = ValiMailUsrPw.getReceiveAddress((MimeMessage)messages[i],Message.RecipientType.BCC);
								if(!CommonUtil.isNull(listTO)) {
									for (MailAddressee mailAddressee : listTO) {
										mailAddressee.setComId(userInfo.getComId());
										mailAddressee.setMailId(mailId);
										mailAddressee.setUserId(userInfo.getId());
										mailAddressee.setType("TO");
										mailDao.add(mailAddressee);
									}
								}
								if(!CommonUtil.isNull(listCC)) {
									for (MailAddressee mailAddressee : listCC) {
										mailAddressee.setComId(userInfo.getComId());
										mailAddressee.setMailId(mailId);
										mailAddressee.setUserId(userInfo.getId());
										mailAddressee.setType("CC");
										mailDao.add(mailAddressee);
									}
								}
								if(!CommonUtil.isNull(listBCC)) {
									for (MailAddressee mailAddressee : listBCC) {
										mailAddressee.setComId(userInfo.getComId());
										mailAddressee.setMailId(mailId);
										mailAddressee.setUserId(userInfo.getId());
										mailAddressee.setType("BCC");
										mailDao.add(mailAddressee);
									}
								}
								
								
							}
							
						}
					}
					folder.close(false);// 关闭邮件夹对象
					store.close(); // 关闭连接对象
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 根据条件查询邮件总数
	 * @author hcj 
	 * @param newMail
	 * @return 
	 * @date 2018年9月27日 下午5:24:52
	 */
	private Integer querCountMail(Mail newMail) {
		return mailDao.querCountMail(newMail);
	}

	/**
	 * 简单发送邮件
	 * @author hcj 
	 * @param mail
	 * @throws MessagingException 
	 * @date 2018年9月26日 上午9:19:54
	 */
	public void sendMail(Mail mail) throws MessagingException {
		MailSet mailSet = (MailSet) mailDao.objectQuery(MailSet.class, mail.getAccountId());
		mail.setAccount(mailSet.getAccount());
		//发送参数配置
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailSet.getServerHost());
		mailSender.setPort(mailSet.getServerPort());
		ValiMailUsrPw valiMailUsrPw = new ValiMailUsrPw();
		Session session = valiMailUsrPw.getSmtpSession(mailSet.getSmtpSSL());
		mailSender.setSession(session);
		mailSender.setUsername(mailSet.getAccount());
		mailSender.setPassword(Encodes.decodeBase64(mailSet.getPasswd()));
		//发送内容
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
		helper.setFrom(mail.getAccount());
		helper.setTo(mail.getRecipients());
		//判断设置抄送人员
		if(!CommonUtil.isNull(mail.getCopyAccounts())) {
			StringBuffer sb = new StringBuffer();
		       for(int i=0; i<mail.getCopyAccounts().size(); i++) {
		    	   if(!CommonUtil.isNull(mail.getCopyAccounts().get(i))) {
		    		   sb.append(mail.getCopyAccounts().get(i));
		    		   if(i != mail.getCopyAccounts().size() - 1) {
			               sb.append(";");
			           }
		    	   }
		           
		       }
		       String[] cc = sb.toString().split(";");
		       if(!CommonUtil.isNull(cc)) {
		    	   helper.setCc(cc);
		       }
		}
		
		helper.setSubject(mail.getSubject());
		helper.setText(mail.getBody(),true);
		if(!CommonUtil.isNull(mail.getUpfiles())) {
			String basepath = FileUtil.getUploadBasePath();
			for (MailUpfile mailUpfile : mail.getUpfiles()) {
				Upfiles upfiles = (Upfiles) mailDao.objectQuery(Upfiles.class, mailUpfile.getUpfileId());
				if(!CommonUtil.isNull(upfiles)) {
					FileSystemResource file = new FileSystemResource(basepath+upfiles.getFilepath());
					helper.addAttachment(upfiles.getFilename(), file);
				}
				
			}
		}
		mailSender.send(message);
	}
	
	/**
	 * 查看本地发送邮件详情
	 * @author hcj 
	 * @param id
	 * @return 
	 * @date 2018年9月26日 上午10:13:45
	 */
	public Mail getMailDetailById(Integer id) {
		Mail mail = mailDao.getMailDetailById(id);
		if(!CommonUtil.isNull(mail)) {
			List<MailUpfile> fileLists = mailDao.listMailFiles(mail.getId());
			mail.setUpfiles(fileLists);
			
			//处理地址
			List<MailAddressee> listAddressee = this.listMailAddressee(mail.getId());
			List<MailAddressee> listAddresseeTo = new ArrayList<>();
			List<MailAddressee> listAddresseeCc = new ArrayList<>();
			List<MailAddressee> listAddresseeBcc = new ArrayList<>();
			if(!CommonUtil.isNull(listAddressee)) {
				for (MailAddressee mailAddressee : listAddressee) {
					if("TO".equals(mailAddressee.getType())) {
						listAddresseeTo.add(mailAddressee);
					}else if("CC".equals(mailAddressee.getType())) {
						listAddresseeCc.add(mailAddressee);
					}else if("BCC".equals(mailAddressee.getType())) {
						listAddresseeBcc.add(mailAddressee);
					}
				}
				mail.setListAddresseeTo(listAddresseeTo);
				mail.setListAddresseeCc(listAddresseeCc);
				mail.setListAddresseeBcc(listAddresseeBcc);
				mail.setListMailAddressee(listAddressee);
			}
		}
		return mail;
	}
	
	/**
	 * 删除本地发送记录
	 * @author hcj 
	 * @param userInfo
	 * @param ids 
	 * @date 2018年9月26日 下午2:21:17
	 */
	public void delSendMail(UserInfo userInfo, Integer[] ids) {
		if(!CommonUtil.isNull(ids)){
			for (Integer id : ids) {
				mailDao.delByField("mailAddressee", new String[]{"mailId"}, new Object[]{id});
				mailDao.delByField("mailUpfile", new String[]{"mailId"}, new Object[]{id});
				mailDao.delById(Mail.class, id);
			}
		}
	}
	
	
}
