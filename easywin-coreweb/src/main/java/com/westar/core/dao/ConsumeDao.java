package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Consume;
import com.westar.base.model.ConsumeType;
import com.westar.base.model.ConsumeUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;

@Repository
public class ConsumeDao extends BaseDao {

	/**
	 * 分页查询消费记录
	 * @param consume
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Consume> listPagedConsume(Consume consume, UserInfo userInfo,String order) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.typeName,c.flowName spFlowName,c.instanceId,  \n");
		sql.append("  case when s.id is null then 0 else 1 end firstData \n");
		sql.append("from consume a \n");
		sql.append("left join consumeType b on a.type = b.id \n");
		sql.append("\n left join (");
		sql.append("\n 	select c.instanceId,c.busId,s.flowName from spFlowRelateData c ");
		sql.append("\n left join spFlowInstance s on s.id = c.instanceId  where c.comId=? and  c.busType=? and s.spState = 1");
		args.add(userInfo.getComId());
		args.add(ConstantInterface.TYPE_CONSUME);
		sql.append("\n 	group by c.instanceId,c.busId,s.flowName ");
		sql.append("\n )c on a.id=c.busId ");
		//判断是否为该日期的第一条数据
		sql.append("\n LEFT JOIN (SELECT * from (SELECT aa.*,rownum ro from (SELECT STARTDATE ,id,status from consume  where comid = ? and creator=? GROUP BY STARTDATE,id,status order by STARTDATE desc,status,id desc)aa ) ");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n    where  ro in (select min(rownum) from (SELECT STARTDATE ,id,status from consume  where comid = ? and creator=? GROUP BY STARTDATE,id,status order by STARTDATE desc,status,id desc）a GROUP BY a.STARTDATE)");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n    ) s on s.id = a.id ");
		sql.append("\n where a.comid=? and a.creator=? ");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		
		//费用类型多选
		List<ConsumeType> consumeTypes = consume.getListConsumeType();
	    if(null!=consumeTypes && !consumeTypes.isEmpty()){
	    	List<Integer> listConsumeTypeId = new ArrayList<Integer>();
	    	for (ConsumeType consumeType : consumeTypes) {
	    		listConsumeTypeId.add(consumeType.getId());
			}
	    	this.addSqlWhereIn(listConsumeTypeId.toArray() , sql, args, "\n and a.type in ?");
		}
	    this.addSqlWhere(consume.getStatus(), sql, args, " and a.status=?");
	    //查询创建创建时间段
  		this.addSqlWhere(consume.getCreateStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
  		this.addSqlWhere(consume.getCreateEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
  		//消费时间查询
  		if(consume.getConsumeStartDate() != null && !consume.getConsumeStartDate().equals("")) {
  			sql.append(" and (substr(a.startDate,0,10)>=? or substr(a.endDate,0,10)>=?)");
  			args.add(consume.getConsumeStartDate());
  			args.add(consume.getConsumeStartDate());
  		}
  		if(consume.getConsumeEndDate() != null && !consume.getConsumeEndDate().equals("")) {
  			sql.append(" and (substr(a.startDate,0,10)<=? or substr(a.endDate,0,10)<=?)");
  			args.add(consume.getConsumeEndDate());
  			args.add(consume.getConsumeEndDate());
  		}
		if(!CommonUtil.isNull(consume.getDescribe())) {
			//模糊查询
	  		this.addSqlWhereLike(consume.getDescribe(), sql, args, "and (a.describe like ? or a.leavePlace like ? or a.arrivePlace like ?) ",3);
		}
		
		//按消费日期开始时间排序
		if(!CommonUtil.isNull(order) && "startDate".equals(order)) {
			sql.append("\n order by a.startDate desc,a.status,a.id desc ");
		}else {
			sql.append("\n order by a.status,a.id desc ");
		}
		return this.pagedQuery(sql.toString(),null, args.toArray(), Consume.class);
	}
	
	/**
	 * 查询费用类型
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConsumeType> listConsumeType(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from consumeType a where a.comid =?");
		args.add(comId);
		sql.append("\n  order by a.typeorder asc ");
		return this.listQuery(sql.toString(), args.toArray(),ConsumeType.class);
	}
	
	/**
	 * 查询费用类型排序号
	 * @param comId
	 * @return
	 */
	public ConsumeType initConsumeTypeOrder(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.typeorder)+1  as typeorder from consumeType a where a.comid =?");
		args.add(comId);
		return (ConsumeType)this.objectQuery(sql.toString(), args.toArray(),ConsumeType.class);
	
	}
	
	/**
	 * 分页查询发票
	 * @param comId
	 * @param consumeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConsumeUpfile> listPagedConsumeUpfile(Integer comId, Integer consumeId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( \n");
		//只是消费记录发票
		sql.append("select a.consumeId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id,'033' type,a.consumeId busId\n");
		sql.append(" from consumeUpfile a \n");
		sql.append("inner join consume con on a.consumeId=con.id and a.comid=con.comid \n");
		sql.append("inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(consumeId, sql, args, " and a.consumeId=?");
		sql.append(") a \n");
		return this.pagedQuery(sql.toString(), "a.userId,a.fileExt,a.recordcreatetime desc", args.toArray(), ConsumeUpfile.class);
	
	}
	/**
	 * 查询记账关联关联的所有数据信息
	 * @param comId 团队号
	 * @param consumeId 记账主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConsumeUpfile> listConsumeUpfile(Integer comId, Integer consumeId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,upfiles.uuid,upfiles.filename,upfiles.fileExt\n");
		//只是消费记录发票
		sql.append(" from consumeUpfile a \n");
		sql.append(" inner join upfiles on a.comId=a.comId and a.upfileId=upfiles.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(consumeId, sql, args, " and a.consumeId=?");
		sql.append("order by a.id \n");
		return this.listQuery(sql.toString(),args.toArray(), ConsumeUpfile.class);
		
	}
	
	
	/**
	 * 查询发票总数
	 * @param comId
	 * @param consumeId
	 * @return
	 */
	public Integer countFile(Integer comId, Integer consumeId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from ( \n");
		//只是消费记录发票
		sql.append("select a.consumeId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName from consumeUpfile a \n");
		sql.append("inner join consume con on a.consumeId=con.id and a.comid=con.comid \n");
		sql.append("inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comid = cc.comid \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(consumeId, sql, args, " and a.consumeId=?");
		sql.append(") a \n");
		return this.countQuery(sql.toString() ,args.toArray());
	}

	/**
	 * 分页查询记账功能用于选择
	 * @param userInfo
	 * @param consume
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Consume> listPagedConsumeForSelect(UserInfo userInfo,Consume consume) {
		//需要查询的字段
		List<String> columns = CommonUtil.getSearchColumn("Consume");
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//取得可选数据信息
		Integer instanceId = consume.getInstanceId();
				
		sql.append("\n select a.id,a.recordCreateTime,b.typeName ");
		if(!CommonUtil.isNull(columns)){
			for (String column : columns) {
				sql.append(",a."+column);
			}
		}
		sql.append("\n from Consume a ");
		sql.append("\n inner join consumeType b on a.type=b.id and a.comId=b.comId  ");
		sql.append("\n left join (");
		sql.append("\n 	select c.instanceId,c.busId from spFlowRelateData c where c.comId=? and  c.busType=? ");
		args.add(userInfo.getComId());
		args.add(ConstantInterface.TYPE_CONSUME);
		if(null!=instanceId && instanceId>0){//取得未报销和本次流程正在报销的
			sql.append("\n and  c.instanceId=? ");
			args.add(instanceId);
			
		}
		sql.append("\n 	group by c.instanceId,c.busId ");
		sql.append("\n )c on a.id=c.busId ");
		sql.append("\n where 1=1 and a.creator=? and a.comId=? ");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		
		
		if(null!=instanceId && instanceId>0){//取得未报销和本次流程正在报销的
			sql.append("\n and (a.status=0  or (a.status=1 and c.instanceId=?) )");
			args.add(instanceId);
			
		}else{
			sql.append("\n and a.status=0 ");
		}
		
		
		//费用类型多选
		List<ConsumeType> consumeTypes = consume.getListConsumeType();
	    if(null!=consumeTypes && !consumeTypes.isEmpty()){
	    	List<Integer> listConsumeTypeId = new ArrayList<Integer>();
	    	for (ConsumeType consumeType : consumeTypes) {
	    		Integer typeId = consumeType.getId();
	    		if(null!=typeId && typeId>0){
	    			listConsumeTypeId.add(consumeType.getId());
	    		}
			}
	    	this.addSqlWhereIn(listConsumeTypeId.toArray() , sql, args, "\n and a.type in ?");
		}
	    
	   //消费时间查询
  		if(consume.getConsumeStartDate() != null && !consume.getConsumeStartDate().equals("")) {
  			sql.append(" and (substr(a.startDate,0,10)>=? or substr(a.endDate,0,10)>=?)");
  			args.add(consume.getConsumeStartDate());
  			args.add(consume.getConsumeStartDate());
  		}
  		if(consume.getConsumeEndDate() != null && !consume.getConsumeEndDate().equals("")) {
  			sql.append(" and (substr(a.startDate,0,10)<=? or substr(a.endDate,0,10)<=?)");
  			args.add(consume.getConsumeEndDate());
  			args.add(consume.getConsumeEndDate());
  		}
  		//模糊查询
  		this.addSqlWhereLike(consume.getDescribe(), sql, args, "and (a.describe like ? or a.leavePlace like ? or a.arrivePlace like ?) ",3);
  		
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(), Consume.class);
	}
	
	/**
	 * 根据id获取消费记录详情
	 * @param id
	 * @return
	 */
	public Consume getConsumeById(Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.typeName,c.flowName spFlowName,c.instanceId,fo.payType,fo.balanceTime,u.userName balanceUserName  \n");
		sql.append("from consume a \n");
		sql.append("left join consumeType b on a.type = b.id \n");
		sql.append("\n left join (");
		sql.append("\n 	select c.instanceId,c.busId,s.flowName from spFlowRelateData c ");
		sql.append("\n left join spFlowInstance s on s.id = c.instanceId where c.busType=? and s.spState = 1 ");
		args.add(ConstantInterface.TYPE_CONSUME);
		sql.append("\n 	group by c.instanceId,c.busId,s.flowName ");
		sql.append("\n )c on a.id=c.busId ");
		sql.append(" left join feeLoanOff fo on fo.instanceId = c.instanceId   \n");
		sql.append(" left join userInfo u on u.id = fo.balanceUserId \n");
		sql.append("where a.id=?\n");
		args.add(id);
		return (Consume) this.objectQuery(sql.toString(), args.toArray(), Consume.class);
	}
	
	/**
	 * 通过费用类型id查询消费记录表是否使用改费用类型
	 * @param id
	 * @return
	 */
	public Integer countConsumeTypeById(Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from consume where type = ?  \n");
		args.add(id);
		return  this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 查询待报销的消费记录总数
	 * @param curUser
	 * @return
	 */
	public Integer countUrConsume(UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from consume where comid = ? and creator = ? and status = 0 \n");
		args.add(curUser.getComId());
		args.add(curUser.getId());
		return  this.countQuery(sql.toString(), args.toArray());
	}
	

}
