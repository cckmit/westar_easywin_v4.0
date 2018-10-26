package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.RecycleBin;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.StringUtil;

@Repository
public class RecycleBinDao extends BaseDao {

	/**
	 * 获取预删除的数据分页列表
	 * @param recycleBin回收站属性条件
	 * @param modList 模块集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RecycleBin> listPagedPreDel(RecycleBin recycleBin, List<String> modList) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//公告
				StringBuffer announSql = new StringBuffer();
				if(null==modList || modList.indexOf(ConstantInterface.TYPE_ANNOUNCEMENT)>=0){
					announSql.append("\n select a.id,a.recordcreatetime,a.bustype,a.busid,a.comid,a.userid,v.title busTypeName");
					announSql.append("\n  from recycleBin a inner join announcement v on a.comid=v.comId and a.busid=v.id and v.delstate=1");
					announSql.append("\n where a.bustype='"+ConstantInterface.TYPE_ANNOUNCEMENT+"'");
					//查询创建时间段
					this.addSqlWhere(recycleBin.getStartDate(), announSql, args, " and substr(a.recordcreatetime,0,10)>=?");
					this.addSqlWhere(recycleBin.getEndDate(), announSql, args, " and substr(a.recordcreatetime,0,10)<=?");
					
					this.addSqlWhere(recycleBin.getComId(), announSql, args, " and a.comId=?");
					this.addSqlWhere(recycleBin.getUserId(), announSql, args, " and a.userId=? ");
				}
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_TASK)>=0){
			taskSql.append("\n select a.id,a.recordcreatetime,a.bustype,a.busid,a.comid,a.userid,t.taskName busTypeName");
			taskSql.append("\n  from recycleBin a inner join task t on a.comid=t.comId and a.busid=t.id and t.delstate=1");
			taskSql.append("\n where a.bustype='003'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), taskSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), taskSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), taskSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), taskSql, args, " and a.userId=? ");
		}
		//投票
		StringBuffer voteSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_VOTE)>=0){
			voteSql.append("\n select a.id,a.recordcreatetime,a.bustype,a.busid,a.comid,a.userid,dbms_lob.substr(v.voteContent,4000) busTypeName");
			voteSql.append("\n  from recycleBin a inner join vote v on a.comid=v.comId and a.busid=v.id and v.delstate=1");
			voteSql.append("\n where a.bustype='004'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), voteSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), voteSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), voteSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), voteSql, args, " and a.userId=? ");
		}
		//项目
		StringBuffer itemSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_ITEM)>=0){
			itemSql.append("\n select a.id,a.recordcreatetime,a.bustype,a.busid,a.comid,a.userid,i.itemName busTypeName");
			itemSql.append("\n  from recycleBin a inner join item i on a.comid=i.comId and a.busid=i.id and i.delstate=1");
			itemSql.append("\n where  a.bustype='005'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), itemSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), itemSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), itemSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), itemSql, args, " and a.userId=? ");
		}
		//问答
		StringBuffer qasSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_QUES)>=0){
			qasSql.append("\n select a.id,a.recordcreatetime,a.bustype,a.busid,a.comid,a.userid,q.title busTypeName");
			qasSql.append("\n  from recycleBin a inner join question q on a.comid=q.comId and a.busid=q.id and q.delstate=1");
			qasSql.append("\n where  a.bustype='011'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), qasSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), qasSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), qasSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), qasSql, args, " and a.userId=? ");
		}
		//客户
		StringBuffer crmSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_CRM)>=0){
			crmSql.append("\n select a.id,a.recordcreatetime,a.bustype,a.busid,a.comid,a.userid,c.customername busTypeName");
			crmSql.append("\n  from recycleBin a inner join customer c on a.comid=c.comId and a.busid=c.id and c.delstate=1 ");
			crmSql.append("\n where a.bustype='012'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), crmSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), crmSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), crmSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), crmSql, args, " and (a.userId=? or a.userId=0)");
		}
		sql.append("\n select a.id,a.recordcreatetime,a.bustype,a.busid,a.comid,a.userid,a.busTypeName from (");
		if(null==modList){
			sql.append(taskSql);
			sql.append("\n union all ");
			sql.append(voteSql);
			sql.append("\n union all ");
			sql.append(itemSql);
			sql.append("\n union all ");
			sql.append(qasSql);
			sql.append("\n union all ");
			sql.append(crmSql);
			sql.append("\n union all ");
			sql.append(announSql);
		}else {
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
					case 39:
						sql.append(announSql);
						break;
					default:
						break;
					}
				if(i<modList.size()-1){
					sql.append("\n union all ");
				}
			}
		}
		sql.append("\n )a");
		sql.append("\n where 1=1 ");
		this.addSqlWhereLike(recycleBin.getContent(), sql, args, " and a.busTypeName like ?");
		return this.pagedQuery(sql.toString(),"a.recordCreateTime desc,a.bustype", args.toArray(), RecycleBin.class);
	}

	/**
	 * （页面删除）选出所有满足删除条件的数据
	 * @param recycleBin
	 * @param modTypes 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RecycleBin> listAllPreDel(RecycleBin recycleBin, List<String> modList) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_TASK)>=0){
			taskSql.append("\n select a.id,a.bustype,a.busid,t.taskName busTypeName");
			taskSql.append("\n  from recycleBin a inner join task t on a.comid=t.comId and a.busid=t.id and t.delstate=1");
			taskSql.append("\n where a.bustype='003'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), taskSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), taskSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), taskSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), taskSql, args, " and a.userId=? ");
		}
		//投票
		StringBuffer voteSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_VOTE)>=0){
			voteSql.append("\n select a.id,a.bustype,a.busid,dbms_lob.substr(v.voteContent,4000) busTypeName");
			voteSql.append("\n  from recycleBin a inner join vote v on a.comid=v.comId and a.busid=v.id and v.delstate=1");
			voteSql.append("\n where a.bustype='004'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), voteSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), voteSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), voteSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), voteSql, args, " and a.userId=? ");
		}
		//项目
		StringBuffer itemSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_ITEM)>=0){
			itemSql.append("\n select a.id,a.bustype,a.busid,i.itemName busTypeName");
			itemSql.append("\n  from recycleBin a inner join item i on a.comid=i.comId and a.busid=i.id and i.delstate=1");
			itemSql.append("\n where  a.bustype='005'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), itemSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), itemSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), itemSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), itemSql, args, " and a.userId=? ");
		}
		//问答
		StringBuffer qasSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_QUES)>=0){
			qasSql.append("\n select a.id,a.bustype,a.busid,q.title busTypeName");
			qasSql.append("\n  from recycleBin a inner join question q on a.comid=q.comId and a.busid=q.id and q.delstate=1");
			qasSql.append("\n where  a.bustype='011'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), qasSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), qasSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), qasSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), qasSql, args, " and a.userId=? ");
		}
		//客户
		StringBuffer crmSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_CRM)>=0){
			crmSql.append("\n select a.id,a.bustype,a.busid,c.customername busTypeName");
			crmSql.append("\n  from recycleBin a inner join customer c on a.comid=c.comId and a.busid=c.id and c.delstate=1 ");
			crmSql.append("\n where a.bustype='012'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), crmSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), crmSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), crmSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), crmSql, args, " and (a.userId=? or a.userId=0)");
		}
		sql.append("\n select a.id,a.bustype,a.busid,a.busTypeName from (");
		if(null==modList){
			sql.append(crmSql);
			sql.append("\n union all ");
			sql.append(itemSql);
			sql.append("\n union all ");
			sql.append(taskSql);
			sql.append("\n union all ");
			sql.append(qasSql);
			sql.append("\n union all ");
			sql.append(voteSql);
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
					default:
						break;
					}
				if(i<modList.size()-1){
					sql.append("\n union all ");
				}
			}
		}
		sql.append("\n )a");
		sql.append("\n where 1=1 ");
		this.addSqlWhereLike(recycleBin.getContent(), sql, args, " and a.busTypeName like ?");
		sql.append("\n order by a.id desc,a.bustype");
		return this.listQuery(sql.toString(),args.toArray(), RecycleBin.class);
	}
	/**
	 * （登录或是定时删除）选出所有满足草果哦三天的数据
	 * @param recycleBin
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RecycleBin> listAllOverTri(RecycleBin recycleBin) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//业务类型
		String type = recycleBin.getBusType();
		//客户
		StringBuffer crmSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_CRM.equals(type)){
			crmSql.append("\n select a.id,a.bustype,a.busid,a.userId,a.comId,c.customername busTypeName");
			crmSql.append("\n  from recycleBin a inner join customer c on a.comid=c.comId and a.busid=c.id and c.delstate=1 ");
			crmSql.append("\n where a.bustype='012'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getEndDate(), crmSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), crmSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), crmSql, args, " and (a.userId=? or a.userId=0)");
		}
		//项目
		StringBuffer itemSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_ITEM.equals(type)){
			itemSql.append("\n select a.id,a.bustype,a.busid,a.userId,a.comId,i.itemName busTypeName");
			itemSql.append("\n  from recycleBin a inner join item i on a.comid=i.comId and a.busid=i.id and i.delstate=1");
			itemSql.append("\n where  a.bustype='005'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getEndDate(), itemSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), itemSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), itemSql, args, " and a.userId=? ");
		}
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_TASK.equals(type)){
			taskSql.append("\n select a.id,a.bustype,a.busid,a.userId,a.comId,t.taskName busTypeName");
			taskSql.append("\n  from recycleBin a inner join task t on a.comid=t.comId and a.busid=t.id and t.delstate=1");
			taskSql.append("\n where a.bustype='003'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getEndDate(), taskSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), taskSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), taskSql, args, " and a.userId=? ");
		}
		//问答
		StringBuffer qasSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_QUES.equals(type)){
			qasSql.append("\n select a.id,a.bustype,a.busid,a.userId,a.comId,q.title busTypeName");
			qasSql.append("\n  from recycleBin a inner join question q on a.comid=q.comId and a.busid=q.id and q.delstate=1");
			qasSql.append("\n where  a.bustype='011'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getEndDate(), qasSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), qasSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), qasSql, args, " and a.userId=? ");
		}
		//投票
		StringBuffer voteSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_VOTE.equals(type)){
			voteSql.append("\n select a.id,a.bustype,a.busid,a.userId,a.comId,dbms_lob.substr(v.voteContent,4000) busTypeName");
			voteSql.append("\n  from recycleBin a inner join vote v on a.comid=v.comId and a.busid=v.id and v.delstate=1");
			voteSql.append("\n where a.bustype='004'");
			//查询创建时间段
			this.addSqlWhere(recycleBin.getStartDate(), voteSql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(recycleBin.getEndDate(), voteSql, args, " and substr(a.recordcreatetime,0,10)<=?");
			
			this.addSqlWhere(recycleBin.getComId(), voteSql, args, " and a.comId=?");
			this.addSqlWhere(recycleBin.getUserId(), voteSql, args, " and a.userId=? ");
		}
		sql.append("\n select a.id,a.bustype,a.busid,a.userId,a.comId,a.busTypeName from (");
		if(StringUtil.isBlank(StringUtil.delNull(type))){
			sql.append(crmSql);
			sql.append("\n union all ");
			sql.append(itemSql);
			sql.append("\n union all ");
			sql.append(taskSql);
			sql.append("\n union all ");
			sql.append(qasSql);
			sql.append("\n union all ");
			sql.append(voteSql);
		}else if(ConstantInterface.TYPE_CRM.equals(type)){
			sql.append(crmSql);
		}else if(ConstantInterface.TYPE_ITEM.equals(type)){
			sql.append(itemSql);
		}else if(ConstantInterface.TYPE_TASK.equals(type)){
			sql.append(taskSql);
		}else if(ConstantInterface.TYPE_QUES.equals(type)){
			sql.append(qasSql);
		}else if(ConstantInterface.TYPE_VOTE.equals(type)){
			sql.append(voteSql);
		}
		sql.append("\n )a");
		sql.append("\n order by a.comId,a.userId,a.id desc,a.bustype");
		return this.listQuery(sql.toString(),args.toArray(), RecycleBin.class);
	}

	

}
