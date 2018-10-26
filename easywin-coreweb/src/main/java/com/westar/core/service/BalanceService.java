package com.westar.core.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.FeeLoan;
import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.UserInfo;
import com.westar.base.util.BeanRefUtil;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.BalanceDao;
import com.westar.core.thread.sendPhoneMsgThread;

@Service
public class BalanceService {

	@Autowired
	BalanceDao balanceDao;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	PhoneMsgService phoneMsgService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	BusRelateSpFlowService busRelateSpFlowService;
	
	/**
	  * 完成结算、借款
	 * @param usePhone 
	 * @param content 
	 * @param instanceId流程id
	 * @param busId借款或者报销id
	 * @param type区分借款还是报销
	 * @param payType支付方式
	 */
	public void addBanlance(UserInfo userInfo,Integer instanceId,Integer busId,String type,String payType, String content, String usePhone) {
		
		//取得需要映射的数据
		Map<String,String> mapForBean = workFlowService.mapForBean(userInfo, instanceId);
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		if(ConstantInterface.MENU_LOAN.equals(type)){//更新借款
			FeeLoan loan = new FeeLoan();
			loan.setId(busId);
			
			if(null!=mapForBean && !mapForBean.isEmpty()){
				BeanRefUtil.setFieldValue(loan,mapForBean);
			}
			
			busRelateSpFlowService.updateApplyByLoanId(loan,userInfo);
			
			loan.setBalanceTime(nowTime);
			loan.setPayType(payType);
			loan.setBalanceUserId(userInfo.getId());
			loan.setBalanceState(1);//已借款
			balanceDao.update(loan);
			
			
			
		}else {//更新报销
			FeeLoanOff feeLoanOff = new FeeLoanOff();
			feeLoanOff.setId(busId);
			
			if(null!=mapForBean && !mapForBean.isEmpty()){
				BeanRefUtil.setFieldValue(feeLoanOff,mapForBean);
			}
			busRelateSpFlowService.updateApplyByLoanOffId(feeLoanOff, userInfo);
			
			feeLoanOff.setBalanceTime(nowTime);
			feeLoanOff.setPayType(payType);
			feeLoanOff.setBalanceUserId(userInfo.getId());
			feeLoanOff.setBalanceState(1);//已结算
			balanceDao.update(feeLoanOff);
		}
		// 删除流程实例与模块关联关系业务数据映射关系临时表
		balanceDao.delByField("busAttrMapFormColTemp", 
				new String[] { "comId","instanceId" }, 
				new Object[] { userInfo.getComId(), instanceId });
		//更新财务人员待办为普通消息并设置已读
		todayWorksService.updateTodayWorksBusSpecTo0ReadTo1(ConstantInterface.TYPE_FINALCIAL_BALANCE, instanceId);
		
		/*//向相应的人发送消息
		SpFlowInstance spFlowInstance = workFlowService.getSpFlowInstanceById(instanceId, userInfo);
		List<UserInfo> shares = new ArrayList<UserInfo>();
		UserInfo proposer = userInfoService.getUserBaseInfo(userInfo.getComId(),
				spFlowInstance.getCreator());
		shares.add(proposer);
		todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_BALANCED,
				instanceId, content,
				spFlowInstance.getCreator(), userInfo.getId(), 1);
		
		//需要设置短信
		if("1".equals(usePhone) && !userInfo.getId().equals(spFlowInstance.getCreator())){//发送短信
			//单线程池
			ExecutorService pool = ThreadPoolExecutor.getInstance();
			//跟范围人员发送通知消息
			pool.execute(new sendPhoneMsgThread(phoneMsgService, userInfo.getComId(), shares, 
					new Object[]{content}, ConstantInterface.MSG_JOB_TO_DO,userInfo.getOptIP()));

		}*/
	}
	
	/**
	 * 批量结算
	 * @param ids
	 * @param userInfo
	 * @param type
	 */
	public void addBalances(Integer[] ids, UserInfo userInfo, String type) {
		if(!CommonUtil.isNull(ids)){
			for (Integer id : ids) {
				if("1".equals(type)) {
					FeeLoanOff loanOffAccount = (FeeLoanOff) balanceDao.objectQuery(FeeLoanOff.class, id);
					this.addBanlance(userInfo, loanOffAccount.getInstanceId(), loanOffAccount.getId(), ConstantInterface.MENU_LOANOFF, "0", "", "");
				}else {
					FeeLoan loan = (FeeLoan) balanceDao.objectQuery(FeeLoan.class, id);
					this.addBanlance(userInfo, loan.getInstanceId(), loan.getId(), ConstantInterface.MENU_LOAN, "0", "", "");
				}
			}
		}
		
	}
	
	
	/**
	 * 添加消息提醒
	 * @param userInfo
	 * @param type区分报销还是借款
	 * @param busId报销或者借款id
	 * @param instanceId流程id
	 * @param proposerId提醒的对象id
	 * @param content消息内容
	 * @param usePhone是否发送短信
	 * @return
	 */
	public Boolean addNotice(UserInfo userInfo,String type,Integer busId,Integer instanceId,Integer proposerId ,String content,String usePhone) {
		Boolean succ = true;
		//更新发送领款状态
		if("0".equals(type)) {
			FeeLoan loan = new FeeLoan();
			loan.setId(busId);
			loan.setSendNotice(1);
			balanceDao.update(loan);
		}else{
			FeeLoanOff loanOffAccount = new FeeLoanOff();
			loanOffAccount.setId(busId);
			loanOffAccount.setSendNotice(1);
			balanceDao.update(loanOffAccount);
		}
		//向相应的人发送消息
		List<UserInfo> shares = new ArrayList<UserInfo>();
		UserInfo proposer = userInfoService.getUserBaseInfo(userInfo.getComId(),
				proposerId);
		shares.add(proposer);
		todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_NOTIFICATIONS,
				instanceId, content,
				proposerId, userInfo.getId(), 1);
		
		//需要设置短信
		if("1".equals(usePhone) && !userInfo.getId().equals(proposerId)){//发送短信
			//单线程池
			ExecutorService pool = ThreadPoolExecutor.getInstance();
			//跟范围人员发送通知消息
			pool.execute(new sendPhoneMsgThread(phoneMsgService, userInfo.getComId(), shares, 
					new Object[]{content}, ConstantInterface.MSG_JOB_TO_DO,userInfo.getOptIP()));

		}
		return succ;
	}
	
	/**
	 * 批量通知
	 * @param ids
	 * @param userInfo
	 * @param type
	 */
	public void addMoreNotices(Integer[] ids, UserInfo userInfo, String type) {
		if(!CommonUtil.isNull(ids)){
			for (Integer id : ids) {
				if("1".equals(type)) {
					FeeLoanOff loanOffAccount = (FeeLoanOff) balanceDao.objectQuery(FeeLoanOff.class, id);
					SpFlowInstance spFlowInstance = (SpFlowInstance) balanceDao.objectQuery(SpFlowInstance.class, loanOffAccount.getInstanceId());
					String content = "您的\" " + spFlowInstance.getFlowName() + " \"报销申请已审核通过；请到财务室进行结算。联系人："
							+userInfo.getUserName()+"；联系电话："+(CommonUtil.isNull(userInfo.getMovePhone())?"":userInfo.getMovePhone());
					this.addNotice(userInfo, type, id, loanOffAccount.getInstanceId(), loanOffAccount.getCreator(), content, "1");
				}else {
					FeeLoan loan = (FeeLoan) balanceDao.objectQuery(FeeLoan.class, id);
					SpFlowInstance spFlowInstance = (SpFlowInstance) balanceDao.objectQuery(SpFlowInstance.class, loan.getInstanceId());
					String content = "您的\" " + spFlowInstance.getFlowName() + " \"借款申请已审核通过；请到财务室进行领款。联系人："
							+userInfo.getUserName()+"；联系电话："+(CommonUtil.isNull(userInfo.getMovePhone())?"":userInfo.getMovePhone());
					this.addNotice(userInfo, type, id, loan.getInstanceId(), loan.getCreator(), content, "1");
				}
			}
		}
		
	}
	
	
	
}
