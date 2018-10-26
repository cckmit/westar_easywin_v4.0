package com.westar.core.dao;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.FestMod;
import com.westar.base.model.FestModDate;
import com.westar.base.model.UserInfo;

import flex.messaging.io.ArrayList;

@Repository
public class FestModDao extends BaseDao {

	/**
	 * 取得指定时间区间的节假日
	 * @param comId 团队号
	 * @param dateTimeSStrYMD 时间起
	 * @param dateTimeEStrYMD 时间止
	 * @param status 1休息日 2工作日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FestModDate> listFestModDates(Integer comId,
			String dateTimeSStrYMD, String dateTimeEStrYMD, Integer status) {
		
		List<Object> args = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select distinct b.festDate,b.status from festMod a");
		sql.append("\n inner join festModDate b on a.comid=b.comid and a.id=b.festModId");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(dateTimeSStrYMD, sql, args, " and b.festDate>=?");
		this.addSqlWhere(dateTimeEStrYMD, sql, args, " and b.festDate<=?");
		this.addSqlWhere(status, sql, args, " and b.status=?");
		sql.append("\n order by  b.festDate asc");
		return this.listQuery(sql.toString(), args.toArray(), FestModDate.class);
	}
	
	/**
	 * 取得指定时间区间的节假日
	 * @param comId 团队号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FestModDate> listFestModDates(Integer comId) {
		
		List<Object> args = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select distinct b.festDate,b.status,b.comId from festMod a");
		sql.append("\n inner join festModDate b on a.comid=b.comid and a.id=b.festModId");
		sql.append("\n where 1=1 and (a.comid=? or a.comid=?)");
		args.add(CommonConstant.SYSCOMID);
		args.add(comId);
		sql.append("\n order by  b.festDate asc");
		return this.listQuery(sql.toString(), args.toArray(), FestModDate.class);
	}
	
	/**
	 * 查询节假日日期
	 * @param festModId节假日主键
	 * @param status 状态 1休息日 2工作日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FestModDate> listFestModDates(Integer festModId, String status) {
		
		List<Object> args = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.festDate,b.status from festModDate b");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(festModId, sql, args, " and b.festModId=?");
		this.addSqlWhere(status, sql, args, " and b.status=?");
		sql.append("\n order by  b.festDate asc");
		return this.listQuery(sql.toString(), args.toArray(), FestModDate.class);
	}
	/**
	 * 获取该日 节假日信息
	 * @param comId
	 * @param date
	 * @param ruleType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public FestModDate festDateStatus(Integer comId,String date,String ruleType) {
		List<Object> args = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select b.festDate,b.status from festModDate b");
		sql.append("\n left join FestMod a on a.id = b.festModId and a.comId = b.comId");
		sql.append("\n where 1=1 ");
		if("1".equals(ruleType)){//是标准考勤
			sql.append("\n and b.comId in(?,?) ");
			args.add(comId);
			args.add(CommonConstant.SYSCOMID);
		}else{
			this.addSqlWhere(comId, sql, args, " and b.comId=?");
		}
		this.addSqlWhere(date, sql, args, " and b.festDate=?");
		return (FestModDate) this.objectQuery(sql.toString(), args.toArray(), FestModDate.class);
	}
	/**
	 * 查询企业本年度的节假日
	 * @param comId 团队号 0为系统的
	 * @param year 年度
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FestMod> listYearFestMod(Integer comId, Integer year) {
		List<Object> args = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from festMod a where 1=1");
		this.addSqlWhere(year, sql, args, " and a.year=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by  a.id ");
		return this.listQuery(sql.toString(), args.toArray(), FestMod.class);
	}

	/**
	 * 列表查询节假日
	 * @param sessionUser 当前操作人员
	 * @param festMod 带条件的节假日
	 * @param ruleType 采用的考勤制度
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FestMod> listFestMod(UserInfo sessionUser, FestMod festMod,String ruleType) {
		List<Object> args = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from (");
		
		sql.append("select a.* from festMod a where 1=1");
		this.addSqlWhere(festMod.getYear(), sql, args, " and a.year=?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comId=?");
		sql.append("\n and exists( ");
		sql.append("\n 	select b.id from festModDate b where a.comid=b.comid and a.id=b.festmodid ");
		this.addSqlWhere(festMod.getStatus(), sql, args, " and b.status=?");
		sql.append("\n ) ");
		
		if("1".equals(ruleType)){//是标准考勤
			sql.append("union all ");
			sql.append("select a.* from festMod a where a.comId="+CommonConstant.SYSCOMID);
			this.addSqlWhere(festMod.getYear(), sql, args, " and a.year=?");
			sql.append("\n and exists( ");
			sql.append("\n 	select b.id from festModDate b where a.comid=b.comid and a.id=b.festmodid ");
			this.addSqlWhere(festMod.getStatus(), sql, args, " and b.status=?");
			
			sql.append("\n ) ");
		}
		sql.append("\n )a");
		sql.append("\n order by a.festival desc,a.id");
		return this.listQuery(sql.toString(), args.toArray(),FestMod.class );
	}

	/**
	 * 列表查询节假日
	 * @param sessionUser 当前操作人员
	 * @param date 带条件的节假日
	 * @param ruleType 采用的考勤制度
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FestMod> listFestMod(UserInfo sessionUser, String date,String ruleType) {
		List<Object> args = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from (");
		
		sql.append("select a.* from festMod a where 1=1");
		if(null!= date && !date.isEmpty()){
			sql.append(" and a.festival <= ?");
			args.add(date);
			this.addSqlWhere(date.substring(0,4), sql, args, " and a.year=?");
		}
		this.addSqlWhere(date.substring(0,4), sql, args, " and a.year=?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comId=?");
		sql.append("\n and exists( ");
		sql.append("\n 	select b.id from festModDate b where a.comid=b.comid and a.id=b.festmodid ");
		sql.append("\n ) ");
		
		if("1".equals(ruleType)){//是标准考勤
			sql.append("union all ");
			sql.append("select a.* from festMod a where a.comId="+CommonConstant.SYSCOMID);
			if(null!= date && !date.isEmpty()){
				sql.append(" and a.festival <= ?");
				args.add(date);
				this.addSqlWhere(date.substring(0,4), sql, args, " and a.year=?");
			}
			sql.append("\n and exists( ");
			sql.append("\n 	select b.id from festModDate b where a.comid=b.comid and a.id=b.festmodid ");
			
			sql.append("\n ) ");
		}
		sql.append("\n )a");
		sql.append("\n order by a.festival desc,a.id");
		return this.listQuery(sql.toString(), args.toArray(),FestMod.class );
	}	

}
