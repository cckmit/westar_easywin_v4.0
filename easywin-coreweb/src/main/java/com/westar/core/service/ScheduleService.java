package com.westar.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.MeetLearn;
import com.westar.base.model.ScheLog;
import com.westar.base.model.ScheTalk;
import com.westar.base.model.ScheUser;
import com.westar.base.model.Schedule;
import com.westar.base.model.Task;
import com.westar.base.model.TaskExecutor;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.ScheduleDao;
import com.westar.core.thread.IndexUpdateThread;

@Service
public class ScheduleService {

	@Autowired
	ScheduleDao scheduleDao;
	
	@Autowired
	IndexService indexService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	TodayWorksService todayWorksService;

	/**
	 * 查询日程事件
	 * @param comId 企业号
	 * @param userId 被查看人员的主键
	 * @param startDate 日程时间起
	 * @param endDate 日程时间止
	 * @param busTypeList 模块类型
	 * @param subType 查询下属 0查询自己 1查询下属 null查询所有
	 * @param userInfo 
	 * @return
	 */
	public List<Schedule> listEvents(Integer comId, Integer userId,String startDate,String endDate,
			  List<String> busTypeList, String subType, UserInfo userInfo) {
		//原始数据
		List<Schedule> list = scheduleDao.listEvents(comId,userId,startDate,endDate,busTypeList,
				subType,userInfo);
		//处理后的数据
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		if(null!=list && list.size()>0){
			for (Schedule schedule : list) {
				if(schedule.getBusType().equals(ConstantInterface.TYPE_SCHEDULE)){//是日程
					if(schedule.getIsRepeat().equals(1)){//需要重复
						List<Schedule> schedules = CommonUtil.formatSchedule(schedule,startDate,endDate);
						scheduleList.addAll(schedules);
					}else{
						scheduleList.add(schedule);
					}
				}else if(schedule.getBusType().equals(ConstantInterface.TYPE_TASK)){
					scheduleList.add(schedule);
				}
			}
		}
		
		return scheduleList;
	}

	/**
	 * 添加日程
	 * @param userInfo 当前操作人员
	 * @param schedule 日程信息
	 * @param scheUserIds 日程参与人员
	 * @return
	 */
	public Integer addSchedule(UserInfo userInfo, Schedule schedule,
			Integer[] scheUserIds) {
		schedule = CommonUtil.resetSchedule(schedule);
		//计算日程执行时间
		Map<String,String> map = CommonUtil.getSchedulDateTime(schedule);
		schedule.setDataTimeS(map.get("dataTimeS"));
		schedule.setDataTimeE(map.get("dataTimeE"));
		
		Integer id = scheduleDao.add(schedule);
		if(null!=scheUserIds && scheUserIds.length>0){
			for (Integer userId : scheUserIds) {
				//日程参与人
				ScheUser scheUser = new ScheUser();
				//企业号
				scheUser.setComId(userInfo.getComId());
				//日程主键
				scheUser.setScheduleId(id);
				//设置人员主键
				scheUser.setUserId(userId);
				//添加参与人员
				scheduleDao.add(scheUser);
			}
		}
		//添加日志
		this.addScheLog(userInfo.getComId(),id, userInfo.getId(), userInfo.getUserName(), "新建日程");
		
		try {
			this.updateScheIndex(id, userInfo, "add");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return id;
	}

	/**
	 * 通过主键取得日程详情
	 * @param id
	 * @return
	 */
	public Schedule getScheduleById(Integer id) {
		Schedule schedule = (Schedule) scheduleDao.objectQuery(Schedule.class, id);
		return schedule;
	}

	/**
	 * 日程的参与人
	 * @param scheduleId 日程主键
	 * @param comId 企业号
	 * @return
	 */
	public List<ScheUser> listScheUser(Integer scheduleId, Integer comId) {
		List<ScheUser> list = scheduleDao.listScheUser(scheduleId,comId);
		return list;
	}

	/**
	 * 删除日程
	 * @param id
	 * @throws Exception
	 */
	public void deleteSchedule(Integer id,UserInfo userInfo ) throws Exception {

		this.updateScheIndex(id, userInfo, "del");
		Integer comId = userInfo.getComId();
		//日程日志
		scheduleDao.delByField("scheLog", new String[]{"comId","scheduleId"}, new Object[]{comId,id});
		//日程留言
		scheduleDao.delByField("scheTalk", new String[]{"comId","scheduleId"}, new Object[]{comId,id});
		//日程关联模块
		scheduleDao.delByField("scheRelateMod", new String[]{"comId","scheduleId"}, new Object[]{comId,id});
		//日程参与人员
		scheduleDao.delByField("scheUser", new String[]{"comId","scheduleId"}, new Object[]{comId,id});
		
		//删除日程
		scheduleDao.delById(Schedule.class, id);
	}
	/**
	 * 修改日程
	 * @param userInfo
	 * @param schedule
	 * @param scheUserIds
	 */
	public Schedule updateSchedule(UserInfo userInfo, Schedule schedule,
			Integer[] scheUserIds) {
		//日程主键
		Integer scheduleId = schedule.getId();
		schedule = CommonUtil.resetSchedule(schedule);
		//计算日程执行时间
		Map<String,String> map = CommonUtil.getSchedulDateTime(schedule);
		
		schedule.setDataTimeS(map.get("dataTimeS"));
		schedule.setDataTimeE(map.get("dataTimeE"));
		//修改日程数据
		scheduleDao.update(schedule);
		
		//日程参与人员
		scheduleDao.delByField("scheUser", new String[]{"comId","scheduleId"}, new Object[]{userInfo.getComId(),scheduleId});
		if(null!=scheUserIds && scheUserIds.length>0){
			for (Integer userId : scheUserIds) {
				//日程参与人
				ScheUser scheUser = new ScheUser();
				//企业号
				scheUser.setComId(userInfo.getComId());
				//日程主键
				scheUser.setScheduleId(scheduleId);
				//设置人员主键
				scheUser.setUserId(userId);
				//添加参与人员
				scheduleDao.add(scheUser);
			}
		}
		
		//添加日程
		this.addScheLog(userInfo.getComId(),schedule.getId(), userInfo.getId(), userInfo.getUserName(), "修改日程");
		
//		try {
//			this.updateScheIndex(scheduleId, userInfo, "update");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return (Schedule) scheduleDao.objectQuery(Schedule.class, schedule.getId());
		
	}
	/**
	 * 拖动改变日程(开始时间和结束时间都需要修改)
	 * @param userInfo
	 * @param id 日程主键
	 * @param day 拖动的天数
	 * @param minu 拖动的分钟数
	 */
	public void updateScheduleByDrag(UserInfo userInfo, Integer id,
			Integer day, Integer minu) {
		Schedule schedule = (Schedule) scheduleDao.objectQuery(Schedule.class, id);
		//原日程设置时间起
		String scheStartDate = schedule.getScheStartDate();
		//原日程设置时间止
		String scheEndDate = schedule.getScheEndDate();
		if(schedule.getIsAllDay()==1){//是全天的
			//开始时间
			Long meetStartTimeL = DateTimeUtil.parseDate(scheStartDate, DateTimeUtil.yyyy_MM_dd).getTime();
			//截止时间
			Long meetEndTimeL = DateTimeUtil.parseDate(scheEndDate, DateTimeUtil.yyyy_MM_dd).getTime();
			//时间差
			Long timeDiff = meetEndTimeL-meetStartTimeL;
			if(day!=0){//有天数
				scheStartDate = DateTimeUtil.addDate(scheStartDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, day);
			}
			if(minu!=0){
				scheStartDate = DateTimeUtil.addDate(scheStartDate, DateTimeUtil.yyyy_MM_dd, Calendar.MINUTE, minu);
			}
			meetStartTimeL = DateTimeUtil.parseDate(scheStartDate, DateTimeUtil.yyyy_MM_dd).getTime()+timeDiff;
			scheEndDate = DateTimeUtil.formatDate(new Date(meetStartTimeL), DateTimeUtil.yyyy_MM_dd);
		}else{
			//完善原日程设置时间起
			scheStartDate = scheStartDate+":00";
			//完善原日程设置时间止
			scheEndDate = scheEndDate+":00";
			//开始时间
			Long meetStartTimeL = DateTimeUtil.parseDate(scheStartDate, DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
			//截止时间
			Long meetEndTimeL = DateTimeUtil.parseDate(scheEndDate, DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
			//时间差
			Long timeDiff = meetEndTimeL-meetStartTimeL;
			
			if(day!=0){//有天数
				scheStartDate = DateTimeUtil.addDate(scheStartDate, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, day);
			}
			if(minu!=0){
				scheStartDate = DateTimeUtil.addDate(scheStartDate, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.MINUTE, minu);
			}
			meetStartTimeL = DateTimeUtil.parseDate(scheStartDate, DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime()+timeDiff;
			scheEndDate = DateTimeUtil.formatDate(new Date(meetStartTimeL), DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
			
			//新的日程设置时间起
			scheStartDate = scheStartDate.substring(0, scheStartDate.lastIndexOf(":"));
			//新的日程设置时间止
			scheEndDate = scheEndDate.substring(0, scheEndDate.lastIndexOf(":"));
		}
		
		schedule.setScheStartDate(scheStartDate);
		schedule.setScheEndDate(scheEndDate);
		if(schedule.getIsRepeat()==1 && schedule.getRepType().equals("2")){//是每周重复的
			List<String> repDates = Arrays.asList(schedule.getRepDate().split(","));
			List<String> res = new ArrayList<String>();
			day = day%7;
			if(day<0){
				day = 7+day;
			}
			if(day!=0){
				for (String repDateStr : repDates) {
					String repDate = (Integer.parseInt(repDateStr)+day)%7+"";
					if(repDate.equals("0")){
						repDate = "7";
					}
					res.add(repDate);
				}
				Collections.sort(res);
				schedule.setRepDate(res.toString().replace("[", "").replace("]", "").replace(" ", ""));
			}
		}
		//计算日程执行时间
		Map<String,String> map = CommonUtil.getSchedulDateTime(schedule);
		
		schedule.setDataTimeS(map.get("dataTimeS"));
		schedule.setDataTimeE(map.get("dataTimeE"));
		
		//修改日程数据
		scheduleDao.update(schedule);
		//添加日志
		this.addScheLog(userInfo.getComId(),id, userInfo.getId(), userInfo.getUserName(), "修改日程设置的起止时间");
		
//		try {
//			this.updateScheIndex(id, userInfo, "update");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	/**
	 * 改变日程时间大小（只修改结束时间）
	 * @param userInfo
	 * @param id 日程主键
	 * @param day 改变的天数
	 * @param minu 改变的分钟数
	 */
	public void updateScheduleByResize(UserInfo userInfo, Integer id,
			Integer day, Integer minu) {
		Schedule schedule = (Schedule) scheduleDao.objectQuery(Schedule.class, id);
		//原日程设置时间止
		String scheEndDate = schedule.getScheEndDate();
		if(schedule.getIsAllDay()==1){//是全天的
			if(day!=0){//有天数
				scheEndDate = DateTimeUtil.addDate(scheEndDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, day);
			}
			if(minu!=0){
				scheEndDate = DateTimeUtil.addDate(scheEndDate, DateTimeUtil.yyyy_MM_dd, Calendar.MINUTE, minu);
			}
		}else{
			//完善原日程设置时间止
			scheEndDate = scheEndDate+":00";
			if(day!=0){//有天数
				scheEndDate = DateTimeUtil.addDate(scheEndDate, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, day);
			}
			if(minu!=0){
				scheEndDate = DateTimeUtil.addDate(scheEndDate, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.MINUTE, minu);
			}
			//新的日程设置时间止
			scheEndDate = scheEndDate.substring(0, scheEndDate.lastIndexOf(":"));
		}
		
		schedule.setScheEndDate(scheEndDate);
		//计算日程执行时间
		Map<String,String> map = CommonUtil.getSchedulDateTime(schedule);
		
		schedule.setDataTimeS(map.get("dataTimeS"));
		schedule.setDataTimeE(map.get("dataTimeE"));
		//修改日程数据
		scheduleDao.update(schedule);
		//添加日志
		this.addScheLog(userInfo.getComId(),id, userInfo.getId(), userInfo.getUserName(), "修改日程设置的结束时间");
		
////		try {
//			this.updateScheIndex(id, userInfo, "update");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
	}

	/**
	 * 查询日程留言
	 * @param comId 企业号
	 * @param scheduleId 日程主键
	 * @return
	 */
	public List<ScheTalk> listPagedScheTalks(Integer comId, Integer scheduleId) {
		List<ScheTalk> list = scheduleDao.listPagedScheTalks(comId,scheduleId);
		List<ScheTalk> scheTalks = new ArrayList<ScheTalk>();
		for (ScheTalk scheTalk : list) {
			//处理回复的内容
			scheTalk.setTalkContent(StringUtil.toHtml(scheTalk.getTalkContent()));
			scheTalks.add(scheTalk);
		}
		return scheTalks;
	}

	/**
	 * 添加日程
	 * @param scheTalk
	 * @param sessionUser
	 * @return
	 */
	public Integer addScheTalk(ScheTalk scheTalk, UserInfo userInfo) {
		//讨论的主键
		Integer id =scheduleDao.add(scheTalk);
		//日程需要告知的人员
		List<UserInfo> shares = new ArrayList<>();
		//添加@人员
		List<ScheUser> scheUsers = scheTalk.getListScheUsers();
		Set<Integer> pushUserIdSet = new HashSet<Integer>();
		if(null != scheUsers && !scheUsers.isEmpty()){
			for (ScheUser scheUser : scheUsers) {
				//人员主键
				Integer userId = scheUser.getUserId();
				pushUserIdSet.add(userId);
				//删除上次的学习人员
				scheduleDao.delByField("scheUser", new String[]{"comId","scheduleId","userId"}, 
						new Object[]{userInfo.getComId(),scheTalk.getScheduleId(),userId});
				scheUser.setScheduleId(scheTalk.getScheduleId());
				scheUser.setComId(userInfo.getComId());
				scheduleDao.add(scheUser);
				
				//保存消息通知人信息
				UserInfo user = new UserInfo();
				user.setId(scheUser.getUserId());
				user.setUserName(scheUser.getUserName());
				shares.add(user);
				
			}
		}
		
		//发送消息
		todayWorksService.addTodayWorks(userInfo, null, scheTalk.getScheduleId(), "添加日程留言", 
				ConstantInterface.TYPE_SCHEDULE, shares, null);
		
//		try {
//			this.updateScheIndex(scheTalk.getScheduleId(), sessionUser, "update");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return id;
	}

	/**
	 * 查询日程指定留言主键的详细信息
	 * @param id 留言主键
	 * @param comId 企业号
	 * @return
	 */
	public ScheTalk getScheTalk(Integer id, Integer comId) {
		ScheTalk scheTalk = scheduleDao.getScheTalk(id,comId);
		//转换
		if(null!=scheTalk){
			String content = StringUtil.toHtml(scheTalk.getTalkContent());
			scheTalk.setTalkContent(content);
		}
		return scheTalk;
	}
	/**
	 * 删除讨论回复
	 * @param id 留言主键
	 * @param delChildNode 是否删除子节点
	 * @return
	 * @throws Exception
	 */
	public void delScheTalk(Integer id, String delChildNode,
			UserInfo userInfo) {
		//查询日程讨论是否存在
		ScheTalk scheTalk = (ScheTalk) scheduleDao.objectQuery(ScheTalk.class, id);
		if(null!=scheTalk){
			this.addScheLog(userInfo.getComId(),scheTalk.getScheduleId(), userInfo.getId(), userInfo.getUserName(), "删除日程留言");
			//删除自己
			if(null==delChildNode){
				scheduleDao.delById(ScheTalk.class, id);
			}else if("yes".equals(delChildNode)){//删除自己和所有的子节点
				scheduleDao.delScheTalk(id,userInfo.getComId());
			}else if("no".equals(delChildNode)){//删除自己,将子节点提高一级
				//将子节点提高一级
				scheduleDao.updateScheTalkParentId(id,userInfo.getComId());
				//删除自己
				scheduleDao.delById(ScheTalk.class, id);
			}
		}
		
//		try {
//			this.updateScheIndex(scheTalk.getScheduleId(), userInfo, "update");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}

	/**
	 * 查看日程日志
	 * @param scheduleId 日程主键
	 * @param comId 企业号
	 * @return
	 */
	public List<ScheLog> listPagedScheLog(Integer scheduleId, Integer comId) {
		List<ScheLog> list = scheduleDao.listPagedScheLog(comId,scheduleId);
		return list;
	}

	/**
	 * 添加日程日志
	 * @param comId 企业号
	 * @param scheduleId 日程主键
	 * @param userId 用户主键
	 * @param userName 用户名称
	 * @param content 日志内容
	 */
	public void addScheLog(Integer comId, Integer scheduleId, Integer userId,
			String userName, String content) {
		ScheLog scheLog = new ScheLog();
		//设置企业号
		scheLog.setComId(comId);
		//设置关联日程主键
		scheLog.setScheduleId(scheduleId);
		//设置人员主键
		scheLog.setUserId(userId);
		//设置日志内容
		scheLog.setContent(content);
		//设置人员名称
		scheLog.setUserName(userName);
		//添加日程日志
		scheduleDao.add(scheLog);
	}
	
	/**
	 * 更新日程索引
	 * @param scheduleId 日程主键
	 * @param userInfo 当前操作人员
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception
	 */
	public void updateScheIndex(Integer scheduleId,UserInfo userInfo,String opType) throws Exception{
		
//		Integer comId = userInfo.getComId();
		
		Schedule scheduleT = (Schedule) scheduleDao.objectQuery(Schedule.class, scheduleId);
		if(null==scheduleT){return;}
		
		Schedule schedule = new Schedule();
		schedule.setRecordCreateTime(scheduleT.getRecordCreateTime());
		schedule.setUserName(userInfo.getUserName());
		schedule.setTitle(scheduleT.getTitle());
		schedule.setContent(scheduleT.getContent());
		
		
//		StringBuffer attStr = new StringBuffer(CommonUtil.objAttr2String(schedule,null));
		StringBuffer attStr = new StringBuffer(scheduleT.getTitle());
		
		//日程参与人员
//		List<ScheUser> scheUsers = scheduleDao.listScheUser(scheduleId, comId);
//		if(null!=scheUsers && scheUsers.size()>0){
//			for (ScheUser scheUser : scheUsers) {
//				attStr.append(scheUser.getUserName()+",");
//			}
//		}
		//日程留言
//		List<ScheTalk> scheTalks = scheduleDao.listPagedScheTalks(comId, scheduleId);
//		if(null!=scheTalks && scheTalks.size()>0){
//			for (ScheTalk vo : scheTalks) {
//				attStr.append(vo.getTalkContent()+","+vo.getTalkerName()+",");
//			}
//		}
		//单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String indexKey = userInfo.getComId()+"_"+ConstantInterface.TYPE_SCHEDULE+"_"+scheduleId;
		//为任务创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				indexKey,userInfo.getComId(),scheduleId,ConstantInterface.TYPE_SCHEDULE,
				schedule.getTitle(),attStr.toString(),DateTimeUtil.parseDate(schedule.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,indexKey));
		}
		
		
	}
	/**
	 * 获取团队下所有日程主键
	 * @param userInfo
	 * @return
	 */
	public List<Schedule> listScheduleOfAll(UserInfo userInfo){
		return scheduleDao.listScheduleOfAll(userInfo);
	}
	
	/**
	 * 日程转任务
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public Boolean updateToTask(Integer id, UserInfo userInfo) {
		Boolean state = true;
		Schedule schedule = (Schedule)scheduleDao.objectQuery(Schedule.class, id);
		if(schedule != null) {
			Task task = new Task();
			//企业号
			task.setComId(schedule.getComId());
			//创建人
			task.setCreator(schedule.getUserId());
			//负责人
			task.setOwner(schedule.getUserId());
			//删除标识(正常)
			task.setDelState(0);
			//默认项目主键0
			task.setBusId(0);
			//默认没有类型
			task.setBusType("0");
			task.setParentId(-1);
			task.setTaskType("1");
			task.setGrade("1");
			task.setTaskName(schedule.getTitle());
			task.setDealTimeLimit(schedule.getScheEndDate());
			task.setTaskRemark(schedule.getContent());
			//设置任务办理人
			List<TaskExecutor> listTaskExecutors =  new ArrayList<>();
			TaskExecutor taskExecutor = new TaskExecutor();
			taskExecutor.setExecutor(schedule.getUserId());
			taskExecutor.setExecutorName(schedule.getUserName());
			listTaskExecutors.add(taskExecutor);
			task.setListTaskExecutor(listTaskExecutors);
			try {
				taskService.addTask(task, null, userInfo, null);
				this.addScheLog(userInfo.getComId(),id, userInfo.getId(), userInfo.getUserName(), "日程转为任务");
			} catch (Exception e) {
				e.printStackTrace();
				state = false;
			}
		}else {
			state = false;
		}
		return state;
	}

	/**
	 * 任务转日程
	 * @param taskId 任务主键
	 * @param userInfo 当前登录人员
	 * @return java.lang.Integer
	 * @author LiuXiaoLin
	 * @date 2018/6/20 0020 13:39
	 */
	public Integer taskConversionSchedule(Integer taskId,UserInfo userInfo){
		//获取需要转日程的任务
		Task task = (Task) scheduleDao.objectQuery(Task.class,taskId);

		//检查当前登录人员和该任务是否有关联
		List<TaskExecutor> executors = taskService.listTaskExecutor(taskId,userInfo);
		boolean isExecutor = false;
		//用于设置任务的起止时间
		TaskExecutor taskExecutor = null;
		//是否为执行人
		for(TaskExecutor taskExecutorT : executors){
			if(taskExecutorT.getExecutor() == userInfo.getId()){
				isExecutor = true;
				taskExecutor = taskExecutorT;
				break;
			}
		}
		boolean isCreator = false;
		//是否为负责人
		if(null != task.getCreator() && task.getCreator() == userInfo.getId()){
			isCreator = true;
		}

		//如果没有关联则不继续
		if(!isCreator && !isExecutor){
			return null;
		}

		Schedule schedule = new Schedule();
		schedule.setComId(task.getComId());
		schedule.setTitle(task.getTaskName());
		//去除html标签
		schedule.setContent(task.getTaskRemark().replaceAll("</?[^>]+>", ""));
		//设置起止日期
		if(isExecutor){
			schedule.setDataTimeS(taskExecutor.getRecordCreateTime());
			schedule.setDataTimeE(taskExecutor.getHandTimeLimit().substring(0,10) + " 59:59:00");
			schedule.setScheStartDate(taskExecutor.getRecordCreateTime().substring(0,10));
			schedule.setScheEndDate(taskExecutor.getHandTimeLimit().substring(0,10));
		}else{
			schedule.setDataTimeS(task.getRecordCreateTime());
			schedule.setDataTimeE(task.getDealTimeLimit().substring(0,10) + " 59:59:00");
			schedule.setScheStartDate(task.getRecordCreateTime().substring(0,10));
			schedule.setScheEndDate(task.getDealTimeLimit().substring(0,10));
		}
		schedule.setUserId(userInfo.getId());
		schedule.setPublicType(0);
		schedule.setIsAllDay(1);
		schedule.setIsRepeat(0);

		Integer scheduleId = scheduleDao.add(schedule);
		return scheduleId;
	}
}
