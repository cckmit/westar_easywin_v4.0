package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Jfzb;
import com.westar.base.model.JfzbDepScope;
import com.westar.base.model.UserInfo;

@Repository
public class JfzbDao extends BaseDao {

	/**
	 * 分页查询积分指标信息
	 * @param jfzb 积分指标查询条件
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Jfzb> listPagedJfbz(Jfzb jfzb, UserInfo sessionUser) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//积分标准主键，创建时间,团队号,适用部门
		sql.append("\n select a.id,a.recordCreateTime,a.comId,depScope.depId,dep.depName,a.recorderId, ");
		sql.append("\n a.jfzbTypeId,dicType.typeName jfzbTypeName,a.leveTwo,a.describe,a.jfTop,a.jfBottom,");
		//录入人员信息
		sql.append("\n usr.userName recorderName");
		//积分标准表
		sql.append("\n from  jfzb a");
		//适用部门
		sql.append("\n inner join jfzbDepScope depScope on  a.id=depScope.jfzbId");
		sql.append("\n inner join department dep on  depScope.depId=dep.id");
		//积分指标类型表
		sql.append("\n inner join jfzbType dicType on  a.jfzbTypeId=dicType.id");
		//记录人员
		sql.append("\n inner join userInfo usr on  usr.id=a.recorderId");
		
		sql.append("\n where 1=1 and a.comId=? ");
		args.add(sessionUser.getComId());
		this.addSqlWhere(jfzb.getJfzbTypeId(), sql, args, "\n and a.jfzbTypeId=?");
		this.addSqlWhereLike(jfzb.getDescribe(), sql, args, "\n and a.describe like ?");
		return this.pagedQuery(sql.toString(), " a.jfzbTypeId,depScope.id,a.id ", args.toArray(), Jfzb.class);
	}
	/**
	 * 查询积分标准信息
	 * @param jfzbId 积分标准主键
	 * @return
	 */
	public Jfzb queryJfzbById(Integer jfzbId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//积分标准主键，创建时间,团队号
		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.remark,");
		sql.append("\n a.jfzbTypeId,dicType.typeName jfzbTypeName,a.leveTwo,a.describe,a.jfTop,a.jfBottom ");
		//积分标准表
		sql.append("\n from  jfzb a");
		//积分指标类型表
		sql.append("\n inner join jfzbType dicType on  a.jfzbTypeId=dicType.id ");
		sql.append("\n where 1=1 and a.id =? ");
		args.add(jfzbId);
		return (Jfzb) this.objectQuery(sql.toString(), args.toArray(), Jfzb.class);
	}
	/**
	 * 查询积分标准适用部门范围
	 * @param jfzbId 积分主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JfzbDepScope> listJfzbDepScope(Integer jfzbId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.depId,b.depName");
		sql.append("\n from jfzbDepScope a ");
		sql.append("\n inner join department b on a.depId=b.id ");
		sql.append("\n where 1=1 and a.jfzbId=?");
		args.add(jfzbId);
		sql.append("\n order by a.depId");
		return this.listQuery(sql.toString(), args.toArray(), JfzbDepScope.class);
	}
	/**
	 * 查询人员的积分指标信息
	 * @param jfzb
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Jfzb> listAllJfzb(Jfzb jfzb, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//积分标准主键，创建时间,团队号,适用部门
		sql.append("\n select a.id,a.recordCreateTime,a.comId,");
		sql.append("\n  a.jfzbTypeId,dicType.typeName jfzbTypeName,a.leveTwo,a.describe,a.jfTop,a.jfBottom");
		//积分标准表
		sql.append("\n from  jfzb a");
		//适用部门
		sql.append("\n inner join jfzbDepScope depScope on  a.id=depScope.jfzbId");
		//积分指标类型表
		sql.append("\n inner join jfzbType dicType on  a.jfzbTypeId=dicType.id");
		
		sql.append("\n where 1=1 and a.comId=? ");
		args.add(userInfo.getComId());
		this.addSqlWhere(jfzb.getJfzbTypeId(), sql, args, "\n and a.jfzbTypeId=?");
		this.addSqlWhereLike(jfzb.getDescribe(), sql, args, "\n and (a.describe like ? or a.leveTwo like ?)",2);
		
		//适用的人员主键
		Integer scopeUserId = jfzb.getScopeUserId();
		//查询需要涉及到的人员
		sql.append("\n and exists(");
		
		sql.append("\n 	select subdep.* from department subdep ");
		sql.append("\n 		where subdep.comid=? and subdep.id=depScope.depId");
		args.add(userInfo.getComId());
		sql.append("\n 		start with subdep.id=(select uorg.depid from userorganic uorg");
		sql.append("\n 		where uorg.comid=? ");
		args.add(userInfo.getComId());
		sql.append("\n   		and uorg.userid=?");
		args.add(scopeUserId);
		sql.append("\n 		)");
		sql.append("\n  CONNECT BY  subdep.id = PRIOR subdep.parentid");
		
		sql.append("\n )");
		sql.append("\n order by a.jfzbTypeId,depScope.id,a.id");
		return this.listQuery(sql.toString(),args.toArray(), Jfzb.class);
	}

	

}
