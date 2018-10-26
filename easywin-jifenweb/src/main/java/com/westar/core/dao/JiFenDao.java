package com.westar.core.dao;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Jifen;
import com.westar.base.model.JifenConfig;
import com.westar.base.model.JifenHistory;
import com.westar.base.model.JifenLev;
import com.westar.base.util.DateTimeUtil;

/**
 * 
 * 描述: 系统使用积分情况
 * @author zzq
 * @date 2018年8月27日 上午10:34:51
 */
@Repository
public class JiFenDao extends BaseDao {

	/**
	 * 积分项配置列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JifenConfig> listJiFenConfigs() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from jifenconfig order by orderNo asc,id desc");
		return this.listQuery(sql.toString(), null, JifenConfig.class);
	}
	/**
	 * 积分项配置分页列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JifenConfig> listPagedJiFenConfigs() {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from jifenconfig a ");
		return this.pagedQuery(sql.toString(), " a.orderNo asc,a.id desc ", null, JifenConfig.class);
	}

	/**
	 * 积分等级配置列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JifenLev> listJifenLevConfig() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from JifenLev order by levMinScore asc");
		return this.listQuery(sql.toString(), null, JifenLev.class);
	}
	/**
	 * 积分等级配置分页列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JifenLev> listPagedJifenLevConfig() {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from JifenLev a ");
		return this.pagedQuery(sql.toString()," a.levMinScore asc,a.id ", null, JifenLev.class);
	}

	/**
	 * 积分配置排序最大值
	 * @return
	 */
	public Integer queryJifenConfigOrderMax() {
		StringBuffer sql = new StringBuffer();
		sql.append("\n select case when max(a.orderNo) is null then 1 else max(a.orderno)+1 end as maxOrder");
		sql.append("\n from jifenConfig a ");
		return this.countQuery(sql.toString(), null);
	}

	/**
	 * 验证类别代码是否有重复
	 * @param jifenCode
	 * @param id
	 * @return
	 */
	public Integer validateJifenCode(String jifenCode, Integer id) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(*) from (");
		sql.append("\n select * from jifenConfig where 1=1 ");
		this.addSqlWhere(jifenCode, sql, args, " and jifenCode=?");
		sql.append("\n minus ");
		sql.append("\n select * from jifenConfig where 1=1 ");
		this.addSqlWhere(id, sql, args, " and id=?");
		sql.append("\n )");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 验证等级名称是否有重复
	 * @param levName
	 * @param id
	 * @return
	 */
	public Integer validateJifenLevName(String levName, Integer id) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(*) from (");
		sql.append("\n select * from jifenLev where 1=1 ");
		this.addSqlWhere(levName, sql, args, " and levName=?");
		sql.append("\n minus ");
		sql.append("\n select * from jifenLev where 1=1 ");
		this.addSqlWhere(id, sql, args, " and id=?");
		sql.append("\n )");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 积分历史
	 * @param jifenHistory
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JifenHistory> listPagedJifenHistory(JifenHistory jifenHistory) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.recordCreateTime,a.jifenChange,a.allScore,");
		sql.append("\n case when a.content is null then b.content else a.content end content");
		sql.append("\n from jifenHistory a inner join jifenConfig b on a.configId=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(jifenHistory.getComId(), sql, args, " and a.comid=?");
		this.addSqlWhere(jifenHistory.getUserId(), sql, args, " and a.userId=?");
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(), JifenHistory.class);
	}
	/**
	 * 业务类型所对应的积分项信息
	 * @param jifenCode
	 * @return
	 */
	public JifenConfig getJifenConfigByCode(String jifenCode) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from jifenConfig where 1=1 ");
		this.addSqlWhere(jifenCode, sql, args, " and jifenCode=?");
		return (JifenConfig) this.objectQuery(sql.toString(), args.toArray(), JifenConfig.class);
	}
	/**
	 * 当前用户的总积分
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Integer getJifenScore(Integer comId, Integer userId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n	select  case when sum(allscore) is null then 0 else sum(allscore) end allscore from jifenHistory where 1=1");
		this.addSqlWhere(comId, sql, args, " and comId=?");
		this.addSqlWhere(userId, sql, args, " and userId=?");
		sql.append("\n	and id = (");
		sql.append("\n	select max(id) from jifenHistory where 1=1");
		this.addSqlWhere(comId, sql, args, " and comId=?");
		this.addSqlWhere(userId, sql, args, " and userId=?");
		sql.append("\n	)");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 用户日常任务积分历史
	 * @param comId 企业编码
	 * @param userId 用户主键
	 * @param configId 积分项主键
	 * @param date 
	 * @return
	 */
	public Integer getJifenScore(Integer comId, Integer userId, Integer configId,String date) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select case when sum(a.jifenChange) is null then 0 else sum(a.jifenChange) end jifenChange from jifenHistory a where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		this.addSqlWhere(configId, sql, args, " and a.configId=?");
		this.addSqlWhere(date, sql, args, " and substr(a.recordcreatetime,0,10)=?");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 修改积分
	 * @param comId 企业编号
	 * @param userId 用户主键
	 * @param allJifen 现有的积分
	 */
	public void updateJifen(Integer comId, Integer userId, Integer allJifen) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append(" update userOrganic set jifenScore=? where comId=? and  userId=?");
		args.add(allJifen);
		args.add(comId);
		args.add(userId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 当前操作员的总排名
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Jifen getAllOrder(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from (");
		sql.append(" select dense_rank() over (order by a.jifenscore desc)jifenOrder, a.jifenscore,a.userId from userorganic a where a.enabled=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append(" )a where 1=1");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		return (Jifen) this.objectQuery(sql.toString(), args.toArray(), Jifen.class);
	}

	/**
	 * 取得积分以及排序
	 * @param comId
	 * @param userId
	 * @param type
	 * @return
	 * @throws ParseException 
	 */
	public Jifen getJifen(Integer comId, Integer userId, Integer type) throws ParseException {
		//当前日期
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select *  from (");
		sql.append("\n select case when a.jifenScore is null then 0 else a.jifenScore end jifenScore,");
		sql.append("\n dense_rank() over ( order by case when a.jifenchange is null then 0 else a.jifenchange end desc) jifenOrder,");
		sql.append("\n b.userId");
		sql.append("\n  from (");
		sql.append("\n select sum(a.jifenchange) jifenchange ,sum(a.jifenchange) jifenScore,a.userId");
		sql.append("\n from jifenhistory a left join userorganic b on a.userid=b.userid and a.comid=b.comid"); 
		sql.append("\n where  1=1 ");
		if(1==type){//本周
			this.addSqlWhere(comId, sql, args, " and a.comId=? ");
			String weekS = DateTimeUtil.getFirstDayOfWeek(nowTime,DateTimeUtil.yyyy_MM_dd);
			String weekE = DateTimeUtil.getLastDayOfWeek(nowTime,DateTimeUtil.yyyy_MM_dd);
			sql.append("and (substr(a.recordcreatetime,0,10)<='"+weekE+"' and substr(a.recordcreatetime,0,10)>='"+weekS+"')");
		}else{//本月
			this.addSqlWhere(comId, sql, args, " and a.comId=?");
			this.addSqlWhere(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM),
					sql, args, " and substr(a.recordcreatetime,0,7)=?");
		}
		sql.append("\n group by a.userid");
		sql.append("\n ) a right join userorganic b on a.userid=b.userid"); 
		sql.append("\n where  b.enabled=1");
		this.addSqlWhere(comId, sql, args, " and b.comId=?");
		sql.append("\n )a where 1=1 ");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		return (Jifen) this.objectQuery(sql.toString(), args.toArray(), Jifen.class);
	}
	/**
	 * 积分名次
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Jifen> listPagedJifenOrder(Integer comId) {
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select dense_rank() over (order by a.jifenscore desc)jifenOrder, a.jifenscore,b.userName,c.depName,a.userId");
		sql.append("\n from userorganic a left join userInfo b on a.userId=b.id ");
		sql.append("\n left join department c on a.depId=c.id and c.comId=a.comId ");
		sql.append("\n where a.enabled=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.pagedQuery(sql.toString(), " jifenOrder asc, a.id ", args.toArray(), Jifen.class);
	}
	/**
	 * 积分名次
	 * @param comId
	 * @param type
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public List<Jifen> listPagedJifenOrder(Integer comId,String type) throws ParseException {
		//当前日期
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select case when a.jifenScore is null then 0 else a.jifenScore end jifenScore,");
		sql.append("\n dense_rank() over ( order by case when a.jifenchange is null then 0 else a.jifenchange end desc) jifenOrder,");
		sql.append("\n c.userName,d.depName,b.userId");
		sql.append("\n  from (");
		sql.append("\n select sum(a.jifenchange) jifenchange ,sum(a.jifenchange) jifenScore,a.userId");
		sql.append("\n from jifenhistory a left join userorganic b on a.userid=b.userid and a.comid=b.comid"); 
		sql.append("\n where  1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		if("1".equals(type)){//本周
			String weekS = DateTimeUtil.getFirstDayOfWeek(nowTime,DateTimeUtil.yyyy_MM_dd);
			String weekE = DateTimeUtil.getLastDayOfWeek(nowTime,DateTimeUtil.yyyy_MM_dd);
			sql.append("and (substr(a.recordcreatetime,0,10)<='"+weekE+"' and substr(a.recordcreatetime,0,10)>='"+weekS+"')");
		}else{//本月
			this.addSqlWhere(comId, sql, args, " and a.comId=?");
			this.addSqlWhere(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM),
					sql, args, " and substr(a.recordcreatetime,0,7)=?");
		}
		sql.append("\n group by a.userid");
		sql.append("\n ) a right join userorganic b on a.userid=b.userid"); 
		sql.append("\n left join userInfo c on b.userid=c.id"); 
		sql.append("\n left join department d on b.depId=d.id");
		sql.append("\n where  b.enabled=1");
		this.addSqlWhere(comId, sql, args, " and b.comId=?");
		return this.pagedQuery(sql.toString(), " jifenOrder asc,b.depId,a.userId ", args.toArray(), Jifen.class);
	}
	/**
	 * 当前人员是否在该积分项积过分
	 * @param comId 企业号
	 * @param userId 用户主键
	 * @param modId 模块主键
	 * @param configId 业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JifenHistory> getJifenHistory(Integer comId, Integer userId,
			Integer modId, Integer configId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from jifenhistory a where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		this.addSqlWhere(configId, sql, args, " and a.configId=?");
		this.addSqlWhere(modId, sql, args, " and a.modId=?");
		return this.listQuery(sql.toString(), args.toArray(), JifenHistory.class);
	}
	/**
	 * 用户的最近等级
	 * @param jifenScore 当前总积分
	 * @return
	 */
	public JifenLev getUserJifenLevMin(Integer jifenScore) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from jifenLev where levMinScore=(");
		sql.append("\n select max(levMinScore) from jifenLev where levMinScore<=?)");
		args.add(jifenScore);
		return (JifenLev) this.objectQuery(sql.toString(), args.toArray(), JifenLev.class);
	}
	/**
	 * 下一等级
	 * @param jifenScore 当前总积分
	 * @return
	 */
	public JifenLev getUserJifenLevNext(Integer jifenScore) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from jifenLev where levMinScore=(");
		sql.append("\n select min(levMinScore) from jifenLev where levMinScore>?)");
		args.add(jifenScore);
		return (JifenLev) this.objectQuery(sql.toString(), args.toArray(), JifenLev.class);
	}

}
