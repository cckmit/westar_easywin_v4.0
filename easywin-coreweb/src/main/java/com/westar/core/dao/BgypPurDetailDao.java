package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.BgypPurDetail;
import com.westar.base.model.UserInfo;

@Repository
public class BgypPurDetailDao extends BaseDao {

	/**
	 * 分页查询采购单详情
	 * @param sessionUser 当前操作人员
	 * @param bgypPurDetail 采购单详情查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypPurDetail> listPagedBgypPurDetail(UserInfo sessionUser,
			BgypPurDetail bgypPurDetail) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.bgypPrice,a.bgypNum,b.bgypName,b.bgypSpec,b.bgypUnit");
		sql.append("\n from bgypPurDetail a inner join bgypItem b on a.bgypitemid=b.id and a.comid=b.comid");
		sql.append("\n where a.purOrderId=?");
		args.add(bgypPurDetail.getPurOrderId());
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		return this.pagedQuery(sql.toString(), "a.id desc", args.toArray(), BgypPurDetail.class);
	}
	/**
	 * 分页查询采购单详情
	 * @param sessionUser 当前操作人员
	 * @param bgypPurDetail 采购单详情查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypPurDetail> listBgypPurDetail(UserInfo sessionUser,
			BgypPurDetail bgypPurDetail) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.comId,a.bgypPrice,a.bgypNum,b.bgypName,b.bgypSpec,b.bgypUnit,");
		sql.append("\n a.bgypItemId,c.zvalue bgypUnitName,a.storeState");
		sql.append("\n from bgypPurDetail a inner join bgypItem b on a.bgypitemid=b.id and a.comid=b.comid");
		sql.append("\n left join datadic c on b.bgypUnit = c.code and c.type='bgypUnit'");
		sql.append("\n where a.purOrderId=?");
		args.add(bgypPurDetail.getPurOrderId());
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		sql.append("\n order by a.id ");
		return this.listQuery(sql.toString(), args.toArray(), BgypPurDetail.class);
	}

	

}
