package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ModAdmin;
import com.westar.base.model.UserInfo;

@Repository
public class ModAdminDao extends BaseDao {

	/**
	 * 查询团队的指定模块的管理人员
	 * @param comId 团队号
	 * @param busType 业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ModAdmin> listModAdmin(Integer comId, String busType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.comId,a.busType,a.userId,c.username adminName");
		sql.append("\n from modAdmin a ");
		sql.append("\n inner join  userInfo c on a.userId=c.id");
		sql.append("\n where 1=1");
		sql.append("\n and a.comId=? and a.busType=?");
		args.add(comId);
		args.add(busType);
		return this.listQuery(sql.toString(), args.toArray(), ModAdmin.class);
	}

	/**
	 * 验证是否为综合模块的管理人员
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 * @return
	 */
	public Integer countModAdmin(UserInfo sessionUser, String busType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(a.id) from modAdmin a where 1=1");
		this.addSqlWhere(sessionUser.getId(), sql, args, "\n and a.userId=?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(busType, sql, args, "\n and a.busType=?");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 查询团队的模块管理员信息
	 * @param comId 团队号
	 * @param busType 业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listBusTypeModAdmin(Integer comId, String busType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//任务负责人
		sql.append("select d.id,d.email,d.wechat,d.qq,d.userName from modAdmin c \n");
		sql.append("inner join userinfo d on  c.userId = d.id \n");
		sql.append("inner join userorganic e on  c.comid=e.comid and d.id=e.userid and e.enabled=1 \n");
		sql.append("where c.comid = ? and c.busType = ? \n");
		args.add(comId);
		args.add(busType);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	

}
