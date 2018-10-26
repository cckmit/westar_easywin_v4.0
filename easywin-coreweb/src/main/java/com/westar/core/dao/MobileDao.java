package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.TodayWorks;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.MsgNoRead;
import com.westar.base.util.ConstantInterface;
import com.westar.core.web.DataDicContext;

/**
 * 
 * 描述: 手机端数据查询
 * @author alan
 * @date 2018年8月29日 下午3:34:03
 */
@Repository
public class MobileDao extends BaseDao {

		/**
	 * 
	 * 获取模块消息提醒数目（待办）
	 * @param comId 企业号
	 * @param userId 当前用户主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgNoRead> countToDoByType(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select busType,count(*) noReadNum from (");

		//任务模块
		sql.append("\n	 select case when a.isclock=1 then '9999' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_TASK+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//周报模块
		sql.append("\n union all");
		sql.append("\n select a.busType from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id ");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_WEEK+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//分享模块
//		sql.append("\n union all");
//		sql.append("\n select a.busType from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id ");
//		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_DAILY+"' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append(" and (a.userId=? or a.userId=0)");
//		args.add(userId);
		//会议确认
		sql.append("\n union all");
		sql.append("\n select a.busType from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id ");
		sql.append("\n where a.busSpec=1 ");
		this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP},
				sql, args, "\n and a.busType in ? ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//加入记录模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_APPLY+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//审批模块
		sql.append("\n	 union all");
		sql.append("\n	 select case when a.isclock=1 then '9999' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id and sp.flowState=1 ");
		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//会议申请
		sql.append("\n union all");
		sql.append("\n select a.busType from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id ");
		sql.append("\n where a.busSpec=1 and a.readState ='0' and a.busType='"+ConstantInterface.TYPE_MEETROOM+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//完结审批模块
		sql.append("\n	 union all");
		sql.append("\n	 select case when a.isclock=1 then '9999' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id ");
		sql.append("\n where a.busspec=1 and a.readState =0 and a.busType='"+ConstantInterface.TYPE_SP_END+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//以下归类其它
		
		//其它模块
		sql.append("\n	 union all");
		sql.append("\n	 select '9999' as busType ");
		sql.append("\n	 from todayWorks a ");
		sql.append("\n  where a.busspec=1 and a.readState=0");

		this.addSqlWhereIn(new Object[]{
				ConstantInterface.TYPE_TASK,ConstantInterface.TYPE_WEEK,ConstantInterface.TYPE_DAILY,
				ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP,ConstantInterface.TYPE_APPLY,
				ConstantInterface.TYPE_FLOW_SP},
				sql, args, "\n and a.busType not in ? ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  and (a.userId=? or a.userId=0)");
		args.add(userId);
		sql.append("\n	 )a group by busType");
		return this.listQuery(sql.toString(), args.toArray(), MsgNoRead.class);
	}
	/**
	 * 任务待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param modList 待办类型集合
	 * @param args 参数
	 */
	private StringBuffer taskToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<String> modList, List<Object> args) {
		StringBuffer taskSql = new StringBuffer();
		taskSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userid,t.taskName busTypeName,");
		taskSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,hand.handTimeLimit dealTimeLimit,t.grade,t.grade neworder,a.isClock,a.clockId,1 weekord");
		taskSql.append("\n  from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id ");
		
		if(null!=modList){
			taskSql.append(" and a.isclock=0");
		}
		taskSql.append("\n left join taskhandover hand on t.comId=hand.comId and t.id=hand.taskid  ");
		taskSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_TASK+"'");
		taskSql.append("\n and hand.id=(select max(h.id) from taskhandover h where h.comId=t.comId and t.id=h.taskid)");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), taskSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), taskSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, taskSql, args, " and a.comId=?");
		this.addSqlWhere(userId, taskSql, args, " and a.userId=? ");
		return taskSql;
	}
	
	/**
	 * 周报待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param args 参数
	 * @return
	 */
	private StringBuffer repToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<Object> args) {
		StringBuffer weekSql = new StringBuffer();
		weekSql.append("\n select aa.* ,rownum as weekord from (");
		weekSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userid,w.weekRepName busTypeName,");
		weekSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId");
		weekSql.append("\n  from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id");
		weekSql.append("\n   left join userOrganic org on w.reporterId=org.userid and w.comId=org.comId");
		weekSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_WEEK+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), weekSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), weekSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, weekSql, args, " and a.comId=?");
		this.addSqlWhere(userId, weekSql, args, " and a.userId=? ");
		weekSql.append("\n  order by a.readState,a.busSpec desc,w.year desc,w.weekNum desc,org.depId,w.reporterId");
		weekSql.append("\n  )aa");
		return weekSql;
	}
	
	/**
	 * 获取代办事项分页列表
	 * @param todayWorks 
	 * @param comId 企业编号
	 * @param userId 用户主键
	 * @param modList 模块集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TodayWorks> listPagedMsgTodo(TodayWorks todayWorks, Integer comId,
			Integer userId, List<String> modList) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_TASK)>=0){
			taskSql = taskToDoSql(todayWorks, comId, userId, modList, args);
		}
		
		//周报
		StringBuffer weekSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_WEEK)>=0){
			weekSql = repToDoSql(todayWorks, comId, userId, args);
			
		}
		//参会确认
		StringBuffer meetSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_MEETING)>=0
				|| modList.indexOf(ConstantInterface.TYPE_MEETING_SP)>=0){
			meetSql = meetToDoSql(todayWorks, comId, userId, args);
		}
		//审批待办
		StringBuffer spAppSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_FLOW_SP)>=0 || modList.indexOf(ConstantInterface.TYPE_SP_END)>=0){
			spAppSql = spToDoSql(todayWorks, comId, userId, args);
		}
		
		sql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userid,a.busTypeName,a.content,a.readState,a.busSpec,");
		sql.append("a.dealTimeLimit,a.modifyer, case when atten.id is null then 0 else 1 end as attentionState,grade,neworder,\n");
		sql.append("\n b.username modifyerName,b.gender,d.uuid modifyerUuid,d.filename,a.isClock,a.clockId,a.weekord from (");
		//全部
		if(null==modList){
			//003
			sql.append(taskSql);
			sql.append("\n union all ");
			//006
			sql.append(weekSql);
			sql.append("\n union all ");
			//017
			sql.append(meetSql);
			sql.append("\n union all ");
			//022
			sql.append(spAppSql);
		}else{
			for(Integer i=0;i<modList.size();i++){
				String modType = modList.get(i);
				switch (Integer.parseInt(modType)) {
					case 3:
						sql.append(taskSql);
						break;
					case 6:
						sql.append(weekSql);
						break;
					case 17:
					case 46:
						sql.append(meetSql);
						break;
					case 22:
						sql.append(spAppSql);
						break;
					default:
						break;
					}
				if(i<modList.size()-1){
					sql.append("\n union all ");
				}
			}
		}
		sql.append("\n )a left join attention atten on a.comId=atten.comId and a.busType=atten.busType and a.busId=atten.busId and atten.userid="+userId);
		sql.append("\n left join userinfo b on a.modifyer=b.id");
		sql.append("\n left join userOrganic c on a.modifyer=c.userid and a.comId=c.comId");
		sql.append("\n left join upfiles d on d.id=c.mediumHeadPortrait and d.comId=c.comId");
		sql.append("\n where 1=1 ");
		this.addSqlWhereLike(todayWorks.getContent(), sql, args, " and (a.busTypeName like ? or a.content like ?)", 2);
		
		return this.pagedQuery(sql.toString(),"a.readState,a.neworder desc,a.weekord,a.recordCreateTime desc,a.busId,a.busType", args.toArray(), TodayWorks.class);
	}
	/**
	 * 参会确认待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param args 参数
	 * @return
	 */
	private StringBuffer meetToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<Object> args) {
		StringBuffer meetSql = new StringBuffer();
		meetSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userid,meet.title busTypeName,");
		meetSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		meetSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		meetSql.append("\n where a.busSpec=1 ");
		this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP},
				meetSql, args, "\n and a.busType in ? ");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), meetSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), meetSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, meetSql, args, " and a.comId=?");
		this.addSqlWhere(userId, meetSql, args, " and a.userId=? ");
		return meetSql;
	}
	private StringBuffer spToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<Object> args) {
		StringBuffer spAppSql = new StringBuffer();
		spAppSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userid,sp.flowName busTypeName,");
		spAppSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		spAppSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id and sp.flowState=1");
		spAppSql.append("\n where a.busSpec=1 and a.busType = '"+ConstantInterface.TYPE_FLOW_SP+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), spAppSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), spAppSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, spAppSql, args, " and a.comId=?");
		this.addSqlWhere(userId, spAppSql, args, " and a.userId=? ");
		return spAppSql;
	}
	/**
	 * 手机端的消息通知的列表查询
	 * @param todayWorks 消息信息查询
	 * @param userInfo 当前操作人员
	 * @param modList 模块集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TodayWorks> listPagedTodayWorksForApp(TodayWorks todayWorks,
			UserInfo userInfo, List<String> modList) {
		
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>(modList.size() * 3 );
		
		
		//构建任务的消息通知
		StringBuffer taskSql = new StringBuffer();
		List<Object> taskArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_TASK)){
			this.constrTaskSqlForApp(todayWorks,userInfo,taskSql,taskArgs);
		}
		
		//构建项目的消息通知
		StringBuffer itemSql = new StringBuffer();
		List<Object> itemArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_ITEM)){
			this.constrItemSqlForApp(todayWorks,userInfo,itemSql,itemArgs);
		}
		//构建产品的消息通知
		StringBuffer proSql = new StringBuffer();
		List<Object> proArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_PRODUCT)){
			this.constrItemSqlForApp(todayWorks,userInfo,proSql,proArgs);
		}
		//构建投票的消息通知
		StringBuffer voteSql = new StringBuffer();
		List<Object> voteArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_VOTE )){
			this.constrVoteSqlForApp(todayWorks,userInfo,voteSql,voteArgs);
		}
		
		//构建周报的消息通知
		StringBuffer weekSql = new StringBuffer();
		List<Object> weekArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_WEEK)){
			this.constrWeekSqlForApp(todayWorks,userInfo,weekSql,weekArgs);
		}
		
		//构建问答的消息通知
		StringBuffer qasSql = new StringBuffer();
		List<Object> qasArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_QUES)){
			this.constrQasSqlForApp(todayWorks,userInfo,qasSql,qasArgs);
		}
		
		//构建客户的消息通知
		StringBuffer crmSql = new StringBuffer();
		List<Object> crmArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_CRM)){
			this.constrCrmSqlForApp(todayWorks,userInfo,crmSql,crmArgs);
		}
		//构建文档的消息通知
		StringBuffer fileSql = new StringBuffer();
		List<Object> fileArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_FILE)){
			this.constrFileSqlForApp(todayWorks,userInfo,fileSql,fileArgs);
		}
		
		//构建关注更新的消息通知
		StringBuffer attenSql = new StringBuffer();
		List<Object> attenArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_ATTEN)){
			this.constrAttenSqlForApp(todayWorks,userInfo,attenSql,attenArgs);
		}
		//构建审批的消息通知
		StringBuffer spFlowSql = new StringBuffer();
		List<Object> spFlowArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_FLOW_SP)){
			this.constrSqFlowSqlForApp(todayWorks,userInfo,spFlowSql,spFlowArgs);
		}
		//构建日报的消息通知
		StringBuffer dailySql = new StringBuffer();
		List<Object> dailyArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_DAILY)){
			this.constrDailySqlForApp(todayWorks,userInfo,dailySql,dailyArgs);
		}
		//构建日报的消息通知
		StringBuffer finicalSql = new StringBuffer();
		List<Object> finicalArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_FINALCIAL_BALANCE)){
			this.constrFinicalSqlForApp(todayWorks,userInfo,finicalSql,finicalArgs);
		}
		
		sql.append("\n select a.*,u.userName,dep.depName from (");
		
		for (int i=0,len = modList.size();i< len;i++) {
			Integer busType = Integer.parseInt(modList.get(i));
			switch (busType) {
			case 3:
				sql.append(taskSql);
				args.addAll(taskArgs);
				break;
			case 4:
				sql.append(voteSql);
				args.addAll(voteArgs);
				break;
			case 5:
				sql.append(itemSql);
				args.addAll(itemArgs);
				break;
			case 6:
				sql.append(weekSql);
				args.addAll(weekArgs);
				break;
			case 11:
				sql.append(qasSql);
				args.addAll(qasArgs);
				break;
			case 12:
				sql.append(crmSql);
				args.addAll(crmArgs);
				break;
			case 13:
				sql.append(fileSql);
				args.addAll(fileArgs);
				break;
			case 21:
				sql.append(attenSql);
				args.addAll(attenArgs);
				break;
			case 22:
				sql.append(spFlowSql);
				args.addAll(spFlowArgs);
				break;
			case 50:
				sql.append(dailySql);
				args.addAll(dailyArgs);
				break;
			case 6601:
				sql.append(finicalSql);
				args.addAll(finicalArgs);
				break;
			case 80:
				sql.append(proSql);
				args.addAll(proArgs);
				break;
			default:
				break;
			}
			if(i < (len - 1) && len > 1){
				sql.append("\n union all");
			}
		}
		sql.append("\n ) a");
		sql.append("\n inner join userorganic uorg on a.userId=uorg.userId and uorg.comId=?");
		args.add(userInfo.getComId());
		sql.append("\n inner join userInfo u on a.userId=u.id");
		sql.append("\n inner join department dep on uorg.depid=dep.id");
		
		return this.pagedQuery(sql.toString(), "a.recordCreateTime desc", args.toArray(), TodayWorks.class);
	}
	/**
	 * 构建关注的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrAttenSqlForApp(TodayWorks todayWorks, UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_ATTEN);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName from (");
		sql.append("\n select b.id,a.busId,a.busType,a.busTypeName,b.recordCreateTime,a.userId from (");
		//客户
		sql.append("\n 		select a.busId,a.busType,b.customername busTypeName,b.owner userId");
		sql.append("\n 		from attention a inner join customer b on a.busId=b.id");
		sql.append("\n 		and b.delState=0 and a.busType="+ConstantInterface.TYPE_CRM);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.customername like ?");
		//项目
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,b.itemname busTypeName,b.owner userId");
		sql.append("\n 		from attention a inner join item b on a.busId=b.id ");
		sql.append("\n 		and b.delState=0 and a.busType="+ConstantInterface.TYPE_ITEM);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.itemname like ?");
		//产品
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,b.name busTypeName,b.creator userId");
		sql.append("\n 		from attention a inner join product b on a.busId=b.id ");
		sql.append("\n 		and a.busType=" + ConstantInterface.TYPE_PRODUCT);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.name like ?");
		//任务
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,b.taskname busTypeName,b.owner userId");
		sql.append("\n 		from attention a inner join task b on a.busId=b.id ");
		sql.append("\n 		and b.delState=0 and a.busType="+ConstantInterface.TYPE_TASK);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.taskname like ?");
		//审批
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,sp.flowName busTypeName,sp.creator userId ");
		sql.append("\n 		from attention a inner join spFlowInstance sp on a.busId=sp.id ");
		sql.append("\n 		and sp.flowState=1 and sp.flowState<>2 and a.busType="+ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and sp.flowName like ?");
		//问答
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,b.title busTypeName,b.userId");
		sql.append("\n 		from attention a inner join question b on a.busId=b.id ");
		sql.append("\n 		and b.delState=0 and a.busType="+ConstantInterface.TYPE_QUES);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.title like ?");
		//投票
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,dbms_lob.substr(b.votecontent,4000) busTypeName,b.owner userId");
		sql.append("\n 		from attention a inner join vote b on a.busId=b.id and b.delState=0 ");
		sql.append("\n 		and a.busType="+ConstantInterface.TYPE_VOTE);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and dbms_lob.substr(b.votecontent,4000) like ?");
		
		//日报
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,b.dailyName busTypeName,b.reporterId userId ");
		sql.append("\n 		from attention a inner join daily b on a.busId=b.id");
		sql.append("\n 		and b.state=0 and a.busType="+ConstantInterface.TYPE_DAILY);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.dailyName like ?");
		
		sql.append("\n )a  ");
		sql.append("\n inner join (");
		sql.append("\n 	select a.id,a.busId,a.busType,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n where a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.flowName like ?");
		sql.append("\n )b on a.busId=b.busId and a.busType=b.busType and b.new_order=1 ");
		sql.append("\n )a where 1=1");
	}
	/**
	 * 构建财务办公的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrFinicalSqlForApp(TodayWorks todayWorks,
			UserInfo userInfo, StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_FINALCIAL_BALANCE);
		//常规审批
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime");
		sql.append("\n ,case when a.spBustype is null then '"+moduleTypeName+"' ");
		sql.append("\n 	else nvl(b.zvalue,'"+moduleTypeName+"') ");
		sql.append("\n 	end moduleTypeName ");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.flowName busTypeName,b.creator userId,a.recordCreateTime,b.busType spBustype");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join spflowinstance b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"' and a.busId=b.id");
		sql.append("\n where b.flowstate<>0 and (a.busspec=1 or a.readState=0)");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.flowName like ?");
		sql.append("\n 	)a");
		sql.append("\n 	left join dataDic b on a.spBustype=b.code and b.type='modBusName'");
		sql.append("\n 	where new_order=1");
	}
	/**
	 * 构建投票的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrVoteSqlForApp(TodayWorks todayWorks, UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_VOTE);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,dbms_lob.substr(b.votecontent,4000) busTypeName,b.owner userId,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join vote b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_VOTE+"' and a.busId=b.id");
		sql.append("\n where b.delstate=0 and (a.busspec=1 or a.readState=0)");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.votecontent like ?");
		sql.append("\n 	)a where new_order=1");
	}
	/**
	 * 构建文档的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrFileSqlForApp(TodayWorks todayWorks, UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_FILE);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.filedescribe busTypeName,a.recordCreateTime,b.userId");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join filedetail b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_FILE+"' and a.busId=b.id");
		sql.append("\n where (a.busspec=1 or a.readState=0)");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.filedescribe like ?");
		sql.append("\n 	)a where new_order=1");
	}
	/**
	 * 构建日报的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrDailySqlForApp(TodayWorks todayWorks, UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_DAILY);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.dailyname busTypeName,a.recordCreateTime,b.reporterId userId");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join daily b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_DAILY+"' and a.busId=b.id");
		sql.append("\n where b.state=0 and (a.busspec=1 or a.readState=0)");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.dailyname like ?");
		sql.append("\n 	)a where new_order=1");
	}
	/**
	 * 构建问答的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrQasSqlForApp(TodayWorks todayWorks, UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_QUES);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.title busTypeName,a.recordCreateTime,a.userId");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join question b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_QUES+"' and a.busId=b.id");
		sql.append("\n where b.delstate=0 and (a.busspec=1 or a.readState=0)");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.title like ?");
		sql.append("\n 	)a where new_order=1");
		
	}
	/**
	 * 构建周报的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrWeekSqlForApp(TodayWorks todayWorks, UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_WEEK);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.weekrepname busTypeName,a.recordCreateTime,b.reporterId userId");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join weekreport b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_WEEK+"' and a.busId=b.id");
		sql.append("\n where b.state=0 and (a.busspec=1 or a.readState=0)");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.weekrepname like ?");
		sql.append("\n 	)a where new_order=1");
		
	}
	/**
	 * 构建审批的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrSqFlowSqlForApp(TodayWorks todayWorks,
			UserInfo userInfo, StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_FLOW_SP);
		//常规审批
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime");
		sql.append("\n ,case when a.spBustype is null then '"+moduleTypeName+"' ");
		sql.append("\n 	else nvl(b.zvalue,'"+moduleTypeName+"') ");
		sql.append("\n 	end moduleTypeName ");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.flowName busTypeName,b.creator userId,a.recordCreateTime,b.busType spBustype");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join spflowinstance b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"' and a.busId=b.id");
		sql.append("\n where b.flowstate<>0 and (a.busspec=1 or a.readState=0)");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.flowName like ?");
		sql.append("\n 	)a");
		sql.append("\n 	left join dataDic b on a.spBustype=b.code and b.type='modBusName'");
		sql.append("\n 	where new_order=1");
	}
	/**
	 * 构建客户的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrCrmSqlForApp(TodayWorks todayWorks, UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_CRM);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.customerName busTypeName,b.owner userId,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join customer b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_CRM+"' and a.busId=b.id");
		sql.append("\n where b.delstate=0 and (a.busspec=1 or a.readState=0)");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and b.customerName like ?");
		sql.append("\n 	)a where new_order=1");
		
	}
	/**
	 *构建项目的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrItemSqlForApp(TodayWorks todayWorks, UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_ITEM);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,item.itemName busTypeName,item.owner userId,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join item on a.comId=item.comId and a.busType='"+ConstantInterface.TYPE_ITEM+"' and a.busId=item.id");
		sql.append("\n where item.delstate=0 and (a.busspec=1 or a.readState=0)");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and item.itemName like ?");
		sql.append("\n 	)a where new_order=1");
	}
	/**
	 * 构建任务的消息通知用于手机显示
	 * @param todayWorks 消息查询条件
	 * @param userInfo 当前操作人员
	 * @param sql 
	 * @param args
	 */
	private void constrTaskSqlForApp(TodayWorks todayWorks, UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_TASK);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,task.taskname busTypeName,task.owner userId,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join task on a.comId=task.comId and a.busType='"+ConstantInterface.TYPE_TASK+"' and a.busId=task.id");
		sql.append("\n where task.delstate=0 and (a.busspec=1 or a.readState=0)");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhereLike(todayWorks.getBusTypeName(), sql, args, "\n and task.taskname like ?");
		sql.append("\n 	)a where new_order=1");
	}
	/**
	 * 查询人员指定模块的未读消息数
	 * @param userInfo
	 * @param modList
	 * @return
	 */
	public List<TodayWorks> listPagedNoReadByUser(UserInfo userInfo, List<String> modList) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//构建任务的消息通知
		StringBuffer taskSql = new StringBuffer();
		List<Object> taskArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_TASK)){
			this.constrTaskSqlForAppCount(userInfo,taskSql,taskArgs);
		}
		
		//构建项目的消息通知
		StringBuffer itemSql = new StringBuffer();
		List<Object> itemArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_ITEM)){
			this.constrItemSqlForAppCount(userInfo,itemSql,itemArgs);
		}
		//构建产品的消息通知
		StringBuffer proSql = new StringBuffer();
		List<Object> proArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_PRODUCT)){
			this.constrProSqlForAppCount(userInfo,proSql,proArgs);
		}
		//构建投票的消息通知
		StringBuffer voteSql = new StringBuffer();
		List<Object> voteArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_ITEM)){
			this.constrVoteSqlForAppCount(userInfo,voteSql,voteArgs);
		}
		
		//构建周报的消息通知
		StringBuffer weekSql = new StringBuffer();
		List<Object> weekArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_WEEK)){
			this.constrWeekSqlForAppCount(userInfo,weekSql,weekArgs);
		}
		
		//构建问答的消息通知
		StringBuffer qasSql = new StringBuffer();
		List<Object> qasArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_QUES)){
			this.constrQasSqlForAppCount(userInfo,qasSql,qasArgs);
		}
		
		//构建客户的消息通知
		StringBuffer crmSql = new StringBuffer();
		List<Object> crmArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_CRM)){
			this.constrCrmSqlForAppCount(userInfo,crmSql,crmArgs);
		}
		//构建文档的消息通知
		StringBuffer fileSql = new StringBuffer();
		List<Object> fileArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_FILE)){
			this.constrFileSqlForAppCount(userInfo,fileSql,fileArgs);
		}
		
		//构建关注更新的消息通知
		StringBuffer attenSql = new StringBuffer();
		List<Object> attenArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_ATTEN)){
			this.constrAttenSqlForAppCount(userInfo,attenSql,attenArgs);
		}
		//构建审批的消息通知
		StringBuffer spFlowSql = new StringBuffer();
		List<Object> spFlowArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_FLOW_SP)){
			this.constrSqFlowSqlForAppCount(userInfo,spFlowSql,spFlowArgs);
		}
		//构建日报的消息通知
		StringBuffer dailySql = new StringBuffer();
		List<Object> dailyArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_DAILY)){
			this.constrDailySqlForAppCount(userInfo,dailySql,dailyArgs);
		}
		//构建日报的消息通知
		StringBuffer finicalSql = new StringBuffer();
		List<Object> finicalArgs = new ArrayList<>(3);
		if(modList.contains(ConstantInterface.TYPE_FINALCIAL_BALANCE)){
			this.constrFinicalSqlForAppCount(userInfo,finicalSql,finicalArgs);
		}
		
		sql.append("\n select a.* from (");
		
		for (int i=0,len = modList.size();i< len;i++) {
			Integer busType = Integer.parseInt(modList.get(i));
			switch (busType) {
			case 3:
				sql.append(taskSql);
				args.addAll(taskArgs);
				break;
			case 4:
				sql.append(voteSql);
				args.addAll(voteArgs);
				break;
			case 5:
				sql.append(itemSql);
				args.addAll(itemArgs);
				break;
			case 6:
				sql.append(weekSql);
				args.addAll(weekArgs);
				break;
			case 11:
				sql.append(qasSql);
				args.addAll(qasArgs);
				break;
			case 12:
				sql.append(crmSql);
				args.addAll(crmArgs);
				break;
			case 13:
				sql.append(fileSql);
				args.addAll(fileArgs);
				break;
			case 21:
				sql.append(attenSql);
				args.addAll(attenArgs);
				break;
			case 22:
				sql.append(spFlowSql);
				args.addAll(spFlowArgs);
				break;
			case 50:
				sql.append(dailySql);
				args.addAll(dailyArgs);
				break;
			case 6601:
				sql.append(finicalSql);
				args.addAll(finicalArgs);
				break;
			case 80:
				sql.append(proSql);
				args.addAll(proArgs);
				break;
			default:
				break;
			}
			if(i < (len - 1) && len > 1){
				sql.append("\n union all");
			}
		}
		sql.append("\n ) a ");
		return this.pagedQuery(sql.toString(), "\n a.recordCreateTime desc", args.toArray(), TodayWorks.class);
	}
	
	/**
	 * 构建关注的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrAttenSqlForAppCount( UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_ATTEN);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName from (");
		sql.append("\n select b.id,a.busId,a.busType,a.busTypeName,b.recordCreateTime,a.userId from (");
		//客户
		sql.append("\n 		select a.busId,a.busType,b.customername busTypeName,b.owner userId");
		sql.append("\n 		from attention a inner join customer b on a.busId=b.id");
		sql.append("\n 		and b.delState=0 and a.busType="+ConstantInterface.TYPE_CRM);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		//项目
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,b.itemname busTypeName,b.owner userId");
		sql.append("\n 		from attention a inner join item b on a.busId=b.id ");
		sql.append("\n 		and b.delState=0 and a.busType="+ConstantInterface.TYPE_ITEM);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		//任务
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,b.taskname busTypeName,b.owner userId");
		sql.append("\n 		from attention a inner join task b on a.busId=b.id ");
		sql.append("\n 		and b.delState=0 and a.busType="+ConstantInterface.TYPE_TASK);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		//审批
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,sp.flowName busTypeName,sp.creator userId ");
		sql.append("\n 		from attention a inner join spFlowInstance sp on a.busId=sp.id ");
		sql.append("\n 		and sp.flowState=1 and sp.flowState<>2 and a.busType="+ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		//问答
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,b.title busTypeName,b.userId");
		sql.append("\n 		from attention a inner join question b on a.busId=b.id ");
		sql.append("\n 		and b.delState=0 and a.busType="+ConstantInterface.TYPE_QUES);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		//投票
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,dbms_lob.substr(b.votecontent,4000) busTypeName,b.owner userId");
		sql.append("\n 		from attention a inner join vote b on a.busId=b.id and b.delState=0 ");
		sql.append("\n 		and a.busType="+ConstantInterface.TYPE_VOTE);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		
		//日报
		sql.append("\n 		union all");
		sql.append("\n 		select a.busId,a.busType,b.dailyName busTypeName,b.reporterId userId ");
		sql.append("\n 		from attention a inner join daily b on a.busId=b.id");
		sql.append("\n 		and b.state=0 and a.busType="+ConstantInterface.TYPE_DAILY);
		sql.append("\n 		where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		
		sql.append("\n )a  ");
		sql.append("\n inner join (");
		sql.append("\n 	select a.id,a.busId,a.busType,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n  where a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n )b on a.busId=b.busId and a.busType=b.busType and b.new_order=1 ");
		sql.append("\n )a where 1=1");
	}
	/**
	 * 构建财务办公的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrFinicalSqlForAppCount(
			UserInfo userInfo, StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_FINALCIAL_BALANCE);
		//常规审批
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime");
		sql.append("\n ,case when a.spBustype is null then '"+moduleTypeName+"' ");
		sql.append("\n 	else nvl(b.zvalue,'"+moduleTypeName+"')");
		sql.append("\n 	end moduleTypeName ");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.flowName busTypeName,b.creator userId,a.recordCreateTime,b.busType spBustype");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join spflowinstance b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"' and a.busId=b.id");
		sql.append("\n where b.flowstate<>0 and  a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a");
		sql.append("\n 	left join dataDic b on a.spBustype=b.code and b.type='modBusName'");
		sql.append("\n 	where new_order=1");
	}
	/**
	 * 构建投票的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrVoteSqlForAppCount( UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_VOTE);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,dbms_lob.substr(b.votecontent,4000) busTypeName,b.owner userId,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join vote b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_VOTE+"' and a.busId=b.id");
		sql.append("\n where b.delstate=0 and  a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a where new_order=1");
	}
	/**
	 * 构建文档的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrFileSqlForAppCount( UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_FILE);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.filedescribe busTypeName,a.recordCreateTime,b.userId");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join filedetail b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_FILE+"' and a.busId=b.id");
		sql.append("\n where  a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a where new_order=1");
	}
	/**
	 * 构建日报的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrDailySqlForAppCount( UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_DAILY);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.dailyname busTypeName,a.recordCreateTime,b.reporterId userId");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join daily b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_DAILY+"' and a.busId=b.id");
		sql.append("\n where b.state=0 and a.readState=0 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a where new_order=1");
	}
	/**
	 * 构建问答的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrQasSqlForAppCount( UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_QUES);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.title busTypeName,a.recordCreateTime,a.userId");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join question b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_QUES+"' and a.busId=b.id");
		sql.append("\n where b.delstate=0 and  a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a where new_order=1");
		
	}
	/**
	 * 构建周报的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrWeekSqlForAppCount( UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_WEEK);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.weekrepname busTypeName,a.recordCreateTime,b.reporterId userId");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join weekreport b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_WEEK+"' and a.busId=b.id");
		sql.append("\n where b.state=0 and a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a where new_order=1");
		
	}
	/**
	 * 构建审批的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrSqFlowSqlForAppCount(
			UserInfo userInfo, StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_FLOW_SP);
		//常规审批
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime");
		sql.append("\n ,case when a.spBustype is null then '"+moduleTypeName+"' ");
		sql.append("\n 	else nvl(b.zvalue,'"+moduleTypeName+"') ");
		sql.append("\n 	end moduleTypeName ");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.flowName busTypeName,b.creator userId,a.recordCreateTime,b.busType spBustype");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join spflowinstance b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"' and a.busId=b.id");
		sql.append("\n where b.flowstate<>0 and a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a");
		sql.append("\n 	left join dataDic b on a.spBustype=b.code and b.type='modBusName'");
		sql.append("\n 	where new_order=1");
	}
	/**
	 * 构建客户的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrCrmSqlForAppCount( UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_CRM);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,b.customerName busTypeName,b.owner userId,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join customer b on a.comId=b.comId and a.busType='"+ConstantInterface.TYPE_CRM+"' and a.busId=b.id");
		sql.append("\n where b.delstate=0 and a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a where new_order=1");
		
	}
	/**
	 *构建项目的消息通知用于手机显示
	 * @param todayWorks
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrItemSqlForAppCount(UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_ITEM);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,item.itemName busTypeName,item.owner userId,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join item on a.comId=item.comId and a.busType='"+ConstantInterface.TYPE_ITEM+"' and a.busId=item.id");
		sql.append("\n where item.delstate=0 and  a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a where new_order=1");
	}
	/**
	 *构建产品的消息通知用于手机显示
	 * @param userInfo
	 * @param sql
	 * @param args
	 */
	private void constrProSqlForAppCount(UserInfo userInfo,StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_PRODUCT);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,item.name busTypeName,item.creator userId,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join product item on a.comId=item.comId and a.busType='" + ConstantInterface.TYPE_PRODUCT + "' and a.busId=item.id");
		sql.append("\n where a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a where new_order=1");
	}
	/**
	 * 构建任务的消息通知用于手机显示
	 * @param todayWorks 消息查询条件
	 * @param userInfo 当前操作人员
	 * @param sql 
	 * @param args
	 */
	private void constrTaskSqlForAppCount(UserInfo userInfo,
			StringBuffer sql, List<Object> args) {
		String moduleTypeName = DataDicContext.getInstance().getCurrentPathZvalue("modBusName", ConstantInterface.TYPE_TASK);
		sql.append("\n select a.id,a.busId,a.busType,a.busTypeName,a.userId,a.recordCreateTime,'"+moduleTypeName+"' moduleTypeName");
		sql.append("\n from (");
		sql.append("\n 	select a.id,a.busId,a.busType,task.taskname busTypeName,task.owner userId,a.recordCreateTime");
		sql.append("\n 	,row_number() over (partition by a.busId,a.busId order by a.id desc) as new_order");
		sql.append("\n 	from todayWorks a");
		sql.append("\n inner join task on a.comId=task.comId and a.busType='"+ConstantInterface.TYPE_TASK+"' and a.busId=task.id");
		sql.append("\n where task.delstate=0 and a.readState=0");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, "\n and a.userId=?");
		sql.append("\n 	)a where new_order=1");
	}
}
