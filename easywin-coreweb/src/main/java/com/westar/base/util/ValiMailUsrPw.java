package com.westar.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.MessageNumberTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;

import com.westar.base.model.Mail;
import com.westar.base.model.MailAddressee;
import com.westar.base.model.MailSet;
import com.westar.base.model.Upfiles;
import com.westar.core.web.PaginationContext;


public class ValiMailUsrPw {
	
	/**
	 * 获取Smtp Session
	 * @author hcj 
	 * @param mailSet
	 * @return 
	 * @date 2018年9月18日 上午10:16:58
	 */
	public Session getSmtpSession(String smtpSSL ){
		// 判断是否需要身份认证
	    Properties pro = new Properties();
	    pro.put("mail.smtp.auth", "true");   
		//创建Session对象，此时需要配置传输的协议，是否身份认证  
		pro.setProperty("mail.transport.protocol", "smtp");
		if(!CommonUtil.isNull(smtpSSL) && "1".equals(smtpSSL)) {
			pro.put("mail.smtp.ssl.enable", "true");
		}
		
        Session session = Session.getInstance(pro);
        return session;
	}
	
	/**
	 * 获取pop session
	 * @author hcj 
	 * @param mailSet
	 * @return 
	 * @date 2018年9月18日 上午10:16:34
	 */
	public Session getPopSession(String smtpSSL){
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "pop3"); // 使用的协议（JavaMail规范要求）
		if(!CommonUtil.isNull(smtpSSL) && "1".equals(smtpSSL)) {
			props.put("mail.pop3.ssl.enable", "true");
		}
		// 获取连接
		Session session = Session.getDefaultInstance(props);
        return session;
	}
	
	/**
	 * 获取imap session
	 * @author hcj 
	 * @param smtpSSL
	 * @return 
	 * @date 2018年9月19日 下午1:04:34
	 */
	public Session getImapSession(String smtpSSL){
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imap"); // 使用的协议（JavaMail规范要求）
		if(!CommonUtil.isNull(smtpSSL) && "1".equals(smtpSSL)) {
			props.put("mail.imap.ssl.enable", "true");
		}
		// 获取连接
		Session session = Session.getDefaultInstance(props);
        return session;
	}
	
    /**
     * 发送验证邮件
     * @param to 发送对象
     * @param subject 主题
     * @param body 内容
     * @return
     * @throws MessagingException 
     * @throws AddressException 
     */
    public void valiDate(Session session,MailSet mailSet) throws AddressException, MessagingException,AuthenticationFailedException {
    	
         // 使用前面文章所完成的邮件创建类获得 Message 对象  
         MimeMessage msg = new MimeMessage(session);  
         msg.setFrom(new InternetAddress(mailSet.getAccount())); 
         List<String> mailTos = mailSet.getMailTos();
         if(null!=mailTos && mailTos.size()>0){
        	 Integer receiverCount = mailTos.size();
        	 InternetAddress[] address = new InternetAddress[receiverCount];  
             for (int i = 0; i < receiverCount; i++) {  
                 address[i] = new InternetAddress(mailTos.get(i));  
             }  
             msg.setRecipients(Message.RecipientType.TO, address);  

             msg.setSubject(mailSet.getSubject());  
             
             // 用于保存最终正文部分  
             MimeBodyPart contentBody = new MimeBodyPart();  
             // 用于组合文本和图片，"related"型的MimeMultipart对象  
             MimeMultipart contentMulti = new MimeMultipart("related");  
             
             // 正文的文本部分  
             MimeBodyPart textBody = new MimeBodyPart();  
             textBody.setContent(mailSet.getBody(), "text/html;charset=gbk");  
             contentMulti.addBodyPart(textBody);
             
             contentBody.setContent(contentMulti); 
             
             // 将邮件中各个部分组合到一个"mixed"型的 MimeMultipart 对象  
             MimeMultipart allPart = new MimeMultipart("mixed");  
             allPart.addBodyPart(contentBody);  
             
             // 将上面混合型的 MimeMultipart 对象作为邮件内容并保存  
             msg.setContent(allPart);  
             msg.saveChanges();  
             
             // 由 Session 对象获得 Transport 对象  
             Transport transport = session.getTransport();  
             // 发送用户名、密码连接到指定的 smtp 服务器  
             transport.connect(mailSet.getServerHost(), mailSet.getAccount(), mailSet.getPasswd());  
             
             transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));  
             transport.close();  
         }
         //接收邮件验证
         Session sendSession = this.getImapSession(mailSet.getSmtpSSL());
         // 获取Store对象
      	 Store store = sendSession.getStore();
      	 store.connect(mailSet.getServerImapHost(), mailSet.getAccount(), mailSet.getPasswd());
      	 store.close(); // 关闭连接对象
    }
    
    
    /**
	 * 获取邮件
	 * @author hcj 
	 * @param mailSet
	 * @return 
	 * @date 2018年9月18日 上午10:16:34
	 */
	public List<Mail> getMails(MailSet mailSet){
		List<Mail> lists = new ArrayList<>();
		// 获取连接
		Session session = this.getImapSession(mailSet.getSmtpSSL());
		try {
			// 获取Store对象
			Store store = session.getStore();
			store.connect(mailSet.getServerImapHost(), mailSet.getAccount(), Encodes.decodeBase64(mailSet.getPasswd()));
			// 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
			Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
			folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限
			int messageCount = folder.getMessageCount();
			PaginationContext.setTotalCount(messageCount); 
			Message[] messages = folder.getMessages((messageCount - PaginationContext.getPageSize()- PaginationContext.getOffset()+1)>0?(messageCount - PaginationContext.getPageSize()- PaginationContext.getOffset()+1):1,messageCount - PaginationContext.getOffset());// 得到邮箱帐户中的所有邮件
			if(!CommonUtil.isNull(messages)) {
				for (int i = messages.length-1; i >= 0; i--) {
					try {
						Mail newMail = new Mail();
						newMail.setSubject(messages[i].getSubject());
						if(!CommonUtil.isNull(messages[i].getSentDate())) {
							newMail.setRecordCreateTime(DateTimeUtil.formatDate(messages[i].getSentDate(), DateTimeUtil.yyyy_MM_dd_HH_mm));
						}
						if(!CommonUtil.isNull(messages[i].getFrom())) {
							InternetAddress address = (InternetAddress) messages[i].getFrom()[0];
							newMail.setAccount(address.getAddress());
							newMail.setPersonal(address.getPersonal());
						}
						newMail.setId(messages[i].getMessageNumber());
						newMail.setReadState(messages[i].getFlags().contains(Flags.Flag.SEEN));
						newMail.setRecipientId(mailSet.getId());
						lists.add(newMail);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}
			folder.close(false);// 关闭邮件夹对象
			store.close(); // 关闭连接对象
			return lists;
		} catch (MessagingException e) {
			e.printStackTrace();
			return lists;
		} 
		
	}
	
	
	/**
	 * 根据时间获取邮件
	 * @author hcj 
	 * @param mailSet
	 * @return 
	 * @date 2018年9月27日 下午2:29:41
	 */
	@SuppressWarnings("deprecation")
	public static Message[] getMessages(MailSet mailSet){
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
			if(!CommonUtil.isNull(mailSet.getSendTime())) {
				SearchTerm st = new SentDateTerm(6,new Date(mailSet.getSendTime()));//6 代表大于等于这时间之后           
				messages = folder.search(st);
			}else {
				messages = folder.getMessages();// 得到邮箱帐户中的所有邮件
			}
			folder.close(false);// 关闭邮件夹对象
			store.close(); // 关闭连接对象
			return messages;
		} catch (MessagingException e) {
			e.printStackTrace();
			return messages;
		}
	}
	
	
	/**
	 * 获取邮件详情
	 * @author hcj 
	 * @param mailSet
	 * @param mailId
	 * @return 
	 * @date 2018年9月25日 上午9:23:11
	 */
	public Mail getMailDetail(MailSet mailSet, Integer mailNum) {
		Mail mail = new Mail();
		// 获取连接
		Session session = this.getImapSession(mailSet.getSmtpSSL());
		try {
			// 获取Store对象
			Store store = session.getStore();
			store.connect(mailSet.getServerImapHost(), mailSet.getAccount(), Encodes.decodeBase64(mailSet.getPasswd()));
			// 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
			Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
			folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限
			SearchTerm search = new MessageNumberTerm(mailNum);
			Message[] messages = folder.search(search);
			if(!CommonUtil.isNull(messages)) {
				mail.setSubject(messages[0].getSubject());
				if(!CommonUtil.isNull(messages[0].getSentDate())) {
					mail.setRecordCreateTime(DateTimeUtil.formatDate(messages[0].getSentDate(), DateTimeUtil.yyyy_MM_dd_HH_mm));
				}
				if(!CommonUtil.isNull(messages[0].getFrom())) {
					mail.setAccount(MimeUtility.decodeText(messages[0].getFrom()[0].toString()));
				}
				mail.setId(messages[0].getMessageNumber());
				mail.setReadState(messages[0].getFlags().contains(Flags.Flag.SEEN));
				
				StringBuffer content = new StringBuffer();
				String contentType = messages[0].getContentType();
				getMailTextContent(messages[0], content,contentType.startsWith("text/plain"));
				
				//当content不存在时换一种方式查询
				if(CommonUtil.isNull(content.toString())) {
					getMailTextContent(messages[0], content);
				}
				mail.setBody(content.toString());
				//String basepath = FileUtil.getUploadBasePath();
				//mail.setFiles(saveAttachment(messages[0],basepath));
			}
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
		
		return mail;
	}
  
	
	/**
	 * 获得邮件文本内容(区分text)
	 * @param part 邮件体
	 * @param content 存储邮件文本内容的字符串
	 * @param flag 
	 * @throws UnsupportedEncodingException 
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void getMailTextContent(Part part, StringBuffer content, boolean flag) throws MessagingException, IOException  {
		//如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
 		boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;	
		if (part.isMimeType("text/html") && !isContainTextAttach && !flag) {  
			try {
				content.append(MimeUtility.decodeText(part.getContent().toString())); 
			} catch (Exception e) {
				
			}
            
        } else if(part.isMimeType("text/plain") && !isContainTextAttach && flag){
        	try {
				content.append(MimeUtility.decodeText(part.getContent().toString())); 
			} catch (Exception e) {
				
			}
        	flag = false;
        } else if (part.isMimeType("message/rfc822")) {	
			getMailTextContent((Part)part.getContent(),content,flag);
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				getMailTextContent(bodyPart,content,flag);
			}
		}
	}
	
	/**
	 * 获得邮件文本内容(不区分text)
	 * @author hcj 
	 * @param part
	 * @param content
	 * @throws MessagingException
	 * @throws IOException 
	 * @date 2018年9月26日 上午11:19:26
	 */
	public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
		//如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
 		boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;	
		if (part.isMimeType("text/*") && !isContainTextAttach) {  
			try {
				content.append(MimeUtility.decodeText(part.getContent().toString())); 
			} catch (Exception e) {
				
			}
        }else if (part.isMimeType("message/rfc822")) {	
			getMailTextContent((Part)part.getContent(),content);
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				getMailTextContent(bodyPart,content);
			}
		}
	}
	
	/**
	 * 保存附件
	 * @param part 邮件中多个组合体中的其中一个组合体
	 * @param destDir  附件保存目录
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<Upfiles> saveAttachment(Part part,Integer comId) throws UnsupportedEncodingException, MessagingException,
			FileNotFoundException, IOException {
		List<Upfiles> files = new ArrayList<>();
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();	//复杂体邮件
			//复杂体邮件包含多个邮件体
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				//获得复杂体邮件中其中一个邮件体
				BodyPart bodyPart = multipart.getBodyPart(i);
				//某一个邮件体也有可能是由多个邮件体组成的复杂体
				String disp = bodyPart.getDisposition();
				if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
					InputStream is = bodyPart.getInputStream();
					if(!CommonUtil.isNull(decodeText(bodyPart.getFileName()))){
						Upfiles upfiles = saveFile(is, decodeText(bodyPart.getFileName()),comId);
						if(null != upfiles){
							files.add(upfiles);
						}
					}
					
				} else if (bodyPart.isMimeType("multipart/*")) {
					saveAttachment(bodyPart,comId);
				} else {
					String contentType = bodyPart.getContentType();
					if ((contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) && !CommonUtil.isNull(decodeText(bodyPart.getFileName()))) {
						Upfiles upfiles = saveFile(bodyPart.getInputStream(), decodeText(bodyPart.getFileName()),comId);
						if(null != upfiles){
							files.add(upfiles);
						}
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachment((Part) part.getContent(),comId);
		}
		return files;
	}
	
	/**
	 * 读取输入流中的数据保存至指定目录
	 * @param is 输入流
	 * @param fileName 文件名
	 * @param comId 
	 * @return 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static Upfiles saveFile(InputStream is, String fileName, Integer comId)
			throws FileNotFoundException, IOException {
		List<Object> list =  FileUtil.getAllowFileTypes();
		String ext = FileUtil.getExtend(fileName);
		if(!list.contains(ext)){
			return null;
		}
		String baseTempPath = FileUtil.getUploadBasePath() + FileUtil.getUploadPath(comId);
		// 临时文件的路径
		String tempFilePath = baseTempPath + File.separator+ fileName;
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(new File(tempFilePath)));
		int len = -1;
		while ((len = bis.read()) != -1) {
			bos.write(len);
			bos.flush();
		}
		Upfiles upfiles = new Upfiles();
		upfiles.setComId(comId);
		upfiles.setUuid(UUIDGenerator.getUUID());
		upfiles.setFilename(fileName);
		upfiles.setFilepath(FileUtil.getUploadPath(comId)+ File.separator+ fileName);
		upfiles.setFileExt(FileUtil.getExtend(fileName));
		bos.close();
		bis.close();
		long a = new File(tempFilePath).length();
		// 文件大小
		String fileSize = a+"";
		String sizeM = MathExtend.divide(fileSize,String.valueOf(1024), 2);
		String dw = "K";
		if (Float.parseFloat(sizeM) > 1024) {
			sizeM = MathExtend.divide(sizeM,String.valueOf(1024), 2);
			dw = "M";
			if (Float.parseFloat(sizeM) > 1024) {
				sizeM = MathExtend.divide(sizeM,String.valueOf(1024), 2);
				dw = "G";
			}
		}
		upfiles.setSizeb(Integer.parseInt(fileSize));
		upfiles.setSizem(sizeM + dw);
		return upfiles;
	}
	
	/**
	 * 文本解码
	 * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
	 * @return 解码后的文本
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeText(String encodeText) throws UnsupportedEncodingException {
		if (encodeText == null || "".equals(encodeText)) {
			return "";
		} else {
			return MimeUtility.decodeText(encodeText);
		}
	}
	
	/** 
          * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人 
     * <p>Message.RecipientType.TO  收件人</p> 
     * <p>Message.RecipientType.CC  抄送</p> 
     * <p>Message.RecipientType.BCC 密送</p> 
     * @param msg 邮件内容 
     * @param type 收件人类型 
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ... 
     * @throws MessagingException 
     */  
    public static List<MailAddressee> getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException{  
        List<MailAddressee> lists = new ArrayList<>();
        Address[] addresss = null;  
        if (type == null) {  
            addresss = msg.getAllRecipients();  
        } else {  
            addresss = msg.getRecipients(type);  
        }  
        if(!CommonUtil.isNull(addresss)) {
        	 for (Address address : addresss) {  
                 InternetAddress internetAddress = (InternetAddress)address;  
                 MailAddressee addressee = new MailAddressee();
                 addressee.setAddress(internetAddress.getAddress());
                 addressee.setPersonal(internetAddress.getPersonal());
                 lists.add(addressee);
             }  
        } 
        return lists;  
    } 

}