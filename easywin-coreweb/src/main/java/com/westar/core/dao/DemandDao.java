package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.westar.base.enums.DemandStageEnum;
import com.westar.base.enums.DemandStateEnum;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.DemandHandleHis;
import com.westar.base.model.DemandProcess;
import com.westar.base.model.Item;
import com.westar.base.model.Product;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.BaseUpfile;
import com.westar.base.pojo.CommonLog;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.ConstantInterface;

/**
 * 
 * 描述:需求管理数据层
 * @author zzq
 * @date 2018年10月9日 下午5:59:54
 */
@Repository
public class DemandDao extends BaseDao {
	
	/**
	 * 验证是否有重复的序列号
	 * @param serialNum 当前序列号
	 * @return
	 */
	public Integer checkSerialNumber(String serialNum) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		sql.append("\n select count(id)");
		sql.append("\n from demandProcess a");
		sql.append("\n where a.serialNum=?");
		args.add(serialNum);
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 分页查询发布的需求
	 * @param sessionUser 当前操作人员
	 * @param demand 需求的查询条件
	 * @return
	 */
	public PageBean<DemandProcess> listPagedMineDemand(UserInfo sessionUser,
			DemandProcess demandProcess) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.serialNum,a.startLevel,a.type,dic.zvalue typename ");
		sql.append("\n ,a.creator,demandU.userName creatorName,a.state,stateTable.stateName ");
		//关联项目
		sql.append("\n ,a.itemId,item.itemName,item.owner as itemOwnerId,itemU.userName as itemOwnerName ");
		//关联产品
		sql.append("\n ,a.productId,p.name productName ");
		
		sql.append("\n from demandProcess a ");
		sql.append("\n inner join userInfo demandU on a.creator=demandU.id ");
		//关联项目
		sql.append("\n inner join item on a.itemId=item.id and a.comId=item.comId ");
		sql.append("\n inner join userInfo itemU on item.owner=itemU.id ");
		//关联产品
		sql.append("\n inner join product p on a.productId=p.id and a.comId=p.comId ");
		//产品类型
		sql.append("\n left join datadic dic on a.type=dic.code and dic.type='demandType' ");
		//阶段类型
		sql.append("\n left join ( ");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = DemandStateEnum.toList();
		for(int i=0,len = list.size();i < len;i++){
			Map<String, Object> map = list.get(i);
			String value =  map.get("value").toString();
			String desc =  map.get("desc").toString();
			sql.append("\n select "+value+" state,'"+desc+"' stateName from dual" );
			if(len-1 > i){
				sql.append("\n union all" );
			}
		}
		sql.append("\n )stateTable on a.state=stateTable.state");
		
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(sessionUser.getId(), sql, args, "\n and a.creator=?");
		
		//查询创建时间段
		this.addSqlWhere(demandProcess.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(demandProcess.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//序列编号查询
		this.addSqlWhere(demandProcess.getSerialNum(), sql, args, "\n and a.serialNum=?");
		//项目查询
		List<Item> listItem = demandProcess.getListItem();
		if(null != listItem && !listItem.isEmpty()){
			List<Integer> items = new ArrayList<Integer>(listItem.size());
			for (Item item : listItem) {
				items.add(item.getId());
			}
			this.addSqlWhereIn(items.toArray(new Integer[items.size()]), sql, args, "\n and a.itemId in ?");
		}
		
		//产品查询
		List<Product> listProduct = demandProcess.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and p.id in ?");
		}
		//类型
		this.addSqlWhere(demandProcess.getType(), sql, args, "\n and a.type=?");
		//进度
		this.addSqlWhere(demandProcess.getState(), sql, args, "\n and a.state=?");
		
 		return this.pagedBeanQuery(sql.toString(), " a.id desc", args.toArray(), DemandProcess.class);
	}
	/**
	 * 分页查询需要签收的需求
	 * @param sessionUser 当前操作人员
	 * @param demand 需求的查询条件
	 * @return
	 */
	public PageBean<DemandProcess> listPagedDemandForAccept(UserInfo sessionUser,
			DemandProcess demandProcess) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.serialNum,a.startLevel,a.type,dic.zvalue typename ");
		sql.append("\n ,a.creator,demandU.userName creatorName,a.state,stateTable.stateName ");
		//关联项目
		sql.append("\n ,a.itemId,item.itemName,item.owner as itemOwnerId,itemU.userName as itemOwnerName ");
		//关联产品
		sql.append("\n ,a.productId,p.name productName ");
		
		sql.append("\n from demandProcess a ");
		sql.append("\n inner join userInfo demandU on a.creator=demandU.id ");
		//关联项目
		sql.append("\n inner join item on a.itemId=item.id and a.comId=item.comId ");
		sql.append("\n inner join userInfo itemU on item.owner=itemU.id ");
		//关联产品
		sql.append("\n inner join product p on a.productId=p.id and a.comId=p.comId ");
		//产品类型
		sql.append("\n left join datadic dic on a.type=dic.code and dic.type='demandType' ");
		//阶段类型
		sql.append("\n left join ( ");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = DemandStateEnum.toList();
		for(int i=0,len = list.size();i < len;i++){
			Map<String, Object> map = list.get(i);
			String value =  map.get("value").toString();
			String desc =  map.get("desc").toString();
			sql.append("\n select "+value+" state,'"+desc+"' stateName from dual" );
			if(len-1 > i){
				sql.append("\n union all" );
			}
		}
		sql.append("\n )stateTable on a.state=stateTable.state");
		
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(sessionUser.getId(), sql, args, "\n and item.owner=?");
		
		//序列编号查询
		this.addSqlWhere(demandProcess.getSerialNum(), sql, args, "\n and a.serialNum=?");
		//查询创建时间段
		this.addSqlWhere(demandProcess.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(demandProcess.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		this.addSqlWhereIn(new Object[]{DemandStateEnum.DEFAULT.getValue(),DemandStateEnum.ANALYSIS.getValue()}, sql, args, "\n and a.state in ?");
		//发布人查询
		List<UserInfo> listCreator = demandProcess.getListCreator();
		if(null != listCreator && !listCreator.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>(listCreator.size());
			for (UserInfo userInfo : listCreator) {
				userIds.add(userInfo.getId());
			}
			this.addSqlWhereIn(userIds.toArray(new Integer[userIds.size()]), sql, args, "\n and a.creator in ?");
			
		}
		//项目查询
		List<Item> listItem = demandProcess.getListItem();
		if(null != listItem && !listItem.isEmpty()){
			List<Integer> items = new ArrayList<Integer>(listItem.size());
			for (Item item : listItem) {
				items.add(item.getId());
			}
			this.addSqlWhereIn(items.toArray(new Integer[items.size()]), sql, args, "\n and a.itemId in ?");
		}
		
		//产品查询
		List<Product> listProduct = demandProcess.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and p.id in ?");
		}
		//类型
		this.addSqlWhere(demandProcess.getType(), sql, args, "\n and a.type=?");
		
		return this.pagedBeanQuery(sql.toString(), " a.id desc", args.toArray(), DemandProcess.class);
	}
	/**
	 * 分页查询需要签收的需求
	 * @param sessionUser 当前操作人员
	 * @param demand 需求的查询条件
	 * @return
	 */
	public PageBean<DemandProcess> listPagedDemandForHandle(UserInfo sessionUser,
			DemandProcess demandProcess) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.serialNum,a.startLevel,a.type,dic.zvalue typename ");
		sql.append("\n ,a.creator,demandU.userName creatorName,a.state,stateTable.stateName ");
		//关联项目
		sql.append("\n ,a.itemId,item.itemName,item.owner as itemOwnerId,itemU.userName as itemOwnerName ");
		//关联产品
		sql.append("\n ,a.productId,p.name productName ");
		
		sql.append("\n from demandProcess a ");
		sql.append("\n inner join userInfo demandU on a.creator=demandU.id ");
		//关联项目
		sql.append("\n inner join item on a.itemId=item.id and a.comId=item.comId ");
		sql.append("\n inner join userInfo itemU on item.owner=itemU.id ");
		//关联产品
		sql.append("\n inner join product p on a.productId=p.id and a.comId=p.comId ");
		//产品类型
		sql.append("\n left join datadic dic on a.type=dic.code and dic.type='demandType' ");
		//阶段类型
		sql.append("\n left join ( ");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = DemandStateEnum.toList();
		for(int i=0,len = list.size();i < len;i++){
			Map<String, Object> map = list.get(i);
			String value =  map.get("value").toString();
			String desc =  map.get("desc").toString();
			sql.append("\n select "+value+" state,'"+desc+"' stateName from dual" );
			if(len-1 > i){
				sql.append("\n union all" );
			}
		}
		sql.append("\n )stateTable on a.state=stateTable.state");
		
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(sessionUser.getId(), sql, args, "\n and item.owner=?");
		this.addSqlWhere(DemandStateEnum.HANDLING.getValue(), sql, args, "\n and a.state=?");
		//序列编号查询
		this.addSqlWhere(demandProcess.getSerialNum(), sql, args, "\n and a.serialNum=?");
		//查询创建时间段
		this.addSqlWhere(demandProcess.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(demandProcess.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//发布人查询
		List<UserInfo> listCreator = demandProcess.getListCreator();
		if(null != listCreator && !listCreator.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>(listCreator.size());
			for (UserInfo userInfo : listCreator) {
				userIds.add(userInfo.getId());
			}
			this.addSqlWhereIn(userIds.toArray(new Integer[userIds.size()]), sql, args, "\n and a.creator in ?");
			
		}
		//项目查询
		List<Item> listItem = demandProcess.getListItem();
		if(null != listItem && !listItem.isEmpty()){
			List<Integer> items = new ArrayList<Integer>(listItem.size());
			for (Item item : listItem) {
				items.add(item.getId());
			}
			this.addSqlWhereIn(items.toArray(new Integer[items.size()]), sql, args, "\n and a.itemId in ?");
		}
		
		//产品查询
		List<Product> listProduct = demandProcess.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and p.id in ?");
		}
		//类型
		this.addSqlWhere(demandProcess.getType(), sql, args, "\n and a.type=?");
		
		return this.pagedBeanQuery(sql.toString(), " a.id desc", args.toArray(), DemandProcess.class);
	}
	/**
	 * 分页查询需要签收的需求
	 * @param sessionUser 当前操作人员
	 * @param demand 需求的查询条件
	 * @return
	 */
	public PageBean<DemandProcess> listPagedDemandForConfirm(UserInfo sessionUser,
			DemandProcess demandProcess) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.serialNum,a.startLevel,a.type,dic.zvalue typename ");
		sql.append("\n ,a.creator,demandU.userName creatorName,a.state,stateTable.stateName ");
		//关联项目
		sql.append("\n ,a.itemId,item.itemName,item.owner as itemOwnerId,itemU.userName as itemOwnerName ");
		//关联产品
		sql.append("\n ,a.productId,p.name productName ");
		
		sql.append("\n from demandProcess a ");
		sql.append("\n inner join userInfo demandU on a.creator=demandU.id ");
		//关联项目
		sql.append("\n inner join item on a.itemId=item.id and a.comId=item.comId ");
		sql.append("\n inner join userInfo itemU on item.owner=itemU.id ");
		//关联产品
		sql.append("\n inner join product p on a.productId=p.id and a.comId=p.comId ");
		//产品类型
		sql.append("\n left join datadic dic on a.type=dic.code and dic.type='demandType' ");
		//阶段类型
		sql.append("\n left join ( ");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = DemandStateEnum.toList();
		for(int i=0,len = list.size();i < len;i++){
			Map<String, Object> map = list.get(i);
			String value =  map.get("value").toString();
			String desc =  map.get("desc").toString();
			sql.append("\n select "+value+" state,'"+desc+"' stateName from dual" );
			if(len-1 > i){
				sql.append("\n union all" );
			}
		}
		sql.append("\n )stateTable on a.state=stateTable.state");
		
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(sessionUser.getId(), sql, args, "\n and a.creator=?");
		this.addSqlWhere(DemandStateEnum.CONFIRM.getValue(), sql, args, "\n and a.state=?");
		
		//序列编号查询
		this.addSqlWhere(demandProcess.getSerialNum(), sql, args, "\n and a.serialNum=?");
		//查询创建时间段
		this.addSqlWhere(demandProcess.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(demandProcess.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
				
		//发布人查询
		List<UserInfo> listCreator = demandProcess.getListCreator();
		if(null != listCreator && !listCreator.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>(listCreator.size());
			for (UserInfo userInfo : listCreator) {
				userIds.add(userInfo.getId());
			}
			this.addSqlWhereIn(userIds.toArray(new Integer[userIds.size()]), sql, args, "\n and a.creator in ?");
			
		}
		//项目查询
		List<Item> listItem = demandProcess.getListItem();
		if(null != listItem && !listItem.isEmpty()){
			List<Integer> items = new ArrayList<Integer>(listItem.size());
			for (Item item : listItem) {
				items.add(item.getId());
			}
			this.addSqlWhereIn(items.toArray(new Integer[items.size()]), sql, args, "\n and a.itemId in ?");
		}
		
		//产品查询
		List<Product> listProduct = demandProcess.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and p.id in ?");
		}
		//类型
		this.addSqlWhere(demandProcess.getType(), sql, args, "\n and a.type=?");
		return this.pagedBeanQuery(sql.toString(), " a.id desc", args.toArray(), DemandProcess.class);
	}
	/**
	 * 分页查询需要签收的需求
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求的查询条件
	 * @param isForceInPersion 
	 * @return
	 */
	public PageBean<DemandProcess> listPagedDemandForAll(UserInfo sessionUser,
			DemandProcess demandProcess, boolean isForceInPersion) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.serialNum,a.startLevel,a.type,dic.zvalue typename ");
		sql.append("\n ,a.creator,demandU.userName creatorName,a.state,stateTable.stateName ");
		//关联项目
		sql.append("\n ,a.itemId,item.itemName,item.owner as itemOwnerId,itemU.userName as itemOwnerName ");
		//关联产品
		sql.append("\n ,a.productId,p.name productName ");
		
		sql.append("\n from demandProcess a ");
		sql.append("\n inner join userInfo demandU on a.creator=demandU.id ");
		//关联项目
		sql.append("\n inner join item on a.itemId=item.id and a.comId=item.comId ");
		sql.append("\n inner join userInfo itemU on item.owner=itemU.id ");
		//关联产品
		sql.append("\n inner join product p on a.productId=p.id and a.comId=p.comId ");
		//产品类型
		sql.append("\n left join datadic dic on a.type=dic.code and dic.type='demandType' ");
		//阶段类型
		sql.append("\n left join ( ");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = DemandStateEnum.toList();
		for(int i=0,len = list.size();i < len;i++){
			Map<String, Object> map = list.get(i);
			String value =  map.get("value").toString();
			String desc =  map.get("desc").toString();
			sql.append("\n select "+value+" state,'"+desc+"' stateName from dual" );
			if(len-1 > i){
				sql.append("\n union all" );
			}
		}
		sql.append("\n )stateTable on a.state=stateTable.state");
		
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		
		//序列编号查询
		this.addSqlWhere(demandProcess.getSerialNum(), sql, args, "\n and a.serialNum=?");
		//查询创建时间段
		this.addSqlWhere(demandProcess.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(demandProcess.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
				
		if(!isForceInPersion){
			this.addSqlWhere(sessionUser.getId(), sql, args, "\n and (a.creator=? or item.owner=? or p.principal=? or p.manager=?)",4);
		}
		//发布人查询
		List<UserInfo> listCreator = demandProcess.getListCreator();
		if(null != listCreator && !listCreator.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>(listCreator.size());
			for (UserInfo userInfo : listCreator) {
				userIds.add(userInfo.getId());
			}
			this.addSqlWhereIn(userIds.toArray(new Integer[userIds.size()]), sql, args, "\n and a.creator in ?");
			
		}
		//项目查询
		List<Item> listItem = demandProcess.getListItem();
		if(null != listItem && !listItem.isEmpty()){
			List<Integer> items = new ArrayList<Integer>(listItem.size());
			for (Item item : listItem) {
				items.add(item.getId());
			}
			this.addSqlWhereIn(items.toArray(new Integer[items.size()]), sql, args, "\n and a.itemId in ?");
		}
		
		//产品查询
		List<Product> listProduct = demandProcess.getListProduct();
		if(null != listProduct && !listProduct.isEmpty()){
			List<Integer> products = new ArrayList<Integer>(listProduct.size());
			for (Product product : listProduct) {
				products.add(product.getId());
			}
			this.addSqlWhereIn(products.toArray(new Integer[products.size()]), sql, args, "\n and p.id in ?");
		}
		//类型
		this.addSqlWhere(demandProcess.getType(), sql, args, "\n and a.type=?");
		//进度
		this.addSqlWhere(demandProcess.getState(), sql, args, "\n and a.state=?");
		
		return this.pagedBeanQuery(sql.toString(), " a.id desc", args.toArray(), DemandProcess.class);
	}

	/**
	 * 查询需求信息
	 * @param sessionUser
	 * @param demandId
	 * @return
	 */
	public DemandProcess queryDemandForCreator(UserInfo sessionUser,
			Integer demandId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.serialNum,a.startLevel,a.type,dic.zvalue typeName ");
		sql.append("\n ,a.expectFinishDate,a.describe,a.standard,a.state,a.handleUser ");
		sql.append("\n ,a.creator,demandU.userName creatorName ");
		//关联项目
		sql.append("\n ,a.itemId,item.itemName,item.owner as itemOwnerId,itemU.userName as itemOwnerName ");
		//关联产品
		sql.append("\n ,a.productId,p.name productName,case when a.itemModId=-1 then item.itemName else fun.functionName end itemModName ");
		
		sql.append("\n from demandProcess a ");
		sql.append("\n inner join userInfo demandU on a.creator=demandU.id ");
		//关联项目
		sql.append("\n inner join item on a.itemId=item.id and a.comId=item.comId ");
		sql.append("\n inner join userInfo itemU on item.owner=itemU.id ");
		//关联产品
		sql.append("\n inner join product p on a.productId=p.id and a.comId=p.comId ");
		//产品类型
		sql.append("\n left join datadic dic on a.type=dic.code and dic.type='demandType' ");
		//修改模块
		sql.append("\n left join functionList fun on fun.id=a.itemModId and fun.busType=? and fun.busId=item.id ");
		args.add(ConstantInterface.TYPE_ITEM);
		sql.append("\n where 1=1");
		this.addSqlWhere(demandId, sql, args, "\n and a.id=?");
		return (DemandProcess) this.objectQuery(sql.toString(), args.toArray(), DemandProcess.class);
	}

	/**
	 * 分页查询需求附件信息
	 * @param userInfo 当前操作人员
	 * @param demandId 需求主键
	 * @return
	 */
	public PageBean<BaseUpfile> listPagedDemandUpfile(UserInfo userInfo,
			Integer demandId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.comId,a.recordcreatetime,a.demandid busId");
		sql.append("\n ,a.upfileid, b.filename,b.fileext,b.uuid");
		sql.append("\n ,a.creator as userId,u.username");
		sql.append("\n from demandfile a");
		sql.append("\n inner join upfiles b on a.comId=b.comid and a.upfileid=b.id");
		sql.append("\n inner join userinfo u on a.creator=u.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comid=?");
		this.addSqlWhere(demandId, sql, args, "\n and a.demandId=?");
		return this.pagedBeanQuery(sql.toString(), " a.id desc", args.toArray(), BaseUpfile.class);
	}

	/**
	 * 分页查询需求的日志信息
	 * @param userInfo当前操作人员
	 * @param demandId 需求主键
	 * @return
	 */
	public PageBean<CommonLog> listPagedDemandLog(UserInfo userInfo,
			Integer demandId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//需求
		sql.append("select a.id,a.comId,a.recordCreateTime,a.content,a.userId,b.username as username\n");
		sql.append("from demandLog a \n");
		sql.append("inner join userInfo b on a.userId = b.id \n");
		sql.append("where a.comId=? and a.demandId = ?  \n");
		args.add(userInfo.getComId());
		args.add(demandId);
		return this.pagedBeanQuery(sql.toString(), " a.id desc", args.toArray(), CommonLog.class);
	}
	/**
	 * 分页查询用于选择的需求
	 * @param sessionUser 当前操作人员
	 * @param demandProcess 需求查询条件
	 * @return
	 */
	public PageBean<DemandProcess> listPagedDemandForSelect(UserInfo sessionUser,
			DemandProcess demandProcess) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.serialNum,a.startLevel,a.type,dic.zvalue typename ");
		sql.append("\n ,a.creator,demandU.userName creatorName ");
		//关联项目
		sql.append("\n ,a.itemId,item.itemName,item.owner as itemOwnerId,itemU.userName as itemOwnerName ");
		//关联产品
		sql.append("\n ,a.productId,p.name productName ");
		sql.append("\n from demandProcess a ");
		sql.append("\n inner join userInfo demandU on a.creator=demandU.id ");
		//关联项目
		sql.append("\n inner join item on a.itemId=item.id and a.comId=item.comId ");
		sql.append("\n inner join userInfo itemU on item.owner=itemU.id ");
		//关联产品
		sql.append("\n inner join product p on a.productId=p.id and a.comId=p.comId ");
		//产品类型
		sql.append("\n left join datadic dic on a.type=dic.code and dic.type='demandType' ");
		
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		
		this.addSqlWhere(demandProcess.getType(), sql, args, "\n and a.type=?");
		
		//查询创建时间段
		this.addSqlWhere(demandProcess.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(demandProcess.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
				
		//发布人查询
		List<UserInfo> listCreator = demandProcess.getListCreator();
		if(null != listCreator && !listCreator.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>(listCreator.size());
			for (UserInfo userInfo : listCreator) {
				userIds.add(userInfo.getId());
			}
			this.addSqlWhereIn(userIds.toArray(new Integer[userIds.size()]), sql, args, "\n and a.creator in ?");
		}
		//项目查询
		List<Item> listItem = demandProcess.getListItem();
		if(null != listItem && !listItem.isEmpty()){
			List<Integer> items = new ArrayList<Integer>(listItem.size());
			for (Item item : listItem) {
				items.add(item.getId());
			}
			this.addSqlWhereIn(items.toArray(new Integer[items.size()]), sql, args, "\n and a.itemId in ?");
		}
		
		return this.pagedBeanQuery(sql.toString(), " a.id desc", args.toArray(), DemandProcess.class);
	}

	/**
	 * 查询
	 * @param comId 团队号
	 * @param demandId 需求主键
	 * @return
	 */
	public DemandHandleHis queryCruDemandHandleHis(Integer comId, Integer demandId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* ");
		sql.append("\n from demandHandleHis a");
		sql.append("\n where curStep=1");
		this.addSqlWhere(comId, sql, args, "\n and a.comId=?");
		this.addSqlWhere(demandId, sql, args, "\n and a.demandId=?");
		return (DemandHandleHis) this.objectQuery(sql.toString(), args.toArray(), DemandHandleHis.class);
	}

	/**
	 * 查询用于催办的人员
	 * @param userInfo
	 * @param demandId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusRemindUser> listDemandRemindExecutor(UserInfo userInfo,
			Integer demandId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.handleUser userId,b.username,a.comId");
		sql.append("\n from demandProcess a ");
		sql.append("\n inner join userinfo b on a.handleUser = b.id ");
		sql.append("\n where a.id = ? and a.comId=? ");// 执行中和待认领的
		args.add(demandId);
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(), BusRemindUser.class);
	}
	
	/**
	 * 通过id查询关联需求的数量
	 * @author hcj 
	 * @date: 2018年10月16日 下午1:11:52
	 * @param ids
	 * @return
	 */
	public Integer queryDemandCountByItemIds(Integer[] ids) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(1) from demandProcess a where 1=1");
		this.addSqlWhereIn(ids,sql,args," and nvl(a.ITEMID,0) in ? ");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 查询需求处理过程信息
	 * @param sessionUser
	 * @param demandId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DemandHandleHis> listDemandHandleHis(UserInfo sessionUser,
			Integer demandId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,demandStage.stageName,u.userName ");
		sql.append("\n from demandHandleHis a");
		//阶段类型
		sql.append("\n left join ( ");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = DemandStageEnum.toList();
		for(int i=0,len = list.size();i < len;i++){
			Map<String, Object> map = list.get(i);
			String value =  map.get("value").toString();
			String desc =  map.get("desc").toString();
			sql.append("\n select "+value+" step,'"+desc+"' stageName from dual" );
			if(len-1 > i){
				sql.append("\n union all" );
			}
		}
		sql.append("\n )demandStage on a.step=demandStage.step");
		sql.append("\n left join userinfo u on a.userId=u.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(demandId, sql, args, "\n and a.demandId=?");
		sql.append("\n order by a.id ");
		return this.listQuery(sql.toString(), args.toArray(), DemandHandleHis.class);
	}

}
