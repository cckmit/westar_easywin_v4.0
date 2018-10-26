package com.westar.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.GroupPersion;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.UserOrganic;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.SelfGroupDao;

@Service
public class SelfGroupService {

	@Autowired
	SelfGroupDao selfGroupDao;
	
	@Autowired
	JiFenService jifenService;
	
	@Autowired
	IndexService indexService;
	
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	UserInfoService userInfoService;

	/**
	 * 移交添加分组
	 * @param userInfo 操作员
	 * @param busId 业务主键
	 * @param toUser 分组的所属人员
	 * @param busType 业务类型
	 */
	public List<SelfGroup> addGrp(UserInfo userInfo, Integer busId, Integer toUser, String busType) {
		//以前使用的分组
		List<SelfGroup> listPreGrp = new ArrayList<SelfGroup>();
		//当前使用的分组
		List<SelfGroup> listGrp = new ArrayList<SelfGroup>();
		if(busType.equals(ConstantInterface.TYPE_CRM)){//客户
			//取得客户的分享组
			listPreGrp = selfGroupDao.getCrmGrp(userInfo.getComId(),busId);
		}else if(busType.equals(ConstantInterface.TYPE_ITEM)){//项目
			//取得客户的分享组
			listPreGrp = selfGroupDao.getItemGrp(userInfo.getComId(),busId);
		}
		if(null!=listPreGrp&& listPreGrp.size()>0){
			for (SelfGroup selfGroup : listPreGrp) {
				List<UserInfo> listUser = selfGroupDao.listGroupUser(selfGroup.getId(),userInfo,null);
				//清除原有的主键
				selfGroup.setId(null);
				//设置组群的拥有人员
				selfGroup.setOwner(toUser);
				//设置组群的成员
				selfGroup.setListUser(listUser);
				//取得新的主键
				Integer grpId =  this.addGroup(selfGroup, "sys");
				//设置新的主键
				selfGroup.setId(grpId);
				
				listGrp.add(selfGroup);
			}
		}
		return listGrp;
	}
	
	/**
	 * 添加个人分组
	 * @param group
	 * @param way 为空则表示为操作员添加 非空则表示移交的时候添加
	 * @return
	 */
	public Integer addGroup(SelfGroup group,String way) {
		//个人分组的最大排序号
		int currentLevelMaxOrderNo = selfGroupDao.getCurrentLevelMaxOrderNo(group.getComId(),group.getOwner());
		group.setOrderNo(currentLevelMaxOrderNo+1);
		//个人分组主键
		Integer grpId = selfGroupDao.add(group);
		//分组成员
		List<UserInfo> userInfo = group.getListUser();
		if(null!=userInfo&&userInfo.size()>0){
			for (UserInfo user : userInfo) {
				GroupPersion groupPersion = new GroupPersion();
				//组与成员的关系
				groupPersion.setComId(group.getComId());
				groupPersion.setGrpId(grpId);
				groupPersion.setUserInfoId(user.getId());
				
				selfGroupDao.add(groupPersion);
			}
		}
		if(way==null){//操作员添加才有积分
			//添加积分
			jifenService.addJifen(group.getComId(),group.getOwner(), ConstantInterface.TYPE_USERGRP,
					"创建个人分组:"+group.getGrpName(),grpId);
		}
		return grpId;
	}
	
	/**
	 * 添加个人分组
	 * @param group
	 * @param way 为空则表示为操作员添加 非空则表示移交的时候添加
	 * @return
	 */
	public Integer addAjaxGroup(SelfGroup group,Integer[] userIds) {
		//个人分组的最大排序号
		int currentLevelMaxOrderNo = selfGroupDao.getCurrentLevelMaxOrderNo(group.getComId(),group.getOwner());
		group.setOrderNo(currentLevelMaxOrderNo+1);
		//个人分组主键
		Integer grpId = selfGroupDao.add(group);
		if(null!=userIds&&userIds.length>0){
			for (Integer userId : userIds) {
				GroupPersion groupPersion = new GroupPersion();
				//组与成员的关系
				groupPersion.setComId(group.getComId());
				groupPersion.setGrpId(grpId);
				groupPersion.setUserInfoId(userId);
				selfGroupDao.add(groupPersion);
			}
		}
		//添加积分
		jifenService.addJifen(group.getComId(),group.getOwner(), ConstantInterface.TYPE_USERGRP,
				"创建个人分组:"+group.getGrpName(),grpId);
		return grpId;
	}
	
	/**
	 * 分页查询个人分组信息
	 * @param group
	 * @return
	 */
	public List<SelfGroup> listUserGroup(UserInfo sesionUser, SelfGroup group) {
		List<SelfGroup> list = selfGroupDao.listUserGroup(group);
		if(null!=list && !list.isEmpty()){
			for(SelfGroup vo:list){
				vo.setListUser(selfGroupDao.listGroupUser(vo.getId(),sesionUser,null));
			}
		}
		return list;
	}
	
	/**
	 * 人员私有组  批量删除
	 * @param ids
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws CorruptIndexException 
	 */
	public void delSelfGroup(Integer[] ids,UserInfo userInfo) throws CorruptIndexException, IOException, ParseException {
		if (ids != null) {
			for (Integer id : ids) {
				selfGroupDao.delByField("groupPersion", "grpId", id);
				SelfGroup selfGroup = (SelfGroup) selfGroupDao.objectQuery(SelfGroup.class, id);
				//修改积分
				jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_USERGRPDEL,
						"删除个人分组:"+selfGroup.getGrpName(),id);
			}
		}
		selfGroupDao.delById(SelfGroup.class, ids);
	}
	/**
	 * 删除分组下组员
	 * @param grpId
	 * @param delUserId
	 * @param userInfo
	 */
	public void delGroupUser(Integer grpId,Integer delUserId,UserInfo userInfo) {
		SelfGroup selfGroup = this.getGroupById(grpId, userInfo);
		UserInfo delUser = userInfoService.getUserInfo(userInfo.getComId(),delUserId);
		//添加日志记录
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除分组'"+selfGroup.getGrpName()+"'下的'"+delUser.getUserName()+"'",
				ConstantInterface.TYPE_USER,userInfo.getComId(),userInfo.getOptIP());
		selfGroupDao.delByField("groupPersion",new String[]{"comId","grpId","userInfoId"},new Integer[]{userInfo.getComId(),grpId,delUserId});
	}
	
	/**
	 * 根据主键查找分组详细信息
	 * @param grpId
	 * @param comId 
	 * @return
	 */
	public SelfGroup getGroupById(Integer grpId, UserInfo sesionUser) {
		SelfGroup group = (SelfGroup) selfGroupDao.objectQuery(SelfGroup.class, grpId);
		if(null!=group){
			//取得分组成员
			group.setListUser(selfGroupDao.listGroupUser(grpId,sesionUser,null));
		}
		return group;
	}
	/**
	 * 分组成员
	 * @param grpId 分组主键
	 * @param comId 团队号
	 * @param onlySubState 是否只查询下属1是
	 * @return
	 */
	public List<UserInfo> listGroupUser(Integer grpId, UserInfo sessionUser,String onlySubState) {
		return selfGroupDao.listGroupUser(grpId,sessionUser,onlySubState);
	}
	
	/**
	 * 修改人员私有组
	 * @param userGroup
	 */
	public void updateSelfGroup(SelfGroup group,UserInfo sessionUser) {
		selfGroupDao.update(group);

		// 私有组下面的人员
		selfGroupDao.delByField("groupPersion", "grpId", group.getId());
		//分组成员
		List<UserInfo> userInfo = group.getListUser();
		if(null!=userInfo&&userInfo.size()>0){
			for (UserInfo user : userInfo) {
				GroupPersion groupPersion = new GroupPersion();
				//组与成员的关系
				groupPersion.setComId(group.getComId());
				groupPersion.setGrpId(group.getId());
				groupPersion.setUserInfoId(user.getId());
				
				selfGroupDao.add(groupPersion);
			}
		}
		//添加日志记录
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "修改分组信息\""+group.getGrpName()+"\"",
				ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		
	}
	
	/**
	 * 修改人员私有组
	 * @param userGroup
	 */
	public void updateSelfGroupPersion(SelfGroup group,UserInfo sessionUser) {
		// 私有组下面的人员
		selfGroupDao.delByField("groupPersion", "grpId", group.getId());
		//分组成员
		List<UserInfo> userInfo = group.getListUser();
		if(null!=userInfo&&userInfo.size()>0){
			for (UserInfo user : userInfo) {
				GroupPersion groupPersion = new GroupPersion();
				//组与成员的关系
				groupPersion.setComId(group.getComId());
				groupPersion.setGrpId(group.getId());
				groupPersion.setUserInfoId(user.getId());
				
				selfGroupDao.add(groupPersion);
			}
		}
		//添加日志记录
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "修改分组成员\""+group.getGrpName()+"\"",
				ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		
	}
	
	/**
	 * 修改人员私有组
	 * @param userGroup
	 */
	public void updateSelfGroupName(SelfGroup group,UserInfo sessionUser) {
		selfGroupDao.update(group);
		//添加日志记录
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "修改分组名称\""+group.getGrpName()+"\"",
				ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		
	}

	/**
	 * 设置个人常用人员显示
	 * @param groupId 分组主键
	 * @param userInfo
	 */
	public void initDefGrpToShow(Integer groupId, UserInfo userInfo) {
		UserOrganic userOrganic = new UserOrganic();
		userOrganic.setComId(userInfo.getComId());
		userOrganic.setUserId(userInfo.getId());
		if(CommonUtil.isNull(groupId)) {
			groupId = null;
		}
		userOrganic.setDefShowGrpId(groupId);
		selfGroupDao.update("update userOrganic a set a.defShowGrpId=:defShowGrpId where a.comid=:comId and a.userId=:userId",userOrganic);
	}
	
	
	
}
