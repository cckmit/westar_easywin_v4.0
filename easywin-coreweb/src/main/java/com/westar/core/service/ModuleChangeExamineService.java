package com.westar.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Customer;
import com.westar.base.model.CustomerHandOver;
import com.westar.base.model.CustomerSharer;
import com.westar.base.model.CustomerStage;
import com.westar.base.model.CustomerType;
import com.westar.base.model.FeedBackInfo;
import com.westar.base.model.ModAdmin;
import com.westar.base.model.ModuleChangeApply;
import com.westar.base.model.ModuleChangeExamine;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.JpushUtils;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.ModuleChangeExamineDao;
import com.westar.core.thread.sendPhoneMsgThread;

@Service
public class ModuleChangeExamineService {

	@Autowired
	ModuleChangeExamineDao moduleChangeExamineDao;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	PhoneMsgService phoneMsgService;
	
	@Autowired
	CrmService crmService;
	
	@Autowired
	ModAdminService modAdminService;
	
	@Autowired
	BusUpdateService busUpdateService;
	
	@Autowired
	ClockService clockService;

	public boolean updateExamConfig(ModuleChangeExamine moduleChangeExamine, UserInfo userInfo) {
		boolean succ = true;
		try {
			String moduleType = moduleChangeExamine.getModuleType();
			//先删除本模块原来记录
			moduleChangeExamineDao.delByField("moduleChangeExamine", new String[]{"comId","moduleType"}, new Object[]{userInfo.getComId(),moduleType});
			if(null!=moduleChangeExamine.getFields()){
				//只记录启用权限
				for(String field:moduleChangeExamine.getFields()){
					moduleChangeExamine.setComId(userInfo.getComId());
					moduleChangeExamine.setField(field);
					moduleChangeExamine.setModuleType(moduleChangeExamine.getModuleType());
					moduleChangeExamine.setEnabled(1);
					moduleChangeExamineDao.add(moduleChangeExamine);
				}
			}
			String logContent = "";
			if(moduleType.equals(ConstantInterface.TYPE_CRM)){
				logContent = "更新了客户中心属性变更审批配置";
			}
			//添加系统日志记录 
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),logContent,moduleType,
					userInfo.getComId(),userInfo.getOptIP());
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 根据业务类型查询属性变更审批配置
	 * @param moduleChangeExamine
	 * @param userInfo
	 * @return
	 */
	public List<ModuleChangeExamine> listModChangeExam(ModuleChangeExamine moduleChangeExamine, UserInfo userInfo) {
		return moduleChangeExamineDao.listModChangeExam(moduleChangeExamine,userInfo);
	}
	
	/**
	 * 添加变更申请
	 * @param moduleChangeApply
	 * @param feedBackTypeId
	 * @param userInfo 
	 */
	public void addApply(ModuleChangeApply moduleChangeApply, Integer feedBackTypeId, UserInfo userInfo) {
		//把移交说明添加到客户维护记录当中
		if(ConstantInterface.TYPE_CRM.equals(moduleChangeApply.getModuleType()) && !CommonUtil.isNull(feedBackTypeId)){
			FeedBackInfo feedBackInfo = new FeedBackInfo();
			feedBackInfo.setComId(userInfo.getComId());
			feedBackInfo.setContent(moduleChangeApply.getContent());
			feedBackInfo.setCustomerId(moduleChangeApply.getBusId());
			feedBackInfo.setFeedBackTypeId(feedBackTypeId);
			feedBackInfo.setParentId(-1);
			feedBackInfo.setUserId(userInfo.getId());
			moduleChangeExamineDao.add(feedBackInfo);
		}
		
		//添加变更申请
		moduleChangeApply.setComId(userInfo.getComId());
		moduleChangeApply.setCreator(userInfo.getId());
		Integer applyId = moduleChangeExamineDao.add(moduleChangeApply);
		
		String content = "";
		//添加操作记录
		if(ConstantInterface.TYPE_CRM.equals(moduleChangeApply.getModuleType())) {
			
			if(moduleChangeApply.getField().equals("customerTypeId")) {
				CustomerType customerType = crmService.queryCustomerType(userInfo.getComId(), Integer.parseInt(moduleChangeApply.getNewValue()));
				CustomerType oldCustomerType = crmService.queryCustomerType(userInfo.getComId(), Integer.parseInt(moduleChangeApply.getOldValue()));
				content = "申请客户类型由:\""+ oldCustomerType.getTypeName() +"\" 变更为:\"" + customerType.getTypeName()+"\"";
			}else if(moduleChangeApply.getField().equals("owner")) {
				UserInfo user = userInfoService.getUserBaseInfo(userInfo.getComId(),Integer.parseInt(moduleChangeApply.getNewValue()));
				content = "申请客户移交给:\"" + user.getUserName()+"\"";;
			}else if(moduleChangeApply.getField().equals("stage")) {
				CustomerStage customerStage = crmService.queryCustomerStage(Integer.parseInt(moduleChangeApply.getNewValue()));
				CustomerStage oldCustomerStage = crmService.queryCustomerStage( Integer.parseInt(moduleChangeApply.getOldValue()));
				content = "申请客户所属阶段由:\""+ oldCustomerStage.getStageName() +"\" 变更为:\"" + customerStage.getStageName()+"\"";;
			}
			crmService.addCustomerLog(userInfo.getComId(), moduleChangeApply.getBusId(), userInfo.getId(), content);
		}
		
		
		//取得属性变更审批人员
		List<ModAdmin> listExamUser = modAdminService.listModAdmin(userInfo.getComId(),ConstantInterface.TYPE_CHANGE_EXAM);
		if(!CommonUtil.isNull(listExamUser)) {
			//向相应的人发送消息
			List<UserInfo> shares = new ArrayList<UserInfo>();
			for (int i = 0; i < listExamUser.size(); i++) {
				UserInfo proposer = userInfoService.getUserBaseInfo(userInfo.getComId(),
						listExamUser.get(i).getUserId());
				shares.add(proposer);
			}
			
			todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_CHANGE_EXAM,
					applyId, content,shares, userInfo.getId(), 1);
			//需要设置短信
			if("1".equals(moduleChangeApply.getUsePhone()) && !userInfo.getId().equals(applyId)){//发送短信
				//单线程池
				ExecutorService pool = ThreadPoolExecutor.getInstance();
				//跟范围人员发送通知消息
				pool.execute(new sendPhoneMsgThread(phoneMsgService, userInfo.getComId(), shares, 
						new Object[]{content}, ConstantInterface.MSG_JOB_TO_DO,userInfo.getOptIP()));

			}
		}
		
		
	}
	
	/**
	 * 根据Id查看属性变更申请详情
	 * @param busId
	 * @return
	 */
	public ModuleChangeApply queryModChangeApplyById(Integer id) {
		return moduleChangeExamineDao.queryModChangeApplyById(id);
	}
	
	/**
	 * 审批属性变更申请
	 * @param moduleChangeExamine
	 * @param userInfo
	 * @return
	 */
	public boolean updateApply(ModuleChangeApply moduleChangeApply, UserInfo userInfo) {
		if(ConstantInterface.TYPE_CRM.equals(moduleChangeApply.getModuleType())) {
			if("customerTypeId".equals(moduleChangeApply.getField())) {
				if(moduleChangeApply.getStatus() == 1) {
					//重置阅读人员
					crmService.delCrmRead(moduleChangeApply.getBusId(), userInfo);
					//把当前操作人添加到客户查看记录表中
					crmService.addCrmRead(moduleChangeApply.getBusId(), userInfo);
					CustomerType orgObj = crmService.queryCustomerType(userInfo.getComId(), Integer.parseInt(moduleChangeApply.getNewValue()));
					//更新客户所属区域
					Customer customer = new Customer();
					customer.setId(moduleChangeApply.getBusId());
					customer.setComId(userInfo.getComId());
					customer.setCustomerTypeId(Integer.parseInt(moduleChangeApply.getNewValue()));
					moduleChangeExamineDao.update(customer);
					
					//模块日志添加
					crmService.addCustomerLog(userInfo.getComId(),moduleChangeApply.getBusId(),userInfo.getId(),"同意变更客户类型为:\""+orgObj.getTypeName()+"\"");
					// 添加更新客户类型记录
					busUpdateService.addBusUpdate(userInfo, moduleChangeApply.getBusId(),ConstantInterface.TYPE_CRM,
							ConstantInterface.BUSUPDATETYPE_UPDATE, "变更客户类型为:\""+orgObj.getTypeName()+"\"");
				}else {
					CustomerType orgObj = crmService.queryCustomerType(userInfo.getComId(), Integer.parseInt(moduleChangeApply.getNewValue()));
					//模块日志添加
					crmService.addCustomerLog(userInfo.getComId(),moduleChangeApply.getBusId(),userInfo.getId(),"不同意变更客户类型为:\""+orgObj.getTypeName()+"\"");
				}
				
			}else if("owner".equals(moduleChangeApply.getField())) {
				if(moduleChangeApply.getStatus() == 1) {
					Customer customer = new Customer();
					customer.setId(moduleChangeApply.getBusId());
					customer.setComId(userInfo.getComId());
					customer.setOwner(Integer.parseInt(moduleChangeApply.getNewValue()));
					moduleChangeExamineDao.update(customer);
					
					//移交记录添加
					CustomerHandOver customerHandOver = new CustomerHandOver();
					customerHandOver.setComId(userInfo.getComId());
					customerHandOver.setCustomerId(customer.getId());
					customerHandOver.setToUser(customer.getOwner());
					customerHandOver.setFromUser(Integer.parseInt(moduleChangeApply.getOldValue()));
					moduleChangeExamineDao.add(customerHandOver);
					//获取移交对象人员信息
					UserInfo toUser = userInfoService.getUserInfo(userInfo.getComId(),Integer.parseInt(moduleChangeApply.getNewValue()));
					//通知接受人
					List<UserInfo> shares = new ArrayList<UserInfo>();
					shares.add(toUser);
					//添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo,toUser.getId(),customer.getId(),"客户移交给了\""+toUser.getUserName()+"\"", ConstantInterface.TYPE_CRM, shares,null);
					//重置阅读人员
					crmService.delCrmRead(customer.getId(), userInfo);
					//把当前操作人添加到客户查看记录表中
					crmService.addCrmRead(customer.getId(), userInfo);
					
					
					customer = (Customer) moduleChangeExamineDao.objectQuery(Customer.class, customer.getId());
					if(null!=customer){
						if(customer.getPubState().equals(0)) {//当客户类型为私有的时候
							//验证当前操作人是不是分享人，不是则把操作人存入客户参与人中
							CustomerSharer oprator = crmService.getCustomerSharer(userInfo.getComId(),customer.getId(),userInfo.getId());
							if(null == oprator){
								CustomerSharer sharer = new CustomerSharer();
								sharer.setComId(userInfo.getComId());
								sharer.setCustomerId(customer.getId());
								sharer.setUserId(userInfo.getId());
								moduleChangeExamineDao.add(sharer);
							}
							
						}
						
						//客户名称
						String crmName = StringUtil.cutStrFace(customer.getCustomerName(), 23);
						
						//添加工作轨迹
						systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_CRM, 
								customer.getId(), "移交的客户\""+crmName+"\"给了\""+toUser.getUserName()+"\"", "负责\""+userInfo.getUserName()+"\"移交的客户\""+crmName);
						//取得待办事项主键
						TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),toUser.getId(),
								customer.getId(),ConstantInterface.TYPE_CRM,0);
						if(null!=todayWorks){
							JpushUtils.sendTodoMessage(userInfo.getComId(), toUser.getId(), userInfo.getId(),
									todayWorks.getId(), customer.getId(), ConstantInterface.TYPE_CRM,0,"客户:"+crmName);
						}
					}
					//添加客户移交记录
					busUpdateService.addBusUpdate(userInfo, customer.getId(), ConstantInterface.TYPE_CRM,
							ConstantInterface.BUSUPDATETYPE_HANDOVER, "把客户移交给了\""+toUser.getUserName()+"\"");
					
					crmService.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"同意变更客户负责人为:\""+toUser.getUserName()+"\"");
					
				}else {
					UserInfo owner = userInfoService.getUserInfo(userInfo.getComId(), Integer.parseInt(moduleChangeApply.getNewValue()));
					//模块日志添加
					crmService.addCustomerLog(userInfo.getComId(),moduleChangeApply.getBusId(),userInfo.getId(),"不同意变更客户负责人为:\""+owner.getUserName()+"\"");
				}
				
				
			}else if("stage".equals(moduleChangeApply.getField())) {
				if(moduleChangeApply.getStatus() == 1) {
					Customer customer = new Customer();
					customer.setId(moduleChangeApply.getBusId());
					customer.setComId(userInfo.getComId());
					customer.setStage(Integer.parseInt(moduleChangeApply.getNewValue()));
					//重置阅读人员
					crmService.delCrmRead(customer.getId(), userInfo);
					//把当前操作人添加到客户查看记录表中
					crmService.addCrmRead(customer.getId(), userInfo);
					
					CustomerStage orgObj = crmService.queryCustomerStage(customer.getStage());
					//更新客户阶段
					moduleChangeExamineDao.update(customer);
					
					//模块日志添加
					crmService.addCustomerLog(userInfo.getComId(),customer.getId(),userInfo.getId(),"同意变更客户阶段为:\""+orgObj.getStageName()+"\"");
					busUpdateService.addBusUpdate(userInfo, customer.getId(),ConstantInterface.TYPE_CRM,
							ConstantInterface.BUSUPDATETYPE_UPDATE, "变更客户阶段为:\""+orgObj.getStageName()+"\"");
				}else {
					CustomerStage orgObj = crmService.queryCustomerStage(Integer.parseInt(moduleChangeApply.getNewValue()));
					//模块日志添加
					crmService.addCustomerLog(userInfo.getComId(),moduleChangeApply.getBusId(),userInfo.getId(),"不同意变更客户阶段为:\""+orgObj.getStageName()+"\"");
				}
				
			}
		}
		
		//更新申请记录
		ModuleChangeApply apply = new ModuleChangeApply();
		apply.setId(moduleChangeApply.getId());
		apply.setStatus(moduleChangeApply.getStatus());
		apply.setSpOpinion(moduleChangeApply.getSpOpinion());
		moduleChangeExamineDao.update(apply);
		//更新属性变更审核为普通消息并设置已读
		todayWorksService.updateTodayWorksBusSpecTo0ReadTo1(ConstantInterface.TYPE_CHANGE_EXAM, moduleChangeApply.getId());
		return true;
	}
	
	/**
	 * 批量变更申请
	 * @param moduleChangeApply
	 * @param feedBackTypeId
	 * @param userInfo
	 * @param busIds
	 */
	public void addMoreApply(ModuleChangeApply moduleChangeApply, Integer feedBackTypeId, UserInfo userInfo,
			Integer[] busIds) {
		if(!CommonUtil.isNull(busIds)) {
			if(ConstantInterface.TYPE_CRM.equals(moduleChangeApply.getModuleType())) {
				for (Integer crmId : busIds) {
					Customer customer = crmService.queryCustomer(userInfo, crmId);
					moduleChangeApply.setBusName(customer.getCustomerName());
					moduleChangeApply.setOldValue(userInfo.getId()+"");
					moduleChangeApply.setBusId(crmId);
					this.addApply(moduleChangeApply, feedBackTypeId, userInfo);
				}
			}
		}
	}
	
	
	
}
