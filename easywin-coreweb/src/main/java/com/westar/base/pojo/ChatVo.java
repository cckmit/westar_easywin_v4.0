package com.westar.base.pojo;

import java.util.List;

import com.westar.base.model.UserInfo;

/**
 * 聊天室成员
 * @author H87
 *
 */
public class ChatVo {
	
	//会话
//	 private Session session; 
	 /*企业编号*/
	 private Integer comId;
	 
	 /*房间编号*/
	 private Integer roomId;
	 
	 /*用户主键*/
	 private Integer userId;
	 
	 /*用户姓名*/
	 private String userName;
	 
	 /*模块主键*/
	 private Integer busId;
	 
	 /*模块类型 见系统常量*/
	 private String busType;
	 
	 //聊天室成员
	 private List<UserInfo> userList;

//	public ChatVo(Session session, Integer comId, Integer roomId,Integer userId, 
//			String userName, Integer busId, String busType,List<UserInfo> userList) {
//		super();
//		this.session = session;
//		this.comId = comId;
//		this.roomId = roomId;
//		this.userId = userId;
//		this.userName = userName;
//		this.busId = busId;
//		this.busType = busType;
//		this.userList = userList;
//	}
//
//	public Session getSession() {
//		return session;
//	}

//	public void setSession(Session session) {
//		this.session = session;
//	}

	public Integer getComId() {
		return comId;
	}

	public void setComId(Integer comId) {
		this.comId = comId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getBusId() {
		return busId;
	}

	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	@Override
	public String toString() {
		return "ChatVo [comId=" + comId + ", roomId=" + roomId + ", userId="
				+ userId + ", userName=" + userName + ", busId=" + busId
				+ ", busType=" + busType + "]";
	}

	public List<UserInfo> getUserList() {
		return userList;
	}

	public void setUserList(List<UserInfo> userList) {
		this.userList = userList;
	}
}
