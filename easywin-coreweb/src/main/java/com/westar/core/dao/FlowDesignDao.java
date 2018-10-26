package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.FormCompon;
import com.westar.base.model.SpFlowHiStep;
import com.westar.base.model.SpFlowModel;
import com.westar.base.model.SpFlowRelevanceCfg;
import com.westar.base.model.SpFlowScopeByDep;
import com.westar.base.model.SpFlowScopeByUser;
import com.westar.base.model.SpFlowStep;
import com.westar.base.model.SpFlowStepRelation;
import com.westar.base.model.SpFlowType;
import com.westar.base.model.SpStepConditions;
import com.westar.base.model.SpStepFormControl;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class FlowDesignDao extends BaseDao {

	/**
	 * 查询获取流程配置信息
	 * @param userInfo
	 * @param flowId
	 * @return
	 */
	public SpFlowModel querySpFlowModel(UserInfo userInfo, Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.username as creatorName,b.gender,d.uuid,d.filename,e.modname as formName,");
		sql.append("\n f.typeName as spFlowTypeName,a.sonFlowId,sonFlow.flowname as sonFlowName");
		sql.append("\n from spFlowModel a");
		sql.append("\n inner join userinfo b on a.creator = b.id");
		sql.append("\n inner join userOrganic c on b.id =c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on  c.smallheadportrait = d.id");
		sql.append("\n left join formMod e on a.comid=e.comid and a.formkey=e.id");
		sql.append("\n left join spFlowType f on  f.comId = a.comId and f.id=a.spFlowTypeId");
		sql.append("\n left join spFlowModel sonFlow on a.comid=sonFlow.comid and a.sonFlowId = sonFlow.id");
		sql.append("\n where a.id=? and a.comid=?");
		args.add(flowId);
		args.add(userInfo.getComId());
		return (SpFlowModel)this.objectQuery(sql.toString(), args.toArray(), SpFlowModel.class);
	}

	/**
	 * 获取流程模型集合
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowModel> listFlowModel(UserInfo userInfo,SpFlowModel spFlowModel){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.username as creatorName,b.gender,d.uuid,d.filename,e.typeName as spFlowTypeName from spFlowModel a ");
		sql.append("\n inner join userinfo b on a.creator = b.id");
		sql.append("\n inner join userOrganic c on b.id =c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on  c.smallheadportrait = d.id");
		sql.append("\n left join spFlowType e on  e.comId = a.comId and e.id=a.spFlowTypeId");
		sql.append("\n where a.comid=?");
		args.add(userInfo.getComId());
		this.addSqlWhere(spFlowModel.getSpFlowTypeId(), sql, args, " and a.spFlowTypeId=?");//分类筛选
		this.addSqlWhereLike(spFlowModel.getFlowName(), sql, args, " and a.flowName like ? \n");//名称筛选
		this.addSqlWhere(spFlowModel.getStatus(), sql, args, " and a.status=?");//流程启用状态筛选
		
		String orderBy ="";//列表排序
		if("flowNameDesc".equals(spFlowModel.getOrderBy())){
			orderBy =" a.flowName desc";
		}else if("flowNameAsc ".equals(spFlowModel.getOrderBy())){
			orderBy =" a.flowName asc";
		}else if("crTimeNewest".equals(spFlowModel.getOrderBy())){
			orderBy =" a.status,a.recordCreateTime desc";
		}else if("crTimeOldest".equals(spFlowModel.getOrderBy())){
			orderBy =" a.status,a.recordCreateTime asc";
		}else{
			orderBy =" a.status desc,a.id desc";
		}
		return this.pagedQuery(sql.toString(),orderBy,args.toArray(),SpFlowModel.class);
	}
	
	/**
	 * 获取所有的流程步骤集合
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStep> listFlowSteps(Integer comId, Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from (");//最后结果排序层
		sql.append("\n select * from (");//树形查询语句外封装一层
		sql.append("\n select a.*,b.nextstepid,b.stepWay as nextStepWay,level as stepLevel  from spFlowStep a ");//有效步骤
		sql.append("\n left join spFlowStepRelation b on a.comid = b.comid "
				+ "and a.flowid = b.flowid and a.id = b.curstepid");
		sql.append("\n where a.comid=? and a.flowid=?");
		args.add(comId);
		args.add(flowId);
		sql.append("\n start with a.steptype='start'");
		sql.append("\n connect by prior b.nextstepid= a.id");
		sql.append("\n order by level");
		sql.append("\n ) ");//树形查询语句外封装一层
		sql.append("\n union");//合并
		sql.append("\n select a.*,b.nextstepid,b.stepWay as nextStepWay,10000 as stepLevel from Spflowstep a");//无效步骤
		sql.append("\n left join spFlowStepRelation b on a.comid=b.comid and a.flowid=b.flowid and a.id=b.nextstepid");
		sql.append("\n where a.comid=? and a.flowid=? and a.steptype<>'start' and b.nextstepid is null");
		args.add(comId);
		args.add(flowId);
		sql.append("\n )order by stepLevel");//最后结果排序层
		return this.listQuery(sql.toString(), args.toArray(),SpFlowStep.class);
	}
	
	/**
	 * 获取有效的流程步骤集合
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStep> listFlowStepsForDeploy(Integer comId, Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.nextstepid,b.stepWay as nextStepWay,level as stepLevel  from spFlowStep a ");//有效步骤
		sql.append("\n left join spFlowStepRelation b on a.comid = b.comid "
				+ "and a.flowid = b.flowid and a.id = b.curstepid");
		sql.append("\n where a.comid=? and a.flowid=?");
		args.add(comId);
		args.add(flowId);
		sql.append("\n start with a.steptype='start'");
		sql.append("\n connect by prior b.nextstepid= a.id");
		sql.append("\n order by level");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowStep.class);
	}
	
	/**
	 * 取得当前步骤的所有前一步骤
	 * @param comId
	 * @param flowId
	 * @param stepId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStepRelation> listFlowParentStep(Integer comId, Integer flowId,Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n SELECT A.* FROM SPFLOWSTEPRELATION A");
		sql.append("\n WHERE A.COMID=? AND A.FLOWID=? ");
		args.add(comId);
		args.add(flowId);
		sql.append("\n start with a.curstepid=? connect by prior a.curstepid= a.nextstepid ");
		args.add(stepId);
		sql.append("\n order siblings by a.curstepid");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowStepRelation.class);
	}

	/**
	 * 根据父节点设置步骤的下步骤
	 * @param stepRelation
	 */
	public void updateNextStepByPstepId(SpFlowStepRelation stepRelation) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update spFlowStepRelation a set a.curStepId = ? where a.comid=? and a.flowid=? "
				+ "and a.curStepId=? and a.nextstepid !=?");
		args.add(stepRelation.getCurStepId());
		args.add(stepRelation.getComId());
		args.add(stepRelation.getFlowId());
		args.add(stepRelation.getOldCurStepId());
		args.add(stepRelation.getCurStepId());
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取流程的下步骤集合
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @param stepId 步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStep> listNextFlowStep(Integer comId, Integer flowId,
			Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.defaultStep,b.stepWay from spFlowStep a ");
		sql.append("\n left join Spflowsteprelation b on a.comid = b.comid and a.flowid = b.flowid ");
		sql.append("\n and a.id = b.nextstepid");
		args.add(comId);
		sql.append("\n where a.comid =? and a.flowid=? and b.curstepid=?");
		sql.append("\n order by b.defaultstep");
		args.add(flowId);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(),SpFlowStep.class);
	}

	/**
	 * 获取流程步骤父步骤集合
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @param stepId 流程步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStep> listParentFlowStep(Integer comId, Integer flowId,
			Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from spFlowStep a ");
		sql.append("\n left join Spflowsteprelation b on a.comid = b.comid and a.flowid = b.flowid ");
		sql.append("\n and a.id = b.curstepid");
		args.add(comId);
		sql.append("\n where a.comid =? and a.flowid=? and b.nextstepid=?");
		args.add(flowId);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(),SpFlowStep.class);
	}
	
	/**
	 * 
	 * 根据步骤主键获取所有的后代步骤数据集
	 * @param comId 团地主键
	 * @param flowId 流程主键
	 * @param stepId 步骤主键
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStep> listChildrenFlowStepByStepId(Integer comId,
			Integer flowId, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.nextstepid,level  from spFlowStep a ");
		sql.append("\n left join spFlowStepRelation b on a.comid = b.comid and a.flowid = b.flowid and a.id = b.curstepid");
		sql.append("\n where a.comid=? and a.flowid=? and a.id !=? ");
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		sql.append("\n start with a.id = ?");
		args.add(stepId);
		sql.append("\n connect by prior  b.nextstepid= a.id");
		return this.listQuery(sql.toString(), args.toArray(),SpFlowStep.class);
	}

	/**
	 * 更新同级步骤扭转方式类型
	 * @param stepRel 步骤关系
	 */
	public void updateChildrenFlowStepInSameStepWay(SpFlowStepRelation stepRel) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update spFlowStepRelation a set a.stepWay=?");
		sql.append("\n where a.comid=? and a.flowid=? and a.curstepid = ? ");
		args.add(stepRel.getStepWay());
		args.add(stepRel.getComId());
		args.add(stepRel.getFlowId());
		args.add(stepRel.getCurStepId());
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 
	 * 获取流程步骤基本信息
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @param stepId 流程步骤主键
	 * @return
	 */
	public SpFlowStep querFlowStep(Integer comId, Integer flowId, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from spFlowStep a");
		sql.append("\n where a.comid = ? and a.flowid = ? and a.id = ?");
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		return (SpFlowStep)this.objectQuery(sql.toString(), args.toArray(),SpFlowStep.class);
	}

	/**
	 * 查询步骤间关系
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @param curStepId 当前步骤主键
	 * @param nextStepId 下步步骤主键
	 * @return
	 */
	public SpFlowStepRelation querySpFlowStepRelation(Integer comId,
			Integer flowId, Integer curStepId, Integer nextStepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from spFlowStepRelation a ");
		sql.append("\n where a.comid = ? and a.flowid = ? and a.curstepid =? and a.nextstepid =?");
		args.add(comId);
		args.add(flowId);
		args.add(curStepId);
		args.add(nextStepId);
		return (SpFlowStepRelation)this.objectQuery(sql.toString(), args.toArray(),SpFlowStepRelation.class);
	}

	/**
	 * 获取流程的其它步骤
	 * @param comId 团地主键
	 * @param flowId 流程主键
	 * @param stepId 比对步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStep> listOtherFlowStep(Integer comId, Integer flowId,
			Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,case when d.nextStepId is not null then 1 else 0 end as isMine from spFlowStep a ");
		sql.append("\n left join(");
		sql.append("\n select b.comid, b.flowid,c.nextstepid from spFlowStep b inner join spFlowStepRelation c ");
		sql.append("\n on b.comid=c.comid and b.flowid=c.flowid and b.id=c.curstepid ");
		sql.append("\n and b.comid=? and b.flowid=? and b.id=?");
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		sql.append("\n ) d on a.comid=d.comid and a.flowid=d.flowid and a.id=d.nextstepid");
		sql.append("\n where a.comid=? and a.flowid=? and a.id<>?");
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(),SpFlowStep.class);
	}

	/**
	 * 更新同级默认步骤状态为0
	 * @param stepRel 步骤配置信息
	 */
	public void updateOtherFlowDefaultStepTo0(SpFlowStepRelation stepRel) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update spFlowStepRelation a set a.defaultstep=0 ");
		sql.append("\n where a.comid=? and a.flowid=? and a.curstepid=? and b.nextstepid<>?");
		args.add(stepRel.getComId());
		args.add(stepRel.getFlowId());
		args.add(stepRel.getCurStepId());
		args.add(stepRel.getNextStepId());
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取步骤审批人数据集
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @param stepId 步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listExecutorOfFlowStep(Integer comId, Integer flowId,
			Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.comid,c.flowid,c.stepid,a.id,a.username,a.gender,d.uuid,d.filename from userinfo a ");
		sql.append("\n inner join userOrganic b on a.id =b.userId ");
		sql.append("\n inner join spFlowStepExecutor c on a.id = c.executor and b.comid = c.comid ");
		sql.append("\n and c.comid=? and c.flowid= ? and c.stepid=? ");
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		sql.append("\n left join upfiles d on b.mediumHeadPortrait = d.id");
		return this.listQuery(sql.toString(), args.toArray(),UserInfo.class);
	}

	/**
	 *  获取步骤授权表单控件数据集
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @param stepId 步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpStepFormControl> listFormCompon(Integer comId,
			Integer flowId, Integer stepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from SpStepFormControl a");
		sql.append("\n where a.comid=? and a.flowid= ? and a.stepid=?");
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(),SpStepFormControl.class);
	}

	/**
	 * 获取表单条件数据集
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @param stepId 步骤主键
	 * @param formLayoutId 表单布局主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpStepConditions> listFlowStepConditions(Integer comId,
			Integer flowId, Integer stepId,Integer formLayoutId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*, ");
		sql.append("\n case when a.conditionvar=10000 then '发起人'");
		sql.append("\n when a.conditionvar=11000 then '发起人所在部门' else c.title end as conditionVarName");
		sql.append("\n from spstepconditions a");
		sql.append("\n left join Spflowmodel b on a.comid=b.comid and a.flowid=b.id");
		sql.append("\n left join formCompon c on a.comid=c.comid and a.conditionvar=c.fieldid and b.formkey=c.formmodid and c.formlayoutid=?");
		args.add(formLayoutId);
		sql.append("\n where a.comid=? and a.flowid=? and a.stepid=?");
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		return this.listQuery(sql.toString(), args.toArray(),SpStepConditions.class);
	}

	/**
	 * 获取团队审批流程
	 * @param curUser 当前操作人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowModel> listSpFlowForSp(UserInfo curUser,SpFlowModel spFlowModel) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select distinct(a.id),a.flowname,a.formkey,e.typeName as spFlowTypeName, ");
		sql.append("\n layout.formState ");
		sql.append("\n from spflowmodel a ");
		sql.append("\n inner join formMod on a.comid=formMod.comid and formMod.id=a.formKey");
		sql.append("\n  inner join (");
 		sql.append("\n  select * from (");
 		sql.append("\n  	select a.*,row_number() over (partition by a.formmodid order by a.version desc) as new_order");
 		sql.append("\n   	from formlayout  a where 1=1 and a.comid=?");
 		args.add(curUser.getComId());
 		sql.append("\n  ) a where new_order=1");
 		sql.append("\n )layout on formMod.id=layout.formmodid");
		sql.append("\n left join spFlowType e on  e.comId = a.comId and e.id=a.spFlowTypeId");
		sql.append("\n left join spFlowScopeByDep byDep on a.comid=byDep.comid and a.id=byDep.flowid");
		sql.append("\n left join spFlowScopeByUser byUser on a.comid=byUser.comid and a.id=byUser.flowid");
		sql.append("\n where a.act_deployment_id is not null and a.status=1 and a.comid = ? and a.flowModBusType=? ");
		args.add(curUser.getComId());
		args.add(ConstantInterface.TYPE_FLOW_SP);
		
		//排除模块化数据
		sql.append("\n and not exists(");
		sql.append("\n select busMapFlow.id from busMapFlow where busMapFlow.comid=? and a.id=busMapFlow.flowId");
		args.add(curUser.getComId());
		sql.append("\n )");
		
		sql.append("\n and");
		/***权限验证开始***/
		sql.append("\n (");
		sql.append("\n (byUser.userid is null and byDep.depid is null)");
		sql.append("\n or (byUser.userid=?)");
		args.add(curUser.getId());
		sql.append("\n or (");
		sql.append("\n exists (select dep.id,dep.depname,dep.parentid from department dep");
		sql.append("\n where dep.comid=? and dep.id=?");
		args.add(curUser.getComId());
		args.add(curUser.getDepId());
		sql.append("\n start with dep.id=byDep.depid");
		sql.append("\n connect by prior dep.id = dep.parentid)");
		sql.append("\n )");
		sql.append("\n )");
		/***权限验证结束***/
		this.addSqlWhere(spFlowModel.getSpFlowTypeId(), sql, args, " and a.spFlowTypeId=?");//分类筛选
		this.addSqlWhereLike(spFlowModel.getFlowName(), sql, args, " and a.flowName like ? \n");//名称筛选
		return this.listQuery(sql.toString(),args.toArray(),SpFlowModel.class);
	}

	/**
	 *  获取团队流程分类
	 * @param curUser 当前操作人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowType> listSpFlowType(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from spFlowType a where 1=1");
		this.addSqlWhere(curUser.getComId(), sql, args, " and a.comId=?");
		sql.append("\n order by a.orderNo,a.id");
		return this.listQuery(sql.toString(), args.toArray(), SpFlowType.class);
	}

	/**
	 * 更新流程分类为0状态
	 * @param spFlowTypeId 流程类型主键
	 * @param comId 团队主键
	 */
	public void updateSpFlowTypeTo0(Integer spFlowTypeId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n update spFlowModel set spFlowTypeId=0 where spFlowTypeId=? and comid=?");
 		args.add(spFlowTypeId);
 		args.add(comId);
 		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取流程分类最大排序序号
	 * @param comId 团队主键
	 * @return
	 */
	public SpFlowType querySpFlowTypeOrderMax(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.orderNo)+1  as orderNo from spFlowType a where a.comid =?");
		args.add(comId);
		return (SpFlowType)this.objectQuery(sql.toString(), args.toArray(),SpFlowType.class);
	}

	/**
	 * 获取个人频繁使用的前4流程
	 * @param curUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowModel> listHourlySpFlow(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from (");// rownum<=4 封装
		sql.append("\n select * from (");//排序封装
		sql.append("\n select distinct(a.id),a.flowname,a.formkey,b.username as creatorName,b.gender,");
		sql.append("\n d.uuid,d.filename,e.typeName as spFlowTypeName,used.times,layout.formState ");
		sql.append("\n from spFlowModel a ");
		sql.append("\n inner join spFlowUsedTimes used on a.comid=used.comid and a.id=used.flowid");
		sql.append("\n inner join userinfo b on a.creator = b.id");
		sql.append("\n inner join userOrganic c on b.id =c.userId and a.comId=c.comId");
		
		sql.append("\n inner join formMod on a.comid=formMod.comid and formMod.id=a.formKey");
		sql.append("\n  inner join (");
 		sql.append("\n  select * from (");
 		sql.append("\n  	select a.*,row_number() over (partition by a.formmodid order by a.version desc) as new_order");
 		sql.append("\n   	from formlayout  a where 1=1 and a.comid=?");
 		args.add(curUser.getComId());
 		sql.append("\n  ) a where new_order=1");
 		sql.append("\n )layout on formMod.id=layout.formmodid");
 		
		sql.append("\n left join upfiles d on  c.smallheadportrait = d.id");
		sql.append("\n left join spFlowType e on  e.comId = a.comId and e.id=a.spFlowTypeId");
		sql.append("\n left join spFlowScopeByDep byDep on a.comid=byDep.comid and a.id=byDep.flowid");
		sql.append("\n left join spFlowScopeByUser byUser on a.comid=byUser.comid and a.id=byUser.flowid");
		sql.append("\n where a.comid=? and used.userid=? and a.status=1 and a.flowModBusType=?");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		args.add(ConstantInterface.TYPE_FLOW_SP);
		
		//排除模块化数据
		sql.append("\n and not exists(");
		sql.append("\n select busMapFlow.id from busMapFlow where busMapFlow.comid=? and a.id=busMapFlow.flowId");
		args.add(curUser.getComId());
		sql.append("\n )");
				
		sql.append("\n and");
		/***权限验证开始***/
		sql.append("\n (");
		sql.append("\n (byUser.userid is null and byDep.depid is null)");
		sql.append("\n or (byUser.userid=?)");
		args.add(curUser.getId());
		sql.append("\n or (");
		sql.append("\n exists (select dep.id,dep.depname,dep.parentid from department dep");
		sql.append("\n where dep.comid=? and dep.id=?");
		args.add(curUser.getComId());
		args.add(curUser.getDepId());
		sql.append("\n start with dep.id=byDep.depid");
		sql.append("\n connect by prior dep.id = dep.parentid)");
		sql.append("\n )");
		sql.append("\n )");
		/***权限验证结束***/
		sql.append("\n ) usedFlows");
		sql.append("\n order by usedFlows.times desc ");
		sql.append("\n ) where rownum<=4");
		return this.listQuery(sql.toString(), args.toArray(), SpFlowModel.class);
	}

	/**
	 * 获取流程部门范围设置数据集
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowScopeByDep> listSpFlowScopeByDep(Integer comId,
			Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.depname ");
		sql.append("\n from spFlowScopeByDep a");
		sql.append("\n inner join department b on a.comid=b.comid and a.depid=b.id");
		sql.append("\n where a.comid=? and a.flowid=? and b.enabled=1");
		args.add(comId);
		args.add(flowId);
		return this.listQuery(sql.toString(), args.toArray(), SpFlowScopeByDep.class);
	}

	/**
	 * 获取流程人员范围设置数据集
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowScopeByUser> listSpFlowScopeByUser(Integer comId,
			Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,c.username ");
		sql.append("\n from spFlowScopeByUser a");
		sql.append("\n inner join userOrganic b on a.comid=b.comid");
		sql.append("\n inner join userInfo c on b.userid=c.id and a.userid=c.id");
		sql.append("\n where a.comid=? and a.flowid=? and b.enabled=1");
		args.add(comId);
		args.add(flowId);
		return this.listQuery(sql.toString(), args.toArray(), SpFlowScopeByUser.class);
	}

	/**
	 * 克隆通过部门控制流程范发起权限
	 * @param comId 团队主键
	 * @param flowId 被克隆流程主键
	 * @param newFlowId 新流程主键
	 */
	public void cloneSpFlowScopeByDep(Integer comId, Integer flowId,
			Integer newFlowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowScopeByDep(comId,flowId,depId)");
		sql.append("\n select comId,?,depId from spFlowScopeByDep where comId=? and flowId=?");
		args.add(newFlowId);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 克隆通过部门控制流程范发起权限
	 * @param comId 团队主键
	 * @param flowId 被克隆流程主键
	 * @param newFlowId 新流程主键
	 */
	public void cloneSpFlowScopeByUser(Integer comId, Integer flowId,
			Integer newFlowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowScopeByUser(comId,flowId,userId)");
		sql.append("\n select comId,?,userId from spFlowScopeByUser where comId=? and flowId=?");
		args.add(newFlowId);
		args.add(comId);
		args.add(flowId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取流程的所有步骤
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStep> listFlowAllSteps(Integer comId, Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from spFlowStep a");
		sql.append("\n where a.comid=? and a.flowid=? order by a.recordcreatetime");
		args.add(comId);
		args.add(flowId);
		return this.listQuery(sql.toString(), args.toArray(), SpFlowStep.class);
	}

	/**
	 * 克隆流程步骤
	 * @param comId 团队主键
	 * @param flowId 被克隆流程主键
	 * @param stepId 被克隆流程步骤主键
	 * @param newFlowId 新流程主键
	 */
	public void cloneSpFlowStep(Integer comId, Integer flowId,Integer stepId,Integer newFlowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowStep(comId,stepName,flowId,stepType,executorWay,conditionExp)");
		sql.append("\n select comId,stepName,?,stepType,executorWay,conditionExp from spFlowStep where comId=? and flowId=? and id=?");
		args.add(newFlowId);
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 克隆流程步骤审批人
	 * @param comId 团队主键
	 * @param flowId 被克隆流程主键
	 * @param stepId 被克隆流程步骤主键
	 * @param newFlowId 新流程主键
	 * @param newStepId 新流程步骤主键
	 */
	public void cloneSpFlowStepExecutor(Integer comId, Integer flowId,
			Integer stepId, Integer newFlowId,Integer newStepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spFlowStepExecutor(comId,flowId,stepId,executor)");
		sql.append("\n select comId,?,?,executor from spFlowStepExecutor where comId=? and flowId=? and stepId=?");
		args.add(newFlowId);
		args.add(newStepId);
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 克隆流程步骤权限控制
	 * @param comId 团队主键
	 * @param flowId 被克隆流程主键
	 * @param stepId 被克隆流程步骤主键
	 * @param newFlowId 新流程主键
	 * @param newStepId 新流程步骤主键
	 */
	public void cloneSpStepFormControl(Integer comId, Integer flowId,
			Integer stepId, Integer newFlowId,Integer newStepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spStepFormControl(comId,flowId,stepId,formControlKey,isFill)");
		sql.append("\n select comId,?,?,formControlKey,isFill from spStepFormControl where comId=? and flowId=? and stepId=?");
		args.add(newFlowId);
		args.add(newStepId);
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 克隆流程步骤条件描述
	 * @param comId 团队主键
	 * @param flowId 被克隆流程主键
	 * @param stepId 被克隆流程步骤主键
	 * @param newFlowId 新流程主键
	 * @param newStepId 新流程步骤主键
	 */
	public void cloneSpStepConditions(Integer comId, Integer flowId,
			Integer stepId, Integer newFlowId,Integer newStepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n insert into spStepConditions(comId,flowId,stepId,conditionVar,conditionType,conditionValue,conditionNum)");
		sql.append("\n select comId,?,?,conditionVar,conditionType,conditionValue,conditionNum from spStepConditions where comId=? and flowId=? and stepId=?");
		args.add(newFlowId);
		args.add(newStepId);
		args.add(comId);
		args.add(flowId);
		args.add(stepId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 获取步骤关系
	 * @param comId 团队主键
	 * @param flowId 克隆流程主键
	 * @param curStepId 当前流程步骤主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowStepRelation> listSpFlowStepRelation(Integer comId,
			Integer flowId, Integer curStepId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from spFlowStepRelation a");
		sql.append("\n where a.comid=? and a.flowid=? and a.curStepId=?");
		args.add(comId);
		args.add(flowId);
		args.add(curStepId);
		return this.listQuery(sql.toString(), args.toArray(), SpFlowStepRelation.class);
	}

	/**
	 * 取得本流程步骤信息
	 * @param comId 团队号
	 * @param curStepId 步骤主键
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	public SpFlowHiStep querySpFlowHiStep(Integer comId, Integer curStepId,Integer instanceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from spFlowHiStep where 1=1 ");
		this.addSqlWhere(instanceId, sql, args, " and busId=?");
		this.addSqlWhere(ConstantInterface.TYPE_FLOW_SP, sql, args, " and busType=?");
		this.addSqlWhere(curStepId, sql, args, " and stepId=?");
		this.addSqlWhere(comId, sql, args, " and comId=?");
		return (SpFlowHiStep) this.objectQuery(sql.toString(), args.toArray(), SpFlowHiStep.class);
	}

	/**
	 * 查询流程关联配置信息
	 * @param flowId 流程主键
	 * @param comId 团队主键
	 * @return
	 */
	public SpFlowModel querySpFlowModelRelevanceCfg(Integer flowId,
			Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.flowname,a.sonFlowId,c.flowname as sonFlowName from spFlowModel a ");
		sql.append("\n left join spFlowModel c on a.comid=c.comid and a.sonFlowId = c.id");
		sql.append("\n where a.comid = ? and a.id=?");
		args.add(comId);
		args.add(flowId);
		return (SpFlowModel) this.objectQuery(sql.toString(), args.toArray(), SpFlowModel.class);
	}

	/**
	 * 获取主流程表单控件集合
	 * @param comId 团队主键
	 * @param flowId 流程主键
	 * @param formModId 布局版本主键
	 * @param formLayoutId 表单布局主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormCompon> listFlowFormComponsAndChecked(Integer comId,
			Integer flowId, Integer formModId, Integer formLayoutId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select formC.comid,formC.flowId,formC.fieldid,formC.title,formC.componentkey,");
		sql.append("\n case when b.id is null then 0 else 1 end checked");
		sql.append("\n from (");
		sql.append("\n select * from(");
		sql.append("\n select a.comid,"+flowId+" as flowId,a.fieldid,a.title,a.componentkey,a.parentid,connect_by_isleaf as isleaf");
		sql.append("\n from formCompon a where a.comId=? and a.formModId=? and a.formLayoutId=?");
		args.add(comId);
		args.add(formModId);
		args.add(formLayoutId);
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR id = parentid");
		sql.append("\n order siblings by a.id");
		sql.append("\n )a where a.componentkey<>'Option' and a.componentkey<>'Paragraph' and a.parentid <> -1 and (a.componentkey='RadioBox' or a.isleaf=1)");
		sql.append("\n ) formC");
		sql.append("\n left join spFlowRelevanceCfg b on formC.comId=b.comid and formC.flowId = b.pflowid and formC.fieldid=b.fromformcontrolkey");
		sql.append("\n where formC.comid=? and formC.flowId=?");
		args.add(comId);
		args.add(flowId);
		return this.listQuery(sql.toString(), args.toArray(),FormCompon.class);
	}

	/**
	 * 获取已关联的关系
	 * @param flowId 流程主键
	 * @param comId 团队主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFlowRelevanceCfg> listSpFlowModelRelevanceCfg(Integer flowId,
			Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.fromformcontrolkey,a.toformcontrolkey,a.sonflowid");
		sql.append("\n from SpFlowRelevanceCfg a");
		sql.append("\n where a.comid=? and a.pflowid=?");
		args.add(comId);
		args.add(flowId);
		return this.listQuery(sql.toString(), args.toArray(),SpFlowRelevanceCfg.class);
	}
	
	/**
	 *  根据模块映射关系获取流程的配置详情
	 * @param userInfo 当前操作人信息
	 * @param busMapFlowId 模块映射关系主键
	 * @return
	 */
	public SpFlowModel querySpFlowModelByBusMapFlowId(
			UserInfo userInfo,Integer busMapFlowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.username as creatorName,b.gender,d.uuid,d.filename,e.modname as formName,");
		sql.append("\n f.typeName as spFlowTypeName,a.sonFlowId,sonFlow.flowname as sonFlowName,busMapFlow.Bustype");
		sql.append("\n from spFlowModel a");
		sql.append("\n inner join busMapFlow busMapFlow on a.comid = busMapFlow.Comid and a.id = busMapFlow.Flowid");
		sql.append("\n inner join userinfo b on a.creator = b.id");
		sql.append("\n inner join userOrganic c on b.id =c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on  c.smallheadportrait = d.id");
		sql.append("\n left join formMod e on a.comid=e.comid and a.formkey=e.id");
		sql.append("\n left join spFlowType f on  f.comId = a.comId and f.id=a.spFlowTypeId");
		sql.append("\n left join spFlowModel sonFlow on a.comid=sonFlow.comid and a.sonFlowId = sonFlow.id");
		sql.append("\n where a.comid=? and busMapFlow.id=?");
		args.add(userInfo.getComId());
		args.add(busMapFlowId);
		return (SpFlowModel)this.objectQuery(sql.toString(), args.toArray(), SpFlowModel.class);
	}
}
