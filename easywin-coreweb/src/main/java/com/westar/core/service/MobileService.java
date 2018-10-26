package com.westar.core.service;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.westar.base.cons.CommonConstant;
import com.westar.base.enums.PubStateEnum;
import com.westar.base.enums.StaticPlatFormTypeEnum;
import com.westar.base.model.AnsTalk;
import com.westar.base.model.Answer;
import com.westar.base.model.Area;
import com.westar.base.model.Attention;
import com.westar.base.model.BusMapFlow;
import com.westar.base.model.Clock;
import com.westar.base.model.Consume;
import com.westar.base.model.ConsumeType;
import com.westar.base.model.ConsumeUpfile;
import com.westar.base.model.Customer;
import com.westar.base.model.CustomerHandOver;
import com.westar.base.model.CustomerLog;
import com.westar.base.model.CustomerStage;
import com.westar.base.model.CustomerType;
import com.westar.base.model.CustomerUpfile;
import com.westar.base.model.Daily;
import com.westar.base.model.DailyLog;
import com.westar.base.model.DailyTalk;
import com.westar.base.model.DailyUpfiles;
import com.westar.base.model.Department;
import com.westar.base.model.FeeBudget;
import com.westar.base.model.FeeLoan;
import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.FeedBackInfo;
import com.westar.base.model.FeedBackType;
import com.westar.base.model.FileClassify;
import com.westar.base.model.FileDetail;
import com.westar.base.model.FormLayout;
import com.westar.base.model.FormMod;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.Item;
import com.westar.base.model.ItemHandOver;
import com.westar.base.model.ItemLog;
import com.westar.base.model.ItemTalk;
import com.westar.base.model.ItemUpfile;
import com.westar.base.model.JoinRecord;
import com.westar.base.model.JoinTemp;
import com.westar.base.model.Leave;
import com.westar.base.model.LinkMan;
import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.MsgShare;
import com.westar.base.model.MsgShareTalk;
import com.westar.base.model.Organic;
import com.westar.base.model.OutLinkMan;
import com.westar.base.model.OverTime;
import com.westar.base.model.PassYzm;
import com.westar.base.model.PointGPS;
import com.westar.base.model.ProLog;
import com.westar.base.model.ProUpFiles;
import com.westar.base.model.QuesFile;
import com.westar.base.model.QuesLog;
import com.westar.base.model.Question;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.ShareGroup;
import com.westar.base.model.SpFlowHiStep;
import com.westar.base.model.SpFlowHuiQianInfo;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.SpFlowTalk;
import com.westar.base.model.SpFlowType;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.StagedItem;
import com.westar.base.model.Task;
import com.westar.base.model.TaskExecutor;
import com.westar.base.model.TaskLog;
import com.westar.base.model.TaskTalk;
import com.westar.base.model.TaskUpfile;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.model.WeekRepLog;
import com.westar.base.model.WeekRepTalk;
import com.westar.base.model.WeekRepUpfiles;
import com.westar.base.model.WeekReport;
import com.westar.base.model.WeekReportPlan;
import com.westar.base.model.WeekReportQ;
import com.westar.base.model.WeekReportVal;
import com.westar.base.model.WeekViewer;
import com.westar.base.pojo.Area4App;
import com.westar.base.pojo.BaseTalk;
import com.westar.base.pojo.BaseUpfile;
import com.westar.base.pojo.DailyPojo;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.FormData;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.IndexView;
import com.westar.base.pojo.ItemStagedInfo;
import com.westar.base.pojo.MobileInit;
import com.westar.base.pojo.MsgNoRead;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.StatisticCrmVo;
import com.westar.base.pojo.StatisticTaskVo;
import com.westar.base.pojo.UserAuth;
import com.westar.base.pojo.WeekReportPojo;
import com.westar.base.util.BeanUtilEx;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.Encodes;
import com.westar.base.util.FileUtil;
import com.westar.base.util.MathExtend;
import com.westar.base.util.StringUtil;
import com.westar.base.util.UUIDGenerator;
import com.westar.core.dao.MobileDao;
import com.westar.core.web.AppAuthKeyManager;
import com.westar.core.web.DataDicContext;
import com.westar.core.web.PaginationContext;
import com.westar.core.web.SessionContext;

/**
 * 业务逻辑数据处理 本层取名规则：增、删、改分别 add、del、update打头；集合查询list打头、其它查询query打头
 * 
 * @author lj
 * 
 */
@Service
public class MobileService {

	@Autowired
	MobileDao mobileDao;

	@Autowired
	TaskService taskService;

	@Autowired
	AttentionService attentionService;

	@Autowired
	ItemService itemService;

	@Autowired
	CrmService crmService;

	@Autowired
	DepartmentService depService;

	@Autowired
	RegistService registService;
	
	@Autowired
	SelfGroupService selfGrpService;

	@Autowired
	MsgShareService msgShareService;

	@Autowired
	WeekReportService weekRepService;

	@Autowired
	FileCenterService fileService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	WeekReportService weekReportService;

	@Autowired
	QasService qasService;

	@Autowired
	IndexService indexService;

	@Autowired
	VoteService voteService;

	@Autowired
	MeetingService meetingService;

	@Autowired
	ClockService clockService;

	@Autowired
	ModOptConfService modOptConfService;

	@Autowired
	UploadService uploadService;
	
	@Autowired
	OrganicService organicService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	JiFenService jifenService;
	
	@Autowired
	JpushRegisteService jpushRegisteService;
	
	@Autowired
	PointGPSService pointGPSService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	FlowDesignService flowDesignService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	FormService formService;
	
	@Autowired
	FestModService festModService;
	
	@Autowired
	FinancialService financialService;
	
	@Autowired
	AdminCfgService adminCfgService;
	
	@Autowired
	AttenceService attenceService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ConsumeService consumeService;
	
	@Autowired
	BusRelateSpFlowService busRelateSpFlowService;
	
	@Autowired
	DailyService dailyService;
	@Autowired
	OutLinkManService outLinkManService;
	@Autowired
	StatisticsService statisticsService;
	
	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(MobileService.class);

	@Autowired
    private ProductService productService;
	
	@Autowired
	private ProductTalkService productTalkService;


    /****************************************用户验证********************************************************/
	
	/**
	 * 验证登录人是否合理以及获取参加的所有团队集合
	 * 
	 * @param account 帐号
	 * @param password 密码
	 * @return
	 */
	public HttpResult<List<Organic>> mobListOrg(String account, String password) {
		// 返回对象声明
		HttpResult<List<Organic>> httpResult = new HttpResult<List<Organic>>();
		String passwordMD5 = Encodes.encodeMd5(password);
		// 查询用户所在的激活企业
		List<Organic> listOrg = organicService.listUserOrg(
				account.toLowerCase(), passwordMD5);
		// 没有激活的
		if (null == listOrg || listOrg.size() == 0) {
			UserInfo user = userInfoService.getUserInfoByAccount(account
					.toLowerCase());
			if (null == user) {
				
				//查询临时用户的集合
				List<JoinTemp> joinTemps = registService.getJoInTemp4Login(account.toLowerCase());
				
				//也没有
				if(null == joinTemps || joinTemps.isEmpty()){
					httpResult.setMsg("用户不存在！");
					httpResult.setCode(-1);
				}else{
					httpResult.setMsg("密码错误！");
					httpResult.setCode(-1);
				}
				
				
			} else {
				// 验证用户登陆信息
				if (!passwordMD5.equals(user.getPassword())) {
					httpResult.setMsg("密码错误！");
					httpResult.setCode(-1);
				} else {
					httpResult.setMsg("您的账号没有激活,或是团队号停用!");
					httpResult.setCode(-1);
				}
			}
		} else {
			httpResult.setMsg("验证成功！");
			httpResult.setCode(HttpResult.CODE_OK);
			httpResult.setData(listOrg);
		}
		return httpResult;
	}
	
	/**
	 * 取得用户详情
	 * @param sessionUser
	 * @param account2 
	 * @param userId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HttpResult<UserInfo> changeOrg(UserInfo sessionUser,HttpServletRequest request,Integer comId, String account) throws IllegalAccessException, InvocationTargetException {
		// 返回对象声明
		HttpResult<UserInfo> httpResult = new HttpResult<UserInfo>();
		if(null== account || "".equals(account)){
			account = sessionUser.getEmail();
			if(null!=account){
				account = account.toLowerCase();
			}else{
				account = sessionUser.getMovePhone();
			}
		}
		final UserInfo userInfo = userInfoService.userAuth(
				account.toLowerCase(), sessionUser.getPassword(), comId.toString());
		httpResult.setCode(HttpResult.CODE_OK);
		
		String authKey=request.getHeader(CommonConstant.APP_AUTH_KEY);
		AppAuthKeyManager.updateAuthKey(authKey,userInfo);
		httpResult.setData(userInfo);
		return httpResult;
	}
	
	/**
	 * 验证登录人是否合理以及获取参加的所有团队集合
	 * @param account2 
	 * @return
	 */
	public HttpResult<List<Organic>> orgList(UserInfo userInfo, String account) {
		// 返回对象声明
		HttpResult<List<Organic>> httpResult = new HttpResult<List<Organic>>();
		if(null== account || "".equals(account)){
			account = userInfo.getEmail();
			if(null!=account){
				account = account.toLowerCase();
			}else{
				account = userInfo.getMovePhone();
			}
		}
		// 查询用户所在的激活企业
		List<Organic> listOrg = organicService.listUserOrg(account.toLowerCase(), userInfo.getPassword());
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listOrg);
		return httpResult;
	}
	
	/**
	 * 登入所选企业平台(移动端登入)
	 * 
	 * @param loginName
	 *            用户名
	 * @param password
	 *            MD5密码
	 * @param comId
	 *            所需企业主键
	 * @param registrationId 
	 * @return
	 */
	public HttpResult<UserAuth> loginInOrg(
			String loginName, String password, String comId, String registrationId,String appSource) {
		// 返回对象声明
		HttpResult<UserAuth> httpResult = new HttpResult<UserAuth>();
		String passwordMD5 = Encodes.encodeMd5(password);
		final UserInfo userInfo = userInfoService.userAuth(
				loginName.toLowerCase(), passwordMD5, comId);
		if (null != userInfo) {
			httpResult.setCode(HttpResult.CODE_OK);
			String authKey = AppAuthKeyManager.newAuthKey(userInfo);
			UserAuth userAuth = new UserAuth(userInfo, authKey);
			httpResult.setData(userAuth);
			
			jpushRegisteService.updateJPushRegist(userInfo.getId(),registrationId,appSource);
			
			// 添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo
					.getUserName(), "从移动端进入平台", ConstantInterface.TYPE_LOGIN,
					userInfo.getComId(),userInfo.getOptIP());
			// 每日登录积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(),
					ConstantInterface.TYPE_LOGIN, "每日登录系统", 0);
		} else {
			httpResult.setMsg("登入异常，请及时联系管理员！");
			httpResult.setCode(-1);
		}
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
	 * @param registrationId 
	 * @return
	 */
	public HttpResult<UserAuth> loginInOrgByAuto(HttpServletRequest request,
			String loginName, String password, String comId, String registrationId,String appSource) {
		// 返回对象声明
		HttpResult<UserAuth> httpResult = new HttpResult<UserAuth>();
		final UserInfo userInfo = userInfoService.userAuth(
				loginName.toLowerCase(),password,comId);
		if (null != userInfo) {
			httpResult.setCode(HttpResult.CODE_OK);
			AppAuthKeyManager.removeAuthKey(userInfo.getId());
			String newAuthKey = AppAuthKeyManager.newAuthKey(userInfo);
			UserAuth userAuth = new UserAuth(userInfo,newAuthKey);
			httpResult.setData(userAuth);
			
			// 添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo
					.getUserName(), "从移动端进入平台", ConstantInterface.TYPE_LOGIN,
					userInfo.getComId(),userInfo.getOptIP());
			// 每日登录积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(),
					ConstantInterface.TYPE_LOGIN, "每日登录系统", 0);
			
			
			jpushRegisteService.updateJPushRegist(userInfo.getId(),registrationId,appSource);
		} else {
			httpResult.setMsg("登入异常，请及时联系管理员！");
			httpResult.setCode(-1);
		}
		return httpResult;
	}
	/**************************************** 以下主页消息页面初始化 *********************************************/
	/**
	 * 罗健临时应对
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Integer countTodo(Integer comId, Integer userId) {
		Integer nums = 0;
		List<MsgNoRead> modTodoList = mobileDao.countToDoByType(comId,userId);
		if(!CommonUtil.isNull(modTodoList)) {
			for (MsgNoRead msgNoRead : modTodoList) {
				nums+=msgNoRead.getNoReadNum();
			}
		}
		return nums;
	}
	/**
	 * 初始化主页提示信息
	 */
	public MobileInit<TodayWorks> initMainMsgFr(UserInfo userInfo) {
		if (null == userInfo) {
			return null;
		}

		MobileInit<TodayWorks> initVo = new MobileInit<TodayWorks>();
		// 总待办数
		Integer todoNums = this.countTodo(userInfo.getComId(),
				userInfo.getId());
		initVo.setUndone_job(todoNums);
		// 总关注未读消息数
		Integer attenNums = todayWorksService.countAttenNoRead(
				userInfo.getComId(), userInfo.getId());
		initVo.setUnread_atten(attenNums);
		initVo.setUnread_shareMsg(0);
		// 前三条未读信息
		// 一次加载行数
		PaginationContext.setPageSize(4);
		// 列表数据起始索引位置
		PaginationContext.setOffset(0 * PaginationContext.getPageSize());
		TodayWorks todayWorks = new TodayWorks();
		todayWorks.setReadState(0);
		
		List<String> modList  = new ArrayList<String>();
		//任务
		modList.add(ConstantInterface.TYPE_TASK);
		//项目
		modList.add(ConstantInterface.TYPE_ITEM);
		//产品
		modList.add(ConstantInterface.TYPE_PRODUCT);
		//周报
		modList.add(ConstantInterface.TYPE_WEEK);
		//分享
		modList.add(ConstantInterface.TYPE_DAILY);
		//问答
		modList.add(ConstantInterface.TYPE_QUES);
		//客户
		modList.add(ConstantInterface.TYPE_CRM);
		//附件
		modList.add(ConstantInterface.TYPE_FILE);
		//申请
		modList.add(ConstantInterface.TYPE_APPLY);
		//审批
		modList.add(ConstantInterface.TYPE_FLOW_SP);
		//分享
		modList.add("100");
		//闹铃
		modList.add("101");
		
		List<TodayWorks> list = todayWorksService.listPagedMsgNoRead(todayWorks,
				userInfo.getComId(), userInfo.getId(), modList);
		List<TodayWorks> result = new ArrayList<TodayWorks>();
		if (null != list && list.size() > 0) {
			for (TodayWorks todoWorks : list) {
				// 更新内容
				String content = todoWorks.getContent();
				todoWorks.setContent(StringUtil.toHtml(content));
				// 业务名称
				String busTypeName = todoWorks.getBusTypeName();
				todoWorks.setBusTypeName(StringUtil.toHtml(busTypeName));

				result.add(todoWorks);
			}
		}
		initVo.setData(result);
		return initVo;

	}

	/**
	 * 主页应用数据统计
	 * 
	 * @param userInfo
	 * @return
	 */
	public HttpResult<Map<String, Integer>> initMainModFr(UserInfo userInfo) {
		// 返回对象声明
		HttpResult<Map<String, Integer>> httpResult = new HttpResult<Map<String, Integer>>();
		Map<String, Integer> map = new HashMap<String, Integer>(5);
		// 任务
		Task task = new Task();
		// 初始化执行人为当前操作人
		task.setExecutor(userInfo.getId());
		// 任务状态标记为执行状态
		task.setState(1);
		List<Task> list = taskService.taskToDoListOfAll(task, userInfo);
		map.put("taskSum", null == list ? 0 : list.size());
		// 客户
		Customer customer = new Customer();
		customer.setComId(userInfo.getComId());
		// 客户区域ID初始化
		customer.setAreaId(0);
		// 我的客户
		customer.setOwnerType("0");
		// 取得模块的数据
		List<Customer> listCustomer = crmService.listCustomerOfAll(customer,
				userInfo.getId(), false);
		map.put("crmSum", null == listCustomer ? 0 : listCustomer.size());
		// 项目
		Item item = new Item();
		// 我的项目
		item.setOwnerType("0");
		List<Item> listItem = itemService.listItemOfAll(item, userInfo, false);
		map.put("itemSum", null == listItem ? 0 : listItem.size());
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(map);
		return httpResult;
	}

	/**************************************** 以下任务业务 ***************************************************/
	/**
	 * @param userInfo
	 *            当前操作人信息
	 * @param taskType
	 *            查看的任务类型
	 * @param state
	 *            任务状态筛选
	 * @param executor
	 *            执行人筛选
	 * @param taskName
	 *            任务名称
	 */
	public List<Task> taskList(UserInfo userInfo, String taskType, int state,
			int executor, String taskName) {
		if (null == userInfo || null == taskType || "".equals(taskType.trim())) {
			return null;
		}
		List<Task> taskList = null;
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
		Task task = new Task();
		// 设置任务名称
		task.setTaskName(taskName);
		if ("toDo".equals(taskType)) {
			List<UserInfo> exexutors = new ArrayList<>();
			exexutors.add(userInfo);
			// 初始化执行人为当前操作人
			task.setListExecutor(exexutors);
			
			// 任务状态标记为执行状态
			task.setState(1);
			// 获取个人所有的待办任务
			taskList = taskService.taskToDoList(task, userInfo);
		} else if ("subToDo".equals(taskType)) {
			// 下属执行
			if (userInfo.getCountSub() > 0) {
				task.setExecuType("1");
				if(state != 0){
					// 任务状态标记为执行状态
					task.setState(state);
				}
				if(executor>0){
					List<UserInfo> exexutors = new ArrayList<>();
					UserInfo executorUser = new UserInfo();
					executorUser.setId(executor);
					exexutors.add(executorUser);
					task.setListExecutor(exexutors);
				}
				taskList = taskService.listPageTask(task, userInfo);
			}
		} else if ("overdue".equals(taskType)) {
			// 逾期任务
			task.setExecutor(executor);
			taskList = taskService.listOverdueTask(task, userInfo,isForceIn);
		} else if ("operate".equals(taskType)) {
			// 经办任务
			task.setOperator(userInfo.getId());
			taskList = taskService.listPageTask(task, userInfo);
		} else if ("charge".equals(taskType)) {
			// 我负责的
			// 初始化负责人为当前操作人
			List<UserInfo> owners = new ArrayList<>();
			owners.add(userInfo);
			task.setListOwner(owners);
			// 执行人类别
			task.setExecuType(null);
			if(state==0){
				task.setState(null);
			}else{
				task.setState(state);
			}
			if(executor>0){
				List<UserInfo> exexutors = new ArrayList<>();
				UserInfo executorUser = new UserInfo();
				executorUser.setId(executor);
				exexutors.add(executorUser);
				task.setListExecutor(exexutors);
			}
			// 分页查询负责的任务
			taskList = taskService.listPageChargeTask(task, userInfo);
		} else if ("allTask".equals(taskType)) {
			if(state==0){
				task.setState(null);
			}else{
				task.setState(state);
			}
			if(executor>0){
				List<UserInfo> exexutors = new ArrayList<>();
				UserInfo executorUser = new UserInfo();
				executorUser.setId(executor);
				exexutors.add(executorUser);
				task.setListExecutor(exexutors);
			}
			// 所有任务
			taskList = taskService.listPageTask(task, userInfo);
		}
		List<Task> taskResultList = new ArrayList<Task>();
		Set<Integer> taskIdSet = new HashSet<Integer>(); 
		// html字符转换成转义字符
		if (null != taskList && !taskList.isEmpty()) {
			for (Task vo : taskList) {
				Integer taskId = vo.getId();
				if(!taskIdSet.contains(taskId)){
					vo.setTaskRemark(vo.getTaskRemark());
					taskResultList.add(vo);
					taskIdSet.add(taskId);
				}
			}
		}
		return taskResultList;
	}

	/**
	 * 取得任务子任务集合
	 * 
	 * @param userInfo
	 * @param pTaskId
	 * @return
	 */
	public List<Task> taskListSons(UserInfo userInfo, Integer pTaskId) {
		List<Task> sonTasks = taskService.listSonTask(pTaskId,
				userInfo.getComId());
		return sonTasks;

	}

	/**
	 * 获取模块关联的任务
	 * 
	 * @param userInfo
	 * @param task
	 * @return
	 */
	public List<Task> task4BusList(UserInfo userInfo, Task task) {
		if(null != task.getBusType() && task.getBusType().equals(ConstantInterface.TYPE_TASK)){
			List<Task> taskList = taskService.listSonTask(task.getBusId(),userInfo.getComId());
			return taskList;
		}else{
			List<Task> taskList = taskService.listPagedBusTask(task, userInfo);
			return taskList;
		}
		

	}

	/**
	 * 获取模块关联的任务
	 * 
	 * @param userInfo
	 * @param task
	 * @return
	 */
	public List<Task> listTaskForRelevance(UserInfo userInfo, Task task) {
		List<Task> taskList = taskService.listTaskForRelevance(task, userInfo);
		return taskList;

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
	public HttpResult<Task> taskDetail(UserInfo userInfo, Integer taskId) {
		// 返回对象声明
		HttpResult<Task> httpResult = new HttpResult<Task>();
		// 一次加载行数
		PaginationContext.setPageSize(9);
		//查看权限验证
		if(taskService.authorCheck(userInfo,taskId,0)){
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASK,taskId);
			//添加查看记录
			viewRecordService.addViewRecord(userInfo,viewRecord);
			Task task = taskService.getTask(taskId,userInfo);
			if(null==task.getTaskRemark()){
				task.setTaskRemark("");
			}
			// 任务紧急度具体化
			String gradeName = DataDicContext.getInstance()
					.getCurrentPathZvalue(
							"grade",
							(null == task.getGrade() ? "1" : task.getGrade())
									.toString());
			task.setGradeName(gradeName);
			// 取得父任务信息
			Task pTask = taskService.getTaskBusInfo(task.getParentId(),
					userInfo);
			if (null != pTask) {
				task.setpTaskName(pTask.getTaskName());
			}
			task.setPtask(pTask);

			// 获取模块操作权限
			List<ModuleOperateConfig> optCfgs = modOptConfService
					.listModuleOperateConfig(userInfo.getComId(),
							ConstantInterface.TYPE_TASK);
			task.setOptCfgs(optCfgs);

			httpResult.setCode(HttpResult.CODE_OK);
			httpResult.setData(task);
		} else {
			httpResult.setCode(-1);
			httpResult.setMsg("抱歉，你没有查看权限");

		}
		// 查看任务，删除消息提醒
		todayWorksService.updateTodoWorkRead(taskId, userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_TASK, 0);
		return httpResult;
	}

	/**
	 * 任务名称修改
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param task
	 *            任务
	 * @return
	 */
	public HttpResult<Map<String, String>> taskNameUpdate(UserInfo userInfo,
			Task task) {
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();

		Boolean succ = taskService.updateTaskName(task, userInfo);
		;
		if (succ) {
			httpResult.setCode(HttpResult.CODE_OK);
		} else {
			httpResult.setCode(-1);
			httpResult.setMsg("任务名称修改失败");
		}
		return httpResult;
	}

	/**
	 * 任务关联修改
	 * 
	 * @param userInfo
	 * @param task
	 * @return
	 */
	public HttpResult<Object> taskBusIdUpdate(UserInfo userInfo, Task task) {
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();

		boolean succ = taskService.updateTaskBusId(task, userInfo);
		if (succ) {
			httpResult.setCode(HttpResult.CODE_OK);
			String busType = task.getBusType();
			if (ConstantInterface.TYPE_ITEM.equals(busType)) {
				Task taskT = taskService.getTaskBusInfo(task.getId(), userInfo);
				httpResult.setData(taskT);
			}
		} else {
			httpResult.setCode(-1);
			httpResult.setMsg("任务关联失败");
		}
		return httpResult;
	}

	/**
	 * 任务关联修改
	 * 
	 * @param userInfo
	 * @param task
	 * @return
	 */
	public HttpResult<Object> taskStageIdUpdate(UserInfo userInfo, Task task) {
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();

		itemService.updateStagInfo(userInfo, task.getId(), task.getBusId(),
				task.getStagedItemId());
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 任务说明修改
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param task
	 *            任务
	 * @return
	 */
	public HttpResult<Map<String, String>> taskRemarkUpdate(UserInfo userInfo,
			Task task) {
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();

		Boolean succ = taskService.updateTaskTaskRemark(task, userInfo);
		;
		if (succ) {
			httpResult.setCode(HttpResult.CODE_OK);
		} else {
			httpResult.setCode(-1);
			httpResult.setMsg("任务说明修改失败");
		}
		return httpResult;
	}

	/**
	 * 认领任务
	 * @param sessioUser 当前操作人员
	 * @param taskId 任务主键
	 * @return
	 */
	public HttpResult<String> acceptTask(UserInfo sessioUser, Integer taskId) {
		// 返回对象声明
		HttpResult<String> httpResult = new HttpResult<String>();

		Map<String, Object> resultMap = taskService.updateAcceptTask(taskId, sessioUser);
		Gson gson = new Gson();
		httpResult.setData(gson.toJson(resultMap));
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}


	/**
	 * 修改任务共享人
	 * 
	 * @param userInfo
	 * @param taskId
	 * @param shares
	 * @return
	 */
	public HttpResult<Object> taskSharerUpdate(UserInfo userInfo,
			Integer taskId, String shares) {
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();

		// 共享人更新
		Gson gson = new Gson();
		List<UserInfo> list = gson.fromJson(shares,new TypeToken<List<UserInfo>>() {}.getType());
		Integer[] userIds = new Integer[list.size()];
		int i = 0;
		for (UserInfo user : list) {
			userIds[i++] = user.getId();
		}
		if (i > 0) {
			boolean succ = taskService.updateTaskSharer(userInfo.getComId(),
					taskId, userIds, userInfo);
			if (succ) {
				httpResult.setCode(HttpResult.CODE_OK);
			} else {
				httpResult.setCode(-1);
				httpResult.setMsg("任务共享人修改失败");
			}
		} else {
			httpResult.setCode(-1);
			httpResult.setMsg("任务共享人修改失败");
		}
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
	public HttpResult<Task> taskDelRelation(UserInfo userInfo, Integer taskId,
			Integer busId, String busType) {
		// 返回对象声明
		HttpResult<Task> httpResult = new HttpResult<Task>();
		// 删除任务关联
		if (ConstantInterface.TYPE_TASK.equals(busType)) {
			Task task = new Task();
			task.setId(taskId);
			task.setComId(userInfo.getComId());
			task.setParentId(busId);

			boolean succ = taskService.delpTaskRelation(task, userInfo);
			// 用于返回客户端
			Task retRask = taskService.getTaskBusInfo(task.getId(), userInfo);
			if (succ) {
				httpResult.setCode(HttpResult.CODE_OK);
				httpResult.setData(retRask);
			} else {
				httpResult.setCode(-1);
				httpResult.setMsg("解除关联任务失败");
			}
		} else {// 删除模块关联
			Task task = new Task();
			task.setId(taskId);
			task.setComId(userInfo.getComId());
			task.setBusId(busId);
			task.setBusType(busType);

			boolean succ = taskService.delTaskBusRelation(task, userInfo);
			if (succ) {
				httpResult.setCode(HttpResult.CODE_OK);
			} else {
				httpResult.setCode(-1);
				httpResult.setMsg("解除模块关联失败");
			}
		}

		return httpResult;
	}

	/**
	 * 任务进度汇报
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param progress
	 *            进度
	 * @param taskId
	 *            任务主键
	 * @return
	 */
	public HttpResult<Map<String, String>> taskProgressReport(
			UserInfo userInfo, Task task) {
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();

		Map<String, Object> map = taskService.updateTaskProgress(task, userInfo);
		if (map.get("status").equals("y")) {
			httpResult.setCode(HttpResult.CODE_OK);
		}else {
			httpResult.setCode(-1);
			httpResult.setMsg("任务进度汇报失败");
		}
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
	public HttpResult<Map<String, String>> taskDealTimeLimitUpdate(
			UserInfo userInfo, Task task) {
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();
		boolean succ = taskService.updateTaskDealTimeLimit(task, userInfo);
		if (succ) {
			httpResult.setCode(HttpResult.CODE_OK);
		} else {
			httpResult.setCode(-1);
			httpResult.setMsg("任务办理时限修改失败");
		}
		return httpResult;
	}

	/**
	 * 任务紧急度修改
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param task
	 *            任务
	 * @return
	 */
	public HttpResult<Map<String, String>> taskGradeUpdate(UserInfo userInfo,
			Task task) {
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();
		String gradeName = DataDicContext.getInstance().getCurrentPathZvalue(
				"grade", task.getGrade());
		boolean succ = taskService.updateTaskGrade(task, userInfo, gradeName);
		if (succ) {
			httpResult.setCode(HttpResult.CODE_OK);
		} else {
			httpResult.setCode(-1);
			httpResult.setMsg("任务紧急度修改失败");
		}
		return httpResult;

	}

	/**
	 * 任务委托
	 * 
	 * @param userInfo
	 * @param task
	 * @param taskId 
	 * @param taskStr 
	 * @return
	 */
	public HttpResult<Map<String, String>> taskNextExecutor(UserInfo userInfo,
			Task task, Integer taskId, String taskStr) {
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();
		if(!StringUtils.isEmpty(taskStr)){
			// 返回对象声明
			Gson gson = new Gson();
			task = gson.fromJson(taskStr, Task.class);
		}
		task.setId(taskId);
		task.setComId(userInfo.getComId());
		task.setOperator(userInfo.getId());
		Task qTask = taskService.getTaskById(task.getId());
		task.setTaskName(qTask.getTaskName());
		
		String taskRemark = task.getTaskRemark();
		
		String cooperateExplain = task.getCooperateExplain();
		if(StringUtils.isEmpty(cooperateExplain)){
			task.setCooperateExplain(taskRemark);
		}
		task.setTaskRemark(null);
		
		/**
		 * 2018-07-12 zzq 暂时屏蔽
		 */
//		Map<String,Object> map = taskService.updateTaskCooperateConfig(task, userInfo, null);
//		if(null!=map.get("status")){
//			httpResult.setCode(-1);
//			httpResult.setMsg(map.get("info").toString());
//		}else{
//			httpResult.setCode(HttpResult.CODE_OK);
//			
//		}
		taskService.updateHandOverTask(task, userInfo, null);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 任务委托
	 * 
	 * @param userInfo
	 * @param task
	 * @param taskId 
	 * @param taskStr 
	 * @return
	 */
	@SuppressWarnings("unused")
	public HttpResult<Map<String, String>> taskTurnBack(UserInfo userInfo,String taskStr) {
		
		
		Task task = new Task();
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();
		/**
		 * 218-07-12 zzq 暂停使用完成功能
		 */
		if(true){
			httpResult.setCode(-1);
			httpResult.setMsg("此功能正在研发中！请使用转办功能！");
			return httpResult;
		}
		if(!StringUtils.isEmpty(taskStr)){
			// 返回对象声明
			Gson gson = new Gson();
			task = gson.fromJson(taskStr, Task.class);
		}
		
		Task taskCheck = taskService.getTaskById(task.getId());
		if(taskCheck.getState().toString().equals(ConstantInterface.STATIS_TASK_STATE_DONE)){
			httpResult.setCode(-1);
			httpResult.setMsg("任务已办结不能委托!");
			return httpResult;
		}
		
		
		task.setComId(userInfo.getComId());
		task.setOperator(userInfo.getId());
		
		taskService.updateTaskTurnBack(task,userInfo,null);
		httpResult.setCode(HttpResult.CODE_OK);
		
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
	public HttpResult<Map<String, String>> taskTalkList(UserInfo userInfo,
			Integer taskId) {
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();

		

		Map<String, String> map = new HashMap<String, String>(2);
		List<TaskTalk> listTaskTalk = taskService.listTaskTalk(taskId,
				userInfo.getComId());
		if (null != listTaskTalk && listTaskTalk.size() > 0) {
			Gson gson = new Gson();
			map.put("talks", gson.toJson(listTaskTalk));
		} else {
			map.put("talks", null);
		}

		map.put("count", PaginationContext.getTotalCount() + "");

		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(map);

		return httpResult;

	}

	/**
	 * 任务反馈
	 * 
	 * @param userInfo
	 * @param taskId
	 * @param pId
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public HttpResult<Task> taskFeedBack(UserInfo userInfo, Integer taskId,
			Integer pId, String content) throws Exception {
		// 返回对象声明
		HttpResult<Task> httpResult = new HttpResult<Task>();
		TaskTalk taskTalk = new TaskTalk();
		taskTalk.setComId(userInfo.getComId());
		taskTalk.setSpeaker(userInfo.getId());
		taskTalk.setContent(content);
		taskTalk.setTaskId(taskId);
		taskTalk.setParentId(pId);
		taskService.addTaskTalk(taskTalk, userInfo);
		// 模块日志添加
		taskService.addTaskLog(userInfo.getComId(), taskTalk.getTaskId(),
				userInfo.getId(), userInfo.getUserName(), "添加留言");
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(new Task());

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
	public HttpResult<Object> taskAdd(UserInfo userInfo, String taskStr)
			throws Exception {
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();
		Gson gson = new Gson();
		Task task = gson.fromJson(taskStr, Task.class);
		// 企业号
		task.setComId(userInfo.getComId());
		// 创建人
		task.setCreator(userInfo.getId());
		// 负责人
		task.setOwner(userInfo.getId());
		// 删除标识(正常)
		task.setDelState(0);
		// 任务状态标识
		task.setState(1);
		
		
		//任务的执行人信息
		Integer executor = task.getExecutor();
		//若是没有设定办理人员集合，则由指定办理人员组成数据
		List<TaskExecutor> listTaskExecutors = task.getListTaskExecutor();
		if(null == listTaskExecutors || listTaskExecutors.isEmpty()){
			//重新实例化办理人员集合信息
			listTaskExecutors = new ArrayList<TaskExecutor>();
			TaskExecutor taskExecutor = new TaskExecutor();
			taskExecutor.setExecutor(executor);
			listTaskExecutors.add(taskExecutor);
			task.setListTaskExecutor(listTaskExecutors);
		}
		
		//没指定任务类型的默认单人任务
		String taskType = task.getTaskType();
		if(StringUtils.isEmpty(taskType)){
			task.setTaskType(ConstantInterface.TASK_TYPE_CHOOSE);
		}
		
		
		
		taskService.addTask(task, null, userInfo, null);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}

	/**
	 * 任务状态修改
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param task
	 *            任务
	 * @return
	 */
	public HttpResult<List<FlowRecord>> flowRecordList(UserInfo userInfo,
			String busType, Integer busId) {
		HttpResult<List<FlowRecord>> httpResult = new HttpResult<List<FlowRecord>>();
		// 移交记录
		List<FlowRecord> listFlowRecord = new ArrayList<FlowRecord>();
		if (ConstantInterface.TYPE_ITEM.equals(busType)) {
			listFlowRecord = itemService.listFlowRecord(busId,
					userInfo.getComId());
			// 项目详情状态
			Item item = null;
			if (null == listFlowRecord || listFlowRecord.size() == 0) {
				item = itemService.getItemById(busId, userInfo);

				FlowRecord flowRecord = new FlowRecord();
				flowRecord.setUserId(item.getCreator());
				flowRecord.setAcceptDate(item.getRecordCreateTime());
				flowRecord.setUserName(item.getOwnerName());
				flowRecord.setUuid(item.getUuid());
				flowRecord.setGender(item.getGender());
				listFlowRecord.add(flowRecord);
			} else {
				item = itemService.getItemById(busId);
			}
			// 任务办结
			if (item.getState() == 4) {
				FlowRecord flowRecord = listFlowRecord.remove(0);
				flowRecord.setState("1");
				listFlowRecord.add(0, flowRecord);
			}

		} else if (ConstantInterface.TYPE_TASK.equals(busType)) {

			listFlowRecord = taskService.listFlowRecord(busId,
					userInfo.getComId());
			// 任务详情状态
			Task task = null;
			if (null == listFlowRecord || listFlowRecord.size() == 0) {
				task = taskService.getTaskById(busId, userInfo);

				FlowRecord flowRecord = new FlowRecord();
				flowRecord.setUserId(task.getCreator());
				flowRecord.setAcceptDate(task.getRecordCreateTime());
				flowRecord.setUserName(task.getOwnerName());
				flowRecord.setUuid(task.getUuid());
				flowRecord.setGender(task.getGender());
				listFlowRecord.add(flowRecord);
			} else {
				task = taskService.getTaskById(busId);
			}
			// 任务办结
			if (task.getState() == 4) {
				FlowRecord flowRecord = listFlowRecord.remove(0);
				flowRecord.setState("1");
				listFlowRecord.add(0, flowRecord);
			}
		} else if (ConstantInterface.TYPE_CRM.equals(busType)) {
			listFlowRecord = crmService.listFlowRecord(busId,
					userInfo.getComId());
			// 客户详情
			Customer crm = null;
			if (null == listFlowRecord || listFlowRecord.size() == 0) {
				crm = crmService.getCrmById(userInfo, busId);

				FlowRecord flowRecord = new FlowRecord();
				flowRecord.setUserId(crm.getCreator());
				flowRecord.setAcceptDate(crm.getRecordCreateTime());
				flowRecord.setUserName(crm.getOwnerName());
				flowRecord.setUuid(crm.getUuid());
				flowRecord.setGender(crm.getGender());
				listFlowRecord.add(flowRecord);
			}
		}
		httpResult.setData(listFlowRecord);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}
	/**
	 * 取得用户详情
	 * @param curUserInfo
	 * @param userId
	 * @return
	 */

	public HttpResult<UserInfo> getUserInfo(
			UserInfo curUserInfo, Integer userId) {
		HttpResult<UserInfo> httpResult = new HttpResult<UserInfo>();
		UserInfo userInfo = userInfoService.getUserInfo(curUserInfo.getComId(),
				userId);
		//设置上级
		List<ImmediateSuper> leads = userInfoService.listImmediateSuper(userInfo);
		if(null!= leads && !leads.isEmpty()){
			List<UserInfo> leadLists = new ArrayList<UserInfo>();
			for (ImmediateSuper immediateSuper : leads) {
				UserInfo leadUser = new UserInfo();
				leadUser.setId(immediateSuper.getLeader());
				leadUser.setUserName(immediateSuper.getLeaderName());
				leadLists.add(leadUser);
				
			}
			userInfo.setListUserInfo(leadLists);
		}
		// Task task = new Task();
		// //初始化执行人为当前操作人
		// task.setExecutor(userInfo.getId());
		// //任务状态标记为执行状态
		// task.setState(1);
		// List<Task> list = taskService.taskToDoList(task,userInfo);
		// //待办任务统计 列表查询条件改了，这也需要修改！
		// List<Task> taskToDoCount = taskService.taskToDoCount(task, userInfo);

		//总关注未读消息数
		Integer attenNums = attentionService.countUserAtten(userId,userInfo.getComId());
		userInfo.setAttenNums(attenNums);
		
		Task task = new Task();
		//初始化执行人
		task.setExecutor(userId);
		//个人所有的待办任务
		Integer taskToDoNum = taskService.countTaskTodo(task, userInfo,"toDo");;
		userInfo.setTaskToDoNum(taskToDoNum);
		
		//我的客户
		Integer myCrmNum = crmService.countMyCrm(userId,userInfo.getComId());
		userInfo.setMyCrmNum(myCrmNum);
		
		//我的项目
		Integer myItemNum = itemService.countMyItem(userId,userInfo.getComId());
		userInfo.setMyItemNum(myItemNum);
		
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(userInfo);
		return httpResult;
	}
	/**
	 *验证是否有上级
	 * @param sessionUser
	 * @return
	 */
	public HttpResult<List<ImmediateSuper>> checkLeader(UserInfo sessionUser) {
		HttpResult<List<ImmediateSuper>> httpResult = new HttpResult<List<ImmediateSuper>>();
		//获取个人直属上级集合
		List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(sessionUser);
		httpResult.setData(listImmediateSuper);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 修改用户名和密码
	 * @param sessionUser 当前操作人员
	 * @param userName 新的用户名称
	 * @param password 新的用户密码
	 * @return
	 */
	public HttpResult<UserInfo> updateUserPassAndName(UserInfo sessionUser,String userName,String  password) {
		HttpResult<UserInfo> httpResult = new HttpResult<UserInfo>();
		//用户信息单个修改
		UserInfo userInfo = userInfoService.updateUserPassAndName(userName,password,sessionUser);
		httpResult.setData(userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 直属上级设定
	 * @param sessionUser 当前操作人员
	 * @param leaderId 上级主键
	 * @return
	 * @throws Exception 
	 */
	public HttpResult<String> updateImmediateSuper(UserInfo sessionUser,Integer leaderId) throws Exception {
		HttpResult<String> httpResult = new HttpResult<String>();
		if(leaderId.equals(sessionUser.getId())){
			//"不能分享给自己，这样没意义！"
			httpResult.setData("f1");
			httpResult.setCode(HttpResult.CODE_OK);
			return httpResult;
		}else{
			boolean succ = userInfoService.superClosedLoopCheck(leaderId,sessionUser);
			if(!succ){
				//"不能分享给\""+user.getUserName()+"\"；不然分享圈就成闭环了。"
				httpResult.setData("f2");
				httpResult.setCode(HttpResult.CODE_OK);
				return httpResult;
			}
		}
		
		//获取个人直属上级集合
		List<ImmediateSuper> listPreSup = userInfoService.listImmediateSuper(sessionUser);
		boolean succ = userInfoService.updateImmediateSuper(new Integer[]{leaderId},sessionUser.getIsChief(), sessionUser);
		if(succ){
			//离职人员有上级
			if(null!=listPreSup && listPreSup.size()>0){
				for (ImmediateSuper immediateSuperss : listPreSup) {
					//减少原来上级的一个下级
					SessionContext.updateSub(immediateSuperss.getLeader(), sessionUser.getComId(),-1);
				}
			}
			//添加一个下级
			SessionContext.updateSub(leaderId, sessionUser.getComId(),1);
			
			httpResult.setData("y");
		}else{
			httpResult.setData("f3");
		}
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 修改部门信息
	 * @param sessionUser 当前操作员
	 * @param depId 部门主键
	 * @param depName 部门名称
	 * @return
	 * @throws Exception
	 */
	public HttpResult<String> updateUserDep(UserInfo sessionUser,Integer depId,String depName) throws Exception {
		HttpResult<String> httpResult = new HttpResult<String>();
		userInfoService.updateUserDep(sessionUser.getComId(),sessionUser.getId(),depId);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 验证账号并且发送验证码
	 * @param sessionUser 当前操作人员
	 * @param noticeType 账号类型
	 * @param account 账号
	 * @return
	 */
	public HttpResult<String> sendPassYzmByNewAccount(UserInfo sessionUser,String noticeType,String account){
		HttpResult<String> httpResult = new HttpResult<String>();
		
		UserInfo userTemp = new UserInfo();
		BeanUtilEx.copyIgnoreNulls(sessionUser, userTemp);
		
		UserInfo sysUser = userInfoService.getUserInfoByAccount(account.toLowerCase());
		if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
			//没有被绑定
			if(null==sysUser){
			}
			//当前操作人员，没有设定手机号
			else if(null==sessionUser.getMovePhone()){ 
				//当前人员手机号与填写的相同
				if(sysUser.getMovePhone().equals(account.toLowerCase())){
					httpResult.setCode(-1);
					httpResult.setMsg("该手机号已被绑定，请重新填写。");
					return httpResult;
				}
			}
			//已被绑定
			else{
				httpResult.setCode(-1);
				httpResult.setMsg("该手机号已被绑定，请重新填写。");
				return httpResult;
				
			}
		}
		
		if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
			userTemp.setMovePhone(account);
		}
		Map<String, Object> map = userInfoService.doSendPassYzm(userTemp,noticeType);
		if(map.get("status").equals("y")){
			httpResult.setCode(HttpResult.CODE_OK);
		}else{
			httpResult.setCode(-1);
			httpResult.setMsg(map.get("info").toString());
		}
		return httpResult;
	}
	/**
	 * 设置用户手机号
	 * @param sessionUser 当前操作人员
	 * @param account 账号
	 * @param yzm 验证码
	 * @return
	 */
	public HttpResult<String> updateUserMovePhone(HttpServletRequest request,UserInfo sessionUser,String account,String yzm) {
		HttpResult<String> httpResult = new HttpResult<String>();
		if(!CommonUtil.isPhone(account)){
			httpResult.setCode(HttpResult.CODE_OK);
			//手机号格式不对
			httpResult.setData("f1");
			return httpResult;
		}
		UserInfo userInfo = userInfoService.getUserInfoByAccount(account.toLowerCase());
		boolean flag = true;
		//没有被绑定
		if(null==userInfo){
			flag = true;
		}
		//当前操作人员，没有设定手机号
		else if(null==sessionUser.getMovePhone()){ 
			//当前人员手机号与填写的相同
			if(userInfo.getMovePhone().equals(account)){
				flag = false;
				//该手机号已被绑定，请重新填写
				httpResult.setData("f2");
			}
		}
		//已被绑定
		else{
			flag = false;
			//该手机号已被绑定，请重新填写
			httpResult.setData("f2");
		}
		
		//账号有效
		if(flag){
			PassYzm passYzm = registService.getPassYzm(account);
			if(null==passYzm ){
				flag = false;
				//验证码失效！
				httpResult.setData("f3");
				
				
			}else if(!yzm.equalsIgnoreCase(passYzm.getPassYzm())) {
				flag = false;
				//验证码输入错误！
				httpResult.setData("f4");
			}
			
			//验证码有效
			if(flag){
				httpResult.setData("y");
				//删除验证码
				userInfoService.delPassYzmById(passYzm.getId());
				// 更新用户帐号
				userInfoService.updateUserAccount(ConstantInterface.GET_BY_PHONE ,account,sessionUser);
				//获取用户更新后信息
				sessionUser = userInfoService.getUserInfo(sessionUser.getComId(),sessionUser.getId());
				
				String authKey=request.getHeader(CommonConstant.APP_AUTH_KEY);
				AppAuthKeyManager.updateSessionUser(authKey,sessionUser);
			}
		}
		
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 修改人员属性
	 * @param authKey 
	 * @param sessionUser当前操作人员
	 * @param attrName 属性名称
	 * @param attrVal 属性值
	 * @return
	 */
	public HttpResult<UserInfo> updateUserAttr(UserInfo sessionUser,String attrType,
			String attrValStr, String authKey){
		HttpResult<UserInfo> httpResult = new HttpResult<UserInfo>();
		
		if(StringUtil.isBlank(StringUtil.delNull(attrType))){
			httpResult.setCode(-1);
			httpResult.setMsg("更新属性标识符为空！attrType："+attrType);
			return httpResult;
		}
		
		Gson gson = new Gson();
		UserInfo userInfo = gson.fromJson(attrValStr, UserInfo.class);
		userInfo.setComId(sessionUser.getComId());
		userInfo.setId(sessionUser.getId());
		
		if("nickname".equals(attrType)){
			String nickName = sessionUser.getNickname();
			List<UserInfo> users = userInfoService.checkNickName(nickName.toLowerCase(),sessionUser.getId());
			if(null!=users && users.size()>0){
				httpResult.setCode(-1);
				httpResult.setMsg("别名已被使用！");
				return httpResult;
			}
			
		}else if("leader".equals(attrType)){
			try {
				Map<String,Object> map =userInfoService.updateUserLeader(userInfo, sessionUser);
				if(map.get("isSucc").equals("no")){
					httpResult.setCode(-1);
					httpResult.setMsg((String) map.get("msg"));
					return httpResult;
				}else{
					UserInfo user = userInfoService.getUserInfo(sessionUser.getComId(),sessionUser.getId()); 
					//设置下级个数
					user.setCountSub(userInfoService.countSubUser(sessionUser.getId(),sessionUser.getComId()));
					//更新session用户信息
					AppAuthKeyManager.updateSessionUser(authKey,user);
					httpResult.setCode(HttpResult.CODE_OK);
					httpResult.setData(user);
					return httpResult;
				}
			} catch (Exception e) {
				httpResult.setCode(-1);
				httpResult.setMsg("直属上级设置失败！");
				return httpResult;
			}
			
			
		}
		
		boolean isSucc = false;
		boolean isAttrChanged = CommonUtil.isAttrChanged(userInfo, attrType, sessionUser);
		if(isAttrChanged){
			//更新用户属性
			isSucc = userInfoService.updateUserAttrs(attrType,userInfo);
		}else{
			httpResult.setCode(HttpResult.CODE_OK);
			return httpResult;
		}
		if(isSucc){
			UserInfo user = userInfoService.getUserInfo(sessionUser.getComId(),sessionUser.getId()); 
			//设置下级个数
			user.setCountSub(userInfoService.countSubUser(sessionUser.getId(),sessionUser.getComId()));
			//更新session用户信息
			AppAuthKeyManager.updateSessionUser(authKey,user);
			httpResult.setCode(HttpResult.CODE_OK);
			httpResult.setData(user);
			return httpResult;
		}else{
			httpResult.setCode(-1);
			httpResult.setMsg("个人信息更新失败！");
			return httpResult;
		}
	}

	/**
	 * 模块状态修改
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param task
	 *            任务
	 * @return
	 */
	public HttpResult<Map<String, String>> modStateUpdate(UserInfo userInfo,
			String busType, Integer busId, Integer state) {
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();
		if (ConstantInterface.TYPE_TASK.equals(busType)
				|| ConstantInterface.TYPE_ITEM.equals(busType)) {

			String stateName = null;
			if (4 == state) {
				stateName = "完成";
			} else if (3 == state) {
				stateName = "挂起";
			} else if (1 == state) {
				stateName = "执行";
			}

			// 默认操作成功
			boolean succ = true;
			String failHeadStr = null;
			if (ConstantInterface.TYPE_TASK.equals(busType)) {
				failHeadStr = "任务";
				Task task = new Task();
				task.setId(busId);
				task.setComId(userInfo.getComId());
				task.setState(state);
				// 标记完成
				if (4 == task.getState()) {
					succ = taskService
							.updateFinishTask(task, userInfo, stateName);
				}
				// 重启
				else if (1 == task.getState()) {
					succ = taskService.updateRestarTask(task, userInfo,
							stateName);
				}
			} else if (ConstantInterface.TYPE_ITEM.equals(busType)) {
				failHeadStr = "项目";
				Item item = new Item();
				item.setId(busId);
				item.setComId(userInfo.getComId());
				item.setState(state);
				// 标记完成
				if (4 == item.getState()) {
					succ = itemService
							.updateDoneItem(item, userInfo, stateName);
				}
				// 重启
				else if (1 == item.getState()) {
					succ = itemService.updateRestarItem(item, userInfo,
							stateName);
				}
			}

			if (succ) {
				httpResult.setCode(HttpResult.CODE_OK);
			} else {
				httpResult.setCode(-1);
				httpResult.setMsg(failHeadStr + "修改状态为'" + state + "'失败");
			}
		} else if (ConstantInterface.TYPE_QUES.equals(busType)) {
			Question ques = new Question();
			ques.setId(busId);
			ques.setState(state.toString());
			qasService.closeQues(ques, userInfo);
		}

		return httpResult;

	}

	/**
	 * 设置模块的父级关联
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
	public HttpResult<Object> modParentUpdate(UserInfo userInfo, Integer modId,
			Integer modPId, String modType) {
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();
		// 设置的任务
		if (ConstantInterface.TYPE_TASK.equals(modType)) {

			Task task = new Task();
			task.setParentId(modPId);
			task.setId(modId);
			task.setComId(userInfo.getComId());

			// 查询任务当前设置父任务的父节点，包括父任务自己
			List<Task> parentTasks = taskService.listTaskOfAllParent(
					task.getParentId(), userInfo.getComId());
			if (null != parentTasks && parentTasks.size() > 0) {
				// 验证是否为自己的子任务
				for (Task taskTemp : parentTasks) {
					if (taskTemp.getId().equals(task.getId())) {
						httpResult.setCode(-1);
						httpResult.setMsg("不能关联子任务");
						return httpResult;
					}
				}
			}
			task.setComId(userInfo.getComId());
			// 父任务
			Task ptask = taskService.getTaskBusInfo(task.getParentId(),
					userInfo);
			boolean succ = taskService.updateTaskParentId(task, userInfo,
					parentTasks, ptask);
			if (succ) {
				httpResult.setCode(HttpResult.CODE_OK);
			} else {
				httpResult.setCode(-1);
				httpResult.setMsg("关联失败");
			}

		}
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
	 * @param executor 
	 * @param status 
	 */
	public List<Attention> attenList(UserInfo userInfo,Attention attenParam ) {
		if (null == userInfo) {
			return null;
		}
		
		String busType = attenParam.getBusType();
		Integer owner = attenParam.getOwner();
		String attenName = attenParam.getModTitle();
		Integer status = attenParam.getStatus();
		Integer executor = attenParam.getExecutor();
		
		List<Attention> attenList = null;
		Attention atten = new Attention();
		if(null != attenParam.getCustomerTypeId() && attenParam.getCustomerTypeId()>0){
			atten.setCustomerTypeId(attenParam.getCustomerTypeId());
		}
		// 企业号
		atten.setComId(userInfo.getComId());
		// 操作员Id
		atten.setUserId(userInfo.getId());
		// 负责人筛选
		if (null != owner && owner != 0) {
			atten.setOwner(owner);
		}
		// 状态筛选
		if (null != status && status != 0) {
			atten.setStatus(status);
		}
		// 执行人筛选
		if (null != executor && executor != 0) {
			atten.setExecutor(executor);
		}
		// 查询参数集合
		List<String> args = new ArrayList<String>();
		if (ConstantInterface.TYPE_TASK.equals(busType)) {
			// 关注任务
			// 类型
			args.add(ConstantInterface.TYPE_TASK);
		} else if (ConstantInterface.TYPE_CRM.equals(busType)) {
			// 关注客户
			// 类型
			args.add(ConstantInterface.TYPE_CRM);
		} else if (ConstantInterface.TYPE_ITEM.equals(busType)) {
			// 关注项目
			// 类型
			args.add(ConstantInterface.TYPE_ITEM);
		} else if (ConstantInterface.TYPE_PRODUCT.equals(busType)) {
			// 关注产品
			// 类型
			args.add(ConstantInterface.TYPE_PRODUCT);
		} else if (ConstantInterface.TYPE_VOTE.equals(busType)) {
			// 关注投票
			// 类型
			args.add(ConstantInterface.TYPE_VOTE);
		} else if (ConstantInterface.TYPE_QUES.equals(busType)) {
			// 关注问答
			// 类型
			args.add(ConstantInterface.TYPE_QUES);
		} else if ("100".equals(busType)) {
			// 关注分享
			// 类型
			args.add("100");
		}else if (ConstantInterface.TYPE_FLOW_SP.equals(busType)) {
			// 关注分享
			// 类型
			args.add(ConstantInterface.TYPE_FLOW_SP);
		} else {
			args = null;
			if (null != attenName && !"".equals(attenName.trim())) {
				atten.setModTitle(attenName);
			}
		}
		// 分页查询关注信息
		attenList = attentionService.listpagedAtten(atten, args);
		for (int i = 0; i < attenList.size(); i++) {
			String busTypeName = null;
			if (attenList.get(i).getBusType().equals("050")) {
				busTypeName = "分享";
			} else {
				busTypeName = DataDicContext
						.getInstance()
						.getCurrentPathZvalue("moduleType",
								attenList.get(i).getBusType()).substring(0, 2);
			}
			attenList.get(i).setBusTypeName(busTypeName);
		}

		return attenList;
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
	public HttpResult<Object> attenChange(UserInfo userInfo, String busType,
			Integer busId, Integer attentionState) {
		HttpResult<Object> httpResult = new HttpResult<Object>();
		if (null == userInfo || null == busType || "".equals(busType)
				|| null == busId || "".equals(busId) || null == attentionState
				|| "".equals(attentionState)) {
			httpResult.setCode(-1);
			httpResult.setMsg("参数异常，请及时联系管理员！");
		}
		Attention atten = new Attention();
		// 所在企业
		atten.setComId(userInfo.getComId());
		// 关注的人员
		atten.setUserId(userInfo.getId());
		atten.setBusType(busType);
		atten.setBusId(busId);
		// 修改数据
		attentionService.delAttention(atten, userInfo, attentionState);
		httpResult.setCode(HttpResult.CODE_OK);
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
	 * @param owner
	 *            负责人主键
	 */
	public List<Item> itemList(UserInfo userInfo, String itemType,
			int selectedItemState, int owner, String itemName) {
		if (null == userInfo || null == itemType || "".equals(itemType.trim())) {
			return null;
		}
		List<Item> itemList = null;
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);
				
		Item item = new Item();
		item.setComId(userInfo.getComId());
		// 若是没有下属，则没有负责人类别一说
		if (userInfo.getCountSub() <= 0) {
			item.setOwnerType(null);
		}
		if ("allItem".equals(itemType)) {
			// 获取所有项目
			item.setOwner(owner);
			item.setState(selectedItemState);
			if (null != itemName && !"".equals(itemName)) {
				item.setItemName(itemName);
			}
			itemList = itemService.listItem(item, userInfo);
		} else if ("myItem".equals(itemType)) {
			// 获取我的项目
			item = new Item();
			item.setComId(userInfo.getComId());
			item.setOwnerType("0");
			item.setState(selectedItemState);
			itemList = itemService.listItem(item, userInfo);
		} else if ("subItem".equals(itemType)) {
			// 获取下属项目
			item = new Item();
			item.setComId(userInfo.getComId());
			item.setOwnerType("1");
			item.setOwner(owner);
			item.setState(selectedItemState);
			itemList = itemService.listItem(item, userInfo);
		} else if ("handsItem".equals(itemType)) {
			// 移交项目
			item = new Item();
			item.setOwner(owner);
			item.setState(selectedItemState);
			itemList = itemService.listItemHandsForPage(item, userInfo);
		} else if ("addByMonthItem".equals(itemType)) {
			// 本月新增
			item = new Item();
			item.setOwner(owner);
			item.setState(selectedItemState);
			itemList = itemService.listItemAddByMonthForPage(item, userInfo,isForceIn);
		}
		return itemList;
	}

	/**
	 * 取得子项目集合
	 * 
	 * @param 当前操作人员
	 * @param pItemId
	 *            父项目主键
	 * @return
	 */
	public List<Item> itemListSons(UserInfo userInfo, Integer pItemId) {
		List<Item> itemList = itemService.listSonItem(pItemId,
				userInfo.getComId());
		return itemList;
	}

	/**
	 * 分页查询客户项目
	 * 
	 * @param userInfo
	 * @param item
	 * @return
	 */
	public List<Item> item4BusList(UserInfo userInfo, Integer busId,String busType) {
		List<Item> itemList = new ArrayList<Item>();
		if(busType.equals(ConstantInterface.TYPE_CRM)){
			Item item = new  Item();
			item.setPartnerId(busId);
			itemList = itemService.listPagedCrmItem(item, userInfo);
		}else if(busType.equals(ConstantInterface.TYPE_ITEM)){
			itemList = itemService.listSonItem(busId, userInfo.getComId());
		}
		return itemList;
	}

	/**
	 * 分页查询权限范围项目
	 * 
	 * @param userInfo
	 * @param item
	 * @return
	 */
	public List<Item> listItemForRelevance(UserInfo userInfo, Item item) {
		List<Item> itemList = itemService.listItemForRelevance(item, userInfo);
		return itemList;
	}

	/**
	 * 分页查询项目阶段信息
	 * 
	 * @param userInfo
	 * @param itemId
	 *            项目主键
	 * @param realPid
	 *            阶段文件夹主键
	 * @param name
	 *            阶段内容名称
	 * @return
	 */
	public List<ItemStagedInfo> itemStagPagedList(UserInfo userInfo,
			Integer itemId, Integer realPid, String name) {
		List<ItemStagedInfo> itemList = itemService.itemStagPagedList(userInfo,
				itemId, realPid, name);
		return itemList;
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
	public HttpResult<StagedItem> itemLatestStagedItem(UserInfo userInfo,
			Integer itemId) {
		// 返回对象声明
		HttpResult<StagedItem> httpResult = new HttpResult<StagedItem>();
		StagedItem stagedItem = itemService.queryTheLatestStagedItem(
				userInfo.getComId(), itemId);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(stagedItem);
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
	 *            阶段所在文件夹主键
	 * @return
	 */
	public List<ItemStagedInfo> itemStageListForRelevance(UserInfo userInfo,
			Integer itemId, Integer stagePid) {
		List<ItemStagedInfo> itemList = itemService.itemStageListForRelevance(
				userInfo, itemId, stagePid);
		return itemList;
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
	public HttpResult<Object> itemAdd(UserInfo userInfo, String itemStr)
			throws Exception {
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();
		Gson gson = new Gson();
		Item item = gson.fromJson(itemStr, Item.class);
		// 企业号
		item.setComId(userInfo.getComId());
		// 创建人
		item.setCreator(userInfo.getId());
		// 删除标识(正常)
		item.setDelState(0);
		// 项目状态标识
		item.setState(1);
		//是否公开
		Integer pubState = item.getPubState();
		if(null == pubState){
			item.setPubState(PubStateEnum.NO.getValue());
		}
		
		itemService.addItem(item, userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}

	/**
	 * 项目详情
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param itemId
	 *            项目主键
	 * @return
	 * @throws Exception
	 */
	public HttpResult<Item> itemDetail(UserInfo userInfo, Integer itemId)
			throws Exception {
		// 返回对象声明
		HttpResult<Item> httpResult = new HttpResult<Item>();
		if (null == itemId || 0 == itemId) {
			httpResult.setCode(401);
			httpResult.setMsg("项目主键不能是：" + itemId);
			return httpResult;
		}
		;
		// 一次加载行数
		PaginationContext.setPageSize(9);
		Item item = itemService.getItem(itemId, userInfo);
		itemService.listItemTalk(itemId, userInfo.getComId(),"app");
		// 设置留言讨论总数
		item.setTalkSum(PaginationContext.getTotalCount());

		// 获取模块操作权限
		List<ModuleOperateConfig> optCfgs = modOptConfService
				.listModuleOperateConfig(userInfo.getComId(),
						ConstantInterface.TYPE_ITEM);
		item.setOptCfgs(optCfgs);

		httpResult.setData(item);
		httpResult.setCode(HttpResult.CODE_OK);
		// 查看项目，删出消息提醒
		todayWorksService.updateTodoWorkRead(itemId, userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_ITEM, 0);
		return httpResult;

	}

	/**
	 * 项目留言讨论数据集
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param itemId
	 *            项目主键
	 * @return
	 * @throws Exception
	 */
	public HttpResult<List<ItemTalk>> itemTalkList(UserInfo userInfo,
			Integer itemId, Integer onMoreNum) throws Exception {
		// 返回对象声明
		HttpResult<List<ItemTalk>> httpResult = new HttpResult<List<ItemTalk>>();
		if (null == itemId || 0 == itemId) {
			httpResult.setCode(401);
			httpResult.setMsg("项目主键不能是：" + itemId);
			return httpResult;
		}
		;
		// 一次加载行数
		PaginationContext.setPageSize(9);
		PaginationContext
				.setOffset(onMoreNum * PaginationContext.getPageSize());
		List<ItemTalk> listItemTalk = itemService.listItemTalk(itemId,
				userInfo.getComId(),"app");
		httpResult.setData(listItemTalk);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}

	/**
	 * 项目移交
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param itemHandOver
	 *            移交配置
	 * @return
	 */
	public HttpResult<Map<String, String>> itemNextOwner(UserInfo userInfo,
			ItemHandOver itemHandOver) {
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();
		ItemHandOver[] itemHandOvers = new ItemHandOver[] { itemHandOver };

		try {
			itemService.addItemHandOver(itemHandOvers, userInfo);
			httpResult.setCode(HttpResult.CODE_OK);
		} catch (Exception e) {
			httpResult.setCode(-1);
			httpResult.setMsg("移交失败!");
		}
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
	 *            属性类型分类
	 * @param newAttrContent
	 *            新的属性值
	 * @return
	 * @throws Exception
	 */
	public HttpResult<Item> itemAttrUpdate(UserInfo userInfo, Integer itemId,
			String attrType, String newAttrContent) throws Exception {
		// 返回对象声明
		HttpResult<Item> httpResult = new HttpResult<Item>();
		if (null == itemId || 0 == itemId) {
			httpResult.setCode(401);
			httpResult.setMsg("项目主键不能是：" + itemId);
			return httpResult;
		} else if (null == attrType || "".equals(attrType.trim())
				|| null == newAttrContent || "".equals(newAttrContent.trim())) {
			httpResult.setCode(401);
			httpResult.setMsg("参数错误！attrType:" + attrType + " &newAttrContent:"
					+ newAttrContent);
			return httpResult;

		}
		;
		Item item = new Item();
		item.setId(itemId);
		item.setComId(userInfo.getComId());
		if ("itemName".equals(attrType)) {
			item.setItemName(newAttrContent);
			// 项目名称变更
			boolean succ = itemService.updateItemName(item, userInfo);
			item.setSucc(succ);
			if (succ) {
				// 模块日志添加
				itemService.addItemLog(userInfo.getComId(), item.getId(),
						userInfo.getId(), userInfo.getUserName(), "项目名称变更为\""
								+ item.getItemName() + "\"");
				item.setPromptMsg("更新成功");
			} else {
				// 模块日志添加
				itemService.addItemLog(userInfo.getComId(), item.getId(),
						userInfo.getId(), userInfo.getUserName(), "项目名称变更为\""
								+ item.getItemName() + "\"失败");
				item.setPromptMsg("更新失败");
			}
		} else if ("itemRemark".equals(attrType)) {
			item.setItemRemark(newAttrContent);
			// 项目说明变更
			boolean succ = itemService.updateItemItemRemark(item, userInfo);
			item.setSucc(succ);
			if (succ) {
				// 模块日志添加
				itemService.addItemLog(userInfo.getComId(), item.getId(),
						userInfo.getId(), userInfo.getUserName(), "项目说明变更成功");
				item.setPromptMsg("更新成功");
			} else {
				// 模块日志添加
				itemService.addItemLog(userInfo.getComId(), item.getId(),
						userInfo.getId(), userInfo.getUserName(), "项目说明变更失败");
				item.setPromptMsg("更新失败");
			}

		} else if ("itemProgress".equals(attrType)) {
			// 汇报项目进度
			item.setItemProgress(Integer.parseInt(newAttrContent));
			String itemProgressDescribe = CommonUtil.dataDicZvalueByCode(
					"progress", null != item.getItemProgress() ? item
							.getItemProgress().toString() : null);
			item.setItemProgressDescribe(itemProgressDescribe);
			boolean succ = itemService.updateItemProgress(item, userInfo);
			item.setSucc(succ);
			if (succ) {
				// 模块日志添加
				itemService.addItemLog(userInfo.getComId(), item.getId(),
						userInfo.getId(), userInfo.getUserName(), "汇报进度:\""
								+ itemProgressDescribe + "\"");
				item.setPromptMsg("更新成功");
			} else {
				// 模块日志添加
				itemService.addItemLog(userInfo.getComId(), item.getId(),
						userInfo.getId(), userInfo.getUserName(), "汇报进度:\""
								+ itemProgressDescribe + "\"失败");
				item.setPromptMsg("更新失败");
			}
		} else if ("itemParent".equals(attrType)) {
			// 项目母项目关联
			item.setParentId(Integer.parseInt(newAttrContent));
			// 查询项目当前设置父节点的父节点
			List<Item> parentItem = itemService.getParentItem(
					item.getParentId(), userInfo.getComId());
			if (null != parentItem && parentItem.size() > 0) {
				// 验证是否为自己的子项目
				for (Item itemTemp : parentItem) {
					if (itemTemp.getId().equals(item.getId())) {
						httpResult.setCode(-1);
						httpResult.setMsg("不能关联子项目");
						return httpResult;
					}
				}
			}
			boolean succ = itemService.updateItemParentId(item, userInfo);
			item.setSucc(succ);
			if (succ) {
				item.setPromptMsg("更新成功");
			} else {
				item.setPromptMsg("更新失败");
			}
		} else if ("itemSonAdd".equals(attrType)) {
			// 项目分解
			item.setId(null);
			// 获取被分解的项目主键
			item.setParentId(itemId);
			// 企业号
			item.setComId(userInfo.getComId());
			// 创建人
			item.setCreator(userInfo.getId());
			// 删除标识(正常)
			item.setDelState(0);
			// 项目状态标识
			item.setState(1);
			itemService.addItem(item, userInfo);
			item.setSucc(true);
			item.setPromptMsg("分解成功");
		} else if ("crmRelate".equals(attrType)) {
			// 客户关联
			item.setPartnerId(Integer.parseInt(newAttrContent));
			;
			boolean succ = itemService.updateItemPartner(item, userInfo);
			item.setSucc(succ);
			if (succ) {
				item.setPromptMsg("更新成功");
			} else {
				item.setPromptMsg("更新失败");
			}
		} else if ("itemSharer".equals(attrType)) {
			// 共享人更新
			StringBuffer sharerName = new StringBuffer();
			;
			Gson gson = new Gson();
			List<UserInfo> list = gson.fromJson(newAttrContent,
					new TypeToken<List<UserInfo>>() {
					}.getType());
			Integer[] userIds = new Integer[list.size()];
			int i = 0;
			for (UserInfo user : list) {
				userIds[i++] = user.getId();
				sharerName.append(user.getUserName() + ",");
			}
			if (userIds.length > 0) {
				sharerName = new StringBuffer(sharerName.subSequence(0,
						sharerName.lastIndexOf(",")));
				boolean succ = itemService.updateItemSharer(userInfo, itemId,
						userIds, sharerName.toString());
				item.setSucc(succ);
				if (succ) {
					// 模块日志添加
					itemService.addItemLog(userInfo.getComId(), itemId,
							userInfo.getId(), userInfo.getUserName(),
							"项目参与人变更为\"" + sharerName.toString() + "\"");
					item.setPromptMsg("更新成功");
				} else {
					// 模块日志添加
					itemService.addItemLog(userInfo.getComId(), itemId,
							userInfo.getId(), userInfo.getUserName(),
							"项目参与人变更为\"" + sharerName.toString() + "\"失败");
					item.setPromptMsg("更新失败");
				}
			} else {
				item.setSucc(false);
				item.setPromptMsg("更新失败");
			}

		}

		httpResult.setData(item);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 删除项目关联
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param itemId
	 *            项目主键
	 * @param busId
	 *            关联模块主键
	 * @param busType
	 *            关联模块类型
	 * @return
	 */
	public HttpResult<Object> itemDelRelation(UserInfo userInfo,
			Integer itemId, Integer busId, String busType) {

		HttpResult<Object> result = new HttpResult<Object>();
		Item item = new Item();
		item.setId(itemId);
		item.setComId(userInfo.getComId());
		// 解除父项目关联
		if (busType.equals(ConstantInterface.TYPE_ITEM)) {
			item.setParentId(busId);
			boolean succ = itemService.delpItemRelation(item, userInfo);
			if (succ) {
				result.setCode(HttpResult.CODE_OK);
			} else {
				result.setCode(-1);
				result.setMsg("解除关联失败");
			}
		}
		// 解除客户关联
		else if (busType.equals(ConstantInterface.TYPE_CRM)) {
			item.setPartnerId(busId);
			boolean succ = itemService.delCrmRelation(item, userInfo);
			if (succ) {
				result.setCode(HttpResult.CODE_OK);
			} else {
				result.setCode(-1);
				result.setMsg("解除客户关联失败");
			}
		}
		return result;

	}

	/**************************************** 以下客户业务 ***************************************************/
	/**
	 * 获取客户数据集合
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param taskType
	 *            查看的任务类型
	 */
	public List<Customer> crmList(UserInfo userInfo, String crmType,
			int customerTypeId, int areaId, int owner, String crmName) {
		if (null == userInfo || null == crmType || "".equals(crmType.trim())) {
			return null;
		}
		List<Customer> crmList = null;
		Customer customer = new Customer();
		customer.setComId(userInfo.getComId());
		if( customerTypeId > 0){
			customer.setCustomerTypeId(customerTypeId);
		}
		if(areaId > 0){
			customer.setAreaId(areaId);
		}
		// 若是没有下属，则没有负责人类别一说
		if (userInfo.getCountSub() <= 0) {
			customer.setOwnerType(null);
		}
		if ("allCrm".equals(crmType)) {
			// 获取所有客户
			customer.setOwner(owner);
			if (null != crmName && !"".equals(crmName)) {
				customer.setCustomerName(crmName);
			}
			crmList = crmService.listCustomerForPage(customer,userInfo);
		} else if ("myCrm".equals(crmType)) {
			// 获取我的客户
			customer = new Customer();
			customer.setComId(userInfo.getComId());
			customer.setOwnerType("0");
			customer.setCustomerTypeId(customerTypeId);
			customer.setAreaId(areaId);
			crmList = crmService.listCustomerForPage(customer,userInfo);
		} else if ("subCrm".equals(crmType)) {
			// 获取下属客户
			customer = new Customer();
			customer.setComId(userInfo.getComId());
			customer.setOwnerType("1");
			customer.setCustomerTypeId(customerTypeId);
			customer.setAreaId(areaId);
			customer.setOwner(owner);
			crmList = crmService.listCustomerForPage(customer,userInfo);
		} else if ("handsCrm".equals(crmType)) {
			// 移交客户
			customer = new Customer();
			customer.setComId(userInfo.getComId());
			customer.setCustomerTypeId(customerTypeId);
			customer.setAreaId(areaId);
			customer.setOwner(owner);
			crmList = crmService.listCustomerHandsForPage(customer,
					userInfo.getId());
		} else if ("addByMonthCrm".equals(crmType)) {
			// 本月新增
			customer = new Customer();
			customer.setComId(userInfo.getComId());
			customer.setCustomerTypeId(customerTypeId);
			customer.setAreaId(areaId);
			customer.setOwner(owner);
			crmList = crmService.listCustomerAddByMonthForPage(customer,userInfo);
		}
		return crmList;
	}

	/**
	 * 获取区域数据集
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @return
	 */
	public List<Area> crmAreaList(UserInfo userInfo) {
		if (null == userInfo) {
			return null;
		}
		// 获取客户区域数据源
		List<Area> listArea = crmService.listArea(userInfo.getComId());
		return listArea;
	}
	/**
	 * 获取区域数据集
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @return
	 */
	public List<Area4App> crmAreaTreeList(UserInfo userInfo) {
		if (null == userInfo) {
			return null;
		}
		// 获取客户区域数据源
		List<Area4App> listArea = crmService.crmAreaTreeList(userInfo);
		return listArea;
	}

	/**
	 * 获取区域数据集
	 * 
	 * @param userInfo
	 * @param areaPId
	 * @return
	 */
	public List<Area> crmAreaListForRelevance(UserInfo userInfo, Integer areaPId) {
		// 获取客户区域数据源
		List<Area> listArea = crmService.listCrmAreaForRelevance(
				userInfo.getComId(), areaPId);
		return listArea;
	}

	/**
	 * 分页查询权限范围项目
	 * 
	 * @param userInfo
	 * @param crm
	 * @return
	 */
	public List<Customer> listCrmForRelevance(UserInfo userInfo, Customer crm) {
		List<Customer> crmList = crmService.listCrmForRelevance(crm, userInfo);
		return crmList;
	}

	/**
	 * 获取客户类型数据集
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @return
	 */
	public List<CustomerType> crmTypeList(UserInfo userInfo) {
		if (null == userInfo) {
			return null;
		}
		// 获取客户类型数据源
		List<CustomerType> listCustomerType = crmService
				.listCustomerType(userInfo.getComId());
		return listCustomerType;
	}
	/**
	 * 获取客户类型数据集
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @return
	 */
	public List<CustomerStage> crmStageList(UserInfo userInfo) {
		if (null == userInfo) {
			return null;
		}
		// 获取客户类型数据源
		List<CustomerStage> listCustomerType = crmService
				.listCustomerStage(userInfo.getComId());
		return listCustomerType;
	}

	/**
	 * 客户新增
	 * 
	 * @param userInfo
	 * @param customerStr
	 *            客户配置信息
	 * @return
	 * @throws Exception
	 */
	public HttpResult<Object> crmAdd(UserInfo userInfo, String customerStr)
			throws Exception {
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();
		Gson gson = new Gson();
		Customer customer = gson.fromJson(customerStr, Customer.class);
		// 团队主键
		customer.setComId(userInfo.getComId());
		// 客户创建人
		customer.setCreator(userInfo.getId());
		// 删除标识(正常)
		customer.setDelState(0);
		//设置私有
		Integer pubState = customer.getPubState();
		if(null == pubState){
			customer.setPubState(PubStateEnum.NO.getValue());
		}
		// 初始化话客户所属区域ID
		if (null != customer.getAreaIdAndType()
				&& !"".equals(customer.getAreaIdAndType())) {
			customer.setAreaId(Integer.parseInt(customer.getAreaIdAndType()
					.split("@")[0]));
		}
		
		//处理联系人信息2018.6.8
		List<LinkMan> listLinkMan = customer.getListLinkMan();
		if(listLinkMan != null && listLinkMan.size() > 0){
			for (int i = 0; i < listLinkMan.size(); i++) {
				OutLinkMan olm = new OutLinkMan();
				olm.setComId(listLinkMan.get(i).getComId());
				olm.setCreator(userInfo.getId());
				olm.setEmail(listLinkMan.get(i).getEmail());
				olm.setPubState(1);
				olm.setLinePhone(listLinkMan.get(i).getLinePhone());
				olm.setLinkManName(listLinkMan.get(i).getLinkManName());
				olm.setMovePhone(listLinkMan.get(i).getMovePhone());
				olm.setPost(listLinkMan.get(i).getPost());
				olm.setQq(listLinkMan.get(i).getQq());
				olm.setWechat(listLinkMan.get(i).getWechat());
				//添加外部联系人记录
				Integer olmId = mobileDao.add(olm);
				//设置id用于添加关系表
				listLinkMan.get(i).setId(olmId);
				
			}
		}
		
		
		// 添加客户并保存
		crmService.addCustomer(customer, userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}

	/**
	 * 客户详情查看
	 * 
	 * @param userInfo
	 *            操作人信息对象
	 * @param crmId
	 *            客户主键
	 * @return
	 */
	public HttpResult<Customer> crmDetail(UserInfo userInfo, Integer crmId) {
		if (null == crmId || 0 == crmId){
			return null;
		}
		// 返回对象声明
		HttpResult<Customer> httpResult = new HttpResult<Customer>();
		Customer crm = crmService.queryCustomer(userInfo, crmId);
		// 一次加载行数
		PaginationContext.setPageSize(9);
		// 客户反馈数据集
		crmService.listFeedBackInfo(userInfo.getComId(), crmId,"app");
		// 留言总数
		crm.setSumOfMsg(PaginationContext.getTotalCount());

		Task task = new Task();
		task.setBusId(crmId);
		task.setBusType(ConstantInterface.TYPE_CRM);
		// 查询客户关联任务数
		Integer crmTaskSum = taskService.countBusTask(task, userInfo);

		crm.setCrmTaskSum(crmTaskSum);

		//取得客户的项目数
		Integer crmItemSum = itemService.countBusItem("012",crmId,userInfo);
		crm.setCrmItemSum(crmItemSum);

		// 获取模块操作权限
		List<ModuleOperateConfig> optCfgs = modOptConfService
				.listModuleOperateConfig(userInfo.getComId(),
						ConstantInterface.TYPE_CRM);
		crm.setOptCfgs(optCfgs);

		httpResult.setData(crm);
		httpResult.setCode(HttpResult.CODE_OK);

		// 查看客户信息，删除客户提醒
		todayWorksService.updateTodoWorkRead(crmId, userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_CRM, 0);
		return httpResult;

	}

	/**
	 * 客户反馈记录数据集
	 * 
	 * @param userInfo
	 *            操作人信息对象
	 * @param crmId
	 *            客户主键
	 * @param onMoreNum
	 *            连续上拉次数
	 * @return
	 */
	public HttpResult<List<FeedBackInfo>> crmMsgList(UserInfo userInfo,
			Integer crmId, Integer onMoreNum) {
		if (null == crmId || 0 == crmId){
			return null;
		}
		// 一次加载行数
		PaginationContext.setPageSize(9);
		PaginationContext
				.setOffset(onMoreNum * PaginationContext.getPageSize());
		// 返回对象声明
		HttpResult<List<FeedBackInfo>> httpResult = new HttpResult<List<FeedBackInfo>>();
		List<FeedBackInfo> listFeedBackInfo = crmService.listFeedBackInfo(
				userInfo.getComId(), crmId,"app");
		httpResult.setData(listFeedBackInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}

	/**
	 * 客户反馈类型数据集
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @return
	 */
	public HttpResult<List<FeedBackType>> crmFeedBackTypeList(UserInfo userInfo) {
		// 返回对象声明
		HttpResult<List<FeedBackType>> httpResult = new HttpResult<List<FeedBackType>>();
		List<FeedBackType> listFeedBackType = crmService
				.listFeedBackType(userInfo.getComId());
		httpResult.setData(listFeedBackType);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}

	/**
	 * 客户移交
	 * 
	 * @param userInfo
	 * @param customerHandOver
	 * @return
	 */
	public HttpResult<String> crmHandOver(UserInfo userInfo,
			CustomerHandOver customerHandOver) {
		// 返回对象声明
		HttpResult<String> httpResult = new HttpResult<String>();
		boolean succ = crmService.addCustomerHandOver(
				new CustomerHandOver[] { customerHandOver }, userInfo);
		if (succ) {
			httpResult.setData("移交成功！");
			httpResult.setCode(HttpResult.CODE_OK);
		} else {
			httpResult.setMsg("移交失败！");
			httpResult.setCode(-1);
		}
		return httpResult;

	}

	/**
	 * 客户类型更新
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param customer
	 *            更新对象
	 * @return
	 * @throws Exception
	 */
	public HttpResult<Customer> crmTypeIdUpdate(UserInfo userInfo,
			Integer crmId, Integer customerTypeId) throws Exception {
		if (null == customerTypeId || null == crmId){
			return null;
		}
		// 返回对象声明
		HttpResult<Customer> httpResult = new HttpResult<Customer>();
		Customer customer = new Customer();
		if (null != customerTypeId && 0 != customerTypeId) {
			customer.setId(crmId);
			customer.setCustomerTypeId(customerTypeId);
			customer.setComId(userInfo.getComId());
			CustomerType customerType = crmService.queryCustomerType(
					userInfo.getComId(), customer.getCustomerTypeId());
			customer.setTypeName(customerType.getTypeName());
			boolean succ = crmService.updateCustomerTypeId(customer, userInfo);
			customer.setSucc(succ);
			if (succ) {
				customer.setPromptMsg("更新成功");
			} else {
				customer.setPromptMsg("更新失败");
			}
		} else {
			customer.setSucc(false);
			customer.setPromptMsg("客户类型为空");
		}
		httpResult.setData(customer);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 客户属性更新
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
	 * @throws Exception
	 */
	public HttpResult<Customer> crmAttrUpdate(UserInfo userInfo, Integer crmId,
			String attrType, String newAttrContent) throws Exception {
		// 返回对象声明
		HttpResult<Customer> httpResult = new HttpResult<Customer>();
		if (null == crmId || 0 == crmId) {
			httpResult.setCode(401);
			httpResult.setMsg("客户主键不能是：" + crmId);
			return httpResult;
		} else if (null == attrType || "".equals(attrType.trim())
				|| null == newAttrContent || "".equals(newAttrContent.trim())) {
			httpResult.setCode(401);
			httpResult.setMsg("参数错误！attrType:" + attrType + " &newAttrContent:"
					+ newAttrContent);
			return httpResult;

		}
		;
		Customer customer = new Customer();
		customer.setId(crmId);
		customer.setComId(userInfo.getComId());
		if ("crmName".equals(attrType)) {
			// 客户名称更新
			customer.setCustomerName(newAttrContent);
			boolean succ = crmService.updateCustomerName(customer, userInfo);
			customer.setSucc(succ);
			if (succ) {
				customer.setPromptMsg("更新成功");
			} else {
				customer.setPromptMsg("更新失败");
			}
		} else if ("crmAddress".equals(attrType)) {
			// 客户地址更新
			customer.setAddress(newAttrContent);
			boolean succ = crmService.updateCustomerAddress(customer, userInfo);
			customer.setSucc(succ);
			if (succ) {
				customer.setPromptMsg("更新成功");
			} else {
				customer.setPromptMsg("更新失败");
			}
		} else if ("crmPost".equals(attrType)) {
			// 客户右边更新
			int postCode = 0;
			try {
				postCode = Integer.parseInt(newAttrContent);
			} catch (NumberFormatException e) {
				customer.setSucc(false);
				customer.setPromptMsg("更新失败,\"" + newAttrContent + "\"不能作为邮编。");
				e.printStackTrace();
			}
			if (postCode != 0) {
				customer.setPostCode(postCode);
				boolean succ = crmService.updateCustomerPostCode(customer,
						userInfo);
				customer.setSucc(succ);
				if (succ) {
					customer.setPromptMsg("更新成功");
				} else {
					customer.setPromptMsg("更新失败");
				}
			}
		} else if ("crmPhone".equals(attrType)) {
			// 客户联系电话更新
			customer.setLinePhone(newAttrContent);
			boolean succ = crmService.updateCustomerLinePhone(customer,
					userInfo);
			customer.setSucc(succ);
			if (succ) {
				customer.setPromptMsg("更新成功");
			} else {
				customer.setPromptMsg("更新失败");
			}
		} else if ("crmFax".equals(attrType)) {
			// 客户传真更新
			customer.setFax(newAttrContent);
			boolean succ = crmService.updateCustomerFax(customer, userInfo);
			customer.setSucc(succ);
			if (succ) {
				customer.setPromptMsg("更新成功");
			} else {
				customer.setPromptMsg("更新失败");
			}
		} else if ("crmRemark".equals(attrType)) {
			// 客户备注更新
			customer.setCustomerRemark(newAttrContent);
			boolean succ = crmService.updateCustomerRemark(customer, userInfo);
			customer.setSucc(succ);
			if (succ) {
				customer.setPromptMsg("更新成功");
			} else {
				customer.setPromptMsg("更新失败");
			}
		} else if ("crmSharer".equals(attrType)) {
			// 客户共享人更新
			Gson gson = new Gson();
			List<UserInfo> list = gson.fromJson(newAttrContent,
					new TypeToken<List<UserInfo>>() {
					}.getType());
			Integer[] userIds = new Integer[list.size()];
			int i = 0;
			for (UserInfo user : list) {
				userIds[i++] = user.getId();
			}
			if (userIds.length > 0) {
				boolean succ = crmService.updateCustomerSharer(
						userInfo.getComId(), crmId, userIds, userInfo);
				customer.setSucc(succ);
				if (succ) {
					customer.setPromptMsg("更新成功");
				} else {
					customer.setPromptMsg("更新失败");
				}
			} else {
				customer.setSucc(false);
				customer.setPromptMsg("更新失败");

			}

		}
		// 更新区域
		else if ("area".equals(attrType)) {
			customer.setAreaId(Integer.parseInt(newAttrContent));
			boolean succ = crmService.updateCustomerAreaId(customer, userInfo);
			customer.setSucc(succ);
			if (succ) {
				customer.setPromptMsg("更新成功");
			} else {
				customer.setPromptMsg("更新失败");
			}
		}

		httpResult.setData(customer);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 客户属性更新-联系人新增
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
	 * @throws Exception
	 */
	public HttpResult<LinkMan> crmAddLinkMan(UserInfo userInfo, LinkMan linkMan) {
		// 返回对象声明
		HttpResult<LinkMan> httpResult = new HttpResult<LinkMan>();
		try {
			linkMan.setComId(userInfo.getComId());
			Integer id = crmService.addLinkMan(linkMan, userInfo);
			linkMan.setId(id);
			httpResult.setData(linkMan);
			httpResult.setCode(HttpResult.CODE_OK);
		} catch (Exception e) {
			httpResult.setCode(-1);
			httpResult.setMsg("联系人添加失败");
		}
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
	 * @throws ParseException
	 */
	public List<WeekReport> weekRepList(UserInfo userInfo,
			WeekReportPojo weekReport) throws ParseException {
		PageBean<WeekReport> pageBean = weekRepService.listPagedWeekReport(weekReport,userInfo);
		List<WeekReport>list = pageBean.getRecordList();
		List<WeekReport> result = new ArrayList<WeekReport>();
		if (null != list && !list.isEmpty()) {
			for (WeekReport obj : list) {
				// 周报已发布
				if (obj.getState().equals("0")) {

					List<WeekReportQ> listWeekQ = weekRepService
							.listWeekReportQ(obj.getId(), userInfo.getComId(),
									obj.getReporterId());
					obj.setWeekReportQs(listWeekQ);
				}
				result.add(obj);
			}
		}

		return list;
	}

	/**
	 * 获取周报详情
	 * 
	 * @param userInfo
	 * @param reporterId
	 * @return
	 * @throws ParseException
	 */
	public HttpResult<WeekReport> weekRepDetail(UserInfo userInfo,
			Integer reporterId) throws ParseException {
		// 返回对象声明
		HttpResult<WeekReport> httpResult = new HttpResult<WeekReport>();

		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
		// 取得所选周报
		WeekReport weekReport = weekReportService.getWeekReportForView(
				reporterId, userInfo, new WeekReportPojo(),isForceIn);
		// 查看权限验证
		if ((weekReportService.authorCheck(userInfo.getComId(), reporterId,
				userInfo.getId(),weekReport.getId()) || isForceIn)) {
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_WEEK,
					reporterId);
			// 添加查看记录
			viewRecordService.addViewRecord(userInfo, viewRecord);
			if (null != weekReport) {
				StringBuffer content = null;
				// 周报汇报项
				List<WeekReportQ> weekReportQs = weekReport.getWeekReportQs();
				if(null != weekReportQs && !weekReportQs.isEmpty()){
					for (WeekReportQ row : weekReportQs) {
						content = new StringBuffer();
						if (null != row.getReportVal()
								&& !"".equals(row.getReportVal())) {
							content.append("\b\b"
									+ row.getReportVal()
									+ "\n");
						} else {
							content.append("\b\b" + "未填写\n");
						}
						row.setReportVal(content.toString());
					}
				}
				// 下周计划
				if ("1".equals(weekReport.getHasPlan())) {
					content = new StringBuffer();
					List<WeekReportPlan> weekReportPlans = weekReport
							.getWeekReportPlans();
					// 序号
					int i = 1;
					for (WeekReportPlan plan : weekReportPlans) {
						content.append((weekReportPlans.size() > 1 ? "\b\b" + i
								+ "、" : "\b\b"));
						content.append(plan.getPlanContent());
						content.append("\b\b"
								+ (null == plan.getPlanTime() ? "" : plan
										.getPlanTime()) + "\n");
						i++;
					}
					weekReport.setHasPlan(content.toString());
				}
				weekReport.setContent(("".equals(content.toString()) ? "未做汇报"
						: content.toString()));
				// 反馈留言
				List<WeekRepTalk> weekTalks = weekReportService
						.listPagedWeekRepTalk(reporterId, userInfo.getComId(),"app");
				weekReport.setAllMsgNum(PaginationContext.getTotalCount());
				if (null != weekTalks && !weekTalks.isEmpty()) {
					for (WeekRepTalk vo : weekTalks) {
						vo.setContent(vo.getContent());
					}
				}
				weekReport.setListWeekTalk(weekTalks);
			}
			httpResult.setCode(HttpResult.CODE_OK);
			httpResult.setData(weekReport);
		} else {
			httpResult.setCode(-1);
			httpResult.setMsg("抱歉，你没有查看权限");

		}
		// 查看周报，删除消息提醒
		todayWorksService.updateTodoWorkRead(reporterId, userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_WEEK, 0);
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
	public HttpResult<List<WeekRepTalk>> weekRepTalkList(UserInfo userInfo,
			Integer reporterId) throws ParseException {
		// 返回对象声明
		HttpResult<List<WeekRepTalk>> httpResult = new HttpResult<List<WeekRepTalk>>();
		// 反馈留言
		List<WeekRepTalk> weekTalks = weekReportService.listPagedWeekRepTalk(
				reporterId, userInfo.getComId(),"app");
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(weekTalks);
		// 查看周报，删除消息提醒
		todayWorksService.updateTodoWorkRead(reporterId, userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_WEEK, 0);
		return httpResult;
	}

	/**
	 * 
	 * 周报汇报配置初始化
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param startDate
	 *            汇报返回的开始日期
	 * @return
	 * @throws Exception
	 */
	public HttpResult<WeekReport> weekRepInit(UserInfo userInfo,
			String startDate) throws Exception {
		// 返回对象声明
		HttpResult<WeekReport> httpResult = new HttpResult<WeekReport>();

		// 选中的时间
		String tempNowDate = startDate;
		// 当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateTimeUtil.parseDate(nowDate, DateTimeUtil.c_yyyy_MM_dd_));
		int dayOfWeek = DateTimeUtil.getDay(cal);
		
		// 没有选择时间，则判断是否在星期四之后
		if (null == tempNowDate || "".equals(tempNowDate)) {
			// 不是星期四之后，则减一周，上周周报
			if (dayOfWeek < 6 && dayOfWeek > 1) {
				nowDate = DateTimeUtil.addDate(nowDate,
						DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, -7);
			}
			// 选择的时间为整理后的当前时间
			tempNowDate = nowDate;
		}

		// 日期所在周数
		Integer weekNum = DateTimeUtil.getWeekOfYear(tempNowDate,
				DateTimeUtil.c_yyyy_MM_dd_);
		// 所选日期所在周的第一天
		String weekS = DateTimeUtil.getFirstDayOfWeek(tempNowDate,
				DateTimeUtil.c_yyyy_MM_dd_);
		// 所选日期所在周的最后天
		String weekE = DateTimeUtil.getLastDayOfWeek(tempNowDate,
				DateTimeUtil.c_yyyy_MM_dd_);

		String weeksYear = weekS.substring(0, 4);
		String weekEYear = weekE.substring(0, 4);
		String weekYear = weeksYear;
		if (!weeksYear.equals(weekEYear)) {
			weekYear = weekEYear;
		}

		// 取得所选日期所写周报
		WeekReport weekReport = weekReportService.initWeekReport(weekNum,
				userInfo, weekYear, weekS, weekE);

		// 周报已经发布了
		if (null != weekReport && weekReport.getCountVal() > 0
				&& "0".equals(weekReport.getState())) {
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_WEEK,
					weekReport.getId());
			// 添加查看记录
			viewRecordService.addViewRecord(userInfo, viewRecord);
		}

		List<String> validateStatus = new ArrayList<String>();
		//当前操作人员的入职时间
		String hireDateStr = null;
		//系统已断开连接
		if(null != userInfo){
			hireDateStr = userInfo.getHireDate();
			//入职时间不为空
			if(null!=hireDateStr){
				hireDateStr = hireDateStr.substring(0,10);
				Date hireDate = DateTimeUtil.parseDate(hireDateStr, DateTimeUtil.yyyy_MM_dd);
				
				tempNowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
				//当前时间所在周的一周的第一天
				String chooseDateWeekSStr = DateTimeUtil.getFirstDayOfWeek(tempNowDate,DateTimeUtil.c_yyyy_MM_dd_);
				chooseDateWeekSStr = DateTimeUtil.addDate(chooseDateWeekSStr, DateTimeUtil.c_yyyy_MM_dd_,
						Calendar.DAY_OF_YEAR, -2);
				Date nowDateWeekS = DateTimeUtil.parseDate(chooseDateWeekSStr, DateTimeUtil.c_yyyy_MM_dd_);
				
				//当前时间的星期五
				String chooseDateWeekEStr = DateTimeUtil.getLastDayOfWeek(tempNowDate,DateTimeUtil.c_yyyy_MM_dd_);
				chooseDateWeekEStr = DateTimeUtil.addDate(chooseDateWeekEStr, DateTimeUtil.c_yyyy_MM_dd_,
						Calendar.DAY_OF_YEAR, -2);
				Date nowDateWeekE = DateTimeUtil.parseDate(chooseDateWeekEStr, DateTimeUtil.c_yyyy_MM_dd_);
				
				//入职时间在上周五到本周四之间的不需要显示上周的
				//在本周入职的且在星期五之前，是新员工
				boolean state = hireDate.getTime() >= nowDateWeekS.getTime() 
						&& hireDate.getTime() < nowDateWeekE.getTime() && (
								dayOfWeek<6 && dayOfWeek>1);
				if(state){
					//新员工
					validateStatus.add("f2");
				}
			}
		}
		
		List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(userInfo);
		boolean hasLeaderState = (null == listImmediateSuper || listImmediateSuper.isEmpty()) && "0".equals(userInfo.getIsChief());
		if(hasLeaderState){
			//没有设定上级
			validateStatus.add("f1");
		}
		weekReport.setValidateStatus(validateStatus);
		
		httpResult.setData(weekReport);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}

	/**
	 * 周报汇报
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param weekReportStr
	 *            周报汇报详情
	 * @return
	 * @throws Exception
	 */
	public HttpResult<Object> weekRepAdd(UserInfo userInfo, String weekReportStr)
			throws Exception {
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();
		
		//获取个人直属上级集合
		List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(userInfo);
		if(null==listImmediateSuper || listImmediateSuper.isEmpty()){
			httpResult.setData("f");
			httpResult.setCode(HttpResult.CODE_OK);
			return httpResult;
		}
		Gson gson = new Gson();
		WeekReport weekReport = gson.fromJson(weekReportStr, WeekReport.class);
		weekReport.setState("0");
		// 周报条目
		List<WeekReportQ> weekReports = weekReport.getWeekReportQs();
		List<WeekReportVal> weekReportVals = new ArrayList<WeekReportVal>();
		for (WeekReportQ weekReportQ : weekReports) {
			String reportName = weekReportQ.getReportName();
			WeekReportVal weekReportVal = new WeekReportVal();
			// 企业
			weekReportVal.setComId(userInfo.getComId());
			// 周报内容要求主键字符串
			String weekReportIdStr = reportName.substring(
					reportName.lastIndexOf("_") + 1, reportName.length());
			weekReportVal.setQuestionId(Integer.parseInt(weekReportIdStr));
			// 周报主键
			weekReportVal.setWeekReportId(weekReport.getId());
			// 周报值
			String val = weekReportQ.getReportVal();
			weekReportVal.setReportValue(val);
			// 没有填值则不算，空格不算
			if (null != val && !"".equals(val) && !"".equals(val.trim())) {
				weekReportVals.add(weekReportVal);
			}
		}
		weekReport.setWeekReportVals(weekReportVals);

		// 设置成了分享
		if (null != weekReport.getIsShare()) {
			weekReport.setIsShare(1);
		} else {// 设置成不分享
			weekReport.setIsShare(0);
		}
		// 周报计划
		if (null != weekReport.getWeeklyPlan()
				&& !"".equals(weekReport.getWeeklyPlan().trim())) {
			List<WeekReportPlan> weekReportPlans = new ArrayList<WeekReportPlan>();
			WeekReportPlan plan = new WeekReportPlan();
			plan.setPlanContent(weekReport.getWeeklyPlan());
			plan.setWeekReportId(weekReport.getId());
			weekReportPlans.add(plan);
			weekReport.setWeekReportPlans(weekReportPlans);
		}
		// 填写周报
		weekReportService.addWeekReport(weekReport, userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}
	/**
	 * 列出周报查看人员
	 * @param sessionUser
	 * @return
	 */
	public HttpResult<List<WeekViewer>> listWeekViewer(UserInfo sessionUser) {
		HttpResult<List<WeekViewer>> httpResult = new HttpResult<List<WeekViewer>>();
		List<WeekViewer> listWeekViewer = weekReportService.listWeekViewer(sessionUser);
		httpResult.setData(listWeekViewer);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 修改周报查看人员
	 * @param sessionUser 当前操作人员
	 * @param weekViewerStr 周报查看人员json对象
	 * @return
	 */
	public HttpResult<String> updateWeekViewers(UserInfo sessionUser,String weekViewerStr) {
		HttpResult<String> httpResult = new HttpResult<String>();
		
		List<WeekViewer> weekViewerList = JSONArray.parseArray(weekViewerStr, WeekViewer.class);
		StringBuilder stringBuilder = new StringBuilder();
		if(null!=weekViewerList && !weekViewerList.isEmpty()){
			for (WeekViewer weekViewer : weekViewerList) {
				weekViewer.setUserId(sessionUser.getId());
				weekViewer.setComId(sessionUser.getComId());
				
				stringBuilder = stringBuilder.append(",");
				stringBuilder = stringBuilder.append(weekViewer.getViewerName());
			}
		}
		String logContent = stringBuilder.toString();
		if(!"".equals(logContent)){
			logContent = logContent.substring(1,logContent.length());
		}
		
		weekReportService.updateWeekViewers(weekViewerList,sessionUser,logContent);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 删除周报的查看人员
	 * @param sessionUser
	 * @param weekViewer
	 * @return
	 */
	public HttpResult<String> delWeekViewer(UserInfo sessionUser,WeekViewer weekViewer) {
		HttpResult<String> httpResult = new HttpResult<String>();
		weekReportService.delWeekViewer(weekViewer,sessionUser);
		httpResult.setCode(HttpResult.CODE_OK);
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
	 *            所在文件夹
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> fileList(UserInfo userInfo, String modTypes,
			String fileScope, Integer pageNum, String fileTypes,
			Integer classifyId, String fileName) {
		// 查询结果
		Map<String, String> map = new HashMap<String, String>(2);

		// 附件详情
		FileDetail fileDetail = new FileDetail();
		// 设置团队号
		fileDetail.setComId(userInfo.getComId());
		// 设置当前人员
		fileDetail.setSessionUser(userInfo.getId());
		// 设置用户
		fileDetail.setUserId(userInfo.getId());
		// 设置所在文件夹
		fileDetail.setClassifyId(classifyId);
		// 设置查询范围
		fileDetail.setSearchMe(fileScope);

		// 设置查询文件名称
		fileDetail.setFileName(fileName);

		// 没有设置所在文件夹则顶层文件夹
		if (null == classifyId || classifyId == 0) {
			fileDetail.setClassifyId(-1);
		}
		// 数组集合化(模块)
		List<String> modList = null;
		if (null != modTypes && !"".equals(modTypes)) {
			modList = new ArrayList<String>();
			modList.add(modTypes);
		}
		// 数组集合化（文档类型）
		List<String> fileTypeList = null;
		if (null != fileTypes && !"".equals(fileTypes)) {
			fileTypeList = new ArrayList<String>();
			fileTypeList.add(fileTypes);
		}
		
		Gson gson = new Gson();
		Map<String,Object> fileDirmap = fileService.listPagedFolderAndFile(fileDetail, userInfo, pageNum);
		List<FileClassify> classifies = (List<FileClassify>) fileDirmap.get("folders");
		if(null!= classifies && !classifies.isEmpty()){
			map.put("dirs", gson.toJson(classifies));
		}else{
			map.put("dirs", null);
		}
		
		// 查询文件
		PageBean<FileDetail> pageBean = (PageBean<FileDetail>) fileDirmap.get("pageBean");
		List<FileDetail> listFiles = pageBean.getRecordList();	
		if (null != listFiles && !listFiles.isEmpty()) {
			map.put("files", gson.toJson(listFiles));
		} else {
			map.put("files", null);
		}
		return map;
	}
	
	/**
	 * 添加文件夹
	 * @param userInfo  当前操作人员
	 * @param fileClassifyStr 文件夹的字符串信息
	 * @return
	 */
	public HttpResult<Void> addFileClassify(UserInfo userInfo, String fileClassifyStr){
		HttpResult<Void> httpResult = new HttpResult<Void>();
		
		Gson gson = new Gson();
		FileClassify fileClassify = gson.fromJson(fileClassifyStr, FileClassify.class);
		
		//是否有父类
		Integer parentId = fileClassify.getParentId();
		if(null == parentId || parentId == -2 ){
			parentId = -1;
			fileClassify.setParentId(parentId);
		}
		
		//是否公开
		Integer pubState = fileClassify.getPubState();
		if( null == pubState){
			fileClassify.setPubState(PubStateEnum.YES.getValue());
		}
	
		//设置类型
		String type = fileClassify.getType();
		if(StringUtils.isEmpty(type)){
			fileClassify.setType(ConstantInterface.TYPE_FILE);
		}
		
		fileService.addFileClassify(fileClassify, userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 往文件夹里面添加文件
	 * @param userInfo 当前操作人员
	 * @param fileDetailStr
	 * @return
	 */
	public HttpResult<Void> addFileDetail(UserInfo userInfo, String fileDetailStr){
		HttpResult<Void> httpResult = new HttpResult<Void>();
		
		Gson gson = new Gson();
		FileDetail fileDetail = gson.fromJson(fileDetailStr, FileDetail.class);
		
		//默认公开
		Integer pubState = fileDetail.getPubState();
		if(null == pubState){
			fileDetail.setPubState(PubStateEnum.YES.getValue());
		}
		//设置文件夹
		Integer classifyId = fileDetail.getClassifyId();
		if(null == classifyId || classifyId == -2){
			classifyId = -1;
			fileDetail.setClassifyId(classifyId);
		}
		//默认文件描述
		String fileDescribe = fileDetail.getFileDescribe();
		if(StringUtils.isEmpty(fileDescribe)){
			fileDescribe = "%s于%s通过移动端上传的文件";
			String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			fileDescribe = String.format(fileDescribe, new Object[]{userInfo.getUserName(),nowDate});
			fileDetail.setFileDescribe(fileDescribe);
		}
		
		//设置类型
		String type = fileDetail.getModuleType();
		if(StringUtils.isEmpty(type)){
			fileDetail.setModuleType(ConstantInterface.TYPE_FILE);
		}
		try {
			fileService.addFile(fileDetail,userInfo);
			httpResult.ok(null);
		} catch (Exception e) {
			httpResult.error("添加文件失败");
		}
		return httpResult;
	}
	
	/**
	 * 查询更目录的数据用于展示
	 * @param userInfo
	 * @return
	 */
	public JSONArray fileTopList(UserInfo userInfo){
		// 查询结果
		JSONArray jsonTopArray = new JSONArray();
		// 附件详情
		FileDetail fileDetail = new FileDetail();
		// 设置团队号
		fileDetail.setComId(userInfo.getComId());
		// 设置当前人员
		fileDetail.setSessionUser(userInfo.getId());
		// 设置用户
		fileDetail.setUserId(userInfo.getId());
		// 设置所在文件夹
		fileDetail.setClassifyId(-1);
		
		//先进行文件夹初始化
		fileService.initFileClassify(userInfo,ConstantInterface.TYPE_FILE);
		
		Map<String,Object> fileDirmap = fileService.listPagedFolderAndFile(fileDetail, userInfo, 0);
		List<FileClassify> classifies = (List<FileClassify>) fileDirmap.get("folders");
		
		// 一次加载行数
		PaginationContext.setPageSize(4);
		// 列表数据起始索引位置
		PaginationContext.setOffset(0 * PaginationContext.getPageSize());
		PaginationContext.removeTotalCount();
		
		Gson gson = new Gson();
		
		if(null!= classifies && !classifies.isEmpty()){
			for (FileClassify fileClassify : classifies) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", fileClassify.getId());
				jsonObject.put("typeName", fileClassify.getTypeName());
				
				FileDetail subFileDetail = new FileDetail();
				// 设置团队号
				subFileDetail.setComId(userInfo.getComId());
				// 设置当前人员
				subFileDetail.setSessionUser(userInfo.getId());
				// 设置用户
				subFileDetail.setUserId(userInfo.getId());
				// 设置所在文件夹
				subFileDetail.setClassifyId(fileClassify.getId());
				
				Map<String,Object> subFileDirmap = fileService.listPagedFolderAndFile(subFileDetail, userInfo, 0);
				// 查询文件
				PageBean<FileDetail> pageBean = (PageBean<FileDetail>) subFileDirmap.get("pageBean");
				
				Integer count = pageBean.getTotalCount();
				
				// 一次加载行数
				PaginationContext.setPageSize(4);
				// 列表数据起始索引位置
				PaginationContext.setOffset(0 * PaginationContext.getPageSize());
				PaginationContext.removeTotalCount();
				
				
				List<FileClassify> subClassifies = (List<FileClassify>) subFileDirmap.get("folders");
				if(null!=subClassifies && !subClassifies.isEmpty()){
					
					Integer dirSize = subClassifies.size();
					jsonObject.put("count", pageBean.getTotalCount() + dirSize);
					
					if(dirSize > 4){
						List<FileClassify> subTopClassifies = subClassifies.subList(0, 4);
						
						jsonObject.put("dirs", gson.toJson(subTopClassifies));
						jsonObject.put("files", new ArrayList());
					}else{
						jsonObject.put("dirs", gson.toJson(subClassifies));
						
						List<FileDetail> listFiles = pageBean.getRecordList();
						if(null!=listFiles && !listFiles.isEmpty()){
							Integer fileSize = listFiles.size();
							Integer countTotal = fileSize + dirSize;
							if(countTotal>4){
								List<FileDetail> sublistFiles = listFiles.subList(0, 4-dirSize);
								jsonObject.put("files", gson.toJson(sublistFiles));
							}else{
								jsonObject.put("files", gson.toJson(listFiles));
							}
							
						}else{
							jsonObject.put("files", new ArrayList());
						}
						
					}
				}else{
					jsonObject.put("dirs", new ArrayList());
					
					jsonObject.put("count",count);
					
					List<FileDetail> listFiles = pageBean.getRecordList();	
					if (null != listFiles && !listFiles.isEmpty()) {
						jsonObject.put("files", gson.toJson(listFiles));
					} else {
						jsonObject.put("files", new ArrayList());
					}
				}
				jsonTopArray.add(jsonObject);
			}
		}
		
		// 查询文件
		PageBean<FileDetail> pageBean = (PageBean<FileDetail>) fileDirmap.get("pageBean");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", -2);
		jsonObject.put("typeName", "未分类");
		jsonObject.put("count", pageBean.getTotalCount());
		
		if(null ==classifies ||  classifies.isEmpty()){
			jsonObject.put("dirs", null);
		}
		
		List<FileDetail> listFiles = pageBean.getRecordList();	
		if (null != listFiles && !listFiles.isEmpty()) {
			jsonObject.put("files", gson.toJson(listFiles));
		} else {
			jsonObject.put("files", new ArrayList());
		}
		jsonTopArray.add(jsonObject);
		return jsonTopArray;
	}

	/**
	 * 获取文档详情
	 * 
	 * @param userInfo 操作人信息
	 * @param fileId 文档主键
	 * @param busSource  数据来源
	 * @return
	 */
	public HttpResult<Upfiles> fileDetail(UserInfo userInfo, Integer fileId, String busSource) {
		// 返回对象声明
		HttpResult<Upfiles> httpResult = new HttpResult<Upfiles>();
		if(null!= busSource && !"".equals(busSource)){
			if(busSource.equals(ConstantInterface.TYPE_FILE)){
				FileDetail fileDetail = fileService.getFileDetail(fileId,userInfo);
				todayWorksService.updateTodoWorkRead(fileId, userInfo.getComId(), userInfo.getId(), 
						ConstantInterface.TYPE_FILE,0);
				fileId = fileDetail.getFileId();
			}
		}
		Upfiles file = uploadService.getUpfileById(fileId);
		httpResult.setData(file);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
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
	public HttpResult<Object> quesAdd(UserInfo userInfo, String quesStr,
			String idType) throws Exception {
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();
		Gson gson = new Gson();
		Question ques = gson.fromJson(quesStr, Question.class);
		// 所在企业
		ques.setComId(userInfo.getComId());
		// 创建人
		ques.setUserId(userInfo.getId());
		// 删除标识(正常)
		ques.setDelState(0);
		// 采纳的答案
		ques.setCnAns(0);
		// 获取信息分享以及范围
		MsgShare msgShare = CommonUtil.getMsgShare(idType,
				ConstantInterface.TYPE_QUES, ques.getTitle(), userInfo);
		// 分享到分享信息列表
		ques.setShareMsg("yes");
		// 发布问题
		qasService.addQues(ques, userInfo, msgShare);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}

	/**
	 * 问答详情
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @param questionId
	 *            问题主键
	 * @return
	 * @throws Exception
	 */
	public HttpResult<Question> quesDetail(UserInfo userInfo, Integer questionId)
			throws Exception {
		// 返回对象声明
		HttpResult<Question> httpResult = new HttpResult<Question>();
		// 一次加载行数
		PaginationContext.setPageSize(9);

		// 所选问题
		Question ques = qasService.getQuesById(questionId, userInfo.getComId(),
				userInfo.getId());
		// 问题是否存在
		if (null == ques || ques.getDelState() == 1) {
			httpResult.setMsg("该问题已被删除！");
			httpResult.setCode(1);
			return httpResult;
		}
		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_QUES, questionId);
		// 添加查看记录
		viewRecordService.addViewRecord(userInfo, viewRecord);
		// 问题的回答
		List<Answer> list = qasService.listPagedAnswer(questionId,
				userInfo.getComId(), userInfo.getId());
		// 回答总数
		ques.setAllAnswers(PaginationContext.getTotalCount());
		ques.setListAns(list);
		// 查看问题，删除今日提醒
		todayWorksService.updateTodoWorkRead(questionId, userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_QUES, 0);
		httpResult.setData(ques);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}
	
	/**
	 * 问答回答与回复留言
	 * @param userInfo 操作人信息
	 * @param questionId 问题主键
	 * @param answerId 答案主键
	 * @param pId 回复父级主键
	 * @param content 留言内容
	 * @param filesStr
	 *            附件集合JSON对象字符串
	 * @return
	 * @throws Exception
	 */
	public HttpResult<Object> quesLeaveMsg(UserInfo userInfo, int questionId,
			int answerId, int pId, String content,String filesStr) throws Exception {
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();
		//上传文件主键数组
		Integer[] upfilesId = null;
		// 留言附件
		if(null!=filesStr && !"".equals(filesStr.trim())){
			Gson gson = new Gson();
			List<Upfiles> fileList = gson.fromJson(filesStr,
					new TypeToken<List<Upfiles>>() {
					}.getType());
			//上传文件主键数组
			upfilesId = new Integer[((null!=fileList && !fileList.isEmpty())?fileList.size():0)];
			int i =0;
			for (Upfiles file : fileList) {
				upfilesId[i] = file.getId();
				i++;
			}
		}
		if (0 == answerId) {
			// 回答
			Answer answer = new Answer();
			// 所在企业
			answer.setComId(userInfo.getComId());
			// 回答人
			answer.setUserId(userInfo.getId());
			answer.setQuesId(questionId);
			answer.setContent(content);
			// 回答问题
			qasService.addAns(answer, userInfo, upfilesId);
		} else {
			// 回复留言
			AnsTalk ansTalk = new AnsTalk();
			// 企业编号
			ansTalk.setComId(userInfo.getComId());
			// 发言人
			ansTalk.setTalker(userInfo.getId());
			ansTalk.setQuesId(questionId);
			ansTalk.setAnsId(answerId);
			ansTalk.setParentId(pId);
			if (-1 != pId) {
				AnsTalk pTalk = qasService.getAnsTalkById(pId,
						userInfo.getComId());
				ansTalk.setPtalker(pTalk.getTalker());
			}
			ansTalk.setTalkContent(content);
			// 评论回答
			qasService.addAnsTalk(ansTalk, userInfo, upfilesId);
		}

		httpResult.setCode(HttpResult.CODE_OK);
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
	public List<TodayWorks> jobList(UserInfo userInfo, String busType,
			String jobName) {
		if (null == userInfo) {
			return null;
		}
		// 模块数组化
		List<String> modList = null;
		if (null != busType && !"".equals(busType)) {
			modList = new ArrayList<String>();
			modList.add(busType);
		}
		TodayWorks job = new TodayWorks();
		if (null != jobName && !"".equals(jobName.trim())) {
			job.setContent(jobName);
		}
//		// 查询待办事项
//		List<TodayWorks> jobList = todayWorksService.listPagedMsgTodo(job,
//				userInfo.getComId(), userInfo.getId(), modList);
		//罗健临时应对
//		List<TodayWorks> jobList = mobileDao.listPagedMsgTodo(job,
//				userInfo.getComId(), userInfo.getId(), modList);
		List<TodayWorks> jobList = todayWorksService.listPagedMsgTodo(job,userInfo.getComId(), userInfo.getId(), modList);
		return jobList;
	}
	
	/**
	 * 查询人员指定模块的未读消息
	 * @param userInfo 
	 * @param busTypeListStr
	 * @return
	 */
	public PageBean<TodayWorks> countNoReadByUser(UserInfo userInfo, String busTypeListStr) {
		PageBean<TodayWorks> pageBean = new PageBean<TodayWorks>();
		if (StringUtils.isEmpty(busTypeListStr)) {
			pageBean.setRecordList(null);
			pageBean.setTotalCount(0);
			return pageBean;
		}
		//转换成json数组
		//String集合
		List<String> modList = JSONArray.parseArray(busTypeListStr, String.class);
		
		modList.remove(ConstantInterface.TYPE_OPERATION);
		
		if(null == modList || modList.isEmpty()){
			pageBean.setRecordList(null);
			pageBean.setTotalCount(0);
			return pageBean;
		}
		
		List<TodayWorks> noreadTodayWorks = mobileDao.listPagedNoReadByUser(userInfo,modList);
		pageBean.setRecordList(noreadTodayWorks);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	
	/**
	 * 查询指定模块的消息数
	 * @param userInfo 当前操作人员
	 * @param busTypeListStr 模块的类型字符串
	 * @param jobName 模糊查询的
	 * @return
	 */
	public PageBean<TodayWorks> listPagedTodayWorksForApp(UserInfo userInfo, String busTypeListStr,
			String jobName) {
		if (StringUtils.isEmpty(busTypeListStr)) {
			PageBean<TodayWorks> pageBean = new PageBean<TodayWorks>();
			pageBean.setRecordList(new ArrayList<TodayWorks>());
			pageBean.setTotalCount(0);
			return pageBean;
		}
		//String集合
		List<String> modList = JSONArray.parseArray(busTypeListStr, String.class);
		
		modList.remove(ConstantInterface.TYPE_OPERATION);
		
		if(null == modList || modList.isEmpty()){
			PageBean<TodayWorks> pageBean = new PageBean<TodayWorks>();
			pageBean.setRecordList(new ArrayList<TodayWorks>());
			pageBean.setTotalCount(0);
			return pageBean;
		}
		
		TodayWorks todayWorks = new TodayWorks();
		if (null != jobName && !"".equals(jobName.trim())) {
			todayWorks.setContent(jobName);
		}
		
		List<TodayWorks> recordList = mobileDao.listPagedTodayWorksForApp(todayWorks,
				userInfo,modList);
		
		PageBean<TodayWorks> pageBean = new PageBean<TodayWorks>();
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
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
	 * @throws ParseException
	 */
	public HttpResult<Map<String, String>> jobDetails(UserInfo userInfo,
			Integer toDoId, Integer busId, String busType, Integer isClock,
			Integer clockId) {
		TodayWorks todayWorks = new TodayWorks();
		todayWorks.setId(toDoId);
		todayWorks.setBusId(busId);
		todayWorks.setBusType(busType);
		todayWorks.setIsClock(isClock);
		todayWorks.setClockId(clockId);
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>(3);
		Gson gson = new Gson();
		// 通过待办事项的主键取得待办事项详情
		TodayWorks toDo = todayWorksService.getMsgTodoById(todayWorks.getId(),
				todayWorks.getBusType(), todayWorks.getBusId(), userInfo);
		map.put("toDo", gson.toJson(toDo));
		// 是闹铃事件
		if (null != clockId && clockId > 0) {
			Clock clock = clockService.getClockById(todayWorks.getClockId(),
					userInfo);
			if (null != clock.getClockRepType()
					&& Integer.parseInt(clock.getClockRepType()) > 0) {
				clock.setClockNextDate(clock.getClockNextDate()
						.substring(0, 10));
			} else {
				clock.setClockNextDate(null);
			}
			clock.setClockRepType(DataDicContext.getInstance()
					.getCurrentPathZvalue("clockType", clock.getClockRepType()));
			map.put("clock", gson.toJson(clock));
		}
		// 其他事件，只要点击就会消除
		todayWorks.setBusSpec(0);
		todayWorks.setReadState(1);
		todayWorksService.updateTodayObj(todayWorks);

		httpResult.setData(map);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**************************************** 以下通讯录 ****************************************************/
	
	/**
	 * 取得通讯录的所有成员
	 * @param userInfo
	 * @param pageNum 页码数
	 * @param uName 用户姓名
	 * @return
	 */
	public HttpResult<List<UserInfo>> userListAll(UserInfo sessionUser,
			Integer pageNum,String uName)  {
		HttpResult<List<UserInfo>> httpResult = new HttpResult<List<UserInfo>>();
		UserInfo userInfo = new UserInfo();
		//在职成员
		userInfo.setEnabled("1");
		userInfo.setComId(sessionUser.getComId());
		userInfo.setUserName(uName);
		
		pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
		//一次加载行数
		PaginationContext.setPageSize(12);
		//列表数据起始索引位置
		PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
		
		//查询企业用户
		List<UserInfo> listUserInfo = userInfoService.listPagedUserInfo(userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listUserInfo);
		
		return httpResult;
	}
	
	/**
	 * 取得通讯录的所有成员 不分页
	 * @param userInfo
	 * @param uName 用户姓名
	 * @return
	 */
	public HttpResult<List<UserInfo>> listUserInfos(UserInfo sessionUser,String uName)  {
		HttpResult<List<UserInfo>> httpResult = new HttpResult<List<UserInfo>>();
		UserInfo userInfo = new UserInfo();
		//在职成员
		userInfo.setEnabled("1");
		userInfo.setComId(sessionUser.getComId());
		userInfo.setUserName(uName);
		//查询企业用户
		List<UserInfo> listUserInfo = userInfoService.listUserInfo(userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listUserInfo);
		return httpResult;
	}
	
	/**
	 * 取得常用人员
	 * @param userInfo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HttpResult<List<UserInfo>> usedUserList(UserInfo userInfo) throws IllegalAccessException, InvocationTargetException {
		HttpResult<List<UserInfo>> httpResult = new HttpResult<List<UserInfo>>();
		//查询企业用户
		List<UserInfo> listUserInfo = userInfoService.listUsedUser(userInfo.getComId(), userInfo.getId(),5);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listUserInfo);
		return httpResult;
	}
	
	/**
	 * 取得常用人员前十
	 * @param userInfo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HttpResult<List<UserInfo>> usedUserList4Ten(UserInfo userInfo) throws IllegalAccessException, InvocationTargetException {
		HttpResult<List<UserInfo>> httpResult = new HttpResult<List<UserInfo>>();
		//查询企业用户
		List<UserInfo> listUserInfo = userInfoService.listUsedUser(userInfo.getComId(), userInfo.getId(),10);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listUserInfo);
		return httpResult;
	}

	/**
	 * 查询团队部门信息
	 * 
	 * @param dep
	 * @param needUser
	 *            是否需要成员
	 * @return
	 */
	public List<Department> listDepAll(Department dep, String needUser) {
		// 查询所有部门
		List<Department> listDep = depService.listTreeOrganization(dep);
		
		if (null != needUser) {
			return listDep;
		}

		// 查询结果添加部门成员
		List<Department> result = new ArrayList<Department>();

		// 有部门成员
		if (null != listDep && listDep.size() > 0) {
			
			for (Department department : listDep) {
				// 部门主键
				Integer depId = department.getId();
				// 设置查询条件
				UserInfo user = new UserInfo();
				user.setDepId(depId);
				user.setComId(dep.getComId());
				// 查询部门成员
				List<UserInfo> listDepUser = userInfoService.listUserOfDep(user);

				// 取得部门名称
				String depName = department.getDepName();
				// 设置部门成员个数
				depName = depName + "(" + listDepUser.size() + ")";
				department.setDepName(depName);

				department.setListUser(listDepUser);
				result.add(department);
			}
			
			//第三级的人员移到上一级
			if(result.size() > 0) {
				for (int i = result.size() -1; i >=0; i--) {
					if(result.get(i).getDepLevel() > 2) {
						for (int j = 0; j < result.size(); j++) {
							if(result.get(j).getId() == result.get(i).getParentId()) {
								if(result.get(i).getListUser() != null && result.get(i).getListUser().size() >0) {
									for (int m = 0; m < result.get(i).getListUser().size(); m++) {
										result.get(j).getListUser().add(result.get(i).getListUser().get(m));
									}
								}
								result.remove(i);
								break;
								
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 查询通讯录的个人分组
	 * 
	 * @param selfGroup
	 * @return
	 */
	public List<SelfGroup> listGrpAll(UserInfo sesionUser,SelfGroup selfGroup) {
		// 查询所有个人分组
		List<SelfGroup> result = selfGrpService.listUserGroup(sesionUser,selfGroup);
		return result;
	}

	/**************************************** 信息分享 ***************************************************/
	/**
	 * 获取消息未读总数
	 * @param comId
	 * @param userId
	 * @return
	 */
	/**
	 * 罗健临时应对
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Integer countNoRead(Integer comId,Integer userId){
//		Integer nums = todayWorksService.countNoRead(comId, userId);
		return 0;
	}
	/**
	 * 获取信息分享列表
	 * 
	 * @param userId
	 * @param pageSize
	 * @param minId
	 * @param msgShare
	 * @param modList
	 * @return
	 * @throws ParseException
	 */
	public List<MsgShare> msgShareList(Integer comId,Integer userId, Integer pageSize,
			Integer minId, MsgShare msgShare, List<String> modList)
			throws ParseException {

		List<MsgShare> list = msgShareService.listMsgShare(comId,userId, pageSize,
				 msgShare, modList,minId);

		return list;
	}

	/**
	 * 分享信息存储
	 * 
	 * @param userInfo
	 * @param keyAndTypeStrs
	 *            分享范围类型与主键
	 * @param content
	 *            分享描述
	 * @return
	 */
	public HttpResult<Object> msgShareAdd(UserInfo userInfo,
			String keyAndTypeStrs, String content) {
		HttpResult<Object> httpResult = new HttpResult<Object>();
		// 重新解析信息分享信息
		MsgShare msgShare = CommonUtil.getMsgShare(keyAndTypeStrs, "050",
				content, userInfo);
		// 创建人姓名
		msgShare.setCreatorName(userInfo.getUserName());
		// 工作类型为分享
		msgShare.setTraceType(0);
		// 设置公开类型 默认公开
		msgShare.setIsPub(1);
		Integer id = dailyService.addDailyByMsgShare(msgShare,userInfo);
		// 信息分享组群人
		List<UserInfo> shares = new ArrayList<UserInfo>();
		// 所有人
		if (msgShare.getScopeType() == 0) {
			shares = userInfoService.listUser(userInfo.getComId());
		}
		// 自定义
		else if (msgShare.getScopeType() == 1) {
			// 配置信息分享范围
			List<ShareGroup> listShareGroup = msgShare.getShareGroup();
			String grpIds = "";
			if (null != listShareGroup && listShareGroup.size() > 0) {
				for (ShareGroup shareGroup : listShareGroup) {
					grpIds += shareGroup.getGrpId() + ",";
				}
			}
			grpIds += "-1";
			// 信息分享组群人
			shares = msgShareService.listScopeUser(grpIds, userInfo.getComId());
		}
		// 添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo, null, id, "分享消息", "050",
				shares, null);

		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(new Object());
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
	public HttpResult<MsgShare> msgShareDetail(UserInfo userInfo, Integer msgId) {
		HttpResult<MsgShare> httpResult = new HttpResult<MsgShare>();
		// 一次加载行数
		PaginationContext.setPageSize(9);
		MsgShare msgShare = msgShareService.viewMsgShareById(userInfo.getId(),
				userInfo.getComId(), msgId, ConstantInterface.TYPE_DAILY);
		
		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_DAILY, msgShare.getModId());
		
		// 添加查看记录
		viewRecordService.addViewRecord(userInfo, viewRecord);
		
		
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_DAILY);
		try {
			Daily daily = dailyService.getDailyForView(msgShare.getModId(), userInfo, new DailyPojo(), isForceIn);
			msgShare.setDaily(daily);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 信息讨论
		List<MsgShareTalk> msgShareTalks = msgShareService
				.listPagedMsgShareTalk(msgId, userInfo.getComId(),"app");
		msgShare.setListMsgShareTalk(msgShareTalks);
		msgShare.setTalkSum(PaginationContext.getTotalCount());
		// 查看分享信息，删除提醒
		todayWorksService.updateTodoWorkRead(msgShare.getModId(), userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_DAILY, 0);

		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(msgShare);
		return httpResult;
	}

	/**
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
	public HttpResult<List<MsgShareTalk>> msgShareTalkList(UserInfo userInfo,
			Integer msgId) {
		HttpResult<List<MsgShareTalk>> httpResult = new HttpResult<List<MsgShareTalk>>();
		// 信息讨论
		List<MsgShareTalk> msgShareTalks = msgShareService
				.listPagedMsgShareTalk(msgId, userInfo.getComId(),"app");
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(msgShareTalks);
		return httpResult;
	}
	/**
	 * 日报留言
	 * @param userInfo
	 * @param msgId
	 * @return
	 */
	public HttpResult<List<DailyTalk>> dailyTalkList(UserInfo userInfo,
			Integer msgId) {
		HttpResult<List<DailyTalk>> httpResult = new HttpResult<List<DailyTalk>>();
		// 信息讨论
		List<DailyTalk> msgShareTalks = dailyService.listPagedDailyTalk 
				(msgId, userInfo.getComId(),"app");
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(msgShareTalks);
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
	 *            留言内容
	 * @param tagKey
	 *            标签主键
	 * @param filesStr
	 *            附件集合JSON对象字符串
	 * @return
	 * @throws Exception
	 */
	public HttpResult<Object> leaveMsg(UserInfo userInfo, String busType,
			Integer busId, Integer pId, String content, Integer tagKey,
			String filesStr) throws Exception {
		if (null == busType || "".equals(busType.trim())) {
			return null;
		}
		// 返回对象声明
		HttpResult<Object> httpResult = new HttpResult<Object>();
		//上传文件主键数组
		Integer[] upfilesId = null;
		// 留言附件
		if(null!=filesStr && !"".equals(filesStr.trim())){
			Gson gson = new Gson();
			List<Upfiles> fileList = gson.fromJson(filesStr,
					new TypeToken<List<Upfiles>>() {
					}.getType());
			//上传文件主键数组
			upfilesId = new Integer[((null!=fileList && !fileList.isEmpty())?fileList.size():0)];
			int i =0;
			for (Upfiles file : fileList) {
				upfilesId[i] = file.getId();
				i++;
			}
		}
		if (ConstantInterface.TYPE_TASK.equals(busType)) {
			// 任务留言
			TaskTalk taskTalk = new TaskTalk();
			taskTalk.setComId(userInfo.getComId());
			taskTalk.setSpeaker(userInfo.getId());
			taskTalk.setContent(content);
			taskTalk.setTaskId(busId);
			taskTalk.setParentId(pId);
			// 留言附件
			taskTalk.setUpfilesId(upfilesId);
			taskService.addTaskTalk(taskTalk, userInfo);
			// 模块日志添加
			taskService.addTaskLog(userInfo.getComId(), taskTalk.getTaskId(),
					userInfo.getId(), userInfo.getUserName(), "添加留言");
		} else if (ConstantInterface.TYPE_WEEK.equals(busType)) {
			// 周报留言
			WeekRepTalk weekRepTalk = new WeekRepTalk();
			weekRepTalk.setComId(userInfo.getComId());
			weekRepTalk.setTalker(userInfo.getId());
			weekRepTalk.setWeekReportId(busId);
			weekRepTalk.setContent(content);
			weekRepTalk.setParentId(pId);
			if (-1 != pId) {
				WeekRepTalk pTalk = weekReportService.getWeekRepTalk(pId,
						userInfo.getComId());
				weekRepTalk.setPtalker(pTalk.getTalker());
			}
			// 留言附件
			weekRepTalk.setUpfilesId(upfilesId);
			// 反馈留言回复
			weekReportService.addWeekRepTalk(weekRepTalk, userInfo);
			// 模块日志添加
			if (-1 == weekRepTalk.getParentId()) {
				weekReportService.addWeekRepLog(userInfo.getComId(),
						weekRepTalk.getWeekReportId(), userInfo.getId(),
						userInfo.getUserName(), "参与反馈留言");
			} else {
				weekReportService.addWeekRepLog(userInfo.getComId(),
						weekRepTalk.getWeekReportId(), userInfo.getId(),
						userInfo.getUserName(), "回复反馈留言");
			}
		} else if (ConstantInterface.TYPE_DAILY.equals(busType)) {
			// 分享信息留言
			DailyTalk msgTalk = new DailyTalk();
			msgTalk.setComId(userInfo.getComId());
			msgTalk.setTalker(userInfo.getId());
			msgTalk.setDailyId(busId);
			msgTalk.setParentId(pId);
			msgTalk.setContent(content);
			if (-1 != pId) {
				DailyTalk magShareTalk = dailyService.getDailyTalk(
						pId, userInfo.getComId());
				msgTalk.setPtalker(magShareTalk.getTalker());
			}
			// 留言附件
			msgTalk.setUpfilesId(upfilesId);
			// 信息讨论回复
			dailyService.addDailyTalk(msgTalk, userInfo);
			// 模块日志添加
			if (-1 == pId) {
				dailyService.addDailyLog(userInfo.getComId(),
						busId, userInfo.getId(),
						userInfo.getUserName(), "参与信息讨论");
			} else {
				dailyService.addDailyLog(userInfo.getComId(),
						busId, userInfo.getId(),
						userInfo.getUserName(), "回复信息讨论");
			}
		} else if (ConstantInterface.TYPE_CRM.equals(busType)) {
			// 客户留言
			FeedBackInfo feedBackInfo = new FeedBackInfo();
			feedBackInfo.setComId(userInfo.getComId());
			feedBackInfo.setCustomerId(busId);
			feedBackInfo.setFeedBackTypeId(tagKey);
			feedBackInfo.setParentId(pId);
			feedBackInfo.setContent(content);
			feedBackInfo.setUserId(userInfo.getId());
			// 留言附件
			feedBackInfo.setUpfilesId(upfilesId);
			// 客户留言持久化
			crmService.addFeedBackInfo(feedBackInfo, null, userInfo);
		} else if (ConstantInterface.TYPE_ITEM.equals(busType)) {
			ItemTalk itemTalk = new ItemTalk();
			// 项目留言
			itemTalk.setComId(userInfo.getComId());
			itemTalk.setUserId(userInfo.getId());
			itemTalk.setItemId(busId);
			itemTalk.setContent(content);
			itemTalk.setParentId(pId);
			// 留言附件
			itemTalk.setUpfilesId(upfilesId);
			itemService.addItemTalk(itemTalk, userInfo);
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), itemTalk.getItemId(),
					userInfo.getId(), userInfo.getUserName(), "添加新评论");
		} else if (ConstantInterface.TYPE_PRODUCT.equals(busType)) {
			BaseTalk proTalk = new BaseTalk();
			// 项目留言
			proTalk.setComId(userInfo.getComId());
			proTalk.setSpeaker(userInfo.getId());
			proTalk.setBusId(busId);
			proTalk.setContent(content);
			proTalk.setParentId(pId);
			
			// 留言附件
			List<Upfiles> fileList = com.alibaba.fastjson.JSONArray.parseArray(filesStr, Upfiles.class);
			List<BaseUpfile> listTalkFile = new ArrayList<>();
			if(null != fileList && !fileList.isEmpty()){
				for (Upfiles upfiles : fileList) {
					BaseUpfile baseUpfile = new BaseUpfile();
					baseUpfile.setUpfileId(upfiles.getId());
					baseUpfile.setFileName(upfiles.getFilename());
					listTalkFile.add(baseUpfile);
				}
			}
			proTalk.setListTalkFile(listTalkFile);
			
			productTalkService.addTalk(userInfo, proTalk);
		}

		httpResult.setData(new Object());
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;

	}

	/**
	 * 业务附件数据集
	 * 
	 * @param userInfo
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            业务主键
	 * @return
	 * @throws ParseException
	 */
	public HttpResult<List<Upfiles>> fileList(UserInfo userInfo,
			String busType, Integer busId) {
		// 返回对象声明
		HttpResult<List<Upfiles>> httpResult = new HttpResult<List<Upfiles>>();
		if (ConstantInterface.TYPE_WEEK.equals(busType)) {
			// 查看周报附件，删除消息提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_WEEK, 0);
			// 周报附件
			List<WeekRepUpfiles> weekRepFiles = weekReportService
					.listPagedWeekRepFiles(userInfo.getComId(), busId);
			List<Upfiles> files = new ArrayList<Upfiles>();
			if (null != weekRepFiles && !weekRepFiles.isEmpty()) {
				Upfiles file = null;
				for (WeekRepUpfiles vo : weekRepFiles) {
					file = new Upfiles();
					file.setFilename(vo.getFileName());
					file.setFileExt(vo.getFileExt());
					file.setUuid(vo.getFileUuid());
					file.setOwnerName(vo.getUsername());
					file.setRecordCreateTime(vo.getUpTime());
					files.add(file);
				}
			}
			httpResult.setData(files);
		} else if (ConstantInterface.TYPE_DAILY.equals(busType)) {
			// 查看信息附件，删除提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_DAILY, 0);
			// 信息附件
			List<DailyUpfiles> listDailyUpfile = dailyService
					.listPagedDailyFiles(userInfo.getComId(), busId);
			List<Upfiles> files = new ArrayList<Upfiles>();
			if (null != listDailyUpfile
					&& !listDailyUpfile.isEmpty()) {
				Upfiles file = null;
				for (DailyUpfiles vo : listDailyUpfile) {
					file = new Upfiles();
					file.setFilename(vo.getFileName());
					file.setFileExt(vo.getFileExt());
					file.setUuid(vo.getUuid());
					file.setOwnerName(vo.getUsername());
					file.setRecordCreateTime(vo.getRecordCreateTime());
					files.add(file);
				}
			}
			httpResult.setData(files);
		} else if (ConstantInterface.TYPE_QUES.equals(busType)) {
			// 查看问答附件，删除今日提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_QUES, 0);
			// 问答附件
			List<QuesFile> quesFiles = qasService.listPagedQuesFile(userInfo,
					busId);
			List<Upfiles> files = new ArrayList<Upfiles>();
			if (null != quesFiles && !quesFiles.isEmpty()) {
				Upfiles file = null;
				for (QuesFile vo : quesFiles) {
					file = new Upfiles();
					file.setFilename(vo.getOrgFileName());
					file.setFileExt(vo.getFileExt());
					file.setUuid(vo.getOrgFileUuid());
					file.setOwnerName(vo.getUsername());
					file.setRecordCreateTime(vo.getUpTime());
					files.add(file);
				}
			}
			httpResult.setData(files);
		} else if (ConstantInterface.TYPE_TASK.equals(busType)) {
			// 查看问答附件，删除今日提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_TASK, 0);
			// 任务附件
			TaskUpfile taskUpfile = new TaskUpfile();
			taskUpfile.setTaskId(busId);
			List<TaskUpfile> taskFiles = taskService.listPagedTaskUpfile(taskUpfile,
					userInfo.getComId());
			List<Upfiles> files = new ArrayList<Upfiles>();
			if (null != taskFiles && !taskFiles.isEmpty()) {
				Upfiles file = null;
				for (TaskUpfile vo : taskFiles) {
					file = new Upfiles();
					file.setFilename(vo.getFilename());
					file.setFileExt(vo.getFileExt());
					file.setUuid(vo.getUuid());
					file.setOwnerName(vo.getCreatorName());
					file.setRecordCreateTime(vo.getRecordCreateTime());
					files.add(file);
				}
			}
			httpResult.setData(files);
		} else if (ConstantInterface.TYPE_CRM.equals(busType)) {
			// 查看问答附件，删除今日提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_CRM, 0);
			// 任务附件
			CustomerUpfile customerUpfile = new CustomerUpfile();
			customerUpfile.setCustomerId(busId);
			List<CustomerUpfile> crmFiles = crmService.listPagedCustomerUpfile(
					userInfo.getComId(), customerUpfile);
			List<Upfiles> files = new ArrayList<Upfiles>();
			if (null != crmFiles && !crmFiles.isEmpty()) {
				Upfiles file = null;
				for (CustomerUpfile vo : crmFiles) {
					file = new Upfiles();
					file.setFilename(vo.getFilename());
					file.setFileExt(vo.getFileExt());
					file.setUuid(vo.getUuid());
					file.setOwnerName(vo.getCreatorName());
					file.setRecordCreateTime(vo.getRecordCreateTime());
					files.add(file);
				}
			}
			httpResult.setData(files);
		} else if (ConstantInterface.TYPE_ITEM.equals(busType)) {
			// 查看问答附件，删除今日提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_ITEM, 0);
			// 任务附件
			ItemUpfile itemUpfile = new ItemUpfile();
			itemUpfile.setItemId(busId);
			List<ItemUpfile> itemFiles = itemService.listPagedItemUpfile(itemUpfile,
					userInfo.getComId(),null);
			List<Upfiles> files = new ArrayList<Upfiles>();
			if (null != itemFiles && !itemFiles.isEmpty()) {
				Upfiles file = null;
				for (ItemUpfile vo : itemFiles) {
					file = new Upfiles();
					file.setFilename(vo.getFilename());
					file.setFileExt(vo.getFileExt());
					file.setUuid(vo.getUuid());
					file.setOwnerName(vo.getCreatorName());
					file.setRecordCreateTime(vo.getRecordCreateTime());
					files.add(file);
				}
			}
			httpResult.setData(files);
		} else if (ConstantInterface.TYPE_PRODUCT.equals(busType)) {
			// 查看产品附件，删除今日提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_PRODUCT, 0);
			// 任务附件
			ProUpFiles proUpFiles = new ProUpFiles();
			proUpFiles.setProId(busId);
			List<ProUpFiles> proUpfiles = productService.listPagedProUpfile(proUpFiles,
					userInfo.getComId());
			List<Upfiles> files = new ArrayList<Upfiles>();
			if (null != proUpfiles && !proUpfiles.isEmpty()) {
				Upfiles file = null;
				for (ProUpFiles voT : proUpfiles) {
					Upfiles vo = (Upfiles) mobileDao.objectQuery(Upfiles.class,voT.getUpFileId());
					file = new Upfiles();
					file.setFilename(vo.getFilename());
					file.setFileExt(vo.getFileExt());
					file.setUuid(vo.getUuid());
					file.setOwnerName(vo.getOwnerName());
					file.setRecordCreateTime(vo.getRecordCreateTime());
					files.add(file);
				}
			}
			httpResult.setData(files);
		}else if(ConstantInterface.TYPE_FLOW_SP.equals(busType)){
			// 查看审批附件，删除今日提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_FLOW_SP, 0);
			// 任务附件
			List<SpFlowUpfile> spFlowFiles = workFlowService.listSpFiles(busId, userInfo);
			List<Upfiles> files = new ArrayList<Upfiles>();
			if (null != spFlowFiles && !spFlowFiles.isEmpty()) {
				Upfiles file = null;
				for (SpFlowUpfile vo : spFlowFiles) {
					file = new Upfiles();
					file.setFilename(vo.getFilename());
					file.setFileExt(vo.getFileExt());
					file.setUuid(vo.getUuid());
					file.setOwnerName(vo.getCreatorName());
					file.setRecordCreateTime(vo.getRecordCreateTime());
					files.add(file);
				}
			}
			httpResult.setData(files);
		}
		httpResult.setCode(HttpResult.CODE_OK);
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
	public HttpResult<List<UserInfo>> logList(UserInfo userInfo,
			String busType, Integer busId) throws ParseException {
		// 返回对象声明
		HttpResult<List<UserInfo>> httpResult = new HttpResult<List<UserInfo>>();
		if (ConstantInterface.TYPE_WEEK.equals(busType)) {
			// 查看周报日志，删除消息提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_WEEK, 0);
			// 周报日志
			List<WeekRepLog> weekRepLogs = weekReportService
					.listPagedWeekRepVoteLog(userInfo.getComId(), busId, "sid");
			List<UserInfo> users = new ArrayList<UserInfo>();
			if (null != weekRepLogs && !weekRepLogs.isEmpty()) {
				UserInfo user = null;
				for (WeekRepLog vo : weekRepLogs) {
					user = new UserInfo();
					user.setSmImgUuid(vo.getUuid());
					user.setSmImgName(vo.getFilename());
					user.setGender(vo.getGender());
					user.setUserName(vo.getUserName());
					user.setRecordCreateTime(vo.getRecordCreateTime());
					user.setRemark(vo.getContent());
					users.add(user);
				}
			}
			httpResult.setData(users);
		} else if (ConstantInterface.TYPE_DAILY.equals(busType)) {
			// 查看信息日志，删除提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_DAILY, 0);
			// 信息讨论日志
			List<DailyLog> dailyLogs = dailyService.listPagedDailyVoteLog(userInfo.getComId(), busId, null);
			List<UserInfo> users = new ArrayList<UserInfo>();
			if (null != dailyLogs && !dailyLogs.isEmpty()) {
				UserInfo user = null;
				for (DailyLog vo : dailyLogs) {
					user = new UserInfo();
					user.setId(vo.getUserId());
					user.setComId(userInfo.getComId());
					user.setUserName(vo.getUserName());
					user.setRecordCreateTime(vo.getRecordCreateTime());
					user.setRemark(vo.getContent());
					users.add(user);
				}
			}
			httpResult.setData(users);
		} else if (ConstantInterface.TYPE_QUES.equals(busType)) {
			// 查看问答日志，删除今日提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_QUES, 0);
			// 问答日志
			List<QuesLog> quesLogs = qasService.listPagedQuesLog(
					userInfo.getComId(), busId);
			List<UserInfo> users = new ArrayList<UserInfo>();
			if (null != quesLogs && !quesLogs.isEmpty()) {
				UserInfo user = null;
				for (QuesLog vo : quesLogs) {
					user = new UserInfo();
					user.setSmImgUuid(vo.getUuid());
					user.setSmImgName(vo.getFilename());
					user.setGender(vo.getGender());
					user.setUserName(vo.getSpeakerName());
					user.setRecordCreateTime(vo.getRecordCreateTime());
					user.setRemark(vo.getContent());
					users.add(user);
				}
			}
			httpResult.setData(users);
		} else if (ConstantInterface.TYPE_TASK.equals(busType)) {
			// 查看任务日志，删除消息提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_TASK, 0);

			List<TaskLog> listTaskLog = taskService.listTaskLog(busId,
					userInfo.getComId());
			List<UserInfo> users = new ArrayList<UserInfo>();
			if (null != listTaskLog && !listTaskLog.isEmpty()) {
				UserInfo user = null;
				for (TaskLog vo : listTaskLog) {
					user = new UserInfo();
					user.setSmImgUuid(vo.getUuid());
					user.setSmImgName(vo.getFilename());
					user.setGender(vo.getGender());
					user.setUserName(vo.getSpeakerName());
					user.setRecordCreateTime(vo.getRecordCreateTime());
					user.setRemark(vo.getContent());
					users.add(user);
				}
			}
			httpResult.setData(users);
		} else if (ConstantInterface.TYPE_CRM.equals(busType)) {
			// 查看客户日志，删除消息提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_CRM, 0);
			List<CustomerLog> listCrmLog = crmService.listCustomerLog(
					userInfo.getComId(), busId);

			List<UserInfo> users = new ArrayList<UserInfo>();
			if (null != listCrmLog && !listCrmLog.isEmpty()) {
				UserInfo user = null;
				for (CustomerLog vo : listCrmLog) {
					user = new UserInfo();
					user.setSmImgUuid(vo.getUuid());
					user.setSmImgName(vo.getFileName());
					user.setGender(vo.getGender());
					user.setUserName(vo.getUserName());
					user.setRecordCreateTime(vo.getRecordCreateTime());
					user.setRemark(vo.getContent());
					users.add(user);
				}
			}
			httpResult.setData(users);
		} else if (ConstantInterface.TYPE_ITEM.equals(busType)) {
			// 查看项目日志，删除消息提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_ITEM, 0);
			List<ItemLog> listItemLog = itemService.listItemLog(busId,
					userInfo.getComId());

			List<UserInfo> users = new ArrayList<UserInfo>();
			if (null != listItemLog && !listItemLog.isEmpty()) {
				UserInfo user = null;
				for (ItemLog vo : listItemLog) {
					user = new UserInfo();
					user.setSmImgUuid(vo.getUuid());
					user.setSmImgName(vo.getFilename());
					user.setGender(vo.getGender());
					user.setUserName(vo.getUserName());
					user.setRecordCreateTime(vo.getRecordCreateTime());
					user.setRemark(vo.getContent());
					users.add(user);
				}
			}
			httpResult.setData(users);
		} else if (ConstantInterface.TYPE_PRODUCT.equals(busType)) {
			// 查看项目日志，删除消息提醒
			todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(),
					userInfo.getId(), ConstantInterface.TYPE_PRODUCT, 0);
			List<ProLog> listProLog = productService.listProLog(busId,
					userInfo.getComId());

			List<UserInfo> users = new ArrayList<UserInfo>();
			if (null != listProLog && !listProLog.isEmpty()) {
				UserInfo user = null;
				for (ProLog vo : listProLog) {
					UserInfo userInfoT = (UserInfo) mobileDao.objectQuery(UserInfo.class,vo.getOperator());
					user = new UserInfo();
					user.setSmImgUuid(vo.getUuid());
					user.setSmImgName(vo.getFilename());
					user.setGender(userInfoT.getGender());
					user.setUserName(userInfoT.getUserName());
					user.setRecordCreateTime(vo.getRecordCreateTime());
					user.setRemark(vo.getContent());
					users.add(user);
				}
			}
			httpResult.setData(users);
		}
		httpResult.setCode(HttpResult.CODE_OK);
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
	public HttpResult<List<ViewRecord>> recordList(UserInfo userInfo,
			String busType, Integer busId) throws ParseException {
		// 返回对象声明
		HttpResult<List<ViewRecord>> httpResult = new HttpResult<List<ViewRecord>>();
//		if(ConstantInterface.TYPE_DAILY.equals(busType)) {
//			MsgShare msgShare = msgShareService.getMsgShareById(userInfo.getComId(), busId);
//			busId = msgShare.getModId();
//		}
		// 浏览的人员
		List<ViewRecord> listViewRecord = viewRecordService.listViewRecord(
				userInfo, busType, busId);
		httpResult.setData(listViewRecord);
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
	 */
	public HttpResult<Boolean> authorCheck(UserInfo userInfo, String busType,
			Integer busId) {
		// 返回对象声明
		HttpResult<Boolean> httpResult = new HttpResult<Boolean>();
		// 默认无权限
		httpResult.setData(false);
		if (ConstantInterface.TYPE_DAILY.equals(busType)) {
			if (msgShareService.authorCheck(userInfo.getComId(), busId,
					userInfo.getId())) {
				httpResult.setData(true);
			}
		} else if (ConstantInterface.TYPE_TASK.equals(busType)) {
			if (taskService.authorCheck(userInfo, busId,0)) {
				httpResult.setData(true);
			}
		} else if (ConstantInterface.TYPE_CRM.equals(busType)) {
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			//是督查人员
			if(isForceIn){
				httpResult.setData(true);
			} else if (crmService.authorCheck(userInfo.getComId(), busId,
					userInfo.getId())) {
				httpResult.setData(true);
			}
		} else if (ConstantInterface.TYPE_ITEM.equals(busType)) {
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			//是督查人员
			if(isForceIn){
				httpResult.setData(true);
			} else if (itemService.authorCheck(userInfo.getComId(), busId,
					userInfo.getId())) {
				httpResult.setData(true);
			}
		} else if (ConstantInterface.TYPE_PRODUCT.equals(busType)) {
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			//是督查人员
			if(isForceIn){
				httpResult.setData(true);
			} else if (productService.authorCheck(userInfo.getComId(), busId,
					userInfo.getId())) {
				httpResult.setData(true);
			}
		} else if (ConstantInterface.TYPE_WEEK.equals(busType)) {
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			//是督查人员
			if(isForceIn){
				httpResult.setData(true);
			} else if (weekReportService.authorCheck(userInfo.getComId(),
					busId, userInfo.getId(),busId)) {
				httpResult.setData(true);
			}
		} else if (ConstantInterface.TYPE_VOTE.equals(busType)) {
			if (voteService.authorCheck(userInfo.getComId(), busId,
					userInfo.getId())) {
				httpResult.setData(true);
			}
		} else if (ConstantInterface.TYPE_MEETING.equals(busType)) {
			if (meetingService.authorCheck(userInfo.getComId(), busId,
					userInfo.getId())) {
				httpResult.setData(true);
			}
		} else if (ConstantInterface.TYPE_FILE.equals(busType)) {
			if (fileService.authorCheck(userInfo, busId, busType)) {
				httpResult.setData(true);
			}
		} else if (ConstantInterface.TYPE_FLOW_SP.equals(busType)) {
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			//是督查人员
			if(isForceIn){
				httpResult.setData(true);
			} else if (workFlowService.authorCheck(userInfo.getComId(), busId,
					userInfo.getId())) {
				httpResult.setData(true);
			}
		}
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 获取分享范围数据集
	 * 
	 * @param userInfo
	 *            操作人信息
	 * @return
	 */
	public HttpResult<List<SelfGroup>> scopeGroups(UserInfo userInfo) {
		// 返回对象声明
		HttpResult<List<SelfGroup>> httpResult = new HttpResult<List<SelfGroup>>();
		// 返回集合申明
		List<SelfGroup> groups = new ArrayList<SelfGroup>();
		SelfGroup group = null;
		// 个人组群集合
		groups = userInfoService.listSelfGroup(userInfo.getComId(),
				userInfo.getId());
		if (null == groups) {
			groups = new ArrayList<SelfGroup>();
		}
		for (SelfGroup grp : groups) {
			grp.setGroupType("1");
		}
		// 集合头部放入
		group = new SelfGroup();
		group.setGrpName("所有同事");
		group.setId(-1);
		group.setGroupType("0");
		groups.add(0, group);
		// 集合尾部加入
		group = new SelfGroup();
		group.setGrpName("我自己");
		group.setId(2);
		group.setGroupType("2");
		groups.add(groups.size(), group);

		httpResult.setData(groups);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 上次使用过的分组
	 * 
	 * @param userInfo
	 * @return
	 */
	public HttpResult<List<SelfGroup>> usedGroups(UserInfo userInfo) {
		// 返回对象声明
		HttpResult<List<SelfGroup>> httpResult = new HttpResult<List<SelfGroup>>();
		// 返回集合申明
		List<SelfGroup> groups = new ArrayList<SelfGroup>();
		SelfGroup group = null;
		// 个人组群集合
		List<SelfGroup> listSelfGroup = userInfoService.listSelfGroup(
				userInfo.getComId(), userInfo.getId());
		// 上次使用的分组
		List<UsedGroup> usedGroups = userInfoService.listUsedGroup(
				userInfo.getComId(), userInfo.getId());
		// 最近一次使用的分组的类型，分组名称以及自定义所有的分组
		// 上次使用过分组，则进行解析
		if (null != usedGroups && usedGroups.size() > 0) {
			for (UsedGroup usedGroup : usedGroups) {
				if ("0".equals(usedGroup.getGroupType())) {
					group = new SelfGroup();
					group.setId(-1);
					group.setGrpName("所有同事");
					group.setGroupType("0");
					groups.add(group);
				} else if ("2".equals(usedGroup.getGroupType())) {
					group = new SelfGroup();
					group.setId(2);
					group.setGrpName("我自己");
					group.setGroupType("2");
					groups.add(group);
				} else {
					for (SelfGroup grp : listSelfGroup) {
						if (usedGroup.getGrpId() == grp.getId()) {
							group = new SelfGroup();
							group.setId(grp.getId());
							group.setGrpName(grp.getGrpName());
							group.setGroupType("1");
							groups.add(group);
						}
					}
				}
			}
		} else {// 默认有一个
			group = new SelfGroup();
			group.setId(-1);
			group.setGrpName("所有同事");
			group.setGroupType("0");
			groups.add(group);
		}

		httpResult.setData(groups);
		httpResult.setCode(HttpResult.CODE_OK);
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
	public HttpResult<Map<String, String>> delBusData(UserInfo userInfo,
			Integer busId, String busType) {
		// 返回对象声明
		HttpResult<Map<String, String>> httpResult = new HttpResult<Map<String, String>>();
		Integer[] ids = new Integer[] { busId };
		if (ConstantInterface.TYPE_TASK.equals(busType)) {
			try {
				taskService.delPreTask(ids, userInfo);
				httpResult.setCode(HttpResult.CODE_OK);
			} catch (Exception e) {
				httpResult.setCode(-1);
				httpResult.setMsg("任务删除失败");
			}

		} else if (ConstantInterface.TYPE_CRM.equals(busType)) {
			try {
				crmService.delPreCustomer(ids, userInfo);
				httpResult.setCode(HttpResult.CODE_OK);
			} catch (Exception e) {
				httpResult.setCode(-1);
				httpResult.setMsg("客户删除失败");
			}
		} else if (ConstantInterface.TYPE_ITEM.equals(busType)) {
			try {
				itemService.delPreItem(ids, userInfo);
				httpResult.setCode(HttpResult.CODE_OK);
			} catch (Exception e) {
				httpResult.setCode(-1);
				httpResult.setMsg("项目删除失败");
			}
		} else if (ConstantInterface.TYPE_QUES.equals(busType)) {
			Boolean flag = qasService.delPreQues(ids, userInfo);
			if (flag) {
				httpResult.setCode(HttpResult.CODE_OK);
			} else {
				httpResult.setCode(-1);
				httpResult.setMsg("问题删除失败");
			}
		}
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
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws org.apache.lucene.queryparser.classic.ParseException
	 */
	public HttpResult<List<IndexView>> searchIndexInCom(UserInfo userInfo,
			String busType, String searchStr) throws ParseException,
			CorruptIndexException, IOException,
			org.apache.lucene.queryparser.classic.ParseException {
		// 返回对象声明
		HttpResult<List<IndexView>> httpResult = new HttpResult<List<IndexView>>();
		// 查询
		if (null != busType && !"".equals(busType.trim())) {
			List<IndexView> listIndexVo = indexService.searchIndexInModule(
					userInfo, busType, searchStr);
			httpResult.setData(listIndexVo);
		} else {
			List<IndexView> listIndexVo = indexService.searchIndexInComV2(
					userInfo, searchStr);
			httpResult.setData(listIndexVo);
		}
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**************************************** 以下文件上传 ***************************************************/
	/**
	 * 文件上传
	 * 
	 * @param userInfo
	 *            当前操作人信息
	 * @param request
	 *            数据封装头部
	 * @return
	 */
	public Upfiles addFile(UserInfo userInfo, HttpServletRequest request) {
		// 返回对象声明
		Upfiles httpResult = new Upfiles();
		// 默认为失败状态
		httpResult.setSucc("no");
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file1");
			// 文件名
			String fileName = file.getOriginalFilename();
			fileName = URLDecoder.decode(fileName,"UTF-8");
			// 文件大小
			Long fileSize = file.getSize();
			// 后缀
			String fileExt = FileUtil.getExtend(fileName);
			/* 如果不是允许的附件类型，直接返回 */
			if (!FileUtil.getAllowFileTypes().contains(fileExt)) {
				httpResult.setMsg("此附件类型不支持上传！");
				return httpResult;
			}
			String basepath = FileUtil.getUploadBasePath();
			/* 所有附件都保存到uploads 不存在则新增文件夹 */
			File f = new File(basepath);
			if (!f.exists()) {
				f.mkdir();
			}
			// 存放的文件夹
			String path = FileUtil.getUploadPath(userInfo.getComId());
			path = path + "/" + UUIDGenerator.getUUID() + "."
					+ fileExt.toLowerCase();
			// 存放文件的绝对路径
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					basepath + path));
			// 附件输入流
			InputStream is = null;
			is = file.getInputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			is.close();
			out.close();

			// 附件信息存库
			Upfiles upfiles = new Upfiles();
			upfiles.setComId(userInfo.getComId());
			upfiles.setUuid(UUIDGenerator.getUUID());
			upfiles.setFilename(fileName);
			upfiles.setFilepath(path);
			upfiles.setFileExt(fileExt.toLowerCase());
			upfiles.setSizeb(fileSize.intValue());
			String sizeM = MathExtend
					.divide(String.valueOf(upfiles.getSizeb()),
							String.valueOf(1024), 2);
			String dw = "K";
			if (Float.parseFloat(sizeM) > 1024) {
				sizeM = MathExtend.divide(sizeM, String.valueOf(1024), 2);
				dw = "M";
				if (Float.parseFloat(sizeM) > 1024) {
					sizeM = MathExtend.divide(sizeM, String.valueOf(1024), 2);
					dw = "G";
				}
			}
			upfiles.setSizem(sizeM + dw);
			Integer id = uploadService.addFile(upfiles);
			upfiles.setId(id);
			upfiles.setRecordCreateTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			BeanUtils.copyProperties(upfiles,httpResult);
			httpResult.setSucc("yes");

		} catch (Exception e) {
			if (!e.getClass().getSimpleName().equals("ClassCastException")) {
				e.printStackTrace();
			}
		}
		return httpResult;
	}
	/**
	 * 判断账号是否存在
	 * @param account 账号
	 * @return
	 */
	public UserInfo registCheckAccount(String account) {
		UserInfo user = userInfoService.checkInputAccount(account);
		return user;
	}
	/**
	 * 检测账号的密码
	 * @param account
	 * @return
	 */
	public String registCheckPasswd(String account,String passWord) {
		UserInfo user =  userInfoService.checkInputAccount(account.toLowerCase());
		JSONObject resultData = new JSONObject();
		//用户不存在
		resultData.put("code", "f1");
		if(null!=user){
			String passwordMD5 = Encodes.encodeMd5(passWord);
			//两次密码相同，输入正确
			if(passwordMD5.equals(user.getPassword())){
				resultData.put("code", "y");
				resultData.put("passwordMD5", passwordMD5);
			}else{
				//密码错误
				resultData.put("code", "f2");
			}
		}
		return resultData.toString();
	}
	/**
	 * 发送验证码
	 * @param account
	 * @return
	 */
	public void registSendYzm(String account) {
		UserInfo curUser = new UserInfo();
		curUser.setMovePhone(account);
		//默认表示为系统短信
		curUser.setComId(ConstantInterface.SYS_COMID);
		userInfoService.doSendPassYzm(curUser,ConstantInterface.GET_BY_PHONE);
	}

	/**
	 * 检测验证输入是否正确
	 * @param account
	 * @param passYzm
	 * @param funType
	 * @return
	 */
	public String registCheckYzm(String account, String passYzm, Integer funType) {
		String status = "y";
		//找回密码
		if(funType==1){
			UserInfo user =  userInfoService.getUserInfoByAccount(account.toLowerCase());
			if(null==user){
				//用户不存在
				status = "f1";
			}
			
		}
		//链接的有效性判断
		PassYzm passYzmObj = registService.getPassYzm(account);
		//验证码不存在
		if(null == passYzmObj){
			status = "f2";
		}else if( passYzmObj.getEnabled()==0){
			//验证码过期
			status = "f2";
		}else{
			if(passYzmObj.getPassYzm().equals(passYzm)){
				//删除验证码
				userInfoService.delPassYzmById(passYzmObj.getId());
				status = "y";
			}else{
				//验证码错误！
				status = "f3";
			}
		}
		return status;
	}
	/**
	 * 创建团队
	 * @param account
	 * @param password
	 * @param orgName
	 * @return
	 */
	public HttpResult<UserAuth> registCreateorg(String account,String userName,
			String password, String orgName,String optIP) {
		
		HttpResult<UserAuth> httpResult = new HttpResult<UserAuth>();
		
		JoinTemp joinTemp = new JoinTemp();
		//设置主键
		joinTemp.setId(-1);
		//设置用户名称
		joinTemp.setUserName(userName);
		//设置账号
		joinTemp.setAccount(account);
		//密码加密
		String passwordMD5 = Encodes.encodeMd5(password);
		joinTemp.setPasswd(passwordMD5);
		//开始注册
		UserInfo userInfo = registService.addOrgAndUser(joinTemp,orgName,optIP);
		
		httpResult.setCode(HttpResult.CODE_OK);
		String authKey = AppAuthKeyManager.newAuthKey(userInfo);
		UserAuth userAuth = new UserAuth(userInfo, authKey);
		httpResult.setData(userAuth);
		
		return httpResult;
	}
	/**
	 * 申请加入或取消申请
	 * @param account 账号
	 * @param comId 企业号
	 * @param userName 用户名称
	 * @param password 密码
	 * @param applyState 申请状态 0 申请1 取消
	 * @return
	 */
	public String registApplyInOrg(String account, Integer comId,String userName,
			String password, Integer applyState) {
		String status = "y";
		//后台再次验证企业的名称
		Organic org = organicService.getOrganicByNum(comId);
		if(null==org){
			//企业不存在
			status = "f";
		}else{
			//密码加密
			String passwordMD5 = Encodes.encodeMd5(password);
			
			//是申请加入，需要修改信息
			if(applyState==0){
				UserInfo user = userInfoService.getUserInfoByAccount(account);
				if(null!=user){
					passwordMD5 = user.getPassword();
					userName = user.getUserName();
				}
				registService.addJoinTemp(account, userName, passwordMD5, null!=user);
				
			}
			
			//加入的临时表
			JoinTemp joinTemp = new JoinTemp();
			//设置密码
			joinTemp.setPasswd(passwordMD5);
			//设置账号
			joinTemp.setAccount(account);
			//设置用户名
			joinTemp.setUserName(userName);
			
			registService.updateJoinTempType(account,comId,applyState,joinTemp);
		}
		
		return status;
	}
	/**
	 * 模糊查询企业名称
	 * @param account
	 * @param searchName
	 * @return
	 */
	public List<Organic> searchOrgList(String account, String searchName) {
		//取得查询企业名称
		List<Organic> orgList = organicService.listSearchOrg(account,searchName);
		return orgList;
	}
	/**
	 * 列出已加入的企业
	 * @param account 账号
	 * @return
	 */
	public List<Organic> listUserAllOrg(String account) {
		//查询该账号的所加入的所有团队
		List<Organic> listUserAllOrg = organicService.listUserAllOrg(account);
		return listUserAllOrg;
	}
	/**
	 * 同意加入团队
	 * @param account 账号
	 * @param comId 团队号
	 * @param confirmCode 确认码
	 * @param registrationId 激光标识
	 * @param appSource
	 * @param optIP
	 * @return
	 */
	public HttpResult<UserAuth> agreeInOrg(
			String account, Integer comId, String confirmCode, String registrationId,String appSource, String optIP) {
		// 返回对象声明
		HttpResult<UserAuth> httpResult = new HttpResult<UserAuth>();
		
		//取得临时用户信息
		JoinTemp joinTemp = registService.getJoInTempByAccount(account.toLowerCase(), comId, ConstantInterface.JOIN_INVITE);
		//验证邀请码,并添加用户
		Map<String, Object> map = registService.agreeInOrg(confirmCode, comId, null, joinTemp,optIP);
		
		if(map.get("status").equals("y")){
			UserInfo userInfo = (UserInfo) map.get("userInfo");
			if(null != userInfo){
				httpResult.setCode(HttpResult.CODE_OK);
				
				Integer count = userInfoService.countUserIn(userInfo.getId());
				userInfo.setUserInOrgNums(count);
				
				String authKey = AppAuthKeyManager.newAuthKey(userInfo);
				UserAuth userAuth = new UserAuth(userInfo, authKey);
				httpResult.setData(userAuth);
				
				
				jpushRegisteService.updateJPushRegist(userInfo.getId(),registrationId,appSource);
			}else{
				httpResult.setMsg("登入异常，请及时联系管理员！");
				httpResult.setCode(-1);
			}
		}else{
			httpResult.setMsg(map.get("info").toString());
			httpResult.setCode(-1);
		}
		return httpResult;
	}
	/**
	 * 拒绝加入团队
	 * @param account 账号
	 * @param comId 团队号
	 * @return
	 */
	public HttpResult<String> rejectInOrg(String account, Integer comId) {
		// 返回对象声明
		HttpResult<String> httpResult = new HttpResult<String>();
		registService.rejectInOrg(account,comId,"yes");
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}

	/**
	 * 
	 * 修改密码
	 * @param account 需修改的账户
	 * @param password 新密码
	 * @return
	 */
	public HttpResult<String> updatePassword(String account, String password) {
		HttpResult<String> httpResult = new HttpResult<String>();
		//再次判断用户是否存在
		UserInfo user = userInfoService.getUserInfoByAccount(account.toLowerCase());
		if(null==user){
			httpResult.setMsg("账号不存在！");
//			httpResult.setData("账号不存在！");
			httpResult.setCode(-1);
		}else{
			//判断验证码的有效性
			PassYzm passYzmObj = registService.getPassYzm(account.toLowerCase());
			//删除验证码
			if(null!=passYzmObj){
				userInfoService.delPassYzmById(passYzmObj.getId());
			}
			user.setPassword(Encodes.encodeMd5(password));
			// 通过帐号修改用户
			userInfoService.updateUserInfoByAccount(user);
			httpResult.setCode(HttpResult.CODE_OK);
		}
		return httpResult;
	}
	/**
	 * 取得加入记录
	 * @param userInfo
	 * @param busId
	 * @return
	 */
	public HttpResult<JoinRecord> getJoinRecord(UserInfo sessionUser, Integer busId) {
		HttpResult<JoinRecord> httpResult = new HttpResult<JoinRecord>();
		UserInfo user = userInfoService.getUserInfo(sessionUser.getComId(),sessionUser.getId()); 
		if(user.getAdmin().equals("0")){
			httpResult.setCode(-1);
			httpResult.setMsg("抱歉，你没有审核权限");
			return httpResult;
		}
		
		//待办事项标识为查看
		todayWorksService.updateTodoWorkRead(busId, sessionUser.getComId(), sessionUser.getId(), ConstantInterface.TYPE_APPLY,0);
		//加入记录
		JoinRecord joinRecord = userInfoService.getJoinRecord(busId);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(joinRecord);
		return httpResult;
	}
	/**
	 * 审核加入记录
	 * @param userInfo
	 * @param account
	 * @param checkState
	 * @param busId
	 * @return
	 */
	public HttpResult<String> checkJoinRecord(UserInfo userInfo,
			String account, String checkState, Integer busId,String optIP) {
		HttpResult<String> httpResult = new HttpResult<String>();
		JoinRecord joinRecord = new JoinRecord();
		joinRecord.setId(busId);
		joinRecord.setComId(userInfo.getComId());
		joinRecord.setAccount(account);
		//审核用户后发送消息
		userInfoService.updateCheckTempUser(joinRecord,userInfo,checkState,optIP);
		
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 添加GPS位置
	 * @param userInfo
	 * @param pointGPSStr
	 * @return
	 */
	public HttpResult<String> addPointGPS(UserInfo userInfo, String pointGPSStr) {
		// 返回对象声明
		HttpResult<String> httpResult = new HttpResult<String>();
		Gson gson = new Gson();
		PointGPS pointGps = gson.fromJson(pointGPSStr, PointGPS.class);
		pointGPSService.addPointGPS(pointGps,userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 取得打卡位置
	 * @param userInfo
	 * @param pointGPSId
	 * @return
	 */
	public HttpResult<PointGPS> getPointGPS(UserInfo userInfo,
			Integer pointGPSId) {
		HttpResult<PointGPS> httpResult = new HttpResult<PointGPS>();
		PointGPS pointGPS = pointGPSService.getPointGPS(userInfo,pointGPSId);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(pointGPS);
		return httpResult;
	}

	/**
	 * 获取流程分类下的所属流程
	 * @param userInfo
	 * @return
	 */
	public HttpResult<List<SpFlowType>> listSpFlowTypeOfFlowModel(UserInfo userInfo) {
		HttpResult<List<SpFlowType>> httpResult = new HttpResult<List<SpFlowType>>();
		List<SpFlowType> listSpFlowTypes = flowDesignService.listSpFlowTypeOfFlowModel(userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listSpFlowTypes);
		return httpResult;
	}

	/**
	 * 发起流程
	 * @param userInfo
	 * @param spBusId 流程模板主键
	 * @param flowState 流程状态0发起，此时spBusId为模板主键，否则为流程实例化主键
	 * @return
	 */
	public HttpResult<SpFlowInstance> startSpFlow(UserInfo userInfo,
			Integer spBusId, Integer flowState) {
		HttpResult<SpFlowInstance> httpResult = new HttpResult<SpFlowInstance>();
		SpFlowInstance spFlowInstance = null;
		//发起的是固定流程
		if(flowState==0){
			spFlowInstance = workFlowService.initSpFlow(spBusId,userInfo);
			//初始化审批关联
			spFlowInstance.setStagedItemId(0);
			spFlowInstance.setBusId(0);
			spFlowInstance.setBusType("0");
		}else if (flowState == -1){
			//发起的是自定义流程
			spFlowInstance = workFlowService.initSpFlowByUserDefined(spBusId,userInfo);
			//初始化审批关联
			spFlowInstance.setStagedItemId(0);
			spFlowInstance.setBusId(0);
			spFlowInstance.setBusType("0");
		}else{
			spFlowInstance = workFlowService.getSpFlowInstanceById(spBusId,userInfo);
			//不是草稿
			if(flowState!=2){
				List<SpFlowHiStep> listSpFlowHiStep = workFlowService.listSpFlowHiStep(spBusId,userInfo);
				if(null!= listSpFlowHiStep && !listSpFlowHiStep.isEmpty()){
					Collections.reverse(listSpFlowHiStep);
				}
				spFlowInstance.setListSpFlowHiStep(listSpFlowHiStep);
				//已办结的
				if(spFlowInstance.getFlowState().equals(4)){
					todayWorksService.updateTodoWorkRead(spBusId,userInfo.getComId(), 
							userInfo.getId(), ConstantInterface.TYPE_SP_END,0);
				}
				//未办结的
				else{
					todayWorksService.updateTodoWorkRead(spBusId,userInfo.getComId(), 
							userInfo.getId(), ConstantInterface.TYPE_FLOW_SP,0);
				}
			}
		}
		//取得表单信息
		Map<String,Object> map = workFlowService.findFormData(spFlowInstance,spFlowInstance.getId()
				,spFlowInstance.getLayoutId(),userInfo);
		//取得布局信息
		spFlowInstance.setFormLayout((FormLayout)map.get("formLayout"));
		//取得表单数据
		spFlowInstance.setFormData((FormData)map.get("formData"));
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(spFlowInstance);
		return httpResult;
	}

	/**
	 * 验证步骤配置信息
	 * @param actInstaceId activity实例化主键
	 * @param instanceId 流程实例化主键
	 */
	@SuppressWarnings("static-access")
	public HttpResult<String> checkStepCfg(UserInfo userInfo,
			String actInstaceId,Integer instanceId) {
		
		HttpResult<String> httpResult = new HttpResult<String>();
		httpResult.setCode(httpResult.CODE_OK);
		
		JSONObject resultObj = new JSONObject();
		
		SpFlowHiStep spFlowHiStep = flowDesignService.checkStepCfg(userInfo,actInstaceId,instanceId);
		if(null == spFlowHiStep){
			resultObj.put("info", "步骤信息配置错误");
			resultObj.put("status", "f");
		}else{
			//需要配置
			if(!StringUtils.isEmpty(spFlowHiStep.getSpCheckCfg()) 
					&& spFlowHiStep.getSpCheckCfg().equals(ConstantInterface.SPSTEP_CHECK_YES)){
				resultObj.put("status", "y");
				resultObj.put("needCfg", "y");
				//需要配置
				String movePhone = userInfo.getMovePhone();
				//没有设定手机号
				if(StringUtils.isEmpty(movePhone)){
					resultObj.put("needPhone", "y");
				}else{
					resultObj.put("needPhone", "f");
					resultObj.put("movePhone", movePhone);
				}
			}else{
				//不需要配置
				resultObj.put("status", "y");
				resultObj.put("needCfg", "f");
			}
		}
		httpResult.setData(resultObj.toString());
		return httpResult;
	}
	/**
	 * 审批步骤配置
	 * @param userInfo
	 * @param instanceId
	 * @return
	 */
	@SuppressWarnings("static-access")
	public HttpResult<SpFlowHiStep> querySpFlowNextStepInfo(UserInfo userInfo, Integer instanceId){
		HttpResult<SpFlowHiStep> httpResult = new HttpResult<SpFlowHiStep>();
		//获取流程下一步步骤信息
		SpFlowHiStep stepInfo = workFlowService.querySpFlowNextStepInfo(instanceId, userInfo);
		httpResult.setCode(httpResult.CODE_OK);
		httpResult.setData(stepInfo);
		return httpResult;
	}
		
	/**
	 * 保存表单数据
	 * @param userInfo 当前操作员
	 * @param formDataStr 表单数据构造
	 * @param saveType 保存类型
	 * @return
	 * @throws Exception 
	 */
	public HttpResult<String> addFormData(UserInfo userInfo,
			String formDataStr, String saveType) {
		HttpResult<String> httpResult = new HttpResult<String>();
//		httpResult.setCode(HttpResult.CODE_OK);
//		JSONObject json = new JSONObject();
//		json.put("status", "y");
//		httpResult.setData(json.toString());
		//发起审批流程
		if("add".equals(saveType.trim())){
			try {
				workFlowService.addFormData(formDataStr,userInfo);
				httpResult.setCode(HttpResult.CODE_OK);
			} catch (Exception e) {
				httpResult.setCode(-1);
				httpResult.setMsg("系统错误，请联系管理人员");
			}
			httpResult.setCode(HttpResult.CODE_OK);
		}else if("update".equals(saveType.trim())){
			//流程审批
			try {
				Map<String,Object> resultMap = workFlowService.updateSpFlow(formDataStr,userInfo);
				httpResult.setCode(HttpResult.CODE_OK);
				JSONObject json = new JSONObject();
				if(null!=resultMap && resultMap.get("status").equals("f")){
					json.put("status", "f");
					json.put("info", resultMap.get("info"));
				}else{
					json.put("status", "y");
					
				}
				httpResult.setData(json.toString());
			} catch (Exception e) {
				httpResult.setCode(-1);
				httpResult.setMsg("系统错误，请联系管理人员");
			}
		}else if("back".equals(saveType.trim())){
			//流程回退
			try {
				Map<String,Object> resultMap = workFlowService.spFlowTurnBackTo(formDataStr,userInfo);
				httpResult.setCode(HttpResult.CODE_OK);
				JSONObject json = new JSONObject();
				if(null!=resultMap && resultMap.get("status").equals("f")){
					json.put("status", "f");
					json.put("info", resultMap.get("info"));
				}else{
					json.put("status", "y");
				}
				httpResult.setData(json.toString());
			} catch (Exception e) {
				httpResult.setCode(-1);
				httpResult.setMsg("系统错误，请联系管理人员");
			}
		}
		return httpResult;
	}
	
	/**
	 * 如果流程步骤有直属上级审批配置，则验证当前操作人是否设置了直属上级
	 * @param userInfo
	 * @param flowId
	 * @return
	 */
	public HttpResult<String> haveSetDirectLeader(UserInfo userInfo,Integer flowId) {
		boolean result = workFlowService.toSetDirectLeader(userInfo,flowId);
		HttpResult<String> httpResult = new HttpResult<String>();
		httpResult.setData(!result ? "y" : "f");
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	/**
	 * 拾取流程
	 * @param userInfo
	 * @param instanceId
	 * @return
	 */
	public HttpResult<String> pickSpFlow(UserInfo userInfo,Integer instanceId) {
		HttpResult<String> httpResult = new HttpResult<String>();
		httpResult.setData("true");
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	
	/**
	 * 实例化会签信息
	 * @param userInfo 当前操作人员
	 * @param instanceId流程实例主键
	 * @param modFormStepDataStr 会签配置信息
	 * @return
	 */
	public HttpResult<String> initSpHuiQian(UserInfo userInfo, Integer instanceId, 
			String modFormStepDataStr){
		HttpResult<String> httpResult = new HttpResult<String>();
		try {
			//审批会签
			workFlowService.initSpHuiQian(instanceId,modFormStepDataStr,userInfo);
			httpResult.setCode(HttpResult.CODE_OK);
			httpResult.setData("ok");
			httpResult.setMsg("success");
		} catch (Exception e) {
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setMsg("流程参数错误！");
			httpResult.setMsg("error");
		}
		return httpResult;
	}
	
	/**
	 * 取得会签信息
	 * @param userInfo 当前操作人员
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	public HttpResult<SpFlowHuiQianInfo> querySpFlowHuiQianInfo(UserInfo userInfo, Integer instanceId){
		HttpResult<SpFlowHuiQianInfo> httpResult = new HttpResult<SpFlowHuiQianInfo>();
		SpFlowHuiQianInfo spFlowHuiQianInfo = workFlowService.querySpFlowHuiQianInfo(instanceId,userInfo);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(spFlowHuiQianInfo);
		httpResult.setMsg("success");
		return httpResult;
	}
	/**
	 * 会签操作
	 * @param userInfo 当前操作人员
	 * @param spFlowHuiQianInfoStr 会签参数
	 * @return
	 */
	public HttpResult<String> initEndHuiQian(UserInfo userInfo, String spFlowHuiQianInfoStr){
		HttpResult<String> httpResult = new HttpResult<String>();
		Gson gson = new Gson();
		SpFlowHuiQianInfo spFlowHuiQianInfo = gson.fromJson(spFlowHuiQianInfoStr, SpFlowHuiQianInfo.class);
		try {
			workFlowService.initEndHuiQian(spFlowHuiQianInfo,userInfo);
			httpResult.setCode(HttpResult.CODE_OK);
			httpResult.setData("ok");
			httpResult.setMsg("会签已反馈。");
		} catch (Exception e) {
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setMsg("流程参数错误！");
			httpResult.setMsg("error");
		}
		return httpResult;
	}
	
	/**
	 * 流程转办
	 * @param userInfo
	 * @param instanceId 流程实例化主键
	 * @param actInstanceId 流程
	 * @param newAssignerId 流程办理人员
	 * @return
	 */
	public HttpResult<String> updateSpInsAssign(UserInfo userInfo, Integer instanceId, 
			String actInstanceId, Integer newAssignerId){
		HttpResult<String> httpResult = new HttpResult<String>();
		
		if(newAssignerId.equals(userInfo.getId())){
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setData("不能转办给自己！");
			httpResult.setMsg("error"); 
			return httpResult;
		}
		
		try {
			workFlowService.updateSpInsAssign(userInfo,instanceId,actInstanceId,newAssignerId);
			httpResult.setCode(HttpResult.CODE_OK);
			httpResult.setData("ok");
			httpResult.setMsg("已成功转办！");
		} catch (Exception e) {
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setData("流程参数错误！");
			httpResult.setMsg("error");
		}
		
		return httpResult;
	}
	/**
	 * 分页查询审批流程
	 * @param userInfo 当前操作员
	 * @param spFlowType 审批范围类型
	 * @param flowState 审批状态
	 * @param creator 发起人
	 * @param flowName 流程名称
	 * @return
	 */
	public List<SpFlowInstance> spFlowList(UserInfo userInfo,SpFlowInstance instance,String spFlowType) {
		List<SpFlowInstance> result = new ArrayList<SpFlowInstance>();
		//代办审批
		if("todo".equals(spFlowType)){
			result = workFlowService.listSpTodDo(userInfo,instance);
		}
		//我的审批
		else if("mine".equals(spFlowType)){
			result = workFlowService.listSpFlowOfMine(userInfo,instance);
			
		}
		//关注审批
		else if("atten".equals(spFlowType)){
			instance.setAttentionState("1");
			result= workFlowService.listSpFlowOfAll(userInfo,instance);
		}
		//全部审批
		else{
			result= workFlowService.listSpFlowOfAll(userInfo,instance);
		}
		return result;
	}
	/**
	 * 获取已执行的审批步骤集合
	 * @param userInfo 当前操作员
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	public HttpResult<List<SpFlowHiStep>> listHistorySpStep(UserInfo userInfo,Integer instanceId){
		HttpResult<List<SpFlowHiStep>> httpResult = new HttpResult<List<SpFlowHiStep>>();
		List<SpFlowHiStep> listHistorySpStep = workFlowService.listHistorySpStep(userInfo,instanceId);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listHistorySpStep);
		return httpResult;
	}
	
	/**
	 * 查询所有的审批留言信息
	 * @param userInfo 当前操作人员
	 * @param instanceId 审批流程主键
	 * @return
	 */
	public HttpResult<List<SpFlowTalk>> listSpFlowTalk(UserInfo userInfo,
			Integer instanceId) {
		HttpResult<List<SpFlowTalk>> httpResult = new HttpResult<List<SpFlowTalk>>();
		List<SpFlowTalk> listSpFlowTalk = workFlowService.listSpFlowTalk(instanceId, userInfo.getComId());
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setData(listSpFlowTalk);
		return httpResult;
	}
	
	/**
	 * 添加审批留言
	 * @param userInfo 当前操作人员
	 * @param spFlowTalk
	 * @return
	 */
	public HttpResult<SpFlowTalk> addSpFlowTalk(UserInfo userInfo,
			String spFlowTalkStr) {
		Gson gson = new Gson();
		SpFlowTalk spFlowTalk = gson.fromJson(spFlowTalkStr, SpFlowTalk.class);
		
		HttpResult<SpFlowTalk> httpResult = new HttpResult<SpFlowTalk>();
		try {
			spFlowTalk.setComId(userInfo.getComId());
			spFlowTalk.setSpeaker(userInfo.getId());
			
			Integer id = workFlowService.addSpFlowTalk(spFlowTalk,userInfo);
			spFlowTalk = workFlowService.querySpFlowTalk(id, userInfo.getComId());
			
			spFlowTalk.setIsLeaf(1);
			httpResult.setCode(HttpResult.CODE_OK);
			httpResult.setData(spFlowTalk);
		} catch (Exception e) {
			httpResult.setCode(HttpResult.CODE_ERROR);
			httpResult.setMsg("文件保存导致，留言添加失败！");
		}
		
		return httpResult;
	}
	
	
	
	
	
	/**
	 * 分页查询表单信息
	 * @param userInfo 当前操作人员
	 * @param formMod 流程使用的表单信息
	 * @return
	 */
	public List<FormMod> formListForSelect(UserInfo userInfo,FormMod formMod){
		List<FormMod> formModlist = formService.listPagedFormMod(formMod,userInfo);
		return formModlist;
	}
	/**
	 * 预览表单模板
	 * @param userInfo 当前操作员
	 * @param formModId 表单主键
	 * @return
	 */
	public FormLayout getFormModbyId(UserInfo userInfo,Integer formModId){
		FormLayout formMod = formService.getFormLayoutByModId(formModId, userInfo);
		return formMod;
	}

	/**
	 * 计算时间
	 * @param userInfo 当前操作员
	 * @param dateS 时间开始
	 * @param dateE 时间结束
	 * @param calTimeType 计算类型 1计算工作时段 2计算非工作时段
	 * @return
	 */
	public String calDateTime(UserInfo userInfo, String dateS, String dateE,
			String calTimeType) {
		Double hours = 0.0D;
		if("1".equals(calTimeType)){
			//计算工作时段
			hours = festModService.calWorkTime(userInfo.getComId(), dateS, dateE);
		}else if("2".equals(calTimeType)){
			//计算非工作时段
			hours = festModService.calOverTime(userInfo.getComId(), dateS, dateE);
		}
		return hours.toString();
	}
	/**
	 * 验证验证码是否正确
	 * @param account
	 * @param param
	 * @return
	 */
	@SuppressWarnings("static-access")
	public HttpResult<String> checkYzm(String account,String param) {
		HttpResult<String> httpResult = new HttpResult<String>();
		PassYzm passYzm = registService.getPassYzm(account);
		httpResult.setCode(httpResult.CODE_OK);
		JSONObject json = new JSONObject();
		if(null==passYzm ){
			json.put("status", "f");
			json.put("info", "验证码失效！");
			
		}else if(param.equalsIgnoreCase(passYzm.getPassYzm())) {
			json.put("status", "y");
		} else {
			json.put("status", "f");
			json.put("info", "验证码输入错误！");
		}
		httpResult.setData(json.toString());
		return httpResult;
	}
	
	/**
	 * 取得自己权限需要映射的对象
	 * @param userInfo 当前操作人员
	 * @param busType 业务主键
	 * @return
	 */
	public HttpResult<List<BusMapFlow>> listBusMapFlowByAuth(UserInfo userInfo,
			String busType) {
		HttpResult<List<BusMapFlow>> httpResult = new HttpResult<List<BusMapFlow>>();
		List<BusMapFlow> listBusMapFlows = adminCfgService.listBusMapFlowByAuth(userInfo,busType);
		httpResult.setData(listBusMapFlows);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}

	/**
	 * 查询差旅借款信息列表
	 * @param userInfo 当前操作人员
	 * @param loanApply 借款申请信息
	 * @return
	 */
	public HttpResult<PageBean<FeeBudget>> listLoanApplyOfAuth(
			UserInfo userInfo, String loanApplyStr) {
		Gson gson = new Gson();
		FeeBudget loanApply = gson.fromJson(loanApplyStr, FeeBudget.class);
		
		HttpResult<PageBean<FeeBudget>> httpResult = new HttpResult<PageBean<FeeBudget>>();
		PageBean<FeeBudget> pageBean = financialService.listLoanApplyOfAuth(userInfo, loanApply);
		httpResult.setData(pageBean);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	/**
	 * 用于借款的选择界面
	 * @param userInfo
	 * @param loanApplyStr
	 * @return
	 */
	public HttpResult<PageBean<FeeBudget>> listPagedLoanApplyForStartSelect(
			UserInfo userInfo, String loanApplyStr) {
		Gson gson = new Gson();
		FeeBudget loanApply = gson.fromJson(loanApplyStr, FeeBudget.class);
		
		HttpResult<PageBean<FeeBudget>> httpResult = new HttpResult<PageBean<FeeBudget>>();
		PageBean<FeeBudget> pageBean = financialService.listPagedLoanApplyForStartSelect(userInfo,loanApply);
		httpResult.setData(pageBean);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	/**
	 * 报销选择的借款申请
	 * @param userInfo
	 * @param loanApplyStr
	 * @return
	 */
	public HttpResult<PageBean<FeeBudget>> listPagedLoanApplyForOffSelect(
			UserInfo userInfo, String loanApplyStr) {
		Gson gson = new Gson();
		FeeBudget loanApply = gson.fromJson(loanApplyStr, FeeBudget.class);
		
		HttpResult<PageBean<FeeBudget>> httpResult = new HttpResult<PageBean<FeeBudget>>();
		PageBean<FeeBudget> pageBean = financialService.listPagedLoanApplyForOffSelect(userInfo,loanApply);
		httpResult.setData(pageBean);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}

	/**
	 * 借款信息列表
	 * @param userInfo 当前操作人员
	 * @param loan 借款信息
	 * @return
	 */
	public HttpResult<PageBean<FeeLoan>> listLoanOfAuth(UserInfo userInfo,
			String loanStr) {
		Gson gson = new Gson();
		FeeLoan loan = gson.fromJson(loanStr, FeeLoan.class);
		
		HttpResult<PageBean<FeeLoan>> httpResult = new HttpResult<PageBean<FeeLoan>>();
		PageBean<FeeLoan> pageBean = financialService.listLoanOfAuthV2(userInfo, loan);
		httpResult.setData(pageBean);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}

	/**
	 * 报销信息列表
	 * @param userInfo 当前操作员
	 * @param loanOffAccount 报销信息
	 * @return
	 */
	public HttpResult<PageBean<FeeLoanOff>> listLoanOffOfAuth(
			UserInfo userInfo, String loanOffAccountStr) {
		Gson gson = new Gson();
		FeeLoanOff loanOffAccount = gson.fromJson(loanOffAccountStr, FeeLoanOff.class);
		
		HttpResult<PageBean<FeeLoanOff>> httpResult = new HttpResult<PageBean<FeeLoanOff>>();
		PageBean<FeeLoanOff> pageBean = financialService.listLoanOffOfAuthV2(userInfo,loanOffAccount,null);
		httpResult.setData(pageBean);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	

	/**
	 * 添加模块审批
	 * @param userInfo 当前操作人员
	 * @param busMapFlowId 采取的映射关系
	 * @param busType 业务类型
	 * @return
	 */
	public HttpResult<SpFlowInstance> addWorkBus(UserInfo userInfo,
			Integer busMapFlowId, String busType) {
		
		HttpResult<SpFlowInstance> httpResult = new HttpResult<SpFlowInstance>();
		SpFlowInstance spFlowInstance =null;
		//出差借款申请 或 常规借款
		if(busType.equals(ConstantInterface.TYPE_FEE_APPLY_TRIP)
				|| busType.equals(ConstantInterface.TYPE_FEE_APPLY_DAYLY)){
			spFlowInstance = busRelateSpFlowService.addFeeBudget(busMapFlowId,userInfo,busType);
		}
		//请假申请
		else if(busType.equals(ConstantInterface.TYPE_LEAVE)){
			spFlowInstance = busRelateSpFlowService.addLeaveApply(busMapFlowId,userInfo,busType);
		}
		//加班申请
		else if(busType.equals(ConstantInterface.TYPE_OVERTIME)){
			spFlowInstance = busRelateSpFlowService.addOverTimeApply(busMapFlowId,userInfo,busType);
		}
		
		httpResult.setData(spFlowInstance);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}

	/**
	 * 发起借款
	 * @param busMapFlowId 采用的映射关系
	 * @param userInfo 当前操作人员 
	 * @param feeBudgetId 借款申请主键
	 * @param busType 业务类型
	 * @param isBusinessTrip 是否为业务出差
	 * @return
	 */
	public HttpResult<SpFlowInstance> addLoan(Integer busMapFlowId,
			UserInfo userInfo, Integer feeBudgetId, String busType,
			Integer isBusinessTrip) {
		HttpResult<SpFlowInstance> httpResult = new HttpResult<SpFlowInstance>();
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addLoan(busMapFlowId,userInfo,feeBudgetId,busType,isBusinessTrip);
		httpResult.setData(spFlowInstance);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}

	/**
	 * 添加借款工作汇报
	 * @param busMapFlowId 采用的映射关系
	 * @param userInfo 当前操作人员
	 * @param applyId 采用的借款申请主键
	 * @param busType 业务类型
	 * @return
	 */
	public HttpResult<SpFlowInstance> addLoanReport(Integer busMapFlowId,
			UserInfo userInfo, Integer feeBudgetId, String busType) {
		HttpResult<SpFlowInstance> httpResult = new HttpResult<SpFlowInstance>();
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addLoanReport(busMapFlowId,userInfo,feeBudgetId,busType);
		httpResult.setData(spFlowInstance);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	/**
	 * 发起报销
	 * @param busMapFlowId 采用的映射关系主键
	 * @param userInfo 当前操作人员
	 * @param busType  业务类型
	 * @param loanWay 借款方式
	 * @param feeBudgetId 借款采用的主键
	 * @param loanReportWay 汇报方式
	 * @param loanReportId 汇报主键
	 * @return
	 */
	public HttpResult<SpFlowInstance> addLoanOff(Integer busMapFlowId,
			UserInfo userInfo, String busType, String loanWay,
			Integer feeBudgetId, String loanReportWay, Integer loanReportId) {
		HttpResult<SpFlowInstance> httpResult = new HttpResult<SpFlowInstance>();
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addLoanOff(busMapFlowId,userInfo,busType,loanWay,feeBudgetId,loanReportWay,loanReportId);
		httpResult.setData(spFlowInstance);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}

	/**
	 * 分页查询加班信息
	 * @param userInfo 当前操作人员
	 * @param overTime 加班信息
	 * @return
	 */
	public HttpResult<PageBean<OverTime>> listPagedOverTime(UserInfo userInfo,
			String overTimeStr) {
		Gson gson = new Gson();
		OverTime overTime = gson.fromJson(overTimeStr, OverTime.class);
		
		HttpResult<PageBean<OverTime>> httpResult = new HttpResult<PageBean<OverTime>>();
		PageBean<OverTime> pageBean = attenceService.listPagedOverTime(userInfo,overTime);
		httpResult.setData(pageBean);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	/**
	 * 分页查询请假信息列表
	 * @param userInfo 当前操作人员
	 * @param leave 请假信息
	 * @return
	 */
	public HttpResult<PageBean<Leave>> listPagedLeave(UserInfo userInfo,
			String leaveStr) {
		Gson gson = new Gson();
		Leave leave = gson.fromJson(leaveStr, Leave.class);
		HttpResult<PageBean<Leave>> httpResult = new HttpResult<PageBean<Leave>>();
		PageBean<Leave> pageBean = attenceService.listPagedLeave(userInfo,leave);
		httpResult.setData(pageBean);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}

	/************************消费记录************************************/
	
	/**
	 * 添加记账数据信息
	 * @param userInfo 当前操作人员
	 * @param consumeStr 记账数据
	 * @return
	 */
	public HttpResult<String> addConsume(UserInfo userInfo, String consumeStr){
		Gson gson = new Gson();
		Consume consume = gson.fromJson(consumeStr, Consume.class);
		consumeService.addConsume(consume, userInfo);
		
		HttpResult<String> httpResult = new HttpResult<String>();
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	
	/**
	 * 添加记账类型数据信息
	 * @param userInfo 当前操作人员
	 * @param consumeTypeStr 记账类型数据
	 * @return
	 */
	public HttpResult<String> addConsumeType(UserInfo userInfo, String consumeTypeStr){
		Gson gson = new Gson();
		ConsumeType consumeType = gson.fromJson(consumeTypeStr, ConsumeType.class);
		consumeService.addConsumeType(consumeType, userInfo);
		HttpResult<String> httpResult = new HttpResult<String>();
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 分页查询用于选择的
	 * @param userInfo 当前操作人员
	 * @param consumeStr 用于查询的消费记录json str
	 * @return
	 */
	public HttpResult<PageBean<Consume>> listPagedConsumeForSelect(UserInfo userInfo, String consumeStr){
		Gson gson = new Gson();
		Consume consume = gson.fromJson(consumeStr, Consume.class);

		HttpResult<PageBean<Consume>> httpResult = new HttpResult<PageBean<Consume>>();
		PageBean<Consume> pageBean = consumeService.listPagedConsumeForSelect(userInfo, consume);
		httpResult.setData(pageBean);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 分页查询所有的记账数据
	 * @param userInfo 当前操作人员
	 * @param consumeStr 用于查询的消费记录json str
	 * @return
	 */
	public HttpResult<PageBean<Consume>> listPagedConsume(UserInfo userInfo, String consumeStr){
		Gson gson = new Gson();
		Consume consume = gson.fromJson(consumeStr, Consume.class);

		HttpResult<PageBean<Consume>> httpResult = new HttpResult<PageBean<Consume>>();
		PageBean<Consume> pageBean = new PageBean<>();
		List<Consume> lists = consumeService.listPagedConsume(consume,userInfo,"startDate");
		pageBean.setRecordList(lists);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		httpResult.setData(pageBean);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 查询团队的记账类型
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public HttpResult<List<ConsumeType>>  listConsumeType(UserInfo userInfo){
		HttpResult<List<ConsumeType>> httpResult = new HttpResult<List<ConsumeType>>();
		List<ConsumeType> consumeTypes = consumeService.listConsumeType(userInfo.getComId());
		httpResult.setData(consumeTypes);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 查询所有消费记录附件
	 * @author hcj 
	 * @param userInfo
	 * @param consumeId
	 * @return 
	 * @date 2018年9月5日 上午10:06:08
	 */
	public HttpResult<List<ConsumeUpfile>> listConsumeUpfile(UserInfo userInfo, Integer consumeId) {
		HttpResult<List<ConsumeUpfile>> httpResult = new HttpResult<List<ConsumeUpfile>>();
		List<ConsumeUpfile> lists = consumeService.listConsumeUpfile(userInfo.getComId(),consumeId);
		httpResult.setData(lists);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 根据查询记账详情
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public HttpResult<Consume> queryConsumeById(UserInfo userInfo,Integer id){
		HttpResult<Consume> httpResult = new HttpResult<Consume>();
		Consume consume = consumeService.getConsumeById(id);
		List<ConsumeUpfile> upfiles = consumeService.listConsumeUpfile(userInfo.getComId(), id);
		if(!CommonUtil.isNull(upfiles)) {
			consume.setListUpfiles(upfiles);
		}
		httpResult.setData(consume);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 根据查询记账类型详情
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public HttpResult<ConsumeType>  queryConsumeTypeById(UserInfo userInfo,Integer id){
		HttpResult<ConsumeType> httpResult = new HttpResult<ConsumeType>();
		ConsumeType consumeType = consumeService.getConsumeTypeById(id);
		httpResult.setData(consumeType);
		httpResult.setCode(HttpResult.CODE_OK);
		httpResult.setMsg("success");
		return httpResult;
	}
	
	/**
	 * 批量删除消费记录
	 * @param sessionUser
	 * @param ids删除的消费记录id
	 * @return
	 */
	public HttpResult<String> delConsume(UserInfo sessionUser,Integer[] ids) {
		HttpResult<String> httpResult = new HttpResult<String>();
		consumeService.deleteConsumes(sessionUser, ids);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	
	/**
	 * 删除发票附件
	 * @author hcj 
	 * @param sessionUser
	 * @param consumeId
	 * @param consumeUpFileId
	 * @return 
	 * @date 2018年9月4日 下午1:49:25
	 */
	public HttpResult<String> delConsumeUpfile(UserInfo sessionUser,Integer consumeId,Integer consumeUpFileId) {
		HttpResult<String> httpResult = new HttpResult<String>();
		consumeService.delConsumeUpfile(consumeUpFileId, sessionUser, consumeId);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	
	/**
	 * 更新消费记录的属性值
	 * @param userInfo
	 * @param consumeStr
	 * @return
	 */
	public HttpResult<Consume> updateConsume(UserInfo userInfo, String consumeStr) {
		Gson gson = new Gson();
		Consume consume = gson.fromJson(consumeStr, Consume.class);
		// 返回对象声明
		HttpResult<Consume> httpResult = new HttpResult<Consume>();
		if (null == consume.getId() || 0 == consume.getId()) {
			httpResult.setCode(401);
			httpResult.setMsg("消费记录主键不能是：" + consume.getId());
			return httpResult;
		} 
		boolean succ = consumeService.updateConsumes(consume, userInfo,"app");
		consume = consumeService.getConsumeById(consume.getId());
		consume.setSucc(succ);
		if (succ) {
			consume.setPromptMsg("更新成功");
		} else {
			consume.setPromptMsg("更新失败");
		}
		httpResult.setData(consume);
		httpResult.setCode(HttpResult.CODE_OK);
		return httpResult;
	}
	
	/**
	 * 单独新增发票
	 * @param userInfo
	 * @param upFileIds 上传的发票附件的upFileId
	 * @param id 
	 * @return
	 */
	public HttpResult<String> addInvoice(UserInfo userInfo, String upFileIds, Integer id) {
		HttpResult<String> httpResult = new HttpResult<String>();
		httpResult.setCode(HttpResult.CODE_OK);
		if(!CommonUtil.isNull(upFileIds)) {
			String[] fileIds = upFileIds.split(",");
			try {
				consumeService.addFiles(id, fileIds, userInfo);
				httpResult.setMsg("success");
			} catch (Exception e) {
				e.printStackTrace();
				httpResult.setMsg("上传失败");
			}
		}else {
			httpResult.setMsg("请上传相关发票信息");
		}
		
		return httpResult;
	}

	/**
	 * 分页查询外部联系人
	 * @param userInfo
	 * @param outLinkManStr
	 * @return
	 */
	public List<OutLinkMan> listPagedOutLinkMan(UserInfo userInfo,
			String outLinkManStr) {
		Gson gson = new Gson();
		OutLinkMan outLinkMan = gson.fromJson(outLinkManStr, OutLinkMan.class);
		if(null == outLinkMan){
			outLinkMan = new OutLinkMan();
		}
		List<OutLinkMan> list = outLinkManService.listPagedOutLinkMan(outLinkMan,userInfo);
		return list;
	}

	/**
	 * 运营分析统计
	 * @param userInfo
	 * @param platformType
	 * @param startDate
	 * @param endDate
	 * @param taskDateTimeType
	 * @return
	 */
	public String platformByType(UserInfo userInfo, String platformType,
			String startDate, String endDate, String taskDateTimeType,String version) {
		//当前时间
		if(StaticPlatFormTypeEnum.TODAYTASK.getValue().equalsIgnoreCase(platformType)){
			//今日新增任务
			com.alibaba.fastjson.JSONObject jsonObject =  statisticsService.statisticTodayByType(userInfo,ConstantInterface.TYPE_TASK,version);
			return jsonObject.toString();
		}else if(StaticPlatFormTypeEnum.TODAYCRM.getValue().equalsIgnoreCase(platformType)){
			//今日新增客户
			com.alibaba.fastjson.JSONObject jsonObject =  statisticsService.statisticTodayByType(userInfo,ConstantInterface.TYPE_CRM,version);
			return jsonObject.toString();
		}else if(StaticPlatFormTypeEnum.TODAYDAILY.getValue().equalsIgnoreCase(platformType)){
			//今日日报
			com.alibaba.fastjson.JSONObject jsonObject =  statisticsService.statisticTodayByType(userInfo,ConstantInterface.TYPE_DAILY,version);
			return jsonObject.toString();
		}else if(StaticPlatFormTypeEnum.WEEKREPORT.getValue().equalsIgnoreCase(platformType)){
			//周报汇报
			com.alibaba.fastjson.JSONObject jsonObject =  statisticsService.statisticTodayByType(userInfo,ConstantInterface.TYPE_WEEK,version);
			return jsonObject.toString();
		}else if(StaticPlatFormTypeEnum.TASKBYDEP.getValue().equalsIgnoreCase(platformType)){
			//运营分析的各部门任务创建和任务办结统计
			List<StatisticTaskVo> listStatisticTaskVo = statisticsService.statisticTaskForDep(userInfo, startDate, endDate,taskDateTimeType,version);
			
			//运营分析的各部门任务创建和任务办结统计
			List<StatisticTaskVo> listStatisticTopTask = statisticsService.statisticTaskTop(userInfo, startDate, endDate,taskDateTimeType,version);
				
			com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
			jsonObject.put("taskByDep", listStatisticTaskVo);
			jsonObject.put("taskTop", listStatisticTopTask);
			return jsonObject.toJSONString();
			
		}else if(StaticPlatFormTypeEnum.TASKBYGRADE.getValue().equalsIgnoreCase(platformType)){
			//运营分析之任务分类
			List<StatisticTaskVo> listStatisticTaskVo = statisticsService.statisticTaskGrade(userInfo,version);
			Gson gson = new Gson();
			return gson.toJson(listStatisticTaskVo);
		}else if(StaticPlatFormTypeEnum.CRMBYTYPE.getValue().equalsIgnoreCase(platformType)){
			//运营分析之客户分类
			List<StatisticCrmVo> listStatisticCrmVo = statisticsService.statisticCrmType(userInfo);
			Gson gson = new Gson();
			return gson.toJson(listStatisticCrmVo);
		}
		return null;
	}

	/**
	 * 获取日报数据集合
	 * @param daily
	 * @param userInfo
	 * @return
	 * @throws ParseException 
	 */
	public HttpResult<String> listPagedDaily(String dailyStr, UserInfo userInfo) {
		Gson gson = new Gson();
		DailyPojo daily = gson.fromJson(dailyStr, DailyPojo.class);
		PageBean<Daily> result = new PageBean<Daily>();
		try {
			String dailierType = daily.getDailierType();
			Integer nowYear = Integer.parseInt(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy));
			daily.setDailyYear(nowYear);
			
			//查询的开始时间
			String startDate = daily.getStartDate();
			if(StringUtils.isEmpty(startDate)){
				Integer year = daily.getDailyYear();
				startDate = year+"-01-01";
				daily.setStartDate(startDate);
			}
			//查询的结束时间
			String endDate = daily.getEndDate();
			if(StringUtils.isEmpty(endDate)){
				Integer year = daily.getDailyYear();
				if(year.equals(nowYear)){
					endDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
				}else{
					endDate = year+"-12-31";
				}
				daily.setEndDate(endDate);
			}
			
			if(StringUtils.isEmpty(dailierType) || "0".equals(dailierType)){
				result = dailyService.listPagedDaily(daily,userInfo);
			}else{
				//分享列表
				result = dailyService.listPagedDaily(daily,userInfo);
			}
		} catch (ParseException e) {
			logger.error("listPagedDaily 日期格式转换错误！");
			return new HttpResult<String>().ok(gson.toJson(new ArrayList<Daily>()));
		}
		List<Daily> resultList = result.getRecordList();
		if(null == resultList) {
			resultList = new ArrayList<Daily>();
		}
		return new HttpResult<String>().ok(gson.toJson(resultList));
	}

	/**
	 * 查看日志详情
	 * @param dailyId
	 * @param userInfo
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	public HttpResult<String> viewDaily(Integer dailyId, UserInfo userInfo) throws NumberFormatException, ParseException {
		DailyPojo dailyParam = new DailyPojo();
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_DAILY);
		//取得所选分享
		Daily dailyT = dailyService.getDailyForView(dailyId,userInfo,dailyParam,isForceIn);
		if(null==dailyT){//分享不存在
			//查看分享，删除消息提醒
			todayWorksService.updateTodoWorkRead(dailyId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_DAILY,0);
		}
		//查看分享，删除消息提醒
		todayWorksService.updateTodoWorkRead(dailyId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_DAILY,0);
		Gson gson = new Gson();
		return new HttpResult<String>().ok(gson.toJson(dailyT));
	}

	
}
