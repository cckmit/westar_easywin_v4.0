package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.SystemLog;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WorkTrace;
import com.westar.base.pojo.CommonLog;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.ConstantInterface;

@Repository
public class SystemLogDao extends BaseDao{
	
	/**
	 * 分页查询系统日志
	 * @param systemLog
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SystemLog> listPagedOrgSysLog(SystemLog systemLog){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select * from(");
		sql.append("\n select a.id,a.recordDateTime,a.optIP,a.comid,b.userid,c.userName,b.depid,f.depName,");
		sql.append("\n a.content,c.gender,d.uuid,d.filename imgName");
		sql.append("\n from systemlog a");
		sql.append("\n inner join userorganic b on a.comid=b.comid and a.userid=b.userId");
		sql.append("\n inner join userInfo c on b.userid=c.id");
		sql.append("\n left join upfiles d on b.smallheadportrait=d.id");
		sql.append("\n left join department f on b.comid=f.comid and b.depid=f.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(systemLog.getComId(), sql, args, " and a.comid=?");
		//查询创建时间段
		this.addSqlWhere(systemLog.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(systemLog.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		//查询部门的
		if(null!=systemLog.getDepId()){
	    	sql.append("and f.id in ( select id from department \n");
	    	sql.append("start with id="+systemLog.getDepId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
		}
		
		//汇报人员筛选
		if(null!=systemLog.getUserId() && systemLog.getUserId()!=0){
			sql.append("and a.userId=? \n");
			args.add(systemLog.getUserId());
		}
		sql.append(") a where 1=1 \n");
		this.addSqlWhereLike(systemLog.getContent(), sql, args, " and a.content like ?");
		return this.pagedQuery(sql.toString(), " a.recordDateTime desc,a.id ", args.toArray(), SystemLog.class);
	}
	/**
	 * 分页查询个人系统日志
	 * @param systemLog
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SystemLog> listPagedSelfSysLog(SystemLog systemLog){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from(");
		sql.append("\n select a.* ");
		sql.append("\n from systemlog a ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(systemLog.getComId(), sql, args, " and a.comid=?");
		this.addSqlWhere(systemLog.getUserId(), sql, args, " and a.userId=?");
		
		//查询创建时间段
		this.addSqlWhere(systemLog.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(systemLog.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		sql.append(") a where 1=1 \n");
		this.addSqlWhereLike(systemLog.getContent(), sql, args, " and a.content like ?");
		
		return this.pagedQuery(sql.toString(), " a.recordDateTime desc,a.id ", args.toArray(), SystemLog.class);
	}

	/**
	 * 获取超出10w的日志
	 * @param num
	 * @return
	 */
	public void delMoreSystemLog() {
		// TODO Auto-generated method stub
		String sql = "delete from systemLog where id in(select id from (select * from systemLog order by recordDateTime) tb where rownum >100000)";
		this.getJdbcTemplate().execute(sql);
	}
	/**
	 * 用于控制选择入职时间的最大值不超过首次使用时间
	 * @param sessionUser
	 * @return
	 */
	public SystemLog getUserHireDate(UserInfo sessionUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from(");
		sql.append("\n select a.recorddatetime from systemlog a where  a.businesstype=008");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comid=?");
		this.addSqlWhere(sessionUser.getId(), sql, args, " and a.userid=?");
		sql.append("\n order by a.id asc");
		sql.append("\n) a where rownum=1");
		return (SystemLog) this.objectQuery(sql.toString(), args.toArray(), SystemLog.class);
	}

	
	/**
	 * 查询工作轨迹(信息分享，客户，项目，任务，问答，投票)
	 * @param userInfo 当前操作人员
	 * @param systemLog 工作轨迹
	 * @param modTypes 模块类型集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WorkTrace> listWorkTrace(UserInfo userInfo, SystemLog systemLog,
			List<String> modList) {
		List<Object> args = new ArrayList<Object>();
		//任务的工作轨迹
		StringBuffer taskSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_TASK)>=0){
			taskSql.append("\n select a9.*,substr(a9.recordCreateTime,0,10) recordDateTime from workTrace a9 left join task b on a9.comid=b.comid and a9.businessType='003' and a9.busid=b.id ");
			taskSql.append("\n where a9.businessType='003' and b.id>0 and b.delstate=0 and a9.busid>0 ");
			this.addSqlWhere(userInfo.getComId(), taskSql, args, " and a9.comid=?");
			this.addSqlWhere(userInfo.getId(), taskSql, args, " and a9.traceUserId=?");
			
		}
		//投票的工作轨迹
		StringBuffer voteSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_VOTE)>=0){
			voteSql.append("\n select a5.*,substr(a5.recordCreateTime,0,10) recordDateTime from workTrace a5 left join vote b on a5.comid=b.comid and a5.businessType='004' and a5.busid=b.id ");
			voteSql.append("\n  where a5.businessType='004' and b.id>0 and b.delstate=0 and a5.busid>0 ");
			this.addSqlWhere(userInfo.getComId(), voteSql, args, " and a5.comid=?");
			this.addSqlWhere(userInfo.getId(), voteSql, args, " and a5.traceUserId=?");
		}
		//项目的工作轨迹
		StringBuffer itemSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_ITEM)>=0){
			itemSql.append("\n select a8.*,substr(a8.recordCreateTime,0,10) recordDateTime from workTrace a8 left join item b on a8.comid=b.comid and a8.businessType='005' and a8.busid=b.id");
			itemSql.append("\n where a8.businessType='005' and b.id>0 and b.delstate=0 and a8.busid>0 ");
			this.addSqlWhere(userInfo.getComId(), itemSql, args, " and a8.comid=?");
			this.addSqlWhere(userInfo.getId(), itemSql, args, " and a8.traceUserId=?");
		}
		//问答的工作轨迹
		StringBuffer qasSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_QUES)>=0){
			qasSql.append("\n select a3.*,substr(a3.recordCreateTime,0,10) recordDateTime from workTrace a3 left join question b on a3.comid=b.comid and a3.businessType='011' and a3.busid=b.id ");
			qasSql.append("\n where a3.businessType='011' and b.id>0 and b.delstate=0  and a3.busid>0 ");
			this.addSqlWhere(userInfo.getComId(), qasSql, args, " and a3.comid=?");
			this.addSqlWhere(userInfo.getId(), qasSql, args, " and a3.traceUserId=?");
		}
		//客户的工作轨迹
		StringBuffer crmSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_CRM)>=0){
			crmSql.append("\n select a7.*,substr(a7.recordCreateTime,0,10) recordDateTime from workTrace a7 left join customer b on a7.comid=b.comid and a7.businessType='012' and a7.busid=b.id ");
			crmSql.append("\n where a7.businessType='012' and b.id>0 and b.delstate=0  and a7.busid>0 ");
			this.addSqlWhere(userInfo.getComId(), crmSql, args, " and a7.comid=?");
			this.addSqlWhere(userInfo.getId(), crmSql, args, " and a7.traceUserId=?");
		}
		//信息分享的工作轨迹
		StringBuffer shareSql = new StringBuffer();
		if(null==modList || modList.indexOf("100")>=0){
			shareSql.append("\n select a1.*,substr(a1.recordCreateTime,0,10) recordDateTime from workTrace a1 left join msgshare b on a1.comid=b.comid and a1.businessType='100' and a1.busid=b.id  ");
			shareSql.append("\n where a1.businessType='100' and b.id>0  and a1.busid>0 ");
			this.addSqlWhere(userInfo.getComId(), shareSql, args, " and a1.comid=?");
			this.addSqlWhere(userInfo.getId(), shareSql, args, " and a1.traceUserId=?");
		}
		StringBuffer sql = new StringBuffer(" select * from (");
		if(null==modList){
			sql.append(taskSql);//003
			sql.append("\n union all");
			sql.append(voteSql);//004
			sql.append("\n union all");
			sql.append(itemSql);//005
			sql.append("\n union all");
			sql.append(qasSql);//011
			sql.append("\n union all");
			sql.append(crmSql);//012
			sql.append("\n union all");
			sql.append(shareSql);//100
		}else{
			for(Integer i=0;i<modList.size();i++){
				String modType = modList.get(i);
				switch (Integer.parseInt(modType)) {
					case 3:
						sql.append(taskSql);
						break;
					case 4:
						sql.append(voteSql);
						break;
					case 5:
						sql.append(itemSql);
						break;
					case 11:
						sql.append(qasSql);
						break;
					case 12:
						sql.append(crmSql);
						break;
					case 100:
						sql.append(shareSql);
						break;
					default:
						break;
					}
				if(i<modList.size()-1){
					sql.append("\n union all ");
				}
			}
		}
		
		sql.append("\n )b where 1=1 ");
		this.addSqlWhere(systemLog.getStartDate(), sql, args, " and b.recordDateTime>=?");
		this.addSqlWhere(systemLog.getEndDate(), sql, args, " and b.recordDateTime<=?");
		sql.append("\n order by b.businessType,b.busid,b.id ");
		return this.listQuery(sql.toString(), args.toArray(), WorkTrace.class);
	}
	
	/**
	 * 日志记录
	 * @param comId 团队主键
	 * @param modId	模块主键	
	 * @param busType 模块类别
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageBean<CommonLog> listPageLog(Integer comId,Integer modId,String busType){
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		if(busType.equals(ConstantInterface.TYPE_TASK)){
			//任务模块
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from taskLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.taskid = ? \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals("1")){
			//分享模块
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from msgShareLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.msgId = ?  \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_FILE)){
			//文档模块
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from fileLog a \n");
			sql.append("left join fileDetail f on f.id = a.fileDetailId \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and f.fileId = ?  \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_CRM)){
			//客户模块
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from customerLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.customerId = ?  \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_ITEM)){
			//项目模块
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from itemLog a \n");
			sql.append("left join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.itemid = ?  \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_PRODUCT)){
			//产品模块
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.operator as userId,b.username as username \n");
			sql.append("from proLog a \n");
			sql.append("inner join userInfo b on a.operator = b.id  \n");
			sql.append("where a.comId=? and a.proId = ?  \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_WEEK)){
			//周报
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from weekRepLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.weekReportId = ? \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_DAILY)){
			//分享
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from dailyLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.dailyId = ? \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_VOTE)){
			//投票
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from voteLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.voteid = ?  \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_QUES)){
			//问答
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from quesLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.quesid = ? \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_MEETING_SP)){
			//会议
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from meetLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.meetingId = ?  \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_CONSUME)){
			//消费记录
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username \n");
			sql.append("from consumeLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.consumeId = ? \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_SCHEDULE)){
			//日程
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username  \n");
			sql.append("from scheLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.scheduleId = ?  \n");
			args.add(comId);
			args.add(modId);
		}else if(busType.equals(ConstantInterface.TYPE_OUTLINKMAN)){
			//外部联系人
			sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username\n");
			sql.append("from olmLog a \n");
			sql.append("inner join userInfo b on a.userId = b.id \n");
			sql.append("where a.comId=? and a.outLinkManId = ?  \n");
			args.add(comId);
			args.add(modId);
		}else{
			return null;
		}
		return this.pagedBeanQuery(sql.toString(), "a.recordCreateTime desc,a.id desc", args.toArray(), CommonLog.class);
	}
}
