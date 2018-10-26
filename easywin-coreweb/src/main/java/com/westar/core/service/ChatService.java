package com.westar.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.westar.base.model.ChatMsg;
import com.westar.base.model.ChatRoom;
import com.westar.base.model.ChatUser;
import com.westar.base.model.ChatsGrpUser;
import com.westar.base.model.ChatsMsg;
import com.westar.base.model.ChatsNoRead;
import com.westar.base.model.ChatsRoom;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.ChatDao;

@Service
public class ChatService {

	@Autowired
	ChatDao chatDao;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	CrmService crmService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	WeekReportService weekService;
	
	@Autowired
	QasService qasService;
	
	@Autowired
	VoteService voteService;

	@Autowired
	DailyService dailyService;

	@Autowired
	ProductService productService;


	/**
	 * 查询聊天室是否已存在
	 * @param id 聊天室主键
	 * @return
	 */
	public ChatRoom getChatRoomById(Integer id) {
		ChatRoom room = (ChatRoom) chatDao.objectQuery(ChatRoom.class,id);
		return room;
	}
	/**
	 * 查询聊天室是否已存在
	 * @param comId 企业号
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @param chatType 是否群聊
	 * @param chater 聊天成员
	 * @return
	 */
	public ChatRoom getChatRoomByType(Integer comId, String busType,
			Integer busId, String chatType, String chater) {
		ChatRoom room = chatDao.getChatRoomByType(comId,busType,busId,chatType,chater);
		return room;
	}
	/**
	 * 添加聊天室
	 * @param chatRoom 
	 */
	public Integer addChatRoom(ChatRoom chatRoom) {
		Integer id = chatDao.add(chatRoom);
		return id;
	}
	/**
	 * 添加聊天成员
	 * @param chatUser
	 */
	public void addChatUser(ChatUser chatUser) {
		chatDao.add(chatUser);
	}
	/**
	 * 查询业务的聊天室
	 * @param userInfo
	 * @param chatRoom
	 * @return
	 */
	public List<ChatRoom> listBusChat(UserInfo userInfo, ChatRoom chatRoom) {
		List<ChatRoom> list = chatDao.listBusChat(userInfo,chatRoom);
		List<ChatRoom> listForRet = new ArrayList<ChatRoom>();
		if(null!=list && list.size()>0){
			for (ChatRoom chatRoomObj : list) {
				//聊天室成员
				List<ChatUser> chatUser = chatDao.getChatUserByRoomId(userInfo.getComId(),chatRoomObj.getId());
				chatRoomObj.setChaters(chatUser);
				listForRet.add(chatRoomObj);
			}
		}
		return listForRet;
	}
	
	/**
	 * 聊天室成员
	 * @param comId 企业号
	 * @param roomId 房间号
	 * @return
	 */
	public List<ChatUser> listChatUser(Integer comId,Integer roomId){
		List<ChatUser> chatUser = chatDao.getChatUserByRoomId(comId,roomId);
		return chatUser;
	}
	
	/**
	 * 查询聊天室的成员
	 * @param comId 企业号
	 * @param roomId 房间号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	public List<UserInfo> listChatRoomUser(Integer comId, Integer roomId, Integer busId, String busType) {
		List<UserInfo> listUser = new ArrayList<UserInfo>();
		ChatRoom chatRoom = (ChatRoom) chatDao.objectQuery(ChatRoom.class, roomId);
		if(null!=chatRoom){
			if(chatRoom.getChatType().equals("1")){//是群聊
				
			}else{
				String chater = chatRoom.getChater();
				//查询人员详情
				listUser = userInfoService.getUserInfoByIds(comId,chater);
			}
		}
		return listUser;
	}
	/**
	 * 添加聊天内容
	 * @param chatMsg
	 */
	public void addChatMsg(ChatMsg chatMsg) {
		chatDao.add(chatMsg);
	}
	/**
	 * 取得聊天记录
	 * @param userInfo 操作员
	 * @param roomId 业务主键
	 * @param minId 当前页面已展示的信息主键最小数
	 * @param chatType 聊天类型
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @param num 展示条数
	 * @return
	 */
	public List<ChatMsg> listChatMsg(UserInfo userInfo, Integer roomId,
			Integer minId, Integer busId, String busType, String chatType, Integer num) {
		List<ChatMsg> list = new ArrayList<ChatMsg>();
		list = chatDao.listChatMsg(userInfo,roomId,minId,busId,busType,num);
		return list;
	}
	/**
	 * 记录没有加载数
	 * @param userInfo 操作员
	 * @param roomId 房间号
	 * @param newMinId 新的最小主键
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param chatType 聊天类型
	 * @return
	 */
	public Integer countLeft(UserInfo userInfo, Integer roomId,
			Integer newMinId, Integer busId, String busType, String chatType) {
		Integer count = chatDao.countLeft(userInfo,roomId,newMinId,busId,busType);
		return count;
	}
	/**
	 * 模块添加日志
	 * @param comId 企业号
	 * @param roomId 房间号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param userId 用户主键
	 * @param userName 用户姓名
	 */
	public void addBusLog(Integer comId, Integer roomId, Integer busId,
			String busType, Integer userId, String userName) {
		List<ChatMsg> list = chatDao.listChatMsgForLog(comId,roomId);
		if(null!=list && list.size()>0){
			//修改聊天记录的状态
			chatDao.updateMsgLog(comId,roomId);
			//聊天日志
			String logContent = roomId+"号聊天室聊天记录<br>";
			for (ChatMsg chatMsg : list) {
				logContent+=chatMsg.getUserName()+"&nbsp;&nbsp;&nbsp;&nbsp;"+chatMsg.getRecordCreateTime()+"</br>";
				logContent+=chatMsg.getChatContent()+"<br>";
			}
			if(busType.equals("1")){//是分享
				//分享添加日志
				msgShareService.addMagShareRepLog(comId, busId, userId, userName, logContent);
			}else if(busType.equals(ConstantInterface.TYPE_CRM)){//是客户
				//客户添加日志
				crmService.addCustomerLog(comId, busId, userId, logContent);
			}else if(busType.equals(ConstantInterface.TYPE_ITEM)){//是项目
				//项目添加日志
				itemService.addItemLog(comId, busId, userId, userName, logContent);
			}else if(busType.equals(ConstantInterface.TYPE_PRODUCT)){//是产品
				//产品添加日志
				productService.addProLog(comId, busId, userId, userName, logContent);
			}else if(busType.equals(ConstantInterface.TYPE_TASK)){//任务
				//任务添加日志
				taskService.addTaskLog(comId, busId, userId, userName, logContent);
			}else if(busType.equals(ConstantInterface.TYPE_WEEK)){//是周报
				//周报添加日志
				weekService.addWeekRepLog(comId, busId, userId, userName,logContent);
			}else if(busType.equals(ConstantInterface.TYPE_DAILY)){//是分享
				//周报添加日志
				dailyService.addDailyLog(comId, busId, userId, userName,logContent);
			}else if(busType.equals(ConstantInterface.TYPE_QUES)){//是问答
				//问答添加日志
				qasService.addQuesLog(comId, busId, userId, userName, logContent);
			}else if(busType.equals(ConstantInterface.TYPE_VOTE)){//是投票
				//投票添加日志
				voteService.addVoteLog(comId, busId, userId, userName, logContent);
			}
		}
	}
	/**
	 * 修改消息未读数以及发送消息提醒
	 * @param comId 企业号
	 * @param roomId 房间号
	 * @param userId 消息接收人员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 */
	public void updateNoReadNum(Integer comId, Integer roomId, Integer userId, Integer busId, String busType) {
		
		//取得聊天人员信息
		ChatUser chatUser = chatDao.getChatUserByUserId(comId,roomId,userId);
		if(null!=chatUser){
			Integer noReadNum = chatUser.getNoReadNum()+1;
			chatUser.setNoReadNum(noReadNum);
			chatDao.update(chatUser);
			//未读消息提示
			String content = roomId+"号聊天室有"+noReadNum+"条未读消息";
			todayWorksService.addChatRemind(comId,roomId,userId,busId,busType,content);
		}
	}
	/**
	 * 删除消息提醒
	 * @param comId 企业号
	 * @param roomId 房间号
	 * @param userId 用户主键
	 */
	public void delTodayWork(Integer comId, Integer roomId, Integer userId) {
		//取得聊天人员信息
		ChatUser chatUser = chatDao.getChatUserByUserId(comId,roomId,userId);
		if(null!=chatUser){
			chatUser.setNoReadNum(0);
			chatDao.update(chatUser);
		}
		//删除聊天提醒
		chatDao.delByField("todayWorks", new String[]{"comId","roomId","userId"}, new Object[]{comId,roomId,userId});
	}
	/**
	 * 设置聊天室成员
	 * @param comId 企业号
	 * @param roomId 房间号
	 * @param userIds 成员主键
	 * @return
	 */
	public List<UserInfo> updateChatUser(Integer comId, Integer roomId,
			String userIds) {
		ChatRoom chatRoom = (ChatRoom) chatDao.objectQuery(ChatRoom.class, roomId);
		//房间原有成员
		String preChaters = chatRoom.getChater();
		//重新设置聊天的成员
		chatRoom.setChater(userIds);
		//修改聊天室成员
		chatDao.update(chatRoom);
		
		//原有成员集合化
		List<String> preUserList =Arrays.asList(preChaters.split(","));
		//重新设置聊天成员
		String[] userIdStr = userIds.split(",");
		for (String userId : userIdStr) {
			//是否为原有成员
			if(!preUserList.contains(userId)){//不是原有成员则添加
				//聊天成员
				ChatUser chatUser = new ChatUser();
				//企业号
				chatUser.setComId(comId);
				//房间号
				chatUser.setRoomId(roomId);
				//成员主键
				chatUser.setUserId(Integer.parseInt(userId));
				//设置未读消息数
				chatUser.setNoReadNum(0);
				//添加聊天成员
				chatDao.add(chatUser);
			}
		}
		List<UserInfo> userList = userInfoService.getUserInfoByIds(comId, userIds);
		return userList;
	}
	
	/**
	 * 删除模块下的所有聊天室
	 * @param comId 企业号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 */
	public void delBusChat(Integer comId, Integer busId, String busType) {
		//取得业务的所有聊天室
		List<ChatRoom> roomList = chatDao.listChatRoomByType(comId, busType, busId);
		if(null!=roomList && roomList.size()>0){
			for (ChatRoom chatRoom : roomList) {
				//删除聊天室成员
				chatDao.delByField("chatUser", new String[]{"comId","roomId"}, new Object[]{comId,chatRoom.getId()});
				//删除聊天内容
				chatDao.delByField("chatMsg", new String[]{"comId","roomId"}, new Object[]{comId,chatRoom.getId()});
				//删除聊天室
				chatDao.delById(ChatRoom.class, chatRoom.getId());
			}
		}
		
	}
	/**
	 * 合并聊天信息留言
	 * @param comId 企业号
	 * @param oldBusId 业务主键（原来的）
	 * @param newBusId 业务主键（后来的）
	 * @param busType 业务类型
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void comPressChat(Integer comId, Integer oldBusId, Integer newBusId,
			String busType) {
		//现在的聊天室
		List<ChatRoom> newChatRoom = chatDao.listChatRoomByType(comId, busType, newBusId);
		//聊天成员与聊天室主键
		Map<String,Integer> userRoom = new HashMap<String, Integer>();
		if(null!=newChatRoom && newChatRoom.size()>0){//现在有聊天室
			for (ChatRoom chatRoom : newChatRoom) {
				userRoom.put(chatRoom.getChater(), chatRoom.getId());
			}
		}
		//原来的聊天室
		List<ChatRoom> oldChatRoom = chatDao.listChatRoomByType(comId, busType, oldBusId);
		//聊天成员与聊天室主键
		if(null!=oldChatRoom && oldChatRoom.size()>0){//有聊天记录
			for (ChatRoom chatRoom : oldChatRoom) {
				if(userRoom.containsKey(chatRoom.getChater())){//现在的聊天室已有聊天记录
					//合并聊天内容
					chatDao.comPressChatMsg(comId,chatRoom.getId(),userRoom.get(chatRoom.getChater()));
					//删除聊天室成员
					chatDao.delByField("chatUser", new String[]{"comId","roomId"}, new Object[]{comId,chatRoom.getId()});
					//删除聊天室
					chatDao.delById(ChatRoom.class, chatRoom.getId());
				}else{
					chatDao.comPressChat(comId,oldBusId,newBusId,busType,chatRoom.getId());
				}
			}
		}
	}
	/**
	 * //取得聊天室主键
	 * @param fromUser 聊天发送方
	 * @param toUser 聊天接收方
	 * @param chatsType 聊天类型 friend 单聊 group 群聊
	 * @return
	 */
	public Integer getChatsRoomId(Integer comId,Integer fromUser, Integer toUser,
			 String chatsType) {
		ChatsRoom chatsRoom = null;
		if(fromUser.equals(toUser)){
			return null;
		}
		if("friend".equals(chatsType)){
			if(fromUser>toUser){
				chatsRoom = chatDao.getChatsRoomId(comId,chatsType,toUser,fromUser);
			}else{
				chatsRoom = chatDao.getChatsRoomId(comId,chatsType,fromUser,toUser);
			}
			
			if(null == chatsRoom){
				chatsRoom = new ChatsRoom();
				//设置企业号
				chatsRoom.setComId(comId);
				//设置聊天类型
				chatsRoom.setChatType(chatsType);
				//哪个主键小，哪个就是创建人
				chatsRoom.setCreater(fromUser>toUser?toUser:fromUser);
				//创建聊天室
				Integer roomId = chatDao.add(chatsRoom);
				chatsRoom.setId(roomId);
				
				//聊天室成员
				ChatsGrpUser chatsGrpUser = new ChatsGrpUser();
				//企业号
				chatsGrpUser.setComId(comId);
				//聊天室主键
				chatsGrpUser.setRoomId(roomId);
				//聊天组主键
				chatsGrpUser.setChatsGrpId(0);
				//哪个主键大，哪个就是成员
				chatsGrpUser.setMemberId(fromUser<toUser?toUser:fromUser);
				chatDao.add(chatsGrpUser);
			}
		}else{
			//在创建分组的时候，已先创建聊天室，一定不为空
			chatsRoom =chatDao.getChatsRoomId(comId,chatsType,fromUser,toUser);
		}
		return chatsRoom.getId();
	}
	/**
	 * 添加聊天室内容
	 * @param chatsMsg
	 */
	public void addChatsMsg(ChatsMsg chatsMsg) {
		chatDao.add(chatsMsg);
	}
	/**
	 * 修改消息未读数
	 * @param comId 企业号
	 * @param roomId 聊天室主键
	 * @param toUser 消息接收方
	 */
	public void updateNoReadNum(Integer comId, Integer roomId, Integer toUser) {
		if(null==roomId){
			return;
		}
		//取得聊天人员信息
		ChatsNoRead chatsNoRead = chatDao.getChatsNoReadByUserId(comId,roomId,toUser);
		if(null!=chatsNoRead){
			Integer noReadNum = chatsNoRead.getNoReadNum()+1;
			chatsNoRead.setNoReadNum(noReadNum);
			chatDao.update(chatsNoRead);
		}else{
			//聊天室消息未读数
			chatsNoRead = new ChatsNoRead();
			//企业号
			chatsNoRead.setComId(comId);
			//初始化未读数为1
			chatsNoRead.setNoReadNum(1);
			//聊天室主键
			chatsNoRead.setRoomId(roomId);
			//消息接收未读人员
			chatsNoRead.setUserId(toUser);
			chatDao.add(chatsNoRead);
			
		}
		
	}
	/**
	 * 取得连续三天的聊天记录
	 * @param roomId 聊天室主键
	 * @param comId 企业号
	 * @return
	 */
	public List<ChatsMsg> listChatsMsg(Integer roomId, Integer comId) {
		List<ChatsMsg> listChats = chatDao.listChatsMsg(roomId,comId);
		return listChats;
	}
	/**
	 * 分页取得聊天历史记录
	 * @param roomId 聊天室主键
	 * @param comId 企业号
	 * @param content 模糊查询
	 * @return
	 */
	public List<ChatsMsg> listPagedChatsMsg(Integer roomId, Integer comId, ChatsMsg chatsMsg) {
		List<ChatsMsg> listChats = chatDao.listPagedChatsMsg(roomId,comId,chatsMsg);
		return listChats;
	}
	/**
	 * 取得人员消息未读数
	 * @param comId 企业号
	 * @param roomId 聊天室主键
	 * @param fromUser 消息未读人员
	 * @return
	 */
	public ChatsNoRead getChatsNoReadByUserId(Integer comId, Integer roomId,
			Integer fromUser) {
		return chatDao.getChatsNoReadByUserId(comId, roomId, fromUser);
	}
	/**
	 * 将聊天室的未读消息数设置为0
	 * @param chatsNoRead
	 */
	public void updateReadAll(ChatsNoRead chatsNoRead) {
		chatDao.update(chatsNoRead);
	}
	/**
	 * 取得聊天室没有读取的消息
	 * @param comId 企业号
	 * @param userId 当前操作人员
	 * @return
	 */
	public List<ChatsNoRead> listChatsNoRead(Integer comId, Integer userId) {
		List<ChatsNoRead> list = chatDao.listChatsNoRead(comId,userId);
		return list;
	}
}
