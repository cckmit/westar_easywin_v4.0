package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.BgypApplyDetail;
import com.westar.base.model.UserInfo;

@Repository
public class BgypApplyDetailDao extends BaseDao {

	/**
	 * 分页查询申领记录的详情
	 * @param sessionUser 当前操作人员
	 * @param bgypPurDetail 申领详情的查询条件
	 * @return
	 */
	public List<BgypApplyDetail> listPagedBgypApplyDetail(UserInfo sessionUser,
			BgypApplyDetail bgypPurDetail) {
		
		return null;
	}
	/**
	 * 查询申领记录的详情
	 * @param sessionUser 当前操作人员
	 * @param bgypPurDetail 申领详情的查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypApplyDetail> listBgypApplyDetail(UserInfo sessionUser,
			BgypApplyDetail bgypApplyDetail) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.applyingNum from (");
		sql.append("\n select a.id bgypItemId,a.comId,a.flId bgypFlId,a.bgypCode,");
		sql.append("\n a.bgypName,a.bgypSpec,a.bgypUnit,c.zvalue bgypUnitName,b.flName bgypFlName,");
		sql.append("\n case when a.storeNum is null then 0 else a.storeNum end storeNum,  ");
		sql.append("\n f.applyNum,f.id");
		sql.append("\n from bgypItem a inner join bgypFl b on a.comId=b.comId and a.flId=b.id");
		sql.append("\n left join datadic c on a.bgypUnit = c.code and c.type='bgypUnit'");
		sql.append("\n inner join bgypApplyDetail f on a.comid=f.comid and a.id=f.bgypItemId");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(bgypApplyDetail.getBgypApplyId(), sql, args, "\n and f.bgypApplyId=?");
		sql.append("\n )a left join (");
		sql.append("\n select c.bgypItemId,sum(c.applyNum) applyingNum");
		sql.append("\n from bgypItem a inner join bgypFl b on a.comId=b.comId and a.flId=b.id");
		sql.append("\n inner join bgypApplyDetail c on a.id=c.bgypItemId and a.comid=c.comId");
		sql.append("\n inner join bgypApply d on d.id=c.bgypApplyId and d.comid=c.comId");
		sql.append("\n where d.applyCheckState = 1");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		sql.append("\n group by c.bgypItemId");
		sql.append("\n )b on a.bgypItemId=b.bgypItemId");
		sql.append("\n order by a.id ");
		return this.listQuery(sql.toString(), args.toArray(), BgypApplyDetail.class);
	}
	/**
	 * 查询库存是否满足本次申请
	 * @param comId 团队号
	 * @param bgypApplyId 本次申请的主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BgypApplyDetail> listLossBgypItemStore(Integer comId, Integer bgypApplyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from (");
		sql.append("\n select c.bgypName,(a.applynum - c.storeNum) as applynum");
		sql.append("\n from bgypApplyDetail a inner join bgypItem c on a.bgypItemId=c.id");
		sql.append("\n where a.comid=? and a.bgypApplyId=?");
		args.add(comId);
		args.add(bgypApplyId);
		sql.append("\n )a where applynum>0");
		return this.listQuery(sql.toString(), args.toArray(), BgypApplyDetail.class);
	}

	

}
