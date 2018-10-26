package com.westar.core.thread;

import java.util.List;

import com.westar.base.model.UserInfo;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.TodayWorksService;

public class UpdateTodoWorksThread implements Runnable {
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
	/**
	 * @param todayWorksService
	 * @param userInfo
	 * @param shares
	 * @param busId
	 * @param busType
	 * @param content
	 */
	public UpdateTodoWorksThread(TodayWorksService todayWorksService, UserInfo userInfo,
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
		todayWorksService.updateTodayWorks(userInfo.getId(),busId, content, busType, shares);
	}
}
