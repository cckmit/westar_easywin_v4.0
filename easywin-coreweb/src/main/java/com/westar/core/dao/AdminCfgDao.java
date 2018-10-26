package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.BusAttrMapFormCol;
import com.westar.base.model.BusMapFlow;
import com.westar.base.model.BusMapFlowAuthDep;
import com.westar.base.model.FeeBudget;
import com.westar.base.model.FormColMapBusAttr;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;

@Repository
public class AdminCfgDao extends BaseDao {

	/**
	 * 获取模块操作与审批流程之间关联集合列表
	 * @param comId 团队主键
	 * @param busType 关联业务分类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusMapFlow> listBusMapFlow(Integer comId, String busType,BusMapFlow busMapFlow) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.recordCreateTime,a.flowid,a.bustype,a.description,b.flowname");
		sql.append("\n from busMapFlow a");
		sql.append("\n inner join spFlowModel b on a.comid=b.comid and a.flowid=b.id");
		sql.append("\n where a.comid=? and a.bustype=? and b.status="+ConstantInterface.COMMON_YES);
		args.add(comId);
		args.add(busType);
		//查询创建时间段
		this.addSqlWhere(busMapFlow.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(busMapFlow.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		this.addSqlWhereLike(busMapFlow.getDescription(), sql, args, " and a.description like ?");//流程名称筛选
		String orderBy ="";//列表排序
		if(CommonUtil.isNull(busMapFlow.getOrderBy())){
			orderBy =" a.recordcreatetime desc";
		}else if("crTimeNewest".equals(busMapFlow.getOrderBy())){
			orderBy =" a.recordCreateTime desc";
		}else if("crTimeOldest".equals(busMapFlow.getOrderBy())){
			orderBy =" a.recordCreateTime asc";
		}
		return this.pagedQuery(sql.toString(), orderBy, args.toArray(),BusMapFlow.class);
	}
	
	/**
	 * 获取模块与审批表单映射记录
	 * @param comId 团队主键
	 * @param bustype 模块标识主键
	 * @param busMapFlowId 关联主键
	 * @return
	 */
	public BusMapFlow queryBusMapFlow(Integer comId,
			String bustype,Integer busMapFlowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.bustype,a.flowid,b.flowname,a.description from busMapFlow a ");
		sql.append("\n inner join spflowmodel b on a.comid = b.comid and a.flowid = b.id");
		sql.append("\n where a.comid=? and a.bustype=? and b.status=1 and a.id=?");
		args.add(comId);
		args.add(bustype);
		args.add(CommonUtil.isNull(busMapFlowId)?-1:busMapFlowId);
		sql.append("  and rownum=1");
		sql.append("\n order by a.recordcreatetime desc");
		return (BusMapFlow)this.objectQuery(sql.toString(), args.toArray(),BusMapFlow.class);
	}

	/**
	 * 获取模块与审批表单映射记录
	 * @param comId 团队主键
	 * @param flowId 关联主键
	 * @return
	 */
	public BusMapFlow queryBusMapFlowByFlowId(Integer comId,Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.bustype,a.flowid,b.flowname,a.description from busMapFlow a ");
		sql.append("\n inner join spflowmodel b on a.comid = b.comid and a.flowid = b.id");
		sql.append("\n where a.comid=? and  b.status=1 and a.flowId = ?");
		args.add(comId);
		args.add(CommonUtil.isNull(flowId)?-1:flowId);
		sql.append("  and rownum=1");
		sql.append("\n order by a.recordcreatetime desc");
		return (BusMapFlow)this.objectQuery(sql.toString(), args.toArray(),BusMapFlow.class);
	}

	/**
	 * 获取映射使用权限范围集合
	 * @param comId
	 * @param actionId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusMapFlowAuthDep> listBusMapSocpe(Integer comId, Integer actionId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comid,a.busMapFlowId,a.depid,b.depname from BusMapFlowAuthDep a");
		sql.append("\n inner join department b on a.comid=b.comid and a.depid=b.id");
		sql.append("\n where a.comid=? and a.busMapFlowId=?");
		args.add(comId);
		args.add(actionId);
		return this.listQuery(sql.toString(), args.toArray(), BusMapFlowAuthDep.class);
	}
	
	/**
	 * 获取模块与审批表单映射关系
	 * @param comId 团队主键
	 * @param busType 模块标识主键
	 * @param busMapFlowId 关联主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusAttrMapFormCol> listbusAttrMapFormCol(
			Integer comId, String busType,Integer busMapFlowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.formCol,b.busAttr from BusMapFlow a");
		sql.append("\n inner join BusAttrMapFormCol b on a.comid=b.comid ");
		sql.append("\n and a.id=b.busMapFlowId and a.bustype=b.bustype and a.comId=? and a.bustype=? and a.id=?");
		args.add(comId);
		args.add(busType);
		args.add(busMapFlowId);
		return this.listQuery(sql.toString(), args.toArray(),BusAttrMapFormCol.class);
	}
	
	/**
	 * 获取业务数据与表单数据映射关系
	 * @param comId
	 * @param busMapFlowId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormColMapBusAttr> listFormColMapBusAttr(
			Integer comId,Integer busMapFlowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.formCol,b.busAttr from BusMapFlow a");
		sql.append("\n inner join formColMapBusAttr b on a.comid=b.comid ");
		sql.append("\n and a.id=b.busMapFlowId and a.comId=? and a.id=?");
		args.add(comId);
		args.add(busMapFlowId);
		return this.listQuery(sql.toString(), args.toArray(),FormColMapBusAttr.class);
	}


	/**
	 * 查询授权模块业务权限
	 * @param curUser 当前操作人
	 * @param bustype 关联动作类型
	 * @return
	 */
	public BusMapFlow queryBusMapFlowByAuth(UserInfo curUser, String bustype) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select allData.id,allData.flowid from (");
		sql.append("\n select a.id,a.flowid from busMapFlow a");
		sql.append("\n inner join spflowmodel b on a.comid=b.comid and a.flowid=b.id");
		sql.append("\n left join busMapFlowAuthDep c on a.comid=c.comid and a.id=c.busmapflowid");
		sql.append("\n where a.comid=? and a.bustype=? and b.status=?");
		args.add(curUser.getComId());
		args.add(bustype);
		args.add(ConstantInterface.COMMON_YES);
		sql.append("\n and(");
		sql.append("\n c.id is null");
		sql.append("\n or");
		sql.append("\n exists(");
		sql.append("\n select dep.id,dep.depname,dep.parentid from department dep");
		sql.append("\n where dep.comid=? and dep.id=?");
		args.add(curUser.getComId());
		args.add(curUser.getDepId());
		sql.append("\n start with dep.id=c.depid");
		sql.append("\n connect by prior dep.id = dep.parentid");
		sql.append("\n )");
		sql.append("\n )");
		sql.append("\n order by a.recordcreatetime desc");
		sql.append("\n ) allData where rownum=1");
		return (BusMapFlow)this.objectQuery(sql.toString(), args.toArray(),BusMapFlow.class);
	}
	/**
	 * 查询授权模块业务权限
	 * @param curUser 当前操作人
	 * @param bustype 关联动作类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusMapFlow> listBusMapFlowByAuth(UserInfo curUser, String bustype) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select allData.id,allData.flowid,allData.description,allData.busType from (");
		sql.append("\n select a.id,a.flowid,a.description,a.busType from busMapFlow a");
		sql.append("\n inner join spflowmodel b on a.comid=b.comid and a.flowid=b.id");
		sql.append("\n left join busMapFlowAuthDep c on a.comid=c.comid and a.id=c.busmapflowid");
		sql.append("\n where a.comid=? and a.bustype=? and b.status=?");
		args.add(curUser.getComId());
		args.add(bustype);
		args.add(ConstantInterface.COMMON_YES);
		sql.append("\n and(");
		sql.append("\n c.id is null");
		sql.append("\n or");
		sql.append("\n exists(");
		sql.append("\n select dep.id,dep.depname,dep.parentid from department dep");
		sql.append("\n where dep.comid=? and dep.id=?");
		args.add(curUser.getComId());
		args.add(curUser.getDepId());
		sql.append("\n start with dep.id=c.depid");
		sql.append("\n connect by prior dep.id = dep.parentid");
		sql.append("\n )");
		sql.append("\n )");
		sql.append("\n order by a.recordcreatetime desc");
		sql.append("\n ) allData where 1=1");
		return this.listQuery(sql.toString(), args.toArray(),BusMapFlow.class);
	}

	/**
	 * 根据依据主键，查询关联业务模块类型
	 * @param curUser
	 * @param feeBudgetId
	 * @return
	 */
	public FeeBudget queryRelateBusTypeWithFeeBudgetId(UserInfo curUser,Integer feeBudgetId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.bustype from feeBudget a ");
		sql.append("\n inner join feeBusMod b on a.id=b.feebudgetid and a.comid=b.comid");
		sql.append("\n where a.id=? and a.comid=? and rownum=1");
		args.add(feeBudgetId);
		args.add(curUser.getComId());
		return (FeeBudget)this.objectQuery(sql.toString(), args.toArray(),FeeBudget.class);
	}
	
	/**
	 * 获取业务数据与表单数据映射关系集合
	 * @param comId
	 * @param busType
	 * @param busMapFlowId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormColMapBusAttr> listFormColMapBusAttr(Integer comId, String busType, Integer busMapFlowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.busattr,a.formcol,f.componentkey ");
		sql.append("\n from formColMapBusAttr a");
		sql.append("\n inner join busMapFlow b on a.busmapflowid=b.id and a.comid=b.comid");
		sql.append("\n inner join spflowmodel c on b.comid=c.comid and b.flowid=c.id");
		sql.append("\n inner join (");
		sql.append("\n 	SELECT A.formmodid,a.id,");
		sql.append("\n 		ROW_NUMBER() OVER(PARTITION BY A.FORMMODID ORDER BY a.version DESC) RN");
		sql.append("\n 	 FROM  formlayout A");
		sql.append("\n )d on c.formkey=d.formmodid and  d.RN = 1");
		sql.append("\n inner join formcompon f on d.id=f.formlayoutid and a.formcol=f.fieldid");
		sql.append("\n where b.id=? and b.bustype=? and b.comid=? and c.status=?");
		args.add(busMapFlowId);
		args.add(busType);
		args.add(comId);
		args.add(ConstantInterface.COMMON_YES);
		return this.listQuery(sql.toString(), args.toArray(),FormColMapBusAttr.class);
	}

}
