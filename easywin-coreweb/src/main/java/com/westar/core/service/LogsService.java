package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Logs;
import com.westar.base.pojo.PageBean;
import com.westar.core.dao.LogsDao;

@Service
public class LogsService {

	@Autowired
	LogsDao logsDao;
	
	/**
	 * 添加日志
	 * @param comId 企业编号
	 * @param busId 业务主键
	 * @param userId 操作者ID
	 * @param userName 操作者
	 * @param content 内容
	 * @param busType 业务类别
	 */
	public void addLogs(Integer comId,Integer busId,Integer userId,String userName,String content,String busType){
		Logs log = new Logs();
		log.setBusId(busId);
		log.setBusType(busType);
		log.setComId(comId);
		log.setContent(content);
		log.setUserId(userId);
		log.setUserName(userName);
		logsDao.add(log);
	}
	/**
	 * 日志记录
	 * @param comId 单位主键
	 * @param modId	模块主键	
	 * @param busType 模块类别
	 * @return
	 */
	public PageBean<Logs> listPageLog(Integer comId,Integer modId,String busType){
		return logsDao.listPageLog(comId, modId, busType);
	}
	/**
	 * 删除公告
	 * @param comId 
	 * @param busId 模块主键	
	 * @param busType 模块类别
	 */
	public void delLogs(Integer comId,Integer busId,String busType){
		logsDao.delByField("logs", new String[]{"comId","busId","busType"}, new Object[]{comId,busId,busType});
	}
	/**
	 * 最后一条日志记录
	 * @param comId
	 * @param modId
	 * @param busType
	 * @return
	 */
	public Logs queryLastLog(Integer comId,Integer modId,String busType){
		return logsDao.queryLastLog(comId, modId, busType);
	}
	
}
