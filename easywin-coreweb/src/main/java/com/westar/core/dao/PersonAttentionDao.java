package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.PersonAttention;
import com.westar.base.model.UserInfo;

@Repository
public class PersonAttentionDao extends BaseDao {
	
	/**
	 * 分页查询关注人员信息
	 * @param personAttention
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PersonAttention> listPagedPersonAttention(PersonAttention personAttention, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.userName,b.movePhone,b.email,d.depName,aa.job  \n");
		sql.append("from personAttention a \n");
		sql.append("left join userInfo b on b.id = a.userId \n");
		sql.append("left join userOrganic aa on b.Id=aa.userId and aa.comid = a.comid \n");
		sql.append("left join department d on aa.depId=d.id and d.comid=aa.comId \n");
		sql.append("where a.creator=? and a.comId=? \n");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		this.addSqlWhereLike(personAttention.getUserName(), sql, args,"and b.userName like ? ");
		this.addSqlWhere(personAttention.getDepId(), sql, args, " and d.id = ? ");
		return this.pagedQuery(sql.toString(),"a.id desc", args.toArray(), PersonAttention.class);
	}
	
	/**
	 * 查询所有的关注人信息
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PersonAttention> listPersonAttention(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.userName,b.movePhone,b.email,d.depName,aa.job  \n");
		sql.append("from personAttention a \n");
		sql.append("left join userInfo b on b.id = a.userId \n");
		sql.append("left join userOrganic aa on b.Id=aa.userId and aa.comid = a.comid \n");
		sql.append("left join department d on aa.depId=d.id and d.comid=aa.comId \n");
		sql.append("where a.creator=? and a.comId=? \n");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(),args.toArray(), PersonAttention.class);
	}
	
	/**
	 * 查询是否关注了该人员
	 * @param userId
	 * @param userInfo
	 * @return
	 */
	public Integer queryCountByUserId(Integer userId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from personAttention a \n");
		sql.append("where a.creator=? and a.comId=? and userId=? \n");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		args.add(userId);
		return this.countQuery(sql.toString(), args.toArray());
	}

	

}
