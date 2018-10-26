package com.westar.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.westar.base.model.Clock;
import com.westar.base.model.ClockPerson;
import com.westar.base.model.ClockWay;
import com.westar.base.model.Meeting;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.CustomJob;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.ClockDao;
import com.westar.core.job.QuartzJobOne;
import com.westar.core.job.QuartzManager;
import com.westar.core.thread.sendPhoneMsgThread;

@Service
public class ClockService {

	@Autowired
	ClockDao clockDao;
	
	@Autowired
	private TodayWorksService todayWorksService;
	
	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	PhoneMsgService phoneMsgService;

	/**
	 * 保存定时提醒
	 * @param clock 提醒信息
	 * @param userInfo 操作人员
	 * @param sendWays 发送方式
	 * @param users 消息接收对象
	 * @return
	 */
	public Integer addClock(Clock clock, UserInfo userInfo, String[] sendWays, Integer[] users) {
		//创建人
		clock.setUserId(userInfo.getId());
		//所在企业
		clock.setComId(userInfo.getComId());
		
		if(null==clock.getBusId()|| StringUtil.isBlank(clock.getBusType())){
			clock.setBusId(0);
			clock.setBusType("0");
		}
		//默认开启
		clock.setClockIsOn("1");
		//默认未执行
		clock.setExecuteState("0");
		
		//最近一次执行时间
		String clockNextDate = CommonUtil.getRecentClockDate(clock.getClockDate(),clock.getClockTime(),clock.getClockRepType(),clock.getClockRepDate());
		clock.setClockNextDate(clockNextDate);
		//不是周且不是月
		if(!clock.getClockRepType().equals("2") && !clock.getClockRepType().equals("3")){
			clock.setClockRepDate("");
		}
		
		Integer id = clockDao.add(clock);
		clock.setId(id);
		//立即添加提醒
		this.addAlarmClock(clock);
		
		if(null!=sendWays && sendWays.length>0){
			for (String sendWay : sendWays) {
				//提醒方式
				ClockWay clockWay = new ClockWay();
				
				//所在企业
				clockWay.setComId(userInfo.getComId());
				//提醒主键
				clockWay.setClockId(id);
				//提醒方式
				clockWay.setSendWay(sendWay);
				//业务主键
				clockWay.setBusId(clock.getBusId());
				//业务类型
				clockWay.setBusType(clock.getBusType());
				
				//添加提醒方式
				clockDao.add(clockWay);
 				
			}
		}
		//闹铃消息接收对象
		if(null!=users && users.length>0){
			for (Integer userId : users) {
				ClockPerson clockPerson = new ClockPerson();
				//接收对象所在企业
				clockPerson.setComId(userInfo.getComId());
				//闹铃主键
				clockPerson.setClockId(id);
				//接收对象主键
				clockPerson.setUserId(userId);
				
				clockDao.add(clockPerson);
			}
		}
		return id;
	}

	/**
	 * 立即添加提醒
	 * @param clock
	 */
	private void addAlarmClock(Clock clock) {
		//最近一次执行时间
		String recentDateTimeStr = clock.getClockNextDate();
		//当前时间
		String nowDateTimeStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		nowDateTimeStr = nowDateTimeStr.substring(0,nowDateTimeStr.lastIndexOf(":"));
		
		//1小之后
		String laterDateTimeStr = DateTimeUtil.addDate(nowDateTimeStr+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
				Calendar.HOUR_OF_DAY, 1);
		laterDateTimeStr = laterDateTimeStr.substring(0,laterDateTimeStr.lastIndexOf(":"));
		
		//当前时间
		Date nowDateTime = DateTimeUtil.parseDate(nowDateTimeStr+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		//1小时后的时间
		Date laterDateTime = DateTimeUtil.parseDate(laterDateTimeStr+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		//最近一次执行时间
		Date recentDateTime = DateTimeUtil.parseDate(recentDateTimeStr+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		
		//执行时间是否能在1小时之内执行
		boolean bool = DateTimeUtil.isBetween(nowDateTime, laterDateTime, recentDateTime);
		if(bool){//是
			//周期类型
			String clockRepType = clock.getClockRepType();
			
			String cornTime;
			try {
				cornTime = DateTimeUtil.transCorn(recentDateTimeStr);
				CustomJob job = new CustomJob();
				job.setJobId("task_"+clock.getId());
				job.setJobGroup("group_"+clock.getId());
				job.setCronExpression(cornTime);//每五秒执行一次
				job.setStateFulljobExecuteClass(QuartzJobOne.class);
				
				JobDataMap paramsMap = new JobDataMap();
				paramsMap.put("task_"+clock.getId(),clock);
				QuartzManager.enableCronSchedule(job, paramsMap, true);
				
				List<Clock> clocks = new ArrayList<Clock>();
				clocks.add(clock);
				this.updateClockExecuteState(clocks);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}catch (Exception e) {//定时的时间已过
				if (!e.getClass().getSimpleName().equals("SchedulerException")) {
					//取得闹铃下次执行时间
					String nextStartDate = CommonUtil.getClockNextStartDate(clock.getClockNextDate(),clockRepType,
							clock.getClockRepDate(),clock.getClockTime());
					clock.setClockNextDate(nextStartDate);
					
					if("0".equals(clockRepType)){//已过期
						clock.setExecuteState("2");
					}else{//下次未执行
						clock.setExecuteState("0");
					}
					clockDao.update(clock);
				}
			}
		}
	}
	/**
	 * 重新启动闹铃
	 * @param clock 只针对非一次的闹铃
	 */
	public void updateRestartClock(Clock clock) {
		//最近一次执行时间
		String recentDateTimeStr = CommonUtil.getRecentClockDate(clock.getClockDate(),clock.getClockTime(),clock.getClockRepType(),clock.getClockRepDate());
		
		//当前时间
		String nowDateTimeStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		nowDateTimeStr = nowDateTimeStr.substring(0,nowDateTimeStr.lastIndexOf(":"));
		
		//1小之后
		String laterDateTimeStr = DateTimeUtil.addDate(nowDateTimeStr+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
				Calendar.HOUR_OF_DAY, 1);
		laterDateTimeStr = laterDateTimeStr.substring(0,laterDateTimeStr.lastIndexOf(":"));
		
		//当前时间
		Date nowDateTime = DateTimeUtil.parseDate(nowDateTimeStr+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		//1小时后的时间
		Date laterDateTime = DateTimeUtil.parseDate(laterDateTimeStr+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		//最近一次执行时间
		Date recentDateTime = DateTimeUtil.parseDate(recentDateTimeStr+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		
		//执行时间是否能在1小时之内执行
		boolean bool = DateTimeUtil.isBetween(nowDateTime, laterDateTime, recentDateTime);
		//周期类型
		String clockRepType = clock.getClockRepType();
		if(bool){//1小时之内，放入定时系统
			
			String cornTime;
			try {
				cornTime = DateTimeUtil.transCorn(recentDateTimeStr);
				CustomJob job = new CustomJob();
				job.setJobId("task_"+clock.getId());
				job.setJobGroup("group_"+clock.getId());
				job.setCronExpression(cornTime);//每五秒执行一次
				job.setStateFulljobExecuteClass(QuartzJobOne.class);
				
				JobDataMap paramsMap = new JobDataMap();
				paramsMap.put("task_"+clock.getId(),clock);
				QuartzManager.enableCronSchedule(job, paramsMap, true);
				
				List<Clock> clocks = new ArrayList<Clock>();
				clocks.add(clock);
				this.updateClockExecuteState(clocks);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}catch (Exception e) {//定时的时间已过
				if (!e.getClass().getSimpleName().equals("SchedulerException")) {
					//取得闹铃下次执行时间
					String nextStartDate = CommonUtil.getClockNextStartDate(recentDateTimeStr,clockRepType,
							clock.getClockRepDate(),clock.getClockTime());
					clock.setClockNextDate(nextStartDate);
					
					if("0".equals(clockRepType)){//已过期
						clock.setExecuteState("2");
					}else{//下次未执行
						clock.setExecuteState("0");
					}
					clockDao.update(clock);
				}
			}
		}else{//不能再1小时之内执行
			if("0".equals(clockRepType) && recentDateTime.before(nowDateTime)){//仅一次且过期了
				
				//设置成没有没执行
				clock.setExecuteState("2");
				
				clockDao.update(clock);
			}else{
				//设置最近一次执行时间
				clock.setClockNextDate(recentDateTimeStr);
				//设置成没有没执行
				clock.setExecuteState("0");
				
				clockDao.update(clock);
				
			}
		}
	}

	/**
	 * 根据clock主键查询详情
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public Clock getClockById(Integer id, UserInfo userInfo) {
		Clock clock = (Clock) clockDao.objectQuery(Clock.class, id);
		if(null!=clock){
			//信息发送方式
			List<ClockWay> clockWays = clockDao.listClockWay(id,userInfo.getComId());
			clock.setListClockWay(clockWays);
		}
		return clock;
	}

	/**
	 * 删除定时提醒
	 * @param id
	 * @param userInfo
	 */
	public void delClock(Integer id, UserInfo userInfo) {
		//删除待办事项或是消息提醒
		clockDao.delByField("todayworks", new String[]{"comId","clockId"}, new Object[]{userInfo.getComId(),id});
		//删除消息接收对象
		clockDao.delByField("clockPerson", new String[]{"comId","clockId"}, new Object[]{userInfo.getComId(),id});
		//删除提醒方式
		clockDao.delByField("clockWay", new String[]{"comId","clockId"}, new Object[]{userInfo.getComId(),id});
		
		//删除定时提醒
		clockDao.delById(Clock.class, id);
		
		//不要定时了
		QuartzManager.disableSchedule("task_"+id, "group_"+id);
		
	}

	/**
	 * 修改定时提醒
	 * @param clock
	 * @param userInfo
	 * @param sendWays
	 * @param users 消息接收对象主键数组
	 * @return
	 */
	public Integer updateClock(Clock clock, UserInfo userInfo, String[] sendWays, Integer[] users) {
		//删除提醒方式
		clockDao.delByField("clockWay", new String[]{"comId","clockId"}, new Object[]{userInfo.getComId(),clock.getId()});
		//删除提醒对象
		clockDao.delByField("clockPerson", new String[]{"comId","clockId"}, new Object[]{userInfo.getComId(),clock.getId()});
		
		if(null==clock.getBusId()|| StringUtil.isBlank(clock.getBusType())){
			clock.setBusId(0);
			clock.setBusType("0");
		}
		//默认开启
		clock.setClockIsOn("1");
		clock.setExecuteState("0");
		
		//最近一次执行时间
		String clockNextDate = CommonUtil.getRecentClockDate(clock.getClockDate(),clock.getClockTime(),clock.getClockRepType(),clock.getClockRepDate());
		clock.setClockNextDate(clockNextDate);
		//不是周且不是月
		if(!clock.getClockRepType().equals("2") && !clock.getClockRepType().equals("3")){
			clock.setClockRepDate("");
		}
		
		clockDao.update(clock);
		//修改定时提醒时间
		this.addAlarmClock(clock);
		
		if(null!=sendWays && sendWays.length>0){
			for (String sendWay : sendWays) {
				//提醒方式
				ClockWay clockWay = new ClockWay();
				//所在企业
				clockWay.setComId(userInfo.getComId());
				//提醒主键
				clockWay.setClockId(clock.getId());
				//提醒方式
				clockWay.setSendWay(sendWay);
				//业务主键
				clockWay.setBusId(clock.getBusId());
				//业务类型
				clockWay.setBusType(clock.getBusType());
				
				//添加提醒方式
				clockDao.add(clockWay);
 				
			}
		}
		//闹铃消息接收对象
		if(null!=users && users.length>0){
			for (Integer userId : users) {
				ClockPerson clockPerson = new ClockPerson();
				//接收对象所在企业
				clockPerson.setComId(userInfo.getComId());
				//闹铃主键
				clockPerson.setClockId(clock.getId());
				//接收对象主键
				clockPerson.setUserId(userId);
				
				clockDao.add(clockPerson);
			}
		}
		return clock.getId();
	}

	/**
	 * 取出距离现在30分钟内执行的定时提醒
	 * @param nowTime
	 * @return
	 */
	public List<Clock> listAlarmClocks(String nowTime,Integer delayTime) {
		List<Clock> listClocks = clockDao.listAlarmClocks(nowTime,delayTime);
		return listClocks;
	}

	/**
	 * 修改闹钟的执行状态
	 * @param listClocks
	 */
	public void updateClockExecuteState(List<Clock> listClocks) {
		for (Clock clock : listClocks) {
			Clock tempClock = new Clock();
			tempClock.setId(clock.getId());
			tempClock.setExecuteState("1");
			clockDao.update(tempClock);
		}
	}

	/**
	 * 定时提醒后进行设定,并发送消息
	 * @param clock
	 */
	public void updateClock(Clock clock,String type) {
		
		//闹铃主键
		Integer clockId = clock.getId();
		Clock clockTemp = (Clock) clockDao.objectQuery(Clock.class, clockId);
		
		//项目合并后业务主键可能变动
		clock.setBusId(clockTemp.getBusId());
		//设置项目是否为待办事项
		clock.setClockMsgType(clockTemp.getClockMsgType());
		
		clockDao.update(clock);
		if(null!=type && type.equals("done")){
			//企业号
			Integer comId = clockTemp.getComId();
			//会议主键
			Integer busId = clockTemp.getBusId();
			if(clock.getBusType().equals(ConstantInterface.TYPE_MEETING)){//发送的是会议提醒
				//与会人员
				List<UserInfo> userList = meetingService.listMeetingUsers(comId, busId);
				List<ClockPerson> clockPersons = new ArrayList<ClockPerson>();
				if(null!=userList && !userList.isEmpty()){
					for (UserInfo userInfo : userList) {
						ClockPerson clockPerson = new ClockPerson();
						//设置企业号
						clockPerson.setComId(comId);
						//设置闹铃主键
						clockPerson.setClockId(0);
						//设置通知人员主键
						clockPerson.setUserId(userInfo.getId());
						//设置通知人员姓名
						clockPerson.setUserName(userInfo.getUserName());
						
						clockPersons.add(clockPerson);
					}
				}
				clock.setContent(clockTemp.getContent());
				clock.setClockPersons(clockPersons);
				//闹铃发送消息提醒
				todayWorksService.updateTodayWork(clock, null);
				//会议提醒成功后，删除闹铃
				clockDao.delById(Clock.class, clockId);
				Meeting meeting = (Meeting) clockDao.objectQuery(Meeting.class, busId);
				
				String msgSendFlag = meeting.getSendPhoneMsg();
				//需要设置短信
				if(StringUtils.isNotEmpty(msgSendFlag) 
						&& msgSendFlag.equals(ConstantInterface.MSG_SEND_YES)){
					
					String content = clock.getContent();
					if (!clock.getBusType().equals(ConstantInterface.TYPE_MEETING)) {// 不是会议
						content = "定时提醒 " + content;
					}
					// 处理消息内容
					if (content.length() > 500) {
						content = content.substring(0, 500);
					}
					//单线程池
					ExecutorService pool = ThreadPoolExecutor.getInstance();
					//跟范围人员发送通知消息
					pool.execute(new sendPhoneMsgThread(phoneMsgService, meeting.getComId(), userList, 
							new Object[]{content}, ConstantInterface.MSG_JOB_TO_DO,"--"));
				}
				
				UserInfo userInfo = new UserInfo();
				userInfo.setComId(clockTemp.getComId());
				userInfo.setId(clockTemp.getUserId());
				
				//添加参会人员确认单
				meetingService.addMeetCheckUser(userInfo,busId,1,"");
				
				if(null!=meeting && !meeting.getMeetingType().equals("0")){//是周期会议
					String meetEndDate = meeting.getEndDate();
					Clock regClock = new Clock();
					//设置企业号
					regClock.setComId(meeting.getComId());
					//创建人员
					regClock.setUserId(meeting.getOrganiser());
					//提醒内容
					regClock.setContent("周期会议《"+meeting.getTitle()+"》");
					//闹铃开始日期
					regClock.setClockDate(meetEndDate.substring(0,10));
					//闹铃下次执行开始日期
					regClock.setClockNextDate(meeting.getEndDate());
					//闹铃开始时间
					regClock.setClockTime(meetEndDate.substring(11,meetEndDate.length()));
					//重复类型,不重复
					regClock.setClockRepType("0");
					//默认开启
					regClock.setClockIsOn("1");
					//业务主键
					regClock.setBusId(meeting.getId());
					//业务类型
					regClock.setBusType(ConstantInterface.TYPE_MEETINGREG);
					//发送普通消息
					regClock.setClockMsgType("0");
					//设置成未执行
					regClock.setExecuteState("0");
					
					Integer id = clockDao.add(regClock);
					regClock.setId(id);
					//立即添加提醒
					this.addAlarmClock(regClock);
				}
				
			}else if(clock.getBusType().equals(ConstantInterface.TYPE_MEETINGREG)){//是周期会议，用于最近一次会议结束后，添加新的会议
				//会议提醒成功后，删除闹铃
				clockDao.delById(Clock.class, clockId);
				//周期会议结束后生成新的周期会议
				meetingService.addRegMeeting(comId, busId);
			}else{
				//信息发送方式
				List<ClockWay> listClockWays = clockDao.listClockWay(clockId, clock.getComId());
				//闹铃对象
				List<ClockPerson> clockPersons = clockDao.listClockPerson(clockId, clock.getComId());
				
				clock.setClockPersons(clockPersons);
				//闹铃发送消息提醒
				todayWorksService.updateTodayWork(clock, listClockWays);
			}
		}
		
	}
	/**
	 * 查询闹铃对象
	 * @param comId 企业号
	 * @param clockId 闹铃主键
	 * @return
	 */
	public List<ClockPerson> listClockPerson(Integer comId,Integer clockId) {
		//闹铃对象
		List<ClockPerson> clockPersons = clockDao.listClockPerson(clockId,comId);
		return clockPersons;
	}

	/**
	 * 选出需要删除的闹铃
	 * @param comId 企业号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param userId 闹铃设置人员 为空则删除该实体的所有闹铃
	 * @return
	 */
	public void delClockByType(Integer comId, Integer busId, String busType) {
		//查询需要删除的闹铃
		List<Clock> listClocks = clockDao.listUserClockForDel(comId,null,busId,busType);
		if(null!=listClocks && listClocks.size()>0){
			for (Clock clock : listClocks) {
				Integer id = clock.getId();
				//删除待办事项或是消息提醒
				clockDao.delByField("todayworks", new String[]{"comId","clockId"}, new Object[]{comId,id});
				//删除待办事项或是消息提醒
				clockDao.delByField("clockPerson", new String[]{"comId","clockId"}, new Object[]{comId,id});
				//闹铃提醒方式
				clockDao.delByField("clockWay", new String[]{"comId","clockId"}, new Object[]{comId,id});
				//不要定时了
				QuartzManager.disableSchedule("task_"+id, "group_"+id);
				
				clockDao.delById(Clock.class, id);
			}
		}
	}

	/**
	 * 取得在服务器关闭的时候未来得及执行的定时提醒
	 * @param nowTime
	 * @return
	 */
	public List<Clock> listClockUndone(String nowTime) {
		List<Clock> listClocks = clockDao.listClockUndone(nowTime);
		return listClocks;
	}

	/**
	 * 分页查询闹铃
	 * @param clock 闹铃属性条件
	 * @param modList 闹铃模块
	 * @return
	 */
	public List<Clock> listPagedClock(Clock clock, List<String> modList) {
		List<Clock> listClocks = clockDao.listPagedClock(clock,modList);
		return listClocks;
	}
	/**
	 * 批量删除闹铃
	 * @param ids 闹铃主键数组
	 * @param userInfo 当前操作人员
	 */
	public void delClocks(Integer[] ids, UserInfo userInfo) {
		if(null!=ids && ids.length>0){
			for (Integer id : ids) {
				//删除待办事项或是消息提醒
				clockDao.delByField("todayworks", new String[]{"comId","clockId"}, new Object[]{userInfo.getComId(),id});
				//删除待办事项或是消息提醒
				clockDao.delByField("clockPerson", new String[]{"comId","clockId"}, new Object[]{userInfo.getComId(),id});
				//删除提醒方式
				clockDao.delByField("clockWay", new String[]{"comId","clockId"}, new Object[]{userInfo.getComId(),id});
				//删除定时提醒
				clockDao.delById(Clock.class, id);
				//不要定时了
				QuartzManager.disableSchedule("task_"+id, "group_"+id);
			}
		}
		
	}

	/**
	 * 取消人员所属模块的闹铃
	 * @param comId 企业号
	 * @param userId 人员主键
	 * @param busId 模块主键
	 * @param busType 业务类型
	 */
	public void delClockByUserId(Integer comId, Integer userId, Integer busId,
			String busType) {
		List<Clock> listClocks = clockDao.listUserClockForDel(comId,userId,busId,busType);
		if(null!=listClocks && listClocks.size()>0){
			for (Clock clock : listClocks) {
				Integer id = clock.getId();
				//不要定时了
				QuartzManager.disableSchedule("task_"+id, "group_"+id);
				
				//删除待办事项或是消息提醒
				clockDao.delByField("todayworks", new String[]{"comId","clockId"}, new Object[]{comId,id});
				//删除待办事项或是消息提醒
				clockDao.delByField("clockPerson", new String[]{"comId","clockId"}, new Object[]{comId,id});
				//删除提醒方式
				clockDao.delByField("clockWay", new String[]{"comId","clockId"}, new Object[]{comId,id});
				//删除闹铃
				clockDao.delById(Clock.class, id);
			}
		}
		
	}

	/**
	 * 闹铃集合
	 * @param userInfo 操作人员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	public List<Clock> listClockForOne(UserInfo userInfo, Integer busId,
			String busType) {
		List<Clock> list = clockDao.listClockForOne(userInfo.getComId(),userInfo.getId(),busId,busType);
		return list;
	}
	
	/**
	 * 直接合并闹铃
	 * @param comId 企业号
	 * @param busOldId 待整合的项目主键
	 * @param busId 合并后的项目主键
	 * @param busType 业务类型
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void compressClock(Integer comId, Integer busOldId, Integer busId,
			String busType) {
		clockDao.compressClock(comId, busOldId, busId, busType);
		
	}
	
	/**
	 * 取得指定人员的所有闹铃
	 * @param userId 指定人员主键
	 * @param comId 企业号
	 * @return
	 */
	public List<Clock> listUserClock(Integer userId, Integer comId){
		List<Clock> list = clockDao.listUserClock(userId,comId);
		return list;
	}

	/**
	 * 删除特定人员的闹铃
	 * @param userId 人员主键
	 * @param comId 企业号
	 */
	public void delUserClock(Integer userId, Integer comId) {
		List<Clock> list = clockDao.listUserClock(userId,comId);
		if(null!=list && list.size()>0){
			for (Clock clock : list) {
				
				Integer id = clock.getId();
				
				//不要定时了
				QuartzManager.disableSchedule("task_"+id, "group_"+id);
				
				//删除待办事项或是消息提醒
				clockDao.delByField("todayworks", new String[]{"comId","clockId"}, new Object[]{comId,id});
				//删除待办事项或是消息提醒
				clockDao.delByField("clockPerson", new String[]{"comId","clockId"}, new Object[]{comId,id});
				//删除提醒方式
				clockDao.delByField("clockWay", new String[]{"comId","clockId"}, new Object[]{comId,id});
				//删除闹铃
				clockDao.delById(Clock.class, id);
			}
		}
	}

	/**
	 * 删除定时系统中数据
	 * @param userId 操作人员
	 * @param comId 企业号
	 */
	public void disableClock(Integer userId, Integer comId) {
		//只删除定时系统中数据，保留数据库数据
		List<Clock> list = clockDao.listUserClock(userId,comId);
		if(null!=list && list.size()>0){
			for (Clock clock : list) {
				Integer id = clock.getId();
				//不要定时了
				QuartzManager.disableSchedule("task_"+id, "group_"+id);
			}
		}
		
	}
	/**
	 * 重置只执行一次在服务器关闭期间有未执行的
	 */
	public void updateResetClock() {
		clockDao.updateResetClock();
		
	}

	/**
	 * 添加会议闹铃
	 * @param meeting
	 * @param userInfo
	 * @param clockDate 
	 * @param content
	 * @param type
	 */
	public void addMeetingClock(Meeting meeting, UserInfo userInfo, String clockDate, String content, String type) {
		//一个会议只有一个闹铃，周期会议有两个
		List<Clock> clocks = clockDao.listClockForOne(userInfo.getComId(), userInfo.getId(), 
				meeting.getId(), ConstantInterface.TYPE_MEETING);
		Clock clock = new Clock();
		if(null != clocks && clocks.size()>0){
			clock = clocks.get(0);
			//闹铃开始日期
			clock.setClockDate(clockDate.substring(0,10));
			//闹铃下次执行开始日期
			clock.setClockNextDate(clockDate);
			//闹铃开始时间
			clock.setClockTime(clockDate.substring(11,clockDate.length()));
			//重新设置闹铃信息
			clock.setContent(content);
		}else{
			//设置企业号
			clock.setComId(userInfo.getComId());
			//创建人员
			clock.setUserId(userInfo.getId());
			//提醒内容
			clock.setContent(content);
			//闹铃开始日期
			clock.setClockDate(clockDate.substring(0,10));
			//闹铃下次执行开始日期
			clock.setClockNextDate(clockDate);
			//闹铃开始时间
			clock.setClockTime(clockDate.substring(11,clockDate.length()));
			//重复类型,不重复
			clock.setClockRepType("0");
			//默认开启
			clock.setClockIsOn("1");
			//业务主键
			clock.setBusId(meeting.getId());
			//业务类型
			clock.setBusType(ConstantInterface.TYPE_MEETING);
			//发送普通消息
			clock.setClockMsgType("1");
		}
		if(type.startsWith("0")){
			if(type.equals("01")){//添加闹铃，未执行
				clock.setExecuteState("0");
				clockDao.add(clock);
			}else if(type.equals("02")){//添加闹铃，待执行
				//待执行
				clock.setExecuteState("1");
				Integer clockId =  clockDao.add(clock);
				clock.setId(clockId);
				String cornTime;
				try {
					cornTime = DateTimeUtil.transCorn(clockDate);
					CustomJob job = new CustomJob();
					job.setJobId("task_"+clock.getId());
					job.setJobGroup("group_"+clock.getId());
					job.setCronExpression(cornTime);//每五秒执行一次
					job.setStateFulljobExecuteClass(QuartzJobOne.class);
					
					JobDataMap paramsMap = new JobDataMap();
					paramsMap.put("task_"+clock.getId(),clock);
					QuartzManager.enableCronSchedule(job, paramsMap, true);
				} catch (ParseException e) {
					e.printStackTrace();
				}catch (Exception e) {//定时的时间已过
				}
			}
		}else if(type.startsWith("1")){
			if(type.equals("11")){//修改闹铃，未执行
				//只需要修改
				clock.setExecuteState("0");
				if(null != clock.getId()){
					clockDao.update(clock);
				}else{
					//添加
					Integer clockId =  clockDao.add(clock);
					clock.setId(clockId);
				}
			}else if(type.equals("12")){//修改闹铃，已执行
				//只需要修改
				clock.setExecuteState("1");
				if(null != clock.getId()){
					clockDao.update(clock);
				}else{
					Integer clockId =  clockDao.add(clock);
					clock.setId(clockId);
				}
			}
			String cornTime;
			try {
				cornTime = DateTimeUtil.transCorn(clockDate);
				CustomJob job = new CustomJob();
				job.setJobId("task_"+clock.getId());
				job.setJobGroup("group_"+clock.getId());
				job.setCronExpression(cornTime);//每五秒执行一次
				job.setStateFulljobExecuteClass(QuartzJobOne.class);
				
				JobDataMap paramsMap = new JobDataMap();
				paramsMap.put("task_"+clock.getId(),clock);
				QuartzManager.enableCronSchedule(job, paramsMap, true);
			} catch (ParseException e) {
				e.printStackTrace();
			}catch (Exception e) {//定时的时间已过
			}
		}
		
	}
	
	
}
