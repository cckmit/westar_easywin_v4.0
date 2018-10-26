package com.westar.core.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.AttenceRecord;
import com.westar.base.model.BusRemind;
import com.westar.base.model.Customer;
import com.westar.base.model.CustomerStage;
import com.westar.base.model.CustomerType;
import com.westar.base.model.Daily;
import com.westar.base.model.DataDic;
import com.westar.base.model.DemandProcess;
import com.westar.base.model.Item;
import com.westar.base.model.Organic;
import com.westar.base.model.Task;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WeekReport;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.ItemStagedInfo;
import com.westar.base.pojo.ModStaticVo;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.StatisticCrmVo;
import com.westar.base.pojo.StatisticFeeCrmVo;
import com.westar.base.pojo.StatisticFeeItemVo;
import com.westar.base.pojo.StatisticTaskVo;
import com.westar.base.pojo.WeekReportPojo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.AttenceService;
import com.westar.core.service.CrmService;
import com.westar.core.service.FinancialService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.ItemService;
import com.westar.core.service.OrganicService;
import com.westar.core.service.StatisticsService;
import com.westar.core.service.TaskService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.WeekReportService;
import com.westar.core.web.DataDicContext;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

/**
 * 系统统计控制层
 * 
 * @author H87
 * 
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {

	@Autowired
	CrmService crmService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	ForceInPersionService forceInService;

	@Autowired
	OrganicService organicService;

	@Autowired
	TaskService taskService;

	@Autowired
	ItemService itemService;
	
	@Autowired
	FinancialService financialService;

	@Autowired
	AttenceService attenceService;
	
	@Autowired
	StatisticsService statisticsService;
	
	@Autowired
	WeekReportService weekReportService;

	/************************** 客户统计功能能区域 *****************************************/

	/**
	 * 客户统计
	 * 
	 * @param statisticsType
	 *            1 分类统计 2 更新统计 3分布统计 4归属统计
	 * @param crm
	 *            客户查询条件
	 * @return
	 */
	@RequestMapping(value = "/crm/statisticsCrmPage")
	public ModelAndView statisticsCrmPage(String statisticsType, Customer customer) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/crm/customerList");
		view.addObject("userInfo", userInfo);
		view.addObject("customer", customer);
		// 获取客户类型数据源
		List<CustomerType> listCustomerType = crmService.listCustomerType(this.getSessionUser().getComId());
		view.addObject("listCustomerType", listCustomerType);

		// 获取客户阶段数据源
		List<CustomerStage> listCrmStage = crmService.listCustomerStage(userInfo.getComId());
		view.addObject("listCrmStage", listCrmStage);
		
		// 取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(), userInfo.getId(), 5);
		view.addObject("listOwners", listOwners);

		// 是更新统计
		if (null != statisticsType && "5".equals(statisticsType)) {

			String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
			view.addObject("defaultYear", nowYear);

			List<String> yeastList = new ArrayList<String>();
			yeastList.add(nowYear);

			Organic org = organicService.getOrganicByNum(userInfo.getComId());
			Integer diffYear = Integer.parseInt(nowYear) - Integer.parseInt(org.getRecordCreateTime().substring(0, 4))
					+ 1;
			for (int i = 1; i < diffYear; i++) {
				nowYear = DateTimeUtil.addDate(nowYear, DateTimeUtil.yyyy, Calendar.YEAR, -1);
				yeastList.add(nowYear);
			}
			view.addObject("yeasList", yeastList);
			view.addObject("regisYM", org.getRecordCreateTime().substring(0, 7));
		}
		view.addObject("homeFlag", ConstantInterface.TYPE_CRM);
		return view;
	}

	/**
	 * 统计客户类型
	 * 
	 * @param statisticsType
	 *            1 分类统计 2 更新统计 3分布统计 4归属统计
	 * @param customer
	 *            客户查询条件
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/listCrmStatisByType")
	public Map<String, Object> listCrmStatisByType(String statisticsType, Customer customer,
			String listOwnerStr,String listCrmTypeStr) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(6);
		UserInfo userInfo = this.getSessionUser();

		if(null != listOwnerStr && !listOwnerStr.isEmpty()){
			Gson gson = new Gson();
			Customer owner = gson.fromJson(listOwnerStr, Customer.class);
			if(null != owner.getListOwner() && !owner.getListOwner().isEmpty()){
				customer.setListOwner(owner.getListOwner());
			}
		}
		if(null != listCrmTypeStr && !listCrmTypeStr.isEmpty()){
			Gson gson = new Gson();
			Customer owner = gson.fromJson(listCrmTypeStr, Customer.class);
			if(null != owner.getListCrmType() && !owner.getListCrmType().isEmpty()){
				customer.setListCrmType(owner.getListCrmType());
			}
		}
		// 客户区域ID初始化
		if (null != customer.getAreaIdAndType() && !"".equals(customer.getAreaIdAndType().trim())) {
			customer.setAreaId(Integer.parseInt(customer.getAreaIdAndType().split("@")[0]));
			customer.setAreaName(crmService.getAreaById(customer.getAreaId()).getAreaName());
		} else {
			customer.setAreaId(0);
		}
		// 验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_CRM);

		if (ConstantInterface.STATIS_CRM_TYPE.equals(statisticsType)) {
			// 1 分类统计
			List<ModStaticVo> busReports = crmService.listCrmStatisByType(customer, userInfo, isForceIn);
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put("listBusReport", busReports);
		} else if (ConstantInterface.STATIS_CRM_MODIFY.equals(statisticsType)) {
			// 2  更新统计

			List<List<ModStaticVo>> result = new ArrayList<List<ModStaticVo>>();	

			// 当前系统时间
			String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd) + " 00:00";
			// 一周前
			String weekAgoDate = DateTimeUtil.addDate(nowDate, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.DAY_OF_YEAR, -7);
			// 半月前
			String halfMonthAgo = DateTimeUtil.addDate(nowDate, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.DAY_OF_YEAR,
					-15);
			// 一月前
			String monthAgo = DateTimeUtil.addDate(nowDate, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.DAY_OF_YEAR, -30);

			for (int i = 1; i <= 3; i++) {
				if (i == 1) {
					customer.setFrequenStartDate(halfMonthAgo);
					customer.setFrequenEndDate(weekAgoDate);
				} else if (i == 2) {
					customer.setFrequenStartDate(monthAgo);
					customer.setFrequenEndDate(halfMonthAgo);
				} else if (i == 3) {
					customer.setFrequenStartDate(null);
					customer.setFrequenEndDate(monthAgo);
				}
				// 列出客户更新频率
				List<ModStaticVo> list = crmService.listCrmFrequenStatistic(customer, userInfo, isForceIn);
				result.add(list);
			}

			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put("listBusReport", result);
			map.put("weekAgoDate", weekAgoDate.substring(0, 10));
			map.put("halfMonthAgo", halfMonthAgo.substring(0, 10));
			map.put("monthAgo", monthAgo.substring(0, 10));

		} else if (ConstantInterface.STATIS_CRM_AREA.equals(statisticsType)) {
			// 3分布统计

			// 模板区域集合
			List<ModStaticVo> listRegion = crmService.listRegionForStatistic(userInfo, customer, isForceIn);
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put("listBusReport", listRegion);
		} else if (ConstantInterface.STATIS_CRM_OWNER.equals(statisticsType)) {
			// 4归属统计
			List<CustomerType> result = crmService.listOwnerCrmTypeStatistic(customer, userInfo, isForceIn);

			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put("listBusReport", result);
		} else if (ConstantInterface.STATIS_CRM_FREQ_ADD.equals(statisticsType)) {
			// 客户增量统计

			List<CustomerType> result = crmService.listCrmAddNumStatis(customer, userInfo, isForceIn);

			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put("listBusReport", result);

			Date date = new Date();
			@SuppressWarnings("deprecation")
			int month = date.getMonth();
			if (!customer.getSelectYear().equals(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy))) {
				month = 11;
			}
			map.put("nowMonth", month + 1);
		}else if (ConstantInterface.STATIS_CRM_STAGE.equals(statisticsType)) {
			// 客户阶段统计
			
			List<CustomerStage> result =crmService.listCrmStageStatistic(customer, userInfo, isForceIn);
			
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put("listBusReport", result);
		}else if (ConstantInterface.STATIS_CRM_BUDGET.equals(statisticsType)) {
			// 客户预算统计
			
			List<ModStaticVo> result = crmService.listCrmBudgetStatistic(customer, userInfo, isForceIn);
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put("listBusReport", result);
		}
		return map;
	}

	/************************** 客户统计功能能区域 *****************************************/

	/************************** 任务统计功能能区域 *****************************************/

	/**
	 * 跳转到任务统计界面
	 * 
	 * @param statisticsType统计类型
	 * @param task
	 *            任务查询条件
	 * @return
	 */
	@RequestMapping(value = "/task/statisticsTaskPage")
	public ModelAndView statisticsTaskPage(String statisticsType, Task task) {
		// 头文件的显示
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/task/taskCenter");
		view.addObject("userInfo", userInfo);
		
		if (null != statisticsType && statisticsType.equals("3")) {
			List<DataDic> dataDics = DataDicContext.getInstance().listTreeDataDicByType("grade");
			view.addObject("dataDics", dataDics);
		}
		view.addObject("homeFlag", ConstantInterface.TYPE_TASK);
		return view;
	}

	/**
	 * 分模块统计任务信息
	 * 
	 * @param statisticsType
	 *            统计类型
	 * @param task
	 *            任务的查询条件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/listTaskStatisByType")
	public Map<String, Object> listTaskStatisByType(String statisticsType, Task task) {
		Map<String, Object> map = new HashMap<String, Object>(3);
		UserInfo userInfo = this.getSessionUser();
		// 验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
		if (ConstantInterface.STATIS_TASK_GRADE.equals(statisticsType)) {
			//紧急度统计
			List<ModStaticVo> result = taskService.listTaskGrade4Statistic(userInfo, task, isForceIn);

			map.put("status", "y");
			map.put("listBusReport", result);
		} else if (ConstantInterface.STATIS_TASK_OVERDUE.equals(statisticsType)) {
			// 2 逾期统计
			List<ModStaticVo> result = taskService.listTaskOverDue4Statistic(userInfo, task, isForceIn);
			map.put("status", "y");
			map.put("listBusReport", result);
		} else if (ConstantInterface.STATIS_TASK_EXECTOR.equals(statisticsType)) {
			// 3执行分配统计
			List<ModStaticVo> result = taskService.listTask4Statistic(userInfo, task, isForceIn);
			map.put("status", "y");
			map.put("listBusReport", result);
		}else if (ConstantInterface.STATIS_TASK_RELATE_CRM.equals(statisticsType)) {
			// 5客户任务统计
			List<ModStaticVo> result = taskService.listCrmTask4Statistic(userInfo, task);
			map.put("status", "y");
			map.put("listBusReport", result);
		}

		return map;
	}
	/************************** 任务统计功能能区域 *****************************************/

	/************************** 项目统计功能能区域 *****************************************/
	
	/**
	 * 项目任务统计
	 * @param item
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/listItemStatics")
	public Map<String, Object> listItemStatis(Item item,Integer pageNum, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		//项目任务统计
		PageBean<ItemStagedInfo> result = taskService.listItemTask4Statistic(userInfo, item);
		
		map.put("pageBean", result);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	/**
	 * 单个项目任务详情列表
	 * @param itemId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/listItemTaskDetail")
	public Map<String, Object> listItemTaskDetail(Integer itemId) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		//项目任务详情列表
		Item result = taskService.listItemTaskDetail(userInfo,itemId);
		map.put("item", result);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	/**********************平台统计信息********************************/
	
	/**
	 * 平台统计中心
	 * @param sid
	 * @return
	 */
	@RequestMapping(value = "/platform/statisticCenter")
	public ModelAndView statisticCenter(String sid) {
		ModelAndView mav = new ModelAndView("redirect:/statistics/platform/operationSummarize?sid="+sid+"&activityMenu=platform_1.0");
		return mav;
	}
	
	/**
	 * 运营总览
	 * @return
	 */
	@RequestMapping(value = "/platform/operationSummarize")
	public ModelAndView operationSummarize() {
		ModelAndView mav = new ModelAndView("/statistics/platform/statisticCenter");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	/**
	 * 运营总览之今日新增客户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/statisticTodayByType")
	public HttpResult<Map<String,Object>> statisticTodayByType(String type,String version){
		HttpResult<Map<String,Object>> httpResult = new HttpResult<Map<String,Object>>();
		UserInfo sessionuser = this.getSessionUser();
		if(null == sessionuser){
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setMsg(CommonConstant.OFF_LINE_INFO);
			return httpResult;
		}
		if(CommonUtil.isNull(version)){
			version="1";
		}
		Map<String,Object> map = statisticsService.statisticTodayByType(sessionuser,type,version);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(map);
		httpResult.setMsg("success");
		return httpResult;
	}
	/**
	 * 运营分析的各部门任务创建和任务办结统计
	 * @param startDate 统计的开始时间
	 * @param endDate 统计的结束时间
	 * @param taskDateTimeType 查询时间类型
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/task/statisticTaskForDep")
	public HttpResult<List<StatisticTaskVo>> statisticTaskForDep(String startDate,String endDate,String taskDateTimeType,String version){
		HttpResult<List<StatisticTaskVo>> httpResult = new HttpResult<List<StatisticTaskVo>>();
		UserInfo sessionuser = this.getSessionUser();
		if(null == sessionuser){
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setMsg(CommonConstant.OFF_LINE_INFO);
			return httpResult;
		}
		if(CommonUtil.isNull(version)){
			version="1";
		}
		//运营分析的各部门任务创建和任务办结统计
		List<StatisticTaskVo> listStatisticTaskVo = statisticsService.statisticTaskForDep(sessionuser, startDate, endDate,taskDateTimeType,version);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listStatisticTaskVo);
		httpResult.setMsg("success");
		return httpResult;
	}
	/**
	 * 运营分析的指定时间的任务办理情况
	 * @param startDate 统计的开始时间
	 * @param endDate 统计的结束时间
	 * @param taskDateTimeType 查询时间类型
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/task/statisticTaskTop")
	public HttpResult<List<StatisticTaskVo>> statisticTaskTop(String startDate,String endDate,String taskDateTimeType,String version) throws ParseException {
		HttpResult<List<StatisticTaskVo>> httpResult = new HttpResult<List<StatisticTaskVo>>();
		UserInfo sessionuser = this.getSessionUser();
		if(null == sessionuser){
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setMsg(CommonConstant.OFF_LINE_INFO);
			return httpResult;
		}
		if(CommonUtil.isNull(version)){
			version="1";
		}
		//运营分析的各部门任务创建和任务办结统计
		List<StatisticTaskVo> listStatisticTaskVo = statisticsService.statisticTaskTop(sessionuser, startDate, endDate,taskDateTimeType,version);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listStatisticTaskVo);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 运营分析之任务分类
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/task/statisticTaskGrade")
	public HttpResult<List<StatisticTaskVo>> statisticTaskGrade(String version) {
		HttpResult<List<StatisticTaskVo>> httpResult = new HttpResult<List<StatisticTaskVo>>();
		UserInfo sessionuser = this.getSessionUser();
		if(null == sessionuser){
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setMsg(CommonConstant.OFF_LINE_INFO);
			return httpResult;
		}
		if(CommonUtil.isNull(version)){
			version="1";
		}
		//运营分析之任务分类
		List<StatisticTaskVo> listStatisticTaskVo = statisticsService.statisticTaskGrade(sessionuser,version);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listStatisticTaskVo);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 运营分析之客户分类
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/crm/statisticCrmType")
	public HttpResult<List<StatisticCrmVo>> statisticCrmType() {
		HttpResult<List<StatisticCrmVo>> httpResult = new HttpResult<List<StatisticCrmVo>>();
		UserInfo sessionuser = this.getSessionUser();
		if(null == sessionuser){
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setMsg(CommonConstant.OFF_LINE_INFO);
			return httpResult;
		}
		//运营分析之客户分类
		List<StatisticCrmVo> listStatisticCrmVo = statisticsService.statisticCrmType(sessionuser);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listStatisticCrmVo);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 跳转任务统计界面
	 * @return
	 */
	@RequestMapping(value = "/platform/statisticTask")
	public ModelAndView statisticTask() {
		ModelAndView mav = new ModelAndView("/statistics/platform/statisticCenter");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		//当前年份
		String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		mav.addObject("nowYear",nowYear);
		return mav;
	}
	/**
	 * 分页查询任务统计
	 * @param statisticsVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/ajaxListStatisticTask")
	public Map<String, Object> ajaxListStatisticTask(StatisticTaskVo statisticsVo,Integer pageNum, Integer pageSize,String version) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		if(CommonUtil.isNull(version)){
			version = "1";
		}
		PageBean<StatisticTaskVo> pageBean = taskService.listPagedStatisticTask(userInfo,statisticsVo,version);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 跳转任务执行督办界面
	 * @return
	 */
	@RequestMapping(value = "/platform/task/statisticSupTask")
	public ModelAndView statisticSupTask(Task task) {
		ModelAndView mav = new ModelAndView("/statistics/platform/statisticCenter");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		mav.addObject("task", task);
		return mav;
	}
	/**
	 * 任务执行督办数据查询
	 * @param statisticsVo
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/ajaxListStatisticSupTask")
	public HttpResult<PageBean<JSONObject>>  ajaxListStatisticSupTask(Task task,
			Integer pageNum, Integer pageSize) {
		HttpResult<PageBean<JSONObject>> httpResult = new HttpResult<PageBean<JSONObject>>();
		UserInfo sessionuser = this.getSessionUser();
		if(null == sessionuser){
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setMsg(CommonConstant.OFF_LINE_INFO);
			return httpResult;
		}
		if(CommonUtil.isNull(task.getVersion())){
			task.setVersion("1");
		}
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<JSONObject> pageBean = statisticsService.listPagedStatisticSupTask(sessionuser,task);
		
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(pageBean);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 跳转客户统计界面
	 * @return
	 */
	@RequestMapping(value = "/platform/statisticCrm")
	public ModelAndView statisticCrm() {
		ModelAndView mav = new ModelAndView("/statistics/platform/statisticCenter");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		//当前年份
		String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		mav.addObject("nowYear",nowYear);
		
		
		return mav;
	}
	/**
	 * 客户统计
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/ajaxListStatisticCrm")
	public Map<String, Object> ajaxListStatisticCrm(StatisticCrmVo statisticsCrmVo,Integer pageNum, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		UserInfo userInfo = this.getSessionUser();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<StatisticCrmVo> pageBean = crmService.listPagedStatisticCrm(userInfo,statisticsCrmVo);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	
	
	/**
	 * 跳转业务出差费用统计界面
	 * @return
	 */
	@RequestMapping(value = "/platform/statisticFeeCrm")
	public ModelAndView statisticCrmFee() {
		ModelAndView mav = new ModelAndView("/statistics/platform/statisticCenter");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		//当前年份
		String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		mav.addObject("nowYear",nowYear);
		
		
		return mav;
	}
	/**
	 * 业务出差费用统计统计
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/ajaxListStatisticFeeCrm")
	public Map<String, Object> ajaxListStatisticCrmFee(StatisticFeeCrmVo statisticFeeCrmVo,
			Integer pageNum, Integer pageSize ) {
		Map<String, Object> map = new HashMap<String, Object>(3);
		UserInfo userInfo = this.getSessionUser();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<StatisticFeeCrmVo> pageBean = financialService.listPagedStatisticFeeCrm(userInfo,statisticFeeCrmVo);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		
		//当前年份
		String nowMonth = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM);
		map.put("nowMonth", nowMonth);
		return map;
	}
	
	/**
	 * 跳转项目出差费用统计界面
	 * @return
	 */
	@RequestMapping(value = "/platform/statisticFeeItem")
	public ModelAndView statisticItemFee() {
		ModelAndView mav = new ModelAndView("/statistics/platform/statisticCenter");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		//当前年份
		String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		mav.addObject("nowYear",nowYear);
		
		
		return mav;
	}
	/**
	 * 项目出差费用统计
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/ajaxListStatisticFeeItem")
	public Map<String, Object> ajaxListStatisticItemFee(StatisticFeeItemVo statisticFeeItemVo,
			Integer pageNum, Integer pageSize ) {
		Map<String, Object> map = new HashMap<String, Object>(3);
		UserInfo userInfo = this.getSessionUser();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<StatisticFeeItemVo> pageBean = financialService.listPagedStatisticFeeItem(userInfo,statisticFeeItemVo);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		
		//当前年份
		String nowMonth = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM);
		map.put("nowMonth", nowMonth);
		
		return map;
	}

	/**
	 * 跳转考勤统计界面
	 * @return
	 */
	@RequestMapping(value = "/platform/statisticAttence")
	public ModelAndView statisticAttence(HttpServletRequest request, String activityMenu, AttenceRecord attenceRecord){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView mav = new ModelAndView("/statistics/platform/statisticCenter");
		UserInfo curUser = this.getSessionUser();
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		//默认查询日期
		if(null == attenceRecord ||null == attenceRecord.getStartDate() || null == attenceRecord.getEndDate()){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// 获取前月的第一天
			// 获取当前日期
			Calendar first = Calendar.getInstance();
			first.add(Calendar.MONTH, -1);
			first.set(Calendar.DAY_OF_MONTH, 1);
			String firstDay = format.format(first.getTime());
			// 获取前月的最后一天
			Calendar last = Calendar.getInstance();
			last.set(Calendar.DAY_OF_MONTH, 0);
			String lastDay = format.format(last.getTime());
			attenceRecord.setStartDate(firstDay);
			attenceRecord.setEndDate(lastDay);
		}
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_ATTENCE);
		List<UserInfo> list = attenceService.attenceStatistics(curUser, attenceRecord,isForceIn);
		//筛选参数
		mav.addObject("listUserInfo",list);
		mav.addObject("userInfo",curUser);
		mav.addObject("isForceIn",isForceIn);
		mav.addObject("activityMenu",activityMenu);
		mav.addObject("attenceRecord",attenceRecord);
		mav.addObject("nowDate",nowDate);
		return mav;
	}

	/**
	 * 考勤统计
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/ajaxListStatisticAttence")
	public Map<String, Object> ajaxListStatisticAttence() {
		Map<String, Object> map = new HashMap<String, Object>(3);
//		UserInfo userInfo = this.getSessionUser();
		return map;
	}
	
	/**
	 * 跳转催办记录显示界面
	 * @return
	 */
	@RequestMapping(value = "/platform/statisticBusRemindPage")
	public ModelAndView statisticBusRemindPage(){
		ModelAndView mav = new ModelAndView("/statistics/platform/statisticCenter");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		
		String startDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM)+"-01";
		String endDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		
		mav.addObject("startDate", startDate);
		mav.addObject("endDate", endDate);
		
		return mav;
	}
	
	/**
	 * 异步分页查询催办统计信息
	 * @param busRemind 催办的查询条件
	 * @param pageNum 页码
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/platform/ajaxListPagedStatisticBusRemind")
	public HttpResult<PageBean<JSONObject>> listPagedStatisticBusRemind(BusRemind busRemind,
			Integer pageNum, Integer pageSize){
		
		HttpResult<PageBean<JSONObject>> httpResult = new HttpResult<PageBean<JSONObject>>();
		
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			return httpResult.error(CommonConstant.OFF_LINE_INFO);
		}
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<JSONObject> pageBean = statisticsService.listPagedStatisticBusRemind(sessionUser,busRemind);
		return httpResult.ok(pageBean);
	}
	
	/**
	 * 日报汇报统计
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/platform/dailyRepStatistics")
	public ModelAndView dailyRepStatistics(HttpServletRequest request,Daily daily,String chooseMonth){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/statistics/platform/statisticCenter");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo", curUser);
		if(CommonUtil.isNull(daily.getDailyDate())) {
			String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			daily.setDailyDate(nowDate);
		}
		//当前所在的年份
		String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		daily.setDailyYear(Integer.parseInt(nowYear));
		view.addObject("nowYear",nowYear);
		List<Daily> listDaily = statisticsService.listPagedDaily(daily,curUser);
		view.addObject("listDaily",listDaily);
		view.addObject("daily",daily);
		return view;
	}
	
	
	/**
	 * 周报汇报状态表
	 * @param weekReport
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/platform/statisticWeekReportPage")
	public ModelAndView listWeekRepStatistics(HttpServletRequest request,WeekReportPojo weekReport) throws ParseException{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		UserInfo userInfo = this.getSessionUser();
		weekReport.setComId(userInfo.getComId());
		ModelAndView view = new ModelAndView("/statistics/platform/statisticCenter");
		
		//周报列表
		List<WeekReport> weekReports = statisticsService.listWeekRepStatistics(weekReport,userInfo);
		view.addObject("weekReports", weekReports);
		view.addObject("userInfo",userInfo);
		//当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
		Integer nowWeekNum = DateTimeUtil.getWeekOfYear(nowDate,DateTimeUtil.c_yyyy_MM_dd_);
		
		view.addObject("nowWeekNum",nowWeekNum);
		view.addObject("weekReport",weekReport);
		return view;
	}
	
	/**
	 * 跳转需求落实监督界面
	 * @return
	 */
	@RequestMapping("/suverViewPlatform/statisticDemandProcess")
	public ModelAndView statisticDemandProcess(){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/statistics/platform/statisticCenter");
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 异步获取需求落实监督数据
	 * @param demandProcess
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/suverViewPlatform/ajaxListPagedStatisticDemandProcess")
	public HttpResult<PageBean<DemandProcess>> ajaxListPagedStatisticDemandProcess(DemandProcess demandProcess,
			Integer pageNum, Integer pageSize){
		HttpResult<PageBean<DemandProcess>> httpResult = new HttpResult<PageBean<DemandProcess>>();
		
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			return httpResult.error(CommonConstant.OFF_LINE_INFO);
		}
		pageNum = null == pageNum ? 0 : pageNum;
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<DemandProcess> pageBean = statisticsService.listPagedStatisticDemandProcess(sessionUser,demandProcess);
		return httpResult.ok(pageBean);
	}
	/**
	 * 跳转在建项目监督界面
	 * @return
	 */
	@RequestMapping("/suverViewPlatform/statisticItem")
	public ModelAndView statisticItem(){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/statistics/platform/statisticCenter");
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 异步获取在建项目监督数据
	 * @param item
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/suverViewPlatform/ajaxListPagedStatisticItem")
	public HttpResult<PageBean<Item>> ajaxListPagedStatisticItem(Item item,
			Integer pageNum, Integer pageSize){
		HttpResult<PageBean<Item>> httpResult = new HttpResult<PageBean<Item>>();
		
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			return httpResult.error(CommonConstant.OFF_LINE_INFO);
		}
		pageNum = null == pageNum ? 0 : pageNum;
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<Item> pageBean = statisticsService.listPagedStatisticItem(sessionUser,item);
		return httpResult.ok(pageBean);
	}
	/**
	 * 跳转任务执行监督界面
	 * @return
	 */
	@RequestMapping("/suverViewPlatform/statisticTask")
	public ModelAndView suverViewStatisticTask(){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/statistics/platform/statisticCenter");
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 异步获取任务执行监督数据
	 * @param task
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/suverViewPlatform/ajaxListPagedStatisticTask")
	public HttpResult<PageBean<Task>> ajaxListPagedStatisticTask(Task task,
			Integer pageNum, Integer pageSize){
		HttpResult<PageBean<Task>> httpResult = new HttpResult<PageBean<Task>>();
		
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			return httpResult.error(CommonConstant.OFF_LINE_INFO);
		}
		pageNum = null == pageNum ? 0 : pageNum;
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<Task> pageBean = statisticsService.listPagedStatisticTask(sessionUser,task);
		return httpResult.ok(pageBean);
	}
	/**
	 * 跳转个人工作监督界面
	 * @return
	 */
	@RequestMapping("/suverViewPlatform/statisticTaskByExecutor")
	public ModelAndView suverViewStatisticTaskByExecutor(){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/statistics/platform/statisticCenter");
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 异步获取个人工作监督数据
	 * @param task
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/suverViewPlatform/ajaxListPagedStatisticTaskByExecutor")
	public HttpResult<PageBean<Task>> ajaxListPagedStatisticTaskByExecutor(Task task,
			Integer pageNum, Integer pageSize){
		HttpResult<PageBean<Task>> httpResult = new HttpResult<PageBean<Task>>();
		
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			return httpResult.error(CommonConstant.OFF_LINE_INFO);
		}
		pageNum = null == pageNum ? 0 : pageNum;
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<Task> pageBean = statisticsService.listPagedStatisticTaskByExecutor(sessionUser,task);
		return httpResult.ok(pageBean);
	}
	
	
	
	
	
	
}