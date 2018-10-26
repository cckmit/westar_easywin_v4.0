package com.westar.core.web.controller;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.MeetingRoom;
import com.westar.base.model.RoomApply;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.FileUtil;
import com.westar.core.service.MeetingRoomService;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UploadService;
/**
 * 会议室（只有管理人员才可以进行维护）
 * @author H87
 *
 */
@Controller
@RequestMapping("/meetingRoom")
public class MeetingRoomController extends BaseController{

	@Autowired
	MeetingRoomService meetingRoomService;
	
	@Autowired
	UploadService uploadService;
	
	
	@Autowired
	TodayWorksService todayWorksService;
	
	/**
	 * 获取会议室审核数量
	 * @param id
	 * @param roomPicId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/countManageRoom")
	public Map<String,Object> countManageRoom(){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null!=userInfo){
			Integer countManageRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put("countManageRoom", countManageRoom);
		}else{
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_F);
			map.put("info", "连接已断开，请重新登陆");
		}
		return map;
	}
	
	
	
	/**
	 * 跳转会议列表
	 * @return
	 */
	@RequestMapping("/listPagedMeetingRoom")
	public ModelAndView listPagedMeetingRoom(MeetingRoom meetingRoom){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/meeting/meetingCenter");
		//分页查询会议室信息
		List<MeetingRoom> meetingRooms = meetingRoomService.listPagedMeetingRoom(meetingRoom,userInfo);
		view.addObject("userInfo", userInfo);
		view.addObject("meetingRooms", meetingRooms);
		//是否为会议室管理人员（是否有权审核）
		Integer countManageRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
		view.addObject("countManageRoom", countManageRoom);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_MEETING);
		return view;
		
	}
	/**
	 * 添加会议室界面
	 * @return
	 */
	@RequestMapping("/addMeetingRoomPage")
	public ModelAndView addMeetingRoomPage(){
		ModelAndView mav = new ModelAndView("/meetingRoom/addMeetingRoom");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	/**
	 * 添加会议室
	 * @param meetingRoom 会议室属性
	 * @param redirectPage 页面跳转
	 * @param way 添加方式
	 * @param fileNameP 页面附件名称
	 * @param filePathP 页面附件路径
	 * @return
	 */
	@RequestMapping("/addMeetingRoom")
	public ModelAndView addMeetingRoom(MeetingRoom meetingRoom,String redirectPage,
			String way,String fileNameP,String filePathP,String sid){
		UserInfo userInfo = this.getSessionUser();
		//会议室的图片主键
		Integer picId = this.getPicId(userInfo,fileNameP,filePathP);
		meetingRoom.setRoomPicId(picId);
		
		//添加会议室
		meetingRoomService.addMeetingRoom(meetingRoom,userInfo.getComId());
		ModelAndView view = new ModelAndView();
		view.setViewName("/refreshParent");
	
		this.setNotification(Notification.SUCCESS, "添加成功！");
		return view;
	}
	/**
	 * 会议室图片的主键
	 * @param userInfo当前操作人员
	 * @param fileNameP附件名称
	 * @param filePathP附件路径
	 * @return
	 */
	private Integer getPicId(UserInfo userInfo, String fileNameP, String filePathP) {
		
		Integer fileId = 0;
		
		if(null!=fileNameP && !"".equals(fileNameP) && null!=filePathP && !"".equals(filePathP)){
			//文件路径
			String basepath = FileUtil.getRootPath();
			String parentPath = basepath+ File.separator+"static"+ File.separator + "temp/meetingRoomImg"+File.separator+userInfo.getComId()+File.separator +userInfo.getId();;
			
			//原图
			File orgFile = new File(basepath+File.separator+filePathP);
			if(orgFile.isFile()){
				//从stataic的文件夹复制文件
				String path = File.separator + "uploads"+File.separator+userInfo.getComId();
				Upfiles orgUpfile = CommonUtil.copyFile(orgFile,uploadService,userInfo,path);
				if(null!=orgUpfile){
					fileId = orgUpfile.getId();
				}
			}
			//删除临时文件
			if(!"".equals(parentPath) && new File(parentPath).isDirectory()){
				File[] f = new File(parentPath).listFiles();
				String prefix = userInfo.getComId()+"_"+userInfo.getId();
				for (File file : f) {
					if(file.getName().startsWith(prefix)){
						file.delete();
					}
				}
			}
		}
		return fileId;
	}
	/**
	 * 添加会议室界面
	 * @return
	 */
	@RequestMapping("/updateMeetingRoomPage")
	public ModelAndView updateMeetingRoomPage(Integer id){
		ModelAndView mav = new ModelAndView("/meetingRoom/updateMeetingRoom");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		//取得会议室的
		MeetingRoom meetingRoom = meetingRoomService.getMeetingRoomById(id,userInfo.getComId());
		mav.addObject("meetingRoom", meetingRoom);
		
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_MEETING);
		return mav;
	}
	/**
	 * 修改会议室的图片
	 * @param id 会议室主键
	 * @param roomPicId会议室图片主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateRoomPicId")
	public Map<String,Object> updateRoomPicId(Integer id,Integer roomPicId){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null!=userInfo){
			//添加会议室
			meetingRoomService.updateMeetingRoomPicId(id, roomPicId);
			map.put("status", "y");
			map.put("info", "修改成功");
		}else{
			map.put("status", "f");
			map.put("info", "连接已断开，请重新登陆");
		}
		return map;
	}
	/**
	 * 修改会议室的名称
	 * @param id 会议室主键
	 * @param roomName 会议室名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateRoomName")
	public Map<String,Object> updateRoomName(Integer id,String roomName){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null!=userInfo){
			//修改会议室名称
			meetingRoomService.updateMeetingRoomName(id, roomName);
			map.put("status", "y");
			map.put("info", "修改成功");
		}else{
			map.put("status", "f");
			map.put("info", "连接已断开，请重新登陆");
		}
		return map;
	}
	/**
	 * 修改会议室的地点
	 * @param id 会议室主键
	 * @param roomAddress 会议室地点
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateRoomAddress")
	public Map<String,Object> updateRoomAddress(Integer id,String roomAddress){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null!=userInfo){
			//修改会议室地点
			meetingRoomService.updateMeetingRoomAddress(id, roomAddress);
			map.put("status", "y");
			map.put("info", "修改成功");
		}else{
			map.put("status", "f");
			map.put("info", "连接已断开，请重新登陆");
		}
		return map;
	}
	/**
	 * 修改会议室的容纳数
	 * @param id 会议室主键
	 * @param containMax 会议室地点
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateRoomContain")
	public Map<String,Object> updateRoomContain(Integer id,Integer containMax){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null!=userInfo){
			//修改会议室容纳数
			meetingRoomService.updateMeetingRoomContain(id, containMax);
			map.put("status", "y");
			map.put("info", "修改成功");
		}else{
			map.put("status", "f");
			map.put("info", "连接已断开，请重新登陆");
		}
		return map;
	}
	/**
	 * 修改会议室描述
	 * @param id 会议室主键
	 * @param containMax 会议室描述
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateRoomContent")
	public Map<String,Object> updateRoomContent(Integer id,String content){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null!=userInfo){
			//修改会议室描述
			meetingRoomService.updateMeetingRoomContent(id, content);
			map.put("status", "y");
			map.put("info", "修改成功");
		}else{
			map.put("status", "f");
			map.put("info", "连接已断开，请重新登陆");
		}
		return map;
	}
	/**
	 * 修改会议室管理员
	 * @param id 会议室主键
	 * @param mamagerId 会议室管理员主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateRoomMamager")
	public Map<String,Object> updateRoomMamager(Integer id,Integer mamagerId){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null!=userInfo){
			//修改会议室管理员
			meetingRoomService.updateMeetingRoomManager(id, mamagerId);
			map.put("status", "y");
			map.put("info", "修改成功");
		}else{
			map.put("status", "f");
			map.put("info", "连接已断开，请重新登陆");
		}
		return map;
	}
	/**
	 * 修改会议室管理员
	 * @param id 会议室主键
	 * @param isDefault 会议室管理员主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateRoomDefault")
	public Map<String,Object> updateRoomDefault(Integer id,String isDefault){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null!=userInfo){
			//修改会议室管理员
			meetingRoomService.updateMeetingRoomDefault(userInfo.getComId(),id, isDefault);
			map.put("status", "y");
			map.put("info", "修改成功");
		}else{
			map.put("status", "f");
			map.put("info", "连接已断开，请重新登陆");
		}
		return map;
	}
	/**
	 * 删除会议室
	 * @param ids 会议室主键
	 * @param redirectPage 页面跳转
	 * @return
	 */
	@RequestMapping("/delMeetingRoom")
	public ModelAndView delMeetingRoom(Integer[] ids,String redirectPage){
		UserInfo userInfo = this.getSessionUser();
		try {
			//删除会议室
			meetingRoomService.delMeetingRoom(userInfo.getComId(),ids);
			this.setNotification(Notification.SUCCESS, "删除成功");
		} catch (Exception e) {
			this.setNotification(Notification.ERROR, "会议室已被使用，不能删除");
		}
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	
	/*************************以下是会议室申请*************************************/
	/**
	 * 分页查询会议室申请
	 *roomApply
	 * @return
	 */
	@RequestMapping("/listPagedRoomApply")
	public ModelAndView listPagedRoomApply(RoomApply roomApply){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/meeting/meetingCenter");
		List<RoomApply> listRoomApply =  meetingRoomService.listPagedRoomApply(userInfo,roomApply);
		//是否为会议室管理人员（是否有权审核）
		Integer countManageRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
		view.addObject("countManageRoom", countManageRoom);
		view.addObject("listRoomApply", listRoomApply);
		view.addObject("userInfo", userInfo);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_MEETING);
		return view;
		
	}
	/**
	 * 分页查询会议室申请
	 *roomApply
	 * @return
	 */
	@RequestMapping("/listPagedApplyForCheck")
	public ModelAndView listPagedApplyForCheck(RoomApply roomApply){
		ModelAndView view = new ModelAndView("/meeting/meetingCenter");
		UserInfo userInfo = this.getSessionUser();
		List<RoomApply> listRoomApply =  meetingRoomService.listPagedApplyForCheck(userInfo,roomApply);
		//是否为会议室管理人员（是否有权审核）
		Integer countManageRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
		view.addObject("countManageRoom", countManageRoom);
		
		view.addObject("listRoomApply", listRoomApply);
		view.addObject("userInfo", userInfo);
		//设置代办已读
		todayWorksService.updateTodoWorkRead(null, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_MEETROOM, 0);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_MEETING);
		return view;
		
	}
	/**
	 * 会议室申请
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param roomId 会议室主键 为0是添加
	 * @param meetId 会议主键  为0是添加
	 * @return
	 */
	@RequestMapping("/listRoomForApply")
	public ModelAndView listRoomForApply(String startDate,String endDate,Integer roomId,Integer meetId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/meetingRoom/listRoomForApply");
		//查询会议室信息
		List<MeetingRoom> meetingRooms = meetingRoomService.listRoomForApply(userInfo.getComId());
		view.addObject("userInfo", userInfo);
		view.addObject("today", DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd));
		String sections = "";
		sections +="[";
		if(null!=meetingRooms && meetingRooms.size()>0){
			for (Integer i=0;i<meetingRooms.size();i++) {
				MeetingRoom meetingRoom = meetingRooms.get(i);
				Integer meetingRoomId = meetingRoom.getId();
				sections +="{key:\""+meetingRoomId+"\",label:\"<div class='dhx_matrix_scell_title' style='height:44px;'>" 
						+"<div style=' display: inline; margin-top: 2px; '>" 
						+"<input id='room_"+meetingRoomId+"' name='autoSelectRoom' type='checkbox' onclick='selectMtRoom(this);'/></div>"
						+"<a href='javascript:void(0)' id='roomName_"+meetingRoomId+"' style='text-align: left;' title='"+meetingRoom.getRoomName()+"' >"+meetingRoom.getRoomName()+"</a>"
						+"</div>\"}";
				if(i<meetingRooms.size()-1){
					sections +=",";
				}
			}
		}
		sections +="]";
		view.addObject("sections", sections);
		if(null==startDate || "".equals(startDate) || null==endDate || "".equals(endDate)){//没有选择时间，则不能选择可用会议室
			view.addObject("defaultRoomId", 0);
			return view;
		}
		//取得可选的的会议室
		Integer defaultRoomId = meetingRoomService.getDefaultRoom(userInfo.getComId(),startDate,endDate,roomId,meetId);
		view.addObject("defaultRoomId", defaultRoomId);
		return view;
	}
	/**
	 * 已被申请的会议室以及时间区间
	 * @param chooseDate 选择的时间段
	 * @param meetId 会议主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listRoomApplyed")
	public Map<String,Object> listRoomApplyed(String chooseDate,Integer meetId){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
		}else{
			if(null==chooseDate || "".equals(chooseDate)){
				chooseDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			}
			//取得所选时间会议室申请情况
			List<RoomApply> applyedList = meetingRoomService.listRoomApplyed(userInfo,chooseDate,meetId);
			map.put("applyedList", applyedList);
			map.put("status", "y");
		}
		return map;
	}
	/**
	 * 验证该会议室时间段是否被占用
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param roomId 会议室主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkRoomDisabled")
	public Map<String,Object> checkRoomDisabled(String startDate,String endDate,Integer roomId,Integer meetingId){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
		}else{
			//验证该会议室时间段是否被占用
			Boolean isDisable = meetingRoomService.checkRoomDisabled(userInfo.getComId(),startDate,endDate,roomId,meetingId);
			map.put("status", "y");
			//默认不能添加
			map.put("disAble", "1");
			if(isDisable){//可以添加
				map.put("disAble", "0");
			}
		}
		return map;
	}
	/**
	 * 删除会议室申请(单个)
	 * @param id 会议室申请主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteRoomApplyed")
	public Map<String,Object> deleteRoomApplyed(Integer id){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
		}else{
			meetingRoomService.deleteRoomApplyed(id);
			map.put("status", "y");
		}
		return map;
	}
	/**
	 * 撤消会议申请
	 * @param ids 会议申请主键
	 * @param redirectPage撤消后页面跳转
	 * @return
	 */
	@RequestMapping("/deleteRoomApply")
	public ModelAndView deleteRoomApply(Integer[] ids,String redirectPage){
		UserInfo userInfo = this.getSessionUser();
		//撤销会议申请
		meetingRoomService.deleteRoomApply(userInfo.getComId(),ids);
		this.setNotification(Notification.SUCCESS, "成功撤销会议申请");
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	/**
	 * 审核会议申请
	 * @param ids
	 * @param redirectPage
	 * @param state
	 * @return
	 */
	@RequestMapping("/checkRoomApply")
	public ModelAndView checkRoomApply(Integer[] ids,String redirectPage,String state,String reason){
		UserInfo userInfo = this.getSessionUser();
		//审核会议申请
		meetingRoomService.updateRoomApply(userInfo,ids,state,reason);
		this.setNotification(Notification.SUCCESS, "成功审核会议申请");
		return new ModelAndView("redirect:"+redirectPage);
	}
	/**
	 * 审核会议申请
	 * @param appIds
	 * @param redirectPage
	 * @param state
	 * @return
	 */
	@RequestMapping("/checkRoomApplyOne")
	public ModelAndView checkRoomApplyOne(Integer appId,String redirectPage,String state,String reason){
		UserInfo userInfo = this.getSessionUser();
		Integer[] ids =  new Integer[]{appId};
		//审核会议申请
		meetingRoomService.updateRoomApply(userInfo,ids,state,reason);
		this.setNotification(Notification.SUCCESS, "成功审核会议申请");
		return new ModelAndView("redirect:"+redirectPage);
	}
	
}