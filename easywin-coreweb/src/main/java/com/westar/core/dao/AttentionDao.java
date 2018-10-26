package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Attention;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.StringUtil;

@Repository
public class AttentionDao extends BaseDao {

	/**
	 * 分页查询关注信息
	 * @param atten
	 * @param modList 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Attention> listpagedAtten(Attention atten, List<String> modList) {
		List<Object> args = new ArrayList<Object>();
		//总的sql语句
		StringBuffer sql = new StringBuffer();
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_TASK)>=0){
			taskSql.append("\n select a.*,b.owner,b.taskname modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName,0 msgId");
			taskSql.append("\n from attention a inner join task b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_TASK);
			taskSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			taskSql.append("\n inner join userInfo d on b.owner=d.id");
			taskSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			taskSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), taskSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), taskSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), taskSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), taskSql, args, " and a.userId=? ");
			this.addSqlWhere(atten.getOwner(), taskSql, args, " and b.owner=? ");
			this.addSqlWhere(atten.getStatus(), taskSql, args, " and b.state=? ");
			this.addSqlWhere(atten.getExecutor(), taskSql, args, " and b.id in(SELECT TASKID from TASKEXECUTOR where EXECUTOR = ?) ");
			
		}
		//投票
		StringBuffer voteSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_VOTE)>=0){
			voteSql.append("\n select a.*,b.owner,dbms_lob.substr(b.votecontent,4000) modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName,0 msgId");
			voteSql.append("\n from attention a inner join vote b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_VOTE);
			voteSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			voteSql.append("\n inner join userInfo d on b.owner=d.id");
			voteSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			voteSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), voteSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), voteSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), voteSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), voteSql, args, " and a.userId=? ");
			this.addSqlWhere(atten.getOwner(), voteSql, args, " and b.owner=? ");
		}
		//项目
		StringBuffer itemSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_ITEM)>=0){
			itemSql.append("\n select a.*,b.owner,b.itemname modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName,0 msgId");
			itemSql.append("\n from attention a inner join item b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_ITEM);
			itemSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			itemSql.append("\n inner join userInfo d on b.owner=d.id");
			itemSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			itemSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), itemSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), itemSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), itemSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), itemSql, args, " and a.userId=? ");
			this.addSqlWhere(atten.getOwner(), itemSql, args, " and b.owner=? ");
			this.addSqlWhere(atten.getStatus(), itemSql, args, " and b.state=? ");
		}
		//问答
		StringBuffer qasSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_QUES)>=0){
			qasSql.append("\n select a.*,b.userId owner,b.title modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName,0 msgId");
			qasSql.append("\n from attention a inner join question b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_QUES);
			qasSql.append("\n inner join userOrganic c on b.comId=c.comId and b.userId=c.userId");
			qasSql.append("\n inner join userInfo d on b.userId=d.id");
			qasSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			qasSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), qasSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), qasSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), qasSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), qasSql, args, " and a.userId=? ");
			this.addSqlWhere(atten.getOwner(), qasSql, args, " and b.userId=? ");
		}
		//客户
		StringBuffer crmSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_CRM)>=0){
			crmSql.append("\n select a.*,b.owner,b.customername modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName,0 msgId");
			crmSql.append("\n from attention a inner join customer b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_CRM);
			crmSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			crmSql.append("\n inner join userInfo d on b.owner=d.id");
			crmSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			crmSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), crmSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), crmSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), crmSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), crmSql, args, " and a.userId=? ");
			this.addSqlWhere(atten.getOwner(), crmSql, args, " and b.owner=? ");
			this.addSqlWhere(atten.getCustomerTypeId(), crmSql, args, " and b.customerTypeId=? ");
		}
		//审批
		StringBuffer spSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_FLOW_SP)>=0){
			spSql.append("\n select a.*,sp.creator owner ,sp.flowName modTitle,sp.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName,0 msgId");
			spSql.append("\n from attention a inner join spFlowInstance sp on a.busId=sp.id and sp.flowState>0 and sp.flowState<>2 and a.busType="+ConstantInterface.TYPE_FLOW_SP);
			spSql.append("\n inner join userOrganic c on sp.comId=c.comId and sp.creator=c.userId");
			spSql.append("\n inner join userInfo d on sp.creator=d.id");
			spSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			spSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), spSql, args, " and substr(sp.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), spSql, args, " and substr(sp.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), spSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), spSql, args, " and a.userId=? ");
			this.addSqlWhere(atten.getOwner(), spSql, args, " and sp.creator=? ");
		}
		
		//产品
		StringBuffer proSql = new StringBuffer();
		if(null==modList || modList.indexOf(ConstantInterface.TYPE_PRODUCT)>=0){
			proSql.append("\n select a.*,b.creator as owner,b.name modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName,0 msgId");
			proSql.append("\n from attention a inner join product b on a.busId=b.id and a.busType=" + ConstantInterface.TYPE_PRODUCT);
			proSql.append("\n inner join userOrganic c on b.comId=c.comId and b.creator=c.userId");
			proSql.append("\n inner join userInfo d on b.creator=d.id");
			proSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			proSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), proSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), proSql, args, " and substr(b.recordCreateTime,0,10)<=?");

			this.addSqlWhere(atten.getComId(), proSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), proSql, args, " and a.userId=? ");
			this.addSqlWhere(atten.getOwner(), proSql, args, " and b.creator=? ");
			this.addSqlWhere(atten.getStatus(), proSql, args, " and b.state=? ");
		}
		
		//分享
		StringBuffer shareSql = new StringBuffer();
		if(null==modList || modList.indexOf("100")>=0){
			shareSql.append("\n select a.*,b.creator owner,dbms_lob.substr(b.content,4000) modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName,b.id msgId");
			shareSql.append("\n from attention a inner join msgshare b on a.busId=b.modid  and a.busType=050");
			shareSql.append("\n inner join userOrganic c on b.comId=c.comId and b.creator=c.userId");
			shareSql.append("\n inner join userInfo d on b.creator=d.id");
			shareSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			shareSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), shareSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), shareSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), shareSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), shareSql, args, " and a.userId=? ");
			this.addSqlWhere(atten.getOwner(), shareSql, args, " and b.creator=? ");
		}
		sql.append("select * from (");
		sql.append("\n select a.*,b.content,b.userId modifyer,d.userName modiferName,f.uuid modifyerUuid,f.filename modifyerFileName,b.recordCreateTime modifTime,");

		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where a.comId = today.comId and a.busId = today.busId \n");
		sql.append(" and today.busType=a.busType and today.userId='"+atten.getUserId()+"' and today.isclock=0\n");
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 1 else 0 end as isread\n");
		
		sql.append("\n from (");
		if(null==modList){//全部
			sql.append(taskSql);//003
			sql.append("\n union all ");
			sql.append(voteSql);//004
			sql.append("\n union all ");
			sql.append(itemSql);//005
			sql.append("\n union all ");
			sql.append(qasSql);//011
			sql.append("\n union all ");
			sql.append(crmSql);//012
			sql.append("\n union all ");
			sql.append(spSql);//022
			sql.append("\n union all ");
			sql.append(proSql);//022
			sql.append("\n union all ");
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
					case 22:
						sql.append(spSql);
						break;
					case 80:
						sql.append(proSql);
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
		sql.append("\n ) a ");
		sql.append("\n left join newsInfo b on a.comId=b.comId and a.busId=b.busId and a.busType=b.busType  ");
		sql.append("\n left join userInfo d on b.userId=d.id");
		sql.append("\n left join userOrganic uOrg on a.comId=uOrg.comId and d.id=uOrg.userId");
		sql.append("\n left join upfiles f on a.comId=f.comId and uOrg.smallheadportrait=f.id");
		
		sql.append("\n where 1=1 ");
		
		this.addSqlWhereLike(atten.getModTitle(), sql, args, " and a.modTitle like ? ");
		this.addSqlWhere(atten.getOwner(), sql, args, " and a.owner=?");
		sql.append(")  where 1=1 ");
		this.addSqlWhere(atten.getIsRead(), sql, args, " and isread=?");
		return this.pagedQuery(sql.toString(), "isread,modTime desc,busType desc", args.toArray(), Attention.class);
	}
	/**
	 * 查看所有的关注数据，不分页
	 * @param atten
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Attention> listAttenOfAll(Attention atten) {
		List<Object> args = new ArrayList<Object>();
		//总的sql语句
		StringBuffer sql = new StringBuffer();
		//业务类型
		String type = atten.getBusType();
		//客户
		StringBuffer crmSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_CRM.equals(type)){
			
			crmSql.append("\n select a.*,b.owner,b.customername modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			crmSql.append("\n from attention a inner join customer b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_CRM);
			crmSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			crmSql.append("\n inner join userInfo d on b.owner=d.id");
			crmSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			crmSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), crmSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), crmSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), crmSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), crmSql, args, " and a.userId=? ");
		}
		//项目
		StringBuffer itemSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_ITEM.equals(type)){
			itemSql.append("\n select a.*,b.owner,b.itemname modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			itemSql.append("\n from attention a inner join item b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_ITEM);
			itemSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			itemSql.append("\n inner join userInfo d on b.owner=d.id");
			itemSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			itemSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), itemSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), itemSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), itemSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), itemSql, args, " and a.userId=? ");
		}
		//产品
		StringBuffer proSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_PRODUCT.equals(type)){
			proSql.append("\n select a.*,b.creator as owner,b.name modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			proSql.append("\n from attention a inner join product b on a.busId=b.id and a.busType=" + ConstantInterface.TYPE_PRODUCT);
			proSql.append("\n inner join userOrganic c on b.comId=c.comId and b.creator=c.userId");
			proSql.append("\n inner join userInfo d on b.creator=d.id");
			proSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			proSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), proSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), proSql, args, " and substr(b.recordCreateTime,0,10)<=?");

			this.addSqlWhere(atten.getComId(), proSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), proSql, args, " and a.userId=? ");
		}
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_TASK.equals(type)){
			taskSql.append("\n select a.*,b.owner,b.taskname modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			taskSql.append("\n from attention a inner join task b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_TASK);
			taskSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			taskSql.append("\n inner join userInfo d on b.owner=d.id");
			taskSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			taskSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), taskSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), taskSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), taskSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), taskSql, args, " and a.userId=? ");
			
		}
		//问答
		StringBuffer qasSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_QUES.equals(type)){
			qasSql.append("\n select a.*,b.userId owner,b.title modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			qasSql.append("\n from attention a inner join question b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_QUES);
			qasSql.append("\n inner join userOrganic c on b.comId=c.comId and b.userId=c.userId");
			qasSql.append("\n inner join userInfo d on b.userId=d.id");
			qasSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			qasSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), qasSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), qasSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), qasSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), qasSql, args, " and a.userId=? ");
		}
		//投票
		StringBuffer voteSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_VOTE.equals(type)){
			voteSql.append("\n select a.*,b.owner,dbms_lob.substr(b.votecontent,4000) modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			voteSql.append("\n from attention a inner join vote b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_VOTE);
			voteSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			voteSql.append("\n inner join userInfo d on b.owner=d.id");
			voteSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			voteSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), voteSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), voteSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), voteSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), voteSql, args, " and a.userId=? ");
		}
		//分享
		StringBuffer shareSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_DAILY.equals(type)){
			shareSql.append("\n select a.*,b.creator owner,dbms_lob.substr(b.content,4000) modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			shareSql.append("\n from attention a inner join msgshare b on a.busId=b.modid  and a.busType=050");
			shareSql.append("\n inner join userOrganic c on b.comId=c.comId and b.creator=c.userId");
			shareSql.append("\n inner join userInfo d on b.creator=d.id");
			shareSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			shareSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), shareSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), shareSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), shareSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), shareSql, args, " and a.userId=? ");
		}
		
		sql.append("\n select a.*,b.content,d.userName modiferName,b.recordCreateTime modifTime,");
		sql.append("\n case when (c.id is null or c.readState=1) then 1 else 0 end as isread");
		sql.append("\n from (");
		//全部
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
			sql.append("\n union all ");
			sql.append(shareSql);
		}else if(ConstantInterface.TYPE_CRM.equals(type)){//只客户
			sql.append(crmSql);
		}else if(ConstantInterface.TYPE_ITEM.equals(type)){//只项目
			sql.append(itemSql);
		}else if(ConstantInterface.TYPE_PRODUCT.equals(type)){//只产品
			sql.append(proSql);
		}else if(ConstantInterface.TYPE_TASK.equals(type)){//只任务
			sql.append(taskSql);
		}else if(ConstantInterface.TYPE_QUES.equals(type)){//只问答
			sql.append(qasSql);
		}else if(ConstantInterface.TYPE_VOTE.equals(type)){//只投票
			sql.append(voteSql);
		}else if(ConstantInterface.TYPE_DAILY.equals(type)){//只分享
			sql.append(shareSql);
		}
		sql.append("\n ) a ");
		sql.append("\n left join newsInfo b on a.comId=b.comId and a.busId=b.busId and a.busType=b.busType  ");
		sql.append("\n left join todayWorks c on a.comId=c.comId and a.busId=c.busId and a.busType=c.busType");
		sql.append("\n and c.userId=a.userId and c.isclock=0");
		sql.append("\n left join userInfo d on b.userId=d.id where 1=1 ");
		
		this.addSqlWhereLike(atten.getModTitle(), sql, args, " and a.modTitle like ? ");
		this.addSqlWhere(atten.getOwner(), sql, args, " and a.owner=?");
		sql.append("\n order by isread,a.modTime desc,a.busType desc");
		return this.listQuery(sql.toString(), args.toArray(), Attention.class);
	}
	/**
	 * 我的所有关注
	 * @param atten
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Attention> listMyAtten(Attention atten) {
		List<Object> args = new ArrayList<Object>();
		//总的sql语句
		StringBuffer sql = new StringBuffer();
		//业务类型
		String type = atten.getBusType();
		//客户
		StringBuffer crmSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_CRM.equals(type)){
			
			crmSql.append("\n select a.*,b.owner,b.customername modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			crmSql.append("\n from attention a inner join customer b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_CRM);
			crmSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			crmSql.append("\n inner join userInfo d on b.owner=d.id");
			crmSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			crmSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), crmSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), crmSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), crmSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), crmSql, args, " and a.userId=? ");
		}
		//项目
		StringBuffer itemSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_ITEM.equals(type)){
			itemSql.append("\n select a.*,b.owner,b.itemname modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			itemSql.append("\n from attention a inner join item b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_ITEM);
			itemSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			itemSql.append("\n inner join userInfo d on b.owner=d.id");
			itemSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			itemSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), itemSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), itemSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), itemSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), itemSql, args, " and a.userId=? ");
		}
		//项目
		StringBuffer proSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_PRODUCT.equals(type)){
			proSql.append("\n select a.*,b.creator as owner,b.name modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			proSql.append("\n from attention a inner join product b on a.busId=b.id and a.busType=" + ConstantInterface.TYPE_PRODUCT);
			proSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			proSql.append("\n inner join userInfo d on b.owner=d.id");
			proSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			proSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), proSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), proSql, args, " and substr(b.recordCreateTime,0,10)<=?");

			this.addSqlWhere(atten.getComId(), proSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), proSql, args, " and a.userId=? ");
		}
		//任务
		StringBuffer taskSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_TASK.equals(type)){
			taskSql.append("\n select a.*,b.owner,b.taskname modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			taskSql.append("\n from attention a inner join task b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_TASK);
			taskSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			taskSql.append("\n inner join userInfo d on b.owner=d.id");
			taskSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			taskSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), taskSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), taskSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), taskSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), taskSql, args, " and a.userId=? ");
			
		}
		//问答
		StringBuffer qasSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_QUES.equals(type)){
			qasSql.append("\n select a.*,b.userId owner,b.title modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			qasSql.append("\n from attention a inner join question b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_QUES);
			qasSql.append("\n inner join userOrganic c on b.comId=c.comId and b.userId=c.userId");
			qasSql.append("\n inner join userInfo d on b.userId=d.id");
			qasSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			qasSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), qasSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), qasSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), qasSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), qasSql, args, " and a.userId=? ");
		}
		//投票
		StringBuffer voteSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_VOTE.equals(type)){
			voteSql.append("\n select a.*,b.owner,dbms_lob.substr(b.votecontent,4000) modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			voteSql.append("\n from attention a inner join vote b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_VOTE);
			voteSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
			voteSql.append("\n inner join userInfo d on b.owner=d.id");
			voteSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			voteSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), voteSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), voteSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), voteSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), voteSql, args, " and a.userId=? ");
		}
		//分享
		StringBuffer shareSql = new StringBuffer();
		if(StringUtil.isBlank(StringUtil.delNull(type)) || ConstantInterface.TYPE_DAILY.equals(type)){
			shareSql.append("\n select a.*,b.creator owner,dbms_lob.substr(b.content,4000) modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
			shareSql.append("\n from attention a inner join msgshare b on a.busId=b.modid  and a.busType=050");
			shareSql.append("\n inner join userOrganic c on b.comId=c.comId and b.creator=c.userId");
			shareSql.append("\n inner join userInfo d on b.creator=d.id");
			shareSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
			shareSql.append("\n where 1=1");
			//查询创建时间段
			this.addSqlWhere(atten.getStartDate(), shareSql, args, " and substr(b.recordCreateTime,0,10)>=?");
			this.addSqlWhere(atten.getEndDate(), shareSql, args, " and substr(b.recordCreateTime,0,10)<=?");
			
			this.addSqlWhere(atten.getComId(), shareSql, args, " and a.comId=?");
			this.addSqlWhere(atten.getUserId(), shareSql, args, " and a.userId=? ");
		}
		
		sql.append("\n select a.*,b.content,d.userName modiferName,b.recordCreateTime modifTime,b.userId modifyer,f.filename,f.uuid modifyerUuid,");
		sql.append("\n case when (c.id is null or c.readState=1) then 1 else 0 end as isread");
		sql.append("\n from (");
		if(StringUtil.isBlank(StringUtil.delNull(type))){//全部
			sql.append(crmSql);
			sql.append("\n union all ");
			sql.append(itemSql);
			sql.append("\n union all ");
			sql.append(taskSql);
			sql.append("\n union all ");
			sql.append(qasSql);
			sql.append("\n union all ");
			sql.append(voteSql);
			sql.append("\n union all ");
			sql.append(shareSql);
		}else if(ConstantInterface.TYPE_CRM.equals(type)){//只客户
			sql.append(crmSql);
		}else if(ConstantInterface.TYPE_ITEM.equals(type)){//只项目
			sql.append(itemSql);
		}else if(ConstantInterface.TYPE_PRODUCT.equals(type)){//只产品
			sql.append(proSql);
		}else if(ConstantInterface.TYPE_TASK.equals(type)){//只任务
			sql.append(taskSql);
		}else if(ConstantInterface.TYPE_QUES.equals(type)){//只问答
			sql.append(qasSql);
		}else if(ConstantInterface.TYPE_VOTE.equals(type)){//只投票
			sql.append(voteSql);
		}else if(ConstantInterface.TYPE_DAILY.equals(type)){//只投票
			sql.append(shareSql);
		}
		sql.append("\n ) a ");
		sql.append("\n left join newsInfo b on a.comId=b.comId and a.busId=b.busId and a.busType=b.busType  ");
		sql.append("\n left join todayWorks c on a.comId=c.comId and a.busId=c.busId and a.busType=c.busType and c.userId=a.userId");
		sql.append("\n left join userInfo d on b.userId=d.id ");
		sql.append("\n inner join userOrganic e on b.comId=e.comId and b.userId=e.userId");
		sql.append("\n left join upfiles f on e.comId=f.comId and e.smallheadportrait=f.id");
		sql.append("\n where 1=1");
		
		this.addSqlWhereLike(atten.getModTitle(), sql, args, " and a.modTitle like ? ");
		this.addSqlWhere(atten.getOwner(), sql, args, " and a.owner=?");
		sql.append(" order by isread,a.modTime desc,a.busType desc");
		return this.listQuery(sql.toString(), args.toArray(),Attention.class);
	}
	/**
	 * 获取前N个关注的信息
	 * @param userInfo 操作员
	 * @param rowNum 结果数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Attention> firstNAttenList(UserInfo userInfo,Integer rowNum) {
		List<Object> args = new ArrayList<Object>();
		//总的sql语句
		StringBuffer sql = new StringBuffer();
		//客户
		StringBuffer crmSql = new StringBuffer();
		crmSql.append("\n select a.*,b.owner,b.customername modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
		crmSql.append("\n from attention a inner join customer b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_CRM);
		crmSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
		crmSql.append("\n inner join userInfo d on b.owner=d.id");
		crmSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
		crmSql.append("\n where 1=1");
		
		this.addSqlWhere(userInfo.getComId(), crmSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), crmSql, args, " and a.userId=? ");

		//项目
		StringBuffer itemSql = new StringBuffer();
		itemSql.append("\n select a.*,b.owner,b.itemname modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
		itemSql.append("\n from attention a inner join item b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_ITEM);
		itemSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
		itemSql.append("\n inner join userInfo d on b.owner=d.id");
		itemSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
		itemSql.append("\n where 1=1");
		
		this.addSqlWhere(userInfo.getComId(), itemSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), itemSql, args, " and a.userId=? ");

		//任务
		StringBuffer taskSql = new StringBuffer();
		taskSql.append("\n select a.*,b.owner,b.taskname modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
		taskSql.append("\n from attention a inner join task b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_TASK);
		taskSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
		taskSql.append("\n inner join userInfo d on b.owner=d.id");
		taskSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
		taskSql.append("\n where 1=1");
		
		this.addSqlWhere(userInfo.getComId(), taskSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), taskSql, args, " and a.userId=? ");
		//审批
		StringBuffer spSql = new StringBuffer();
		spSql.append("\n select a.*,sp.creator owner,sp.flowName modTitle,sp.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
		spSql.append("\n from attention a inner join spFlowInstance sp on a.busId=sp.id and sp.flowState=1 and sp.flowState<>2 and a.busType="+ConstantInterface.TYPE_FLOW_SP);
		spSql.append("\n inner join userOrganic c on sp.comId=c.comId and sp.creator=c.userId");
		spSql.append("\n inner join userInfo d on sp.creator=d.id");
		spSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
		spSql.append("\n where 1=1");
		
		this.addSqlWhere(userInfo.getComId(), spSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), spSql, args, " and a.userId=? ");
		//问答
		StringBuffer qasSql = new StringBuffer();
		qasSql.append("\n select a.*,b.userId owner,b.title modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
		qasSql.append("\n from attention a inner join question b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_QUES);
		qasSql.append("\n inner join userOrganic c on b.comId=c.comId and b.userId=c.userId");
		qasSql.append("\n inner join userInfo d on b.userId=d.id");
		qasSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
		qasSql.append("\n where 1=1");
		
		this.addSqlWhere(userInfo.getComId(), qasSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), qasSql, args, " and a.userId=? ");
		//投票
		StringBuffer voteSql = new StringBuffer();
		voteSql.append("\n select a.*,b.owner,dbms_lob.substr(b.votecontent,4000) modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
		voteSql.append("\n from attention a inner join vote b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_VOTE);
		voteSql.append("\n inner join userOrganic c on b.comId=c.comId and b.owner=c.userId");
		voteSql.append("\n inner join userInfo d on b.owner=d.id");
		voteSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
		voteSql.append("\n where 1=1");
		
		this.addSqlWhere(userInfo.getComId(), voteSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), voteSql, args, " and a.userId=? ");
		//分享
		StringBuffer shareSql = new StringBuffer();
		shareSql.append("\n select a.*,b.creator owner,dbms_lob.substr(b.content,4000) modTitle,b.recordCreateTime modTime,d.gender,d.userName,e.uuid imgUuid,e.filename imgName");
		shareSql.append("\n from attention a inner join msgshare b on a.busId=b.modid  and a.busType=050");
		shareSql.append("\n inner join userOrganic c on b.comId=c.comId and b.creator=c.userId");
		shareSql.append("\n inner join userInfo d on b.creator=d.id");
		shareSql.append("\n left join upfiles e on c.comId=e.comId and c.smallheadportrait=e.id");
		shareSql.append("\n where 1=1");
		
		this.addSqlWhere(userInfo.getComId(), shareSql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), shareSql, args, " and a.userId=? ");
		
		sql.append("\n select * from (");
		sql.append("\n select a.*,b.content,d.userName modiferName,b.recordCreateTime modifTime,");

		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where a.comId = today.comId and a.busId = today.busId \n");
		sql.append(" and today.busType=a.busType and today.userId='"+userInfo.getId()+"' and today.isclock=0\n");
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 1 else 0 end as isread\n");
		
		sql.append("\n from (");
		sql.append(crmSql);
		sql.append("\n union all ");
		sql.append(itemSql);
		sql.append("\n union all ");
		sql.append(taskSql);
		sql.append("\n union all ");
		sql.append(spSql);
		sql.append("\n union all ");
		sql.append(qasSql);
		sql.append("\n union all ");
		sql.append(voteSql);
		sql.append("\n union all ");
		sql.append(shareSql);
		sql.append("\n ) a ");
		sql.append("\n left join newsInfo b on a.comId=b.comId and a.busId=b.busId and a.busType=b.busType  ");
		sql.append("\n left join userInfo d on b.userId=d.id where 1=1 ");
		
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=? ");
		sql.append("\n order by isread,modifTime desc,a.modTime desc,a.busType desc ");
		
		sql.append(") where rownum <= ?");
		args.add(rowNum);
		return this.listQuery(sql.toString(), args.toArray(), Attention.class);
	}

	/**
	 * 取得模块关注成员
	 * @param comId 企业号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Attention> listAtten(Integer comId, Integer busId,
			String busType) {
		List<Object> args = new ArrayList<Object>();
		//总的sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from attention a");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId , sql, args, " and a.busId=?");
		this.addSqlWhere(busType , sql, args, " and a.busType=?");
		sql.append("\n order by a.id ");
		return this.listQuery(sql.toString(), args.toArray(), Attention.class);
	}
	
	/**
	 * 取得指定人员的关注数 app
	 * @param userId 指定人员
	 * @param comId 企业号
	 * @return
	 */
	public Integer countUserAtten(Integer userId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		//总的sql语句
		StringBuffer sql = new StringBuffer();
		//客户
		StringBuffer crmSql = new StringBuffer();
		crmSql.append("\n select a.id from attention a inner join customer b");
		crmSql.append("\n on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_CRM);
		crmSql.append("\n where 1=1");
		this.addSqlWhere(comId, crmSql, args, " and a.comId=?");
		this.addSqlWhere(userId, crmSql, args, " and a.userId=? ");
		//项目
		StringBuffer itemSql = new StringBuffer();
		itemSql.append("\n select a.id from attention a inner join item b ");
		itemSql.append("\n on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_ITEM);
		itemSql.append("\n where 1=1");
		this.addSqlWhere(comId, itemSql, args, " and a.comId=?");
		this.addSqlWhere(userId, itemSql, args, " and a.userId=? ");
		//任务
		StringBuffer taskSql = new StringBuffer();
		taskSql.append("\n select a.id");
		taskSql.append("\n from attention a inner join task b on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_TASK);
		taskSql.append("\n where 1=1");
		this.addSqlWhere(comId, taskSql, args, " and a.comId=?");
		this.addSqlWhere(userId, taskSql, args, " and a.userId=? ");
		
		//问答
		StringBuffer qasSql = new StringBuffer();
		qasSql.append("\n select a.id from attention a inner join question b");
		qasSql.append("\n  on a.busId=b.id and b.delState=0 and a.busType="+ConstantInterface.TYPE_QUES);
		qasSql.append("\n where 1=1");
		this.addSqlWhere(comId, qasSql, args, " and a.comId=?");
		this.addSqlWhere(userId, qasSql, args, " and a.userId=? ");
		//投票
		StringBuffer voteSql = new StringBuffer();
		voteSql.append("\n select a.id from attention a inner join vote b");
		voteSql.append("\n on a.busId=b.id and b.delState=0 and a.busType=" + ConstantInterface.TYPE_VOTE);
		voteSql.append("\n where 1=1");
		this.addSqlWhere(comId, voteSql, args, " and a.comId=?");
		this.addSqlWhere(userId, voteSql, args, " and a.userId=? ");
		
		//分享
		StringBuffer shareSql = new StringBuffer();
		shareSql.append("\n select a.id from attention a inner join msgshare b");
		shareSql.append("\n  on a.busId=b.modid  and a.busType=" + ConstantInterface.TYPE_DAILY);
		shareSql.append("\n where 1=1");
		this.addSqlWhere(comId, shareSql, args, " and a.comId=?");
		this.addSqlWhere(userId, shareSql, args, " and a.userId=? ");
		
		//产品
		StringBuffer proSql = new StringBuffer();
		proSql.append("\n select a.id from attention a inner join product b ");
		proSql.append("\n on a.busId=b.id and a.busType="+ConstantInterface.TYPE_PRODUCT);
		proSql.append("\n where 1=1");
		this.addSqlWhere(comId, proSql, args, " and a.comId=?");
		this.addSqlWhere(userId, proSql, args, " and a.userId=? ");
		
		
		sql.append("\n select count(*)from (");
		sql.append(crmSql);
		sql.append("\n union all ");
		sql.append(itemSql);
		sql.append("\n union all ");
		sql.append(taskSql);
		sql.append("\n union all ");
		sql.append(qasSql);
		sql.append("\n union all ");
		sql.append(voteSql);
		sql.append("\n union all ");
		sql.append(shareSql);
		sql.append("\n union all ");
		sql.append(proSql);
		sql.append("\n ) a ");
		sql.append("\n where 1=1");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 取得关注具体信息
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @param userInfo当前操作人员
	 * @return
	 */
	public Attention getAtten(String busType, Integer busId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id from attention a where 1=1");
		this.addSqlWhere(busId, sql, args, " and a.busId=?");
		this.addSqlWhere(busType, sql, args, " and a.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=?");
		return (Attention) this.objectQuery(sql.toString(), args.toArray(), Attention.class);
	}

	

}
