package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.JobHisFile;
import com.westar.base.model.JobHistory;
import com.westar.base.model.UserInfo;

@Repository
public class JobHistoryDao extends BaseDao {

	/**
	 * 分页查询工作经历信息
	 * @param sessionUser
	 * @param jobHistory
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JobHistory> listPagedJobHistory(UserInfo sessionUser,
			JobHistory jobHistory) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select uInfo.gender userGender,uInfo.userName,imgFile.uuid userImgUuid,imgFile.filename userImgName,");
		sql.append("\n jobHis.id,jobHis.dutyed,jobHis.retereName,jobHis.componName,jobHis.startDate,jobHis.endDate");
		sql.append("\n from jobHistory jobHis inner join userOrganic uOrg on jobHis.userId = uOrg.userId");
		sql.append("\n inner join userInfo uInfo on jobHis.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		this.addSqlWhere(jobHistory.getUserId(), sql, args, "\n and jobHis.userId=?");
		//模糊查询
		this.addSqlWhereLike(jobHistory.getComponName(), sql, args, "\n and  jobHis.componName like ? ");
				
		return this.pagedQuery(sql.toString(), "jobHis.id desc", args.toArray(), JobHistory.class);
	}

	/**
	 * 查询工作经历详细信息
	 * @param sessionUser 当前操作人员
	 * @param jobHisId 工作经历主键
	 * @return
	 */
	public JobHistory queryJobHistoryById(UserInfo sessionUser, Integer jobHisId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select uInfo.gender userGender,uInfo.userName,imgFile.uuid userImgUuid,imgFile.filename userImgName,");
		sql.append("\n jobHis.id,jobHis.dutyed,jobHis.retereName,jobHis.componName,jobHis.startDate,jobHis.endDate,");
		sql.append("\n jobHis.achievement,jobHis.remark,jobHis.userId,jobHis.depName");
		sql.append("\n from jobHistory jobHis inner join userOrganic uOrg on jobHis.userId = uOrg.userId");
		sql.append("\n inner join userInfo uInfo on jobHis.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(jobHisId, sql, args, "\n and jobHis.id=?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		return (JobHistory) this.objectQuery(sql.toString(), args.toArray(), JobHistory.class);
	}

	/**
	 * 查询工作经历的附件信息
	 * @param sessionUser 当前操作人员
	 * @param jobHisId 工作经历的主键信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JobHisFile> listJobHisFiles(UserInfo sessionUser,
			Integer jobHisId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comid,a.jobHisId,a.upfileId,b.fileName,b.fileExt,b.uuid fileUuid, ");
		sql.append("\n  case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic"); 
		sql.append("\n  from jobHisFile a inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(jobHisId, sql, args, " and a.jobHisId=?");
		return this.listQuery(sql.toString(), args.toArray(), JobHisFile.class);
	}

	

}
