package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.JoinRecord;
import com.westar.base.model.JoinTemp;
import com.westar.base.model.PassYzm;
import com.westar.base.util.DateTimeUtil;

@Repository
public class RegistDao extends BaseDao {

	/**
	 * 判断邮箱验证码是否过期
	 * @param account
	 * @return
	 */
	public PassYzm getPassYzm(String account) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		sql.append("\n select a.*,");
		sql.append("\n case when  round(to_number(TO_DATE('"+nowTime+"','yyyy-mm-dd hh24:mi:ss')");
		sql.append("\n -TO_DATE(a.recordcreatetime,'yyyy-mm-dd hh24:mi:ss'))*24*60*60-20*60)<=0 then 1 else 0");
		sql.append("\n end as enabled");
		sql.append("\n from passYzm a where 1=1 ");
		this.addSqlWhere(account.toLowerCase(), sql, args, " and lower(a.account)=?");
		return (PassYzm) this.objectQuery(sql.toString(), args.toArray(), PassYzm.class);
	}

	/**
	 * 邮箱验证码验重
	 * @param passYzm
	 * @return
	 */
	public boolean passYzmCheck(Integer passYzm) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from passYzm \n");
		sql.append("where passYzm=? \n");
		args.add(passYzm);
		return (PassYzm)this.objectQuery(sql.toString(), args.toArray(),PassYzm.class)!=null;
	}

	/**
	 * 查询申请表中是否有无效数据
	 * @param account 
	 * @return
	 */
	public JoinTemp findInvalidUser(String account) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from joinTemp \n");
		sql.append("where lower(account)=? and joinType=-1\n");
		args.add(account);
		return (JoinTemp)this.objectQuery(sql.toString(), args.toArray(),JoinTemp.class);
	}
	/**
	 * 统一临时表中的密码
	 * @param account
	 * @param passwordMD5 
	 */
	public void updatePasswd(String account, String passwordMD5) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update joinTemp set passwd=? where lower(account)=? and joinType>-1");
		args.add(passwordMD5);
		args.add(account);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 修改用户临时表的其他信息
	 * @param account 临时表的账号
	 * @param userName 统一账号的用户名称
	 * @param passwd 统一账号的密码
	 */
	public void updateJoinTemps(String account, String userName, String passwd) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update joinTemp set passwd=?,userName=? where lower(account)=?");
		args.add(passwd);
		args.add(userName);
		args.add(account);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	
	/**
	 * 判断用户是否有一个激活码
	 * @param userEmail
	 * @param comId
	 * @return
	 */
	public JoinRecord getConfirmId(String userEmail, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select  a.* from JoinRecord a ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userEmail, sql, args, " and lower(a.account)=?");
		return (JoinRecord) this.objectQuery(sql.toString(), args.toArray(), JoinRecord.class);
	}

	/**
	 * 通过账号取得用户临时信息
	 * @param account 账号
	 * @param comId 团队号
	 * @param joinType -1未操作 0注册 1申请 2 邀请
	 * @return
	 */
	public JoinTemp getJoInTempByAccount(String account, Integer comId,Integer joinType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from joinTemp \n");
		sql.append("where lower(account)=? and comId=? and joinType=?\n");
		args.add(account);
		args.add(comId);
		args.add(joinType);
		return (JoinTemp)this.objectQuery(sql.toString(), args.toArray(),JoinTemp.class);
	}
	/**
	 * 通过账号取得用户临时信息
	 * @param account 账号
	 * @param comId 团队号
	 * @param joinType -1未操作 0注册 1申请 2 邀请
	 * @return
	 */
	public JoinRecord getJoInRecordByAccount(String account, Integer comId,Integer joinType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from joinRecord \n");
		sql.append("where lower(account)=? and comId=? and joinType=?\n");
		args.add(account);
		args.add(comId);
		args.add(joinType);
		return (JoinRecord)this.objectQuery(sql.toString(), args.toArray(),JoinRecord.class);
	}
	/**
	 * 通过账号取得用户临时信息
	 * @param account 账号
	 * @param comId 团队号
	 * @param joinType -1未操作 0注册 1申请 2 邀请
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JoinRecord> getJoInRecordByDouAccount(String email,String movePhone,Integer comId,Integer joinType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from joinRecord \n");
		sql.append("where comId=? and joinType=? and (lower(account)=? or lower(account)=?)\n");
		args.add(comId);
		args.add(joinType);
		args.add(email);
		args.add(movePhone);
		sql.append("order by id desc\n");
		return this.listQuery(sql.toString(), args.toArray(), JoinRecord.class);
	}

	/**
	 * 列出账号的所有邀请记录
	 * @param account 账号
	 * @param joinType 加入方式
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JoinTemp> getJoInTemp4Login(String account) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.orgnum,a.orgname,0 isSysUser,1 neworder");
		sql.append("\n from organic a inner join jointemp bb  on a.orgnum=bb.comid and bb.joinType=2");
		sql.append("\n where a.enabled='1'");
		this.addSqlWhere(account, sql, args, " and lower(bb.account)=? " );
		sql.append("\n and not exists(");
		sql.append("\n select id from userinfo b where bb.account=b.nickname or bb.account=b.email or bb.account=b.movephone");
		sql.append("\n )");
		sql.append("\n union all");
		sql.append("\n select a.id,a.orgnum,a.orgname,0 isSysUser,1 neworder");
		sql.append("\n from organic a inner join jointemp bb  on a.orgnum=bb.comid and bb.joinType=2");
		sql.append("\n left join userinfo c on (bb.account=c.nickname or bb.account=c.email or bb.account=c.movephone)");
		sql.append("\n where a.enabled='1'");
		sql.append("\n and exists(");
		sql.append("\n select id from userinfo b where (bb.account=b.nickname or bb.account=b.email or bb.account=b.movephone)");
		this.addSqlWhere(
				account,
				sql,
				args,
				" and (lower(b.email)=? or lower(b.nickname)=? or b.movephone=?)",
				3);
		sql.append("\n )");
		return this.listQuery(sql.toString(), args.toArray(), JoinTemp.class);
	}
}
