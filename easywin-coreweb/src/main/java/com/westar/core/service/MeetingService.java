package com.westar.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.westar.base.model.MeetCheckUser;
import com.westar.base.model.MeetDep;
import com.westar.base.model.MeetLearn;
import com.westar.base.model.MeetLog;
import com.westar.base.model.MeetPerson;
import com.westar.base.model.MeetRegular;
import com.westar.base.model.MeetTalk;
import com.westar.base.model.MeetTalkFile;
import com.westar.base.model.Meeting;
import com.westar.base.model.MeetingRoom;
import com.westar.base.model.NoticePerson;
import com.westar.base.model.RoomApply;
import com.westar.base.model.ShareUser;
import com.westar.base.model.SummaryFile;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WeekRepTalkFile;
import com.westar.base.model.WeekRepUpfiles;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.pojo.ModFormStepData;
import com.westar.base.util.BeanUtilEx;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.MeetingDao;
import com.westar.core.thread.IndexUpdateThread;
import com.westar.core.thread.sendPhoneMsgThread;

@Service
public class MeetingService {

	@Autowired
	MeetingDao meetingDao;
	
	@Autowired
	MeetingRoomService meetingRoomService;
	
	@Autowired
	ClockService clockService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	IndexService indexService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	ModFlowService modFlowService;
	
	@Autowired
	ForceInPersionService forceInPersionService;
	
	@Autowired
	MeetSummaryService meetSummaryService;
	
	@Autowired
	PhoneMsgService phoneMsgService;
	
	@Autowired
	TodayWorksService todayWorksService;

	/**
	 * 分页查询会议信息
	 * @param meetingRoom 会议室属性条件
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public List<Meeting> listPagedMeeting(UserInfo userInfo,
			Meeting meeting) {
		List<Meeting>  meetings = meetingDao.listPagedMeeting(meeting,userInfo);
		return meetings;
	}
	/**
	 * 待开会议
	 * @param userInfo
	 * @param meeting
	 * @return
	 */
	public List<Meeting> listPagedSpFlowMeeting(UserInfo userInfo, Meeting meeting) {
		boolean isForceInPersion = forceInPersionService.isForceInPersion(userInfo, ConstantInterface.TYPE_MEETING);
		List<Meeting>  meetings = meetingDao.listPagedSpFlowMeeting(meeting,userInfo,isForceInPersion);
		return meetings;
	}
	/**
	 * 待开会议
	 * @param userInfo
	 * @param meeting
	 * @return
	 */
	public List<Meeting> listPagedTodoMeeting(UserInfo userInfo, Meeting meeting) {
		List<Meeting>  meetings = meetingDao.listPagedTodoMeeting(meeting,userInfo);
		return meetings;
	}
	/**
	 * 已开会议
	 * @param userInfo
	 * @param meeting
	 * @return
	 */
	public List<Meeting> listPagedDoneMeeting(UserInfo userInfo, Meeting meeting) {
		boolean isForceInPersion = forceInPersionService.isForceInPersion(userInfo, ConstantInterface.TYPE_MEETING);
		List<Meeting>  meetings = meetingDao.listPagedDoneMeeting(meeting,userInfo,isForceInPersion);
		return meetings;
	}
	/**
	 * 已开会议
	 * @param userInfo
	 * @param meeting
	 * @return
	 */
	public List<Meeting> listPagedMeetWithSpSummary(UserInfo userInfo, Meeting meeting) {
		boolean isForceInPersion = forceInPersionService.isForceInPersion(userInfo, ConstantInterface.TYPE_MEETING);
		List<Meeting>  meetings = meetingDao.listPagedMeetWithSpSummary(meeting,userInfo,isForceInPersion);
		return meetings;
	}
	/**
	 * 添加会议
	 * @param meeting
	 * @param userInfo
	 * @throws Exception 
	 */
	public void addMeeting(Meeting meeting, UserInfo userInfo) throws Exception {
		//设置企业号
		meeting.setComId(userInfo.getComId());
		//设置发起人
		meeting.setOrganiser(userInfo.getId());
		String roomType = meeting.getRoomType();
		if(roomType.equals("1")){//是外部会议室
			meeting.setMeetingAddrId(0);
		}
		//会议会议类型 是否为单次
		String meetingType = meeting.getMeetingType();
		if(null!=meetingType && !meetingType.equals("0")){
			MeetRegular meetRegular = meeting.getMeetRegular();
			if(null!=meetRegular){
				Map<String,String> startEndMap = CommonUtil.getMeetThisDate(meeting, meetRegular);
				//会议下次开始时间
				String startDate = startEndMap.get("startDate");
				//会议下次结束时间
				String endDate = startEndMap.get("endDate");
				if(null!=startDate && null!=endDate ){//说明还有下次执行的机会
					meeting.setStartDate(startDate);
					meeting.setEndDate(endDate);
				}else{
					meeting.setMeetingType("0");
				}
			}else{//没有设置周期
				meeting.setMeetingType("0");
			}
		}
		//添加会议
		Integer meetingId = meetingDao.add(meeting);
		//设置会议主键
		meeting.setId(meetingId);
		
		//添加会议审批信息
		this.addMeetingSpFlow(meeting, userInfo);
		//审批状态
		Integer spState = meeting.getSpState();
		boolean meetingDoneState = (null== spState || spState.equals(0) || spState.equals(4));
		
		//会议是否发布
		Integer meetingState = meeting.getMeetingState();
				
		//需要申请会议室
		if(roomType.equals("0") && meetingDoneState
				&& 1==meetingState && meeting.getMeetingAddrId()>0){
			//会议室申请
			RoomApply roomApply = new RoomApply();
			//企业号
			roomApply.setComId(userInfo.getComId());
			//会议室主键
			roomApply.setRoomId(meeting.getMeetingAddrId());
			//会议主键
			roomApply.setMeetingId(meetingId);
			//会议开始时间
			roomApply.setStartDate(meeting.getStartDate());
			//会议结束时间
			roomApply.setEndDate(meeting.getEndDate());
			//会议室申请人员
			roomApply.setUserId(userInfo.getId());
			
			MeetingRoom meetRoom = meetingRoomService.getMeetingRoomById(meeting.getMeetingAddrId(), userInfo.getComId());
			if(meetRoom.getMamager().equals(userInfo.getId())){//会议发起人是会议室管理员，则不需要审核
				//申请状态
				roomApply.setState("1");
				
				meetingDao.add(roomApply);
			}else{
				//申请状态
				roomApply.setState("0");
				
				meetingDao.add(roomApply);
				//申请会议室
				todayWorksService.addTodayWorks(userInfo, meetRoom.getMamager(), meetingId,
						"会议《"+meeting.getTitle()+"》申请会议室《"+meetRoom.getRoomName()+"》", ConstantInterface.TYPE_MEETROOM, null, null);
			}
			
		}
		//添加会议参会范围
		this.addMeetingScope(meeting, userInfo, meetingId);
		if(meetingState==0){//保存会议
			//添加会议日志
			this.addMeetLog(userInfo.getComId(), meetingId, userInfo.getId(), userInfo.getUserName(), "保存会议");
		}else{
			//添加会议日志
			this.addMeetLog(userInfo.getComId(), meetingId, userInfo.getId(), userInfo.getUserName(), "发布会议");
			
			meeting.setId(meetingId);
			if(meetingDoneState){//会议不需要审批或是审批通过了
				//会议开始时间
				String startDateStr = meeting.getStartDate();
				//提醒时间
				String clockDateStr = startDateStr;
				//会提前提醒时间
				String aheadTime = meeting.getAheadTime();
				if("1".equals(aheadTime)){//提前5分钟
					clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -5);
				}else if("2".equals(aheadTime)){//15分钟
					clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -15);
				}else if("3".equals(aheadTime)){//30分钟
					clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -30);
				}else if("4".equals(aheadTime)){//1小时
					clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.HOUR_OF_DAY, -1);
				}else if("5".equals(aheadTime)){//1天
					clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.DAY_OF_YEAR, -1);
				}
				//当前时间
				String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
				//当前时间一小时后
				String hourLaterStr = DateTimeUtil.addDate(nowDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.HOUR_OF_DAY, 1);
				
				//当前时间
				Date nowDate = DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
				//当前时间1小时后
				Date hourLaterDate = DateTimeUtil.parseDate(hourLaterStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
				//提醒时间
				Date remindDate = DateTimeUtil.parseDate(clockDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
				//会议开始时间
				Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
				
				//消息提醒内容
				String content = "安排会议《"+meeting.getTitle()+"》,开始于"+DateTimeUtil.formatDate(startDate, DateTimeUtil.c_yyyy_MM_dd_)
						+","+DateTimeUtil.formatDate(startDate, DateTimeUtil.HH_mm_ss).substring(0,5);
				
				if(hourLaterDate.before(remindDate)){//提醒时间在当前时间的1小时后
					clockService.addMeetingClock(meeting,userInfo,clockDateStr,content,"01");
				}else if(nowDate.before(remindDate) && hourLaterDate.after(remindDate)){//提醒时间在当前时间1小时内
					clockService.addMeetingClock(meeting,userInfo,clockDateStr,content,"02");
				}else{//提醒时间在当前时间以前
					List<UserInfo> shares = meetingDao.listMeetingUsers(userInfo.getComId(), meetingId);
					//添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo,userInfo.getId(), meetingId, content, ConstantInterface.TYPE_MEETING, shares,null);
					
					String msgSendFlag = meeting.getSendPhoneMsg();
					//需要设置短信
					if(StringUtils.isNotEmpty(msgSendFlag) 
							&& msgSendFlag.equals(ConstantInterface.MSG_SEND_YES)){
						//单线程池
						ExecutorService pool = ThreadPoolExecutor.getInstance();
						//跟范围人员发送通知消息
						pool.execute(new sendPhoneMsgThread(phoneMsgService, userInfo.getComId(), shares, 
								new Object[]{content}, ConstantInterface.MSG_JOB_TO_DO, userInfo.getOptIP()));
					}
				}
				//添加参会人员确认单
				this.addMeetCheckUser(userInfo,meetingId,1,"");
			}
		}
		
		if(null!=meetingType && !meetingType.equals("0")){
			MeetRegular meetRegular = meeting.getMeetRegular();
			if(null!=meetRegular){
				//设置会议主键
				meetRegular.setMeetingId(meetingId);
				//设置企业号
				meetRegular.setComId(userInfo.getComId());
				meetingDao.add(meetRegular);
			}
		}
		
		try {
			this.updateMeetIndex(meetingId, userInfo, "add");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 添加会议的时候发起流程
	 * @param meeting 会议信息
	 * @param userInfo 当前操作人员
	 * @throws Exception
	 */
	private void addMeetingSpFlow(Meeting meeting, UserInfo userInfo) throws Exception {
		Integer meetingId = meeting.getId();
		//初始化流程信息
		Map<String,Object> paramMap = modFlowService.constrModFlowConf(meeting.getModFlowConfStr());
		Map<String,Object> result = null;
		if(paramMap.get("spType").equals("1")){//审批类的模块
			//审批状态，默认审核中
			meeting.setSpState(1);
			if(paramMap.get("flowType").equals("1")){//固定流程
				//流程主键
				Integer flowId = (Integer) paramMap.get("flowId");
				meeting.setFlowId(flowId);
				//流程部署信息
				String processInstanceId = modFlowService.initModFlowStartConf(userInfo,paramMap,meetingId,ConstantInterface.TYPE_MEETING_SP);
				meeting.setActInstaceId(processInstanceId);
				paramMap.put("actInstaceId", processInstanceId);
				//设置流程信息
				result = modFlowService.updateModFlowNextStep(userInfo,paramMap,meetingId,ConstantInterface.TYPE_MEETING_SP);
				if(result.get("status").toString().equals("f")){
					throw new Exception(result.get("info").toString());
				}else{
					//审批结果
					String spState = result.get("spState").toString();
					if(spState.equals("refuse")){//流程终止
						meeting.setSpState(3);
					}else if(spState.equals("finish")){//流程正常办结
						meeting.setSpState(2);
					}else if(spState.equals("next")){//流程正常办下一步骤
						Set<Integer> exectors = (Set<Integer>) result.get("exectors");
						modFlowService.addModFlowTodo(meeting.getTitle(), userInfo, meetingId,ConstantInterface.TYPE_MEETING_SP, paramMap, exectors);
					}else if(spState.equals("huiqian")){//流程是会签
						todayWorksService.delTodoWork(meetingId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING_SP, "1");
					}
				}
				meetingDao.update(meeting);
			}else{
				List<UserInfo> stepUsers = (List<UserInfo>) paramMap.get("stepUsers");
				ModFormStepData modFormStepData = new ModFormStepData();
				Map<String,Object> freeMap = modFlowService.initFreeFlowStartConf(userInfo, stepUsers, meetingId, ConstantInterface.TYPE_MEETING_SP);
				modFormStepData.setNextStepUsers((List<UserInfo>) freeMap.get("nextStepUsers"));
				modFormStepData.setNextStepId(Integer.parseInt(freeMap.get("nextStepId").toString()));
				modFormStepData.setSpState(1);
				Gson gson = new Gson();
				paramMap.put("modFormStepData", modFormStepData);
				String processInstanceId = (String) freeMap.get("actInstaceId");
				meeting.setActInstaceId(processInstanceId);

				paramMap.put("actInstaceId", processInstanceId);
				//设置流程信息
				result = modFlowService.updateModFlowNextStep(userInfo,paramMap,meetingId,ConstantInterface.TYPE_MEETING_SP);
				if(result.get("status").toString().equals("f")){
					throw new Exception(result.get("info").toString());
				}else{
					//审批结果
					String spState = result.get("spState").toString();
					if(spState.equals("refuse")){//流程终止
						meeting.setSpState(3);
					}else if(spState.equals("finish")){//流程正常办结
						meeting.setSpState(2);
					}else if(spState.equals("next")){//流程正常办下一步骤
						Set<Integer> exectors = (Set<Integer>) result.get("exectors");
						modFlowService.addModFlowTodo(meeting.getTitle(), userInfo, meetingId,ConstantInterface.TYPE_MEETING_SP, paramMap, exectors);
					}else if(spState.equals("huiqian")){//流程是会签
						todayWorksService.delTodoWork(meetingId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING_SP, "1");
					}
				}
				meetingDao.update(meeting);
			}
		}else{
			//审批状态，默认不审核
			meeting.setSpState(0);
			meetingDao.update(meeting);
		}
	}
	/**
	 * 添加参会人员以及原因
	 * @param userInfo
	 * @param meetingId
	 * @param state
	 * @param reason
	 */
	public void addMeetCheckUser(UserInfo userInfo, Integer meetingId, Integer state,
			String reason) {
		//查询是否确认过事由
		MeetCheckUser meetCheckUser = meetingDao.getMeetCheckUser(userInfo.getComId(),userInfo.getId(),meetingId);
		if(null==meetCheckUser){
			//添加参会
			meetCheckUser = new MeetCheckUser();
			//设置企业号
			meetCheckUser.setComId(userInfo.getComId());
			//设置会议主键
			meetCheckUser.setMeetingId(meetingId);
			//自己参会
			meetCheckUser.setState(state);
			//设置参会人员
			meetCheckUser.setUserId(userInfo.getId());
			//设置原因
			meetCheckUser.setReason(reason);
			meetingDao.add(meetCheckUser);
		}else{
			if(null==reason){
				reason="";
			}
			meetCheckUser.setState(state);
			meetCheckUser.setReason(reason);
			meetingDao.update(meetCheckUser);
		}
		//将待办事项设置成普通事项
		todayWorksService.updateWorkToNormal(meetingId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING);
	}
	/**
	 * 通过会议主键取得会议信息
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 * @param batchUpdate 批量修改传数据
	 */
	public Meeting getMeetingById(Integer meetingId,Integer comId, String batchUpdate) {
		Meeting meeting = meetingDao.getMeetingById(meetingId,comId);
		if(null!=meeting){
			//与会人员
			meeting.setListMeetPerson(meetingDao.listMeetPerson(meetingId,comId));
			//与会部门
			meeting.setListMeetDep(meetingDao.listMeetDep(meetingId,comId));
			//告知人员
			meeting.setListNoticePerson(meetingDao.listNoticePerson(meetingId,comId));
			
			if(meeting.getMeetingState()==1 //会议已发布
					&& !meeting.getMeetingType().equals("0")//是周期会议 
					&& null!=batchUpdate){//需要查看周期属性
				MeetRegular meetRegular = meetingDao.getMeetRegular(meetingId,comId);
				meeting.setMeetRegular(meetRegular);
			}else if(meeting.getMeetingState()==0 //会议未发布
					&& !meeting.getMeetingType().equals("0")){//是周期会议 
				MeetRegular meetRegular = meetingDao.getMeetRegular(meetingId,comId);
				meeting.setMeetRegular(meetRegular);
			}
		}
		return meeting;
		
	}
	
	/**
	 * 修改会议
	 * @param meeting 会议属性
	 * @param userInfo 操作员
	 * @param preMeetId 分离会议周期性
	 * @param method 是否分离周期会议
	 * @throws Exception 
	 */
	public void updateMeeting(Meeting meeting, UserInfo userInfo, String method, Integer preMeetId) throws Exception {
		
		if(method.equals("add")){//修改的是单次周期会议(修改原有会议的的下次执行时间，并添加本次修改的会议)
			//修改周期会议的起止时间
			MeetRegular meetRegular = meetingDao.getMeetRegular(preMeetId, userInfo.getComId());
			//原有会议
			Meeting preMeeting = (Meeting) meetingDao.objectQuery(Meeting.class, preMeetId);
			//原有会议的下次执行时间
			Map<String, String> nextDateMap = CommonUtil.getMeetNextDate(preMeeting,meetRegular);
			if(null!=nextDateMap){//没有下次执行时间，就没修改原有的的会议为单次会议
				//会议下次开始时间
				String nextStartDate = nextDateMap.get("startDate");
				//会议下次结束时间
				String nextEndDate = nextDateMap.get("endDate");
				if(null!=nextStartDate && null!=nextEndDate ){//说明还有下次执行的机会
					//重新设置会议的开始时间和截止时间
					preMeeting.setStartDate(nextStartDate);
					preMeeting.setEndDate(nextEndDate);
					//修改原有会议的下次执行时间
					meetingDao.update(preMeeting);
					
					//原有会议的申请，此时修改申请的时间
					if(preMeeting.getRoomType().equals("0") && preMeeting.getMeetingAddrId()>0){//申请的是公司会议室
						//删除上次申请发送的会议室申请消息
						meetingDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, 
								new Object[]{userInfo.getComId(),ConstantInterface.TYPE_MEETROOM,preMeetId});
						//会议室信息
						MeetingRoom meetRoom = meetingRoomService.getMeetingRoomById(meeting.getMeetingAddrId(), userInfo.getComId());
						//会议室申请
						RoomApply roomApply = new RoomApply();
						//企业号
						roomApply.setComId(userInfo.getComId());
						//会议室主键
						roomApply.setRoomId(meeting.getMeetingAddrId());
						//会议主键
						roomApply.setMeetingId(preMeetId);
						//会议开始时间
						roomApply.setStartDate(nextStartDate);
						//会议结束时间
						roomApply.setEndDate(nextEndDate);
						//会议室申请人员
						roomApply.setUserId(userInfo.getId());
						
						if(meetRoom.getMamager().equals(userInfo.getId())){//会议发起人是会议室管理员，则不需要审核
							//申请状态
							roomApply.setState("1");
							meetingDao.add(roomApply);
						}else{
							//申请状态
							roomApply.setState("0");
							
							meetingDao.add(roomApply);
							//申请会议室
							todayWorksService.addTodayWorks(userInfo, meetRoom.getMamager(), preMeetId,
									"会议《"+meeting.getTitle()+"》申请会议室《"+meetRoom.getRoomName()+"》", ConstantInterface.TYPE_MEETROOM, null, null);
						}
					}
					//删除与会人员确认单
					meetingDao.delByField("meetCheckUser", new String[]{"comId","meetingId"}, new Object[]{userInfo.getComId(),preMeetId});
					//删除待办事项
					meetingDao.delByField("todayworks", new String[]{"comId","busType","busId"},
							new Object[]{userInfo.getComId(),ConstantInterface.TYPE_MEETING,preMeetId});
					//删除定时会议信息
					clockService.delClockByUserId(userInfo.getComId(),userInfo.getId(), preMeetId, ConstantInterface.TYPE_MEETING);
					
					//会议开始时间
					String startDateStr = meeting.getStartDate();
					//提醒时间
					String clockDateStr = startDateStr;
					//会提前提醒时间
					String aheadTime = meeting.getAheadTime();
					if("1".equals(aheadTime)){//提前5分钟
						clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -5);
					}else if("2".equals(aheadTime)){//15分钟
						clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -15);
					}else if("3".equals(aheadTime)){//30分钟
						clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -30);
					}else if("4".equals(aheadTime)){//1小时
						clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.HOUR_OF_DAY, -1);
					}else if("5".equals(aheadTime)){//1天
						clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.DAY_OF_YEAR, -1);
					}
					
					//当前时间
					String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
					//当前时间一小时后
					String hourLaterStr = DateTimeUtil.addDate(nowDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.HOUR_OF_DAY, 1);
					
					//当前时间
					Date nowDate = DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
					//当前时间1小时后
					Date hourLaterDate = DateTimeUtil.parseDate(hourLaterStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
					//提醒时间
					Date remindDate = DateTimeUtil.parseDate(clockDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
					//会议开始时间
					Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
					
					//消息提醒内容
					String content = "安排会议《"+meeting.getTitle()+"》,开始于"+DateTimeUtil.formatDate(startDate, DateTimeUtil.c_yyyy_MM_dd_)
							+","+DateTimeUtil.formatDate(startDate, DateTimeUtil.HH_mm_ss).substring(0,5);
					
					//添加参会人员确认单
					this.addMeetCheckUser(userInfo,preMeetId,1,"");
					//添加本次会议
					this.addMeeting(meeting, userInfo);
					
					if(hourLaterDate.before(remindDate)){//提醒时间在当前时间的1小时后
						clockService.addMeetingClock(meeting,userInfo,clockDateStr,content,"01");
					}else if(nowDate.before(remindDate) && hourLaterDate.after(remindDate)){//提醒时间在当前时间1小时内
						clockService.addMeetingClock(meeting,userInfo,clockDateStr,content,"02");
					}else{//提醒时间在当前时间以前
						List<UserInfo> shares = meetingDao.listMeetingUsers(userInfo.getComId(), preMeetId);
						//添加待办提醒通知
						todayWorksService.addTodayWorks(userInfo,userInfo.getId(), preMeetId, content, ConstantInterface.TYPE_MEETING, shares,null);
					}
				
				}else{
					//设置会议的主键
					meeting.setId(preMeetId);
					method = "update";
				}
			}else{
				//设置会议的主键
				meeting.setId(preMeetId);
				method = "update";
			}
		}
		if(method.equals("update")){//修改会议信息
			//会议主键
			Integer meetingId = meeting.getId();
			String actInstaceId = meeting.getActInstaceId();
			String modFlowConfStr = meeting.getModFlowConfStr();
			//流转流程
			//审批状态
			Integer spState =0;
			if(!StringUtils.isEmpty(actInstaceId) && !StringUtils.isEmpty(modFlowConfStr)){
				JSONObject modFormStepDataObj = JSONObject.parseObject(modFlowConfStr);
				String modFormStepDataStr = modFormStepDataObj.getString("modFormStepData");
				Map<String, Object> result = modFlowService.updateSpModFlow(userInfo, meetingId,ConstantInterface.TYPE_MEETING,
						actInstaceId , modFormStepDataStr);
				String spStateStr = result.get("spState").toString();
				spState = null == spState?0:Integer.parseInt(spStateStr);
			}
			
			String roomType = meeting.getRoomType();
			if(roomType.equals("1")){//是外部会议室
				meeting.setMeetingAddrId(0);
			}
			//会议会议类型 是否为单次
			String meetingType = meeting.getMeetingType();
			if(null!=meetingType && !meetingType.equals("0")){
				MeetRegular meetRegular = meeting.getMeetRegular();
				if(null!=meetRegular){
					Map<String,String> startEndMap = CommonUtil.getMeetThisDate(meeting, meetRegular);
					//会议下次开始时间
					String startDate = startEndMap.get("startDate");
					//会议下次结束时间
					String endDate = startEndMap.get("endDate");
					if(null!=startDate && null!=endDate ){//说明还有下次执行的机会
						meeting.setStartDate(startDate);
						meeting.setEndDate(endDate);
					}else{
						meeting.setMeetingType("0");
					}
				}else{//没有设置周期
					meeting.setMeetingType("0");
				}
			}
			meetingDao.update(meeting);
			//删除与会人员
			meetingDao.delByField("meetPerson", new String[]{"comId","meetingId"}, new Object[]{userInfo.getComId(),meetingId});
			//删除与会部门
			meetingDao.delByField("meetDep", new String[]{"comId","meetingId"}, new Object[]{userInfo.getComId(),meetingId});
			//删除告知人员
			meetingDao.delByField("noticePerson", new String[]{"comId","meetingId"}, new Object[]{userInfo.getComId(),meetingId});
			//删除与会人员确认单
			meetingDao.delByField("meetCheckUser", new String[]{"comId","meetingId"}, new Object[]{userInfo.getComId(),meetingId});
			//删除待办事项
			meetingDao.delByField("todayworks", new String[]{"comId","busType","busId"},
					new Object[]{userInfo.getComId(),ConstantInterface.TYPE_MEETING,meetingId});
			//会议是否发布
			Integer meetingState = meeting.getMeetingState();
			
			boolean meetingDoneState = (null== spState || spState.equals(0) || spState.equals(4));
			//需要申请会议室
			if(roomType.equals("0") && 1==meetingState && meetingDoneState){
				//是否需要申请会议室，默认需要
				Boolean flag = true;
				//查询是否改动过申请时间
				RoomApply roomApplyObj = meetingRoomService.getMeetRoomApply(userInfo.getComId(),meetingId);
				if(null!=roomApplyObj){//以前申请过
					if(roomApplyObj.getStartDate().equals(meeting.getStartDate())//开始时间没变
							&& roomApplyObj.getEndDate().equals(meeting.getEndDate())//结束时间没有变
							&& roomApplyObj.getRoomId().equals(meeting.getMeetingAddrId())){//会议室没有变
						//不需要重新申请
						flag = false;
						if(roomApplyObj.getState().equals("2") ){
							roomApplyObj.setState("0");
							meetingDao.update(roomApplyObj);
							
							//会议室信息
							MeetingRoom meetRoom = meetingRoomService.getMeetingRoomById(meeting.getMeetingAddrId(), userInfo.getComId());
							//申请会议室
							todayWorksService.addTodayWorks(userInfo, meetRoom.getMamager(), meeting.getId(),
									"会议《"+meeting.getTitle()+"》申请会议室《"+meetRoom.getRoomName()+"》", ConstantInterface.TYPE_MEETROOM, null, null);
						};
					}else{//申请时间有变动，需要重新处理
						//删除会议室申请
						meetingDao.delByField("roomApply", new String[]{"comId","meetingId"}, new Object[]{userInfo.getComId(),meetingId});
						//删除上次申请发送的会议室申请消息
						meetingDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, 
								new Object[]{userInfo.getComId(),ConstantInterface.TYPE_MEETROOM,meetingId});
					}
				}
				//添加申请
				if(flag && meeting.getMeetingAddrId()>0 && meetingDoneState){
					//会议室申请
					RoomApply roomApply = new RoomApply();
					//企业号
					roomApply.setComId(userInfo.getComId());
					//会议室主键
					roomApply.setRoomId(meeting.getMeetingAddrId());
					//会议主键
					roomApply.setMeetingId(meetingId);
					//会议开始时间
					roomApply.setStartDate(meeting.getStartDate());
					//会议结束时间
					roomApply.setEndDate(meeting.getEndDate());
					//会议室申请人员
					roomApply.setUserId(userInfo.getId());
					
					//会议室信息
					MeetingRoom meetRoom = meetingRoomService.getMeetingRoomById(meeting.getMeetingAddrId(), userInfo.getComId());
					if(meetRoom.getMamager().equals(userInfo.getId())){//会议发起人是会议室管理员，则不需要审核
						//申请状态
						roomApply.setState("1");
						meetingDao.add(roomApply);
					}else{
						//申请状态
						roomApply.setState("0");
						
						meetingDao.add(roomApply);
						//申请会议室
						todayWorksService.addTodayWorks(userInfo, meetRoom.getMamager(), meeting.getId(),
								"会议《"+meeting.getTitle()+"》申请会议室《"+meetRoom.getRoomName()+"》", ConstantInterface.TYPE_MEETROOM, null, null);
					}
				}
			}else{//保存的会议，不需要申请会议室
				//删除会议室申请
				meetingDao.delByField("roomApply", new String[]{"comId","meetingId"}, new Object[]{userInfo.getComId(),meetingId});
				//删除上次申请发送的会议室申请消息
				meetingDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, 
						new Object[]{userInfo.getComId(),ConstantInterface.TYPE_MEETROOM,meetingId});
			}
			//添加会议的发言人信息
			this.addMeetingScope(meeting, userInfo, meetingId);
			
			if(meetingState==0){//保存的会议
				//添加会议日志
				this.addMeetLog(userInfo.getComId(), meeting.getId(), userInfo.getId(), userInfo.getUserName(), "保存会议");
				//删除定时信息
				clockService.delClockByUserId(userInfo.getComId(),userInfo.getId(),meeting.getId(), ConstantInterface.TYPE_MEETING);
			}else{
				//添加会议日志
				this.addMeetLog(userInfo.getComId(), meeting.getId(), userInfo.getId(), userInfo.getUserName(), "发布会议");
				if(meetingDoneState){
					//会议开始时间
					String startDateStr = meeting.getStartDate();
					//提醒时间
					String clockDateStr = startDateStr;
					//会提前提醒时间
					String aheadTime = meeting.getAheadTime();
					if("1".equals(aheadTime)){//提前5分钟
						clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -5);
					}else if("2".equals(aheadTime)){//15分钟
						clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -15);
					}else if("3".equals(aheadTime)){//30分钟
						clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -30);
					}else if("4".equals(aheadTime)){//1小时
						clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.HOUR_OF_DAY, -1);
					}else if("5".equals(aheadTime)){//1天
						clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.DAY_OF_YEAR, -1);
					}
					//当前时间
					String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
					//当前时间一小时后
					String hourLaterStr = DateTimeUtil.addDate(nowDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.HOUR_OF_DAY, 1);
					
					//当前时间
					Date nowDate = DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
					//当前时间1小时后
					Date hourLaterDate = DateTimeUtil.parseDate(hourLaterStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
					//提醒时间
					Date remindDate = DateTimeUtil.parseDate(clockDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
					//会议开始时间
					Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
					
					//消息提醒内容
					String content = "安排会议《"+meeting.getTitle()+"》,开始于"+DateTimeUtil.formatDate(startDate, DateTimeUtil.c_yyyy_MM_dd_)
							+","+DateTimeUtil.formatDate(startDate, DateTimeUtil.HH_mm_ss).substring(0,5);
					
					if(hourLaterDate.before(remindDate)){//提醒时间在当前时间的1小时后
						clockService.addMeetingClock(meeting,userInfo,clockDateStr,content,"11");
					}else if(nowDate.before(remindDate) && hourLaterDate.after(remindDate)){//提醒时间在当前时间1小时内
						clockService.addMeetingClock(meeting,userInfo,clockDateStr,content,"12");
					}else{//提醒时间在当前时间以前
						List<UserInfo> shares = meetingDao.listMeetingUsers(userInfo.getComId(), meetingId);
						//添加待办提醒通知
						todayWorksService.addTodayWorks(userInfo,userInfo.getId(), meetingId, content, ConstantInterface.TYPE_MEETING, shares,null);
						
						String msgSendFlag = meeting.getSendPhoneMsg();
						//需要设置短信
						if(StringUtils.isNotEmpty(msgSendFlag) 
								&& msgSendFlag.equals(ConstantInterface.MSG_SEND_YES)){
							//单线程池
							ExecutorService pool = ThreadPoolExecutor.getInstance();
							//跟范围人员发送通知消息
							pool.execute(new sendPhoneMsgThread(phoneMsgService, userInfo.getComId(), shares, 
									new Object[]{content}, ConstantInterface.MSG_JOB_TO_DO, userInfo.getOptIP()));
						}
					}
					//添加参会人员确认单
					this.addMeetCheckUser(userInfo,meetingId,1,"");
				}
			}
			if(null!=meetingType && !meetingType.equals("0")){
				MeetRegular meetregular = meeting.getMeetRegular();
				MeetRegular meetregularObj = meetingDao.getMeetRegular(meetingId, userInfo.getComId());
				if(null==meetregularObj){
					//设置会议主键
					meetregular.setMeetingId(meetingId);
					//设置企业号
					meetregular.setComId(userInfo.getComId());
					meetingDao.add(meetregular);
				}else{
					//设置周期时间
					meetregularObj.setRegularDate(meetregular.getRegularDate());
					meetregularObj.setStartDate(meetregular.getStartDate());
					meetregularObj.setEndDate(meetregular.getEndDate());
					meetingDao.update(meetregularObj);
				}
			}else{
				//删除会议周期
				meetingDao.delByField("meetRegular", new String[]{"comId","meetingId"}, new Object[]{userInfo.getComId(),meetingId});
				
			}
			
//			try {
//				this.updateMeetIndex(meetingId, userInfo, "update");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
	}
	
	/**
	 * 完成审批会议后添加会议申请和定时闹铃
	 * @param userInfo 当前操作人员
	 * @param busId 会议主键
	 */
	public void updateFinishSpMeeting(UserInfo userInfo, Integer meetingId){
		//删除会议室申请
		meetingDao.delByField("roomApply", new String[]{"comId","meetingId"}, 
				new Object[]{userInfo.getComId(),meetingId});
		//删除上次申请发送的会议室申请消息
		meetingDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, 
				new Object[]{userInfo.getComId(),ConstantInterface.TYPE_MEETROOM,meetingId});
		
		Meeting meeting = (Meeting) meetingDao.objectQuery(Meeting.class, meetingId);
		//是否为外部会议室
		String roomType = meeting.getRoomType();
		if(roomType.equals("1")){//是外部会议室
			meeting.setMeetingAddrId(0);
		}
		
		//会议会议类型 是否为单次
		String meetingType = meeting.getMeetingType();
		if(null!=meetingType && !meetingType.equals("0")){
			MeetRegular meetRegular = meeting.getMeetRegular();
			if(null!=meetRegular){
				Map<String,String> startEndMap = CommonUtil.getMeetThisDate(meeting, meetRegular);
				//会议下次开始时间
				String startDate = startEndMap.get("startDate");
				//会议下次结束时间
				String endDate = startEndMap.get("endDate");
				if(null!=startDate && null!=endDate ){//说明还有下次执行的机会
					meeting.setStartDate(startDate);
					meeting.setEndDate(endDate);
				}else{
					meeting.setMeetingType("0");
				}
			}else{//没有设置周期
				meeting.setMeetingType("0");
			}
		}
		
		//需要申请会议室
		if(roomType.equals("0")
				 && meeting.getMeetingAddrId()>0){
			//会议室申请
			RoomApply roomApply = new RoomApply();
			//企业号
			roomApply.setComId(userInfo.getComId());
			//会议室主键
			roomApply.setRoomId(meeting.getMeetingAddrId());
			//会议主键
			roomApply.setMeetingId(meetingId);
			//会议开始时间
			roomApply.setStartDate(meeting.getStartDate());
			//会议结束时间
			roomApply.setEndDate(meeting.getEndDate());
			//会议室申请人员
			roomApply.setUserId(userInfo.getId());
			
			MeetingRoom meetRoom = meetingRoomService.getMeetingRoomById(meeting.getMeetingAddrId(), userInfo.getComId());
			if(meetRoom.getMamager().equals(userInfo.getId())){//会议发起人是会议室管理员，则不需要审核
				//申请状态
				roomApply.setState("1");
				
				meetingDao.add(roomApply);
			}else{
				//申请状态
				roomApply.setState("0");
				
				meetingDao.add(roomApply);
				//申请会议室
				todayWorksService.addTodayWorks(userInfo, meetRoom.getMamager(), meetingId,
						"会议《"+meeting.getTitle()+"》申请会议室《"+meetRoom.getRoomName()+"》", ConstantInterface.TYPE_MEETROOM, null, null);
			}
			
		}
		
		//会议开始时间
		String startDateStr = meeting.getStartDate();
		//提醒时间
		String clockDateStr = startDateStr;
		//会提前提醒时间
		String aheadTime = meeting.getAheadTime();
		if("1".equals(aheadTime)){//提前5分钟
			clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -5);
		}else if("2".equals(aheadTime)){//15分钟
			clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -15);
		}else if("3".equals(aheadTime)){//30分钟
			clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -30);
		}else if("4".equals(aheadTime)){//1小时
			clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.HOUR_OF_DAY, -1);
		}else if("5".equals(aheadTime)){//1天
			clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.DAY_OF_YEAR, -1);
		}
		//当前时间
		String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		//当前时间一小时后
		String hourLaterStr = DateTimeUtil.addDate(nowDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.HOUR_OF_DAY, 1);
		
		//当前时间
		Date nowDate = DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
		//当前时间1小时后
		Date hourLaterDate = DateTimeUtil.parseDate(hourLaterStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
		//提醒时间
		Date remindDate = DateTimeUtil.parseDate(clockDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
		//会议开始时间
		Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
		
		//消息提醒内容
		String content = "安排会议《"+meeting.getTitle()+"》,开始于"+DateTimeUtil.formatDate(startDate, DateTimeUtil.c_yyyy_MM_dd_)
				+","+DateTimeUtil.formatDate(startDate, DateTimeUtil.HH_mm_ss).substring(0,5);
		
		if(hourLaterDate.before(remindDate)){//提醒时间在当前时间的1小时后
			clockService.addMeetingClock(meeting,userInfo,clockDateStr,content,"01");
		}else if(nowDate.before(remindDate) && hourLaterDate.after(remindDate)){//提醒时间在当前时间1小时内
			clockService.addMeetingClock(meeting,userInfo,clockDateStr,content,"02");
		}else{//提醒时间在当前时间以前
			List<UserInfo> shares = meetingDao.listMeetingUsers(userInfo.getComId(), meetingId);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,userInfo.getId(), meetingId, content, ConstantInterface.TYPE_MEETING, shares,null);
		}
	}
	/**添加会议查看范围的人员
	 * @param meeting
	 * @param userInfo
	 * @param meetingId
	 */
	private void addMeetingScope(Meeting meeting, UserInfo userInfo,
			Integer meetingId) {
		//与会人员
		List<MeetPerson> meetPersons = meeting.getListMeetPerson();
		if(null!=meetPersons && meetPersons.size()>0){
			for (MeetPerson meetPerson : meetPersons) {
				//设置企业号
				meetPerson.setComId(userInfo.getComId());
				//会议主键
				meetPerson.setMeetingId(meetingId);
				meetingDao.add(meetPerson);
			}
		}
		//与会部门
		List<MeetDep> meetDeps = meeting.getListMeetDep();
		if(null!=meetDeps && meetDeps.size()>0){
			for (MeetDep meetDep : meetDeps) {
				//设置企业号
				meetDep.setComId(userInfo.getComId());
				//会议主键
				meetDep.setMeetingId(meetingId);
				meetingDao.add(meetDep);
			}
		}
		//告知人员
		List<NoticePerson> noticePersons = meeting.getListNoticePerson();
		if(null!=noticePersons && noticePersons.size()>0){
			for (NoticePerson noticePerson : noticePersons) {
				//设置企业号
				noticePerson.setComId(userInfo.getComId());
				//会议主键
				noticePerson.setMeetingId(meetingId);
				meetingDao.add(noticePerson);
			}
		}
	}
	/**
	 * 撤销会议
	 * @param userInfo 操作人员
	 * @param ids 会议主键
	 */
	public void delRevokeMeeting(UserInfo userInfo, Integer[] ids) {
		//选出需要撤销的会议
		List<Meeting> listMeet =  meetingDao.listMeetingForRevoke(userInfo.getComId(),ids);
		if(null!=listMeet && listMeet.size()>0){
			for (Meeting meeting : listMeet) {
				//会议室类型
				String roomType = meeting.getRoomType();
				
				Integer id = meeting.getId();
				//删除会议申请
				meetingDao.delByField("roomApply", new String[]{"comId","meetingId"}, new Object[]{userInfo.getComId(),id});
				//删除上次申请发送的会议室申请消息
				meetingDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, 
						new Object[]{userInfo.getComId(),ConstantInterface.TYPE_MEETROOM,id});
				
				//删除上次申请发送的会议室申请消息
				meetingDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, 
						new Object[]{userInfo.getComId(),ConstantInterface.TYPE_MEETING,id});
				
				if("1".equals(roomType)){
					meeting.setMeetingAddrId(0);
					meeting.setMeetingAddrName("");
				}
				meeting.setMeetingState(0);
				meetingDao.update(meeting);
				//添加会议日志
				this.addMeetLog(userInfo.getComId(), meeting.getId(), userInfo.getId(), userInfo.getUserName(), "撤销发布会议");
				//撤销闹铃
				clockService.delClockByUserId(userInfo.getComId(), userInfo.getId(), meeting.getId(), ConstantInterface.TYPE_MEETING);
				
			}
		}
	}
	/**
	 * 删除会议
	 * @param comId企业号
	 * @param ids 会议主键
	 * @throws Exception 
	 */
	public void delMeeting(UserInfo userInfo, Integer[] ids) throws Exception {
		Integer comId = userInfo.getComId();
		for (Integer meetingId : ids) {
			//会议纪要附件
			List<SummaryFile> listSummaryFile = meetSummaryService.listSummaryFile(meetingId, comId);
			if(null!=listSummaryFile){
				for(SummaryFile vo:listSummaryFile){
					uploadService.updateUpfileIndex(vo.getUpfileId(), userInfo, "del",meetingId,ConstantInterface.TYPE_MEETING);
				}
			}
			//会议留言
			List<MeetTalkFile> listMeetTalkFile = meetingDao.listMeetTalkFile(comId, meetingId,null);
			if(null!=listMeetTalkFile){
				for(MeetTalkFile vo:listMeetTalkFile){
					uploadService.updateUpfileIndex(vo.getUpfileId(), userInfo, "del",meetingId,ConstantInterface.TYPE_MEETING);
				}
			}
			this.updateMeetIndex(meetingId, userInfo, "del");
			//删除与会人员
			meetingDao.delByField("meetPerson", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除与会部门
			meetingDao.delByField("meetDep", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除告知人员
			meetingDao.delByField("noticePerson", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除会议室申请
			meetingDao.delByField("roomApply", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除会议纪要附件
			meetingDao.delByField("summaryFile", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除会议纪要
			meetingDao.delByField("meetSummary", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除会议日志
			meetingDao.delByField("meetLog", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除会议留言附件
			meetingDao.delByField("meetTalkFile", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除会议留言
			meetingDao.delByField("meetTalk", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除会议周期设置
			meetingDao.delByField("meetregular", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除与会人员确认单
			meetingDao.delByField("meetCheckUser", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除定时器
			clockService.delClockByUserId(comId, null, meetingId, ConstantInterface.TYPE_MEETING);
			//删除审批模块数据
			modFlowService.delModSpFlow(userInfo, meetingId, ConstantInterface.TYPE_MEETING);
			
			//删除上次申请发送的会议室申请消息
			meetingDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, 
					new Object[]{comId,ConstantInterface.TYPE_MEETROOM,meetingId});
			
			//删除会议学习人员
			meetingDao.delByField("meetLearn", new String[]{"comId","meetingId"}, new Object[]{comId,meetingId});
			//删除会议
			meetingDao.delById(Meeting.class, meetingId);
			
		}
		
	}
	/**
	 * 取得会议信息
	 * @param meetingId 会议主键
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public Meeting getMeetForSummary(Integer meetingId, UserInfo userInfo) {
		Meeting meeting = meetingDao.getMeetingById(meetingId, userInfo.getComId());
		if(null!=meeting){
			//设置显示数量
			Integer talkNum = meetingDao.countTalk(userInfo.getComId(), meetingId);
			Integer fileNum = meetingDao.countFile(meetingId, userInfo.getComId());
			meeting.setFileNum(fileNum);
			meeting.setTalkNum(talkNum);
			//与会人员
			meeting.setListMeetPerson(meetingDao.listMeetPerson(meetingId,userInfo.getComId()));
			//与会部门
			meeting.setListMeetDep(meetingDao.listMeetDep(meetingId,userInfo.getComId()));
			//告知人员
			meeting.setListNoticePerson(meetingDao.listNoticePerson(meetingId,userInfo.getComId()));
		}
		return meeting;
	}
	
	/**
	 * 分页查询会议留言
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 * @return
	 */
	public List<MeetTalk> listPagedMeetTalk(Integer meetingId, Integer comId) {
		List<MeetTalk> list = meetingDao.listPagedMeetTalk(comId,meetingId);
		List<MeetTalk> meetTalks = new ArrayList<MeetTalk>();
		for (MeetTalk meetTalk : list) {
			//处理回复的内容
			meetTalk.setContent(StringUtil.toHtml(meetTalk.getContent()));
			meetTalks.add(meetTalk);
			meetTalk.setListMeetTalkFile(meetingDao.listMeetTalkFile(comId,meetingId,meetTalk.getId()));
		}
		return meetTalks;
	}
	/**
	 * 添加会议留言
	 * @param meetTalk
	 * @param userInfo
	 * @return
	 */
	public Integer addMeetTalk(MeetTalk meetTalk, UserInfo userInfo) {
		Integer id = meetingDao.add(meetTalk);
		Integer[] fileIds = meetTalk.getUpfilesId();
		if(null != fileIds && fileIds.length>0){
			for (Integer upfileId : fileIds) {
				MeetTalkFile meetTalkFile = new MeetTalkFile();
				//设置企业号
				meetTalkFile.setComId(userInfo.getComId());
				//会议主键
				meetTalkFile.setMeetingId(meetTalk.getMeetingId());
				//留言主键
				meetTalkFile.setTalkId(id);
				//附件主键
				meetTalkFile.setUpfileId(upfileId);
				//设置留言人主键
				meetTalkFile.setUserId(userInfo.getId());
				
				meetingDao.add(meetTalkFile);
			}
		}
		
		//添加信息学习人员
		List<MeetLearn> listMeetLearn = meetTalk.getListMeetLearn();
		Set<Integer> pushUserIdSet = new HashSet<Integer>();
		if(null != listMeetLearn && !listMeetLearn.isEmpty()){
			for (MeetLearn meetLearn : listMeetLearn) {
				//人员主键
				Integer userId = meetLearn.getUserId();
				pushUserIdSet.add(userId);
				//删除上次的学习人员
				meetingDao.delByField("meetLearn", new String[]{"comId","meetingId","userId"}, 
						new Object[]{userInfo.getComId(),meetTalk.getMeetingId(),userId});
				meetLearn.setMeetingId(meetTalk.getMeetingId());
				meetLearn.setComId(userInfo.getComId());
				meetingDao.add(meetLearn);
			}
		}
		
		//添加会议日志
		this.addMeetLog(userInfo.getComId(), meetTalk.getMeetingId(), userInfo.getId(), userInfo.getUserName(), "添加会议留言");
		
		//会议需要告知的人员
		List<UserInfo> shares = meetingDao.listMeetingViews(userInfo.getComId(), meetTalk.getMeetingId(), pushUserIdSet);
		//发送消息
		todayWorksService.addTodayWorks(userInfo, null, meetTalk.getMeetingId(), "添加会议留言", 
				ConstantInterface.TYPE_MEETING, shares, null);
		
//		try {
//			this.updateMeetIndex(meetTalk.getMeetingId(), userInfo, "update");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return id;
	}
	/**
	 * 查询单个的会议留言
	 * @param id留言主键
	 * @param comId 企业号
	 * @return
	 */
	public MeetTalk queryMeetTalk(Integer id, Integer comId) {
		MeetTalk meetTalk = meetingDao.queryMeetTalk(id,comId);
		//任务留言的附件
		if(null!=meetTalk){
			String content = StringUtil.toHtml(meetTalk.getContent());
			meetTalk.setContent(content);
			meetTalk.setListMeetTalkFile(meetingDao.listMeetTalkFile(comId,meetTalk.getMeetingId(),id));
		}
		return meetTalk;
	}
	/**
	 * 删除会议留言
	 * @param talkId
	 * @param delChildNode
	 * @param userInfo
	 * @throws Exception 
	 */
	public void delMeetTalk(Integer talkId, String delChildNode, UserInfo userInfo) throws Exception {
		//查询日程讨论是否存在
		MeetTalk meetTalk = (MeetTalk) meetingDao.objectQuery(MeetTalk.class, talkId);
		if(null!=meetTalk){
			//删除自己
			if(null==delChildNode){
				List<MeetTalkFile> listMeetTalkFile = meetingDao.listMeetTalkFile(userInfo.getComId(), meetTalk.getMeetingId(),null);
				if(null!=listMeetTalkFile){
					for(MeetTalkFile vo:listMeetTalkFile){
						uploadService.updateUpfileIndex(vo.getUpfileId(), userInfo, "del",meetTalk.getMeetingId(),ConstantInterface.TYPE_MEETING);
					}
				}
				//删除留言附件
				meetingDao.delByField("meetTalkFile", new String[]{"talkId","comId","meetingId"}, 
						new Object[]{talkId,userInfo.getComId(),meetTalk.getMeetingId()});
				meetingDao.delById(MeetTalk.class, talkId);
			}else if("yes".equals(delChildNode)){//删除自己和所有的子节点
				//留言
				List<MeetTalk> taskTalks = meetingDao.listMeetTalkForDel(userInfo.getComId(), talkId);
				if(null!=taskTalks && taskTalks.size()>0){
					for (MeetTalk meetTalkObj : taskTalks) {
						List<MeetTalkFile> listMeetTalkFile = meetingDao.listMeetTalkFile(meetTalkObj.getComId(), meetTalkObj.getMeetingId(),null);
						if(null!=listMeetTalkFile){
							for(MeetTalkFile vo:listMeetTalkFile){
								uploadService.updateUpfileIndex(vo.getUpfileId(), userInfo, "del",meetTalkObj.getMeetingId(),ConstantInterface.TYPE_MEETING);
							}
						}
						//删除留言附件
						meetingDao.delByField("meetTalkFile", new String[]{"talkId","comId","meetingId"}, 
								new Object[]{meetTalkObj.getId(),meetTalkObj.getComId(),meetTalkObj.getMeetingId()});
						//删除留言
						meetingDao.delById(MeetTalk.class, meetTalkObj.getId());
					}
				}
			}else if("no".equals(delChildNode)){//删除自己,将子节点提高一级
				//将子节点提高一级
				meetingDao.updateMeetTalkParentId(talkId,userInfo.getComId());
				List<MeetTalkFile> listMeetTalkFile = meetingDao.listMeetTalkFile(userInfo.getComId(), meetTalk.getMeetingId(),null);
				if(null!=listMeetTalkFile){
					for(MeetTalkFile vo:listMeetTalkFile){
						uploadService.updateUpfileIndex(vo.getUpfileId(), userInfo, "del",meetTalk.getMeetingId(),ConstantInterface.TYPE_MEETING);
					}
				}
				//删除留言附件
				meetingDao.delByField("meetTalkFile", new String[]{"talkId","comId","meetingId"}, 
						new Object[]{talkId,userInfo.getComId(),meetTalk.getMeetingId()});
				//删除自己
				meetingDao.delById(MeetTalk.class, talkId);
			}
		}
		//添加会议日志
		this.addMeetLog(userInfo.getComId(), meetTalk.getMeetingId(), userInfo.getId(), userInfo.getUserName(), "删除会议留言");
		//会议需要告知的人员
		List<UserInfo> shares = meetingDao.listMeetingViews(userInfo.getComId(), meetTalk.getMeetingId(),null);
		//发送消息
		todayWorksService.addTodayWorks(userInfo, null, meetTalk.getMeetingId(), "删除会议留言", 
				ConstantInterface.TYPE_MEETING, shares, null);
		
//		try {
//			this.updateMeetIndex(meetTalk.getMeetingId(), userInfo, "update");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	/**
	 * 分页查询会议日志
	 * @param meetingId
	 * @param comId
	 * @return
	 */
	public List<MeetLog> listPagedMeetLogs(Integer meetingId, Integer comId) {
		List<MeetLog> list = meetingDao.listPagedScheLog(comId,meetingId);
		return list;
	}
	
	/**
	 * 模块日志添加
	 * @param comId 企业号
	 * @param meetingId 会议主键
	 * @param userId 操作员主键
	 * @param userName 用户姓名
	 * @param content 日志内容
	 */
	public void addMeetLog(Integer comId, Integer meetingId, Integer userId,
			String userName, String content) {
		MeetLog meetLog = new MeetLog(comId,meetingId,userId,content,userName);
		meetingDao.add(meetLog);
	}
	/**
	 * 会议附件列表
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 * @return
	 */
	public List<SummaryFile> listPagedMeetUpfiles(Integer meetingId,
			Integer comId) {
		List<SummaryFile> listFiles = meetingDao.listPagedMeetUpfiles(meetingId,comId);
		return listFiles;
	}
	/**
	 * 会议参与人员
	 * @param comId 企业号
	 * @param busId 会议主键
	 * @return
	 */
	public List<UserInfo> listMeetingUsers(Integer comId,Integer busId) {
		//会议参与人员
		List<UserInfo> shares = meetingDao.listMeetingUsers(comId,busId);
		return shares;
	}
	/**
	 *会议告知和参与人员
	 * @param comId 企业号
	 * @param userId 操作员主键
	 * @param busId 业务主键
	 * @return
	 */
	public List<UserInfo> listMeetingViews(Integer comId,Integer busId) {
		//会议告知和参与人员
		List<UserInfo> shares = meetingDao.listMeetingViews(comId,busId,null);
		return shares;
	}
	/**
	 * 会议开始,再次邀请成员加入
	 * @param meeting
	 * @param userInfo
	 * @return 
	 */
	public Map<String, Object> updateMeetUser(Integer meetingId, UserInfo userInfo,Integer[] userIds) {
		if(null != userIds && userIds.length>0){
			Map<String, Object> map = new HashMap<String, Object>();
			//本次添加的参会成员
			List<UserInfo> userInDetailList = new ArrayList<UserInfo>();
			//本次正真邀请的成员
			List<Integer> userInList = new ArrayList<Integer>();
			
			//本次邀请的成员
			List<Integer> idAllList = new ArrayList<Integer>();
			for (Integer integer : userIds) {
				idAllList.add(integer);
			}
			//查询以前没有邀请的成员
			List<UserInfo> invPersons = meetingDao.listInvPerson(meetingId,userInfo.getComId(),idAllList.toArray());
			if(null!=invPersons && invPersons.size()>0){//本次有邀请成员
				for (UserInfo invPerson : invPersons) {
					MeetPerson meetPerson = new MeetPerson();
					//设置企业号
					meetPerson.setComId(userInfo.getComId());
					//会议主键
					meetPerson.setMeetingId(meetingId);
					//设置与会人员
					meetPerson.setUserId(invPerson.getId());
					meetingDao.add(meetPerson);
					
					UserInfo invUserInfo = userInfoService.getUserInfo(userInfo.getComId(), invPerson.getId());
					userInDetailList.add(invUserInfo);
					
					userInList.add(invPerson.getId());
					
					todayWorksService.delTodoWork(meetingId, userInfo.getComId(), invPerson.getId(), ConstantInterface.TYPE_MEETING, null);
				}
				
				Meeting meetingObj = (Meeting) meetingDao.objectQuery(Meeting.class, meetingId);
				String startDateStr = meetingObj.getStartDate();
				Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
				//消息提醒内容
				String content = "安排会议《"+meetingObj.getTitle()+"》,开始于"+DateTimeUtil.formatDate(startDate, DateTimeUtil.c_yyyy_MM_dd_)
						+","+DateTimeUtil.formatDate(startDate, DateTimeUtil.HH_mm_ss).substring(0,5);
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo,userInfo.getId(), meetingId, content, ConstantInterface.TYPE_MEETING, invPersons,null);
				
			}
			idAllList.removeAll(userInList);
			map.put("users", userInDetailList);
			map.put("removeUser", idAllList);
			
//			try {
//				this.updateMeetIndex(meetingId, userInfo, "update");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			return map;
		}else{
			return null;
		}
		
	}
	/**
	 * 列出已经参加会议的成员
	 * @param meetingId
	 * @param comId
	 * @return
	 */
	public Meeting getMeetForInvById(Integer meetingId, Integer comId) {
		Meeting meeting = meetingDao.getMeetingById(meetingId,comId);
		if(null!=meeting){
			//与会人员
			meeting.setListMeetPerson(meetingDao.listMeetPerson(meetingId,comId));
			//与会部门
			meeting.setListMeetDep(meetingDao.listMeetDep(meetingId,comId));
		}
		return meeting;
	}
	/**
	 * 检测会议状态
	 * @param id 会议主键
	 * @param comId 企业号
	 * @return
	 */
	public Meeting getMeetTimeOut(Integer id, Integer comId) {
		return meetingDao.checkMeetTimeOut(id,comId);
	}
	/**
	 * 周期会议结束后，生成新的会议
	 * @param comId
	 * @param busId
	 */
	public void addRegMeeting(Integer comId, Integer busId) {
		Meeting meeting = (Meeting) meetingDao.objectQuery(Meeting.class, busId);
		MeetRegular meetRegular = meetingDao.getMeetRegular(busId, comId);
		Map<String, String> nextDateMap = CommonUtil.getMeetNextDate(meeting,meetRegular);
		if(null!=nextDateMap){
			//会议下次开始时间
			String nextStartDate = nextDateMap.get("startDate");
			//会议下次结束时间
			String nextEndDate = nextDateMap.get("endDate");
			if(null!=nextStartDate && null!=nextEndDate ){//说明还有下次执行的机会
				Meeting regMeeting = new Meeting();
				BeanUtilEx.copyIgnoreNulls(meeting,regMeeting);
				//重新生成主键
				regMeeting.setId(null);
				//重新生成创建时间
				regMeeting.setRecordCreateTime(null);
				//设置下次开始时间
				regMeeting.setStartDate(nextStartDate);
				//设置下次结束时间
				regMeeting.setEndDate(nextEndDate);
				//添加下次要执行的会议
				Integer meetingId = meetingDao.add(regMeeting);
				//与会人员
				List<MeetPerson> meetPersons = meetingDao.listMeetPerson(busId, comId);
				if(null!=meetPersons && meetPersons.size()>0){//有与会人员
					for (MeetPerson meetPerson : meetPersons) {
						MeetPerson regMeetPerson = new MeetPerson();
						BeanUtilEx.copyIgnoreNulls(meetPerson,regMeetPerson);
						//不要主键
						regMeetPerson.setId(null);
						//不要创建时间
						regMeetPerson.setRecordCreateTime(null);
						//重新设置会议主键
						regMeetPerson.setMeetingId(meetingId);
						
						meetingDao.add(regMeetPerson);
					}
				}
				//与会部门
				List<MeetDep> meetDeps = meetingDao.listMeetDep(busId, comId);
				if(null!=meetDeps && meetDeps.size()>0){//有与会部门
					for (MeetDep meetDep : meetDeps) {
						MeetDep regMeetDep = new MeetDep();
						BeanUtilEx.copyIgnoreNulls(meetDep,regMeetDep);
						//不要主键
						regMeetDep.setId(null);
						//不要创建时间
						regMeetDep.setRecordCreateTime(null);
						//重新设置会议主键
						regMeetDep.setMeetingId(meetingId);
						
						meetingDao.add(regMeetDep);
					}
				}
				//告知人员
				List<NoticePerson> noticePersons = meetingDao.listNoticePerson(busId, comId);
				if(null != noticePersons && noticePersons.size()>0){
					for (NoticePerson noticePerson : noticePersons) {
						NoticePerson regNoticePerson = new NoticePerson();
						BeanUtilEx.copyIgnoreNulls(noticePerson,regNoticePerson);
						//不要主键
						noticePerson.setId(null);
						//不要创建时间
						noticePerson.setRecordCreateTime(null);
						//重新设置会议主键
						noticePerson.setMeetingId(meetingId);
						
						meetingDao.add(noticePerson);
					}
				}
				UserInfo user = (UserInfo) meetingDao.objectQuery(UserInfo.class, meeting.getOrganiser());
				UserInfo userInfo = new UserInfo();
				userInfo.setComId(comId);
				userInfo.setId(meeting.getOrganiser());
				userInfo.setUserName(user.getUserName());
				//添加会议日志
				this.addMeetLog(userInfo.getComId(), meetingId, userInfo.getId(), userInfo.getUserName(), "发布会议");
				
				regMeeting.setId(meetingId);
				
				meetRegular.setMeetingId(meetingId);
				meetingDao.update(meetRegular);
				//会议开始时间
				String startDateStr = regMeeting.getStartDate();
				//提醒时间
				String clockDateStr = startDateStr;
				//会提前提醒时间
				String aheadTime = regMeeting.getAheadTime();
				if("1".equals(aheadTime)){//提前5分钟
					clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -5);
				}else if("2".equals(aheadTime)){//15分钟
					clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -15);
				}else if("3".equals(aheadTime)){//30分钟
					clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.MINUTE, -30);
				}else if("4".equals(aheadTime)){//1小时
					clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.HOUR_OF_DAY, -1);
				}else if("5".equals(aheadTime)){//1天
					clockDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.DAY_OF_YEAR, -1);
				}
				//会议开始时间
				Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
				//消息提醒内容
				String content = "安排会议《"+regMeeting.getTitle()+"》,开始于"+DateTimeUtil.formatDate(startDate, DateTimeUtil.c_yyyy_MM_dd_)
						+","+DateTimeUtil.formatDate(startDate, DateTimeUtil.HH_mm_ss).substring(0,5);
				
				clockService.addMeetingClock(regMeeting,userInfo,clockDateStr,content,"01");
				
				//需要申请会议室
				if(regMeeting.getRoomType().equals("0")  && regMeeting.getMeetingAddrId()>0){
					
					//会议室申请
					RoomApply roomApply = new RoomApply();
					//企业号
					roomApply.setComId(userInfo.getComId());
					//会议室主键
					roomApply.setRoomId(regMeeting.getMeetingAddrId());
					//会议主键
					roomApply.setMeetingId(meetingId);
					//会议开始时间
					roomApply.setStartDate(regMeeting.getStartDate());
					//会议结束时间
					roomApply.setEndDate(regMeeting.getEndDate());
					//会议室申请人员
					roomApply.setUserId(userInfo.getId());
					
					MeetingRoom meetRoom = meetingRoomService.getMeetingRoomById(meeting.getMeetingAddrId(), userInfo.getComId());
					if(meetRoom.getMamager().equals(userInfo.getId())){//会议发起人是会议室管理员，则不需要审核
						//申请状态
						roomApply.setState("1");
						
						meetingDao.add(roomApply);
					}else{
						//申请状态
						roomApply.setState("0");
						
						meetingDao.add(roomApply);
						//申请会议室
						todayWorksService.addTodayWorks(userInfo, meetRoom.getMamager(), meetingId,
								"会议《"+meeting.getTitle()+"》申请会议室《"+meetRoom.getRoomName()+"》", ConstantInterface.TYPE_MEETROOM, null, null);
					}
				}
				
				try {
					this.updateMeetIndex(meetingId, userInfo, "add");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	/**
	 * 取得会议对象
	 * @param meetingId
	 * @return
	 */
	public Meeting getMeetingObj(Integer meetingId) {
		
		return (Meeting) meetingDao.objectQuery(Meeting.class, meetingId);
	}
	/**
	 * 通过申请取得会议信息
	 * @param comId 企业号 
	 * @param appId 申请的主键
	 * @param meetingId 
	 * @return
	 */
	public Meeting getMeetByAppId(Integer comId, Integer appId, Integer meetingId) {
		
		return meetingDao.getMeetByAppId(comId,appId,meetingId);
	}
	/**
	 * 判断用书是否有权限
	 * @param comId 企业号
	 * @param userId 用户主键
	 * @param meetingId 会议主键
	 * @return
	 */
	public Integer authCheckMeetUser(Integer comId, Integer userId,
			Integer meetingId) {
		Integer count = meetingDao.authCheckMeetUser(comId,userId,meetingId);
		return count;
	}
	/**
	 * 查询参会人员确认状态
	 * @param meetingId 会议主键
	 * @param comId 企业号
	 * @param userId 
	 * @return
	 */
	public List<MeetCheckUser> listMeetCheckUser(Integer meetingId,
			Integer comId, Integer userId) {
		List<MeetCheckUser> list = meetingDao.listMeetCheckUser(meetingId,comId,userId);
		return list;
	}
	/**
	 * 查询会议用于会议室换管理员
	 * @param comId 企业号
	 * @param preManager 会议室以前的管理员
	 * @return
	 */
	public List<Meeting> listMeetForRoomChange(Integer comId, Integer preManager) {
		List<Meeting> list = meetingDao.listMeetForRoomChange(comId,preManager);
		return list;
	}
	/**
	 * 验证人员是否参会
	 * @param comId 企业号
	 * @param userId 用户主键
	 * @param busId 会议主键
	 * @return
	 */
	public MeetCheckUser checkMeetUser(Integer comId, Integer userId,
			Integer busId) {
		return meetingDao.checkMeetUser(comId,userId,busId);
	}
	/**
	 * 删除本次邀请的成员
	 * @param meetIngId 会议主键
	 * @param userInfo 操作员主键
	 * @param userId 参会人员主键
	 */
	public void delInvMeetUser(Integer meetIngId, UserInfo userInfo,
			Integer userId) {
		//删除单个与会人员
		meetingDao.delByField("meetPerson", new String[]{"comId","meetingId","userId"}, 
				new Object[]{userInfo.getComId(),meetIngId,userId});
		//删除该人员的待办提醒
		todayWorksService.delTodoWork(meetIngId, userInfo.getComId(), userId, ConstantInterface.TYPE_MEETING, "1");
		
//		try {
//			this.updateMeetIndex(meetIngId, userInfo, "update");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
	}
	
	/**
	 * 更新会议索引
	 * @param meetingId 会议主键
	 * @param userInfo 当前操作人员
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception
	 */
	public void updateMeetIndex(Integer meetingId,UserInfo userInfo,String opType) throws Exception{
		
		Integer comId = userInfo.getComId();
		Meeting meetingT = meetingDao.getMeetingById(meetingId, comId);
		if(null== meetingT){return;}
		
		Meeting meeting = new Meeting();
		
		meeting.setRecordCreateTime(meetingT.getRecordCreateTime());
		meeting.setTitle(meetingT.getTitle());
		meeting.setOrganiserName(meetingT.getOrganiserName());
		meeting.setRecorderName(meetingT.getRecorderName());
		meeting.setMeetingAddrName(meetingT.getMeetingAddrName());
		meeting.setContent(meetingT.getContent());
		meeting.setSummary(meetingT.getSummary());
//		StringBuffer attStr = new StringBuffer(CommonUtil.objAttr2String(meeting,null));
		StringBuffer attStr = new StringBuffer(meetingT.getTitle());
		
		
		
		//单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String index_key = userInfo.getComId()+"_"+ConstantInterface.TYPE_MEETING+"_"+meetingId;
		//为任务创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				index_key,userInfo.getComId(),meetingId,ConstantInterface.TYPE_MEETING,
				meeting.getTitle(),attStr.toString(),DateTimeUtil.parseDate(meeting.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,index_key));
		}
		
		
	}
	/**
	 * 获取团队所有的会议主键集合
	 * @param userInfo
	 * @return
	 */
	public List<Meeting> listMeetingOfAll(UserInfo userInfo){
		return meetingDao.listMeetingOfAll(userInfo);
	}
	/**
	 * 会议查看权限验证
	 * @param comId 团队主键
	 * @param meetingId 会议主键
	 * @param userId 验证人员主键
	 * @return
	 */
	public boolean authorCheck(Integer comId,Integer meetingId,Integer userId){
		List<Meeting> listMeeting = meetingDao.authorCheck(comId,meetingId,userId);
		if(null!=listMeeting && !listMeeting.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 查询与该会议同时段的参与的其他会议集合
	 * @param meetIngId 会议主键
	 * @param userInfo 当前操作员
	 * @return
	 */
	public List<Meeting> checkMeeting(Integer meetingId, UserInfo userInfo) {
		Meeting meeting = (Meeting) meetingDao.objectQuery(Meeting.class, meetingId);
		
		return meetingDao.checkMeeting(meeting,userInfo);
	}
	
	/**
	 * 获取会议详情及会议学习人信息
	 * @param id
	 * @param comId
	 * @return
	 */
	public Meeting getMeeting(Integer id, Integer comId) {
		Meeting meeting = meetingDao.getMeetingById(id,comId);
		List<MeetLearn> listMeetLearn = meetingDao.listMeetLearn(id, comId);
		meeting.setListMeetLearn(listMeetLearn);
		//生成客户参与人JSon字符串
		StringBuffer sharerJson = null;
		if(null!=listMeetLearn && !listMeetLearn.isEmpty()){
			sharerJson = new StringBuffer("[");
			
			for(MeetLearn vo:listMeetLearn){
				sharerJson.append("{'userID':'"+vo.getUserId()+"','userName':'"+vo.getSharerName()+"','gender':'"+vo.getGender()+"','uuid':'"+vo.getUuid()+"','fileName':'"+vo.getFileName()+"'},");	
			}
			
			sharerJson = new StringBuffer(sharerJson.substring(0,sharerJson.lastIndexOf(",")));
			sharerJson.append("]");
			
			meeting.setSharerJson(sharerJson.toString());
		}
		return meeting;
	}
	
	public boolean meetLearnUpdate(Integer comId, Integer meetingId, Integer[] userIds, UserInfo userInfo) {
		boolean succ = true;
		try {
			//先删除客户参与人
			meetingDao.delByField("meetLearn", new String[]{"comId","meetingId"},new Integer[]{comId,meetingId});
			List<UserInfo> shares = new ArrayList<>();
			if(null!=userIds && userIds.length>0){
				StringBuffer sharerName = new StringBuffer();
				for(Integer userId:userIds){
					UserInfo sharerInfo = userInfoService.getUserInfo(userInfo.getComId(),userId);
					sharerName.append(sharerInfo.getUserName()+",");
					shares.add(sharerInfo);
				}
				sharerName = new StringBuffer(sharerName.subSequence(0,sharerName.lastIndexOf(",")));
				//会议学习人更新
				MeetLearn meetLearn =null;
				for(Integer userId:userIds){
					meetLearn = new MeetLearn();
					meetLearn.setComId(comId);
					meetLearn.setUserId(userId);
					meetLearn.setMeetingId(meetingId);
					meetingDao.add(meetLearn);
				}
				//添加会议日志
				this.addMeetLog(userInfo.getComId(), meetingId, userInfo.getId(), userInfo.getUserName(), "变更会议学习人员");
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo,userInfo.getId(), meetingId, "变更会议学习人员为:\""+sharerName.toString()+"\"", ConstantInterface.TYPE_MEET_SUMMARY, shares,null);
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 删除会议学习人员
	 * @param comId
	 * @param meetingId
	 * @param userId
	 * @param userInfo
	 * @param userName
	 * @return
	 */
	public boolean delMeetLearn(Integer comId, Integer meetingId, Integer userId, UserInfo userInfo, String userName) {
		boolean succ = true;
		try {
			//删除会议学习人员
			meetingDao.delByField("meetLearn", new String[]{"comId","meetingId","userId"},new Integer[]{comId,meetingId,userId});
			//添加会议日志
			this.addMeetLog(userInfo.getComId(), meetingId, userInfo.getId(), userInfo.getUserName(), "删除会议学习人员：" + userName);
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 删除会议附件、留言附件
	 * @param meetUpFileId
	 * @param type
	 * @param userInfo
	 * @param meetingId
	 */
	public void delMeetUpfile(Integer meetUpFileId, String type, UserInfo userInfo, Integer meetingId) {
		if(type.equals("0")){
			SummaryFile file = (SummaryFile) meetingDao.objectQuery(SummaryFile.class, meetUpFileId);
			meetingDao.delById(SummaryFile.class, meetUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) meetingDao.objectQuery(Upfiles.class, file.getUpfileId());
			this.addMeetLog(userInfo.getComId(),meetingId,userInfo.getId(),userInfo.getUserName(),"删除了会议附件："+upfiles.getFilename());
		}else{
			MeetTalkFile file = (MeetTalkFile) meetingDao.objectQuery(MeetTalkFile.class, meetUpFileId);
			meetingDao.delById(MeetTalkFile.class, meetUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) meetingDao.objectQuery(Upfiles.class, file.getUpfileId());
			this.addMeetLog(userInfo.getComId(),meetingId,userInfo.getId(),userInfo.getUserName(),"删除了会议留言附件："+upfiles.getFilename());
		}
	}
}
