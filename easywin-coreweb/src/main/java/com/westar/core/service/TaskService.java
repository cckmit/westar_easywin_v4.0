package com.westar.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.AttenceRule;
import com.westar.base.model.AttenceType;
import com.westar.base.model.Attention;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.Customer;
import com.westar.base.model.DataDic;
import com.westar.base.model.DelayApply;
import com.westar.base.model.Department;
import com.westar.base.model.FestModDate;
import com.westar.base.model.Item;
import com.westar.base.model.MsgShare;
import com.westar.base.model.RecycleBin;
import com.westar.base.model.StagedInfo;
import com.westar.base.model.Task;
import com.westar.base.model.TaskExecutor;
import com.westar.base.model.TaskHandOver;
import com.westar.base.model.TaskLog;
import com.westar.base.model.TaskSharer;
import com.westar.base.model.TaskTalk;
import com.westar.base.model.TaskTalkUpfile;
import com.westar.base.model.TaskUpfile;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.pojo.ItemStagedInfo;
import com.westar.base.pojo.ModStaticVo;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.StatisticTaskVo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.JpushUtils;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.TaskDao;
import com.westar.core.thread.IndexUpdateThread;
import com.westar.core.web.DataDicContext;
import com.westar.core.web.PaginationContext;

@Service
public class TaskService {
	
	@Autowired
	TaskDao taskDao;
	@Autowired
	FileCenterService fileCenterService;
	@Autowired
	ItemService itemService;
	@Autowired
	CrmService crmService;
	@Autowired
	IndexService indexService;
	@Autowired
	UploadService uploadService;
	@Autowired
	JiFenService jifenService;
	@Autowired
	SystemLogService systemLogService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	AttentionService attentionService;
	@Autowired
	ClockService clockService;
	@Autowired
	ForceInPersionService forceInService;
	@Autowired
	FestModService festModService;
	@Autowired
	AttenceService attenceService;
    @Autowired
    ViewRecordService viewRecordService;
    
    @Autowired
    TaskPromoteService taskPromoteService;
    
	@Autowired
	TodayWorksService todayWorksService;
	@Autowired
	DepartmentService departmentService;

    /**
	 * 获取督察任务集合
	 * @param task
	 * @return
	 */
	public List<Task> listPageTask(Task taskParam,UserInfo userInfo){
		//验证当前登录人是否是督察人员
	    boolean isForceInPersion = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
	    
	    List<Task> results = new ArrayList<>();
	     
	    List<Task> listPagedeTask = taskDao.listPageTask(taskParam,userInfo,isForceInPersion);
	    if(null!=listPagedeTask && !listPagedeTask.isEmpty()){
	    	for (Task task : listPagedeTask) {
				List<Task> taskWithExector = taskDao.listTaskForExecutor(task.getId(),taskParam,userInfo,isForceInPersion);
				if(null!= taskWithExector && !taskWithExector.isEmpty()){
					for (Task taskDetail : taskWithExector) {
						Task result = new Task();
						BeanUtils.copyProperties(task,result);
						
						result.setExecutor(taskDetail.getExecutor());
						result.setExecutorName(taskDetail.getExecutorName());
						result.setDealTimeLimit(taskDetail.getDealTimeLimit());
						result.setTaskProgress(taskDetail.getTaskProgress());
						results.add(result);
					}
				}else{
					Task result = new Task();
					BeanUtils.copyProperties(task,result);
					results.add(result);
				}
			}
	    }
		return results;
	}
	/**
	 * 获取督察任务集合
	 * @param task
	 * @return
	 */
	public List<Task> listPagedTaskForSupView(Task task,UserInfo userInfo){
		//验证当前登录人是否是督察人员
		boolean isForceInPersion = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
		
		//执行部门
		List<Department> listExecuteDep = task.getListExecuteDep();
		Set<Integer> depIds = new HashSet<Integer>();
		if(null!=listExecuteDep && !listExecuteDep.isEmpty()){//有办理人员
			for (Department department : listExecuteDep) {
				depIds.add(department.getId());
			}
			List<Department>  sondeps = departmentService.listTreeSonDep(depIds.toArray(new Integer[depIds.size()]), ConstantInterface.SYS_ENABLED_YES, userInfo.getComId());
			if(!CommonUtil.isNull(sondeps)){
				for (Department department : sondeps) {
					depIds.add(department.getId());
				}
			}
		}
		List<UserInfo> allSubUser = userInfoService.listUserAllSub(userInfo);
		List<Integer> subUserIds = new ArrayList<Integer>();
		subUserIds.add(userInfo.getId());
		if(null!=allSubUser && !allSubUser.isEmpty()){
			for (UserInfo subUser : allSubUser) {
				subUserIds.add(subUser.getId());
			}
		}
		
		List<Task> results = new ArrayList<Task>();
		List<Task>  listPagedTaskForSupView = taskDao.listPagedTaskForSupView(task,depIds,subUserIds,userInfo,isForceInPersion);
		
		if(null!=listPagedTaskForSupView && !listPagedTaskForSupView.isEmpty()){
			for (Task viewTask : listPagedTaskForSupView) {
				
				List<Task> obj = taskDao.listTaskExecutorForSupView(viewTask.getId(),task,depIds,subUserIds,userInfo,isForceInPersion);
				if(null != obj && !obj.isEmpty()){
					for (Task taskDetail : obj) {
						Task result = new Task();
						BeanUtils.copyProperties(viewTask,result);
						
						result.setExecutor(taskDetail.getExecutor());
						result.setExecutorName(taskDetail.getExecutorName());
						result.setFromUser(taskDetail.getFromUser());
						result.setFromUserName(taskDetail.getFromUserName());
						result.setDealTimeLimit(taskDetail.getDealTimeLimit());
						result.setOperatStartDate(taskDetail.getOperatStartDate());
						result.setOperatEndDate(taskDetail.getOperatEndDate());
						result.setEndTime(taskDetail.getEndTime());
						result.setOverDueLevel(taskDetail.getOverDueLevel());
						results.add(result);
					}
				}else{
					Task result = new Task();
					BeanUtils.copyProperties(viewTask,result);
					results.add(result);
				}
			}
		}
		return results;
	}
	/**
	 * 任务紧急度统计
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Task> allTaskCount(Task task,UserInfo userInfo,boolean isForceInPersion){
		return taskDao.allTaskCount(task,userInfo,isForceInPersion);
	}
	/**
	 * 获取个人权限下的所有任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Task> listTaskOfAll(Task task,UserInfo userInfo,boolean isForceInPersion){
		return taskDao.listTaskOfAll(task,userInfo,isForceInPersion);
	}
	/**
	 * 获取团队任务主键集合
	 * @param userInfo
	 * @return
	 */
	public List<Task> listTaskOfAll(UserInfo userInfo){
		return taskDao.listTaskOfAll(userInfo);
	}
	/**
	 * 获取个人权限下的所有逾期任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Task> listOverdueTaskOfAll(Task task,UserInfo userInfo,boolean isForceInPersion){
		return taskDao.listOverdueTaskOfAll(task,userInfo,isForceInPersion);
	}
	/**
	 * 分页获取逾期任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Task> listOverdueTask(Task task,UserInfo userInfo,boolean isForceInPersion){
		return taskDao.listOverdueTask(task,userInfo,isForceInPersion);
	}
	/**
	 * 获取个人待办任务集合
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> taskToDoList(Task task,UserInfo userInfo){
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			task.setVersion("2");
		}else{
			task.setVersion("1");
		}
		return taskDao.taskToDoList(task,userInfo);
	}
	/**
	 * 待办任务统计
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> taskToDoCount(Task task,UserInfo userInfo){
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			task.setVersion("2");
		}else{
			task.setVersion("1");
		}
		return taskDao.taskToDoCount(task,userInfo);
	}
	/***
	 * 获取所有的待办任务
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> taskToDoListOfAll(Task task,UserInfo userInfo){
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			task.setVersion("2");
		}else{
			task.setVersion("1");
		}
		return taskDao.taskToDoListOfAll(task,userInfo);
	}
	/**
	 * 分页查询负责的任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Task> listPageChargeTask(Task task,UserInfo userInfo){
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			task.setVersion("2");
		}else{
			task.setVersion("1");
		}
		return taskDao.listPageChargeTask(task,userInfo);
	}
	/**
	 * 我负责的任务统计
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> chargeTaskCount(Task task,UserInfo userInfo){
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			task.setVersion("2");
		}else{
			task.setVersion("1");
		}
		return taskDao.chargeTaskCount(task,userInfo);
	}
	/**
	 * 获取所有的自己负责人任务
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> listChargeTaskOfAll(Task task,UserInfo userInfo){
		String version = task.getVersion();
		if(StringUtils.isNotEmpty(version) && version.equals("2")){
			task.setVersion("2");
		}else{
			task.setVersion("1");
		}
		return taskDao.listChargeTaskOfAll(task,userInfo);
	}
	
	/**
	 * 创建新任务
	 * @param task 任务信息
	 * @param msgShare 
	 * @param sessionUser 操作员
	 * @param sendWay 
	 * @return
	 * @throws Exception
	 */
	public Integer addTask(Task task,MsgShare msgShare,UserInfo sessionUser, String[] sendWay) throws Exception{
		
		String version = task.getVersion();
		if(StringUtils.isEmpty(version)){
			task.setVersion("1");
		}
		
		//企业号
		task.setComId(sessionUser.getComId());
		//创建人
		task.setCreator(sessionUser.getId());
		//负责人
		task.setOwner(sessionUser.getId());
		//删除标识(正常)
		task.setDelState(0);
				
		if(null==task.getBusId() || task.getBusId()==0){
			//默认项目主键0
			task.setBusId(0);
			//默认没有类型
			task.setBusType("0");
		}
		//默认父任务主键为-1
		if(null==task.getParentId()){
			task.setParentId(-1);
		}
		
		//任务的办理类型
		String taskType = task.getTaskType();
		//设置任务状态（认领的任务需要）
		String  taskState = taskType.equals(ConstantInterface.TASK_TYPE_CHOOSE)?ConstantInterface.STATIS_TASK_STATE_CHOOSE:ConstantInterface.STATIS_TASK_STATE_DOING;
		List<TaskExecutor> listTaskExecutors = task.getListTaskExecutor();
		if(null!=listTaskExecutors && listTaskExecutors.size()==1 ){//只有一个办理人员，自动认领
			taskState = ConstantInterface.STATIS_TASK_STATE_DOING;
		}
		//设置任务状态信息
		task.setState(Integer.parseInt(taskState));
		
		//任务基本信息存入
		Integer taskId = taskDao.add(task);
		//任务参与人
		Set<Integer> shareIdSets = new HashSet<Integer>();
		//把当前操作人添加为任务参与人
		shareIdSets.add(sessionUser.getId());
		//把任务负责人添加为任务参与人
		shareIdSets.add(task.getOwner());
		
		//父任务参与人
		Set<Integer> pshareIdSets = new HashSet<Integer>();
		//如果父任务ID不为空，则是添加子任务
		if(null!=task.getParentId() && task.getParentId() > 0 ){
			Task pTask = (Task) taskDao.objectQuery(Task.class, task.getParentId());
			//先放父亲任务负责人
			pshareIdSets.add(pTask.getOwner());
			List<TaskSharer> sharers = taskDao.listTaskSharer(task.getParentId(),task.getComId());
			if(null!=sharers && !sharers.isEmpty()){
				for(TaskSharer vo:sharers){
					pshareIdSets.add(vo.getSharerId());
				}
			}
			
		}
		//向sets存放当前任务的参与人
		List<TaskSharer> listTaskShare = task.getListTaskSharer();
		if(null!=listTaskShare && !listTaskShare.isEmpty()){
			for(TaskSharer taskSharer : listTaskShare){
				shareIdSets.add(taskSharer.getSharerId());
			}
		}
		//当前任务的参与人包括父任务
		Set<Integer> taskSharers = new HashSet<Integer>();
		taskSharers.addAll(shareIdSets);
		taskSharers.addAll(pshareIdSets);
		if(null!=taskSharers && taskSharers.size()>0){
			TaskSharer taskSharer =null;
			for (Integer shareId : taskSharers) {
				taskSharer = new TaskSharer();
				taskSharer.setComId(task.getComId());
				taskSharer.setTaskId(taskId);
				taskSharer.setSharerId(shareId);
				taskDao.add(taskSharer);  
			}
		}
		task.setId(taskId);
		//添加任务附件
		this.addTaskFile(task, sessionUser);
		
		/*******此处关联模块数据需要修改一下开始**********/
		//TODO 此处关联模块数据需要修改一下开始,降低依赖关系
		//关联的业务主键
		Integer busId = task.getBusId();
		//任务的模块关联
		if(null!=busId && busId>0){
			if(task.getBusType().equals(ConstantInterface.TYPE_ITEM)){//关联的项目
				//默认项目阶段主键
				Integer stagedItemId =task.getStagedItemId();
				//项目阶段关联
				if(null==task.getStagedItemId() || task.getStagedItemId()==0){
					stagedItemId = itemService.getStageItemId(sessionUser, busId);
				}
				//添加项目阶段明细
				itemService.addStageRelateMod(sessionUser, busId, stagedItemId, taskId,ConstantInterface.TYPE_TASK);
				//添加项目日志
				itemService.addItemLog(sessionUser.getComId(), busId, sessionUser.getId(),
						sessionUser.getUserName(), "发布项目任务:\""+task.getTaskName()+"\"");
				//更新项目索引
//				itemService.updateItemIndex(busId,userInfo,"update");
			}else if(task.getBusType().equals(ConstantInterface.TYPE_CRM)){
				//添加项目日志
				crmService.addCustomerLog(sessionUser.getComId(), busId, sessionUser.getId(),
						 "发布客户任务:\""+task.getTaskName()+"\"");
			}
		}
		/*******此处关联模块数据需要修改一下结束**********/
		
		//添加任务的执行人和流转记录信息
		this.addTaskExectorAndHandInfo(task, taskId,-1,sessionUser,1);
		//无效状态不积分
		if(null!=task.getState() && -1!=task.getState()){
			//添加积分
			jifenService.addJifen(sessionUser.getComId(), sessionUser.getId(), ConstantInterface.TYPE_TASK,
					"创建任务:"+task.getTaskName(),taskId);
		}
		
		//模块日志添加
		this.addTaskLog(sessionUser.getComId(),taskId, sessionUser.getId(), sessionUser.getUserName(), "创建任务:\""+task.getTaskName()+"\"");
		//如果有母任务，则像母任务添加日志
		if(-1!=task.getParentId()){
			this.addTaskLog(sessionUser.getComId(),task.getParentId(), sessionUser.getId(), sessionUser.getUserName(), "分解任务:\""+task.getTaskName()+"\"");
		}
		//标识关注
		if(null!=task.getAttentionState() && task.getAttentionState().equals("1")){
			attentionService.addAtten(ConstantInterface.TYPE_TASK, taskId, sessionUser);
		}
		//任务的负责人，执行人以及关注人员和参与人
		List<UserInfo> shares = taskDao.listTaskOwnersNoForce(sessionUser.getComId(), taskId);
		//设置任务的办理人员
		if(null != listTaskExecutors && !listTaskExecutors.isEmpty()){//设置有办理人员
			
			//添加日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "创建任务\""+task.getTaskName()+"\"",
					ConstantInterface.TYPE_TASK, taskId, sessionUser.getComId(), sessionUser.getOptIP());
			
			for (TaskExecutor taskExecutor : listTaskExecutors) {
				//任务的办理人员
				Integer taskExecutorId = taskExecutor.getExecutor();
				if(!taskExecutorId.equals(sessionUser.getId())){
					//添加待办提醒通知
					todayWorksService.addTodayWorks(sessionUser.getComId(),ConstantInterface.TYPE_TASK, taskId,"办理\""+sessionUser.getUserName()+"\"创建的任务\""+task.getTaskName()+"\"", 
							taskExecutorId, sessionUser.getId(), 1);
					//添加工作轨迹
					systemLogService.addWorkTrace(sessionUser, taskExecutorId, ConstantInterface.TYPE_TASK, taskId, 
							"办理\""+sessionUser.getUserName()+"\"创建的任务\""+task.getTaskName()+"\"");
					String delTimeLimit =  task.getDealTimeLimit();
					String content = "";
					if(null==delTimeLimit || "".equals(delTimeLimit)){
						content = "请尽快办理任务:"+task.getTaskName();
					}else{
						content = "请在 "+delTimeLimit+" 前办理任务:"+task.getTaskName();
					}
					todayWorksService.sendMsgByUserConf(sessionUser, taskId, content, ConstantInterface.TYPE_TASK, shares, sendWay);
					
					//取得待办事项主键
					TodayWorks todayWorks = todayWorksService.getUserTodayWork(sessionUser.getComId(),taskExecutorId,
							taskId,ConstantInterface.TYPE_TASK,0);
					if(null!=todayWorks){
						JpushUtils.sendTodoMessage(sessionUser.getComId(), taskExecutorId, sessionUser.getId(),
								todayWorks.getId(), taskId, ConstantInterface.TYPE_TASK,0,"任务:"+task.getTaskName());
					}
					//模块日志添加
					this.addTaskLog(sessionUser.getComId(),taskId, taskExecutorId, taskExecutor.getExecutorName(), "办理\""+sessionUser.getUserName()+"\"创建的任务\""+task.getTaskName()+"\"");
					
				}else{
					//添加待办提醒通知
					todayWorksService.addTodayWorks(sessionUser.getComId(),ConstantInterface.TYPE_TASK, taskId,"创建的任务\""+task.getTaskName()+"\"", 
							taskExecutorId, sessionUser.getId(), 1);
					
					//添加工作轨迹
					systemLogService.addWorkTrace(sessionUser, taskExecutorId, ConstantInterface.TYPE_TASK, taskId, 
							"创建的任务\""+task.getTaskName()+"\"");
				}
			}
		}
		//添加任务索引
		this.updateTaskIndex(taskId,sessionUser,"add");
		return taskId;
	}
	
	/**
	 * 添加任务的办理人员和移交信息
	 * @param task
	 * @param taskId
	 */
	public void addTaskExectorAndHandInfo(Task task, Integer taskId,Integer preStep,UserInfo sessionUser,Integer actHandleState) {
		//任务的办理类型
		String taskType = task.getTaskType();
		//设置任务的办理人员
		List<TaskExecutor> listTaskExecutors = task.getListTaskExecutor();
		if(null != listTaskExecutors && !listTaskExecutors.isEmpty()){//设置有办理人员
			//设置任务状态（认领的任务需要）
			Integer state = (StringUtils.isEmpty(taskType) || taskType.equals(ConstantInterface.TASK_TYPE_SURE))?1:0;
			if(listTaskExecutors.size()==1){//自动认领
				state = 1;
			}
			//修改任务类型
			Task updateTaskObj = new Task();
			updateTaskObj.setId(task.getId());
			updateTaskObj.setState(state);
			taskDao.update(updateTaskObj);
			
			String version = task.getVersion();
			if(StringUtils.isNotEmpty(version) && "2".equals(version) && state == 1){
				state = Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_PAUSE);
			}
			for (TaskExecutor taskExecutor : listTaskExecutors) {
				//设置团队信息
				taskExecutor.setComId(task.getComId());
				//设置任务信息
				taskExecutor.setTaskId(taskId);
				//设置推送人员
				taskExecutor.setPushUser(sessionUser.getId());
				//设置任务进度
				taskExecutor.setTaskProgress(0);
				//设置办理时限
				taskExecutor.setHandTimeLimit(task.getDealTimeLimit());
				
				taskExecutor.setState(state);
				
				//仅用于任务2.0开始
				taskExecutor.setStepTag(task.getStepTag());
				taskExecutor.setExpectTime(task.getExpectTime());
				//仅用于任务2.0结束
				
				//添加办理人员
				taskDao.add(taskExecutor);
				
				/************添加移交记录信息*******************/
				//添加协办记录
				TaskHandOver taskHandOver = new TaskHandOver();
				//设置企业号
				taskHandOver.setComId(task.getComId());
				//设置任务主键
				taskHandOver.setTaskId(taskId);
				//任务移交
				taskHandOver.setFromUser(sessionUser.getId());
				//任务接收
				taskHandOver.setToUser(taskExecutor.getExecutor());
				//设置办理时限
				taskHandOver.setHandTimeLimit(task.getDealTimeLimit());
				//是否当前步骤节点
				taskHandOver.setCurStep(1);
				//设置前一步骤
				taskHandOver.setPreStep(preStep);
				//设置为实际办理人员
				taskHandOver.setActHandleState(actHandleState);
				
				//仅用于任务2.0开始
				taskHandOver.setStepTag(task.getStepTag());
				taskHandOver.setExpectTime(task.getExpectTime());
				//仅用于任务2.0结束
				
				taskDao.add(taskHandOver);
			}
		}
		
	}
	/**
	 * 批量删除任务
	 * @param ids 任务主键数组
	 * @param userInfo 企业主键
	 * @throws Exception 
	 */
	public void delTask(List<Integer> ids,UserInfo userInfo) throws Exception{
		for(Integer id : ids){
			//删除任务数据前，先更新其关联模块索引数据
			Task task = (Task) taskDao.objectQuery(Task.class, id);
			if(null==task){//任务不存在
				continue;
			}
			//更新任务索引
			this.updateTaskIndex(task.getId(), userInfo,"del");
			//添加系统日志记录 
			systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_TASK, id,
					"删除任务\""+task.getTaskName()+"\"", "删除任务\""+task.getTaskName()+"\"");
			
			//删除任务执行人
			taskDao.delByField("taskExecutor", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),id});
			//删除分享人
			taskDao.delByField("taskSharer", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),id});
			//删除附件
			taskDao.delByField("taskUpfile", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),id});
			//删除任务留言附件
			taskDao.delByField("taskTalkUpfile", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),id});
			//删除任务留言
			taskDao.delByField("taskTalk", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),id});
			//删除任务日志
			taskDao.delByField("taskLog", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),id});
			
			//若是关联的项目，则将项目阶段数据删除
			if(task.getBusType().equals(ConstantInterface.TYPE_ITEM)){
				//删除项目任务关联表
				List<StagedInfo> listStagedInfo = itemService.listStagedInfoBymoduleIdAndType(userInfo.getComId(),id,"task");
				if(null!=listStagedInfo){
					Task taskVo = (Task) taskDao.objectQuery(Task.class, id);
					for(StagedInfo vo:listStagedInfo){
						//项目模块日志添加
						itemService.addItemLog(vo.getComId(),vo.getItemId(),userInfo.getId(), userInfo.getUserName(),"删除关联任务\""+taskVo.getTaskName()+"\"");
					}
				}
				taskDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"}, new Object[]{userInfo.getComId(),id,"task"});
			}
			//删除任务协办记录
			taskDao.delByField("taskHandOver", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),id});
			//删除延迟信息
			taskDao.delByField("delayApply", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),id});
			//删除浏览记录
			taskDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),id,ConstantInterface.TYPE_TASK});
			//关注信息
			taskDao.delByField("attention", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),id,ConstantInterface.TYPE_TASK});
			//最新动态
			taskDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),id,ConstantInterface.TYPE_TASK});
			//删除实际执行时间
			taskDao.delByField("taskExecuteTime", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),id});
			
			//无效状态不积分
			if(null!=task.getState() && -1!=task.getState()){
				//修改积分
				jifenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_TASKDEL,
						"删除任务:"+task.getTaskName(),id);
			}
			//更新父任务索引
//			if(null!=task.getParentId() && task.getParentId() >0){
////				this.updateTaskIndex(task.getParentId(), userInfo,"update");
//			}
//			Integer busId = task.getBusId();
//			String busType = task.getBusType();
			//更新关联项目索引
//			if(null!=busId && busId>0){
//				if(busType.equals(BusinessTypeConstant.type_item)){
//					itemService.updateItemIndex(busId, userInfo,"update");
//				}
//			}
			
			//删除任务基本信息
			taskDao.delById(Task.class, id);
		}
	}
	/**
	 * 批量删除任务 (在项目阶段明细也有删除任务)
	 * @param ids 任务主键数组
	 * @param userInfo 企业主键
	 * @throws Exception 
	 */
	public void delPreTask(Integer[] ids,UserInfo userInfo) throws Exception{
		if(null!=ids && ids.length>0){
			for(Integer id : ids){
				Task task = (Task) taskDao.objectQuery(Task.class, id);
				//预删除标识
				task.setDelState(1);
				//修改任务信息
				taskDao.update(task);
				
				//删除数据更新记录
				taskDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_TASK,id});
				//删除回收箱数据
				taskDao.delByField("recycleBin", new String[]{"comId","busType","busId","userId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_TASK,id,userInfo.getId()});
				
				//取得所有设置了提醒的闹铃(删除所有的)
				clockService.delClockByType(userInfo.getComId(),id,ConstantInterface.TYPE_TASK);
				
				//该删除任务的子任务集合
				List<Task> sonTasks = taskDao.listTaskOfAllOnlyChildren(id, userInfo.getComId());
				if(null!=sonTasks && sonTasks.size()>0){
					for (Task sonTask : sonTasks) {
						//将子任务向上提一级
						sonTask.setParentId(task.getParentId());
						taskDao.update(sonTask);
					}
				}
				//回收箱
				RecycleBin recyleBin =  new RecycleBin();
				//业务主键
				recyleBin.setBusId(id);
				//业务类型
				recyleBin.setBusType(ConstantInterface.TYPE_TASK);
				//企业号
				recyleBin.setComId(userInfo.getComId());
				//创建人
				recyleBin.setUserId(userInfo.getId());
				taskDao.add(recyleBin);
			}
		}
	}
	/**
	 * 根据主键获取任务详情(需要任务的参与人)
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public Task getTask(Integer id,UserInfo userInfo){
		
		Task task = taskDao.queryTaskById(id,userInfo);
		if(null==task){return null;}
		//任务办理人集合
		List<TaskExecutor> listTaskExecutor = this.listTaskExecutor(id,userInfo);
		task.setListTaskExecutor(listTaskExecutor);
		//任务分享人集合
		List<TaskSharer> listTaskSharer = taskDao.listTaskSharer(id, userInfo.getComId());
		task.setListTaskSharer(listTaskSharer);
		//子任务集合
		List<Task> listSonTask = taskDao.listSonTask(id, userInfo.getComId());
		if(null!=listSonTask && !listSonTask.isEmpty()){
			for (Task sonTask : listSonTask) {
				List<TaskExecutor> sonTaskExecutors = taskDao.listTaskExecutor(sonTask.getId(), userInfo.getComId());
				sonTask.setListTaskExecutor(sonTaskExecutors);
			}
		}
		
		task.setListSonTask(listSonTask);
		task.setSonTaskNum(listSonTask.size());
		
		//生成任务参与人JSon字符串
		StringBuffer taskSharerJson = null;
		if(null!=listTaskSharer && !listTaskSharer.isEmpty()){
			taskSharerJson = new StringBuffer("[");
			for(TaskSharer vo:listTaskSharer){
				taskSharerJson.append("{'userID':'"+vo.getSharerId()+"','userName':'"+vo.getSharerName()+"','gender':'"+vo.getGender()+"','uuid':'"+vo.getUuid()+"','fileName':'"+vo.getFilename()+"'},");	
			}
			taskSharerJson = new StringBuffer(taskSharerJson.substring(0,taskSharerJson.lastIndexOf(",")));
			taskSharerJson.append("]");
			task.setTaskSharerJson(taskSharerJson.toString());
		}
		//统计任务留言数
		Integer msgNum =  taskDao.countTaskMsgs(id, userInfo.getComId());
		task.setMsgNum(msgNum);
		//统计任务文档数
		Integer docNum =  taskDao.countTaskDocs(id, userInfo.getComId());
		task.setDocNum(docNum);
		//删除任务的待办提醒消息为普通消息
		delTaskTodayWorksByOwner(userInfo, task);
		return task;
	}
	/**
	 * 删除任务的待办提醒消息为普通消息
	 * @param userInfo 当前
	 * @param task
	 * @param listTaskExecutor
	 */
	private void delTaskTodayWorksByOwner(UserInfo userInfo, Task task) {
		//默认不是执行人
		boolean executeState = false;
		if(!CommonUtil.isNull(task.getListTaskExecutor())){
			for (TaskExecutor taskExecutor : task.getListTaskExecutor()) {
				if(taskExecutor.getExecutor().equals(userInfo.getId())){
					//是执行人
					executeState = true;
					break;
				}
			}
		}
		//不是执行人，则将待办数据移除
		if(!executeState){
			//todayWorksService.updateTodayWorksBusSpecTo0(ConstantInterface.TYPE_TASK, task.getId(), userInfo.getId());
			todayWorksService.delTodayWorksByOwner(userInfo.getComId(),userInfo.getId(),
					task.getId(),ConstantInterface.TYPE_TASK);
		}
	}
	/**
	 * 取得任务的执行人
	 * @param taskId 任务主键
 	 * @param sessioUser 当前操作人
	 * @return java.util.List<com.westar.base.model.UserInfo>
	 * @author Administrator
	 * @date 2018/6/20 0020 13:41
	 */
	public List<UserInfo> listExecutorsForCopy(Integer taskId,UserInfo sessioUser){
		//获取执行人
		List<TaskExecutor> listTaskExecutor = this.listTaskExecutor(taskId, sessioUser);
		Integer[] ids = new Integer[listTaskExecutor.size()];
		for(int i=0;i<listTaskExecutor.size();i++){
			ids[i] = listTaskExecutor.get(i).getExecutor();
		}
		//任务办理人信息
		List<UserInfo> listExecutors = taskDao.listExecutorsForCopy(ids);
		return listExecutors;
	}

	/**
	 * 取得任务的执行人信息，用于复制任务显示执行人
	 * @param taskId 任务主键
	 * @param sessioUser 当前操作员
	 * @return
	 */
	public List<TaskExecutor> listTaskExecutor(Integer taskId,UserInfo sessioUser){
		//任务办理人集合
		List<TaskExecutor> listTaskExecutor = taskDao.listTaskExecutor(taskId, sessioUser.getComId());
		return listTaskExecutor;
	}

	/**
	 * 验证当前操作人对此任务是否有查看权限
	 * @param comId
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public boolean authorCheck(UserInfo userInfo,Integer taskId,Integer clockId){
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
		if(isForceIn){
			return true;
		}else{
			List<Task> listTask = taskDao.authorCheck(userInfo.getComId(),taskId,userInfo.getId());
			if(null!=listTask && !listTask.isEmpty()){
				return true;
			}else{
				//查看验证，删除消息提醒
				todayWorksService.updateTodoWorkRead(taskId,userInfo.getComId(), 
						userInfo.getId(), ConstantInterface.TYPE_TASK,clockId);
				return false;
			}
		}
	}
	/**
	 * 汇报任务进度
	 * @param task
	 * @return
	 */
	public Map<String, Object> updateTaskProgress(Task task,UserInfo userInfo){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Task taskTemp = (Task) taskDao.objectQuery(Task.class, task.getId());
		//本次汇报进度
		Integer subProgress = task.getTaskProgress();
		subProgress = subProgress == null?0:subProgress;
		try {
			String taskprogressDescribe = subProgress +"%";
			//任务总的进度信息
			Integer taskProgressTotlal = subProgress;
			//任务的执行人员主键
			Integer userId = userInfo.getId();
			//设置修改任务的进度信息
			List<TaskExecutor> taskExecutors = this.listTaskExecutor(task.getId(), userInfo);
			//多人任务默认已办结
			boolean taskSubDoneState = true;
			if(null != taskExecutors && !taskExecutors.isEmpty()){
				
				//没有办结的数目
				Integer todoNum = 0;
				TaskExecutor selfExector = null;
				for (TaskExecutor taskExecutor : taskExecutors) {
					Integer executor = taskExecutor.getExecutor();
					if(executor.equals(userId)){//当前人员只执行人
						selfExector = taskExecutor;
						if(subProgress != 100){
							//没有办结
							taskSubDoneState = false;
						}
						continue;
					}
					
					//总进度
					taskProgressTotlal += null == taskExecutor.getTaskProgress()?0: taskExecutor.getTaskProgress();
					
					//多人任务其他人员没有办结
					if(!taskExecutor.getState().equals(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DONE))){//没有办结
						taskSubDoneState = false;
						todoNum++;
					}
				}
				if(todoNum>0){//有其他人员没有办结自己和办结
					selfExector.setTaskProgress(subProgress);
					if(subProgress == 100){
						selfExector.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DONE));
						if(taskTemp.getOwner().equals(userId)){
							TaskHandOver taskHandOver = taskDao.queryCurUserHandInfo(userInfo, task.getId());
							taskHandOver.setCurStep(-1);
							taskHandOver.setEndTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
							taskDao.update(taskHandOver);
						}
					}
					taskDao.update(selfExector);
				}else{
					selfExector.setTaskProgress(subProgress);
					taskDao.update(selfExector);
				}
				
				
				//任务总的进度信息
				Integer taskProgress = taskProgressTotlal / taskExecutors.size();
				task.setTaskProgress(taskProgress);
				//更新任务进度
				taskDao.taskProgressReport(task);
				
				//重新设置任务进度描述
				taskprogressDescribe = taskProgress + "%";
			}
			
			if(taskSubDoneState){//多人任务全部已办结
				//任务办结
				map.put("status", "y");
				map.put("info", "汇报为\""+subProgress+"%\"成功");
				//需要通知办结
				map.put("confirm", "y");
			}else{
				//任务的负责人，执行人以及关注人员
				List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
				//更新待办提醒通知
				todayWorksService.addTodayWorks(userInfo,task.getOwner(), task.getId(), "汇报任务进度为:"+taskprogressDescribe, ConstantInterface.TYPE_TASK, shares,null);
				//模块日志添加
				this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "汇报进度为\""+taskprogressDescribe+"\"成功");
				
				map.put("status", "y");
				map.put("info", "汇报为\""+subProgress+"%\"成功");
				//不需要通知办结
				map.put("confirm", "f");
			}
		} catch (Exception e) {
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "汇报为\""+subProgress +"%\"失败");
			
			map.put("status", "f");
			map.put("info", "汇报为\""+subProgress+"%\"失败");
		}
		return map;
	}
	/**
	 * 任务的重要紧急性
	 * @param task 任务
	 * @param userInfo 操作员
	 * @param gradeName 重要紧急性名称
	 * @return
	 * @throws Exception
	 */
	public boolean updateTaskGrade(Task task,UserInfo userInfo, String gradeName){
		boolean succ = true;
		try {
			//更新任务的重要紧急性
			taskDao.update(task);
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//更新待办提醒通知
			todayWorksService.addTodayWorks(userInfo,task.getExecutor(), task.getId(), "任务的重要紧急性更新为:"+gradeName, ConstantInterface.TYPE_TASK, shares,null);
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
			
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务的重要紧急性更新为\""+gradeName+"\"成功");
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务的重要紧急性更新为\""+gradeName+"\"失败");
			
		}
		return succ;
	}
	/**
	 * 完成时限更新
	 * @param task
	 * @return
	 */
	public boolean updateTaskDealTimeLimit(Task task,UserInfo userInfo){
		boolean succ = true;
		try {
			//用于查询任务的执行人
			Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
			String version = taskT.getVersion(); 
			if(StringUtils.isNotEmpty(version) && version.equals("2")){
				//完成时限更新
				taskDao.updateExpectTime(task);
			}else{
				//完成时限更新
				taskDao.updateDealTimeLimit(task);
			}
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), task.getId(), "变更任务时限为\""+task.getDealTimeLimit()+"\"小时", ConstantInterface.TYPE_TASK, shares,null);
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "变更任务时限为\""+task.getDealTimeLimit()+"小时\"小时成功");
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "变更任务时限为\""+task.getDealTimeLimit()+"小时\"失败");
			
		}
		return succ;
	}
	/**
	 * 完成时限更新
	 * @param task
	 * @return
	 */
	public boolean updateTaskExpectTime(Task task,UserInfo userInfo){
		boolean succ = true;
		try {
			//用于查询任务的执行人
			Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
			String version = taskT.getVersion(); 
			if(StringUtils.isNotEmpty(version) && version.equals("2")){
				//完成时限更新
				taskDao.updateExpectTime(task);
			}else{
				//完成时限更新
				taskDao.updateDealTimeLimit(task);
			}
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), task.getId(), "变更任务时限为\""+task.getExpectTime()+"\"", ConstantInterface.TYPE_TASK, shares,null);
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "变更任务时限为\""+task.getExpectTime()+"\"成功");
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "变更任务时限为\""+task.getExpectTime()+"\"失败");
			
		}
		return succ;
	}
	/**
	 * 任务名称变更
	 * @param task
	 * @return
	 */
	public boolean updateTaskName(Task task,UserInfo userInfo){
		boolean succ = true;
		try {
			//任务名称变更
			taskDao.taskNameUpdate(task);
			//用于查询任务的执行人
			Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), task.getId(), "任务名称变更为\""+task.getTaskName()+"\"", ConstantInterface.TYPE_TASK, shares,null);
			//更新任务索引
			this.updateTaskIndex(task.getId(),userInfo,"update");
			
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务名称变更为\""+task.getTaskName()+"\"成功");
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务名称变更为\""+task.getTaskName()+"\"失败");
		}
		return succ;
	}
	/**
	 * 任务说明更新
	 * @param task
	 * @return
	 */
	public boolean updateTaskTaskRemark(Task task,UserInfo userInfo){
		boolean succ = true;
		try {
			//更新任务进度
			taskDao.taskTaskRemarkUpdate(task);
			//用于查询任务的执行人
			Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), task.getId(), "变更任务说明:"+task.getTaskRemark(), ConstantInterface.TYPE_TASK, shares,null);
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务说明变更成功");
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务说明变更失败");
		}
		return succ;
	}
	/**
	 * 任务母任务关联
	 * 	若母任务有关联模块，则统一修改项目关联；若没有关联模块，则不处理项目关联
	 * @param task 需要关联任务的任务
	 * @param parentTasks 本次设置的父节点集合
	 * @param ptask 父任务一定不为空
	 * @return
	 */
	public boolean updateTaskParentId(Task task,UserInfo userInfo, List<Task> parentTasks,
			Task parentTask){
		boolean succ = true;
		try {
			//用于查询任务的执行人
			Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
			//设置父任务
			taskT.setParentId(task.getParentId());
			//首先重启父任务,包括任务自身
			if(null!=parentTasks && parentTasks.size()>0){
				for (Task ptask : parentTasks) {
					if(ptask.getState()==4){//任务是完成了的,需要重启
						ptask.setState(1);
						taskDao.update(ptask);
						
						if(ptask.getDelState()==0){//任务没有预删除,需要发送消息
							//任务的负责人，执行人以及关注人员
							List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), ptask.getId(),true);
							//添加待办提醒通知
							todayWorksService.addTodayWorks(userInfo,ptask.getExecutor(), ptask.getId(), 
									"被子任务“"+taskT.getTaskName()+"”关联,重启任务", ConstantInterface.TYPE_TASK, shares,null);
						}
					}
					
				}
			}
			
			//任务母任务关联
			taskDao.taskParentIdUpdate(task);
			
			//父任务对应的模块主键
			Integer pBusId = 0;
			//父任务对应的模块类型
			String pBusType = "0";
			//父任务的模块存在并且父任务模块没被删除
			if(null!=parentTask && null != parentTask.getBusDelState()&& parentTask.getBusDelState()==0){
				//父任务对应的模块主键
				pBusId = parentTask.getBusId();
				//父任务对应的模块类型
				pBusType = parentTask.getBusType();
			}
			//任务对应的模块主键
			Integer busId = taskT.getBusId();
			if(null!=pBusId && pBusId>0){//父任务有关联模块，且父任务的关联模块没有被删除
				//统一到父任务中
				task.setBusId(pBusId);
				task.setBusType(pBusType);
				//任务的项目关联
				taskDao.taskBusIdUpdate(task);
				
				if(pBusType.equals(ConstantInterface.TYPE_ITEM)){//关联的是项目
					if(null!=busId && busId>0 && pBusId.equals(busId)){//父子任务的关联的项目相同
						//修改自己和子任务对应的项目阶段
						this.updateStageInfo(userInfo,taskT,busId,pBusType,"self",
								"任务"+taskT.getTaskName()+"设置父任务时，添加关联项目");
					}else{//父子任务的关联的项目不同
						//修改自己和子任务对应的项目阶段
						this.updateStageInfo(userInfo,taskT,pBusId,pBusType,"parent",
								"上级任务"+taskT.getTaskName()+"设置父任务时，添加关联项目");
						
						if(null!=busId && busId>0 && taskT.getBusType().equals(ConstantInterface.TYPE_ITEM)){
							//添加项目日志
							itemService.addItemLog(userInfo.getComId(), busId, userInfo.getId(),
									userInfo.getUserName(), "任务"+taskT.getTaskName()+"设置父任务时，删除关联项目");
						}
						//添加项目日志
						itemService.addItemLog(userInfo.getComId(), pBusId, userInfo.getId(),
								userInfo.getUserName(), "任务"+taskT.getTaskName()+"设置父任务时，添加关联项目");
					}
				}else{//父任务关联的是其他模块
					//删除自己和子任务对应的项目阶段
					this.updateStageInfo(userInfo,taskT,pBusId,pBusType,null,null);
				}
			}
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), task.getId(), "母任务关联为\""+parentTask.getTaskName()+"\"", ConstantInterface.TYPE_TASK, shares,null);
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
			
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "母任务关联为\""+parentTask.getTaskName()+"\"成功");
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "母任务关联为\""+parentTask.getTaskName()+"\"失败");
			
		}
		return succ;
	}
	
	/**
	 * 删除任务的项目阶段关联
	 * @param userInfo 当前操作人员
	 * @param taskT 当前任务
	 * @throws Exception 
	 */
	public void delStageTask(UserInfo userInfo, Integer taskId) throws Exception{
		//取得当前任务的所有子任务
		List<Task> sonTasks = taskDao.listTaskOfAllOnlyChildren(taskId, userInfo.getComId());
		if(null!=sonTasks && sonTasks.size()>0){//存在子任务
			for (Task objTask : sonTasks) {
				//删除子任务的项目阶段关联
				taskDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"}, new Object[]{userInfo.getComId(),objTask.getId(),"task"});
			}
		}
		//删除任务的项目阶段关联
		taskDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"}, new Object[]{userInfo.getComId(), taskId,"task"});
		//更新任务索引
//		this.updateTaskIndex(taskId,userInfo,"update");
	}
	/**
	 * 修改任务自己或是子任务对应的项目阶段明细；设置子任务关联项目，项目添加子任务关联项目的日志
	 * @param userInfo 操作人员
	 * @param task 任务
	 * @param busId 项目主键
	 * @param type 项目阶段主键来源 parent父任务的阶段主键 self任务本身所在的阶段主键
	 * @throws Exception 
	 */
	public void updateStageInfo(UserInfo userInfo, Task task, Integer busId,String busType,String type,String content) {
		if(busType.equals(ConstantInterface.TYPE_ITEM)){//关联是项目
			//项目阶段主键
			Integer stagedItemId = null;
			//阶段明细
			StagedInfo stagedInfo = null;
			if("parent".equals(type)){//统一到父任务所在的项目阶段明细
				stagedInfo = itemService.getStagedRelateInfo(userInfo.getComId(), task.getParentId(),ConstantInterface.STAGED_TASK);
			}else if("self".equals(type)){//统一到任务自身所在的项目阶段明细
				stagedInfo = itemService.getStagedRelateInfo(userInfo.getComId(), task.getId(),ConstantInterface.STAGED_TASK);
			}
			if(null!=stagedInfo){//已关联项目阶段明细
				//项目阶段主键
				stagedItemId = stagedInfo.getStagedItemId();
				if("parent".equals(type)){//统一到父任务所在的项目阶段明细,则需要判断自身是否也在统一个项目中
					//判断任务本身在项目阶段明细
					StagedInfo sonStagedInfo = itemService.getStagedRelateInfo(userInfo.getComId(), task.getId(),ConstantInterface.STAGED_TASK);
					/**
					 * 说明
					 * 	1、自身已有项目阶段明细， 父任务与自身关联的不是同一个项目，则修改自身关联的项目阶段明细
					 * 	2、自身没有项目阶段明细， 则添加数据
					 */
					if(null!=sonStagedInfo){//项目阶段明细中已有自身信息
						if(!sonStagedInfo.getItemId().equals(stagedInfo.getItemId())){//父任务与自身在不同的项目中
							//重新设置项目主键
							sonStagedInfo.setItemId(stagedInfo.getItemId());
							//重新设置阶段明细主键
							sonStagedInfo.setStagedItemId(stagedItemId);
							//已在调用此方法前添加了任务自身的日志， 此处不用添加项目日志
							taskDao.update(sonStagedInfo);
						}
					}else{//项目阶段明细中没有自身信息
						//项目阶段添加关联任务
						itemService.addStageRelateMod(userInfo, busId, stagedItemId, task.getId(),ConstantInterface.TYPE_TASK);
					}
				}else if("self".equals(type)){//stagedInfo已判断不为空，任务已关联项目模块
					//判断任务本身在项目阶段明细
					if(!busId.equals(stagedInfo.getItemId())){//设定的不是原来的项目。项目不同，则项目阶段也不同
						stagedItemId = itemService.getStageItemId(userInfo, busId);
						//重新设置项目主键
						stagedInfo.setItemId(busId);
						//重新设置阶段明细主键
						stagedInfo.setStagedItemId(stagedItemId);
						
						taskDao.update(stagedInfo);
					}
					
				}
			}else{//没有关联项目阶段明细则直接添加数据
				//取得最近
				stagedItemId = itemService.getStageItemId(userInfo, busId);
				//项目阶段添加关联任务
				itemService.addStageRelateMod(userInfo, busId, stagedItemId, task.getId(),ConstantInterface.TYPE_TASK);
			}
			//取得当前任务的所有子任务
			List<Task> sonTasks = taskDao.listTaskOfAllOnlyChildren(task.getId(), userInfo.getComId());
			if(null!=sonTasks && sonTasks.size()>0){//存在子任务
				for (Task sonTask : sonTasks) {
					//修改子任务的关联项目
					sonTask.setBusId(busId);
					sonTask.setBusType(busType);
					taskDao.update(sonTask);
					
					itemService.addItemLog(userInfo.getComId(), busId, userInfo.getId(),
							userInfo.getUserName(), content);
					
					//判断项目阶段明细中是否有子任务了
					StagedInfo sonTaskStagedInfo = itemService.getStagedRelateInfo(userInfo.getComId(), sonTask.getId(),ConstantInterface.STAGED_TASK);
					//没有则添加
					if(null==sonTaskStagedInfo){
						//项目阶段添加关联任务
						itemService.addStageRelateMod(userInfo, busId, stagedItemId, sonTask.getId(),ConstantInterface.TYPE_TASK);
					}else{//有就修改
						if(!sonTaskStagedInfo.getItemId().equals(busId)){//不在同一个项目中
							//对应的项目主键
							sonTaskStagedInfo.setItemId(busId);
							//取得对应阶段主键
							sonTaskStagedInfo.setStagedItemId(stagedItemId);
							taskDao.update(sonTaskStagedInfo);
						}
					}
				}
			}
		}else{//关联的其他模块，删除项目阶段任务
			if(busId>0){//有关联的模块，非项目
				//取得当前任务的所有子任务
				List<Task> sonTasks = taskDao.listTaskOfAllOnlyChildren(task.getId(), userInfo.getComId());
				if(null!=sonTasks && sonTasks.size()>0){//存在子任务
					for (Task objTask : sonTasks) {
						//删除子任务的项目阶段关联
						taskDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"}, new Object[]{userInfo.getComId(),objTask.getId(),"task"});
						//设置子任务的业务主键
						objTask.setBusId(busId);
						//设置业务类型
						objTask.setBusType(busType);
						//修改数据
						taskDao.update(objTask);
					}
				}
			}
			//删除任务的项目阶段关联
			taskDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"}, new Object[]{userInfo.getComId(), task.getId(),"task"});
		}
	}
	/**
	 * 删除关联任务关联字段
	 * @param task
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean delpTaskRelation(Task task,UserInfo userInfo){
		boolean succ = true;
		Task pTask = this.getTaskById(task.getParentId());
		task.setpTaskName(pTask.getTaskName());
		
		try {
			//清除关联任务字段
			task.setParentId(-1);
			//任务母任务关联
			taskDao.taskParentIdUpdate(task);
			//用于查询任务的执行人
			Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), task.getId(), "解除与任务\""+task.getpTaskName()+"\"间的关联关系", ConstantInterface.TYPE_TASK, shares,null);
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "母任务关联为\""+pTask.getTaskName()+"\"成功");
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "母任务关联为\""+pTask.getTaskName()+"\"失败");
		}
		return succ;
	}
	/**
	 * 项目关联
	 * @param task
	 * @return
	 */
	public boolean updateTaskBusId(Task task,UserInfo userInfo){
		boolean succ = true;
		//操作成功的日志
		String logSContent = "删除模块关联称成功！";
		//操作失败的日志
		String logEContent = "删除模块关联称失败！";
		if(task.getBusType().equals(ConstantInterface.TYPE_ITEM)){
			Item item = itemService.getItemById(task.getBusId());
			task.setBusName(item.getItemName());
			
			logSContent = "关联项目\""+item.getItemName()+"\"成功";
			logEContent = "关联项目\""+item.getItemName()+"\"失败";
		}else if(task.getBusType().equals(ConstantInterface.TYPE_CRM)){
			Customer crm = crmService.getCrmById(task.getBusId());
			task.setBusName(crm.getCustomerName());
			
			logSContent = "关联客户\""+crm.getCustomerName()+"\"成功";
			logEContent = "关联客户\""+crm.getCustomerName()+"\"失败";
		}
				
		try {
			//业务主键
			Integer busId = task.getBusId();
			//业务类型
			String busType = task.getBusType();
			if(busType.equals(ConstantInterface.TYPE_ITEM)){
				//项目关联
				taskDao.taskBusIdUpdate(task);
				//级联修改子任务的关联项目
				this.updateStageInfo(userInfo, task, busId,busType, "self","任务添加关联项目");
				//添加项目日志
				itemService.addItemLog(userInfo.getComId(), busId, userInfo.getId(), 
						userInfo.getUserName(), "任务添加关联项目");
				
				//用于查询任务的执行人
				Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
				//任务的负责人，执行人以及关注人员
				List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), task.getId(), "关联项目\""+task.getBusName()+"\"", ConstantInterface.TYPE_TASK, shares,null);
				
				//模块日志添加
				itemService.addItemLog(userInfo.getComId(),busId,userInfo.getId(), userInfo.getUserName(),"关联任务\""+taskT.getTaskName()+"\"");
				//更新项目索引
//				itemService.updateItemIndex(busId,userInfo,"update");
				//更新任务索引
//				this.updateTaskIndex(task.getId(),userInfo,"update");
			}else {//关联其他模块
				//修改任务关联
				taskDao.taskBusIdUpdate(task);
				//级联删除关联项目
				this.updateStageInfo(userInfo, task, busId,busType, null,null);
			}
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(),logSContent);
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(),logEContent);
			
		}
		return succ;
	}
	/**
	 * 删除项目关联
	 * @param task
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean delTaskBusRelation(Task task,UserInfo userInfo){
		boolean succ = true;
		//操作成功的日志
		String logSContent = "删除模块关联成功！";
		//操作失败的日志
		String logEContent = "删除模块关联失败！";
		
		if(task.getBusType().equals(ConstantInterface.TYPE_ITEM)){
			Item item = itemService.getItemById(task.getBusId());
			task.setBusName(item.getItemName());
		}else if(task.getBusType().equals(ConstantInterface.TYPE_CRM)){
			Customer crm  = crmService.getCrmById(task.getBusId());
			task.setBusName(crm.getCustomerName());
		}
		
		try {
			Task taskVo = (Task) taskDao.objectQuery(Task.class, task.getId());
			//把需要清零的字段用临时变量itemId存储起来
//			Integer busId = taskVo.getBusId();
			String busType = taskVo.getBusType();
			
			//项目关联字段更新为0
			task.setBusId(0);
			//重新设置关联类型
			task.setBusType("0");
			//项目关联
			taskDao.taskBusIdUpdate(task);
			
			if(busType.equals(ConstantInterface.TYPE_ITEM)){//关联是项目
				//删除任务在项目阶段明细
				List<StagedInfo> listStagedInfo = itemService.listStagedInfoBymoduleIdAndType(task.getComId(),task.getId(),"task");
				if(null!=listStagedInfo){
					for(StagedInfo vo:listStagedInfo){
						taskDao.delByField("stagedInfo", new String[]{"comId","id"}, new Object[]{vo.getComId(),vo.getId()});
						//模块日志添加
						itemService.addItemLog(vo.getComId(),vo.getItemId(),userInfo.getId(), userInfo.getUserName(),"解除与任务\""+taskVo.getTaskName()+"\"的关联关系");
					}
				}
				//更新项目索引
//				itemService.updateItemIndex(busId,userInfo,"update");
			}
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			
			String modName = "";
			if(busType.equals(ConstantInterface.TYPE_ITEM)){
				modName = "项目";
			}else if(busType.equals(ConstantInterface.TYPE_CRM)){
				modName = "客户";
			}
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,taskVo.getExecutor(), task.getId(), "解除与"+modName+"\""+task.getBusName()+"\"的关联关系", ConstantInterface.TYPE_TASK, shares,null);
			
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
			
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), logSContent);
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), logEContent);
			
		}
		return succ;
	}
	/**
	 * 任务负责人更新
	 * @param task
	 * @return
	 */
	public boolean updateTaskOwner(Task task,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			Task obj = (Task) taskDao.objectQuery(Task.class, task.getId());
			//任务分享人
			TaskSharer taskSharer = taskDao.getTaskSharer4Owner(task.getId(),task.getComId(),obj.getOwner());
			
			//任务负责人更新
			taskDao.taskOwnerUpdate(task);
			//获取任务参与人集合
			List<TaskSharer> listTaskSharer = taskDao.listTaskSharer(task.getId(),task.getComId());
			Map<Integer,Integer> sharerMap = new HashMap<Integer, Integer>();
			//把当前操作人添加为任务参与人
			sharerMap.put(userInfo.getId(),userInfo.getComId());
			//把下一步执行人添加到参与人
			sharerMap.put(task.getOwner(),userInfo.getComId());
			//过滤已有的参与人
			if(null!=listTaskSharer && !listTaskSharer.isEmpty()){
				for(TaskSharer vo: listTaskSharer){
					sharerMap.put(vo.getSharerId(),userInfo.getComId());
				}
			}
			//更新任务参与人
			//先删除任务的参与人
			taskDao.delByField("taskSharer", new String[]{"comId","taskId"},new Integer[]{userInfo.getComId(),task.getId()});
			for(Map.Entry<Integer,Integer> entry:sharerMap.entrySet()){    
			    taskSharer = new TaskSharer();
				taskSharer.setComId(userInfo.getComId());
				taskSharer.setTaskId(task.getId());
				taskSharer.setSharerId(entry.getKey());
				taskDao.add(taskSharer);    
			}

			//用于查询任务的执行人
			Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), task.getId(), "任务负责人变更为\""+task.getOwnerName()+"\"",
					ConstantInterface.TYPE_TASK, shares,null);
			
			//添加工作轨迹
			systemLogService.addSystemLogWithTrace(userInfo, taskT.getExecutor(), ConstantInterface.TYPE_TASK, task.getId(),
					"任务\""+taskT.getTaskName()+"\"的负责人变更为\""+task.getOwnerName()+"\"", "负责任务\""+taskT.getTaskName()+"\"");
			
			//更新任务的所有后代参与人--此任务需放在更新方法最后执行
			this.updateTaskSharerForChildren(task.getId(),userInfo.getComId());
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 任务执行人更新（暂时没有使用）
	 * @param task
	 * @return
	 */
	public boolean updateTaskExecutor(Task task,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//执行人在分享人中，修改后还是有，不需要修改附件管理的附件关联
			//若是需要使用，并将原来的执行者删除，需要修改关联附件管理
			//任务执行人更新
			taskDao.update("update task a set a.executor=:executor where a.comid=:comId and a.id=:id", task);
			//获取任务参与人集合
			List<TaskSharer> listTaskSharer = taskDao.listTaskSharer(task.getId(),task.getComId());
			Map<Integer,Integer> sharerMap = new HashMap<Integer, Integer>();
			//把当前操作人添加为任务参与人
			sharerMap.put(userInfo.getId(),userInfo.getComId());
			//把下一步执行人添加到参与人
			sharerMap.put(task.getExecutor(),userInfo.getComId());
			//过滤已有的参与人信心
			if(null!=listTaskSharer && !listTaskSharer.isEmpty()){
				for(TaskSharer vo: listTaskSharer){
					sharerMap.put(vo.getSharerId(),userInfo.getComId());
				}
			}
			//更新任务参与人
			//先删除任务的参与人
			taskDao.delByField("taskSharer", new String[]{"comId","taskId"},new Integer[]{userInfo.getComId(),task.getId()});
			TaskSharer taskSharer =null;
			for(Map.Entry<Integer,Integer> entry:sharerMap.entrySet()){    
			    taskSharer = new TaskSharer();
				taskSharer.setComId(userInfo.getComId());
				taskSharer.setTaskId(task.getId());
				taskSharer.setSharerId(entry.getKey());
				taskDao.add(taskSharer);    
			}

			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,task.getExecutor(), task.getId(), "任务执行人变更为\""+task.getExecutorName()+"\"", ConstantInterface.TYPE_TASK, shares,null);
			
			//更新任务的所有后代参与人--此任务需放在更新方法最后执行
			this.updateTaskSharerForChildren(task.getId(),userInfo.getComId());
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 任务参与人更新
	 * @param comId
	 * @param taskId
	 * @param userIds
	 * @param sharerName 
	 * @return
	 */
	public boolean updateTaskSharer(Integer comId,Integer taskId,Integer[] userIds,UserInfo userInfo){
		boolean succ = true;
		StringBuffer sharerName =null;
		if(null!=userIds && userIds.length>0){
			//项目负责人更新
			sharerName = new StringBuffer();
			for(Integer userId:userIds){
				UserInfo sharerInfo = userInfoService.getUserInfo(userInfo.getComId(),userId);
				sharerName.append(sharerInfo.getUserName()+",");
			}
			sharerName = new StringBuffer(sharerName.subSequence(0,sharerName.lastIndexOf(",")));
		}
		
		try {
			Task taskObj = (Task) taskDao.objectQuery(Task.class, taskId);
			//任务负责人将文件归类后需要删除的
			List<TaskSharer> taskSharers = taskDao.listRemoveTaskSharer(taskId, comId,userIds);
			for (TaskSharer taskSharer : taskSharers) {
				//分享人非执行人或是负责人
				if(taskSharer.getSharerId()!=taskObj.getExecutor() && taskSharer.getSharerId()!=taskObj.getOwner()){
					//取得所有设置了提醒的闹铃
					clockService.delClockByUserId(userInfo.getComId(), taskSharer.getSharerId(), 
							taskObj.getId(), ConstantInterface.TYPE_TASK);
				}
			}
			//先删除任务的参与人
			taskDao.delByField("taskSharer", new String[]{"comId","taskId"},new Integer[]{comId,taskId});
			if(null!=userIds && userIds.length>0){
				//任务负责人更新
				TaskSharer taskSharer =null;
				for(Integer userId:userIds){
					taskSharer = new TaskSharer();
					taskSharer.setComId(comId);
					taskSharer.setSharerId(userId);
					taskSharer.setTaskId(taskId);
					taskDao.add(taskSharer);
				}
			}

			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), taskObj.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,taskObj.getExecutor(), taskObj.getId(), "任务参与人变更为\""+sharerName+"\"", ConstantInterface.TYPE_TASK, shares,null);
			//更新任务的所有后代参与人--此任务需放在更新方法最后执行
			this.updateTaskSharerForChildren(taskId, comId);
			//更新任务索引
//			this.updateTaskIndex(taskObj.getId(),userInfo,"update");
			
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),taskId, userInfo.getId(), userInfo.getUserName(), "任务参与人变更为\""+sharerName.toString()+"\"成功");
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),taskId, userInfo.getId(), userInfo.getUserName(), "任务参与人变更为\""+sharerName.toString()+"\"失败");
		}
		return succ;
	}
	/**
	 * 删除单个任务参与人
	 * @param comId
	 * @param taskId
	 * @param userId
	 * @param shareName 
	 * @return
	 */
	public boolean delTaskSharer(Integer comId,Integer taskId,Integer userId,UserInfo userInfo, String shareName) throws Exception{
		boolean succ = true;
		try {
			//任务信息
			Task task = (Task) taskDao.objectQuery(Task.class, taskId);
			//删除人员非执行人或非负责人
			if(task.getExecutor()!=userId && task.getOwner()!=userId){
				//取得所有设置了提醒的闹铃
				clockService.delClockByUserId(userInfo.getComId(),userId, 
						task.getId(), ConstantInterface.TYPE_TASK);
			}
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,task.getExecutor(), task.getId(), "删除任务参与人\""+shareName+"\"", ConstantInterface.TYPE_TASK, shares,null);
			//删除任务的参与人
			taskDao.delByField("taskSharer", new String[]{"comId","taskId","sharerId"},new Integer[]{comId,taskId,userId});
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 任务标记完成
	 * @param task
	 * @param userInfo 
	 * @param stateName 
	 * @return
	 */
	public boolean updateFinishTask(Task task, UserInfo userInfo, String stateName){
		boolean succ = true;
		try {
			String finishDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
			//先标记子任务完成
			if(null!=task.getChildAlsoRemark() && "yes".equals(task.getChildAlsoRemark())){
				//任务的所有子任务
				List<Task> listSonTask = taskDao.listTaskOfAllOnlyChildren(task.getId(),userInfo.getComId());
				if(null!=listSonTask && !listSonTask.isEmpty()){
					for(Task sonTask:listSonTask){
						if(sonTask.getState()==4 && sonTask.getDelState()==0){//若是子任务已办结，不用发送消息
							continue;
						}
						
						//删除子任务的执行人
						taskDao.delByField("taskExecutor", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),sonTask.getId()});
						//子任务办结设置节点的办结时间
						this.updateFinishTaskHandOver(sonTask,userInfo,null);
						
						//取得所有设置了提醒的闹铃(删除所有的)
						clockService.delClockByType(userInfo.getComId(),sonTask.getId(),ConstantInterface.TYPE_TASK);
						
						//设置任务办结时间
						this.updateTaskFinishTime(finishDateTime, sonTask.getId());
						
						//任务标记
						sonTask.setState(task.getState());
						sonTask.setTaskProgress(100);//进度100%
						taskDao.remarkTaskState(sonTask);
						
						if(sonTask.getDelState()==0){//向任务成员发送消息
							//任务的负责人，执行人以及关注人员
							List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), sonTask.getId(),true);
							//子项变动发送消息
							todayWorksService.addTodayWorks(userInfo, -1, sonTask.getId(), "任务标记为‘"+stateName+"’",
									ConstantInterface.TYPE_TASK, shares, null);
						}
						//模块日志添加
						this.addTaskLog(userInfo.getComId(),sonTask.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+stateName+"\"成功");
					}
				}
			}
			
			//删除任务的执行人
			taskDao.delByField("taskExecutor", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),task.getId()});
			
			//任务办结设置节点的办结时间
			this.updateFinishTaskHandOver(task,userInfo,null);
			
			//设置任务办结时间
			this.updateTaskFinishTime(finishDateTime, task.getId());
			
			//任务标记完成
			task.setTaskProgress(100);//进度100%
			taskDao.remarkTaskState(task);
			
			//取得所有设置了提醒的闹铃(删除所有的)
			clockService.delClockByType(userInfo.getComId(),task.getId(),ConstantInterface.TYPE_TASK);
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,-1, task.getId(),"任务标记为‘"+stateName+"’", ConstantInterface.TYPE_TASK, shares,null);
			if(task.getState()==4){//结束任务加分
				Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
				//修改积分
				jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASKFINISH, "结束任务:"+taskT.getTaskName(),task.getId());
			}
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+stateName+"\"成功");
		
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+stateName+"\"失败");
		}
		return succ;
	}
	/**
	 * @param finishDateTime
	 * @param sonTask
	 * @return
	 */
	private void updateTaskFinishTime(String finishDateTime, Integer taskId) {
		if(StringUtils.isEmpty(finishDateTime)){
			//任务办结
			Task task = new Task();
			task.setId(taskId);
			task.setFinishTime(null);
			//更新task
			StringBuffer sql = new StringBuffer("update task a set finishTime=:finishTime where a.id=:id");
			taskDao.update(sql.toString(),task);
		}else{
			//任务办结
			Task task = new Task();
			task.setId(taskId);
			task.setFinishTime(finishDateTime);
			taskDao.update(task);
		}
	}
	/**
	 * 办结任务信息
	 * @param task
	 * @param userInfo
	 * @param costTime 
	 */
	private void updateFinishTaskHandOver(Task task, UserInfo userInfo, Map<Integer, String> executorCostTimeMap) {
		//最后办结的人员
		List<TaskHandOver> taskHandOvers = taskDao.listHandOverForExecute(task.getId(), userInfo.getComId(), 1);
		if(null!=taskHandOvers && !taskHandOvers.isEmpty()){
			for (TaskHandOver taskHandOver : taskHandOvers) {
				
				Integer executor = taskHandOver.getToUser();
				String costTime = executorCostTimeMap.get(executor);
				
				taskHandOver.setCurStep(-1);
				taskHandOver.setActHandleState(1);
				taskHandOver.setEndTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
				taskHandOver.setCostTime(costTime);
				taskDao.update(taskHandOver);
			}
		}
	}
	/**
	 * 任务重启
	 * @param task
	 * @param userInfo 
	 * @param stateName 
	 * @return
	 */
	public boolean updateRestarTask(Task task, UserInfo userInfo, String stateName){
		boolean succ = true;
		try {
			//先标记子任务执行
			if(null!=task.getChildAlsoRemark() && "yes".equals(task.getChildAlsoRemark())){
				//任务的所有子任务
				List<Task> listSonTask = taskDao.listTaskOfAllOnlyChildren(task.getId(),task.getComId());
				if(null!=listSonTask && !listSonTask.isEmpty()){
					for(Task sonTask:listSonTask){
						sonTask.setState(task.getState());
						//任务标记
						taskDao.remarkTaskState(sonTask);
						//删除最后的执行人员
						taskDao.delByField("TaskExecutor", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),sonTask.getId()});
						//最后任务办理人员
						List<TaskExecutor> lastExecutors = this.addTaskExectorFormHandOver(sonTask);
						
						//取消任务办结时间
						this.updateTaskFinishTime(null, sonTask.getId());
						
						//向未删除的任务成员发送消息
						if(sonTask.getDelState()==0
								&& null!=lastExecutors && !lastExecutors.isEmpty()){
							//发送待办信息
							for (TaskExecutor taskExecutor : lastExecutors) {
								todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_TASK, sonTask.getId(),"任务标记为\""+stateName+"\"成功", taskExecutor.getExecutor(), userInfo.getId(), 1);
							}
						}
						//模块日志添加
						this.addTaskLog(userInfo.getComId(),sonTask.getId(),userInfo.getId(), userInfo.getUserName(), "任务标记为\""+stateName+"\"成功");
					}
				}
			}
			//任务的所有父任务包括自己
			List<Task> listParentTask = taskDao.listTaskOfAllParent(task.getId(),userInfo.getComId());
			if(null!=listParentTask && listParentTask.size()>0){
				for (Task pTask : listParentTask) {
					if(1==pTask.getState() && pTask.getDelState()==0){//任务是开启的,没有预删除，不发送消息
						continue;
					}
					//删除最后的执行人员
					taskDao.delByField("TaskExecutor", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),pTask.getId()});
					//最后任务办理人员
					List<TaskExecutor> lastExecutors = this.addTaskExectorFormHandOver(pTask);
					
					pTask.setState(1);
					//任务标记
					taskDao.remarkTaskState(pTask);
					
					//取消任务办结时间
					this.updateTaskFinishTime(null, pTask.getId());
					
					if(pTask.getDelState()==0 && null!=lastExecutors && !lastExecutors.isEmpty()){//项目没有删除的任务成员发送消息
						//发送待办信息
						for (TaskExecutor taskExecutor : lastExecutors) {
							//父项变动发送消息
							todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_TASK, pTask.getId(), "任务标记为‘"+stateName+"’", taskExecutor.getExecutor(), userInfo.getId(), 1);
						}
					}
					//模块日志添加
					this.addTaskLog(userInfo.getComId(),pTask.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+stateName+"\"成功");
				}
			}
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+stateName+"\"失败");
		
		}
		return succ;
	}
	/**
	 * 任务重新启动恢复执行人信息
	 * @param task 任务信息
	 * @return
	 */
	public List<TaskExecutor> addTaskExectorFormHandOver(Task task) {
		List<TaskExecutor> executors = new  ArrayList<TaskExecutor>();
		//查询子任务最后的办结人
		List<TaskHandOver> lastExecutors = taskDao.listHandOverForLastExecute(task.getId(),task.getComId(),null);
		if(null!=lastExecutors && !lastExecutors.isEmpty()){
			//任务类型
			String taskType = task.getTaskType();
			//设置任务状态默认是办理
			String taskState = ConstantInterface.STATIS_TASK_STATE_DOING;
			if(taskType.equals(ConstantInterface.TASK_TYPE_CHOOSE)//认领的任务有多人办理，则需要认领
					&& lastExecutors.size()>1){
				taskState = ConstantInterface.STATIS_TASK_STATE_CHOOSE;
			}
			
			task.setState(Integer.parseInt(taskState));
			taskDao.update(task);
			
			for (TaskHandOver taskHandOver : lastExecutors) {
				String taskExecuteState = taskState;
				Integer curStep = taskHandOver.getCurStep();
				Integer totalProgess = task.getTaskProgress();
				if(curStep == -1 || curStep == 1 ){
					//重启执行节点信息
					taskHandOver.setCurStep(1);
					taskDao.update("update taskHandOver a set a.endTime=null,a.curStep=:curStep where a.id=:id", taskHandOver);
				}else{
					//任务是办结的
					taskExecuteState = ConstantInterface.STATIS_TASK_STATE_DONE;
					totalProgess = 100;
				}
				
				Integer executor = taskHandOver.getToUser();
				//添加执行人信息
				TaskExecutor taskExecutor = new TaskExecutor();
				taskExecutor.setExecutor(executor);
				//设置团队信息
				taskExecutor.setComId(task.getComId());
				//设置任务信息
				taskExecutor.setTaskId(task.getId());
				//设置任务进度
				taskExecutor.setTaskProgress(null == totalProgess ? 0 : totalProgess);
				//设置任务状态
				taskExecutor.setState(Integer.parseInt(taskExecuteState));
				//设置消息推送人员
				taskExecutor.setPushUser(taskHandOver.getFromUser());
				//设置办理时限
				taskExecutor.setHandTimeLimit(taskHandOver.getHandTimeLimit());
				//添加办理人员
				taskDao.add(taskExecutor);
				
				executors.add(taskExecutor);
			}
		}
		return executors;
	}
	/**
	 * 任务暂停
	 * @param task 任务配置信息
	 * @param userInfo 当前操作人
	 * @param state 更新状态描述
	 * @return
	 */
	public boolean updatePauseTask(Task task, UserInfo userInfo, String state){
		boolean succ = true;
		try {
			//任务的所有子任务
			List<Task> listSonTask = taskDao.listTaskOfAllOnlyChildren(task.getId(),task.getComId());
			if(null!=listSonTask && !listSonTask.isEmpty()){
				for(Task sonTask:listSonTask){
					sonTask.setState(task.getState());
					taskDao.remarkTaskState(sonTask);//任务暂停
					
					//取消任务办结时间
					this.updateTaskFinishTime(null, sonTask.getId());
					
					//向未删除的任务成员发送消息
					if(sonTask.getDelState()==0){
						//任务的负责人，执行人以及关注人员
						List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), sonTask.getId(),true);
						//查询子任务最后的办结人
						List<TaskHandOver> lastExecutors = taskDao.listHandOverForExecute(sonTask.getId(),task.getComId(),null);
						if(null!=lastExecutors && !lastExecutors.isEmpty()){
							for (TaskHandOver taskHandOver : lastExecutors) {
								//子项变动发送消息
								todayWorksService.addTodayWorks(userInfo, taskHandOver.getToUser(), sonTask.getId(), 
										"任务标记为‘"+state+"’",ConstantInterface.TYPE_TASK, shares, null);
								
							}
						}
						
					}
					//模块日志添加
					this.addTaskLog(userInfo.getComId(),sonTask.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+state+"\"成功");
				}
			}
			taskDao.remarkTaskState(task);//任务暂停
			
			//取消任务办结时间
			this.updateTaskFinishTime(null, task.getId());
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,-1, task.getId(),"任务标记为‘"+state+"’", ConstantInterface.TYPE_TASK, shares,null);
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+state+"\"成功");
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+state+"\"失败");
		
		}
		return succ;
	}

	/**
	 * 添加任务留言
	 * @param taskTalk
	 * @throws Exception 
	 */
	public Integer addTaskTalk(TaskTalk taskTalk,UserInfo userInfo) throws Exception{
		Integer id = taskDao.add(taskTalk);
		//查询任务信息
		Task task = (Task) taskDao.objectQuery(Task.class, taskTalk.getTaskId());
		
		Integer[] upfilesId = taskTalk.getUpfilesId();
		if(null!=upfilesId){
			for (Integer upfileId : upfilesId) {
				TaskTalkUpfile upfiles = new TaskTalkUpfile();
				//企业编号
				upfiles.setComId(taskTalk.getComId());
				//任务主键
				upfiles.setTaskId(taskTalk.getTaskId());
				//留言的主键
				upfiles.setTaskTalkId(id);
				//上传人
				upfiles.setUserId(taskTalk.getSpeaker());
				//文件主键
				upfiles.setUpfileId(upfileId);
				
				taskDao.add(upfiles);
				//为附件创建索引
				uploadService.updateUpfileIndex(upfileId,userInfo,"add",taskTalk.getTaskId(),ConstantInterface.TYPE_TASK);
			}
			//添加到文档中心
			fileCenterService.addModFile(userInfo, Arrays.asList(upfilesId), task.getTaskName());
		}

		//添加信息分享人员
		List<TaskSharer> listTaskSharer = taskTalk.getListTaskSharer();
		Set<Integer> pushUserIdSet = new HashSet<Integer>();
		if(null != listTaskSharer && !listTaskSharer.isEmpty()){
			for (TaskSharer taskSharer : listTaskSharer) {
				//人员主键
				Integer userId = taskSharer.getSharerId();
				pushUserIdSet.add(userId);
				//删除上次的分享人员
				taskDao.delByField("taskSharer", new String[]{"comId","taskId","sharerId"},
						new Object[]{userInfo.getComId(),taskTalk.getTaskId(),userId});
                taskSharer.setTaskId(taskTalk.getTaskId());
                taskSharer.setComId(userInfo.getComId());
                taskDao.add(taskSharer);
			}
		}

		//分享信息查看
		List<UserInfo> taskShares = new ArrayList<UserInfo>();
		
		if (null != task) {
			//查询消息的推送人员
			taskShares = taskDao.listPushTodoUserForTask(taskTalk.getTaskId(), userInfo.getComId(),pushUserIdSet);
			Iterator<UserInfo> userids =  taskShares.iterator();
			for(;userids.hasNext();){
				UserInfo user = userids.next();
				if(user.getId().equals(userInfo.getId())){
					userids.remove();
                    taskShares.remove(user);
				}
				//设置全部普通消息
				todayWorksService.updateTodayWorksBusSpecTo0(ConstantInterface.TYPE_TASK, taskTalk.getTaskId(), user.getId());
			}
			// 添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_TASK, taskTalk.getTaskId(), "参与任务讨论:" + taskTalk.getContent(),
                    taskShares, userInfo.getId(), 1);

			//清除上次查看记录
			viewRecordService.delViewRecord(userInfo,taskShares,taskTalk.getTaskId(),ConstantInterface.TYPE_TASK);
		}

//		//用于查询任务的执行人
//		Task taskT = (Task) taskDao.objectQuery(Task.class, taskTalk.getTaskId());
//		//任务的负责人，执行人以及关注人员
//		List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), taskTalk.getTaskId(),true);
//		//添加待办提醒通知
//		todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), taskTalk.getTaskId(), "添加留言:"+taskTalk.getContent(), ConstantInterface.TYPE_TASK, shares,null);
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_TASKTALK,
				"添加任务留言",taskTalk.getTaskId());
		//更新任务索引
//		this.updateTaskIndex(taskTalk.getTaskId(),userInfo,"update");
		return id;
	}
	/**
	 * 根据主键id查询留言详情
	 * @param id
	 * @param comId
	 * @return
	 */
	public TaskTalk queryTaskTalk(Integer id,Integer comId){
		TaskTalk taskTalk = taskDao.queryTaskTalk(id,comId);
		//任务留言的附件
		if(null!=taskTalk){
			taskTalk.setListTaskTalkFile(taskDao.listTaskTalkFile(comId,taskTalk.getTaskId(),id));
		}
		return taskTalk;
	}
	/**
	 * 根据任务主键查询其下的留言信息
	 * @param taskId
	 * @param comId
	 * @return
	 */
	public List<TaskTalk> listTaskTalk(Integer taskId,Integer comId){
		//任务的分页留言信息
		List<TaskTalk> listTaskTalk = taskDao.listTaskTalk(taskId,comId);
		//需要返回的结果信息
		List<TaskTalk> list = new ArrayList<TaskTalk>();
		if(null!=listTaskTalk && !listTaskTalk.isEmpty()){
			//查询留言的所有附件信息
			List<TaskTalkUpfile> talkUpfiles = taskDao.listTaskTalkFile(comId, taskId, null);
			//缓存留言与附件关系集合
			Map<Integer, List<TaskTalkUpfile>> map = new HashMap<Integer, List<TaskTalkUpfile>>();
			
			//存在附件信息
			if(null!=talkUpfiles && !talkUpfiles.isEmpty()){
				//遍历附件集合，缓存数据
				for(TaskTalkUpfile taskTalkUpfile:talkUpfiles){
					//留言主键
					Integer taskTalkId = taskTalkUpfile.getTaskTalkId();
					//取得对应的集合信息
					List<TaskTalkUpfile> taskTalkUpfiles = map.get(taskTalkId);
					if(null == taskTalkUpfiles){
						taskTalkUpfiles = new ArrayList<TaskTalkUpfile>();
					}
					//集合添加数据
					taskTalkUpfiles.add(taskTalkUpfile);
					//缓存数据
					map.put(taskTalkId, taskTalkUpfiles);
				}
			}
			//遍历留言信息
			for (TaskTalk taskTalk : listTaskTalk) {
				//留言设置附件关联
				taskTalk.setListTaskTalkFile(map.get(taskTalk.getId()));
				list.add(taskTalk);
			}
		}
		return list;
	}
	/**
	 * 删除任务留言
	 * @param taskTalk
	 * @throws Exception 
	 */
	public void delTaskTalk(TaskTalk taskTalk,UserInfo userInfo) throws Exception{
		//用于查询任务的执行人
		Task taskT = (Task) taskDao.objectQuery(Task.class, taskTalk.getTaskId());
				
		if("yes".equals(taskTalk.getDelChildNode())){
			//留言的附件
			List<TaskTalk> taskTalks = taskDao.listTaskTalkUpfileForDel(taskTalk.getComId(), taskTalk.getId());
			for (TaskTalk taskTalkSingle : taskTalks) {
				//删除留言附件
				taskDao.delByField("taskTalkUpfile", new String[]{"comId","taskTalkId"}, 
						new Object[]{taskTalkSingle.getComId(),taskTalkSingle.getId()});
			}
			//删除当前节点及其子节点回复
			taskDao.delTaskTalk(taskTalk.getId(),taskTalk.getComId());
		}else{
			//删除留言附件
			taskDao.delByField("taskTalkUpfile", new String[]{"comId","taskTalkId"}, 
					new Object[]{taskTalk.getComId(),taskTalk.getId()});
			
			//把子节点的parentId向上提一级
			taskDao.updateTaskTalkParentId(taskTalk.getId(),taskTalk.getComId());
			//删除当前节点
			taskDao.delByField("taskTalk", new String[]{"comId","id"},new Integer[]{taskTalk.getComId(),taskTalk.getId()});
		}
		
		//任务的负责人，执行人以及关注人员
		List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), taskTalk.getTaskId(),true);
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), taskTalk.getTaskId(), "删除留言", ConstantInterface.TYPE_TASK, shares,null);
		
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_TALKDEL,
				"删除任务留言",taskTalk.getTaskId());
		//更新任务索引
//		this.updateTaskIndex(taskTalk.getTaskId(),userInfo,"update");
	}
	/**
	 * 添加任务模块操作日志
	 * @param comId 企业主键
	 * @param taskId 关联任务主键
	 * @param userId 操作者ID
	 * @param userName 操作者姓名
	 * @param content 动作描述
	 */
	public void addTaskLog(Integer comId,Integer taskId,Integer userId,String userName,String content){
		TaskLog taskLog = new TaskLog();
		taskLog.setComId(comId);
		taskLog.setTaskId(taskId);
//		content = userName +"在"+DateTimeUtil.formatDate(new Date(), 1)+content;
		taskLog.setContent(content);
		taskLog.setUserId(userId);
		taskLog.setUserName(userName);
		taskDao.add(taskLog);
	}
	/**
	 * 获取任务日志集合
	 * @param taskId
	 * @param comId
	 * @return
	 */
	public List<TaskLog> listTaskLog(Integer taskId,Integer comId){
		return taskDao.listTaskLog(taskId,comId);
	}
	/**
	 * 获取此任务以及此任务后代任务以外的任务集合
	 * @param task
	 * @return
	 */
	public String taskStrJson(Task task){
		List<Task> listTask = taskDao.listTaskOfOthers(task);
		StringBuffer strJson=new StringBuffer();
		if(null!=listTask && !listTask.isEmpty()){
			strJson.append("[");
			for(Task vo:listTask){
				strJson.append("{\"id\":\""+vo.getId()+"\",\"name\":\""+vo.getTaskName()+"\"},");
			}
			strJson=new StringBuffer(strJson.substring(0,strJson.lastIndexOf(",")));
			strJson.append("]");
		}
		return strJson.toString();
	}
	/**
	 * 获取任务附件
	 * @param taskId
	 * @param comId
	 * @return
	 */
	public List<TaskUpfile> listPagedTaskUpfile(TaskUpfile taskUpfile,Integer comId){
		return taskDao.listPagedTaskUpfile(taskUpfile,comId);
	}
	/**
	 * 获取任务对象json字符串
	 * @param taskID
	 * @param comId
	 * @return
	 */
	public String taskJsonStr(Integer taskID,UserInfo user){
		Task task = taskDao.queryTaskById(taskID, user);
		StringBuffer strJson=new StringBuffer();
		if(null!=task){
			strJson.append("{id:\""+task.getId()+"\",taskName:\""+task.getTaskName()+"\",owner:\""+task.getOwner()+"\"");
			strJson.append(",ownerName:\""+task.getOwnerName()+"\",uuid:\""+task.getUuid()+"\",filename:\""+task.getFilename()+"\",gender:\""+task.getGender()+"\"");
			strJson.append("}");
		}
		return strJson.toString();
	}
	
	/**
	 * 验证执行人是否有重复的
	 * @param userInfo
	 * @param task
	 * @return
	 */
	private List<TaskExecutor> checkRepExecutor(UserInfo userInfo,Task task){
		//重复的执行人
		List<TaskExecutor> listRepUser = new ArrayList<>();
		//本次选中的执行人
		List<TaskExecutor> thisExecutors = task.getListTaskExecutor();
		//本次选中的执行人主键
		Set<Integer> listExecutorId = new HashSet<>(thisExecutors.size());
		//遍历本次选中的执行人
		if(null!=thisExecutors && !thisExecutors.isEmpty()){
			for (TaskExecutor taskExecutor : thisExecutors) {
				Integer executor = taskExecutor.getExecutor();
				if(!userInfo.getId().equals(executor)){
					listExecutorId.add(taskExecutor.getExecutor());
				}
			}
		}
		//任务当前执行人集合
		List<TaskExecutor> executors = taskDao.listTaskExecutor(task.getId(), userInfo.getComId());
		if(null!=executors && !executors.isEmpty()){
			for (TaskExecutor taskExecutor : executors) {
				Integer executor = taskExecutor.getExecutor();
				if(listExecutorId.contains(executor)){
					listRepUser.add(taskExecutor);
				}
			}
		}
		return listRepUser;
		
	}
	/**
	 * 协同转办
	 * @param taskParam 任务信息
	 * @param userInfo 当前操作人员
	 * @param sendWay 发送方式
	 * @return
	 */
	public Map<String,Object> updateTaskCooperateConfig(Task taskParam,UserInfo userInfo,String[] sendWay){
		
		Task taskT = (Task) taskDao.objectQuery(Task.class, taskParam.getId());
		
		Map<String,Object> map = new HashMap<>();
		//验证是否有除开自己以外的执行人
		List<TaskExecutor> listRepUser = this.checkRepExecutor(userInfo,taskParam);
		if(null!= listRepUser && !listRepUser.isEmpty()){
			String repUserName = "";
			for (TaskExecutor taskExecutor : listRepUser) {
				repUserName = repUserName+","+taskExecutor.getExecutorName();
			}
			repUserName = repUserName.substring(1);
			map.put("status", "f2");
			map.put("info", "已有任务执行人["+repUserName+"]!");
			return map;
		}
		
		/******修改任务属性**********/
		//添加任务的执行人
		String handLimitTime = taskParam.getDealTimeLimit();
		//任务执行人更新后，原有执行人已在参与人中，无需修改附件管理的关联
		//任务执行人更新
		taskParam.setDealTimeLimit(null);
		taskDao.update(taskParam);
		/******修改任务属性结束**********/
		
		//协同附件绑定任务
		taskParam.setTaskName(taskT.getTaskName());
		
		this.addTaskFile(taskParam, userInfo);
		//协同说明添加到任务留言下
		if(null!=taskParam.getCooperateExplain() && !"".equals(taskParam.getCooperateExplain())){
			TaskTalk taskTalk = new TaskTalk();
			taskTalk.setTaskId(taskParam.getId());
			taskTalk.setComId(taskParam.getComId());
			taskTalk.setContent(taskParam.getCooperateExplain());
			taskTalk.setSpeaker(taskParam.getOperator());
			taskTalk.setParentId(-1);
			taskDao.add(taskTalk);
		}
		//办理时限需要重新设置
		taskParam.setDealTimeLimit(handLimitTime);
		//修改任务的执行人和流程步骤信息
		this.updateTaskExectorAndHandInfo(taskParam, userInfo);
		
		/*************修改任务的共享人员信息开始*******************/
		this.updateTaskShareInfo(taskParam, userInfo);
		/*************修改任务的共享人员信息结束*******************/
		
		//删除自己待办信息
		todayWorksService.delTodoWork(taskParam.getId(), userInfo.getComId(), userInfo.getId(),
				ConstantInterface.TYPE_TASK, "1");
		//发送消息信息
		this.updateSendCooperateMsg(taskParam, userInfo,sendWay);
		
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASKDONE, "协同任务:"+taskT.getTaskName(),taskParam.getId());
		
		//更新任务的所有后代参与人--此任务需放在更新方法最后执行
		this.updateTaskSharerForChildren(taskParam.getId(),taskParam.getComId());
		
		return map;
				
	}
	
	/**
	 * 协同任务配置
	 * @param task
	 * @param msgShare
	 */
	public void updateHandOverTask(Task task,UserInfo userInfo,String[] sendWay){
		/******修改任务属性**********/
		//添加任务的执行人
		String handLimitTime = task.getDealTimeLimit();
		//任务执行人更新后，原有执行人已在参与人中，无需修改附件管理的关联
		//任务执行人更新
		task.setDealTimeLimit(null);
		
		String expectTime = task.getExpectTime();
		task.setExpectTime(null);
		
		taskDao.update(task);
		/******修改任务属性结束**********/
		//协同附件绑定任务
		this.addTaskFile(task, userInfo);
		
		//协同说明添加到任务留言下
		if(null!=task.getCooperateExplain() && !"".equals(task.getCooperateExplain())){
			TaskTalk taskTalk = new TaskTalk();
			taskTalk.setTaskId(task.getId());
			taskTalk.setComId(task.getComId());
			taskTalk.setContent(task.getCooperateExplain());
			taskTalk.setSpeaker(task.getOperator());
			taskTalk.setParentId(-1);
			taskDao.add(taskTalk);
		}
		//办理时限需要重新设置
		task.setDealTimeLimit(handLimitTime);
		task.setExpectTime(expectTime);
		
		//修改任务的执行人和流程步骤信息
		this.updateTaskHandOverUser(task, userInfo,sendWay);
		
		/*************修改任务的共享人员信息开始*******************/
		this.updateTaskShareInfo(task, userInfo);
		/*************修改任务的共享人员信息结束*******************/
		
		Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASKDONE, "协同任务:"+taskT.getTaskName(),task.getId());
		
		//更新任务的所有后代参与人--此任务需放在更新方法最后执行
		this.updateTaskSharerForChildren(task.getId(),task.getComId());
				
	}
	
	/**
	 * 向指定人员发送消息	
	 * @param taskParam 任务信息
	 * @param userInfo 当前操作人员
	 * @param sendWay
	 */
	private void updateSendCooperateMsg(Task taskParam, UserInfo userInfo,
			String[] sendWay) {
		Task taskT = (Task) taskDao.objectQuery(Task.class, taskParam.getId());
		
		String handLimitTime = taskParam.getDealTimeLimit();
		//设置办理时限
		if(CommonUtil.isNull(handLimitTime)){//节点时限未设置时，节点办理时限默认为任务完成时限
			Task mTask = taskT;
			//任务无时限
			if(CommonUtil.isNull(mTask.getDealTimeLimit())){
				handLimitTime = mTask.getDealTimeLimit();
			}else{
				//任务期限 与 当前日期比较
				int res =DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd).compareTo(mTask.getDealTimeLimit());
				if(res>0){//当前时间大
					handLimitTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
				}else{//任务时限大
					handLimitTime = mTask.getDealTimeLimit();
				}
			}
		}else{
			//节点任务期限 与 当前日期比较
			int res =DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd).compareTo(handLimitTime);
			if(res>0){//当前时间大
				handLimitTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			}//节点任务期限大或相等。 不改变
		}
		if(!StringUtils.isEmpty(handLimitTime)){
			//节点任务期限 与 当前日期比较
			int res =DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd).compareTo(handLimitTime);
			if(res>0){//当前时间大
				handLimitTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			}//节点任务期限大或相等。 不改变
		}
		//向执行人发送待办消息
		//设置任务的办理人员
		List<TaskExecutor> listTaskExecutors = taskParam.getListTaskExecutor();
		//已有的执行人员
		Set<Integer> executorIds = new  HashSet<Integer>();
		//任务执行人
		List<String> executorNameList = new ArrayList<String>();
		if(null != listTaskExecutors && !listTaskExecutors.isEmpty()){//设置有办理人员
			for (TaskExecutor taskExecutor : listTaskExecutors) {
				//删除上次待办信息
				todayWorksService.delTodoWork(taskParam.getId(), userInfo.getComId(), taskExecutor.getExecutor(),ConstantInterface.TYPE_TASK, "1");
				executorIds.add(taskExecutor.getExecutor());
				
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_TASK, taskParam.getId(), 
						"任务转办给\""+taskExecutor.getExecutorName()+"\"协同办理", taskExecutor.getExecutor(), userInfo.getId(), 1);
				executorNameList.add(taskExecutor.getExecutorName());
				
				//添加工作轨迹
				systemLogService.addWorkTrace(userInfo, taskExecutor.getExecutor(), 
						ConstantInterface.TYPE_TASK, taskParam.getId(), "协同办理\""+userInfo.getUserName()+"\"转办的任务\""+taskT.getTaskName()+"\"");
				
				//取得待办事项主键
				TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),taskExecutor.getExecutor(),
						taskT.getId(),ConstantInterface.TYPE_TASK,0);
				if(null!=todayWorks){
					JpushUtils.sendTodoMessage(userInfo.getComId(), taskExecutor.getExecutor(), userInfo.getId(),
							todayWorks.getId(), taskT.getId(), ConstantInterface.TYPE_TASK,0,"任务:"+taskT.getTaskName());
				}
				//模块日志添加
				this.addTaskLog(userInfo.getComId(),taskParam.getId(),userInfo.getId(),userInfo.getUserName(),
						"任务\""+taskT.getTaskName()+"\"转办给\""+taskExecutor.getExecutorName()+"\"协同办理");
			}
			//添加系统日志
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "任务转办给"+executorNameList.toString()+"协同办理",
					ConstantInterface.TYPE_TASK, taskParam.getId(), userInfo.getComId(), userInfo.getOptIP());
		}
		
		List<UserInfo> shares = new ArrayList<>();
		//任务负责人和关注任务的人员
		List<TaskSharer> listTaskShare = taskParam.getListTaskSharer();
		List<Integer> sharerIds = new ArrayList<>();
		if(null!=listTaskShare && !listTaskShare.isEmpty()){
			Iterator<TaskSharer> iterator =  listTaskShare.iterator();
			while (iterator.hasNext()) {
				TaskSharer taskSharer = iterator.next();
				sharerIds.add(taskSharer.getSharerId());
			}
			shares = userInfoService.listScopeUserForMsg(userInfo.getComId(), sharerIds);
		}
		//正式推送普通消息
		List<UserInfo> userInfos = taskDao.listTaskUserForMsg(userInfo.getComId(), taskParam.getId(),false);
		userInfos.addAll(shares);
		if(null!=userInfos && !userInfos.isEmpty()){
			//不给自己推送消息
			executorIds.add(userInfo.getId());
			for (UserInfo shareUser : userInfos) {
				Integer sharerId = shareUser.getId();
				if(!executorIds.contains(sharerId)){//不给执行人推送消息
					//添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_TASK, taskParam.getId(), 
							"任务转办给"+executorNameList.toString()+"协同办理", sharerId, userInfo.getId(), 0);
					//取得待办事项主键
					TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),sharerId,
							taskT.getId(),ConstantInterface.TYPE_TASK,0);
					if(null!=todayWorks){
						JpushUtils.sendTodoMessage(userInfo.getComId(), sharerId, userInfo.getId(),
								todayWorks.getId(), taskT.getId(), ConstantInterface.TYPE_TASK,0,"任务:"+taskT.getTaskName());
					}
					
					executorIds.add(sharerId);
				}
			}
		}
		
	}
	/**
	 * 协同任务配置
	 * @param task
	 * @param msgShare
	 */
	public void updateTaskTurnBack(Task task,UserInfo userInfo,String[] sendWay){
		Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
		/******修改任务属性**********/
		//添加任务的执行人
		String handLimitTime = task.getDealTimeLimit();
		//任务执行人更新后，原有执行人已在参与人中，无需修改附件管理的关联
		//任务执行人更新
		task.setDealTimeLimit(null);
		taskDao.update(task);
		/******修改任务属性结束**********/
		//协同附件绑定任务
		task.setTaskName(taskT.getTaskName());
		this.addTaskFile(task, userInfo);
		
		//协同说明添加到任务留言下
		if(null!=task.getCooperateExplain() && !"".equals(task.getCooperateExplain())){
			TaskTalk taskTalk = new TaskTalk();
			taskTalk.setTaskId(task.getId());
			taskTalk.setComId(task.getComId());
			taskTalk.setContent(task.getCooperateExplain());
			taskTalk.setSpeaker(task.getOperator());
			taskTalk.setParentId(-1);
			taskDao.add(taskTalk);
		}
		//办理时限需要重新设置
		task.setDealTimeLimit(handLimitTime);
		//修改任务的执行人和流程步骤信息
		this.updateTaskBackAndHandInfo(task, userInfo,sendWay);
		
		/*************修改任务的共享人员信息开始*******************/
		this.updateTaskShareInfo(task, userInfo);
		/*************修改任务的共享人员信息结束*******************/
		
		
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASKDONE, "协同任务:"+taskT.getTaskName(),task.getId());
		
		//更新任务的所有后代参与人--此任务需放在更新方法最后执行
		this.updateTaskSharerForChildren(task.getId(),task.getComId());
		
	}
	/**
	 * 修改任务的共享人员信息
	 * @param task
	 * @param userInfo
	 */
	public void updateTaskShareInfo(Task task, UserInfo userInfo) {
		
		Map<Integer,Integer> sharerMap = new HashMap<Integer, Integer>();
		//把当前操作人添加为任务参与人
		sharerMap.put(userInfo.getId(),userInfo.getComId());
		//设置任务的办理人员
		List<TaskExecutor> listTaskExecutors = task.getListTaskExecutor();
		if(null != listTaskExecutors && !listTaskExecutors.isEmpty()){//设置有办理人员
			for (TaskExecutor taskExecutor : listTaskExecutors) {
				//把下一步执行人添加到参与人
				sharerMap.put(taskExecutor.getExecutor(),userInfo.getComId());
			}
		}
		//本次分享人员
		List<TaskSharer> listTaskShare = task.getListTaskSharer();
		if(null!=listTaskShare && !listTaskShare.isEmpty()){
			for (TaskSharer taskSharer : listTaskShare) {
				//把下一步执行人添加到参与人
				sharerMap.put(taskSharer.getSharerId(),userInfo.getComId());
			}
		}
		
		TaskSharer taskSharer = null;
		//更新任务参与人
		for(Map.Entry<Integer,Integer> entry:sharerMap.entrySet()){    
			//先删除任务的参与人
			taskDao.delByField("taskSharer", new String[]{"comId","taskId","sharerId"},
					new Integer[]{userInfo.getComId(),task.getId(),entry.getKey()});
		    
			taskSharer = new TaskSharer();
			taskSharer.setComId(userInfo.getComId());
			taskSharer.setTaskId(task.getId());
			taskSharer.setSharerId(entry.getKey());
			taskDao.add(taskSharer);    
		}
	}
	/**
	 * 修改任务的执行人和流程步骤信息
	 * @param task
	 * @param userInfo
	 */
	public void updateTaskExectorAndHandInfo(Task task, UserInfo userInfo) {
		//当前办理人员的移交记录信息
		TaskHandOver taskHandOver = taskDao.queryCurUserHandInfo(userInfo,task.getId());
		//任务移交
		//删除自己办理人
		taskDao.delByField("taskExecutor", new String[]{"comId","taskId","executor"}
			,new Integer[]{userInfo.getComId(),task.getId(),userInfo.getId()});
		//更新任务记录taskHandOver步骤节点为非当前步骤节点
		taskHandOver.setActHandleState(0);
		taskHandOver.setCurStep(-1);
		taskHandOver.setEndTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		taskDao.update(taskHandOver);
		
		//重新设置任务办理人员
		Integer preStep = -1;
		preStep = taskHandOver.getId();
		this.addTaskExectorAndHandInfo(task, task.getId(),preStep,userInfo,1);
	}
	
	private String constrHandLimitTime(Task task){
		String handLimitTime = task.getDealTimeLimit();
		//设置办理时限
		if(CommonUtil.isNull(handLimitTime)){//节点时限未设置时，节点办理时限默认为任务完成时限
			Task mTask = (Task) taskDao.objectQuery(Task.class, task.getId());
			//任务无时限
			if(CommonUtil.isNull(mTask.getDealTimeLimit())){
				handLimitTime = mTask.getDealTimeLimit();
			}else{
				//任务期限 与 当前日期比较
				int res =DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd).compareTo(mTask.getDealTimeLimit());
				if(res>0){//当前时间大
					handLimitTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
				}else{//任务时限大
					handLimitTime = mTask.getDealTimeLimit();
				}
			}
		}else{
			//节点任务期限 与 当前日期比较
			int res =DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd).compareTo(handLimitTime);
			if(res>0){//当前时间大
				handLimitTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			}//节点任务期限大或相等。 不改变
		}
		if(!StringUtils.isEmpty(handLimitTime)){
			//节点任务期限 与 当前日期比较
			int res =DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd).compareTo(handLimitTime);
			if(res>0){//当前时间大
				handLimitTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			}//节点任务期限大或相等。 不改变
		}
		return handLimitTime;
	}
	
	/**
	 * 修改任务的执行人和流程步骤信息
	 * @param task
	 * @param userInfo
	 */
	private void updateTaskHandOverUser(Task task, UserInfo userInfo,String[] sendWay) {
		
		String handLimitTime = this.constrHandLimitTime(task); task.getDealTimeLimit();
		
		//本次汇报进度
		Integer subProgress = task.getTaskProgress();
		subProgress = subProgress == null?0:subProgress;
		
		Task taskT = (Task) taskDao.objectQuery(Task.class, task.getId());
		
		//日志内容
		String content = "";
		if(StringUtils.isNotEmpty(handLimitTime)){
			content = "请在 "+handLimitTime+" 前办理任务:"+taskT.getTaskName();
		}else{
			content = "请尽快办理任务:"+taskT.getTaskName();
		}
		
		String version = task.getVersion();
		boolean blnPromoteTask = StringUtils.isNotEmpty(version) && "2".equals(version);
		
		List<TaskHandOver> taskHandOvers = taskDao.listTaskHandOverForExecute(task.getId(), userInfo.getComId(),null);
		if(null!=taskHandOvers){
			if(taskHandOvers.size()==1){//是适用于一个办理人任务
				Integer preStep = -1;
				TaskHandOver taskHandOver = taskHandOvers.get(0);
				preStep = taskHandOver.getId();
				
				Map<Integer, String> executorCostTimeMap = new HashMap<>();
				String costTime = null;
				if(blnPromoteTask) {
					costTime = taskPromoteService.calDoneExecuteTime(userInfo, task.getId(), taskHandOver.getToUser());
				}
				executorCostTimeMap.put(taskHandOver.getToUser(), costTime);
				
				//删除任务的执行人（此时任务的执行人只有一个）
				taskDao.delByField("taskExecutor", new String[]{"comId","taskId"},new Integer[]{userInfo.getComId(),task.getId()});
				
				//删除子任务的执行人
				taskDao.delByField("taskExecuteTime", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),task.getId()});
				//更新任务记录taskHandOver步骤节点为非当前步骤节点
				this.updateFinishTaskHandOver(task, userInfo,executorCostTimeMap);
				
				//重新设置任务办理人员
				this.addTaskExectorAndHandInfo(task, task.getId(),preStep,userInfo,1);
				
				//任务的负责人，执行人以及关注人员
				List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
				//设置任务的办理人员
				List<TaskExecutor> listTaskExecutors = task.getListTaskExecutor();
				if(null != listTaskExecutors && !listTaskExecutors.isEmpty()){//设置有办理人员
					//删除上次待办信息
					todayWorksService.delTodoWork(task.getId(), userInfo.getComId(), listTaskExecutors.get(0).getPushUser(),
							ConstantInterface.TYPE_TASK, "1");
					//任务执行人
					List<String> executorNameList = new ArrayList<String>();
					for (TaskExecutor taskExecutor : listTaskExecutors) {
						//删除上次待办信息
						todayWorksService.delTodoWork(task.getId(), userInfo.getComId(), taskExecutor.getExecutor(),
								ConstantInterface.TYPE_TASK, "1");
						//添加待办提醒通知
						todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_TASK, task.getId(), 
								"任务委托给\""+taskExecutor.getExecutorName()+"\"协同办理", taskExecutor.getExecutor(), userInfo.getId(), 1);
						executorNameList.add(taskExecutor.getExecutorName());
						//添加工作轨迹
						systemLogService.addWorkTrace(userInfo, taskExecutor.getExecutor(), 
								ConstantInterface.TYPE_TASK, task.getId(), "协同办理\""+userInfo.getUserName()+"\"委托的任务\""+taskT.getTaskName()+"\"");
						
						
						
						todayWorksService.sendMsgByUserConf(userInfo, taskT.getId(), content, ConstantInterface.TYPE_TASK, shares, sendWay);
						
						//取得待办事项主键
						TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),task.getExecutor(),
								taskT.getId(),ConstantInterface.TYPE_TASK,0);
						if(null!=todayWorks){
							JpushUtils.sendTodoMessage(userInfo.getComId(), taskExecutor.getExecutor(), userInfo.getId(),
									todayWorks.getId(), taskT.getId(), ConstantInterface.TYPE_TASK,0,"任务:"+taskT.getTaskName());
						}
						//模块日志添加
						this.addTaskLog(userInfo.getComId(),task.getId(),userInfo.getId(),userInfo.getUserName(),
								"任务\""+taskT.getTaskName()+"\"委托给\""+taskExecutor.getExecutorName()+"\"协同办理");
					}
					//添加系统日志
					systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "任务委托给"+executorNameList.toString()+"协同办理",
							ConstantInterface.TYPE_TASK, task.getId(), userInfo.getComId(), userInfo.getOptIP());
				}
			}else{//是用于协同的任务
				//多人任务是否全部办结,默认已办结
				boolean taskDoneState = true;
				Integer fromUserId = 0;
				Integer preStep = -1;
				
				for (TaskHandOver taskHandOver : taskHandOvers) {
					//任务的移交人员
					fromUserId = taskHandOver.getFromUser();
					preStep = taskHandOver.getId();
					Integer toUser = taskHandOver.getToUser();
					if(toUser.equals(userInfo.getId())){
						//修改办理节点的办结时间
						TaskHandOver updateTaskHandOver = new TaskHandOver();
						updateTaskHandOver.setComId(userInfo.getComId());
						updateTaskHandOver.setTaskId(task.getId());
						updateTaskHandOver.setEndTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
						updateTaskHandOver.setCurStep(0);
						updateTaskHandOver.setToUser(userInfo.getId());
						taskDao.update("update taskHandOver a set a.endTime=:endTime,a.curStep=:curStep where a.comid=:comId and a.taskId=:taskId and a.curStep=1 and a.toUser=:toUser", updateTaskHandOver);
						
						//修改当前人的工作进度
						TaskExecutor curTaskExecutor = new TaskExecutor();
						curTaskExecutor.setComId(userInfo.getComId());
						curTaskExecutor.setTaskId(task.getId());
						curTaskExecutor.setExecutor(toUser);
						curTaskExecutor.setTaskProgress(100);
						curTaskExecutor.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DONE));
						taskDao.update("update taskExecutor a set a.state=:state,a.taskProgress=:taskProgress where a.comid=:comId and a.taskId=:taskId and a.executor=:executor", curTaskExecutor);
						
					}else{
						//还有其他人没有完成
						Integer curStep = taskHandOver.getCurStep();
						if(curStep == 1){
							taskDoneState = false;
						}
					}
				}
				
				if(taskDoneState){//多人任务全部完成，将任务扭转给发布人
					
					List<TaskExecutor> executors = taskDao.listTaskExecutor(task.getId(), userInfo.getComId());
					Map<Integer,String> executorCostTimeMap = new HashMap<Integer,String>();
					for (TaskExecutor taskExecutor : executors) {
						Integer executor = taskExecutor.getExecutor();
						String costTime = taskPromoteService.calDoneExecuteTime(userInfo, task.getId(), taskExecutor.getExecutor());
						executorCostTimeMap.put(executor, costTime);
					}
					//删除任务的执行人（此时任务的执行人只有一个）
					taskDao.delByField("taskExecutor", new String[]{"comId","taskId"},new Integer[]{userInfo.getComId(),task.getId()});
					//删除子任务的执行人
					taskDao.delByField("taskExecuteTime", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),task.getId()});
					
					//重新设置任务办理人员
					this.addTaskExectorAndHandInfo(task, task.getId(),preStep,userInfo,1);
					
					if(!userInfo.getId().equals(fromUserId)){//委托给别人
						//任务的负责人，执行人以及关注人员
						List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
						
						//设置任务的办理人员
						List<TaskExecutor> listTaskExecutors = task.getListTaskExecutor();
						if(null != listTaskExecutors && !listTaskExecutors.isEmpty()){//设置有办理人员
							//任务执行人
							List<String> executorNameList = new ArrayList<String>();
							for (TaskExecutor taskExecutor : listTaskExecutors) {
								//删除上次待办信息
								todayWorksService.delTodoWork(task.getId(), userInfo.getComId(), taskExecutor.getExecutor(),
										ConstantInterface.TYPE_TASK, "1");
								//添加待办提醒通知
								todayWorksService.addTodayWorks(userInfo,taskExecutor.getExecutor(), task.getId(), "任务委托给\""+taskExecutor.getExecutorName()+"\"协同办理", ConstantInterface.TYPE_TASK, shares,null);
								executorNameList.add(taskExecutor.getExecutorName());
								
								//添加工作轨迹
								systemLogService.addWorkTrace(userInfo, taskExecutor.getExecutor(), 
										ConstantInterface.TYPE_TASK, task.getId(), "协同办理\""+userInfo.getUserName()+"\"委托的任务\""+taskT.getTaskName()+"\"");
								
								todayWorksService.sendMsgByUserConf(userInfo, taskT.getId(), content, ConstantInterface.TYPE_TASK, shares, sendWay);
								
								//取得待办事项主键
								TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),task.getExecutor(),
										taskT.getId(),ConstantInterface.TYPE_TASK,0);
								if(null!=todayWorks){
									JpushUtils.sendTodoMessage(userInfo.getComId(), taskExecutor.getExecutor(), userInfo.getId(),
											todayWorks.getId(), taskT.getId(), ConstantInterface.TYPE_TASK,0,"任务:"+taskT.getTaskName());
								}
								//模块日志添加
								this.addTaskLog(userInfo.getComId(),task.getId(),userInfo.getId(),userInfo.getUserName(),
										"任务\""+taskT.getTaskName()+"\"委托给\""+taskExecutor.getExecutorName()+"\"协同办理");
							}
							
							//添加系统日志
							systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "任务委托给"+executorNameList.toString()+"协同办理",
									ConstantInterface.TYPE_TASK, task.getId(), userInfo.getComId(), userInfo.getOptIP());
						}
					}
					
				}else{
					//删除待办信息
					todayWorksService.delTodoWork(task.getId(), userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASK, "1");
					
					UserInfo taskExecutor = (UserInfo) taskDao.objectQuery(UserInfo.class, fromUserId);
					if(null!=taskExecutor){
						//模块日志添加
						this.addTaskLog(userInfo.getComId(),task.getId(),userInfo.getId(),userInfo.getUserName(),
								"任务\""+taskT.getTaskName()+"\"委托给\""+taskExecutor.getUserName()+"\"协同办理");
					}
//					if(CommonUtil.isNull(task.getListTaskExecutor())){
//					}else{
//						TaskExecutor taskExecutor = task.getListTaskExecutor().get(0);
//						//模块日志添加
//						this.addTaskLog(userInfo.getComId(),task.getId(),userInfo.getId(),userInfo.getUserName(),
//								"任务\""+taskT.getTaskName()+"\"委托给\""+taskExecutor.getExecutorName()+"\"协同办理");
//					}
				}
			
			}
			
		}
	}
	
	/**
	 * 修改任务的执行人和流程步骤信息
	 * @param taskParam
	 * @param userInfo
	 */
	public void updateTaskBackAndHandInfo(Task taskParam, UserInfo userInfo,String[] sendWay) {
		
		Task taskT = (Task) taskDao.objectQuery(Task.class, taskParam.getId());
		//任务当前执行人
		List<TaskHandOver> listTaskHandOver = taskDao.listHandOverForExecute(taskParam.getId(), userInfo.getComId(),null);
		if(null!=listTaskHandOver && !listTaskHandOver.isEmpty()){
			if(listTaskHandOver.size()==1){
				//删除任务的执行人（此时任务的执行人只有一个）
				taskDao.delByField("taskExecutor", new String[]{"comId","taskId"},new Integer[]{userInfo.getComId(),taskParam.getId()});
				Integer preStep = -1;
				TaskHandOver taskHandOver = listTaskHandOver.get(0);
				preStep = taskHandOver.getId();
				
				//更新任务记录taskHandOver步骤节点为非当前步骤节点
				this.updateFinishTaskHandOver(taskParam, userInfo,null);
				//重新设置任务办理人员
				this.addTaskExectorAndHandInfo(taskParam, taskParam.getId(),preStep,userInfo,0);
				
				//删除自己待办信息
				todayWorksService.delTodoWork(taskParam.getId(), userInfo.getComId(), userInfo.getId(),
						ConstantInterface.TYPE_TASK, "1");
				//发送消息
				this.updateSendTurnBackMsg(taskParam,userInfo,true);
				return;
			}
			//只有一位办理人员结束
			
			//有多位办理人员
			
			//多人任务是否全部办结,默认已办结
			boolean taskDoneState = true;
			Integer preStep = -1;
			
			for (TaskHandOver taskHandOver : listTaskHandOver) {
				preStep = taskHandOver.getId();
				Integer toUser = taskHandOver.getToUser();
				if(toUser.equals(userInfo.getId())){
					//修改办理节点的办结时间
					TaskHandOver updateTaskHandOver = new TaskHandOver();
					updateTaskHandOver.setComId(userInfo.getComId());
					updateTaskHandOver.setTaskId(taskParam.getId());
					updateTaskHandOver.setEndTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
					updateTaskHandOver.setCurStep(0);
					updateTaskHandOver.setToUser(userInfo.getId());
					taskDao.update("update taskHandOver a set a.endTime=:endTime,a.curStep=:curStep where a.comid=:comId and a.taskId=:taskId and a.curStep=1 and a.toUser=:toUser", updateTaskHandOver);
					
					//修改当前人的工作进度
					TaskExecutor curTaskExecutor = new TaskExecutor();
					curTaskExecutor.setComId(userInfo.getComId());
					curTaskExecutor.setTaskId(taskParam.getId());
					curTaskExecutor.setExecutor(toUser);
					curTaskExecutor.setTaskProgress(100);
					curTaskExecutor.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DONE));
					taskDao.update("update taskExecutor a set a.state=:state,a.taskProgress=:taskProgress where a.comid=:comId and a.taskId=:taskId and a.executor=:executor", curTaskExecutor);
					
				}else{
					//还有其他人没有完成
					Integer curStep = taskHandOver.getCurStep();
					if(curStep == 1){
						taskDoneState = false;
					}
				}
			}
			
			if(taskDoneState){//多人任务全部完成，将任务扭转给发布人
				//删除任务的执行人（此时任务的执行人只有一个）
				taskDao.delByField("taskExecutor", new String[]{"comId","taskId"},new Integer[]{userInfo.getComId(),taskParam.getId()});
				
				//重新设置任务办理人员
				this.addTaskExectorAndHandInfo(taskParam, taskParam.getId(),preStep,userInfo,0);
				
				//删除自己待办信息
				todayWorksService.delTodoWork(taskParam.getId(), userInfo.getComId(), userInfo.getId(),
						ConstantInterface.TYPE_TASK, "1");
				//发送消息
				this.updateSendTurnBackMsg(taskParam,userInfo,true);
				
			}else{
				//删除自己待办信息
				todayWorksService.delTodoWork(taskParam.getId(), userInfo.getComId(), 
						userInfo.getId(),ConstantInterface.TYPE_TASK, "1");
				
				TaskExecutor taskExecutor = taskParam.getListTaskExecutor().get(0);
				//发送消息
				this.updateSendTurnBackMsg(taskParam,userInfo,false);
				//模块日志添加
				this.addTaskLog(userInfo.getComId(),taskParam.getId(),userInfo.getId(),userInfo.getUserName(),
						"任务\""+taskT.getTaskName()+"\"转办给\""+taskExecutor.getExecutorName()+"\"协同办理");
			}
			
		}
	}
	/**
	 * 发送普通消息
	 * @param taskParam
	 * @param userInfo
	 */
	private void updateSendTurnBackMsg(Task taskParam, UserInfo userInfo,boolean todoState) {
		Task taskT = (Task) taskDao.objectQuery(Task.class, taskParam.getId());
		//已有的执行人员
		Set<Integer> executorIds = new  HashSet<Integer>();
		//向执行人发送待办消息
		//设置任务的办理人员
		List<TaskExecutor> listTaskExecutors = taskParam.getListTaskExecutor();
		//任务执行人
		List<String> executorNameList = new ArrayList<String>();
		if(null != listTaskExecutors && !listTaskExecutors.isEmpty()){//设置有办理人员
			for (TaskExecutor taskExecutor : listTaskExecutors) {
				executorIds.add(taskExecutor.getExecutor());
				executorNameList.add(taskExecutor.getExecutorName());
				if(todoState){//需要发送待办
					//删除上次待办信息
					todayWorksService.delTodoWork(taskParam.getId(), userInfo.getComId(), taskExecutor.getExecutor(),ConstantInterface.TYPE_TASK, "1");
					
					//添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_TASK, taskParam.getId(), 
							"任务完成", taskExecutor.getExecutor(), userInfo.getId(), 1);
					
					//添加工作轨迹
					systemLogService.addWorkTrace(userInfo, taskExecutor.getExecutor(), 
							ConstantInterface.TYPE_TASK, taskParam.getId(), "协同办理\""+userInfo.getUserName()+"\"完成的任务\""+taskT.getTaskName()+"\"");
					
					//取得待办事项主键
					TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),taskExecutor.getExecutor(),
							taskT.getId(),ConstantInterface.TYPE_TASK,0);
					if(null!=todayWorks){
						JpushUtils.sendTodoMessage(userInfo.getComId(), taskExecutor.getExecutor(), userInfo.getId(),
								todayWorks.getId(), taskT.getId(), ConstantInterface.TYPE_TASK,0,"任务:"+taskT.getTaskName());
					}
					//模块日志添加
					this.addTaskLog(userInfo.getComId(),taskParam.getId(),userInfo.getId(),
							userInfo.getUserName(), "完成任务\""+taskT.getTaskName());
				}
			}
			//添加系统日志
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "完成任务",
					ConstantInterface.TYPE_TASK, taskParam.getId(), userInfo.getComId(), userInfo.getOptIP());
		}
		List<UserInfo> shares = new ArrayList<>();
		//任务负责人和关注任务的人员
		List<TaskSharer> listTaskShare = taskParam.getListTaskSharer();
		List<Integer> sharerIds = new ArrayList<>();
		if(null!=listTaskShare && !listTaskShare.isEmpty()){
			Iterator<TaskSharer> iterator =  listTaskShare.iterator();
			while (iterator.hasNext()) {
				TaskSharer taskSharer = iterator.next();
				sharerIds.add(taskSharer.getSharerId());
			}
			shares = userInfoService.listScopeUserForMsg(userInfo.getComId(), sharerIds);
		}
		//正式推送普通消息
		List<UserInfo> userInfos = taskDao.listTaskUserForMsg(userInfo.getComId(), taskParam.getId(),false);
		userInfos.addAll(shares);
		if(null!=userInfos && !userInfos.isEmpty()){
			//不给自己推送消息
			executorIds.add(userInfo.getId());
			for (UserInfo shareUser : userInfos) {
				Integer sharerId = shareUser.getId();
				if(!executorIds.contains(sharerId)){//不给执行人推送消息
					//添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_TASK, taskParam.getId(), 
							"完成任务", sharerId, userInfo.getId(), 0);
					//取得待办事项主键
					TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),sharerId,
							taskT.getId(),ConstantInterface.TYPE_TASK,0);
					if(null!=todayWorks){
						JpushUtils.sendTodoMessage(userInfo.getComId(), sharerId, userInfo.getId(),
								todayWorks.getId(), taskT.getId(), ConstantInterface.TYPE_TASK,0,"任务:"+taskT.getTaskName());
					}
					executorIds.add(sharerId);
				}
			}
		}
		
	}
	/**
	 * 添加任务附件信息
	 * @param task 附件信息
	 * @param userInfo 当前操作人员
	 */
	private void addTaskFile(Task task, UserInfo userInfo) {
		List<TaskUpfile> listTaskUpfiles = task.getListTaskUpfile();
		if(null!=listTaskUpfiles && !listTaskUpfiles.isEmpty()){
			//缓存附件主键信息
			List<Integer> listFileId = new ArrayList<Integer>();
			//遍历添加附件信息
			for(TaskUpfile taskUpfile : listTaskUpfiles){
				
				taskDao.delByField("taskUpfile", new String[]{"comId","upfileId","taskId"}, 
						new Object[]{userInfo.getComId(),taskUpfile.getUpfileId(),task.getId()});
				
				taskUpfile.setComId(task.getComId());
				taskUpfile.setTaskId(task.getId());
				taskUpfile.setUserId(userInfo.getId());
				taskDao.add(taskUpfile);
				
				listFileId.add(taskUpfile.getUpfileId());
			}
			//归档到文档中心
			fileCenterService.addModFile(userInfo,listFileId,task.getTaskName());
			
		}
		List<Upfiles> listUpfiles = task.getListUpfiles();
		if(null!=listUpfiles && !listUpfiles.isEmpty()){
			//缓存附件主键信息
			List<Integer> listFileId = new ArrayList<Integer>();
			//遍历添加附件信息
			for(Upfiles upfile : listUpfiles){
				TaskUpfile taskUpfile = new TaskUpfile();
				taskUpfile.setUpfileId(upfile.getId());
				taskDao.delByField("taskUpfile", new String[]{"comId","upfileId","taskId"}, 
						new Object[]{userInfo.getComId(),taskUpfile.getUpfileId(),task.getId()});
				
				taskUpfile.setComId(task.getComId());
				taskUpfile.setTaskId(task.getId());
				taskUpfile.setUserId(userInfo.getId());
				taskDao.add(taskUpfile);
				
				listFileId.add(taskUpfile.getUpfileId());
			}
			//归档到文档中心
			fileCenterService.addModFile(userInfo,listFileId,task.getTaskName());
			
		}
	}
	/**
	 * 更新任务索引
	 * @param taskId
	 * @param userInfo
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception 
	 */
	public void updateTaskIndex(Integer taskId,UserInfo userInfo,String opType) throws Exception{
		//更新任务索引
		Task task = taskDao.queryTaskById(taskId,userInfo);
		if(null==task){return;}
		//再添加新索引数据
		//把bean非空属性值连接策划那个字符串
		//不检索字段设置为null
//		task.setId(null);
//		task.setComId(null);
//		task.setParentId(null);
//		task.setBusId(null);
//		task.setBusType(null);
//		task.setOwner(null);
//		task.setCreator(null);
//		task.setTaskProgress(null);
//		task.setState(null);
//		task.setExecutor(null);
//		task.setUuid(null);
//		task.setFilename(null);
//		task.setGender(null);
//		task.setTalkSum(null);
//		task.setSonTaskNum(null);
//		task.setExecutorFileName(null);
//		task.setExecutorUuid(null);
//		task.setExecutorGender(null);
//		task.setOperator(null);
//		task.setPromptMsg(null);
//		task.setShareMsg(null);
//		StringBuffer attStr = new StringBuffer(CommonUtil.objAttr2String(task,null));
		StringBuffer attStr = new StringBuffer(task.getTaskName());
		//获取项目所有参与人
//		List<TaskSharer> listTaskSharer = taskDao.listTaskOwners(userInfo.getComId(),taskId);
//		//参与人名称连接成字符串创建索引
//		for(TaskSharer vo : listTaskSharer){
//			attStr.append(vo.getSharerName()+",");
//		}
		//任务留言记录创建字符串索引
//		List<TaskTalk> listTaskTalk = taskDao.listTaskTalk4Index(userInfo.getComId(),taskId);
//		for(TaskTalk vo:listTaskTalk){
//			attStr.append(vo.getContent()+","+vo.getSpeakerName()+",");
//		}
		//任务子任务集合为其创建索引
//		List<Task> listTask = taskDao.listSonTask4Index(userInfo.getComId(),taskId);
//		for(Task vo : listTask){
//			attStr.append(vo.getTaskName()+",");
//		}
		//获取任务附件集合
//		List<TaskUpfile> listUpfiles = taskDao.listPagedTaskUpfile(taskId,userInfo.getComId());
//		if(null!=listUpfiles){
//			Upfiles upfile = null;
//			for(TaskUpfile vo:listUpfiles){
//				//附件内容添加
//				upfile = uploadService.queryUpfile4Index(vo.getUpfileId(),userInfo.getComId());
//				//附件名称
//				attStr.append(upfile.getFilename()+",");
//				//附件内容
//				attStr.append(upfile.getFileContent()+",");
//			}
//		}
		//单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String indexKey = userInfo.getComId()+"_"+ConstantInterface.TYPE_TASK+"_"+taskId;
		//为任务创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				indexKey,userInfo.getComId(),taskId,ConstantInterface.TYPE_TASK,
				task.getTaskName(),attStr.toString(),DateTimeUtil.parseDate(task.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,indexKey));
		}
		
	}
	/**
	 * 更新任务所有后代的参与人
	 * @param taskId
	 * @param comId
	 */
	public void updateTaskSharerForChildren(int taskId,int comId){
		//获取当前任务所有参与人
		List<TaskSharer> listCurTaskSharer = taskDao.listTaskOwners(comId,taskId);
		//当前任务参与人MAP
		Map<Integer,Integer> sharerMap = null;
		//获取任务的所有后代任务
		List<Task> listTaskOfChildren = taskDao.listTaskOfOnlyChildren(taskId,comId);
		if(null!=listTaskOfChildren && !listTaskOfChildren.isEmpty()){
			List<TaskSharer> listTaskOwners = null;
			for(Task vo:listTaskOfChildren){
				sharerMap = new HashMap<Integer,Integer>();
				if(null!=listCurTaskSharer && !listCurTaskSharer.isEmpty()){
					for(TaskSharer sharer:listCurTaskSharer){
						sharerMap.put(sharer.getSharerId(),comId);
					}
				}
				TaskSharer taskSharer =null;
				listTaskOwners = taskDao.listTaskOwners(comId,vo.getId());
				if(null!=listTaskOwners && !listTaskOwners.isEmpty()){
					for(TaskSharer sharer:listTaskOwners){
						sharerMap.put(sharer.getSharerId(),comId);
					}
					//先删除任务的参与人
					taskDao.delByField("taskSharer", new String[]{"comId","taskId"},new Integer[]{comId,vo.getId()});
					//把父任务所有相关人员添加为子任务的参与人
					for(Map.Entry<Integer,Integer> entry:sharerMap.entrySet()){    
					    taskSharer = new TaskSharer();
						taskSharer.setComId(comId);
						taskSharer.setTaskId(vo.getId());
						taskSharer.setSharerId(entry.getKey());
						taskDao.add(taskSharer);    
					}
				}
			}
		}
	}



	/**
	 * 删除自己的无效任务
	 * @param userInfo
	 */
	public void delUnusedTask(UserInfo userInfo){
		List<Task> listOfUnusedTask = taskDao.listOfUnusedTask(userInfo);
		if(null==listOfUnusedTask || listOfUnusedTask.isEmpty() ){return;}
		List<Integer> taskIds = new ArrayList<Integer>();
		for(int i=0;i<listOfUnusedTask.size();i++){
			taskIds.add(listOfUnusedTask.get(i).getId());
		}
		try {
			this.delTask(taskIds, userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 待办任务数
	 * @param comId 企业编号
	 * @param userId 当前操作员的主键
	 * @return
	 */
	public Integer countTodo(Integer comId, Integer userId) {
		return taskDao.countTodo(comId,userId);
	}
	
	/**
	 * 负责人或是执行人的任务
	 * @param comId 企业号
	 * @param userId 用户主键
	 * @return
	 */
	public List<Task> listUserAllTask(Integer comId, Integer userId) {
		return taskDao.listUserAllTask(comId,userId);
	}
	
	/**
	 * 查看任务移交记录
	 * @param taskId 任务主键
	 * @param comId 企业号
	 * @return
	 */
	public List<FlowRecord> listFlowRecord(Integer taskId, Integer comId) {
		return taskDao.listFlowRecord(taskId,comId);
	}
	
	/**
	 * 任务详情状态（仅需要纯粹的任务信息）
	 * @param taskId
	 * @return
	 */
	public Task getTaskById(Integer taskId) {
		return (Task) taskDao.objectQuery(Task.class, taskId);
	}
	/**
	 * 任务详情状态（不需要任务参与人）
	 * @param taskId
	 * @param sessioUser
	 * @return
	 */
	public Task getTaskById(Integer taskId, UserInfo sessioUser) {
		Task task = taskDao.queryTaskById(taskId, sessioUser);
		//任务的执行人员信息
		List<TaskExecutor> listTaskExecutor = this.listTaskExecutor(taskId, sessioUser);
		task.setListTaskExecutor(listTaskExecutor);
		return task;
	}

	/**
	 * 获取有指定执行人的任务信息
	 * @param taskId 任务主键
 	 * @param sessioUser 当前操作用户，一般为任务负责人
	 * @return com.westar.base.model.Task
	 * @author LiuXiaoLin
	 * @date 2018/6/19 0019 13:49
	 */
	public Task getTaskForCopy(Integer taskId,UserInfo sessioUser){
		//获取任务的基本信息
		Task task = (Task) taskDao.objectQuery(Task.class,taskId);

		//获取任务关联
		Task taskT = taskDao.getTaskBusInfo(taskId,sessioUser);
		//设置任务关联
		task.setBusName(taskT.getBusName());

		//任务的执行人员信息
//		List<TaskExecutor> listTaskExecutor = this.listTaskExecutor(taskId, sessioUser);
//		task.setListTaskExecutor(listTaskExecutor);

        //获取任务附件
        List<TaskUpfile> listUpfile = taskDao.listTaskUpfile(taskId,sessioUser.getComId());
        List<Upfiles> upfiles = new ArrayList<Upfiles>();
        Upfiles upfile = null;
        Set<Integer> set = new HashSet<Integer>();
        if(null!=listUpfile && !listUpfile.isEmpty()){
        	
        	for(TaskUpfile taskUpfile : listUpfile){
        		Integer upfilesId = taskUpfile.getUpfileId();
        		if(set.contains(upfilesId)){
        			continue;
        		}
        		set.add(upfilesId);
        		
        		upfile = new Upfiles();
        		upfile.setFilename(taskUpfile.getFilename());
        		upfile.setFileExt(taskUpfile.getFileExt());
        		upfile.setOwnerName(taskUpfile.getCreatorName());
        		upfile.setId(taskUpfile.getUpfileId());
        		upfile.setComId(sessioUser.getComId());
        		upfile.setSizem(taskUpfile.getSizem());
        		upfiles.add(upfile);
        	}
        }

        task.setListUpfiles(upfiles);

		return task;
	}

	/**
	 * 获取任务下的所有子任务集合 
	 * @param taskId
	 * @param comid
	 * @return
	 */
	public List<Task> listSonTask(int taskId,int comid){
		return taskDao.listSonTask(taskId,comid);
	}
	/**
	 * 查询任务当前任务的父节点，不包括自己
	 * @param parentId
	 * @param comId
	 * @return
	 */
	public List<Task> getAllParentTask(Integer parentId, Integer comId) {
		List<Task> list = taskDao.getAllParentTask(parentId,comId);
		return list;
	}
	/**
	 * 查询任务当前设置父节点的父节点,包括自己
	 * @param parentId
	 * @param comId
	 * @return
	 */
	public List<Task> listTaskOfAllParent(Integer parentId, Integer comId) {
		List<Task> list = taskDao.listTaskOfAllParent(parentId,comId);
		return list;
	}
	
	/**
	 * 任务的所有查看人
	 * @param comId 企业号
	 * @param modId 任务主键
	 * @return
	 */
	public List<UserInfo> listTaskUserForMsg(Integer comId, Integer modId){
		//任务的所有查看人
		List<UserInfo> shares = taskDao.listTaskUserForMsg(comId, modId,true);
		return shares;
	}
	/**
	 * 查询任务名称有匹配的任务
	 * @param taskName 任务名称
	 * @param comId 企业号
	 * @return
	 */
	public List<Task> listSimiTasksByTaskName(String taskName, Integer comId) {
		return taskDao.listSimiTasksByTaskName(taskName,comId);
	}
	/**
	 * 合并任务信息
	 * @param task 合并后的任务
	 * @param ids 参与合并的任务
	 * @param userInfo 操作员
	 * @param taskIdAndBusIds  参与合并的任务主键与项目主键对
	 * @throws Exception 
	 */
	public void updateTaskForCompress(Task task, Integer[] ids,
			UserInfo userInfo, String taskIdAndBusIds) throws Exception {
		if(null!=ids && ids.length>0){
			//合并后任务主键
			Integer taskId = task.getId();
			//父任务主键
			Integer ptaskId = task.getParentId();
			//选中的模块主键
			Integer busId = 0;
			//选中的模块类型
			String busType = "0";
			
			//父任务
			Task pTask = taskDao.getTaskBusInfo(ptaskId, userInfo);
			if(null!=pTask && pTask.getBusId()>0 && pTask.getBusDelState()==0){//合并后，相当于有父任务
				busId = pTask.getBusId();
				busType = pTask.getBusType();
				
				//统一到父任务中
				task.setBusId(busId);
				task.setBusType(busType);
			}else{//合并后，相当于没有父任务,/需要考虑合并后的任务是否关联模块
				//选中模块对应的任务
				if(!StringUtil.isBlank(taskIdAndBusIds)){
					String[] taskIdAndBusId = taskIdAndBusIds.split("@");
					if(taskIdAndBusId.length>1 && !StringUtil.isBlank(taskIdAndBusId[1])){
						//选中的项目主键，该项目一定没有被删除
						busId = Integer.parseInt(taskIdAndBusId[1]);
						busType = taskIdAndBusId[2];
						
						if(busId>0){//本次有关联模块
							task.setBusId(busId);
							task.setBusType(busType);
						}else{//没有关联的模块，需要将任务关联模块关系取消
							task.setBusId(0);
							task.setBusType("0");
						}
					}
				}
			}
			//选中的任务简介的任务主键
			Integer remark = Integer.parseInt(task.getTaskRemark());
			
			if(remark.equals(taskId)){//若是简介没有改变
				task.setTaskRemark(null);
			}else{
				//设置合并后的任务简介
				Task taskForRemark = (Task) taskDao.objectQuery(Task.class, remark);
				String taskRemark = taskForRemark.getTaskRemark();
				task.setTaskRemark(taskRemark);
			}
			//合并任务的主要信息
			taskDao.update(task);
			
			//整合的任务分享人
			List<TaskSharer> taskSharerList = taskDao.listTaskSharer(taskId,userInfo.getComId());
			//整合的客户分享人主键
			Set<Integer> sharerId = new HashSet<Integer>();
			
			if(null!=taskSharerList && taskSharerList.size()>0){
				for (TaskSharer taskSharer : taskSharerList) {
					sharerId.add(taskSharer.getSharerId());
				}
			}
			//合并前关注客户信息的人员
			List<Attention> listAtten = attentionService.listAtten(userInfo.getComId(), taskId, ConstantInterface.TYPE_TASK);
			//合并前关注客户信息的人员主键
			Set<Integer> attenUserId = new HashSet<Integer>();
			if(null != listAtten && listAtten.size()>0){
				for (Attention attention : listAtten) {
					attenUserId.add(attention.getUserId());
				}
			}
			String log = "";
			
			if(busType.equals(ConstantInterface.TYPE_ITEM)){//合并后是项目，关联项目阶段
				//合并后的任务在项目阶段明细
				StagedInfo stagedInfo = itemService.getStagedRelateInfo(userInfo.getComId(),taskId,ConstantInterface.STAGED_TASK);
				if(null==stagedInfo){//合并后的任务没在项目阶段明细中存在
					//取得最近
					Integer stagedItemId = itemService.getStageItemId(userInfo, busId);
					//项目阶段添加关联任务
					itemService.addStageRelateMod(userInfo, busId, stagedItemId, task.getId(),ConstantInterface.TYPE_TASK);
				}else if(!stagedInfo.getItemId().equals(busId)){//若是关联的不是同一个项目,修改数据
					Integer stagedItemId = itemService.getStageItemId(userInfo, busId);
					//重新设置项目主键
					stagedInfo.setItemId(busId);
					//重新设置阶段明细主键
					stagedInfo.setStagedItemId(stagedItemId);
					
					taskDao.update(stagedInfo);
				}
			}else{//合并后不是项目,删除项目任务关联
				//删除任务的项目阶段关联
				taskDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"}, 
						new Object[]{userInfo.getComId(), taskId,"task"});
			}
			//开始合并信息
			for (Integer taskCId : ids) {
				//取得任务信息
				Task taskT = (Task) taskDao.objectQuery(Task.class, taskCId);
				log +="'"+taskT.getTaskName()+"',";
				
				//任务的执行人
				List<TaskExecutor> taskExecutors = taskDao.listTaskExecutor(taskCId, userInfo.getComId());
				
				//整合待选任务的模块
				//整合项目阶段（不包括子任务）
				if(null!=taskT.getBusId() && taskT.getBusId()>0 ){//待选任务已关联有模块
					if(taskT.getBusType().equals(ConstantInterface.TYPE_ITEM) 
							&& !taskCId.equals(taskId)){//待合并任务，关联的是项目,需要整合项目阶段
						//待选任务的项目阶段
						StagedInfo stagedCInfo = itemService.getStagedRelateInfo(userInfo.getComId(),taskCId,ConstantInterface.STAGED_TASK);
						if(null!=stagedCInfo ){//待合并任务在某个项目中
							//删除不是合并后的任务关联的项目阶段
							taskDao.delById(StagedInfo.class, stagedCInfo.getId());
							//添加项目日志
							itemService.addItemLog(userInfo.getComId(),stagedCInfo.getItemId(), userInfo.getId(),
									userInfo.getUserName(), "合并任务后，删除"+taskT.getTaskName()+"的项目关联");
						}
					}
				}
				if(taskCId.equals(taskId)){//是合并的任务
					continue;
				}
				
				//直接合并留言信息和附件
				taskDao.compressTalk(userInfo.getComId(), taskCId,taskId);
				
				//整合子任务（先修改父任务，然后统一修改关联的项目阶段）
				taskDao.compressSonTask(userInfo.getComId(), taskCId,taskId);
				//整合闹铃
				clockService.compressClock(userInfo.getComId(), taskCId,taskId,ConstantInterface.TYPE_TASK);
				//合并附件留言
				fileCenterService.comPressFileTalk(userInfo.getComId(),taskCId,taskId,ConstantInterface.TYPE_TASK);
				
				//合并分享人
				//整合的任务分享人
				List<TaskSharer> taskSharerCList = taskDao.listTaskSharer(taskCId,userInfo.getComId());
				if(null!=taskSharerCList && !taskSharerCList.isEmpty()){
					for (TaskSharer taskCSharer : taskSharerCList) {
						if(sharerId.contains(taskCSharer.getSharerId())){//有共同参与人
							//删除参与人
							taskDao.delById(TaskSharer.class, taskCSharer.getId());
							continue;
						}else{//没有共同的分享人，就添加数据
							
							sharerId.add(taskCSharer.getSharerId());
							
							//重新设置任务主键
							taskCSharer.setTaskId(taskId);
							//修改参与人所属客户信息
							taskDao.update(taskCSharer);
						}
					}
				}
				
				//任务的分享人员
				if(null!=taskExecutors && !taskExecutors.isEmpty()){
					for (TaskExecutor executor : taskExecutors) {
						//若是任务的执行人不在任务参与人中，则将其设置成参与人
						if(!sharerId.contains(executor.getExecutor())){
							TaskSharer taskSharer = new TaskSharer();
							//设置企业号
							taskSharer.setComId(userInfo.getComId());
							//设置所属的任务
							taskSharer.setTaskId(taskId);
							//将任务的执行人放置到合并的任务参与人中
							taskSharer.setSharerId(executor.getExecutor());
							//添加数据
							taskDao.add(taskSharer);
						}
					}
				}
				
				
				//合并关注信息
				List<Attention> listAttention = attentionService.listAtten(userInfo.getComId(), taskCId, ConstantInterface.TYPE_TASK);
				if(null != listAttention && listAttention.size() > 0){
					for (Attention attention : listAttention) {
						if(attenUserId.contains(attention.getUserId())){//人员同时关注合并的任务信息
							taskDao.delById(Attention.class, attention.getId());
							continue;
						}else{
							attenUserId.add(attention.getUserId());
							//重新设置业务主键
							attention.setBusId(taskId);
							taskDao.update(attention);
						}
					}
				}
				//删除执行人
				taskDao.delByField("taskExecutor", new String[]{"comId","taskId"}, 
						new Object[]{userInfo.getComId(), taskCId});
				//删除办理时限报延申请
				taskDao.delByField("delayApply", new String[]{"comId","taskId"}, 
						new Object[]{userInfo.getComId(), taskCId});
				//删除移交记录表
				taskDao.delByField("taskHandOver", new String[]{"comId","taskId"}, 
						new Object[]{userInfo.getComId(), taskCId});
				//删除日志表
				taskDao.delByField("taskLog", new String[]{"comId","taskId"}, 
						new Object[]{userInfo.getComId(), taskCId});
				//删除浏览记录
				taskDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, 
						new Object[]{userInfo.getComId(), taskCId,ConstantInterface.TYPE_TASK});
				//删除最新动态
				taskDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, 
						new Object[]{userInfo.getComId(), taskCId,ConstantInterface.TYPE_TASK});
				//删除消息提醒
				taskDao.delByField("todayWorks", new String[]{"comId","busId","busType"}, 
						new Object[]{userInfo.getComId(), taskCId,ConstantInterface.TYPE_TASK});
				//删除实际执行时间
				taskDao.delByField("taskExecuteTime", new String[]{"comId","taskId"}, 
						new Object[]{userInfo.getComId(), taskCId});
				
				taskDao.delById(Task.class, taskCId);
			}
			
			/**
			 * 说明:上面操作已将待合并的任务的子任务调整了上级任务，并且整理了待合并的任务自身的模块关联
			 * 	二:整合子任务的模块关联
			 * 		（1）合并后有父任务
			 * 			1、父任务有关联模块，则直接整合到父任务关联的模块
			 * 			2、父任务没有关联模块，则直接整合到选中的模块
			 * 		（2）合并后没有父任务
			 * 			1、若本次有关联模块，直接整合到选中的模块
			 * 			2、若本次没有关联模块，只删除自己关联的项目，不删除子类的
			 */
			//需要整理一下
			if(null!=pTask){//合并后有父任务
				if(pTask.getBusId()>0 && pTask.getBusDelState()==0){//父任务关联了模块,且没有被删除
					//任务以及子任务设置任务阶段，子任务这是项目关联
					this.updateStageInfo(userInfo, task, pTask.getBusId(),pTask.getBusType(), "parent",
							"合并任务"+log.substring(0,log.length()-1)+"后关联项目");
				}else if(busId>0){//父任务没有关联了项目或是已删除，本次合并后有项目
					//任务以及子任务设置任务阶段，子任务这是项目关联
					this.updateStageInfo(userInfo, task, busId,busType, "self",
							"合并任务"+log.substring(0,log.length()-1)+"后关联项目");
				}
			}else if(busId>0){//合并后没有父任务，但是有合并项目
				//任务以及子任务设置任务阶段，子任务这是项目关联
				this.updateStageInfo(userInfo, task, busId,busType, "self",
						"合并任务"+log.substring(0,log.length()-1)+"后关联项目");
			}else{//合并后没有模块
				//只删除自己关联的项目，不删除子类的
				taskDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"}, new Object[]{userInfo.getComId(), taskId,"task"});
			}
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskDao.listTaskUserForMsg(userInfo.getComId(), taskId,true);
			
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,task.getExecutor(),taskId,"合并任务"+log.substring(0,log.length()-1)+"信息", 
					ConstantInterface.TYPE_TASK, shares,null);
			
			//添加日志
			this.addTaskLog(userInfo.getComId(), taskId, userInfo.getId(), 
					userInfo.getUserName(),  "合并任务"+log.substring(0,log.length()-2)+"信息");

			//更新任务索引
//			this.updateTaskIndex(taskId,userInfo,"update");
		}
	}
	/**
	 * 取得任务的关联项目以及项目阶段名称
	 * @param taskId 任务主键
	 * @param userInfo 操作人员
	 * @return
	 */
	public Task getTaskBusInfo(Integer taskId, UserInfo userInfo) {
		Task task = taskDao.getTaskBusInfo(taskId,userInfo);
		
		//父任务的项目已被删除
		boolean blnSetVal = null != task 
				&& null!= task.getBusDelState()
				&& task.getBusDelState()==1;
		if(blnSetVal){
			task.setBusId(0);
			task.setBusName(null);
			task.setStagedItemId(null);
			task.setStagedItemName(null);
		}
		return task;
	}
	/**
	 * 取得当前操作员所在企业的所有待整理的任务
	 * @param userInfo
	 */
	public List<Task> listTaskForStageInfo(UserInfo userInfo) {
		//取得当前操作员所在企业的所有待整理的任务
		List<Task> taskList = taskDao.listTaskForStageInfo(userInfo.getComId());
		return taskList;
	}
	/**
	 * 任务关联的项目数据整合
	 * @param userInfo 操作员
	 * @param busId 项目主键
	 * @return
	 */
	public void updateBusTask(UserInfo userInfo, Integer busId,String busType,String content) {
		List<Task> list = taskDao.listBusTask(userInfo,busId,busType);
		if(null!=list && list.size()>0){
			
			if(ConstantInterface.TYPE_ITEM.equals(busType)){
				//任务对应的父任务主键
				Map<Integer, Integer> taskItemMap = new HashMap<Integer, Integer>();
				for (Task task : list) {
					//任务主键
					Integer taskId = task.getId();
					//父任务主键
					Integer ptaskId = task.getParentId();
					
					if(!taskItemMap.containsKey(ptaskId)){//父任务不存在
						task.setBusId(busId);
						task.setBusType(busType);
						taskDao.update(task);
						
						//任务以及子任务设置任务阶段，子任务这是项目关联
						this.updateStageInfo(userInfo, task, busId,busType, "self",content);
					}
					taskItemMap.put(taskId, ptaskId);
				}
			}else if(ConstantInterface.TYPE_CRM.equals(busType)){
				//任务对应的父任务主键
				Map<Integer, Integer> taskCrmMap = new HashMap<Integer, Integer>();
				for (Task task : list) {
					//任务主键
					Integer taskId = task.getId();
					//父任务主键
					Integer ptaskId = task.getParentId();
					
					if(!taskCrmMap.containsKey(ptaskId)){//父任务不存在
						task.setBusId(busId);
						task.setBusType(busType);
						taskDao.update(task);
						//任务以及子任务设置任务阶段，子任务这是项目关联
						this.updateStageInfo(userInfo, task, busId,busType, null,null);
					}
					taskCrmMap.put(taskId, ptaskId);
				}
			}
		}
	}
	/**
	 * 分类统计个人权限下任务数据
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public Task taskCount(UserInfo userInfo,boolean isForceInPersion){
		return taskDao.taskCount(userInfo,isForceInPersion);
	}
	
	/**
	 * 模块关联的任务
	 * @param task 任务
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public List<Task> listPagedBusTask(Task task, UserInfo userInfo) {
		List<Task> listTask = taskDao.listPagedBusTask(task,userInfo);
		return listTask;
	}
	/**
	 * 取得需要关联的任务数据
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> listTaskForRelevance(Task task, UserInfo userInfo) {
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
		List<Task> listTask = taskDao.listTaskForRelevance(task,userInfo,isForceIn);
		return listTask;
	}
	/**
	 * 模块关联的任务数
	 * @param task 任务
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public Integer countBusTask(Task task, UserInfo userInfo) {
		return taskDao.countBusTask(task,userInfo);
	}
	
	/**
	 * 模块关联的任务总数（后台用）
	 * @param task 任务
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public Integer countTasks(Task task, UserInfo userInfo) {
		return taskDao.countTasks(task,userInfo);
	}
	
	/**
	 * 个人超期任务统计
	 * @param userInfo 当前操作人信息
	 * @return
	 */
	public int overdueTaskNum(UserInfo userInfo){
		return taskDao.overdueTaskNum(userInfo);
	}
	/**
	 * 删除任务留言和任务关联附件
	 * @param taskId 任务转
	 * @param taskFileId 附件关联主键
	 * @param type 类型 task talk
	 * @return
	 * @throws Exception 
	 */
	public void delTaskUpfile(Integer taskFileId, String type,UserInfo userInfo,Integer taskId) throws Exception {
		if(type.equals("task")){
			TaskUpfile taskUpfile = (TaskUpfile) taskDao.objectQuery(TaskUpfile.class, taskFileId);
			taskDao.delById(TaskUpfile.class, taskFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) taskDao.objectQuery(Upfiles.class, taskUpfile.getUpfileId());
			this.addTaskLog(userInfo.getComId(),taskId,userInfo.getId(),userInfo.getUserName(),"删除了任务附件："+upfiles.getFilename());
		}else{
			TaskTalkUpfile taskTalkUpfile = (TaskTalkUpfile) taskDao.objectQuery(TaskTalkUpfile.class, taskFileId);
			taskDao.delById(TaskTalkUpfile.class, taskFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) taskDao.objectQuery(Upfiles.class, taskTalkUpfile.getUpfileId());
			this.addTaskLog(userInfo.getComId(),taskId,userInfo.getId(),userInfo.getUserName(),"删除了任务留言附件："+upfiles.getFilename());
		}
		//更新任务索引
//		this.updateTaskIndex(taskFileId,userInfo,"update");
	}
	
	/**
	 * 项目阶段关联
	 * @param task
	 * @param userInfo
	 */
	public void updateStagedId(Task task, UserInfo userInfo) {
		
		itemService.updateStagInfo(userInfo,task.getId(),task.getBusId(),task.getStagedItemId());
	}
	
	/**
	 * 取得指定人员的待办事项 -app
	 * @param userId 指定人员主键
	 * @param userInfo 当前操作人员
	 * @param type 任务类型 待办todo 
	 * @return
	 */
	public Integer countTaskTodo(Task task, UserInfo userInfo,String type) {
		String version = task.getVersion();
		if(StringUtils.isEmpty(version) || !version.equals("2")){
			task.setVersion("1");
		}
		return taskDao.countTaskTodo(task,userInfo,type);
	}
	
	/*****************************任务统计*****************************/
	/**
	 * 下属任务数量办理统计
	 * @param userInfo 当前操作人
	 * @param task  任务属性筛选
	 * @return
	 */
	public List<UserInfo> listSubTaskCount(UserInfo userInfo,Task task){
		String version = task.getVersion();
		if(StringUtils.isEmpty(version) || !version.equals("2")){
			task.setVersion("1");
		}
		return taskDao.listSubTaskCount(userInfo, task);
		
	}
	
	/**
	 * 查询用于参与任务紧急程度统计的负责人
	 * @param userInfo 当前操作人员
	 * @param task 任务的查询条件
	 * @param isForceIn 是否为强制参与人
	 * @return
	 */
	public List<ModStaticVo> listTaskGrade4Statistic(UserInfo userInfo,Task task, Boolean isForceIn){
		
		List<ModStaticVo> result = new ArrayList<ModStaticVo>();
		
		List<DataDic> dataDics = DataDicContext.getInstance().listTreeDataDicByType("grade");
		if(null!=dataDics && !dataDics.isEmpty()){
			//取出统计人员的名称
			List<Task> listTaskOwer = taskDao.listTaskOwner4Statistic(userInfo,task,isForceIn);
			List<Integer> owners = new ArrayList<Integer>();
			if(null != listTaskOwer && !listTaskOwer.isEmpty()){
				for (Task owner : listTaskOwer) {
					owners.add(owner.getOwner());
				}
			}
			List<DataDic> dataDicsT = new ArrayList<DataDic>();
			dataDicsT.addAll(dataDics);
			Collections.reverse(dataDicsT);
			for ( int i=0;i<dataDicsT.size();i++) {
				DataDic dataDic = dataDicsT.get(i);
				
				if(dataDic.getParentId()==-1){
					continue;
				}
				
				task.setGrade(dataDic.getCode());
				
				ModStaticVo modStaticVo = new ModStaticVo();
				modStaticVo.setName(dataDic.getZvalue());
				modStaticVo.setType(dataDic.getCode());
				//任务紧急度统计
				List<ModStaticVo> allTaskCount = taskDao.listTaskGrade4Statistic(userInfo,task,isForceIn,owners);
				modStaticVo.setChildModStaticVo(allTaskCount);
				
				result.add(modStaticVo);
			}
		}
		return result;
	}
	/**
	 * 任务超期统计
	 * @param userInfo 当前操作人员
	 * @param task  任务的查询条件
	 * @param isForceIn 是否为强制参与人 
	 * @param executors 参与任务逾期统计的执行人主键
	 * @return
	 */
	public List<ModStaticVo> listTaskOverDue4Statistic(UserInfo userInfo,Task task, Boolean isForceIn){
		
		String version = task.getVersion();
		if(StringUtils.isEmpty(version) || !version.equals("2")){
			task.setVersion("2");
		}
		
		List<ModStaticVo> result = new ArrayList<ModStaticVo>();
		List<DataDic> dataDics = DataDicContext.getInstance().listTreeDataDicByType("grade");
		if(null!=dataDics && !dataDics.isEmpty()){
			//取出统计人员的名称
			List<Task> listTaskExectors = taskDao.listExecutor4StatisticOverDue(userInfo,task,isForceIn);
			
			List<Integer> executors = new ArrayList<Integer>();
			if(null != listTaskExectors && !listTaskExectors.isEmpty()){
				for (Task owner : listTaskExectors) {
					executors.add(owner.getExecutor());
				}
			}
			List<DataDic> dataDicsT = new ArrayList<DataDic>();
			dataDicsT.addAll(dataDics);
			Collections.reverse(dataDicsT);
			for ( int i=0;i<dataDicsT.size();i++) {
				DataDic dataDic = dataDicsT.get(i);
				
				if(dataDic.getParentId()==-1){
					continue;
				}
				
				task.setGrade(dataDic.getCode());
				
				ModStaticVo modStaticVo = new ModStaticVo();
				modStaticVo.setName(dataDic.getZvalue());
				modStaticVo.setType(dataDic.getCode());
				//任务紧急度统计
				List<ModStaticVo> allTaskCount = taskDao.listTaskOverDue4Statistic(userInfo,task,isForceIn,executors);
				modStaticVo.setChildModStaticVo(allTaskCount);
				
				result.add(modStaticVo);
			}
		}
		return result;
	}
	/**
	 * 下属任务超期统计
	 * @param userInfo
	 * @return
	 */
	public List<ModStaticVo> listSubTaskOver(UserInfo userInfo){
		return taskDao.listSubTaskOver(userInfo);
	}
	
	/**
	 * 任务分配统计
	 * @param userInfo 当前操作人员
	 * @param task 任务的查询条件
	 * @param isForceIn 是否为强制参与人
	 * @param executors 任务执行 的主键集合
	 * @return
	 */
	public List<ModStaticVo> listTask4Statistic(UserInfo userInfo,Task task, Boolean isForceIn){
		
		
		List<ModStaticVo> result = new ArrayList<ModStaticVo>();
		List<DataDic> dataDics = DataDicContext.getInstance().listTreeDataDicByType("grade");
		if(null!=dataDics && !dataDics.isEmpty()){
			//取出统计人员的名称
			List<Task> listTaskExector = taskDao.listExecutor4Statistic(userInfo,task,isForceIn);
			List<Integer> executors = new ArrayList<Integer>();
			if(null != listTaskExector && !listTaskExector.isEmpty()){
				for (Task owner : listTaskExector) {
					executors.add(owner.getExecutor());
				}
			}
			List<DataDic> dataDicsT = new ArrayList<DataDic>();
			dataDicsT.addAll(dataDics);
			Collections.reverse(dataDicsT);
			for ( int i=0;i<dataDicsT.size();i++) {
				DataDic dataDic = dataDicsT.get(i);
				
				if(dataDic.getParentId()==-1){
					continue;
				}
				
				task.setGrade(dataDic.getCode());
				
				ModStaticVo modStaticVo = new ModStaticVo();
				modStaticVo.setName(dataDic.getZvalue());
				modStaticVo.setType(dataDic.getCode());
				//任务紧急度统计
				List<ModStaticVo> allTaskCount = taskDao.listExecutor4Statistic(userInfo,task,isForceIn,executors);
				modStaticVo.setChildModStaticVo(allTaskCount);
				result.add(modStaticVo);
			}
		}
		return result;
	}
	/**
	 * 统计项目任务查询
	 * @param userInfo
	 * @param item
	 * @param isForceIn
	 * @return
	 */
	public PageBean<ItemStagedInfo> listItemTask4Statistic(UserInfo curUser,Item item){
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_ITEM);
		//1、获取当前操作人权限下的所有项目
		List<Item> countNum = itemService.countAllItem(item,curUser,isForceIn);
		List<ItemStagedInfo> listStagedItem = itemService.listItemStagedInfo(item, curUser, isForceIn);
		if(!CommonUtil.isNull(listStagedItem)){
			AttenceRule attenceRule = attenceService.getAttenceRule(null,curUser.getComId());//考勤规则
			List<FestModDate> listFestModDateOfTeam = festModService.listFestModDateOfTeam(curUser.getComId());//获取当前团队下所有的节假日维护信息
			//团队的工作时段
			List<AttenceType> listAttenceTypes = attenceService.listAttenceTypes(curUser.getComId(), attenceRule.getId());
			for (ItemStagedInfo stageInfoVo : listStagedItem) {
				this.itemStageInfoCount(curUser, attenceRule,listFestModDateOfTeam,listAttenceTypes, stageInfoVo);//项目阶段信息计算
			}
		}
		
		
		/*if(!CommonUtil.isNull(listItemOfAll)){
			AttenceRule attenceRule = attenceService.getAttenceRule(null,curUser.getComId());//考勤规则
			List<FestModDate> listFestModDateOfTeam = festModService.listFestModDateOfTeam(curUser.getComId());//获取当前团队下所有的节假日维护信息
			//团队的工作时段
			List<AttenceType> listAttenceTypes = attenceService.listAttenceTypes(curUser.getComId(), attenceRule.getId());
			for (Item itemVo : listItemOfAll) {
				itemStageInfoCount(curUser, attenceRule,listFestModDateOfTeam,listAttenceTypes, itemVo);//项目阶段信息计算
			}
		}*/
	    PageBean<ItemStagedInfo> pageBean = new PageBean<ItemStagedInfo>();
	 	pageBean.setRecordList(listStagedItem);
	 	pageBean.setTotalCount(null==countNum?0:countNum.size());
		 	
		return pageBean;
	}
	
	/**
	 * 项目阶段信息计算
	 * @param curUser 当前操作人
	 * @param attenceRule 考勤规则
	 * @param listFestModDateOfTeam 当前团队下所有的节假日维护信息
	 * @param listAttenceTypes 团队的工作时段
	 * @param itemVo
	 */
	public void itemStageInfoCount(UserInfo curUser, AttenceRule attenceRule,
			List<FestModDate> listFestModDateOfTeam,
			List<AttenceType> listAttenceTypes, Item itemVo) {
		//1.1获取项目阶段划分
		List<ItemStagedInfo> listStagedItemInfo = itemService.listItemStagedInfo(curUser.getComId(),itemVo.getId());
		//1.2获取项目阶段下的所属任务集合
		for (ItemStagedInfo stagedItem : listStagedItemInfo) {
			List<Task> listTask = taskDao.listTaskByStagedItem(curUser.getComId(),itemVo.getId(),stagedItem.getRealId());
			stagedItem.setListTask(listTask);
			if(!CommonUtil.isNull(listTask)){
				Double usedTimes=0.0;
				Double overTimes=0.0;
				for (Task task2 : listTask) {
					//2、根据项目获取项目的每个阶段的任务耗时
					Task taskInfo = taskDao.queryTaskUsedTimes(curUser.getComId(),task2.getId());
					if(!CommonUtil.isNull(taskInfo)){
						//计算任务耗时
						if(ConstantInterface.STATIS_TASK_STATE_DONE.equals(taskInfo.getState().toString())){
							Double taskUsedTimes = festModService.calWorkTime(curUser.getComId(),taskInfo.getRecordCreateTime(),
									taskInfo.getEndTime(),attenceRule,listAttenceTypes,listFestModDateOfTeam);
							task2.setUsedTimes(new Double(taskUsedTimes).longValue());//任务耗时
							usedTimes += taskUsedTimes;
							task2.setEndTime(taskInfo.getEndTime());//记录任务完成时间
						}else{
							Double taskUsedTimes = festModService.calWorkTime(curUser.getComId(),taskInfo.getRecordCreateTime(),
									DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm),attenceRule,listAttenceTypes,listFestModDateOfTeam);
							task2.setUsedTimes(new Double(taskUsedTimes).longValue());//任务耗时
							usedTimes += taskUsedTimes;
						}
						//计算任务执行超时
						if(!CommonUtil.isNull(taskInfo.getDealTimeLimit())){//有任务办理时限时
							if(ConstantInterface.STATIS_TASK_STATE_DONE.equals(taskInfo.getState().toString())){
								Double taskOverTimes = festModService.calWorkTime(curUser.getComId(),taskInfo.getDealTimeLimit()+" 23:59",
										taskInfo.getEndTime(),attenceRule,listAttenceTypes,listFestModDateOfTeam);
								task2.setOverTimes(new Double(taskOverTimes).longValue());//任务超时
								overTimes += taskOverTimes;
								task2.setEndTime(taskInfo.getEndTime());//记录任务完成时间
							}else{
								Double taskOverTimes = festModService.calWorkTime(curUser.getComId(),taskInfo.getDealTimeLimit()+" 23:59",
										DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm),attenceRule,listAttenceTypes,
										listFestModDateOfTeam);
								task2.setOverTimes(new Double(taskOverTimes).longValue());//任务超时
								overTimes += taskOverTimes;
							}
						}
						
					}
				}
				stagedItem.setUsedTimes(new Double(usedTimes).longValue());//阶段耗时
				stagedItem.setOverTimes(new Double(overTimes).longValue());//阶段超时
			}
		}
		itemVo.setListStagedItemInfo(listStagedItemInfo);
	}

	/**
	 * 项目阶段信息计算
	 * @param curUser 当前操作人
	 * @param attenceRule 考勤规则
	 * @param listFestModDateOfTeam 当前团队下所有的节假日维护信息
	 * @param listAttenceTypes 团队的工作时段
	 * @param itemVo
	 */
	private void itemStageInfoCount(UserInfo curUser, AttenceRule attenceRule,
			List<FestModDate> listFestModDateOfTeam,
			List<AttenceType> listAttenceTypes, ItemStagedInfo stagedInfoVo) {
		
			List<Task> listTask = taskDao.listTaskByStagedItem(curUser.getComId(),stagedInfoVo.getItemId(),stagedInfoVo.getRealId());
			stagedInfoVo.setListTask(listTask);
			if(!CommonUtil.isNull(listTask)){
				Double usedTimes=0.0;
				Double overTimes=0.0;
				for (Task task2 : listTask) {
					//2、根据项目获取项目的每个阶段的任务耗时
					Task taskInfo = taskDao.queryTaskUsedTimes(curUser.getComId(),task2.getId());
					if(!CommonUtil.isNull(taskInfo)){
						//计算任务耗时
						if(ConstantInterface.STATIS_TASK_STATE_DONE.equals(taskInfo.getState().toString())){
							Double taskUsedTimes = festModService.calWorkTime(curUser.getComId(),taskInfo.getRecordCreateTime(),
									taskInfo.getEndTime(),attenceRule,listAttenceTypes,listFestModDateOfTeam);
							task2.setUsedTimes(new Double(taskUsedTimes).longValue());//任务耗时
							usedTimes += taskUsedTimes;
							task2.setEndTime(taskInfo.getEndTime());//记录任务完成时间
						}else{
							Double taskUsedTimes = festModService.calWorkTime(curUser.getComId(),taskInfo.getRecordCreateTime(),
									DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm),attenceRule,listAttenceTypes,listFestModDateOfTeam);
							task2.setUsedTimes(new Double(taskUsedTimes).longValue());//任务耗时
							usedTimes += taskUsedTimes;
						}
						//计算任务执行超时
						if(!CommonUtil.isNull(taskInfo.getDealTimeLimit())){//有任务办理时限时
							if(ConstantInterface.STATIS_TASK_STATE_DONE.equals(taskInfo.getState().toString())){
								Double taskOverTimes = festModService.calWorkTime(curUser.getComId(),taskInfo.getDealTimeLimit()+" 23:59",
										taskInfo.getEndTime(),attenceRule,listAttenceTypes,listFestModDateOfTeam);
								task2.setOverTimes(new Double(taskOverTimes).longValue());//任务超时
								overTimes += taskOverTimes;
								task2.setEndTime(taskInfo.getEndTime());//记录任务完成时间
							}else{
								Double taskOverTimes = festModService.calWorkTime(curUser.getComId(),taskInfo.getDealTimeLimit()+" 23:59",
										DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm),attenceRule,listAttenceTypes,
										listFestModDateOfTeam);
								task2.setOverTimes(new Double(taskOverTimes).longValue());//任务超时
								overTimes += taskOverTimes;
							}
						}
						
					}
				}
				stagedInfoVo.setUsedTimes(new Double(usedTimes).longValue());//阶段耗时
				stagedInfoVo.setOverTimes(new Double(overTimes).longValue());//阶段超时
			}
	}
	/**
	 * 统计项目任务查询
	 * @param userInfo
	 * @param task
	 * @param isForceIn
	 * @return
	 */
	public List<ModStaticVo> listCrmTask4Statistic(UserInfo userInfo,Task task){
		
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_CRM);
		List<ModStaticVo> result = new ArrayList<ModStaticVo>();
		
		List<DataDic> dataDics = DataDicContext.getInstance().listTreeDataDicByType("grade");
		if(null!=dataDics && !dataDics.isEmpty()){
			
			for ( int i=0;i<dataDics.size();i++) {
				DataDic dataDic = dataDics.get(i);
				
				if(dataDic.getParentId()==-1){
					continue;
				}
				
				task.setGrade(dataDic.getCode());
				
				ModStaticVo modStaticVo = new ModStaticVo();
				modStaticVo.setName(dataDic.getZvalue());
				modStaticVo.setType(dataDic.getCode());
				//任务紧急度统计
				List<ModStaticVo> allTaskCount = taskDao.listCrmTask4Statistic(userInfo,task,isForceIn);
				modStaticVo.setChildModStaticVo(allTaskCount);
				
				result.add(modStaticVo);
			}
		}
		return result;
	}
	public Item listItemTaskDetail(UserInfo curUser, Integer itemId) {
		//验证当前登录人是否是督察人员
		//boolean isForceIn = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_ITEM);
		//1、获取项目
		Item item = itemService.getItemById(itemId, curUser);
		if(!CommonUtil.isNull(item)){
			AttenceRule attenceRule = attenceService.getAttenceRule(null,curUser.getComId());//考勤规则
			List<FestModDate> listFestModDateOfTeam = festModService.listFestModDateOfTeam(curUser.getComId());//获取当前团队下所有的节假日维护信息
			//团队的工作时段
			List<AttenceType> listAttenceTypes = attenceService.listAttenceTypes(curUser.getComId(), attenceRule.getId());
			itemStageInfoCount(curUser, attenceRule,listFestModDateOfTeam,listAttenceTypes,item);//项目阶段信息计算
		}
		return item;
	}
	/**
	 * 初始化报延申请
	 * @param delayApply 申请配置信息
	 * @param userInfo 当前操作人
	 */
	public void initDelayApply(DelayApply delayApply, UserInfo userInfo) {
		delayApply.setComId(userInfo.getComId());//企业号
		delayApply.setFromUser(userInfo.getId());//创建人
		Task task = this.getTaskById(delayApply.getTaskId(),userInfo);
		delayApply.setToUser(task.getOwner());//设置申请人
		Integer applyId = taskDao.add(delayApply);
		//给审批人发送待办事项信息
		UserInfo spUser = new UserInfo();
		spUser.setId(delayApply.getToUser());
		List<UserInfo> shares = new ArrayList<UserInfo>();
		shares.add(spUser);
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,delayApply.getToUser(),applyId,"申请任务办理时限延期到\""+delayApply.getLimitDate()+"\"。", ConstantInterface.TYPE_DELAYAPPLY,shares,null);
		//模块日志添加
		this.addTaskLog(userInfo.getComId(),delayApply.getTaskId(),userInfo.getId(),userInfo.getUserName(),
				"申请任务办理时限延期到\""+delayApply.getLimitDate()+"\"。");
	}
	/**
	 * 查询报延申请详情
	 * @param applyId 申请主键
	 * @param userInfo 当前操作人
	 * @return
	 */
	public DelayApply queryDelayApply(Integer applyId, UserInfo userInfo) {
		return taskDao.queryDelayApply(applyId,userInfo);
	}
	/**
	 * 报延审批
	 * @param delayApply
	 * @param userInfo
	 */
	public void updateDelayApply(DelayApply delayApply, UserInfo userInfo) {
		delayApply.setComId(userInfo.getComId());
		taskDao.update("update delayApply a set a.status=:status,a.spOpinion=:spOpinion where a.comid=:comId and a.id=:id",delayApply);
		if("1".equals(delayApply.getStatus().toString())){//同意
			TaskHandOver taskhandover = new TaskHandOver();
			taskhandover.setHandTimeLimit(delayApply.getLimitDate());
			taskhandover.setExpectTime(delayApply.getLimitDate());
			
			taskhandover.setComId(delayApply.getComId());
			taskhandover.setTaskId(delayApply.getTaskId());
			taskhandover.setToUser(delayApply.getFromUser());
			
			//修改任务的办理时限
			TaskExecutor taskExecutor = new TaskExecutor();
			taskExecutor.setComId(delayApply.getComId());
			taskExecutor.setTaskId(delayApply.getTaskId());
			taskExecutor.setHandTimeLimit(delayApply.getLimitDate());
			taskExecutor.setExpectTime(delayApply.getLimitDate());
			taskExecutor.setExecutor(delayApply.getFromUser());
			
			String taskVersion = delayApply.getTaskVersion();
			if(StringUtils.isNotEmpty(taskVersion) && taskVersion.equals("2")){
				//办理时限变更
				taskDao.update("update taskhandover a set a.expectTime=a.expectTime+:expectTime where a.comid=:comId "
						+ "and a.taskId=:taskId and a.toUser=:toUser and a.curStep=1 and a.endTime is null",taskhandover);
				
				taskDao.update("update taskExecutor a set a.expectTime=a.expectTime+:expectTime where a.comid=:comId"
						+ " and a.taskId=:taskId and a.executor=:executor",taskExecutor);
			}else{
				
				//办理时限变更
				taskDao.update("update taskhandover a set a.handTimeLimit=:handTimeLimit where a.comid=:comId "
						+ "and a.taskId=:taskId and a.toUser=:toUser and a.curStep=1 and a.endTime is null",taskhandover);
				
				taskDao.update("update taskExecutor a set a.handTimeLimit=:handTimeLimit where a.comid=:comId"
						+ " and a.taskId=:taskId and a.executor=:executor",taskExecutor);
				
				//修改任务的完成时限
				Task task = (Task)taskDao.objectQuery(Task.class, taskExecutor.getTaskId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					if(task != null && sdf.parse(task.getDealTimeLimit()).before(sdf.parse(taskExecutor.getHandTimeLimit()))) {//申延时间大于完成时限时修改
						taskDao.update("update task a set a.DEALTIMELIMIT=:handTimeLimit where a.comid=:comId"
								+ " and a.id=:taskId",taskExecutor);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			
			
			
			
		}
		List<UserInfo> shares = new ArrayList<UserInfo>();
		shares.add(userInfo);
		//更新待办提醒通知
		todayWorksService.addTodayWorks(userInfo,-1,delayApply.getId(),"延报审核完成", ConstantInterface.TYPE_DELAYAPPLY,shares,null);
		//模块日志添加
		this.addTaskLog(userInfo.getComId(),delayApply.getTaskId(),userInfo.getId(),userInfo.getUserName(),
				("1".equals(delayApply.getStatus().toString())?"同意":"不同意")+"任务办理时限延期到\""+delayApply.getLimitDate()+"\"。");
		
	}
	/**
	 * 认领任务信息
	 * @param taskId 任务主键
	 * @param sessioUser 当前操作员
	 */
	public Map<String, Object> updateAcceptTask(Integer taskId, UserInfo sessioUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		Task taskObj =  (Task) taskDao.objectQuery(Task.class, taskId);
		
		if(ConstantInterface.STATIS_TASK_STATE_DOING.equals(taskObj.getState().toString())){
			map.put("status", "f");
			map.put("info", "任务已被认领！");
			return map;
		}
		if(ConstantInterface.STATIS_TASK_STATE_DONE.equals(taskObj.getState().toString())){
			map.put("status", "f");
			map.put("info", "任务已完结,无需认领！");
			return map;
		}
		
		//当前操作人员主键
		Integer sessionUserId = sessioUser.getId();
		//所在团队信息
		Integer comId = sessioUser.getComId();
		List<TaskExecutor> taskExecutors = this.listTaskExecutor(taskId, sessioUser);
		if(null != taskExecutors && !taskExecutors.isEmpty()){
			for (TaskExecutor taskExecutor : taskExecutors) {
				//任务执行人主键
				Integer executor = taskExecutor.getExecutor();
				if(!executor.equals(sessionUserId)){//不是当前人员信息
					//删除其他执行人信息
					todayWorksService.delTodoWork(taskId, comId, executor, ConstantInterface.TYPE_TASK, "1");
					//删除任务非自己的执行人
					taskDao.delByField("taskExecutor",new String[]{"comId","taskId","executor"},
							new Object[]{comId,taskId,executor});
					//删除任务当前步骤信息
					taskDao.delByField("taskHandOver",new String[]{"comId","taskId","toUser","curStep"},
							new Object[]{comId,taskId,executor,1});
				}else{
					//修改任务执行状态
					Task task = new Task();
					task.setId(taskId);
					task.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DOING));
					taskDao.update(task);
					
					//修改办理人的任务执行状态
					taskExecutor.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DOING));
					taskExecutor.setTaskProgress(0);
					taskDao.update(taskExecutor);
				}
				
			}
			//更新待办提醒通知
			todayWorksService.addTodayWorks(comId, ConstantInterface.TYPE_TASK, taskId, "认领任务:"+taskObj.getTaskName(),
					taskObj.getOwner(), sessioUser.getId(), 0);
			//添加日志记录
			this.addTaskLog(comId, taskId, sessionUserId, sessioUser.getUserName(), "认领任务:"+taskObj.getTaskName());
		}
		
		map.put("status", "y");
		return map;
		
	}
	/**
	 * 任务附件
	 * @param taskId
	 * @param comId
	 * @return
	 */
	public List<TaskUpfile> listTaskUpfile(Integer taskId,Integer comId){
		return taskDao.listTaskUpfile(taskId,comId);
	}
	
	/**
	 *  人员离职，移交任务
	 * @param userInfo 离职人员
	 * @param userId 接收人
	 * @param userName
	 */
	public void updateFordimission(UserInfo userInfo, Integer userId, String userName) {
		// 任务参与人删除
		taskDao.delByField("taskSharer", new String[] { "comId", "sharerId" },
				new Object[] { userInfo.getComId(), userInfo.getId() });
		// 离职人员负责的任务或是正在执行的任务
		List<Task> tasklist = this.listUserAllTask(userInfo.getComId(), userInfo.getId());
		if (null != tasklist && !tasklist.isEmpty()) {
			for (Task task : tasklist) {
				
				//是否为新版本
				String version =task.getVersion();
				boolean blnPromote = StringUtils.isNotEmpty(version) && "2".equals(version);
				
				// 获取当前执行移交记录
				List<TaskHandOver> taskHandOvers = taskDao.listHandOverForExecute(task.getId(), userInfo.getComId(),
						1);
				Integer preStepId = -1;
				if(!CommonUtil.isNull(taskHandOvers) && !CommonUtil.isNull(taskHandOvers.get(0))){
					preStepId = taskHandOvers.get(0).getId();
				}
				// 任务执行人
				List<TaskExecutor> listTaskExecutor = taskDao.listTaskExecutor(task.getId(), userInfo.getComId());

				// 更新任务记录taskHandOver步骤节点为非当前步骤节点
				TaskHandOver updateTaskHandOver = new TaskHandOver();
				updateTaskHandOver.setCurStep(0);
				updateTaskHandOver.setComId(userInfo.getComId());
				updateTaskHandOver.setTaskId(task.getId());
				updateTaskHandOver.setEndTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
				updateTaskHandOver.setToUser(userInfo.getId());
				if(blnPromote){
					String costTime = taskPromoteService.calDoneExecuteTime(userInfo, task.getId(), userInfo.getId());
					updateTaskHandOver.setCostTime(costTime);
				}
				// 当前人和接收人执行该任务的状态
				String curExecuteState = "0";
				String userExecuteState = "0";
				TaskExecutor curExecutor = new TaskExecutor();

				for (TaskExecutor taskExe : listTaskExecutor) {
					if (taskExe.getExecutor().equals(userInfo.getId()) 
							&& !taskExe.getState().equals(ConstantInterface.STATIS_TASK_STATE_DONE)) {// 离职人该任务在执行中
						curExecuteState = "1";
						curExecutor = taskExe;
					}
					if (taskExe.getExecutor().equals(userId)) {// 接收人该任务在执行中
						userExecuteState = "1";
					}
				}
				// 修改负责人
				if (task.getOwner().equals(userInfo.getId())) {
					// 重设负责人
					task.setOwner(userId);
				}
				// 修改执行人
				if (curExecuteState.equals("1")) {// 任务执行人是离职人员，需要修改执行人为移交人员
					if (task.getState().equals(Integer.valueOf(ConstantInterface.STATIS_TASK_STATE_CHOOSE))) {// 未认领
						// 删除离职人员handOver
						taskDao.delByField("taskHandOver", new String[] { "comId", "taskId", "toUser" },
								new Object[] { userInfo.getComId(), task.getId(), userInfo.getId()});
						
						if( userExecuteState.equals("1")){// 移交人员是执行人员
							// 删除离职人员执行
							taskDao.delByField("taskExecutor", new String[] { "comId", "taskId", "executor" },
									new Object[] { userInfo.getComId(), task.getId(), userInfo.getId() });
							if (listTaskExecutor.size() == 2) {// 没有其他执行人,执行人员直接认领
								// 修改办理人的任务执行状态
								TaskExecutor userTaskExecutor = new TaskExecutor();
								userTaskExecutor.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DOING));
								userTaskExecutor.setTaskProgress(0);
								userTaskExecutor.setComId(userInfo.getComId());
								userTaskExecutor.setTaskId(task.getId());
								userTaskExecutor.setExecutor(userId);

								// 更改执行人认领
								taskDao.update("update taskExecutor set state=:state where comId=:comId and taskId=:taskId "
										+ "and executor=:executor", userTaskExecutor);
								
								// 更改任务认领状态
								task.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DOING));
							}
						}else{
							TaskHandOver taskHandOver = new TaskHandOver();
							// 企业号
							taskHandOver.setComId(userInfo.getComId());
							// 客户主键
							taskHandOver.setTaskId(task.getId());
							// 原有人员
							taskHandOver.setFromUser(task.getOwner());
							// 现有人员
							taskHandOver.setToUser(userId);
							// 设置新记录步骤节点为当前步骤节点
							taskHandOver.setCurStep(1);
							taskHandOver.setPreStep(-1);
							
							if(!CommonUtil.isNull(taskHandOvers) 
									&& !CommonUtil.isNull(taskHandOvers.get(0))
									&& blnPromote){
								TaskHandOver preTaskHandOver= taskHandOvers.get(0);
								
								taskHandOver.setStepTag(preTaskHandOver.getStepTag());
								taskHandOver.setExpectTime(preTaskHandOver.getExpectTime());
							}
							
							
							// 添加移交记录
							taskDao.add(taskHandOver);

							// 更改执行人
							curExecutor.setExecutor(userId);
							taskDao.update(curExecutor);
						}
					}else if ((task.getState().equals(Integer.valueOf(ConstantInterface.STATIS_TASK_STATE_DOING))
							||task.getState().equals(Integer.valueOf(ConstantInterface.STATIS_TASK_STATE_PAUSE)))
							&& task.getTaskType().equals("2")) {// 协同任务
						
						taskDao.update(" update taskHandOver set curStep=:curStep,endTime=:endTime,costTime=:costTime where taskId=:taskId "
								+ "and comId=:comId and toUser=:toUser", updateTaskHandOver);
						
						if(userExecuteState.equals("1")){
							taskDao.delByField("taskExecutor", new String[] { "comId", "taskId", "executor" },
									new Object[] { userInfo.getComId(), task.getId(), userInfo.getId() });
						}else{
							TaskHandOver taskHandOver = new TaskHandOver();
							// 企业号
							taskHandOver.setComId(userInfo.getComId());
							// 客户主键
							taskHandOver.setTaskId(task.getId());
							// 原有人员
							taskHandOver.setFromUser(task.getOwner());
							// 现有人员
							taskHandOver.setToUser(userId);
							// 设置新记录步骤节点为当前步骤节点
							taskHandOver.setCurStep(1);
							taskHandOver.setPreStep(preStepId);
							taskHandOver.setActHandleState(1);
							
							if(!CommonUtil.isNull(taskHandOvers) 
									&& !CommonUtil.isNull(taskHandOvers.get(0))
									&& blnPromote){
								TaskHandOver preTaskHandOver= taskHandOvers.get(0);
								
								taskHandOver.setStepTag(preTaskHandOver.getStepTag());
								taskHandOver.setExpectTime(preTaskHandOver.getExpectTime());
							}
							
							// 添加移交记录
							taskDao.add(taskHandOver);

							// 更改执行人
							curExecutor.setExecutor(userId);
							taskDao.update(curExecutor);
						}
							
					} else {
						taskDao.update(" update taskHandOver set curStep=:curStep,endTime=:endTime,costTime=:costTime where taskId=:taskId "
								+ "and comId=:comId and toUser=:toUser", updateTaskHandOver);
						TaskHandOver taskHandOver = new TaskHandOver();
						// 企业号
						taskHandOver.setComId(userInfo.getComId());
						// 客户主键
						taskHandOver.setTaskId(task.getId());
						// 原有人员
						taskHandOver.setFromUser(task.getOwner());
						// 现有人员
						taskHandOver.setToUser(userId);
						// 设置新记录步骤节点为当前步骤节点
						taskHandOver.setCurStep(1);
						taskHandOver.setPreStep(preStepId);
						taskHandOver.setActHandleState(1);
						
						if(!CommonUtil.isNull(taskHandOvers) 
								&& !CommonUtil.isNull(taskHandOvers.get(0))
								&& blnPromote){
							TaskHandOver preTaskHandOver= taskHandOvers.get(0);
							
							taskHandOver.setStepTag(preTaskHandOver.getStepTag());
							taskHandOver.setExpectTime(preTaskHandOver.getExpectTime());
						}
						
						// 添加移交记录
						taskDao.add(taskHandOver);

						// 更改执行人
						curExecutor.setExecutor(userId);
						taskDao.update(curExecutor);
					}
				}
				// 任务移交
				taskDao.update(task);
				// 添加日志
				this.addTaskLog(userInfo.getComId(), task.getId(), userInfo.getId(), userInfo.getUserName(),
						"离职时把任务移交给了\"" + userName + "\"");
				// 若是任务没有完成，则进行提示
				if (null != task.getState() && task.getState() == 1) {
					// 任务的所有查看人
					List<UserInfo> shares = taskDao.listTaskOwnersNoForce(userInfo.getComId(), task.getId());
					// 添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo, userId, task.getId(),
							userInfo.getUserName() + "离职时把任务移交给了\"" + userName + "\"", ConstantInterface.TYPE_TASK,
							shares, null);

				}
			}
		}

	}
	
	/**
	 * 查询用于
	 * @param userInfo
	 * @param taskId
	 * @param busType
	 * @return
	 */
	public Map<String, Object> queryRemindConf(UserInfo userInfo,
			Integer taskId) {
		Map<String, Object> map = new HashMap<String,Object>();
		Task task = taskDao.queryTaskById(taskId, userInfo);
		map.put("busModName", task.getTaskName());
		if(null!=task && 4==task.getState()){//事项已经结束
			map.put("status", "f");
			map.put("info","事项已经结束！");
		}else{
			map.put("status", "y");
			String defMsg = "请尽快办理“"+task.getTaskName()+"”事项！";
			if(null!=task.getDealTimeLimit() || null!=task.getHandTimeLimit()){
				String handTime = null == task.getDealTimeLimit()?task.getHandTimeLimit():task.getDealTimeLimit();
				defMsg = "”"+task.getTaskName()+"“事项办理时限为”"+handTime+"“,请尽快办理！";
			}
			map.put("defMsg", defMsg);
			
			//事项的执行人员信息
			List<BusRemindUser> listReminderUser = this.listTaskRemindExecutor(taskId, userInfo);
			map.put("listRemindUser", listReminderUser);
		}
		return map;
	}
	/**
	 * 获取可以催办的事项执行人
	 * @param taskId
	 * @param userInfo
	 * @return
	 */
	public List<BusRemindUser> listTaskRemindExecutor(Integer taskId,UserInfo userInfo){
		return taskDao.listTaskRemindExecutor(taskId,userInfo);
	}
	

	/*****************************任务统计*****************************/
	/**
	 * 用于任务统计的数据
	 * @param userInfo
	 * @param statisticsVo
	 * @param version 
	 * @return
	 */
	public PageBean<StatisticTaskVo> listPagedStatisticTask(UserInfo userInfo,
			StatisticTaskVo statisticsVo, String version) {
		List<StatisticTaskVo> recordList = taskDao.listPagedDoingTask(userInfo,statisticsVo,version);
		PageBean<StatisticTaskVo> pageBean = new PageBean<StatisticTaskVo>();
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
}
