package com.westar.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.westar.base.model.BgypPurDetail;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.BgypPurDetailDao;

@Service
public class BgypPurDetailService {

	@Autowired
	BgypPurDetailDao bgypPurDetailDao;

	/**
	 * 分页查询采购单详情
	 * @param sessionUser 当前操作人员
	 * @param bgypPurDetail 采购单详情查询条件
	 * @return
	 */
	public List<BgypPurDetail> listPagedBgypPurDetail(UserInfo sessionUser,
			BgypPurDetail bgypPurDetail) {
		return bgypPurDetailDao.listPagedBgypPurDetail(sessionUser,bgypPurDetail);
	}
	/**
	 * 查询采购单详情集合
	 * @param sessionUser 当前操作人员
	 * @param bgypPurDetail 采购单详情查询条件
	 * @return
	 */
	public List<BgypPurDetail> listBgypPurDetail(UserInfo sessionUser,
			BgypPurDetail bgypPurDetail) {
		return bgypPurDetailDao.listBgypPurDetail(sessionUser,bgypPurDetail);
	}
	/**
	 * 删除采购单详情条目
	 * @param sessionUser 当前操作人员
	 * @param bgypPurDetailId采购单详情主键
	 */
	public void delBgypPurDetail(UserInfo sessionUser, Integer bgypPurDetailId) {
		bgypPurDetailDao.delById(BgypPurDetail.class, bgypPurDetailId);
		
	}
	/**
	 * 修改采购单详情条目
	 * @param sessionUser 当前操作人员
	 * @param bgypPurDetail 采购单详情
	 */
	public List<BgypPurDetail> updateBgypPurDetail(UserInfo sessionUser,
			String bgypPurDetailStr) {
		List<BgypPurDetail> list = new ArrayList<BgypPurDetail>();
		
		List<BgypPurDetail> bgypPurDetailJson = JSONArray.parseArray(bgypPurDetailStr,BgypPurDetail.class);
		if(null!=bgypPurDetailJson && !bgypPurDetailJson.isEmpty()){
			for (BgypPurDetail bgypPurDetail : bgypPurDetailJson) {
				bgypPurDetail.setComId(sessionUser.getComId());
				if(null!=bgypPurDetail.getId() && bgypPurDetail.getId()>0){
					bgypPurDetailDao.update(bgypPurDetail);
				}else{
					Integer id= bgypPurDetailDao.add(bgypPurDetail);
					bgypPurDetail.setId(id);
				}
				list.add(bgypPurDetail);
			}
		}
		return list;
	}
	
	
	
}
