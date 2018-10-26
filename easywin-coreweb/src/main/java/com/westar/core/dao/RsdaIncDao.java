package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.RsdaIncFile;
import com.westar.base.model.RsdaIncentive;
import com.westar.base.model.UserInfo;

@Repository
public class RsdaIncDao extends BaseDao {

	/**
	 * 分页查询激励机制信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaInc 激励机制查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RsdaIncentive> listPagedRsdaInc(UserInfo sessionUser,
			RsdaIncentive rsdaInc) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select uInfo.gender userGender,uInfo.userName,imgFile.uuid userImgUuid,imgFile.filename userImgName,");
		sql.append("\n rsdaInc.id,rsdaInc.incentiveType,rsdaInc.incName,rsdaInc.incentiveDate,rsdaInc.remark");
		sql.append("\n from rsdaIncentive rsdaInc inner join userOrganic uOrg on rsdaInc.userId = uOrg.userId");
		sql.append("\n inner join userInfo uInfo on rsdaInc.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		//人员查询
		this.addSqlWhere(rsdaInc.getUserId(), sql, args, "\n and rsdaInc.userId=?");
		//奖惩类型
		this.addSqlWhere(rsdaInc.getIncentiveType(), sql, args, "\n and rsdaInc.incentiveType=?");
		
		//查询创建时间段
		this.addSqlWhere(rsdaInc.getStartDate(), sql, args, " and substr(rsdaInc.incentiveDate,0,10)>=?");
		this.addSqlWhere(rsdaInc.getEndDate(), sql, args, " and substr(rsdaInc.incentiveDate,0,10)<=?");
		//模糊查询
		this.addSqlWhereLike(rsdaInc.getRemark(), sql, args, "\n and  rsdaInc.incName like ? ");
				
		return this.pagedQuery(sql.toString(), "rsdaInc.id desc", args.toArray(), RsdaIncentive.class);
	}

	/**
	 * 查询激励机制详细信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaIncId 激励机制主键
	 * @return
	 */
	public RsdaIncentive queryRsdaIncById(UserInfo sessionUser, Integer rsdaIncId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select uInfo.gender userGender,uInfo.userName,imgFile.uuid userImgUuid,imgFile.filename userImgName,");
		sql.append("\n rsdaInc.id,rsdaInc.incentiveType,rsdaInc.incName,rsdaInc.incentiveDate,rsdaInc.remark,rsdaInc.userId");
		sql.append("\n from rsdaIncentive rsdaInc inner join userOrganic uOrg on rsdaInc.userId = uOrg.userId");
		sql.append("\n inner join userInfo uInfo on rsdaInc.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(rsdaIncId, sql, args, "\n and rsdaInc.id=?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		return (RsdaIncentive) this.objectQuery(sql.toString(), args.toArray(), RsdaIncentive.class);
	}

	/**
	 * 查询激励机制的附件信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaIncId 激励机制的主键信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RsdaIncFile> listRsdaIncFiles(UserInfo sessionUser,
			Integer rsdaIncId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comid,a.rsdaIncId,a.upfileId,b.fileName,b.fileExt,b.uuid fileUuid, ");
		sql.append("\n  case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic"); 
		sql.append("\n  from rsdaIncFile a inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(rsdaIncId, sql, args, " and a.rsdaIncId=?");
		return this.listQuery(sql.toString(), args.toArray(), RsdaIncFile.class);
	}

}
