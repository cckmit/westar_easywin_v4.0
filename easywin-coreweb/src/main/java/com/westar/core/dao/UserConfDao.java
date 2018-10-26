package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.UserConf;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class UserConfDao extends BaseDao {

	/**
	 * 查询人员配置项的开启状态
	 * @param comId 企业号
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserConf> listUserConfig(UserInfo sessionUser,String type) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n SELECT B.ZVALUE SYSCONFIGNAME,B.CODE SYSCONFCODE,");
		sql.append("\n CASE WHEN A.OPENSTATE IS NULL THEN 1 ELSE A.OPENSTATE END OPENSTATE");
		sql.append("\n FROM USERCONF A RIGHT JOIN DATADIC B　");
		sql.append("\n ON A.SYSCONFCODE=B.CODE AND USERID=? AND A.COMID=?");
		sql.append("\n WHERE B.TYPE='"+ConstantInterface.UCFG_SYS_SWITCH+"' AND B.PARENTID>0 and nvl(A.TYPE,'sysConfig')='"+ConstantInterface.UCFG_SYS_SWITCH+"'");
		sql.append("\n ORDER BY B.ID");
		args.add(sessionUser.getId());
		args.add(sessionUser.getComId());
		return this.listQuery(sql.toString(), args.toArray(), UserConf.class);
	}

	/**
	 * 取得人员的某个配置信息
	 * @param comId 企业号
	 * @param userId 操作员主键
	 * @param sysConfCode 配置项代码
	 * @return
	 */
	public UserConf queryUserConf(UserInfo sessionUser,UserConf userConf) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n SELECT A.ID,A.OPENSTATE");
		sql.append("\n FROM USERCONF A WHERE　1=1");
		this.addSqlWhere(sessionUser.getId(), sql, args, " AND A.USERID=?　");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " AND A.COMID=?　");
		this.addSqlWhere(userConf.getSysConfCode(), sql, args, " AND A.SYSCONFCODE=?　");
		this.addSqlWhere(userConf.getType(), sql, args, " AND A.TYPE=?　");
		return (UserConf) this.objectQuery(sql.toString(), args.toArray(), UserConf.class);
	}
	/**
	 * 查询用户的消息接收方式
	 * @param comId 企业号
	 * @param userId 消息接收人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserConf> listUserMsgWayConf(Integer comId, Integer userId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n SELECT B.ZVALUE SYSCONFIGNAME,B.CODE SYSCONFCODE,");
		sql.append("\n CASE WHEN A.OPENSTATE IS NULL THEN 1 ELSE A.OPENSTATE END OPENSTATE");
		sql.append("\n FROM USERCONF A RIGHT JOIN DATADIC B　");
		sql.append("\n ON A.SYSCONFCODE=B.CODE AND USERID=? AND A.COMID=?");
		sql.append("\n WHERE B.TYPE='"+ConstantInterface.UCFG_SYS_SWITCH+"' AND B.PARENTID>0 and A.TYPE='"+ConstantInterface.UCFG_SYS_SWITCH+"'");
		sql.append("\n AND B.CODE IN('2')");
		sql.append("\n ORDER BY B.ID");
		args.add(userId);
		args.add(comId);
		return this.listQuery(sql.toString(), args.toArray(), UserConf.class);
	}

	

}
