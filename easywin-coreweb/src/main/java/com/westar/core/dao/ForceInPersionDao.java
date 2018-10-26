package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ForceInPersion;
import com.westar.base.model.UserInfo;

@Repository
public class ForceInPersionDao extends BaseDao {

	/**
	 * 取得模块监督人员集合
	 * @param comId 团队号
	 * @param busType 业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ForceInPersion> listForceInPerson(Integer comId, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,c.username sharerName,c.gender,d.uuid");
		sql.append("\n from ForceInPersion a inner join userorganic b on a.comId=b.comId and b.enabled=1 and a.userId=b.userId");
		sql.append("\n inner join userinfo c on a.userId=c.id");
		sql.append("\n left join upfiles d on b.comId=d.comId and b.smallheadportrait=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busType, sql, args, " and a.type=?");
		sql.append("\n order by a.id asc");
		return this.listQuery(sql.toString(), args.toArray(), ForceInPersion.class);
	}

	/**
	 * 查询当前操作员是否为监督人员
	 * @param userInfo
	 * @param busType
	 * @return
	 */
	public ForceInPersion getForceInPersion(UserInfo userInfo, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from forceInPersion a where a.comId = ? and a.type=? and a.userId =? \n");
		args.add(userInfo.getComId());
		args.add(busType);
		args.add(userInfo.getId());
		return (ForceInPersion)this.objectQuery(sql.toString(), args.toArray(),ForceInPersion.class);
		
	}

	

}
