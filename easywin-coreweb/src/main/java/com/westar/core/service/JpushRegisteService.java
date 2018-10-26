package com.westar.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.JpushRegiste;
import com.westar.core.dao.JpushRegisteDao;

/**
 * 极光推送标识
 * @author H87
 *
 */
@Service
public class JpushRegisteService {

	@Autowired
	JpushRegisteDao jpushRegisteDao;

	/**
	 * 修改极光推送标识
	 * @param userId 用户主键
	 * @param registrationId 极光推送标识
	 * @param appSource 
	 */
	public void updateJPushRegist(Integer userId, String registrationId, String appSource) {
		JpushRegiste jpushRegiste = jpushRegisteDao.getJPushByUserId(userId);
		if(null==jpushRegiste){//没有注册
			jpushRegiste = new JpushRegiste();
			jpushRegiste.setUserId(userId);
			jpushRegiste.setRegistrationId(registrationId);
			jpushRegiste.setAppSource(appSource);
			jpushRegisteDao.add(jpushRegiste);
		}else if(null==jpushRegiste.getRegistrationId() || 
				"".equals(jpushRegiste.getRegistrationId()) ||
				!jpushRegiste.getRegistrationId().equals(registrationId)){//注册了，但是有变动
			jpushRegiste.setRegistrationId(registrationId);
			jpushRegiste.setAppSource(appSource);
			jpushRegisteDao.update(jpushRegiste);
		}
		
	}
	/**
	 * 取得极光标识
	 * @param userId 用户主键
	 * @return
	 */
	public JpushRegiste getJPushByUserId(Integer userId){
		return jpushRegisteDao.getJPushByUserId(userId);
	}
	
	
	
}
