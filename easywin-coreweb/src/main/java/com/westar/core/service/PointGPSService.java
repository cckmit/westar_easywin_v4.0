package com.westar.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.PointGPS;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.PointGPSDao;

@Service
public class PointGPSService {

	@Autowired
	PointGPSDao pointGPSDao;

	/**
	 *保存用户打卡位置
	 * @param pointGps
	 * @param userInfo
	 */
	public void addPointGPS(PointGPS pointGps, UserInfo userInfo) {
		// 企业号
		pointGps.setComId(userInfo.getComId());
		// 创建人
		pointGps.setUserId(userInfo.getId());
		pointGPSDao.add(pointGps);
	}
	/**
	 * 取得用户打卡位置
	 * @param userInfo
	 * @param pointGPSId
	 * @return
	 */
	public PointGPS getPointGPS(UserInfo userInfo, Integer pointGPSId) {
		PointGPS pointGPS = pointGPSDao.getPointGPS(userInfo,pointGPSId);
		return pointGPS;
	}
	
	
	
}
