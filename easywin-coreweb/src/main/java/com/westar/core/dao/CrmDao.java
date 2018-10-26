package com.westar.core.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Area;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.Customer;
import com.westar.base.model.CustomerLog;
import com.westar.base.model.CustomerRead;
import com.westar.base.model.CustomerSharer;
import com.westar.base.model.CustomerStage;
import com.westar.base.model.CustomerType;
import com.westar.base.model.CustomerUpfile;
import com.westar.base.model.FeedBackInfo;
import com.westar.base.model.FeedBackType;
import com.westar.base.model.FeedInfoFile;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.LinkMan;
import com.westar.base.model.Region;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.ModStaticVo;
import com.westar.base.pojo.StatisticCrmVo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;

@Repository
public class CrmDao extends BaseDao{


	/**
	 * 初始化区域排序值
	 * @param comId
	 * @param parentId
	 * @return
	 */
	public Integer initAreaOrder(Integer comId,Integer parentId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select case when max(a.areaOrder)is null then 1 else max(a.areaOrder)+1 end as areaOrder from area a where a.comId =? and a.parentId=?");
		args.add(comId);
		args.add(parentId);
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 获取区域集合
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Area> listArea(Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.areaname,a.regionId,\n");
		sql.append("a.parentid,connect_by_isleaf as isLeaf,level \n");
		sql.append("from area a left join region b on a.regionId=b.id\n");
		sql.append("where a.comId = ? \n");
		args.add(comId);
		sql.append("start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid\n");
		sql.append("order siblings by regioncode\n");
		return this.listQuery(sql.toString(), args.toArray(),Area.class);
	}
	/**
	 * 获取区域集合(app)
	 * @param comId 企业号
	 * @param areaPid 区域主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Area> listCrmAreaForRelevance(Integer comId,Integer areaPid){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from ( \n");
		sql.append("select a.id,a.recordcreatetime,a.comId,a.areaname,a.areaorder,a.parentid,a.creator ,connect_by_isleaf as isLeaf, \n");
		sql.append("a.allSpelling,a.firstSpelling from area a \n");
		sql.append("where a.comId = ? and  LEVEL=1\n");
		args.add(comId);
		sql.append("start with parentid="+areaPid+" CONNECT BY PRIOR id = parentid\n");
		sql.append("order siblings by a.areaorder desc,a.id \n");
		sql.append(") a where 1=1 \n");
		return this.pagedQuery(sql.toString(), " a.areaorder desc,a.id", args.toArray(), Area.class);
	}
	/**
	 * 更新区域树形数据结构
	 * @param nodeId
	 * @param pId
	 * @param comId
	 */
	public void zTreeOnDrop(Integer nodeId,Integer pId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update area a set a.parentid=? where a.comId=? and a.id=?");
		args.add(pId);
		args.add(comId);
		args.add(nodeId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 获取区域节点子集合
	 * @param comId
	 * @param pId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Area> areaChildren(Integer comId,Integer pId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.recordcreatetime,a.comId,a.areaname,a.areaorder,a.parentid,a.creator, \n");
		sql.append("sum(case when b.id is not null then 1 else 0 end) as childSum  \n");
		sql.append("from area a left join area b on a.comId = b.comId and a.id = b.parentid \n");
		sql.append("where a.comId=? start with a.parentid=? \n");
		args.add(comId);
		args.add(pId);
		sql.append("connect by prior a.id = a.parentid \n");
		sql.append("group by a.id,a.recordcreatetime,a.comId,a.areaname,a.areaorder,a.parentid,a.creator \n");
		return this.listQuery(sql.toString(), args.toArray(), Area.class);
	}
//	/**
//	 * 根据主键查询区域节点信息
//	 * @param comId
//	 * @param id
//	 * @return
//	 */
//	public Area areaQuery(Integer comId,Integer id){
//		List<Object> args = new ArrayList<Object>();
//		StringBuffer sql = new StringBuffer();
//		sql.append("select a.id,a.recordcreatetime,a.comId,a.areaname,a.areaorder,a.parentid,a.creator, \n");
//		sql.append("sum(case when b.id is not null then 1 else 0 end) as childSum  \n");
//		sql.append("from area a left join area b on a.comId = b.comId and a.id= b.parentid  \n");
//		sql.append("where a.comId=? and a.id=? \n");
//		args.add(comId);
//		args.add(id);
//		sql.append("group by a.id,a.recordcreatetime,a.comId,a.areaname,a.areaorder,a.parentid,a.creator \n");
//		return (Area)this.objectQuery(sql.toString(), args.toArray(), Area.class);
//	}
	/**
	 * 删除客户区域
	 * @param id 区域主键
	 * @param comId 企业编号
	 */
	public void delArea(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from area a where a.comId =? and a.id in \n");
		sql.append("(select id from area start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	
	/**
	 * 区域节点所属节点向上调一级
	 * @param oldParentId
	 * @param newParentId
	 * @param comId
	 */
	public void areaParentUpdate(Integer comId,Integer newParentId,Integer oldParentId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update area a set a.parentid=? where a.comId=? and a.parentid=?");
		args.add(newParentId);
		args.add(comId);
		args.add(oldParentId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 获取客户类型集合
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerType> listCustomerType(Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from customerType a where a.comId =?");
		args.add(comId);
		sql.append("\n  order by a.typeorder asc ");
		return this.listQuery(sql.toString(), args.toArray(),CustomerType.class);
	}
	/**
	 * 初始化客户类型排序值
	 * @param comId
	 * @return
	 */
	public CustomerType initCustomerTypeOrder(Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.typeorder)+1  as typeorder from customerType a where a.comId =?");
		args.add(comId);
		return (CustomerType)this.objectQuery(sql.toString(), args.toArray(),CustomerType.class);
	}
	/**
	 * 查询客户列表导出数据
	 * @param operator
	 * @param isForceIn
	 * @return
	 */
	public List<Customer> listCustomerForExport(Integer operator, boolean isForceIn,Customer customer) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//更新频率开始时间
		String frequenStartDate = customer.getFrequenStartDate();
		//更新频率结束时间
		String frequenEndDate = customer.getFrequenEndDate(); 
		
		boolean isSearchFreque = false;
		if((null!= frequenStartDate && !"".equals(frequenStartDate))||
				(null!= frequenEndDate && !"".equals(frequenEndDate))){//只要有一个满足就需要查询
			isSearchFreque = true;
		}
		
		//外面再包一层
		sql.append("select * from ( \n");
		sql.append("	select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,a.stage,a.budget,cs.stageName,\n");
		sql.append("	customerType.typeOrder,newsInfo.content as modifyContent,modifier.username as modifierName,modifier.gender modifierGender,modifier.id as modifier, \n");
		sql.append("	case when atten.id is null then 0 else 1 end as attentionState,\n");
		
		//是否有待办事项
		sql.append("    case when today.readstate=0 then 0 else 1 end as isread, \n");
		
		sql.append("	c.username as ownerName,c.gender,d.uuid,d.filename,modifierFile.uuid modifierUuid,modifierFile.filename modifierFileName,area.areaName,customerType.typeName,\n");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("	case when newsInfo.recordcreatetime is null then a.recordcreatetime else newsInfo.recordcreatetime end as modifyTime  \n");
		if(isSearchFreque){
			sql.append("	from ( \n");
			sql.append("	select a.*,row_number() over (partition by b.customerId order by b.recordcreatetime desc) as new_order, \n");
			sql.append("	case when b.recordcreatetime is null then a.recordcreatetime else b.recordcreatetime end frequetime from customer a \n");
			sql.append("\n  left join ( ");
			//最近更新时间
			sql.append("\n	select b.id,b.recordcreatetime,b.busid customerId,b.comId from  busUpdate b ");
			sql.append("\n	inner join customer a on a.id=b.busid and a.comId=b.comId ");
			sql.append("\n where a.delstate=0 and a.comId=?  and b.bustype = '"+ConstantInterface.TYPE_CRM+"'");
			args.add(customer.getComId());
			sql.append("\n		  )b on a.id=b.customerId and a.comId=b.comId ");
			sql.append("\n where a.delstate=0 and a.comId=?");
			args.add(customer.getComId());
			sql.append("\n )a \n");
		}else{
			sql.append("	from customer a \n");
		}
		sql.append("	left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("	left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");
		
		sql.append(" 	left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" 		and a.id = newsInfo.busId and newsInfo.busType="+ConstantInterface.TYPE_CRM+"\n");
		sql.append(" 	left join userinfo modifier on newsInfo.Userid = modifier.id  \n");
		sql.append(" 	left join userOrganic modifierOrg on modifier.id = modifierOrg.userId and a.comId = modifierOrg.comId \n");
		sql.append(" 	left join upfiles modifierFile on modifierOrg.mediumHeadPortrait = modifierFile.id \n");
		
		sql.append(" 	inner join userinfo c on a.owner = c.id \n");
		sql.append(" 	inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append(" 	left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append(" 	left join customerStage cs on cs.id = a.stage and cs.comId = a.comId \n");
		
		sql.append("	left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=? \n");
		args.add(operator);
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_CRM+"' and today.userId=?  \n ");
		args.add(operator);
		if(isSearchFreque){
			sql.append("	where a.new_order=1 and a.delstate=0 and  a.comId = ? \n");
			args.add(customer.getComId());
			//更新频率时间选择
			this.addSqlWhere(customer.getFrequenStartDate(), sql, args, " and a.frequetime>?");
			this.addSqlWhere(customer.getFrequenEndDate(), sql, args, " and a.frequetime<=?");
		}else{
			sql.append("	where a.delstate=0 and  a.comId = ? \n");
			args.add(customer.getComId());
		}
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//查询资金段
		this.addSqlWhere(customer.getMinBudget(), sql, args, " and a.budget>=?");
		this.addSqlWhere(customer.getMaxBudget(), sql, args, " and a.budget<=?");
		//客户类型筛选
	    if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
		  this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
	    }
	    //客户类型多选
	    if(null != customer.getListCrmType() && !customer.getListCrmType().isEmpty()){
			sql.append("	 and  ( a.customerTypeId= 0 ");
			for(CustomerType crmType : customer.getListCrmType()){
				sql.append("or a.customerTypeId=?  \n");
				args.add(crmType.getId());
			}
			sql.append("	 ) ");
		}
	  //客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
	    if(null != customer.getListOwner() && !customer.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 0 ");
			for(UserInfo owner : customer.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		//列表查看权限验证
		if(!isForceIn){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(operator);
			//上级权限验证//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(operator);
			args.add(customer.getComId());
			args.add(operator);
			
			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(operator);
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(customer.getComId());
			args.add(operator);
			sql.append(") \n");
		}
		sql.append(")a where 1=1 \n");
		
		
		//外层结束
		
		
		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		//客户阶段筛选
		if(null!=customer.getStage() && !"".equals(customer.getStage())){
			this.addSqlWhere(customer.getStage(), sql, args, " and a.stage =  ? \n");
		}
		//如果是查看未读列表，则建立表间关系
		if(null!=customer.getIsRead() && 0==customer.getIsRead()){
			sql.append("and a.isread=0 \n");
		}
		//负责人类型
		String ownerType =  customer.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){
				//查询自己的
				this.addSqlWhere(operator, sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){
				//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(customer.getComId());
				args.add(operator);
			}
		}
		this.addSqlWhere(customer.getAttentionState(), sql, args, " and a.attentionState=?");

		//不查询页面已有数据
		String crmIds = customer.getCrmIds();
		if(null!=crmIds && !"".equals(crmIds)){
			sql.append("\n and a.id not in ("+crmIds+") ");
		}
		sql.append(" order by a.typeOrder,a.isread,a.modifyTime desc,a.id desc");
		return this.listQuery(sql.toString(), args.toArray(),Customer.class);
	}
	/**
	 * 获取个人所能见客户集合
	 * @param customer
	 * @param operator 当前操作人
	 * @param isForceInPersion 是否是督察人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listCustomerForPage(Customer customer,Integer operator,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		//更新频率开始时间
		String frequenStartDate = customer.getFrequenStartDate();
		//更新频率结束时间
		String frequenEndDate = customer.getFrequenEndDate();

		boolean isSearchFreque = false;
		if((null!= frequenStartDate && !"".equals(frequenStartDate))||
				(null!= frequenEndDate && !"".equals(frequenEndDate))){//只要有一个满足就需要查询
			isSearchFreque = true;
		}

		//外面再包一层
		sql.append("select * from ( \n");
		sql.append("	select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,a.stage,a.budget,cs.stageName,\n");
		sql.append("	customerType.typeOrder,newsInfo.content as modifyContent,modifier.username as modifierName,modifier.gender modifierGender,modifier.id as modifier, \n");
		sql.append("	case when atten.id is null then 0 else 1 end as attentionState,\n");

		//是否有待办事项
		sql.append("   case when today.readstate=0 then 0 else 1 end as isread, \n");

		sql.append("	c.username as ownerName,c.gender,d.uuid,d.filename,modifierFile.uuid modifierUuid,modifierFile.filename modifierFileName,area.areaName,customerType.typeName,\n");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("	case when newsInfo.recordcreatetime is null then a.recordcreatetime else newsInfo.recordcreatetime end as modifyTime  \n");
		if(isSearchFreque){
			sql.append("	from ( \n");
			sql.append("	select a.*,row_number() over (partition by b.customerId order by b.recordcreatetime desc) as new_order, \n");
			sql.append("	case when b.recordcreatetime is null then a.recordcreatetime else b.recordcreatetime end frequetime from customer a \n");
			sql.append("\n  left join ( ");
			//最近更新时间
			sql.append("\n	select b.id,b.recordcreatetime,b.busid customerId,b.comId from  busUpdate b ");
			sql.append("\n	inner join customer a on a.id=b.busid and a.comId=b.comId ");
			sql.append("\n where a.delstate=0 and a.comId=?  and b.bustype = '"+ConstantInterface.TYPE_CRM+"'");
			args.add(customer.getComId());
			sql.append("\n		  )b on a.id=b.customerId and a.comId=b.comId ");
			sql.append("\n where a.delstate=0 and a.comId=?");
			args.add(customer.getComId());
			sql.append("\n )a \n");
		}else{
			sql.append("	from customer a \n");
		}
		sql.append("	left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("	left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");

		sql.append(" 	left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" 		and a.id = newsInfo.busId and newsInfo.busType="+ConstantInterface.TYPE_CRM+"\n");
		sql.append(" 	left join userinfo modifier on newsInfo.Userid = modifier.id  \n");
		sql.append(" 	left join userOrganic modifierOrg on modifier.id = modifierOrg.userId and a.comId = modifierOrg.comId \n");
		sql.append(" 	left join upfiles modifierFile on modifierOrg.mediumHeadPortrait = modifierFile.id \n");

		sql.append(" 	inner join userinfo c on a.owner = c.id \n");
		sql.append(" 	inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append(" 	left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append(" 	left join customerStage cs on cs.id = a.stage and cs.comId = a.comId \n");

		sql.append("	left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=? \n");
		args.add(operator);
		//操作人员的待办事项或是未读提醒
		sql.append("\n left join todayworks today on a.comId = today.comId and c.id = today.busid ");
		sql.append("\n and today.bustype='"+ConstantInterface.TYPE_CRM+"' and today.userId=? ");
		args.add(operator);
		if(isSearchFreque){
			sql.append("	where a.new_order=1 and a.delstate=0 and  a.comId = ? \n");
			args.add(customer.getComId());
			//更新频率时间选择
			this.addSqlWhere(customer.getFrequenStartDate(), sql, args, " and a.frequetime>?");
			this.addSqlWhere(customer.getFrequenEndDate(), sql, args, " and a.frequetime<=?");
		}else{
			sql.append("	where a.delstate=0 and  a.comId = ? \n");
			args.add(customer.getComId());
		}


		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//查询资金段
		this.addSqlWhere(customer.getMinBudget(), sql, args, " and a.budget>=?");
		this.addSqlWhere(customer.getMaxBudget(), sql, args, " and a.budget<=?");
		//客户类型筛选
	    if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
		  this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
	    }
	    //客户类型多选
	    if(null != customer.getListCrmType() && !customer.getListCrmType().isEmpty()){
			sql.append("	 and  ( a.customerTypeId= 0 ");
			for(CustomerType crmType : customer.getListCrmType()){
				sql.append("or a.customerTypeId=?  \n");
				args.add(crmType.getId());
			}
			sql.append("	 ) ");
		}
	  //客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
	    if(null != customer.getListOwner() && !customer.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 0 ");
			for(UserInfo owner : customer.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//查询公开客户开始
			sql.append("a.pubState = 1 or \n");
			//任务的参与、执行、负责权限验证
			sql.append(" 	(a.pubState = 0 and (a.owner =? \n");
			args.add(operator);
			//上级权限验证
			//参与人上级权限验证
			sql.append(" 		or exists( \n");
			sql.append("  			select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  			(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" 		) \n");
			args.add(operator);
			args.add(customer.getComId());
			args.add(operator);

			//负责人上级权限验证
			sql.append(" 		or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(operator);
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" 		or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(customer.getComId());
			args.add(operator);
			sql.append("		) \n");
			sql.append("	) \n");
			sql.append(") \n");
			//查询公开客户结束
		}
		sql.append(")a where 1=1 \n");


		//外层结束


		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		//客户阶段筛选
		if(null!=customer.getStage() && !"".equals(customer.getStage())){
			this.addSqlWhere(customer.getStage(), sql, args, " and a.stage =  ? \n");
		}
		//如果是查看未读列表，则建立表间关系
		if(null!=customer.getIsRead() && 0==customer.getIsRead()){
			sql.append("and a.isread=0 \n");
		}
		//负责人类型
		String ownerType =  customer.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){//查询自己的
				this.addSqlWhere(operator, sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(customer.getComId());
				args.add(operator);
			}
		}
		this.addSqlWhere(customer.getAttentionState(), sql, args, " and a.attentionState=?");

		//不查询页面已有数据
		String crmIds = customer.getCrmIds();
		if(null!=crmIds && !"".equals(crmIds)){
			sql.append("\n and a.id not in ("+crmIds+") ");
		}
		String order = "";
		if(null != customer.getOrderBy() && "customerType".equals(customer.getOrderBy())){
			order = "a.typeOrder,a.isread,a.modifyTime desc,a.id desc";
		}else{
			order  = "a.isread,a.modifyTime desc,a.id desc";
		}
		return this.pagedQuery(sql.toString(),order, args.toArray(),Customer.class);
	}
	/**
	 * 获取个人所能见客户集合
	 * @param customer
	 * @param userInfo 当前操作人
	 * @param isForceInPersion 是否是督察人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listCrmForRelevance(Customer customer,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//外面再包一层
		sql.append("select * from ( \n");
		sql.append("	select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.customerTypeId,\n");
		sql.append("	c.username as ownerName,c.gender,d.uuid,d.filename,area.areaName,customerType.typeName,\n");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("	case when newsInfo.recordcreatetime is null then a.recordcreatetime else newsInfo.recordcreatetime end as modifyTime  \n");

		sql.append("	from customer a \n");
		sql.append("	left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("	left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");
		sql.append(" 	left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" 		and a.id = newsInfo.busId and newsInfo.busType="+ConstantInterface.TYPE_CRM+"\n");
		sql.append(" 	inner join userinfo c on a.owner = c.id \n");
		sql.append(" 	inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append(" 	left join upfiles d on cc.mediumHeadPortrait = d.id \n");

		sql.append("	where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and ((\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
			//查询公开客户开始
			sql.append("or a.id in(SELECT id from CUSTOMER where delstate=0 and  comId = ? and pubState = 1) \n");
			args.add(customer.getComId());
			sql.append(") \n");
			//查询公开客户结束
		}
		sql.append(")a where 1=1 \n");
		//客户类型筛选
		if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
			this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
		}
		//客户区域筛选
		if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
			sql.append("and a.areaid in ( select id from area \n");
			sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
			sql.append(")\n");
		}
		//客户负责人筛选
		if(null!=customer.getOwner() && customer.getOwner() !=0){
			this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
		}
		//客户类型筛选
		if(null!=customer.getCustomerTypeId() && customer.getCustomerTypeId() !=0){
			this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customerTypeId=? \n");
		}
		//外层结束


		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		//不查询页面已有数据
		String crmIds = customer.getCrmIds();
		if(null!=crmIds && !"".equals(crmIds)){
			sql.append("\n and a.id not in ("+crmIds+") ");
		}
		return this.pagedQuery(sql.toString(), "a.modifyTime desc,a.id desc", args.toArray(),Customer.class);
	}
	/***
	 * 获取个人权限下的所有客户（不分页）
	 * @param customer
	 * @param operator
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listCustomerOfAll(Customer customer,Integer operator,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//外面再包一层
		sql.append("select * from ( \n");
		sql.append("select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,\n");
		sql.append("newsInfo.content as modifyContent,modifier.username as modifierName,modifier.id as modifier, \n");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as isread, \n");

		sql.append("c.username as ownerName,c.gender,d.uuid,d.filename,modifierFile.uuid modifierUuid,modifierFile.filename modifierFileName,area.areaName,customerType.typeName,\n");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("case when newsInfo.recordcreatetime is null then a.recordcreatetime else newsInfo.recordcreatetime end as modifyTime \n");
		sql.append("from customer a \n");
		//外层结束
		sql.append("left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");

		//更新人信息
		sql.append(" left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" and a.id = newsInfo.busId and newsInfo.busType="+ConstantInterface.TYPE_CRM+"\n");
		sql.append(" left join userinfo modifier on newsInfo.Userid = modifier.id  \n");
		sql.append(" left join userOrganic modifierOrg on modifier.id = modifierOrg.userId and a.comId = modifierOrg.comId \n");
		sql.append(" left join upfiles modifierFile on modifierOrg.mediumHeadPortrait = modifierFile.id \n");
		//负责人信息
		sql.append(" inner join userinfo c on a.owner = c.id \n");
		sql.append(" inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append(" left join upfiles d on cc.mediumHeadPortrait = d.id \n");

		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=? \n");
		args.add(operator);
		//操作人员的待办事项或是未读提醒
		sql.append("\n left join todayworks today on a.comId = today.comId and a.id = today.busid ");
		sql.append("\n and today.bustype='"+ConstantInterface.TYPE_CRM+"' and today.userId=? ");
		args.add(operator);
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(customer.getComId());
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and ((\n");
			//任务的参与、执行、负责权限验证
			sql.append("a.owner =? \n");
			args.add(operator);
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(operator);
			args.add(customer.getComId());
			args.add(operator);

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(operator);
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(customer.getComId());
			args.add(operator);
			sql.append(") \n");
			//查询公开客户开始
			sql.append("or a.id in(SELECT id from CUSTOMER where delstate=0 and  comId = ? and pubState = 1) \n");
			args.add(customer.getComId());
			sql.append(") \n");
			//查询公开客户结束
		}
		//客户类型筛选
	    if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
		  this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
	    }
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }


		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		sql.append(")a where 1=1 \n");
		//如果是查看未读列表，则建立表间关系
		if(null!=customer.getIsRead() && 0==customer.getIsRead()){
			sql.append("and a.isread=0 \n");
		}
		//负责人类型
		String ownerType =  customer.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){
				//查询自己的
				this.addSqlWhere(operator, sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){
				//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(customer.getComId());
				args.add(operator);
			}
		}
		sql.append("order by a.isread,a.modifyTime desc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Customer.class);
	}
	/**
	 * 获取个人权限下的所有客户月增长集合
	 * @param customer
	 * @param operator
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listCustomerAddByMonthOfAll(Customer customer,Integer operator,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//外面再包一层
		sql.append("select * from ( \n");
		sql.append("select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,\n");
		sql.append("newsInfo.content as modifyContent,modifier.username as modifierName,  \n");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,\n");

		//是否有待办事项
		sql.append("case when today.readstate=0 then 0 else 1 end as isread, \n");

		sql.append("c.username as ownerName,c.gender,d.uuid,d.filename,area.areaName,customerType.typeName,\n");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("case when newsInfo.recordcreatetime is null then a.recordcreatetime else newsInfo.recordcreatetime end as modifyTime\n");
		sql.append("from customer a \n");
		//外层结束
		sql.append("left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");

		sql.append(" left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" and a.id = newsInfo.busId and newsInfo.busType="+ConstantInterface.TYPE_CRM+"\n");
		sql.append(" left join userinfo modifier on newsInfo.Userid = modifier.id  \n");

		sql.append(" inner join userinfo c on a.owner = c.id \n");
		sql.append(" inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append(" left join upfiles d on cc.mediumHeadPortrait = d.id \n");

		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=? \n");
		args.add(operator);
		//操作人员的待办事项或是未读提醒
		sql.append("\n left join todayworks today on a.comId = today.comId and a.id = today.busid ");
		sql.append("\n and today.bustype='"+ConstantInterface.TYPE_CRM+"' and today.userId=? ");
		args.add(operator);
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(customer.getComId());
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(operator);
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(operator);
			args.add(customer.getComId());
			args.add(operator);

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(operator);
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(customer.getComId());
			args.add(operator);
			sql.append(") \n");
		}
		//客户类型筛选
	    if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
		  this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
	    }
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }

		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		sql.append(")a where 1=1 \n");
		//如果是查看未读列表，则建立表间关系
		if(null!=customer.getIsRead() && 0==customer.getIsRead()){
			sql.append("and a.isread=0 \n");
		}
		//筛选本月添加的
		sql.append("and to_date(substr(a.recordcreatetime,1,10),'yyyy-mm-dd') >= trunc(sysdate,'mm') \n");
		//负责人类型
		String ownerType =  customer.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){
				//查询自己的
				this.addSqlWhere(operator, sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){
				//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(customer.getComId());
				args.add(operator);
			}
		}
		sql.append("order by a.isread,a.modifyTime desc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Customer.class);
	}
	/**
	 * 分页获取个人权限下的所有客户月增长集合
	 * @param customer
	 * @param operator
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listCustomerAddByMonthForPage(Customer customer,Integer operator,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//外面再包一层
		sql.append("select * from ( \n");
		sql.append("select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,\n");
		sql.append("newsInfo.content as modifyContent,modifier.username as modifierName,  \n");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as isread, \n");

		sql.append("c.username as ownerName,c.gender,d.uuid,d.filename,area.areaName,customerType.typeName,\n");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("case when newsInfo.recordcreatetime is null then a.recordcreatetime else newsInfo.recordcreatetime end as modifyTime \n");
		sql.append("from customer a \n");
		//外层结束
		sql.append("left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");

		sql.append(" left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" and a.id = newsInfo.busId and newsInfo.busType="+ConstantInterface.TYPE_CRM+"\n");
		sql.append(" left join userinfo modifier on newsInfo.Userid = modifier.id  \n");

		sql.append(" inner join userinfo c on a.owner = c.id \n");
		sql.append(" inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append(" left join upfiles d on cc.mediumHeadPortrait = d.id \n");

		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=? \n");
		args.add(operator);
		//操作人员的待办事项或是未读提醒
		sql.append("\n left join todayworks today on a.comId = today.comId and a.id = today.busid ");
		sql.append("\n and today.bustype='"+ConstantInterface.TYPE_CRM+"' and today.userId=? ");
		args.add(operator);
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(customer.getComId());
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(operator);
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(operator);
			args.add(customer.getComId());
			args.add(operator);

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(operator);
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(customer.getComId());
			args.add(operator);
			sql.append(") \n");
		}
		//客户类型筛选
	    if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
		  this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
	    }
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		sql.append(")a where 1=1 \n");
		//如果是查看未读列表，则建立表间关系
		if(null!=customer.getIsRead() && 0==customer.getIsRead()){
			sql.append("and a.isread=0 \n");
		}
		//筛选本月添加的
		//sql.append("and to_date(substr(a.recordcreatetime,1,10),'yyyy-mm-dd') >= trunc(sysdate,'mm') \n");
		//负责人类型
		String ownerType =  customer.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){
				//查询自己的
				this.addSqlWhere(operator, sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){
				//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(customer.getComId());
				args.add(operator);
			}
		}
		return this.pagedQuery(sql.toString(), "a.isread,a.modifyTime desc,a.id desc", args.toArray(), Customer.class);
	}
	/**
	 * 查询所有自己移交的客户
	 * @param customer
	 * @param operator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listCustomerHandsOfAll(Customer customer,Integer operator){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//外面再包一层
		sql.append("select * from ( \n");
		sql.append("select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,\n");
		sql.append("newsInfo.content as modifyContent,modifier.username as modifierName,  \n");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as isread, \n");

		sql.append("c.username as ownerName,c.gender,d.uuid,d.filename,area.areaName,customerType.typeName,\n");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("case when newsInfo.recordcreatetime is null then a.recordcreatetime else newsInfo.recordcreatetime end as modifyTime \n");
		sql.append("from customer a \n");
		sql.append("left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");

		sql.append(" left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" and a.id = newsInfo.busId and newsInfo.busType="+ConstantInterface.TYPE_CRM+"\n");
		sql.append(" left join userinfo modifier on newsInfo.Userid = modifier.id  \n");

		sql.append(" inner join userinfo c on a.owner = c.id \n");
		sql.append(" inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append(" left join upfiles d on cc.mediumHeadPortrait = d.id \n");

		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=? \n");
		args.add(operator);
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_CRM+"' and today.userId=?  \n ");
		args.add(operator);
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(customer.getComId());
		//数据筛选，筛选自己移交的
		sql.append(" and a.owner!=? \n");
		args.add(operator);
		sql.append(" and exists(\n");
		sql.append(" select b.* from customerHandOver b where a.comId = b.comId and a.id = b.customerId\n");
		sql.append(" and  b.fromuser=? and b.touser!=? \n");
		sql.append(" )\n");
		args.add(operator);
		args.add(operator);
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//客户类型筛选
	    if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
		  this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
	    }
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		sql.append(")a where 1=1 \n");
		//如果是查看未读列表，则建立表间关系
		if(null!=customer.getIsRead() && 0==customer.getIsRead()){
			sql.append("and a.isread=0 \n");
		}
		//负责人类型
		String ownerType =  customer.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){
				//查询自己的
				this.addSqlWhere(operator, sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){
				//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(customer.getComId());
				args.add(operator);
			}
		}
		sql.append("order by a.isread,a.modifyTime desc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Customer.class);
	}
	/**
	 * 分页查询所有自己移交的客户
	 * @param customer
	 * @param operator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listCustomerHandsForPage(Customer customer,Integer operator){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//外面再包一层
		sql.append("select * from ( \n");
		sql.append("select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,\n");
		sql.append("newsInfo.content as modifyContent,modifier.username as modifierName,  \n");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as isread, \n");

		sql.append("c.username as ownerName,c.gender,d.uuid,d.filename,area.areaName,customerType.typeName,\n");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("case when newsInfo.recordcreatetime is null then a.recordcreatetime else newsInfo.recordcreatetime end as modifyTime\n");
		sql.append("from customer a \n");
		sql.append("left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");

		sql.append(" left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" and a.id = newsInfo.busId and newsInfo.busType="+ConstantInterface.TYPE_CRM+"\n");
		sql.append(" left join userinfo modifier on newsInfo.Userid = modifier.id  \n");

		sql.append(" inner join userinfo c on a.owner = c.id \n");
		sql.append(" inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append(" left join upfiles d on cc.mediumHeadPortrait = d.id \n");

		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=? \n");
		args.add(operator);
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_CRM+"' and today.userId=?  \n ");
		args.add(operator);
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(customer.getComId());
		//数据筛选，筛选自己移交的
		sql.append(" and a.owner!=? \n");
		args.add(operator);
		sql.append(" and exists(\n");
		sql.append(" select b.* from customerHandOver b where a.comId = b.comId and a.id = b.customerId\n");
		sql.append(" and  b.fromuser=? and b.touser!=? \n");
		sql.append(" )\n");
		args.add(operator);
		args.add(operator);
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//客户类型筛选
	    if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
		  this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
	    }
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		sql.append(")a where 1=1 \n");
		//如果是查看未读列表，则建立表间关系
		if(null!=customer.getIsRead() && 0==customer.getIsRead()){
			sql.append("and a.isread=0 \n");
		}
		//负责人类型
		String ownerType =  customer.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){
				//查询自己的
				this.addSqlWhere(operator, sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){
				//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(customer.getComId());
				args.add(operator);
			}
		}
		return this.pagedQuery(sql.toString(),"a.isread,a.modifyTime desc,a.id desc", args.toArray(),Customer.class);
	}
	/**
	 * 获取客户详细信息
	 * @param user 操作人员
	 * @param customerId 客户主键
	 * @return
	 */
	public Customer queryCustomer(UserInfo user,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,cs.stageName,b.username as ownerName,b.gender,c.uuid,c.filename,d.areaname,d.id||'@0' as areaIdAndType,e.typename, \n");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState \n");
		sql.append("from customer a \n");
		sql.append("inner join customerStage cs on cs.id = a.stage and a.comId = cs.comId  \n");
		sql.append("inner join area d on a.areaid = d.id and a.comId = d.comId  \n");
		sql.append("inner join Customertype e on a.customertypeid = e.id and a.comId = e.comId  \n");
		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=? \n");
		args.add(user.getId());
		sql.append("inner join userInfo b on a.owner = b.id \n");
		sql.append("inner join userOrganic bb on b.id = bb.userId and a.comId = bb.comId \n");
		sql.append("left join upfiles c on  bb.smallheadportrait = c.id \n");
		sql.append("where a.comId=? and a.id =? \n");
		args.add(user.getComId());
		args.add(customerId);
		return (Customer)this.objectQuery(sql.toString(), args.toArray(),Customer.class);
	}
	/**
	 * 获取客户分享人集合
	 * @param comId
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerSharer> listCustomerSharer(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,a.customerid,b.username as sharerName,b.gender,c.uuid,c.filename \n");
		sql.append("from customersharer a \n");
		sql.append("inner join userInfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id = bb.userId and a.comId = bb.comId \n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id \n");
		sql.append("where a.comId=? and a.customerid=? \n");
		args.add(comId);
		args.add(customerId);
		return this.listQuery(sql.toString(), args.toArray(), CustomerSharer.class);
	}
	/**
	 * 获取联系人集合
	 * @param comId
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LinkMan> listLinkMan(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//2017.6.7  sql.append("select a.* from linkman a \n");
		sql.append("select a.*,b.linkManName,b.post,b.movePhone,b.email,b.wechat,b.linePhone,b.qq from olmAndCus a "
				+ "right join outLinkMan b on b.id = a.outLinkManId \n");
		sql.append("where a.comId=? and a.customerid=? \n");
		sql.append("order by a.id asc \n");
		args.add(comId);
		args.add(customerId);
		return this.listQuery(sql.toString(), args.toArray(), LinkMan.class);
	}

	/**
	 * 获取客户附件集合
	 * @param comId
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerUpfile> listCustomerUpfile(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( \n");
		sql.append("select a.customerId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'"+ConstantInterface.TYPE_CRM+"' as type from customerUpfile a \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and a.customerid=?");
		sql.append("\n union all \n");
		sql.append("select a.customerId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'"+ConstantInterface.TYPE_CRMTALK+"' as type  from feedInfoFile a \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");;
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and a.customerid=?");
		sql.append(") a \n");
		return this.listQuery(sql.toString(), args.toArray(), CustomerUpfile.class);
	}
	/**
	 * 获取客户附件集合
	 * @param comId
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerUpfile> listCrmFileForDel(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( \n");
		sql.append("select a.customerId,a.upfileId,'"+ConstantInterface.TYPE_CRM+"' as type,0 subBusId \n");
		sql.append("from customerUpfile a inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and a.customerid=?");
		sql.append("\n union all \n");
		sql.append("select a.customerId,a.upfileId,'"+ConstantInterface.TYPE_CRMTALK+"' as type,a.backInfoId subBusId  \n");
		sql.append("from feedInfoFile a inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and a.customerid=?");
		sql.append(") a \n");
		return this.listQuery(sql.toString(), args.toArray(), CustomerUpfile.class);
	}


	/**
	 * 获取客户附件集合
	 * @param comId
	 * @param customerUpfile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerUpfile> listPagedCustomerUpfile(Integer comId,CustomerUpfile customerUpfile){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( \n");

		//只是客户模块
		sql.append("select a.customerId,a.comId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id,? upType, \n");
		args.add(ConstantInterface.TYPE_CRM);
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'012' type,a.customerid busId,crm.customername busName from customerUpfile a \n");
		sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerUpfile.getCustomerId(), sql, args, " and a.customerid=?");
		sql.append("\n union all \n");
		sql.append("select a.customerId,a.comId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id,? upType, \n");
		args.add(ConstantInterface.TYPE_CRMTALK);
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName ,'012' type,a.customerid busId,crm.customername busName from feedInfoFile a\n");
		sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");;
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerUpfile.getCustomerId(), sql, args, " and a.customerid=?");

		//只是项目模块（项目，讨论，阶段）
		sql.append("\n union all \n");
		sql.append("select item.partnerId  customerId,a.comId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id,? upType, \n");
		args.add(ConstantInterface.TYPE_ITEM);
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'005' type,a.itemId busId,item.itemname busName from itemUpfile a \n");
		sql.append("inner join item on a.comId = item.comId and a.itemId = item.id and item.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerUpfile.getCustomerId(), sql, args, " and item.partnerId=?");
		sql.append("\n union all \n");
		sql.append("select item.partnerId  customerId,a.comId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id,? upType, \n");
		args.add(ConstantInterface.TYPE_ITEMTALK);
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'005' type,a.itemId busId,item.itemname busName from itemTalkFile a \n");
		sql.append("inner join item on a.comId = item.comId and a.itemId = item.id and item.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerUpfile.getCustomerId(), sql, args, " and item.partnerId=?");
		sql.append("\n union all \n");
		sql.append("select item.partnerId  customerId,a.comId,a.creator userId,a.moduleId upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id,? upType, \n");
		args.add(ConstantInterface.TYPE_ITEMSTAGE);
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'005' type,a.itemId busId,item.itemname busName from stagedInfo a \n");
		sql.append("inner join item on a.comId = item.comId and a.itemId = item.id and item.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.moduleId = b.id \n");
		sql.append("left join userinfo c on a.creator = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerUpfile.getCustomerId(), sql, args, " and item.partnerId=?");

		//项目任务附件
		sql.append("\n union all \n");
		sql.append("select item.partnerId  customerId,a.comId,a.userId,a.upfileid upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id,? upType, \n");
		args.add(ConstantInterface.TYPE_TASK);
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'003' type,a.taskid busId,task.taskname busName from taskupfile a  \n");
		sql.append("inner join task on a.comId=task.comId and a.taskid=task.id and task.delstate=0 and task.bustype='005'\n");
		sql.append("inner join item on a.comId = item.comId and task.busId = item.id and item.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileId = b.id \n");
		sql.append("left join userinfo c on a.userId = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerUpfile.getCustomerId(), sql, args, " and item.partnerId=?");
		//项目任务讨论附件
		sql.append("\n union all \n");
		sql.append("select item.partnerId  customerId,a.comId,a.userId,a.upfileid upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id,? upType, \n");
		args.add(ConstantInterface.TYPE_TASKTALK);
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'003' type,a.taskid busId,task.taskname busName from taskTalkUpfile a  \n");
		sql.append("inner join task on a.comId=task.comId and a.taskid=task.id and task.delstate=0 and task.bustype='005'\n");
		sql.append("inner join item on a.comId = item.comId and task.busId = item.id and item.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileId = b.id \n");
		sql.append("left join userinfo c on a.userId = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerUpfile.getCustomerId(), sql, args, " and item.partnerId=?");

		//只是任务模块（任务，讨论）
		sql.append("\n union all \n");
		sql.append("select task.busId  customerId,a.comId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id,? upType, \n");
		args.add(ConstantInterface.TYPE_TASK);
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'003' type,a.taskId busId,task.taskname busName from taskUpfile a \n");
		sql.append("inner join task on a.comId = task.comId and a.taskId = task.id and task.busType='"+ConstantInterface.TYPE_CRM+"' and task.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerUpfile.getCustomerId(), sql, args, " and task.busId=?");
		sql.append("\n union all \n");
		sql.append("select task.busId  customerId,a.comId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt,a.id,? upType, \n");
		args.add(ConstantInterface.TYPE_TASKTALK);
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'003' type,a.taskId busId,task.taskname busName from taskTalkUpfile a \n");
		sql.append("inner join task on a.comId = task.comId and a.taskId = task.id and task.busType='"+ConstantInterface.TYPE_CRM+"'  and task.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerUpfile.getCustomerId(), sql, args, " and task.busId=?");
		sql.append(") a where 1=1 \n");
		this.addSqlWhereLike(customerUpfile.getFilename(), sql, args, " and a.filename like ? ");
		String order = "a.recordcreatetime desc";
		if(!CommonUtil.isNull(customerUpfile)){
			if("fileExt".equals(customerUpfile.getOrder())) {
				order = "a.fileExt,a.recordcreatetime desc";
			}else if("userId".equals(customerUpfile.getOrder())) {
				order = "a.userId,a.recordcreatetime desc";
			}
		}
		return this.pagedQuery(sql.toString(), order, args.toArray(), CustomerUpfile.class);
	}

	/**
	 * 查询附件总数
	 * @param comId
	 * @param customerId
	 * @return
	 */
	public Integer countFile(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from ( \n");

		//只是客户模块
		sql.append("select a.customerId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'012' type,a.customerid busId,crm.customername busName from customerUpfile a \n");
		sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and a.customerid=?");
		sql.append("\n union all \n");
		sql.append("select a.customerId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName ,'012' type,a.customerid busId,crm.customername busName from feedInfoFile a\n");
		sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");;
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and a.customerid=?");

		//只是项目模块（项目，讨论，阶段）
		sql.append("\n union all \n");
		sql.append("select item.partnerId  customerId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'005' type,a.itemId busId,item.itemname busName from itemUpfile a \n");
		sql.append("inner join item on a.comId = item.comId and a.itemId = item.id and item.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and item.partnerId=?");
		sql.append("\n union all \n");
		sql.append("select item.partnerId  customerId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'005' type,a.itemId busId,item.itemname busName from itemTalkFile a \n");
		sql.append("inner join item on a.comId = item.comId and a.itemId = item.id and item.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and item.partnerId=?");
		sql.append("\n union all \n");
		sql.append("select item.partnerId  customerId,a.creator userId,a.moduleId upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'005' type,a.itemId busId,item.itemname busName from stagedInfo a \n");
		sql.append("inner join item on a.comId = item.comId and a.itemId = item.id and item.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.moduleId = b.id \n");
		sql.append("left join userinfo c on a.creator = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and item.partnerId=?");

		//项目任务附件
		sql.append("\n union all \n");
		sql.append("select item.partnerId  customerId,a.userId,a.upfileid upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'003' type,a.taskid busId,task.taskname busName from taskupfile a  \n");
		sql.append("inner join task on a.comId=task.comId and a.taskid=task.id and task.delstate=0 and task.bustype='005'\n");
		sql.append("inner join item on a.comId = item.comId and task.busId = item.id and item.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileId = b.id \n");
		sql.append("left join userinfo c on a.userId = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and item.partnerId=?");
		//项目任务讨论附件
		sql.append("\n union all \n");
		sql.append("select item.partnerId  customerId,a.userId,a.upfileid upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'003' type,a.taskid busId,task.taskname busName from taskTalkUpfile a  \n");
		sql.append("inner join task on a.comId=task.comId and a.taskid=task.id and task.delstate=0 and task.bustype='005'\n");
		sql.append("inner join item on a.comId = item.comId and task.busId = item.id and item.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileId = b.id \n");
		sql.append("left join userinfo c on a.userId = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and item.partnerId=?");

		//只是任务模块（任务，讨论）
		sql.append("\n union all \n");
		sql.append("select task.busId  customerId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'003' type,a.taskId busId,task.taskname busName from taskUpfile a \n");
		sql.append("inner join task on a.comId = task.comId and a.taskId = task.id and task.busType='"+ConstantInterface.TYPE_CRM+"' and task.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and task.busId=?");
		sql.append("\n union all \n");
		sql.append("select task.busId  customerId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,b.fileExt, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'003' type,a.taskId busId,task.taskname busName from taskTalkUpfile a \n");
		sql.append("inner join task on a.comId = task.comId and a.taskId = task.id and task.busType='"+ConstantInterface.TYPE_CRM+"'  and task.delstate=0\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and task.busId=?");
		sql.append(") a \n");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 客户权限查看权限验证
	 * @param comId
	 * @param customerId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> authorCheck(Integer comId,Integer customerId,Integer userId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id from customer a  \n");
		sql.append("left join customerSharer b on a.comId = b.comId and a.id = b.customerId \n");
		sql.append("where a.comId = ? and a.id=? and a.delState=0 \n");
		args.add(comId);
		args.add(customerId);
		sql.append("and (\n");
		//任务的参与、执行、负责权限验证
		sql.append(" b.userId =? or a.owner =? \n");
		args.add(userId);
		args.add(userId);
		//上级权限验证
		//参与人上级权限验证
		sql.append(" or exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
		args.add(comId);
		args.add(userId);
		//负责人上级权限验证
		sql.append(" or exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
		args.add(comId);
		args.add(userId);
		sql.append(") \n");
		//合并
		sql.append("union \n");
		//客户成员组验证
		sql.append("select a.customerId as id from view_customer_group_userId a \n");
		sql.append("where a.comId = ? and a.customerId=? and a.userId=? \n");
		args.add(comId);
		args.add(customerId);
		args.add(userId);
		sql.append("union \n");
		//公开客户验证
		sql.append("select  id from customer where pubState = 1 and comId=? \n");
		args.add(comId);
		return this.listQuery(sql.toString(), args.toArray(),Customer.class);
	}
	/**
	 * 根据主键获取客户类型
	 * @param comId
	 * @param typeId
	 * @return
	 */
	public CustomerType queryCustomerType(Integer comId,Integer typeId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from customerType a where a.comId=? and a.id=?");
		args.add(comId);
		args.add(typeId);
		return (CustomerType)this.objectQuery(sql.toString(), args.toArray(),CustomerType.class);
	}
	/**
	 * 根据主键获取联系人信息
	 * @param comId
	 * @param linkManId
	 * @return
	 */
	public LinkMan queryLinkMan(Integer comId,Integer customerId,Integer linkManId){
		List<Object> args = new ArrayList<Object>();
		//2017.6.7 StringBuffer sql = new StringBuffer("select a.* from linkman a where a.comId=? and a.customerid=? and a.id=?");
		StringBuffer sql = new StringBuffer("select a.*,b.linkManName from olmAndCus a "
				+ "left join outLinkMan b on b.id = a.outLinkManId "
				+ "where a.comId=? and a.customerid=? and a.id=?");
		args.add(comId);
		args.add(customerId);
		args.add(linkManId);
		return (LinkMan)this.objectQuery(sql.toString(), args.toArray(),LinkMan.class);
	}
	/**
	 * 获取客户日志集合
	 * @param customerId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerLog> listCustomerLog(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.username as userName,b.gender,c.uuid,c.filename as fileName \n");
		sql.append("from customerLog a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id = bb.userId and a.comId = bb.comId \n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.customerId = ? \n");
		args.add(comId);
		args.add(customerId);
		return this.pagedQuery(sql.toString()," a.recordcreatetime desc", args.toArray(), CustomerLog.class);
	}
	/**
	 * 获取反馈类型集合
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeedBackType> listFeedBackType(Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from feedBackType a where a.comId=? ");
		args.add(comId);
		sql.append(" order by a.typeorder asc");
		return this.listQuery(sql.toString(), args.toArray(),FeedBackType.class);
	}

	/**
	 * 初始化客户反馈类型排序值
	 * @param comId
	 * @return
	 */
	public FeedBackType queryFeedBackTypeOrderMax(Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.typeorder)+1  as typeorder from feedBackType a where a.comId =?");
		args.add(comId);
		return (FeedBackType)this.objectQuery(sql.toString(), args.toArray(),FeedBackType.class);
	}
	/**
	 * 根据主键获取客户反馈类型
	 * @param comId
	 * @param typeId
	 * @return
	 */
	public FeedBackType queryFeedBackType(Integer comId,Integer typeId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from feedBackType a where a.comId=? and a.id=?");
		args.add(comId);
		args.add(typeId);
		return (FeedBackType)this.objectQuery(sql.toString(), args.toArray(),FeedBackType.class);
	}
	/**
	 * 获取客户维护记录集合
	 * @param comId
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeedBackInfo> listFeedBackInfo(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select connect_by_isleaf as isLeaf,PRIOR a.userId as pSpeaker,PRIOR a.content as pContent,\n");
		sql.append("PRIOR b.username as pSpeakerName,a.*,b.username as speakerName,b.gender,c.uuid,c.filename,f.typename \n");
		sql.append("from feedBackInfo a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id = bb.userId and a.comId = bb.comId \n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("inner join feedBackType f on f.comId = a.comId and a.feedbacktypeid = f.id \n");
		sql.append("where a.comId=? and a.customerId = ? \n");
		args.add(comId);
		args.add(customerId);
		sql.append("start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid \n");
		sql.append("order siblings by a.recordcreatetime desc,a.id \n");
		return this.pagedQuery(sql.toString(), null, args.toArray(),FeedBackInfo.class);
	}
	/**
	 * 根据主键获取客户维护信息明细
	 * @param comId
	 * @param feedBackInfoId
	 * @return
	 */
	public FeedBackInfo queryFeedBackInfo(Integer comId,Integer feedBackInfoId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.customerId,a.id,d.userId as pSpeaker,d.content as pContent,e.username as pSpeakerName, \n");
		sql.append("a.*,b.username as speakerName,b.gender,c.uuid,c.filename,f.typename  \n");
		sql.append("from feedBackInfo a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId = bb.comId \n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("left join feedBackInfo d on a.parentid = d.id and a.comId = d.comId \n");
		sql.append("left join userinfo e on d.userId = e.id \n");
		sql.append("inner join feedBackType f on f.comId = a.comId and a.feedbacktypeid = f.id \n");
		sql.append("where a.comId=? and a.id = ? \n");
		args.add(comId);
		args.add(feedBackInfoId);
		return (FeedBackInfo)this.objectQuery(sql.toString(),args.toArray(),FeedBackInfo.class);
	}
	/**
	 * 删除客户维护记录以及记录的所有后代记录
	 * @param comId
	 * @param feedBackInfoId
	 */
	public void delFeedBackInfo(Integer comId,Integer feedBackInfoId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from feedBackInfo a where a.comId =? and a.id in \n");
		sql.append("(select id from feedBackInfo start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(feedBackInfoId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 删除客户维护记录以及记录的所有后代记录准备
	 * @param comId
	 * @param feedBackInfoId
	 */
	@SuppressWarnings("unchecked")
	public List<FeedBackInfo> listFeedBackInfoForDel(Integer comId,Integer feedBackInfoId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from feedBackInfo where comId=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(feedBackInfoId);
		return this.listQuery(sql.toString(), args.toArray(), FeedBackInfo.class);
	}
	/**
	 * 更新客户维护记录父节点
	 * @param comId
	 * @param feedBackInfoId
	 */
	public void updateFeedBackInfoParentId(Integer comId,Integer feedBackInfoId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update feedBackInfo set parentId=(select c.parentid \n");
		sql.append("from feedBackInfo c \n");
		sql.append("where c.id=?) where parentid = ? and comId = ? \n");
		args.add(feedBackInfoId);
		args.add(feedBackInfoId);
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 *
	 * 非现在的客户信息参与人
	 * @param comId
	 * @param customerId
	 * @param userIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerSharer> listRemoveCustomerSharer(Integer comId,
			Integer customerId, Integer[] userIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( \n");
		//原有客户参与人
		sql.append("select a.comId,a.customerId,a.userId from customerSharer a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(customerId, sql, args, " and  a.customerId = ? ");
		this.addSqlWhere(comId, sql, args, " and  a.comId= ? ");
		sql.append("minus \n");
		//客户负责人
		sql.append("select a.comId,a.id customerId,a.owner userId from customer a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(customerId, sql, args, " and  a.id = ? ");
		this.addSqlWhere(comId, sql, args, " and  a.comId= ? ");
		sql.append("minus \n");
		//现在的客户参与人
		sql.append("select a.comId,a.customerId,a.userId from customerSharer a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(customerId, sql, args, " and  a.customerId = ?");
		this.addSqlWhere(comId, sql, args, " and a.comId= ?");
		sql.append(" and a.userId in (0");
		if(null!=userIds && userIds.length>0){
			for (Integer userId : userIds) {
				sql.append(","+userId);
			}
		}
		sql.append(")\n");
		sql.append("minus \n");
		//原有客户共享组
		sql.append("select a.comId,a.customerId,b.userinfoid userId from customerShareGroup a inner join grouppersion b \n");
		sql.append("on a.comId=b.comId and a.grpid=b.grpid where  1=1 \n");
		this.addSqlWhere(customerId, sql, args, " and  a.customerId = ?");
		this.addSqlWhere(comId, sql, args, " and a.comId= ?");


		sql.append(")\n");
		return this.listQuery(sql.toString(), args.toArray(), CustomerSharer.class);
	}
	/**
	 * 负责人是客户信息参与人
	 * @param comId
	 * @param customerId
	 * @param userId
	 * @return
	 */
	public CustomerSharer getCustomerSharer(Integer comId,
			Integer customerId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//原有项目参与人
		sql.append("select a.* from customerSharer a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(customerId, sql, args, " and  a.customerId = ? ");
		this.addSqlWhere(comId, sql, args, " and  a.comId= ? ");
		this.addSqlWhere(userId, sql, args, " and  a.userId= ? ");
		return (CustomerSharer) this.objectQuery(sql.toString(), args.toArray(), CustomerSharer.class);
	}
	/**
	 * 获取匹配客户集合
	 * @param customer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listCustomer(Customer customer){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.customerName from customer a \n");
		sql.append("where a.comId=? \n");
		args.add(customer.getComId());
		this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		return this.listQuery(sql.toString(), args.toArray(),Customer.class);
	}
	/**
	 * 反馈的附件信息
	 * @param comId 企业编号
	 * @param customerId 客户主键
	 * @param backInfoId 反馈信息主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeedInfoFile> listFeedInfoFiles(Integer comId,
			Integer customerId, Integer backInfoId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.customerId,a.backInfoId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,b.fileext,\n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from feedInfoFile a  inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("where a.comId =? and a.customerid = ? \n");
		args.add(comId);
		args.add(customerId);
		this.addSqlWhere(backInfoId, sql, args, " and a.backInfoId=? ");
		sql.append("order by isPic asc, a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), FeedInfoFile.class);
	}
	/**
	 * 获取客户的所有参与人员集合
	 * @param comId
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerSharer> listCustomerOwners(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.owner as userId,a.id as customerid,c.username from customer a \n");
		sql.append("inner join Userinfo c on a.owner = c.id \n");
		sql.append("inner join userOrganic uOrg1 on a.comId = uOrg1.Comid and c.id = uOrg1.Userid and uOrg1.Enabled=1 \n");
		sql.append("where a.comId = ? and a.id = ? \n");
		args.add(comId);
		args.add(customerId);
		sql.append("union \n");
		sql.append("select b.userid,b.customerid,d.username from customerSharer b \n");
		sql.append("inner join Userinfo d on  b.userid = d.id \n");
		sql.append("inner join userOrganic uOrg2 on b.comId = uOrg2.Comid and d.id = uOrg2.Userid and uOrg2.Enabled=1 \n");
		sql.append("where b.comId = ? and b.customerid=? \n");
		args.add(comId);
		args.add(customerId);
//		sql.append("union \n");
//		sql.append("select f.userid,? as customerid,e.username from forceinpersion f \n");
//		args.add(customerId);
//		sql.append("inner join Userinfo e on f.userid=e.id \n");
//		sql.append("inner join userOrganic uOrg3 on f.comId = uOrg3.Comid and e.id = uOrg3.Userid and uOrg3.Enabled=1 \n");
//		sql.append("where f.comId = ? and f.type=? \n");
//		args.add(comId);
//		args.add(BusinessTypeConstant.type_crm);
		//客户组成员获取
		sql.append("union \n");
		sql.append("select b.id as userId,a.customerid,b.username as sharerName from view_customer_group_userId a \n");
		sql.append("inner join userInfo b on a.userId = b.id \n");
		sql.append("where a.comId=? and a.customerid=? \n");
		args.add(comId);
		args.add(customerId);
		return this.listQuery(sql.toString(),args.toArray(),CustomerSharer.class);
	}
	/**
	 * 获取客户的所有参与人员集合不包含督察人员(在岗的)
	 * @param comId
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listCrmOwnersNoForce(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//客户负责人
		sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from customer a \n");
		sql.append("inner join Userinfo c on a.owner = c.id \n");
		sql.append("inner join userOrganic uOrg1 on a.comId = uOrg1.Comid and c.id = uOrg1.Userid and uOrg1.Enabled=1 \n");
		sql.append("where a.comId = ? and a.id = ? \n");
		args.add(comId);
		args.add(customerId);
		sql.append("union \n");
		//客户参与人
		sql.append("select d.id,d.email,d.wechat,d.qq,d.userName from customerSharer b \n");
		sql.append("inner join Userinfo d on  b.userid = d.id \n");
		sql.append("inner join userOrganic uOrg2 on b.comId = uOrg2.Comid and d.id = uOrg2.Userid and uOrg2.Enabled=1 \n");
		sql.append("where b.comId = ? and b.customerid=? \n");
		args.add(comId);
		args.add(customerId);
		//客户组成员获取
		sql.append("union \n");
		sql.append("select b.id,b.email,b.wechat,b.qq,b.userName from view_customer_group_userId a \n");
		sql.append("inner join userInfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic uOrg3 on a.comId = uOrg3.Comid and a.userId = uOrg3.Userid and uOrg3.Enabled=1 \n");
		sql.append("where a.comId=? and a.customerid=? \n");
		args.add(comId);
		args.add(customerId);
		//关注该客户的人员获取
		sql.append("union \n");
		sql.append("select b.id,b.email,b.wechat,b.qq,b.userName from attention a \n");
		sql.append("inner join userInfo b on a.userId = b.id \n");
		sql.append("and a.busType="+ConstantInterface.TYPE_CRM+" \n");
		sql.append("inner join userOrganic uOrg3 on a.comId = uOrg3.Comid and b.id = uOrg3.Userid and uOrg3.Enabled=1 \n");
		sql.append("where a.comId=? and a.busId=? \n");
		args.add(comId);
		args.add(customerId);
		return this.listQuery(sql.toString(),args.toArray(),UserInfo.class);
	}
	/**
	 * 获取客户反馈记录集合FOR索引
	 * @param comId
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeedBackInfo> listFeedBackInfo4Index(Integer comId,Integer customerId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.content,b.typename,c.username as speakerName from feedBackInfo a \n");
		sql.append("inner join feedbacktype b on a.comId = b.comId and a.feedbacktypeid = b.id \n");
		sql.append("inner join userInfo c on a.userid = c.id \n");
		sql.append("where a.comId=? and a.customerid=? \n");
		args.add(comId);
		args.add(customerId);
		return this.listQuery(sql.toString(),args.toArray(),FeedBackInfo.class);
	}
	/**
	 * 模板区域
	 * @param regionIds 查询的区域条件
	 * @param comId 团队号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Region> listRegion(List<Integer> regionIds, Integer comId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select id,comId,regionCode,regionName,parentId,regionLevel,regionOrder,regionPingying,regionShortpy,connect_by_isleaf as isLeaf \n");
		sql.append("from region where level>1\n");
		sql.append("start with parentid=-1 CONNECT BY PRIOR id = parentid\n");
		return this.listQuery(sql.toString(), null,Region.class);
	}
	/**
	 * 获取用户查看客户记录信息
	 * @param comId
	 * @param customerId
	 * @param userId
	 * @return
	 */
	public CustomerRead queryCustomerRead(Integer comId,Integer customerId,Integer userId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from customerRead a where a.comId=? and a.customerid=? and a.userid=? and rownum=1");
		args.add(comId);
		args.add(customerId);
		args.add(userId);
		return (CustomerRead)this.objectQuery(sql.toString(),args.toArray(),CustomerRead.class);
	}
	/**
	 * 查找上级人员
	 * @param customerId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ImmediateSuper> listImmediateSuper(Integer customerId,
			Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.leader from myLeaders a where exists (");
		//客户负责人
		sql.append("\n select b.id from customer b where b.owner = a.creator and b.id=? and b.comId=?");
		args.add(customerId);
		args.add(comId);
		sql.append("\n union all");
		//客户参与人
		sql.append("\n select b.userid from  customerSharer b where b.customerId=? and b.comId=?");
		args.add(customerId);
		args.add(comId);
		sql.append("\n )");
		sql.append("\n connect by prior a.leader = a.creator");
		sql.append("\n )a group by a.leader");
		return this.listQuery(sql.toString(), args.toArray(), ImmediateSuper.class);
	}
	/**
	 * 人员负责的客户
	 * @param comId 企业号
	 * @param userId 人员主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listUserAllCrm(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.owner,a.customername from  customer a");
		sql.append("\n where a.delstate=0 and  a.comId=? and a.owner=?");
		args.add(comId);
		args.add(userId);
		return this.listQuery(sql.toString(), args.toArray(), Customer.class);
	}
	/**
	 * 移交记录
	 * @param crmId 客户主键
	 * @param comId 企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FlowRecord> listFlowRecord(Integer crmId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.*,c.touser userId from ( ");
		sql.append("\n 	select * from ( ");
		sql.append("\n 		select * from ( ");
		sql.append("\n 			select a.recordcreatetime acceptDate,c.gender,c.username,d.uuid,d.filename,a.fromuser,a.touser,0 neworder");
		sql.append("\n 			from customerHandOver a inner join userorganic b on a.comId=b.comId and a.touser=b.userid");
		sql.append("\n 			inner join userinfo c on b.userid=c.id");
		sql.append("\n 			left join upfiles d on b.mediumheadportrait=d.id");
		sql.append("\n 			where a.comId=? and a.customerId=?");
		args.add(comId);
		args.add(crmId);
		sql.append("\n 			order by a.id ");
		sql.append("\n 		)a where rownum=1 ");
		sql.append("\n 	)a where a.fromuser<>a.touser");
		sql.append("\n 	union all  ");
		sql.append("\n 	select b.*,rownum neworder from ( ");
		sql.append("\n 		select a.recordcreatetime acceptDate,c.gender,c.username,d.uuid,d.filename,a.fromuser,a.touser");
		sql.append("\n 		from customerHandOver a inner join userorganic b on a.comId=b.comId and a.touser=b.userid");
		sql.append("\n 		inner join userinfo c on b.userid=c.id");
		sql.append("\n 		left join upfiles d on b.mediumheadportrait=d.id");
		sql.append("\n 		where a.comId=? and a.customerId=?");
		args.add(comId);
		args.add(crmId);
		sql.append("\n 		order by a.id ");
		sql.append("\n 	)b");
		sql.append("\n )c order by neworder desc");
		return this.listQuery(sql.toString(), args.toArray(), FlowRecord.class);
	}
	/**
	 * 获取个人私有组并于当前项目比对是否关联
	 * @param customerId
	 * @param userId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> listSelfGroupOfCrm(Integer customerId,Integer userId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,case when b.id is null then 0 else 1 end as checked from selfgroup a \n");
		sql.append("left join customerShareGroup b on a.comId = b.comId and a.id = b.grpid and b.customerId=? \n");
		args.add(customerId);
		sql.append("where a.comId =? and a.owner=? \n");
		args.add(comId);
		args.add(userId);
		return this.pagedQuery(sql.toString(),"checked desc,a.id desc", args.toArray(),SelfGroup.class);
	}
	/**
	 * 获取已经和客户关联的私有组集合
	 * @param customerId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> listShareGroupOfCrm(int customerId,int comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from selfgroup a \n");
		sql.append("inner join customerShareGroup b on a.comId = b.comId and a.id = b.grpid \n");
		sql.append("where a.comId =? and b.customerId=? \n");
		args.add(comId);
		args.add(customerId);
		return this.listQuery(sql.toString(), args.toArray(),SelfGroup.class);
	}
	/**
	 * 获取个人权限下排列前N的客户数据集合
	 * @param userInfo
	 * @param isForceInPersion
	 * @param rowNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> firstNCustomerList(UserInfo userInfo,boolean isForceInPersion,Integer rowNum){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from ( \n");
		//外面再包一层
		sql.append("select * from ( \n");
		sql.append("	select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,  \n");
		sql.append("	newsInfo.content as modifyContent,modifier.username as modifierName,  \n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as isread, \n");

		sql.append("	case when atten.id is null then 0 else 1 end as attentionState,\n");
		sql.append("	c.username as ownerName,c.gender,d.uuid,area.areaName,customerType.typeName,  \n");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("	case when newsInfo.recordcreatetime is null then a.recordcreatetime else newsInfo.recordcreatetime end as modifyTime \n");
		sql.append("	from customer a \n");
		sql.append("	left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("	left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");

		sql.append("	inner join userinfo c on a.owner = c.id \n");
		sql.append("	inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
		sql.append("	left join upfiles d on cc.mediumHeadPortrait = d.id \n");

		sql.append("	left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=? \n");
		args.add(userInfo.getId());

		sql.append(" 	left join newsInfo on a.comId = newsInfo.comId and a.id = newsInfo.busId   \n");
		sql.append(" 	and  newsInfo.busType="+ConstantInterface.TYPE_CRM+" \n");


		sql.append(" 	left join userinfo modifier on newsInfo.Userid = modifier.id  \n");
		//操作人员的待办事项或是未读提醒
		sql.append("\n left join todayworks today on a.comId = today.comId and a.id = today.busid ");
		sql.append("\n and today.bustype='"+ConstantInterface.TYPE_CRM+"' and today.userId=? ");
		args.add(userInfo.getId());
		sql.append("	where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
		}
		//外层结束

		sql.append(")a order by a.isread,a.modifyTime desc,a.id desc \n");
		sql.append(") where rownum <= ?");
		args.add(rowNum);
		return this.listQuery(sql.toString(),args.toArray(),Customer.class);
	}
	/**
	 * 判断移除人员是否仍有查看权限
	 * @param customerId 客户主键
	 * @param comId 企业号
	 * @param userId 移除的参与人员主键
	 * @param owner 客户的负责人
	 * @return
	 */
	public Integer countUserNum(Integer customerId, Integer comId,
			Integer userId, Integer owner) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("\n select count(*) from ( ");
		//移除人员是否为客户负责人
		sql.append("\n select a.id from customer a where 1=1");
		this.addSqlWhere(customerId, sql, args, " and a.id=?");
		this.addSqlWhere(userId, sql, args, " and a.owner=?");
		sql.append("\n union all");
		//移除人员是否为参与人
		sql.append("\n select a.customerId from customerSharer a where 1=1");
		this.addSqlWhere(customerId, sql, args, " and a.customerId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		sql.append("\n union all");
		//移除人员是否在共享组
		sql.append("\n select a.customerId from  customerShareGroup a inner join grouppersion b on a.comId=b.comId");
		sql.append("\n and a.grpid=b.grpid");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(customerId, sql, args, " and a.customerId=?");
		this.addSqlWhere(owner, sql, args, " and b.userinfoid=?");
		sql.append("\n)");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 客户信息分享组中需要移除的成员
	 * @param comId 企业号
	 * @param customerId 客户主键
	 * @param grpId 分组主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listRemoveGrpUser(Integer comId,
			Integer customerId, Integer grpId) {

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//本次移除分组成员
		sql.append("\n select a.userinfoid id from grouppersion a where 1=1 ");
		this.addSqlWhere(grpId, sql, args, " and a.grpId=?");
		sql.append("\n minus");
		//客户负责人
		sql.append("\n select a.owner id from customer a where 1=1 ");
		this.addSqlWhere(customerId, sql, args, " and a.id=?");
		sql.append("\n minus ");
		//客户分享人
		sql.append("\n select a.userId id from customerSharer a where 1=1 ");
		this.addSqlWhere(customerId, sql, args, " and a.customerId=?");
		sql.append("\n minus ");
		//客户分享组
		sql.append("\n select b.userinfoid id from customerShareGroup a inner join grouppersion b");
		sql.append("\n on a.comId=b.comId and a.grpId=b.grpid ");
		this.addSqlWhere(customerId, sql, args, " and a.customerId=?");

		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 客户信息分享组中需要移除的成员
	 * @param comId 企业号
	 * @param customerId 客户主键
	 * @param grpIds 分组数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listRemoveCrmGrp(Integer comId,
			Integer customerId, Integer[] grpIds) {

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//客户原有分享组
		sql.append("\n select b.userinfoid id from customerShareGroup a inner join grouppersion b");
		sql.append("\n on a.comId=b.comId and a.grpId=b.grpid ");
		this.addSqlWhere(customerId, sql, args, " and a.customerId=?");
		sql.append("\n minus");
		//客户负责人
		sql.append("\n select a.owner id from customer a where 1=1 ");
		this.addSqlWhere(customerId, sql, args, " and a.id=?");
		sql.append("\n minus ");
		//客户分享人
		sql.append("\n select a.userId id from customerSharer a where 1=1 ");
		this.addSqlWhere(customerId, sql, args, " and a.customerId=?");
		sql.append("\n minus ");
		//本次设置分组成员
		sql.append("\n select a.userinfoid id from grouppersion a where 1=1 ");
		if(null!=grpIds && grpIds.length>0){
			sql.append("\n and a.grpid in (0");
			for (Integer grpId : grpIds) {
				sql.append(","+grpId);
			}
			sql.append(")");
		}

		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 根据客户名称查找相似客户
	 * @param crmName
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listSimilarCrmsByCheckCrmName(String crmName,
			Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.customerName,b.username as ownerName,a.recordcreatetime,b.gender,d.uuid,d.filename  \n");
		sql.append("from customer a inner join Userinfo b on a.owner = b.id \n");
		sql.append("inner join userOrganic c on a.comId = c.comId and b.id = c.userid \n");
		sql.append("left join upfiles d on c.mediumHeadPortrait = d.id \n");
		sql.append("where a.comId = ? and a.delstate=0 and c.enabled=1 \n");
		args.add(comId);
		this.addSqlWhereLike(crmName,sql,args," and a.customerName like ? \n");
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(), Customer.class);
	}
	/**
	 * 直接合并外部联系人
	 * @param comId 企业号
	 * @param customerId 合并前的客户主键
	 * @param crmId 合并后的客户主键
	 */
	public void compressOutLinkMan(Integer comId, Integer customerId, Integer crmId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update olmAndCus set customerId=? where comId=? and customerId=?");
		args.add(crmId);
		args.add(comId);
		args.add(customerId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 直接合并联系人
	 * @param comId 企业号
	 * @param customerId 合并前的客户主键
	 * @param crmId 合并后的客户主键
	 */
	public void compressLinkMan(Integer comId, Integer customerId, Integer crmId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update linkMan set customerId=? where comId=? and customerId=?");
		args.add(crmId);
		args.add(comId);
		args.add(customerId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 直接合并反馈信息
	 * @param comId 企业号
	 * @param customerId 合并前的客户主键
	 * @param crmId 合并后的客户主键
	 */
	public void compressFeedBack(Integer comId, Integer customerId,
			Integer crmId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并反馈信息
		sql.append(" update feedBackInfo set customerId=? where comId=? and customerId=?");
		args.add(crmId);
		args.add(comId);
		args.add(customerId);
		this.excuteSql(sql.toString(), args.toArray());
		//合并反馈信息附件
		args = new ArrayList<Object>();
		sql = new StringBuffer();
		sql.append(" update feedInfoFile set customerId=? where comId=? and customerId=?");
		args.add(crmId);
		args.add(comId);
		args.add(customerId);
		this.excuteSql(sql.toString(), args.toArray());
		//合并客户附件
		args = new ArrayList<Object>();
		sql = new StringBuffer();
		sql.append(" update customerUpfile set customerId=? where comId=? and customerId=?");
		args.add(crmId);
		args.add(comId);
		args.add(customerId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 客户合并后项目关联也需要调整
	 * @param comId
	 * @param customerId
	 * @param crmId
	 */
	public void compressItem(Integer comId, Integer customerId, Integer crmId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update item set partnerId=? where comId=? and partnerId=?");
		args.add(crmId);
		args.add(comId);
		args.add(customerId);
		this.excuteSql(sql.toString(), args.toArray());

	}
	/**
	 * 将关联到该客户的项目信息设置为0
	 * @param comId 企业号
	 * @param crmId 客户主键
	 */
	public void updateItemCrmId(Integer comId, Integer crmId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update item set partnerid=0 where comId=? and partnerid=?");
		args.add(comId);
		args.add(crmId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 将任务关联的项目字段设为0
	 * @param busId 任务表项目主键字段
	 * @param comId 企业号
	 */
	public void updateTaskBusId(Integer busId, Integer comId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update task set busId=0,busType='0' where comId=? and busId=? and busType=?");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 查询其他评论是否有相同的附件
	 * @param comId 企业号
	 * @param customerId 客户主键
	 * @param upfileId 附件主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeedInfoFile> listCrmSimUpfiles(Integer comId,
			Integer customerId, Integer upfileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		//客户模块附件
		sql.append("select a.comId,a.customerid,a.upfileId  from customerUpfile a \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and a.customerid=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		sql.append("\n union all \n");
		//客户留言附件
		sql.append("select  a.comId,a.customerid,a.upfileId from feedInfoFile a\n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and a.customerid=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		return this.listQuery(sql.toString(), args.toArray(), FeedInfoFile.class);
	}
	/**
	 * 取得模块统计数据
	 * @param customer 客户查询条件对象
	 * @param operator 当前操作人员
	 * @param isForceInPersion 是否为强制参与人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModStaticVo> listCrmStatis(Customer customer, Integer operator,
			boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//外面再包一层
		sql.append("select count(a.customertypeid) value,b.id type,b.typeName name from ( \n");
		sql.append("select * from ( \n");
		sql.append("	select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,\n");
		sql.append("	case when atten.id is null then 0 else 1 end as attentionState,\n");

		sql.append("	area.areaName,customerType.typeName\n");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("	from customer a \n");
		sql.append("	left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("	left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");

		sql.append("	left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=? \n");
		args.add(operator);
		sql.append("	where a.delstate=0 and  a.comId = ? \n");
		args.add(customer.getComId());
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//客户阶段筛选
		if(null!=customer.getStage() && !"".equals(customer.getStage())){
			this.addSqlWhere(customer.getStage(), sql, args, " and a.stage =  ? \n");
		}
		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(operator);
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(operator);
			args.add(customer.getComId());
			args.add(operator);

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(operator);
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(customer.getComId());
			args.add(operator);
			sql.append(") \n");
		}
		sql.append(")a where 1=1 \n");
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
		//外层结束


		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		//负责人类型
		String ownerType =  customer.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){
				//查询自己的
				this.addSqlWhere(operator, sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){
				//查询下属的
				sql.append(" and exists(select id from myleaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(customer.getComId());
				args.add(operator);
			}
		}
		this.addSqlWhere(customer.getAttentionState(), sql, args, " and a.attentionState=?");
		sql.append("\n )a right join customerType b on a.customertypeid=b.id and a.comId=b.comId");
		sql.append("\n where 1=1 \n");
		this.addSqlWhere(customer.getComId(), sql, args, " and b.comId=?");
		sql.append("\n group by b.id,b.typeName \n");

		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}
	/**
	 * 获取团队客户主键集合
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> listCrmOfAll(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id from customer a where a.comId=?");
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(), Customer.class);
	}

	/**
	 * 取得执行人员的客户数 app
	 * @param userId 指定人员主键
	 * @param comId 企业号
	 * @return
	 */
	public Integer countMyCrm(Integer userId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(a.id) from customer a \n");
		sql.append("where a.delstate=0 and a.comId = ? and a.owner=? \n");
		args.add(comId);
		args.add(userId);
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 统计客户类型
	 * @param customer 查询的客户条件
	 * @param sessionUser 当前操作员
	 * @param isForceInPersion 是否为强制参与人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModStaticVo> listCrmStatisByType(Customer customer,
			UserInfo sessionUser, boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from (");
		sql.append("\n select case when a.dataValue is null then 0 else a.dataValue end value,b.typename name,b.id type from (");
		sql.append("\n select a.customertypeid,count(a.customertypeid) dataValue from customer a ");
		sql.append("\n where a.delstate=0 and  a.comId = ? \n");
		args.add(sessionUser.getComId());
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(sessionUser.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(sessionUser.getId());
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());
			sql.append(") \n");
		}
		//客户类型筛选
	    if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
		  this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
	    }
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
	    if(null != customer.getListOwner() && !customer.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 0 ");
			for(UserInfo owner : customer.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
	    //客户名筛选
  		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
  			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
  		}

		sql.append("\n group by a.customertypeid");
		sql.append("\n )a right join customertype b on a.customertypeid=b.id");
		sql.append("\n where b.comId = ? \n");
		args.add(sessionUser.getComId());
		sql.append("\n order by b.typeorder,b.id");
		sql.append("	 )a where 1=1");
		 //客户类型多选
	    if(null != customer.getListCrmType() && !customer.getListCrmType().isEmpty()){
			sql.append("	 and  ( a.type= 0 ");
			for(CustomerType crmType : customer.getListCrmType()){
				sql.append("or a.type=?  \n");
				args.add(crmType.getId());
			}
			sql.append("	 ) ");
		}
		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}
	/**
	 * 统计客户类型
	 * @param customer 查询的客户条件
	 * @param sessionUser 当前操作员
	 * @param isForceInPersion 是否为强制参与人
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModStaticVo> listCrmAddNumStatis(Customer customer,
			UserInfo sessionUser, boolean isForceInPersion) {
		//当前时间的年月
		String nowDateMonth = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM);

		Date date = new Date();
		int month = Calendar.getInstance().get(Calendar.MONTH);
		String selectYear = customer.getSelectYear();
		if(!selectYear.equals(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy))){
			month = 11;
			nowDateMonth = selectYear+"-"+(month+1);
		}

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select case when value is null then 0 else value end value,b.type type, ");
		sql.append("\n case ");
		String tempMoth = nowDateMonth+"";
		for(int i=0;i<=month;i++){
			sql.append("\n when b.type='"+(i+1)+"' then '"+tempMoth+"' \n");
			tempMoth = DateTimeUtil.addDate(tempMoth, DateTimeUtil.yyyy_MM, Calendar.MONTH, -1);
		}
		sql.append("\n end name \n");
		sql.append("\n from (");
		sql.append("\n select count(a.type) as value,a.type type from (");
		sql.append("\n select a.id,a.customerName,case ");
		tempMoth = nowDateMonth+"";
		for(int i=0;i<=month;i++){
			sql.append("\n when substr(a.recordcreatetime,0,7)='"+tempMoth+"' then "+(i+1)+" \n");
			tempMoth = DateTimeUtil.addDate(tempMoth, DateTimeUtil.yyyy_MM, Calendar.MONTH, -1);
		}
		sql.append("\n end type \n");
		sql.append("\n from customer a");
		sql.append("\n where a.delstate=0 and  a.comId = ? \n");
		args.add(sessionUser.getComId());
		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(sessionUser.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(sessionUser.getId());
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());
			sql.append(") \n");
		}
		//客户类型筛选
	    if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
		  this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
	    }
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
	    if(null != customer.getListOwner() && !customer.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 0 ");
			for(UserInfo owner : customer.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
	    //客户名筛选
  		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
  			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
  		}

		sql.append("\n  )a group by a.type ");
		sql.append("\n  )a right join (");
		for(int i=1;i<=(month+1);i++){
			sql.append("\n select "+i+" type from dual");
			if(i <= month){
				sql.append("\n union ");

			}

		}
		sql.append("\n  )b on a.type=b.type");
		sql.append("\n order by b.type desc");
		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}
	/**
	 * 统计时需要排除总数为0的人员
	 * @param customer
	 * @param sessionUser
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModStaticVo> listOwnerCrmNOStatistic(Customer customer, UserInfo sessionUser,Boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("\n  select case when a.dataValue is null then 0 else a.dataValue end value,b.username name,b.id type from (");
		sql.append("\n  select a.owner,count(a.owner) dataValue from customer a  ");
		sql.append("\n where a.delstate=0 and  a.comId = ? \n");
		args.add(sessionUser.getComId());

		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(sessionUser.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(sessionUser.getId());
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());
			sql.append(") \n");
		}

		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户名筛选
  		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
  			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
  		}
  		if(null != customer.getListOwner() && !customer.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 0 ");
			for(UserInfo owner : customer.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		sql.append("\n group by a.owner");
		sql.append("\n )a right join userinfo b on a.owner=b.id");
		sql.append("\n  where a.dataValue>0 order by value desc");
		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}
	/**
	 * 列出客户负责人的各种客户分类统计
	 * @param customer 当前客户的查询条件
	 * @param userIds
	 * @param sessionUser 当前操作人
	 * @param isForceInPersion 是否为监督人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModStaticVo> listOwnerCrmTypeStatistic(Customer customer, List<Integer> userIds, UserInfo sessionUser,Boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select case when a.dataValue is null then 0 else a.dataValue end value,b.username name,b.id type from (");
		sql.append("\n  select a.owner,count(a.owner) dataValue from customer a  ");
		sql.append("\n where a.delstate=0 and  a.comId = ? and a.customertypeid=? \n");
		args.add(sessionUser.getComId());
		args.add(customer.getCustomerTypeId());
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");

		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(sessionUser.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(sessionUser.getId());
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());
			sql.append(") \n");
		}

		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户名筛选
  		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
  			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
  		}

		sql.append("\n group by a.owner");
		sql.append("\n )a right join userinfo b on a.owner=b.id");
		sql.append("\n inner join userorganic c on b.id=c.userid");
		sql.append("\n where c.comId = ?\n");
		args.add(sessionUser.getComId());

		String order = "";
		if(null!=userIds && !userIds.isEmpty()){
			this.addSqlWhereIn(userIds.toArray(), sql, args, " and b.id in ?");
			for(int i=0;i<userIds.size();i++){
				order = order+"'"+userIds.get(i)+"'," +(i+1)+",";
			}
		}else{
			sql.append("\n and b.id = -1\n");
		}
		if(order.length()>0){
			order  = order.substring(0, order.length()-1);
			sql.append("\n order by DECODE(b.id,"+order+")");

		}else{
			sql.append("\n order by b.id");
		}
		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}
	/**
	 * 统计客户阶段
	 * @param customer
	 * @param sessionUser
	 * @param isForceInPersion
	 * @return
	 */
	public List<ModStaticVo> listCrmStageStatistic(Customer customer, UserInfo sessionUser, Boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select * from (");
		sql.append("\n  select case when a.dataValue is null then 0 else a.dataValue end value,b.typeName name,b.id type from (");
		sql.append("\n  select a.customerTypeId,count(a.customerTypeId) dataValue from customer a  ");
		sql.append("\n where a.delstate=0 and  a.comId = ? and a.stage=? \n");
		args.add(sessionUser.getComId());
		args.add(customer.getStage());
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户名筛选
  		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
  			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
  		}

  		//客户负责人
  		if(null != customer.getListOwner() && !customer.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 0 ");
			for(UserInfo owner : customer.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		//列表查看权限验证
		if(!isForceInPersion){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(sessionUser.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(sessionUser.getId());
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());
			sql.append(") \n");
		}
		sql.append("\n group by a.customerTypeId");
		sql.append("\n )a right join customerType b on a.customerTypeId=b.id ");
		sql.append("\n where b.comId = ?\n");
		args.add(sessionUser.getComId());

		sql.append("\n order by b.typeOrder,a.dataValue desc nulls last");
		sql.append("	 ) a where 1=1");
		 //客户类型多选
	    if(null != customer.getListCrmType() && !customer.getListCrmType().isEmpty()){
			sql.append("	 and  ( a.type= 0 ");
			for(CustomerType crmType : customer.getListCrmType()){
				sql.append("or a.type=?  \n");
				args.add(crmType.getId());
			}
			sql.append("	 ) ");
		}
		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}
	/**
	 * 统计客户更新频率
	 * @param customer 客户的查询条件
	 * @param sessionUser 当前操作人员
	 * @param isForceIn 是否为监督人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModStaticVo> listCrmFrequenStatistic(Customer customer,
			UserInfo sessionUser, Boolean isForceIn) {

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n 	select * from(");
		sql.append("\n select cc.id type,case when bb.value is null then 0 else bb.value end value,cc.typename name from (");
		sql.append("\n select aa.customertypeid,count(aa.customertypeid) value from (");
		sql.append("\n 	select a.*");
		sql.append("\n from (");
		sql.append("\n 	select case when b.recordcreatetime is null then a.recordcreatetime else b.recordcreatetime end recordcreatetime,");
		sql.append("\n a.id,a.comId,a.customername,a.owner,a.areaid,a.recordcreatetime recDateTime,a.customertypeid,");
		sql.append("\n  row_number() over (partition by b.customerId order by b.recordcreatetime desc) as new_order");
		sql.append("\n from customer a left join");
		//最近更新时间
		sql.append("\n(");
		sql.append("\n		  select b.id,b.recordcreatetime,b.busid customerId,b.comId from  busUpdate b inner join ");
		sql.append("\n		  customer a on a.id=b.busid and a.comId=b.comId ");
		sql.append("\n where a.delstate=0 and a.comId=?  and b.bustype = '"+ConstantInterface.TYPE_CRM+"'");
		args.add(sessionUser.getComId());
		sql.append("\n		  )b on a.id=b.customerId and a.comId=b.comId ");
		sql.append("\n where a.delstate=0 and a.comId=?");
		args.add(sessionUser.getComId());
		sql.append("\n )a where a.new_order=1 \n");
		//更新频率时间选择
		this.addSqlWhere(customer.getFrequenStartDate(), sql, args, " and a.recordcreatetime>?");
		this.addSqlWhere(customer.getFrequenEndDate(), sql, args, " and a.recordcreatetime<=?");

		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recDateTime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recDateTime,0,10)<=?");

		//列表查看权限验证
		if(!isForceIn){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(sessionUser.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(sessionUser.getId());
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());
			sql.append(") \n");
		}

		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户名筛选
  		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
  			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
  		}
  	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
	    if(null != customer.getListOwner() && !customer.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 0 ");
			for(UserInfo owner : customer.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		sql.append("\n  )aa where 1=1  ");
		sql.append("\n  group by aa.customertypeid");
		sql.append("\n  ) bb right join customertype cc on bb. customertypeid=cc.id");
		sql.append("\n where 1=1 and cc.comId=?");
		args.add(sessionUser.getComId());
		sql.append("\n order by cc.typeOrder,cc.id");
		sql.append("\n  )a where 1=1  ");
		 //客户类型多选
	    if(null != customer.getListCrmType() && !customer.getListCrmType().isEmpty()){
			sql.append("	 and  ( a.type= 0 ");
			for(CustomerType crmType : customer.getListCrmType()){
				sql.append("or a.type=?  \n");
				args.add(crmType.getId());
			}
			sql.append("	 ) ");
		}
		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}
	/**
	 * 取得全国区域模板用于统计
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Region> listRegionForStatistic(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.parentId,a.regionLevel,");
		sql.append("\n  case when INSTR(a.regionName,'省')>0 then replace(a.regionName, '省', '')");
		sql.append("\n  when INSTR(a.regionName,'市')>0 then replace(a.regionName, '市', '')");
		sql.append("\n  when INSTR(a.regionName,'广西')>0 then '广西'");
		sql.append("\n  when INSTR(a.regionName,'新疆')>0 then '新疆'");
		sql.append("\n   when INSTR(a.regionName,'宁夏')>0 then '宁夏'");
		sql.append("\n  when INSTR(a.regionName,'自治区')>0 then replace(a.regionName, '自治区', '')");
		sql.append("\n   else  a.regionName  end regionName from (");
		sql.append("\n  select id,comId,regionCode,regionName,parentId,regionLevel,regionOrder,regionPingying,regionShortpy,");
		sql.append("\n  connect_by_isleaf as isLeaf,level levelA");
		sql.append("\n  from region start with parentid=-1 CONNECT BY PRIOR id = parentid");
		sql.append("\n  )a where a.levelA=2 order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), Region.class);
	}
	/**
	 * 取得全国区域模板用于统计
	 * @param sessionUser
	 * @param regionId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Area> listTreeArea(UserInfo sessionUser,List<Integer> regionId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.areaName,a.parentId,a.id,connect_by_isleaf as isLeaf,level,rownum neworder from area a");
		sql.append("\n where 1=1 start with regionid ");
		this.addSqlWhereIn(regionId.toArray(), sql, args, " in ?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and comId=?");
		sql.append("\n connect by prior id = parentid");
		sql.append("\n order siblings by a.areaorder,a.id");
		return this.listQuery(sql.toString(), args.toArray(), Area.class);
	}
	/**
	 * 取得区域的所有子区域主键
	 * @param userInfo 当前操作员
	 * @param regionId 选中的区域模板主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Area> listRegionArea(UserInfo userInfo,Integer regionId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,connect_by_isleaf as isLeaf,level from area a ");
		sql.append("\n where 1=1");
		sql.append("\n start with regionid="+regionId+" and comId="+userInfo.getComId());
		sql.append("\n connect by prior id = parentid");
		sql.append("\n order siblings by a.areaorder,a.id");
		return this.listQuery(sql.toString(), args.toArray(), Area.class);
	}
	/**
	 * 取得区域的所有子区域主键
	 * @param sessionUser 当前操作员
	 * @param regionId 选中的区域模板主键
	 * @param isForceIn
	 * @param customer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Area> listRegionAreaCrmCount(UserInfo sessionUser,List<Integer> regionId,
			Customer customer, boolean isForceIn){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,case when b.value is null then 0 else b.value end crmSum from (");
		sql.append("\n select a.*,connect_by_isleaf as isLeaf,level,rownum neworder from area a");
		sql.append("\n where 1=1 start with regionid ");
		this.addSqlWhereIn(regionId.toArray(), sql, args, " in ?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and comId=?");
		sql.append("\n connect by prior id = parentid");
		sql.append("\n order siblings by a.areaorder,a.id");
		sql.append("\n ) a left join (");
		sql.append("\n select a.areaid,count(a.id) value");
		sql.append("\n from customer a where a.delstate=0");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and comId=?");
		//查询创建时间段
				this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.RECORDCREATETIME,0,10)>=?");
				this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.RECORDCREATETIME,0,10)<=?");

				//列表查看权限验证
				if(!isForceIn){
					//如果不是督察人员则验证权限
					sql.append("and (\n");
					//任务的参与、执行、负责权限验证
					sql.append(" a.owner =? \n");
					args.add(sessionUser.getId());
					//上级权限验证
					//参与人上级权限验证
					sql.append(" or exists( \n");
					sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
					sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
					sql.append(" ) \n");
					args.add(sessionUser.getId());
					args.add(sessionUser.getComId());
					args.add(sessionUser.getId());

					//负责人上级权限验证
					sql.append(" or exists( \n");
					sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
					args.add(sessionUser.getId());
					args.add(sessionUser.getComId());
					sql.append(" )\n");
					//项目成员组权限验证
					sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
					args.add(sessionUser.getComId());
					args.add(sessionUser.getId());
					sql.append(") \n");
				}
			    //客户名筛选
		  		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
		  			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		  		}
		  	    //客户负责人筛选
			    if(null!=customer.getOwner() && customer.getOwner() !=0){
					  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
			    }
			    if(null != customer.getListOwner() && !customer.getListOwner().isEmpty()){
					sql.append("	 and  ( a.owner= 0 ");
					for(UserInfo owner : customer.getListOwner()){
						sql.append("or a.owner=?  \n");
						args.add(owner.getId());
					}
					sql.append("	 ) ");
				}
		sql.append("\n group by a.areaid");
		sql.append("\n )b on a.id=b.areaid");
		sql.append("\n order by neworder asc");
		return this.listQuery(sql.toString(), args.toArray(), Area.class);
	}
	/**
	 * 取得区域的所有子区域主键
	 * @param userInfo 当前操作员
	 * @param areaId 选中的区域模板主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Area> listSelectArea(UserInfo userInfo,Integer areaId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,connect_by_isleaf as isLeaf,level from area a ");
		sql.append("\n where 1=1");
		sql.append("\n start with id="+areaId+" and comId="+userInfo.getComId());
		sql.append("\n connect by prior id = parentid");
		sql.append("\n order siblings by a.areaorder,a.id");
		return this.listQuery(sql.toString(), args.toArray(), Area.class);
	}
	/**
	 * 取得团队已导入的团队
	 * @param comId 团队主键
	 * @param level
	 * @param regionId 区域主键
	 * @param level
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Region> getHasInRegions(Integer comId, Integer regionId, Integer level) {
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,level regionLevel from (");
		sql.append("\n select * from (");
		sql.append("\n select a.id,a.parentid,a.regionname,needLeafMod needMod, level lev,areaId,a.regioncode,");
		sql.append("\n sum(nvl(needLeafMod, 0)) over(partition by connect_by_root(a.id)) needLeafMod");
		sql.append("\n from (");
		sql.append("\n select a.*,case when b.id is null then 1 else 0 end needLeafMod,");
		sql.append("\n case when b.id is null then 0 else b.id end areaId");
		sql.append("\n from region a left join area b on a.id=b.regionid and b.comId="+comId);
		sql.append("\n  )a  connect by prior a.id = a.parentid");
		sql.append("\n  )a  where lev=1  and (needMod>=1 or needLeafMod>=1) ");

		if(level==-1){//省份从模板导入
			sql.append("\n )a start with a.parentid = 1");
		}else if(level==0){//从市级导入数据
			//取得团队已导入的市级区域
			sql.append("\n )a start with a.id = "+regionId);
		}else if(level==1){//从县级导入数据
			//取得团队已导入的县级区域
			sql.append("\n )a start with a.id = "+regionId);
		}

		sql.append("\n  connect by prior id = parentid");
		sql.append("\n   order siblings by regioncode");
		return this.listQuery(sql.toString(), null,Region.class);
	}
	/*************************************统计下属负责客户数量**********************************************/
	/**
	 * 统计下属负责客户数量
	 * @param userInfo
	 * @param customer 客户
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listSubCrmNum(UserInfo userInfo,Customer customer){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//更新频率开始时间
		String frequenStartDate = customer.getFrequenStartDate();
		//更新频率结束时间
		String frequenEndDate = customer.getFrequenEndDate();

		boolean isSearchFreque = false;
		if((null!= frequenStartDate && !"".equals(frequenStartDate))||
				(null!= frequenEndDate && !"".equals(frequenEndDate))){//只要有一个满足就需要查询
			isSearchFreque = true;
		}

		sql.append(" select * from(\n");
		sql.append(" select  c.id,c.username,case when a.crmnum is null then 0 else a.crmnum end myCrmNum,c.gender,e.uuid  smImgUuid  from(\n");
		sql.append(" select owner,count(id) crmnum from (\n");
		sql.append(" select owner,a.id,  \n");
		//sql.append(" case when atten.id is null then 0 else 1 end as attentionState, ");
		//newsInfo没有数据的，同一默认一个更新日期
		sql.append("	case when newsInfo.recordcreatetime is null then a.recordcreatetime else newsInfo.recordcreatetime end as modifyTime  \n");
		if(isSearchFreque){
			sql.append("	from ( \n");
			sql.append("	select a.*,row_number() over (partition by b.customerId order by b.recordcreatetime desc) as new_order, \n");
			sql.append("	case when b.recordcreatetime is null then a.recordcreatetime else b.recordcreatetime end frequetime \n");
			sql.append("\n from customer a left join ");
			//最近更新时间
			sql.append("\n(");
			sql.append("\n		  select b.id,b.recordcreatetime,b.busid customerId,b.comId from  busUpdate b inner join ");
			sql.append("\n		  customer a on a.id=b.busid and a.comId=b.comId ");
			sql.append("\n where a.delstate=0 and a.comId=?  and b.bustype = '"+ConstantInterface.TYPE_CRM+"'");
			args.add(customer.getComId());
			sql.append("\n		  )b on a.id=b.customerId and a.comId=b.comId ");
			sql.append("\n where a.delstate=0 and a.comId=?");
			args.add(customer.getComId());
			sql.append("\n )a \n");
		}else{
			sql.append("	from customer a \n");
		}
		sql.append(" 	left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" 		and a.id = newsInfo.busId and newsInfo.busType="+ConstantInterface.TYPE_CRM+"\n");

		if(isSearchFreque){
			sql.append("	where a.new_order=1 and a.delstate=0 and  a.comId = ? \n");
			args.add(userInfo.getComId());
			//更新频率时间选择
			this.addSqlWhere(customer.getFrequenStartDate(), sql, args, " and a.frequetime>?");
			this.addSqlWhere(customer.getFrequenEndDate(), sql, args, " and a.frequetime<=?");
		}else{
			sql.append("	where a.delstate=0 and  a.comId = ? \n");
			args.add(userInfo.getComId());
		}
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//客户类型筛选
		if(null!=customer.getCustomerTypeId()&& customer.getCustomerTypeId() !=0){
		  this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customertypeid=? \n");
		}
		//客户阶段筛选
		if(null!=customer.getStage() && !"".equals(customer.getStage())){
			this.addSqlWhere(customer.getStage(), sql, args, " and a.stage =  ? \n");
		}
		//客户区域筛选
		if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
		sql.append("and a.areaid in ( select id from area \n");
		sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
		sql.append(")\n");
		}
		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		//关注
		if(customer.getAttentionState() != null && customer.getAttentionState().equals("1")){
			sql.append("\n and exists (");
			sql.append("\n select id from attention atten where atten.comId = a.comId and atten.busid = a.id and atten.bustype='"+ConstantInterface.TYPE_CRM+"' and atten.userId=?");
			args.add(userInfo.getId());
			sql.append(")");
		}

		sql.append(" )group by owner )a \n");
		sql.append(" right join userinfo c on a.owner = c.id\n");
		sql.append(" left join userOrganic cc on c.id =cc.userId and ?=cc.comId\n");
		args.add(userInfo.getComId());


		sql.append(" left join upfiles e on cc.mediumHeadPortrait = e.id\n");
		sql.append(" where cc.enabled='1' \n");
		sql.append(" and exists (\n");
		sql.append(" select id from myLeaders where creator = c.id and leader = ? and comId = ? creator <> leader \n");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		sql.append(" )order by myCrmNum desc,c.id\n");
		sql.append(" )where 1=1 and rownum<=10\n");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 查询客户阶段列表
	 * @param comId
	 * @return
	 */
	public List<CustomerStage> listCustomerStage(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from customerStage a where a.comId=? ");
		args.add(comId);
		sql.append(" order by a.orderNum asc");
		return this.listQuery(sql.toString(), args.toArray(),CustomerStage.class);
	}
	/**
	 * 查询客户阶段最大排序
	 * @param comId
	 * @return
	 */
	public CustomerStage queryCrmStageOrderMax(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.orderNum)+1  as orderNum from customerStage a where a.comId =?");
		args.add(comId);
		return (CustomerStage)this.objectQuery(sql.toString(), args.toArray(),CustomerStage.class);
	}
	/**
	 * 查询处于某阶段的客户数量
	 * @param stageId
	 * @param userInfo
	 * @return
	 */
	public Integer countCustomerByStage(Integer stageId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from customer where stage = ? and comId = ?");
		args.add(stageId);
		args.add(userInfo.getComId());
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 取得模块统计数据
	 * @param customer 客户查询条件对象
	 * @param operator 当前操作人员
	 * @param isForceIn 是否为强制参与人
	 * @return
	 */
	public List<ModStaticVo> listCrmStatisByBudget(Customer customer, Integer operator, boolean isForceIn) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select type,name,case when value is null then 0 else value end value from ( \n");
		//外面再包一层
		sql.append("select sum(a.budget) value,b.id type,b.typeName name from ( \n");
		sql.append("select * from ( \n");
		sql.append("	select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,a.budget,\n");

		sql.append("	area.areaName,customerType.typeName\n");
		sql.append("	from customer a \n");
		sql.append("	left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("	left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");
		sql.append("	where a.delstate=0 and  a.comId = ? \n");
		args.add(customer.getComId());
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//客户阶段筛选
		if(null!=customer.getStage() && !"".equals(customer.getStage())){
			this.addSqlWhere(customer.getStage(), sql, args, " and a.stage =  ? \n");
		}
		//列表查看权限验证
		if(!isForceIn){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(operator);
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(operator);
			args.add(customer.getComId());
			args.add(operator);

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(operator);
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(customer.getComId());
			args.add(operator);
			sql.append(") \n");
		}
		sql.append(")a where 1=1 \n");
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
		//外层结束


		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		//负责人类型
		String ownerType =  customer.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){//查询自己的
				this.addSqlWhere(operator, sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){//查询下属的
				sql.append(" and exists(select id from myleaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(customer.getComId());
				args.add(operator);
			}
		}
		sql.append("\n )a right join customerType b on a.customertypeid=b.id and a.comId=b.comId");
		sql.append("\n where 1=1 \n");
		this.addSqlWhere(customer.getComId(), sql, args, " and b.comId=?");
		sql.append("\n group by b.id,b.typeName \n");
		sql.append("\n )a \n");
		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}

	/**
	 * 统计客户资金预算
	 * @param customer
	 * @param sessionUser
	 * @param isForceIn
	 * @return
	 */
	public List<ModStaticVo> listCrmBudgetStatistic(Customer customer, UserInfo sessionUser, Boolean isForceIn) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select type,name,case when value is null then 0 else value end value from ( \n");
		//外面再包一层
		sql.append("select sum(a.budget) value,b.id type,b.stageName name from ( \n");
		sql.append("select * from ( \n");
		sql.append("	select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,a.budget,customerStage.id stage\n");
		sql.append("	,customerType.typeName,customerStage.stageName \n");
		sql.append("	from customer a \n");
		sql.append("	left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("	left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");
		sql.append("	left join customerStage  on a.comId = customerStage.comId and a.stage = customerStage.id \n");
		sql.append("	where a.delstate=0 and  a.comId = ? \n");
		args.add(sessionUser.getComId());
		
		//类型
		this.addSqlWhere(customer.getCustomerTypeId(), sql, args, " and a.customerTypeId =  ? \n");
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//客户阶段筛选
		this.addSqlWhere(customer.getStage(), sql, args, " and a.stage =  ? \n");
		//列表查看权限验证
		if(!isForceIn){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(sessionUser.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());
			
			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(sessionUser.getId());
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());
			sql.append(") \n");
		}
		sql.append(")a where 1=1 \n");
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		
		 //客户类型多选
	    if(null != customer.getListCrmType() && !customer.getListCrmType().isEmpty()){
			sql.append("	 and  ( a.customerTypeId= 0 ");
			for(CustomerType crmType : customer.getListCrmType()){
				sql.append("or a.customerTypeId=?  \n");
				args.add(crmType.getId());
			}
			sql.append("	 ) ");
		}
		 //负责人筛选
	    if(null != customer.getListOwner() && !customer.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 0 ");
			for(UserInfo owner : customer.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		sql.append("\n )a  right join customerStage b on a.stage=b.id and a.comId=b.comId");
		sql.append("\n where 1=1 \n");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and b.comId=?");
		sql.append("\n group by b.id,b.stageName \n");
		sql.append("\n )a \n");
		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}
	/**
	 * 统计阶段客户数量
	 * @param customer
	 * @param sessionUser
	 * @param isForceIn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModStaticVo> listStageCrmNum(Customer customer, UserInfo sessionUser, Boolean isForceIn) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select type,name,case when value is null then 0 else value end value from ( \n");
		//外面再包一层
		sql.append("select count(a.id) value,b.id type,b.stageName name  from ( \n");
		sql.append("select * from ( \n");
		sql.append("	select a.comId,a.id,a.customerName,a.owner,a.recordcreatetime,a.areaId,a.customerTypeId,a.budget,customerStage.id stage\n");
		sql.append("	,customerType.typeName,customerStage.stageName \n");
		sql.append("	from customer a \n");
		sql.append("	left join area  on a.comId = area.comId and a.areaId = area.id \n");
		sql.append("	left join customerType  on a.comId = customerType.comId and a.customerTypeId = customerType.id \n");
		sql.append("	left join customerStage  on a.comId = customerStage.comId and a.stage = customerStage.id \n");
		sql.append("	where a.delstate=0 and  a.comId = ? \n");
		args.add(sessionUser.getComId());
		//客户区域筛选
	    if(null!=customer.getAreaId()&& customer.getAreaId() !=0){
	    	sql.append("and a.areaid in ( select id from area \n");
	    	sql.append("start with id="+customer.getAreaId()+" connect by prior id = parentid \n");
	    	sql.append(")\n");
	    }
	    //客户负责人筛选
	    if(null!=customer.getOwner() && customer.getOwner() !=0){
			  this.addSqlWhere(customer.getOwner(), sql, args, " and a.owner=? \n");
	    }
		//客户名筛选
		if(null!=customer.getCustomerName() && !"".equals(customer.getCustomerName())){
			this.addSqlWhereLike(customer.getCustomerName(), sql, args, " and a.customerName like ? \n");
		}
		 //客户类型多选
	    if(null != customer.getListCrmType() && !customer.getListCrmType().isEmpty()){
			sql.append("	 and  ( a.customerTypeId= 0 ");
			for(CustomerType crmType : customer.getListCrmType()){
				sql.append("or a.customerTypeId=?  \n");
				args.add(crmType.getId());
			}
			sql.append("	 ) ");
		}
	    if(null != customer.getListOwner() && !customer.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 0 ");
			for(UserInfo owner : customer.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		//查询创建时间段
		this.addSqlWhere(customer.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(customer.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		//列表查看权限验证
		if(!isForceIn){
			//如果不是督察人员则验证权限
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? \n");
			args.add(sessionUser.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from customerSharer b where a.id = b.customerId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());
			
			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append("	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(sessionUser.getId());
			args.add(customer.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInCustomerShareGroup(?,a.id,?) > 0 \n");
			args.add(sessionUser.getComId());
			args.add(sessionUser.getId());
			sql.append(") \n");
		}
		sql.append(")a where 1=1 \n");
		sql.append("\n )a  right join customerStage b on a.stage = b.id and a.comId=b.comId");
		sql.append("\n where 1=1 \n");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and b.comId=?");
		sql.append("\n group by b.id,b.stageName \n");
		sql.append("\n )a  where 1=1 \n");
		//客户阶段筛选
		this.addSqlWhere(customer.getStage(), sql, args, " and a.type =  ? \n");
		return this.listQuery(sql.toString(), args.toArray(), ModStaticVo.class);
	}
	/**
	 * 
	 * @param userInfo
	 * @param statisticCrmVo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StatisticCrmVo> listPagedStatisticCrm(UserInfo userInfo,
			StatisticCrmVo statisticCrmVo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select a.* from (");
		sql.append("\n select a.crmId,a.crmName,a.crmTypeId,a.crmTypeName,");
		sql.append("\n 	a.typeOrder,a.owner,a.ownerName,a.modifyPeriod,a.lastUpdateDate,");
		sql.append("\n 	(sysdate - to_date(a.lastUpdateDate,'yyyy-MM-dd HH24:mi:ss'))* 24 * 60 * 60 * 1000 lastTimes,");
		
		sql.append("\n case when a.modifyPeriod > 0 then");
		sql.append("\n 	case when (sysdate - to_date(a.lastUpdateDate,'yyyy-MM-dd HH24:mi:ss')) - a.modifyPeriod > 15 then 1");
		sql.append("\n 		when (sysdate - to_date(a.lastUpdateDate,'yyyy-MM-dd HH24:mi:ss')) - a.modifyPeriod> 7 then 2");
		sql.append("\n 		when (sysdate - to_date(a.lastUpdateDate,'yyyy-MM-dd HH24:mi:ss')) - a.modifyPeriod > 3 then 3");
		sql.append("\n 		when (sysdate - to_date(a.lastUpdateDate,'yyyy-MM-dd HH24:mi:ss')) - a.modifyPeriod> 1 then 4");
		sql.append("\n 		else 5 end");
		sql.append("\n 	else 5 end overTimeLevel");
		
		sql.append("\n   ");
		sql.append("\n 	from (");
		sql.append("\n 		select a.id crmId,a.customername crmName,a.customerTypeId crmTypeId,b.typeName crmTypeName,");
		sql.append("\n			b.typeOrder,a.owner,c.username ownerName,b.modifyPeriod,");
		//最近更新时间
		sql.append("\n 		case when crmFeedbackDate.recordcreatetime is null then crmHandOverDate.recordcreatetime");
		sql.append("\n 			when to_date(crmFeedbackDate.recordcreatetime,'yyyy-MM-dd HH24:mi:ss')<=");
		sql.append("\n 			to_date(crmHandOverDate.recordcreatetime,'yyyy-MM-dd HH24:mi:ss')");
		sql.append("\n 			then crmHandOverDate.recordcreatetime else crmFeedbackDate.recordcreatetime end lastUpdateDate");
		
		sql.append("\n		from customer a");
		sql.append("\n 		inner join customerType b on a.customerTypeId=b.id");
		sql.append("\n 		inner join userinfo c on a.owner=c.id");
		//客户移交最近的时间
		sql.append("\n 		left join (");
		sql.append("\n 			select * from (");
		sql.append("\n				select a.customerid,a.recordcreatetime,crm.owner,row_number() over (partition by a.customerid order by a.id desc) as new_order");
		sql.append("\n  			from customerhandover a ");
		sql.append("\n 				inner join customer crm on a.customerid=crm.id and a.touser=crm.owner");
		sql.append("\n  			where 1=1 and a.comId=?");
		args.add(userInfo.getComId());
		sql.append("\n 			) a where new_order=1");
		sql.append("\n 		)crmHandOverDate on crmHandOverDate.customerid=a.id");
		//客户反馈最近时间
		sql.append("\n 		left join (");
		sql.append("\n 			select * from (");
		sql.append("\n				select a.customerid,a.recordcreatetime,crm.owner,row_number() over (partition by a.customerid order by a.id desc) as new_order");
		sql.append("\n  			from feedBackInfo a ");
		sql.append("\n 				inner join customer crm on a.customerid=crm.id and a.userId=crm.owner");
		sql.append("\n  			where 1=1 and a.comId=?");
		args.add(userInfo.getComId());
		sql.append("\n 			) a where new_order=1");
		sql.append("\n 		)crmFeedbackDate on crmFeedbackDate.customerid=a.id");
		sql.append("\n 		where 1=1 and a.comId=?");
		args.add(userInfo.getComId());
		this.addSqlWhere(statisticCrmVo.getCrmTypeId(), sql, args, "\n and a.customerTypeId=?");
		
		//负责人查询
		List<UserInfo> listOwner = statisticCrmVo.getListOwner();
		if(null!=listOwner && !listOwner.isEmpty()){
			List<Integer> ownerIds = new ArrayList<Integer>(listOwner.size());
			for (UserInfo owner : listOwner) {
				ownerIds.add(owner.getId());
			}
			Integer[] ownerId = new Integer[listOwner.size()];
			this.addSqlWhereIn(ownerIds.toArray(ownerId), sql, args, "\n and a.owner in ?");
		}
		//客户名称模糊查询
		this.addSqlWhereLike(statisticCrmVo.getCrmName(), sql, args, "\n and a.customername like ?");
		
		sql.append("\n 	) a  where 1=1");
		sql.append("\n ) a  where 1=1");
		this.addSqlWhere(statisticCrmVo.getOverTimeLevel(), sql, args, "\n and a.overTimeLevel=?");
		return this.pagedQuery(sql.toString(), " a.typeOrder nulls last,a.crmTypeId,a.lastTimes asc", args.toArray(), StatisticCrmVo.class);
	}
	
	/**
	 * 客户催办
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Customer quereCrmForOverTime(UserInfo userInfo,Integer crmId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n 		select a.id,a.recordcreatetime,a.customername,a.customerTypeId,b.typeName,");
		sql.append("\n			b.typeOrder,a.owner,c.username ownerName,b.modifyPeriod,");
		//最近更新时间
		sql.append("\n 		case when crmFeedbackDate.recordcreatetime is null then crmHandOverDate.recordcreatetime");
		sql.append("\n 			when to_date(crmFeedbackDate.recordcreatetime,'yyyy-MM-dd HH24:mi:ss')<=");
		sql.append("\n 			to_date(crmHandOverDate.recordcreatetime,'yyyy-MM-dd HH24:mi:ss')");
		sql.append("\n 			then crmHandOverDate.recordcreatetime else crmFeedbackDate.recordcreatetime end modifyTime");
		
		sql.append("\n		from customer a");
		sql.append("\n 		inner join customerType b on a.customerTypeId=b.id");
		sql.append("\n 		inner join userinfo c on a.owner=c.id");
		//客户移交最近的时间
		sql.append("\n 		left join (");
		sql.append("\n 			select * from (");
		sql.append("\n				select a.customerid,a.recordcreatetime,crm.owner,row_number() over (partition by a.customerid order by a.id desc) as new_order");
		sql.append("\n  			from customerhandover a ");
		sql.append("\n 				inner join customer crm on a.customerid=crm.id and a.touser=crm.owner");
		sql.append("\n  			where 1=1 and a.comId=? and a.customerId=?");
		args.add(userInfo.getComId());
		args.add(crmId);
		sql.append("\n 			) a where new_order=1");
		sql.append("\n 		)crmHandOverDate on crmHandOverDate.customerid=a.id");
		//客户反馈最近时间
		sql.append("\n 		left join (");
		sql.append("\n 			select * from (");
		sql.append("\n				select a.customerid,a.recordcreatetime,crm.owner,row_number() over (partition by a.customerid order by a.id desc) as new_order");
		sql.append("\n  			from feedBackInfo a ");
		sql.append("\n 				inner join customer crm on a.customerid=crm.id and a.userId=crm.owner");
		sql.append("\n  			where 1=1 and a.comId=? and a.customerId=?");
		args.add(userInfo.getComId());
		args.add(crmId);
		sql.append("\n 			) a where new_order=1");
		sql.append("\n 		)crmFeedbackDate on crmFeedbackDate.customerid=a.id");
		sql.append("\n 		where 1=1 and a.comId=? and a.id=?");
		args.add(userInfo.getComId());
		args.add(crmId);
		return (Customer) this.objectQuery(sql.toString(), args.toArray(), Customer.class);
	}
	
	/**
	 * 查询客户催办人员
	 * @param userInfo 当前操作人员
	 * @param crmId 客户主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusRemindUser> listCrmRemindExecutor(UserInfo userInfo,
			Integer crmId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.owner userId,b.username");
		sql.append("\n from customer a ");
		sql.append("\n inner join userinfo b on a.owner = b.id ");
		sql.append("\n inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n where a.id = ? and a.comId = ? and a.owner <> ? ");//执行中和待认领的
		args.add(crmId);
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		return this.listQuery(sql.toString(), args.toArray(), BusRemindUser.class);
	}
	
	/**
	 * 查询客户维护记录本次需要推送的人员
	 * @param customerId 客户主键
	 * @param comId 团队号
	 * @param pushUserIdSet 本次推送的人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPushTodoUserForCustomer(Integer customerId, Integer comId, Set<Integer> pushUserIdSet){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享负责人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName ");
		sql.append("\n from customer d ");
		sql.append("\n inner join userinfo b on d.owner=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=d.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(customerId, sql, args, " and d.id=?");

		sql.append("\n union");
		//分享关注人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
		sql.append("\n from customer d inner join attention a on a.busId=d.id and d.comId=a.comId and a.busType = " + ConstantInterface.TYPE_CRM + "");
		sql.append("\n inner join userinfo b on d.owner=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=a.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(customerId, sql, args, " and d.id=?");
		//本次推送的人员
		if(null!=pushUserIdSet && !pushUserIdSet.isEmpty()){
			sql.append("\n union");
			sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
			sql.append("\n from userinfo b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhereIn(pushUserIdSet.toArray(new Integer[pushUserIdSet.size()]), sql, args, "\n and b.id in ?");
		}
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
}
