package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.enums.LoanWayEnum;
import com.westar.base.model.FeeLoan;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class BusRelateSpFlowDao extends BaseDao {

	
	/**
	 * 查询借款审批信息
	 * @param applyId
	 * @return
	 */
	public FeeLoan queryLoanDraftFlowByApplyId(Integer applyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,2 flowState");
		sql.append("\n from FeeLoan a inner join spflowinstance spFlow on a.instanceId = spFlow.id and a.comId=spFlow.comId");
		sql.append("\n where 1=1 and a.feeBudgetId=? and spFlow.flowState=2");
		args.add(applyId);
		return (FeeLoan) this.objectQuery(sql.toString(), args.toArray(), FeeLoan.class);
	}
	/**
	 * 删除直接借款依据
	 * @param userInfo
	 */
	public void delUnStartFeeBudgetForLoan(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete  from feeBudget where id in(" );
		sql.append("\n 	select c.id from feeLoan a" );
		sql.append("\n 	inner join spflowinstance b on a.instanceid=b.id and a.comid=? and b.flowstate=0 and b.creator=?");
		sql.append("\n 	inner join feeBudget c on a.feeBudgetId=c.id and c.status=4 and nvl(c.instanceid,0)=0");
		sql.append("\n )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());// 流程当前审批人
	}
	/**
	 * 删除直接借款依据
	 * @param userInfo
	 */
	public void delUnStartFeeBudgetForLoanOff(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete  from feeBudget where id in(" );
		sql.append("\n 	select c.id from feeLoanOff a" );
		sql.append("\n 	inner join spflowinstance b on a.instanceid=b.id and a.comid=? and b.flowstate=0 and b.creator=?");
		sql.append("\n 	inner join feeBudget c on a.feeBudgetId=c.id and c.status=4 and nvl(c.instanceid,0)=0");
		sql.append("\n )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());// 流程当前审批人
	}
	/**
	 * 删除关联了流程审批的指定表的无效数据
	 * @param clz
	 * @param userInfo
	 */
	public void delRelateTableWithInsId(Class<?> clz, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from "+clz.getSimpleName()+" a");
		sql.append("\n where exists (");
		sql.append("\n select b.id from spflowinstance b where a.instanceid=b.id and a.comid=? and b.flowstate=0 and b.creator=?");
		sql.append("\n )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());// 流程当前审批人
	}

	/**
	 * 删除当前操作人的无效业务申请
	 * 
	 * @param userInfo
	 */
	public void delUnuseEventPm(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from eventPm eventPm ");
		sql.append("\n where eventPm.comId=? and eventPm.status=0 and exists (");
		args.add(userInfo.getComId());
		sql.append("\n 	select b.id from spflowinstance b ");
		sql.append("\n 	inner join eventPm a on a.instanceid=b.id and a.comId=b.comId and b.busType=? ");
		args.add(ConstantInterface.TYPE_ITOM_EVENTPM);
		sql.append("\n 	where a.comid=? and b.flowstate=0 and b.creator=? and eventPm.id=a.id ");
		sql.append("\n )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());// 流程当前审批人

	}
	/**
	 * 删除问题过程管理信息
	 * 
	 * @param userInfo
	 */
	public void delUnuseIssuePm(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from issuePm issuePm ");
		sql.append("\n where issuePm.comId=? and issuePm.status=0 and exists (");
		args.add(userInfo.getComId());
		sql.append("\n 	select b.id from spflowinstance b ");
		sql.append("\n 	inner join issuePm a on a.instanceid=b.id and a.comId=b.comId and b.busType=? ");
		args.add(ConstantInterface.TYPE_ITOM_ISSUEPM);
		sql.append("\n 	where a.comid=? and b.flowstate=0 and b.creator=?");
		sql.append("\n )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());// 流程当前审批人
	}
	/**
	 * 删除变更过程管理信息
	 * 
	 * @param userInfo
	 */
	public void delUnuseModifyPm(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from modifyPm modifyPm ");
		sql.append("\n where modifyPm.comId=? and modifyPm.status=0 and exists (");
		args.add(userInfo.getComId());
		sql.append("\n 	select b.id from spflowinstance b ");
		sql.append("\n 	inner join modifyPm a on a.instanceid=b.id and a.comId=b.comId and b.busType=? ");
		args.add(ConstantInterface.TYPE_ITOM_MODIFYPM);
		sql.append("\n 	where a.comid=? and b.flowstate=0 and b.creator=? and modifyPm.id=a.id");
		sql.append("\n )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());// 流程当前审批人
	}

	/**
	 * 删除问发布过程管理信息
	 * 
	 * @param userInfo
	 */
	public void delUnuseReleasePm(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from releasePm ");
		sql.append("\n where releasePm.comId=? and releasePm.status=0 and exists (");
		args.add(userInfo.getComId());
		sql.append("\n 	select b.id from spflowinstance b ");
		sql.append("\n 	inner join releasePm a on a.instanceid=b.id and a.comId=b.comId and b.busType=? ");
		args.add(ConstantInterface.TYPE_ITOM_RELEASEPM);
		sql.append("\n 	where a.comid=? and b.flowstate=0 and b.creator=? and releasePm.id=a.id");
		sql.append("\n )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());// 流程当前审批人
	}
	
	
	/**
	 * 删除无效请假申请
	 * 
	 * @param userInfo
	 */
	public void delLeaveApply(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from leave a");
		sql.append("\n where exists (");
		sql.append("\n select b.id from spflowinstance b where a.instanceid=b.id and a.comid=? and b.flowstate=0 and b.creator=?");
		sql.append("\n )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());// 流程当前审批人
	}

	/**
	 * 删除无效加班申请
	 * 
	 * @param userInfo
	 */
	public void delOverTimeApply(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from overTime a");
		sql.append("\n where exists (");
		sql.append("\n select b.id from spflowinstance b where a.instanceid=b.id and a.comid=? and b.flowstate=0 and b.creator=?");
		sql.append("\n )");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());// 流程当前审批人
	}
}
