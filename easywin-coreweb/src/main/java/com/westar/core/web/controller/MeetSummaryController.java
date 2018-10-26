package com.westar.core.web.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.enums.ModSpStateEnum;
import com.westar.base.model.MeetCheckUser;
import com.westar.base.model.MeetSummary;
import com.westar.base.model.Meeting;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.pojo.ModSpConf;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.MeetSummaryService;
import com.westar.core.service.MeetingService;
import com.westar.core.service.ModFlowService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.web.FreshManager;

@Controller
@RequestMapping("/meetSummary")
public class MeetSummaryController extends BaseController{

	@Autowired
	MeetSummaryService meetSummaryService;
	
	@Autowired
	MeetingService meetingService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ModFlowService modFlowService;
	
	@Autowired
	UserInfoService userInfoService;
	
	/**
	 * 添加会议纪要页面跳转
	 * @param meetingId 会议主键
	 * @param redirectPage 添加数据后页面跳转
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/addSummary")
	public ModelAndView addSummary(MeetSummary meetSummary,String redirectPage) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		meetSummaryService.addSummary(meetSummary,userInfo);
		this.setNotification(Notification.SUCCESS, "成功上传会议纪要");
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	/**
	 * 修改并审批会议纪要信息
	 * @param meetSummary 会议纪要信息
	 * @param redirectPage 跳转页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/updateSpSummary")
	public ModelAndView updateSummary(MeetSummary meetSummary,String redirectPage) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		meetSummaryService.updateSummary(meetSummary,userInfo);
		this.setNotification(Notification.SUCCESS, "成功上传会议纪要");
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	/**
	 * 添加会议纪要页面跳转
	 * @param meetingId 会议主键
	 * @param redirectPage 添加数据后页面跳转
	 * @return
	 */
	@RequestMapping("/addSummaryPage")
	public ModelAndView addSummaryPage(Integer meetingId,HttpServletRequest request){
		ModelAndView view = new ModelAndView("/meetSummary/addSummary");
		
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		
		Meeting meeting = meetingService.getMeetForSummary(meetingId,userInfo);
		if(null==meeting){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，会议已被删除");
			return view;
		}
		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING, meetingId);
		//取得是否添加浏览记录
		boolean bool = FreshManager.checkOpt(request, viewRecord);
		if(bool){
			//添加查看记录
			viewRecordService.addViewRecord(userInfo,viewRecord);
		}
		//查询会议纪要信息
		MeetSummary meetSummary = meetSummaryService.queryMeetSummary(userInfo,meetingId);
		meeting.setMeetSummary(meetSummary);
		
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(meetSummary.getId(),userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEET_SUMMARY,0);
		todayWorksService.updateTodoWorkRead(meetSummary.getId(),userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING,0);
		
		if(meetSummary.getSpState().equals(1)){
			ModSpConf modSpConf = modFlowService.queryModSpConf(userInfo,meetSummary.getId(),
					ConstantInterface.TYPE_MEET_SUMMARY,meetSummary.getActInstaceId());
			
			if(modSpConf.getExecutor() == -1 
					||  modSpConf.getExecutor().equals(userInfo.getId())){//流程办理人员
				meetSummary.setModSpConf(modSpConf);
			}
			if(modSpConf.getExecutor().equals(userInfo.getId()) //执行人是自己
					&& userInfo.getId().equals(meeting.getRecorder())//记录员人是自己
					&& "start".equalsIgnoreCase(modSpConf.getStepType())){//是开始步骤
				view.setViewName("/meetSummary/updateSpSummary");
			}else{
				view.setViewName("/meetSummary/viewSpSummary");
			}
		}
		view.addObject("meetSummary", meetSummary);
		
		view.addObject("meeting", meeting);
		
		//查询参会人员确认状态
		List<MeetCheckUser> listMeetCheckUser = meetingService.listMeetCheckUser(meetingId, userInfo.getComId(),userInfo.getId());
		view.addObject("listMeetCheckUser", listMeetCheckUser);
		
		
		return view;
	}
	/**
	 * 重置添加会议纪要
	 * @param meetingId 会议主键
	 * @param meetSummaryId 会议纪要主键
	 * @param request
	 * @return
	 */
	@RequestMapping("/reUploadSummary")
	public ModelAndView reUploadSummary(Integer meetingId,Integer meetSummaryId,
			HttpServletRequest request,String sid){
		UserInfo userInfo = this.getSessionUser();
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(meetSummaryId,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEET_SUMMARY,0);
		
		return new ModelAndView("redirect:/meetSummary/addSummaryPage?sid="+sid+"&meetingId="+meetingId);
	}
	
	/**
	 * 审批会议纪要信息
	 * @param summaryId 会议纪要主键
	 * @param sid session唯一标识
	 * @return
	 */
	@RequestMapping("/viewSummaryById")
	public ModelAndView viewSummarybyId(Integer summaryId,String sid){
		UserInfo userInfo = this.getSessionUser();
		MeetSummary meetSummary = meetSummaryService.querySummaryForMeet(userInfo, summaryId);
		return new ModelAndView("redirect:/meetSummary/viewSummaryPage?sid="+sid+"&meetingId="+meetSummary.getMeetingId());
	}
	
	/**
	 * 查看会议纪要页面跳转
	 * @param meetingId 会议主键
	 * @return
	 */
	@RequestMapping("/viewSummaryPage")
	public ModelAndView viewSummaryPage(Integer meetingId,HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		UserInfo userInfo = this.getSessionUser();
		Meeting meeting = meetingService.getMeetForSummary(meetingId,userInfo);
		view.addObject("meeting", meeting);
		view.addObject("userInfo", userInfo);
		if(null!=meeting){
			
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING, meetingId);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				viewRecordService.addViewRecord(userInfo,viewRecord);
			}
			
			//查询会议纪要信息
			MeetSummary meetSummary = meetSummaryService.queryMeetSummary(userInfo,meetingId);
			//删除消息提醒
			todayWorksService.updateTodoWorkRead(meetSummary.getId(),userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEET_SUMMARY,0);
			todayWorksService.updateTodoWorkRead(meetSummary.getId(),userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETING,0);
			
			meeting.setMeetSummary(meetSummary);
			
			if(meetSummary.getSpState().equals(1)){
				ModSpConf modSpConf = modFlowService.queryModSpConf(userInfo,meetSummary.getId(),
						ConstantInterface.TYPE_MEET_SUMMARY,meetSummary.getActInstaceId());
				
				if(modSpConf.getExecutor()==-1 ||  modSpConf.getExecutor().equals(userInfo.getId())){//流程办理人员
					meetSummary.setModSpConf(modSpConf);
				}
				if(modSpConf.getExecutor().equals(userInfo.getId())//执行人是自己
						&& userInfo.getId().equals(meeting.getRecorder())//记录员是自己
						&& "start".equalsIgnoreCase(modSpConf.getStepType())){//是开始步骤
					view.setViewName("/meetSummary/updateSpSummary");
				}else{
					view.setViewName("/meetSummary/viewSpSummary");
				}
				view.addObject("meetSummary", meetSummary);
			}else if(!ModSpStateEnum.NONE.getValue().equals(meetSummary.getSpState())){
				view.setViewName("/meetSummary/viewSpSummary");
				view.addObject("meetSummary", meetSummary);
			}else{
				
				if(meeting.getRecorder().equals(userInfo.getId())){//会议记录员
					view.setViewName("/meetSummary/addSummary");
				}else{
					view.setViewName("/meetSummary/viewSummary");
				}
				view.addObject("meetSummary", meetSummary);
			}
			//查询参会人员确认状态
			List<MeetCheckUser> listMeetCheckUser = meetingService.listMeetCheckUser(meetingId, userInfo.getComId(),userInfo.getId());
			view.addObject("listMeetCheckUser", listMeetCheckUser);
		}else{
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，会议已被删除");
			return view;
		}
		return view;
	}
	
	/**
	 * 进入会议学习主页
	 * @param meetId
	 * @return
	 */
	@RequestMapping(value="/meetLearnPage")
	public ModelAndView meetLearnPage(Meeting meeting){
		ModelAndView view = new ModelAndView("/meetSummary/meetLearn");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		meeting = meetingService.getMeeting(meeting.getId(), userInfo.getComId());
		view.addObject("listCustomerSharer",meeting.getListMeetLearn());
		view.addObject("meeting",meeting);
		view.addObject("meetingId",meeting.getId());
		return view;
	}
	
	/**
	 * 客户参与人更新
	 * @param meetingId
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/meetLearnUpdate")
	public String meetLearnUpdate(Integer meetingId, Integer[] userIds)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		boolean succ = meetingService.meetLearnUpdate(userInfo.getComId(),
				meetingId, userIds, userInfo);
		if (succ) {
			return "变更成功";
		} else {
			return "变更失败";
		}
	}
	
	/**
	 * 删除会议学习人员
	 * @param meetingId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delMeetLearn")
	public String delMeetLearn(Integer meetingId, Integer userId)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		UserInfo sharerInfo = userInfoService.getUserInfo(userInfo.getComId(),
				userId);
		boolean succ = meetingService.delMeetLearn(userInfo.getComId(),
				meetingId, userId, userInfo, sharerInfo.getUserName());
		if (succ) {
			return "删除成功";
		} else {
			return "删除失败";
		}
	}
	
}