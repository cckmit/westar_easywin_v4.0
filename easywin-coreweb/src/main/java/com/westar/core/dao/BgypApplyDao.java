package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.westar.base.model.BgypApply;
import com.westar.base.model.BgypItem;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class BgypApplyDao extends BaseDao {

	/**
	 * 分页查询申领记录
	 * @param sessionUser 当前操作人员
	 * @param bgypApply 申领条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypApply> listPagedBgypApply(UserInfo sessionUser,
			BgypApply bgypApply) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from (");
		sql.append("\n select a.id,a.comId,a.remark,a.applyDate,a.applyCheckState,");
		sql.append("\n uinfo.userName,uinfo.gender userGender,ufile.uuid userUuid,ufile.fileName userFileName,");
		
		sql.append("\n spUser.gender spUserGender,spUser.username spUserName, spUfile.uuid spUserImgUuid,spUfile.filename spUserImgName,");
		sql.append("\n a.spUserId,a.spContent,");
		sql.append("\n case when a.applyCheckState=3 then 1 ");
		sql.append("\n  when a.applyCheckState=0 then 2 ");
		sql.append("\n  when a.applyCheckState=1 then 3 ");
		sql.append("\n  when a.applyCheckState=2 then 4 end newOrder ");
		
		sql.append("\n from bgypApply a ");
		sql.append("\n inner join userorganic uorg on a.comid=uorg.comid and a.applyUserId=uorg.userId");
		sql.append("\n inner join userInfo uinfo on uorg.userId=uInfo.id");
		sql.append("\n left join upfiles ufile on a.comId=ufile.comId and uorg.smallHeadPortrait=ufile.id");
		
		sql.append("\n left join userorganic spOrg on a.comid=spOrg.comId and a.spUserId=spOrg.userid");
		sql.append("\n left join userinfo spUser on spOrg.userid=spUser.id");
		sql.append("\n left join upfiles spUfile on spOrg.smallheadportrait=spUfile.id");
		
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		
		//采购单状态
		String applyCheckState = bgypApply.getApplyCheckState();
		if(!StringUtils.isEmpty(applyCheckState)){
			if(applyCheckState.equals(ConstantInterface.PURORDER_STATE_NORMAL)){//未送审的
				sql.append("\n and a.applyUserId=? ");
				args.add(sessionUser.getId());
				this.addSqlWhereIn(new Object[]{ ConstantInterface.PURORDER_STATE_NORMAL,ConstantInterface.PURORDER_STATE_BACK}, 
						sql, args, "  and a.applyCheckState in ?");
			}else if(applyCheckState.equals(ConstantInterface.PURORDER_STATE_CHECK)){//待送审的
				sql.append("\n and a.applyUserId=? and a.applyCheckState = ? ");
				args.add(sessionUser.getId());
				args.add(ConstantInterface.PURORDER_STATE_CHECK);
			}else if(applyCheckState.equals(ConstantInterface.PURORDER_STATE_PASS)){//审核通过的
				sql.append("\n and a.applyUserId=? and a.applyCheckState = ? ");
				args.add(sessionUser.getId());
				args.add(ConstantInterface.PURORDER_STATE_PASS);
			}
		}else{
			this.addSqlWhere(sessionUser.getId(), sql, args, "\n and a.applyUserId=?");
		}
				
		//查询创建时间段
		this.addSqlWhere(bgypApply.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(bgypApply.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		sql.append("\n)a where 1=1");
				
		return this.pagedQuery(sql.toString(), "a.newOrder,a.id desc", args.toArray(), BgypApply.class);
	}
	/**
	 * 分页查询申领记录
	 * @param sessionUser 当前操作人员
	 * @param bgypApply 申领条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypApply> listPagedSpBgypApply(UserInfo sessionUser,
			BgypApply bgypApply) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from (");
		sql.append("\n select a.id,a.comId,a.remark,a.applyDate,a.applyCheckState,");
		sql.append("\n uinfo.userName,uinfo.gender userGender,ufile.uuid userUuid,ufile.fileName userFileName,");
		
		sql.append("\n spUser.gender spUserGender,spUser.username spUserName, spUfile.uuid spUserImgUuid,spUfile.filename spUserImgName,");
		sql.append("\n a.spUserId,a.spContent,");
		sql.append("\n case when a.applyCheckState=3 then 1 ");
		sql.append("\n  when a.applyCheckState=0 then 2 ");
		sql.append("\n  when a.applyCheckState=1 then 3 ");
		sql.append("\n  when a.applyCheckState=2 then 4 end newOrder ");
		
		sql.append("\n from bgypApply a ");
		sql.append("\n inner join userorganic uorg on a.comid=uorg.comid and a.applyUserId=uorg.userId");
		sql.append("\n inner join userInfo uinfo on uorg.userId=uInfo.id");
		sql.append("\n left join upfiles ufile on a.comId=ufile.comId and uorg.smallHeadPortrait=ufile.id");
		
		sql.append("\n left join userorganic spOrg on a.comid=spOrg.comId and a.spUserId=spOrg.userid");
		sql.append("\n left join userinfo spUser on spOrg.userid=spUser.id");
		sql.append("\n left join upfiles spUfile on spOrg.smallheadportrait=spUfile.id");
		
		sql.append("\n where a.applyCheckState in ('"+ConstantInterface.PURORDER_STATE_CHECK+"','"+ConstantInterface.PURORDER_STATE_PASS+"')");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(bgypApply.getApplyUserId(), sql, args, "\n and a.applyUserId=?");
		
		//查询创建时间段
		this.addSqlWhere(bgypApply.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(bgypApply.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		sql.append("\n)a where 1=1");
		
		return this.pagedQuery(sql.toString(), "a.newOrder,a.id desc", args.toArray(), BgypApply.class);
	}

	/**
	 * 查询沈理工记录的基本信息
	 * @param comId 团队号
	 * @param applyId 申领记录主键
	 * @return
	 */
	public BgypApply queryBgypApplyById(Integer comId, Integer applyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.remark,a.applyDate,a.applyCheckState,a.applyUserId,");
		sql.append("\n uinfo.userName,uinfo.gender userGender,ufile.uuid userUuid,ufile.fileName userFileName,");
		
		sql.append("\n spUser.gender spUserGender,spUser.username spUserName, spUfile.uuid spUserImgUuid,spUfile.filename spUserImgName,");
		sql.append("\n a.spUserId,a.spContent");
		
		sql.append("\n from bgypApply a ");
		sql.append("\n inner join userorganic uorg on a.comid=uorg.comid and a.applyUserId=uorg.userId");
		sql.append("\n inner join userInfo uinfo on uorg.userId=uInfo.id");
		sql.append("\n left join upfiles ufile on a.comId=ufile.comId and uorg.smallHeadPortrait=ufile.id");
		
		sql.append("\n left join userorganic spOrg on a.comid=spOrg.comId and a.spUserId=spOrg.userid");
		sql.append("\n left join userinfo spUser on spOrg.userid=spUser.id");
		sql.append("\n left join upfiles spUfile on spOrg.smallheadportrait=spUfile.id");
		
		sql.append("\n where a.comId=? and a.id=?");
		args.add(comId);
		args.add(applyId);
		return (BgypApply) this.objectQuery(sql.toString(), args.toArray(), BgypApply.class);
	}

	/**
	 * 减少库存信息
	 * @param bgypItem 库存信息
	 */
	public void updateBgypItemStore(BgypItem bgypItem) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update bgypItem a set storeNum = storeNum-"+bgypItem.getStoreNum()+" where a.comid=? and a.id=?");
		args.add(bgypItem.getComId());
		args.add(bgypItem.getId());
		this.excuteSql(sql.toString(), args.toArray());
		
	}

	

}
