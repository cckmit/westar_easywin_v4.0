package com.westar.core.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.westar.base.model.Customer;
import com.westar.base.model.Department;
import com.westar.base.model.Item;
import com.westar.base.model.StagedInfo;
import com.westar.base.model.Task;
import com.westar.base.model.TaskExecuteTime;
import com.westar.base.model.TaskExecutor;
import com.westar.base.model.TaskHandOver;
import com.westar.base.model.TaskLog;
import com.westar.base.model.TaskSharer;
import com.westar.base.model.TaskTalk;
import com.westar.base.model.TaskTalkUpfile;
import com.westar.base.model.TaskUpfile;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.MathExtend;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.TaskPromoteDao;
import com.westar.core.thread.IndexUpdateThread;

@Service
public class TaskPromoteService {
	
	@Autowired
	TaskPromoteDao taskPromoteDao;
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
	     
	    List<Task> listPagedeTask = taskPromoteDao.listPageTask(taskParam,userInfo,isForceInPersion);
	    if(null!=listPagedeTask && !listPagedeTask.isEmpty()){
	    	for (Task task : listPagedeTask) {
				List<Task> taskWithExector = taskPromoteDao.listTaskForExecutor(task.getId(),taskParam,userInfo,isForceInPersion);
				if(null!= taskWithExector && !taskWithExector.isEmpty()){
					for (Task taskDetail : taskWithExector) {
						Task result = new Task();
						BeanUtils.copyProperties(task,result);
						
						result.setExecutor(taskDetail.getExecutor());
						result.setExecutorName(taskDetail.getExecutorName());
						result.setDealTimeLimit(taskDetail.getDealTimeLimit());
						result.setTaskProgress(taskDetail.getTaskProgress());
						result.setRemainingTime(taskDetail.getRemainingTime());
						result.setCurStartTime(taskDetail.getCurStartTime());
						result.setExecuteState(taskDetail.getExecuteState());
						result.setOverDueLevel(taskDetail.getOverDueLevel());
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
		List<Task>  listPagedTaskForSupView = taskPromoteDao.listPagedTaskForSupView(task,depIds,subUserIds,userInfo,isForceInPersion);
		
		if(null!=listPagedTaskForSupView && !listPagedTaskForSupView.isEmpty()){
			for (Task viewTask : listPagedTaskForSupView) {
				
				List<Task> obj = taskPromoteDao.listTaskExecutorForSupView(viewTask.getId(),task,depIds,subUserIds,userInfo,isForceInPersion);
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
		return taskPromoteDao.allTaskCount(task,userInfo,isForceInPersion);
	}
	/**
	 * 获取个人权限下的所有任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Task> listTaskOfAll(Task task,UserInfo userInfo,boolean isForceInPersion){
		return taskPromoteDao.listTaskOfAll(task,userInfo,isForceInPersion);
	}
	/**
	 * 获取团队任务主键集合
	 * @param userInfo
	 * @return
	 */
	public List<Task> listTaskOfAll(UserInfo userInfo){
		return taskPromoteDao.listTaskOfAll(userInfo);
	}
	/**
	 * 获取个人权限下的所有逾期任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Task> listOverdueTaskOfAll(Task task,UserInfo userInfo,boolean isForceInPersion){
		return taskPromoteDao.listOverdueTaskOfAll(task,userInfo,isForceInPersion);
	}
	/**
	 * 分页获取逾期任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Task> listOverdueTask(Task task,UserInfo userInfo,boolean isForceInPersion){
		return taskPromoteDao.listOverdueTask(task,userInfo,isForceInPersion);
	}
	/**
	 * 获取个人待办任务集合
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> taskToDoList(Task task,UserInfo userInfo){
		return taskPromoteDao.taskToDoList(task,userInfo);
	}
	/***
	 * 获取所有的待办任务
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> taskToDoListOfAll(Task task,UserInfo userInfo){
		return taskPromoteDao.taskToDoListOfAll(task,userInfo);
	}
	/**
	 * 分页查询负责的任务
	 * @param task
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Task> listPageChargeTask(Task task,UserInfo userInfo){
		return taskPromoteDao.listPageChargeTask(task,userInfo);
	}
	/**
	 * 我负责的任务统计
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> chargeTaskCount(Task task,UserInfo userInfo){
		return taskPromoteDao.chargeTaskCount(task,userInfo);
	}
	/**
	 * 获取所有的自己负责人任务
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> listChargeTaskOfAll(Task task,UserInfo userInfo){
		return taskPromoteDao.listChargeTaskOfAll(task,userInfo);
	}
	/**
	 * 取得任务的执行人信息，用于复制任务显示执行人
	 * @param taskId 任务主键
	 * @param sessioUser 当前操作员
	 * @return
	 */
	public List<TaskExecutor> listTaskExecutor(Integer taskId,UserInfo sessioUser){
		//任务办理人集合
		List<TaskExecutor> listTaskExecutor = taskPromoteDao.listTaskExecutor(taskId, sessioUser.getComId());
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
			List<Task> listTask = taskPromoteDao.authorCheck(userInfo.getComId(),taskId,userInfo.getId());
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
		
		Task taskTemp = (Task) taskPromoteDao.objectQuery(Task.class, task.getId());
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
							TaskHandOver taskHandOver = taskPromoteDao.queryCurUserHandInfo(userInfo, task.getId());
							taskHandOver.setCurStep(-1);
							taskHandOver.setEndTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
							taskPromoteDao.update(taskHandOver);
						}
					}
					taskPromoteDao.update(selfExector);
				}else{
					selfExector.setTaskProgress(subProgress);
					taskPromoteDao.update(selfExector);
				}
				
				
				//任务总的进度信息
				Integer taskProgress = taskProgressTotlal / taskExecutors.size();
				task.setTaskProgress(taskProgress);
				//更新任务进度
				taskPromoteDao.taskProgressReport(task);
				
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
				List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
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
			taskPromoteDao.update(task);
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
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
			//完成时限更新
			taskPromoteDao.taskDealTimeLimitUpdate(task);
			taskPromoteDao.taskExecuteTimeLimitUpdate(task);
			taskPromoteDao.taskOverTimeLimitUpdate(task);
			//用于查询任务的执行人
			Task taskT = (Task) taskPromoteDao.objectQuery(Task.class, task.getId());
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,taskT.getExecutor(), task.getId(), "变更任务时限为\""+task.getDealTimeLimit()+"\"", ConstantInterface.TYPE_TASK, shares,null);
			//更新任务索引
//			this.updateTaskIndex(task.getId(),userInfo,"update");
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "变更任务时限为\""+task.getDealTimeLimit()+"\"成功");
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "变更任务时限为\""+task.getDealTimeLimit()+"\"失败");
			
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
			taskPromoteDao.taskNameUpdate(task);
			//用于查询任务的执行人
			Task taskT = (Task) taskPromoteDao.objectQuery(Task.class, task.getId());
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
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
			taskPromoteDao.taskTaskRemarkUpdate(task);
			//用于查询任务的执行人
			Task taskT = (Task) taskPromoteDao.objectQuery(Task.class, task.getId());
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
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
	 * 删除任务的项目阶段关联
	 * @param userInfo 当前操作人员
	 * @param taskT 当前任务
	 * @throws Exception 
	 */
	public void delStageTask(UserInfo userInfo, Integer taskId) throws Exception{
		//取得当前任务的所有子任务
		List<Task> sonTasks = taskPromoteDao.listTaskOfAllOnlyChildren(taskId, userInfo.getComId());
		if(null!=sonTasks && sonTasks.size()>0){//存在子任务
			for (Task objTask : sonTasks) {
				//删除子任务的项目阶段关联
				taskPromoteDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"}, new Object[]{userInfo.getComId(),objTask.getId(),"task"});
			}
		}
		//删除任务的项目阶段关联
		taskPromoteDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"}, new Object[]{userInfo.getComId(), taskId,"task"});
		//更新任务索引
//		this.updateTaskIndex(taskId,userInfo,"update");
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
			taskPromoteDao.taskParentIdUpdate(task);
			//用于查询任务的执行人
			Task taskT = (Task) taskPromoteDao.objectQuery(Task.class, task.getId());
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
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
			Task taskVo = (Task) taskPromoteDao.objectQuery(Task.class, task.getId());
			//把需要清零的字段用临时变量itemId存储起来
//			Integer busId = taskVo.getBusId();
			String busType = taskVo.getBusType();
			
			//项目关联字段更新为0
			task.setBusId(0);
			//重新设置关联类型
			task.setBusType("0");
			//项目关联
			taskPromoteDao.taskBusIdUpdate(task);
			
			if(busType.equals(ConstantInterface.TYPE_ITEM)){//关联是项目
				//删除任务在项目阶段明细
				List<StagedInfo> listStagedInfo = itemService.listStagedInfoBymoduleIdAndType(task.getComId(),task.getId(),"task");
				if(null!=listStagedInfo){
					for(StagedInfo vo:listStagedInfo){
						taskPromoteDao.delByField("stagedInfo", new String[]{"comId","id"}, new Object[]{vo.getComId(),vo.getId()});
						//模块日志添加
						itemService.addItemLog(vo.getComId(),vo.getItemId(),userInfo.getId(), userInfo.getUserName(),"解除与任务\""+taskVo.getTaskName()+"\"的关联关系");
					}
				}
				//更新项目索引
//				itemService.updateItemIndex(busId,userInfo,"update");
			}
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			
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
				List<Task> listSonTask = taskPromoteDao.listTaskOfAllOnlyChildren(task.getId(),userInfo.getComId());
				if(null!=listSonTask && !listSonTask.isEmpty()){
					for(Task sonTask:listSonTask){
						if(sonTask.getState()==4 && sonTask.getDelState()==0){//若是子任务已办结，不用发送消息
							continue;
						}
						List<TaskExecutor> executors = taskPromoteDao.listTaskExecutor(sonTask.getId(), userInfo.getComId());
						Map<Integer,String> executorCostTimeMap = new HashMap<Integer,String>();
						for (TaskExecutor taskExecutor : executors) {
							Integer executor = taskExecutor.getExecutor();
							String costTime = this.calDoneExecuteTime(userInfo, sonTask.getId(), taskExecutor.getExecutor());
							executorCostTimeMap.put(executor, costTime);
						}
						//删除子任务的执行人
						taskPromoteDao.delByField("taskExecutor", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),sonTask.getId()});
						//子任务办结设置节点的办结时间
						this.updateFinishTaskHandOver(sonTask,userInfo,executorCostTimeMap);
						
						//取得所有设置了提醒的闹铃(删除所有的)
						clockService.delClockByType(userInfo.getComId(),sonTask.getId(),ConstantInterface.TYPE_TASK);
						
						//设置任务办结时间
						this.updateTaskFinishTime(finishDateTime, sonTask.getId());
						
						//任务标记
						sonTask.setState(task.getState());
						sonTask.setTaskProgress(100);//进度100%
						taskPromoteDao.remarkTaskState(sonTask);
						taskPromoteDao.updateTaskExecuteTime(sonTask);
						if(sonTask.getDelState()==0){//向任务成员发送消息
							//任务的负责人，执行人以及关注人员
							List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), sonTask.getId(),true);
							//子项变动发送消息
							todayWorksService.addTodayWorks(userInfo, -1, sonTask.getId(), "任务标记为‘"+stateName+"’",
									ConstantInterface.TYPE_TASK, shares, null);
						}
						//模块日志添加
						this.addTaskLog(userInfo.getComId(),sonTask.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+stateName+"\"成功");
					}
				}
			}
			
			List<TaskExecutor> executors = taskPromoteDao.listTaskExecutor(task.getId(), userInfo.getComId());
			Map<Integer,String> executorCostTimeMap = new HashMap<Integer,String>();
			for (TaskExecutor taskExecutor : executors) {
				Integer executor = taskExecutor.getExecutor();
				String costTime = this.calDoneExecuteTime(userInfo, task.getId(), taskExecutor.getExecutor());
				executorCostTimeMap.put(executor, costTime);
			}
			//删除任务的执行人
			taskPromoteDao.delByField("taskExecutor", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),task.getId()});
			//任务办结设置节点的办结时间
			this.updateFinishTaskHandOver(task,userInfo,executorCostTimeMap);
			
			//设置任务办结时间
			this.updateTaskFinishTime(finishDateTime, task.getId());
			
			//任务标记完成
			task.setTaskProgress(100);//进度100%
			taskPromoteDao.remarkTaskState(task);
			taskPromoteDao.updateTaskExecuteTime(task);
			//取得所有设置了提醒的闹铃(删除所有的)
			clockService.delClockByType(userInfo.getComId(),task.getId(),ConstantInterface.TYPE_TASK);
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,-1, task.getId(),"任务标记为‘"+stateName+"’", ConstantInterface.TYPE_TASK, shares,null);
			if(task.getState()==4){//结束任务加分
				Task taskT = (Task) taskPromoteDao.objectQuery(Task.class, task.getId());
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
			taskPromoteDao.update(sql.toString(),task);
		}else{
			//任务办结
			Task task = new Task();
			task.setId(taskId);
			task.setFinishTime(finishDateTime);
			taskPromoteDao.update(task);
		}
	}
	/**
	 * 办结任务信息
	 * @param task
	 * @param userInfo
	 */
	private void updateFinishTaskHandOver(Task task, UserInfo userInfo, Map<Integer, String> executorCostTimeMap) {
		//最后办结的人员
		List<TaskHandOver> taskHandOvers = taskPromoteDao.listHandOverForExecute(task.getId(), userInfo.getComId(), 1);
		if(null!=taskHandOvers && !taskHandOvers.isEmpty()){
			for (TaskHandOver taskHandOver : taskHandOvers) {
				Integer executor = taskHandOver.getToUser();
				String costTime = executorCostTimeMap.get(executor);
				
				taskHandOver.setCurStep(-1);
				taskHandOver.setActHandleState(1);
				taskHandOver.setEndTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
				taskHandOver.setCostTime(costTime);
				taskPromoteDao.update(taskHandOver);
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
				List<Task> listSonTask = taskPromoteDao.listTaskOfAllOnlyChildren(task.getId(),task.getComId());
				if(null!=listSonTask && !listSonTask.isEmpty()){
					for(Task sonTask:listSonTask){
						sonTask.setState(task.getState());
						//任务标记
						taskPromoteDao.remarkTaskState(sonTask);
						taskPromoteDao.updateTaskExecuteTime(sonTask);
						//删除最后的执行人员
						taskPromoteDao.delByField("TaskExecutor", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),sonTask.getId()});
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
			List<Task> listParentTask = taskPromoteDao.listTaskOfAllParent(task.getId(),userInfo.getComId());
			if(null!=listParentTask && listParentTask.size()>0){
				for (Task pTask : listParentTask) {
					if(1==pTask.getState() && pTask.getDelState()==0){//任务是开启的,没有预删除，不发送消息
						continue;
					}
					//删除最后的执行人员
					taskPromoteDao.delByField("TaskExecutor", new String[]{"comId","taskId"}, new Object[]{userInfo.getComId(),pTask.getId()});
					//最后任务办理人员
					List<TaskExecutor> lastExecutors = this.addTaskExectorFormHandOver(pTask);
					if(ConstantInterface.STATIS_TASK_STATE_PAUSE.equals(pTask.getState().toString())){
						pTask.setState(1);
					}
					//任务标记
					taskPromoteDao.remarkTaskState(pTask);
					
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
		List<TaskHandOver> lastExecutors = taskPromoteDao.listHandOverForLastExecute(task.getId(),task.getComId(),null);
		if(null!=lastExecutors && !lastExecutors.isEmpty()){
			//任务类型
			String taskType = task.getTaskType();
			//设置任务状态默认是办暂停
			String taskState = ConstantInterface.STATIS_TASK_STATE_PAUSE;
			if(taskType.equals(ConstantInterface.TASK_TYPE_CHOOSE)//认领的任务有多人办理，则需要认领
					&& lastExecutors.size()>1){
				taskState = ConstantInterface.STATIS_TASK_STATE_CHOOSE;
			}
			task.setState(Integer.parseInt(taskState));
			
			for (TaskHandOver taskHandOver : lastExecutors) {
				
				String stepTag = taskHandOver.getStepTag();
				String expectTime = taskHandOver.getExpectTime();
				
				String taskExecuteState = taskState;
				Integer curStep = taskHandOver.getCurStep();
				Integer totalProgess = task.getTaskProgress();
				if(curStep == -1 || curStep == 1 ){
					//重启执行节点信息
					taskHandOver.setCurStep(1);
					taskPromoteDao.update("update taskHandOver a set a.endTime=null,a.costTime=null,a.curStep=:curStep where a.id=:id", taskHandOver);
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
				
				taskExecutor.setExpectTime(expectTime);
				taskExecutor.setStepTag(stepTag);
				//添加办理人员
				taskPromoteDao.add(taskExecutor);
				
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
			List<Task> listSonTask = taskPromoteDao.listTaskOfAllOnlyChildren(task.getId(),task.getComId());
			if(null!=listSonTask && !listSonTask.isEmpty()){
				for(Task sonTask:listSonTask){
					sonTask.setState(task.getState());
					taskPromoteDao.remarkTaskState(sonTask);//任务暂停
					taskPromoteDao.updateTaskExecuteTime(sonTask);
					//取消任务办结时间
					this.updateTaskFinishTime(null, sonTask.getId());
					
					List<TaskExecutor> executors = taskPromoteDao.listTaskExecutor(sonTask.getId(), userInfo.getComId());
					Map<Integer,String> executorCostTimeMap = new HashMap<Integer,String>();
					for (TaskExecutor taskExecutor : executors) {
						
						Integer executor = taskExecutor.getExecutor();
						String costTime = this.calDoneExecuteTime(userInfo, sonTask.getId(), taskExecutor.getExecutor());
						executorCostTimeMap.put(executor, costTime);
					}
					//子任务办结设置节点的办结时间
					this.updateFinishTaskHandOver(sonTask,userInfo,executorCostTimeMap);
					
					
					//向未删除的任务成员发送消息
					if(sonTask.getDelState()==0){
						//任务的负责人，执行人以及关注人员
						List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), sonTask.getId(),true);
						//查询子任务最后的办结人
						List<TaskHandOver> lastExecutors = taskPromoteDao.listHandOverForExecute(sonTask.getId(),task.getComId(),null);
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
			taskPromoteDao.remarkTaskState(task);//任务暂停
			taskPromoteDao.updateTaskExecuteTime(task);
			List<TaskExecutor> executors = taskPromoteDao.listTaskExecutor(task.getId(), userInfo.getComId());
			Map<Integer,String> executorCostTimeMap = new HashMap<Integer,String>();
			for (TaskExecutor taskExecutor : executors) {
				Integer executor = taskExecutor.getExecutor();
				String costTime = this.calDoneExecuteTime(userInfo, task.getId(), taskExecutor.getExecutor());
				executorCostTimeMap.put(executor, costTime);
			}
			//子任务办结设置节点的办结时间
			this.updateFinishTaskHandOver(task,userInfo,executorCostTimeMap);
			
			//取消任务办结时间
			this.updateTaskFinishTime(null, task.getId());
			
			//任务的负责人，执行人以及关注人员
			List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(userInfo.getComId(), task.getId(),true);
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
		Integer id = taskPromoteDao.add(taskTalk);
		//查询任务信息
		Task task = (Task) taskPromoteDao.objectQuery(Task.class, taskTalk.getTaskId());
		
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
				
				taskPromoteDao.add(upfiles);
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
				taskPromoteDao.delByField("taskSharer", new String[]{"comId","taskId","sharerId"},
						new Object[]{userInfo.getComId(),taskTalk.getTaskId(),userId});
                taskSharer.setTaskId(taskTalk.getTaskId());
                taskSharer.setComId(userInfo.getComId());
                taskPromoteDao.add(taskSharer);
			}
		}

		//分享信息查看
		List<UserInfo> taskShares = new ArrayList<UserInfo>();
		
		if (null != task) {
			//查询消息的推送人员
			taskShares = taskPromoteDao.listPushTodoUserForTask(taskTalk.getTaskId(), userInfo.getComId(),pushUserIdSet);
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
		TaskTalk taskTalk = taskPromoteDao.queryTaskTalk(id,comId);
		//任务留言的附件
		if(null!=taskTalk){
			taskTalk.setListTaskTalkFile(taskPromoteDao.listTaskTalkFile(comId,taskTalk.getTaskId(),id));
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
		List<TaskTalk> listTaskTalk = taskPromoteDao.listTaskTalk(taskId,comId);
		//需要返回的结果信息
		List<TaskTalk> list = new ArrayList<TaskTalk>();
		if(null!=listTaskTalk && !listTaskTalk.isEmpty()){
			//查询留言的所有附件信息
			List<TaskTalkUpfile> talkUpfiles = taskPromoteDao.listTaskTalkFile(comId, taskId, null);
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
		taskPromoteDao.add(taskLog);
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
		Task task = taskPromoteDao.queryTaskById(taskId,userInfo);
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
		List<TaskSharer> listCurTaskSharer = taskPromoteDao.listTaskOwners(comId,taskId);
		//当前任务参与人MAP
		Map<Integer,Integer> sharerMap = null;
		//获取任务的所有后代任务
		List<Task> listTaskOfChildren = taskPromoteDao.listTaskOfOnlyChildren(taskId,comId);
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
				listTaskOwners = taskPromoteDao.listTaskOwners(comId,vo.getId());
				if(null!=listTaskOwners && !listTaskOwners.isEmpty()){
					for(TaskSharer sharer:listTaskOwners){
						sharerMap.put(sharer.getSharerId(),comId);
					}
					//先删除任务的参与人
					taskPromoteDao.delByField("taskSharer", new String[]{"comId","taskId"},new Integer[]{comId,vo.getId()});
					//把父任务所有相关人员添加为子任务的参与人
					for(Map.Entry<Integer,Integer> entry:sharerMap.entrySet()){    
					    taskSharer = new TaskSharer();
						taskSharer.setComId(comId);
						taskSharer.setTaskId(vo.getId());
						taskSharer.setSharerId(entry.getKey());
						taskPromoteDao.add(taskSharer);    
					}
				}
			}
		}
	}
	
	/**
	 * 负责人或是执行人的任务
	 * @param comId 企业号
	 * @param userId 用户主键
	 * @return
	 */
	public List<Task> listUserAllTask(Integer comId, Integer userId) {
		return taskPromoteDao.listUserAllTask(comId,userId);
	}
	
	/**
	 * 查看任务移交记录
	 * @param taskId 任务主键
	 * @param comId 企业号
	 * @return
	 */
	public List<FlowRecord> listFlowRecord(Integer taskId, Integer comId) {
		return taskPromoteDao.listFlowRecord(taskId,comId);
	}
	
	/**
	 * 任务详情状态（仅需要纯粹的任务信息）
	 * @param taskId
	 * @return
	 */
	public Task getTaskById(Integer taskId) {
		return (Task) taskPromoteDao.objectQuery(Task.class, taskId);
	}
	/**
	 * 任务详情状态（不需要任务参与人）
	 * @param taskId
	 * @param sessioUser
	 * @return
	 */
	public Task getTaskById(Integer taskId, UserInfo sessioUser) {
		Task task = taskPromoteDao.queryTaskById(taskId, sessioUser);
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
		Task task = (Task) taskPromoteDao.objectQuery(Task.class,taskId);

		//获取任务关联
		Task taskT = taskPromoteDao.getTaskBusInfo(taskId,sessioUser);
		//设置任务关联
		task.setBusName(taskT.getBusName());

		//任务的执行人员信息
//		List<TaskExecutor> listTaskExecutor = this.listTaskExecutor(taskId, sessioUser);
//		task.setListTaskExecutor(listTaskExecutor);

        //获取任务附件
        List<TaskUpfile> listUpfile = taskPromoteDao.listTaskUpfile(taskId,sessioUser.getComId());
        List<Upfiles> upfiles = new ArrayList<Upfiles>();
        Upfiles upfile = null;
        for(TaskUpfile taskUpfile : listUpfile){
            upfile = new Upfiles();
            upfile.setFilename(taskUpfile.getFilename());
            upfile.setFileExt(taskUpfile.getFileExt());
            upfile.setOwnerName(taskUpfile.getCreatorName());
            upfile.setId(taskUpfile.getUpfileId());
            upfile.setComId(sessioUser.getComId());
            upfile.setSizem(taskUpfile.getSizem());
            upfiles.add(upfile);
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
		return taskPromoteDao.listSonTask(taskId,comid);
	}
	/**
	 * 查询任务当前任务的父节点，不包括自己
	 * @param parentId
	 * @param comId
	 * @return
	 */
	public List<Task> getAllParentTask(Integer parentId, Integer comId) {
		List<Task> list = taskPromoteDao.getAllParentTask(parentId,comId);
		return list;
	}
	/**
	 * 查询任务当前设置父节点的父节点,包括自己
	 * @param parentId
	 * @param comId
	 * @return
	 */
	public List<Task> listTaskOfAllParent(Integer parentId, Integer comId) {
		List<Task> list = taskPromoteDao.listTaskOfAllParent(parentId,comId);
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
		List<UserInfo> shares = taskPromoteDao.listTaskUserForMsg(comId, modId,true);
		return shares;
	}
	
	/**
	 * 取得任务的关联项目以及项目阶段名称
	 * @param taskId 任务主键
	 * @param userInfo 操作人员
	 * @return
	 */
	public Task getTaskBusInfo(Integer taskId, UserInfo userInfo) {
		Task task = taskPromoteDao.getTaskBusInfo(taskId,userInfo);
		return task;
	}
	
	/*****************************任务统计 ******************************/
	
	
	/**
	 * 添加任务的执行时间表
	 * @param sessionUser 当前操作人员
	 * @param taskId 任务主键
	 * @param stepTag 任务执行办理节点标识
	 * @param executor 执行人
	 */
	public void addTaskExecuteTime(Integer comId,Integer taskId,
			String stepTag,Integer executor){
		TaskExecuteTime taskExecuteTime = new TaskExecuteTime();
		taskExecuteTime.setComId(comId);
		taskExecuteTime.setTaskId(taskId);
		taskExecuteTime.setExecutor(executor);
		taskExecuteTime.setStepTag(stepTag);
		taskPromoteDao.add(taskExecuteTime);
	}
	/**
	 * 任务办理完成
	 * @param sessionUser
	 * @param taskId
	 * @param executor
	 */
	public String calDoneExecuteTime(UserInfo sessionUser,Integer taskId,
			Integer executor){
		//查询当前人员的的执行记录文件
		List<TaskExecuteTime> listTaskExecuteTime = taskPromoteDao.listTaskExecuteTime(sessionUser,taskId,executor);
		//
		String strCostTime = "0";
		if(null != listTaskExecuteTime && !listTaskExecuteTime.isEmpty()){
			String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			Long timeMiles = 0L;
			
			for (TaskExecuteTime taskExecuteTime : listTaskExecuteTime) {
				String recordCreateTime = taskExecuteTime.getRecordCreateTime();
				recordCreateTime = recordCreateTime.substring(0,16);
				
				String endTime = taskExecuteTime.getEndTime();
				if(StringUtils.isEmpty(endTime)){
					endTime = nowDateTime;
				}
				endTime = endTime.substring(0,16);
				
				
				Long startDateTime = DateTimeUtil.parseDate(recordCreateTime, DateTimeUtil.yyyy_MM_dd_HH_mm).getTime();
				Long endDateTime = DateTimeUtil.parseDate(endTime, DateTimeUtil.yyyy_MM_dd_HH_mm).getTime();
				
				
				timeMiles = timeMiles + (endDateTime - startDateTime);
				
			}
			Long second = timeMiles/(1000);
			int min = (int) (second/(60));
			BigDecimal bd = new BigDecimal(min / 60.0D);
			bd  =   bd.setScale(1,BigDecimal.ROUND_HALF_UP); 
			
			strCostTime = MathExtend.add(strCostTime, bd.toString());
		}
		return strCostTime;
	}
	
	/**
	 * 修改任务的执行状态
	 * @param task
	 * @param userInfo
	 * @param stateName
	 * @return
	 */
	public boolean updateStarExecuteTask(Integer taskId, UserInfo sessionUser, String stateName){
		//查询其他正在办理任务，暂停一下
		//查询当前人员的的执行记录文件
		List<TaskExecuteTime> listTaskExecuteTime = taskPromoteDao.listTaskExecuteTimeForPause(sessionUser,null,sessionUser.getId());
		if(null != listTaskExecuteTime && !listTaskExecuteTime.isEmpty()){
			String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
			for (TaskExecuteTime taskExecuteTime : listTaskExecuteTime) {
				
				taskExecuteTime.setEndTime(nowDateTime);
				taskPromoteDao.update(taskExecuteTime);
				
				//暂停执行人的
				TaskExecutor taskExecutor = new TaskExecutor();
				taskExecutor.setComId(sessionUser.getComId());
				taskExecutor.setExecutor(sessionUser.getId());
				taskExecutor.setTaskId(taskExecuteTime.getTaskId());
				taskExecutor.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_PAUSE));
				taskPromoteDao.update("update taskExecutor set state=:state where comId=:comId and taskId=:taskId and executor=:executor and state != 4", taskExecutor);
				
				//添加日志
				this.addTaskLog(sessionUser.getComId(), taskId, sessionUser.getId(), sessionUser.getUserName(), "暂停执行该任务！");
			}
		}
		//查询所有的执行人
		List<TaskExecutor> executors = taskPromoteDao.listTaskExecutor(taskId, sessionUser.getComId());
		String stepTag = executors.get(0).getStepTag();
		this.addTaskExecuteTime(sessionUser.getComId(), taskId, stepTag, sessionUser.getId());
		
		//开始执行人的
		TaskExecutor taskExecutor = new TaskExecutor();
		taskExecutor.setComId(sessionUser.getComId());
		taskExecutor.setExecutor(sessionUser.getId());
		taskExecutor.setTaskId(taskId);
		taskExecutor.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DOING));
		taskPromoteDao.update("update taskExecutor set state=:state where comId=:comId and taskId=:taskId and executor=:executor", taskExecutor);
		//更新开始时间
		taskExecutor.setCurStartTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		taskPromoteDao.update("update taskExecutor set curStartTime=:curStartTime where comId=:comId and taskId=:taskId and executor=:executor and curStartTime is null", taskExecutor);
		
		
		//添加日志
		this.addTaskLog(sessionUser.getComId(), taskId, sessionUser.getId(), sessionUser.getUserName(), "开始执行该任务！");
		return true;
	}
	
	/**
	 * 暂停执行该任务
	 * @param taskId 任务主键
	 * @param sessionUser 当前操作人员
	 * @param stateName
	 * @return
	 */
	public boolean updatePauseExecuteTask(Integer taskId, UserInfo sessionUser, String stateName){
		List<TaskExecuteTime> listTaskExecuteTime = taskPromoteDao.listTaskExecuteTimeForPause(sessionUser,taskId,sessionUser.getId());
		if(null != listTaskExecuteTime && !listTaskExecuteTime.isEmpty()){
			String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
			for (TaskExecuteTime taskExecuteTime : listTaskExecuteTime) {
				
				taskExecuteTime.setEndTime(nowDateTime);
				taskPromoteDao.update(taskExecuteTime);
				
				//暂停执行人的
				TaskExecutor taskExecutor = new TaskExecutor();
				taskExecutor.setComId(sessionUser.getComId());
				taskExecutor.setExecutor(sessionUser.getId());
				taskExecutor.setTaskId(taskExecuteTime.getTaskId());
				taskExecutor.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_PAUSE));
				taskPromoteDao.update("update taskExecutor set state=:state where comId=:comId and taskId=:taskId and executor=:executor", taskExecutor);
				
				//添加日志
				this.addTaskLog(sessionUser.getComId(), taskId, sessionUser.getId(), sessionUser.getUserName(), "暂停执行该任务！");
			}
		}
		return true;
	}
	/**
	 * 认领任务信息
	 * @param taskId 任务主键
	 * @param sessioUser 当前操作员
	 */
	public Map<String, Object> updateAcceptTask(Integer taskId, UserInfo sessioUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		Task taskObj =  (Task) taskPromoteDao.objectQuery(Task.class, taskId);
		
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
					taskPromoteDao.delByField("taskExecutor",new String[]{"comId","taskId","executor"},
							new Object[]{comId,taskId,executor});
					//删除任务当前步骤信息
					taskPromoteDao.delByField("taskHandOver",new String[]{"comId","taskId","toUser","curStep"},
							new Object[]{comId,taskId,executor,1});
				}else{
					//修改任务执行状态
					Task task = new Task();
					task.setId(taskId);
					task.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DOING));
					taskPromoteDao.update(task);
					
					//修改办理人的任务执行状态
					taskExecutor.setState(Integer.parseInt(ConstantInterface.STATIS_TASK_STATE_DOING));
					taskExecutor.setTaskProgress(0);
					taskExecutor.setCurStartTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
					taskPromoteDao.update(taskExecutor);
					
					String stepTag = taskExecutor.getStepTag();
					this.addTaskExecuteTime(sessioUser.getComId(), taskId, stepTag, sessioUser.getId());
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
	 * 查询个人统计信息
	 * @author hcj 
	 * @date: 2018年10月23日 下午2:09:50
	 * @param userInfo
	 * @param task
	 * @return
	 * @throws ParseException 
	 */
	public Task countUserTask(UserInfo userInfo, Task task) throws ParseException {
		return taskPromoteDao.countUserTask(userInfo,task);
	}
	/**
	 * 任务时限统计
	 * @author hcj 
	 * @date: 2018年10月24日 上午9:05:29
	 * @param task
	 * @param userInfo
	 * @return
	 */
	public List<Task> taskToDoCount(Task task, UserInfo userInfo) {
		return taskPromoteDao.taskToDoCount(task,userInfo);
	}
	
	
}
