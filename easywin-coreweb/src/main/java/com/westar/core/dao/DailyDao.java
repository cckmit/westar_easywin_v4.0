package com.westar.core.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Daily;
import com.westar.base.model.DailyLog;
import com.westar.base.model.DailyMod;
import com.westar.base.model.DailyModContDep;
import com.westar.base.model.DailyModContMember;
import com.westar.base.model.DailyModContent;
import com.westar.base.model.DailyPlan;
import com.westar.base.model.DailyQ;
import com.westar.base.model.DailyShareGroup;
import com.westar.base.model.DailyTalk;
import com.westar.base.model.DailyTalkFile;
import com.westar.base.model.DailyUpfiles;
import com.westar.base.model.DailyVal;
import com.westar.base.model.DailyViewer;
import com.westar.base.model.ForceInPersion;
import com.westar.base.model.ModContDep;
import com.westar.base.model.SubTimeSet;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.DailyPojo;
import com.westar.base.util.ConstantInterface;

@Repository
public class DailyDao extends BaseDao {

	/**
	 * 获取指定公司的分享模板
	 * 
	 * @param comId
	 *            公司主键
	 * @return com.westar.base.model.DailyMod
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:37
	 */
	public DailyMod getDailyModMod(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from DailyMod where comId = ? ");
		args.add(comId);
		return (DailyMod) this.objectQuery(sql.toString(), args.toArray(),
				DailyMod.class);
	}

	/**
	 * 
	 * 模板条目
	 * 
	 * @param modId
	 *            模板
	 * @param comId
	 *            公司编号
	 * @param dailyLev
	 *            级别 1 团队级 2 部门级 3成员级别
	 * @return java.util.List<com.westar.base.model.DailyModContent>
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:37
	 */
	@SuppressWarnings("unchecked")
	public List<DailyModContent> listDailyModContent(Integer modId,
			Integer comId, String dailyLev) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select a.* from dailyModContent a where 1=1");
		this.addSqlWhere(modId, sql, args, " and a.modId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(dailyLev, sql, args, " and a.dailyLev=?");

		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyModContent.class);
	}

	/**
	 * 分享模板部门级条目部门
	 * 
	 * @param modContId
	 *             * @param comId 公司主键  * @param modId 模块主键
	 * @return java.util.List<com.westar.base.model.DailyModContDep>
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:37
	 */
	@SuppressWarnings("unchecked")
	public List<DailyModContDep> listDailyModContDep(Integer modContId,
			Integer comId, Integer modId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.depname ");
		sql.append("\n from dailyModContDep a inner join department b on a.comId=b.comId and a.depid=b.id ");
		sql.append("where 1=1 \n");
		this.addSqlWhere(modContId, sql, args, " and a.modcontid=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(modId, sql, args, " and a.modId=?");
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyModContDep.class);
	}

	/**
	 * 模板成员级条目成员
	 * 
	 * @param modContId
	 *             * @param comId 公司主键  * @param modId 模块主键
	 * @return java.util.List<com.westar.base.model.DailyModContMember>
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:38
	 */
	@SuppressWarnings("unchecked")
	public List<DailyModContMember> listDailyModContMember(Integer modContId,
			Integer comId, Integer modId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.gender,b.userName as memberName,c.uuid as imgUuid, c.filename as imgName \n");
		sql.append(" from dailyModContMember a inner join userinfo b on  a.memberId=b.id \n");
		sql.append(" inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId \n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait=c.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(modContId, sql, args, " and a.modcontid=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(modId, sql, args, " and a.modId=?");
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyModContMember.class);
	}

	/**
	 * 分享模块列表
	 * 
	 * @param dailyPojo
	 *             * @param userInfo  * @param isForceInPersion
	 * @return java.util.List<com.westar.base.model.Daily>
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:38
	 */
	@SuppressWarnings("unchecked")
	public List<Daily> listPagedDaily(DailyPojo dailyPojo, UserInfo userInfo,
			boolean isForceInPersion) throws ParseException {

		/************************************** 分享列表 ***************************************/
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("\n select a.comId,a.yeardate as dailyDate,a.userid reporterId,a.username,");
		sql.append("\n case when b.id is null then 0 else b.id end id,");
		sql.append("\n a.dailyName,b.recordCreateTime,");
		sql.append("\n case when b.state is null then '1' else b.state end state,");
		sql.append("\n a.depId,substr(a.registInTime,0,10) registInTime,f.depname");
		sql.append("\n from (");
		sql.append("\n 		select uorg.comId,uorg.userId,a.yeardate,u.username,uorg.depid,");
		sql.append("\n 		u.recordcreatetime registInTime,u.username||' '||a.yeardate ||' 日报' dailyName");
		sql.append("\n  	from userorganic uorg");
		sql.append("\n 		inner join userinfo u on uorg.userId=u.id");
		sql.append("\n 		left join  sysalldate a on 1=1");
		sql.append("\n 		where uorg.comId=? and uorg.enabled=1");
		args.add(userInfo.getComId());
		this.addSqlWhere(dailyPojo.getDailyYear(), sql, args, "\n and a.year=?");
		this.addSqlWhere(dailyPojo.getStartDate(), sql, args,
				"\n and a.yeardate>=?");
		this.addSqlWhere(dailyPojo.getEndDate(), sql, args,
				"\n and a.yeardate<=?");
		// 不能小于该员工的入职日期
		sql.append("\n  and a.yeardate >= substr(uorg.recordcreatetime,0,10)");
		sql.append("\n ) a");
		sql.append("\n left join (");
		sql.append("\n 		select uorg.comId,uorg.userId,daily.dailyName,daily.hasPlan,daily.id,daily.recordcreatetime,");
		sql.append("\n      case when daily.state is null then '1' else daily.state end state,");
		sql.append("\n      case when daily.scopetype is null then 2 else daily.scopetype end scopetype,");
		sql.append("\n      daily.dailydate");
		sql.append("\n  	from userorganic uorg");
		sql.append("\n  	left join daily on uorg.comId=daily.comId and uorg.userid=daily.reporterid");
		sql.append("\n  	where uorg.comId=? and uorg.enabled=1");
		args.add(userInfo.getComId());

		this.addSqlWhere(dailyPojo.getDailyYear(), sql, args,
				"\n and substr(daily.dailyDate,0,4)=?");
		this.addSqlWhere(dailyPojo.getStartDate(), sql, args,
				"\n and daily.dailydate>=?");
		this.addSqlWhere(dailyPojo.getEndDate(), sql, args,
				"\n and daily.dailydate<=?");

		sql.append("\n");
		sql.append("\n )b on a.userid=b.userid and a.yeardate=b.dailydate");
		sql.append("\n left join department f on a.comId=f.comId and a.depid=f.id");
		sql.append("\n left join(");
		sql.append("\n      select toDo.Busid,count(toDo.Busid) todonum from todayworks toDo ");
		sql.append("\n      where toDo.isclock=0 and toDo.readState=0 and toDo.Bustype="
				+ ConstantInterface.TYPE_DAILY + " ");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n   and toDo.Userid=?");
		sql.append("\n      group by toDo.Busid");
		sql.append("\n ) toDo on b.id=toDo.busid ");
		sql.append("\n where 1=1  ");

		// 非督察人员
		if (!isForceInPersion) {
			sql.append("\n and( ");

			// 负责人上级权限验证
			sql.append("\n exists( \n");
			sql.append(" 	select sup.* from myLeaders sup where sup.comId=a.comId and ?=sup.leader and creator=a.userId\n");
			sql.append(" )\n");
			args.add(userInfo.getId());

			// 分享人员自己
			sql.append("\n or a.userid=? \n");
			args.add(userInfo.getId());

			// 被@的人
			sql.append("\n	or exists (select dailyId from dailyShareUser where comId = ? and userid = ? and b.id = dailyId)");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());

			// 分享范围
			sql.append("\n  or exists (");
			sql.append("\n select a.id from dailyShareGroup a left join grouppersion c on c.grpId = a.grpid and c.comid = a.comid where a.dailyId = b.id and a.comid = ? and c.userinfoid = ?");
			sql.append("\n)");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());

			// 分享范围为所有人
			sql.append("\n  or b.scopeType = 0");

			sql.append(" ) \n");
		}

		// 负责人类型
		String dailierType = dailyPojo.getDailierType();
		if (null != dailierType && !"".equals(dailierType)) {
			if ("0".equals(dailierType)) {// 查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=?");
			} else if ("1".equals(dailierType)) {// 查询下属的
				// 负责人上级权限验证
				sql.append(" and exists( \n");
				sql.append(" 	select sup.* from myLeaders sup where sup.comId=a.comId and ?=sup.leader and creator=a.userId\n");
				sql.append("  and sup.leader<>creator\n");
				sql.append(" )\n");
				args.add(userInfo.getId());
			}
		}

		// 汇报人员筛选
		if (null != dailyPojo.getDailierId() && dailyPojo.getDailierId() != 0) {// 单个
			sql.append("and a.userId=? \n");
			args.add(dailyPojo.getDailierId());
		}

		if (null != dailyPojo.getListOwner()
				&& !dailyPojo.getListOwner().isEmpty()) {// 一群
			sql.append("	 and  (  a.userId = 0 ");
			for (UserInfo owner : dailyPojo.getListOwner()) {
				sql.append("or a.userId = ?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}

		// 部门筛选
		this.addSqlWhereIn(dailyPojo.getListTreeDeps(), sql, args,
				"\n and a.depid in ?");

		// 分享名称筛选
		this.addSqlWhereLike(dailyPojo.getDailyName(), sql, args,
				" and a.dailyName like ?");
		return this
				.pagedQuery(sql.toString(), "a.yeardate desc,a.depid,a.userid",
						args.toArray(), Daily.class);

	}

	/**
	 * 公司的分享模板
	 * 
	 * @param comId
	 * @return
	 */
	public DailyMod getDailyMod(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from DailyMod where comId = ? ");
		args.add(comId);
		return (DailyMod) this.objectQuery(sql.toString(), args.toArray(),
				DailyMod.class);
	}

	/**
	 * 取得所选日期所写分享
	 * 
	 * @param comId
	 * @param reporterId
	 * @return
	 */
	public Daily getDaily(Integer comId, Integer reporterId, String chooseDate) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,(select count(*) from  dailyVal b where a.comId=b.comId and a.id=b.dailyId) as countVal,");
		sql.append("\n case when a.scopeType = 0 then '所有同事' when  a.scopeType = 2 then '我自己' else '自定义' end as scopeStr");
		sql.append("\n from daily a");
		sql.append("\n where 1 = 1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");// 企业
		this.addSqlWhere(reporterId, sql, args, " and a.reporterId=?");// 汇报人
		this.addSqlWhere(chooseDate, sql, args, "  and a.dailyDate = ?");
		return (Daily) this.objectQuery(sql.toString(), args.toArray(),
				Daily.class);
	}

	/**
	 * 取得所选日期所写分享
	 * 
	 * @param comId
	 * @param reporterId
	 * @return
	 */
	public Daily getMsgShareExitst(Integer comId, Integer reporterId,
			String chooseDate) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,(select count(*) from  dailyVal b where a.comId=b.comId and a.id=b.dailyId) as countVal,");
		sql.append("\n case when a.scopeType = 0 then '所有同事' when  a.scopeType = 2 then '自己' else '自定义' end as scopeStr");
		sql.append("\n from daily a");
		sql.append("\n where 1 = 1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");// 企业
		this.addSqlWhere(reporterId, sql, args, " and a.reporterId=?");// 汇报人
		this.addSqlWhere(chooseDate, sql, args, "  and a.dailyDate = ?");
		return (Daily) this.objectQuery(sql.toString(), args.toArray(),
				Daily.class);
	}

	/**
	 * 取得所选日期所写分享
	 * 
	 * @param comId
	 * @return
	 */
	public Daily getDaily(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.userName,(select count(*) from  dailyVal b where a.comId=b.comId and a.id=b.dailyId) as countVal ");
		sql.append("\n from daily a left join userinfo b on  a.reporterId=b.id where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");// 企业
		this.addSqlWhere(id, sql, args, " and a.Id=?");// 分享主键
		return (Daily) this.objectQuery(sql.toString(), args.toArray(),
				Daily.class);
	}

	/**
	 * 取得所选日期所写分享
	 * 
	 * @param id
	 *            分享主键
	 * @param userInfo
	 *            当前操作人员
	 * @param daily
	 *            分享条件
	 * @param isForceInPersion
	 *            是否强制参与人
	 * @return
	 * @throws ParseException
	 */
	public Daily getDailyForView(Integer id, UserInfo userInfo,
			DailyPojo daily, boolean isForceInPersion) throws ParseException {
		List<Object> args = new ArrayList<>();
		StringBuffer sql = new StringBuffer();

		sql.append("select * from (");
		sql.append("\n  select a.* from ( ");
		sql.append("\n  select a.*,b.userName,b.gender,picF.Uuid,picF.Filename as ImgName,c.depName,aa.depId,");
		sql.append("\n  (");
		sql.append("\n  select count(*) from  dailyQ b where a.comId=b.comId and a.id=b.dailyId");
		sql.append("\n  ) as countQues, ");
		sql.append("\n  (");
		sql.append("\n  select count(*) from  dailyVal b where a.comId=b.comId and a.id=b.dailyId");
		sql.append("\n  ) as countVal, ");
		sql.append("\n  case when today.id is null then ");
		sql.append("\n  case when(");
		sql.append("\n  select count(*) from ");
		sql.append("\n  todayworks today where c.comId = today.comId and c.id = today.busid ");
		sql.append("\n  and today.bustype=" + ConstantInterface.TYPE_DAILY
				+ " and today.userId=? and today.isclock=0");
		args.add(userInfo.getId());
		sql.append("\n  and today.readState=0");
		sql.append("\n  )=0 then 3 else 1 end when  today.readstate=0  then 0 when  today.readstate=1  then 2 end as isread,");
		sql.append("\n  case when atten.id is null then 0 else 1 end as attentionState");
		sql.append("\n  from daily a inner join userinfo b on a.ReporterId=b.id  ");
		sql.append("\n  inner join userOrganic aa on aa.userId=b.id and a.comId=aa.comId  ");
		sql.append("\n  inner join department c on aa.depId=c.id and aa.comId=c.comId  ");
		sql.append("\n  left join upfiles picF on  aa.smallheadportrait = picF.id ");
		sql.append("\n  left join todayworks today on a.comId = today.comId and a.id = today.busid and today.busspec=1 and today.bustype="
				+ ConstantInterface.TYPE_DAILY + " and today.userId=? ");
		args.add(userInfo.getId());
		// 关注
		sql.append("\n  left join attention atten on a.id = atten.busid and atten.userid = ? and atten.comid = ? and atten.bustype = "
				+ ConstantInterface.TYPE_DAILY + "");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());

		// 已发布分享
		sql.append("\n  where a.state=0");

		// 公司筛选
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");

		// 非督察人员
		if (!isForceInPersion) {
			sql.append("\n and( ");
			// 负责人上级权限验证
			sql.append(" exists(select id from myLeaders where creator = aa.userid and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			// 分享人员自己
			sql.append(" or a.ReporterId=? \n");
			args.add(userInfo.getId());
			// 被@的人
			sql.append("\n	or exists (select dailyId from dailyShareUser where comId = ? and userid = ? and a.id = dailyId)");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			// 分享范围
			sql.append("\n  or exists (");
			sql.append("\n select b.id from dailyShareGroup b left join grouppersion c on c.grpId = b.grpid and c.comid = b.comid where a.id = b.dailyId and b.comid = ? and c.userinfoid = ?");
			sql.append("\n)");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			// 公开
			sql.append(" or a.scopeType = 0");

			sql.append(" ) \n");
		}

		sql.append("\n  ) a where a.state=0 ");

		// 公司过滤
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");

		// 分享名称筛选
		this.addSqlWhereLike(daily.getDailyName(), sql, args,
				" and a.weekRepName like ?");

		// 汇报人员筛选
		if (null != daily.getDailierId() && daily.getDailierId() != 0) {
			sql.append("and a.ReporterId=? \n");
			args.add(daily.getDailierId());
		}

		// 时段查询
		this.addSqlWhere(daily.getStartDate(), sql, args,
				"\n  and a.dailyDate >= ?");
		this.addSqlWhere(daily.getEndDate(), sql, args,
				"\n  and a.dailyDate <= ?");

		// 负责人类型
		String dailierType = daily.getDailierType();
		if (null != dailierType && !"".equals(dailierType)) {
			if ("0".equals(dailierType)) {// 查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args,
						" and a.ReporterId=?");
			} else if ("1".equals(dailierType)) {// 查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.ReporterId and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}

		// 排序
		sql.append("order by a.isread,a.dailyDate desc,a.depId,a.reporterId\n");

		sql.append("\n) a ");
		sql.append("\n  where 1=1");

		// 分享主键
		this.addSqlWhere(id, sql, args, "\n  and a.id = ?");
		return (Daily) this.objectQuery(sql.toString(), args.toArray(),
				Daily.class);
	}

	/**
	 * 分享内容
	 * 
	 * @param dailyId
	 *            分享
	 * @param comId
	 *            企业
	 * @param reporterId
	 *            汇报人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyQ> listDailyQ(Integer dailyId, Integer comId,
			Integer reporterId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.dailyValue as dailyval from dailyQ a ");
		sql.append("\n left join daily c on c.id=a.dailyId and a.comId=c.comId ");
		sql.append("\n left join dailyVal b on a.comId=b.comId and a.dailyId = b.dailyId");
		sql.append("\n and a.id = b.questionId where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");// 企业
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");// 所属分享
		this.addSqlWhere(reporterId, sql, args, " and c.reporterId=?");// 汇报人
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), DailyQ.class);
	}

	/**
	 * 今日计划
	 * 
	 * @param dailyId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyPlan> listDailyPlan(Integer dailyId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from dailyPlan a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");// 企业
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");// 所属分享
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), DailyPlan.class);
	}

	/**
	 * 取得初始化模板条目
	 * 
	 * @param comId
	 * @param memberId
	 * @param depId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyModContent> initDailyContent(Integer comId,
			Integer memberId, Integer depId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.modcontent,a.isRequire,a.sysState from(");
		// 团队级
		sql.append("\n		select a.* from dailyModContent a where a.dailylev=1 and a.hidestate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n		union all");
		// 部门级
		sql.append("\n select a.* from dailyModContent a where a.dailylev=2 and a.hidestate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n and a.id in (");
		sql.append("\n   select b.modcontid from　dailyModcontdep b where b.modcontid=a.id and a.comId=b.comId and a.modid=b.modid");
		sql.append("\n    and b.depId in (");
		sql.append("\n    select a.id from department a  where 1=1 and a.enabled=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(depId, sql, args, " start with a.id=?");
		sql.append("\n		 CONNECT BY PRIOR  a.parentid=a.id");
		sql.append("\n    )");
		sql.append("\n)");

		sql.append("\n		union all");
		// 成员级
		sql.append("\n		select a.* from dailyModContent a where a.dailylev=3 and a.hidestate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");

		sql.append("\n and a.id in (");
		sql.append("\n		select b.modcontid from dailyModContMember b where a.comId=b.comId and a.modid=b.modid");
		this.addSqlWhere(memberId, sql, args, " and b.memberid=?");
		sql.append("\n		)");

		sql.append("\n		) a where 1=1 order by a.dailylev asc,a.id asc");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyModContent.class);
	}

	/**
	 * 查询默认的日报条目
	 * 
	 * @param comId
	 * @return
	 */
	public List<DailyModContent> queryDailySysContent(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*  from dailyModContent a  ");
		sql.append("\n where a.sysstate=1 and a.comId=? ");
		args.add(comId);
		sql.append("\n order by a.dailylev asc,a.id asc");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyModContent.class);
	}

	/**
	 * 取得所选日期所写分享
	 * 
	 * @param comId
	 * @param reporterId
	 * @return
	 */
	public Daily getDailyQ(Integer comId, Integer reporterId, String chooseDate) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.*,(select count(*) from  dailyQ b where a.comId=b.comId and a.id=b.dailyId) as countQues,");
		// 还没有填写
		sql.append("\n  0 as countVal");
		sql.append("\n  from daily a where 1=1");
		// 企业
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		// 汇报人
		this.addSqlWhere(reporterId, sql, args, " and a.reporterId=?");
		// 分享日期
		this.addSqlWhere(chooseDate, sql, args, "  and a.dailyDate = ?");
		return (Daily) this.objectQuery(sql.toString(), args.toArray(),
				Daily.class);
	}

	/**
	 * 分享附件
	 * 
	 * @param dailyId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyUpfiles> listDailyFiles(Integer dailyId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comId,a.dailyId,a.upfileId,b.filename,a.recordCreateTime as upTime,c.userName,c.gender,d.uuid,d.fileName as imgName, ");
		sql.append("\n  f.fileExt,f.uuid as fileUuid,f.fileName ");
		sql.append("\n  from dailyUpfiles a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id  ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  left join upfiles f on a.upfileId=f.id and a.comId=f.comId");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyUpfiles.class);
	}

	/**
	 * 分享附件
	 * 
	 * @param dailyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyShareGroup> listDailyShareGroup(Integer dailyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.*,c.grpname ");
		sql.append("\n from daily a ");
		sql.append("\n right join dailysharegroup b on a.id = b.dailyid ");
		sql.append("\n left join selfgroup c on b.grpid = c.id ");
		sql.append("\n where a.id = ?");
		args.add(dailyId);
		return this.listQuery(sql.toString(), args.toArray(),
				DailyShareGroup.class);
	}

	/**
	 * 分享附件
	 * 
	 * @param comId
	 * @param dailyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyUpfiles> listPagedDailyFiles(Integer comId, Integer dailyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select * from ( ");
		sql.append("\n  select a.id,a.comId,a.dailyId,a.upfileId,b.filename,b.uuid as fileUuid,a.recordCreateTime as upTime,'daily' type,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId ");
		sql.append("\n  from dailyUpfiles a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on  cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  union all ");
		sql.append("\n  select a.id,a.comId,a.dailyId,a.upfileId,b.filename,b.uuid as fileUuid,a.recordCreateTime as upTime,'talk' type,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId ");
		sql.append("\n  from dailyTalkFile a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on  cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n )a ");
		return this.pagedQuery(sql.toString(), " a.upTime desc,a.id",
				args.toArray(), DailyUpfiles.class);
	}

	/**
	 * 查询附件总数
	 * 
	 * @param comId
	 * @param dailyId
	 * @return
	 */
	public Integer countFile(Integer comId, Integer dailyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select count(1) from ( ");
		sql.append("\n  select a.id,a.comId,a.dailyId,a.upfileId,b.filename,b.uuid as fileUuid,a.recordCreateTime as upTime,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId ");
		sql.append("\n  from dailyUpfiles a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on  cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  union all ");
		sql.append("\n  select a.id,a.comId,a.dailyId,a.upfileId,b.filename,b.uuid as fileUuid,a.recordCreateTime as upTime,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId ");
		sql.append("\n  from dailyTalkFile a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on  cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n )a ");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 获取个人权限下的所有分享（不分页）
	 * 
	 * @param daily
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<Daily> listDailyOfAll(DailyPojo daily, UserInfo userInfo,
			boolean isForceInPersion) throws ParseException {
		// 本日汇报情况
		String dailyDoneState = daily.getDailyDoneState();
		// 查询时间起
		String dailyS = daily.getStartDate();
		// 查询时间止
		String dailyE = daily.getEndDate();
		// 本日所在日数
		Integer dailyNum = null;
		// 日数所在年
		String dailyYear = null;
		// if(null!=dailyDoneState && !"".equals(dailyDoneState)){//需要已汇报或是未汇报
		// //当前时间
		// String nowDate =
		// DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
		// //本日第一天
		// dailyS = DateTimeUtil.getFirstDayOfDaily(nowDate,
		// DateTimeUtil.c_yyyy_MM_dd_);
		// //本日最后一天
		// dailyE = DateTimeUtil.getLastDayOfDaily(nowDate,
		// DateTimeUtil.c_yyyy_MM_dd_);
		//
		// //日期所在日数
		// dailyNum =
		// DateTimeUtil.getDailyOfYear(nowDate,DateTimeUtil.c_yyyy_MM_dd_);
		// //当前日坐在年
		// String dailysYear = dailyS.substring(0, 4);
		// String dailyEYear = dailyE.substring(0, 4);
		// dailyYear = dailysYear;
		// if(!dailysYear.equals(dailyEYear)){
		// dailyYear = dailyEYear;
		// }
		//
		// }else{
		// if (dailyS != null && !StringUtil.isBlank(dailyS.toString())) {
		// dailyS = DateTimeUtil.getFirstDayOfDaily(dailyS,
		// DateTimeUtil.c_yyyy_MM_dd_);
		// }
		// if(dailyE != null && !StringUtil.isBlank(dailyE.toString())){
		// dailyE = DateTimeUtil.getLastDayOfDaily(dailyE,
		// DateTimeUtil.c_yyyy_MM_dd_);
		// }
		// }

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select * from ( \n");

		sql.append("\n select a.*,b.userid reporterId,f.depname,d.username,case when c.id is null then 0 else c.id end id,");
		sql.append("\n case when c.dailyname is null then d.username||'日总结_'||a.year||'年第'||a.dailynum||'日'");
		sql.append("\n else c.dailyname end dailyname,c.recordCreateTime,");
		sql.append("\n case when c.state is null then '1' else c.state end state,");

		sql.append(" case when today.id is null then \n");
		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where c.comId = today.comId and c.id = today.busid \n");
		sql.append(" and today.bustype='" + ConstantInterface.TYPE_WEEK
				+ "' and today.userId=? and today.isclock=0\n");
		args.add(userInfo.getId());
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 3 else 1 end \n");
		sql.append("  when  today.readstate=0  then 0 \n");
		sql.append("  when  today.readstate=1  then 2 \n");
		sql.append(" end as isread,\n");

		sql.append("\n d.gender,g.uuid,g.filename as imgName,b.depId ");
		sql.append("\n from (");
		if (null != dailyNum && null != dailyYear) {
			// 选出企业中已发布分享的日数，年份，日数起止时间
			sql.append("\n select " + userInfo.getComId() + " comId,"
					+ dailyNum + " dailynum," + dailyYear + " year,");
			sql.append("\n '" + dailyS + "' dailys, '" + dailyE
					+ "' dailye  from dual");
		} else {
			// 选出企业中已发布分享的日数，年份，日数起止时间
			sql.append("\n select distinct a.comId,a.dailynum,a.year,a.dailys,a.dailye  from daily a where a.comId=? and a.state=0");
			args.add(userInfo.getComId());
			sql.append("\n order by a.dailynum desc ,a.year");
		}

		sql.append("\n )a");
		// 关联企业成员
		sql.append("\n left join userorganic b on a.comId=b.comId and b.enabled=1");
		// 企业成员是否发布分享
		sql.append("\n left join daily c on a.comId=c.comId and a.dailynum=c.dailynum and a.year=c.year and b.userid=c.reporterid");
		// 成员基本信息
		sql.append("\n left join userinfo d on b.userid=d.id");
		// 成员所以在部门
		sql.append("\n left join department f on b.comId=f.comId and b.depid=f.id");
		// 成员头像
		sql.append("\n left join upfiles g on  b.mediumHeadPortrait=g.id");
		// 操作人员的待办事项或是未读提醒
		sql.append("\n left join todayworks today on a.comId = today.comId and c.id = today.busid and today.busspec=1");
		sql.append("\n and today.bustype='" + ConstantInterface.TYPE_WEEK
				+ "' and today.userId=? ");
		args.add(userInfo.getId());
		sql.append("\n where 1=1 ");

		// 查询部门的
		if (null != daily.getDepId()) {
			sql.append("and f.id in ( select id from department \n");
			sql.append("start with id=" + daily.getDepId()
					+ " connect by prior id = parentid \n");
			sql.append(")\n");
		}

		// 非督察人员
		if (!isForceInPersion) {
			sql.append("\n and( ");
			// //指定范围的
			// sql.append("\n 	 exists( ");
			// sql.append("\n  	select * from dailyViewer b where "+userInfo.getId()+"=b.viewerId and b.userid=c.ReporterId and a.comId=b.comId ");
			// sql.append("\n 	 ) ");
			// 负责人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = b.userId and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			// 分享人员自己
			sql.append(" or c.ReporterId=? \n");
			args.add(userInfo.getId());
			// 被@的人
			// sql.append("\n	or c.id in (select dailyId from dailyShareUser where comId = ? and userid = ?)");
			sql.append("\n	or exists (select dailyId from dailyShareUser where comId = ? and userid = ? and c.id = dailyId)");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			// 分享范围
			sql.append("\n  or exists (");
			sql.append("\n select a.id from dailyShareGroup a left join grouppersion b on c.grpId = a.grpid and b.comid = a.comid where a.dailyId = c.id and a.comid = ? and b.userinfoid = ?");
			sql.append("\n)");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(" ) \n");
		}

		sql.append(") reports where 1=1 \n");
		this.addSqlWhereLike(daily.getDailyName(), sql, args,
				" and dailyName like ?");
		// 汇报人员筛选
		if (null != daily.getDailierId() && daily.getDailierId() != 0) {
			sql.append("and reports.ReporterId=? \n");
			args.add(daily.getDailierId());
		}

		// 查询时间起
		this.addSqlWhere(dailyS, sql, args, " and reports.dailyS>=?");
		// 查询时间止
		this.addSqlWhere(dailyE, sql, args, " and reports.dailyE<=?");

		// 负责人类型
		String dailyerType = daily.getDailierType();
		if (null != dailyerType && !"".equals(dailyerType)) {
			if ("0".equals(dailyerType)) {// 查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args,
						" and reports.ReporterId=?");
			} else if ("1".equals(dailyerType)) {// 查询下属的
				sql.append(" and exists(select id from myLeaders where creator = reports.ReporterId and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		// 查询本日汇报情况
		this.addSqlWhere(daily.getDailyDoneState(), sql, args,
				" and reports.state=?");
		sql.append("order by reports.isread,reports.year desc,reports.dailyNum desc,reports.depId,reports.reporterId \n");
		return this.listQuery(sql.toString(), args.toArray(), Daily.class);

	}

	/**
	 * 分享发布情况统计
	 * 
	 * @param daily
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<Daily> listDailyStatistics(DailyPojo daily, UserInfo userInfo,
			boolean isForceInPersion) throws ParseException {

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("\n select * from(");
		sql.append("\n select reports.*,f.depname,");
		sql.append("\n case when reports.createDate is null then 0");
		sql.append("\n 	when TO_date(reports.createDate,'yyyy-mm-dd')+reports.timeType <= TO_date(reports.dailye,'yyyy\"年\"mm\"月\"dd\"日\"') then 1");
		sql.append("\n 	else 2 end submitState 	  from ( ");
		sql.append("\n 		select  a.*,b.userid reporterId,d.gender,b.depId,d.username,substr(b.recordcreatetime,0,10) registInTime,");
		sql.append("\n 			  case when c.state =0 then substr(c.recordcreatetime,0,10) else '' end createDate,");
		sql.append("\n 		case when s.timeType is null  then '-1' else s.timeType end timeType");

		sql.append("\n 		from (");
		// 选出企业中已发布分享的日数，年份，日数起止时间
		sql.append("\n select a.comId,a.dailynum,a.year,a.dailys,a.dailye  from daily a where a.comId=? and a.state=0");
		args.add(userInfo.getComId());
		sql.append("\n group by a.comId,a.dailynum,a.year,a.dailys,a.dailye ");

		sql.append("\n )a");

		sql.append("\n 	left join subtimeset s on  s.comId=a.comId");
		sql.append("\n	left join userorganic b on a.comId=b.comId and b.enabled=1  ");
		sql.append("\n	left join daily c on a.comId=c.comId and a.dailynum=c.dailynum and a.year=c.year and b.userid=c.reporterid");
		sql.append("\n 	left join userinfo d on b.userid=d.id");

        sql.append("\n 	left join subtimeset s on  s.comId=a.comId");
        sql.append("\n	left join userorganic b on a.comId=b.comId and b.enabled=1 and b.INSERVICE =1 ");
        sql.append("\n	left join daily c on a.comId=c.comId and a.dailynum=c.dailynum and a.year=c.year and b.userid=c.reporterid");
        sql.append("\n 	left join userinfo d on b.userid=d.id");

		sql.append("\n where 1=1 \n");
		// 分享时间范围小于人员入职时间，不查询
		sql.append("\n  and TO_date(reports.dailye, 'yyyy\"年\"mm\"月\"dd\"日\"')>=TO_date(reports.registInTime, 'yyyy-mm-dd')");

		// 查询部门的
		if (null != daily.getDepId()) {
			sql.append("and f.id in ( select id from department \n");
			sql.append("start with id=" + daily.getDepId()
					+ " connect by prior id = parentid \n");
			sql.append(")\n");
		}

		// 部门查询
		this.addSqlWhereIn(daily.getListTreeDeps(), sql, args,
				"\n and f.id in ?");

		// 汇报人员筛选
		if (null != daily.getDailierId() && daily.getDailierId() != 0) {
			sql.append("and reports.ReporterId=? \n");
			args.add(daily.getDailierId());
		}
		if (null != daily.getListOwner() && !daily.getListOwner().isEmpty()) {
			sql.append("	 and  (  reports.ReporterId = 0 ");
			for (UserInfo owner : daily.getListOwner()) {
				sql.append("or reports.ReporterId = ?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		// 查询时间起
		this.addSqlWhere(daily.getStartDate(), sql, args,
				" and reports.dailyS>=?");
		// 查询时间止
		this.addSqlWhere(daily.getEndDate(), sql, args,
				" and reports.dailyE<=?");

		this.addSqlWhere(daily.getDailyYear(), sql, args,
				" and reports.year =?");
		if (null != daily.getDailyNum()) {// 不限日数条件
			this.addSqlWhere(daily.getDailyNum(), sql, args,
					" and reports.dailyNum =?");
		}
		sql.append("	 )a where 1=1 ");
		// 发布状态
		this.addSqlWhere(daily.getSubmitState(), sql, args,
				" and a.submitState =?");
		return this
				.pagedQuery(
						sql.toString(),
						"a.year desc,a.dailyNum desc,a.depId asc,a.reporterId asc,submitState desc ",
						args.toArray(), Daily.class);
	}

	/**
	 * 获取团队分享主键集合
	 * 
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Daily> listDailyOfAll(UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id from daily a where a.comId=?");
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(), Daily.class);
	}

	/**
	 * 获取本日的满足查询条件的数目
	 * 
	 * @param dailyDoneState
	 *            0本日已汇报 1本日未汇报
	 * @param userInfo
	 *            当前操作人员
	 * @param isForceInPersion
	 *            是否强制参与人
	 * @return
	 * @throws ParseException
	 */
	// public Integer getDailyCount(String dailyDoneState,
	// UserInfo userInfo, boolean isForceInPersion) throws ParseException {
	// //当前时间
	// String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
	// //查询时间起
	// String dailyS = DateTimeUtil.getFirstDayOfDaily(nowDate,
	// DateTimeUtil.c_yyyy_MM_dd_);
	// //查询时间止
	// String dailyE = DateTimeUtil.getLastDayOfDaily(nowDate,
	// DateTimeUtil.c_yyyy_MM_dd_);
	//
	// //日期所在日数
	// Integer dailyNum =
	// DateTimeUtil.getDailyOfYear(nowDate,DateTimeUtil.c_yyyy_MM_dd_);
	//
	// //当前日坐在年
	// String dailysYear = dailyS.substring(0, 4);
	// String dailyEYear = dailyE.substring(0, 4);
	// String dailyYear = dailysYear;
	// if(!dailysYear.equals(dailyEYear)){
	// dailyYear = dailyEYear;
	// }
	//
	// List<Object> args = new ArrayList<Object>();
	// StringBuffer sql = new StringBuffer();
	//
	// sql.append("select count(reports.id) from ( \n");
	//
	// sql.append("\n select a.*,b.userid reporterId,d.username,case when c.id is null then 0 else c.id end id,");
	// sql.append("\n case when c.dailyname is null then d.username||'日总结_'||a.year||'年第'||a.dailynum||'日'");
	// sql.append("\n else c.dailyname end dailyname,c.recordCreateTime,");
	// sql.append("\n case when c.state is null then '1' else c.state end state");
	// sql.append("\n from (");
	// //选出企业中已发布分享的日数，年份，日数起止时间
	// sql.append("\n select "+userInfo.getComId()+" comId,"+dailyNum+" dailynum,"+dailyYear+" year,");
	// sql.append("\n '"+dailyS+"' dailys, '"+dailyE+"' dailye  from dual");
	//
	// sql.append("\n )a");
	// //关联企业成员
	// sql.append("\n left join userorganic b on a.comId=b.comId and b.enabled=1");
	// //企业成员是否发布分享
	// sql.append("\n left join daily c on a.comId=c.comId and a.dailynum=c.dailynum and a.year=c.year and b.userid=c.reporterid");
	// //成员基本信息
	// sql.append("\n left join userinfo d on b.userid=d.id");
	// sql.append("\n where 1=1 ");
	//
	// //非督察人员
	// if(!isForceInPersion){
	// sql.append("\n and( ");
	// //指定范围的
	// sql.append("\n 	 exists( ");
	// sql.append("\n  	select * from dailyViewer b where "+userInfo.getId()+"=b.viewerId and b.userid=c.ReporterId and a.comId=b.comId ");
	// sql.append("\n 	 ) ");
	// //负责人上级权限验证
	// sql.append(" or exists(select id from myLeaders where creator = b.userId and comId = ? and leader = ? and leader <> creator )");
	// 	args.add(userInfo.getComId());
	// 	args.add(userInfo.getId());
	// //分享人员自己
	// sql.append(" or b.userid=? \n");
	// args.add(userInfo.getId());
	// //被@的人
	// //
	// sql.append("\n	or c.id in (select dailyId from dailyShareUser where comId = ? and userid = ?)");
	// sql.append("\n	or exists (select dailyId from dailyShareUser where comId = ? and userid = ? and c.id = dailyId)");
	// args.add(userInfo.getComId());
	// args.add(userInfo.getId());
	// sql.append(" ) \n");
	// }
	//
	// sql.append(") reports where 1=1 \n");
	// //查询时间起
	// this.addSqlWhere(dailyS, sql, args, " and reports.dailyS>=?");
	// //查询时间止
	// this.addSqlWhere(dailyE, sql, args, " and reports.dailyE<=?");
	//
	// //查询本日汇报情况
	// this.addSqlWhere(dailyDoneState, sql, args, " and reports.state=?");
	//
	// return this.countQuery(sql.toString(), args.toArray());
	//
	// }

	/**
	 * 反馈留言
	 * 
	 * @param dailyId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyTalk> listPagedDailyTalk(Integer dailyId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comId,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from dailyTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join dailyTalk e on a.parentid=e.id and a.comId=e.comId");
		sql.append("\n where 1=1");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.pagedQuery(sql.toString(), null, args.toArray(),
				DailyTalk.class);
	}

	/**
	 * 留言总数
	 * 
	 * @param dailyId
	 * @param comId
	 * @return
	 */
	public Integer countTalk(Integer dailyId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(1) from (");
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comId,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from dailyTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join dailyTalk e on a.parentid=e.id and a.comId=e.comId");
		sql.append("\n where 1=1");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		sql.append("\n )");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 分享日志
	 * 
	 * @param dailyId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyLog> listPagedVoteLog(Integer dailyId, Integer comId,
			String sid) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.recordCreateTime,a.comId,a.dailyId,a.userId,a.userName,replace(a.content,'@sid','"
				+ sid + "') content, \n");
		sql.append("b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from dailyLog a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.dailyId = ? \n");
		args.add(comId);
		args.add(dailyId);
		return this.pagedQuery(sql.toString(), " a.recordcreatetime desc",
				args.toArray(), DailyLog.class);
	}

	/**
	 * 反馈留言
	 * 
	 * @param id
	 * @param comId
	 * @return
	 */
	public DailyTalk getDailyTalk(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.dailyId,a.recordCreateTime,a.id,a.parentid,a.comId,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from dailyTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comID=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join dailyTalk e on a.parentid=e.id and a.comId=e.comId");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return (DailyTalk) this.objectQuery(sql.toString(), args.toArray(),
				DailyTalk.class);
	}

	/**
	 * 将子节点提高一级
	 * 
	 * @param id
	 * @param comId
	 */
	public void updateDailyTalkParentId(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		DailyTalk dailyTalk = (DailyTalk) this.objectQuery(DailyTalk.class, id);
		if (null != dailyTalk && -1 == dailyTalk.getParentId()) {// 父节点为-1时将妻哪一个说话人设为空
			sql.append("update dailyTalk set parentId=(select c.parentid \n");
			sql.append("from dailyTalk c \n");
			sql.append("where c.id=?),ptalker=null where parentid = ? and comId = ?\n");
			args.add(id);
			args.add(id);
			args.add(comId);

		} else {// 删除自己,将子节点提高一级
			sql.append("update dailyTalk set parentId=(select c.parentid \n");
			sql.append("from dailyTalk c \n");
			sql.append("where c.id=?) where parentid = ? and comId = ? \n");
			args.add(id);
			args.add(id);
			args.add(comId);
		}
		this.excuteSql(sql.toString(), args.toArray());

	}

	/**
	 * 模板成员级条目成员
	 * 
	 * @param modContId
	 * @param comId
	 * @param modId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyModContMember> listModContMember(Integer modContId,
			Integer comId, Integer modId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.gender,b.userName as memberName,c.uuid as imgUuid, c.filename as imgName \n");
		sql.append(" from dailyModContMember a inner join userinfo b on  a.memberId=b.id \n");
		sql.append(" inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId \n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait=c.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(modContId, sql, args, " and a.modcontid=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(modId, sql, args, " and a.modId=?");
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyModContMember.class);
	}

	/**
	 * 模板部门级条目部门
	 * 
	 * @param modContId
	 * @param comId
	 * @param modId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModContDep> listModContDep(Integer modContId, Integer comId,
			Integer modId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.depname ");
		sql.append("\n from dailyModcontdep a inner join department b on a.comId=b.comId and a.depid=b.id ");
		sql.append("where 1=1 \n");
		this.addSqlWhere(modContId, sql, args, " and a.modcontid=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(modId, sql, args, " and a.modId=?");
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), ModContDep.class);
	}

	/**
	 * 需要删除的附件
	 * 
	 * @param comId
	 *            企业编号
	 * @param dailyId
	 *            分享主键
	 * @param fileIds
	 *            本次附件主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyUpfiles> listRemoveDailyFiles(Integer comId,
			Integer dailyId, String fileIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("\n  select* from ( ");
		sql.append("\n  select a.* from dailyUpfiles a  where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		sql.append("\n  minus ");
		sql.append("\n  select a.* from dailyUpfiles a  where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		sql.append("\n and a.upfileId in (" + fileIds + ")");
		sql.append("\n  ) a where 1=1 ");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyUpfiles.class);
	}

	/**
	 * 查出本次需要一处的人员
	 * 
	 * @param dailyViewerList
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyViewer> listDailyViewerForRemove(
			List<DailyViewer> dailyViewerList, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		Integer comId = userInfo.getComId();
		Integer userId = userInfo.getId();

		sql.append("\n select a.viewerId userId from dailyViewer a");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		sql.append("\n minus");
		sql.append("\n select leader as userId from myLeaders where creator <> leader and ");
		this.addSqlWhere(comId, sql, args, " and comId=?");
		this.addSqlWhere(userId, sql, args, " and creator=?");
		sql.append("\n minus");
		sql.append("\n select a.viewerId userId from dailyViewer a ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		sql.append("\n and a.viewerId in (" + userInfo.getId());
		if (null != dailyViewerList && !dailyViewerList.isEmpty()) {
			for (DailyViewer dailyViewer : dailyViewerList) {
				sql.append("," + dailyViewer.getViewerId());
			}
		}
		sql.append("\n )");
		return this
				.listQuery(sql.toString(), args.toArray(), DailyViewer.class);
	}

	/**
	 * 
	 * @param userId
	 *            操作员
	 * @param comId
	 *            企业名称
	 * @return 每一期分享的附件集合
	 */
	@SuppressWarnings("unchecked")
	public List<DailyUpfiles> listUserDailyFiles(Integer userId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from (");
		sql.append("\n select a.comId,a.dailyId,a.upfileId,a.userId,'"
				+ ConstantInterface.TYPE_WEEK + "' type ");
		sql.append("\n from dailyUpfiles a inner join daily b on a.comId=b.comId and a.dailyid=b.id");
		sql.append("\n and b.state=0 where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and b.comId=?");
		this.addSqlWhere(userId, sql, args, " and b.reporterid=?");
		sql.append("\n union all ");
		sql.append("\n select a.comId,a.dailyId,a.upfileId,a.userId,'"
				+ ConstantInterface.TYPE_WEEKTALK + "' type ");
		sql.append("\n from dailyTalkFile a inner join daily b on a.comId=b.comId and a.dailyid=b.id");
		sql.append("\n and b.state=0 where 1=1");
		this.addSqlWhere(comId, sql, args, " and b.comId=?");
		this.addSqlWhere(userId, sql, args, " and b.reporterid=?");
		sql.append("\n )a where 1=1 ");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyUpfiles.class);
	}

	/**
	 * 权限检查
	 * 
	 * @param comId
	 * @param userId
	 * @param reporterId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyViewer> authorCheck(Integer comId, Integer userId,
			Integer reporterId, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct * from ( \n");
		// sql.append("	select a.viewerId  from  dailyViewer a \n");
		// sql.append("		where a.comId = ? and a.userId =? \n");
		// args.add(comId);
		// args.add(reporterId);
		// sql.append("	union all \n");
		// 本人
		sql.append("	select " + userId + " viewerId from dual  \n");
		sql.append("	union all \n");
		sql.append("	select * from ( \n");
		sql.append("		select leader from myLeaders where creator = ? and comId = ? and creator <> leader \n");
		args.add(reporterId);
		args.add(comId);
		// @
		sql.append("	)a \n");
		sql.append("	union all \n");
		sql.append("	select userId from dailyShareUser");
		sql.append("	where comId = ? and userId = ? and dailyId = ?");
		args.add(comId);
		args.add(userId);
		args.add(busId);
		sql.append("    union all \n");
		// 分享范围
		sql.append("\n select a.id from dailyShareGroup a left join grouppersion c on c.grpId = a.grpid and c.comid = a.comid where a.comid = ? and c.userinfoid = ? and a.dailyId = ?");
		args.add(comId);
		args.add(userId);
		args.add(busId);
		sql.append(")a \n");
		return this
				.listQuery(sql.toString(), args.toArray(), DailyViewer.class);
	}

	/**
	 * 讨论的附件
	 * 
	 * @param comId
	 * @param dailyId
	 * @param talkId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyTalkFile> listDailyTalkFile(Integer comId,
			Integer dailyId, Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.dailyId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, \n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from dailyTalkFile a inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comId =? and a.dailyId = ? and a.talkId=?\n");
		args.add(comId);
		args.add(dailyId);
		args.add(talkId);
		sql.append("order by isPic asc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyTalkFile.class);
	}

	/**
	 * 待删除的讨论
	 * 
	 * @param comId
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyTalk> listDailyTalkForDel(Integer comId, Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from DailyTalk where comId=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(id);
		return this.listQuery(sql.toString(), args.toArray(), DailyTalk.class);
	}

	/**
	 * 删除反馈留言
	 * 
	 * @param id
	 * @param comId
	 */
	public void delDailyTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from dailyTalk a where a.comId =? and a.id in \n");
		sql.append("(select id from dailyTalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());

	}

	/**
	 * 获取分享计划为其创建索引
	 * 
	 * @param comId
	 * @param dailyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyPlan> listDailyPlan4Index(Integer comId, Integer dailyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.plancontent from DailyPlan a where a.comId =? and a.dailyid = ? \n");
		args.add(comId);
		args.add(dailyId);
		return this.listQuery(sql.toString(), args.toArray(), DailyPlan.class);
	}

	/**
	 * 获取分享汇报内容为其创建索引
	 * 
	 * @param comId
	 * @param dailyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyVal> listDailyVal4Index(Integer comId, Integer dailyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.dailyValue from dailyVal a where a.comId =? and a.dailyId = ? \n");
		args.add(comId);
		args.add(dailyId);
		return this.listQuery(sql.toString(), args.toArray(), DailyVal.class);
	}

	/**
	 * 查询具体条目的值
	 * 
	 * @param comId
	 * @param dailyId
	 * @param questionId
	 * @return
	 */
	public DailyVal queryDailyVal(Integer comId, Integer dailyId,
			Integer questionId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.dailyValue from dailyVal a where a.comId =? and a.dailyId = ? and questionId=? \n");
		args.add(comId);
		args.add(dailyId);
		args.add(questionId);
		return (DailyVal) this.objectQuery(sql.toString(), args.toArray(),
				DailyVal.class);
	}

	/**
	 * 获取反馈留言为其创建索引
	 * 
	 * @param comId
	 * @param dailyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyTalk> listDailyTalk4Index(Integer comId, Integer dailyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.content,b.username as talkerName from dailyTalk a \n");
		sql.append("inner join userInfo b on a.talker = b.id \n");
		sql.append("where a.comId =? and a.dailyid = ? \n");
		args.add(comId);
		args.add(dailyId);
		return this.listQuery(sql.toString(), args.toArray(), DailyTalk.class);
	}

	/**
	 * 分享查看人
	 * 
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listDailyViewUser(Integer comId, Integer dailyId,
			Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		// //汇报对象和设置可查看的人
		// sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName from dailyViewer b ");
		// sql.append("\n inner join userInfo c on b.viewerId=c.id ");
		// sql.append("\n inner join userorganic d on b.comId=d.comId and c.id=d.userid and d.enabled=1 ");
		// sql.append("\n where 1=1 ");
		// this.addSqlWhere(comId, sql, args, "  and b.comId=?");
		// this.addSqlWhere(userId, sql, args, "  and b.userid=?");
		// //上级
		// sql.append("\n union all");
		// sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName from immediatesuper b ");
		// sql.append("\n inner join userInfo c on b.leader=c.id ");
		// sql.append("\n inner join userorganic d on b.comId=d.comId and c.id=d.userid and d.enabled=1 ");
		// sql.append("\n where 1=1 ");
		// this.addSqlWhere(comId, sql, args, "  and b.comId=?");
		// this.addSqlWhere(userId, sql, args, "  and b.creator=?");

		// 上级
		// sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName from immediatesuper b ");
		// sql.append("\n inner join userInfo c on b.leader=c.id ");
		// sql.append("\n inner join userorganic d on b.comId=d.comId and c.id=d.userid and d.enabled=1 ");
		// sql.append("\n where 1=1 ");
		// this.addSqlWhere(comId, sql, args, "  and b.comId=?");
		// this.addSqlWhere(userId, sql, args, " and b.creator=?");
		// sql.append("\n union all");
		// @人
		sql.append("\n  select b.id,b.email,b.wechat,b.qq,b.userName,c.comId from dailyShareUser a");
		sql.append("\n  left join userInfo b on a.userId = b.id");
		sql.append("\n  left join userOrganic c on b.id = c.userId");
		sql.append("\n  where 1=1");
		this.addSqlWhere(comId, sql, args, "  and c.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId = ?");
		sql.append("\n union all");
		// 分享组
		sql.append("\n  select c.id,c.email,c.wechat,c.qq,c.userName,d.comId from grouppersion a ");
		sql.append("\n  left join dailyShareGroup b on a.grpId = b.grpId and a.comId = b.comId ");
		sql.append("\n  left join userInfo c on a.userInfoId = c.id");
		sql.append("\n  left join userOrganic d on c.id = d.userId and d.comId = ?");
		args.add(comId);
		sql.append("\n  where 1=1");
		this.addSqlWhere(comId, sql, args, "  and b.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and b.dailyId = ?");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 分享查看人
	 * 
	 * @param comId
	 * @param userId
	 * @return
	 */
	public UserInfo getDailySelf(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName  from userInfo c");
		sql.append("\n inner join userorganic d on c.id=d.userid and d.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, "  and d.comId=?");
		this.addSqlWhere(userId, sql, args, "  and d.userid=?");
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(),
				UserInfo.class);
	}

	/**
	 * 获取督察人员集合
	 * 
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ForceInPersion> forceInPersionForDaily(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.username as sharerName,b.gender,c.uuid,c.filename \n");
		sql.append("from forceInPersion a \n");
		sql.append("inner join userInfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId = bb.comId \n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id \n");
		sql.append("where a.comId=? and a.type=? \n");
		args.add(comId);
		args.add(ConstantInterface.TYPE_WEEK);
		return this.listQuery(sql.toString(), args.toArray(),
				ForceInPersion.class);
	}

	/**
	 * 自己已发布的分享集合
	 * 
	 * @param userId
	 *            当前操作人员主键
	 * @param comId
	 *            企业号
	 * @param sendedMsg
	 *            是否发送过消息 true 取得发送过消息但有上级没有处理的 false 纯粹取已发布的
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Daily> listDaily(Integer userId, Integer comId,
			boolean sendedMsg) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from Daily a where a.state=0 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		this.addSqlWhere(userId, sql, args, " and a.reporterid=? ");
		if (sendedMsg) {
			sql.append("\n and exists(");
			sql.append("\n 		select b.* from todayworks b where b.comId=a.comId and a.id=b.busid and b.bustype='"
					+ ConstantInterface.TYPE_WEEK + "'");
			sql.append("\n 		)");
		}
		return this.listQuery(sql.toString(), args.toArray(), Daily.class);
	}

	/**
	 * 查询模块下是否有相同的附件
	 * 
	 * @param comId
	 *            企业号
	 * @param dailyId
	 *            模块主键
	 * @param upfileId
	 *            附件主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyTalkFile> listDailySimUpfiles(Integer comId,
			Integer dailyId, Integer upfileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		// 分享模块附件
		sql.append("select a.comId,a.dailyId,a.upfileId  from dailyUpfiles a \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		sql.append("\n union all \n");
		// 分享留言附件
		sql.append("select  a.comId,a.dailyId,a.upfileId from dailyTalkFile a\n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and a.dailyId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		return this.listQuery(sql.toString(), args.toArray(),
				DailyTalkFile.class);
	}

	/**
	 * 查询分享查看人员
	 * 
	 * @param sessionUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DailyViewer> listDailyViewer(UserInfo sessionUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.username viewerName,b.gender,c.uuid,c.filename \n");
		sql.append("from dailyViewer a \n");
		sql.append("inner join userInfo b on a.viewerId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId = bb.comId and bb.enabled=1\n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id \n");
		sql.append("where a.comId=? and a.userid=? \n");
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		sql.append("order by a.id asc\n");
		return this
				.listQuery(sql.toString(), args.toArray(), DailyViewer.class);
	}

	/**
	 * 查询企业提交分享时间设置
	 * 
	 * @param userInfo
	 * @return
	 */
	public SubTimeSet querySubTimeSet(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from SubTimeSet where comId = ? \n");
		args.add(userInfo.getComId());
		return (SubTimeSet) this.objectQuery(sql.toString(), args.toArray(),
				SubTimeSet.class);
	}

	/**
	 * 查询本次需要推送的人员
	 * 
	 * @param msgId
	 *            分享主键
	 * @param comId
	 *            团队号
	 * @param pushUserIdSet
	 *            本次推送的人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPushTodoUser(Integer msgId, Integer comId,
			Set<Integer> pushUserIdSet) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		// 分享的发布人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName ");
		sql.append("\n from msgShare d ");
		sql.append("\n inner join userinfo b on d.creator=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=d.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(msgId, sql, args, " and d.id=?");

		sql.append("\n union");
		// 分享爱你个的关注人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
		sql.append("\n from msgShare d inner join attention a on a.busId=d.id and d.comId=a.comId and a.busType=1");
		sql.append("\n inner join userinfo b on d.creator=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=a.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(msgId, sql, args, " and d.id=?");
		// 本次推送的人员
		if (null != pushUserIdSet && !pushUserIdSet.isEmpty()) {
			sql.append("\n union");
			sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
			sql.append("\n from userinfo b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhereIn(
					pushUserIdSet.toArray(new Integer[pushUserIdSet.size()]),
					sql, args, "\n and b.id in ?");
		}
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 查询本次需要推送的人员
	 * 
	 * @param dailyId
	 *            分享分享主键
	 * @param comId
	 *            团队号
	 * @param pushUserIdSet
	 *            本次推送的人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPushTodoDailykPortUser(Integer dailyId,
			Integer comId, Set<Integer> pushUserIdSet) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		// 分享的发布人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName ");
		sql.append("\n from daily d ");
		sql.append("\n inner join userinfo b on d.reporterId=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=d.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and d.id=?");

		sql.append("\n union");
		// 分享爱你个的关注人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
		sql.append("\n from daily d inner join attention a on a.busId=d.id and d.comId=a.comId and a.busType=1");
		sql.append("\n inner join userinfo b on d.reporterId=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=a.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and d.id=?");
		// 本次推送的人员
		if (null != pushUserIdSet && !pushUserIdSet.isEmpty()) {
			sql.append("\n union");
			sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
			sql.append("\n from userinfo b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhereIn(
					pushUserIdSet.toArray(new Integer[pushUserIdSet.size()]),
					sql, args, "\n and b.id in ?");
		}

		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 分页查询日报用于分享（仅限初始化）
	 * 
	 * @param orgNum
	 *            团队号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Daily> listPagedDailyForMsg(Integer orgNum) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.recordCreateTime,a.comid,a.reporterid,a.scopeType,a.dailydate");
		sql.append("\n from daily a ");
		sql.append("\n where a.comId=?");
		args.add(orgNum);
		return this.pagedQuery(sql.toString(), "a.id", args.toArray(),
				Daily.class);
	}

	/**
	 * 查询系统的日报数据信息
	 * 
	 * @param comId
	 *            团队号
	 * @param dailyId
	 * @return
	 */
	public List<DailyQ> queryDailySysQues(Integer comId, Integer dailyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* ");
		sql.append("\n from DailyQ a");
		sql.append("\n where a.sysState=1 and a.comId=? and a.dailyId=?");
		args.add(comId);
		args.add(dailyId);
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), DailyQ.class);
	}
}
