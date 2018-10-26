package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.BusAttrMapFormColTemp;
import com.westar.core.dao.FormColTempDao;
@Service
public class FormColTempService {
	@Autowired
	FormColTempDao formColTempDao;
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
		 formColTempDao.addBusAttrMapFormColTemp(busMapFlowId, instanceId, comId, busId, busType);
	}
	/**
	 * 根据流程实例主键获取业务、审批表关联关系
	 * @param comId
	 * @param instanceId
	 * @return
	 */
	public List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp(Integer comId,
			Integer instanceId) {
		return formColTempDao.listBusAttrMapFormColTemp(comId, instanceId);
	}
}
