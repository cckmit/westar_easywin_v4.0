package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.westar.base.model.CustomerSharer;
import com.westar.base.model.MeetCheckUser;
import com.westar.base.model.MeetDep;
import com.westar.base.model.MeetLearn;
import com.westar.base.model.MeetLog;
import com.westar.base.model.MeetPerson;
import com.westar.base.model.MeetRegular;
import com.westar.base.model.MeetTalk;
import com.westar.base.model.MeetTalkFile;
import com.westar.base.model.Meeting;
import com.westar.base.model.NoticePerson;
import com.westar.base.model.SummaryFile;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;

@Repository
public class MeetingDao extends BaseDao {

	/**
	 * 分页查询会议信息
	 * @param meeting
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meeting> listPagedMeeting(Meeting meeting, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		sql.append("\n select a.* from (");
		sql.append("\n select a.*,");
		//会议已结束
		sql.append("\n case when a.endDate<='"+nowDateTime+"' then 0 else  ");
		//会议已开始1 未开始2
		sql.append("\n case when a.startDate<='"+nowDateTime+"' then 1 else 2 end  ");
		sql.append("\n end timeOut,");
		sql.append("\n case when b.state is null then '-1' else b.state end applyState");
		sql.append("\n from meeting a left join roomapply b on a.meetingaddrid=b.roomid and a.id=b.meetingid");
		sql.append("\n where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.organiser=?");
		if(null != meeting.getListCreator() && !meeting.getListCreator().isEmpty()){
			sql.append("and ( a.organiser = 0 \n");
			for(UserInfo owner : meeting.getListCreator()){
				sql.append("	or a.organiser =  ? ");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		
		this.addSqlWhere(meeting.getMeetingState(), sql, args, " and a.meetingState=?");
		sql.append("\n )a where 1=1 ");
		
		//查询创建时间段
		this.addSqlWhere(meeting.getStartDate(), sql, args, " and substr(a.startDate,0,10)>=?");
		this.addSqlWhere(meeting.getEndDate(), sql, args, " and substr(a.endDate,0,10)<=?");
		
		this.addSqlWhereLike(meeting.getTitle(), sql, args, " and a.title like ? \n");
		
		return this.pagedQuery(sql.toString(), "a.timeOut desc,a.startDate desc", args.toArray(), Meeting.class);
	}
	
	/**
	 * 已发布的会议
	 * @param meeting
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meeting> listPagedTodoMeeting(Meeting meeting, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		sql.append("\n select a.*,case when b.state is null then 0 else b.state end checkState from (");
		sql.append("\n select a.*,b.userName organiserName,");
		//会议已结束
		sql.append("\n case when a.endDate<='"+nowDateTime+"' then 0 else  ");
		//会议开始1 未开始2
		sql.append("\n case when a.startDate<='"+nowDateTime+"' then 1 else 2 end  ");
		sql.append("\n end timeOut,");
		sql.append("\n case when c.state is null then '-1' else c.state end applyState");
		sql.append("\n from meeting a inner join userinfo b on a.organiser=b.id");
		sql.append("\n left join roomapply c on a.meetingaddrid=c.roomid and a.id=c.meetingid");
		sql.append("\n where a.meetingState=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		
		sql.append("\n and (a.spState=0 or a.spState=4)");
		
		//在与会人员中
		sql.append("\n and ( exists( ");
		sql.append("\n	 select person.id from meetPerson person where person.comId=a.comId and person.meetingId=a.id");
		this.addSqlWhere(userInfo.getId(), sql, args, " and person.userId=?");
		sql.append("\n	 )");
		
		sql.append("\n or");
		//在与会部门中
		sql.append("\n  exists(");
		sql.append("\n 	 select meetDep.id from meetDep inner join userorganic org1 on meetDep.comId=org1.comId and meetdep.depId=org1.depId");
		sql.append("\n 	where meetDep.comId=a.comId and meetDep.meetingId=a.id");
		this.addSqlWhere(userInfo.getId(), sql, args, " and org1.userId=?");
		sql.append("\n 	)");
		
		//发起人
		this.addSqlWhere(userInfo.getId(), sql, args, " or a.organiser=?");
		//主持人
		this.addSqlWhere(userInfo.getId(), sql, args, " or a.presenter=?");
		//记录人
		this.addSqlWhere(userInfo.getId(), sql, args, " or a.recorder=?");
		
		sql.append("\n )");
		
		sql.append("\n )a left join meetCheckUser b on a.comId=b.comId and a.id=b.meetingId ");
		this.addSqlWhere(userInfo.getId(), sql, args, " and b.userId=?");
		sql.append("\n where 1=1  and (a.timeOut=2 or a.timeOut=1)");
		//发起人
		this.addSqlWhere(meeting.getOrganiser(), sql, args, " and a.organiser=?");
		
		//发起人
		List<UserInfo> listCreator = meeting.getListCreator();
		if(null!=listCreator && !listCreator.isEmpty() ){
			List<Integer> listCreatorId = new ArrayList<>(listCreator.size());
			for (UserInfo creator : listCreator) {
				listCreatorId.add(creator.getId());
			}
			this.addSqlWhereIn(listCreatorId.toArray(new Integer[listCreatorId.size()]), sql, args, "\n and a.organiser in ? ");
		}
		
		
		//查询创建时间段
		this.addSqlWhere(meeting.getStartDate(), sql, args, " and substr(a.startDate,0,10)>=?");
		this.addSqlWhere(meeting.getEndDate(), sql, args, " and substr(a.endDate,0,10)<=?");
		
		this.addSqlWhereLike(meeting.getTitle(), sql, args, " and a.title like ? \n");
		return this.pagedQuery(sql.toString(), "a.id", args.toArray(), Meeting.class);
	}
	/**
	 * 已发布的会议
	 * @param meeting
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meeting> listPagedSpFlowMeeting(Meeting meeting, UserInfo userInfo,Boolean isForceInPersion) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from (");
		sql.append("\n select a.*,b.userName organiserName,curExe.executor spExecutorId,curExeUser.username as spExecutorName");
		sql.append("\n from meeting a inner join userinfo b on a.organiser=b.id");
		
		sql.append("\n left join spFlowCurExecutor curExe on a.comId = curExe.comid and a.id = curExe.busId and curExe.busType=?");
		args.add(ConstantInterface.TYPE_MEETING_SP);
		sql.append("\n left join userinfo curExeUser on curExe.executor=curExeUser.id");
		sql.append("\n where a.meetingState=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		
		//审批状态
		Integer spState = meeting.getSpState();
		if(null== spState){
			sql.append("\n and a.spState<>0 ");
		}else{
			this.addSqlWhere(spState, sql, args, "\n and a.spState = ?");
		}
		
		//发起人
		this.addSqlWhere(meeting.getOrganiser(), sql, args, " and a.organiser=?");
		//发起人
		List<UserInfo> listCreator = meeting.getListCreator();
		if(null!=listCreator && !listCreator.isEmpty() ){
			List<Integer> listCreatorId = new ArrayList<>(listCreator.size());
			for (UserInfo creator : listCreator) {
				listCreatorId.add(creator.getId());
			}
			this.addSqlWhereIn(listCreatorId.toArray(new Integer[listCreatorId.size()]), sql, args, "\n and a.organiser in ? ");
		}
		
		// 查看权限范围界定
		if (!isForceInPersion) {
			sql.append("\n and (");
			// 发起人、当前审批人、历史审批人
			sql.append("\n a.organiser=? or curExe.executor =? ");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			// 上级权限验证
			// 历史审批人上级权限验证
			sql.append("\n  or exists( ");
			sql.append("\n   	select b.* from spFlowHiExecutor b where a.id = b.busId and b.busType=? and");
			args.add(ConstantInterface.TYPE_MEETING_SP);
			sql.append("\n   	(exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.executor and sup.leader=?)\n");
			sql.append("\n  )) \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			// 当前审批人上级权限验证
			sql.append("\n or exists(");
			sql.append("\n select id from myLeaders where creator = curExe.executor and leader = ? and comId = ? and creator <> leader");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append("\n )\n");
			// 发起人上级权限验证
			sql.append("\n or exists(\n");
			sql.append("\n select id from myLeaders where creator = a.organiser and leader = ? and comId = ? and creator <> leader");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append("\n 	)");
			sql.append("\n 	) ");
			// 历史审批人以及上级权限
			sql.append("\n  or exists( ");
			sql.append("\n   	select b.* from spFlowHiExecutor b where a.id = b.busId and b.busType=? ");
			args.add(ConstantInterface.TYPE_MEETING_SP);
			sql.append("\n   	and (b.executor =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.executor and sup.leader=?))\n");
			sql.append("\n 	) ");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			// 共享人以及上级权限
			sql.append("\n  or exists( ");
			sql.append("\n   	select b.* from spflownoticeuser b where a.id = b.busId and b.busType=? ");
			args.add(ConstantInterface.TYPE_MEETING_SP);
			sql.append("\n   	and (b.noticeUserId =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.noticeUserId and sup.leader=?))\n");
			sql.append("\n 	) ");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
		}
				
		//查询创建时间段
		this.addSqlWhere(meeting.getStartDate(), sql, args, " and substr(a.startDate,0,10)>=?");
		this.addSqlWhere(meeting.getEndDate(), sql, args, " and substr(a.endDate,0,10)<=?");
		
		sql.append("\n )a  ");
		sql.append("\n where 1=1 ");
		
		this.addSqlWhereLike(meeting.getTitle(), sql, args, " and a.title like ? \n");
		return this.pagedQuery(sql.toString(), "a.id desc", args.toArray(), Meeting.class);
	}
	/**
	 * 已发布的会议
	 * @param meeting
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meeting> listPagedDoneMeeting(Meeting meeting, UserInfo userInfo,boolean isForceInPersion) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		sql.append("\n select a.* from (");
		sql.append("\n select a.*,b.userName organiserName,");
		//会议已结束
		sql.append("\n case when a.endDate<='"+nowDateTime+"' then 0 else  ");
		//会议开始1 未开始2
		sql.append("\n case when a.startDate<='"+nowDateTime+"' then 1 else 2 end  ");
		sql.append("\n end timeOut,");
		sql.append("\n case when summary.spState is null then 0 else summary.spState end summarySpState,");
		//为空和为0非审批，否则是
		sql.append("\n case when summary.spState is null or summary.spState=0 then 0 else 1 end summaryNeedSpState,");
		sql.append("\n case when summary.summary is null then");
		sql.append("\n (select count(*) from summaryFile files where files.comId=a.comId and a.id=files.meetingId)");
		sql.append("\n else 1 end summaryState");
		sql.append("\n from meeting a inner join userinfo b on a.organiser=b.id");
		sql.append("\n left join meetSummary summary on a.id=summary.meetingId");
		sql.append("\n where a.meetingState=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		
		if(!isForceInPersion){
			
			//在与会人员中
			sql.append("\n and ( exists( ");
			sql.append("\n	 select person.id from meetPerson person where person.comId=a.comId and person.meetingId=a.id");
			this.addSqlWhere(userInfo.getId(), sql, args, " and person.userId=?");
			sql.append("\n	 )");
			
			sql.append("\n or");
			//在与会部门中
			sql.append("\n  exists(");
			sql.append("\n 	 select meetDep.id from meetDep inner join userorganic org1 on meetDep.comId=org1.comId and meetdep.depId=org1.depId");
			sql.append("\n 	where meetDep.comId=a.comId and meetDep.meetingId=a.id");
			this.addSqlWhere(userInfo.getId(), sql, args, " and org1.userId=?");
			sql.append("\n 	)");
			//发起人
			this.addSqlWhere(userInfo.getId(), sql, args, " or a.organiser=?");
			
			//主持人
			this.addSqlWhere(userInfo.getId(), sql, args, " or a.presenter=?");
			//记录人
			this.addSqlWhere(userInfo.getId(), sql, args, " or a.recorder=?");
			
			sql.append("\n or");
			
			sql.append("\n  exists(");
			sql.append("\n 	 select ml.id from meetLearn ml where ml.comId=a.comId and ml.meetingId = a.id");
			this.addSqlWhere(userInfo.getId(), sql, args, " and ml.userId=?");
			sql.append("\n 	)");
			
			sql.append("\n or");
			//在告知人员中
			sql.append("\n  exists( ");
			sql.append("\n	 select person.id from noticePerson person where person.comId=a.comId and person.meetingId=a.id");
			this.addSqlWhere(userInfo.getId(), sql, args, " and person.userId=?");
			sql.append("\n	 )");
			sql.append("\n )");
		}
		
		sql.append("\n )a where 1=1 ");
		sql.append("\n and a.timeOut=0 ");
		//发起人
		this.addSqlWhere(meeting.getOrganiser(), sql, args, " and a.organiser=?");
		//发起人
		List<UserInfo> listCreator = meeting.getListCreator();
		if(null!=listCreator && !listCreator.isEmpty() ){
			List<Integer> listCreatorId = new ArrayList<>(listCreator.size());
			for (UserInfo creator : listCreator) {
				listCreatorId.add(creator.getId());
			}
			this.addSqlWhereIn(listCreatorId.toArray(new Integer[listCreatorId.size()]), sql, args, "\n and a.organiser in ? ");
		}
		//查询创建时间段
		this.addSqlWhere(meeting.getStartDate(), sql, args, " and substr(a.startDate,0,10)>=?");
		this.addSqlWhere(meeting.getEndDate(), sql, args, " and substr(a.endDate,0,10)<=?");
		
		this.addSqlWhereLike(meeting.getTitle(), sql, args, " and a.title like ? \n");
		
		return this.pagedQuery(sql.toString(), "a.startDate desc,a.id", args.toArray(), Meeting.class);
	}
	
	/**
	 * 已发布的会议
	 * @param meeting
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meeting> listPagedMeetWithSpSummary(Meeting meeting, 
			UserInfo userInfo,boolean isForceInPersion) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		sql.append("\n select a.* from (");
		sql.append("\n select a.*,b.userName organiserName,");
		//会议已结束
		sql.append("\n case when a.endDate<='"+nowDateTime+"' then 0 else  ");
		//会议开始1 未开始2
		sql.append("\n case when a.startDate<='"+nowDateTime+"' then 1 else 2 end  ");
		sql.append("\n end timeOut,");
		sql.append("\n case when summary.spState is null then 0 else summary.spState end summarySpState,");
		//为空和为0非审批，否则是
		sql.append("\n case when summary.spState is null or summary.spState=0 then 0 else 1 end summaryNeedSpState,");
		sql.append("\n curExe.executor spExecutorId,curExeUser.username as spExecutorName,");
		sql.append("\n case when summary.summary is null then");
		sql.append("\n (select count(*) from summaryFile files where files.comid=a.comid and a.id=files.meetingId)");
		sql.append("\n else 1 end summaryState");
		sql.append("\n from meeting a inner join userinfo b on a.organiser=b.id");
		sql.append("\n inner join meetSummary summary on a.id=summary.meetingId");
		
		sql.append("\n left join spFlowCurExecutor curExe on summary.comId = curExe.comid and summary.id = curExe.busId and curExe.busType=?");
		args.add(ConstantInterface.TYPE_MEET_SUMMARY);
		sql.append("\n left join userinfo curExeUser on curExe.executor=curExeUser.id");
		
		sql.append("\n where a.meetingState=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		// 查看权限范围界定
		if (!isForceInPersion) {
			sql.append("\n and (");
			// 发起人、当前审批人、历史审批人
			sql.append("\n a.recorder=? or curExe.executor =? ");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			// 上级权限验证
			// 历史审批人上级权限验证
			sql.append("\n  or exists( ");
			sql.append("\n   	select b.* from spFlowHiExecutor b where summary.id = b.busId and b.busType=? and");
			args.add(ConstantInterface.TYPE_MEET_SUMMARY);
			sql.append("\n   	(exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.executor and sup.leader=?)\n");
			sql.append("\n  )) \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			// 当前审批人上级权限验证
			sql.append("\n or exists(");
			sql.append("\n select id from myLeaders where creator = curExe.executor and leader = ? and comId = ? and creator <> leader");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append("\n )\n");
			// 发起人上级权限验证
			sql.append("\n or exists(\n");
			sql.append("\n select id from myLeaders where creator = a.recorder and leader = ? and comId = ? and creator <> leader");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append("\n 	)");
			sql.append("\n 	) ");
			// 历史审批人以及上级权限
			sql.append("\n  or exists( ");
			sql.append("\n   	select b.* from spFlowHiExecutor b where summary.id = b.busId and b.busType=? ");
			args.add(ConstantInterface.TYPE_MEETING);
			sql.append("\n   	and (b.executor =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.executor and sup.leader=?))\n");
			sql.append("\n 	) ");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			// 共享人以及上级权限
			sql.append("\n  or exists( ");
			sql.append("\n   	select b.* from spflownoticeuser b where summary.id = b.busId and b.busType=? ");
			args.add(ConstantInterface.TYPE_MEET_SUMMARY);
			sql.append("\n   	and (b.noticeUserId =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.noticeUserId and sup.leader=?))\n");
			sql.append("\n 	) ");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
		}
		
		sql.append("\n )a where 1=1 ");
		sql.append("\n and a.timeOut=0 ");
		
		//审批状态
		Integer spState = meeting.getSummarySpState();
		if(null== spState){
			sql.append("\n and a.summarySpState<>0 ");
		}else{
			this.addSqlWhere(spState, sql, args, "\n and a.summarySpState = ?");
		}
				
		
		this.addSqlWhere(meeting.getSummaryNeedSpState(), sql, args, "\n and a.summaryNeedSpState=?");
		//发起人
		this.addSqlWhere(meeting.getOrganiser(), sql, args, " and a.organiser=?");
		//发起人
		List<UserInfo> listCreator = meeting.getListCreator();
		if(null!=listCreator && !listCreator.isEmpty() ){
			List<Integer> listCreatorId = new ArrayList<>(listCreator.size());
			for (UserInfo creator : listCreator) {
				listCreatorId.add(creator.getId());
			}
			this.addSqlWhereIn(listCreatorId.toArray(new Integer[listCreatorId.size()]), sql, args, "\n and a.organiser in ? ");
		}
		//查询创建时间段
		this.addSqlWhere(meeting.getStartDate(), sql, args, " and substr(a.startDate,0,10)>=?");
		this.addSqlWhere(meeting.getEndDate(), sql, args, " and substr(a.endDate,0,10)<=?");
		
		this.addSqlWhereLike(meeting.getTitle(), sql, args, " and a.title like ? \n");
		return this.pagedQuery(sql.toString(), "a.startDate desc,a.id", args.toArray(), Meeting.class);
	}

	/**
	 * 通过会议主键取得会议信息
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 */
	public Meeting getMeetingById(Integer meetingId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,");
		sql.append("\n user1.username presenterName,");
		sql.append("\n user2.username recorderName");
		sql.append("\n from meeting a ");
		//主持人
		sql.append("\n left join userinfo user1 on a.presenter=user1.id");
		//记录员
		sql.append("\n left join userinfo user2 on a.recorder=user2.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(meetingId, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return (Meeting) this.objectQuery(sql.toString(), args.toArray(), Meeting.class);
	}

	/**
	 * 与会人员
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MeetPerson> listMeetPerson(Integer meetingId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,");
		sql.append("\n user1.username personName");
		sql.append("\n from meetperson a");
		sql.append("\n left join userinfo user1 on a.userId=user1.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), MeetPerson.class);
	}
	/**
	 * 与会部门
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MeetDep> listMeetDep(Integer meetingId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,dep.depName");
		sql.append("\n from meetDep a left join department dep on a.comId=dep.comId and a.depid=dep.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), MeetDep.class);
	}

	/**
	 * 告知人员
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<NoticePerson> listNoticePerson(Integer meetingId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,");
		sql.append("\n user1.username,user1.gender userGender,file1.uuid userImgUuid,file1.filename userImgName");
		sql.append("\n from noticePerson a");
		sql.append("\n left join userorganic org on a.comId=org.comId and a.userid=org.userId");
		sql.append("\n left join userinfo user1 on org.userid=user1.id");
		sql.append("\n left join upfiles file1 on org.comId=file1.comId and org.mediumheadportrait=file1.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), NoticePerson.class);
	}

	/**
	 * 选出需要撤销的会议
	 * @param comId 企业号
	 * @param ids 会议主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meeting> listMeetingForRevoke(Integer comId, Integer[] ids) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.comId,a.roomType,a.meetingAddrId from meeting a");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhereIn(ids, sql, args, " and a.id in ?");
		sql.append("\n order by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), Meeting.class);
	}

	/**
	 * 取得会议信息
	 * @param meetingId 会议主键
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public Meeting getMeetForSummary(Integer meetingId, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recorder,a.title,a.startDate,a.endDate,a.meetingAddrName,a.content,a.summary,b.userName");
		sql.append("\n from meeting a inner join userinfo b on a.organiser=b.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(meetingId, sql, args, " and a.id=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		return (Meeting) this.objectQuery(sql.toString(), args.toArray(), Meeting.class);
	}
	/**
	 * 分页查询会议留言
	 * @param comId 企业号
	 * @param meetingId 会议主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MeetTalk> listPagedMeetTalk(Integer comId, Integer meetingId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comId,a.content,a.talker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName");
		sql.append("\n  from meetTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join meetTalk e on a.parentid=e.id and a.comId=e.comId");
		sql.append("\n  left join userinfo d on e.talker=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.pagedQuery(sql.toString(),null ,args.toArray(), MeetTalk.class);
	}
	
	/**
	 * 查询会议留言总数
	 * @param comId
	 * @param meetingId
	 * @return
	 */
	public Integer countTalk(Integer comId, Integer meetingId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(1) from (");
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comId,a.content,a.talker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName");
		sql.append("\n  from meetTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join meetTalk e on a.parentid=e.id and a.comId=e.comId");
		sql.append("\n  left join userinfo d on e.talker=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		sql.append("\n )");
		return this.countQuery(sql.toString(),args.toArray());
	}
	
	/**
	 * 分页查询会议留言
	 * @param comId 企业号
	 * @param meetingId 会议主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MeetTalk> listMeetTalk(Integer comId, Integer meetingId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.recordCreateTime,a.id,a.parentid,a.comId,a.content,a.talker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName");
		sql.append("\n  from meetTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join meetTalk e on a.parentid=e.id and a.comId=e.comId");
		sql.append("\n  left join userinfo d on e.talker=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return this.listQuery(sql.toString(), args.toArray(), MeetTalk.class);
	}

	/**
	 * 查询单个的会议留言
	 * @param id 留言主键
	 * @param comId 企业号
	 * @return
	 */
	public MeetTalk queryMeetTalk(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.recordCreateTime,a.id,a.parentid,a.comId,a.content,a.talker,c.uuid as talkerSmlImgUuid,a.meetingId,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName");
		sql.append("\n  from meetTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join meetTalk e on a.parentid=e.id and a.comId=e.comId ");
		sql.append("\n  left join userinfo d on e.talker=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return (MeetTalk) this.objectQuery(sql.toString(), args.toArray(), MeetTalk.class);
	}

	/**
	 * 留言的附件
	 * @param comId
	 * @param meetingId
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MeetTalkFile> listMeetTalkFile(Integer comId, Integer meetingId, Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.meetingId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext,\n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from meetTalkFile a inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comId =? and a.meetingId = ? \n");
		args.add(comId);
		args.add(meetingId);
		this.addSqlWhere(id, sql, args, " and a.talkId=?");
		sql.append("order by isPic asc, a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), MeetTalkFile.class);
	}

	/**
	 * 查询需要删除的留言
	 * @param comId 企业号
	 * @param talkId 留言主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MeetTalk> listMeetTalkForDel(Integer comId, Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from meetTalk where comId=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(talkId);
		return this.listQuery(sql.toString(), args.toArray(), MeetTalk.class);
	}

	/**
	 * 将留言向上提一级
	 * @param talkId
	 * @param comId
	 */
	public void updateMeetTalkParentId(Integer talkId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		MeetTalk meettalk = (MeetTalk) this.objectQuery(MeetTalk.class, talkId);
		//附件讨论的父节点主键
		Integer parentId = -1;
		if(null!=meettalk){
			parentId = meettalk.getParentId();
		}
		sql.append("update meetTalk set parentId=? where parentid = ? and comId = ?\n");
		args.add(parentId);
		args.add(talkId);
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 分页查询会议日志
	 * @param comId
	 * @param meetingId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MeetLog> listPagedScheLog(Integer comId, Integer meetingId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from meetLog a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.meetingId = ? \n");
		args.add(comId);
		args.add(meetingId);
		return this.pagedQuery(sql.toString()," a.recordcreatetime desc", args.toArray(), MeetLog.class);
	}

	/**
	 * 会议附件列表
	 * @param meetingId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SummaryFile> listPagedMeetUpfiles(Integer meetingId,
			Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from (");
		sql.append("\n select a.comId,a.meetingId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, ");
		sql.append("\n c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, '1' sourceType,a.id sourceId,");
		sql.append("\n case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic ");
		sql.append("\n from meetTalkFile a inner join upfiles b on a.comId = b.comId and a.upfileid = b.id ");
		sql.append("\n left join userinfo c on  a.userid = c.id \n");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("\n where a.comId =? and a.meetingId = ? \n");
		args.add(comId);
		args.add(meetingId);
		sql.append("\n union all ");
		sql.append("\n select a.comId,a.meetingId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,");
		sql.append("\n c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext,'0' sourceType,a.id sourceId,");
		sql.append("\n case when b.fileext in ('gif', 'jpg','jpeg', 'png', 'bmp')then 1 else 0 end as isPic");
		sql.append("\n from summaryFile a inner join upfiles b on a.comId = b.comId and a.upfileId = b.id");
		sql.append("\n left join userinfo c on  a.userid = c.id");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id");
		sql.append("\n where a.comId =? and a.meetingId = ? \n");
		args.add(comId);
		args.add(meetingId);
		sql.append("\n ) a ");
		return this.pagedQuery(sql.toString(), " a.sourceType,a.sourceId", args.toArray(), SummaryFile.class);
	}
	
	/**
	 * 查询附件总数
	 * @param meetingId
	 * @param comId
	 * @return
	 */
	public Integer countFile(Integer meetingId,Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(1) from (");
		sql.append("\n select a.meetingId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, ");
		sql.append("\n c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, '1' sourceType,a.id sourceId,");
		sql.append("\n case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic ");
		sql.append("\n from meetTalkFile a inner join upfiles b on a.comId = b.comId and a.upfileid = b.id ");
		sql.append("\n left join userinfo c on  a.userid = c.id \n");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("\n where a.comId =? and a.meetingId = ? \n");
		args.add(comId);
		args.add(meetingId);
		sql.append("\n union all ");
		sql.append("\n select a.meetingId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,");
		sql.append("\n c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext,'0' sourceType,a.id sourceId,");
		sql.append("\n case when b.fileext in ('gif', 'jpg','jpeg', 'png', 'bmp')then 1 else 0 end as isPic");
		sql.append("\n from summaryFile a inner join upfiles b on a.comId = b.comId and a.upfileId = b.id");
		sql.append("\n left join userinfo c on  a.userid = c.id");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id");
		sql.append("\n where a.comId =? and a.meetingId = ? \n");
		args.add(comId);
		args.add(meetingId);
		sql.append("\n ) a ");
		return this.countQuery(sql.toString(), args.toArray());
	}
	

	/**
	 * 判断是否有权限查看会议
	 * @param comId
	 * @param userId
	 * @param busId
	 * @return
	 */
	public Integer authCheckMeetUser(Integer comId,Integer userId, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(*) from (");
		//发布人
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName from meeting a");
		sql.append("\n inner join userinfo b on a.organiser=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		this.addSqlWhere(userId, sql, args, " and b.id=?");
		sql.append("\n union all ");
		//主持人
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName　 from meeting a");
		sql.append("\n inner join userinfo b on a.presenter=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		this.addSqlWhere(userId, sql, args, " and b.id=?");
		sql.append("\n union all ");
		//记录员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName from meeting a");
		sql.append("\n inner join userinfo b on a.recorder=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		this.addSqlWhere(userId, sql, args, " and b.id=?");
		sql.append("\n union all ");
		//与会人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName　 from meetPerson a");
		sql.append("\n inner join userinfo b on a.userId=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.meetingId=?");
		this.addSqlWhere(userId, sql, args, " and b.id=?");
		sql.append("\n union all ");
		//与会部门
		sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName from meetDep a");
		sql.append("\n inner join userorganic  b on a.depId=b.depId and a.comId=b.comId");
		sql.append("\n inner join userinfo c on b.userId=c.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.meetingId=?");
		this.addSqlWhere(userId, sql, args, " and c.id=?");
		
		sql.append("\n) a");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 会议参与人员
	 * @param comId 企业号
	 * @param busId 会议主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listMeetingUsers(Integer comId, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select distinct * from (");
		//发布人
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName,b.movePhone from meeting a");
		sql.append("\n inner join userinfo b on a.organiser=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		sql.append("\n union all ");
		//主持人
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName,b.movePhone　 from meeting a");
		sql.append("\n inner join userinfo b on a.presenter=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		sql.append("\n union all ");
		//记录员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName,b.movePhone from meeting a");
		sql.append("\n inner join userinfo b on a.recorder=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		sql.append("\n union all ");
		//与会人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName,b.movePhone　 from meetPerson a");
		sql.append("\n inner join userinfo b on a.userId=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.meetingId=?");
		sql.append("\n union all ");
		//与会部门
		sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName,c.movePhone from meetDep a");
		sql.append("\n inner join userorganic  b on a.depId=b.depId and a.comId=b.comId");
		sql.append("\n inner join userinfo c on b.userId=c.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.meetingId=?");
		
		sql.append("\n) a");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 *会议告知和参与人员
	 * @param comId 企业号
	 * @param userId 操作员主键
	 * @param busId 业务主键
	 * @param pushUserIdSet 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listMeetingViews(Integer comId, Integer busId, Set<Integer> pushUserIdSet) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select distinct * from (");
		//发布人
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName from meeting a");
		sql.append("\n inner join userinfo b on a.organiser=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		sql.append("\n union all ");
		//主持人
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName　 from meeting a");
		sql.append("\n inner join userinfo b on a.presenter=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		sql.append("\n union all ");
		//记录员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName from meeting a");
		sql.append("\n inner join userinfo b on a.recorder=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		sql.append("\n union all ");
		//与会人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName　 from meetPerson a");
		sql.append("\n inner join userinfo b on a.userId=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.meetingId=?");
		sql.append("\n union all ");
		//与会部门
		sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName from meetDep a");
		sql.append("\n inner join userorganic  b on a.depId=b.depId and a.comId=b.comId");
		sql.append("\n inner join userinfo c on b.userId=c.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.meetingId=?");
		sql.append("\n union all ");
		//告知人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName from noticePerson a");
		sql.append("\n inner join userinfo b on a.userId=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.meetingId=?");
		//本次推送的人员
		if(null!=pushUserIdSet && !pushUserIdSet.isEmpty()){
			sql.append("\n union");
			sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
			sql.append("\n from userinfo b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhereIn(pushUserIdSet.toArray(new Integer[pushUserIdSet.size()]), sql, args, "\n and b.id in ?");
		}
		
		sql.append("\n) a");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 查询本次邀请的成员
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 * @param ids 页面邀请的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listInvPerson(Integer meetingId, Integer comId,
			Object[] ids) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName from userinfo b where 1=1");
		this.addSqlWhereIn(ids, sql, args, " and b.id in ?");
		sql.append("\n minus");
		sql.append("\n select distinct * from (");
		//发布人
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName from meeting a");
		sql.append("\n inner join userinfo b on a.organiser=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(meetingId, sql, args, " and a.id=?");
		sql.append("\n union all ");
		//主持人
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName　 from meeting a");
		sql.append("\n inner join userinfo b on a.presenter=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(meetingId, sql, args, " and a.id=?");
		sql.append("\n union all ");
		//记录员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName from meeting a");
		sql.append("\n inner join userinfo b on a.recorder=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(meetingId, sql, args, " and a.id=?");
		sql.append("\n union all ");
		//与会人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName　 from meetPerson a");
		sql.append("\n inner join userinfo b on a.userId=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		sql.append("\n union all ");
		//与会部门
		sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName from meetDep a");
		sql.append("\n inner join userorganic  b on a.depId=b.depId and a.comId=b.comId");
		sql.append("\n inner join userinfo c on b.userId=c.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		
		sql.append("\n) a");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 检测会议状态
	 * @param id 会主键
	 * @param comId 企业号
	 * @return
	 */
	public Meeting checkMeetTimeOut(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		sql.append("\n select a.*,");
		//会议已结束
		sql.append("\n case when a.endDate<'"+nowDateTime+"' then 0 else  ");
		//会议已开始1 未开始2
		sql.append("\n case when a.startDate<='"+nowDateTime+"' then 1 else 2 end  ");
		sql.append("\n end timeOut,");
		sql.append("\n case when b.state is null then '-1' else b.state end applyState");
		sql.append("\n from meeting a left join roomapply b on a.meetingaddrid=b.roomid and a.id=b.meetingid");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		return (Meeting) this.objectQuery(sql.toString(), args.toArray(), Meeting.class);
	}

	/**
	 * 取得会议的周期设置
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 * @return
	 */
	public MeetRegular getMeetRegular(Integer meetingId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from meetRegular a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		return (MeetRegular) this.objectQuery(sql.toString(), args.toArray(), MeetRegular.class);
	}

	/**
	 * 通过申请取得会议信息
	 * @param comId 企业号
	 * @param appId 会议申请主键
	 * @param meetingId 
	 * @return
	 */
	public Meeting getMeetByAppId(Integer comId, Integer appId, Integer meetingId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.comId,a.title, a.startdate,a.enddate,a.organiser,a.meetingAddrId,a.meetingAddrName ");
		sql.append("\n from meeting a inner join roomapply b on a.id=b.meetingid and a.comId=b.comId");
		sql.append("\n where a.roomType=0 ");
		this.addSqlWhere(comId, sql, args, " and b.comId=?");
		this.addSqlWhere(appId, sql, args, " and b.id=?");
		this.addSqlWhere(meetingId, sql, args, " and b.meetingId=?");
		return (Meeting) this.objectQuery(sql.toString(), args.toArray(), Meeting.class);
	}

	/**
	 * 查询是否确认过事由
	 * @param comId 企业号
	 * @param userId
	 * @param meetingId 会议主键
	 * @return
	 */
	public MeetCheckUser getMeetCheckUser(Integer comId, Integer userId,
			Integer meetingId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from meetCheckUser a");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		return (MeetCheckUser) this.objectQuery(sql.toString(), args.toArray(), MeetCheckUser.class);
	}
	/**
	 * 查询参会人员确认状态
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 * @param userId 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MeetCheckUser> listMeetCheckUser(Integer busId,
			Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.state,b.reason,c.username,c.gender,d.uuid,d.filename,");
		sql.append("\n	case when (b.userid is null and a.userid="+userId+") then 1 else 0 end neworder");
		sql.append("\n	from (");
		sql.append("\n		select distinct * from (");
		//发布人
		sql.append("\n   		select a.organiser userId,a.comId,a.id meetingId from meeting a");
		sql.append("\n 			where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		sql.append("\n 			union all ");
		//主持人
		sql.append("\n 			select a.presenter userId,a.comId,a.id meetingId  from meeting a");
		sql.append("\n 			where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		sql.append("\n 			union all ");
		//记录员
		sql.append("\n 			select a.recorder userId,a.comId,a.id meetingId  from meeting a");
		sql.append("\n 			where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		sql.append("\n 			union all ");
		//与会人员
		sql.append("\n 			select a.userId userId,a.comId,a.meetingId  from meetPerson a");
		sql.append("\n 			where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.meetingId=?");
		sql.append("\n 			union all ");
		//与会部门
		sql.append("\n 			select b.userId userId,a.comId,a.meetingId from meetDep a");
		sql.append("\n 			inner join userorganic  b on a.depId=b.depId and a.comId=b.comId");
		sql.append("\n 			where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.meetingId=?");
		sql.append("\n	) a");
		sql.append("\n) a left join meetCheckUser b on a.userId=b.userid and a.comId=b.comId and a.meetingId=b.meetingId");
		sql.append("\n left join userinfo c on a.userId = c.id");
		sql.append("\n left join userOrganic cc on a.userId =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on cc.smallheadportrait = d.id");
		sql.append("\norder by neworder desc, a.userid");
		return this.listQuery(sql.toString(), args.toArray(), MeetCheckUser.class);
	}
	/**
	 * 查询会议用于会议室换管理员
	 * @param comId 企业号
	 * @param preManager 会议室以前的管理员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meeting> listMeetForRoomChange(Integer comId, Integer preManager) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from meeting a inner join todayworks b on a.comId=b.comId and a.id=b.busId");
		sql.append("\n and b.bustype='"+ConstantInterface.TYPE_MEETROOM+"'");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(preManager, sql, args, " and b.userid=?");
		return this.listQuery(sql.toString(), args.toArray(), Meeting.class);
	}
	/**
	 * 验证人员是否参会
	 * @param comId 企业号
	 * @param userId 用户主键
	 * @param busId 会议主键
	 * @return
	 */
	public MeetCheckUser checkMeetUser(Integer comId, Integer userId,
			Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from meetCheckUser a ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.meetingId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		return (MeetCheckUser) this.objectQuery(sql.toString(), args.toArray(), MeetCheckUser.class);
	}
	/**
	 * 获取团队所有会议主键集合
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meeting> listMeetingOfAll(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id from meeting a where a.comId = ?");
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(),Meeting.class);
	}
	/**
	 * 会议查看权限验证
	 * @param comId 团队主键
	 * @param meetingId 会议主键
	 * @param userId 验证人员主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meeting> authorCheck(Integer comId,Integer meetingId,Integer userId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.organiser as id from meeting a where a.id= ? and a.comId = ? ");
		args.add(meetingId);
		args.add(comId);
		sql.append("\n union ");
		sql.append("\n select a.presenter as id from meeting a where a.id= ? and a.comId = ? "); 
		args.add(meetingId);
		args.add(comId);
		sql.append("\n union ");
		sql.append("\n select a.recorder as id from meeting a where a.id= ? and a.comId = ? "); 
		args.add(meetingId);
		args.add(comId);
		sql.append("\n union ");
		sql.append("\n select b.userid as id from meeting a inner join meetPerson b on a.id = ? and a.comId = ? and a.id = b.meetingid and b.userid=? ");
		args.add(meetingId);
		args.add(comId);
		args.add(userId);
		sql.append("\n union ");
		sql.append("\n select b.userid as id from meeting a inner join noticePerson b on a.id = ? and a.comId = ? and a.id = b.meetingid and b.userid=? ");
		args.add(meetingId);
		args.add(comId);
		args.add(userId);
		sql.append("\n union ");
		sql.append("\n select c.userid as id from meeting a inner join meetDep b on a.id=? and a.comId=? and a.id = b.meetingid ");
		sql.append("\n inner join userOrganic c on c.comId = b.comId and b.depid=c.depid and c.userid = ? ");
		args.add(meetingId);
		args.add(comId);
		args.add(userId);
		return this.listQuery(sql.toString(), args.toArray(),Meeting.class);
	}

	/**
	 * 查询与该会议同时段的参与的其他会议集合
	 * @param meetIngId 会议主键
	 * @param userInfo 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Meeting> checkMeeting(Meeting meeting, UserInfo userInfo) {
		
		//本次会议开始时间
		String startDate = meeting.getStartDate();
		//本次会议结束时间
		String endDate = meeting.getEndDate();
		//当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select bb.*, case when  checkUser.state is null then 0 else checkUser.state end checkState");//会议没有结束
		sql.append("\n from meeting bb left join meetCheckUser checkUser on bb.id = checkUser.meetingid and checkUser.userid="+userInfo.getId());//会议没有结束
		sql.append("\n where bb.meetingstate=1 and bb.enddate>='"+nowDate+"' ");//会议没有结束
		sql.append("\n and bb.comId="+userInfo.getComId()+" and (");
		sql.append("\n 	(bb.startdate<='"+startDate+"' and bb.enddate>='"+startDate+"')");//在开始时间有段重合
		sql.append("\n 	or");
		sql.append("\n 	(bb.startdate<='"+endDate+"' and bb.enddate>='"+endDate+"')");//在结束时间有段重合
		sql.append("\n 	or");
		sql.append("\n (bb.startdate>='"+startDate+"' and bb.enddate<='2017-03-03 18:30')");//在中间有部分时间重合
		sql.append("\n ) and exists(");
		sql.append("\n 		select a.organiser as id from meeting a where a.id= bb.id and a.comId = bb.comId and a.organiser="+userInfo.getId());//是发起人
		sql.append("\n 		union");
		sql.append("\n 		select a.presenter as id from meeting a where a.id= bb.id and a.comId = bb.comId and a.presenter="+userInfo.getId());//是主持人
		sql.append("\n 		union");
		sql.append("\n 		select a.recorder as id from meeting a where a.id= bb.id and a.comId = bb.comId and a.recorder="+userInfo.getId());//是记录员
		sql.append("\n 		union");
		sql.append("\n  	select b.userid as id from meeting a inner join meetPerson b on a.id = b.meetingid and a.comId=b.comId");//是参会人员
		sql.append("\n 		and a.id = b.meetingid and b.userid="+userInfo.getId()+" where a.id=bb.id ");
		sql.append("\n 		union");
		sql.append("\n 		select c.userid as id from meeting a inner join meetDep b on a.id = b.meetingid and a.comId = b.comId");//是参会部门
		sql.append("\n 		and a.id = b.meetingid inner join userOrganic c on c.comId = b.comId and b.depid=c.depid and c.userid ="+userInfo.getId());
		sql.append("\n  	where a.id=bb.id ");
		sql.append("\n )");
		return this.listQuery(sql.toString(), args.toArray(), Meeting.class);
	}
	
	/**
	 * 获取会议学习人信息
	 * @param id
	 * @param comId
	 * @return
	 */
	public List<MeetLearn> listMeetLearn(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,a.meetingId,b.username as sharerName,b.gender,c.uuid,c.filename \n");
		sql.append("from meetLearn a \n");
		sql.append("inner join userInfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id = bb.userId and a.comId = bb.comId \n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id \n");
		sql.append("where a.comId=? and a.meetingId=? \n");
		args.add(comId);
		args.add(id);
		return this.listQuery(sql.toString(), args.toArray(), MeetLearn.class);
	}
}
