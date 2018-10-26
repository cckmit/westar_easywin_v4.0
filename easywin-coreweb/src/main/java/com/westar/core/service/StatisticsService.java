package com.westar.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.westar.base.model.BusRemind;
import com.westar.base.model.Daily;
import com.westar.base.model.DataDic;
import com.westar.base.model.DemandProcess;
import com.westar.base.model.Department;
import com.westar.base.model.Item;
import com.westar.base.model.Task;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WeekReport;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.StatisticCrmVo;
import com.westar.base.pojo.StatisticTaskVo;
import com.westar.base.pojo.WeekReportPojo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.StatisticsDao;
import com.westar.core.web.DataDicContext;
import com.westar.core.web.PaginationContext;

@Service
public class StatisticsService {
	
	@Autowired
	StatisticsDao statisticsDao;

	@Autowired
	DepartmentService departmentService;
	
	private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);
	
	/**
	 * 通过类型获取运营总览的数据
	 * @param sessionUser
	 * @param type
	 * @param version 
	 * @return
	 */
	public JSONObject statisticTodayByType(UserInfo sessionUser,String type, String version){
		JSONObject jsonObject = null;
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		if(ConstantInterface.TYPE_TASK.equals(type)){//今日新增任务
			jsonObject =  statisticsDao.statisticsTask(sessionUser,nowDateTime,version);
		}else if(ConstantInterface.TYPE_CRM.equals(type)){//今日新增客户
			jsonObject = statisticsDao.statisticsTodayCrm(sessionUser,nowDateTime);
		}else if(ConstantInterface.TYPE_DAILY.equals(type)){//今日日报
			jsonObject = statisticsDao.statisticsTodayDaily(sessionUser,nowDateTime);
		}else if(ConstantInterface.TYPE_WEEK.equals(type)){//本周周报
			//取得一周第一天
			String startDate = null;
			try {
				startDate = DateTimeUtil.getFirstDayOfWeek(nowDateTime, DateTimeUtil.yyyy_MM_dd);
				
				String endDate = null;
				
				String weekName = DateTimeUtil.getChineseDay();
				if(weekName.equals(DateTimeUtil.DAY_NAMES[0])
						||weekName.equals(DateTimeUtil.DAY_NAMES[5])
						|| weekName.equals(DateTimeUtil.DAY_NAMES[6])){//今天在星期五之前
					//取得一周最后一天
					endDate = DateTimeUtil.addDate(startDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 6);
				}else{
					//取得上周最后一天
					endDate = DateTimeUtil.addDate(startDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, -1);
				}
				
				//取得周报的截止时间
				String lastDate = statisticsDao.statisticsWeekLastDate(sessionUser,endDate);
				
				jsonObject = statisticsDao.statisticsWeek(sessionUser,endDate,lastDate);
				//周数
				Integer nowWeekNum = DateTimeUtil.getWeekOfYear(endDate,DateTimeUtil.yyyy_MM_dd);
				jsonObject.put("NOWWEEKNUM", nowWeekNum);
				jsonObject.put("NOWWEEKYEAR", endDate.substring(0,4));
			} catch (ParseException e) {
				
			}
		}
		return jsonObject;
	}
	/**
	 * 按照部门统计指定时间段创建和办结的
	 * @param sessionuser 当前操作人员
	 * @param startDate 查询的开始时间
	 * @param endDate 查询的结束时间
	 * @param taskDateTimeType 
	 * @param version 
	 * @return
	 */
	public List<StatisticTaskVo> statisticTaskForDep(UserInfo sessionuser,String startDate,String endDate, String taskDateTimeType, String version){
		
		//当前日期
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		if(StringUtils.isNotEmpty(taskDateTimeType)){
			//今日
			if(taskDateTimeType.equals("day")){
				startDate = nowDate;
				endDate = nowDate;
			}
			//本周
			else if(taskDateTimeType.equals("week")){
				//取得一周第一天
				try {
					startDate = DateTimeUtil.getFirstDayOfWeek(nowDate, DateTimeUtil.yyyy_MM_dd);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//取得一周最后一天
				endDate = DateTimeUtil.addDate(startDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 6);
			}
			//本月
			else if(taskDateTimeType.equals("month")){
				//取得一月第一天
				startDate = nowDate.substring(0,7)+"-01";
				//取得一月最后一天
				String nextMonth = DateTimeUtil.addDate(startDate, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1);
				endDate = DateTimeUtil.addDate(nextMonth, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, -1);
			}
			//全年
			else if(taskDateTimeType.equals("year")){
				//取得全年第一天
				startDate = nowDate.substring(0,4)+"-01-01";
				endDate = nowDate;
			}
		}
		
		if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
			startDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM)+"-01";
			endDate = nowDate;
		}else if(StringUtils.isNotEmpty(startDate) && StringUtils.isEmpty(endDate)){
			endDate = nowDate;
		}else if(StringUtils.isEmpty(startDate) && StringUtils.isNotEmpty(endDate)){
			startDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM)+"-01";
		}
		return statisticsDao.statisticTaskHandAndFinishForDep(sessionuser, startDate, endDate,version);
	}
	/**
	 * 运营分析的指定时间的任务办理情况
	 * @param sessionuser 当前操作人员
	 * @param startDate 统计的开始时间
	 * @param endDate 统计的结束时间
	 * @param taskDateTimeType 
	 * @param taskDateTimeType 查询时间类型
	 * @param version 
	 * @return
	 */
	public List<StatisticTaskVo> statisticTaskTop(UserInfo sessionuser,String startDate,String endDate, String taskDateTimeType, String version){
		
		//当前日期
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		if(StringUtils.isNotEmpty(taskDateTimeType)){
			//今日
			if(taskDateTimeType.equals("day")){
				startDate = nowDate;
				endDate = nowDate;
			}
			//本周
			else if(taskDateTimeType.equals("week")){
				//取得一周第一天
				try {
					startDate = DateTimeUtil.getFirstDayOfWeek(nowDate, DateTimeUtil.yyyy_MM_dd);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//取得一周最后一天
				endDate = DateTimeUtil.addDate(startDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 6);
			}
			//本月
			else if(taskDateTimeType.equals("month")){
				//取得一月第一天
				startDate = nowDate.substring(0,7)+"-01";
				//取得一月最后一天
				String nextMonth = DateTimeUtil.addDate(startDate, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1);
				endDate = DateTimeUtil.addDate(nextMonth, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, -1);
			}else if(taskDateTimeType.equals("year")){//全年
				//取得全年第一天
				startDate = nowDate.substring(0,4)+"-01-01";
				endDate = nowDate;
			}
		}
		
		if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
			startDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM)+"-01";
			endDate = nowDate;
		}else if(StringUtils.isNotEmpty(startDate) && StringUtils.isEmpty(endDate)){
			endDate = nowDate;
		}else if(StringUtils.isEmpty(startDate) && StringUtils.isNotEmpty(endDate)){
			startDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM)+"-01";
		}
		return statisticsDao.statisticTaskTop(sessionuser, startDate, endDate,version);
	}
	/**
	 * 运营分析之任务分类
	 * @param sessionuser
	 * @param version 
	 * @return
	 */
	public List<StatisticTaskVo> statisticTaskGrade(UserInfo sessionuser, String version){
		List<DataDic> dataDicList = DataDicContext.getInstance().listTreeDataDicByType("grade");
		return statisticsDao.statisticTaskGrade(sessionuser,dataDicList,version);
	}
	/**
	 * 运营分析之客户分类
	 * @param sessionuser
	 * @return
	 */
	public List<StatisticCrmVo> statisticCrmType(UserInfo sessionuser){
		return statisticsDao.statisticCrmType(sessionuser);
	}
	
	/**
	 * 任务执行督办数据查询
	 * @param sessionuser 当前操作人员
	 * @param statisticsVo 查询条件
	 * @return
	 */
	public PageBean<JSONObject> listPagedStatisticSupTask(UserInfo sessionuser,
			Task task){
		//任务执行督办数据查询
		List<JSONObject> recordList = statisticsDao.listPagedStatisticSupTask(sessionuser,task);
		
		PageBean<JSONObject> pageBean = new PageBean<JSONObject>();
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	
	/**
	 * 分页查询催办统计信息
	 * @param sessionUser 当前操作人员
	 * @param busRemind 催办查询条件
	 * @return
	 */
	public PageBean<JSONObject> listPagedStatisticBusRemind(
			UserInfo sessionUser, BusRemind busRemind) {
		//事项催办统计
		List<JSONObject> recordList = statisticsDao.listPagedStatisticBusRemind(sessionUser,busRemind);
		
		PageBean<JSONObject> pageBean = new PageBean<JSONObject>();
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	
	/**
	 * 日报汇报汇总
	 * @param daily
	 * @param curUser
	 * @return
	 */
	public List<Daily> listPagedDaily(Daily daily, UserInfo curUser) {
		//部门查询
		if(null!=daily.getListDep()){
			//将数据整理成查询条件
			List<Integer> depIds = new ArrayList<Integer>();
			//遍历部门主键信息
			for (Department department : daily.getListDep()) {
				depIds.add(department.getId());
			}
			//数据条件存入
			daily.setListTreeDeps(depIds.toArray(new Integer[]{}));
		}
		List<Integer> ownerArray = new ArrayList<Integer>();
		
		 if(null != daily.getListOwner() && !daily.getListOwner().isEmpty()){
			//遍历部门主键信息
			for (UserInfo owner : daily.getListOwner()) {
				ownerArray.add(owner.getId());
			}
			//数据条件存入
			daily.setOwnerArray(ownerArray.toArray(new Integer[]{}));
		 }
		return  statisticsDao.listPagedDaily(daily,curUser);
	}
	
	/**
	 * 周报发布情况统计
	 * @param weekReport
	 * @param userInfo
	 * @return
	 */
	public List<WeekReport> listWeekRepStatistics(WeekReportPojo weekReport, UserInfo userInfo) {
		
		List<Department> listDep = weekReport.getListDep();
		//部门查询
		if(null!=listDep && !listDep.isEmpty()){
			//将数据整理成查询条件
			List<Integer> depIds = new ArrayList<Integer>();
			for (Department department : listDep) {
				//查询本部门和下级部门信息
				List<Department> listTreeDeps = departmentService.listTreeSonDep(department.getId(), ConstantInterface.SYS_ENABLED_YES, userInfo.getComId());
				//有查询结果需要遍历
				if(null != listTreeDeps && !listTreeDeps.isEmpty()){
					//遍历部门主键信息
					for (Department sonDepartment : listTreeDeps) {
						if(!depIds.contains(sonDepartment.getId())){
							depIds.add(sonDepartment.getId());
						}
					}
				}
			}
			//数据条件存入
			weekReport.setListTreeDeps(depIds.toArray(new Integer[]{}));
		}
		
		//本周所在周数
		Integer weekNum = weekReport.getWeekNum();
		//周数所在年
		Integer weekYear = weekReport.getWeekYear();
		if(null != weekYear || null != weekNum){//按照年份和周数选择的话就没有时间区间信息
			weekReport.setStartDate("");
			weekReport.setEndDate("");
		}
		List<WeekReport> weekReports = new ArrayList<WeekReport>(15);
		try {
			weekReports = statisticsDao.listWeekRepStatistics(weekReport, userInfo);
		} catch (ParseException e) {
			logger.error("listWeekRepStatistics 格式转换错误");
		}
		return weekReports;
	}
	
	/**
	 * 分页查询需求落实监督数据
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 
	 * @return
	 */
	public PageBean<DemandProcess> listPagedStatisticDemandProcess(
			UserInfo sessionUser, DemandProcess demandProcess) {
		return statisticsDao.listPagedStatisticDemandProcess(sessionUser,demandProcess);
	}
	/**
	 * 分页查询在建项目监督数据
	 * @param sessionUser
	 * @param item
	 * @return
	 */
	public PageBean<Item> listPagedStatisticItem(UserInfo sessionUser, Item item) {
		return statisticsDao.listPagedStatisticItem(sessionUser, item);
	}
	/**
	 * 分页查询任务执行监督数据
	 * @param sessionUser
	 * @param task
	 * @return
	 */
	public PageBean<Task> listPagedStatisticTask(UserInfo sessionUser, Task task) {
		return statisticsDao.listPagedStatisticTask(sessionUser,task);
	}
	/**
	 * 分页查询任务执行监督数据
	 * @param sessionUser
	 * @param task
	 * @return
	 */
	public PageBean<Task> listPagedStatisticTaskByExecutor(UserInfo sessionUser, Task task) {
		return statisticsDao.listPagedStatisticTask(sessionUser,task);
	}
	
}
