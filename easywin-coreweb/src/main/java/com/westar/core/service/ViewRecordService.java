package com.westar.core.service;

import java.util.List;

import com.westar.base.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Item;
import com.westar.base.model.MsgShare;
import com.westar.base.model.Schedule;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.ViewRecordDao;

@Service
public class ViewRecordService {

	@Autowired
	ViewRecordDao viewRecordDao;
	
	/**
	 * 查看记录
	 * 
	 * @param userInfo
	 * @param viewRecord
	 */
	public void addViewRecord(UserInfo userInfo, ViewRecord viewRecord) {
		ViewRecord viewRecordT = viewRecordDao.getViewRecord(viewRecord);
		if (null == viewRecordT) {
			// 保存最近一次查看记录
			viewRecordDao.add(viewRecord);
		}else{
			//修改查看记录
			viewRecordDao.updateViewRecord(viewRecordT);
		}
	}

	/**
	 * 浏览的人员集合
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            业务主键
	 * @return
	 */
	public List<ViewRecord> listViewRecord(UserInfo userInfo, String busType,
			Integer busId) {
		if (null == busId || busId == 0) {
			return null;
		}
		List<ViewRecord> list = viewRecordDao.listViewRecord(userInfo, busType,
				busId);
		if(busType.equals("1")){//当业务类型为分享的时候查询未读的人员
			MsgShare msg = (MsgShare) viewRecordDao.objectQuery(MsgShare.class, busId);
			if(msg != null){
				if(msg.getScopeType().equals(0)){//当范围为所有人的时候只需要查看了的
					
				}else{
					List<ViewRecord> listMsg = viewRecordDao.listViewRecordMsg(userInfo, busType,busId);
					if(listMsg != null && listMsg.size() > 0){
						for (int i = 0; i < listMsg.size(); i++) {
							list.add(listMsg.get(i));
						}
					}
				}
			}
			
			
		}else if(ConstantInterface.TYPE_WEEK.equals(busType)){//当业务类型为周报的时候查询未读人员
			List<ViewRecord> listRep = viewRecordDao.listViewRecordRep(userInfo, busType,busId);
			if(listRep != null && listRep.size() > 0){
				for (int i = 0; i < listRep.size(); i++) {
					list.add(listRep.get(i));
				}
			}
		}else if(ConstantInterface.TYPE_DAILY.equals(busType)){//当业务类型为分享的时候查询未读人员
			List<ViewRecord> listRep = viewRecordDao.listViewDaily(userInfo, busType,busId);
			if(listRep != null && listRep.size() > 0){
				for (int i = 0; i < listRep.size(); i++) {
					list.add(listRep.get(i));
				}
			}
		}else if(ConstantInterface.TYPE_ITEM.equals(busType)){//当业务类型为项目的时候查询未读人员
			Item item = (Item) viewRecordDao.objectQuery(Item.class, busId);
			if(item != null && item.getPubState().equals(0)) {//当公开私有属性为私有的时候
				List<ViewRecord> listItem = viewRecordDao.listViewRecordItem(userInfo, busType,busId);
				if(listItem != null && listItem.size() > 0){
					for (int i = 0; i < listItem.size(); i++) {
						list.add(listItem.get(i));
					}
				}
			}
			
		}else if(ConstantInterface.TYPE_CRM.equals(busType)){//当业务类型为客户的时候查询未读人员
			List<ViewRecord> listCus = viewRecordDao.listViewRecordCus(userInfo, busType,busId);
			if(listCus != null && listCus.size() > 0){
				for (int i = 0; i < listCus.size(); i++) {
					list.add(listCus.get(i));
				}
			}
		}else if(ConstantInterface.TYPE_ANNOUNCEMENT.equals(busType)){//当业务类型为公告的时候查询未读人员
			List<ViewRecord> listAn = viewRecordDao.listViewRecordAn(userInfo, busType,busId);
			if(listAn != null && listAn.size() > 0){
				for (int i = 0; i < listAn.size(); i++) {
					list.add(listAn.get(i));
				}
			}
			
		}else if(busType.equals(ConstantInterface.TYPE_SCHEDULE)){//当业务类型为日程的时候查询未读人员
			Schedule schedule = (Schedule)viewRecordDao.objectQuery(Schedule.class, busId);
			Boolean showLeader = false;
			if(schedule != null && schedule.getPublicType().equals(0)) {//当公开程度为上级可见
				showLeader = true;
			}
			List<ViewRecord> listSch = viewRecordDao.listViewRecordSch(userInfo, busType,busId,showLeader);
			if(listSch != null && listSch.size() > 0){
				for (int i = 0; i < listSch.size(); i++) {
					list.add(listSch.get(i));
				}
			}
			
		}else if(ConstantInterface.TYPE_LEARN.equals(busType) || ConstantInterface.TYPE_FILE.equals(busType)){//当业务类型为在线学习或者文档的时候查询未读人员
			List<ViewRecord> listVi = viewRecordDao.listViewRecordVi(userInfo, busType,busId);
			if(listVi != null && listVi.size() > 0){
				for (int i = 0; i < listVi.size(); i++) {
					list.add(listVi.get(i));
				}
			}
			
		}else if(ConstantInterface.TYPE_MEETING.equals(busType)){//当业务类型为会议的时候查询未读人员
			List<ViewRecord> listMeetings = viewRecordDao.listViewRecordMeetings(userInfo, busType,busId);
			if(listMeetings != null && listMeetings.size() > 0){
				for (int i = 0; i < listMeetings.size(); i++) {
					list.add(listMeetings.get(i));
				}
			}
			
		}
		return list;
	}
	
	/**
	 * 查看记录
	 * 
	 * @param userInfo
	 * @param viewRecord
	 */
	public String addViewRecordReturn(UserInfo userInfo, ViewRecord viewRecord) {
		ViewRecord viewRecordT = viewRecordDao.getViewRecord(viewRecord);
		if (null == viewRecordT) {
			// 保存最近一次查看记录
			viewRecordDao.add(viewRecord);
			return ConstantInterface.TYPE_ADD;
		}else{
			//修改查看记录
			viewRecordDao.updateViewRecord(viewRecordT);
			return ConstantInterface.TYPE_UPDATE;
		}
	}

	/**
	 * 删除查看记录信息
	 * @param sessionUser 
	 * @param shares
	 * @param busId 
	 * @param busType
	 */
	public void delViewRecord(UserInfo sessionUser, List<UserInfo> shares, Integer busId, String busType) {
		if(null!=shares && !shares.isEmpty()){
			for (UserInfo userInfo : shares) {
				viewRecordDao.delByField("viewRecord", new String[]{"comId","userId","busType","busId"},
						new Object[]{sessionUser.getComId(),userInfo.getId(),busType,busId});
			}
		}
		
	}
	
	
	
}
