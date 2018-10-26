package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.SysUpgradeLog;
import com.westar.core.dao.SysUpgradeLogDao;

@Service
public class SysUpgradeLogService {

	@Autowired
	SysUpgradeLogDao sysUpgradeLogDao;
	
	/**
	 * 添加升级日志
	 * @param upLog
	 */
	public void  addUpgradeLog(SysUpgradeLog upLog){
		sysUpgradeLogDao.add(upLog);
	}
	
	/**
	 *  滚动查询升级日志
	 * @param sysUpLog
	 * @return
	 */
	public List<SysUpgradeLog> listSysUpLog(Integer maxId, Integer type){
		return sysUpgradeLogDao.listSysUpLog( maxId,  type);
	}
	
}
