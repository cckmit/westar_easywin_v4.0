package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.SpFlowCurExecutor;
import com.westar.base.model.SpFlowHiExecutor;
import com.westar.base.model.SpFlowHiStep;
import com.westar.base.model.SpFlowHiStepRelation;
import com.westar.base.model.SpFlowHuiQianInfo;
import com.westar.base.model.SpFlowModel;
import com.westar.base.model.SpFlowStepConditions;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class ModFlowDao extends BaseDao {

	/**
	 * 列表选择模块使用的固定流程
	 * @param sessionUser 当前操作人员
	 * @param busType 模块标识
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowModel> listModSpFlow(UserInfo sessionUser, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from SpFlowModel a ");
		sql.append("\n where a.STATUS=1 and a.comid=? and a.flowModBusType=?");
		args.add(sessionUser.getComId());
		args.add(busType);
		sql.append("\n order by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), SpFlowModel.class);
	}
	
	/**
	 * 获取流程实例步骤第一步
	 * @param comId 单位主键
	 * @param instanceId 流程实例主键
	 * @return
	 */
	public SpFlowHiStep querySpFlowHiStepOfStart(Integer comId,
			Integer busId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.stepname,a.stepid from spFlowHiStep a");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.steptype='start'");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		return (SpFlowHiStep)this.objectQuery(sql.toString(), args.toArray(),SpFlowHiStep.class);
	}
	
	
	/**
	 * 初始化流程是咧相关的变量主键
	 * @param comId 单位主键
	 * @param busId 流程实例化主键
	 * @param flowId 流程模型主键
	 */
	public void initSpFlowRunVariableKey(Integer comId, Integer busId,Integer flowId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into spFlowRunVariableKey(comId,busId,busType,variableKey) \n");
		sql.append("select comId,?,?,conditionVar from spstepconditions where comId=? and flowId=? \n");
		args.add(busId);
		args.add(busType);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 拷贝流程步骤表单授权
	 * @param comId 单位主键
	 * @param busId 流程实例主键
	 * @param flowId 流程模型主键
	 */
	public void initSpFlowRunStepFormControl(Integer comId, Integer busId,
			Integer flowId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowRunStepFormControl(comId,busId,busType,stepId,stepType,formControlKey,isFill)");
		sql.append("\n select a.comid,?,?,a.id,a.steptype,b.formcontrolkey,b.isFill from spFlowStep a  \n");
		sql.append("\n inner join spStepFormControl b on a.comId=b.comid and a.id=b.stepid where a.comId=? and a.flowId=?");
		args.add(busId);
		args.add(busType);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 初始化流程实例步骤
	 * @param comId 单位主键
	 * @param busId 流程实例主键
	 * @param flowId 拷贝流程模型主键
	 */
	public void initSpFlowHiStep(Integer comId, Integer busId,
			Integer flowId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowHiStep(comId,busId,busType,stepId,stepName,stepType,executorWay,conditionExp,spCheckCfg)");
		sql.append("\n select comId,?,?,id,stepName,stepType,executorWay,conditionExp,spCheckCfg from spFlowStep where comId=? and flowId=?");
		args.add(busId);
		args.add(busType);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	

	/**
	 * 初始化流程实例步骤审批人
	 * @param comId 单位主键
	 * @param busId 流程实例主键
	 * @param flowId 拷贝流程模型主键
	 */
	public void initSpFlowHiStepExecutor(Integer comId, Integer busId,
			Integer flowId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowHiStepExecutor(comId,busId,busType,stepId,executor)");
		sql.append("\n select comId,?,?,stepId,executor from spFlowStepExecutor where comId=? and flowId=?");
		args.add(busId);
		args.add(busType);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 初始化流程实例步骤间关系表
	 * @param comId 单位主键
	 * @param busId 流程实例主键
	 * @param flowId 拷贝流程模型主键
	 */
	public void initSpFlowHiStepRelation(Integer comId, Integer busId,
			Integer flowId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowHiStepRelation(comId,busId,busType,curStepId,nextStepId,stepWay,defaultStep) ");
		sql.append("\n select comId,?,?,curStepId,nextStepId,stepWay,defaultStep from spFlowStepRelation where comId=? and flowId=? ");
		args.add(busId);
		args.add(busType);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 初始化子流程实例化后配置表
	 * @param comId 单位主键
	 * @param busId 流程实例主键
	 * @param pflowId 拷贝主流程模型主键
	 * @param sonFlowId 关联子流程模型主键
	 */
	public void initSpFlowRunRelevanceCfg(Integer comId, Integer busId,
			Integer pflowId,Integer sonFlowId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowRunRelevanceCfg(comId,busId,busType,fromFormControlKey,toFormControlKey) ");
		sql.append("\n select comId,?,?,fromFormControlKey,toFormControlKey from spFlowRelevanceCfg where comId=? and pflowId=? and sonFlowId=? ");
		args.add(busId);
		args.add(busType);
		args.add(comId);
		args.add(pflowId);
		args.add(sonFlowId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 查询流程审批人员
	 * @param comId 团队号
	 * @param instanceId 流程实例主键
	 * @param executor 当前执行人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SpFlowCurExecutor querySpFlowCurExecutorV2(UserInfo sessionUser,
			Integer instanceId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from spFlowCurExecutor where comId=? and busId=? and busType=?");
		args.add(sessionUser.getComId());
		args.add(instanceId);
		args.add(busType);
		this.addSqlWhere(sessionUser.getId(), sql, args, "\n and executor=?");
		return (SpFlowCurExecutor) this.objectQuery(sql.toString(), args.toArray(), SpFlowCurExecutor.class);
	}

	/**
	 * 查询流程开始步骤
	 * @param comId 团队号
	 * @param flowId 流程主键
	 * @return
	 */
	public SpFlowHiStep queryFlowStartStep(Integer comId, Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n SELECT COMID,ID AS STEPID,STEPNAME,STEPTYPE,EXECUTORWAY,SPCHECKCFG");
		sql.append("\n FROM SPFLOWSTEP");
		sql.append("\n WHERE 1=1 AND COMID=? AND FLOWID=? and STEPTYPE=?");
		args.add(comId);
		args.add(flowId);
		args.add("start");
		return (SpFlowHiStep) this.objectQuery(sql.toString(), args.toArray(), SpFlowHiStep.class);
	}
	
	/**
	 * 根据当前步骤主键以及步骤间关系获取下步步骤主键
	 * @param comId 团队主键
	 * @param instanceId 流程实例主键
	 * @param curStepId 当前步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStepRelation> querySpFlowStepRelation(Integer comId,
			Integer curStepId, Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.nextstepid,a.curstepid,a.stepWay,a.defaultStep ");
		sql.append("\n from spFlowStepRelation a ");
		sql.append("\n where a.comid=? and a.curstepid=? and a.flowId=?");
		args.add(comId);
		args.add(curStepId);
		args.add(flowId);
		return this.listQuery(sql.toString(), args.toArray(), SpFlowHiStepRelation.class);
	}
	
	/**
	 * 获取流程实例步骤基本配置信息
	 * @param comId 团队好主键
	 * @param instanceId 流程实例主键
	 * @param stepId 步骤主键
	 * @return
	 */
	public SpFlowHiStep queryModFlowNextStepInfo(Integer comId,
			Integer flowId, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id as stepid,a.stepname,a.steptype,a.executorway,a.spcheckcfg ");
		sql.append("\n from spFlowStep a ");
		sql.append("\n where a.comid=? and a.flowId=? and a.id=? ");
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		return (SpFlowHiStep)this.objectQuery(sql.toString(), args.toArray(),SpFlowHiStep.class);
	}
	
	/**
	 * 获取流程实例步骤基本配置信息
	 * @param comId 团队好主键
	 * @param instanceId 流程实例主键
	 * @param stepId 步骤主键
	 * @return
	 */
	public SpFlowHiStep querySpFlowNextStepInfo(Integer comId,
			Integer busId, Integer stepId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select DISTINCT a.busId,a.busType,a.stepid,a.stepname,a.steptype,a.executorway,a.spcheckcfg ");
		sql.append("\n from spFlowHiStep a ");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.stepid=? ");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		args.add(stepId);
		return (SpFlowHiStep)this.objectQuery(sql.toString(), args.toArray(),SpFlowHiStep.class);
	}

	/**
	 * 查询流程模板步骤候选人
	 * @param comId 团队号
	 * @param flowId 流程主键
	 * @param stepId 步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listModFlowStepExecutor(Integer comId, Integer flowId,
			Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comid,u.id,u.gender,f.uuid,f.filename,");
		sql.append("\n  case when insuser.repuserid is null then u.username else u.username||'('||repuser.userName||')'  end username,");
		sql.append("\n CASE WHEN temp.dutyOrgOrder is null THEN '未排序' ELSE temp.dutyOrgOrder END as dutyOrgOrder ");
		sql.append("\n from spFlowStepExecutor a ");
		sql.append("\n left join userorganic org on a.comid = org.comid and a.executor=org.userid");
		sql.append("\n left join Userinfo u on org.userid=u.id");
		sql.append("\n left join upfiles f on org.smallHeadPortrait=f.id and f.comid=org.comId");
		//代理人员
		sql.append("\n left join insteaduser insUser on a.comid=insuser.comid and a.executor=insuser.userid");
		sql.append("\n left join userinfo repuser on insuser.repuserid=repuser.id");
		
		sql.append("\n left join (select CASE MIN(a.typeOrder) WHEN 0 THEN '未排序' ELSE MIN(a.typeOrder)||'' END as dutyOrgOrder,");
		sql.append("\n b.userId as userId,a.comId as comId from dutyOrg a");
		sql.append("\n right join dutyUser b on a.id = b.dutyId");
		sql.append("\n where a.typeOrder <> 0");
		sql.append("\n group by a.comId,b.userId,a.comId)temp on org.comId = temp.comId and a.id = temp.userId");
		sql.append("\n where a.comid=? and a.flowId=? and a.stepid=? and org.enabled=1 and org.inservice=1");
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		sql.append("\n order by dutyOrgOrder");
		return this.listQuery(sql.toString(), args.toArray(),UserInfo.class);
	}
	
	/**
	 * 根据配置获取候选人信息
	 * @param comId 团队主键
	 * @param instanceId 流程实例主键
	 * @param stepId 步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listSpFlowHiExecutor(Integer comId,
			Integer busId, Integer stepId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comid,u.id,u.gender,f.uuid,f.filename,u.userName");
		sql.append("\n from spFlowHiStepExecutor a ");
		sql.append("\n left join userorganic org on a.comid = org.comid and a.executor=org.userid");
		sql.append("\n left join Userinfo u on org.userid=u.id");
		sql.append("\n left join upfiles f on org.smallHeadPortrait=f.id and f.comid=org.comId");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.stepid=?");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	
	/**
	 * 查询当前流程执行人
	 * @param curUser 当前操作员
	 * @param busId 模块主键
	 * @param busType 模块类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listSpFlowCurExecutor(UserInfo curUser,
			Integer busId,String busType) {
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.id,c.username from spFlowCurExecutor a ");
		sql.append("\n inner join userorganic b on a.comid=b.comid and a.executor=b.userid");
		sql.append("\n inner join Userinfo c on b.userid=c.id");
		sql.append("\n where a.comid=? and a.busid=? and a.busType=?");
		args.add(curUser.getComId());
		args.add(busId);
		args.add(busType);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 根据当前步骤主键以及步骤间关系获取下步步骤主键
	 * @param comId 团队主键
	 * @param busId 流程实例主键
	 * @param curStepId 当前步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStepRelation> querySpFlowHiStepRelation(Integer comId,
			Integer busId, Integer curStepId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.nextstepid,a.busId,a.busType,a.curstepid ");
		sql.append("\n from spFlowHiStepRelation a ");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.curstepid=?");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		args.add(curStepId);
		sql.append("\n order by a.DEFAULTSTEP desc nulls last ");
		return this.listQuery(sql.toString(), args.toArray(), SpFlowHiStepRelation.class);
	}
	/**
	 * 获取流程实例当前的审批人集合
	 * @param comId
	 * @param busId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowCurExecutor> listSpFlowCurExecutor(Integer comId,
			Integer busId,String busType,String executeType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from spFlowCurExecutor where comId=? and busId=? and busType=?");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		this.addSqlWhere(executeType, sql, args, "\n and executeType=?");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowCurExecutor.class);
	}
	/**
	 * 查询历史步骤信息
	 * @param comId
	 * @param busId
	 * @param busType
	 * @param actInstaceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStep> listHistorySpStep(Integer comId, Integer busId,
			String busType, String actInstaceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select distinct * from (");
		sql.append("\n select c.task_def_key_ as activitiTaskId,c.name_ as stepName ");
		sql.append("\n from act_hi_procinst b ");
		sql.append("\n inner join act_hi_taskinst c on b.id_=c.proc_inst_id_");
		sql.append("\n where b.business_key_=? and b.id_=? and c.end_time_ is not null");
		args.add(busId);
		args.add(actInstaceId);
		sql.append("\n group by c.task_def_key_,c.name_,c.start_time_");
		sql.append("\n  )a order by a.activitiTaskId");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowHiStep.class);
	}
	
	/**
	 * 获取已执行步骤配置信息
	 * @param comId 单位主键
	 * @param instanceId 流程实例主键
	 * @param activitiTaskId 流程事项部署主键
	 * @param actInstanceId 
	 * @return
	 */
	public SpFlowHiStep querySpFlowHisStep(Integer comId, Integer busId,String busType, 
			String actInstanceId, String activitiTaskId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.name_ as stepName,c.assignee_ as assignee, d.steptype ");
		sql.append("\n from act_hi_procinst b ");
		sql.append("\n inner join act_hi_taskinst c on b.id_ = c.proc_inst_id_ and c.end_time_ is not null");
		sql.append("\n inner join spFlowHiStep d on b.business_key_=d.busId and d.busType=? and d.stepid="+activitiTaskId.split("_")[1]);
		sql.append("\n where d.comid=? and b.business_key_=? and b.id_=? and c.task_def_key_=? and rownum=1");
		args.add(busType);
		args.add(comId);
		args.add(busId);
		args.add(actInstanceId);
		args.add(activitiTaskId);
		sql.append("\n order by c.start_time_ desc");
		return (SpFlowHiStep)this.objectQuery(sql.toString(), args.toArray(),SpFlowHiStep.class);
	}
	
	/**
	 * 获取流程实例步骤信息（展示）
	 * @param busId 流程实例主键
	 * @param userInfo 当前操作人信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStep> listSpFlowModStepForShow(
			UserInfo userInfo,Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comid,a.id stepid,a.stepname,a.steptype,a.xint,a.yint,0 ifDo");
		sql.append("\n from spFlowStep a");
		sql.append("\n where a.comid=? and a.flowId=?  ");
		args.add(userInfo.getComId());
		args.add(flowId);
		return this.listQuery(sql.toString(), args.toArray(), SpFlowHiStep.class);
	}
	
	/**
	 * 获取流程实例步骤信息（展示）
	 * @param busId 流程实例主键
	 * @param userInfo 当前操作人信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStep> listSpFlowHiStepForShow(
			UserInfo userInfo,Integer busId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comid,a.busId,a.busType,a.stepid,a.stepname,a.steptype,a.xint,a.yint,");
		sql.append("\n case when b.endtime is not null then 2");
		sql.append("\n when b.starttime is not null and b.endtime is null then 1");
		sql.append("\n else 0 end ifDo");
		sql.append("\n from spFlowHiStep a");
		sql.append("\n left join spFlowRunStepInfo b on a.comid=b.comid and a.busId=b.busId and a.stepid=b.stepid and a.busType=b.busType ");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.isHq is null");
		args.add(userInfo.getComId());
		args.add(busId);
		args.add(busType);
		return this.listQuery(sql.toString(), args.toArray(), SpFlowHiStep.class);
	}
	
	/**
	 * 获取实例下步步骤信息
	 * @param comId 团队主键
	 * @param instanceId 流程实例主键
	 * @param stepId 步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStep> listModFlowNextStep(Integer comId,
			Integer flowId, Integer curstepid) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comid,b.id stepid,b.stepname,b.steptype");
		sql.append("\n from spFlowStepRelation a");
		sql.append("\n inner join spFlowStep b on a.comid=b.comid and a.flowId=b.flowId and a.nextstepid=b.id");
		sql.append("\n where a.comid=? and a.flowId=? and a.curstepid=?");
		args.add(comId);
		args.add(flowId);
		args.add(curstepid);
		return this.listQuery(sql.toString(), args.toArray(), SpFlowHiStep.class);
	}
	
	
	/**
	 * 获取实例下步步骤信息
	 * @param comId 团队主键
	 * @param busId 流程实例主键
	 * @param stepId 步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStep> listSpFlowHiNextStep(Integer comId,
			Integer busId, Integer stepId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comid,a.busId,a.busType,b.stepid,b.stepname,b.steptype");
		sql.append("\n from spFlowHiStepRelation a");
		sql.append("\n inner join spFlowHiStep b on a.comid=b.comid and a.busId=b.busId and a.busType=b.busType and a.nextstepid=b.stepid");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.curstepid=?");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(), SpFlowHiStep.class);
	}
	
	/**
	 * 获取流程实例扭转历史步骤信息
	 * @param instanceId 流程实例化主键
	 * @param userInfo 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStep> listSpFlowHiStep( UserInfo userInfo,Integer busId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select d.stepid,c.name_ as stepname,d.steptype,userInfo.id as executor,userInfo.userName as executorName,c.name_,c.assignee_,");
		sql.append("\n TO_CHAR(c.start_time_, 'YYYY-MM-DD HH24:MI:SS AM DY') as startTime,");
		sql.append("\n TO_CHAR(c.end_time_, 'YYYY-MM-DD HH24:MI:SS AM DY') as endTime,c.duration_ as usedTime,");
		// sql.append("\n userInfo.gender,imgFile.uuid,imgFile.filename,spMsg.message_ as spMsg");
		sql.append("\n userInfo.gender,imgFile.uuid,imgFile.filename,UTL_RAW.CAST_TO_VARCHAR2(spMsg.full_msg_) as spMsg");
		sql.append("\n from act_hi_procinst a");
		if(ConstantInterface.TYPE_MEETING_SP.equals(busType)){
			sql.append("\n inner join meeting b on a.id_=b.actinstaceid and a.business_key_=b.id");
		}else if(ConstantInterface.TYPE_MEET_SUMMARY.equals(busType)){
			sql.append("\n inner join meetSummary b on a.id_=b.actinstaceid and a.business_key_=b.id");
		}
		
		sql.append("\n inner join act_hi_taskinst c on b.actinstaceid=c.proc_inst_id_");
		sql.append("\n inner join spflowhistep d on d.comid=b.comid and d.busId=b.id and d.busType=? and c.task_def_key_='step_'||d.stepid");
		args.add(busType);
		sql.append("\n inner join organic org on b.comid = org.orgnum");
		sql.append("\n left join userinfo userInfo on org.orgnum ||':'||userInfo.id=c.assignee_");
		sql.append("\n left join userOrganic userOrg on b.comid = userOrg.comid and userInfo.id = userOrg.userid");
		sql.append("\n left join upfiles imgFile on userOrg.mediumheadportrait = imgFile.id");
		// sql.append("\n left join act_hi_taskinst hiTask on a.id_=hiTask.Proc_Inst_Id_ and c.task_def_key_=hiTask.Task_Def_Key_");
		sql.append("\n left join act_hi_comment spMsg on a.id_=spMsg.proc_inst_id_ and c.Id_=spMsg.task_id_");
		sql.append("\n where b.comid=? and b.id=?");
		sql.append("\n order by c.start_time_");
		args.add(userInfo.getComId());
		args.add(busId);
		return this.listQuery(sql.toString(), args.toArray(),SpFlowHiStep.class);
	}
	/**
	 * 分页查询流程关联附件
	 * @param instanceId 流程实例化主键
	 * @param userInfo 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowUpfile> listPagedSpFiles(UserInfo userInfo,Integer busId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.busId,a.busType,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, \n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from spFlowUpfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comid =? and a.busId = ? and a.busType=?\n");
		args.add(userInfo.getComId());
		args.add(busId);
		args.add(busType);
		return this.pagedQuery(sql.toString(), " isPic asc, a.id desc", args.toArray(), SpFlowUpfile.class);
	}
	
	
	
	/**
	 * 获取流程实例步骤中stepid最大的步骤信息
	 * 用于配置会签步骤步骤StepId
	 * @param comId 单位主键
	 * @param busId 流程主键
	 * @return
	 */
	public SpFlowHiStep querySpFlowHiStepByMaxStepId(Integer comId,
			Integer busId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select maxRow.stepid,maxRow.comid,maxRow.busId,maxRow.busType from (");
		sql.append("\n select a.stepid,a.comid,a.busId,a.busType from spFlowHiStep a ");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? order by id desc");
		sql.append("\n ) maxRow where rownum=1");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		return (SpFlowHiStep)this.objectQuery(sql.toString(), args.toArray(),SpFlowHiStep.class);
	}
	/**
	 * 查询指定步骤的步骤类型
	 * @param comId 团队号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param stepId 步骤主键
	 * @return
	 */
	public SpFlowHiStep querySpFlowForStepType(Integer comId,
			Integer busId,String busType,Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select maxRow.stepid,maxRow.comid,maxRow.busId,maxRow.busType,maxRow.stepType from (");
		sql.append("\n select a.stepid,a.comid,a.busId,a.busType,a.stepType from spFlowHiStep a ");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.stepId=? order by id desc");
		sql.append("\n ) maxRow where rownum=1");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		args.add(stepId);
		return (SpFlowHiStep)this.objectQuery(sql.toString(), args.toArray(),SpFlowHiStep.class);
	}
	
	
	/**
	 * 查询步骤的条件信息
	 * @param comId 团队号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param stepId 步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStepConditions> listSpFlowStepByFlowConditions(Integer comId,
			Integer flowId, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* ");
		sql.append("\n from spStepConditions a ");
		sql.append("\n where a.comid=? and a.flowId=? and a.stepid=? ");
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(),SpFlowStepConditions.class);
	}
	/**
	 * 查询步骤的条件信息
	 * @param comId 团队号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param stepId 步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStepConditions> listSpFlowStepConditions(Integer comId,
			Integer busId,String busType, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* ");
		sql.append("\n from spFlowStepConditions a ");
		sql.append("\n where a.comid=? and a.busId=? and a.bustype=? and a.stepid=? ");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(),SpFlowStepConditions.class);
	}
	
	/**
	 * 取得最近的审批执行人信息
	 * 
	 * @param comId
	 *            团队号
	 * @param busId
	 *            流程主键
	 * @param stepId
	 *            步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiExecutor> listSpFlowHiExecutorForBack(Integer comId,
			Integer busId, String busType, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*");
		sql.append("\n from spflowhiexecutor a");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.stepid=?");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		args.add(stepId);
		sql.append("\n order by id desc");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowHiExecutor.class);
	}

	public SpFlowHuiQianInfo queryUserHuiQianInfo(UserInfo userInfo,
			Integer busId, String busType,String actinstaceid) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.recordcreatetime,a.comid,a.busid,a.bustype,a.assignee,a.content,a.stepid,a.huiqiancontent,a.endtime,");
		sql.append("\n c.username,e.proc_inst_id_,f.id_ as actTaskId,f.name_ as stepName");
		sql.append("\n from spFlowHuiQianInfo a");
		sql.append("\n inner join userorganic b on a.comid=b.comid and a.assignee=b.userid");
		sql.append("\n inner join userInfo c on b.userid=c.id");
		sql.append("\n inner join act_hi_procinst e on e.id_='"+actinstaceid+"' and e.business_key_=?");
		args.add(busId);
		sql.append("\n inner join act_ru_task f on e.proc_inst_id_=f.proc_inst_id_ and ? ||':'||a.assignee=f.assignee_");
		args.add(userInfo.getComId());
		sql.append("\n where a.assignee=? and a.endtime is null and a.comid=? and a.busid=? and a.bustype=?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		args.add(busId);
		args.add(busType);
		return (SpFlowHuiQianInfo) this.objectQuery(sql.toString(), args.toArray(), SpFlowHuiQianInfo.class);
	
	}

}
