package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Institution;
import com.westar.base.model.Department;
import com.westar.base.model.InstituType;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class InstitutionDao extends BaseDao {
	/********************************制度****************************************/
	/**
	 * 分页查看制度
	 * @param institution
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Institution> listPagedInstitu(Institution institution,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("select a.id,a.comId,a.title,a.recordcreatetime,a.instituType,i.typeName,a.creator,v.readTime,b.userName creatorName,b.gender creatorGender,d.uuid creatorUUID,d.filename creatorFileName, ");
		
		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where  today.comId = ? and a.id = today.busid \n");
		sql.append(" and today.bustype='"+ConstantInterface.TYPE_INSTITUTION+"' and today.userId= ? and today.isclock=0\n");
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 1 else 0 end as isread,\n");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		
		sql.append("\n case when atten.id is null then 0 else 1 end as attentionState");
		
		sql.append("\n from institution a ");
		sql.append("\n inner join instituType i on i.comid = a.comid and i.id = a.instituType");
		sql.append("\n inner join userinfo b on a.creator=b.id");
		sql.append("\n inner join userOrganic c on b.id =c.userId and a.comid=c.comId");
		sql.append("\n left join upfiles d on c.mediumHeadPortrait = d.id");
		sql.append("\n left join attention atten on ? = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_INSTITUTION+"' and atten.userId=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n left join (select v.busId,count(v.userId) readTime");
		sql.append("\n  from viewRecord v where busType = ? and comId = ? group by v.busId) v");
		args.add(ConstantInterface.TYPE_INSTITUTION);
		args.add(userInfo.getComId());
		sql.append("\n  on a.id = v.busId ");
		sql.append("\n where 1=1  and a.delState <>1 ");
		
		
		sql.append("\n and a.id in(");
		//制度人员范围
		sql.append("\n  select instituId from instituscopebyuser where userid= ? and  comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//部门范围
		sql.append("\n  union ");
		sql.append("\n  select instituId from  instituscopebydep  a");
		sql.append("\n 	inner join userorganic cc  on  cc.depId = a.depId ");
		sql.append("\n where cc.userid = ? and  a.comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//创建范围
		sql.append("\n union ");
		sql.append("\n select id as instituId from institution where creator = ?  and comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//下属创建范围
		sql.append("\n union ");
		sql.append("\n select a.id as instituId from institution a where exists (");
		sql.append("\n select id from myLeaders where creator = a.creator and leader = ? and comId = ? and creator <> leader ");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		sql.append("\n  ) ");
		//未设置范围的
		sql.append("\n union ");
		sql.append("\n select id as instituId from institution where scopeState = 0 and comId= ? ");
		args.add(userInfo.getComId());
		sql.append("\n )");
		
		//查询创建时间段
		this.addSqlWhere(institution.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(institution.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//关注
		if(institution.getAttentionState() != null && institution.getAttentionState().equals("1")){
			sql.append("\n 			and exists (");
			sql.append("\n 				select id from attention atten where atten.comid = a.comid and atten.busid = a.id and atten.bustype='"+ConstantInterface.TYPE_INSTITUTION+"' and atten.userId=?");
			args.add(userInfo.getId());
			sql.append("			)");
		}
		//创建人
		if(institution.getCreator() != null && institution.getCreator() >0){
			sql.append("\n and a.creator = ?");
			args.add(institution.getCreator());
		}
		
		if(null != institution.getListCreator() && !institution.getListCreator().isEmpty()){
			sql.append("	and  ( a.creator= 0 ");
			for(UserInfo creator : institution.getListCreator()){
				sql.append("or a.creator=?  \n");
				args.add(creator.getId());
			}
			sql.append("	 ) ");
		}
		
		//标题筛选
		if(null!=institution.getTitle() && !"".equals(institution.getTitle() )){
			this.addSqlWhereLike(institution.getTitle() , sql, args, " and a.title like ? \n");
		}
		//类型筛选
		if(null != institution.getInstituType() && !"".equals(institution.getInstituType())){
			sql.append("\n and a.instituType = ?");
			args.add(institution.getInstituType());
		}
		sql.append(")a  where 1=1 ");
		//排序规则
		String orderBy = "";
		 if("creator".equals(institution.getOrderBy())){
			//按创建人排序
			orderBy = " a.creator,a.grade desc,a.id desc";
		}else if("crTimeNewest".equals(institution.getOrderBy())){
			//按创建时间(最新)排序
			orderBy = " a.recordcreatetime desc";
		}else if("crTimeOldest".equals(institution.getOrderBy())){
			//按创建时间(最早)排序
			orderBy = " a.recordcreatetime";
		}else{
			//默认未读排序规则
			orderBy = "a.isread asc, a.recordcreatetime desc";
		}
		return this.pagedQuery(sql.toString(),orderBy , args.toArray(), Institution.class);
	}
	/**
	 * 获取权限下前N条数据
	 * @param userInfo
	 * @param num
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Institution> firstNInstitu(UserInfo userInfo,Integer num){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from (");
		sql.append("select a.id,a.title,a.recordcreatetime,a.instituType,i.typeName,a.creator,a.readTime,b.userName creatorName,b.gender creatorGender,d.uuid creatorUUID,d.filename creatorFileName, ");
		
		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where  today.comId = ? and a.id = today.busid \n");
		sql.append(" and today.bustype='"+ConstantInterface.TYPE_INSTITUTION+"' and today.userId= ? and today.isclock=0\n");
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 1 else 0 end as isread,\n");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		
		sql.append("\n case when atten.id is null then 0 else 1 end as attentionState");
		
		sql.append("\n from institution a ");
		sql.append("\n inner join instituType i on i.comid = a.comid and i.id = a.instituType");
		sql.append("\n inner join userinfo b on a.creator=b.id");
		sql.append("\n inner join userOrganic c on b.id =c.userId and a.comid=c.comId");
		sql.append("\n left join upfiles d on c.mediumHeadPortrait = d.id");
		sql.append("\n left join attention atten on ? = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_INSTITUTION+"' and atten.userId=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n where 1=1  and a.delState <>1 ");
		
		
		sql.append("\n and a.id in(");
		//制度人员范围
		sql.append("\n  select instituId from instituscopebyuser where userid= ? and  comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//部门范围
		sql.append("\n  union ");
		sql.append("\n  select instituId from  instituscopebydep  a");
		sql.append("\n 	inner join userorganic cc  on  cc.depId = a.depId ");
		sql.append("\n where cc.userid = ? and  a.comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//创建范围
		sql.append("\n union ");
		sql.append("\n select id as instituId from institution where creator = ?  and comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//下属创建范围
		sql.append("\n union ");
		sql.append("\n select a.id as instituId from institution a where exists(");
		sql.append("\n select id from myLeaders where creator = a.creator and leader = ? and comId = ? and creator <> leader");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		sql.append("\n  ) ");
		args.add(userInfo.getComId());
		//未设置范围的
		sql.append("\n union ");
		sql.append("\n select id as instituId from institution where scopeState = 0 and comId= ? ");
		args.add(userInfo.getComId());
		sql.append("\n )");
		sql.append(" order by isread asc,a.recordcreatetime desc )a where rownum <= ? ");
		args.add(num);
		return this.listQuery(sql.toString(), args.toArray(), Institution.class);
	}
	/**
	 * 获取制度的基本信息
	 * @param institutionId
	 * @param userId
	 * @return
	 */
	public Institution getInstitutionInfo(Integer institutionId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,i.typeName,o.orgName,b.userName creatorName,b.gender creatorGender,d.uuid creatorUUID,d.filename creatorFileName ,");
		sql.append("\n case when atten.id is null then 0 else 1 end as attentionState");
		sql.append("\n from institution a ");
		sql.append("\n inner join instituType i on i.comid = a.comid and i.id = a.instituType");
		sql.append("\n inner join userinfo b on a.creator=b.id");
		sql.append("\n inner join userOrganic c on b.id =c.userId and a.comId=c.comId");
		sql.append("\n inner join Organic o on a.comId = o.orgNum");
		sql.append("\n left join upfiles d on c.mediumHeadPortrait = d.id");
		sql.append("\n left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ANNOUNCEMENT+"' and atten.userId=?");
		args.add(userId);
		sql.append("\n where 1=1");
		sql.append("and a.id =? ");
		args.add(institutionId);
		return (Institution) this.objectQuery(sql.toString(), args.toArray(), Institution.class);
	}
	/**
	 * 权限验证完善
	 * @param comId
	 * @param institutionId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Institution> authorCheck(Integer comId, Integer institutionId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id from institution a ");
		sql.append("\n where 1=1");
		sql.append("\n and a.id =? ");
		args.add(institutionId);
		sql.append("\n and a.id in(");
		//制度人员范围
		sql.append("\n  select instituId from instituscopebyuser where userid= ? and comid= ?");
		args.add(userId);
		args.add(comId);
		//部门范围
		sql.append("\n  union all");
		sql.append("\n  select instituId from  instituscopebydep  a");
		sql.append("\n 	inner join userorganic cc  on  cc.depId = a.depId ");
		sql.append("\n where cc.userid = ? and a.comid= ?");
		args.add(userId);
		args.add(comId);
		//创建范围
		sql.append("\n union all ");
		sql.append("\n select id as institutionId from institution where creator = ?");
		args.add(userId);
		sql.append("\n union ");
		sql.append("\n select id as institutionId from institution where exists (");
		sql.append("\n  select id from myLeaders where comId=? and leader = ? and creator = institution.creator ");
		args.add(comId);
		args.add(userId);
		sql.append("\n )");
		//未设置范围的
		sql.append("\n union all ");
		sql.append("\n select id as institutionId from institution where scopeState = 0 and comId = ? ");
		args.add(comId);
		sql.append("\n )");
		return this.listQuery(sql.toString(), args.toArray(), Institution.class);
	}

	/**
	 * 修改标题
	 * @param institution
	 */
	public void updateTitle(Institution institution){
		StringBuffer sql = new StringBuffer("update institution a set a.title=:title where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),institution);
	}
	/**
	 * 修改类型
	 * @param institution
	 */
	public void updateType(Institution institution){
		StringBuffer sql = new StringBuffer("update institution a set a.instituType=:instituType where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),institution);
	}
	/**
	 * 修改制度内容
	 * @param institution
	 */
	public void updateInstituRemark(Institution institution){
		StringBuffer sql = new StringBuffer("update institution a set a.instituRemark=:instituRemark where a.comid=:comId and a.id=:id");
		this.update(sql.toString(),institution);
	}
	/**
	 * 增加阅读次数
	 * @param institutionId
	 */
	public void updateInstituRead(Integer institutionId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("update institution a set a.readtime = a.readtime+1 where a.id=? ");
		args.add(institutionId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 修改制度范围状态
	 * @param comId
	 * @param institutionId
	 * @param state
	 */
	public void updateScopeState(Integer comId,Integer institutionId,Integer state){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("update institution a set a.scopeState =? where a.id=? and a.comid = ?");
		args.add(state);
		args.add(institutionId);
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/********************************制度范围****************************************/
	/**
	 * 获取制度的制度部门范围
	 * @param comId
	 * @param institutionId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Department> listInstituScopeDep(Integer comId,Integer institutionId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id, GET_DEPNAME(a.comid,a.id) depName,a.comId  from department a ");
		sql.append("\n inner join instituScopeByDep b on a.id = b.depId and a.comid = b.comid");
		sql.append("\n inner join organic c on a.comId = c.orgNum ");
		sql.append("\n where 1=1  ");
		sql.append("\n and b.comid = ? and b.instituId =?");
		args.add(comId);
		args.add(institutionId);
		return this.listQuery(sql.toString(), args.toArray(), Department.class);
	}
	
	/**
	 * 获取制度的制度人员范围
	 * @param comId
	 * @param institutionId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listInstituScopeUser(Integer comId,Integer institutionId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.username,cc.comId from userinfo a ");
		sql.append("\n inner join userorganic cc on a.id = cc.userid");
		sql.append("\n inner join instituScopeByUser b on cc.userid = b.userid and cc.comid = b.comid");
		sql.append("\n where 1=1  ");
		sql.append("\n and b.comid = ? and b.instituId =?");
		args.add(comId);
		args.add(institutionId);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/***************************** 以下是制度类型管理 *****************************************/
	/**
	 * 获取制度类型集合
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InstituType> listInstituType(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from instituType a where a.comId=? ");
		args.add(comId);
		sql.append(" order by a.orderNum asc");
		return this.listQuery(sql.toString(), args.toArray(),InstituType.class);
	}
	/**
	 * 获取制度类型最大序号
	 * @param comId
	 * @return
	 */
	public InstituType queryInstituTypeOrderMax(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.orderNum)+1  as orderNum from instituType a where a.comid =?");
		args.add(comId);
		return (InstituType)this.objectQuery(sql.toString(), args.toArray(),InstituType.class);
	}
	/**
	 * 查询处于某类型的制度数量
	 * @param instituTypeId
	 * @param userInfo
	 * @return
	 */
	public Integer countInstituByInstituType(Integer instituTypeId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from institution where instituType = ? and comId = ?");
		args.add(instituTypeId);
		args.add(userInfo.getComId());
		return this.countQuery(sql.toString(), args.toArray());
	}

}
