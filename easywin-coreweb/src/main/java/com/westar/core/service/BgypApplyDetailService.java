package com.westar.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.westar.base.model.BgypApplyDetail;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.BgypApplyDetailDao;

@Service
public class BgypApplyDetailService {

	@Autowired
	BgypApplyDetailDao bgypApplyDetailDao;

	/**
	 * 分页查询申领的办公用品详情
	 * @param sessionUser 当前操作人员
	 * @param bgypApplyDetail 申领详情信息
	 * @return
	 */
	public List<BgypApplyDetail> listPagedBgypApplyDetail(UserInfo sessionUser,
			BgypApplyDetail bgypApplyDetail) {
		return bgypApplyDetailDao.listPagedBgypApplyDetail(sessionUser,bgypApplyDetail);
	}

	/**
	 * 查询申领的办公用品详情
	 * @param sessionUser 当前操作人员
	 * @param bgypApplyDetail 申领详情信息
	 * @return
	 */
	public List<BgypApplyDetail> listBgypApplyDetail(UserInfo sessionUser,
			BgypApplyDetail bgypApplyDetail) {
		return bgypApplyDetailDao.listBgypApplyDetail(sessionUser,bgypApplyDetail);
	}
	
	/**
	 * 修改采购单详情条目
	 * @param sessionUser 当前操作人员
	 * @param bgypPurDetail 采购单详情
	 */
	public List<BgypApplyDetail> updateBgypApplyDetails(UserInfo sessionUser,
			String bgypApplyDetailStr) {
		List<BgypApplyDetail> list = new ArrayList<BgypApplyDetail>();
		
		List<BgypApplyDetail> bgypApplyDetailJson = JSONArray.parseArray(bgypApplyDetailStr,BgypApplyDetail.class);
		if(null!=bgypApplyDetailJson && !bgypApplyDetailJson.isEmpty()){
			for (BgypApplyDetail bgypApplyDetail : bgypApplyDetailJson) {
				bgypApplyDetail.setComId(sessionUser.getComId());
				if(null!=bgypApplyDetail.getId() && bgypApplyDetail.getId()>0){
					bgypApplyDetailDao.update(bgypApplyDetail);
				}else{
					Integer id= bgypApplyDetailDao.add(bgypApplyDetail);
					bgypApplyDetail.setId(id);
				}
				list.add(bgypApplyDetail);
			}
		}
		return list;
	}

	/**
	 * 删除申领的详情信息
	 * @param sessionUser 当前操作人员
	 * @param applyDetailId 申领的详情主键
	 */
	public void delBgypApplyDetail(UserInfo sessionUser, Integer applyDetailId) {
		bgypApplyDetailDao.delById(BgypApplyDetail.class, applyDetailId);
		
	}

	/**
	 * 查询本次申请的是否够数
	 * @param comId 团队号
	 * @param bgypApplyId 本次申请的主键
	 * @return
	 */
	public List<BgypApplyDetail> listLossBgypItemStore(Integer comId, Integer bgypApplyId) {
		return bgypApplyDetailDao.listLossBgypItemStore(comId,bgypApplyId);
	}
	
	
	
}
