package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.ForMeDo;
import com.westar.core.dao.ForMeDoDao;

@Service
public class ForMeDoService {

	@Autowired
	ForMeDoDao forMeDoDao;
	
	/**
	 * 查询代理人员
	 * @param comId 组织号
	 * @param userId 离岗人员主键
	 * @return
	 */
	public ForMeDo queryInsteadUser(Integer comId,Integer userId){
		return forMeDoDao.queryInsteadUser(comId,userId);
	}
	
	/**
	 * 查询离岗替岗人员
	 * @param comId
	 * @return
	 */
	public List<ForMeDo> listInsteadUser(Integer comId) {
		return forMeDoDao.listInsteadUser(comId);
	}
	
	
}
