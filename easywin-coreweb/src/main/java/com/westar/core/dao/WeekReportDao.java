package com.westar.core.dao;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ForceInPersion;
import com.westar.base.model.ModContDep;
import com.westar.base.model.ModContMember;
import com.westar.base.model.SubTimeSet;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WeekRepLog;
import com.westar.base.model.WeekRepModContent;
import com.westar.base.model.WeekRepTalk;
import com.westar.base.model.WeekRepTalkFile;
import com.westar.base.model.WeekRepUpfiles;
import com.westar.base.model.WeekReport;
import com.westar.base.model.WeekReportMod;
import com.westar.base.model.WeekReportPlan;
import com.westar.base.model.WeekReportQ;
import com.westar.base.model.WeekReportVal;
import com.westar.base.model.WeekViewer;
import com.westar.base.pojo.WeekReportPojo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;

@Repository
public class WeekReportDao extends BaseDao {

	/**
	 * 公司的周报模板
	 * @param comId
	 * @return
	 */
	public WeekReportMod getWeekReportMod(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from WeekReportMod where comId = ? ");
		args.add(comId);
		return (WeekReportMod) this.objectQuery(sql.toString(), args.toArray(), WeekReportMod.class);
	}

	/**
	 * 模板条目
	 * @param modId 模板
	 * @param comId 公司编号
	 * @param repLev 级别 1 团队级 2 部门级 3成员级别 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepModContent> listWeekRepModContent(Integer modId,
														 Integer comId, String repLev) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append(" select a.* from weekRepModContent a where 1=1");
		this.addSqlWhere(modId, sql, args, " and a.modId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(repLev, sql, args, " and a.repLev=?");

		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), WeekRepModContent.class);
	}

	/**
	 * 取得所选日期所写周报
	 * @param weekNum
	 * @param comId
	 * @param reporterId
	 * @return
	 */
	public WeekReport getWeekReport(Integer weekNum, Integer comId,Integer reporterId,
									String year) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,(select count(*) from  weekReportVal b where a.comid=b.comid and a.id=b.weekReportId) as countVal ");
		sql.append("\n from weekReport a where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");//企业
		this.addSqlWhere(reporterId, sql, args, " and a.reporterId=?");//汇报人
		this.addSqlWhere(year, sql, args, " and a.year=?");//年
		this.addSqlWhere(weekNum, sql, args, " and a.weekNum=?");//周
		return (WeekReport) this.objectQuery(sql.toString(), args.toArray(), WeekReport.class);
	}
	/**
	 * 取得所选日期所写周报
	 * @param comId
	 * @param id
	 * @return
	 */
	public WeekReport getWeekReport(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.userName,(select count(*) from  weekReportVal b where a.comid=b.comid and a.id=b.weekReportId) as countVal ");
		sql.append("\n from weekReport a left join userinfo b on  a.reporterId=b.id where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");//企业
		this.addSqlWhere(id, sql, args, " and a.Id=?");//周报主键
		return (WeekReport) this.objectQuery(sql.toString(), args.toArray(), WeekReport.class);
	}
	/**
	 * 取得所选日期所写周报
	 * @param id 周报主键
	 * @param userInfo 当前操作人员
	 * @param weekReport 周报条件
	 * @param isForceInPersion 是否强制参与人
	 * @return
	 * @throws ParseException
	 */
	public WeekReport getWeekReportForView(Integer id, UserInfo userInfo,WeekReportPojo weekReport, boolean isForceInPersion) throws ParseException {
		//本周汇报情况
		String weekDoneState = weekReport.getWeekDoneState();
		//查询时间起
		String weekS = weekReport.getStartDate();
		//查询时间止
		String weekE = weekReport.getEndDate();
		if(null!=weekDoneState && !"".equals(weekDoneState)){//需要已汇报或是未汇报
			//当前时间
			String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
			//本周第一天
			weekS = DateTimeUtil.getFirstDayOfWeek(nowDate, DateTimeUtil.c_yyyy_MM_dd_);
			//本周最后一天
			weekE = DateTimeUtil.getLastDayOfWeek(nowDate, DateTimeUtil.c_yyyy_MM_dd_);
		}else{
			if (weekS != null && !StringUtil.isBlank(weekS.toString())) {
				weekS = DateTimeUtil.getFirstDayOfWeek(weekS, DateTimeUtil.c_yyyy_MM_dd_);
			}
			if(weekE != null && !StringUtil.isBlank(weekE.toString())){
				weekE = DateTimeUtil.getLastDayOfWeek(weekE, DateTimeUtil.c_yyyy_MM_dd_);
			}
		}

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from (");
		//对所有结果进行包含
		sql.append("select a.* from ( \n");

		sql.append("\n select a.*,b.userName,b.gender,picF.Uuid,picF.Filename as ImgName,c.depName,aa.depId,(select count(*) from  weekReportVal b where a.comid=b.comid and a.id=b.weekReportId) as countVal, ");

		sql.append("\n case when today.id is null then ");
		sql.append("\n case when(");
		sql.append("\n select count(*) from ");
		sql.append("\n todayworks today where c.comid = today.comid and c.id = today.busid ");
		sql.append("\n and today.bustype='"+ConstantInterface.TYPE_WEEK+"' and today.userId=? and today.isclock=0");
		args.add(userInfo.getId());
		sql.append("\n and today.readState=0");
		sql.append("\n )=0 then 3 else 1 end ");
		sql.append("\n  when  today.readstate=0  then 0 ");
		sql.append("\n  when  today.readstate=1  then 2 ");
		sql.append("\n end as isread");

		//周报关联企业成员基本信息
		sql.append("\n from weekReport a inner join userinfo b on a.ReporterId=b.id  ");
		//周报关联企业成员
		sql.append("\n inner join userOrganic aa on aa.userId=b.id and a.comId=aa.comId  ");
		//成员所在部门
		sql.append("\n inner join department c on aa.depId=c.id and aa.comId=c.comid  ");
		//汇报人头像信息
		sql.append("\n left join upfiles picF on  aa.smallheadportrait = picF.id ");
		//操作人员的未读提醒或是代办事项
		sql.append("\n left join todayworks today on a.comid = today.comid and a.id = today.busid and today.busspec=1 \n");
		sql.append("\n and today.bustype='"+ConstantInterface.TYPE_WEEK+"' and today.userId=? \n");
		args.add(userInfo.getId());
		//周报是已发布的
		sql.append("\n where a.state=0  ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");

		//非督察人员
		if(!isForceInPersion){
			sql.append("\n and( ");
			//指定范围的
			sql.append("\n 	 exists( ");
			sql.append("\n  	select * from weekViewer b where "+userInfo.getId()+"=b.viewerId and aa.userid=a.ReporterId and a.comId=b.comid ");
			sql.append("\n 	 ) ");
			//负责人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = aa.userid and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//周报人员自己
			sql.append(" or a.ReporterId=? \n");
			args.add(userInfo.getId());
			//被@的人
//			sql.append("\n	or a.id in (select weekReportId from weekReportShareUser where comid = ? and userid = ?)");
			sql.append("\n	or exists (select weekReportId from weekReportShareUser where comid = ? and userid = ? and a.id = weekReportId)");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(" ) \n");
		}

		sql.append(") a where a.state=0 \n");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		this.addSqlWhereLike(weekReport.getWeekName(), sql, args, " and a.weekRepName like ?");
		//汇报人员筛选
		if(null!=weekReport.getWeekerId() && weekReport.getWeekerId()!=0){
			sql.append("and a.ReporterId=? \n");
			args.add(weekReport.getWeekerId());
		}
		//查询时间起
		this.addSqlWhere(weekS, sql, args, " and a.weekS>=?");
		//查询时间止
		this.addSqlWhere(weekE, sql, args, " and a.weekE<=?");
		//负责人类型
		String weekerType =  weekReport.getWeekerType();
		if(null!=weekerType && !"".equals(weekerType)){
			if("0".equals(weekerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.ReporterId=?");
			}else if("1".equals(weekerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.ReporterId and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		sql.append("order by a.isread,a.year desc,a.weekNum desc,a.depId,a.reporterId\n");
		sql.append(") a where 1=1 \n");
		this.addSqlWhere(id, sql, args, " and a.Id=?");//周报主键
		return (WeekReport) this.objectQuery(sql.toString(), args.toArray(), WeekReport.class);
	}

	/**
	 * 周报内容
	 * @param weekReportId 周报
	 * @param comId 企业
	 * @param reporterId 汇报人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekReportQ> listWeekReportQ(Integer weekReportId, Integer comId, Integer reporterId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.reportValue as reportval from weekReportQ a ");
		sql.append("\n left join weekReport c on c.id=a.weekReportId and a.comid=c.comid ");
		sql.append("\n left join weekReportVal b on a.comid=b.comId and a.weekReportId = b.weekReportId");
		sql.append("\n and a.id = b.questionId where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");//企业
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");//所属周报
		this.addSqlWhere(reporterId, sql, args, " and c.reporterId=?");//汇报人
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), WeekReportQ.class);
	}

	/**
	 * 下周计划
	 * @param weekReportId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekReportPlan> listWeekReportPlan(Integer weekReportId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from weekReportPlan a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");//企业
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");//所属周报
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), WeekReportPlan.class);
	}
	/**
	 * 取得初始化模板条目
	 * @param comId
	 * @param memberId
	 * @param depId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepModContent> initWeekRepContent(Integer comId,
													  Integer memberId, Integer depId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.modcontent,a.isRequire from(");
		//团队级
		sql.append("\n		select a.* from weekRepModContent a where a.replev=1 and a.hidestate=0");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		sql.append("\n		union all");
		//部门级
		sql.append("\n select a.* from weekRepModContent a where a.replev=2 and a.hidestate=0");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		sql.append("\n and a.id in (");
		sql.append("\n   select b.modcontid from　modContDep b where b.modcontid=a.id and a.comid=b.comid and a.modid=b.modid");
		sql.append("\n    and b.depId in (");
		sql.append("\n    select a.id from department a  where 1=1 and a.enabled=1");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(depId, sql, args, " start with a.id=?");
		sql.append("\n		 CONNECT BY PRIOR  a.parentid=a.id");
		sql.append("\n    )");
		sql.append("\n)");

		sql.append("\n		union all");
		//成员级
		sql.append("\n		select a.* from weekRepModContent a where a.replev=3 and a.hidestate=0");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");

		sql.append("\n and a.id in (");
		sql.append("\n		select b.modcontid from modContMember b where a.comid=b.comid and a.modid=b.modid");
		this.addSqlWhere(memberId, sql, args, " and b.memberid=?");
		sql.append("\n		)");

		sql.append("\n		) a where 1=1 order by a.replev asc,a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), WeekRepModContent.class);
	}
	/**
	 * 取得所选日期所写周报
	 * @param weekNum
	 * @param comId
	 * @param reporterId
	 * @return
	 */
	public WeekReport getWeekReportQ(Integer weekNum, Integer comId,Integer reporterId,
									 String year) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.*,(select count(*) from  weekReportQ b where a.comid=b.comid and a.id=b.weekReportId) as countQues,");
		//还没有填写
		sql.append("\n  0 as countVal");
		sql.append("\n  from weekReport a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");//企业
		this.addSqlWhere(reporterId, sql, args, " and a.reporterId=?");//汇报人
		this.addSqlWhere(year, sql, args, " and a.year=?");//年
		this.addSqlWhere(weekNum, sql, args, " and a.weekNum=?");//周
		return (WeekReport) this.objectQuery(sql.toString(), args.toArray(), WeekReport.class);
	}

	/**
	 * 周报附件
	 * @param weekReportId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepUpfiles> listWeekReportFiles(Integer weekReportId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comid,a.weekReportId,a.upfileId,b.filename,a.recordCreateTime as upTime,c.userName,c.gender,d.uuid,d.fileName as imgName, ");
		sql.append("\n  f.fileExt,f.uuid as fileUuid,f.fileName ");
		sql.append("\n  from weekRepUpfiles a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id  ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  left join upfiles f on a.upfileId=f.id and a.comId=f.comId");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		return this.listQuery(sql.toString(), args.toArray(), WeekRepUpfiles.class);
	}
	/**
	 * 周报附件
	 * @param comId
	 * @param weekReportId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepUpfiles> listPagedWeekRepFiles(Integer comId,Integer weekReportId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select * from ( ");
		sql.append("\n  select a.id,a.comid,a.weekReportId,a.upfileId,b.filename,b.uuid as fileUuid,a.recordCreateTime as upTime,'week' type,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId ");
		sql.append("\n  from weekRepUpfiles a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on  cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  union all ");
		sql.append("\n  select a.id,a.comid,a.weekReportId,a.upfileId,b.filename,b.uuid as fileUuid,a.recordCreateTime as upTime,'talk' type,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId ");
		sql.append("\n  from weekRepTalkFile a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on  cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n )a ");
		return this.pagedQuery(sql.toString()," a.upTime desc,a.id", args.toArray(), WeekRepUpfiles.class);
	}
	
	/**
	 * 查询周报附件总数
	 * @param comId
	 * @param weekReportId
	 * @return
	 */
	public Integer countFiles(Integer comId,Integer weekReportId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select count(1) from ( ");
		sql.append("\n  select a.id,a.comid,a.weekReportId,a.upfileId,b.filename,b.uuid as fileUuid,a.recordCreateTime as upTime,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId ");
		sql.append("\n  from weekRepUpfiles a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on  cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  union all ");
		sql.append("\n  select a.id,a.comid,a.weekReportId,a.upfileId,b.filename,b.uuid as fileUuid,a.recordCreateTime as upTime,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId ");
		sql.append("\n  from weekRepTalkFile a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on  cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n )a ");
		return this.countQuery(sql.toString(), args.toArray());
	}


	/**
	 * 周报列表
	 * @param weekReport
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<WeekReport> listPagedWeekReport(WeekReportPojo weekReport,UserInfo userInfo, boolean isForceInPersion) throws ParseException {

		//本周汇报情况
		String weekDoneState = weekReport.getWeekDoneState();
		//查询时间起
		String weekS = weekReport.getStartDate();
		//查询时间止
		String weekE = weekReport.getEndDate();
		//本周所在周数
		Integer weekNum = null;
		//周数所在年
		String weekYear = null;
		if(null!=weekDoneState && !"".equals(weekDoneState)){//需要已汇报或是未汇报
			//当前时间
			String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
			//本周第一天
			weekS = DateTimeUtil.getFirstDayOfWeek(nowDate, DateTimeUtil.c_yyyy_MM_dd_);
			//本周最后一天
			weekE = DateTimeUtil.getLastDayOfWeek(nowDate, DateTimeUtil.c_yyyy_MM_dd_);

			//日期所在周数
			weekNum = DateTimeUtil.getWeekOfYear(nowDate,DateTimeUtil.c_yyyy_MM_dd_);
			//当前周坐在年
			String weeksYear = weekS.substring(0, 4);
			String weekEYear = weekE.substring(0, 4);
			weekYear = weeksYear;
			if(!weeksYear.equals(weekEYear)){
				weekYear = weekEYear;
			}
		}else{
			if (weekS != null && !StringUtil.isBlank(weekS.toString())) {
				weekS = DateTimeUtil.getFirstDayOfWeek(weekS, DateTimeUtil.c_yyyy_MM_dd_);
			}
			if(weekE != null && !StringUtil.isBlank(weekE.toString())){
				weekE = DateTimeUtil.getLastDayOfWeek(weekE, DateTimeUtil.c_yyyy_MM_dd_);
			}
		}

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select reports.*,f.depname, ");
		//是否有待办事项
		sql.append("\n 	case when toDo.todonum>0 then 0 else 1 end as isread\n");
		sql.append("from ( \n");

		sql.append("\n select a.*,b.userid reporterId,d.username,case when c.id is null then 0 else c.id end id,");
		sql.append("\n case when c.weekrepname is null then d.username||'周总结_'||a.year||'年第'||a.weeknum||'周'");
		sql.append("\n else c.weekrepname end weekrepname,c.recordCreateTime,");
		sql.append("\n case when c.state is null then '1' else c.state end state,");


		sql.append("\n d.gender,g.uuid,g.filename as imgName,b.depId,substr(b.recordcreatetime,0,10) registInTime ");

		sql.append("\n from (");
		if(null!=weekNum && null!=weekYear){
			//选出企业中已发布周报的周数，年份，周数起止时间
			sql.append("\n select "+userInfo.getComId()+" comid,"+weekNum+" weeknum,"+weekYear+" year,");
			sql.append("\n '"+weekS+"' weeks, '"+weekE+"' weeke  from dual");
		}else{
			//选出企业中已发布周报的周数，年份，周数起止时间
			sql.append("\n select distinct a.comid,a.weeknum,a.year,a.weeks,a.weeke  from weekReport a where a.comid=? and a.state=0");
			args.add(userInfo.getComId());
			sql.append("\n order by a.weeknum desc ,a.year");
		}

		sql.append("\n )a");
		//关联企业成员
		sql.append("\n left join userorganic b on a.comid=b.comid and b.enabled=1");
		//企业成员是否发布周报
		sql.append("\n left join weekreport c on a.comid=c.comid and a.weeknum=c.weeknum and a.year=c.year and b.userid=c.reporterid");
		//成员基本信息
		sql.append("\n left join userinfo d on b.userid=d.id");
		//成员头像
		sql.append("\n left join upfiles g on  b.mediumHeadPortrait=g.id");

		sql.append("\n where 1=1 ");
		this.addSqlWhere(weekReport.getWeekYear(), sql, args, "\n and a.year=?");
		//非督察人员
		if(!isForceInPersion){
			sql.append("\n and( ");
			//指定范围的
			sql.append("\n 	 exists( ");
			sql.append("\n  	select * from weekViewer b where "+userInfo.getId()+"=b.viewerId and b.userid=c.ReporterId and a.comId=b.comid ");
			sql.append("\n 	 ) ");
			//负责人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = b.userId and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//周报人员自己
			sql.append(" or b.userid=? \n");
			args.add(userInfo.getId());
			//被@的人
			sql.append("\n	or exists (select weekReportId from weekReportShareUser where comid = ? and userid = ? and c.id = weekReportId)");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(" ) \n");
		}

		sql.append(") reports \n");
		//成员所以在部门
		sql.append("left join department f on reports.comid=f.comid and reports.depid=f.id\n");

		//消息通知
		sql.append("\n		left join( ");
		sql.append("\n 			select toDo.Busid,count(toDo.Busid) todonum from todayworks toDo \n");
		sql.append("\n 			where toDo.isclock=0 and toDo.readState=0 and toDo.Bustype=?  and toDo.Userid=? ");
		sql.append("\n 			group by toDo.Busid");
		sql.append("\n		) toDo on reports.id=toDo.busid ");
		args.add(ConstantInterface.TYPE_WEEK);
		args.add(userInfo.getId());

		sql.append("\n where 1=1 \n");
		//周报时间范围小于人员入职时间，不查询
		sql.append("\n and TO_date(reports.weeke, 'yyyy\"年\"mm\"月\"dd\"日\"')>=TO_date(reports.registInTime, 'yyyy-mm-dd') \n");

		//查询部门的
		this.addSqlWhereIn(weekReport.getListTreeDeps(), sql, args, "\n and reports.depid in ?");


		this.addSqlWhereLike(weekReport.getWeekName(), sql, args, " and weekRepName like ? ");
		//汇报人员筛选
		if(null!=weekReport.getWeekerId() && weekReport.getWeekerId()!=0){
			sql.append("and reports.ReporterId=? \n");
			args.add(weekReport.getWeekerId());
		}
		if(null != weekReport.getListOwner() && !weekReport.getListOwner().isEmpty()){
			sql.append("	 and  (  reports.ReporterId = 0 ");
			for(UserInfo owner : weekReport.getListOwner()){
				sql.append("or reports.ReporterId = ?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		//查询时间起
		this.addSqlWhere(weekS, sql, args, " and reports.weekS>=?");
		//查询时间止
		this.addSqlWhere(weekE, sql, args, " and reports.weekE<=?");

		//负责人类型
		String weekerType =  weekReport.getWeekerType();
		if(null!=weekerType && !"".equals(weekerType)){
			if("0".equals(weekerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and reports.ReporterId=?");
			}else if("1".equals(weekerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = reports.ReporterId and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		//查询本周汇报情况
		this.addSqlWhere(weekReport.getWeekDoneState(), sql, args, " and reports.state=?");

		List<Object> argsCount = new ArrayList<Object>();
		StringBuffer sqlCount = new StringBuffer();

		sqlCount.append("select count(weeks) from ( \n");

		sqlCount.append("\n  select a.*,b.userid reporterId,case when c.id is null then 0 else c.id end id,");
		sqlCount.append("\n case when c.weekrepname is null then d.username||'周总结_'||a.year||'年第'||a.weeknum||'周'");
		sqlCount.append("\n else c.weekrepname end weekrepname,c.recordCreateTime,");
		sqlCount.append("\n case when c.state is null then '1' else c.state end state,b.depId, substr(b.recordcreatetime,0,10) registInTime");
		//周报时间范围小于人员入职时间，不查询
		sqlCount.append("\n from (");
		if(null!=weekNum && null!=weekYear){
			//选出企业中已发布周报的周数，年份，周数起止时间
			sqlCount.append("\n select "+userInfo.getComId()+" comid,"+weekNum+" weeknum,"+weekYear+" year,");
			sqlCount.append("\n '"+weekS+"' weeks, '"+weekE+"' weeke  from dual");
		}else{
			//选出企业中已发布周报的周数，年份，周数起止时间
			sqlCount.append("\n select distinct a.comid,a.weeknum,a.year,a.weeks,a.weeke  from weekReport a where a.comid=? and a.state=0");
			argsCount.add(userInfo.getComId());
			sqlCount.append("\n order by a.weeknum desc ,a.year");
		}

		sqlCount.append("\n )a");
		//关联企业成员
		sqlCount.append("\n left join userorganic b on a.comid=b.comid and b.enabled=1");
		//企业成员是否发布周报
		sqlCount.append("\n left join weekreport c on a.comid=c.comid and a.weeknum=c.weeknum and a.year=c.year and b.userid=c.reporterid");
		//成员基本信息
		sqlCount.append("\n left join userinfo d on b.userid=d.id");
		sqlCount.append("\n where 1=1 ");
		this.addSqlWhere(weekReport.getWeekYear(), sqlCount, argsCount, "\n and a.year=?");
		//非督察人员
		if(!isForceInPersion){
			sqlCount.append("\n and( ");
			//指定范围的
			sqlCount.append("\n 	 exists( ");
			sqlCount.append("\n  	select * from weekViewer b where "+userInfo.getId()+"=b.viewerId and b.userid=c.ReporterId and a.comId=b.comid ");
			sqlCount.append("\n 	 ) ");
			//负责人上级权限验证
			sqlCount.append(" or exists(select id from myLeaders where creator = b.userId and comId = ? and leader = ? and leader <> creator )");
			argsCount.add(userInfo.getComId());
			argsCount.add(userInfo.getId());
			//周报人员自己
			sqlCount.append(" or b.userid=? \n");
			argsCount.add(userInfo.getId());
			//被@的人
//			sqlCount.append("\n	or c.id in (select weekReportId from weekReportShareUser where comid = ? and userid = ?)");
			sqlCount.append("\n	or exists (select weekReportId from weekReportShareUser where comid = ? and userid = ? and c.id = weekReportId)");
			argsCount.add(userInfo.getComId());
			argsCount.add(userInfo.getId());

			sqlCount.append(" ) \n");
		}

		sqlCount.append(") reports \n");
		//成员所以在部门
		sqlCount.append("\n left join department f on reports.comid=f.comid and reports.depid=f.id\n");
		sqlCount.append("\n where 1=1 \n");
		sqlCount.append("\n and TO_date(reports.weeke, 'yyyy\"年\"mm\"月\"dd\"日\"')>=TO_date(reports.registInTime, 'yyyy-mm-dd') \n");

		//查询部门的
		this.addSqlWhereIn(weekReport.getListTreeDeps(), sqlCount, argsCount, "\n and reports.depid in ?");

		this.addSqlWhereLike(weekReport.getWeekName(), sqlCount, argsCount, " and weekRepName like ? ");
		//汇报人员筛选
		if(null!=weekReport.getWeekerId() && weekReport.getWeekerId()!=0){
			sqlCount.append("and reports.ReporterId=? \n");
			argsCount.add(weekReport.getWeekerId());
		}
		if(null != weekReport.getListOwner() && !weekReport.getListOwner().isEmpty()){
			sqlCount.append("	 and  (  reports.ReporterId = 0 ");
			for(UserInfo owner : weekReport.getListOwner()){
				sqlCount.append("or reports.ReporterId = ?  \n");
				argsCount.add(owner.getId());
			}
			sqlCount.append("	 ) ");
		}
		//查询时间起
		this.addSqlWhere(weekS, sqlCount, argsCount, " and reports.weekS>=?");
		//查询时间止
		this.addSqlWhere(weekE, sqlCount, argsCount, " and reports.weekE<=?");

		//负责人类型
		if(null!=weekerType && !"".equals(weekerType)){
			if("0".equals(weekerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sqlCount, argsCount, " and reports.ReporterId=?");
			}else if("1".equals(weekerType)){//查询下属的
				sqlCount.append(" and exists(select id from myLeaders where creator = reports.ReporterId and comId = ? and leader = ? and leader <> creator )");
				argsCount.add(userInfo.getComId());
				argsCount.add(userInfo.getId());
			}
		}
		//查询本周汇报情况
		this.addSqlWhere(weekReport.getWeekDoneState(), sqlCount, argsCount, " and reports.state=?");
		return this.pagedQuery(sql.toString(), sqlCount.toString(), "reports.year desc,reports.weekNum desc,reports.depId,reports.reporterId",
				args.toArray(), argsCount.toArray(), WeekReport.class);
	}

	/**
	 * 获取个人权限下的所有周报（不分页）
	 * @param weekReport
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<WeekReport> listWeekReportOfAll(WeekReportPojo weekReport,
												UserInfo userInfo, boolean isForceInPersion) throws ParseException {
		//本周汇报情况
		String weekDoneState = weekReport.getWeekDoneState();
		//查询时间起
		String weekS = weekReport.getStartDate();
		//查询时间止
		String weekE = weekReport.getEndDate();
		//本周所在周数
		Integer weekNum = null;
		//周数所在年
		String weekYear = null;
		if(null!=weekDoneState && !"".equals(weekDoneState)){//需要已汇报或是未汇报
			//当前时间
			String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
			//本周第一天
			weekS = DateTimeUtil.getFirstDayOfWeek(nowDate, DateTimeUtil.c_yyyy_MM_dd_);
			//本周最后一天
			weekE = DateTimeUtil.getLastDayOfWeek(nowDate, DateTimeUtil.c_yyyy_MM_dd_);

			//日期所在周数
			weekNum = DateTimeUtil.getWeekOfYear(nowDate,DateTimeUtil.c_yyyy_MM_dd_);
			//当前周坐在年
			String weeksYear = weekS.substring(0, 4);
			String weekEYear = weekE.substring(0, 4);
			weekYear = weeksYear;
			if(!weeksYear.equals(weekEYear)){
				weekYear = weekEYear;
			}

		}else{
			if (weekS != null && !StringUtil.isBlank(weekS.toString())) {
				weekS = DateTimeUtil.getFirstDayOfWeek(weekS, DateTimeUtil.c_yyyy_MM_dd_);
			}
			if(weekE != null && !StringUtil.isBlank(weekE.toString())){
				weekE = DateTimeUtil.getLastDayOfWeek(weekE, DateTimeUtil.c_yyyy_MM_dd_);
			}
		}

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select * from ( \n");

		sql.append("\n select a.*,b.userid reporterId,f.depname,d.username,case when c.id is null then 0 else c.id end id,");
		sql.append("\n case when c.weekrepname is null then d.username||'周总结_'||a.year||'年第'||a.weeknum||'周'");
		sql.append("\n else c.weekrepname end weekrepname,c.recordCreateTime,");
		sql.append("\n case when c.state is null then '1' else c.state end state,");

		sql.append(" case when today.id is null then \n");
		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where c.comid = today.comid and c.id = today.busid \n");
		sql.append(" and today.bustype='"+ConstantInterface.TYPE_WEEK+"' and today.userId=? and today.isclock=0\n");
		args.add(userInfo.getId());
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 3 else 1 end \n");
		sql.append("  when  today.readstate=0  then 0 \n");
		sql.append("  when  today.readstate=1  then 2 \n");
		sql.append(" end as isread,\n");

		sql.append("\n d.gender,g.uuid,g.filename as imgName,b.depId ");
		sql.append("\n from (");
		if(null!=weekNum && null!=weekYear){
			//选出企业中已发布周报的周数，年份，周数起止时间
			sql.append("\n select "+userInfo.getComId()+" comid,"+weekNum+" weeknum,"+weekYear+" year,");
			sql.append("\n '"+weekS+"' weeks, '"+weekE+"' weeke  from dual");
		}else{
			//选出企业中已发布周报的周数，年份，周数起止时间
			sql.append("\n select distinct a.comid,a.weeknum,a.year,a.weeks,a.weeke  from weekReport a where a.comid=? and a.state=0");
			args.add(userInfo.getComId());
			sql.append("\n order by a.weeknum desc ,a.year");
		}

		sql.append("\n )a");
		//关联企业成员
		sql.append("\n left join userorganic b on a.comid=b.comid and b.enabled=1");
		//企业成员是否发布周报
		sql.append("\n left join weekreport c on a.comid=c.comid and a.weeknum=c.weeknum and a.year=c.year and b.userid=c.reporterid");
		//成员基本信息
		sql.append("\n left join userinfo d on b.userid=d.id");
		//成员所以在部门
		sql.append("\n left join department f on b.comid=f.comid and b.depid=f.id");
		//成员头像
		sql.append("\n left join upfiles g on  b.mediumHeadPortrait=g.id");
		//操作人员的待办事项或是未读提醒
		sql.append("\n left join todayworks today on a.comid = today.comid and c.id = today.busid and today.busspec=1");
		sql.append("\n and today.bustype='"+ConstantInterface.TYPE_WEEK+"' and today.userId=? ");
		args.add(userInfo.getId());
		sql.append("\n where 1=1 ");

		//查询部门的
		if(null!=weekReport.getDepId()){
			sql.append("and f.id in ( select id from department \n");
			sql.append("start with id="+weekReport.getDepId()+" connect by prior id = parentid \n");
			sql.append(")\n");
		}

		//非督察人员
		if(!isForceInPersion){
			sql.append("\n and( ");
			//指定范围的
			sql.append("\n 	 exists( ");
			sql.append("\n  	select * from weekViewer b where "+userInfo.getId()+"=b.viewerId and b.userid=c.ReporterId and a.comId=b.comid ");
			sql.append("\n 	 ) ");
			//负责人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = b.userId and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//周报人员自己
			sql.append(" or c.ReporterId=? \n");
			args.add(userInfo.getId());
			//被@的人
//			sql.append("\n	or c.id in (select weekReportId from weekReportShareUser where comid = ? and userid = ?)");
			sql.append("\n	or exists (select weekReportId from weekReportShareUser where comid = ? and userid = ? and c.id = weekReportId)");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(" ) \n");
		}

		sql.append(") reports where 1=1 \n");
		this.addSqlWhereLike(weekReport.getWeekName(), sql, args, " and weekRepName like ?");
		//汇报人员筛选
		if(null!=weekReport.getWeekerId() && weekReport.getWeekerId()!=0){
			sql.append("and reports.ReporterId=? \n");
			args.add(weekReport.getWeekerId());
		}

		//查询时间起
		this.addSqlWhere(weekS, sql, args, " and reports.weekS>=?");
		//查询时间止
		this.addSqlWhere(weekE, sql, args, " and reports.weekE<=?");

		//负责人类型
		String weekerType =  weekReport.getWeekerType();
		if(null!=weekerType && !"".equals(weekerType)){
			if("0".equals(weekerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and reports.ReporterId=?");
			}else if("1".equals(weekerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = reports.ReporterId and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		//查询本周汇报情况
		this.addSqlWhere(weekReport.getWeekDoneState(), sql, args, " and reports.state=?");
		sql.append("order by reports.isread,reports.year desc,reports.weekNum desc,reports.depId,reports.reporterId \n");
		return this.listQuery(sql.toString(), args.toArray(),WeekReport.class);

	}

	/**
	 * 获取团队周报主键集合
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekReport> listWeekReportOfAll(UserInfo userInfo){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id from weekReport a where a.comid=?");
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(),WeekReport.class);
	}
	/**
	 * 获取本周的满足查询条件的数目
	 * @param weekDoneState 0本周已汇报 1本周未汇报
	 * @param userInfo 当前操作人员
	 * @param isForceInPersion 是否强制参与人
	 * @return
	 * @throws ParseException
	 */
	public Integer getWeekReportCount(String weekDoneState,
									  UserInfo userInfo, boolean isForceInPersion) throws ParseException {
		//当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
		//查询时间起
		String weekS = DateTimeUtil.getFirstDayOfWeek(nowDate, DateTimeUtil.c_yyyy_MM_dd_);
		//查询时间止
		String weekE = DateTimeUtil.getLastDayOfWeek(nowDate, DateTimeUtil.c_yyyy_MM_dd_);

		//日期所在周数
		Integer weekNum = DateTimeUtil.getWeekOfYear(nowDate,DateTimeUtil.c_yyyy_MM_dd_);

		//当前周坐在年
		String weeksYear = weekS.substring(0, 4);
		String weekEYear = weekE.substring(0, 4);
		String weekYear = weeksYear;
		if(!weeksYear.equals(weekEYear)){
			weekYear = weekEYear;
		}

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select count(reports.id) from ( \n");

		sql.append("\n select a.*,b.userid reporterId,d.username,case when c.id is null then 0 else c.id end id,");
		sql.append("\n case when c.weekrepname is null then d.username||'周总结_'||a.year||'年第'||a.weeknum||'周'");
		sql.append("\n else c.weekrepname end weekrepname,c.recordCreateTime,");
		sql.append("\n case when c.state is null then '1' else c.state end state");
		sql.append("\n from (");
		//选出企业中已发布周报的周数，年份，周数起止时间
		sql.append("\n select "+userInfo.getComId()+" comid,"+weekNum+" weeknum,"+weekYear+" year,");
		sql.append("\n '"+weekS+"' weeks, '"+weekE+"' weeke  from dual");

		sql.append("\n )a");
		//关联企业成员
		sql.append("\n left join userorganic b on a.comid=b.comid and b.enabled=1");
		//企业成员是否发布周报
		sql.append("\n left join weekreport c on a.comid=c.comid and a.weeknum=c.weeknum and a.year=c.year and b.userid=c.reporterid");
		//成员基本信息
		sql.append("\n left join userinfo d on b.userid=d.id");
		sql.append("\n where 1=1 ");

		//非督察人员
		if(!isForceInPersion){
			sql.append("\n and( ");
			//指定范围的
			sql.append("\n 	 exists( ");
			sql.append("\n  	select * from weekViewer b where "+userInfo.getId()+"=b.viewerId and b.userid=c.ReporterId and a.comId=b.comid ");
			sql.append("\n 	 ) ");
			//负责人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = b.userId and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//周报人员自己
			sql.append(" or b.userid=? \n");
			args.add(userInfo.getId());
			//被@的人
//			sql.append("\n	or c.id in (select weekReportId from weekReportShareUser where comid = ? and userid = ?)");
			sql.append("\n	or exists (select weekReportId from weekReportShareUser where comid = ? and userid = ? and c.id = weekReportId)");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(" ) \n");
		}

		sql.append(") reports where 1=1 \n");
		//查询时间起
		this.addSqlWhere(weekS, sql, args, " and reports.weekS>=?");
		//查询时间止
		this.addSqlWhere(weekE, sql, args, " and reports.weekE<=?");

		//查询本周汇报情况
		this.addSqlWhere(weekDoneState, sql, args, " and reports.state=?");

		return this.countQuery(sql.toString(), args.toArray());

	}

	/**
	 * 反馈留言
	 * @param weekReportId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepTalk> listPagedWeekRepTalk(Integer weekReportId,
												  Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comid,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from weekRepTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join weekRepTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.pagedQuery(sql.toString(),null ,args.toArray(), WeekRepTalk.class);
	}
	
	/**
	 * 查询留言总数
	 * @param weekReportId
	 * @param comId
	 * @return
	 */
	public Integer countTalk(Integer weekReportId,
			  Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(1) from (");
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comid,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from weekRepTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join weekRepTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		sql.append("\n )");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 周报日志
	 * @param weekReportId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepLog> listPagedVoteLog(Integer weekReportId, Integer comId,String sid) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.recordCreateTime,a.comId,a.weekReportId,a.userId,a.userName,replace(a.content,'@sid','"+sid+"') content, \n");
		sql.append("b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from weekRepLog a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.weekReportId = ? \n");
		args.add(comId);
		args.add(weekReportId);
		return this.pagedQuery(sql.toString()," a.recordcreatetime desc", args.toArray(), WeekRepLog.class);
	}
	/**
	 * 反馈留言
	 * @param id
	 * @param comId
	 * @return
	 */
	public WeekRepTalk getWeekRepTalk(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.weekReportId,a.recordCreateTime,a.id,a.parentid,a.comid,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from weekRepTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comID=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join weekRepTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return (WeekRepTalk) this.objectQuery(sql.toString(), args.toArray(), WeekRepTalk.class);
	}

	/**
	 * 将子节点提高一级
	 * @param id
	 * @param comId
	 */
	public void updateWeekRepTalkParentId(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		WeekRepTalk weekRepTalk = (WeekRepTalk) this.objectQuery(WeekRepTalk.class, id);
		if(null!=weekRepTalk && -1==weekRepTalk.getParentId()){//父节点为-1时将妻哪一个说话人设为空
			sql.append("update weekRepTalk set parentId=(select c.parentid \n");
			sql.append("from weekRepTalk c \n");
			sql.append("where c.id=?),ptalker=null where parentid = ? and comId = ?\n");
			args.add(id);
			args.add(id);
			args.add(comId);

		}else{//删除自己,将子节点提高一级
			sql.append("update weekRepTalk set parentId=(select c.parentid \n");
			sql.append("from weekRepTalk c \n");
			sql.append("where c.id=?) where parentid = ? and comId = ? \n");
			args.add(id);
			args.add(id);
			args.add(comId);
		}
		this.excuteSql(sql.toString(), args.toArray());

	}

	/**
	 * 模板成员级条目成员
	 * @param modContId
	 * @param comId
	 * @param modId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModContMember> listModContMember(Integer modContId, Integer comId,
												 Integer modId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.gender,b.userName as memberName,c.uuid as imgUuid, c.filename as imgName \n");
		sql.append(" from modContMember a inner join userinfo b on  a.memberId=b.id \n");
		sql.append(" inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId \n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait=c.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(modContId, sql, args, " and a.modcontid=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(modId, sql, args, " and a.modId=?");
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), ModContMember.class);
	}

	/**
	 * 模板部门级条目部门
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
		sql.append("\n from modContDep a inner join department b on a.comid=b.comid and a.depid=b.id ");
		sql.append("where 1=1 \n");
		this.addSqlWhere(modContId, sql, args, " and a.modcontid=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(modId, sql, args, " and a.modId=?");
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), ModContDep.class);
	}

	/**
	 * 需要删除的附件
	 * @param comId 企业编号
	 * @param weekReportId 周报主键
	 * @param fileIds 本次附件主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepUpfiles> listRemoveWeekFiles(Integer comId, Integer weekReportId,
													String fileIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("\n  select* from ( ");
		sql.append("\n  select a.* from weekRepUpfiles a  where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		sql.append("\n  minus ");
		sql.append("\n  select a.* from weekRepUpfiles a  where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		sql.append("\n and a.upfileId in ("+fileIds+")");
		sql.append("\n  ) a where 1=1 ");
		return this.listQuery(sql.toString(), args.toArray(), WeekRepUpfiles.class);
	}

	/**
	 * 查出本次需要一处的人员
	 * @param weekViewerList
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekViewer> listWeekViewerForRemove(List<WeekViewer> weekViewerList, UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		Integer comId = userInfo.getComId();
		Integer userId = userInfo.getId();

		sql.append("\n select a.viewerId userId from weekViewer a");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		sql.append("\n minus");
		sql.append("\n select leader from myLeaders a where creator <> leader ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.creator=?");
		sql.append("\n minus");
		sql.append("\n select a.viewerId userId from weekViewer a ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		sql.append("\n and a.viewerId in ("+userInfo.getId());
		if(null!=weekViewerList && !weekViewerList.isEmpty()){
			for (WeekViewer weekViewer : weekViewerList) {
				sql.append(","+weekViewer.getViewerId());
			}
		}
		sql.append("\n )");
		this.printSql(sql, args);
		return this.listQuery(sql.toString(), args.toArray(), WeekViewer.class);
	}


	/**
	 *
	 * @param userId 操作员
	 * @param comId 企业名称
	 * @return 每一期周报的附件集合
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepUpfiles> listUserWeekReportFiles(Integer userId,
														Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from (");
		sql.append("\n select a.comId,a.weekReportId,a.upfileId,a.userId,'"+ConstantInterface.TYPE_WEEK+"' type ");
		sql.append("\n from weekRepUpfiles a inner join weekreport b on a.comid=b.comid and a.weekreportid=b.id");
		sql.append("\n and b.state=0 where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and b.comId=?");
		this.addSqlWhere(userId, sql, args, " and b.reporterid=?");
		sql.append("\n union all ");
		sql.append("\n select a.comId,a.weekReportId,a.upfileId,a.userId,'"+ConstantInterface.TYPE_WEEKTALK+"' type ");
		sql.append("\n from weekRepTalkFile a inner join weekreport b on a.comid=b.comid and a.weekreportid=b.id");
		sql.append("\n and b.state=0 where 1=1");
		this.addSqlWhere(comId, sql, args, " and b.comId=?");
		this.addSqlWhere(userId, sql, args, " and b.reporterid=?");
		sql.append("\n )a where 1=1 ");
		return this.listQuery(sql.toString(), args.toArray(), WeekRepUpfiles.class);
	}

	/**
	 *
	 * @param comId
	 * @param userId
	 * @param reporterId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekViewer> authorCheck(Integer comId, Integer userId,
										Integer reporterId,Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct * from ( \n");
		sql.append("	select a.viewerId  from  weekViewer a \n");
		sql.append("		where a.comid = ? and a.userId =? \n");
		args.add(comId);
		args.add(reporterId);
		sql.append("	union all \n");
		sql.append("	select "+reporterId+" viewerId from dual  \n");
		sql.append("	union all \n");
		sql.append("	select * from ( \n");
		sql.append("		select leader from myLeaders where creator = ? and comId = ? and creator <> leader \n");
		args.add(comId);
		args.add(reporterId);
		sql.append("	)a \n");
		sql.append("	union all \n");
		sql.append("	select userid from weekReportShareUser");
		sql.append("	where comid = ? and userid = ? and weekReportId = ?");
		args.add(comId);
		args.add(userId);
		args.add(busId);
		sql.append(")a \n");
		return this.listQuery(sql.toString(), args.toArray(),WeekViewer.class);
	}

	/**
	 * 讨论的附件
	 * @param comId
	 * @param weekReportId
	 * @param talkId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepTalkFile> listWeekRepTalkFile(Integer comId,
													 Integer weekReportId, Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.weekReportId,a.userId,a.talkId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, \n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from weekRepTalkFile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comid =? and a.weekReportId = ? and a.talkId=?\n");
		args.add(comId);
		args.add(weekReportId);
		args.add(talkId);
		sql.append("order by isPic asc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), WeekRepTalkFile.class);
	}

	/**
	 * 待删除的讨论
	 * @param comId
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepTalk> listWeekRepTalkForDel(Integer comId, Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from WeekRepTalk where comid=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(id);
		return this.listQuery(sql.toString(), args.toArray(), WeekRepTalk.class);
	}

	/**
	 * 删除反馈留言
	 * @param id
	 * @param comId
	 */
	public void delWeekRepTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from weekRepTalk a where a.comid =? and a.id in \n");
		sql.append("(select id from weekRepTalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());

	}
	/**
	 * 获取周报计划为其创建索引
	 * @param comId
	 * @param weekReportId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekReportPlan> listWeekReportPlan4Index(Integer comId,Integer weekReportId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.plancontent from WeekReportPlan a where a.comid =? and a.weekreportid = ? \n");
		args.add(comId);
		args.add(weekReportId);
		return this.listQuery(sql.toString(),args.toArray(),WeekReportPlan.class);
	}
	/**
	 * 获取周报汇报内容为其创建索引
	 * @param comId
	 * @param weekReportId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekReportVal> listWeekReportVal4Index(Integer comId,Integer weekReportId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.reportValue from weekReportVal a where a.comid =? and a.weekReportId = ? \n");
		args.add(comId);
		args.add(weekReportId);
		return this.listQuery(sql.toString(),args.toArray(),WeekReportVal.class);
	}
	/**
	 * 获取反馈留言为其创建索引
	 * @param comId
	 * @param weekReportId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepTalk> listWeekRepTalk4Index(Integer comId,Integer weekReportId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.content,b.username as talkerName from weekRepTalk a \n");
		sql.append("inner join userInfo b on a.talker = b.id \n");
		sql.append("where a.comid =? and a.weekreportid = ? \n");
		args.add(comId);
		args.add(weekReportId);
		return this.listQuery(sql.toString(),args.toArray(),WeekRepTalk.class);
	}
	/**
	 * 周报查看人
	 * @param comId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listWeekRepViewUser(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName from weekViewer b ");
		sql.append("\n inner join userInfo c on b.viewerId=c.id ");
		sql.append("\n inner join userorganic d on b.comid=d.comid and c.id=d.userid and d.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, "  and b.comId=?");
		this.addSqlWhere(userId, sql, args, "  and b.userid=?");
		sql.append("\n union all");
		sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName from myLeaders b ");
		sql.append("\n inner join userInfo c on b.leader=c.id ");
		sql.append("\n inner join userorganic d on b.comid=d.comid and c.id=d.userid and d.enabled=1 ");
		sql.append("\n where 1=1 and b.creator <> b.leader ");
		this.addSqlWhere(comId, sql, args, "  and b.comId=?");
		this.addSqlWhere(userId, sql, args, "  and b.creator=?");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 周报查看人
	 * @param comId
	 * @param userId
	 * @return
	 */
	public UserInfo getWeekSelf(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName  from userInfo c");
		sql.append("\n inner join userorganic d on c.id=d.userid and d.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, "  and d.comId=?");
		this.addSqlWhere(userId, sql, args, "  and d.userid=?");
		return (UserInfo) this.objectQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 获取督察人员集合
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ForceInPersion> forceInPersionForWeek(Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.username as sharerName,b.gender,c.uuid,c.filename \n");
		sql.append("from forceInPersion a \n");
		sql.append("inner join userInfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comid = bb.comid \n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id \n");
		sql.append("where a.comid=? and a.type=? \n");
		args.add(comId);
		args.add(ConstantInterface.TYPE_WEEK);
		return this.listQuery(sql.toString(), args.toArray(), ForceInPersion.class);
	}
	/**
	 * 自己已发布的周报集合
	 * @param userId 当前操作人员主键
	 * @param comId 企业号
	 * @param sendedMsg 是否发送过消息 true 取得发送过消息但有上级没有处理的  false 纯粹取已发布的
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekReport> listWeekReport(Integer userId, Integer comId,boolean sendedMsg) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from WeekReport a where a.state=0 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		this.addSqlWhere(userId, sql, args, " and a.reporterid=? ");
		if(sendedMsg){
			sql.append("\n and exists(");
			sql.append("\n 		select b.* from todayworks b where b.comid=a.comid and a.id=b.busid and b.bustype='"+ConstantInterface.TYPE_WEEK+"'");
			sql.append("\n 		)");
		}
		return this.listQuery(sql.toString(), args.toArray(), WeekReport.class);
	}

	/**
	 * 查询模块下是否有相同的附件
	 * @param comId 企业号
	 * @param weekReportId 模块主键
	 * @param upfileId 附件主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekRepTalkFile> listWeekSimUpfiles(Integer comId,
													Integer weekReportId, Integer upfileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		//周报模块附件
		sql.append("select a.comId,a.weekReportId,a.upfileId  from weekRepUpfiles a \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		sql.append("\n union all \n");
		//周报留言附件
		sql.append("select  a.comId,a.weekReportId,a.upfileId from weekRepTalkFile a\n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(weekReportId, sql, args, " and a.weekReportId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		return this.listQuery(sql.toString(), args.toArray(), WeekRepTalkFile.class);
	}
	/**
	 * 查询周报查看人员
	 * @param sessionUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WeekViewer> listWeekViewer(UserInfo sessionUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.username viewerName,b.gender,c.uuid,c.filename \n");
		sql.append("from weekViewer a \n");
		sql.append("inner join userInfo b on a.viewerId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comid = bb.comid and bb.enabled=1\n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id \n");
		sql.append("where a.comid=? and a.userid=? \n");
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		sql.append("order by a.id asc\n");
		return this.listQuery(sql.toString(), args.toArray(), WeekViewer.class);
	}
	/**
	 * 查询企业提交周报时间设置
	 * @param userInfo
	 * @return
	 */
	public SubTimeSet querySubTimeSet(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from SubTimeSet where comId = ? \n");
		args.add(userInfo.getComId());
		return (SubTimeSet) this.objectQuery(sql.toString(), args.toArray(), SubTimeSet.class);
	}


	/**
	 * 查询本次需要推送的人员
	 * @param msgId 分享主键
	 * @param comId 团队号
	 * @param pushUserIdSet 本次推送的人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPushTodoUser(Integer msgId, Integer comId, Set<Integer> pushUserIdSet){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享的发布人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName ");
		sql.append("\n from msgShare d ");
		sql.append("\n inner join userinfo b on d.creator=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comid=d.comid and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comid=?");
		this.addSqlWhere(msgId, sql, args, " and d.id=?");

		sql.append("\n union");
		//分享爱你个的关注人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
		sql.append("\n from msgShare d inner join attention a on a.busId=d.id and d.comid=a.comid and a.busType=1");
		sql.append("\n inner join userinfo b on d.creator=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comid=a.comid and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comid=?");
		this.addSqlWhere(msgId, sql, args, " and d.id=?");
		//本次推送的人员
		if(null!=pushUserIdSet && !pushUserIdSet.isEmpty()){
			sql.append("\n union");
			sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
			sql.append("\n from userinfo b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhereIn(pushUserIdSet.toArray(new Integer[pushUserIdSet.size()]), sql, args, "\n and b.id in ?");
		}
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}/**
	 * 查询本次需要推送的人员
	 * @param weekReportId 周报分享主键
	 * @param comId 团队号
	 * @param pushUserIdSet 本次推送的人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPushTodoWeekkPortUser(Integer weekReportId, Integer comId, Set<Integer> pushUserIdSet){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享的发布人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName ");
		sql.append("\n from weekReport d ");
		sql.append("\n inner join userinfo b on d.reporterId=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comid=d.comid and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comid=?");
		this.addSqlWhere(weekReportId, sql, args, " and d.id=?");

		sql.append("\n union");
		//分享爱你个的关注人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
		sql.append("\n from weekReport d inner join attention a on a.busId=d.id and d.comid=a.comid and a.busType=1");
		sql.append("\n inner join userinfo b on d.reporterId=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comid=a.comid and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comid=?");
		this.addSqlWhere(weekReportId, sql, args, " and d.id=?");
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
}
