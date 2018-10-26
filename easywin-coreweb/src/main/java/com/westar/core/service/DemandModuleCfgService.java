package com.westar.core.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.westar.base.model.DemandHandleStepCfg;
import com.westar.base.model.DemandModuleCfg;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.DemandModuleCfgDao;

/**
 * 
 * 描述:需求模板配置的业务逻辑层
 * @author zzq
 * @date 2018年10月23日 下午1:49:23
 */
@Service
public class DemandModuleCfgService {

	@Autowired
	DemandModuleCfgDao demandModuleCfgDao;
	
	@Autowired
	SystemLogService systemLogService;
	

	/**
	 * 分页查询需求模板信息
	 * @param sessionUser 当前操作人员
	 * @param demandModuleCfg 需求的查询条件
	 * @return
	 */
	public PageBean<DemandModuleCfg> listPagedDemandModuleCfg(
			UserInfo sessionUser, DemandModuleCfg demandModuleCfg) {
		
		return demandModuleCfgDao.listPagedDemandModuleCfg(sessionUser,demandModuleCfg);
	}

	/**
	 * 查询需求模板
	 * @param sessionUser 当前操作人员
	 * @param demandModuleCfg 查询条件
	 * @return
	 */
	public List<DemandModuleCfg> listDemandModuleCfg(UserInfo sessionUser,
			DemandModuleCfg demandModuleCfg) {
		List<DemandModuleCfg> list = demandModuleCfgDao.listDemandModuleCfg(sessionUser, demandModuleCfg);
		if(null != list && !list.isEmpty()){
			for (DemandModuleCfg moduleCfg : list) {
				List<DemandHandleStepCfg> listDemandHandleStepCfg 
					= demandModuleCfgDao.listDemandHandleStepCfg(sessionUser.getComId(),moduleCfg.getId());
				moduleCfg.setListDemandHandleStepCfg(listDemandHandleStepCfg);
			}
		}
		return list;
	}
	
	/**
	 * 验证名称的重复
	 * @param sessionUser
	 * @param modName
	 * @param demandModuleCfgId
	 * @return
	 */
	public boolean checkDemandModuleCfgByName(UserInfo sessionUser,
			String modName,Integer demandModuleCfgId){
		DemandModuleCfg demandModuleCfg = demandModuleCfgDao.queryDemandModuleCfgByName(
				sessionUser.getComId(), modName);
		if( null == demandModuleCfg){
			return true;
		}
		if(demandModuleCfg.getId().equals(demandModuleCfgId)){
			return true;
		}
		return false;
	}

	/**
	 * 添加需求模板
	 * @param sessionUser
	 * @param demandModuleCfg
	 */
	public void addDemandModuleCfg(UserInfo sessionUser,
			DemandModuleCfg demandModuleCfg) {
		if(true){
			return;
		}
		
		//设置更新时间
		String modifyTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		
		//基础属性赋值
		demandModuleCfg.setComId(sessionUser.getComId());
		demandModuleCfg.setUserId(sessionUser.getId());
		demandModuleCfg.setModifyUserId(sessionUser.getId());
		demandModuleCfg.setModifyTime(modifyTime);
		//添加需求模板
		Integer demandModuleCfgId = demandModuleCfgDao.add(demandModuleCfg);
		
		//需求处理步骤
		List<DemandHandleStepCfg> listDemandHandleStepCfg = demandModuleCfg.getListDemandHandleStepCfg();
		if(null!=listDemandHandleStepCfg && !listDemandHandleStepCfg.isEmpty()){
			for (DemandHandleStepCfg demandHandleStepCfg : listDemandHandleStepCfg) {
				demandHandleStepCfg.setComId(sessionUser.getComId());
				demandHandleStepCfg.setDemandModuleCfgId(demandModuleCfgId);
				demandModuleCfgDao.add(demandHandleStepCfg);
			}
		}
		
		//添加系统日志
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName()
					, "添加需求处理模板："+demandModuleCfg.getModName(), ConstantInterface.TYPE_ORG
					, sessionUser.getComId(), sessionUser.getOptIP());
	}

	/**
	 * 查询单个的需求处理信息
	 * @param sessionUser 当前操作人员
	 * @param demandModuleCfgId 需求处理模板主键
	 * @return
	 */
	public DemandModuleCfg queryDemandModuleCfg(UserInfo sessionUser,
			Integer demandModuleCfgId) {
		DemandModuleCfg demandModuleCfg = (com.westar.base.model.DemandModuleCfg) demandModuleCfgDao.objectQuery(DemandModuleCfg.class, demandModuleCfgId);
		if(null != demandModuleCfg){
			List<DemandHandleStepCfg> listDemandHandleStepCfg 
			= demandModuleCfgDao.listDemandHandleStepCfg(sessionUser.getComId(),demandModuleCfgId);
			demandModuleCfg.setListDemandHandleStepCfg(listDemandHandleStepCfg);
		}
		return demandModuleCfg;
	}

	/**
	 * 修改需求处理进度模板
	 * @param sessionUser
	 * @param demandModuleCfg
	 */
	public void updateDemandModuleCfg(UserInfo sessionUser,
			DemandModuleCfg demandModuleCfg) {
		Integer demandModuleCfgId = demandModuleCfg.getId();
		
		//设置更新时间
		String modifyTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		
		//基础属性赋值
		demandModuleCfg.setComId(sessionUser.getComId());
		demandModuleCfg.setModifyUserId(sessionUser.getId());
		demandModuleCfg.setModifyTime(modifyTime);
		//添加需求模板
		demandModuleCfgDao.update(demandModuleCfg);
		
		//需求处理步骤
		demandModuleCfgDao.delByField("demandHandleStepCfg", new String[]{"comId","demandModuleCfgId"}
			, new Object[]{sessionUser.getComId(),demandModuleCfgId});
		
		List<DemandHandleStepCfg> listDemandHandleStepCfg = demandModuleCfg.getListDemandHandleStepCfg();
		if(null!=listDemandHandleStepCfg && !listDemandHandleStepCfg.isEmpty()){
			for (DemandHandleStepCfg demandHandleStepCfg : listDemandHandleStepCfg) {
				demandHandleStepCfg.setComId(sessionUser.getComId());
				demandHandleStepCfg.setDemandModuleCfgId(demandModuleCfgId);
				demandModuleCfgDao.add(demandHandleStepCfg);
			}
		}
		
		//添加系统日志
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName()
					, "编辑需求处理模板："+demandModuleCfg.getModName(), ConstantInterface.TYPE_ORG
					, sessionUser.getComId(), sessionUser.getOptIP());
		
		
	}

	/**
	 * 删除需求处理模板信息
	 * @param sessionUser
	 * @param ids
	 */
	public void delDemandModuleCfg(UserInfo sessionUser, Integer[] ids) {
		if(null != ids && ids.length>0){
			for (Integer demandModuleCfgId : ids) {
				//需求处理步骤
				demandModuleCfgDao.delByField("demandHandleStepCfg", new String[]{"comId","demandModuleCfgId"}
					, new Object[]{sessionUser.getComId(),demandModuleCfgId});
				demandModuleCfgDao.delById(DemandModuleCfg.class, demandModuleCfgId);
			}
		}
		
	}

	/**
	 * 复制需求处理模板信息
	 * @param sessionUser
	 * @param demandModuleCfgId
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void copyDemandModuleCfg(UserInfo sessionUser,
			Integer demandModuleCfgId) {
		
		DemandModuleCfg demandModuleCfg = (DemandModuleCfg) demandModuleCfgDao.objectQuery(DemandModuleCfg.class, demandModuleCfgId);
		demandModuleCfg.setId(null);
		String modName = demandModuleCfg.getModName();
		
		
		//设置模板复制后的名称
		boolean blnNameDup = this.checkDemandModuleCfgByName(sessionUser, modName, null);
		Integer index = 1;
		String modNameClone = modName;
		while(blnNameDup){
			modNameClone = modName +"-副本"+index;
			blnNameDup = this.checkDemandModuleCfgByName(sessionUser, modNameClone, null);
		}
		
		DemandModuleCfg moduleCfgClone = new DemandModuleCfg();
		moduleCfgClone.setModName(modNameClone);
		
		//设置更新时间
		String modifyTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		
		//基础属性赋值
		moduleCfgClone.setComId(sessionUser.getComId());
		moduleCfgClone.setUserId(sessionUser.getId());
		moduleCfgClone.setModifyUserId(sessionUser.getId());
		moduleCfgClone.setModifyTime(modifyTime);
		//添加需求模板
		Integer demandModuleCfgCloneId = demandModuleCfgDao.add(moduleCfgClone);
		
		//复制处理步骤
		List<DemandHandleStepCfg> listDemandHandleStepCfg = demandModuleCfg.getListDemandHandleStepCfg();
		if(null!=listDemandHandleStepCfg && !listDemandHandleStepCfg.isEmpty()){
			for (DemandHandleStepCfg demandHandleStepCfg : listDemandHandleStepCfg) {
				demandHandleStepCfg.setComId(sessionUser.getComId());
				demandHandleStepCfg.setDemandModuleCfgId(demandModuleCfgCloneId);
				demandHandleStepCfg.setId(null);
				demandModuleCfgDao.add(demandHandleStepCfg);
			}
		}
		//添加系统日志
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName()
					, "复制需求处理模板："+modName, ConstantInterface.TYPE_ORG
					, sessionUser.getComId(), sessionUser.getOptIP());
				
	}
	
}
