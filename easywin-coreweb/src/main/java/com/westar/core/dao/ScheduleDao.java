package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ScheLog;
import com.westar.base.model.ScheTalk;
import com.westar.base.model.ScheUser;
import com.westar.base.model.Schedule;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class ScheduleDao extends BaseDao {

	/**
	 * 查询日程事件
	 * @param comId 企业号
	 * @param userId 被查看人员的主键
	 * @param startDate 日程时间起
	 * @param endDate 日程时间止
	 * @param modList 模块类型
	 * @param subType 查询下属 0查询自己 1查询下属 null查询所有
	 * @param userInfo 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Schedule> listEvents(Integer comId, Integer userId,String startDate, String endDate,
			 List<String> modList, String subType, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//任务中数据
		/*StringBuffer taskSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_TASK)>=0){
			taskSql.append("\n select a.comId,a.userId,a.title,a.scheStartDate,a.scheEndDate,a.isAllDay,");
			taskSql.append("\n a.isRepeat,a.repType,a.repDate,a.repEndType,a.repEndDate,a.username,");
			taskSql.append("\n a.busId,a.busType from ( ");
			
			taskSql.append("\n  select a.comId,a.executor userId,a.taskname title,");
			taskSql.append("\n   case when hand.recordcreatetime is null then substr(a.recordcreatetime,0,10) else substr(hand.recordcreatetime,0,10) end scheStartDate,");
			taskSql.append("\n   case when hand.handTimeLimit is null then ");
			taskSql.append("\n  case when a.dealTimeLimit is null then  substr('"+endDate+"',0,10) else substr(a.dealTimeLimit,0,10) end");
			taskSql.append("\n  else substr(hand.handTimeLimit,0,10) end  scheEndDate, ");
			taskSql.append("\n   1 isAllDay,0 isRepeat,null repType,null repDate,null repEndType,null repEndDate,c.username,");
			taskSql.append("\n  a.id busId,'"+ConstantInterface.TYPE_TASK+"' busType");
			taskSql.append("\n  from task a inner join userorganic b on a.executor=b.userid and a.comid=b.comid and b.enabled=1");
			taskSql.append("\n  inner join userinfo c on b.userid=c.id ");
			taskSql.append("\n  left join taskhandover hand on a.comid=hand.comid and a.id=hand.taskid ");
			taskSql.append("\n  where a.delstate=0 and a.state=1");
			this.addSqlWhere(userInfo.getComId(), taskSql, args, " and a.comId=?");
			taskSql.append("\n   and hand.id=(select max(h.id) from taskhandover h where h.comid=a.comid and a.id=h.taskid)");
			
			//任务的执行权限验证
			taskSql.append(" and (a.executor = ?  \n");
			args.add(userInfo.getId());
			//执行人上级权限验证
			taskSql.append("	or exists(\n");
			taskSql.append("		select sup.* from immediateSuper sup\n");
			taskSql.append("		where sup.comid=? and sup.leader=?\n");
			taskSql.append("		start with sup.creator=a.executor\n");
			taskSql.append("		connect by prior sup.leader = sup.creator\n");
			taskSql.append("	)\n");
			taskSql.append(")\n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			
			taskSql.append("\n )a where 1=1 ");
			taskSql.append("\n 		and( ");
			taskSql.append("\n 		(a.scheStartDate>=? and a.scheStartDate<=?) ");
			args.add(startDate);
			args.add(endDate);
			taskSql.append("\n 			or");
			taskSql.append("\n 		(a.scheEndDate>=? and a.scheEndDate<=?) ");
			args.add(startDate);
			args.add(endDate);
			taskSql.append("\n 			or");
			taskSql.append("\n 		(a.scheEndDate>=? and a.scheStartDate<=?) ");
			args.add(endDate);
			args.add(startDate);
			taskSql.append("\n 		) ");
			//指定有下属
			if(null!=subType && !"".equals(subType)){
				if("0".equals(subType)){//查询自己的
					this.addSqlWhere(userInfo.getId(), taskSql, args, " and a.userId=?");
				}else if("1".equals(subType)){//查询下属的
					taskSql.append(" and exists(select id from myLeaders where creator = a.userId and comId = ? and leader = ? and leader <> creator )");
					args.add(userInfo.getComId());
					args.add(userInfo.getId());
				}
			}
			if(userId>0){
				//指定有人员
				this.addSqlWhere(userId, taskSql, args, "and a.userId=?");
			}
		}*/
		//日程中数据
		StringBuffer scheSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_SCHEDULE)>=0){
			scheSql.append("\n select distinct a.comId,a.userId,a.title,a.scheStartDate,a.scheEndDate,a.isAllDay,");
			scheSql.append("\n a.isRepeat,a.repType,a.repDate,a.repEndType,a.repEndDate,a.username,");
			scheSql.append("\n a.id busId,'"+ConstantInterface.TYPE_SCHEDULE+"' busType from ( ");
			//查询公开的
			scheSql.append("\n select a.*,b.username ");
			scheSql.append("\n from schedule a inner join userorganic c on a.userid=c.userid and a.comid=c.comid and c.enabled=1");
			scheSql.append("\n inner join userinfo b on a.userid=b.id");
			scheSql.append("\n where a.publicType=2 ");
			this.addSqlWhere(userInfo.getComId(), scheSql, args, " and a.comId=?");
			
			scheSql.append("\n union all  ");
			//查询指定人员的
			scheSql.append("\n select a.*,b.username ");
			scheSql.append("\n from schedule a inner join userorganic c on a.userid=c.userid and a.comid=c.comid and c.enabled=1");
			scheSql.append("\n inner join userinfo b on a.userid=b.id");
			scheSql.append("\n inner join scheUser sche on a.id=sche.scheduleId and a.comid=sche.comid");
			scheSql.append("\n where 1=1 ");
			this.addSqlWhere(userInfo.getComId(), scheSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), scheSql, args, " and sche.userId=?");
			
			scheSql.append("\n union all  ");
			//查询自己查看的
			scheSql.append("\n select a.*,b.username ");
			scheSql.append("\n from schedule a inner join userinfo b on a.userid=b.id");
			scheSql.append("\n where a.publicType=1 ");
			this.addSqlWhere(userInfo.getComId(), scheSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), scheSql, args, " and a.userId=?");
			scheSql.append("\n union all  ");
			//查询上级查看的
			scheSql.append("\n select a.*,b.username ");
			scheSql.append("\n from schedule a inner join userorganic c on a.userid=c.userid and a.comid=c.comid and c.enabled=1");
			scheSql.append("\n inner join userinfo b on a.userid=b.id");
			scheSql.append("\n where a.publicType=0 ");
			this.addSqlWhere(userInfo.getComId(), scheSql, args, " and a.comId=?");
			scheSql.append("	and ( a.userId=?\n");
			args.add(userInfo.getId());
			scheSql.append("		or exists(\n");
			scheSql.append("				select id from myLeaders where creator = a.userId and leader = ? and comId = ? and creator <> leader \n");
			scheSql.append("		)\n");
			scheSql.append("	)\n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			scheSql.append("\n )a where 1=1 ");
			//不重复执行的
			scheSql.append("\n and ( a.isrepeat=0 or");
			//重复执行，不结束
			scheSql.append("\n 	(a.isrepeat=1 and a.rependtype=0  )");
			scheSql.append("\n 	or ");
			//重复执行，有截止限制
			scheSql.append("\n 	(a.isrepeat=1  and a.rependtype>0");
			scheSql.append("\n 		and( ");
			scheSql.append("\n 		(a.datatimes>=? and a.datatimes<=?) ");
			args.add(startDate);
			args.add(endDate);
			scheSql.append("\n 			or");
			scheSql.append("\n 		(a.datatimee>=? and a.datatimee<=?) ");
			args.add(startDate);
			args.add(endDate);
			scheSql.append("\n 			or");
			scheSql.append("\n 		(a.datatimee>=? and a.datatimes<=?) ");
			args.add(endDate);
			args.add(startDate);
			scheSql.append("\n 		)");
			scheSql.append("\n 	)");
			scheSql.append("\n 	)");
			//指定有下属
			if(null!=subType && !"".equals(subType)){
				if("0".equals(subType)){//查询自己的
					this.addSqlWhere(userInfo.getId(), scheSql, args, " and a.userId=?");
				}else if("1".equals(subType)){//查询下属的
					scheSql.append(" and exists(select id from myLeaders where creator = a.userId and comId = ? and leader = ? and leader <> creator )");
					args.add(userInfo.getComId());
					args.add(userInfo.getId());
				}
			}
			if(userId>0){
				//指定有人员
				this.addSqlWhere(userId, scheSql, args, "and a.userId=?");
			}
		}
		/*if(null==modList){
			sql.append(taskSql);
			sql.append("\n union all ");
			sql.append(scheSql);
		}else{
			for(Integer i=0;i<modList.size();i++){
				String modType = modList.get(i);
				switch (Integer.parseInt(modType)) {
				case 3:
					sql.append(taskSql);
					break;
				case 16:
					sql.append(scheSql);
					break;
				}
				if(i<modList.size()-1){
					sql.append("\n union all ");
				}
			}
		}*/
		return this.listQuery(scheSql.toString(), args.toArray(), Schedule.class);
	}

	/**
	 * 查询日程的参与人
	 * @param scheduleId 日程主键
	 * @param comId 企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ScheUser> listScheUser(Integer scheduleId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.userid,c.gender,c.username,d.filename imgName,d.uuid imgUuid");
		sql.append("\n from scheUser a inner join userorganic b on a.comid=b.comid and a.userid=b.userid and b.enabled=1");
		sql.append("\n inner join userinfo c on a.userid=c.id");
		sql.append("\n left join upfiles d on b.comid=d.comid and b.smallheadportrait=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(scheduleId, sql, args, " and a.scheduleId=?");
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), ScheUser.class);
	}

	/**
	 * 分页查询日程留言
	 * @param comId 企业号
	 * @param scheduleId 日程主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ScheTalk> listPagedScheTalks(Integer comId, Integer scheduleId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comid,a.talkContent,a.userId,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName");
		sql.append("\n  from scheTalk a inner join userinfo b on a.userid=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join scheTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n  left join userinfo d on e.userid=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(scheduleId, sql, args, " and a.scheduleId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.pagedQuery(sql.toString(),null ,args.toArray(), ScheTalk.class);
	}
	/**
	 * 查询日程留言
	 * @param comId 企业号
	 * @param scheduleId 日程主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ScheTalk> listScheTalks(Integer comId, Integer scheduleId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.recordCreateTime,a.id,a.parentid,a.comid,a.talkContent,a.userId,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName");
		sql.append("\n  from scheTalk a inner join userinfo b on a.userid=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join scheTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n  left join userinfo d on e.userid=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(scheduleId, sql, args, " and a.scheduleId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return this.listQuery(sql.toString(),args.toArray(), ScheTalk.class);
	}
	/**
	 * 查询日程指定留言主键的详细信息
	 * @param id 留言主键
	 * @param comId 企业号
	 * @return
	 */
	public ScheTalk getScheTalk(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.recordCreateTime,a.id,a.parentid,a.comid,a.talkContent,a.userId,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName");
		sql.append("\n  from scheTalk a inner join userinfo b on a.userid=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join scheTalk e on a.parentid=e.id and a.comid=e.comid ");
		sql.append("\n  left join userinfo d on e.userid=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return (ScheTalk) this.objectQuery(sql.toString(), args.toArray(), ScheTalk.class);
	}
	/**
	 * 删除日程留言
	 * @param id
	 * @param comId
	 */
	public void delScheTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from scheTalk a where a.comid =? and a.id in \n");
		sql.append("(select id from scheTalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 将子节点提高一级
	 * @param id 附件讨论的主键
	 * @param comId 企业号
	 */
	public void updateScheTalkParentId(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		ScheTalk schetalk = (ScheTalk) this.objectQuery(ScheTalk.class, id);
		//附件讨论的父节点主键
		Integer parentId = -1;
		if(null!=schetalk){
			parentId = schetalk.getParentId();
		}
		sql.append("update scheTalk set parentId=? where parentid = ? and comId = ?\n");
		args.add(parentId);
		args.add(id);
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 查询日程日志
	 * @param comId
	 * @param scheduleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ScheLog> listPagedScheLog(Integer comId, Integer scheduleId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from scheLog a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.scheduleId = ? \n");
		args.add(comId);
		args.add(scheduleId);
		return this.pagedQuery(sql.toString()," a.recordcreatetime desc", args.toArray(), ScheLog.class);
	}
	/**
	 * 获取团队日程主键集合
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Schedule> listScheduleOfAll(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id from schedule a where a.comid=?");
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(),Schedule.class);
	}

}
