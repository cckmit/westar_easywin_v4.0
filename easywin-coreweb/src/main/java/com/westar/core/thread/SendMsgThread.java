package com.westar.core.thread;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.westar.base.model.UserInfo;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.TodayWorksService;

public class SendMsgThread implements Runnable {
	/**
	 * service
	 */
	private TodayWorksService todayWorksService;
	/**
	 * 发送人
	 */
	private UserInfo userInfo;
	/**
	 * 接受方
	 */
	private List<UserInfo> shares;
	/**
	 * 业务主键
	 */
	private Integer busId;
	/**
	 * 业务类别
	 */
	private String busType;
	/**
	 * 消息内容
	 */
	private String content;
	
	
	/**
	 * 构造函数
	 * @param todayWorksService
	 * @param userInfo
	 * @param shares
	 * @param busId
	 * @param busType
	 * @param content
	 */
	public SendMsgThread(TodayWorksService todayWorksService, UserInfo userInfo,
			List<UserInfo> shares, Integer busId, String busType, String content) {
		super();
		this.todayWorksService = todayWorksService;
		this.userInfo = userInfo;
		this.shares = shares;
		this.busId = busId;
		this.busType = busType;
		this.content = content;
	}

	@Override
	public void run() {
		Set<Integer> userIds = new HashSet<Integer>();
		userIds.add(userInfo.getId());
		for(UserInfo shareUser : shares){
			Integer userId = shareUser.getId();
			if(!userIds.contains(userId)){
				todayWorksService.sendMsgForOne(shareUser.getComId(),busId,busType,userId, userInfo.getId(), content);
			}
			userIds.add(userId);
		}
		
	}
}
