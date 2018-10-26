package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Jfzb;
import com.westar.base.model.JfzbType;

@Repository
public class JfzbTypeDao extends BaseDao {

	/**
	 * 查询积分指标分类信息
	 * @param comId 团队号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JfzbType> listJfzbType(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.id,a.comId,a.typeName,a.dicOrder");
		sql.append("\n from jfzbType a where a.comid =?");
		args.add(comId);
		sql.append("\n  order by a.dicOrder asc ");
		return this.listQuery(sql.toString(), args.toArray(),JfzbType.class);
	}
	/**
	 * 查询团队的指标分类最大排序
	 * @param comId 团队号
	 * @return
	 */
	public JfzbType initJfzbTypeOrder(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.dicOrder)+1  as dicOrder from jfzbType a where a.comid = ?");
		args.add(comId);
		return (JfzbType)this.objectQuery(sql.toString(), args.toArray(),JfzbType.class);
	}
	/**
	 * 查询使用该分类的积分标准
	 * @param jfzbTypeId
	 * @return
	 */
	public Integer queryJfzbForCheck(Integer jfzbTypeId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(a.id) ");
		sql.append("\n from jfzb a ");
		sql.append("\n where jfzbTypeId=? ");
		args.add(jfzbTypeId);
		return this.countQuery(sql.toString(), args.toArray());
	}

	

}
