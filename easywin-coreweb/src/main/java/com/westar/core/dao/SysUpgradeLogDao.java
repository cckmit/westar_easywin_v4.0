package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.SysUpgradeLog;

@Repository
public class SysUpgradeLogDao extends BaseDao {
	
	/**
	 *  滚动查询升级日志
	 * @param sysUpLog
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysUpgradeLog> listSysUpLog(Integer maxId, Integer type){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id, a.content,a.versionName,a.recordcreatetime upgradeTime,a.terraceType from(  \n");
		sql.append("select * from sysupgradelog  where 1=1  \n");
		this.addSqlWhere(type, sql, args, " and terraceType = ?");
		if(null!=maxId && maxId>0 ){
			this.addSqlWhere(maxId, sql, args, " and id< ?");
		}
		sql.append(" order by recordcreatetime desc) a \n");
		sql.append("where  rownum<=5");
		return this.listQuery(sql.toString(),args.toArray(), SysUpgradeLog.class);
	}

}
