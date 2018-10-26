package com.westar.core.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Area;
import com.westar.base.model.Attention;
import com.westar.base.model.BusMapFlow;
import com.westar.base.model.Consume;
import com.westar.base.model.ConsumeType;
import com.westar.base.model.ConsumeUpfile;
import com.westar.base.model.Customer;
import com.westar.base.model.CustomerHandOver;
import com.westar.base.model.CustomerStage;
import com.westar.base.model.CustomerType;
import com.westar.base.model.DailyTalk;
import com.westar.base.model.DataDic;
import com.westar.base.model.Department;
import com.westar.base.model.FeeBudget;
import com.westar.base.model.FeeLoan;
import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.FeedBackInfo;
import com.westar.base.model.FeedBackType;
import com.westar.base.model.FormLayout;
import com.westar.base.model.FormMod;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.Item;
import com.westar.base.model.ItemHandOver;
import com.westar.base.model.ItemTalk;
import com.westar.base.model.JoinRecord;
import com.westar.base.model.Leave;
import com.westar.base.model.LinkMan;
import com.westar.base.model.MsgShare;
import com.westar.base.model.MsgShareTalk;
import com.westar.base.model.Organic;
import com.westar.base.model.OutLinkMan;
import com.westar.base.model.OverTime;
import com.westar.base.model.PointGPS;
import com.westar.base.model.Question;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.SpFlowHiStep;
import com.westar.base.model.SpFlowHuiQianInfo;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.SpFlowTalk;
import com.westar.base.model.SpFlowType;
import com.westar.base.model.StagedItem;
import com.westar.base.model.Task;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.model.WeekRepTalk;
import com.westar.base.model.WeekReport;
import com.westar.base.model.WeekViewer;
import com.westar.base.pojo.Area4App;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.IndexView;
import com.westar.base.pojo.ItemStagedInfo;
import com.westar.base.pojo.MobileInit;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.UpdateInfo;
import com.westar.base.pojo.UserAuth;
import com.westar.base.pojo.WeekReportPojo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.CusAccessObjectUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.service.MobileService;
import com.westar.core.web.DataDicContext;
import com.westar.core.web.PaginationContext;

/**
 * 移动端应用控制类 本层取名规则：业务名称+目标
 * 
 * @author lj
 * 
 */
@Controller
@RequestMapping("/mobileOrg")
public class MobileOrgController extends BaseController {

	@Autowired
	MobileService mobileService;

	@ModelAttribute("u_context")
	public UserInfo getUserContext(HttpServletRequest request) {
		return (UserInfo) request
				.getAttribute(CommonConstant.APP_USER_CONTEXT_KEY);
	}

	/**
	 * 验证登录人是否合理以及获取参加的所有团队集合
	 * 
	 * @param loginName
	 *            帐号
	 * @param password
	 *            密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/mobListOrg", method = RequestMethod.POST)
	public HttpResult<List<Organic>> mobListOrg(String loginName,
			String password) {
		// 返回对象
		HttpResult<List<Organic>> httpResult = mobileService.mobListOrg(
				loginName, password);
		return httpResult;
	}

	/**
	 * 取得用户详情
	 * 
	 * @param sessionUser
	 * @param userId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@ResponseBody
	@RequestMapping(value = "/changeOrg", method = RequestMethod.POST)
	public HttpResult<UserInfo> changeOrg(
			@ModelAttribute("u_context") UserInfo sessionUser,
			HttpServletRequest request, Integer comId, String account)
			throws IllegalAccessException, InvocationTargetException {
		// 返回对象
		HttpResult<UserInfo> httpResult = mobileService.changeOrg(sessionUser,
				request, comId, account);
		return httpResult;
	}

	/**
	 * 验证登录人是否合理以及获取参加的所有团队集合
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/orgList", method = RequestMethod.POST)
	public HttpResult<List<Organic>> orgList(
			@ModelAttribute("u_context") UserInfo userInfo, String account) {
		// 返回对象声明
		HttpResult<List<Organic>> httpResult = mobileService.orgList(userInfo,
				account);
		return httpResult;
	}

	/**
	 * 登入所选企业平台(移动端登入)
	 * 
	 * @param loginName
	 *            用户名
	 * @param password
	 *            密码
	 * @param comId
	 *            所需企业主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loginInOrg", method = RequestMethod.POST)
	public HttpResult<UserAuth> loginInOrg(String loginName, String password,
			String comId, String registrationId,String appSource) {
		// 返回对象声明
		HttpResult<UserAuth> httpResult = mobileService.loginInOrg(loginName,
				password, comId, registrationId,appSource);
		return httpResult;
	}

	/**
	 * 移动端根据本地文件登录验证
	 * 
	 * @param loginName
	 *            用户名
	 * @param password
	 *            MD5密码
	 * @param comId
	 *            所需企业主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loginInOrgByAuto", method = RequestMethod.POST)
	public HttpResult<UserAuth> loginInOrgByAuto(HttpServletRequest request,
			String loginName, String password, String comId,
			String registrationId,String appSource) {
		// 返回对象声明
		HttpResult<UserAuth> httpResult = mobileService.loginInOrgByAuto(
				request, loginName, password, comId, registrationId,appSource);
		return httpResult;
	}

	/**************************************** 以下主页消息页面初始化 *********************************************/
	/**
	 * 初始化主页提示信息
	 */
	@ResponseBody
	@RequestMapping(value = "/initMainMsgFr", method = RequestMethod.POST)
	public HttpResult<MobileInit<TodayWorks>> initMainMsgFr(
			@ModelAttribute("u_context") UserInfo userInfo) {
		// 返回对象声明
		HttpResult<MobileInit<TodayWorks>> httpResult = new HttpResult<MobileInit<TodayWorks>>();
		MobileInit<TodayWorks> initVo = mobileService.initMainMsgFr(userInfo);
		httpResult.setData(initVo);
		return httpResult;
	}

	/**
	 * 主页应用数据统计
	 * 
	 * @param userInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/initMainModFr", method = RequestMethod.POST)
	public HttpResult<Map<String, Integer>> initMainModFr(
			@ModelAttribute("u_context") UserInfo userInfo) {
		HttpResult<Map<String, Integer>> httpResult = mobileService
				.initMainModFr(userInfo);
		return httpResult;
	}
	
	
	
	
	

	/**************************************** 以下任务业务 ***************************************************/
	/**
	 * 获取当前登录人待办任务
	 * 
	 * @param userInfo
	 *            当前登录人信息
	 * @param pageNum
	 *            连续上啦加载的次数
	 * @param taskType
	 *            查看的任务类型
	 * @param state
	 *            任务状态筛选
	 * @param executor
	 *            执行人筛选
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskList", method = RequestMethod.POST)
	public HttpResult<List<Task>> taskList(@ModelAttribute("u_context") UserInfo userInfo, Integer pageNum,
			Integer pageSize, String taskType, int state, int executor, String taskName) {
		// 返回对象声明
		HttpResult<List<Task>> httpResult = new HttpResult<List<Task>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<Task> taskList = mobileService.taskList(userInfo, taskType, state,
				executor, taskName);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(taskList);
		return httpResult;
	}

	/**
	 * 取得任务子任务集合
	 * 
	 * @param userInfo
	 * @param pTaskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskListSons", method = RequestMethod.POST)
	public HttpResult<List<Task>> taskListSons(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pTaskId) {
		// 返回对象声明
		HttpResult<List<Task>> httpResult = new HttpResult<List<Task>>();
		List<Task> taskList = mobileService.taskListSons(userInfo, pTaskId);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(taskList);
		return httpResult;
	}

	/**
	 * 获取模块关联的任务
	 * 
	 * @param userInfo
	 * @param task
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/task4BusList", method = RequestMethod.POST)
	public HttpResult<List<Task>> task4BusList(
			@ModelAttribute("u_context") UserInfo userInfo, Task task,
			Integer pageNum,Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		// 返回对象声明
		HttpResult<List<Task>> httpResult = new HttpResult<List<Task>>();
		List<Task> taskList = mobileService.task4BusList(userInfo, task);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(taskList);
		return httpResult;
	}

	/**
	 * 获取权限范围下的任务列表
	 * 
	 * @param userInfo
	 * @param task
	 * @param pageNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskListForRelevance", method = RequestMethod.POST)
	public HttpResult<List<Task>> taskListForRelevance(
			@ModelAttribute("u_context") UserInfo userInfo, Task task,
			Integer pageNum,Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9 : pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		// 返回对象声明
		HttpResult<List<Task>> httpResult = new HttpResult<List<Task>>();
		List<Task> taskList = mobileService
				.listTaskForRelevance(userInfo, task);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(taskList);
		return httpResult;
	}

	/**
	 * 认领任务
	 * @param userInfo 当前操作人员
	 * @param taskId 任务主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/acceptTask",method = RequestMethod.POST)
	public HttpResult<String> acceptTask(@ModelAttribute("u_context") UserInfo userInfo,Integer taskId){

		HttpResult<String> httpResult = mobileService.acceptTask(userInfo,taskId);
		return httpResult;
	}

	/**
	 * 获取任务详情
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param taskId
	 *            任务主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskDetail", method = RequestMethod.POST)
	public HttpResult<Task> taskDetail(
			@ModelAttribute("u_context") UserInfo userInfo, Integer taskId) {
		HttpResult<Task> httpResult = mobileService
				.taskDetail(userInfo, taskId);
		return httpResult;
	}

	/**
	 * 任务名称修改
	 * 
	 * @param userInfo
	 *            当阿倩操作人员
	 * @param taskName
	 *            任务名称
	 * @param taskId
	 *            任务主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskNameUpdate", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> taskNameUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, Task task,
			Integer taskId) {
		task.setId(taskId);
		task.setComId(userInfo.getComId());
		HttpResult<Map<String, String>> httpResult = mobileService
				.taskNameUpdate(userInfo, task);
		return httpResult;
	}

	/**
	 * 修改任务关联
	 * 
	 * @param userInfo
	 * @param task
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskBusIdUpdate", method = RequestMethod.POST)
	public HttpResult<Object> taskBusIdUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, Task task,
			Integer taskId) {
		task.setId(taskId);
		task.setComId(userInfo.getComId());
		HttpResult<Object> httpResult = mobileService.taskBusIdUpdate(userInfo,
				task);
		return httpResult;
	}

	/**
	 * 修改任务的项目阶段
	 * 
	 * @param userInfo
	 * @param task
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskStageIdUpdate", method = RequestMethod.POST)
	public HttpResult<Object> taskStageIdUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, Task task,
			Integer taskId) {
		task.setId(taskId);
		task.setComId(userInfo.getComId());
		HttpResult<Object> httpResult = mobileService.taskStageIdUpdate(
				userInfo, task);
		return httpResult;
	}

	/**
	 * 任务说明修改
	 * 
	 * @param userInfo
	 *            当阿倩操作人员
	 * @param taskName
	 *            任务名称
	 * @param taskId
	 *            任务主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskRemarkUpdate", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> taskRemarkUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, Task task,
			Integer taskId) {
		task.setId(taskId);
		task.setComId(userInfo.getComId());
		HttpResult<Map<String, String>> httpResult = mobileService
				.taskRemarkUpdate(userInfo, task);
		return httpResult;
	}

	/**
	 * 修改任务共享人
	 * 
	 * @param userInfo
	 * @param shares
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskSharerUpdate", method = RequestMethod.POST)
	public HttpResult<Object> taskSharerUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, String shares,
			Integer taskId) {
		HttpResult<Object> httpResult = mobileService.taskSharerUpdate(
				userInfo, taskId, shares);
		return httpResult;
	}

	/**
	 * 删除任务的模块关联
	 * 
	 * @param userInfo
	 * @param taskId
	 *            任务主键
	 * @param busId
	 *            业务主键
	 * @param busType
	 *            业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskDelRelation", method = RequestMethod.POST)
	public HttpResult<Task> taskDelRelation(
			@ModelAttribute("u_context") UserInfo userInfo, Integer taskId,
			Integer busId, String busType) {
		HttpResult<Task> httpResult = mobileService.taskDelRelation(userInfo,
				taskId, busId, busType);
		return httpResult;
	}

	/**
	 * 任务进度汇报
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param task
	 *            任务
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskProgressReport", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> taskProgressReport(
			@ModelAttribute("u_context") UserInfo userInfo, Task task,
			Integer taskId) {
		task.setId(taskId);
		task.setComId(userInfo.getComId());
		HttpResult<Map<String, String>> httpResult = mobileService.taskProgressReport(userInfo, task);
		return httpResult;
	}

	/**
	 * 任务办理时限修改
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param task
	 *            任务
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskDealTimeLimitUpdate", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> taskDealTimeLimitUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, Task task,
			Integer taskId) {
		task.setId(taskId);
		task.setComId(userInfo.getComId());
		HttpResult<Map<String, String>> httpResult = mobileService
				.taskDealTimeLimitUpdate(userInfo, task);
		return httpResult;
	}

	/**
	 * 任务紧急度修改
	 * 
	 * @param userInfo
	 *            当阿倩操作人员
	 * @param task
	 *            任务
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskGradeUpdate", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> taskGradeUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, Task task,
			Integer taskId) {
		task.setId(taskId);
		task.setComId(userInfo.getComId());
		HttpResult<Map<String, String>> httpResult = mobileService
				.taskGradeUpdate(userInfo, task);
		return httpResult;
	}

	/**
	 * 任务委托
	 * 
	 * @param userInfo
	 * @param task
	 *            任务属性
	 * @param taskId
	 *            任务主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskNextExecutor", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> taskNextExecutor(
			@ModelAttribute("u_context") UserInfo userInfo, String taskStr, Task task,
			Integer taskId) {
		HttpResult<Map<String, String>> httpResult = mobileService.taskNextExecutor(userInfo, task,taskId,taskStr);
		return httpResult;
	}
	
	/**
	 * 任务完成后移交给负责人
	 * @param userInfo
	 * @param taskStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskTurnBack", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> taskTurnBack(
			@ModelAttribute("u_context") UserInfo userInfo, String taskStr) {
		HttpResult<Map<String, String>> httpResult = mobileService.taskTurnBack(userInfo,taskStr);
		return httpResult;
	}
	
	

	/**
	 * 模块关联任务发布
	 * 
	 * @param userInfo
	 * @param taskStr
	 *            任务属性配置
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskAddRelateMod", method = RequestMethod.POST)
	public HttpResult<Object> taskAddRelateMod(
			@ModelAttribute("u_context") UserInfo userInfo, String taskStr)
			throws Exception {
		HttpResult<Object> httpResult = mobileService.taskAdd(userInfo, taskStr);
		return httpResult;
	}

	/**
	 * 获取任务留言
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param taskId
	 *            任务主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskTalkList", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> taskTalkList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer taskId,
			Integer pageNum,Integer pageSize) {
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9 : pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		HttpResult<Map<String, String>> httpResult = mobileService
				.taskTalkList(userInfo, taskId);
		return httpResult;
	}

	/**
	 * 任务反馈留言
	 * 
	 * @param userInfo
	 *            操作用户信息
	 * @param taskId
	 *            任务主键
	 * @param pId
	 *            回复主键
	 * @param content
	 *            恢复内容
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskFeedBack", method = RequestMethod.POST)
	public HttpResult<Task> taskFeedBack(
			@ModelAttribute("u_context") UserInfo userInfo, Integer taskId,
			Integer pId, String content) throws Exception {
		HttpResult<Task> httpResult = mobileService.taskFeedBack(userInfo,
				taskId, pId, content);
		return httpResult;
	}

	/**
	 * 任务发布
	 * 
	 * @param userInfo
	 *            操作用户信息
	 * @param taskStr
	 *            任务配置信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/task/taskAdd", method = RequestMethod.POST)
	public HttpResult<Object> taskAdd(
			@ModelAttribute("u_context") UserInfo userInfo, String taskStr)
			throws Exception {
		HttpResult<Object> httpResult = mobileService
				.taskAdd(userInfo, taskStr);
		return httpResult;
	}

	/**************************************** 以下关注业务 ***************************************************/
	/**
	 * 获取关注类型数据集合
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param busType
	 *            需查询的数据类型
	 */
	@ResponseBody
	@RequestMapping(value = "/atten/attenList", method = RequestMethod.POST)
	public HttpResult<List<Attention>> attenList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pageNum,
			Integer pageSize, Attention attention) {
		// 返回对象声明
		HttpResult<List<Attention>> httpResult = new HttpResult<List<Attention>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<Attention> attenList = mobileService.attenList(userInfo, attention);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(attenList);
		return httpResult;
	}

	/**
	 * 数据关注
	 * 
	 * @param userInfo
	 *            当前操作用户信息
	 * @param busType
	 *            关注数据类型
	 * @param busId
	 *            关注数据主键
	 * @param attentionState
	 *            标记标识
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/atten/attenChange", method = RequestMethod.POST)
	public HttpResult<Object> attenChange(
			@ModelAttribute("u_context") UserInfo userInfo, String busType,
			Integer busId, Integer attentionState) {
		// 返回对象声明
		HttpResult<Object> httpResult = mobileService.attenChange(userInfo,
				busType, busId, attentionState);
		return httpResult;
	}

	/**************************************** 以下项目业务 ***************************************************/
	/**
	 * 获取项目数据集
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param pageNum
	 *            连续上拉次数
	 * @param itemType
	 *            数据列表类型
	 * @param selectedItemState
	 *            项目状态筛选
	 * @param owner
	 *            负责人主键
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemList", method = RequestMethod.POST)
	public HttpResult<List<Item>> itemList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pageNum,Integer pageSize,
			String itemType, int selectedItemState, int owner, String itemName) {
		// 返回对象声明
		HttpResult<List<Item>> httpResult = new HttpResult<List<Item>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<Item> itemList = mobileService.itemList(userInfo, itemType,
				selectedItemState, owner, itemName);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(itemList);
		return httpResult;
	}

	/**
	 * 取得项目子项目集合
	 * 
	 * @param userInfo
	 * @param pItemId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemListSons", method = RequestMethod.POST)
	public HttpResult<List<Item>> itemListSons(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pItemId) {
		// 返回对象声明
		HttpResult<List<Item>> httpResult = new HttpResult<List<Item>>();
		List<Item> itemList = mobileService.itemListSons(userInfo, pItemId);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(itemList);
		return httpResult;
	}

	/**
	 * 获取模块关联的项目
	 * 
	 * @param userInfo
	 * @param item
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/item4BusList", method = RequestMethod.POST)
	public HttpResult<List<Item>> item4BusList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer busId,
			Integer pageNum,Integer pageSize, String busType) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		// 返回对象声明
		HttpResult<List<Item>> httpResult = new HttpResult<List<Item>>();
		List<Item> itemList = mobileService.item4BusList(userInfo, busId,
				busType);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(itemList);
		return httpResult;
	}

	/**
	 * 获取权限范围下的项目列表
	 * 
	 * @param userInfo
	 * @param task
	 * @param pageNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemListForRelevance", method = RequestMethod.POST)
	public HttpResult<List<Item>> itemListForRelevance(
			@ModelAttribute("u_context") UserInfo userInfo, Item item,
			Integer pageNum,Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		// 返回对象声明
		HttpResult<List<Item>> httpResult = new HttpResult<List<Item>>();
		List<Item> itemList = mobileService
				.listItemForRelevance(userInfo, item);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(itemList);
		return httpResult;
	}

	/**
	 * 项目阶段用于选择
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param itemId
	 *            项目主键
	 * @param stagePid
	 *            阶段文件夹主键
	 * @param pageNum
	 *            当前页码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemStageListForRelevance", method = RequestMethod.POST)
	public HttpResult<List<ItemStagedInfo>> itemStageListForRelevance(
			@ModelAttribute("u_context") UserInfo userInfo, Integer itemId,
			Integer stagePid, Integer pageNum,Integer pageSize) {
		// 返回对象声明
		HttpResult<List<ItemStagedInfo>> httpResult = new HttpResult<List<ItemStagedInfo>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<ItemStagedInfo> itemList = mobileService
				.itemStageListForRelevance(userInfo, itemId, stagePid);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(itemList);
		return httpResult;
	}

	/**
	 * 项目阶段分页查询
	 * 
	 * @param userInfo
	 * @param pageNum
	 * @param itemType
	 * @param selectedItemState
	 * @param owner
	 * @param itemName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemStagPagedList", method = RequestMethod.POST)
	public HttpResult<List<ItemStagedInfo>> itemStagPagedList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer itemId,
			Integer realPid, String name, Integer pageNum,Integer pageSize) {
		// 返回对象声明
		HttpResult<List<ItemStagedInfo>> httpResult = new HttpResult<List<ItemStagedInfo>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<ItemStagedInfo> itemList = mobileService.itemStagPagedList(
				userInfo, itemId, realPid, name);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(itemList);
		return httpResult;
	}

	/**
	 * 获取项目的最新的一级项目阶段对象
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param itemId
	 *            项目主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemLatestStagedItem", method = RequestMethod.POST)
	public HttpResult<StagedItem> itemLatestStagedItem(
			@ModelAttribute("u_context") UserInfo userInfo, Integer itemId) {
		return mobileService.itemLatestStagedItem(userInfo, itemId);
	}

	/**
	 * 项目添加
	 * 
	 * @param userInfo
	 * @param itemStr
	 *            项目配置信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemAdd", method = RequestMethod.POST)
	public HttpResult<Object> itemAdd(
			@ModelAttribute("u_context") UserInfo userInfo, String itemStr)
			throws Exception {
		HttpResult<Object> httpResult = mobileService
				.itemAdd(userInfo, itemStr);
		return httpResult;
	}

	/**
	 * 添加关联项目或是分解项目
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param itemStr
	 *            项目配置信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemAddRelateMod", method = RequestMethod.POST)
	public HttpResult<Object> itemAddRelateMod(
			@ModelAttribute("u_context") UserInfo userInfo, String itemStr)
			throws Exception {
		HttpResult<Object> httpResult = mobileService
				.itemAdd(userInfo, itemStr);
		return httpResult;
	}

	/**
	 * 项目详情
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param itemId
	 *            项目主键
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemDetail", method = RequestMethod.POST)
	public HttpResult<Item> itemDetail(
			@ModelAttribute("u_context") UserInfo userInfo, Integer itemId)
			throws Exception {
		HttpResult<Item> httpResult = mobileService
				.itemDetail(userInfo, itemId);
		return httpResult;
	}

	/**
	 * 项目留言讨论数据集
	 * 
	 * @param userInfo
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemTalkList", method = RequestMethod.POST)
	public HttpResult<List<ItemTalk>> itemTalkList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer itemId,
			Integer onMoreNum) throws Exception {
		HttpResult<List<ItemTalk>> httpResult = mobileService.itemTalkList(
				userInfo, itemId, onMoreNum);
		return httpResult;
	}

	/**
	 * 项目移交
	 * 
	 * @param userInfo
	 * @param itemHandOver
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemNextOwner", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> itemNextOwner(
			@ModelAttribute("u_context") UserInfo userInfo,
			ItemHandOver itemHandOver) {
		itemHandOver.setComId(userInfo.getComId());
		HttpResult<Map<String, String>> httpResult = mobileService
				.itemNextOwner(userInfo, itemHandOver);
		return httpResult;
	}

	/**
	 * 项目属性更新
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param itemId
	 *            项目主键
	 * @param attrType
	 *            更新的属性类型
	 * @param newAttrContent
	 *            新的属性值
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemAttrUpdate", method = RequestMethod.POST)
	public HttpResult<Item> itemAttrUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, Integer itemId,
			String attrType, String newAttrContent) {
		HttpResult<Item> httpResult = null;
		try {
			httpResult = mobileService.itemAttrUpdate(userInfo, itemId,
					attrType, newAttrContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpResult;
	}

	/**
	 * 解除项目关联
	 * 
	 * @param userInfo
	 * @param itemId
	 *            项目主键
	 * @param busId
	 *            关联主键
	 * @param busType
	 *            关联模块
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/item/itemDelRelation", method = RequestMethod.POST)
	public HttpResult<Object> itemDelRelation(
			@ModelAttribute("u_context") UserInfo userInfo, Integer itemId,
			Integer busId, String busType) {
		HttpResult<Object> httpResult = mobileService.itemDelRelation(userInfo,
				itemId, busId, busType);
		return httpResult;
	}

	/**************************************** 以下客户业务 ***************************************************/
	/**
	 * 获取客户数据集合
	 * 
	 * @param userInfo
	 *            当前登录人信息
	 * @param pageNum
	 *            连续上啦加载的次数
	 * @param crmType
	 *            查看的客户类型
	 * @param owner
	 *            负责人筛选
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmList", method = RequestMethod.POST)
	public HttpResult<List<Customer>> crmList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pageNum,Integer pageSize,
			String crmType, int customerTypeId, int areaId, int owner,
			String crmName) {
		// 返回对象声明
		HttpResult<List<Customer>> httpResult = new HttpResult<List<Customer>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<Customer> crmList = mobileService.crmList(userInfo, crmType,
				customerTypeId, areaId, owner, crmName);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(crmList);
		return httpResult;
	}

	/**
	 * 获取区域数据集
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmAreaList", method = RequestMethod.POST)
	public HttpResult<List<Area>> crmAreaList(
			@ModelAttribute("u_context") UserInfo userInfo) {
		// 返回对象声明
		HttpResult<List<Area>> httpResult = new HttpResult<List<Area>>();
		List<Area> crmAreaList = mobileService.crmAreaList(userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(crmAreaList);
		return httpResult;
	}

	/**
	 * 获取区域数据集(省中包含市)
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmAreaTreeList", method = RequestMethod.POST)
	public HttpResult<List<Area4App>> crmAreaTreeList(
			@ModelAttribute("u_context") UserInfo userInfo) {
		// 返回对象声明
		HttpResult<List<Area4App>> httpResult = new HttpResult<List<Area4App>>();
		List<Area4App> crmAreaList = mobileService.crmAreaTreeList(userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(crmAreaList);
		return httpResult;
	}

	/**
	 * 取得区域列表
	 * 
	 * @param pageNum
	 *            当前页码
	 * @param areaPId
	 *            区域父Id
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmAreaListForRelevance", method = RequestMethod.POST)
	public HttpResult<List<Area>> crmAreaListForRelevance(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pageNum,Integer pageSize,
			Integer areaPId) {

		// 返回对象声明
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		// 返回对象声明
		HttpResult<List<Area>> httpResult = new HttpResult<List<Area>>();
		List<Area> crmAreaList = mobileService.crmAreaListForRelevance(
				userInfo, areaPId);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(crmAreaList);
		return httpResult;
	}

	/**
	 * 获取客户列表用于选择
	 * 
	 * @param userInfo
	 * @param crm
	 * @param pageNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmListForRelevance", method = RequestMethod.POST)
	public HttpResult<List<Customer>> crmListForRelevance(
			@ModelAttribute("u_context") UserInfo userInfo, Customer crm,
			Integer pageNum,Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		// 返回对象声明
		HttpResult<List<Customer>> httpResult = new HttpResult<List<Customer>>();
		List<Customer> crmList = mobileService.listCrmForRelevance(userInfo,
				crm);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(crmList);
		return httpResult;
	}

	/**
	 * 获取客户类型数据集
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmTypeList", method = RequestMethod.POST)
	public HttpResult<List<CustomerType>> crmTypeList(
			@ModelAttribute("u_context") UserInfo userInfo) {
		// 返回对象声明
		HttpResult<List<CustomerType>> httpResult = new HttpResult<List<CustomerType>>();
		List<CustomerType> crmTypeList = mobileService.crmTypeList(userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(crmTypeList);
		return httpResult;
	}
	/**
	 * 获取客户阶段数据集
	 * @param userInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmStageList", method = RequestMethod.POST)
	public HttpResult<List<CustomerStage>> crmStageList(
			@ModelAttribute("u_context") UserInfo userInfo) {
		// 返回对象声明
		HttpResult<List<CustomerStage>> httpResult = new HttpResult<List<CustomerStage>>();
		List<CustomerStage> crmStageList = mobileService.crmStageList(userInfo);
		return httpResult.ok(crmStageList);
	}

	/**
	 * 新增客户
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param customerStr
	 *            客户配置信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmAdd", method = RequestMethod.POST)
	public HttpResult<Object> crmAdd(
			@ModelAttribute("u_context") UserInfo userInfo, String customerStr)
			throws Exception {
		HttpResult<Object> httpResult = mobileService.crmAdd(userInfo,customerStr);
		return httpResult;
	}

	/**
	 * 客户详情查看
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param crmId
	 *            客户对象主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmDetail", method = RequestMethod.POST)
	public HttpResult<Customer> crmDetail(
			@ModelAttribute("u_context") UserInfo userInfo, Integer crmId) {
		HttpResult<Customer> httpResult = mobileService.crmDetail(userInfo,
				crmId);
		return httpResult;
	}

	/**
	 * 客户反馈记录数据集
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param crmId
	 *            客户对象主键
	 * @param onMoreNum
	 *            连续上拉次数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmMsgList", method = RequestMethod.POST)
	public HttpResult<List<FeedBackInfo>> crmMsgList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer crmId,
			Integer onMoreNum) {
		HttpResult<List<FeedBackInfo>> httpResult = mobileService.crmMsgList(
				userInfo, crmId, onMoreNum);
		return httpResult;
	}

	/**
	 * 客户反馈类型数据集
	 * 
	 * @param userInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmFeedBackTypeList", method = RequestMethod.POST)
	public HttpResult<List<FeedBackType>> crmFeedBackTypeList(
			@ModelAttribute("u_context") UserInfo userInfo) {

		HttpResult<List<FeedBackType>> httpResult = mobileService
				.crmFeedBackTypeList(userInfo);
		return httpResult;
	}

	/**
	 * 客户移交
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param customerHandOver
	 *            客户移交数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmHandOver", method = RequestMethod.POST)
	public HttpResult<String> crmHandOver(
			@ModelAttribute("u_context") UserInfo userInfo,
			CustomerHandOver customerHandOver) {
		customerHandOver.setFromUser(userInfo.getId());
		customerHandOver.setComId(userInfo.getComId());
		HttpResult<String> httpResult = mobileService.crmHandOver(userInfo,
				customerHandOver);
		return httpResult;
	}

	/**
	 * 
	 * 客户类型更新
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param customer
	 *            更新对象
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmTypeIdUpdate", method = RequestMethod.POST)
	public HttpResult<Customer> crmTypeIdUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, Integer crmId,
			Integer customerTypeId) {
		HttpResult<Customer> httpResult = null;
		try {
			httpResult = mobileService.crmTypeIdUpdate(userInfo, crmId,
					customerTypeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpResult;
	}

	/**
	 * 客户属性更新，更新都写在一个方法内，通过attrType区分更新那个属性（客户类型更新除外）。
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param crmId
	 *            客户主键
	 * @param attrType
	 *            属性类型
	 * @param newAttrContent
	 *            新属性值
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmAttrUpdate", method = RequestMethod.POST)
	public HttpResult<Customer> crmAttrUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, Integer crmId,
			String attrType, String newAttrContent) {
		HttpResult<Customer> httpResult = null;
		try {
			httpResult = mobileService.crmAttrUpdate(userInfo, crmId, attrType,
					newAttrContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpResult;
	}

	/**
	 * 添加联系人
	 * 
	 * @param userInfo
	 * @param linkMan
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/crm/crmAddLinkMan", method = RequestMethod.POST)
	public HttpResult<LinkMan> crmAddLinkMan(
			@ModelAttribute("u_context") UserInfo userInfo, LinkMan linkMan) {
		HttpResult<LinkMan> httpResult = null;
		httpResult = mobileService.crmAddLinkMan(userInfo, linkMan);
		return httpResult;
	}

	/**************************************** 以下周报业务 ***************************************************/

	/**
	 * 查询周报信息
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param depId
	 *            所选部门
	 * @param reporterId
	 *            汇报人员和主键
	 * @param pageNum
	 *            当前分页
	 * @param weekerType
	 *            汇报范围
	 * @param weekName
	 *            周报名称筛选关键字
	 */
	@ResponseBody
	@RequestMapping(value = "/weekReport/weekRepList", method = RequestMethod.POST)
	public HttpResult<List<WeekReport>> weekRepList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer depId,
			Integer reporterId, Integer pageNum,Integer pageSize, String weekerType,
			String weekName) throws ParseException {
		// 返回对象声明
		HttpResult<List<WeekReport>> httpResult = new HttpResult<List<WeekReport>>();

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		WeekReportPojo weekReport = new WeekReportPojo();
		// 设置汇报人员
		weekReport.setWeekerId(0 == reporterId ? null : reporterId);
		// 设置查询下属
		weekReport.setWeekerType(weekerType);
		// 若是没有下属
		if (userInfo.getCountSub() <= 0 && null != weekerType
				&& "1".equals(weekerType)) {
			weekReport.setWeekerType(null);
		}
		weekReport.setDepId(0 == depId ? null : depId);
		// 周报名称筛选关键字
		weekReport.setWeekName(weekName);
		// 取得信息分享的列表
		List<WeekReport> msgShareList = mobileService.weekRepList(userInfo,
				weekReport);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(msgShareList);
		return httpResult;
	}

	/**
	 * 获取周报详情
	 * 
	 * @param userInfo
	 * @param reporterId
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/weekReport/weekRepDetail", method = RequestMethod.POST)
	public HttpResult<WeekReport> weekRepDetail(
			@ModelAttribute("u_context") UserInfo userInfo, Integer reporterId)
			throws ParseException {
		// 一次加载行数
		PaginationContext.setPageSize(9);
		HttpResult<WeekReport> httpResult = mobileService.weekRepDetail(
				userInfo, reporterId);
		return httpResult;
	}

	/**
	 * 获取周报留言数据集
	 * 
	 * @param userInfo
	 *            当前操作用户信息
	 * @param reporterId
	 *            周报主键
	 * @param pageNum
	 *            连续加载次数
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/weekReport/weekRepTalkList", method = RequestMethod.POST)
	public HttpResult<List<WeekRepTalk>> weekRepTalkList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer reporterId,
			Integer pageNum,Integer pageSize) throws ParseException {
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		HttpResult<List<WeekRepTalk>> httpResult = mobileService
				.weekRepTalkList(userInfo, reporterId);
		return httpResult;
	}

	/**
	 * 周报汇报配置初始化
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param startDate
	 *            汇报返回的开始日期
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/weekReport/weekRepInit", method = RequestMethod.POST)
	public HttpResult<WeekReport> weekRepInit(
			@ModelAttribute("u_context") UserInfo userInfo, String startDate)
			throws Exception {
		HttpResult<WeekReport> httpResult = mobileService.weekRepInit(userInfo,
				startDate);
		return httpResult;
	}

	/**
	 * 
	 * 周报汇报
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param weekReportStr
	 *            周报汇报详情
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/weekReport/weekRepAdd", method = RequestMethod.POST)
	public HttpResult<Object> weekRepAdd(
			@ModelAttribute("u_context") UserInfo userInfo, String weekReportStr)
			throws Exception {
		HttpResult<Object> httpResult = mobileService.weekRepAdd(userInfo,
				weekReportStr);
		return httpResult;
	}

	/**
	 * 周报查看人员范围
	 * 
	 * @param userInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/weekReport/listWeekViewer", method = RequestMethod.POST)
	public HttpResult<List<WeekViewer>> listWeekViewer(
			@ModelAttribute("u_context") UserInfo userInfo) {
		HttpResult<List<WeekViewer>> httpResult = mobileService
				.listWeekViewer(userInfo);
		return httpResult;

	}

	/**
	 * 更新周报审定人员
	 * 
	 * @param userInfo
	 *            当前操作员
	 * @param weekViewerStr
	 *            周报查看人员审定json对象
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/weekReport/updateWeekViewers", method = RequestMethod.POST)
	public HttpResult<String> updateWeekViewers(
			@ModelAttribute("u_context") UserInfo userInfo, String weekViewerStr) {
		HttpResult<String> httpResult = mobileService.updateWeekViewers(
				userInfo, weekViewerStr);
		return httpResult;

	}

	/**
	 * 删除周报的查看人员
	 * 
	 * @param userInfo
	 * @param weekViewer
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/weekReport/delWeekViewer", method = RequestMethod.POST)
	public HttpResult<String> delWeekViewer(
			@ModelAttribute("u_context") UserInfo userInfo,
			WeekViewer weekViewer) {
		HttpResult<String> httpResult = mobileService.delWeekViewer(userInfo,
				weekViewer);
		return httpResult;

	}

	/**************************************** 以下文档业务 ***************************************************/

	/**
	 * 查询附件信息
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param modTypes
	 *            模块类型
	 * @param fileScope
	 *            范围
	 * @param pageNum
	 *            页码
	 * @param fileTypes
	 *            附件类型
	 * @param classifyId
	 *            所在文件夹主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/file/fileList", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> fileList(
			@ModelAttribute("u_context") UserInfo userInfo, String modTypes,
			String fileScope, Integer pageNum,Integer pageSize, String fileTypes,
			Integer classifyId, String fileName) {

		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		// 查询企业用户
		Map<String, String> result = mobileService.fileList(userInfo, modTypes,
				fileScope, pageNum, fileTypes, classifyId, fileName);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(result);

		return httpResult;
	}
	
	/**
	 * 添加文件夹
	 * @param userInfo
	 * @param FileClassifyStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/file/addFileClassify", method = RequestMethod.POST)
	public HttpResult<Void> addDir(
			@ModelAttribute("u_context") UserInfo userInfo,String fileClassifyStr) {
		HttpResult<Void> httpResult = mobileService.addFileClassify(userInfo,fileClassifyStr);
		return httpResult;
	}
	
	/**
	 * 往文件夹里面添加文件
	 * @param userInfo 当前操作人员
	 * @param fileDetailStr 文件json 信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/file/addFileDetail", method = RequestMethod.POST)
	public HttpResult<Void> addFileDetail(
			@ModelAttribute("u_context") UserInfo userInfo,String fileDetailStr) {
		HttpResult<Void> httpResult = mobileService.addFileDetail(userInfo,fileDetailStr);
		return httpResult;
	}
	
	
	
	
	/**
	 * 根目录数据
	 * @param userInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/file/fileTopList", method = RequestMethod.POST)
	public HttpResult<String> fileTopList(@ModelAttribute("u_context") UserInfo userInfo) {
		HttpResult<String> httpResult = new HttpResult<String>();
		// 一次加载行数
		PaginationContext.setPageSize(4);
		// 列表数据起始索引位置
		PaginationContext.setOffset(0 * PaginationContext.getPageSize());

		
		// 查询企业用户
		JSONArray result = mobileService.fileTopList(userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(result.toString());
		return httpResult;
	}
	
	
	

	/**
	 * 获取文档详情
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param fileId
	 *            文档主键
	 * @param busSource
	 *            数据来源
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/file/fileDetail", method = RequestMethod.POST)
	public HttpResult<Upfiles> fileDetail(
			@ModelAttribute("u_context") UserInfo userInfo, Integer fileId,
			String busSource) {
		return mobileService.fileDetail(userInfo, fileId, busSource);

	}

	/**************************************** 以下问答业务 ***************************************************/
	/**
	 * 我要提问
	 * 
	 * @param userInfo
	 * @param quesStr
	 *            问题配置
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/qas/quesAdd", method = RequestMethod.POST)
	public HttpResult<Object> quesAdd(
			@ModelAttribute("u_context") UserInfo userInfo, String quesStr,
			String idType) throws Exception {
		HttpResult<Object> httpResult = mobileService.quesAdd(userInfo,
				quesStr, idType);
		return httpResult;
	}

	/**
	 * 问答详情
	 * 
	 * @param userInfo
	 *            操作人员信息
	 * @param questionId
	 *            问题主键
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/qas/quesDetail", method = RequestMethod.POST)
	public HttpResult<Question> quesDetail(
			@ModelAttribute("u_context") UserInfo userInfo, Integer questionId)
			throws Exception {
		HttpResult<Question> httpResult = mobileService.quesDetail(userInfo,
				questionId);
		return httpResult;
	}

	/**
	 * 问答回答与回复留言
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param questionId
	 *            问题主键
	 * @param answerId
	 *            答案主键
	 * @param pId
	 *            回复父级主键
	 * @param content
	 *            留言内容
	 * @param filesStr
	 *            附件集合JSON对象字符串
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/qas/quesLeaveMsg", method = RequestMethod.POST)
	public HttpResult<Object> quesLeaveMsg(
			@ModelAttribute("u_context") UserInfo userInfo, int questionId,
			int answerId, int pId, String content, String filesStr)
			throws Exception {
		HttpResult<Object> httpResult = mobileService.quesLeaveMsg(userInfo,
				questionId, answerId, pId, content, filesStr);
		return httpResult;
	}

	/**************************************** 以下投票业务 ***************************************************/
	/**************************************** 以下日程业务 ***************************************************/
	/**************************************** 以下会议业务 ***************************************************/
	/**************************************** 以下闹钟业务 ***************************************************/
	/**************************************** 以下审批业务 ***************************************************/
	/**************************************** 以下待办业务 ***************************************************/
	/**
	 * 获取个人待办工作数据集
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param pageNum
	 *            连续上拉次数
	 * @param busType
	 *            工作类型
	 */
	@ResponseBody
	@RequestMapping(value = "/todayWorks/jobList", method = RequestMethod.POST)
	public HttpResult<List<TodayWorks>> jobList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pageNum,Integer pageSize,
			String busType, String jobName) {
		// 返回对象声明
		HttpResult<List<TodayWorks>> httpResult = new HttpResult<List<TodayWorks>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<TodayWorks> jobList = mobileService.jobList(userInfo, busType,
				jobName);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(jobList);
		return httpResult;
	}
	
	/**
	 * 分页查询所有的消息通知
	 * @param userInfo 当前操作人员
	 * @param pageNum 页码
	 * @param pageSize 分页数
	 * @param busTypeListStr 业务类型的类型集合
	 * @param jobName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/todayWorks/listPagedTodayWorks", method = RequestMethod.POST)
	public HttpResult<PageBean<TodayWorks>> listPagedTodayWorks(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pageNum,Integer pageSize,
			String busTypeListStr, String jobName) {
		// 返回对象声明
		HttpResult<PageBean<TodayWorks>> httpResult = new HttpResult<PageBean<TodayWorks>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<TodayWorks> jobList = mobileService.listPagedTodayWorksForApp(userInfo, busTypeListStr,jobName);
		return httpResult.ok(jobList);
	}
	
	/**
	 * 查询人员指定模块的未读消息
	 * @param userInfo
	 * @param busTypeListStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/main/countNoRead", method = RequestMethod.POST)
	public HttpResult<PageBean<TodayWorks>> countNoRead(
			@ModelAttribute("u_context") UserInfo userInfo,String busTypeListStr) {
		HttpResult<PageBean<TodayWorks>> httpResult = new HttpResult<PageBean<TodayWorks>>();
		Integer pageNum = 0;
		Integer pageSize = 9;
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<TodayWorks> noReadNum = mobileService.countNoReadByUser(userInfo,busTypeListStr);
		return httpResult.ok(noReadNum);
	}
	
	

	/**
	 * 闹钟事件详情
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param toDoId
	 *            todayWork主键
	 * @param busId
	 *            数据主键
	 * @param busType
	 *            数据类型主键
	 * @param isClock
	 *            是否闹钟时间标识符
	 * @param clockId
	 *            闹钟主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/todayWorks/jobDetails", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> jobDetails(
			@ModelAttribute("u_context") UserInfo userInfo, Integer toDoId,
			Integer busId, String busType, Integer isClock, Integer clockId) {
		HttpResult<Map<String, String>> httpResult = mobileService.jobDetails(
				userInfo, toDoId, busId, busType, isClock, clockId);
		return httpResult;
	}

	/**************************************** 以下通讯录 ***************************************************/

	/**
	 * 取得通讯录的所有成员
	 * 
	 * @param userInfo
	 * @param pageNum
	 *            页码数
	 * @param uName
	 *            用户姓名
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/userListAll", method = RequestMethod.POST)
	public HttpResult<List<UserInfo>> userListAll(
			@ModelAttribute("u_context") UserInfo sessionUser, Integer pageNum,
			String uName) {
		HttpResult<List<UserInfo>> httpResult = mobileService.userListAll(
				sessionUser, pageNum, uName);
		return httpResult;
	}
	
	/**
	 * 取得通讯录所有成员
	 * 
	 * @param userInfo
	 * @param uName
	 *            用户姓名
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/listUserInfos", method = RequestMethod.POST)
	public HttpResult<List<UserInfo>> listUserInfos(
			@ModelAttribute("u_context") UserInfo sessionUser,String uName) {
		HttpResult<List<UserInfo>> httpResult = mobileService.listUserInfos(
				sessionUser, uName);
		return httpResult;
	}
	

	/**
	 * 取得常用人员
	 * 
	 * @param userInfo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/usedUserList", method = RequestMethod.POST)
	public HttpResult<List<UserInfo>> usedUserList(
			@ModelAttribute("u_context") UserInfo userInfo)
			throws IllegalAccessException, InvocationTargetException {
		HttpResult<List<UserInfo>> httpResult = mobileService
				.usedUserList(userInfo);
		return httpResult;
	}

	/**
	 * 取得常用人员前十
	 * 
	 * @param userInfo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/usedUserList4Ten", method = RequestMethod.POST)
	public HttpResult<List<UserInfo>> usedUserList4Ten(
			@ModelAttribute("u_context") UserInfo userInfo)
			throws IllegalAccessException, InvocationTargetException {
		HttpResult<List<UserInfo>> httpResult = mobileService
				.usedUserList4Ten(userInfo);
		return httpResult;
	}

	/**
	 * 取得用户详情
	 * 
	 * @param userInfo
	 * @param userId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/getUserInfo", method = RequestMethod.POST)
	public HttpResult<UserInfo> getUserInfo(
			@ModelAttribute("u_context") UserInfo userInfo, Integer userId)
			throws IllegalAccessException, InvocationTargetException {
		return mobileService.getUserInfo(userInfo, userId);
	}

	/**
	 * 验证是否有上级
	 * 
	 * @param userInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/checkLeader", method = RequestMethod.POST)
	public HttpResult<List<ImmediateSuper>> checkLeader(
			@ModelAttribute("u_context") UserInfo userInfo) {
		return mobileService.checkLeader(userInfo);
	}

	/**
	 * 修改用户名称和密码
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param userNameParam
	 *            新的用户名称
	 * @param passwordParam
	 *            新的用户密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/updateUserPassAndName", method = RequestMethod.POST)
	public HttpResult<UserInfo> updateUserPassAndName(
			@ModelAttribute("u_context") UserInfo userInfo,
			String userNameParam, String passwordParam) {
		HttpResult<UserInfo> httpResult = mobileService.updateUserPassAndName(
				userInfo, userNameParam, passwordParam);
		return httpResult;
	}

	/**
	 * 上级设定
	 * 
	 * @param userInfo
	 * @param leaderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/updateImmediateSuper", method = RequestMethod.POST)
	public HttpResult<String> updateImmediateSuper(
			@ModelAttribute("u_context") UserInfo userInfo, Integer leaderId)
			throws Exception {
		return mobileService.updateImmediateSuper(userInfo, leaderId);
	}

	/**
	 * 修改人员的属性
	 * 
	 * @param userInfo
	 * @param attrType
	 *            属性名称
	 * @param attrVal
	 *            属性值
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "userInfo/updateUserAttr", method = RequestMethod.POST)
	public HttpResult<UserInfo> updateUserAttr(
			@ModelAttribute("u_context") UserInfo userInfo, String attrType,
			String attrVal, HttpServletRequest request) {
		String authKey = (String) request.getAttribute("authKey");
		// 返回对象声明
		return mobileService.updateUserAttr(userInfo, attrType, attrVal,
				authKey);
	}

	/**
	 * 修改用户的部门信息
	 * 
	 * @param userInfo
	 *            当前操作员
	 * @param depIdParam
	 *            部门主键
	 * @param depNameParam
	 *            部门名称
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/updateUserDep", method = RequestMethod.POST)
	public HttpResult<String> updateUserDep(
			@ModelAttribute("u_context") UserInfo userInfo, Integer depIdParam,
			String depNameParam) throws Exception {
		return mobileService.updateUserDep(userInfo, depIdParam, depNameParam);
	}

	/**
	 * 验证账号并且发送验证码
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param noticeType
	 *            账号类型
	 * @param account
	 *            账号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/sendPassYzmByNewAccount", method = RequestMethod.POST)
	public HttpResult<String> sendPassYzmByNewAccount(
			@ModelAttribute("u_context") UserInfo userInfo, String noticeType,
			String account) {
		return mobileService.sendPassYzmByNewAccount(userInfo, noticeType,
				account);
	}

	/**
	 * 设置用户手机号
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param account
	 *            账号
	 * @param yzm
	 *            验证码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/updateUserMovePhone", method = RequestMethod.POST)
	public HttpResult<String> updateUserMovePhone(
			@ModelAttribute("u_context") UserInfo userInfo,
			HttpServletRequest request, String account, String yzm) {
		return mobileService.updateUserMovePhone(request, userInfo, account,
				yzm);
	}

	/**
	 * 取得通讯录的部门信息
	 * 
	 * @param userInfo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@ResponseBody
	@RequestMapping(value = "/dep/depListAll", method = RequestMethod.POST)
	public HttpResult<List<Department>> depListAll(
			@ModelAttribute("u_context") UserInfo userInfo)
			throws IllegalAccessException, InvocationTargetException {

		HttpResult<List<Department>> httpResult = new HttpResult<List<Department>>();
		// 设置查询条件
		Department dep = new Department();
		dep.setComId(userInfo.getComId());
		dep.setParentId(-1);

		// 查询企业部门(需要查询部门成员)
		List<Department> listDep = mobileService.listDepAll(dep, null);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listDep);

		return httpResult;
	}

	/**
	 * 取得通讯录的部门信息
	 * 
	 * @param userInfo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@ResponseBody
	@RequestMapping(value = "/dep/depTreeList", method = RequestMethod.POST)
	public HttpResult<List<Department>> depTreeList(
			@ModelAttribute("u_context") UserInfo userInfo)
			throws IllegalAccessException, InvocationTargetException {

		HttpResult<List<Department>> httpResult = new HttpResult<List<Department>>();
		// 设置查询条件
		Department dep = new Department();
		dep.setComId(userInfo.getComId());
		dep.setParentId(-1);

		// 查询企业部门（不需要查询部门成员）
		List<Department> listDep = mobileService.listDepAll(dep, "tree");
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listDep);

		return httpResult;
	}

	/**
	 * 通讯录个人分组
	 * 
	 * @param userInfo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@ResponseBody
	@RequestMapping(value = "/selfGrp/selfGrpListAll", method = RequestMethod.POST)
	public HttpResult<List<SelfGroup>> selfGrpListAll(
			@ModelAttribute("u_context") UserInfo userInfo)
			throws IllegalAccessException, InvocationTargetException {

		HttpResult<List<SelfGroup>> httpResult = new HttpResult<List<SelfGroup>>();
		// 设置查询条件
		SelfGroup selfGroup = new SelfGroup();
		selfGroup.setComId(userInfo.getComId());
		selfGroup.setOwner(userInfo.getId());

		// 查询企业部门
		List<SelfGroup> listGrp = mobileService.listGrpAll(userInfo, selfGroup);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listGrp);

		return httpResult;
	}

	/**************************************** 信息分享 ***************************************************/
	/**
	 * 获取消息未读总数
	 * 
	 * @param userInfo
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/msgShare/countNoRead", method = RequestMethod.POST)
	public HttpResult<Integer> countNoRead(
			@ModelAttribute("u_context") UserInfo userInfo)
			throws ParseException {
		// 返回对象声明
		HttpResult<Integer> httpResult = new HttpResult<Integer>();
		Integer nums = mobileService.countNoRead(userInfo.getComId(),
				userInfo.getId());

		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(nums);
		return httpResult;
	}

	/**
	 * 获取信息分享列表
	 * 
	 * @param userInfo
	 * @param busType
	 *            模块类型
	 * @param userId
	 *            分享人员主键
	 * @param minId
	 *            当前分享最小主键
	 * @param creatorType
	 *            范围
	 * @param content
	 *            内容
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/msgShare/msgShareList", method = RequestMethod.POST)
	public HttpResult<List<MsgShare>> msgShareList(
			@ModelAttribute("u_context") UserInfo userInfo, String busType,
			Integer userId, Integer minId, String creatorType, String content)
			throws ParseException {
		// 返回对象声明
		HttpResult<List<MsgShare>> httpResult = new HttpResult<List<MsgShare>>();

		// 设置条件
		MsgShare msgShare = new MsgShare();

		// 企业号
		msgShare.setComId(userInfo.getComId());
		msgShare.setCreator(0 == userId ? null : userId);

		msgShare.setContent(content);

		msgShare.setCreatorType(creatorType);
		// 若是没有下属
		if (userInfo.getCountSub() <= 0 && null != creatorType
				&& "1".equals(creatorType)) {
			msgShare.setCreatorType(null);

		}

		// 数组集合化
		List<String> modList = null;
		if (!StringUtil.isBlank(busType)) {
			if (ConstantInterface.TYPE_SHARE.equals(busType)) {
				busType = "1";
			}
			modList = new ArrayList<String>();
			modList.add(busType);
		}
		// 所有模块 // 是信息分享 // 是信息分享 // 是投票分享 // 是文档分享 // 是问答分享
		if (null == modList 
				|| modList.size() == 0
				|| modList.indexOf(ConstantInterface.TYPE_SHARE) >= 0
				|| modList.indexOf("1") >= 0
				|| modList.indexOf(ConstantInterface.TYPE_VOTE) >= 0
				|| modList.indexOf(ConstantInterface.TYPE_FILE) >= 0
				|| modList.indexOf(ConstantInterface.TYPE_QUES) >= 0
		) {
			// 一次加载行数
			PaginationContext.setPageSize(9);
			// 取得信息分享的列表
			List<MsgShare> msgShareList = mobileService.msgShareList(userInfo.getComId(),
					userInfo.getId(), 9, minId, msgShare, modList);
			// 遍历后替换表情
			List<MsgShare> msgs = new ArrayList<MsgShare>();
			if (null != msgShareList) {
				for (MsgShare obj : msgShareList) {
					obj.setContent(StringUtil.cutString(obj.getContent(), 240,null));
					msgs.add(obj);
				}
			}

			httpResult.setCode(HttpResult.CODE_OK);
			httpResult.setData(msgs);
			return httpResult;
		} else {
			List<MsgShare> msgs = new ArrayList<MsgShare>();
			httpResult.setCode(HttpResult.CODE_OK);
			httpResult.setData(msgs);
			return httpResult;
		}

	}

	/**
	 * 
	 * 分享信息
	 * 
	 * @param userInfo
	 * @param keyAndTypeStrs
	 *            分享范围类型与主键
	 * @param content
	 *            分享描述
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/msgShare/msgShareAdd", method = RequestMethod.POST)
	public HttpResult<Object> msgShareAdd(
			@ModelAttribute("u_context") UserInfo userInfo,
			String keyAndTypeStrs, String content) throws ParseException {
		// 返回对象声明
		HttpResult<Object> httpResult = mobileService.msgShareAdd(userInfo,
				keyAndTypeStrs, content);
		return httpResult;
	}

	/**
	 * 获取分享信息详情
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param msgId
	 *            分享信息主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/msgShare/msgShareDetail", method = RequestMethod.POST)
	public HttpResult<MsgShare> msgShareDetail(
			@ModelAttribute("u_context") UserInfo userInfo, Integer msgId) {
		HttpResult<MsgShare> httpResult = mobileService.msgShareDetail(
				userInfo, msgId);
		return httpResult;
	}

	/**
	 * 
	 * 获取分享信息讨论数据集
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param msgId
	 *            分享信息主键
	 * @param pageNum
	 *            连续上拉加载次数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/msgShare/msgShareTalkList", method = RequestMethod.POST)
	public HttpResult<List<MsgShareTalk>> msgShareTalkList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer msgId,
			Integer pageNum,Integer pageSize) {
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		HttpResult<List<MsgShareTalk>> httpResult = mobileService
				.msgShareTalkList(userInfo, msgId);
		return httpResult;
	}
	/**
	 * 日报留言
	 * @param userInfo
	 * @param msgId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/msgShare/dailyTalkList", method = RequestMethod.POST)
	public HttpResult<List<DailyTalk>> dailyTalkList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer msgId,
			Integer pageNum,Integer pageSize) {
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		HttpResult<List<DailyTalk>> httpResult = mobileService
				.dailyTalkList(userInfo, msgId);
		return httpResult;
	}

	/**************************************** 重用 ***************************************************/
	/**
	 * 留言
	 * 
	 * @param userInfo
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            业务主键
	 * @param pId
	 *            留言父级主键
	 * @param content
	 *            留言类容
	 * @param tagKey
	 *            标签主键
	 * @param filesStr
	 *            附件集合JSON对象字符串
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/leaveMsg", method = RequestMethod.POST)
	public HttpResult<Object> leaveMsg(
			@ModelAttribute("u_context") UserInfo userInfo, String busType,
			Integer busId, Integer pId, String content, Integer tagKey,
			String filesStr) throws Exception {
		HttpResult<Object> httpResult = mobileService.leaveMsg(userInfo,
				busType, busId, pId, content, tagKey, filesStr);
		return httpResult;
	}

	/**
	 * 数据附件集获取
	 * 
	 * @param userInfo
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            业务主键
	 * @param pageNum
	 *            连续上拉加载次数
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/fileList", method = RequestMethod.POST)
	public HttpResult<List<Upfiles>> fileList(
			@ModelAttribute("u_context") UserInfo userInfo, String busType,
			Integer busId, Integer pageNum,Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		HttpResult<List<Upfiles>> httpResult = mobileService.fileList(userInfo,
				busType, busId);
		return httpResult;
	}

	/**
	 * 业务日志数据集
	 * 
	 * @param userInfo
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            业务主键
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/logList", method = RequestMethod.POST)
	public HttpResult<List<UserInfo>> logList(
			@ModelAttribute("u_context") UserInfo userInfo, String busType,
			Integer busId, Integer pageNum,Integer pageSize) throws ParseException {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		HttpResult<List<UserInfo>> httpResult = mobileService.logList(userInfo,
				busType, busId);
		return httpResult;
	}

	/**
	 * 获取业务查看记录数据集
	 * 
	 * @param userInfo
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            业务主键
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/recordList", method = RequestMethod.POST)
	public HttpResult<List<ViewRecord>> recordList(
			@ModelAttribute("u_context") UserInfo userInfo, String busType,
			Integer busId, Integer pageNum,Integer pageSize) throws ParseException {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		HttpResult<List<ViewRecord>> httpResult = mobileService.recordList(
				userInfo, busType, busId);
		return httpResult;
	}

	/**
	 * 获取紧急度数据集
	 * 
	 * @param userInfo
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/getListDataDic", method = RequestMethod.POST)
	public HttpResult<List<DataDic>> getListDataDic(
			@ModelAttribute("u_context") UserInfo userInfo, String type)
			throws ParseException {
		HttpResult<List<DataDic>> httpResult = new HttpResult<List<DataDic>>();
		List<DataDic> listTreeDataDic = DataDicContext.getInstance()
				.listTreeDataDicByType(type);
		httpResult.setData(listTreeDataDic);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 数据查看权限验证
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param busType
	 *            数据类型
	 * @param busId
	 *            数据主键
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/authorCheck", method = RequestMethod.POST)
	public HttpResult<Boolean> authorCheck(
			@ModelAttribute("u_context") UserInfo userInfo, String busType,
			Integer busId) throws Exception {
		HttpResult<Boolean> httpResult = mobileService.authorCheck(userInfo,
				busType, busId);
		return httpResult;
	}

	/**
	 * 获取分享范围数据集（个人分组）
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/scopeGroups", method = RequestMethod.POST)
	public HttpResult<List<SelfGroup>> scopeGroups(
			@ModelAttribute("u_context") UserInfo userInfo)
			throws ParseException {
		HttpResult<List<SelfGroup>> httpResult = mobileService
				.scopeGroups(userInfo);
		return httpResult;
	}

	/**
	 * 上次使用过的分组
	 * 
	 * @param userInfo
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/usedGroups", method = RequestMethod.POST)
	public HttpResult<List<SelfGroup>> usedGroups(
			@ModelAttribute("u_context") UserInfo userInfo)
			throws ParseException {
		HttpResult<List<SelfGroup>> httpResult = mobileService
				.usedGroups(userInfo);
		return httpResult;
	}

	/**
	 * 删除模块数据
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param busId
	 *            业务主键
	 * @param busType
	 *            业务
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/delBusData", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> delBusData(
			@ModelAttribute("u_context") UserInfo userInfo, Integer busId,
			String busType) throws Exception {
		HttpResult<Map<String, String>> httpResult = mobileService.delBusData(
				userInfo, busId, busType);
		return httpResult;
	}

	/**
	 * 模块状态修改
	 * 
	 * @param userInfo当前啊操作人员
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            模块主键
	 * @param state
	 *            状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/modStateUpdate", method = RequestMethod.POST)
	public HttpResult<Map<String, String>> modStateUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, String busType,
			Integer busId, Integer state) {
		HttpResult<Map<String, String>> httpResult = mobileService
				.modStateUpdate(userInfo, busType, busId, state);
		return httpResult;
	}

	/**
	 * 设置模块父级关联
	 * 
	 * @param userInfo
	 * @param modId
	 *            模块主键
	 * @param modPId
	 *            父级主键
	 * @param modType
	 *            模块类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/modParentUpdate", method = RequestMethod.POST)
	public HttpResult<Object> modParentUpdate(
			@ModelAttribute("u_context") UserInfo userInfo, Integer modId,
			Integer modPId, String modType) {
		HttpResult<Object> httpResult = mobileService.modParentUpdate(userInfo,
				modId, modPId, modType);
		return httpResult;
	}

	/**
	 * 移交记录
	 * 
	 * @param userInfo当前啊操作人员
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            模块主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/comm/flowRecordList", method = RequestMethod.POST)
	public HttpResult<List<FlowRecord>> flowRecordList(
			@ModelAttribute("u_context") UserInfo userInfo, String busType,
			Integer busId) {
		HttpResult<List<FlowRecord>> httpResult = mobileService.flowRecordList(
				userInfo, busType, busId);
		return httpResult;
	}

	/**************************************** 以下数据索引 ***************************************************/
	/**
	 * 全文检索
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param busType
	 *            数据模块主键
	 * @param searchStr
	 *            检索关键字
	 * @return
	 * @throws ParseException
	 * @throws org.apache.lucene.queryparser.classic.ParseException
	 * @throws IOException
	 * @throws CorruptIndexException
	 */
	@ResponseBody
	@RequestMapping(value = "/index/searchIndexInCom", method = RequestMethod.POST)
	public HttpResult<List<IndexView>> searchIndexInCom(
			@ModelAttribute("u_context") UserInfo userInfo, String busType,
			String searchStr) throws ParseException, CorruptIndexException,
			IOException, org.apache.lucene.queryparser.classic.ParseException {
		HttpResult<List<IndexView>> httpResult = mobileService
				.searchIndexInCom(userInfo, busType, searchStr);
		return httpResult;
	}

	/**************************************** 以下文件上传 ***************************************************/
	/**
	 * 文件上传
	 * 
	 * @param userInfo
	 *            当前操作人
	 * @param request
	 * @return
	 * @throws ParseException
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws org.apache.lucene.queryparser.classic.ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/upload/addFile", method = RequestMethod.POST)
	public Upfiles addFile(@ModelAttribute("u_context") UserInfo userInfo,
			HttpServletRequest request) {
		Upfiles httpResult = mobileService.addFile(userInfo, request);
		return httpResult;
	}

	/**
	 * 读取app版本信息
	 * 
	 * @param userInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUpdateInfo", method = RequestMethod.POST)
	public HttpResult<UpdateInfo> getUpdateInfo(
			@ModelAttribute("u_context") UserInfo userInfo) {
		HttpResult<UpdateInfo> httpResult = new HttpResult<UpdateInfo>();
		UpdateInfo updateInfo = new UpdateInfo();
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("android_version.xml");
		SAXReader reader = new SAXReader();
		reader.setEncoding("GB2312");
		org.dom4j.Document document;
		try {
			document = reader.read(inputStream);
			// 读取节点信息
			Node version = document.selectSingleNode("//info/version");
			Node versionName = document.selectSingleNode("//info/versionName");
			Node url = document.selectSingleNode("//info/url");
			Node description = document.selectSingleNode("//info/description");
			// 设值
			updateInfo.setVersion(Integer.parseInt(version.getStringValue()));
			updateInfo.setVersionName(versionName.getStringValue());
			updateInfo.setUrl(url.getStringValue());
			// 取得描述
			String disStr = description.getStringValue();
			// 删除前后空格
			disStr = StringUtil.trim(disStr);
			// 替换换行符
			// disStr = disStr.replaceAll("\n\t\t", "\r\n");
			updateInfo.setDescription(disStr);

			httpResult.setData(updateInfo);
			httpResult.setCode(HttpResult.CODE_OK);
		} catch (DocumentException e) {

			httpResult.setCode(-1);
			httpResult.setMsg("读取版本信息失败");
		}
		return httpResult;
	}

	/*************************** 审核用户开始 ***********************************/

	/**
	 * 取得加入记录
	 * 
	 * @param account
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getJoinRecord", method = RequestMethod.POST)
	public HttpResult<JoinRecord> getJoinRecord(
			@ModelAttribute("u_context") UserInfo userInfo, Integer busId) {
		HttpResult<JoinRecord> httpResult = mobileService.getJoinRecord(
				userInfo, busId);
		return httpResult;
	}

	/**
	 * 审核加入记录
	 * 
	 * @param account
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkJoinRecord", method = RequestMethod.POST)
	public HttpResult<String> checkJoinRecord(HttpServletRequest request,
			@ModelAttribute("u_context") UserInfo userInfo, String account,
			String checkState, Integer busId) {
		// 操作IP
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		HttpResult<String> httpResult = mobileService.checkJoinRecord(userInfo,
				account, checkState, busId, optIP);
		return httpResult;
	}

	/*************************** 审核用户结束 ***********************************/
	/*************************** 注册功能开始 ***********************************/

	/**
	 * 判断账号是否存在
	 * 
	 * @param account
	 *            账号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/checkAccount", method = RequestMethod.POST)
	public HttpResult<UserInfo> registCheckAccount(String account) {
		HttpResult<UserInfo> httpResult = new HttpResult<UserInfo>();
		// 判断账号是否存在
		UserInfo user = mobileService.registCheckAccount(account);
		httpResult.setData(user);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 检测账号密码
	 * 
	 * @param account
	 * @param passWord
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/checkPasswd", method = RequestMethod.POST)
	public HttpResult<String> registCheckPasswd(String account, String passWord) {
		HttpResult<String> httpResult = new HttpResult<String>();
		// 判断账号是否存在
		String status = mobileService.registCheckPasswd(account, passWord);
		httpResult.setData(status);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 发送验证码
	 * 
	 * @param account
	 *            账号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/sendYzm", method = RequestMethod.POST)
	public HttpResult<String> registSendYzm(String account) {
		HttpResult<String> httpResult = new HttpResult<String>();
		// 判断账号是否存在
		mobileService.registSendYzm(account);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 检测验证输入是否正确
	 * 
	 * @param account
	 * @param passYzm
	 * @param funType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/checkYzm", method = RequestMethod.POST)
	public HttpResult<String> registCheckYzm(String account, String passYzm,
			Integer funType) {
		HttpResult<String> httpResult = new HttpResult<String>();
		// 检测验证输入是否正确
		String status = mobileService.registCheckYzm(account, passYzm, funType);
		httpResult.setData(status);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 创建团队
	 * 
	 * @param account
	 *            账号
	 * @param password
	 *            密码
	 * @param orgName
	 *            企业名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/createOrg", method = RequestMethod.POST)
	public HttpResult<UserAuth> registCreateOrg(HttpServletRequest request,
			String account, String userName, String password, String orgName) {
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		HttpResult<UserAuth> httpResult = mobileService.registCreateorg(
				account, userName, password, orgName, optIP);
		return httpResult;
	}

	/**
	 * 申请加入或取消申请
	 * 
	 * @param account
	 *            账号
	 * @param comId
	 *            企业名称
	 * @param applyState
	 *            申请状态 0申请 1取消
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/applyInOrg", method = RequestMethod.POST)
	public HttpResult<String> registApplyInOrg(String account, String userName,
			String password, Integer comId, Integer applyState) {
		HttpResult<String> httpResult = new HttpResult<String>();
		// 检测验证输入是否正确
		String status = mobileService.registApplyInOrg(account, comId,
				userName, password, applyState);
		httpResult.setData(status);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 模糊查询企业名称
	 * 
	 * @param account
	 *            账号
	 * @param searchName
	 *            查询名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/searchOrgList", method = RequestMethod.POST)
	public HttpResult<List<Organic>> searchOrgList(String account,
			String searchName) {
		HttpResult<List<Organic>> httpResult = new HttpResult<List<Organic>>();
		// 一次加载行数
		PaginationContext.setPageSize(8);
		List<Organic> orgList = mobileService
				.searchOrgList(account, searchName);
		httpResult.setData(orgList);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 列出已加入的企业
	 * 
	 * @param account
	 *            账号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/listUserAllOrg", method = RequestMethod.POST)
	public HttpResult<List<Organic>> listUserAllOrg(String account) {
		HttpResult<List<Organic>> httpResult = new HttpResult<List<Organic>>();
		// 一次加载行数
		List<Organic> orgList = mobileService.listUserAllOrg(account);
		httpResult.setData(orgList);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 同意加入团队
	 * 
	 * @param account
	 *            账号
	 * @param comId
	 *            团队号
	 * @param confirmCode
	 *            确认码
	 * @param registrationId
	 *            激光推送标识
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/agreeInOrg", method = RequestMethod.POST)
	public HttpResult<UserAuth> agreeInOrg(HttpServletRequest request,
			String account, Integer comId, String confirmCode,
			String registrationId,String appSource) {
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		// 返回对象声明
		HttpResult<UserAuth> httpResult = mobileService.agreeInOrg(account,
				comId, confirmCode, registrationId,appSource, optIP);
		return httpResult;
	}

	/**
	 * 拒绝加入团队
	 * 
	 * @param account
	 * @param comId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/rejectInOrg", method = RequestMethod.POST)
	public HttpResult<String> rejectInOrg(String account, Integer comId) {
		// 返回对象声明
		HttpResult<String> httpResult = mobileService.rejectInOrg(account,
				comId);
		return httpResult;
	}

	/*************************** 注册功能结束 ***********************************/
	/*************************** 找回密码开始 ***********************************/
	/**
	 * 修改密码
	 * 
	 * @param account
	 *            需修改的账户
	 * @param password
	 *            新密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registe/updatePassword", method = RequestMethod.POST)
	public HttpResult<String> updatePassword(String account, String password) {
		// 通过帐号修改账户密码
		HttpResult<String> httpResult = mobileService.updatePassword(account,
				password);
		return httpResult;
	}

	/*************************** 找回密码结束 ***********************************/

	/*************************** GPS ***********************************/

	/**
	 * 保存打卡位置
	 * 
	 * @param userInfo
	 * @param pointGPSStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/GPS/addPointGPS", method = RequestMethod.POST)
	public HttpResult<String> addPointGPS(
			@ModelAttribute("u_context") UserInfo userInfo, String pointGPSStr) {
		// 保存打卡位置
		HttpResult<String> httpResult = mobileService.addPointGPS(userInfo,
				pointGPSStr);
		return httpResult;
	}

	/**
	 * 取得打卡位置
	 * 
	 * @param userInfo
	 * @param pointGPSId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/GPS/getPointGPS", method = RequestMethod.POST)
	public HttpResult<PointGPS> getPointGPS(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pointGPSId) {
		// 取得打卡位置
		HttpResult<PointGPS> httpResult = mobileService.getPointGPS(userInfo,
				pointGPSId);
		return httpResult;
	}

	/*************************** GPS ***********************************/
	/*************************** 审批 ***********************************/
	/**
	 * 分类获取团队流程
	 * 
	 * @param userInfo
	 * @param pointGPSId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/flowDesign/listSpFlowTypeOfFlowModel", method = RequestMethod.POST)
	public HttpResult<List<SpFlowType>> listSpFlowTypeOfFlowModel(
			@ModelAttribute("u_context") UserInfo userInfo) {
		// 分类获取团队流程
		HttpResult<List<SpFlowType>> httpResult = mobileService
				.listSpFlowTypeOfFlowModel(userInfo);
		return httpResult;
	}

	/**
	 * 发起流程
	 * 
	 * @param spBusId
	 *            流程模板主键
	 * @param flowState
	 *            流程状态0发起，此时spBusId为模板主键，否则为流程实例化主键
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/getSpInstance", method = RequestMethod.POST)
	public HttpResult<SpFlowInstance> getSpInstance(
			@ModelAttribute("u_context") UserInfo userInfo, Integer spBusId,
			Integer flowState) {
		// 发起流程
		HttpResult<SpFlowInstance> httpResult = mobileService.startSpFlow(
				userInfo, spBusId, flowState);
		return httpResult;
	}

	/**
	 * 验证步骤配置信息
	 * 
	 * @param actInstaceId
	 *            activity实例化主键
	 * @param instanceId
	 *            流程实例化主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/checkStepCfg", method = RequestMethod.POST)
	public HttpResult<String> updateUserMovePhone(
			@ModelAttribute("u_context") UserInfo userInfo,
			String actInstaceId, Integer instanceId) {
		return mobileService.checkStepCfg(userInfo, actInstaceId, instanceId);
	}
	
	/**
	 * 用于下一步骤信息选择
	 * @param userInfo
	 * @param instanceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/querySpFlowNextStepInfo", method = RequestMethod.POST)
	public HttpResult<SpFlowHiStep> querySpFlowNextStepInfo(
			@ModelAttribute("u_context") UserInfo userInfo,Integer instanceId) {
		return mobileService.querySpFlowNextStepInfo(userInfo,instanceId);
	}
	
	

	/**
	 * 保存表单数据
	 * 
	 * @param userInfo
	 * @param formDataStr
	 *            表单数据
	 * @param saveType
	 *            保存类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/addFormData", method = RequestMethod.POST)
	public HttpResult<String> addFormData(
			@ModelAttribute("u_context") UserInfo userInfo, String formDataStr,
			String saveType) {
		// 发起流程
		HttpResult<String> httpResult = mobileService.addFormData(userInfo,
				formDataStr, saveType);
		return httpResult;
	}

	/**
	 * 如果流程步骤有直属上级审批配置，则验证当前操作人是否设置了直属上级
	 * 
	 * @param userInfo
	 * @param flowId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/haveSetDirectLeader", method = RequestMethod.POST)
	public HttpResult<String> haveSetDirectLeader(
			@ModelAttribute("u_context") UserInfo userInfo, Integer flowId) {
		// 发起流程
		HttpResult<String> httpResult = mobileService.haveSetDirectLeader(
				userInfo, flowId);
		return httpResult;
	}

	/**
	 * 拾取流程
	 * 
	 * @param userInfo
	 * @param instanceId
	 *            流程实例主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/pickSpFlow", method = RequestMethod.POST)
	public HttpResult<String> pickSpFlow(
			@ModelAttribute("u_context") UserInfo userInfo, Integer instanceId) {
		// 发起流程
		HttpResult<String> httpResult = mobileService.pickSpFlow(userInfo,
				instanceId);
		return httpResult;
	}
	/**
	 * 会签操作
	 * @param userInfo 当前操作人员
	 * @param instanceId 流程部署主键
	 * @param modFormStepDataStr 会签参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/updateSpHuiQian", method = RequestMethod.POST)
	public HttpResult<String> updateSpHuiQian(
			@ModelAttribute("u_context") UserInfo userInfo, Integer instanceId,String modFormStepDataStr) {
		// 会签操作
		HttpResult<String> httpResult = mobileService.initSpHuiQian(userInfo,
				instanceId,modFormStepDataStr);
		return httpResult;
	}
	
	/**
	 * 审批会签配置
	 * @param userInfo 当前操作人员
	 * @param spFlowHuiQianInfoStr 会签信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/querySpFlowHuiQianInfo", method = RequestMethod.POST)
	public HttpResult<SpFlowHuiQianInfo> querySpFlowHuiQianInfo(
			@ModelAttribute("u_context") UserInfo userInfo, Integer instanceId) {
		//审批会签配置
		HttpResult<SpFlowHuiQianInfo> httpResult = mobileService.querySpFlowHuiQianInfo(userInfo,instanceId);
		return httpResult;
	}
	/**
	 * 会签操作
	 * @param userInfo 当前操作人员
	 * @param spFlowHuiQianInfoStr 会签信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/initEndHuiQian", method = RequestMethod.POST)
	public HttpResult<String> initEndHuiQian(
			@ModelAttribute("u_context") UserInfo userInfo, String spFlowHuiQianInfoStr) {
		// 会签操作
		HttpResult<String> httpResult = mobileService.initEndHuiQian(userInfo,spFlowHuiQianInfoStr);
		return httpResult;
	}
	/**
	 * 审批转办
	 * @param userInfo 当前操作人员
	 * @param instanceId 流程实例化主键
	 * @param actInstanceId 流程部署主键
	 * @param newAssignerId 流程办理人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/updateSpInsAssign", method = RequestMethod.POST)
	public HttpResult<String> updateSpInsAssign(
			@ModelAttribute("u_context") UserInfo userInfo, Integer instanceId,String actInstanceId,
			Integer newAssignerId) {
		// 会签操作
		HttpResult<String> httpResult = mobileService.updateSpInsAssign(userInfo,instanceId,actInstanceId,newAssignerId);
		return httpResult;
	}
	/**
	 * 分页查询审批
	 * 
	 * @param userInfo
	 *            当前操作员
	 * @param pageNum
	 *            当前分页数
	 * @param spType
	 *            审批类型
	 * @param flowState
	 *            审批状态
	 * @param creator
	 *            发起人
	 * @param flowName
	 *            流程名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/spFlowList", method = RequestMethod.POST)
	public HttpResult<List<SpFlowInstance>> spFlowList(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pageNum,Integer pageSize,
			String spScopeType, SpFlowInstance instance) {
		// 返回对象声明
		HttpResult<List<SpFlowInstance>> httpResult = new HttpResult<List<SpFlowInstance>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<SpFlowInstance> spList = mobileService.spFlowList(userInfo,
				instance, spScopeType);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(spList);
		return httpResult;
	}

	/**
	 * 获取已执行的审批步骤集合
	 * 
	 * @param instanceId
	 *            流程实例主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/spListHistorySpStep", method = RequestMethod.POST)
	public HttpResult<List<SpFlowHiStep>> spListHistorySpStep(
			@ModelAttribute("u_context") UserInfo userInfo, Integer instanceId) {
		// 发起流程
		HttpResult<List<SpFlowHiStep>> httpResult = mobileService
				.listHistorySpStep(userInfo, instanceId);
		return httpResult;
	}
	/**
	 * 审批留言查询
	 * @param userInfo 当前操作人员
	 * @param instanceId 审批流程主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/listSpFlowTalk", method = RequestMethod.POST)
	public HttpResult<List<SpFlowTalk>> listSpFlowTalk(
			@ModelAttribute("u_context") UserInfo userInfo, Integer instanceId){
		// 发起流程
		HttpResult<List<SpFlowTalk>> httpResult = mobileService
				.listSpFlowTalk(userInfo, instanceId);
		return httpResult;
	}
	
	/**
	 * 添加审批留言
	 * @param userInfo
	 * @param spFlowTalk
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workFlow/addSpFlowTalk", method = RequestMethod.POST)
	public HttpResult<SpFlowTalk> addSpFlowTalk(
			@ModelAttribute("u_context") UserInfo userInfo, String spFlowTalkStr){
		//添加审批留言
		HttpResult<SpFlowTalk> httpResult = mobileService
				.addSpFlowTalk(userInfo, spFlowTalkStr);
		return httpResult;
	}
	
	/**
	 * 分页查询自定义流程表单
	 * 
	 * @param userInfo
	 *            当前操作员
	 * @param pageNum
	 *            分页数
	 * @param formMod
	 *            表单详情
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/form/formListForSelect", method = RequestMethod.POST)
	public HttpResult<List<FormMod>> formListForSelect(
			@ModelAttribute("u_context") UserInfo userInfo, Integer pageNum,Integer pageSize,
			FormMod formMod) {
		// 返回对象声明
		HttpResult<List<FormMod>> httpResult = new HttpResult<List<FormMod>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<FormMod> spList = mobileService.formListForSelect(userInfo,
				formMod);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(spList);
		return httpResult;
	}

	/**
	 * 表单预览
	 * 
	 * @param userInfo
	 *            当前操作员
	 * @param formModId
	 *            表单主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/form/formViewById", method = RequestMethod.POST)
	public HttpResult<FormLayout> formView(
			@ModelAttribute("u_context") UserInfo userInfo, Integer formModId) {
		// 返回对象声明
		HttpResult<FormLayout> httpResult = new HttpResult<FormLayout>();
		FormLayout formMod = mobileService.getFormModbyId(userInfo, formModId);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(formMod);
		return httpResult;
	}

	/*************************** 审批 ***********************************/
	/*************************** 时间计算 ***********************************/
	/**
	 * 时间段时间计算
	 * 
	 * @param userInfo
	 * @param dateS
	 *            开始时间
	 * @param dateE
	 *            结束时间
	 * @param calTimeType
	 *            计算类型 1 工作时间、2 非工作时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/festMod/calDateTime", method = RequestMethod.POST)
	public HttpResult<String> calDateTime(
			@ModelAttribute("u_context") UserInfo userInfo, String dateS,
			String dateE, String calTimeType) {
		// 返回对象声明
		HttpResult<String> httpResult = new HttpResult<String>();
		String dateHours = mobileService.calDateTime(userInfo, dateS, dateE,
				calTimeType);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(dateHours);
		return httpResult;
	}

	/*************************** 时间计算 ***********************************/

	/**
	 * 验证系统验证码
	 * 
	 * @param account
	 *            账号
	 * @param passYzm
	 *            验证码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/common/checkYzm", method = RequestMethod.POST)
	public HttpResult<String> checkYzm(String account, String passYzm) {
		return mobileService.checkYzm(account, passYzm);
	}
	/**
	 * 映射关系选择
	 * @param userInfo 当前操作员
	 * @param busType 业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/adminCfg/listBusMapFlowByAuth", method = RequestMethod.POST)
	public HttpResult<List<BusMapFlow>> listBusMapFlowByAuth(@ModelAttribute("u_context") UserInfo userInfo,
			String busType) {
		return mobileService.listBusMapFlowByAuth(userInfo, busType);
	}
	
	
	/***************************费用管理***********************************/
	/**
	 * 借款申请记录信息
	 * @param loanApply 借款申请
	 * @param pageNum 页码
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/financial/loanApply/listLoanApplyOfAuth", method = RequestMethod.POST)
	public HttpResult<PageBean<FeeBudget>> listLoanApplyOfAuth(@ModelAttribute("u_context") UserInfo userInfo,
			String loanApplyStr,Integer pageNum, Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		return mobileService.listLoanApplyOfAuth(userInfo, loanApplyStr);
	}
	/**
	 * 用于借款的选择界面
	 * @param userInfo
	 * @param loanApplyStr
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/financial/loanApply/listPagedLoanApplyForStartSelect", method = RequestMethod.POST)
	public HttpResult<PageBean<FeeBudget>> listPagedLoanApplyForStartSelect(@ModelAttribute("u_context") UserInfo userInfo,
			String loanApplyStr,Integer pageNum, Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		return mobileService.listPagedLoanApplyForStartSelect(userInfo, loanApplyStr);
	}
	/**
	 * 用于报销的选择界面
	 * @param userInfo 当前操作人员
	 * @param loanApplyStr 借款申请gson
	 * @param pageNum 分页数
	 * @param pageSize 页码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/financial/loanApply/listPagedLoanApplyForOffSelect", method = RequestMethod.POST)
	public HttpResult<PageBean<FeeBudget>> listPagedLoanApplyForOffSelect(@ModelAttribute("u_context") UserInfo userInfo,
			String loanApplyStr,Integer pageNum, Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		return mobileService.listPagedLoanApplyForOffSelect(userInfo, loanApplyStr);
	}
	
	
	/**
	 * 借款记录信息
	 * @param userInfo 当前操作人员
	 * @param loan 借款信息
	 * @param pageNum 页码信息
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/financial/loan/listLoanOfAuth", method = RequestMethod.POST)
	public HttpResult<PageBean<FeeLoan>> listLoanOfAuth(@ModelAttribute("u_context") UserInfo userInfo,
			String loanStr,Integer pageNum, Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9 : pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		return mobileService.listLoanOfAuth(userInfo, loanStr);
	}
	/**
	 * 报销记录信息
	 * @param userInfo 当前操作人员
	 * @param loanOffAccount 报销信息
	 * @param pageNum 分页数
	 * @param pageSize 页码数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/financial/loanOff/listLoanOffOfAuth", method = RequestMethod.POST)
	public HttpResult<PageBean<FeeLoanOff>> listLoanOffOfAuth(@ModelAttribute("u_context") UserInfo userInfo,
			String loanOffAccountStr,Integer pageNum, Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		pageNum = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9 : pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		return mobileService.listLoanOffOfAuth(userInfo, loanOffAccountStr);
	}
	
	
	
	/**
	 * 发起借款申请
	 * @param userInfo 当前操作人员
	 * @param busMapFlowId 采取的映射关系
	 * @param busType 业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workBus/addWorkBus", method = RequestMethod.POST)
	public HttpResult<SpFlowInstance> addWorkBus(@ModelAttribute("u_context") UserInfo userInfo,
			Integer busMapFlowId,String busType) {
		return mobileService.addWorkBus(userInfo, busMapFlowId,busType);
	}
	
	
	/**
	 * 发起借款
	 * @param userInfo 当前操作人员
	 * @param busMapFlowId 采用的映射关系
	 * @param applyId 借款申请主键
	 * @param busType 业务类型
	 * @param isBusinessTrip 是否为差旅
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/financial/loan/addLoan", method = RequestMethod.POST)
	public HttpResult<SpFlowInstance> addLoan(@ModelAttribute("u_context") UserInfo userInfo,
			Integer busMapFlowId,Integer applyId,String busType,Integer isBusinessTrip) {
		return mobileService.addLoan(busMapFlowId,userInfo,applyId,busType,isBusinessTrip);
	}
	/**
	 * 汇报工作
	 * @param userInfo 当前操作人员
	 * @param busMapFlowId 采用的映射关系
	 * @param applyId 借款申请主键
	 * @param busType 业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/financial/loanReport/addLoanReport", method = RequestMethod.POST)
	public HttpResult<SpFlowInstance> addLoanReport(@ModelAttribute("u_context") UserInfo userInfo,
			Integer busMapFlowId,Integer applyId,String busType) {
		return mobileService.addLoanReport(busMapFlowId,userInfo,applyId,busType);
	}
	/**
	 * 发起报销
	 * @param userInfo 当前操作人员
	 * @param busMapFlowId 采用的映射关系
	 * @param loanWay 借款方式
	 * @param loanBusId 采用的借款主键 loanapply 与loan
	 * @param loanReportWay 汇报方式
	 * @param loanReportId 汇报主键
	 * @param busType 业务主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/financial/loanOff/addLoanOff", method = RequestMethod.POST)
	public HttpResult<SpFlowInstance> addLoanOff(@ModelAttribute("u_context") UserInfo userInfo,
			Integer busMapFlowId,String loanWay,Integer loanBusId,
			String loanReportWay,Integer loanReportId,String busType) {
		loanBusId = null == loanBusId?0:loanBusId;
		return mobileService.addLoanOff(busMapFlowId,userInfo,busType,loanWay,loanBusId,loanReportWay,loanReportId);
	}
	
	
	/************************考勤管理************************************/
	
	/**
	 * 分页查询加班信息
	 * @param userInfo 当前操作人员
	 * @param overTime 加班信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/attence/listPagedOverTime", method = RequestMethod.POST)
	public HttpResult<PageBean<OverTime>> listPagedOverTime(@ModelAttribute("u_context") UserInfo userInfo,
			String overTimeStr) {
		return mobileService.listPagedOverTime(userInfo,overTimeStr);
	}
	/**
	 * 分页查询请假信息
	 * @param userInfo 当前操作人员
	 * @param leave 请假信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/attence/listPagedLeave", method = RequestMethod.POST)
	public HttpResult<PageBean<Leave>> listPagedLeave(@ModelAttribute("u_context") UserInfo userInfo,
			String leaveStr) {
		return mobileService.listPagedLeave(userInfo,leaveStr);
	}
	
	/************************消费记录************************************/
	
	/**
	 * 分页查询用于记账的数据
	 * @param userInfo 当前操作人员
	 * @param consumeStr 记账数据Str
	 * @param pageNum 页码
	 * @param pageSize 分页大小
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/listPagedConsumeForSelect", method = RequestMethod.POST)
	public HttpResult<PageBean<Consume>> listPagedConsumeForSelect(@ModelAttribute("u_context") UserInfo userInfo,
			String consumeStr,Integer pageNum, Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9 : pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		return mobileService.listPagedConsumeForSelect(userInfo, consumeStr);
	}
	
	/**
	 * 分页查询所有的记账的数据
	 * @param userInfo 当前操作人员
	 * @param consumeStr 记账数据Str
	 * @param pageNum 页码
	 * @param pageSize 分页大小
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/listPagedConsume", method = RequestMethod.POST)
	public HttpResult<PageBean<Consume>> listPagedConsume(@ModelAttribute("u_context") UserInfo userInfo,
			String consumeStr,Integer pageNum, Integer pageSize) {
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9 : pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		return mobileService.listPagedConsume(userInfo, consumeStr);
	}
	
	/**
	 * 添加记账数据
	 * @param userInfo 当前操作人员
	 * @param consumeStr 记账json数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/addConsume", method = RequestMethod.POST)
	public HttpResult<String> addConsume(@ModelAttribute("u_context") UserInfo userInfo,String consumeStr) {
		return mobileService.addConsume(userInfo,consumeStr);
	}
	
	/**
	 * 添加记账类型数据
	 * @param userInfo 当前操作人员
	 * @param consumeTypeStr 记账类型json数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/addConsumeType", method = RequestMethod.POST)
	public HttpResult<String> addConsumeType(@ModelAttribute("u_context") UserInfo userInfo,String consumeTypeStr) {
		return mobileService.addConsumeType(userInfo,consumeTypeStr);
	}
	
	/**
	 * 查询记账类型
	 * @param userInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/listConsumeType", method = RequestMethod.POST)
	public HttpResult<List<ConsumeType>> listConsumeType(@ModelAttribute("u_context") UserInfo userInfo) {
		return mobileService.listConsumeType(userInfo);
	}
	
	/**
	 * 查询消费记录附件
	 * @author hcj 
	 * @param userInfo
	 * @param consumeId
	 * @return 
	 * @date 2018年9月5日 上午10:04:22
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/listConsumeUpfile", method = RequestMethod.POST)
	public HttpResult<List<ConsumeUpfile>> listConsumeUpfile(@ModelAttribute("u_context") UserInfo userInfo,Integer consumeId) {
		return mobileService.listConsumeUpfile(userInfo,consumeId);
	}
	
	/**
	 * 根据id查询记账详情
	 * @param userInfo
	 * @param id 记账id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/queryConsumeById", method = RequestMethod.POST)
	public HttpResult<Consume> queryConsumeById(@ModelAttribute("u_context") UserInfo userInfo,Integer id) {
		return mobileService.queryConsumeById(userInfo,id);
	}
	
	/**
	 * 根据id查询记账类型详情
	 * @param userInfo
	 * @param id 记账类型id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/queryConsumeTypeById", method = RequestMethod.POST)
	public HttpResult<ConsumeType> queryConsumeTypeById(@ModelAttribute("u_context") UserInfo userInfo,Integer id) {
		return mobileService.queryConsumeTypeById(userInfo,id);
	}
	
	/**
	 * 批量删除消费记录
	 * 
	 * @param userInfo
	 * @param ids 删除的消费记录id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/delConsume", method = RequestMethod.POST)
	public HttpResult<String> delConsume(
			@ModelAttribute("u_context") UserInfo userInfo,
			Integer[] ids) {
		HttpResult<String> httpResult = mobileService.delConsume(userInfo,
				ids);
		return httpResult;

	}
	
	/**
	 * 删除发票附件
	 * @author hcj 
	 * @param userInfo
	 * @param consumeId 消费记录id
	 * @param consumeUpFileId 附件id
	 * @return 
	 * @date 2018年9月4日 下午1:49:48
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/delConsumeUpfile", method = RequestMethod.POST)
	public HttpResult<String> delConsumeUpfile(
			@ModelAttribute("u_context") UserInfo userInfo,
			Integer consumeId,Integer consumeUpFileId) {
		HttpResult<String> httpResult = mobileService.delConsumeUpfile(userInfo,
				consumeId,consumeUpFileId);
		return httpResult;

	}
	
	/**
	 * 更新消费记录的属性值
	 * @param userInfo
	 * @param consumeStr消费记录实体（传入需要更新的属性）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/updateConsume", method = RequestMethod.POST)
	public HttpResult<Consume> updateConsume(
			@ModelAttribute("u_context") UserInfo userInfo,String consumeStr) {
		HttpResult<Consume> httpResult = null;
		try {
			httpResult = mobileService.updateConsume(userInfo, consumeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpResult;
	}
	
	/**
	 * 单独新增发票
	 * @param userInfo
	 * @param upFileIds 上传的发票附件的upFileId用逗号隔开
	 * @param id 消费记录id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consume/addInvoice", method = RequestMethod.POST)
	public HttpResult<String> addInvoice(@ModelAttribute("u_context") UserInfo userInfo,String upFileIds,Integer id) {
		return mobileService.addInvoice(userInfo,upFileIds,id);
	}
	
	/**
	 * 分页查询外部
	 * @param userInfo
	 * @param pageNum
	 * @param pageSize
	 * @param outLinkManStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/outLinkMan/listPagedOutLinkMan", method = RequestMethod.POST)
	public HttpResult<List<OutLinkMan>> listPagedOutLinkMan(@ModelAttribute("u_context") UserInfo userInfo,Integer pageNum,
			Integer pageSize, String outLinkManStr){
		// 返回对象声明
		HttpResult<List<OutLinkMan>> httpResult = new HttpResult<List<OutLinkMan>>();
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<OutLinkMan> outLinkManList = mobileService.listPagedOutLinkMan(userInfo, outLinkManStr);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(outLinkManList);
		return httpResult;
	}
	
	/**
	 * 运营分析统计
	 * @param userInfo 当前操作人员
	 * @param platformType 查询的类型
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param taskDateTimeType 任务分析的时间类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/statistics/platformByType", method = RequestMethod.POST)
	public HttpResult<String> platformByType(@ModelAttribute("u_context") UserInfo userInfo,String platformType,
			String startDate,String endDate,String taskDateTimeType,String version){
		if(StringUtils.isEmpty(taskDateTimeType)){
			return new HttpResult<String>().error("参数错误");
		}
		if(CommonUtil.isNull(version)){
			version="1";
		}
		String resultStr = mobileService.platformByType(userInfo,platformType,startDate,endDate,taskDateTimeType,version);
		return new HttpResult<String>().ok(resultStr);
	}
	
	/**
	 * 获取日报数据集合
	 * @param userInfo
	 * @param dailyStr
	 * @param pageNum
	 * @param pageSize
	 * @param creatorType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/daily/listPagedDaily", method = RequestMethod.POST)
	public HttpResult<String> listPagedDaily(
			@ModelAttribute("u_context") UserInfo userInfo,
			String dailyStr ,Integer pageNum,Integer pageSize){
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		pageSize = ((null == pageSize || "".equals(pageSize.toString().trim())) ? 9
				: pageSize);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		// 返回对象声明
		HttpResult<String> httpResult = mobileService.listPagedDaily(dailyStr,userInfo);
		return httpResult;
	}
	
	/**
	 * 获取日报数据集合
	 * @param userInfo
	 * @param daily
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/daily/viewDaily", method = RequestMethod.POST)
	public HttpResult<String> viewDaily(
			@ModelAttribute("u_context") UserInfo userInfo,
			Integer dailyId) throws ParseException {
		// 返回对象声明
		HttpResult<String> httpResult = mobileService.viewDaily(dailyId,userInfo);
		return httpResult;
	}
}
