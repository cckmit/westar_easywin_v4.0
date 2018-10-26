package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.SerialNum;
import com.westar.base.model.SpSerialNumRel;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class SerialNumDao extends BaseDao {

	/**
	 * 分页查询序列编号
	 * @param sessionUser 当前操作人员
	 * @param serialNum 序列编号查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SerialNum> listPagedSerialNum(UserInfo sessionUser,
			SerialNum serialNum) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*  ");
		sql.append("\n from serialNum a");
		sql.append("\n where a.comId=?");
		args.add(sessionUser.getComId());
		this.addSqlWhere(serialNum.getYear(), sql, args, "\n and a.year=?");
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(), SerialNum.class);
	}
	/**
	 * 分页查询序列编号用于编号
	 * @param sessionUser 当前操作人员
	 * @param serialNum 序列编号查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SerialNum> listPagedSerialNumSelect(UserInfo sessionUser,
			SerialNum serialNum) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*  ");
		sql.append("\n from serialNum a "); 
		sql.append("\n left join ( "); 
		sql.append("\n  select layout.comId, formconf.confvalue serialNumId from ("); 
		sql.append("\n  	select * from ("); 
		sql.append("\n  		select a.*,row_number() over (partition by a.formmodid order by a.version desc) as new_order"); 
		sql.append("\n  		from formlayout  a where 1=1 and a.comid=?"); 
		args.add(sessionUser.getComId());
		sql.append("\n  	) a where new_order=1"); 
		sql.append("\n  )layout inner join formconf on layout.id=formconf.formlayoutid and layout.formmodid=formconf.formmodid"); 
		sql.append("\n  where formconf.confname='serialNumId'"); 
		sql.append("\n  ) b on a.comId=b.comId and a.id=b.serialNumId"); 
		sql.append("\n where a.comId=? and (nvl(b.serialNumId,0)=0 or b.serialNumId=? )");
		args.add(sessionUser.getComId());
		args.add(serialNum.getExceptId());
		this.addSqlWhere(serialNum.getYear(), sql, args, "\n and a.year=?");
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(), SerialNum.class);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 查询
	 * @param sessionUser
	 * @param busId
	 * @param busType
	 * @return
	 */
	public SpSerialNumRel querySpSerialNumRel(UserInfo sessionUser,
			Integer busId, String busType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		
		if(busType.equals(ConstantInterface.TYPE_FLOW_SP)){//审批
			sql.append("\n select a.* ");
			sql.append("\n from spSerialNumRel a inner join spflowinstance sp on a.comid=sp.comid and a.busid=sp.id and a.bustype='"+ConstantInterface.TYPE_FLOW_SP+"'");
			sql.append("\n where a.comid=?");
			args.add(sessionUser.getComId());
			this.addSqlWhere(busId, sql, args, "\n and a.busId=?");
			this.addSqlWhere(busType, sql, args, "\n and a.busType=?");
		}
		return (SpSerialNumRel) this.objectQuery(sql.toString(), args.toArray(), SpSerialNumRel.class);
	}
	/**
	 * 查询用于验证的审批序列编号
	 * @param serialNumId
	 * @param serialNum
	 * @param busId 
	 * @param busType
	 * @return
	 */
	public Integer querySpSerialNumRelForCheck(Integer serialNumId,
			String serialNum, Integer busId, String busType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(a.id) ");
		sql.append("\n from spSerialNumRel a ");
		sql.append("\n where 1=1 and a.serialNum=? and a.busType=? and a.serialNumId=? and a.busId<>?");
		args.add(serialNum);
		args.add(busType);
		args.add(serialNumId);
		args.add(busId);
		return this.countQuery(sql.toString(), args.toArray());
	}

	

}
