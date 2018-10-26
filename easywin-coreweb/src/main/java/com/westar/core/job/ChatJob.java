package com.westar.core.job;


/**
 *聊天处理类 
 * @author H87
 *
 */
//@ServerEndpoint(value="/chat/{comId}/{roomId}/{userId}/{userName}/{busId}/{busType}")
//@ServerEndpoint(value="/chatRoom/{comId}/{roomId}/{userId}/{userName}/{busId}/{busType}")
//public class ChatJob {
//	
//	//企业大楼
//	private static  Map<String,Map<Integer,ChatJob>> ComMap = 
//			new HashMap<String,Map<Integer,ChatJob>>();
//	
//	private static ChatService chatService;
//	
//	public static void setChatService(ChatService chatService) {
//		ChatJob.chatService = chatService;
//	}
//	
//	//会话
//	private ChatVo chatVo; 
//	
//	/**
//	 * 打开通讯的时候
//	 * @param session 回话session 不是http的session
//	 * @param conf 配置文件
//	 * @param comId 企业号
//	 * @param roomId 房间号
//	 * @param busId 业务主键
//	 * @param busType 业务类型
//	 * @param userId 用户主键
//	 * @param userName 用户姓名
//	 */
////    @OnOpen
//    public void open(Session session, 
//    		EndpointConfig conf,
//    		@PathParam("comId") Integer comId,
//    		@PathParam("roomId") Integer roomId,
//    		@PathParam("busId") Integer busId,
//    		@PathParam("busType") String busType,
//    		@PathParam("userId") Integer userId,
//    		@PathParam("userName") String userName
//    		) {
//    	//删除消息提醒
//    	chatService.delTodayWork(comId,roomId,userId);
//    	//聊天室的所有成员
//    	List<UserInfo> userList = chatService.listChatRoomUser(comId,roomId,busId,busType);
//    	ChatVo chatVo = new ChatVo(session, comId, roomId, userId, userName, busId, busType,userList);
//    	this.chatVo = chatVo;
//    	//聊天室主键
//    	String roomKey = comId+"_"+roomId;
//    	//聊天室在线成员
//    	Map<Integer,ChatJob> ComRoomMap = ComMap.get(roomKey);
//    	
//    	if(null==ComRoomMap){//没有企业楼
//    		Map<Integer,ChatJob> UserMap = 
//    				new HashMap<Integer,ChatJob>();
//    		//添加团队成员
//    		UserMap.put(userId, this);
//    		
//    		ComMap.put(roomKey, UserMap);
//    	}else{
//    		if(null ==ComRoomMap.get(userId)){//成员没有在聊天室，则添加在线人员
//    			ComRoomMap.put(userId, this);
//    			ComMap.put(roomKey, ComRoomMap);
//    		}
//    	}
//    	
//     // 触发连接事件，在连接池中添加连接
// 		JSONObject result = new JSONObject();
// 		//向所有在线用户推送当前用户上线的消息
// 		try {
// 			result.element("type", "user_join");
// 			
// 			JSONObject userObj = new JSONObject();
// 			userObj.element("id", userId);
// 			userObj.element("name", userName);
// 			
// 			result.element("user", userObj.toString());
// 			//在线人员集合
//        	Set<Integer> keySet = ComMap.get(roomKey).keySet();
//        	for (Integer objKey : keySet) {
//        		if(!objKey.equals(userId)){
//        			ChatJob chatJob = ComMap.get(roomKey).get(objKey);
//        			Session sessionT = chatJob.chatVo.getSession();
//        			if(null!=sessionT && sessionT.isOpen()){
//        				//向在线成员发送消息
//        				sessionT.getBasicRemote().sendText(result.toString());
//        			}
//        		}
//        	}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
// 		
//        result = new JSONObject();
// 		result.element("type", "get_online_user");
// 		//向连接池添加当前的连接对象
// 		//向当前连接发送当前在线用户的列表
// 		try {
// 			Set<String> users = new HashSet<String>();
//    		if(null!=userList){
//    			for (UserInfo userInfo : userList) {
//    				JSONObject userObj = new JSONObject();
//        			userObj.element("id", userInfo.getId());
//        			userObj.element("name",userInfo.getUserName());
//        			if(null==ComMap.get(roomKey).get(userInfo.getId())){
//        				userObj.element("online",0);
//        			}
//        			
//        			users.add(userObj.toString());
//				}
//    		}
//    		result.element("list", users);
//        	session.getBasicRemote().sendText(result.toString());
//    		
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        
//    }
//
////    @OnMessage
//    public void processUpload(ByteBuffer msg, boolean last, Session session) {
//        System.out.println("Binary message");
//    }
//
//    /**
//     * 消息推送
//     * @param msg 消息内容
//     * @param comId 企业号
//     * @param roomId 房间号
//     * @param busId 业务主键
//     * @param userId 当前发送消息的人员
//     * @param busType 业务类型
//     */
////    @OnMessage
//    public void message(String msg,@PathParam("comId") Integer comId,
//    		@PathParam("roomId") Integer roomId,@PathParam("busId") Integer busId,
//    		@PathParam("userId") Integer userId,@PathParam("busType") String busType) {
//        try {
//        	//房间主键
//        	String roomKey = comId+"_"+roomId;
//        	
//        	//添加聊天内容
//        	JSONObject result = JSONObject.fromObject(msg);
//        	//消息内容
//        	String content = result.getString("content");
//        	//消息类型
//        	String type = result.getString("type");
//        	
//        	if(null!=type && type.equals("message")){//是发送的消息
//        		//取得聊天房间
//        		ChatJob chatJob = ComMap.get(roomKey).get(userId);
//        		//聊天室所有成员
//        		List<UserInfo> listUser = chatJob.chatVo.getUserList();
//        		for (UserInfo userInfo : listUser) {
//        			//消息接收人员主键
//        			Integer reciverId = userInfo.getId();
//        			//取得聊天室成员的会话
//        			ChatJob chatJobObj = ComMap.get(roomKey).get(reciverId);
//        			if(null!=chatJobObj){//人员在线
//        				Session sessionT = chatJobObj.chatVo.getSession();
//        				if(null!=sessionT && sessionT.isOpen()){//人员在线，
//        					//发送消息
//        					sessionT.getBasicRemote().sendText(msg);
//        				}else{//人员不在线
//        					//修改消息未读数以及发送消息提醒
//        					chatService.updateNoReadNum(comId, roomId, reciverId, busId, busType);
//        				}
//        			}else{//人员不在线
//        				//修改消息未读数以及发送消息提醒
//        				chatService.updateNoReadNum(comId, roomId, reciverId, busId, busType);
//        			}
//        		}
//        		
//        		//记录聊天内容
//        		ChatMsg chatMsg = new ChatMsg(comId, roomId, userId,content,0);
//        		chatService.addChatMsg(chatMsg);
//        	}else if(null!=type && type.equals("userInv")){//邀请成员
//        		//邀请后聊天室所有成员
//        		List<UserInfo> userList = chatService.updateChatUser(comId,roomId,content);
//        		
//        		//设置推送的消息
//        		JSONObject onLineUser = new JSONObject();
//        		onLineUser.element("type", "get_online_user");
//        		Set<String> users = new HashSet<String>();
//        		if(null!=userList){
//	    			for (UserInfo userInfo : userList) {
//	    				JSONObject userObj = new JSONObject();
//	        			userObj.element("id", userInfo.getId());
//	        			userObj.element("name",userInfo.getUserName());
//	        			if(null==ComMap.get(roomKey).get(userInfo.getId())){
//	        				userObj.element("online",0);
//	        			}
//	        			users.add(userObj.toString());
//					}
//	    		}
//        		onLineUser.element("list", users);
//        		//重新设置在线人员的聊天室成员集合
//	    		Map<Integer, ChatJob> resetMap = new HashMap<Integer, ChatJob>();
//        		//在线成员列表
//            	Set<Integer> keySet = ComMap.get(roomKey).keySet();
//            	//遍历在线人员，并发送消息，重新设置在线人员的聊天室成员集合
//            	for (Integer objKey : keySet) {
//        			ChatJob chatJob = ComMap.get(roomKey).get(objKey);
//        			
//        			Session sessionT = chatJob.chatVo.getSession();
//        			if(null!=sessionT && sessionT.isOpen()){
//        				//向在线人员发送人员信息
//        				sessionT.getBasicRemote().sendText(onLineUser.toString());
//        				
//        				ChatVo chatVo = chatJob.chatVo;
//        				chatVo.setUserList(userList);
//        				
//        				chatJob.chatVo = chatVo;
//        				resetMap.put(objKey, chatJob);
//        			}
//            	}
//            	
//            	ComMap.put(roomKey, resetMap);
//        	}
//        	
//        	
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//    }
//    /**
//     * 关闭聊天室后发送离线消息
//     * @param session 会话
//     * @param reason 原因
//     * @param comId 企业号
//     * @param roomId 房间号
//     * @param busId 业务主键
//     * @param busType 业务类型
//     * @param userId 用户主键
//     * @param userName 用户姓名
//     */
////    @OnClose
//    public void close(Session session, 
//    		CloseReason reason,
//    		@PathParam("comId") Integer comId,
//    		@PathParam("roomId") Integer roomId,
//    		@PathParam("busId") Integer busId,
//    		@PathParam("busType") String busType,
//    		@PathParam("userId") Integer userId,
//    		@PathParam("userName") String userName
//    		) {
//    	//房间主键
//    	String roomKey = comId+"_"+roomId;
//    	//聊天室所有成员
//    	Map<Integer,ChatJob> ComRoomMap = ComMap.get(roomKey);
//    	if(null!=ComRoomMap){//企业楼
//    		//移除人员
//    		ComRoomMap.remove(userId);
//    		ComMap.put(roomKey, ComRoomMap);
//    	}
//    	
//    	//向所有在线用户推送当前用户上线的消息
//    	JSONObject result = new JSONObject();
// 		try {
// 			result.element("type", "user_leave");
// 			
// 			JSONObject userObj = new JSONObject();
// 			userObj.element("id", userId);
// 			userObj.element("name", userName);
// 			
// 			result.element("user", userObj.toString());
// 			//用户列表
//        	Set<Integer> keySet = ComMap.get(roomKey).keySet();
//        	
//        	//在线人数
//        	Integer count = 0;
//        	//遍历在线人员，并发送离线消息
//        	for (Integer objKey : keySet) {
//        		ChatJob chatJob = ComMap.get(roomKey).get(objKey);
//        		Session sessionT = chatJob.chatVo.getSession();
//        		if(null!=sessionT && sessionT.isOpen()){//向客户端发送数据
//        			sessionT.getBasicRemote().sendText(result.toString());
//        			count++;
//        		}
//        	}
//        	//在线人数
//        	if(count==0){//没有在线成员的时候，将本次会话内容整理成日志
//        		chatService.addBusLog(comId,roomId,busId,busType,userId,userName);
//        	}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    }
//
////    @OnError
//    public void error(Session session, Throwable t) {
//        t.printStackTrace();
//
//    }
    
//}