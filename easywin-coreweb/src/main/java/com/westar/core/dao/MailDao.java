package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Mail;
import com.westar.base.model.MailAddressee;
import com.westar.base.model.MailUpfile;
import com.westar.base.model.UserInfo;

@Repository
public class MailDao extends BaseDao {
	
	/**
	 * 分页查询邮件
	 * @author hcj 
	 * @param mail
	 * @param userInfo
	 * @return 
	 * @date 2018年9月18日 下午1:21:41
	 */
	@SuppressWarnings("unchecked")
	public List<Mail> listPagedMail(Mail mail, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,b.account from mail a ");
		sql.append("\n left join mailSet b on b.id = a.accountId ");
		sql.append("\n where 1=1 and a.userId=? and a.comId=? ");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		this.addSqlWhere(mail.getAccountId(), sql, args, " and a.accountId=? ");
		this.addSqlWhereLike(mail.getSubject(), sql, args, " and a.subject like ? ");
		this.addSqlWhere(mail.getFolder(), sql, args, " and a.folder=? ");
		sql.append("\n order by a.sendTime desc");
		return this.pagedQuery(sql.toString(),null, args.toArray(), Mail.class);
	}
	
	/**
	 * 获取发送邮件详情
	 * @author hcj 
	 * @param id
	 * @return 
	 * @date 2018年9月26日 上午10:17:03
	 */
	public Mail getMailDetailById(Integer id) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,b.account from mail a ");
		sql.append("\n left join mailSet b on b.id = a.accountId ");
		sql.append("\n where a.id=?");
		args.add(id);
		return (Mail) this.objectQuery(sql.toString(), args.toArray(), Mail.class);
	}
	
	/**
	 * 查询发送邮件附件
	 * @author hcj 
	 * @param id
	 * @return 
	 * @date 2018年9月26日 上午10:19:05
	 */
	@SuppressWarnings("unchecked")
	public List<MailUpfile> listMailFiles(Integer mailId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,b.filename,b.uuid  from mailUpfile a ");
		sql.append("\n left join upfiles b on b.id=a.upfileId  ");
		sql.append("\n where a.mailId=?");
		args.add(mailId);
		sql.append("\n order by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), MailUpfile.class);
	}
	
	/**
	 * 查询最大的发送时间
	 * @author hcj 
	 * @param mail
	 * @return 
	 * @date 2018年9月27日 下午4:11:07
	 */
	public Mail queryMaxSenTime(Mail mail) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select max(sendTime) sendTime  from mail where FOLDER=? and ACCOUNTID=?");
		args.add(mail.getFolder());
		args.add(mail.getAccountId());
		return (Mail) this.objectQuery(sql.toString(), args.toArray(), Mail.class);
	}
	
	/**
	 * 根据条件查询邮件总数
	 * @author hcj 
	 * @param newMail
	 * @return 
	 * @date 2018年9月27日 下午5:25:47
	 */
	public Integer querCountMail(Mail newMail) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(1) from mail a where 1=1 ");
		this.addSqlWhere(newMail.getAccountId(), sql, args, " and a.accountId=? ");
		this.addSqlWhere(newMail.getFolder(), sql, args, " and a.folder=? ");
		this.addSqlWhere(newMail.getSubject(), sql, args, " and a.subject=? ");
		//this.addSqlWhere(newMail.getBody(), sql, args, " and dbms_lob.compare(a.body,to_clob(?)) = 0");
		this.addSqlWhere(newMail.getSendTime(), sql, args, " and a.sendTime=? ");
		this.addSqlWhere(newMail.getFromAddress(), sql, args, " and a.fromAddress=? ");
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 通过mailID查询收件人
	 * @author hcj 
	 * @param mailId
	 * @return 
	 * @date 2018年9月28日 上午10:16:19
	 */
	@SuppressWarnings("unchecked")
	public List<MailAddressee> listMailAddressee(Integer mailId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from mailAddressee where mailId = ?");
		args.add(mailId);
		return this.listQuery(sql.toString(), args.toArray(), MailAddressee.class);
	}

}
