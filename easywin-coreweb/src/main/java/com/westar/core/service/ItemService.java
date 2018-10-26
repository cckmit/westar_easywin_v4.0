package com.westar.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.westar.base.model.Attention;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.Customer;
import com.westar.base.model.CustomerUpfile;
import com.westar.base.model.DemandFile;
import com.westar.base.model.DemandTalkUpfile;
import com.westar.base.model.FeedInfoFile;
import com.westar.base.model.Item;
import com.westar.base.model.ItemHandOver;
import com.westar.base.model.ItemLog;
import com.westar.base.model.ItemProgress;
import com.westar.base.model.ItemShareGroup;
import com.westar.base.model.ItemSharer;
import com.westar.base.model.ItemTalk;
import com.westar.base.model.ItemTalkFile;
import com.westar.base.model.ItemUpfile;
import com.westar.base.model.ProTalkUpfile;
import com.westar.base.model.ProUpFiles;
import com.westar.base.model.RecycleBin;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.StagedInfo;
import com.westar.base.model.StagedItem;
import com.westar.base.model.Task;
import com.westar.base.model.TaskTalkUpfile;
import com.westar.base.model.TaskUpfile;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.pojo.ItemStagedInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.JpushUtils;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.ItemDao;
import com.westar.core.thread.IndexUpdateThread;
import com.westar.core.web.PaginationContext;

@Service
public class ItemService {
	
	@Autowired
	ItemDao itemDao;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	CrmService crmService;
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	IndexService indexService; 
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	JiFenService jiFenService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	SelfGroupService selfGroupService;
	
	@Autowired
	AttentionService attentionService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	ClockService clockService;
	
	@Autowired
	ChatService chatService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ItemProgressService itemProgressService;
	
	@Autowired
	DemandService demandService;
	
	@Autowired
	FunctionListService functionListService;
	
	@Autowired
	ProductService productService;
	
	/**
	 * 获取项目列表
	 * @param item
	 * @return
	 */
	public List<Item> listItem(Item item,UserInfo userInfo){
		// 验证当前登录人是否是督察人员
		boolean isForceInPersion = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);
		return itemDao.listItem(item,userInfo,isForceInPersion);
	}
	/**
	 * 获取项目列表
	 * @param item
	 * @return
	 */
	public PageBean<Item> listPagedItem(Item item,UserInfo userInfo){
		// 验证当前登录人是否是督察人员
		boolean isForceInPersion = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);
		List<Item> items = itemDao.listItem(item,userInfo,isForceInPersion);
		PageBean<Item> pageBean = new PageBean<Item>();
		pageBean.setRecordList(items);
		// 除开页面已有数据的总数
		Integer total = PaginationContext.getTotalCount();
		pageBean.setTotalCount(total);
		return pageBean;
	}
	/**
	 * 获取项目列表
	 * @param item
	 * @return
	 */
	public PageBean<Item> listPagedItemForDemand(Item item,UserInfo userInfo){
		PageBean<Item> pageBean = itemDao.listPagedItemForDemand(item,userInfo);
		return pageBean;
	}
	/**
	 * 获取项目集合（不分页）
	 * @param item
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Item> listItemOfAll(Item item,UserInfo userInfo,boolean isForceInPersion){
		return itemDao.listItemOfAll(item,userInfo,isForceInPersion);
	}
	/**
	 * 获取团队项目主键集合
	 * @param userInfo
	 * @return
	 */
	public List<Item> listItemOfAll(UserInfo userInfo){
		return itemDao.listItemOfAll(userInfo);
	}
	/**
	 * 获取个人权限下的本月新增（不分页）
	 * @param item
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Item> listItemAddByMonthOfAll(Item item,UserInfo userInfo,boolean isForceInPersion){
		return itemDao.listItemAddByMonthOfAll(item,userInfo,isForceInPersion);
	}
	/**
	 * 获取个人权限下的本月新增（分页）
	 * @param item
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	public List<Item> listItemAddByMonthForPage(Item item,UserInfo userInfo,boolean isForceInPersion){
		return itemDao.listItemAddByMonthForPage(item,userInfo,isForceInPersion);
	}
	/**
	 * 获取个人移交项目（不分页）
	 * @param item
	 * @param userInfo
	 * @return
	 */
	public List<Item> listItemHandsOfAll(Item item,UserInfo userInfo){
		return itemDao.listItemHandsOfAll(item,userInfo);
	}
	/**
	 * 获取个人移交项目（分页）
	 * @param item
	 * @param userInfo
	 * @return
	 */
	public List<Item> listItemHandsForPage(Item item,UserInfo userInfo){
		return itemDao.listItemHandsForPage(item,userInfo);
	}
	/**
	 * 添加新项目
	 * @param item 项目信息
	 * @param msgShare 信息分享
	 * @param userInfo 操作员
	 * @return
	 * @throws Exception
	 */
	public Integer addItem(Item item,UserInfo userInfo) throws Exception{
		//默认父项目主键-1
		if(null==item.getParentId()){
			item.setParentId(-1);
		}
		//默认关联客户主键0
		if(null==item.getPartnerId()){
			item.setPartnerId(0);
		}
		//更新时间
		item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		//项目正常，没有预删除
		item.setDelState(0);
		//设置最近更新时间
		item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		//项目基本信息存入
		Integer id = itemDao.add(item);
		item.setId(id);
		//项目功能清单存入
		if(!CommonUtil.isNull(item.getProductId())){
			functionListService.addImportFunctionList(id, ConstantInterface.TYPE_ITEM, ConstantInterface.TYPE_PRODUCT, item.getProductId(), userInfo);
		}
		//项目进度存入
		if(!CommonUtil.isNull(item.getListItemProgress())){
			for (ItemProgress itemProgress : item.getListItemProgress()) {
				if(!CommonUtil.isNull(itemProgress) && !CommonUtil.isNull(itemProgress.getProgressName())){
					itemProgress.setComId(userInfo.getComId());
					itemProgress.setCreator(userInfo.getId());
					itemProgress.setItemId(id);
					itemDao.add(itemProgress);
				}
			}
		}
		//设置进度第一阶段
		ItemProgress itemProgress =itemProgressService.queryMinUnStartedProgressByItemId(id);
		if(!CommonUtil.isNull(itemProgress)){
			itemProgress.setUserId(userInfo.getId());
			itemProgress.setStartTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm));
			itemProgress.setFinishTime(itemProgress.getStartTime());
			itemDao.update(itemProgress);
		}
		//设置进度第二阶段执行人、开始时间
		ItemProgress itemProg =itemProgressService.queryMinUnStartedProgressByItemId(id);
		if(!CommonUtil.isNull(itemProg)){
			itemProg.setUserId(userInfo.getId());
			itemProg.setStartTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm));
			itemDao.update(itemProg);
		}
		if(item.getPubState() != null && item.getPubState().equals(0)) {//当分享范围为私有时
			//项目参与人存入
			Set<Integer> sets = new HashSet<Integer>();
			if(null!=item.getListItemSharer() && item.getListItemSharer().size()>0){
				for(ItemSharer share : item.getListItemSharer()){
					if(!sets.contains(share.getUserId())){
						ItemSharer itemSharer = new ItemSharer();
						itemSharer.setComId(item.getComId());
						itemSharer.setItemId(id);
						itemSharer.setUserId(share.getUserId());
						itemDao.add(itemSharer);
						sets.add(share.getUserId());
					}
				}
			}
			//项目创建人加入分享中
			if(!sets.contains(userInfo.getId())){
				ItemSharer itemSharer = new ItemSharer();
				itemSharer.setComId(item.getComId());
				itemSharer.setItemId(id);
				itemSharer.setUserId(userInfo.getId());
				itemDao.add(itemSharer);
				sets.add(userInfo.getId());
			}
			
			//项目负责人加入分享中
			if(!sets.contains(item.getOwner())){
				ItemSharer itemSharer = new ItemSharer();
				itemSharer.setComId(item.getComId());
				itemSharer.setItemId(id);
				itemSharer.setUserId(item.getOwner());
				itemDao.add(itemSharer);
				sets.add(userInfo.getId());
			}
			
			//研发负责人加入分享中
			if(!CommonUtil.isNull(item.getDevelopLeader()) && !sets.contains(item.getDevelopLeader())){
				ItemSharer itemSharer = new ItemSharer();
				itemSharer.setComId(item.getComId());
				itemSharer.setItemId(id);
				itemSharer.setUserId(item.getDevelopLeader());
				itemDao.add(itemSharer);
				sets.add(userInfo.getId());
			}
		}
		//创建项目默认第一阶段信息
		StagedItem stagedItem = new StagedItem();
		stagedItem.setItemId(id);
		stagedItem.setComId(item.getComId());
		stagedItem.setCreator(item.getCreator());
		stagedItem.setStagedName("项目前期");
		stagedItem.setStagedOrder(1);
		stagedItem.setParentId(-1);
		//文件夹主键
	    Integer stagedItemId =	itemDao.add(stagedItem);
		
	    //添加项目附件信息
		this.addItemUpfiles(item, userInfo,stagedItemId);
		
		//项目移交记录表添加
		ItemHandOver itemHandOver = new ItemHandOver();
		itemHandOver.setComId(item.getComId());
		itemHandOver.setItemId(id);
		itemHandOver.setFromUser(item.getCreator());
		itemHandOver.setToUser(item.getOwner());
		itemDao.add(itemHandOver);
		//项目来源不是百分百有的
		if(null!=item && null!=item.getPartnerId()){
			Customer customer = crmService.getCrmById(item.getPartnerId());
			if(null!=customer){
				//客户模块日志添加
				crmService.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"关联项目\""+item.getItemName()+"\"");
			}
		}
		
		//项目的所有查看人
		List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), id);
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,item.getOwner(), id, "新项目:\""+item.getItemName()+"\"", ConstantInterface.TYPE_ITEM, shares,null);
		
		//项目模块日志添加
		this.addItemLog(userInfo.getComId(),id, userInfo.getId(), userInfo.getUserName(), "创建项目:\""+item.getItemName()+"\"");
		//如果有母项目，则给母项目添加日志
		if(-1 != item.getParentId()){
			this.addItemLog(userInfo.getComId(),item.getParentId(), userInfo.getId(), userInfo.getUserName(), "分解项目:\""+item.getItemName()+"\"");
		}
		//修改积分
		jiFenService.addJifen(userInfo.getComId(), userInfo.getId(),  ConstantInterface.TYPE_ITEM,
				"创建项目:"+item.getItemName(),id);
		
		//添加关注
		if(null!=item.getAttentionState() && item.getAttentionState().equals("1")){
			attentionService.addAtten(ConstantInterface.TYPE_ITEM, id, userInfo);
		}
		if(!userInfo.getId().equals(item.getOwner())){
			
			//取得待办事项主键
			TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),item.getOwner(),
					id,ConstantInterface.TYPE_ITEM,0);
			if(null!=todayWorks){
				JpushUtils.sendTodoMessage(userInfo.getComId(), item.getOwner(), userInfo.getId(),
						todayWorks.getId(), id, ConstantInterface.TYPE_ITEM,0,"项目:"+item.getItemName());
			}
			//添加工作轨迹
			systemLogService.addSystemLogWithTrace(userInfo,item.getOwner(), ConstantInterface.TYPE_ITEM, 
					id, "创建项目\""+item.getItemName()+"\"", "负责\""+userInfo.getUserName()+"\"创建的项目\""+item.getItemName()+"\"");
		}else{
			//添加工作轨迹
			systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_ITEM, 
					id, "创建项目\""+item.getItemName()+"\"", "创建项目\""+item.getItemName()+"\"");
			
		}
		//为项目创建索引
		this.updateItemIndex(id, userInfo,"add");
		return id;
	}
	/**
	 * 添加项目附件信息
	 * @param item 项目信息
	 * @param userInfo 当前操作人员
	 * @param stagedItemId 项目阶段信息
	 * @throws Exception
	 */
	private void addItemUpfiles(Item item, UserInfo userInfo,Integer stagedItemId) throws Exception {
		//项目的附件
		List<Upfiles> listUpfiles = item.getListUpfiles();
		if(null!=listUpfiles && !listUpfiles.isEmpty()){
			ItemUpfile itemUpfile =null;
			List<Integer> fileIds = new ArrayList<Integer>();
			for(Upfiles file : listUpfiles){
				itemUpfile = new ItemUpfile();
				//企业号
				itemUpfile.setComId(item.getComId());
				//项目主键
				itemUpfile.setItemId(item.getId());
				//文件主键
				itemUpfile.setUpfileId(file.getId());
				//上传人主键
				itemUpfile.setUserId(item.getCreator());
				//项目阶段主键
				itemUpfile.setStagedItemId(stagedItemId);
				//添加项目文件
				itemDao.add(itemUpfile);
				//为附件创建索引
				uploadService.updateUpfileIndex(file.getId(), userInfo,"add",item.getId(),ConstantInterface.TYPE_ITEM);
				
				fileIds.add(file.getId());
			}
			//模块信息
			fileCenterService.addModFile(userInfo, fileIds, item.getItemName());
		}
	}
	/**
	 * 添加项目模块操作日志
	 * @param comId 企业主键
	 * @param itemId 关联项目主键
	 * @param userId 操作者ID
	 * @param userName 操作者姓名
	 * @param content 动作描述
	 */
	public void addItemLog(Integer comId,Integer itemId,Integer userId,String userName,String content){
		ItemLog itemLog = new ItemLog();
		itemLog.setComId(comId);
		itemLog.setItemId(itemId);
//		content = userName +"在"+DateTimeUtil.formatDate(new Date(), 1)+content;
		itemLog.setContent(content);
		itemLog.setUserId(userId);
		itemLog.setUserName(userName);
		itemDao.add(itemLog);
	}
	/**
	 * 获取项目信息(需要分享人员)
	 * @param itemId 项目主键
	 * @param user 操作人员
	 * @return
	 */
	public Item getItem(Integer itemId,UserInfo user){
		Item item = itemDao.queryItemById(itemId,user);
		List<ItemSharer> listItemSharer = itemDao.listItemSharer(itemId, user.getComId());
		List<Item> listSonItem = itemDao.listSonItem(itemId, user.getComId());
		item.setListItemSharer(listItemSharer);
		item.setListSonItem(listSonItem);
		item.setSonItemNum(listSonItem.size());
		
		//查询设置留言、附件总数
		Integer talkNum = itemDao.countTalk(itemId, user.getComId());
		Integer fileNum = itemDao.countFile(itemId, user.getComId(),"other");
		item.setTalkNum(talkNum);
		item.setFileNum(fileNum);
		Integer saleFileNum = itemDao.countFile(itemId, user.getComId(),"sale");
		item.setSaleFileNum(saleFileNum);
		//维护记录总数
		Integer maintenanceRecordNum = itemDao.countMaintenanceRecord(itemId);
		item.setMaintenanceRecordNum(maintenanceRecordNum);
		//设置项目进度配置
		List<ItemProgress> listItemProgress = itemProgressService.listItemProgress(itemId);
		item.setListItemProgress(listItemProgress);
		
		//生成项目参与人JSon字符串
		StringBuffer itemSharerJson = null;
		if(null!=listItemSharer && !listItemSharer.isEmpty()){
			itemSharerJson = new StringBuffer("[");
			for(ItemSharer vo:listItemSharer){
				itemSharerJson.append("{'userID':'"+vo.getUserId()+"','userName':'"+vo.getSharerName()+"','gender':'"+vo.getGender()+"','uuid':'"+vo.getUuid()+"','fileName':'"+vo.getFilename()+"'},");	
			}
			itemSharerJson = new StringBuffer(itemSharerJson.substring(0,itemSharerJson.lastIndexOf(",")));
			itemSharerJson.append("]");
			item.setItemSharerJson(itemSharerJson.toString());
		}
		//删除项目待办提醒信息
		todayWorksService.delTodayWorksByOwner(user.getComId(),user.getId(),itemId,ConstantInterface.TYPE_ITEM);
		return item;
	}
	/**
	 * 项目的所有子项目
	 * @param id
	 * @param comId
	 * @return
	 */
	public List<Item> listSonItem(Integer id,Integer comId){
		List<Item> listSonItem = itemDao.listSonItem(id, comId);
		return listSonItem;
		
	}
	/**
	 * 项目查看权限验证
	 * @param comId
	 * @param itemId
	 * @param userId
	 * @return
	 */
	public boolean authorCheck(Integer comId,Integer itemId,Integer userId){
		List<Item> listItem = itemDao.authorCheck(comId,itemId,userId);
		if(null!=listItem && !listItem.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 权限验证
	 * @param userInfo
	 * @param itemId
	 * @return
	 */
	public boolean authorCheck(UserInfo userInfo,Integer itemId){
		// 验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);
		if(isForceIn){//是监督人员
			return true;
		}else{
			List<Item> listItem = itemDao.authorCheck(userInfo.getComId(),itemId,userInfo.getId());
			if(null!=listItem && !listItem.isEmpty()){
				return true;
			}else{
				// 查看验证，删出消息提醒
				todayWorksService.delTodoWork(itemId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ITEM,
						null);
				return false;
			}
		}
				
	}
	/**
	 * 批量删除项目
	 * @param ids 项目主键数组
	 * @param comId 企业主键
	 * @throws Exception 
	 */
	public void delItem(List<Integer> ids,UserInfo userInfo) throws Exception{
		if(null!=ids && ids.isEmpty()){
			for(Integer id : ids){
				//删除项目前，先更新与其关联的模块索引数据
				Item item = itemDao.queryItemById(id,userInfo);
				if(null==item){
					continue;
				}
				
				//将任务关联到该项目的全部设置为0
				itemDao.updateTaskBusId(id,userInfo.getComId(),ConstantInterface.TYPE_ITEM);
				//删除项目索引库
				this.updateItemIndex(item.getId(), userInfo,"del");
				//删除分享人
				itemDao.delByField("itemSharer", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),id});
				//删除附件
				itemDao.delByField("itemUpfile", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),id});
				//删除项目讨论附件
				itemDao.delByField("itemTalkFile", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),id});
				//删除项目讨论
				itemDao.delByField("itemTalk", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),id});
				//删除项目日志
				itemDao.delByField("itemLog", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),id});
				//删除项目阶段明细
				itemDao.delByField("stagedInfo", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),id});
				//删除项目阶段
				itemDao.delByField("stagedItem", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),id});
				//删除项目负责人变更记录
				itemDao.delByField("itemHandOver", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),id});
				//删除项目组成员关联
				itemDao.delByField("itemShareGroup", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),id});
				
				//删除浏览记录
				itemDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),id,ConstantInterface.TYPE_ITEM});
				//关注信息
				itemDao.delByField("attention", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),id,ConstantInterface.TYPE_ITEM});
				//最新动态
				itemDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),id,ConstantInterface.TYPE_ITEM});
				//删除功能清单
				itemDao.delByField("functionList", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),id,ConstantInterface.TYPE_ITEM});
				//删除进度
				itemDao.delByField("itemProgress", new String[]{"comId","itemId"}, 
						new Object[]{userInfo.getComId(), id});
				//删除聊天记录
				chatService.delBusChat(userInfo.getComId(),id,ConstantInterface.TYPE_ITEM);
				
				//添加工作轨迹
				systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_ITEM, 
						id, "删除项目\""+item.getItemName()+"\"", "删除项目\""+item.getItemName()+"\"");
				
				//修改积分
				jiFenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ITEMDEL,
						"删除项目:"+item.getItemName(),id);
				//删除项目基本信息
				itemDao.delById(Item.class, id);
			}
		}
	}
	/**
	 * 批量删除项目
	 * @param ids
	 * @param userInfo
	 * @throws Exception
	 */
	public void delPreItem(Integer[] ids,UserInfo userInfo) throws Exception{
		if(null!=ids && ids.length>0){
			for(Integer id : ids){
				Item item = (Item) itemDao.objectQuery(Item.class, id);
				//预删除标识
				item.setDelState(1);
				//修改项目信息
				itemDao.update(item);
				
				//删除数据更新记录
				itemDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_ITEM,id});
				//删除回收箱数据
				itemDao.delByField("recycleBin", new String[]{"comId","busType","busId","userId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_ITEM,id,userInfo.getId()});
				
				//取得所有设置了提醒的闹铃(删除所有的)
				clockService.delClockByType(userInfo.getComId(),id,ConstantInterface.TYPE_ITEM);
				
				//该删除项目的子项目集合
				List<Item> sonItems = itemDao.listItemOfAllOnlyChildren(id, userInfo.getComId());
				if(null!=sonItems && sonItems.size()>0){
					for (Item sonItem : sonItems) {
						//将子项目向上提一级
						sonItem.setParentId(item.getParentId());
						itemDao.update(sonItem);
					}
				}
				
				//回收箱
				RecycleBin recyleBin =  new RecycleBin();
				//业务主键
				recyleBin.setBusId(id);
				//业务类型
				recyleBin.setBusType(ConstantInterface.TYPE_ITEM);
				//企业号
				recyleBin.setComId(userInfo.getComId());
				//创建人
				recyleBin.setUserId(userInfo.getId());
				itemDao.add(recyleBin);
			}
		}
	}
	/**
	 * 汇报项目进度
	 * @param item
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateItemProgress(Item item,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//更新项目进度
			itemDao.itemProgressReport(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), item.getId());
			
			//进度为100%时，把项目标记为完成状态4
			if(null!=item.getItemProgress() && 0==item.getItemProgress()){
				item.setState(4);
				itemDao.remarkItemState(item);
				//通知负责人项目完成
//				UserInfo itemOwer = userInfoService.getBusinessOwner(userInfo.getComId(), item.getId(),BusinessTypeConstant.type_item);
//				List<UserInfo> shares = new ArrayList<UserInfo>();
//				shares.add(itemOwer);
				//添加待办提醒通知,先删除所有消息
//				todayWorksService.updateTodayWorks(userInfo,-1, item.getId(), "汇报进度:\""+item.getItemProgressDescribe()+"\"", 
//					BusinessTypeConstant.type_item, shares,null);
//				
				//取得所有设置了提醒的闹铃(删除所有的)
				clockService.delClockByType(userInfo.getComId(),item.getId(),ConstantInterface.TYPE_ITEM);
			
			}else{
				//添加待办提醒通知,更新待办事项
				//todayWorksService.updateTodayWorks(userInfo,null, item.getId(), "汇报进度:\""+item.getItemProgressDescribe()+"\"",
				//		BusinessTypeConstant.type_item, shares,null);
			}
			//更新项目索引
//			this.updateItemIndex(item.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 项目名称变更
	 * @param item
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateItemName(Item item,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//项目名称变更
			itemDao.itemNameUpdate(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), item.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, item.getId(), "项目名称变更为\""+item.getItemName()+"\"", BusinessTypeConstant.type_item, shares,null);

			//更新项目索引
			this.updateItemIndex(item.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 项目说明更新
	 * @param item
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateItemItemRemark(Item item,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//更新项目说明
			itemDao.itemItemRemarkUpdate(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), item.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,item.getId(), "项目简介变更为:"+item.getItemRemark(), BusinessTypeConstant.type_item, shares,null);

			//更新项目索引
//			this.updateItemIndex(item.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 项目金额变更
	 * @author hcj 
	 * @date: 2018年10月12日 下午4:34:46
	 * @param item
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateAmount(Item item,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//更新项目说明
			itemDao.update(item);
			// 模块日志添加
			this.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目金额变更成功");
		} catch (Exception e) {
			succ = false ;
			// 模块日志添加
			this.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目金额变更失败");
			throw e;
		}
		return succ;
	}
	
	/**
	 * 项目研发负责人变更
	 * @author hcj 
	 * @date: 2018年10月12日 下午4:34:46
	 * @param item
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateDevelopLeader(Item item,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//更新项目说明
			itemDao.update(item);
			UserInfo user = (UserInfo) itemDao.objectQuery(UserInfo.class, item.getDevelopLeader());
			// 模块日志添加
			this.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目研发负责人变更为："+user.getUserName());
		} catch (Exception e) {
			succ = false ;
			// 模块日志添加
			this.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目研发负责人变更失败");
			throw e;
		}
		return succ;
	}
	
	/**
	 * 项目服务期限变更
	 * @author hcj 
	 * @date: 2018年10月12日 下午4:34:46
	 * @param item
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateServiceDate(Item item,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//更新项目说明
			itemDao.update(item);
			// 模块日志添加
			this.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目服务期限变更成功");
		} catch (Exception e) {
			succ = false ;
			// 模块日志添加
			this.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目服务期限变更失败");
			throw e;
		}
		return succ;
	}
	
	/**
	 * 项目公开私有更新
	 * @param item
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateItemPubState(Item item,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//删除分享范围记录
			itemDao.update("delete from itemSharer a  where a.comid=:comId and a.itemId=:id", item);
			if(item.getPubState().equals(1)) {//当公开私有为私有的时候 默认添加操作人和负责人
				Set<Integer> shareIdSets = new HashSet<Integer>();
				//把当前操作人添加为项目参与人
				shareIdSets.add(userInfo.getId());
				//把项目负责人添加为项目参与人
				shareIdSets.add(item.getOwner());
				//项目参与人存入
				if(null!=shareIdSets && shareIdSets.size()>0){
					for (Integer shareId : shareIdSets) {
						ItemSharer itemSharer = new ItemSharer();
						itemSharer.setComId(item.getComId());
						itemSharer.setItemId(item.getId());
						itemSharer.setUserId(shareId);
						itemDao.add(itemSharer);
					}
				}
			
			}
			//客户公开私有更新
			itemDao.update("update item a set a.pubState=:pubState where a.comid=:comId and a.id=:id", item);
			
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	
	
	/**
	 * 项目母项目关联
	 * @param item
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateItemParentId(Item item,UserInfo userInfo){
		boolean succ = true;
		Item pItem = this.getItemById(item.getParentId());
		item.setPartnerName(pItem.getItemName());
		
		try {
			//更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//项目母项目关联
			itemDao.itemParentIdUpdate(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), item.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, item.getId(), "项目父项目关联为\""+item.getPartnerName()+"\"", BusinessTypeConstant.type_item, shares,null);

			//更新项目索引
//			this.updateItemIndex(item.getId(), userInfo,"update");
			//模块日志添加
			this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目母项目关联为\""+pItem.getItemName()+"\"");
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目母项目关联为\""+pItem.getItemName()+"\"失败");
		}
		return succ;
	}
	/**
	 * 关联项目来源
	 * @param item
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateItemPartner(Item item,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//项目母项目关联
			itemDao.update("update item a set a.partnerId=:partnerId,modifyDate=:modifyDate where a.comid=:comId and a.id=:id",item);
			Customer customer = crmService.getCrmById(item.getPartnerId());
			if(null!=customer){
				//项目模块日志添加
				this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), 
						"项目来源关联客户\""+customer.getCustomerName()+"\"");
				//客户模块日志添加
				Item itemVo = (Item) itemDao.objectQuery(Item.class, item.getId());
				crmService.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"关联项目\""+itemVo.getItemName()+"\"");

				//获取项目所有参与人集合没有督察人员
				//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), item.getId());
				//添加待办提醒通知
				//todayWorksService.updateTodayWorks(userInfo,null, item.getId(), "项目来源关联客户\""+customer.getCustomerName()+"\"",BusinessTypeConstant.type_item, shares,null);
			}
			//更新项目索引
//			this.updateItemIndex(item.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 项目负责人更新
	 * @param item
	 * @param userInfo 
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateItemOwner(Item item, UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			Item itemObj = (Item)itemDao.objectQuery(Item.class, item.getId());
			//项目负责人
			Integer itemOwner = itemObj.getOwner();
			//项目负责人非自己
			if(itemOwner!=item.getOwner()){
				//判断原负责人是否在分享人中
				ItemSharer itemSharer =itemDao.getSharerForOwner(item.getId(),userInfo.getComId(),itemOwner); 
				//把原来项目负责人添加到项目参与人中
				if(null==itemSharer){
					itemSharer = new ItemSharer();
					itemSharer.setComId(userInfo.getComId());
					itemSharer.setItemId(item.getId());
					itemSharer.setUserId(itemOwner);
					itemDao.add(itemSharer);
				}
				item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
				//项目负责人更新
				itemDao.itemOwnerUpdate(item);
				//想项目参与人汇报项目进度
				//项目移交记录表添加
				ItemHandOver itemHandOver = new ItemHandOver();
				itemHandOver.setComId(item.getComId());
				itemHandOver.setItemId(item.getId());
				itemHandOver.setFromUser(userInfo.getId());
				itemHandOver.setToUser(item.getOwner());
				itemDao.add(itemHandOver);
				
				//获取项目所有参与人集合没有督察人员
				//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), item.getId());
				//获取已更新的项目负责人
				List<UserInfo> shares =  userInfoService.listOwnerForMsg(userInfo.getComId(), item.getId(),null, ConstantInterface.TYPE_ITEM,null);
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo,item.getOwner(), item.getId(), 
						"项目经理变更为\""+item.getOwnerName()+"\"", ConstantInterface.TYPE_ITEM, shares,null);
				//更新项目的所有后代参与人
				this.updateItemSharerForChildren(item.getId(),userInfo.getComId());

				//更新项目索引
//				this.updateItemIndex(item.getId(), userInfo,"update");
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 项目参与人更新
	 * @param userInfo
	 * @param itemId
	 * @param userIds
	 * @param sharerName 
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateItemSharer(UserInfo userInfo,Integer itemId,Integer[] userIds, String sharerName) throws Exception{
		boolean succ = true;
		try {
			//项目负责人将文件归类后需要删除的
			List<ItemSharer> itemSharers = itemDao.listRemoveItemSharer(itemId, userInfo.getComId(),userIds);
			//项目信息
			Item itemObj = (Item) itemDao.objectQuery(Item.class, itemId);
			if(null!=itemSharers && itemSharers.size()>0){
				for(ItemSharer itemSharer : itemSharers){
					//删除设置了提醒的闹铃
					clockService.delClockByUserId(userInfo.getComId(),itemSharer.getUserId(),itemId,ConstantInterface.TYPE_ITEM);
				}
				
			}
			//先删除项目的参与人
			itemDao.delByField("itemSharer", new String[]{"comId","itemId"},new Integer[]{userInfo.getComId(),itemId});
			if(null!=userIds && userIds.length>0){
				//项目负责人更新
				ItemSharer itemSharer =null;
				for(Integer userId:userIds){
					itemSharer = new ItemSharer();
					itemSharer.setComId(userInfo.getComId());
					itemSharer.setUserId(userId);
					itemSharer.setItemId(itemId);
					itemDao.add(itemSharer);
				}
			}
			//判断是否添加了创建人和负责人
			int countC = 0;
			int countO = 0;
			for (int i = 0; i < userIds.length; i++) {
				if(userIds[i].equals(itemObj.getCreator())) {
					countC++;
				}else if(userIds[i].equals(itemObj.getOwner())) {
					countO++;
				}
			}
			ItemSharer itemSharer =null;
			if(countC < 1 ) {
				itemSharer = new ItemSharer();
				itemSharer.setComId(userInfo.getComId());
				itemSharer.setUserId(itemObj.getCreator());
				itemSharer.setItemId(itemId);
				itemDao.add(itemSharer);
			}
			if(countO < 1 && !itemObj.getCreator().equals(itemObj.getOwner())) {
				itemSharer = new ItemSharer();
				itemSharer.setComId(userInfo.getComId());
				itemSharer.setUserId(itemObj.getOwner());
				itemSharer.setItemId(itemId);
				itemDao.add(itemSharer);
			}
			
			
			//项目修改更新时间
			Item itemT = new Item();
			itemT.setId(itemId);
			itemT.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(itemT);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), itemId);
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, itemId, "项目参与人变更为\""+sharerName+"\"", 
			//		BusinessTypeConstant.type_item, shares,null);
			//更新项目的所有后代参与人
			this.updateItemSharerForChildren(itemObj.getId(),userInfo.getComId());
			//更新项目索引
//			this.updateItemIndex(itemObj.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 删除项目参与人
	 * @param comId 企业主键
	 * @param itemId 项目主键
	 * @param userId 负责人主键
	 * @param shareName 参与人姓名
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delItemSharer(Integer comId,Integer itemId,Integer userId,UserInfo userInfo, String shareName) throws Exception{
		boolean succ = true;
		try {
			//项目信息
//			Item itemObj = (Item) itemDao.objectQuery(Item.class, itemId);
			//删除项目的参与人
			itemDao.delByField("itemSharer", new String[]{"comId","itemId","userId"},new Integer[]{comId,itemId,userId});
			//判断移除的人员是否仍有查看权限
			Integer shareNum = itemDao.countUserNum(itemId,userInfo.getComId(),userId);
			//没有权限了
			if(shareNum>0){
				//删除设置了提醒的闹铃
				clockService.delClockByUserId(userInfo.getComId(),userId,itemId,ConstantInterface.TYPE_ITEM);
			}
			
			//项目修改更新时间
			Item itemT = new Item();
			itemT.setId(itemId);
			itemT.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(itemT);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), itemId);
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, itemId, "删除项目参与人\""+shareName+"\"", 
			//		BusinessTypeConstant.type_item, shares,null);
			//更新项目索引
//			this.updateItemIndex(itemObj.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 项目删除分享组
	 * @param itemId
	 * @param grpId
	 * @param grpName
	 * @param userInfo
	 * @throws Exception 
	 */
	public void delItemGroup(Integer itemId, Integer grpId, String grpName,
			UserInfo userInfo) throws Exception {
		//删除项目的参与人
		itemDao.delByField("itemShareGroup", new String[]{"comId","itemId","grpId"},new Integer[]{userInfo.getComId(),itemId,grpId});
		
		//取得本次移除分组的成员
		List<UserInfo> removeUserList = itemDao.listRemoveGrpUser(itemId,userInfo.getComId(),grpId);
		//有移除人员，附件不为空
		if(null!=removeUserList &&removeUserList.size()>0 ){
			
			for (UserInfo user : removeUserList) {
				//删除设置了提醒的闹铃
				clockService.delClockByUserId(userInfo.getComId(),user.getId(),itemId,ConstantInterface.TYPE_ITEM);
			}
		}
		
		//项目修改更新时间
		Item itemT = new Item();
		itemT.setId(itemId);
		itemT.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		itemDao.update(itemT);
		
		//获取项目所有参与人集合没有督察人员
		//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), itemId);
		//添加待办提醒通知
		//todayWorksService.updateTodayWorks(userInfo,null, itemId, "删除项目分享组\""+grpName+"\"", 
		//		BusinessTypeConstant.type_item, shares,null);
		//更新项目索引
//		this.updateItemIndex(itemId, userInfo,"update");
		
	}
	/**
	 * 项目标记完成
	 * @param item
	 * @param userInfo 
	 * @param state 标记
	 * @return
	 */
	public boolean updateDoneItem(Item item, UserInfo userInfo, String state){
		boolean succ = true;
		try {
			//需要标记子项目
			if(null!=item.getChildAlsoRemark() && "yes".equals(item.getChildAlsoRemark())){
				//查询子项目集合
				List<Item> listSonItem = itemDao.listItemOfAllOnlyChildren(item.getId(),item.getComId());
				//存在子项目
				if(null!=listSonItem && !listSonItem.isEmpty()){
					//循环标记子项目
					for(Item sonItem:listSonItem){
						if(sonItem.getState()==4 && sonItem.getDelState()==0){//子项目已经被标记完成,或是已被删除
							continue;
						}
						
						//取得所有设置了提醒的闹铃(删除所有的)
						clockService.delClockByType(userInfo.getComId(),sonItem.getId(),ConstantInterface.TYPE_ITEM);
						
						sonItem.setState(item.getState());
						//项目修改更新时间
						sonItem.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
						//项目标记
						itemDao.remarkItemState(sonItem);
						
						if(sonItem.getDelState()==0){//只有没被删除的项目才可以发送消息
							//获取项目所有参与人集合没有督察人员
							List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), sonItem.getId());
							//添加待办提醒通知
							todayWorksService.addTodayWorks(userInfo,-1, sonItem.getId(),  "项目标记为\""+state+"\"", 
									ConstantInterface.TYPE_ITEM, shares,null);
						}
					}
				}
			}
			//项目修改更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//项目标记
			itemDao.remarkItemState(item);
			
			//取得所有设置了提醒的闹铃(删除所有的)
			clockService.delClockByType(userInfo.getComId(),item.getId(),ConstantInterface.TYPE_ITEM);
			
			//获取项目所有参与人集合没有督察人员
			List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), item.getId());
			//添加待办提醒通知，先删除所有待办事项
			todayWorksService.addTodayWorks(userInfo,-1,item.getId(), "项目标记为\""+state+"\"", 
					ConstantInterface.TYPE_ITEM, shares,null);
			//模块日志添加
			this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目标记为\""+state+"\"");
			
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目标记为\""+state+"\"失败");
		}
		return succ;
	}
	/**
	 * 项目标记重启
	 * @param item
	 * @param userInfo 
	 * @param state 标记
	 * @return
	 */
	public boolean updateRestarItem(Item item, UserInfo userInfo, String state){
		boolean succ = true;
		try {
			//是否需要重启子项目
			if(null!=item.getChildAlsoRemark() && "yes".equals(item.getChildAlsoRemark())){
				//查询所有的子项目
				List<Item> listItem = itemDao.listItemOfAllOnlyChildren(item.getId(),item.getComId());
				//子项目不为空
				if(null!=listItem && !listItem.isEmpty()){
					for(Item sonItem:listItem){
						sonItem.setState(item.getState());
						//项目修改更新时间
						sonItem.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
						//项目标记
						itemDao.remarkItemState(sonItem);
						if(sonItem.getDelState()==0){//只有没有被删除的项目才可以发送消息
							//获取项目所有参与人集合没有督察人员
							List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), sonItem.getId());
							//添加待办提醒通知,更新待办事项
							todayWorksService.addTodayWorks(userInfo,sonItem.getOwner(),sonItem.getId(), 
									"项目标记为\""+state+"\"", ConstantInterface.TYPE_ITEM, shares,null);
						}
						
					}
				}
			}
			
			//项目的所有父项目包括自己和预删除的
			List<Item> listParentTask = itemDao.listItemOfAllParent(item.getId(),userInfo.getComId());
			if(null!=listParentTask && listParentTask.size()>0){
				for (Item pItem : listParentTask) {
					if(pItem.getState()==1 && pItem.getDelState()==0){//父项目是开启的,且没有被删除
						continue;
					}
					//项目状态
					pItem.setState(item.getState());
					//项目修改更新时间
					pItem.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
					//项目标记
					itemDao.remarkItemState(pItem);
					
					if(pItem.getDelState()==0){//没有被删除的项目才可以发送消息
						//获取项目所有参与人集合没有督察人员
						List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), pItem.getId());
						//添加待办提醒通知,更新待办事项
						todayWorksService.addTodayWorks(userInfo,pItem.getOwner(),pItem.getId(), 
							"项目标记为\""+state+"\"", ConstantInterface.TYPE_ITEM, shares,null);
					}
				}
			}
			//模块日志添加
			this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目标记为\""+state+"\"");
			
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目标记为\""+state+"\"失败");
		}
		return succ;
	}
	/**
	 * 获取此项目以及此项目后代项目以外的项目集合
	 * @param item
	 * @return
	 */
	public String itemStrJson(Item item){
		List<Item> listItem = itemDao.listItemOfOthers(item);
		StringBuffer strJson=new StringBuffer();
		if(null!=listItem && !listItem.isEmpty()){
			strJson.append("[");
			for(Item vo:listItem){
				strJson.append("{\"id\":\""+vo.getId()+"\",\"name\":\""+vo.getItemName()+"\"},");
			}
			strJson=new StringBuffer(strJson.substring(0,strJson.lastIndexOf(",")));
			strJson.append("]");
		}
		return strJson.toString();
	}
	/**
	 * 获取匹配客户JSON字符串
	 * @param customer
	 * @return
	 */
	public String partnerJson(Customer customer){
		return crmService.partnerJson(customer);
	}
	/**
	 * 添加项目讨论
	 * @param itemTalk
	 * @param userInfo 当前用户
	 * @return
	 * @throws Exception
	 */
	public Integer addItemTalk(ItemTalk itemTalk,UserInfo userInfo) throws Exception{
		Integer id = itemDao.add(itemTalk);
		Integer[] upfilesId = itemTalk.getUpfilesId();
		if(null!=upfilesId){
			//取得最近的的项目阶段文件夹主键
			Integer stagedItemId = getStageItemId(userInfo, itemTalk.getItemId());
			
			for (Integer upfileId : upfilesId) {
				ItemTalkFile itemTalkFile = new ItemTalkFile();
				//企业编号
				itemTalkFile.setComId(itemTalk.getComId());
				//项目编号
				itemTalkFile.setItemId(itemTalk.getItemId());
				//讨论主键
				itemTalkFile.setTalkId(id);
				//文件主键
				itemTalkFile.setUpfileId(upfileId);
				//上传人
				itemTalkFile.setUserId(itemTalk.getUserId());
				//项目阶段主键
				itemTalkFile.setStagedItemId(stagedItemId);
				itemDao.add(itemTalkFile);
				//为附件创建索引
				uploadService.updateUpfileIndex(upfileId, userInfo, "add",itemTalk.getItemId(),ConstantInterface.TYPE_ITEM);
			}
			//附件归档
			Item item = (Item) itemDao.objectQuery(Item.class, itemTalk.getItemId());
			fileCenterService.addModFile(userInfo, Arrays.asList(upfilesId), item.getItemName());
			
		}
		
		//添加信息分享人员
		List<ItemSharer> listShareUser = itemTalk.getListItemSharers();
		Set<Integer> pushUserIdSet = new HashSet<Integer>();
		if(null != listShareUser && !listShareUser.isEmpty()){
			for (ItemSharer shareUser : listShareUser) {
				//人员主键
				Integer userId = shareUser.getUserId();
				pushUserIdSet.add(userId);
				//删除上次的分享人员
				itemDao.delByField("itemSharer", new String[]{"comId","itemId","userId"}, 
						new Object[]{userInfo.getComId(),itemTalk.getItemId(),userId});
				shareUser.setItemId(itemTalk.getItemId());
				shareUser.setComId(userInfo.getComId());
				itemDao.add(shareUser);
			}
		}
		
		//项目修改更新时间
		Item item = new Item();
		item.setId(itemTalk.getItemId());
		item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		itemDao.update(item);
		
		//获取项目所有参与人集合没有督察人员
		//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), itemTalk.getItemId());
		
		//获取该项目的负责人和留言父用户
		List<UserInfo> shares =  userInfoService.listOwnerForMsg(userInfo.getComId(),itemTalk.getItemId(), itemTalk.getParentId(), ConstantInterface.TYPE_ITEM,pushUserIdSet);
		
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, itemTalk.getItemId(),"添加新评论:"+itemTalk.getContent(), ConstantInterface.TYPE_ITEM, shares,null);
				
		//修改积分
		jiFenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ITEMTALK,
				"添加项目评论",itemTalk.getItemId());
		//更新项目索引
//		this.updateItemIndex(itemTalk.getItemId(), userInfo,"update");
		return id;
	}
	/**
	 * 根据主键id查询讨论详情
	 * @param id
	 * @param comId
	 * @return
	 */
	public ItemTalk queryItemTalk(Integer id,Integer comId){
		ItemTalk itemTalk = itemDao.queryItemTalk(id,comId);
		//讨论的附件
		if(null!=itemTalk){
			itemTalk.setListItemTalkFile(itemDao.listItemTalkFile(comId, itemTalk.getItemId(), id));
		}
		return itemTalk;
	}
	/**
	 * 根据项目主键查询其下的讨论信息
	 * @param itemId
	 * @param comId
	 * @return
	 */
	public List<ItemTalk> listItemTalk(Integer itemId,Integer comId,String dest){
		List<ItemTalk> listItemTalk = itemDao.listItemTalk(itemId,comId);
		List<ItemTalk> list = new ArrayList<ItemTalk>();
		for (ItemTalk itemTalk : listItemTalk) {
			//非手机端需要字符转换
			if(!"app".equals(dest)){
				String content = StringUtil.toHtml(itemTalk.getContent());
				itemTalk.setContent(content);
			}
			
			itemTalk.setListItemTalkFile(itemDao.listItemTalkFile(comId,itemId,itemTalk.getId()));
			list.add(itemTalk);
		}
		return list;
	}
	/**
	 * 删除项目讨论
	 * @param itemTalk
	 * @throws Exception 
	 */
	public void delItemTalk(ItemTalk itemTalk,UserInfo userInfo) throws Exception{
		if("yes".equals(itemTalk.getDelChildNode())){
			//待删除的项目讨论
			List<ItemTalk> listItemTalk = itemDao.listItemTalkForDel(itemTalk.getComId(), itemTalk.getId());
			for (ItemTalk itemTalkSingle : listItemTalk) {
				//删除附件
				itemDao.delByField("itemTalkFile", new String[]{"comId","talkId"}, 
						new Object[]{itemTalkSingle.getComId(),itemTalkSingle.getId()});
			}
			
			//删除当前节点及其子节点回复
			itemDao.delItemTalk(itemTalk.getId(),itemTalk.getComId());
		}else{
			//删除附件
			itemDao.delByField("itemTalkFile", new String[]{"comId","talkId"}, 
					new Object[]{itemTalk.getComId(),itemTalk.getId()});
			//把子节点的parentId向上提一级
			itemDao.updateItemTalkParentId(itemTalk.getId(),itemTalk.getComId());
			//删除当前节点
			itemDao.delByField("itemTalk", new String[]{"comId","id"},new Integer[]{itemTalk.getComId(),itemTalk.getId()});
		}
		
		//项目修改更新时间
		Item item = new Item();
		item.setId(itemTalk.getItemId());
		item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		itemDao.update(item);
		
		//获取项目所有参与人集合没有督察人员
		//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), itemTalk.getItemId());
		List<UserInfo> shares = userInfoService.listOwnerForMsg(userInfo.getComId(), itemTalk.getItemId(), null, ConstantInterface.TYPE_ITEM,null);
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, itemTalk.getItemId(),"删除了评论", ConstantInterface.TYPE_ITEM, shares,null);
		//更新项目索引
//		this.updateItemIndex(itemTalk.getItemId(), userInfo,"update");
		//修改积分
		jiFenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TALKDEL,
				"删除项目评论",itemTalk.getItemId());
	}
	/**
	 * 获取项目日志集合
	 * @param itemId
	 * @param comId
	 * @return
	 */
	public List<ItemLog> listItemLog(Integer itemId,Integer comId){
		return itemDao.listItemLog(itemId,comId);
	}
	/**
	 * 获取项目附件
	 * @param itemUpfile
	 * @param comId
	 * @param type 
	 * @return
	 */
	public List<ItemUpfile> listPagedItemUpfile(ItemUpfile itemUpfile,Integer comId, String type){
		return itemDao.listPagedItemUpfile(itemUpfile,comId,type);
	}
	/**
	 * 获取项目阶段JSON字符串
	 * @param comId
	 * @param itemId
	 * @param sid 
	 * @return
	 */
	public String itemStagedJsonStr(Integer comId,Integer itemId, String sid){
		List<ItemStagedInfo> listItemStagedInfo = itemDao.listStagedItemInfo(comId,itemId);
		StringBuffer strJson=new StringBuffer("[");
		if(null!=listItemStagedInfo && !listItemStagedInfo.isEmpty()){
			for(ItemStagedInfo vo:listItemStagedInfo){
				//阶段文件夹
				if(null!=vo.getType() && !"".equals(vo.getType()) && "folder".equals(vo.getType())){
					strJson.append("{\"id\":\""+vo.getKey()+"\",\"pId\":\""+vo.getParentId()+"\",\"name\":\""+vo.getName()+"\",realId:\""+vo.getRealId()+"\",type:\""+vo.getType()+"\"");
					strJson.append(", icon:\"/static/images/folder_add.png\",iconOpen:\"/static/images/folder_open.png\", iconClose:\"/static/images/folder_database.png\"");
					strJson.append(",moduleId:\""+vo.getModuleId()+"\"");
					strJson.append("},");
//				}else if(null!=vo.getType() && !"".equals(vo.getType()) && "file".equals(vo.getType())){//阶段附件
//					strJson.append("{\"id\":\""+vo.getKey()+"\",\"pId\":\""+vo.getParentId()+"\",\"name\":\"【阶段附件】"+vo.getName()+"\",realId:\""+vo.getRealId()+"\",type:\""+vo.getType()+"\",dropInner:false");
//					strJson.append(",icon:\"/static/images/page_white_edit.png\",\"uuid\":\""+vo.getUuid()+"\",\"target\":\"blank\",fileExt:\""+vo.getFileExt()+"\"");
//					strJson.append(",moduleId:\""+vo.getModuleId()+"\"");
//					strJson.append("},");
//				}else if(null!=vo.getType() && !"".equals(vo.getType()) && "itemUpFile".equals(vo.getType())){//项目自身附件
//					strJson.append("{\"id\":\""+vo.getKey()+"\",\"pId\":\""+vo.getParentId()+"\",\"name\":\"【项目附件】"+vo.getName()+"\",realId:\""+vo.getRealId()+"\",type:\""+vo.getType()+"\",dropInner:false");
//					strJson.append(",icon:\"/static/images/page_white_edit.png\",\"uuid\":\""+vo.getUuid()+"\",\"target\":\"blank\",fileExt:\""+vo.getFileExt()+"\"");
//					strJson.append(",moduleId:\""+vo.getModuleId()+"\"");
//					strJson.append("},");
//				}else if(null!=vo.getType() && !"".equals(vo.getType()) && "itemTalkFile".equals(vo.getType())){//项目留言附件
//					strJson.append("{\"id\":\""+vo.getKey()+"\",\"pId\":\""+vo.getParentId()+"\",\"name\":\"【留言附件】"+vo.getName()+"\",realId:\""+vo.getRealId()+"\",type:\""+vo.getType()+"\",dropInner:false");
//					strJson.append(",icon:\"/static/images/page_white_edit.png\",\"uuid\":\""+vo.getUuid()+"\",\"target\":\"blank\",fileExt:\""+vo.getFileExt()+"\"");
//					strJson.append(",moduleId:\""+vo.getModuleId()+"\"");
//					strJson.append("},");
//				}else if(null!=vo.getType() && !"".equals(vo.getType()) && "sp_flow".equals(vo.getType())){//审批
//					strJson.append("{\"id\":\""+vo.getKey()+"\",\"pId\":\""+vo.getParentId()+"\",\"name\":\"【审批】"+vo.getName()+"\",realId:\""+vo.getRealId()+"\",type:\""+vo.getType()+"\",dropInner:false");
//					strJson.append(",icon:\"/static/images/page_white_edit.png\",\"click\":\"stagedInfoView('"+vo.getModuleId()+"','022')\"");
//					strJson.append(",moduleId:\""+vo.getModuleId()+"\"");
//					strJson.append("},");
//				}else{//任务
//					strJson.append("{\"id\":\""+vo.getKey()+"\",\"pId\":\""+vo.getParentId()+"\",\"name\":\"【任务】"+vo.getName()+"\",realId:\""+vo.getRealId()+"\",type:\""+vo.getType()+"\",dropInner:false");
//					strJson.append(",icon:\"/static/images/page_white_edit.png\",\"click\":\"stagedInfoView('"+vo.getModuleId()+"','003')\"");
//					strJson.append(",moduleId:\""+vo.getModuleId()+"\"");
//					strJson.append("},");
				}
			}
			strJson=new StringBuffer(strJson.substring(0,strJson.lastIndexOf(",")));
		}
		strJson.append("]");
		return strJson.toString();
	}
	
	/**
	 * 获取项目阶段集合
	 * @param comId 团队主键	
	 * @param itemId 项目主键
	 * @return
	 */
	public List<ItemStagedInfo> listItemStagedInfo(Integer comId,Integer itemId){
		return itemDao.listStagedItemInfo(comId,itemId);
	}
	/**
	 * 获取项目阶段选择JSON字符串
	 * @param comId
	 * @param itemId
	 * @return
	 */
	public String stagedItemForRelevanceJsonStr(Integer comId,Integer itemId){
		List<ItemStagedInfo> listItemStagedInfo = itemDao.listStagedItemForRelevance(comId,itemId);
		StringBuffer strJson=new StringBuffer("[");
		if(null!=listItemStagedInfo && !listItemStagedInfo.isEmpty()){
			for(ItemStagedInfo vo:listItemStagedInfo){
				if(null!=vo.getType() && !"".equals(vo.getType()) && "folder".equals(vo.getType())){
					strJson.append("{\"id\":\""+vo.getKey()+"\",\"pId\":\""+vo.getParentId()+"\",\"name\":\""+vo.getName()+"\",realId:\""+vo.getRealId()+"\",type:\""+vo.getType()+"\"");
					strJson.append(", icon:\"/static/images/folder_add.png\",iconOpen:\"/static/images/folder_open.png\", iconClose:\"/static/images/folder_database.png\"");
					strJson.append(",moduleId:\""+vo.getModuleId()+"\"},");
				}else{
					strJson.append("{\"id\":\""+vo.getKey()+"\",\"pId\":\""+vo.getParentId()+"\",\"name\":\"【任务】"+vo.getName()+"\",realId:\""+vo.getRealId()+"\",type:\""+vo.getType()+"\",dropInner:false");
					strJson.append(",icon:\"/static/images/page_white_edit.png\",\"click\":\"stagedInfoView('/task/viewTask?pager.pageSize=15&id="+vo.getModuleId()+"')\"");
					strJson.append(",moduleId:\""+vo.getModuleId()+"\"");
					strJson.append("},");
				}
			}
			strJson=new StringBuffer(strJson.substring(0,strJson.lastIndexOf(",")));
		}
		strJson.append("]");
		return strJson.toString();
	}
	/**
	 * 模块数据和项目阶段节点关联
	 * @param stagedInfo
	 * @param taskName 
	 * @param stagedName 
	 * @throws Exception 
	 */
	public void addStagedInfo(StagedInfo stagedInfo,UserInfo userInfo, String stagedName, String taskName) throws Exception{
		StagedInfo checkObj = itemDao.queryItemHaveStagedInfo(userInfo.getComId(),stagedInfo.getItemId(),stagedInfo.getStagedItemId(),stagedInfo.getModuleId(),stagedInfo.getModuleType());
		if(null == checkObj){
			if("task".equals(stagedInfo.getModuleType())){
				//任务项目关联
				Task task = new Task();
				task.setId(stagedInfo.getModuleId());
				task.setBusId(stagedInfo.getItemId());
				task.setBusType(ConstantInterface.TYPE_ITEM);
				itemDao.update(task);
			}
			itemDao.add(stagedInfo);
			//项目修改更新时间
			Item item = new Item();
			item.setId(stagedInfo.getItemId());
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), stagedInfo.getItemId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, stagedInfo.getItemId(),"在\""+stagedName+"\"下创建任务\""+taskName+"\"", BusinessTypeConstant.type_item, shares,null);
			//更新项目索引
//			this.updateItemIndex(stagedInfo.getItemId(), userInfo,"update");
		}
	}
	/**
	 * 获取项目环节信息
	 * @param itemId
	 * @param stagedItemId
	 * @param comId
	 * @return
	 */
	public StagedItem queryStagedItem(Integer itemId,Integer stagedItemId,Integer comId){
		return itemDao.queryStagedItem(itemId,stagedItemId,comId);
	}
	/**
	 * 添加项目阶段文件夹
	 * @param stagedItem
	 * @throws Exception 
	 */
	public Integer addStagedFolder(StagedItem stagedItem,UserInfo userInfo) throws Exception{
		//项目修改更新时间
		Item item = new Item();
		item.setId(stagedItem.getItemId());
		item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		itemDao.update(item);
		
		//获取项目所有参与人集合没有督察人员
		//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), stagedItem.getItemId());
		//添加待办提醒通知
		//todayWorksService.updateTodayWorks(userInfo,null, stagedItem.getItemId(),"创建文件夹:\""+stagedItem.getStagedName()+"\"", BusinessTypeConstant.type_item, shares,null);
		//更新项目索引
//		this.updateItemIndex(stagedItem.getItemId(), userInfo,"update");
		return itemDao.add(stagedItem);
	}
	/**
	 * 项目数节点拖拽事件更新
	 * @param nodeId
	 * @param pId
	 * @param nodeType
	 * @param comId
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean zTreeOnDrop(Integer nodeId,Integer pId,Integer itemId,String nodeType,Integer comId) throws Exception{
		boolean succ = true;
		try {
			itemDao.zTreeOnDrop(nodeId,pId,itemId,nodeType,comId);
		} catch (Exception e) {
			succ = false;
			throw e;
		}
		return succ;
	}
	/**
	 * 获取项目环节信息
	 * @param id
	 * @param itemId
	 * @param comId
	 * @param nodeType
	 * @return
	 */
	public StagedInfo queryStagedInfo(Integer id,Integer itemId,Integer comId,String nodeType){
		return itemDao.queryStagedInfo(id,itemId,comId,nodeType);
	}
	/**
	 * 删除项目节点数据
	 * @param nodeId 节点ID
	 * @param itemId 项目主键
	 * @param nodeType 节点类型
	 * @param comId 企业唯一标识符
	 * @param moduleId 业务主键
	 * @param delChildren 是否删除子集标识符
	 * @param dropStagedItem 删除的项目阶段对象
	 * @param logInfo 变动描述
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delzTree(Integer nodeId,Integer itemId,String nodeType,Integer comId,Integer moduleId,String delChildren,
			StagedItem dropStagedItem,UserInfo userInfo, String logInfo) throws Exception{
		boolean succ = true;
		try {
			if("folder".equals(nodeType)){
				//项目阶段删除
				//是否删除子集
				if("yes".equals(delChildren)){
					//删除子集
					if(null!=dropStagedItem.getStagedItemChildren() && !dropStagedItem.getStagedItemChildren().isEmpty()){
						for(ItemStagedInfo vo :dropStagedItem.getStagedItemChildren()){
							if("folder".equals(vo.getType())){
								if(vo.getChildren() > 0){
									List<ItemStagedInfo> listChildren = itemDao.listStagedItemChildren(comId, itemId, vo.getRealId());
									StagedItem cStagedItem = itemDao.queryStagedItem(itemId,vo.getRealId(),comId);
									cStagedItem.setStagedItemChildren(listChildren);
									this.delzTree(vo.getRealId(), itemId,vo.getType(), comId, 0, delChildren, cStagedItem,userInfo,logInfo);
								}else{
									if("file".equals(vo.getType()) || "task".equals(vo.getType())){
										//项目阶段业务数据删除
										itemDao.delByField("stagedInfo", new String[]{"comId","itemId","id"},new Object[]{comId,itemId,vo.getRealId()});
									}else if("itemUpfile".equals(vo.getType())){
										//项目附件数据删除
										itemDao.delByField("itemUpfile", new String[]{"comId","itemId","id"},new Object[]{comId,itemId,vo.getRealId()});
									}else if("itemTalkfile".equals(vo.getType())){
										//项目留言附件数据删除
										itemDao.delByField("itemTalkfile", new String[]{"comId","itemId","id"},new Object[]{comId,itemId,vo.getRealId()});
									}
									//项目阶段业务数据删除
									itemDao.delByField("stagedInfo", new String[]{"comId","itemId","stageditemid"},new Object[]{comId,itemId,vo.getRealId()});
								}
							}else{
								if("file".equals(vo.getType()) || "task".equals(vo.getType())){
									//项目阶段业务数据删除
									itemDao.delByField("stagedInfo", new String[]{"comId","itemId","id"},new Object[]{comId,itemId,vo.getRealId()});
								}else if("itemUpfile".equals(vo.getType())){
									//项目阶段业务数据删除
									itemDao.delByField("itemUpfile", new String[]{"comId","itemId","id"},new Object[]{comId,itemId,vo.getRealId()});
								}else if("itemTalkfile".equals(vo.getType())){
									//项目阶段业务数据删除
									itemDao.delByField("itemTalkfile", new String[]{"comId","itemId","id"},new Object[]{comId,itemId,vo.getRealId()});
								}
							}
						}
					}
				}else{//将下级向上提一级
					
					if(dropStagedItem.getParentId()==-1){//当前删除的文件夹是顶层文件夹
						//判断当前节点是否有子文件夹（取得一级子文件夹）
						List<StagedItem> list = itemDao.listStagedItem(comId, itemId, dropStagedItem.getId());
						if(null!=list && !list.isEmpty()){//有子文件夹，将当前的文件以及文件放入最先建立的文件夹中
							//将要移动的子文件夹
							StagedItem stageLevItem = list.get(0);
							
							Integer stagedItemId = stageLevItem.getId();
							for (StagedItem stagedItem : list) {
								if(stagedItem.getId().equals(stagedItemId)){
									//项目阶段所属节点上级换成最近的
									itemDao.stagedItemParentUpdate(comId, dropStagedItem.getId(),-1);
								}else{
									//项目阶段所属节点上级换成最近的
									itemDao.stagedItemParentUpdate(comId, dropStagedItem.getId(),stagedItemId);
								}
							}
							
							//业务所属节点的上级换成最近的
							itemDao.stagedinfoStageditemidUpdate(comId,dropStagedItem.getId(),stagedItemId,itemId);
							//将项目附件和项目留言附件重新关联项目阶段
							itemDao.updateFileStagedId(comId,dropStagedItem.getId(),stagedItemId,itemId);
							
							
						}else{
							Integer stagedItemId = itemDao.getLatestStagedItem(userInfo.getComId(),itemId,dropStagedItem.getId()).getId();
							//业务所属节点的上级换成最近的
							itemDao.stagedinfoStageditemidUpdate(comId,dropStagedItem.getId(),stagedItemId,itemId);
							//将项目附件和项目留言附件重新关联项目阶段
							itemDao.updateFileStagedId(comId,dropStagedItem.getId(),stagedItemId,itemId);
							//项目阶段所属节点上级换成最近的
							itemDao.stagedItemParentUpdate(comId, dropStagedItem.getId(),stagedItemId);
							
						}
						
					}else{
						//业务所属节点向上调一级
						itemDao.stagedinfoStageditemidUpdate(comId,dropStagedItem.getId(),dropStagedItem.getParentId(),itemId);
						//将项目附件和项目留言附件重新关联项目阶段
						itemDao.updateFileStagedId(comId,dropStagedItem.getId(),dropStagedItem.getParentId(),itemId);
						//项目阶段所属节点向上调一级
						itemDao.stagedItemParentUpdate(comId, dropStagedItem.getId(),dropStagedItem.getParentId());
					}
					
				}
				//项目阶段文件夹删除
				itemDao.delByField("stagedItem", new String[]{"comId","itemId","id"},new Object[]{comId,itemId,dropStagedItem.getId()});
			}else{
				
				if("itemTalkFile".equalsIgnoreCase(nodeType)){//删除的项目留言附件
					//项目阶段业务数据删除
					itemDao.delById(ItemTalkFile.class, nodeId);
				}else if("itemUpFile".equalsIgnoreCase(nodeType)){//删除的是项目附件
					//项目阶段业务数据删除
					itemDao.delById(ItemUpfile.class, nodeId);
				}else{
					//项目阶段明细是否为任务
					StagedInfo stagedInfo = (StagedInfo) itemDao.objectQuery(StagedInfo.class, nodeId);
					if(null!=stagedInfo && !stagedInfo.getModuleType().equals("task")){//不为任务的才删除
						//项目阶段业务数据删除
						itemDao.delById(StagedInfo.class, nodeId);
					}
				}
			}
			//项目修改更新时间
			Item item = new Item();
			item.setId(itemId);
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), itemId);
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,itemId,logInfo, BusinessTypeConstant.type_item, shares,null);
			//更新项目索引
//			this.updateItemIndex(itemId, userInfo,"update");
		} catch (Exception e) {
			succ = false;
			throw e;
		}
		return succ;
	}
	/**
	 * 查询项目阶段文件夹下的所有子集
	 * @param comId
	 * @param itemId
	 * @param pId
	 * @return
	 */
	public List<ItemStagedInfo> listStagedItemChildren(Integer comId,Integer itemId,Integer pId){
		return itemDao.listStagedItemChildren(comId, itemId, pId);
	}
	/**
	 * 获取项目阶段信息集合
	 * @param stagedItem
	 * @return
	 */
	public List<StagedItem> listStagedItem(Integer comId,Integer itemId,Integer parentId){
		return itemDao.listStagedItem(comId,itemId,parentId);
	}
	/**
	 * 获取项目阶段内文件夹排序号
	 * @param stagedItem
	 * @return
	 */
	public StagedItem initStagedFolderOrder(StagedItem stagedItem){
		return itemDao.initStagedFolderOrder(stagedItem);
	}
	/**
	 *  项目阶段名称更新
	 * @param stagedItem
	 * @param userInfo
	 * @param oldStaged
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateStagedName(StagedItem stagedItem,UserInfo userInfo, StagedItem oldStaged) throws Exception{
		boolean succ = true;
		try {
			//项目阶段名称更新
			itemDao.update("update stagedItem a set a.stagedName=:stagedName where a.comid=:comId and a.itemId=:itemId and a.id=:id", stagedItem);

			//项目修改更新时间
			Item item = new Item();
			item.setId(stagedItem.getItemId());
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), stagedItem.getItemId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,stagedItem.getItemId(),"更新项目\""+oldStaged.getItemName()+"\"的项目阶段\""+oldStaged.getStagedName()+"\"为\""+stagedItem.getStagedName()+"\"", BusinessTypeConstant.type_item, shares,null);
			//更新项目索引
//			this.updateItemIndex(stagedItem.getItemId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 更新项目阶段排序
	 * @param stagedItem
	 * @param userInfo 
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateStagedOrder(StagedItem stagedItem, UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//更新项目阶段排序
			itemDao.update("update stagedItem a set a.stagedOrder=:stagedOrder where a.comid=:comId and a.itemId=:itemId and a.id=:id", stagedItem);
			//向项目参与人汇报项目更新
			
			//项目修改更新时间
			Item item = new Item();
			item.setId(stagedItem.getItemId());
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), stagedItem.getItemId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,stagedItem.getItemId(),"更新项目阶段排序", BusinessTypeConstant.type_item, shares,null);
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 删除项目阶段
	 * @param stagedItem
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delItemStaged(StagedItem stagedItem,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//删除项目阶段
			itemDao.update("delete from stagedItem a where a.comid=:comId and a.itemId=:itemId and a.id=:id", stagedItem);
			
			//项目修改更新时间
			Item item = new Item();
			item.setId(stagedItem.getItemId());
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), stagedItem.getItemId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,stagedItem.getItemId(),"删除项目阶段", BusinessTypeConstant.type_item, shares,null);
			//更新项目索引
//			this.updateItemIndex(stagedItem.getItemId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 获取项目的最新的一级项目阶段对象
	 * @param comId
	 * @param itemId
	 * @return
	 */
	public StagedItem queryTheLatestStagedItem(Integer comId,Integer itemId){
		return itemDao.queryTheLatestStagedItem(comId,itemId);
	}
	/**
	 * 根据业务模块主键和业务类型查询项目阶段明细
	 * @param comId
	 * @param itemId
	 * @param moduleId
	 * @param type
	 * @return
	 */
	public List<StagedInfo> listStagedInfoBymoduleIdAndType(Integer comId,Integer moduleId,String type){
		return itemDao.listStagedInfoBymoduleIdAndType(comId,moduleId,type);
	}
	/**
	 * 更新项目索引
	 * @param itemId
	 * @param userInfo
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception 
	 */
	public void updateItemIndex(Integer itemId,UserInfo userInfo,String opType) throws Exception{
		//更新项目索引
		Item item = itemDao.queryItemById(itemId,userInfo);
		if(null==item){return;}
		//再添加新索引数据
		//把bean非空属性值连接策划那个字符串
		//不检索字段设置为null
//		item.setId(null);
//		item.setComId(null);
//		item.setParentId(null);
//		item.setPartnerId(null);
//		item.setOwner(null);
//		item.setCreator(null);
//		item.setItemProgress(null);
//		item.setState(null);
//		item.setUuid(null);
//		item.setFilename(null);
//		item.setGender(null);
//		item.setTalkSum(null);
//		item.setSonItemNum(null);
//		item.setPromptMsg(null);
//		item.setShareMsg(null);
//		StringBuffer attStr = new StringBuffer(CommonUtil.objAttr2String(item,null));
		StringBuffer attStr = new StringBuffer(item.getItemName());
		//获取项目所有参与人
//		List<ItemSharer> listItemSharer = itemDao.listItemOwners(userInfo.getComId(),itemId);
//		//参与人名称连接成字符串创建索引
//		for(ItemSharer vo : listItemSharer){
//			attStr.append(vo.getSharerName()+",");
//		}
		//项目讨论记录创建字符串索引
//		List<ItemTalk> listItemTalk = itemDao.listItemTalk4Index(userInfo.getComId(),itemId);
//		for(ItemTalk vo:listItemTalk){
//			attStr.append(vo.getContent()+","+vo.getSpeakerName()+",");
//		}
		//项目子项目集合为其创建索引
//		List<Item> listItem = itemDao.listSonItem4Index(userInfo.getComId(),itemId);
//		for(Item vo : listItem){
//			attStr.append(vo.getItemName()+",");
//		}
		//项目阶段信息索引创建
//		List<ItemStagedInfo> listItemStagedInfo = itemDao.listStagedItemInfo(userInfo.getComId(),itemId);
//		for(ItemStagedInfo vo : listItemStagedInfo){
//			attStr.append(vo.getName()+",");
//		}
		//获取项目附件集合
//		List<ItemUpfile> listUpfiles = itemDao.listItemUpfile(itemId,userInfo.getComId());
//		if(null!=listUpfiles){
//			Upfiles upfile = null;
//			for(ItemUpfile vo:listUpfiles){
//				//附件内容添加
//				upfile = uploadService.queryUpfile4Index(vo.getUpfileId(),userInfo.getComId());
//				//附件名称
//				attStr.append(upfile.getFilename()+",");
//				//附件内容
//				attStr.append(upfile.getFileContent()+",");
//			}
//		}
		//线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String index_key = userInfo.getComId()+"_"+ConstantInterface.TYPE_ITEM+"_"+itemId;
		//为项目创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				index_key,userInfo.getComId(),itemId,ConstantInterface.TYPE_ITEM,
				item.getItemName(),attStr.toString(),DateTimeUtil.parseDate(item.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,index_key));
		}
		
	}
	/**
	 * 更新项目所有后代参与人
	 * @param itemId
	 * @param comId
	 */
	public void updateItemSharerForChildren(int itemId,int comId){
		//获取当前项目所有参与人
		List<ItemSharer> listCurItemSharer = itemDao.listItemOwners(comId,itemId);
		//当前任务参与人MAP
		Map<Integer,Integer> sharerMap = null;
		//获取任务的所有后代任务
		List<Item> listItemOfChildren = itemDao.listItemOfOnlyChildren(itemId,comId);
		if(null!=listItemOfChildren && !listItemOfChildren.isEmpty()){
			List<ItemSharer> listItemOwners = null;
			for(Item vo:listItemOfChildren){
				sharerMap = new HashMap<Integer,Integer>();
				if(null!=listCurItemSharer && !listCurItemSharer.isEmpty()){
					for(ItemSharer sharer:listCurItemSharer){
						sharerMap.put(sharer.getUserId(),comId);
					}
				}
				ItemSharer itemSharer =null;
				listItemOwners = itemDao.listItemOwners(comId,vo.getId());
				if(null!=listItemOwners && !listItemOwners.isEmpty()){
					for(ItemSharer sharer:listItemOwners){
						sharerMap.put(sharer.getUserId(),comId);
					}
					//先删除项目的参与人
					//先删除项目的参与人
					itemDao.delByField("itemSharer", new String[]{"comId","itemId"},new Integer[]{comId,vo.getId()});
					//把父任务所有相关人员添加为子任务的参与人
					for(Map.Entry<Integer,Integer> entry:sharerMap.entrySet()){    
						itemSharer = new ItemSharer();
						itemSharer.setComId(comId);
						itemSharer.setItemId(vo.getId());
						itemSharer.setUserId(entry.getKey());
						itemDao.add(itemSharer);    
					}
				}
			}
		}
	}
	/**
	 * stagedInfoFile
	 * @param stagedInfoFile
	 * @param userInfo
	 * @throws Exception 
	 */
	public void addStagedFile(StagedInfo stagedInfoFile, UserInfo userInfo) throws Exception {
		List<Upfiles> files = stagedInfoFile.getListFiles();
		if(null != files && files.size() > 0){
			List<Integer> fileIds = new  ArrayList<Integer>();
			for (Upfiles upfiles : files) {
				StagedInfo stagedFile = new StagedInfo();
				//企业编号
				stagedFile.setComId(userInfo.getComId());
				//项目主键
				stagedFile.setItemId(stagedInfoFile.getItemId());
				//项目阶段主键
				stagedFile.setStagedItemId(stagedInfoFile.getStagedItemId());
				//关联附件主键
				stagedFile.setModuleId(upfiles.getId());
				//项目详情类型
				stagedFile.setModuleType("file");
				//上传人
				stagedFile.setCreator(userInfo.getId());
				
				itemDao.add(stagedFile);
				
				fileIds.add(upfiles.getId());
			}
			
			//附件归档
			Item itemT = (Item) itemDao.objectQuery(Item.class, stagedInfoFile.getItemId());
			fileCenterService.addModFile(userInfo, fileIds, itemT.getItemName());
			
			//项目修改更新时间
			Item item = new Item();
			item.setId(stagedInfoFile.getItemId());
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(item);
			
		}
		
	}
	/**
	 * 项目阶段文件夹下一级数量
	 * @param id 项目阶段主键
	 * @param itemId 项目主键
	 * @param comId 企业编号
	 * @return
	 */
	public Integer countItemStageChildren(Integer id, Integer itemId, Integer comId) {
		return itemDao.countItemStageChildren(id,itemId,comId);
	}
	
	/**
	 * 项目阶段树拖拽前验证，如果被托节点是目标节点的子节点，则验证不通过
	 * @param itemId
	 * @param childId
	 * @param parentId
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean zTreeBeforeDropCheckForItem(int itemId,int childId,int parentId,UserInfo userInfo) throws Exception{
		boolean succ = true;
		StagedItem stagedItem = itemDao.zTreeBeforeDropCheck(itemId, childId, parentId, userInfo);
		if(null==stagedItem){
			succ = true ;
		}else{
			succ = false ;
		}
		return succ;
	}
	/**
	 * 人员负责的项目
	 * @param comId 企业号
	 * @param userId 负责人主键
	 * @return
	 */
	public List<Item> listUserAllItem(Integer comId, Integer userId) {
		return itemDao.listUserAllItem(comId,userId);
	}
	/**
	 * 项目移交记录
	 * @param itemId 项目主键
	 * @param comId 企业号
	 * @return
	 */
	public List<FlowRecord> listFlowRecord(Integer itemId, Integer comId) {
		return itemDao.listFlowRecord(itemId,comId);
	}
	/**
	 * 项目详情（不需要分享人）
	 * @param itemId项目主键
	 * @param user 操作人员
	 * @return
	 */
	public Item getItemById(Integer itemId, UserInfo user) {
		return itemDao.queryItemById(itemId, user);
	}
	/**
	 * 项目详情（纯粹相项目信息）
	 * @param itemId
	 * @return
	 */
	public Item getItemById(Integer itemId) {
		return (Item) itemDao.objectQuery(Item.class, itemId);
	}
	/**
	 * 获取项目成员组集合
	 * @param itemId
	 * @param userId
	 * @param comId
	 * @return
	 */
	public List<SelfGroup> listSelfGroupOfItem(int itemId,int userId,int comId){
		return itemDao.listSelfGroupOfItem(itemId,userId,comId);
	}
	/**
	 * 获取与项目已经关联的组集合
	 * @param itemId
	 * @param comId
	 * @return
	 */
	public List<SelfGroup> listShareGroupOfItem(int itemId,int comId){
		return itemDao.listShareGroupOfItem(itemId,comId);
	}
	/**
	 * 项目成员组关联
	 * @param itemId
	 * @param grpIds
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateItemGroup(Integer itemId,Integer[] grpIds,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//客户信息分享组中需要移除的成员
			List<UserInfo> removeGrpUsers = itemDao.listRemoveCrmGrp(userInfo.getComId(), itemId,grpIds);
			
			//附件和分享人不为空
			if(null != removeGrpUsers && removeGrpUsers.size()>0 ){
				for (UserInfo user : removeGrpUsers) {
					//删除设置了提醒的闹铃
					clockService.delClockByUserId(userInfo.getComId(),user.getId(),itemId,ConstantInterface.TYPE_ITEM);
				}
			}
			
			//先删除项目组成员关联
			itemDao.delByField("itemShareGroup", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),itemId});
			//项目删除分享组
			if(null!=grpIds && grpIds.length>0){
				ItemShareGroup itemShareGroup = null;
				for(int id:grpIds){
					itemShareGroup = new ItemShareGroup();
					itemShareGroup.setComId(userInfo.getComId());
					itemShareGroup.setGrpId(id);
					itemShareGroup.setItemId(itemId);
					itemDao.add(itemShareGroup);
				}
			}
			List<SelfGroup> listShareGroup = itemDao.listShareGroupOfItem(itemId,userInfo.getComId());
			StringBuffer shareGroup = new StringBuffer();
			if(null!=listShareGroup && !listShareGroup.isEmpty()){
				for(SelfGroup vo:listShareGroup){
					shareGroup.append(vo.getGrpName()+"、");
				}
				shareGroup = new StringBuffer(shareGroup.substring(0,shareGroup.lastIndexOf("、")));
			}
			//项目修改更新时间
			Item item = new Item();
			item.setId(itemId);
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), itemId);
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,itemId,"项目成员组关联为\""+shareGroup.toString()+"\"", BusinessTypeConstant.type_item, shares,null);
			//项目模块日志添加
			this.addItemLog(userInfo.getComId(),itemId,userInfo.getId(), userInfo.getUserName(), "项目成员组关联为\""+shareGroup.toString()+"\"");
			//更新项目索引库
//			this.updateItemIndex(itemId, userInfo, "update");
		} catch (Exception e) {
			succ = false;
			throw e;
		}
		return succ;
	}
	/**
	 * 获取与查询项目名称相似的项目集合
	 * @param itemName
	 * @param comId
	 * @return
	 */
	public List<Item> listSimilarItemsByCheckItemName(String itemName,int comId){
		return itemDao.listSimilarItemsByCheckItemName(itemName,comId);
	}
	/**
	 * 项目移交
	 * @param itemHandOvers 移交的项目信息集合
	 * @param userInfo 操作员
	 * @param msgShare
	 * @return
	 * @throws Exception
	 */
	public boolean addItemHandOver(ItemHandOver[] itemHandOvers,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			if(null!=itemHandOvers){
				Item item = null;
				ItemTalk itemTalk = null;
				for(ItemHandOver itemHandOver:itemHandOvers){
					Integer itemId = itemHandOver.getItemId();
					//把移交说明添加到项目讨论当中
					if(!StringUtil.isBlank(StringUtil.trim(itemHandOver.getReplayContent()))){
						itemTalk = new ItemTalk();
						itemTalk.setComId(userInfo.getComId());
						itemTalk.setContent(itemHandOver.getReplayContent());
						itemTalk.setItemId(itemId);
						itemTalk.setParentId(-1);
						itemTalk.setUserId(userInfo.getId());
						itemDao.add(itemTalk);
					}
					
//					//移交添加分组
//					List<SelfGroup> listGrp = selfGroupService.addGrp(userInfo,itemId,itemHandOver.getToUser(),BusinessTypeConstant.type_item);
//					if(null!=listGrp && listGrp.size()>0){
//						itemDao.delByField("customerShareGroup", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),itemId});
//						for (SelfGroup selfGroup : listGrp) {
//							//项目共享组
//							ItemShareGroup itemShareGroup = new ItemShareGroup();
//							//企业号
//							itemShareGroup.setComId(userInfo.getComId());
//							//客户主键
//							itemShareGroup.setItemId(itemId);
//							//分组主键
//							itemShareGroup.setGrpId(selfGroup.getId());
//							//添加项目共享组
//							itemDao.add(itemShareGroup);
//						}
//					}
					
					
					//项目负责人变更
					item = new Item();
					item.setComId(userInfo.getComId());
					item.setId(itemId);
					//项目修改更新时间
					item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
					item.setOwner(itemHandOver.getToUser());
					itemDao.update(item);
					if(null==itemHandOver.getFromUser()){
						itemHandOver.setFromUser(userInfo.getId());
					}
					
					//移交记录添加
					itemDao.add(itemHandOver);
					//获取移交对象人员信息
					UserInfo toUser = userInfoService.getUserInfo(userInfo.getComId(),itemHandOver.getToUser());
					
					//获取项目所有参与人集合没有督察人员
					//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(),itemId);
					//通知人为移交的对象
					List<UserInfo> shares = new ArrayList<UserInfo>();
					shares.add(toUser);
					//添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo,itemHandOver.getToUser(),itemHandOver.getItemId(),"项目移交给了\""+toUser.getUserName()+"\"",
							ConstantInterface.TYPE_ITEM, shares,null);
					
					
					item = (Item) itemDao.objectQuery(Item.class, itemId);
					
					if(item.getPubState().equals(0)) {//当分享范围为私有时
						//验证当前操作人是不是分享人，不是则把操作人存入项目参与人中
						ItemSharer oprator = itemDao.getSharerForOwner(itemId,userInfo.getComId(),userInfo.getId());
						if(null == oprator){
							ItemSharer sharer = new ItemSharer();
							sharer.setComId(userInfo.getComId());
							sharer.setItemId(itemId);
							sharer.setUserId(userInfo.getId());
							itemDao.add(sharer);
						}
					}
					
					
					//添加工作轨迹
					systemLogService.addSystemLogWithTrace(userInfo, itemHandOver.getToUser(), ConstantInterface.TYPE_ITEM, 
							itemHandOver.getItemId(), "移交的项目\""+item.getItemName()+"\"给了\""+toUser.getUserName()+"\"", "负责\""+userInfo.getUserName()+"\"移交的项目\""+item.getItemName()+"\"");
					
					//取得待办事项主键
					TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),itemHandOver.getToUser(),
							itemId,ConstantInterface.TYPE_ITEM,0);
					if(null!=todayWorks){
						JpushUtils.sendTodoMessage(userInfo.getComId(), itemHandOver.getToUser(), userInfo.getId(),
								todayWorks.getId(), itemId, ConstantInterface.TYPE_ITEM,0,"项目:"+item.getItemName());
					}					
					//模块日志添加
					this.addItemLog(userInfo.getComId(),itemId,userInfo.getId(),userInfo.getUserName(),"把项目移交给了\""+toUser.getUserName()+"\"");
					//更新项目索引库
//					this.updateItemIndex(itemId, userInfo, "update");
				}
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 查询项目当前设置父节点的父节点
	 * @param parentId
	 * @param comId
	 * @return
	 */
	public List<Item> getParentItem(Integer parentId, Integer comId) {
		List<Item> list = itemDao.getParentItem(parentId,comId);
		return list;
	}
	/**
	 * 解除项目上级关联
	 * @param item
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public boolean delpItemRelation(Item item, UserInfo userInfo){
		boolean succ = true;
		Item pItem = this.getItemById(item.getParentId());
		item.setpItemName(pItem.getItemName());
		
		try {
			//清除关联任务字段
			item.setParentId(-1);
			//项目修改更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//任务母任务关联
			itemDao.update(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), item.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,item.getId(),"解除与项目\""+item.getpItemName()+"\"间的关联关系", BusinessTypeConstant.type_item, shares,null);
			//更新任务索引
//			this.updateItemIndex(item.getId(), userInfo, "update");
			//模块日志添加
			this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "解除上级项目和\""+pItem.getItemName()+"\"的关联,成功");
			
		} catch (Exception e) {
			succ = false ;
			this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "解除上级项目和\""+pItem.getItemName()+"\"的关联,失败");
		}
		return succ;
	}
	/**
	 * 解除客户关联
	 * @param item
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public boolean delCrmRelation(Item item, UserInfo userInfo){
		boolean succ = true;
		
		Customer crm= crmService.getCrmById(item.getPartnerId());
		item.setPartnerName(crm.getCustomerName());
		
		try {
			//清除关联客户字段
			item.setPartnerId(0);
			//项目修改更新时间
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//修改客户关联
			itemDao.update(item);
			
			//获取项目所有参与人集合没有督察人员
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), item.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,item.getId(),"解除与客户\""+item.getPartnerName()+"\"的关联关系", BusinessTypeConstant.type_item, shares,null);
			//更新任务索引
//			this.updateItemIndex(item.getId(), userInfo, "update");
			
			//模块日志添加
			this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "解除与客户\""+crm.getCustomerName()+"\"的关联,成功");
		} catch (Exception e) {
			succ = false ;
			this.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "解除与客户\""+crm.getCustomerName()+"\"的关联,失败");
		}
		return succ;
	}
	/**
	 * 获取自己权限下前N个项目集合
	 * @param userInfo
	 * @param isForceInPersion
	 * @param rowNum
	 * @return
	 */
	public List<Item> firstNItemList(UserInfo userInfo,boolean isForceInPersion,Integer rowNum){
		return itemDao.firstNItemList(userInfo,isForceInPersion,rowNum);
	}
	
	/**
	 * 消息的接收人员集合
	 * @param comId 企业号
	 * @param modId 模块主键
	 * @return
	 */
	public List<UserInfo> listItemOwnersNoForce(Integer comId, Integer modId){
		List<UserInfo> shares = itemDao.listItemOwnersNoForce(comId, modId);
		return shares;
	}
	/**
	 * 合并项目信息
	 * @param item 合并后的项目
	 * @param ids 参与合并的项目
	 * @param userInfo 操作人员
	 * @throws Exception 
	 */
	public void updateItemForCompress(Item item, Integer[] ids,
			UserInfo userInfo) throws Exception {
		//有需要合并的项目
		if(null != ids && ids.length > 0){
			//合并后项目主键
			Integer itemId = item.getId();
			//合并项目的主要信息
			itemDao.update(item);
			
			//整合的项目分享人
			List<ItemSharer> itemSharerList = itemDao.listItemSharer(itemId,userInfo.getComId());
			//整合的客户分享人主键
			Set<Integer> sharerId = new HashSet<Integer>();
			
			if(null!=itemSharerList && itemSharerList.size()>0){
				for (ItemSharer itemSharer : itemSharerList) {
					sharerId.add(itemSharer.getUserId());
				}
			}
			
			//合并前关注客户信息的人员
			List<Attention> listAtten = attentionService.listAtten(userInfo.getComId(), itemId, ConstantInterface.TYPE_ITEM);
			//合并前关注客户信息的人员主键
			Set<Integer> attenUserId = new HashSet<Integer>();
			if(null != listAtten && listAtten.size()>0){
				for (Attention attention : listAtten) {
					attenUserId.add(attention.getUserId());
				}
			}
			
			String log = "";
			//开始合并信息
			for (Integer itemCId : ids) {
				//取得项目信息
				Item itemT = (Item) itemDao.objectQuery(Item.class, itemCId);
				log +="'"+itemT.getItemName()+"',";
				if(itemCId.equals(itemId)){//是合并的项目
					continue;
				}
				//直接合并留言信息和附件
				itemDao.compressTalk(userInfo.getComId(), itemCId,itemId);
				//直接合并任务关联信息
				itemDao.compressTask(userInfo.getComId(), itemCId,itemId);
				//直接合并需求关联信息
				itemDao.compressDemand(userInfo.getComId(), itemCId,itemId);
				//直接合并项目阶段明细信息
				itemDao.compressStageInfo(userInfo.getComId(), itemCId,itemId);
				//直接合并项目阶段信息
				itemDao.compressStageItem(userInfo.getComId(), itemCId,itemId,itemT.getItemName());
				//整合子项目
				itemDao.compressSonItem(userInfo.getComId(), itemCId,itemId);
				//整合闹铃
				clockService.compressClock(userInfo.getComId(), itemCId,itemId,ConstantInterface.TYPE_ITEM);
				//合并聊天室
				chatService.comPressChat(userInfo.getComId(), itemCId,itemId,ConstantInterface.TYPE_ITEM);
				//合并附件留言
				fileCenterService.comPressFileTalk(userInfo.getComId(),itemCId,itemId,ConstantInterface.TYPE_ITEM);
				//合并分享人
				//整合的项目分享人
				List<ItemSharer> itemSharerCList = itemDao.listItemSharer(itemCId,userInfo.getComId());
				if(null!=itemSharerCList &&itemSharerCList.size()>0){
					for (ItemSharer itemCSharer : itemSharerCList) {
						if(sharerId.contains(itemCSharer.getUserId())){//有共同参与人
							//删除参与人
							itemDao.delById(ItemSharer.class, itemCSharer.getId());
							continue;
						}else{//没有共同的分享人，就添加数据
							
							sharerId.add(itemCSharer.getUserId());
							
							//重新设置项目主键
							itemCSharer.setItemId(itemId);
							//修改参与人所属客户信息
							itemDao.update(itemCSharer);
						}
					}
				}
				//合并添加分组
				List<SelfGroup> listGrp = selfGroupService.addGrp(userInfo,itemCId,userInfo.getId(),ConstantInterface.TYPE_ITEM);
				if(null!=listGrp && listGrp.size()>0){
					itemDao.delByField("itemShareGroup", new String[]{"comId","itemId"}, new Object[]{userInfo.getComId(),itemCId});
					for (SelfGroup selfGroup : listGrp) {
						//项目共享组
						ItemShareGroup itemShareGroup = new ItemShareGroup();
						//企业号
						itemShareGroup.setComId(userInfo.getComId());
						//项目主键
						itemShareGroup.setItemId(itemId);
						//分组主键
						itemShareGroup.setGrpId(selfGroup.getId());
						//添加项目共享组
						itemDao.add(itemShareGroup);
					}
				}
				//合并关注信息
				List<Attention> listAttention = attentionService.listAtten(userInfo.getComId(), itemCId, ConstantInterface.TYPE_ITEM);
				if(null != listAttention && listAttention.size() > 0){
					for (Attention attention : listAttention) {
						if(attenUserId.contains(attention.getUserId())){//人员同时关注合并的客户信息
							itemDao.delById(Attention.class, attention.getId());
							continue;
						}else{
							attenUserId.add(attention.getUserId());
							//重新设置业务主键
							attention.setBusId(itemId);
							itemDao.update(attention);
						}
					}
				}
				
				
				//删除移交记录表
				itemDao.delByField("itemHandOver", new String[]{"comId","itemId"}, 
						new Object[]{userInfo.getComId(), itemCId});
				//删除日志表
				itemDao.delByField("itemLog", new String[]{"comId","itemId"}, 
						new Object[]{userInfo.getComId(), itemCId});
				//删除浏览记录
				itemDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, 
						new Object[]{userInfo.getComId(), itemCId,ConstantInterface.TYPE_ITEM});
				//删除最新动态
				itemDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, 
						new Object[]{userInfo.getComId(), itemCId,ConstantInterface.TYPE_ITEM});
				//删除消息提醒
				itemDao.delByField("todayWorks", new String[]{"comId","busId","busType"}, 
						new Object[]{userInfo.getComId(), itemCId,ConstantInterface.TYPE_ITEM});
				//删除进度
				itemDao.delByField("itemProgress", new String[]{"comId","itemId"}, 
						new Object[]{userInfo.getComId(), itemCId});
				//删除功能清单
				itemDao.delByField("functionList", new String[]{"comId","busId","busType"}, 
						new Object[]{userInfo.getComId(), itemCId,ConstantInterface.TYPE_ITEM});
				
				itemDao.delById(Item.class, itemCId);
				
			}
			
			//项目的所有查看人
			//List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), itemId);
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,itemId,"合并项目"+log.substring(0,log.length()-1)+"信息", 
			//		BusinessTypeConstant.type_item, shares,null);
			//添加日志
			this.addItemLog(userInfo.getComId(), itemId, userInfo.getId(), 
					userInfo.getUserName(),  "合并项目"+log.substring(0,log.length()-1)+"信息");
			//更新任务索引
//			this.updateItemIndex(itemId, userInfo, "update");
		}
		
	}
	/**
	 * 任务关联的项目明细
	 * @param comId 企业号
	 * @param taskCId 任务主键
	 * @return
	 */
	public StagedInfo getStagedRelateInfo(Integer comId, Integer taskCId,String moduleType) {
		StagedInfo stagedInfo = itemDao.getStagedInfo(comId, taskCId,moduleType);
		return stagedInfo;
	}
	/**
	 * 整理项目阶段明细
	 * @param userInfo
	 */
	public void updateStagInfo(UserInfo userInfo) {
		List<Task> taskList = taskService.listTaskForStageInfo(userInfo);
		if(null!=taskList && taskList.size()>0){
			//任务对应的项目
			Map<Integer, Integer> taskItemMap = new HashMap<Integer, Integer>();
			
			//任务对应的客户
			Map<Integer, Integer> taskCrmMap = new HashMap<Integer, Integer>();
			
			//任务对应的项目阶段
			Map<Integer, Integer> taskStageMap = new HashMap<Integer, Integer>();
			for (Task task : taskList) {
				//任务主键
				Integer taskId = task.getId();
				//父任务主键
				Integer ptaskId = task.getParentId();
				//任务关联的模块主键
				Integer busId = task.getBusId();
				//任务关联的模块类型
				String busType = task.getBusType();
				//父任务关联的模块
				Integer pBusId = taskItemMap.get(ptaskId);
				
				//父任务是否关联项目
				if(null!=pBusId){//父任务关联的是项目
					//统一到父任务中
					busType = ConstantInterface.TYPE_ITEM;
				}else{//不是关联的
					pBusId = taskCrmMap.get(ptaskId);
					if(null!=pBusId){//父任务关联的是客户
						//统一到父任务中
						busType = ConstantInterface.TYPE_CRM;
					}else{
						pBusId = 0;//没有父任务,取得自己的模块类型
					}
				}
				
				//任务所在的项目阶段
				Integer stagedItemId = null;
				//父任务所在的项目阶段
				Integer pstagedItemId = taskStageMap.get(ptaskId);
				
				//任务关联的模块是否删除
				Integer busDelState = task.getBusDelState();
			
				if(pBusId==0){//父任务也没有关联模块
					if(null==busId|| busId==0){//任务没有关联模块
						continue;
					}
					//任务自身关联了项目，统一到自己设定的项目阶段中
					if(busDelState==1){//关联的模块已被删除，不用修改数据
						//首先修改任务关联的项目
						task.setBusId(0);
						task.setBusType("0");
						itemDao.update(task);
						if(ConstantInterface.TYPE_ITEM.equals(busType)){//自身关联的项目
							//删除任务的项目阶段关联
							itemDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"},
									new Object[]{userInfo.getComId(), taskId,"task"});
						}
						continue;
					}
					//以下一定有关联模块
					if(ConstantInterface.TYPE_ITEM.equals(busType)){//自身关联的项目
						//取得任务所在的项目阶段
						StagedInfo stagedInfo = itemDao.getStagedInfo(userInfo.getComId(), taskId,ConstantInterface.STAGED_TASK);
						if(null!=stagedInfo){//关联了项目,并且项目阶段中也出现了
							//项目阶段主键
							stagedItemId = stagedInfo.getStagedItemId();
							if(!busId.equals(stagedInfo.getItemId())){//关联的项目不一致,修改阶段明细
								//添加项目日志
								this.addItemLog(userInfo.getComId(), stagedInfo.getItemId(), userInfo.getId(),
										userInfo.getUserName(), "整理项目阶段明细，任务"+task.getTaskName()+"解除项目关联");
								//添加项目日志
								this.addItemLog(userInfo.getComId(), busId, userInfo.getId(),
										userInfo.getUserName(), "整理项目阶段明细，任务"+task.getTaskName()+"添加项目关联");
								//设置项目信息
								stagedInfo.setItemId(busId);
								//取得项目阶段主键
								stagedItemId = this.getStageItemId(userInfo,busId);
								//设置项目阶段信息
								stagedInfo.setStagedItemId(pstagedItemId);
								//修改项目阶段明细数据
								itemDao.update(stagedInfo);
							}
						}else{
							//取得项目阶段主键
							stagedItemId = this.getStageItemId(userInfo,busId);
							//添加项目阶段明细
							this.addStageRelateMod(userInfo,busId,stagedItemId,task.getId(),ConstantInterface.TYPE_TASK);
							
						}
						//添加任务--项目
						taskItemMap.put(taskId, busId);
						//添加任务--项目阶段
						taskStageMap.put(taskId, stagedItemId);
					}else if(ConstantInterface.TYPE_CRM.equals(busType)){//关联的客户
						//删除任务的项目阶段关联
						itemDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"},
								new Object[]{userInfo.getComId(), taskId,"task"});
						//添加任务--客户
						taskCrmMap.put(taskId, busId);
					}
				}else if(pBusId>0 && ConstantInterface.TYPE_ITEM.equals(busType)){//父任务关联的是项目
					//首先修改任务关联的项目
					task.setBusId(pBusId);
					task.setBusType(busType);
					
					itemDao.update(task);
					
					//统一到父任务的项目阶段
					stagedItemId = pstagedItemId;
					//看任务自身是否关联有项目阶段
					StagedInfo stagedInfo = itemDao.getStagedInfo(userInfo.getComId(), taskId,ConstantInterface.STAGED_TASK);
					if(null!=stagedInfo){//任务自身关联有项目阶段
						if(stagedInfo.getItemId().equals(pBusId)){//父任务关联的是同一个项目
							stagedItemId = stagedInfo.getStagedItemId();
						}else{//关联的不是同一个项目
							//添加项目日志
							this.addItemLog(userInfo.getComId(), stagedInfo.getItemId(), userInfo.getId(),
									userInfo.getUserName(), "整理项目阶段明细，任务"+task.getTaskName()+"解除项目关联");
							//添加项目日志
							this.addItemLog(userInfo.getComId(), busId, userInfo.getId(),
									userInfo.getUserName(), "整理项目阶段明细，任务"+task.getTaskName()+"添加项目关联");
							
							//设置关联项目
							stagedInfo.setItemId(pBusId);
							//设置关联项目阶段
							stagedInfo.setStagedItemId(stagedItemId);
							
							itemDao.update(stagedInfo);
						}
						
					}else{//任务自身没有关联项目阶段
						//添加项目阶段明细
						this.addStageRelateMod(userInfo,pBusId,stagedItemId,task.getId(),ConstantInterface.TYPE_TASK);
						//添加项目日志
						this.addItemLog(userInfo.getComId(), pBusId, userInfo.getId(),
								userInfo.getUserName(), "整理项目阶段明细，任务"+task.getTaskName()+"添加项目关联");
					}
					
					//添加任务--项目
					taskItemMap.put(taskId, pBusId);
					//添加任务--项目阶段
					taskStageMap.put(taskId, pstagedItemId);
				}else if(pBusId>0 && ConstantInterface.TYPE_CRM.equals(busType)){//父任务关联的客户
					//首先修改任务关联的项目
					task.setBusId(pBusId);
					task.setBusType(busType);
					
					itemDao.update(task);
					//删除任务的项目阶段关联
					itemDao.delByField("stagedInfo", new String[]{"comId","moduleId","moduleType"},
							new Object[]{userInfo.getComId(), taskId,"task"});
					
					//添加任务--客户
					taskCrmMap.put(taskId, busId);
				}
				
			}
		}
		
	}
	/**
	 * 添加项目阶段明细关联的任务
	 * @param userInfo 当亲操作人员
	 * @param itemId 项目主键
	 * @param stagedItemId 项目阶段主键
	 * @param busId 任务主键
	 */
	public void addStageRelateMod(UserInfo userInfo, Integer itemId,
			Integer stagedItemId, Integer busId,String busType) {
		StagedInfo stagedInfo = new StagedInfo();
		//企业号
		stagedInfo.setComId(userInfo.getComId());
		//对应的项目主键
		stagedInfo.setItemId(itemId);
		//取得对应阶段主键
		stagedInfo.setStagedItemId(stagedItemId);
		//设置项目阶段明细对应模块主键
		stagedInfo.setModuleId(busId);
		if(ConstantInterface.TYPE_TASK.equals(busType)){
			//对应的模块类型
			stagedInfo.setModuleType(ConstantInterface.STAGED_TASK);
		}else if(ConstantInterface.TYPE_FLOW_SP.equals(busType)){
			//对应的模块类型
			stagedInfo.setModuleType(ConstantInterface.STAGED_FLOW_SP);
			
		}
		//设置项目阶段明细创建人主键
		stagedInfo.setCreator(userInfo.getId());
		
		itemDao.add(stagedInfo);
		
	}
	/**
	 * 取得项目最近一次的项目阶段主键
	 * @param userInfo 操作人员
	 * @param itemId 项目主键
	 * @return
	 */
	public Integer getStageItemId(UserInfo userInfo, Integer itemId) {
		Integer stagedItemId = null;
		//取得最近
		StagedItem stagedItem =itemDao.queryTheLatestStagedItem(userInfo.getComId(),itemId);
		if(null!=stagedItem){
			stagedItemId = stagedItem.getId();
		}else{
			//创建项目默认第一阶段信息
			stagedItem = new StagedItem();
			//项目主键
			stagedItem.setItemId(itemId);
			//企业号
			stagedItem.setComId(userInfo.getComId());
			//创建人
			stagedItem.setCreator(userInfo.getId());
			//阶段名称
			stagedItem.setStagedName("项目前期");
			//默认排序
			stagedItem.setStagedOrder(1);
			//默认上级目录主键
			stagedItem.setParentId(-1);
			//项目阶段主键
			stagedItemId = itemDao.add(stagedItem);
		}
		return stagedItemId;
	}
	
	/**
	 * 获取项目下的所有任务
	 * @param comId 团队主键
	 * @param itemId 项目主键
	 * @return
	 */
	public List<Task> listTaskOfItem(Integer comId,Integer itemId){
		return itemDao.listTaskOfItem(comId,itemId);
	}
	/**
	 * 列出客户关联的项目
	 * @param item 项目
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public List<Item> listPagedCrmItem(Item item, UserInfo userInfo) {
		List<Item> listItem = itemDao.listPagedCrmItem(item,userInfo);
		return listItem;
	}
	
	/**
	 * 查询客户关联的项目数
	 * @param item
	 * @param userInfo
	 * @return
	 */
	public Integer countItem(Item item, UserInfo userInfo) {
		return itemDao.countItem(item,userInfo);
	}
	
	/**
	 * 取得客户项目项目数
	 * @param busType 业务主键
	 * @param busId 业务类型
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public Integer countBusItem(String busType,Integer busId, UserInfo userInfo) {
		Integer crmItemSum = itemDao.countBusItem(busType,busId,userInfo);
		return crmItemSum;
	}
	/**
	 * 取得需要关联的任务数据
	 * @param item
	 * @param userInfo
	 * @return
	 */
	public List<Item> listItemForRelevance(Item item, UserInfo userInfo) {
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);
		List<Item> listItem = itemDao.listItemForRelevance(item,userInfo,isForceIn);
		return listItem;
	}
	/**
	 * 修改任务的项目阶段数据
	 * @param userInfo
	 * @param taskId
	 * @param itemId
	 * @param stagedItemId
	 */
	public void updateStagInfo(UserInfo userInfo, Integer taskId,Integer itemId,
			Integer stagedItemId) {
		StagedInfo stagedInfo = itemDao.getStagedInfo(userInfo.getComId(), taskId,ConstantInterface.STAGED_TASK);
		if(null!=stagedInfo){
			stagedInfo.setStagedItemId(stagedItemId);
			itemDao.update(stagedInfo);
		}
	}
	/**
	 * 分页查询项目阶段信息
	 * @param userInfo
	 * @param itemId 项目主键
	 * @param realPid 阶段文件夹主键
	 * @param name 阶段内容名称
	 * @return
	 */
	public List<ItemStagedInfo> itemStagPagedList(UserInfo userInfo,
			Integer itemId, Integer realPid, String name) {
		List<ItemStagedInfo> listItemStagedInfo = itemDao.itemStagPagedList(userInfo.getComId(),itemId,realPid,name);
		return listItemStagedInfo;
	}
	/**
	 * 项目阶段用于选择
	 * @param userInfo 当前操作人员
	 * @param itemId 项目主键
	 * @param stagePid  阶段所在文件夹主键
	 * @return
	 */
	public List<ItemStagedInfo> itemStageListForRelevance(UserInfo userInfo,
			Integer itemId, Integer stagePid) {
		List<ItemStagedInfo> listItemStagedInfo = itemDao.itemStageListForRelevance(userInfo.getComId(),itemId,stagePid);
		return listItemStagedInfo;
	}
	/**
	 * 取得指定人员负责的项目数 app
	 * @param userId 指定人员主键
	 * @param comId 企业号
	 * @return
	 */
	public Integer countMyItem(Integer userId, Integer comId) {
		return itemDao.countMyItem(userId,comId);
	}
	/**
	 * 分页查询项目阶段详情
	 * @param userInfo 当前操作人员
	 * @param stagedInfo 项目阶段信息
	 * @param relateMod 关联的模块
	 * @return
	 */
	public List<StagedInfo> listPagedStagedInfo(UserInfo userInfo,
			StagedInfo stagedInfo, String[] relateMod) {
		return itemDao.listPagedStagedInfo(userInfo,stagedInfo,relateMod);
	}
	/**
	 * 修改项目关联信息
	 * @param userInfo
	 * @param stagedInfoPre
	 * @param newStagedId
	 * @param newStagedName
	 */
	public void updateStagedItem(UserInfo userInfo, StagedInfo stagedInfoPre,
			Integer newStagedId, String newStagedName) {
		String modType = stagedInfoPre.getModuleType();
		Integer id = stagedInfoPre.getInfoId();
		if("task".equals(modType)//任务关联
				||"file".equals(modType)//阶段附件
				||"sp_flow".equals(modType) //留言附件
				){
			StagedInfo stagedInfo = new StagedInfo();
			stagedInfo.setId(id);
			stagedInfo.setStagedItemId(newStagedId);
			itemDao.update(stagedInfo);
		}else if("itemUpFile".equals(modType)){//项目附件
			ItemUpfile itemTalkFile = new ItemUpfile();
			itemTalkFile.setId(id);
			itemTalkFile.setStagedItemId(newStagedId);
			itemDao.update(itemTalkFile);
		}else if("itemTalkFile".equals(modType)){//留言
			ItemTalkFile itemTalkFile = new ItemTalkFile();
			itemTalkFile.setId(id);
			itemTalkFile.setStagedItemId(newStagedId);
			itemDao.update(itemTalkFile);
		}
	}
		
	/**
	 * 删除项目关联信息
	 * @param userInfo
	 * @param stagedInfo
	 */
	public void deleteStagedItem(UserInfo userInfo, StagedInfo stagedInfoPre) {
		String modType = stagedInfoPre.getModuleType();
		Integer id = stagedInfoPre.getInfoId();
		Integer itemId = stagedInfoPre.getItemId();
		if("file".equals(modType)){//阶段附件
			StagedInfo stagedInfo = (StagedInfo) itemDao.objectQuery(StagedInfo.class, id);
			itemDao.delById(StagedInfo.class, id);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, stagedInfo.getModuleId());
			this.addItemLog(userInfo.getComId(),itemId,userInfo.getId(),userInfo.getUserName(),"删除了项目阶段管理附件："+upfiles.getFilename());
		}else if("itemUpFile".equals(modType)){//项目附件
			ItemUpfile itemUpfile = (ItemUpfile) itemDao.objectQuery(ItemUpfile.class, id);
			itemDao.delById(ItemUpfile.class, id);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, itemUpfile.getUpfileId());
			this.addItemLog(userInfo.getComId(),itemId,userInfo.getId(),userInfo.getUserName(),"删除了项目附件："+upfiles.getFilename());
		}else if("itemTalkFile".equals(modType)){//留言附件
			ItemTalkFile itemTalkFile = (ItemTalkFile) itemDao.objectQuery(ItemTalkFile.class, id);
			itemDao.delById(ItemTalkFile.class, id);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, itemTalkFile.getUpfileId());
			this.addItemLog(userInfo.getComId(),itemId,userInfo.getId(),userInfo.getUserName(),"删除了项目留言附件："+upfiles.getFilename());
			
		}
			
	}
	/**
	 * 统计权限下所有项目数量
	 * @param item
	 * @param curUser
	 * @param isForceIn
	 * @return
	 */
	public List<Item> countAllItem(Item item, UserInfo curUser, boolean isForceIn) {
		return itemDao.countAllItem(item,curUser,isForceIn);
	}
	/**
	 * 分页查询项目及其阶段
	 * @param item
	 * @param curUser
	 * @param isForceIn
	 * @return
	 */
	public List<ItemStagedInfo> listItemStagedInfo(Item item, UserInfo curUser, boolean isForceIn) {
		return itemDao.listItemStagedInfo(item, curUser, isForceIn);
	}
	
	/**
	 * 删除项目附件、留言附件
	 * @param itemUpFileId
	 * @param type
	 * @param userInfo
	 * @param itemId
	 * @throws Exception
	 */
	public void delItemUpfile(Integer itemUpFileId, String type, UserInfo userInfo, Integer itemId) throws Exception {
		if(type.equals("itemTalk")){
			ItemTalkFile itemTalkFile = (ItemTalkFile) itemDao.objectQuery(ItemTalkFile.class, itemUpFileId);
			itemDao.delById(ItemTalkFile.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, itemTalkFile.getUpfileId());
			this.addItemLog(userInfo.getComId(),itemId,userInfo.getId(),userInfo.getUserName(),"删除了项目留言附件："+upfiles.getFilename());
			
		}else if(type.equals("item")){
			ItemUpfile itemUpfile = (ItemUpfile) itemDao.objectQuery(ItemUpfile.class, itemUpFileId);
			itemDao.delById(ItemUpfile.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, itemUpfile.getUpfileId());
			this.addItemLog(userInfo.getComId(),itemId,userInfo.getId(),userInfo.getUserName(),"删除了项目附件："+upfiles.getFilename());
		}else if(type.equals("file")) {
			StagedInfo stagedInfo = (StagedInfo) itemDao.objectQuery(StagedInfo.class, itemUpFileId);
			itemDao.delById(StagedInfo.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, stagedInfo.getModuleId());
			this.addItemLog(userInfo.getComId(),itemId,userInfo.getId(),userInfo.getUserName(),"删除了项目阶段管理附件："+upfiles.getFilename());
		}else if(type.equals("task")){
			TaskUpfile taskUpfile = (TaskUpfile) itemDao.objectQuery(TaskUpfile.class, itemUpFileId);
			itemDao.delById(TaskUpfile.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, taskUpfile.getUpfileId());
			taskService.addTaskLog(userInfo.getComId(),taskUpfile.getTaskId(),userInfo.getId(),userInfo.getUserName(),"删除了任务附件："+upfiles.getFilename());
		}else if(type.equals("taskTalk")){
			TaskTalkUpfile taskTalkUpfile = (TaskTalkUpfile) itemDao.objectQuery(TaskTalkUpfile.class, itemUpFileId);
			itemDao.delById(TaskTalkUpfile.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, taskTalkUpfile.getUpfileId());
			taskService.addTaskLog(userInfo.getComId(),taskTalkUpfile.getTaskId(),userInfo.getId(),userInfo.getUserName(),"删除了任务留言附件："+upfiles.getFilename());
		}else if(type.equals("crm")){
			CustomerUpfile taskTalkUpfile = (CustomerUpfile) itemDao.objectQuery(CustomerUpfile.class, itemUpFileId);
			itemDao.delById(CustomerUpfile.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, taskTalkUpfile.getUpfileId());
			crmService.addCustomerLog(userInfo.getComId(),taskTalkUpfile.getCustomerId(),userInfo.getId(),"删除了客户附件："+upfiles.getFilename());
		}else if(type.equals("crmTalk")){
			FeedInfoFile taskTalkUpfile = (FeedInfoFile) itemDao.objectQuery(FeedInfoFile.class, itemUpFileId);
			itemDao.delById(FeedInfoFile.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, taskTalkUpfile.getUpfileId());
			crmService.addCustomerLog(userInfo.getComId(),taskTalkUpfile.getCustomerId(),userInfo.getId(),"删除了反馈留言附件："+upfiles.getFilename());
		}else if(type.equals("demand")){
			DemandFile taskTalkUpfile = (DemandFile) itemDao.objectQuery(DemandFile.class, itemUpFileId);
			itemDao.delById(DemandFile.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, taskTalkUpfile.getUpfileId());
			demandService.addDemandLog(userInfo,taskTalkUpfile.getDemandId(),"删除了需求说明文件："+upfiles.getFilename());
		}else if(type.equals("demandTalk")){
			DemandTalkUpfile taskTalkUpfile = (DemandTalkUpfile) itemDao.objectQuery(DemandTalkUpfile.class, itemUpFileId);
			itemDao.delById(DemandTalkUpfile.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, taskTalkUpfile.getUpfileId());
			demandService.addDemandLog(userInfo,taskTalkUpfile.getDemandId(),"删除了需求留言附件："+upfiles.getFilename());
		}else if(type.equals("product")){
			ProUpFiles taskTalkUpfile = (ProUpFiles) itemDao.objectQuery(ProUpFiles.class, itemUpFileId);
			itemDao.delById(ProUpFiles.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, taskTalkUpfile.getUpFileId());
			productService.addProLog(userInfo.getComId(),taskTalkUpfile.getProId(),userInfo.getId(),userInfo.getUserName(),"删除了产品附件："+upfiles.getFilename());
		}else if(type.equals("productTalk")){
			ProTalkUpfile taskTalkUpfile = (ProTalkUpfile) itemDao.objectQuery(ProTalkUpfile.class, itemUpFileId);
			itemDao.delById(ProTalkUpfile.class, itemUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) itemDao.objectQuery(Upfiles.class, taskTalkUpfile.getUpfileId());
			productService.addProLog(userInfo.getComId(),taskTalkUpfile.getProId(),userInfo.getId(),userInfo.getUserName(),"删除了产品留言附件："+upfiles.getFilename());
		}
	}
	
	/**
	 * 更新项目进度
	 * @author hcj 
	 * @date: 2018年10月15日 下午1:46:55
	 * @param itemProgress
	 * @param userInfo
	 */
	public void updateItemProgress(ItemProgress itemProgress, UserInfo userInfo) {
		if(!CommonUtil.isNull(itemProgress.getStartTime())){
			itemProgress = (ItemProgress) itemDao.objectQuery(ItemProgress.class, itemProgress.getId());
			//项目进度回复
			itemProgress.setUserId(userInfo.getId());
			itemProgressService.updateItemProgressToOld(itemProgress);
			Item item = new Item();
			item.setId(itemProgress.getItemId());
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(item);
			this.addItemLog(userInfo.getComId(),itemProgress.getItemId(),userInfo.getId(),userInfo.getUserName(),"项目进度还原到："+itemProgress.getProgressName());
			//项目的所有查看人
			List<UserInfo> shares = itemDao.listItemOwnersNoForce(userInfo.getComId(), itemProgress.getItemId());
			//发布待办信息
			todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_ITEM,
					itemProgress.getItemId(), "项目进度还原到："+itemProgress.getProgressName(), shares, userInfo.getId(), ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_1);
		}else{
			
			
			itemProgress = (ItemProgress) itemDao.objectQuery(ItemProgress.class, itemProgress.getId());
			//更新进度
			itemProgress.setUserId(userInfo.getId());
			itemProgress.setStartTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm));
			itemProgressService.updateItemProgress(itemProgress);
			//更新项目修改时间
			Item item = new Item();
			item.setId(itemProgress.getItemId());
			item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			itemDao.update(item);
			//添加日志
			this.addItemLog(userInfo.getComId(),itemProgress.getItemId(),userInfo.getId(),userInfo.getUserName(),"更新项目进度到："+itemProgress.getProgressName());
		}
	}
	/**
	 * 催办信息
	 * @author hcj 
	 * @date: 2018年10月15日 下午3:02:28
	 * @param userInfo
	 * @param itemId
	 * @return
	 */
	public Map<String, Object> queryRemindConf(UserInfo userInfo, Integer itemId) {
		Map<String, Object> map = new HashMap<String,Object>();
		Item item = itemDao.queryItemById(itemId, userInfo);
		map.put("busModName", item.getItemName());
		map.put("status", "y");
		String defMsg = "请尽快推进项目“"+item.getItemName()+"”的进度！";
		map.put("defMsg", defMsg);
		//事项的执行人员信息
		List<BusRemindUser> listReminderUser = new ArrayList<>();
		BusRemindUser busRemindUser = new BusRemindUser();
		busRemindUser.setUserId(item.getOwner());
		busRemindUser.setUserName(item.getOwnerName());
		listReminderUser.add(busRemindUser);
		map.put("listRemindUser", listReminderUser);
		return map;
	}
	
	/**
	 * 更新所属产品
	 * @author hcj 
	 * @date: 2018年10月16日 上午10:21:51
	 * @param item
	 * @param userInfo
	 */
	public void updateProduct(Item item, UserInfo userInfo) {
		item.setModifyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		if(!CommonUtil.isNull(item.getProductId())){
			itemDao.update(item);
			//添加日志
			item = this.getItemById(item.getId(), userInfo);
			//项目功能清单存入
			if(!CommonUtil.isNull(item.getProductId())){
				functionListService.addImportFunctionList(item.getId(), ConstantInterface.TYPE_ITEM, ConstantInterface.TYPE_PRODUCT, item.getProductId(), userInfo);
			}
			this.addItemLog(userInfo.getComId(),item.getId(),userInfo.getId(),userInfo.getUserName(),"更新所属产品为："+item.getProductName());
		}else{
			item = this.getItemById(item.getId(), userInfo);
			itemDao.update(" update item set productId = null where id=:id ", item);
			//添加日志
			this.addItemLog(userInfo.getComId(),item.getId(),userInfo.getId(),userInfo.getUserName(),"移除了产品所属："+item.getProductName());
		}
	}
	/**
	 * 分页获取维护记录
	 * @author hcj 
	 * @date: 2018年10月16日 下午3:36:02
	 * @param itemUpfile
	 * @param userInfo
	 * @return
	 */
	public List<ItemUpfile> listPagedMaintenanceRecord(ItemUpfile itemUpfile, UserInfo userInfo) {
		return itemDao.listPagedMaintenanceRecord(itemUpfile, userInfo);
	}
}
