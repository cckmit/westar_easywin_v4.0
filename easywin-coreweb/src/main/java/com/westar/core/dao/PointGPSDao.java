package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.PointGPS;
import com.westar.base.model.UserInfo;

@Repository
public class PointGPSDao extends BaseDao {
	
	/**
	 *取得用户打卡位置
	 * @param userInfo
	 * @param pointGPSId
	 * @return
	 */
	public PointGPS getPointGPS(UserInfo userInfo,
			Integer pointGPSId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from (");
		sql.append("\n select a.* from pointGPS a where  1=1");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=?");
		sql.append("\n order by a.id desc");
		sql.append("\n)a where rownum=1");
		return (PointGPS) this.objectQuery(sql.toString(), args.toArray(), PointGPS.class);
	}

	

}
