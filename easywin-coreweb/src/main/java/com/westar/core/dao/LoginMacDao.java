package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class LoginMacDao extends BaseDao {

	/**
	 * 取得账号是否在该pc登录过
	 * @param userId 用户主键
	 * @param smac mac地址
	 * @param ip 
	 * @return
	 */
	public Integer countLoginMac(String userId, String smac) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		
		sql.append("\n select count(*) from (");
		sql.append("\n select a.* from loginMac a where 1=1 ");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		this.addSqlWhere(smac, sql, args, " and a.mac=?");
		sql.append("\n )");
		return this.countQuery(sql.toString(), args.toArray());
	}

	

}
