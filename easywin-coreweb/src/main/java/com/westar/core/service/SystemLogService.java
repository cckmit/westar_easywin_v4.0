package com.westar.core.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.SystemLog;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WorkTrace;
import com.westar.base.pojo.CommonLog;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.dao.SystemLogDao;

@Service
public class SystemLogService {
	@Autowired
	private SystemLogDao systemLogDao;
	
	/**
	 * 分页查询系统日志
	 * @param systemLog
	 * @return
	 */
	public List<SystemLog> listPagedOrgSysLog(SystemLog systemLog){
		return systemLogDao.listPagedOrgSysLog(systemLog);
	}
	/**
	 * 分页查询系统日志
	 * @param systemLog
	 * @return
	 */
	public List<SystemLog> listPagedSelfSysLog(SystemLog systemLog){
		return systemLogDao.listPagedSelfSysLog(systemLog);
	}
	
	/**
	 * 添加日志
	 * @param userId 操作者id
	 * @param userName 操作者姓名
	 * @param content 操作内容
	 * @param businessType 业务类别
	 * @param comId 企业主键
	
	 */
	public void addSystemLog(Integer userId,String userName,String content,String businessType,
			Integer comId,String optIP){
		this.addSystemLog(userId, userName, content, businessType, 0, comId, optIP);
	}
	/**
	 * 添加日志
	 * @param userId 操作者id
	 * @param userName 操作者姓名
	 * @param content 操作内容
	 * @param businessType 业务类别
	 * @param comId 企业主键
	
	 */
	public void addSystemLog(Integer userId,String userName,String content,String businessType,
			Integer busId,Integer comId,String optIP){
		SystemLog systemLog = new SystemLog();
		systemLog.setComId(comId);
		systemLog.setContent(content);
		systemLog.setUserId(userId);
		systemLog.setUserName(userName);
		systemLog.setRecordDateTime(DateTimeUtil.formatDate(new Date(), 1));
		systemLog.setBusinessType(businessType);
		systemLog.setBusId(busId);
		systemLog.setOptIP(optIP);
		systemLogDao.add(systemLog);
	}
	
	/**
	 * 添加系统日志以及工作轨迹
	 * @param sessionUser
	 * @param traceUserId
	 * @param busType
	 * @param busId
	 * @param content
	 * @param traceContent
	 * @param optIP
	 */
	public void addSystemLogWithTrace(UserInfo sessionUser, Integer traceUserId, String busType, Integer busId,
			String content,String traceContent){
		//添加日志
		this.addSystemLog(sessionUser.getId(), sessionUser.getIfOnline(), content, busType, 
				busId,sessionUser.getComId(), sessionUser.getOptIP());
		//添加工作轨迹
		this.addWorkTrace(sessionUser, traceUserId, busType, busId, traceContent);
	}
	
	/**
	 * 添加工作轨迹
	 * @param sessionUser 企业号
	 * @param traceUserId 操作员主键
	 * @param busType 业务类型
 	 * @param busId 业务主键
	 * @param content 轨迹内容
	 */
	public void addWorkTrace(UserInfo sessionUser, Integer traceUserId, String busType, Integer busId,
			String content) {
		
		WorkTrace workTrace = new WorkTrace();
		//设置企业号
		workTrace.setComId(sessionUser.getComId());
		
		content = StringUtil.cutString(content, 1000,null);
		//日志内容
		workTrace.setContent(content);
		//操作人员
		workTrace.setTraceUserId(traceUserId);
		//业务类型
		workTrace.setBusinessType(busType);
		//业务主键
		workTrace.setBusId(busId);
		systemLogDao.add(workTrace);
	}
	
	/**
	 * 根据id删除日志信息
	 * @param id
	 */
	public void delSystemLogById(Integer id){
		systemLogDao.delById(SystemLog.class, id);
	}
	
	/**
	 * 根据id批量删除日志信息
	 * @param ids
	 */
	public void delSystemLogByIds(Integer[] ids){
		int len = ids.length;
		for(int i=0;i<len;i++){
			this.delSystemLogById(ids[i]);
		}
	}

	/**
	 * 删除超过10w的日志
	 */
	public void delMoreSystemLog() {
		systemLogDao.delMoreSystemLog();
	}
	/**
	 * 用于控制选择入职时间的最大值不超过首次使用时间
	 * @param sessionUser
	 * @return
	 */
	public SystemLog getUserHireDate(UserInfo sessionUser) {
		SystemLog systemLog = systemLogDao.getUserHireDate(sessionUser);
		return systemLog;
	}
	
	/**
	 * 查询工作轨迹(信息分享，客户，项目，任务，问答，投票)
	 * @param userInfo 当前操作人员
	 * @param msgShare 工作轨迹
	 * @param modList 模块类型集合
	 * @return
	 */
	public List<WorkTrace> listWorkTrace(UserInfo userInfo, SystemLog systemLog,
			List<String> modList) {
		List<WorkTrace> list = systemLogDao.listWorkTrace(userInfo,systemLog,modList);
		return list;
	}
	/**
	 * 日志记录
	 * @param comId 团队主键
	 * @param modId	模块主键	
	 * @param busType 模块类别
	 * @return
	 */
	public PageBean<CommonLog> listPageLog(Integer comId,Integer modId,String busType){
		return systemLogDao.listPageLog(comId, modId, busType);
	}
}
