package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Gdzc;
import com.westar.base.model.GdzcMaintainRecord;
import com.westar.base.model.GdzcReduceRecord;
import com.westar.base.model.GdzcType;
import com.westar.base.model.GdzcUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class GdzcDao extends BaseDao {

	/**
	 * 固定资产列表
	 * @param gdzc
	 * @param userInfo
	 * @return
	 */
	public List<Gdzc> listGdzc(Gdzc gdzc, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("	select a.*,b.typeName ssTypeName,c.typeName addTypeName from gdzc a ");
		sql.append("\n	inner join  gdzcType b on a.ssType = b.id and a.comId = b.comid ");
		sql.append("\n	inner join  gdzcType c on a.addType = c.id and a.comId = c.comid ");
		
		sql.append("\n where a.comId = ? and a.state <> 0");
		args.add(userInfo.getComId());
		//固定资产类型
		this.addSqlWhere(gdzc.getSsType(), sql, args, " and a.ssType = ?");
		//添加类型
		this.addSqlWhere(gdzc.getAddDate(), sql, args, " and a.addType = ?");
		//创建时间
		this.addSqlWhere(gdzc.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(gdzc.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//添加时间
		this.addSqlWhere(gdzc.getAddStartDate(), sql, args, " and a.addDate>=?");
		this.addSqlWhere(gdzc.getAddEndDate(), sql, args, " and a.addDate<=?");
		
		//名称筛选
		if(null!=gdzc.getGdzcName() && !"".equals(gdzc.getGdzcName())){
			this.addSqlWhereLike(gdzc.getGdzcName(), sql, args, " and a.gdzcName like ? \n");
		}
		
		this.addSqlWhere(gdzc.getState(), sql, args, " and a.state = ?");
		return this.pagedQuery(sql.toString()," a.state,a.addDate desc", args.toArray(),Gdzc.class);
	}
	/**
	 * 固定资产类型类别
	 * @param comId
	 * @param busType
	 * @return
	 */
	public List<GdzcType> listGdzcType(Integer comId, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from gdzcType a where a.comId=? ");
		args.add(comId);
		this.addSqlWhere(busType, sql, args, " and a.busType = ?");
		sql.append(" order by a.orderNum asc");
		return this.listQuery(sql.toString(), args.toArray(),GdzcType.class);
	}
	/**
	 * 获取固定资产类型最大序列数
	 * @param comId
	 * @param busType
	 * @return
	 */
	public GdzcType queryGdzcTypeOrderMax(Integer comId, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.orderNum)+1  as orderNum from gdzcType a where a.comid =? and a.busType= ?");
		args.add(comId);
		args.add(busType);
		return (GdzcType)this.objectQuery(sql.toString(), args.toArray(),GdzcType.class);
	}
	/**
	 * 获取占用该类型的数量
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public Integer countGdzcByGdzcType(Integer id, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from (");
		sql.append("	select id  from Gdzc  where and (ssType = ? or addType = ?) and comId = ?");
		args.add(id);
		args.add(id);
		args.add(userInfo.getComId());
		
		sql.append("union all");
		sql.append("	select id  from gdzcReduceRecord  where and reduceType = ? and comId = ?");
		args.add(id);
		args.add(userInfo.getComId());
		
		sql.append("union all");
		sql.append("	select id  from gdzcMaintainRecord  where and maintainType = ? and comId = ?");
		args.add(id);
		args.add(userInfo.getComId());
		
		sql.append(")");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 通过ID查询固定资产详情
	 * @param gdzcId
	 * @param userInfo
	 * @return
	 */
	public Gdzc queryGdzcById(Integer gdzcId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.*,b.depName,c.userName managerName,c.gender,d.uuid userUuid,d.filename userFileName,e.typeName ssTypeName,f.typeName addTypeName   ");
		sql.append("\n	 from gdzc a");
		sql.append("\n	inner join department b on a.comid = b.comid and a.depId = b.id");
		sql.append("\n	inner join  userInfo c on a.manager = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n left join gdzcType e on e.comid = a.comid and e.id = a.ssType ");
		sql.append("\n left join gdzcType f on f.comid = a.comid and f.id = a.addType ");
		sql.append("\n where a.comid = ? and a.id =? ");
		args.add(userInfo.getComId());
		args.add(gdzcId);
		return (Gdzc) this.objectQuery(sql.toString(), args.toArray(), Gdzc.class);
	}
	/**
	 * 固定资产附件
	 * @param gdzcId
	 * @param comId
	 * @return
	 */
	public List<GdzcUpfile> listGdzcUpfile(Integer gdzcId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n ");
		//车辆附件
		sql.append("\n select b.filename fileName,b.uuid,b.fileExt,c.userName creatorName,c.gender,d.uuid userUuid,d.filename userFileName");
		sql.append("\n ,a.* ");
		sql.append("\n from gdzcUpfile a ");
		sql.append("\n inner join upfiles b on a.comId = b.comId and a.upfileId = b.id");
		sql.append("\n left join userinfo c on  a.userid = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n where a.comid = ? and a.busId = ? and a.busType = ?");
		args.add(comId);
		args.add(gdzcId);
		args.add(ConstantInterface.TYPE_GDZC);
		//减少附件
		sql.append("union all  \n");
		sql.append("\n select b.filename fileName,b.uuid,b.fileExt,c.userName creatorName,c.gender,d.uuid userUuid,d.filename userFileName");
		sql.append("\n ,a.* ");
		sql.append("\n from gdzcUpfile a ");
		sql.append("\n inner join gdzcReduceRecord i on a.comId = i.comId and a.busId = i.id");
		sql.append("\n inner join gdzc gdzc on gdzc.comId = i.comId and gdzc.id = i.gdzcId");
		sql.append("\n inner join upfiles b on a.comId = b.comId and a.upfileId = b.id");
		sql.append("\n left join userinfo c on  a.userid = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n where a.comid = ? and gdzc.id = ? and a.busType = ?");
		args.add(comId);
		args.add(gdzcId);
		args.add(ConstantInterface.TYPE_GDZC_REDUCE);
		//维修记录附件
		sql.append("union all  \n");
		sql.append("\n select b.filename fileName,b.uuid,b.fileExt,c.userName creatorName,c.gender,d.uuid userUuid,d.filename userFileName");
		sql.append("\n ,a.* ");
		sql.append("\n from gdzcUpfile a ");
		sql.append("\n inner join gdzcMaintainRecord i on a.comId = i.comId and a.busId = i.id");
		sql.append("\n inner join gdzc gdzc on gdzc.comId = i.comId and gdzc.id = i.gdzcId");
		sql.append("\n inner join upfiles b on a.comId = b.comId and a.upfileId = b.id");
		sql.append("\n left join userinfo c on  a.userid = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n where a.comid = ? and gdzc.id = ? and a.busType = ?");
		args.add(comId);
		args.add(gdzcId);
		args.add(ConstantInterface.TYPE_GDZC_MAINTAIN);
		return this.listQuery(sql.toString(), args.toArray(), GdzcUpfile.class);
	}
	/**
	 * 固定资产维修记录
	 * @param gdzcId
	 * @param userInfo
	 * @return
	 */
	public List<GdzcMaintainRecord> listMaintainRecord(Integer gdzcId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,c.userName executorName,c.gender,d.uuid userUuid,d.filename userFileName,e.typeName maintainTypeName from gdzcMaintainRecord a");
		sql.append("\n left join userinfo c on  a.executor = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n left join gdzcType e on e.comid = a.comid and e.id = a.maintainType ");
		sql.append("\n where a.comid = ? and a.gdzcId =? ");
		args.add(userInfo.getComId());
		args.add(gdzcId);
		return pagedQuery(sql.toString(), " a.recordCreateTime desc", args.toArray(),GdzcMaintainRecord.class);
	}
	/**
	 * 固定资产维修基本信息
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public GdzcMaintainRecord queryMaintainRecordById(Integer id, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,c.userName executorName,c.gender,d.uuid userUuid,d.filename userFileName,e.typeName maintainTypeName from GdzcMaintainRecord a");
		sql.append("\n left join userinfo c on  a.executor = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n left join gdzcType e on e.comid = a.comid and e.id = a.maintainType ");
		sql.append("\n where a.comid = ? and a.id =? ");
		args.add(userInfo.getComId());
		args.add(id);
		return (GdzcMaintainRecord) this.objectQuery(sql.toString(), args.toArray(), GdzcMaintainRecord.class);
	}
	/**
	 * 查询最近一次维修记录
	 * @param gdzcId
	 * @return
	 */
	public GdzcMaintainRecord queryLastRecord(Integer gdzcId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from (");
		sql.append("\n select a.* from GdzcMaintainRecord a");
		sql.append("\n where a.gdzcId = ? order by a.recordCreateTime desc");
		args.add(gdzcId);
		sql.append("\n )a where rownum  = 1");
		return (GdzcMaintainRecord) this.objectQuery(sql.toString(), args.toArray(), GdzcMaintainRecord.class);
	}
	/**
	 * 减少记录列表
	 * @param gdzcId
	 * @param userInfo
	 * @return
	 */
	public List<GdzcReduceRecord> listGdzcReduceRecord(Integer gdzcId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,c.userName executorName,c.gender,d.uuid userUuid,d.filename userFileName,e.typeName reduceTypeName from GdzcReduceRecord a");
		sql.append("\n left join userinfo c on  a.reduceUser = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n left join gdzcType e on e.comid = a.comid and e.id = a.reduceType ");
		sql.append("\n where a.comid = ? and a.gdzcId =? ");
		args.add(userInfo.getComId());
		args.add(gdzcId);
		return pagedQuery(sql.toString(), " a.recordCreateTime desc", args.toArray(),GdzcReduceRecord.class);
	}
	/**
	 * 通过id查询记录详情
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public GdzcReduceRecord queryReduceRecordById(Integer id, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,c.userName executorName,c.gender,d.uuid userUuid,d.filename userFileName,e.typeName reduceTypeName from GdzcReduceRecord a");
		sql.append("\n left join userinfo c on  a.reduceUser = c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id ");
		sql.append("\n left join gdzcType e on e.comid = a.comid and e.id = a.reduceType ");
		sql.append("\n where a.comid = ? and a.id =? ");
		args.add(userInfo.getComId());
		args.add(id);
		return (GdzcReduceRecord) this.objectQuery(sql.toString(), args.toArray(), GdzcReduceRecord.class);
	}

}
