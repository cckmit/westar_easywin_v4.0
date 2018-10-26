package com.westar.base.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**  
 * 邮件发送程序  
 * @author zzq  
 */ 
public class MessageSender {  
	
	/**
	 *  本机smtp服务器
	 */
	private static final String HOST = "smtp.exmail.qq.com";
	/**
	 * 邮件发送人的邮件地址
	 */
	private static final String FROM = "westar@westarsoft.com.cn";
	/**
	 * 发件人的邮件帐户
	 */
	private static final String USERNAME = "westar@westarsoft.com.cn";
	/**
	 *  发件人的邮件密码
	 */
	private static final String PASSWORD = "WESTARsoft1234";
	/**
	 * 发件
	 */
	private static final String PROTOCOL = "smtp";
	
	private static final String PORT = "465";
	/**
	 * 只编译一次，后边只管用
	 */
	private static MessageSender sender = new MessageSender();
	/**
	 * 只编译一次，后边只管用
	 */
	private static Session session;
	
	private MessageSender(){
		//创建Session对象，此时需要配置传输的协议，是否身份认证  
//		Properties property = new Properties();  
//        property.setProperty("mail.transport.protocol", PROTOCOL);  
//        property.setProperty("mail.smtp.auth", "true");
//        property.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
     // 判断是否需要身份认证
	    Properties pro = new Properties();
	    pro.put("mail.smtp.auth", "true");   
		//创建Session对象，此时需要配置传输的协议，是否身份认证  
		pro.setProperty("mail.transport.protocol", PROTOCOL);  
		pro.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		pro.setProperty("mail.smtp.port", PORT);
		pro.setProperty("mail.smtp.socketFactory.port", PORT);
		
        session = Session.getInstance(pro);
	}
	
	public static MessageSender getMessageSender(){
		return sender;
	}

    /**  
     * 根据传入的文件路径创建附件并返回  
     */ 
    @SuppressWarnings("unused")
	private MimeBodyPart createAttachment(String fileName) throws Exception {  
        MimeBodyPart attachmentPart = new MimeBodyPart();  
        FileDataSource fds = new FileDataSource(fileName);  
        attachmentPart.setDataHandler(new DataHandler(fds));  
        attachmentPart.setFileName(fds.getName());  
        return attachmentPart;  
    }  
 
    /**  
     * 根据传入的邮件正文body和文件路径创建图文并茂的正文部分  
     */ 
    private MimeBodyPart createContent(String body, String fileName)  
            throws Exception {  
        // 用于保存最终正文部分  
        MimeBodyPart contentBody = new MimeBodyPart();  
        // 用于组合文本和图片，"related"型的MimeMultipart对象  
        MimeMultipart contentMulti = new MimeMultipart("related");  
 
        // 正文的文本部分  
        MimeBodyPart textBody = new MimeBodyPart();  
        textBody.setContent(body, "text/html;charset=gbk");  
        contentMulti.addBodyPart(textBody); 
        //此功能暂时不用
        if(null!=fileName){
//        	 正文的图片部分  
	        MimeBodyPart jpgBody = new MimeBodyPart();  
	        FileDataSource fds = new FileDataSource(fileName);  
	        jpgBody.setDataHandler(new DataHandler(fds));  
	        jpgBody.setContentID("logo_jpg");  
	        contentMulti.addBodyPart(jpgBody);  
        }
        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文  
        contentBody.setContent(contentMulti);  
        return contentBody;  
    }  
 
    /**  
     * 根据传入的 Seesion 对象创建混合型的 MIME消息  
     */ 
    private MimeMessage createMessage(String to,String subject,String body) throws Exception {  
 
        MimeMessage msg = new MimeMessage(session);  
        msg.setFrom(new InternetAddress(FROM));  
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));  
        msg.setSubject(subject);  
 
        // 创建邮件的各个 MimeBodyPart 部分  
//        MimeBodyPart attachment01 = createAttachment("F:\\java\\Hello_JavaMail");  
//        MimeBodyPart attachment02 = createAttachment("F:\\java\\Hello_JavaMail.7z");  
        MimeBodyPart content = createContent(body, null);  
 
        // 将邮件中各个部分组合到一个"mixed"型的 MimeMultipart 对象  
        MimeMultipart allPart = new MimeMultipart("mixed");  
//        allPart.addBodyPart(attachment01);  
//        allPart.addBodyPart(attachment02);  
        allPart.addBodyPart(content);  
 
        // 将上面混合型的 MimeMultipart 对象作为邮件内容并保存  
        msg.setContent(allPart);  
        msg.saveChanges();  
        return msg;  
    }  
    /**
     * 发送邮件
     * @param to 发送对象
     * @param subject 主题
     * @param body 内容
     * @return
     * @throws Exception 
     */
    public static void sendEmail(String to,String subject,String body) throws Exception{
         // 使用前面文章所完成的邮件创建类获得 Message 对象  
         MimeMessage mail = sender.createMessage(to,subject,body);  
         // 由 Session 对象获得 Transport 对象  
         Transport transport = session.getTransport();  
         // 发送用户名、密码连接到指定的 smtp 服务器  
         transport.connect(HOST, USERNAME, PASSWORD);  
  
         transport.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));  
         transport.close();  
    }
    /**
     * 测试邮件发送
     * @param args
     */
    public static void main(String[] args) {
		MessageSender sender = MessageSender.getMessageSender();
		 //收件人
        String to = "zzqliwei@qq.com"; 
        //标题
        String subject = "创建内含附件、图文并茂的邮件！"; 
        //内容
        String body = "<h4>内含附件、图文并茂的邮件测试！！！1209114537@qq.com</h4> </br>" 
            + "<a href = http://haolloyin.blog.51cto.com/> 蚂蚁</a></br>" 
            + "</a>"; 
		try {
			sender.sendEmail(to, subject, body);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

 
} 