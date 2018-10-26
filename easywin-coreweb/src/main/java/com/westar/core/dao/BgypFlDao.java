package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.BgypFl;

@Repository
public class BgypFlDao extends BaseDao {

	/**
	 * 取得办公用品树形集合
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypFl> listTreeBgypFl(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select id,flName,parentid,flCode from bgypFl where 1=1");
		this.addSqlWhere(comId, sql, args, " and comId=? ");
		sql.append("\n start with parentid=-1 CONNECT BY PRIOR id = parentid");
		sql.append("\n order siblings by flCode");
		return this.listQuery(sql.toString(), args.toArray(), BgypFl.class);
	}

	/**
	 * 验证办公用品
	 * @param comId 团队号
	 * @param bgypFlId 办公用品分类主键
	 * @param flCode 办公用品分类代码
	 * @return
	 */
	public Integer countBgypFlByCode(Integer comId, Integer bgypFlId, String flCode) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(id) from (");
		sql.append("\n select id from bgypFl where 1=1");
		this.addSqlWhere(comId, sql, args, " and comId=? ");
		this.addSqlWhere(flCode, sql, args, " and flCode=? ");
		sql.append("\n )a where a.id<>?");
		args.add(bgypFlId);
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 查询删除的分类有没有办公用品
	 * @param sessionUser 当前操作人员
	 * @param bgypFlIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypFl> listBgypFls4Del(Integer comId,
			Integer[] bgypFlIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.flName,a.flCode");
		sql.append("\n from bgypFl a ");
		sql.append("\n where 1=1 ");
		this.addSqlWhereIn(bgypFlIds, sql, args, "\n and a.id in ?");
		this.addSqlWhere(comId, sql, args, "\n and a.comId = ?");
		sql.append("\n and exists( ");
		sql.append("\n select b.id from bgypItem b where b.comId=a.comId and a.id=b.flId");
		sql.append("\n )");
		return this.listQuery(sql.toString(), args.toArray(), BgypFl.class);
	}

	/**
	 * 取得办公用品详情
	 * @param bgypFlId 办公用品主键
	 * @return
	 */
	public BgypFl queryBgypFlDetailById(Integer bgypFlId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.parentId,a.flCode,a.flName,b.flName as pFlName");
		sql.append("\n from bgypfl a left join bgypfl b on a.parentId=b.id");
		sql.append("\n where 1=1 and a.id=?");
		args.add(bgypFlId);
		return (BgypFl) this.objectQuery(sql.toString(), args.toArray(), BgypFl.class);
	}

	/**
	 * 修改子类的所属关联
	 * @param comId 团队号
	 * @param bgypFlIds
	 */
	public void updateSonBgypFl(Integer comId, Integer[] bgypFlIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update bgypFl a set a.parentId=(select b.parentId from bgypFl b where a.parentId= b.id)");
		sql.append("\n where  a.parentId>0 ");
		this.addSqlWhereIn(bgypFlIds, sql, args, "\n and a.parentId in ?");
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 查询是否有分类过
	 * @param comId 团队号
	 * @return
	 */
	public Integer countComBgypFl(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(id) from bgypFl where 1=1 and comid="+comId);
		return this.countQuery(sql.toString(), args.toArray());
	}

	

}
