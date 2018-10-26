package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.BgypApply;
import com.westar.base.model.BgypApplyDetail;
import com.westar.base.model.BgypItem;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.BgypApplyDao;

@Service
public class BgypApplyService {

	@Autowired
	BgypApplyDao bgypApplyDao;
	
	@Autowired
	BgypApplyDetailService applyDetailService;
	
	@Autowired
	ModAdminService modAdminService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	UserInfoService userInfoService;
 
	/**
	 * 分页查询申领记录
	 * @param sessionUser 当前操作人员
	 * @param bgypApply 申领条件
	 * @return
	 */
	public List<BgypApply> listPagedBgypApply(UserInfo sessionUser,
			BgypApply bgypApply) {
		return bgypApplyDao.listPagedBgypApply(sessionUser,bgypApply);
	}
	/**
	 * 分页查询申领记录
	 * @param sessionUser 当前操作人员
	 * @param bgypApply 申领条件
	 * @return
	 */
	public List<BgypApply> listPagedSpBgypApply(UserInfo sessionUser,
			BgypApply bgypApply) {
		return bgypApplyDao.listPagedSpBgypApply(sessionUser,bgypApply);
	}
	/**
	 * 添加申领记录
	 * @param sessionUser 当前操作人员
	 * @param bgypApply申领信息
	 */
	public void addBgypApply(UserInfo sessionUser, BgypApply bgypApply) {
		bgypApply.setComId(sessionUser.getComId());
		bgypApply.setApplyType("1");
		bgypApply.setApplyUserId(sessionUser.getId());
		
		String applyCheckState = bgypApply.getApplyCheckState();
		if(applyCheckState.equals(ConstantInterface.PURORDER_STATE_CHECK)){
			bgypApply.setApplyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd));
		}
		Integer bgypApplyId= bgypApplyDao.add(bgypApply);
		
		List<BgypApplyDetail> listBgypApplyDetails = bgypApply.getListBgypApplyDetails();
		if(null!= listBgypApplyDetails && !listBgypApplyDetails.isEmpty()){
			for (BgypApplyDetail bgypApplyDetail : listBgypApplyDetails) {
				bgypApplyDetail.setComId(sessionUser.getComId());
				bgypApplyDetail.setBgypApplyId(bgypApplyId);
				bgypApplyDao.add(bgypApplyDetail);
			}
		}
		
		if(ConstantInterface.PURORDER_STATE_CHECK.equals(applyCheckState)){//送审
			//给管理人员发送
			List<UserInfo> shares = modAdminService.listBusTypeModAdmin(sessionUser.getComId(), ConstantInterface.TYPE_BGYP);
			
			todayWorksService.addTodayWorks(sessionUser, null, bgypApplyId,
					"办公用品申领提交审核", ConstantInterface.TYPE_BGYP_APPLY_CHECK, shares, null);
		}
		
		
	}

	/**
	 * 查询申领记录的基本信息
	 * @param comId 团队号
	 * @param applyId 申领记录的主键
	 * @return
	 */
	public BgypApply queryBgypApplyById(Integer comId, Integer applyId) {
		return bgypApplyDao.queryBgypApplyById(comId,applyId);
	}

	/**
	 * 修改申领的基本信息和详情
	 * @param sessionUser 当前操作人员
	 * @param bgypApply 申领记录的基本信息以及详情
	 */
	public void updateBgypApply(UserInfo sessionUser, BgypApply bgypApply) {
		Integer bgypApplyId = bgypApply.getId();
		
		String applyCheckState = bgypApply.getApplyCheckState();
		if(applyCheckState.equals(ConstantInterface.PURORDER_STATE_CHECK)){//审核
			bgypApply.setApplyDate(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd));
			bgypApply.setSpUserId(0);
			bgypApply.setSpContent(" ");
		}
		
		bgypApplyDao.update(bgypApply);
		List<BgypApplyDetail> listBgypApplyDetails = bgypApply.getListBgypApplyDetails();
		if(null!= listBgypApplyDetails && !listBgypApplyDetails.isEmpty()){
			for (BgypApplyDetail bgypApplyDetail : listBgypApplyDetails) {
				if(null!=bgypApplyDetail && null!=bgypApplyDetail.getId() && bgypApplyDetail.getId()>0){
					bgypApplyDao.update(bgypApplyDetail);
				}else{
					bgypApplyDetail.setComId(sessionUser.getComId());
					bgypApplyDetail.setBgypApplyId(bgypApplyId);
					bgypApplyDao.add(bgypApplyDetail);
				}
			}
		}
		
		if(ConstantInterface.PURORDER_STATE_CHECK.equals(applyCheckState)){//送审
			//给管理人员发送
			List<UserInfo> shares = modAdminService.listBusTypeModAdmin(sessionUser.getComId(), ConstantInterface.TYPE_BGYP);
			
			todayWorksService.addTodayWorks(sessionUser, null, bgypApplyId,
					"办公用品申领提交审核", ConstantInterface.TYPE_BGYP_APPLY_CHECK, shares, null);
		}
		
	}

	/**
	 * 修改办公用品申领的审核状态
	 * @param sessionUser当前操作人员
	 * @param bgypApply 申领的审核信息
	 * @return
	 */
	public Map<String, Object> updateBgypApplyState(UserInfo sessionUser,
			BgypApply bgypApply,String spContent) {
		String applySheckState = bgypApply.getApplyCheckState();
		Map<String, Object>  map = new HashMap<String, Object>();
		
		if(ConstantInterface.PURORDER_STATE_BACK.equals(applySheckState)){//驳回
			bgypApply.setSpUserId(sessionUser.getId());
			bgypApply.setSpContent(spContent);
			//修改状态
			bgypApplyDao.update(bgypApply);
			
			List<UserInfo> shares = new ArrayList<UserInfo>();
			UserInfo purUser = userInfoService.getUserBaseInfo(sessionUser.getComId(), bgypApply.getApplyUserId());
			shares.add(purUser);
			todayWorksService.addTodayWorks(sessionUser, null, bgypApply.getId(),
					"办公用品申领,审核未通过。审核意见为："+spContent, ConstantInterface.TYPE_BGYP_APPLY_NOTICE, shares, null);
		}else if(ConstantInterface.PURORDER_STATE_PASS.equals(applySheckState)){//通过并出库
			bgypApply.setSpUserId(sessionUser.getId());
			//查看是否有足够的库存
			List<BgypApplyDetail> listLoss =  applyDetailService.listLossBgypItemStore(sessionUser.getComId(),bgypApply.getId());
			if(null != listLoss && !listLoss.isEmpty()){
				map.put("listLossApply", listLoss);
				map.put("status", "f");
				return map;
			}else{
				//修改状态
				bgypApplyDao.update(bgypApply);
				BgypApplyDetail bgypApplyDetail = new BgypApplyDetail();
				bgypApplyDetail.setBgypApplyId(bgypApply.getId());
				List<BgypApplyDetail> applyDetails = applyDetailService.listBgypApplyDetail(sessionUser, bgypApplyDetail);
				if(null!=applyDetails && !applyDetails.isEmpty()){
					for (BgypApplyDetail applyDetail : applyDetails) {
						
						//办公库存信息
						BgypItem bgypStore = new BgypItem();
						bgypStore.setComId(sessionUser.getComId());
						bgypStore.setId(applyDetail.getBgypItemId());
						bgypStore.setStoreNum(applyDetail.getApplyNum());
						//修改库存信息
						bgypApplyDao.updateBgypItemStore(bgypStore);
					}
				}
			}
			
			List<UserInfo> shares = new ArrayList<UserInfo>();
			UserInfo purUser = userInfoService.getUserBaseInfo(sessionUser.getComId(), bgypApply.getApplyUserId());
			shares.add(purUser);
			todayWorksService.addTodayWorks(sessionUser, null, bgypApply.getId(),
					"办公用品申领审核通过", ConstantInterface.TYPE_BGYP_APPLY_NOTICE, shares, null);
			
		}
		map.put("status", "y");
		return map;
	}

	/**
	 * 删除申领记录
	 * @param sessionUser 当前操作人员
	 * @param ids 申领记录的主键集合
	 */
	public void deleteBgypApply(UserInfo sessionUser, Integer[] ids) {
		for (Integer applyId : ids) {
			//申领清单附件
			bgypApplyDao.delByField("bgypApplyDetail", new String[]{"comId","bgypApplyId"}, 
					new Object[]{sessionUser.getComId(),applyId});
			//申领清单信息
			bgypApplyDao.delById(BgypApply.class, applyId);
		}
	}
	
	
	
}
