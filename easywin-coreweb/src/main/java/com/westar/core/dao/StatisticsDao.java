package com.westar.core.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.westar.base.enums.DemandStageEnum;
import com.westar.base.enums.OverDueLevelEnum;
import com.westar.base.model.BusRemind;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.Daily;
import com.westar.base.model.DataDic;
import com.westar.base.model.DemandProcess;
import com.westar.base.model.Department;
import com.westar.base.model.Item;
import com.westar.base.model.Product;
import com.westar.base.model.Task;
import com.westar.base.model.TaskExecutor;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WeekReport;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.StatisticCrmVo;
import com.westar.base.pojo.StatisticTaskVo;
import com.westar.base.pojo.WeekReportPojo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.web.PaginationContext;

/**
 * 
 * 描述:系统统计数据查询
 * @author zzq
 * @date 2018年8月27日 下午2:15:31
 */
@Repository
public class StatisticsDao extends BaseDao {

	/**
	 * 任务统计分析
	 * @param curUser
	 * @param nowDateStr
	 * @param version 
	 * @return
	 */
	public JSONObject statisticsTask(UserInfo curUser, String nowDateStr, String version) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select ");
		sql.append("\n sum(case when substr(a.recordcreatetime,0,10)=to_char(sysdate,'yyyy-mm-dd') then 1 else 0 end) as newTaskNum,");
		sql.append("\n sum(case when substr(a.finishtime,0,10) >=to_char(sysdate,'yyyy-mm-dd') then 1 else 0 end) as finishTaskNum,");
		sql.append("\n count(a.id) as allTaskNum");
		sql.append("\n from task a where a.comid=? and (a.state=? or a.state=?) and a.delstate=0");
		args.add(curUser.getComId());
		args.add(ConstantInterface.STATIS_TASK_STATE_DOING);
		args.add(ConstantInterface.STATIS_TASK_STATE_DONE);
		this.addSqlWhere(version, sql, args, " and a.version=? ");
		return this.objectQueryJSON(sql.toString(), args.toArray());
	}
	
	/**
	 * 今日客户统计
	 * @param sessionUser
	 * @param nowDateStr
	 * @return
	 */
	public JSONObject statisticsTodayCrm(UserInfo sessionUser, String nowDateStr) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select sum(case when substr(a.recordcreatetime,0,10)='"+nowDateStr+"' then 1 else 0 end) as newCrmNum ");
		
		sql.append("\n ,sum(case when a.modifyPeriod > 0 then");
		sql.append("\n 	case when (sysdate - to_date(a.lastUpdateDate,'yyyy-MM-dd HH24:mi:ss')) - a.modifyPeriod >= 1 then 1");
		sql.append("\n 		else 0 end");
		sql.append("\n 	else 0 end ) overTimeCrmNum");
		
		sql.append("\n ,sum(case when a.modifyPeriod > 0 then");
		sql.append("\n 	case when (sysdate - to_date(a.lastUpdateDate,'yyyy-MM-dd HH24:mi:ss')) - a.modifyPeriod <= 0 then 1");
		sql.append("\n 		else 0 end");
		sql.append("\n 	else 1 end ) onTimeCrmNum");
		
		sql.append("\n  ,sum(case when substr(a.lastUpdateDate,0,10)='"+nowDateStr+"' then 1 else 0 end)modifyCrmNum ");
		sql.append("\n ,count(a.id) as allCrmNum");
		sql.append("\n from (");
		sql.append("\n 	select ");
		//新增数
		sql.append("\n 		a.recordcreatetime,b.modifyPeriod");
		//最近更新时间
		sql.append("\n 		,case when crmFeedbackDate.recordcreatetime is null and crmHandOverDate.recordcreatetime is null then a.recordcreatetime");
		sql.append("\n 			when crmFeedbackDate.recordcreatetime is null then crmHandOverDate.recordcreatetime");
		sql.append("\n 			when to_date(crmFeedbackDate.recordcreatetime,'yyyy-MM-dd HH24:mi:ss')<=");
		sql.append("\n 			to_date(crmHandOverDate.recordcreatetime,'yyyy-MM-dd HH24:mi:ss')");
		sql.append("\n 			then crmHandOverDate.recordcreatetime else crmFeedbackDate.recordcreatetime end lastUpdateDate");
				
		sql.append("\n 		,a.id");
		sql.append("\n 		from customer a");
		sql.append("\n 		inner join customerType b on a.customerTypeId=b.id");
		//客户移交最近的时间
		sql.append("\n 		left join (");
		sql.append("\n 			select * from (");
		sql.append("\n				select a.customerid,a.recordcreatetime,crm.owner,row_number() over (partition by a.customerid order by a.id desc) as new_order");
		sql.append("\n  			from customerhandover a ");
		sql.append("\n 				inner join customer crm on a.customerid=crm.id and a.touser=crm.owner");
		sql.append("\n  			where 1=1 and a.comid=?");
		args.add(sessionUser.getComId());
		sql.append("\n 			) a where new_order=1 and substr(a.recordcreatetime,0,10)=?");
		args.add(nowDateStr);
		sql.append("\n 		)crmHandOverDate on crmHandOverDate.customerid=a.id");
		//客户反馈最近时间
		sql.append("\n 		left join (");
		sql.append("\n 			select * from (");
		sql.append("\n				select a.customerid,a.recordcreatetime,crm.owner,row_number() over (partition by a.customerid order by a.id desc) as new_order");
		sql.append("\n  			from feedBackInfo a ");
		sql.append("\n 				inner join customer crm on a.customerid=crm.id and a.userId=crm.owner");
		sql.append("\n  			where 1=1 and a.comid=?");
		args.add(sessionUser.getComId());
		sql.append("\n 			) a where new_order=1 and substr(a.recordcreatetime,0,10)=?");
		args.add(nowDateStr);
		sql.append("\n 		)crmFeedbackDate on crmFeedbackDate.customerid=a.id");
				
		sql.append("\n 	where a.comid=? and a.delstate=0");
		args.add(sessionUser.getComId());
		sql.append("\n 	)a where 1=1");
		return this.objectQueryJSON(sql.toString(), args.toArray());
	}
	/**
	 * 今日日报统计
	 * @param sessionUser
	 * @param nowDateStr
	 * @return
	 */
	public JSONObject statisticsTodayDaily(UserInfo sessionUser, String nowDateStr) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select ");
		//应交
		sql.append("\n count(uorg.id) dailyShouldNum");
		//实交
		sql.append("\n ,sum(case when substr(a.recordcreatetime,0,10)=a.dailydate then 1 else 0 end) dailyActNum");
		//迟交
		sql.append("\n ,sum(case when substr(a.recordcreatetime,0,10)>a.dailydate then 1 else 0 end) dailyOverNum");
		//未交
		sql.append("\n ,sum(case when a.recordcreatetime is null then 1 else 0 end)dailyNoneNum ");
		sql.append("\n from userorganic uorg");
		sql.append("\n left join daily a on a.comId=uorg.comId and a.reporterid=uorg.userid");
		sql.append("\n and a.dailydate='"+nowDateStr+"'");
		sql.append("\n where uorg.enabled=1 and uorg.comid=?");
		args.add(sessionUser.getComId());
		return this.objectQueryJSON(sql.toString(), args.toArray());
	}
	
	/**
	 * 取得周报的截止时间
	 * @param sessionUser
	 * @param endDate
	 * @return
	 */
	public String statisticsWeekLastDate(UserInfo sessionUser,String endDate) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();

		//查询选定时间的提交周报的截止时间
		sql.append("\n select TO_char(TO_date('"+endDate+"','yyyy-mm-dd') - timetype,'yyyy-mm-dd') lastDate");
		sql.append("\n from ( ");
		sql.append("\n 	select case when a.timetype is null then '2' else a.timetype end timetype");
		sql.append("\n  from (");
		sql.append("\n  select 1 from dual");
		sql.append("\n  ) t left join");
		sql.append("\n  subtimeset a on 1=1 and a.comId = "+sessionUser.getComId());
		sql.append("\n )a");
		
		JSONObject jsonObject = this.objectQueryJSON(sql.toString(), args.toArray());
		//周报的提交截止时间
		String lastDate =  jsonObject.getString("LASTDATE");
		return lastDate;
	}
	
	/**
	 * 本周周报日报统计
	 * @param sessionUser
	 * @param endDate
	 * @return
	 */
	public JSONObject statisticsWeek(UserInfo sessionUser,String endDate,String lastDate) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//转成中文版的年月日
		Date date = DateTimeUtil.parseDate(endDate, DateTimeUtil.yyyy_MM_dd);
		String endDateC = DateTimeUtil.formatDate(date, DateTimeUtil.c_yyyy_MM_dd_);
		
		sql = new StringBuffer();
		args = new ArrayList<Object>();
		sql.append("\n select ");
		//应交
		sql.append("\n count(uorg.id) weekShouldNum");
		//实交
		sql.append("\n ,sum(case when substr(a.recordcreatetime,0,10)<=? then 1 else 0 end) weekActNum");
		args.add(lastDate);
		//迟交
		sql.append("\n ,sum(case when substr(a.recordcreatetime,0,10)>? then 1 else 0 end) weekOverNum");
		args.add(lastDate);
		//未交
		sql.append("\n ,sum(case when a.recordcreatetime is null then 1 else 0 end) weekNoneNum ");
		sql.append("\n from userorganic uorg");
		sql.append("\n left join weekreport a on a.comId=uorg.comId and a.reporterid=uorg.userid");
		sql.append("\n and a.state=0 and a.weeke='"+endDateC+"'");
		sql.append("\n where uorg.enabled=1 and uorg.comid=?");
		args.add(sessionUser.getComId());
		return this.objectQueryJSON(sql.toString(), args.toArray());
	}
	
	/**
	 * 按照部门统计指定时间段创建和办结的
	 * @param sessionuser 当前操作人员
	 * @param startDate 查询的开始时间
	 * @param endDate 查询的结束时间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StatisticTaskVo> statisticTaskForDep(UserInfo sessionuser,String startDate,String endDate,String version){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		
		sql.append("\n select a.id depId,a.depname,nvl(b.createNum,0)createNum,nvl(c.finishNum,0)finishNum");
		//统计部门表
		sql.append("\n from department a ");
		//创建任务统计
		sql.append("\n left join (");
		sql.append("\n 		select uorg.Depid");
		sql.append("\n 			,sum(1)createNum");
		sql.append("\n  	from task a");
		sql.append("\n 		inner join userorganic uorg on a.owner=uorg.userid and a.comid=uorg.comid");
		sql.append("\n 		where 1=1 and a.delstate=0");
		this.addSqlWhere(version, sql, args, " and task.version=? ");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(startDate, sql, args, "\n and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(endDate, sql, args, "\n and substr(a.recordcreatetime,0,10)<=?");
		sql.append("\n 		group by uorg.Depid");
		sql.append("\n ) b on a.id=b.depId");
		//办结任务统计
		sql.append("\n left join (");
		sql.append("\n 		select uorg.Depid");
		sql.append("\n 			,sum(1)finishNum");
		sql.append("\n  	from task a");
		sql.append("\n 		inner join userorganic uorg on a.owner=uorg.userid and a.comid=uorg.comid");
		sql.append("\n 		where 1=1 and a.delstate=0");
		this.addSqlWhere(version, sql, args, " and task.version=? ");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(startDate, sql, args, "\n and substr(a.finishtime,0,10)>=?");
		this.addSqlWhere(endDate, sql, args, "\n and substr(a.finishtime,0,10)<=?");
		sql.append("\n 		group by uorg.Depid");
		sql.append("\n ) c on a.id=c.depId");
		//创建任务统计
		sql.append("\n 	where 1=1");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		sql.append("\n 	order by nvl(b.createNum,0)+nvl(c.finishNum,0) desc");
		return this.listQuery(sql.toString(), args.toArray(), StatisticTaskVo.class);
	}  
	/**
	 * 按照部门统计指定时间段创建和办结的
	 * @param sessionuser 当前操作人员
	 * @param startDate 查询的开始时间
	 * @param endDate 查询的结束时间
	 * @param version 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StatisticTaskVo> statisticTaskHandAndFinishForDep(UserInfo sessionuser,String startDate,String endDate, String version){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		
		sql.append("\n select a.id depId,a.depname,nvl(b.createNum,0)createNum,nvl(c.finishNum,0)finishNum");
		//统计部门表
		sql.append("\n from department a ");
		//创建任务统计
		sql.append("\n left join (");
		sql.append("\n 		select a.Depid,count(*) createNum");
		sql.append("\n  	from(");
		sql.append("\n 			select distinct a.taskid,uorg.depid");
		sql.append("\n 		 	from taskhandover a");
		sql.append("\n 			inner join task on a.taskId=task.id");
		sql.append("\n 			inner join userorganic uorg on a.comId=uorg.comId and a.touser=uorg.userid");
		sql.append("\n 			where 1=1 and task.delstate=0");
		this.addSqlWhere(version, sql, args, " and task.version=? ");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(startDate, sql, args, "\n and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(endDate, sql, args, "\n and substr(a.recordcreatetime,0,10)<=?");
		sql.append("\n 		)a");
		sql.append("\n 		group by a.Depid");
		
		sql.append("\n ) b on a.id=b.depId");
		//办结任务统计
		sql.append("\n left join (");
		
		sql.append("\n 		select a.Depid ,count(*)finishNum");
		sql.append("\n 		from(");
		sql.append("\n 			select distinct a.taskid,uorg.depid");
		sql.append("\n 			from taskhandover a");
		sql.append("\n 			inner join task on a.taskId=task.id");
		sql.append("\n 			inner join userorganic uorg on a.comId=uorg.comId and a.touser=uorg.userid");
		sql.append("\n 			where task.delstate=0 and a.curstep<>1 and a.acthandlestate=1");
		this.addSqlWhere(version, sql, args, " and task.version=? ");
		this.addSqlWhereIn(new Object[]{0,1,4}, sql, args, "\n and task.state in ?");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(startDate, sql, args, "\n and substr(a.endtime,0,10)>=?");
		this.addSqlWhere(endDate, sql, args, "\n and substr(a.endtime,0,10)<=?");
		sql.append("\n 		)a group by a.Depid");
		
		sql.append("\n ) c on a.id=c.depId");
		//创建任务统计
		sql.append("\n 	where 1=1");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		sql.append("\n 	order by nvl(b.createNum,0)+nvl(c.finishNum,0) desc");
		return this.listQuery(sql.toString(), args.toArray(), StatisticTaskVo.class);
	}  
	

	/**
	 * 按照部门统计指定时间段创建和办结的
	 * @param sessionuser 当前操作人员
	 * @param startDate 查询的开始时间
	 * @param endDate 查询的结束时间
	 * @param version 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StatisticTaskVo> statisticTaskTop(UserInfo sessionuser,String startDate,String endDate, String version){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.totalNum,a.userId,u.username from (");
		sql.append("\n 		select a.userId,count(*) totalNum from (");
		sql.append("\n 			select a.taskid,a.fromuser userId");
		sql.append("\n 			from taskhandover a");
		sql.append("\n 			inner join task on a.taskId=task.id");
		sql.append("\n  		where 1=1 and task.delstate=0");
		this.addSqlWhere(version, sql, args, " and task.version=? ");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		sql.append("\n 			and substr(a.recordcreatetime,0,10) between ? and ?");
		args.add(startDate);
		args.add(endDate);
		sql.append("\n 			union");
		sql.append("\n 			select a.taskid,a.touser userId");
		sql.append("\n 			from taskhandover a");
		sql.append("\n 			inner join task on a.taskId=task.id");
		sql.append("\n  		where 1=1 and task.delstate=0");
		this.addSqlWhere(version, sql, args, " and task.version=? ");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		sql.append("\n 			and substr(a.recordcreatetime,0,10) between ? and ?");
		args.add(startDate);
		args.add(endDate);
		sql.append("\n 		)a ");
		sql.append("\n 		where 1=1");
		sql.append("\n 		group by a.userId");
		sql.append("\n 		order by count(*) desc");
		sql.append("\n )a inner join userinfo u on a.userid=u.id");
		sql.append("\n where rownum<9");
		sql.append("\n order by totalNum desc");
		return this.listQuery(sql.toString(), args.toArray(), StatisticTaskVo.class);
	} 
	/**
	 * 运营分析之任务分类
	 * @param sessionuser
	 * @param dataDicList
	 * @param version 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StatisticTaskVo> statisticTaskGrade(UserInfo sessionuser, List<DataDic> dataDicList, String version){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		sql.append("\n select a.grade,a.gradename,nvl(b.totalNum,0) totalNum,nvl(c.overNums,0)overNums");
		sql.append("\n from (");
		for (int i=0, len = dataDicList.size(); i < len; i++) {
			DataDic dataDic = dataDicList.get(i);
			if(dataDic.getParentId().equals(-1)){
				continue;
			}
			sql.append("\n select ? as grade, ? as gradename from dual");
			args.add(dataDic.getCode());
			args.add(dataDic.getZvalue());
			if(i < len-1){
				sql.append("\n union all");
			}
		}
		sql.append("\n	) a ");
		sql.append("\n left join (");
		sql.append("\n 		select a.grade,count(*) totalNum ");
		sql.append("\n 		from task a");
		sql.append("\n 		where 1=1 and a.delstate=0");
		this.addSqlWhere(version, sql, args, " and a.version=? ");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		sql.append("\n 		group by a.grade");
		sql.append("\n	)b on a.grade = b.grade");
		
		sql.append("\n left join (");
		sql.append("\n 		select a.grade,count(*) overNums ");
		sql.append("\n 		from task a");
		sql.append("\n 		where 1=1 and a.delstate=0");
		this.addSqlWhere(version, sql, args, " and a.version=? ");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		sql.append("\n 		and (");
		sql.append("\n 		 	(a.state=1 and '"+nowDate+"'>a.dealtimelimit)");
		sql.append("\n 		)");
		sql.append("\n 		group by a.grade");
		sql.append("\n)c on a.grade = c.grade");
		
		sql.append("\n order by a.grade desc");
		return this.listQuery(sql.toString(), args.toArray(), StatisticTaskVo.class);
	}
	
	/**
	 * 运营分析之客户分类
	 * @param sessionuser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StatisticCrmVo> statisticCrmType(UserInfo sessionuser){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id crmTypeId,a.typename crmTypeName,nvl(b.crmTypeNum,0)crmTypeNum");
		sql.append("\n from customertype a ");
		sql.append("\n left join (");
		sql.append("\n 		select a.customertypeid,count(*) crmTypeNum");
		sql.append("\n 		from customer a");
		sql.append("\n 		where 1=1 and a.delstate=0");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		sql.append("\n 		group by a.customertypeid");
		sql.append("\n ) b on a.id=b.customertypeid");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionuser.getComId(), sql, args, "\n and a.comId=?");
		sql.append("\n order by a.typeorder,a.id");
		return this.listQuery(sql.toString(), args.toArray(), StatisticCrmVo.class);
	}
	
	/**
	 * 任务执行督办数据查询
	 * @param sessionUser 当前操作人员
	 * @param statisticsVo 查询条件
	 * @return
	 */
	public List<JSONObject> listPagedStatisticSupTask(
			UserInfo sessionUser, Task task) {
		
		String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		String threeDateLaterStr = DateTimeUtil.addDate(nowDateStr, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 3);
		
		//排序
		String orderBy = "a.grade desc,a.id desc";
		if(StringUtils.isNotEmpty(task.getOrderBy())){
			Integer orderByNum = Integer.parseInt(task.getOrderBy());
			switch (orderByNum) {
			case 1:
				orderBy = "a.grade desc,a.id desc";
				break;
			case 2:
				orderBy = "a.grade asc,a.id desc";
				break;
			case 3:
				orderBy = "a.overDueOrder asc,a.dealtimelimit asc nulls last ,a.id desc";
				break;
			case 4:
				orderBy = "a.overDueOrder desc,a.dealtimelimit desc nulls last ,a.id desc";
				break;
			case 5:
				orderBy = "a.overDueOrder asc,a.dealtimelimit asc nulls last ,a.id desc";
				break;
			case 6:
				orderBy = "a.overDueOrder desc,a.dealtimelimit desc nulls last ,a.id desc";
				break;
			default:
				break;
			}
		}
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,b.overDueLevel ");
		sql.append("\n ,row_number() over (order by "+ orderBy +" ) as row_number");
		sql.append("\n from (");
		sql.append("\n 		select a.id,a.taskname,a.dealtimelimit,a.grade,d.zvalue gradename");
		//逾期等级显示
		sql.append(" 		,case when a.state=4 or a.state=3 then 4 \n");
		sql.append(" 		when a.dealtimelimit is null or a.dealtimelimit>=? then 3 \n");
		args.add(threeDateLaterStr);
		sql.append(" 		when a.dealtimelimit<? and a.dealtimelimit>=? then 2\n");
		args.add(threeDateLaterStr);
		args.add(nowDateStr);
		sql.append(" 		else 1  end overDueOrder \n");
		
		sql.append("\n from task a ");
		sql.append("\n left join datadic d on a.grade=d.code and d.type='grade' ");
		sql.append("\n where a.delstate=0 and a.comId=? ");
		args.add(sessionUser.getComId());
		this.addSqlWhere(task.getVersion(), sql, args, " and a.version=? ");
		this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
		
		//任务名称模糊查询
		this.addSqlWhereLike(task.getTaskName(), sql, args, "\n and a.taskName like ?");
		
		//任务紧急程度查询
		this.addSqlWhere(task.getGrade(), sql, args, "\n and a.grade=?");
		sql.append("\n )a  ");
		sql.append("\n left join ( ");
		
		OverDueLevelEnum[] ary = OverDueLevelEnum.values();
		for (int i = 0,len = ary.length; i < len; i++) {
			sql.append("\n select ? overDueOrder, ? overDueLevel from dual ");
			args.add((i+1));
			args.add(String.valueOf(ary[i].getValue()));
			if(i < (len - 1)){
				sql.append("\n union all ");
			}
		}
		sql.append("\n )b on a.overDueOrder = b.overDueOrder");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(task.getOverDueLevel(), sql, args, "\n and a.overDueOrder=?");
		
		
		List<TaskExecutor> listTaskExecutor = task.getListTaskExecutor();
		List<Integer> executorIds = new ArrayList<Integer>();
		if(null!=listTaskExecutor && !listTaskExecutor.isEmpty()){
			for (TaskExecutor taskExecutor : listTaskExecutor) {
				executorIds.add(taskExecutor.getExecutor());
			}
		}
		
		//部门查询
		List<Department> listExecuteDep = task.getListExecuteDep();
		List<Integer> depIds = new ArrayList<Integer>();
		if(null!=listExecuteDep && !listExecuteDep.isEmpty()){
			for (Department department : listExecuteDep) {
				depIds.add(department.getId());
			}
		}
		//是否需要查询部门或人员
		boolean searchDepOrUserState = (null !=depIds && !depIds.isEmpty())
				 || (null!=executorIds && !executorIds.isEmpty());
		if(searchDepOrUserState){
			sql.append("\n and exists(");
			sql.append("\n  	select b.id from taskexecutor b");
			sql.append("\n  	inner join userorganic uorg on b.comid=uorg.comid and b.executor=uorg.userid");
			sql.append("\n  	inner join deptree executeDep on uorg.depid=executeDep.depid");
			sql.append("\n  	where b.comid=? and b.taskid=a.id and b.state<>4");
			args.add(sessionUser.getComId());
			if(null !=depIds && !depIds.isEmpty()){
				this.addSqlWhereIn(depIds.toArray(), sql, args, "\n and ( executeDep.Parentid in ?");
				this.addSqlWhereIn(depIds.toArray(), sql, args, "\n or executeDep.depId in ? )");
			}
			
			if(null!=executorIds && !executorIds.isEmpty()){
				this.addSqlWhereIn(executorIds.toArray(), sql, args, "\n and b.executor in ?");
			}
			sql.append("\n )");
		}
		//总数查询
		StringBuffer countSql = new StringBuffer();
		countSql.append("\n select count(*) from (");
		countSql.append(sql);
		countSql.append("\n )a where 1=1");
		Integer count = this.countQuery(countSql.toString(), args.toArray());
		PaginationContext.setTotalCount(count);
		
		//分页查询
		StringBuffer pagedQueryString = new StringBuffer();
		pagedQueryString.append("\n select task.id taskId,task.taskname,task.dealtimelimit,task.grade,task.gradename,task.overDueLevel  ");
		pagedQueryString.append("\n ,b.executor,u.username,b.state executeState,b.taskprogress executeprogress");
		pagedQueryString.append("\n from (");
		pagedQueryString.append("\n 	select t.*");
		pagedQueryString.append("\n 	from (");
		pagedQueryString.append(sql);
		pagedQueryString.append(" 		) t where row_number>").append(PaginationContext.getOffset());
		pagedQueryString.append("\n and row_number<=").append(PaginationContext.getOffset() + PaginationContext.getPageSize());
		pagedQueryString.append("\n order by row_number");
		pagedQueryString.append("\n )task");
		pagedQueryString.append("\n left join taskexecutor b on task.id=b.taskid");
		pagedQueryString.append("\n left join userinfo u on b.executor=u.id");
		pagedQueryString.append("\n where 1=1");
		
		if(null!=executorIds && !executorIds.isEmpty()){
			this.addSqlWhereIn(executorIds.toArray(), pagedQueryString, args, "\n and b.executor in ?");
		}
		
		if(null !=depIds && !depIds.isEmpty()){
			pagedQueryString.append("\n and exists(");
			pagedQueryString.append("\n  	select uorg.id from userorganic uorg ");
			pagedQueryString.append("\n  	inner join deptree executeDep on uorg.depid=executeDep.depid");
			pagedQueryString.append("\n  	where uorg.comid=? and uorg.userId=b.executor");
			args.add(sessionUser.getComId());
			this.addSqlWhereIn(depIds.toArray(), pagedQueryString, args, "\n and ( executeDep.Parentid in ?");
			this.addSqlWhereIn(depIds.toArray(), pagedQueryString, args, "\n or executeDep.depId in ? )");
			pagedQueryString.append("\n )");
		}
		return this.listQueryJSON(pagedQueryString.toString(), args.toArray());
	}

	/**
	 * 事项催办统计
	 * @param sessionuser 当前操作人员
	 * @param busRemind 催办的查询条件
	 * @return
	 */
	public List<JSONObject> listPagedStatisticBusRemind(UserInfo sessionUser,
			BusRemind busRemind) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//被催办的人员
		List<BusRemindUser> listBusRemindUser = busRemind.getListBusRemindUser();
		List<Integer> listRemindUser = new ArrayList<Integer>();
		if(null!=listBusRemindUser && !listBusRemindUser.isEmpty()){
			for (BusRemindUser busRemindUser : listBusRemindUser) {
				listRemindUser.add(busRemindUser.getUserId());
			}
		}
		//被催办的人员所在部门
		List<Department> listBusRemindDep = busRemind.getListBusRemindDep();
		List<Integer> listRemindDep = new ArrayList<Integer>();
		if(null!=listBusRemindDep && !listBusRemindDep.isEmpty()){
			for (Department department : listBusRemindDep) {
				listRemindDep.add(department.getId());
			}
		}
		sql.append("\n select a.busId,a.busType,b.busmodname,nvl(a.busremindTimes,0) busremindTimes");
		sql.append("\n ,a.remindUserId");
		sql.append("\n ,row_number() over (order by nvl(a.busremindTimes,0) desc ) as row_number");
		sql.append("\n from (");
		sql.append("\n 		select a.busId,a.busType,b.userid remindUserId,count(*) busremindTimes ");
		sql.append("\n 		from busremind a");
		sql.append("\n 		inner join busreminduser b on a.id=b.busremindid and a.comId=b.comId");
		sql.append("\n 		inner join userorganic uorg on b.comId=uorg.comId and b.userid=uorg.userId");
		sql.append("\n 		where 1=1 and a.comId=?");
		args.add(sessionUser.getComId());
		this.addSqlWhereLike(busRemind.getBusModName(), sql, args, "\n and a.busModName like ?");
		
		//查询创建时间段
		this.addSqlWhere(busRemind.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(busRemind.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		if(null!=listRemindDep && !listRemindDep.isEmpty()){
			sql.append("\n  and exists(");
			sql.append("\n  select id from depTree where depTree.depId=uorg.depId ");
			this.addSqlWhereIn(listRemindDep.toArray(new Integer[listRemindDep.size()]), sql, args, "\n and (depTree.parentId in ?");
			this.addSqlWhereIn(listRemindDep.toArray(new Integer[listRemindDep.size()]), sql, args, "\n or  depTree.depId in ? )");
			sql.append("\n  )");
		}
		if(null!=listRemindUser && !listRemindUser.isEmpty()){
			this.addSqlWhereIn(listRemindUser.toArray(new Integer[listRemindUser.size()]), sql, args, "\n and b.userid in ?");
		}
		sql.append("\n		group by a.busId,a.busType,b.userid");
		sql.append("\n ) a");
		sql.append("\n inner join (");
		sql.append("\n 		select a.busId,a.busType,a.busmodname");
		sql.append("\n 		,row_number() over (partition by a.busId,a.busType order by a.id desc) as new_order");
		sql.append("\n 		from busremind a");
		sql.append("\n 		where 1=1 and a.comId=?");
		args.add(sessionUser.getComId());
		//查询创建时间段
		this.addSqlWhere(busRemind.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(busRemind.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		this.addSqlWhereLike(busRemind.getBusModName(), sql, args, "\n and a.busModName like ?");
		
		sql.append("\n ) b on a.busId = b.busId and a.busType=b.busType and b.new_order=1");
		sql.append("\n where 1=1");
		
		//总数查询
		StringBuffer countSql = new StringBuffer();
		countSql.append("\n select count(*) from (");
		countSql.append(sql);
		countSql.append("\n )a where 1=1");
		Integer count = this.countQuery(countSql.toString(), args.toArray());
		PaginationContext.setTotalCount(count);
		//分页查询
		StringBuffer pagedQueryString = new StringBuffer();
		pagedQueryString.append("\n select a.*,uorg.depid,dep.depName,u.username");
		pagedQueryString.append("\n from (");
		pagedQueryString.append("\n 	select t.*");
		pagedQueryString.append("\n 	from (");
		pagedQueryString.append(sql);
		pagedQueryString.append(" 		) t where row_number>").append(PaginationContext.getOffset());
		pagedQueryString.append("\n and row_number<=").append(PaginationContext.getOffset() + PaginationContext.getPageSize());
		pagedQueryString.append("\n ) a");
		pagedQueryString.append("\n inner join userorganic uorg on uorg.comId=? and a.remindUserId=uorg.userid");
		args.add(sessionUser.getComId());
		pagedQueryString.append("\n inner join department dep on uorg.comid=dep.comid and uorg.depid=dep.id");
		pagedQueryString.append("\n inner join userinfo u on a.remindUserId=u.id ");
		pagedQueryString.append("\n where 1=1");
		pagedQueryString.append("\n order by a.busremindTimes desc");
		return this.listQueryJSON(pagedQueryString.toString(), args.toArray());
	}

	/**
	 * 日报汇报汇总
	 * @param daily
	 * @param curUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Daily> listPagedDaily(Daily dailyPojo, UserInfo curUser) {
        List<Object> args = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer();

        sql.append("\n select d.id,a.comid,b.username,c.depname,? as dailydate,substr(d.recordcreatetime,0,10) as recordcreatetime,d.version,");
        args.add(dailyPojo.getDailyDate());
        sql.append("\n case when (substr(d.recordcreatetime,0,10)=dailydate) then 1");
        sql.append("\n when (substr(d.recordcreatetime,0,10)>dailydate) then 2");
        sql.append("\n else 0 end submitState");
        sql.append("\n from userorganic a ");
        sql.append("\n inner join userinfo b on a.userid=b.id and a.enabled=1");
        sql.append("\n left join department c on a.comid=c.comid and a.depid=c.id");
        sql.append("\n left join daily d on a.comid=d.comid and a.userid=d.reporterid and d.dailydate=?");
        args.add(dailyPojo.getDailyDate());
        sql.append("\n where a.comid=?");
        args.add(curUser.getComId());
        //发布状态
  		Integer submitState = dailyPojo.getSubmitState();
  		if(null != submitState){
  			if(submitState == 1){
  		        sql.append("\n and d.recordcreatetime is not null");
  			}else if(submitState == 2) {
  		        sql.append("\n and substr(d.recordcreatetime,0,10) > dailydate");
  			}else{
  		        sql.append("\n and d.recordcreatetime is null");
  			}
  		}
        //汇报人筛选
        this.addSqlWhereIn(dailyPojo.getOwnerArray(), sql, args, "\n and a.userid in ?");
        //汇报部门筛选
        this.addSqlWhereIn(dailyPojo.getListTreeDeps(), sql, args, "\n and a.depid in ?");
        
        return this.pagedQuery(sql.toString(), "a.depid", args.toArray(), Daily.class);
    }
	
	/**
	 * 周报发布情况统计
	 * @param weekReport
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<WeekReport> listWeekRepStatistics(WeekReportPojo weekReport,UserInfo userInfo) throws ParseException {

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("\n select * from(");
		sql.append("\n select reports.*,f.depname,");
		sql.append("\n case when reports.createDate is null then 0");
		sql.append("\n 	when TO_date(reports.createDate,'yyyy-mm-dd')+reports.timeType <= TO_date(reports.weeke,'yyyy\"年\"mm\"月\"dd\"日\"') then 1");
		sql.append("\n 	else 2 end submitState 	 ");
		sql.append("\n 	from ( ");
		sql.append("\n 		select  a.*,b.userid reporterId,d.gender,b.depId,d.username,substr(b.recordcreatetime,0,10) registInTime,");
		sql.append("\n 			  case when c.state = 0 then substr(c.recordcreatetime,0,10) else '' end createDate, ");
		sql.append("\n 			  case when c.state = 0 then nvl(c.id,0) else 0 end id,");
		sql.append("\n 		case when s.timeType is null  then '-1' else s.timeType end timeType");

		sql.append("\n 		from (");
		//选出企业中已发布周报的周数，年份，周数起止时间
		sql.append("\n select a.comid,a.weeknum,a.year,a.weeks,a.weeke  from weekReport a where a.comid=? and a.state=0");
		args.add(userInfo.getComId());
		sql.append("\n group by a.comid,a.weeknum,a.year,a.weeks,a.weeke ");

		sql.append("\n )a");

		sql.append("\n 	left join subtimeset s on  s.comid=a.comId");
		sql.append("\n	left join userorganic b on a.comid=b.comid and b.enabled=1 and b.INSERVICE =1 ");
		sql.append("\n	left join weekreport c on a.comid=c.comid and a.weeknum=c.weeknum and a.year=c.year and b.userid=c.reporterid");
		sql.append("\n 	left join userinfo d on b.userid=d.id");
		
		sql.append("\n 	)reports");
		sql.append("\n 	 left join department f on reports.comid=f.comid and reports.depid=f.id");


		sql.append("\n where 1=1 \n");
		//周报时间范围小于人员入职时间，不查询
		sql.append("\n  and TO_date(reports.weeke, 'yyyy\"年\"mm\"月\"dd\"日\"')>=TO_date(reports.registInTime, 'yyyy-mm-dd')");

		//查询部门的
		if(null!=weekReport.getDepId()){
			sql.append("and f.id in ( select id from department \n");
			sql.append("start with id="+weekReport.getDepId()+" connect by prior id = parentid \n");
			sql.append(")\n");
		}

		//部门查询
		this.addSqlWhereIn(weekReport.getListTreeDeps(), sql, args, "\n and f.id in ?");

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
		this.addSqlWhere(weekReport.getStartDate(), sql, args, " and reports.weekS>=?");
		//查询时间止
		this.addSqlWhere(weekReport.getEndDate(), sql, args, " and reports.weekE<=?");

		this.addSqlWhere(weekReport.getWeekYear(), sql, args, " and reports.year =?");
		if(null != weekReport.getWeekNum()){//不限周数条件
			this.addSqlWhere(weekReport.getWeekNum(), sql, args, " and reports.weekNum =?");
		}
		sql.append("	 )a where 1=1 ");
		//发布状态
		Integer submitState = weekReport.getSubmitState();
		if(null != submitState){
			if(submitState == 1){
				this.addSqlWhereIn(new Object[]{1,2}, sql, args, " and a.submitState in?");
			}else{
				this.addSqlWhere(weekReport.getSubmitState(), sql, args, " and a.submitState =?");
			}
		}
		return this.pagedQuery(sql.toString(), "a.year desc,a.weekNum desc,a.depId asc,a.reporterId asc,submitState desc " , args.toArray(), WeekReport.class);
	}

	
	/**
	 * 分页查询需求落实监督数据
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 
	 * @return
	 */
	public PageBean<DemandProcess> listPagedStatisticDemandProcess(
			UserInfo sessionUser, DemandProcess demandProcess) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<>();
		
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		
		sql.append("\n select a.id,a.comid,a.recordcreatetime,a.serialnum,a.startLevel");
		sql.append("\n ,a.type,dic.zvalue as typename,a.state");
		sql.append("\n ,a.itemid, item.itemname,a.handleUser,b.username as handleUserName");
		sql.append("\n ,hisDemand.recordcreatetime as stageStartDateTime,hisDemand.state as stageState");
		sql.append(",to_number(to_date(?, 'yyyy-mm-dd HH24:MI:SS')- to_date(nvl(hisDemand.recordcreatetime,?), 'yyyy-mm-dd HH24:MI:SS')) * 86400000 stageCostTime ");
		args.add(nowDateTime);
		args.add(nowDateTime);
		
		sql.append("\n from demandprocess a ");
		sql.append("\n inner join item on a.itemid=item.id");
		sql.append("\n inner join userinfo b on a.handleUser=b.id");
		sql.append("\n left join datadic dic on a.type=dic.code and dic.type='demandType'");
		sql.append("\n left join demandHandleHis hisDemand on a.id=hisDemand.demandId and a.comId=hisDemand.comId and hisDemand.curStep=1 ");
		sql.append("\n where a.comId=?");
		args.add(sessionUser.getComId());
		
		//查询创建时间段
		this.addSqlWhere(demandProcess.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(demandProcess.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		//查詢类型
		this.addSqlWhere(demandProcess.getType(), sql, args, " and a.type=?");
		
		this.addSqlWhereLike(demandProcess.getSerialNum(), sql, args, "\n and a.serialnum like ? ");
		
		//查询发布人员
		List<UserInfo> listCreator = demandProcess.getListCreator();
		if(null != listCreator && !listCreator.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>();
			for (UserInfo userInfo : listCreator) {
				userIds.add(userInfo.getId());
			}
			this.addSqlWhereIn(userIds.toArray(), sql, args, "\n and a.creator in ?");
		}
		
		//查询产品
		List<Product> listProduct = demandProcess.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> productIds = new ArrayList<Integer>();
			for (Product product : listProduct) {
				productIds.add(product.getId());
			}
			this.addSqlWhereIn(productIds.toArray(), sql, args, "\n and a.productId in ?");
		}
		//查詢項目
		List<Item> listItem = demandProcess.getListItem();
		if(null != listItem && !listItem.isEmpty()){
			List<Integer> itemIds = new ArrayList<Integer>();
			for (Item item : listItem) {
				itemIds.add(item.getId());
			}
			this.addSqlWhereIn(itemIds.toArray(), sql, args, "\n and a.itemId in ?");
		}
		
		return this.pagedBeanQuery(sql.toString(), " a.id desc", args.toArray(), DemandProcess.class);
	}

	/**
	 * 分页查询在建项目监督数据
	 * @param sessionUser
	 * @param item
	 * @return
	 */
	public PageBean<Item> listPagedStatisticItem(UserInfo sessionUser, Item item) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 分页查询任务执行监督数据
	 * @param sessionUser
	 * @param item
	 * @return
	 */
	public PageBean<Task> listPagedStatisticTask(UserInfo sessionUser, Task task) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 分页查询任务执行监督数据
	 * @param sessionUser
	 * @param item
	 * @return
	 */
	public PageBean<Task> listPagedStatisticTaskByExecutor(UserInfo sessionUser, Task task) {
		// TODO Auto-generated method stub
		return null;
	}
}
