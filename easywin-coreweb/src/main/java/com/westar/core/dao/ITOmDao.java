package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.westar.base.enums.EventDegreeEnum;
import com.westar.base.enums.EventStatusEnum;
import com.westar.base.enums.IssueEndCodeEnum;
import com.westar.base.enums.IssueStatusEnum;
import com.westar.base.enums.ModifyEndCodeEnum;
import com.westar.base.enums.MonthEnum;
import com.westar.base.enums.QuarterEnum;
import com.westar.base.enums.ReleaseEndCodeEnum;
import com.westar.base.model.EventPm;
import com.westar.base.model.IssuePm;
import com.westar.base.model.ModifyPm;
import com.westar.base.model.ReleasePm;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.EventPMTable;
import com.westar.base.pojo.IssuePMTable;
import com.westar.base.pojo.ModifyPMTable;
import com.westar.base.pojo.ReleasePMTable;
import com.westar.base.util.ConstantInterface;

@Repository
public class ITOmDao extends BaseDao {
	/**
	 * 分页查询事件管理
	 * @param sessionUser 当前操作人员
	 * @param eventPm 事件管理过程查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EventPm> listPagedEventPm(UserInfo sessionUser, EventPm eventPm) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//事件主键，事件
		sql.append("\n select A.RECORDCREATETIME,A.ID,A.EVENTID,A.CRMNAME,A.EVENTATTR,A.EVENTSOURCE,A.EVENTTYPE, ");
		sql.append("\n A.EVENTSTATUS,A.STATUS,A.EVENTPRIORITYDEGREE, ");
		sql.append("\n A.BUSTYPE,A.BUSID,A.BUSMODNAME, ");
		//流程名称，流程状态
		sql.append("\n A.INSTANCEID,SPFLOW.FLOWNAME,SPFLOW.FLOWSTATE, ");
		sql.append("\n A.CREATOR,U.USERNAME CREATORNAME");
		sql.append("\n from EVENTPM A");
		//关联的审批流程
		sql.append("\n inner join SPFLOWINSTANCE SPFLOW ON A.INSTANCEID=SPFLOW.ID AND A.COMID=SPFLOW.COMID AND SPFLOW.FLOWSTATE<>0");
		sql.append("\n inner join USERINFO U ON A.CREATOR=U.ID");
		sql.append("\n where 1=1");
		
		//发起人查询
		List<UserInfo> listCreator = eventPm.getListCreator();
		if(null!=listCreator && !listCreator.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>();
			for (UserInfo creator : listCreator) {
				userIds.add(creator.getId());
			}
			Integer[] userIdArray = new Integer[userIds.size()];
			this.addSqlWhereIn(userIds.toArray(userIdArray), sql, args, "\n and a.creator in ?");
		}
		//查询创建时间段
		this.addSqlWhere(eventPm.getStartDate(), sql, args, " and substr(a.RECORDCREATETIME,0,10)>=?");
		this.addSqlWhere(eventPm.getEndDate(), sql, args, " and substr(a.RECORDCREATETIME,0,10)<=?");
		//流程查询
		this.addSqlWhereLike(eventPm.getFlowName(), sql, args, "\n and SPFLOW.flowname like ?");
		
		//事件级别
		this.addSqlWhere(eventPm.getEventPriorityDegree(), sql, args, "\n and A.EVENTPRIORITYDEGREE=?");
		//流程状态
		this.addSqlWhere(eventPm.getStatus(), sql, args, "\n and A.STATUS=?");
		
		//事件状态
		String eventStatus = eventPm.getEventStatus();
		if(StringUtils.isNotEmpty(eventStatus)){
			this.addSqlWhereIn(eventStatus.split(","), sql, args, "\n and A.EVENTSTATUS in ?");
		}
		//关联项目查询
		this.addSqlWhere(eventPm.getBusId(), sql, args, "\n and A.busId=?");
		this.addSqlWhere(eventPm.getBusType(), sql, args, "\n and A.bustype=?");
		
		//重复事件标记
		String eventRepetitionMark = eventPm.getEventRepetitionMark();
		if(StringUtils.isNotEmpty(eventRepetitionMark)){
			if(ConstantInterface.SPSTEP_CHECK_YES.equals(eventRepetitionMark)){
				sql.append("\n and a.eventRepetitionMark is not null");
			}else{
				sql.append("\n and a.eventRepetitionMark is null");
			}
		}
				
		
		return this.pagedQuery(sql.toString(), " A.ID DESC", args.toArray(), EventPm.class);
	}
	/**
	 * 分页查询问题过程管理
	 * @param sessionUser 当前操作人员
	 * @param issuePm 问题管理查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IssuePm> listPagedIssuePm(UserInfo sessionUser, IssuePm issuePm) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//事件主键，事件
		sql.append("\n select A.RECORDCREATETIME,A.ID,A.ISSUEID,A.CRMNAME,A.ISSUESOURCE,A.ISSUETYPE, ");
		sql.append("\n A.ISSUESTATUS,A.STATUS,A.ISSUEENDCODE, ");
		sql.append("\n A.BUSTYPE,A.BUSID,A.BUSMODNAME, ");
		//流程名称，流程状态
		sql.append("\n A.INSTANCEID,SPFLOW.FLOWNAME,SPFLOW.FLOWSTATE, ");
		sql.append("\n A.CREATOR,U.USERNAME CREATORNAME");
		sql.append("\n from ISSUEPM A");
		//关联的审批流程
		sql.append("\n inner join SPFLOWINSTANCE SPFLOW ON A.INSTANCEID=SPFLOW.ID AND A.COMID=SPFLOW.COMID AND SPFLOW.FLOWSTATE<>0");
		sql.append("\n inner join USERINFO U ON A.CREATOR=U.ID");
		sql.append("\n where 1=1");
		//发起人查询
		List<UserInfo> listCreator = issuePm.getListCreator();
		if(null!=listCreator && !listCreator.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>();
			for (UserInfo creator : listCreator) {
				userIds.add(creator.getId());
			}
			Integer[] userIdArray = new Integer[userIds.size()];
			this.addSqlWhereIn(userIds.toArray(userIdArray), sql, args, "\n and a.creator in ?");
		}
		//查询创建时间段
		this.addSqlWhere(issuePm.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(issuePm.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//查询关闭时间段
		this.addSqlWhere(issuePm.getStartResolveDate(), sql, args, " and substr(a.issueCloseDate,0,10)>=?");
		this.addSqlWhere(issuePm.getEndResolveDate(), sql, args, " and substr(a.issueCloseDate,0,10)<=?");
		//流程查询
		this.addSqlWhereLike(issuePm.getFlowName(), sql, args, "\n and SPFLOW.flowname like ?");
		
		//流程状态
		this.addSqlWhere(issuePm.getStatus(), sql, args, "\n and A.STATUS=?");
		//问题状态
		this.addSqlWhere(issuePm.getIssueStatus(), sql, args, "\n and A.ISSUESTATUS=?");
		if(null!=issuePm.getIssueStatus()){
			sql.append("\n 		and A.ISSUEENDCODE<>?");
			args.add(IssueEndCodeEnum.CANCEL.getDesc());
		}
		
		
		//问题结束代码
		String issueEndCode = issuePm.getIssueEndCode();
		if(StringUtils.isNotEmpty(issueEndCode)){
			this.addSqlWhereIn(issueEndCode.split(","), sql, args, "\n and A.ISSUEENDCODE in ?");
		}
		//关联项目查询
		this.addSqlWhere(issuePm.getBusId(), sql, args, "\n and A.busId=?");
		this.addSqlWhere(issuePm.getBusType(), sql, args, "\n and A.bustype=?");
		//问题事件标记
		String issueRepetitionMark = issuePm.getIssueRepetitionMark();
		if(StringUtils.isNotEmpty(issueRepetitionMark)){
			if(ConstantInterface.SPSTEP_CHECK_YES.equals(issueRepetitionMark)){
				sql.append("\n and a.issueRepetitionMark is not null");
			}else{
				sql.append("\n and a.issueRepetitionMark is null");
			}
		}
				
		return this.pagedQuery(sql.toString(), " A.ID DESC", args.toArray(), IssuePm.class);
	}
	/**
	 * 分页查询变更过程管理
	 * @param sessionUser 当前操作人员
	 * @param modifyPm 变更管理查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModifyPm> listPagedModifyPm(UserInfo sessionUser, ModifyPm modifyPm) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select A.* from ( ");
		sql.append("\n 		select A.RECORDCREATETIME,A.ID,A.MODIFYID,A.MODIFYSOURCE,A.MODIFYTYPE, ");
		sql.append("\n 			A.MODIFYSTATUS,A.STATUS,");
		sql.append("\n A.BUSTYPE,A.BUSID,A.BUSMODNAME, ");
		//流程名称，流程状态
		sql.append("\n			A.INSTANCEID,SPFLOW.FLOWNAME,SPFLOW.FLOWSTATE, ");
		sql.append("\n 			A.CREATOR,U.USERNAME CREATORNAME,");
		sql.append("\n 			case when a.modifyPlanEndTimes is null then 1");
		sql.append("\n 				when a.modifyStartTimes is null then 1");
		sql.append("\n 				when to_date(a.modifyPlanEndTimes,'yyyy-MM-dd HH24:mi:ss')<=");
		sql.append("\n 					to_date(a.modifyStartTimes,'yyyy-MM-dd HH24:mi:ss') then 1");
		sql.append("\n 				else 0 end scheduleState");
		sql.append("\n from MODIFYPM A");
		//关联的审批流程
		sql.append("\n inner join SPFLOWINSTANCE SPFLOW ON A.INSTANCEID=SPFLOW.ID AND A.COMID=SPFLOW.COMID AND SPFLOW.FLOWSTATE<>0");
		sql.append("\n inner join USERINFO U ON A.CREATOR=U.ID");
		sql.append("\n where 1=1");
		
		//发起人查询
		List<UserInfo> listCreator = modifyPm.getListCreator();
		if(null!=listCreator && !listCreator.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>();
			for (UserInfo creator : listCreator) {
				userIds.add(creator.getId());
			}
			Integer[] userIdArray = new Integer[userIds.size()];
			this.addSqlWhereIn(userIds.toArray(userIdArray), sql, args, "\n and a.creator in ?");
		}
		//查询创建时间段
		this.addSqlWhere(modifyPm.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(modifyPm.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//流程查询
		this.addSqlWhereLike(modifyPm.getFlowName(), sql, args, "\n and SPFLOW.flowname like ?");
		
		//变更流程状态
		Integer status = modifyPm.getStatus();
		if(null!=status && -14 == status){
			//流程状态
			this.addSqlWhereIn(new Object[]{-1,4}, sql, args, "\n and A.STATUS in ?");
			
		}else{
			//流程状态
			this.addSqlWhere(modifyPm.getStatus(), sql, args, "\n and A.STATUS=?");
		}
		
		//非法变更标识
		String illegalState = modifyPm.getIllegalState();
		if(StringUtils.isNotEmpty(illegalState) && ConstantInterface.SYS_ENABLED_YES.equals(illegalState)){
			sql.append("\n 	and( a.STATUS =? or (");
			args.add(ConstantInterface.COMMON_NO);
			sql.append("\n 		a.STATUS =? and a.modifyEndCode=? ");
			args.add(ConstantInterface.SP_STATE_FINISH);
			args.add(ModifyEndCodeEnum.CANCEL.getDesc());
			sql.append("\n 		)  ");
			sql.append("\n 	)  ");
		}
		//关联项目查询
		this.addSqlWhere(modifyPm.getBusId(), sql, args, "\n and A.busId=?");
		this.addSqlWhere(modifyPm.getBusType(), sql, args, "\n and A.bustype=?");
		
		//变更结束标识
		this.addSqlWhere(modifyPm.getModifyEndCode(), sql, args, "\n and A.modifyEndCode=?");
		sql.append("\n)a  where 1=1");
		
		//是否按时
		this.addSqlWhere(modifyPm.getScheduleState(), sql, args, "\n and a.scheduleState=?");
		return this.pagedQuery(sql.toString(), " A.ID DESC", args.toArray(), ModifyPm.class);
	}
	/**
	 * 分页查询变更过程管理
	 * @param sessionUser 当前操作人员
	 * @param releasePm 发布管理查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReleasePm> listPagedReleasePm(UserInfo sessionUser,ReleasePm releasePm) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n SELECT A.* from ( ");
		sql.append("\n 		SELECT A.RECORDCREATETIME,A.ID,A.RELEASEID,A.RELEASESOURCE,A.RELEASETYPE, ");
		sql.append("\n 			A.RELEASESTATUS,A.STATUS, ");
		sql.append("\n 			A.BUSTYPE,A.BUSID,A.BUSMODNAME, ");
		//流程名称，流程状态
		sql.append("\n 			A.INSTANCEID,SPFLOW.FLOWNAME,SPFLOW.FLOWSTATE, ");
		sql.append("\n 			A.CREATOR,U.USERNAME CREATORNAME,");
		sql.append("\n 			CASE WHEN A.RELEASEENDTIMES IS NULL THEN 1");
		sql.append("\n 				WHEN A.RELEASEPLANENDTIMES IS NULL THEN 1");
		sql.append("\n 				when to_date(a.releasePlanEndTimes,'yyyy-MM-dd HH24:mi:ss')<=");
		sql.append("\n 					to_date(a.releaseEndTimes,'yyyy-MM-dd HH24:mi:ss') then 1");
		sql.append("\n 				else 0 end scheduleState");
		sql.append("\n from RELEASEPM A");
		//关联的审批流程
		sql.append("\n inner join SPFLOWINSTANCE SPFLOW ON A.INSTANCEID=SPFLOW.ID AND A.COMID=SPFLOW.COMID AND SPFLOW.FLOWSTATE<>0");
		sql.append("\n inner join USERINFO U ON A.CREATOR=U.ID");
		sql.append("\n where 1=1");
		//发起人查询
		List<UserInfo> listCreator = releasePm.getListCreator();
		if(null!=listCreator && !listCreator.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>();
			for (UserInfo creator : listCreator) {
				userIds.add(creator.getId());
			}
			Integer[] userIdArray = new Integer[userIds.size()];
			this.addSqlWhereIn(userIds.toArray(userIdArray), sql, args, "\n and a.creator in ?");
		}
		//查询创建时间段
		this.addSqlWhere(releasePm.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(releasePm.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//流程查询
		this.addSqlWhereLike(releasePm.getFlowName(), sql, args, "\n and SPFLOW.flowname like ?");
		//流程状态
		this.addSqlWhere(releasePm.getStatus(), sql, args, "\n and A.STATUS=?");
		//发布结束标识
		this.addSqlWhere(releasePm.getReleaseEndCode(), sql, args, "\n and A.releaseEndCode=?");
		//关联项目查询
		this.addSqlWhere(releasePm.getBusId(), sql, args, "\n and A.busId=?");
		this.addSqlWhere(releasePm.getBusType(), sql, args, "\n and A.bustype=?");
		sql.append("\n)a where 1=1");
		//是否按时
		this.addSqlWhere(releasePm.getScheduleState(), sql, args, "\n and a.scheduleState=?");
		return this.pagedQuery(sql.toString(), " A.ID DESC", args.toArray(), ReleasePm.class);
	}
	
	/**
	 * 事件管理过程分析
	 * @param eventPm 事件管理过程
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EventPMTable> analyseEventPm(EventPm eventPm,UserInfo sessionUser){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select EVENTPMMOD.EVENTPRIORITYDEGREE PRIORITYDEGREE,EVENTPMMOD.ORDERLEVEL DEGREELEVEL,  ");
		sql.append("\n  NVL(EVENTPM.EVENTPMNUM,0) DEGREENUM,nvl(EVENTPMRESPONE.TOTALRESPONETIME,0) TOTALRESPONETIME,");
		sql.append("\n  NVL(EVENTPMRESOLVE.RESOLVENUM,0) RESOLVENUM,nvl(RESOLVE.TOTALRESOVETIME,0) TOTALRESOVETIME");
		sql.append("\n  from (");
		
		//事件级别类型
		List<Map<String, Object>> list = EventDegreeEnum.toList();
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			sql.append("\n		select ? EVENTPRIORITYDEGREE,? ORDERLEVEL from dual");
			args.add(map.get("desc"));
			args.add(map.get("value"));
			if(i < list.size()-1){
				sql.append("\n		union all");
			}
		}
		sql.append("\n ) EVENTPMMOD  left join (");
		//各个级别事件数目
		sql.append("\n		select EVENTPRIORITYDEGREE,COUNT(EVENTPRIORITYDEGREE) EVENTPMNUM");
		sql.append("\n		from EVENTPM");
		sql.append("\n  	where EVENTPM.COMID=? and EVENTPM.STATUS=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n 		AND EVENTPM.EVENTREPETITIONMARK IS NULL");
		this.addSqlWhere(eventPm.getBusId(), sql, args, "\n and EVENTPM.busId=?");
		this.addSqlWhere(eventPm.getBusType(), sql, args, "\n and EVENTPM.bustype=?");
		sql.append("\n 		group by EVENTPM.EVENTPRIORITYDEGREE");
		sql.append("\n 	)EVENTPM  on EVENTPM.EVENTPRIORITYDEGREE=EVENTPMMOD.EVENTPRIORITYDEGREE");
		sql.append("\n  left join (");
		//响应时间
		sql.append("\n   select eventPriorityDegree,");
		sql.append("\n  	sum(");
		sql.append("\n  	 case when eventpm.eventTime is null then 0");
		sql.append("\n  	 	when to_date(eventpm.recordcreatetime,'yyyy-MM-dd HH24:mi:ss')< ");
		sql.append("\n  	  		to_date(eventpm.eventTime,'yyyy-MM-dd HH24:mi') then 0 ");
		sql.append("\n  		else (to_date(eventpm.recordcreatetime,'yyyy-MM-dd HH24:mi:ss') - ");
		sql.append("\n  	 		to_date(eventpm.eventTime,'yyyy-MM-dd HH24:mi')) * 24 * 60 * 60 * 1000 end) totalResponeTime");
		sql.append("\n		from EVENTPM");
		sql.append("\n  	where EVENTPM.COMID=? and EVENTPM.STATUS=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n 		and EVENTPM.EVENTREPETITIONMARK IS NULL");
		this.addSqlWhere(eventPm.getBusId(), sql, args, "\n and EVENTPM.busId=?");
		this.addSqlWhere(eventPm.getBusType(), sql, args, "\n and EVENTPM.bustype=?");
		sql.append("\n 		group by EVENTPM.EVENTPRIORITYDEGREE");
		sql.append("\n  )EVENTPMRESPONE on EVENTPMRESPONE.EVENTPRIORITYDEGREE=EVENTPMMOD.EVENTPRIORITYDEGREE");
		sql.append("\n  left join (");
		//各个级别事件数目
		sql.append("\n		select EVENTPRIORITYDEGREE,COUNT(EVENTPRIORITYDEGREE) RESOLVENUM");
		sql.append("\n		from EVENTPM");
		sql.append("\n  	where EVENTPM.COMID=? and EVENTPM.STATUS=? ");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		this.addSqlWhereIn(new Object[]{EventStatusEnum.CLOSE.getDesc(),EventStatusEnum.RESOLVE.getDesc()},
				sql, args, "and EVENTPM.EVENTSTATUS in ?");
		sql.append("\n 		AND EVENTPM.EVENTREPETITIONMARK IS NULL");
		this.addSqlWhere(eventPm.getBusId(), sql, args, "\n and EVENTPM.busId=?");
		this.addSqlWhere(eventPm.getBusType(), sql, args, "\n and EVENTPM.bustype=?");
		sql.append("\n 		group by EVENTPM.EVENTPRIORITYDEGREE");
		sql.append("\n 	)EVENTPMRESOLVE  on EVENTPMRESOLVE.EVENTPRIORITYDEGREE=EVENTPMMOD.EVENTPRIORITYDEGREE");
		
		sql.append("\n  left join (");
		//响应时间
		sql.append("\n   select eventPriorityDegree,");
		sql.append("\n  	sum(");
		sql.append("\n  	 case when eventpm.eventEndTimes is null then 0");
		sql.append("\n  	 	when to_date(eventpm.eventEndTimes,'yyyy-MM-dd HH24:mi:ss')< ");
		sql.append("\n  	  		to_date(eventpm.recordcreatetime,'yyyy-MM-dd HH24:mi:ss') then 0 ");
		sql.append("\n  		else (to_date(eventpm.eventEndTimes,'yyyy-MM-dd HH24:mi:ss') - ");
		sql.append("\n  	 		to_date(eventpm.recordcreatetime,'yyyy-MM-dd HH24:mi:ss')) * 24 * 60 * 60 * 1000 end) totalResoveTime");
		sql.append("\n		from EVENTPM");
		sql.append("\n  	where EVENTPM.COMID=? and EVENTPM.STATUS=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n 		and EVENTPM.EVENTREPETITIONMARK IS NULL");
		this.addSqlWhere(eventPm.getBusId(), sql, args, "\n and EVENTPM.busId=?");
		this.addSqlWhere(eventPm.getBusType(), sql, args, "\n and EVENTPM.bustype=?");
		sql.append("\n 		group by EVENTPM.EVENTPRIORITYDEGREE");
		sql.append("\n  )RESOLVE on RESOLVE.EVENTPRIORITYDEGREE=EVENTPMMOD.EVENTPRIORITYDEGREE");
		
		sql.append("\n where 1=1 ");
		sql.append("\n order by eventpmMod.orderLevel");
		sql.append("\n");
		sql.append("\n");
		return this.listQuery(sql.toString(), args.toArray(), EventPMTable.class);
	}
	
	/**
	 * 问题管理过程分析
	 * @param issuePm 问题管理过程
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IssuePMTable> analyseIssuePm(IssuePm issuePm,UserInfo sessionUser){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		Integer year = issuePm.getYear();
		
		sql.append("\n select ISSUEPMMOD.monthName,ISSUEPMMOD.monthOrder,ISSUEPMMOD.yearMonth,");
		sql.append("\n  nvl(issuePm.totalNum,0) totalNum,nvl(issuePmDone.resolveDoneNum,0) resolveDoneNum,");
		sql.append("\n 	nvl(issuePmClose.closeNum,0) closeNum,nvl(issuePmresolveTime.totalResoveTime,0) resolveTimes,");
		sql.append("\n 	nvl(issuePmresolve.resolveNum,0) resolveNum");
		sql.append("\n from (");
		
		//事件级别类型
		List<Map<String, Object>> list = MonthEnum.toList();
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			sql.append("\n		select ? monthName,? yearMonth,? monthOrder from dual");
			args.add(map.get("desc"));
			Integer value = Integer.parseInt(map.get("value").toString());
			if(value<10){
				args.add(year+"-0"+value);
			}else{
				args.add(year+"-"+value);
			}
			args.add(value);
			if(i < list.size()-1){
				sql.append("\n		union all");
			}
		}
		sql.append("\n ) ISSUEPMMOD  left join (");
		//问题总数
		sql.append("\n  	select substr(ISSUEPM.Recordcreatetime,0,7) tag,count(ISSUEPM.id) TOTALNUM");
		sql.append("\n  	from ISSUEPM");
		sql.append("\n  	where  ISSUEPM.COMID=? and ISSUEPM.STATUS=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n   	and ISSUEPM.ISSUEREPETITIONMARK IS NULL");
		sql.append("\n 		and ISSUEPM.ISSUEENDCODE<>?");
		args.add(IssueEndCodeEnum.CANCEL.getDesc());
		this.addSqlWhere(issuePm.getBusId(), sql, args, "\n and ISSUEPM.busId=?");
		this.addSqlWhere(issuePm.getBusType(), sql, args, "\n and ISSUEPM.bustype=?");
		sql.append("\n  	group by substr(ISSUEPM.Recordcreatetime,0,7)");
		sql.append("\n )ISSUEPM  on ISSUEPMMOD.yearMonth=ISSUEPM.tag");
		sql.append("\n left join (");
		//成功解决问题个数
		sql.append("\n 		select substr(ISSUEPM.Recordcreatetime,0,7) tag,count(ISSUEPM.id) resolveDoneNum");
		sql.append("\n 		from issuePm ");
		sql.append("\n  	where  issuePm.comid=? and issuePm.status=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n 		and issuePm.issueRepetitionMark is null");
		sql.append("\n  	and issuePm.issueEndCode=?");
		args.add(IssueEndCodeEnum.RESOLVED.getDesc());
		this.addSqlWhere(issuePm.getBusId(), sql, args, "\n and ISSUEPM.busId=?");
		this.addSqlWhere(issuePm.getBusType(), sql, args, "\n and ISSUEPM.bustype=?");
		sql.append("\n  	group by substr(ISSUEPM.Recordcreatetime,0,7)");
		sql.append("\n )issuePmDone on ISSUEPMMOD.yearMonth=issuePmDone.tag");
		
		sql.append("\n left join (");
		//关闭问题个数
		sql.append("\n 		select substr(ISSUEPM.Recordcreatetime,0,7) tag,count(ISSUEPM.id) closeNum");
		sql.append("\n 		from issuePm ");
		sql.append("\n  	where  issuePm.comid=? and issuePm.status=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n  	AND ISSUEPM.ISSUEREPETITIONMARK IS null");
		sql.append("\n 		and ISSUEPM.ISSUEENDCODE<>?");
		args.add(IssueEndCodeEnum.CANCEL.getDesc());
		sql.append("\n 		AND ISSUEPM.ISSUESTATUS=?");
		args.add(IssueStatusEnum.CLOSE.getDesc());
		this.addSqlWhere(issuePm.getBusId(), sql, args, "\n and ISSUEPM.busId=?");
		this.addSqlWhere(issuePm.getBusType(), sql, args, "\n and ISSUEPM.bustype=?");
		sql.append("\n  	group by substr(ISSUEPM.Recordcreatetime,0,7)");
		sql.append("\n )issuePmClose on ISSUEPMMOD.yearMonth=issuePmClose.tag");
		sql.append("\n left join (");
		//累加解决时间
		sql.append("\n  	select substr(ISSUEPM.Recordcreatetime,0,7) tag,sum(");
		sql.append("\n 		case when issuePm.issueCloseDate is null then 0");
		sql.append("\n 			when to_date(issuePm.issueCloseDate,'yyyy-MM-dd HH24:mi:ss')<");
		sql.append("\n 				to_date(issuePm.recordcreatetime,'yyyy-MM-dd HH24:mi:ss') then 0");
		sql.append("\n 			else (to_date(issuePm.issueCloseDate,'yyyy-MM-dd HH24:mi:ss') -");
		sql.append("\n 				to_date(issuePm.recordcreatetime,'yyyy-MM-dd HH24:mi:ss')) * 24 * 60 * 60 * 1000 end");
		sql.append("\n 		)totalResoveTime");
		sql.append("\n 		from issuePm ");
		sql.append("\n  	where  issuePm.comid=? and issuePm.status=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n  	and issuePm.issueRepetitionMark is null");
		sql.append("\n  	and issuePm.issueStatus=?");
		args.add(IssueStatusEnum.CLOSE.getDesc());
		this.addSqlWhere(issuePm.getBusId(), sql, args, "\n and ISSUEPM.busId=?");
		this.addSqlWhere(issuePm.getBusType(), sql, args, "\n and ISSUEPM.bustype=?");
		sql.append("\n  	group by substr(ISSUEPM.Recordcreatetime,0,7)");
		sql.append("\n )issuePmresolveTime on ISSUEPMMOD.yearMonth=issuePmresolveTime.tag");
		sql.append("\n left join (");
		//解决完成问题的数量
		sql.append("\n  	select substr(ISSUEPM.issueCloseDate,0,7) tag,count(ISSUEPM.id) resolveNum");
		sql.append("\n  	from issuePm");
		sql.append("\n  	where  issuePm.comid=? and issuePm.status=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n   	and issuePm.issueRepetitionMark is null");
		sql.append("\n 		and issuePm.issueEndCode<>?");
		args.add(IssueEndCodeEnum.CANCEL.getDesc());
		this.addSqlWhere(issuePm.getBusId(), sql, args, "\n and ISSUEPM.busId=?");
		this.addSqlWhere(issuePm.getBusType(), sql, args, "\n and ISSUEPM.bustype=?");
		sql.append("\n  	group by substr(ISSUEPM.issueCloseDate,0,7)");
		sql.append("\n )issuePmresolve on ISSUEPMMOD.yearMonth=issuePmresolve.tag");
		sql.append("\n where 1=1 ");
		sql.append("\n order by ISSUEPMMOD.yearMonth ");
		return this.listQuery(sql.toString(), args.toArray(), IssuePMTable.class);
	}
	/**
	 * 变更管理过程分析
	 * @param modifyPm 变更管理过程
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModifyPMTable> analyseModifyPm(ModifyPm modifyPm,UserInfo sessionUser){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//查询的年份
		Integer year = modifyPm.getYear();
		
		sql.append("\n  select MODIFYPMMOD.QUARTERNAME,MODIFYPMMOD.QUARTERLEVEL,");
		sql.append("\n  nvl(MODIFYPM.TOTALDONENUM,0) TOTALDONENUM,nvl(MODIFYPMFAIL.FAILNUM,0) FAILNUM,");
		sql.append("\n  nvl(MODIFYPMTOTAL.TOTALNUM,0) TOTALNUM,nvl(MODIFYPMCANCEL.CANCELNUM,0) CANCELNUM,");
		sql.append("\n  nvl(MODIFYPMILLEGAL.ILLEGALNUM,0) ILLEGALNUM");
		sql.append("\n  from (");
		
		//事件级别类型
		List<Map<String, Object>> list = QuarterEnum.toList();
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			sql.append("\n		select ? year, ? QUARTERNAME,? QUARTERLEVEL from DUAL");
			args.add(year);
			args.add(map.get("desc"));
			args.add(map.get("value"));
			if(i < list.size()-1){
				sql.append("\n	union all");
			}
		}
		sql.append("\n ) MODIFYPMMOD ");
		//成功变更数
		sql.append("\n left join (");
		sql.append("\n 		select MODIFYPM.QUARTERLEVEL,count(MODIFYPM.QUARTERLEVEL)TOTALDONENUM,MODIFYPM.YEAR  from (");
		sql.append("\n 			select to_char(TO_DATE(MODIFYPM.RECORDCREATETIME, 'yyyy-MM-dd HH24:mi:ss'), 'q') as QUARTERLEVEL,");
		sql.append("\n 			substr(MODIFYPM.RECORDCREATETIME,0,4) YEAR");
		sql.append("\n 			from MODIFYPM");
		sql.append("\n  		where MODIFYPM.COMID=? AND MODIFYPM.STATUS=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		this.addSqlWhere(modifyPm.getBusId(), sql, args, "\n and MODIFYPM.busId=?");
		this.addSqlWhere(modifyPm.getBusType(), sql, args, "\n and MODIFYPM.bustype=?");
		sql.append("\n 		)MODIFYPM WHERE 1=1 AND MODIFYPM.YEAR=?");
		args.add(year);
		sql.append("\n 		group by MODIFYPM.QUARTERLEVEL,MODIFYPM.YEAR");
		sql.append("\n )MODIFYPM ON MODIFYPMMOD.QUARTERLEVEL=MODIFYPM.QUARTERLEVEL and MODIFYPMMOD.year=MODIFYPM.year");
		//失败变更数
		sql.append("\n left join (");
		sql.append("\n 		select MODIFYPM.QUARTERLEVEL,count(MODIFYPM.QUARTERLEVEL)FAILNUM,MODIFYPM.YEAR  from (");
		sql.append("\n 			select to_char(TO_DATE(MODIFYPM.RECORDCREATETIME, 'yyyy-MM-dd HH24:mi:ss'), 'q') as QUARTERLEVEL,");
		sql.append("\n 			substr(MODIFYPM.RECORDCREATETIME,0,4) YEAR");
		sql.append("\n 			from MODIFYPM");
		sql.append("\n  		where MODIFYPM.COMID=? AND MODIFYPM.STATUS=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n  		and modifyPm.modifyEndCode=?");
		args.add(ModifyEndCodeEnum.FAIL.getDesc());
		this.addSqlWhere(modifyPm.getBusId(), sql, args, "\n and MODIFYPM.busId=?");
		this.addSqlWhere(modifyPm.getBusType(), sql, args, "\n and MODIFYPM.bustype=?");
		sql.append("\n 		)modifyPm where 1=1 and modifyPm.year=?");
		args.add(year);
		sql.append("\n 		group by modifyPm.quarterLevel,MODIFYPM.YEAR");
		sql.append("\n )modifyPmFail on MODIFYPMMOD.quarterLevel=modifyPmFail.quarterLevel and MODIFYPMMOD.year=modifyPmFail.year");
		//变更总数
		sql.append("\n left join (");
		sql.append("\n 		select modifyPm.quarterLevel,count(modifyPm.quarterLevel) totalNum,MODIFYPM.YEAR  from (");
		sql.append("\n 			select to_char(TO_DATE(modifyPm.Recordcreatetime, 'yyyy-MM-dd HH24:mi:ss'), 'q') as quarterLevel,");
		sql.append("\n 			substr(modifyPm.Recordcreatetime,0,4) year");
		sql.append("\n 			from modifyPm");
		sql.append("\n  		where MODIFYPM.COMID=? ");
		args.add(sessionUser.getComId());
		this.addSqlWhereIn(new Object[]{ConstantInterface.SP_STATE_FINISH,ConstantInterface.COMMON_NO},
				sql, args, " AND MODIFYPM.STATUS in ?");
		this.addSqlWhere(modifyPm.getBusId(), sql, args, "\n and MODIFYPM.busId=?");
		this.addSqlWhere(modifyPm.getBusType(), sql, args, "\n and MODIFYPM.bustype=?");
		sql.append("\n 		)modifyPm where 1=1 and modifyPm.year=?");
		args.add(year);
		sql.append("\n 		group by modifyPm.quarterLevel,MODIFYPM.YEAR");
		sql.append("\n )modifyPmTotal on MODIFYPMMOD.quarterLevel=modifyPmTotal.quarterLevel and MODIFYPMMOD.year=modifyPmTotal.year");
		//变更取消总数
		sql.append("\n left join (");
		sql.append("\n 		select modifyPm.quarterLevel,count(modifyPm.quarterLevel) cancelNum,MODIFYPM.YEAR  from (");
		sql.append("\n 			select to_char(TO_DATE(modifyPm.Recordcreatetime, 'yyyy-MM-dd HH24:mi:ss'), 'q') as quarterLevel,");
		sql.append("\n 			substr(modifyPm.Recordcreatetime,0,4) year");
		sql.append("\n 			from modifyPm");
		sql.append("\n  		where MODIFYPM.COMID=? AND MODIFYPM.STATUS=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n  		and modifyPm.modifyEndCode=?");
		args.add(ModifyEndCodeEnum.CANCEL.getDesc());
		this.addSqlWhere(modifyPm.getBusId(), sql, args, "\n and MODIFYPM.busId=?");
		this.addSqlWhere(modifyPm.getBusType(), sql, args, "\n and MODIFYPM.bustype=?");
		sql.append("\n 		)modifyPm where 1=1 and modifyPm.year=?");
		args.add(year);
		sql.append("\n 		group by modifyPm.quarterLevel,MODIFYPM.YEAR");
		sql.append("\n )modifyPmCancel on MODIFYPMMOD.quarterLevel=modifyPmTotal.quarterLevel and MODIFYPMMOD.year=modifyPmCancel.year");
		//非法变更总数
		sql.append("\n left join (");
		sql.append("\n 		select modifyPm.quarterLevel,count(modifyPm.quarterLevel) illegalNum,MODIFYPM.YEAR  from (");
		sql.append("\n 			select to_char(TO_DATE(modifyPm.Recordcreatetime, 'yyyy-MM-dd HH24:mi:ss'), 'q') as quarterLevel,");
		sql.append("\n 			substr(modifyPm.Recordcreatetime,0,4) year");
		sql.append("\n 			from modifyPm");
		sql.append("\n  		where MODIFYPM.COMID=? ");
		args.add(sessionUser.getComId());
		
		sql.append("\n  		and( MODIFYPM.STATUS =? or (");
		args.add(ConstantInterface.COMMON_NO);
		sql.append("\n  			MODIFYPM.STATUS =? and modifyPm.modifyEndCode=? ");
		args.add(ConstantInterface.SP_STATE_FINISH);
		args.add(ModifyEndCodeEnum.CANCEL.getDesc());
		sql.append("\n  			)  ");
		sql.append("\n  		)  ");
		this.addSqlWhere(modifyPm.getBusId(), sql, args, "\n and MODIFYPM.busId=?");
		this.addSqlWhere(modifyPm.getBusType(), sql, args, "\n and MODIFYPM.bustype=?");
		sql.append("\n 		)modifyPm where 1=1 and modifyPm.year=?");
		args.add(year);
		sql.append("\n 		group by modifyPm.quarterLevel,MODIFYPM.YEAR");
		sql.append("\n )modifyPmIllegal on MODIFYPMMOD.quarterLevel=modifyPmIllegal.quarterLevel and MODIFYPMMOD.year=modifyPmIllegal.year");
		sql.append("\n order by MODIFYPMMOD.quarterLevel");
		return this.listQuery(sql.toString(), args.toArray(), ModifyPMTable.class);
	}
	/**
	 * 变更管理过程分析
	 * @param eventPm 事件管理过程
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReleasePMTable> analyseReleasePm(ReleasePm releasePm,UserInfo sessionUser){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//查询的年份
		Integer year = releasePm.getYear();
				
		sql.append("\n select RELEASEPMMOD.QUARTERNAME,RELEASEPMMOD.QUARTERLEVEL, ");
		sql.append("\n NVL(RELEASEPM.TOTALNUM,0) TOTALNUM,NVL(RELEASEPMDONE.TOTALDONENUM,0) TOTALDONENUM, ");
		sql.append("\n NVL(RELEASEPMSCHEDULER.SCHEDULERNUM,0) SCHEDULERNUM,NVL(RELEASEPMSHOULD.SHOULDNUM,0) SHOULDNUM,");
		sql.append("\n NVL(RELEASEPMSCHEDULEM.SCHEDULEMNUM,0) SCHEDULEMNUM ");
		//发布总数
		sql.append("\n from (");

		//事件级别类型
		List<Map<String, Object>> list = QuarterEnum.toList();
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			sql.append("\n		select ? year, ? QUARTERNAME,? QUARTERLEVEL from DUAL");
			args.add(year);
			args.add(map.get("desc"));
			args.add(map.get("value"));
			if(i < list.size()-1){
				sql.append("\n	union all");
			}
		}
		sql.append("\n ) RELEASEPMMOD ");
		//成功变更数
		sql.append("\n left join (");
		sql.append("\n 		select releasePm.QUARTERLEVEL,count(releasePm.QUARTERLEVEL) TOTALNUM,releasePm.YEAR from (");
		sql.append("\n 			select to_char(TO_DATE(releasePm.RECORDCREATETIME, 'yyyy-MM-dd HH24:mi:ss'), 'q') as QUARTERLEVEL,");
		sql.append("\n 			substr(releasePm.RECORDCREATETIME,0,4) YEAR");
		sql.append("\n 			from releasePm");
		sql.append("\n 			where releasePm.comid=? and releasePm.status=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		this.addSqlWhere(releasePm.getBusId(), sql, args, "\n and releasePm.busId=?");
		this.addSqlWhere(releasePm.getBusType(), sql, args, "\n and releasePm.bustype=?");
		sql.append("\n 		)releasePm WHERE 1=1 AND releasePm.YEAR=?");
		args.add(year);
		sql.append("\n 		group by releasePm.QUARTERLEVEL,releasePm.YEAR");
		sql.append("\n )releasePm on RELEASEPMMOD.year=releasePm.year and RELEASEPMMOD.QUARTERLEVEL=releasePm.QUARTERLEVEL");
		//	发布成功的数量
		sql.append("\n left join (");
		sql.append("\n 		select releasePm.QUARTERLEVEL,count(releasePm.QUARTERLEVEL) totalDoneNum,releasePm.YEAR from (");
		
		sql.append("\n 			select to_char(TO_DATE(releasePm.RECORDCREATETIME, 'yyyy-MM-dd HH24:mi:ss'), 'q') as QUARTERLEVEL,");
		sql.append("\n 			substr(releasePm.RECORDCREATETIME,0,4) YEAR");
		
		sql.append("\n 			from releasePm");
		sql.append("\n 			where releasePm.comid=? and releasePm.status=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n 			and releasePm.releaseEndCode=? ");
		args.add(ReleaseEndCodeEnum.SUCCESS.getDesc());
		this.addSqlWhere(releasePm.getBusId(), sql, args, "\n and releasePm.busId=?");
		this.addSqlWhere(releasePm.getBusType(), sql, args, "\n and releasePm.bustype=?");
		sql.append("\n 		)releasePm WHERE 1=1 AND releasePm.YEAR=?");
		args.add(year);
		sql.append("\n 		group by releasePm.QUARTERLEVEL,releasePm.YEAR");
		sql.append("\n )releasePmDone on RELEASEPMMOD.year=releasePmDone.year and RELEASEPMMOD.QUARTERLEVEL=releasePmDone.QUARTERLEVEL");
		//按计划完成的发布数量
		sql.append("\n left join (");
		sql.append("\n  	select releasePm.QUARTERLEVEL,sum(releasePm.scheduleRNum)scheduleRNum,releasePm.YEAR from(");
		sql.append("\n 			select to_char(TO_DATE(releasePm.RECORDCREATETIME, 'yyyy-MM-dd HH24:mi:ss'), 'q') as QUARTERLEVEL,");
		sql.append("\n 			substr(releasePm.RECORDCREATETIME,0,4) YEAR,");
		sql.append("\n 			case when releasePm.releaseEndTimes is null then 1");
		sql.append("\n 				when releasePm.releasePlanEndTimes is null then 1");
		sql.append("\n 				when to_date(releasePm.releasePlanEndTimes,'yyyy-MM-dd HH24:mi:ss')<=");
		sql.append("\n 					to_date(releasePm.releaseEndTimes,'yyyy-MM-dd HH24:mi:ss') then 1");
		sql.append("\n 				else 0 end scheduleRNum");
		sql.append("\n 			from releasePm");
		sql.append("\n 		where releasePm.comid=? and releasePm.status=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		this.addSqlWhere(releasePm.getBusId(), sql, args, "\n and releasePm.busId=?");
		this.addSqlWhere(releasePm.getBusType(), sql, args, "\n and releasePm.bustype=?");
		sql.append("\n 		)releasePm WHERE 1=1 AND releasePm.YEAR=?");
		args.add(year);
		sql.append("\n 		group by releasePm.QUARTERLEVEL,releasePm.YEAR");
		sql.append("\n )releasePmScheduleR on RELEASEPMMOD.year=releasePmScheduleR.year and RELEASEPMMOD.QUARTERLEVEL=releasePmScheduleR.QUARTERLEVEL");
		//应执行的配置更行次数
		sql.append("\n left join (");
		sql.append("\n  	select modifyPm.QUARTERLEVEL,count(modifyPm.QUARTERLEVEL) shouldNum,modifyPm.YEAR from(");
		sql.append("\n 			select to_char(TO_DATE(modifyPm.Recordcreatetime, 'yyyy-MM-dd HH24:mi:ss'), 'q') as quarterLevel,");
		sql.append("\n 			substr(modifyPm.Recordcreatetime,0,4) year");
		sql.append("\n 			from modifyPm");
		sql.append("\n 			where modifyPm.comid=? and modifyPm.status=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		this.addSqlWhere(releasePm.getBusId(), sql, args, "\n and modifyPm.busId=?");
		this.addSqlWhere(releasePm.getBusType(), sql, args, "\n and modifyPm.bustype=?");
		sql.append("\n 		)modifyPm where 1=1 and modifyPm.year=?");
		args.add(year);
		sql.append("\n 		group by modifyPm.quarterLevel,MODIFYPM.YEAR");
		sql.append("\n )releasePmShould on RELEASEPMMOD.year=releasePmShould.year and RELEASEPMMOD.QUARTERLEVEL=releasePmShould.QUARTERLEVEL");
		//按时执行的配置更新次数
		sql.append("\n left join (");
		sql.append("\n  	select modifyPm.QUARTERLEVEL,sum(modifyPm.scheduleMNum) scheduleMNum,modifyPm.YEAR from(");
		sql.append("\n 			select to_char(TO_DATE(modifyPm.Recordcreatetime, 'yyyy-MM-dd HH24:mi:ss'), 'q') as quarterLevel,");
		sql.append("\n 			substr(modifyPm.Recordcreatetime,0,4) year,");
		sql.append("\n 			case when modifyPm.modifyPlanEndTimes is null then 1");
		sql.append("\n 				when modifyPm.modifyEndTimes is null then 1");
		sql.append("\n 				when to_date(modifyPm.modifyPlanEndTimes,'yyyy-MM-dd HH24:mi:ss')<=");
		sql.append("\n 					to_date(modifyPm.modifyEndTimes,'yyyy-MM-dd HH24:mi:ss') then 1");
		sql.append("\n 				else 0 end scheduleMNum");
		sql.append("\n 		from modifyPm");
		sql.append("\n 		where modifyPm.comid=? and modifyPm.status=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		this.addSqlWhere(releasePm.getBusId(), sql, args, "\n and modifyPm.busId=?");
		this.addSqlWhere(releasePm.getBusType(), sql, args, "\n and modifyPm.bustype=?");
		sql.append("\n 		)modifyPm where 1=1 and modifyPm.year=?");
		args.add(year);
		sql.append("\n 		group by modifyPm.quarterLevel,MODIFYPM.YEAR");
		sql.append("\n )releasePmScheduleM on RELEASEPMMOD.year=releasePmScheduleM.year and RELEASEPMMOD.QUARTERLEVEL=releasePmScheduleM.QUARTERLEVEL");
		sql.append("\n where 1=1");
		sql.append("\n order by RELEASEPMMOD.QUARTERLEVEL");
		return this.listQuery(sql.toString(), args.toArray(), ReleasePMTable.class);
	}

}
