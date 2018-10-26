package com.westar.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.LoginMac;
import com.westar.base.model.UserInfo;
import com.westar.base.util.StringUtil;
import com.westar.core.dao.LoginMacDao;

@Service
public class LoginMacService {

	@Autowired
	LoginMacDao loginMacDao;

	/**
	 * 添加用户常用登录地址
	 * @param userInfo 用户信息
	 * @param smac mac地址
	 * @param ip IP
	 */
	public void addLoginMac(UserInfo userInfo, String smac, String ip) {
		if(!StringUtil.isBlank(smac) && !StringUtil.isBlank(ip)){
			String userId = userInfo.getId().toString();
			Integer count = loginMacDao.countLoginMac(userId, smac);
			if(count==0){//在这台艰难上没有登陆过没有登录
				LoginMac loginMac = new LoginMac();
				loginMac.setUserId(userInfo.getId());
				loginMac.setMac(smac);
				loginMac.setIp(ip);
				
				loginMacDao.add(loginMac);
			}
		}
		
	}
	
	/**
	 * 取得账号是否在该pc登录过
	 * @param userId 用户主键
	 * @param smac mac地址
	 * @param ip 
	 * @return
	 */
	public Integer countLoginMac(String userId,String smac){
		Integer count = loginMacDao.countLoginMac(userId,smac);
		return count;
	}
	
	
	
}
