package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.westar.base.cons.MsgSendWay;
import com.westar.base.model.BusRemind;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.Clock;
import com.westar.base.model.ClockPerson;
import com.westar.base.model.ClockWay;
import com.westar.base.model.ForMeDo;
import com.westar.base.model.MeetCheckUser;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UserConf;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.MsgNoRead;
import com.westar.base.util.BeanUtilEx;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.JpushUtils;
import com.westar.core.dao.TodayWorksDao;

@Service
public class TodayWorksService {
	
	private static final Log loger = LogFactory.getLog(TodayWorksService.class);

	@Autowired
	TodayWorksDao todayWorksDao;
	
	@Autowired
	AttentionService attentionService;
	
	@Autowired
	UserConfService userConfService;
	
	
	@Autowired
	MeetingService meetingService;
	
	/**
	 * 分页查询待办事项
	 * @param sessionUser
	 * @param todayWorks
	 * @return
	 */
	public List<TodayWorks> listPagedTodo(UserInfo sessionUser,TodayWorks todayWorks){
		List<TodayWorks> list = new ArrayList<>();
		return list;
	}
	
	/*********************查询结束*****************************************/
	/*********************开始发送消息***************************************/
	
	/**
	 * 更新消息提醒(非闹铃)
	 * 
	 * @param userInfo
	 *            操作用户
	 * @param chargeMan
	 *            负责人 为空则表示更新待办事项并发送普通消息 >0则表示添加待办事项并发送普通消息
	 *            -1则表示删除待办事项并发送普通消息(针对会议确认的，没有实际意义，只用于判断)
	 * @param modId
	 *            业务主键
	 * @param content
	 *            消息详情
	 * @param busType
	 *            业务类别代码
	 * @param shares
	 *            消息接收方
	 * @param sendWay
	 *            消息发送方式 1邮件 2短信 3微信 4微博
	 */
	public void addTodayWorks(UserInfo userInfo, Integer chargeMan, Integer modId, String content, String busType,
			List<UserInfo> shares, List<String> sendWay) {
		// 处理消息内容
		if (content.length() > 500) {
			content = content.substring(0, 500);
		}
		// 向数据更新记录列表插入数据
 		TodayWorks todayWork = new TodayWorks();
		// 企业号
		todayWork.setComId(userInfo.getComId());
		// 业务类型
		todayWork.setBusType(busType);
		// 业务主键
		todayWork.setBusId(modId);
		// 消息发送方
		todayWork.setModifyer(userInfo.getId());
		// 消息内容
		todayWork.setContent(content);
		// 不是从闹铃发送
		todayWork.setIsClock(0);
		// 闹铃主键
		todayWork.setClockId(0);
		// 设置聊天室主键
		todayWork.setRoomId(0);
		// 用于人员去重
		Set<Integer> sets = new HashSet<Integer>();
		if (!busType.equals(ConstantInterface.TYPE_MEETROOM) // 不是会议室申请
				&& !busType.equals(ConstantInterface.TYPE_MEETING)) {// 不是会议通知确认

			if (null == shares || shares.size() == 0) {// 没有分享人员，不发送消息
				return;
			}
			/**
			 * 向一个人发送待办信息
			 */
			// 添加模块最新动态
			if (null != busType && ("1".equals(busType) || // 是信息分享
					ConstantInterface.TYPE_CRM.equals(busType) || // 是客户模块
					ConstantInterface.TYPE_ITEM.equals(busType) || // 是项目模块
					ConstantInterface.TYPE_TASK.equals(busType) || // 是任务模块
					ConstantInterface.TYPE_QUES.equals(busType) || // 是问答模块
					ConstantInterface.TYPE_VOTE.equals(busType))) {// 是投票模块
				// 添加模块最新动态
				attentionService.addNewsInfo(userInfo, modId, content, busType);
			}
			if (busType.equals(ConstantInterface.TYPE_BGYP_APPLY_NOTICE)
					|| busType.equals(ConstantInterface.TYPE_BGYP_APPLY_CHECK)) {
				todayWorksDao.updateWorkToNormal(modId, userInfo.getComId(), null,
						ConstantInterface.TYPE_BGYP_APPLY_CHECK);
			} else if (busType.equals(ConstantInterface.TYPE_BGYP_BUY_NOTICE)
					|| busType.equals(ConstantInterface.TYPE_BGYP_BUY_CHECK)) {
				todayWorksDao.updateWorkToNormal(modId, userInfo.getComId(), null, ConstantInterface.TYPE_BGYP_BUY_CHECK);
			} else {
				// 查看是否有关于待办的事项(非闹铃)
				List<TodayWorks> list = todayWorksDao.getUpdateCount(userInfo.getComId(), busType, modId);
				// 将代办人员的代办事项设置成未读
				sets = this.updateTodoState(userInfo, list, chargeMan, content, sets, todayWork);
			}
			// 不给自己发送消息
			sets.add(userInfo.getId());

			// 发送普通消息
			if (null != shares && !shares.isEmpty()) {
				sets = this.addUserTodayWorks(shares, todayWork, busType, chargeMan, sets, userInfo);
			}
		} else if (ConstantInterface.TYPE_MEETING.equals(busType)) {// 是会议待办
			if (null == shares || shares.size() == 0) {// 没有分享人员，不发送消息
				return;
			} else {
				if (null != chargeMan) {
					// 有待办事项
					List<TodayWorks> list = todayWorksDao.getUpdateCount(userInfo.getComId(), busType, modId);
					if (null != list && list.size() > 0) {// 该会议的申请，没有处理
						for (TodayWorks todayWorkObj : list) {
							// 消息的接收方
							Integer userId = todayWorkObj.getUserId();
							sets.add(userId);
							todayWorkObj.setContent(content);
							if (userId.equals(userInfo.getId())) {
								// 当前操作人员已读
								todayWorkObj.setReadState(1);
							} else {
								// 非当前操作人员未读
								todayWorkObj.setReadState(0);
							}
							// 更新待办事项
							todayWorksDao.update(todayWorkObj);
						}
					}
					// 给人员发送消息
					sets = this.addUserTodayWorks(shares, todayWork, busType, chargeMan, sets, userInfo);

				} else {// 发送的是普通消息
					sets.add(userInfo.getId());
					// 给人员发送消息
					sets = this.addUserTodayWorks(shares, todayWork, busType, chargeMan, sets, userInfo);
				}
			}
		} else if (ConstantInterface.TYPE_MEETROOM.equals(busType)) {// 是会议室申请的
			if (null == shares) {// 会议室申请，发送的待办消息
				// 查看是否有关于待办的事项(非闹铃)
				List<TodayWorks> list = todayWorksDao.getUpdateCount(userInfo.getComId(), busType, modId);
				if (null != list && list.size() > 0) {// 该会议的申请，没有处理
					for (TodayWorks todayWorkObj : list) {
						// 消息的接收方
						Integer userId = todayWorkObj.getUserId();
						// 添加待办事项
						TodayWorks obj = new TodayWorks();
						BeanUtilEx.copyIgnoreNulls(todayWorkObj, obj);
						if (userId.equals(userInfo.getId())) {
							// 当前操作人员已读
							obj.setReadState(1);
						} else {
							// 非当前操作人员未读
							obj.setReadState(0);
						}
						// 消息发送方
						obj.setModifyer(userInfo.getId());
						// 消息内容
						obj.setContent(content);
						// 普通消息
						obj.setId(null);
						obj.setRecordCreateTime(null);
						todayWorksDao.add(obj);

						// 原来的设置成普通消息
						todayWorkObj.setBusSpec(0);
						// 原来的消息已读
						todayWorkObj.setReadState(1);
						// 更新待办事项
						todayWorksDao.update(todayWorkObj);
					}
				} else {// 没有发送过待办事项
					TodayWorks todayWorkN = new TodayWorks();
					BeanUtilEx.copyIgnoreNulls(todayWork, todayWorkN);
					// 消息接收方
					todayWorkN.setUserId(chargeMan);
					// 非当前操作人员未读
					todayWorkN.setReadState(0);
					// 设置消息类别 审批
					todayWorkN.setBusSpec(1);
					todayWorksDao.add(todayWorkN);
				}
			} else {// 会议室审核后，发送通知消息
				TodayWorks todayWorkN = new TodayWorks();
				BeanUtilEx.copyIgnoreNulls(todayWork, todayWorkN);
				// 消息接收方
				todayWorkN.setUserId(shares.get(0).getId());
				// 非当前操作人员未读
				todayWorkN.setReadState(0);
				// 设置消息类别 审批
				todayWorkN.setBusSpec(0);
				todayWorksDao.add(todayWorkN);

			}
		}
		// 发送方式
		if (null != sendWay) {
			for (String way : sendWay) {
				if (MsgSendWay.EMAIL.equals(way)) {// 邮件

				} else if (MsgSendWay.SMS.equals(way)) {// 短信

				} else if (MsgSendWay.WECHAT.equals(way)) {// 微信

				} else if (MsgSendWay.BLOG.equals(way)) {// 微博

				} else if (MsgSendWay.LAYMI.equals(way)) {// 及时通讯

				}
			}
		}

	}
	
	/**
	 * 给人员发送消息
	 * 
	 * @param shares
	 *            分享人员
	 * @param todayWork
	 *            待办模板
	 * @param busType
	 *            业务类型
	 * @param chargeMan
	 *            负责人
	 * @param sets
	 *            人员主键集合
	 * @param userInfo
	 * @return
	 */
	private Set<Integer> addUserTodayWorks(List<UserInfo> shares, TodayWorks todayWork, String busType,
			Integer chargeMan, Set<Integer> sets, UserInfo userInfo) {

		// 普通消息设置成已读
		todayWorksDao.updateNormalRead(userInfo, todayWork);

		for (UserInfo share : shares) {
			if (!sets.contains(share.getId())) {// 没有发送过消息的
				TodayWorks todayWorkN = new TodayWorks();
				BeanUtilEx.copyIgnoreNulls(todayWork, todayWorkN);
				// 消息接收方
				todayWorkN.setUserId(share.getId());
				// 设置消息类别 （申请加入和周报灵设置为审批）
				if (ConstantInterface.TYPE_APPLY.equals(busType)// 人员邀请
						|| ConstantInterface.TYPE_BGYP_APPLY_CHECK.equals(busType)// 办公用品申领审核
						|| ConstantInterface.TYPE_BGYP_BUY_CHECK.equals(busType)) {// 办公用品采购审核
					// 为审批
					todayWorkN.setBusSpec(1);
				} else if (ConstantInterface.TYPE_WEEK.equals(busType) || // 周报
						busType.equals(ConstantInterface.TYPE_MEETING) ||// 会议待办
						busType.equals(ConstantInterface.TYPE_DAILY)) {// 分享
					if (null == chargeMan) {// 发送普通消息，更新待办事项
						todayWorkN.setBusSpec(0);
					} else {
						todayWorkN.setBusSpec(1);
					}
				} else {
					// 其他为 普通
					todayWorkN.setBusSpec(0);
				}

				if (busType.equals(ConstantInterface.TYPE_MEETING)) {
					// 非当前操作人员未读
					if (share.getId().equals(userInfo.getId())) {
						// 当前操作人员已读
						todayWorkN.setReadState(1);
					} else {
						// 非当前操作人员未读
						todayWorkN.setReadState(0);
					}
				} else {
					// 设置是否已读
					todayWorkN.setReadState(0);
				}
				todayWorksDao.add(todayWorkN);
				// 防止重发消息
				sets.add(share.getId());
			}
		}
		return sets;
	}
	
	/**
	 * //将代办人员的代办事项设置成未读
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param list
	 *            非闹铃待办事项
	 * @param chargeMan
	 *            负责人
	 * @param content
	 *            消息内容
	 * @param sets
	 *            人员主键集合
	 * @param todayWork
	 */
	private Set<Integer> updateTodoState(UserInfo userInfo, List<TodayWorks> list, Integer chargeMan, String content,
			Set<Integer> sets, TodayWorks todayWork) {
		// 有关于待办的事项，先这些人发送消息
		if (null != list && list.size() > 0) {
			for (TodayWorks todayWorkObj : list) {
				if (null == chargeMan) {// 发送的是普通消息
					// 消息的接收方
					Integer userId = todayWorkObj.getUserId();
					// 添加待办事项
					TodayWorks obj = new TodayWorks();
					BeanUtilEx.copyIgnoreNulls(todayWorkObj, obj);

					if (userId.equals(userInfo.getId())) {
						// 当前操作人员已读
						obj.setReadState(1);
					} else {
						// 非当前操作人员未读
						obj.setReadState(0);
					}
					// 消息发送方
					obj.setModifyer(userInfo.getId());
					// 消息内容
					obj.setContent(content);
					// 普通消息
					obj.setId(null);
					obj.setRecordCreateTime(null);
					todayWorksDao.add(obj);
					sets.add(userId);

				}
				// 原来的设置成普通消息
				todayWorkObj.setBusSpec(0);
				// 原来的已读
				todayWorkObj.setReadState(1);
				// 更新待办事项
				todayWorksDao.update(todayWorkObj);
			}
		}
		// 给人员发送待办消息,且模块没有办结
		if (null != chargeMan && chargeMan > 0 && !ConstantInterface.TYPE_WEEK.equals(todayWork.getBusType())) {
			TodayWorks todayWorkN = new TodayWorks();
			BeanUtilEx.copyIgnoreNulls(todayWork, todayWorkN);
			// 消息接收方
			todayWorkN.setUserId(chargeMan);
			// 设置消息类别 审批
			todayWorkN.setBusSpec(1);
			// 设置是否已读
			if (chargeMan.equals(userInfo.getId())) {
				// 当前操作人员已读
				todayWorkN.setReadState(1);
			} else {
				// 非当前操作人员未读
				todayWorkN.setReadState(0);
			}
			todayWorksDao.add(todayWorkN);
			// 防止重发消息
			sets.add(chargeMan);
		}
		return sets;

	}
	
	/**
	 * 向指定人员推送待办事项
	 * 
	 * @param comId
	 *            团队号
	 * @param busType
	 *            业务模块类型
	 * @param busId
	 *            业务类型主键
	 * @param content
	 *            待办事项描述
	 * @param owner
	 *            待办信息所有人
	 * @param sender
	 *            待办信息推送人
	 * @param busSpec
	 *            业务类别 0普通消息; 1需办理
	 */
	public void addTodayWorks(Integer comId,String busType, Integer busId, String content, Integer owner, Integer sender,
			Integer busSpec) {
		this.delTodayWorksByOwner(comId, owner, busId, busType);//同一人的同一业务待办去重
		//新增待办工作提示
		TodayWorks todayWork = new TodayWorks();
		todayWork.setComId(comId);
		todayWork.setBusType(busType);
		todayWork.setBusId(busId);
		todayWork.setContent(content);
		todayWork.setUserId(owner);
		todayWork.setModifyer(sender);
		todayWork.setBusSpec(busSpec);
		//设置是否已读
		Integer readState = ConstantInterface.TYPE_TODAYWORKS_READSTATE_0;
		if(sender.equals(owner)){
			readState = ConstantInterface.TYPE_TODAYWORKS_READSTATE_1;
		}
		todayWork.setReadState(readState);// 新增的信息，状态肯定为未读
		todayWork.setIsClock(0);
		todayWork.setClockId(0);
		todayWork.setRoomId(0);
		todayWorksDao.add(todayWork);
	}
	
	
	/**
	 * 向指定人员推送待办事项
	 * 
	 * @param comId
	 *            团队号
	 * @param busType
	 *            业务模块类型
	 * @param busId
	 *            业务类型主键
	 * @param content
	 *            待办事项描述
	 * @param userInfos
	 *            待办信息所有人
	 * @param sender
	 *            待办信息推送人
	 * @param busSpec
	 *            业务类别 0普通消息; 1需办理
	 */
	public void addTodayWorks(Integer comId,String busType, Integer busId, String content, List<UserInfo> userInfos, Integer sender,
			Integer busSpec) {
		Set<Integer> userIds = new HashSet<Integer>();
		for (UserInfo userInfo : userInfos) {
			if(userIds.contains(userInfo.getId())){
				continue;
			}
			this.addTodayWorks(comId,busType, busId, content, userInfo.getId(), sender, busSpec);
			userIds.add(userInfo.getId());
		}
	}
	
	/**
	 * 取得用户的待办事项（单个）
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            人员主键
	 * @param busId
	 *            业务主键
	 * @param busType
	 *            业务类型
	 * @param clockId
	 *            是否为闹铃
	 * @return
	 */
	public TodayWorks getUserTodayWork(Integer comId, Integer userId, Integer busId, String busType, Integer clockId) {

		return todayWorksDao.getUserTodayWork(busId, comId, userId, busType, clockId);
	}

	/**
	 * 更新消息为已读
	 * 
	 * @param busType
	 *            业务类型标识
	 * @param busId
	 *            业务类型主键
	 * @param owner
	 *            消息所有者
	 */
	public void updateTodayWorksReadStateTo1(String busType, Integer busId, Integer owner) {
		TodayWorks todayWork = new TodayWorks();
		todayWork.setBusType(busType);
		todayWork.setBusId(busId);
		todayWork.setUserId(owner);
		todayWork.setReadState(ConstantInterface.TYPE_TODAYWORKS_READSTATE_1);// 查看后，状态肯定为已读
		todayWorksDao.update("update todayWorks a set a.readState=:readState "
				+ "where a.busType=:busType and a.busId=:busId and a.userId=:userId", todayWork);
	}

	/**
	 * 把待办事项更新为普通消息
	 * 
	 * @param busType
	 *            业务类型标识
	 * @param busId
	 *            业务类型主键
	 * @param owner
	 *            消息所有者
	 */
	public void updateTodayWorksBusSpecTo0(String busType, Integer busId, Integer owner) {
		TodayWorks todayWork = new TodayWorks();
		todayWork.setBusType(busType);
		todayWork.setBusId(busId);
		todayWork.setUserId(owner);
		todayWork.setBusSpec(ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_0);// 待办事项更新为普通消息0
		todayWorksDao.update("update todayWorks a set a.busSpec=:busSpec "
				+ "where a.busType=:busType and a.busId=:busId and a.userId=:userId", todayWork);
	}

	/**
	 * 把整个业务待办事项更新为普通消息
	 * 
	 * @param busType
	 *            业务类型标识
	 * @param busId
	 *            业务类型主键
	 */
	public void updateTodayWorksBusSpecTo0(String busType, Integer busId) {
		TodayWorks todayWork = new TodayWorks();
		todayWork.setBusType(busType);
		todayWork.setBusId(busId);
		todayWork.setBusSpec(ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_0);// 待办事项更新为普通消息0
		todayWorksDao.update("update todayWorks a set a.busSpec=:busSpec " + "where a.busType=:busType and a.busId=:busId",
				todayWork);
	}
	
	/**
	 * 把整个业务待办事项更新为普通消息并且标记为已读
	 * 
	 * @param busType
	 *            业务类型标识
	 * @param busId
	 *            业务类型主键
	 */
	public void updateTodayWorksBusSpecTo0ReadTo1(String busType, Integer busId) {
		TodayWorks todayWork = new TodayWorks();
		todayWork.setBusType(busType);
		todayWork.setBusId(busId);
		todayWork.setBusSpec(ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_0);// 待办事项更新为普通消息0
		todayWork.setReadState(ConstantInterface.TYPE_TODAYWORKS_READSTATE_1);
		todayWorksDao.update("update todayWorks a set a.busSpec=:busSpec,a.readState=:readState " + "where a.busType=:busType and a.busId=:busId",
				todayWork);
	}

	/**
	 * 删除已读的普通消息
	 */
	public void delUselessTodayWorks() {
		loger.info("开始删除了！");
		todayWorksDao.delByField("todayWorks", new String[] { "busSpec", "readState" }, new Object[] {
				ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_0, ConstantInterface.TYPE_TODAYWORKS_READSTATE_1 });
	}
	
	/**
	 * 添加催办待办
	 * @param busRemind
	 * @param repUserMap
	 * @throws Exception
	 */
	public void sendBusReminderMsg(BusRemind busRemind, Map<Integer, ForMeDo> repUserMap) throws Exception {
		List<BusRemindUser> listBusRemindUser = busRemind.getListBusRemindUser();
		//向数据更新记录列表插入数据
		TodayWorks todayWork  = new TodayWorks();
		//业务类型
		todayWork.setBusType(ConstantInterface.TYPE_REMINDER);
		//不是从闹铃发送
		todayWork.setIsClock(0);
		//闹铃主键
		todayWork.setClockId(0);
		//设置聊天室主键
		todayWork.setRoomId(0);
		
		Set<Integer> remindUsers = new HashSet<Integer>();
		
		if(null!=busRemind  && null!=listBusRemindUser && !listBusRemindUser.isEmpty()){
			
			todayWork.setModifyer(busRemind.getUserId());
			todayWork.setComId(busRemind.getComId());
			todayWork.setBusId(busRemind.getId());
			todayWork.setReadState(0);
			todayWork.setBusSpec(1);
			todayWork.setContent(busRemind.getContent());
			for (BusRemindUser reminderUser : listBusRemindUser) {
				//添加待办事项
				TodayWorks obj = new TodayWorks();
				BeanUtilEx.copyIgnoreNulls(todayWork, obj);
				
				//提醒人
				Integer userId = reminderUser.getUserId();
				//代理人员
				ForMeDo insteadUser = repUserMap.get(userId);
				if(null != insteadUser){
					Integer repUserId = insteadUser.getUserId();
					if(!remindUsers.contains(repUserId)){
						//提醒人
						obj.setUserId(repUserId);
						todayWorksDao.add(obj);
						
					}
				}else{
					if(!remindUsers.contains(userId)){
						//提醒人
						obj.setUserId(userId);
						todayWorksDao.add(obj);
					}
				}
				
			}
		} else {
			throw new Exception("催办信息异常!");
		}

		
	}
	
	/**
	 * 将待办事项设置成普通事项
	 * 
	 * @param busId
	 *            业务主键
	 * @param comId
	 *            企业号
	 * @param userId
	 *            用户主键
	 * @param busType
	 *            业务类型
	 */
	public void updateWorkToNormal(Integer busId, Integer comId, Integer userId, String busType) {
		todayWorksDao.updateWorkToNormal(busId, comId, userId, busType);

	}
	
	/**
	 * 发送待办消息
	 * 
	 * @param modifyer
	 * @param modId
	 * @param content
	 * @param busType
	 * @param shares
	 */
	public void updateTodayWorks(Integer modifyer, Integer modId, String content, String busType,
			List<UserInfo> shares) {
		// 处理消息内容
		if (content.length() > 500) {
			content = content.substring(0, 500);
		}
		TodayWorks todayWork = new TodayWorks();
		for (UserInfo share : shares) {
			// 业务类型
			todayWork.setBusType(busType);
			// 业务主键
			todayWork.setBusId(modId);
			// 消息发送方
			todayWork.setModifyer(modifyer);
			// 消息内容
			todayWork.setContent(content);
			// 闹铃主键
			todayWork.setIsClock(0);
			todayWork.setClockId(0);

			// 设置聊天室主键
			todayWork.setRoomId(0);
			// 待办消息
			todayWork.setBusSpec(1);
			todayWork.setReadState(0);
			// 接收人
			todayWork.setUserId(share.getId());
			// 接收人单位主键
			todayWork.setComId(share.getComId());
			todayWorksDao.add(todayWork);
		}
	}
	
	/**
	 * 发送通知类消息
	 * 
	 * @param comId
	 *            单位主键
	 * @param busType
	 *            通知类型
	 * @param userId
	 *            消息接收人
	 * @param modifyer
	 *            消息发送人
	 * @param content
	 *            消息内容
	 */
	public void sendMsgForOne(Integer comId, Integer busId, String busType, Integer userId, Integer modifyer,
			String content) {
		// 向数据更新记录列表插入数据
		TodayWorks todayWork = new TodayWorks();
		// 企业号
		todayWork.setComId(comId);
		// 业务主键
		todayWork.setBusId(busId);
		// 业务类型
		todayWork.setBusType(busType);
		// 消息接受方
		todayWork.setUserId(userId);
		// 消息发送方
		todayWork.setModifyer(modifyer);
		// 消息内容
		todayWork.setContent(content);
		// 设置消息类别
		todayWork.setBusSpec(0);
		// 非当前操作人员未读
		todayWork.setReadState(0);
		// 不是从闹铃发送
		todayWork.setIsClock(0);
		// 闹铃主键
		todayWork.setClockId(0);
		// 设置聊天室主键
		todayWork.setRoomId(0);
		if (!modifyer.equals(userId) ) {// 不给自己发消息
			todayWorksDao.add(todayWork);
		}
	}
	
	/**
	 * 获取所有的未读信息
	 * 
	 * @param todayWorks
	 * @param comId
	 * @param userId
	 * @param modList
	 * @return
	 */
	public List<TodayWorks> listMsgNoRead(TodayWorks todayWorks, Integer comId, Integer userId, List<String> modList) {
		List<TodayWorks> list = todayWorksDao.listMsgNoRead(todayWorks, comId, userId, modList, null);
		return list;
	}
	
	/**
	 * 设置今日提醒已读
	 * 
	 * @param modId
	 *            模块主键
	 * @param comId
	 *            企业号
	 * @param userId
	 *            提醒的阅读者 null为所有人
	 * @param busType
	 *            模块类型
	 * @param clockId
	 *            闹铃主键 0非闹铃
	 */
	public void updateTodoWorkRead(Integer modId, Integer comId, Integer userId, String busType, Integer clockId) {
		if (null != userId && !busType.equals(ConstantInterface.TYPE_APPLY)) {// 不是用户申请加入
			// 设置消息提醒内容
			TodayWorks todayWorks = new TodayWorks();
			// 若是周报、分享、项目和客户，则需要待办已读和所有未读消息
			if (busType.equals(ConstantInterface.TYPE_WEEK)// 周报
					|| busType.equals(ConstantInterface.TYPE_INSTITUTION)// 制度
					|| busType.equals(ConstantInterface.TYPE_ITEM)// 项目
					|| busType.equals(ConstantInterface.TYPE_PRODUCT)// 产品
					|| busType.equals(ConstantInterface.TYPE_DAILY)// 分享
					|| busType.equals(ConstantInterface.TYPE_CRM)// 客户
					|| busType.equals("1")// 分享
					|| busType.equals(ConstantInterface.TYPE_REMINDER)//催办
			) {
				todayWorks.setBusSpec(1);
			} else {
				// 设置未读
				todayWorks.setReadState(0);
			}
			todayWorks.setBusId(modId);
			List<String> modList = new ArrayList<String>();
			if ("1".equals(busType)) {
				busType = "100";
			}
			modList.add(busType);
			// 列出用户该模块下未读消息
			List<TodayWorks> listWork = todayWorksDao.listMsgNoRead(todayWorks, comId, userId, modList, busType);
			if (null != listWork) {// 有未读消息
				for (TodayWorks workObj : listWork) {
					if (!(busType.equals(ConstantInterface.TYPE_TASK)// 任务待办，不需要设置成普通事项
							|| busType.equals(ConstantInterface.TYPE_FLOW_SP)// 审批，不需要设置成普通事项
							|| busType.equals(ConstantInterface.TYPE_MEETROOM)// 会议申请，不需要设置成普通事项
							|| busType.equals(ConstantInterface.TYPE_MEETING)// 会议确认，不需要设置成普通事项
							|| busType.equals(ConstantInterface.TYPE_BGYP_BUY_CHECK)// 采购申请审核
							|| busType.equals(ConstantInterface.TYPE_BGYP_APPLY_CHECK)
							|| busType.equals(ConstantInterface.TYPE_FINALCIAL_BALANCE))// 财务结算
							&& workObj.getBusSpec() == 1) {
						workObj.setBusSpec(0);
					}
					// 设置普通已读
					workObj.setReadState(1);
					todayWorksDao.update(workObj);
				}
			}
		} else if (null != userId && busType.equals(ConstantInterface.TYPE_APPLY)) {// 用户申请加入
			TodayWorks todayWorks = this.getUserTodayWork(modId, comId, userId, busType, clockId);
			if (null != todayWorks) {
				todayWorks.setReadState(1);
				todayWorksDao.update(todayWorks);
			}
		} else {
			// 删除数据更新记录(该业务发送多人，已有一人处理)用户申请加入
			todayWorksDao.delByField("todayWorks", new String[] { "comId", "busType", "busId" },
					new Object[] { comId, busType, modId });

		}
	}
	
	/**
	 * 全部标识已读
	 * 
	 * @param todayWorks
	 * @param modList
	 */
	public void updateReadAllWorks(TodayWorks todayWorks, List<String> modList) {
		// 获取所有满足条件消息提醒列表（包括闹铃）
		List<TodayWorks> list = todayWorksDao.listMsgNoRead(todayWorks, todayWorks.getComId(), todayWorks.getUserId(),
				modList, "all");
		if (null != list && list.size() > 0) {
			for (TodayWorks todayWork : list) {
				if (null != todayWork) {// 待办事项设置已读
					if (todayWork.getReadState() == 0) {
						todayWork.setReadState(1);
						todayWorksDao.update(todayWork);
					}
				}
			}

		}

	}
	

	/**
	 * 删除不是自己权限范围的消息提醒
	 * 
	 * @param modId
	 *            模块主键
	 * @param comId
	 *            企业号
	 * @param userId
	 *            消息提醒人员
	 * @param busType
	 *            业务类别
	 */
	public void delTodoWork(Integer modId, Integer comId, Integer userId, String busType, String busSpec) {
		if (null == busSpec) {
			// 删除数据更新记录（定时提醒已查看）
			todayWorksDao.delByField("todayWorks", new String[] { "comId", "busType", "busId", "userId" },
					new Object[] { comId, busType, modId, userId });
		} else {
			// 删除数据更新记录（定时提醒已查看）
			todayWorksDao.delByField("todayWorks", new String[] { "comId", "busType", "busId", "userId", "busSpec" },
					new Object[] { comId, busType, modId, userId, busSpec });
		}
	}
	/**
	 * 删除模块所有待办信息
	 * @param busId
	 * @param comId
	 * @param busType
	 * @param busSpec
	 */
	public void delTodoWork(Integer busId, Integer comId, String busType, String busSpec) {
		if (null == busSpec) {
			// 删除数据更新记录（定时提醒已查看）
			todayWorksDao.delByField("todayWorks", new String[] { "comId", "busType", "busId", },
					new Object[] { comId, busType, busId });
		} else {
			// 删除数据更新记录（定时提醒已查看）
			todayWorksDao.delByField("todayWorks", new String[] { "comId", "busType", "busId", "busSpec" },
					new Object[] { comId, busType, busId, busSpec });
		}
	}
	
	/**
	 * 发送通知类消息
	 * 
	 * @param comId
	 *            团队主键
	 * @param busType
	 *            通知类型
	 * @param userId
	 *            消息接收人
	 * @param modifyer
	 *            消息发送人
	 * @param content
	 *            消息内容
	 */
	public void sendMsgForOne(Integer comId, String busType, Integer userId, Integer modifyer, String content) {
		// 向数据更新记录列表插入数据
		TodayWorks todayWork = new TodayWorks();
		// 企业号
		todayWork.setComId(comId);
		// 业务类型
		todayWork.setBusType(busType);
		// 业务主键
		todayWork.setBusId(0);
		// 消息接受方
		todayWork.setUserId(userId);
		// 消息发送方
		todayWork.setModifyer(modifyer);
		// 消息内容
		todayWork.setContent(content);
		// 设置消息类别 审批
		todayWork.setBusSpec(0);
		// 非当前操作人员未读
		todayWork.setReadState(0);
		// 不是从闹铃发送
		todayWork.setIsClock(0);
		// 闹铃主键
		todayWork.setClockId(0);
		// 设置聊天室主键
		todayWork.setRoomId(0);

		todayWorksDao.add(todayWork);
	}
	
	/**
	 * 通过用户配置的信息发送消息
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param modId
	 *            模块主键
	 * @param content
	 *            消息内容
	 * @param busType
	 *            业务主键
	 * @param shares
	 *            消息接收人员
	 */
	public void sendMsgByUserConf(UserInfo userInfo, Integer modId, String content, String busType,
			List<UserInfo> shares, String[] sendWay) {
		Set<String> sendWays = new HashSet<String>();
		if (null != sendWay && sendWay.length > 0) {
			for (int i = 0; i < sendWay.length; i++) {
				String sendWayObj = sendWay[i];
				if (sendWayObj.equals("0")) {// 及时通讯
					sendWays.add("2");
				}
			}
		}
		if (null == sendWays || sendWays.size() == 0) {
			return;
		}
		// 用于人员去重
		Set<Integer> sets = new HashSet<Integer>();
		if (ConstantInterface.TYPE_TASK.equals(busType)) {
			if (null != shares) {
				sets.add(userInfo.getId());
				for (UserInfo userObj : shares) {

					Integer toUserId = userObj.getId();
					if (sets.contains(toUserId)) {
						continue;
					} else {
						sets.add(toUserId);
					}
					List<UserConf> listUserConf = userConfService.listUserMsgWayConf(userInfo.getComId(), toUserId);
					if (null != listUserConf && listUserConf.size() > 0) {
						for (UserConf userConf : listUserConf) {
							// 消息发送类型
							String uConfType = userConf.getSysConfCode();
							// 是否开启
							Integer openState = userConf.getOpenState();
							if (openState == 1 && sendWays.contains(uConfType)) {// 消息接收方，开启了本次设置的消息传递方式
								if (uConfType.equals("2") && !userInfo.getId().equals(toUserId)) {// 是即时通讯
									// chatRoomJob.sendMsg(content, userInfo, toUserId);
								}
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 排在待办事项列表前五条数据集合
	 * 
	 * @param userInfo
	 * @param num
	 * @return
	 */
	public List<TodayWorks> firstNWorkList(UserInfo userInfo, Integer num) {
		List<TodayWorks> list = todayWorksDao.firstNWorkList(userInfo, num);
		return list;
	}
	/**
	 * 获取全部消息提醒数目
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            当前用户主键
	 * @return
	 */
	public Integer countTodo(Integer comId, Integer userId) {
//		Integer nums = todayWorksDao.countTodo(comId, userId);
		Integer nums = 0;
		List<MsgNoRead> modTodoList = this.countToDoByType(comId,userId);
		if(!CommonUtil.isNull(modTodoList)) {
			for (MsgNoRead msgNoRead : modTodoList) {
				nums+=msgNoRead.getNoReadNum();
			}
		}
		return nums;
	}
	
	/**
	 * 关注信息未读数
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            操作人员
	 * @return
	 */
	public Integer countAttenNoRead(Integer comId, Integer userId) {
		Integer nums = todayWorksDao.countAttenNoRead(comId, userId);
		return nums;
	}
	/**
	 * 首页待办点击后补充数据
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param num
	 *            页面需要展示的数据条数
	 * @param todoIds
	 *            页面点击前的待办主键
	 * @param leftNum
	 *            页面点击后剩下的数据
	 * @return
	 */
	public List<TodayWorks> otherNWorkList(UserInfo userInfo, Integer num, String todoIds, Integer leftNum) {
		List<TodayWorks> list = todayWorksDao.otherNWorkList(userInfo, num, todoIds, leftNum);
		return list;
	}
	/**
	 * 闹铃发送消息
	 * 
	 * @param clock
	 *            闹铃
	 * @param listClockWays
	 */
	public void updateTodayWork(Clock clock, List<ClockWay> listClockWays) {

		// 业务主键
		Integer busId = clock.getBusId();
		// 所在企业
		Integer comId = clock.getComId();
		// 人员
		Integer userId = clock.getUserId();
		// 业务类型
		String busType = clock.getBusType();

		String clockMsgType = clock.getClockMsgType();

		String content = clock.getContent();
		if (!clock.getBusType().equals(ConstantInterface.TYPE_MEETING)) {// 不是会议
			content = "定时提醒 " + content;
		}
		// 处理消息内容
		if (content.length() > 500) {
			content = content.substring(0, 500);
		}

		Integer clockId = clock.getId();
		List<ClockPerson> clockPersons = clock.getClockPersons();
		Set<Integer> userIds = new HashSet<Integer>();
		if (null != clockPersons && !clockPersons.isEmpty()) {
			for (ClockPerson person : clockPersons) {
				if (userIds.contains(person.getUserId())) {// 若是已经发送过闹铃，则中断
					continue;
				}
				TodayWorks todayWorks = new TodayWorks();
				// 企业号
				todayWorks.setComId(comId);
				// 业务类型
				todayWorks.setBusType(busType);
				// 业务主键
				todayWorks.setBusId(busId);
				// 接收人主键
				todayWorks.setUserId(person.getUserId());
				// 更新人主键
				todayWorks.setModifyer(userId);
				// 未读
				todayWorks.setReadState(0);
				// 消息内容
				todayWorks.setContent(content);
				// 从闹铃发送
				if (busType.equals(ConstantInterface.TYPE_MEETING)) {
					todayWorks.setIsClock(0);
				} else {
					todayWorks.setIsClock(1);
				}
				if (null == clockMsgType || "0".equals(clockMsgType)) {// 普通提醒
					// 普通提醒
					todayWorks.setBusSpec(0);
				} else {
					if (busType.equals(ConstantInterface.TYPE_MEETING)) {
						MeetCheckUser meetCheckUser = meetingService.checkMeetUser(comId, person.getUserId(), busId);
						if (null == meetCheckUser) {
							// 待办提醒
							todayWorks.setBusSpec(1);
						} else {
							// 普通提醒
							todayWorks.setBusSpec(0);
						}
					} else {
						// 待办提醒
						todayWorks.setBusSpec(1);
					}
				}
				// 闹铃主键
				todayWorks.setClockId(clockId);
				// 设置聊天室主键
				todayWorks.setRoomId(0);
				Integer todoId = todayWorksDao.add(todayWorks);
				if (null != clockMsgType && !"0".equals(clockMsgType)) {// 普通提醒
					JpushUtils.sendTodoMessage(comId, person.getUserId(), -1, todoId, busId, busType, clockId, content);
				}
			}
		}

		// 发送方式
		if (null != listClockWays) {
			for (ClockWay way : listClockWays) {
				if (MsgSendWay.EMAIL.equals(way.getSendWay())) {// 邮件

				} else if (MsgSendWay.SMS.equals(way.getSendWay())) {// 短信

				} else if (MsgSendWay.WECHAT.equals(way.getSendWay())) {// 微信

				} else if (MsgSendWay.BLOG.equals(way.getSendWay())) {// 微博

				}
			}
		}
	}
	
	/**
	 * 设置已读
	 * 
	 * @param todayWorks
	 */
	public void updateTodayObj(TodayWorks todayWorks) {
		todayWorksDao.update(todayWorks);
	}
	/**
	 * 取得待办事项的详情
	 * 
	 * @param id
	 *            待办事项的主键
	 * @param busId
	 *            业务类型
	 * @param busType
	 *            业务主键
	 * @param userInfo
	 *            当前操作人员
	 * @return
	 */
	public TodayWorks getMsgTodoById(Integer id, String busType, Integer busId, UserInfo userInfo) {
		TodayWorks todo = todayWorksDao.getMsgTodoById(id, busType, busId, userInfo);
		return todo;
	}
	
	/**
	 * 直接删除待办事项
	 * 
	 * @param id
	 *            待办事项主键
	 */
	public void delTodayWorkById(Integer id) {
		todayWorksDao.delById(TodayWorks.class, id);
	}
	
	/**
	 * 取得未读信息
	 * 
	 * @param userInfo
	 * @return
	 */
	public TodayWorks getMsgNoRead(UserInfo userInfo) {
		TodayWorks todayWork = todayWorksDao.getMsgNoRead(userInfo);
		return todayWork;
	}
	
	/**
	 * 添加消息提醒
	 * 
	 * @param comId
	 * @param roomId
	 * @param userId
	 * @param busId
	 * @param busType
	 * @param content
	 */
	public void addChatRemind(Integer comId, Integer roomId, Integer userId, Integer busId, String busType,
			String content) {
		// 查询是否有未读聊天消息
		TodayWorks todayWork = todayWorksDao.getTodayWorkByRoomId(comId, roomId, userId);
		if (null != todayWork) {
			todayWork.setContent(content);
			todayWorksDao.update(todayWork);
		} else {
			todayWork = new TodayWorks();
			// 企业号
			todayWork.setComId(comId);
			// 业务类型
			todayWork.setBusType(busType);
			// 业务主键
			todayWork.setBusId(busId);
			// 发送人主键
			todayWork.setUserId(userId);
			// 更新人主键
			todayWork.setModifyer(userId);
			// 未读
			todayWork.setReadState(0);
			// 消息内容
			todayWork.setContent(content);
			// 不从闹铃发送
			todayWork.setIsClock(0);
			// 闹铃主键
			todayWork.setClockId(0);
			// 聊天提醒
			todayWork.setBusSpec(2);
			// 聊天提醒
			todayWork.setRoomId(roomId);
			// 添加记录
			todayWorksDao.add(todayWork);
		}

	}
	
	/**
	 * 获取消息提醒分页列表
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            用户主键
	 * @param modList
	 *            模块集合
	 * @return
	 */
	public List<TodayWorks> listPagedMsgNoRead(TodayWorks todayWorks, Integer comId, Integer userId,
			List<String> modList) {
		//罗健临时应对
//		List<TodayWorks> list = todayWorksDao.listPagedMsgNoRead(todayWorks, comId, userId, modList);
		return null;
	}
	
	/**
	 * 获取代办事项分页列表
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            用户主键
	 * @param modList
	 *            模块
	 * @return
	 */
	public List<TodayWorks> listPagedMsgTodo(TodayWorks todayWorks, Integer comId, Integer userId,
			List<String> modList) {
		List<TodayWorks> list = todayWorksDao.listPagedMsgTodo(todayWorks, comId, userId, modList);
		return list;
	}
	
	/**
	 * 获取所有待办事项
	 * 
	 * @param todayWorks
	 * @param comId
	 * @param userId
	 * @return
	 */
	public List<TodayWorks> listWorksTodo(TodayWorks todayWorks, Integer comId, Integer userId) {
		List<TodayWorks> list = todayWorksDao.listWorksTodo(todayWorks, comId, userId);
		return list;
	}
	
	/**
	 * 获取模块消息提醒数目
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            当前用户主键
	 * @return
	 */
	public List<MsgNoRead> countToDoByType(Integer comId, Integer userId) {
		List<MsgNoRead> nums = todayWorksDao.countToDoByType(comId, userId);
		return nums;
	}
	
	/**
	 * 获取消息提醒数目
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            当前用户主键
	 * @return
	 */
	public List<MsgNoRead> countNoReadByType(Integer comId, Integer userId) {
		//List<MsgNoRead> listNoReadNum = todayWorksDao.countNoReadByType(comId, userId);
		List<MsgNoRead> nums = todayWorksDao.countToDoByType(comId, userId);
		return nums;
	}
	
	/**
	 * 分模块更新未读
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            操作人员
	 * @return
	 */
	public List<MsgNoRead> countAttenNoReadByType(Integer comId, Integer userId) {
		List<MsgNoRead> nums = todayWorksDao.countAttenNoReadByType(comId, userId);
		return nums;
	}
	
	/**
	 * 获取消息提醒数目
	 * 
	 * @param comId
	 *            企业号
	 * @param userId
	 *            当前用户主键
	 * @return
	 */
	public Integer countNoRead(Integer comId, Integer userId) {
		Integer nums = todayWorksDao.countNoRead(comId, userId);
		return nums;
	}
	
	/**
	 * 标识已读
	 * 
	 * @param ids
	 * @param userInfo
	 */
	public void updateReadWorks(Integer[] ids, UserInfo userInfo) {
		if (null != ids && ids.length > 0) {
			for (Integer id : ids) {
				// 查询今日提醒
				TodayWorks todayWork = (TodayWorks) todayWorksDao.objectQuery(TodayWorks.class, id);
				if (null != todayWork) {// 设置已读
					if (todayWork.getReadState() == 0) {
						todayWork.setReadState(1);
						todayWorksDao.update(todayWork);
					}
				}
			}
		}

	}
	


	/**
	 * 标识已读（待办事项标识已读，闹铃待办直接删除）
	 * 
	 * @param id
	 * @param userInfo
	 */
	public void updateReadTodayWork(Integer id, UserInfo userInfo) {
		if (null != id && id > 0) {
			// 查询今日提醒
			TodayWorks todayWork = (TodayWorks) todayWorksDao.objectQuery(TodayWorks.class, id);
			if (null != todayWork) {
				todayWork.setReadState(1);
				todayWorksDao.update(todayWork);
			}
		}

	}
	/**
	 * 依据人员清除待办工作提醒
	 * @param comId 团队主键
	 * @param owner 所有人主键
	 * @param busId 业务主键
	 * @param busType 业务类型
	 */
	public void delTodayWorksByOwner(Integer comId,Integer owner,Integer busId,String busType) {
		todayWorksDao.delByField("todayWorks", new String[] {"comId","userId","busId","busType"},
				new Object[] {comId,owner,busId,busType});// 删除待办工作提醒
		
	}
	/**
	 * 依据业务主键清除待办工作提醒
	 * @param comId 团队主键
	 * @param busId 业务主键
	 * @param busType 业务类型
	 */
	public void delTodayWorksByBusId(Integer comId,Integer busId,String busType) {
		todayWorksDao.delByField("todayWorks", new String[] {"comId","busId","busType"},
				new Object[] {comId,busId,busType});
		
	}
}
