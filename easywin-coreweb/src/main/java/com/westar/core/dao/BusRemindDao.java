package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.BusRemind;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;

@Repository
public class BusRemindDao extends BaseDao {

	/**
	 * 
	 * @param sessionUser
	 * @param busId
	 * @param busType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusRemind> listPagedBusRemindForSingle(UserInfo sessionUser,
			Integer busId, String busType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("select a.*,b.userName  from busRemind a");
		sql.append("\n inner join userinfo b on a.userId = b.id");
		sql.append("\n where a.comId = ?");
		args.add(sessionUser.getComId());
		this.addSqlWhere(busId, sql, args, "\n and a.busId = ?");
		this.addSqlWhere(busType, sql, args, "\n and a.busType = ?");
		return this.pagedQuery(sql.toString(), "a.id desc", args.toArray(),
				BusRemind.class);
	}

	/**
	 * 查询需要提醒的人员信息，用于发送短信
	 * 
	 * @param comId
	 *            团队号
	 * @param taskReminderId
	 *            提醒的主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listRemindUser(Integer comId, Integer taskReminderId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		// 事项参与人
		sql.append("select b.id,b.email,b.wechat,b.qq,b.userName,b.movePhone from BusRemindUser a \n");
		sql.append("inner join userinfo b on  a.userId = b.id \n");
		sql.append("inner join userorganic d on  a.comid=d.comid and b.id=d.userid and d.enabled=1 \n");
		sql.append("where a.comid = ? and a.id = ? \n");
		args.add(comId);
		args.add(taskReminderId);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 查询催办信息
	 * 
	 * @param busReminder
	 * @return
	 */
	public BusRemind queryTaskRemind(BusRemind busReminder) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append(" select a.*,b.userName,c.uuid,b.gender,c.filename");
		sql.append("\n from busRemind a");
		sql.append("\n inner join userinfo b on a.userId = b.id");
		sql.append("\n inner join userOrganic bb on b.id = bb.userId and a.comId = bb.comId");
		sql.append("\n left join upfiles c on bb.mediumHeadPortrait = c.id");

		sql.append("\n where 1 = 1");

		this.addSqlWhere(busReminder.getId(), sql, args, "\n and a.id = ?");
		this.addSqlWhere(busReminder.getBusId(), sql, args,
				"\n and a.busId = ?");
		this.addSqlWhere(busReminder.getBusType(), sql, args,
				"\n and a.busType = ?");
		this.addSqlWhere(busReminder.getComId(), sql, args,
				"\n and a.comId = ?");
		return (BusRemind) this.objectQuery(sql.toString(), args.toArray(),
				BusRemind.class);
	}

	/**
	 * 查询催办人员
	 * 
	 * @param sessionUser
	 * @param busRemindId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusRemindUser> listBusRemindUser(UserInfo sessionUser,
			Integer busRemindId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("select a.*,b.userName,c.uuid,b.gender,c.filename from busRemindUser a");
		sql.append("\n left join busRemind aa on a.busRemindId = aa.id");
		sql.append("\n inner join userinfo b on a.userId = b.id");
		sql.append("\n inner join userOrganic bb on b.id = bb.userId and bb.comId = aa.comId");
		sql.append("\n left join upfiles c on bb.mediumHeadPortrait = c.id");
		sql.append("\n where 1 = 1");

		this.addSqlWhere(busRemindId, sql, args, "\n and aa.id = ?");
		return this.listQuery(sql.toString(), args.toArray(),
				BusRemindUser.class);
	}

	/**
	 * 查询个人被催办记录
	 * 
	 * @param busRemind
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusRemind> listPagedSelfBusRemind(BusRemind busRemind,
			UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*");
		sql.append("\n ,case when nvl(u.id,0)=0 then '系统自动催办' else u.userName end userName  ");
		sql.append("\n from busRemind a ");
		sql.append("\n inner join busRemindUser reminduser on a.id=reminduser.busRemindId and a.comId=reminduser.comId");
		sql.append("\n left join userInfo u on a.userId=u.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args,
				"\n and reminduser.userId=?");

		// 查询创建时间段
		this.addSqlWhere(busRemind.getStartDate(), sql, args,
				" and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(busRemind.getEndDate(), sql, args,
				" and substr(a.recordcreatetime,0,10)<=?");

		this.addSqlWhereLike(busRemind.getBusModName(), sql, args,
				"\n and a.busModName like ?");

		return this.pagedQuery(sql.toString(), " a.recordCreateTime desc",
				args.toArray(), BusRemind.class);
	}
	/**
	 * 查询单个人员在指定模块的催办信息
	 * 
	 * @param userInfo
	 *            当前操作人员
	 * @param busRemind
	 *            催办条件查询
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageBean<BusRemind> listPagedBusRemindForBus(UserInfo userInfo,
			BusRemind busRemind) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*");
		sql.append("\n ,case when nvl(u.id,0)=0 then '系统自动催办' else u.userName end userName  ");
		sql.append("\n from busRemind a ");
		sql.append("\n inner join busRemindUser reminduser on a.id=reminduser.busRemindId and a.comId=reminduser.comId");
		sql.append("\n left join userInfo u on a.userId=u.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(busRemind.getRemindUserId(), sql, args,
				"\n and reminduser.userId=?");
		this.addSqlWhere(busRemind.getBusId(), sql, args, "\n and a.busId=?");
		this.addSqlWhere(busRemind.getBusType(), sql, args,
				"\n and a.busType=?");
		
		//查询创建时间段
		this.addSqlWhere(busRemind.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(busRemind.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		return this.pagedBeanQuery(sql.toString(), " a.recordCreateTime desc",
				args.toArray(), BusRemind.class);
	}

}
