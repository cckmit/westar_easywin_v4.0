package com.westar.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.Item;
import com.westar.base.model.ItemLog;
import com.westar.base.model.ItemSharer;
import com.westar.base.model.ItemTalk;
import com.westar.base.model.ItemTalkFile;
import com.westar.base.model.ItemUpfile;
import com.westar.base.model.Product;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.StagedInfo;
import com.westar.base.model.StagedItem;
import com.westar.base.model.Task;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.ItemStagedInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.web.PaginationContext;

@Repository
public class ItemDao extends BaseDao {
	
	/**
	 * 获取项目列表
	 * @param item
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item>  listItem(Item item,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select allRow.* from ( \n");
		sql.append("select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username as ownerName,c.gender,d.uuid,d.filename, \n");
		sql.append("case when a.modifyDate is null then a.recordcreatetime else a.modifyDate end modifyDate,a.amount,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as readstate, \n");
		
		sql.append("case when atten.id is null then 0 else 1 end as attentionState, \n");
		//更新人信息
		sql.append("newsInfo.content modifyContent,modifier.id modifier,modifier.gender modifierGender,modifier.username as modifierName,modifierFile.uuid modifierUuid,modifierFile.filename modifierFileName \n");
		sql.append("from item a  \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ITEM+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		//获取更新人信息
		sql.append(" left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" and a.id = newsInfo.busId and newsInfo.busType="+ConstantInterface.TYPE_ITEM+"\n");
		sql.append(" left join userinfo modifier on newsInfo.Userid = modifier.id  \n");
		sql.append(" left join userOrganic modifierOrg on modifier.id = modifierOrg.userId and a.comId = modifierOrg.comId \n");
		sql.append(" left join upfiles modifierFile on modifierOrg.mediumHeadPortrait = modifierFile.id \n");
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_ITEM+"' and today.userId=?  \n ");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//查看范围限制
		if(!isForceInPersion){
			sql.append("and ((\n");
			//任务的参与、执行、负责权限验证
			sql.append("a.owner =? \n");
			args.add(userInfo.getId());
			sql.append(" or a.developLeader =? \n");
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from itemsharer b where a.id = b.itemId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			
			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//研发负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.developLeader and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInItemShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
			
			//查询公开项目开始
			sql.append("or a.id in(SELECT id from item where delstate=0 and  comId = ? and pubState = 1) \n");
			args.add(userInfo.getComId());
			sql.append(") \n");
			//查询公开项目结束
		}
		//项目状态筛选
		if(null!=item.getState() && item.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		//项目负责人筛选
		if(null!=item.getOwner() && item.getOwner()!=0){
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}
		
		//项目名称筛选
		if(null!=item.getItemName() && !"".equals(item.getItemName())){
			this.addSqlWhereLike(item.getItemName(),sql,args," and a.itemname like ? \n");
		}
		//产品查询
		List<Product> listProduct = item.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and a.productId in ?");
		}
		
		//负责人类型
		String ownerType =  item.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		if(null != item.getListOwner() && !item.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 1 ");
			for(UserInfo owner : item.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}

		//整个数据源小括号包含
		sql.append(") allRow \n");
		sql.append("where 1=1 \n");
		
		this.addSqlWhere(item.getAttentionState(), sql, args, " and allRow.attentionState=?");
		
		//不查询页面已有数据
		String itemIds = item.getItemIds();
		if(null!=itemIds && !"".equals(itemIds)){
			sql.append("\n and allRow.id not in ("+itemIds+") ");
		}
		return this.pagedQuery(sql.toString(), "allRow.readstate,allRow.modifyDate desc,allRow.id desc", args.toArray(), Item.class);
	}
	/**
	 * 获取项目列表
	 * @param item
	 * @return
	 */
	public PageBean<Item>  listPagedItemForDemand(Item item,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select allRow.* from ( \n");
		sql.append("select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username as ownerName, \n");
		sql.append("case when a.modifyDate is null then a.recordcreatetime else a.modifyDate end modifyDate,\n");
		//产品信息
		sql.append("a.productId,p.name as productName\n");
		sql.append("from item a  \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		
		sql.append("inner join product p on a.productId=p.id and a.comId=p.comId and p.state<>3 \n");
		
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//项目状态筛选
		if(null!=item.getState() && item.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		//项目负责人筛选
		if(null!=item.getOwner() && item.getOwner()!=0){
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}
		
		//项目名称筛选
		if(null!=item.getItemName() && !"".equals(item.getItemName())){
			this.addSqlWhereLike(item.getItemName(),sql,args," and a.itemname like ? \n");
		}
		
		//产品查询
		List<Product> listProduct = item.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and a.productId in ?");
		}
		//负责人类型
		String ownerType =  item.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		if(null != item.getListOwner() && !item.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner = 0 ");
			for(UserInfo owner : item.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		
		//整个数据源小括号包含
		sql.append(") allRow \n");
		sql.append("where 1=1 \n");
		return this.pagedBeanQuery(sql.toString(), "allRow.modifyDate desc,allRow.id desc", args.toArray(), Item.class);
	}
	/***
	 * 获取项目集合（不分页）
	 * @param item
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemOfAll(Item item,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username as ownerName,d.uuid, \n");
		sql.append("case when a.modifyDate is null then a.recordcreatetime else a.modifyDate end modifyDate,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as readstate, \n");
		
		sql.append("case when atten.id is null then 0 else 1 end as attentionState  \n");
		sql.append("from item a  \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ITEM+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_ITEM+"' and today.userId=?  \n ");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//查看范围限制
		if(!isForceInPersion){
			sql.append("and ((\n");
			//任务的参与、执行、负责权限验证
			sql.append("a.owner =? or a.developLeader=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from itemsharer b where a.id = b.itemId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			
			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//研发负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.developLeader and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInItemShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
			
			//查询公开项目开始
			sql.append("or a.id in(SELECT id from item where delstate=0 and  comId = ? and pubState = 1) \n");
			args.add(userInfo.getComId());
			sql.append(") \n");
			//查询公开项目结束
		}
		//项目状态筛选
		if(null!=item.getState() && item.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		//项目负责人筛选
		if(null!=item.getOwner() && item.getOwner()!=0){
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}
		
		//项目名称筛选
		if(null!=item.getItemName() && !"".equals(item.getItemName())){
			this.addSqlWhereLike(item.getItemName(),sql,args," and a.itemname like ? \n");
		}
		//产品查询
		List<Product> listProduct = item.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and a.productId in ?");
		}
	
		//负责人类型
		String ownerType =  item.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		sql.append("order by readstate,a.modifyDate desc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Item.class);
	}
	/**
	 * 获取团队项目主键集合
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemOfAll(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id from item a where a.comId=?");
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(),args.toArray(),Item.class);
	}

	/**
	 * 获取项目列表
	 * @param item
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemForRelevance(Item item,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select allRow.* from ( \n");
		sql.append("select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state, \n");
		sql.append("case when a.state=4 then -1 else a.state end as stateSelect,\n");
		sql.append("c.username as ownerName,c.gender,d.uuid,d.filename \n");
		sql.append("from item a  \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");

		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//查看范围限制
		if(!isForceInPersion){
			sql.append("and ((\n");
			//任务的参与、执行、负责权限验证
			sql.append("a.owner =? or a.developLeader=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from itemsharer b where a.id = b.itemId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//研发负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.developLeader and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInItemShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");

			//查询公开项目开始
			sql.append("or a.id in(SELECT id from item where delstate=0 and  comId = ? and pubState = 1) \n");
			args.add(userInfo.getComId());
			sql.append(") \n");
			//查询公开项目结束
		}
		//项目状态筛选
		if(null!=item.getState() && item.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		//项目负责人筛选
		if(null!=item.getOwner() && item.getOwner()!=0){
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}

		//项目名称筛选
		if(null!=item.getItemName() && !"".equals(item.getItemName())){
			this.addSqlWhereLike(item.getItemName(),sql,args," and a.itemname like ? \n");
		}
		//产品查询
		List<Product> listProduct = item.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and a.productId in ?");
		}

		//整个数据源小括号包含
		sql.append(") allRow where 1=1 and  allRow.stateSelect>-1\n");
		return this.pagedQuery(sql.toString(), "allRow.id desc", args.toArray(), Item.class);
	}
	/***
	 * 获取个人权限下的本月新增（不分页）
	 * @param item
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemAddByMonthOfAll(Item item,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username as ownerName,d.uuid, \n");
		sql.append("case when a.modifyDate is null then a.recordcreatetime else a.modifyDate end modifyDate,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as readstate, \n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState  \n");
		sql.append("from item a  \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ITEM+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_ITEM+"' and today.userId=?  \n ");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//查看范围限制
		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? or a.developLeader=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from itemsharer b where a.id = b.itemId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());

			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//研发负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.developLeader and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInItemShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
		}
		//项目状态筛选
		if(null!=item.getState() && item.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		//项目负责人筛选
		if(null!=item.getOwner() && item.getOwner()!=0){
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}
		//产品查询
		List<Product> listProduct = item.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and a.productId in ?");
		}
		//项目名称筛选
		if(null!=item.getItemName() && !"".equals(item.getItemName())){
			this.addSqlWhereLike(item.getItemName(),sql,args," and a.itemname like ? \n");
		}
		//筛选本月添加的
		sql.append("and to_date(substr(a.recordcreatetime,1,10),'yyyy-mm-dd') >= trunc(sysdate,'mm') \n");
		//负责人类型
		String ownerType =  item.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		sql.append("order by readstate,a.modifyDate desc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Item.class);
	}
	/***
	 * 获取个人权限下的本月新增（分页）
	 * @param item
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemAddByMonthForPage(Item item,UserInfo userInfo,boolean isForceInPersion){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username as ownerName,c.gender,d.uuid,d.filename, \n");
		sql.append("case when a.modifyDate is null then a.recordcreatetime else a.modifyDate end modifyDate,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as readstate, \n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState  \n");
		sql.append("from item a  \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ITEM+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_ITEM+"' and today.userId=?  \n ");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//查看范围限制
		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" a.owner =? or a.developLeader=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from itemsharer b where a.id = b.itemId and  \n");
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
			//研发负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.developLeader and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInItemShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
		}
		//项目状态筛选
		if(null!=item.getState() && item.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		//项目负责人筛选
		if(null!=item.getOwner() && item.getOwner()!=0){
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}
		//产品查询
		List<Product> listProduct = item.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and a.productId in ?");
		}
		//项目名称筛选
		if(null!=item.getItemName() && !"".equals(item.getItemName())){
			this.addSqlWhereLike(item.getItemName(),sql,args," and a.itemname like ? \n");
		}
		//筛选本月添加的
		sql.append("and to_date(substr(a.recordcreatetime,1,10),'yyyy-mm-dd') >= trunc(sysdate,'mm') \n");
		//负责人类型
		String ownerType =  item.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		return this.pagedQuery(sql.toString(),"readstate,a.modifyDate desc,a.id desc", args.toArray(),Item.class);
	}
	/***
	 * 获取个人移交项目（分页）
	 * @param item
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemHandsForPage(Item item,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,\n");
		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as readstate, \n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState  \n");
		sql.append(" from (  select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username as ownerName,d.uuid, \n");
		sql.append("case when a.modifyDate is null then a.recordcreatetime else a.modifyDate end modifyDate\n");
		sql.append("from item a  \n");
		sql.append("inner join itemHandOver b on a.comId = b.comId and a.id = b.itemId \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_ITEM+"' and today.userId=?  \n ");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//数据筛选，筛选自己移交的
		sql.append(" and a.owner!=? and b.fromuser=? and b.touser!=? \n");
		args.add(userInfo.getId());
		args.add(userInfo.getId());
		args.add(userInfo.getId());
		//查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//项目状态筛选
		if(null!=item.getState() && item.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		//项目负责人筛选
		if(null!=item.getOwner() && item.getOwner()!=0){
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}
		//产品查询
		List<Product> listProduct = item.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and a.productId in ?");
		}
		//项目名称筛选
		if(null!=item.getItemName() && !"".equals(item.getItemName())){
			this.addSqlWhereLike(item.getItemName(),sql,args," and a.itemname like ? \n");
		}

		sql.append("group by a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username,a.modifyDate,d.uuid\n");
		sql.append(")a left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ITEM+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("where 1=1 \n");
		//负责人类型
		String ownerType =  item.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		return this.pagedQuery(sql.toString(),"readstate,a.modifyDate desc,a.id desc", args.toArray(),Item.class);
	}
	/***
	 * 获取个人移交项目（不分页）
	 * @param item
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemHandsOfAll(Item item,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as readstate, \n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState  \n");
		sql.append(" from (  select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username as ownerName,d.uuid, \n");
		sql.append("case when a.modifyDate is null then a.recordcreatetime else a.modifyDate end modifyDate\n");
		sql.append("from item a  \n");
		sql.append("inner join itemHandOver b on a.comId = b.comId and a.id = b.itemId \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_ITEM+"' and today.userId=?  \n ");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//数据筛选，筛选自己移交的
		sql.append(" and a.owner!=? and b.fromuser=? and b.touser!=? \n");
		args.add(userInfo.getId());
		args.add(userInfo.getId());
		args.add(userInfo.getId());
		//查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//项目状态筛选
		if(null!=item.getState() && item.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		//项目负责人筛选
		if(null!=item.getOwner() && item.getOwner()!=0){
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}

		//项目名称筛选
		if(null!=item.getItemName() && !"".equals(item.getItemName())){
			this.addSqlWhereLike(item.getItemName(),sql,args," and a.itemname like ? \n");
		}
		//产品查询
		List<Product> listProduct = item.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and a.productId in ?");
		}
		sql.append("group by a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username,a.modifyDate,d.uuid\n");
		sql.append(")a left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ITEM+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("where 1=1 \n");
		//负责人类型
		String ownerType =  item.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		sql.append("order by readstate,a.modifyDate desc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(),Item.class);
	}
	/**
	 * 根据主键获项目配置
	 * @param itemId 项目主键
	 * @param user 操作人员
	 * @return
	 */
	public Item queryItemById(Integer itemId,UserInfo user){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,b.username as ownerName,b.gender,c.uuid,c.filename,d.itemname as pItemName, \n");
		sql.append("case when atten.id is null then 0 else 1 end as attentionState,  \n");
		sql.append("e.customerName as partnerName,u.userName developLeaderName,g.userName productManagerName,case when f.name is not null then f.name||'_V'||f.version else null end productName \n");
		sql.append("from item a inner join userinfo b on a.owner = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id \n");
		sql.append("left join item d on d.id = a.parentid \n");
		sql.append("left join customer e on a.comId = e.comId and a.partnerId = e.id and e.delstate=0\n");
		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ITEM+"' and atten.userId=? \n");
		
		args.add(user.getId());
		sql.append("left join userinfo u on u.id = a.developLeader \n");
		sql.append("left join product f on f.id = a.productId \n");
		sql.append("left join userinfo g on g.id = f.manager \n");
		sql.append("where a.id = ? and a.comId=?");
		args.add(itemId);
		args.add(user.getComId());
		return (Item)this.objectQuery(sql.toString(), args.toArray(), Item.class);
	}

	/**
	 * 根据项目主键获取子项目集合
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listSonItem(Integer itemId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.recordcreatetime,a.comId,a.itemname,a.owner,a.state,b.username as ownerName,b.gender,c.uuid, \n");
		sql.append("c.filename,count(d.id) as sonItemNum from item a inner join userinfo b on a.owner = b.id  \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id \n");
		sql.append("left join item d on a.comId = d.comId and a.id = d.parentid \n");
		sql.append("where a.comId=? and a.parentid = ? and a.delstate=0 \n");
		sql.append("group by a.id,a.recordcreatetime,a.comId,a.itemname,a.owner,a.state,b.username ,b.gender,c.uuid,c.filename \n");
		sql.append("order by a.id desc");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(), args.toArray(), Item.class);
	}
	/**
	 * 项目查看权限验证
	 * @param comId
	 * @param itemId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> authorCheck(Integer comId,Integer itemId,Integer userId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//人员验证
		sql.append("select a.id from item a \n");
		sql.append("left join itemsharer b on a.comId = b.comId and a.id = b.itemid \n");
		sql.append("where a.comId = ? and a.id=? and a.delstate=0  \n");
		args.add(comId);
		args.add(itemId);
		sql.append("and (\n");
		//任务的参与、执行、负责权限验证
		sql.append(" b.userid =? or a.owner =? or a.developLeader =? \n");
		args.add(userId);
		args.add(userId);
		args.add(userId);
		//上级权限验证
		//参与人上级权限验证
		sql.append(" or exists(select id from myLeaders where creator = b.userid and comId = ? and leader = ? and leader <> creator )");
		args.add(comId);
		args.add(userId);
		//负责人上级权限验证
		sql.append(" or exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
		args.add(comId);
		args.add(userId);
		//研发负责人上级权限验证
		sql.append(" or exists(select id from myLeaders where creator = a.developLeader and comId = ? and leader = ? and leader <> creator )");
		args.add(comId);
		args.add(userId);
		sql.append(") \n");
		//合并
		sql.append("union \n");
		//项目成员组验证
		sql.append("select a.itemId as id from view_item_group_userId a \n");
		sql.append("where a.comId = ? and a.itemId=? and a.userId=? \n");
		args.add(comId);
		args.add(itemId);
		args.add(userId);
		//公开项目
		sql.append("union select id from item where comId=? and delstate=0 and pubState=1 \n");
		args.add(comId);
		return this.listQuery(sql.toString(),args.toArray(),Item.class);
	}

	/**
	 * 查询项目参与人信息
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemSharer> listItemSharer(Integer itemId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,b.username as sharerName,b.gender,c.uuid,c.filename from itemSharer a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.smallheadportrait = c.id\n");
		sql.append("where a.itemId = ? and a.comId = ? \n");
		args.add(itemId);
		args.add(comId);
		return this.listQuery(sql.toString(), args.toArray(), ItemSharer.class);
	}
	/**
	 * 非现在的项目参与人
	 * @param itemId
	 * @param comId
	 * @param userIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemSharer> listRemoveItemSharer(Integer itemId,Integer comId,Integer[] userIds){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( \n");
		//原有项目参与人
		sql.append("select a.comId,a.itemId,a.userId from itemSharer a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(itemId, sql, args, " and  a.itemId = ? ");
		this.addSqlWhere(comId, sql, args, " and  a.comId= ? ");
		
		sql.append("minus \n");
		//项目负责人
		sql.append("select a.comId,a.id itemId,a.owner userId  from item a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(itemId, sql, args, " and  a.Id = ? ");
		this.addSqlWhere(comId, sql, args, " and  a.comId= ? ");
		
		sql.append("minus \n");
		//研发负责人
		sql.append("select a.comId,a.id itemId,a.developLeader userId  from item a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(itemId, sql, args, " and  a.Id = ? ");
		this.addSqlWhere(comId, sql, args, " and  a.comId= ? ");
		
		sql.append("minus \n");
		//现在的项目参与人
		sql.append("select a.comId,a.itemId,a.userId  from itemSharer a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(itemId, sql, args, " and  a.itemId = ?");
		this.addSqlWhere(comId, sql, args, " and a.comId= ?");
		sql.append(" and a.userId in (0");
		if(null!=userIds && userIds.length>0){
			for (Integer userId : userIds) {
				sql.append(","+userId);
			}
		}
		sql.append(")\n");
		sql.append("minus \n");
		//项目分享组
		sql.append("\n select a.comId,a.itemId,b.userinfoid userId from itemShareGroup a inner join grouppersion b");
		sql.append("\n on a.comId=b.comId and a.grpId=b.grpid ");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
		sql.append(")\n");
		return this.listQuery(sql.toString(), args.toArray(), ItemSharer.class);
	}
	/**
	 * 汇报项目进度
	 * @param item
	 * @return
	 */
	public void itemProgressReport(Item item){
		StringBuffer sql = new StringBuffer("update item a set a.itemProgress=:itemProgress,modifyDate=:modifyDate where a.comId=:comId and a.id=:id");
		this.update(sql.toString(),item);
	}
	/**
	 * 项目名称变更
	 * @param item
	 * @return
	 */
	public void itemNameUpdate(Item item){
		StringBuffer sql = new StringBuffer("update item a set a.itemName=:itemName,modifyDate=:modifyDate where a.comId=:comId and a.id=:id");
		this.update(sql.toString(),item);
	}
	/**
	 * 项目说明更新
	 * @param item
	 * @return
	 */
	public void itemItemRemarkUpdate(Item item){
		StringBuffer sql = new StringBuffer("update item a set a.itemRemark=:itemRemark,modifyDate=:modifyDate where a.comId=:comId and a.id=:id");
		this.update(sql.toString(),item);
	}
	/**
	 * 项目母项目关联
	 * @param item
	 * @return
	 */
	public void itemParentIdUpdate(Item item){
		StringBuffer sql = new StringBuffer("update item a set a.parentId=:parentId,modifyDate=:modifyDate where a.comId=:comId and a.id=:id");
		this.update(sql.toString(),item);
	}
	/**
	 * 项目负责人更新
	 * @param item
	 * @return
	 */
	public void itemOwnerUpdate(Item item){
		StringBuffer sql = new StringBuffer("update item a set a.owner=:owner,modifyDat=:modifyDate where a.comId=:comId and a.id=:id");
		this.update(sql.toString(),item);
	}
	/**
	 * 项目状态标记
	 * @param item
	 * @return
	 */
	public void remarkItemState(Item item){
		StringBuffer sql = new StringBuffer("update item a set state=:state,modifyDate=:modifyDate where a.comId=:comId and a.id=:id");
		this.update(sql.toString(),item);
	}
	/**
	 * 所有后代项目集合 ，不包括预删除的
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemOfChildren(Integer itemId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from item a where a.comId=? and a.delstate=0 start with a.id=? connect by prior a.id =  a.parentid");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(), args.toArray(), Item.class);
	}
	/**
	 * 获取项目的所有后代集合；不包含当前项目和预删除的任务
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemOfOnlyChildren(Integer itemId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from item a where a.comId=? and a.delstate=0 start with a.parentid=? connect by prior a.id =  a.parentid");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(), args.toArray(), Item.class);
	}
	/**
	 * 获取项目的所有后代集合；不包含当前项目
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemOfAllOnlyChildren(Integer itemId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from item a where a.comId=? start with a.parentid=? connect by prior a.id =  a.parentid");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(), args.toArray(), Item.class);
	}
	/**
	 * 获取此项目以及此项目后代项目以外的项目集合
	 * @param item
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemOfOthers(Item item){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.itemname from item a \n");
		sql.append("where a.comId=? and a.delstate=0 \n");
		args.add(item.getComId());
		this.addSqlWhereLike(item.getpItemName(), sql, args, " and a.itemname like ? \n");
		sql.append("and a.id not in \n");
		sql.append("(select id from item a where a.comId=? and a.delstate=0 start with a.id=? connect by prior a.id =  a.parentid) ");
		args.add(item.getComId());
		args.add(item.getId());
		return this.listQuery(sql.toString(), args.toArray(),Item.class);
	}
	/**
	 * 根据主键id查询讨论详情
	 * @param id
	 * @param comId
	 * @return
	 */
	public ItemTalk queryItemTalk(Integer id,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select d.userId as pSpeaker,d.content as pContent,e.username as pSpeakerName,\n");
		sql.append("a.*,b.username as speakerName,b.gender,c.uuid,c.filename from itemTalk a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("left join itemTalk d on a.parentid = d.id and a.comId = d.comId \n");
		sql.append("left join userinfo e on d.userId = e.id \n");
		sql.append("where a.comId=? and a.id = ?");
		args.add(comId);
		args.add(id);
		return (ItemTalk)this.objectQuery(sql.toString(), args.toArray(), ItemTalk.class);
	}
	/**
	 * 根据项目主键查询其下的讨论信息
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemTalk> listItemTalk(Integer itemId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select connect_by_isleaf as isLeaf,d.userId as pSpeaker,d.content as pContent, \n");
		sql.append("e.username as pSpeakerName,a.*,b.username as speakerName,b.gender,c.uuid,c.filename from itemTalk a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("left join itemTalk d on a.parentid = d.id and a.comId = d.comId \n");
		sql.append("left join userinfo e on d.userId = e.id \n");
		sql.append("where a.comId=? and a.itemid = ? \n");
		sql.append("start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid \n");
		sql.append("order siblings by a.recordcreatetime desc,a.id");
		args.add(comId);
		args.add(itemId);
		return this.pagedQuery(sql.toString(), null, args.toArray(), ItemTalk.class);
	}
	
	/**
	 * 查询留言总数
	 * @param itemId
	 * @param comId
	 * @return
	 */
	public Integer countTalk(Integer itemId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select count(1) from (");
		sql.append("select connect_by_isleaf as isLeaf,d.userId as pSpeaker,d.content as pContent, \n");
		sql.append("e.username as pSpeakerName,a.*,b.username as speakerName,b.gender,c.uuid,c.filename from itemTalk a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("left join itemTalk d on a.parentid = d.id and a.comId = d.comId \n");
		sql.append("left join userinfo e on d.userId = e.id \n");
		sql.append("where a.comId=? and a.itemid = ? \n");
		sql.append("start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid \n");
		sql.append("order siblings by a.recordcreatetime desc,a.id");
		sql.append(")");
		args.add(comId);
		args.add(itemId);
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 根据父节点查询回复集合
	 * @param itemId
	 * @param comId
	 * @param parentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemTalk> listReplyItemTalk(Integer itemId,Integer comId,Integer parentId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,b.username as speakerName,b.gender,c.uuid,c.filename from itemTalk a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.itemid = ? and a.parentid=? \n");
		sql.append("CONNECT BY PRIOR a.id = a.parentid \n");
		args.add(comId);
		args.add(itemId);
		args.add(parentId);
		return this.listQuery(sql.toString(), args.toArray(), ItemTalk.class);
	}
	/**
	 * 更新项目讨论的父级节点
	 * @param id
	 * @param comId
	 */
	public void updateItemTalkParentId(Integer id,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update itemtalk set parentId=(select c.parentid \n");
		sql.append("from itemtalk c \n");
		sql.append("where c.id=?) where parentid = ? and comId = ? \n");
		args.add(id);
		args.add(id);
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 删除项目节点讨论及其回复
	 * @param id
	 * @param comId
	 */
	public void delItemTalk(Integer id,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from itemtalk a where a.comId =? and a.id in \n");
		sql.append("(select id from itemtalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 获取项目日志集合
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemLog> listItemLog(Integer itemId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from itemLog a \n");
		sql.append("inner join userinfo b on a.userId = b.id  \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.itemid = ? \n");
		args.add(comId);
		args.add(itemId);
		return this.pagedQuery(sql.toString()," a.recordcreatetime desc", args.toArray(), ItemLog.class);
	}
	/**
	 * 获取项目附件
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemUpfile> listItemUpfile(Integer itemId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from \n");
		sql.append("( \n");
		//项目阶段
		sql.append("select stagedItem.stagedName as sourceName,a.itemid as key,a.moduleId upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'"+ConstantInterface.TYPE_ITEMSTAGE+"' as type from stagedInfo a \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.moduleId = b.id \n");
		sql.append("left join userinfo c on a.creator = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("left join item on a.comId = item.comId and a.itemid = item.id \n");
		sql.append("left join stagedItem on a.comId = item.comId and a.stagedItemId = stagedItem.id \n");
		sql.append("where a.comId=? and item.id=? and a.moduleType='file' \n");
		args.add(comId);
		args.add(itemId);
		sql.append("union all\n");
		//任务
		sql.append("select task.taskname as sourceName,task.id as key,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'"+ConstantInterface.TYPE_TASK+"' as type from taskUpfile a \n");
		sql.append("inner join task on a.comId = task.comId and a.taskid = task.id and task.bustype='"+ConstantInterface.TYPE_ITEM+"'\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("left join item on task.comId = item.comId and task.busid = item.id \n");
		sql.append("where task.comId=? start with item.id=? connect by prior task.id =  task.parentid\n");
		args.add(comId);
		args.add(itemId);
		sql.append("union all\n");
		//任务讨论
		sql.append("select task.taskname as sourceName,task.id as key,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,\n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'"+ConstantInterface.TYPE_TASKTALK+"' as type  from taskTalkUpfile a \n");
		sql.append("inner join task on a.comId = task.comId and a.taskid = task.id and task.bustype='"+ConstantInterface.TYPE_ITEM+"'\n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("left join item on task.comId = item.comId and task.busid = item.id \n");
		sql.append("where task.comId=? start with item.id=? connect by prior task.id =  task.parentid\n");
		args.add(comId);
		args.add(itemId);
		sql.append("union all\n");
		//项目
		sql.append("select item.itemname as sourceName,a.itemId as key,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,\n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'"+ConstantInterface.TYPE_ITEM+"' as type  from itemUpfile a \n");
		sql.append("inner join item on a.comId = item.comId and a.itemid = item.id \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where item.comId =? start with item.id = ? connect by prior item.id =  item.parentid \n");
		args.add(comId);
		args.add(itemId);
		sql.append("union all\n");
		//项目讨论
		sql.append("select item.itemname as sourceName,a.itemId as key,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,\n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'"+ConstantInterface.TYPE_ITEMTALK+"' as type  from itemTalkFile a \n");
		sql.append("inner join item on a.comId = item.comId and a.itemid = item.id \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where item.comId =? start with item.id = ? connect by prior item.id =  item.parentid \n");
		sql.append(") a \n");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(), args.toArray(), ItemUpfile.class);
	}
		/**
		 * 获取项目附件
		 * @param itemUpfile
		 * @param comId
		 * @param type 
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public List<ItemUpfile> listPagedItemUpfile(ItemUpfile itemUpfile,Integer comId, String type){
			List<Object> args = new ArrayList<Object>();
			StringBuffer sql = new StringBuffer();
			sql.append("select distinct(a.upfileid) as idOfFile,a.* from \n");
			sql.append("( \n");
			if(CommonUtil.isNull(type)){
				/*获取项目节点中上传的附件*/
				sql.append("select stagedItem.stagedName as sourceName,a.itemid as key,b.filename,b.uuid,a.recordcreatetime,a.moduleId upfileid,a.creator userId,c.username as creatorName,a.id, \n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'file' as type,b.fileExt from stagedInfo a \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.moduleId = b.id \n");
				sql.append("left join userinfo c on  a.creator = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("left join item on a.comId = item.comId and a.itemid = item.id \n");
				sql.append("left join stagedItem on a.comId = stagedItem.comId and a.stagedItemId = stagedItem.id \n");
				sql.append("where a.comId=? and a.itemId=? and a.moduleType='file'\n");
				args.add(comId);
				args.add(itemUpfile.getItemId());
				/*关联任务本身附件*/
				sql.append(" union all \n");
				/*获取客户节点中上传的附件*/
				sql.append("select crm.customerName as sourceName,crm.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileId upfileid,a.userId,c.username as creatorName,a.id, \n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'crm' type,b.fileExt from customerUpfile a \n");
				sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
				sql.append("inner join item i on i.partnerId=crm.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where 1=1 \n");
				this.addSqlWhere(comId, sql, args, " and a.comId=?");
				this.addSqlWhere(itemUpfile.getItemId(), sql, args, " and i.id=?");
				sql.append("\n union all \n");
				sql.append("select crm.customerName as sourceName,crm.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileId upfileid,a.userId,c.username as creatorName,a.id, \n");				
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName ,'crmTalk' type,b.fileExt from feedInfoFile a\n");
				sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
				sql.append("inner join item i on i.partnerId=crm.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
				sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");;
				sql.append("where 1=1\n");
				this.addSqlWhere(comId, sql, args, " and a.comId=?");
				this.addSqlWhere(itemUpfile.getItemId(), sql, args, " and i.id=?");
				/*关联任务本身附件*/
				sql.append(" union all \n");
				sql.append("select task.taskname as sourceName,task.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id, \n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'task' as type,b.fileExt from taskUpfile a \n");
				sql.append("inner join task on a.comId = task.comId and a.taskid = task.id and task.bustype='"+ConstantInterface.TYPE_ITEM+"'\n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("left join item on task.comId = item.comId and task.busid = item.id \n");
				sql.append("where task.comId=? and task.id in (select b.id from item a right join task b on a.comId = b.comId \n");
				sql.append("and a.id=b.busid and b.busType='"+ConstantInterface.TYPE_ITEM+"' where b.comId=? start with a.id=? connect by prior b.id = b.parentid) \n");
				args.add(comId);
				args.add(comId);
				args.add(itemUpfile.getItemId());
				/*关联任务讨论时上传的附件*/
				sql.append(" union all \n");
				sql.append("select task.taskname as sourceName,task.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'taskTalk' as type,b.fileExt from taskTalkUpfile a \n");
				sql.append("inner join task on a.comId = task.comId and a.taskid = task.id and task.bustype='"+ConstantInterface.TYPE_ITEM+"'\n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("left join item on task.comId = item.comId and task.busid = item.id \n");
				sql.append("where task.comId=? and task.id in (select b.id from item a right join task b on a.comId = b.comId \n");
				sql.append(" and a.id=b.busid and b.busType='"+ConstantInterface.TYPE_ITEM+"'  where b.comId=? start with a.id=? connect by  prior b.id = b.parentid) \n");
				args.add(comId);
				args.add(comId);
				args.add(itemUpfile.getItemId());
				 /*项目以及项目后代的项目附件**/ 
				sql.append(" union all \n");
				sql.append("select item.itemname as sourceName,a.itemId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'item' as type,b.fileExt  from itemUpfile a \n");
				sql.append("inner join item on a.comId = item.comId and a.itemid = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and item.id in (select a.id from item a where a.comId=? start with a.id=? connect by  prior a.id = a.parentid) \n");
				args.add(comId);
				args.add(comId);
				args.add(itemUpfile.getItemId());
				/*项目留言附件*/
				sql.append(" union all \n");
				sql.append("select item.itemname as sourceName,a.itemId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'itemTalk' as type,b.fileExt  from itemTalkFile a \n");
				sql.append("inner join item on a.comId = item.comId and a.itemid = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and item.id in (select a.id from item a where a.comId=? start with a.id=? connect by  prior a.id = a.parentid) \n");
				args.add(comId);
				args.add(comId);
				args.add(itemUpfile.getItemId());
				/*需求附件*/
				sql.append(" union all \n");
				sql.append("select item.SERIALNUM as sourceName,a.DEMANDID as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.CREATOR userid,c.username as creatorName,a.id,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'demand' as type,b.fileExt  from DEMANDFILE a \n");
				sql.append("inner join DEMANDPROCESS item on a.comId = item.comId and a.DEMANDID = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.CREATOR = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and item.ITEMID=?  \n");
				args.add(comId);
				args.add(itemUpfile.getItemId());
				/*需求留言附件*/
				sql.append(" union all \n");
				sql.append("select item.SERIALNUM as sourceName,a.DEMANDID as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'demandTalk' as type,b.fileExt  from DEMANDTALKUPFILE a \n");
				sql.append("inner join DEMANDPROCESS item on a.comId = item.comId and a.DEMANDID = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and item.ITEMID=?  \n");
				args.add(comId);
				args.add(itemUpfile.getItemId());
				/*产品附件*/
				sql.append(" union all \n");
				sql.append("select item.name as sourceName,a.proId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.uploader userid,c.username as creatorName,a.id,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'product' as type,b.fileExt  from proUpFiles a \n");
				sql.append("inner join product item on a.comId = item.comId and a.proId = item.id \n");
				sql.append("inner join item i on i.productId = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.uploader = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and i.id=?  \n");
				args.add(comId);
				args.add(itemUpfile.getItemId());
				/*产品留言附件*/
				sql.append(" union all \n");
				sql.append("select item.name as sourceName,a.proId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'productTalk' as type,b.fileExt  from proTalkUpfile a \n");
				sql.append("inner join product item on a.comId = item.comId and a.proId = item.id \n");
				sql.append("inner join item i on i.productId = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and i.id=?  \n");
				args.add(comId);
				args.add(itemUpfile.getItemId());
			}else{
				if(!CommonUtil.isNull(type) && "sale".equals(type)){
					/*获取项目节点中上传的附件*/
					sql.append("select stagedItem.stagedName as sourceName,a.itemid as key,b.filename,b.uuid,a.recordcreatetime,a.moduleId upfileid,a.creator userId,c.username as creatorName,a.id, \n");
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'file' as type,b.fileExt from stagedInfo a \n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.moduleId = b.id \n");
					sql.append("left join userinfo c on  a.creator = c.id \n");
					sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
					sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
					sql.append("left join item on a.comId = item.comId and a.itemid = item.id \n");
					sql.append("left join stagedItem on a.comId = stagedItem.comId and a.stagedItemId = stagedItem.id \n");
					sql.append("where a.comId=? and a.itemId=? and a.moduleType='file'\n");
					args.add(comId);
					args.add(itemUpfile.getItemId());
					/*关联任务本身附件*/
					sql.append(" union all \n");
					/*获取客户节点中上传的附件*/
					sql.append("select crm.customerName as sourceName,crm.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileId upfileid,a.userId,c.username as creatorName,a.id, \n");
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'crm' type,b.fileExt from customerUpfile a \n");
					sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
					sql.append("inner join item i on i.partnerId=crm.id \n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
					sql.append("left join userinfo c on a.userid = c.id \n");
					sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
					sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
					sql.append("where 1=1 \n");
					this.addSqlWhere(comId, sql, args, " and a.comId=?");
					this.addSqlWhere(itemUpfile.getItemId(), sql, args, " and i.id=?");
					sql.append("\n union all \n");
					sql.append("select crm.customerName as sourceName,crm.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileId upfileid,a.userId,c.username as creatorName,a.id, \n");				
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName ,'crmTalk' type,b.fileExt from feedInfoFile a\n");
					sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
					sql.append("inner join item i on i.partnerId=crm.id \n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
					sql.append("left join userinfo c on  a.userid = c.id \n");
					sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
					sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");;
					sql.append("where 1=1\n");
					this.addSqlWhere(comId, sql, args, " and a.comId=?");
					this.addSqlWhere(itemUpfile.getItemId(), sql, args, " and i.id=?");
					 /*项目以及项目后代的项目附件**/ 
					sql.append(" union all \n");
					sql.append("select item.itemname as sourceName,a.itemId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id,\n");
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'item' as type,b.fileExt  from itemUpfile a \n");
					sql.append("inner join item on a.comId = item.comId and a.itemid = item.id \n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
					sql.append("left join userinfo c on  a.userid = c.id \n");
					sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
					sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
					sql.append("where item.comId =? and item.id in (select a.id from item a where a.comId=? start with a.id=? connect by  prior a.id = a.parentid) \n");
					args.add(comId);
					args.add(comId);
					args.add(itemUpfile.getItemId());
					/*产品附件*/
					sql.append(" union all \n");
					sql.append("select item.name as sourceName,a.proId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.uploader userid,c.username as creatorName,a.id,\n");
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'product' as type,b.fileExt  from proUpFiles a \n");
					sql.append("inner join product item on a.comId = item.comId and a.proId = item.id \n");
					sql.append("inner join item i on i.productId = item.id \n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
					sql.append("left join userinfo c on  a.uploader = c.id \n");
					sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
					sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
					sql.append("where item.comId =? and i.id=?  \n");
					args.add(comId);
					args.add(itemUpfile.getItemId());
					/*产品留言附件*/
					sql.append(" union all \n");
					sql.append("select item.name as sourceName,a.proId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id,\n");
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'productTalk' as type,b.fileExt  from proTalkUpfile a \n");
					sql.append("inner join product item on a.comId = item.comId and a.proId = item.id \n");
					sql.append("inner join item i on i.productId = item.id \n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
					sql.append("left join userinfo c on  a.userid = c.id \n");
					sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
					sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
					sql.append("where item.comId =? and i.id=?  \n");
					args.add(comId);
					args.add(itemUpfile.getItemId());
				}else{
					/*关联任务本身附件*/
					sql.append("select task.taskname as sourceName,task.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id, \n");
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'task' as type,b.fileExt from taskUpfile a \n");
					sql.append("inner join task on a.comId = task.comId and a.taskid = task.id and task.bustype='"+ConstantInterface.TYPE_ITEM+"'\n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
					sql.append("left join userinfo c on  a.userid = c.id \n");
					sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
					sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
					sql.append("left join item on task.comId = item.comId and task.busid = item.id \n");
					sql.append("where task.comId=? and task.id in (select b.id from item a right join task b on a.comId = b.comId \n");
					sql.append("and a.id=b.busid and b.busType='"+ConstantInterface.TYPE_ITEM+"' where b.comId=? start with a.id=? connect by prior b.id = b.parentid) \n");
					args.add(comId);
					args.add(comId);
					args.add(itemUpfile.getItemId());
					/*关联任务讨论时上传的附件*/
					sql.append(" union all \n");
					sql.append("select task.taskname as sourceName,task.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id,\n");
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'taskTalk' as type,b.fileExt from taskTalkUpfile a \n");
					sql.append("inner join task on a.comId = task.comId and a.taskid = task.id and task.bustype='"+ConstantInterface.TYPE_ITEM+"'\n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
					sql.append("left join userinfo c on  a.userid = c.id \n");
					sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
					sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
					sql.append("left join item on task.comId = item.comId and task.busid = item.id \n");
					sql.append("where task.comId=? and task.id in (select b.id from item a right join task b on a.comId = b.comId \n");
					sql.append(" and a.id=b.busid and b.busType='"+ConstantInterface.TYPE_ITEM+"'  where b.comId=? start with a.id=? connect by  prior b.id = b.parentid) \n");
					args.add(comId);
					args.add(comId);
					args.add(itemUpfile.getItemId());
					/*项目留言附件*/
					sql.append(" union all \n");
					sql.append("select item.itemname as sourceName,a.itemId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id,\n");
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'itemTalk' as type,b.fileExt  from itemTalkFile a \n");
					sql.append("inner join item on a.comId = item.comId and a.itemid = item.id \n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
					sql.append("left join userinfo c on  a.userid = c.id \n");
					sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
					sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
					sql.append("where item.comId =? and item.id in (select a.id from item a where a.comId=? start with a.id=? connect by  prior a.id = a.parentid) \n");
					args.add(comId);
					args.add(comId);
					args.add(itemUpfile.getItemId());
					/*需求附件*/
					sql.append(" union all \n");
					sql.append("select item.SERIALNUM as sourceName,a.DEMANDID as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.CREATOR userid,c.username as creatorName,a.id,\n");
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'demand' as type,b.fileExt  from DEMANDFILE a \n");
					sql.append("inner join DEMANDPROCESS item on a.comId = item.comId and a.DEMANDID = item.id \n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
					sql.append("left join userinfo c on  a.CREATOR = c.id \n");
					sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
					sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
					sql.append("where item.comId =? and item.ITEMID=?  \n");
					args.add(comId);
					args.add(itemUpfile.getItemId());
					/*需求留言附件*/
					sql.append(" union all \n");
					sql.append("select item.SERIALNUM as sourceName,a.DEMANDID as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,a.id,\n");
					sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'demandTalk' as type,b.fileExt  from DEMANDTALKUPFILE a \n");
					sql.append("inner join DEMANDPROCESS item on a.comId = item.comId and a.DEMANDID = item.id \n");
					sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
					sql.append("left join userinfo c on  a.userid = c.id \n");
					sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
					sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
					sql.append("where item.comId =? and item.ITEMID=?  \n");
					args.add(comId);
					args.add(itemUpfile.getItemId());
				}
			}
			
			
			sql.append(") a where 1=1 \n");
			this.addSqlWhereLike(itemUpfile.getFilename(), sql, args, " and a.filename like ?");
			//排序方式
			String order = "a.recordcreatetime desc";
			if(!CommonUtil.isNull(itemUpfile.getOrder())) {
				if("fileExt".equals(itemUpfile.getOrder())) {
					order = "a.fileExt,a.recordcreatetime desc";
				}else if("userId".equals(itemUpfile.getOrder())){
					order = "a.userId,a.recordcreatetime desc";
				}
				
			}
			return this.pagedQuery(sql.toString(), order, args.toArray(), ItemUpfile.class);
	}
		
	/**
	 * 查询文件总数
	 * @param itemId
	 * @param comId
	 * @return
	 */
	public Integer countFile(Integer itemId,Integer comId, String type){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from (");
		sql.append("select distinct(a.upfileid) as idOfFile,a.* from \n");
		sql.append("( \n");
		if(CommonUtil.isNull(type)){
			/*获取项目节点中上传的附件*/
			sql.append("select stagedItem.stagedName as sourceName,a.itemid as key,b.filename,b.uuid,a.recordcreatetime,a.moduleId upfileid,a.creator userId,c.username as creatorName, \n");
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'file' as type,b.fileExt from stagedInfo a \n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.moduleId = b.id \n");
			sql.append("left join userinfo c on  a.creator = c.id \n");
			sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
			sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
			sql.append("left join item on a.comId = item.comId and a.itemid = item.id \n");
			sql.append("left join stagedItem on a.comId = stagedItem.comId and a.stagedItemId = stagedItem.id \n");
			sql.append("where a.comId=? and a.itemId=? and a.moduleType='file'\n");
			args.add(comId);
			args.add(itemId);
			/*关联任务本身附件*/
			sql.append(" union all \n");
			/*获取客户节点中上传的附件*/
			sql.append("select crm.customerName as sourceName,crm.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileId upfileid,a.userId,c.username as creatorName, \n");
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'crm' type,b.fileExt from customerUpfile a \n");
			sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
			sql.append("inner join item i on i.partnerId=crm.id \n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
			sql.append("left join userinfo c on a.userid = c.id \n");
			sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
			sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
			sql.append("where 1=1 \n");
			this.addSqlWhere(comId, sql, args, " and a.comId=?");
			this.addSqlWhere(itemId, sql, args, " and i.id=?");
			sql.append("\n union all \n");
			sql.append("select crm.customerName as sourceName,crm.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileId upfileid,a.userId,c.username as creatorName, \n");				
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName ,'crm' type,b.fileExt from feedInfoFile a\n");
			sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
			sql.append("inner join item i on i.partnerId=crm.id \n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
			sql.append("left join userinfo c on  a.userid = c.id \n");
			sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
			sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");;
			sql.append("where 1=1\n");
			this.addSqlWhere(comId, sql, args, " and a.comId=?");
			this.addSqlWhere(itemId, sql, args, " and i.id=?");
			/*关联任务本身附件*/
			sql.append(" union all \n");
			sql.append("select task.taskname as sourceName,task.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName, \n");
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'task' as type,b.fileExt from taskUpfile a \n");
			sql.append("inner join task on a.comId = task.comId and a.taskid = task.id and task.bustype='"+ConstantInterface.TYPE_ITEM+"'\n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
			sql.append("left join userinfo c on  a.userid = c.id \n");
			sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
			sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
			sql.append("left join item on task.comId = item.comId and task.busid = item.id \n");
			sql.append("where task.comId=? and task.id in (select b.id from item a right join task b on a.comId = b.comId \n");
			sql.append("and a.id=b.busid and b.busType='"+ConstantInterface.TYPE_ITEM+"' where b.comId=? start with a.id=? connect by prior b.id = b.parentid) \n");
			args.add(comId);
			args.add(comId);
			args.add(itemId);
			/*关联任务讨论时上传的附件*/
			sql.append(" union all \n");
			sql.append("select task.taskname as sourceName,task.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,\n");
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'task' as type,b.fileExt from taskTalkUpfile a \n");
			sql.append("inner join task on a.comId = task.comId and a.taskid = task.id and task.bustype='"+ConstantInterface.TYPE_ITEM+"'\n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
			sql.append("left join userinfo c on  a.userid = c.id \n");
			sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
			sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
			sql.append("left join item on task.comId = item.comId and task.busid = item.id \n");
			sql.append("where task.comId=? and task.id in (select b.id from item a right join task b on a.comId = b.comId \n");
			sql.append(" and a.id=b.busid and b.busType='"+ConstantInterface.TYPE_ITEM+"'  where b.comId=? start with a.id=? connect by  prior b.id = b.parentid) \n");
			args.add(comId);
			args.add(comId);
			args.add(itemId);
			 /*项目以及项目后代的项目附件**/ 
			sql.append(" union all \n");
			sql.append("select item.itemname as sourceName,a.itemId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,\n");
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'item' as type,b.fileExt  from itemUpfile a \n");
			sql.append("inner join item on a.comId = item.comId and a.itemid = item.id \n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
			sql.append("left join userinfo c on  a.userid = c.id \n");
			sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
			sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
			sql.append("where item.comId =? and item.id in (select a.id from item a where a.comId=? start with a.id=? connect by  prior a.id = a.parentid) \n");
			args.add(comId);
			args.add(comId);
			args.add(itemId);
			/*项目留言附件*/
			sql.append(" union all \n");
			sql.append("select item.itemname as sourceName,a.itemId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,\n");
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'itemTalk' as type,b.fileExt  from itemTalkFile a \n");
			sql.append("inner join item on a.comId = item.comId and a.itemid = item.id \n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
			sql.append("left join userinfo c on  a.userid = c.id \n");
			sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
			sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
			sql.append("where item.comId =? and item.id in (select a.id from item a where a.comId=? start with a.id=? connect by  prior a.id = a.parentid) \n");
			args.add(comId);
			args.add(comId);
			args.add(itemId);
			/*需求附件*/
			sql.append(" union all \n");
			sql.append("select item.SERIALNUM as sourceName,a.DEMANDID as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.CREATOR userid,c.username as creatorName,\n");
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'demand' as type,b.fileExt  from DEMANDFILE a \n");
			sql.append("inner join DEMANDPROCESS item on a.comId = item.comId and a.DEMANDID = item.id \n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
			sql.append("left join userinfo c on  a.CREATOR = c.id \n");
			sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
			sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
			sql.append("where item.comId =? and item.ITEMID=?  \n");
			args.add(comId);
			args.add(itemId);
			/*需求留言附件*/
			sql.append(" union all \n");
			sql.append("select item.SERIALNUM as sourceName,a.DEMANDID as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,\n");
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'demandTalk' as type,b.fileExt  from DEMANDTALKUPFILE a \n");
			sql.append("inner join DEMANDPROCESS item on a.comId = item.comId and a.DEMANDID = item.id \n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
			sql.append("left join userinfo c on  a.userid = c.id \n");
			sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
			sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
			sql.append("where item.comId =? and item.ITEMID=?  \n");
			args.add(comId);
			args.add(itemId);
			/*产品附件*/
			sql.append(" union all \n");
			sql.append("select item.name as sourceName,a.proId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.uploader userid,c.username as creatorName,\n");
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'product' as type,b.fileExt  from proUpFiles a \n");
			sql.append("inner join product item on a.comId = item.comId and a.proId = item.id \n");
			sql.append("inner join item i on i.productId = item.id \n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
			sql.append("left join userinfo c on  a.uploader = c.id \n");
			sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
			sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
			sql.append("where item.comId =? and i.id=?  \n");
			args.add(comId);
			args.add(itemId);
			/*产品留言附件*/
			sql.append(" union all \n");
			sql.append("select item.name as sourceName,a.proId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,\n");
			sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'productTalk' as type,b.fileExt  from proTalkUpfile a \n");
			sql.append("inner join product item on a.comId = item.comId and a.proId = item.id \n");
			sql.append("inner join item i on i.productId = item.id \n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
			sql.append("left join userinfo c on  a.userid = c.id \n");
			sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
			sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
			sql.append("where item.comId =? and i.id=?  \n");
			args.add(comId);
			args.add(itemId);
		}else{
			if(!CommonUtil.isNull(type) && "sale".equals(type)){
				/*获取项目节点中上传的附件*/
				sql.append("select stagedItem.stagedName as sourceName,a.itemid as key,b.filename,b.uuid,a.recordcreatetime,a.moduleId upfileid,a.creator userId,c.username as creatorName, \n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'file' as type,b.fileExt from stagedInfo a \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.moduleId = b.id \n");
				sql.append("left join userinfo c on  a.creator = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("left join item on a.comId = item.comId and a.itemid = item.id \n");
				sql.append("left join stagedItem on a.comId = stagedItem.comId and a.stagedItemId = stagedItem.id \n");
				sql.append("where a.comId=? and a.itemId=? and a.moduleType='file'\n");
				args.add(comId);
				args.add(itemId);
				/*关联任务本身附件*/
				sql.append(" union all \n");
				/*获取客户节点中上传的附件*/
				sql.append("select crm.customerName as sourceName,crm.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileId upfileid,a.userId,c.username as creatorName, \n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'crm' type,b.fileExt from customerUpfile a \n");
				sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
				sql.append("inner join item i on i.partnerId=crm.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where 1=1 \n");
				this.addSqlWhere(comId, sql, args, " and a.comId=?");
				this.addSqlWhere(itemId, sql, args, " and i.id=?");
				sql.append("\n union all \n");
				sql.append("select crm.customerName as sourceName,crm.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileId upfileid,a.userId,c.username as creatorName, \n");				
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName ,'crm' type,b.fileExt from feedInfoFile a\n");
				sql.append("inner join customer crm on a.customerid=crm.id and a.comId=crm.comId \n");
				sql.append("inner join item i on i.partnerId=crm.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id = cc.userId and a.comId = cc.comId \n");
				sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");;
				sql.append("where 1=1\n");
				this.addSqlWhere(comId, sql, args, " and a.comId=?");
				this.addSqlWhere(itemId, sql, args, " and i.id=?");
				/*项目以及项目后代的项目附件**/ 
				sql.append(" union all \n");
				sql.append("select item.itemname as sourceName,a.itemId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'item' as type,b.fileExt  from itemUpfile a \n");
				sql.append("inner join item on a.comId = item.comId and a.itemid = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and item.id in (select a.id from item a where a.comId=? start with a.id=? connect by  prior a.id = a.parentid) \n");
				args.add(comId);
				args.add(comId);
				args.add(itemId);
				/*产品附件*/
				sql.append(" union all \n");
				sql.append("select item.name as sourceName,a.proId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.uploader userid,c.username as creatorName,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'product' as type,b.fileExt  from proUpFiles a \n");
				sql.append("inner join product item on a.comId = item.comId and a.proId = item.id \n");
				sql.append("inner join item i on i.productId = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.uploader = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and i.id=?  \n");
				args.add(comId);
				args.add(itemId);
				/*产品留言附件*/
				sql.append(" union all \n");
				sql.append("select item.name as sourceName,a.proId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'productTalk' as type,b.fileExt  from proTalkUpfile a \n");
				sql.append("inner join product item on a.comId = item.comId and a.proId = item.id \n");
				sql.append("inner join item i on i.productId = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and i.id=?  \n");
				args.add(comId);
				args.add(itemId);
			}else{
				/*关联任务本身附件*/
				sql.append("select task.taskname as sourceName,task.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName, \n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'task' as type,b.fileExt from taskUpfile a \n");
				sql.append("inner join task on a.comId = task.comId and a.taskid = task.id and task.bustype='"+ConstantInterface.TYPE_ITEM+"'\n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("left join item on task.comId = item.comId and task.busid = item.id \n");
				sql.append("where task.comId=? and task.id in (select b.id from item a right join task b on a.comId = b.comId \n");
				sql.append("and a.id=b.busid and b.busType='"+ConstantInterface.TYPE_ITEM+"' where b.comId=? start with a.id=? connect by prior b.id = b.parentid) \n");
				args.add(comId);
				args.add(comId);
				args.add(itemId);
				/*关联任务讨论时上传的附件*/
				sql.append(" union all \n");
				sql.append("select task.taskname as sourceName,task.id as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'task' as type,b.fileExt from taskTalkUpfile a \n");
				sql.append("inner join task on a.comId = task.comId and a.taskid = task.id and task.bustype='"+ConstantInterface.TYPE_ITEM+"'\n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("left join item on task.comId = item.comId and task.busid = item.id \n");
				sql.append("where task.comId=? and task.id in (select b.id from item a right join task b on a.comId = b.comId \n");
				sql.append(" and a.id=b.busid and b.busType='"+ConstantInterface.TYPE_ITEM+"'  where b.comId=? start with a.id=? connect by  prior b.id = b.parentid) \n");
				args.add(comId);
				args.add(comId);
				args.add(itemId);
				/*项目留言附件*/
				sql.append(" union all \n");
				sql.append("select item.itemname as sourceName,a.itemId as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'itemTalk' as type,b.fileExt  from itemTalkFile a \n");
				sql.append("inner join item on a.comId = item.comId and a.itemid = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and item.id in (select a.id from item a where a.comId=? start with a.id=? connect by  prior a.id = a.parentid) \n");
				args.add(comId);
				args.add(comId);
				args.add(itemId);
				/*需求附件*/
				sql.append(" union all \n");
				sql.append("select item.SERIALNUM as sourceName,a.DEMANDID as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.CREATOR userid,c.username as creatorName,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'demand' as type,b.fileExt  from DEMANDFILE a \n");
				sql.append("inner join DEMANDPROCESS item on a.comId = item.comId and a.DEMANDID = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.CREATOR = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and item.ITEMID=?  \n");
				args.add(comId);
				args.add(itemId);
				/*需求留言附件*/
				sql.append(" union all \n");
				sql.append("select item.SERIALNUM as sourceName,a.DEMANDID as key,b.filename,b.uuid,a.recordcreatetime,a.upfileid,a.userid,c.username as creatorName,\n");
				sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,'demandTalk' as type,b.fileExt  from DEMANDTALKUPFILE a \n");
				sql.append("inner join DEMANDPROCESS item on a.comId = item.comId and a.DEMANDID = item.id \n");
				sql.append("inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
				sql.append("left join userinfo c on  a.userid = c.id \n");
				sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
				sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
				sql.append("where item.comId =? and item.ITEMID=?  \n");
				args.add(comId);
				args.add(itemId);
			}
		}
		
		sql.append(") a \n");
		sql.append(" )");
		return this.countQuery(sql.toString(), args.toArray());
	}
		
	/**
	 * 获取项目阶段信息集合
	 * @param comId
	 * @param itemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemStagedInfo> listStagedItemInfo(Integer comId,Integer itemId){
		//TODO 项目附件以及留言附件（用户展示）
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from ( \n");
		//项目文件夹
		sql.append("select 'p'||a.id id,a.id as realId,'p'||a.id as key,a.recordcreatetime,a.stagedname as name,b.username,a.stagedorder as orderBy,\n");
		sql.append("'p'||a.parentid as parentId,a.parentid as realPid,'folder' as type,0 as moduleId,null uuid,null fileExt,a.creator from stagedItem a  \n");
		sql.append("inner join Userinfo b on  a.creator = b.id \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
//		sql.append("union \n");
//		//任务
//		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.taskname as name,c.username,0 as orderBy,\n");
//		sql.append("'p'||d.id as parentId,d.id as realPid,a.moduleType as type,a.moduleId,null uuid,null fileExt from stagedInfo a \n");
//		sql.append("inner join task b on a.comId = b.comId and a.moduleid = b.id and a.moduletype='task' \n");
//		sql.append("inner join userInfo c on  b.owner = c.id \n");
//		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
//		sql.append("where b.delState=0 and  a.comId =? and a.itemid=? \n");
//		args.add(comId);
//		args.add(itemId);
//		sql.append("union \n");
//		//审批
//		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,sp.flowName as name,c.username,0 as orderBy,\n");
//		sql.append("'p'||d.id as parentId,d.id as realPid,a.moduleType as type,a.moduleId,null uuid,null fileExt from stagedInfo a \n");
//		sql.append("inner join spFlowInstance sp on a.comId = sp.comId and a.moduleid = sp.id and a.moduletype='"+BusinessTypeConstant.STAGED_FLOW_SP+"' \n");
//		sql.append("inner join userInfo c on  sp.creator = c.id \n");
//		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
//		sql.append("where sp.flowState>0 and flowState<>2 and  a.comId =? and a.itemid=? \n");
//		args.add(comId);
//		args.add(itemId);
//		//项目阶段附件
//		sql.append("union \n");
//		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.filename as name,c.username,0 as orderBy,\n");
//		sql.append("'p'||d.id as parentId,d.id as realPid,a.moduleType as type,a.moduleId,b.uuid,b.fileExt from stagedInfo a \n");
//		sql.append("inner join upfiles b on a.comId = b.comId and a.moduleid = b.id and a.moduletype='file' \n");
//		sql.append("inner join userInfo c on  a.creator = c.id \n");
//		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
//		sql.append("where a.comId =? and a.itemid=? \n");
//		args.add(comId);
//		args.add(itemId);
//		//项目自身附件
//		sql.append("union \n");
//		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.filename as name,c.username,0 as orderBy,\n");
//		sql.append("'p'||d.id as parentId,d.id as realPid,'itemUpFile' as type,a.upfileId moduleId,b.uuid,b.fileExt from itemupfile a \n");
//		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileId = b.id \n");
//		sql.append("inner join userInfo c on  a.userId = c.id \n");
//		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
//		sql.append("where a.comId =? and a.itemid=? \n");
//		args.add(comId);
//		args.add(itemId);
//		//项目留言附件
//		sql.append("union \n");
//		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.filename as name,c.username,0 as orderBy,\n");
//		sql.append("'p'||d.id as parentId,d.id as realPid,'itemTalkFile' as type,a.upfileId moduleId,b.uuid,b.fileExt from itemTalkfile a \n");
//		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileId = b.id \n");
//		sql.append("inner join userInfo c on  a.userId = c.id \n");
//		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
//		sql.append("where a.comId =? and a.itemid=? \n");
//		args.add(comId);
//		args.add(itemId);
		sql.append(") a start with a.parentid = 'p-1' connect by prior a.id =  a.parentid \n");
		sql.append("order by a.orderBy desc,a.recordcreatetime desc \n");
		return this.listQuery(sql.toString(), args.toArray(), ItemStagedInfo.class);
	}
	/**
	 * 分页查询项目阶段信息
	 * @param userInfo
	 * @param itemId 项目主键
	 * @param realPid 阶段文件夹主键
	 * @param name 阶段内容名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemStagedInfo> itemStagPagedList(Integer comId,Integer itemId, Integer realPid, String name){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from ( \n");
		if(null!=name && !"".equals(name)){
			sql.append("select a.* from ( \n");
		}
		//项目文件夹
		sql.append("select 'p'||a.id id,a.id as realId,'p'||a.id as key,a.recordcreatetime,a.stagedname as name,b.username,a.stagedorder as orderBy,\n");
		sql.append("'p'||a.parentid as parentId,a.parentid as realPid,'folder' as type,0 as moduleId,null uuid,null fileExt,0 searchType from stagedItem a  \n");
		sql.append("inner join Userinfo b on  a.creator = b.id \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		sql.append("union \n");
		//任务
		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.taskname as name,c.username,0 as orderBy,\n");
		sql.append("'p'||d.id as parentId,d.id as realPid,a.moduleType as type,a.moduleId,null uuid,null fileExt,1 searchType from stagedInfo a \n");
		sql.append("inner join task b on a.comId = b.comId and a.moduleid = b.id and a.moduletype='task' \n");
		sql.append("inner join userInfo c on  b.owner = c.id \n");
		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
		sql.append("where b.delState=0 and  a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		//项目阶段附件
		sql.append("union \n");
		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.filename as name,c.username,0 as orderBy,\n");
		sql.append("'p'||d.id as parentId,d.id as realPid,a.moduleType as type,a.moduleId,b.uuid,b.fileExt,1 searchType from stagedInfo a \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.moduleid = b.id and a.moduletype='file' \n");
		sql.append("inner join userInfo c on  a.creator = c.id \n");
		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		//项目自身附件
		sql.append("union \n");
		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.filename as name,c.username,0 as orderBy,\n");
		sql.append("'p'||d.id as parentId,d.id as realPid,'itemUpFile' as type,a.upfileId moduleId,b.uuid,b.fileExt,1 searchType from itemupfile a \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileId = b.id \n");
		sql.append("inner join userInfo c on  a.userId = c.id \n");
		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		//项目留言附件
		sql.append("union \n");
		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.filename as name,c.username,0 as orderBy,\n");
		sql.append("'p'||d.id as parentId,d.id as realPid,'itemTalkFile' as type,a.upfileId moduleId,b.uuid,b.fileExt,1 searchType from itemTalkfile a \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileId = b.id \n");
		sql.append("inner join userInfo c on  a.userId = c.id \n");
		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		if(null==name || "".equals(name)){
			sql.append(") a where 1=1 \n");
			this.addSqlWhere("p"+realPid, sql, args, " and a.parentid=?");
		}else{
			sql.append(") a start with a.parentid = 'p"+realPid+"' connect by prior a.id =  a.parentid \n");
			sql.append(") a where a.searchType=1 \n");
			this.addSqlWhereLike(name, sql, args, " and a.name like ?");
		}
		return this.pagedQuery(sql.toString(), " a.orderBy desc,a.recordcreatetime desc", args.toArray(), ItemStagedInfo.class);
	}
	/**
	 * 项目阶段用于选择（APP）
	 * @param comId 企业号
	 * @param itemId 项目主键
	 * @param stagePid 阶段所在文件夹主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemStagedInfo> itemStageListForRelevance(Integer comId,Integer itemId, Integer stagePid){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from ( \n");
		sql.append("select a.*,connect_by_isleaf isLeaf from (\n");
		//项目文件夹
		sql.append("select 'p'||a.id id,a.id as realId,'p'||a.id as key,a.recordcreatetime,\n");
		sql.append(" a.stagedname as name,b.username,a.stagedorder as orderBy,\n");
		sql.append("'p'||a.parentid as parentId,a.parentid as realPid from stagedItem a  \n");
		sql.append("inner join Userinfo b on  a.creator = b.id \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		sql.append(") a where LEVEL=1 start with a.parentid = 'p"+stagePid+"' connect by prior a.id =  a.parentid \n");
		sql.append("order siblings by a.orderBy desc,a.id \n");
		sql.append(") a where 1=1 \n");
		return this.pagedQuery(sql.toString(), " a.orderBy desc,a.recordcreatetime desc", args.toArray(), ItemStagedInfo.class);
	}
	/**
	 * 获取选择项目阶段集合
	 * @param comId
	 * @param itemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemStagedInfo> listStagedItemForRelevance(Integer comId,Integer itemId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from ( \n");
		sql.append("select 'p'||a.id id,a.id as realId,'p'||a.id as key,a.recordcreatetime,a.stagedname as name,b.username,a.stagedorder as orderBy,\n");
		sql.append("'p'||a.parentid as parentId,a.parentid as realPid,'folder' as type,0 as moduleId from stagedItem a  \n");
		sql.append("inner join Userinfo b on  a.creator = b.id \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		sql.append(") a start with a.parentid = 'p-1' connect by prior a.id =  a.parentid \n");
		sql.append("order siblings by   a.orderBy desc,a.recordcreatetime desc \n");
		return this.listQuery(sql.toString(), args.toArray(), ItemStagedInfo.class);
	}
	/**
	 * 获取项目环节信息
	 * @param itemId
	 * @param stagedItemId
	 * @param comId
	 * @return
	 */
	public StagedItem queryStagedItem(Integer itemId,Integer stagedItemId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.itemname from stagedItem a \n");
		sql.append("inner join item b  on a.comId = b.comId and a.itemid = b.id \n");
		sql.append("where a.comId = ? and a.itemid = ? and a.id = ? \n");
		args.add(comId);
		args.add(itemId);
		args.add(stagedItemId);
		return (StagedItem)this.objectQuery(sql.toString(), args.toArray(), StagedItem.class);
	}
	/**
	 * 项目数节点拖拽事件更新
	 * @param nodeId
	 * @param pId
	 * @param nodeType
	 * @param comId
	 */
	public void zTreeOnDrop(Integer nodeId,Integer pId,Integer itemId,String nodeType,Integer comId){
		if(null==nodeType || "".equals(nodeType)){
			return ;
		}
		StringBuffer sql = new StringBuffer();
		if("folder".equals(nodeType)){
			sql.append("update stagedItem a set a.parentId=:parentId where a.comId=:comId and a.id=:id and a.itemId=:itemId");
			StagedItem stagedItem = new StagedItem();
			stagedItem.setParentId(pId);
			stagedItem.setComId(comId);
			stagedItem.setId(nodeId);
			stagedItem.setItemId(itemId);
			this.update(sql.toString(), stagedItem);
		}else if("file".equals(nodeType) || "task".equals(nodeType)){
			sql.append("update stagedInfo a set a.stagedItemId=:stagedItemId where a.comId=:comId and a.id=:id and a.itemId=:itemId");
			StagedInfo stagedInfo = new StagedInfo();
			stagedInfo.setStagedItemId(pId);
			stagedInfo.setComId(comId);
			stagedInfo.setId(nodeId);
			stagedInfo.setItemId(itemId);
			this.update(sql.toString(), stagedInfo);
		}else if("itemUpfile".equalsIgnoreCase(nodeType)){
			sql.append("update itemUpfile a set a.stagedItemId=:stagedItemId where a.comId=:comId and a.id=:id and a.itemId=:itemId");
			StagedInfo stagedInfo = new StagedInfo();
			stagedInfo.setStagedItemId(pId);
			stagedInfo.setComId(comId);
			stagedInfo.setId(nodeId);
			stagedInfo.setItemId(itemId);
			this.update(sql.toString(), stagedInfo);
		}else if("itemTalkfile".equalsIgnoreCase(nodeType)){
			sql.append("update itemTalkfile a set a.stagedItemId=:stagedItemId where a.comId=:comId and a.id=:id and a.itemId=:itemId");
			StagedInfo stagedInfo = new StagedInfo();
			stagedInfo.setStagedItemId(pId);
			stagedInfo.setComId(comId);
			stagedInfo.setId(nodeId);
			stagedInfo.setItemId(itemId);
			this.update(sql.toString(), stagedInfo);
			
		}
	}
	/**
	 * 获取项目环节信息
	 * @param id 项目阶段明细主键
	 * @param itemId 项目主键
	 * @param comId 企业号
	 * @param nodeType 节点类别
	 * @return
	 */
	public StagedInfo queryStagedInfo(Integer id,Integer itemId,Integer comId,String nodeType){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		if("task".equals(nodeType)){
			sql.append("select a.*,b.taskname as moduleName from stagedInfo a \n");
			sql.append("inner join task b on a.comId = b.comId and a.moduleId = b.id \n");
		}else if("file".equals(nodeType)){
			sql.append("select a.*,b.filename as moduleName from stagedInfo a \n");
			sql.append("inner join upfiles b on a.comId = b.comId and a.moduleId = b.id \n");
		}
		sql.append("where a.comId=? and a.itemId=? and a.id =? and a.moduleType=?");
		args.add(comId);
		args.add(itemId);
		args.add(id);
		args.add(nodeType);
		return (StagedInfo)this.objectQuery(sql.toString(), args.toArray(),StagedInfo.class);
	}
	/**
	 * 查询项目阶段文件夹下的所有子集
	 * @param comId
	 * @param itemId
	 * @param pId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemStagedInfo> listStagedItemChildren(Integer comId,Integer itemId,Integer pId){
		//TODO 项目附件以及留言附件（用于删除）
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from ( \n");
		//项目阶段
		sql.append("select 'p'||a.id id,a.id as realId,'p'||a.id as key,a.recordcreatetime,a.stagedname as name,b.username,\n");
		sql.append("a.stagedorder as orderBy,'p'||a.parentid as parentId,a.parentid as realPid,'folder' as type,0 as moduleId  \n");
		sql.append(",sum(case when c.id is not null then 1 else 0 end) as children \n");
		sql.append("from stagedItem a \n");
		sql.append("inner join Userinfo b on  a.creator = b.id \n");
		sql.append("left join stagedItem c on a.comId = c.comId and a.id = c.parentid \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		sql.append("group by a.id,a.recordcreatetime,a.stagedname,b.username,a.stagedorder,a.parentid \n");
		args.add(comId);
		args.add(itemId);
		//项目阶段的任务
		sql.append("union \n");
		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.taskname as name,c.username,\n");
		sql.append("0 as orderBy,'p'||d.id as parentId,d.id as realPid,a.moduleType as type,a.moduleId,0 as children from stagedInfo a  \n");
		sql.append("inner join task b on a.comId = b.comId and a.moduleid = b.id and a.moduletype='task' \n");
		sql.append("inner join userInfo c on  b.owner = c.id \n");
		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
		sql.append("where b.delState=0 and  a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		//项目阶段的附件
		sql.append("union \n");
		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.filename as name,c.username,\n");
		sql.append("0 as orderBy,'p'||d.id as parentId,d.id as realPid,a.moduleType as type,a.moduleId,0 as children from stagedInfo a \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.moduleid = b.id and a.moduletype='file' \n");
		sql.append("inner join userInfo c on  a.creator = c.id \n");
		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		//项目的附件
		sql.append("union \n");
		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.filename as name,c.username,\n");
		sql.append("0 as orderBy,'p'||d.id as parentId,d.id as realPid,'itemUpfile' as type,a.upfileId moduleId,0 as children from itemUpfile a \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileId = b.id \n");
		sql.append("inner join userInfo c on  a.userId = c.id \n");
		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		//项目留言的附件
		sql.append("union \n");
		sql.append("select 'a'||a.id id,a.id as realId,'a'||a.id as key,a.recordcreatetime,b.filename as name,c.username,\n");
		sql.append("0 as orderBy,'p'||d.id as parentId,d.id as realPid,'itemTalkfile' as type,a.upfileId moduleId,0 as children from itemTalkfile a \n");
		sql.append("inner join upfiles b on a.comId = b.comId and a.upfileId = b.id \n");
		sql.append("inner join userInfo c on  a.userId = c.id \n");
		sql.append("inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id \n");
		sql.append("where a.comId =? and a.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		sql.append(") a start with a.parentid = ? connect by prior a.id =  a.parentid \n");
		args.add("p"+pId);
		sql.append("order by a.orderBy desc,a.recordcreatetime desc \n");
		return this.listQuery(sql.toString(), args.toArray(), ItemStagedInfo.class);
	}
	/**
	 * 更新项目阶段所属父级节点
	 * @param comId
	 * @param oldPid
	 * @param newPid
	 */
	public Integer stagedItemParentUpdate(Integer comId,Integer oldPid,Integer newPid){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("update stagedItem a set a.parentid =? where a.comId =? and a.parentid=? ");
		args.add(newPid);
		args.add(comId);
		args.add(oldPid);
		return this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 更新项目阶段业务数据表所属阶段节点
	 * @param comId 企业号
	 * @param oldStageditemid 原来的项目阶段
	 * @param newStageditemid 现在的项目阶段
	 * @param itemId  项目主键
	 */
	public Integer stagedinfoStageditemidUpdate(Integer comId,Integer oldStageditemid,
			Integer newStageditemid, Integer itemId){
		//修改项目阶段明细的
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("update stagedinfo a set a.stageditemid =? where a.comId =? and a.itemId=? and a.stageditemid=?");
		args.add(newStageditemid);
		args.add(comId);
		args.add(itemId);
		args.add(oldStageditemid);
		return this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 将项目附件和项目留言附件重新关联项目阶段
	 * @param comId 企业号
	 * @param oldStageditemid 原来的项目阶段
	 * @param newStageditemid 现在的项目阶段
	 * @param itemId  项目主键
	 */
	public void updateFileStagedId(Integer comId,Integer oldStageditemid,
			Integer newStageditemid, Integer itemId){
		//修改项目阶段明细的
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql  = new StringBuffer("update itemUpfile a set a.stageditemid =? where a.comId =? and a.itemId=?  and a.stageditemid=?");
		args.add(newStageditemid);
		args.add(comId);
		args.add(itemId);
		args.add(oldStageditemid);
		this.excuteSql(sql.toString(), args.toArray());
		
		//修改项目留言附件
		args = new ArrayList<Object>();
		sql = new StringBuffer("update itemTalkfile a set a.stageditemid =? where a.comId =? and a.itemId=?  and a.stageditemid=?");
		args.add(newStageditemid);
		args.add(comId);
		args.add(itemId);
		args.add(oldStageditemid);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 获取项目阶段信息集合
	 * @param stagedItem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StagedItem> listStagedItem(Integer comId,Integer itemId,Integer parentId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from stagedItem a where a.comId=? and a.itemid=? and a.parentid =? order by a.stagedorder asc");
		args.add(comId);
		args.add(itemId);
		args.add(parentId);
		return this.listQuery(sql.toString(), args.toArray(), StagedItem.class);
	}
	/**
	 * 获取项目阶段内文件夹排序号
	 * @param stagedItem
	 * @return
	 */
	public StagedItem initStagedFolderOrder(StagedItem stagedItem){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.stagedorder)+1 as stagedOrder from stagedItem a where a.comId =? and a.itemid=? and a.parentId=?");
		args.add(stagedItem.getComId());
		args.add(stagedItem.getItemId());
		args.add(stagedItem.getId());
		return (StagedItem)this.objectQuery(sql.toString(), args.toArray(),StagedItem.class);
	}
	/**
	 * 判断原负责人是否在分享人中
	 * @param itemId 项目主键
	 * @param comId 企业编号
	 * @param owner 项目负责人
	 * @return
	 */
	public ItemSharer getSharerForOwner(Integer itemId, Integer comId,
			Integer owner) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//原有项目参与人
		sql.append("select a.*from itemSharer a \n");
		sql.append("where  1=1 \n");
		this.addSqlWhere(itemId, sql, args, " and  a.itemId = ? ");
		this.addSqlWhere(comId, sql, args, " and  a.comId= ? ");
		this.addSqlWhere(owner, sql, args, " and  a.userId= ? ");
		return (ItemSharer) this.objectQuery(sql.toString(), args.toArray(), ItemSharer.class);
	}
	/**
	 * 获取项目的最新的一级项目阶段对象
	 * @param comId
	 * @param itemId
	 * @return
	 */
	public StagedItem queryTheLatestStagedItem(Integer comId,Integer itemId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( \n");
		sql.append("select * from stagedItem a where a.comId=? and a.parentid = ? and a.itemid=? order by a.stagedorder desc \n");
		args.add(comId);
		args.add(-1);
		args.add(itemId);
		sql.append(") a where rownum=1 \n");
		return (StagedItem)this.objectQuery(sql.toString(), args.toArray(), StagedItem.class);
	}
	/**
	 * 取得非当前阶段最新的一级项目阶段对象
	 * @param comId 企业号
	 * @param itemId 项目主键
	 * @param stagedItemId 项目阶段主键
	 * @return
	 */
	public StagedItem getLatestStagedItem(Integer comId,Integer itemId,Integer stagedItemId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( \n");
		sql.append("select * from stagedItem a where a.comId=? and a.parentid = ? and a.itemid=? and a.id<>? order by a.stagedorder desc \n");
		args.add(comId);
		args.add(-1);
		args.add(itemId);
		args.add(stagedItemId);
		sql.append(") a where rownum=1 \n");
		return (StagedItem)this.objectQuery(sql.toString(), args.toArray(), StagedItem.class);
	}
	/**
	 * 根据业务模块主键和业务类型查询项目阶段明细
	 * @param comId
	 * @param itemId
	 * @param moduleId
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StagedInfo> listStagedInfoBymoduleIdAndType(Integer comId,Integer moduleId,String type){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from stagedInfo a where a.comId =? and a.moduleid =?  and a.moduletype =? \n");
		args.add(comId);
		args.add(moduleId);
		args.add(type);
		return this.listQuery(sql.toString(), args.toArray(), StagedInfo.class);
	}
	/**
	 * 項目讨论的附件
	 * @param comId 企业编号
	 * @param itemId  项目主键
	 * @param talkId 讨论主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemTalkFile> listItemTalkFile(Integer comId, Integer itemId,
			Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.itemId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("a.stagedItemId,a.talkId,c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, \n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from itemTalkFile a inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comId =? and a.itemId = ? and a.talkId=?\n");
		args.add(comId);
		args.add(itemId);
		args.add(talkId);
		sql.append("order by isPic asc, a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), ItemTalkFile.class);
	}
	/**
	 * 待删除的项目讨论
	 *@param comId 企业编号
	 * @param talkId 讨论的主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemTalk> listItemTalkForDel(Integer comId, Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from itemTalk where comId=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(talkId);
		return this.listQuery(sql.toString(), args.toArray(), ItemTalk.class);
	}
	/**
	 * 获取项目所有参与人集合
	 * @param comId
	 * @param itemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemSharer> listItemOwners(Integer comId,Integer itemId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.owner as userId,a.id as itemId,c.username as sharerName from item a \n");
		sql.append("inner join Userinfo c on  a.owner = c.id \n");
		sql.append("where a.comId = ? and a.id = ? \n");
		args.add(comId);
		args.add(itemId);
		sql.append("union \n");
		sql.append("select b.userid,b.itemId,d.username as sharerName from ItemSharer b \n");
		sql.append("inner join Userinfo d on  b.userid = d.id \n");
		sql.append("where b.comId = ? and b.itemId=? \n");
		args.add(comId);
		args.add(itemId);
		sql.append("union \n");
		sql.append("select a.developLeader as userId,a.id as itemId,c.username as sharerName from item a \n");
		sql.append("inner join Userinfo c on  a.developLeader = c.id \n");
		sql.append("where a.comId = ? and a.id = ? \n");
		args.add(comId);
		args.add(itemId);
		//督察人员
//		sql.append("union \n");
//		sql.append("select f.userid,? as itemId,e.username as sharerName from forceinpersion f \n");
//		args.add(itemId);
//		sql.append("inner join Userinfo e on f.userid=e.id \n");
//		sql.append("inner join userOrganic uOrg3 on f.comId = uOrg3.Comid and e.id = uOrg3.Userid and uOrg3.Enabled=1 \n");
//		sql.append("where f.comId = ? and f.type=? \n");
//		args.add(comId);
//		args.add(BusinessTypeConstant.type_item);
		//项目组成员获取
		sql.append("union \n");
		sql.append("select b.id as userId,a.itemId,b.username as sharerName from view_item_group_userId a \n");
		sql.append("inner join userInfo b on a.userId = b.id \n");
		sql.append("where a.comId=? and a.itemId=? \n");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(),args.toArray(),ItemSharer.class);
	}
	/**
	 * 获取项目所有参与人集合没有督察人员(在岗的)
	 * @param comId
	 * @param itemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listItemOwnersNoForce(Integer comId,Integer itemId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//项目的负责人
		sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from item a \n");
		sql.append("inner join Userinfo c on  a.owner = c.id \n");
		sql.append("inner join userorganic d on  a.comId = d.comId and c.id=d.userid and d.enabled=1 \n");
		sql.append("where a.comId = ? and a.id = ? \n");
		args.add(comId);
		args.add(itemId);
		//项目的研发负责人
		sql.append("union \n");
		sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from item a \n");
		sql.append("inner join Userinfo c on  a.developLeader = c.id \n");
		sql.append("inner join userorganic d on  a.comId = d.comId and c.id=d.userid and d.enabled=1 \n");
		sql.append("where a.comId = ? and a.id = ? \n");
		args.add(comId);
		args.add(itemId);
		//项目参与人
		sql.append("union \n");
		sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from ItemSharer b \n");
		sql.append("inner join Userinfo c on  b.userid = c.id \n");
		sql.append("inner join userorganic d on  b.comId = d.comId and c.id=d.userid and d.enabled=1 \n");
		sql.append("where b.comId = ? and b.itemId=? \n");
		args.add(comId);
		args.add(itemId);
		//项目组成员获取
		sql.append("union \n");
		sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from view_item_group_userId a \n");
		sql.append("inner join userInfo c on a.userId = c.id \n");
		sql.append("inner join userorganic d on  a.comId = d.comId and c.id=d.userid and d.enabled=1 \n");
		sql.append("where a.comId=? and a.itemId=? \n");
		args.add(comId);
		args.add(itemId);
		//关注项目的成员获取
		sql.append("union \n");
		sql.append("select c.id,c.email,c.wechat,c.qq,c.userName from attention a \n");
		sql.append("inner join userInfo c on a.userId = c.id \n");
		sql.append("and a.busType="+ConstantInterface.TYPE_ITEM+" \n");
		sql.append("inner join userorganic d on  a.comId = d.comId and c.id=d.userid and d.enabled=1 \n");
		sql.append("where a.comId=? and a.busId=? \n");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(),args.toArray(),UserInfo.class);
	}
	/**
	 * 获取项目讨论记录集合FOR索引
	 * @param comId
	 * @param itemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemTalk> listItemTalk4Index(Integer comId,Integer itemId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.content,b.username as speakerName from itemTalk a \n");
		sql.append("inner join userInfo b on  a.userid = b.id \n");
		sql.append("where a.comId = ? and a.itemid = ? \n");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(),args.toArray(),ItemTalk.class);
	}
	/**
	 * 获取项目子项目集合为其创建索引
	 * @param comId
	 * @param itemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listSonItem4Index(Integer comId,Integer itemId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.itemname from item a where a.comId = ? and a.delstate=0 and a.parentid=? \n");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(),args.toArray(),Item.class);
	}
	/**
	 * 验证模块数据是否在项目阶段里面已经被关联
	 * @param comId 企业主键
	 * @param itemId 项目主键
	 * @param stagedItemId 项目阶段主键
	 * @param moduleId 关联业务主键
	 * @param moduleType 关联业务类型
	 * @return
	 */
	public StagedInfo queryItemHaveStagedInfo(Integer comId,Integer itemId,Integer stagedItemId,Integer moduleId,String moduleType){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from stagedInfo a \n");
		sql.append("where a.comId =? and a.itemid =? and a.stageditemid =? and a.moduleid =? and a.moduletype =? \n");
		args.add(comId);
		args.add(itemId);
		args.add(stagedItemId);
		args.add(moduleId);
		args.add(moduleType);
		return (StagedInfo) this.objectQuery(sql.toString(), args.toArray(),StagedInfo.class);
	}


	/**
	 * 项目阶段文件夹下一级数量
	 * @param id 项目阶段主键
	 * @param itemId 项目主键
	 * @param comId 企业编号
	 * @return
	 */
	public Integer countItemStageChildren(Integer id, Integer itemId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(*) from (");
		sql.append("\n select id from stagedInfo where comID=? and itemid=? and stagedItemId=?");
		sql.append("\n union ");
		sql.append("\n select id from stagedItem where comID=? and itemid=? and parentId=?");
		sql.append("\n union ");
		sql.append("\n select id from itemUpfile where comID=? and itemid=? and stagedItemId=?");
		sql.append("\n union ");
		sql.append("\n select id from itemTalkfile where comID=? and itemid=? and stagedItemId=?");
		sql.append("\n )a");
		
		args.add(comId);
		args.add(itemId);
		args.add(id);
		
		args.add(comId);
		args.add(itemId);
		args.add(id);
		
		args.add(comId);
		args.add(itemId);
		args.add(id);
		
		args.add(comId);
		args.add(itemId);
		args.add(id);
		
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 查找欲当父节点的上级中是否包含子节点
	 * @param itemId
	 * @param childId
	 * @param parentId
	 * @param userInfo
	 * @return
	 */
	public StagedItem zTreeBeforeDropCheck(int itemId,int childId,int parentId,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from stagedItem a");
		sql.append("\n where a.comId = ? and a.itemid = ? and a.parentid=?");
		args.add(userInfo.getComId());
		args.add(itemId);
		args.add(childId);
		sql.append("\n start with a.id= ?");
		args.add(parentId);
		sql.append("\n connect by prior a.parentid = a.id");
		return (StagedItem)this.objectQuery(sql.toString(),args.toArray(),StagedItem.class);
	}
	/**
	 * 查找上级人员
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ImmediateSuper> listImmediateSuper(Integer itemId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select leader from myLeaders where creator exists (");
		sql.append("\n select b.owner from  item b where b.id=? and b.comId=?");
		args.add(itemId);
		args.add(comId);
		sql.append("\n ) and creator <> leader and comId = ?");
		args.add(comId);
		sql.append("\n union all");
		//项目参与人
		sql.append("\n select b.userid from  itemsharer b where b.itemId=? and b.comId=?");
		args.add(itemId);
		args.add(comId);
		sql.append("\n )");
		sql.append("\n connect by prior a.leader = a.creator");
		sql.append("\n )a group by a.leader");
		return this.listQuery(sql.toString(), args.toArray(), ImmediateSuper.class);
	}
	/**
	 * 人员负责的项目
	 * @param comId 企业号
	 * @param userId 负责人主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listUserAllItem(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.state,a.owner,a.itemname from  item a");
		sql.append("\n where a.comId=? and a.owner=? and a.delstate=0");
		args.add(comId);
		args.add(userId);
		return this.listQuery(sql.toString(), args.toArray(), Item.class);
	}
	/**
	 * 项目移交记录
	 * @param itemId 项目主键
	 * @param comId企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FlowRecord> listFlowRecord(Integer itemId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.*,c.touser userId from ( ");
		sql.append("\n 	select * from ( ");
		sql.append("\n 		select * from ( ");
		sql.append("\n 			select a.recordcreatetime acceptDate,c.gender,c.username,d.uuid,a.fromuser,a.touser,0 neworder");
		sql.append("\n 			from itemhandover a inner join userorganic b on a.comId=b.comId and a.fromuser=b.userid");
		sql.append("\n 			inner join userinfo c on b.userid=c.id");
		sql.append("\n 			left join upfiles d on b.mediumheadportrait=d.id");
		sql.append("\n			where a.comId=? and a.itemid=?");
		args.add(comId);
		args.add(itemId);
		sql.append("\n 			order by a.id ");
		sql.append("\n 		)a where rownum=1 ");
		sql.append("\n 	)a where a.fromuser<>a.touser");
		sql.append("\n 	union all  ");
		sql.append("\n 	select b.*,rownum neworder from ( ");
		sql.append("\n 		select a.recordcreatetime acceptDate,c.gender,c.username,d.uuid,a.fromuser,a.touser");
		sql.append("\n 		from itemhandover a inner join userorganic b on a.comId=b.comId and a.touser=b.userid");
		sql.append("\n 		inner join userinfo c on b.userid=c.id");
		sql.append("\n 		left join upfiles d on b.mediumheadportrait=d.id");
		sql.append("\n 		where a.comId=? and a.itemid=?");
		args.add(comId);
		args.add(itemId);
		sql.append("\n 		order by a.id ");
		sql.append("\n 	)b");
		sql.append("\n )c order by neworder desc");
		return this.listQuery(sql.toString(), args.toArray(), FlowRecord.class);
	}
	/**
	 * 获取项目成员组集合
	 * @param itemId
	 * @param userId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> listSelfGroupOfItem(int itemId,int userId,int comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,case when b.id is null then 0 else 1 end as checked from selfgroup a \n");
		sql.append("left join itemShareGroup b on a.comId = b.comId and a.id = b.grpid and b.itemid=? \n");
		args.add(itemId);
		sql.append("where a.comId =? and a.owner=? \n");
		args.add(comId);
		args.add(userId);
		return this.pagedQuery(sql.toString(),"checked desc,a.id desc", args.toArray(),SelfGroup.class);
	}
	/**
	 * 获取与项目已经关联的组集合
	 * @param itemId
	 * @param userId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> listShareGroupOfItem(int itemId,int comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from selfgroup a \n");
		sql.append("inner join itemShareGroup b on a.comId = b.comId and a.id = b.grpid \n");
		sql.append("where a.comId =? and b.itemid=? \n");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(), args.toArray(),SelfGroup.class);
	}
	/**
	 * 获取与查询项目名称相似的项目集合
	 * @param itemName
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listSimilarItemsByCheckItemName(String itemName,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.itemname,b.username as ownerName,a.recordcreatetime,b.gender,d.uuid,d.filename from item a \n");
		sql.append("inner join Userinfo b on a.owner = b.id \n");
		sql.append("inner join userOrganic c on a.comId = c.comId and b.id = c.userid \n");
		sql.append("left join upfiles d on c.mediumHeadPortrait = d.id \n");
		sql.append("where a.comId = ? and a.delstate=0 and c.enabled=1 \n");
		args.add(comId);
		this.addSqlWhereLike(itemName,sql,args," and a.itemname like ? \n");
		return this.pagedQuery(sql.toString(), " a.modifydate desc", args.toArray(), Item.class);
	}
	/**
	 * 查询项目当前设置父节点的父节点
	 * @param parentId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> getParentItem(Integer parentId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from item a \n");
		sql.append("where a.comId = ? and a.delstate=0 and a.id<>? start with id=? CONNECT BY PRIOR parentid =id \n");
		args.add(comId);
		args.add(parentId);
		args.add(parentId);
		sql.append("order by a.id desc  \n");
		return this.listQuery(sql.toString(),args.toArray(),Item.class);
	}
	/**
	 * 获取自己权限下前N个项目集合
	 * @param userInfo
	 * @param isForceInPersion
	 * @param rowNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> firstNItemList(UserInfo userInfo,boolean isForceInPersion,Integer rowNum){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from ( \n");
		sql.append("select a.*, \n");
		
		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as readstate, \n");
		
		sql.append("case when atten.id is null then 0 else 1 end as attentionState  from ( \n");
		sql.append("select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username as ownerName, \n");
		sql.append("case when a.modifyDate is null then a.recordcreatetime else a.modifyDate end modifyDate\n");
		sql.append("from item a  \n");
		sql.append("left join itemsharer b on a.comId = b.comId and a.id = b.itemid \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_ITEM+"' and today.userId=?  \n ");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//查看范围限制
		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append(" b.userid =? or a.owner =? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = b.userid and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//负责人上级权限验证
			sql.append(" or exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			//项目成员组权限验证
			sql.append(" or isInItemShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
		}
		sql.append("group by a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username,a.modifyDate\n");
		sql.append(")a left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ITEM+"' and atten.userId=? \n");
		args.add(userInfo.getId());
		sql.append("order by readstate,a.modifyDate desc,a.id desc \n");
		sql.append(") where rownum <= ?");
		args.add(rowNum);
		return this.listQuery(sql.toString(),args.toArray(),Item.class);
	}
	/**
	 * 判断移除的人员是否仍有查看权限
	 * @param itemId 项目主键
	 * @param comId 企业号
	 * @param userId 移除人员主键
	 * @return
	 */
	public Integer countUserNum(Integer itemId, Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("\n select count(*) from ( ");
		//移除人员是否为客户负责人
		sql.append("\n select a.id from item a where 1=1");
		this.addSqlWhere(itemId, sql, args, " and a.id=?");
		this.addSqlWhere(userId, sql, args, " and a.owner=?");
		sql.append("\n union all");
		//移除人员是否为研发负责人
		sql.append("\n select a.id from item a where 1=1");
		this.addSqlWhere(itemId, sql, args, " and a.id=?");
		this.addSqlWhere(userId, sql, args, " and a.developLeader=?");
		sql.append("\n union all");
		//移除人员是否为参与人
		sql.append("\n select a.itemId from itemSharer a where 1=1");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		sql.append("\n union all");
		//移除人员是否在共享组
		sql.append("\n select a.itemId from  itemShareGroup a inner join grouppersion b on a.comId=b.comId");
		sql.append("\n and a.grpid=b.grpid");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
		this.addSqlWhere(userId, sql, args, " and b.userinfoid=?");
		sql.append("\n)");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 取得本次移除分组的成员
	 * @param itemId 项目主键
	 * @param comId 企业号
	 * @param grpId 分组主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listRemoveGrpUser(Integer itemId, Integer comId,
			Integer grpId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//本次移除分组成员
		sql.append("\n select a.userinfoid id from grouppersion a where 1=1 ");
		this.addSqlWhere(grpId, sql, args, " and a.grpId=?");
		sql.append("\n minus");
		//项目负责人
		sql.append("\n select a.owner id from item a where 1=1 ");
		this.addSqlWhere(itemId, sql, args, " and a.id=?");
		sql.append("\n minus ");
		//研发负责人
		sql.append("\n select a.developLeader id from item a where 1=1 ");
		this.addSqlWhere(itemId, sql, args, " and a.id=?");
		sql.append("\n minus ");
		//项目分享人
		sql.append("\n select a.userId id from itemSharer a where 1=1 ");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
		sql.append("\n minus ");
		//项目分享组
		sql.append("\n select b.userinfoid id from itemShareGroup a inner join grouppersion b");
		sql.append("\n on a.comId=b.comId and a.grpId=b.grpid ");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
		
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 项目分享组中需要移除的成员
	 * @param comId 企业号
	 * @param itemId 项目主键
	 * @param grpIds 分组主键集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listRemoveCrmGrp(Integer comId, Integer itemId,
			Integer[] grpIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//项目原有分享组
		sql.append("\n select b.userinfoid id from itemShareGroup a inner join grouppersion b");
		sql.append("\n on a.comId=b.comId and a.grpId=b.grpid ");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
		sql.append("\n minus");
		//项目负责人
		sql.append("\n select a.owner id from item a where 1=1 ");
		this.addSqlWhere(itemId, sql, args, " and a.id=?");
		sql.append("\n minus ");
		//研发负责人
		sql.append("\n select a.developLeader id from item a where 1=1 ");
		this.addSqlWhere(itemId, sql, args, " and a.id=?");
		sql.append("\n minus ");
		//项目分享人
		sql.append("\n select a.userId id from itemSharer a where 1=1 ");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
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
	 * 直接合并留言信息和附件
	 * @param comId 企业号
	 * @param itemCId 待整合的项目主键
	 * @param itemId 合并欧的项目主键
	 * 
	 */
	public void compressTalk(Integer comId, Integer itemCId, Integer itemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并反馈信息
		sql.append(" update itemTalk set itemId=? where comId=? and itemId=?");
		args.add(itemId);
		args.add(comId);
		args.add(itemCId);
		this.excuteSql(sql.toString(), args.toArray());
		//合并留言附件
		args = new ArrayList<Object>();
		sql = new StringBuffer();
		sql.append(" update itemTalkFile set itemId=? where comId=? and itemId=?");
		args.add(itemId);
		args.add(comId);
		args.add(itemCId);
		this.excuteSql(sql.toString(), args.toArray());
		//合并项目附件
		args = new ArrayList<Object>();
		sql = new StringBuffer();
		sql.append(" update itemUpfile set itemId=? where comId=? and itemId=?");
		args.add(itemId);
		args.add(comId);
		args.add(itemCId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 直接合并留言信息和附件
	 * @param comId 企业号
	 * @param itemCId 待整合的项目主键
	 * @param itemId 合并后的项目主键
	 * 
	 */
	public void compressTask(Integer comId, Integer itemCId, Integer itemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update task set busId=? where comId=? and busId=? and busType='005'");
		args.add(itemId);
		args.add(comId);
		args.add(itemCId);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	
	/**
	 * 直接合并需求信息
	 * @author hcj 
	 * @date: 2018年10月16日 下午4:38:54
	 * @param comId
	 * @param itemCId
	 * @param itemId
	 */
	public void compressDemand(Integer comId, Integer itemCId, Integer itemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update demandProcess set itemId=? where comId=? and itemId=?");
		args.add(itemId);
		args.add(comId);
		args.add(itemCId);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 直接合并项目阶段明细信息
	 * @param comId 企业号
	 * @param itemCId 待整合的项目主键
	 * @param itemId 合并后的项目主键
	 */
	public void compressStageInfo(Integer comId, Integer itemCId, Integer itemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update stagedInfo set itemId=? where comId=? and itemId=?");
		args.add(itemId);
		args.add(comId);
		args.add(itemCId);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 直接合并项目阶段信息
	 * @param comId 企业号
	 * @param itemCId 待整合的项目主键
	 * @param itemId 合并后的项目主键
	 * @param itemName 
	 */
	public void compressStageItem(Integer comId, Integer itemCId, Integer itemId, String itemName) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update stagedItem set itemId=? where comId=? and itemId=? and parentId>0");
		args.add(itemId);
		args.add(comId);
		args.add(itemCId);
		this.excuteSql(sql.toString(), args.toArray());
		
		//合并反馈信息附件的分类
		args = new ArrayList<Object>();
		sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update stagedItem set itemId=?,stagedname='("+itemName+")'||stagedname where comId=? and itemId=? and parentId=-1");
		args.add(itemId);
		args.add(comId);
		args.add(itemCId);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 直接合并项目阶段信息
	 * @param comId 企业号
	 * @param itemCId 待整合的项目主键
	 * @param itemId 合并后的项目主键
	 */
	public void compressSonItem(Integer comId, Integer itemCId, Integer itemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并项目关联客户信息
		sql.append("update item set parentId=? where comId=? and parentId=?");
		args.add(itemId);
		args.add(comId);
		args.add(itemCId);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 项目的所有父项目包括自己，但没有预删除的项目
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemOfParent(Integer itemId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from item a where a.comId=? and a.delstate=0 start with a.id=? connect by prior a.parentid =  a.id");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(), args.toArray(), Item.class);
	}
	/**
	 * 项目的所有父项目包括自己和预删除的
	 * @param itemId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listItemOfAllParent(Integer itemId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from item a where a.comId=? start with a.id=? connect by prior a.parentid =  a.id");
		args.add(comId);
		args.add(itemId);
		return this.listQuery(sql.toString(), args.toArray(), Item.class);
	}
	/**
	 * 任务关联的项目明细
	 * @param comId 企业号
	 * @param moduleType 关联模块类型
	 * @param moduleId 关联模块主键
	 * @return
	 */
	public StagedInfo getStagedInfo(Integer comId, Integer moduleId,String moduleType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from stagedInfo a where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(moduleId, sql, args, " and a.moduleId=?");
		this.addSqlWhere(moduleType, sql, args, " and a.moduleType=?");
		return (StagedInfo) this.objectQuery(sql.toString(), args.toArray(), StagedInfo.class);
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
	 * 获取项目下的所有任务
	 * @param comId 团队主键
	 * @param itemId 项目主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Task> listTaskOfItem(Integer comId,Integer itemId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.id,a.recordcreatetime,b.taskname,b.dealtimelimit,c.username as ownerName,");
		sql.append("\n a.moduleId,f.uuid,f.filename from stagedInfo a ");
		sql.append("\n inner join task b on a.comId = b.comId and a.moduleid = b.id and a.moduletype='task' ");
		sql.append("\n inner join userInfo c on  b.owner = c.id ");
		sql.append("\n inner join stagedItem d on a.comId = d.comId and a.stageditemid = d.id ");
		sql.append("\n inner join userOrganic org on c.id =org.userId and a.comId=org.comId ");
		sql.append("\n left join upfiles f on org.mediumHeadPortrait = f.id");
		sql.append("\n where b.delState=0 and  a.comId =? and a.itemid=?");
		args.add(comId);
		args.add(itemId);
		return this.pagedQuery(sql.toString(), "a.recordcreatetime desc,a.id desc", args.toArray(), Task.class);
	}
	/**
	 * 列出客户关联的项目
	 * @param item 项目
	 * @param userInfo 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> listPagedCrmItem(Item item, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.comId,a.itemname,a.partnerid,a.owner,a.state,a.itemProgress,b.username as ownerName,a.recordcreatetime,b.gender,");
		sql.append("\n d.uuid,d.filename,case when atten.id is null then 0 else 1 end as attentionState");
		sql.append("\n from item a");
		sql.append("\n inner join Userinfo b on a.owner = b.id");
		sql.append("\n inner join userOrganic c on a.comId = c.comId and b.id = c.userid");
		sql.append("\n left join upfiles d on c.mediumHeadPortrait = d.id");
		sql.append("\n left join attention atten on a.comId = atten.comId and a.id = atten.busid ");
		sql.append("\n and atten.bustype='"+ConstantInterface.TYPE_ITEM+"' and atten.userId="+userInfo.getId()+"");
		sql.append("\n where a.delstate=0 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(item.getPartnerId(), sql, args, " and a.partnerid=?");
		return this.pagedQuery(sql.toString(), "a.state,a.recordcreatetime desc", args.toArray(), Item.class);
	}
	/**
	 * 查询客户关联项目总数
	 * @param item
	 * @param userInfo
	 * @return
	 */
	public Integer countItem(Item item, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(1) from ( ");
		sql.append("\n select a.id,a.itemname,a.partnerid,a.owner,a.state,a.itemProgress,b.username as ownerName,a.recordcreatetime,b.gender,");
		sql.append("\n d.uuid,d.filename,case when atten.id is null then 0 else 1 end as attentionState");
		sql.append("\n from item a");
		sql.append("\n inner join Userinfo b on a.owner = b.id");
		sql.append("\n inner join userOrganic c on a.comId = c.comId and b.id = c.userid");
		sql.append("\n left join upfiles d on c.mediumHeadPortrait = d.id");
		sql.append("\n left join attention atten on a.comId = atten.comId and a.id = atten.busid ");
		sql.append("\n and atten.bustype='"+ConstantInterface.TYPE_ITEM+"' and atten.userId="+userInfo.getId()+"");
		sql.append("\n where a.delstate=0 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(item.getPartnerId(), sql, args, " and a.partnerid=?");
		sql.append("\n )");
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 取得客户项目项目数
	 * @param busType 业务主键
	 * @param busId 业务类型
	 * @param userInfo 当前操作人员
	 * @return
	 */
	public Integer countBusItem(String busType,Integer busId, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(a.id) ");
		sql.append("\n from item a");
		sql.append("\n inner join Userinfo b on a.owner = b.id");
		sql.append("\n inner join userOrganic c on a.comId = c.comId and b.id = c.userid");
		sql.append("\n where a.delstate=0 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.partnerid=?");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 查询模块是否有相同的附件
	 * @param comId
	 * @param itemId
	 * @param upfileId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemTalkFile> listItemSimUpfiles(Integer comId, Integer itemId,
			Integer upfileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//项目模块附件
		sql.append("select a.comId,a.itemId,a.upfileId  from itemUpfile a \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		sql.append("\n union all \n");
		//项目留言附件
		sql.append("select  a.comId,a.itemId,a.upfileId from itemTalkFile a\n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		sql.append("\n union all \n");
		//项目阶段附件
		sql.append("select  a.comId,a.itemId,a.moduleId upfileId from stagedInfo a\n");
		sql.append("where a.moduleType='file'\n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.moduleId=?");
		return this.listQuery(sql.toString(), args.toArray(), ItemTalkFile.class);
	}
	
	/**
	 * 取得指定人员负责的项目数 app
	 * @param userId 指定人员主键
	 * @param comId 企业号
	 * @return
	 */
	public Integer countMyItem(Integer userId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(a.id) from item a \n");
		sql.append("where a.delstate=0 and a.comId = ? and a.owner=? \n");
		args.add(comId);
		args.add(userId);
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 分页查询项目阶段详情
	 * @param userInfo 当前操作人员
	 * @param stagedInfo 项目阶段信息
	 * @param relateMod 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<StagedInfo> listPagedStagedInfo(UserInfo userInfo,
			StagedInfo stagedInfo, String[] relateMod) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select a.*,a.userId creator,c.userName,headimg.uuid userUuid,headimg.filename headImgName,");
		sql.append("\n c.gender,upfiles.uuid fileuuid,upfiles.fileext,upfiles.filename  from (");
		sql.append("\n	select a.id infoId,a.comId,a.itemid,a.stageditemid,a.moduleid,a.moduletype,");
		sql.append("\n		case when a.moduletype='task' then task.taskname ");
		sql.append("\n			when a.moduletype='file' then files.filename");
		sql.append("\n			when a.moduletype='sp_flow' then ins.flowname");
		sql.append("\n			else '未知' end moduleName,");
		sql.append("\n		case when a.moduletype='task' then task.owner ");
		sql.append("\n			when a.moduletype='file' then a.creator");
		sql.append("\n 			when a.moduletype='sp_flow' then ins.creator");
		sql.append("\n			else 0 end userId,");
		sql.append("\n		case when a.moduletype='task' then 0 ");
		sql.append("\n			when a.moduletype='file' then files.id");
		sql.append("\n 			when a.moduletype='sp_flow' then ins.creator");
		sql.append("\n			else 0 end upfileId,");
		sql.append("\n		case when a.moduletype='task' then task.recordcreatetime  ");
		sql.append("\n			when a.moduletype='file' then files.recordcreatetime");
		sql.append("\n 			when a.moduletype='sp_flow' then ins.recordcreatetime");
		sql.append("\n			else '0' end busDateTime");
		sql.append("\n	from StagedInfo a left join task on a.comId=task.comId and a.moduletype='task' and a.moduleid=task.id");
		sql.append("\n	left join upfiles files on a.comId=files.comId and a.moduletype='file' and a.moduleid=files.id");
		sql.append("\n 	left join spflowinstance ins on a.comId=ins.comId and a.moduletype='sp_flow' and a.moduleid=ins.id");
		sql.append("\n	where 1=1 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId= ?");
		this.addSqlWhere(stagedInfo.getItemId(), sql, args, " and a.itemId =?");
		this.addSqlWhere(stagedInfo.getTaskState(), sql, args, " and task.state =  ?");//任务状态筛选
		
		sql.append("\n	union all ");
		
		sql.append("\n	select a.id infoId,a.comId,a.itemid,a.stageditemid,a.upfileid moduleid,'itemUpFile' moduletype,");
		sql.append("\n	itemUpfile.filename,a.userId,a.upfileId,itemUpFile.recordcreatetime busDateTime");
		sql.append("\n	from itemUpfile a  left join upfiles itemUpFile on a.comId=itemUpFile.comId ");
		sql.append("\n	and a.upfileid=itemUpFile.id");
		sql.append("\n	where 1=1 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId= ?");
		this.addSqlWhere(stagedInfo.getItemId(), sql, args, " and a.itemId =?");
		
		sql.append("\n	union all ");
		
		sql.append("\n	select a.id infoId,a.comId,a.itemid,a.stageditemid,a.upfileid moduleid,'itemTalkFile' moduletype,");
		sql.append("\n	itemTalkFile.filename,a.userId,a.upfileId,itemTalkFile.recordcreatetime busDateTime");
		sql.append("\n	from itemTalkfile a  left join upfiles itemTalkFile on a.comId=itemTalkFile.comId ");
		sql.append("\n	and a.upfileid=itemTalkFile.id");
		sql.append("\n	where 1=1 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId= ?");
		this.addSqlWhere(stagedInfo.getItemId(), sql, args, " and a.itemId =?");
		
		sql.append("\n)a left join userorganic b on a.comId=b.comId and a.userId=b.userid ");
		sql.append("\nleft join userinfo c on a.userId=c.id ");
		sql.append("\nleft join upfiles headimg on a.comId=headimg.comId and b.smallheadportrait=headimg.id");
		sql.append("\nleft join upfiles on a.comId=upfiles.comId and a.upfileId=upfiles.id");
		sql.append("\n where 1=1 ");
		
		Integer stagedItemId =  stagedInfo.getStagedItemId();
		if(null != stagedItemId &&  stagedItemId>-1){
			sql.append("\n and stageditemid in ( ");
			sql.append("\n  select id from stagedItem ");
			sql.append("\n   start with stagedItem.id="+stagedInfo.getStagedItemId());
			sql.append("\n  connect by prior stagedItem.id =  stagedItem.parentid");
			sql.append("\n ) ");
		}
		this.addSqlWhereIn(relateMod, sql, args, " and a.moduletype in ?");
		//模糊查询模块名称
		this.addSqlWhereLike(stagedInfo.getModuleName(), sql, args, " and a.moduleName like ?");
		return this.pagedQuery(sql.toString(), "a.busDateTime desc,a.moduletype,a.moduleid,a.stageditemid desc", args.toArray(), StagedInfo.class);
	}
	/**
	 * 统计权限下所有项目数量
	 * @param item
	 * @param curUser
	 * @param isForceIn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> countAllItem(Item item, UserInfo userInfo, boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id from item a \n");
		
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		//查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//查看范围限制
		if(!isForceInPersion){
			sql.append("and (\n");
			//任务的参与、执行、负责权限验证
			sql.append("a.owner =? or a.developLeader=? \n");
			args.add(userInfo.getId());
			args.add(userInfo.getId());
			//上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from itemsharer b where a.id = b.itemId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			
			//负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//研发负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.developLeader and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			//项目成员组权限验证
			sql.append(" or isInItemShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
		}
		//项目状态筛选
		if(null!=item.getState() && item.getState()!=0){
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		//项目负责人筛选
		if(null!=item.getOwner() && item.getOwner()!=0){
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}
		
		//项目名称筛选
		if(null!=item.getItemName() && !"".equals(item.getItemName())){
			this.addSqlWhereLike(item.getItemName(),sql,args," and a.itemname like ? \n");
		}
		
		//负责人类型
		String ownerType =  item.getOwnerType();
		if(null!=ownerType && !"".equals(ownerType)){
			if("0".equals(ownerType)){//查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.owner=?");
			}else if("1".equals(ownerType)){//查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		if(null != item.getListOwner() && !item.getListOwner().isEmpty()){
			sql.append("	 and  ( a.owner= 1 ");
			for(UserInfo owner : item.getListOwner()){
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		this.addSqlWhere(item.getAttentionState(), sql, args, " and allRow.attentionState=?");
		
		return this.listQuery(sql.toString(), args.toArray(), Item.class);
	}

	/**
	 * 获取项目列表
	 * 
	 * @param item
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemStagedInfo> listItemStagedInfo(Item item, UserInfo userInfo, boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select item.comId,item.itemname,item.owner,item.recordcreatetime ,item.state,item.ownerName,  \n ");
		sql.append("stage.id,stage.realId,stage.key,stage.itemId,stage.name  \n ");
		sql.append(" from ( \n ");
		sql.append("select outer.* from ( \n ");
		sql.append("select  inner.*,rownum as rowno  from ( \n");
		
		sql.append("select* from ( \n ");
		sql.append("select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username as ownerName,c.gender,d.uuid,d.filename, \n");
		sql.append("case when a.modifyDate is null then a.recordcreatetime else a.modifyDate end modifyDate,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as readstate, \n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState, \n");
		// 更新人信息
		sql.append("newsInfo.content modifyContent,modifier.id modifier,modifier.gender modifierGender,modifier.username as modifierName,modifierFile.uuid modifierUuid,modifierFile.filename modifierFileName \n");
		sql.append("from item a  \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"
				+ ConstantInterface.TYPE_ITEM + "' and atten.userId=? \n");
		args.add(userInfo.getId());
		// 获取更新人信息
		sql.append(" left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" and a.id = newsInfo.busId and newsInfo.busType=" + ConstantInterface.TYPE_ITEM + "\n");
		sql.append(" left join userinfo modifier on newsInfo.Userid = modifier.id  \n");
		sql.append(" left join userOrganic modifierOrg on modifier.id = modifierOrg.userId and a.comId = modifierOrg.comId \n");
		sql.append(" left join upfiles modifierFile on modifierOrg.mediumHeadPortrait = modifierFile.id \n");
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_ITEM+"' and today.userId=?  \n ");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		// 查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		// 查看范围限制
		if (!isForceInPersion) {
			sql.append("and (\n");
			// 任务的参与、执行、负责权限验证
			sql.append("a.owner =? \n");
			args.add(userInfo.getId());
			// 上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from itemsharer b where a.id = b.itemId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());

			// 负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			// 项目成员组权限验证
			sql.append(" or isInItemShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
		}
		// 项目状态筛选
		if (null != item.getState() && item.getState() != 0) {
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		// 项目负责人筛选
		if (null != item.getOwner() && item.getOwner() != 0) {
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}

		// 项目名称筛选
		if (null != item.getItemName() && !"".equals(item.getItemName())) {
			this.addSqlWhereLike(item.getItemName(), sql, args, " and a.itemname like ? \n");
		}

		// 负责人类型
		String ownerType = item.getOwnerType();
		if (null != ownerType && !"".equals(ownerType)) {
			if ("0".equals(ownerType)) {// 查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.owner=?");
			} else if ("1".equals(ownerType)) {// 查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		if (null != item.getListOwner() && !item.getListOwner().isEmpty()) {
			sql.append("	 and  ( a.owner= 1 ");
			for (UserInfo owner : item.getListOwner()) {
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		sql.append(")a  where 1=1 ");
		this.addSqlWhere(item.getAttentionState(), sql, args, " and a.attentionState=?");

		// 不查询页面已有数据
		String itemIds = item.getItemIds();
		if (null != itemIds && !"".equals(itemIds)) {
			sql.append("\n and a.id not in (" + itemIds + ") ");
		}
		sql.append("\n order by a.readstate,a.modifyDate desc,a.id desc");
		// 整个数据源小括号包含
		sql.append(") inner where rownum <=").append(PaginationContext.getOffset() + PaginationContext.getPageSize());
		sql.append("\n ) outer where outer.rowno>").append(PaginationContext.getOffset());
		sql.append("\n )item ");
		
		sql.append("\n left join ( ");
		
		
		sql.append("select a.* from ( \n");
		//项目文件夹
		sql.append("select 'p'||a.id id,a.id as realId,'p'||a.id as key,a.recordcreatetime,a.stagedname as name,b.username,a.stagedorder as orderBy,\n");
		sql.append("'p'||a.parentid as parentId,a.parentid as realPid,'folder' as type,0 as moduleId,null uuid,null fileExt,a.itemid,a.comId from stagedItem a  \n");
		sql.append("inner join Userinfo b on  a.creator = b.id \n");
		sql.append("where a.comId =?  \n");
		args.add(userInfo.getComId());
		sql.append(") a  start with a.parentid = 'p-1' connect by prior a.id =  a.parentid \n");
		sql.append("order by a.orderBy desc,a.recordcreatetime desc \n");
		sql.append(")stage on  item.comId = stage.comId and stage.itemid = item.id");
		sql.append("\n order by item.readstate,item.modifyDate desc,itemid desc");
		return this.listQuery(sql.toString(), args.toArray(), ItemStagedInfo.class);
		
	}
	
	
	
	/**
	 * 获取项目列表
	 * 
	 * @param item
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemStagedInfo> listAllItemStagedInfo(Item item, UserInfo userInfo, boolean isForceInPersion) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select item.comId,item.itemname,item.owner,item.recordcreatetime ,item.state,item.ownerName,  \n ");
		sql.append("stage.id,stage.realId,stage.key,stage.itemId,stage.name  \n ");
		sql.append(" from ( \n ");
		sql.append("select outer.* from ( \n ");
		sql.append("select  inner.*,rownum as rowno  from ( \n");
		
		sql.append("select* from ( \n ");
		sql.append("select a.comId,a.id,a.itemname,a.owner,a.recordcreatetime,a.state,c.username as ownerName,c.gender,d.uuid,d.filename, \n");
		sql.append("case when a.modifyDate is null then a.recordcreatetime else a.modifyDate end modifyDate,\n");

		//是否有待办事项
		sql.append("  case when today.readstate=0 then 0 else 1 end as readstate, \n");

		sql.append("case when atten.id is null then 0 else 1 end as attentionState, \n");
		// 更新人信息
		sql.append("newsInfo.content modifyContent,modifier.id modifier,modifier.gender modifierGender,modifier.username as modifierName,modifierFile.uuid modifierUuid,modifierFile.filename modifierFileName \n");
		sql.append("from item a  \n");
		sql.append("inner join userinfo c on  a.owner = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId \n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"
				+ ConstantInterface.TYPE_ITEM + "' and atten.userId=? \n");
		args.add(userInfo.getId());
		// 获取更新人信息
		sql.append(" left join newsInfo  on a.comId = newsInfo.comId  \n");
		sql.append(" and a.id = newsInfo.busId and newsInfo.busType=" + ConstantInterface.TYPE_ITEM + "\n");
		sql.append(" left join userinfo modifier on newsInfo.Userid = modifier.id  \n");
		sql.append(" left join userOrganic modifierOrg on modifier.id = modifierOrg.userId and a.comId = modifierOrg.comId \n");
		sql.append(" left join upfiles modifierFile on modifierOrg.mediumHeadPortrait = modifierFile.id \n");
		//操作人员的待办事项或是未读提醒
		sql.append("    left join todayworks today on a.comId = today.comId and a.id = today.busid \n ");
		sql.append("    and today.bustype='"+ConstantInterface.TYPE_ITEM+"' and today.userId=?  \n ");
		args.add(userInfo.getId());
		sql.append("where a.delstate=0 and  a.comId = ? \n");
		args.add(userInfo.getComId());
		// 查询创建时间段
		this.addSqlWhere(item.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(item.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		// 查看范围限制
		if (!isForceInPersion) {
			sql.append("and (\n");
			// 任务的参与、执行、负责权限验证
			sql.append("a.owner =? \n");
			args.add(userInfo.getId());
			// 上级权限验证
			//参与人上级权限验证
			sql.append(" or exists( \n");
			sql.append("  	select b.* from itemsharer b where a.id = b.itemId and  \n");
			sql.append("  	(b.userId =? or exists(select sup.leader from myLeaders sup where sup.comId=? and sup.creator=b.userId and sup.leader=?))\n");
			sql.append(" ) \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			args.add(userInfo.getId());

			// 负责人上级权限验证
			sql.append(" or exists( \n");
			sql.append(" 	select id from myLeaders where creator = a.owner and leader = ? and comId = ? and creator <> leader \n");
			args.add(userInfo.getId());
			args.add(userInfo.getComId());
			sql.append(" )\n");
			// 项目成员组权限验证
			sql.append(" or isInItemShareGroup(?,a.id,?) > 0 \n");
			args.add(userInfo.getComId());
			args.add(userInfo.getId());
			sql.append(") \n");
		}
		// 项目状态筛选
		if (null != item.getState() && item.getState() != 0) {
			sql.append("and a.state=? \n");
			args.add(item.getState());
		}
		// 项目负责人筛选
		if (null != item.getOwner() && item.getOwner() != 0) {
			sql.append("and a.owner =? \n");
			args.add(item.getOwner());
		}

		// 项目名称筛选
		if (null != item.getItemName() && !"".equals(item.getItemName())) {
			this.addSqlWhereLike(item.getItemName(), sql, args, " and a.itemname like ? \n");
		}

		// 负责人类型
		String ownerType = item.getOwnerType();
		if (null != ownerType && !"".equals(ownerType)) {
			if ("0".equals(ownerType)) {// 查询自己的
				this.addSqlWhere(userInfo.getId(), sql, args, " and a.owner=?");
			} else if ("1".equals(ownerType)) {// 查询下属的
				sql.append(" and exists(select id from myLeaders where creator = a.owner and comId = ? and leader = ? and leader <> creator )");
				args.add(userInfo.getComId());
				args.add(userInfo.getId());
			}
		}
		if (null != item.getListOwner() && !item.getListOwner().isEmpty()) {
			sql.append("	 and  ( a.owner= 1 ");
			for (UserInfo owner : item.getListOwner()) {
				sql.append("or a.owner=?  \n");
				args.add(owner.getId());
			}
			sql.append("	 ) ");
		}
		sql.append(")a  where 1=1 ");
		this.addSqlWhere(item.getAttentionState(), sql, args, " and a.attentionState=?");

		// 不查询页面已有数据
		String itemIds = item.getItemIds();
		if (null != itemIds && !"".equals(itemIds)) {
			sql.append("\n and a.id not in (" + itemIds + ") ");
		}
		sql.append("\n order by a.readstate,a.modifyDate desc,a.id desc");
		// 整个数据源小括号包含
		sql.append(") inner ");
		sql.append("\n ) outer ");
		sql.append("\n )item ");
		
		sql.append("\n left join ( ");
		
		
		sql.append("select a.* from ( \n");
		//项目文件夹
		sql.append("select 'p'||a.id id,a.id as realId,'p'||a.id as key,a.recordcreatetime,a.stagedname as name,b.username,a.stagedorder as orderBy,\n");
		sql.append("'p'||a.parentid as parentId,a.parentid as realPid,'folder' as type,0 as moduleId,null uuid,null fileExt,a.itemid,a.comId from stagedItem a  \n");
		sql.append("inner join Userinfo b on  a.creator = b.id \n");
		sql.append("where a.comId =?  \n");
		args.add(userInfo.getComId());
		sql.append(") a  start with a.parentid = 'p-1' connect by prior a.id =  a.parentid \n");
		sql.append("order by a.orderBy desc,a.recordcreatetime desc \n");
		sql.append(")stage on  item.comId = stage.comId and stage.itemid = item.id");
		sql.append("\n order by item.readstate,item.modifyDate desc,itemid desc");
		return this.listQuery(sql.toString(), args.toArray(), ItemStagedInfo.class);
	}
	
	/**
	 * 分页获取维护记录
	 * @author hcj 
	 * @date: 2018年10月16日 下午3:37:08
	 * @param itemUpfile
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemUpfile> listPagedMaintenanceRecord(ItemUpfile itemUpfile, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( \n");
		sql.append(" select a.taskName sourceName,'task' type,a.id,a.creator userId,u.userName creatorName,a.recordCreateTime,b.id itemId \n");
		sql.append(" from task a \n");
		sql.append(" inner join item b on b.id = a.busId and a.busType=? \n");
		args.add(ConstantInterface.TYPE_ITEM);
		sql.append(" left join userInfo u on u.id = a.creator \n");
		sql.append(" where b.id=? \n");
		args.add(itemUpfile.getItemId());
		sql.append(" union all \n");
		sql.append(" select a.serialNum sourceName,'demand' type,a.id,a.creator userId,u.userName creatorName,a.recordCreateTime,b.id itemId \n");
		sql.append(" from demandProcess a \n");
		sql.append(" inner join item b on b.id = a.itemId  \n");
		sql.append(" left join userInfo u on u.id = a.creator \n");
		sql.append(" where b.id=? \n");
		args.add(itemUpfile.getItemId());
		sql.append(") where 1=1 ");
		this.addSqlWhereLike(itemUpfile.getSourceName(), sql, args, " and sourceName like ? ");
		return this.pagedQuery(sql.toString(),"recordCreateTime desc", args.toArray(), ItemUpfile.class);
	}
	
	/**
	 * 查询维护记录总数
	 * @author hcj 
	 * @date: 2018年10月16日 下午4:24:32
	 * @param itemUpfile
	 * @param userInfo
	 * @return
	 */
	public Integer countMaintenanceRecord(Integer itemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from ( \n");
		sql.append(" select a.taskName sourceName,'task' type,a.id,a.creator userId,u.userName creatorName,a.recordCreateTime,b.id itemId \n");
		sql.append(" from task a \n");
		sql.append(" inner join item b on b.id = a.busId and a.busType=? \n");
		args.add(ConstantInterface.TYPE_ITEM);
		sql.append(" left join userInfo u on u.id = a.creator \n");
		sql.append(" where b.id=? \n");
		args.add(itemId);
		sql.append(" union all \n");
		sql.append(" select a.serialNum sourceName,'demand' type,a.id,a.creator userId,u.userName creatorName,a.recordCreateTime,b.id itemId \n");
		sql.append(" from demandProcess a \n");
		sql.append(" inner join item b on b.id = a.itemId  \n");
		sql.append(" left join userInfo u on u.id = a.creator \n");
		sql.append(" where b.id=? \n");
		args.add(itemId);
		sql.append(") where 1=1 ");
		return this.countQuery(sql.toString(), args.toArray());
	}
}