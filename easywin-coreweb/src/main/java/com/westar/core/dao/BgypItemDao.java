package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.BgypItem;
import com.westar.base.model.UserInfo;

@Repository
public class BgypItemDao extends BaseDao {

	/**
	 * 分页查询分类的办公用品
	 * @param sessionUser 当前操作人员
	 * @param bgypItem 办公用品条目的查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypItem> listPagedBgypItem4Fl(UserInfo sessionUser,
			BgypItem bgypItem) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.flId,a.bgypCode,a.bgypName,a.bgypSpec,a.bgypUnit,c.zvalue bgypUnitName,");
		sql.append("\n b.flName,a.storeNum");
		sql.append("\n from bgypItem a inner join bgypFl b on a.comId=b.comId and a.flId=b.id");
		sql.append("\n left join datadic c on a.bgypUnit = c.code and c.type='bgypUnit'");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		Integer flId = bgypItem.getFlId();
		if(null!=flId){
			sql.append("\n and a.flId in( select id from bgypFl fl where 1=1");
			this.addSqlWhere(sessionUser.getComId(), sql, args, " and comId=? ");
			sql.append("\n start with fl.id="+flId+" CONNECT BY PRIOR fl.id = fl.parentid");
			sql.append("\n ) ");
			
		}
 		return this.pagedQuery(sql.toString(), " a.flId,a.id ", args.toArray(), BgypItem.class);
	}
	/**
	 * 分页查询分类的办公用品
	 * @param sessionUser 当前操作人员
	 * @param bgypItem 办公用品条目的查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypItem> listPagedBgypStore(UserInfo sessionUser,
			BgypItem bgypItem) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from (");
		sql.append("\n select a.id,a.comId,a.flId,a.bgypCode,a.bgypName,a.bgypSpec,a.bgypUnit,c.zvalue bgypUnitName,");
		sql.append("\n b.flCode,b.flName,a.storeNum,a.bgypCode||a.bgypName content");
		sql.append("\n from bgypItem a inner join bgypFl b on a.comId=b.comId and a.flId=b.id");
		sql.append("\n left join datadic c on a.bgypUnit = c.code and c.type='bgypUnit'");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		Integer flId = bgypItem.getFlId();
		if(null!=flId){
			sql.append("\n and a.flId in( select id from bgypFl fl where 1=1");
			this.addSqlWhere(sessionUser.getComId(), sql, args, " and comId=? ");
			sql.append("\n start with fl.id="+flId+" CONNECT BY PRIOR fl.id = fl.parentid");
			sql.append("\n ) ");
		}
		sql.append("\n )a where 1=1");
		this.addSqlWhereLike(bgypItem.getBgypName(), sql, args, "\n and a.content like ?");
		return this.pagedQuery(sql.toString(), "a.flCode,a.bgypCode", args.toArray(), BgypItem.class);
	}
	/**
	 * 查询分类下的所有条目
	 * @param sessionUser 当前操作员
	 * @param flId 分类主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypItem> listBgypItem4FlWithSub(UserInfo sessionUser,
			Integer flId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.flId,a.bgypCode,a.bgypName,a.bgypSpec,a.bgypUnit,c.zvalue bgypUnitName,");
		sql.append("\n b.flName");
		sql.append("\n from bgypItem a inner join bgypFl b on a.comId=b.comId and a.flId=b.id");
		sql.append("\n left join datadic c on a.bgypUnit = c.code and c.type='bgypUnit'");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		if(null!=flId){
			sql.append("\n and a.flId in( select id from bgypFl fl where 1=1");
			this.addSqlWhere(sessionUser.getComId(), sql, args, " and comId=? ");
			sql.append("\n start with fl.id="+flId+" CONNECT BY PRIOR fl.id = fl.parentid");
			sql.append("\n ) ");
		}
		return this.pagedQuery(sql.toString(), " a.flId,a.id ", args.toArray(), BgypItem.class);
	}
	
	/**
	 * 按分类查询办公用品的库存信息
	 * @param sessionUser
	 * @param flId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypItem> listBgypStore4FlWithSub(UserInfo sessionUser,
			Integer flId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select a.*,b.applyingNum from (");
		sql.append("\n select a.id,a.comId,a.flId bgypFlId,a.bgypCode,");
		sql.append("\n a.bgypName,a.bgypSpec,a.bgypUnit,c.zvalue bgypUnitName,b.flName,");
		sql.append("\n case when a.storeNum is null then 0 else a.storeNum end storeNum  ");
		sql.append("\n from bgypItem a inner join bgypFl b on a.comId=b.comId and a.flId=b.id");
		sql.append("\n left join datadic c on a.bgypUnit = c.code and c.type='bgypUnit'");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		if(null!=flId){
			sql.append("\n and a.flId in( select id from bgypFl fl where 1=1");
			this.addSqlWhere(sessionUser.getComId(), sql, args, " and comId=? ");
			sql.append("\n start with fl.id="+flId+" CONNECT BY PRIOR fl.id = fl.parentid");
			sql.append("\n ) ");
		}
		sql.append("\n )a left join (");
		sql.append("\n select c.bgypItemId,sum(c.applyNum) applyingNum");
		sql.append("\n from bgypItem a inner join bgypFl b on a.comId=b.comId and a.flId=b.id");
		sql.append("\n inner join bgypApplyDetail c on a.id=c.bgypItemId and a.comid=c.comId");
		sql.append("\n inner join bgypApply d on d.id=c.bgypApplyId and d.comid=c.comId");
		sql.append("\n where d.applyCheckState = 1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		if(null!=flId){
			sql.append("\n and a.flId in( select id from bgypFl fl where 1=1");
			this.addSqlWhere(sessionUser.getComId(), sql, args, " and comId=? ");
			sql.append("\n start with fl.id="+flId+" CONNECT BY PRIOR fl.id = fl.parentid");
			sql.append("\n ) ");
		}
		sql.append("\n group by c.bgypItemId");
		sql.append("\n )b on a.id=b.bgypItemId");
		return this.pagedQuery(sql.toString(), " a.bgypFlId,a.id ", args.toArray(), BgypItem.class);
	}

	/**
	 * 查询
	 * @param comId
	 * @param i
	 * @param bgypCode
	 * @param flId 
	 * @return
	 */
	public Integer countBgypItemByCode(Integer comId, Integer bgypItemId, String bgypCode, Integer flId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(id) from (");
		sql.append("\n select id from bgypItem where 1=1");
		this.addSqlWhere(comId, sql, args, " and comId=? ");
		this.addSqlWhere(bgypCode, sql, args, " and bgypCode=? ");
		sql.append("\n )a where a.id<>?");
		args.add(bgypItemId);
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 根据条目主键查询
	 * @param comId 团队号
	 * @param bgypItemId 条目主键
	 * @return
	 */
	public BgypItem queryBgypItemById(Integer comId, Integer bgypItemId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.flId,a.bgypCode,a.bgypName,a.bgypSpec,a.bgypUnit,");
		sql.append("\n b.flName,a.storeNum");
		sql.append("\n from bgypItem a inner join bgypFl b on a.comId=b.comId and a.flId=b.id");
		sql.append("\n where a.id=? and a.comId=? ");
		args.add(bgypItemId);
		args.add(comId);
		return (BgypItem) this.objectQuery(sql.toString(), args.toArray(), BgypItem.class);
	}
	/**
	 * 查询本次需要删除的数据是否被使用过
	 * @param bgypItemIds 需要删除的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypItem> listBgypItem4Del(Integer[] bgypItemIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select a.id,a.bgypName from (");
		sql.append("\n select a.id,a.bgypName from bgypItem a where 1=1");
		this.addSqlWhereIn(bgypItemIds, sql, args, "\n and a.id in ?");
		sql.append("\n )a where 1=1");
		sql.append("\n and exists(");
		sql.append("\n select bPur.bgypItemId from bgypPurDetail bPur where bPur.bgypItemId=a.id");
		sql.append("\n union ");
		sql.append("\n select bApply.bgypItemId from bgypApplyDetail bApply where bApply.bgypItemId=a.id");
		sql.append("\n)");
		return this.listQuery(sql.toString(), args.toArray(), BgypItem.class);
	}

}
