package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.westar.base.model.Organic;
import com.westar.base.model.OrganicCfg;
import com.westar.base.model.OrganicSpaceCfg;
import com.westar.base.model.UserInfo;
import com.westar.base.model.UserOrganic;
import com.westar.base.util.ConstantInterface;

@Repository
public class OrganicDao extends BaseDao {

	/**
	 * 查询企业信息
	 * 
	 * @param orgNum 团队组织号
	 * @return
	 */
	public Organic getOrgInfo(Integer orgNum) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.uuid as logoUuid,b.filename as logoName,");
		sql.append("\n d.username linkerName,d.email linkerEmail,d.movephone linkerMovePhone,e.members,");
		sql.append("\n case when orgSpace.Usersnum is null then "+ConstantInterface.ORG_DEFAULT_MEMBERS+" else orgSpace.Usersnum end as usersUpperLimit,");
		sql.append("\n orgSpace.endDate,");
		sql.append("\n case when orgSpace.Enddate >= to_char(sysdate,'yyyy-MM-dd') then 1 else 0 end as inService");
		sql.append("\n from organic ");
		sql.append("\n a left join upfiles b on a.logo=b.id");
		sql.append("\n inner join userOrganic c on a.orgnum=c.comid");
		sql.append("\n inner join userinfo d on c.userid = d.id");
		sql.append("\n inner join (");
		sql.append("\n  select org.comid,count(u.id) as members");
		sql.append("\n  from userinfo u ");
		sql.append("\n  inner join userOrganic org on u.id=org.userid");
		sql.append("\n  where org.enabled=1 and org.inService=1 group by org.comid");
		sql.append("\n ) e on a.orgnum = e.comId");
		sql.append("\n left join organicSpaceCfg orgSpace on a.orgnum = orgSpace.Comid and orgSpace.Enddate >= to_char(sysdate,'yyyy-MM-dd')");
		sql.append("\n where a.orgnum=? and c.admin=1 and rownum=1");
		args.add(orgNum);
		return (Organic) this.objectQuery(sql.toString(), args.toArray(), Organic.class);
	}

	/**
	 * 验证企业是否存在
	 * 
	 * @param orgNum
	 * @return
	 */
	public Organic getOrganicByNum(Integer orgNum) {
		StringBuffer sql = new StringBuffer("select * from organic where orgnum=?");

		return (Organic) this.objectQuery(sql.toString(),new Object[] { orgNum }, Organic.class);
	}

	/**
	 * 新注册企业用户账号验证
	 * 
	 * @param account
	 * @return
	 */
	public Organic orgAccountCheck(String account) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from organic \n");
		sql.append("where lower(email)=? or legalManTel=? \n");
		args.add(account);
		args.add(account);
		Organic organic = (Organic) this.objectQuery(sql.toString(), args.toArray(), Organic.class);
		return organic;
	}

	/**
	 * 当前账号已加入的企业
	 * 
	 * @param email
	 * @param passwordMD5 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Organic> listUserOrg(String email, String passwordMD5) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("\n select distinct * from (");
		sql.append("\n select id,orgnum,orgname,isSysUser,neworder,inService from (");
		sql.append("\n select a.id,a.orgnum,a.orgname,1 isSysUser,");
		sql.append("\n case when bb.admin=1 then 0 else 1 end as neworder,bb.inService");
		sql.append("\n from organic a inner join userOrganic bb  on a.orgnum=bb.comid ");
		sql.append("\n inner join userinfo b on bb.userid=b.id ");
		sql.append("\n where 1=1 and bb.enabled='1' ");
		// 团队可用或(团队冻结了且为超级管理人员)
		sql.append("\n and (a.enabled='1' or (a.enabled='0' and bb.admin=1) )");
		this.addSqlWhere(
				email,
				sql,
				args,
				" and (lower(b.email)=? or lower(b.nickname)=? or b.movephone=?)",
				3);
		this.addSqlWhere(passwordMD5, sql, args, " and b.password=?");
		sql.append("\n union all");
		sql.append("\n select a.id,a.orgnum,a.orgname,0 isSysUser,1 neworder,"+ConstantInterface.USER_INSERVICE_NO+" as inService");
		sql.append("\n from organic a inner join jointemp bb  on a.orgnum=bb.comid and bb.joinType=2");
		sql.append("\n where a.enabled='1'");
		this.addSqlWhere( email, sql, args, " and lower(bb.account)=? " );
		this.addSqlWhere(passwordMD5, sql, args, " and bb.passwd=?");
		sql.append("\n and not exists(");
		sql.append("\n select id from userinfo b where bb.account=b.nickname or bb.account=b.email or bb.account=b.movephone");
		sql.append("\n )");
		sql.append("\n union all");
		sql.append("\n select a.id,a.orgnum,a.orgname,0 isSysUser,1 neworder,"+ConstantInterface.USER_INSERVICE_NO+" as inService");
		sql.append("\n from organic a inner join jointemp bb  on a.orgnum=bb.comid and bb.joinType=2");
		sql.append("\n left join userinfo c on (bb.account=c.nickname or bb.account=c.email or bb.account=c.movephone)");
		sql.append("\n where a.enabled='1'");
		this.addSqlWhere(passwordMD5, sql, args, " and bb.passwd=?");
		sql.append("\n and exists(");
		sql.append("\n select id from userinfo b where (bb.account=b.nickname or bb.account=b.email or bb.account=b.movephone)");
		this.addSqlWhere(
				email,
				sql,
				args,
				" and (lower(b.email)=? or lower(b.nickname)=? or b.movephone=?)",
				3);
		sql.append("\n )");
		sql.append("\n )a");
		sql.append("\n )a");
		sql.append("\n order by neworder,inService desc,isSysUser");
		return this.listQuery(sql.toString(), args.toArray(), Organic.class);
	}
	/**
	 * 验证当前操作人员是否是超级管理员
	 * 
	 * @param curUser
	 * @return
	 */
	public UserInfo isAdministrator(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from userinfo a");
		sql.append("\n inner join userOrganic b on a.id=b.userid and b.admin=1");
		sql.append("\n inner join organic c on b.comid=c.orgnum");
		sql.append("\n where a.id=? and c.orgnum=?");
		args.add(curUser.getId());
		args.add(curUser.getComId());
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 模糊查询企业名称
	 * 
	 * @param account
	 * @param searchName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Organic> listSearchOrg(String account, String searchName) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("\n	select a.orgname,a.orgnum,d.username linkerName,case when b.userid is null then '-1' else c.enabled end isSysUser,");
		sql.append("\n	case when f.id is null then 0 else 1 end isApplying,a.recordCreateTime ");
		sql.append("\n	from organic a left join (");
		sql.append("\n		select a.* from (");
		sql.append("\n			select rownum, a.id userId,b.comid from userinfo a");
		sql.append("\n			inner join userorganic b on a.id=b.userid");
		sql.append("\n			where (lower(a.email)='" + account
				+ "' or a.movephone='" + account + "') ");
		sql.append("\n		 and b.enabled=1");
		sql.append("\n		order by a.id asc");
		sql.append("\n		) a where 1=1");
		sql.append("\n	)b on a.orgnum=b.comid");
		sql.append("\n	left join userorganic c on a.orgnum=c.comid and c.admin=1");
		sql.append("\n	left join userinfo d on c.userid=d.id ");
		sql.append("\n	left join joinTemp f on a.orgnum=f.comId and lower(f.account)='"
				+ account + "' and jointype=1");
		sql.append("\n	where a.enabled=1");
		this.addSqlWhereLike(searchName, sql, args, " and a.orgname like ?");
		return this.pagedQuery(sql.toString(), " isSysUser,a.id desc",
				args.toArray(), Organic.class);
	}

	/**
	 * 查询该账号的所有团队
	 * 
	 * @param account
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Organic> listUserAllOrg(String account) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("\n	select c.orgname,c.orgnum,f.username linkerName,a.admin as isSysUser,c.recordCreateTime,");
		sql.append("\n	case when a.admin=0 then '3' else a.admin end newOrder ");
		sql.append("\n	from userorganic a inner join userinfo b on a.userid=b.id");
		sql.append("\n	left join organic c on a.comid=c.orgnum");
		sql.append("\n	left join userorganic d on c.orgnum=d.comid and d.admin='1'");
		sql.append("\n	left join userinfo f on d.userid=f.id");
		sql.append("\n	where (lower(b.email)='" + account
				+ "' or b.movephone='" + account + "')");
		sql.append("\n	and c.enabled=1 and a.enabled=1");
		sql.append("\n	order by newOrder asc");
		return this.listQuery(sql.toString(), args.toArray(), Organic.class);
	}

	/**
	 * 取得团队配置信息
	 * 
	 * @param comId
	 *            团队号
	 * @param cfgType
	 *            配置类型
	 * @return
	 */
	public OrganicCfg getOrganicCfg(Integer comId, String cfgType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from organicCfg a where 1=1");
		this.addSqlWhere(comId, sql, args, "and a.comId=?");
		this.addSqlWhere(cfgType, sql, args, "and a.cfgType=?");
		return (OrganicCfg) this.objectQuery(sql.toString(), args.toArray(),
				OrganicCfg.class);
	}

	/**
	 * 修改团队配置
	 * 
	 * @param sessionUser
	 *            当前操作员
	 * @param organicCfg
	 *            配置信息
	 */
	public void updateOrgCfg(UserInfo sessionUser, OrganicCfg organicCfg) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update organicCfg set cfgValue=? where comId=? and cfgType=?");
		args.add(organicCfg.getCfgValue());
		args.add(sessionUser.getComId());
		args.add(organicCfg.getCfgType());
		Integer result = this.excuteSql(sql.toString(), args.toArray());
		if (result == 0) {
			organicCfg.setComId(sessionUser.getComId());
			this.add(organicCfg);
		}

	}

	/**
	 * 取得团队的配置信息
	 * 
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OrganicCfg> listOrgCfg(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from organicCfg a where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), OrganicCfg.class);
	}

	/**
	 * 分页查询 所有企业信息
	 * 
	 * @param organic
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Organic> listPagedOranic(Organic organic) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.orgNum,b.orgName,b.recordcreatetime,b.enabled,");
		sql.append("\n a.username as linkerName,a.id as userId,a.movephone as linkerMovePhone");
		sql.append("\n from userinfo a inner join userOrganic aa on a.Id=aa.userId");
		sql.append("\n inner join organic b on aa.comId=b.orgNum");
		sql.append("\n where 1=1 AND aa.admin=1");
		
		this.addSqlWhereLike(organic.getOrgName(), sql, args, " and b.orgName like ?");
		this.addSqlWhere(organic.getEnabled(), sql, args, " and b.enabled = ?");
		
		//查询创建时间段
		this.addSqlWhere(organic.getStartDate(), sql, args, " and substr(b.recordcreatetime,0,10)>=?");
		this.addSqlWhere(organic.getEndDate(), sql, args, " and substr(b.recordcreatetime,0,10)<=?");
		
		return this.pagedQuery(sql.toString(), " b.recordcreatetime desc ", args.toArray(), Organic.class);
	}
	/**
	 * 查询所有的团队
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Organic> listAllOrganic() {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.* from organic b");
		return this.listQuery(sql.toString(), args.toArray(), Organic.class);
	}

	/**
	 * 获取超出服务限制的团队信息集合
	 */
	@SuppressWarnings("unchecked")
	public List<Organic> listOrgOutOfService() {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select finalResult.orgnum,finalResult.orgname");
		sql.append("\n from (");
		sql.append("\n select orgMember.orgnum,orgMember.orgname,orgMember.usersNum,count(orgMember.userid) as members from (");
		sql.append("\n select a.orgnum,a.orgname,b.userid,");
		sql.append("\n case when orgSpace.Usersnum is null then ? else orgSpace.Usersnum end as usersNum");
		args.add(ConstantInterface.ORG_DEFAULT_MEMBERS);//默认允许使用的用户数
		sql.append("\n from organic a");
		sql.append("\n inner join userOrganic b on a.orgnum = b.comid and b.enabled=1 and b.inService=1");
		sql.append("\n left join organicSpaceCfg orgSpace on a.orgnum=orgSpace.comid and orgSpace.Enddate >= to_char(sysdate,'yyyy-MM-dd')");
		sql.append("\n ) orgMember group by orgMember.orgnum,orgMember.orgname,orgMember.Usersnum");
		sql.append("\n )finalResult where finalResult.members>finalResult.usersNum");
		return this.listQuery(sql.toString(), args.toArray(), Organic.class);
	}

	/**
	 * 根据加入团队时间降序禁用到平台默认允许使用人数
	 * @param orgNum 团队主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserOrganic> listUserOrganic(Integer orgNum) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select finalResult.id from (");
		sql.append("\n select rowSelect.id,rowSelect.recordCreateTime,rowSelect.userName,rownum as orderNum from");
		sql.append("\n (");
		sql.append("\n select aa.id,aa.recordCreateTime,case when a.userName is null then a.email else a.userName end as userName,rownum as orderNum");
		sql.append("\n from  userOrganic aa");
		sql.append("\n inner join  userinfo a on aa.userid=a.id");
		sql.append("\n inner join organic b on aa.comId = b.orgNum");
		sql.append("\n where 1=1  and aa.enabled='1'");
		sql.append("\n and ((b.enabled='0' and aa.admin=1) or  b.enabled='1')  and aa.comId=?");
		args.add(orgNum);
		sql.append("\n order by recordCreateTime,a.id asc");
		sql.append("\n ) rowSelect");
		sql.append("\n ) finalResult where finalResult.orderNum >"+ConstantInterface.ORG_DEFAULT_MEMBERS);
		return this.listQuery(sql.toString(), args.toArray(),UserOrganic.class);
	}

	/**
	 * 根据加入团队时间降序禁用到平台默认允许使用人数
	 * @param orgNum 团队主键
	 */
	public void updateOrgMemberToUseless(Integer orgNum) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update userOrganic a set a.inService=? where a.comid=? and a.id in");
		args.add(ConstantInterface.USER_INSERVICE_NO);
		args.add(orgNum);
		sql.append("\n (");
		sql.append("\n select finalResult.id from (");
		sql.append("\n select rowSelect.id,rowSelect.recordCreateTime,rowSelect.userName,rownum as orderNum from");
		sql.append("\n (");
		sql.append("\n select aa.id,aa.recordCreateTime,case when a.userName is null then a.email else a.userName end as userName,rownum as orderNum");
		sql.append("\n from  userOrganic aa");
		sql.append("\n inner join  userinfo a on aa.userid=a.id");
		sql.append("\n inner join organic b on aa.comId = b.orgNum");
		sql.append("\n where 1=1 and aa.admin<>1 and aa.enabled=? and aa.inService=?");
		args.add(ConstantInterface.ENABLED_YES);
		args.add(ConstantInterface.USER_INSERVICE_YES);
		sql.append("\n and b.enabled=? and aa.comId=?");
		args.add(ConstantInterface.ENABLED_YES);
		args.add(orgNum);
		sql.append("\n order by recordCreateTime,a.id asc");
		sql.append("\n ) rowSelect");
		sql.append("\n ) finalResult where finalResult.orderNum >="+ConstantInterface.ORG_DEFAULT_MEMBERS);
		sql.append("\n )");
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 检测团队是否超出服务范围
	 * @param comId 团队主键
	 * @return
	 */
	public Organic orgOutOfService(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select finalResult.orgnum,finalResult.orgname");
		sql.append("\n from (");
		sql.append("\n select orgMember.orgnum,orgMember.orgname,orgMember.usersNum,count(orgMember.userid) as members from (");
		sql.append("\n select a.orgnum,a.orgname,b.userid,");
		sql.append("\n case when orgSpace.Usersnum is null then ? else orgSpace.Usersnum end as usersNum");
		args.add(ConstantInterface.ORG_DEFAULT_MEMBERS);//默认允许使用的用户数
		sql.append("\n from organic a");
		sql.append("\n inner join userOrganic b on a.orgnum = b.comid and b.enabled=1 and b.inService=1");
		sql.append("\n left join organicSpaceCfg orgSpace on a.orgnum=orgSpace.comid and orgSpace.Enddate >= to_char(sysdate,'yyyy-MM-dd')");
		sql.append("\n ) orgMember group by orgMember.orgnum,orgMember.orgname,orgMember.Usersnum");
		sql.append("\n )finalResult where finalResult.members>finalResult.usersNum and finalResult.orgnum = ?");
		args.add(comId);
		return (Organic) this.objectQuery(sql.toString(), args.toArray(), Organic.class);
	}

	/**
	 * 获取团队购买服务信息
	 * @param comId 团队主键
	 * @return
	 */
	public OrganicSpaceCfg getOrganicSpaceCfgInfo(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.orgname,b.recordcreatetime,b.usersnum,b.storagespace,b.startdate,b.enddate,c.transactionmoney,");
		sql.append("\n case when b.enddate>=to_char(sysdate,'yyyy-mm-dd') then 1 else 0 end as orgServiceStatus,");
		sql.append("\n ceil(c.productnum*c.originalPrice*orgUpgrade.years*c.discount) as orderCost");//实际订单金额
		sql.append("\n from organic a");
		sql.append("\n inner join organicSpaceCfg b on a.orgnum=b.comid");
		sql.append("\n inner join orders c on b.comid=c.comid and b.orderid=c.id");
		sql.append("\n left join orgUpgrade orgUpgrade on c.id = orgUpgrade.Orderid and c.comid = orgUpgrade.Comid");
		sql.append("\n where a.orgnum=?");
		args.add(comId);
		return (OrganicSpaceCfg)this.objectQuery(sql.toString(), args.toArray(),OrganicSpaceCfg.class);
	}

	/**
	 * 统计团队下人员启用状态为启用的人员数
	 * @param comId 团队主键
	 * @return
	 */
	public Organic countOrgEnabledUsersNum(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select count(u.id) as members");
		sql.append("\n  from userinfo u ");
		sql.append("\n  inner join userOrganic org on u.id=org.userid");
		sql.append("\n  where org.enabled=1 and org.comid=? ");
		args.add(comId);
		return (Organic)this.objectQuery(sql.toString(), args.toArray(),Organic.class);
	}
	
	/**
	 * 激活团队成员InService为1
	 * @param orgNum 团队主键
	 * @param usersNum 可以使用人数范围
	 */
	public void updateOrgMemberToInService(Integer orgNum,Integer usersNum) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update userOrganic a set a.inService=? where a.id in");
		args.add(ConstantInterface.USER_INSERVICE_YES);
		sql.append("\n (");
		sql.append("\n select finalResult.id from (");
		sql.append("\n select rowSelect.id,rowSelect.recordCreateTime,rowSelect.userName,rownum as orderNum from");
		sql.append("\n (");
		sql.append("\n select aa.id,aa.recordCreateTime,case when a.userName is null then a.email else a.userName end as userName,rownum as orderNum");
		sql.append("\n from  userOrganic aa");
		sql.append("\n inner join  userinfo a on aa.userid=a.id");
		sql.append("\n inner join organic b on aa.comId = b.orgNum");
		sql.append("\n where 1=1  and aa.enabled=?");
		args.add(ConstantInterface.ENABLED_YES);
		sql.append("\n and b.enabled=? and aa.comId=?");
		args.add(ConstantInterface.ENABLED_YES);
		args.add(orgNum);
		sql.append("\n order by recordCreateTime,a.id asc");
		sql.append("\n ) rowSelect");
		sql.append("\n ) finalResult where finalResult.orderNum <= ?");
		args.add(usersNum);
		sql.append("\n )");
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 按模块删除数据表
	 * @param tableName
	 */
	public List<JSONObject> listTableInfo(String tableName) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.TABLE_NAME tableName");
		sql.append("\n from all_constraints p, all_constraints c");
		sql.append("\n  where p.table_name = '"+tableName+"'");
		sql.append("\n and p.OWNER = SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA')");
		sql.append("\n  and c.OWNER=SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA')");
		sql.append("\n  and c.constraint_type = 'R'");
		sql.append("\n and p.CONSTRAINT_NAME = c.R_CONSTRAINT_NAME ");
		return this.listQueryJSON(sql.toString(), args.toArray());
	}

}
