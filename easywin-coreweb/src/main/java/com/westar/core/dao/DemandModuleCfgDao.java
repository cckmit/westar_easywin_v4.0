package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.DemandHandleStepCfg;
import com.westar.base.model.DemandModuleCfg;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;

@Repository
public class DemandModuleCfgDao extends BaseDao {

	/**
	 * 分页查询需求处理进度模板
	 * @param sessionUser 当前操作人员
	 * @param demandModuleCfg 
	 * @return
	 */
	public PageBean<DemandModuleCfg> listPagedDemandModuleCfg(
			UserInfo sessionUser, DemandModuleCfg demandModuleCfg) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		
		sql.append("\n select a.*,b.userName");
		sql.append("\n from demandModuleCfg a");
		sql.append("\n inner join userInfo b on a.userId=b.id");
		sql.append("\n where a.comId=?");
		args.add(sessionUser.getComId());
		this.addSqlWhereLike(demandModuleCfg.getModName(), sql, args, "\n and a.modName like ?");
 		return this.pagedBeanQuery(sql.toString(), " a.modifyTime desc", args.toArray(), DemandModuleCfg.class);
	}

	/**
	 * 查询需求处理进度模板
	 * @param sessionUser 当前操作人员
	 * @param demandModuleCfg 查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DemandModuleCfg> listDemandModuleCfg(UserInfo sessionUser,
			DemandModuleCfg demandModuleCfg) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		
		sql.append("\n select a.* ");
		sql.append("\n from demandModuleCfg a");
		sql.append("\n where a.comId=?");
		args.add(sessionUser.getComId());
		
		sql.append("\n order by a.modifyTime desc");
		return this.listQuery(sql.toString(), args.toArray(), DemandModuleCfg.class);
	}

	/**
	 * 根据名称查询需求处理进度模板
	 * @param comId 团队号
	 * @param modName 需求模板名称
	 * @return
	 */
	public DemandModuleCfg queryDemandModuleCfgByName(Integer comId ,String modName){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		
		sql.append("\n select a.* ");
		sql.append("\n from demandModuleCfg a");
		sql.append("\n where a.comId=?");
		args.add(comId);
		this.addSqlWhere(modName, sql, args, "\n and a.modName=?");
		return (DemandModuleCfg) this.objectQuery(sql.toString(), args.toArray(), DemandModuleCfg.class);
	}

	/**
	 * 查询询需求处理进度的步骤
	 * @param comId团队号
	 * @param demandModuleCfgId 需求进度模板
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DemandHandleStepCfg> listDemandHandleStepCfg(Integer comId,
			Integer demandModuleCfgId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		
		sql.append("\n select a.* ");
		sql.append("\n from demandHandleStepCfg a");
		sql.append("\n where a.comId=? and a.demandModuleCfgId=?");
		args.add(comId);
		args.add(demandModuleCfgId);
		sql.append("\n order by a.stepOrder,a.id");
		
		return this.listQuery(sql.toString(), args.toArray(), DemandHandleStepCfg.class);
	}
}
