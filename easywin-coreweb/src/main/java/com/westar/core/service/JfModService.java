package com.westar.core.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.JfMod;
import com.westar.base.model.JfScore;
import com.westar.base.model.JfSubUserScope;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.ItemStagedInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.TaskJfStatis;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.JfModDao;
import com.westar.core.web.PaginationContext;

@Service
public class JfModService {

	@Autowired
	JfModDao jfModDao;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	SystemLogService systemLogService;
	/**
	 * 分页查询待评分的任务
	 * @param jfMod 模块主键
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	public List<JfMod> listPagedTaskToJf(JfMod jfMod, UserInfo sessionUser) {
		List<JfMod> list = jfModDao.listPagedTaskToJf(jfMod,sessionUser);
		return list;
	}
	/**
	 * 分页查询待评分的任务
	 * @param jfMod 模块主键
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	public List<JfScore> listPagedMineTaskToJf(JfScore jfScore, UserInfo sessionUser) {
		List<JfScore> list = jfModDao.listPagedMineTaskToJf(jfScore,sessionUser);
		return list;
	}
	/**
	 * 添加任务积分信息
	 * @param jfMod
	 * @param sessionUser
	 */
	public void addBusJf(JfMod jfMod, UserInfo sessionUser) {
		Integer busId = jfMod.getBusId();
		String busType = jfMod.getBusType();
		
		//积分模块主键
		Integer jfModId = 0;
		//查询是否已经积过分
		JfMod preJfMod = jfModDao.queryJfMod(busId,busType);
		if(null == preJfMod){
			//设置团队号
			jfMod.setComId(sessionUser.getComId());
			//添加积分模块
			jfModId = jfModDao.add(jfMod);
		}else{
			jfModId = preJfMod.getId();
		}
		//得分情况
		List<JfScore> listJfScores = jfMod.getListJfScores();
		if(null!=listJfScores && !listJfScores.isEmpty() ){
			for (JfScore jfScore : listJfScores) {
				Integer dfUserId = jfScore.getDfUserId();
				//删除上次评分
				jfModDao.delByField("jfScore", new String[]{"jfModId","dfUserId"}, new Object[]{jfModId,dfUserId});
				//设置模块信息
				jfScore.setJfModId(jfModId);
				//设置团队号
				jfScore.setComId(sessionUser.getComId());
				//设置评分人员
				jfScore.setPfUserId(sessionUser.getId());
				//添加积分评定信息
				jfModDao.add(jfScore);
			}
		}
		
	}
	/**
	 * 添加常规积分
	 * @param jfMod
	 * @param sessionUser
	 */
	public void addNormalJf(JfMod jfMod, UserInfo sessionUser) {
		if(CommonUtil.isNull(jfMod.getBusType())) {
			jfMod.setBusType("0");
		}
		//设置团队号
		jfMod.setComId(sessionUser.getComId());
		//添加积分模块
		Integer jfModId = jfModDao.add(jfMod);
		
		//得分情况
		List<JfScore> listJfScores = jfMod.getListJfScores();
		if(null!=listJfScores && !listJfScores.isEmpty() ){
			for (JfScore jfScore : listJfScores) {
				//设置模块信息
				jfScore.setJfModId(jfModId);
				//设置团队号
				jfScore.setComId(sessionUser.getComId());
				//设置评分人员
				jfScore.setPfUserId(sessionUser.getId());
				//添加积分评定信息
				jfModDao.add(jfScore);
			}
		}
		
	}
	/**
	 * 查询需要评分的数据信息
	 * @param jfMod 积分模块信息
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	public List<JfScore> listTaskJfScore(JfMod jfMod, UserInfo sessionUser) {
		List<JfScore> jfScores = jfModDao.listTaskJfScore(jfMod,sessionUser);
		return jfScores;
	}
	/**
	 * 查询自己设置的下属评分范围
	 * @param sessionUser
	 * @return
	 */
	public List<JfSubUserScope> listJfSubScope(UserInfo sessionUser) {
		return jfModDao.listJfSubScope(sessionUser);
	}
	/**
	 * 修改自己的评分下属
	 * @param sessionUser
	 * @param jfSubScopeList
	 * @param needScore 
	 */
	public void updateJfSubScope(UserInfo sessionUser,
			List<JfSubUserScope> jfSubScopeList, String needScore) {
		//删除自己的评分下属
		jfModDao.delByField("jfSubUserScope", new String[]{"comId","leaderId","needScore"}, 
				new Object[]{sessionUser.getComId(),sessionUser.getId(),needScore});
		String userNames = "";
		if(null!=jfSubScopeList && !jfSubScopeList.isEmpty()){
			for (JfSubUserScope jfSubUser : jfSubScopeList) {
				jfSubUser.setLeaderId(sessionUser.getId());
				jfSubUser.setComId(sessionUser.getComId());
				userNames = userNames + "、"+ jfSubUser.getSubUserName();
				jfModDao.add(jfSubUser);
			}
		}
		
		if(!"".equals(userNames)){
			userNames = userNames.substring(1,userNames.length());
		}
		String log = "任务参与评分下属变更为:\""+userNames +"\"";
		if(StringUtils.isNotEmpty(needScore) && needScore.equals("0")){
			log = "任务不参与评分下属变更为:\""+userNames +"\"";
		}
		//添加系统日志记录 
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), log,
				ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		
	}
	/**
	 * 删除自己的评分下属
	 * @param sessionUser
	 * @param jfSubUserScope
	 */
	public void delJfSubUserScope(UserInfo sessionUser,
			JfSubUserScope jfSubUserScope) {
		String needScore = jfSubUserScope.getNeedScore();
		//删除自己的评分下属
		jfModDao.delByField("jfSubUserScope", new String[]{"comId","leaderId","subUserId","needScore"}, 
				new Object[]{sessionUser.getComId(),sessionUser.getId(),jfSubUserScope.getSubUserId(),needScore});
		
		String log = "删除任务参与评分下属人员:\""+jfSubUserScope.getSubUserName() +"\"";
		if(StringUtils.isNotEmpty(needScore) && needScore.equals("0")){
			log = "删除任务不参与评分下属人员:\""+jfSubUserScope.getSubUserName() +"\"";
		}
		
		//添加系统日志记录 
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(),log,
				ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());

	}
	/**
	 * 积分统计
	 * @param jfScore
	 * @param userInfo
	 * @return
	 */
	public PageBean<TaskJfStatis> listTaskJfStatis(JfScore jfScore, UserInfo userInfo,boolean isForceIn) {
		Integer totalCount = jfModDao.countTaskJfStatis(jfScore, userInfo,isForceIn);
		PaginationContext.setTotalCount(totalCount);
		List<TaskJfStatis> list = jfModDao.listTaskJfStatis(jfScore, userInfo,isForceIn);
//		for(TaskJfStatis taskJfStatis:list) {
//			TaskJfStatis taskJfStatisT = jfModDao.getTaskJfStatisT(taskJfStatis.getUserId(),jfScore,userInfo);
//			taskJfStatis.setJfTaskNum(taskJfStatisT.getJfTaskNum());
//			taskJfStatis.setScoreTotal(taskJfStatisT.getScoreTotal());
//			taskJfStatis.setTaskTotal(taskJfStatisT.getTaskTotal());
//		}
		
		PageBean<TaskJfStatis> pageBean = new PageBean<TaskJfStatis>();
	 	pageBean.setRecordList(list);
	 	pageBean.setTotalCount(totalCount);
			 	
		return pageBean;
	}
	/**
	 * 积分统计
	 * @param jfScore
	 * @param userInfo
	 * @return
	 */
	public List<TaskJfStatis> listTaskJfStatisOfAll(JfScore jfScore, UserInfo userInfo,boolean isForceIn) {
		Integer totalCount = jfModDao.countTaskJfStatis(jfScore, userInfo,isForceIn);
		PaginationContext.setPageSize(totalCount);
		PaginationContext.setOffset(0);
		PaginationContext.setTotalCount(totalCount);
		List<TaskJfStatis> list = jfModDao.listTaskJfStatis(jfScore, userInfo,isForceIn);
//		for(TaskJfStatis taskJfStatis:list) {
//			TaskJfStatis taskJfStatisT = jfModDao.getTaskJfStatisT(taskJfStatis.getUserId(),jfScore,userInfo);
//			taskJfStatis.setJfTaskNum(taskJfStatisT.getJfTaskNum());
//			taskJfStatis.setScoreTotal(taskJfStatisT.getScoreTotal());
//			taskJfStatis.setTaskTotal(taskJfStatisT.getTaskTotal());
//		}
		return list;
	}
	
}
