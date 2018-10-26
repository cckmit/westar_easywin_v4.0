package com.westar.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.BusRemindUser;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Service
public class BusModService {

	@Autowired
	TaskService taskService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	CrmService crmService;
	
	@Autowired
	DemandService demandService;

	@Autowired
	FinancialService financialService;
	
	@Autowired
	ItemService itemService;

	/**
	 * 查询催办参数信息
	 * @param userInfo 当前操作人员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	public Map<String, Object> queryReminderConf(UserInfo userInfo,
			Integer busId, String busType) {
		Map<String,Object> map =new HashMap<String,Object>();
		if(busType.equals(ConstantInterface.TYPE_TASK)){
			//任务催办
			return taskService.queryRemindConf(userInfo,busId);
		}else if(busType.equals(ConstantInterface.TYPE_FLOW_SP)){
			//审批催办
			return workFlowService.queryRemindConf(userInfo,busId,ConstantInterface.TYPE_FLOW_SP);
		}else if(busType.equals(ConstantInterface.TYPE_CRM)){
			//客户催办
			return crmService.queryRemindConf(userInfo,busId);
		}else if(busType.equals(ConstantInterface.TYPE_ITEM)){
			//项目催办
			return itemService.queryRemindConf(userInfo,busId);
		}else if(busType.equals(ConstantInterface.TYPE_DEMAND_PROCESS)){
			//需求催办
			return demandService.queryRemindConf(userInfo,busId);
		}else{
			map.put("status", "f");
			map.put("info", busType+"__类型不支持！");
		}
		return map;
	}
	/**
	 * 查询催办参数信息
	 * @param userInfo 当前操作人员
	 * @param busType 业务类型
	 * @return
	 */
	public List<BusRemindUser> queryRemindersConf(UserInfo userInfo,Integer busId, String busType) {
		List<BusRemindUser> list = null;
		if(busType.equals(ConstantInterface.TYPE_FLOW_SP)){//审批催办
			return workFlowService.queryRemindsConf(userInfo,busId,ConstantInterface.TYPE_FLOW_SP);
		}
		return list;
	}
}
