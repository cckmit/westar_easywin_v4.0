package com.westar.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.enums.PubStateEnum;
import com.westar.base.model.Area;
import com.westar.base.model.Attention;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.Customer;
import com.westar.base.model.CustomerHandOver;
import com.westar.base.model.CustomerLog;
import com.westar.base.model.CustomerRead;
import com.westar.base.model.CustomerShareGroup;
import com.westar.base.model.CustomerSharer;
import com.westar.base.model.CustomerStage;
import com.westar.base.model.CustomerType;
import com.westar.base.model.CustomerUpfile;
import com.westar.base.model.FeedBackInfo;
import com.westar.base.model.FeedBackType;
import com.westar.base.model.FeedInfoFile;
import com.westar.base.model.Item;
import com.westar.base.model.ItemTalkFile;
import com.westar.base.model.ItemUpfile;
import com.westar.base.model.LinkMan;
import com.westar.base.model.MsgShare;
import com.westar.base.model.OlmAndCus;
import com.westar.base.model.OlmContactWay;
import com.westar.base.model.OutLinkMan;
import com.westar.base.model.RecycleBin;
import com.westar.base.model.Region;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.StagedInfo;
import com.westar.base.model.Task;
import com.westar.base.model.TaskTalkUpfile;
import com.westar.base.model.TaskUpfile;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Area4App;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.pojo.ModStaticVo;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.StatisticCrmVo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.JpushUtils;
import com.westar.base.util.PinyinToolkit;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.CrmDao;
import com.westar.core.thread.IndexUpdateThread;
import com.westar.core.web.PaginationContext;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 
 * 描述: 客户模块的业务逻辑层
 * @author alan
 * @date 2018年8月30日 下午1:06:41
 */
@Service
public class CrmService {

	@Autowired
	CrmDao crmDao;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	IndexService indexService; 
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	JiFenService jifenService;
	
	@Autowired
	SelfGroupService selfGroupService;
	
	@Autowired
	AttentionService attentionService;
	
	@Autowired
	ClockService clockService;
	
	@Autowired
	ChatService chatService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	BusUpdateService busUpdateService;

	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	OutLinkManService outLinkManService;
	
	private static final Log log = LogFactory.getLog(CrmService.class);

	/**
	 * 区域添加
	 * @param area
	 */
	public void addArea(Area area,UserInfo userInfo){
		//设置排序号
		area.setAreaOrder(crmDao.initAreaOrder(userInfo.getComId(), area.getParentId()));
		crmDao.add(area);
		//添加系统日志记录 
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "添加区域:\""+area.getAreaName()+"\"",
				ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
	}
	/**
	 * 初始化区域排序值
	 * @param comId
	 * @param parentId
	 * @return
	 */
	public Integer queryAreaOrderMax(Integer comId,Integer parentId){
		return crmDao.initAreaOrder(comId,parentId);
	}
	/**
	 * 获取客户区域JSON字符串
	 * @param comId
	 * @return
	 */
	public String areaJsonStr(Integer comId){
		List<Area> listArea = crmDao.listArea(comId);
		StringBuffer strJson=new StringBuffer("[");
		if(null!=listArea && !listArea.isEmpty()){
			for(Area vo:listArea){
				strJson.append("{\"id\":\""+vo.getId()+"\",\"regionId\":\""+vo.getRegionId()+"\",\"pId\":\""+vo.getParentId()+"\",\"name\":\""+vo.getAreaName()+"\"");
				strJson.append(vo.getIsLeaf()==0?",\"open\":true,\"nocheck\":true,\"ztype\":\"1\"":",\"ztype\":\"0\"");
				strJson.append("},");
			}
			strJson=new StringBuffer(strJson.substring(0,strJson.lastIndexOf(",")));
		}
		strJson.append("]");
		return strJson.toString();
	}
	/**
	 * 更新区域树形数据结构
	 * @param nodeId
	 * @param pId
	 * @return
	 */
	public boolean zTreeOnDrop(Integer nodeId,Integer pId,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			crmDao.zTreeOnDrop(nodeId,pId,userInfo.getComId());
		} catch (Exception e) {
			succ = false;
			throw e;
		}
		return succ;
	}
	/**
	 * 删除区域节点数据
	 * @param comId 企业唯一标识符
	 * @param delChildren 是否删除子集标识符
	 * @param curArea 当前节点对象
	 * @return
	 */
	public boolean zTreeRemove(Integer comId,String delChildren,Area curArea,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//区域节点删除
			//是否删除子集
			if("yes".equals(delChildren)){
				//删除区域
				crmDao.delArea(curArea.getId(),comId);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除区域节点:\""+curArea.getAreaName()+"\"以及区域下数据",
						ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
			}else{
				//获取需要更新对象
				Area orgObj = (Area) crmDao.objectQuery(Area.class,curArea.getId());
				//获取需要更新对象
				Area pObj = (Area) crmDao.objectQuery(Area.class,curArea.getParentId());
				//区域节点所属节点向上调一级
				crmDao.areaParentUpdate(comId,curArea.getParentId(),curArea.getId());
				//区域节点删除
				crmDao.delByField("area", new String[]{"comId","id"},new Object[]{comId,curArea.getId()});
				
				if(null!=pObj){
					//添加系统日志记录 
					systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "\""+orgObj.getAreaName()+"\"子节点的父节点更新为\""+pObj.getAreaName()+"\"",ConstantInterface.TYPE_CRM,
							userInfo.getComId(),userInfo.getOptIP());
				}else{
					//添加系统日志记录 
					systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "\""+orgObj.getAreaName()+"\"子节点的父节点更新为\"根节点\"",
							ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
				}
			}
		} catch (Exception e) {
			succ = false;
		}
		return succ;
	}
//	/**
//	 * 根据主键查询区域节点信息
//	 * @param comId
//	 * @param id
//	 * @return
//	 */
//	public Area areaQuerys(Integer comId,Integer id){
//		return crmDao.areaQuery(comId, id);
//	}
	/**
	 * 根据主键查询区域节点信息
	 * @param id
	 * @return
	 */
	public Area getAreaById(Integer id){
		return (Area) crmDao.objectQuery(Area.class, id);
	}
	/**
	 * 获取区域子集集合
	 * @param comId
	 * @param pId
	 * @return
	 */
	public List<Area> areaChildren(Integer comId,Integer pId){
		return crmDao.areaChildren(comId, pId);
	}
	/**
	 * 添加客户类型
	 * @param customerType
	 * @return
	 */
	public Integer addCustomerType(CustomerType customerType,UserInfo userInfo) throws Exception{
		Integer id =  crmDao.add(customerType);
		//添加系统日志记录 
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "添加客户类型:\""+customerType.getTypeName()+"\"",
				ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
		return id;
	}
	/**
	 * 获取客户类型集合
	 * @param comId
	 * @return
	 */
	public List<CustomerType> listCustomerType(Integer comId){
		return crmDao.listCustomerType(comId);
	}
	/**
	 * 初始化客户类型排序值
	 * @param comId
	 * @return
	 */
	public Integer queryCustomerTypeOrderMax(Integer comId){
		CustomerType customerType = crmDao.initCustomerTypeOrder(comId);
		return null==customerType.getTypeOrder()?1:customerType.getTypeOrder();
	}
	/**
	 * 更新客户类型名称
	 * @param customerType
	 * @return
	 */
	public boolean updateTypeName(CustomerType customerType,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			CustomerType crmType =(CustomerType) crmDao.objectQuery(CustomerType.class, customerType.getId());
			//有变动
			if(null!=crmType && !crmType.getTypeName().equals(customerType.getTypeName())){
				//更新客户类型名称
				crmDao.update("update customerType a set a.typeName=:typeName where a.comid=:comId and a.id=:id", customerType);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "更新客户类型名称\""+customerType.getTypeName()+"\"",
						ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 更新客户类型排序
	 * @param customerType
	 * @return
	 */
	public boolean updateTypeOrder(CustomerType customerType,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//获取需要删除的客户类型
			CustomerType orgObj = crmDao.queryCustomerType(customerType.getComId(), customerType.getId());
			//有变动
			if(null!=orgObj && !orgObj.getTypeOrder().equals(customerType.getTypeOrder())){
				//更新客户类型排序
				crmDao.update("update customerType a set a.typeOrder=:typeOrder where a.comid=:comId and a.id=:id", customerType);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "更新客户类型\""+orgObj.getTypeName()+"\"的排序为\""+customerType.getTypeOrder()+"\"",
						ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 更新客户更新周期
	 * @param customerType
	 * @return
	 */
	public boolean updateModifyPeriod(CustomerType customerType,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//获取需要删除的客户类型
			CustomerType orgObj = crmDao.queryCustomerType(customerType.getComId(), customerType.getId());
			//有变动
			if(null!=orgObj && !orgObj.getTypeOrder().equals(customerType.getTypeOrder())){
				//更新客户类型排序
				crmDao.update("update customerType a set a.modifyPeriod=:modifyPeriod where a.comid=:comId and a.id=:id", customerType);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "更新客户类型\""+orgObj.getTypeName()+"\"的周期设定为\""+customerType.getModifyPeriod()+"\"",
						ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 根据主键获取客户类型对象
	 * @param comId
	 * @param typeId
	 * @return
	 */
	public CustomerType queryCustomerType(Integer comId,Integer typeId){
		return crmDao.queryCustomerType(comId,typeId);
	}
	
	/**
	 * 根据主键获取客户阶段对象
	 * @param stageId
	 * @return
	 */
	public CustomerStage queryCustomerStage(Integer stageId){
		return (CustomerStage) crmDao.objectQuery(CustomerStage.class, stageId);
	}
	/**
	 * 客户类型删除
	 * @param customerType
	 * @return
	 */
	public boolean delCustomerType(CustomerType customerType,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//获取需要删除的客户类型
			CustomerType orgObj = crmDao.queryCustomerType(customerType.getComId(), customerType.getId());
			if(null!=orgObj){
				//客户类型删除
				crmDao.update("delete from customerType a where a.comid=:comId and a.id=:id", customerType);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除客户类型:\""+orgObj.getTypeName()+"\"",
						ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
		}
		return succ;
	}
	/**
	 * 客户添加
	 * @param customer 客户信息
	 * @param msgShare 分享
	 * @param userInfo 操作员
	 * @return
	 * @throws Exception
	 */
	public Integer addCustomer(Customer customer,UserInfo userInfo) throws Exception{

		//客户基本信息存入
		Integer customerId = crmDao.add(customer);
		customer.setId(customerId);
		//模块日志添加
		this.addCustomerLog(userInfo.getComId(),customerId,userInfo.getId(),"新增客户:\""+customer.getCustomerName()+"\"");
		
		if(!userInfo.getId().equals(customer.getOwner())){
			//添加工作轨迹
			systemLogService.addSystemLogWithTrace(userInfo, customer.getOwner(), ConstantInterface.TYPE_CRM, 
					customerId, "新增客户\""+customer.getCustomerName()+"\"",
					"负责\""+userInfo.getUserName()+"\"新增的客户\""+customer.getCustomerName()+"\"");
		}else{
			//添加工作轨迹
			systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_CRM, 
					customerId, "新增客户\""+customer.getCustomerName()+"\"",
					"新增客户\""+customer.getCustomerName()+"\"");
		}
		
		//添加客户分人员
		this.addCustomerShare(customer, userInfo, customerId);
		
		this.addCrmFiles(customer, userInfo);
		//添加客户的外部联系人信息
		this.addCrmOutLinkManRelate(customer, customerId);
		
		//添加客户移交记录表信息
		CustomerHandOver customerHandOver = new CustomerHandOver();
		customerHandOver.setComId(userInfo.getComId());
		customerHandOver.setCustomerId(customerId);
		customerHandOver.setFromUser(userInfo.getId());
		customerHandOver.setToUser(customer.getOwner());
		crmDao.add(customerHandOver);
		
		//添加查看记录
		this.addCrmRead(customerId, userInfo);
		//客户的所有查看人
		List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customerId);
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,customer.getOwner(),customerId,"新增客户:"+customer.getCustomerName(), 
				ConstantInterface.TYPE_CRM, shares,null);
		
		//不给自己发消息
		if(!customer.getOwner().equals(userInfo.getComId())){
			//取得待办事项主键
			TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(), customer.getOwner(),
					customerId,ConstantInterface.TYPE_CRM,0);
			if(null!=todayWorks){
				JpushUtils.sendTodoMessage(userInfo.getComId(), customer.getOwner(), userInfo.getId(),
						todayWorks.getId(), customerId, ConstantInterface.TYPE_CRM,0,"新增客户:"+customer.getCustomerName());
			}
		}
		//添加更新信息
		busUpdateService.addBusUpdate(userInfo, customerId,ConstantInterface.TYPE_CRM,
				ConstantInterface.BUSUPDATETYPE_ADD, "新增客户:"+customer.getCustomerName());
		//添加积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_CRM,
				"新增客户:"+customer.getCustomerName(),customerId);
		
		//标注关注
		if(null!=customer.getAttentionState() && customer.getAttentionState().equals(ConstantInterface.ATTENTION_STATE_YES)){
			attentionService.addAtten(ConstantInterface.TYPE_CRM, customerId, userInfo);
		}
		//客户索引添加
		this.updateCustomerIndex(customerId, userInfo,"add");
		return customerId;
	}
	/**
	 * 客户联系人关联存入
	 * @param customer
	 * @param customerId
	 */
	private void addCrmOutLinkManRelate(Customer customer, Integer customerId) {
		//客户联系人关联存入
		List<LinkMan> listLinkMan = customer.getListLinkMan();
		if(null!=listLinkMan){
			Set<Integer> olmIds = new HashSet<Integer>();
			//去重
			for(LinkMan vo : listLinkMan){
				olmIds.add(vo.getId());
			}
			//关联存入
			for (Integer olmId : olmIds) {
				if(null != olmId){
					OlmAndCus oac = new OlmAndCus();
					oac.setComId(customer.getComId());
					oac.setCustomerId(customerId);
					oac.setOutLinkManId(olmId);
					crmDao.add(oac);
				}
			}
		}
	}
	/**
	 * 添加客户分人员
	 * @param customer
	 * @param userInfo
	 * @param customerId
	 */
	private void addCustomerShare(Customer customer, UserInfo userInfo,
			Integer customerId) {
		//当分享范围为私有时添加参与人
		if(customer.getPubState() != null && customer.getPubState().equals(0)){
			//客户参与人存入
			List<CustomerSharer> listCustomerSharer = customer.getListCustomerSharer();
			//客户参与人去重
			Set<Integer> shareIdSets = new HashSet<Integer>();
			//把当前操作人添加为客户参与人
			shareIdSets.add(userInfo.getId());
			//把客户负责人添加为客户参与人
			shareIdSets.add(customer.getOwner());
			
			//参与人集合数据加入set
			if(null!=listCustomerSharer){
				for(CustomerSharer customerSharer : listCustomerSharer){
					shareIdSets.add(customerSharer.getUserId());
				}
			}
			//客户参与人存入
			if(null!=shareIdSets && shareIdSets.size()>0){
				CustomerSharer customerSharer =null;
				for (Integer shareId : shareIdSets) {
					customerSharer = new CustomerSharer();
					customerSharer.setComId(userInfo.getComId());
					customerSharer.setCustomerId(customerId);
					customerSharer.setUserId(shareId);
					crmDao.add(customerSharer);
				}
			}
		}
	}
	/**
	 * 
	 * @param customer
	 * @param userInfo
	 * @throws Exception
	 */
	private void addCrmFiles(Customer customer, UserInfo userInfo) throws Exception {
		Integer customerId = customer.getId();
		//客户的附件
		List<CustomerUpfile> listUpfiles = customer.getListUpfiles();
		if(null!=listUpfiles){
			//缓存附件主键信息
			List<Integer> listFileId = new ArrayList<Integer>();
			
			for (CustomerUpfile customerUpfile : listUpfiles) {
				customerUpfile.setCustomerId(customerId);
				customerUpfile.setComId(customer.getComId());
				customerUpfile.setUserId(customer.getCreator());
				crmDao.add(customerUpfile);
				//为附件创建索引
				uploadService.updateUpfileIndex(customerUpfile.getUpfileId(), userInfo, "add",customerId,ConstantInterface.TYPE_CRM);
				
				listFileId.add(customerUpfile.getUpfileId());
			}
			//归档到文档中心
			fileCenterService.addModFile(userInfo,listFileId,customer.getCustomerName());
		}
	}

	/**
	 * 获取个人所能见客户集合(PC)
	 * @param customer
	 * @return
	 */
	public List<Customer> listCustomerForPage(Customer customer,UserInfo sessionUser){
		//验证当前登录人是否是督察人员
		boolean isForceInPersion = forceInService.isForceInPersion(sessionUser, ConstantInterface.TYPE_CRM);
		return crmDao.listCustomerForPage(customer,sessionUser.getId(),isForceInPersion);
	}
	/**
	 * 分页查询客户信息
	 * @param customer
	 * @param sessionUser
	 * @return
	 */
	public PageBean<Customer> listPagedCrm(Customer customer,UserInfo sessionUser){
		boolean isForceInPersion = forceInService.isForceInPersion(sessionUser, ConstantInterface.TYPE_CRM);
		List<Customer> list = crmDao.listCustomerForPage(customer,sessionUser.getId(),isForceInPersion);;
		
		PageBean<Customer> pageBean = new PageBean<Customer>();
		pageBean.setRecordList(list);
		
		// 除开页面已有数据的总数
		Integer total = PaginationContext.getTotalCount();
		pageBean.setTotalCount(total);
				
		return pageBean;
	}
	/**
	 * 获取个人所能见客户集合(手机)
	 * @param customer
	 * @return
	 */
	public List<Customer> listCrmForRelevance(Customer customer,UserInfo userInfo){
		boolean isForceInPersion = true;
		return crmDao.listCrmForRelevance(customer,userInfo,isForceInPersion);
	}
	/**
	 * 获取个人权限下的所有客户（不分页）
	 * @param customer
	 * @param operator
	 * @param isForceInPersion
	 * @return
	 */
	public List<Customer> listCustomerOfAll(Customer customer,Integer operator,boolean isForceInPersion){
		return crmDao.listCustomerOfAll(customer,operator,isForceInPersion);
	}
	/**
	 * 获取个人权限下的所有客户月增长集合
	 * @param customer
	 * @param operator
	 * @param isForceInPersion
	 * @return
	 */
	public List<Customer> listCustomerAddByMonthOfAll(Customer customer,Integer operator,boolean isForceInPersion){
		return crmDao.listCustomerAddByMonthOfAll(customer,operator,isForceInPersion);
	}
	/***
	 * 分页获取个人权限下的所有客户月增长集合
	 * @param customer
	 * @return
	 */
	public List<Customer> listCustomerAddByMonthForPage(Customer customer,UserInfo sessionUser){
		//验证当前登录人是否是督察人员
		boolean isForceInPersion = forceInService.isForceInPersion(sessionUser, ConstantInterface.TYPE_CRM);
		return crmDao.listCustomerAddByMonthForPage(customer,sessionUser.getId(),isForceInPersion);
	}
	/***
	 * 查询所有自己移交的客户
	 * @param customer
	 * @param operator
	 * @return
	 */
	public List<Customer> listCustomerHandsOfAll(Customer customer,Integer operator){
		return crmDao.listCustomerHandsOfAll(customer,operator);
	}
	/**
	 * 分页查询所有自己移交的客户
	 * @param customer
	 * @param operator
	 * @return
	 */
	public List<Customer> listCustomerHandsForPage(Customer customer,Integer operator){
		return crmDao.listCustomerHandsForPage(customer,operator);
	}
	/**
	 * 批量删除客户
	 * @param ids
	 * @param userInfo
	 * @throws Exception 
	 */
	public void delCustomer(List<Integer> ids,UserInfo userInfo) throws Exception{
		Customer customer =null;
		for(Integer id :ids){
			customer = crmDao.queryCustomer(userInfo,id);
			//客户信息不存在
			if(null==customer){
				continue;
			}
			//索引库删除
			this.updateCustomerIndex(id, userInfo,"del");
			//添加工作轨迹
			systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_CRM, 
					id, "删除客户:\""+customer.getCustomerName()+"\"", "删除客户:\""+customer.getCustomerName()+"\"");
			
			//删除客户联系人
			crmDao.delByField("linkMan", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			crmDao.delByField("olmAndCus", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			//删除客户分享人
			crmDao.delByField("customerSharer", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			//删除客户附件
			crmDao.delByField("customerUpfile", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			//删除客户客户日志
			crmDao.delByField("customerLog", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			//删除客户移交记录
			crmDao.delByField("customerHandOver", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			//删除客户反馈记录附件
			crmDao.delByField("feedInfoFile", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			//删除客户反馈记录
			crmDao.delByField("feedBackInfo", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			//删除客户查看记录
			crmDao.delByField("customerRead", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			//删除项目组成员关联
			crmDao.delByField("customerShareGroup", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			
			crmDao.delByField("customernewtestinfo", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			//删除浏览记录
			crmDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),id,ConstantInterface.TYPE_CRM});
			//关注信息
			crmDao.delByField("attention", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),id,ConstantInterface.TYPE_CRM});
			//最新动态
			crmDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),id,ConstantInterface.TYPE_CRM});
			//删除聊天记录
			chatService.delBusChat(userInfo.getComId(),id,ConstantInterface.TYPE_CRM);
			//将关联到该客户的项目信息设置为0
			crmDao.updateItemCrmId(userInfo.getComId(),id);
			//将任务关联到该客户的全部设置为0
			crmDao.updateTaskBusId(id, userInfo.getComId(), ConstantInterface.TYPE_CRM);
			
			//修改积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_CRMDEL,
					"删除客户:"+customer.getCustomerName(),id);
			//删除客户基本信息
			crmDao.delById(Customer.class, id);
		}
	}
	/**
	 * 批量预删除客户
	 * @param ids
	 * @param userInfo
	 * @throws IOException 
	 * @throws CorruptIndexException 
	 * @throws ParseException 
	 */
	public void delPreCustomer(Integer[] ids,UserInfo userInfo) throws CorruptIndexException, IOException, ParseException{
		for(Integer id :ids){
			Customer customer =new Customer();
			//客户主键
			customer.setId(id);
			//预删除标识
			customer.setDelState(1);
			//修改客户信息
			crmDao.update(customer);
			
			//删除客户与外部联系人关系
			crmDao.delByField("olmAndCus", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			
			//删除客户与联系人关系
			crmDao.delByField("linkMan", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),id});
			//删除数据更新记录
			crmDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_CRM,id});
		
			//删除回收箱数据
			crmDao.delByField("recycleBin", new String[]{"comId","busType","busId","userId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_CRM,id,userInfo.getId()});
			
			//取得所有设置了提醒的闹铃(删除所有的)
			clockService.delClockByType(userInfo.getComId(),id,ConstantInterface.TYPE_CRM);
			
			//回收箱
			RecycleBin recyleBin =  new RecycleBin();
			//业务主键
			recyleBin.setBusId(id);
			//业务类型
			recyleBin.setBusType(ConstantInterface.TYPE_CRM);
			//企业号
			recyleBin.setComId(userInfo.getComId());
			//创建人
			recyleBin.setUserId(userInfo.getId());
			crmDao.add(recyleBin);
			
		}
	}
	/**
	 * 获取客户详细信息(需要联系人)
	 * @param customerId
	 * @return
	 */
	public Customer queryCustomer(UserInfo userInfo,Integer customerId){
		Customer customer = crmDao.queryCustomer(userInfo, customerId);
		if(null==customer){
			return null;
		}
		
		//查询显示数量
		Integer fileNum = crmDao.countFile(userInfo.getComId(), customerId);
		Task task = new Task();
		task.setBusType(ConstantInterface.TYPE_CRM);
		task.setBusId(customerId);
		Integer taskNum = taskService.countTasks(task,userInfo);
		Item item = new Item();
		item.setPartnerId(customerId);
		Integer itemNum = itemService.countItem(item, userInfo);
		customer.setFileNum(fileNum);
		customer.setTaskNum(taskNum);
		customer.setItemNum(itemNum);
		//获取用户的客户查看记录信息
		CustomerRead customerRead = crmDao.queryCustomerRead(userInfo.getComId(),customerId,userInfo.getId());
		if(null==customerRead){
			//添加当前操作人员为查看人员
			this.addCrmRead(customerId, userInfo);
		}
		
		//获取客户联系人集合
		List<LinkMan> listLinkMan = crmDao.listLinkMan(userInfo.getComId(), customerId);
		if(!CommonUtil.isNull(listLinkMan)) {
			for (LinkMan lm : listLinkMan) {
				//查询联系方式
				List<OlmContactWay> contactWays = outLinkManService.listContactWayForShow(lm.getOutLinkManId(),userInfo);
				lm.setContactWays(contactWays);
			}
		}
		customer.setListLinkMan(listLinkMan);
		//初始化联系人个数
		customer.setLinkManSum(listLinkMan.size());
		
		//获取客户分享人集合
		List<CustomerSharer> listCustomerSharer = crmDao.listCustomerSharer(userInfo.getComId(), customerId);
		customer.setListCustomerSharer(listCustomerSharer);
		
		//获取客户附件集合
		List<CustomerUpfile> listUpfiles = crmDao.listCustomerUpfile(userInfo.getComId(), customerId);
		customer.setListUpfiles(listUpfiles);
		
		//生成客户参与人JSon字符串
		StringBuffer sharerJson = null;
		if(null!=listCustomerSharer && !listCustomerSharer.isEmpty()){
			sharerJson = new StringBuffer("[");
			
			for(CustomerSharer vo:listCustomerSharer){
				sharerJson.append("{'userID':'"+vo.getUserId()+"','userName':'"+vo.getSharerName()+"','gender':'"+vo.getGender()+"','uuid':'"+vo.getUuid()+"','fileName':'"+vo.getFileName()+"'},");	
			}
			
			sharerJson = new StringBuffer(sharerJson.substring(0,sharerJson.lastIndexOf(",")));
			sharerJson.append("]");
			
			customer.setSharerJson(sharerJson.toString());
		}
		//删除客户待办提醒信息
		todayWorksService.delTodayWorksByOwner(userInfo.getComId(),userInfo.getId(),customerId,ConstantInterface.TYPE_CRM);
		return customer;
	}
	/**
	 * 客户查看权限验证
	 * @param comId
	 * @param customerId
	 * @param userId
	 * @return
	 */
	public boolean authorCheck(Integer comId,Integer customerId,Integer userId){
		List<Customer> listCustomer = crmDao.authorCheck(comId,customerId,userId);
		if(null!=listCustomer && !listCustomer.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 客户查看权限验证
	 * @param userInfo
	 * @param customerId
	 * @return
	 */
	public boolean authorCheck(UserInfo userInfo,Integer customerId){
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_CRM);
		if(isForceIn){
			return true;
		}else{
			List<Customer> listCustomer = crmDao.authorCheck(userInfo.getComId(),customerId,userInfo.getId());
			if(null!=listCustomer && !listCustomer.isEmpty()){
				return true;
			}else{
				// 查看验证，删除客户提醒
				todayWorksService.delTodoWork(customerId, userInfo.getComId(),
						userInfo.getId(), ConstantInterface.TYPE_CRM, null);
				return false;
			}
		}
				
	}
	/**
	 * 添加项目模块操作日志
	 * @param comId 企业主键
	 * @param customerId 客户主键
	 * @param userId 操作者ID
	 * @param content 动作描述
	 */
	public void addCustomerLog(Integer comId,Integer customerId,Integer userId,String content){
		CustomerLog customerLog = new CustomerLog();
		customerLog.setComId(comId);
		customerLog.setCustomerId(customerId);
		customerLog.setContent(content);
		customerLog.setUserId(userId);
		crmDao.add(customerLog);
	}
	/**
	 * 客户名称更新
	 * @param customer
	 * @param userInfo
	 * @return
	 */
	public boolean updateCustomerName(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//客户名称更新
			crmDao.update("update customer a set a.customerName=:customerName where a.comid=:comId and a.id=:id", customer);
			
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, customer.getId(), "客户名称变更为\""+customer.getCustomerName()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置查看记录
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"客户名称变更为\""+customer.getCustomerName()+"\"");
			//更新客户索引信息
			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 更新客户所属区域
	 * @param customer
	 * @param userInfo
	 * @return
	 */
	public boolean updateCustomerAreaId(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			Area area = this.getAreaById(customer.getAreaId());
			customer.setAreaName(area.getAreaName());
			
			//更新客户所属区域
			crmDao.update("update customer a set a.areaId=:areaId where a.comid=:comId and a.id=:id", customer);
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, customer.getId(), 
			//		"变更客户所属区域为:\""+area.getAreaName()+"\"", BusinessTypeConstant.type_crm, shares,null);
		
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"变更客户所属区域为:\""+area.getAreaName()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 更新客户类型
	 * @param customer
	 * @param userInfo
	 * @return
	 */
	public boolean updateCustomerTypeId(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, customer.getId(),"变更客户类型为:\""+customer.getTypeName()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			CustomerType orgObj = this.queryCustomerType(userInfo.getComId(), customer.getCustomerTypeId());
			//更新客户所属区域
			crmDao.update("update customer a set a.customerTypeId=:customerTypeId where a.comid=:comId and a.id=:id", customer);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"变更客户类型为:\""+orgObj.getTypeName()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
			// 添加更新客户类型记录
			busUpdateService.addBusUpdate(userInfo, customer.getId(),ConstantInterface.TYPE_CRM,
					ConstantInterface.BUSUPDATETYPE_UPDATE, "变更客户类型为:\""+orgObj.getTypeName()+"\"");
			
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 更新客户阶段
	 * @param customer
	 * @param userInfo
	 * @return
	 */
	public boolean updateCustomerStage(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, customer.getId(),"变更客户类型为:\""+customer.getTypeName()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			CustomerStage orgObj = (CustomerStage) crmDao.objectQuery(CustomerStage.class, customer.getStage());
			//更新客户阶段
			crmDao.update(customer);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"变更客户阶段为:\""+orgObj.getStageName()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
			// 添加更新客户类型记录
			busUpdateService.addBusUpdate(userInfo, customer.getId(),ConstantInterface.TYPE_CRM,
					ConstantInterface.BUSUPDATETYPE_UPDATE, "变更客户阶段为:\""+orgObj.getStageName()+"\"");
			
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 更新预算资金
	 * @param customer
	 * @param userInfo
	 * @return
	 */
	public boolean updateCustomerBudget(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, customer.getId(),"变更客户类型为:\""+customer.getTypeName()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//更新客户阶段
			crmDao.update(customer);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"变更资金预算为:\""+customer.getBudget()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
			// 添加更新客户类型记录
			busUpdateService.addBusUpdate(userInfo, customer.getId(),ConstantInterface.TYPE_CRM,
					ConstantInterface.BUSUPDATETYPE_UPDATE, "变更资金预算为:\""+customer.getBudget()+"\"");
			
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户负责人更新
	 * @param customer
	 * @param userInfo
	 * @return
	 */
	public boolean updateCustomerOwner(Customer customer,UserInfo userInfo)throws Exception{
		boolean succ = true;
		try {
			//客户信息
			Customer obj = (Customer) crmDao.objectQuery(Customer.class, customer.getId());
			
			//客户负责人更新
			crmDao.update("update customer a set a.owner=:owner where a.comid=:comId and a.id=:id", customer);
			//判断移除的人员是否仍有查看权限
			Integer shareNum = crmDao.countUserNum(customer.getId(),userInfo.getComId(),obj.getOwner(),obj.getOwner());
			
			//修改后的没有查看权限
			if(shareNum==0){
				//删除设置了提醒的闹铃
				clockService.delClockByUserId(userInfo.getComId(),userInfo.getId(),customer.getId(),ConstantInterface.TYPE_CRM);
			}
			
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//获取接收客户的负责人
			List<UserInfo> shares = userInfoService.listOwnerForMsg(userInfo.getComId(), customer.getId(),null,ConstantInterface.TYPE_CRM,null);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,customer.getOwner(), customer.getId(),"客户负责人变更为\""+customer.getOwnerName()+"\"", ConstantInterface.TYPE_CRM, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"变更客户负责人为:\""+customer.getOwnerName()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 更新客户参与人
	 * @param comId 企业编号
	 * @param customerId 客户信息主键
	 * @param userIds 参与人
	 * @param userInfo 操作员信息
	 * @return
	 */
	public boolean updateCustomerSharer(Integer comId,Integer customerId,Integer[] userIds,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			Customer customer = (Customer) crmDao.objectQuery(Customer.class, customerId);
			//将要移除的客户信息分享人
			List<CustomerSharer> customerSharers = crmDao.listRemoveCustomerSharer(userInfo.getComId(), customerId,userIds);
			//附件和分享人不为空
			if(null != customerSharers && customerSharers.size()>0 ){
				for (CustomerSharer customerSharer : customerSharers) {
					//删除设置了提醒的闹铃
					clockService.delClockByUserId(userInfo.getComId(),customerSharer.getUserId(),customer.getId(),ConstantInterface.TYPE_CRM);
				}
			}
			
			//先删除客户参与人
			crmDao.delByField("customerSharer", new String[]{"comId","customerId"},new Integer[]{comId,customerId});
			//重置客户的参与人
			this.updateCrmShare(comId, customerId, userIds, userInfo, customer);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 重置客户的参与人
	 * @param comId
	 * @param customerId
	 * @param userIds
	 * @param userInfo
	 * @param customer
	 */
	private void updateCrmShare(Integer comId, Integer customerId,
			Integer[] userIds, UserInfo userInfo, Customer customer) {
		if(null!=userIds && userIds.length>0){
			
			StringBuffer sharerName = new StringBuffer();
			for(Integer userId:userIds){
				UserInfo sharerInfo = userInfoService.getUserInfo(userInfo.getComId(),userId);
				sharerName.append(sharerInfo.getUserName()+",");
			}
			sharerName = new StringBuffer(sharerName.subSequence(0,sharerName.lastIndexOf(",")));
			//客户参与人更新
			CustomerSharer customerSharer =null;
			for(Integer userId:userIds){
				customerSharer = new CustomerSharer();
				customerSharer.setComId(comId);
				customerSharer.setUserId(userId);
				customerSharer.setCustomerId(customerId);
				crmDao.add(customerSharer);
			}
			//判断是否添加了创建人和负责人
			int countC = 0;
			int countO = 0;
			for (int i = 0; i < userIds.length; i++) {
				if(userIds[i].equals(customer.getCreator())) {
					countC++;
				}else if(userIds[i].equals(customer.getOwner())) {
					countO++;
				}
			}
			if(countC < 1 ) {
				customerSharer = new CustomerSharer();
				customerSharer.setComId(comId);
				customerSharer.setUserId(customer.getCreator());
				customerSharer.setCustomerId(customerId);
				crmDao.add(customerSharer);
			}
			if(countO < 1 && !customer.getCreator().equals(customer.getOwner())) {
				customerSharer = new CustomerSharer();
				customerSharer.setComId(comId);
				customerSharer.setUserId(customer.getOwner());
				customerSharer.setCustomerId(customerId);
				crmDao.add(customerSharer);
			}
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customerId,userInfo.getId(),"变更客户参与人为:\""+sharerName.toString()+"\"");

			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, customer.getId(),"变更客户参与人为:\""+sharerName.toString()+"\"", BusinessTypeConstant.type_crm, shares,null);
		}else{
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, customer.getId(),"删除所有参与人", BusinessTypeConstant.type_crm, shares,null);
		}
	}
	/**
	 * 删除单个客户参与人
	 * @param comId 企业编号
	 * @param customerId 客户主键
	 * @param userId 要移除的分享人
	 * @param userInfo 操作员信息
	 * @param shareName 参与人的姓名 
	 * @return
	 */
	public boolean delCustomerSharer(Integer comId,Integer customerId,
			Integer userId,UserInfo userInfo, String shareName) throws Exception{
		boolean succ = true;
		try {
			//客户信息
			Customer customer = (Customer) crmDao.objectQuery(Customer.class, customerId);
			//删除客户参与人
			crmDao.delByField("customerSharer", new String[]{"comId","customerId","userId"},new Integer[]{comId,customerId,userId});
			
			//判断移除的人员是否仍有查看权限
			Integer shareNum = crmDao.countUserNum(customerId,userInfo.getComId(),userId,customer.getOwner());
			//没有查看权限了
			if(shareNum==0){
				//删除设置了提醒的闹铃
				clockService.delClockByUserId(userInfo.getComId(),userId,customer.getId(),ConstantInterface.TYPE_CRM);
			}
			
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, customer.getId(),"删除客户参与人:\""+shareName+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customerId,userInfo.getId(),"删除客户参与人:\""+shareName+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 删除客户分享组
	 * @param customerId 客户主键
	 * @param groupId 分享组主键
	 * @param userInfo 当前操作人信息
	 * @return
	 * @throws Exception
	 */
	public boolean delCrmShareGroup(Integer customerId,Integer groupId,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			SelfGroup selfgroup = (SelfGroup) crmDao.objectQuery(SelfGroup.class,groupId);
			//删除客户分享组
			crmDao.delByField("customerShareGroup", new String[]{"comId","customerId","grpId"},new Object[]{userInfo.getComId(),customerId,groupId});
			
			//取得本次移除分组的成员
			List<UserInfo> removeUserList = crmDao.listRemoveGrpUser(customerId,userInfo.getComId(),groupId);
			//有移除人员，附件不为空
			if(null!=removeUserList &&removeUserList.size()>0 ){
				for (UserInfo user : removeUserList) {
					//删除设置了提醒的闹铃
					clockService.delClockByUserId(userInfo.getComId(),user.getId(),customerId,ConstantInterface.TYPE_CRM);
					
				}
				
			}
			
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customerId);
			//添加提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, customerId,"删除分享组\""+selfgroup.getGrpName()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customerId,userInfo.getId(),"删除分享组\""+selfgroup.getGrpName()+"\"");
			//客户索引库更新
//			this.updateCustomerIndex(customerId, userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户联系人批量修改
	 * @param customer
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomerLinkMan(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, customer.getId(),"客户联系人维护", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//循环判断联系人是不是新加的，如果是则添加关系
			List<LinkMan> listLinkMan = customer.getListLinkMan();
			if(listLinkMan != null && listLinkMan.size() > 0){
				for (int i = 0; i < listLinkMan.size(); i++) {
					if(listLinkMan.get(i).getOutLinkManId() != null){
						OlmAndCus oac = new OlmAndCus();
						oac.setComId(userInfo.getComId());
						oac.setOutLinkManId(listLinkMan.get(i).getOutLinkManId());
						oac.setCustomerId(customer.getId());
						crmDao.add(oac);
					}
				}
			}
			
			
			//先删除原来客户联系人
			/*2018.6.8   crmDao.delByField("linkMan", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),customer.getId()});
			//客户联系人关联存入
			List<LinkMan> listLinkMan = customer.getListLinkMan();
			if(null!=listLinkMan){
				for(LinkMan vo : listLinkMan){
					if((null!=vo.getLinkManName() && !"".equals(vo.getLinkManName()))){
						vo.setComId(customer.getComId());
						vo.setCustomerId(customer.getId());
						crmDao.add(vo);
					}
				}
			}*/
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"客户联系人维护");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 逐个删除联系人
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean delLinkMan(LinkMan linkMan,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//重置阅读人员
			this.delCrmRead(linkMan.getCustomerId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(linkMan.getCustomerId(), userInfo);
			
			//获取联系人删除对象
			LinkMan delObj = crmDao.queryLinkMan(userInfo.getComId(),linkMan.getCustomerId(),linkMan.getId());
			//删除客户联系人
			//2018.6.7 crmDao.delByField("linkman", new String[]{"comId","customerId","id"},new Integer[]{userInfo.getComId(),linkMan.getCustomerId(),linkMan.getId()});
			crmDao.delByField("olmAndCus", new String[]{"comId","customerId","id"},new Integer[]{userInfo.getComId(),linkMan.getCustomerId(),linkMan.getId()});
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), linkMan.getCustomerId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,linkMan.getCustomerId(),"删除客户联系人\""+delObj.getLinkManName()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),linkMan.getCustomerId(),userInfo.getId(),"删除客户联系人\""+delObj.getLinkManName()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(linkMan.getCustomerId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 
	 * 根据主键获取客户联系人信息
	 * @param comId
	 * @param linkManId
	 * @return
	 */
	public LinkMan queryLinkMan(Integer comId,Integer customerId,Integer linkManId){
		return crmDao.queryLinkMan(comId,customerId,linkManId);
	}
	/**
	 * 客户传真更新
	 * @param customer
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomerFax(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,customer.getId(),"传真更新为:\""+customer.getFax()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//客户传真更新
			crmDao.update("update customer a set a.fax=:fax where a.comid=:comId and a.id=:id", customer);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"传真更新为:\""+customer.getFax()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户联系电话更新
	 * @param customer
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomerLinePhone(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,customer.getId(),"客户联系电话更新为:\""+customer.getLinePhone()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//客户联系电话更新
			crmDao.update("update customer a set a.linePhone=:linePhone where a.comid=:comId and a.id=:id", customer);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"客户联系电话更新为:\""+customer.getLinePhone()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户联系地址更新
	 * @param customer
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomerAddress(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,customer.getId(),"更新客户联系地址:\""+customer.getAddress()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//客户联系地址更新
			crmDao.update("update customer a set a.address=:address where a.comid=:comId and a.id=:id", customer);
		
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"更新客户联系地址:\""+customer.getAddress()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户邮编更新
	 * @param customer
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomerPostCode(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,customer.getId(),"客户邮编更新为:\""+customer.getPostCode()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//客户邮编更新
			crmDao.update("update customer a set a.postCode=:postCode where a.comid=:comId and a.id=:id", customer);
		
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"客户邮编更新为:\""+customer.getPostCode()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户备注更新
	 * @param customer
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomerRemark(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,customer.getId(),"更新客户备注为:\""+customer.getCustomerRemark()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//客户备注更新
			crmDao.update("update customer a set a.customerRemark=:customerRemark where a.comid=:comId and a.id=:id", customer);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"更新客户备注为:\""+customer.getCustomerRemark()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 客户公开或私有更新
	 * @param customer
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomerPubState(Customer customer,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customer.getId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,customer.getId(),"更新客户备注为:\""+customer.getCustomerRemark()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//重置阅读人员
			this.delCrmRead(customer.getId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(customer.getId(), userInfo);
			
			//删除分享范围记录
			crmDao.update("delete from customerSharer a  where a.comid=:comId and a.customerId=:id", customer);
			//当公开私有为私有的时候 默认添加操作人和负责人
			if(customer.getPubState().equals(0)) {
				Set<Integer> shareIdSets = new HashSet<Integer>();
				//把当前操作人添加为客户参与人
				shareIdSets.add(userInfo.getId());
				//把客户负责人添加为客户参与人
				shareIdSets.add(customer.getOwner());
				//客户参与人存入
				if(null!=shareIdSets && shareIdSets.size()>0){
					CustomerSharer customerSharer =null;
					for (Integer shareId : shareIdSets) {
						customerSharer = new CustomerSharer();
						customerSharer.setComId(userInfo.getComId());
						customerSharer.setCustomerId(customer.getId());
						customerSharer.setUserId(shareId);
						crmDao.add(customerSharer);
					}
				}
			
			}
			//客户公开私有更新
			crmDao.update("update customer a set a.pubState=:pubState where a.comid=:comId and a.id=:id", customer);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"更新客户公开私有范围为:\""+(customer.getPubState().equals(0)?"私有":"公开")+"\"");
			//更新客户索引信息
			this.updateCustomerIndex(customer.getId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	
	/**
	 * 获取客户日志
	 * @param comId
	 * @param customerId
	 * @return
	 */
	public List<CustomerLog> listCustomerLog(Integer comId,Integer customerId){
		return crmDao.listCustomerLog(comId,customerId);
	}
	
	/**
	 * 获取客户附件文档集合
	 * @param comId
	 * @param customerId
	 * @return
	 */
	public List<CustomerUpfile> listPagedCustomerUpfile(Integer comId,CustomerUpfile customerUpfile){
		return crmDao.listPagedCustomerUpfile(comId,customerUpfile);
	}

	/**
	 * 获取反馈类型集合
	 * @param comId
	 * @return
	 */
	public List<FeedBackType> listFeedBackType(Integer comId){
		return crmDao.listFeedBackType(comId);
	}
	/**
	 * 初始化客户反馈类型排序值
	 * @param comId
	 * @return
	 */
	public Integer queryFeedBackTypeOrderMax(Integer comId){
		FeedBackType feedBackType = crmDao.queryFeedBackTypeOrderMax(comId);
		return null==feedBackType.getTypeOrder()?1:feedBackType.getTypeOrder();
	}
	/**
	 * 添加客户反馈类型
	 * @param feedBackType
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public Integer addFeedBackType(FeedBackType feedBackType,UserInfo userInfo){
		Integer id = crmDao.add(feedBackType);
		//添加系统日志记录 
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "添加客户反馈类型:\""+feedBackType.getTypeName()+"\"",
				ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
		return id;
	}
	/**
	 * 更新客户反馈类型名称
	 * @param feedBackType
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateFeedBackTypeName(FeedBackType feedBackType,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			FeedBackType obj = (FeedBackType) crmDao.objectQuery(FeedBackType.class, feedBackType.getId());
			//有变动
			if(null!=obj && !obj.getTypeName().equals(feedBackType.getTypeName())){
				//更新客户反馈类型名称
				crmDao.update("update feedBackType a set a.typeName=:typeName where a.comid=:comId and a.id=:id", feedBackType);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "更新客户反馈类型名称:\""+feedBackType.getTypeName()+"\"",
						ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 更新客户反馈类型
	 * @param feedBackType
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateFeedBackTypeOrder(FeedBackType feedBackType,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//获取更新客户反馈类型对象
			FeedBackType orgObj = crmDao.queryFeedBackType(feedBackType.getComId(), feedBackType.getId());
			//有变动
			if(null!=orgObj && !orgObj.getTypeOrder().equals(feedBackType.getTypeOrder())){
				//更新客户类型排序
				crmDao.update("update feedBackType a set a.typeOrder=:typeOrder where a.comid=:comId and a.id=:id", feedBackType);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "更新客户反馈类型:\""+orgObj.getTypeName()+"\"的排序为\""+feedBackType.getTypeOrder()+"\"",
						ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 删除客户反馈类型
	 * @param feedBackType
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean delFeedBackType(FeedBackType feedBackType,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//获取需要删除的客户反馈类型
			FeedBackType orgObj = crmDao.queryFeedBackType(feedBackType.getComId(), feedBackType.getId());
			if(null!= orgObj){
				//客户反馈类型删除
				crmDao.update("delete from feedBackType a where a.comid=:comId and a.id=:id", feedBackType);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除客户反馈类型:\""+orgObj.getTypeName()+"\"",
						ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
		}
		return succ;
	}
	/**
	 * 添加新维护记录
	 * @param feedBackInfo
	 * @param msgShare
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Integer addFeedBackInfo(FeedBackInfo feedBackInfo,MsgShare msgShare,UserInfo userInfo) throws Exception{
		Customer customer = (Customer) crmDao.objectQuery(Customer.class, feedBackInfo.getCustomerId());
		//客户负责人和留言父用户
		List<UserInfo> shares = userInfoService.listOwnerForMsg(userInfo.getComId(), feedBackInfo.getCustomerId(), feedBackInfo.getParentId(),ConstantInterface.TYPE_CRM,null);
		//客户的所有查看人
		//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), feedBackInfo.getCustomerId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null,feedBackInfo.getCustomerId(),"添加新维护记录:"+feedBackInfo.getContent(), ConstantInterface.TYPE_CRM, shares,null);
		
		//重置阅读人员
		this.delCrmRead(feedBackInfo.getCustomerId(), userInfo);
		//把当前操作人添加到客户查看记录表中
		this.addCrmRead(feedBackInfo.getCustomerId(), userInfo);
		
		//追踪记录信息存入
		Integer feedBackInfoId = crmDao.add(feedBackInfo);
		
		//模块日志添加
		this.addCustomerLog(userInfo.getComId(),feedBackInfo.getCustomerId(),userInfo.getId(),"添加新维护记录。");
		//添加客户维护记录的附件
		this.addCrmFeedBackFile(feedBackInfo, userInfo, customer, feedBackInfoId);
		
		//添加信息分享人员
		List<CustomerSharer> listCustomerSharer = feedBackInfo.getListCustomerSharers();
		Set<Integer> pushUserIdSet = new HashSet<Integer>();
		if(null != listCustomerSharer && !listCustomerSharer.isEmpty()){
			for (CustomerSharer customerSharer : listCustomerSharer) {
				//人员主键
				Integer userId = customerSharer.getUserId();
				pushUserIdSet.add(userId);
				
				//客户私有才添加分享才添加分享人员
				if(PubStateEnum.NO.getValue().equals(customer.getPubState())){
					//删除上次的分享人员
					crmDao.delByField("customerSharer", new String[]{"comId","customerId","userId"},
							new Object[]{userInfo.getComId(),feedBackInfo.getCustomerId(),userId});
					customerSharer.setCustomerId(feedBackInfo.getCustomerId());
					customerSharer.setComId(userInfo.getComId());
					crmDao.add(customerSharer);
				}
			}
		}

		//分享信息查看
		List<UserInfo> custormerShare = new ArrayList<UserInfo>();
		
		if (null != customer) {
			//查询消息的推送人员
			custormerShare = crmDao.listPushTodoUserForCustomer(feedBackInfo.getCustomerId(), userInfo.getComId(),pushUserIdSet);
			Iterator<UserInfo> userids =  custormerShare.iterator();
			for(;userids.hasNext();){
				UserInfo user = userids.next();
				if(user.getId().equals(userInfo.getId())){
					userids.remove();
					custormerShare.remove(user);
				}
				//设置全部普通消息
				todayWorksService.updateTodayWorksBusSpecTo0(ConstantInterface.TYPE_CRM, feedBackInfo.getCustomerId(), user.getId());
			}
			// 添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_CRM, feedBackInfo.getCustomerId(), customer.getCustomerName()+"有更新！",
					custormerShare, userInfo.getId(), 1);

			//清除上次查看记录
			viewRecordService.delViewRecord(userInfo,custormerShare,feedBackInfo.getCustomerId(),ConstantInterface.TYPE_CRM);
		}

		//为反馈信息创建索引
//		this.updateCustomerIndex(feedBackInfo.getCustomerId(), userInfo,"update");
		//添加积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_CRMTALK,
				"添加客户维护记录",feedBackInfo.getCustomerId());
		
		// 添加客户维护记录
		busUpdateService.addBusUpdate(userInfo, feedBackInfo.getCustomerId(), ConstantInterface.TYPE_CRM,
				ConstantInterface.BUSUPDATETYPE_FEEDBACK, "添加新维护记录:"+feedBackInfo.getContent());
					
		return feedBackInfoId;
	}
	/**
	 * 添加客户维护记录的附件
	 * @param feedBackInfo 反馈信息
	 * @param userInfo 当前操作人员
	 * @param customer 客户信息
	 * @param feedBackInfoId 反馈主键
	 */
	private void addCrmFeedBackFile(FeedBackInfo feedBackInfo,
			UserInfo userInfo, Customer customer, Integer feedBackInfoId)
			throws Exception {
		//记录附件
		Integer[] upfilesId = feedBackInfo.getUpfilesId();
		if(null!=upfilesId && upfilesId.length>0){
			for (Integer upfileId : upfilesId) {
				FeedInfoFile feedInfoFile = new FeedInfoFile();
				//企业编号
				feedInfoFile.setComId(feedBackInfo.getComId());
				//客户主键
				feedInfoFile.setCustomerId(feedBackInfo.getCustomerId());
				//反馈信息主键
				feedInfoFile.setBackInfoId(feedBackInfoId);
				//附件主键
				feedInfoFile.setUpfileId(upfileId);
				//附件上传人
				feedInfoFile.setUserId(feedBackInfo.getUserId());
				crmDao.add(feedInfoFile);
				//为附件创建索引
				uploadService.updateUpfileIndex(upfileId, userInfo, "add",feedBackInfo.getCustomerId(),ConstantInterface.TYPE_CRM);
			}
			//归档到文档中心
			fileCenterService.addModFile(userInfo,Arrays.asList(upfilesId),customer.getCustomerName());
		}
	}
	/**
	 * 获取客户维护信息列表
	 * @param comId
	 * @param customerId
	 * @return
	 */
	public List<FeedBackInfo> listFeedBackInfo(Integer comId,Integer customerId,String dest){
		//查询客户的分页反馈信息
		List<FeedBackInfo> listFeedBackInfo = crmDao.listFeedBackInfo(comId,customerId);
		//需要返回的结果集
		List<FeedBackInfo> list = new ArrayList<FeedBackInfo>();
		//遍历查询结果
		if(null!=listFeedBackInfo && !listFeedBackInfo.isEmpty()){
			//查询所有的反馈附件
			List<FeedInfoFile> feedInfoFiles = crmDao.listFeedInfoFiles(comId,customerId,null);
			//缓存反馈信息和附件集合的关系
			Map<Integer, List<FeedInfoFile>> map = new HashMap<Integer, List<FeedInfoFile>>();
			//判断是否有附件信息
			if(null!=feedInfoFiles && !feedInfoFiles.isEmpty()){
				//遍历附件信息，放入缓存中
				for(FeedInfoFile feedInfoFile : feedInfoFiles){
					//反馈主键
					Integer backInfoId = feedInfoFile.getBackInfoId();
					//取得反馈对应的附件集合
					List<FeedInfoFile> infoFiles = map.get(backInfoId);
					if(null == infoFiles){
						infoFiles = new ArrayList<FeedInfoFile>();
					}
					//附件集合添加数据
					infoFiles.add(feedInfoFile);
					map.put(backInfoId, infoFiles);
				}
			}
			for (FeedBackInfo feedBackInfo : listFeedBackInfo) {
				//手机端不需要字符转换
				if(!"app".equals(dest)){
					//表情替换
					feedBackInfo.setContent(StringUtil.toHtml(feedBackInfo.getContent()));
				}
				//反馈的附件信息
				feedBackInfo.setListfeedInfoFiles(map.get(feedBackInfo.getId()));
				list.add(feedBackInfo);
			}
		}
		return list;
	}
	/**
	 * 根据主键获取客户维护信息明细
	 * @param comId
	 * @param feedBackInfoId
	 * @return
	 */
	public FeedBackInfo queryFeedBackInfo(Integer comId,Integer feedBackInfoId){
		FeedBackInfo feedBackInfo = crmDao.queryFeedBackInfo(comId,feedBackInfoId);
		//反馈的附件信息
		feedBackInfo.setListfeedInfoFiles(crmDao.listFeedInfoFiles(comId,feedBackInfo.getCustomerId(),feedBackInfo.getId()));
		return feedBackInfo;
	}
	/**
	 * 删除客户维护记录
	 * @param feedBackInfo
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean delFeedBackInfo(FeedBackInfo feedBackInfo,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			if("yes".equals(feedBackInfo.getDelChildNode())){
				List<FeedBackInfo> feedBackInfos = crmDao.listFeedBackInfoForDel(userInfo.getComId(), feedBackInfo.getId());
				for (FeedBackInfo feedBackInfo2 : feedBackInfos) {
					//删除附件
					crmDao.delByField("feedInfoFile", new String[]{"comId","backInfoId"}, 
							new Object[]{feedBackInfo2.getComId(),feedBackInfo2.getId()});
				}
				//删除当前客户维护记录节点及其子节点回复
				crmDao.delFeedBackInfo(userInfo.getComId(), feedBackInfo.getId());
			}else{
				//删除附件
				crmDao.delByField("feedInfoFile", new String[]{"comId","backInfoId"}, 
						new Object[]{userInfo.getComId(),feedBackInfo.getId()});
				//把客户维护记录子节点的parentId向上提一级
				crmDao.updateFeedBackInfoParentId(userInfo.getComId(), feedBackInfo.getId());
				//删除当前回复节点
				crmDao.delByField("feedBackInfo", new String[]{"comId","id"}, new Object[]{userInfo.getComId(),feedBackInfo.getId()});
			}
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),feedBackInfo.getCustomerId(),userInfo.getId(),"删除客户维护记录。");
			//修改积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_CRMTALKDEL,
					"删除客户维护记录",feedBackInfo.getCustomerId());
			
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), feedBackInfo.getCustomerId());
			List<UserInfo> shares =userInfoService.listOwnerForMsg(userInfo.getComId(), feedBackInfo.getCustomerId(), null,ConstantInterface.TYPE_CRM,null);
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,null,feedBackInfo.getCustomerId(),"删除客户维护记录", ConstantInterface.TYPE_CRM, shares,null);
			
			//重置阅读人员
			this.delCrmRead(feedBackInfo.getCustomerId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(feedBackInfo.getCustomerId(), userInfo);
			//客户索引库更新
//			this.updateCustomerIndex(feedBackInfo.getCustomerId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户移交
	 * @param userInfo 操作员
	 * @return
	 * @throws Exception
	 */
	public boolean addCustomerHandOver(CustomerHandOver[] customerHandOvers,UserInfo userInfo){
		boolean succ = true;
		try {
			if(null!=customerHandOvers){
				Customer customer = null;
				for(CustomerHandOver customerHandOver:customerHandOvers){
					//移交的客户主键
					Integer crmId = customerHandOver.getCustomerId();
					this.addCrmFeedbackByHandOver(userInfo, customerHandOver, crmId);
					//客户负责人变更
					customer = new Customer();
					customer.setComId(userInfo.getComId());
					customer.setId(crmId);
					customer.setOwner(customerHandOver.getToUser());
					crmDao.update("update customer a set a.owner=:owner where a.comid=:comId and a.id=:id", customer);
					//移交记录添加
					crmDao.add(customerHandOver);
					//获取移交对象人员信息
					UserInfo toUser = userInfoService.getUserInfo(userInfo.getComId(),customerHandOver.getToUser());
					//客户的所有查看人
					//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), crmId);
					//通知接受人
					List<UserInfo> shares = new ArrayList<UserInfo>();
					shares.add(toUser);
					//添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo,customerHandOver.getToUser(),customerHandOver.getCustomerId(),"客户移交给了\""+toUser.getUserName()+"\"", ConstantInterface.TYPE_CRM, shares,null);
					//重置阅读人员
					this.delCrmRead(customerHandOver.getCustomerId(), userInfo);
					//把当前操作人添加到客户查看记录表中
					this.addCrmRead(customerHandOver.getCustomerId(), userInfo);
					
					//模块日志添加
					this.addCustomerLog(userInfo.getComId(),customerHandOver.getCustomerId(),userInfo.getId(),"把客户移交给了\""+toUser.getUserName()+"\"");
					
					customer = (Customer) crmDao.objectQuery(Customer.class, customerHandOver.getCustomerId());
					if(null!=customer){
						//当客户类型为私有的时候
						if(customer.getPubState().equals(PubStateEnum.NO.getValue())) {
							//验证当前操作人是不是分享人，不是则把操作人存入客户参与人中
							CustomerSharer oprator = crmDao.getCustomerSharer(userInfo.getComId(),crmId,userInfo.getId());
							if(null == oprator){
								CustomerSharer sharer = new CustomerSharer();
								sharer.setComId(userInfo.getComId());
								sharer.setCustomerId(crmId);
								sharer.setUserId(userInfo.getId());
								crmDao.add(sharer);
							}
							
						}
						
						//客户名称
						String crmName = StringUtil.cutStrFace(customer.getCustomerName(), 23);
						
						//添加工作轨迹
						systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_CRM, 
								customerHandOver.getCustomerId(), "移交的客户\""+crmName+"\"给了\""+toUser.getUserName()+"\"", "负责\""+userInfo.getUserName()+"\"移交的客户\""+crmName);
						//取得待办事项主键
						TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),customerHandOver.getToUser(),
								crmId,ConstantInterface.TYPE_CRM,0);
						if(null!=todayWorks){
							JpushUtils.sendTodoMessage(userInfo.getComId(), customerHandOver.getToUser(), userInfo.getId(),
									todayWorks.getId(), crmId, ConstantInterface.TYPE_CRM,0,"客户:"+crmName);
						}
					}
					//客户索引库更新
//					this.updateCustomerIndex(customerHandOver.getCustomerId(), userInfo,"update");
					
					//添加客户移交记录
					busUpdateService.addBusUpdate(userInfo, customerHandOver.getCustomerId(), ConstantInterface.TYPE_CRM,
							ConstantInterface.BUSUPDATETYPE_HANDOVER, "把客户移交给了\""+toUser.getUserName()+"\"");
				}
			}
			
		} catch (Exception e) {
			succ = false ;
		}
		return succ;
	}
	/**
	 * 把移交说明添加到客户维护记录当中
	 * @param userInfo
	 * @param customerHandOver
	 * @param crmId
	 */
	private void addCrmFeedbackByHandOver(UserInfo userInfo,
			CustomerHandOver customerHandOver, Integer crmId) {
		FeedBackInfo feedBackInfo = null;
		//把移交说明添加到客户维护记录当中
		if(!StringUtil.isBlank(StringUtil.trim(customerHandOver.getReplayContent())) && null!=customerHandOver.getFeedBackTypeId() && !"".equals(customerHandOver.getFeedBackTypeId())){
			feedBackInfo = new FeedBackInfo();
			feedBackInfo.setComId(userInfo.getComId());
			feedBackInfo.setContent(customerHandOver.getReplayContent());
			feedBackInfo.setCustomerId(crmId);
			feedBackInfo.setFeedBackTypeId(customerHandOver.getFeedBackTypeId());
			feedBackInfo.setParentId(-1);
			feedBackInfo.setUserId(userInfo.getId());
			crmDao.add(feedBackInfo);
		}
	}
	/**
	 * 获取匹配客户JSON字符串
	 * @param customer
	 * @return
	 */
	public String partnerJson(Customer customer){
		List<Customer> listCustomer = crmDao.listCustomer(customer);
		StringBuffer strJson=new StringBuffer();
		if(null!=listCustomer && !listCustomer.isEmpty()){
			strJson.append("[");
			for(Customer vo:listCustomer){
				strJson.append("{\"id\":\""+vo.getId()+"\",\"name\":\""+vo.getCustomerName()+"\"},");
			}
			strJson=new StringBuffer(strJson.substring(0,strJson.lastIndexOf(",")));
			strJson.append("]");
		}
		return strJson.toString();
	}
	/**
	 * 更新客户索引
	 * @param customerId
	 * @param userInfo
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception 
	 */
	public void updateCustomerIndex(Integer customerId,UserInfo userInfo,String opType) throws Exception{
		//更新客户索引
		Customer customer = crmDao.queryCustomer(userInfo,customerId);
		//再添加新索引数据
		//把bean非空属性值连接策划那个字符串
		//不检索字段设置为null
//		customer.setId(null);
//		customer.setAreaId(null);
//		customer.setAreaIdAndType(null);
//		customer.setComId(null);
//		customer.setCustomerTypeId(null);
//		customer.setGroupIdAndType(null);
//		customer.setOwner(null);
//		customer.setCreator(null);
//		customer.setFileName(null);
//		customer.setUuid(null);
//		StringBuffer attStr = new StringBuffer(CommonUtil.objAttr2String(customer,null));
		StringBuffer attStr = new StringBuffer(customer.getCustomerName());
		//获取客户所有参与人
//		List<CustomerSharer> listCustomerOwners = crmDao.listCustomerOwners(userInfo.getComId(),customerId);
//		//参与人名称连接成字符串创建索引
//		for(CustomerSharer vo : listCustomerOwners){
//			attStr.append(vo.getUserName()+",");
//		}
		//客户联系人接成字符串创建索引
//		List<LinkMan> listLinkMan = crmDao.listLinkMan(userInfo.getComId(),customerId);
//		if(null!=listLinkMan){
//			for(LinkMan vo : listLinkMan){
//				vo.setId(null);
//				vo.setRecordCreateTime(null);
//				vo.setCustomerId(null);
//				vo.setComId(null);
//				attStr.append(CommonUtil.objAttr2String(vo,null));
//			}
//		}
		//客户维护记录创建字符串索引
//		List<FeedBackInfo> listFeedBackInfo = crmDao.listFeedBackInfo4Index(userInfo.getComId(),customerId);
//		for(FeedBackInfo vo:listFeedBackInfo){
//			attStr.append(CommonUtil.objAttr2String(vo,null));
//		}
		//获取客户附件集合
//		List<CustomerUpfile> listUpfiles = crmDao.listPagedCustomerUpfile(userInfo.getComId(), customerId);
//		if(null!=listUpfiles){
//			Upfiles upfile = null;
//			for(CustomerUpfile vo:listUpfiles){
//				//附件内容添加
//				upfile = uploadService.queryUpfile4Index(vo.getUpfileId(),userInfo.getComId());
//				//附件名称
//				attStr.append(upfile.getFilename()+",");
//				//附件内容
//				attStr.append(upfile.getFileContent()+",");
//			}
//		}
		//返回一个线程池（这个线程池只有一个线程）,这个线程池可以在线程死后（或发生异常时）重新启动一个线程来替代原来的线程继续执行下去！
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String indexKey = userInfo.getComId()+"_"+ConstantInterface.TYPE_CRM+"_"+customerId;
		//为客户创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				indexKey,userInfo.getComId(),customerId,ConstantInterface.TYPE_CRM,
				customer.getCustomerName(),attStr.toString(),DateTimeUtil.parseDate(customer.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,indexKey));
		}
		
	}
	/**
	 * 现有客户区域集合
	 * @param comId 企业编号
	 * @return
	 */
	public List<Area> listArea(Integer comId) {
		return crmDao.listArea(comId);
	}
	/**
	 * 现有客户区域集合
	 * @return
	 */
	public List<Area4App> crmAreaTreeList(UserInfo userInfo) {
		List<Region> list = crmDao.listRegionForStatistic(userInfo);
		List<Area4App> result = new ArrayList<Area4App>();
		if(null != list && !list.isEmpty()){
			//区域主键集合
			List<Integer> regionsIds = new ArrayList<Integer>();
			//区域主键对应区域实体类
			Map<Integer,Region> regionIdMap = new HashMap<Integer, Region>();
			for (Region region : list) {
				regionsIds.add(region.getId());
				regionIdMap.put(region.getId(), region);
			}
			//查询区域主键信息
			List<Area> areas = crmDao.listTreeArea(userInfo,regionsIds);
			
			if(null!=areas && !areas.isEmpty()){
				
				Area4App rootArea = new Area4App();
				rootArea.setId(0);
				rootArea.setAreaName("请选择");
				
				List<Area4App> leve1ChildRoot = new ArrayList<Area4App>();
				
				Area4App rootAreaChild = new Area4App();
				rootAreaChild.setId(0);
				rootAreaChild.setAreaName("请选择");
				
				List<Area4App> leve2ChildRoot = new ArrayList<Area4App>();
				Area4App level1AreaChild = new Area4App();
				level1AreaChild.setId(0);
				level1AreaChild.setAreaName("请选择");
				leve2ChildRoot.add(level1AreaChild);
				
				
				rootAreaChild.setChildren(leve2ChildRoot);
				leve1ChildRoot.add(rootAreaChild);
				
				rootArea.setChildren(leve1ChildRoot);
				result.add(rootArea);
				
				Area4App areaLeve1 = null;
				Area4App areaLeve2 = null;
				List<Area4App> leve1Child = new ArrayList<Area4App>();
				List<Area4App> leve2Child = new ArrayList<Area4App>();
				for(int i=0;i<areas.size();i++){
					Area areaMod = areas.get(i);
					Integer level = areaMod.getLevel();
					
					Area4App area = new Area4App();
					area.setId(areaMod.getId());
					area.setAreaName(areaMod.getAreaName());
					
					if(level == 1){
						if(null!=areaLeve1){
							Area4App pleaseArea = new Area4App();
							pleaseArea.setId(0);
							pleaseArea.setAreaName("请选择");
							
							Area4App pleaseAreaChild = new Area4App();
							pleaseAreaChild.setId(0);
							pleaseAreaChild.setAreaName("请选择");
							
							List<Area4App> pleaseAreaChilds = new ArrayList<Area4App>();
							pleaseAreaChilds.add(pleaseAreaChild);
							
							pleaseArea.setChildren(pleaseAreaChilds);
							leve1Child.add(0,pleaseArea);
							
							areaLeve1.setChildren(leve1Child);
							result.add(areaLeve1);
							leve1Child = new ArrayList<Area4App>();
						}
						areaLeve1 = area;
					}else if(level == 2){
						if(null!=areaLeve2){
							Area4App pleaseArea = new Area4App();
							pleaseArea.setId(0);
							pleaseArea.setAreaName("请选择");
							leve2Child.add(0,pleaseArea);
							
							areaLeve2.setChildren(leve2Child);
							leve1Child.add(areaLeve2);
							
							leve2Child = new ArrayList<Area4App>();
						}
						areaLeve2 = area;
					}else if(level == 3){
						leve2Child.add(area);
					}
					
				}
			}
		}
		return result;
	}
	
	public List<Area> listCrmAreaForRelevance(Integer comId,Integer areaPid) {
		return crmDao.listCrmAreaForRelevance(comId,areaPid);
	}
	/**
	 * 模板区域集合
	 * @param integer 
	 * @param area 
	 * @return
	 */
	public List<Region> listRegion(Area area, Integer comId) {
		Integer level = area.getLevel();
		Integer regionId = area.getRegionId();
		//取得团队已导入的省级区域
		return  crmDao.getHasInRegions(comId,regionId,level);
		//return crmDao.listRegion(null,comId);
	}
	/**
	 * 从模板添加区域
	 * @param areas 模板选中的区域
	 * @param userInfo 操作的用户
	 * 
	 */
	public void addAreaFromMod(List<Area> areas, UserInfo userInfo) {
		
		/**
		 * 思路：
		 * 1、若是已导入的区域,则缓存 模板主键---区域主键，用于在导入子区域的时候通过 子区域的父类模板主键 设置 子区域的父类主键
		 */
		
		
		
		//<模板区域ID,新的区域ID> 通过模板主键取得对应的区域主键
		Map<Integer, Integer> areaMap = new HashMap<Integer, Integer>();
		//<新的区域ID,新的区域子节点顺序> 通过区域主键取得子节点排序
		Map<Integer, Integer> orderMap = new HashMap<Integer, Integer>();
		
		Integer comId = userInfo.getComId();
		for (Area area : areas) {
			
			//是否需要导入
			Integer needMod = area.getNeedMod();
			if(needMod == 0){
				//区域主键
				Integer areaId = area.getId();
				//添加模板主键取得对应的区域
				areaMap.put(area.getRegionId(), areaId);
			}else{//需要导入的
				//区域的企业
				area.setComId(comId);
				//创建人
				area.setCreator(userInfo.getId());
				//区域名称不为空时此时添加用户名称全拼和简拼
				if(null!=area.getAreaName()){
					area.setAllSpelling(PinyinToolkit.cn2Spell(area.getAreaName()));
					area.setFirstSpelling(PinyinToolkit.cn2FirstSpell(area.getAreaName()));
				}
				//通过模板主键取得对应的区域主键作为新区域的父Id
				Integer areaParentId = areaMap.get(area.getModParentId());
				
				//没有，则用页面传入的parentId
				if(null==areaParentId){
					//通过区域的主键取得子节点最大排序号
					Integer areaOrder = orderMap.get(area.getParentId());
					//没有排序号则从数据库查找
					if(null == areaOrder){
						areaOrder = crmDao.initAreaOrder(comId,area.getParentId());
					}
					area.setAreaOrder(areaOrder);
					//添加区域
					Integer areaId = crmDao.add(area);
					//重新设置排序
					orderMap.put(area.getParentId(), areaOrder+1);
					//添加模板主键取得对应的区域
					areaMap.put(area.getRegionId(), areaId);
					
				}else{//有
					//通过区域的主键取得子节点最大排序号
					Integer areaOrder = orderMap.get(areaParentId);
					//没有排序号则从数据库查找
					if(null==areaOrder){
						areaOrder = crmDao.initAreaOrder(comId,areaParentId);
					}
					area.setAreaOrder(areaOrder);
					//重新设置排序
					orderMap.put(area.getParentId(), areaOrder+1);
					//区域的父ID
					area.setParentId(areaParentId);
					//添加区域
					Integer areaId = crmDao.add(area);
					//是叶节点
					if(area.getIsLeaf()==0){
						//添加模板主键取得对应的区域
						areaMap.put(area.getRegionId(), areaId);
					}
					
				}
			}
		}
	}

	/**
	 * 获取客户下面的联系人集合
	 * @param comId 信息企业主键
	 * @param customerId 客户主键
	 * @return
	 */
	public List<LinkMan> listCrmLinkMan(Integer comId,Integer customerId){
		//获取客户联系人集合
		return crmDao.listLinkMan(comId, customerId);
	}
	/**
	 * 获取用户查看客户记录信息
	 * @param comId
	 * @param customerId
	 * @param userId
	 * @return
	 */
	public CustomerRead queryCustomerRead(Integer comId,Integer customerId,Integer userId){
		return crmDao.queryCustomerRead(comId,customerId,userId);
	} 
	/**
	 * 人员负责的客户
	 * @param comId企业号
	 * @param userId 人员主键
	 * @return
	 */
	public List<Customer> listUserAllCrm(Integer comId, Integer userId) {
		return crmDao.listUserAllCrm(comId,userId);
	}
	/**
	 * 移交记录
	 * @param crmId 客户主键
	 * @param comId 企业号
	 * @return
	 */
	public List<FlowRecord> listFlowRecord(Integer crmId, Integer comId) {
		return crmDao.listFlowRecord(crmId,comId);
	}
	/**
	 * 客户详情(不需要客户联系人)
	 * @param userInfo 操作员
	 * @param crmId 客户主键
	 * @return
	 */
	public Customer getCrmById(UserInfo userInfo, Integer crmId) {
		return crmDao.queryCustomer(userInfo, crmId);
	}
	/**
	 * 客户详情(纯粹客户信息)
	 * @param crmId 客户主键
	 * @return
	 */
	public Customer getCrmById(Integer crmId) {
		return (Customer) crmDao.objectQuery(Customer.class, crmId);
	}
	/**
	 * 获取项目成员组集合
	 * @param itemId
	 * @param userId
	 * @param comId
	 * @return
	 */
	public List<SelfGroup> listSelfGroupOfCrm(Integer customerId,Integer userId,Integer comId){
		return crmDao.listSelfGroupOfCrm(customerId,userId,comId);
	}
	/**
	 * 获取已经和客户关联的私有组集合
	 * @param customerId
	 * @param comId
	 * @return
	 */
	public List<SelfGroup> listShareGroupOfCrm(int customerId,int comId){
		return crmDao.listShareGroupOfCrm(customerId,comId);
	}
	/**
	 * 更新客户成员组
	 * @param customerId 客户主键
	 * @param grpIds 本次分组信息
	 * @param userInfo 操作人员
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomerGroup(Integer customerId,Integer[] grpIds,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			//先删除项目组成员关联
			crmDao.delByField("customerShareGroup", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),customerId});
			//再关联新组
			if(null!=grpIds && grpIds.length>0){
				CustomerShareGroup customerShareGroup = null;
				for(Integer id:grpIds){
					customerShareGroup = new CustomerShareGroup();
					customerShareGroup.setComId(userInfo.getComId());
					customerShareGroup.setGrpId(id);
					customerShareGroup.setCustomerId(customerId);
					crmDao.add(customerShareGroup);
				}
			}
			List<SelfGroup> listShareGroup = crmDao.listShareGroupOfCrm(customerId,userInfo.getComId());
			StringBuffer shareGroup = new StringBuffer();
			if(null!=listShareGroup && !listShareGroup.isEmpty()){
				for(SelfGroup vo:listShareGroup){
					shareGroup.append(vo.getGrpName()+"、");
				}
				shareGroup = new StringBuffer(shareGroup.substring(0,shareGroup.lastIndexOf("、")));
			}

			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), customerId);
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,customerId,"客户成员组关联为\""+shareGroup.toString()+"\"", BusinessTypeConstant.type_crm, shares,null);
			
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),customerId,userInfo.getId(),"客户成员组关联为\""+shareGroup.toString()+"\"");
			//客户索引库更新
//			this.updateCustomerIndex(customerId, userInfo,"update");
		} catch (Exception e) {
			succ = false;
			throw e;
		}
		return succ;
	}
	/**
	 * 获取个人权限下排列前N的客户数据集合
	 * @param userInfo
	 * @param isForceInPersion
	 * @param rowNum
	 * @return
	 */
	public List<Customer> firstNCustomerList(UserInfo userInfo,boolean isForceInPersion,Integer rowNum){
		return crmDao.firstNCustomerList(userInfo,isForceInPersion,rowNum);
	}
	
	/**
	 * 添加客户查看记录
	 * @param crmId 客户主键
	 * @param userInfo 操作人员
	 */
	public void addCrmRead(Integer crmId,UserInfo userInfo){
		CustomerRead customerRead = crmDao.queryCustomerRead(userInfo.getComId(), crmId, userInfo.getId());
		if(null==customerRead){
			//向客户查看记录添加查看记录
			customerRead = new CustomerRead();
			customerRead.setComId(userInfo.getComId());
			customerRead.setCustomerId(crmId);
			customerRead.setUserId(userInfo.getId());
			crmDao.add(customerRead);
		}
	}
	/**
	 * 客户信息有变动，重新设置阅读人员
	 * @param crmId 客户主键
	 * @param userInfo 操作人员
	 */
	public void delCrmRead(Integer crmId,UserInfo userInfo){
		crmDao.delByField("customerRead", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),crmId});
	}
	
	/**
	 * 取得消息的接收对象
	 * @param comId 企业号
	 * @param modId 模块主键
	 * @return
	 */
	public List<UserInfo> listCrmOwnersNoForce(Integer comId, Integer modId){
		List<UserInfo> shares = crmDao.listCrmOwnersNoForce(comId, modId);
		return shares;
	}
	/**
	 * 根据客户名称查找相似客户
	 * @param crmName
	 * @param comId
	 * @return
	 */
	public List<Customer> listSimilarCrmsByCheckCrmName(String crmName,
			Integer comId) {
		List<Customer> list = crmDao.listSimilarCrmsByCheckCrmName(crmName,comId);
		return list;
	}
	/**
	 * 	合并客户信息
	 * @param crm合并后的客户
	 * @param ids 参与合并的客户
	 * @param userInfo 操作人员
	 * @throws Exception 
	 */
	public void updateCrmForCompress(Customer crm, Integer[] ids,
			UserInfo userInfo) throws Exception {
		if(null!=ids && ids.length>0){
			Integer crmId = crm.getId();
			//合并客户的主要信息
			crmDao.update(crm);
			//整合的客户分享人
			List<CustomerSharer> crmSharerList = crmDao.listCustomerSharer(userInfo.getComId(), crmId);
			//整合的客户分享人主键
			Set<Integer> sharerId = new HashSet<Integer>();
			
			if(null!=crmSharerList && crmSharerList.size()>0){
				for (CustomerSharer customerSharer : crmSharerList) {
					sharerId.add(customerSharer.getUserId());
				}
			}
			
			//合并前关注客户信息的人员
			List<Attention> listAtten = attentionService.listAtten(userInfo.getComId(), crmId, ConstantInterface.TYPE_CRM);
			//合并前关注客户信息的人员主键
			Set<Integer> attenUserId = new HashSet<Integer>();
			if(null != listAtten && listAtten.size()>0){
				for (Attention attention : listAtten) {
					attenUserId.add(attention.getUserId());
				}
			}
			
			String log = "";
			//开始合并信息
			for (Integer customerId : ids) {
				Customer crmT = (Customer) crmDao.objectQuery(Customer.class, customerId);
				log +="'"+crmT.getCustomerName()+"',";
				//是合并的客户
				if(customerId.equals(crmId)){
					continue;
				}
				//直接合并联系人
				crmDao.compressLinkMan(userInfo.getComId(), customerId,crmId);
				//直接合并外部联系人
				crmDao.compressOutLinkMan(userInfo.getComId(), customerId,crmId);
				//直接合并反馈信息和附件
				crmDao.compressFeedBack(userInfo.getComId(), customerId,crmId);
				//直接合并项目信息
				crmDao.compressItem(userInfo.getComId(), customerId,crmId);
				//整合闹铃
				clockService.compressClock(userInfo.getComId(), customerId,crmId,ConstantInterface.TYPE_CRM);
				//合并聊天室
				chatService.comPressChat(userInfo.getComId(), customerId,crmId,ConstantInterface.TYPE_CRM);
				//合并附件留言
				fileCenterService.comPressFileTalk(userInfo.getComId(), customerId,crmId,ConstantInterface.TYPE_CRM);
				//合并分享人
				List<CustomerSharer> crmSharer = crmDao.listCustomerSharer(userInfo.getComId(), customerId);
				if(null!=crmSharer && !crmSharer.isEmpty()){
					for (CustomerSharer customerSharer : crmSharer) {
						//有共同参与人
						if(sharerId.contains(customerSharer.getUserId())){
							//删除参与人
							crmDao.delById(CustomerSharer.class, customerSharer.getId());
							continue;
						}else{//没有共同的分享人，就添加数据
							
							sharerId.add(customerSharer.getUserId());
							//重新设置客户主键
							customerSharer.setCustomerId(crmId);
							//修改参与人所属客户信息
							crmDao.update(customerSharer);
						}
					}
				}
				//合并添加分组
				List<SelfGroup> listGrp = selfGroupService.addGrp(userInfo,customerId,userInfo.getId(),ConstantInterface.TYPE_CRM);
				if(null!=listGrp && listGrp.size()>0){
					crmDao.delByField("customerShareGroup", new String[]{"comId","customerId"}, new Object[]{userInfo.getComId(),customerId});
					for (SelfGroup selfGroup : listGrp) {
						//客户共享组
						CustomerShareGroup crmShareGroup = new CustomerShareGroup();
						//企业号
						crmShareGroup.setComId(userInfo.getComId());
						//客户主键
						crmShareGroup.setCustomerId(crmId);
						//分组主键
						crmShareGroup.setGrpId(selfGroup.getId());
						//添加客户共享组
						crmDao.add(crmShareGroup);
					}
				}
				//合并关注信息
				List<Attention> listAttention = attentionService.listAtten(userInfo.getComId(), customerId, ConstantInterface.TYPE_CRM);
				if(null != listAttention && listAttention.size() > 0){
					for (Attention attention : listAttention) {
						//人员同时关注合并的客户信息
						if(attenUserId.contains(attention.getUserId())){
							crmDao.delById(Attention.class, attention.getId());
							continue;
						}else{
							attenUserId.add(attention.getUserId());
							//重新设置业务主键
							attention.setBusId(crmId);
							crmDao.update(attention);
						}
					}
				}
				
				//删除外部联系人关联表
				crmDao.delByField("olmAndCus", new String[]{"comId","customerId"}, 
						new Object[]{userInfo.getComId(), customerId});
				//删除移交记录表
				crmDao.delByField("customerHandOver", new String[]{"comId","customerId"}, 
						new Object[]{userInfo.getComId(), customerId});
				//删除日志表
				crmDao.delByField("customerLog", new String[]{"comId","customerId"}, 
						new Object[]{userInfo.getComId(), customerId});
				//删除客户查看记录
				crmDao.delByField("customerRead", new String[]{"comId","customerId"}, 
						new Object[]{userInfo.getComId(), customerId});
				//删除客户最新动态(已没有只用)
				crmDao.delByField("customerNewtestInfo", new String[]{"comId","customerId"}, 
						new Object[]{userInfo.getComId(), customerId});
				//删除浏览记录
				crmDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, 
						new Object[]{userInfo.getComId(), customerId,ConstantInterface.TYPE_CRM});
				//删除最新动态
				crmDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, 
						new Object[]{userInfo.getComId(), customerId,ConstantInterface.TYPE_CRM});
				//删除消息提醒
				crmDao.delByField("todayWorks", new String[]{"comId","busId","busType"}, 
						new Object[]{userInfo.getComId(), customerId,ConstantInterface.TYPE_CRM});
				//删除客户信息
				crmDao.delById(Customer.class, customerId);
			}
			
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), crmId);
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null,crmId,"合并客户"+log.substring(0,log.length()-1)+"信息", 
			//		BusinessTypeConstant.type_crm, shares,null);
			//添加日志
			this.addCustomerLog(userInfo.getComId(), crmId, userInfo.getId(), "合并客户"+log.substring(0,log.length()-1)+"信息");
			//客户索引库更新
//			this.updateCustomerIndex(crmId, userInfo,"update");
		}
		
	}
	/**
	 * 客户联系人姓名更新
	 * @param linkMan
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateLinkManName(LinkMan linkMan,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			crmDao.update("update linkMan a set a.linkManName=:linkManName where a.comid=:comId and a.customerId=:customerId and a.id=:id",linkMan);
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), linkMan.getCustomerId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, linkMan.getCustomerId(),"客户联系人名称更新:\""+linkMan.getLinkManName()+"\"", BusinessTypeConstant.type_crm, shares,null);
			//重置阅读人员
			this.delCrmRead(linkMan.getCustomerId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(linkMan.getCustomerId(), userInfo);
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),linkMan.getCustomerId(),userInfo.getId(),"客户联系人名称更新:\""+linkMan.getLinkManName()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(linkMan.getCustomerId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户联系人职务更新
	 * @param linkMan
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateLinkManPost(LinkMan linkMan,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			crmDao.update("update linkMan a set a.post=:post where a.comid=:comId and a.customerId=:customerId and a.id=:id",linkMan);
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), linkMan.getCustomerId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, linkMan.getCustomerId(),"客户联系人职务更新为:\""+linkMan.getPost()+"\"", BusinessTypeConstant.type_crm, shares,null);
			//重置阅读人员
			this.delCrmRead(linkMan.getCustomerId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(linkMan.getCustomerId(), userInfo);
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),linkMan.getCustomerId(),userInfo.getId(),"客户联系人职务更新为:\""+linkMan.getPost()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(linkMan.getCustomerId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户联系人移动电话更新
	 * @param linkMan
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateLinkManMovePhone(LinkMan linkMan,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			crmDao.update("update linkMan a set a.movePhone=:movePhone where a.comid=:comId and a.customerId=:customerId and a.id=:id",linkMan);
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), linkMan.getCustomerId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, linkMan.getCustomerId(),"客户联系人移动电话更新为:\""+linkMan.getMovePhone()+"\"", BusinessTypeConstant.type_crm, shares,null);
			//重置阅读人员
			this.delCrmRead(linkMan.getCustomerId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(linkMan.getCustomerId(), userInfo);
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),linkMan.getCustomerId(),userInfo.getId(),"客户联系人移动电话更新为:\""+linkMan.getMovePhone()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(linkMan.getCustomerId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户联系人电子邮件更新
	 * @param linkMan
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateLinkManEmail(LinkMan linkMan,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			crmDao.update("update linkMan a set a.email=:email where a.comid=:comId and a.customerId=:customerId and a.id=:id",linkMan);
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), linkMan.getCustomerId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, linkMan.getCustomerId(),"客户联系人电子邮件更新为:\""+linkMan.getEmail()+"\"", BusinessTypeConstant.type_crm, shares,null);
			//重置阅读人员
			this.delCrmRead(linkMan.getCustomerId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(linkMan.getCustomerId(), userInfo);
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),linkMan.getCustomerId(),userInfo.getId(),"客户联系人电子邮件更新为:\""+linkMan.getEmail()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(linkMan.getCustomerId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户联系人微信更新
	 * @param linkMan
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateLinkManWechat(LinkMan linkMan,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			crmDao.update("update linkMan a set a.wechat=:wechat where a.comid=:comId and a.customerId=:customerId and a.id=:id",linkMan);
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), linkMan.getCustomerId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, linkMan.getCustomerId(),"客户联系人微信更新为:\""+linkMan.getWechat()+"\"", BusinessTypeConstant.type_crm, shares,null);
			//重置阅读人员
			this.delCrmRead(linkMan.getCustomerId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(linkMan.getCustomerId(), userInfo);
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),linkMan.getCustomerId(),userInfo.getId(),"客户联系人微信更新为:\""+linkMan.getWechat()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(linkMan.getCustomerId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户联系人qq更新
	 * @param linkMan
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateLinkManQq(LinkMan linkMan,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			crmDao.update("update linkMan a set a.qq=:qq where a.comid=:comId and a.customerId=:customerId and a.id=:id",linkMan);
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), linkMan.getCustomerId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, linkMan.getCustomerId(),"客户联系人qq更新为:\""+linkMan.getQq()+"\"", BusinessTypeConstant.type_crm, shares,null);
			//重置阅读人员
			this.delCrmRead(linkMan.getCustomerId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(linkMan.getCustomerId(), userInfo);
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),linkMan.getCustomerId(),userInfo.getId(),"客户联系人qq更新为:\""+linkMan.getQq()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(linkMan.getCustomerId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/**
	 * 客户联系人座机更新
	 * @param linkMan
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateLinkManLinePhone(LinkMan linkMan,UserInfo userInfo) throws Exception{
		boolean succ = true;
		try {
			crmDao.update("update linkMan a set a.linePhone=:linePhone where a.comid=:comId and a.customerId=:customerId and a.id=:id",linkMan);
			//客户的所有查看人
			//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), linkMan.getCustomerId());
			//添加待办提醒通知
			//todayWorksService.updateTodayWorks(userInfo,null, linkMan.getCustomerId(),"客户联系人座机更新为:\""+linkMan.getLinePhone()+"\"", BusinessTypeConstant.type_crm, shares,null);
			//重置阅读人员
			this.delCrmRead(linkMan.getCustomerId(), userInfo);
			//把当前操作人添加到客户查看记录表中
			this.addCrmRead(linkMan.getCustomerId(), userInfo);
			//模块日志添加
			this.addCustomerLog(userInfo.getComId(),linkMan.getCustomerId(),userInfo.getId(),"客户联系人座机更新为:\""+linkMan.getLinePhone()+"\"");
			//更新客户索引信息
//			this.updateCustomerIndex(linkMan.getCustomerId(), userInfo,"update");
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	/***
	 * 客户联系人添加
	 * @param linkMan
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public Integer addLinkMan(LinkMan linkMan,UserInfo userInfo) throws Exception{
		// 2018.6.8 Integer linkManId = crmDao.add(linkMan);
		OutLinkMan olm = new OutLinkMan();
		olm.setComId(linkMan.getComId());
		olm.setCreator(userInfo.getId());
		olm.setEmail(linkMan.getEmail());
		olm.setPubState(1);
		olm.setLinePhone(linkMan.getLinePhone());
		olm.setLinkManName(linkMan.getLinkManName());
		olm.setMovePhone(linkMan.getMovePhone());
		olm.setPost(linkMan.getPost());
		olm.setQq(linkMan.getQq());
		olm.setWechat(linkMan.getWechat());
		//添加外部联系人记录
		Integer olmId = crmDao.add(olm);
		
		OlmAndCus oac = new OlmAndCus();
		oac.setComId(linkMan.getComId());
		oac.setOutLinkManId(olmId);
		oac.setCustomerId(linkMan.getCustomerId());
		//添加客户、联系人关系记录
		crmDao.add(oac);
		
		//客户的所有查看人
		//List<UserInfo> shares = crmDao.listCrmOwnersNoForce(userInfo.getComId(), linkMan.getCustomerId());
		//添加待办提醒通知
		//todayWorksService.updateTodayWorks(userInfo,null, linkMan.getCustomerId(),"新增客户联系人:\""+linkMan.getLinkManName()+"\"", BusinessTypeConstant.type_crm, shares,null);
		//模块日志添加
		this.addCustomerLog(userInfo.getComId(),linkMan.getCustomerId(),userInfo.getId(),"新增客户联系人:\""+linkMan.getLinkManName()+"\"");
		//更新客户索引信息
//		this.updateCustomerIndex(linkMan.getCustomerId(), userInfo,"update");
		return olmId;
	}
	/**
	 * 取得模块统计数据
	 * @param customer 客户查询条件对象
	 * @param operator 当前操作人员
	 * @param isForceInPersion 是否为强制参与人
	 * @return
	 */
	public List<ModStaticVo> listCrmStatis(Customer customer,UserInfo sessionUser) {
		//验证当前登录人是否是督察人员
		boolean isForceInPersion = forceInService.isForceInPersion(sessionUser, ConstantInterface.TYPE_CRM);

		List<ModStaticVo> list = crmDao.listCrmStatis(customer,sessionUser.getId(),isForceInPersion);
		return list;
	}
	/**
	 * 获取团队客户主键集合
	 * @param userinfo
	 * @return
	 */
	public List<Customer> listCrmOfAll(UserInfo userinfo){
		return crmDao.listCrmOfAll(userinfo);
	}
	/**
	 * 取得执行人员的客户数 app
	 * @param userId 指定人员主键
	 * @param comId 企业号
	 * @return
	 */
	public Integer countMyCrm(Integer userId, Integer comId) {
		
		return crmDao.countMyCrm(userId,comId);
	}
	/**
	 * 统计客户类型
	 * @param customer 查询的客户信息
	 * @param userInfo 当前操作员
	 * @param isForceIn 是否为强制参与人
	 * @return
	 */
	public List<ModStaticVo> listCrmStatisByType(Customer customer,
			UserInfo userInfo, boolean isForceIn) {
		
		return crmDao.listCrmStatisByType(customer,userInfo,isForceIn);
	}
	/**
	 * 统计客户增量
	 * @param customer 查询的客户信息
	 * @param userInfo 当前操作员
	 * @param isForceIn 是否为强制参与人
	 * @return
	 */
	public List<CustomerType> listCrmAddNumStatis(Customer customer,
			UserInfo userInfo, boolean isForceIn) {
		List<CustomerType> result = new ArrayList<CustomerType>();
		// 获取客户类型数据源
		List<CustomerType> listCustomerType = crmDao.listCustomerType(userInfo.getComId());
		if(null!=listCustomerType && !listCustomerType.isEmpty()){
			for (CustomerType customerType : listCustomerType) {
				customer.setCustomerTypeId(customerType.getId());
				//列出客户负责人各种分类的统计
				List<ModStaticVo> list = crmDao.listCrmAddNumStatis(customer, userInfo, isForceIn);
				
				customerType.setListModStaticVos(list);
				result.add(customerType);
			}
		}
		return result;
	}
	/**
	 * 统计时需要排除总数为0的人员
	 * @param customer 客户查询条件
	 * @param sessionUser 当前操作人员
	 * @param isForceInPersion 是否为强制参与人
	 * @return
	 */
	public List<ModStaticVo> listOwnerCrmNOStatistic(Customer customer, UserInfo userInfo,Boolean isForceInPersion) {
		return crmDao.listOwnerCrmNOStatistic(customer,userInfo,isForceInPersion);
	}
	/**
	 * 列出客户负责人的各种客户分类统计
	 * @param customer 客户的查询条件
	 * @param userIds 用于查询排序的用户主键顺序
	 * @param userInfo 当前操作人
	 * @param isForceIn 是否为监督人员
	 * @return 
	 */
	public List<CustomerType> listOwnerCrmTypeStatistic(Customer customer, UserInfo userInfo,Boolean isForceIn) {
		List<CustomerType> result = new ArrayList<CustomerType>();
		// 获取客户类型数据源
		List<CustomerType> listCustomerType = crmDao.listCustomerType(userInfo.getComId());
		if(null!=listCustomerType && !listCustomerType.isEmpty()){
			
			//列出客户负责人各种分类的统计
			List<ModStaticVo> listNoPerson = crmDao.listOwnerCrmNOStatistic(customer, userInfo,isForceIn);
			List<Integer> userIds = new ArrayList<Integer>();
			if(null!=listNoPerson && !listNoPerson.isEmpty()){
				for (ModStaticVo modStaticVo : listNoPerson) {
					userIds.add(Integer.parseInt(modStaticVo.getType()));
				}
			}
			for (CustomerType customerType : listCustomerType) {
				customer.setCustomerTypeId(customerType.getId());
				//列出客户负责人各种分类的统计
				List<ModStaticVo> list = crmDao.listOwnerCrmTypeStatistic(customer,userIds, userInfo,isForceIn);
				
				customerType.setListModStaticVos(list);
				result.add(customerType);
			}
		}
		return result;
	}
	/**
	 * 列出客户负责人的各种客户阶段统计
	 * @param customer 客户的查询条件
	 * @param userIds 用于查询排序的用户主键顺序
	 * @param userInfo 当前操作人
	 * @param isForceIn 是否为监督人员
	 * @return
	 */
	public List<CustomerStage> listCrmStageStatistic(Customer customer, UserInfo userInfo,Boolean isForceIn){
		List<CustomerStage> result =  new ArrayList<CustomerStage>();
		// 获取客户阶段数据源
		List<CustomerStage> listCrmStage = crmDao.listCustomerStage(userInfo.getComId());
		if(null != listCrmStage && !listCrmStage.isEmpty()){
			for(CustomerStage stage :listCrmStage){
				customer.setStage(stage.getId());
				List<ModStaticVo> list = crmDao.listCrmStageStatistic(customer,userInfo,isForceIn);
				stage.setListModStaticVos(list);
				result.add(stage);
			}
		}
		return result;
	}
	
	/**
	 * 统计客户资金预算
	 * @param customer
	 * @param sessionUser
	 * @param isForceIn
	 * @return
	 */
	public List<ModStaticVo> listCrmBudgetStatistic(Customer customer,
			UserInfo sessionUser, Boolean isForceIn) {
		List<ModStaticVo> listBudeget = crmDao.listCrmBudgetStatistic(customer, sessionUser, isForceIn);
		if(null!=listBudeget && !listBudeget.isEmpty()){
			for(ModStaticVo m :listBudeget){
				customer.setStage(Integer.valueOf(m.getType()));
				List<ModStaticVo> listStageCrmNum = crmDao.listStageCrmNum(customer, sessionUser, isForceIn);
				m.setChildModStaticVo(listStageCrmNum);
			}
		}
		return listBudeget;
	}
	/**
	 * 统计客户更新频率
	 * @param customer 客户的查询条件
	 * @param sessionUser 当前操作人员
	 * @param isForceIn 是否为监督人员
	 * @param frequenceType 更新频率类型
	 * @return
	 */
	public List<ModStaticVo> listCrmFrequenStatistic(Customer customer,
			UserInfo sessionUser, Boolean isForceIn) {
		return crmDao.listCrmFrequenStatistic(customer,sessionUser,isForceIn);
	}
	/**
	 * 取得全国区域模板用于统计
	 * @param userInfo
	 * @param customer 
	 * @param isForceIn 
	 * @return
	 */
	public List<ModStaticVo> listRegionForStatistic(UserInfo userInfo, Customer customer, boolean isForceIn) {
		
		List<ModStaticVo> listModStatistic = new ArrayList<ModStaticVo>();
		List<Region> list = crmDao.listRegionForStatistic(userInfo);
		if(null != list && !list.isEmpty()){
			//区域主键集合
			List<Integer> regionsIds = new ArrayList<Integer>();
			//区域主键对应区域实体类
			Map<Integer,Region> regionIdMap = new HashMap<Integer, Region>();
			
			for (Region region : list) {
				regionsIds.add(region.getId());
				regionIdMap.put(region.getId(), region);
			}
			//查询区域主键信息
			List<Area> areas = crmDao.listRegionAreaCrmCount(userInfo,regionsIds,customer,isForceIn);
			if(null!=areas && !areas.isEmpty()){
				//省份主键对应省份的区市等
				Map<Integer,List<Area>> rootAreaCitys = new HashMap<Integer, List<Area>>();
				//区域对应的省份主键
				Map<Integer,Integer> areaIdRootId = new HashMap<Integer, Integer>();
				//省份的统计总数
				Map<Integer,Integer> rootAreaIdCount = new HashMap<Integer, Integer>();
				//1、第一步，区域按省份分类
				for (Area area : areas) {
					//区域等级，若为1，则是根
					Integer level = area.getLevel();
					//区域主键
					Integer areaId = area.getId();
					//父类主键
					Integer areaParentId = area.getParentId();
					//区域客户数
					Integer crmSum = area.getCrmSum();
					//是根
					if(level == 1){
						List<Area> rootArea = new ArrayList<Area>();
						//添加省份
						rootArea.add(area);
						//记录根的所有城市集合
						rootAreaCitys.put(areaId, rootArea);
						//记录区域主键对应的省份
						areaIdRootId.put(areaId, areaId);
						//记录省份对应的客户总数
						rootAreaIdCount.put(areaId, crmSum);
					}else{
						//根区域主键
						Integer rootAreaId = areaIdRootId.get(areaParentId);
						areaIdRootId.put(areaId, rootAreaId);
						
						//记录省份对应的客户总数
						crmSum = rootAreaIdCount.get(rootAreaId)+crmSum;
						rootAreaIdCount.put(rootAreaId, crmSum);
						
						//记录根的所有城市集合
						List<Area> rootArea = rootAreaCitys.get(rootAreaId);
						rootArea.add(area);
						rootAreaCitys.put(rootAreaId, rootArea);
					}
				}
				//开始统计
				if(null!= rootAreaCitys && !rootAreaCitys.isEmpty()){
					for (Entry<Integer, List<Area>> entry : rootAreaCitys.entrySet()) {
						//取得单个省份的市区集合
						List<Area> listAreas = entry.getValue();
						
						//取得省份的区域信息
						Area rootArea = listAreas.get(0);
						//省份关联的区域主键
						Integer regionId = rootArea.getRegionId();
						//省份的区域主键
						Integer rootAreaId = rootArea.getId();
						//省通区域主键转同理类型
						String rootType = rootAreaId.toString();
						
						//省份的客户总数
						Integer count = rootAreaIdCount.get(rootAreaId);
						
						Region region = regionIdMap.get(regionId);
						ModStaticVo modStaticVo = new ModStaticVo();
						modStaticVo.setName(region.getRegionName());
						modStaticVo.setValue(count);
						modStaticVo.setType(rootType);
						
						List<ModStaticVo> childModStaticVo = this.statisticChild(listAreas);
						modStaticVo.setChildModStaticVo(childModStaticVo);	
						
						listModStatistic.add(modStaticVo);
						
					}
				}
				
				
			}
		}
		return listModStatistic;
	}
	/**
	 * 统计各个省份的区域数据
	 * @param listAreas 省份和总区域
	 * @return
	 */
	private List<ModStaticVo> statisticChild(List<Area> listAreas) {
		
		List<ModStaticVo> list = new ArrayList<ModStaticVo>();
		//子区域主键对应客户总数
		Map<Integer,Integer> rootAreaIdCount = new HashMap<Integer, Integer>();
		//二级根区域对应的区域实体
		Map<Integer, Area> childRootAreaMap = new HashMap<Integer, Area>();
		//区域对应的省份主键
		Map<Integer,Integer> areaIdRootId = new HashMap<Integer, Integer>();
		//直辖市
		Map<Integer,Integer> removeProvince = new HashMap<Integer, Integer>();
		//直辖区域
		Map<Integer,Integer> removeCity = new HashMap<Integer, Integer>();
		
		//去掉省份
		Area areaP = listAreas.remove(0);
		//是北京市或是北京市的子区域 
		if(areaP.getAreaName().startsWith("北京")
				|| areaP.getAreaName().startsWith("重庆")
				|| areaP.getAreaName().startsWith("上海")
				|| areaP.getAreaName().startsWith("天津")){
			removeProvince.put(areaP.getId(), areaP.getId());
		}
		for (int i = 0;i<listAreas.size();i++) {
			Area area  = listAreas.get(i);
			//取得等级数
			Integer level = area.getLevel();
			//区域客户数
			Integer crmSum = area.getCrmSum();
			//区域主键
			Integer areaId = area.getId();
			//父类主键
			Integer areaParentId = area.getParentId();
			
			if(removeProvince.containsKey(areaParentId) && level<=3){
				removeProvince.put(areaId, areaId);
				//等级为2，是新的区域
				if(level==3){
					areaIdRootId.put(areaId, areaId);
					childRootAreaMap.put(areaId, area);
					
					rootAreaIdCount.put(areaId, crmSum);
				}
			}else{
				//等级为2，是新的区域
				if(level==2){
					if(area.getAreaName().startsWith("自治区直辖县级行政区划") || 
							area.getAreaName().startsWith("省直辖县级行政区划")){
						removeCity.put(areaId, areaId);
					}else{
						areaIdRootId.put(areaId, areaId);
						
						childRootAreaMap.put(areaId, area);
						
						rootAreaIdCount.put(areaId, crmSum);
					}
				}else{
					if(removeCity.containsKey(areaParentId) && level==3){
						areaIdRootId.put(areaId, areaId);
						
						childRootAreaMap.put(areaId, area);
						
						rootAreaIdCount.put(areaId, crmSum);
					}else{
						//根区域主键
						Integer rootAreaId = areaIdRootId.get(areaParentId);
						areaIdRootId.put(areaId, rootAreaId);
						
						//记录省份对应的客户总数
						crmSum = rootAreaIdCount.get(rootAreaId)+crmSum;
						rootAreaIdCount.put(rootAreaId, crmSum);
					}
				}
			}
		}
		for (Entry<Integer, Area> entry : childRootAreaMap.entrySet()) {
			
			Area area = entry.getValue();
			ModStaticVo modStaticVo = new ModStaticVo();
			String name = area.getAreaName();
			if(name.endsWith("区")){
				if(name.startsWith("海东地区")){
					name = "海东市";
				}else if(name.startsWith("昌都地区")){
					name = "昌都市";
				}else if(name.startsWith("林芝地区")){
					name = "林芝市";
				}else if(name.startsWith("日喀则地区")){
					name = "日喀则市";
				}else if(name.startsWith("毕节地区")){
					name = "毕节市";
				}else if(name.startsWith("铜仁地区")){
					name = "铜仁市";
				}
			}else if(name.equals("襄樊市")){
				name = "襄阳市";
				
			}
			
			Integer areaId = area.getId();
			Integer count = rootAreaIdCount.get(area.getId());
			modStaticVo.setName(name);
			modStaticVo.setValue(count);
			modStaticVo.setType(areaId.toString());
			
			list.add(modStaticVo);
		}
		return list;
	}
	/**
	 * 统计下属负责客户数量
	 * @param userInfo 
	 * @param customer 客户
	 * @return
	 */
	public List<UserInfo> listSubCrmNum(UserInfo userInfo,Customer customer){
		return crmDao.listSubCrmNum(userInfo, customer);
	}
	/********************************客户阶段维护***************************************/
	/**
	 * 客户阶段列表
	 * @param comId
	 * @return
	 */
	public List<CustomerStage> listCustomerStage(Integer comId) {
		return crmDao.listCustomerStage( comId);
	}
	/**
	 * 添加客户阶段
	 * @param crmStage
	 * @param userInfo
	 * @return
	 */
	public Integer addCrmStage(CustomerStage crmStage, UserInfo userInfo) {
		Integer id = crmDao.add(crmStage);
		//添加系统日志记录 
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), 
				"添加客户阶段:\""+crmStage.getStageName()+"\"",ConstantInterface.TYPE_CRM,
				userInfo.getComId(),userInfo.getOptIP());
		return id;
	}
	/**
	 * 客户阶段最大序号
	 * @param comId
	 * @return
	 */
	public Integer queryCrmStageOrderMax(Integer comId) {
		CustomerStage crmStage = crmDao.queryCrmStageOrderMax(comId);
		return null==crmStage.getOrderNum()?1:crmStage.getOrderNum();
	}
	/**
	 *  更新客户阶段名称
	 * @param crmStage
	 * @param userInfo
	 * @return
	 */
	public boolean updateStageName(CustomerStage crmStage, UserInfo userInfo) {
		boolean succ = true;
		try {
			CustomerStage obj = (CustomerStage) crmDao.objectQuery(CustomerStage.class, crmStage.getId());
			//有变动
			if(null!=obj && !obj.getStageName().equals(crmStage.getStageName())){
				//更新客户反馈类型名称
				crmDao.update(crmStage);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), 
						"更新客户阶段名称:\""+crmStage.getStageName()+"\"",ConstantInterface.TYPE_CRM,
						userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
			log.error(e);
		}
		return succ;
	}
	/**
	 * 更新客户阶段排序
	 * @param crmStage
	 * @param userInfo
	 * @return
	 */
	public boolean updateStageOrder(CustomerStage crmStage, UserInfo userInfo) {
		boolean succ = true;
		try {
			CustomerStage obj = (CustomerStage) crmDao.objectQuery(CustomerStage.class, crmStage.getId());
			//有变动
			if(null!=obj && !obj.getOrderNum().equals(crmStage.getOrderNum())){
				//更新客户类型排序
				crmDao.update(crmStage);
				//添加系统日志记录 
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), 
						"更新客户阶段:\""+obj.getStageName()+"\"的排序为\""+crmStage.getOrderNum()+"\"",
						ConstantInterface.TYPE_CRM,userInfo.getComId(),userInfo.getOptIP());
			}
		} catch (Exception e) {
			succ = false ;
			log.error(e);
		}
		return succ;
	}
	/**
	 *  删除客户阶段类型
	 * @param crmStage
	 * @param userInfo
	 * @return
	 */
	public boolean delCustomerStage(CustomerStage crmStage, UserInfo userInfo) {
		boolean succ = true;
		try {
			Integer crmNum = crmDao.countCustomerByStage(crmStage.getId(),userInfo);
			if(crmNum>0){
				succ = false ;
			}else{
				//获取需要删除的客户反馈类型
				CustomerStage obj = (CustomerStage) crmDao.objectQuery(CustomerStage.class, crmStage.getId());
				if(null!= obj){
					//客户反馈类型删除
					crmDao.delById(CustomerStage.class, crmStage.getId());
					//添加系统日志记录 
					systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), 
							"删除客户阶段:\""+obj.getStageName()+"\"",ConstantInterface.TYPE_CRM,
							userInfo.getComId(),userInfo.getOptIP());
				}
			}
			
		} catch (Exception e) {
			succ = false ;
		}
		return succ;
	}
	/**
	 * 取得模块统计数据
	 * @param customer 客户查询条件对象
	 * @param operator 当前操作人员
	 * @param isForceInPersion 是否为强制参与人
	 * @return
	 */
	public List<ModStaticVo> listCrmStatisByBudget(Customer customer, Integer operator, boolean isForceIn) {
		return crmDao.listCrmStatisByBudget(customer,operator,isForceIn);
	}
	/**
	 * 查询客户列表导出数据
	 * @param operator 当前操作人
	 * @param isForceIn
	 * @param customer
	 * @return
	 */
	public List<Customer> listCustomerForExport(Integer operator, boolean isForceIn,Customer customer) {
		return crmDao.listCustomerForExport(operator,isForceIn,customer);
	}
	
	/**
	 * 查询客户催办信息
	 * @param userInfo
	 * @param crmId
	 * @param busType
	 * @return
	 */
	public Map<String, Object> queryRemindConf(UserInfo userInfo,Integer crmId) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		Customer crm = crmDao.quereCrmForOverTime(userInfo,crmId);
		map.put("busModName", crm.getCustomerName());
		String modifyTime = crm.getModifyTime();
		if(null== modifyTime){
			modifyTime = crm.getRecordCreateTime();
		}
		//最近更新时间
		Long modifyTimes = DateTimeUtil.parseDate(
				modifyTime, DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
		//当前时间
		Long nowDateTimes = Calendar.getInstance().getTime().getTime();
		//时间差
		Long leftTimes = nowDateTimes - modifyTimes;
		
		Long oneDay =  24L * 60 * 60 * 1000;
		Long day = leftTimes/oneDay;
		if(day <= 0){
			day = 1L;
		}
		map.put("status", "y");
		String defMsg = "[客户]“"+crm.getCustomerName()+"”已"+day+"天未跟进，请及时跟进反馈！";
		map.put("defMsg", defMsg);
		
		//事项的执行人员信息
		List<BusRemindUser> listReminderUser = this.listCrmRemindExecutor(userInfo,crmId);
		map.put("listRemindUser", listReminderUser);
		
		return map;
	}
	
	/**
	 * 查询客户的负责人信息
	 * @param userInfo 当前操作人员
	 * @param crmId 客户主键
	 * @return
	 */
	private List<BusRemindUser> listCrmRemindExecutor(UserInfo userInfo,
			Integer crmId) {
		return crmDao.listCrmRemindExecutor(userInfo,crmId);
	}
	/*****************************任务统计*****************************/
	/**
	 * 用于客户统计的数据
	 * @param userInfo
	 * @param statisticCrmVo
	 * @return
	 */
	public PageBean<StatisticCrmVo> listPagedStatisticCrm(UserInfo userInfo,
			StatisticCrmVo statisticCrmVo) {
		List<StatisticCrmVo> recordList = crmDao.listPagedStatisticCrm(userInfo,statisticCrmVo);
		PageBean<StatisticCrmVo> pageBean = new PageBean<StatisticCrmVo>();
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	
	/**
	 * 根据type删除对应的附件表记录
	 * @param crmUpFileId
	 * @param type
	 * @param userInfo
	 * @param crmId
	 */
	public void delCrmUpfile(Integer crmUpFileId, String type, UserInfo userInfo, Integer crmId) {
		if(type.equals(ConstantInterface.TYPE_ITEMTALK)){
			ItemTalkFile itemTalkFile = (ItemTalkFile) crmDao.objectQuery(ItemTalkFile.class, crmUpFileId);
			crmDao.delById(ItemTalkFile.class, crmUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) crmDao.objectQuery(Upfiles.class, itemTalkFile.getUpfileId());
			itemService.addItemLog(userInfo.getComId(),itemTalkFile.getItemId(),userInfo.getId(),userInfo.getUserName(),"删除了项目留言附件："+upfiles.getFilename());
			
		}else if(type.equals(ConstantInterface.TYPE_ITEM)){
			ItemUpfile itemUpfile = (ItemUpfile) crmDao.objectQuery(ItemUpfile.class, crmUpFileId);
			crmDao.delById(ItemUpfile.class, crmUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) crmDao.objectQuery(Upfiles.class, itemUpfile.getUpfileId());
			itemService.addItemLog(userInfo.getComId(),itemUpfile.getItemId(),userInfo.getId(),userInfo.getUserName(),"删除了项目附件："+upfiles.getFilename());
		}else if(type.equals(ConstantInterface.TYPE_ITEMSTAGE)) {
			StagedInfo stagedInfo = (StagedInfo) crmDao.objectQuery(StagedInfo.class, crmUpFileId);
			crmDao.delById(StagedInfo.class, crmUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) crmDao.objectQuery(Upfiles.class, stagedInfo.getModuleId());
			itemService.addItemLog(userInfo.getComId(),stagedInfo.getItemId(),userInfo.getId(),userInfo.getUserName(),"删除了项目阶段管理附件："+upfiles.getFilename());
		}else if(type.equals(ConstantInterface.TYPE_TASK)){
			TaskUpfile taskUpfile = (TaskUpfile) crmDao.objectQuery(TaskUpfile.class, crmUpFileId);
			crmDao.delById(TaskUpfile.class, crmUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) crmDao.objectQuery(Upfiles.class, taskUpfile.getUpfileId());
			taskService.addTaskLog(userInfo.getComId(),taskUpfile.getTaskId(),userInfo.getId(),userInfo.getUserName(),"删除了任务附件："+upfiles.getFilename());
		}else if(type.equals(ConstantInterface.TYPE_TASKTALK)){
			TaskTalkUpfile taskTalkUpfile = (TaskTalkUpfile) crmDao.objectQuery(TaskTalkUpfile.class, crmUpFileId);
			crmDao.delById(TaskTalkUpfile.class, crmUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) crmDao.objectQuery(Upfiles.class, taskTalkUpfile.getUpfileId());
			taskService.addTaskLog(userInfo.getComId(),taskTalkUpfile.getTaskId(),userInfo.getId(),userInfo.getUserName(),"删除了任务留言附件："+upfiles.getFilename());
		}else if(type.equals(ConstantInterface.TYPE_CRM)){
			CustomerUpfile customerUpfile = (CustomerUpfile) crmDao.objectQuery(CustomerUpfile.class, crmUpFileId);
			crmDao.delById(CustomerUpfile.class, crmUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) crmDao.objectQuery(Upfiles.class, customerUpfile.getUpfileId());
			this.addCustomerLog(userInfo.getComId(),crmId,userInfo.getId(),"删除了客户附件："+upfiles.getFilename());
		}else if(type.equals(ConstantInterface.TYPE_CRMTALK)){
			FeedInfoFile feedInfoFile = (FeedInfoFile) crmDao.objectQuery(FeedInfoFile.class, crmUpFileId);
			crmDao.delById(FeedInfoFile.class, crmUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) crmDao.objectQuery(Upfiles.class, feedInfoFile.getUpfileId());
			this.addCustomerLog(userInfo.getComId(),crmId,userInfo.getId(),"删除了客户留言附件："+upfiles.getFilename());
		}
	}
	/**
	 * 判断移除的人员是否仍有查看权限
	 * @param id
	 * @param comId
	 * @param userId
	 * @param owner
	 * @return
	 */
	public Integer countUserNum(Integer id, Integer comId, Integer userId, Integer owner) {
		return crmDao.countUserNum(id, comId, userId, owner);
	}
	/**
	 * 验证当前操作人是不是分享人，不是则把操作人存入客户参与人中
	 * @param comId
	 * @param id
	 * @param userId
	 * @return
	 */
	public CustomerSharer getCustomerSharer(Integer comId, Integer id, Integer userId) {
		return crmDao.getCustomerSharer(comId, id, userId);
	}
	
}
