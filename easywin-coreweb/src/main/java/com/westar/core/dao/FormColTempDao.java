package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.BusAttrMapFormColTemp;

@Repository
public class FormColTempDao extends BaseDao{
	/**
	 * 创建临时流程实例与模块关联关系业务数据映射关系临时表
	 * @param busMapFlowId 模块操作与审批流程之间关联主键
	 * @param instanceId 流程实例主键
	 * @param comId 团队主键
	 * @param busId 关联业务主键
	 * @param busType 关联业务类型
	 */
	public void addBusAttrMapFormColTemp(Integer busMapFlowId,Integer instanceId,
			Integer comId,Integer busId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into busAttrMapFormColTemp(comId,instanceId,formCol,busAttr,busId,busType,isRequire) \n");
		sql.append("select comId,?,formCol,busAttr,?,?,isRequire from busAttrMapFormCol where comId=? and busMapFlowId=? \n");
		args.add(instanceId);
		args.add(busId);
		args.add(busType);
		args.add(comId);
		args.add(busMapFlowId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 根据流程实例主键获取业务、审批表关联关系
	 * @param comId
	 * @param instanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp(Integer comId,
			Integer instanceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.busid,a.bustype,a.formcol,a.busattr ");
		sql.append("\n from busAttrMapFormColTemp a");
		sql.append("\n where a.comid=? and a.instanceid=?");
		args.add(comId);
		args.add(instanceId);
		return this.listQuery(sql.toString(), args.toArray(),BusAttrMapFormColTemp.class);
	}
}
