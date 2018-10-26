package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ForMeDo;

@Repository
public class ForMeDoDao extends BaseDao {

	/**
	 * 查询离岗人员的代理人员
	 * @param comId 组织
	 * @param userId
	 * @return
	 */
	public ForMeDo queryInsteadUser(Integer comId, Integer userId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		sql.append("\n select a.id,a.comId,a.creator,a.userId,c.userName");
		sql.append("\n from ForMeDo a");
		sql.append("\n inner join userorganic b on a.comid=b.comid and a.userId=b.userId");
		sql.append("\n inner join userInfo c on a.userId=c.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(userId, sql, args, "\n and a.creator=?");
		this.addSqlWhere(comId, sql, args, "\n and a.comId=?");
		return (ForMeDo) this.objectQuery(sql.toString(), args.toArray(), ForMeDo.class);
	}
	
	/**
	 * 替岗人员信息
	 * @param comId
	 * @return
	 */
	public List<ForMeDo> listInsteadUser(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		sql.append("\n select a.id,a.comId,a.creator,f.userName creatorName,d.depId creatorDepId,");
		sql.append("\n b.depId userDepId,a.userId,c.userName");
		sql.append("\n from formedo a");
		//替岗人员
		sql.append("\n inner join userorganic b on a.comid=b.comid and a.userId=b.userId");
		sql.append("\n inner join userInfo c on a.userId=c.id");
		//创建人
		sql.append("\n inner join userorganic d on a.comid=d.comid and a.creator=d.userId");
		sql.append("\n inner join userInfo f on a.creator=f.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, "\n and a.comId=?");
		return this.listQuery(sql.toString(), args.toArray(), ForMeDo.class);
	}

	

}
