package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.ModOptConfDao;

@Service
public class ModOptConfService {

	@Autowired
	ModOptConfDao modOptConfDao;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	MsgShareService msgShareService;
	
	
	
	/**
	 * 获取业务模块操作配置集合(用于页面展示)
	 * @param comId 企业主键
	 * @param moduleType 模块标识符
	 * @return
	 */
	public List<ModuleOperateConfig> listModOptConfig(Integer comId,String moduleType){
		return modOptConfDao.listModOptConfig(comId, moduleType);
	}
	
	/**
	 * 模块中心操作配置
	 * @param config
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateModOperateConfig(ModuleOperateConfig config,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			String moduleType = config.getModuleType();
			//先删除本模块原来记录
			modOptConfDao.delByField("moduleOperateConfig", new String[]{"comId","moduleType"}, new Object[]{userInfo.getComId(),moduleType});
			if(null!=config.getOpTypes()){
				//只记录启用权限
				for(String type:config.getOpTypes()){
					config.setComId(userInfo.getComId());
					config.setOperateType(type);
					config.setModuleType(config.getModuleType());
					config.setEnabled(1);
					modOptConfDao.add(config);
				}
			}
			String logContent = "";
			if(moduleType.equals(ConstantInterface.TYPE_CRM)){
				logContent = "更新了客户中心操作权限";
			}else if(moduleType.equals(ConstantInterface.TYPE_ITEM)){
				logContent = "更新了项目中心操作权限";
			}else if(moduleType.equals(ConstantInterface.TYPE_WEEK)){
				logContent = "更新了周报中心操作权限";
			}else if(moduleType.equals(ConstantInterface.TYPE_DAILY)){
				logContent = "更新了分享中心操作权限";
			}else if(moduleType.equals(ConstantInterface.TYPE_TASK)){
				logContent = "更新了任务中心操作权限";
			}else{
				logContent = "更新了"+moduleType+"操作权限";
			}
			//添加系统日志记录 
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),logContent,moduleType,
					userInfo.getComId(),userInfo.getOptIP());
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 获取业务模块操作配置集合（用于控制）
	 * @param comId 企业主键
	 * @param moduleType 模块标识符
	 * @return
	 */
	public List<ModuleOperateConfig> listModuleOperateConfig(Integer comId,String moduleType){
		return modOptConfDao.listModuleOperateConfig(comId, moduleType);
	}
	/**
	 * 判断操作权限（用于控制）
	 * @param comId 企业主键
	 * @param moduleType 模块标识符
	 * @return
	 */
	public ModuleOperateConfig getModuleOperateConfig(Integer comId,String moduleType,String optType){
		return modOptConfDao.getModuleOperateConfig(comId, moduleType,optType);
	}
	
}
