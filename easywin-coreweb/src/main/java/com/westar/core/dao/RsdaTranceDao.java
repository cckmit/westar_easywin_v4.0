package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.RsdaTrance;
import com.westar.base.model.RsdaTranceFile;
import com.westar.base.model.UserInfo;

@Repository
public class RsdaTranceDao extends BaseDao {

	/**
	 * 分页查询人事调动信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaTrance 人事调动的查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RsdaTrance> listPagedRsdaTrance(UserInfo sessionUser,
			RsdaTrance rsdaTrance) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select uInfo.gender userGender,uInfo.userName,imgFile.uuid userImgUuid,imgFile.filename userImgName,");
		sql.append("\n a.id,a.tranceDate,a.tranceType,a.remark");
		sql.append("\n from rsdaTrance a inner join userOrganic uOrg on a.userId = uOrg.userId");
		sql.append("\n inner join userInfo uInfo on a.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		this.addSqlWhere(rsdaTrance.getUserId(), sql, args, "\n and a.userId=?");
		
		this.addSqlWhere(rsdaTrance.getTranceType(), sql, args, "\n and a.tranceType=?");
		//查询创建时间段
		this.addSqlWhere(rsdaTrance.getStartDate(), sql, args, " and substr(a.tranceDate,0,10)>=?");
		this.addSqlWhere(rsdaTrance.getEndDate(), sql, args, " and substr(a.tranceDate,0,10)<=?");
		//模糊查询
		this.addSqlWhereLike(rsdaTrance.getRemark(), sql, args, "\n and  a.remark like ? ");
		return this.pagedQuery(sql.toString(), "a.id desc", args.toArray(), RsdaTrance.class);
	}

	/**
	 * 根据主键查询人事调动
	 * @param sessionUser 当前操作人员
	 * @param radaTranceId 人事调动主键信息
	 * @return
	 */
	public RsdaTrance queryRsdaTranceById(UserInfo sessionUser,
			Integer radaTranceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comId,a.userId,uInfo.gender userGender,uInfo.userName,imgFile.uuid userImgUuid,imgFile.filename userImgName,");
		sql.append("\n a.id,a.tranceDate,a.tranceType,a.remark,a.trancePreOrg,a.tranceAftOrg,a.trancePreDepId,a.tranceAftDepId,");
		sql.append("\n preDep.depname trancePreDepName,aftDep.depName tranceAftDepName");
		sql.append("\n from rsdaTrance a inner join userOrganic uOrg on a.userId = uOrg.userId");
		sql.append("\n inner join userInfo uInfo on a.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n left join department preDep on preDep.id=a.trancePreDepId");
		sql.append("\n left join department aftDep on aftDep.id=a.tranceAftDepId");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		this.addSqlWhere(radaTranceId, sql, args, "\n and a.id=?");
		return (RsdaTrance) this.objectQuery(sql.toString(), args.toArray(), RsdaTrance.class);
	}

	/**
	 * 查询人事调动的附件信息
	 * @param sessionUser 当前操作人员
	 * @param radaTranceId 人事调动主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RsdaTranceFile> listTranceFiles(UserInfo sessionUser,
			Integer radaTranceId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comid,a.tranceId,a.upfileId,b.fileName,b.fileExt,b.uuid fileUuid, ");
		sql.append("\n  case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic"); 
		sql.append("\n  from rsdaTranceFile a inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(radaTranceId, sql, args, " and a.tranceId=?");
		return this.listQuery(sql.toString(), args.toArray(), RsdaTranceFile.class);
	}

	

}
