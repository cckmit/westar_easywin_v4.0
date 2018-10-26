package com.westar.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.BgypFl;
import com.westar.base.model.BgypItem;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.BgypItemDao;

@Service
public class BgypItemService {

	@Autowired
	BgypItemDao bgypItemDao;

	/**
	 * 查询分类的办公用品条目
	 * @param sessionUser 当前操作人员
	 * @param bgypItem 办公用品条目查询条件
	 * @return
	 */
	public List<BgypItem> listPagedBgypItem4Fl(UserInfo sessionUser,
			BgypItem bgypItem) {
		return bgypItemDao.listPagedBgypItem4Fl(sessionUser,bgypItem);
	}
	/**
	 * 查询分类的办公用品条目
	 * @param sessionUser 当前操作人员
	 * @param bgypItem 办公用品条目查询条件
	 * @return
	 */
	public List<BgypItem> listPagedBgypStore(UserInfo sessionUser,
			BgypItem bgypItem) {
		return bgypItemDao.listPagedBgypStore(sessionUser,bgypItem);
	}
	
	/**
	 * 取得子类的所有办公条目
	 * @param sessionUser 当前操作员
	 * @param flId 分类主键
	 * @return
	 */
	public List<BgypItem> listBgypItem4FlWithSub(UserInfo sessionUser,
			Integer flId) {
		return bgypItemDao.listBgypItem4FlWithSub(sessionUser,flId);
	}
	/**
	 * 按分类查询办公用品的库存信息
	 * @param sessionUser 当前操作人员
	 * @param flId 分类主键
	 * @return
	 */
	public List<BgypItem> listBgypStore4FlWithSub(UserInfo sessionUser,
			Integer flId) {
		
		return bgypItemDao.listBgypStore4FlWithSub(sessionUser,flId);
	}

	/**
	 * 添加办公用品条目
	 * @param sessionUser 当前操作人员
	 * @param bgypItem 办公用品条目
	 * @return
	 */
	public Map<String, Object> addBgypItem(UserInfo sessionUser,
			BgypItem bgypItem) {
		Map<String, Object> map = new HashMap<String, Object>();
		//办公用品参数信息
		Integer checkBgypItem = bgypItemDao.countBgypItemByCode(sessionUser.getComId(),0,bgypItem.getBgypCode(),bgypItem.getFlId());
		if(checkBgypItem.equals(0)){//没有重复的编码
			bgypItem.setComId(sessionUser.getComId());
			bgypItemDao.add(bgypItem);
			map.put("status", "y");
		}else{
			map.put("status", "f");
			map.put("info", "编码已存在");
		}
		return map;
	}
	/**
	 * 修改办公用品条目
	 * @param sessionUser 当前操作人员
	 * @param bgypItem 办公用品条目
	 * @return
	 */
	public Map<String, Object> updateBgypItem(UserInfo sessionUser,
			BgypItem bgypItem) {
		Map<String, Object> map = new HashMap<String, Object>();
		//办公用品参数信息
		Integer checkBgypItem = bgypItemDao.countBgypItemByCode(sessionUser.getComId(),bgypItem.getId(),bgypItem.getBgypCode(),bgypItem.getFlId());
		if(checkBgypItem.equals(0)){//没有重复的编码
			bgypItem.setComId(sessionUser.getComId());
			bgypItemDao.update(bgypItem);
			map.put("status", "y");
		}else{
			map.put("status", "f");
			map.put("info", "编码已存在");
		}
		return map;
	}

	/**
	 * 根据条目主键查询
	 * @param comId 团队主键
	 * @param bgypItemId 条目主键
	 * @return
	 */
	public BgypItem queryBgypItemById(Integer comId, Integer bgypItemId) {
		return bgypItemDao.queryBgypItemById(comId,bgypItemId);
	}

	/**
	 * 删除办公用品条目
	 * @param comId 团队主键
	 * @param bgypItemIds 条目主键
	 */
	public List<BgypItem> delBgypItem(Integer comId, Integer[] bgypItemIds) {
		
		List<BgypItem> bgypItems = bgypItemDao.listBgypItem4Del(bgypItemIds);
		if(null==bgypItems || bgypItems.isEmpty()){
			bgypItemDao.delById(BgypItem.class, bgypItemIds);
			return null;
		}else{
			return bgypItems;
		}
	}
	/**
	 * 修改办公用品分类
	 * @param comId 团队号 
	 * @param bgypFl 分类
	 * @param bgypItemIds
	 */
	public void updateBgypItemFl(Integer comId, BgypFl bgypFl,
			Integer[] bgypItemIds) {
		for (int i = 0; i < bgypItemIds.length; i++) {
			BgypItem bgypItem = new BgypItem();	
			bgypItem.setId(bgypItemIds[i]);
			bgypItem.setFlId(bgypFl.getId());
			bgypItemDao.update(bgypItem);
		}
	}
	
}
