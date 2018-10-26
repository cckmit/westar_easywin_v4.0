package com.westar.core.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.UserConf;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.UserConfDao;

@Service
public class UserConfService {

	@Autowired
	UserConfDao userConfDao;

	/**
	 * 查询人员配置项的开启状态集合
	 * @param comId 企业号
	 * @param userId
	 * @return
	 */
	public List<UserConf> listUserConfig(UserInfo sessionUser,String type) {
		
		return userConfDao.listUserConfig(sessionUser,type);
	}
	
	/**
	 * 修改个人配置信息
	 * @param comId 企业号
	 * @param userId 人员主键
	 * @param sysConfId 系统配置主键
	 */
	public Integer updateUserConf(UserInfo sessionUser,UserConf userConf) {
		Integer comId = sessionUser.getComId(); 
		Integer userId = sessionUser.getId();
		
		String sysConfCode = userConf.getSysConfCode();
		UserConf userConfVo = userConfDao.queryUserConf(sessionUser,userConf);
		if(!StringUtils.isEmpty(sysConfCode) && !sysConfCode.equals("-1") ){
			
			Integer openState = 0;
			if(null!=userConfVo){
				openState = (userConfVo.getOpenState()+1)%2;
				userConfVo.setOpenState(openState);
				userConfDao.update(userConfVo);
			}else{
				//设置企业号
				userConf.setComId(comId);
				//设置人员主键
				userConf.setUserId(userId);
				//设置开启状态
				userConf.setOpenState(openState);
				//添加个人配置
				userConfDao.add(userConf);
			}
			return openState;
			
		}else{
			if(null!=userConfVo){
				userConfVo.setOpenState(userConf.getOpenState());
				userConfDao.update(userConfVo);
			}else{
				//设置企业号
				userConf.setComId(comId);
				//设置人员主键
				userConf.setUserId(userId);
				
				userConf.setSysConfCode("-1");
				//添加个人配置
				userConfDao.add(userConf);
			}
			return null;
		}
		
		
	}
	/**
	 * 查询配置信息
	 * @param sessionUser
	 * @param userConf
	 * @return
	 */
	public UserConf queryUserConf(UserInfo sessionUser,UserConf userConf){
		UserConf userConfVo = userConfDao.queryUserConf(sessionUser,userConf);
		return userConfVo;
	}

	/**
	 * 查询用户的消息接收方式
	 * @param comId 企业号
	 * @param userId 消息接收人员
	 * @return
	 */
	public List<UserConf> listUserMsgWayConf(Integer comId, Integer userId) {
		
		return userConfDao.listUserMsgWayConf(comId,userId);
	}
	
	
}
