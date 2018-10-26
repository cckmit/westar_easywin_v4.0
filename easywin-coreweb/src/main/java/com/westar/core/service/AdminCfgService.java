package com.westar.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.BusAttrMapFormCol;
import com.westar.base.model.BusMapFlow;
import com.westar.base.model.BusMapFlowAuthDep;
import com.westar.base.model.FormColMapBusAttr;
import com.westar.base.model.FormCompon;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.KeyValue;
import com.westar.base.util.BusMapFlowUtil;
import com.westar.base.util.CommonUtil;
import com.westar.core.dao.AdminCfgDao;

@Service
public class AdminCfgService {
	
	@Autowired
	FinancialService financialService;
	@Autowired
	AdminCfgDao adminCfgDao;
	@Autowired
	FlowDesignService flowDesignService;
	

	/**
	 * 获取模块操作与审批流程之间关联集合列表
	 * @param comId 团队主键
	 * @param busType 关联业务分类
	 * @return
	 */
	public List<BusMapFlow> listBusMapFlow(Integer comId, String busType,BusMapFlow busMapFlow) {
		return adminCfgDao.listBusMapFlow(comId,busType,busMapFlow);
	}

	/**
	 * 公文操作与审批流程关联
	 * @param userInfo 当前操作人信息
	 * @param busMapFlow 配置描述
	 */
	public Integer initBusMapFlow(UserInfo userInfo,BusMapFlow busMapFlow) {
		Integer actionId = null;
		busMapFlow.setComId(userInfo.getComId());
		if(null!=busMapFlow.getId() && busMapFlow.getId()>0){
			adminCfgDao.update(busMapFlow);
			actionId = busMapFlow.getId();//更新
		}else{
			actionId = adminCfgDao.add(busMapFlow);//新增
		}
		//设置使用范围
		adminCfgDao.delByField("busMapFlowAuthDep", new String[]{"comId","busMapFlowId"}, 
				new Object[]{userInfo.getComId(),actionId});
		if(null!=busMapFlow.getListBusMapFlowAuthDep() && !busMapFlow.getListBusMapFlowAuthDep().isEmpty()){
			
			for (BusMapFlowAuthDep scope : busMapFlow.getListBusMapFlowAuthDep()) {
				scope.setComId(userInfo.getComId());
				scope.setBusMapFlowId(actionId);
				adminCfgDao.add(scope);
			}
		}
		//保存映射关系
		this.initbusAttrMapFormCol(busMapFlow, userInfo, busMapFlow.getBusType());
		return actionId;
	}

	/**
	 * 查询公文收发问关联审批流程信息
	 * @param comId 单位主键
	 * @param bustype 关联动作类型
	 * @param actionId 关联主键
	 * @return
	 */
	public BusMapFlow queryBusMapFlow(Integer comId,String bustype,Integer actionId) {
		BusMapFlow busMapFlow = adminCfgDao.queryBusMapFlow(comId,bustype,actionId);
		if(null!=busMapFlow){
			//获取控制范围
			List<BusMapFlowAuthDep> listBusMapFlowAuthDep = adminCfgDao.listBusMapSocpe(comId,busMapFlow.getId());
			busMapFlow.setListBusMapFlowAuthDep(listBusMapFlowAuthDep);
		}
		return busMapFlow;
	}

	/**
	 * 查询公文收发问关联审批流程信息根据flowid
	 * @param comId 单位主键
	 * @param flowId 关联主键
	 * @return
	 */
	public BusMapFlow queryBusMapFlowByFlowId(Integer comId,Integer flowId) {
		BusMapFlow busMapFlow = adminCfgDao.queryBusMapFlowByFlowId(comId,flowId);
		if(null!=busMapFlow){
			//获取控制范围
			List<BusMapFlowAuthDep> listBusMapFlowAuthDep = adminCfgDao.listBusMapSocpe(comId,busMapFlow.getId());
			busMapFlow.setListBusMapFlowAuthDep(listBusMapFlowAuthDep);
		}
		return busMapFlow;
	}

	/**
	 * 获取模块与审批表单映射关系
	 * @param userInfo 当前操作人信息
	 * @param busType 模块标识主键
	 * @param actionId 关联主键
	 * @return
	 */
	public List<KeyValue> listKeyMap(UserInfo userInfo,String busType,Integer actionId) {
		
		//取得模块所需要的数据
		List<KeyValue> listAttr = BusMapFlowUtil.listAttr(busType);
		
		List<KeyValue> newList = new ArrayList<KeyValue>();
		if(null!=listAttr && !listAttr.isEmpty()){
			for(KeyValue mapKey : listAttr){  
				try {
					newList.add(mapKey.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}  
			} 
		}
		List<BusAttrMapFormCol> listbusAttrMapFormCol = adminCfgDao.listbusAttrMapFormCol(userInfo.getComId(),busType,actionId);
		if(null!=listbusAttrMapFormCol && !listbusAttrMapFormCol.isEmpty()){
			for (BusAttrMapFormCol busAttrMapFormCol : listbusAttrMapFormCol) {
				for (KeyValue mapKey : newList) {
					if(busAttrMapFormCol.getBusAttr().equals(mapKey.getKey())){
						mapKey.setFormControlKey(busAttrMapFormCol.getFormCol());
					}
				}
			}
		}
		return newList;
	}
/**
 * 取得费用管理中属性映射关系集合
 * @param userInfo
 * @param busType
 * @param actionId
 * @return
 */
public List<KeyValue> listFormColMapBusAttr(UserInfo userInfo,String busType,Integer actionId) {
		
		//取得模块所需要的数据
		List<KeyValue> listAttr = BusMapFlowUtil.listAttrOfFeeBudget();
		
		List<KeyValue> newList = new ArrayList<KeyValue>();
		if(null!=listAttr && !listAttr.isEmpty()){
			for(KeyValue mapKey : listAttr){  
				try {
					newList.add(mapKey.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}  
			} 
		}
		List<FormColMapBusAttr> listFormColMapBusAttr = adminCfgDao.listFormColMapBusAttr(userInfo.getComId(),actionId);
		if(null!=listFormColMapBusAttr && !listFormColMapBusAttr.isEmpty()){
			for (FormColMapBusAttr formColMapBusAttr : listFormColMapBusAttr) {
				for (KeyValue mapKey : newList) {
					if(formColMapBusAttr.getBusAttr().equals(mapKey.getKey())){
						mapKey.setFormControlKey(formColMapBusAttr.getFormCol());
					}
				}
			}
		}
		return newList;
	}

	/**
	 * 获取关联发文公文的审批流程表单控件集合
	 * @param userInfo 当前操作人
	 * @param busType 关联类型
	 * @param actionId 关联主键
	 * @return
	 */
	public List<FormCompon> listFlowFormCompons(UserInfo userInfo,String busType,Integer actionId) {
		BusMapFlow busMapFlow = adminCfgDao.queryBusMapFlow(userInfo.getComId(),busType,actionId);
		List<FormCompon> listFlowFormCompons = null;
		if(null!=busMapFlow){
			listFlowFormCompons = flowDesignService.listFlowFormComponsByBusMapFlowId(busMapFlow.getFlowId(),userInfo,actionId);
		}
		return listFlowFormCompons;
	}
	
	/**
	 * 清除公文操作与审批流程关联
	 * @param userInfo 当前操作人信息
	 * @param busType 关联类型
	 * @param actionId 关联主键
	 */
	public void delBusMapFlow(UserInfo userInfo, String busType,Integer actionId) {
		//出差业务配置信息、借款业务配置信息、报销业务配置信息
		adminCfgDao.delByField("busMapFlowAuthDep", new String[]{"comId","busMapFlowId"}, new Object[]{userInfo.getComId(),actionId});
		adminCfgDao.delByField("busAttrMapFormCol", new String[]{"comId","busType","busMapFlowId"}, new Object[]{userInfo.getComId(),busType,actionId});
		adminCfgDao.delByField("busMapFlow", new String[]{"comId","busType","id"}, new Object[]{userInfo.getComId(),busType,actionId});
	}
	
	/**
	 * 清除范围限制
	 * @param busMapFlow 映射关系对象
	 * @param userInfo 当前操作人
	 */
	public void delBusMapFlowAuthDep(BusMapFlow busMapFlow,UserInfo userInfo) {
		adminCfgDao.delByField("busMapFlowAuthDep", new String[]{"comId","busMapFlowId"},
				new Object[]{userInfo.getComId(),busMapFlow.getId()});
	}
	
	/**
	 * 初始化表单数据与业务数据映射关系
	 * @param busMapFlow 表单数据与业务数据映射关系
	 * @param userInfo 当前操作人
	 * @param busType 关联类型
	 */
	public void initbusAttrMapFormCol(
			BusMapFlow busMapFlow,UserInfo userInfo,String busType) {
		//删除原有的关联信息
		adminCfgDao.delByField("busAttrMapFormCol", new String[]{"comId","busType","busMapFlowId"}, new Object[]{userInfo.getComId(),busType,busMapFlow.getId()});
		
		String[] keyValueArray = busMapFlow.getKeyValueArray();
		if(null!=keyValueArray && keyValueArray.length>0){
			BusAttrMapFormCol busAttrMapFormCol = null;
			for (String keyValue : keyValueArray) {
				if(keyValue.contains("&")){
					busAttrMapFormCol = new BusAttrMapFormCol();
					busAttrMapFormCol.setComId(userInfo.getComId());
					busAttrMapFormCol.setBusMapFlowId(busMapFlow.getId());
					busAttrMapFormCol.setBusType(busType);
					busAttrMapFormCol.setFormCol(keyValue.split("&")[0]);
					busAttrMapFormCol.setBusAttr(keyValue.split("&")[1]);
					busAttrMapFormCol.setIsRequire(keyValue.split("&")[2]);
					adminCfgDao.add(busAttrMapFormCol);
				}
			}
		}
		
		//删除原有的业务数据与表单数据映射关系
		adminCfgDao.delByField("formColMapBusAttr", new String[]{"comId","busMapFlowId"}, new Object[]{userInfo.getComId(),busMapFlow.getId()});
		
		String[] formColMapBusAttrs = busMapFlow.getFormColMapBusAttrs();
		if(null!=formColMapBusAttrs && formColMapBusAttrs.length>0){
			FormColMapBusAttr formColMapBusAttr = null;
			for (String mapKey : formColMapBusAttrs) {
				if(mapKey.contains("&")){
					formColMapBusAttr = new FormColMapBusAttr();
					formColMapBusAttr.setComId(userInfo.getComId());
					formColMapBusAttr.setBusMapFlowId(busMapFlow.getId());
					formColMapBusAttr.setFormCol(mapKey.split("&")[0]);
					formColMapBusAttr.setBusAttr(mapKey.split("&")[1]);
					adminCfgDao.add(formColMapBusAttr);
				}
			}
		}
	}
	
	/**
	 * 初始化表单数据与业务数据映射关系(单个更新)
	 * @param busAttrMapFormCol
	 * @param userInfo
	 */
	public void initbusAttrMapFormCol(BusAttrMapFormCol busAttrMapFormCol,
			UserInfo userInfo) {
		Integer busMapFlowId = busAttrMapFormCol.getBusMapFlowId();
		String formCol = busAttrMapFormCol.getFormCol();
		String busAttr = busAttrMapFormCol.getBusAttr();
		String busType = busAttrMapFormCol.getBusType();
		//删除原有的关联信息
		adminCfgDao.delByField("busAttrMapFormCol", new String[]{"comId","busType","busMapFlowId","busAttr"}, 
				new Object[]{userInfo.getComId(),busType,busMapFlowId,busAttr});
		if(!CommonUtil.isNull(formCol) && !CommonUtil.isNull(busAttr)) {
			busAttrMapFormCol.setComId(userInfo.getComId());
			adminCfgDao.add(busAttrMapFormCol);
		}
	}

	/**
	 * 批量删除配置
	 * @param userInfo 当前操作人
	 * @param ids 主键数组
	 */
	public void delBusMapFlowByBatch(UserInfo userInfo, Integer[] ids) {
		if(!CommonUtil.isNull(ids)){
			for (Integer id : ids) {
				//出差业务配置信息、借款业务配置信息、报销业务配置信息
				adminCfgDao.delByField("busMapFlowAuthDep", new String[]{"comId","busMapFlowId"}, new Object[]{userInfo.getComId(),id});
				adminCfgDao.delByField("busAttrMapFormCol", new String[]{"comId","busMapFlowId"}, new Object[]{userInfo.getComId(),id});
				adminCfgDao.delByField("busMapFlow", new String[]{"comId","id"}, new Object[]{userInfo.getComId(),id});
			}
		}
		
	}

	/**
	 * 查询授权模块业务权限
	 * @param curUser 当前操作人
	 * @param bustype 关联动作类型
	 * @return
	 */
	public BusMapFlow queryBusMapFlowByAuth(UserInfo curUser, String bustype) {
		return adminCfgDao.queryBusMapFlowByAuth(curUser,bustype);
	}
	/**
	 * 查询授权模块业务权限
	 * @param curUser 当前操作人
	 * @param bustype 关联动作类型
	 * @return
	 */
	public List<BusMapFlow> listBusMapFlowByAuth(UserInfo curUser, String bustype) {
		return adminCfgDao.listBusMapFlowByAuth(curUser,bustype);
	}
	/**
	 * 查询授权模块业务权限
	 * @param curUser 当前操作人
	 * @param bustype 关联动作类型()
	 * @param feeBudgetId 依据主键（费用管理）
	 * @return
	 */
	public List<BusMapFlow> listBusMapFlowByAuth(UserInfo curUser, String bustype,Integer feeBudgetId) {
		//FeeBudget feeBudget = adminCfgDao.queryRelateBusTypeWithFeeBudgetId(curUser,feeBudgetId);
		return adminCfgDao.listBusMapFlowByAuth(curUser,bustype);
	}

	/**
	 * 获取业务数据与表单数据映射关系集合
	 * @param comId
	 * @param busType
	 * @param busMapFlowId
	 * @return
	 */
	public List<FormColMapBusAttr> listFormColMapBusAttr(Integer comId, String busType, Integer busMapFlowId) {
		return adminCfgDao.listFormColMapBusAttr(comId,busType,busMapFlowId);
	}

}
