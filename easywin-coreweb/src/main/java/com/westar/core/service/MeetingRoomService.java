package com.westar.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Meeting;
import com.westar.base.model.MeetingRoom;
import com.westar.base.model.RoomApply;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.MeetingRoomDao;

@Service
public class MeetingRoomService {

	@Autowired
	MeetingRoomDao meetingRoomDao;
	
	@Autowired
	MeetingService meetingService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	UserInfoService userInfoService;

	/**
	 * 分页查询会议室信息
	 * @param meetingRoom 会议室属性条件
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public List<MeetingRoom> listPagedMeetingRoom(MeetingRoom meetingRoom,
			UserInfo userInfo) {
		List<MeetingRoom>  meetingRooms = meetingRoomDao.listPagedMeetingRoom(meetingRoom,userInfo);
		return meetingRooms;
	}
	/**
	 * 添加会议室
	 * @param meetingRoom
	 * @param userInfo
	 */
	public void addMeetingRoom(MeetingRoom meetingRoom, Integer comId) {
		meetingRoom.setComId(comId);
		meetingRoomDao.add(meetingRoom);
	}
	/**
	 * 取得会议室的详情
	 * @param id 会议室主键
	 * @param comId 企业号
	 * @return
	 */
	public MeetingRoom getMeetingRoomById(Integer id, Integer comId) {
		MeetingRoom meetingRoom = meetingRoomDao.getMeetingRoomById(id,comId);
		return meetingRoom;
	}
	/**
	 * 修改会议室图片主键
	 * @param meetingRoomId 会议室主键
	 * @param roomPicId 会议室图片主键
	 */
	public void updateMeetingRoomPicId(Integer meetingRoomId, Integer roomPicId) {
		//会议室
		MeetingRoom meetingRoom = new MeetingRoom();
		//会议室主键
		meetingRoom.setId(meetingRoomId);
		//会议室图片主键
		meetingRoom.setRoomPicId(roomPicId);
		meetingRoomDao.update(meetingRoom);
		
	}
	/**
	 * 修改会议室的名称
	 * @param meetingRoomId 会议室主键
	 * @param roomName 会议室名称
	 * @return
	 */
	public void updateMeetingRoomName(Integer meetingRoomId, String roomName) {
		//会议室
		MeetingRoom meetingRoom = new MeetingRoom();
		//会议室主键
		meetingRoom.setId(meetingRoomId);
		//会议室图片主键
		meetingRoom.setRoomName(roomName);
		meetingRoomDao.update(meetingRoom);
	}
	/**
	 * 修改会议室的地点
	 * @param meetingRoomId 会议室主键
	 * @param roomAddress 会议室地点
	 * @return
	 */
	public void updateMeetingRoomAddress(Integer meetingRoomId, String roomAddress) {
		//会议室
		MeetingRoom meetingRoom = new MeetingRoom();
		//会议室主键
		meetingRoom.setId(meetingRoomId);
		//会议室图片主键
		meetingRoom.setRoomAddress(roomAddress);
		meetingRoomDao.update(meetingRoom);		
	}
	/**
	 * 修改会议室的容纳数
	 * @param meetingRoomId 会议室主键
	 * @param containMax 会议室容纳数
	 * @return
	 */
	public void updateMeetingRoomContain(Integer meetingRoomId, Integer containMax) {
		//会议室
		MeetingRoom meetingRoom = new MeetingRoom();
		//会议室主键
		meetingRoom.setId(meetingRoomId);
		//会议室图片主键
		meetingRoom.setContainMax(containMax);
		meetingRoomDao.update(meetingRoom);
		
	}
	/**
	 * 修改会议室描述
	 * @param meetingRoomId 会议室主键
	 * @param content 会议室描述
	 * @return
	 */
	public void updateMeetingRoomContent(Integer meetingRoomId, String content) {
		//会议室
		MeetingRoom meetingRoom = new MeetingRoom();
		//会议室主键
		meetingRoom.setId(meetingRoomId);
		//会议室图片主键
		meetingRoom.setContent(content);
		meetingRoomDao.update(meetingRoom);
		
	}
	/**
	 * 修改会议室管理员
	 * @param meetingRoomId 会议室主键
	 * @param mamagerId 会议室管理员主键
	 * @return
	 */
	public void updateMeetingRoomManager(Integer meetingRoomId, Integer mamagerId) {
		//会议室
		MeetingRoom meetingRoom = (MeetingRoom) meetingRoomDao.objectQuery(MeetingRoom.class, meetingRoomId);
		//将待办设置成会议室负责人
		Integer comId = meetingRoom.getComId();
		Integer preManager = meetingRoom.getMamager();
		//会议室管理员主键
		meetingRoom.setMamager(mamagerId);
		meetingRoomDao.update(meetingRoom);
		
		//查询会议审核待办事项
		List<Meeting> listMeetForRoomChange = meetingService.listMeetForRoomChange(comId,preManager);
		if(null!=listMeetForRoomChange && listMeetForRoomChange.size()>0){
			for (Meeting meeting : listMeetForRoomChange) {
				//会议发布人员
				Integer organiser = meeting.getOrganiser();
				//会议主键
				Integer meetingId = meeting.getId();
				if(mamagerId.equals(organiser)){//现在的管理员是会议的发布人员，则直接审核通过
					//删除待办
					todayWorksService.delTodoWork(meetingId, comId, mamagerId, 
							ConstantInterface.TYPE_MEETROOM, "1");
					//直接审核通过
					RoomApply roomApply = meetingRoomDao.getMeetRoomApply(comId, meetingId);
					roomApply.setState("1");
					meetingRoomDao.update(roomApply);
				}
			}
		}
		//设置待办事项
		meetingRoomDao.updateMeetUserForTodo(comId,preManager,mamagerId,meetingRoomId);
		
	}
	/**
	 * 修改默认会议室
	 * @param meetingRoomId 会议室主键
	 * @param id 
	 * @param mamagerId 会议室管理员主键
	 * @return
	 */
	public void updateMeetingRoomDefault(Integer comId, Integer meetingRoomId, String isDefault) {
		
		if(isDefault.equals("1")){
			//将已有的默认会议室置空
			meetingRoomDao.deleteDefault(comId);
		}
		//会议室
		MeetingRoom meetingRoom = new MeetingRoom();
		//会议室主键
		meetingRoom.setId(meetingRoomId);
		//会议室图片主键
		meetingRoom.setIsDefault(isDefault);
		meetingRoomDao.update(meetingRoom);
	}
	/**
	 * 删除会议室
	 * @param comId 企业号
	 * @param ids 会议室主键
	 */
	public void delMeetingRoom(Integer comId, Integer[] ids) {
		if(null!=ids && ids.length>0){
			for (Integer id : ids) {
				meetingRoomDao.delById(MeetingRoom.class, id);
			}
		}
		
	}
	/**
	 * 会议室列表用于申请
	 * @param comId 企业号
	 * @return
	 */
	public List<MeetingRoom> listRoomForApply(Integer comId) {
		List<MeetingRoom> list = meetingRoomDao.listRoomForApply(comId);
		return list;
	}
	/**
	 * 取得所选时间会议室申请情况
	 * @param userInfo 当前操作员
	 * @param chooseDate选取的时间
	 * @param meetId 会议主键
	 * @return
	 */
	public List<RoomApply> listRoomApplyed(UserInfo userInfo, String chooseDate,Integer meetId) {
		List<RoomApply> list = meetingRoomDao.listRoomApplyed(userInfo,chooseDate,meetId);
		List<RoomApply> roomApplys = new ArrayList<RoomApply>();
		if(null!= list && list.size()>0){
			for (RoomApply roomApply : list) {
				if(roomApply.getState().equals("2")){
					roomApply.setState("0");
				}
				roomApplys.add(roomApply);
			}
		}
		return roomApplys;
	}
	/**
	 * 验证该会议室时间段是否被占用
	 * @param comId 企业号
	 * @param startDate 开始时间
	 * @param endDate 截止时间
	 * @param roomId 会议室主键
	 * @param meetingId 会议主键
	 * @return
	 */
	public Boolean checkRoomDisabled(Integer comId, String startDate,
			String endDate, Integer roomId, Integer meetingId) {
		if(null!=meetingId && meetingId>0){
			Meeting meeting  = (Meeting) meetingRoomDao.objectQuery(Meeting.class, meetingId);
			if(meeting.getMeetingAddrId().equals(roomId)//会议室没有变
					&&startDate.equals(meeting.getStartDate())//会议开始时间没有变
					&&endDate.equals(meeting.getEndDate())//会议结束时间没有变
					){
				return true;
			}
		}
		Boolean isDisable = meetingRoomDao.checkRoomDisabled(comId,startDate,endDate,roomId,meetingId);
		return isDisable;
	}
	/**
	 * 删除会议室申请
	 * @param id 会议室申请主键
	 */
	public void deleteRoomApplyed(Integer id) {
		meetingRoomDao.delById(RoomApply.class, id);
	}
	/**
	 * 分页查询会议室申请
	 * @param userInfo 当前操纵人员
	 * @param roomApply 会议室申请属性
	 * @return
	 */
	public List<RoomApply> listPagedRoomApply(UserInfo userInfo,
			RoomApply roomApply) {
		List<RoomApply> listRoomApply = meetingRoomDao.listPagedRoomApply(userInfo,roomApply);
		
		List<RoomApply> listRoomApplys = new ArrayList<RoomApply>();
		if(null != listRoomApply && listRoomApply.size()>0){
			for (RoomApply roomApplyObj : listRoomApply) {
				if(roomApplyObj.getTimeOut()!=1 && roomApplyObj.getState().equals("0")){//已经开始的会议或是结束的会议，自动审核通过
					roomApplyObj.setState("1");
					meetingRoomDao.update(roomApplyObj);
					
					//删除审核待办
					todayWorksService.delTodoWork(roomApply.getMeetingId(), userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETROOM,"1");
				}
				listRoomApplys.add(roomApplyObj);
			}
		}
		return listRoomApplys;
	}
	/**
	 * 查询当前用户负责的会议室申请
	 * @param comId 企业号
	 * @param userId 当前操作员
	 * @return
	 */
	public List<RoomApply> listUserRoomApply(Integer comId,Integer userId){
		List<RoomApply> listRoomApplys = meetingRoomDao.listUserRoomApply(comId,userId);
		return listRoomApplys;
	}
			
	
	/**
	 * 查询用于审核的会议申请
	 * @param userInfo 当前操纵人员
	 * @param roomApply 会议室申请属性
	 * @return
	 */
	public List<RoomApply> listPagedApplyForCheck(UserInfo userInfo,
			RoomApply roomApply) {
		List<RoomApply> listRoomApply = meetingRoomDao.listPagedApplyForCheck(userInfo,roomApply);
		List<RoomApply> listRoomApplys = new ArrayList<RoomApply>();
		if(null != listRoomApply && listRoomApply.size()>0){
			for (RoomApply roomApplyObj : listRoomApply) {
				if(roomApplyObj.getTimeOut()!=1 && roomApplyObj.getState().equals("0")){//已经开始的会议或是结束的会议，自动审核通过
					roomApplyObj.setState("1");
					meetingRoomDao.update(roomApplyObj);
					//删除审核待办
					todayWorksService.delTodoWork(roomApply.getMeetingId(), userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETROOM,"1");
				
				}
				listRoomApplys.add(roomApplyObj);
			}
		}
		return listRoomApplys;
	}
	/**
	 * 是否为会议室管理人员
	 * @param comId 企业号
	 * @param userId 操作人员
	 * @return
	 */
	public Integer countManageRoom(Integer comId, Integer userId) {
		Integer count = meetingRoomDao.countManageRoom(comId,userId);
		return count;
	}
	/**
	 * 撤销会议申请
	 * @param comId
	 * @param ids
	 */
	public void deleteRoomApply(Integer comId, Integer[] ids) {
		//查询用于删除的会议申请，修改
		List<RoomApply> listRoomApply = meetingRoomDao.listRoomApplyFordel(comId,ids);
		if(null != listRoomApply && listRoomApply.size()>0){
			for (RoomApply roomApply : listRoomApply) {
				//会议主键
				Integer meetingId = roomApply.getMeetingId();
				//清除会议的会议室
				Meeting meeting = new Meeting();
				meeting.setId(meetingId);
				meeting.setMeetingAddrId(0);
				meeting.setMeetingAddrName("");
				meetingRoomDao.update(meeting);
				
				MeetingRoom meetingRoom = (MeetingRoom) meetingRoomDao.objectQuery(MeetingRoom.class, roomApply.getRoomId());
				//删除管理员的待办事项
				todayWorksService.delTodoWork(roomApply.getMeetingId(), comId, meetingRoom.getMamager(), ConstantInterface.TYPE_MEETROOM,"1");
			}
			//批量删除会议申请
			meetingRoomDao.delById(RoomApply.class, ids);
			
			
			
		}
		
	}
	/**
	 * 审核会议申请
	 * @param comId
	 * @param ids
	 * @param state
	 * @param reason 
	 */
	public void updateRoomApply(UserInfo userInfo, Integer[] ids, String state, String reason) {
		for (Integer id : ids) {
			RoomApply roomApply = (RoomApply) meetingRoomDao.objectQuery(RoomApply.class, id);
			roomApply.setState(state);
			meetingRoomDao.update(roomApply);
			
			//通过申请取得会议信息
			Meeting meeting = meetingService.getMeetByAppId(userInfo.getComId(),id,roomApply.getMeetingId());
			
			UserInfo organiser = userInfoService.getUserInfoById(meeting.getOrganiser().toString());
			List<UserInfo> shares = new ArrayList<UserInfo>();
			shares.add(organiser);
			
			//向会议发布人员发送审核消息
			if("1".equals(state)){//审核通过
				String content = "会议《"+meeting.getTitle()+"》申请的会议室'"+meeting.getMeetingAddrName()+"'审核通过";
				todayWorksService.addTodayWorks(userInfo, null, meeting.getId(), content,
						ConstantInterface.TYPE_MEETROOM, shares, null);
			}else if("2".equals(state)){//审核不通过
				String content = "会议《"+meeting.getTitle()+"》申请的会议室'"+meeting.getMeetingAddrName()+"'审核未通过，";
				content +="审核意见为:"+reason;
				todayWorksService.addTodayWorks(userInfo, null, meeting.getId(), content,
						ConstantInterface.TYPE_MEETROOM, shares, null);
			}
			//删除会议室审核待办
			todayWorksService.delTodoWork(meeting.getId(), userInfo.getComId(), userInfo.getId(),
					ConstantInterface.TYPE_MEETROOM,"1");
		}
		
	}
	/**
	 * 取得会议的会议申请
	 * @param comId 企业号
	 * @param meetingId 会议主键
	 * @return
	 */
	public RoomApply getMeetRoomApply(Integer comId, Integer meetingId) {
		RoomApply roomApply = meetingRoomDao.getMeetRoomApply(comId,meetingId);
		return roomApply;
	}
	/**
	 * 取得可选的的会议室
	 * @param comId 企业号
	 * @param startDateStr 开始时间
	 * @param endDateStr 结束时间
	 * @param roomId 会议室主键
	 * @param meetId 会议主键
	 * @return
	 */
	public Integer getDefaultRoom(Integer comId, String startDateStr,
			String endDateStr, Integer roomId, Integer meetId) {
		Integer defaultRoomId = 0;
		MeetingRoom meetingRoom = new MeetingRoom();
		//选择的开始时间
		Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
		//选择的结束时间
		Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
		if(startDate.before(new Date())){//开始时间小于当前时间
			return defaultRoomId;
		}
		if(meetId>0){//是修改会议时间的
			//是否有过申请
			RoomApply roomApplyObj = meetingRoomDao.getMeetRoomApply(comId,meetId);
			if(null!=roomApplyObj){//申请过
				//申请的开始时间
				String applyStartDateStr = roomApplyObj.getStartDate();
				//申请的结束时间
				String applyEndDateStr = roomApplyObj.getEndDate();
				//申请的会议室主键
				Integer applyId = roomApplyObj.getRoomId();
				if(applyStartDateStr.equals(startDateStr)//开始时间没变
						&& applyEndDateStr.equals(endDateStr)//结束时间没有变
						&& applyId.equals(roomId)){//会议室没有变
					//不需要设置默认的会议室
				}else if(applyStartDateStr.equals(startDateStr)//开始时间没变
						&& applyEndDateStr.equals(endDateStr)//结束时间没有变
						&& !applyId.equals(roomId)){//会议室有变化
					//需要排除已申请的会议室
					meetingRoom = meetingRoomDao.getDefaultRoom(comId,startDateStr,endDateStr,roomId,applyId);
					if(null!=meetingRoom){
						defaultRoomId = meetingRoom.getId();
					}
				}else{//会议时间有变化或会议室没有变化
					//申请的开始时间
					Date applyStartDate = DateTimeUtil.parseDate(applyStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
					//申请的结束时间
					Date applyEndDate = DateTimeUtil.parseDate(applyEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
					//申请的开始时间是否在里面
					Boolean isStartIn = DateTimeUtil.isBetween(startDate, endDate, applyStartDate);
					
					if(isStartIn){//申请的开始时间在里面，需要排除已申请的会议室
						meetingRoom = meetingRoomDao.getDefaultRoom(comId,startDateStr,endDateStr,roomId,applyId);
						if(null!=meetingRoom){
							defaultRoomId = meetingRoom.getId();
						}
					}else{
						//申请的结束时间是否在里面
						Boolean isEndIn = DateTimeUtil.isBetween(startDate, endDate, applyEndDate);
						if(isEndIn){//申请的结束时间在里面，需要排除已申请的会议室
							
							meetingRoom = meetingRoomDao.getDefaultRoom(comId,startDateStr,endDateStr,roomId,applyId);
							if(null!=meetingRoom){
								defaultRoomId = meetingRoom.getId();
							}
						}else{
							Boolean isAllIn = DateTimeUtil.isBetween(applyStartDate, applyEndDate,startDate);
							if(isAllIn){//选择的时间在申请的时间里面，需要排除已申请的会议室
								
								meetingRoom = meetingRoomDao.getDefaultRoom(comId,startDateStr,endDateStr,roomId,defaultRoomId);
								if(null!=meetingRoom){
									defaultRoomId = meetingRoom.getId();
								}
							}else{//可以选择本次的会议室主键
								defaultRoomId = roomId;
							}
						}
						
					}
				}
			}else{
				//不需要排除
				meetingRoom = meetingRoomDao.getDefaultRoom(comId,startDateStr,endDateStr,roomId,0);
				if(null!=meetingRoom){
					defaultRoomId = meetingRoom.getId();
				}
			}
		}else{
			//不需要排除
			meetingRoom = meetingRoomDao.getDefaultRoom(comId,startDateStr,endDateStr,roomId,0);
			if(null!=meetingRoom){
				defaultRoomId = meetingRoom.getId();
			}
		}
		return defaultRoomId;
	}
	
	/**
	 * 重新设置待办人员
	 * @param comId 企业号
	 * @param userId 当前操作人员 
	 * @param mamager 会议室负责人
	 * @param roomId 会议室主键
	 */
	public void updateMeetUserForTodo(Integer comId, Integer userId, Integer mamager,
			Integer roomId) {
		meetingRoomDao.updateMeetUserForTodo(comId,userId,mamager,roomId);
		
	}
}
