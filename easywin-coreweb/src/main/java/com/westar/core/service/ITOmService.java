package com.westar.core.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.EventPm;
import com.westar.base.model.IssuePm;
import com.westar.base.model.ModifyPm;
import com.westar.base.model.ReleasePm;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.EventPMTable;
import com.westar.base.pojo.IssuePMTable;
import com.westar.base.pojo.ModifyPMTable;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.ReleasePMTable;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.ITOmDao;
import com.westar.core.web.PaginationContext;

@Service
public class ITOmService {
	
	private static Logger loger = Logger.getLogger(ITOmService.class);
	
	@Autowired
	ITOmDao iTOmDao;
	/**
	 * 分页查询事件管理
	 * @param userInfo 当前操作人员
	 * @param eventPm 事件管理查询条件
	 * @return
	 */
	public PageBean<EventPm> listPagedEventPm(UserInfo userInfo, EventPm eventPm) {
		PageBean<EventPm> pageBean = new PageBean<EventPm>();
		//分页查询事件管理
		List<EventPm> recordList = iTOmDao.listPagedEventPm(userInfo,eventPm);
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	/**
	 * 分页查询问题过程管理
	 * @param userInfo 当前操作人员
	 * @param issuePm 问题管理查询条件
	 * @return
	 */
	public PageBean<IssuePm> listPagedIssuePm(UserInfo userInfo, IssuePm issuePm) {
		PageBean<IssuePm> pageBean = new PageBean<IssuePm>();
		//分页查询事件管理
		List<IssuePm> recordList = iTOmDao.listPagedIssuePm(userInfo,issuePm);
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	/**
	 * 分页查询变更过程管理
	 * @param userInfo 当前操作人员
	 * @param modifyPm 变更管理查询条件
	 * @return
	 */
	public PageBean<ModifyPm> listPagedModifyPm(UserInfo userInfo, ModifyPm modifyPm) {
		PageBean<ModifyPm> pageBean = new PageBean<ModifyPm>();
		//分页查询事件管理
		List<ModifyPm> recordList = iTOmDao.listPagedModifyPm(userInfo,modifyPm);
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	/**
	 * 分页查询变更过程管理
	 * @param userInfo 当前操作人员
	 * @param releasePm 发布管理查询条件
	 * @return
	 */
	public PageBean<ReleasePm> listPagedReleasePm(UserInfo userInfo, ReleasePm releasePm) {
		PageBean<ReleasePm> pageBean = new PageBean<ReleasePm>();
		//分页查询事件管理
		List<ReleasePm> recordList = iTOmDao.listPagedReleasePm(userInfo,releasePm);
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}

	
	
	
	
	
	/**
	 * 事件过程分析
	 * @param eventPm 事件过程分析查询条件
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	public List<EventPMTable> analyseEventPm(EventPm eventPm,UserInfo sessionUser){
		List<EventPMTable> eventPmTables = iTOmDao.analyseEventPm(eventPm, sessionUser);
		return eventPmTables;
	}
	/**
	 * 问题过程分析
	 * @param issuePm 事件过程分析查询条件
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	public List<IssuePMTable> analyseIssuePm(IssuePm issuePm,UserInfo sessionUser){
		List<IssuePMTable> issuePmTables = iTOmDao.analyseIssuePm(issuePm, sessionUser);
		return issuePmTables;
	}
	/**
	 * 变更过程分析
	 * @param modifyPm 事件过程分析查询条件
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	public List<ModifyPMTable> analyseModifyPm(ModifyPm modifyPm,UserInfo sessionUser){
		List<ModifyPMTable> modifyPmTables = iTOmDao.analyseModifyPm(modifyPm, sessionUser);
		return modifyPmTables;
	}
	/**
	 * 变更过程分析
	 * @param modifyPm 事件过程分析查询条件
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	public List<ReleasePMTable> analyseReleasePm(ReleasePm releasePm,UserInfo sessionUser){
		List<ReleasePMTable> releasePmTables = iTOmDao.analyseReleasePm(releasePm, sessionUser);
		return releasePmTables;
	}
	/**
	 * 查询运维管理关联的流程信息
	 * @param userInfo 当前操作人员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	public Integer getItomDetailIns(UserInfo userInfo, Integer busId,
			String busType) {
		if(ConstantInterface.TYPE_ITOM_EVENTPM.equals(busType)){
			EventPm pmObj = (EventPm) iTOmDao.objectQuery(EventPm.class, busId);
			return pmObj.getInstanceId();
		} else if(ConstantInterface.TYPE_ITOM_ISSUEPM.equals(busType)){
			IssuePm pmObj = (IssuePm) iTOmDao.objectQuery(IssuePm.class, busId);
			return pmObj.getInstanceId();
		} else if(ConstantInterface.TYPE_ITOM_MODIFYPM.equals(busType)){
			ModifyPm pmObj = (ModifyPm) iTOmDao.objectQuery(ModifyPm.class, busId);
			return pmObj.getInstanceId();
		} else if(ConstantInterface.TYPE_ITOM_RELEASEPM.equals(busType)){
			ReleasePm pmObj = (ReleasePm) iTOmDao.objectQuery(ReleasePm.class, busId);
			return pmObj.getInstanceId();
		}
		return null;
	}
}
