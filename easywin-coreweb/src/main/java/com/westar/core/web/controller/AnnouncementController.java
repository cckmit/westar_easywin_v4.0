package com.westar.core.web.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Announcement;
import com.westar.base.model.Department;
import com.westar.base.model.Talk;
import com.westar.base.model.TalkUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.pojo.BusModVo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.AnnouncementService;
import com.westar.core.service.LogsService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TalkService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UploadService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.web.FreshManager;

@Controller
@RequestMapping("/announcement")
public class AnnouncementController extends BaseController{

	@Autowired
	AnnouncementService announcementService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	LogsService logsService;
	
	@Autowired
	TalkService talkService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	/********************************公告****************************************/
	
	/**
	 * 分页显示公告
	 * @param request
	 * @param announ
	 * @return
	 */
	@RequestMapping("/listPagedAnnoun")
	public ModelAndView listPagedAnnoun(HttpServletRequest request,Announcement announ) {
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		
		List<Announcement> list = announcementService.listPagedAnnoun(announ,userInfo);
		ModelAndView mav = new ModelAndView("/announcement/announCenter");
		
		//取得常用人员列表
		List<UserInfo> listCreators = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		mav.addObject("listCreators",listCreators);
						
		//初始化创建人名称
		if(null!=announ.getCreator() && announ.getCreator()!=0){
			UserInfo creator = userInfoService.getUserInfoById(announ.getCreator().toString());
			if(null!=creator){
				announ.setCreatorName(creator.getUserName());
			}
		}
		
		mav.addObject("list",list);
		mav.addObject("announ",announ);
		mav.addObject("userInfo",userInfo);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_ANNOUNCEMENT);
		return mav;
	}
	/**
	 * 至公告添加页面
	 * @return
	 */
	@RequestMapping("/addAnnounPage")
	public ModelAndView addAnnounPage(){
		ModelAndView mav = new ModelAndView("/announcement/addAnnoun");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo",userInfo);
		return mav;
	}
	/**
	 * 至公告添加页面
	 * @return
	 */
	@RequestMapping("/addAnnounBySimplePage")
	public ModelAndView addAnnounBySimplePage(){
		ModelAndView mav = new ModelAndView("/announcement/addAnnoun");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo",userInfo);
		return mav;
	}
	/**
	 *  添加公告
	 * @param announ 公告
	 * @param sendWay
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addAnnoun")
	public ModelAndView addAnnoun(Announcement announ,String sendWay) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		
		announcementService.addAnnoun(announ, null, userInfo, sendWay);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		ModelAndView view = new ModelAndView("/refreshParent");
		return view;
	}
	/**
	 * 查看公告
	 * @param request
	 * @param redirectPage
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewAnnoun")
	public ModelAndView viewAnnoun(HttpServletRequest request,String redirectPage,Integer id) throws Exception{
		ModelAndView mav = null ;
		UserInfo userInfo = this.getSessionUser();
		//公告查看权限验证
		if(!announcementService.authorCheck(userInfo.getComId(),id,userInfo.getId())){
			mav = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有查看公告权限");
		}else{
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ANNOUNCEMENT, id);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				String viewType = viewRecordService.addViewRecordReturn(userInfo,viewRecord);
				if(ConstantInterface.TYPE_ADD.equals(viewType)){
					//增加查看次数
					announcementService.updateAnnounRead(id);
				}
			}
			
			Announcement announcement = announcementService.getAnnouncementInfo(id,userInfo.getId());
			mav = new ModelAndView("/announcement/viewAnnoun");
			//获取公告前五条留言
			List<Talk> talks = talkService.listFiveTalk(id,userInfo.getComId(),ConstantInterface.TYPE_ANNOUNCEMENT);
			
			List<Department> scopeDep = announcementService.listAnnumnScopeDep(announcement.getComId(), announcement.getId());
			List<UserInfo> scopeUser = announcementService.listAnnumnScopeUser(announcement.getComId(), announcement.getId());
			
			mav.addObject("talks",talks);
			mav.addObject("busId",id);
			
			mav.addObject("announ", announcement);
			mav.addObject("scopeDep", scopeDep);
			mav.addObject("scopeUser", scopeUser);
			mav.addObject("userInfo", userInfo);
		}
		//查看公告详情，删除消息提醒
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ANNOUNCEMENT,0);
		return mav;
	}
	/**
	 * 修改公告
	 * @param request
	 * @param redirectPage
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editAnnoun")
	public ModelAndView editAnnoun(HttpServletRequest request,String redirectPage,Integer id) throws Exception{
		ModelAndView mav = null ;
		UserInfo userInfo = this.getSessionUser();
		
		Announcement announcement = announcementService.getAnnouncementInfo(id,userInfo.getId());
		
		if(announcement.getCreator() == userInfo.getId() || Integer.valueOf(userInfo.getAdmin())>0){
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ANNOUNCEMENT, id);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				String viewType = viewRecordService.addViewRecordReturn(userInfo,viewRecord);
				if(ConstantInterface.TYPE_ADD.equals(viewType)){
					//增加查看次数
					announcementService.updateAnnounRead(id);
				}
			}
			mav = new ModelAndView("/announcement/editAnnoun");
			
			List<Department> scopeDep = announcementService.listAnnumnScopeDep(announcement.getComId(), announcement.getId());
			List<UserInfo> scopeUser = announcementService.listAnnumnScopeUser(announcement.getComId(), announcement.getId());
			
			mav.addObject("announ", announcement);
			mav.addObject("scopeDep", scopeDep);
			mav.addObject("scopeUser", scopeUser);
			mav.addObject("userInfo", userInfo);
		}else{
			mav = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有编辑公告权限");
		}
		//查看公告详情，删除消息提醒
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ANNOUNCEMENT,0);
		return mav;
	}
	/**
	 * 删除公告
	 * @param id 公告主键
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/delAnnouncement")
	public Map<String, Object> delAnnouncement(Integer id) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Boolean flag =  announcementService.delPreAnnouncement(new Integer[]{id},sessionUser);
		if(!flag){
			this.setNotification(Notification.ERROR, "公告已被删除！");
		}else{
			//系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "删除公告",
					ConstantInterface.TYPE_ANNOUNCEMENT, sessionUser.getComId(),sessionUser.getOptIP());
			this.setNotification(Notification.SUCCESS, "删除成功！");
		}
		map.put("status", "y");
		return map;
	}
	/**
	 * 批量删除公告
	 * @param ids
	 * @param redirectPage
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/delBatchAnnouncement")
	public ModelAndView delBatchAnnouncement(Integer[] ids,String redirectPage) throws Exception{
		UserInfo sessionUser = this.getSessionUser();
		Boolean falg = announcementService.delPreAnnouncement(ids,sessionUser);
		if(!falg){
			this.setNotification(Notification.ERROR, "公告已被删除！");
		}else{
			//系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量删除公告",
					ConstantInterface.TYPE_ANNOUNCEMENT, sessionUser.getComId(),sessionUser.getOptIP());
			this.setNotification(Notification.SUCCESS, "删除成功！");
		}
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		return mav;
	}
	
	/**
	 * 公告标题变更
	 * @param announ
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/announTitleUpdate")
	public String announTitleUpdate(Announcement announ) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		announ.setComId(userInfo.getComId());
		boolean succ = announcementService.updateAnnounTitle(announ, userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	
	/**
	 * 公告说明更新
	 * @param announ
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateAnnounmRemark")
	public String updateAnnounmRemark(Announcement announ) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		announ.setComId(userInfo.getComId());
		boolean succ = announcementService.updateAnnounmRemark(announ, userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	
	/**
	 * 公告类型更新
	 * @param announ
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateAnnounType")
	public String updateAnnounType(Announcement announ) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		announ.setComId(userInfo.getComId());
		boolean succ = announcementService.updateAnnounType(announ, userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 * 公告重要程度更新
	 * @param announ
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateAnnounGrade")
	public String updateAnnounGrade(Announcement announ) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		announ.setComId(userInfo.getComId());
		boolean succ = announcementService.updateAnnounGrade(announ, userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 * 删除公告附件
	 * @param announId
	 * @param upfileId
	 * @param userInfo
	 */
	@ResponseBody
	@RequestMapping(value="/delAnnounFile")
	public String delAnnounFile(Integer announId,Integer upfileId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		boolean succ = announcementService.delAnnounFile(announId,upfileId, userInfo);
		if(succ){
			return "删除成功";
		}else{
			return "删除失败";
		}
	}
	/**
	 *添加公告人员范围
	 * @param announ
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addAnnounScopeUser")
	public String addAnnounScopeUser(Integer announId,Integer userId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		boolean succ = announcementService.addAnnounScopeUser(announId,userId ,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 *添加公告部门范围
	 * @param announ
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addAnnounScopeDep")
	public String addAnnounScopeDep(Integer announId,Integer depId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		boolean succ = announcementService.addAnnounScopeDep(announId,depId ,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 *删除公告人员范围
	 * @param announ
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delAnnounScopeUser")
	public String delAnnounScopeUser(Integer announId,Integer userId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		boolean succ = announcementService.delAnnounScopeUser(announId,userId ,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 *删除公告部门范围
	 * @param announ
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delAnnounScopeDep")
	public String delAnnounScopeDep(Integer announId,Integer depId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		boolean succ = announcementService.delAnnounScopeDep(announId,depId ,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/********************************公告附件****************************************/
	/**
	 * 公告附件页面
	 * @param announId 公告主键
	 * @return
	 */
	@RequestMapping("/announFilePage")
	public ModelAndView announFilePage(Integer announId) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/announcement/announFiles");
		//查看投票附件，删除消息提醒
		//msgShareService.updateTodoWorkRead(announId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTE,0);
		//投票讨论的附件
		List<TalkUpfile> announTalkFiles = talkService.listPagedFiles(userInfo.getComId(),announId,ConstantInterface.TYPE_ANNOUNCEMENT);
		mav.addObject("announTalkFiles",announTalkFiles);
		mav.addObject("userInfo",userInfo);
		mav.addObject("announId",announId);
		return mav;
	}
	
	/********************************公告讨论****************************************/
	/**
	 * 公告查看讨论页面
	 * @param announId 主键
	 * @return
	 */
	@RequestMapping("/announTalkPage")
	public ModelAndView announTalkPage(Integer announId) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/announcement/announTalk");
		//查看投票讨论，删除消息提醒
		//msgShareService.updateTodoWorkRead(voteId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTE,0);
		
		//讨论
		List<Talk> talks = talkService.listPagedTalk(announId,userInfo.getComId(),ConstantInterface.TYPE_ANNOUNCEMENT);
		mav.addObject("talks",talks);
		mav.addObject("busId",announId);
		mav.addObject("sessionUser",userInfo);
		return mav;
	}
	/**
	 * 讨论回复
	 * @param talk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/addAnnounTalk")
	public Map<String, Object> addAnnounTalk(Talk talk) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		talk.setComId(sessionUser.getComId());
		talk.setTalker(sessionUser.getId());
		
		//公告信息
		Announcement announcement = announcementService.getAnnouncementInfo(talk.getBusId(), sessionUser.getId());
		
		BusModVo busModVo = new BusModVo(talk.getBusId(),ConstantInterface.TYPE_ANNOUNCEMENT,announcement.getTitle());
		
		Integer talkId = talkService.addTalk(talk,sessionUser,busModVo);
		//模块日志添加
		if(-1==talk.getParentId()){
			logsService.addLogs(sessionUser.getComId(),talk.getBusId(), sessionUser.getId(), sessionUser.getUserName(), "参与公告讨论",ConstantInterface.TYPE_ANNOUNCEMENT);
		}else{
			logsService.addLogs(sessionUser.getComId(),talk.getBusId(), sessionUser.getId(), sessionUser.getUserName(), "回复公告讨论",ConstantInterface.TYPE_ANNOUNCEMENT);
		}
		
		map.put("status", "y");
		map.put("talkId", talkId);
		//用于返回页面拼接代码
		Talk talk4Page = talkService.getTalk(talkId, sessionUser.getComId(),ConstantInterface.TYPE_ANNOUNCEMENT);
		map.put("talk", talk4Page);
		map.put("userInfo", sessionUser);
		return map;
	}
	/**
	 * 删除讨论回复
	 * @param voteTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/delAnnounTalk")
	public Map<String, Object> delAnnounTalk(Talk talk,String delChildNode) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		talk.setComId(sessionUser.getComId());
		
		//要删除的回复所有子节点和自己
		List<Integer> childIds = talkService.delTalk(talk,delChildNode,sessionUser,ConstantInterface.TYPE_ANNOUNCEMENT);
		map.put("status", "y");
		map.put("childIds", childIds);
		//模块日志添加
		logsService.addLogs(sessionUser.getComId(), talk.getBusId(), sessionUser.getId(), sessionUser.getUserName(), "删除公告讨论",ConstantInterface.TYPE_ANNOUNCEMENT);
		return map;
	}
	/**
	 * 删除查看范围
	 * @param type
	 * @param comId 类型
	 * @param busId 主键
	 * @param announId 公告主键
	 */
	@ResponseBody
	@RequestMapping("/delScope")
	public Map<String, Object> delScope(String type,Integer busId,Integer announId) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		announcementService.delScope(type, sessionUser.getComId(), busId, announId);
		boolean changeScope = announcementService.delAfterUpdateScope(announId,sessionUser.getComId());
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		map.put("changeScope",changeScope);
		return map;
	}
	/**
	 * 删除查看范围
	 * @param type
	 * @param comId 类型
	 * @param busId 主键
	 * @param announId 公告主键
	 */
	@ResponseBody
	@RequestMapping("/updateScope")
	public Map<String, Object> updateScope(String type,Integer[] busId,Integer announId) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		announcementService.updateScope(type, busId, announId,sessionUser.getComId());
		announcementService.addBeforeUpdateScope(announId,sessionUser.getComId());
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
}