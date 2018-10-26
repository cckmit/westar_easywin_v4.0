package com.westar.core.web.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.westar.base.model.ChatMsg;
import com.westar.base.model.ChatRoom;
import com.westar.base.model.ChatUser;
import com.westar.base.model.ChatsMsg;
import com.westar.base.model.ChatsNoRead;
import com.westar.base.model.Department;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.ChatService;
import com.westar.core.service.DepartmentService;
import com.westar.core.service.SelfGroupService;
import com.westar.core.service.UserInfoService;
import com.westar.core.web.PaginationContext;

@Controller
@RequestMapping("/chat")
public class ChatController extends BaseController{

	@Autowired
	ChatService chatService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	DepartmentService depService;
	
	@Autowired
	SelfGroupService selfGroupService;
	
	@RequestMapping(value="/toChat")
	public ModelAndView toChat(ChatRoom chatRoom){
		UserInfo userInfo = this.getSessionUser();
		//聊天室主键
		Integer id = chatRoom.getId();
		//业务类型
		String busType = chatRoom.getBusType();
		//业务主键
		Integer busId = chatRoom.getBusId();
		//是否为群聊
		String chatType = chatRoom.getChatType();
		//聊天成员
		String chater = chatRoom.getChater();
		if(null!=id){//已有聊天室主键
			chatRoom = chatService.getChatRoomById(id);
		}else{
			//查询聊天室是否已存在
			ChatRoom chatRoomS = chatService.getChatRoomByType(userInfo.getComId(),busType,busId,chatType,chater);
			if(null!=chatRoomS){//聊天室已存在
				id = chatRoomS.getId();
				
				chatRoom.setId(id);
			}else{//创建聊天室
				//企业号
				chatRoom.setComId(userInfo.getComId());
				//创建人员
				chatRoom.setCreater(userInfo.getId());
				
				id = chatService.addChatRoom(chatRoom);
				chatRoom.setId(id);
				
				//确定不是群聊
				if(null!=chatType && chatType.equals("0")){
					String[] userIdStr = chater.split(",");
					for (String userId : userIdStr) {
						//聊天成员
						ChatUser chatUser = new ChatUser();
						//企业号
						chatUser.setComId(userInfo.getComId());
						//房间号
						chatUser.setRoomId(id);
						//成员主键
						chatUser.setUserId(Integer.parseInt(userId));
						//设置未读消息数
						chatUser.setNoReadNum(0);
						//添加聊天成员
						chatService.addChatUser(chatUser);
					}
				}
			}
		}
		ModelAndView mav = new ModelAndView("/chat/forChat");
		mav.addObject("chatRoom",chatRoom);
		mav.addObject("userInfo",userInfo);
		
		//聊天室成员
		List<ChatUser> userList = chatService.listChatUser(userInfo.getComId(), id);
		
		JSONObject result = new JSONObject();
		Set<String> users = new HashSet<String>();
		if(null!=userList){
			for (ChatUser userInfoObj : userList) {
				JSONObject userObj = new JSONObject();
    			userObj.put("id", userInfoObj.getUserId());
    			userObj.put("name",userInfoObj.getUserName());
    			userObj.put("online",0);
    			
    			users.add(userObj.toString());
			}
		}
		result.put("list", users);
		mav.addObject("userList",result.toString());
		
		return mav;
	}
	/**
	 * 列出业务模块的聊天室
	 * @param chatRoom
	 * @return
	 */
	@RequestMapping(value="/listBusChat")
	public ModelAndView listBusChat(ChatRoom chatRoom){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/chat/listBusChat");
		
		List<ChatRoom> list = chatService.listBusChat(userInfo,chatRoom);
		
		mav.addObject("list",list);
		mav.addObject("userInfo",userInfo);
		return mav;
	}
	
	/**
	 * 取得聊天记录
	 * @param roomId
	 * @param minId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delTodayWork")
	public Map<String,Object> delTodayWork(Integer roomId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "用户已断开连接");
		}else{
			//删除消息提醒
			chatService.delTodayWork(userInfo.getComId(), roomId, userInfo.getId());
			map.put("status", "y");
		}
		return map;
	}
	/**
	 * 取得聊天记录
	 * @param roomId
	 * @param minId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxListMsg")
	public Map<String,Object> ajaxListMsg(Integer roomId,Integer minId,Integer busId,String busType,String chatType){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "用户已断开连接");
		}else{
			List<ChatMsg> list = chatService.listChatMsg(userInfo,roomId,minId,busId,busType,chatType,15);
			map.put("status", "y");
			map.put("list", list);
			
			Integer newMinId = -1;
			Integer count = 0;
			map.put("newMinId", newMinId);
			map.put("count", count);
			if(null!=list && list.size()>0){
				newMinId = list.get(list.size()-1).getId();
				//记录没有加载数
				count = chatService.countLeft(userInfo,roomId,newMinId,busId,busType,chatType);
				map.put("newMinId", newMinId);
				map.put("count", count);
			}
			
		}
		return map;
	}
	
	
	/*******************人员选择****************************/
	/**
	 * 转向到人员多选页面
	 * @return
	 */
	@RequestMapping("/userMorePage")
	public ModelAndView userMorePage() {
		return new ModelAndView("/chat/userMoreSelect/userMore");
	}
	/**
	 * 人员多选机构树
	 * @return
	 */
	@RequestMapping("/userMoreSelect/orgTreePage")
	public ModelAndView userOneSelect_orgTreePage() {
		return new ModelAndView("/chat/userMoreSelect/orgTree").addObject("sessionUser", this.getSessionUser());
	}
	
	/**
	 * 人员多选 显示人员表
	 * @return
	 */
	@RequestMapping("/userMoreSelect/userTable")
	public ModelAndView userMoreSelect_userTable(UserInfo userInfo) {
		UserInfo user = this.getSessionUser();
		userInfo.setComId(user.getComId());
		userInfo.setEnabled(ConstantInterface.SYS_ENABLED_YES);
		List<UserInfo> list = userInfoService.listUser(userInfo);
		return new ModelAndView("/chat/userMoreSelect/userTable", "list", list);
	}
	/**
	 * 根据分组查询人员 人员多选
	 * @param groupId 为空则标需要最近使用的人员
	 * @return
	 */
	@RequestMapping("/userMoreSelect/group/userTable")
	public ModelAndView userMoreSelect_group_userTable(Integer groupId,String onlySubState) {
		ModelAndView mav = new ModelAndView("/chat/userMoreSelect/userTable");
		UserInfo userInfo = this.getSessionUser();
		List<UserInfo> list = new ArrayList<UserInfo>();
		if(null!=groupId){
			//列出分组的成员
			list =  selfGroupService.listGroupUser(groupId, userInfo,onlySubState);
		}else{
			//列出常用的人员
			list = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),10);
			mav.addObject("ininState", "ininState");
		}
		mav.addObject("list", list);
		return mav;
	}
	
	/***********************************************************************************************/
	
	/**
	 * 取得聊天成员
	 */
	@ResponseBody
	@RequestMapping(value="/getChatUserList")
	public String getChatUserList(Integer comId,String sid,Integer userId){
		// 触发连接事件，在连接池中添加连接
 		JSONObject result = new JSONObject();
 		
 		result.put("code", 0);
 		result.put("msg", "");
 		
 		Department dep = new Department();
		dep.setComId(comId);
		dep.setParentId(-1);
		
		Gson gson = new Gson();
		
 		//查询所有部门
		List<Department> listDep = depService.listTreeOrganization(dep);
		if(null!=listDep && listDep.size()>0){
			JSONObject dataJson = new JSONObject();
			List<String> friendList = new ArrayList<String>();
			for (Department department : listDep) {
				JSONObject depJson = new JSONObject();
				depJson.put("groupname", department.getDepName());
				depJson.put("id", department.getId());
				
				Integer depId = department.getId();
				//设置查询条件
				UserInfo user = new UserInfo();
				user.setDepId(depId);
				user.setComId(dep.getComId());
				
				//查询部门成员
				List<UserInfo> listDepUser = userInfoService.listUserOfDep(user);
				
				List<String> userList = new ArrayList<String>();
				for (UserInfo depUser : listDepUser) {
					JSONObject userJson = new JSONObject();
					userJson.put("username", depUser.getUserName());
					userJson.put("id", depUser.getId());
					String avatar = "";
					if(null==depUser.getSmImgUuid() || "".equals(depUser.getSmImgUuid())){
						avatar = "/static/headImg/2"+((null==depUser.getGender() || "".equals(depUser.getGender()))?2:depUser.getGender())+".jpg";
					}else{
						avatar ="/downLoad/down/"+depUser.getSmImgUuid()+"/"+depUser.getSmImgName();
					}
					userJson.put("avatar", avatar);
					userJson.put("sign", depUser.getSelfIntr());
					if(depUser.getId().equals(userId)){
						dataJson.put("mine", userJson.toString());
						continue;
					}
					userList.add(userJson.toString());
				}
				
				depJson.put("list", gson.toJson(userList));
				
				friendList.add(depJson.toString());
			}
			dataJson.put("friend", gson.toJson(friendList));
			
			result.put("data", dataJson.toString());
		}
		
		return result.toString();
		
	}
	/**
	 * 取得聊天历史(所有的)
	 * @param type 聊天类型
	 * @param sid session标识
	 * @param toUser 消息接收方
	 * @param fromUser 消息发送方
	 * @param comId 企业号
	 * @param content 模糊查询
	 * @return
	 */
	@RequestMapping(value="/listPagedChatHistory")
	public ModelAndView listPagedChatHistory(String type,String sid,Integer toUser,Integer fromUser,
			Integer comId,ChatsMsg chatsMsg){
		
		//一次加载行数
		PaginationContext.setPageSize(20);
		ModelAndView mav = new ModelAndView("/chat/listChatHistory");
		//取得聊天室主键(需要优化)
		Integer roomId = chatService.getChatsRoomId(comId,fromUser,toUser,type);
		if(null!=roomId){//没有历史记录
			//取得聊天记录
			List<ChatsMsg> listChatsMsg = chatService.listPagedChatsMsg(roomId,comId,chatsMsg);
			JSONArray historysJson = new JSONArray();
	    	if(null!=listChatsMsg && listChatsMsg.size()>0){
	    		for (ChatsMsg chatsMsgObj : listChatsMsg) {
	    			JSONObject chatsMsgJson = new JSONObject();
	    			//用户姓名
	    			chatsMsgJson.put("username", chatsMsgObj.getUserName());
	    			//用户头像
	    			String avatar = "";
	    			
	    			if(null==chatsMsgObj.getUuid() || "".equals(chatsMsgObj.getUuid())){
	    				avatar = "/static/headImg/2"+((null==chatsMsgObj.getGender() || "".equals(chatsMsgObj.getGender()))?2:chatsMsgObj.getGender())+".jpg";
	    			}else{
	    				avatar = "/downLoad/down/"+chatsMsgObj.getUuid()+"/"+chatsMsgObj.getFileName();
	    			}
	    			chatsMsgJson.put("avatar", avatar);
	    			//对话界面
	    			chatsMsgJson.put("id",toUser.toString());
	    			String content = chatsMsgObj.getContent();
	    			
	    			content = content.replace("\"", "&quot;");
	    			content = content.replace("\'", "&#39;");
	    			//消息内容
	    			chatsMsgJson.put("content", content);
	    			//聊天类型
	    			chatsMsgJson.put("type", type);
	    			//消息发送方
	    			chatsMsgJson.put("timestamp", chatsMsgObj.getRecordCreateTime());
	    			
	    			historysJson.add(0, chatsMsgJson.toString());
	    		}
	    	}
	    	mav.addObject("history", historysJson.toString());
		}
		return mav;
	}
	/**
	 * 取得聊天历史(面板显示的)
	 */
	@ResponseBody
	@RequestMapping(value="/initChatsHistory")
	public String initChatsHistory(String chatsType,Integer comId,Integer fromUser,Integer toUser){
		
		//取得聊天室主键(需要优化)
		Integer roomId = chatService.getChatsRoomId(comId,fromUser,toUser,chatsType);
		if(null==roomId){//没有历史记录
			return null;
		}
		
		//取得聊天人员信息
		ChatsNoRead chatsNoRead = chatService.getChatsNoReadByUserId(comId,roomId,fromUser);
		if(null!=chatsNoRead){
			if(chatsNoRead.getNoReadNum()>0){
				chatsNoRead.setNoReadNum(0);
				//将聊天室的未读消息数设置为0
				chatService.updateReadAll(chatsNoRead);
			}
		}
		
		//取得聊天记录，显示连续三天的记录
		List<ChatsMsg> listChatsMsg = chatService.listChatsMsg(roomId,comId);
		
		// 触发连接事件，在连接池中添加连接
    	JSONObject result = new JSONObject();
    	result.put("code", 0);
    	JSONArray historysJson = new JSONArray();
    	if(null!=listChatsMsg && listChatsMsg.size()>0){
    		for (ChatsMsg chatsMsg : listChatsMsg) {
    			JSONObject chatsMsgJson = new JSONObject();
    			//用户姓名
    			chatsMsgJson.put("username", chatsMsg.getUserName());
    			//用户头像
    			String avatar = "";
    			
    			if(null==chatsMsg.getUuid() || "".equals(chatsMsg.getUuid())){
    				avatar = "/static/headImg/2"+((null==chatsMsg.getGender() || "".equals(chatsMsg.getGender()))?2:chatsMsg.getGender())+".jpg";
    			}else{
    				avatar = "/downLoad/down/"+chatsMsg.getUuid()+"/"+chatsMsg.getFileName();
    			}
    			chatsMsgJson.put("avatar", avatar);
    			//对话界面
    			chatsMsgJson.put("id",toUser);
    			//消息内容
    			chatsMsgJson.put("content", chatsMsg.getContent());
    			//聊天类型
    			chatsMsgJson.put("type", chatsType);
    			//消息发送方
    			Integer msgFrom = chatsMsg.getMsgFrom();
    			if(chatsType.equals("friend")){
    				//是否为自己
    				chatsMsgJson.put("mine", msgFrom.equals(fromUser));
    			}else{
    				//是否为自己
    				chatsMsgJson.put("mine",false);
    			}
    			chatsMsgJson.put("timestamp", DateTimeUtil.parseDate(chatsMsg.getRecordCreateTime(), DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime());
    			
    			historysJson.add(chatsMsgJson.toString());
			}
    	}
    	result.put("history_message", historysJson.toString());
		
		return result.toString();
	}
	/**
	 * 取得消息未读，用于面板初始化
	 * @param comId 企业号
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getChatsNoRead")
	public String getChatsNoRead(Integer comId,Integer sessionUser){
		//取得聊天记录，显示连续三天的记录
		List<ChatsNoRead> listChatsNoRead = chatService.listChatsNoRead(comId,sessionUser);
		
		// 触发连接事件，在连接池中添加连接
    	JSONObject result = new JSONObject();
    	result.put("code", 0);
    	JSONArray historysJson = new JSONArray();
    	
		if(null!=listChatsNoRead && listChatsNoRead.size()>0){
//			for (ChatsNoRead chatsNoRead : listChatsNoRead) {
//				//取得聊天记录，显示连续三天的记录
//				List<ChatsMsg> listChatsMsg = chatService.listChatsMsg(chatsNoRead.getRoomId(),comId);
//				if(null!=listChatsMsg && listChatsMsg.size()>0){
//					//
//					String toUser = chatsNoRead.getFromUser();
//					String chatsType = chatsNoRead.getChatType();
//					for (ChatsMsg chatsMsg : listChatsMsg) {
//						JSONObject chatsMsgJson = new JSONObject();
//		    			//用户姓名
//		    			chatsMsgJson.put("username", chatsMsg.getUserName());
//		    			//用户头像
//		    			String avatar = "";
//		    			
//		    			if(null==chatsMsg.getUuid() || "".equals(chatsMsg.getUuid())){
//		    				avatar = "/static/headImg/2"+((null==chatsMsg.getGender() || "".equals(chatsMsg.getGender()))?2:chatsMsg.getGender())+".jpg";
//		    			}else{
//		    				avatar = "/downLoad/down/"+chatsMsg.getUuid()+"/"+chatsMsg.getFileName();
//		    			}
//		    			chatsMsgJson.put("avatar", avatar);
//		    			//对话界面
//		    			chatsMsgJson.put("id",toUser);
//		    			//消息内容
//		    			chatsMsgJson.put("content", chatsMsg.getContent());
//		    			//聊天类型
//		    			chatsMsgJson.put("type", chatsType);
//		    			//消息发送方
//		    			Integer msgFrom = chatsMsg.getMsgFrom();
//		    			if(chatsType.equals("friend")){
//		    				//是否为自己
//		    				chatsMsgJson.put("mine", msgFrom.equals(sessionUser));
//		    			}else{
//		    				//是否为自己
//		    				chatsMsgJson.put("mine",false);
//		    			}
//		    			chatsMsgJson.put("timestamp", DateTimeUtil.parseDate(chatsMsg.getRecordCreateTime(), DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime());
//		    			
//		    			historysJson.add(chatsMsgJson.toString());
//					}
//				}
//			}
			
			for (ChatsNoRead chatsNoRead : listChatsNoRead) {
				//聊天类型
				String chatsType = chatsNoRead.getChatType();
				//聊天对象
				
				Integer fromUserId = Integer.parseInt(chatsNoRead.getFromUser());
				JSONObject chatsMsgJson = new JSONObject();
				//用户头像
    			String avatar = "";
				if(chatsType.equals("friend")){
					UserInfo fromUser = userInfoService.getUserBaseInfo(comId, fromUserId);
					if(null==fromUser.getSmImgUuid() || "".equals(fromUser.getSmImgUuid())){
	    				avatar = "/static/headImg/2"+((null==fromUser.getGender() || "".equals(fromUser.getGender()))?2:fromUser.getGender())+".jpg";
	    			}else{
	    				avatar = "/downLoad/down/"+fromUser.getSmImgUuid()+"/"+fromUser.getSmImgName();
	    			}
					//用户姓名
					chatsMsgJson.put("username", fromUser.getUserName());
				}
				
    			chatsMsgJson.put("avatar", avatar);
    			//对话界面
    			chatsMsgJson.put("id",fromUserId);
    			//聊天类型
    			chatsMsgJson.put("type", chatsType);
    			//是否为自己
    			chatsMsgJson.put("mine",false);
    			
    			historysJson.add(chatsMsgJson.toString());
    			
			}
		}
		result.put("history_message", historysJson.toString());
		
		return result.toString();
		
	}
	
	
	
}