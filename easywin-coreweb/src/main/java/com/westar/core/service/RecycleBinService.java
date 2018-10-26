package com.westar.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Announcement;
import com.westar.base.model.Customer;
import com.westar.base.model.Item;
import com.westar.base.model.Question;
import com.westar.base.model.RecycleBin;
import com.westar.base.model.Task;
import com.westar.base.model.UserInfo;
import com.westar.base.model.Vote;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.RecycleBinDao;

@Service
public class RecycleBinService {

	@Autowired
	RecycleBinDao recycleBinDao;
	
	@Autowired
	CrmService crmService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	QasService qasService;
	
	@Autowired
	VoteService voteService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TodayWorksService todayWorksService;

	@Autowired
	AnnouncementService announcementService;
	/**
	 * 获取预删除的数据分页列表
	 * @param recycleBin回收站属性条件
	 * @param modList 模块集合
	 * @return
	 */
	public List<RecycleBin> listPagedPreDel(RecycleBin recycleBin, List<String> modList) {
		
		return recycleBinDao.listPagedPreDel(recycleBin,modList);
	}

	/**
	 * 操作删除对象
	 * @param ids 删除对象
	 * @param state 操作1 删除0还原
	 * @param userInfo 
	 * @throws Exception 
	 */
	public void updateObject(String[] ids, String state, UserInfo userInfo) throws Exception {
		if("1".equals(state)){//删除
			//客户类主键
			List<Integer> crmIdList = new ArrayList<Integer>();
			//项目类主键
			List<Integer> itemIdList = new ArrayList<Integer>();
			
			//任务类主键
			List<Integer> taskIdList = new ArrayList<Integer>();
			for (String objs : ids) {
				String[] obj = objs.split("@");
				//回收箱内容主键
				Integer recycleBinId = Integer.parseInt(obj[0]);
				//业务主键
				Integer busId = Integer.parseInt(obj[1]);
				//业务类类型
				String busType = obj[2];
				//删除回收箱数据
				recycleBinDao.delById(RecycleBin.class, recycleBinId);
				if(ConstantInterface.TYPE_CRM.equals(busType)){
					
					crmIdList.add(busId);
					
				}else if(ConstantInterface.TYPE_ITEM.equals(busType)){
					
					itemIdList.add(busId);
					
				}else if(ConstantInterface.TYPE_TASK.equals(busType)){
					
					taskIdList.add(busId);
					
				}else if(ConstantInterface.TYPE_QUES.equals(busType)){
					
					qasService.delQues(busId, userInfo);
					
				}else if(ConstantInterface.TYPE_VOTE.equals(busType)){
					voteService.delVote(busId, userInfo);
				}else if(ConstantInterface.TYPE_ANNOUNCEMENT.equals(busType)){
					announcementService.delAnnoun(busId, userInfo);
				}
			}
			if(null != crmIdList && crmIdList.size()>0){
				crmService.delCustomer(crmIdList, userInfo);
			}
			if(null != itemIdList && itemIdList.size()>0){
				itemService.delItem(itemIdList, userInfo);
			}
			if(null != taskIdList && taskIdList.size()>0){
				taskService.delTask(taskIdList, userInfo);
			}
			
		}else if("0".equals(state)){//还原
			for (String objs : ids) {
				String[] obj = objs.split("@");
				//回收箱内容主键
				Integer recycleBinId = Integer.parseInt(obj[0]);
				//业务主键
				Integer busId = Integer.parseInt(obj[1]);
				//业务类类型
				String busType = obj[2];
				//删除回收箱数据
				recycleBinDao.delById(RecycleBin.class, recycleBinId);
				if(ConstantInterface.TYPE_CRM.equals(busType)){
					//客户
					Customer crm = (Customer) recycleBinDao.objectQuery(Customer.class, busId);
					if(null!=crm){
						//设置正常恢复
						crm.setDelState(0);
						recycleBinDao.update(crm);
						
						//任务关联的项目数据整合
						taskService.updateBusTask(userInfo,busId,ConstantInterface.TYPE_CRM,"从回收箱恢复客户,整理任务关联");
						
						//任务的所有查看人
						List<UserInfo> shares = crmService.listCrmOwnersNoForce(userInfo.getComId(), busId);
						//恢复客户事项，并发送消息
						todayWorksService.addTodayWorks(userInfo, crm.getOwner(), busId, 
								"从回收箱恢复客户", busType, shares, null);
						
					}
				}else if(ConstantInterface.TYPE_ITEM.equals(busType)){
					//项目
					Item item = (Item) recycleBinDao.objectQuery(Item.class, busId);
					if(null!=item){
						//设置项目正常恢复
						item.setDelState(0);
						recycleBinDao.update(item);
						
						//任务关联的项目数据整合
						taskService.updateBusTask(userInfo,busId,ConstantInterface.TYPE_ITEM,"从回收箱恢复项目,整理任务关联");
						
						//任务的所有查看人
						List<UserInfo> shares = itemService.listItemOwnersNoForce(userInfo.getComId(), busId);
						//恢复项目事项，并发送消息
						todayWorksService.addTodayWorks(userInfo, item.getOwner(), busId, 
								"从回收箱恢复项目", busType, shares, null);
						
						//取得关联该项目的所有有任务
						
					}
				}else if(ConstantInterface.TYPE_TASK.equals(busType)){
					//任务
					Task task = (Task) recycleBinDao.objectQuery(Task.class, busId);
					if(null!=task){
						//设置任务正常恢复
						task.setDelState(0);
						recycleBinDao.update(task);
						
						//任务的所有查看人
						List<UserInfo> shares = taskService.listTaskUserForMsg(userInfo.getComId(), busId);
						//恢复任务事项，并发送消息
						todayWorksService.addTodayWorks(userInfo, task.getExecutor(), busId, 
								"从回收箱恢复任务", busType, shares, null);
					}
				}else if(ConstantInterface.TYPE_QUES.equals(busType)){
					//问答
					Question ques = (Question) recycleBinDao.objectQuery(Question.class, busId);
					if(null!=ques){
						ques.setDelState(0);
						recycleBinDao.update(ques);
					}
					
				}else if(ConstantInterface.TYPE_VOTE.equals(busType)){
					//投票
					Vote vote = (Vote) recycleBinDao.objectQuery(Vote.class, busId);
					if(null != vote){
						vote.setDelState(0);
						recycleBinDao.update(vote);
						
					}
				}
			}
		}
		
	}

	/**
	 * 操作所有满足条件的删除对象
	 * @param recycleBin
	 * @param state 操作1 删除0还原
	 * @param userInfo
	 * @param modTypes 
	 * @throws Exception 
	 */
	public void updateAllObject(RecycleBin recycleBin, String state,
			UserInfo userInfo, List<String> modList) throws Exception {
		//选出所有满足条件的
		List<RecycleBin> list = recycleBinDao.listAllPreDel(recycleBin,modList);
		if(null!=list && list.size()>0){
			if("1".equals(state)){
				//客户类主键
				List<Integer> crmIdList = new ArrayList<Integer>();
				//项目类主键
				List<Integer> itemIdList = new ArrayList<Integer>();
				
				//任务类主键
				List<Integer> taskIdList = new ArrayList<Integer>();
				for (RecycleBin obj : list) {
					//回收箱内容主键
					Integer recycleBinId = obj.getId();
					//业务主键
					Integer busId = obj.getBusId();
					//业务类类型
					String busType =obj.getBusType();
					//删除回收箱数据
					recycleBinDao.delById(RecycleBin.class, recycleBinId);
					if(ConstantInterface.TYPE_CRM.equals(busType)){
						
						crmIdList.add(busId);
						
					}else if(ConstantInterface.TYPE_ITEM.equals(busType)){
						
						itemIdList.add(busId);
						
					}else if(ConstantInterface.TYPE_TASK.equals(busType)){
						
						taskIdList.add(busId);
						
					}else if(ConstantInterface.TYPE_QUES.equals(busType)){
						
						qasService.delQues(busId, userInfo);
						
					}else if(ConstantInterface.TYPE_VOTE.equals(busType)){
						voteService.delVote(busId, userInfo);
					}
				}
				if(null != crmIdList && crmIdList.size()>0){
					crmService.delCustomer(crmIdList, userInfo);
				}
				if(null != itemIdList && itemIdList.size()>0){
					itemService.delItem(itemIdList, userInfo);
				}
				if(null != taskIdList && taskIdList.size()>0){
					taskService.delTask(taskIdList, userInfo);
				}
				
			}else if("0".equals(state)){//还原
				for (RecycleBin obj : list) {
					//回收箱内容主键
					Integer recycleBinId = obj.getId();
					//业务主键
					Integer busId = obj.getBusId();
					//业务类类型
					String busType =obj.getBusType();
					//删除回收箱数据
					recycleBinDao.delById(RecycleBin.class, recycleBinId);
					if(ConstantInterface.TYPE_CRM.equals(busType)){
						//客户
						Customer crm = (Customer) recycleBinDao.objectQuery(Customer.class, busId);
						if(null!=crm){
							//设置正常恢复
							crm.setDelState(0);
							recycleBinDao.update(crm);
							
							//任务关联的项目数据整合
							taskService.updateBusTask(userInfo,busId,ConstantInterface.TYPE_CRM,"从回收箱恢复客户,整理任务关联");
							
							//任务的所有查看人
							List<UserInfo> shares = crmService.listCrmOwnersNoForce(userInfo.getComId(), busId);
							//恢复客户事项，并发送消息
							todayWorksService.addTodayWorks(userInfo, crm.getOwner(), busId, 
									"从回收箱恢复客户", busType, shares, null);
						}
					}else if(ConstantInterface.TYPE_ITEM.equals(busType)){
						//项目
						Item item = (Item) recycleBinDao.objectQuery(Item.class, busId);
						if(null!=item){
							//设置项目正常恢复
							item.setDelState(0);
							recycleBinDao.update(item);
							//任务关联的项目数据整合
							taskService.updateBusTask(userInfo,busId,ConstantInterface.TYPE_ITEM,"从回收箱恢复项目,整理任务关联");
							
							//项目的所有查看人
							List<UserInfo> shares = itemService.listItemOwnersNoForce(userInfo.getComId(), busId);
							//恢复项目事项，并发送消息
							todayWorksService.addTodayWorks(userInfo, item.getOwner(), busId, 
									"从回收箱恢复项目", busType, shares, null);
						}
					}else if(ConstantInterface.TYPE_TASK.equals(busType)){
						//任务
						Task task = (Task) recycleBinDao.objectQuery(Task.class, busId);
						if(null!=task){
							//设置任务正常恢复
							task.setDelState(0);
							recycleBinDao.update(task);
							
							//任务的所有查看人
							List<UserInfo> shares = taskService.listTaskUserForMsg(userInfo.getComId(), busId);
							//恢复任务事项，并发送消息
							todayWorksService.addTodayWorks(userInfo, task.getExecutor(), busId, 
									"从回收箱恢复任务", busType, shares, null);
						
						}
					}else if(ConstantInterface.TYPE_QUES.equals(busType)){
						//问答
						Question ques = (Question) recycleBinDao.objectQuery(Question.class, busId);
						if(null!=ques){
							ques.setDelState(0);
							recycleBinDao.update(ques);
						}
						
					}else if(ConstantInterface.TYPE_VOTE.equals(busType)){
						//投票
						Vote vote = (Vote) recycleBinDao.objectQuery(Vote.class, busId);
						if(null!=vote){
							
							vote.setDelState(0);
							recycleBinDao.update(vote);
						}
					}else if(ConstantInterface.TYPE_ANNOUNCEMENT.equals(busType)){
						//公告
						Announcement announ = (Announcement) recycleBinDao.objectQuery(Announcement.class, busId);
							if(null!=announ){
								announ.setDelState(0);
							recycleBinDao.update(announ);
						}
					}
				}
			}
		}
	}
	/**
	 * （登录或是定时删除）选出所有满足超过三天的数据
	 * @param recycleBin
	 * @return
	 * @throws Exception 
	 */
	public void delAllOverTri(UserInfo userInfo) throws Exception {
		//回收箱用户查询
		RecycleBin recycleBin = new RecycleBin();
		if(null!=userInfo){
			//企业号
			recycleBin.setComId(userInfo.getComId());
			//用户主键
			recycleBin.setUserId(userInfo.getId());
		}
		//当前时间
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		//三天前时间
		String dateTime = DateTimeUtil.addDate(nowTime, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, -3);
		recycleBin.setEndDate(dateTime);
		//选出所有满足超过三天的数据
		List<RecycleBin> list = recycleBinDao.listAllOverTri(recycleBin);
		//若是定时任务来删除的话
		if(null==userInfo){//定时任务
			//用户信息缓存 key comId_userd,value userInfo
			Map<String, UserInfo> userMap = new HashMap<String, UserInfo>();
			//模块主键缓存 key comId_userd_busType,value userInfo
			Map<String, List<Integer>> idMap = new HashMap<String, List<Integer>>();
			if(null!=list && list.size()>0){
				//客户类主键
				List<Integer> crmIdList = new ArrayList<Integer>();
				//项目类主键
				List<Integer> itemIdList = new ArrayList<Integer>();
				//任务类主键
				List<Integer> taskIdList = new ArrayList<Integer>();
				
				//添加一个最后标识
				RecycleBin lastUser = new RecycleBin();
				lastUser.setUserId(-1);
				lastUser.setComId(-1);
				list.add(lastUser);
				
				//是否为最后一个默认不是
				boolean islast = false;
				
				for (RecycleBin obj : list) {
					//用户
					Integer userId = obj.getUserId();
					//企业号
					Integer comId = obj.getComId();
					//用户map键
					String key = comId+"_"+userId;
					
					UserInfo mapUser = userMap.get(key);
					
					if(null==mapUser){//map中没有该用户，则从系统中查询一个来
						if("-1_-1".equals(key)){//是否为最后一个
							islast = true;
						}
						//客户类集合有数据
						if(null != crmIdList && crmIdList.size()>0){
							idMap.put(key+"_"+ConstantInterface.TYPE_CRM, crmIdList);
						}
						//项目类集合有数据
						if(null != itemIdList && itemIdList.size()>0){
							idMap.put(key+"_"+ConstantInterface.TYPE_ITEM, itemIdList);
						}
						//任务累积和有数据
						if(null != taskIdList && taskIdList.size()>0){
							idMap.put(key+"_"+ConstantInterface.TYPE_TASK, taskIdList);
						}
						
						//清空客户类主键
						crmIdList.clear();
						//清空项目类主键
						itemIdList.clear();
						//清空任务类主键
						taskIdList.clear();
						
						//从数据查找用户
						mapUser = userInfoService.getUserInfo(comId, userId);
						//放入缓存
						userMap.put(key, mapUser);
					}
					
					//回收箱内容主键
					Integer recycleBinId = obj.getId();
					//业务主键
					Integer busId = obj.getBusId();
					//业务类类型
					String busType =obj.getBusType();
					
					if(null!=recycleBinId){
						//删除回收箱数据
						recycleBinDao.delById(RecycleBin.class, recycleBinId);
					}
					
					if(null==busType){//最后一个
						
					}else if(ConstantInterface.TYPE_CRM.equals(busType)){//客户
						//添加客户主键
						crmIdList.add(busId);
					}else if(ConstantInterface.TYPE_ITEM.equals(busType)){//项目
						//添加项目主键
						itemIdList.add(busId);
					}else if(ConstantInterface.TYPE_TASK.equals(busType)){//任务
						//添加任务主键
						taskIdList.add(busId);
					}else if(ConstantInterface.TYPE_QUES.equals(busType)){//问答
						//直接删除问答
						qasService.delQues(busId, mapUser);
					}else if(ConstantInterface.TYPE_VOTE.equals(busType)){//投票
						//直接删除投票
						voteService.delVote(busId, mapUser);
					}
					
					//满足十个或是最后一个，清空缓存
					if(userMap.size()>9 || islast){
						//idMap遍历
						for (Map.Entry<String, List<Integer>> entry : idMap.entrySet()) {
							//模块主键构成的key
							String comIdUserIdBusType = entry.getKey();
							//模块主键集合
							List<Integer> mapList = entry.getValue();
							//主键key元素
							String[] elements = comIdUserIdBusType.split("_");
							//用户
							UserInfo mapTempUser = userMap.get(elements[0]+"_"+elements[1]);
							//业务类型
							String busTempType = elements[2];
							
							if(ConstantInterface.TYPE_TASK.equals(busTempType)){//任务
								//批量删除任务
								taskService.delTask(mapList, mapTempUser);
							}else if(ConstantInterface.TYPE_CRM.equals(busTempType)){//客户
								//批量删除客户
								crmService.delCustomer(mapList, mapTempUser);
							}else if(ConstantInterface.TYPE_VOTE.equals(busTempType)){//项目
								//批量删除项目
								itemService.delItem(mapList, mapTempUser);
							}
						}
						
						//清空用户缓存
						userMap.clear();
						//清空模块主键缓存
						idMap.clear();
					}
				}
			}
		}else{//用户不为空，说明是登录进来的
			if(null!=list && list.size()>0){
				//客户类主键
				List<Integer> crmIdList = new ArrayList<Integer>();
				//项目类主键
				List<Integer> itemIdList = new ArrayList<Integer>();
				
				//任务类主键
				List<Integer> taskIdList = new ArrayList<Integer>();
				for (RecycleBin obj : list) {
					//回收箱内容主键
					Integer recycleBinId = obj.getId();
					//业务主键
					Integer busId = obj.getBusId();
					//业务类类型
					String busType =obj.getBusType();
					
					//删除回收箱数据
					recycleBinDao.delById(RecycleBin.class, recycleBinId);
					
					if(ConstantInterface.TYPE_CRM.equals(busType)){
						//添加客户主键
						crmIdList.add(busId);
					}else if(ConstantInterface.TYPE_ITEM.equals(busType)){
						//添加项目逐渐
						itemIdList.add(busId);
					}else if(ConstantInterface.TYPE_TASK.equals(busType)){
						//添加任务主键
						taskIdList.add(busId);
					}else if(ConstantInterface.TYPE_QUES.equals(busType)){
						//直接删除问答
						qasService.delQues(busId, userInfo);
						
					}else if(ConstantInterface.TYPE_VOTE.equals(busType)){
						//直接删除投票
						voteService.delVote(busId, userInfo);
					}
				}
				
				if(null != crmIdList && crmIdList.size()>0){
					//批量删除客户
					crmService.delCustomer(crmIdList, userInfo);
				}
				if(null != itemIdList && itemIdList.size()>0){
					//批量删除项目
					itemService.delItem(itemIdList, userInfo);
				}
				if(null != taskIdList && taskIdList.size()>0){
					//批量删除任务
					taskService.delTask(taskIdList, userInfo);
				}
			}
		}
	}
	
	
}
