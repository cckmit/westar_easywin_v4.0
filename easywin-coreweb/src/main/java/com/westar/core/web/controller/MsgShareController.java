package com.westar.core.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Answer;
import com.westar.base.model.Clock;
import com.westar.base.model.Customer;
import com.westar.base.model.DailyTalk;
import com.westar.base.model.FeedBackInfo;
import com.westar.base.model.FeedBackType;
import com.westar.base.model.Item;
import com.westar.base.model.ItemTalk;
import com.westar.base.model.MsgShare;
import com.westar.base.model.MsgShareLog;
import com.westar.base.model.MsgShareTalk;
import com.westar.base.model.MsgShareTalkUpfile;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.ShareGroup;
import com.westar.base.model.Task;
import com.westar.base.model.TaskTalk;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.model.Vote;
import com.westar.base.model.VoteTalk;
import com.westar.base.model.WeekRepTalk;
import com.westar.base.pojo.BaseTalk;
import com.westar.base.pojo.MsgNoRead;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.service.ClockService;
import com.westar.core.service.CrmService;
import com.westar.core.service.DailyService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.ItemService;
import com.westar.core.service.MeetingRoomService;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.ProductService;
import com.westar.core.service.ProductTalkService;
import com.westar.core.service.QasService;
import com.westar.core.service.TaskService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.service.VoteService;
import com.westar.core.service.WeekReportService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

/**
 * 信息分享 若要删除分享 type=1的，则不能真正删除(修改 tracetype，变动分享范围) 详情zzq
 * @author lj
 *
 */
@Controller
@RequestMapping("/msgShare")
public class MsgShareController extends BaseController {
	
	@Autowired
	MsgShareService msgShareService;
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	VoteService voteService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	CrmService crmService;
	
	@Autowired
	QasService qasService;
	
	@Autowired
	ClockService clockService;
	
	@Autowired
	WeekReportService weekReportService;
	
	@Autowired
	MeetingRoomService meetingRoomService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	ViewRecordService viewRecordService;

	@Autowired
	DailyService dailyService;

	@Autowired
    ProductService productService;
	
	@Autowired
    ProductTalkService productTalkService;

    /**
	 * 添加分享信息
	 * @param idType 分享范围类别
	 * @return
	 */
	@RequestMapping("/addMsgSharePage")
	public ModelAndView addMsgSharePage(String idType){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/msgShare/addMsgShare");
		mav.addObject("sessionUser",userInfo);
		
		//上次使用的分组
		List<UsedGroup> usedGroups = null;
		if(null==idType || "".equals(idType)){
			usedGroups = userInfoService.listUsedGroup(userInfo.getComId(),userInfo.getId());
		}else{
			usedGroups = new ArrayList<UsedGroup>();
			
			String[] groupIdS = idType.split(",");
			if(null!=groupIdS && groupIdS.length>0){
				for(String str :groupIdS){
					if("0".equals(str.split("@")[1])){
						//所有人
						UsedGroup usedGroup = new UsedGroup();
						usedGroup.setGroupType("0");
						usedGroups.add(usedGroup);
						break;
					}else if("2".equals(str.split("@")[1])){
						
						//自己
						UsedGroup usedGroup = new UsedGroup();
						usedGroup.setGroupType("2");
						usedGroups.add(usedGroup);
						break;
					}else{
						UsedGroup usedGroup = new UsedGroup();
						usedGroup.setGroupType("1");
						usedGroup.setGrpId(Integer.parseInt(str.split("@")[0]));
						usedGroups.add(usedGroup);
					}
				}
			}
		}
		//个人组群集合
		List<SelfGroup> listSelfGroup = userInfoService.listSelfGroup(userInfo.getComId(),userInfo.getId());
		//最近一次使用的分组的类型，分组名称以及自定义所有的分组
		Map<String, String> grpMap = CommonUtil.usedGrpJson(usedGroups,listSelfGroup);
		//最近一次使用的分组的类型
		mav.addObject("idType", grpMap.get("idType"));
		if(null==idType || "".equals(idType)){
			//分组名称
			mav.addObject("scopeTypeSel", grpMap.get("scopeTypeSel"));
		}
		//自定义所有的分组
		mav.addObject("selfGroupStr", grpMap.get("selfGroupStr"));
		return mav;
	}
	
	
	/**
	 * 新增分享信息（没有使用）
	 * @param msgShare
	 * @return
	 */
	@RequestMapping("/addMsgShare")
	public ModelAndView addMsgShare(MsgShare msgShare,String idType){
		//表情替换
		String content = CommonUtil.biaoQingReplace(msgShare.getContent());
		msgShare.setContent(content);
		UserInfo userInfo = this.getSessionUser();
		//获取信息分享范围
		String[] groupIdS = idType.split(",");
		if(null!=groupIdS && groupIdS.length>0){
			List<ShareGroup> listShareGroup = new ArrayList<ShareGroup>();
			for(String str :groupIdS){
				if("0".equals(str.split("@")[1])){
					//所有人
					msgShare.setScopeType(ALL_COLLEAGUE);
					break;
				}else if("2".equals(str.split("@")[1])){
					//自己
					msgShare.setScopeType(MYSELF);
					break;
				}else{
				    //根据组界定范围
					msgShare.setScopeType(BY_GROUPID);
					ShareGroup shareGroup = new ShareGroup();
					shareGroup.setComId(userInfo.getComId());
					shareGroup.setGrpId(Integer.parseInt(str.split("@")[0]));
					listShareGroup.add(shareGroup);
				}
			}
			msgShare.setShareGroup(listShareGroup);
		}
		msgShare.setCreator(userInfo.getId());
		msgShare.setComId(userInfo.getComId());
		//任务分享类型
		msgShare.setType("1");
		
		//工作类型为分享
		msgShare.setTraceType(0);
		//设置公开类型 默认公开
		msgShare.setIsPub(1);
		
		msgShareService.addMsgShare(msgShare,userInfo);
		ModelAndView view = new ModelAndView("/bodyRefresh");
		return view;
	}
	/**
	 * AJAX新增分享信息
	 * @param msgShare
	 * @param idType
	 * @throws IOException 
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddMsgShare")
	public Map<String, Object> ajaxAddMsgShare(MsgShare msgShare,String idType){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		//重新解析信息分享信息
		msgShare = CommonUtil.getMsgShare(idType, "1", msgShare.getContent(), userInfo);
		//创建人姓名
		msgShare.setCreatorName(userInfo.getUserName());
		//工作类型为分享
		msgShare.setTraceType(0);
		//设置公开类型 默认公开
		msgShare.setIsPub(1);
		
		//通过分享添加日报
		Integer msgId= dailyService.addDailyByMsgShare(msgShare,userInfo);
		
		MsgShare vo = msgShareService.getMsgShareById(userInfo.getComId(), msgId);
		String content = vo.getContent().replaceAll("<[.[^>]]*>","");
		vo.setContent(StringUtil.cutString(content,140,null));
		map.put("msgShare", vo);
		map.put("status", "y");
		return map;
	}
	/**
	 * 弹出页面编辑分享信息
	 * @param msgShare
	 * @return
	 */
	@RequestMapping("/fullScreenEditor")
	public ModelAndView fullScreenEditor(MsgShare msgShare){
		ModelAndView view = new ModelAndView("/msgShare/popup");
		return view;
	}
	/**
	 * 分享信息查看
	 * @param id
	 * @param type
	 * @return
	 */
	@RequestMapping("/msgShareViewPage")
	public ModelAndView msgShareView(HttpServletRequest request,Integer id,String type,String sid,String scroll){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView();
		if("1".equals(type)){//普通分享
			
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), type, id);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				viewRecordService.addViewRecord(userInfo,viewRecord);
			}
			//浏览的人员
			List<ViewRecord> listViewRecord = viewRecordService.listViewRecord(userInfo,type,id);
			view.addObject("listViewRecord", listViewRecord);
			
			MsgShare msgShare = msgShareService.viewMsgShareById(userInfo.getId(),userInfo.getComId(),id,type);
			view.setViewName("/msgShare/msgShareView");
			view.addObject("msg", msgShare);
			//信息附件
			List<MsgShareTalkUpfile> listMsgShareTalkUpfile = msgShareService.listPagedMsgShareFiles(userInfo.getComId(),id);
			view.addObject("listMsgShareTalkUpfile",listMsgShareTalkUpfile);
			view.addObject("userInfo",userInfo);
			
			//查看分享信息，删除提醒
			todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), "1",0);
		}
		view.addObject("userInfo", userInfo);
		view.addObject("scroll", scroll);
		return view;
	}
	/**
	 * AJAX 获取滚动条滚动刷新数据
	 * @param pageSize 分页大小
	 * @param minId 当前最小的ID
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/nextPageSizeMsgs")
	public List<MsgShare> nextPageSizeMsgs(Integer pageSize,MsgShare msgShare,String[] modTypes,Integer pageNum) throws IOException, ParseException{
		UserInfo userInfo = this.getSessionUser();
		msgShare.setComId(userInfo.getComId());

		//是否查询下属
		String creatorType = msgShare.getCreatorType();
		//若是没有下属
	    if(userInfo.getCountSub()<=0 && null!=creatorType && "1".equals(creatorType)){
	    	msgShare.setCreatorType(null);
	    }

	    //数组集合化
  		List<String>  modPage = new  ArrayList<String>();
  		List<String> modList = null;
  		if(null!=modTypes && modTypes.length>0){
  			modList = Arrays.asList(modTypes);
  			modPage.addAll(modList);
			if(modPage.indexOf("100")>=0){
				modPage.remove("100");
				modPage.add("1");
			}

			Collections.sort(modPage);
  		}

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		List<MsgShare> list = msgShareService.listMsgShare(userInfo.getComId(),userInfo.getId(),pageSize,msgShare,modPage,null);
		//遍历后替换表情
		List<MsgShare> msgs = new ArrayList<MsgShare>();
		for (MsgShare obj : list) {
			obj.setContent(StringUtil.toHtml(StringUtil.cutString(obj.getContent(),140,null)));
			msgs.add(obj);
		}
		return msgs;
	}

	/**
	 * 留言显示更多
	 * @param pageSize
	 * @param minId
	 * @param msgShare
	 * @param modTypes
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/nextPageSizeMsgTalks")
	public List<MsgShareTalk> nextPageSizeMsgTalks(Integer pageSize,Integer minId,MsgShareTalk msgShareTalk,String modTypes) throws IOException, ParseException{
		UserInfo userInfo = this.getSessionUser();
		List<MsgShareTalk> msgShareTalks = msgShareService.nextPageSizeMsgTalks(msgShareTalk.getMsgId(),userInfo.getComId(),pageSize,minId,"pc");
		return msgShareTalks;
	}


	/**
	 * 删除分享信息信息
	 * @param id 分享信息主键
	 * @param type 分享信息类型
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delShareMsg")
	public Map<String, String> delShareMsg(Integer id,String type) throws Exception{

		Map<String, String> map = new HashMap<String, String>();
		UserInfo userInfo = this.getSessionUser();
		if(type.equals("1")){//是信息分享
			//删除
			msgShareService.delShareMsg(id,userInfo);

			map.put("status","y");
		}
		return map;
	}
	/**
	 * 跳转分享信息讨论页面
	 * @param msgShareTalk
	 * @return
	 */
	@RequestMapping(value="/msgShareTalkPage")
	public ModelAndView msgShareTalkPage(MsgShareTalk msgShareTalk){
		ModelAndView view = new ModelAndView("/msgShare/msgShareTalk");
		view.addObject("msgShareTalk",msgShareTalk);
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		//信息讨论
		List<MsgShareTalk> msgShareTalks = msgShareService.
				listPagedMsgShareTalk(msgShareTalk.getMsgId(),userInfo.getComId(),"pc");
		Integer minId = null;
		if(msgShareTalks != null && msgShareTalks.size() > 0) {
			minId = msgShareTalks.get(msgShareTalks.size()-1).getId();
		}
		view.addObject("list",msgShareTalks);
		view.addObject("minId",minId);
		//查看信息讨论，删除提醒
		todayWorksService.updateTodoWorkRead(msgShareTalk.getMsgId(), userInfo.getComId(), userInfo.getId(), "1",0);
		
		return view;
	}
	/**
	 * 跳转分享信息日志页面
	 * @param msgId
	 * @return
	 */
	@RequestMapping(value="/msgShareLogPage")
	public ModelAndView msgShareLogPage(Integer msgId){
		//系统当前用户
		UserInfo userInfo = this.getSessionUser();
		//信息讨论日志
		List<MsgShareLog> listMsgShareLog = msgShareService.listPagedMsgShareLog(msgId,userInfo.getComId());
 		ModelAndView view = new ModelAndView("/msgShare/msgShareLog");
 		view.addObject("listMsgShareLog", listMsgShareLog);
 		//查看信息日志，删除提醒
 		todayWorksService.updateTodoWorkRead(msgId, userInfo.getComId(), userInfo.getId(), "1",0);
		return view;
	}
	/**
	 * 跳转分享信息文档页面
	 * @param msgShareTalkUpfile
	 * @return
	 */
	@RequestMapping(value="/msgShareUpfilePage")
	public ModelAndView msgShareUpfilePage(Integer msgId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/msgShare/msgShareUpfile");
		//信息附件
		List<MsgShareTalkUpfile> listMsgShareTalkUpfile = msgShareService.listPagedMsgShareFiles(userInfo.getComId(),msgId);
		view.addObject("listMsgShareTalkUpfile",listMsgShareTalkUpfile);
		view.addObject("userInfo",userInfo);
		
		//查看信息附件，删除提醒
		todayWorksService.updateTodoWorkRead(msgId, userInfo.getComId(), userInfo.getId(), "1",0);
		
		return view;
	}
	
	/**
	 * 分享讨论回复
	 * @param msgTalk
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/addMsgShareTalk",method = RequestMethod.POST)
	public Map<String, Object> addMsgShareTalk(MsgShareTalk msgTalk) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		msgTalk.setComId(sessionUser.getComId());
		msgTalk.setSpeaker(sessionUser.getId());
		//信息讨论回复
		Integer id = msgShareService.addMsgShareTalk(msgTalk,sessionUser);
		//模块日志添加
		if(-1==msgTalk.getParentId()){
			msgShareService.addMagShareRepLog(sessionUser.getComId(), msgTalk.getMsgId(), sessionUser.getId(), sessionUser.getUserName(), "参与信息讨论");
		}else{
			msgShareService.addMagShareRepLog(sessionUser.getComId(), msgTalk.getMsgId(), sessionUser.getId(), sessionUser.getUserName(), "回复信息讨论");
		}
		map.put("status", "y");
		map.put("id", id);
		//用于返回页面拼接代码
		MsgShareTalk msgShareTalk = msgShareService.getMagshareTalk(id, sessionUser.getComId());
		map.put("msgTalk", msgShareTalk);
		map.put("sessionUser", this.getSessionUser());
		return map;
	}
	
	/**
	 * 删除信息讨论回复
	 * @param msgShareTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/delMsgShareTalk",method = RequestMethod.POST)
	public Map<String, Object> delMsgShareTalk(MsgShareTalk msgShareTalk,String delChildNode) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		msgShareTalk.setComId(sessionUser.getComId());
		//要删除的回复所有子节点和自己
		List<Integer> childIds = msgShareService.delMsgShareTalk(msgShareTalk,delChildNode,sessionUser);
		map.put("status", "y");
		map.put("childIds", childIds);
		//模块日志添加
		msgShareService.addMagShareRepLog(sessionUser.getComId(), msgShareTalk.getMsgId(), sessionUser.getId(), sessionUser.getUserName(), "删除信息讨论");
		
		return map;
	}
	
	
	
	/**************************今日提醒***********************************************/
	
	/**
	 * 获取消息提醒分页列表
	 * @param request
	 * @param todayWorks消息提醒的属性条件
	 * @param modTypes模块数组
	 * @return
	 */
	@RequestMapping("/listPagedMsgNoRead")
	public ModelAndView listPagedMsgNoRead(HttpServletRequest request,TodayWorks todayWorks){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		ModelAndView view = new ModelAndView("/msgShare/listPagedMsgNotice");
		UserInfo userInfo = this.getSessionUser();
		//模块数组化
		List<String> modList = null;
		String busType = todayWorks.getBusType();
		if(!StringUtil.isBlank(busType)){
			modList = new ArrayList<String>();
			modList.add(busType);
		}
		List<TodayWorks> listTodayWork = todayWorksService.listPagedMsgNoRead(todayWorks,userInfo.getComId(),userInfo.getId(),modList);
		List<TodayWorks> list = new ArrayList<TodayWorks>();
		if(null!=listTodayWork && listTodayWork.size()>0){
			for (TodayWorks obj : listTodayWork) {
				String busTypeName = obj.getBusTypeName();
				if(CommonUtil.isNull(busTypeName)){
					obj.setBusTypeName(null);
					list.add(obj);
					continue;
				}
				busTypeName = busTypeName.replaceAll("\\n", "").replaceAll("\\t", "");
				obj.setBusTypeName(busTypeName);
				list.add(obj);
			}
		}
		
		
		view.addObject("userInfo",userInfo);
		view.addObject("list",list);
		//判断页面是否为消息中心，若是则不显示提醒数
		view.addObject("noRead","no");
		view.addObject("showModNoRead","yes");
		view.addObject("modList",modList);
		//负责的会议室
		Integer countRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
		view.addObject("countRoom",countRoom);
		return view;
	}
	/**
	 * 获取消息提醒分页列表
	 * @param request
	 * @param todayWorks 消息提醒的属性条件
	 * @param modTypes 模块数组
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListMsgNoRead")
	public Map<String,Object> ajaxListMsgNoRead(TodayWorks todayWorks,Integer pageNum,Integer pageSize){
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
		//一次加载行数
		PaginationContext.setPageSize(pageSize);
		//列表数据起始索引位置
		PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
		
		//模块数组化
		List<String> modList = null;
		String busType = todayWorks.getBusType();
		if(!StringUtil.isBlank(busType)){
			modList = new ArrayList<String>();
			modList.add(busType);
		}
		List<TodayWorks> listTodayWork = todayWorksService.listPagedMsgNoRead(todayWorks,userInfo.getComId(),userInfo.getId(),modList);
		List<TodayWorks> list = new ArrayList<TodayWorks>();
		if(null!=listTodayWork && listTodayWork.size()>0){
			for (TodayWorks obj : listTodayWork) {
				String busTypeName = obj.getBusTypeName();
				busTypeName = busTypeName.replaceAll("\\n", "").replaceAll("\\t", "");
				
				busTypeName = StringUtil.cutString(busTypeName, 82,null);
				busTypeName = StringUtil.toHtml(busTypeName);
				obj.setBusTypeName(busTypeName);
				
				String content = obj.getContent();
				content = StringUtil.cutString(content, 302,null);
				content = StringUtil.toHtml(content);
				obj.setContent(content);
				list.add(obj);
			}
		}
		
		map.put("list", list);
		
		map.put("status", "y");
		return map;
	}
	/**
	 * 异步取得待办事项的分页数据
	 * @param todayWorks 条件
	 * @param todoIds 页面展示的待办事项主键
	 * @param pageNum 当前页码
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/toDoJobs/ajaxJobsCenter")
	public Map<String,Object> ajaxJobsCenter(TodayWorks todayWorks,Integer pageNum,Integer pageSize){
		Map<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
		//一次加载行数
		PaginationContext.setPageSize(pageSize);
		//列表数据起始索引位置
		PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
		
		//模块数组化
		List<String> modList = null;
		String busType = todayWorks.getBusType();
		if(!StringUtil.isBlank(busType)){
			modList = new ArrayList<String>();
			modList.add(busType);
		}
		//查询待办事项		
		List<TodayWorks> list = todayWorksService.listPagedMsgTodo(todayWorks,userInfo.getComId(),userInfo.getId(),modList);
		map.put("list",list);
		//判断页面是否为消息中心，若是则不显示提醒数
		map.put("status", "y");
		map.put("todoNum", PaginationContext.getTotalCount());
		
		return map;
	}
	/**
	 * 获取代办事项分页列表
	 * @param request
	 * @param todayWorks
	 * @param modTypes
	 * @return
	 */
	@RequestMapping("/toDoJobs/jobsCenter")
	public ModelAndView listJobTodo(HttpServletRequest request,TodayWorks todayWorks,String[] modTypes){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		ModelAndView view = new ModelAndView("/msgShare/toDoJobs/jobsCenter");
		UserInfo userInfo = this.getSessionUser();
		
		//模块数组化
		List<String> modList = null;
		if(null!=modTypes && modTypes.length>0){
			Arrays.sort(modTypes);
			modList = Arrays.asList(modTypes);
		}
		//查询待办事项		
		List<TodayWorks> list = todayWorksService.listPagedMsgTodo(todayWorks,userInfo.getComId(),userInfo.getId(),modList);
		view.addObject("userInfo",userInfo);
		view.addObject("list",list);
		//判断页面是否为消息中心，若是则不显示提醒数
		view.addObject("todo","no");
		view.addObject("modList",modList);
		
		Integer countRoom = meetingRoomService.countManageRoom(userInfo.getComId(),userInfo.getId());
		view.addObject("countRoom",countRoom);
		
		List<MsgNoRead> modTodoList = todayWorksService.countToDoByType(userInfo.getComId(),userInfo.getId());
		if(null!=modTodoList && !modTodoList.isEmpty()){
			Gson gson = new Gson();
			view.addObject("modTodoList",gson.toJson(modTodoList));
		}else{
			view.addObject("modTodoList",null);
		}
		
		return view;
	}
	/**
	 * 获取消息提醒数目
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/countTodo")
	public Map<String, Object> countTodo(){
		Map<String, Object>  map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			return null;
		}
		
		//各个模块的未读消息
		List<MsgNoRead> listNoReadNum = todayWorksService.countNoReadByType(userInfo.getComId(),userInfo.getId());
		map.put("listNoReadNum", listNoReadNum);
		
		map.put("status", "y");
		map.put("userInfo", userInfo);
		
		String nowdatetime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		map.put("serverTime", nowdatetime);
		return map;
	}
	/**
	 * 未读消息的数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/showMsgNoReadNum")
	public Map<String, Object> showMsgNoReadNum() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (userInfo != null) {
			//总提醒数
			Integer num = todayWorksService.countNoRead(userInfo.getComId(),userInfo.getId());
			map.put("ifLogin", true);
			map.put("num", num);
		} else {
			map.put("ifLogin", false);
		}
		return map;
	}
	/**
	 * 查询一条未读消息 并显示消息提示
	 * @return
	 */
	@RequestMapping("/showMsgNoReadPage")
	public ModelAndView showMsgNoReadPage() {
		ModelAndView mav = new ModelAndView("/msgShare/showMsgNoRead");
		UserInfo userInfo = this.getSessionUser();
		if (userInfo != null) {
			//取得未读信息
			TodayWorks todayWorks = todayWorksService.getMsgNoRead(userInfo);
			
			//业务类型
			String busType = todayWorks.getBusType();
			//业务主键
			Integer busId = todayWorks.getBusId();
			if("0".equals(busType)|| ConstantInterface.TYPE_FILE.equals(busType)//普通闹铃，文件,
					||ConstantInterface.TYPE_APPLY.equals(busType)){//人员申请
				
				mav.addObject("needAns", "n");//不需要回复
			}else if("1".equals(busType)||ConstantInterface.TYPE_WEEK.equals(busType)||ConstantInterface.TYPE_DAILY.equals(busType)){//普通分享,周报,分享
				
				mav.addObject("needAns", "y");//需要回复
			}else if(ConstantInterface.TYPE_QUES.equals(busType)){//问答
				
				mav.addObject("needAns", "n");//不需要回复
			}else if(ConstantInterface.TYPE_CRM.equals(busType)){//客户
				//客户
				Customer crm = crmService.getCrmById(busId);
				if(null!=crm && crm.getDelState()==0){//客户存在且没有删除
					//反馈类型
					List<FeedBackType> listFeedBackType = crmService.listFeedBackType(userInfo.getComId());
					if(null!=listFeedBackType && listFeedBackType.size()>0){
						mav.addObject("needAns", "y");//需要回复
						mav.addObject("listFeedBackType", listFeedBackType);
					}else{
						mav.addObject("needAns", "y");//不需要回复
					}
				}else{
					mav.addObject("needAns", "n");//不需要回复
				}
			}else if(ConstantInterface.TYPE_VOTE.equals(busType)){//投票
				//投票内容和投票截止时间
				Vote vote = voteService.getVoteObj(busId, userInfo.getComId());
				if(null!=vote && vote.getDelState()==0){//投票存在,且没有被删除
					//截止时间
					long finishTime = DateTimeUtil.parseDate(vote.getFinishTime()+":00:00",DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
					//当前时间
					long nowTine = (new Date()).getTime();
					if(nowTine>finishTime){//过期了
						mav.addObject("needAns", "n");//不需要回复
					}else{
						mav.addObject("needAns", "y");//需要回复
					}
				}else{//投票不存在，或是已被删除
					mav.addObject("needAns", "n");//不需要回复
				}
				
			}else if(ConstantInterface.TYPE_TASK.equals(busType)){//任务
				//任务对象查询
				Task task = taskService.getTaskById(busId);
				if(null!=task && task.getDelState()==0 && task.getState()==1){//任务正在执行
					mav.addObject("needAns", "y");//需要回复
				}else{
					mav.addObject("needAns", "n");//不需要回复
				}
			}else if(ConstantInterface.TYPE_MEETING.equals(busType)){//会议
				mav.addObject("needAns", "n");//不需要回复
			}else if(ConstantInterface.TYPE_MEETROOM.equals(busType)){//会议
				mav.addObject("needAns", "n");//不需要回复
			}else if(ConstantInterface.TYPE_PRODUCT.equals(busType)){//产品
				mav.addObject("needAns", "n");//不需要回复
			}else if(ConstantInterface.TYPE_ITEM.equals(busType)){//项目
				//任务对象查询
				Item item = itemService.getItemById(busId);
				if(null!=item && item.getDelState()==0 && item.getState()==1){//任务正在执行
					mav.addObject("needAns", "y");//需要回复
				}else{
					mav.addObject("needAns", "n");//不需要回复
				}
			}
			if(todayWorks.getIsClock()==1 || todayWorks.getRoomId()>0){//所有闹铃都不需要回复
				mav.addObject("needAns", "n");//不需要回复
			}
			//总提醒数
			Integer num = todayWorksService.countNoRead(userInfo.getComId(),userInfo.getId());
			mav.addObject("todayWorks", todayWorks);
			mav.addObject("num", num);
		}
		return mav;
	}
	/**
	 * 当前消息设置为已读 并查询下一条未读消息
	 * @param id 消息主键id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/doReadOne")
	public Map<String, Object> doReadOne(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (userInfo != null) {
			//标识已读
			todayWorksService.updateReadTodayWork(id,userInfo);
			//取得未读信息
			TodayWorks todayWorks = todayWorksService.getMsgNoRead(userInfo);
			if(null!=todayWorks){//没有待办事项了
				//业务类型
				String busType = todayWorks.getBusType();
				//业务主键
				Integer busId = todayWorks.getBusId();
				if("0".equals(busType)|| ConstantInterface.TYPE_FILE.equals(busType)//普通闹铃，文件,
						||ConstantInterface.TYPE_APPLY.equals(busType)){//人员申请
					
					map.put("needAns", "n");//不需要回复
				}else if("1".equals(busType)||ConstantInterface.TYPE_WEEK.equals(busType)||ConstantInterface.TYPE_WEEK.equals(busType)){//普通分享,周报,分享
					map.put("needAns", "y");//需要回复
				}else if(ConstantInterface.TYPE_QUES.equals(busType)){//问答
					map.put("needAns", "n");//不需要回复
				}else if(ConstantInterface.TYPE_CRM.equals(busType)){//客户
					//客户
					Customer crm = crmService.getCrmById(busId);
					if(null!=crm && crm.getDelState()==0){//客户存在且没有删除
						//反馈类型
						List<FeedBackType> listFeedBackType = crmService.listFeedBackType(userInfo.getComId());
						if(null!=listFeedBackType && listFeedBackType.size()>0){
							map.put("needAns", "y");//需要回复
							map.put("listFeedBackType", listFeedBackType);
						}else{
							map.put("needAns", "y");//不需要回复
						}
					}else{
						map.put("needAns", "n");//不需要回复
					}
				}else if(ConstantInterface.TYPE_VOTE.equals(busType)){//投票
					//投票内容和投票截止时间
					Vote vote = voteService.getVoteObj(busId, userInfo.getComId());
					if(null!=vote && vote.getDelState()==0){//投票存在,且没有被删除
						//截止时间
						long finishTime = DateTimeUtil.parseDate(vote.getFinishTime()+":00:00",DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
						//当前时间
						long nowTine = (new Date()).getTime();
						if(nowTine>finishTime){//过期了
							map.put("needAns", "n");//不需要回复
						}else{
							map.put("needAns", "y");//需要回复
						}
					}else{//投票不存在，或是已被删除
						map.put("needAns", "n");//不需要回复
					}
					
				}else if(ConstantInterface.TYPE_TASK.equals(busType)){//任务
					//任务对象查询
					Task task = taskService.getTaskById(busId);
					if(null!=task && task.getDelState()==0 && task.getState()==1){//任务正在执行
						map.put("needAns", "y");//需要回复
					}else{
						map.put("needAns", "n");//不需要回复
					}
				}else if(ConstantInterface.TYPE_ITEM.equals(busType)){//项目
					//任务对象查询
					Item item = itemService.getItemById(busId);
					if(null!=item && item.getDelState()==0 && item.getState()==1){//任务正在执行
						map.put("needAns", "y");//需要回复
					}else{
						map.put("needAns", "n");//不需要回复
					}
				}
				
				if(todayWorks.getIsClock()==1 || todayWorks.getRoomId()>0){//所有闹铃都不需要回复
					map.put("needAns", "n");//不需要回复
				}
			}else{
				map.put("needAns", "n");//不需要回复
			}
			//总提醒数
			Integer num = todayWorksService.countNoRead(userInfo.getComId(),userInfo.getId());
			map.put("work", todayWorks);
			map.put("num", num);
			map.put("ifLogin", true);
			map.put("num", num);
		} else {
			map.put("ifLogin", false);
		}
		return map;
	}
	/**
	 * 回复消息
	 * @param content 内容
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @param feedBackTypeId 业务主键
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/answerMsg")
	public Map<String, Object> answerMsg(String content,String busType,Integer busId,Integer feedBackTypeId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (userInfo != null) {
			if(busType.equals(ConstantInterface.TYPE_TASK)){//任务留言
				//任务留言
				TaskTalk taskTalk = new  TaskTalk();
				//企业号
				taskTalk.setComId(userInfo.getComId());
				//任务主键
				taskTalk.setTaskId(busId);
				//留言父节点
				taskTalk.setParentId(-1);
				//留言人
				taskTalk.setSpeaker(userInfo.getId());
				//留言内容
				taskTalk.setContent(content);
				//添加留言
				taskService.addTaskTalk(taskTalk, userInfo);
			}else if(busType.equals(ConstantInterface.TYPE_ITEM)){//项目留言
				//项目留言
				ItemTalk itemTalk = new ItemTalk();
				//企业号
				itemTalk.setComId(userInfo.getComId());
				//项目主键
				itemTalk.setItemId(busId);
				//留言父节点
				itemTalk.setParentId(-1);
				//留言人
				itemTalk.setUserId(userInfo.getId());
				//留言内容
				itemTalk.setContent(content);
				itemService.addItemTalk(itemTalk, userInfo);
			}else if(busType.equals(ConstantInterface.TYPE_PRODUCT)){
				
				BaseTalk proTalk = new BaseTalk();
				// 项目留言
				proTalk.setComId(userInfo.getComId());
				proTalk.setSpeaker(userInfo.getId());
				proTalk.setBusId(busId);
				proTalk.setContent(content);
				proTalk.setParentId(-1);
				
				productTalkService.addTalk(userInfo, proTalk);
				
			}else if(busType.equals(ConstantInterface.TYPE_CRM)){//客户
				FeedBackInfo feedBackInfo = new FeedBackInfo();
				//企业号
				feedBackInfo.setComId(userInfo.getComId());
				//客户主键
				feedBackInfo.setCustomerId(busId);
				//反馈类型
				feedBackInfo.setFeedBackTypeId(feedBackTypeId);
				//留言父节点
				feedBackInfo.setParentId(-1);
				//留言人
				feedBackInfo.setUserId(userInfo.getId());
				//留言内容
				feedBackInfo.setContent(content);
				crmService.addFeedBackInfo(feedBackInfo, null, userInfo);
			}else if(busType.equals(ConstantInterface.TYPE_QUES)){//问答的回答
				Answer answer = new Answer();
				//企业号
				answer.setComId(userInfo.getComId());
				//问题主键
				answer.setQuesId(busId);
				//回答人
				answer.setUserId(userInfo.getId());
				//回答内容
				answer.setContent(content);
				
				qasService.addAns(answer, userInfo, null);
			}else if(busType.equals(ConstantInterface.TYPE_VOTE)){//投票评论
				VoteTalk voteTalk = new VoteTalk();
				//企业号
				voteTalk.setComId(userInfo.getComId());
				//投票主键
				voteTalk.setVoteId(busId);
				//留言父节点
				voteTalk.setParentId(-1);
				//留言人
				voteTalk.setTalker(userInfo.getId());
				//留言内容
				voteTalk.setContent(content);
				
				voteService.addVoteTalk(voteTalk, userInfo);
			}else if(busType.equals(ConstantInterface.TYPE_WEEK)){//周报评论
				WeekRepTalk weekRepTalk = new WeekRepTalk();
				//企业号
				weekRepTalk.setComId(userInfo.getComId());
				//周报主键
				weekRepTalk.setWeekReportId(busId);
				//留言父节点
				weekRepTalk.setParentId(-1);
				//留言人
				weekRepTalk.setTalker(userInfo.getId());
				//留言内容
				weekRepTalk.setContent(content);
				weekReportService.addWeekRepTalk(weekRepTalk, userInfo);
			}else if(busType.equals(ConstantInterface.TYPE_DAILY)){//分享评论
				DailyTalk dailyTalk = new DailyTalk();
				//企业号
				dailyTalk.setComId(userInfo.getComId());
				//周报主键
				dailyTalk.setDailyId(busId);
				//留言父节点
				dailyTalk.setParentId(-1);
				//留言人
				dailyTalk.setTalker(userInfo.getId());
				//留言内容
				dailyTalk.setContent(content);
				dailyService.addDailyTalk(dailyTalk, userInfo);
			}else if(busType.equals("1")){//信息分享
				MsgShareTalk msgTalk = new MsgShareTalk();
				//企业号
				msgTalk.setComId(userInfo.getComId());
				//分享主键
				msgTalk.setMsgId(busId);
				//留言父节点
				msgTalk.setParentId(-1);
				//留言人
				msgTalk.setSpeaker(userInfo.getId());
				//留言内容
				msgTalk.setContent(content);
				msgShareService.addMsgShareTalk(msgTalk, userInfo);
			}
			map.put("status", "y");
		}else{
			map.put("status", "n");
		}
		return map;
	}
	/**
	 * 半小时内不提示
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/doAfterShow")
	public Map<String, Object> doAfterShow() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			UserInfo user = this.getSessionUser();
			if (user != null) {
				result.put("ifLogin", true);
				String nowdatetime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
				SimpleDateFormat format = null;
				Calendar calendar = Calendar.getInstance();
				format = DateTimeUtil.getDateFormat(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
				calendar.setTime(format.parse(nowdatetime));
				calendar.add(Calendar.MINUTE, 30);
				String nextShowMsg = format.format(calendar.getTime());
				result.put("nextShowMsg", nextShowMsg);
			} else {
				result.put("ifLogin", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 未读消息全部标识
	 * @param todayWorks
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/readAll")
	public Map<String, Object> readAll(TodayWorks todayWorks) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserInfo user = this.getSessionUser();
		if (user != null) {
			todayWorks.setUserId(user.getId());
			todayWorks.setComId(user.getComId());
			//未读消息
			todayWorks.setReadState(0);
			todayWorksService.updateReadAllWorks(todayWorks,null);
			result.put("ifLogin", true);
		} else {
			result.put("ifLogin", false);
		}
		return result;
	}
	/**
	 * 标识已读
	 * @param ids
	 * @param redirectPage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delWorks")
	public ModelAndView delWorks(Integer[] ids,String redirectPage) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		//标识已读
		todayWorksService.updateReadWorks(ids,userInfo);
		this.setNotification(Notification.SUCCESS, "已标识");
		return new ModelAndView("redirect:"+redirectPage);
	}
	/**
	 * 闹铃提示标识已读
	 * @param msgId
	 * @param redirectPage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delClockWorks")
	public ModelAndView delWorks(Integer msgId,String redirectPage) throws Exception {
		UserInfo sessionUser = this.getSessionUser();
		//标识已读
		todayWorksService.updateReadWorks(new Integer[] { msgId },sessionUser);
		this.setNotification(Notification.SUCCESS, "已标识");
		return new ModelAndView("redirect:"+redirectPage);
	}
	/**
	 * 全部标识已读
	 * @param redirectPage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delAllWorks")
	public ModelAndView delAllWorks(TodayWorks todayWorks,String redirectPage) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		todayWorks.setUserId(userInfo.getId());
		todayWorks.setComId(userInfo.getComId());
		//模块数组化
		List<String> modList = null;
		String busType = todayWorks.getBusType();
		if(!StringUtil.isBlank(busType)){
			modList = new ArrayList<String>();
			modList.add(busType);
		}
		//标识已读
		todayWorksService.updateReadAllWorks(todayWorks,modList);
		this.setNotification(Notification.SUCCESS, "已全部标识");
		return new ModelAndView("redirect:"+redirectPage);
	}
	/**
	 * 查看待办事项详情（只有闹铃待办）
	 * @param todayWorks
	 * @param redirectPage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/viewTodoDetailPage")
	public ModelAndView viewTodoDetailPage(TodayWorks todayWorks,String redirectPage) throws Exception {
		
		UserInfo userInfo = this.getSessionUser();
		//业务类型
		String busType = todayWorks.getBusType();
		//业务主键
		Integer busId = todayWorks.getBusId();
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
		
		if(ConstantInterface.TYPE_CRM.equals(busType)){//客户信息
			if(!isForceIn && !crmService.authorCheck(userInfo.getComId(),busId,userInfo.getId())){//没有查看权限
				//删除客户提醒
				todayWorksService.delTodayWorkById(todayWorks.getId());
				
				this.setNotification(Notification.ERROR, "抱歉，你没有查看权限");
				return new ModelAndView("/refreshParent");
			}
			
		}else if(ConstantInterface.TYPE_ITEM.equals(busType)){//项目信息
			if(!isForceIn && !itemService.authorCheck(userInfo.getComId(),busId,userInfo.getId())){//没有查看权限
				//删除项目提醒
				todayWorksService.delTodayWorkById(todayWorks.getId());
				
				this.setNotification(Notification.ERROR, "抱歉，你没有查看权限");
				return new ModelAndView("/refreshParent");
			}
		}else if(ConstantInterface.TYPE_PRODUCT.equals(busType)){
			//产品信息
			if(!isForceIn && !productService.authorCheck(userInfo.getComId(),busId,userInfo.getId())){//没有查看权限
				//删除项目提醒
				todayWorksService.delTodayWorkById(todayWorks.getId());

				this.setNotification(Notification.ERROR, "抱歉，你没有查看权限");
				return new ModelAndView("/refreshParent");
			}
		}else if(ConstantInterface.TYPE_TASK.equals(busType)){//任务信息
			if(!taskService.authorCheck(userInfo,busId,0)){//没有查看权限
				//删除任务提醒
				todayWorksService.delTodayWorkById(todayWorks.getId());
				
				this.setNotification(Notification.ERROR, "抱歉，你没有查看权限");
				return new ModelAndView("/refreshParent");
			}
		}else if(ConstantInterface.TYPE_WEEK.equals(busType)){//周报信息
			//有查看权限或是监督人员
			if(!isForceIn && !weekReportService.authorCheck(userInfo.getComId(),busId,userInfo.getId(),busId)){
				//删除周报提醒
				todayWorksService.delTodayWorkById(todayWorks.getId());
				
				this.setNotification(Notification.ERROR, "抱歉，你没有查看权限");
				return new ModelAndView("/refreshParent");
			}
		}else if(ConstantInterface.TYPE_DAILY.equals(busType)){//分享信息
			//有查看权限或是监督人员
			if(!isForceIn && !dailyService.authorCheck(userInfo.getComId(),busId,userInfo.getId(),busId)){
				//删除周报提醒
				todayWorksService.delTodayWorkById(todayWorks.getId());

				this.setNotification(Notification.ERROR, "抱歉，你没有查看权限");
				return new ModelAndView("/refreshParent");
			}
		}else if(ConstantInterface.TYPE_APPLY.equals(busType)){//申请信息
			if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
				//删除申请提醒
				todayWorksService.delTodayWorkById(todayWorks.getId());
				
				this.setNotification(Notification.ERROR, "抱歉，你没有查看权限");
				return new ModelAndView("/refreshParent");
			}
		}
		//通过待办事项的主键取得待办事项详情
		TodayWorks todo = todayWorksService.getMsgTodoById(todayWorks.getId(),todayWorks.getBusType(),
				todayWorks.getBusId(),userInfo);
		if(null==todo){//已经查看了
			return new ModelAndView("/refreshParent");
		}
		
		ModelAndView mav = new ModelAndView("/msgShare/viewTodoDetail");
		mav.addObject("todo", todo);
		mav.addObject("noRead", "no");
		//闹铃主键
		Integer clockId = todayWorks.getClockId();
		if(null!=clockId && clockId>0){//是闹铃事件
			Clock clock = clockService.getClockById(todayWorks.getClockId(), userInfo);
			mav.addObject("clock", clock);
			
			todayWorks.setReadState(1);
			todayWorksService.updateTodayObj(todayWorks);
		}
		
		mav.addObject("userInfo", userInfo);
		
		mav.addObject("redirectPage", redirectPage);
		
		if(todo.getBusSpec()==1){//只有待办事项才进行操作
			//通过了验证，并且有权限查看待办事项
			if((ConstantInterface.TYPE_TASK.equals(busType)
					|| ConstantInterface.TYPE_ITEM.equals(busType)
					|| ConstantInterface.TYPE_PRODUCT.equals(busType)
					|| ConstantInterface.TYPE_CRM.equals(busType))
					&& null!=clockId && clockId>0 ){
				todayWorks.setBusSpec(0);
				todayWorks.setReadState(1);
				todayWorksService.updateTodayObj(todayWorks);
			}else if(ConstantInterface.TYPE_TASK.equals(busType) //任务 需要办结才能消除
					||ConstantInterface.TYPE_WEEK.equals(busType)//周报 需要查看才能消除
					||ConstantInterface.TYPE_DAILY.equals(busType) //分享 需要查看才能消除
					||ConstantInterface.TYPE_APPLY.equals(busType) ){//申请 需要处理才能消除
				todayWorks.setReadState(1);
				todayWorksService.updateTodayObj(todayWorks);
			}else{//其他事件，只要点击就会消除
				todayWorks.setBusSpec(0);
				todayWorks.setReadState(1);
				todayWorksService.updateTodayObj(todayWorks);
			}
		}
		
		return mav;
	}
	/**
	 * 分享信息权限验证
	 * @param msgId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/authorCheck")
	public MsgShare authorCheck(Integer msgId){
		UserInfo userInfo = this.getSessionUser();
		MsgShare shareVo = new MsgShare();
		if(null==userInfo){
			shareVo.setSucc(false);
			shareVo.setPromptMsg("服务已关闭，请稍后重新操作！");
			return shareVo;
		}
		if(msgShareService.authorCheck(userInfo.getComId(),msgId,userInfo.getId())){
			shareVo.setSucc(true);
		}else{
			shareVo.setSucc(false);
			shareVo.setPromptMsg("抱歉，你没有查看权限");
		}
		return shareVo;
	}
	
	/**
	 * 删除分享附件
	 * @param msgId
	 * @param msgUpFileId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delMsgUpfile")
	public Map<String, Object> delMsgUpfile(Integer msgId,Integer msgUpFileId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		msgShareService.delMsgUpfile(msgUpFileId,userInfo,msgId);
		map.put("status", "y");
		return map;
	}
	
}
