package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Intro;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.IntroDao;

@Service
public class IntroService {

	@Autowired
	IntroDao introDao;
	/**
	 * 查询该模块是否引导
	 * @param busType
	 * @param userInfo
	 * @return
	 */
	public Intro queryIntroState(String busType, UserInfo userInfo) {
		return introDao.queryIntroState(busType,userInfo);
	}
	/**
	 * 引导完成，添加引导
	 * @param busType
	 * @param userInfo
	 * @return
	 */
	public Integer addIntro(String busType, UserInfo userInfo) {
		Intro intro = new Intro();
		intro.setBusType(busType);
		intro.setComId(userInfo.getComId());
		intro.setUserId(userInfo.getId());
		intro.setIntroState(0);
		return introDao.add(intro);
	}
}
