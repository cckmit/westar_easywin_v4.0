package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.BgypItem;
import com.westar.base.model.BgypPurDetail;
import com.westar.base.model.BgypPurFile;
import com.westar.base.model.BgypPurOrder;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.BgypPurOrderDao;

@Service
public class BgypPurOrderService {

	@Autowired
	BgypPurOrderDao bgypPurOrderDao;
	
	@Autowired
	BgypPurDetailService bgypPurDetailService;
	
	@Autowired
	ModAdminService modAdminService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	UserInfoService userInfoService;
	
	/**
	 * 分页查询采购单汇总信息
	 * @param sessionUser 当前操作人员
	 * @param bgypPurOrder 采购单查询条件
	 * @return
	 */
	public List<BgypPurOrder> listPagedBgypPurOrder(UserInfo sessionUser,
			BgypPurOrder bgypPurOrder) {
		return bgypPurOrderDao.listPagedBgypPurOrder(sessionUser,bgypPurOrder);
	}
	/**
	 * 分页查询采购单汇总信息
	 * @param sessionUser 当前操作人员
	 * @param bgypPurOrder 采购单查询条件
	 * @return
	 */
	public List<BgypPurOrder> listPagedSpBgypPurOrder(UserInfo sessionUser,
			BgypPurOrder bgypPurOrder) {
		return bgypPurOrderDao.listPagedSpBgypPurOrder(sessionUser,bgypPurOrder);
	}

	/**
	 * 添加采购单汇总信息
	 * @param sessionUser 当前操作人员
	 * @param bgypPurOrder 采购单汇总信息
	 */
	public void addBgypPurOrder(UserInfo sessionUser, BgypPurOrder bgypPurOrder) {
		//设置团队号
		bgypPurOrder.setComId(sessionUser.getComId());
		//设置录入人员
		bgypPurOrder.setPurRecorder(sessionUser.getId());
		Integer purOrderId = bgypPurOrderDao.add(bgypPurOrder);
		
		//相关附件
		List<BgypPurFile> listBgypPurFiles = bgypPurOrder.getListBgypPurFiles();
		if(null != listBgypPurFiles && !listBgypPurFiles.isEmpty()){
			 for (BgypPurFile bgypPurFile : listBgypPurFiles) {
				 bgypPurFile.setComId(sessionUser.getComId());
				 bgypPurFile.setPurOrderId(purOrderId);
				 bgypPurOrderDao.add(bgypPurFile);
			 }
		}
		//用品条目		 
		List<BgypPurDetail> listBgypPurDetails = bgypPurOrder.getListBgypPurDetails();
		if(null!=listBgypPurDetails && !listBgypPurDetails.isEmpty()){
			for (BgypPurDetail bgypPurDetail : listBgypPurDetails) {
				bgypPurDetail.setComId(sessionUser.getComId());
				bgypPurDetail.setPurOrderId(purOrderId);
				bgypPurDetail.setStoreState("0");
				bgypPurOrderDao.add(bgypPurDetail);
				
			}
		}
		
		String purOrderState = bgypPurOrder.getPurOrderState();
		if(ConstantInterface.PURORDER_STATE_CHECK.equals(purOrderState)){//送审
			//给管理人员发送
			List<UserInfo> shares = modAdminService.listBusTypeModAdmin(sessionUser.getComId(), ConstantInterface.TYPE_BGYP);
			
			todayWorksService.addTodayWorks(sessionUser, null, purOrderId,
					"采购单提交审核", ConstantInterface.TYPE_BGYP_BUY_CHECK, shares, null);
		}
		
	}
	/**
	 * 添加采购单汇总信息
	 * @param sessionUser 当前操作人员
	 * @param bgypPurOrder 采购单汇总信息
	 */
	public void updateBgypPurOrder(UserInfo sessionUser, BgypPurOrder bgypPurOrder) {
		Integer purOrderId = bgypPurOrder.getId();
		
		String purOrderState = bgypPurOrder.getPurOrderState();
		if(ConstantInterface.PURORDER_STATE_CHECK.equals(purOrderState)){//送审
			bgypPurOrder.setSpUserId(0);
			bgypPurOrder.setSpContent(" ");
		}
		bgypPurOrderDao.update(bgypPurOrder);
		
		bgypPurOrderDao.delByField("bgypPurFile", new String[]{"comId","purOrderId"},
				new Object[]{sessionUser.getComId(),purOrderId});
		
		//相关附件
		List<BgypPurFile> listBgypPurFiles = bgypPurOrder.getListBgypPurFiles();
		if(null != listBgypPurFiles && !listBgypPurFiles.isEmpty()){
			for (BgypPurFile bgypPurFile : listBgypPurFiles) {
				bgypPurFile.setComId(sessionUser.getComId());
				bgypPurFile.setPurOrderId(purOrderId);
				bgypPurOrderDao.add(bgypPurFile);
			}
		}
		//用品条目		 
		List<BgypPurDetail> listBgypPurDetails = bgypPurOrder.getListBgypPurDetails();
		if(null!=listBgypPurDetails && !listBgypPurDetails.isEmpty()){
			for (BgypPurDetail bgypPurDetail : listBgypPurDetails) {
				if(null!=bgypPurDetail && null!=bgypPurDetail.getId() && bgypPurDetail.getId()>0){
					bgypPurOrderDao.update(bgypPurDetail);
				}else{
					bgypPurDetail.setComId(sessionUser.getComId());
					bgypPurDetail.setPurOrderId(purOrderId);
					bgypPurDetail.setStoreState("0");
					bgypPurOrderDao.add(bgypPurDetail);
				}
				
			}
		}
		
		if(ConstantInterface.PURORDER_STATE_CHECK.equals(purOrderState)){//送审
			//给管理人员发送
			List<UserInfo> shares = modAdminService.listBusTypeModAdmin(sessionUser.getComId(), ConstantInterface.TYPE_BGYP);
			
			todayWorksService.addTodayWorks(sessionUser, null, purOrderId,
					"采购单提交审核", ConstantInterface.TYPE_BGYP_BUY_CHECK, shares, null);
		}
		
	}

	/**
	 * 根据采购单主键查寻详情
	 * @param comId
	 * @param purOrderId
	 * @return
	 */
	public BgypPurOrder queryBgypPurOrderById(Integer comId, Integer purOrderId) {
		BgypPurOrder bgypPurOrder = bgypPurOrderDao.queryBgypPurOrderById(comId,purOrderId);
		if(null!=bgypPurOrder){
			bgypPurOrder.setListBgypPurFiles(bgypPurOrderDao.listBgypPurFiles(comId,purOrderId));
		}
		return bgypPurOrder;
	}

	/**
	 * 修改审核状态
	 * @param sessionUser
	 * @param purOrderId
	 * @param purOrderState
	 * @return
	 */
	public Map<String, Object> updateBgypPurOrderState(UserInfo sessionUser,
			BgypPurOrder bgypPurOrder,String spContent) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String purOrderState = bgypPurOrder.getPurOrderState();
		if(ConstantInterface.PURORDER_STATE_CHECK.equals(purOrderState)){//送审
			
			bgypPurOrder.setSpUserId(0);
			bgypPurOrder.setSpContent(" ");
			//修改状态
			bgypPurOrderDao.update(bgypPurOrder);
			
			//给管理人员发送
			List<UserInfo> shares = modAdminService.listBusTypeModAdmin(sessionUser.getComId(),
					ConstantInterface.TYPE_BGYP);
			
			todayWorksService.addTodayWorks(sessionUser, null, bgypPurOrder.getId(),
					"采购单提交审核", ConstantInterface.TYPE_BGYP_BUY_CHECK, shares, null);
			
			
		}else if(ConstantInterface.PURORDER_STATE_BACK.equals(purOrderState)){//驳回
			
			bgypPurOrder.setSpUserId(sessionUser.getId());
			bgypPurOrder.setSpContent(spContent);
			//修改状态
			bgypPurOrderDao.update(bgypPurOrder);
			
			List<UserInfo> shares = new ArrayList<UserInfo>();
			UserInfo purUser = userInfoService.getUserBaseInfo(sessionUser.getComId(), bgypPurOrder.getPurUserId());
			shares.add(purUser);
			todayWorksService.addTodayWorks(sessionUser, null, bgypPurOrder.getId(),
					"采购单审核未通过,审批意见："+spContent, ConstantInterface.TYPE_BGYP_BUY_NOTICE, shares, null);
		}else if(ConstantInterface.PURORDER_STATE_PASS.equals(purOrderState)){//通过并入库
			bgypPurOrder.setSpUserId(sessionUser.getId());
			bgypPurOrder.setSpContent(spContent);
			
			BgypPurDetail bgypPurDetail = new BgypPurDetail();
			bgypPurDetail.setPurOrderId(bgypPurOrder.getId());
			//本次需要入库的信息
			List<BgypPurDetail> listBgypPurDetails = bgypPurDetailService.listBgypPurDetail(sessionUser, bgypPurDetail);
			if(null!=listBgypPurDetails && !listBgypPurDetails.isEmpty()){
				for (BgypPurDetail purDetail : listBgypPurDetails) {
					if(purDetail.getStoreState().equals("1")){
						continue;
					}
					purDetail.setStoreState("1");
					bgypPurOrderDao.update(purDetail);
					
					//办公库存信息
					BgypItem bgypStore = new BgypItem();
					bgypStore.setComId(sessionUser.getComId());
					bgypStore.setId(purDetail.getBgypItemId());
					bgypStore.setStoreNum(purDetail.getBgypNum());
					//修改库存信息
					bgypPurOrderDao.updateBgypItemStore(bgypStore);
				}
			}
			//修改状态
			bgypPurOrderDao.update(bgypPurOrder);
			
			List<UserInfo> shares = new ArrayList<UserInfo>();
			UserInfo purUser = userInfoService.getUserBaseInfo(sessionUser.getComId(), bgypPurOrder.getPurUserId());
			shares.add(purUser);
			todayWorksService.addTodayWorks(sessionUser, null, bgypPurOrder.getId(),
					"采购单审核通过", ConstantInterface.TYPE_BGYP_BUY_NOTICE, shares, null);
			
		}
		
		map.put("status", "y");
		return map;
	}

	/**
	 * 删除采购单
	 * @param sessionUser 当前操作员
	 * @param ids
	 */
	public void deleteBgypPurOrder(UserInfo sessionUser, Integer[] ids) {
		
		List<BgypPurOrder> bgypPurOrders = bgypPurOrderDao.listBgypPurOrder4Del(sessionUser,ids);
		if(null!=bgypPurOrders && !bgypPurOrders.isEmpty()){
			for (BgypPurOrder bgypPurOrder : bgypPurOrders) {
				//采购清单附件
				bgypPurOrderDao.delByField("bgypPurFile", new String[]{"comId","purOrderId"}, 
						new Object[]{sessionUser.getComId(),bgypPurOrder.getId()});
				//用品采购清单
				bgypPurOrderDao.delByField("bgypPurDetail", new String[]{"comId","purOrderId"}, 
						new Object[]{sessionUser.getComId(),bgypPurOrder.getId()});
				//用品采购清单总
				bgypPurOrderDao.delById(BgypPurOrder.class, bgypPurOrder.getId());
			}
		}
		
	}
	
	
	
}
