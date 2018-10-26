package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.TodayWorks;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.MsgNoRead;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.StringUtil;

@Repository
public class TodayWorksDao extends BaseDao {

	/**
	 * 取得当前用户待办事项
	 * @param modId 模块主键
	 * @param comId 企业号
	 * @param userId 操作员主键
	 * @param busType 业务类型
	 * @param clockId 闹铃主键 0不是从闹铃来的 非0则是闹铃主键
	 * @return
	 */
	public TodayWorks getUserTodayWork(Integer modId, Integer comId,
			Integer userId, String busType, Integer clockId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from todayWorks where busspec=1 and rownum=1 ");
		this.addSqlWhere(comId, sql, args, " and comId=?");
		this.addSqlWhere(busType, sql, args, " and busType=?");
		this.addSqlWhere(modId, sql, args, " and busId=?");
		this.addSqlWhere(userId, sql, args, " and userId=?");
		if(clockId>0){//从闹铃来的
			this.addSqlWhere(clockId, sql, args, " and clockId=?");
		}else{//不是从闹铃来的
			sql.append("\n and isClock=0");
		}
		return (TodayWorks) this.objectQuery(sql.toString(), args.toArray(), TodayWorks.class);
	}
	/**
	 * 将普通消息设置成已读
	 * @param userInfo
	 * @param todayWork
	 */
	public void updateNormalRead(UserInfo userInfo, TodayWorks todayWork) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update todayworks a set readState=1 where a.busSpec=0 and a.isClock=0");
		this.addSqlWhere(todayWork.getBusId(), sql, args, " and a.busId=?");
		this.addSqlWhere(todayWork.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(todayWork.getBusType(), sql, args, " and a.busType=?");
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 将待办事项设置成普通事项
	 * @param busId 业务主键
	 * @param comId 企业号
	 * @param userId 用户主键
	 * @param busType 业务类型
	 */
	public void updateWorkToNormal(Integer busId, Integer comId,
			Integer userId, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update todayworks a set busspec=0 where 1=1");
		this.addSqlWhere(busId, sql, args, " and a.busId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		this.addSqlWhere(busType, sql, args, " and a.busType=?");
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 获取消息提醒列表
	 * @param todayWorks 
	 * @param comId 企业编号
	 * @param userId 用户主键
	 * @param modList 模块集合
	 * @param source 为空则用于查询非闹铃，不为空则用于查询模块所有未读数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TodayWorks> listMsgNoRead(TodayWorks todayWorks, Integer comId,
			Integer userId, List<String> modList,String source) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		Integer busSpec = todayWorks.getBusSpec();
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_TASK)>=0){
			taskSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			taskSql.append("\n  from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id");
			taskSql.append("\n where a.busType='"+ConstantInterface.TYPE_TASK+"'");
			this.addSqlWhere(todayWorks.getReadState(), taskSql, args, " and a.readState=?");
			//判断是否需要查询闹铃
			if(null!=modList && null==source){
				taskSql.append(" and a.isClock=0 ");
			}
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), taskSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), taskSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, taskSql, args, " and a.comId=?");
			this.addSqlWhere(userId, taskSql, args, " and a.userId=? ");
		}
		//投票
		StringBuffer voteSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_VOTE)>=0){
			voteSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,dbms_lob.substr(v.voteContent,4000) busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			voteSql.append("\n  from todayWorks a inner join vote v on a.comId=v.comId and a.busId=v.id");
			voteSql.append("\n where a.busType='"+ConstantInterface.TYPE_VOTE+"'");
			this.addSqlWhere(todayWorks.getReadState(), voteSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), voteSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), voteSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, voteSql, args, " and a.comId=?");
			this.addSqlWhere(userId, voteSql, args, " and a.userId=? ");
		}
		//日程
		StringBuffer schSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_SCHEDULE)>=0){
			schSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,v.title busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			schSql.append("\n  from todayWorks a inner join SCHEDULE v on a.comId=v.comId and a.busId=v.id");
			schSql.append("\n where a.busType='"+ConstantInterface.TYPE_SCHEDULE+"'");
			this.addSqlWhere(todayWorks.getReadState(), schSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), schSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), schSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, schSql, args, " and a.comId=?");
			this.addSqlWhere(userId, schSql, args, " and a.userId=? ");
		}
		//项目
		StringBuffer itemSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_ITEM)>=0){
			itemSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.itemName busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			itemSql.append("\n  from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
			itemSql.append("\n where  a.busType='"+ConstantInterface.TYPE_ITEM+"'");
			this.addSqlWhere(todayWorks.getReadState(), itemSql, args, " and a.readState=?");
			//判断是否需要查询闹铃
			if(null!=modList && null==source){
				itemSql.append(" and a.isClock=0 ");
			}
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), itemSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), itemSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, itemSql, args, " and a.comId=?");
			this.addSqlWhere(userId, itemSql, args, " and a.userId=? ");
			//只用于项目查看查询未读消息和已读的待办事项
			if(null!=modList && modList.indexOf(ConstantInterface.TYPE_ITEM)>=0 && null!=busSpec && busSpec==1){
				itemSql.append("\n and (a.busspec=1 or a.readState=0) and a.busId="+todayWorks.getBusId());
			}
		}
		//产品
		StringBuffer proSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_PRODUCT)>=0){
			proSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,pro.name busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			proSql.append("\n  from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id");
			proSql.append("\n where  a.busType='"+ConstantInterface.TYPE_PRODUCT+"'");
			this.addSqlWhere(todayWorks.getReadState(), proSql, args, " and a.readState=?");
			//判断是否需要查询闹铃
			if(null!=modList && null==source){
				proSql.append(" and a.isClock=0 ");
			}
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), proSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), proSql, args, " and substr(a.recordCreateTime,0,10)<=?");

			this.addSqlWhere(comId, proSql, args, " and a.comId=?");
			this.addSqlWhere(userId, proSql, args, " and a.userId=? ");
			//只用于项目查看查询未读消息和已读的待办事项
			if(null!=modList && modList.indexOf(ConstantInterface.TYPE_ITEM)>=0 && null!=busSpec && busSpec==1){
				proSql.append("\n and (a.busspec=1 or a.readState=0) and a.busId="+todayWorks.getBusId());
			}
		}
		//周报
		StringBuffer weekSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_WEEK)>=0){
			weekSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.weekRepName busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			weekSql.append("\n  from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id");
			weekSql.append("\n where a.busType='"+ConstantInterface.TYPE_WEEK+"'");
			this.addSqlWhere(todayWorks.getReadState(), weekSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), weekSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), weekSql, args, " and substr(a.recordCreateTime,0,10)<=?");

			this.addSqlWhere(comId, weekSql, args, " and a.comId=?");
			this.addSqlWhere(userId, weekSql, args, " and a.userId=? ");
			//只用于周报查看查询未读消息和已读的待办事项
			if(null!=modList && modList.indexOf(ConstantInterface.TYPE_WEEK)>=0 && null!=busSpec && busSpec==1){
				weekSql.append("\n and (a.busspec=1 or a.readState=0) and a.busId="+todayWorks.getBusId());
			}
		}
		//分享
		StringBuffer dailySql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_DAILY)>=0){
			dailySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.dailyName busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			dailySql.append("\n  from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id");
			dailySql.append("\n where a.busType='"+ConstantInterface.TYPE_DAILY+"'");
			this.addSqlWhere(todayWorks.getReadState(), dailySql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), dailySql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), dailySql, args, " and substr(a.recordCreateTime,0,10)<=?");

			this.addSqlWhere(comId, dailySql, args, " and a.comId=?");
			this.addSqlWhere(userId, dailySql, args, " and a.userId=? ");
			//只用于分享查看查询未读消息和已读的待办事项
			if(null!=modList && modList.indexOf(ConstantInterface.TYPE_WEEK)>=0 && null!=busSpec && busSpec==1){
				dailySql.append("\n and (a.busspec=1 or a.readState=0) and a.busId="+todayWorks.getBusId());
			}
		}
		//问答
		StringBuffer qasSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_QUES)>=0){
			qasSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,q.title busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			qasSql.append("\n  from todayWorks a inner join question q on a.comId=q.comId and a.busId=q.id");
			qasSql.append("\n where a.busType='"+ConstantInterface.TYPE_QUES+"'");
			this.addSqlWhere(todayWorks.getReadState(), qasSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), qasSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), qasSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, qasSql, args, " and a.comId=?");
			this.addSqlWhere(userId, qasSql, args, " and a.userId=? ");
		}
		//客户
		StringBuffer crmSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_CRM)>=0){
			crmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.customername busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			crmSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id");
			crmSql.append("\n where  a.busType='"+ConstantInterface.TYPE_CRM+"'");
			this.addSqlWhere(todayWorks.getReadState(), crmSql, args, " and a.readState=?");
			//判断是否需要查询闹铃
			if(null!=modList && null==source){
				crmSql.append(" and a.isClock=0 ");
			}
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), crmSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), crmSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, crmSql, args, " and a.comId=?");
			this.addSqlWhere(userId, crmSql, args, " and (a.userId=? or a.userId=0)");
			
			//只用于客户查看查询未读消息和已读的待办事项
			if(null!=modList && modList.indexOf(ConstantInterface.TYPE_CRM)>=0 && null!=busSpec && busSpec==1){
				crmSql.append("\n and (a.busspec=1 or a.readState=0)and a.busId="+todayWorks.getBusId());
			}
		}
		//附件
		StringBuffer fileSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_FILE)>=0){
			fileSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,f.fileDescribe busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			fileSql.append("\n  from todayWorks a inner join fileDetail f on a.comId=f.comId and a.busId=f.id");
			fileSql.append("\n where a.busType='"+ConstantInterface.TYPE_FILE+"'");
			this.addSqlWhere(todayWorks.getReadState(), fileSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), fileSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), fileSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, fileSql, args, " and a.comId=?");
			this.addSqlWhere(userId, fileSql, args, " and a.userId=? ");
		}
		//申请
		StringBuffer invSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_APPLY)>=0){
			invSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'申请加入企业' busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			invSql.append("\n  from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
			invSql.append("\n where  a.busType='"+ConstantInterface.TYPE_APPLY+"'");
			this.addSqlWhere(todayWorks.getReadState(), invSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), invSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), invSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, invSql, args, " and a.comId=?");
			this.addSqlWhere(userId, invSql, args, " and a.userId=? ");
		}
		//会议
		StringBuffer meetSql = new StringBuffer();
		if(null==modList || (modList.indexOf(ConstantInterface.TYPE_MEETING)>=0
				|| modList.indexOf(ConstantInterface.TYPE_MEETING_SP)>=0)){
			meetSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
			meetSql.append("\n a.modifyer,a.content,a.readState,a.busSpec");
			meetSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
			meetSql.append("\n where 1=1 " );
			this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP}, meetSql, args, "\n and a.busType in ? ");
			this.addSqlWhere(todayWorks.getReadState(), meetSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), meetSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), meetSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, meetSql, args, " and a.comId=?");
			this.addSqlWhere(userId, meetSql, args, " and a.userId=? ");
		}
		//会议申请
		StringBuffer meetAppSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_MEETROOM)>=0){
			meetAppSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
			meetAppSql.append("\n a.modifyer,a.content,a.readState,a.busSpec");
			meetAppSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
			meetAppSql.append("\n where a.busType='"+ConstantInterface.TYPE_MEETROOM+"'");
			this.addSqlWhere(todayWorks.getReadState(), meetAppSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), meetAppSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), meetAppSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, meetAppSql, args, " and a.comId=?");
			this.addSqlWhere(userId, meetAppSql, args, " and a.userId=? ");
		}
		//审批
		StringBuffer spSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_FLOW_SP)>=0){
			spSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			spSql.append("\n  from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id");
			spSql.append("\n where a.busType='"+ConstantInterface.TYPE_FLOW_SP+"' and sp.flowState=1");
			this.addSqlWhere(todayWorks.getReadState(), spSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), spSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), spSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, spSql, args, " and a.comId=?");
			this.addSqlWhere(userId, spSql, args, " and a.userId=? ");
			
			
		}
		//审批（完结）
		StringBuffer spEndSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_SP_END)>=0){
			spEndSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			spEndSql.append("\n  from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id");
			spEndSql.append("\n where  a.busType='"+ConstantInterface.TYPE_SP_END+"'and a.readstate ='0' ");
			this.addSqlWhere(todayWorks.getReadState(), spEndSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), spEndSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), spEndSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, spEndSql, args, " and a.comId=?");
			this.addSqlWhere(userId, spEndSql, args, " and a.userId=? ");
		}
		//采购
		StringBuffer bgypPurSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_BGYP_BUY_CHECK)>=0
				|| modList.indexOf(ConstantInterface.TYPE_BGYP_BUY_NOTICE)>=0){
			bgypPurSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'采购单:'||bpur.purOrderNum busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			bgypPurSql.append("\n  from todayWorks a inner join bgypPurOrder bpur on a.comId=bpur.comId and a.busId=bpur.id");
			bgypPurSql.append("\n where a.busType in ('"+ConstantInterface.TYPE_BGYP_BUY_CHECK+"','"+ConstantInterface.TYPE_BGYP_BUY_NOTICE+"')");
			this.addSqlWhere(todayWorks.getReadState(), bgypPurSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), bgypPurSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), bgypPurSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, bgypPurSql, args, " and a.comId=?");
			this.addSqlWhere(userId, bgypPurSql, args, " and a.userId=? ");
		}
		
		//申领
		StringBuffer bgypApplySql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_BGYP_APPLY_CHECK)>=0
				|| modList.indexOf(ConstantInterface.TYPE_BGYP_APPLY_NOTICE)>=0){
			bgypApplySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'用品申领:'||bapply.applyDate busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			bgypApplySql.append("\n  from todayWorks a inner join bgypApply bapply on a.comId=bapply.comId and a.busId=bapply.id");
			bgypApplySql.append("\n where a.busType in ('"+ConstantInterface.TYPE_BGYP_APPLY_CHECK+"','"+ConstantInterface.TYPE_BGYP_APPLY_NOTICE+"')");
			this.addSqlWhere(todayWorks.getReadState(), bgypApplySql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), bgypApplySql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), bgypApplySql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, bgypApplySql, args, " and a.comId=?");
			this.addSqlWhere(userId, bgypApplySql, args, " and a.userId=? ");
		}
			
		//公告
		StringBuffer announSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_ANNOUNCEMENT)>=0){
			announSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,v.title busTypeName,");
			announSql.append("\n a.modifyer,a.content,a.readState,a.busSpec");
			announSql.append("\n from todayWorks a inner join announcement v on a.busId=v.id");
			
			this.addSqlWhere(comId, announSql, args, " and a.comId= ? ");
			announSql.append("\n where v.delstate = 0 and a.busType='"+ConstantInterface.TYPE_ANNOUNCEMENT+"'");
			this.addSqlWhere(todayWorks.getReadState(), announSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), announSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), announSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(userId, announSql, args, " and a.userId=? ");;
		}
		//制度
		StringBuffer instituSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_INSTITUTION)>=0){
			instituSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,v.title busTypeName,");
			instituSql.append("\n a.modifyer,a.content,a.readState,a.busSpec");
			instituSql.append("\n from todayWorks a inner join institution v on a.busId=v.id");
			
			this.addSqlWhere(comId, instituSql, args, " and a.comId= ? ");
			instituSql.append("\n where v.delstate = 0 and a.busType='"+ConstantInterface.TYPE_INSTITUTION+"'");
			this.addSqlWhere(todayWorks.getReadState(), instituSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), instituSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), instituSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(userId, instituSql, args, " and a.userId=? ");;
		}
		//会议纪要
		StringBuffer meetSummarySql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_MEET_SUMMARY)>=0){
			meetSummarySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
			meetSummarySql.append("\n a.modifyer,a.content,a.readState,a.busSpec");
			meetSummarySql.append("\n  from todayWorks a inner join meetSummary summary on a.comId=summary.comId and a.busId=summary.id");
			meetSummarySql.append("\n inner join meeting meet on summary.comId=meet.comId and summary.meetingId=meet.id ");
			meetSummarySql.append("\n where a.busType='"+ConstantInterface.TYPE_MEET_SUMMARY+"'");
			this.addSqlWhere(todayWorks.getReadState(), meetSummarySql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), meetSummarySql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), meetSummarySql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, meetSummarySql, args, " and a.comId=?");
			this.addSqlWhere(userId, meetSummarySql, args, " and a.userId=? ");
		}
		
		//领款通知
		StringBuffer dawSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_NOTIFICATIONS)>=0){
			dawSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,");
			dawSql.append("\n a.modifyer,a.content,a.readState,a.busSpec");
			dawSql.append("\n  from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id");
			dawSql.append("\n where a.busType='"+ConstantInterface.TYPE_NOTIFICATIONS+"'");
			this.addSqlWhere(todayWorks.getReadState(), dawSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), dawSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), dawSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, dawSql, args, " and a.comId=?");
			this.addSqlWhere(userId, dawSql, args, " and a.userId=? ");
		}
		
		//外部联系人
		StringBuffer olmSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_OUTLINKMAN)>=0){
			olmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.linkManName busTypeName,");
			olmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec");
			olmSql.append("\n  from todayWorks a inner join OUTLINKMAN sp on a.comId=sp.comId and a.busId=sp.id");
			olmSql.append("\n where a.busType='"+ConstantInterface.TYPE_OUTLINKMAN+"'");
			this.addSqlWhere(todayWorks.getReadState(), olmSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), olmSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), olmSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, olmSql, args, " and a.comId=?");
			this.addSqlWhere(userId, olmSql, args, " and a.userId=? ");
		}
		
		//完成结算通知
		StringBuffer banSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_BALANCED)>=0){
			banSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,");
			banSql.append("\n a.modifyer,a.content,a.readState,a.busSpec");
			banSql.append("\n  from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id");
			banSql.append("\n where a.busType='"+ConstantInterface.TYPE_BALANCED+"'");
			this.addSqlWhere(todayWorks.getReadState(), banSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), banSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), banSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, banSql, args, " and a.comId=?");
			this.addSqlWhere(userId, banSql, args, " and a.userId=? ");
		}
		
		//财务结算通知
		StringBuffer fbSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_FINALCIAL_BALANCE)>=0){
			fbSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,");
			fbSql.append("\n a.modifyer,a.content,a.readState,a.busSpec");
			fbSql.append("\n  from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id");
			fbSql.append("\n where a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"'");
			this.addSqlWhere(todayWorks.getReadState(), fbSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), fbSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), fbSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, fbSql, args, " and a.comId=?");
			this.addSqlWhere(userId, fbSql, args, " and a.userId=? ");
		}
		
		//属性变更通知
		StringBuffer ceSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_CHANGE_EXAM)>=0){
			ceSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.busName busTypeName,");
			ceSql.append("\n a.modifyer,a.content,a.readState,a.busSpec");
			ceSql.append("\n  from todayWorks a inner join moduleChangeApply sp on a.comId=sp.comId and a.busId=sp.id");
			ceSql.append("\n where a.busType='"+ConstantInterface.TYPE_CHANGE_EXAM+"'");
			this.addSqlWhere(todayWorks.getReadState(), ceSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), ceSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), ceSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, ceSql, args, " and a.comId=?");
			this.addSqlWhere(userId, ceSql, args, " and a.userId=? ");
		}
		
		//催办
		StringBuffer busRemindSql = new StringBuffer();
		if(null == modList || modList.indexOf(ConstantInterface.TYPE_REMINDER)>=0){
			busRemindSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.busModName busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			busRemindSql.append("\n  from todayWorks a inner join busremind t on a.comId=t.comId and a.busId=t.id");
			busRemindSql.append("\n where a.busType='"+ConstantInterface.TYPE_REMINDER+"'");
			this.addSqlWhere(todayWorks.getReadState(), busRemindSql, args, " and a.readState=?");
			//判断是否需要查询闹铃
			if(null!=modList && null==source){
				busRemindSql.append(" and a.isClock=0 ");
			}
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), busRemindSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), busRemindSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, busRemindSql, args, " and a.comId=?");
			this.addSqlWhere(userId, busRemindSql, args, " and a.userId=? ");
		}
		
				
		//分享
		StringBuffer shareSql = new StringBuffer();
		if(null == modList || modList.indexOf("100")>=0){
			shareSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,TO_CHAR(m.content) busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			shareSql.append("\n  from todayWorks a inner join msgShare m on a.comId=m.comId and a.busId=m.id");
			shareSql.append("\n where  a.busType='1'");
			this.addSqlWhere(todayWorks.getReadState(), shareSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), shareSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), shareSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, shareSql, args, " and a.comId=?");
			this.addSqlWhere(userId, shareSql, args, " and a.userId=? ");
		}
		
		//普通闹铃提示
		StringBuffer clockSql = new StringBuffer();
		
		if(null == modList || modList.indexOf("101")>=0){
			if(null!=modList && modList.size()>0){
				clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.customername busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
				clockSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id");
				clockSql.append("\n where a.isClock=1 and a.busType='"+ConstantInterface.TYPE_CRM+"'");
				this.addSqlWhere(todayWorks.getReadState(), clockSql, args, " and a.readState=?");
				//查询创建时间段
				this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
				this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
				
				this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
				this.addSqlWhere(userId, clockSql, args, " and (a.userId=? or a.userId=0)");
				clockSql.append("\n union all ");
				clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.itemName busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
				clockSql.append("\n  from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
				clockSql.append("\n where a.isClock=1 and  a.busType='"+ConstantInterface.TYPE_ITEM+"'");
				this.addSqlWhere(todayWorks.getReadState(), clockSql, args, " and a.readState=?");
				//查询创建时间段
				this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
				this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
				
				this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
				this.addSqlWhere(userId, clockSql, args, " and a.userId=? ");
				clockSql.append("\n union all ");
				clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
				clockSql.append("\n  from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id");
				clockSql.append("\n where a.isClock=1 and a.busType='"+ConstantInterface.TYPE_TASK+"'");
				this.addSqlWhere(todayWorks.getReadState(), clockSql, args, " and a.readState=?");
				//查询创建时间段
				this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
				this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
				
				this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
				this.addSqlWhere(userId, clockSql, args, " and a.userId=? ");
				clockSql.append("\n union all ");
			}
			clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'普通闹铃' busTypeName,a.modifyer,a.content,a.readState,a.busSpec");
			clockSql.append("\n  from todayWorks a ");
			clockSql.append("\n where  a.busType='0' and a.busId=0 ");
			this.addSqlWhere(todayWorks.getReadState(), clockSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
			this.addSqlWhere(userId, clockSql, args, " and a.userId=? ");
		}
		
		sql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,a.busTypeName,a.content,a.readState,a.busSpec,a.modifyer,");
		sql.append("\n b.username modifyerName,b.gender,d.uuid modifyerUuid,d.filename from (");
		if(null == modList){//全部
			sql.append(taskSql);//003
			
			sql.append("\n union all ");
			sql.append(voteSql);//004
			sql.append("\n union all ");
			sql.append(itemSql);//005
			sql.append("\n union all ");
			sql.append(weekSql);//006
			sql.append("\n union all ");
			sql.append(dailySql);//050
			sql.append("\n union all ");
			sql.append(proSql);//050
			sql.append("\n union all ");
			sql.append(qasSql);//011
			sql.append("\n union all ");
			sql.append(crmSql);//012
			sql.append("\n union all ");
			sql.append(fileSql);//013
			sql.append("\n union all ");
			sql.append(invSql);//015
			sql.append("\n union all ");
			sql.append(schSql);//016
			sql.append("\n union all ");
			sql.append(meetSql);//017
			sql.append("\n union all ");
			sql.append(meetAppSql);//018
			sql.append("\n union all ");
			sql.append(spSql);//022
			sql.append("\n union all ");
			sql.append(spEndSql);//02201
			sql.append("\n union all ");
			
			sql.append(bgypPurSql);//027010
			sql.append("\n union all ");
			sql.append(bgypApplySql);//027020
			sql.append("\n union all ");
			sql.append(announSql);//039
			sql.append("\n union all ");
			sql.append(instituSql);//040
			sql.append("\n union all ");
			sql.append(meetSummarySql);//047
			sql.append("\n union all ");
			sql.append(dawSql);//103
			sql.append("\n union all ");
			sql.append(olmSql);//068
			sql.append("\n union all ");
			sql.append(banSql);//06602
			sql.append("\n union all ");
			sql.append(fbSql);//06601
			sql.append("\n union all ");
			sql.append(ceSql);//067
			sql.append("\n union all ");
			sql.append(busRemindSql);//99
			sql.append("\n union all ");
			sql.append(shareSql);//100
			sql.append("\n union all ");
			sql.append(clockSql);//101
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
					case 6:
						sql.append(weekSql);
						break;
					case 11:
						sql.append(qasSql);
						break;
					case 12:
						sql.append(crmSql);
						break;
					case 13:
						sql.append(fileSql);
						break;
					case 15:
						sql.append(invSql);
						break;
					case 16:
						sql.append(schSql);
						break;
					case 17:
					case 46:
						sql.append(meetSql);
						break;
					case 18:
						sql.append(meetAppSql);
						break;
					case 22:
						sql.append(spSql);
						break;
					case 2201:
						sql.append(spEndSql);
						break;
					case 27010:
						sql.append(bgypPurSql);
						break;
					case 27020:
						sql.append(bgypApplySql);
						break;
					case 39:
						sql.append(announSql);
						break;
					case 40:
						sql.append(instituSql);
						break;
					case 47:
						sql.append(meetSummarySql);//047
						break;
                    case 50:
                        sql.append(dailySql);//050
                        break;
					case 99:
						sql.append(busRemindSql);
						break;
					case 100:
						sql.append(shareSql);
						break;
					case 101:
						sql.append(clockSql);
						break;
					case 103:
						sql.append(dawSql);
						break;
					case 68:
						sql.append(olmSql);
						break;
					case 6602:
						sql.append(banSql);
						break;
					case 6601:
						sql.append(fbSql);
						break;
					case 67:
						sql.append(ceSql);
						break;
					case 80:
						sql.append(proSql);
						break;
					default:
						break;
						
					}
				if(i<modList.size()-1){
					sql.append("\n union all ");
				}
			}
		}
		sql.append("\n )a left join userinfo b on a.modifyer=b.id");
		sql.append("\n left join userOrganic c on a.modifyer=c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on d.id=c.mediumHeadPortrait and d.comId=c.comId");
		sql.append("\n where 1=1 ");
		this.addSqlWhereLike(todayWorks.getContent(), sql, args, " and (a.busTypeName like ? or a.content like ?)", 2);
		this.addSqlWhere(todayWorks.getBusId(), sql, args, " and a.busId=?");
		return this.listQuery(sql.toString(), args.toArray(), TodayWorks.class);
	}
	
	/**
	 * 查看是否需要更新数据
	 * @param comId 企业号
	 * @param busType 模块类型
	 * @param busId 业务主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TodayWorks> getUpdateCount(Integer comId,String busType,Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" select * from todayWorks where busSpec=1 and isClock=0");
		this.addSqlWhere(comId, sql, args, " and comId=?");
		this.addSqlWhere(busType, sql, args, " and busType=?");
		this.addSqlWhere(busId, sql, args, " and busId=?");
		return this.listQuery(sql.toString(), args.toArray(), TodayWorks.class);
	}
	
	/**
	 * 排在待办事项列表前五条数据集合
	 * @param userInfo
	 * @param num
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TodayWorks> firstNWorkList(UserInfo userInfo, Integer num) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享
		StringBuffer msgShareSql = new StringBuffer();
		msgShareSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		msgShareSql.append("\n case when a.isClock=0 then dbms_lob.substr(t.Content,4000) else a.content end busTypeName,");
		msgShareSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		msgShareSql.append("\n from todayWorks a inner join msgshare t on a.comId=t.comId and a.busId=t.id");
		msgShareSql.append("\n where a.busSpec=1 and a.busType='1'");
		
		this.addSqlWhere(userInfo.getComId(), msgShareSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), msgShareSql, args, " and a.userId=? ");
		//任务
		StringBuffer taskSql = new StringBuffer();
		taskSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		taskSql.append("\n case when a.isClock=0 then t.taskName else a.content end busTypeName,");
		taskSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,t.grade,t.grade neworder,a.isClock,a.clockId, 1 weekord");
		taskSql.append("\n from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id");
		taskSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_TASK+"'");
		
		this.addSqlWhere(userInfo.getComId(), taskSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), taskSql, args, " and a.userId=? ");
		
		//任务报延
		StringBuffer delayApplySql = new StringBuffer();
		delayApplySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		delayApplySql.append("\n case when a.isClock=0 then t.taskName else a.content end busTypeName,");
		delayApplySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		delayApplySql.append("\n from todayWorks a inner join delayApply b on a.comId = b.comId and a.busId = b.id inner join task t on b.comId=t.comId and b.taskid=t.id");
		delayApplySql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_DELAYAPPLY+"'");
		
		this.addSqlWhere(userInfo.getComId(), delayApplySql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), delayApplySql, args, " and a.userId=? ");
		
		//项目
		StringBuffer itemSql = new StringBuffer();
		itemSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		itemSql.append("\n case when a.isClock=0 then i.itemName else a.content end busTypeName,");
		itemSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		itemSql.append("\n from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
		itemSql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_ITEM+"'");
		
		this.addSqlWhere(userInfo.getComId(), itemSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), itemSql, args, " and a.userId=? ");
		//产品
		StringBuffer proSql = new StringBuffer();
		proSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		proSql.append("\n case when a.isClock=0 then pro.name else a.content end busTypeName,");
		proSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		proSql.append("\n from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id");
		proSql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_PRODUCT+"'");

		this.addSqlWhere(userInfo.getComId(), proSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), proSql, args, " and a.userId=? ");
		//客户
		StringBuffer crmSql = new StringBuffer();
		crmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		crmSql.append("\n case when a.isClock=0 then c.customername else a.content end  busTypeName,");
		crmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		crmSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id ");
		crmSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_CRM+"'");
		
		this.addSqlWhere(userInfo.getComId(), crmSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), crmSql, args, " and (a.userId=? or a.userId=0)");
		//审批
		StringBuffer spSql = new StringBuffer();
		spSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		spSql.append("\n case when a.isClock=0 then sp.flowname else a.content end busTypeName,");
		spSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		spSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id");
		spSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"' and sp.flowState=1");
		
		this.addSqlWhere(userInfo.getComId(), spSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), spSql, args, " and a.userId=? ");
		
		//审批完结
		StringBuffer spEndSql = new StringBuffer();
		spEndSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		spEndSql.append("\n case when a.isClock=0 then sp.flowname else a.content end busTypeName,");
		spEndSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		spEndSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id");
		spEndSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_SP_END+"'");
		
		this.addSqlWhere(userInfo.getComId(), spEndSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), spEndSql, args, " and a.userId=? ");
		
		//会议确认
		StringBuffer meetSql = new StringBuffer();
		meetSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
		meetSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		meetSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		meetSql.append("\n where a.busSpec=1 ");
		this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP},
				meetSql, args, "\n and a.busType in ? ");
		
		this.addSqlWhere(userInfo.getComId(), meetSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), meetSql, args, " and a.userId=? ");
		//会议申请
		StringBuffer meetAppSql = new StringBuffer();
		meetAppSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
		meetAppSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		meetAppSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		meetAppSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_MEETROOM+"'");
		
		this.addSqlWhere(userInfo.getComId(), meetAppSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), meetAppSql, args, " and a.userId=? ");
		
		//周报
		StringBuffer weekSql = new StringBuffer();
		weekSql.append("\n select aa.* ,rownum as weekord from (");
		weekSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.weekRepName busTypeName,");
		weekSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId");
		weekSql.append("\n from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id");
		weekSql.append("\n   left join userOrganic org on w.reporterId=org.userId and w.comId=org.comId");
		weekSql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_WEEK+"'");
		
		this.addSqlWhere(userInfo.getComId(), weekSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), weekSql, args, " and a.userId=? ");
		weekSql.append("\n  order by a.readState,a.busSpec desc,w.year desc,w.weekNum desc,org.depId,w.reporterId");
		weekSql.append("\n  )aa");

		//分享
		StringBuffer dailySql = new StringBuffer();
		dailySql.append("\n select aa.* ,rownum as weekord from (");
		dailySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.dailyName busTypeName,");
		dailySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId");
		dailySql.append("\n from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id");
		dailySql.append("\n   left join userOrganic org on w.reporterId=org.userId and w.comId=org.comId");
		dailySql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_DAILY+"'");

		this.addSqlWhere(userInfo.getComId(), dailySql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), dailySql, args, " and a.userId=? ");
		dailySql.append("\n  order by a.readState,a.busSpec desc,w.dailyDate desc,org.depId,w.reporterId");
		dailySql.append("\n  )aa");
		
		//申请
		StringBuffer invSql = new StringBuffer();
		invSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,jt.userName||'申请加入企业' busTypeName,");
		invSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		invSql.append("\n from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
		invSql.append("\n left join joinTemp jt on j.comId=jt.comId and j.account=jt.account");
		invSql.append("\n where a.busSpec=1 and   a.busType='"+ConstantInterface.TYPE_APPLY+"'");
		
		this.addSqlWhere(userInfo.getComId(), invSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), invSql, args, " and a.userId=? ");
		
		//采购单
		StringBuffer bgypPurSql = new StringBuffer();
		bgypPurSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'采购单:'||bpur.purOrderNum busTypeName,");
		bgypPurSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		bgypPurSql.append("\n from todayWorks a inner join bgypPurOrder bPur on a.comId=bPur.comId and a.busId=bPur.id");
		bgypPurSql.append("\n where a.busSpec=1 and a.busType ='"+ConstantInterface.TYPE_BGYP_BUY_CHECK+"'");
		
		this.addSqlWhere(userInfo.getComId(), bgypPurSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), bgypPurSql, args, " and a.userId=? ");
		
		//申领
		StringBuffer bgypApplySql = new StringBuffer();
		bgypApplySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'用品申领:'||bApply.applyDate busTypeName,");
		bgypApplySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		bgypApplySql.append("\n from todayWorks a inner join bgypApply bApply on a.comId=bApply.comId and a.busId=bApply.id");
		bgypApplySql.append("\n where a.busSpec=1 and a.busType ='"+ConstantInterface.TYPE_BGYP_APPLY_CHECK+"'");
		
		this.addSqlWhere(userInfo.getComId(), bgypApplySql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), bgypApplySql, args, " and a.userId=? ");
		
		//制度
		StringBuffer instituSql = new StringBuffer();
		instituSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		instituSql.append("\n case when a.isClock=0 then c.title else a.content end  busTypeName,");
		instituSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		instituSql.append("\n from todayWorks a inner join institution c on a.comId=c.comId and a.busId=c.id ");
		instituSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_INSTITUTION+"'");
		
		this.addSqlWhere(userInfo.getComId(), instituSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), instituSql, args, " and (a.userId=? or a.userId=0)");
		
		//会议纪要审批
		StringBuffer summarySql = new StringBuffer();
		summarySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		summarySql.append("\n case when a.isClock=0 then meet.title else a.content end busTypeName,");
		summarySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		summarySql.append("\n from todayWorks a inner join meetSummary summary on a.comId=summary.comId and a.busId=summary.id");
		summarySql.append("\n inner join meeting meet on a.comId=meet.comId and summary.meetingId=meet.id");
		summarySql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_MEET_SUMMARY+"'");
		
		this.addSqlWhere(userInfo.getComId(), summarySql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), summarySql, args, " and a.userId=? ");
		
		
		//领款通知
		StringBuffer dawSql = new StringBuffer();
		dawSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.flowName busTypeName,");
		dawSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		dawSql.append("\n from todayWorks a ");
		dawSql.append("\n inner join spFlowInstance b on a.busId=b.id and  a.comId=b.comId" );
		dawSql.append("\n where a.busSpec=1 and readState = '0' and  a.busType='"+ConstantInterface.TYPE_NOTIFICATIONS+"'");
		
		this.addSqlWhere(userInfo.getComId(), dawSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), dawSql, args, " and a.userId=? ");
		
		//外部联系人
		StringBuffer olmSql = new StringBuffer();
		olmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.linkManName busTypeName,");
		olmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		olmSql.append("\n from todayWorks a ");
		olmSql.append("\n inner join OUTLINKMAN b on a.busId=b.id and  a.comId=b.comId" );
		olmSql.append("\n where a.busSpec=1 and readState = '0' and  a.busType='"+ConstantInterface.TYPE_OUTLINKMAN+"'");
		
		this.addSqlWhere(userInfo.getComId(), olmSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), olmSql, args, " and a.userId=? ");
		
		//完成通知
		StringBuffer banSql = new StringBuffer();
		banSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.flowName busTypeName,");
		banSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		banSql.append("\n from todayWorks a ");
		banSql.append("\n inner join spFlowInstance b on a.busId=b.id and  a.comId=b.comId" );
		banSql.append("\n where a.busSpec=1 and readState = '0' and  a.busType='"+ConstantInterface.TYPE_BALANCED+"'");
		
		this.addSqlWhere(userInfo.getComId(), banSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), banSql, args, " and a.userId=? ");
		
		//财务结算通知
		StringBuffer fbSql = new StringBuffer();
		fbSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.flowName busTypeName,");
		fbSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		fbSql.append("\n from todayWorks a ");
		fbSql.append("\n inner join spFlowInstance b on a.busId=b.id and  a.comId=b.comId" );
		fbSql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"'");
		this.addSqlWhere(userInfo.getComId(), fbSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), fbSql, args, " and a.userId=? ");
		
		//属性变更通知
		StringBuffer ceSql = new StringBuffer();
		ceSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.busName busTypeName,");
		ceSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		ceSql.append("\n from todayWorks a ");
		ceSql.append("\n inner join moduleChangeApply b on a.busId=b.id and  a.comId=b.comId" );
		ceSql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_CHANGE_EXAM+"'");
		
		this.addSqlWhere(userInfo.getComId(), ceSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), ceSql, args, " and a.userId=? ");
		//催办
		StringBuffer busRemindSql = new StringBuffer();
		busRemindSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.busModName busTypeName,");
		busRemindSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		busRemindSql.append("\n from todayWorks a ");
		busRemindSql.append("\n inner join busremind b on a.busId=b.id and  a.comId=b.comId" );
		busRemindSql.append("\n where a.busSpec=1 and readState = '0' and   a.busType='"+ConstantInterface.TYPE_REMINDER+"'");
		
		this.addSqlWhere(userInfo.getComId(), busRemindSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), busRemindSql, args, " and a.userId=? ");
				
		//闹铃
		StringBuffer clockSql = new StringBuffer();
		clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,a.content busTypeName,");
		clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		clockSql.append("\n from todayWorks a ");
		clockSql.append("\n where a.busSpec=1 and  a.busType='0'");
		
		this.addSqlWhere(userInfo.getComId(), clockSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), clockSql, args, " and a.userId=? ");
		
		
		sql.append("\n select * from ( ");
		
		sql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,a.busTypeName,a.content,");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,grade,neworder,a.readState,\n");
		sql.append("\n a.busSpec,b.username modifyerName,b.gender,d.uuid modifyerUuid,a.isClock,a.clockId,a.weekord from (");
		sql.append(msgShareSql);
		sql.append("\n union all ");
		sql.append(taskSql);
		sql.append("\n union all ");
		sql.append(delayApplySql);//任务报延
		sql.append("\n union all ");
		sql.append(itemSql);
		sql.append("\n union all ");
		sql.append(crmSql);
		sql.append("\n union all ");
		sql.append(spSql);
		sql.append("\n union all ");
		sql.append(spEndSql);
		sql.append("\n union all ");
		sql.append(meetSql);
		sql.append("\n union all ");
		sql.append(meetAppSql);
		sql.append("\n union all ");
		sql.append(weekSql);
		sql.append("\n union all ");
		sql.append(dailySql);
		sql.append("\n union all ");
		sql.append(invSql);
		sql.append("\n union all ");
		sql.append(bgypPurSql);
		sql.append("\n union all ");
		sql.append(bgypApplySql);
		sql.append("\n union all ");
		sql.append(instituSql);
		sql.append("\n union all ");
		sql.append(summarySql);
		sql.append("\n union all ");
		sql.append(dawSql);
		sql.append("\n union all ");
		sql.append(olmSql);
		sql.append("\n union all ");
		sql.append(banSql);
		sql.append("\n union all ");
		sql.append(fbSql);
		sql.append("\n union all ");
		sql.append(ceSql);
		sql.append("\n union all ");
		sql.append(busRemindSql);
		sql.append("\n union all ");
		sql.append(clockSql);
		sql.append("\n union all ");
		sql.append(proSql);
		sql.append("\n )a left join attention atten on a.comId=atten.comId and a.busType=atten.busType and a.busId=atten.busId and atten.userId="+userInfo.getId());
		sql.append("\n left join userinfo b on a.modifyer=b.id");
		sql.append("\n left join userOrganic c on a.modifyer=c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on d.id=c.mediumHeadPortrait and d.comId=c.comId");
		sql.append("\n order by a.readState,a.neworder desc,a.weekord,a.recordCreateTime desc,a.busType ");
		sql.append("\n ) where rownum <= ? ");
		args.add(num);
		return this.listQuery(sql.toString(), args.toArray(), TodayWorks.class);
	}
	
	/**
	 * index
	 * 获取全部消息提醒数目
	 * @param comId 企业号
	 * @param userId 当前用户主键
	 * @return
	 */
	public Integer countTodo(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(*)from (");
		//客户模块
		sql.append("\n	 select a.* from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id ");
		sql.append("\n where  a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_CRM+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//项目模块
		sql.append("\n	 union all ");
		sql.append("\n	 select a.* from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id ");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_ITEM+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//产品模块
		sql.append("\n	 union all ");
		sql.append("\n	 select a.* from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id ");
		sql.append("\n 	 where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_PRODUCT+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//任务模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_TASK+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//制度模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join institution t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_INSTITUTION+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//周报模块
		sql.append("\n union all");
		sql.append("\n select a.* from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id ");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_WEEK+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//分享模块
		sql.append("\n union all");
		sql.append("\n select a.* from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id ");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_DAILY+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//会议确认
		sql.append("\n union all");
		sql.append("\n select a.* from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id ");
		sql.append("\n where a.busSpec=1 ");
		this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP},
				sql, args, "\n and a.busType in ? ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//会议申请
		sql.append("\n union all");
		sql.append("\n select a.* from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id ");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_MEETROOM+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//加入记录模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_APPLY+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//闹铃待办
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a ");
		sql.append("\n where a.busSpec=1 and a.busType='0' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//审批完结待办
		sql.append("\n union all");
		sql.append("\n select a.* from todayWorks a ");
		sql.append("\n where a.busSpec='1' and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_SP_END+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("  and (a.userId=? or a.userId=0) ");
		args.add(userId);

		//审批待办
		sql.append("\n union all");
		sql.append("\n select a.* from todayWorks a ");
		sql.append("\n inner join spflowinstance sp on a.busId=sp.id and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"' and sp.flowstate=1");
		sql.append("\n inner join spflowcurexecutor spUser on sp.id=spUser.busId and sp.comId=spUser.comId and spUser.executor=? and spUser.busId=?");
		args.add(userId);
		args.add(ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0) ");
		args.add(userId);
		
		//会议纪要审批
		sql.append("\n union all");
		sql.append("\n select a.* from todayWorks a inner join meetSummary summary on a.comId=summary.comId and a.busId=summary.id ");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_MEET_SUMMARY+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//领款通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join spflowinstance t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_NOTIFICATIONS+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//外部联系人
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join OUTLINKMAN t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_OUTLINKMAN+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//完成结算通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join spflowinstance t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_BALANCED+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//财务结算通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join spflowinstance t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//属性变更通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join moduleChangeApply t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_CHANGE_EXAM+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//催办模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join busremind t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_REMINDER+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
				
		
		sql.append(" )");
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 关注信息未读数
	 * @param comId 企业号
	 * @param userId 操作人员
	 * @return
	 */
	public Integer countAttenNoRead(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		//总的sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(*)from (");
		sql.append("\n  select case when(");
		sql.append("\n  select count(*) from");
		sql.append("\n  todayworks today where a.comId = today.comId and a.busId = today.busId	");
		sql.append("\n  and today.busType=a.busType and today.userId='"+userId+"' and today.isClock=0");
		sql.append("\n and today.readState=0)=0 then 0 else 1 end as isread");
		sql.append("\n   from (");
		//客户
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join customer b on a.busId=b.id ");
		sql.append("\n and  b.delState=0 and a.busType="+ConstantInterface.TYPE_CRM);
		sql.append("\n where 1=1");

		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		
		//项目
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join item b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_ITEM);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		
		//任务
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join task b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_TASK);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
			
		//问答
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join question b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_QUES);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		//投票
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join vote b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_VOTE);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		//分享
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join msgshare b on a.busId=b.id and a.busType=1");
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		//公告
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join announcement b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_ANNOUNCEMENT);
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join institution b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_INSTITUTION);
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n ) a ");
		sql.append("\n ) a where  isread=1");
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 首页待办点击后补充数据
	 * @param userInfo 当前操作人员
	 * @param num 页面需要展示的数据条数
	 * @param todoIds 页面点击前的待办主键
	 * @param leftNum 页面点击后剩下的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TodayWorks> otherNWorkList(UserInfo userInfo, Integer num, String todoIds, Integer leftNum) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//客户
		StringBuffer crmSql = new StringBuffer();
		crmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		crmSql.append("\n case when a.isClock=0 then c.customername else a.content end  busTypeName,");
		crmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		crmSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id ");
		crmSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_CRM+"'");
		
		this.addSqlWhere(userInfo.getComId(), crmSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), crmSql, args, " and (a.userId=? or a.userId=0)");
		//项目
		StringBuffer itemSql = new StringBuffer();
		itemSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		itemSql.append("\n case when a.isClock=0 then i.itemName else a.content end busTypeName,");
		itemSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		itemSql.append("\n from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
		itemSql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_ITEM+"'");
		
		this.addSqlWhere(userInfo.getComId(), itemSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), itemSql, args, " and a.userId=? ");

		//产品
		StringBuffer proSql = new StringBuffer();
		proSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		proSql.append("\n case when a.isClock=0 then pro.name else a.content end busTypeName,");
		proSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		proSql.append("\n from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id");
		proSql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_PRODUCT+"'");

		this.addSqlWhere(userInfo.getComId(), proSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), proSql, args, " and a.userId=? ");
		
		//任务
		StringBuffer taskSql = new StringBuffer();
		taskSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		taskSql.append("\n case when a.isClock=0 then t.taskName else a.content end busTypeName,");
		taskSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,t.grade,t.grade neworder,a.isClock,a.clockId, 1 weekord");
		taskSql.append("\n from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id");
		taskSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_TASK+"'");
		
		this.addSqlWhere(userInfo.getComId(), taskSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), taskSql, args, " and a.userId=? ");
		
		//审批
		StringBuffer spSql = new StringBuffer();
		spSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		spSql.append("\n case when a.isClock=0 then sp.flowName else a.content end busTypeName,");
		spSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		spSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id and sp.flowState=1");
		spSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"'");
		
		this.addSqlWhere(userInfo.getComId(), spSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), spSql, args, " and a.userId=? ");
		
		//审批完结
		StringBuffer spEndSql = new StringBuffer();
		spEndSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
		spEndSql.append("\n case when a.isClock=0 then sp.flowName else a.content end busTypeName,");
		spEndSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		spEndSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id ");
		spEndSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_SP_END+"'");
		
		this.addSqlWhere(userInfo.getComId(), spEndSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), spEndSql, args, " and a.userId=? ");
		
		//会议确认
		StringBuffer meetSql = new StringBuffer();
		meetSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
		meetSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		meetSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		meetSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_MEETING+"'");
		
		this.addSqlWhere(userInfo.getComId(), meetSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), meetSql, args, " and a.userId=? ");
		//会议申请
		StringBuffer meetAppSql = new StringBuffer();
		meetAppSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
		meetAppSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		meetAppSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		meetAppSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_MEETROOM+"'");
		
		this.addSqlWhere(userInfo.getComId(), meetAppSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), meetAppSql, args, " and a.userId=? ");
		
		//周报
		StringBuffer weekSql = new StringBuffer();
		weekSql.append("\n select aa.* ,rownum as weekord from (");
		weekSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.weekRepName busTypeName,");
		weekSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId");
		weekSql.append("\n from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id");
		weekSql.append("\n   left join userOrganic org on w.reporterId=org.userId and w.comId=org.comId");
		weekSql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_WEEK+"'");
		
		this.addSqlWhere(userInfo.getComId(), weekSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), weekSql, args, " and a.userId=? ");
		weekSql.append("\n  order by a.readState,a.busSpec desc,w.year desc,w.weekNum desc,org.depId,w.reporterId");
		weekSql.append("\n  )aa");

		//分享
		StringBuffer dailySql = new StringBuffer();
		dailySql.append("\n select aa.* ,rownum as weekord from (");
		dailySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.dailyName busTypeName,");
		dailySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId");
		dailySql.append("\n from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id");
		dailySql.append("\n   left join userOrganic org on w.reporterId=org.userId and w.comId=org.comId");
		dailySql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_DAILY+"'");

		this.addSqlWhere(userInfo.getComId(), dailySql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), dailySql, args, " and a.userId=? ");
		dailySql.append("\n  order by a.readState,a.busSpec desc,w.dailyDate desc,org.depId,w.reporterId");
		dailySql.append("\n  )aa");
		
		//申请
		StringBuffer invSql = new StringBuffer();
		invSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,jt.userName||'申请加入企业' busTypeName,");
		invSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		invSql.append("\n from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
		invSql.append("\n left join joinTemp jt on j.comId=jt.comId and j.account=jt.account");
		invSql.append("\n where a.busSpec=1 and   a.busType='"+ConstantInterface.TYPE_APPLY+"'");
		
		this.addSqlWhere(userInfo.getComId(), invSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), invSql, args, " and a.userId=? ");
		//闹铃
		StringBuffer clockSql = new StringBuffer();
		clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,a.content busTypeName,");
		clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,'0' grade,'1' neworder,a.isClock,a.clockId, 1 weekord");
		clockSql.append("\n from todayWorks a ");
		clockSql.append("\n where a.busSpec=1 and  a.busType='0'");
		
		this.addSqlWhere(userInfo.getComId(), clockSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), clockSql, args, " and a.userId=? ");
		
		
		sql.append("\n select * from ( ");
		
		sql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,a.busTypeName,a.content,");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,grade,neworder,a.readState,\n");
		sql.append("\n a.busSpec,b.username modifyerName,b.gender,d.uuid modifyerUuid,a.isClock,a.clockId,a.weekord from (");
		sql.append(crmSql);
		sql.append("\n union all ");
		sql.append(itemSql);
		sql.append("\n union all ");
		sql.append(proSql);
		sql.append("\n union all ");
		sql.append(taskSql);
		sql.append("\n union all ");
		sql.append(spSql);
		sql.append("\n union all ");
		sql.append(spEndSql);
		sql.append("\n union all ");
		sql.append(meetSql);
		sql.append("\n union all ");
		sql.append(meetAppSql);
		sql.append("\n union all ");
		sql.append(weekSql);
		sql.append("\n union all ");
		sql.append(dailySql);
		sql.append("\n union all ");
		sql.append(invSql);
		sql.append("\n union all ");
		sql.append(clockSql);
		sql.append("\n )a left join attention atten on a.comId=atten.comId and a.busType=atten.busType and a.busId=atten.busId and atten.userId="+userInfo.getId());
		sql.append("\n left join userinfo b on a.modifyer=b.id");
		sql.append("\n left join userOrganic c on a.modifyer=c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on d.id=c.mediumHeadPortrait and d.comId=c.comId");
		sql.append("\n where a.id not in ("+todoIds+") ");
		sql.append("\n order by a.readState,a.neworder desc,a.weekord,a.recordCreateTime desc,a.busType ");
		sql.append("\n ) where rownum <= ? ");
		args.add(num-leftNum);
		return this.listQuery(sql.toString(), args.toArray(), TodayWorks.class);
	}
	
	/**
	 * 取得待办事项的详情
	 * @param id 待办事项的主键
	 * @param busId 业务类型
	 * @param type 业务主键
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public TodayWorks getMsgTodoById(Integer id, String type, Integer busId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//客户
		StringBuffer crmSql = new StringBuffer();
		if(ConstantInterface.TYPE_CRM.equals(type)){
			//是待办事项（常规待办和闹铃待办）
			crmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.customername busTypeName,");
			crmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			crmSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id");
			crmSql.append("\n where a.busspec=1 and  a.busType='"+ConstantInterface.TYPE_CRM+"'");
			
			this.addSqlWhere(id, crmSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), crmSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), crmSql, args, " and (a.userId=? or a.userId=0)");
			
			crmSql.append("\n union all ");
			//是查看后闹铃
			crmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.customername busTypeName,");
			crmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			crmSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id");
			crmSql.append("\n where a.busspec=0 and  a.busType='"+ConstantInterface.TYPE_CRM+"' and isClock=1");
			
			this.addSqlWhere(id, crmSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), crmSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), crmSql, args, " and (a.userId=? or a.userId=0)");
		}
		//项目
		StringBuffer itemSql = new StringBuffer();
		if(ConstantInterface.TYPE_ITEM.equals(type)){
			//是待办事项（常规待办和闹铃待办）
			itemSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.itemName busTypeName,");
			itemSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			itemSql.append("\n  from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
			itemSql.append("\n where a.busspec=1 and  a.busType='"+ConstantInterface.TYPE_ITEM+"'");
			
			this.addSqlWhere(id, itemSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), itemSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), itemSql, args, " and a.userId=? ");
			
			itemSql.append("\n union all ");
			//是查看后闹铃
			itemSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.itemName busTypeName,");
			itemSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			itemSql.append("\n  from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
			itemSql.append("\n where a.busspec=0 and  a.busType='"+ConstantInterface.TYPE_ITEM+"' and isClock=1");
			
			this.addSqlWhere(id, itemSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), itemSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), itemSql, args, " and a.userId=? ");
		}
		//产品
		StringBuffer proSql = new StringBuffer();
		if(ConstantInterface.TYPE_ITEM.equals(type)){
			//是待办事项（常规待办和闹铃待办）
			proSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,pro.name busTypeName,");
			proSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			proSql.append("\n  from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id");
			proSql.append("\n where a.busspec=1 and  a.busType='"+ConstantInterface.TYPE_ITEM+"'");

			this.addSqlWhere(id, proSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), proSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), proSql, args, " and a.userId=? ");

			proSql.append("\n union all ");
			//是查看后闹铃
			proSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,pro.name busTypeName,");
			proSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			proSql.append("\n  from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id");
			proSql.append("\n where a.busspec=0 and  a.busType='"+ConstantInterface.TYPE_PRODUCT+"' and isClock=1");

			this.addSqlWhere(id, proSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), proSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), proSql, args, " and a.userId=? ");
		}
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(ConstantInterface.TYPE_TASK.equals(type)){
			//是待办事项（常规待办和闹铃待办）
			taskSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,");
			taskSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			taskSql.append("\n  from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id");
			taskSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_TASK+"'");
			
			this.addSqlWhere(id, taskSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), taskSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), taskSql, args, " and a.userId=? ");
			
			taskSql.append("\n union all");
			//是查看后闹铃
			taskSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,");
			taskSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			taskSql.append("\n  from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id");
			taskSql.append("\n where a.busspec=0 and a.busType='"+ConstantInterface.TYPE_TASK+"' and isClock=1");
			
			this.addSqlWhere(id, taskSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), taskSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), taskSql, args, " and a.userId=? ");
		}
		//周报
		StringBuffer weekSql = new StringBuffer();
		if(ConstantInterface.TYPE_WEEK.equals(type)){
			weekSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.weekRepName busTypeName,");
			weekSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			weekSql.append("\n  from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id");
			weekSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_WEEK+"'");
			
			this.addSqlWhere(id, weekSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), weekSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), weekSql, args, " and a.userId=? ");
		}
		//分享
		StringBuffer dailySql = new StringBuffer();
		if(ConstantInterface.TYPE_DAILY.equals(type)){
			dailySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.dailyName busTypeName,");
			dailySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			dailySql.append("\n  from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id");
			dailySql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_DAILY+"'");

			this.addSqlWhere(id, dailySql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), dailySql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), dailySql, args, " and a.userId=? ");
		}
		//申请
		StringBuffer invSql = new StringBuffer();
		if(ConstantInterface.TYPE_APPLY.equals(type)){
			invSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,jt.userName||'申请加入企业' busTypeName,");
			invSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			invSql.append("\n  from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
			invSql.append("\n left join joinTemp jt on j.comId=jt.comId and j.account=jt.account");
			invSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_APPLY+"'");
			
			this.addSqlWhere(id, invSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), invSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), invSql, args, " and a.userId=? ");
		}
		//领款通知
		StringBuffer dawSql = new StringBuffer();
		if(ConstantInterface.TYPE_NOTIFICATIONS.equals(type)){
			dawSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.flowName busTypeName,");
			dawSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			dawSql.append("\n  from todayWorks a inner join spFlowInstance w on a.comId=w.comId and a.busId=w.id");
			dawSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_NOTIFICATIONS+"'");

			this.addSqlWhere(id, dawSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), dawSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), dawSql, args, " and a.userId=? ");
		}
		
		//外部联系人
		StringBuffer olmSql = new StringBuffer();
		if(ConstantInterface.TYPE_OUTLINKMAN.equals(type)){
			olmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.linkmanName busTypeName,");
			olmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			olmSql.append("\n  from todayWorks a inner join OUTLINKMAN w on a.comId=w.comId and a.busId=w.id");
			olmSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_OUTLINKMAN+"'");

			this.addSqlWhere(id, olmSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), olmSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), olmSql, args, " and a.userId=? ");
		}
		
		//完成结算通知
		StringBuffer banSql = new StringBuffer();
		if(ConstantInterface.TYPE_BALANCED.equals(type)){
			banSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.flowName busTypeName,");
			banSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			banSql.append("\n  from todayWorks a inner join spFlowInstance w on a.comId=w.comId and a.busId=w.id");
			banSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_BALANCED+"'");

			this.addSqlWhere(id, banSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), banSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), banSql, args, " and a.userId=? ");
		}
		
		//财务结算通知
		StringBuffer fbSql = new StringBuffer();
		if(ConstantInterface.TYPE_FINALCIAL_BALANCE.equals(type)){
			fbSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.flowName busTypeName,");
			fbSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			fbSql.append("\n  from todayWorks a inner join spFlowInstance w on a.comId=w.comId and a.busId=w.id");
			fbSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"'");

			this.addSqlWhere(id, fbSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), fbSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), fbSql, args, " and a.userId=? ");
		}
		
		//属性变更通知
		StringBuffer ceSql = new StringBuffer();
		if(ConstantInterface.TYPE_CHANGE_EXAM.equals(type)){
			ceSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.busName busTypeName,");
			ceSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			ceSql.append("\n  from todayWorks a inner join moduleChangeApply w on a.comId=w.comId and a.busId=w.id");
			ceSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_CHANGE_EXAM+"'");

			this.addSqlWhere(id, ceSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), ceSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), ceSql, args, " and a.userId=? ");
		}
		//闹铃
		StringBuffer clockSql = new StringBuffer();
		if("0".equals(type)){
			clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'闹铃定时' busTypeName,");
			clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId");
			clockSql.append("\n from todayWorks a ");
			clockSql.append("\n where a.busType='0' and (a.busId=0 or a.busId=a.clockId) ");
			
			this.addSqlWhere(id, clockSql, args, " and a.Id=?");
			this.addSqlWhere(userInfo.getComId(), clockSql, args, " and a.comId=?");
			this.addSqlWhere(userInfo.getId(), clockSql, args, " and a.userId=? ");
		}
		
		
		sql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,a.busTypeName,a.content,a.readState,a.busSpec,");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,\n");
		sql.append("\n b.username modifyerName,b.gender,d.uuid modifyerUuid,a.isClock,a.clockId from (");
		
		if(ConstantInterface.TYPE_CRM.equals(type)){//只客户
			sql.append(crmSql);
		}else if(ConstantInterface.TYPE_ITEM.equals(type)){//只项目
			sql.append(itemSql);
		}else if(ConstantInterface.TYPE_PRODUCT.equals(type)){//只产品
			sql.append(proSql);
		}else if(ConstantInterface.TYPE_TASK.equals(type)){//只任务
			sql.append(taskSql);
		}else if(ConstantInterface.TYPE_WEEK.equals(type)){//只周报
			sql.append(weekSql);
		}else if(ConstantInterface.TYPE_DAILY.equals(type)){//只分享
			sql.append(weekSql);
		}else if(ConstantInterface.TYPE_APPLY.equals(type)){//只申请
			sql.append(invSql);
		}else if(ConstantInterface.TYPE_NOTIFICATIONS.equals(type)){//只领款通知
			sql.append(dawSql);
		}else if(ConstantInterface.TYPE_OUTLINKMAN.equals(type)){//只外部联系人
			sql.append(olmSql);
		}else if(ConstantInterface.TYPE_BALANCED.equals(type)){//只完成结算通知
			sql.append(banSql);
		}else if(ConstantInterface.TYPE_FINALCIAL_BALANCE.equals(type)){//只领款通知
			sql.append(fbSql);
		}else if(ConstantInterface.TYPE_CHANGE_EXAM.equals(type)){//只属性变更通知
			sql.append(ceSql);
		}else if("0".equals(type)){//只申请
			sql.append(clockSql);
		}
		sql.append("\n )a left join attention atten on a.comId=atten.comId and a.busType=atten.busType and a.busId=atten.busId and atten.userId="+userInfo.getId());
		sql.append("\n left join userinfo b on a.modifyer=b.id");
		sql.append("\n left join userOrganic c on a.modifyer=c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on d.id=c.mediumHeadPortrait and d.comId=c.comId");
		sql.append("\n where 1=1 ");
		return (TodayWorks) this.objectQuery(sql.toString(), args.toArray(), TodayWorks.class);
	}
	
	/**
	 * 取得未读信息
	 * @param userInfo
	 * @return
	 */
	public TodayWorks getMsgNoRead(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//客户
		StringBuffer crmSql = new StringBuffer();
		crmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.customername busTypeName,");
		crmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		crmSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id");
		crmSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_CRM+"'");
		
		this.addSqlWhere(userInfo.getComId(), crmSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), crmSql, args, " and a.userId=? ");
		
		//日程
		StringBuffer schSql = new StringBuffer();
		schSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.title busTypeName,");
		schSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		schSql.append("\n  from todayWorks a inner join SCHEDULE i on a.comId=i.comId and a.busId=i.id");
		schSql.append("\n where a.readState=0 and a.readState=0 and  a.busType='"+ConstantInterface.TYPE_SCHEDULE+"'");
		
		this.addSqlWhere(userInfo.getComId(), schSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), schSql, args, " and a.userId=? ");
		//项目
		StringBuffer itemSql = new StringBuffer();
		itemSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.itemName busTypeName,");
		itemSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		itemSql.append("\n  from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
		itemSql.append("\n where a.readState=0 and a.readState=0 and  a.busType='"+ConstantInterface.TYPE_ITEM+"'");
		
		this.addSqlWhere(userInfo.getComId(), itemSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), itemSql, args, " and a.userId=? ");
		//产品
		StringBuffer proSql = new StringBuffer();
		proSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,pro.name busTypeName,");
		proSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		proSql.append("\n  from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id");
		proSql.append("\n where a.readState=0 and a.readState=0 and  a.busType='" + ConstantInterface.TYPE_PRODUCT + "'");

		this.addSqlWhere(userInfo.getComId(), proSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), proSql, args, " and a.userId=? ");
		//任务
		StringBuffer taskSql = new StringBuffer();
		taskSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,a.modifyer,");
		taskSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		taskSql.append("\n from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id");
		taskSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_TASK+"'");
		
		this.addSqlWhere(userInfo.getComId(), taskSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), taskSql, args, " and a.userId=? ");
		//审批
		StringBuffer spSql = new StringBuffer();
		spSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,a.modifyer,");
		spSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		spSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id and sp.flowState=1");
		spSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"'");
		
		this.addSqlWhere(userInfo.getComId(), spSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), spSql, args, " and a.userId=? ");
		
		//审批(完结)
		StringBuffer spEndSql = new StringBuffer();
		spEndSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,a.modifyer,");
		spEndSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		spEndSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id ");
		spEndSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_SP_END+"'");
		
		this.addSqlWhere(userInfo.getComId(), spEndSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), spEndSql, args, " and a.userId=? ");
		
		//问答
		StringBuffer qasSql = new StringBuffer();
		qasSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,q.title busTypeName,");
		qasSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		qasSql.append("\n  from todayWorks a inner join question q on a.comId=q.comId and a.busId=q.id");
		qasSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_QUES+"'");
		this.addSqlWhere(userInfo.getComId(), qasSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), qasSql, args, " and a.userId=? ");
		//投票
		StringBuffer voteSql = new StringBuffer();
		voteSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,dbms_lob.substr(v.voteContent,4000) busTypeName,");
		voteSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		voteSql.append("\n  from todayWorks a inner join vote v on a.comId=v.comId and a.busId=v.id");
		voteSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_VOTE+"'");
		this.addSqlWhere(userInfo.getComId(), voteSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), voteSql, args, " and a.userId=? ");
		//周报
		StringBuffer weekSql = new StringBuffer();
		weekSql.append("\n select aa.*,rownum as weekord from (");
		weekSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.weekRepName busTypeName,");
		weekSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId");
		weekSql.append("\n  from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id");
		weekSql.append("\n  left join userOrganic org on w.reporterId=org.userId and w.comId=org.comId");
		weekSql.append("\n where a.readState=0 and  a.busType='"+ConstantInterface.TYPE_WEEK+"'");
		
		this.addSqlWhere(userInfo.getComId(), weekSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), weekSql, args, " and a.userId=? ");
		weekSql.append("\n  order by a.readState,a.busSpec desc,w.year desc,w.weekNum desc,org.depId,w.reporterId");
		weekSql.append("\n )aa");

		//分享
		StringBuffer dailySql = new StringBuffer();
		dailySql.append("\n select aa.*,rownum as weekord from (");
		dailySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.dailyName busTypeName,");
		dailySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId");
		dailySql.append("\n  from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id");
		dailySql.append("\n  left join userOrganic org on w.reporterId=org.userId and w.comId=org.comId");
		dailySql.append("\n where a.readState=0 and  a.busType='"+ConstantInterface.TYPE_DAILY+"'");

		this.addSqlWhere(userInfo.getComId(), dailySql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), dailySql, args, " and a.userId=? ");
		dailySql.append("\n  order by a.readState,a.busSpec desc,w.dailyDate desc,org.depId,w.reporterId");
		dailySql.append("\n )aa");

		//附件
		StringBuffer fileSql = new StringBuffer();
		fileSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,f.fileDescribe busTypeName,");
		fileSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		fileSql.append("\n  from todayWorks a inner join fileDetail f on a.comId=f.comId and a.busId=f.id");
		fileSql.append("\n where a.readState=0 and  a.busType='"+ConstantInterface.TYPE_FILE+"'");
		
		this.addSqlWhere(userInfo.getComId(), fileSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), fileSql, args, " and a.userId=? ");
		//分享
		StringBuffer shareSql = new StringBuffer();
		shareSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,TO_CHAR(m.content) busTypeName,");
		shareSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		shareSql.append("\n  from todayWorks a inner join msgShare m on a.comId=m.comId and a.busId=m.id");
		shareSql.append("\n where a.readState=0 and  a.busType='1'");
		
		this.addSqlWhere(userInfo.getComId(), shareSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), shareSql, args, " and a.userId=? ");
		//申请
		StringBuffer invSql = new StringBuffer();
		invSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,jt.userName||'申请加入企业' busTypeName,");
		invSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		invSql.append("\n  from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
		invSql.append("\n left join joinTemp jt on j.comId=jt.comId and j.account=jt.account");
		invSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_APPLY+"'");
		
		this.addSqlWhere(userInfo.getComId(), invSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), invSql, args, " and a.userId=? ");
		//会议
		StringBuffer meetSql = new StringBuffer();
		meetSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
		meetSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,0 isClock,0 clockId,a.roomId,1 weekord");
		meetSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		meetSql.append("\n where a.readState=0 ");
		this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP},
				meetSql, args, "\n and a.busType in ? ");
		
		this.addSqlWhere(userInfo.getComId(), meetSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), meetSql, args, " and a.userId=? ");
		//会议申请
		StringBuffer meetAppSql = new StringBuffer();
		meetAppSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
		meetAppSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,0 isClock,0 clockId,a.roomId,1 weekord");
		meetAppSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		meetAppSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_MEETROOM+"'");
		
		this.addSqlWhere(userInfo.getComId(), meetAppSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), meetAppSql, args, " and a.userId=? ");
		
		//普通消息
		StringBuffer noticeSql = new StringBuffer();
		noticeSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'普通消息' busTypeName,");
		noticeSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,0 isClock,0 clockId,a.roomId,1 weekord");
		noticeSql.append("\n  from todayWorks a ");
		noticeSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_NOTICE+"'");
		
		this.addSqlWhere(userInfo.getComId(), noticeSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), noticeSql, args, " and a.userId=? ");
		
		//采购单
		StringBuffer bgypPurSql = new StringBuffer();
		bgypPurSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'采购单:'||bpur.purOrderNum busTypeName,a.modifyer,");
		bgypPurSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		bgypPurSql.append("\n from todayWorks a inner join bgypPurOrder bPur on a.comId=bPur.comId and a.busId=bPur.id");
		bgypPurSql.append("\n where a.readState=0 and a.busType in ('"+ConstantInterface.TYPE_BGYP_BUY_CHECK+"','"+ConstantInterface.TYPE_BGYP_BUY_NOTICE+"')");
		
		this.addSqlWhere(userInfo.getComId(), bgypPurSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), bgypPurSql, args, " and a.userId=? ");
		
		//采购单
		StringBuffer bgypApplySql = new StringBuffer();
		bgypApplySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'用品申领:'||bapply.applyDate busTypeName,a.modifyer,");
		bgypApplySql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		bgypApplySql.append("\n from todayWorks a inner join bgypApply bApply on a.comId=bApply.comId and a.busId=bApply.id");
		bgypApplySql.append("\n where a.readState=0 and a.busType in ('"+ConstantInterface.TYPE_BGYP_APPLY_CHECK+"','"+ConstantInterface.TYPE_BGYP_APPLY_NOTICE+"')");
		
		this.addSqlWhere(userInfo.getComId(), bgypApplySql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), bgypApplySql, args, " and a.userId=? ");

		//制度
		StringBuffer instituSql = new StringBuffer();
		instituSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.title busTypeName,a.modifyer,");
		instituSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		instituSql.append("\n from todayWorks a inner join institution b on a.comId=b.comId and a.busId=b.id");
		instituSql.append("\n where a.readState=0 and a.busType = '"+ConstantInterface.TYPE_INSTITUTION+"'");
		
		this.addSqlWhere(userInfo.getComId(), instituSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), instituSql, args, " and a.userId=? ");
		
		//公告
		StringBuffer announSql = new StringBuffer();
		announSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.title busTypeName,a.modifyer,");
		announSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		announSql.append("\n from todayWorks a inner join announcement b on a.comId=b.comId and a.busId=b.id");
		announSql.append("\n where a.readState=0 and a.busType = '"+ConstantInterface.TYPE_ANNOUNCEMENT+"'");
		
		this.addSqlWhere(userInfo.getComId(), announSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), announSql, args, " and a.userId=? ");
		
		//会议纪要审批
		StringBuffer summarySql = new StringBuffer();
		summarySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
		summarySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,0 isClock,0 clockId,a.roomId,1 weekord");
		summarySql.append("\n  from todayWorks a inner join meetSummary summary on a.comId=summary.comId and a.busId=summary.id");
		summarySql.append("\n  inner join meeting meet on a.comId=meet.comId and summary.meetingId=meet.id");
		summarySql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_MEET_SUMMARY+"'");
		
		this.addSqlWhere(userInfo.getComId(), summarySql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), summarySql, args, " and a.userId=? ");
		
		//领款通知
		StringBuffer dawSql = new StringBuffer();
		dawSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.flowName busTypeName,a.modifyer,");
		dawSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		dawSql.append("\n from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id");
		dawSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_NOTIFICATIONS+"'");
		
		this.addSqlWhere(userInfo.getComId(), dawSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), dawSql, args, " and a.userId=? ");
		
		//外部联系人
		StringBuffer olmSql = new StringBuffer();
		olmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.linkmanName busTypeName,a.modifyer,");
		olmSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		olmSql.append("\n from todayWorks a inner join OUTLINKMAN t on a.comId=t.comId and a.busId=t.id");
		olmSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_OUTLINKMAN+"'");
		
		this.addSqlWhere(userInfo.getComId(), olmSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), olmSql, args, " and a.userId=? ");
		
		//完成结算通知
		StringBuffer banSql = new StringBuffer();
		banSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.flowName busTypeName,a.modifyer,");
		banSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		banSql.append("\n from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id");
		banSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_BALANCED+"'");
		
		this.addSqlWhere(userInfo.getComId(), banSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), banSql, args, " and a.userId=? ");
		
		//财务结算通知
		StringBuffer fbSql = new StringBuffer();
		fbSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.flowName busTypeName,a.modifyer,");
		fbSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		fbSql.append("\n from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id");
		fbSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"'");
		
		this.addSqlWhere(userInfo.getComId(), fbSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), fbSql, args, " and a.userId=? ");
		
		//属性变更通知
		StringBuffer ceSql = new StringBuffer();
		ceSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.busName busTypeName,a.modifyer,");
		ceSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		ceSql.append("\n from todayWorks a inner join moduleChangeApply t on a.comId=t.comId and a.busId=t.id");
		ceSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_CHANGE_EXAM+"'");
		
		this.addSqlWhere(userInfo.getComId(), ceSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), ceSql, args, " and a.userId=? ");
		
		//催办
		StringBuffer busRemindSql = new StringBuffer();
		busRemindSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.busModName busTypeName,a.modifyer,");
		busRemindSql.append("\n a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		busRemindSql.append("\n from todayWorks a inner join busremind t on a.comId=t.comId and a.busId=t.id");
		busRemindSql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_REMINDER+"'");
		
		this.addSqlWhere(userInfo.getComId(), busRemindSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), busRemindSql, args, " and a.userId=? ");
		
		//普通闹铃提示
		StringBuffer clockSql = new StringBuffer();
		
		clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'闹铃定时' busTypeName,");
		clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
		clockSql.append("\n from todayWorks a ");
		clockSql.append("\n where a.readState=0 and a.busType='0' and a.busId=0 ");
		
		this.addSqlWhere(userInfo.getComId(), clockSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), clockSql, args, " and a.userId=? ");
		
		
		sql.append("\n select a.* from (");
		sql.append("\n select a.*,lead(a.id,1,0) over(order by a.id desc) nextObjId,b.username modifyerName from (");
			sql.append(crmSql);
			sql.append("\n union all ");
			sql.append(schSql);
			sql.append("\n union all ");
			sql.append(itemSql);
			sql.append("\n union all ");
			sql.append(taskSql);
			sql.append("\n union all ");
			sql.append(spSql);
			sql.append("\n union all ");
			sql.append(spEndSql);
			sql.append("\n union all ");
			sql.append(qasSql);
			sql.append("\n union all ");
			sql.append(voteSql);
			sql.append("\n union all ");
			sql.append(weekSql);
			sql.append("\n union all ");
			sql.append(dailySql);
			sql.append("\n union all ");
			sql.append(fileSql);
			sql.append("\n union all ");
			sql.append(shareSql);
			sql.append("\n union all ");
			sql.append(invSql);
			sql.append("\n union all ");
			sql.append(meetSql);
			sql.append("\n union all ");
			sql.append(meetAppSql);
			sql.append("\n union all ");
			sql.append(noticeSql);
			sql.append("\n union all ");
			
			sql.append(bgypPurSql);
			sql.append("\n union all ");
			sql.append(bgypApplySql);
			sql.append("\n union all ");
			sql.append(instituSql);
			sql.append("\n union all ");
			sql.append(announSql);
			sql.append("\n union all ");
			sql.append(summarySql);
			sql.append("\n union all ");
			sql.append(dawSql);
			sql.append("\n union all ");
			sql.append(olmSql);
			sql.append("\n union all ");
			sql.append(banSql);
			sql.append("\n union all ");
			sql.append(fbSql);
			sql.append("\n union all ");
			sql.append(ceSql);
			sql.append("\n union all ");
			sql.append(busRemindSql);
			sql.append("\n union all ");
		sql.append(proSql);
		sql.append("\n union all ");
			sql.append(clockSql);
		sql.append("\n ) a ");
		sql.append("\n left join userOrganic bb on bb.userId=a.modifyer and a.comId=bb.comId");
		sql.append("\n left join userinfo b on bb.userId=b.id");
		sql.append("\n where a.readstate=0 order by a.weekord,a.recordCreateTime desc,a.busType");
		sql.append("\n )a where 1=1 and rownum=1");
		return (TodayWorks) this.objectQuery(sql.toString(), args.toArray(), TodayWorks.class);
	}
	
	/**
	 * 查询是否有未读聊天记录
	 * @param comId 企业号
	 * @param roomId 房间号
	 * @param userId 用户主键
	 * @return
	 */
	public TodayWorks getTodayWorkByRoomId(Integer comId, Integer roomId,
			Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from todayWorks a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(roomId, sql, args, " and a.roomId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		return (TodayWorks) this.objectQuery(sql.toString(), args.toArray(), TodayWorks.class);
	}
	

	/**
	 * 获取消息提醒分页列表
	 * @param todayWorks 
	 * @param comId 企业编号
	 * @param userId 用户主键
	 * @param modList 模块集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TodayWorks> listPagedMsgNoRead(TodayWorks todayWorks, Integer comId,
			Integer userId, List<String> modList) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_TASK)>=0){
			taskSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,");
			taskSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			taskSql.append("\n  from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id");
			
			if(null!=modList){
				taskSql.append(" and a.isClock=0 ");
			}
			taskSql.append("\n where a.busType='"+ConstantInterface.TYPE_TASK+"'");
			this.addSqlWhere(todayWorks.getReadState(), taskSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), taskSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), taskSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, taskSql, args, " and a.comId=?");
			this.addSqlWhere(userId, taskSql, args, " and a.userId=? ");
		}
		
		//投票
		StringBuffer voteSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_VOTE)>=0){
			voteSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,dbms_lob.substr(v.voteContent,4000) busTypeName,");
			voteSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			voteSql.append("\n from todayWorks a inner join vote v on a.comId=v.comId and a.busId=v.id");
			voteSql.append("\n where a.busType='"+ConstantInterface.TYPE_VOTE+"'");
			this.addSqlWhere(todayWorks.getReadState(), voteSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), voteSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), voteSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, voteSql, args, " and a.comId=?");
			this.addSqlWhere(userId, voteSql, args, " and a.userId=? ");
		}
		//项目
		StringBuffer itemSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_ITEM)>=0){
			itemSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.itemName busTypeName,");
			itemSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			itemSql.append("\n  from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
			
			if(null!=modList){
				itemSql.append(" and a.isClock=0 ");
			}
			
			itemSql.append("\n where a.busType='"+ConstantInterface.TYPE_ITEM+"'");
			this.addSqlWhere(todayWorks.getReadState(), itemSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), itemSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), itemSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, itemSql, args, " and a.comId=?");
			this.addSqlWhere(userId, itemSql, args, " and a.userId=? ");
		}
		//产品
		StringBuffer proSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_PRODUCT)>=0){
			proSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,pro.name busTypeName,");
			proSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			proSql.append("\n  from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id");

			if(null!=modList){
				proSql.append(" and a.isClock=0 ");
			}

			proSql.append("\n where a.busType='" + ConstantInterface.TYPE_PRODUCT + "'");
			this.addSqlWhere(todayWorks.getReadState(), proSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), proSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), proSql, args, " and substr(a.recordCreateTime,0,10)<=?");

			this.addSqlWhere(comId, proSql, args, " and a.comId=?");
			this.addSqlWhere(userId, proSql, args, " and a.userId=? ");
		}
		//周报
		StringBuffer weekSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_WEEK)>=0){
			weekSql.append("\n select aa.*,rownum as weekord from (");
			weekSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.weekRepName busTypeName,");
			weekSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId");
			weekSql.append("\n  from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id");
			weekSql.append("\n  left join userOrganic org on w.reporterId=org.userId and w.comId=org.comId");
			weekSql.append("\n where a.busType='"+ConstantInterface.TYPE_WEEK+"'");
			this.addSqlWhere(todayWorks.getReadState(),weekSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), weekSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), weekSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, weekSql, args, " and a.comId=?");
			this.addSqlWhere(userId, weekSql, args, " and a.userId=? ");
			weekSql.append("\n order by a.readState,a.busSpec desc, w.year desc,w.weekNum desc,org.depId,w.reporterId");
			weekSql.append("\n   )aa");
		}
		//分享
		StringBuffer dailySql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_DAILY)>=0){
			dailySql.append("\n select aa.*,rownum as weekord from (");
			dailySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.dailyName busTypeName,");
			dailySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId");
			dailySql.append("\n  from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id");
			dailySql.append("\n  left join userOrganic org on w.reporterId=org.userId and w.comId=org.comId");
			dailySql.append("\n where a.busType='"+ConstantInterface.TYPE_DAILY+"'");
			this.addSqlWhere(todayWorks.getReadState(),dailySql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), dailySql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), dailySql, args, " and substr(a.recordCreateTime,0,10)<=?");

			this.addSqlWhere(comId, dailySql, args, " and a.comId=?");
			this.addSqlWhere(userId, dailySql, args, " and a.userId=? ");
			dailySql.append("\n order by a.readState,a.busSpec desc, w.dailyDate desc,org.depId,w.reporterId");
			dailySql.append("\n   )aa");
		}
		//问答
		StringBuffer qasSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_QUES)>=0){
			qasSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,q.title busTypeName,");
			qasSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			qasSql.append("\n  from todayWorks a inner join question q on a.comId=q.comId and a.busId=q.id");
			qasSql.append("\n where a.busType='"+ConstantInterface.TYPE_QUES+"'");
			this.addSqlWhere(todayWorks.getReadState(), qasSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), qasSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), qasSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, qasSql, args, " and a.comId=?");
			this.addSqlWhere(userId, qasSql, args, " and a.userId=? ");
		}
		//客户
		StringBuffer crmSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_CRM)>=0){
			crmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.customername busTypeName,");
			crmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			crmSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id");
			
			if(null!=modList){
				crmSql.append(" and a.isClock=0 ");
			}
			crmSql.append("\n where  a.busType='"+ConstantInterface.TYPE_CRM+"'");
			this.addSqlWhere(todayWorks.getReadState(), crmSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), crmSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), crmSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, crmSql, args, " and a.comId=?");
			this.addSqlWhere(userId, crmSql, args, " and (a.userId=? or a.userId=0)");
		}
		
		//日程
		StringBuffer schSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_SCHEDULE)>=0){
			schSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.title busTypeName,");
			schSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			schSql.append("\n from todayWorks a inner join SCHEDULE c on a.comId=c.comId and a.busId=c.id");
			
			if(null!=modList){
				schSql.append(" and a.isClock=0 ");
			}
			schSql.append("\n where  a.busType='"+ConstantInterface.TYPE_SCHEDULE+"'");
			this.addSqlWhere(todayWorks.getReadState(), schSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), schSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), schSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, schSql, args, " and a.comId=?");
			this.addSqlWhere(userId, schSql, args, " and (a.userId=? or a.userId=0)");
		}
		
		//附件
		StringBuffer fileSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_FILE)>=0){
			fileSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,f.fileDescribe busTypeName,");
			fileSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			fileSql.append("\n from todayWorks a inner join fileDetail f on a.comId=f.comId and a.busId=f.id");
			fileSql.append("\n where a.busType='"+ConstantInterface.TYPE_FILE+"'");
			this.addSqlWhere(todayWorks.getReadState(), fileSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), fileSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), fileSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, fileSql, args, " and a.comId=?");
			this.addSqlWhere(userId, fileSql, args, " and a.userId=? ");
		}
		//申请
		StringBuffer invSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_APPLY)>=0){
			invSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,jt.userName||'申请加入企业' busTypeName,");
			invSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			invSql.append("\n  from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
			invSql.append("\n left join joinTemp jt on j.comId=jt.comId and j.account=jt.account");
			invSql.append("\n where a.busType='"+ConstantInterface.TYPE_APPLY+"'");
			this.addSqlWhere(todayWorks.getReadState(), invSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), invSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), invSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, invSql, args, " and a.comId=?");
			this.addSqlWhere(userId, invSql, args, " and a.userId=? ");
		}
		
		//会议
		StringBuffer meetSql = new StringBuffer();
		if(null==modList || (modList.indexOf(ConstantInterface.TYPE_MEETING)>=0
				|| modList.indexOf(ConstantInterface.TYPE_MEETING_SP)>=0)){
			meetSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
			meetSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,0 isClock,0 clockId,a.roomId,1 weekord");
			meetSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
			meetSql.append("\n where 1=1 ");
			this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP},
					meetSql, args, "\n and a.busType in ? ");
			this.addSqlWhere(todayWorks.getReadState(), meetSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), meetSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), meetSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, meetSql, args, " and a.comId=?");
			this.addSqlWhere(userId, meetSql, args, " and a.userId=? ");
		}
		//会议申请
		StringBuffer meetAppSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_MEETROOM)>=0){
			meetAppSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
			meetAppSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,0 isClock,0 clockId,a.roomId,1 weekord");
			meetAppSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
			meetAppSql.append("\n where a.busType='"+ConstantInterface.TYPE_MEETROOM+"'");
			this.addSqlWhere(todayWorks.getReadState(), meetAppSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), meetAppSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), meetAppSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, meetAppSql, args, " and a.comId=?");
			this.addSqlWhere(userId, meetAppSql, args, " and a.userId=? ");
		}
		//通知消息
		StringBuffer noticeSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_NOTICE)>=0){
			noticeSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'普通消息' busTypeName,");
			noticeSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,0 isClock,0 clockId,a.roomId,1 weekord");
			noticeSql.append("\n  from todayWorks a ");
			noticeSql.append("\n where a.busType='"+ConstantInterface.TYPE_NOTICE+"'");
			this.addSqlWhere(todayWorks.getReadState(), noticeSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), noticeSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), noticeSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, noticeSql, args, " and a.comId=?");
			this.addSqlWhere(userId, noticeSql, args, " and a.userId=? ");
		}
		//审批
		StringBuffer spSql = new StringBuffer();
		//审批(完结)
		StringBuffer spEndSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_FLOW_SP)>=0){
			spSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,");
			spSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			spSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id and sp.flowState=1");
			
			if(null!=modList){
				spSql.append(" and a.isClock=0 ");
			}
			spSql.append("\n where  a.busType='"+ConstantInterface.TYPE_FLOW_SP+"'");
			this.addSqlWhere(todayWorks.getReadState(), spSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), spSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), spSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, spSql, args, " and a.comId=?");
			this.addSqlWhere(userId, spSql, args, " and (a.userId=? or a.userId=0)");
			
			spEndSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,");
			spEndSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			spEndSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id ");
			
			if(null!=modList){
				spEndSql.append(" and a.isClock=0 ");
			}
			spEndSql.append("\n where  a.busType='"+ConstantInterface.TYPE_SP_END+"'");
			this.addSqlWhere(todayWorks.getReadState(), spEndSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), spEndSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), spEndSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, spEndSql, args, " and a.comId=?");
			this.addSqlWhere(userId, spEndSql, args, " and (a.userId=? or a.userId=0)");
		}
		
		//用品采购
		StringBuffer ypPurSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_BGYP_BUY_CHECK)>=0){
			
			ypPurSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'采购单:'||bpur.purOrderNum busTypeName,");
			ypPurSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			ypPurSql.append("\n  from todayWorks a inner join bgypPurOrder bpur on a.comId=bpur.comId and a.busId=bpur.id");
			
			ypPurSql.append("\n where (a.busType='"+ConstantInterface.TYPE_BGYP_BUY_CHECK+"' or a.busType='"+ConstantInterface.TYPE_BGYP_BUY_NOTICE+"')");
			this.addSqlWhere(todayWorks.getReadState(), ypPurSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), ypPurSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), ypPurSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, ypPurSql, args, " and a.comId=?");
			this.addSqlWhere(userId, ypPurSql, args, " and a.userId=? ");
		}
		//用品采购
		StringBuffer ypApplySql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_BGYP_APPLY_CHECK)>=0){
			
			ypApplySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'用品申领:'||bapply.applyDate busTypeName,");
			ypApplySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			ypApplySql.append("\n  from todayWorks a inner join bgypApply bapply on a.comId=bapply.comId and a.busId=bapply.id");
			
			ypApplySql.append("\n where (a.busType='"+ConstantInterface.TYPE_BGYP_APPLY_CHECK+"' or a.busType='"+ConstantInterface.TYPE_BGYP_APPLY_NOTICE+"')");
			this.addSqlWhere(todayWorks.getReadState(), ypApplySql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), ypApplySql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), ypApplySql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, ypApplySql, args, " and a.comId=?");
			this.addSqlWhere(userId, ypApplySql, args, " and a.userId=? ");
		}
		//公告
		StringBuffer announSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_ANNOUNCEMENT)>=0){
			
			announSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,v.title busTypeName,");
			announSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			announSql.append("\n from todayWorks a inner join announcement v on a.busId=v.id  and a.comId=v.comId");
			announSql.append("\n where v.delstate = 0 and  a.busType='"+ConstantInterface.TYPE_ANNOUNCEMENT+"'");
			this.addSqlWhere(todayWorks.getReadState(), announSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), announSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), announSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(userId, announSql, args, " and a.userId=? ");
		}
		
		//制度
		StringBuffer instituSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_INSTITUTION)>=0){
			
			instituSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,v.title busTypeName,");
			instituSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			instituSql.append("\n from todayWorks a inner join institution v on a.busId=v.id  and a.comId=v.comId");
			instituSql.append("\n where v.delstate = 0 and  a.busType='"+ConstantInterface.TYPE_INSTITUTION+"'");
			this.addSqlWhere(todayWorks.getReadState(), instituSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), instituSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), instituSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(userId, instituSql, args, " and a.userId=? ");
		}
		
		//会议纪要审批
		StringBuffer summarySql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_MEET_SUMMARY)>=0){
			summarySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
			summarySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,0 isClock,0 clockId,a.roomId,1 weekord");
			summarySql.append("\n  from todayWorks a inner join meetSummary summary on a.comId=summary.comId and a.busId=summary.id");
			summarySql.append("\n  inner join meeting meet on a.comId=meet.comId and summary.meetingId=meet.id");
			summarySql.append("\n where a.busType='"+ConstantInterface.TYPE_MEET_SUMMARY+"'");
			this.addSqlWhere(todayWorks.getReadState(), summarySql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), summarySql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), summarySql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, summarySql, args, " and a.comId=?");
			this.addSqlWhere(userId, summarySql, args, " and a.userId=? ");
		}
		
		//领款通知
		StringBuffer dawSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_NOTIFICATIONS)>=0){
			dawSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.flowName busTypeName,");
			dawSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			dawSql.append("\n  from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id");
			
			dawSql.append("\n where a.busType='"+ConstantInterface.TYPE_NOTIFICATIONS+"'");
			this.addSqlWhere(todayWorks.getReadState(), dawSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), dawSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), dawSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, dawSql, args, " and a.comId=?");
			this.addSqlWhere(userId, dawSql, args, " and a.userId=? ");
		}
		
		//外部联系人
		StringBuffer olmSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_OUTLINKMAN)>=0){
			olmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.linkmanName busTypeName,");
			olmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			olmSql.append("\n  from todayWorks a inner join OUTLINKMAN t on a.comId=t.comId and a.busId=t.id");
			
			olmSql.append("\n where a.busType='"+ConstantInterface.TYPE_OUTLINKMAN+"'");
			this.addSqlWhere(todayWorks.getReadState(), olmSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), olmSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), olmSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, olmSql, args, " and a.comId=?");
			this.addSqlWhere(userId, olmSql, args, " and a.userId=? ");
		}
		
		//完成结算通知
		StringBuffer banSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_BALANCED)>=0){
			banSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.flowName busTypeName,");
			banSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			banSql.append("\n  from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id");
			
			banSql.append("\n where a.busType='"+ConstantInterface.TYPE_BALANCED+"'");
			this.addSqlWhere(todayWorks.getReadState(), banSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), banSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), banSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, banSql, args, " and a.comId=?");
			this.addSqlWhere(userId, banSql, args, " and a.userId=? ");
		}
		
		//财务结算通知
		StringBuffer fbSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_FINALCIAL_BALANCE)>=0){
			fbSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.flowName busTypeName,");
			fbSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			fbSql.append("\n  from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id");
			
			fbSql.append("\n where a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"'");
			this.addSqlWhere(todayWorks.getReadState(), fbSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), fbSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), fbSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, fbSql, args, " and a.comId=?");
			this.addSqlWhere(userId, fbSql, args, " and a.userId=? ");
		}
		
		//属性变更通知
		StringBuffer ceSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_CHANGE_EXAM)>=0){
			ceSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.busName busTypeName,");
			ceSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			ceSql.append("\n  from todayWorks a inner join moduleChangeApply t on a.comId=t.comId and a.busId=t.id");
			
			ceSql.append("\n where a.busType='"+ConstantInterface.TYPE_CHANGE_EXAM+"'");
			this.addSqlWhere(todayWorks.getReadState(), ceSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), ceSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), ceSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, ceSql, args, " and a.comId=?");
			this.addSqlWhere(userId, ceSql, args, " and a.userId=? ");
		}
		
		//催办
		StringBuffer busRemindSql = new StringBuffer();
		if(null==modList || modList.indexOf("099")>=0){
			busRemindSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.busModName busTypeName,");
			busRemindSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			busRemindSql.append("\n  from todayWorks a inner join busremind t on a.comId=t.comId and a.busId=t.id");
			
			if(null!=modList){
				busRemindSql.append(" and a.isClock=0 ");
			}
			busRemindSql.append("\n where a.busType='"+ConstantInterface.TYPE_REMINDER+"'");
			this.addSqlWhere(todayWorks.getReadState(), busRemindSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), busRemindSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), busRemindSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, busRemindSql, args, " and a.comId=?");
			this.addSqlWhere(userId, busRemindSql, args, " and a.userId=? ");
		}
		//分享
		StringBuffer shareSql = new StringBuffer();
		if(null==modList || modList.indexOf("100")>=0){
			shareSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,TO_CHAR(m.content) busTypeName,");
			shareSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			shareSql.append("\n  from todayWorks a inner join msgShare m on a.comId=m.comId and a.busId=m.id");
			shareSql.append("\n where a.busType='1'");
			this.addSqlWhere(todayWorks.getReadState(), shareSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), shareSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), shareSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, shareSql, args, " and a.comId=?");
			this.addSqlWhere(userId, shareSql, args, " and a.userId=? ");
		}
		
		//普通闹铃提示
		StringBuffer clockSql = new StringBuffer();
		
		if(null==modList|| modList.indexOf("101")>=0){
			if(null!=modList && modList.size()>0){
				clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.customername busTypeName,");
				clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
				clockSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id");
				clockSql.append(" and a.isClock=1 ");
				clockSql.append("\n where   a.busType='"+ConstantInterface.TYPE_CRM+"'");
				this.addSqlWhere(todayWorks.getReadState(), clockSql, args, " and a.readState=?");
				//查询创建时间段
				this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
				this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
				
				this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
				this.addSqlWhere(userId, clockSql, args, " and (a.userId=? or a.userId=0)");
				clockSql.append("\n union all ");
				clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.itemName busTypeName,");
				clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
				clockSql.append("\n  from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
				clockSql.append(" and a.isClock=1 ");
				
				clockSql.append("\n where a.busType='"+ConstantInterface.TYPE_ITEM+"'");
				this.addSqlWhere(todayWorks.getReadState(), clockSql, args, " and a.readState=?");
				//查询创建时间段
				this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
				this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
				
				this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
				this.addSqlWhere(userId, clockSql, args, " and a.userId=? ");
				clockSql.append("\n union all ");
				clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,");
				clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
				clockSql.append("\n  from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id");
				clockSql.append(" and a.isClock=1 ");
				clockSql.append("\n where a.busType='"+ConstantInterface.TYPE_TASK+"'");
				this.addSqlWhere(todayWorks.getReadState(), clockSql, args, " and a.readState=?");
				//查询创建时间段
				this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
				this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
				
				this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
				this.addSqlWhere(userId, clockSql, args, " and a.userId=? ");
				clockSql.append("\n union all ");
			}
			clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,");
			clockSql.append("\n case when a.busType=0 and a.busSpec=1 then '闹铃待办' else '闹铃提醒' end busTypeName, ");
			clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,a.isClock,a.clockId,a.roomId,1 weekord");
			clockSql.append("\n  from todayWorks a ");
			clockSql.append("\n where a.busType='0' and (a.busId=0 or a.busId=a.clockId) ");
			this.addSqlWhere(todayWorks.getReadState(), clockSql, args, " and a.readState=?");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
			this.addSqlWhere(userId, clockSql, args, " and a.userId=? ");
		}
		
		sql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,a.modifyer,a.busTypeName,a.content,a.readState,a.busSpec,");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,\n");
		sql.append("\n b.username modifyerName,b.gender,d.uuid modifyerUuid,a.isClock,a.clockId,a.roomId,a.weekord from (");
		if(null==modList){//全部
			sql.append(taskSql);//003
			sql.append("\n union all ");
			sql.append(voteSql);//004
			sql.append("\n union all ");
			sql.append(itemSql);//005
			sql.append("\n union all ");
			sql.append(weekSql);//006
			sql.append("\n union all ");
			sql.append(dailySql);//050
			sql.append("\n union all ");
			sql.append(qasSql);//011
			sql.append("\n union all ");
			sql.append(crmSql);//012
			sql.append("\n union all ");
			sql.append(fileSql);//013
			sql.append("\n union all ");
			sql.append(invSql);//015
			sql.append("\n union all ");
			sql.append(schSql);//016
			sql.append("\n union all ");
			sql.append(meetSql);//017
			sql.append("\n union all ");
			sql.append(meetAppSql);//018
			sql.append("\n union all ");
			sql.append(noticeSql);//019
			sql.append("\n union all ");
			sql.append(spSql);//022
			sql.append("\n union all ");
			sql.append(spEndSql);//02201
			sql.append("\n union all ");
			sql.append(ypPurSql);//02701
			sql.append("\n union all ");
			sql.append(ypApplySql);//02702
			sql.append("\n union all ");
			sql.append(announSql);//39
			sql.append("\n union all ");
			sql.append(instituSql);//39
			sql.append("\n union all ");
			sql.append(summarySql);//47
			sql.append("\n union all ");
			sql.append(dawSql);//103
			sql.append("\n union all ");
			sql.append(olmSql);//068
			sql.append("\n union all ");
			sql.append(banSql);//06602
			sql.append("\n union all ");
			sql.append(fbSql);//06601
			sql.append("\n union all ");
			sql.append(ceSql);//067
			sql.append("\n union all ");
			sql.append(proSql);//080
			sql.append("\n union all ");
			sql.append(busRemindSql);//99
			sql.append("\n union all ");
			sql.append(shareSql);//100
			sql.append("\n union all ");
			sql.append(clockSql);//101
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
					case 6:
						sql.append(weekSql);
						break;
					case 11:
						sql.append(qasSql);
						break;
					case 12:
						sql.append(crmSql);
						break;
					case 13:
						sql.append(fileSql);
						break;
					case 15:
						sql.append(invSql);
						break;
					case 16:
						sql.append(schSql);
						break;
					case 17:
						sql.append(meetSql);
						break;
					case 18:
						sql.append(meetAppSql);
						break;
					case 19:
						sql.append(noticeSql);
						break;
					case 22:
						sql.append(spSql);
						sql.append("\n union all ");
						sql.append(spEndSql);//02201
						break;
					case 27010:
						sql.append(ypPurSql);
						break;
					case 27020:
						sql.append(ypApplySql);
						break;
					case 39:
						sql.append(announSql);//39
						break;
					case 40:
						sql.append(instituSql);//40
						break;
					case 47:
						sql.append(summarySql);//47
						break;
					case 50:
						sql.append(dailySql);//50
						break;
					case 99:
						sql.append(busRemindSql);
						break;
					case 100:
						sql.append(shareSql);
						break;
					case 101:
						sql.append(clockSql);
						break;
					case 103:
						sql.append(dawSql);
						break;
					case 68:
						sql.append(olmSql);
						break;
					case 6602:
						sql.append(banSql);
						break;
					case 6601:
						sql.append(fbSql);
						break;
					case 67:
						sql.append(ceSql);
						break;
					default:
						break;
					}
				if(i<modList.size()-1){
					sql.append("\n union all ");
				}
			}
		}
		sql.append("\n )a left join attention atten on a.comId=atten.comId and a.busType=atten.busType and a.busId=atten.busId and atten.userId="+userId);
		sql.append("\n left join userinfo b on a.modifyer=b.id");
		sql.append("\n left join userOrganic c on a.modifyer=c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on d.id=c.mediumHeadPortrait and d.comId=c.comId");
		sql.append("\n where 1=1 ");
		this.addSqlWhereLike(todayWorks.getContent(), sql, args, " and (a.busTypeName like ? or a.content like ?)", 2);
		
		String todoIds = todayWorks.getTodoIds();
		if(null!=todoIds && !"".equals(todoIds)){
			sql.append("\n and a.id not in ("+todoIds+")");
		}
		return this.pagedQuery(sql.toString(),"a.readState,a.weekord,a.recordCreateTime desc,a.busType", args.toArray(), TodayWorks.class);
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
		StringBuffer taskSql_1 = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_TASK)>=0){
			taskSql_1 = taskToDoSql(todayWorks, comId, userId, modList, args);
		}
		
		//周报
		StringBuffer weekSql_2 = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_WEEK)>=0){
			weekSql_2 = repToDoSql(todayWorks, comId, userId, args);
			
		}
		//分享
		StringBuffer dailySql_3 = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_DAILY)>=0){
			dailySql_3 = dailyToDoSql(todayWorks, comId, userId, args);
		}

		//邀请
		StringBuffer invSql_4 = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_APPLY)>=0){
			invSql_4 = invToDoSql(todayWorks, comId, userId, args);
		}
		//参会确认
		StringBuffer meetSql_5 = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_MEETING)>=0
				|| modList.indexOf(ConstantInterface.TYPE_MEETING_SP)>=0){
			meetSql_5 = meetToDoSql(todayWorks, comId, userId, args);
		}
		//审批待办
		StringBuffer spAppSql_6 = new StringBuffer();
		//审批完结
		StringBuffer spEndSql_7 = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_FLOW_SP)>=0 || modList.indexOf(ConstantInterface.TYPE_SP_END)>=0){
			spAppSql_6 = spToDoSql(todayWorks, comId, userId, args);
			spEndSql_7 = spEndSql(todayWorks, comId, userId, args);
		}
		
		//分享
		StringBuffer msgShareSql_8 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0 ){
			msgShareSql_8 = msgShareToDoSql(todayWorks, comId, userId, modList, args);
		}
		//日程
		StringBuffer schSql_9 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			schSql_9 = schToDoSql(todayWorks, comId, userId, modList, args);
		}
		//任务报延
		StringBuffer delayApplySql_10 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			delayApplySql_10 = delayApplySql(todayWorks, comId, userId, modList, args);
		}
		//项目
		StringBuffer itemSql_11 = new StringBuffer();
		if(null==modList ||  modList.indexOf("9999")>=0){
			itemSql_11 = itemToDoSql(todayWorks, comId, userId, modList, args);
		}
		
		//客户
		StringBuffer crmSql_12 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			crmSql_12 = crmToDoSql(todayWorks, comId, userId, modList, args);
		}
		//会议申请
		StringBuffer meetAppSql_13 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			meetAppSql_13 = meetAppToDoSql(todayWorks, comId, userId, args);
		}
		//用品采购
		StringBuffer bgypPurSql_14 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			bgypPurSql_14 = bgypPurToDoSql(todayWorks, comId, userId, args);
		}
		//用品申领
		StringBuffer bgypApplySql_15 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			bgypApplySql_15 = bgypApplyToDoSql(todayWorks, comId, userId, args);
		}
		//制度
		StringBuffer instituSql_16 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			instituSql_16 = instituSql(todayWorks, comId, userId, args);
		}
		
		//会议纪要审批
		StringBuffer summarySql_17 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			summaryToDoSql(todayWorks, comId, userId, args, summarySql_17);
		}
		
		//领款通知
		StringBuffer dawSql_18 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			dawToDoSql(comId, userId, args, dawSql_18);
		}
		
		//完成结算通知
		StringBuffer banSql_19 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			banToDoSql(comId, userId, args, banSql_19);
		}
		
		//财务结算通知
		StringBuffer fbSql_20 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			fbToDoSql(todayWorks,comId, userId, args, fbSql_20);
		}
		//属性变更通知
		StringBuffer ceSql_21 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			ceToDoSql(todayWorks,comId, userId, args, ceSql_21);
		}
		//催办
		StringBuffer busRemindSql_22 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			busRemindToDoSql(comId, userId, args, busRemindSql_22);
		}
		
		//闹铃
		StringBuffer clockSql_23 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			clockSql_23 = clockToDoSql(todayWorks, comId, userId, modList, args);
		}
		
		//外部联系人
		StringBuffer olmSql_24 = new StringBuffer();
		if(null==modList || modList.indexOf("9999")>=0){
			olmToDoSql(comId, userId, args, olmSql_24);
		}
		//产品
		StringBuffer proSql_25 = new StringBuffer();
		if(null==modList ||  modList.indexOf("9999")>=0){
			proSql_25 = proToDoSql(todayWorks, comId, userId, modList, args);
		}
		sql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,a.busTypeName,a.content,a.readState,a.busSpec,");
		sql.append("a.dealTimeLimit,a.modifyer, case when atten.id is null then 0 else 1 end as attentionState,grade,neworder,\n");
		sql.append("\n b.username modifyerName,b.gender,d.uuid modifyerUuid,d.filename,a.isClock,a.clockId,a.weekord from (");
		//sql的凭借顺序，必须和args里面的参数对应；所以拼接顺序由SQL字符串你后的数字升序排列；之后添加的，在数字后递增
		if(null==modList){//全部
			sql.append(taskSql_1);//003
			sql.append("\n union all ");
			sql.append(weekSql_2);//006
			sql.append("\n union all ");
			sql.append(dailySql_3);//050
			sql.append("\n union all ");
			sql.append(invSql_4);//015
			sql.append("\n union all ");
			sql.append(meetSql_5);//017
			sql.append("\n union all ");
			sql.append(spAppSql_6);//022
			sql.append("\n union all ");
			sql.append(spEndSql_7);//02201
			sql.append("\n union all ");
			sql.append(msgShareSql_8);//1
			sql.append("\n union all ");
			sql.append(schSql_9);//016
			sql.append("\n union all ");
			sql.append(delayApplySql_10);//任务报延
			sql.append("\n union all ");
			sql.append(itemSql_11);//005
			sql.append("\n union all ");
			sql.append(crmSql_12);//012
			sql.append("\n union all ");
			sql.append(meetAppSql_13);//018
			sql.append("\n union all ");
			sql.append(bgypPurSql_14);//027010
			sql.append("\n union all ");
			sql.append(bgypApplySql_15);//027020
			sql.append("\n union all ");
			sql.append(instituSql_16);//40
			sql.append("\n union all ");
			sql.append(summarySql_17);//47
			sql.append("\n union all ");
			sql.append(dawSql_18);//103
			sql.append("\n union all ");
			sql.append(banSql_19);//06602
			sql.append("\n union all ");
			sql.append(fbSql_20);//06601
			sql.append("\n union all ");
			sql.append(ceSql_21);//067
			sql.append("\n union all ");
			sql.append(busRemindSql_22);//99
			sql.append("\n union all ");
			sql.append(clockSql_23);//101
			sql.append("\n union all ");
			sql.append(olmSql_24);//068
			sql.append("\n union all ");
			sql.append(proSql_25);//067
		}else{
			for(Integer i=0;i<modList.size();i++){
				String modType = modList.get(i);
				switch (Integer.parseInt(modType)) {
					case 3:
						sql.append(taskSql_1);
						break;
					case 6:
						sql.append(weekSql_2);
						break;
					case 15:
						sql.append(invSql_4);
						break;
					case 17:
					case 46:
						sql.append(meetSql_5);
						break;
					case 22:
						sql.append(spAppSql_6);
						sql.append("\n union all ");
						sql.append(spEndSql_7);
						break;
					case 50:
						sql.append(dailySql_3);
						break;
					case 9999:
						sql.append(msgShareSql_8);
						sql.append("\n union all ");
						sql.append(schSql_9);
						sql.append("\n union all ");
						sql.append(delayApplySql_10);//任务报延
						sql.append("\n union all ");
						sql.append(itemSql_11);
						sql.append("\n union all ");
						sql.append(crmSql_12);
						sql.append("\n union all ");
						sql.append(meetAppSql_13);
						sql.append("\n union all ");
						sql.append(bgypPurSql_14);
						sql.append("\n union all ");
						sql.append(bgypApplySql_15);
						sql.append("\n union all ");
						sql.append(instituSql_16);
						sql.append("\n union all ");
						sql.append(summarySql_17);
						sql.append("\n union all ");
						sql.append(dawSql_18);
						sql.append("\n union all ");
						sql.append(banSql_19);
						sql.append("\n union all ");
						sql.append(fbSql_20);
						sql.append("\n union all ");
						sql.append(ceSql_21);
						sql.append("\n union all ");
						sql.append(busRemindSql_22);
						sql.append("\n union all ");
						sql.append(clockSql_23);
						sql.append("\n union all ");
						sql.append(olmSql_24);
						sql.append("\n union all ");
						sql.append(proSql_25);//067
						break;
					default:
						break;
					}
				if(i<modList.size()-1){
					sql.append("\n union all ");
				}
			}
		}
		sql.append("\n )a left join attention atten on a.comId=atten.comId and a.busType=atten.busType and a.busId=atten.busId and atten.userId="+userId);
		sql.append("\n left join userinfo b on a.modifyer=b.id");
		sql.append("\n left join userOrganic c on a.modifyer=c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on d.id=c.mediumHeadPortrait and d.comId=c.comId");
		sql.append("\n where 1=1 ");
		this.addSqlWhereLike(todayWorks.getContent(), sql, args, " and (a.busTypeName like ? or a.content like ?)", 2);
		
		//不查询页面已有数据
//		String todoIds = todayWorks.getTodoIds();
//		if(null!=todoIds && !"".equals(todoIds)){
//			sql.append("\n and a.id not in ("+todayWorks.getTodoIds()+") ");
//		}
		return this.pagedQuery(sql.toString(),"a.readState,a.neworder desc,a.weekord,a.recordCreateTime desc,a.busId,a.busType", args.toArray(), TodayWorks.class);
	}
	/**
	 * 领款通知
	 * @param comId
	 * @param userId
	 * @param args
	 * @param dawSql
	 */
	private void dawToDoSql(Integer comId, Integer userId, List<Object> args, StringBuffer dawSql) {
		dawSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.flowName busTypeName,");
		dawSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		
		dawSql.append("\n from todayWorks a ");
		dawSql.append("\n inner join spFlowInstance b on a.busId=b.id and  a.comId=b.comId" );
		dawSql.append("\n where a.busSpec=1 and a.readstate ='0' and  a.busType='"+ConstantInterface.TYPE_NOTIFICATIONS+"'");
		this.addSqlWhere(comId, dawSql, args, " and a.comId=?");
		this.addSqlWhere(userId, dawSql, args, " and a.userId=? ");
	}
	
	/**
	 * 外部联系人
	 * @param comId
	 * @param userId
	 * @param args
	 * @param olmSql
	 */
	private void olmToDoSql(Integer comId, Integer userId, List<Object> args, StringBuffer olmSql) {
		olmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.linkmanName busTypeName,");
		olmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		
		olmSql.append("\n from todayWorks a ");
		olmSql.append("\n inner join OUTLINKMAN b on a.busId=b.id and  a.comId=b.comId" );
		olmSql.append("\n where a.busSpec=1 and a.readstate ='0' and  a.busType='"+ConstantInterface.TYPE_OUTLINKMAN+"'");
		this.addSqlWhere(comId, olmSql, args, " and a.comId=?");
		this.addSqlWhere(userId, olmSql, args, " and a.userId=? ");
	}
	
	/**
	 * 完成结算通知
	 * @param comId
	 * @param userId
	 * @param args
	 * @param banSql
	 */
	private void banToDoSql(Integer comId, Integer userId, List<Object> args, StringBuffer banSql) {
		banSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.flowName busTypeName,");
		banSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		
		banSql.append("\n from todayWorks a ");
		banSql.append("\n inner join spFlowInstance b on a.busId=b.id and  a.comId=b.comId" );
		banSql.append("\n where a.busSpec=1 and a.readstate ='0' and  a.busType='"+ConstantInterface.TYPE_BALANCED+"'");
		this.addSqlWhere(comId, banSql, args, " and a.comId=?");
		this.addSqlWhere(userId, banSql, args, " and a.userId=? ");
	}
	
	/**
	 * 财务结算通知
	 * @param comId
	 * @param userId
	 * @param args
	 * @param fbSql
	 */
	private void fbToDoSql(TodayWorks todayWorks, Integer comId, Integer userId, List<Object> args, StringBuffer fbSql) {
		fbSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.flowName busTypeName,");
		fbSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		
		fbSql.append("\n from todayWorks a ");
		fbSql.append("\n inner join spFlowInstance b on a.busId=b.id and  a.comId=b.comId" );
		fbSql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"'");
		this.addSqlWhere(todayWorks.getReadState(), fbSql, args, " and a.readState=?");//阅读状态筛选
		this.addSqlWhere(comId, fbSql, args, " and a.comId=?");
		this.addSqlWhere(userId, fbSql, args, " and a.userId=? ");
	}
	
	/**
	 * 属性变更通知
	 * @param comId
	 * @param userId
	 * @param args
	 * @param ceSql
	 */
	private void ceToDoSql(TodayWorks todayWorks, Integer comId, Integer userId, List<Object> args, StringBuffer ceSql) {
		ceSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.busName busTypeName,");
		ceSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		
		ceSql.append("\n from todayWorks a ");
		ceSql.append("\n inner join moduleChangeApply b on a.busId=b.id and  a.comId=b.comId" );
		ceSql.append("\n where a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_CHANGE_EXAM+"'");
		this.addSqlWhere(todayWorks.getReadState(), ceSql, args, " and a.readState=?");//阅读状态筛选
		this.addSqlWhere(comId, ceSql, args, " and a.comId=?");
		this.addSqlWhere(userId, ceSql, args, " and a.userId=? ");
	}
	/**
	 * 催办待办SQL
	 * @param comId
	 * @param userId
	 * @param args
	 * @param busRemindSql
	 */
	private void busRemindToDoSql(Integer comId, Integer userId, List<Object> args, StringBuffer busRemindSql) {
		busRemindSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.busModName busTypeName,");
		busRemindSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		
		busRemindSql.append("\n from todayWorks a ");
		busRemindSql.append("\n inner join busremind b on a.busId=b.id and  a.comId=b.comId" );
		busRemindSql.append("\n where a.busSpec=1 and a.readstate ='0' and  a.busType='"+ConstantInterface.TYPE_REMINDER+"'");
		this.addSqlWhere(comId, busRemindSql, args, " and a.comId=?");
		this.addSqlWhere(userId, busRemindSql, args, " and a.userId=? ");
	}
	/**
	 * 会议纪要待办SQL
	 * @param todayWorks
	 * @param comId
	 * @param userId
	 * @param args
	 * @param summarySql
	 */
	private void summaryToDoSql(TodayWorks todayWorks, Integer comId, Integer userId, List<Object> args,
			StringBuffer summarySql) {
		summarySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
		summarySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		summarySql.append("\n from todayWorks a inner join meetSummary summary on a.comId=summary.comId and a.busId=summary.id");
		summarySql.append("\n inner join meeting meet on a.comId=meet.comId and summary.meetingId=meet.id");
		summarySql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_MEET_SUMMARY+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), summarySql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), summarySql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, summarySql, args, " and a.comId=?");
		this.addSqlWhere(userId, summarySql, args, " and a.userId=? ");
	}
	
	/**
	 * 会议申请待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param args 参数
	 * @return
	 */
	private StringBuffer meetAppToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<Object> args) {
		StringBuffer meetAppSql = new StringBuffer();
		meetAppSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
		meetAppSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		meetAppSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		meetAppSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_MEETROOM+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), meetAppSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), meetAppSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		this.addSqlWhere(todayWorks.getReadState(), meetAppSql, args, " and a.readState=?");//阅读状态筛选
		
		this.addSqlWhere(comId, meetAppSql, args, " and a.comId=?");
		this.addSqlWhere(userId, meetAppSql, args, " and a.userId=? ");
		return meetAppSql;
	}
	/**
	 * 用品采购待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param args 参数
	 * @return
	 */
	private StringBuffer bgypPurToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<Object> args) {
		StringBuffer bgypPurSql = new StringBuffer();
		bgypPurSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'采购单:'||bpur.purOrderNum busTypeName,");
		bgypPurSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		bgypPurSql.append("\n  from todayWorks a inner join bgypPurOrder bPur on a.comId=bPur.comId and a.busId=bPur.id");
		bgypPurSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_BGYP_BUY_CHECK+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), bgypPurSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), bgypPurSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, bgypPurSql, args, " and a.comId=?");
		this.addSqlWhere(userId, bgypPurSql, args, " and a.userId=? ");
		return bgypPurSql;
	}
	/**
	 * 用品申领待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param args 参数
	 * @return
	 */
	private StringBuffer bgypApplyToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<Object> args) {
		StringBuffer bgypApplySql = new StringBuffer();
		bgypApplySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'用品申领:'||bapply.applyDate busTypeName,");
		bgypApplySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		bgypApplySql.append("\n  from todayWorks a inner join bgypApply bApply on a.comId=bApply.comId and a.busId=bApply.id");
		bgypApplySql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_BGYP_APPLY_CHECK+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), bgypApplySql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), bgypApplySql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, bgypApplySql, args, " and a.comId=?");
		this.addSqlWhere(userId, bgypApplySql, args, " and a.userId=? ");
		return bgypApplySql;
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
		meetSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
		meetSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		meetSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		meetSql.append("\n where a.busSpec=1 and to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') < meet.startdate");
		this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP},
				meetSql, args, "\n and a.busType in ? ");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), meetSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), meetSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		this.addSqlWhere(todayWorks.getReadState(), meetSql, args, " and a.readState=?");//阅读状态筛选
		
		this.addSqlWhere(comId, meetSql, args, " and a.comId=?");
		this.addSqlWhere(userId, meetSql, args, " and a.userId=? ");
		return meetSql;
	}
	
	/**
	 * 申请待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param args 参数
	 * @return
	 */
	private StringBuffer invToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<Object> args) {
		StringBuffer invSql = new StringBuffer();
		invSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,jt.userName||'申请加入企业' busTypeName,");
		invSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		invSql.append("\n  from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
		invSql.append("\n left join joinTemp jt on j.comId=jt.comId and j.account=jt.account");
		invSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_APPLY+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), invSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), invSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		this.addSqlWhere(todayWorks.getReadState(), invSql, args, " and a.readState=?");//阅读状态筛选
		
		this.addSqlWhere(comId, invSql, args, " and a.comId=?");
		this.addSqlWhere(userId, invSql, args, " and a.userId=? ");
		return invSql;
	}
	
	/**
	 * 客户待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param modList 待办类型集合
	 * @param args 参数
	 * @return
	 */
	private StringBuffer crmToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<String> modList, List<Object> args) {
		StringBuffer crmSql = new StringBuffer();
		crmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.customername busTypeName,");
		crmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		crmSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id");
		if(null!=modList ){//不处理
			crmSql.append(" and a.isClock=0");
		}
		crmSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_CRM+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), crmSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), crmSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, crmSql, args, " and a.comId=?");
		this.addSqlWhere(userId, crmSql, args, " and (a.userId=? or a.userId=0)");
		return crmSql;
	}
	
	
	 /**
	  * 日程待办事项SQL
	  * @param todayWorks
	  * @param comId
	  * @param userId
	  * @param modList
	  * @param args
	  * @return
	  */
	private StringBuffer schToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<String> modList, List<Object> args) {
		StringBuffer crmSql = new StringBuffer();
		crmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.TITLE busTypeName,");
		crmSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		crmSql.append("\n from todayWorks a inner join SCHEDULE c on a.comId=c.comId and a.busId=c.id");
		if(null!=modList ){//不处理
			crmSql.append(" and a.isClock=0");
		}
		crmSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_CRM+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), crmSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), crmSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, crmSql, args, " and a.comId=?");
		this.addSqlWhere(userId, crmSql, args, " and (a.userId=? or a.userId=0)");
		return crmSql;
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
		weekSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.weekRepName busTypeName,");
		weekSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId");
		weekSql.append("\n  from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id");
		weekSql.append("\n   left join userOrganic org on w.reporterId=org.userId and w.comId=org.comId");
		weekSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_WEEK+"'");
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
	 * 分享待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param args 参数
	 * @return
	 */
	private StringBuffer dailyToDoSql(TodayWorks todayWorks, Integer comId,
									Integer userId, List<Object> args) {
		StringBuffer dailySql = new StringBuffer();
		dailySql.append("\n select aa.* ,rownum as weekord from (");
		dailySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.dailyName busTypeName,");
		dailySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId");
		dailySql.append("\n  from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id");
		dailySql.append("\n   left join userOrganic org on w.reporterId=org.userId and w.comId=org.comId");
		dailySql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_DAILY+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), dailySql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), dailySql, args, " and substr(a.recordCreateTime,0,10)<=?");

		this.addSqlWhere(comId, dailySql, args, " and a.comId=?");
		this.addSqlWhere(userId, dailySql, args, " and a.userId=? ");
		dailySql.append("\n  order by a.readState,a.busSpec desc,w.dailyDate desc,org.depId,w.reporterId");
		dailySql.append("\n  )aa");
		return dailySql;
	}
	
	/**
	 * 项目待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param modList 待办类型集合
	 * @param args 参数
	 */
	private StringBuffer itemToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<String> modList, List<Object> args) {
		StringBuffer itemSql = new StringBuffer();
		itemSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.itemName busTypeName,");
		itemSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		itemSql.append("\n  from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
		
		if(null!=modList){
			itemSql.append(" and a.isClock=0");
		}
		itemSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_ITEM+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), itemSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), itemSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, itemSql, args, " and a.comId=?");
		this.addSqlWhere(userId, itemSql, args, " and a.userId=? ");
		return itemSql;
	}

	/**
	 * 产品待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param modList 待办类型集合
	 * @param args 参数
	 */
	private StringBuffer proToDoSql(TodayWorks todayWorks, Integer comId,
									 Integer userId, List<String> modList, List<Object> args) {
		StringBuffer itemSql = new StringBuffer();
		itemSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,pro.name busTypeName,");
		itemSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		itemSql.append("\n  from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id");

		if(null!=modList){
			itemSql.append(" and a.isClock=0");
		}
		itemSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_PRODUCT+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), itemSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), itemSql, args, " and substr(a.recordCreateTime,0,10)<=?");

		this.addSqlWhere(comId, itemSql, args, " and a.comId=?");
		this.addSqlWhere(userId, itemSql, args, " and a.userId=? ");
		return itemSql;
	}
	
	/**
	 * 任务待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param modList 待办类型集合
	 * @param args 参数
	 */
	private StringBuffer msgShareToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<String> modList, List<Object> args) {
		StringBuffer msgShareSql = new StringBuffer();
		msgShareSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,dbms_lob.substr(c.Content,4000) busTypeName,");
		msgShareSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		msgShareSql.append("\n from todayWorks a inner join msgshare c on a.comId=c.comId and a.busId=c.id");
		if(null!=modList ){//不处理
			msgShareSql.append(" and a.isClock=0");
		}
		msgShareSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='1'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), msgShareSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), msgShareSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, msgShareSql, args, " and a.comId=?");
		this.addSqlWhere(userId, msgShareSql, args, " and (a.userId=? or a.userId=0)");
		return msgShareSql;
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
		taskSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,");
		taskSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,hand.handTimeLimit dealTimeLimit,t.grade,t.grade neworder,a.isClock,a.clockId,1 weekord");
		taskSql.append("\n  from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id ");
		
		if(null!=modList){
			taskSql.append(" and a.isClock=0");
		}
		taskSql.append("\n left join taskhandover hand on t.comId=hand.comId and t.id=hand.taskid  ");
		taskSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_TASK+"'");
		taskSql.append("\n and hand.id=(select max(h.id) from taskhandover h where h.comId=t.comId and t.id=h.taskid)");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), taskSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), taskSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		this.addSqlWhere(todayWorks.getReadState(), taskSql, args, " and a.readState=?");//阅读状态筛选
		
		this.addSqlWhere(comId, taskSql, args, " and a.comId=?");
		this.addSqlWhere(userId, taskSql, args, " and a.userId=? ");
		return taskSql;
	}
	/**
	 * 任务待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param modList 待办类型集合
	 * @param args 参数
	 */
	private StringBuffer delayApplySql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<String> modList, List<Object> args) {
		StringBuffer delayApplySql = new StringBuffer();
		delayApplySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,");
		delayApplySql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		delayApplySql.append("\n  from todayWorks a inner join delayApply b on a.comId = b.comId and a.busId = b.id inner join task t on b.comId=t.comId and b.taskid=t.id ");
		
		if(null!=modList){
			delayApplySql.append(" and a.isClock=0");
		}
		delayApplySql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_DELAYAPPLY+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), delayApplySql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), delayApplySql, args, " and substr(a.recordCreateTime,0,10)<=?");
		this.addSqlWhere(todayWorks.getReadState(), delayApplySql, args, " and a.readState=?");//阅读状态筛选
		
		this.addSqlWhere(comId, delayApplySql, args, " and a.comId=?");
		this.addSqlWhere(userId, delayApplySql, args, " and a.userId=? ");
		return delayApplySql;
	}

	/**
	 * 审批完结待办SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param args 参数
	 * @return
	 */
	private StringBuffer instituSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<Object> args) {
		StringBuffer institutionSql = new StringBuffer();
		institutionSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,b.title busTypeName,");
		institutionSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		institutionSql.append("\n from todayWorks a inner join institution b on a.comId=b.comId and a.busId=b.id ");
		institutionSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType = '"+ConstantInterface.TYPE_INSTITUTION+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), institutionSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), institutionSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, institutionSql, args, " and a.comId=?");
		this.addSqlWhere(userId, institutionSql, args, " and a.userId=? ");
		return institutionSql;
	}
	/**
	 * 审批待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param args 参数
	 * @return
	 */
	private StringBuffer spToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<Object> args) {
		StringBuffer spAppSql = new StringBuffer();
		spAppSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,");
		spAppSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		spAppSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id and sp.flowState=1");
		spAppSql.append("\n where a.busSpec=1 and a.busType = '"+ConstantInterface.TYPE_FLOW_SP+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), spAppSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), spAppSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		this.addSqlWhere(todayWorks.getReadState(), spAppSql, args, " and a.readState=?");//阅读状态筛选
		
		this.addSqlWhere(comId, spAppSql, args, " and a.comId=?");
		this.addSqlWhere(userId, spAppSql, args, " and a.userId=? ");
		return spAppSql;
	}
	/**
	 * 审批完结待办SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param args 参数
	 * @return
	 */
	private StringBuffer spEndSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<Object> args) {
		StringBuffer spEndSql = new StringBuffer();
		spEndSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,sp.flowName busTypeName,");
		spEndSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		spEndSql.append("\n from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id ");
		spEndSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType = '"+ConstantInterface.TYPE_SP_END+"'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), spEndSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), spEndSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, spEndSql, args, " and a.comId=?");
		this.addSqlWhere(userId, spEndSql, args, " and a.userId=? ");
		return spEndSql;
	}
	/**
	 * 闹钟待办事项SQL
	 * @param todayWorks 待办事项参数对象
	 * @param comId 团队主键
	 * @param userId 操作人
	 * @param modList 待办类型集合
	 * @param args 参数
	 * @return
	 */
	private StringBuffer clockToDoSql(TodayWorks todayWorks, Integer comId,
			Integer userId, List<String> modList, List<Object> args) {
		StringBuffer clockSql = new StringBuffer();
		if(null!=modList && modList.indexOf("101")>=0){
			//客户闹铃
			clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.customername busTypeName,");
			clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
			clockSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id");
			clockSql.append(" and a.isClock=1");
			clockSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_CRM+"'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
			this.addSqlWhere(userId, clockSql, args, " and (a.userId=? or a.userId=0)");
			
			clockSql.append("\n  union all ");
			//项目闹铃
			clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.itemName busTypeName,");
			clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
			clockSql.append("\n  from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
			clockSql.append(" and a.isClock=1");
			clockSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_ITEM+"'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
			this.addSqlWhere(userId, clockSql, args, " and a.userId=? ");
			
			clockSql.append("\n  union all ");
			//任务闹铃
			clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,");
			clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,hand.handTimeLimit dealTimeLimit,t.grade,t.grade neworder,a.isClock,a.clockId,1 weekord");
			clockSql.append("\n  from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id ");
			
			clockSql.append(" and a.isClock=1");
			
			clockSql.append("\n left join taskhandover hand on t.comId=hand.comId and t.id=hand.taskid  ");
			clockSql.append("\n where a.busspec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_TASK+"'");
			clockSql.append("\n and hand.id=(select max(h.id) from taskhandover h where h.comId=t.comId and t.id=h.taskid)");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
			this.addSqlWhere(userId, clockSql, args, " and a.userId=? ");
			clockSql.append("\n  union all ");
		}
		
		//普通闹铃
		clockSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,a.content busTypeName,");
		clockSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit,'0' grade,'1' neworder,a.isClock,a.clockId,1 weekord");
		clockSql.append("\n  from todayWorks a ");
		clockSql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='0'");
		//查询创建时间段
		this.addSqlWhere(todayWorks.getStartDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)>=?");
		this.addSqlWhere(todayWorks.getEndDate(), clockSql, args, " and substr(a.recordCreateTime,0,10)<=?");
		
		this.addSqlWhere(comId, clockSql, args, " and a.comId=?");
		this.addSqlWhere(userId, clockSql, args, " and a.userId=? ");
		
		return clockSql;
	}
	/**
	 * 获取所有待办事项(手机端使用，若是多选需要修改)
	 * @param todayWorks
	 * @param comId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TodayWorks> listWorksTodo(TodayWorks todayWorks, Integer comId,
			Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//业务类型
		String type = todayWorks.getBusType();
		//客户
		StringBuffer crmSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_CRM.equals(type)){
			crmSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,c.customername busTypeName,a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit");
			crmSql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id");
			crmSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_CRM+"'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), crmSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), crmSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, crmSql, args, " and a.comId=?");
			this.addSqlWhere(userId, crmSql, args, " and (a.userId=? or a.userId=0)");
		}
		//项目
		StringBuffer itemSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_ITEM.equals(type)){
			itemSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,i.itemName busTypeName,a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit");
			itemSql.append("\n  from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id");
			itemSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_ITEM+"'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), itemSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), itemSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, itemSql, args, " and a.comId=?");
			this.addSqlWhere(userId, itemSql, args, " and a.userId=? ");
		}
		//产品
		StringBuffer proSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_PRODUCT.equals(type)){
			proSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,pro.name busTypeName,a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit");
			proSql.append("\n  from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id");
			proSql.append("\n where a.busSpec=1 and a.busType='" + ConstantInterface.TYPE_PRODUCT + "'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), proSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), proSql, args, " and substr(a.recordCreateTime,0,10)<=?");

			this.addSqlWhere(comId, proSql, args, " and a.comId=?");
			this.addSqlWhere(userId, proSql, args, " and a.userId=? ");
		}
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_TASK.equals(type)){
			taskSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,t.taskName busTypeName,a.modifyer,a.content,a.readState,a.busSpec,t.dealTimeLimit");
			taskSql.append("\n  from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id");
			taskSql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_TASK+"'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), taskSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), taskSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, taskSql, args, " and a.comId=?");
			this.addSqlWhere(userId, taskSql, args, " and a.userId=? ");
		}
		//周报
		StringBuffer weekSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_WEEK.equals(type)){
			weekSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.weekRepName busTypeName,a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit");
			weekSql.append("\n  from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id");
			weekSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_WEEK+"'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), weekSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), weekSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, weekSql, args, " and a.comId=?");
			this.addSqlWhere(userId, weekSql, args, " and a.userId=? ");
		}
		//分享
		StringBuffer dailySql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_DAILY.equals(type)){
			dailySql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,w.dailyName busTypeName,a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit");
			dailySql.append("\n  from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id");
			dailySql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_DAILY+"'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), dailySql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), dailySql, args, " and substr(a.recordCreateTime,0,10)<=?");

			this.addSqlWhere(comId, dailySql, args, " and a.comId=?");
			this.addSqlWhere(userId, dailySql, args, " and a.userId=? ");
		}
		//申请
		StringBuffer invSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) ||ConstantInterface.TYPE_APPLY.equals(type)){
			invSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,'申请加入企业' busTypeName,a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit");
			invSql.append("\n  from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
			invSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_APPLY+"'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), invSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), invSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, invSql, args, " and a.comId=?");
			this.addSqlWhere(userId, invSql, args, " and a.userId=? ");
		}
		//参会确认
		StringBuffer meetSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) ||ConstantInterface.TYPE_MEETING.equals(type)){
			meetSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
			meetSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit");
			meetSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
			meetSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_MEETING+"'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), meetSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), meetSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, meetSql, args, " and a.comId=?");
			this.addSqlWhere(userId, meetSql, args, " and a.userId=? ");
		}
		//会议申请
		StringBuffer meetAppSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) ||ConstantInterface.TYPE_MEETROOM.equals(type)){
			meetAppSql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,meet.title busTypeName,");
			meetAppSql.append("\n a.modifyer,a.content,a.readState,a.busSpec,null dealTimeLimit");
			meetAppSql.append("\n  from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
			meetAppSql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_MEETROOM+"'");
			//查询创建时间段
			this.addSqlWhere(todayWorks.getStartDate(), meetAppSql, args, " and substr(a.recordCreateTime,0,10)>=?");
			this.addSqlWhere(todayWorks.getEndDate(), meetAppSql, args, " and substr(a.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(comId, meetAppSql, args, " and a.comId=?");
			this.addSqlWhere(userId, meetAppSql, args, " and a.userId=? ");
		}
		sql.append("\n select a.id,a.recordCreateTime,a.busType,a.busId,a.comId,a.userId,a.busTypeName,a.content,a.readState,a.busSpec,a.dealTimeLimit,a.modifyer,");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,\n");
		sql.append("\n b.username modifyerName,b.gender,d.uuid modifyerUuid,d.filename from (");
		if(StringUtil.isBlank(StringUtil.delNull(type))){//全部
			sql.append(crmSql);
			sql.append("\n union all ");
			sql.append(itemSql);
			sql.append("\n union all ");
			sql.append(taskSql);
			sql.append("\n union all ");
			sql.append(weekSql);
			sql.append("\n union all ");
			sql.append(dailySql);
			sql.append("\n union all ");
			sql.append(invSql);
			sql.append("\n union all ");
			sql.append(meetAppSql);
		}else if(ConstantInterface.TYPE_CRM.equals(type)){//只客户
			sql.append(crmSql);
		}else if(ConstantInterface.TYPE_ITEM.equals(type)){//只项目
			sql.append(itemSql);
		}else if(ConstantInterface.TYPE_PRODUCT.equals(type)){//只产品
			sql.append(proSql);
		}else if(ConstantInterface.TYPE_TASK.equals(type)){//只任务
			sql.append(taskSql);
		}else if(ConstantInterface.TYPE_WEEK.equals(type)){//只周报
			sql.append(weekSql);
		}else if(ConstantInterface.TYPE_DAILY.equals(type)){//只分享
			sql.append(dailySql);
		}else if(ConstantInterface.TYPE_APPLY.equals(type)){//只申请
			sql.append(invSql);
		}else if(ConstantInterface.TYPE_MEETROOM.equals(type)){//只申请
			sql.append(meetAppSql);
		}
		sql.append("\n )a left join attention atten on a.comId=atten.comId and a.busType=atten.busType and a.busId=atten.busId and atten.userId="+userId);
		sql.append("\n left join userinfo b on a.modifyer=b.id");
		sql.append("\n left join userOrganic c on a.modifyer=c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on d.id=c.mediumHeadPortrait and d.comId=c.comId");
		sql.append("\n where 1=1 ");
		this.addSqlWhereLike(todayWorks.getContent(), sql, args, " and (a.busTypeName like ? or a.content like ?)", 2);
		sql.append("\n order by a.readState,a.recordCreateTime desc,a.busType");
		return this.listQuery(sql.toString(), args.toArray(), TodayWorks.class);
	}
	
	
	
	/**
	 * 获取待办提醒数目(按业务模块分类)
	 * @param comId 企业号
	 * @param userId 当前用户主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgNoRead> countNoReadByType(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select busType,count(*) noReadNum from (");
		//客户模块
		sql.append("\n select case when a.isClock=1 then '101' else a.busType end busType ");
		sql.append("\n from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_CRM+"' and c.delstate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//日程模块
		sql.append("\n	 union all ");
		sql.append("\n select case when a.isClock=1 then '101' else a.busType end busType ");
		sql.append("\n from todayWorks a inner join SCHEDULE c on a.comId=c.comId and a.busId=c.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_SCHEDULE+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//项目模块
		sql.append("\n	 union all ");
		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_ITEM+"' and i.delstate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//产品模块
		sql.append("\n	 union all ");
		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id ");
		sql.append("\n where a.readState=0 and a.busType='" + ConstantInterface.TYPE_PRODUCT + "' and i.delstate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//任务模块
		sql.append("\n	 union all");
		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_TASK+"' and t.delstate=0 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//审批模块
		sql.append("\n	 union all");
		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"' and sp.flowState=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//审批模块（完结）
		sql.append("\n	 union all");
		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id ");
		sql.append("\n where  a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_SP_END+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//问答模块
		sql.append("\n	  union all");
		sql.append("\n	 select a.busType from todayWorks a inner join question q on a.comId=q.comId and a.busId=q.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_QUES+"' and q.delstate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//投票模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType from todayWorks a inner join vote v on a.comId=v.comId and a.busId=v.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_VOTE+"' and v.delstate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//周报模块
		sql.append("\n	  union all");
		sql.append("\n	 select a.busType from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_WEEK+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//分享模块
		sql.append("\n	  union all");
		sql.append("\n	 select a.busType from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_DAILY+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//附件模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType from todayWorks a inner join fileDetail f on a.comId=f.comId and a.busId=f.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_FILE+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//分享模块
		sql.append("\n	 union all");
		sql.append("\n	 select '100' from todayWorks a inner join msgShare m on a.comId=m.comId and a.busId=m.id");
		sql.append("\n where a.readState=0 and a.busType='1'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//加入记录模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_APPLY+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//会议模块
		sql.append("\n	 union all");
		sql.append("\n	 select case when a.busType='046' then '017' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		sql.append("\n   where a.readState=0 ");
		this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP},
				sql, args, "\n and a.busType in ? ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//会议申请模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_MEETROOM+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//用品采购
		sql.append("\n	 union all");
		sql.append("\n	 select '"+ConstantInterface.TYPE_BGYP_BUY_CHECK+"' busType  from todayWorks a inner join bgypPurOrder bPur on a.comId=bPur.comId and a.busId=bPur.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_BGYP_BUY_CHECK+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//用品采购
		sql.append("\n	 union all");
		sql.append("\n	 select '"+ConstantInterface.TYPE_BGYP_BUY_CHECK+"' busType from todayWorks a inner join bgypPurOrder bPur on a.comId=bPur.comId and a.busId=bPur.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_BGYP_BUY_NOTICE+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//用品申领
		sql.append("\n	 union all");
		sql.append("\n	 select '"+ConstantInterface.TYPE_BGYP_APPLY_CHECK+"' busType  from todayWorks a inner join bgypApply bApply on a.comId=bApply.comId and a.busId=bApply.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_BGYP_APPLY_CHECK+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//用品申领
		sql.append("\n	 union all");
		sql.append("\n	 select '"+ConstantInterface.TYPE_BGYP_APPLY_CHECK+"' busType from todayWorks a inner join bgypApply bApply on a.comId=bApply.comId and a.busId=bApply.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_BGYP_APPLY_NOTICE+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//闹铃模块
		sql.append("\n	 union all");
		sql.append("\n	 select '101' from todayWorks a ");
		sql.append("\n where a.readState=0 and a.busType='0' and a.busId=0 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//普通模块
		sql.append("\n	 union all");
		sql.append("\n	 select '"+ConstantInterface.TYPE_NOTICE+"' from todayWorks a ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_NOTICE+"' and a.busId=0 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//公告模块
		sql.append("\n	 union all");
		sql.append("\n select '"+ConstantInterface.TYPE_ANNOUNCEMENT+"' from todayWorks a ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_ANNOUNCEMENT+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		
		//制度模块
		sql.append("\n	 union all");
		sql.append("\n select '"+ConstantInterface.TYPE_INSTITUTION+"' from todayWorks a ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_INSTITUTION+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//会议纪要审批
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType from todayWorks a ");
		sql.append("\n	 inner join meetSummary summary on a.comId=summary.comId and a.busId=summary.id");
		sql.append("\n	 inner join meeting meet on a.comId=meet.comId and summary.meetingId=meet.id");
		sql.append("\n   where a.readState=0 and a.busType='"+ConstantInterface.TYPE_MEET_SUMMARY+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//领款通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType ");
		sql.append("\n	 from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_NOTIFICATIONS+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//外部联系人模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType ");
		sql.append("\n	 from todayWorks a inner join OUTLINKMAN t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_OUTLINKMAN+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//完成结算通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType ");
		sql.append("\n	 from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_BALANCED+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//财务结算通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType ");
		sql.append("\n	 from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//属性变更通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType ");
		sql.append("\n	 from todayWorks a inner join moduleChangeApply t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_CHANGE_EXAM+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//催办模块
		sql.append("\n	 union all");
		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join busremind t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_REMINDER+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
				
		sql.append("\n	 )a group by busType");
		return this.listQuery(sql.toString(), args.toArray(), MsgNoRead.class);
	}
	
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
		List<Object> notInArgs = new ArrayList<Object>();
		sql.append("\n select busType,count(*) noReadNum from (");

		//任务模块
		sql.append("\n	 select case when a.isClock=1 then '9999' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_TASK+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		notInArgs.add(ConstantInterface.TYPE_TASK);
		//周报模块
		sql.append("\n union all");
		sql.append("\n select a.busType from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id ");
		sql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_WEEK+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		notInArgs.add(ConstantInterface.TYPE_WEEK);
		//分享模块
		sql.append("\n union all");
		sql.append("\n select a.busType from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id ");
		sql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_DAILY+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		notInArgs.add(ConstantInterface.TYPE_DAILY);
		//会议确认
		sql.append("\n union all");
		sql.append("\n select a.busType from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id ");
		sql.append("\n where a.busSpec=1 and to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') < meet.startdate");
		this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP},
				sql, args, "\n and a.busType in ? ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		notInArgs.add(ConstantInterface.TYPE_MEETING);
		notInArgs.add(ConstantInterface.TYPE_MEETING_SP);
		//加入记录模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.busType from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_APPLY+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		notInArgs.add(ConstantInterface.TYPE_APPLY);
		//会议申请
		sql.append("\n union all");
		sql.append("\n select a.busType from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id ");
		sql.append("\n where a.busSpec=1 and a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_MEETROOM+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		notInArgs.add(ConstantInterface.TYPE_MEETROOM);
		//审批模块
		sql.append("\n	 union all");
		sql.append("\n	 select case when a.isClock=1 then '9999' else a.busType end busType ");
		sql.append("\n	 from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id and sp.flowState=1 ");
		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		notInArgs.add(ConstantInterface.TYPE_FLOW_SP);
		//完结审批模块
		sql.append("\n	 union all");
		sql.append("\n	 select case when a.isClock=1 then '9999' else '022' end busType ");
		sql.append("\n	 from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id ");
		sql.append("\n where a.busspec=1 and a.readstate =0 and a.busType='"+ConstantInterface.TYPE_SP_END+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		notInArgs.add(ConstantInterface.TYPE_SP_END);
		//以下归类其它
//		//分享模块
//		sql.append("\n	 union all");
//		sql.append("\n	 select case when a.isClock=1 then '1' else a.busType end busType ");
//		sql.append("\n	 from todayWorks a inner join msgshare c on a.comId=c.comId and a.busId=c.id ");
//		sql.append("\n where  a.busSpec=1 and a.busType='1' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append(" and (a.userId=? or a.userId=0)");
//		args.add(userId);
//		//客户模块
//		sql.append("\n	 union all ");
//		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
//		sql.append("\n	 from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id ");
//		sql.append("\n where  a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_CRM+"' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append(" and (a.userId=? or a.userId=0)");
//		args.add(userId);
//		//项目模块
//		sql.append("\n	 union all ");
//		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
//		sql.append("\n	 from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id ");
//		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_ITEM+"' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append(" and (a.userId=? or a.userId=0)");
//		args.add(userId);
//		//任务报延
//		sql.append("\n	 union all");
//		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
//		sql.append("\n	 from todayWorks a inner join delayApply t on a.comId=t.comId and a.busId=t.id ");
//		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_DELAYAPPLY+"' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append(" and (a.userId=? or a.userId=0)");
//		args.add(userId);
//		//制度模块
//		sql.append("\n	 union all");
//		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
//		sql.append("\n	 from todayWorks a inner join institution t on a.comId=t.comId and a.busId=t.id ");
//		sql.append("\n where    a.busSpec=1 and  a.busType='"+ConstantInterface.TYPE_INSTITUTION+"' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append(" and (a.userId=? or a.userId=0)");
//		args.add(userId);
//		
//		//采购
//		sql.append("\n	 union all");
//		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
//		sql.append("\n	 from todayWorks a inner join bgypPurOrder t on a.comId=t.comId and a.busId=t.id ");
//		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_BGYP_BUY_CHECK+"' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append(" and (a.userId=? or a.userId=0)");
//		args.add(userId);
//		//申领待办
//		sql.append("\n	 union all");
//		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
//		sql.append("\n	 from todayWorks a inner join bgypApply t on a.comId=t.comId and a.busId=t.id ");
//		sql.append("\n where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_BGYP_APPLY_CHECK+"' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append(" and (a.userId=? or a.userId=0)");
//		args.add(userId);
//		
//		//会议纪要审批
//		sql.append("\n union all");
//		sql.append("\n select a.busType from todayWorks a inner join meetSummary summary on a.comId=summary.comId and a.busId=summary.id ");
//		sql.append("\n inner join meeting meet on a.comId=meet.comId and summary.meetingId=meet.id ");
//		sql.append("\n where a.busSpec=1 and a.busType='"+ConstantInterface.TYPE_MEET_SUMMARY+"' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append(" and (a.userId=? or a.userId=0)");
//		args.add(userId);
//		
//		//催办模块
//		sql.append("\n	 union all");
//		sql.append("\n	 select case when a.isClock=1 then '101' else a.busType end busType ");
//		sql.append("\n	 from todayWorks a inner join busremind t on a.comId=t.comId and a.busId=t.id ");
//		sql.append("\n  where a.busspec=1 and a.busType='"+ConstantInterface.TYPE_REMINDER+"' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append("\n  and (a.userId=? or a.userId=0)");
//		args.add(userId);
//		
//		//闹铃待办
//		sql.append("\n	 union all");
//		sql.append("\n	 select '101' busType from todayWorks a ");
//		sql.append("\n where a.busSpec=1 and a.busType='0' ");
//		this.addSqlWhere(comId, sql, args, " and a.comId=?");
//		sql.append(" and (a.userId=? or a.userId=0)");
//		args.add(userId);
		
		
		//财务结算通知模块、任务报延模块、属性变更已读信息
		sql.append("\n	 union all");
		sql.append("\n	 select '9999' as busType ");
		sql.append("\n	 from todayWorks a ");
		sql.append("\n  where a.busspec=1 and a.readState=1 and (a.busType = ? or a.busType = ? or a.busType = ?)");
		args.add(ConstantInterface.TYPE_FINALCIAL_BALANCE);
		args.add(ConstantInterface.TYPE_DELAYAPPLY);
		args.add(ConstantInterface.TYPE_CHANGE_EXAM);
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  and (a.userId=? or a.userId=0)");
		args.add(userId);
		//其它模块
		sql.append("\n	 union all");
		sql.append("\n	 select '9999' as busType ");
		sql.append("\n	 from todayWorks a ");
		sql.append("\n  where a.busspec=1 and a.readState=0");

		this.addSqlWhereIn(notInArgs.toArray(),sql, args, "\n and a.busType not in ? ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  and (a.userId=? or a.userId=0)");
		args.add(userId);
		sql.append("\n	 )a group by busType");
		return this.listQuery(sql.toString(), args.toArray(), MsgNoRead.class);
	}
	
	/**
	 * 分模块更新未读
	 * @param comId 企业号
	 * @param userId 操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgNoRead> countAttenNoReadByType(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		//总的sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("\n select busType, count(busType) noReadNum from (");
		sql.append("\n  select case when(");
		sql.append("\n  select count(*) from");
		sql.append("\n  todayworks today where a.comId = today.comId and a.busId = today.busId	");
		sql.append("\n  and today.busType=a.busType and today.userId='"+userId+"' and today.isClock=0");
		sql.append("\n and today.readState=0)=0 then 0 else 1 end as isread,busType");
		sql.append("\n   from (");

		//公告
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join announcement b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_ANNOUNCEMENT);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join institution b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_INSTITUTION);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		//客户
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join customer b on a.busId=b.id ");
		sql.append("\n and  b.delState=0 and a.busType="+ConstantInterface.TYPE_CRM);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		
		//项目
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join item b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_ITEM);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		
		//任务
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join task b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_TASK);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		
		//问答
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join question b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_QUES);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		//审批
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join spFlowInstance sp on a.busId=sp.id");
		sql.append("\n and sp.flowState>0 and sp.flowState<>2 and a.busType="+ConstantInterface.TYPE_FLOW_SP);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		//投票
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join vote b on a.busId=b.id");
		sql.append("\n and b.delState=0 and a.busType="+ConstantInterface.TYPE_VOTE);
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n union all");
		//分享
		sql.append("\n select a.comId,a.id,a.busId,a.busType from attention a inner join msgshare b on a.busId=b.id and a.busType=1");
		sql.append("\n where 1=1");
		
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=? ");
		
		sql.append("\n ) a ");
		sql.append("\n ) a where  isread=1");
		sql.append("\n group by busType");
		return this.listQuery(sql.toString(), args.toArray(), MsgNoRead.class);
	}
	
	/**
	 * 获取待办提醒数目(总未读消息)
	 * @param comId 企业号
	 * @param userId 当前用户主键
	 * @return
	 */
	public Integer countNoRead(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(*)from (");
		//客户模块
		sql.append("\n	 select a.* from todayWorks a inner join customer c on a.comId=c.comId and a.busId=c.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_CRM+"' and c.delstate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//日程模块
		sql.append("\n	 union all ");
		sql.append("\n	 select a.* from todayWorks a inner join SCHEDULE c on a.comId=c.comId and a.busId=c.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_SCHEDULE+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//项目模块
		sql.append("\n	 union all ");
		sql.append("\n	 select a.* from todayWorks a inner join item i on a.comId=i.comId and a.busId=i.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_ITEM+"' and i.delstate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//产品模块
		sql.append("\n	 union all ");
		sql.append("\n	 select a.* from todayWorks a inner join product pro on a.comId=pro.comId and a.busId=pro.id ");
		sql.append("\n where a.readState=0 and a.busType='" + ConstantInterface.TYPE_PRODUCT + "'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//任务模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join task t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_TASK+"' and t.delstate=0 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//审批模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_FLOW_SP+"' and sp.flowState=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//审批模块（完结）
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join spFlowInstance sp on a.comId=sp.comId and a.busId=sp.id ");
		sql.append("\n where a.readstate ='0' and a.busType='"+ConstantInterface.TYPE_SP_END+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//问答模块
		sql.append("\n	  union all");
		sql.append("\n	 select a.* from todayWorks a inner join question q on a.comId=q.comId and a.busId=q.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_QUES+"' and q.delstate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//投票模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join vote v on a.comId=v.comId and a.busId=v.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_VOTE+"' and v.delstate=0");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//周报模块
		sql.append("\n	  union all");
		sql.append("\n	 select a.* from todayWorks a inner join weekReport w on a.comId=w.comId and a.busId=w.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_WEEK+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//分享模块
		sql.append("\n	  union all");
		sql.append("\n	 select a.* from todayWorks a inner join daily w on a.comId=w.comId and a.busId=w.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_DAILY+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//附件模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join fileDetail f on a.comId=f.comId and a.busId=f.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_FILE+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//分享模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join msgShare m on a.comId=m.comId and a.busId=m.id");
		sql.append("\n where a.readState=0 and a.busType='1'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//加入记录模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join joinrecord j on a.comId=j.comId and a.busId=j.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_APPLY+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//通知消息
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_NOTICE+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//会议模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		sql.append("\n where a.readState=0 ");
		this.addSqlWhereIn(new Object[]{ConstantInterface.TYPE_MEETING,ConstantInterface.TYPE_MEETING_SP},
				sql, args, "\n and a.busType in ? ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//会议申请模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join meeting meet on a.comId=meet.comId and a.busId=meet.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_MEETROOM+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//采购
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join bgypPurOrder bPur on a.comId=bPur.comId and a.busId=bPur.id ");
		sql.append("\n where a.readState=0 and a.busType in ('"+ConstantInterface.TYPE_BGYP_BUY_CHECK+"','"+ConstantInterface.TYPE_BGYP_BUY_NOTICE+"')");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//申领
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join bgypApply bapply on a.comId=bapply.comId and a.busId=bapply.id ");
		sql.append("\n where a.readState=0 and a.busType in ('"+ConstantInterface.TYPE_BGYP_APPLY_CHECK+"','"+ConstantInterface.TYPE_BGYP_APPLY_NOTICE+"')");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
				
		//闹铃模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a ");
		sql.append("\n where a.readState=0 and a.busType='0' and a.busId=0 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//公告模块
		sql.append("\n union all");
		sql.append("\n select a.* from todayWorks a ");
		sql.append("\n inner join announcement an on a.busId = an.id and  a.comId= ? ");
		args.add(comId);
		sql.append("\n where a.readstate ='0' and an.delstate= 0 and a.busType='"+ConstantInterface.TYPE_ANNOUNCEMENT+"' ");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//制度模块
		sql.append("\n union all");
		sql.append("\n select a.* from todayWorks a ");
		sql.append("\n inner join institution an on a.busId = an.id and  a.comId= ? ");
		args.add(comId);
		sql.append("\n where a.readstate ='0' and an.delstate= 0 and a.busType='"+ConstantInterface.TYPE_INSTITUTION+"' ");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//会议纪要审批
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a");
		sql.append("\n	 inner join meetSummary summary on a.comId=summary.comId and a.busId=summary.id");
		sql.append("\n	 inner join meeting meet on a.comId=meet.comId and summary.meetingId=meet.id");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_MEET_SUMMARY+"' ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//领款通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_NOTIFICATIONS+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//外部联系人模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_OUTLINKMAN+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		//完成结算通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_BALANCED+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//财务结算通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join spFlowInstance t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_FINALCIAL_BALANCE+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//属性变更通知模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join moduleChangeApply t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_CHANGE_EXAM+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		//催办模块
		sql.append("\n	 union all");
		sql.append("\n	 select a.* from todayWorks a inner join busremind t on a.comId=t.comId and a.busId=t.id ");
		sql.append("\n where a.readState=0 and a.busType='"+ConstantInterface.TYPE_REMINDER+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" and (a.userId=? or a.userId=0)");
		args.add(userId);
		
		sql.append("\n	 )a");
		return this.countQuery(sql.toString(), args.toArray());
	}
	
}
