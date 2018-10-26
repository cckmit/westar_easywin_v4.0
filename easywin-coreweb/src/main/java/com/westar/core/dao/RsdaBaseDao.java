package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.RsdaBase;
import com.westar.base.model.RsdaBaseFile;
import com.westar.base.model.RsdaOther;
import com.westar.base.model.UserInfo;

@Repository
public class RsdaBaseDao extends BaseDao {

	/**
	 * 分页查询人事档案信息
	 * @param sessionUser 当前操作人员
	 * @param rsdaBase 人事档案的条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RsdaBase> listPagedRsdaBase(UserInfo sessionUser,
			RsdaBase rsdaBase) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select uInfo.id userId,uInfo.userName,uInfo.movePhone userTel,imgFile.uuid userImgUuid,imgFile.fileName userImgName,");
		sql.append("\n uInfo.gender,dep.depName,uOrg.recordCreateTime hireDate,uOrg.enabled,uInfo.nickName,");
		sql.append("\n rsdaBase.politStatus,rsdaBase.nativePro,rsdaBase.nativeCity");
		sql.append("\n ");
		sql.append("\n from userOrganic uOrg inner join userInfo uInfo on uOrg.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.comId=uOrg.comId and uOrg.bigHeadPortrait=imgFile.id");
		sql.append("\n inner join department dep on uOrg.comId=dep.comId and uOrg.depId=dep.id");
		sql.append("\n left join rsdaBase on uOrg.userId=rsdaBase.userId");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		//人员
		this.addSqlWhere(rsdaBase.getUserId(), sql, args, "\n and uOrg.userId=?");
		//部门
		this.addSqlWhere(rsdaBase.getDepId(), sql, args, "\n and uOrg.depId=?");
		
		//查询创建时间段
		this.addSqlWhere(rsdaBase.getStartDate(), sql, args, " and substr(uOrg.recordcreatetime,0,10)>=?");
		this.addSqlWhere(rsdaBase.getEndDate(), sql, args, " and substr(uOrg.recordcreatetime,0,10)<=?");
		//政治面貌
		this.addSqlWhere(rsdaBase.getPolitStatus(), sql, args, "\n and rsdaBase.politStatus=?");
		//模糊查询
		this.addSqlWhereLike(rsdaBase.getSearchContent(), sql, args, "\n and (uInfo.userName like ? or uInfo.nickName like ?)",2);
		
		
		return this.pagedQuery(sql.toString(), "uInfo.id", args.toArray(), RsdaBase.class);
	}

	/**
	 * 根据人员主键查询人事档案
	 * @param sessionUser 当前操作人员
	 * @param userId 人员主键
	 * @return
	 */
	public RsdaBase queryRsdaBaseByUserId(UserInfo sessionUser, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select uInfo.id userId,uInfo.userName,uInfo.movePhone userTel,imgFile.uuid userImgUuid,imgFile.fileName userImgName,");
		sql.append("\n uInfo.gender,dep.depName,uOrg.recordCreateTime hireDate,uOrg.enabled,uInfo.email,");
		sql.append("\n rsdaBase.id,rsdaBase.politStatus,rsdaBase.nativePro,rsdaBase.nativeCity,");
		sql.append("\n rsdaBase.marryStatus,rsdaBase.nation,rsdaBase.politDate,rsdaBase.idCardNum,rsdaBase.homeAdress,");
		sql.append("\n rsdaOther.id rsdaOtherId,rsdaOther.jobNumber,rsdaOther.employeeType,rsdaOther.dutyName,rsdaOther.payrollType,");
		sql.append("\n rsdaOther.gwName,rsdaOther.zcName,rsdaOther.urgenterName,rsdaOther.urgenterTel,");
		
		sql.append("\n rsdaBase.eduFormal,rsdaBase.degree,rsdaBase.graduateDate,rsdaBase.schoolName,rsdaBase.major,");
		sql.append("\n rsdaBase.special,rsdaBase.remark");
		sql.append("\n ");
		sql.append("\n from userOrganic uOrg inner join userInfo uInfo on uOrg.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.comId=uOrg.comId and uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n inner join department dep on uOrg.comId=dep.comId and uOrg.depId=dep.id");
		sql.append("\n left join rsdaBase on uOrg.userId=rsdaBase.userId");
		sql.append("\n left join rsdaOther on uOrg.userId=rsdaOther.userId and rsdaOther.comId=uOrg.comId");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(userId, sql, args, "\n and uOrg.userId=?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		return (RsdaBase) this.objectQuery(sql.toString(), args.toArray(), RsdaBase.class);
	}
	/**
	 * 根据人员主键查询人事档案
	 * @param sessionUser 当前操作人员
	 * @param rsdaId 人事档案主键
	 * @return
	 */
	public RsdaBase queryRsdaBaseByRsdaId(UserInfo sessionUser, Integer rsdaId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select uInfo.id userId,uInfo.userName,uInfo.movePhone userTel,imgFile.uuid userImgUuid,imgFile.fileName userImgName,");
		sql.append("\n uInfo.gender,dep.depName,uOrg.recordCreateTime hireDate,uOrg.enabled,uInfo.email,");
		sql.append("\n rsdaBase.id,rsdaBase.politStatus,rsdaBase.nativePro,rsdaBase.nativeCity,");
		sql.append("\n rsdaBase.marryStatus,rsdaBase.nation,rsdaBase.politDate,rsdaBase.idCardNum,rsdaBase.homeAdress,");
		sql.append("\n rsdaOther.id rsdaOtherId,rsdaOther.jobNumber,rsdaOther.employeeType,rsdaOther.dutyName,rsdaOther.payrollType,");
		sql.append("\n rsdaOther.gwName,rsdaOther.zcName,rsdaOther.urgenterName,rsdaOther.urgenterTel,");
		
		sql.append("\n rsdaBase.eduFormal,rsdaBase.degree,rsdaBase.graduateDate,rsdaBase.schoolName,rsdaBase.major,");
		sql.append("\n rsdaBase.special,rsdaBase.remark");
		sql.append("\n ");
		sql.append("\n from userOrganic uOrg inner join userInfo uInfo on uOrg.userId=uInfo.id");
		sql.append("\n left join upfiles imgFile on uOrg.comId=uOrg.comId and uOrg.smallHeadPortrait=imgFile.id");
		sql.append("\n inner join department dep on uOrg.comId=dep.comId and uOrg.depId=dep.id");
		sql.append("\n left join rsdaBase on uOrg.userId=rsdaBase.userId");
		sql.append("\n left join rsdaOther on uOrg.userId=rsdaOther.userId and rsdaOther.comId=uOrg.comId");
		sql.append("\n");
		sql.append("\n where 1=1");
		this.addSqlWhere(rsdaId, sql, args, "\n and rsdaBase.id=?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and uOrg.comId=?");
		return (RsdaBase) this.objectQuery(sql.toString(), args.toArray(), RsdaBase.class);
	}
	/**
	 * 修改人事档案的工号
	 * @param rsdaOther
	 */
	public void updteRsdaJobNum(RsdaOther rsdaOther) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("update rsdaOther set jobNumber=? where comId=? and userId=?" );
		args.add(rsdaOther.getJobNumber());
		args.add(rsdaOther.getComId());
		args.add(rsdaOther.getUserId());
		Integer resultNum = this.excuteSql(sql.toString(), args.toArray());
		if(resultNum==0){
			this.add(rsdaOther);
		}
	}

	/**
	 * 查询人事档案的材料信息
	 * @param rsdaBaseId 人事档案主键
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RsdaBaseFile> listRsdaBaseFiles(Integer rsdaBaseId,
			UserInfo sessionUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comid,a.rsdaBaseId,a.upfileId,b.fileName,b.fileExt,b.uuid fileUuid, ");
		sql.append("\n  case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic"); 
		sql.append("\n  from rsdaBaseFile a inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(rsdaBaseId, sql, args, " and a.rsdaBaseId=?");
		return this.listQuery(sql.toString(), args.toArray(), RsdaBaseFile.class);
	}
}
