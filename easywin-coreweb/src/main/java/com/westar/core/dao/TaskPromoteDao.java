package com.westar.core.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.westar.base.enums.OverDueLevelEnum;
import com.westar.base.model.Department;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.Task;
import com.westar.base.model.TaskExecuteTime;
import com.westar.base.model.TaskExecutor;
import com.westar.base.model.TaskHandOver;
import com.westar.base.model.TaskSharer;
import com.westar.base.model.TaskTalk;
import com.westar.base.model.TaskTalkUpfile;
import com.westar.base.model.TaskUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.ModStaticVo;
import com.westar.base.pojo.RelateModeVo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.web.PaginationContext;

@Repository
public class TaskPromoteDao extends BaseDao {

	/********************************任务查询****************************************/
	/**
	 * 获取督察任务集合(所有)
	 * @param task
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listPageTask(Task task,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* ");
		sql.append(" from ( \n");
		
		//结果集最外取别名
		sql.append("\n 		select distinct (a.id),a.comid,a.taskname,a.owner,a.recordcreatetime,a.state,a.busId,a.busType,");
		sql.append("\n 		a.grade,c.username as ownerName,c.gender,");
		sql.append("\n 		a.taskprogress totalProgress,a.dealtimelimit taskLimiteDate,");

		sql.append("\n 		case when toDo.todonum>0 then 0 else 1 end as isread,\n");

		sql.append("\n 		case when atten.id is null then 0 else 1 end as attentionState,");

		sql.append("\n 		case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.itemName ");
		sql.append("\n 		when a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' then spFlow.flowName ");
		sql.append("\n 		when a.bustype='"+ConstantInterface.TYPE_DEMAND_PROCESS+"' then dp.serialNum ");
		sql.append("\n 		when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.customername end busName, ");

		sql.append("\n 		case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.delState ");
		sql.append("\n 			when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.delState end busDelState, ");
		sql.append("\n 		case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.state else 0 end busState ");
		//查询任务
		sql.append("\n 		from task a ");
		//关联项目
		sql.append("\n 		left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0");
		//关联的客户
		sql.append("\n 		left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 ");
		//关联的审批
		sql.append("\n 		left join spflowinstance spflow on a.comid=spflow.comid and a.busid=spflow.id and a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"'");
		//关联
		sql.append(" left join demandprocess dp on a.comid=dp.comid and a.busid=dp.id and a.bustype='"+ConstantInterface.TYPE_DEMAND_PROCESS+"' \n");
		//任务负责人
		sql.append("\n 		left join userinfo c on a.owner = c.id \n");
		sql.append("\n 		left join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		//关注信息
		sql.append("\n 		left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("\n 		left join  \n");
		//消息通知
		sql.append("\n		( ");
		sql.append("\n 			select toDo.Busid,count(toDo.Busid) todonum from todayworks toDo \n");
		sql.append("\n 			where toDo.isclock=0 and toDo.readState=0 and toDo.Bustype=?  and toDo.Userid=? ");
		sql.append("\n 			group by toDo.Busid");
		sql.append("\n		) toDo on a.id=toDo.busid ");
		args.add(ConstantInterface.TYPE_TASK);
		args.add(userInfo.getId());
		
		//经办人是否在关注人员里面
		sql.append("\n left join (\n");
		sql.append("\n 		 select handOver.taskid,handOver.userid from(");
		sql.append("\n 		 	select handOver.taskid,handOver.Fromuser userId from  taskHandOver handOver ");
		sql.append("\n 		  	inner join PERSONATTENTION att on att.userId=handOver.Fromuser");
		sql.append("\n 		  	where handOver.comId=? and att.CREATOR=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n 		 	union");
		sql.append("\n 		 	select handOver.taskid,handOver.touser userId from  taskHandOver handOver ");
		sql.append("\n 		  	inner join PERSONATTENTION att on att.userId=handOver.touser");
		sql.append("\n 		  	where handOver.comId=? and att.CREATOR=? ");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		
		sql.append("\n 		 ) handOver");

		sql.append(")m on m.taskId = a.id \n");
		//任务没有被删除
		sql.append("\n 		where a.delstate=0 and  a.comid = ? ");
		args.add(userInfo.getComId());
		//版本
		this.addSqlWhere(task.getVersion(), sql, args, "\n and a.version=?");
		
		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("\n and (\n");
			//负责人权限验证
			sql.append("\n a.owner=? ");
			args.add(userInfo.getId());
			
			//经办人在关注人员里面
			sql.append("\n or nvl(m.taskId,0) != 0 ");
			//执行人上级权限验证
			sql.append("\n or exists( ");
			sql.append("\n  	select b.id from taskExecutor b where a.id = b.taskid and \n");
			sql.append("\n  	(b.executor =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.executor and sup.leader=?))\n");
			sql.append("\n ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.id from tasksharer b where a.id = b.taskid and \n");
			sql.append("  	(b.sharerid =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.sharerid and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//负责人上级权限验证
			sql.append("\n or exists(select *from myLeaders sup where sup.comid=?  and sup.leader=? and  sup.creator=a.owner)\n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//关注人员
			sql.append("\n or exists( ");
			sql.append("\n  	select b.id from taskExecutor b where a.id = b.taskid and b.executor in\n");
			sql.append("\n  	(SELECT userId from PERSONATTENTION a where a.comid=? and CREATOR = ?)\n");
			sql.append("\n ) \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			
			sql.append("\n ) \n");
		}

		//有查询关联模块信息
		String relateModType = task.getRelateModType();
		if(!StringUtils.isEmpty(relateModType)){
			List<RelateModeVo> modeVos = task.getListRelateModes();
			List<Integer> busIds = new ArrayList<Integer>();
			if(null!=modeVos){
				for (RelateModeVo relateModeVo : modeVos) {
					busIds.add(relateModeVo.getBusId());
				}
				if(ConstantInterface.TYPE_ITEM.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and item.id in ?");
				}else if(ConstantInterface.TYPE_CRM.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and crm.id in ?");
				}
			}
		}

		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		List<UserInfo> executors = task.getListExecutor();

		//状态这个筛选条件比较复杂。当只有状态或者有与其无关联条件时这个状态只针对任务本身。
		//但是当有经办人或者执行人与其一起作为筛选条件时，则协同任务还涉及到经办人对任务的状态。
		if(null!=task.getState()){
			switch (task.getState()){
				case 1:
					//进行中
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
					break;
				case 3:
					//挂起，暂停
					this.addSqlWhere(task.getState(), sql, args, "\n and a.state=?");
					break;
				case 4:
					//已完成但不关联经办人并且也不关联执行人。
					if(CommonUtil.isNull(executors) && CommonUtil.isNull(task.getListOperator())){
						this.addSqlWhere(task.getState(), sql, args, "\n and a.state=?");
					}
					break;
				default:
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
					break;
			}
		}

		//执行人选择
		if(!CommonUtil.isNull(executors)){
			sql.append("\n and a.state in (0,1) and  exists(  ");
			List<Integer> executorIds = new ArrayList<Integer>();
			for(UserInfo executor : executors){
				executorIds.add(executor.getId());
			}
			sql.append("\n select a.id from taskExecutor taskEx   ");
			sql.append("\n where taskEx.taskId=a.id  ");
			this.addSqlWhereIn(executorIds.toArray(), sql, args, "\n and taskEx.executor in ?");
			if(null!=task.getState()){
				if(task.getState().equals(1)){
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and taskEx.state in ?");
				}else{
					this.addSqlWhere(task.getState(), sql, args, "\n and taskEx.state=?");
				}
			}
			sql.append("	 ) ");
		}

		List<Department> listExecuteDep = task.getListExecuteDep();
		//执行人所在部门
		if(!CommonUtil.isNull(listExecuteDep)){
			List<Integer> depIds = new ArrayList<>();
			for (Department department : listExecuteDep) {
				depIds.add(department.getId());
			}
			sql.append("\n and  exists  ");
			sql.append("\n (");
			sql.append("\n  	select u.id from  userInfo u");
			sql.append("\n  	inner join userOrganic uo on u.id = uo.userId and uo.comId = ? \n");
			args.add(userInfo.getComId());
			sql.append("\n  	inner join department  executeDep on uo.depId = executeDep.Id and  uo.comId = executeDep.comId \n");
			sql.append("\n  	where 1=1 ");
			sql.append("\n  	and exists(");
			sql.append("\n  		select taskEx.id from taskexecutor taskEx ");
			sql.append("\n  		where taskEx.executor=u.id and taskEx.taskid=a.id ");
			sql.append("\n  	)");
			sql.append("\n  	and exists(");
			this.addSqlWhereIn(depIds.toArray(), sql, args, "\n SELECT * from DEPTREE  where depid in ? and depid = executeDep.id ");
			sql.append("\n  	)");
			sql.append("\n ) and a.state in (0,1)");
		}

		//执行人类型
		String execuType =  task.getExecuType();
		if(null!=execuType && !"".equals(execuType)){
			if("0".equals(execuType)){//查询自己的
				sql.append("\n and exists(");
				sql.append("\n select taskEx.id from taskexecutor taskEx");
				sql.append("\n where taskEx.executor=? and taskEx.taskid=a.id ");
				sql.append("\n )");
				args.add(userInfo.getId());
				this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in  ? ");
			}else if("1".equals(execuType)){//查询下属的

				sql.append("\n and exists(");
				sql.append("\n select taskEx.id from taskexecutor taskEx");
				sql.append("\n where taskEx.EXECUTOR in(select sup.CREATOR from myLeaders sup where sup.comid=? and sup.leader=? and  sup.CREATOR != ?) and taskEx.taskid=a.id ");
				sql.append("\n )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
				args.add(userInfo.getId());
				this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in  ? ");
			}
		}

		//任务紧急度筛选
		this.addSqlWhere(task.getGrade(), sql, args, "\n and grade=?");

		//负责人的主键集合
		if(null != task.getListOwner() && !task.getListOwner().isEmpty()){
			List<Integer> ownerIds = new ArrayList<Integer>();
			for(UserInfo owner : task.getListOwner()){
				ownerIds.add(owner.getId());
			}
			this.addSqlWhereIn(ownerIds.toArray(), sql, args, "\n and a.owner in ?");
		}
		//经办时间
		String operatStartDate = task.getOperatStartDate(); 
		String operatEndDate = task.getOperatEndDate(); 
		
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		//经办人筛选
		if(!CommonUtil.isNull(task.getListOperator())){
			//不包括正在实行的
			String containExecutor = task.getContainExecutor();

			sql.append("\n and exists (\n");
			sql.append("\n 		 select handOver.taskid,handOver.userid from(");
			sql.append("\n 		 	select handOver.taskid,handOver.Fromuser userId from  taskHandOver handOver ");
			sql.append("\n 		  	where handOver.comId=? ");
			args.add(userInfo.getComId());
			
			
			if(StringUtils.isNotEmpty(operatStartDate)
					|| StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 		  	and handOver.endTime is not null  ");
			}
			//查询创建时间段
			this.addSqlWhere(operatStartDate, sql, args, " and substr(handOver.recordcreatetime,0,10)>=?");
			if(StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 		  	and substr(nvl(handOver.endTime,?),0,10)<=?  ");
				args.add(nowDate);
				args.add(operatEndDate);
			}
			sql.append("\n 		 	union");
			sql.append("\n 		 	select handOver.taskid,handOver.touser userId from  taskHandOver handOver ");
			sql.append("\n 		  	where handOver.comId=? ");
			args.add(userInfo.getComId());
			
			if(StringUtils.isNotEmpty(operatStartDate)
					|| StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 		  	and handOver.endTime is not null  ");
			}
			//查询创建时间段
			this.addSqlWhere(operatStartDate, sql, args, " and substr(handOver.recordcreatetime,0,10)>=?");
			//查询创建时间段
			if(StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 		  	and substr(nvl(handOver.endTime,?),0,10)<=?  ");
				args.add(nowDate);
				args.add(operatEndDate);
			}
			sql.append("\n 		 ) handOver");
			if(!StringUtils.isEmpty(containExecutor) && containExecutor.equals("1")){
				//任务正在办理的执行人统计
				sql.append("\n 	left join (");
				sql.append("\n 		select count(taskex.executor) exnum, taskex.taskid, taskex.executor");
				sql.append("\n 		from taskexecutor taskex");
				sql.append("\n 		where taskex.comid = ? and taskex.state in(0,1)");
				args.add(userInfo.getComId());
				sql.append("\n  	group by taskex.taskid, taskex.executor");
				//只统计正在办理的任务信息
				sql.append("\n  ) taskexB on handOver.taskId = taskexB.taskid ");
				sql.append("\n and handOver.userId= taskexB.executor");
			}

			sql.append("\n where handOver.taskId=a.id ");
			List<Integer> operatorIds = new ArrayList<Integer>();
			for (UserInfo operator : task.getListOperator()) {
				operatorIds.add(operator.getId());
			}
			this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n and handOver.userId in ?");
			if(!StringUtils.isEmpty(containExecutor) && containExecutor.equals("1")){
				sql.append("\n and nvl(taskexB.exnum,0)=0");
			}
			if(null!=task.getState()) {
				sql.append("\n	and exists(");
				sql.append("\n	select taskEx.taskId from taskExecutor taskEx where handOver.taskId=taskEx.taskId");
				if (task.getState().equals(1)) {
					sql.append("\n	 and (taskEx.state=0 or taskEx.state=1)");
				}else{
					sql.append("\n	 and taskEx.state=?");
					args.add(task.getState());
				}
				this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n  and taskEx.EXECUTOR in ?");
				sql.append("\n	)");
			}
			sql.append(")\n");


		}else{
			if(StringUtils.isNotEmpty(operatStartDate)
					|| StringUtils.isNotEmpty(operatEndDate)){
				//不包括正在实行的
				String containExecutor = task.getContainExecutor();

				sql.append("\n and exists (\n");
				sql.append("\n 		 select handOver.taskid,handOver.userid from(");
				sql.append("\n 		 	select handOver.taskid,handOver.Fromuser userId from  taskHandOver handOver ");
				sql.append("\n 		  	where handOver.comId=? ");
				args.add(userInfo.getComId());
				sql.append("\n 		  	and handOver.endTime is not null  ");
				//查询创建时间段
				this.addSqlWhere(operatStartDate, sql, args, " and substr(handOver.recordcreatetime,0,10)>=?");
				if(StringUtils.isNotEmpty(operatEndDate)){
					sql.append("\n 		  	and substr(nvl(handOver.endTime,?),0,10)<=?  ");
					args.add(nowDate);
					args.add(operatEndDate);
				}
				sql.append("\n 		 	union");
				sql.append("\n 		 	select handOver.taskid,handOver.touser userId from  taskHandOver handOver ");
				sql.append("\n 		  	where handOver.comId=? ");
				args.add(userInfo.getComId());
				sql.append("\n 		  	and handOver.endTime is not null  ");
				//查询创建时间段
				this.addSqlWhere(operatStartDate, sql, args, " and substr(handOver.recordcreatetime,0,10)>=?");
				if(StringUtils.isNotEmpty(operatEndDate)){
					sql.append("\n 		  	and substr(nvl(handOver.endTime,?),0,10)<=?  ");
					args.add(nowDate);
					args.add(operatEndDate);
				}
				sql.append("\n 		 ) handOver");
				if(!StringUtils.isEmpty(containExecutor) && containExecutor.equals("1")){
					//任务正在办理的执行人统计
					sql.append("\n 	left join (");
					sql.append("\n 		select count(taskex.executor) exnum, taskex.taskid, taskex.executor");
					sql.append("\n 		from taskexecutor taskex");
					sql.append("\n 		where taskex.comid = ? and taskex.state in(0,1)");
					args.add(userInfo.getComId());
					sql.append("\n  	group by taskex.taskid, taskex.executor");
					//只统计正在办理的任务信息
					sql.append("\n  ) taskexB on handOver.taskId = taskexB.taskid ");
					sql.append("\n and handOver.userId= taskexB.executor");
				}

				sql.append("\n where handOver.taskId=a.id ");
				if(!StringUtils.isEmpty(containExecutor) && containExecutor.equals("1")){
					sql.append("\n and nvl(taskexB.exnum,0)=0");
				}
				if(null!=task.getState()) {
					sql.append("\n	and exists(");
					sql.append("\n	select taskEx.taskId from taskExecutor taskEx where handOver.taskId=taskEx.taskId");
					if (task.getState().equals(1)) {
						sql.append("\n	 and (taskEx.state=0 or taskEx.state=1)");
					}else{
						sql.append("\n	 and taskEx.state=?");
						args.add(task.getState());
					}
					sql.append("\n	)");
				}
				sql.append(")\n");
			}
		}
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}
		sql.append("\n ) a where 1=1 \n");
		//是否查询逾期任务
		if(null!= task.getOverdue() && task.getOverdue()){
			sql.append("\n and a.id in ( SELECT TASKID from (SELECT sum(ceil(((To_date(nvl(a.ENDTIME,?) , 'yyyy-mm-dd hh24-mi-ss') ");
			sql.append("\n - To_date(a.RECORDCREATETIME , 'yyyy-mm-dd hh24-mi-ss'))) * 24 * 60)) executeTime,a.TASKID,a.EXECUTOR,b.EXPECTTIME ");
			sql.append("\n from TASKEXECUTETIME a LEFT JOIN TASKEXECUTOR b on b.taskId = a.taskId and b.EXECUTOR = a.EXECUTOR	");
			sql.append("\n GROUP BY a.TASKID,a.EXECUTOR,b.EXPECTTIME) where (nvl(EXPECTTIME,0)*60-nvl(executeTime,0)) <0	");
			sql.append("\n )");
			args.add(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		}
		this.addSqlWhere(task.getAttentionState(), sql, args, " and a.attentionState=?");
		//排序规则
		String orderBy = "";
		if("grade".equals(task.getOrderBy())){
			//按紧急程度排序
			orderBy = " a.state,grade desc, a.taskLimiteDate ";
		}else if("owner".equals(task.getOrderBy())){
			//按责任人排序
			orderBy = " a.owner,grade desc,a.id desc";
		}else if("executor".equals(task.getOrderBy())){
			//按执行人排序
			orderBy = " a.id desc";
		}else if("crTimeNewest".equals(task.getOrderBy())){
			//按任务创建时间(最新)排序
			orderBy = " a.recordcreatetime desc";
		}else if("crTimeOldest".equals(task.getOrderBy())){
			//按任务创建时间(最早)排序
			orderBy = " a.recordcreatetime";
		}else if("limitTimeNewest".equals(task.getOrderBy())){
			//按任务到期时间(最新)排序
			orderBy = " a.state ,a.taskLimiteDate desc,a.id desc ";
		}else if("limitTimeOldest".equals(task.getOrderBy())){
			//按任务到期时间(最早)排序
			orderBy = " a.state , a.taskLimiteDate,a.id desc";
		}else if("readState".equals(task.getOrderBy())){
			//按未读排序
			orderBy = " a.isread asc,a.state,a.recordcreatetime desc";
		}else{
			//默认排序规则
			orderBy = " a.state,a.recordcreatetime desc";
		}
		
		return this.pagedQuery(sql.toString(), orderBy, args.toArray(), Task.class);
	}
	/**
	 * 获取督察任务集合(所有)
	 * @param task
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskForExecutor(Integer taskId,Task task,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select exUser.executor,g.username as executorName,to_char((nvl(exUser.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') remainingTime, ");
		sql.append("\n case when exUser.handTimeLimit is null then a.dealtimelimit else exUser.handTimeLimit end dealTimeLimit,");
		sql.append("\n case when exUser.taskProgress is null then a.taskprogress else exUser.taskProgress end taskProgress,exUser.curStartTime,exUser.state executeState,");
		//逾期等级显示
		sql.append(" case when a.state=4 or a.state=3 then ? \n");
		args.add(OverDueLevelEnum.LEVELGRAY.getValue());
		sql.append(" when to_char((nvl(exUser.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') >24 then ? \n");
		args.add(OverDueLevelEnum.LEVELGREEN.getValue());
		sql.append(" when to_char((nvl(exUser.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') <24 and to_char((nvl(exUser.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') >=0 then ?\n");
		args.add(OverDueLevelEnum.LEVELYELLOW.getValue());
		sql.append(" else ?  end overDueLevel \n");
		args.add(OverDueLevelEnum.LEVELRED.getValue());
		sql.append("\n from task a ");
		sql.append("\n left join taskExecutor exUser on exUser.comid = a.comid and a.id = exUser.taskid");
		//任务执行时间
		sql.append("\n left join  \n");
		sql.append("\n( ");
		sql.append("\n   	SELECT sum(ceil(((To_date(nvl(a.ENDTIME,?) , 'yyyy-mm-dd hh24-mi-ss')  ");
		sql.append("\n   		- To_date(a.RECORDCREATETIME , 'yyyy-mm-dd hh24-mi-ss'))) * 24 * 60)) executeTime, ");
		sql.append("\n		a.TASKID,a.EXECUTOR  ");
		sql.append("\n		from TASKEXECUTETIME a GROUP BY a.TASKID,a.EXECUTOR ");
		sql.append("\n)exeTime on exeTime.taskId=a.id and exeTime.EXECUTOR=exUser.EXECUTOR  ");
		args.add(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		//任务执行人
		sql.append("\n left join userinfo g on exUser.executor = g.id \n");
		sql.append("\n where 1=1 and a.id=?");
		args.add(taskId);
		sql.append("\n order by exUser.id desc");
		return this.listQuery(sql.toString(), args.toArray(), Task.class);
	}
	/**
	 * 获取督察任务集合(所有)
	 * @param task
	 * @param depIds 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listPagedTaskForSupView(Task task,Set<Integer> depIds,List<Integer> subUserIds, 
			UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		
		//任务执行人查询（包括正在执行的）
		List<UserInfo> listExecutor = task.getListExecutor();
		List<Integer> executorIds = new ArrayList<Integer>();
		
		//任务办理时间段查询
		String operatStartDate = task.getOperatStartDate();
		String operatEndDate = task.getOperatEndDate();
		
		sql.append("select a.* ");
		sql.append(" from ( \n");
		//结果集最外取别名
		sql.append("\n 		select a.id,a.comid,a.taskname,a.owner,a.recordcreatetime,a.state,a.busId,a.busType,");
		sql.append("\n 		a.grade,c.username as ownerName,");
		sql.append("\n 		case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.itemName ");
		sql.append("\n 		when a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' then spFlow.flowName ");
		sql.append("\n 		when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.customername end busName, ");
		sql.append("\n 		a.taskprogress totalProgress,a.dealtimelimit taskLimiteDate ");
		//查询任务
		sql.append("\n 		from task a ");
		
		//关联项目
		sql.append("\n 		left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0");
		//关联的客户
		sql.append("\n 		left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 ");
		//关联的审批
		sql.append("\n 		left join spflowinstance spflow on a.comid=spflow.comid and a.busid=spflow.id and a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"'");
				
		//任务负责人
		sql.append("\n 		left join userinfo c on a.owner = c.id \n");
		sql.append("\n 		left join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		
		
		//任务没有被删除
		sql.append("\n 		where a.delstate=0 and  a.comid = ?");
		args.add(userInfo.getComId());
		
		if((null!=listExecutor && !listExecutor.isEmpty())
				|| !CommonUtil.isNull(depIds)
				|| StringUtils.isNotEmpty(operatStartDate)
				|| StringUtils.isNotEmpty(operatEndDate)){
			
			if(null!=listExecutor && !listExecutor.isEmpty()){//有办理人员
				for (UserInfo executor : listExecutor) {
					executorIds.add(executor.getId());
				}
			}
			sql.append("\n 	and exists(");
			sql.append("\n 		select taskh.taskId from taskhandover taskh ");
			sql.append("\n 		left join userOrganic uorg on taskh.touser= uorg.userId and taskh.comId=uorg.comId  ");
			sql.append("\n 		where taskh.comid = ? and taskh.taskId=a.id");
			args.add(userInfo.getComId());
			this.addSqlWhereIn(executorIds.toArray(new Integer[executorIds.size()]) , sql, args, "\n and taskh.toUser in ?");
			this.addSqlWhereIn(depIds.toArray(new Integer[depIds.size()]), sql, args, "\n and uorg.depId in ? ");
			
			//有开始时间
			if(StringUtils.isNotEmpty(operatStartDate)){
				sql.append("\n 	and (substr(taskh.recordcreatetime,0,10)>=? or substr(nvl(taskh.endTime,?),0,10)>=? )");
				args.add(operatStartDate);
				args.add(nowDateStr);
				args.add(operatStartDate);
			}
			//有结束时间
			if(StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 	and (substr(taskh.recordcreatetime,0,10)<=? or substr(nvl(taskh.endTime,?),0,10)<=? )");
				args.add(operatEndDate);
				args.add(nowDateStr);
				args.add(operatEndDate);
				
			}
			sql.append("\n 	group by taskh.comId,taskh.taskId");
			sql.append("\n 	) ");
		}
		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("\n and (\n");
			//负责人权限验证
			this.addSqlWhereIn(subUserIds.toArray(new Integer[subUserIds.size()]), sql, args, "\n a.owner in ? ");
			//执行人上级权限验证
			sql.append("\n or a.id in ( ");
			sql.append("\n  	select b.id from taskExecutor b where a.id = b.taskid \n");
			this.addSqlWhereIn(subUserIds.toArray(new Integer[subUserIds.size()]), sql, args, "\n and b.executor in ? ");
			sql.append("\n 		union all \n");
			//参与人上级权限验证
			sql.append("  	select b.id from tasksharer b where a.id = b.taskid  \n");
			this.addSqlWhereIn(subUserIds.toArray(new Integer[subUserIds.size()]), sql, args, "\n and b.sharerid in ? ");
			sql.append("\n ) ");
			sql.append("\n ) \n");
		}
		
		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		//负责人的主键集合
		if(null != task.getListOwner() && !task.getListOwner().isEmpty()){
			List<Integer> ownerIds = new ArrayList<Integer>();
			for(UserInfo owner : task.getListOwner()){
				ownerIds.add(owner.getId());
			}
			this.addSqlWhereIn(ownerIds.toArray(), sql, args, "\n and a.owner in ?");
		}
		
		//有查询关联模块信息
		String relateModType = task.getRelateModType();
		if(!StringUtils.isEmpty(relateModType)){
			List<RelateModeVo> modeVos = task.getListRelateModes();
			List<Integer> busIds = new ArrayList<Integer>();
			if(null!=modeVos){
				for (RelateModeVo relateModeVo : modeVos) {
					busIds.add(relateModeVo.getBusId());
				}
				if(ConstantInterface.TYPE_ITEM.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and item.id in ?");
				}else if(ConstantInterface.TYPE_CRM.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and crm.id in ?");
				}
			}
		}
		
		//状态这个筛选条件比较复杂。当只有状态或者有与其无关联条件时这个状态只针对任务本身。
		//但是当有经办人或者执行人与其一起作为筛选条件时，则协同任务还涉及到经办人对任务的状态。
		if(null!=task.getState()){
			switch (task.getState()){
				case 1:
					//进行中
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
					break;
				case 3:
					//挂起，暂停
					this.addSqlWhere(task.getState(), sql, args, "\n and a.state=?");
					break;
				case 4:
					//已完成但不关联经办人并且也不关联执行人。
					this.addSqlWhere(task.getState(), sql, args, "\n and a.state=?");
					break;
				default:
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
					break;
			}
		}
		
		
		
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}
		sql.append("\n ) a where 1=1 \n");
		
		//排序规则
		String orderBy = "";
		if("grade".equals(task.getOrderBy())){
			//按紧急程度排序
			orderBy = " a.state,grade desc, a.taskLimiteDate ";
		}else if("owner".equals(task.getOrderBy())){
			//按责任人排序
			orderBy = " a.owner,grade desc,a.id desc";
		}else if("executor".equals(task.getOrderBy())){
			//按执行人排序
			orderBy = " a.id desc";
		}else if("crTimeNewest".equals(task.getOrderBy())){
			//按任务创建时间(最新)排序
			orderBy = " a.recordcreatetime desc";
		}else if("crTimeOldest".equals(task.getOrderBy())){
			//按任务创建时间(最早)排序
			orderBy = " a.recordcreatetime";
		}else if("limitTimeNewest".equals(task.getOrderBy())){
			//按任务到期时间(最新)排序
			orderBy = " a.state ,a.taskLimiteDate desc,a.id desc ";
		}else if("limitTimeOldest".equals(task.getOrderBy())){
			//按任务到期时间(最早)排序
			orderBy = " a.state , a.taskLimiteDate,a.id desc";
		}else{
			//默认排序规则
			orderBy = " a.state,a.recordcreatetime desc";
		}
		return this.pagedQuery(sql.toString(), orderBy, args.toArray(), Task.class);
	}
	
	/**
	 * 查询任务的其他信息
	 * @param taskId
	 * @param task
	 * @param depIds
	 * @param subUserIds
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskExecutorForSupView(Integer taskId, Task task, Set<Integer> depIds, List<Integer> subUserIds,
			UserInfo userInfo, boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		
		//任务执行人查询（包括正在执行的）
		List<Integer> executorIds = new ArrayList<Integer>();
		
		//任务办理时间段查询
		String operatStartDate = task.getOperatStartDate();
		String operatEndDate = task.getOperatEndDate();
		
		sql.append("\n select a.*, ");
		////逾期等级显示
		sql.append("\n case when a.dealTimeLimit is null then '"+OverDueLevelEnum.LEVELGREEN.getValue() +"'");
		sql.append("\n else case when a.dealTimeLimit >= substr(a.Endtime,0,10) then '"+OverDueLevelEnum.LEVELGREEN.getValue() +"'");
		sql.append("\n else '"+OverDueLevelEnum.LEVELRED.getValue() +"' end ");
		sql.append("\n end overDueLevel");
		
		sql.append("\n from (");
		sql.append("\n select exUser.id exUserId,exUser.toUser executor,g.username as executorName, ");
		sql.append("\n exUser.fromUser,f.username as fromUserName, ");
		sql.append("\n case when exUser.handTimeLimit is null then a.dealTimeLimit else exUser.handTimeLimit end dealTimeLimit, ");
		
		sql.append("\n exUser.recordCreateTime operatStartDate,exUser.endTime operatEndDate,");
		sql.append("\n case when exUser.Endtime is not null then exUser.Endtime else '"+nowDateStr+"' end  Endtime");
		sql.append("\n from task a");
		sql.append("\n left join taskhandover exUser on exUser.comid = a.comid and a.id = exUser.taskid");
		//任务执行人
		sql.append("\n left join userOrganic uorg on exUser.toUser = uorg.userId and a.comId=uorg.comId \n");
		sql.append("\n left join userinfo g on exUser.toUser = g.id \n");
		sql.append("\n left join userinfo f on exUser.fromUser = f.id \n");
		sql.append("\n where 1=1 and a.id =?");
		args.add(taskId);
		sql.append("\n and exUser.actHandleState=1");
		// 执行统计信息
		if(null!=executorIds && !executorIds.isEmpty()){
			this.addSqlWhereIn(executorIds.toArray(new Integer[executorIds.size()]) , sql, args, "\n and exUser.toUser in ?");
		}
		//负责人的主键集合
		if(null != task.getListOwner() && !task.getListOwner().isEmpty()){
			List<Integer> ownerIds = new ArrayList<Integer>();
			for(UserInfo owner : task.getListOwner()){
				ownerIds.add(owner.getId());
			}
			this.addSqlWhereIn(ownerIds.toArray(), sql, args, "\n and a.owner in ?");
		}
		this.addSqlWhereIn(depIds.toArray(new Integer[depIds.size()]), sql, args, "\n and uorg.depid in ? ");
		
		//有开始时间
		if(StringUtils.isNotEmpty(operatStartDate)){
			sql.append("\n 	and (substr(exUser.recordcreatetime,0,10)>=? or substr(nvl(exUser.endTime,?),0,10)>=? )");
			args.add(operatStartDate);
			args.add(nowDateStr);
			args.add(operatStartDate);
		}
		//有结束时间
		if(StringUtils.isNotEmpty(operatEndDate)){
			sql.append("\n 	and (substr(exUser.recordcreatetime,0,10)<=? or substr(nvl(exUser.endTime,?),0,10)<=? )");
			args.add(operatEndDate);
			args.add(nowDateStr);
			args.add(operatEndDate);
			
		}
		sql.append("\n )a order by a.exUserId desc");
		return this.listQuery(sql.toString(), args.toArray(), Task.class);
	}
	/**
	 * 获取个人待办任务集合
	 * @param task
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> taskToDoList(Task task,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* ,");
		
		//逾期等级显示
		sql.append(" case when a.state=4 or a.state=3 then ? \n");
		args.add(OverDueLevelEnum.LEVELGRAY.getValue());
		sql.append(" when remainingTime >24 then ? \n");
		args.add(OverDueLevelEnum.LEVELGREEN.getValue());
		sql.append(" when remainingTime <24 and remainingTime >=0 then ?\n");
		args.add(OverDueLevelEnum.LEVELYELLOW.getValue());
		sql.append(" else ?  end overDueLevel \n");
		args.add(OverDueLevelEnum.LEVELRED.getValue());
		
		sql.append("from ( \n");
		//任务团队号，任务主键，任务名称，任务负责人，任务发布时间，任务状态，关联模块主键，关联模块类型
		sql.append("select a.comid,a.id,a.taskname,a.owner,a.recordcreatetime,a.state,a.busId,a.bustype,\n");
		//任务执行人主键，任务执行状态，任务执行进度
		sql.append("\n exUser.executor,exUser.state executeState,exUser.taskProgress executeProgress,");
		//是否为任务执行人 1 是 0 不是,执行人的接收时间
		sql.append("\n case when exUser.id >0 then 1 else 0 end isExecute,exUser.recordCreateTime as handTime, ");
		//任务总进度，任务紧急度，任务推送人员
		sql.append("\n a.taskprogress,a.grade,exUser.pushUser pushUserId,\n");
		//任务推送人姓名，任务推送人性别，任务推送人头像uuid,任务推送人头像文件名称
		sql.append("c.username as fromUserName,c.gender as fromUserGender,d.uuid as fromUserUuid,d.filename as fromUserFileName,\n");
		//任务的办理时限，若是没有办理时限，则取得任务的办理时限
		sql.append("case when exUser.handtimelimit is null then a.dealtimelimit else exUser.handtimelimit end dealtimelimit ,");
		//消息是否未读
		sql.append("case when toDo.todonum>0 then 0 else 1 end as isread,\n");
		//任务是否被关注
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,  \n");

		//关联模块的名称
		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.itemName \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' then spflow.flowName \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_DEMAND_PROCESS+"' then  dp.serialNum \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.customername end busName, \n");
		
		//关联模块是否被删除
		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.delState \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.delState end busDelState, \n");
		//关联模块的状态
		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.state else 0 end busState, \n");
		
		//任务执行时间
		sql.append("to_char((nvl(exUser.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') remainingTime,exUser.curStartTime \n");
		//任务主表
		sql.append("from task a \n");
		//办理人员（需要认领或是需要办理的）
		sql.append("left join taskExecutor exUser on a.comid=exUser.comid and a.id=exUser.taskid and exUser.executor=? \n");
		args.add(userInfo.getId());
		//关联的项目信息
		sql.append("left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0\n");
		//关联的客户信息
		sql.append("left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 \n");
		//关联
		sql.append("left join spflowinstance spflow on a.comid=spflow.comid and a.busid=spflow.id and a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' \n");
		//关联
		sql.append("left join demandprocess dp on a.comid=dp.comid and a.busid=dp.id and a.bustype='"+ConstantInterface.TYPE_DEMAND_PROCESS+"' \n");
		
		//任务的执行人
		sql.append("left join userinfo c on exUser.pushUser = c.id \n");
		sql.append("left join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		//关注信息
		sql.append("left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());

		//待办信息
		sql.append("left join  \n");
		sql.append("\n( ");
		sql.append("\n select toDo.Busid,count(toDo.Busid) todonum from todayworks toDo \n");
		sql.append("\n where toDo.isclock=0 and toDo.readState=0 and toDo.Bustype=?  and toDo.Userid=? ");
		sql.append("\n group by toDo.Busid");
		sql.append("\n) toDo on a.id=toDo.busid ");
		args.add(ConstantInterface.TYPE_TASK);
		args.add(userInfo.getId());
		
		//任务执行时间
		sql.append("\n left join  \n");
		sql.append("\n( ");
		sql.append("\n   	SELECT sum(ceil(((To_date(nvl(a.ENDTIME,?) , 'yyyy-mm-dd hh24-mi-ss')  ");
		sql.append("\n   		- To_date(a.RECORDCREATETIME , 'yyyy-mm-dd hh24-mi-ss'))) * 24 * 60)) executeTime, ");
		sql.append("\n		a.TASKID,a.EXECUTOR  ");
		sql.append("\n		from TASKEXECUTETIME a GROUP BY a.TASKID,a.EXECUTOR ");
		sql.append("\n)exeTime on exeTime.taskId=a.id and exeTime.EXECUTOR=exUser.EXECUTOR  ");
		args.add(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		//任务没有被删除，指定团队，指定的执行人员
		sql.append("where a.delstate=0 and a.comid = ?  and exUser.executor=? \n");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		//待办任务包括认领的和办理的
		this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in  ?");
		//版本控制
		this.addSqlWhere(task.getVersion(), sql, args, "\n and a.version=?");

		//查询关联的模块信息
		String relateModType = task.getRelateModType();
		if(!StringUtils.isEmpty(relateModType)){
			List<RelateModeVo> modeVos = task.getListRelateModes();
			List<Integer> busIds = new ArrayList<Integer>();
			if(null!=modeVos){
				for (RelateModeVo relateModeVo : modeVos) {
					busIds.add(relateModeVo.getBusId());
				}

				if(ConstantInterface.TYPE_ITEM.equals(relateModType)){
					//关联的项目
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and item.id in ?");
				}else if(ConstantInterface.TYPE_CRM.equals(relateModType)){
					//关联的客户
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and crm.id in ?");
				}
			}
		}
		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(exUser.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(exUser.recordcreatetime,0,10)<=?");

		//任务负责人筛选
		if(null!=task.getOwner() && task.getOwner()!=0){
			sql.append("and a.owner=? \n");
			args.add(task.getOwner());
		}

		//任务推送人筛选
		List<UserInfo> pushUsers = task.getListFromUser();
		if(null!=pushUsers && !pushUsers.isEmpty()){
			List<Integer> pushUserIds = new ArrayList<Integer>();
			for(UserInfo pushUser : pushUsers){
				pushUserIds.add(pushUser.getId());
			}
			this.addSqlWhereIn(pushUserIds.toArray(), sql, args, "\n and exUser.pushUser in ?");
		}
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}
		//任务紧急度筛选
		this.addSqlWhere(task.getGrade(), sql, args, "\n and grade=?");

		sql.append(") a where 1=1");
		//统计图穿透筛选
		if("over".equals(task.getCountType())){
			sql.append("and a.remainingTime < 0 \n");
		}else if("today".equals(task.getCountType())){
			sql.append("and a.remainingTime <8 and a.remainingTime >=0 \n");
		}else if("near".equals(task.getCountType())){
			sql.append("and a.remainingTime <24 and a.remainingTime >=8 \n");
		}else if("future".equals(task.getCountType())){
			sql.append("and a.remainingTime >=24 \n");
		}else if("unLimit".equals(task.getCountType())){
			sql.append("and a.remainingTime is null \n");
		}
		//是否查询逾期任务
		if(null != task.getOverdue() && task.getOverdue()){
			sql.append("\n and a.remainingTime < 0");
		}

		//排序规则
		String orderBy = "";
		if("grade".equals(task.getOrderBy())){
			//按紧急程度排序
			orderBy = " a.state,grade desc, a.dealtimelimit ";
		}else if("owner".equals(task.getOrderBy())){
			//按责任人排序
			orderBy = " a.owner,grade desc,a.id desc";
		}else if("executor".equals(task.getOrderBy())){
			//按执行人排序
			orderBy = " a.executor,grade desc,a.id desc";
		}else if("crTimeNewest".equals(task.getOrderBy())){
			//按任务创建时间(最新)排序
			orderBy = " a.recordcreatetime desc";
		}else if("crTimeOldest".equals(task.getOrderBy())){
			//按任务创建时间(最早)排序
			orderBy = " a.recordcreatetime";
		}else if("limitTimeNewest".equals(task.getOrderBy())){
			//按任务到期时间(最新)排序
			orderBy = " a.dealtimelimit desc,a.id desc ";
		}else if("limitTimeOldest".equals(task.getOrderBy())){
			//按任务到期时间(最早)排序
			orderBy = "a.dealtimelimit,a.id desc";
		}else if("readState".equals(task.getOrderBy())){
			//按未读排序
			orderBy = " isread asc,a.state,a.recordcreatetime desc";
		}else if("handTimeOldest".equals(task.getOrderBy())){
			//按任务接收时间(升序)排序
			orderBy = " a.handTime";
		}else if("handTimeNewest".equals(task.getOrderBy())){
			//按任务接收时间(降序)排序
			orderBy = " a.handTime desc";
		}else{
			//默认排序规则-接收时间
			orderBy = " a.state,a.handTime desc";
		}
		return this.pagedQuery(sql.toString(),orderBy, args.toArray(), Task.class);
	}
	/**
	 * 分页查询负责的任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listPageChargeTask(Task task,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.* ");
		sql.append(" from ( \n");
		//任务名称，任务发布时间，任务状态，关联模块主键，关联模块类型
		sql.append("	select a.owner,a.comid,a.id,a.taskname,a.recordcreatetime,a.state,a.busId,a.busType,\n");
		//任务总体进度，任务总体时限
		sql.append("	a.taskprogress totalProgress,a.grade,a.dealtimelimit taskLimiteDate,\n");
		//是否未读
		sql.append("	case when toDo.todonum>0 then 0 else 1 end as isread,\n");
		//关注状态
		sql.append("	case when atten.id is null then 0 else 1 end as attentionState,\n");
		//关联模块的名称
		sql.append("	case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.itemName \n");
		sql.append("	when a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' then spflow.flowName \n");
		sql.append("	when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.customername  \n");
		sql.append("	when a.bustype='"+ConstantInterface.TYPE_DEMAND_PROCESS+"' then dem.serialNum end busName, \n");
		//关联模块是否删除
		sql.append("	case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.delState \n");
		sql.append("	when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.delState end busDelState, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.state else 0 end busState \n");
		sql.append("from task a \n");
		sql.append("left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0\n");
		sql.append("left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 \n");
		sql.append("left join spflowinstance spflow on a.comid=spflow.comid and a.busid=spflow.id and a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' \n");
		sql.append("left join DEMANDPROCESS dem on a.comid=dem.comid and a.busid=dem.id and a.bustype='"+ConstantInterface.TYPE_DEMAND_PROCESS+"' \n");
		sql.append("left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());

		sql.append("left join  \n");
		sql.append("\n( ");
		sql.append("\n select toDo.Busid,count(toDo.Busid) todonum from todayworks toDo \n");
		sql.append("\n where toDo.isclock=0 and toDo.readState=0 and toDo.Bustype=?  and toDo.Userid=? ");
		sql.append("\n group by toDo.Busid");
		sql.append("\n) toDo on a.id=toDo.busid ");
		args.add(ConstantInterface.TYPE_TASK);
		args.add(userInfo.getId());
		sql.append("\n where a.delstate=0 and a.comid = ? and a.state <> -1 \n");
		args.add(userInfo.getComId());
		
		//版本控制
		this.addSqlWhere(task.getVersion(), sql, args, "\n and a.version=?");

		String relateModType = task.getRelateModType();
		if(!StringUtils.isEmpty(relateModType)){
			List<RelateModeVo> modeVos = task.getListRelateModes();
			List<Integer> busIds = new ArrayList<Integer>();
			if(null!=modeVos){
				for (RelateModeVo relateModeVo : modeVos) {
					busIds.add(relateModeVo.getBusId());
				}
				if(ConstantInterface.TYPE_ITEM.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and item.id in ?");
				}else if(ConstantInterface.TYPE_CRM.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and crm.id in ?");
				}else if(ConstantInterface.TYPE_FLOW_SP.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and spflow.id in ?");
				}
			}
		}

		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//任务状态筛选
//		if(null!=task.getState() && task.getState()!=0){
//			sql.append("and a.state=? \n");
//			args.add(task.getState());
//		}
		if(null!=task.getState()){
			switch (task.getState()){
				case 1:
					//进行中
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
					break;
				case 3:
					//挂起，暂停
					this.addSqlWhere(task.getState(), sql, args, "\n and a.state=?");
					break;
				case 4:
					//已完成但不关联经办人并且也不关联执行人。
					if(CommonUtil.isNull(task.getExecutor()) && CommonUtil.isNull(task.getListOperator())){
						this.addSqlWhere(task.getState(), sql, args, "\n and a.state=?");
					}
					break;
				default:
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
					break;
			}
		}

		//任务紧急度筛选
		if(null!=task.getGrade() && !"".equals(task.getGrade())){
			sql.append("and grade=? \n");
			args.add(task.getGrade());
		}

		List<Department> listExecuteDep = task.getListExecuteDep();
		//执行人所在部门
		if(!CommonUtil.isNull(listExecuteDep)){
			List<Integer> depIds = new ArrayList<>();
			for (Department department : listExecuteDep) {
				depIds.add(department.getId());
			}
			sql.append("\n and  exists  ");
			sql.append("\n (");
			sql.append("\n  	select u.id from  userInfo u");
			sql.append("\n  	inner join userOrganic uo on u.id = uo.userId and uo.comId = ? \n");
			args.add(userInfo.getComId());
			sql.append("\n  	inner join deptree  executeDep on uo.depId = executeDep.Id and  uo.comId = executeDep.comId \n");
			sql.append("\n  	where uo.comId = ?  ");
			args.add(userInfo.getComId());
			this.addSqlWhereIn(depIds.toArray(), sql, args, "\n and ( executeDep.Parentid in ?");
			this.addSqlWhereIn(depIds.toArray(), sql, args, "\n or executeDep.depId in ? )");
			sql.append("\n ) and a.state in (0,1)");
		}

		//任务负责人筛选
		List<UserInfo> owners = task.getListOwner();
		if(null!=owners && !owners.isEmpty()){
			List<Integer> ownerIds = new ArrayList<Integer>();
			for (UserInfo owner : owners) {
				ownerIds.add(owner.getId());
			}
			this.addSqlWhereIn(ownerIds.toArray(), sql, args, "\n and a.owner in ?");
		}
		//经办时间
		String operatStartDate = task.getOperatStartDate(); 
		String operatEndDate = task.getOperatEndDate(); 
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		//经办人筛选
		if(!CommonUtil.isNull(task.getListOperator())){
			sql.append("\n and exists (\n");
			sql.append("\n select handOver.id from  taskHandOver handOver  where handOver.comId=? ");
			args.add(userInfo.getComId());
			sql.append("\n and (");
			List<Integer> operatorIds = new ArrayList<Integer>();
			for (UserInfo operator : task.getListOperator()) {
				operatorIds.add(operator.getId());
			}
			this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n handOver.fromUser in ?");
			this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n or handOver.toUser in ?");
			sql.append("\n )");
			
			if(StringUtils.isNotEmpty(operatStartDate)
					|| StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 		  	and handOver.endTime is not null  ");
			}
			//查询创建时间段
			this.addSqlWhere(operatStartDate, sql, args, " and substr(handOver.recordcreatetime,0,10)>=?");
			//查询创建时间段
			if(StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 		  	and substr(nvl(handOver.endTime,?),0,10)<=?  ");
				args.add(nowDate);
				args.add(operatEndDate);
			}
			
			sql.append("\n and handOver.taskId=a.id");
			if(null!=task.getState()) {
				sql.append("\n	and exists(");
				sql.append("\n	select taskEx.taskId from taskExecutor taskEx where handOver.taskId=taskEx.taskId");
				if (task.getState().equals(1)) {
					sql.append("\n	 and (taskEx.state=0 or taskEx.state=1)");
				}else{
					sql.append("\n	 and taskEx.state=?");
					args.add(task.getState());
				}
				this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n  and taskEx.EXECUTOR in ?");
				sql.append("\n	)");
			}

			sql.append(")\n");
		}else{
			if(StringUtils.isNotEmpty(operatStartDate) 
					|| StringUtils.isNotEmpty(operatEndDate) ){
				
				sql.append("\n and exists (\n");
				sql.append("\n select handOver.id from  taskHandOver handOver  where handOver.comId=? ");
				args.add(userInfo.getComId());
				sql.append("\n 		  	and handOver.endTime is not null  ");
				//查询创建时间段
				this.addSqlWhere(operatStartDate, sql, args, " and substr(handOver.recordcreatetime,0,10)>=?");
				//查询创建时间段
				if(StringUtils.isNotEmpty(operatEndDate)){
					sql.append("\n 		  	and substr(nvl(handOver.endTime,?),0,10)<=?  ");
					args.add(nowDate);
					args.add(operatEndDate);
				}
				
				sql.append("\n and handOver.taskId=a.id");
				if(null!=task.getState()) {
					sql.append("\n	and exists(");
					sql.append("\n	select taskEx.taskId from taskExecutor taskEx where handOver.taskId=taskEx.taskId");
					if (task.getState().equals(1)) {
						sql.append("\n	 and (taskEx.state=0 or taskEx.state=1)");
					}else{
						sql.append("\n	 and taskEx.state=?");
						args.add(task.getState());
					}
					sql.append("\n	)");
				}
				sql.append(")\n");
			}
		}
		
		
		
		
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}
		//执行人类型
		String execuType =  task.getExecuType();
		if(null!=execuType && !"".equals(execuType)){
			if("0".equals(execuType)){//查询自己的
				sql.append("\n and exists(");
				sql.append("\n select taskEx.id from taskexecutor taskEx");
				sql.append("\n where taskEx.executor=? and taskEx.taskid=a.id ");
				sql.append("\n )");
				args.add(userInfo.getId());
				this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in  ? ");
			}else if("1".equals(execuType)){//查询下属的

				sql.append("\n and exists(");
				sql.append("\n select taskEx.id from taskexecutor taskEx");
				sql.append("\n where exists(select id from myLeaders where creator = reports.ReporterId and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
				sql.append("\n )");
				this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in  ? ");
			}
		}
		//执行人选择
		List<UserInfo> executors = task.getListExecutor();
		if(!CommonUtil.isNull(executors)){
			sql.append("\n and a.state in (0,1) and  exists(  ");
			List<Integer> executorIds = new ArrayList<Integer>();
			for(UserInfo executor : executors){
				executorIds.add(executor.getId());
			}
			sql.append("\n select a.id from taskExecutor taskEx   ");
			sql.append("\n where taskEx.taskId=a.id  ");
			this.addSqlWhereIn(executorIds.toArray(), sql, args, "\n and taskEx.executor in ?");
			//添加筛选执行人同时筛选状态的控制
			if(null!=task.getState()){
				if(task.getState().equals(1)){
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and taskEx.state in ?");
				}else{
					this.addSqlWhere(task.getState(), sql, args, "\n and taskEx.state=?");
				}
			}
			sql.append("	 ) ");
		}
		sql.append(")a where 1=1");
		this.addSqlWhere(task.getAttentionState(), sql, args, " and a.attentionState=?");
		//是否查询逾期任务
		if(null!= task.getOverdue() && task.getOverdue()){
			sql.append("\n and a.id in ( SELECT TASKID from (SELECT sum(ceil(((To_date(nvl(a.ENDTIME,?) , 'yyyy-mm-dd hh24-mi-ss') ");
			sql.append("\n - To_date(a.RECORDCREATETIME , 'yyyy-mm-dd hh24-mi-ss'))) * 24 * 60)) executeTime,a.TASKID,a.EXECUTOR,b.EXPECTTIME ");
			sql.append("\n from TASKEXECUTETIME a LEFT JOIN TASKEXECUTOR b on b.taskId = a.taskId and b.EXECUTOR = a.EXECUTOR	");
			sql.append("\n GROUP BY a.TASKID,a.EXECUTOR,b.EXPECTTIME) where (nvl(EXPECTTIME,0)*60-nvl(executeTime,0)) <0	");
			sql.append("\n )");
			args.add(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		}
		//排序规则
		String orderBy = "";
		if("grade".equals(task.getOrderBy())){
			//按紧急程度排序
			orderBy = " a.grade desc,a.id desc";
		}else if("owner".equals(task.getOrderBy())){
			//按紧急程度排序
			orderBy = " a.owner,a.id desc";
		}else if("executor".equals(task.getOrderBy())){
			//按执行人排序
			orderBy = " a.id desc";
		}else if("crTimeNewest".equals(task.getOrderBy())){
			//按任务创建时间(最新)排序
			orderBy = " a.recordcreatetime desc";
		}else if("crTimeOldest".equals(task.getOrderBy())){
			//按任务创建时间(最早)排序
			orderBy = " a.recordcreatetime";
		}else if("limitTimeNewest".equals(task.getOrderBy())){
			//按任务到期时间(最新)排序
			orderBy = " a.taskLimiteDate desc,a.id desc ";
		}else if("limitTimeOldest".equals(task.getOrderBy())){
			//按任务到期时间(最早)排序
			orderBy = " a.taskLimiteDate,a.id desc";
		}else if("readState".equals(task.getOrderBy())){
			//按未读排序
			orderBy = " isread asc,a.state,a.recordcreatetime desc";
		}else{
			//默认排序规则
			orderBy = " a.state,a.recordcreatetime desc";
		}


		//总数
		String countQueryString = "select  count(*) from ( "+sql+" ) a";
		Integer count = this.countQuery(countQueryString.toString(), args.toArray());
		PaginationContext.setTotalCount(count);

		StringBuffer pagedQueryString = new StringBuffer();
		pagedQueryString.append("\n select a.*,g.username as executorName,exUser.executor,to_char((nvl(exUser.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') remainingTime, ");
		pagedQueryString.append("\n g.gender as executorGender,h.uuid as executorUuid,h.filename as executorFileName,exUser.curStartTime,exUser.state executeState,");
		pagedQueryString.append("\n case when exUser.handTimeLimit is null then a.taskLimiteDate else exUser.handTimeLimit end dealTimeLimit,");
		pagedQueryString.append("\n case when exUser.taskProgress is null then a.totalProgress else exUser.taskProgress end taskProgress,exUser.recordCreateTime as handTime,");
		//逾期等级显示
		pagedQueryString.append(" case when a.state=4 or a.state=3 then 'gray' when to_char((nvl(exUser.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') >24 then 'green' \n");
		pagedQueryString.append(" when to_char((nvl(exUser.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') <24 and to_char((nvl(exUser.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') >=0 then 'yellow'\n");
		pagedQueryString.append(" else 'red'  end overDueLevel \n");
		pagedQueryString.append("\n from (");
		pagedQueryString.append("\n		select outer.*  from (");
		pagedQueryString.append("\n			select inner.*,rownum as rowno from( \n");
		pagedQueryString.append(sql).append(" order by" + orderBy);
		pagedQueryString.append("\n			) inner where rownum <=").append(PaginationContext.getOffset() + PaginationContext.getPageSize());
		pagedQueryString.append("\n		) outer where outer.rowno>").append(PaginationContext.getOffset());
		pagedQueryString.append("\n )a left join taskExecutor exUser on exUser.comid = a.comid and a.id = exUser.taskid");
		//任务执行时间
		pagedQueryString.append("\n left join  \n");
		pagedQueryString.append("\n( ");
		pagedQueryString.append("\n   	SELECT sum(ceil(((To_date(nvl(a.ENDTIME,?) , 'yyyy-mm-dd hh24-mi-ss')  ");
		pagedQueryString.append("\n   		- To_date(a.RECORDCREATETIME , 'yyyy-mm-dd hh24-mi-ss'))) * 24 * 60)) executeTime, ");
		pagedQueryString.append("\n		a.TASKID,a.EXECUTOR  ");
		pagedQueryString.append("\n		from TASKEXECUTETIME a GROUP BY a.TASKID,a.EXECUTOR ");
		pagedQueryString.append("\n)exeTime on exeTime.taskId=a.id and exeTime.EXECUTOR=exUser.EXECUTOR ");
		args.add(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		//任务执行人
		pagedQueryString.append("\n left join userinfo g on exUser.executor = g.id \n");
		pagedQueryString.append("\n left join userOrganic gg on g.id =gg.userId and exUser.comId=gg.comId\n");
		pagedQueryString.append("\n left join upfiles h on gg.mediumHeadPortrait = h.id \n");
		pagedQueryString.append("\n where 1=1 ");

		//按执行人排序
		if("executor".equals(task.getOrderBy())){
			pagedQueryString.append("\n order by exUser.id, a.rowno ");
		}else{
			pagedQueryString.append("\n order by a.rowno");

		}
		List<Task> list = this.listQuery(pagedQueryString.toString(), args.toArray(), Task.class);

		return list;
	}
	
	/********************************任务查询****************************************/
	/********************************mobile任务查询****************************************/
	/**
	 * 获取个人权限下的所有任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskOfAll(Task task,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select a.comid,a.id,a.taskname,a.owner,a.recordcreatetime,hand.handTimeLimit dealtimelimit,a.state,a.busId,a.busType,a.executor,\n");
//		sql.append("a.taskprogress,case when a.state=1 then a.grade else '0' end as grade,\n");
		sql.append("a.taskprogress,a.grade,\n");
		sql.append("c.username as ownerName,c.gender,d.uuid,d.filename,\n");

		sql.append("case when toDo.todonum>0 then 0 else 1 end as isread,\n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState,g.username as executorName,\n");
		sql.append("g.gender as executorGender,h.uuid as executorUuid,h.filename as executorFileName, \n");
		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.itemName \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.customername end busName, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.delState \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.delState end busDelState, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.state else 0 end busState \n");
		sql.append("from task a \n");
		sql.append("inner join taskhandover hand on a.comid=hand.comid and a.id=hand.taskid and hand.curstep=1  \n");

		sql.append("left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0\n");
		sql.append("left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 \n");
		sql.append("left join userinfo c on a.owner = c.id \n");
		sql.append("left join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("left join userinfo g on a.executor = g.id \n");
		sql.append("left join userOrganic gg on g.id =gg.userId and a.comId=gg.comId\n");
		sql.append("left join upfiles h on gg.mediumHeadPortrait = h.id \n");
		sql.append("left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());

		sql.append("left join  \n");
		sql.append("\n( ");
		sql.append("\n select toDo.Busid,count(toDo.Busid) todonum from todayworks toDo \n");
		sql.append("\n where toDo.isclock=0 and toDo.readState=0 and toDo.Bustype=?  and toDo.Userid=? ");
		sql.append("\n group by toDo.Busid");
		sql.append("\n) toDo on a.id=toDo.busid ");
		args.add(ConstantInterface.TYPE_TASK);
		args.add(userInfo.getId());

		sql.append("\n where a.delstate=0 and  a.comid = ? and a.state <> -1 \n");
		args.add(userInfo.getComId());

		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append("a.executor =? or a.owner=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.id from tasksharer b where a.id = b.taskid and \n");
			sql.append("  	(b.sharerid =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.sharerid and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//执行人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = a.executor and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//负责人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//被@的人

			sql.append(") \n");
		}
		//任务状态筛选
		if(null!=task.getState() && task.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(task.getState());
		}
		//任务执行人筛选
		if(null!=task.getExecutor() && task.getExecutor()!=0){
			sql.append("and a.executor=? and a.state=1 \n");
			args.add(task.getExecutor());
		}
		//任务负责人筛选
		if(null!=task.getOwner() && task.getOwner()!=0){
			sql.append("and a.owner=? \n");
			args.add(task.getOwner());
		}
		//经办人筛选
		if(null!=task.getOperator() && task.getOperator()!=0){
			sql.append("and exists (\n");
			sql.append("	select id from  taskHandOver  where comId=? and (fromUser=? or toUser=?) and taskId=a.id");
			args.add(userInfo.getComId());
			args.add(task.getOperator());
			args.add(task.getOperator());
			sql.append(")\n");
		}
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}


		//执行人类型
		String execuType =  task.getExecuType();
		if(null!=execuType && !"".equals(execuType)){
			if("0".equals(execuType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.executor=? and a.state=1 \n");
			}else if("1".equals(execuType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.executor and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		sql.append("order by a.state,a.recordcreatetime desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Task.class);
	}
	/**
	 * 获取团队任务主键集合
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskOfAll(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id from task a where a.comid=?");
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(),Task.class);
	}
	/**
	 * 获取个人权限下的所有逾期任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listOverdueTaskOfAll(Task task,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("select a.comid,a.id,a.taskname,a.owner,a.recordcreatetime,hand.handTimeLimit dealtimelimit,a.state,a.busId,a.busType,a.executor,\n");
		sql.append("case when hand.handtimelimit is null then a.dealtimelimit else hand.handtimelimit end handtimelimit,\n");
//		sql.append("a.taskprogress,case when a.state=1 then a.grade else '0' end as grade,\n");
		sql.append("a.taskprogress,a.grade,\n");
		sql.append("c.username as ownerName,c.gender,d.uuid,d.filename,\n");

		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where a.comid = today.comid and a.id = today.busid \n");
		sql.append(" and today.bustype='"+ConstantInterface.TYPE_TASK+"' and today.userId=? and today.isclock=0\n");
		args.add(userInfo.getId());
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 1 else 0 end as isread,\n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState,g.username as executorName,\n");
		sql.append("g.gender as executorGender,h.uuid as executorUuid,h.filename as executorFileName,  \n");
		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.itemName \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.customername end busName, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.delState \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.delState end busDelState, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.state else 0 end busState \n");
		sql.append("from task a \n");
		sql.append("inner join taskhandover hand on a.comid=hand.comid and a.id=hand.taskid and hand.curstep=1  \n");
		sql.append("left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0\n");
		sql.append("left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 \n");
		sql.append("left join userinfo c on a.owner = c.id \n");
		sql.append("left join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("left join userinfo g on a.executor = g.id \n");
		sql.append("left join userOrganic gg on g.id =gg.userId and a.comId=gg.comId\n");
		sql.append("left join upfiles h on gg.mediumHeadPortrait = h.id \n");
		sql.append("left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comid = ? and a.state=1 \n");
		args.add(userInfo.getComId());


		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append("a.executor =? or a.owner=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.id from tasksharer b where a.id = b.taskid and \n");
			sql.append("  	(b.sharerid =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.sharerid and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//执行人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = a.executor and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//负责人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
		}
		//任务状态筛选
		if(null!=task.getState() && task.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(task.getState());
		}
		//任务执行人筛选
		if(null!=task.getExecutor() && task.getExecutor()!=0){
			sql.append("and a.executor=? and a.state=1 \n");
			args.add(task.getExecutor());
		}
		//任务负责人筛选
		if(null!=task.getOwner() && task.getOwner()!=0){
			sql.append("and a.owner=? \n");
			args.add(task.getOwner());
		}
		//经办人筛选
		if(null!=task.getOperator() && task.getOperator()!=0){
			sql.append("and exists (\n");
			sql.append("	select id from  taskHandOver  where comId=? and (fromUser=? or toUser=?) and taskId=a.id");
			args.add(userInfo.getComId());
			args.add(task.getOperator());
			args.add(task.getOperator());
			sql.append(")\n");
		}
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}
		//执行人类型
		String execuType =  task.getExecuType();
		if(null!=execuType && !"".equals(execuType)){
			if("0".equals(execuType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.executor=?");
			}else if("1".equals(execuType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.executor and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		sql.append(") a where 1=1");
		sql.append("and to_date(a.dealtimelimit,'yyyy-mm-dd') < trunc(SYSDATE)\n");
		sql.append("order by a.state,a.recordcreatetime desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Task.class);
	}
	/**
	 * 分页获取逾期任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listOverdueTask(Task task,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("select a.comid,a.id,a.taskname,a.owner,a.recordcreatetime,hand.handTimeLimit dealtimelimit,a.state,a.busId,a.busType,a.executor,\n");
//		sql.append("a.taskprogress,case when a.state=1 then a.grade else '0' end as grade,\n");
		sql.append("a.taskprogress,a.grade,\n");
		sql.append("case when hand.handtimelimit is null then a.dealtimelimit else hand.handtimelimit end handtimelimit,\n");
		sql.append("c.username as ownerName,c.gender,d.uuid,d.filename,\n");

		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where a.comid = today.comid and a.id = today.busid \n");
		sql.append(" and today.bustype='"+ConstantInterface.TYPE_TASK+"' and today.userId=? and today.isclock=0\n");
		args.add(userInfo.getId());
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 1 else 0 end as isread,\n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState,g.username as executorName,\n");
		sql.append("g.gender as executorGender,h.uuid as executorUuid,h.filename as executorFileName,  \n");
		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.itemName \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.customername end busName, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.delState \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.delState end busDelState, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.state else 0 end busState \n");
		sql.append("from task a \n");
		sql.append("inner join taskhandover hand on a.comid=hand.comid and a.id=hand.taskid and hand.curstep=1  \n");
		sql.append("left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0\n");
		sql.append("left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 \n");
		sql.append("left join userinfo c on a.owner = c.id \n");
		sql.append("left join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("left join userinfo g on a.executor = g.id \n");
		sql.append("left join userOrganic gg on g.id =gg.userId and a.comId=gg.comId\n");
		sql.append("left join upfiles h on gg.mediumHeadPortrait = h.id \n");
		sql.append("left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comid = ? and a.state =1 \n");
		args.add(userInfo.getComId());
		sql.append("and to_date(a.dealtimelimit,'yyyy-mm-dd') < trunc(SYSDATE)\n");

		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append("a.executor =? or a.owner=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.id from tasksharer b where a.id = b.taskid and \n");
			sql.append("  	(b.sharerid =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.sharerid and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//执行人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = a.executor and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//负责人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//被@的人

			sql.append(") \n");
		}
		//任务状态筛选
		if(null!=task.getState() && task.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(task.getState());
		}
		//任务执行人筛选
		if(null!=task.getExecutor() && task.getExecutor()!=0){
			sql.append("and a.executor=? and a.state=1 \n");
			args.add(task.getExecutor());
		}
		//任务负责人筛选
		if(null!=task.getOwner() && task.getOwner()!=0){
			sql.append("and a.owner=? \n");
			args.add(task.getOwner());
		}
		//经办人筛选
		if(null!=task.getOperator() && task.getOperator()!=0){
			sql.append("and exists (\n");
			sql.append("select id from  taskHandOver  where comId=? and (fromUser=? or toUser=?) and taskId=a.id");
			args.add(userInfo.getComId());
			args.add(task.getOperator());
			args.add(task.getOperator());
			sql.append(")\n");
		}
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}
		//执行人类型
		String execuType =  task.getExecuType();
		if(null!=execuType && !"".equals(execuType)){
			if("0".equals(execuType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.executor=?");
			}else if("1".equals(execuType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.executor and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		sql.append(") a where 1=1");
		sql.append("and to_date(a.dealtimelimit,'yyyy-mm-dd') < trunc(SYSDATE)\n");
		return this.pagedQuery(sql.toString(), "a.state,a.recordcreatetime desc", args.toArray(), Task.class);
	}


	/**
	 * 获取所有的待办任务
	 * @param task
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> taskToDoListOfAll(Task task,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.comid,a.id,a.taskname,a.owner,a.recordcreatetime,hand.handTimeLimit dealtimelimit,a.state,a.busId,a.busType,\n");
//		sql.append("a.taskprogress,case when a.state=1 then a.grade else '0' end as grade,\n");
		sql.append("a.taskprogress,a.grade,\n");
		sql.append("c.username as ownerName,c.gender,d.uuid,d.filename,\n");

		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where a.comid = today.comid and a.id = today.busid \n");
		sql.append(" and today.bustype='"+ConstantInterface.TYPE_TASK+"' and today.userId=? and today.isclock=0\n");
		args.add(userInfo.getId());
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 1 else 0 end as isread,\n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState,  \n");
		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.itemName \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.customername end busName, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.delState \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.delState end busDelState, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.state else 0 end busState \n");
		sql.append("from task a \n");
		sql.append("inner join taskhandover hand on a.comid=hand.comid and a.id=hand.taskid and hand.curstep=1  \n");
		sql.append("left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0\n");
		sql.append("left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 \n");
		sql.append("left join userinfo c on a.owner = c.id \n");
		sql.append("left join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and a.comid = ? and a.state=? and a.executor=? \n");
		args.add(userInfo.getComId());
		args.add(1);
		args.add(userInfo.getId());

		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		//任务负责人筛选
		if(null!=task.getOwner() && task.getOwner()!=0){
			sql.append("and a.owner=? \n");
			args.add(task.getOwner());
		}
		//经办人筛选
		if(null!=task.getOperator() && task.getOperator()!=0){
			sql.append("and exists (\n");
			sql.append("	select id from  taskHandOver  where comId=? and (fromUser=? or toUser=?) and taskId=a.id");
			args.add(userInfo.getComId());
			args.add(task.getOperator());
			args.add(task.getOperator());
			sql.append(")\n");
		}
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}
		sql.append("order by a.state,a.recordcreatetime desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Task.class);
	}



	/**
	 * 获取所有的自己负责人任务
	 * @param task
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listChargeTaskOfAll(Task task,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.comid,a.id,a.taskname,a.recordcreatetime,hand.handTimeLimit dealtimelimit,a.state,a.busId,a.busType,a.executor,\n");
//		sql.append(" a.taskprogress, case when a.state=1 then a.grade else '0' end as grade,\n");
		sql.append(" a.taskprogress, a.grade,\n");

		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where a.comid = today.comid and a.id = today.busid \n");
		sql.append(" and today.bustype='"+ConstantInterface.TYPE_TASK+"' and today.userId=? and today.isclock=0\n");
		args.add(userInfo.getId());
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 1 else 0 end as isread,\n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState,g.username as executorName,\n");
		sql.append("g.gender as executorGender,h.uuid as executorUuid,h.filename as executorFileName,\n");
		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.itemName \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.customername end busName, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.delState \n");
		sql.append("when a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.delState end busDelState, \n");

		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then item.state else 0 end busState \n");
		sql.append("from task a \n");
		sql.append("inner join taskhandover hand on a.comid=hand.comid and a.id=hand.taskid and hand.curstep=1  \n");
		sql.append("left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0\n");
		sql.append("left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 \n");
		sql.append("inner join userinfo g on a.executor = g.id \n");
		sql.append("inner join userOrganic gg on g.id =gg.userId and a.comId=gg.comId\n");
		sql.append("left join upfiles h on gg.mediumHeadPortrait = h.id \n");
		sql.append("left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and a.comid = ? and a.state <> -1 \n");
		args.add(userInfo.getComId());
		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//任务状态筛选
		if(null!=task.getState() && task.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(task.getState());
		}
		//任务执行人筛选
		if(null!=task.getExecutor() && task.getExecutor()!=0){
			sql.append("and a.executor=? and a.state=1 \n");
			args.add(task.getExecutor());
		}
		//任务负责人筛选
		if(null!=task.getOwner() && task.getOwner()!=0){
			sql.append("and a.owner=? \n");
			args.add(task.getOwner());
		}
		//经办人筛选
		if(null!=task.getOperator() && task.getOperator()!=0){
			sql.append("and exists (\n");
			sql.append("	select id from  taskHandOver  where comId=? and (fromUser=? or toUser=?) and taskId=a.id");
			args.add(userInfo.getComId());
			args.add(task.getOperator());
			args.add(task.getOperator());
			sql.append(")\n");
		}
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}
		//执行人类型
		String execuType =  task.getExecuType();
		if(null!=execuType && !"".equals(execuType)){
			if("0".equals(execuType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.executor=?");
			}else if("1".equals(execuType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.executor and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		sql.append("order by a.state,a.recordcreatetime desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Task.class);
	}
	/********************************mobile任务查询****************************************/
	/**
	 * 根据主键获取任务详情
	 * @param id
	 * @return
	 */
	public Task queryTaskById(Integer id,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//任务主键，任务发布时间，任务团队号，父任务主键，任务名称，任务描述
		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.parentId,a.taskName,a.taskRemark,a.version,");
		//任务创建人，任务总进度，任务办理状态，任务删除状态，任务关联模块类型，父任务信息
		sql.append("\n a.creator,a.taskProgress,a.state,a.delState,a.busType,d.taskname as pTaskName, \n");
		//任务负责人
		sql.append("\n a.owner,b.username as ownerName,b.gender,c.uuid,c.filename, \n");
		//任务的办理时限，任务的类型，任务的完成时限，任务紧急程度
		sql.append("\n exUser.handTimeLimit,a.taskType,a.dealTimeLimit,a.grade, \n");
		//任务执行人主键，任务执行状态，任务执行进度
		sql.append("\n exUser.executor,exUser.state executeState,exUser.taskProgress executeProgress,");
		//任务关注状态
		sql.append("\n case when atten.id is null then 0 else 1 end as attentionState, \n");

		//关联的模块主键信息
		sql.append("case  \n");
		sql.append(" when a.busType='"+ConstantInterface.TYPE_ITEM+"' then nvl(e.id,0) \n");
		sql.append(" when  a.bustype='"+ConstantInterface.TYPE_CRM+"' then nvl(crm.id,0)  \n");
		sql.append(" when  a.bustype='"+ConstantInterface.TYPE_DEMAND_PROCESS+"' then  nvl(dp.id,0) \n");
		sql.append(" when  a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' then nvl(spflow.id,0)\n");
		sql.append(" else a.busid end busId, \n");
		//关联的模块名称
		sql.append("case when a.bustype='"+ConstantInterface.TYPE_ITEM+"' then e.itemname   \n");
		sql.append(" when  a.bustype='"+ConstantInterface.TYPE_CRM+"' then crm.customername  \n");
		sql.append(" when  a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' then spflow.flowName  \n");
		sql.append(" when  a.bustype='"+ConstantInterface.TYPE_DEMAND_PROCESS+"' then '需求'|| dp.serialNum  \n");
		sql.append(" end busName,  \n");
		//关联的项目阶段信息
		sql.append("stagedInfo.id stagedItemId,stagedItem.Stagedname stagedItemName \n");
		//任务主键信息
		sql.append("from task a \n");
		//办理人员（需要认领或是需要办理的）
		sql.append("left join taskExecutor exUser on a.comid=exUser.comid and a.id=exUser.taskid and exUser.executor=? \n");
		args.add(userInfo.getId());
//		//推送人员
//		sql.append("left join ( select exUserIn.pushUser,exUserIn.taskId from taskExecutor exUserIn where exUserIn.taskid=? group by exUserIn.pushUser,exUserIn.taskId ) pushUser on  a.id=pushUser.taskid \n");
//		args.add(id);


		//任务负责人
		sql.append("inner join userinfo b on a.owner = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on  bb.smallheadportrait = c.id \n");
		//父任务信息
		sql.append("left join task d on a.comid = d.comid and d.id = a.parentid \n");

		//项目正常才取得项目信息
		sql.append("left join item e on a.comid = e.comid and a.busid=e.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and e.delstate=0\n");
		sql.append("left join stagedInfo on a.comid=stagedInfo.Comid and stagedInfo.Moduletype='task' and stagedInfo.moduleId=a.id and e.id=stagedInfo.itemid\n");
		sql.append("left join stagedItem on a.comid=stagedItem.Comid and e.id=stagedItem.itemid and stagedItem.id=stagedInfo.stagedItemId\n");
		//获取客户信息
		sql.append("left join customer crm on a.comid = crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0\n");
		//获取审批信息
		sql.append("left join spflowinstance spflow on a.comid = spflow.comid and a.busid=spflow.id and a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"'\n");
		//获取客户信息
		sql.append("left join demandprocess dp on a.comid = dp.comid and a.busid=dp.id and a.bustype='"+ConstantInterface.TYPE_DEMAND_PROCESS+"'\n");
		//关注信息
		sql.append("left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("where a.id = ? and a.comId=?");
		args.add(id);
		args.add(userInfo.getComId());
		return (Task)this.objectQuery(sql.toString(), args.toArray(), Task.class);
	}
	/**
	 * 验证当前操作人对此任务是否有查看权限
	 * @param comId
	 * @param taskId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> authorCheck(Integer comId,Integer taskId,Integer userId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from task a  \n");
		sql.append("left join taskExecutor exUser on a.comid=exUser.comId and a.id=exUser.taskId \n");
		sql.append("where a.comid = ? and a.id=? and a.delstate=0  \n");
		args.add(comId);
		args.add(taskId);
		sql.append("and (\n");
		//任务的参与、执行、负责权限验证
		sql.append("exUser.executor =? or a.owner=? \n");
		args.add(userId);
		args.add(userId);
		//上级权限验证
		//参与人上级权限验证
		sql.append(" or exists( \n");
		sql.append("  	select b.id from tasksharer b where a.id = b.taskid and \n");
		sql.append("  	(b.sharerid =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.sharerid and sup.leader=?))\n");
		sql.append(" ) \n");
		args.add(userId);
		args.add(comId);
		args.add(userId);
		//执行人上级权限验证
		sql.append(" or exists(select id from myLeaders where creator = exUser.executor and comId = ? and leader = ? and leader <> creator )");
		args.add(comId);
		args.add(userId);
		//负责人上级权限验证
		sql.append(" or exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
		args.add(comId);
		args.add(userId);
		sql.append(") \n");
		return this.listQuery(sql.toString(), args.toArray(),Task.class);
	}
	/**
	 * 根据任务主键获取子任务集合
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listSonTask(Integer taskId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.id,a.recordcreatetime,a.comid,a.taskname,a.taskremark,a.taskProgress, \n");
		sql.append("a.owner,a.state, b.username as ownerName,b.gender,c.uuid,c.filename from task a \n");
//		sql.append("count(d.id) as sonTaskNum from task a \n");
		sql.append("inner join userinfo b on a.owner = b.id  \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id \n");
		sql.append("left join task d on a.comid = d.comid and a.id = d.parentid \n");
		sql.append("where a.comId=? and a.parentid = ? and a.delstate=0 \n");
//		sql.append("group by a.id,a.recordcreatetime,a.comid,a.taskname,a.owner,a.executor,a.state,hand.handTimeLimit,b.username,b.gender,c.uuid,c.filename,e.username,e.gender,f.uuid,f.filename \n");
//		sql.append("order by a.id desc");
		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(), args.toArray(), Task.class);
	}
	/**
	 * 所有后代任务集合
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskOfChildren(Integer taskId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from task a where a.comid=? and a.delstate=0 start with a.id=? connect by prior a.id =  a.parentid");
		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(), args.toArray(), Task.class);
	}
	/**
	 * 获取当前任务的所有后代；不包括当前任务和预删除的任务
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskOfOnlyChildren(Integer taskId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from task a where a.comid=? and a.delstate=0 start with a.parentid=? connect by prior a.id =  a.parentid");
		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(), args.toArray(), Task.class);
	}
	/**
	 * 获取当前任务的所有后代；不包括当前任务
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskOfAllOnlyChildren(Integer taskId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from task a where a.comid=? start with a.parentid=? connect by prior a.id =  a.parentid");
		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(), args.toArray(), Task.class);
	}
	/**
	 * 获取当前任务的所有父任务,不包括预删除的
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskOfParent(Integer taskId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from task a where a.comid=? and a.delstate=0 start with a.id=? connect by prior a.parentid =  a.id");
		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(), args.toArray(), Task.class);
	}
	/**
	 * 查询任务当前设置父节点的父节点，不包括自己和预删除的
	 * @param parentId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> getParentTask(Integer parentId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id from task a \n");
		sql.append("where a.comid = ? and a.delstate=0 and a.id<>? start with id=? CONNECT BY PRIOR parentid =id \n");
		args.add(comId);
		args.add(parentId);
		args.add(parentId);
		sql.append("order by a.id desc  \n");
		return this.listQuery(sql.toString(),args.toArray(),Task.class);
	}
	/**
	 * 查询任务当前设置父节点的父节点，不包括自己
	 * @param parentId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> getAllParentTask(Integer parentId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id from task a \n");
		sql.append("where a.comid = ? and a.id<>? start with id=? CONNECT BY PRIOR parentid =id \n");
		args.add(comId);
		args.add(parentId);
		args.add(parentId);
		sql.append("order by a.id desc  \n");
		return this.listQuery(sql.toString(),args.toArray(),Task.class);
	}
	/**
	 * 获取当前任务的所有父任务
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskOfAllParent(Integer taskId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from task a where a.comid=? start with a.id=? connect by prior a.parentid =  a.id");
		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(), args.toArray(), Task.class);
	}
	/**
	 * 查询任务参与人信息
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskSharer> listTaskSharer(Integer taskId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,b.username as sharerName,b.gender,c.uuid,c.filename \n");
		sql.append("from taskSharer a inner join userinfo b on a.sharerid = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id \n");
		sql.append("where a.taskid = ? and a.comid = ? \n");
		args.add(taskId);
		args.add(comId);
		return this.listQuery(sql.toString(), args.toArray(), TaskSharer.class);
	}
	/**
	 * 任务办理人员信息
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskExecutor> listTaskExecutor(Integer taskId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,b.username as executorName,pushUser.username as pushUserName");
		sql.append("\n from taskExecutor a ");
		sql.append("\n inner join userinfo pushUser on pushUser.id =a.pushUser");
		sql.append("\n inner join userinfo b on a.executor = b.id ");
		sql.append("\n where a.taskid = ? and a.comid = ? \n");
		args.add(taskId);
		args.add(comId);
		return this.listQuery(sql.toString(), args.toArray(), TaskExecutor.class);
	}

	/**
	 * 获取需要复制任务的执行人信息
	 * @param ids 执行人主键数组
	 * @return java.util.List<com.westar.base.model.UserInfo>
	 * @author LiuXiaoLin
	 * @date 2018/6/20 0020 13:42
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listExecutorsForCopy(Integer[] ids){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("\n select distinct b.*,d.uuid as smImgUuid,d.filename as smImgName,dep.depName,formedoUser.Id as formedoUserId,formedoUser.Username as formedoUserName ");
		sql.append("\n from userinfo b");
		sql.append("\n inner join userOrganic c on b.id = c.userid and c.enabled =1");
		sql.append("\n left join upfiles d on c.comid = d.comid and c.smallheadportrait = d.id");
		sql.append("\n left join department dep on c.depId=dep.id and dep.comid=c.comId");
		sql.append("\n left join formedo on b.id=formedo.creator");
		sql.append("\n left join userOrganic formedoOrg on formedo.comid=formedoOrg.Comid and formedo.userid=formedoOrg.Userid and formedoOrg.Enabled=1");
		sql.append("\n left join userinfo formedoUser on formedoOrg.userid=formedoUser.Id");
		sql.append("\n where 1=1");

		this.addSqlWhereIn(ids,sql,args,"\n and b.id in ?");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 汇报任务进度
	 * @param task
	 * @return
	 */
	public void taskProgressReport(Task task){
		StringBuffer sql = new StringBuffer("update task a set a.taskProgress=:taskProgress where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),task);
	}
	/**
	 * 完成时限更新
	 * @param task
	 * @return
	 */
	public void taskDealTimeLimitUpdate(Task task){
		StringBuffer sql = new StringBuffer("update task a set a.dealTimeLimit=:dealTimeLimit where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),task);
	}
	/**
	 * 修改办理时限
	 * @param task
	 */
	public void taskExecuteTimeLimitUpdate(Task task){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append(" update taskExecutor set handTimeLimit = ? where taskId = ? and comid = ?");
		args.add(task.getDealTimeLimit());
		args.add(task.getId());
		args.add(task.getComId());
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 修改当期步骤时限
	 * @param task
	 */
	public void taskOverTimeLimitUpdate(Task task){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append(" update taskhandover set handTimeLimit = ? where taskId = ? and curstep='1' and comid = ?");
		args.add(task.getDealTimeLimit());
		args.add(task.getId());
		args.add(task.getComId());
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 任务名称变更
	 * @param task
	 * @return
	 */
	public void taskNameUpdate(Task task){
		StringBuffer sql = new StringBuffer("update task a set a.taskName=:taskName where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),task);
	}
	/**
	 * 任务说明更新
	 * @param task
	 * @return
	 */
	public void taskTaskRemarkUpdate(Task task){
		StringBuffer sql = new StringBuffer("update task a set a.taskRemark=:taskRemark where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),task);
	}
	/**
	 * 任务母任务关联
	 * @param task
	 * @return
	 */
	public void taskParentIdUpdate(Task task){
		StringBuffer sql = new StringBuffer("update task a set a.parentId=:parentId where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),task);
	}
	/**
	 * 模块关联
	 * @param task
	 * @return
	 */
	public void taskBusIdUpdate(Task task){
		StringBuffer sql = new StringBuffer("update task a set a.busId=:busId,a.busType=:busType where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),task);
	}
	/**
	 * 任务负责人更新
	 * @param task
	 * @return
	 */
	public void taskOwnerUpdate(Task task){
		StringBuffer sql = new StringBuffer("update task a set a.owner=:owner where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),task);
	}
	/**
	 * 任务标记完成
	 * @param task
	 * @return
	 */
	public void remarkTaskState(Task task){
		//更新task
		StringBuffer sql = new StringBuffer("update task a set state=:state,taskProgress=:taskProgress where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),task);
	}

	/**
	 * 更新实际执行时间
	 * @author hcj 
	 * @date: 2018年10月22日 下午4:51:07
	 * @param task
	 */
	public void updateTaskExecuteTime(Task task){
		//更新taskExecuteTime
		task.setEndTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm));
		StringBuffer sql3 = new StringBuffer("update taskExecuteTime a set a.endTime=:endTime where a.taskId=:id and a.endTime is null ");
		this.update(sql3.toString(),task);

	}
	/**
	 * 根据主键id查询讨论详情
	 * @param id
	 * @param comId
	 * @return
	 */
	public TaskTalk queryTaskTalk(Integer id,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select d.speaker as pSpeaker,d.content as pContent,e.username as pSpeakerName,a.*,b.username as speakerName,b.gender,c.uuid,c.filename from taskTalk a \n");
		sql.append("inner join userinfo b on a.speaker = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on  bb.mediumHeadPortrait = c.id \n");
		sql.append("left join taskTalk d on a.parentid = d.id and a.comid = d.comid \n");
		sql.append("left join userinfo e on d.speaker = e.id \n");
		sql.append("where a.comId=? and a.id = ?");
		args.add(comId);
		args.add(id);
		return (TaskTalk)this.objectQuery(sql.toString(), args.toArray(), TaskTalk.class);
	}
	/**
	 * 根据任务主键查询其下的讨论信息
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskTalk> listTaskTalk(Integer taskId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select connect_by_isleaf as isLeaf,PRIOR a.speaker as pSpeaker,PRIOR a.content as pContent, \n");
		sql.append("PRIOR b.username as pSpeakerName,a.*,b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from taskTalk a \n");
		sql.append("inner join userinfo b on a.speaker = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on  bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.taskid = ? \n");
		sql.append("start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid \n");
		sql.append("order siblings by a.recordcreatetime desc,a.id");
		args.add(comId);
		args.add(taskId);
		return this.pagedQuery(sql.toString(), null, args.toArray(), TaskTalk.class);
	}
	/**
	 * 根据父节点查询回复集合
	 * @param taskId
	 * @param comId
	 * @param parentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskTalk> listReplyTaskTalk(Integer taskId,Integer comId,Integer parentId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,b.username as speakerName,b.gender,c.uuid,c.filename from taskTalk a \n");
		sql.append("inner join userinfo b on a.speaker = b.id  \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.taskid = ? and a.parentid=? \n");
		sql.append("CONNECT BY PRIOR a.id = a.parentid \n");
		args.add(comId);
		args.add(taskId);
		args.add(parentId);
		return this.listQuery(sql.toString(), args.toArray(), TaskTalk.class);
	}
	/**
	 * 更新任务讨论的父级节点
	 * @param id
	 * @param comId
	 */
	public void updateTaskTalkParentId(Integer id,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update tasktalk set parentId=(select c.parentid \n");
		sql.append("from tasktalk c \n");
		sql.append("where c.id=?) where parentid = ? and comId = ? \n");
		args.add(id);
		args.add(id);
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 删除任务节点讨论及其回复
	 * @param id
	 * @param comId
	 */
	public void delTaskTalk(Integer id,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from tasktalk a where a.comid =? and a.id in \n");
		sql.append("(select id from tasktalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取任务附件
	 * @param taskId 任务主键
	 * @param comId 企业主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskUpfile> listTaskUpfile(Integer taskId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from( \n");
		sql.append("select a.* from \n");
		sql.append("( \n");
		sql.append("select a.id,a.upfileId,task.comid,task.taskname,task.id as taskId,task.parentid,b.filename,b.uuid,a.recordcreatetime,0 taskTalkId,\n");
		sql.append("c.username as creatorName,c.gender,d.uuid as userUuid,d.filename userFileName,'"+ConstantInterface.TYPE_TASK+"' type,b.sizem\n");
		sql.append("from taskUpfile a inner join task on a.comid = task.comid and a.taskid = task.id \n");
		sql.append("inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append(") a \n");
		sql.append("where a.comid=? start with a.taskId=? connect by prior a.taskId =  a.parentid \n");
		args.add(comId);
		args.add(taskId);
		sql.append("union all  \n");
		sql.append("select a.* from \n");
		sql.append("( \n");
		sql.append("select a.id,a.upfileId,task.comid,task.taskname,task.id as taskId,task.parentid,b.filename,b.uuid,a.recordcreatetime,a.taskTalkId,\n");
		sql.append("c.username as creatorName,c.gender,d.uuid as userUuid,d.filename userFileName, '"+ConstantInterface.TYPE_TASKTALK+"' type,b.sizem \n");
		sql.append("from taskTalkUpfile a  inner join task on a.comid = task.comid and a.taskid = task.id \n");
		sql.append("inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append(") a \n");
		sql.append("where a.comid=? start with a.taskId=? connect by prior a.taskId =  a.parentid \n");
		args.add(comId);
		args.add(taskId);
		sql.append(") a \n");
		return this.listQuery(sql.toString(), args.toArray(), TaskUpfile.class);
	}
	
	/**
	 * 非现在的任务参与人
	 * @param taskId
	 * @param comId
	 * @param userIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskSharer> listRemoveTaskSharer(Integer taskId,Integer comId,Integer[] userIds){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( \n");
		//原有项目参与人
		sql.append("select a.* from taskSharer a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(taskId, sql, args, " and  a.taskId = ? ");
		this.addSqlWhere(comId, sql, args, " and  a.comId= ? ");

		sql.append("minus \n");
		//现在的项目参与人
		sql.append("select a.* from taskSharer a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(taskId, sql, args, " and  a.taskId = ?");
		this.addSqlWhere(comId, sql, args, " and a.comId= ?");
		sql.append(" and a.sharerId in (0");
		if(null!=userIds && userIds.length>0){
			for (Integer userId : userIds) {
				sql.append(","+userId);
			}
		}
		sql.append(")\n");
		sql.append(")\n");
		return this.listQuery(sql.toString(), args.toArray(), TaskSharer.class);
	}
	/**
	 * 任务负责人是否在分享人中
	 * @param id 任务主键
	 * @param comId 企业编号
	 * @param owner 任务负责人
	 * @return
	 */
	public TaskSharer getTaskSharer4Owner(Integer taskId, Integer comId,
										  Integer owner) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//原有项目参与人
		sql.append("select a.* from taskSharer a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(taskId, sql, args, " and  a.taskId = ? ");
		this.addSqlWhere(comId, sql, args, " and  a.comId= ? ");
		this.addSqlWhere(comId, sql, args, " and a.sharerId= ? ");

		return (TaskSharer) this.objectQuery(sql.toString(), args.toArray(), TaskSharer.class);
	}
	/**
	 * 任务讨论的附件
	 * @param comId 企业编号
	 * @param taskId 任务主键
	 * @param taskTalkId 讨论的主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskTalkUpfile> listTaskTalkFile(Integer comId,
												 Integer taskId, Integer taskTalkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.taskId,a.taskTalkId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext,\n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from taskTalkUpfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comid =? and a.taskId = ? \n");
		args.add(comId);
		args.add(taskId);
		this.addSqlWhere(taskTalkId, sql, args, " and a.taskTalkId=?");
		sql.append("order by isPic asc, a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), TaskTalkUpfile.class);
	}
	/**
	 * 讨论的附件
	 * @param comId
	 * @param talkId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskTalk> listTaskTalkUpfileForDel(Integer comId,
												   Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from taskTalk where comid=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(talkId);
		return this.listQuery(sql.toString(), args.toArray(), TaskTalk.class);
	}
	/**
	 * 获取任务所有参与人集合
	 * @param comId
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskSharer> listTaskOwners(Integer comId,Integer taskId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.sharerid,a.taskid,b.username as sharerName from tasksharer a \n");
		sql.append("inner join userinfo b on  a.sharerid = b.id \n");
		sql.append("where a.comid = ? and a.taskid = ? \n");
		args.add(comId);
		args.add(taskId);
		sql.append("union \n");
		sql.append("select c.owner as sharerid,c.id as taskId,d.username as sharerName from task c \n");
		sql.append("inner join userinfo d on  c.owner = d.id \n");
		sql.append("where c.comid = ? and c.id = ? \n");
		args.add(comId);
		args.add(taskId);
		sql.append("union \n");
		sql.append("select taskEx.executor as sharerid,e.id as taskId,f.username as sharerName from task e \n");
		sql.append("inner join taskExecutor taskEx on  taskEx.taskid = e.id \n");
		sql.append("inner join userinfo f on taskEx.executor = f.id \n");
		sql.append("where e.comid = ? and e.id = ? \n");
		args.add(comId);
		args.add(taskId);
		//督察人员
//		sql.append("union \n");
//		sql.append("select f.userid as sharerid,? as taskId,e.username as sharerName from forceinpersion f \n");
//		args.add(taskId);
//		sql.append("inner join Userinfo e on f.userid=e.id \n");
//		sql.append("inner join userOrganic uOrg3 on f.comid = uOrg3.Comid and e.id = uOrg3.Userid and uOrg3.Enabled=1 \n");
//		sql.append("where f.comid = ? and f.type=? \n");
//		args.add(comId);
//		args.add(BusinessTypeConstant.type_task);
		return this.listQuery(sql.toString(),args.toArray(),TaskSharer.class);
	}
	/**
	 * 获取任务所有参与人集合没有督察人员(在岗的)
	 * @param comId 企业号
	 * @param taskId 任务主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listTaskOwnersNoForce(Integer comId,Integer taskId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//任务参与人
		sql.append("select b.id,b.email,b.wechat,b.qq,b.userName from tasksharer a \n");
		sql.append("inner join userinfo b on  a.sharerid = b.id \n");
		sql.append("inner join userorganic d on  a.comid=d.comid and b.id=d.userid and d.enabled=1 \n");
		sql.append("where a.comid = ? and a.taskid = ? \n");
		args.add(comId);
		args.add(taskId);
		//任务负责人
		sql.append("union \n");
		sql.append("select d.id,d.email,d.wechat,d.qq,d.userName from task c \n");
		sql.append("inner join userinfo d on  c.owner = d.id \n");
		sql.append("inner join userorganic e on  c.comid=e.comid and d.id=e.userid and e.enabled=1 \n");
		sql.append("where c.comid = ? and c.id = ? \n");
		args.add(comId);
		args.add(taskId);
		sql.append("union \n");
		//任务执行人
		sql.append("select f.id,f.email,f.wechat,f.qq,f.userName from task e \n");
		sql.append("inner join taskExecutor exUser on  e.id = exUser.taskId \n");
		sql.append("inner join userinfo f on  exUser.executor = f.id \n");
		sql.append("inner join userorganic d on  e.comid=d.comid and f.id=d.userid and d.enabled=1 \n");
		sql.append("where e.comid = ? and e.id = ? \n");
		args.add(comId);
		args.add(taskId);
		sql.append("union \n");
		//任务关注人
		sql.append("select f.id,f.email,f.wechat,f.qq,f.userName from attention e \n");
		sql.append("inner join userinfo f on  e.userId = f.id \n");
		sql.append("and e.busType="+ConstantInterface.TYPE_TASK+" \n");
		sql.append("inner join userorganic d on  e.comid=d.comid and f.id=d.userid and d.enabled=1 \n");
		sql.append("where e.comid = ? and e.busId = ? \n");
		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(),args.toArray(),UserInfo.class);
	}
	/**
	 * 获取任务负责人，执行人以及关注人员
	 * @param comId 企业号
	 * @param taskId 任务主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listTaskUserForMsg(Integer comId,Integer taskId,boolean needExecuor ){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//任务负责人
		sql.append("select d.id,d.email,d.wechat,d.qq,d.userName from task c \n");
		sql.append("inner join userinfo d on  c.owner = d.id \n");
		sql.append("inner join userorganic e on  c.comid=e.comid and d.id=e.userid and e.enabled=1 \n");
		sql.append("where c.comid = ? and c.id = ? \n");
		args.add(comId);
		args.add(taskId);
		sql.append("union \n");
		if(needExecuor){
			//任务执行人
			sql.append("select f.id,f.email,f.wechat,f.qq,f.userName from task e \n");
			sql.append("inner join taskExecutor tExecuor on  tExecuor.taskId = e.id \n");
			sql.append("inner join userinfo f on  tExecuor.executor = f.id \n");
			sql.append("inner join userorganic d on  e.comid=d.comid and f.id=d.userid and d.enabled=1 \n");
			sql.append("where e.comid = ? and e.id = ? \n");
			args.add(comId);
			args.add(taskId);
			sql.append("union \n");
		}
		//任务关注人
		sql.append("select f.id,f.email,f.wechat,f.qq,f.userName from attention e \n");
		sql.append("inner join userinfo f on  e.userId = f.id \n");
		sql.append("and e.busType="+ConstantInterface.TYPE_TASK+" \n");
		sql.append("inner join userorganic d on  e.comid=d.comid and f.id=d.userid and d.enabled=1 \n");
		sql.append("where e.comid = ? and e.busId = ? \n");
		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(),args.toArray(),UserInfo.class);
	}
	/**
	 * 获取任务讨论记录集合FOR索引
	 * @param comId
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskTalk> listTaskTalk4Index(Integer comId,Integer taskId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.content,b.username as speakerName from taskTalk a \n");
		sql.append("inner join userInfo b on a.speaker = b.id \n");
		sql.append("where a.comid = ? and a.taskid = ? \n");
		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(),args.toArray(),TaskTalk.class);
	}
	/**
	 * 获取任务子任务集合为其创建索引
	 * @param comId
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listSonTask4Index(Integer comId,Integer taskId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.taskName from task a where a.comid = ? and a.parentid=? \n");
		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(),args.toArray(),Task.class);
	}

	/**
	 * 获取自己的无效任务集合
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listOfUnusedTask(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from task a where a.state=? and a.delstate=0 and a.comid = ? and a.creator=?");
		args.add(-1);
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		return this.listQuery(sql.toString(),args.toArray(),Task.class);
	}
	/**
	 * 查找上级人员
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ImmediateSuper> listImmediateSuper(Integer taskId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select leader from myLeaders where creator and comId = ? exists (");
		args.add(comId);
		sql.append("\n 	select b.owner from  task b where b.id=? and b.comId=?");
		args.add(taskId);
		args.add(comId);
		sql.append("\n )");
		sql.append("\n union all");
		//任务执行人
		sql.append("\n select b.executor from  task b where b.id=? and b.comId=?");
		args.add(taskId);
		args.add(comId);
		sql.append("\n union all");
		//任务参与人
		sql.append("\n select b.sharerId from  taskSharer b where b.taskId=? and b.comId=?");
		args.add(taskId);
		args.add(comId);
		sql.append("\n )");
		sql.append("\n connect by prior a.leader = a.creator");
		sql.append("\n )a group by a.leader");
		return this.listQuery(sql.toString(), args.toArray(), ImmediateSuper.class);
	}

	/**
	 * 负责人或是执行人的任务
	 * @param comId 企业号
	 * @param userId 人员主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listUserAllTask(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.owner,nvl(b.executor,0) executor,a.state,a.taskname,a.taskType ");
		sql.append("\n from task a");
		sql.append("\n left join taskexecutor b on a.comid=b.comid and a.id=b.taskId and b.executor=?");
		args.add(userId);
		sql.append("\n where a.comId=? and (a.owner=? or b.executor=?) and a.delstate=0");
		args.add(comId);
		args.add(userId);
		args.add(userId);
		sql.append("\n order by a.owner,b.executor");
		return this.listQuery(sql.toString(), args.toArray(), Task.class);
	}
	/**
	 * 查看任务移交记录
	 * @param taskId任务主键
	 * @param comId企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FlowRecord> listFlowRecord(Integer taskId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.recordcreatetime acceptDate,c.gender,a.endtime endTime,c.username,d.uuid,a.fromuser fromUser,a.touser userId,a.handtimelimit handTimeLimit, \n");
		sql.append("to_number(to_date(a.endtime, 'yyyy-mm-dd HH24:MI:SS')-to_date(a.recordcreatetime, 'yyyy-mm-dd HH24:MI:SS')) * 86400000 useTime,");
		sql.append("case  when (a.handtimelimit is not null) \n");
		sql.append("then to_number(to_date(a.endtime, 'yyyy-mm-dd HH24:MI:SS')-to_date(a.handtimelimit||' 23:59:59', 'yyyy-mm-dd HH24:MI:SS')) * 86400000 \n");
		sql.append("else 0  end overTime \n");
		sql.append("from taskhandover a inner join userorganic b on a.comid=b.comid and a.touser=b.userid \n");
		sql.append("inner join userinfo c on b.userid=c.id \n");
		sql.append("left join upfiles d on b.mediumheadportrait=d.id \n");
		sql.append("where a.comid=? and a.taskid=? \n");
		sql.append("order by a.endtime desc nulls first, a.id desc \n");

		args.add(comId);
		args.add(taskId);
		return this.listQuery(sql.toString(), args.toArray(), FlowRecord.class);
	}
	/**
	 * 直接合并留言信息和附件
	 * @param comId 企业号
	 * @param taskCId 待整合的任务主键
	 * @param taskId 整合后的任务主键
	 */
	public void compressTalk(Integer comId, Integer taskCId, Integer taskId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并留言信息
		sql.append(" update taskTalk set taskId=? where comid=? and taskId=?");
		args.add(taskId);
		args.add(comId);
		args.add(taskCId);
		this.excuteSql(sql.toString(), args.toArray());
		//合并留言附件
		args = new ArrayList<Object>();
		sql = new StringBuffer();
		sql.append(" update taskTalkUpfile set taskId=? where comid=? and taskId=?");
		args.add(taskId);
		args.add(comId);
		args.add(taskCId);
		this.excuteSql(sql.toString(), args.toArray());
		//合并任务附件
		args = new ArrayList<Object>();
		sql = new StringBuffer();
		sql.append(" update taskUpfile set taskId=? where comid=? and taskId=?");
		args.add(taskId);
		args.add(comId);
		args.add(taskCId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 修改项目明细数据
	 * @param comId 企业号
	 * @param taskCId 待选任务主键
	 * @param taskId 合并后的任务主键
	 */
	public void updateStagedInfo(Integer comId, Integer taskCId, Integer taskId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目阶段信息
		sql.append(" update stagedInfo set moduleId=? where moduleType='task' and comid=? and moduleId=?");
		args.add(taskId);
		args.add(comId);
		args.add(taskCId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 整合子任务
	 * @param comId
	 * @param taskCId
	 * @param taskId
	 */
	public void compressSonTask(Integer comId, Integer taskCId, Integer taskId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update task set parentId=? where comid=? and parentId=?");
		args.add(taskId);
		args.add(comId);
		args.add(taskCId);
		this.excuteSql(sql.toString(), args.toArray());

	}

	/**
	 * 取得任务的关联项目以及项目阶段名称
	 * @param taskId 任务主键
	 * @param userInfo 操作人员
	 * @return
	 */
	public Task getTaskBusInfo(Integer taskId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.parentId,a.taskName,a.bustype,b.id busid,b.itemname busname,d.id stagedItemId,  ");
		sql.append("\n  d.stagedname stagedItemName,b.delstate busDelState,a.delstate  ");
		sql.append("\n from task a left join item b on a.busid=b.id and a.comid=b.comid");
		sql.append("\n left join stagedInfo c on c.comid=a.comid  and a.id=c.moduleId and c.moduleType='task'");
		sql.append("\n left join stagedItem d on a.comid=d.comid and a.busId=d.itemId and c.stagedItemId=d.id");
		sql.append("\n where a.bustype='"+ConstantInterface.TYPE_ITEM+"' ");
		this.addSqlWhere(taskId, sql, args, " and a.id=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		sql.append("\n union all ");
		sql.append("\n select a.id,a.parentId,a.taskName,a.bustype,crm.id busid,crm.customername busname,0 stagedItemId, ");
		sql.append("\n null stagedItemName,crm.delstate busDelState,a.delstate ");
		sql.append("\n  from task a left join customer crm on a.busid=crm.id and a.comid=crm.comid ");
		sql.append("\n where a.bustype='"+ConstantInterface.TYPE_CRM+"' ");
		this.addSqlWhere(taskId, sql, args, " and a.id=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		sql.append("\n union all ");
		sql.append("\n select a.id,a.parentId,a.taskName,a.bustype,spflow.id busid,spflow.flowName busname,0 stagedItemId, ");
		sql.append("\n null stagedItemName,0 busDelState,a.delstate ");
		sql.append("\n from task a left join spflowinstance spflow on a.busid=spflow.id and a.comid=spflow.comid ");
		sql.append("\n where a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' ");
		this.addSqlWhere(taskId, sql, args, " and a.id=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		sql.append("\n union all ");
		sql.append("\n select a.id,a.parentId,a.taskName,a.bustype,a.busid,null busname,0 stagedItemId, ");
		sql.append("\n null stagedItemName,1 busDelState,a.delstate ");
		sql.append("\n  from task a ");
		sql.append("\n where a.bustype=0 ");
		this.addSqlWhere(taskId, sql, args, " and a.id=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		return (Task) this.objectQuery(sql.toString(), args.toArray(), Task.class);
	}
	
	/**
	 * 查询模块下是否有相同的附件
	 * @param comId
	 * @param taskId
	 * @param upfileId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskUpfile> listTaskSimUpfiles(Integer comId, Integer taskId,
											   Integer upfileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		//任务模块附件
		sql.append("select a.comId,a.taskId,a.upfileId  from taskUpfile a \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(taskId, sql, args, " and a.taskId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		sql.append("\n union all \n");
		//任务留言附件
		sql.append("select  a.comId,a.taskId,a.upfileId from taskTalkUpfile a\n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(taskId, sql, args, " and a.taskId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		return this.listQuery(sql.toString(), args.toArray(), TaskUpfile.class);
	}


	
/******************************** 任务统计****************************************/
	/**
	 * 我负责的任务统计
	 * @param task
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> chargeTaskCount(Task task,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select countTypeName,countType,count(countType) as counts from ( \n");
		//在修改时有20条数据重复显示所以使用了distinct
		sql.append("select distinct a.id,a.comId,a.taskName,a.owner,a.grade as countType, \n");
		sql.append("dic.zvalue as countTypeName \n");
		sql.append("from task a \n");
		sql.append("left join taskExecutor taskEx on a.comid=taskEx.comid and a.id=taskEx.taskid  \n");
		sql.append("left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0\n");
		sql.append("left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 \n");
		sql.append("left join spflowinstance spflow on a.comid=spflow.comid and a.busid=spflow.id and a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' \n");
		sql.append("left join userinfo g on taskEx.executor = g.id \n");
		sql.append("left join userOrganic gg on g.id =gg.userId and a.comId=gg.comId\n");
		sql.append("left join upfiles h on gg.mediumHeadPortrait = h.id \n");
		sql.append("left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("inner join datadic dic on a.grade = dic.code and dic.type='grade' where a.delstate=0 \n");

		//有查询关联模块信息
		String relateModType = task.getRelateModType();
		if(!StringUtils.isEmpty(relateModType)){
			List<RelateModeVo> modeVos = task.getListRelateModes();
			List<Integer> busIds = new ArrayList<Integer>();
			if(null!=modeVos){
				for (RelateModeVo relateModeVo : modeVos) {
					busIds.add(relateModeVo.getBusId());
				}
				if(ConstantInterface.TYPE_ITEM.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and item.id in ?");
				}else if(ConstantInterface.TYPE_CRM.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and crm.id in ?");
				}else if(ConstantInterface.TYPE_FLOW_SP.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and spflow.id in ?");
				}
			}
		}

		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		List<UserInfo> executors = task.getListExecutor();

		//执行人选择
		if(!CommonUtil.isNull(executors)){
			sql.append("\n and a.state in (0,1) and  exists(  ");
			List<Integer> executorIds = new ArrayList<Integer>();
			for(UserInfo executor : executors){
				executorIds.add(executor.getId());
			}
			sql.append("\n select a.id from taskExecutor taskEx   ");
			sql.append("\n where taskEx.taskId=a.id  ");
			this.addSqlWhereIn(executorIds.toArray(), sql, args, "\n and taskEx.executor in ?");
			if(null!=task.getState()){
				if(task.getState().equals(1)){
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and taskEx.state in ?");
				}else{
					this.addSqlWhere(task.getState(), sql, args, "\n and taskEx.state=?");
				}
			}
			sql.append("	 ) ");
		}

		List<Department> listExecuteDep = task.getListExecuteDep();
		//执行人所在部门
		if(!CommonUtil.isNull(listExecuteDep)){
			List<Integer> depIds = new ArrayList<>();
			for (Department department : listExecuteDep) {
				depIds.add(department.getId());
			}
			sql.append("\n and  exists  ");
			sql.append("\n (");
			sql.append("\n  	select u.id from  userInfo u");
			sql.append("\n  	inner join userOrganic uo on u.id = uo.userId and uo.comId = ? \n");
			args.add(userInfo.getComId());
			sql.append("\n  	inner join department  executeDep on uo.depId = executeDep.Id and  uo.comId = executeDep.comId \n");
			sql.append("\n  	where 1=1 ");
			sql.append("\n  	and exists(");
			sql.append("\n  		select taskEx.id from taskexecutor taskEx ");
			sql.append("\n  		where taskEx.executor=u.id and taskEx.taskid=a.id ");
			sql.append("\n  	)");
			this.addSqlWhereIn(depIds.toArray(), sql, args, "\n start with executeDep.id in ? ");
			sql.append("\n		connect by prior executeDep.id = executeDep.parentid \n");
			sql.append("\n ) and a.state in (0,1)");
		}

		//执行人类型
		String execuType =  task.getExecuType();
		if(null!=execuType && !"".equals(execuType)){
			if("0".equals(execuType)){//查询自己的
				sql.append("\n and exists(");
				sql.append("\n select taskEx.id from taskexecutor taskEx");
				sql.append("\n where taskEx.executor=? and taskEx.taskid=a.id ");
				sql.append("\n )");
				args.add(userInfo.getId());
				this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in  ? ");
			}else if("1".equals(execuType)){//查询下属的

				sql.append("\n and exists(");
				sql.append("\n select taskEx.id from taskexecutor taskEx");
				sql.append("\n where exists(select id from myLeaders where creator = reports.ReporterId and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
				sql.append("\n )");
				this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in  ? ");
			}
		}


		//任务紧急度筛选
		this.addSqlWhere(task.getGrade(), sql, args, "\n and grade=?");

		//负责人的主键集合
		if(null != task.getListOwner() && !task.getListOwner().isEmpty()){
			List<Integer> ownerIds = new ArrayList<Integer>();
			for(UserInfo owner : task.getListOwner()){
				ownerIds.add(owner.getId());
			}
			this.addSqlWhereIn(ownerIds.toArray(), sql, args, "\n and a.owner in ?");
		}
		
		//经办时间
		String operatStartDate = task.getOperatStartDate(); 
		String operatEndDate = task.getOperatEndDate(); 
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		//经办人筛选
		if(!CommonUtil.isNull(task.getListOperator())){
			sql.append("\n and exists (\n");
			sql.append("\n select handOver.id from  taskHandOver handOver  where handOver.comId=? ");
			args.add(userInfo.getComId());
			sql.append("\n and (");
			List<Integer> operatorIds = new ArrayList<Integer>();
			for (UserInfo operator : task.getListOperator()) {
				operatorIds.add(operator.getId());
			}
			this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n handOver.fromUser in ?");
			this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n or handOver.toUser in ?");
			sql.append("\n )");
			if(StringUtils.isNotEmpty(operatStartDate)
					|| StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 		  	and handOver.endTime is not null  ");
			}
			//查询创建时间段
			this.addSqlWhere(operatStartDate, sql, args, " and substr(handOver.recordcreatetime,0,10)>=?");
			//查询创建时间段
			if(StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 		  	and substr(nvl(handOver.endTime,?),0,10)<=?  ");
				args.add(nowDate);
				args.add(operatEndDate);
			}
			
			sql.append("\n and handOver.taskId=a.id");
			if(null!=task.getState()) {
				sql.append("\n	and exists(");
				sql.append("\n	select taskEx.taskId from taskExecutor taskEx where handOver.taskId=taskEx.taskId");
				if (task.getState().equals(1)) {
					sql.append("\n	 and (taskEx.state=0 or taskEx.state=1)");
				}else{
					sql.append("\n	 and taskEx.state=?");
					args.add(task.getState());
				}
				this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n  and taskEx.EXECUTOR in ?");
				sql.append("\n	)");
			}

			sql.append(")\n");
		}else{
			if(StringUtils.isNotEmpty(operatStartDate)
					|| StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n and exists (\n");
				sql.append("\n select handOver.id from  taskHandOver handOver  where handOver.comId=? ");
				args.add(userInfo.getComId());
				sql.append("\n 		  	and handOver.endTime is not null  ");
				//查询创建时间段
				this.addSqlWhere(operatStartDate, sql, args, " and substr(handOver.recordcreatetime,0,10)>=?");
				//查询创建时间段
				if(StringUtils.isNotEmpty(operatEndDate)){
					sql.append("\n 		  	and substr(nvl(handOver.endTime,?),0,10)<=?  ");
					args.add(nowDate);
					args.add(operatEndDate);
				}
				
				sql.append("\n and handOver.taskId=a.id");
				if(null!=task.getState()) {
					sql.append("\n	and exists(");
					sql.append("\n	select taskEx.taskId from taskExecutor taskEx where handOver.taskId=taskEx.taskId");
					if (task.getState().equals(1)) {
						sql.append("\n	 and (taskEx.state=0 or taskEx.state=1)");
					}else{
						sql.append("\n	 and taskEx.state=?");
						args.add(task.getState());
					}
					sql.append("\n	)");
				}

				sql.append(")\n");
			}
		}
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}
		//是否查询逾期任务
		if(null != task.getOverdue() && task.getOverdue()){
			sql.append("\n and ( a.taskLimiteDate < to_char(sysdate,'yyyy-MM-dd')");
			sql.append("\n  or exists(");
			sql.append("\n 		select taskEx.id from taskExecutor taskEx where taskEx.taskid=a.id and taskEx.handTimeLimit < to_char(sysdate,'yyyy-MM-dd')");
			sql.append("\n 	)");
			sql.append("\n )");
		}
		this.addSqlWhere(task.getAttentionState(), sql, args, " and a.attentionState=?");

		sql.append("	and a.owner = ?\n");
		args.add(userInfo.getId());

		//状态筛选
		if(null!=task.getState()){
			switch (task.getState()){
				case 1:
					//进行中
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
					break;
				case 3:
					//挂起，暂停
					this.addSqlWhere(task.getState(), sql, args, "\n and a.state=?");
					break;
				case 4:
					//已完成但不关联经办人并且也不关联执行人。
					if(CommonUtil.isNull(executors) && CommonUtil.isNull(task.getListOperator())){
						this.addSqlWhere(task.getState(), sql, args, "\n and a.state=?");
					}
					break;
				default:
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
					break;
			}
		}
		sql.append(		" )group by countType,countTypeName order by countType desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Task.class);
	}
	/**
	 * 任务紧急度统计
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> allTaskCount(Task task,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//结果集最外取别名
		sql.append("select countTypeName,countType,count(countType) as counts from ( \n");
		sql.append("select * from \n");
		sql.append("( \n");

		sql.append("select distinct a.id,a.grade as countType, \n");
		sql.append("case when dic.ZVALUE is null then '其它' else dic.ZVALUE end as countTypeName, \n");
		sql.append("case when taskEx.handtimelimit is null then a.dealtimelimit else taskEx.handtimelimit end dealtimelimit ,");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState \n");
		sql.append("from task a \n");

		sql.append("left join taskExecutor taskEx on a.comid=taskEx.comid and a.id=taskEx.taskid and taskEx.state in (0,1)  \n");

		sql.append("left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0\n");
		sql.append("left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 \n");
		sql.append("left join spflowinstance spflow on a.comid=spflow.comid and a.busid=spflow.id and a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"' \n");
		sql.append("left join userinfo c on a.owner = c.id \n");
		sql.append("left join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");

		sql.append("left join userinfo g on taskEx.executor = g.id \n");
		sql.append("left join userOrganic gg on g.id =gg.userId and a.comId=gg.comId\n");
		sql.append("left join upfiles h on gg.mediumHeadPortrait = h.id \n");

		sql.append("left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_TASK+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("left join datadic dic on a.grade = dic.code and dic.type='grade' \n");
		sql.append("where a.delstate=0 and  a.comid = ? and a.state <> -1 \n");
		args.add(userInfo.getComId());

		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		//有查询关联模块信息
		String relateModType = task.getRelateModType();
		if(!StringUtils.isEmpty(relateModType)){
			List<RelateModeVo> modeVos = task.getListRelateModes();
			List<Integer> busIds = new ArrayList<Integer>();
			if(null!=modeVos){
				for (RelateModeVo relateModeVo : modeVos) {
					busIds.add(relateModeVo.getBusId());
				}
				if(ConstantInterface.TYPE_ITEM.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and item.id in ?");
				}else if(ConstantInterface.TYPE_CRM.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and crm.id in ?");
				}else if(ConstantInterface.TYPE_FLOW_SP.equals(relateModType)){
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and spflow.id in ?");
				}
			}
		}

		//任务负责人筛选
		List<UserInfo> owners = task.getListOwner();
		if(null!=owners && !owners.isEmpty()){
			List<Integer> ownerIds = new ArrayList<Integer>();
			for (UserInfo owner : owners) {
				ownerIds.add(owner.getId());
			}
			this.addSqlWhereIn(ownerIds.toArray(), sql, args, "\n and a.owner in ?");
		}

		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append("taskEx.executor =? or a.owner=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.id from tasksharer b where a.id = b.taskid and \n");
			sql.append("  	(b.sharerid =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.sharerid and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//执行人上级权限验证
			sql.append("or exists(\n");
			sql.append(" select id from myLeaders where creator = taskEx.executor and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(")\n");
			//负责人上级权限验证
			//执行人上级权限验证
			sql.append("or exists(\n");
			sql.append(" select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(")\n");
			sql.append(") \n");
		}
		List<UserInfo> executors = task.getListExecutor();

		//状态筛选
		if(null!=task.getState()){
			switch (task.getState()){
				case 1:
					//进行中
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
					break;
				case 3:
					//挂起，暂停
					this.addSqlWhere(task.getState(), sql, args, "\n and a.state=?");
					break;
				case 4:
					//已完成但不关联经办人并且也不关联执行人。
					if(CommonUtil.isNull(executors) && CommonUtil.isNull(task.getListOperator())){
						this.addSqlWhere(task.getState(), sql, args, "\n and a.state=?");
					}
					break;
				default:
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ?");
					break;
			}
		}

		//执行人选择
		if(!CommonUtil.isNull(executors)){
			sql.append("\n and a.state in (0,1) and  exists(  ");
			List<Integer> executorIds = new ArrayList<Integer>();
			for(UserInfo executor : executors){
				executorIds.add(executor.getId());
			}
			sql.append("\n select a.id from taskExecutor taskEx   ");
			sql.append("\n where taskEx.taskId=a.id  ");
			this.addSqlWhereIn(executorIds.toArray(), sql, args, "\n and taskEx.executor in ?");
			if(null!=task.getState()){
				if(task.getState().equals(1)){
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and taskEx.state in ?");
				}else{
					this.addSqlWhere(task.getState(), sql, args, "\n and taskEx.state=?");
				}
			}
			sql.append("	 ) ");
		}

		List<Department> listExecuteDep = task.getListExecuteDep();
		//执行人所在部门
		if(!CommonUtil.isNull(listExecuteDep)){
			List<Integer> depIds = new ArrayList<>();
			for (Department department : listExecuteDep) {
				depIds.add(department.getId());
			}
			sql.append("\n and  exists  ");
			sql.append("\n (");
			sql.append("\n  	select u.id from  userInfo u");
			sql.append("\n  	left join userOrganic uo on u.id = uo.userId and uo.comId = ? \n");
			args.add(userInfo.getComId());
			sql.append("\n  	left join department  executeDep on uo.depId = executeDep.Id and  uo.comId = executeDep.comId \n");
			sql.append("\n  	where 1=1 and taskEx.executor=u.id");
			this.addSqlWhereIn(depIds.toArray(), sql, args, "\n start with executeDep.id in ? ");
			sql.append("\n		connect by prior executeDep.id = executeDep.parentid \n");
			sql.append(")\n");
		}
		//经办时间
		String operatStartDate = task.getOperatStartDate(); 
		String operatEndDate = task.getOperatEndDate();
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		//经办人筛选
		if(null!=task.getListOperator() && null != task.getListOperator().get(0)){
			sql.append("\n and exists (\n");
			sql.append("\n select handOver.id from  taskHandOver handOver  where handOver.comId=? ");
			args.add(userInfo.getComId());
			//添加在筛选经办人同时筛选状态的控制
			sql.append("\n and (");
			List<Integer> operatorIds = new ArrayList<Integer>();
			for (UserInfo operator : task.getListOperator()) {
				operatorIds.add(operator.getId());
			}
			this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n handOver.fromUser in ?");
			this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n or handOver.toUser in ?");
			sql.append("\n )");
			if(StringUtils.isNotEmpty(operatStartDate)
					|| StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 		  	and handOver.endTime is not null  ");
			}
			//查询创建时间段
			this.addSqlWhere(operatStartDate, sql, args, " and substr(handOver.recordcreatetime,0,10)>=?");
			//查询创建时间段
			if(StringUtils.isNotEmpty(operatEndDate)){
				sql.append("\n 		  	and substr(nvl(handOver.endTime,?),0,10)<=?  ");
				args.add(nowDate);
				args.add(operatEndDate);
			}
			
			sql.append("\n and handOver.taskId=a.id");
			if(null!=task.getState()) {
				sql.append("\n	and exists(");
				sql.append("\n	select taskEx.taskId from taskExecutor taskEx where handOver.taskId=taskEx.taskId");
				if (task.getState().equals(1)) {
					sql.append("\n	 and (taskEx.state=0 or taskEx.state=1)");
				}else{
					sql.append("\n	 and taskEx.state=?");
					args.add(task.getState());
				}
				this.addSqlWhereIn(operatorIds.toArray(), sql, args, "\n  and taskEx.EXECUTOR in ?");
				sql.append("\n	)");
			}

			sql.append(")\n");
		}else {
			if(StringUtils.isNotEmpty(operatStartDate)
					|| StringUtils.isNotEmpty(operatEndDate) ){
				sql.append("\n and exists (\n");
				sql.append("\n select handOver.id from  taskHandOver handOver  where handOver.comId=? ");
				args.add(userInfo.getComId());
				sql.append("\n 		  	and handOver.endTime is not null ");
				//查询创建时间段
				this.addSqlWhere(operatStartDate, sql, args, " and substr(handOver.recordcreatetime,0,10)>=?");
				//查询创建时间段
				if(StringUtils.isNotEmpty(operatEndDate)){
					sql.append("\n 		  	and substr(nvl(handOver.endTime,?),0,10)<=?  ");
					args.add(nowDate);
					args.add(operatEndDate);
				}
				
				sql.append("\n and handOver.taskId=a.id");
				if(null!=task.getState()) {
					sql.append("\n	and exists(");
					sql.append("\n	select taskEx.taskId from taskExecutor taskEx where handOver.taskId=taskEx.taskId");
					if (task.getState().equals(1)) {
						sql.append("\n	 and (taskEx.state=0 or taskEx.state=1)");
					}else{
						sql.append("\n	 and taskEx.state=?");
						args.add(task.getState());
					}
					sql.append("\n	)");
				}

				sql.append(")\n");
			}
		}
		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}

		//任务紧急度筛选
		this.addSqlWhere(task.getGrade(), sql, args, "\n and grade=?");
		//执行人类型
		String execuType =  task.getExecuType();
		if(null!=execuType && !"".equals(execuType)){
			if("0".equals(execuType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and taskEx.executor=? and a.state in (0,1)");
			}else if("1".equals(execuType)){//查询下属的
				sql.append(" and a.state in (0,1) and exists(select id from myLeaders where creator = taskEx.executor and comId = ? and leader = ? and leader <> creator ) \n");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		sql.append(") a where 1=1 \n");
		this.addSqlWhere(task.getAttentionState(), sql, args, " and a.attentionState=?");

		//是否查询逾期任务
		if(null != task.getOverdue() && task.getOverdue() ){
			sql.append("\n and a.dealtimelimit < to_char(sysdate,'yyyy-MM-dd')");
		}
		sql.append(") group by countType,countTypeName order by countType desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Task.class);
	}
	/**
	 * 待办任务数
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Integer countTodo(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select count(a.id)\n");
		sql.append("from task a \n");
		sql.append("where a.comid = ? and a.state=? and a.executor=? and delstate=0\n");
		args.add(comId);
		args.add(1);
		args.add(userId);
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/******************************** 任务紧急度统计****************************************/
	
	

	/******************************** 任务紧急度统计****************************************/
	

	
	/**
	 * 取得任务参与统计的任务执行人员主键
	 * @param userInfo
	 * @param task
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listExecutor4StatisticOverDue(UserInfo userInfo,
													Task task, Boolean isForceInPersion) {

		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.userId executor from (");
		sql.append("	select a.executor,count(a.executor) value from ( \n");
		sql.append("		select a.id,b.executor,\n");
		sql.append("		case when b.handtimelimit is null then a.dealtimelimit\n");
		sql.append("		else b.handtimelimit end handtimelimit,a.dealtimelimit\n");
		sql.append("		from task a left join taskExecutor b \n");
		sql.append("  		on a.comid=b.comid and a.id=b.taskid and b.state in (0,1)  \n");
		sql.append("   		where a.delstate=0 and a.comid=? and a.state=1    \n");
		args.add(userInfo.getComId());
		sql.append("   		and (b.handtimelimit is not null \n");
		sql.append("   		or (b.handtimelimit is null and a.dealtimelimit is not null ))\n");
		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append("b.executor =? or a.owner=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.id from tasksharer b where a.id = b.taskid and \n");
			sql.append("  	(b.sharerid =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.sharerid and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//执行人上级权限验证
			sql.append("or exists(\n");
			sql.append("select id from myLeaders where creator = b.executor and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(")\n");
			//负责人上级权限验证
			//执行人上级权限验证
			sql.append("or exists(\n");
			sql.append("select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(") \n");
			sql.append(") \n");
		}

		List<UserInfo> executors = task.getListExecutor();
		//执行人选择
		if(!CommonUtil.isNull(executors)){
			sql.append("\n and a.state in (0,1) ");
			List<Integer> executorIds = new ArrayList<Integer>();
			for(UserInfo executor : executors){
				executorIds.add(executor.getId());
			}
			this.addSqlWhereIn(executorIds.toArray(), sql, args, "\n and b.executor in ?");
		}

		List<Department> listExecuteDep = task.getListExecuteDep();
		//执行人所在部门
		if(!CommonUtil.isNull(listExecuteDep)){
			List<Integer> depIds = new ArrayList<>();
			for (Department department : listExecuteDep) {
				depIds.add(department.getId());
			}
			sql.append("\n and  exists  ");
			sql.append("\n (");
			sql.append("\n  	select u.id from  userInfo u");
			sql.append("\n  	inner join userOrganic uo on u.id = uo.userId and uo.comId = ? \n");
			args.add(userInfo.getComId());
			sql.append("\n  	inner join department  executeDep on uo.depId = executeDep.Id and  uo.comId = executeDep.comId \n");
			sql.append("\n  	where 1=1 and b.executor=u.id");
			this.addSqlWhereIn(depIds.toArray(), sql, args, "\n start with executeDep.id in ? ");
			sql.append("\n		connect by prior executeDep.id = executeDep.parentid \n");
			sql.append("\n ) and a.state in (0,1)");
		}
		sql.append("\n		)a ");
		sql.append("\n	where a.handtimelimit<'"+nowDate+"'  group by a.executor");
		sql.append("\n		)a inner join (");
		sql.append("\n		select userId from userorganic where comid=?");
		args.add(userInfo.getComId());
		sql.append("\n 		)b on a.executor=b.userId");

		sql.append("\n 	order by value desc nulls last,b.userId");
		return this.listQuery(sql.toString(), args.toArray(), Task.class);

	}
	/******************************** 任务超期统计****************************************/
	/******************************** 任务分配统计****************************************/
	/**
	 * 任务分配统计
	 * @param userInfo 当前操作人员
	 * @param task 任务的查询条件
	 * @param isForceInPersion 是否为强制参与人
	 * @param executors 执行人主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModStaticVo> listExecutor4Statistic(UserInfo userInfo,
													Task task, Boolean isForceInPersion, List<Integer> executors) {

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//结果集最外取别名
		sql.append("select c.username name,b.userId type,\n");

		sql.append("case when a.value is null then 0 else a.value end value\n");
		sql.append("	from (\n");

		sql.append("		select b.executor,count(a.id) value \n");
		sql.append("		from task a \n");
		sql.append("		inner join taskExecutor b on a.id=b.taskid \n");
		sql.append("   		where a.delstate=0 and a.comid=?  \n");
		args.add(userInfo.getComId());

		if(CommonUtil.isNull(task.getListExecutor())){
			this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and b.state in ?");
		}

		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append("b.executor =? or a.owner=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.id from tasksharer b where a.id = b.taskid and \n");
			sql.append("  	(b.sharerid =? or exists(select sup.leader from myLeaders sup where sup.comid=? and sup.creator=b.sharerid and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//执行人上级权限验证
			sql.append("or exists(\n");
			sql.append("select id from myLeaders where creator = b.executor and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(")\n");
			//负责人上级权限验证
			//执行人上级权限验证
			sql.append("or exists(\n");
			sql.append("select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(") \n");
			sql.append(") \n");
		}
		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		//任务紧急度筛选
		if(null!=task.getGrade() && !"".equals(task.getGrade())){
			sql.append("	and a.grade=? \n");
			args.add(task.getGrade());
		}
		List<UserInfo> executorsT = task.getListExecutor();
		//执行人选择
		if(!CommonUtil.isNull(executorsT)){
			sql.append("\n and  exists(  ");
			List<Integer> executorIds = new ArrayList<Integer>();
			for(UserInfo executor : task.getListExecutor()){
				executorIds.add(executor.getId());
			}
			sql.append("\n select a.id from taskExecutor taskEx   ");
			sql.append("\n where taskEx.taskId=a.id  ");
			this.addSqlWhereIn(executorIds.toArray(), sql, args, "\n and taskEx.executor in ?");
			if(null!=task.getState()){
				if(task.getState().equals(1)){
					this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and taskEx.state in ?");
				}else{
					this.addSqlWhere(task.getState(), sql, args, "\n and taskEx.state=?");
				}
			}
			sql.append("	 ) ");
		}

		List<Department> listExecuteDep = task.getListExecuteDep();
		//执行人所在部门
		if(!CommonUtil.isNull(listExecuteDep)){
			List<Integer> depIds = new ArrayList<>();
			for (Department department : listExecuteDep) {
				depIds.add(department.getId());
			}
			sql.append("\n and  exists  ");
			sql.append("\n (");
			sql.append("\n  	select u.id from  userInfo u");
			sql.append("\n  	inner join userOrganic uo on u.id = uo.userId and uo.comId = ? \n");
			args.add(userInfo.getComId());
			sql.append("\n  	inner join department  executeDep on uo.depId = executeDep.Id and  uo.comId = executeDep.comId \n");
			sql.append("\n  	where 1=1 and b.executor = u.id ");
			this.addSqlWhereIn(depIds.toArray(), sql, args, "\n start with executeDep.id in ? ");
			sql.append("\n		connect by prior executeDep.id = executeDep.parentid \n");
			sql.append("\n ) and a.state in (0,1)");
		}

		sql.append("  group by b.executor\n");
		sql.append(")a right join userorganic b on a.executor=b.userid  \n");
		sql.append("inner join userinfo c on c.id=b.userId \n");
		sql.append("where  1=1  \n");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and b.comid=? ");
		String order = "";
		if(null!=executors && !executors.isEmpty()){
			this.addSqlWhereIn(executors.toArray(), sql, args, " and b.userId in ?");
			for(int i=0;i<executors.size();i++){
				order = order+"'"+executors.get(i)+"'," +(i+1)+",";
			}
		}
		if(order.length()>0){
			order  = order.substring(0, order.length()-1);
			sql.append("\n order by DECODE(b.userid,"+order+")");

		}else{
			sql.append("\n order by b.userid");
		}
		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}

	
	/******************************** 任务分配统计****************************************/
	
	/********************************客户任务统计****************************************/
	/********************************项目任务统计****************************************/
	
	/********************************下属任务统计****************************************/
	

	/**
	 * 获取项目阶段下的所属任务集合
	 * @param comId 团队主键
	 * @param itemId 项目主键 与stagedItemId 不能同时为空
	 * @param stagedItemId 项目阶段主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskByStagedItem(Integer comId, Integer itemId,
										   Integer stagedItemId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select task.id,task.taskname,task.recordcreatetime,task.creator,task.owner, ");
		sql.append("\n task.state,task.dealtimelimit,task.grade,a.itemid busId,a.id stagedItemId");
		sql.append("\n from stagedItem a");
		sql.append("\n inner join stagedInfo b on a.id=b.stageditemid and a.itemid=b.itemid");
		sql.append("\n inner join task on b.moduleid=task.id and b.moduletype='task'");
		sql.append("\n where a.comid=?");
		args.add(comId);
		this.addSqlWhere(itemId, sql, args, "\n and a.itemid=?");
		this.addSqlWhere(stagedItemId, sql, args, "\n and a.id=?");
		sql.append("\n order by a.itemid,a.id ");
		return this.listQuery(sql.toString(), args.toArray(),Task.class);
	}

	/**
	 * 计算任务的耗时计算
	 * @param comId 团队主键
	 * @param taskId 任务主键
	 * @return
	 */
	public Task queryTaskUsedTimes(Integer comId, Integer taskId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select taskRow.taskname,taskRow.state,taskRow.recordcreatetime,taskRow.dealtimelimit,taskRow.handtimelimit,taskRow.curstep,taskRow.endtime,");
		sql.append("\n case when taskRow.State=4 then to_number(to_date(taskRow.endtime, 'yyyy-mm-dd HH24:MI:SS')-to_date(taskRow.recordcreatetime, 'yyyy-mm-dd HH24:MI:SS')) * 24");
		sql.append("\n when taskRow.State=1 then to_number(to_date(to_char(sysdate,'yyyy-mm-dd HH24:MI:SS'), 'yyyy-mm-dd HH24:MI:SS')-to_date(taskRow.recordcreatetime, 'yyyy-mm-dd HH24:MI:SS')) * 24 "
				+ "end usedTimes,");
		sql.append("\n case when taskRow.Dealtimelimit is not null then ");
		sql.append("\n     case when taskRow.State=4 then to_number(to_date(taskRow.endtime, 'yyyy-mm-dd HH24:MI:SS')-to_date(taskRow.Dealtimelimit, 'yyyy-mm-dd HH24:MI:SS')) * 24");
		sql.append("\n     when taskRow.State=1 then to_number(to_date(to_char(sysdate,'yyyy-mm-dd HH24:MI:SS'), 'yyyy-mm-dd HH24:MI:SS')-to_date(taskRow.Dealtimelimit, 'yyyy-mm-dd HH24:MI:SS')) * 24 end");
		sql.append("\n else -1 end overTimes");
		sql.append("\n from (");
		sql.append("\n select a.taskname,a.state,a.recordcreatetime,a.dealtimelimit,b.handtimelimit,b.curstep,b.endtime");
		sql.append("\n from task a ");
		sql.append("\n inner join taskHandOver b on a.id=b.taskid");
		sql.append("\n where a.comid=? and a.id=?");
		args.add(comId);
		args.add(taskId);
		sql.append("\n order by b.recordcreatetime desc");
		sql.append("\n ) taskRow where rownum=1");
		return (Task)this.objectQuery(sql.toString(), args.toArray(), Task.class);
	}



	/**
	 * 统计任务留言数
	 * @param taskId 任务主键
	 * @param comId 团队主键
	 * @return
	 */
	public Integer countTaskMsgs(Integer taskId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(a.id) as msgNum from taskTalk a ");
		sql.append("\n where a.comid=? and a.taskid=?");
		args.add(comId);
		args.add(taskId);
		int totalCount = this.countQuery(sql.toString(), args.toArray());
		return totalCount;
	}

	/**
	 * 统计任务文档数
	 * @param taskId 任务主键
	 * @param comId 团队主键
	 * @return
	 */
	public Integer countTaskDocs(Integer taskId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(a.upfileid) as docNum from( ");
		sql.append("\n select distinct(a.upfileid) as idOfFile,a.* from ");
		sql.append("\n ( ");
		sql.append("\n select task.comid,task.taskname,task.id as taskId,a.upfileid");
		sql.append("\n from taskUpfile a ");
		sql.append("\n inner join task on a.comid = task.comid and a.taskid = task.id");
		sql.append("\n inner join upfiles b on a.comid = b.comid and a.upfileid = b.id ");
		sql.append("\n ) a ");
		sql.append("\n where a.comid=? ");
		args.add(comId);
		sql.append("\n union all ");
		sql.append("\n select distinct(a.upfileid) as idOfFile,a.* from ");
		sql.append("\n ( ");
		sql.append("\n select task.comid,task.taskname,task.id as taskId,a.upfileid");
		sql.append("\n from taskTalkUpfile a ");
		sql.append("\n inner join task on a.comid = task.comid and a.taskid = task.id ");
		sql.append("\n inner join upfiles b on a.comid = b.comid and a.upfileid = b.id ");
		sql.append("\n ) a ");
		sql.append("\n where a.comid=? ");
		args.add(comId);
		sql.append("\n ) a ");
		sql.append("\n where 1=1  ");
		sql.append("\n and  a.taskid in (select a.id from task a where a.comid=? start with a.id=? connect by  prior a.id = a.parentid)  ");
		args.add(comId);
		args.add(taskId);
		int totalCount = this.countQuery(sql.toString(), args.toArray());
		return totalCount;
	}
	/**
	 * 查询任务办结前最后办理人员
	 * @param taskId 任务主键
	 * @param comId 团队主键
	 * @param curStepState 为空则查询办结时的所有办理人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskHandOver> listHandOverForExecute(
			Integer taskId,Integer comId,Integer curStepState) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* ");
		sql.append("\n 	from taskHandOver a");
		sql.append("\n 	where 1=1 ");
		sql.append("\n  and a.taskid=? ");
		args.add(taskId);
		this.addSqlWhere(1, sql, args, "\n and a.curstep=?");
		sql.append("\n order by a.id");
		
		return this.listQuery(sql.toString(), args.toArray(), TaskHandOver.class);
	}
	
	/**
	 * 取得任务的最后执行人
	 * @param taskId
	 * @param comId
	 * @param curStepState
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskHandOver> listHandOverForLastExecute(
			Integer taskId,Integer comId,Integer curStepState) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* ");
		sql.append("\n 	from taskHandOver a");
		sql.append("\n 	where 1=1 ");
		sql.append("\n  and a.taskid=? and PRESTEP = (SELECT max(PRESTEP) from TASKHANDOVER where TASKID = ?) \n");
		args.add(taskId);
		args.add(taskId);
		sql.append("\n order by a.id");
		
		return this.listQuery(sql.toString(), args.toArray(), TaskHandOver.class);
	}
	/**
	 * 查询当前办理人员的办理信息
	 * @param sessionUser 当前操作人员
	 * @param taskId 任务主键
	 * @return
	 */
	public TaskHandOver queryCurUserHandInfo(UserInfo sessionUser,Integer taskId) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* ");
		sql.append("\n 	from taskHandOver a");
		sql.append("\n 	where 1=1  ");
		sql.append("\n  and a.taskid=? and a.toUser=?");
		args.add(taskId);
		args.add(sessionUser.getId());
		this.addSqlWhere(1, sql, args, "\n and a.curstep=?");
		sql.append("\n order by a.id");
		
		return (TaskHandOver) this.objectQuery(sql.toString(), args.toArray(), TaskHandOver.class);
	}
	
	
	
	
	/**
	 * 查询任务本次需要推送的人员
	 * @param taskId 周报主键
	 * @param comId 团队号
	 * @param pushUserIdSet 本次推送的人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPushTodoUserForTask(Integer taskId, Integer comId, Set<Integer> pushUserIdSet){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享发布人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName ");
		sql.append("\n from task d ");
		sql.append("\n inner join userinfo b on d.creator=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comid=d.comid and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comid=?");
		this.addSqlWhere(taskId, sql, args, " and d.id=?");

		sql.append("\n union");
		//分享关注人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
		sql.append("\n from task d inner join attention a on a.busId=d.id and d.comid=a.comid and a.busType = " + ConstantInterface.TYPE_TASK + "");
		sql.append("\n inner join userinfo b on d.creator=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comid=a.comid and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comid=?");
		this.addSqlWhere(taskId, sql, args, " and d.id=?");
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
	 * 查询任务办结前最后办理人员
	 * @param taskId 任务主键
	 * @param comId 团队主键
	 * @param curStepState 为空则查询办结时的所有办理人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskHandOver> listTaskHandOverForExecute(
			Integer taskId,Integer comId,Integer curStepState) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* ");
		sql.append("\n 	from taskHandOver a");
		sql.append("\n 	where a.preStep = (");
		sql.append("\n		select max(b.preStep) from taskHandOver b");
		sql.append("\n  	where b.taskid=?");
		args.add(taskId);
		sql.append("\n 	)  ");
		sql.append("\n  and a.taskid=?");
		args.add(taskId);
		this.addSqlWhere(curStepState, sql, args, "\n and a.curstep=?");
		sql.append("\n order by a.id");

		return this.listQuery(sql.toString(), args.toArray(), TaskHandOver.class);
	}
	/**
	 * 查询当前人员的任务办理时间节点记录
	 * @param sessionUser 当前操作人员
	 * @param taskId 任务主键
	 * @param executor 执行人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskExecuteTime> listTaskExecuteTime(UserInfo sessionUser,
			Integer taskId, Integer executor) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* ");
		sql.append("\n from taskExecuteTime a ");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(taskId, sql, args, "\n and a.taskId=?");
		this.addSqlWhere(executor, sql, args, "\n and a.executor=?");
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), TaskExecuteTime.class);
	}
	/**
	 * 查询当前人员的任务办理时间节点记录
	 * @param sessionUser 当前操作人员
	 * @param taskId 任务主键
	 * @param executor 执行人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskExecuteTime> listTaskExecuteTimeForPause(UserInfo sessionUser,Integer taskId,Integer executor) {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* ");
		sql.append("\n from taskExecuteTime a ");
		sql.append("\n where a.endTime is null");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(taskId, sql, args, "\n and a.taskId=?");
		this.addSqlWhere(executor, sql, args, "\n and a.executor=?");
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), TaskExecuteTime.class);
	}
	
	/**
	 * 查询个人统计信息
	 * @author hcj 
	 * @date: 2018年10月23日 下午2:10:39
	 * @param userInfo
	 * @param task
	 * @return
	 * @throws ParseException 
	 */
	public Task countUserTask(UserInfo userInfo, Task task) throws ParseException {
		StringBuffer sql = new  StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select (SELECT count(1) from task a inner JOIN TASKEXECUTOR b on b.taskId = a.id and b.EXECUTOR = ? ");
		args.add(userInfo.getId());
		sql.append("\n where a.VERSION=2 and a.STATE in(0,1))todoNum,a.*  ");
		sql.append("\n from(SELECT count(1) wjNum,to_char(avg(executeTime),'fm9999999990.0') executeTime from (SELECT avg(nvl(c.COSTTIME,0)) executeTime,a.id FROM task a  ");
		sql.append("\n LEFT JOIN TASKHANDOVER c on c.taskId = a.id  ");
		sql.append("\n where a.VERSION=2 and a.STATE =4 and c.TOUSER=? and a.FINISHTIME >=?  and a.FINISHTIME <=? GROUP BY a.id))a ");
		args.add(userInfo.getId());
		args.add(DateTimeUtil.getFirstDayOfWeek(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss), DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		args.add(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		return (Task) this.objectQuery(sql.toString(), args.toArray(), Task.class);
	}
	
	/**
	 * 任务时限统计
	 * @author hcj 
	 * @date: 2018年10月24日 上午9:06:19
	 * @param task
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> taskToDoCount(Task task, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select countTypeName,countType,count(countType) as counts from ( \n");
		
		sql.append("select case when to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') <0  then 'over' \n");
		sql.append("when to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') <8 and to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') >=0 then 'today' \n");
		sql.append("when to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') <24 and to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') >=8 then 'near' \n");
		sql.append("when to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') >=24  then 'future' \n");
		sql.append("else 'unLimit' end as countType, \n");
		sql.append("case when to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') <0  then '逾期' \n");
		sql.append("when to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') <8 and to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') >=0 then '今天' \n");
		sql.append("when to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') <24 and to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') >=8 then '即将' \n");
		sql.append("when to_char((nvl(taskEx.EXPECTTIME,0)-round(nvl(exeTime.executeTime,0)/60,1)),'fm9999999990.0') >=24  then '正常' \n");
		sql.append("else '无期限' end as countTypeName \n");
		sql.append(" FROM task a \n");

		sql.append("inner join taskExecutor taskEx on a.comid=taskEx.comid and a.id=taskEx.taskid \n");
		//任务执行时间
		sql.append("\n left join  \n");
		sql.append("\n( ");
		sql.append("\n   	SELECT sum(ceil(((To_date(nvl(a.ENDTIME,?) , 'yyyy-mm-dd hh24-mi-ss')  ");
		sql.append("\n   		- To_date(a.RECORDCREATETIME , 'yyyy-mm-dd hh24-mi-ss'))) * 24 * 60)) executeTime, ");
		sql.append("\n		a.TASKID,a.EXECUTOR  ");
		sql.append("\n		from TASKEXECUTETIME a GROUP BY a.TASKID,a.EXECUTOR ");
		sql.append("\n)exeTime on exeTime.taskId=a.id and exeTime.EXECUTOR=taskEx.EXECUTOR  ");
		args.add(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		sql.append("left join item on a.comid=item.comid and a.busid=item.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"' and item.delstate=0\n");
		sql.append("left join customer crm on a.comid=crm.comid and a.busid=crm.id and a.bustype='"+ConstantInterface.TYPE_CRM+"' and crm.delstate=0 \n");
		sql.append("left join spflowinstance spflow on a.comid=spflow.comid and a.busid=spflow.id and a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"'\n");
		sql.append("left join userinfo c on a.owner = c.id \n");
		sql.append("left join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("where a.delstate=0 and a.comid = ? and taskEx.executor=? \n");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.addSqlWhere(task.getVersion(), sql, args, "\n and a.version=?");
		//任务推送人筛选
		List<UserInfo> pushUsers = task.getListFromUser();
		if(null!=pushUsers && !pushUsers.isEmpty()){
			List<Integer> pushUserIds = new ArrayList<Integer>();
			for(UserInfo pushUser : pushUsers){
				pushUserIds.add(pushUser.getId());
			}
			this.addSqlWhereIn(pushUserIds.toArray(), sql, args, "\n and taskEx.pushUser in ?");
		}
		//查询关联的模块信息
		String relateModType = task.getRelateModType();
		if(!StringUtils.isEmpty(relateModType)){
			List<RelateModeVo> modeVos = task.getListRelateModes();
			List<Integer> busIds = new ArrayList<Integer>();
			if(null!=modeVos){
				for (RelateModeVo relateModeVo : modeVos) {
					busIds.add(relateModeVo.getBusId());
				}

				if(ConstantInterface.TYPE_ITEM.equals(relateModType)){
					//关联的项目
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and item.id in ?");
				}else if(ConstantInterface.TYPE_CRM.equals(relateModType)){
					//关联的客户
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and crm.id in ?");
				}else if(ConstantInterface.TYPE_FLOW_SP.equals(relateModType)){
					//关联的审批
					this.addSqlWhereIn(busIds.toArray(), sql, args, "\n and spflow.id in ?");
				}
			}
		}
		//任务状态
		this.addSqlWhereIn(new Object[]{0,1}, sql, args, "\n and a.state in ? ");

		//任务紧急度筛选
		this.addSqlWhere(task.getGrade(), sql, args, "\n and grade=?");

		//查询创建时间段
		this.addSqlWhere(task.getStartDate(), sql, args, " and substr(taskEx.recordcreatetime,0,10)>=?");
		this.addSqlWhere(task.getEndDate(), sql, args, " and substr(taskEx.recordcreatetime,0,10)<=?");

		//任务名筛选
		if(null!=task.getTaskName() && !"".equals(task.getTaskName())){
			this.addSqlWhereLike(task.getTaskName(), sql, args, " and a.taskname like ? \n");
		}
		sql.append(") group by countType,countTypeName order by countType asc \n");
		return this.listQuery(sql.toString(), args.toArray(),Task.class);
	}
}
