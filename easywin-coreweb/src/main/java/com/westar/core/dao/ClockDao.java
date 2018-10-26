package com.westar.core.dao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Clock;
import com.westar.base.model.ClockPerson;
import com.westar.base.model.ClockWay;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;

@Repository
public class ClockDao extends BaseDao {

	/**
	 * 信息发送方式
	 * @param id
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ClockWay> listClockWay(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.zvalue sendWayV,a.code sendWay,case when b.id is null then 0 else 1 end checkState");
		sql.append("\n from datadic a left join clockWay b on b.sendWay=a.code and b.comid=? and b.clockid=?");
		args.add(comId);
		args.add(id);
		sql.append("\n where a.type='clockWay' and a.parentid>0  order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), ClockWay.class);
	}

	/**
	 * 取出距离现在指定时间内执行的定时提醒
	 * @param nowTime 当前时间
	 * @param delayTime 时间区间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Clock> listAlarmClocks(String nowTime,Integer delayTime) {
		String lateDateTime = DateTimeUtil.addDate(nowTime, 
				DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.MINUTE, delayTime);
		//精确到分钟
		lateDateTime = lateDateTime.substring(0, lateDateTime.lastIndexOf(":"));
		//精确到分钟
		nowTime = nowTime.substring(0, nowTime.lastIndexOf(":"));
		
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from clock a inner join userorganic b on a.comid=b.comid and a.userid=b.userid ");
		sql.append("\n inner join organic c on b.comid=c.orgnum ");
		//人员和企业必须是激活状态
		sql.append("\n where a.clockIsOn=1 and a.executeState=0 and b.enabled=1 and c.enabled=1");
		this.addSqlWhere(nowTime, sql, args, " and a.clocknextdate>=?");
		this.addSqlWhere(lateDateTime, sql, args, " and a.clocknextdate<?");
		sql.append("\n order by a.clocknextdate");
		return this.listQuery(sql.toString(), args.toArray(), Clock.class);
	}

	/**
	 * 选出需要删除的闹铃
	 * @param comId 企业号
	 * @param userId 人员主键
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Clock> listUserClockForDel(Integer comId,Integer userId, Integer busId,
			String busType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from clock a where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		this.addSqlWhere(busId, sql, args, " and a.busId=?");
		this.addSqlWhere(busType, sql, args, " and a.busType=?");
		return this.listQuery(sql.toString(), args.toArray(),Clock.class);
	}

	/**
	 * 取得在服务器关闭的时候未来得及执行的定时提醒
	 * @param nowTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Clock> listClockUndone(String nowTime) {
		//精确到分钟
		nowTime = nowTime.substring(0, nowTime.lastIndexOf(":"));
		
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from clock a where a.clockIsOn=1 and a.clockRepType>0");
		this.addSqlWhere(nowTime, sql, args, " and a.clocknextdate<=?");
		sql.append("\n order by a.clocknextdate");
		return this.listQuery(sql.toString(), args.toArray(),Clock.class);
	}

	/**
	 * 分页查询闹铃
	 * @param clock闹铃属性条件
	 * @param modList模块集合 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Clock> listPagedClock(Clock clock, List<String> modList) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		//任务闹铃
		StringBuffer taskSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_TASK)>=0){
			taskSql.append("\n select a.* ,b.taskName entyname from clock a");
			taskSql.append("\n inner join task b on a.comid=b.comid and a.busid=b.id and a.bustype='"+ConstantInterface.TYPE_TASK+"'");
			taskSql.append("\n where 1=1");
			this.addSqlWhere(clock.getComId(), taskSql, args, " and a.comid=? ");
			this.addSqlWhere(clock.getUserId(), taskSql, args, " and a.userId=? ");
			this.addSqlWhere(clock.getClockRepType(), taskSql, args, " and a.clockRepType=? ");
		}
		//项目闹铃
		StringBuffer itemSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_ITEM)>=0){
			itemSql.append("\n select a.* ,b.itemName entyname from clock a");
			itemSql.append("\n inner join item b on a.comid=b.comid and a.busid=b.id and a.bustype='"+ConstantInterface.TYPE_ITEM+"'");
			itemSql.append("\n where 1=1");
			this.addSqlWhere(clock.getComId(), itemSql, args, " and a.comid=? ");
			this.addSqlWhere(clock.getUserId(), itemSql, args, " and a.userId=? ");
			this.addSqlWhere(clock.getClockRepType(), itemSql, args, " and a.clockRepType=? ");
		}
		//客户闹铃
		StringBuffer crmSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_CRM)>=0){
			crmSql.append("\n select a.* ,b.customername entyname from clock a");
			crmSql.append("\n inner join customer b on a.comid=b.comid and a.busid=b.id and a.bustype='"+ConstantInterface.TYPE_CRM+"'");
			crmSql.append("\n where 1=1");
			this.addSqlWhere(clock.getComId(), crmSql, args, " and a.comid=? ");
			this.addSqlWhere(clock.getUserId(), crmSql, args, " and a.userId=? ");
			this.addSqlWhere(clock.getClockRepType(), crmSql, args, " and a.clockRepType=? ");
		}
		//普通闹铃
		StringBuffer normalSql = new StringBuffer();
		if(null==modList || modList.indexOf("101")>=0){
			normalSql.append("\n select a.*,null entyname from clock a where a.bustype='0'");
			this.addSqlWhere(clock.getComId(), normalSql, args, " and a.comid=? ");
			this.addSqlWhere(clock.getUserId(), normalSql, args, " and a.userId=? ");
			this.addSqlWhere(clock.getClockRepType(), normalSql, args, " and a.clockRepType=? ");
		}
		sql.append("\n select a.* from ( ");
		sql.append("\n select a.*, ");
		sql.append("\n case when a.clockRepType=0 and  a.clockNextDate||':00'<='"+nowDateTime+"' then 1");
		sql.append("\n else 0 end as clockState ");
		sql.append("\n from(");
		
		if(null==modList){
			sql.append(taskSql);
			sql.append("\n union all");
			sql.append(itemSql);
			sql.append("\n union all");
			sql.append(crmSql);
			sql.append("\n union all");
			sql.append(normalSql);
			
		}else{
			for(Integer i=0;i<modList.size();i++){
				String modType = modList.get(i);
				switch (Integer.parseInt(modType)) {
					case 3:
						sql.append(taskSql);
						break;
					case 5:
						sql.append(itemSql);
						break;
					case 12:
						sql.append(crmSql);
						break;
					case 101:
						sql.append(normalSql);
						break;
					default:
						break;
					}
				if(i<modList.size()-1){
					sql.append("\n union all ");
				}
			}
		}
		
		
		sql.append("\n)a where 1=1 ");
		sql.append("\n)a where 1=1 ");
		this.addSqlWhere(clock.getStartDate(), sql, args, " and a.clockNextDate>=? and a.clockState=0");
		this.addSqlWhere(clock.getEndDate(), sql, args, " and a.clockNextDate<=? and a.clockState=0");
		this.addSqlWhereLike(clock.getContent(), sql, args, " and (a.content||a.entyname) like ?");
		return this.pagedQuery(sql.toString(), "clockState,clockNextDate", args.toArray(), Clock.class);
	}

	/**
	 * 闹铃集合
	 * @param comId 企业号
	 * @param userId 操作人员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Clock> listClockForOne(Integer comId, Integer userId,
			Integer busId, String busType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//当前时间
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		sql.append("\n select a.*, ");
		sql.append("\n case when a.clockRepType=0 and  a.clockNextDate||':00'<='"+nowDateTime+"' then 1");
		sql.append("\n else 0 end as clockState ");
		sql.append("\n from clock a");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comid=? ");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		this.addSqlWhere(busType, sql, args, " and a.busType=? ");
		this.addSqlWhere(busId, sql, args, " and a.busId=? ");
		sql.append("\n order by clockState,a.id desc ");
		return this.listQuery(sql.toString(), args.toArray(), Clock.class);
	}

	/**
	 * 直接合并闹铃
	 * @param comId 企业号
	 * @param busOldId 待整合的项目主键
	 * @param busId 合并后的项目主键
	 * @param busType 业务类型
	 * 
	 */
	public void compressClock(Integer comId, Integer busOldId, Integer busId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update clock set busId=? where comid=? and busId=? and busType=?");
		args.add(busId);
		args.add(comId);
		args.add(busOldId);
		args.add(busType);
		this.excuteSql(sql.toString(), args.toArray());
		
	}

	/**
	 * 特定人员的闹铃
	 * @param userId 人员主键
	 * @param comId 企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Clock> listUserClock(Integer userId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from clock a where 1=1");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(),Clock.class);
	}

	/**
	 * 闹铃对象
	 * @param clockId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ClockPerson> listClockPerson(Integer clockId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.comId,a.clockId,a.userId,b.userName from clockPerson a ");
		sql.append("\n left join userInfo b on a.userId=b.id  where 1=1");
		this.addSqlWhere(clockId, sql, args, " and a.clockId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n union ");
		sql.append("\n select a.comId,a.id clockId,a.userId,b.userName from clock a  ");
		sql.append("\n left join userInfo b on a.userId=b.id  where 1=1");
		this.addSqlWhere(clockId, sql, args, " and a.Id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.listQuery(sql.toString(), args.toArray(),ClockPerson.class);
	}
	/**
	 * 重置只执行一次在服务器关闭期间有未执行的
	 */
	public void updateResetClock() {
		StringBuffer sql = new StringBuffer();
		sql.append("\n update clock a set a.executeState=0  where a.clockIsOn=1 and a.executeState=1");
		this.excuteSql(sql.toString(), null);
	}
}
