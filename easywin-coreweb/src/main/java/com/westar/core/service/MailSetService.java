package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.MailSet;
import com.westar.base.util.Encodes;
import com.westar.core.dao.MailSetDao;

@Service
public class MailSetService {

	@Autowired
	MailSetDao mailSetDao;

	/**
	 * 查询个人邮箱设置
	 * @param mailSet
	 * @return
	 */
	public List<MailSet> listMailSet(MailSet mailSet) {
		List<MailSet> list = mailSetDao.listMailSet(mailSet);
		return list;
	}
	/**
	 * 分页查询个人邮箱设置
	 * @param mailSet
	 * @return
	 */
	public List<MailSet> listPagedMailSet(MailSet mailSet) {
		List<MailSet> list = mailSetDao.listPagedMailSet(mailSet);
		return list;
	}
	
	/**
	 * 添加邮箱设置
	 * @param mailSet
	 * @return
	 */
	public Integer addMailSet(MailSet mailSet) {
		String passWd = mailSet.getPasswd();
		//密码加密
		mailSet.setPasswd(Encodes.encodeBase64(passWd));
		Integer id = mailSetDao.add(mailSet);
		return id;
	}
	
	/**
	 * 查询个人邮箱设置
	 * @param id
	 * @return
	 */
	public MailSet getMailSetById(Integer id) {
		MailSet mailSet = (MailSet) mailSetDao.objectQuery(MailSet.class, id);
		//密码解密
		String passwd = mailSet.getPasswd();
		mailSet.setPasswd(Encodes.decodeBase64(passwd));
		return mailSet;
	}

	/**
	 * 添加邮箱设置
	 * @param mailSet
	 * @return
	 */
	public void updateMailSet(MailSet mailSet) {
		String passWd = mailSet.getPasswd();
		//密码加密
		mailSet.setPasswd(Encodes.encodeBase64(passWd));
		//修改邮箱设置
		mailSetDao.update(mailSet);
	}
	
	/**
	 * 删除邮箱配置
	 * @param id
	 */
	public Integer delMailSet(Integer id) {
		Integer count = mailSetDao.queryUseCount(id);
		if(count < 1) {
			mailSetDao.delById(MailSet.class, id);
		}
		return count;
	}
	
	
	
}
