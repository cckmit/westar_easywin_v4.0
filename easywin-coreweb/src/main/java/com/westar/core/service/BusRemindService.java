package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.westar.base.model.SpFlowCurExecutor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.BusRemind;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.ForMeDo;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.BusRemindDao;
import com.westar.core.thread.sendPhoneMsgThread;
import com.westar.core.web.PaginationContext;

@Service
public class BusRemindService {

	@Autowired
	BusRemindDao busRemindDao;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	PhoneMsgService phoneMsgService;
	
	@Autowired
	ForMeDoService forMeDoService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	/**
	 * 查询单个人员在指定模块的催办数
	 * @param userInfo 当前操作人员
	 * @param busRemind 催办条件查询
	 * @return
	 */
	public PageBean<BusRemind> listPagedBusRemindForBus(UserInfo userInfo, BusRemind busRemind){
		return busRemindDao.listPagedBusRemindForBus(userInfo,busRemind);
	}
	/**
	 * 添加催办信息
	 * @param busRemind
	 * @param userInfo
	 * @throws Exception 
	 */
	public void addBusRemind(BusRemind busRemind, UserInfo userInfo) throws Exception {
		busRemind.setUserId(userInfo.getId());
		busRemind.setComId(userInfo.getComId());
		
		Integer busReminderId = busRemindDao.add(busRemind);
		busRemind.setId(busReminderId);
		
		//代理人员集合
		List<ForMeDo> insteadUsers = forMeDoService.listInsteadUser(userInfo.getComId());
		//离岗人员
		Map<Integer,ForMeDo> repUserMap = new HashMap<>();
		if(null!=insteadUsers && !insteadUsers.isEmpty()){
			for (ForMeDo insteadUser : insteadUsers) {
				repUserMap.put(insteadUser.getCreator(), insteadUser);
			}
		}
		
		//添加催办人员信息
		 this.addbusRemindusers(busRemind, userInfo,repUserMap);
		//发送待办信息
		 todayWorksService.sendBusReminderMsg(busRemind,repUserMap);
		
		//短信发送标识
		String msgSendFlag = busRemind.getMsgSendFlag();
		if(!StringUtils.isEmpty(msgSendFlag) && msgSendFlag.equals(ConstantInterface.MSG_SEND_YES)){
			String modTypeName = CommonUtil.dataDicZvalueByCode("moduleType", busRemind.getBusType());
			
			if(!StringUtils.isEmpty(modTypeName)){
				modTypeName = modTypeName.substring(0,2);
			}else{
				modTypeName = busRemind.getBusType();
			}
			
			//此处进行催办信息发送
			List<UserInfo> userList = busRemindDao.listRemindUser(userInfo.getComId(),busReminderId);
			//单线程池
			ExecutorService pool = ThreadPoolExecutor.getInstance();
			if(ConstantInterface.TYPE_CRM.equals(busRemind.getBusType())){
				modTypeName = "";
			}
			//跟范围人员发送通知消息
			pool.execute(new sendPhoneMsgThread(phoneMsgService, userInfo.getComId(), userList, 
					new Object[]{modTypeName,busRemind.getContent()}, ConstantInterface.MSG_JOB_TO_DO,userInfo.getOptIP()));
		}
	}
	/**
	 * 批量添加催办信息
	 * @param busRemind
	 * @param userInfo
	 * @param insIds 
	 * @throws Exception
	 */
	public void addBusReminds(BusRemind busRemind, UserInfo userInfo, String[] insIds) throws Exception {
		for (String insId : insIds) {
			busRemind.setBusId(Integer.parseInt(insId));
			//查询该审批的所有当前审批人
			List<SpFlowCurExecutor> spFlowCurExecutors = workFlowService.getSpFlowCurExecutorByExecuteType(userInfo.getComId(),Integer.parseInt(insId),"assignee");

			SpFlowInstance spFlowInstance = (SpFlowInstance) busRemindDao.objectQuery(SpFlowInstance.class,Integer.parseInt(insId));
			busRemind.setBusModName(spFlowInstance.getFlowName());
			List<BusRemindUser> lists = new ArrayList<>();
			//添加被催办人集合
			for(SpFlowCurExecutor spFlowCurExecutor : spFlowCurExecutors){
				BusRemindUser busRemindUser = new BusRemindUser();
				busRemindUser.setUserId(spFlowCurExecutor.getExecutor());
				lists.add(busRemindUser);
			}
			busRemind.setListBusRemindUser(lists);
			//添加催办
			this.addBusRemind(busRemind, userInfo);
		}
	}
	/**
	 * 添加催办提醒人员
	 * @param busRemind
	 * @param userInfo
	 * @param repUserMap 
	 * @return
	 */
	private Set<Integer> addbusRemindusers(BusRemind busRemind, UserInfo userInfo,
			Map<Integer, ForMeDo> repUserMap) {
		Integer busReminderId = busRemind.getId();
		Set<Integer> remindUsers = new HashSet<Integer>();
		List<BusRemindUser> listBusRemindUser = busRemind.getListBusRemindUser();
		if(null!=listBusRemindUser && !listBusRemindUser.isEmpty()){
			
			
			
			for (BusRemindUser busRemindUser : listBusRemindUser) {
				//提醒人
				Integer userId = busRemindUser.getUserId();
				
				//代理人员
				ForMeDo insteadUser = repUserMap.get(userId);
				if(null != insteadUser){
					Integer repUserId = insteadUser.getUserId();
					if(!remindUsers.contains(repUserId)){
						//添加代理人员
						busRemindUser.setUserId(repUserId);
						busRemindUser.setComId(userInfo.getComId());
						busRemindUser.setBusRemindId(busReminderId);
						busRemindDao.add(busRemindUser);
						
						remindUsers.add(repUserId);
					}
				}
				if(!remindUsers.contains(userId)){
					//添加代理人员
					busRemindUser.setComId(userInfo.getComId());
					busRemindUser.setBusRemindId(busReminderId);
					busRemindDao.add(busRemindUser);
					remindUsers.add(userId);
				}
			}
		}
		
		return remindUsers;
	}

	/**
	 * 查询催办信息
	 * @param sessionUser 当前操作员
	 * @param busRemindId 催办主键
	 * @return
	 */
	public BusRemind queryBusRemindById(UserInfo sessionUser,Integer busRemindId) {
		BusRemind taskReminder = new BusRemind();
		taskReminder.setId(busRemindId);
		taskReminder = busRemindDao.queryTaskRemind(taskReminder);
		if(null!=taskReminder){
			List<BusRemindUser> reminderUsers = busRemindDao.listBusRemindUser(sessionUser,busRemindId);
			taskReminder.setListBusRemindUser(reminderUsers);
		}
		return taskReminder;
	}

	/**
	 * 查询个人被催办记录信息
	 * @param busRemind
	 * @param userInfo
	 * @return
	 */
	public PageBean<BusRemind> listPagedSelfBusRemind(BusRemind busRemind,
			UserInfo userInfo) {
		
		List<BusRemind> recordList = busRemindDao.listPagedSelfBusRemind(busRemind,userInfo);
		
		PageBean<BusRemind> pageBean = new PageBean<BusRemind>();
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		pageBean.setRecordList(recordList);
		return pageBean;
	}
	/**
	 * 查询个人被催办记录信息
	 * @param busRemind
	 * @param userInfo
	 * @return
	 */
	public PageBean<BusRemind> listPagedBusRemindForMod(BusRemind busRemind,
			UserInfo userInfo) {
		PageBean<BusRemind> pageBean = busRemindDao.listPagedBusRemindForBus(userInfo,busRemind);
		return pageBean;
	}
	
}
