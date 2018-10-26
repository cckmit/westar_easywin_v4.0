package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.westar.base.model.BgypItem;
import com.westar.base.model.BgypPurFile;
import com.westar.base.model.BgypPurOrder;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class BgypPurOrderDao extends BaseDao {

	/**
	 * 分页查询采购单汇总信息
	 * @param sessionUser 当前操作员
	 * @param bgypPurOrder 采购单查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypPurOrder> listPagedBgypPurOrder(UserInfo sessionUser,
			BgypPurOrder bgypPurOrder) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.bgypPurDate,a.purOrderNum,purUserId,bgypTotalPrice,a.purOrderState,");
		sql.append("\n c.gender purUserGender,c.username purUserName, d.uuid purUserImgUuid,d.filename purUserImgName,");
		sql.append("\n spUser.gender spUserGender,spUser.username spUserName, spUfile.uuid spUserImgUuid,spUfile.filename spUserImgName,");
		sql.append("\n a.spUserId,a.spContent");
		sql.append("\n from bgypPurOrder a inner join userorganic b on a.comid=b.comId and a.purUserId=b.userid");
		sql.append("\n inner join userinfo c on b.userid=c.id");
		sql.append("\n left join upfiles d on b.smallheadportrait=d.id");
		
		sql.append("\n left join userorganic spOrg on a.comid=spOrg.comId and a.spUserId=spOrg.userid");
		sql.append("\n left join userinfo spUser on spOrg.userid=spUser.id");
		sql.append("\n left join upfiles spUfile on spOrg.smallheadportrait=spUfile.id");
		
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(sessionUser.getId(), sql, args, "\n and a.purUserId=?");
		
		//采购单状态
		String purOrderState = bgypPurOrder.getPurOrderState();
		if(!StringUtils.isEmpty(purOrderState)){
			if(purOrderState.equals(ConstantInterface.PURORDER_STATE_NORMAL)){//未送审的
				sql.append("\n and a.purRecorder=? ");
				args.add(sessionUser.getId());
				this.addSqlWhereIn(new Object[]{ ConstantInterface.PURORDER_STATE_NORMAL,ConstantInterface.PURORDER_STATE_BACK}, 
						sql, args, "  and a.purOrderState in ?");
			}else if(purOrderState.equals(ConstantInterface.PURORDER_STATE_CHECK)){//待送审的
				sql.append("\n and a.purUserId=? and a.purOrderState = ? ");
				args.add(sessionUser.getId());
				args.add(ConstantInterface.PURORDER_STATE_CHECK);
			}else if(purOrderState.equals(ConstantInterface.PURORDER_STATE_PASS)){//审核通过的
				sql.append("\n and a.purUserId=? and a.purOrderState = ? ");
				args.add(sessionUser.getId());
				args.add(ConstantInterface.PURORDER_STATE_PASS);
			}
		}
		return this.pagedQuery(sql.toString(), "a.id desc", args.toArray(), BgypPurOrder.class);
	}
	/**
	 * 分页查询采购单汇总信息
	 * @param sessionUser 当前操作员
	 * @param bgypPurOrder 采购单查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypPurOrder> listPagedSpBgypPurOrder(UserInfo sessionUser,
			BgypPurOrder bgypPurOrder) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.bgypPurDate,a.purOrderNum,purUserId,bgypTotalPrice,a.purOrderState,");
		sql.append("\n c.gender purUserGender,c.username purUserName, d.uuid purUserImgUuid,d.filename purUserImgName,");
		
		sql.append("\n spUser.gender spUserGender,spUser.username spUserName, spUfile.uuid spUserImgUuid,spUfile.filename spUserImgName");
		
		sql.append("\n from bgypPurOrder a inner join userorganic b on a.comid=b.comId and a.purUserId=b.userid");
		sql.append("\n inner join userinfo c on b.userid=c.id");
		sql.append("\n left join upfiles d on b.smallheadportrait=d.id");
		
		sql.append("\n left join userorganic spOrg on a.comid=spOrg.comId and a.spUserId=spOrg.userid");
		sql.append("\n left join userinfo spUser on spOrg.userid=spUser.id");
		sql.append("\n left join upfiles spUfile on spOrg.smallheadportrait=spUfile.id");
		
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		
		sql.append("\n and (");
		sql.append("\n 	a.purUserId=? or a.purRecorder=? ");
		args.add(sessionUser.getId());
		args.add(sessionUser.getId());
		sql.append("\n 	or exists(");
		sql.append("\n 		select id from modAdmin where modAdmin.comid=? and modAdmin.busType=? and modAdmin.userId=?");
		args.add(sessionUser.getComId());
		args.add(ConstantInterface.TYPE_BGYP);
		args.add(sessionUser.getId());
		sql.append("\n 	)");
		sql.append("\n )");
		
		this.addSqlWhere(bgypPurOrder.getPurUserId(), sql, args, "\n and a.purUserId=?");
		//采购单审核状态
		String purOrderState = bgypPurOrder.getPurOrderState();
		if(null!=purOrderState && (purOrderState.equals(ConstantInterface.PURORDER_STATE_CHECK)
				|| purOrderState.equals(ConstantInterface.PURORDER_STATE_PASS))){//没有查询条件，则查询所有
			sql.append("\n and a.purOrderState = ? ");
			args.add(purOrderState);
		}else{
			sql.append("\n and (a.purOrderState = ? or  a.purOrderState = ?)");
			args.add(ConstantInterface.PURORDER_STATE_CHECK);
			args.add(ConstantInterface.PURORDER_STATE_PASS);
		}
		
		//查询创建时间段
		this.addSqlWhere(bgypPurOrder.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(bgypPurOrder.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		return this.pagedQuery(sql.toString(), "a.purOrderState,a.id desc", args.toArray(), BgypPurOrder.class);
	}

	/**
	 * 查询采购单信息
	 * @param comId
	 * @param purOrderId
	 * @return
	 */
	public BgypPurOrder queryBgypPurOrderById(Integer comId, Integer purOrderId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.bgypPurDate,a.purOrderNum,purUserId,bgypTotalPrice,a.content,a.purOrderState,");
		sql.append("\n c.gender purUserGender,c.username purUserName, d.uuid purUserImgUuid,d.filename purUserImgName,");
		
		sql.append("\n spUser.gender spUserGender,spUser.username spUserName, spUfile.uuid spUserImgUuid,spUfile.filename spUserImgName,");
		sql.append("\n a.spContent");
		
		sql.append("\n from bgypPurOrder a inner join userorganic b on a.comid=b.comId and a.purUserId=b.userid");
		sql.append("\n inner join userinfo c on b.userid=c.id");
		sql.append("\n left join upfiles d on b.smallheadportrait=d.id");
		
		sql.append("\n left join userorganic spOrg on a.comid=spOrg.comId and a.spUserId=spOrg.userid");
		sql.append("\n left join userinfo spUser on spOrg.userid=spUser.id");
		sql.append("\n left join upfiles spUfile on spOrg.smallheadportrait=spUfile.id");
		
		sql.append("\n where a.id=? and a.comId=?");
		args.add(purOrderId);
		args.add(comId);
		return (BgypPurOrder) this.objectQuery(sql.toString(), args.toArray(), BgypPurOrder.class);
	}

	/**
	 * 查询采购单附件
	 * @param comId 团队主键
	 * @param purOrderId 采购单主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypPurFile> listBgypPurFiles(Integer comId, Integer purOrderId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comid,a.purOrderId,a.upfileId,b.fileName,b.fileext,b.uuid fileUuid, ");
		sql.append("\n  case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic"); 
		sql.append("\n  from bgypPurFile a inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(purOrderId, sql, args, " and a.purOrderId=?");
		return this.listQuery(sql.toString(), args.toArray(), BgypPurFile.class);
	}

	/**
	 * 通过条目修改库存信息
	 * @param bgypItem
	 */
	public void updateBgypItemStore(BgypItem bgypItem) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update bgypItem a set storeNum = storeNum+"+bgypItem.getStoreNum()+" where a.comid=? and a.id=?");
		args.add(bgypItem.getComId());
		args.add(bgypItem.getId());
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 删除自己录入的未审核的数据
	 * @param sessionUser 当前操作员
	 * @param ids 采购单主键信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypPurOrder> listBgypPurOrder4Del(UserInfo sessionUser,
			Integer[] ids) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.purRecorder,a.purOrderState");
		sql.append("\n from bgypPurOrder a ");
		sql.append("\n where a.comId=? and a.purRecorder=?");
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		this.addSqlWhereIn(ids, sql, args, "\n and a.id in ?");
		return this.listQuery(sql.toString(), args.toArray(), BgypPurOrder.class);
	}

	

}
