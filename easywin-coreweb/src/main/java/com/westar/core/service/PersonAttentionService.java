package com.westar.core.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.PersonAttention;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.core.dao.PersonAttentionDao;

@Service
public class PersonAttentionService {

	@Autowired
	PersonAttentionDao personAttentionDao;
	
	@Autowired
	UserInfoService userInfoService;
	
	/**
	 * 添加关注人员
	 * @param userIds
	 * @param userInfo
	 * @return
	 */
	public boolean addPersonAttention(Integer[] userIds, UserInfo userInfo) {
		boolean succ = true;
		try {
			//添加新的关注人信息
			if(null!=userIds && userIds.length>0){
				PersonAttention personAttention =null;
				for(Integer userId:userIds){
					Integer count = this.queryCountByUserId(userId,userInfo);
					if(count==0 && userInfo.getId() != userId) {
						personAttention = new PersonAttention();
						personAttention.setComId(userInfo.getComId());
						personAttention.setUserId(userId);
						personAttention.setCreator(userInfo.getId());
						personAttentionDao.add(personAttention);
					}
					
				}
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 更新关注人员
	 * @param userIds
	 * @param userInfo
	 * @return
	 */
	public boolean updatePersonAttention(Integer[] userIds, UserInfo userInfo) {
		boolean succ = true;
		try {
			//删除原有的关注人信息
			personAttentionDao.delByField("personAttention", new String[] { "comId", "creator" },
					new Object[] { userInfo.getComId(), userInfo.getId() });
			//添加新的关注人信息
			if(null!=userIds && userIds.length>0){
				PersonAttention personAttention =null;
				for(Integer userId:userIds){
					Integer count = this.queryCountByUserId(userId,userInfo);
					if(count==0 && userInfo.getId() != userId) {
						personAttention = new PersonAttention();
						personAttention.setComId(userInfo.getComId());
						personAttention.setUserId(userId);
						personAttention.setCreator(userInfo.getId());
						personAttentionDao.add(personAttention);
					}
					
				}
			}
		} catch (Exception e) {
			succ = false ;
			throw e;
		}
		return succ;
	}
	
	/**
	 * 查询是否关注了该人员
	 * @param userId
	 * @param userInfo
	 * @return
	 */
	private Integer queryCountByUserId(Integer userId, UserInfo userInfo) {
		return personAttentionDao.queryCountByUserId(userId,userInfo);
	}

	/**
	 * 批量删除关注人员
	 * @param userId
	 * @param userInfo
	 * @return
	 */
	public boolean delPersonAttentions(Integer[] ids, UserInfo userInfo) {
		if(!CommonUtil.isNull(ids)) {
			for (Integer id : ids) {
				personAttentionDao.delById(PersonAttention.class, id);
			}
		}
		return true;
	}
	
	/**
	 * 分页查询关注人员信息
	 * @param personAttention
	 * @param userInfo
	 * @return
	 */
	public List<PersonAttention> listPagedPersonAttention(PersonAttention personAttention, UserInfo userInfo) {
		return personAttentionDao.listPagedPersonAttention(personAttention,userInfo);
	}
	
	/**
	 * 查询个人所有的关注人员
	 * @param personAttention
	 * @param userInfo
	 * @return
	 */
	public List<PersonAttention> listPersonAttention(UserInfo userInfo) {
		return personAttentionDao.listPersonAttention(userInfo);
	}
	
	
}
