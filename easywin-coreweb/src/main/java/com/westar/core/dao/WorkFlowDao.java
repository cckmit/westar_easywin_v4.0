package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Repository;

import com.westar.base.enums.ActLinkTypeEnum;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.SpFlowCurExecutor;
import com.westar.base.model.SpFlowHiExecutor;
import com.westar.base.model.SpFlowHiStep;
import com.westar.base.model.SpFlowHiStepRelation;
import com.westar.base.model.SpFlowHuiQianInfo;
import com.westar.base.model.SpFlowInputData;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.SpFlowOptData;
import com.westar.base.model.SpFlowRelateData;
import com.westar.base.model.SpFlowRunRelevanceCfg;
import com.westar.base.model.SpFlowRunStepFormControl;
import com.westar.base.model.SpFlowRunVariableKey;
import com.westar.base.model.SpFlowStep;
import com.westar.base.model.SpFlowStepConditions;
import com.westar.base.model.SpFlowTalk;
import com.westar.base.model.SpFlowTalkUpfile;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.SpFlowUsedTimes;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.ConstantInterface;

@Repository
public class WorkFlowDao extends BaseDao {

	/**
	 * 更新关系流程的主键processInstanceKey
	 * 
	 * @param userInfo
	 * @param processInstanceKey
	 *            activiti实例化主键
	 * @param initId
	 *            关系表主键
	 */
	public void initProcessInstanceKey(UserInfo userInfo,
			String processInstanceKey, Integer initId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update sp_model_init a set a.processInstanceKey=? where a.comid=? and a.id=?");
		args.add(processInstanceKey);
		args.add(userInfo.getComId());
		args.add(initId);
		this.excuteSql(sql.toString(), args.toArray());

	}

	/**
	 * 取得用户的上次初始化的信息
	 * 
	 * @param formKey
	 * @param userInfo
	 * @return
	 */
	public SpFlowInstance getUserTempWorkFlow(Integer formKey, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,layout.id layoutId,formMod.modName formModName from spFlowInstance a ");
		sql.append("\n inner join formlayout layout on a.formversion=layout.version and a.formkey=layout.formmodid");
		sql.append("\n inner join formMod on a.comid=formMod.comid and a.formKey=formMod.id");

		sql.append("\n where a.flowState=0");
		this.addSqlWhere(formKey, sql, args, " and a.formKey=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.creator=?");
		return (SpFlowInstance) this.objectQuery(sql.toString(),
				args.toArray(), SpFlowInstance.class);
	}

	/**
	 * 取得 流程表单数据
	 * 
	 * @param comId
	 *            团队号
	 * @param instanceId
	 *            流程表单主键
	 * @param fieldId
	 *            组件字段标识
	 * @param dataIndex
	 * @return
	 */
	public SpFlowInputData getSpFlowInputFieldData(Integer comId,
			Integer instanceId, Integer fieldId, Integer dataIndex) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from spFlowInputData a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(instanceId, sql, args, " and a.instanceId=?");
		this.addSqlWhere(fieldId, sql, args, " and a.fieldId=?");
		this.addSqlWhere(dataIndex, sql, args, " and a.dataIndex=?");
		return (SpFlowInputData) this.objectQuery(sql.toString(),
				args.toArray(), SpFlowInputData.class);
	}

	/**
	 * 分页查询流程表单
	 * 
	 * @param userInfo
	 * @param spFlowInstance
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowInstance> listPagedWorkFlow(UserInfo userInfo,
			SpFlowInstance spFlowInstance) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		// TODO 需要重新完善
		sql.append("\n select a.* from spFlowInstance a where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.creator=?");
		this.addSqlWhere(spFlowInstance.getFlowState(), sql, args,
				" and a.flowState=?");
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(),
				SpFlowInstance.class);
	}

	/**
	 * 取得流程实例化
	 * 
	 * @param instanceId
	 *            //实例化对象主键
	 * @param userInfo
	 * @return
	 */
	public SpFlowInstance getSpFlowInstanceById(Integer instanceId,
			UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.flowSerialNumber,a.id,a.recordCreateTime,a.comId,a.flowName,a.creator,a.formKey,a.formVersion,");
		sql.append("\n a.flowState,a.actInstaceId,a.busType,a.busId,a.flowId,formMod.modName formmodName, ");
		sql.append("\n a.spState,a.sonFlowId,a.sonInstanceId,c.username as creatorName,dep.depname,layout.id layoutId,layout.formState formLayoutState, ");
		sql.append("\n case when atten.id is null then 0 else 1 end as attentionState, ");
		sql.append("\n case when a.bustype='" + ConstantInterface.TYPE_ITEM	+ "' then e.itemname");
		sql.append("\n 	    when  a.bustype='" + ConstantInterface.TYPE_TASK + "'then task.taskname ");
		sql.append("\n 	    when  a.bustype='" + ConstantInterface.TYPE_CRM + "'then crm.customername end busName,");
		sql.append("\n case when stagedInfo.id is null then 0 else stagedInfo.id end stagedItemId,stagedItem.Stagedname stagedItemName,");

		sql.append("\n a.sonInstanceId,sonIns.flowSerialNumber sonFlowSerialNum,sonIns.flowName sonFlowname");

		sql.append("\n from spFlowInstance a left join userinfo c on a.creator=c.id");
		sql.append("\n left join userorganic d on a.comid=d.comid and d.userid=a.creator");
		sql.append("\n left join department dep on a.comid=dep.comid and d.depid=dep.id");
		sql.append("\n left join formlayout layout on a.formversion=layout.version and a.formkey=layout.formmodid");
		sql.append("\n left join formMod on a.comid=formMod.comid and a.formKey=formMod.id and layout.formModId=formMod.id");
		// 项目正常才取得项目信息
		sql.append("\n left join item e on a.comid = e.comid and a.busid=e.id and a.bustype='"+ ConstantInterface.TYPE_ITEM + "' and e.delstate=0");
		sql.append("\n left join stagedInfo on a.comid=stagedInfo.Comid and stagedInfo.Moduletype='" + ConstantInterface.STAGED_FLOW_SP + "' ");
		sql.append("\n 	and stagedInfo.moduleId=a.id and e.id=stagedInfo.itemid");
		sql.append("\n left join stagedItem on a.comid=stagedItem.Comid and e.id=stagedItem.itemid and stagedItem.id=stagedInfo.stagedItemId");
		// 获取客户信息
		sql.append("\n left join customer crm on a.comid = crm.comid and a.busid=crm.id and a.bustype='" + ConstantInterface.TYPE_CRM + "' and crm.delstate=0");
		// 获取审批信息
		sql.append("\n left join task on a.comid = task.comid and a.busid=task.id and a.bustype='" + ConstantInterface.TYPE_TASK + "' and task.delstate=0");
		// 关注状态
		sql.append("\n left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ ConstantInterface.TYPE_FLOW_SP + "' and atten.userId=? \n");
		sql.append("\n left join spFlowInstance sonIns on a.comid = sonIns.comid and a.sonInstanceId = sonIns.id \n");
		args.add(userInfo.getId());
		sql.append("\n where 1=1");
		this.addSqlWhere(instanceId, sql, args, " and a.id=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		return (SpFlowInstance) this.objectQuery(sql.toString(),
				args.toArray(), SpFlowInstance.class);
	}

	/**
	 * 流程表单选择数据查询
	 * 
	 * @param instanceId
	 *            实例化流程主键
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowInputData> listSpFormDatas(Integer instanceId,
			Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.busId busTableId,b.busType busTableType ");
		sql.append("\n from spFlowInputData a");
		sql.append("\n left join spFlowRelateData b on a.id=b.formFlowDataId and a.instanceId=b.instanceId");
		sql.append("\n where 1=1");
		this.addSqlWhere(instanceId, sql, args, " and a.instanceId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowInputData.class);
	}

	/**
	 * 取得动态表单数据信息（用于删除）
	 * 
	 * @param comId
	 * @param instanceId
	 * @param fieldId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowInputData> getSpFormDatas(Integer comId,
			Integer instanceId, Integer fieldId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from spFlowInputData a");
		sql.append("\n where 1=1");
		this.addSqlWhere(instanceId, sql, args, " and a.instanceId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(fieldId, sql, args, " and a.fieldId=?");
		sql.append("\n order by id asc");
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowInputData.class);
	}
	
	/**
	 * 查询所有的关联关系数据信息
	 * @param instanceId 审批主键
	 * @param formFlowDataId 审批数据主键
	 * @param comId 团队号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowRelateData> listSpFormRelateDatas(Integer instanceId,
			Integer formFlowDataId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from spFlowRelateData a");
		sql.append("\n where 1=1");
		this.addSqlWhere(instanceId, sql, args, " and a.instanceId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(formFlowDataId, sql, args, " and a.formFlowDataId=?");
		sql.append("\n order by id asc");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowRelateData.class);
	}
	/**
	 * 查询
	 * @param instanceId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowRelateData> listRelateDataForChangeState(Integer instanceId,Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.busType,a.busid from spFlowRelateData a");
		sql.append("\n where 1=1");
		this.addSqlWhere(instanceId, sql, args, " and a.instanceId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n group by a.busType,a.busid ");
		sql.append("\n order by  a.busType,a.busid");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowRelateData.class);
	}
	/**
	 * 查询记账data
	 * @param instanceId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowRelateData> listRelateDataForConsume(Integer instanceId,Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from spFlowRelateData a");
		sql.append("\n where 1=1");
		this.addSqlWhere(instanceId, sql, args, " and a.instanceId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by  a.busType,a.busid");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowRelateData.class);
	}

	/**
	 * 流程表单数据查询
	 * 
	 * @param instanceId
	 *            实例化流程主键
	 * @param formFlowDataId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowOptData> listSpFormOpts(Integer instanceId,
			Integer formFlowDataId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from spFlowOptData a");
		sql.append("\n where 1=1");
		this.addSqlWhere(instanceId, sql, args, " and a.instanceId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(formFlowDataId, sql, args, " and a.formFlowDataId=?");
		sql.append("\n order by id asc");
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowOptData.class);
	}

	/**
	 * 修改初始化时间
	 * 
	 * @param id
	 *            流程实例化 主键
	 * @param recordDateTime
	 *            发布时间
	 */
	public void updateSpFlowDateTime(Integer id, String recordDateTime) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update spFlowInstance set recordCreateTime=? where id=?");
		args.add(recordDateTime);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());

	}

	/**
	 * 根据人，获取待办审批
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowInstance> listSpTodDo(UserInfo userInfo,
			SpFlowInstance instance) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select distinct(a.id) as instanceKey,a.*,b.executeType,b.executor,c.username as executorName,c.gender,e.uuid,e.filename,");
		sql.append("\n cUser.username as creatorName,cUser.gender as creatorGender,cFile.uuid as creatorUuid,cFile.filename as creatorFileName,");
		sql.append("\n layout.formState formLayoutState");
		sql.append("\n from spFlowInstance a");
		sql.append("\n left join formlayout layout on a.formversion=layout.version and a.formkey=layout.formmodid");
		sql.append("\n left join spFlowCurExecutor b on a.comId = b.comid and a.id = b.busId and b.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n left join userinfo c on b.executor=c.id");
		sql.append("\n left join userOrganic d on a.comid = d.comid and c.id = d.userid");
		sql.append("\n left join upfiles e on d.mediumheadportrait = e.id");
		sql.append("\n inner join userinfo cUser on a.creator=cUser.id");
		sql.append("\n inner join userOrganic cOrg on a.comid = cOrg.comid and cUser.id = cOrg.userid");
		sql.append("\n left join upfiles cFile on cOrg.mediumheadportrait = cFile.id");
		sql.append("\n where a.comid=? and ((b.executor=? and a.flowstate=1) or (a.creator=? and a.flowstate=2))");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(userInfo.getId());
		if (null != instance.getCreator() && instance.getCreator() > 0) {
			this.addSqlWhere(instance.getCreator(), sql, args,
					" and  a.creator=?");// 流程发起人筛选
		}
		if (null != instance.getListCreator()
				&& !instance.getListCreator().isEmpty()) {
			sql.append("	 and  ( a.creator= 1 ");
			for (UserInfo owner : instance.getListCreator()) {
				sql.append("or a.creator=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		this.addSqlWhereLike(instance.getFlowName(), sql, args,
				" and a.flowName like ? \n");// 流程名称筛选
		// 查询创建时间段
		this.addSqlWhere(instance.getStartDate(), sql, args,
				" and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(instance.getEndDate(), sql, args,
				" and substr(a.recordcreatetime,0,10)<=?");

		if (null != instance.getFlowState() && instance.getFlowState() > -1) {
			this.addSqlWhere(instance.getFlowState(), sql, args,
					" and a.flowstate=?");// 流程状态筛选
		}

		String orderBy = "";// 列表排序
		if (null == instance.getOrderBy() || "".equals(instance.getOrderBy())) {
			orderBy = " a.flowState,a.id desc";
		} else if ("executor".equals(instance.getOrderBy())) {
			orderBy = " c.username";
		} else if ("crTimeNewest".equals(instance.getOrderBy())) {
			orderBy = " a.recordCreateTime desc";
		} else if ("crTimeOldest".equals(instance.getOrderBy())) {
			orderBy = " a.recordCreateTime asc";
		}
		return this.pagedQuery(sql.toString(), orderBy, args.toArray(),
				SpFlowInstance.class);
	}

	/**
	 * 获取流程实例当前的审批人集合
	 * 
	 * @param comId
	 * @param instanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowCurExecutor> listSpFlowCurExecutor(Integer comId,
			Integer instanceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from spFlowCurExecutor where comId=? and busId=? and busType=?");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowCurExecutor.class);
	}
	
	/**
	 * 查询、验证当前人是否是审批人信息
	 * @param userInfo
	 * @param instanceId
	 * @return
	 */
	public SpFlowCurExecutor getSpFlowCurExecutor(UserInfo userInfo,
			Integer instanceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* ");
		sql.append("\n from spFlowCurExecutor a ");
		sql.append("\n inner join spflowinstance b on a.busid=b.id and a.bustype=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n inner join act_hi_procinst c on c.id_=b.actinstaceid and c.business_key_=b.id");
		sql.append("\n inner join act_ru_task d on c.proc_inst_id_=d.proc_inst_id_ and ? ||':'||a.executor=d.assignee_");
		args.add(userInfo.getComId());
		sql.append("\n where a.busid=? and a.bustype=? and a.comid=? and a.executor=?");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		return (SpFlowCurExecutor) this.objectQuery(sql.toString(), args.toArray(), SpFlowCurExecutor.class);
	}
	
	/**
	 * 根据审批类型获取当前审批人集合
	 * @param comId 团队主键
	 * @param instanceId 流程实例主键
	 * @param executeType 办理类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowCurExecutor> getSpFlowCurExecutorByExecuteType(Integer comId,Integer instanceId,String executeType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* ");
		sql.append("\n from spFlowCurExecutor a ");
		sql.append("\n inner join spflowinstance b on a.busid=b.id and a.bustype=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n inner join act_hi_procinst c on c.id_=b.actinstaceid and c.business_key_=b.id");
		sql.append("\n inner join act_ru_task d on c.proc_inst_id_=d.proc_inst_id_ and ? ||':'||a.executor=d.assignee_");
		args.add(comId);
		sql.append("\n where a.busid=? and a.bustype=? and a.comid=? and a.executeType=?");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(comId);
		args.add(executeType);
		return this.listQuery(sql.toString(), args.toArray(),SpFlowCurExecutor.class);
	}
	/**
	 * 初始化流程是咧相关的变量主键
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例化主键
	 * @param flowId
	 *            流程模型主键
	 */
	public void initSpFlowRunVariableKey(Integer comId, Integer instanceId,
			Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into spFlowRunVariableKey(comId,busId,busType,variableKey) \n");
		sql.append("select comId,?,?,conditionVar from spstepconditions where comId=? and flowId=? \n");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取实例化流程变量主键集合
	 * 
	 * @param comId
	 * @param instanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowRunVariableKey> listSpFlowRunVariableKey(Integer comId,
			Integer instanceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from spFlowRunVariableKey a where a.comid=? and a.busId=? and a.busType=?");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		return this.listQuery(sql.toString(), args.toArray(),SpFlowRunVariableKey.class);
	}
	
	/**
	 * 删除失效审批流程附件自己的
	 * 
	 * @param userInfo
	 *            当前操作人
	 */
	public void delRelateTableWithBusId(Class<?> clz,UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from "+clz.getSimpleName()+" a");
		sql.append("\n where exists (");
		sql.append("\n		select b.id from spflowinstance b where a.busId=b.id and a.busType=? ");
		sql.append("\n		and a.comid=? and b.flowstate=0 and b.creator=?");
		sql.append("\n )");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());//
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
	 * 删除流程实例化对象表
	 * 
	 * @param userInfo
	 */
	public void delSpFlowInstance(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from spFlowInstance a where a.comid=? and a.flowstate=0 and a.creator=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		this.excuteSql(sql.toString(), args.toArray());// 流程实例化对象表
	}

	/**
	 * 获取个人权限的流程实例
	 * 
	 * @param userInfo
	 *            当前操作人
	 * @param instance
	 *            筛选参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowInstance> listSpFlowOfMine(UserInfo userInfo,
			SpFlowInstance instance) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select distinct(a.id) as instanceId,a.*,b.executeType,b.executor,c.username as executorName,c.gender,e.uuid,e.filename,");
		sql.append("\n cUser.username as creatorName,cUser.gender as creatorGender,cFile.uuid as creatorUuid,cFile.filename as creatorFileName,");
		sql.append("\n layout.formState formLayoutState");
		sql.append("\n from spFlowInstance a");
		sql.append("\n left join formlayout layout on a.formversion=layout.version and a.formkey=layout.formmodid");
		sql.append("\n left join spFlowCurExecutor b on a.comId = b.comid and a.id = b.busId and b.busType=? and b.executetype='"+ActLinkTypeEnum.ASSIGNEE.getValue()+"'");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n left join userinfo c on b.executor=c.id");
		sql.append("\n left join userOrganic d on a.comid = d.comid and c.id = d.userid");
		sql.append("\n left join upfiles e on d.mediumheadportrait = e.id");

		sql.append("\n inner join userinfo cUser on a.creator=cUser.id");
		sql.append("\n inner join userOrganic cOrg on a.comid = cOrg.comid and cUser.id = cOrg.userid");
		sql.append("\n left join upfiles cFile on cOrg.mediumheadportrait = cFile.id");

		sql.append("\n where 1=1 and a.flowstate<>0");
		if (null != instance.getFlowState() && instance.getFlowState() > -1) {
			this.addSqlWhere(instance.getFlowState(), sql, args,
					" and a.flowstate=?");// 流程状态筛选
		}
		if (null != instance.getCreator() && instance.getCreator() > 0) {
			this.addSqlWhere(instance.getCreator(), sql, args,
					" and  a.creator=?");// 流程发起人筛选
		}
		this.addSqlWhere(instance.getSpState(), sql, args, " and a.spState=?");// 审批状态筛选
		this.addSqlWhere(instance.getExecutor(), sql, args,
				" and  b.executor=?");// 流程审批人筛选
		if (null != instance.getListExecutor()
				&& !instance.getListExecutor().isEmpty()) {
			sql.append("	and  ( b.executor= 1 ");
			for (UserInfo executor : instance.getListExecutor()) {
				sql.append("or b.executor=?  \n");
				args.add(executor.getId());
			}
			sql.append("	 ) ");
		}
		// 查询创建时间段
		this.addSqlWhere(instance.getStartDate(), sql, args,
				" and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(instance.getEndDate(), sql, args,
				" and substr(a.recordcreatetime,0,10)<=?");
		this.addSqlWhereLike(instance.getFlowName(), sql, args,
				" and a.flowName like ? \n");// 流程名称筛选
		sql.append("\n and a.comid=? and a.creator=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		String orderBy = "";// 列表排序
		if (null == instance.getOrderBy() || "".equals(instance.getOrderBy())) {
			orderBy = " a.flowState,a.id desc";
		} else if ("executor".equals(instance.getOrderBy())) {
			orderBy = " c.username";
		} else if ("crTimeNewest".equals(instance.getOrderBy())) {
			orderBy = " a.recordCreateTime desc";
		} else if ("crTimeOldest".equals(instance.getOrderBy())) {
			orderBy = " a.recordCreateTime asc";
		}
		return this.pagedQuery(sql.toString(), orderBy, args.toArray(),
				SpFlowInstance.class);
	}

	/**
	 * 拷贝流程步骤表单授权
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param flowId
	 *            流程模型主键
	 */
	public void initSpFlowRunStepFormControl(Integer comId, Integer instanceId,
			Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowRunStepFormControl(comId,busId,busType,stepId,stepType,formControlKey,isFill)");
		sql.append("\n select a.comid,?,'022' busType,a.id stepId, a.steptype,b.formcontrolkey,b.isFill from spFlowStep a  \n");
		sql.append("\n inner join spStepFormControl b on a.comId=b.comid and a.id=b.stepid where a.comId=? and a.flowId=?");
		args.add(instanceId);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取流程步骤表单权限数据集
	 * 
	 * @param userInfo
	 *            当前操作人
	 * @param instanceId
	 *            流程实例主键
	 * @param stepId
	 *            流程步骤主键,0表示开始步骤
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowRunStepFormControl> listSpFlowRunStepFormControl(
			UserInfo userInfo, Integer instanceId, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from spFlowRunStepFormControl a ");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=?");
		args.add(userInfo.getComId());
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		if ("0".equals(stepId.toString())) {
			sql.append("\n and a.stepType=?");
			args.add("start");
		} else {
			sql.append("\n and a.stepid=?");
			args.add(stepId);
		}
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowRunStepFormControl.class);
	}

	/**
	 * 
	 * 获取个人权限下的所有审批流程
	 * 
	 * @param userInfo
	 *            当前操作人
	 * @param instance
	 *            筛选参数
	 * @param isForceInPersion
	 *            是否是模块监督人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowInstance> listSpFlowOfAll(UserInfo userInfo,
			SpFlowInstance instance, boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select distinct(a.id) as instanceId,a.*,b.executeType,b.executor,c.username as executorName,");
		sql.append("\n cUser.username as creatorName");
		sql.append("\n from spFlowInstance a");
		sql.append("\n left join spFlowHiExecutor hi on a.comId = hi.comid and a.id = hi.busId and hi.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n left join spFlowCurExecutor b on a.comId = b.comid and a.id = b.busId and b.busType=? and b.executetype='"+ActLinkTypeEnum.ASSIGNEE.getValue()+"'");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n left join userinfo c on b.executor=c.id");
		sql.append("\n inner join userinfo cUser on a.creator=cUser.id");
		sql.append("\n left join attention atten on a.comid=atten.comid and a.id=atten.busid ");
		sql.append("\n and atten.bustype='" + ConstantInterface.TYPE_FLOW_SP
				+ "' and atten.userid=?");
		sql.append("\n where 1=1 and a.flowstate<>0 and a.comid=?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());

		String attention = instance.getAttentionState();
		if (null != attention && "1".equals(attention)) {
			sql.append("\n and atten.id>0");
		}
		if (null != instance.getFlowState() && instance.getFlowState() > -1) {
			// 流程状态筛选
			this.addSqlWhere(instance.getFlowState(), sql, args,
					" and a.flowstate=?");
		}
		// 审批状态筛选
		this.addSqlWhere(instance.getSpState(), sql, args, " and a.spState=?");
		// 流程审批人筛选
		this.addSqlWhere(instance.getExecutor(), sql, args,
				" and  b.executor=?");
		if (null != instance.getListExecutor()
				&& !instance.getListExecutor().isEmpty()) {
			sql.append("	and  ( b.executor= 1 ");
			for (UserInfo executor : instance.getListExecutor()) {
				sql.append("or b.executor=?  \n");
				args.add(executor.getId());
			}
			sql.append("	 ) ");
		}
		if (null != instance.getCreator() && instance.getCreator() > 0) {
			// 流程发起人筛选
			this.addSqlWhere(instance.getCreator(), sql, args,
					" and  a.creator=?");
		}
		if (null != instance.getListCreator()
				&& !instance.getListCreator().isEmpty()) {
			sql.append("	 and  ( a.creator= 1 ");
			for (UserInfo owner : instance.getListCreator()) {
				sql.append("or a.creator=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		// 查询创建时间段
		this.addSqlWhere(instance.getStartDate(), sql, args,
				" and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(instance.getEndDate(), sql, args,
				" and substr(a.recordcreatetime,0,10)<=?");
		// 流程名称筛选
		this.addSqlWhereLike(instance.getFlowName(), sql, args,
				" and a.flowName like ? \n");

		// 查看权限范围界定
		if (!isForceInPersion) {
			sql.append("\n and (");
			// 发起人、当前审批人、历史审批人
			sql.append("\n a.creator=? or b.executor =? or hi.executor=?");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			args.add(userInfo.getId());

			//当前审批人上级权限验证
			sql.append("\n or exists(");
			sql.append("\n select id from myLeaders where creator = b.executor and leader = ? and comId = b.comId");
			sql.append("\n )\n");
			args.add(userInfo.getId());
			// 发起人上级权限验证
			sql.append("\n or exists(\n");
			sql.append("\n select id from myLeaders where creator = a.creator and leader = ? and comId = a.comId");
			sql.append("\n 	)");
			args.add(userInfo.getId());
			//会签过的人也要显示出来
			sql.append("\n or exists (");
			sql.append("\n select id from SpFlowHuiQianInfo where busId = a.id and comId = a.comId and ASSIGNEE=?  and busType = " + ConstantInterface.TYPE_FLOW_SP + "");
			args.add(userInfo.getId());
			sql.append("\n )");

			sql.append("\n 	) ");
		}
		//排除他人的草稿
		sql.append("\n and ((a.creator !=? and  a.flowState != 2) or a.creator=?)");
		args.add(userInfo.getId());
		args.add(userInfo.getId());

		// 列表排序
		String orderBy = "";
		if (null == instance.getOrderBy() || "".equals(instance.getOrderBy())) {
			orderBy = " a.flowState,a.id desc";
		} else if ("executor".equals(instance.getOrderBy())) {
			orderBy = " c.username";
		} else if ("crTimeNewest".equals(instance.getOrderBy())) {
			orderBy = " a.recordCreateTime desc";
		} else if ("crTimeOldest".equals(instance.getOrderBy())) {
			orderBy = " a.recordCreateTime asc";
		}
		// 1、监督人员（所有流程）
		// 2、自己的流程
		// 3、参与的流程
		// 4、下属的流程
		return this.pagedQuery(sql.toString(), orderBy, args.toArray(),
				SpFlowInstance.class);
	}

	/**
	 * 初始化流程实例步骤
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param flowId
	 *            拷贝流程模型主键
	 */
	public void initSpFlowHiStep(Integer comId, Integer instanceId,
			Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowHiStep(comId,busId,busType,stepId,stepName,stepType,executorWay,conditionExp,spCheckCfg)");
		sql.append("\n select comId,?,?,id,stepName,stepType,executorWay,conditionExp,spCheckCfg from spFlowStep where comId=? and flowId=?");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 初始化流程实例步骤条件
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param flowId
	 *            拷贝流程模型主键
	 */
	public void initSpFlowStepConditions(Integer comId, Integer instanceId,
			Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowStepConditions(comId,busId,busType,stepId,conditionVar,conditionType,conditionValue,conditionNum)");
		sql.append("\n select comId,?,?,stepId,conditionVar,conditionType,conditionValue,conditionNum from spStepConditions where comId=? and flowId=?");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 初始化流程实例步骤审批人
	 * 
	 * @param comId
	 *            单位主键
	 * @param busId
	 *            流程实例主键
	 * @param flowId
	 *            拷贝流程模型主键
	 */
	public void initSpFlowHiStepExecutor(Integer comId, Integer instanceId,
			Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowHiStepExecutor(comId,busId,busType,stepId,executor)");
		sql.append("\n select comId,?,?,stepId,executor from spFlowStepExecutor where comId=? and flowId=?");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 初始化流程实例步骤间关系表
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param flowId
	 *            拷贝流程模型主键
	 */
	public void initSpFlowHiStepRelation(Integer comId, Integer instanceId,
			Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowHiStepRelation(comId,busId,busType,curStepId,nextStepId,stepWay,defaultStep) ");
		sql.append("\n select comId,?,?,curStepId,nextStepId,stepWay,defaultStep from spFlowStepRelation where comId=? and flowId=? ");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 初始化子流程实例化后配置表
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param pflowId
	 *            拷贝主流程模型主键
	 * @param sonFlowId
	 *            关联子流程模型主键
	 */
	public void initSpFlowRunRelevanceCfg(Integer comId, Integer instanceId,
			Integer pflowId, Integer sonFlowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowRunRelevanceCfg(comId,busId,busType,fromFormControlKey,toFormControlKey) ");
		sql.append("\n select comId,?,?,fromFormControlKey,toFormControlKey from spFlowRelevanceCfg where comId=? and pflowId=? and sonFlowId=? ");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(comId);
		args.add(pflowId);
		args.add(sonFlowId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 分页查询流程关联附件
	 * 
	 * @param instanceId
	 *            流程实例化主键
	 * @param userInfo
	 *            当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowUpfile> listPagedSpFiles(Integer instanceId,
			UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select ");
		sql.append("\n allFiles.id,allFiles.busId,allFiles.userId,allFiles.upfileId,allFiles.filename,allFiles.uuid,");
		sql.append("\n allFiles.recordcreatetime,allFiles.creatorName,allFiles.addType,");
		sql.append("\n allFiles.gender,allFiles.userUuid,allFiles.userFileName,allFiles.fileext,allFiles.isPic");
		sql.append("\n from (");
		//审批过程产生的附件
		sql.append("\n select a.id,a.busId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,a.addType, ");
		sql.append("\n c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, ");
		sql.append("\n case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic ");
		sql.append("\n from spFlowUpfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id");
		sql.append("\n left join userinfo c on a.userid = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on cc.mediumHeadPortrait = d.id ");
		sql.append("\n where a.comid =? and a.busId =? and a.busType=?");
		args.add(userInfo.getComId());
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n union all");
		//审批留言产生的附件
		sql.append("\n select a.id,a.busId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,2, ");
		sql.append("\n c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, ");
		sql.append("\n case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic ");
		sql.append("\n from spflowtalkupfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id");
		sql.append("\n left join userinfo c on a.userid = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on cc.mediumHeadPortrait = d.id ");
		sql.append("\n where a.comid =? and a.busId =? and a.busType=?");
		args.add(userInfo.getComId());
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n ) allFiles");
		return this.pagedQuery(sql.toString(), " allFiles.isPic asc, allFiles.id desc",
				args.toArray(), SpFlowUpfile.class);
	
	}

	/**
	 * 查询流程关联附件
	 * 
	 * @param instanceId
	 *            流程实例化主键
	 * @param userInfo
	 *            当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowUpfile> listSpFiles(Integer instanceId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//审批过程产生的附件
		sql.append("\n select allFiles.busId,allFiles.userId,allFiles.upfileId,allFiles.filename,allFiles.uuid,");
		sql.append("\n allFiles.recordCreateTime,allFiles.creatorName,allFiles.fileExt");
		sql.append("\n from (");
		sql.append("\n select a.busId,a.userId,a.upfileId,b.filename,b.uuid,a.recordCreateTime,\n");
		sql.append("\n userT.username creatorName,b.fileExt \n");
		sql.append("\n from spFlowUpfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("\n left join userinfo userT on a.userId=userT.id\n");
		sql.append("\n where a.comid =? and a.busId = ? and a.bustype=? \n");
		args.add(userInfo.getComId());
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n union all");
		//审批留言产生的附件
		sql.append("\n select a.busId,a.userId,a.upfileId,b.filename,b.uuid,a.recordCreateTime,");
		sql.append("\n userT.username creatorName,b.fileExt ");
		sql.append("\n from spflowtalkupfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("\n left join userinfo userT on a.userId=userT.id\n");
		sql.append("\n where a.comid =? and a.busId = ? and a.bustype=? \n");
		args.add(userInfo.getComId());
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n ) allFiles");
		sql.append("\n order by allFiles.recordCreateTime ");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowUpfile.class);
	}

	/**
	 * 获取流程实例扭转历史步骤信息
	 * 
	 * @param instanceId
	 *            流程实例化主键
	 * @param userInfo
	 *            当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStep> listSpFlowHiStep(Integer instanceId,
			UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		/*sql.append("\n select d.stepid,c.name_ as stepname,d.steptype,userInfo.id as executor,userInfo.userName as executorName,c.name_,c.assignee_,");
		sql.append("\n TO_CHAR(c.start_time_, 'YYYY-MM-DD HH24:MI:SS AM DY') as startTime,");
		sql.append("\n TO_CHAR(c.end_time_, 'YYYY-MM-DD HH24:MI:SS AM DY') as endTime,c.duration_ as usedTime,");
		// sql.append("\n userInfo.gender,imgFile.uuid,imgFile.filename,spMsg.message_ as spMsg");
		sql.append("\n userInfo.gender,imgFile.uuid,imgFile.filename,UTL_RAW.CAST_TO_VARCHAR2(spMsg.full_msg_) as spMsg");
		sql.append("\n from act_hi_procinst a");
		sql.append("\n inner join spflowinstance b on a.id_=b.actinstaceid and a.business_key_=b.id");
		sql.append("\n inner join act_hi_taskinst c on b.actinstaceid=c.proc_inst_id_");
		sql.append("\n inner join spflowhistep d on d.comid=b.comid and d.busId=b.id and d.busType=? and c.task_def_key_='step_'||d.stepid");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n inner join organic org on b.comid = org.orgnum");
		sql.append("\n left join userinfo userInfo on org.orgnum ||':'||userInfo.id=c.assignee_");
		sql.append("\n left join userOrganic userOrg on b.comid = userOrg.comid and userInfo.id = userOrg.userid");
		sql.append("\n left join upfiles imgFile on userOrg.mediumheadportrait = imgFile.id");
		// sql.append("\n left join act_hi_taskinst hiTask on a.id_=hiTask.Proc_Inst_Id_ and c.task_def_key_=hiTask.Task_Def_Key_");
		sql.append("\n left join act_hi_comment spMsg on a.id_=spMsg.proc_inst_id_ and c.Id_=spMsg.task_id_");
		sql.append("\n where b.comid=? and b.id=?");
		sql.append("\n order by c.start_time_");
		args.add(userInfo.getComId());
		args.add(instanceId);*/

		//修改后
		sql.append("\n select allHiSteps.stepname,allHiSteps.name_,allHiSteps.assignee_,");
		sql.append("\n allHiSteps.startTime,allHiSteps.endTime,allHiSteps.usedTime,");
		sql.append("\n userInfo.id as executor,userInfo.userName as executorName,");
		sql.append("\n userInfo.gender,imgFile.uuid,imgFile.filename,UTL_RAW.CAST_TO_VARCHAR2(spMsg.full_msg_) as spMsg");
		sql.append("\n from (");
		sql.append("\n select c.name_ as stepname,c.name_,c.assignee_,b.comid,c.Id_,a.id_ as proc_inst_id_,");
		sql.append("\n TO_CHAR(c.start_time_, 'YYYY-MM-DD HH24:MI:SS AM DY') as startTime,");
		sql.append("\n TO_CHAR(c.end_time_, 'YYYY-MM-DD HH24:MI:SS AM DY') as endTime,c.duration_ as usedTime");
		sql.append("\n from act_hi_procinst a");
		sql.append("\n inner join spflowinstance b on a.id_=b.actinstaceid and a.business_key_=b.id");
		sql.append("\n right join act_hi_taskinst c on b.actinstaceid=c.proc_inst_id_");
		sql.append("\n where c.delete_reason_='completed' or c.end_time_ is null");
		sql.append("\n start with b.comid=? and b.id=?");
		args.add(userInfo.getComId());
		args.add(instanceId);
		sql.append("\n connect by prior c.id_=c.parent_task_id_");
		sql.append("\n ) allHiSteps ");
		sql.append("\n left join userinfo userInfo on ? ||':'||userInfo.id=allHiSteps.assignee_");
		args.add(userInfo.getComId());
		sql.append("\n left join userOrganic userOrg on allHiSteps.comid = userOrg.comid and userInfo.id = userOrg.userid");
		sql.append("\n left join upfiles imgFile on userOrg.mediumheadportrait = imgFile.id");
		sql.append("\n left join act_hi_comment spMsg on allHiSteps.Id_=spMsg.task_id_ and spMsg.Type_='comment'");//comment 意见文本
		sql.append("\n order by allHiSteps.endTime");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowHiStep.class);
	}

	/**
	 * 获取自己无效的流程数据集
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowInstance> listMyUselessSpFlowInstance(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from spflowinstance a where a.comid=? and a.creator=? and a.flowstate=0");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowInstance.class);
	}

	/**
	 * 查询流程使用次数记录
	 * 
	 * @param curuser
	 *            当前操作人
	 * @param flowId
	 *            流程主键
	 * @return
	 */
	public SpFlowUsedTimes querySpFlowUsedTimes(UserInfo curuser, Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from SpFlowUsedTimes a where a.comid=? and a.userId=? and a.flowId=?");
		args.add(curuser.getComId());
		args.add(curuser.getId());
		args.add(flowId);
		return (SpFlowUsedTimes) this.objectQuery(sql.toString(),
				args.toArray(), SpFlowUsedTimes.class);
	}

	/**
	 * 获取已执行的审批步骤集合
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 * @return 流程实例主键
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStep> listHistorySpStep(Integer comId,
			Integer instanceId,String taskDefKey) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.task_def_key_ as activitiTaskId,c.name_ as stepName from spflowinstance a");
		sql.append("\n inner join act_hi_procinst b on a.id = b.business_key_ and a.actinstaceid = b.id_");
		sql.append("\n inner join act_hi_taskinst c on b.id_=c.proc_inst_id_");
		sql.append("\n where a.comid=? and a.id =? and c.end_time_ is not null and c.task_def_key_!=?");
		args.add(comId);
		args.add(instanceId);
		args.add(taskDefKey);
		sql.append("\n group by c.task_def_key_,c.name_");
		// sql.append("\n order by c.start_time_");
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowHiStep.class);
	}

	/**
	 * 获取已执行步骤配置信息
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param activitiTaskId
	 *            流程审批部署主键
	 * @return
	 */
	public SpFlowHiStep querySpFlowHisStep(Integer comId, Integer instanceId,
			String activitiTaskId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.flowname,c.name_ as stepName,c.assignee_ as assignee from spflowinstance a");
		sql.append("\n inner join act_hi_procinst b on a.id=b.business_key_ and a.actinstaceid=b.id_");
		sql.append("\n inner join act_hi_taskinst c on b.id_ = c.proc_inst_id_ and c.end_time_ is not null");
		sql.append("\n where a.comid=? and a.id=? and c.task_def_key_=? and rownum=1");
		args.add(comId);
		args.add(instanceId);
		args.add(activitiTaskId);
		sql.append("\n order by c.start_time_ desc");
		return (SpFlowHiStep) this.objectQuery(sql.toString(), args.toArray(),
				SpFlowHiStep.class);
	}

	/**
	 * 获取团队下所有有效的审批流程
	 * 
	 * @param userInfo
	 *            当前操作人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowInstance> listSpFlowInstanceOfOrg(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.flowname from SpFlowInstance a ");
		sql.append("\n where a.comid=? and (a.flowstate<>0 and a.flowstate<>2) ");
		args.add(userInfo.getComId());
		sql.append("\n order by a.recordcreatetime asc");
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowInstance.class);
	}

	/**
	 * 审批流程实例查看权限验证
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param userId
	 *            验证人主键
	 * @return
	 */
	public SpFlowInstance authorCheck(Integer comId, Integer instanceId,
			Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id from (");
		sql.append("\n 	select a.id from (");
		sql.append("\n 			select a.id,a.creator userid from spFlowInstance a");
		sql.append("\n 			where 1=1 and a.flowstate<>0 and a.comid=? and a.id=?");
		args.add(comId);
		args.add(instanceId);
		sql.append("\n 			union all ");
		sql.append("\n 			select a.id,hi.executor userid from spFlowInstance a ");
		sql.append("\n 			left join spFlowHiExecutor hi on a.comId = hi.comid and a.id = hi.busId and hi.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 			where 1=1 and a.flowstate<>0 and a.comid=? and a.id=?");
		args.add(comId);
		args.add(instanceId);
		sql.append("\n 			union all ");
		sql.append("\n 			select a.id,b.executor userid from spFlowInstance a ");
		sql.append("\n 			left join spFlowCurExecutor b on a.comId = b.comid and a.id = b.busId and b.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 			where 1=1 and a.flowstate<>0 and a.comid=? and a.id=?");
		args.add(comId);
		args.add(instanceId);
		sql.append("\n		) a  where 1=1");
		// 查看权限范围界定
		sql.append("\n 	and (");
		// 发起人、当前审批人、历史审批人
		sql.append("\n 		a.userid=?  ");
		args.add(userId);
		// 上级权限验证
		// 历史审批人上级权限验证
		sql.append("\n  		or exists( ");
		sql.append("\n   			select sup.leader from myLeaders sup where sup.comid=? and sup.creator=a.userid and sup.leader=?\n");
		sql.append("\n  		) \n");
		args.add(comId);
		args.add(userId);
		sql.append("\n  	) \n");
		sql.append("\n  ) a \n");
		sql.append("\n  union  ");
		sql.append("\n  select busid id from ( ");
		// 审批负责人
		sql.append("\n   select b.id,b.busid,b.owner from task b \n");
		sql.append("\n   where b.busid=? and b.bustype=?\n");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n   union \n");
		// 审批执行人
		sql.append("\n   select b.id,b.busid,c.executor owner from task b \n");
		sql.append("\n   left join taskexecutor c on b.id=c.taskid and b.comid=c.comid\n");
		sql.append("\n   where b.busid=? and b.bustype=?\n");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n   union \n");
		// 审批移交人员
		sql.append("\n   select b.id,b.busid,c.FROMUSER owner from task b \n");
		sql.append("\n   left join taskhandover c on b.id=c.taskid and b.comid=c.comid\n");
		sql.append("\n   where b.busid=? and b.bustype=?\n");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n   union \n");
		// 审批办理人员
		sql.append("\n   select b.id,b.busid,c.toUSER owner from task b \n");
		sql.append("\n   left join taskhandover c on b.id=c.taskid and b.comid=c.comid\n");
		sql.append("\n   where b.busid=? and b.bustype=?\n");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n   ) b where 1=1 and b.owner=?\n");
		args.add(userId);
		return (SpFlowInstance) this.objectQuery(sql.toString(),
				args.toArray(), SpFlowInstance.class);
	}

	/**
	 * 审批的审批人员查看
	 * 
	 * @param comId
	 * @param instanceId
	 * @param userId
	 * @param intanceId
	 * @return
	 */
	public SpFlowInstance authorBaseCheck(Integer comId, Integer instanceId,
			Integer userId, Integer intanceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id from (");
		sql.append("\n 	select a.id from (");
		sql.append("\n 			select a.id,a.creator userid from spFlowInstance a");
		sql.append("\n 			where 1=1 and a.flowstate<>0 and a.comid=? and a.id=?");
		args.add(comId);
		args.add(instanceId);
		sql.append("\n 			union all ");
		sql.append("\n 			select a.id,hi.executor userid from spFlowInstance a ");
		sql.append("\n 			left join spFlowHiExecutor hi on a.comId = hi.comid and a.id = hi.busId and a.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 			where 1=1 and a.flowstate<>0 and a.comid=? and a.id=?");
		args.add(comId);
		args.add(instanceId);
		sql.append("\n 			union all ");
		sql.append("\n 			select a.id,b.executor userid from spFlowInstance a ");
		sql.append("\n 			left join spFlowCurExecutor b on a.comId = b.comid and a.id = b.busId and a.busType=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 			where 1=1 and a.flowstate<>0 and a.comid=? and a.id=?");
		args.add(comId);
		args.add(instanceId);
		sql.append("\n		) a  where 1=1");
		// 查看权限范围界定
		sql.append("\n 	and (");
		// 发起人、当前审批人、历史审批人
		sql.append("\n 		a.userid=?  ");
		args.add(userId);
		// 上级权限验证
		// 历史审批人上级权限验证
		sql.append("\n  		or exists( ");
		sql.append("\n   			select sup.leader from myLeaders sup where sup.comid=? and sup.creator=a.userid and sup.leader=?\n");
		sql.append("\n  		) \n");
		args.add(comId);
		args.add(userId);
		sql.append("\n  	) \n");
		sql.append("\n  ) a \n");
		sql.append("\n  union  ");
		sql.append("\n  select busid id from ( ");
		// 审批负责人
		sql.append("\n   select b.id,b.busid,b.owner from task b \n");
		sql.append("\n   where b.busid=? and b.bustype=? and b.id=?\n");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(intanceId);
		sql.append("\n   union \n");
		// 审批执行人
		sql.append("\n   select b.id,b.busid,c.executor owner from task b \n");
		sql.append("\n   left join taskexecutor c on b.id=c.taskid and b.comid=c.comid\n");
		sql.append("\n   where b.busid=? and b.bustype=? and b.id=?\n");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(intanceId);
		sql.append("\n   union \n");
		// 审批移交人员
		sql.append("\n   select b.id,b.busid,c.FROMUSER owner from task b \n");
		sql.append("\n   left join taskhandover c on b.id=c.taskid and b.comid=c.comid\n");
		sql.append("\n   where b.busid=? and b.bustype=? and b.id=?\n");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(intanceId);
		sql.append("\n   union \n");
		// 审批办理人员
		sql.append("\n   select b.id,b.busid,c.toUSER owner from task b \n");
		sql.append("\n   left join taskhandover c on b.id=c.taskid and b.comid=c.comid\n");
		sql.append("\n   where b.busid=? and b.bustype=? and b.id=?\n");
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(intanceId);
		sql.append("\n   ) b where 1=1 and b.owner=?\n");
		args.add(userId);
		return (SpFlowInstance) this.objectQuery(sql.toString(),
				args.toArray(), SpFlowInstance.class);
	}

	/**
	 * 查询流程步骤里面是否有需要直属上级审批的步骤
	 * 
	 * @param comId
	 *            团队主键
	 * @param flowId
	 *            发起流程主键
	 * @return
	 */
	public SpFlowStep haveDirectLeaderToSpStep(Integer comId, Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from spFlowStep a");
		sql.append("\n where a.comid=? and a.flowid=? ");
		args.add(comId);
		args.add(flowId);
		sql.append("\n and a.executorway='directLeader' and a.steptype='exe' and rownum=1");
		return (SpFlowStep) this.objectQuery(sql.toString(), args.toArray(),
				SpFlowStep.class);
	}

	/**
	 * 获取子流程实例化后映射配置集合
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowRunRelevanceCfg> listSpFlowRunRelevanceCfg(Integer comId,
			Integer instanceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.fromformcontrolkey,a.toformcontrolkey");
		sql.append("\n from SpFlowRunRelevanceCfg a where a.comid=? and a.busId=? and busType=?");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowRunRelevanceCfg.class);
	}

	/**
	 * 获取控件选项为多选，最终值为可能为多个的控件值对象
	 * 
	 * @param formFlowDataId
	 *            流程表单数据spFlowInputData主键
	 * @param comId
	 *            单位主键
	 * @param instanceId
	 *            流程实例主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowOptData> querySpFlowOptDataByMore(Integer formFlowDataId,
			Integer comId, Integer instanceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select A.ID,A.RECORDCREATETIME,A.COMID,A.INSTANCEID,A.FORMFLOWDATAID,A.OPTIONID,");
		sql.append("\n A.CONTENT,A.CONTENTMORE,A.DATABUSTYPE");
		// sql.append("\n case when spins.bustype='"+ConstantInterface.TYPE_OFFICAL_DOC_SEND+"' and b.componentkey='Department' ");
		// sql.append("\n  then dbms_lob.substr(get_depName(a.comid,a.OPTIONID),200)||org.orgname");
		// sql.append("\n else a.content end content");
		sql.append("\n from spFlowOptData a");
		sql.append("\n inner join spflowinstance spins on a.comid=spins.comid and a.instanceid=spins.id");
		sql.append("\n inner join spFlowInputData b on a.comid=b.comid and a.formflowdataid=b.id");
		sql.append("\n inner join organic org on org.orgnum=a.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(instanceId, sql, args, " and a.instanceId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(formFlowDataId, sql, args, " and a.formFlowDataId=?");
		sql.append("\n order by id asc");
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowOptData.class);

	}

	/**
	 * 获取控件选项为多选，最终值为一个的控件值对象
	 * 
	 * @param formFlowDataId
	 *            流程表单数据spFlowInputData主键
	 * @param comId
	 *            单位主键
	 * @param instanceId
	 *            流程实例主键
	 * @return
	 */
	public SpFlowOptData querySpFlowOptDataByOne(Integer formFlowDataId,
			Integer comId, Integer instanceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.content from spFlowInputData a ");
		sql.append("\n where a.comid=? and a.instanceid=? and a.id =?");
		args.add(comId);
		args.add(instanceId);
		args.add(formFlowDataId);
		return (SpFlowOptData) this.objectQuery(sql.toString(), args.toArray(),
				SpFlowOptData.class);
	}

	

	

	

	

	/**
	 * 变更activiti中当前审批审批表办理人
	 * 
	 * @param intanceId
	 *            审批主键
	 * @param actInstaceId
	 *            activiti实例主键
	 * @param assignee_
	 *            审批办理人
	 */
	public void update_act_ru_task(String intanceId, String actInstaceId,
			String assignee_) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update act_ru_task a set a.assignee_=? where a.proc_inst_id_=? and a.id_=?");
		args.add(assignee_);
		args.add(actInstaceId);
		args.add(intanceId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 变更activiti中历史审批审批表办理人
	 * 
	 * @param intanceId
	 *            审批主键
	 * @param actInstaceId
	 *            activiti实例主键
	 * @param assignee_
	 *            审批办理人
	 */
	public void update_act_hi_taskinst(String intanceId, String actInstaceId,
			String assignee_) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update act_hi_taskinst a set a.assignee_=? where a.proc_inst_id_=? and a.id_=?");
		args.add(assignee_);
		args.add(actInstaceId);
		args.add(intanceId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 变更activiti中历史动作记录表办理人
	 * 
	 * @param intanceId
	 *            审批主键
	 * @param actInstaceId
	 *            activiti实例主键
	 * @param assignee_
	 *            审批办理人
	 */
	public void update_act_hi_actinst(String intanceId, String actInstaceId,
			String assignee_) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update act_hi_actinst a set a.assignee_=? where a.proc_inst_id_=? and a.task_id_=?");
		args.add(assignee_);
		args.add(actInstaceId);
		args.add(intanceId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取审批表单内上传附件
	 * 
	 * @param userInfo
	 * @param instanceId
	 * @param formColKey 表单控件主键
	 * @return
	 */
	public SpFlowUpfile getOptionUpfile(UserInfo userInfo, Integer instanceId,
			Integer upfileId,Integer formColKey) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from SpFlowUpfile a where a.comId = ? and a.busId = ? and busType=? and upfileId = ? and formColKey=? ");
		args.add(userInfo.getComId());
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(upfileId);
		args.add(formColKey);
		return (SpFlowUpfile) this.objectQuery(sql.toString(), args.toArray(),
				SpFlowUpfile.class);
	}

	/**
	 * 获取审批表单内上传附件
	 * 
	 * @param userInfo
	 * @param instanceId
	 * @return
	 */
	public void delOptionUpfiles(UserInfo userInfo, Integer instanceId,
			Object[] upfiles,Integer formColKey) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from SpFlowUpfile a where a.comId = ? and a.busId = ? and busType=? and addType = 1 and formColKey=?");
		args.add(userInfo.getComId());
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(formColKey);
		this.addSqlWhereIn(upfiles, sql, args, " and upfileId not in ?");
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取流程实例步骤基本配置信息
	 * 
	 * @param comId
	 *            团队好主键
	 * @param instanceId
	 *            流程实例主键
	 * @param stepId
	 *            步骤主键
	 * @return
	 */
	public SpFlowHiStep querySpFlowNextStepInfo(Integer comId,
			Integer instanceId, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.busId,a.stepid,a.stepname,a.steptype,a.executorway,a.spcheckcfg,a.conditionExp ");
		sql.append("\n from spFlowHiStep a ");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.stepid=? ");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(stepId);
		return (SpFlowHiStep) this.objectQuery(sql.toString(), args.toArray(),
				SpFlowHiStep.class);
	}

	/**
	 * 查询步骤的条件信息
	 * 
	 * @param comId
	 *            团队号
	 * @param instanceId
	 *            流程实例化主键
	 * @param stepId
	 *            步骤主键
	 * @return
	 */
	public List<SpFlowStepConditions> listSpFlowStepConditions(Integer comId,
			Integer instanceId, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* ");
		sql.append("\n from spFlowStepConditions a ");
		sql.append("\n where a.comid=? and a.busId=? and a.bustype=? and a.stepid=? ");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowStepConditions.class);
	}

	/**
	 * 根据当前步骤主键以及步骤间关系获取下步步骤主键
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param curStepId
	 *            当前步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiStepRelation> querySpFlowHiStepRelation(Integer comId,
			Integer instanceId, Integer curStepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.nextstepid,a.busId,a.curstepid,nvl(a.DEFAULTSTEP,0) DEFAULTSTEP ");
		sql.append("\n from spFlowHiStepRelation a ");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.curstepid=?");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(curStepId);
		sql.append("\n order by a.DEFAULTSTEP nulls last ");
		return this.listQuery(sql.toString(), args.toArray(),
				SpFlowHiStepRelation.class);
	}

	/**
	 * 根据配置获取候选人信息
	 * 
	 * @param comId
	 *            团队主键
	 * @param instanceId
	 *            流程实例主键
	 * @param stepId
	 *            步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listSpFlowHiExecutor(Integer comId,
			Integer instanceId, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comid,u.id,u.gender,f.uuid,f.filename,u.userName");
		sql.append("\n from spFlowHiStepExecutor a ");
		sql.append("\n left join userorganic org on a.comid = org.comid and a.executor=org.userid");
		sql.append("\n left join Userinfo u on org.userid=u.id");
		sql.append("\n left join upfiles f on org.smallHeadPortrait=f.id and f.comid=org.comId");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.stepid=?");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 取得最近的审批执行人信息
	 * 
	 * @param comId
	 *            团队号
	 * @param instanceId
	 *            流程主键
	 * @param stepId
	 *            步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHiExecutor> listSpFlowHiExecutorForBack(Integer comId,
			Integer instanceId, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*");
		sql.append("\n from spflowhiexecutor a");
		sql.append("\n where a.comid=? and a.busId=? and a.busType=? and a.stepid=?");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		args.add(stepId);
		sql.append("\n order by id desc");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowHiExecutor.class);
	}

	/**
	 * 获取可以催办的事项执行人
	 * 
	 * @param userInfo
	 *            当前操作员
	 * @param busId
	 *            业务主键
	 * @param busType
	 *            业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusRemindUser> listSpFlowRemindExecutor(UserInfo userInfo,
			Integer busId, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.executor userId,b.username");
		sql.append("\n from spflowcurexecutor a ");
		sql.append("\n inner join userinfo b on a.executor = b.id ");
		sql.append("\n where a.busId = ? and a.busType=? and a.comid = ? and a.executor <> ? ");// 执行中和待认领的
		args.add(busId);
		args.add(busType);
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		return this.listQuery(sql.toString(), args.toArray(),
				BusRemindUser.class);
	}

	/**
	 * 获取activities子审批集合
	 * @param intanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskEntity> queryActivitiesSonTaskList(String intanceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from act_ru_task a where a.parent_task_id_=?");
		args.add(intanceId);
		return this.listQuery(sql.toString(), args.toArray(),TaskEntity.class);
	}

	/**
	 * 获取会签进度记录
	 * @param instaceId
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowHuiQianInfo> listSpHuiQianProcess(Integer instaceId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.recordcreatetime,a.comid,a.assignee,a.content,a.stepid,a.huiqiancontent,a.endtime,");
		sql.append("\n a.busid,a.bustype,c.proc_inst_id_,d.id_ as actTaskId,userInfo.userName");
		sql.append("\n from SpFlowHuiQianInfo a");
		sql.append("\n inner join spflowinstance b on a.busid=b.id and a.bustype=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n inner join act_hi_procinst c on c.id_=b.actinstaceid and c.business_key_=b.id");
		sql.append("\n inner join act_ru_task d on c.proc_inst_id_=d.proc_inst_id_ and ? ||':'||a.assignee=d.assignee_");
		args.add(userInfo.getComId());
		sql.append("\n left join userinfo userInfo on ? ||':'||userInfo.id=d.assignee_");
		args.add(userInfo.getComId());
		sql.append("\n where b.id = ? and a.endtime is null order by a.recordcreatetime desc");
		args.add(instaceId);
		return this.listQuery(sql.toString(), args.toArray(),SpFlowHuiQianInfo.class);
	}
	
	/**
	 * 获取会签进度记录统计
	 * @param instaceId
	 * @param userInfo
	 * @return
	 */
	public Integer spHuiQianProcessCount(Integer instaceId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(*) ");
		sql.append("\n from SpFlowHuiQianInfo a");
		sql.append("\n inner join spflowinstance b on a.busid=b.id and a.bustype=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n inner join act_hi_procinst c on c.id_=b.actinstaceid and c.business_key_=b.id");
		sql.append("\n inner join act_ru_task d on c.proc_inst_id_=d.proc_inst_id_ and ? ||':'||a.assignee=d.assignee_");
		args.add(userInfo.getComId());
		sql.append("\n left join userinfo userInfo on ? ||':'||userInfo.id=d.assignee_");
		args.add(userInfo.getComId());
		sql.append("\n where b.id = ? and a.endtime is null order by a.recordcreatetime desc");
		args.add(instaceId);
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 查询个人会签信息（在办的）
	 * @param intanceId
	 * @param userIfo
	 * @return
	 */
	public SpFlowHuiQianInfo querySpFlowHuiQianInfo(Integer instanceId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.recordcreatetime,a.comid,a.busid,a.bustype,a.assignee,a.content,a.stepid,a.huiqiancontent,a.endtime,");
		sql.append("\n c.username,d.flowname,e.proc_inst_id_,f.id_ as actTaskId,f.name_ as stepName");
		sql.append("\n from spFlowHuiQianInfo a");
		sql.append("\n inner join userorganic b on a.comid=b.comid and a.assignee=b.userid");
		sql.append("\n inner join userInfo c on b.userid=c.id");
		sql.append("\n inner join spflowinstance d on a.busid=d.id and a.bustype=?");
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n inner join act_hi_procinst e on e.id_=d.actinstaceid and e.business_key_=d.id");
		args.add(userInfo.getComId());
		sql.append("\n inner join act_ru_task f on e.proc_inst_id_=f.proc_inst_id_ and ? ||':'||a.assignee=f.assignee_");
		sql.append("\n where a.assignee=? and a.endtime is null and a.comid=? and a.busid=? and a.bustype=?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		return (SpFlowHuiQianInfo) this.objectQuery(sql.toString(), args.toArray(), SpFlowHuiQianInfo.class);
	}

	/**
	 * 获取审批留言
	 * @param instanceId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowTalk> listSpFlowTalk(Integer instanceId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select connect_by_isleaf as isLeaf,PRIOR a.speaker as pSpeaker,PRIOR a.content as pContent, \n");
		sql.append("PRIOR b.username as pSpeakerName,a.*,b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from SpFlowTalk a \n");
		sql.append("inner join userinfo b on a.speaker = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on  bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.busId = ? and a.busType=? \n");
		sql.append("start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid \n");
		sql.append("order siblings by a.recordcreatetime desc,a.id");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		return this.pagedQuery(sql.toString(), null, args.toArray(), SpFlowTalk.class);
	}
	
	/**
	 * 获取审批统计
	 * @param instanceId
	 * @param comId
	 * @return
	 */
	public Integer spFlowTalkCount(Integer instanceId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select count(*) \n");
		sql.append("from SpFlowTalk a \n");
		sql.append("inner join userinfo b on a.speaker = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on  bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.busId = ? and a.busType=? \n");
		sql.append("start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid \n");
		sql.append("order siblings by a.recordcreatetime desc,a.id");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 获取留言附件
	 * @param comId
	 * @param instanceId
	 * @param spFlowTalkId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowTalkUpfile> listSpFlowTalkFile(Integer comId, Integer instanceId, Integer spFlowTalkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.busId,a.busType,a.spFlowTalkId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext,\n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from spFlowTalkUpfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comid =? and a.busId = ? and a.busType=?  \n");
		args.add(comId);
		args.add(instanceId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		this.addSqlWhere(spFlowTalkId, sql, args, " and a.spFlowTalkId=?");
		sql.append("order by isPic asc, a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), SpFlowTalkUpfile.class);
	}

	/**
	 * 获取留言详情
	 * @param id
	 * @param comId
	 * @return
	 */
	public SpFlowTalk querySpFlowTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select d.speaker as pSpeaker,d.content as pContent,e.username as pSpeakerName,a.*,b.username as speakerName,b.gender,c.uuid,c.filename from spFlowTalk a \n");
		sql.append("inner join userinfo b on a.speaker = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on  bb.mediumHeadPortrait = c.id \n");
		sql.append("left join spFlowTalk d on a.parentid = d.id and a.comid = d.comid \n");
		sql.append("left join userinfo e on d.speaker = e.id \n");
		sql.append("where a.comId=? and a.id = ?");
		args.add(comId);
		args.add(id);
		return (SpFlowTalk)this.objectQuery(sql.toString(), args.toArray(), SpFlowTalk.class);
	}

	/**
	 * 删除留言
	 * @param id
	 * @param comId
	 */
	public void delSpFlowTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from spFlowTalk a where a.comid =? and a.id in \n");
		sql.append("(select id from spFlowTalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 更新留言父级
	 * @param id
	 * @param comId
	 */
	public void updateSpFlowTalkParentId(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update spFlowTalk set parentId=(select c.parentid \n");
		sql.append("from spFlowTalk c \n");
		sql.append("where c.id=?) where parentid = ? and comId = ? \n");
		args.add(id);
		args.add(id);
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取审批流程需要被通知的相关人员（发起人、审批人、会签人以及关注人）
	 * @param comId
	 * @param intanceId
	 * @param needExecuor
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listSpFlowUserForMsg(Integer comId, Integer intanceId, boolean needExecuor) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//审批负责人
		sql.append("select d.id,d.email,d.wechat,d.qq,d.userName from spFlowInstance c \n");
		sql.append("inner join userinfo d on  c.creator = d.id \n");
		sql.append("inner join userorganic e on  c.comid=e.comid and d.id=e.userid and e.enabled=1 \n");
		sql.append("where c.comid = ? and c.id = ? \n");
		args.add(comId);
		args.add(intanceId);
		sql.append("union \n");
		if(needExecuor){
			//审批执行人
			sql.append("\n select f.id,f.email,f.wechat,f.qq,f.userName ");
			sql.append("\n from spFlowCurExecutor a ");
			sql.append("\n inner join spflowinstance b on a.busid=b.id and a.bustype=?");
			args.add(ConstantInterface.TYPE_FLOW_SP);
			sql.append("\n inner join act_hi_procinst c on c.id_=b.actinstaceid and c.business_key_=b.id \n");
			sql.append("\n inner join act_ru_task d on c.proc_inst_id_=d.proc_inst_id_ and ? ||':'||a.executor=d.assignee_ \n");
			args.add(comId);
			sql.append("inner join userinfo f on a.executor = f.id \n");
			sql.append("inner join userorganic e on  a.comid=e.comid and f.id=e.userid and e.enabled=1 \n");
			sql.append("\n where a.busid=? and a.bustype=? and a.comid=? ");
			args.add(intanceId);
			args.add(ConstantInterface.TYPE_FLOW_SP);
			args.add(comId);
			sql.append("union \n");
		}
		//审批关注人
		sql.append("select f.id,f.email,f.wechat,f.qq,f.userName from attention e \n");
		sql.append("inner join userinfo f on  e.userId = f.id \n");
		sql.append("and e.busType="+ConstantInterface.TYPE_FLOW_SP+" \n");
		sql.append("inner join userorganic d on  e.comid=d.comid and f.id=d.userid and d.enabled=1 \n");
		sql.append("where e.comid = ? and e.busId = ? \n");
		args.add(comId);
		args.add(intanceId);
		return this.listQuery(sql.toString(),args.toArray(),UserInfo.class);
	}

	/**
	 * 分页查询模块关联的审批列表数据
	 * @param sessionUser 当前操作人员
	 * @param flowInstance 审批的查询条件
	 * @return
	 */
	public PageBean<SpFlowInstance> listPagedBusSpflow(UserInfo sessionUser,
			SpFlowInstance flowInstance) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.recordCreateTime,a.comId");
		sql.append("\n ,a.flowName,a.busId,a.busType,a.creator,u.userName as creatorName");
		sql.append("\n ,a.spstate,a.flowState");
		sql.append("\n from spflowInstance a ");
		sql.append("\n inner join userInfo u on a.creator=u.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(flowInstance.getBusId(), sql, args, "\n and a.busId=?");
		this.addSqlWhere(flowInstance.getBusType(), sql, args, "\n and a.busType=?");
		return this.pagedBeanQuery(sql.toString(), " a.id desc", args.toArray(), SpFlowInstance.class);
	}
}
