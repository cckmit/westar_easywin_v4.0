package com.westar.core.web.controller;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.MeetCheckUser;
import com.westar.base.model.MeetLog;
import com.westar.base.model.MeetTalk;
import com.westar.base.model.Meeting;
import com.westar.base.model.MeetingRoom;
import com.westar.base.model.SummaryFile;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.ModSpConf;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.MeetingRoomService;
import com.westar.core.service.MeetingService;
import com.westar.core.service.ModFlowService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.web.FreshManager;

@Controller
@RequestMapping("/meeting")
public class MeetingController extends BaseController{

	@Autowired
	MeetingService meetingService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	MeetingRoomService meetingRoomService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	ModFlowService modFlowService;
	
	/**
	 * 跳转会议列表
	 * @return
	 */
	@RequestMapping("/listPagedMeeting")
	
	public ModelAndView listPagedMeeting(Meeting meeting,HttpServletRequest request){
		UserInfo userInfo = this.getSessionUser();
		List<Meeting> listMeetings = meetingService.listPagedMeeting(userInfo,meeting);
		ModelAndView view = new ModelAndView("/meeting/meetingCenter");
		view.addObject("userInfo", userInfo);
		view.addObject("listMeetings", listMeetings);
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		//是否为会议室管理人员（是否有权审核）
		Integer countManageRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
		view.addObject("countManageRoom", countManageRoom);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_MEETING);
		return view;
		
	}
	/**
	 * 跳转待开会议会议列表
	 * @return
	 */
	@RequestMapping("/listPagedTodoMeeting")
	public ModelAndView listPagedTodoMeeting(Meeting meeting,HttpServletRequest request){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/meeting/meetingCenter");
		view.addObject("userInfo", userInfo);
		//会议已发布
		meeting.setMeetingState(1);
		meeting.setTimeOut("2");
		//待开会议
		List<Meeting> listMeetings = meetingService.listPagedTodoMeeting(userInfo,meeting);
		view.addObject("listMeetings", listMeetings);
		
		//取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		view.addObject("listOwners",listOwners);
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		//是否为会议室管理人员（是否有权审核）
		Integer countManageRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
		view.addObject("countManageRoom", countManageRoom);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_MEETING);
		return view;
		
	}
	/**
	 * 跳转待开会议会议列表
	 * @return
	 */
	@RequestMapping("/listPagedSpFlowMeeting")
	public ModelAndView listPagedSpFlowMeeting(Meeting meeting,HttpServletRequest request){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/meeting/meetingCenter");
		view.addObject("userInfo", userInfo);
		//会议已发布
		meeting.setMeetingState(1);
		meeting.setTimeOut("2");
		//待开会议
		List<Meeting> listMeetings = meetingService.listPagedSpFlowMeeting(userInfo,meeting);
		view.addObject("listMeetings", listMeetings);
		
		//取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		view.addObject("listOwners",listOwners);
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		//是否为会议室管理人员（是否有权审核）
		Integer countManageRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
		view.addObject("countManageRoom", countManageRoom);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_MEETING);
		return view;
		
	}
	/**
	 * 跳转已开会议列表
	 * @return
	 */
	@RequestMapping("/listPagedDoneMeeting")
	public ModelAndView listPagedDoneMeeting(Meeting meeting,HttpServletRequest request){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/meeting/meetingCenter");
		view.addObject("userInfo", userInfo);
		//会议已发布
		meeting.setMeetingState(1);
		meeting.setTimeOut("0");
		//已开会议
		List<Meeting> listMeetings = meetingService.listPagedDoneMeeting(userInfo,meeting);
		view.addObject("listMeetings", listMeetings);
		
		//取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		view.addObject("listOwners",listOwners);
		
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		//是否为会议室管理人员（是否有权审核）
		Integer countManageRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
		view.addObject("countManageRoom", countManageRoom);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_MEETING);
		return view;
		
	}
	/**
	 * 跳转已开会议列表
	 * @return
	 */
	@RequestMapping("/listPagedMeetWithSpSummary")
	public ModelAndView listPagedMeetWithSpSummary(Meeting meeting,HttpServletRequest request){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/meeting/meetingCenter");
		view.addObject("userInfo", userInfo);
		//会议已发布
		meeting.setMeetingState(1);
		meeting.setTimeOut("0");
		//已开会议
		List<Meeting> listMeetings = meetingService.listPagedMeetWithSpSummary(userInfo,meeting);
		view.addObject("listMeetings", listMeetings);
		
		//取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		view.addObject("listOwners",listOwners);
		
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		//是否为会议室管理人员（是否有权审核）
		Integer countManageRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
		view.addObject("countManageRoom", countManageRoom);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_MEETING);
		return view;
		
	}
	
	/**
	 * 添加会议界面
	 * @param spFlag 是否为审批 1是 0不是
	 * @return
	 */
	@RequestMapping("/addMeetingPage")
	public ModelAndView addMeetingPage(String spFlag,Meeting meeting){
		ModelAndView mav = new ModelAndView("/meeting/addMeeting");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		
		if(null!=spFlag && spFlag.equals("1")){
			mav.setViewName("/meeting/addSpMeeting");
		}
		//当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		mav.addObject("nowDate", nowDate);
		
		//当前时间所在的周数
		Integer dayWeek = DateTimeUtil.getDay();
		mav.addObject("dayWeek", dayWeek);
		
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		//分钟数
		Integer minu = c.get(Calendar.MINUTE);
		//默认会议时间提前至少半个小时
		if(minu<=30){
			c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)+1);
			c.set(Calendar.MINUTE, 0);
		}else{
			c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)+1);
			c.set(Calendar.MINUTE, 30);
		}
		Date startDate = DateTimeUtil.calendar2Date(c);
		String startDateStr = DateTimeUtil.formatDate(startDate, DateTimeUtil.yyyy_MM_dd_HH_mm);
		String endDateStr = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm, Calendar.HOUR_OF_DAY, 1);
		
		//初始化数值
		if(StringUtils.isEmpty(meeting.getStartDate())){
			meeting.setStartDate(startDateStr);
		}
		if(StringUtils.isEmpty(meeting.getEndDate())){
			meeting.setEndDate(endDateStr);
		}
		return mav;
	}
	/**
	 * 添加会议
	 * @param meeting 会议属性
	 * @param redirectPage
	 * @param way 是否要跳转
	 * @param sid
	 * @return
	 */
	@RequestMapping("/addMeeting")
	public ModelAndView addMeeting(Meeting meeting,String redirectPage,
			String way,String sid){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/refreshParent");
		this.setNotification(Notification.SUCCESS, "添加成功！");
		//添加会议
		try {
			meetingService.addMeeting(meeting,userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return view;
	}
	/**
	 * 编辑会议界面跳转
	 * @param id 会议主键
	 * @param request
	 * @param batchUpdate 是否批量修改，针对周期会议 0不是 1是
	 * @return
	 */
	@RequestMapping("/updateMeetingPage")
	public ModelAndView updateMeetingPage(Integer id,HttpServletRequest request,
			String batchUpdate,String redirectPage,String sid){
		
		UserInfo userInfo = this.getSessionUser();
		
		Meeting timeMeet = meetingService.getMeetTimeOut( id,userInfo.getComId());
		if(null==timeMeet){
			return new ModelAndView("/refreshParent");
		}
		Integer timeOut = Integer.parseInt(timeMeet.getTimeOut());
		ModelAndView mav = new ModelAndView();
		mav.addObject("userInfo", userInfo);
		Integer spState = timeMeet.getSpState();
		if(timeMeet.getMeetingState()==1 && (spState.equals(4) || spState.equals(-1)) || spState.equals(0)){//会议已发布
			if(timeOut==1){//会议已开始
				mav.setViewName("/meeting/invMeetUser");
				Meeting meeting = meetingService.getMeetForInvById(id, userInfo.getComId());
				mav.addObject("meeting", meeting);
				return mav;
			}else if(timeOut==0 ){//会议结束
				Meeting meeting = meetingService.getMeetForInvById(id, userInfo.getComId());
				if(meeting.getRecorder().equals(userInfo.getId())){//会议结束后，记录人员传纪要
					return new ModelAndView("redirect:/meetSummary/addSummaryPage?sid="+sid+"&meetingId="+id);
				}else{//非会议记录人员查看会议纪要
					return new ModelAndView("redirect:/meetSummary/viewSummaryPage?sid="+sid+"&meetingId="+id);
				}
			}
		}
		mav.setViewName("/meeting/updateMeeting");
		//当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		mav.addObject("nowDate", nowDate);
		Meeting meeting = meetingService.getMeetingById(id, userInfo.getComId(),batchUpdate);
		
		if(timeMeet.getSpState().equals(1)){
			ModSpConf modSpConf = modFlowService.queryModSpConf(userInfo,id,ConstantInterface.TYPE_MEETING,meeting.getActInstaceId());
			if(modSpConf.getExecutor()==-1 ||  modSpConf.getExecutor().equals(userInfo.getId())){//流程办理人员
				meeting.setModSpConf(modSpConf);
			}
			if(modSpConf.getExecutor().equals(userInfo.getId())//执行人是自己
					&& userInfo.getId().equals(meeting.getOrganiser())//发起人是自己
					&& "start".equalsIgnoreCase(modSpConf.getStepType())){//是开始步骤
				mav.setViewName("/meeting/updateSpMeeting");
			}else{
				mav.setViewName("/meeting/viewSpMeeting");
			}
		}
		
		mav.addObject("meeting", meeting);
	
		if(meeting.getMeetingState()==1 //会议已发布
				&& !meeting.getMeetingType().equals("0")//是周期会议 
				&& null!=batchUpdate && batchUpdate.equals("0")){//需要查看周期属性,需要添加会议
			mav.addObject("method", "add");
		}else{
			mav.addObject("method", "update");
		}
		
		//查询参会人员确认状态
		List<MeetCheckUser> listMeetCheckUser = meetingService.listMeetCheckUser(id, userInfo.getComId(),userInfo.getId());
		mav.addObject("listMeetCheckUser", listMeetCheckUser);
		
		//当前时间所在的周数
		Integer dayWeek = DateTimeUtil.getDay();
		mav.addObject("dayWeek", dayWeek);
		if(meeting.getOrganiser().equals(userInfo.getId())){
			meetingService.addMeetCheckUser(userInfo, id, 1, "");
		}
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(id,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING,0);
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(id,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETROOM,0);
					
		return mav;
	}
	/**
	 * 修改会议
	 * @param meeting 会议属性
	 * @param redirectPage
	 * @param method 是否要添加会议
	 * @param perMeetId 若是需要添加会议，则需要充周期会议分离出来
	 * @return
	 */
	@RequestMapping("/updateMeeting")
	public ModelAndView upateMeeting(Meeting meeting,String method,Integer perMeetId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/refreshParent");
		this.setNotification(Notification.SUCCESS, "操作成功！");
		//修改会议
		try {
			meetingService.updateMeeting(meeting,userInfo,method,perMeetId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return view;
	}
	
	/**
	 * 撤销会议
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/revokeMeeting")
	public ModelAndView revokeMeeting(Integer[] ids,String redirectPage){
		UserInfo userInfo = this.getSessionUser();
		try {
			//撤销会议
			meetingService.delRevokeMeeting(userInfo,ids);
			this.setNotification(Notification.SUCCESS, "撤销会议成功");
		} catch (Exception e) {
			this.setNotification(Notification.ERROR, "撤销会议失败");
		}
		return new ModelAndView("redirect:"+redirectPage);
	}
	/**
	 * 删除会议
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/deleteMeeting")
	public ModelAndView deleteMeeting(Integer[] ids,String redirectPage){
		UserInfo userInfo = this.getSessionUser();
		try {
			//删除会议
			meetingService.delMeeting(userInfo,ids);
			this.setNotification(Notification.SUCCESS, "会议删除成功");
		} catch (Exception e) {
			this.setNotification(Notification.ERROR, "删除失败");
		}
		return new ModelAndView("redirect:"+redirectPage);
	}
	/**
	 * 查看会议
	 * @param meetingId 会议主键
	 * @param request
	 * @return
	 */
	@RequestMapping("/viewMeetingPage")
	public ModelAndView viewMeeting(Integer  meetingId,HttpServletRequest request){
		ModelAndView view = new ModelAndView("/meeting/viewMeeting");
		
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		//不需要周期数据
		Meeting meeting = meetingService.getMeetingById(meetingId, userInfo.getComId(),null);
		if(null==meeting){
			this.setNotification(Notification.ERROR, "会议已被删除！");
			return new ModelAndView("/refreshParent");
		}
		if(meeting.getSpState().equals(1)){
			ModSpConf modSpConf = modFlowService.queryModSpConf(userInfo,meetingId,ConstantInterface.TYPE_MEETING_SP,meeting.getActInstaceId());
			if(modSpConf.getExecutor()==-1 ||  modSpConf.getExecutor().equals(userInfo.getId())){//流程办理人员
				meeting.setModSpConf(modSpConf);
			}
			if(modSpConf.getExecutor().equals(userInfo.getId())//执行人是自己
					&& userInfo.getId().equals(meeting.getOrganiser())//发起人是自己
					&& "start".equalsIgnoreCase(modSpConf.getStepType())){//是开始步骤
				view.setViewName("/meeting/updateSpMeeting");
				view.addObject("method", "update");
			}else{
				view.setViewName("/meeting/viewSpMeeting");
			}
		}
		view.addObject("meeting", meeting);
		//查询参会人员确认状态
		List<MeetCheckUser> listMeetCheckUser = meetingService.listMeetCheckUser(meetingId, userInfo.getComId(),userInfo.getId());
		view.addObject("listMeetCheckUser", listMeetCheckUser);
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(meetingId,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING,0);
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(meetingId,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETROOM,0);
		
		return view;
	}
	/**
	 * 查看会议
	 * @param meetingId 会议主键
	 * @param request
	 * @return
	 */
	@RequestMapping("/viewSpMeetingPage")
	public ModelAndView viewSpMeeting(Integer  meetingId,HttpServletRequest request){
		ModelAndView view = new ModelAndView("/meeting/viewSpMeeting");
		
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		//不需要周期数据
		Meeting meeting = meetingService.getMeetingById(meetingId, userInfo.getComId(),null);
		if(null==meeting){
			this.setNotification(Notification.ERROR, "会议已被删除！");
			return new ModelAndView("/refreshParent");
		}
		if(meeting.getSpState().equals(1)){
			ModSpConf modSpConf = modFlowService.queryModSpConf(userInfo,meetingId,ConstantInterface.TYPE_MEETING_SP,meeting.getActInstaceId());
			if(modSpConf.getExecutor()==-1 ||  modSpConf.getExecutor().equals(userInfo.getId())){//流程办理人员
				meeting.setModSpConf(modSpConf);
			}
		}
		view.addObject("meeting", meeting);
		//查询参会人员确认状态
		List<MeetCheckUser> listMeetCheckUser = meetingService.listMeetCheckUser(meetingId, userInfo.getComId(),userInfo.getId());
		view.addObject("listMeetCheckUser", listMeetCheckUser);
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(meetingId,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING,0);
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(meetingId,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETROOM,0);
		
		return view;
	}
	
	
	/**
	 * 邀请参会人员(会议已开始，但是未结束)
	 * @param meetingId
	 * @param request
	 * @return
	 */
	@RequestMapping("/invMeetUserPage")
	public ModelAndView invMeetUserPage(Integer  meetingId,HttpServletRequest request,
			String redirectPage,String sid){
		
		UserInfo userInfo = this.getSessionUser();
		
		Meeting timeMeet = meetingService.getMeetTimeOut( meetingId,userInfo.getComId());
		if(null==timeMeet){
			return new ModelAndView("/refreshParent");
		}
		Integer timeOut = Integer.parseInt(timeMeet.getTimeOut());
		
		Meeting meeting = meetingService.getMeetForInvById(meetingId, userInfo.getComId());
		if(timeOut==0){//会议已结束
			if(meeting.getRecorder().equals(userInfo.getId())){//会议结束后，记录人员传纪要
				return new ModelAndView("redirect:/meetSummary/addSummaryPage?sid="+sid+"&meetingId="+meetingId);
			}else{//非会议记录人员查看会议纪要
				return new ModelAndView("redirect:/meetSummary/viewSummaryPage?sid="+sid+"&meetingId="+meetingId);
			}
		}
		ModelAndView view = new ModelAndView("/meeting/invMeetUser");
		
		view.addObject("userInfo", userInfo);
		
		view.addObject("meeting", meeting);
		
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(meetingId,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING,0);
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(meetingId,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETROOM,0);
		
		//查询参会人员确认状态
		List<MeetCheckUser> listMeetCheckUser = meetingService.listMeetCheckUser(meetingId, userInfo.getComId(),userInfo.getId());
		view.addObject("listMeetCheckUser", listMeetCheckUser);
				
		//添加会议确认
		if(meeting.getOrganiser().equals(userInfo.getId())){
			meetingService.addMeetCheckUser(userInfo, meetingId, 1, "");
		}
		return view;
	}
	/**
	 * 邀请人员加入会议
	 * @param meeting
	 * @param redirectPage
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/invMeetUser")
	public Map<String, Object> invMeetUser(Integer meetIngId,Integer[] userIds){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
		}else{
			Map<String, Object> users = meetingService.updateMeetUser(meetIngId,userInfo,userIds);
			map.put("status", "y");
			map.putAll(users);
		}
		return map;
	}
	/**
	 * 删除本次邀请的成员
	 * @param meetIngId 会议主键
	 * @param userInfo 操作员主键
	 * @param userId 参会人员主键
	 */
	@ResponseBody
	@RequestMapping("/delInvMeetUser")
	public Map<String, Object> delInvMeetUser(Integer meetIngId,Integer userId){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
		}else{
			meetingService.delInvMeetUser(meetIngId,userInfo,userId);
			map.put("status", "y");
		}
		return map;
	}
	/**
	 * 验证当前操作员该会议时段
	 * @param meetIngId 会议主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkMeeting")
	public Map<String, Object> checkMeeting(Integer meetingId){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
		}else{
			//查询同时段所有由重合的会议
			List<Meeting> meetings = meetingService.checkMeeting(meetingId,userInfo);
			if(null != meetings && meetings.size()>1){
				map.put("status", "f");
				map.put("meetings",meetings);
			}else{
				map.put("status", "y");
			}
		}
		return map;
	}
	
	/******************************以下是会议留言部分*****************************/
	/**
	 * 会议留言
	 * @param meetingId 会议主键
	 * @return
	 */
	@RequestMapping(value="/listPagedMeetTalk")
	public ModelAndView listPagedMeetTalk(Integer meetingId){
		ModelAndView view = new ModelAndView("/meeting/listMeetTalk");
		UserInfo userInfo = this.getSessionUser();
		
		view.addObject("userInfo", userInfo);
		List<MeetTalk> listMeetTalk = meetingService.listPagedMeetTalk(meetingId, userInfo.getComId());
		view.addObject("listMeetTalk",listMeetTalk);
		view.addObject("meetingId",meetingId);
		return view;
	}
	/**
	 * ajax添加会议留言
	 * @param meetTalk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddMeetTalk")
	public Map<String, Object> ajaxAddTaskTalk(MeetTalk meetTalk) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
		}else{
			//设置企业号
			meetTalk.setComId(userInfo.getComId());
			//设置留言人
			meetTalk.setTalker(userInfo.getId());
			//添加后的返回主键
			Integer id = meetingService.addMeetTalk(meetTalk,userInfo);
			//重新取得会议留言的信息
			meetTalk = meetingService.queryMeetTalk(id, userInfo.getComId());
			
			map.put("status", "y");
			map.put("userInfo", userInfo);
			map.put("meetTalk", meetTalk);
		}
		//模块日志添加
		return map;
	}
	/**
	 * 查看会议状态
	 * @param meetId 会议主键
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/checkMeetTimeOut")
	public Map<String, Object> checkMeetTimeOut(Integer meetId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "连接已断开，请重新登陆");
			return map;
		}
		map.put("status", "y");
		map.put("timeOut", -1);
		map.put("organiser", 0);
		map.put("recorder", 0);
		//判断会议状态0已结束 1 正在进行 2 未开始
		Meeting timeMeet = meetingService.getMeetTimeOut( meetId,userInfo.getComId());
		if(null!=timeMeet){
			map.put("organiser", timeMeet.getOrganiser());
			map.put("recorder", timeMeet.getRecorder());
			if(timeMeet.getMeetingState()==1){
				map.put("timeOut", Integer.parseInt(timeMeet.getTimeOut()));
			}
		}
		return map;
	}
	/**
	 * 删除讨论回复
	 * @param id 留言主键
	 * @param delChildNode 是否删除子节点
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delMeetTalk")
	public Map<String, Object> delMeetTalk(Integer id,String delChildNode) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆！");
		}else{
			//要删除的回复所有子节点和自己
			meetingService.delMeetTalk(id,delChildNode,userInfo);
			map.put("status", "y");
		}
		return map;
	}
	/**
	 * 会议附件列表
	 * @param meetingId
	 * @return
	 */
	@RequestMapping("/listPagedMeetUpfiles")
	public ModelAndView listMeetUpfiles(Integer meetingId){
		ModelAndView view = new ModelAndView("/meeting/listMeetUpfiles");
		UserInfo userInfo = this.getSessionUser();
		List<SummaryFile> listMeetUpfiles = meetingService.listPagedMeetUpfiles(meetingId, userInfo.getComId());
		view.addObject("listMeetUpfiles",listMeetUpfiles);
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 查看会议日志
	 * @param meetingId
	 * @return
	 */
	@RequestMapping("/listPagedMeetLogs")
	public ModelAndView listPagedMeetLogs(Integer meetingId){
		ModelAndView view = new ModelAndView("/meeting/listMeetLogs");
		UserInfo userInfo = this.getSessionUser();
		List<MeetLog> listMeetLogs = meetingService.listPagedMeetLogs(meetingId, userInfo.getComId());
		view.addObject("listMeetLogs",listMeetLogs);
		return view;
	}
	
	/**
	 * 查看会议是否有权限
	 * @param meetingId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/authCheckMeetUser")
	public Map<String, Object> authCheckMeetUser(Integer meetingId){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新连接");
		}else{
			Meeting meeting = meetingService.getMeetTimeOut(meetingId, userInfo.getComId());
			map.put("status", "f");
			if(null!=meeting){
				//判断用户是否有权限查看会议
				Integer countUsers = meetingService.authCheckMeetUser(userInfo.getComId(),userInfo.getId(),meetingId);
				if(countUsers.equals(0)){//没有查看权限
					todayWorksService.delTodoWork(meetingId,userInfo.getComId(),userInfo.getId(), ConstantInterface.TYPE_MEETING,null);
					map.put("info", "没有会议查看权限");
				}else{
					map.put("status", "y");
					map.put("meeting", meeting);
				}
			}else{
				todayWorksService.delTodoWork(meetingId,userInfo.getComId(),userInfo.getId(), ConstantInterface.TYPE_MEETING,null);
				map.put("info", "会议已删除");
			}
		}
		return map;
	}
	/**
	 * 验证是否为会议室管理人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/authCheckMeetRoom")
	public Map<String, Object> authCheckMeetRoom(Integer meetingId){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新连接");
		}else{
			Meeting meeting = meetingService.getMeetingObj(meetingId);
			map.put("status", "f");
			if(null!=meeting){
				if(meeting.getRoomType().equals("0") && meeting.getMeetingAddrId()>0){
					MeetingRoom meetingRoom = meetingRoomService.getMeetingRoomById(meeting.getMeetingAddrId(),userInfo.getComId());
					if(meetingRoom.getMamager().equals(userInfo.getId())){
						map.put("status", "y");
					}else{
						//将待办设置成会议室负责人
						meetingRoomService.updateMeetUserForTodo(userInfo.getComId(),userInfo.getId(),meetingRoom.getMamager(),meetingRoom.getId());
						map.put("info", "没有会议室管理权限");
					}
				}else{
					map.put("info", "会议已撤销申请会议室");
				}
			}else{
				map.put("info", "会议已删除");
			}
		}
		return map;
	}
	
	/*********************设置人员参会确认单*********************************/
	/**
	 *设置与会人员确认单
	 * @param state 确认状态
	 * @param reason 确认理由
	 * @param meetingId 会议主键
	 * @param redirectPage 确认后页面跳转
	 * @return
	 */
	@RequestMapping("/addMeetCheckUser")
	public ModelAndView addMeetCheckUser(Integer state,String reason,Integer meetingId,String redirectPage){
		
		ModelAndView mav = new ModelAndView();
		UserInfo userInfo = this.getSessionUser();
		//设置与会人员确认单
		meetingService.addMeetCheckUser(userInfo, meetingId, state, reason);
		mav.setViewName("redirect:"+redirectPage);
		return mav;
		
	}
	/**
	 * 异步设置与会人员确认单
	 * @param state
	 * @param reason
	 * @param meetingId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddMeetCheckUser")
	public Map<String,Object> ajaxAddMeetCheckUser(Integer state,String reason,Integer meetingId){
		
		 Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "连接已断开，请重新登陆");
		}else{
			//设置与会人员确认单
			meetingService.addMeetCheckUser(userInfo, meetingId, state, reason);
			map.put("status", "y");
		}
		return map;
		
	}
	/**
	 * 会议查看权限验证
	 * @param MeetingId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/authorCheck")
	public Meeting authorCheck(Integer meetingId){
		UserInfo userInfo = this.getSessionUser();
		Meeting vo = new Meeting();
		if(null==userInfo){
			vo.setSucc(false);
			vo.setPromptMsg("服务已关闭，请稍后重新操作！");
			return vo;
		}
		if(meetingService.authorCheck(userInfo.getComId(),meetingId,userInfo.getId())){
			vo.setSucc(true);
		}else{
			vo.setSucc(false);
			vo.setPromptMsg("抱歉，你没有查看权限");
		}
		return vo;
	}
	
	/**
	 * 删除会议附件、留言附件
	 * @param meetingId
	 * @param meetUpFileId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delMeetUpfile")
	public Map<String, Object> delMeetUpfile(Integer meetingId,Integer meetUpFileId,String type) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		meetingService.delMeetUpfile(meetUpFileId,type,userInfo,meetingId);
		map.put("status", "y");
		return map;
	}
	
	
}