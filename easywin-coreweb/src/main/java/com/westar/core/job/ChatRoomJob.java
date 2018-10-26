package com.westar.core.job;


/**
 *聊天处理类 
 * @author H87
 *
 */
//@Component
//@ServerEndpoint(value="/chatRoom/{comId}/{userId}")
//public class ChatRoomJob {
//	
//	//企业大楼
//	private static  Map<Integer,Map<Integer,Session>> comMap = 
//		new HashMap<Integer,Map<Integer,Session>>();
//	
//	private static ChatService chatService;
//	
//	public static void setChatService(ChatService chatService2) {
//		chatService = chatService2;
//	}
//	
//	/**
//	 * 打开通讯的时候
//	 * @param session 回话session 不是http的session
//	 * @param conf 配置文件
//	 * @param comId 企业号
//	 * @param userId 用户主键
//	 */
//    //@OnOpen
//    public void open(
//    		Session session, 
//    		EndpointConfig conf,
//    		@PathParam("comId") Integer comId,
//    		@PathParam("userId") Integer userId
//    		) {
//    	Map<Integer,Session> userMap = comMap.get(comId);
//    	if(null==userMap || userMap.size()==0){
//    		userMap = new HashMap<Integer, Session>();
//    	}
//    	userMap.put(userId, session);
//    	comMap.put(comId, userMap);
//        
//    }
//
//    //@OnMessage
//    public void processUpload(ByteBuffer msg, boolean last, Session session) {
//        System.out.println("Binary message");
//    }
//
//    /**
//     * 消息推送
//     * @param msg 消息内容
//     * @param comId 企业号
//     * @param userId 当前发送消息的人员
//     */
//    //@OnMessage
//    public void message(String msg,@PathParam("comId") Integer comId,
//    		@PathParam("userId") Integer userId) {
//    	//添加聊天内容
//    	JSONObject msgObj = JSONObject.fromObject(msg);
//    	
//    	//消息类别
//    	String type = msgObj.getString("type");
//    	//聊天范围
//    	String chatsType = "";
//    	
//    	Integer fromUser = 0;
//    	Integer toUser = 0;
//    	String content = "";
//    	// 触发连接事件，在连接池中添加连接
//    	JSONObject result = new JSONObject();
//    	if(null==type || "".equals(type)){
//    		
//    	}else {
//    		result.element("type", type);
//    		
//    		JSONObject resultData = new JSONObject();
//    		if("chatMessage".equals(type)){
//    			String dataStr = msgObj.getString("data");
//    			JSONObject dataObj = JSONObject.fromObject(dataStr);
//    			
//    			String mineStr = dataObj.getString("mine");
//    			JSONObject mineObj = JSONObject.fromObject(mineStr);
//    			//消息来源用户名
//    			resultData.element("username", mineObj.getString("username"));
//    			//消息来源用户头像
//    			resultData.element("avatar", mineObj.getString("avatar"));
//    			//聊天窗口来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
//    			resultData.element("id", mineObj.getInt("id"));
//    			//消息来源人员主键
//    			fromUser = mineObj.getInt("id");
//    			//消息内容
//    			content = mineObj.getString("content");
//    			resultData.element("content",content );
//    			
//    			String toStr = dataObj.getString("to");
//    			JSONObject toObj = JSONObject.fromObject(toStr);
//    			
//    			chatsType = toObj.getString("type");
//    			//聊天窗口来源类型，从发送消息传递的to里面获取
//    			resultData.element("type", chatsType);
//    			//消息来源人员主键
//    			toUser = toObj.getInt("id");
//    			
//    			//是否我发送的消息，如果为true，则会显示在右方
//    			resultData.element("mine", false);
//    			//服务端动态时间戳
//    			resultData.element("timestamp", new Date().getTime());
//    		
//    		}else if("init".equals(type)){
//    			chatsType = "friend";
//    			fromUser = userId;
//    			toUser = userId;
//    		}
//    		result.element("data", resultData.toString());
//    	}
//    	
//    	if(null!=chatsType && !"".equals(chatsType) && fromUser>0 && toUser>0){
//    		try {
//				//取得聊天室主键(需要优化)
//    			Integer roomId = chatService.getChatsRoomId(comId,fromUser,toUser,chatsType);
//    			
//    			if(chatsType.equals("friend")){//单聊
//    				
//    				Map<Integer,Session> userMap = comMap.get(comId);
//    				if(null!=userMap && userMap.size()>0){
//    					Session toSession = userMap.get(toUser);
//    					if(null!=toSession && toSession.isOpen()){
//    						//向在线成员发送消息
//    						toSession.getBasicRemote().sendText(result.toString());
//    					}else{
//    						//设置消息未读
//    						chatService.updateNoReadNum(comId, roomId, toUser);
//    					}
//    				}else{
//    					//设置消息未读
//    					chatService.updateNoReadNum(comId, roomId, toUser);
//    				}
//    			}
//    			if(null!=roomId){
//    				//添加聊天记录
//	    			ChatsMsg chatsMsg = new ChatsMsg();
//	    			chatsMsg.setComId(comId);
//	    			chatsMsg.setRoomId(roomId);
//	    			chatsMsg.setContent(content);
//	    			chatsMsg.setMsgFrom(fromUser);
//	    			chatsMsg.setMsgTo(toUser);
//    				//添加聊天记录
//    				chatService.addChatsMsg(chatsMsg);
//    			}
//    			
//			} catch (Exception e) {
//				
//			}
//    	}
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
//    //@OnClose
//    public void close(Session session, 
//    		CloseReason reason,
//    		@PathParam("comId") Integer comId,
//    		@PathParam("userId") Integer userId
//    		) {
//    	Map<Integer,Session> userMap = comMap.get(comId);
//    	if(null==userMap || userMap.size()==0){
//    		userMap = new HashMap<Integer, Session>();
//    	}else{
//    		if(userMap.containsKey(userId)){
//    			userMap.remove(userId);
//    		}
//    	}
//    	comMap.put(comId, userMap);
//    }
//
//    //@OnError
//    public void error(Session session, Throwable t,@PathParam("comId") Integer comId,
//    		@PathParam("userId") Integer userId) {
//    	Map<Integer,Session> userMap = comMap.get(comId);
//    	if(null==userMap || userMap.size()==0){
//    		userMap = new HashMap<Integer, Session>();
//    	}else{
//    		if(userMap.containsKey(userId)){
//    			userMap.remove(userId);
//    		}
//    	}
//    	comMap.put(comId, userMap);
//
//    }
//    /**
//     * 发送系统推送的消息
//     * @param msg 消息内容
//     * @param sessopnUser 当前操作人员
//     * @param toUser
//     */
//	public void sendMsg(String msg,UserInfo sessopnUser,Integer toUser) {
//		
//		// 触发连接事件，在连接池中添加连接
//    	JSONObject result = new JSONObject();
//    	result.element("type", "systemMessage");
//    	
//		Integer comId = sessopnUser.getComId();
//		Integer fromUser = sessopnUser.getId();
//		
//		JSONObject resultData = new JSONObject();
//		String avatar = "";
//		if(null==sessopnUser.getSmImgUuid() || "".equals(sessopnUser.getSmImgUuid())){
//			avatar = "/static/headImg/2"+((null==sessopnUser.getGender() || "".equals(sessopnUser.getGender()))?2:sessopnUser.getGender())+".jpg";
//		}else{
//			avatar = "/downLoad/down/"+sessopnUser.getSmImgUuid()+"/"+sessopnUser.getSmImgName();
//		}
//		
//		//消息来源用户名
//		resultData.element("username", sessopnUser.getUserName());
//		//消息来源用户头像
//		resultData.element("avatar", avatar);
//		//聊天窗口来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
//		resultData.element("id", sessopnUser.getId());
//		
//		resultData.element("content",msg );
//		
//		//聊天窗口来源类型，从发送消息传递的to里面获取
//		resultData.element("type", "friend");
//		//是否我发送的消息，如果为true，则会显示在右方
//		resultData.element("mine", false);
//		//服务端动态时间戳
//		resultData.element("timestamp", new Date().getTime());
//		
//		result.element("data", resultData.toString());
//		
//		try {
//			//取得聊天室主键(需要优化)
//			Integer roomId = chatService.getChatsRoomId(comId,fromUser,toUser,"friend");
//			
//			Map<Integer,Session> userMap = comMap.get(comId);
//			if(null!=userMap && userMap.size()>0){
//				Session toSession = userMap.get(toUser);
//				if(null!=toSession && toSession.isOpen()){
//					//向在线成员发送消息
//					toSession.getBasicRemote().sendText(result.toString());
//				}else{
//					//设置消息未读
//					chatService.updateNoReadNum(comId, roomId, toUser);
//				}
//			}else{
//				//设置消息未读
//				chatService.updateNoReadNum(comId, roomId, toUser);
//			}
//			if(null!=roomId){
//				//添加聊天记录
//    			ChatsMsg chatsMsg = new ChatsMsg();
//    			chatsMsg.setComId(comId);
//    			chatsMsg.setRoomId(roomId);
//    			chatsMsg.setContent(msg);
//    			chatsMsg.setMsgFrom(fromUser);
//    			chatsMsg.setMsgTo(toUser);
//				//添加聊天记录
//				chatService.addChatsMsg(chatsMsg);
//			}
//			
//		} catch (Exception e) {
//			
//		}
//		
//	}
//
//    
//}