package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.RsdaResume;
import com.westar.base.model.RsdaResumeFile;
import com.westar.base.model.UserInfo;

@Repository
public class RsdaResumeDao extends BaseDao {

	/**
	 * 分页查询复职信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaResume 复职查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RsdaResume> listPagedRsdaResume(UserInfo sessionUser,
			RsdaResume rsdaResume) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select uInfo.gender userGender,uInfo.userName,imgFile.uuid userImgUuid,imgFile.filename userImgName,");
		sql.append("\n a.id,a.userDuty,a.resumeDate,a.resumeType,a.resumeDepId,dep.depName resumeDepName,a.remark");
		sql.append("\n from rsdaResume a inner join userOrganic uOrg on a.userId = uOrg.userId");
		sql.append("\n inner join userInfo uInfo on a.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n left join department dep on a.resumeDepId=dep.id");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		this.addSqlWhere(rsdaResume.getUserId(), sql, args, "\n and a.userId=?");
		
		this.addSqlWhere(rsdaResume.getResumeType(), sql, args, "\n and a.resumeType=?");
		//查询创建时间段
		this.addSqlWhere(rsdaResume.getStartDate(), sql, args, " and substr(a.resumeDate,0,10)>=?");
		this.addSqlWhere(rsdaResume.getEndDate(), sql, args, " and substr(a.resumeDate,0,10)<=?");
		//模糊查询
		this.addSqlWhereLike(rsdaResume.getRemark(), sql, args, "\n and a.remark like ? ");
				
		return this.pagedQuery(sql.toString(), "a.id desc", args.toArray(), RsdaResume.class);
	}

	/**
	 * 查询复职信息
	 * @param sessionUser 当前操作人员
	 * @param resumeId 复职信息主键
	 * @return
	 */
	public RsdaResume queryRsdaResumeById(UserInfo sessionUser, Integer resumeId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comId,a.userId,uInfo.gender userGender,uInfo.userName,imgFile.uuid userImgUuid,imgFile.filename userImgName,");
		sql.append("\n a.id,a.userDuty,a.resumeDate,a.resumeType,a.resumeDepId,dep.depName resumeDepName,a.remark");
		sql.append("\n ");
		sql.append("\n from rsdaResume a inner join userOrganic uOrg on a.userId = uOrg.userId");
		sql.append("\n inner join userInfo uInfo on a.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n left join department dep on a.resumeDepId=dep.id");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		this.addSqlWhere(resumeId, sql, args, "\n and a.id=?");
		return (RsdaResume) this.objectQuery(sql.toString(), args.toArray(), RsdaResume.class);
	}

	/**
	 * 查询复职附件
	 * @param sessionUser 当前操作人员
	 * @param resumeId 复职信息主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RsdaResumeFile> listResumeFiles(UserInfo sessionUser,
			Integer resumeId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comid,a.rsdaResumeId,a.upfileId,b.fileName,b.fileExt,b.uuid fileUuid, ");
		sql.append("\n  case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic"); 
		sql.append("\n  from rsdaResumeFile a inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(resumeId, sql, args, " and a.rsdaResumeId=?");
		return this.listQuery(sql.toString(), args.toArray(), RsdaResumeFile.class);
	}

	

}
