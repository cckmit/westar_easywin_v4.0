package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.AnnounUpfile;
import com.westar.base.model.Announcement;
import com.westar.base.model.Department;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class AnnouncementDao extends BaseDao {
	/********************************公告****************************************/
	/**
	 * 分页查看公告
	 * @param announ
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Announcement> listPagedAnnoun(Announcement announ,UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from (");
		sql.append("select a.comId,a.id,a.title,a.recordcreatetime,a.type,a.grade,a.creator,a.readTime,b.userName creatorName,b.gender creatorGender,d.uuid creatorUUID,d.filename creatorFileName, ");
		
		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where  today.comId = ? and a.id = today.busid \n");
		sql.append(" and today.bustype='"+ConstantInterface.TYPE_ANNOUNCEMENT+"' and today.userId= ? and today.isclock=0\n");
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 1 else 0 end as isread,\n");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		
		sql.append("\n case when atten.id is null then 0 else 1 end as attentionState");
		
		sql.append("\n from announcement a ");
		sql.append("\n inner join userinfo b on a.creator=b.id");
		sql.append("\n inner join userOrganic c on b.id =c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on c.mediumHeadPortrait = d.id");
		sql.append("\n left join attention atten on ? = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ANNOUNCEMENT+"' and atten.userId=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		sql.append("\n where 1=1  and a.delState <>1 ");


		sql.append("\n and a.id in(");
		//公告人员范围
		sql.append("\n  select announId from announscopebyuser where userid= ? and  comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//部门范围
		sql.append("\n  union ");
		sql.append("\n  select announId from  announscopebydep  a");
		sql.append("\n 	inner join userorganic cc  on  cc.depId = a.depId ");
		sql.append("\n where cc.userid = ? and  a.comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//创建范围
		sql.append("\n union ");
		sql.append("\n select id as announId from announcement where creator = ?  and comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//下属创建范围
		sql.append("\n union ");
		sql.append("\n select a.id as announId from announcement a where exists (");
		sql.append("\n select id from myLeaders where creator = a.creator and leader = ? and comId = ? and creator <> leader ");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		sql.append("\n )");
		//未设置范围的
		sql.append("\n union ");
		sql.append("\n select id as announId from announcement where scopeState = 0 and comId= ? ");
		args.add(userInfo.getComId());
		sql.append("\n )");

		//查询创建时间段
		this.addSqlWhere(announ.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(announ.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//关注
		if(announ.getAttentionState() != null && announ.getAttentionState().equals("1")){
			sql.append("\n 			and exists (");
			sql.append("\n 				select id from attention atten where atten.comId = a.comId and atten.busid = a.id and atten.bustype='"+ConstantInterface.TYPE_ANNOUNCEMENT+"' and atten.userId=?");
			args.add(userInfo.getId());
			sql.append("			)");
		}
		//创建人
		if(announ.getCreator() != null && announ.getCreator() >0){
			sql.append("\n and a.creator = ?");
			args.add(announ.getCreator());
		}

		if(null != announ.getListCreator() && !announ.getListCreator().isEmpty()){
			sql.append("	and  ( a.creator= 0 ");
			for(UserInfo creator : announ.getListCreator()){
				sql.append("or a.creator=?  \n");
				args.add(creator.getId());
			}
			sql.append("	 ) ");
		}

		//紧急度筛选
		if(null!=announ.getGrade() && !"".equals(announ.getGrade())){
			sql.append("\n and a.grade=? \n");
			args.add(announ.getGrade());
		}
		//标题筛选
		if(null!=announ.getTitle() && !"".equals(announ.getTitle() )){
			this.addSqlWhereLike(announ.getTitle() , sql, args, " and a.title like ? \n");
		}
		//标题筛选
		if(null!=announ.getType() && !"".equals(announ.getType())){
			sql.append("\n and a.type=? \n");
			args.add(announ.getType());
		}
		sql.append(")a  where 1=1 ");
		//排序规则
		String orderBy = "";
		if("grade".equals(announ.getOrderBy())){
			//按紧急程度排序
			orderBy = " a.grade desc,recordcreatetime desc ";
		}else if("creator".equals(announ.getOrderBy())){
			//按创建人排序
			orderBy = " a.creator,a.grade desc,a.id desc";
		}else if("crTimeNewest".equals(announ.getOrderBy())){
			//按创建时间(最新)排序
			orderBy = " a.recordcreatetime desc";
		}else if("crTimeOldest".equals(announ.getOrderBy())){
			//按创建时间(最早)排序
			orderBy = " a.recordcreatetime";
		}else{
			//默认未读排序规则
			orderBy = "a.isread asc, a.recordcreatetime desc";
		}
		return this.pagedQuery(sql.toString(),orderBy, args.toArray(), Announcement.class);
	}
	/**
	 * 获取权限下前N条数据
	 * @param userInfo
	 * @param num
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Announcement> firstNAnnoun(UserInfo userInfo,Integer num){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select * from (");
		sql.append("select a.id,a.title,a.recordcreatetime,a.type,a.grade,a.creator,a.readTime,b.userName creatorName,b.gender creatorGender,d.uuid creatorUUID,d.filename creatorFileName, ");

		sql.append("case when(\n");
		sql.append("select count(*) from \n");
		sql.append(" todayworks today where ? = today.comId and a.id = today.busid \n");
		sql.append(" and today.bustype='"+ConstantInterface.TYPE_ANNOUNCEMENT+"' and today.userId= ? and today.isclock=0\n");
		sql.append("and today.readState=0\n");
		sql.append(")=0 then 1 else 0 end as isread,\n");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());

		sql.append("\n case when atten.id is null then 0 else 1 end as attentionState");

		sql.append("\n from announcement a ");
		sql.append("\n inner join userinfo b on a.creator=b.id");
		sql.append("\n inner join userOrganic c on b.id =c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on c.mediumHeadPortrait = d.id");
		sql.append("\n left join attention atten on ? = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ANNOUNCEMENT+"' and atten.userId=?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		sql.append("\n where 1=1  and a.delState <>1");

		sql.append("\n and a.id in(");

		//公告人员范围
		sql.append("\n  select announId from announscopebyuser where userid= ? and  comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//部门范围
		sql.append("\n  union ");
		sql.append("\n  select announId from  announscopebydep  a");
		sql.append("\n 	inner join userorganic cc  on  cc.depId = a.depId ");
		sql.append("\n where cc.userid = ? and  a.comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//创建范围
		sql.append("\n union ");
		sql.append("\n select id as announId from announcement where creator = ? and  comId= ?");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		//下属创建范围
		sql.append("\n union ");
		sql.append("\n select a.id as announId from announcement a where exists (");
		sql.append("\n select id from myLeaders where creator = a.creator and leader = ? and comId = ? and creator <> leader ");
		args.add(userInfo.getId());
		args.add(userInfo.getComId());
		sql.append("\n )");
		//未设置范围的
		sql.append("\n union ");
		sql.append("\n select id as announId from announcement where scopeState = 0  and  comId= ?");
		args.add(userInfo.getComId());
		sql.append("\n )");

		sql.append(" order by isread asc,a.recordcreatetime desc )a where rownum <= ? ");
		args.add(num);
		return this.listQuery(sql.toString(), args.toArray(), Announcement.class);
	}
	/**
	 * 获取公告的基本信息
	 * @param announId
	 * @param userId
	 * @return
	 */
	public Announcement getAnnouncementInfo(Integer announId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,o.orgName,b.userName creatorName,b.gender creatorGender,d.uuid creatorUUID,d.filename creatorFileName ,");
		sql.append("\n case when atten.id is null then 0 else 1 end as attentionState");
		sql.append("\n from announcement a ");
		sql.append("\n inner join userinfo b on a.creator=b.id");
		sql.append("\n inner join userOrganic c on b.id =c.userId and a.comId=c.comId");
		sql.append("\n inner join Organic o on a.comId = o.orgNum");
		sql.append("\n left join upfiles d on c.mediumHeadPortrait = d.id");
		sql.append("\n left join attention atten on a.comId = atten.comId and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_ANNOUNCEMENT+"' and atten.userId=?");
		args.add(userId);
		sql.append("\n where 1=1");
		sql.append("and a.id =? ");
		args.add(announId);
		return (Announcement) this.objectQuery(sql.toString(), args.toArray(), Announcement.class);
	}
	/**
	 * 权限验证完善
	 * @param comId
	 * @param announId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Announcement> authorCheck(Integer comId, Integer announId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id from announcement a ");
		sql.append("\n where 1=1");
		sql.append("\n and a.id =? ");
		args.add(announId);
		sql.append("\n and a.id in(");
		//公告人员范围
		sql.append("\n  select announId from announscopebyuser where userid= ? and comId= ?");
		args.add(userId);
		args.add(comId);
		//部门范围
		sql.append("\n  union all");
		sql.append("\n  select announId from  announscopebydep  a");
		sql.append("\n 	inner join userorganic cc  on  cc.depId = a.depId ");
		sql.append("\n where cc.userid = ? and a.comId= ?");
		args.add(userId);
		args.add(comId);
		//创建范围
		sql.append("\n union all ");
		sql.append("\n select id as announId from announcement where creator = ?");
		args.add(userId);
		//创建范围
		sql.append("\n union ");
		sql.append("\n select a.id as announId from announcement a where exists (");
		sql.append("\n select id from myLeaders where creator = a.creator and leader = ? and comId = ? and creator <> leader ");
		args.add(userId);
		args.add(comId);
		sql.append("\n )");
		//未设置范围的
		sql.append("\n union all ");
		sql.append("\n select id as announId from announcement where scopeState = 0 and comId = ? ");
		args.add(comId);
		sql.append("\n )");
		return this.listQuery(sql.toString(), args.toArray(), Announcement.class);
	}

	/**
	 * 修改标题
	 * @param announ
	 */
	public void updateTitle(Announcement announ){
		StringBuffer sql = new StringBuffer("update announcement a set a.title=:title where a.comId=:comId and a.id=:id");
		this.update(sql.toString(),announ);
	}
	/**
	 * 修改类型
	 * @param announ
	 */
	public void updateType(Announcement announ){
		StringBuffer sql = new StringBuffer("update announcement a set a.type=:type where a.comId=:comId and a.id=:id");
		this.update(sql.toString(),announ);
	}
	/**
	 * 修改重要程度
	 * @param announ
	 */
	public void updateGrade(Announcement announ){
		StringBuffer sql = new StringBuffer("update announcement a set a.grade=:grade where a.comId=:comId and a.id=:id");
		this.update(sql.toString(),announ);
	}
	/**
	 * 修改公告内容
	 * @param announ
	 */
	public void updateAnnounRemark(Announcement announ){
		StringBuffer sql = new StringBuffer("update announcement a set a.announRemark=:announRemark where a.comId=:comId and a.id=:id");
		this.update(sql.toString(),announ);
	}
	/**
	 * 增加阅读次数
	 * @param announ
	 */
	public void updateAnnounRead(Integer announId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("update announcement a set a.readtime = a.readtime+1 where a.id=? ");
		args.add(announId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 修改公告范围状态
	 * @param comId
	 * @param announId
	 * @param state
	 */
	public void updateScopeState(Integer comId,Integer announId,Integer state){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("update announcement a set a.scopeState =? where a.id=? and a.comId = ?");
		args.add(state);
		args.add(announId);
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/********************************公告附件****************************************/
	/**
	 * 公告附件
	 * @param comId
	 * @param Id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnnounUpfile> listAnnounFiles(Integer comId, Integer announId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comId,a.busId,b.filename,b.uuid, a.upfileId,b.fileExt ");
		sql.append("\n  from announUpfile a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(announId, sql, args, " and a.busId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.pagedQuery(sql.toString()," a.recordCreateTime desc", args.toArray(), AnnounUpfile.class);
	}

	/********************************公告范围****************************************/
	/**
	 * 获取公告的公告部门范围
	 * @param comId
	 * @param announId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Department> listAnnumnScopeDep(Integer comId,Integer announId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id, GET_DEPNAME(a.comId,a.id) depName,a.comId  from department a ");
		sql.append("\n inner join announScopeByDep b on a.id = b.depId and a.comId = b.comId");
		sql.append("\n inner join organic c on a.comId = c.orgNum ");
		sql.append("\n where 1=1  ");
		sql.append("\n and b.comId = ? and b.announId =?");
		args.add(comId);
		args.add(announId);
		return this.listQuery(sql.toString(), args.toArray(), Department.class);
	}

	/**
	 * 获取公告的公告人员范围
	 * @param comId
	 * @param announId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listAnnumnScopeUser(Integer comId,Integer announId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.username,cc.comId from userinfo a ");
		sql.append("\n inner join userorganic cc on a.id = cc.userid");
		sql.append("\n inner join announScopeByUser b on cc.userid = b.userid and cc.comId = b.comId");
		sql.append("\n where 1=1  ");
		sql.append("\n and b.comId = ? and b.announId =?");
		args.add(comId);
		args.add(announId);
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 分页查询公告附件信息
	 * @param announId 公告主键
	 * @param comId 团队号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnnounUpfile> listPagedMeetUpfiles(Integer announId,
			Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from (");
		//纪要附件
		sql.append("\n select a.busId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, ");
		sql.append("\n c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, '1' sourceType,a.id sourceId,");
		sql.append("\n case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic ");
		sql.append("\n from announUpfile a inner join upfiles b on a.comId = b.comId and a.upfileid = b.id ");
		sql.append("\n left join userinfo c on  a.userid = c.id \n");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("\n where a.comId =? and a.busId = ? \n");
		args.add(comId);
		args.add(announId);
		//讨论附件
		sql.append("\n union all ");
		sql.append("\n select a.busId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName,");
		sql.append("\n c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext,'0' sourceType,a.id sourceId,");
		sql.append("\n case when b.fileext in ('gif', 'jpg','jpeg', 'png', 'bmp')then 1 else 0 end as isPic");
		sql.append("\n from talkUpfile a inner join upfiles b on a.comId = b.comId and a.upfileId = b.id");
		sql.append("\n left join userinfo c on  a.userid = c.id");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n left join upfiles d on  cc.mediumHeadPortrait = d.id");
		sql.append("\n where a.comId =? and a.busId = ? \n");
		args.add(comId);
		args.add(announId);
		this.addSqlWhere(ConstantInterface.TYPE_ANNOUNCEMENT, sql, args, " and a.busType=?");
		sql.append("\n ) a ");
		return this.pagedQuery(sql.toString(), " a.sourceType,a.sourceId", args.toArray(), AnnounUpfile.class);
	}
	

}
