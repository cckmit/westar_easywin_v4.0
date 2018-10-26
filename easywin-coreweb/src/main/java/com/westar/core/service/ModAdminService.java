package com.westar.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.westar.base.model.ModAdmin;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.ModAdminDao;

@Service
public class ModAdminService {

	@Autowired
	ModAdminDao modAdminDao;
	
	@Autowired
	SystemLogService systemLogService;

	/**
	 * 取得模块的指定管理人员
	 * @param comId 团队号
	 * @param busType 业务类型
	 * @return
	 */
	public List<ModAdmin> listModAdmin(Integer comId, String busType) {
		return modAdminDao.listModAdmin(comId,busType);
	}

	/**
	 * 修改模块管理员
	 * @param sessionUser 当前操作人员
	 * @param modAdminStr 本次修改后的模块管理人员
	 * @param busType 业务类型
	 */
	public void updateModAdmin(UserInfo sessionUser, String modAdminStr,
			String busType) {
		//删除模块管理人员
		modAdminDao.delByField("modAdmin", new String[]{"comId","busType"}, new Object[]{sessionUser.getComId(),busType});
		
		//模块管理人员的名字
		String userNames = "";
		//将字符串转换成json数组
		List<ModAdmin> list = JSONArray.parseArray(modAdminStr, ModAdmin.class);
		if(null!=list && !list.isEmpty()){
			//遍历json数组
			for (ModAdmin modAdmin : list) {
				modAdmin.setComId(sessionUser.getComId());
				modAdmin.setBusType(busType);
				//添加管理人员
				modAdminDao.add(modAdmin);
				//保存的管理员名称
				userNames = userNames + "、"+ modAdmin.getAdminName();
			}
		}
		
				
				
		if(!"".equals(userNames)){
			userNames = userNames.substring(1,userNames.length());
		}
		String log = "";
		if(busType.equals(ConstantInterface.TYPE_BGYP)){
			log = "办公用品管理人员变更为:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_BGYP_BUY)){
			log = "办公用品采购人员变更为:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_RSDA)){
			log = "人事档案管理人员变更为:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_CLGL)){
			log = "车辆管理人员变更为:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_GDZC)){
			log = "固定资产管理人员变更为:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_FINALCIAL_OFFICE)){
			log = "财务办公人员变更为:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_CHANGE_EXAM)){
			log = "客户属性变更审批人员变更为:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_NEED_EXAM_USER)){
			log = "客户属性变更审批范围变更为:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_INSTITUTION)){
			log = "制度管理管理人员变更为:\""+userNames +"\"";
		}else{
			log = busType+"管理人员变更为:\""+userNames +"\"";
		}
		//添加系统日志记录 
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), log,busType,
				sessionUser.getComId(),sessionUser.getOptIP());
		
	}

	/**
	 * 删除模块管理人员
	 * @param sessionUser
	 * @param modAdmin
	 */
	public void delModAdmin(UserInfo sessionUser, ModAdmin modAdmin) {
		String busType = modAdmin.getBusType();
		String userNames = modAdmin.getAdminName();
		//删除模块管理人员
		modAdminDao.delById(ModAdmin.class, modAdmin.getId());
		String log = "";
		if(busType.equals(ConstantInterface.TYPE_BGYP)){
			log = "删除办公用品管理人员:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_BGYP_BUY)){
			log = "删除办公用品采购人员:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_RSDA)){
			log = "删除人事档案管理人员:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_CLGL)){
			log = "删除车辆管理人员:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_GDZC)){
			log = "删除固定资产管理人员:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_FINALCIAL_OFFICE)){
			log = "删除财务办公人员:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_CHANGE_EXAM)){
			log = "删除客户属性变更审批人员:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_NEED_EXAM_USER)){
			log = "删除客户属性变更审批范围人员:\""+userNames +"\"";
		}else if(busType.equals(ConstantInterface.TYPE_INSTITUTION)){
			log = "删除制度管理管理人员:\""+userNames +"\"";
		}else{
			log = "删除"+busType+"管理人员:\""+userNames +"\"";
		}
		//添加系统日志记录 
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(),log,busType,
				sessionUser.getComId(),sessionUser.getOptIP());
	}
	/**
	 * 验证人员当前是否为模块管理人员
	 * @param sessionUser 当前操作人员
	 * @param busType 模块类型
	 * @return
	 */
	public boolean authCheck(UserInfo sessionUser,String busType){
		Integer modAdminCount = modAdminDao.countModAdmin(sessionUser,busType);
		return modAdminCount >0 ;
		
	}
	/**
	 * 验证人员当前是否为模块管理人员
	 * @param sessionUser 当前操作人员
	 * @param busType 模块类型
	 * @return
	 */
	public Map<String,Boolean> authCheck(UserInfo sessionUser,String[] busTypes){
		Map<String,Boolean> map = new HashMap<String, Boolean>();
		if(null !=busTypes){
			for (String busType : busTypes) {
				boolean modAdminFlag = this.authCheck(sessionUser,busType);
				map.put(busType, modAdminFlag);
			}
		}
		return map ;
		
	}

	/**
	 * 查询模块管理员的消息推送信息
	 * @param comId 组织号
	 * @param busType 业务类型 
	 * @return
	 */
	public List<UserInfo> listBusTypeModAdmin(Integer comId, String busType) {
		return modAdminDao.listBusTypeModAdmin(comId,busType);
	}
}
