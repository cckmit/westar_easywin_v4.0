package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.MeetingRoom;
import com.westar.base.model.RoomApply;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;

@Repository
public class MeetingRoomDao extends BaseDao {

	/**
	 * 分页查询会议室信息
	 * @param meetingRoom
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MeetingRoom> listPagedMeetingRoom(MeetingRoom meetingRoom,
			UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,c.userName mamagerName,c.gender mamagerGender,d.uuid mamagerUuid,d.fileName mamagerImgName ");
		sql.append("\n from meetingRoom a  left join userorganic b on a.comid=b.comid and a.mamager=b.userid and b.enabled=1");
		sql.append("\n left join userinfo c on b.userid=c.id");
		sql.append("\n left join upfiles d on b.bigHeadPortrait=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		return this.pagedQuery(sql.toString(), " a.id asc", args.toArray(), MeetingRoom.class);
	}

	/**
	 * 通过主键取得会议室详情
	 * @param id
	 * @param comId
	 * @return
	 */
	public MeetingRoom getMeetingRoomById(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,c.userName mamagerName,c.gender mamagerGender,d.uuid mamagerUuid,d.fileName mamagerImgName,f.fileName roomPicName,f.uuid roomPicUuid ");
		sql.append("\n from meetingRoom a  left join userorganic b on a.comid=b.comid and a.mamager=b.userid and b.enabled=1");
		sql.append("\n left join userinfo c on b.userid=c.id");
		sql.append("\n left join upfiles d on b.bigHeadPortrait=d.id");
		sql.append("\n left join upfiles f on a.roomPicId=f.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		return (MeetingRoom) this.objectQuery(sql.toString(), args.toArray(), MeetingRoom.class);
	}

	/**
	 * 将已有的默认会议室置空
	 * @param comId 企业号
	 */
	public void deleteDefault(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append(" update meetingRoom set isDefault=0 where comid=? and isDefault=1");
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 会议室列表用于申请
	 * @param comId 企业号
	 * @param startDate
	 * @param endDate
	 * @param roomId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MeetingRoom> listRoomForApply(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		
		sql.append("\n select a.* from meetingRoom a ");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		sql.append("\n order by a.id ");
		return this.listQuery(sql.toString(),args.toArray(), MeetingRoom.class);
	}

	/**
	 * 取得所选时间会议室申请情况
	 * @param comId 企业号
	 * @param chooseDate 选取的时间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RoomApply> listRoomApplyed(UserInfo userInfo, String chooseDate,Integer meetId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		sql.append("\n select a.*,b.username,");
		sql.append("\n case when a.userId="+userInfo.getId()+" and a.meetingId="+meetId+" then 0 else 1 end status,");
		//会议已结束
		sql.append("\n case when a.endDate<'"+nowDateTime+"' then 3 else  ");
		//会议已开始1 未开始2
		sql.append("\n case when a.startDate<='"+nowDateTime+"' then 2 else 1 end  ");
		sql.append("\n end timeOut ");
		sql.append("\n from roomApply a ");
		sql.append("\n inner join userinfo b on a.userid=b.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		sql.append("\n and (");
		//会议开始时间或结束时间在指定的时间范围内
		this.addSqlWhere(chooseDate, sql, args, " substr(a.startDate,0,10)=?");
		this.addSqlWhere(chooseDate, sql, args, " or substr(a.endDate,0,10)=?");
		//会议时间需要经过指定时间
		sql.append("\n or (");
		this.addSqlWhere(chooseDate, sql, args, " a.startDate<?");
		this.addSqlWhere(chooseDate, sql, args, " and a.endDate>?");
		sql.append("\n )");
		sql.append("\n )");
		sql.append("\n order by a.id ");
		return this.listQuery(sql.toString(), args.toArray(), RoomApply.class);
	}

	/**
	 * 验证该会议室时间段是否被占用
	 * @param comId 企业号
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param roomId 会议室主键
	 * @param meetingId 
	 * @return
	 */
	public Boolean checkRoomDisabled(Integer comId, String startDate,
			String endDate, Integer roomId, Integer meetingId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(a.id)　from roomApply a where 1=1");
		this.addSqlWhere(roomId, sql, args, " and a.roomId=?");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId<>?");
		sql.append("\n 		and( ");
		//占用已申请的开始时间
		sql.append("\n 		(a.startDate>=? and a.startDate<=?) ");
		args.add(startDate);
		args.add(endDate);
		sql.append("\n 			or");
		//占用已申请的结束时间
		sql.append("\n 		(a.endDate>=? and a.endDate<=?) ");
		args.add(startDate);
		args.add(endDate);
		sql.append("\n 			or");
		//占用已申请的时间
		sql.append("\n 		(a.endDate>=? and a.startDate<=?) ");
		args.add(endDate);
		args.add(startDate);
		sql.append("\n 		) ");
		//有则被占用
		return this.countQuery(sql.toString(), args.toArray())>0?false:true;
	}
	/**
	 * 分页查询会议室申请
	 * @param userInfo 当前操纵人员
	 * @param roomApply 会议室申请属性
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RoomApply> listPagedRoomApply(UserInfo userInfo,
			RoomApply roomApply) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		sql.append("\n select a.*,d.username managerName,b.roomname,c.title meetTitle,　");
		//会议已结束
		sql.append("\n case when a.endDate<'"+nowDateTime+"' then 3 else  ");
		//会议已开始1 未开始2
		sql.append("\n case when a.startDate<='"+nowDateTime+"' then 2 else 1 end ");
		sql.append("\n end timeOut ");
		sql.append("\n from roomApply a ");
		sql.append("\n inner join meetingroom b on a.comid=b.comid and a.roomId=b.id");
		sql.append("\n left join meeting c on a.comid=c.comid and a.meetingid=c.id");
		sql.append("\n left join userinfo d on b.mamager=d.id");
		sql.append("\n  where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=?");
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(), RoomApply.class);
	}
	/**
	 * 查询当前用户负责的会议室申请
	 * @param comId 企业号
	 * @param userId 当前操作员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RoomApply> listUserRoomApply(Integer comId,
			Integer userId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* ");
		sql.append("\n from roomApply a ");
		sql.append("\n  where (a.state=0 or a.state=2) ");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		return this.listQuery(sql.toString(), args.toArray(), RoomApply.class);
	}
	/**
	 * 查询用于审核的会议申请
	 * @param userInfo
	 * @param roomApply
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RoomApply> listPagedApplyForCheck(UserInfo userInfo,
			RoomApply roomApply) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//当前时间
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		sql.append("\n select a.*,d.username,b.roomname,c.title meetTitle,　");
		//会议已结束
		sql.append("\n case when a.endDate<'"+nowDateTime+"' then 3 else  ");
		//会议已开始1 未开始2
		sql.append("\n case when a.startDate<='"+nowDateTime+"' then 2 else 1 end ");
		sql.append("\n end timeOut ");
		sql.append("\n from roomApply a ");
		sql.append("\n inner join meetingroom b on a.comid=b.comid and a.roomId=b.id");
		sql.append("\n left join meeting c on a.comid=c.comid and a.meetingid=c.id");
		sql.append("\n left join userinfo d on a.userId=d.id");
		sql.append("\n  where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and b.mamager=?");
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(), RoomApply.class);
	}
	/**
	 * 是否为会议室管理人员
	 * @param comId 企业号
	 * @param userId 操作人员
	 * @return
	 */
	public Integer countManageRoom(Integer comId, Integer userId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(a.id)　from meetingRoom a ");
		sql.append("\n  where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(userId, sql, args, " and a.mamager=?");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 查询用于删除的会议申请
	 * @param comId
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RoomApply> listRoomApplyFordel(Integer comId, Integer[] ids) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.comid,a.roomId,a.meetingId from roomApply a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhereIn(ids, sql, args, " and a.id in ?");
		return this.listQuery(sql.toString(), args.toArray(), RoomApply.class);
	}

	/**
	 * 取得会议的会议室申请
	 * @param comId
	 * @param meetingId
	 * @return
	 */
	public RoomApply getMeetRoomApply(Integer comId, Integer meetingId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from roomApply a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(meetingId, sql, args, " and a.meetingId=?");
		return (RoomApply) this.objectQuery(sql.toString(), args.toArray(), RoomApply.class);
	}

	/**
	 * 取得默认选择的会议室
	 * @param comId 企业号
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param roomId 会议室主键
	 * @param exceptRoomId需要排除的会议室
	 * @return
	 */
	public MeetingRoom getDefaultRoom(Integer comId, String startDate,
			String endDate, Integer roomId, Integer exceptRoomId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select rownum,outter.* from (");
		//已被申请
		sql.append("\n select rownum neworder, case when a.id is null then 1 else 0 end order1,");
		//需要排除
		sql.append("\n case when b.id="+exceptRoomId+" then 0 else 1 end order2,");
		//页面已选择的
		if(roomId>0){
			sql.append("\n case when b.id="+roomId+" then 1 else 0 end orderT,");
		}
		//默认的
		sql.append("\n b.isdefault  order3,");
		sql.append("\n b.*  from (");
		sql.append("\n select a.*　from roomApply a where 1=1");
		sql.append("\n 		and( ");
		//占用已申请的开始时间
		sql.append("\n 		(a.startDate>? and a.startDate<?) ");
		args.add(startDate);
		args.add(endDate);
		sql.append("\n 			or");
		//占用已申请的结束时间
		sql.append("\n 		(a.endDate>? and a.endDate<?) ");
		args.add(startDate);
		args.add(endDate);
		sql.append("\n 			or");
		//占用已申请的时间
		sql.append("\n 		(a.endDate>? and a.startDate<?) ");
		args.add(endDate);
		args.add(startDate);
		sql.append("\n 		) ");
		sql.append("\n )a right join meetingroom b on a.comid=b.comid and a.roomid=b.id");
		if(roomId>0){
			sql.append("\n order by order1 desc,order2 desc,orderT desc,order3 desc,b.id asc");
		}else{
			sql.append("\n order by order1 desc,order2 desc,order3 desc,b.id asc");
		}
		sql.append("\n )outter");
		if(roomId>0){
			sql.append("\n where order1*order2*orderT=1 and rownum=1");
		}else{
			sql.append("\n where order1*order2=1 and rownum=1");
		}
		return (MeetingRoom) this.objectQuery(sql.toString(), args.toArray(), MeetingRoom.class);
	}
	/**
	 * 重新设置待办人员
	 * @param comId 企业号
	 * @param userId 会议室原来负责人 
	 * @param mamager 会议室现在负责人
	 * @param meetingId 会议主键
	 */
	public void updateMeetUserForTodo(Integer comId, Integer userId,
			Integer mamager, Integer roomId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("update todayworks a set a.userid=?,readstate=0 where a.comid=? and a.userid=? and a.busType=?");
		sql.append("\n  and exists(");
		sql.append("\n  select meet.meetingAddrId from meeting meet ");
		sql.append("\n where meet.comid=? and meet.roomType=0 and meet.Id=a.busid and meet.meetingAddrId=?");
		sql.append("\n )");
		args.add(mamager);
		args.add(comId);
		args.add(userId);
		args.add(ConstantInterface.TYPE_MEETROOM);
		args.add(comId);
		args.add(roomId);
		this.excuteSql(sql.toString(), args.toArray());
	}
}
