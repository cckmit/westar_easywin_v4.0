package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.RsdaLeave;
import com.westar.base.model.RsdaLeaveFile;
import com.westar.base.model.UserInfo;

@Repository
public class RsdaLeaveDao extends BaseDao {

	/**
	 * 分页查询离职信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaLeave 离职信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RsdaLeave> listPagedRsdaLeave(UserInfo sessionUser,
			RsdaLeave rsdaLeave) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select uInfo.gender userGender,uInfo.userName,imgFile.uuid userImgUuid,imgFile.filename userImgName,");
		sql.append("\n a.id,a.userDuty,a.pactEndDate,a.leaveDate,a.leaveType,a.leaveReason");
		sql.append("\n from rsdaLeave a inner join userOrganic uOrg on a.userId = uOrg.userId");
		sql.append("\n inner join userInfo uInfo on a.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		this.addSqlWhere(rsdaLeave.getUserId(), sql, args, "\n and a.userId=?");
		
		this.addSqlWhere(rsdaLeave.getLeaveType(), sql, args, "\n and a.leaveType=?");
		//查询创建时间段
		this.addSqlWhere(rsdaLeave.getStartDate(), sql, args, " and substr(a.leaveDate,0,10)>=?");
		this.addSqlWhere(rsdaLeave.getEndDate(), sql, args, " and substr(a.leaveDate,0,10)<=?");
		//模糊查询
		this.addSqlWhereLike(rsdaLeave.getLeaveReason(), sql, args, "\n and (a.leaveReason like ? or a.userDuty like ? ",2);
		
		return this.pagedQuery(sql.toString(), "a.id desc", args.toArray(), RsdaLeave.class);
	}

	/**
	 * 根据主键查询离职信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaLeaveId 离职信息主键
	 * @return
	 */
	public RsdaLeave queryRsdaLeaveById(UserInfo sessionUser,
			Integer rsdaLeaveId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.comId,uInfo.gender userGender,uInfo.userName,imgFile.uuid userImgUuid,imgFile.filename userImgName,");
		sql.append("\n a.id,a.userDuty,a.pactEndDate,a.leaveDate,a.leaveType,a.remark,a.leaveReason,a.userId");
		sql.append("\n from rsdaLeave a inner join userOrganic uOrg on a.userId = uOrg.userId");
		sql.append("\n inner join userInfo uInfo on a.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		this.addSqlWhere(rsdaLeaveId, sql, args, "\n and a.id=?");
		return (RsdaLeave) this.objectQuery(sql.toString(), args.toArray(), RsdaLeave.class);
	}

	/**
	 * 查询离职信息的附件
	 * @param sessionUser 当前操作人员
	 * @param rsdaLeaveId 离职信息主键
	 * @return
	 */
	public List<RsdaLeaveFile> listRsdaLeaveFiles(UserInfo sessionUser,
			Integer rsdaLeaveId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comid,a.rsdaLeaveId,a.upfileId,b.fileName,b.fileExt,b.uuid fileUuid, ");
		sql.append("\n  case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic"); 
		sql.append("\n  from rsdaLeaveFile a inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(rsdaLeaveId, sql, args, " and a.rsdaLeaveId=?");
		return this.listQuery(sql.toString(), args.toArray(), RsdaLeaveFile.class);
	}

	

}
