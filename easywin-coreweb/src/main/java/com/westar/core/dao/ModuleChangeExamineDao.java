package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ModuleChangeApply;
import com.westar.base.model.ModuleChangeExamine;
import com.westar.base.model.UserInfo;

@Repository
public class ModuleChangeExamineDao extends BaseDao {
	
	/**
	 * 根据业务类型查询属性变更审批配置
	 * @param moduleChangeExamine
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModuleChangeExamine> listModChangeExam(ModuleChangeExamine moduleChangeExamine, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from moduleChangeExamine a where a.comid =? and moduleType = ?");
		args.add(userInfo.getComId());
		args.add(moduleChangeExamine.getModuleType());
		this.addSqlWhere(moduleChangeExamine.getField(), sql, args, " and field = ?");
		return this.listQuery(sql.toString(), args.toArray(),ModuleChangeExamine.class);
	}
	
	/**
	 * 根据id查询属性变更申请详情
	 * @param id
	 * @return
	 */
	public ModuleChangeApply queryModChangeApplyById(Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,u.userName creatorName \n");
		sql.append("from moduleChangeApply a \n");
		sql.append("left join  userInfo u on u.id = a.creator \n");
		sql.append("where a.id= ? \n");
		args.add(id);
		return (ModuleChangeApply) this.objectQuery(sql.toString(), args.toArray(),ModuleChangeApply.class);
	}

	

}
