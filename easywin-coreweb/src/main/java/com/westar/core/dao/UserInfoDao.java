package com.westar.core.dao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.westar.base.model.Department;
import com.westar.base.model.ForMeDo;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.JoinRecord;
import com.westar.base.model.Organic;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UsedUser;
import com.westar.base.model.UserInfo;
import com.westar.base.model.UserOrganic;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;

@Repository
public class UserInfoDao extends BaseDao {

	/**
	 * 查询用户分页列表  分页查询
	 * @param userInfo
	 * @return 人员信息集合
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPagedUserInfo(UserInfo userInfo) {
		String nowtime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		/* 40秒前更新过在线时间的表示在线 */
		String cxtime = DateTimeUtil.addDate(nowtime, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.SECOND, -40);
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n a.allSpelling,a.firstSpelling,a.password,a.linePhone,a.movePhone,");
		sql.append("\n a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n a.birthday,a.nickname,aa.comId,aa.admin,aa.job,aa.depId,aa.enabled,aa.lastOnlineTime,d.depName,");
		sql.append("\n aa.jifenScore,aa.id as userOrganicId ,b.orgName,c.uuid as smImgUuid,c.fileName as smImgName,");
		sql.append("\n case when aa.admin=0 then 0 when aa.admin=1 then 2 else 1 end as neworder,aa.inservice,aa.enrollNumber,");
		//当没有角色时默认为普通人员
		sql.append("\n case when binding.roles is null then '普通人员' else binding.roles end as roles");
		sql.append("\n from userinfo a inner join userOrganic aa on a.Id=aa.userId");
		sql.append("\n inner join organic b on aa.comId=b.orgNum");
		sql.append("\n left join upfiles c on aa.smallHeadPortrait=c.id and c.comId=aa.comId");
		sql.append("\n left join department d on aa.depId=d.id and d.comId=aa.comId");
		sql.append("\n left join (select a.userId,wm_concat(b.roleName) as roles from roleBindingUser a left join role b on a.roleId = b.id group by a.userId) binding on a.id = binding.userId");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and aa.comId=?");
		if(null!=userInfo.getUserName()){
			this.addSqlWhereLike(userInfo.getUserName().toLowerCase(), sql, args, "and (lower(a.email) like ? or lower(a.userName) like ? or lower(a.allSpelling) like ? or lower(a.firstSpelling) like ?)",4);
		}
		// 1表示查询在线的
		if (!StringUtil.isBlank(userInfo.getIfOnline())) {
			sql.append("\n and aa.lastOnlineTime>?");
			args.add(cxtime);
		}
		return this.pagedQuery(sql.toString()," neworder desc,aa.enabled desc,aa.inservice desc,aa.id", args.toArray(), UserInfo.class);
	}
	
	/**
	 * 查询用户分页列表  不分页
	 * @param userInfo
	 * @return 人员信息集合
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUserInfo(UserInfo userInfo) {
		String nowtime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		/* 40秒前更新过在线时间的表示在线 */
		String cxtime = DateTimeUtil.addDate(nowtime, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.SECOND, -40);
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n a.allSpelling,a.firstSpelling,a.password,a.linePhone,a.movePhone,");
		sql.append("\n a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n a.birthday,a.nickname,aa.comId,aa.admin,aa.job,aa.depId,aa.enabled,aa.lastOnlineTime,d.depName,");
		sql.append("\n aa.jifenScore,aa.id as userOrganicId ,b.orgName,c.uuid as smImgUuid,c.fileName as smImgName,");
		sql.append("\n case when aa.admin=0 then 0 when aa.admin=1 then 2 else 1 end as neworder,aa.inservice,aa.enrollNumber ");
		sql.append("\n from userinfo a inner join userOrganic aa on a.Id=aa.userId");
		sql.append("\n inner join organic b on aa.comId=b.orgNum");
		sql.append("\n left join upfiles c on aa.smallHeadPortrait=c.id and c.comId=aa.comId");
		sql.append("\n left join department d on aa.depId=d.id and d.comId=aa.comId");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and aa.comId=?");
		if(null!=userInfo.getUserName()){
			this.addSqlWhereLike(userInfo.getUserName().toLowerCase(), sql, args, "and (lower(a.email) like ? or lower(a.userName) like ? or lower(a.allSpelling) like ? or lower(a.firstSpelling) like ?)",4);
		}
		// 1表示查询在线的
		if (!StringUtil.isBlank(userInfo.getIfOnline())) {
			sql.append("\n and aa.lastOnlineTime>?");
			args.add(cxtime);
		}
		sql.append("\n order by neworder desc,aa.enabled desc,aa.inservice desc,aa.id");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 查询当前在线人数
	 * @return 在线人数
	 */
	public int countOnlineUser() {
		int n = 0;
		String nowtime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		/* 40秒前更新过在线时间的表示在线 */
		String cxtime = DateTimeUtil.addDate(nowtime, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.SECOND, -40);
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(a.id)");
		sql.append("\n from userInfo a ");
		sql.append("\n where 1=1");
		sql.append("\n and a.lastonlinetime>?");
		args.add(cxtime);
		n = this.countQuery(sql.toString(), args.toArray());
		return n;
	}

	/**
	 * 查询人员信息  人员选择树
	 * @param userInfo
	 * @return 人员信息集合
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUser(UserInfo userInfo) {
		List<UserInfo> list = null;
//		String nowtime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		/* 40秒前更新过在线时间的表示在线 */
//		String cxtime = DateTimeUtil.addDate(nowtime, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.SECOND, -40);
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.depname as depName,c.uuid as smImgUuid,c.filename as smImgName,");
		sql.append("\n formedoUser.Id as formedoUserId,formedoUser.Username as formedoUserName ");
		sql.append("\n from userinfo a");
		sql.append("\n inner join userOrganic aa on aa.userid = a.id ");
		sql.append("\n inner join department b on aa.depid = b.id and aa.comId = b.comId ");
		sql.append("\n left join upfiles c on aa.comId = c.comId and aa.smallheadportrait = c.id");
		sql.append("\n left join formedo on a.id=formedo.creator");
		sql.append("\n left join userOrganic formedoOrg on formedo.comId=formedoOrg.Comid and formedo.userid=formedoOrg.Userid and formedoOrg.Enabled=1");
		sql.append("\n left join userinfo formedoUser on formedoOrg.userid=formedoUser.Id");
		sql.append("\n where aa.comId = ? and aa.enabled=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getEnabled());
		if(null!=userInfo.getAnyNameLike()){
			this.addSqlWhereLike(userInfo.getAnyNameLike().toLowerCase(), sql, args, "\n and (lower(a.userName) like ? or lower(a.allSpelling) like ? or lower(a.firstSpelling) like ?)", 3);
		}
		if(userInfo.getAnyDepId() != null){
			sql.append("\n and aa.depid in (");
			sql.append("\n select  a.id from department a where a.comId = ? ");
			args.add(userInfo.getComId());
			sql.append("\n start with a.id =? connect by prior a.id = a.parentid");
			args.add(userInfo.getAnyDepId());
			sql.append("\n )");
		}
		String onlySubState = userInfo.getOnlySubState();
		if(StringUtils.isNotEmpty(onlySubState) && onlySubState.equals("1")){
			sql.append("\n and exists (");
			sql.append("\n select id from myLeaders where creator = a.id and leader = ? and comId = ? and creator <> leader ");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append("\n )");
		}
		sql.append(" order by a.id asc");
		list = this.listQuery(sql.toString(),args.toArray(), UserInfo.class);
		return list;
	}

	/**
	 * 查询用户信息详细  查询详细信息
	 * @param id 所查询的人员表的ID
	 * @return 人员信息
	 */
	public UserInfo getUserInfo(Integer comId,Integer id) {
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,aa.recordCreateTime hireDate,aa.defShowGrpId,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n a.allSpelling,a.firstSpelling,a.password,a.linePhone,a.movePhone,a.selfIntr,");
		sql.append("\n case when uConf.openState is null then 1 else uConf.openState end autoEject,");
		sql.append("\n a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n a.birthday,a.nickname,aa.comId,aa.job,aa.depId,aa.admin,aa.enabled,aa.lastOnlineTime,aa.isChief,");
		sql.append("\n aa.jifenScore,b.uuid as bigImgUuid,b.filename as bigImgName,e.orgName,");
		sql.append("\n b.uuid as bigImgUuid,b.filename as bigImgName,");
		sql.append("\n c.uuid as midImgUuid,c.filename as midImgName,");
		sql.append("\n d.uuid as smImgUuid,d.filename as smImgName,");
		sql.append("\n f.uuid as logoUuid,f.filename as logoName,g.depName,");
		
		sql.append("\n (select levname from jifenLev where levMinScore=(");
		sql.append("\n select max(levMinScore) from jifenLev where levMinScore<=aa.jifenScore)) as levName,");
		
		sql.append("\n (select levMinScore from jifenLev where levMinScore=(");
		sql.append("\n select min(levMinScore) from jifenLev where levMinScore>aa.jifenScore)) as nextLevJifen, ");
		
		sql.append("\n (select levMinScore from jifenLev where levMinScore=(");
		sql.append("\n select max(levMinScore) from jifenLev where levMinScore<=aa.jifenScore)) as minLevJifen,");
		
		sql.append("\n userLastLogin.recordcreatetime as lastLoginTime,userLoginTimes.times as loginTimes,aa.enrollNumber");
		
		sql.append("\n from  userOrganic aa inner join  userinfo a on aa.userid=a.id");
		sql.append("\n left join userConf uConf on uConf.comId= aa.comId and uConf.userId=aa.userId and uConf.sysConfCode='1' ");
		sql.append("\n left join upfiles b on aa.bigheadportrait=b.id");
		sql.append("\n left join upfiles c on aa.mediumHeadPortrait=c.id");
		sql.append("\n left join upfiles d on aa.smallHeadPortrait=d.id ");
		sql.append("\n left join organic e on aa.comId=e.orgNum ");
		sql.append("\n left join upfiles f on e.logo=f.id ");
		sql.append("\n left join department g on g.id=aa.depId and g.comId=aa.comId ");
		//增加个人上次登录时间以及登录次数查询
		sql.append("\n left join (");
		sql.append("\n select * from (");
		sql.append("\n select a.id,a.recordcreatetime||','||a.content as recordcreatetime,a.comId,a.userid, ROW_NUMBER() OVER (ORDER BY a.id desc) R");
		sql.append("\n from systemLog a where a.comId=? and a.userid=?)");
		sql.append("\n where R = 2 ) userLastLogin on aa.comId=userLastLogin.comId and a.id=userLastLogin.userid");
		sql.append("\n left join (");
		sql.append("\n select a.comId,a.userid,count(*) as times from systemLog a group by a.comId,a.userid");
		sql.append("\n ) userLoginTimes on aa.comId=userLoginTimes.comId and a.id=userLoginTimes.userid");
		sql.append("\n where 1=1 and aa.comId=? and a.id=?");
		return (UserInfo) this.objectQuery(sql.toString(), new Object[] {comId,id,comId,id}, UserInfo.class);
	}

	/**
	 * 验证用户账号唯一性
	 * @param loginName 登录名
	 * @return 账号唯一返回true 
	 */
	public boolean validateLoginName(String loginName) {
		int count = this.countQuery("select count(id) from userinfo where loginName=?", new Object[] { loginName });
		if (count > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 根据账号和密码查找用户
	 * @param loginName 登录名
	 * @param password  密码
	 * @param comId 
	 * @return 人员信息
	 */
	public UserInfo userAuth(String loginName, String password, String comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from ");//人员账号比对
		sql.append("\n (");
//		sql.append("\n select * from");//团队空间允许人数筛选
//		sql.append("\n (");
		sql.append("\n select a.id,aa.recordCreateTime hireDate,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n case when uConf.openState is null then 1 else uConf.openState end autoEject,");
		sql.append("\n a.allSpelling,a.firstSpelling,a.password,a.linePhone,a.movePhone,a.selfIntr,");
		sql.append("\n a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n a.birthday,a.nickname,aa.comId,aa.job,aa.depId,aa.admin,aa.enabled,aa.lastOnlineTime,aa.ischief,");
		sql.append("\n aa.jifenScore,aa.alterInfo,b.orgName ,h.depName,aa.inService,");
		sql.append("\n e.uuid as smImgUuid,e.filename as smImgName,");
		sql.append("\n g.uuid as logoUuid,g.filename as logoName,");
		sql.append("\n (select levname from jifenLev where levMinScore=(");
		sql.append("\n select max(levMinScore) from jifenLev where levMinScore<=aa.jifenScore)) as levName,");
		sql.append("\n (select levMinScore from jifenLev where levMinScore=(");
		sql.append("\n select min(levMinScore) from jifenLev where levMinScore>aa.jifenScore)) as nextLevJifen,");
		sql.append("\n (select levMinScore from jifenLev where levMinScore=(");
		sql.append("\n select max(levMinScore) from jifenLev where levMinScore<=aa.jifenScore)) as minLevJifen,");
		sql.append("\n case when orgSpace.Usersnum is null then "+ConstantInterface.ORG_DEFAULT_MEMBERS+" else orgSpace.Usersnum end as usersNum");
		sql.append("\n from  userOrganic aa");
		sql.append("\n inner join  userinfo a on aa.userid=a.id");
		sql.append("\n inner join organic b on aa.comId = b.orgNum");
		sql.append("\n left join userConf uConf on uConf.comId= aa.comId and uConf.userId=aa.userId and uConf.sysConfCode='1'");
		sql.append("\n left join upfiles e on aa.smallHeadPortrait=e.id");
		sql.append("\n left join organic f on aa.comId=f.orgNum");
		sql.append("\n left join upfiles g on f.logo=g.id");
		sql.append("\n left join department h on aa.depId=h.id");
		sql.append("\n left join organicSpaceCfg orgSpace on aa.comId = orgSpace.Comid and orgSpace.Enddate >= to_char(sysdate,'yyyy-MM-dd')");
		sql.append("\n where 1=1  and aa.enabled='1' ");
		sql.append("\n and ((b.enabled='0' and aa.admin=1) or  b.enabled='1')  and aa.comId=?");
		args.add(comId);
		sql.append("\n order by hireDate,a.id asc");
//		sql.append("\n ) rowSelect where rownum <=rowSelect.usersNum");
		sql.append("\n ) userSelect where (lower(userSelect.email)=? or lower(userSelect.nickname)=? or userSelect.movephone=?)");
		args.add(loginName);
		args.add(loginName);
		args.add(loginName);
		sql.append("\n and userSelect.password=?");
		args.add(password);
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 根据部门id批量删除用户信息
	 * @param orgId  用户所属机构ID
	 */
	public void delUserInfoByOrgId(Integer orgId) {
		this.delByField("userinfo", "orgId", orgId);
	}

	/**
	 * 查询指定私有组包含的人员
	 * @param groupId 部门私有组主键id
	 * @return 人员集合
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUserByGroup(Integer groupId) {
		String nowtime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		/* 40秒前更新过在线时间的表示在线 */
		String cxtime = DateTimeUtil.addDate(nowtime, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.SECOND, -40);
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.userName ");
		sql.append("\n ,case when a.lastOnlineTime>'" + cxtime + "' then 1 else 0 end ifOnline");
		sql.append("\n from userInfo a");
		sql.append("\n left join userGroupItem b on a.id=b.userid");
		sql.append("\n where b.groupId=?");
		args.add(groupId);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 新企业用户注册主键验证
	 * @param key
	 * @return
	 */
	public boolean orgKeyCheck(Integer key){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from organic \n");
		sql.append("where orgNum=? \n");
		args.add(key);
		Organic organic = (Organic)this.objectQuery(sql.toString(), args.toArray(),Organic.class);
		if(null!=organic){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 修改密码前验证密码
	 * @param id
	 * @param passwordMD5
	 * @return
	 */
	public boolean validatePassword(String id, String passwordMD5) {
		StringBuffer sql = new StringBuffer("select * from userinfo \n");
		sql.append("\n where id=? and password=?");
		//查询有结果则密码正确，否则密码错误
		return this.objectQuery(sql.toString(), new Object[]{id,passwordMD5}, UserInfo.class)==null?false:true;
	}

	/**
	 * 查询部门的用户列表
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPagedUserForDep(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n a.allSpelling,a.firstSpelling,a.password,a.linePhone,a.movePhone,");
		sql.append("\n a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n a.birthday,a.nickname,aa.comId,aa.job,aa.depId,aa.admin,aa.enabled,aa.lastOnlineTime,");
		sql.append("\n aa.jifenScore,b.depname, ");
		sql.append("\n case when aa.depid="+userInfo.getDepId()+" then 0 else 1 end as neworder");
		sql.append("\n from userinfo a inner join userOrganic aa on a.id=aa.userId");
		sql.append("\n left join department b on aa.depid=b.id and aa.comId=b.comId");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and aa.comId=?");
		if(null!=userInfo.getCondition()){
			this.addSqlWhereLike(userInfo.getCondition().toLowerCase(), sql, args, " and (lower(a.email) like ? or lower(a.userName) like ? or lower(a.allSpelling) like ? or lower(a.firstSpelling) like ?)",4);
		}
		return this.pagedQuery(sql.toString(), " neworder,aa.depid,aa.enabled desc,a.id", args.toArray(), UserInfo.class);
	}
	/**
	 * 查询分组的用户列表
	 * @param userInfo
	 * @param grpId 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPagedUserForGrp(UserInfo userInfo, String grpId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sqlCount = new StringBuffer("select count(*) from (");
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n a.allSpelling,a.firstSpelling,a.password,a.linePhone,a.movePhone,f.uuid as smImgUuid,f.filename as smImgName,");
		sql.append("\n a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n a.birthday,a.nickname,aa.comId,aa.job,aa.depId,aa.admin,aa.enabled,aa.lastOnlineTime,");
		sql.append("\n aa.jifenScore,c.depName, ");
		sql.append("\n case when a.id in (select b.userinfoid from groupPersion b where b.grpid="+grpId+") then  ");
		sql.append("\n case when a.id = "+userInfo.getId()+" then 1 else 0 end else ");
		sql.append("\n case when a.id = "+userInfo.getId()+" then 1 else 2 end end as neworder");
		sql.append("\n from userinfo a");
		sql.append("\n inner join userOrganic aa on a.id=aa.userId and aa.enabled =1");
		sql.append("\n left join department c on aa.depid=c.id and aa.comId=c.comId");
		sql.append("\n left join upfiles f on aa.comId = f.comId and aa.smallheadportrait = f.id");
		sql.append("\n where 1=1 and aa.enabled='1'");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and aa.comId=?");
		if(null!=userInfo.getCondition()){
			this.addSqlWhereLike(userInfo.getCondition().toLowerCase(), sql, args, " and (lower(a.email) like ? or lower(a.userName) like ? or lower(a.allSpelling) like ? or lower(a.firstSpelling) like ?)",4);
		}
		sqlCount.append(sql);
		sqlCount.append(" )a where 1=1");
		return this.pagedQuery(sql.toString(), sqlCount.toString()," neworder,aa.depid,a.id", args.toArray(), UserInfo.class);
	}

	/**
	 * 分页查询部门的成员
	 * @param depId 部门主键
	 * @param comId 企业编号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPagedDepUser(Integer depId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n a.allSpelling,a.firstSpelling,a.password,a.linePhone,a.movePhone,");
		sql.append("\n a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n a.birthday,a.nickname,aa.comId,aa.job,aa.depId,aa.admin,aa.enabled,aa.lastOnlineTime,");
		sql.append("\n aa.jifenScore");
		sql.append("\n from userinfo a inner join userOrganic aa on a.id=aa.userId");
		sql.append("\n where 1=1 and aa.enabled='1'");
		this.addSqlWhere(comId, sql, args, " and aa.comId=?");
		this.addSqlWhere(depId, sql, args, " and aa.depid=?");
		return this.pagedQuery(sql.toString(), " a.id", args.toArray(), UserInfo.class);
	}
	/**
	 * 部门的成员
	 * @param depId 部门主键
	 * @param comId 企业编号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listDepUser(Integer depId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n a.allSpelling,a.firstSpelling,a.password,a.linePhone,a.movePhone,");
		sql.append("\n a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n a.birthday,a.nickname,aa.comId,aa.job,aa.depId,aa.admin,aa.enabled,aa.lastOnlineTime,aa.enabled,");
		sql.append("\n aa.jifenScore");
		sql.append("\n from userinfo a inner join userOrganic aa on a.id=aa.userId");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and aa.comId=?");
		this.addSqlWhere(depId, sql, args, " and aa.depid=?");
		sql.append("\n order by aa.enabled desc,a.id asc");
		
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 取得在职用户信息
	 * @param depId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listEnabledUser(Integer depId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n a.allSpelling,a.firstSpelling,a.password,a.linePhone,a.movePhone,");
		sql.append("\n a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n a.birthday,a.nickname,aa.comId,aa.job,aa.depId,aa.admin,aa.enabled,aa.lastOnlineTime,aa.enabled,");
		sql.append("\n aa.jifenScore");
		sql.append("\n from userinfo a inner join userOrganic aa on a.id=aa.userId");
		sql.append("\n where aa.enabled=1 ");
		this.addSqlWhere(comId, sql, args, " and aa.comId=?");
		this.addSqlWhere(depId, sql, args, " and aa.depid=?");
		sql.append("\n order by aa.enabled desc,a.id asc");
		
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 根据人员主键owner获取所属组群
	 * @param comId
	 * @param owner
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> listSelfGroup(Integer comId,Integer owner){
		StringBuffer sql = new StringBuffer("select a.* from selfgroup a where a.comId=? and a.owner=? order by a.id desc ");
		return this.listQuery(sql.toString(), new Object[]{comId,owner}, SelfGroup.class);
	}
	
	/**
	 * 验证当前邮箱是否设置密码，用于统一密码
	 * @param account
	 * @return
	 */
	public UserInfo getUserInfoByAccount(String account) {
		StringBuffer sql = new StringBuffer("select * from (");
		sql.append("\n select rownum, a.* from userinfo a ");
		sql.append("\n where (lower(a.email)=? or lower(a.nickname)=? or a.movephone=?) order by id asc");
		sql.append("\n ) a where rownum=1");
		return (UserInfo) this.objectQuery(sql.toString(), new Object[]{account,account,account}, UserInfo.class);
	}

	/**
	 * 待审核
	 * @param userInfo
	 * @param joinType 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JoinRecord> listPagedForJoin(JoinRecord joinRecord) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from joinrecord a");
		sql.append("\n where 1=1");
		this.addSqlWhere(joinRecord.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(joinRecord.getJoinType(), sql, args, " and a.joinType=?");
		this.addSqlWhere(joinRecord.getCheckState(), sql, args, " and a.checkState=?");
 		return this.pagedQuery(sql.toString(),"a.checkState,a.id desc", args.toArray(), JoinRecord.class);
	}

	/**
	 * 链接的有效性判断
	 * @param confirmId
	 * @return
	 */
	public JoinRecord justConfirmId(String confirmId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select  a.*,b.orgname as comName from JoinRecord a");
		sql.append("\n left join organic b on a.comId=b.Orgnum ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(confirmId, sql, args, " and a.confirmId=?");
		return (JoinRecord) this.objectQuery(sql.toString(), args.toArray(), JoinRecord.class);
	}

	/**
	 *  取得公司的唯一账号
	 * @param account
	 * @param comId
	 * @return
	 */
	public UserInfo getUserInfoByType(String account, Integer comId,String type) {
		List<Object> args = new ArrayList<Object>();
		
		StringBuffer sql = new StringBuffer("select * from (");
		sql.append("\n select a.*,b.comId,b.enabled from userinfo a inner join userorganic b on a.id=b.userid");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and  b.comId=?");
		if(type.equals(ConstantInterface.GET_BY_EMAIL)){
			this.addSqlWhere(account, sql, args, " and lower(a.email)=?");
		}else if(type.equals(ConstantInterface.GET_BY_PHONE)){
			this.addSqlWhere(account, sql, args, " and lower(a.MOVEPHONE)=?");
		}
		sql.append("\n ) a where rownum=1");
		
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 修改密码
	 * @param userInfo
	 */
	public void updatePassword(UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		sql.append("\n update userinfo set password=:password where id=:id");
		this.update(sql.toString(),userInfo);
		
	}

	/**
	 * 人员邀请记录
	 * @param joinRecord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JoinRecord> listPagedInvUser(JoinRecord joinRecord) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.* from joinrecord a where a.jointype='2'");
		this.addSqlWhere(joinRecord.getComId(), sql, args, "and  a.comId=? ");
		return this.pagedQuery(sql.toString(), " a.state asc,a.id desc", args.toArray(), JoinRecord.class);
	}

	/**
	 * 上次使用的分组
	 * @param comId 企业编号
	 * @param userId 操作员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UsedGroup> listUsedGroup(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,b.grpName from usedGroup a ");
		sql.append("\n left join selfGroup b on a.comId=b.comId and b.owner=a.userId and a.grpId=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		sql.append("\n order by a.id asc ");
		return this.listQuery(sql.toString(), args.toArray(), UsedGroup.class);
	}
	/**
	 * 获取人员在当前组织下所在的所有分组
	 * @param comId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> listSelfGroupUserIn(Integer comId,Integer userId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.grpname from selfGroup a \n");
		sql.append("inner join groupPersion b on a.comId = b.comId and a.id = b.grpid \n");
		sql.append("where a.comId = ? and b.userinfoid = ? \n");
		args.add(comId);
		args.add(userId);
		return this.listQuery(sql.toString(), args.toArray(), SelfGroup.class);
	}

	/**
	 * 职位变动
	 * @param comId 企业编号
	 * @param userId用户主键
	 * @param job 职位
	 */
	public void updateUserOrganic(Integer comId, Integer userId, String job) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userOrganic set job=? where comId=? and  userId=?");
		args.add(job);
		args.add(comId);
		args.add(userId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 验证用户登陆信息
	 * @param email
	 * @param passwordMD5
	 * @return
	 */
	public UserInfo checkUserInfo(String email, String passwordMD5) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from (");
		sql.append("select rownum,a.* from userinfo a where 1=1 ");
		this.addSqlWhere(email, sql, args, " and lower(a.email)=? ");
		this.addSqlWhere(passwordMD5, sql, args, " and a.password=?");
		sql.append("\n ) a where rownum=1");
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 修改头像
	 * @param comId 企业编号
	 * @param userId 用户主键
	 * @param bigHeadPortrait 大头像
	 * @param mediumHeadPortrait 中头像
	 * @param smallHeadPortrait 小头像
	 */
	public void updateUserOrganicImg(Integer comId, Integer userId,
			Integer bigHeadPortrait, Integer mediumHeadPortrait,
			Integer smallHeadPortrait) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userOrganic set bigHeadPortrait=?,mediumHeadPortrait=?,smallHeadPortrait=? where comId=? and  userId=?");
		args.add(bigHeadPortrait);
		args.add(mediumHeadPortrait);
		args.add(smallHeadPortrait);
		args.add(comId);
		args.add(userId);
		this.excuteSql(sql.toString(), args.toArray());
		
	}

	/**
	 * 取得注册用户最近注册的信息
	 * @param email
	 * @return
	 */
	public JoinRecord getConfirmIdForRegist(String email) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select* from (");
		sql.append("\n select rownum,a.* from JoinRecord a ");
		sql.append("\n where a.state<>1 and a.joinType=0 ");
		this.addSqlWhere(email, sql, args, " and lower(a.email)=?");
		sql.append("\n ) a where rownum=1");
		return (JoinRecord) this.objectQuery(sql.toString(), args.toArray(), JoinRecord.class);
	}

	/**
	 * 修改在线改时间
	 * @param userOrganic
	 */
	public void updateLastOnLineTime(UserOrganic userOrganic) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userOrganic set lastOnlineTime=? where comId=? and  userId=?");
		args.add(userOrganic.getLastOnlineTime());
		args.add(userOrganic.getComId());
		args.add(userOrganic.getUserId());
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 统计需要你查看的模块数据集合
	 * @param comId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TodayWorks> listTodayWorks(Integer comId,Integer userId){
		//TODO 是否需要考虑其他模块的数据
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select case when a.bustype='"+ConstantInterface.TYPE_CRM+"' then '客户中心更新' end as busTypeName,a.bustype,sum from (\n");
		sql.append("select  a.bustype,count(a.busid) as sum from todayworks a \n");
		sql.append("where a.comId=? and a.userid=? and a.bustype='"+ConstantInterface.TYPE_CRM+"'\n");
		args.add(comId);
		args.add(userId);
		sql.append("group by a.bustype\n");
		sql.append(")a\n");
		return this.listQuery(sql.toString(), args.toArray(),TodayWorks.class);
	}

	

	/**
	 * 通过帐号修改用户
	 * @param user
	 */
	public void updateUserInfoByAccount(UserInfo user) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userinfo set password=? where (lower(email)=? or movePhone=?)");
		args.add(user.getPassword());
		args.add(StringUtil.isBlank(user.getEmail())?"":user.getEmail().toLowerCase());
		args.add(StringUtil.isBlank(user.getMovePhone())?"":user.getMovePhone());
		this.excuteSql(sql.toString(), args.toArray());
		
	}

	/**
	 * 树形查询部门的用户列表
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPagedUserTreeForDep(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.depname as depName,c.uuid as smImgUuid,c.filename as smImgName,aa.enabled from userinfo a");
		sql.append("\n inner join userOrganic aa on aa.userid = a.id ");
		sql.append("\n inner join department b on aa.depid = b.id and aa.comId = b.comId ");
		sql.append("\n left join upfiles c on aa.comId = c.comId and aa.smallheadportrait = c.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and aa.comId = ?");
		if(userInfo.getDepId() != null && userInfo.getDepId()>-1){
			sql.append("\n and aa.depid in (");
			sql.append("\n select  a.id from department a where a.comId = ?");
			args.add(userInfo.getComId());
			sql.append("\n start with a.id =? connect by prior a.id = a.parentid");
			args.add(userInfo.getDepId());
			sql.append("\n )");
		}
		if(null!=userInfo.getCondition()){
			this.addSqlWhereLike(userInfo.getCondition().toLowerCase(), sql, args, " and (lower(a.email) like ? or lower(a.userName) like ? or lower(a.allSpelling) like ? or lower(a.firstSpelling) like ?)",4);
		}
		return this.pagedQuery(sql.toString()," aa.enabled desc,aa.depid,a.id desc",args.toArray(),UserInfo.class);
	}

	/**
	 * 公司总人数
	 * @param comId
	 * @return
	 */
	public Integer countUsers(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(a.id) from userorganic a where a.enabled=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 验证当前的用户的登录别名
	 * @param nickName
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> checkNickName(String nickName, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id from userInfo a where 1=1 ");
		this.addSqlWhere(nickName, sql, args, " and lower(a.nickName)=? ");
		this.addSqlWhere(userId, sql, args, " and a.id<>? ");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 修改登录别名
	 * @param userInfo
	 */
	public void updateUserNickname(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userInfo set nickname=? where id=?");
		args.add(userInfo.getNickname());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());
		
	}

	/**
	 * 修改用户名
	 * @param userInfo
	 */
	public void updateUserName(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		if(!"".equals(StringUtil.delNull(userInfo.getUserName()))){
			sql.append(" update userInfo set userName=?,allSpelling=?,firstSpelling=? where id=?");
			args.add(userInfo.getUserName());
			args.add(userInfo.getAllSpelling());
			args.add(userInfo.getFirstSpelling());
			args.add(userInfo.getId());
			this.excuteSql(sql.toString(), args.toArray());
		}else{
			sql.append(" update userInfo set userName=? where id=?");
			args.add(userInfo.getUserName());
			args.add(userInfo.getId());
			this.excuteSql(sql.toString(), args.toArray());
		}
		
	}

	/**
	 * 修改用户性别
	 * @param userInfo
	 */
	public void updateUserGender(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userInfo set gender=? where id=?");
		args.add(userInfo.getGender());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 修改用户生日
	 * @param userInfo
	 */
	public void updateUserBirthday(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userInfo set birthday=? where id=?");
		args.add(userInfo.getBirthday());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 修改用户QQ
	 * @param userInfo
	 */
	public void updateUserQq(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userInfo set qq=? where id=?");
		args.add(userInfo.getQq());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 修改用户手机号
	 * @param userInfo
	 */
	public void updateUserMovePhone(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userInfo set movePhone=? where id=?");
		args.add(userInfo.getMovePhone());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());
		
	}

	/**
	 * 修改用户座机号
	 * @param userInfo
	 */
	public void updateUserLinePhone(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userInfo set linePhone=? where id=?");
		args.add(userInfo.getLinePhone());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());
		
	}

	/**
	 * 修改用户微信号
	 * @param userInfo
	 */
	public void updateUserWechat(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userInfo set wechat=? where id=?");
		args.add(userInfo.getWechat());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());
		
	}

	/**
	 * 获取个人直属上级集合
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ImmediateSuper> listImmediateSuper(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.leader,b.username as leaderName,b.gender,d.uuid,d.filename from immediateSuper a \n");
		sql.append("inner join userinfo b on a.leader = b.id \n");
		sql.append("inner join userOrganic c on a.comid = c.comid and b.id=c.userid and c.enabled=1 \n");
		sql.append("left join upfiles d on a.comid = d.comid and c.smallHeadPortrait=d.id \n");
		sql.append("where a.comid=? and a.creator=? \n");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		return this.listQuery(sql.toString(), args.toArray(),ImmediateSuper.class);
	}

	/**
	 * 企业的所有成员主键(在岗的)
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUser(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName,a.comId,a.enrollNumber from userorganic a ");
		sql.append("\n inner join userinfo b on a.userid=b.id where a.enabled=1 and a.comId=?");
		args.add(comId);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 企业的所有成员主键，打卡编号(在岗的)
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUserWithEnNumber(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName,a.comId,a.enrollNumber from userorganic a ");
		sql.append("\n inner join userinfo b on a.userid=b.id where a.enabled=1 and a.comId=? and a.enrollNumber is not null");
		args.add(comId);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 直属上级设置是否闭环验证
	 * @param leader
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ImmediateSuper> superClosedLoopCheck(int leader,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from immediateSuper a");
		sql.append("\n where a.comId = ? and a.creator=?");
		args.add(userInfo.getComId());
		args.add(leader);
		sql.append("\n start with a.leader= ?");
		args.add(userInfo.getId());
		sql.append("\n connect by a.leader = prior a.creator");
		return this.listQuery(sql.toString(),args.toArray(),ImmediateSuper.class);
	}

	/**
	 *  用户头像信息
	 * @param comId 企业号
	 * @param userId 用户主键
	 * @return
	 */
	public UserInfo getUserHeadImg(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comId,a.bigHeadPortrait,a.mediumHeadPortrait,a.smallHeadPortrait from userOrganic a");
		sql.append("\n where a.comId = ? and a.userId=?");
		args.add(comId);
		args.add(userId);
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 取得企业的管理人员
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listOrgAdmin(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName from userorganic a ");
		sql.append("\n inner join userinfo b on a.userid=b.id ");
		sql.append("\n where a.enabled=1 and a.admin>0 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 查询申请中需要本次失去权限的人员处理的待办事项
	 * @param comId 企业号
	 * @param userId 人员主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JoinRecord> listJoinRecord(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from joinrecord a inner join todayworks b on a.comId =b.comId");
		sql.append("\n and a.id=b.busid and b.bustype='"+ConstantInterface.TYPE_APPLY+"' ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(userId, sql, args, " and b.userId=?");
		this.addSqlWhere(comId, sql, args, " and b.comId=?");
		return this.listQuery(sql.toString(), args.toArray(), JoinRecord.class);
	}
	

	/**
	 * 常用的人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUsedUser(UserInfo sessionUser, String onlySubState,Integer num) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("\n select * from (  ");
		sql.append("\n select b.*,c.comId,d.uuid as smImgUuid,d.filename as smImgName,dep.depName,formedoUser.Id as formedoUserId,formedoUser.Username as formedoUserName ");
		sql.append("\n from usedUser a inner join userinfo b on a.usedUserId=b.id");
		sql.append("\n inner join userOrganic c on a.comId = c.comId and b.id = c.userid and c.enabled =1");
		sql.append("\n left join upfiles d on c.comId = d.comId and c.smallheadportrait = d.id");
		sql.append("\n left join department dep on c.depId=dep.id and dep.comId=c.comId");
		sql.append("\n left join formedo on a.usedUserId=formedo.creator");
		sql.append("\n left join userOrganic formedoOrg on formedo.comId=formedoOrg.Comid and formedo.userid=formedoOrg.Userid and formedoOrg.Enabled=1");
		sql.append("\n left join userinfo formedoUser on formedoOrg.userid=formedoUser.Id");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getId(), sql, args, " and a.userId=?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(sessionUser.getId(), sql, args, " and a.useduserid <> ?");
		//是否只查询下属
		if(StringUtils.isNotEmpty(onlySubState) && onlySubState.equals("1")){
			sql.append("\n and exists (");
			sql.append("\n select id from myLeaders where creator = a.usedUserId and leader = ? and comId = ? and creator <> leader ");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			sql.append("\n )");
		}
		
		sql.append("\n order by a.frequence desc,a.id desc");
		sql.append("\n ) where rownum<=?");
		args.add(num);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 取得缓存用户是否为系统可用的用户
	 * @param id 用户主键
	 * @return
	 */
	public UserInfo getUserInfoById(String id) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*  from userinfo a ");
		sql.append("\n where 1=1  ");
		this.addSqlWhere(id, sql, args, "  and a.id=? ");
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 获取当前登录人私有组及其成员
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUserOfGroup(UserInfo userInfo){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id as grpId,a.comId,a.grpname,c.username,c.id,c.allspelling,c.firstspelling,c.gender,f.uuid as smImgUuid,f.filename as smImgName from selfgroup a");
		sql.append("\n inner join groupPersion b on a.comId = b.comId and a.id = b.grpid");
		sql.append("\n inner join userinfo c on b.userinfoid = c.id");
		sql.append("\n inner join userOrganic org on a.comId = org.comId and c.id = org.userid and org.enabled =1");
		sql.append("\n left join upfiles f on a.comId = f.comId and org.smallheadportrait = f.id");
		sql.append("\n where a.comId=? and a.owner=? and a.id=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(userInfo.getGrpId());
		sql.append("\n order by a.orderno desc");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 获取企业部门以及部门人员
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUserOfDep(UserInfo userInfo){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select distinct(c.id),a.id as depId,a.comId,a.depname,org.comId,c.username,c.allspelling,c.firstspelling,c.gender,c.selfintr,f.uuid as smImgUuid,f.filename as smImgName from department a");
		sql.append("\n inner join userOrganic org on a.comId = org.comId and a.id = org.depid and org.enabled =1");
		sql.append("\n inner join userinfo c on c.id = org.userid");
		sql.append("\n left join upfiles f on a.comId = f.comId and org.smallheadportrait = f.id");
		sql.append("\n where a.comId=? and a.id=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getDepId());
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 取得直属下属的成员数
	 * @param userId 人员主键
	 * @param comId 企业号
	 * @return
	 */
	public Integer countSubUser(Integer userId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(*) from myLeaders a ");
		sql.append("\n where a.creator <> a.leader ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.leader=?");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 取得用户的弹窗设置
	 * @param userId 用户主键
	 * @param comId 企业号
	 * @return
	 */
	public UserOrganic getUserOrganic(Integer userId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from userorganic a where 1=1");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return (UserOrganic) this.objectQuery(sql.toString(), args.toArray(), UserOrganic.class);
	}

	/**
	 * 取得人员名片
	 * @param comId
	 * @param userId
	 * @return
	 */
	public UserInfo getUserBaseInfo(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n a.linePhone,a.movePhone,a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n aa.comId,aa.admin,aa.job,aa.depId,aa.enabled,d.depName,c.uuid as smImgUuid,c.fileName as smImgName,aa.enrollNumber");
		sql.append("\n from userinfo a inner join userOrganic aa on a.Id=aa.userId");
		sql.append("\n left join upfiles c on aa.smallHeadPortrait=c.id and c.comId=aa.comId");
		sql.append("\n left join department d on aa.depId=d.id and d.comId=aa.comId");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and aa.comId=?");
		this.addSqlWhere(userId, sql, args, " and aa.userid=?");
		
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 查询聊天室的成员信息
	 * @param comId 企业号
	 * @param userIds 成员主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> getUserInfoByIds(Integer comId, String userIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName");
		sql.append("\n from userinfo a ");
		sql.append("\n where 1=1 and a.id in ("+userIds+")");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	
	
	/**
	 * 修改部门
	 * @param comId 企业编号
	 * @param userId 用户主键
	 * @param depId 部门主键
	 */
	public void updateUserDep(Integer comId, Integer userId,
			Integer depId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userOrganic set depId=? where comId=? and  userId=?");
		args.add(depId);
		args.add(comId);
		args.add(userId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 用户不需要提醒完善提醒
	 * @param comId 企业编号
	 * @param userId 用户主键
	 */
	public void updateAlterInfo(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userOrganic set alterInfo=0 where comId=? and  userId=?");
		args.add(comId);
		args.add(userId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 入职时间修改
	 * @param comId 企业编号
	 * @param userId 用户主键
	 * @param hireDate 入职时间
	 */
	public void updateUserHireDate(Integer comId, Integer userId, String hireDate) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userOrganic set recordCreateTime=? where comId=? and  userId=?");
		args.add(hireDate);
		args.add(comId);
		args.add(userId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 添加常用人员的使用频率
	 * @param comId 企业号
	 * @param userId 操作员主键
	 * @param usedUserId 常用人员主键
	 */
	public void addUsedUser(Integer comId, Integer userId, Integer usedUserId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update usedUser set frequence=frequence+1 where comId=? and  userId=? and usedUserId=?");
		args.add(comId);
		args.add(userId);
		args.add(usedUserId);
		Integer result = this.excuteSql(sql.toString(), args.toArray());
		if(result==0){
			UsedUser usedUser = new  UsedUser();
			//企业号
			usedUser.setComId(comId);
			//操作员主键
			usedUser.setUserId(userId);
			//选择的人员主键
			usedUser.setUsedUserId(usedUserId);
			//设置使用频率
			usedUser.setFrequence(1);
			//添加选择的人员
			this.add(usedUser);
		}
		
	}

	/**
	 * 检测帐号是否注册
	 * @param account
	 * @return
	 */
	public UserInfo checkInputAccount(String account) {
		StringBuffer sql = new StringBuffer("select * from (");
		sql.append("\n select rownum, a.* from userinfo a ");
		sql.append("\n where (lower(a.email)=? or a.movephone=?) order by id asc");
		sql.append("\n ) a where rownum=1");
		return (UserInfo) this.objectQuery(sql.toString(), new Object[]{account,account}, UserInfo.class);
	}

	/**
	 * 更新用户email
	 * @param curUser
	 */
	public void updateUserEmail(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userInfo set email=? where id=?");
		args.add(curUser.getEmail());
		args.add(curUser.getId());
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 验证当前操作人员是否是超级管理员
	 * @param curUser
	 * @return
	 */
	public UserInfo isAdministrator(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from userinfo a");
		sql.append("\n inner join userOrganic b on a.id=b.userid and b.admin=1");
		sql.append("\n inner join organic c on b.comId=c.orgnum");
		sql.append("\n where a.id=? and c.orgnum=?");
		args.add(curUser.getId());
		args.add(curUser.getComId());
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 验证当前操作人员是否是管理员
	 * @param curUser
	 * @return
	 */
	public UserInfo isAdmin(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from userinfo a");
		sql.append("\n inner join userOrganic b on a.id=b.userid and b.admin>0");
		sql.append("\n inner join organic c on b.comId=c.orgnum");
		sql.append("\n where a.id=? and c.orgnum=?");
		args.add(curUser.getId());
		args.add(curUser.getComId());
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 获取直属上级信息
	 * @param userInfo 当前操作人
	 * @return
	 */
	public UserInfo queryDirectLeaderInfo(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select b.*,d.uuid,d.filename \n");
		sql.append("from myLeaders a \n");
		sql.append("inner join userinfo b on a.leader = b.id \n");
		sql.append("inner join userOrganic c on a.comId = c.comId and b.id=c.userid and c.enabled=1 \n");
		sql.append("left join upfiles d on a.comId = d.comId and c.smallHeadPortrait=d.id \n");
		sql.append("where a.comId=? and a.creator=? and a.creator <> a.leader and rownum=1 \n");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		return (UserInfo)this.objectQuery(sql.toString(), args.toArray(),UserInfo.class);
	}

	/**
	 * 修改用户是否为首席
	 * @param isChief
	 * @param sessionUser
	 */
	public void updateUserChief(String isChief, UserInfo sessionUser) {
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update userOrganic set isChief=? where comId=? and userId=?");
		args.add(CommonUtil.isNull(isChief)?"0":isChief);
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 客户负责人
	 * @param comId 企业编号
	 * @param customerId 客户主键
	 * @return
	 */
	public UserInfo getCrmOwner(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//客户负责人
		sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from customer a \n");
		sql.append("inner join Userinfo c on a.owner = c.id \n");
		sql.append("inner join userOrganic uOrg1 on a.comId = uOrg1.Comid and c.id = uOrg1.Userid and uOrg1.Enabled=1 \n");
		sql.append("where a.comId = ? and a.id = ? \n");
		args.add(comId);
		args.add(customerId);
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(),UserInfo.class);
	}
	/**
	 * 项目的负责人
	 * @param comId 企业编号
	 * @param itemId 项目ID
	 * @return
	 */
	public UserInfo getItemOwner(Integer comId,Integer itemId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//项目的负责人
		sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from item a \n");
		sql.append("inner join Userinfo c on  a.owner = c.id \n");
		sql.append("inner join userorganic d on  a.comId = d.comId and c.id=d.userid and d.enabled=1 \n");
		sql.append("where a.comId = ? and a.id = ? \n");
		args.add(comId);
		args.add(itemId);
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 获取模块负责人和留言父用户
	 * @param comId 企业主键
	 * @param modId 业务主键
	 * @param talkParentId  父用户讨论信息主键
	 * @param busType 业务类别
	 * @param pushUserIdSet 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listOwnerForMsg(Integer comId,Integer modId,Integer talkParentId,String busType, Set<Integer> pushUserIdSet){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//项目的负责人
		sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from item a \n");
		sql.append("inner join Userinfo c on  a.owner = c.id \n");
		sql.append("inner join userorganic d on  a.comId = d.comId and c.id=d.userid and d.enabled=1 \n");
		sql.append("where a.comId = ? and a.id = ? and "+ ConstantInterface.TYPE_ITEM +"= ? \n");
		args.add(comId);
		args.add(modId);
		args.add(busType);
		
		if(talkParentId!=null && talkParentId>0){
			//项目留言父用户
			sql.append("union \n");
			sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from Userinfo c \n");
			sql.append("inner join itemTalk t on  c.id = t.userid and t.id=? and "+ ConstantInterface.TYPE_ITEM +"= ? \n");
			args.add(talkParentId);
			args.add(busType);
		}
		
		//客户负责人
		sql.append("union \n");
		sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from customer b \n");
		sql.append("inner join Userinfo c on b.owner = c.id \n");
		sql.append("inner join userorganic d on b.comId = d.Comid and c.id = d.Userid and d.Enabled=1 \n");
		sql.append("where b.comId = ? and b.id = ? and "+ConstantInterface.TYPE_CRM+"= ? \n");
		args.add(comId);
		args.add(modId);
		args.add(busType);
		
		if(talkParentId!=null && talkParentId>0){
			//客户留言父用户
			sql.append("union \n");
			sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from Userinfo c \n");
			sql.append("inner join feedBackInfo t on  c.id = t.userid and t.id=?  and "+ConstantInterface.TYPE_CRM+"= ? \n");
			args.add(talkParentId);
			args.add(busType);
		}
		//本次推送的人员
		if(null!=pushUserIdSet && !pushUserIdSet.isEmpty()){
			sql.append("\n union");
			sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
			sql.append("\n from userinfo b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhereIn(pushUserIdSet.toArray(new Integer[pushUserIdSet.size()]), sql, args, "\n and b.id in ?");
		}
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 用户加入团队的个数
	 * @param userId
	 * @return
	 */
	public Integer countUserIn(Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(userId) from userOrganic where 1=1");
		this.addSqlWhere(userId, sql, args, " and userId=?");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 获取团队使用人数上限零界值人员信息（按加入团队时间升序排列）
	 * @param comId 团队主键
	 * @return
	 */
	public UserInfo queryOrgUsersUpperLimitUser(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from ");//临界人员选择
		sql.append("\n (");
		sql.append("\n select rowSelect.*,rownum orderNum from");//团队空间允许人数筛选
		sql.append("\n (");
		sql.append("\n select a.id,aa.recordCreateTime hireDate,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n case when uConf.openState is null then 1 else uConf.openState end autoEject,");
		sql.append("\n a.allSpelling,a.firstSpelling,a.password,a.linePhone,a.movePhone,a.selfIntr,");
		sql.append("\n a.email,a.wechat,a.qq,aa.bigHeadPortrait,aa.mediumHeadPortrait,aa.smallHeadPortrait,a.gender,");
		sql.append("\n a.birthday,a.nickname,aa.comId,aa.job,aa.depId,aa.admin,aa.enabled,aa.lastOnlineTime,aa.ischief,");
		sql.append("\n aa.jifenScore,aa.alterInfo,b.orgName ,h.depName,aa.inService,");
		sql.append("\n e.uuid as smImgUuid,e.filename as smImgName,");
		sql.append("\n g.uuid as logoUuid,g.filename as logoName,");
		sql.append("\n (select levname from jifenLev where levMinScore=(");
		sql.append("\n select max(levMinScore) from jifenLev where levMinScore<=aa.jifenScore)) as levName,");
		sql.append("\n (select levMinScore from jifenLev where levMinScore=(");
		sql.append("\n select min(levMinScore) from jifenLev where levMinScore>aa.jifenScore)) as nextLevJifen,");
		sql.append("\n (select levMinScore from jifenLev where levMinScore=(");
		sql.append("\n select max(levMinScore) from jifenLev where levMinScore<=aa.jifenScore)) as minLevJifen,");
		sql.append("\n case when orgSpace.Usersnum is null then "+ConstantInterface.ORG_DEFAULT_MEMBERS+" else orgSpace.Usersnum end as usersNum");
		sql.append("\n from  userOrganic aa");
		sql.append("\n inner join  userinfo a on aa.userid=a.id");
		sql.append("\n inner join organic b on aa.comId = b.orgNum");
		sql.append("\n left join userConf uConf on uConf.comId= aa.comId and uConf.userId=aa.userId and uConf.sysConfCode='1'");
		sql.append("\n left join upfiles e on aa.smallHeadPortrait=e.id");
		sql.append("\n left join organic f on aa.comId=f.orgNum");
		sql.append("\n left join upfiles g on f.logo=g.id");
		sql.append("\n left join department h on aa.depId=h.id");
		sql.append("\n left join organicSpaceCfg orgSpace on aa.comId = orgSpace.Comid and orgSpace.Enddate >= to_char(sysdate,'yyyy-MM-dd')");
		sql.append("\n where 1=1  and aa.enabled='1' and aa.inService=1 ");
		sql.append("\n and b.enabled='1' and aa.comId=?");
		args.add(comId);
		sql.append("\n order by hireDate,a.id asc");
		sql.append("\n ) rowSelect ");
		sql.append("\n ) finalResult where finalResult.orderNum=finalResult.usersNum");
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 将离职人员的下属设定为接收人员
	 * @param userInfo 当前离职人员
	 * @param userId 接收人员
	 */
	public void updateDismissionLeader(UserInfo userInfo, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update immediateSuper set leader=? where comId=? and leader=?");
		args.add(userId);
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 是否有权限
	 * @param curUser
	 * @param userId
	 * @return
	 */
	public UserInfo authorCheck(UserInfo userInfo,Integer userId,boolean isForceIn){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id");
		sql.append("\n from userinfo a inner join userOrganic aa on a.id=aa.userId");
		sql.append("\n inner join department d on aa.depId=d.id and d.comId = aa.comId");
		
		sql.append("\n where aa.enabled=1 and aa.inService = 1 	and aa.comId=? and a.id=? " );
		args.add(userInfo.getComId());
		args.add(userId);
		
		if(!isForceIn){
			sql.append("\n and (a.id=? " );
			args.add(userInfo.getId());
			//人员上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 		select id from myLeaders where creator = a.id and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append("	))\n");
		}
		return (UserInfo) this.objectQuery(sql.toString(),args.toArray(),UserInfo.class);
	}
	/**
	 * 取得在职用户信息
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUserByAttence(UserInfo userInfo,List<UserInfo> listUserInfo,List<Department> listDep,boolean isForceIn) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n aa.comId,aa.depId,aa.admin,aa.enrollNumber,d.depName");
		sql.append("\n from userinfo a inner join userOrganic aa on a.id=aa.userId");
		sql.append("\n inner join department d on aa.depId=d.id and d.comId = aa.comId");
		
		sql.append("\n where aa.enabled=1 and aa.inService = 1 	and aa.comId=? " );
		args.add(userInfo.getComId());
		
		//人员筛选条件
		if(null != listUserInfo && !listUserInfo.isEmpty()){
			sql.append("	and  ( a.id = 0 ");
			for(UserInfo user : listUserInfo){
				sql.append("or a.id=?  \n");
				args.add(user.getId());
			}
			sql.append("	 ) ");
		}
		//部门筛选条件
		if(null != listDep && !listDep.isEmpty()){
			sql.append("and (\n");
			for(int i =0;i<listDep.size();i++){
				if(i == 0){
					sql.append("\n exists  ");
				}else{
					sql.append("\n or exists ");
				}
				sql.append(" \n (");
				sql.append("\n  select u.id from  userInfo u");
				sql.append("\n  inner join userOrganic uo on u.id = uo.userId and uo.comId = ? \n");
				args.add(userInfo.getComId());
				sql.append("\n  inner join department  executeDep on uo.depId = executeDep.Id and  uo.comId = executeDep.comId \n");
				sql.append("\n  where u.id  = a.id    \n");
				sql.append("	start with executeDep.id=? \n");
				sql.append("	connect by prior executeDep.id = executeDep.parentid \n");
				args.add(listDep.get(i).getId());
				sql.append(" )\n");
			}
			sql.append(") \n");
		}
		
		if(!isForceIn){
			sql.append("\n and (a.id=? " );
			args.add(userInfo.getId());
			//人员上级权限验证
			sql.append(" or exists( \n");
			sql.append("		select id from myLeaders where creator = a.id and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append("	))\n");
		}
		return this.pagedQuery(sql.toString()," aa.depId asc,a.id asc ", args.toArray(), UserInfo.class);
	}
	
	/**
	 * 取得在职用户信息
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listAllEnabledUser(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,a.movePhone,");
		sql.append("\n aa.comId,aa.depId,aa.admin,aa.enrollNumber,d.depName");
		sql.append("\n from userinfo a inner join userOrganic aa on a.id=aa.userId");
		sql.append("\n inner join department d on aa.depId=d.id and d.comId = aa.comId");
		
		sql.append("\n where aa.enabled=1 and aa.inService = 1 	and aa.comId=? " );
		args.add(userInfo.getComId());
		
		
		return this.listQuery(sql.toString(),args.toArray(), UserInfo.class);
	}
	/**
	 * 通过编号查询用户
	 * @param curUser
	 * @param enrollNumber
	 * @return
	 */
	public UserOrganic queryUserByEnrollNumber(UserInfo curUser, String enrollNumber) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.* from UserOrganic a where a.comId = ? and a.enrollNumber = ?");
		args.add(curUser.getComId());
		args.add(enrollNumber);
		return (UserOrganic) this.objectQuery(sql.toString(), args.toArray(), UserOrganic.class);
	}
	/**
	 * 公告范围合并人员
	 * @param comId
	 * @param announId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listAnnounScopeUser(Integer comId,Integer announId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.userName,a.movephone,a.email,a.comId");
		sql.append("\n from (");
		//部门范围
		sql.append("\n select c.*,cc.comId  from userorganic cc");
		sql.append("\n inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n inner join department d on d.id = cc.depId");
		sql.append("\n where cc.depId in (");
		sql.append("\n  select depId from announscopebydep where announid = ?)");
		args.add(announId);
		//人员范围
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId  from userorganic cc");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n inner join announscopebyuser an on cc.userId = an.userId and cc.comId = an.comId ");
		sql.append("\n where an.comId=? and an.announid = ?");
		args.add(comId);
		args.add(announId);
		sql.append("\n ) a where 1=1");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 制度范围合并人员
	 * @param comId
	 * @param announId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listInstituScopeUser(Integer comId,Integer instituId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.userName,a.movephone,a.email,a.comId");
		sql.append("\n from (");
		//部门范围
		sql.append("\n select c.*,cc.comId  from userorganic cc");
		sql.append("\n inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n inner join department d on d.id = cc.depId");
		sql.append("\n where cc.depId in (");
		sql.append("\n  select depId from instituscopebydep where instituId = ?)");
		args.add(instituId);
		//人员范围
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId from userorganic cc");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n inner join instituscopebyuser an on cc.userId = an.userId and cc.comId = an.comId ");
		sql.append("\n where an.comId=? and an.instituId = ?");
		args.add(comId);
		args.add(instituId);
		sql.append("\n ) a ");
		sql.append("\n  where 1=1 ");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 制度范围合并人员 (分页)
	 * @param comId
	 * @param announId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> pagedInstituViewUser(Integer comId,Integer instituId,Integer creatorId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from ( ");
		sql.append("\n select a.id,a.userName,a.movephone,a.email,a.comId,a.gender,a.smImgUuid,a.smImgName,b.id isView,b.recordCreateTime viewTime ");
		sql.append("\n from (");
		//部门范围
		sql.append("\n select c.*,cc.comId,u.uuid smImgUuid,u.filename smImgName  from userorganic cc");
		sql.append("\n inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n inner join department d on d.id = cc.depId");
		sql.append("\n left join upfiles u on cc.comId=u.comId and cc.mediumheadportrait=u.id");
		sql.append("\n where cc.depId in (");
		sql.append("\n  select depId from instituscopebydep where instituId = ?)");
		args.add(instituId);
		//人员范围
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId,u.uuid imguuid,u.filename imgname  from userorganic cc");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n left join upfiles u on cc.comId=u.comId and cc.mediumheadportrait=u.id");
		sql.append("\n inner join instituscopebyuser an on cc.userId = an.userId and cc.comId = an.comId ");
		sql.append("\n where an.comId=? and an.instituId = ?");
		args.add(comId);
		args.add(instituId);
		//创建人员
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId,u.uuid imguuid,u.filename imgname  from userorganic cc");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n left join upfiles u on cc.comId=u.comId and cc.mediumheadportrait=u.id");
		sql.append("\n inner join institution an on cc.userId = an.creator ");
		sql.append("\n where an.comId=? and an.id = ?");
		args.add(comId);
		args.add(instituId);
		//创建人上属
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId,u.uuid imguuid,u.filename imgname  from userorganic cc");
		sql.append("\n  inner join (select leader from myLeaders where comId=? and creator <> leader ");
		args.add(comId);
		sql.append("\n start with creator= ?");
		args.add(creatorId);
		sql.append("\n  connect by prior leader = creator )a on cc.userId = a.leader ");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n left join upfiles u on cc.comId=u.comId and cc.mediumheadportrait=u.id");
		
		sql.append("\n ) a ");
		sql.append("\n  left join viewRecord b on a.id = b.userId and b.busId = ? and b.busType = ? ");
		args.add(instituId);
		args.add(ConstantInterface.TYPE_INSTITUTION);
		sql.append("\n  where 1=1 ");
		sql.append("\n ) a   where 1=1");
		return this.pagedQuery(sql.toString(),"	a.viewTime desc", args.toArray(), UserInfo.class);
	}
	/**
	 * 制度范围合并人员
	 * @param comId
	 * @param announId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listInstituViewUser(Integer comId,Integer instituId,Integer creatorId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from ( ");
		sql.append("\n select a.id,a.userName,a.movephone,a.email,a.comId,a.gender,a.smImgUuid,a.smImgName,b.id isView,b.recordCreateTime viewTime ");
		sql.append("\n from (");
		//部门范围
		sql.append("\n select c.*,cc.comId,u.uuid smImgUuid,u.filename smImgName  from userorganic cc");
		sql.append("\n inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n inner join department d on d.id = cc.depId");
		sql.append("\n left join upfiles u on cc.comId=u.comId and cc.mediumheadportrait=u.id");
		sql.append("\n where cc.depId in (");
		sql.append("\n  select depId from instituscopebydep where instituId = ?)");
		args.add(instituId);
		//人员范围
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId,u.uuid imguuid,u.filename imgname  from userorganic cc");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n left join upfiles u on cc.comId=u.comId and cc.mediumheadportrait=u.id");
		sql.append("\n inner join instituscopebyuser an on cc.userId = an.userId and cc.comId = an.comId ");
		sql.append("\n where an.comId=? and an.instituId = ?");
		args.add(comId);
		args.add(instituId);
		//创建人员
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId,u.uuid imguuid,u.filename imgname  from userorganic cc");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n left join upfiles u on cc.comId=u.comId and cc.mediumheadportrait=u.id");
		sql.append("\n inner join institution an on cc.userId = an.creator ");
		sql.append("\n where an.comId=? and an.id = ?");
		args.add(comId);
		args.add(instituId);
		//创建人上属
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId,u.uuid imguuid,u.filename imgname  from userorganic cc");
		sql.append("\n  inner join (select leader from myLeaders where comId = ?");
		args.add(comId);
		sql.append("\n start with creator= ?");
		args.add(creatorId);
		sql.append("\n  connect by prior leader = creator )a on cc.userId = a.leader ");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n left join upfiles u on cc.comId=u.comId and cc.mediumheadportrait=u.id");
		
		sql.append("\n ) a ");
		sql.append("\n  left join viewRecord b on a.id = b.userId and b.busId = ? and b.busType = ? ");
		args.add(instituId);
		args.add(ConstantInterface.TYPE_INSTITUTION);
		sql.append("\n  where 1=1 ");
		sql.append("\n ) a   where 1=1");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 取得在职用户阅读人员
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<UserInfo> listAllEnabledRead(Integer comId,Integer instituId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select c.*,cc.comId,u.uuid smImgUuid,u.filename smImgName,a.b.id isView,b.recordCreateTime viewTime   from userorganic cc");
		sql.append("\n inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n left join upfiles u on cc.comId=u.comId and cc.mediumheadportrait=u.id");
		sql.append("\n left join viewRecord b on c.id = b.userId and b.busId = ? and b.busType = ? ");
		args.add(instituId);
		args.add(ConstantInterface.TYPE_INSTITUTION);
		sql.append("\n  where 1=1 ");
		sql.append("\n  and cc.enabled=1 and cc.inService = 1 	and cc.comId=?  ");
		args.add(comId);
		
		return this.pagedQuery(sql.toString(),"	b.recordCreateTime asc", args.toArray(), UserInfo.class);
	}

	/**
	 * 制度范围合并人员
	 * @param comId
	 * @param announId
	 * @return
	 */
	public Integer countInstituRead(Integer comId,Integer instituId,Integer creatorId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select　count(a.id) from (");
		//部门范围
		sql.append("\n select c.*,cc.comId   from userorganic cc");
		sql.append("\n inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n inner join department d on d.id = cc.depId");
		sql.append("\n where cc.depId in (");
		sql.append("\n  select depId from instituscopebydep where instituId = ?)");
		args.add(instituId);
		//人员范围
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId  from userorganic cc");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n inner join instituscopebyuser an on cc.userId = an.userId and cc.comId = an.comId ");
		sql.append("\n where an.comId=? and an.instituId = ?");
		args.add(comId);
		args.add(instituId);
		//创建人员
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId from userorganic cc");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n inner join institution an on cc.userId = an.creator ");
		sql.append("\n where an.comId=? and an.id = ?");
		args.add(comId);
		args.add(instituId);
		//创建人上属
		sql.append("\n union");
		sql.append("\n select c.*,cc.comId  from userorganic cc");
		sql.append("\n  inner join (select leader from myLeaders  where comId=?");
		args.add(comId);
		sql.append("\n start with creator= ?");
		args.add(creatorId);
		sql.append("\n  connect by prior leader = creator )a on cc.userId = a.leader ");
		sql.append("\n  inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n ) a ");
		sql.append("\n  left join viewRecord b on a.id = b.userId and b.busId = ? and b.busType = ? ");
		args.add(instituId);
		args.add(ConstantInterface.TYPE_INSTITUTION);
		sql.append("\n  where 1=1 and b.id >0 ");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 取得在职用户阅读人员
	 * @param comId
	 * @return
	 */
	public Integer countAllEnabledRead(Integer comId,Integer instituId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select　count(a.id) from (");
		sql.append("\n select c.*,cc.comId   from userorganic cc");
		sql.append("\n inner join userinfo  c on c.id = cc.userId ");
		sql.append("\n where cc.enabled=1 and cc.inService = 1 	and cc.comId=?" );
		args.add(comId);
		sql.append("\n ) a ");
		sql.append("\n  left join viewRecord b on a.id = b.userId and b.busId = ? and b.busType = ? ");
		args.add(instituId);
		args.add(ConstantInterface.TYPE_INSTITUTION);
		sql.append("\n where  b.id >0 " );
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 从直属上级列表中获取所有的领导集合
	 * @param curUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ImmediateSuper> listLeadersFromImmediateSuper(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comid,a.creator,a.leader from immediateSuper a");
		sql.append("\n where a.comid=?");
		args.add(curUser.getComId());
		sql.append("\n start with a.creator=?");
		args.add(curUser.getId());
		sql.append("\n connect by prior a.leader = a.creator");
		return this.listQuery(sql.toString(), args.toArray(),ImmediateSuper.class);
	}
	
	/**
	 * 查询自己是否为替岗人员
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Integer queryInsteadNum(Integer comId, Integer userId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		sql.append("\n select count(a.id) ");
		sql.append("\n from forMeDo a");
		sql.append("\n where 1=1");
		this.addSqlWhere(userId, sql, args, "\n and a.creator=?");
		this.addSqlWhere(comId, sql, args, "\n and a.comId=?");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 取得已代理人员的集合
	 * @param sessionUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ForMeDo> listForMeDo(Integer comId,Integer creator) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		sql.append("\n select a.id,a.comId,a.userId,a.creator,c.userName");
		sql.append("\n from forMeDo a");
		sql.append("\n inner join userorganic b on a.comId=b.comId and a.userId=b.userId");
		sql.append("\n inner join userInfo c on a.userId=c.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(creator, sql, args, "\n and a.creator=?");
		this.addSqlWhere(comId, sql, args, "\n and a.comId=?");
		sql.append("\n order by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), ForMeDo.class);
	}

	/**
	 * 查询离岗人员的代理人员
	 * @param comId 组织
	 * @param userId
	 * @return
	 */
	public ForMeDo queryForMeDo(Integer comId, Integer creator) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		sql.append("\n select a.id,a.comId,a.userId,a.creator,c.userName");
		sql.append("\n from forMeDo a");
		sql.append("\n inner join userorganic b on a.comId=b.comId and a.userId=b.userId");
		sql.append("\n inner join userInfo c on a.userId=c.id");
		sql.append("\n where 1=1 and b.enabled=?");
		args.add(ConstantInterface.COMMON_YES);
		this.addSqlWhere(creator, sql, args, "\n and a.creator=?");
		this.addSqlWhere(comId, sql, args, "\n and a.comId=?");
		return (ForMeDo) this.objectQuery(sql.toString(), args.toArray(), ForMeDo.class);
	}

	/**
	 * 查询团队的所有离岗人员
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ForMeDo> listForMeDo(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.comId,a.userId,f.userName as creatorName,a.creator,c.userName,b.depId userDepId,d.depId as creatorDepId");
		sql.append("\n from forMeDo a");
		sql.append("\n inner join userorganic b on a.comId=b.comId and a.creator=b.userId");
		sql.append("\n inner join userInfo c on a.creator=c.id");
		sql.append("\n inner join userorganic d on a.comId=d.comId and a.userId=d.userId");
		sql.append("\n inner join userInfo f on a.userId=f.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, "\n and a.comId=?");
		return this.listQuery(sql.toString(), args.toArray(), ForMeDo.class);
	}
	
	/**
	 * 查询指定人员的退送消息信息
	 * @param comId
	 * @param sharerIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listScopeUserForMsg(Integer comId, List<Integer> sharerIds){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//任务负责人
		sql.append("select d.id,d.email,d.wechat,d.qq,d.userName \n");
		sql.append("from userinfo d \n");
		sql.append("inner join userorganic e on d.id=e.userid and e.enabled=1 \n");
		sql.append("where e.comId = ? \n");
		args.add(comId);
		this.addSqlWhereIn(sharerIds.toArray(new Integer[sharerIds.size()]), sql, args, "\n and d.id in ?");
		return this.listQuery(sql.toString(),args.toArray(),UserInfo.class);
	}

	/**
	 * 查询是否为顶层领导
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public UserOrganic checkTopLeaderState(UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.ischief");
		sql.append("\n from userorganic a ");
		sql.append("\n where 1=1 and a.comId=? and a.userid=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		return (UserOrganic) this.objectQuery(sql.toString(), args.toArray(), UserOrganic.class);
	}

	/**
	 * 查询当前人员的所有下属
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUserAllSub(UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.creator id ");
		sql.append("\n from myLeaders a ");
		sql.append("\n where a.comId = ? and creator = ? and creator <> leader ");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 初始化人员树
	 * @param comId 团队号
	 */
	public void initUserTree(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n insert into myLeaders(comId,creator,leader)");
		sql.append("\n (");
		sql.append("\n 	select a.comId,a.creator,REGEXP_SUBSTR(a.leader ,'[^,]+',1,l) as leader");
		sql.append("\n 	from (");
		sql.append("\n 		select d.comId,d.id as creator,SUBSTR(SYS_CONNECT_BY_PATH(d.id,','),2) as leader,level newlevel ");
		sql.append("\n 		from (");
		sql.append("\n 			select org.comId,a.id,");
		sql.append("\n 			case when b.leader is not null then b.leader else -1 end as leader");
		sql.append("\n 			from userOrganic org ");
		sql.append("\n  		inner join userInfo a on org.userid=a.id and org.enabled=1");
		sql.append("\n 			left join immediateSuper b on a.id=b.creator");
		sql.append("\n 			where org.comId=?");
		args.add(comId);
		sql.append("\n 		) d start with d.leader=-1 connect by prior d.id=d.leader");
		sql.append("\n ) a,");
		sql.append("\n (SELECT LEVEL l FROM DUAL CONNECT BY LEVEL<=100) b");
		sql.append("\n WHERE l <=newlevel");
		sql.append("\n )");
		this.excuteSql(sql.toString(), args.toArray());
		
	}

}
