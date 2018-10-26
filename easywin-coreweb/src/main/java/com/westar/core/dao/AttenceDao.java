package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.westar.base.model.AttenceConnSet;
import com.westar.base.model.AttenceRecord;
import com.westar.base.model.AttenceRecordDetail;
import com.westar.base.model.AttenceRecordUpload;
import com.westar.base.model.AttenceRule;
import com.westar.base.model.AttenceTime;
import com.westar.base.model.AttenceType;
import com.westar.base.model.AttenceUser;
import com.westar.base.model.AttenceWeek;
import com.westar.base.model.Leave;
import com.westar.base.model.OverTime;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class AttenceDao extends BaseDao {
	/**
	 * 取得系统吗
	 * @param comId 团队号
	 * @param isSys 是否查询系统级别的 1是0否
	 * @return
	 */
	public AttenceRule getAttenceRule(Integer comId, Integer isSys) {
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.* from attenceRule a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		//是系统界别的
		this.addSqlWhere(isSys, sql, args, " and a.isSystem=?");
		return (AttenceRule) this.objectQuery(sql.toString(), args.toArray(), AttenceRule.class);
	}

	/**
	 * 取得考勤的工作日类型集合
	 * @param comId 团队号
	 * @param attenceRuleId 考勤规则主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AttenceType> listAttenceTypes(Integer comId,Integer attenceRuleId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.* from attenceType a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(attenceRuleId, sql, args, " and a.attenceRuleId=?");
		sql.append(" order by a.weekType desc,a.id");
		return this.listQuery(sql.toString(), args.toArray(), AttenceType.class);
	}
	/**
	 * 获取某星期数 的考勤时间段
	 * @param comId
	 * @param weekNum 星期数 0星期天
	 * @param attenceRuleId 考勤规则主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AttenceTime> queryAttenceTypeByWeek(Integer comId,Integer weekNum,Integer attenceRuleId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.* from attenceTime a");
		sql.append("\n left join   attenceType b  on a.comId = b.comId and b.id = a.attenceTypeId ");
		sql.append("\n left join attenceweek c on b.comId = c.comId and b.id = c.attenceTypeId ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and c.weekDay=?");
		this.addSqlWhere(attenceRuleId, sql, args, " and b.attenceRuleId=?");
		this.addSqlWhere(weekNum, sql, args, " and a.comId=?");
		sql.append(" order by a.dayTimeS");
		return this.listQuery(sql.toString(), args.toArray(), AttenceTime.class);
	}
	
	/**
	 * 查询工作日的星期数
	 * @param comId 团队号
	 * @param attenceRuleId 考勤规则主键
	 * @param attenceTypeId 工作日类型的主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AttenceWeek> listAttenceWeeks(Integer comId,
			Integer attenceRuleId, Integer attenceTypeId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.* from attenceWeek a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(attenceRuleId, sql, args, " and a.attenceRuleId=?");
		this.addSqlWhere(attenceTypeId, sql, args, " and a.attenceTypeId=?");
		sql.append(" order by a.weekDay,a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), AttenceWeek.class);
	}

	/**
	 * 取得工作时段集合
	 * @param comId 团队号
	 * @param attenceRuleId 考勤规则主键
	 * @param attenceTypeId 考情规则类型主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AttenceTime> listAttenceTimes(Integer comId,
			Integer attenceRuleId, Integer attenceTypeId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.* from attenceTime a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(attenceRuleId, sql, args, " and a.attenceRuleId=?");
		this.addSqlWhere(attenceTypeId, sql, args, " and a.attenceTypeId=?");
		sql.append(" order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), AttenceTime.class);
	}
	/**
	 * 某时间 请假记录
	 * @param creator
	 * @param date yyyy-MM-dd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Leave> listLeaveByDate(UserInfo creator,String date) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	");
		sql.append("\n select a.* ");
		sql.append("\n	from leave a");
		sql.append("\n	inner join spflowinstance tripFlow on a.instanceid=tripFlow.id and a.comid=tripFlow.comid");
		sql.append("\n inner join userOrganic userOrg on a.creator = userOrg.Userid and a.comid=userorg.comid");
		sql.append("\n where a.comid=? and tripFlow.flowstate<>0  and a.creator =? and  tripFlow.spState = 1");
		args.add(creator.getComId());
		args.add(creator.getId());
		sql.append("\n and a.startTime <= ? and a.endTime >= ?");
		args.add(date+" 23:59");
		args.add(date+" 00:00");
		return this.listQuery(sql.toString(), args.toArray(), Leave.class);
	}
	/**
	 * 某时间 请假记录
	 * @param creator
	 * @param date  yyyy-MM-dd HH:mm
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Leave> listLeaveByDateTime(UserInfo creator,String date) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	");
		sql.append("\n select a.* ");
		sql.append("\n	from leave a");
		sql.append("\n	inner join spflowinstance tripFlow on a.instanceid=tripFlow.id and a.comid=tripFlow.comid");
		sql.append("\n inner join userOrganic userOrg on a.creator = userOrg.Userid and a.comid=userorg.comid");
		sql.append("\n where a.comid=? and tripFlow.flowstate<>0  and a.creator =? and  tripFlow.spState = 1");
		args.add(creator.getComId());
		args.add(creator.getId());
		sql.append("\n and a.startTime <= ? and a.endTime >= ?");
		args.add(date);
		args.add(date);
		return this.listQuery(sql.toString(), args.toArray(), Leave.class);
	}
	/**
	 * 权限下请假列表
	 * @param curUser
	 * @param leave
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Leave> listPagedLeave(UserInfo curUser, Leave leave,boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.recordcreatetime,a.starttime,a.endtime,a.bustype,a.leaveTime,a.creator,a.status,");
		sql.append("\n a.instanceid,tripFlow.flowname,tripFlow.flowstate,tripFlow.spstate,userinfo.username as creatorName,");
		sql.append("\n flowCurExecutor.executor,curSpUser.username as executorName");
		sql.append("\n	from leave a");
		sql.append("\n	inner join spflowinstance tripFlow on a.instanceid=tripFlow.id and a.comid=tripFlow.comid");
		sql.append("\n inner join userinfo on a.creator=userinfo.id");
		sql.append("\n left join spFlowCurExecutor flowCurExecutor on tripFlow.comId = flowCurExecutor.comid ");
		sql.append("\n 	and tripFlow.id = flowCurExecutor.busId and flowCurExecutor.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n left join userinfo curSpUser on flowCurExecutor.Executor=curSpUser.id");
		sql.append("\n where a.comid=? and tripFlow.flowstate<>0");
		args.add(curUser.getComId());
		//当前人个人申请
		this.addSqlWhere(leave.getCreator(), sql, args, " and a.creator =?");
		
		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("and (\n");
			//创建人权限验证
			sql.append("a.creator =? \n");
			args.add(curUser.getId());
			//创建人上级权限验证
			sql.append("or exists(\n");
			sql.append("	select id from myLeaders where creator = a.creator and leader = ? and comId = ? and creator <> leader \n");
			args.add(curUser.getId());
			args.add(curUser.getComId());
			sql.append(")\n");
			sql.append(") \n");
		}
		

		if(null!=leave.getFlowState() && leave.getFlowState()>-1){
			this.addSqlWhere(leave.getFlowState(), sql, args, " and tripFlow.flowstate=?");//流程状态筛选
		}
		this.addSqlWhere(leave.getSpState(), sql, args, " and tripFlow.spState=?");//审批状态筛选
		this.addSqlWhere(leave.getExecutor(), sql, args, " and  flowCurExecutor.executor=?");//流程审批人筛选
		if(null != leave.getListExecutor() && !leave.getListExecutor().isEmpty()){
			sql.append("	and  ( flowCurExecutor.executor= 1 ");
			for(UserInfo executor : leave.getListExecutor()){
				sql.append("or flowCurExecutor.executor=?  \n");
				args.add(executor.getId());
			}
			sql.append("	 ) ");
		}
		
		if(null != leave.getStartTime() && null != leave.getEndTime()){
			sql.append("\n	and ((a.STARTTIME >= ? and a.STARTTIME  <= ?)");
			sql.append("\n		or (a.ENDTIME >= ? and a.ENDTIME  <= ?))");
			args.add(leave.getStartTime()+" 00:00");
			args.add(leave.getEndTime()+" 23:59");
			args.add(leave.getStartTime()+" 00:00");
			args.add(leave.getEndTime()+" 23:59");
//			sql.append("\n	and (");
//			sql.append("\n	( ? <=a.startTime   and ?<= a.endTime  and ?>=a.startTime and ?>= a.endTime )");
//			args.add(leave.getStartTime()+" 00:00");
//			args.add(leave.getStartTime()+" 00:00");
//			args.add(leave.getEndTime()+" 23:59");
//			args.add(leave.getEndTime()+" 23:59");
//			sql.append("\n	 or (? >=  a.startTime and ?>= a.startTime and ? >= a.endTime)");
//			args.add(leave.getStartTime()+" 00:00");
//			args.add(leave.getEndTime()+" 23:59");
//			args.add(leave.getEndTime()+" 23:59");
//			sql.append("\n	   or(? <= a.startTime and ?  >= a.startTime and ? <= a.endTime)");
//			args.add(leave.getStartTime()+" 00:00");
//			args.add(leave.getEndTime()+" 23:59");
//			args.add(leave.getEndTime()+" 23:59");
//			sql.append("\n	 or(?<= a.endTime and ?>= a.startTime)");
//			args.add(leave.getEndTime()+" 23:59");
//			args.add(leave.getStartTime()+" 00:00");
//			sql.append("\n	  )");
		}
		
		//查询创建时间段
		this.addSqlWhere(leave.getStartDate(), sql, args, " and substr(tripFlow.recordcreatetime,0,10)>=?");
		this.addSqlWhere(leave.getEndDate(), sql, args, " and substr(tripFlow.recordcreatetime,0,10)<=?");
		this.addSqlWhereLike(leave.getFlowName(), sql, args, " and tripFlow.flowName like ? \n");//流程名称筛选
		String orderBy ="";//列表排序
		if(null==leave.getOrderBy() || "".equals(leave.getOrderBy())){
			orderBy =" tripFlow.recordcreatetime desc,tripFlow.flowState";
		}else if("executor".equals(leave.getOrderBy())){
			orderBy =" curSpUser.username";
		}else if("crTimeNewest".equals(leave.getOrderBy())){
			orderBy =" tripFlow.recordCreateTime desc";
		}else if("crTimeOldest".equals(leave.getOrderBy())){
			orderBy =" tripFlow.recordCreateTime asc";
		}
		return this.pagedQuery(sql.toString(), orderBy, args.toArray(), Leave.class);
	}
	/**
	 * 权限下加班列表
	 * @param curUser
	 * @param overTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OverTime> listPagedOverTime(UserInfo curUser, OverTime overTime,boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.recordcreatetime,a.starttime,a.endtime,a.overTime,a.xzhsStartTime,a.xzhsEndTime,a.xzhsOverTime,a.creator,a.status,");
		sql.append("\n a.instanceid,tripFlow.flowname,tripFlow.flowstate,tripFlow.spstate,userinfo.username as creatorName,");
		sql.append("\n flowCurExecutor.executor,curSpUser.username as executorName");
		sql.append("\n	from overTime a");
		sql.append("\n	inner join spflowinstance tripFlow on a.instanceid=tripFlow.id and a.comid=tripFlow.comid");
		sql.append("\n inner join userinfo on a.creator=userinfo.id");
		sql.append("\n left join spFlowCurExecutor flowCurExecutor on tripFlow.comId = flowCurExecutor.comid ");
		sql.append("\n  and tripFlow.id = flowCurExecutor.busId and flowCurExecutor.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n left join userinfo curSpUser on flowCurExecutor.Executor=curSpUser.id");
		sql.append("\n where a.comid=? and tripFlow.flowstate<>0");
		args.add(curUser.getComId());
		//当前人个人申请
		this.addSqlWhere(overTime.getCreator(), sql, args, " and a.creator =?");
		
		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("and (\n");
			//创建人权限验证
			sql.append("a.creator =? \n");
			args.add(curUser.getId());
			//创建人上级权限验证
			sql.append("or exists(\n");
			sql.append("	select id from myLeaders where creator = a.creator and leader = ? and comId = ? and creator <> leader \n");
			args.add(curUser.getId());
			args.add(curUser.getComId());
			sql.append(")\n");
			sql.append(") \n");
		}
		if(null != overTime.getStartTime() && null != overTime.getEndTime()){
			sql.append("\n	and a.xzhsStartTime >= ? and  a.xzhsStartTime <= ?");
			args.add(overTime.getStartTime()+" 00:00");
			args.add(overTime.getEndTime()+" 23:59");
		}

		if(null!=overTime.getFlowState() && overTime.getFlowState()>-1){
			this.addSqlWhere(overTime.getFlowState(), sql, args, " and tripFlow.flowstate=?");//流程状态筛选
		}
		this.addSqlWhere(overTime.getSpState(), sql, args, " and tripFlow.spState=?");//审批状态筛选
		this.addSqlWhere(overTime.getExecutor(), sql, args, " and  flowCurExecutor.executor=?");//流程审批人筛选
		if(null != overTime.getListExecutor() && !overTime.getListExecutor().isEmpty()){
			sql.append("	and  ( flowCurExecutor.executor= 1 ");
			for(UserInfo executor : overTime.getListExecutor()){
				sql.append("or flowCurExecutor.executor=?  \n");
				args.add(executor.getId());
			}
			sql.append("	 ) ");
		}
		//查询创建时间段
		this.addSqlWhere(overTime.getStartDate(), sql, args, " and substr(tripFlow.recordcreatetime,0,10)>=?");
		this.addSqlWhere(overTime.getEndDate(), sql, args, " and substr(tripFlow.recordcreatetime,0,10)<=?");
		this.addSqlWhereLike(overTime.getFlowName(), sql, args, " and tripFlow.flowName like ? \n");//流程名称筛选
		String orderBy ="";//列表排序
		if(null==overTime.getOrderBy() || "".equals(overTime.getOrderBy())){
			orderBy =" tripFlow.recordcreatetime desc,tripFlow.flowState";
		}else if("executor".equals(overTime.getOrderBy())){
			orderBy =" curSpUser.username";
		}else if("crTimeNewest".equals(overTime.getOrderBy())){
			orderBy =" tripFlow.recordCreateTime desc";
		}else if("crTimeOldest".equals(overTime.getOrderBy())){
			orderBy =" tripFlow.recordCreateTime asc";
		}
		return this.pagedQuery(sql.toString(), orderBy, args.toArray(), OverTime.class);
	}
	/**
	 * 查询考勤记录
	 * @param userInfo
	 * @param attenceRecord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AttenceRecord> listAttenceRecord(UserInfo userInfo, AttenceRecord attenceRecord) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	");
		
		sql.append("\n	select a.*,c.userName ");
		sql.append("\n	from attenceRecord a ");
		sql.append("\n	inner join userOrganic cc on a.comid = cc.comid and a.enrollNumber = cc.enrollNumber ");
		sql.append("\n	inner join userinfo c on cc.userId = c.id ");
		
		sql.append("\n	where a.comId = ? ");
		args.add(userInfo.getComId());
		
		this.addSqlWhere(attenceRecord.getUserId(), sql, args, " and c.id =?");
		return this.pagedQuery(sql.toString(), " a.time desc", args.toArray(), AttenceRecord.class);
	}
	
	/**
	 * 查询考勤记录
	 * @param userInfo
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AttenceRecord> listAttenceByUser(UserInfo userInfo,String date) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n	select a.*");
		sql.append("\n	from attenceRecord a ");
		sql.append("\n	where a.comId = ? and a.enrollNumber = ? ");
		args.add(userInfo.getComId());
		args.add(userInfo.getEnrollNumber());
		
		if(null !=date && !date.isEmpty()){
			sql.append("\n	and a.time >= ? and a.time<=?");
			args.add(date+" 00:00:00");
			args.add(date+" 23:59:59");
		}
		
		sql.append("\n	order by a.time asc");

		return this.listQuery(sql.toString(), args.toArray(), AttenceRecord.class);
	}
	
	/**
	 * 查询连接配置
	 * @param userInfo
	 * @return 
	 */
	public AttenceConnSet queryAttenceConnSet(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n	select a.*");
		sql.append("\n	from attenceConnSet a ");
		sql.append("\n	where a.comId = ? ");
		args.add(userInfo.getComId());
		return (AttenceConnSet) this.objectQuery(sql.toString(), args.toArray(), AttenceConnSet.class);
		
	}
	/**
	 * 获取上次上传的时间
	 * @param userInfo
	 * @return
	 */
	public AttenceRecordUpload queryLastUpload(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n	select max(a.lastTime) lastTime");
		sql.append("\n	from AttenceRecordUpload a ");
		sql.append("\n	where a.comId = ? ");
		args.add(userInfo.getComId());
		return (AttenceRecordUpload) this.objectQuery(sql.toString(), args.toArray(), AttenceRecordUpload.class);
	}
	/**
	 * 某人请假次数，时间统计
	 * @param userInfo
	 * @param attenceRecord 统计条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject attenceDetailStatistics(UserInfo userInfo,AttenceRecord attenceRecord,String busType){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n	select count(distinct a.busId) totalNum,nvl(sum(a.attenchour),0) totalSum  ");
		sql.append("\n	from attenceDetail a ");
		sql.append("\n	where a.comid=? and a.userid = ? and a.busType=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(busType);
		this.addSqlWhere(attenceRecord.getStartDate(), sql, args, "\n and a.attencDate>=?");
		this.addSqlWhere(attenceRecord.getEndDate(), sql, args, "\n and a.attencDate<=?");
		return this.objectQueryJSON(sql.toString(), args.toArray());
	}
	/**
	 * 考勤统计列表
	 * @param userInfo
	 * @param attenceRecord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listAttenceStatistics(UserInfo userInfo,AttenceRecord attenceRecord){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.userName,a.comId,a.depId,a.enrollNumber,a.depName,");
		sql.append("\n count(a.leaveId) leaveTimeT,sum(a.leaveTime) leaveTotal,");
		sql.append("\n count(a.overTimeId) overTimeT,sum(a.overTime) overTimeTotal from (");
		
		sql.append("\n select a.id,case when a.userName is null then a.email else a.userName end as userName,");
		sql.append("\n aa.comId,aa.depId,aa.enrollNumber,d.depName,");
		sql.append("\n leave.leaveTime,leave.id leaveId,overTime.overTime,overTime.id overTimeId");
		
		sql.append("\n from userinfo a inner join userOrganic aa on a.id=aa.userId");
		sql.append("\n inner join department d on aa.depId=d.id and d.comId = aa.comid");
		sql.append("\n left join  leave leave on aa.comId = leave.comid and a.id = leave.creator");
		sql.append("\n left join spflowinstance tripFlow on leave.instanceid=tripFlow.id and leave.comid=tripFlow.comid  and tripFlow.spState =1");
		sql.append("\n left join  overTime overTime on aa.comId = overTime.comid and a.id = overTime.creator");
		sql.append("\n left join spflowinstance flow on overTime.instanceid=flow.id and overTime.comid=flow.comid  and flow.spState =1");
		sql.append("\n where aa.enabled=1 and aa.inService = 1 	and aa.comId=? " );
		args.add(userInfo.getComId());
		sql.append("\n ) a where 1= 1" );
		sql.append("\n group by a.id,a.userName,a.comId,a.depId,a.enrollNumber,a.depName " );
		
		return this.pagedQuery(sql.toString()," leaveTimeT desc ,overTimeT desc,  a.enrollNumber asc nulls last,a.id asc ", args.toArray(), UserInfo.class);
	}
	/**
	 * 分页查询  考勤人员
	 * @param attenceUser
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AttenceUser> listPageAttenceUser(AttenceUser attenceUser,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from attenceUser a");
		sql.append("\n where a.comId = ?");
		args.add(userInfo.getComId());
		
		this.addSqlWhereLike(attenceUser.getName(), sql, args, " and a.name like ?");
		
		return this.pagedQuery(sql.toString(), " a.id ", args.toArray(), AttenceUser.class);
	}

	/**
	 * 获取所有公司编号
	 * @param
	 * @return java.util.List<java.lang.String>
	 * @author LiuxXiaoLin
	 * @date 2018/6/8 0008 14:33
	 */
	public List<UserInfo> listCompanies(){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		sql.append("select orgNum comid from organic");
		List<UserInfo> list = this.listQuery(sql.toString(),args.toArray(),UserInfo.class);
		return list;
	}

/**
 *  通过申请时间查询加班
 * @param userInfo
 * @param date
 * @return java.util.List<com.westar.base.model.OverTime>
 * @date 2018/6/11 0011 14:44
 */
	public List<OverTime> listOverTime(UserInfo userInfo,String date){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("\n	select a.*");
		sql.append("\n	from overTime a ");
		sql.append("\n	where a.comId = ? and a.creator = ? ");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());

		if(null !=date && !date.isEmpty()){
			sql.append("\n	and a.startTime >= ? and a.endTime<=?");
			args.add(date + " 00:00:00");
			args.add(date + " 23:59:59");
		}

		sql.append("\n	order by a.endTime asc");
		return this.listQuery(sql.toString(), args.toArray(), OverTime.class);
	}

	/**
	 * 通过行政核实时间查询加班
	 * @param userInfo
 * @param date
	 * @return java.util.List<com.westar.base.model.OverTime>
	 * @author LiuXiaoLin
	 * @date 2018/6/11 0011 14:45
	 */
	public List<OverTime> listOverTimeByXZHSTime(UserInfo userInfo,String date){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("\n	select a.*");
		sql.append("\n	from overTime a ");
		sql.append("\n	where a.comId = ? and a.creator = ? ");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());

		if(null !=date && !date.isEmpty()){
			sql.append("\n	and a.xzhsStartTime >= ? and a.xzhsEndTime<=?");
			args.add(date + " 00:00:00");
			args.add(date + " 23:59:59");
		}

		sql.append("\n	order by a.xzhsEndTime asc");
		return this.listQuery(sql.toString(), args.toArray(), OverTime.class);
	}

	/**
	 * 取得该团队的考勤数据
	 * @param orgNum
	 * @return
	 */
	public JSONObject queryFirstAttenceDate(Integer orgNum) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	select * from (");
		sql.append("\n	select substr(a.time,0,10),row_number() over ( order by a.time asc) as new_order ");
		sql.append("\n	from attencerecord a");
		sql.append("\n	where a.comId=?");
		args.add(orgNum);
		sql.append("\n	)a where new_order=1");
		return this.objectQueryJSON(sql.toString(), args.toArray());
	}

	/**
	 * 查询未同步的考勤数据
	 * @param nowDate
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AttenceRecordDetail> listAttenceRecordForInit(String nowDate,
			Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from (");
		sql.append("\n select b.id,a.comId, a.userid,a.enrollNumber,c.time,nvl(b.recordType,-1) recordType ");
		sql.append("\n from userorganic a");
		sql.append("\n left join attenceRecord c on a.comid=c.comId and a.enrollNumber=c.enrollNumber ");
		sql.append("\n and substr(c.time,0,10)='"+nowDate+"'");
		sql.append("\n left join attenceRecordDetail b on a.comId=b.comId and a.enrollNumber=b.enrollNumber ");
		sql.append("\n and a.userid=b.userid and b.attencdate= '"+nowDate+"'");
		sql.append("\n where a.comId=? and a.enrollNumber>0");
		args.add(comId);
		sql.append("\n) a where nvl(recordType,-1)=-1");
		sql.append("\n order by a.userid,a.time");
		return this.listQuery(sql.toString(), args.toArray(), AttenceRecordDetail.class);
	}
}
