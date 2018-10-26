package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ModuleOperateConfig;

@Repository
public class ModOptConfDao extends BaseDao {

	/**
	 * 获取业务模块操作配置集合
	 * @param comId 企业主键
	 * @param moduleType 模块标识符
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModuleOperateConfig> listModOptConfig(Integer comId,String moduleType){
		List<Object> args=new ArrayList<Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("\n select a.code operatetype,a.zvalue moduleTypeName,");
		sql.append("\n case when b.enabled is null then 0 else b.enabled end enabled");
		sql.append("\n from datadic a left join moduleOperateConfig b on a.code=b.operatetype");
		sql.append("\n and b.comid =? and b.moduletype=?");
		sql.append("\n where a.type='operateType' and a.parentid>0");
		sql.append("\n order by a.id asc");
		args.add(comId);
		args.add(moduleType);
		return this.listQuery(sql.toString(), args.toArray(), ModuleOperateConfig.class);
	}
	
	/**
	 * 获取业务模块操作配置集合
	 * @param comId 企业主键
	 * @param moduleType 模块标识符
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModuleOperateConfig> listModuleOperateConfig(Integer comId,String moduleType){
		List<Object> args=new ArrayList<Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("select a.comid,a.moduletype,a.operatetype,a.enabled \n");
		sql.append("from moduleOperateConfig a where a.comid =? and a.moduletype=? \n");
		args.add(comId);
		args.add(moduleType);
		return this.listQuery(sql.toString(), args.toArray(), ModuleOperateConfig.class);
	}

	/**
	 * 判断操作权限
	 * @param comId 企业号
	 * @param moduleType 模块类型
	 * @param optType 操作权限
	 * @return
	 */
	public ModuleOperateConfig getModuleOperateConfig(Integer comId,
			String moduleType, String optType) {
		List<Object> args=new ArrayList<Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("select a.comid,a.moduletype,a.operatetype,a.enabled \n");
		sql.append("from moduleOperateConfig a where a.comid =? and a.moduletype=? and operatetype=?  \n");
		args.add(comId);
		args.add(moduleType);
		args.add(optType);
		return (ModuleOperateConfig) this.objectQuery(sql.toString(), args.toArray(), ModuleOperateConfig.class);
	}

}
