package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.MailSet;

@Repository
public class MailSetDao extends BaseDao {

	/**
	 * 查询个人邮箱设置
	 * @param mailSet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MailSet> listMailSet(MailSet mailSet) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from mailSet a where 1=1");
		this.addSqlWhere(mailSet.getUserId(), sql, args, " and a.userId=?");
		sql.append("\n order by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), MailSet.class);
	}
	
	/**
	 * 分页查询个人邮箱设置
	 * @param mailSet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MailSet> listPagedMailSet(MailSet mailSet) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from mailSet a where 1=1");
		this.addSqlWhere(mailSet.getUserId(), sql, args, " and a.userId=?");
		sql.append("\n order by a.id desc");
		return this.pagedQuery(sql.toString(),null, args.toArray(), MailSet.class);
	}
	
	/**
	 * 查询是否使用
	 * @author hcj 
	 * @param accountId
	 * @return 
	 * @date 2018年9月26日 上午11:31:05
	 */
	public Integer queryUseCount(Integer accountId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(1) from mail a where accountId = ?");
		args.add(accountId);
		return this.countQuery(sql.toString(), args.toArray());
	}

	

}
