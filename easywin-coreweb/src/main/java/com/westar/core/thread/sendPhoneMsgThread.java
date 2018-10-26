package com.westar.core.thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.westar.base.model.UserInfo;
import com.westar.core.service.PhoneMsgService;

public class sendPhoneMsgThread implements Runnable {
	/**
	 * service
	 */
	private PhoneMsgService phoneMsgService;
	/**
	 * 当前操作人信息
	 */
	private Integer comId;
	/**
	 * 接受人集合
	 */
	private List<UserInfo> shares;
	/**
	 * 发送配置
	 */
	private Object[] argsObj;
	/**
	 * 业务类型
	 */
	private  String busType;
	/**
	 * 业务类型
	 */
	private  String optIP;
	
	/**
	 * 构造函数
	 * @param phoneMsgService
	 * @param comId
	 * @param shares
	 * @param argsObj
	 * @param busType
	 * @param optIP
	 */
	public sendPhoneMsgThread(PhoneMsgService phoneMsgService, Integer comId,
			List<UserInfo> shares, Object[] argsObj, String busType,String optIP) {
		super();
		this.phoneMsgService = phoneMsgService;
		this.comId = comId;
		this.shares = shares;
		this.argsObj = argsObj;
		this.busType = busType;
		this.optIP = optIP;
	}
	
	@Override
	public void run() {
		//与会人员手机
		Set<String> phoneSet = new HashSet<String>();
		if(null != shares && !shares.isEmpty()){
			for(UserInfo user :shares){
				if(user.getMovePhone()!=null && !phoneSet.contains(user.getMovePhone())){
					phoneMsgService.sendMsg(user.getMovePhone(), argsObj, busType, comId,optIP);
					phoneSet.add(user.getMovePhone());
				}
			}
		}
		
	}
}
