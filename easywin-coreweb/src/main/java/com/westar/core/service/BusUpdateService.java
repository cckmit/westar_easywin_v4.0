package com.westar.core.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.BusUpdate;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.BusUpdateDao;

@Service
public class BusUpdateService {

	@Autowired
	BusUpdateDao busUpdateDao;
	
	/**
	 * 添加业务更新记录
	 * @param userInfo 当前用户
	 * @param busId 业务主键
	 * @param busType 更新类型
	 * @param updateType
	 * @param content 更新内容
	 */
	public void addBusUpdate(UserInfo userInfo,Integer busId,String busType,String updateType,String content){
		BusUpdate busUpdate = new BusUpdate();
		busUpdate.setBusId(busId);
		busUpdate.setBusType(busType);
		busUpdate.setComId(userInfo.getComId());
		busUpdate.setUpdateType(updateType);
		busUpdate.setUserId(userInfo.getId());
		busUpdate.setContent(content);
		busUpdateDao.add(busUpdate);
	}
	
}
