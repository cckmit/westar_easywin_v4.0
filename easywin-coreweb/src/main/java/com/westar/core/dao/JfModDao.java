package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.JfMod;
import com.westar.base.model.JfScore;
import com.westar.base.model.JfSubUserScope;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.TaskJfStatis;
import com.westar.base.util.ConstantInterface;
import com.westar.core.web.PaginationContext;

@Repository
public class JfModDao extends BaseDao {

	/**
	 * 分页查询
	 * @param jfMod 积分模块信息
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JfMod> listPagedTaskToJf(JfMod jfMod, UserInfo sessionUser) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select handover.endTime modReaseDate, b.id,a.id busId,'"+ConstantInterface.TYPE_TASK+"' busType,a.taskName modName ");
		sql.append("\n from task a  ");
		sql.append("\n inner join (  ");
		sql.append("\n  select handover.taskid ");
		//任务移交记录
		sql.append("\n 		from (");
		sql.append("\n			select handover.fromuser userId,handover.taskid from taskhandover handover");
		sql.append("\n 		  	where handover.comId=?");
		args.add(sessionUser.getComId());
		sql.append("\n 		  	union  ");
		sql.append("\n 		 	select handover.touser userId,handover.taskid from taskhandover handover");
		sql.append("\n 		  	where handover.comId=?");
		args.add(sessionUser.getComId());
		sql.append("\n 		)handover ");
		
		//需要参与评分的的人员
		sql.append("\n 		left join (");
		//添加直属下属
		sql.append("\n 			select leader leadderId,creator subUserid from myLeaders where leader = ? and comId = ? and creator <> leader ");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 			union");
		//添加自己设定的评分人员
		sql.append("\n 			select scopeUserB.Leaderid,scopeUserB.Subuserid from jfSubUserScope scopeUserB");
		sql.append("\n 			where scopeUserB.LeaderId=? and scopeUserB.comId=? and scopeUserB.needScore='1'");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 			minus");
		//减去直属下属中被别人设定的评分的人员
		sql.append("\n			select leader leadderId,creator subUserId from myLeaders where leader = ? and comId = ? and creator <> leader ");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 			and exists(");
		sql.append("\n 				select scopeUserB.Subuserid from jfSubUserScope scopeUserB");
		sql.append("\n 				where scopeUserB.LeaderId<>? and scopeUserB.comId=? and scopeUserB.needScore='1'");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 			 	and myLeaders.creator=scopeUserB.Subuserid");
		sql.append("\n 			)");
		sql.append("\n 			minus");
		//减去自己设定过得不需要参与评分的
		sql.append("\n 			select scopeUserB.Leaderid,scopeUserB.Subuserid from jfSubUserScope scopeUserB");
		sql.append("\n 			where scopeUserB.LeaderId=? and scopeUserB.comId=? and scopeUserB.needScore='0'");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 		) userscope on handover.userId=userscope.subUserId");
		//移交人评分情况
		sql.append("\n 		left join (");
		sql.append("\n 			select count(score.dfUserId) userNum,jfMod.busId,score.dfUserId from jfScore score");
		sql.append("\n 			inner join jfMod on score.jfModId= jfMod.id");
		sql.append("\n 			where score.comId=? and jfMod.Bustype=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.TYPE_TASK);
		sql.append("\n 			group by jfMod.busId,score.dfUserId");
		sql.append("\n 		)fuser on handover.taskid=fuser.busId and handover.userId=fuser.dfUserId ");
		//同一任务
		sql.append("\n 		where 1=1 ");
		//自己是移交人或接收人上级
		sql.append("\n 		and nvl(userscope.subUserId,0)>0 ");
		//移交人未评分
		sql.append("\n 		and nvl(fuser.userNum,0)=0 ");
		sql.append("\n 		group by handover.taskid ");
		sql.append("\n ) tempTask on a.id=tempTask.taskid");
		
		
		
		sql.append("\n left join jfMod b on a.id=b.busId and b.busType=?");
		args.add(ConstantInterface.TYPE_TASK);
		sql.append("\n left join (");
		sql.append("\n select distinct max(handover.endTime) endTime,handover.taskid ");
		sql.append("\n from taskhandover handover");
		sql.append("\n where handover.comId=?");
		//团队信息
		args.add(sessionUser.getComId());
		sql.append("\n group by handover.taskid");
		sql.append("\n  )handover on a.id=handover.taskid");
		//任务已经办结
		sql.append("\n where a.state=? and a.comId=?");
		args.add(ConstantInterface.STATIS_TASK_STATE_DONE);
		//团队信息
		args.add(sessionUser.getComId());
		
		this.addSqlWhere(jfMod.getSearchYear(), sql, args, " and substr(handover.endTime,0,4)=?");
		//查询创建时间段
		this.addSqlWhere(jfMod.getStartDate(), sql, args, " and substr(handover.endTime,0,10)>=?");
		this.addSqlWhere(jfMod.getEndDate(), sql, args, " and substr(handover.endTime,0,10)<=?");
		//其他查询条件
		this.addSqlWhereLike(jfMod.getModName(), sql, args, "\n and a.taskname like ?");
		
		StringBuffer sqlCount = new StringBuffer();
		sqlCount.append("select count(*) from (");
		sqlCount.append(sql);
		sqlCount.append(") where 1=1");
		return this.pagedQuery(sql.toString(), sqlCount.toString(), " handover.endTime ", args.toArray(), JfMod.class);
	}
	/**
	 * 查询自己的任务得分情况
	 * @param jfScore
	 * @param sessionUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JfScore> listPagedMineTaskToJf(JfScore jfScore, UserInfo sessionUser) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from (");
		//外面包一层开始
		sql.append("\n select score.recordCreateTime,handover.endTime modReaseDate, a.id busId,'"+ConstantInterface.TYPE_TASK+"' busType,a.taskName modName, ");
		//采用的积分标准,积分上下线,得分
		sql.append("\n score.jfzbId,c.jfTop,c.jfBottom,score.score,pfUserId,");
		sql.append("\n case when score.id is null then 0  ");//0未评分
		sql.append("\n 	else  ");
		sql.append("\n 		case when score.jfzbId=0 then 1 else 2 end   ");//1不评分  2已评分
		sql.append("\n 	end  scoreState,");
		sql.append("\n 	pfUser.userName pfUserName,pfUser.gender pfUserGender");
		sql.append("\n from task a  ");
		sql.append("\n left join jfMod b on a.id=b.busId and b.busType=?");
		args.add(ConstantInterface.TYPE_TASK);
		sql.append("\n left join (");
		sql.append("\n select distinct max(handover.endTime) endTime,handover.taskid ");
		sql.append("\n from taskhandover handover");
		sql.append("\n where handover.comId=?");
		//团队信息
		args.add(sessionUser.getComId());
		sql.append("\n group by handover.taskid");
		sql.append("\n  )handover on a.id=handover.taskid");
		
		sql.append("\n left join jfScore score on b.id=score.jfModId and score.dfUserId=?");
		args.add(sessionUser.getId());
		sql.append("\n left join userInfo pfUser on pfUser.id=score.pfUserId");
		//积分指标
		sql.append("\n left join jfzb c on score.jfzbId=c.id");
		//任务已经办结
		sql.append("\n where a.state=? and a.comId=?");
		args.add(ConstantInterface.STATIS_TASK_STATE_DONE);
		//团队信息
		args.add(sessionUser.getComId());
		//经办人员没有的过分数据的
		sql.append("\n and exists(");
		sql.append("\n  select handover.fromuser,handover.touser");
		sql.append("\n 		from taskhandover handover");
		sql.append("\n  	where handover.taskid=a.id");
		sql.append("\n 		and (handover.fromuser ="+sessionUser.getId());
		sql.append("\n 			or");
		sql.append("\n  		handover.touser="+sessionUser.getId());
		sql.append("\n  		)");
		sql.append("\n )");
		//外面包一层结束
		sql.append("\n ) a where 1=1 ");
		this.addSqlWhere(jfScore.getScoreState(), sql, args, "\n and a.scoreState=?");
		
		this.addSqlWhere(jfScore.getSearchYear(), sql, args, " and substr(a.modReaseDate,0,4)=?");
		//查询创建时间段
		this.addSqlWhere(jfScore.getStartDate(), sql, args, " and substr(a.modReaseDate,0,10)>=?");
		this.addSqlWhere(jfScore.getEndDate(), sql, args, " and substr(a.modReaseDate,0,10)<=?");
		
		this.addSqlWhereLike(jfScore.getModName(), sql, args, "\n and a.modname like ?");
		String orderBy = " a.scoreState, a.modReaseDate ";
		return this.pagedQuery(sql.toString(),orderBy, args.toArray(), JfScore.class);
	}

	/**
	 * 查询原有的模块积分
	 * @param busId 业务主键 必须是业务的 不能为空
	 * @param busType 业务类型 必须是业务的 不能为空
	 * @return
	 */
	public JfMod queryJfMod(Integer busId, String busType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append(" select a.id from jfMod a");
		sql.append("\n where 1=1 and a.busId=? and busType=?");
		args.add(busId);
		args.add(busType);
		return (JfMod) this.objectQuery(sql.toString(), args.toArray(), JfMod.class);
	}
	/**
	 * 查询任务的所有办理人员的需要评分的
	 * @param jfMod 积分模块信息
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JfScore> listTaskJfScore(JfMod jfMod, UserInfo sessionUser) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//需要得分的人员信息
		sql.append("\n select a.userid dfUserId,u.username dfUserName,u.gender dfUserGender,b.remark,");
		//采用的积分标准,积分上下线,得分
		sql.append("\n b.jfzbId,c.jfTop,c.jfBottom,b.score,c.leveTwo,jfzbType.typeName jfzbTypeName,");
		sql.append("\n case when nvl(userscope.subUserId,0)>0 then 1 else  0 end dirLeaderState");
		
		sql.append("\n from (");
		//任务推送人
		sql.append("\n 	select a.fromuser userid from taskhandover a where a.taskid=?");
		args.add(jfMod.getBusId());
		sql.append("\n  	union ");
		//任务接收人 
		sql.append("\n 	select a.touser userid  from taskhandover a where a.taskid=?");
		args.add(jfMod.getBusId());
		//任务办理人信息
		sql.append("\n )a inner join userinfo u on a.userid=u.id");
		//积分模块
		sql.append("\n left join jfMod on jfMod.busId=? and jfMod.bustype=?");
		args.add(jfMod.getBusId());
		args.add(jfMod.getBusType());
		//积分信息
		sql.append("\n left join jfScore b on a.userid=b.dfUserId and jfMod.id=b.jfmodid");
		//积分指标
		sql.append("\n left join jfzb c on b.jfzbId=c.id");
		sql.append("\n left join jfzbType on jfzbType.id=c.jfzbTypeId");
		
		//需要参与评分的的人员
		sql.append("\n 		left join (");
		//添加直属下属
		sql.append("\n			select leader leadderId,creator subUserId from myLeaders where leader = ? and comId = ? and creator <> leader ");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 			union");
		//添加自己设定的评分人员
		sql.append("\n 			select scopeUserB.Leaderid,scopeUserB.Subuserid from jfSubUserScope scopeUserB");
		sql.append("\n 			where scopeUserB.LeaderId=? and scopeUserB.comId=? and scopeUserB.needScore='1'");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 			minus");
		//减去自己设定的评分人员中和直属下属重复的
		sql.append("\n			select leader.leader leadderId,leader.creator subUserId from myLeaders leader ");
		sql.append("\n 			inner join jfSubUserScope scopeUserA on leader.leader=scopeUserA.Leaderid ");
		sql.append("\n 			and scopeUserA.Subuserid=leader.creator and leader.comId=scopeUserA.comId");
		sql.append("\n 			where leader.Leader=? and leader.comId=? and leader.creator <> leader.leader and scopeUserA.needScore='1' ");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 			minus");
		//减去直属下属中被别人设定的评分的人员
		sql.append("\n			select leader leadderId,creator subUserId from myLeaders where leader = ? and comId = ? and creator <> leader ");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 			and exists(");
		sql.append("\n 				select scopeUserB.Subuserid from jfSubUserScope scopeUserB");
		sql.append("\n 				where scopeUserB.LeaderId<>? and scopeUserB.comId=? and scopeUserB.needScore='1'");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 			 	and myLeaders.creator=scopeUserB.Subuserid");
		sql.append("\n 			)");
		sql.append("\n 			minus");
		//减去自己不需要设定过得不需要参与评分的
		sql.append("\n 			select scopeUserB.Leaderid,scopeUserB.Subuserid from jfSubUserScope scopeUserB");
		sql.append("\n 			where scopeUserB.LeaderId=? and scopeUserB.comId=? and scopeUserB.needScore='0'");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n 		) userscope on a.userId=userscope.subUserId");
		sql.append("\n where 1=1 ");
		sql.append("\n order by a.userid nulls last,  u.id");
		return this.listQuery(sql.toString(), args.toArray(), JfScore.class);
	}
	/**
	 * 查询自己设置的下属评分人员
	 * @param sessionUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JfSubUserScope> listJfSubScope(UserInfo sessionUser) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,b.userName as subUserName ");
		sql.append("\n from jfSubUserScope a");
		sql.append("\n inner join userInfo b on a.subUserId=b.id");
		sql.append("\n where 1=1 and a.leaderId=? and a.comId=? ");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		sql.append("\n order by a.id ");
		return this.listQuery(sql.toString(), args.toArray(), JfSubUserScope.class)	;
	}
	/**
	 * 积分统计
	 * @param jfScore
	 * @param userInfo
	 * @return
	 */
	public List<TaskJfStatis> listTaskJfStatis(JfScore jfScore,UserInfo userInfo,boolean isForceIn){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n SELECT a.*,nvl(b.taskTotal,0) taskTotal,nvl(c.jfTaskNum,0) jfTaskNum,nvl(d.scoreTotal,0) scoreTotal FROM ( ");

		sql.append("\n (select a.depId,a.depName,a.userId,a.userName  ");
		//有下属的部门 分页
		sql.append("\n from ( select a.id depId,a.depName ,b.userId,c.userName from( ");
		sql.append("select outer.* from ( \n ");
		sql.append("select  inner.*,rownum as rowno  from ( \n");
		sql.append("\n select a.* from ( ");
		sql.append("\n select a.id,a.depName,count(b.id) sumUserId from department a ");
		sql.append("\n left join (select a.* from userOrganic a ");
		sql.append("\n where a.enabled = '1' and a.comId = ? ");
		args.add(userInfo.getComId());
		if(!isForceIn){
			sql.append("\n and exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=a.userId and sup.leader=?) ");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
		}
		sql.append("\n )b on a.comId = b.comId and a.id = b.depId group by a.id,a.depName ");
		sql.append(" )a where 1=1 and a.sumUserId is not null and a.sumUserId <> 0 order by a.id  ");
		sql.append(") inner where rownum <=").append(PaginationContext.getOffset() + PaginationContext.getPageSize());
		sql.append("\n ) outer where outer.rowno>").append(PaginationContext.getOffset());
		sql.append("\n )a ");
		sql.append("\n left join (select a.* from userOrganic a ");
		sql.append("\n where a.enabled = '1' and a.comId = ? ");
		args.add(userInfo.getComId());
		if(!isForceIn){
			sql.append("\n and exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=a.userId and sup.leader=?) ");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
		}
		sql.append("\n )b on b.comId = ? and a.id = b.depId ");
		args.add(userInfo.getComId());
		sql.append("\n LEFT JOIN USERINFO c on c.id = b.userId where 1=1");
		//不统计当前人
		//this.addSqlWhere(userInfo.getId(), sql, args, " and b.userId <> ? ");
		sql.append("\n )a where 1=1 )a");

		//总任务数
		sql.append("\n LEFT JOIN (");
		sql.append("\n SELECT count(*) taskTotal,a.USERID FROM (");
		sql.append("\n SELECT distinct b.busId,a.USERID from USERORGANIC a");
		sql.append("\n INNER JOIN (");
		sql.append("\n select  a.id busId,b.FROMUSER,b.TOUSER  from task a");
		sql.append("\n LEFT JOIN (");
		sql.append("\n select distinct max(handover.endTime) endTime,handover.taskid from taskhandover handover");
		sql.append("\n where handover.comId= ?  group by handover.taskid)handover on a.id=handover.taskid");
		args.add(userInfo.getComId());
		sql.append("\n LEFT JOIN taskhandover b ON a.ID = b.TASKID");
		sql.append("\n where a.state= ? and a.comId=?");
		args.add(ConstantInterface.STATIS_TASK_STATE_DONE);
		args.add(userInfo.getComId());
		this.addSqlWhere(jfScore.getSearchYear(), sql, args, " and substr(handover.endTime,0,4)=?");
		this.addSqlWhere(jfScore.getStartDate(), sql, args, " and substr(handover.endTime,0,10)>=?");
		this.addSqlWhere(jfScore.getEndDate(), sql, args, " and substr(handover.endTime,0,10)<=?");
		sql.append("\n )b ON  a.USERID = b.FROMUSER OR a.USERID = b.TOUSER");
		sql.append("\n WHERE 1=1 and a.COMID = ?");
		args.add(userInfo.getComId());
		sql.append("\n )a GROUP BY  a.USERID");
		sql.append("\n )b ON a.USERID = b.userId");
		//已评任务数
		sql.append("\n LEFT JOIN (");
		sql.append("\n select count(a.busId) jfTaskNum,a.USERID from (");
		sql.append("\n select a.id busId,score.dfUserId userId from task a");
		sql.append("\n inner join jfMod b on a.id=b.busId and b.busType=? AND a.COMID = b.COMID");
		args.add(ConstantInterface.TYPE_TASK);
		sql.append("\n inner join jfScore score on b.id=score.jfModId");
		sql.append("\n inner JOIN (");
		sql.append("\n select distinct max(handover.endTime) endTime,handover.taskid from taskhandover handover");
		sql.append("\n where  handover.comId= ? group by handover.taskid )handover on a.id=handover.taskid ");
		sql.append("\n where  a.comId=? ");
		args.add(userInfo.getComId());
		args.add(userInfo.getComId());
		this.addSqlWhere(jfScore.getSearchYear(), sql, args, " and substr(handover.endTime,0,4)=?");
		this.addSqlWhere(jfScore.getStartDate(), sql, args, " and substr(handover.endTime,0,10)>=?");
		this.addSqlWhere(jfScore.getEndDate(), sql, args, " and substr(handover.endTime,0,10)<=?");
		sql.append("\n )a GROUP BY a.userId ");
		sql.append("\n )c ON a.USERID = c.userId ");
		//总得分
		sql.append("\n LEFT JOIN (");
		sql.append("\n select nvl(sum(a.score),0) scoreTotal,a.USERID from (");
		sql.append("\n select to_number(score.score) score,score.dfUserId userId from task a");
		sql.append("\n inner join jfMod b on a.id=b.busId and b.busType=? AND a.COMID = b.COMID");
		args.add(ConstantInterface.TYPE_TASK);
		sql.append("\n inner join jfScore score on b.id=score.jfModId");
		sql.append("\n inner JOIN (");
		sql.append("\n select distinct max(handover.endTime) endTime,handover.taskid from taskhandover handover");
		sql.append("\n where  handover.comId= ? group by handover.taskid )handover on a.id=handover.taskid ");
		sql.append("\n where  a.comId=? ");
		args.add(userInfo.getComId());
		args.add(userInfo.getComId());
		this.addSqlWhere(jfScore.getSearchYear(), sql, args, " and substr(handover.endTime,0,4)=?");
		this.addSqlWhere(jfScore.getStartDate(), sql, args, " and substr(handover.endTime,0,10)>=?");
		this.addSqlWhere(jfScore.getEndDate(), sql, args, " and substr(handover.endTime,0,10)<=?");
		sql.append("\n )a GROUP BY a.userId ");
		sql.append("\n )d ON a.USERID = d.userId");
		sql.append("\n )order by a.depId,a.userId");
		return this.listQuery(sql.toString(), args.toArray(), TaskJfStatis.class);
	}
	/**
	 * 获取统计其他信息
	 * @param userId
	 * @param jfScore
	 * @return
	 */
	public TaskJfStatis getTaskJfStatisT(Integer userId, JfScore jfScore,UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select  ");
		//总任务数
		sql.append("\n (select count(a.busId) from (");
		sql.append("\n select handover.endTime modReaseDate, a.id busId  from task a");
		sql.append("\n left join (");
		sql.append("\n select distinct max(handover.endTime) endTime,handover.taskid ");
		sql.append("\n from taskhandover handover");
		sql.append("\n where handover.comId=?");
		args.add(userInfo.getComId());
		sql.append("\n group by handover.taskid");
		sql.append("\n  )handover on a.id=handover.taskid");
		sql.append("\n where a.state=? and a.comId=?");
		args.add(ConstantInterface.STATIS_TASK_STATE_DONE);
		args.add(userInfo.getComId());
		sql.append("\n and exists(");
		sql.append("\n  select handover.fromuser,handover.touser");
		sql.append("\n 		from taskhandover handover");
		sql.append("\n  	where handover.taskid=a.id");
		sql.append("\n 		and (handover.fromuser = ? or handover.touser= ? )");
		args.add(userId);
		args.add(userId);
		sql.append("\n )");
		sql.append("\n ) a where 1=1 ");
		this.addSqlWhere(jfScore.getSearchYear(), sql, args, " and substr(a.modReaseDate,0,4)=?");
		this.addSqlWhere(jfScore.getStartDate(), sql, args, " and substr(a.modReaseDate,0,10)>=?");
		this.addSqlWhere(jfScore.getEndDate(), sql, args, " and substr(a.modReaseDate,0,10)<=?");
		sql.append("\n ) taskTotal ");
		//已评分任务数
		sql.append("\n ,(select count(a.busId) from (");
		sql.append("\n select handover.endTime modReaseDate, a.id busId from task a ");
		sql.append("\n inner join jfMod b on a.id=b.busId and b.busType=?");
		args.add(ConstantInterface.TYPE_TASK);
		sql.append("\n   inner join jfScore score on b.id=score.jfModId and score.dfUserId= ?");
		args.add(userId);
		sql.append("\n left join (");
		sql.append("\n select distinct max(handover.endTime) endTime,handover.taskid ");
		sql.append("\n from taskhandover handover");
		sql.append("\n where handover.comId=?");
		args.add(userInfo.getComId());
		sql.append("\n group by handover.taskid");
		sql.append("\n  )handover on a.id=handover.taskid");
		sql.append("\n and exists(");
		sql.append("\n  select handover.fromuser,handover.touser");
		sql.append("\n 		from taskhandover handover");
		sql.append("\n  	where handover.taskid=a.id");
		sql.append("\n 		and (handover.fromuser = ? or handover.touser= ? )");
		args.add(userId);
		args.add(userId);
		sql.append("\n )");
		sql.append("\n ) a where 1=1 ");
		this.addSqlWhere(jfScore.getSearchYear(), sql, args, " and substr(a.modReaseDate,0,4)=?");
		this.addSqlWhere(jfScore.getStartDate(), sql, args, " and substr(a.modReaseDate,0,10)>=?");
		this.addSqlWhere(jfScore.getEndDate(), sql, args, " and substr(a.modReaseDate,0,10)<=?");
		sql.append("\n ) jfTaskNum ");
		//评分总数
		sql.append("\n ,(select nvl(sum(a.score),0) from (");
		sql.append("\n select handover.endTime modReaseDate, a.id busId,to_number(score.score) score from task a ");
		sql.append("\n inner join jfMod b on a.id=b.busId and b.busType=?");
		args.add(ConstantInterface.TYPE_TASK);
		sql.append("\n   inner join jfScore score on b.id=score.jfModId and score.dfUserId= ?");
		args.add(userId);
		sql.append("\n left join (");
		sql.append("\n select distinct max(handover.endTime) endTime,handover.taskid ");
		sql.append("\n from taskhandover handover");
		sql.append("\n where handover.comId=?");
		args.add(userInfo.getComId());
		sql.append("\n group by handover.taskid");
		sql.append("\n  )handover on a.id=handover.taskid");
		sql.append("\n and exists(");
		sql.append("\n  select handover.fromuser,handover.touser");
		sql.append("\n 		from taskhandover handover");
		sql.append("\n  	where handover.taskid=a.id");
		sql.append("\n 		and (handover.fromuser = ? or handover.touser= ? )");
		args.add(userId);
		args.add(userId);
		sql.append("\n )");
		sql.append("\n ) a where 1=1 ");
		this.addSqlWhere(jfScore.getSearchYear(), sql, args, " and substr(a.modReaseDate,0,4)=?");
		this.addSqlWhere(jfScore.getStartDate(), sql, args, " and substr(a.modReaseDate,0,10)>=?");
		this.addSqlWhere(jfScore.getEndDate(), sql, args, " and substr(a.modReaseDate,0,10)<=?");
		sql.append("\n ) scoreTotal ");
		sql.append("\n from dual ");
		return (TaskJfStatis) this.objectQuery(sql.toString(), args.toArray(),TaskJfStatis.class);
	}
	/**
	 * 积分统计
	 * @param jfScore
	 * @param userInfo
	 * @return
	 */
	public Integer countTaskJfStatis(JfScore jfScore,UserInfo userInfo,boolean isForceIn){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//有下属的部门 分页
		sql.append("\n select count(a.id) from ( ");
		sql.append("\n select a.id,a.depName,count(b.id) sumUserId from department a ");
		sql.append("\n left join (select a.* from userOrganic a ");
		sql.append("\n where a.enabled = '1' and a.comId = ? ");
		args.add(userInfo.getComId());
		if(!isForceIn){
			sql.append("\n and exists( ");
			sql.append("\n  	select sup.leader from myLeaders sup where sup.comId=? and sup.creator=a.userId and sup.leader=?");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append("\n ) ");
		}
		sql.append("\n )b on a.comId = b.comId and a.id = b.depId group by a.id,a.depName ");
		sql.append(" )a where 1=1 and a.sumUserId is not null and a.sumUserId <> 0  ");
		return this.countQuery(sql.toString(), args.toArray());
	}
}
