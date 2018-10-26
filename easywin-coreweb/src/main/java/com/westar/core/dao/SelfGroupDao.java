package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.westar.base.model.SelfGroup;
import com.westar.base.model.UserInfo;

@Repository
public class SelfGroupDao extends BaseDao {
	/**
	 * 取得客户的分享组
	 * @param comId
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> getCrmGrp(Integer comId, Integer customerId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select b.id, b.comid,b.grpName,b.orderNo,b.allSpelling,b.firstSpelling  ");
		sql.append("\n from customerShareGroup a inner join selfGroup b on a.comid=b.comid and a.grpId=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(customerId, sql, args, " and a.customerId=?");
		return this.listQuery(sql.toString(), args.toArray(), SelfGroup.class);
	}
	/**
	 * 取得项目的分享组
	 * @param comId
	 * @param itemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> getItemGrp(Integer comId, Integer itemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select b.id, b.comid,b.grpName,b.orderNo,b.allSpelling,b.firstSpelling  ");
		sql.append("\n from itemsharegroup a inner join selfGroup b on a.comid=b.comid and a.grpId=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(itemId, sql, args, " and a.itemId=?");
		return this.listQuery(sql.toString(), args.toArray(), SelfGroup.class);
	}

	/**
	 * 取得分组成员
	 * @param grpId
	 * @param sessionUser
	 * @param onlySubState 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listGroupUser(Integer grpId, UserInfo sessionUser, String onlySubState) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("\n select b.*,d.uuid as smImgUuid,d.filename as smImgName,dep.depname,formedoUser.Id as formedoUserId,formedoUser.Username as formedoUserName ");
		sql.append("\n from groupPersion a inner join userinfo b on a.userinfoid=b.id ");
		sql.append("\n inner join userOrganic c on a.comid = c.comid and b.id = c.userid and c.enabled =1");
		sql.append("\n left join upfiles d on c.comid = d.comid and c.smallheadportrait = d.id");
		sql.append("\n left join department dep on c.comid = dep.comid and c.depid = dep.id");
		sql.append("\n left join formedo on a.userinfoid=formedo.creator");
		sql.append("\n left join userOrganic formedoOrg on formedo.comid=formedoOrg.Comid and formedo.userid=formedoOrg.Userid and formedoOrg.Enabled=1");
		sql.append("\n left join userinfo formedoUser on formedoOrg.userid=formedoUser.Id");
		sql.append("\n where 1=1");
		this.addSqlWhere(grpId, sql, args, " and a.grpId=?");
		this.addSqlWhere(sessionUser.getComId(), sql, args, " and a.comId=?");
		if(StringUtils.isNotEmpty(onlySubState) && onlySubState.equals("1")){
			sql.append("\n and exists (");
			sql.append("\n select id from myLeaders where creator = b.id and leader = ? and comId = ? and creator <> leader ");
			args.add(sessionUser.getId());
			args.add(sessionUser.getComId());
			sql.append("\n )");
		}
		sql.append("\n order by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 个人分组的最大排序号
	 * @param comId
	 * @param owner
	 * @return
	 */
	public int getCurrentLevelMaxOrderNo(Integer comId, Integer owner) {
		String sql = "select case when max(orderNo) is null then 0 else  max(orderNo) end  from selfGroup where comid=? and owner=?";
		return this.countQuery(sql, new Object[] { comId,owner });
	}
	
	/**
	 * 分页查询个人分组信息
	 * @param group
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> listUserGroup(SelfGroup group) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* \n");
		sql.append("\n from selfgroup a");
		sql.append("\n where 1=1");
		this.addSqlWhere(group.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(group.getOwner(), sql, args, " and a.owner=?");
		this.addSqlWhereLeftLike(group.getGrpName(), sql, args, " and a.grpName like ?");
		sql.append("\n order by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), SelfGroup.class);
	}

	

}
