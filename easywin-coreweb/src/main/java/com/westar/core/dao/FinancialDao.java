package com.westar.core.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.westar.base.enums.LoanWayEnum;
import com.westar.base.enums.MonthEnum;
import com.westar.base.enums.TripFeeEnum;
import com.westar.base.model.Consume;
import com.westar.base.model.Department;
import com.westar.base.model.FeeBudget;
import com.westar.base.model.FeeBusMod;
import com.westar.base.model.FeeLoan;
import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.FeeLoanReport;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.StatisticFeeCrmVo;
import com.westar.base.pojo.StatisticFeeItemVo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.web.PaginationContext;

@Repository
public class FinancialDao extends BaseDao {
	/**
	 * 获取个人某时间出差记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeBudget> listLoanApplyByDate(UserInfo creator,String date) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*");
		sql.append("\n from feeBudget a");
		sql.append("\n where a.comid=? and a.creator=? and a.status <> "+ConstantInterface.COMMON_DEF);
		args.add(creator.getComId());
		args.add(creator.getId());
		
		sql.append(" and a.startTime<= ? and a.endTime >= ?");
		args.add(date);
		args.add(date);
		return this.listQuery(sql.toString(), args.toArray(),FeeBudget.class);
	}
	
	/**
	 * 分页获取权限下的出差记录列表
	 * @param curUser 当前操作人
	 * @param loanApply 筛选参数
	 * @param isForceInPersion 是否是监督人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeBudget> listPagedLoanApplyOfAuth(UserInfo curUser,
			FeeBudget loanApply,Boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//额度申请主键，申请时间，计划开始时间，计划结束时间，申请人，
		sql.append("\n select a.id,a.recordcreatetime,a.starttime,a.endtime,a.creator,userinfo.username as creatorName,");
		//申请状态，申请额度信息，是否为出差借款，出差地点
		sql.append("\n a.status,nvl(a.allowedQuota,0) allowedQuota,nvl(a.loanFeeTotal,0) loanFeeTotal,nvl(a.loanOffTotal,0) loanOffTotal,nvl(a.loanItemTotal,0) loanItemTotal,");
		sql.append("\n a.isBusinessTrip,a.tripPlace,a.initStatus,");
		//关联流程主键，流程名称，流程办理状态，流程审批状态
		sql.append("\n a.instanceid,tripFlow.flowname,tripFlow.flowstate,tripFlow.spstate,");
		
		//标记是否可以直接销账
		sql.append("\n case when nvl(s.id,0) > 0 then 1 else 0 end canDirectBalance,case when fdb.id is null then 0 else 1 end directBalanceState ");
		//额度申请表
		sql.append("\n from feeBudget a");
		//申请人员
		sql.append("\n inner join userinfo on a.creator=userinfo.id");
		//额度申请流程
		sql.append("\n left join spflowinstance tripFlow on a.instanceid=tripFlow.id and a.comid=tripFlow.comid and tripFlow.flowstate<>0");
		
		//判断是否已经直接销账
		sql.append("\n left join feeDirectBalance fdb on fdb.feeBudgetId = a.id ");
		
		//判断是否可以直接销账
		sql.append("\n left join (SELECT a.id from FEEBUDGET a ");
		sql.append("\n LEFT JOIN FEELOANREPORT b on b.FEEBUDGETID = a.id ");
		sql.append("\n LEFT JOIN SPFLOWINSTANCE c on b.INSTANCEID = c.id ");
		sql.append("\n LEFT JOIN (SELECT a.FEEBUDGETID id from FEELOAN a LEFT JOIN SPFLOWINSTANCE b on b.id = a.instanceId ");
		sql.append("\n	 where a.status not in (4,-1) and b.flowState != 0)d on d.id  = a.id ");
		sql.append("\n where a.ISBUSINESSTRIP = 1 and (b.id is null or c.flowState = 0) and a.status = 4 and a.LOANOFFSTATE = 0 and d.id is null) s on s.id = a.id  ");
		
		sql.append("\n where a.comid=?");
		args.add(curUser.getComId());
		sql.append("\n and a.loanWay=?");
		args.add(LoanWayEnum.QUOTA.getValue());
		
		sql.append("\n and ((a.initStatus=1 and a.instanceid=0) or (a.initStatus=0 and a.instanceid=tripFlow.id))");
		
		String busType = loanApply.getBusType();
		if(StringUtils.isNotEmpty(busType)){
			sql.append("\n and exists(\n");
			sql.append("\n select id from feeBusMod busMod where busMod.feeBudgetId=a.id");
			this.addSqlWhere(loanApply.getBusType(), sql, args, "\n and busMod.busType=?");
			sql.append("\n )");
		}
		//关联模块
		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append("a.creator =? \n");
			args.add(curUser.getId());
			//上级权限验证
			//创建人上级权限验证
//			sql.append("or exists(\n");
//			sql.append("	select sup.id from immediateSuper sup\n");
//			sql.append("	where sup.comid=? and sup.leader=?\n");
//			sql.append("	start with sup.creator=a.creator\n");
//			sql.append("	connect by prior sup.leader = sup.creator\n");
//			sql.append(")\n");
//			args.add(curUser.getComId());
//			args.add(curUser.getId());
			sql.append(") \n");
		}
		
		//申请人筛选
		if(null!=loanApply.getCreator() && loanApply.getCreator()>0){
			this.addSqlWhere(loanApply.getCreator(), sql, args, " and  a.creator=?");//流程发起人筛选
		}
		if(null != loanApply.getListCreator() && !loanApply.getListCreator().isEmpty()){
			List<Integer> listOwnerId = new ArrayList<Integer>();
			for(UserInfo owner : loanApply.getListCreator()){
				listOwnerId.add(owner.getId());
			}
			this.addSqlWhereIn(listOwnerId.toArray(new Object[listOwnerId.size()]), sql, args, "\n and a.creator in ?");
		}
		this.addSqlWhere(loanApply.getIsBusinessTrip(), sql, args, "\n and a.isBusinessTrip=?");
		//销账状态
		Integer loanOffState = loanApply.getLoanOffState();
		if(null!=loanOffState){
			this.addSqlWhere(loanOffState, sql, args, "\n and a.loanOffState = ? and a.status=4");
			
		}else{
			this.addSqlWhere(loanApply.getStatus(), sql, args, "\n and a.status=?");
			
		}
		
		
		
		
		
		if(null!=loanApply.getFlowState() && loanApply.getFlowState()>-1){
			this.addSqlWhere(loanApply.getFlowState(), sql, args, " and tripFlow.flowstate=?");//流程状态筛选
		}
		this.addSqlWhere(loanApply.getSpState(), sql, args, " and tripFlow.spState=?");//审批状态筛选
		this.addSqlWhere(loanApply.getExecutor(), sql, args, " and  flowCurExecutor.executor=?");//流程审批人筛选
		if(null != loanApply.getListExecutor() && !loanApply.getListExecutor().isEmpty()){
			List<Integer> listExecutorId = new ArrayList<Integer>();
			for(UserInfo executor : loanApply.getListExecutor()){
				listExecutorId.add(executor.getId());
			}
			this.addSqlWhereIn(listExecutorId.toArray(), sql, args, "\n and flowCurExecutor.executor in ?");
		}
		//查询创建时间段
		this.addSqlWhere(loanApply.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(loanApply.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		//模糊查询
		String flowName = loanApply.getFlowName();
		if(!StringUtils.isEmpty(flowName)){
			sql.append("\n and ( tripFlow.flowName like '%"+flowName+"%'");
			sql.append("\n 	or exists(");
			sql.append("\n 	select id from feeBusMod mod where mod.feeBudgetId=a.id and mod.tripBusName like '%"+flowName+"%' ");
			sql.append("\n 	and mod.loanWay=?");
			args.add(LoanWayEnum.QUOTA.getValue());
			sql.append("\n 	)");
			sql.append("\n )");
		}
		String orderBy ="";//列表排序
		orderBy =" tripFlow.recordcreatetime desc,tripFlow.flowState";
		if(null==loanApply.getOrderBy() || "".equals(loanApply.getOrderBy())){
		}else if("executor".equals(loanApply.getOrderBy())){
			orderBy =" curSpUser.username";
		}else if("crTimeNewest".equals(loanApply.getOrderBy())){
			orderBy =" tripFlow.recordCreateTime desc";
		}else if("crTimeOldest".equals(loanApply.getOrderBy())){
			orderBy =" tripFlow.recordCreateTime asc";
		}
		//总数
		String countQueryString = "select  count(*) from ( "+sql+" ) a";
		Integer count = this.countQuery(countQueryString.toString(), args.toArray());
		PaginationContext.setTotalCount(count);
		
		
		StringBuffer pagedQueryString = new StringBuffer();
		pagedQueryString.append("select a.*,b.busId,b.busType,b.tripBusName");
		pagedQueryString.append("\n from (");
		pagedQueryString.append("\nselect outer.*  from (");
		pagedQueryString.append("\nselect inner.*,rownum as rowno from( \n");
		pagedQueryString.append(sql).append(" order by" + orderBy);
		pagedQueryString.append("\n) inner where rownum <=").append(PaginationContext.getOffset() + PaginationContext.getPageSize());
		pagedQueryString.append("\n) outer where outer.rowno>").append(PaginationContext.getOffset());
		//关联额度借款表
		pagedQueryString.append("\n)a left join feeBusMod b on a.id=b.feeBudgetId and b.loanWay=?");
		args.add(LoanWayEnum.QUOTA.getValue());
		pagedQueryString.append("\n order by a.rowno,b.busid desc");
		return this.listQuery(pagedQueryString.toString(), args.toArray(), FeeBudget.class);
	}

	/**
	 * 分页获取个人借款记录
	 * @param curUser 当前操作人
	 * @param loan 借款筛选参数
	 * @param isForceInPersion 是否是监督人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoan> listPagedLoanOfAuth(UserInfo curUser, FeeLoan loan,Boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select apy.* from (");
		sql.append("\n 		select distinct apy.id feeBudgetId ,apy.recordCreateTime apyDateTime,apy.isBusinessTrip,apy.loanway, ");
		sql.append("\n 		apy.comId,apy.instanceId loanApplyInsId,apyFlow.flowname loanApplyInsName,apy.initStatus apyInitStatus, ");
		sql.append("\n 		apy.status loanApplyState,apy.starttime loanApplyStartDate,apy.endtime loanApplyEndDate,");
		sql.append("\n 		u.userName creatorName,apy.creator,");
		//剩余额度
		sql.append("\n 		apy.allowedQuota - apy.loanFeeTotal as allowedQuota,");
		//标记是否可以直接销账
		sql.append("\n case when nvl(dbs.id,0) > 0 then 1 else 0 end canDirectBalance,case when fdb.id is null then 0 else 1 end directBalanceState ");
		
		sql.append("\n 		from feebudget apy");
		sql.append("\n 		inner join userInfo u on apy.creator=u.id");
		sql.append("\n 		left join spflowInstance apyFlow on apy.instanceId=apyFlow.id and apy.comId=apyFlow.comId");
		sql.append("\n  	left join FEELOAN f on f.FEEBUDGETID = apy.id ");
		sql.append("\n  	LEFT JOIN SPFLOWINSTANCE s on s.id = f.instanceid ");
		//判断是否已经直接销账
		sql.append("\n left join feeDirectBalance fdb on fdb.feeBudgetId = apy.id ");
		//判断是否可以直接销账
		sql.append("\n left join (SELECT a.id from FEEBUDGET a ");
		sql.append("\n LEFT JOIN FEELOANOFF b on b.FEEBUDGETID = a.id ");
		sql.append("\n LEFT JOIN SPFLOWINSTANCE c on b.INSTANCEID = c.id ");
		sql.append("\n LEFT JOIN (SELECT a.FEEBUDGETID id from FEELOAN a LEFT JOIN SPFLOWINSTANCE b on b.id = a.instanceId ");
		sql.append("\n	 where a.status not in (4,-1) and b.flowState != 0)d on d.id  = a.id ");
		sql.append("\n where a.ISBUSINESSTRIP = 0 and (b.id is null or c.flowState = 0) and a.status = 4 and a.LOANOFFSTATE = 0 and d.id is null and a.loanWay = 1) dbs on dbs.id = apy.id  ");
		
		sql.append("\n  	where apy.comId=? and (nvl(apyFlow.id,0) != 0 or nvl(s.id,0) != 0) ");
		args.add(curUser.getComId());
		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("	and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" 		apy.creator =? \n");
			args.add(curUser.getId());
			//上级权限验证
			//创建人上级权限验证
//			sql.append("			or exists(\n");
//			sql.append("				select sup.id from immediateSuper sup\n");
//			sql.append("				where sup.comid=? and sup.leader=?\n");
//			sql.append("				start with sup.creator=apy.creator\n");
//			sql.append("				connect by prior sup.leader = sup.creator\n");
//			sql.append("			)\n");
//			args.add(curUser.getComId());
//			args.add(curUser.getId());
			sql.append("	) \n");
		}
		//监督人员排除别人的一般借款草稿
		sql.append("\n 		and apy.id not in ( ");
		sql.append("\n 		SELECT b.id from FEELOAN f ");
		sql.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		sql.append("\n 		where f.comid=? and f.STATUS = 0 and f.CREATOR != ? and s.FLOWSTATE= 2 ");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		sql.append("\n 		and b.loanway=? and nvl(b.INSTANCEID ,0)= 0 ");
		args.add(LoanWayEnum.DIRECT.getValue());
		//排除借款异常数据
		sql.append("\n 		union ");
		sql.append("\n 		SELECT b.id from FEELOAN f ");
		sql.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		sql.append("\n 		where f.comid=? and f.STATUS = 0  and s.FLOWSTATE= 0 and b.ISBUSINESSTRIP=0 and b.LOANWAY=2");
		args.add(curUser.getComId());
		//排除别人借款失败的数据
		sql.append("\n 		union select f.id from FEEBUDGET f where f.STATUS = -1 and f.creator != ?");
		args.add(curUser.getId());
		sql.append("\n 		union SELECT f.id from FEEBUDGET f INNER JOIN ");
		sql.append("\n 			(SELECT FEEBUDGETID id,count(1) from feeloan where status=-1 GROUP BY FEEBUDGETID having count(1) >0) fe on fe.id = f.id");
		sql.append("\n 		 	where f.creator != ?  ");
		args.add(curUser.getId());
		if(null==loan.getStatus() || !loan.getStatus().equals(-1)) {
			sql.append("\n 		and f.id not in (SELECT FEEBUDGETID id from feeloan where status !=-1 GROUP BY FEEBUDGETID having count(1) >0) ");
		}
		sql.append("\n 		) ");
		//借款范围
		this.addSqlWhereIn(new Object[]{LoanWayEnum.QUOTA.getValue(),LoanWayEnum.DIRECT.getValue(),LoanWayEnum.RELATEITEM.getValue()},
				sql, args, "\n and apy.loanway in ?");
		//借款类型
		this.addSqlWhere(loan.getIsBusinessTrip(), sql, args, "\n and apy.isBusinessTrip=?");
		//申请人筛选
		if(null!=loan.getCreator() && loan.getCreator()>0){
			this.addSqlWhere(loan.getCreator(), sql, args, " and  apy.creator=?");//流程发起人筛选
		}
		if(null != loan.getListCreator() && !loan.getListCreator().isEmpty()){
			List<Integer> listCreatorId = new ArrayList<Integer>();
			for(UserInfo owner : loan.getListCreator()){
				listCreatorId.add(owner.getId());
			}
			this.addSqlWhereIn(listCreatorId.toArray(new Object[listCreatorId.size()]), sql, args, "\n and apy.creator in ? ");
		}
		
		//报销状态
		Integer  status = loan.getStatus();
		if(null!=status ){
			switch(status){
				case 5://已领款
					sql.append("\n and exists(");
					sql.append("\n 		select id from feeloan ");
					sql.append("\n 		where feeloan.feeBudgetId=apy.id and feeloan.balanceState=1");
					sql.append("\n 	)");
					break;
				case 4://待领款
					sql.append("\n and exists(");
					sql.append("\n 		select id from feeloan ");
					sql.append("\n 		where feeloan.feeBudgetId=apy.id and (feeloan.balanceState=0 or feeloan.balanceState is null) and feeloan.status=4");
					sql.append("\n 	)");
					break;
				case 3://待借款
					sql.append("\n and exists(");
					sql.append("\n 		select feeApy.id from feebudget feeApy ");
					sql.append("\n 		left join feeloan on feeApy.id=feeloan.feeBudgetId  ");
					sql.append("\n 		where feeApy.id=apy.id and feeloan.status=4 and nvl(feeloan.status,0)=0");
					sql.append("\n 	)");
					break;
				case 1://借款中
					sql.append("\n and ( exists(");
					sql.append("\n 		select feeApy.id from feebudget feeApy ");
					sql.append("\n 		where feeApy.id=apy.id and feeApy.status=1 ");
					sql.append("\n 		union all ");
					sql.append("\n 		select feeApy.id from feebudget feeApy ");
					sql.append("\n 		left join feeloan on feeApy.id=feeloan.feeBudgetId");
					sql.append("\n 		where feeApy.id=apy.id and (nvl(feeloan.status,0)=0 or nvl(feeloan.status,0)=1) ");
					sql.append("\n 	)");
					sql.append("\n 		or exists(");
					sql.append("\n 		select id from feeloan ");
					sql.append("\n 		where feeloan.feeBudgetId=apy.id and (feeloan.balanceState=0 or feeloan.balanceState is null) and feeloan.status=4");
					sql.append("\n 	) ");
					//自己的草稿
					sql.append("\n or apy.id in( ");
					sql.append("\n 	SELECT f.id from FEEBUDGET  f");
					sql.append("\n 	left join SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
					sql.append("\n 	 where f.LOANWAY = 1 and f.CREATOR = ? and f.comid = ? and nvl(s.flowState,0)=2 and  ISBUSINESSTRIP=0");
					args.add(curUser.getId());
					args.add(curUser.getComId());
					sql.append("\n 	 UNION SELECT f.id from FEELOAN l ");
					sql.append("\n 	 left join FEEBUDGET  f on f.id = l.FEEBUDGETID ");
					sql.append("\n 	left join SPFLOWINSTANCE s on s.id = l.INSTANCEID ");
					sql.append("\n 	 where  l.CREATOR = ? and l.comid = ? and nvl(s.flowState,0)=2 ");
					args.add(curUser.getId());
					args.add(curUser.getComId());
					sql.append("\n 	 ) ) and apy.status !=-1");
					break;
				default:
					sql.append("\n and exists(");
					sql.append("\n 		select feeApy.id from feebudget feeApy ");
					sql.append("\n 		where feeApy.id=apy.id and feeApy.status=-1 ");
					sql.append("\n 		union all ");
					sql.append("\n 		select id from feeloan ");
					sql.append("\n 		where feeloan.feeBudgetId=apy.id and feeloan.status=-1 ");
					sql.append("\n 	)");
					break;
			}
		}
		
		//排除草稿
		sql.append("\n 	and ( (not exists(");
		//排除草稿
		sql.append("\n 		select feeApy.id from feebudget feeApy");
		sql.append("\n 		where feeApy.id=apy.id and feeApy.status=0");
		//排除直接借款草稿
		sql.append("\n 		union all");
		sql.append("\n 		select feeApy.id from feebudget feeApy");
		sql.append("\n 		inner join feeloan on feeApy.id=feeloan.feeBudgetId ");
		sql.append("\n 		where feeApy.id=apy.id and feeApy.status=0");
		this.addSqlWhereIn(new Object[]{LoanWayEnum.DIRECT.getValue(),LoanWayEnum.RELATEITEM.getValue()},
				sql, args, "\n and feeApy.loanway in ?");
		//排除直接借款草稿
		sql.append("\n 		union all");
		sql.append("\n 		select feeApy.id from feebudget feeApy");
		sql.append("\n 		where feeApy.id=apy.id and feeApy.status<>4 and feeApy.isBusinessTrip=1");
		this.addSqlWhereIn(new Object[]{LoanWayEnum.QUOTA.getValue()},
				sql, args, "\n and feeApy.loanway in ?");
		//排除出差借款审核通过，但是没有一次借款的
		sql.append("\n 		union all");
		sql.append("\n 		select feeApy.id from feebudget feeApy");
		sql.append("\n 		left join feeloan on feeApy.id=feeloan.feeBudgetId ");
		sql.append("\n 		where feeApy.id=apy.id and feeApy.status=4 and nvl(feeloan.status,0)=0");
		sql.append("\n 		 and feeApy.isBusinessTrip=1 ");
		this.addSqlWhereIn(new Object[]{LoanWayEnum.QUOTA.getValue()},
				sql, args, "\n and feeApy.loanway in ?");
		sql.append("\n 		minus ");
		sql.append("\n 		select feeApy.id from feebudget feeApy");
		sql.append("\n 		left join feeloan on feeApy.id=feeloan.feeBudgetId ");
		sql.append("\n 		where feeApy.id=apy.id and feeApy.status=4");
		sql.append("\n 		 and feeApy.isBusinessTrip=1 ");
		this.addSqlWhereIn(new Object[]{LoanWayEnum.QUOTA.getValue(),},
				sql, args, "\n and feeApy.loanway in ?");
		sql.append("\n 		 group by feeApy.id having count(feeApy.id)>1 ");
		sql.append("\n 	)) ");
		//查询出个人的草稿
		sql.append("\n or apy.id in( ");
		sql.append("\n 	SELECT f.id from FEEBUDGET  f");
		sql.append("\n 	left join SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 	 where f.LOANWAY = 1 and f.CREATOR = ? and f.comid = ? and nvl(s.flowState,0)=2 and  ISBUSINESSTRIP=0");
		args.add(curUser.getId());
		args.add(curUser.getComId());
		sql.append("\n 	 UNION SELECT f.id from FEELOAN l ");
		sql.append("\n 	 left join FEEBUDGET  f on f.id = l.FEEBUDGETID ");
		sql.append("\n 	left join SPFLOWINSTANCE s on s.id = l.INSTANCEID ");
		sql.append("\n 	 where  l.CREATOR = ? and l.comid = ? and nvl(s.flowState,0)=2 ");
		args.add(curUser.getId());
		args.add(curUser.getComId());
		sql.append("\n 	 ) )");
		
				
		//模糊查询
		String flowName = loan.getFlowName();
		//查询创建时间段
		String startDate = loan.getStartDate();
		String endDate = loan.getEndDate();
		if(StringUtils.isNotEmpty(flowName) 
				|| StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)){
			sql.append("\n 	and (  ");
			sql.append("\n 	exists( ");
			sql.append("\n 		select feeApy.id from feebudget feeApy ");
			sql.append("\n 		inner join spflowInstance apyFlow on feeApy.instanceId=apyFlow.id and feeApy.comId=apyFlow.comId");
			sql.append("\n 		where feeApy.id=apy.id ");
			this.addSqlWhereLike(flowName, sql, args, "\n and apyFlow.flowname like ? ");
			//查询创建时间段
			this.addSqlWhere(startDate, sql, args, " and substr(apyFlow.recordcreatetime,0,10)>=?");
			this.addSqlWhere(endDate, sql, args, " and substr(apyFlow.recordcreatetime,0,10)<=?");
			
			sql.append("\n 		union all ");
			sql.append("\n 		select feeloan.id from feeloan ");
			sql.append("\n 		inner join spflowInstance loanFlow on feeloan.instanceId=loanFlow.id and feeloan.comId=loanFlow.comId");
			sql.append("\n 		where feeloan.feeBudgetId=apy.id ");
			this.addSqlWhereLike(flowName, sql, args, "\n and loanFlow.flowname like ? ");
			this.addSqlWhere(startDate, sql, args, " and substr(loanFlow.recordcreatetime,0,10)>=?");
			this.addSqlWhere(endDate, sql, args, " and substr(loanFlow.recordcreatetime,0,10)<=?");
			sql.append("\n 		)");
			sql.append("\n 	)");
		}
		sql.append("\n ) apy where 1=1 ");
		sql.append("\n order by apy.apyDateTime desc,apy.feeBudgetId desc");
		
		//总数
		StringBuffer countQueryString = new StringBuffer("select  count(*) from ( "+sql+" ) a");
		
		Integer count = this.countQuery(countQueryString.toString(), args.toArray());
		PaginationContext.setTotalCount(count);
		StringBuffer pagedQueryString = new StringBuffer(); 
		pagedQueryString.append("\n select apy.*,feeloan.id,nvl(feeloan.recordCreateTime,apy.apyDateTime) recordCreateTime,");
		pagedQueryString.append("\n feeloan.instanceId,feeloan.balanceState,feeloan.sendNotice,nvl(feeloan.loanMoney,0) loanMoney,");
		pagedQueryString.append("\n loanSpflow.flowName,feeloan.status,nvl(feeloan.borrowingBalance,0) borrowingBalance");
		pagedQueryString.append("\n from (");
		pagedQueryString.append("\n		select outer.* from (");
		pagedQueryString.append("\n			select inner.*,rownum as rowno from(");
		pagedQueryString.append(sql);
		pagedQueryString.append("\n 			) inner where rownum <=").append(PaginationContext.getOffset() + PaginationContext.getPageSize());
		pagedQueryString.append("\n 		) outer where outer.rowno>").append(PaginationContext.getOffset());
		pagedQueryString.append("\n )apy left join feeloan on apy.feeBudgetId=feeloan.feeBudgetId ");
		//排除异常数据、他人草稿、他人失败记录
		pagedQueryString.append("\n 		and feeloan.id not in (SELECT f.id from FEELOAN f ");
		pagedQueryString.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		pagedQueryString.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		pagedQueryString.append("\n 		where f.comid=? and ((f.STATUS = 0 and ((s.FLOWSTATE= 0)or (s.FLOWSTATE= 2 and s.creator !=?)))  or (f.status=-1 and s.creator !=?)) )");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		args.add(curUser.getId());
		pagedQueryString.append("\n left join spflowinstance loanSpflow on loanSpflow.id=feeloan.instanceId and loanSpflow.FLOWSTATE<>0");
		pagedQueryString.append("\n where 1=1 and (nvl(loanSpflow.id,0) > 0 or nvl(apy.loanApplyInsId,0) > 0)");
		
		
		if(null!=status ){
			switch(status){
				case 5://已领款
					pagedQueryString.append("\n and nvl(feeloan.balanceState,0)=1");
					break;
				case 4://待领款
					pagedQueryString.append("\n and apy.loanApplyState=4 and nvl(feeloan.balanceState,0)=0");
					break;
				case 3://待借款
					pagedQueryString.append("\n and apy.loanApplyState=4 and nvl(feeloan.status,0)=0");
					break;
				case 1://借款中
					pagedQueryString.append("\n and ( ( (apy.loanApplyState=1 or (apy.loanApplyState=4 and nvl(feeloan.status,0) in (0,1))) or (apy.loanApplyState=4 and nvl(feeloan.balanceState,0)=0))");
					//自己的草稿
					pagedQueryString.append("\n or apy.feeBudgetId in( ");
					pagedQueryString.append("\n 	SELECT f.id from FEEBUDGET  f");
					pagedQueryString.append("\n 	left join SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
					pagedQueryString.append("\n 	 where f.LOANWAY = 1 and f.CREATOR = ? and f.comid = ? and nvl(s.flowState,0)=2 and  ISBUSINESSTRIP=0");
					args.add(curUser.getId());
					args.add(curUser.getComId());
					pagedQueryString.append("\n 	 UNION SELECT f.id from FEELOAN l ");
					pagedQueryString.append("\n 	 left join FEEBUDGET  f on f.id = l.FEEBUDGETID ");
					pagedQueryString.append("\n 	left join SPFLOWINSTANCE s on s.id = l.INSTANCEID ");
					pagedQueryString.append("\n 	 where  l.CREATOR = ? and l.comid = ? and nvl(s.flowState,0)=2 ");
					args.add(curUser.getId());
					args.add(curUser.getComId());
					pagedQueryString.append("\n 	 ) )");
					break;
				default:
					pagedQueryString.append("\n and (apy.loanApplyState=-1 or  nvl(feeloan.status,0)=-1)");
					break;
			}
		}
		//排除异常数据
		pagedQueryString.append("\n 		and apy.feeBudgetId not in (SELECT b.id from FEELOAN f ");
		pagedQueryString.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		pagedQueryString.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		pagedQueryString.append("\n 		where f.comid=? and f.STATUS = 0 and s.FLOWSTATE= 0 and b.ISBUSINESSTRIP=0 and b.LOANWAY=2)");
		args.add(curUser.getComId());
		//排除草稿
		pagedQueryString.append("\n order by apy.apyDateTime desc,apy.feeBudgetId desc");
		return this.listQuery(pagedQueryString.toString(), args.toArray(), FeeLoan.class);
	}
	/**
	 * 获取个人所有借款记录
	 * @param userInfo 当前操作人
	 * @param feeBudgetId 申请记录主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoan> listLoanForQuota(UserInfo userInfo,Integer feeBudgetId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//借款主键，借款流程创建时间，额度申请主键，审核状态，申请人主键
		sql.append("\n select a.id,substr(a.Recordcreatetime,0,16) recordcreatetime,a.feeBudgetId,a.status,a.creator,");
		//借款金额,借款中金额,是否为初始化
		sql.append("\n nvl(a.borrowingBalance,0) borrowingBalance,nvl(a.loanMoney,0) loanMoney,a.initStatus,");
		//借款流程主键，借款流程名称，借款流程状态，借款审核状态，申请人名称
		sql.append("\n a.instanceid,loanFlow.flowname,loanFlow.flowstate,loanFlow.spstate,userinfo.username as creatorName,");
		//流程执行人员主键，流程执行人员名称，流程执行人员性别
		sql.append("\n flowCurExecutor.executor,curSpUser.username as executorName");
		//借款表
		sql.append("\n from feeloan a");
		//借款流程
		sql.append("\n left join spflowinstance loanFlow on a.instanceid=loanFlow.id and a.comid=loanFlow.comid");
		//借款人信息
		sql.append("\n left join userinfo on a.creator=userinfo.id");
		//流程执行人信息
		sql.append("\n left join spFlowCurExecutor flowCurExecutor on loanFlow.comId = flowCurExecutor.comid ");
		sql.append("\n and loanFlow.id = flowCurExecutor.busId and flowCurExecutor.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n left join userinfo curSpUser on flowCurExecutor.Executor=curSpUser.id");
		
		sql.append("\n where a.comid=? and a.creator=? and a.status<>"+ConstantInterface.COMMON_DEF+"");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		if(!CommonUtil.isNull(feeBudgetId)){
			this.addSqlWhere(feeBudgetId, sql, args, " and a.feeBudgetId=?");//申请记录主键
		}
		sql.append("\n order by a.Recordcreatetime desc,a.status desc");
		return this.listQuery(sql.toString(),args.toArray(),FeeLoan.class);
	}
	/**
	 * 获取个人所有借款记录
	 * @param userInfo 当前操作人
	 * @param feeBudgetId 申请记录主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanReport> listLoanReport(UserInfo userInfo,Integer feeBudgetId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.recordcreatetime,a.feeBudgetId,a.status,a.creator,");
		sql.append("\n a.instanceid,loanFlow.flowname,loanFlow.flowstate,loanFlow.spstate,userinfo.username as creatorName,");
		sql.append("\n flowCurExecutor.executor,curSpUser.username as executorName");
		sql.append("\n from feeloanReport a");
		sql.append("\n inner join spflowinstance loanFlow on a.instanceid=loanFlow.id and a.comid=loanFlow.comid");
		sql.append("\n inner join userinfo on a.creator=userinfo.id");
		sql.append("\n left join spFlowCurExecutor flowCurExecutor on loanFlow.comId = flowCurExecutor.comid ");
		sql.append("\n 	and loanFlow.id = flowCurExecutor.busId and flowCurExecutor.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n left join userinfo curSpUser on flowCurExecutor.Executor=curSpUser.id");
		sql.append("\n where a.comid=? and a.creator=? and a.status<>"+ConstantInterface.COMMON_DEF+"");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		if(!CommonUtil.isNull(feeBudgetId)){
			this.addSqlWhere(feeBudgetId, sql, args, " and a.feeBudgetId=?");//申请记录主键
		}
		return this.listQuery(sql.toString(),args.toArray(),FeeLoanReport.class);
	}
	/**
	 * 分页获取报销记录
	 * @param curUser 当前操作人
	 * @param loanOff 报销筛选条件
	 * @param isForceInPersion 是否是监督人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanOff> listLoanOffOfAuthV2(UserInfo curUser,
			FeeLoanOff loanOff,Boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select apy.* from (");
		sql.append("\n 		select apy.id feeBudgetId ,apy.recordCreateTime apyDateTime,apy.isBusinessTrip,apy.loanway, ");
		sql.append("\n 		apy.comId,apy.instanceId loanApplyInsId,apyFlow.flowname loanApplyName,apy.initStatus apyInitStatus,");
		sql.append("\n 		u.userName creatorName,apy.creator,apy.loanFeeTotal");
		sql.append("\n 		from feebudget apy");
		sql.append("\n 		inner join userInfo u on apy.creator=u.id");
		sql.append("\n 		left join spflowInstance apyFlow on apy.instanceId=apyFlow.id and apy.comId=apyFlow.comId");
		sql.append("\n  	where apy.comId=? and apy.status=4");
		args.add(curUser.getComId());
		//查看权限范围界定
		if(!isForceInPersion){
			sql.append("	and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" 		apy.creator =? \n");
			args.add(curUser.getId());
			//上级权限验证
			//创建人上级权限验证
//			sql.append("			or exists(\n");
//			sql.append("				select sup.id from immediateSuper sup\n");
//			sql.append("				where sup.comid=? and sup.leader=?\n");
//			sql.append("				start with sup.creator=apy.creator\n");
//			sql.append("				connect by prior sup.leader = sup.creator\n");
//			sql.append("			)\n");
//			args.add(curUser.getComId());
//			args.add(curUser.getId());
			sql.append("	) \n");
		}
		//监督人员排除别人的一般报销草稿
		sql.append("\n 		and apy.id not in ( ");
		sql.append("\n 		SELECT b.id from FEELOANOFF f ");
		sql.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		sql.append("\n 		where f.comid=? and f.STATUS = 0 and f.CREATOR != ? and s.FLOWSTATE= 2 ");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		sql.append("\n 		and f.LOANREPORTID=0 and nvl(b.INSTANCEID ,0)= 0 ");
		sql.append("\n 		union ");
		sql.append("\n 		SELECT b.id from FEELOANOFF f ");
		sql.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		sql.append("\n 		where f.comid=? and f.STATUS = 0  and s.FLOWSTATE= 0 and b.ISBUSINESSTRIP=0");
		args.add(curUser.getComId());
		//排除别人的汇报草稿
		sql.append("\n 		union SELECT rep.FEEBUDGETID from FEELOANREPORT rep LEFT JOIN SPFLOWINSTANCE s on s.id = rep.INSTANCEID where s.FLOWSTATE = 2 and rep.creator != ?");
		args.add(curUser.getId());
		sql.append("\n 			and rep.FEEBUDGETID not in(SELECT rep.FEEBUDGETID from FEELOANREPORT rep LEFT JOIN SPFLOWINSTANCE s on s.id = rep.INSTANCEID where s.FLOWSTATE != 2) ");
		//排除别人报销失败的数据
		sql.append("\n 		union select f.id from FEEBUDGET f where f.STATUS = -1 and f.creator != ?");
		
		args.add(curUser.getId());
		sql.append("\n 		union SELECT FEEBUDGETID from FEELOANOFF where status = -1 and creator != ? ");
		args.add(curUser.getId());
		if(null==loanOff.getStatus() ||  loanOff.getStatus() != -1) {
			sql.append("\n 		and FEEBUDGETID not in(SELECT FEEBUDGETID from FEELOANOFF where status != -1  ) ");
		}
		sql.append("\n 		union SELECT FEEBUDGETID from FEELOANREPORT where status = -1 and creator != ? ");
		args.add(curUser.getId());
		if(null==loanOff.getStatus() ||  loanOff.getStatus() != -1) {
			sql.append("\n 		and FEEBUDGETID not in(SELECT FEEBUDGETID from FEELOANREPORT where status != -1  ) ");
		}
		sql.append("\n 	) ");
		
		
		//报销范围
		this.addSqlWhereIn(new Object[]{LoanWayEnum.QUOTA.getValue(),LoanWayEnum.DIRECTOFF.getValue(),LoanWayEnum.ITEMOFF.getValue()},
				sql, args, "\n and apy.loanway in ? ");
		//报销类型
		this.addSqlWhere(loanOff.getIsBusinessTrip(), sql, args, "\n and apy.isBusinessTrip=?");
		//申请人筛选
		if(null!=loanOff.getCreator() && loanOff.getCreator()>0){
			this.addSqlWhere(loanOff.getCreator(), sql, args, " and  apy.creator=?");//流程发起人筛选
		}
		if(null != loanOff.getListCreator() && !loanOff.getListCreator().isEmpty()){
			List<Integer> listCreatorId = new ArrayList<Integer>();
			for(UserInfo owner : loanOff.getListCreator()){
				listCreatorId.add(owner.getId());
			}
			this.addSqlWhereIn(listCreatorId.toArray(new Object[listCreatorId.size()]), sql, args, "\n and apy.creator in ? ");
		}
		//报销状态
		Integer  status = loanOff.getStatus();
		if(null!=status ){
			switch(status){
				case 5://已结算
					sql.append("\n and exists(");
					sql.append("\n 		select id from feeloanoff ");
					sql.append("\n 		where feeloanoff.feeBudgetId=apy.id and feeloanoff.balanceState=1");
					sql.append("\n 	)");
					break;
				case 4://待领款
					sql.append("\n and exists(");
					sql.append("\n 		select id from feeloanoff ");
					sql.append("\n 		where feeloanoff.feeBudgetId=apy.id and (feeloanoff.balanceState=0 or feeloanoff.balanceState is null) and feeloanOff.status=4");
					sql.append("\n 	)");
					break;
				case 1://报销中
					sql.append("\n and ( exists(");
					sql.append("\n 		select feeApy.id from feebudget feeApy ");
					sql.append("\n 		inner join feeloanreport loanrep on feeApy.id=loanrep.feeBudgetId ");
					sql.append("\n 		left join feeloanoff on feeApy.id=feeloanoff.feeBudgetId and loanrep.id=feeloanoff.loanreportid ");
					
					sql.append("\n 		where feeApy.id=apy.id and (loanrep.status=1 or (loanrep.status=4 and  (nvl(feeloanoff.status,0)=0 or feeloanoff.status=1) )) ");
					sql.append("\n 		union all ");
					sql.append("\n 		select feeApy.id from feebudget feeApy");
					sql.append("\n 		inner join feeloanoff on feeApy.id=feeloanoff.feeBudgetId ");
					sql.append("\n 		where feeApy.id=apy.id and nvl(feeloanoff.loanreportId,0)=0 ");
					sql.append("\n 		and (nvl(feeloanoff.status,0)=0 or feeloanoff.status=1) ");
					sql.append("\n 		union all ");
					sql.append("\n 		select loanrep.feeBudgetId from feebudget feeApy ");
					sql.append("\n 		inner join feeloanreport loanrep on feeApy.id=loanrep.feeBudgetId ");
					sql.append("\n 		where feeApy.id=apy.id and loanrep.status=0 ");
					sql.append("\n 		group by loanrep.feeBudgetId having count(loanrep.feeBudgetId)>0 ");
					sql.append("\n 	)");
					sql.append("\n 		or exists(");
					sql.append("\n 		select id from feeloanoff ");
					sql.append("\n 		where feeloanoff.feeBudgetId=apy.id and (feeloanoff.balanceState=0 or feeloanoff.balanceState is null) and feeloanOff.status=4");
					sql.append("\n 	) ");
					//汇报成功报销失败未重新报销的
					sql.append("\n 		or exists(select max(b.id) id from feeBudget  a  inner join feeLoanReport c on a.id=c.feebudgetid ");
					sql.append("\n 		inner join feeLoanOff b on b.loanReportId = c.id LEFT JOIN feeLoanOff b2 on b2.loanReportId = c.id and b2.status !=-1 ");
					sql.append("\n 		where c.status=4 and b.status = -1 and b2.id is null and a.id=apy.id GROUP BY a.id ) ) ");
					break;
				default:
					sql.append("\n and exists(");
					sql.append("\n 		select loanrep.id from feeloanreport loanrep ");
					sql.append("\n 		where loanrep.feeBudgetId=apy.id and loanrep.status=-1 ");
					sql.append("\n 		union all ");
					sql.append("\n 		select id from feeloanoff ");
					sql.append("\n 		where feeloanoff.feeBudgetId=apy.id and feeloanoff.status=-1 ");
					sql.append("\n 	)");
					break;
			}
		}
		//未开始报销的数据，不包括重复报销的
		sql.append("\n and not exists(");
		//出差未汇报的的需要排除
		sql.append("\n 		select feeApy.id from feebudget feeApy ");
		sql.append("\n 		left join feeloanreport loanrep on feeApy.id=loanrep.feeBudgetId ");
		sql.append("\n 		LEFT JOIN spFlowInstance s on s.id= loanrep.instanceId  ");
		sql.append("\n 		where feeApy.id=apy.id and feeApy.isBusinessTrip=1");
		this.addSqlWhere(LoanWayEnum.QUOTA.getValue(), sql, args, "\n and feeApy.loanway=?");
		sql.append("\n 		and nvl(loanrep.status,0)=0 and nvl(s.flowState,0) !=2 ");
		//不汇报的未报销的
		sql.append("\n 		union all ");
		sql.append("\n 		select feeApy.id from feebudget feeApy ");
		sql.append("\n 		left join feeloanoff on feeApy.id=feeloanoff.feeBudgetId ");
		sql.append("\n 		where feeApy.id=apy.id and feeApy.isBusinessTrip=0 and nvl(feeloanoff.loanreportId,0)=0");
		this.addSqlWhere(LoanWayEnum.QUOTA.getValue(), sql, args, "\n and feeApy.loanway=?");
		sql.append("\n 		and nvl(feeloanoff.status,0)=0");
		//不汇报的未报销的
		sql.append("\n 		union all ");
		sql.append("\n 		select feeApy.id from feebudget feeApy");
		sql.append("\n 		left join feeloanoff on feeApy.id=feeloanoff.feeBudgetId ");
		sql.append("\n 		where feeApy.id=apy.id and nvl(feeloanoff.loanreportId,0)=0 ");
		this.addSqlWhereIn(new Object[]{LoanWayEnum.ITEMOFF.getValue()},
				sql, args, "\n and feeApy.loanway in ?");
		sql.append("\n 		and nvl(feeloanoff.status,0)=0 ");
		
		sql.append("\n 		minus ");
		//多次汇报的
		//出差未汇报的的需要排除
		sql.append("\n 		select loanrep.feeBudgetId from feebudget feeApy ");
		sql.append("\n 		inner join feeloanreport loanrep on feeApy.id=loanrep.feeBudgetId ");
		sql.append("\n 		where feeApy.id=apy.id and feeApy.isBusinessTrip=1 and nvl(loanrep.status,0)<>0");
		this.addSqlWhere(LoanWayEnum.QUOTA.getValue(), sql, args, "\n and feeApy.loanway=?");
		sql.append("\n 		group by loanrep.feeBudgetId having count(loanrep.feeBudgetId)>0 ");
		
		sql.append("\n 		minus ");
		sql.append("\n 		select feeloanoff.feeBudgetId from feebudget feeApy ");
		sql.append("\n 		inner join feeloanoff on feeApy.id=feeloanoff.feeBudgetId ");
		sql.append("\n 		where feeApy.id=apy.id and feeApy.isBusinessTrip=0 and nvl(feeloanoff.loanreportId,0)=0");
		this.addSqlWhere(LoanWayEnum.QUOTA.getValue(), sql, args, "\n and feeApy.loanway=?");
		sql.append("\n 		and nvl(feeloanoff.status,0)<>0");
		sql.append("\n 		group by feeloanoff.feeBudgetId having count(feeloanoff.feeBudgetId)>0 ");
		sql.append("\n 	)");
		
		//模糊查询
		String loanReportName = loanOff.getLoanReportName();
		//查询创建时间段
		String startDate = loanOff.getStartDate();
		String endDate = loanOff.getEndDate();
		if(StringUtils.isNotEmpty(loanReportName) 
				|| StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)){
			sql.append("\n 	and (  ");
			this.addSqlWhereLike(loanReportName, sql, args, "\n nvl(apyFlow.flowname,'') like ? ");
			if(StringUtils.isNotEmpty(loanReportName) ){
				sql.append("\n 	or exists( ");
			}else{
				sql.append("\n 	exists( ");
			}
			sql.append("\n 		select loanrep.id from feeloanreport loanrep ");
			sql.append("\n 		inner join spflowInstance repFlow on loanrep.instanceId=repFlow.id and loanrep.comId=repFlow.comId");
			sql.append("\n 		where loanrep.feeBudgetId=apy.id ");
			this.addSqlWhereLike(loanReportName, sql, args, "\n and repFlow.flowname like ? ");
			//查询创建时间段
			this.addSqlWhere(startDate, sql, args, " and substr(loanrep.recordcreatetime,0,10)>=?");
			this.addSqlWhere(endDate, sql, args, " and substr(loanrep.recordcreatetime,0,10)<=?");
			
			sql.append("\n 		union all ");
			sql.append("\n 		select feeloanoff.id from feeloanoff ");
			sql.append("\n 		inner join spflowInstance loanOffFlow on feeloanoff.instanceId=loanOffFlow.id and feeloanoff.comId=loanOffFlow.comId");
			sql.append("\n 		where feeloanoff.feeBudgetId=apy.id ");
			this.addSqlWhereLike(loanReportName, sql, args, "\n and loanOffFlow.flowname like ? ");
			this.addSqlWhere(startDate, sql, args, " and substr(feeloanoff.recordcreatetime,0,10)>=?");
			this.addSqlWhere(endDate, sql, args, " and substr(feeloanoff.recordcreatetime,0,10)<=?");
			sql.append("\n 		)");
			sql.append("\n 	)");
		}
		sql.append("\n )apy where 1=1");
		sql.append("\n order by apy.apyDateTime desc,apy.feeBudgetId desc");
		//总数
		String countQueryString = "select  count(*) from ( "+sql+" ) a";
		Integer count = this.countQuery(countQueryString.toString(), args.toArray());
		PaginationContext.setTotalCount(count);
		StringBuffer pagedQueryString = new StringBuffer(); 
		//
		pagedQueryString.append("\n select apy.feeBudgetId,apy.isBusinessTrip,apy.loanApplyName,apy.loanApplyInsId,");
		pagedQueryString.append("\n apy.creator,apy.creatorName,apy.apyInitStatus,apy.loanFeeTotal,");
		//汇报主键，汇报流程
		pagedQueryString.append("\n nvl(loanrep.instanceid,0) loanRepInsId,nvl(repFlow.flowname,'') loanReportName,nvl(loanrep.status,4) loanRepStatus,");
		
		pagedQueryString.append("\n nvl(loanrep.recordCreateTime,'') loanRepDate,loanrep.id loanReportId,");
		
		//报销主键，报销流程
		pagedQueryString.append("\n case when apy.loanway=1 and nvl(loanOffQuato.loanReportId,0)>0 then loanOffQuato.id else loanOffDir.id end id,");
		pagedQueryString.append("\n case when apy.loanway=1 and nvl(loanOffQuato.loanReportId,0)>0 then loanOffFlowQuato.flowName else loanOffFlowDir.flowname end loanOffName,");
		
		//报销流程主键
		pagedQueryString.append("\n case when apy.loanway=1 and nvl(loanOffQuato.loanReportId,0)>0 then loanOffQuato.instanceId else loanOffDir.instanceId end instanceId,");
		//结算状态
		pagedQueryString.append("\n case when apy.loanway=1 and nvl(loanOffQuato.loanReportId,0)>0 then loanOffQuato.balanceState else loanOffDir.balanceState end balanceState,");
		//待审金额
		pagedQueryString.append("\n case when apy.loanway=1 and nvl(loanOffQuato.loanReportId,0)>0 then loanOffQuato.loanOffPreFee else loanOffDir.loanOffPreFee end loanOffPreFee,");
		//报销金额
		pagedQueryString.append("\n case when apy.loanway=1 and nvl(loanOffQuato.loanReportId,0)>0 then loanOffQuato.loanOffItemFee else loanOffDir.loanOffItemFee end loanOffItemFee,");
		//销账金额
		pagedQueryString.append("\n case when apy.loanway=1 and nvl(loanOffQuato.loanReportId,0)>0 then loanOffQuato.loanOffBalance else loanOffDir.loanOffBalance end loanOffBalance,");
		//报销时间
		pagedQueryString.append("\n case when apy.loanway=1 and nvl(loanOffQuato.loanReportId,0)>0 then nvl(loanOffQuato.recordCreateTime,loanrep.recordCreateTime) else loanOffDir.recordCreateTime end recordCreateTime,");
		//审批状态
		pagedQueryString.append("\n case when apy.loanway=1 and nvl(loanOffQuato.loanReportId,0)>0 then loanOffQuato.status else loanOffDir.status end status,");
		//审批状态
		pagedQueryString.append("\n case when apy.loanway=1 and nvl(loanOffQuato.loanReportId,0)>0 then loanOffQuato.sendNotice else loanOffDir.sendNotice end sendNotice");
		pagedQueryString.append("\n from (");
		pagedQueryString.append("\n 		select outer.* from (");
		pagedQueryString.append("\n 			select inner.*,rownum as rowno from(");
		pagedQueryString.append(sql);
		pagedQueryString.append("\n 			) inner where rownum <=").append(PaginationContext.getOffset() + PaginationContext.getPageSize());
		pagedQueryString.append("\n 		) outer where outer.rowno>").append(PaginationContext.getOffset());
		pagedQueryString.append("\n )apy left join feeLoanReport loanrep on apy.feeBudgetId=loanrep.feeBudgetId and apy.comId=loanrep.comId and loanrep.id not in(SELECT id from feeLoanReport where status = -1 and  CREATOR != ?)");
		args.add(curUser.getId());
		pagedQueryString.append("\n left join spflowInstance repFlow on loanrep.instanceId=repFlow.id and repFlow.comId=loanrep.comId and (repFlow.CREATOR =? or (repFlow.flowState != 2 and repFlow.CREATOR !=?))");
		args.add(curUser.getId());
		args.add(curUser.getId());
		pagedQueryString.append("\n left join feeLoanOff loanOffQuato on apy.feeBudgetId=loanOffQuato.feeBudgetId and apy.comId=loanOffQuato.comId ");
		pagedQueryString.append("\n 	and loanOffQuato.loanReportId=loanrep.id and loanOffQuato.loanReportId>0");
		//监督人员排除草稿
		pagedQueryString.append("\n 		and loanOffQuato.id not in ( ");
		pagedQueryString.append("\n 		SELECT f.id from FEELOANOFF f ");
		pagedQueryString.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		pagedQueryString.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		pagedQueryString.append("\n 		where f.comid=? and f.STATUS = 0 and f.CREATOR != ? and s.FLOWSTATE= 2 ");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		//无效数据、别人失败的数据
		pagedQueryString.append("\n 		union SELECT f.id from FEELOANOFF f ");
		pagedQueryString.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		pagedQueryString.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		pagedQueryString.append("\n 		where f.comid=? and ((f.STATUS = 0  and s.FLOWSTATE= 0) or (f.STATUS = -1 and f.CREATOR != ?))");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		pagedQueryString.append("\n 		 )");
		pagedQueryString.append("\n left join spflowInstance loanOffFlowQuato on loanOffQuato.instanceId=loanOffFlowQuato.id ");
		pagedQueryString.append("\n and loanOffFlowQuato.comId=loanOffQuato.comId and loanOffFlowQuato.Flowstate<>0");
		
		pagedQueryString.append("\n left join feeLoanOff loanOffDir on apy.feeBudgetId=loanOffDir.feeBudgetId and apy.comId=loanOffDir.comId and nvl(loanOffDir.loanReportId,0)=0 and loanOffDir.id not in(SELECT id from FEELOANOFF where status = -1 and  CREATOR != ?)");
		args.add(curUser.getId());
		pagedQueryString.append("\n left join spflowInstance loanOffFlowDir on loanOffDir.instanceId=loanOffFlowDir.id and loanOffFlowDir.comId=loanOffDir.comId");
		pagedQueryString.append("\n where 1=1");
		
		
		
		if(null!=status ){
			switch(status){
				case 5://已结算
					pagedQueryString.append("\n and (nvl(loanOffQuato.balanceState,0)=1 or nvl(loanOffDir.balanceState,0)=1)");
					break;
				case 4://待结算
					pagedQueryString.append("\n and (nvl(loanOffQuato.status,0)=4 or nvl(loanOffDir.status,0)=4)");
					break;
				case 1://报销中
					pagedQueryString.append("\n and ((nvl(loanrep.status,0)=1 or (nvl(loanrep.status,0)=4 and (nvl(loanOffQuato.status,0)=0 or loanOffQuato.status=1)) or nvl(loanOffDir.status,0)=1 or loanOffDir.status =0 ) or (nvl(loanOffQuato.status,0)=4 or nvl(loanOffDir.status,0)=4)");
					//汇报成功报销失败未重新报销的
					pagedQueryString.append("\n 		or exists(select max(b.id) id from feeBudget  a  inner join feeLoanReport c on a.id=c.feebudgetid ");
					pagedQueryString.append("\n 		inner join feeLoanOff b on b.loanReportId = c.id LEFT JOIN feeLoanOff b2 on b2.loanReportId = c.id and b2.status !=-1 ");
					pagedQueryString.append("\n 		where c.status=4 and b.status = -1 and b2.id is null and a.id=apy.feeBudgetId GROUP BY a.id ) or (loanrep.status=0 and repFlow.flowState=2) ) ");
					break;
				default:
					pagedQueryString.append("\n and (nvl(loanrep.status,0)=-1 or nvl(loanOffQuato.status,0)=-1 or nvl(loanOffDir.status,0)=-1  )");
					break;
			}
		}
		
		pagedQueryString.append("\n order by apy.apyDateTime desc,nvl(loanrep.id,0) desc,nvl(loanOffQuato.id,loanOffDir.id) desc");
		return this.listQuery(pagedQueryString.toString(), args.toArray(), FeeLoanOff.class);
	}
	/**
	
	

	/**
	 * 获取个人报销中出差报销需要显示的详情
	 * @param loanOffAccount
	 * @param userInfo
	 * @return
	 */
	public FeeLoanOff getLoanOffAccountForDetails(FeeLoanOff loanOffAccount,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		if(null != loanOffAccount.getInstanceId() && loanOffAccount.getInstanceId() > 0){//如果有报销的instanceId则查询报销
            sql.append("\n select distinct loanOff.id as id,0 loanRepInsId,loanOff.status,0 as loanRepStatus,instance.flowserialnumber,");
            sql.append("\n loanOff.loanOffItemFee,loanOff.loanOffBalance,instance.flowName as loanOffName,'' as loanReportName,");
            sql.append("\n loanOff.instanceId,loanOff.isBusinessTrip,loanOff.feeBudGetId,loanOff.balanceState,feeBud.loanWay,");
            sql.append("\n usr.userName as creatorName,'' as reportName,loanOff.recordcreatetime as recordcreatetime,'' as loanRepDate,loanOff.balanceTime,loanOff.payType,u.userName balanceUserName ");
            sql.append("\n from spflowinstance instance ");
            sql.append("\n inner join feeLoanOff loanOff on instance.id = loanOff.Instanceid and loanOff.Comid = instance.comid and loanOff.comid = ?");
            args.add(userInfo.getComId());
            sql.append("\n left join userInfo usr on loanOff.Creator = usr.id");
            sql.append("\n left join userInfo u on loanOff.balanceUserId = u.id");
            sql.append("\n left join feeBudGet feeBud on loanOff.feeBudGetId = feeBud.id and loanOff.comid = feeBud.comid");
			sql.append("\n where 1 = 1");
			this.addSqlWhere(loanOffAccount.getInstanceId(),sql,args," and instance.id = ?");
        }else if(null != loanOffAccount.getLoanRepInsId() && loanOffAccount.getLoanRepInsId() > 0){//如果没有报销instanceId并且有汇报instanceId则查询汇报
            sql.append("\n select distinct rep.id as loanReportId,rep.status as loanRepStatus,instance.flowSerialnumber as repSerialNumber,");
            sql.append("\n instance.flowName as loanReportName,fee.isBusinessTrip,fee.loanWay,");
            sql.append("\n rep.instanceId as loanRepInsId,fee.id as feeBudGetId,loan.balanceState,");
			sql.append("\n usr.userName as loanReporterName,rep.recordcreatetime as loanRepDate,loan.balanceTime,loan.payType,u.userName balanceUserName ");
            sql.append("\n from spflowinstance instance ");
            sql.append("\n inner join feeLoanReport rep on instance.id = rep.Instanceid and rep.Comid = instance.comid and rep.comid = ?");
            args.add(userInfo.getComId());
            sql.append("\n left join userInfo usr on rep.Creator = usr.id");
            sql.append("\n left join feeBudGet fee on rep.feeBudGetId = fee.id and rep.comid = fee.comid");
			sql.append("\n left join feeLoanOff loan on loan.feeBudGetId = fee.id and loan.comid = fee.comid and instance.id = loan.instanceid");
			sql.append("\n left join userInfo u on loan.balanceUserId = u.id");
			sql.append("\n where 1 = 1");
            this.addSqlWhere(loanOffAccount.getLoanRepInsId(),sql,args," and instance.id = ?");
        }else{
		    return null;
        }
        return (FeeLoanOff)this.objectQuery(sql.toString(), args.toArray(),FeeLoanOff.class);
	}
	/**
	 * 获取个人报销中一般报销需要显示的详情
	 * @param loanOffAccount
	 * @param userInfo
	 * @return
	 */
	public FeeLoanOff getLoanAccountForDetails(FeeLoanOff loanOffAccount,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("\n select distinct loanOff.id as id,0 loanRepInsId,loanOff.status,0 as loanRepStatus,instance.flowserialnumber,");
		sql.append("\n loanOff.loanOffItemFee,loanOff.loanOffBalance,instance.flowName as loanOffName,'' as loanReportName,");
		sql.append("\n loanOff.instanceId,loanOff.isBusinessTrip,loanOff.feeBudGetId,loanOff.balanceState,feeBud.loanWay,");
		sql.append("\n usr.userName as creatorName,'' as reportName,loanOff.recordcreatetime as recordcreatetime,'' as loanRepDate ");
		sql.append("\n from spflowinstance instance ");
		sql.append("\n inner join feeBudGet loanOff on instance.id = loanOff.Instanceid and loanOff.Comid = instance.comid and loanOff.comid = ? and loanOff.creator = ?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n left join userInfo usr on loanOff.Creator = usr.id");
		sql.append("\n left join feeBudGet feeBud on loanOff.feeBudGetId = feeBud.id and loanOff.comid = feeBud.comid");
		sql.append("\n where 1 = 1");
		this.addSqlWhere(loanOffAccount.getInstanceId(),sql,args," and instance.id = ?");

		return (FeeLoanOff)this.objectQuery(sql.toString(), args.toArray(),FeeLoanOff.class);
	}
    /**
     * 获取当前报销的出差详情
     * @param id
     * @param userInfo
     * @return
     */
    public FeeBudget getLoanApplyForDetails(Integer id,UserInfo userInfo){
        List<Object> args = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer();

        sql.append("\n select a.*,b.flowName from feeBudGet a left join spflowinstance b on a.instanceId = b.id ");
        sql.append("\n and a.comid = b.comid");
        sql.append("\n where 1 = 1 ");
        sql.append("\n and a.id = ? and a.comid = ?");
        args.add(id);
        args.add(userInfo.getComId());
        return (FeeBudget)this.objectQuery(sql.toString(), args.toArray(),FeeBudget.class);
    }
	/**
	 * 查询不能报销的
	 * @param sessionUser
	 * @param loanOffAccount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanOff> listLoanOffN(UserInfo sessionUser, FeeLoanOff loanOffAccount) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//汇报主键，汇报人，汇报状态，报销主键，报销状态，报销流程状态
		sql.append("\n select loanRep.id loanReportId,loanRep.creator, loanRep.status loanRepStatus,loanOff.id,loanOff.status,loanOffFlow.flowState,loanRep.busTripState isBusinessTrip, ");
		//预报销金额,报销费用，汇报人，汇报时间
		sql.append("\n loanOff.loanOffPreFee,loanOff.loanOffItemFee,loanOff.loanOffBalance,repUser.userName creatorName,substr(loanRep.Recordcreatetime,0,16) loanRepDate,");
		//汇报的开始时间，汇报的截止时间，报销时间
		sql.append("\n loanRep.startTime loanRepStartDate,loanRep.endTime loanRepEndDate,substr(loanOff.Recordcreatetime,0,16) recordCreateTime, ");
		//汇报流程状态，汇报流程名称，汇报流程主键，报销流程主键,报销流程名称
		sql.append("\n loanRepFlow.flowState loanRepFlowStatus, loanRepFlow.flowName loanReportName,loanRep.instanceid loanRepInsId,loanOff.instanceId,loanOffFlow.flowName loanOffName ");
		//汇报表
		sql.append("\n from feeLoanReport loanRep ");
		//销账表
		sql.append("\n left join feeLoanOff loanOff on loanRep.Id=loanOff.Loanreportid and loanrep.feeBudgetId=loanOff.feeBudgetId ");
		//销账流程表
		sql.append("\n left join spFlowInstance loanOffFlow on loanOffFlow.Id=loanOff.instanceId and loanOffFlow.comId=loanOff.comId");
		sql.append("\n and (loanOff.Status in("+ConstantInterface.COMMON_YES+","+ConstantInterface.COMMON_FINISH+") or(loanOff.Status=0 and loanOffFlow.flowState =2) )");
		//汇报人员
		sql.append("\n left join userInfo repUser on loanRep.creator=repUser.id");
		//汇报流程表
		sql.append("\n left join spflowinstance loanRepFlow on loanRep.instanceid=loanRepFlow.id and loanRep.comId=loanRepFlow.comId");
		sql.append("\n where loanRep.comId=? and loanRep.feeBudgetId=?");
		args.add(sessionUser.getComId());
		args.add(loanOffAccount.getFeeBudgetId());
		this.addSqlWhere(loanOffAccount.getLoanReportId(), sql, args, "\n and loanRep.id=?");
		sql.append("\n order by loanRep.feeBudgetId desc,loanRep.id desc,loanOff.status desc,loanOffFlow.flowState");
		return this.listQuery(sql.toString(), args.toArray(), FeeLoanOff.class);
	}
	/**
	 * 查询报销详情
	 * @param sessionUser 当前操作人员
	 * @param loanOffAccount 报销信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanOff> listFeeLoanOffForFeebudgetDetail(UserInfo sessionUser, FeeLoanOff loanOffAccount) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//汇报主键，汇报人，汇报状态，报销主键，报销状态，报销流程状态
		sql.append("\n select loanRep.id loanReportId,loanRep.creator, loanRep.status loanRepStatus,loanOff.id,loanOff.status,loanOffFlow.flowState,loanRep.busTripState isBusinessTrip, ");
		//预报销金额,报销费用，汇报人，汇报时间
		sql.append("\n loanOff.loanOffPreFee,loanOff.loanOffItemFee,loanOff.loanOffBalance,repUser.userName creatorName,substr(loanRep.Recordcreatetime,0,16) loanRepDate,");
		//汇报的开始时间，汇报的截止时间，报销时间
		sql.append("\n loanRep.startTime loanRepStartDate,loanRep.endTime loanRepEndDate,substr(loanOff.Recordcreatetime,0,16) recordCreateTime, ");
		//汇报流程状态，汇报流程名称，汇报流程主键，报销流程主键,报销流程名称
		sql.append("\n loanRepFlow.flowState loanRepFlowStatus, loanRepFlow.flowName loanReportName,loanRep.instanceid loanRepInsId,loanOff.instanceId,loanOffFlow.flowName loanOffName ");
		//汇报表
		sql.append("\n from feeLoanReport loanRep ");
		//销账表
		sql.append("\n left join feeLoanOff loanOff on loanRep.Id=loanOff.Loanreportid and loanrep.feeBudgetId=loanOff.feeBudgetId and loanOff.Status<>"+ConstantInterface.COMMON_DEF+"");
		//销账流程表
		sql.append("\n left join spFlowInstance loanOffFlow on loanOffFlow.Id=loanOff.instanceId and loanOffFlow.comId=loanOff.comId");
		sql.append("\n and loanOff.Status in("+ConstantInterface.COMMON_YES+","+ConstantInterface.COMMON_FINISH+")");
		//汇报人员
		sql.append("\n left join userInfo repUser on loanRep.creator=repUser.id");
		//汇报流程表
		sql.append("\n left join spflowinstance loanRepFlow on loanRep.instanceid=loanRepFlow.id and loanRep.comId=loanRepFlow.comId");
		sql.append("\n where loanRep.comId=? and loanRep.feeBudgetId=?");
		args.add(sessionUser.getComId());
		args.add(loanOffAccount.getFeeBudgetId());
		this.addSqlWhere(loanOffAccount.getLoanReportId(), sql, args, "\n and loanRep.id=?");
		sql.append("\n order by loanRep.feeBudgetId desc,loanRep.id desc,loanOff.status desc,loanOffFlow.flowState");
		return this.listQuery(sql.toString(), args.toArray(), FeeLoanOff.class);
	}
	/**
	 * 查询不能报销的
	 * @param sessionUser
	 * @param loanOffAccount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanOff> listLoanOffNDaily(UserInfo sessionUser, FeeLoanOff loanOffAccount) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//汇报主键，汇报人，汇报状态，报销主键，报销状态，报销流程状态
		sql.append("\n select 0 loanReportId,loanOff.creator, 4 loanRepStatus,0 id,loanOff.status,loanOffFlow.flowState,loanOff.isBusinessTrip busTripState, ");
		//预报销金额,报销费用，汇报人，汇报时间
		sql.append("\n loanOff.loanOffPreFee,loanOff.loanOffItemFee,loanOff.loanOffBalance,repUser.userName creatorName,null loanRepDate,");
		//汇报的开始时间，汇报的截止时间，报销时间
		sql.append("\n null loanRepStartDate,null loanRepEndDate,substr(loanOff.Recordcreatetime,0,16) recordCreateTime, ");
		//汇报流程状态，汇报流程名称，汇报流程主键，报销流程主键,报销流程名称
		sql.append("\n 4 loanRepFlowStatus, null loanReportName,0 loanRepInsId,loanOff.instanceId,loanOffFlow.flowName loanOffName ");
		//汇报表
		sql.append("\n from feeLoanOff loanOff ");
		//销账流程表
		sql.append("\n left join spFlowInstance loanOffFlow on loanOffFlow.Id=loanOff.instanceId and loanOffFlow.comId=loanOff.comId");
		sql.append("\n and loanOff.Status in("+ConstantInterface.COMMON_YES+","+ConstantInterface.COMMON_FINISH+")");
		//汇报人员
		sql.append("\n left join userInfo repUser on loanOff.creator=repUser.id");
		
		sql.append("\n where loanOff.Status<>"+ConstantInterface.COMMON_DEF+" and  loanOff.comId=? and loanOff.feeBudgetId=? and loanOff.loanWay=?");
		args.add(sessionUser.getComId());
		args.add(loanOffAccount.getFeeBudgetId());
		args.add(LoanWayEnum.QUOTA.getValue());
		sql.append("\n order by loanOff.status desc,loanOffFlow.flowState");
		return this.listQuery(sql.toString(), args.toArray(), FeeLoanOff.class);
	}

	/**
	 * 查询本次出差计划的基本信息用于借款
	 * @param applyId 出差计划主键
	 * @return
	 */
	public FeeBudget queryLoanApply4Check(Integer applyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select loanApply.id,loanApply.status,nvl(loanApply.allowedQuota,0) allowedQuota ,loanApply.isBusinessTrip,loanApply.loanWay,");
		sql.append("\n loanApply.endTime,loanApply.creator,applyIns.flowName,loanApply.loanFeeTotal,loanApply.instanceId");
		sql.append("\n from feeBudget loanApply ");
		sql.append("\n left join spflowinstance applyIns on loanApply.instanceId=applyIns.id");
		sql.append("\n where 1=1 and loanApply.id=?");
		args.add(applyId);
		return (FeeBudget) this.objectQuery(sql.toString(), args.toArray(), FeeBudget.class);
	}
	/**
	 * 根据报账主键信息查询借款额度信息
	 * @param loanOffId
	 * @param userInfo
	 * @return
	 */
	public FeeBudget queryApplyByLoanOffId(Integer loanOffId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* ");
		sql.append("\n from loanApply a  ");
		sql.append("\n inner join loanOffAccount b on a.comid=b.comId and a.id=b.loanBusId and b.loanway=?");
		sql.append("\n where 1=1 and b.id=? and b.comId=?");
		args.add(loanOffId);
		args.add(userInfo.getComId());
		args.add(LoanWayEnum.QUOTA.getValue());
		return (FeeBudget) this.objectQuery(sql.toString(), args.toArray(), FeeBudget.class);
	}
	/**
	 * 查询人员
	 * @param isBusinessTrip 
	 * @return
	 */
	public FeeBudget queryTotalApplyFee(Integer comId,Integer userId, Integer isBusinessTrip) {
	        
		String startDate = DateTimeUtil.getFirstDayOfYear(DateTimeUtil.yyyy_MM_dd);
		
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		String endDate = DateTimeUtil.addDate(nowDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 1);
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select sum(a.loanFeeTotal) as totalBorrow,sum(a.loanOffTotal) totalOff");
		sql.append("\n from feeBudget a ");
		sql.append("\n where a.isBusinessTrip=? and a.status=? and loanOffState=?");//审核通过的差旅，未销账的
		args.add(isBusinessTrip);
		args.add(ConstantInterface.COMMON_FINISH);
		args.add(ConstantInterface.COMMON_DEF);
		sql.append("\n and a.recordCreateTime>=? and a.recordCreateTime<=?");
		sql.append("\n and a.creator=? and a.comId=?");
		
		args.add(startDate);
		args.add(endDate);
		args.add(userId);
		args.add(comId);
		return (FeeBudget) this.objectQuery(sql.toString(), args.toArray(), FeeBudget.class);
	}
	/**
	 * 用于选择的借款最近报销情况
	 * @param comId
	 * @param userId
	 * @param applyId
	 * @return
	 */
	public FeeLoanOff queryLoanOffOfDailyRunningNoRep(Integer comId,Integer userId, Integer applyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id as feeBudgetId,0 loanReportId,4 loanRepStatus,0 loanRepFlowId,null loanReportName,");
		sql.append("\n c.id,c.status,c.instanceid,loanOffFlow.Flowname as loanOffName,");
		sql.append("\n case when  (c.status is null or c.status="+ConstantInterface.COMMON_NO+") then 1");
		sql.append("\n when (c.status="+ConstantInterface.COMMON_YES+") then 2 else 4 end stepth");
		sql.append("\n from feeBudget a");
		sql.append("\n left join feeLoanOff c on a.id = c.feeBudgetId and (c.status<>? and c.status<>?) and c.loanWay=?");
		args.add(ConstantInterface.COMMON_NO);
		args.add(ConstantInterface.COMMON_DEF);
		args.add(LoanWayEnum.QUOTA.getValue());
		sql.append("\n left join spflowinstance loanOffFlow on loanOffFlow.Id=c.Instanceid");
		sql.append("\n where (c.status is null or c.status="+ConstantInterface.COMMON_YES+") ");//前提；报销说明、报销申请有其一审批未成功完结
		sql.append("\n and a.comid=? and a.creator=? and a.id=? and rownum=1");
		args.add(comId);
		args.add(userId);
		args.add(applyId);
		sql.append("\n order by c.id desc");
		return (FeeLoanOff)this.objectQuery(sql.toString(), args.toArray(),FeeLoanOff.class);
	}
	/**
	 * 查询当前人员出差借款中有效的额度申请信息
	 * @param curUser 当前操作人员
	 * @param loanApply 额度申请查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeBudget> listPagedLoanTripApplyForStartSelect(
			UserInfo curUser, FeeBudget loanApply) {
		String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n SELECT A.* FROM (");
		sql.append("\n 		SELECT A.ID,A.RECORDCREATETIME,A.TRIPPLACE,A.STARTTIME,A.ENDTIME,A.STATUS,A.ALLOWEDQUOTA,");
		sql.append("\n 		A.LOANFEETOTAL,A.loanOffTotal,A.loanItemTotal,A.INSTANCEID,SPFLOW.FLOWNAME,A.ISBUSINESSTRIP ");
		sql.append("\n 		FROM feeBudget A");
		sql.append("\n 		INNER JOIN SPFLOWINSTANCE SPFLOW ON A.COMID=SPFLOW.COMID AND A.INSTANCEID=SPFLOW.ID");
		sql.append("\n 		WHERE A.ISBUSINESSTRIP=? AND A.COMID=? AND A.CREATOR=?");
		args.add(TripFeeEnum.YES.getValue());
		args.add(curUser.getComId());
		args.add(curUser.getId());
		if(null!=loanApply.getStatus()){
			this.addSqlWhereIn(new Object[]{loanApply.getStatus()},sql, args, "\n AND A.STATUS IN ?");
		}else{
			//默认审核通过的或是正在进行中的
			this.addSqlWhereIn(new Object[]{ConstantInterface.COMMON_FINISH}, 
					sql, args, "\n AND A.STATUS IN ?");
		}
		//查询创建时间段
		this.addSqlWhere(loanApply.getStartDate(), sql, args, " AND SUBSTR(A.RECORDCREATETIME,0,10)>=?");
		this.addSqlWhere(loanApply.getEndDate(), sql, args, " AND SUBSTR(A.RECORDCREATETIME,0,10)<=?");
		
		this.addSqlWhere(nowDateStr, sql, args, "\n AND A.ENDTIME>=?");
		sql.append("\n )A ");
		
		sql.append("\n WHERE 1=1 ");
		//额度没有使用完
		sql.append("\n AND	A.STATUS = ? AND A.ALLOWEDQUOTA>A.LOANFEETOTAL");
		args.add(ConstantInterface.COMMON_FINISH);
		
		//排除正在借款的
		sql.append("\n  and not exists(");
		sql.append("\n  	select id from feeloan where feeloan.feebudgetid=a.id and feeloan.status=1");
		sql.append("\n  )");
		return this.pagedQuery(sql.toString(), " A.STATUS,A.ID DESC", args.toArray(), FeeBudget.class);
	}
	/**
	 * 查询当前人员一般借款中有效的额度申请信息
	 * @param curUser 当前操作人员
	 * @param loanApply 额度申请查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeBudget> listPagedLoanDailyApplyForStartSelect(
			UserInfo curUser, FeeBudget loanApply) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n SELECT A.ID,A.RECORDCREATETIME,A.STARTTIME,A.ENDTIME,A.STATUS,A.ALLOWEDQUOTA,A.INSTANCEID,SPFLOW.FLOWNAME,A.ISBUSINESSTRIP  ");
		sql.append("\n FROM feeBudget A");
		sql.append("\n INNER JOIN SPFLOWINSTANCE SPFLOW ON A.COMID=SPFLOW.COMID AND A.INSTANCEID=SPFLOW.ID");
		sql.append("\n WHERE A.ISBUSINESSTRIP=0 AND A.COMID=? AND A.CREATOR=?");
		args.add(TripFeeEnum.NO.getValue());
		args.add(curUser.getComId());
		args.add(curUser.getId());
		if(null!=loanApply.getStatus()){
			this.addSqlWhereIn(new Object[]{loanApply.getStatus()},sql, args, "\n AND A.STATUS IN ?");
		}else{
			this.addSqlWhereIn(new Object[]{ConstantInterface.COMMON_YES,ConstantInterface.COMMON_FINISH}, 
					sql, args, "\n AND A.STATUS IN ?");
		}
		//查询创建时间段
		this.addSqlWhere(loanApply.getStartDate(), sql, args, " AND SUBSTR(A.RECORDCREATETIME,0,10)>=?");
		this.addSqlWhere(loanApply.getEndDate(), sql, args, " AND SUBSTR(A.RECORDCREATETIME,0,10)<=?");
		return this.pagedQuery(sql.toString(), " A.STATUS,A.ID DESC", args.toArray(), FeeBudget.class);
	}
	/**
	 * 查询用于报销的额度申请
	 * @param sessionUser 当前操作人员
	 * @param feeBudget 额度申请的信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeBudget> listPagedFeeBudgetForOffSelect(UserInfo sessionUser, FeeBudget feeBudget) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		
		
		sql.append("\n SELECT A.* FROM (");
		sql.append("\n 		SELECT A.ID,A.RECORDCREATETIME,A.STATUS,A.ALLOWEDQUOTA,A.loanFeeTotal,");
		sql.append("\n 			A.loanOffTotal,A.INSTANCEID,A.ISBUSINESSTRIP,a.initStatus,");
		sql.append("\n 			case when a.initStatus=1 then '2018年2月28日前未销账金额初始化' else SPFLOW.FLOWNAME end FLOWNAME");
		sql.append("\n 		FROM feeBudget A");
		sql.append("\n 		LEFT JOIN SPFLOWINSTANCE SPFLOW ON A.COMID=SPFLOW.COMID AND A.INSTANCEID=SPFLOW.ID");
		sql.append("\n 		left join feeDirectBalance fdb on fdb.feeBudgetId = A.id ");
		sql.append("\n 		WHERE A.ISBUSINESSTRIP=? AND A.COMID=? AND A.CREATOR=? and a.loanway=? and fdb.id is null");
		args.add(feeBudget.getIsBusinessTrip());
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		//额度借款
		args.add(LoanWayEnum.QUOTA.getValue());
		//未销账的
		this.addSqlWhere(0, sql, args, "\n and ( ( a.loanOffState=?");
		this.addSqlWhere(ConstantInterface.COMMON_FINISH, sql, args, "\n AND A.STATUS = ?");
		
		//排除 汇报的中 或是 报销中的  或是 全部汇报完成的
		sql.append("\n 		and not exists (");
		//汇报中
		sql.append("\n 		select loanrep.feeBudgetId from feeLoanReport loanrep");
		sql.append("\n 		left join feeloanoff loanoff on loanrep.id=loanoff.loanreportid ");
		sql.append("\n 		 and loanoff.feebudgetid=loanoff.feebudgetid");
		sql.append("\n 		where  loanrep.feeBudgetId = a.id and (loanrep.status = 1");
		sql.append("\n 			or (loanrep.status = 4 and (nvl(loanoff.status,0)<>4) )");
		sql.append("\n 		)");
		sql.append("\n 		union all");
		// 报销中的
		sql.append("\n 		select loanOff.feeBudgetId from feeLoanOff loanOff");
		sql.append("\n 		where loanOff.feeBudgetId = a.id and loanOff.status = 1");
		sql.append("\n 		) )");
		//汇报成功报销失败未重新报销的
		sql.append("\n 		or a.id in(select a.id from feeBudget  a  inner join feeLoanReport c on a.id=c.feebudgetid ");
		sql.append("\n 		inner join feeLoanOff b on b.loanReportId = c.id LEFT JOIN feeLoanOff b2 on b2.loanReportId = c.id and b2.status !=-1 ");
		sql.append("\n		left join spFlowInstance s on s.id = b2.instanceId ");
		sql.append("\n 		where c.status=4 and b.status = -1 and (b2.id is null or s.flowState = 0) )  ");
		
		//申请成功未借款、未汇报、汇报失败的的
		sql.append("\n 		or a.id in(SELECT a.id from FEEBUDGET a LEFT JOIN FEELOANREPORT b on b.FEEBUDGETID = a.id ");
		sql.append("\n		left join spFlowInstance s on s.id = b.instanceId ");
		sql.append("\n 		where 1=1 and a.STATUS = 4 and a.INSTANCEID > 0  and a.LOANOFFSTATE = 1 and a.ISBUSINESSTRIP = 1   ");
		sql.append("\n    		and (b.id is null or (b.status=-1 and b.FEEBUDGETID not in(select FEEBUDGETID from FEELOANREPORT a ");
		sql.append("\n				left join spFlowInstance s on s.id = a.instanceId 	where a.status != -1 and s.flowState != 0))");
		sql.append("\n 		or (b.status=0 and s.flowState=0 ) ) )");
		//汇报成功未报销的
		sql.append("\n or a.id in(select a.id ");
		sql.append("\n from feeBudget a ");
		sql.append("\n inner join feeLoanReport b on a.id=b.feebudgetid and b.status=4");
		sql.append("\n left join feeLoanOff c");
		sql.append("\n on a.id=c.feebudgetid and b.id=c.loanreportid");
		sql.append("\n left join spFlowInstance s on s.id = c.instanceId");
		sql.append("\n where c.id is null or s.flowState = 0))");
		
		sql.append("\n )A ");
		sql.append("\n WHERE 1=1  ");
		return this.pagedQuery(sql.toString(), "A.RECORDCREATETIME DESC", args.toArray(), FeeBudget.class);
	}
	/**
	 * 查询用于报销的借款申请
	 * @param sessionUser 当前操作人员
	 * @param loan 额度申请的信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoan> listPagedLoanForOffSelect(UserInfo sessionUser, FeeLoan loan) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n SELECT A.* FROM (");
		sql.append("\n 		SELECT A.ID,A.RECORDCREATETIME,A.STATUS,A.BORROWINGBALANCE,SPFLOW.FLOWNAME,A.ISBUSINESSTRIP ");
		sql.append("\n 		FROM LOAN A");
		sql.append("\n 		INNER JOIN SPFLOWINSTANCE SPFLOW ON A.COMID=SPFLOW.COMID AND A.INSTANCEID=SPFLOW.ID");
		sql.append("\n 		WHERE A.ISBUSINESSTRIP=? AND A.COMID=? AND A.CREATOR=? AND SPFLOW.SPSTATE=1 and A.LOANAPPLYID=0");
		args.add(loan.getIsBusinessTrip());
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		this.addSqlWhere(ConstantInterface.COMMON_FINISH, sql, args, "\n AND A.STATUS = ?");
		sql.append("\n )A ");
		sql.append("\n WHERE 1=1  ");
		return this.pagedQuery(sql.toString(), " A.STATUS,A.ID DESC", args.toArray(), FeeBudget.class);
	}
	/**
	 * 查询报销审批信息
	 * @return
	 */
	public FeeLoanOff queryLoanOffFlowByReportId(Integer loanReportId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,2 flowState");
		sql.append("\n from feeLoanOff a inner join spflowinstance spFlow on a.instanceId = spFlow.id and a.comId=spFlow.comId");
		sql.append("\n where 1=1 and a.loanReportId=? and spFlow.flowState=2");
		args.add(loanReportId);
		return (FeeLoanOff) this.objectQuery(sql.toString(), args.toArray(), FeeLoanOff.class);
	}
	/**
	 * 用于业务出差统计
	 * @param userInfo 当前操作人员
	 * @param statisticFeeCrmVo 业务出差统计查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StatisticFeeCrmVo> listPagedStatisticFeeCrm(UserInfo userInfo,
			StatisticFeeCrmVo statisticFeeCrmVo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> list = MonthEnum.toList();
		String year = statisticFeeCrmVo.getYear();
		sql.append("\n select dep.depId,dep.depname,dep.newOrder,uorg.userid,");
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			Integer value = Integer.parseInt(map.get("value").toString());
			String tableName = "loanofffee"+map.get("value");
			sql.append("\n  nvl("+tableName+".loanItemTotal,0) itemFee"+value+",");
			
		}
		sql.append("\n u.username");
		sql.append("\n from (");
		sql.append("\n  select id depId,depname,parentid,rownum newOrder");
		sql.append("\n  from department dep where 1=1 and enabled=1 and comId=? ");
		args.add(userInfo.getComId());
		sql.append("\n  start with parentid=-1 CONNECT BY PRIOR id = parentid");
		sql.append("\n  order siblings by dep.id desc");
		sql.append("\n  )dep inner join userorganic uorg on dep.depId = uorg.depid and uorg.comid=? ");
		args.add(userInfo.getComId());
		sql.append("\n  inner join userinfo u on uorg.userid=u.id");
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			Integer value = Integer.parseInt(map.get("value").toString());
			sql.append("\n  left join (");
			sql.append("\n  	select a.creator,sum(a.loanOffItemFee) loanItemTotal");
			sql.append("\n      from (");
			sql.append("\n      	select b.id,a.creator,b.loanOffItemFee  from feeBudget a");
			sql.append("\n      	inner join feeLoanOff b on b.status=4 and b.loanReportWay=1");
			sql.append("\n      	and b.loanWay=1 and b.isBusinessTrip=1 and a.id=b.feeBudgetId");
			sql.append("\n      	inner join feeBusMod c on a.id=c.feeBudgetId and c.loanWay=1 and c.busType=?");
			args.add(ConstantInterface.TYPE_CRM);
			sql.append("\n      	inner join customer crm on crm.id=c.busid and crm.delstate=0");
			sql.append("\n      where substr(a.recordcreatetime,0,7)=?");
			if(value<10){
				args.add(year+"-0"+value);
			}else{
				args.add(year+"-"+value);
			}
			sql.append("\n      union");
			
			sql.append("\n      select b.id,b.creator,b.loanOffItemFee from feeLoanOff b");
			sql.append("\n      inner join feeBusMod c on b.id=c.feeBudgetId and b.loanWay=1 and c.busType=?");
			args.add(ConstantInterface.TYPE_CRM);
			sql.append("\n      inner join customer crm on crm.id=c.busid and crm.delstate=0");
			sql.append("\n      where b.status=4 and b.loanReportWay=3 and b.loanWay=2 and b.isBusinessTrip=1");
			sql.append("\n      and substr(b.recordcreatetime,0,7)=?");
			if(value<10){
				args.add(year+"-0"+value);
			}else{
				args.add(year+"-"+value);
			}
			sql.append("\n      )a where 1=1");
			sql.append("\n group by a.creator");
			String tableName = "loanofffee"+map.get("value");
			sql.append("\n )"+tableName+" on u.id="+tableName+".creator");
		}
		sql.append("\n  where 1=1");
		//部门选择
		List<Department> listDeps = statisticFeeCrmVo.getListDeps();
		if(null!=listDeps && !listDeps.isEmpty()){
			List<Integer> depIds = new ArrayList<Integer>(listDeps.size());
			for (Department department : listDeps) {
				depIds.add(department.getId());
			}
			Integer[] depId = new Integer[listDeps.size()];
			this.addSqlWhereIn(depIds.toArray(depId), sql, args, "\n and dep.depId in ?");
		}
		
		sql.append("\n  and ( ");
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			String tableName = "loanofffee"+map.get("value");
			if(i>0){
				sql.append(" or");
			}
			sql.append("\n nvl("+tableName+".loanItemTotal,0)>0 ");
			
		}	
		sql.append("\n )");
		
		return this.pagedQuery(sql.toString(), " dep.newOrder", args.toArray(), StatisticFeeCrmVo.class);
	}
	/**
	 * 用于业务出差统计
	 * @param userInfo 当前操作人员
	 * @param statisticFeeItemVo 业务出差统计查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StatisticFeeItemVo> listPagedStatisticFeeItem(UserInfo userInfo,
			StatisticFeeItemVo statisticFeeItemVo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> list = MonthEnum.toList();
		String year = statisticFeeItemVo.getYear();
		sql.append("\n select dep.id depId,dep.depname,uorg.userid,item.id itemId,item.itemName,");
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			Integer value = Integer.parseInt(map.get("value").toString());
			String tableName = "loanofffee"+map.get("value");
			sql.append("\n  nvl("+tableName+".loanItemTotal,0) itemFee"+value+",");
			
		}
		sql.append("\n u.username");
		sql.append("\n from item ");
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			Integer value = Integer.parseInt(map.get("value").toString());
			sql.append("\n  left join (");
			
			sql.append("\n  	select a.busId,sum(a.loanOffItemFee) loanItemTotal");
			sql.append("\n      from (");
			sql.append("\n      	select b.id,c.busId,b.loanOffItemFee  from feeBudget a");
			sql.append("\n      	inner join feeLoanOff b on b.status=4 and b.loanReportWay=1");
			sql.append("\n      	and b.loanWay=1 and b.isBusinessTrip=1 and a.id=b.feeBudgetId");
			sql.append("\n      	inner join feeBusMod c on a.id=c.feeBudgetId and c.loanWay=1 and c.busType=?");
			args.add(ConstantInterface.TYPE_ITEM);
			sql.append("\n      where substr(a.recordcreatetime,0,7)=?");
			if(value<10){
				args.add(year+"-0"+value);
			}else{
				args.add(year+"-"+value);
			}
			sql.append("\n      union");
			
			sql.append("\n      select b.id,c.busId,b.loanOffItemFee from feeLoanOff b");
			sql.append("\n      inner join feeBusMod c on b.id=c.feeBudgetId and b.loanWay=1 and c.busType=?");
			args.add(ConstantInterface.TYPE_ITEM);
			sql.append("\n      where b.status=4 and b.loanReportWay=3 and b.loanWay=2 and b.isBusinessTrip=1");
			sql.append("\n      and substr(b.recordcreatetime,0,7)=?");
			if(value<10){
				args.add(year+"-0"+value);
			}else{
				args.add(year+"-"+value);
			}
			sql.append("\n      )a where 1=1");
			sql.append("\n group by a.busId");
			String tableName = "loanofffee"+map.get("value");
			sql.append("\n )"+tableName+" on item.id="+tableName+".busId");
		}
		sql.append("\n  left join userorganic uorg on item.owner=uorg.userId and uorg.comid=? ");
		args.add(userInfo.getComId());
		sql.append("\n  left join department dep on dep.id=uorg.depId ");
		sql.append("\n  left join userInfo u on item.owner=u.id ");
		sql.append("\n  where 1=1 and item.comId=? and item.delstate=0 ");
		args.add(userInfo.getComId());
		//部门选择
		List<Department> listDeps = statisticFeeItemVo.getListDeps();
		if(null!=listDeps && !listDeps.isEmpty()){
			List<Integer> depIds = new ArrayList<Integer>(listDeps.size());
			for (Department department : listDeps) {
				depIds.add(department.getId());
			}
			Integer[] depId = new Integer[listDeps.size()];
			this.addSqlWhereIn(depIds.toArray(depId), sql, args, "\n and dep.id in ?");
		}
		sql.append("\n  and ( ");
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			String tableName = "loanofffee"+map.get("value");
			if(i>0){
				sql.append(" or");
			}
			sql.append("\n nvl("+tableName+".loanItemTotal,0)>0 ");
			
		}	
		sql.append("\n )");
		return this.pagedQuery(sql.toString(), " dep.id,item.id desc", args.toArray(), StatisticFeeItemVo.class);
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * 分页查询财务办公借款记录
	 * @param curUser
	 * @param loan
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoan> listPagedLoans(UserInfo curUser, FeeLoan loan) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select apy.orderDate,apy.comId,apy.creator,apy.isbusinesstrip ,");
		sql.append("\n apy.executorName,");
		sql.append("\n apy.instanceid,apy.flowName, apy.borrowingBalance,");
		sql.append("\n apy.status,apy.flowState,apy.spstate,apy.recordCreateTime,apy.creatorName,apy.id,apy.sendNotice,apy.balanceState");
		sql.append("\n from (");
		sql.append("\n				select apy.*,u.userName as creatorName from(");
		
		//借款申请的主键，借款申请时间，借款申请的团队号，借款申请的人员，借款申请类型
		sql.append("\n				select loan.recordcreatetime orderDate,loan.comid,loan.creator,loan.isbusinesstrip,");
		
		sql.append("\n				loanCurSpUser.userName executorName," );
		//借款流程主键，借款流程名称
		sql.append("\n				loan.instanceid,loanFlow.flowName, nvl(loan.borrowingBalance,0) borrowingBalance,");
		//借款状态，借款流程状态，借款审批状态,借款时间
		sql.append("\n				loan.status,nvl(loan.sendNotice, 0) sendNotice, nvl(loan.balanceState,0)balanceState,loanFlow.flowState,loanFlow.spstate,loan.recordCreateTime,loan.id");
				
		sql.append("\n				from feeLoan loan");
		sql.append("\n		 		inner join userinfo loanoffUser on loan.creator=loanoffUser.id");
		//借款流程表
		sql.append("\n				inner join spflowinstance loanFlow on loan.instanceId=loanFlow.id and loanFlow.flowstate<>0");
		sql.append("\n 				left join spFlowCurExecutor loanFlowCurExecutor on loanFlow.comId = loanFlowCurExecutor.comid ");
		sql.append("\n 					and loanFlow.id = loanFlowCurExecutor.busId and loanFlowCurExecutor.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 				left join userinfo loanCurSpUser on loanFlowCurExecutor.Executor=loanCurSpUser.id");
		//借款申请表
		sql.append("\n		 		where loan.comId=?");
		args.add(curUser.getComId());
		//财务办公能看到的数据
		sql.append("and ((loanFlow.FLOWSTATE = '4' and loanFlow.SPSTATE = '1') or loanFlowCurExecutor.EXECUTOR = ?) \n");
		args.add(curUser.getId());
		
		sql.append("\n			)apy inner join userinfo u on apy.creator=u.id ");
		sql.append("\n			where 1=1");
		
		
		this.addSqlWhere(loan.getIsBusinessTrip(), sql, args, " and  apy.isBusinessTrip=?");
		this.addSqlWhere(loan.getStatus(), sql, args, " and  apy.status=?");
		if(null!=loan.getFlowState() && loan.getFlowState()>-1){
			this.addSqlWhere(loan.getFlowState(), sql, args, " and apy.flowstate=?");//流程状态筛选
		}
		this.addSqlWhere(loan.getSpState(), sql, args, " and apy.spState=?");//审批状态筛选
		//查询创建时间段
		this.addSqlWhere(loan.getStartDate(), sql, args, " and substr(apy.recordcreatetime,0,10)>=?");
		this.addSqlWhere(loan.getEndDate(), sql, args, " and substr(apy.recordcreatetime,0,10)<=?");
		this.addSqlWhereLike(loan.getFlowName(), sql, args, " and (apy.flowName like ? )\n");//流程名称筛选
		if(loan.getSendNotice() != null) {
			if(loan.getSendNotice() == 1) {
				sql.append("\n 	and  apy.balanceState != 1 and  apy.flowState = 4  and apy.sendNotice = 1");
			}else {
				sql.append("\n 	and  apy.balanceState != 1 and  apy.flowState = 4  and apy.sendNotice != 1");
			}
		}
		if(loan.getBalanceState() != null) {
			if(loan.getBalanceState() == 1) {
				sql.append("\n 	and  apy.balanceState = 1");
			}else if(loan.getBalanceState() == 0) {
				sql.append("\n 	and  apy.balanceState != 1 and  apy.flowState = 4");
			}else if(loan.getBalanceState() == 3) {
				sql.append("\n and	nvl(apy.balanceState,0)!=1");
			}else {
				sql.append("\n 	and  apy.flowState != 4");
			}
		}
		if(null != loan.getListCreator() && !loan.getListCreator().isEmpty()){
			sql.append("	 and  ( apy.creator= 0 ");
			for(UserInfo owner : loan.getListCreator()){
				sql.append(" or apy.creator=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		sql.append("\n		 	order by apy.orderDate desc ,apy.id desc");
		sql.append("\n )apy where 1=1");
		return this.pagedQuery(sql.toString(),"apy.flowState,apy.balanceState,apy.sendNotice,apy.id desc", args.toArray(), FeeLoan.class);
	}
	
	/**
	 * 查询借款审核总数
	 * @param curUser
	 * @return
	 */
	public Integer countLoans(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n				select count(1) from feeloan loan");
		//借款流程表
		sql.append("\n				inner join spflowinstance ins on loan.instanceId=ins.id and ins.flowstate<>0");
		sql.append("\n 				left join spFlowCurExecutor flowCurExecutor on ins.comId = flowCurExecutor.comid");
		sql.append("\n 				   	and ins.id = flowCurExecutor.busId and flowCurExecutor.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n			where loan.comid=?");
		args.add(curUser.getComId());
		//财务办公能看到的数据,只统计需要办理总数
		sql.append("and ((ins.FLOWSTATE = '4' and ins.SPSTATE = '1') or flowCurExecutor.EXECUTOR = ?)  \n");
		args.add(curUser.getId());
		sql.append("\n		and	nvl(loan.balanceState,0)!=1");
		return this.countQuery(sql.toString(),args.toArray());
	}
	
	
	/**
	 * 查询借款审核总数
	 * @param curUser
	 * @return
	 */
	public Integer countLoanOffs(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  				select count(1)");
		sql.append("\n  				from feeLoanOff loanoff");
		sql.append("\n  				inner join spflowinstance loanOffFlow on loanoff.instanceId=loanOffFlow.id and loanOffFlow.flowstate<>0");
		sql.append("\n  				inner join userinfo loanoffUser on loanoff.creator=loanoffUser.id");
		sql.append("\n 					left join spFlowCurExecutor flowCurExecutor on loanOffFlow.comId = flowCurExecutor.comid");
		sql.append("\n 					and loanOffFlow.id = flowCurExecutor.busId and flowCurExecutor.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 					left join userinfo curSpUser on flowCurExecutor.Executor=curSpUser.id");
		sql.append("\n  				where loanoff.comId=?");
		args.add(curUser.getComId());
		//财务办公能看到的数据
		sql.append("and ((loanOffFlow.FLOWSTATE = '4' and loanOffFlow.SPSTATE = '1') or flowCurExecutor.EXECUTOR = ?) \n");
		args.add(curUser.getId());
		sql.append("\n and	nvl(loanoff.balanceState,0)!=1");
		return this.countQuery(sql.toString(),args.toArray());
	}
	
	/**
	 * 分页查询财务办公报销信息
	 * @param loanOff
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanOff> listPagedLoanOffs(UserInfo curUser, FeeLoanOff loanOff) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n 				select rep.* from (");
		sql.append("\n  				select loanoff.id,loanoff.comId,loanoff.creator,loanoff.isBusinessTrip ,");
		sql.append("\n  				nvl(loanOff.sendNotice, 0) sendNotice,nvl(loanOff.balanceState, 0) balanceState,loanOffFlow.flowName loanOffName,");
		sql.append("\n  				loanOffFlow.flowState,loanoffUser.userName creatorName,loanOff.instanceId,");
		sql.append("\n  				substr(loanOff.Recordcreatetime,0,16) Recordcreatetime,loanOff.loanOffItemFee,loanOff.status,flowCurExecutor.executor,curSpUser.username as executorName");
		sql.append("\n  				from feeLoanOff loanoff");
		sql.append("\n  				inner join spflowinstance loanOffFlow on loanoff.instanceId=loanOffFlow.id and loanOffFlow.flowstate<>0");
		sql.append("\n  				inner join userinfo loanoffUser on loanoff.creator=loanoffUser.id");
		sql.append("\n 					left join spFlowCurExecutor flowCurExecutor on loanOffFlow.comId = flowCurExecutor.comid");
		sql.append("\n 					and loanOffFlow.id = flowCurExecutor.busId and flowCurExecutor.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 					left join userinfo curSpUser on flowCurExecutor.Executor=curSpUser.id");
		sql.append("\n  				where loanoff.comId=?");
		args.add(curUser.getComId());
		//财务办公能看到的数据
		sql.append("and ((loanOffFlow.FLOWSTATE = '4' and loanOffFlow.SPSTATE = '1') or flowCurExecutor.EXECUTOR = ?) \n");
		args.add(curUser.getId());
				
		sql.append("\n 				 ) rep where 1=1 ");
		
		this.addSqlWhere(loanOff.getIsBusinessTrip(), sql, args, "\n and rep.isBusinessTrip=?");
		this.addSqlWhere(loanOff.getLoanRepStatus(), sql, args, "\n and rep.status=?");
		//查询创建时间段
		this.addSqlWhere(loanOff.getStartDate(), sql, args, " and substr(rep.recordcreatetime,0,10)>=?");
		this.addSqlWhere(loanOff.getEndDate(), sql, args, " and substr(rep.recordcreatetime,0,10)<=?");
		this.addSqlWhereLike(loanOff.getLoanOffName(), sql, args, " and rep.loanOffName like ? \n");//流程名称筛选
		if(loanOff.getSendNotice() != null) {
			if(loanOff.getSendNotice() == 1) {
				sql.append("\n 	and  rep.balanceState != 1 and  rep.flowState = 4  and rep.sendNotice = 1");
			}else {
				sql.append("\n 	and  rep.balanceState != 1 and  rep.flowState = 4  and rep.sendNotice != 1");
			}
		}
		if(loanOff.getBalanceState() != null) {
			if(loanOff.getBalanceState() == 1) {
				sql.append("\n 	and  rep.balanceState = 1");
			}else if(loanOff.getBalanceState() == 0) {
				sql.append("\n 	and  rep.balanceState != 1 and  rep.flowState = 4");
			}else if(loanOff.getBalanceState() == 3) {
				sql.append("\n and	nvl(rep.balanceState,0)!=1");
			}else {
				sql.append("\n 	and  rep.flowState != 4");
			}
		}
		if(null != loanOff.getListCreator() && !loanOff.getListCreator().isEmpty()){
			sql.append("	 and  ( rep.creator= 0 ");
			for(UserInfo owner : loanOff.getListCreator()){
				sql.append(" or rep.creator=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		return this.pagedQuery(sql.toString(),"rep.flowState,rep.balanceState ,rep.sendNotice,rep.recordcreatetime desc", args.toArray(), FeeLoanOff.class);
	}
	/**
	 * 查询个人报销信息（办结）
	 * @param userInfo
	 * @return
	 */
	public FeeLoanOff queryFeeLoanOffForPersonalForDone(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select ");
		sql.append("\n count(rownum) as loanOffDoneTimes,");
		sql.append("\n sum(a.loanoffitemfee) as loanDoneItemTotal,");
		sql.append("\n sum(a.loanoffbalance) as loanOffDoneTotal");
		sql.append("\n from feeLoanOff a ");
		sql.append("\n inner join feeBudget b on a.feebudgetid = b.id and a.comid=b.comid and a.creator=b.creator ");
		sql.append("\n where a.comid=? and a.creator=? and a.status=4 and a.balancestate=1");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n group by a.balancestate");
		return (FeeLoanOff)this.objectQuery(sql.toString(), args.toArray(),FeeLoanOff.class);
	}
	/**
	 * 查询个人报销信息（报销中）
	 * @param userInfo
	 * @return
	 */
	public FeeLoanOff queryFeeLoanOffForPersonalForDoing(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select ");
		sql.append("\n count(rownum) as loanOffDoingTimes,");
		sql.append("\n sum(a.loanoffitemfee) as loanDoingItemTotal,");
		sql.append("\n sum(a.loanoffbalance) as loanOffDoingTotal");
		sql.append("\n from (");
		sql.append("\n select 0 as balancestate,'0' as loanoffitemfee,'0' as loanoffbalance");
		sql.append("\n from feeLoanReport a ");
		sql.append("\n inner join feeBudget b on a.feebudgetid=b.id and a.comid=b.comid and a.creator=b.creator");
		sql.append("\n where a.comid=? and a.creator=? and a.status=1");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n union all");
		sql.append("\n select 0 as balancestate,'0' as loanoffitemfee,'0' as loanoffbalance");
		sql.append("\n from feeLoanReport a ");
		sql.append("\n inner join feeBudget b on a.feebudgetid=b.id and a.comid=b.comid and a.creator=b.creator");
		sql.append("\n left join spFlowInstance s on s.id = a.instanceId ");
		sql.append("\n where a.comid=? and a.creator=? and a.status=0 and s.flowState=2");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n union all");
		sql.append("\n select 0 as balancestate,nvl(c.loanOffPreFee,0) as loanoffitemfee,nvl(c.loanoffbalance,0) as loanoffbalance");
		sql.append("\n from feeLoanReport a ");
		sql.append("\n inner join feeBudget b on a.feebudgetid=b.id and a.comid=b.comid and a.creator=b.creator");
		sql.append("\n LEFT JOIN feeLoanOff c on a.id = c.LOANREPORTID ");
		sql.append("\n where a.comid=? and a.creator=? and a.status=4 and nvl(c.status,0) = 0");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n union all");
		sql.append("\n select nvl(a.balancestate,0) balancestate,");
		sql.append("\n a.loanOffPreFee loanoffitemfee,");
		sql.append("\n a.loanoffbalance");
		sql.append("\n from feeLoanOff a ");
		sql.append("\n inner join feeBudget b on a.feebudgetid = b.id and a.comid=b.comid and a.creator=b.creator ");
		sql.append("\n where a.comid=? and a.creator=? and (a.balancestate=0 or a.balancestate is null) and ((a.status in(1,4)) or (a.status=0 and nvl(LOANREPORTID,0)=0 )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		//汇报成功报销失败未重新报销的
		sql.append("\n 		or a.id in(select max(b.id) id from feeBudget  a  inner join feeLoanReport c on a.id=c.feebudgetid ");
		sql.append("\n 		inner join feeLoanOff b on b.loanReportId = c.id LEFT JOIN feeLoanOff b2 on b2.loanReportId = c.id and b2.status !=-1 ");
		sql.append("\n 		where c.status=4 and b.status = -1 and b2.id is null GROUP BY a.id ) ) ");
		//排除异常数据
		sql.append("\n 		and b.id not in ( ");
		sql.append("\n 		SELECT b.id from FEELOANOFF f ");
		sql.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		sql.append("\n 		where f.comid=? and f.STATUS = 0 and f.CREATOR = ? and s.FLOWSTATE= 0 )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n ) a ");
		sql.append("\n group by a.balancestate");
		return (FeeLoanOff)this.objectQuery(sql.toString(), args.toArray(),FeeLoanOff.class);
	}
	/**
	 * 查询个人借款信息(已借款)
	 * @param userInfo
	 * @return
	 */
	public FeeLoan queryFeeLoanForPersonalForDone(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//已经借款成功的
		sql.append("\n select count(rownum) as feeLoanDoneTimes,");
		sql.append("\n sum(a.borrowingBalance) as borrowingBalanceDoneTotal");
		sql.append("\n from feeLoan a ");
		sql.append("\n inner join feeBudget b on a.feebudgetid=b.id and a.comid=b.comid");
		sql.append("\n where a.comid=? and a.creator=? and a.status=4 and a.balanceState = 1");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n group by a.status");
		return (FeeLoan)this.objectQuery(sql.toString(), args.toArray(),FeeLoan.class);
	}
	/**
	 * 查询个人借款信息(申请中)
	 * @param userInfo
	 * @return
	 */
	public FeeLoan queryFeeLoanForPersonalForDoing(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//借款申请中的
		sql.append("\n select count(rownum) as feeLoanDoingTimes,");
		sql.append("\n sum(a.loanmoney) as loanmoneyDoingTotal");
		sql.append("\n from ( ");
		
		//差旅额度借款
		sql.append("\n  select b.loanmoney,a.creator from feeBudget a ");
		sql.append("\n  inner join feeloan b on a.id=b.feebudgetid and a.comid=b.comid");
		sql.append("\n  where a.loanway=1  and a.isBusinessTrip=1 and a.status=4 and b.status in(1,4) and nvl(b.balanceState,0) = 0");
		sql.append("\n  and a.comid=? and a.creator=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		//排除异常数据
		sql.append("\n 		and a.id not in ( ");
		sql.append("\n 		SELECT b.id from FEELOAN f ");
		sql.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		sql.append("\n 		where f.comid=? and f.STATUS = 0 and f.CREATOR = ? and s.FLOWSTATE= 0 )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		//直接额度借款
		sql.append("\n  union all ");
		sql.append("\n  select nvl(b.loanmoney,0) loanmoney,a.creator from feeBudget a ");
		sql.append("\n  left join feeloan b on a.id=b.feebudgetid and a.comid=b.comid");
		sql.append("\n  where a.loanway=1  and a.isBusinessTrip=0 and (a.status=1 or ( a.status=4 and ( nvl(b.status,0)=0 or (nvl(b.status,0) in(1,4) and nvl(b.balanceState,0) = 0))))");
		sql.append("\n  and a.comid=? and a.creator=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		//排除异常数据
		sql.append("\n 		and a.id not in ( ");
		sql.append("\n 		SELECT b.id from FEELOAN f ");
		sql.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		sql.append("\n 		where f.comid=? and f.STATUS = 0 and f.CREATOR = ? and s.FLOWSTATE= 0 )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		//张总借款借款
		sql.append("\n  union all ");
		sql.append("\n  select nvl(b.loanmoney,0) loanmoney,a.creator from feeBudget a ");
		sql.append("\n  inner join feeloan b on a.id=b.feebudgetid and a.comid=b.comid");
		sql.append("\n  where (a.loanway=3 or a.loanway=2) and a.status=4 and nvl(b.status,0) in(0,1,4) and nvl(b.balanceState,0) = 0");
		sql.append("\n  and a.comid=? and a.creator=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		//排除异常数据
		sql.append("\n 		and a.id not in ( ");
		sql.append("\n 		SELECT b.id from FEELOAN f ");
		sql.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		sql.append("\n 		where f.comid=? and f.STATUS = 0 and f.CREATOR = ? and s.FLOWSTATE= 0 )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		
		//草稿
		sql.append("\n 	 UNION ALL SELECT '0' loanmoney,f.creator from FEEBUDGET  f");
		sql.append("\n 	left join SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 	 where f.LOANWAY = 1 and f.CREATOR = ? and f.comid = ? and nvl(s.flowState,0)=2 and  ISBUSINESSTRIP=0");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		sql.append("\n 	 UNION ALL SELECT nvl(l.loanmoney,'0') loanmoney,f.creator from FEELOAN l ");
		sql.append("\n 	 left join FEEBUDGET  f on f.id = l.FEEBUDGETID ");
		sql.append("\n 	left join SPFLOWINSTANCE s on s.id = l.INSTANCEID ");
		sql.append("\n 	 where  l.CREATOR = ? and l.comid = ? and nvl(s.flowState,0)=2 and f.LOANWAY = 1 and  f.ISBUSINESSTRIP=1");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		
		sql.append("\n )a  ");
		sql.append("\n group by a.creator");
		return (FeeLoan)this.objectQuery(sql.toString(), args.toArray(),FeeLoan.class);
	}
	/**
	 * 查询个人消费信息
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Consume> listConsumeForPersonalByStatus(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.status,");
		sql.append("\n count(rownum) as consumeTimes,");
		sql.append("\n sum(a.amount) as amount");
		sql.append("\n from consume a");
		sql.append("\n where a.comid=? and a.creator=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n group by a.status");
		return this.listQuery(sql.toString(), args.toArray(), Consume.class);
	}
	/**
	 * 个人出差统计
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeBudget> queryBusinessTripForPersonal(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//借款申请中的
		sql.append("\n select a.loanoffstate,");
		sql.append("\n count(rownum) as times,");
		sql.append("\n sum(a.loanItemTotal) as loanItemTotal");
		sql.append("\n from feeBudget a");
		sql.append("\n where a.isbusinesstrip=1 and a.comid=? and a.creator=? and a.status=4");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.addSqlWhere(LoanWayEnum.QUOTA.getValue(), sql, args, "\n and a.loanway=?");
		sql.append("\n group by a.loanoffstate");
		return this.listQuery(sql.toString(), args.toArray(), FeeBudget.class);
	}
	/**
	 * 查询借款信息用于确认
	 * @param userInfo 当前操作人员
	 * @param loanId 借款主键
	 * @return
	 */
	public FeeLoan getLoanForBalance(UserInfo userInfo, Integer loanId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,u.userName creatorName ");
		sql.append("\n from feeLoan a ");
		sql.append("\n inner join userinfo u on a.creator=u.id");
		sql.append("\n where a.id=?");
		args.add(loanId);
		return (FeeLoan) this.objectQuery(sql.toString(), args.toArray(), FeeLoan.class);
	}
	
	/**
	 * 查询报销信息用于确认
	 * @param userInfo 当前操作人员
	 * @param loanOffId 报销主键
	 * @return
	 */
	public FeeLoanOff getLoanOffForBalance(UserInfo userInfo, Integer loanOffId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,u.userName creatorName ");
		sql.append("\n from feeLoanOff a ");
		sql.append("\n inner join userinfo u on a.creator=u.id");
		sql.append("\n where a.id=?");
		args.add(loanOffId);
		return (FeeLoanOff) this.objectQuery(sql.toString(), args.toArray(), FeeLoanOff.class);
	}
	/**
	 * 分页查询预算信息
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeBudget> listPagedFeeBudgetForInit(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*");
		sql.append("\n from feeBudget a ");
		sql.append("\n where a.comId=?");
		args.add(comId);
		return this.pagedQuery(sql.toString(), " a.id ", args.toArray(), FeeBudget.class);
	}
	/**
	 * 分页查询预算信息
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoan> listPagedFeeLoanForInit(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*");
		sql.append("\n from feeLoan a ");
		sql.append("\n where a.comId=? and feeBudgetId=0 ");
		args.add(comId);
		return this.pagedQuery(sql.toString(), " a.id ", args.toArray(), FeeLoan.class);
	}
	/**
	 * 分页查询预算信息
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanOff> listPagedFeeLoanOffForInit(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*");
		sql.append("\n from feeLoanOff a ");
		sql.append("\n where a.comId=? and feeBudgetId=0 ");
		args.add(comId);
		return this.pagedQuery(sql.toString(), " a.id ", args.toArray(), FeeLoanOff.class);
	}
	/**
	 * 查询预算的所有借款数据
	 * @param feeBudget
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List listFeeLoanForInit(FeeBudget feeBudget,Class clz) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*");
		sql.append("\n from "+ clz.getSimpleName()+" a ");
		sql.append("\n where a.comId=? and a.feeBudgetId=?");
		args.add(feeBudget.getComId());
		args.add(feeBudget.getId());
		return this.listQuery(sql.toString(), args.toArray(), clz);
	}
	/**
	 * 获取个人借款中需要显示的详情
	 * @param userInfo
	 * @return
	 */
	public FeeLoan getLoanForDetails(FeeLoan loan,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		if(null != loan.getInstanceId() && loan.getInstanceId() > 0){//查询借款
			sql.append("\n	select c.flowSerialNumber,b.status,nvl(b.borrowingbalance,0) borrowingbalance,b.balanceTime,b.payType,b.balanceState,u.userName balanceUserName,");
			sql.append("\n	b.allowedquota,c.flowname,d.userName as creatorName,b.recordcreatetime,c.id as instanceId,a.loanWay,a.id as feebudgetid,a.isbusinesstrip");
			sql.append("\n	from feebudget a ");
			sql.append("\n	inner join feeloan b on a.id = b.feebudgetid and a.comid = b.comid and a.comid = ? ");
			args.add(userInfo.getComId());
			sql.append("\n	inner join spflowinstance c on b.instanceid = c.id and c.comid = b.comid");
			sql.append("\n	left join userinfo d on d.id = a.creator");
			sql.append("\n	left join userinfo u on u.id = b.balanceUserId");
			sql.append("\n	where c.id = ?");
			args.add(loan.getInstanceId());
		}else if(null != loan.getLoanApplyInsId() && loan.getLoanApplyInsId() > 0){//查询出差申请
			sql.append("\n	select a.flowSerialNumber,a.flowName as loanApplyInsName,b.status,b.startTime as loanApplyStartDate,b.loanWay,b.id as feebudgetid,f.balanceTime,f.payType,f.balanceState,u.userName balanceUserName,");
			sql.append("\n	b.endTime as loanApplyEndDate,b.status as loanApplyState,c.userName as creatorName,b.recordcreatetime,a.id as loanApplyInsId,b.isbusinesstrip");
			sql.append("\n	from spflowinstance a ");
			sql.append("\n	inner join feebudget b on a.id = b.instanceid and a.comid = b.comid and b.comid = ?");
			args.add(userInfo.getComId());
			sql.append("\n	left join userinfo c on b.creator = c.id");
			sql.append("\n	left join feeloan f on f.instanceId = a.id and f.comid = a.comid ");
			sql.append("\n	left join userinfo u on u.id = f.balanceUserId");
			sql.append("\n	where a.id = ?");
			args.add(loan.getLoanApplyInsId());
		}else{
			return null;
		}
		return (FeeLoan) this.objectQuery(sql.toString(), args.toArray(), FeeLoan.class);
	}

	/**
	 * 分页获取报销记录
	 * @param curUser 当前操作人
	 * @param feeLoanOff 报销筛选条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanOff> listPersonalLoanOffOfAuthV2(UserInfo curUser,FeeLoanOff feeLoanOff) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select loanOff.* from (");
		sql.append("\n 		select a.creator,u.username,repFlow.Flowname loanReportName,case when rep.bustripstate = 1 then rep.instanceid else 0 end as loanRepInsId,");
		sql.append("\n 			a.id  Feebudgetid,a.isbusinesstrip,rep.status loanRepStatus,a.loanway,rep.id loanReportId,");
		sql.append("\n 			nvl(loanOff.recordcreatetime,rep.recordcreatetime) recordcreatetime,");
		sql.append("\n 			nvl(offExecutor.Executor,repExecutor.Executor) executor,");
		sql.append("\n 			nvl(offSpUser.userName,repSpUser.userName) executorName,");
		sql.append("\n 			loanOff.id,nvl(loanOff.Instanceid,0) instanceId,loanOffFlow.flowname loanOffName,nvl(loanOff.Status,0) Status,");
		sql.append("\n 			nvl(loanOff.balanceState,0) balanceState,nvl(s.id,0) canReApply,");
		sql.append("\n 			case when rep.status=-1 or loanOff.status=-1 then 6");
		sql.append("\n 				when rep.status=1 then 1");
		sql.append("\n 				when rep.status=4 and nvl(loanOff.status,0)=0 then 2");
		sql.append("\n 				when rep.status=4 and nvl(loanOff.status,0)=1 then 3");
		sql.append("\n 				when rep.status=4 and nvl(loanOff.status,0)=4 and nvl(loanOff.balanceState,0)=0 then 4");
		sql.append("\n 				when rep.status=4 and nvl(loanOff.status,0)=4 and nvl(loanOff.balanceState,0)=1 then 5");
		sql.append("\n 			else 0");
		sql.append("\n 			end neworder,nvl(repFlow.flowState,0)repFlowState,sp.flowName loanApplyName,sp.id loanApplyInsId");
		sql.append("\n 		from feebudget a ");
		sql.append("\n 		left join spflowinstance sp on sp.id = a.instanceId ");
		sql.append("\n 		inner join userinfo u on a.creator=u.id");
		sql.append("\n 		inner join feeloanreport rep on a.id=rep.feebudgetid");
		sql.append("\n 		inner join spflowinstance repFlow on a.comid=repFlow.comId and rep.instanceid=repflow.id and repFlow.flowState != 0");
		sql.append("\n 		left join spFlowCurExecutor repExecutor on repFlow.comId = repExecutor.comid");
		sql.append("\n 		and repFlow.id = repExecutor.busId and repExecutor.busType='022' and repExecutor.executeType='assignee'");
		sql.append("\n 		left join userinfo repSpUser on repExecutor.Executor=repSpUser.id");
		sql.append("\n ");
		sql.append("\n 		left join feeloanoff loanOff on a.id=loanOff.Feebudgetid and rep.id=loanOff.Loanreportid ");
		//排除无效数据
		sql.append("\n 			and loanOff.id not in(select f.id from FEELOANOFF f inner JOIN SPFLOWINSTANCE s on s.id = f.instanceId where s.FLOWSTATE=0)");
		
		sql.append("\n 		left join spflowinstance loanOffFlow on a.comid=loanOffFlow.comId and loanOff.instanceid=loanOffFlow.id and nvl(loanOffFlow.flowState,0) != 0 ");
		sql.append("\n 		left join spFlowCurExecutor offExecutor on loanOffFlow.comId = offExecutor.comid");
		sql.append("\n 		and loanOffFlow.id = offExecutor.busId and offExecutor.busType='022' and offExecutor.executeType='assignee'");
		sql.append("\n 		left join userinfo offSpUser on offExecutor.Executor=offSpUser.id");
		//查询区分是否为汇报成功报销失败未重新报销的
		sql.append("\n 		left join (select max(b.id) id from feeBudget  a  inner join feeLoanReport c on a.id=c.feebudgetid ");
		sql.append("\n 		inner join feeLoanOff b on b.loanReportId = c.id LEFT JOIN feeLoanOff b2 on b2.loanReportId = c.id and b2.status !=-1 ");
		sql.append("\n 		left join spFlowInstance s on s.id = b2.instanceId ");
		sql.append("\n 		where c.status=4 and b.status = -1 and (b2.id is null or nvl(s.flowState,0) = 0) GROUP BY a.id )s on s.id = loanOff.id ");
		
		sql.append("\n 		where a.comid=? and a.creator=? and a.status=? ");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		
		this.addSqlWhere(feeLoanOff.getIsBusinessTrip(), sql, args, " and  a.isBusinessTrip=?");//是否为差旅
		
		sql.append("\n  union ");
		sql.append("\n 		select a.creator,u.username,'' as loanReportName,0 as loanRepInsId,");
		sql.append("\n 			a.id  Feebudgetid,a.isbusinesstrip,4 loanRepStatus,a.loanway,null loanReportId,");
		sql.append("\n 			loanOff.recordcreatetime,offExecutor.Executor,offSpUser.userName executorName,");
		sql.append("\n 			loanOff.id,loanOffFlow.id as Instanceid,loanOffFlow.flowname loanOffName,loanOff.Status,");
		sql.append("\n 			nvl(loanOff.balanceState,0) balanceState,0 canReApply,");
		sql.append("\n 			case when loanOff.status=-1 then 6");
		sql.append("\n 				when nvl(loanOff.status,0)=0 then 2");
		sql.append("\n 				when nvl(loanOff.status,0)=1 then 3");
		sql.append("\n 				when nvl(loanOff.status,0)=4 and nvl(loanOff.balanceState,0)=0 then 4");
		sql.append("\n 				when nvl(loanOff.status,0)=4 and nvl(loanOff.balanceState,0)=1 then 5");
		sql.append("\n 			else 0");
		sql.append("\n 			end neworder,4 repFlowState,sp.flowName loanApplyName,sp.id loanApplyInsId");
		sql.append("\n 		from feebudget a ");
		sql.append("\n 		left join spflowinstance sp on sp.id = a.instanceId ");
		sql.append("\n 		inner join userinfo u on a.creator=u.id");
		sql.append("\n 		inner join feeloanoff loanOff on a.id=loanOff.Feebudgetid");
		sql.append("\n 		inner join spflowinstance loanOffFlow on a.comid=loanOffFlow.comId and loanOff.instanceid=loanOffFlow.id and loanOffFlow.flowstate<>0");
		sql.append("\n 		left join spFlowCurExecutor offExecutor on loanOffFlow.comId = offExecutor.comid");
		sql.append("\n 		and loanOffFlow.id = offExecutor.busId and offExecutor.busType='022' and offExecutor.executeType='assignee'");
		sql.append("\n 		left join userinfo offSpUser on offExecutor.Executor=offSpUser.id");
		sql.append("\n 		where a.comid=? and a.creator=? and a.status=? and nvl(loanOff.Loanreportid,0)=0");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		this.addSqlWhere(feeLoanOff.getIsBusinessTrip(), sql, args, " and  a.isBusinessTrip=?");//是否为差旅

		//这儿是为了剔除掉一般费用说明的显示
		sql.append("\n )loanOff where ((loanOff.Instanceid <> 0 and loanOff.loanrepinsid = 0) or (loanOff.Instanceid = 0 and loanOff.loanrepinsid <> 0) or (loanOff.Instanceid <> 0 and loanOff.loanrepinsid <> 0)) ");
		
		Integer  status = feeLoanOff.getStatus();
		if(null!=status ){
			switch(status){
				case 6://申请中
					sql.append("\n and ( ((loanOff.balanceState=0 or loanOff.balanceState is null) and loanOff.status=4)");
					sql.append("\n 		or(loanOff.loanRepStatus=4 and loanOff.status=0)");
					sql.append("\n 		or(LoanOff.loanRepStatus=1 or loanOff.status=1) ");
					//汇报成功报销失败未重新报销的
					sql.append("\n 		or loanOff.id in (select max(b.id) id from feeBudget  a  inner join feeLoanReport c on a.id=c.feebudgetid ");
					sql.append("\n 		inner join feeLoanOff b on b.loanReportId = c.id LEFT JOIN feeLoanOff b2 on b2.loanReportId = c.id and b2.status !=-1 ");
					sql.append("\n 		left join spFlowInstance s on s.id = b2.instanceId ");
					sql.append("\n 		where c.status=4 and b.status = -1 and (b2.id is null or nvl(s.flowState,0) = 0) GROUP BY a.id ) or (loanOff.loanRepStatus=0 and loanOff.repFlowState=2) ) ");
					break;
				case 5://已领款
					sql.append("\n and loanOff.balanceState=1");
					break;
				case 4://待领款
					sql.append("\n and (loanOff.balanceState=0 or loanOff.balanceState is null) and loanOff.status=4");
					break;
				case 3://待报销
					sql.append("\n and loanOff.loanRepStatus=4 and loanOff.status=0");
					break;
				case 1://借款中
					sql.append("\n and (LoanOff.loanRepStatus=1 or loanOff.status=1 )");
					break;
				default:
					sql.append("\n and (loanOff.loanRepStatus=-1 or loanOff.status=-1 )");
					break;
			}
		}
		this.addSqlWhereLike(feeLoanOff.getLoanOffName(), sql, args, " and (loanOff.loanReportName like ? or  loanOff.loanOffName like ?) \n",2);//流程名称筛选
		//日期筛选
		this.addSqlWhere(feeLoanOff.getStartDate(),sql,args,"and substr(loanOff.recordcreatetime,0,10) >= ?");
		this.addSqlWhere(feeLoanOff.getEndDate(),sql,args,"and substr(loanOff.recordcreatetime,0,10) <= ?");
		return this.pagedQuery(sql.toString(), "loanOff.recordcreatetime desc,loanOff.neworder,loanOff.loanRepInsId desc,loanOff.instanceId desc ", args.toArray(), FeeLoanOff.class);
	}

	/**
	 * 分页获取个人借款记录
	 * @param sessionUser 当前操作人
	 * @param feeLoan 借款筛选参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoan> listPagedPersonalLoanOfAuth(UserInfo sessionUser, FeeLoan feeLoan) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select loan.* from (");
		sql.append("\n 		select a.creator,u.username,apyFlow.flowname loanApplyInsName,a.instanceid loanApplyInsId,");
		sql.append("\n 		a.id feeBudgetId,a.isBusinessTrip,a.status loanApplyState,a.loanway,");
		sql.append("\n 		nvl(b.recordcreatetime,a.recordcreatetime) recordcreatetime,");
		sql.append("\n 		nvl(loanFlowCurExecutor.Executor,apyFlowCurExecutor.Executor) executor,");
		sql.append("\n 		b.id,b.instanceid,loanflow.flowname,nvl(b.status,0) status,nvl(b.balanceState,0) balanceState,");
		//审批执行人
		sql.append("\n 		case when nvl(a.instanceId,0)>0 and a.status=1 then apyCurSpUser.userName else loanCurSpUser.userName end  executorName,");
		sql.append("\n 		case when a.status=-1 or b.status=-1 then 6");
		sql.append("\n 			when a.status=1 then 1");
		sql.append("\n 			when a.status=4 and nvl(b.status,0)=0 then 2");
		sql.append("\n 			when a.status=4 and nvl(b.status,0)=1 then 3");
		sql.append("\n 			when a.status=4 and nvl(b.status,0)=4 and nvl(b.balanceState,0)=0 then 4");
		sql.append("\n 			when a.status=4 and nvl(b.status,0)=4 and nvl(b.balanceState,0)=1 then 5");
		sql.append("\n 			else 0");
		sql.append("\n 		end neworder");
		//可以借款的预算
		sql.append("\n 		from feebudget a");
		//申请人员
		sql.append("\n 		inner join userinfo u on a.creator=u.id");
		//预算申请流程表
		sql.append("\n 		left join spflowinstance apyFlow on a.instanceid=apyFlow.id");
		
		sql.append("\n 		left join spFlowCurExecutor apyFlowCurExecutor on apyFlow.comId = apyFlowCurExecutor.comid ");
		sql.append("\n 			and apyFlow.id = apyFlowCurExecutor.busId and apyFlowCurExecutor.busType=? and apyFlowCurExecutor.executeType='assignee'");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 		left join userinfo apyCurSpUser on apyFlowCurExecutor.Executor=apyCurSpUser.id");
		
		//借款表
		sql.append("\n 		left join feeloan b on a.id=b.feebudgetid");
		//借款流程表
		sql.append("\n 		left join spflowinstance loanflow on b.instanceid=loanflow.id");
		sql.append("\n 		left join spFlowCurExecutor loanFlowCurExecutor on loanFlow.comId = loanFlowCurExecutor.comid");
		sql.append("\n 		and loanFlow.id = loanFlowCurExecutor.busId and loanFlowCurExecutor.busType=? and loanFlowCurExecutor.executeType='assignee'");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 		left join userinfo loanCurSpUser on loanFlowCurExecutor.Executor=loanCurSpUser.id");
		//预算审核通过的
		sql.append("\n 		where 1=1 ");
		//申请人筛选
		this.addSqlWhere(sessionUser.getId(), sql, args, " and  a.creator=?");//流程发起人筛选
		this.addSqlWhere(feeLoan.getIsBusinessTrip(), sql, args, " and  a.isBusinessTrip=?");//是否为差旅
		sql.append("\n )loan where 1=1 and (nvl(loan.loanApplyInsId,0) >0 or nvl(loan.instanceid,0) >0 )");
		//排除异常数据
		sql.append("\n 		and (( loan.feebudgetid not in ( ");
		sql.append("\n 		SELECT b.id from FEELOAN f ");
		sql.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		sql.append("\n 		where f.comid=? and f.STATUS = 0 and f.CREATOR = ? and s.FLOWSTATE= 0 )");
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		Integer  status = feeLoan.getStatus();
		if(null!=status ){
			switch(status){
				case 6://申请中
					sql.append("\n and ( ((loan.balanceState=0 or loan.balanceState is null) and loan.status=4)");
					sql.append("\n 		or(loan.loanApplyState=4 and loan.status=0)");
					sql.append("\n 		or(loan.loanApplyState=1 or loan.status=1)) and loan.status != -1");
					break;
				case 5://已领款
					sql.append("\n and loan.balanceState=1");
					break;
				case 4://待领款
					sql.append("\n and (loan.balanceState=0 or loan.balanceState is null) and loan.status=4");
					break;
				case 3://待借款
					sql.append("\n and loan.loanApplyState=4 and loan.status=0");
					break;
				case 1://待借款
					sql.append("\n and (loan.loanApplyState=1 or loan.status=1 )");
					break;
				default:
					sql.append("\n and (loan.loanApplyState=-1 or loan.status=-1 )");
					break;
			}
			
		}
		
		sql.append("\n 		and ( ");
		sql.append("\n 				(loan.loanway=? and loan.isBusinessTrip=?)");//招待活动的借款
		args.add(LoanWayEnum.QUOTA.getValue());
		args.add(TripFeeEnum.NO.getValue());
		sql.append("\n 				or (loan.loanway=? and loan.isBusinessTrip=? and loan.loanApplyState=4 and loan.status<>0)");//出差借款
		args.add(LoanWayEnum.QUOTA.getValue());
		args.add(TripFeeEnum.YES.getValue());
		
		sql.append("\n 				or loan.loanway=?");//直接借款
		args.add(LoanWayEnum.DIRECT.getValue());
		sql.append("\n 				or loan.loanway=?");//张总借款
		args.add(LoanWayEnum.RELATEITEM.getValue());
		sql.append("\n 		))");
		if(status == null || status == 3 || status == 6) {
			//草稿
			sql.append("\n 		or loan.feeBudgetId in(");
			sql.append("\n 	 select f.id from FEEBUDGET  f");
			sql.append("\n 	left join SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
			sql.append("\n 	 where f.LOANWAY = 1 and f.CREATOR = ? and f.comid = ? and nvl(s.flowState,0)=2 and  ISBUSINESSTRIP=0");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			sql.append("\n 	 union SELECT f.id from FEELOAN l ");
			sql.append("\n 	 left join FEEBUDGET  f on f.id = l.FEEBUDGETID ");
			sql.append("\n 	left join SPFLOWINSTANCE s on s.id = l.INSTANCEID ");
			sql.append("\n 	 where  l.CREATOR = ? and l.comid = ? and nvl(s.flowState,0)=2 and f.LOANWAY = 1");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			sql.append("\n 	 ) ");
			if(status != null) {
				sql.append("\n 	and loan.status !=-1 and nvl(loan.balanceState,0) != 1");
			}
		}
		sql.append("\n 	) ");	
		
		//查询创建时间段
		this.addSqlWhere(feeLoan.getStartDate(), sql, args, " and substr(loan.recordcreatetime,0,10)>=?");
		this.addSqlWhere(feeLoan.getEndDate(), sql, args, " and substr(loan.recordcreatetime,0,10)<=?");
		this.addSqlWhereLike(feeLoan.getFlowName(), sql, args, " and (loan.loanApplyInsName like ? or loan.flowName like ? )\n",2);//流程名称筛选
		return this.pagedQuery(sql.toString(), "loan.recordcreatetime desc,loan.neworder,loan.loanApplyInsId desc,loan.instanceId desc ", args.toArray(), FeeLoan.class);
	}

	/**
	 * 获取个人借款记录总数
	 * @param curUser 当前操作人
	 * @return
	 */
	public Integer countPersonalLoanOfAuthV2(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("\n select count(1) from feebudget a");
		sql.append("\n 	left join feeloan b on a.id=b.feebudgetid");
		sql.append("\n 		left join spflowinstance loanflow on b.instanceid=loanflow.id");
		sql.append("\n 		left join spflowinstance apyFlow on a.instanceid=apyFlow.id");
		sql.append("\n 	where 1=1 and (nvl(apyFlow.id,0) > 0 or nvl(loanflow.id,0) > 0)");
		//排除异常数据
		sql.append("\n 		and ( (a.id not in ( ");
		sql.append("\n 		SELECT b.id from FEELOAN f ");
		sql.append("\n 		inner JOIN SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 		INNER JOIN FEEBUDGET b on b.id = f.FEEBUDGETID ");
		sql.append("\n 		where f.comid=? and f.STATUS = 0 and f.CREATOR = ? and s.FLOWSTATE= 0 )");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		sql.append("\n and ( ((b.balanceState=0 or b.balanceState is null) and b.status=4)");
		sql.append("\n 		or(a.status=4 and nvl(b.status,0)=0)");
		sql.append("\n 		or(a.status=1 or b.status=1) )");
		//申请人筛选
		this.addSqlWhere(curUser.getId(), sql, args, " and  a.creator=?");//流程发起人筛选
		
		sql.append("\n 		and ( ");
		sql.append("\n 				(a.loanway=? and a.isBusinessTrip=?)");//招待活动的借款
		args.add(LoanWayEnum.QUOTA.getValue());
		args.add(TripFeeEnum.NO.getValue());
		sql.append("\n 				or (a.loanway=? and a.isBusinessTrip=? and a.status=4 and b.status<>0)");//出差借款
		args.add(LoanWayEnum.QUOTA.getValue());
		args.add(TripFeeEnum.YES.getValue());
		
		sql.append("\n 				or a.loanway=?");//直接借款
		args.add(LoanWayEnum.DIRECT.getValue());
		sql.append("\n 				or a.loanway=?");//张总借款
		args.add(LoanWayEnum.RELATEITEM.getValue());
		sql.append("\n 		) )");
		//草稿
		sql.append("\n 		or a.id in(");
		sql.append("\n 	 select f.id from FEEBUDGET  f");
		sql.append("\n 	left join SPFLOWINSTANCE s on s.id = f.INSTANCEID ");
		sql.append("\n 	 where f.LOANWAY = 1 and f.CREATOR = ? and f.comid = ? and nvl(s.flowState,0)=2 and  ISBUSINESSTRIP=0");
		args.add(curUser.getId());
		args.add(curUser.getComId());
		sql.append("\n 	 union SELECT f.id from FEELOAN l ");
		sql.append("\n 	 left join FEEBUDGET  f on f.id = l.FEEBUDGETID ");
		sql.append("\n 	left join SPFLOWINSTANCE s on s.id = l.INSTANCEID ");
		sql.append("\n 	 where  l.CREATOR = ? and l.comid = ? and nvl(s.flowState,0)=2 and f.LOANWAY = 1");
		args.add(curUser.getId());
		args.add(curUser.getComId());
		sql.append("\n 	) and nvl(b.status,0) !=-1 ) and nvl(b.balanceState,0) != 1 ");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 获取报销记录总数(申请中)
	 * @param curUser 当前操作人
	 * @return
	 */
	public Integer countPersonalLoanOffOfAuthV2(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(1)from (");
		sql.append("\n 		select a.id,loanOff.id loanOffId,nvl(loanOff.instanceid,0) instanceid,nvl(offExecutor.Executor,repExecutor.Executor) executor,case when rep.bustripstate = 1 then rep.instanceid else 0 end as loanRepInsId,rep.status loanRepStatus,nvl(loanOff.balanceState,0) balanceState,nvl(loanOff.Status,0) Status,nvl(repFlow.flowState,0) flowState");
		sql.append("\n 		from feebudget a ");
		sql.append("\n 		inner join feeloanreport rep on a.id=rep.feebudgetid");
		sql.append("\n 		inner join spflowinstance repFlow on a.comid=repFlow.comId and rep.instanceid=repflow.id");
		sql.append("\n 		left join feeloanoff loanOff on a.id=loanOff.Feebudgetid and rep.id=loanOff.Loanreportid");
		sql.append("\n 		left join spFlowCurExecutor repExecutor on rep.comId = repExecutor.comid");
		sql.append("\n 			and rep.instanceid = repExecutor.busId and repExecutor.busType='022' and repExecutor.executeType='assignee'");
		sql.append("\n 		left join spFlowCurExecutor offExecutor on loanOff.comId = offExecutor.comid");
		sql.append("\n 			and loanOff.instanceid = offExecutor.busId and offExecutor.busType='022' and offExecutor.executeType='assignee'");
		sql.append("\n 		where a.comid=? and a.creator=? and a.status=?");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		sql.append("\n  union ");
		sql.append("\n 		select a.id,loanOff.id loanOffId,loanOff.Instanceid,offExecutor.Executor,0 as loanRepInsId,4 loanRepStatus,nvl(loanOff.balanceState,0) balanceState,nvl(loanOff.Status,0) Status,0 flowState");
		sql.append("\n 		from feebudget a ");
		sql.append("\n 		inner join feeloanoff loanOff on a.id=loanOff.Feebudgetid");
		sql.append("\n 		inner join spflowinstance loanOffFlow on a.comid=loanOffFlow.comId and loanOff.instanceid=loanOffFlow.id and loanOffFlow.flowstate<>0");
		sql.append("\n 		left join spFlowCurExecutor offExecutor on loanOff.comId = offExecutor.comid");
		sql.append("\n 			and loanOff.instanceid = offExecutor.busId and offExecutor.busType='022' and offExecutor.executeType='assignee'");
		sql.append("\n 		where a.comid=? and a.creator=? and a.status=? and nvl(loanOff.Loanreportid,0)=0");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		args.add(ConstantInterface.SP_STATE_FINISH);
		//这儿是为了剔除掉一般费用说明的显示
		sql.append("\n )loanOff where ((loanOff.Instanceid <> 0 and loanOff.loanrepinsid = 0) or (loanOff.Instanceid = 0 and loanOff.loanrepinsid <> 0) or (loanOff.Instanceid <> 0 and loanOff.loanrepinsid <> 0))");
		sql.append("\n and ( ((loanOff.balanceState=0 or loanOff.balanceState is null) and loanOff.status=4)");
		sql.append("\n 		or(loanOff.loanRepStatus=4 and loanOff.status=0)");
		sql.append("\n 		or(LoanOff.loanRepStatus=1 or loanOff.status=1) ");
		//汇报成功报销失败未重新报销的
		sql.append("\n 		or loanOff.loanOffId in (select max(b.id) id from feeBudget  a  inner join feeLoanReport c on a.id=c.feebudgetid ");
		sql.append("\n 		inner join feeLoanOff b on b.loanReportId = c.id LEFT JOIN feeLoanOff b2 on b2.loanReportId = c.id and b2.status !=-1 ");
		sql.append("\n 		where c.status=4 and b.status = -1 and b2.id is null GROUP BY a.id ) or (loanOff.loanRepStatus=0 and loanOff.flowState=2) ) ");
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 根据insid查询借款或者报销Id
	 * @param busId
	 * @return
	 */
	public FeeLoan queryLoanByInsId(Integer insId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n SELECT nvl(lo.id,lf.id) id from SPFLOWINSTANCE s");
		sql.append("\n LEFT JOIN FEELOAN lo on lo.INSTANCEID = s.id");
		sql.append("\n LEFT JOIN FEELOANOFF lf on lf.INSTANCEID = s.id");
		sql.append("\n where s.id =?");
		args.add(insId);
		return (FeeLoan) this.objectQuery(sql.toString(), args.toArray(), FeeLoan.class);
	}

	/**
	 * 查询报销中的记录信息
	 * @param feeBudgetId
	 * @return
	 */
	public FeeLoanOff queryFeeLoanOffOnDoing(Integer feeBudgetId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//报销处于总结阶段
		sql.append("\n select a.id as feeBudgetId,b.instanceid,'feeLoanReport' as stepType,b.id loanReportId");
		sql.append("\n from feeBudget a");
		sql.append("\n inner join feeLoanReport b");
		sql.append("\n on a.id=b.feebudgetid and a.comid=b.comid ");
		sql.append("\n left join spFlowInstance s on s.id = b.instanceId ");
		sql.append("\n where a.id=? and b.status in (0,1) and s.flowState != 0");
		args.add(feeBudgetId);
		//报销处于总结完了，但是未发起报销单阶段
		sql.append("\n union all");
		sql.append("\n select a.id as feeBudgetId,c.instanceid,'feeLoanOff' as stepType,b.id loanReportId");
		sql.append("\n from feeBudget a ");
		sql.append("\n inner join feeLoanReport b on a.id=b.feebudgetid and b.status=4");
		sql.append("\n left join feeLoanOff c");
		sql.append("\n on a.id=c.feebudgetid and b.id=c.loanreportid");
		sql.append("\n where a.id=? and c.id is null");
		args.add(feeBudgetId);
		//报销处于报销单阶段
		sql.append("\n union all");
		sql.append("\n select a.id as feeBudgetId,c.instanceid,'feeLoanOff' as stepType,b.id loanReportId");
		sql.append("\n from feeBudget a ");
		sql.append("\n inner join feeLoanReport b on a.id=b.feebudgetid and b.status=4");
		sql.append("\n inner join feeLoanOff c");
		sql.append("\n on a.id=c.feebudgetid and b.id=c.loanreportid");
		sql.append("\n left join spFlowInstance s on s.id = c.instanceId ");
		sql.append("\n where a.id=? and c.status in (0,1,4) and s.flowState != 0 and nvl(c.balanceState,0) != 1");
		args.add(feeBudgetId);
		return (FeeLoanOff)this.objectQuery(sql.toString(), args.toArray(),FeeLoanOff.class);
	}
	
	/**
	 * 查询汇报成功未报销的记录信息
	 * @param feeBudgetId
	 * @return
	 */
	public FeeLoanOff queryFeeLoanOffUnOff(Integer feeBudgetId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id as feeBudgetId,c.instanceid,'feeLoanOff' as stepType,b.id loanReportId");
		sql.append("\n from feeBudget a ");
		sql.append("\n inner join feeLoanReport b on a.id=b.feebudgetid and b.status=4");
		sql.append("\n left join feeLoanOff c");
		sql.append("\n on a.id=c.feebudgetid and b.id=c.loanreportid");
		sql.append("\n left join spFlowInstance s on s.id = c.instanceId");
		sql.append("\n where a.id=? and (c.id is null or s.flowState=0)");
		args.add(feeBudgetId);
		return (FeeLoanOff)this.objectQuery(sql.toString(), args.toArray(),FeeLoanOff.class);
	}
	

	/**
	 * 查询已完结的报销
	 * @param feeBudgetId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanOff> queryFeeLoanOffDone(Integer feeBudgetId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id as feeBudgetId,c.instanceid,'feeLoanOff' as stepType");
		sql.append("\n from feeBudget a ");
		sql.append("\n inner join feeLoanOff c");
		sql.append("\n on a.id=c.feebudgetid");
		sql.append("\n where a.id=? and c.status=4");
		args.add(feeBudgetId);
		return this.listQuery(sql.toString(), args.toArray(),FeeLoanOff.class);
	}
	
	/**
	 * 查询汇报成功并且报销失败的记录
	 * @param feeBudgetId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanReport> queryFeeLoanReport(Integer feeBudgetId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id as feeBudgetId,c.id");
		sql.append("\n from feeBudget a ");
		sql.append("\n inner join feeLoanReport c");
		sql.append("\n 	on a.id=c.feebudgetid");
		sql.append("\n inner join feeLoanOff b on b.loanReportId = c.id");
		sql.append("\n LEFT JOIN feeLoanOff b2 on b2.loanReportId = c.id and b2.status = 4");
		sql.append("\n where a.id=? and c.status=4 and b.status = -1 and b2.id is null");
		args.add(feeBudgetId);
		return this.listQuery(sql.toString(), args.toArray(),FeeLoanReport.class);
	}

	/**
	 * 查询成功借款
	 * @param feeBudgetId
	 * @param creator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoan> listRecentFeeLoan(Integer feeBudgetId, Integer creator) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from FeeLoan a");
		sql.append("\n where a.feeBudgetId=? and a.creator=?");
		args.add(feeBudgetId);
		args.add(creator);
		sql.append("\n order by a.status desc");
		return this.listQuery(sql.toString(), args.toArray(), FeeLoan.class);
	}
	/**
	 * 查询成功销账
	 * @param feeBudgetId
	 * @param loanReportId
	 * @param creator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeLoanOff> listAllLoanOff(Integer feeBudgetId,
			Integer loanReportId, Integer creator,Integer isBusinessTrip) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.* from feeBudget a");
		sql.append("\n inner join feeLoanOff b on a.id=b.feeBudgetId");
		sql.append("\n where a.id=? and a.creator=? and a.isBusinessTrip=?");
		args.add(feeBudgetId);
		args.add(creator);
		args.add(isBusinessTrip);
		
		if(null!=loanReportId && loanReportId>0){
			this.addSqlWhere(loanReportId, sql, args, "\n and b.loanReportId = ?");
		}
		sql.append("\n order by b.status desc");
		return this.listQuery(sql.toString(), args.toArray(), FeeLoanOff.class);
	}

	/**
	 * 查询预算信息
	 * @param feeBudgetId
	 * @return
	 */
	public FeeBudget getBaseFeeBudGetById(Integer feeBudgetId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,spflow.flowName,fdb.content,fdb.RECORDCREATETIME directBalanceTime,u.userName directBalanceUserName from feeBudget a");
		sql.append("\n left join spflowinstance spflow on spflow.id=a.instanceId");
		sql.append("\n left join feeDirectBalance  fdb on fdb.feeBudgetId=a.id");
		sql.append("\n left join userInfo  u on fdb.creator=u.id");
		sql.append("\n where 1=1 and a.id=?");
		args.add(feeBudgetId);
		return (FeeBudget) this.objectQuery(sql.toString(), args.toArray(), FeeBudget.class);
	}
	
	/**
	 * 根据汇报主键获取依据详情
	 * @param loanReportId
	 * @param comId
	 * @return
	 */
	public FeeBudget queryFeeBudgetByLoanReport(Integer loanReportId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.instanceId,a.startTime,a.endTime,a.tripPlace,a.allowedQuota,a.loanFeeTotal,a.loanOffTotal,a.loanItemTotal");
		sql.append("\n from feeBudget a ");
		sql.append("\n inner join feeLoanReport b on a.id=b.feebudgetid and a.comid=b.comid");
		sql.append("\n where b.id=? and b.comid=?");
		args.add(loanReportId);
		args.add(comId);
		return (FeeBudget)this.objectQuery(sql.toString(), args.toArray(),FeeBudget.class);
	}

	/**
	 * 获取出差依据关联数据集合
	 * @param feeBudgetId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeeBusMod> listFeeBusMod(Integer feeBudgetId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.busid,a.bustype,a.tripbusname ");
		sql.append("\n from feeBusMod a where a.feebudgetid=? and a.comid=?");
		args.add(feeBudgetId);
		args.add(comId);
		return this.listQuery(sql.toString(), args.toArray(),FeeBusMod.class);
	}

	/**
	 * 根据出差报销主键获取依据详情
	 * @param feeLoanOffId
	 * @param comId
	 * @return
	 */
	public FeeBudget queryFeeBudgetByFeeLoanOff(Integer feeLoanOffId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.instanceId,a.startTime,a.endTime,a.tripPlace,a.allowedQuota,a.loanFeeTotal,a.loanOffTotal,a.loanItemTotal");
		sql.append("\n from feeBudget a ");
		sql.append("\n inner join feeLoanOff b on a.id=b.feebudgetid and a.comid=b.comid");
		sql.append("\n where b.id=? and b.comid=?");
		args.add(feeLoanOffId);
		args.add(comId);
		return (FeeBudget)this.objectQuery(sql.toString(), args.toArray(),FeeBudget.class);
	}
	
	/**
	 * 根据loanReportId获取关联报销详情
	 * @param loanReportId
	 * @param comId
	 * @return
	 */
	public FeeLoanOff queryLoanOffByRepId(Integer loanReportId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*");
		sql.append("\n from feeLoanOff a where a.loanReportId=? and a.comId=? and a.status!=-1");
		args.add(loanReportId);
		args.add(comId);
		return (FeeLoanOff)this.objectQuery(sql.toString(), args.toArray(),FeeLoanOff.class);
	}
	
	/**
	 * 根据feeBudgetId获取关联报销详情
	 * @param feeBudgetId
	 * @param comId
	 * @return
	 */
	public FeeLoanOff queryLoanOffByFeebId(Integer feeBudgetId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,2 flowState");
		sql.append("\n from feeLoanOff a inner join spflowinstance spFlow on a.instanceId = spFlow.id and a.comId=spFlow.comId");
		sql.append("\n where 1=1  and a.feeBudgetId=? and a.comid=? and spFlow.flowState=2");
		args.add(feeBudgetId);
		args.add(comId);
		return (FeeLoanOff)this.objectQuery(sql.toString(), args.toArray(),FeeLoanOff.class);
	}
}
