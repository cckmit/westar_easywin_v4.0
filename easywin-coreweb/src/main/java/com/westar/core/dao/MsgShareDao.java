package com.westar.core.dao;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.westar.base.model.ShareGroup;
import com.westar.base.model.ShareUser;
import org.springframework.stereotype.Repository;

import com.westar.base.model.MsgShare;
import com.westar.base.model.MsgShareLog;
import com.westar.base.model.MsgShareTalk;
import com.westar.base.model.MsgShareTalkUpfile;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WeekRepTalk;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.StringUtil;

@Repository
public class MsgShareDao extends BaseDao {
	
	/**
	 * 获取分享信息集合
	 * @param userId 登录人
	 * @param pageSize 信息显示数量
	 * @param minId 页码
	 * @param modList
	 * @param minId 
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShare> listMsgShare(Integer comId,Integer userId,Integer pageSize,
									    MsgShare msgShare, List<String> modList, Integer minId) throws ParseException{
		List<Object> args = new ArrayList<Object>();

		//创建人类型
		String creatorType = msgShare.getCreatorType();


		StringBuffer sql = new StringBuffer("");
		sql.append("select x.*,b.username as creatorName,substr(x.recordcreatetime,0,16) as createDate, \n");
		sql.append(" case when atten.id is null then 0 else 1 end attentionState,\n");
		sql.append("(select count(id) from msgShareTalk where msgId=x.id and traceType=0) totalTalks\n");
		sql.append("from ( \n");

		//所有模块
		if(null==modList
				//是信息分享
				||modList.size()==0
//				||modList.indexOf("100")>=0//是信息分享
//				||modList.indexOf("1")>=0//是信息分享
				||modList.indexOf(ConstantInterface.TYPE_DAILY)>=0
				//是投票分享
				||modList.indexOf(ConstantInterface.TYPE_VOTE)>=0
				//是文档分享
				||modList.indexOf(ConstantInterface.TYPE_FILE)>=0
				//是问答分享
				||modList.indexOf(ConstantInterface.TYPE_QUES)>=0
				){
			sql.append("select b.*,0 isDel, \n");
			sql.append("case when b.type=004 and vote.delState=0 then 0  \n");
			sql.append("when b.type=011 and qas.delState=0 then 0   \n");
			sql.append(" when ( b.type<>'011' and b.type<>'004' ) then 0 else 1 end delState \n");
			sql.append("from ( \n");

			//分享范围的
			sql.append(" select a.id,0 isDel from msgShare a where 1=1 \n");
			sql.append(" and exists( \n");
			sql.append(" select id from shareUser where a.id=shareUser.msgId and a.comId=shareUser.comId\n");
			sql.append(" )\n");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and a.comId=? ");
			if(null!=modList && modList.size()>0){
				this.addSqlWhereIn(modList.toArray(), sql, args, "  and a.type in ?");
			}

			sql.append("union \n");
			//自己
			sql.append("select a.id,0 isDel from msgShare a where a.scopetype=2 and a.traceType=0\n");
			this.addSqlWhere(userId, sql, args, " and a.creator=? ");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and a.comId=? ");
			if(null!=modList && modList.size()>0){
				this.addSqlWhereIn(modList.toArray(), sql, args, "  and a.type in ?");
			}
			sql.append("union \n");
			//所有人
			sql.append("select a.id,0 isDel from msgShare a where a.scopetype=0 and a.traceType=0\n");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and a.comId=?");
			if(null!=modList && modList.size()>0){
				this.addSqlWhereIn(modList.toArray(), sql, args, "  and a.type in ?");
			}
			sql.append("union \n");
			//指定范围
			sql.append("select a.id,0 isDel from msgShare a where a.scopetype=1 and a.traceType=0\n");
			this.addSqlWhere(userId, sql, args, " and a.creator=? ");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and a.comId=? ");
			if(null!=modList && modList.size()>0){
				this.addSqlWhereIn(modList.toArray(), sql, args, "  and a.type in ?");
			}
			sql.append("union \n");
			//指定范围（不一定有自己）
			sql.append("select a.id,0 isDel from msgShare a where a.scopetype=1 and a.traceType=0\n");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and a.comId=? ");
			this.addSqlWhere(userId, sql, args, " and creator<>? ");
			sql.append("and exists(select b.msgId from sharegroup b inner join  grouppersion c on b.grpid=c.grpid and b.comId=c.comId \n");
			sql.append("where a.id=b.msgid and a.comId=b.comId \n");
			this.addSqlWhere(userId, sql, args, " and c.userinfoid=? ");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and c.comId=? ");
			if(null!=modList && modList.size()>0){
				this.addSqlWhereIn(modList.toArray(), sql, args, "  and a.type in ?");
			}
			sql.append(")\n");

			sql.append("\n union \n");
			//分享数据
			sql.append("\n select b.id,0 isDel from (" );
			sql.append("\n select b.comId,case when b.id is null then 0 else b.id end id");
			sql.append("\n  from (");
			sql.append("\n    select uorg.comId,uorg.userId,daily.dailyName,daily.hasPlan,daily.id,daily.recordcreatetime,");
			sql.append("\n    case when daily.state is null then '1' else daily.state end state,");
			sql.append("\n    case when daily.scopetype is null then 2 else daily.scopetype end scopetype,");
			sql.append("\n    daily.dailydate");
			sql.append("\n    from userorganic uorg");
			sql.append("\n    inner join daily on uorg.comId=daily.comId and uorg.userid=daily.reporterid");
			sql.append("\n    left join dailyShareGroup dailyGrp on daily.comId=dailyGrp.Comid and daily.id=dailyGrp.Dailyid");
			sql.append("\n    left join grouppersion grpUser on dailyGrp.Comid=grpUser.Comid and dailyGrp.Grpid = grpUser.Grpid and grpUser.Userinfoid=?");
			args.add(userId);
			sql.append("\n    where uorg.comId=? and uorg.enabled=1");
			args.add(comId);
			sql.append("\n    and(  ");
			sql.append("exists(select id from myLeaders where creator = uorg.userId and comId = ? and leader = ? and leader <> creator and leader <> creator )");
			args.add(comId);
			args.add(userId);
			sql.append("\n      or uorg.userid=? ");
			args.add(userId);
			sql.append("\n      or exists (select dailyId from dailyShareUser where comId = ? and userid = ? and daily.id = dailyId)");
			args.add(comId);
			args.add(userId);
			sql.append("\n      or daily.scopeType = 0 ");
			sql.append("\n      or (daily.scopeType = 1 and grpUser.Userinfoid=?) ");
			args.add(userId);
			sql.append("\n    ) ");
			sql.append("\n  )b");
			sql.append("\n  where 1=1  ");
			sql.append("\n  and b.id <> 0");
			sql.append("\n ) a inner join msgshare b on b.modid = a.id and a.comId = b.comId and b.type = " + ConstantInterface.TYPE_DAILY + " \n");

			sql.append(")bb inner join msgShare b on bb.id=b.id \n");
			sql.append(" left join vote on vote.comId=b.comId and b.modid=vote.id and b.type=004  \n");
			sql.append("left join question qas on qas.comId=b.comId and b.modid=qas.id and b.type=011  \n");
			sql.append("where 1=1 \n");
			sql.append("and b.type <> 1 \n");
			if(!StringUtil.isBlank(creatorType)){
				if("0".equals(creatorType)){
					this.addSqlWhere(userId, sql, args, " and b.creator=? ");
				}else if("1".equals(creatorType)){
					sql.append("and exists(\n");
					sql.append("	select id from myLeaders where creator = b.creator and leader = ? and comId = ? and creator <> leader \n");
					args.add(userId);
					args.add(msgShare.getComId());
					sql.append(")\n");
				}
			}
		}

		sql.append(") x inner join userInfo b on x.creator=b.id  \n");
		sql.append(" left join attention atten on x.comId=atten.comId and x.modId=atten.busid \n");
		sql.append(" and x.type=atten.bustype and atten.userid= "+userId+"\n");
		sql.append(" where 1=1 and x.delState=0\n");

		//指定了消息创建人员
		this.addSqlWhere(msgShare.getCreator(), sql, args, " and x.creator=?");
		
		//查询创建时间段
		this.addSqlWhere(msgShare.getStartDate(), sql, args, " and substr(x.recordcreatetime,0,10)>=?");
		this.addSqlWhere(msgShare.getEndDate(), sql, args, " and substr(x.recordcreatetime,0,10)<=?");
		if(!CommonUtil.isNull(minId) && minId != -1) {
			this.addSqlWhere(minId, sql, args, " and x.id <? ");
		}
		this.addSqlWhere(msgShare.getContent(), sql, args, " and instr(x.content,?,1)>0 ");
		return this.pagedQuery(sql.toString(), " x.id desc", args.toArray(),MsgShare.class);
	}
	/**
	 * 个人分享信息数
	 * @param comId 公司编号
	 * @param userId 用户主键
	 * @param msgShare
	 * @param modList
	 * @return
	 */
	public Integer getCountMsg(Integer comId,Integer userId, MsgShare msgShare, List<String> modList){

		List<Object> args = new ArrayList<Object>();

		//创建人类型
		String creatorType = msgShare.getCreatorType();

		StringBuffer sql = new StringBuffer("select count(*) from ( \n");

		if(null==modList//所有模块
				||modList.size()==0//是信息分享
//				||modList.indexOf("100")>=0//是信息分享
//				||modList.indexOf("1")>=0//是信息分享
				||modList.indexOf("050")>=0//是分享
				||modList.indexOf("080")>=0//是产品
				||modList.indexOf(ConstantInterface.TYPE_VOTE)>=0//是投票分享
				||modList.indexOf(ConstantInterface.TYPE_FILE)>=0//是文档分享
				||modList.indexOf(ConstantInterface.TYPE_QUES)>=0//是问答分享
					){
			sql.append(" select * from ( \n");
			//自己
			sql.append("select a.id,0 isDel,a.creator,a.content from msgShare a where a.scopetype=2 and a.traceType=0\n");
			this.addSqlWhere(userId, sql, args, " and a.creator=? ");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and a.comId=? ");
			if(null!=modList){
				this.addSqlWhereIn(modList.toArray(), sql, args, "  and a.type in ?");
			}
			sql.append(" union all \n");
			//所有人
			sql.append(" select a.id,0 isDel,a.creator,a.content from msgShare a where a.scopetype=0 and a.traceType=0\n");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and a.comId=?");
			if(null!=modList && modList.size()>0){
				this.addSqlWhereIn(modList.toArray(), sql, args, "  and a.type in ?");
			}
			sql.append(" union all \n");
			//指定范围
			sql.append("select a.id,0 isDel,a.creator,a.content from msgShare a where a.scopetype=1 and a.traceType=0\n");
			this.addSqlWhere(userId, sql, args, " and a.creator=? ");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and a.comId=? ");
			if(null!=modList && modList.size()>0){
				this.addSqlWhereIn(modList.toArray(), sql, args, "  and a.type in ?");
			}
			sql.append(" union all \n");
			//指定范围（不一定有自己）
			sql.append("select a.id,0 isDel,a.creator,a.content from msgShare a where a.scopetype=1 and a.traceType=0\n");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and a.comId=? ");
			this.addSqlWhere(userId, sql, args, " and creator<>? ");
			sql.append("and exists(select b.msgId from sharegroup b inner join  grouppersion c on b.grpid=c.grpid and b.comId=c.comId \n");
			sql.append("where a.id=b.msgid and a.comId=b.comId \n");
			this.addSqlWhere(userId, sql, args, " and c.userinfoid=? ");
			this.addSqlWhere(msgShare.getComId(), sql, args, " and c.comId=? ");
			if(null!=modList && modList.size()>0){
				this.addSqlWhereIn(modList.toArray(), sql, args, "  and a.type in ?");
			}
			sql.append(")\n");

			sql.append(")b where 1=1 \n");
			if(!StringUtil.isBlank(creatorType)){
				if("0".equals(creatorType)){
					this.addSqlWhere(userId, sql, args, " and b.creator=? ");
				}else if("1".equals(creatorType)){
					sql.append("and exists(\n");
					sql.append("	select id from myLeaders where creator = b.creator and leader = ? and comId = ? creator <> leader \n");
					args.add(userId);
					args.add(msgShare.getComId());
					sql.append(")\n");
				}
			}
		}
		sql.append(") x \n");
		sql.append(" where 1=1\n");

		//指定了消息创建人员
		this.addSqlWhere(msgShare.getCreator(), sql, args, " and x.creator=?");

		//查询创建时间段
		this.addSqlWhere(msgShare.getStartDate(), sql, args, " and substr(x.recordcreatetime,0,10)>=?");
		this.addSqlWhere(msgShare.getEndDate(), sql, args, " and substr(x.recordcreatetime,0,10)<=?");
		this.addSqlWhere(msgShare.getContent(), sql, args, " and instr(x.content,?,1)>0 ");
		return this.countQuery(sql.toString(), args.toArray());

	}
	/**
	 * 根据ID获取分享信息内容
	 * @param comId
	 * @param id
	 * @param type
	 * @return
	 */
	public MsgShare viewMsgShareById(Integer userId,Integer comId,Integer id, String type){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select x.*,c.gender,d.uuid smImgUuid,d.filename smImgName,c.username creatorName \n");
		sql.append("  from ( \n");


		sql.append("select a.*,lead(a.id,1,0) over(order by a.id desc) nextObjId, \n");
		sql.append(" case when atten.id is null then 0 else 1 end attentionState,\n");
		sql.append(" lag(a.id,1,0) over(order by a.id desc) backObjId from (\n");

		//分享范围的
		sql.append(" select msgShare.id from msgShare where 1=1 \n");
		sql.append(" and exists( \n");
		sql.append(" 	 select u.id from GROUPPERSION g \n");
		sql.append(" 	 LEFT JOIN DAILYSHAREGROUP s on s.GRPID = g.GRPID \n");
		sql.append(" 	 LEFT JOIN USERINFO u on u.id = g.USERINFOID where s.dailyId=msgShare.modId \n");
		sql.append(" 	 UNION select u.id from daily w \n");
		sql.append(" 	 left JOIN IMMEDIATESUPER i on i.CREATOR = w.REPORTERID \n");
		sql.append(" 	 LEFT JOIN USERINFO u on u.id = i.leader  where w.id=msgShare.modId \n");
		sql.append(" 	 UNION select s.USERID from daily m right JOIN dailyShareUser s 	on s.dailyId = m.id where m.id=msgShare.modId \n");
		sql.append(" )\n");
		this.addSqlWhere(comId, sql,args ," and comId=? ");
		this.addSqlWhere(type, sql,args , " and type=? ");
		//自己
		sql.append(" union\n");
		sql.append(" select msgShare.id from msgShare where scopetype=2 and traceType=0 \n");

		this.addSqlWhere(userId, sql, args, " and creator=?");
		this.addSqlWhere(comId, sql,args ," and comId=? ");
		this.addSqlWhere(type, sql,args , " and type=? ");
		//所有人
		sql.append(" union\n");
		sql.append(" select msgShare.id from msgShare where scopetype=0 and traceType=0 \n");
		this.addSqlWhere(comId, sql,args ," and comId=? ");
		this.addSqlWhere(type, sql,args , " and type=? ");
		sql.append("  union\n");
		//自己指定分组
		sql.append("  select a.id from msgShare a where a.scopetype=1 and a.traceType=0 \n");
		this.addSqlWhere(userId, sql, args, " and a.creator=?");
		this.addSqlWhere(comId, sql,args ," and a.comId=? ");
		this.addSqlWhere(type, sql,args , " and a.type=? ");
		sql.append("  union all\n");
		//自己指定分组（不一定有自己）
		sql.append("  select a.id from msgShare a where a.scopetype=1 and a.traceType=0 \n");
		this.addSqlWhere(userId, sql, args, " and a.creator<>?");
		this.addSqlWhere(comId, sql,args ," and a.comId=? ");
		this.addSqlWhere(type, sql,args , " and a.type=? ");
		sql.append("  and  exists(\n");
		sql.append("   select b.msgId from sharegroup b inner join  grouppersion c on b.grpid=c.grpid and b.comId=c.comId\n");
		sql.append("   where a.id=b.msgid and a.comId=b.comId \n");
		this.addSqlWhere(userId, sql, args, " and c.userinfoid=?");
		this.addSqlWhere(comId, sql,args ," and c.comId=? ");
		sql.append("  )\n");

		sql.append(")msgShareT  inner join msgShare a on msgShareT.id=a.id\n");
		sql.append("left join attention atten on a.comId=atten.comId and a.modid=atten.busid and atten.bustype=050 \n");
		sql.append("and atten.userId="+userId+"  order by a.id desc\n");
		sql.append(") x  \n");
		sql.append(" left join userorganic b on x.comId=b.comId and x.creator=b.userid \n ");
		sql.append(" left join userinfo c on x.creator=c.id \n");
		sql.append(" left join upfiles d on b.comId=d.comId and b.smallHeadPortrait=d.id \n");
		sql.append("where 1=1 ");
		this.addSqlWhere(id, sql,args , " and x.id=? and rownum <= 1");
		return (MsgShare)this.objectQuery(sql.toString(), args.toArray(), MsgShare.class);
	}
	/**
	 * 根据ID获取分享信息内容
	 * @param comId
	 * @param id
	 * @return
	 */
	public MsgShare getMsgShareById(Integer comId,Integer id){
		StringBuffer sql = new StringBuffer("select a.*,b.username as creatorName,substr(a.recordcreatetime,0,16) as createDate,b.gender, \n");
		sql.append("c.uuid as bigImgUuid,c.filename as bigImgName, \n");
		sql.append("d.uuid as midImgUuid,d.filename as midImgName, \n");
		sql.append("e.uuid as smImgUuid,e.filename as smImgName \n");
		sql.append("from msgShare a inner join userinfo b on a.creator = b.id \n");
		sql.append("inner join userOrganic bb on b.id=bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.bigheadportrait=c.id \n");
		sql.append("left join upfiles d on bb.mediumHeadPortrait=d.id \n");
		sql.append("left join upfiles e on bb.smallHeadPortrait=e.id \n");
		sql.append("where a.comId = ? and a.id = ?");
		return (MsgShare)this.objectQuery(sql.toString(),new Object[]{comId,id}, MsgShare.class);
	}
	/**
	 * 两次分享组的差异
	 * @param comId
	 * @param userId
	 * @param scopeType
	 * @param grpIds
	 * @return
	 */
	public Integer countDiffGroup(Integer comId, Integer userId,
			Integer scopeType, String grpIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		Integer count = 0;
		sql.append("select count(*) from usedGroup a");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		this.addSqlWhere(scopeType, sql, args, " and a.groupType=?");
		count = this.countQuery(sql.toString(), args.toArray());

		if(1==scopeType && count==0){//本次是自定义，以前是(所有人员或是自己)，则重新设置
			return 1;
		} else if(1==scopeType && count>0){//本次是自定义，以前是自定义
			sql = new StringBuffer();
			args = new ArrayList<Object>();
			sql.append("\n select count(*) from (");

			//历史数据中与本次数据的对比
			sql.append("\n select * from ( ");
			//历史数据
			sql.append("\n select a.grpId,b.grpName from usedGroup a ");
			sql.append("\n left join selfGroup b on a.comId=b.comId and b.owner=a.userId and a.grpId=b.id");
			sql.append("\n where 1=1 ");
			this.addSqlWhere(comId, sql, args, " and a.comId=?");
			this.addSqlWhere(userId, sql, args, " and a.userId=?");
			if (grpIds != null && (grpIds instanceof String ? !StringUtil.isBlank(grpIds.toString()) : true)) {
				sql.append("\n and a.grpId in ("+grpIds+") ");
			}

			sql.append("\n minus ");
			//本次数据
			sql.append("\n select b.id,b.grpName from selfGroup b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhere(comId, sql, args, " and b.comId=?");
			this.addSqlWhere(userId, sql, args, " and b.owner=?");
			if (grpIds != null && (grpIds instanceof String ? !StringUtil.isBlank(grpIds.toString()) : true)) {
				sql.append("\n and b.id in ("+grpIds+") ");
			}

			sql.append("\n)a");

			sql.append("\n union all ");
			//本次数据中与历史数据的对比
			sql.append("\n select * from ( ");
			//本次数据
			sql.append("\n select b.id,b.grpName from selfGroup b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhere(comId, sql, args, " and b.comId=?");
			this.addSqlWhere(userId, sql, args, " and b.owner=?");
			if (grpIds != null && (grpIds instanceof String ? !StringUtil.isBlank(grpIds.toString()) : true)) {
				sql.append("\n and b.id in ("+grpIds+") ");
			}
			sql.append("\n minus ");
			//历史数据
			sql.append("\n select a.grpId,b.grpName from usedGroup a ");
			sql.append("\n left join selfGroup b on a.comId=b.comId and b.owner=a.userId and a.grpId=b.id");
			sql.append("\n where 1=1 ");
			this.addSqlWhere(comId, sql, args, " and a.comId=?");
			this.addSqlWhere(userId, sql, args, " and a.userId=?");
			if (grpIds != null && (grpIds instanceof String ? !StringUtil.isBlank(grpIds.toString()) : true)) {
				sql.append("\n and a.grpId in ("+grpIds+") ");
			}

			sql.append("\n)b ");

			sql.append("\n)c where 1=1 ");
			return this.countQuery(sql.toString(), args.toArray());
		}else{//以前是自己或是所有人员
			//count为1则表示没有变动，为0则表示有变动
			return count;
		}
	}
	/**
	 * 主键查找信息讨论内容
	 * @param id 讨论主键
	 * @param comId 企业号
	 * @return
	 */
	public MsgShareTalk getMagShareTalk(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.msgId,a.recordCreateTime,a.id,a.parentid,a.comId,a.content,a.speaker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from msgShareTalk a inner join userinfo b on a.speaker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comID=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join msgShareTalk e on a.parentid=e.id and a.comId=e.comId");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return (MsgShareTalk) this.objectQuery(sql.toString(), args.toArray(), MsgShareTalk.class);
	}
	/**
	 * 讨论的附件
	 * @param comId
	 * @param msgId
	 * @param talkId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareTalkUpfile> listMagShareTalkFile(Integer comId,
			Integer msgId, Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.msgId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, \n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from msgShareTalkUpfile a inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comId =? and a.msgId = ? and a.msgShareTalkId=?\n");
		args.add(comId);
		args.add(msgId);
		args.add(talkId);
		sql.append("order by isPic asc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), MsgShareTalkUpfile.class);
	}
	/**
	 * 信息讨论分页
	 * @param msgId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareTalk> listPagedMsgShareTalk(Integer msgId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comId,a.content,a.talker speaker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from DAILYTALK a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  LEFT JOIN MSGSHARE m on m.modid = a.DAILYID ");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join msgShareTalk e on a.parentid=e.id and a.comId=e.comId");
		sql.append("\n where 1=1");
		this.addSqlWhere(msgId, sql, args, " and m.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.pagedQuery(sql.toString(),null ,args.toArray(), MsgShareTalk.class);
	}
	/**
	 * 信息讨论分页
	 * @param msgId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareTalk> listMsgShareTalk(Integer msgId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.* ");
		sql.append("\n  from msgShareTalk a ");
		sql.append("\n where 1=1");
		this.addSqlWhere(msgId, sql, args, " and a.msgId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime");
		return this.listQuery(sql.toString(), args.toArray(), MsgShareTalk.class);
	}
	/**
	 * 讨论的附件
	 * @param comId
	 * @param msgId
	 * @param talkId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareTalkUpfile> listMsgShareTalkFile(Integer comId, Integer msgId, Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select m.id msgId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, \n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from DAILYTALKFILE a inner join upfiles b on a.comId = b.comId and a.upfileid = b.id \n");
		sql.append("\n  LEFT JOIN MSGSHARE m on m.modid = a.DAILYID ");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comId =? and m.id = ? and a.TALKID=?\n");
		args.add(comId);
		args.add(msgId);
		args.add(talkId);
		sql.append("order by isPic asc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), MsgShareTalkUpfile.class);
	}
	/**
	 * 待删除的讨论
	 * @param comId
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareTalk> listMsgShareTalkForDel(Integer comId, Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from msgShareTalk where comId=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(id);
		return this.listQuery(sql.toString(), args.toArray(), MsgShareTalk.class);
	}
	/**
	 * 删除当前节点及其子节点回复
	 * @param id
	 * @param comId
	 */
	public void delMsgShareTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from msgShareTalk a where a.comId =? and a.id in \n");
		sql.append("(select id from msgShareTalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());

	}
	/**
	 * 讨论子节点提高一级
	 * @param id
	 * @param comId
	 */
	public void updateMsgShareTalkParentId(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		WeekRepTalk weekRepTalk = (WeekRepTalk) this.objectQuery(WeekRepTalk.class, id);
		if(null!=weekRepTalk && -1==weekRepTalk.getParentId()){//父节点为-1时将妻哪一个说话人设为空
			sql.append("update msgShareTalk set parentId=(select c.parentid \n");
			sql.append("from msgShareTalk c \n");
			sql.append("where c.id=?),ptalker=null where parentid = ? and comId = ?\n");
			args.add(id);
			args.add(id);
			args.add(comId);

		}else{//删除自己,将子节点提高一级
			sql.append("update msgShareTalk set parentId=(select c.parentid \n");
			sql.append("from msgShareTalk c \n");
			sql.append("where c.id=?) where parentid = ? and comId = ? \n");
			args.add(id);
			args.add(id);
			args.add(comId);
		}
		this.excuteSql(sql.toString(), args.toArray());

	}
	/**
	 * 为每一个参与组添加一条检索索引
	 * @param comId
	 * @param msgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> listMsgShareGrpIdStr4Index(Integer comId,
			Integer msgId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select \n");
		sql.append("case a.scopetype \n");
		sql.append("when 0 then 'all' \n");
		sql.append("when 2 then CONCAT('self_',a.creator) \n");
		sql.append("when 1 then b.grpid||'' end as grpIdStr \n");
		sql.append("from msgShare a \n");
		sql.append("left join shareGroup b on a.comId = b.comId and a.id = b.msgId \n");
		sql.append("where a.comId = ? and a.id =? \n");
		args.add(comId);
		args.add(msgId);
		return this.listQuery(sql.toString(),args.toArray(),SelfGroup.class);
	}
	/**
	 * 获取信息讨论为其创建索引
	 * @param comId
	 * @param msgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareTalk> listMsgShareTalk4Index(Integer comId,
			Integer msgId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.content,b.username as talkerName from msgShareTalk a \n");
		sql.append("inner join userInfo b on  a.speaker = b.id \n");
		sql.append("where a.comId =? and a.msgId = ? \n");
		args.add(comId);
		args.add(msgId);
		return this.listQuery(sql.toString(),args.toArray(),MsgShareTalk.class);
	}
	/**
	 * 获取信息讨论附件集合
	 * @param comId
	 * @param msgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareTalkUpfile> listMsgShareTalkFiles(Integer comId,
			Integer msgId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comId,a.msgId,a.upfileId,b.filename,b.uuid ,c.userName as creatorName,a.recordCreateTime,");
		sql.append("\n  c.gender,d.uuid as userUuid,d.fileName as userFileName from msgShareTalkUpfile a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id  ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on  cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(msgId, sql, args, " and a.msgId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.listQuery(sql.toString(), args.toArray(), MsgShareTalkUpfile.class);
	}
	/**
	 *  信息日志
	 * @param msgId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareLog> listPagedMsgShareLog(Integer msgId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,m.id msgId,b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from DAILYLOG a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("left join msgShare m on m.modId = a.dailyId \n");
		sql.append("where a.comId=? and m.id = ? \n");
		args.add(comId);
		args.add(msgId);
		return this.pagedQuery(sql.toString()," a.recordcreatetime desc", args.toArray(), MsgShareLog.class);
	}
	/**
	 *  信息日志
	 * @param msgId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareLog> listShareLog(Integer msgId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* \n");
		sql.append("from msgShareLog a \n");
		sql.append("where a.comId=? and a.msgId = ? \n");
		args.add(comId);
		args.add(msgId);
		return this.pagedQuery(sql.toString()," a.recordcreatetime", args.toArray(), MsgShareLog.class);
	}

	/**
	 *  信息日志
	 * @param msgId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareLog> listShareLogOfAll(Integer msgId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* \n");
		sql.append("from msgShareLog a \n");
		sql.append("where a.comId=? and a.msgId = ? \n");
		args.add(comId);
		args.add(msgId);
		sql.append(" order by a.recordcreatetime");
		return this.listQuery(sql.toString(), args.toArray(), MsgShareLog.class);
	}

	/**
	 * 信息附件分页
	 * @param comId
	 * @param msgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareTalkUpfile> listPagedMsgShareFiles(Integer comId,
			Integer msgId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comId,m.id msgId,b.filename,b.uuid,c.userName as creatorName,a.recordCreateTime,a.userId,b.fileExt,a.upfileId,");
		sql.append("\n  c.gender,d.uuid as userUuid,d.fileName as userFileName from DAILYTALKFILE a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id  ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join msgShare m on m.modId=a.dailyId  ");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(msgId, sql, args, " and m.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.pagedQuery(sql.toString()," a.recordCreateTime desc", args.toArray(), MsgShareTalkUpfile.class);
	}



	/**
	 * 自定义的分组成员
	 * @param comId
	 * @param grpIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listGroupUser(Integer comId, String grpIds) {
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.userinfoid as id From groupPersion a ");
		sql.append("\n where  comId='"+comId+"' and grpid in ("+grpIds+")");
		sql.append("\n group by  a.userinfoid");
		return this.listQuery(sql.toString(), null, UserInfo.class);
	}

	/**
	 * 指定分组的人员
	 * @param grpIds 拼接的分组主键
	 * @param comId 企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listScopeUser(String grpIds, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName from grouppersion a ");
		sql.append("\n inner join userinfo b on a.userinfoid=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=a.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		if (grpIds != null && (grpIds instanceof String ? !StringUtil.isBlank(grpIds.toString()) : true)) {
			sql.append("\n and a.grpId in ("+grpIds+") ");
		}
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 取得信息分享的人员范围
	 * @param msgId 信息主键
	 * @param comId 企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listShareUser(Integer msgId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
		sql.append("\n from sharegroup d inner join  grouppersion a on a.comId=d.comId and d.grpid=a.grpId");
		sql.append("\n inner join userinfo b on a.userinfoid=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=a.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(msgId, sql, args, " and d.msgId=?");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	/**
	 * 取得信息分享的人员范围
	 * @param msgId 信息主键
	 * @param comId 企业号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ShareUser> listAllShareUser(Integer msgId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("\n select * from shareuser where msgid = ? and comId = ? ");
		args.add(msgId);
		args.add(comId);
		return this.listQuery(sql.toString(), args.toArray(), ShareUser.class);
	}
    /**
     * 取得信息分享的人员范围
     * @param msgId 信息主键
     * @param comId 企业号
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ShareGroup> listShareGroup(Integer msgId, Integer comId) {
        List<Object> args = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("");
        sql.append("\n  select * from shareGroup where msgId = ? and comId = ?");
        args.add(msgId);
        args.add(comId);
        return this.listQuery(sql.toString(), args.toArray(), ShareGroup.class);
    }

	/**
	 * 查询本次需要推送的人员
	 * @param msgId 分享主键
	 * @param comId 团队号
	 * @param pushUserIdSet 本次推送的人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPushTodoUser(Integer msgId, Integer comId, Set<Integer> pushUserIdSet){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享的发布人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName ");
		sql.append("\n from msgShare d ");
		sql.append("\n inner join userinfo b on d.creator=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=d.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(msgId, sql, args, " and d.id=?");

		sql.append("\n union");
		//分享爱你个的关注人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
		sql.append("\n from msgShare d inner join attention a on a.busId=d.id and d.comId=a.comId and a.busType=1");
		sql.append("\n inner join userinfo b on d.creator=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=a.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(msgId, sql, args, " and d.id=?");
		//本次推送的人员
		if(null!=pushUserIdSet && !pushUserIdSet.isEmpty()){
			sql.append("\n union");
			sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
			sql.append("\n from userinfo b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhereIn(pushUserIdSet.toArray(new Integer[pushUserIdSet.size()]), sql, args, "\n and b.id in ?");
		}
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 查询周报本次需要推送的人员
	 * @param weekReportId 周报主键
	 * @param comId 团队号
	 * @param pushUserIdSet 本次推送的人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPushTodoUserForWeekReport(Integer weekReportId, Integer comId, Set<Integer> pushUserIdSet){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享发布人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName ");
		sql.append("\n from weekReport d ");
		sql.append("\n inner join userinfo b on d.id=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=d.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(weekReportId, sql, args, " and d.id=?");

		sql.append("\n union");
		//分享关注人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
		sql.append("\n from weekReport d inner join attention a on a.busId=d.id and d.comId=a.comId and a.busType = " + ConstantInterface.TYPE_WEEK + "");
		sql.append("\n inner join userinfo b on d.id=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=a.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(weekReportId, sql, args, " and d.id=?");
		//本次推送的人员
		if(null!=pushUserIdSet && !pushUserIdSet.isEmpty()){
			sql.append("\n union");
			sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
			sql.append("\n from userinfo b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhereIn(pushUserIdSet.toArray(new Integer[pushUserIdSet.size()]), sql, args, "\n and b.id in ?");
		}
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}

	/**
	 * 查询分享本次需要推送的人员
	 * @param dailyId 分享主键
	 * @param comId 团队号
	 * @param pushUserIdSet 本次推送的人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listPushTodoUserForDaily(Integer dailyId, Integer comId, Set<Integer> pushUserIdSet){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享发布人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName ");
		sql.append("\n from daily d ");
		sql.append("\n inner join userinfo b on d.id=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=d.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and d.id=?");

		sql.append("\n union");
		//分享关注人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
		sql.append("\n from daily d inner join attention a on a.busId=d.id and d.comId=a.comId and a.busType = " + ConstantInterface.TYPE_DAILY + "");
		sql.append("\n inner join userinfo b on d.id=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comId=a.comId and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comId=?");
		this.addSqlWhere(dailyId, sql, args, " and d.id=?");
		//本次推送的人员
		if(null!=pushUserIdSet && !pushUserIdSet.isEmpty()){
			sql.append("\n union");
			sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
			sql.append("\n from userinfo b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhereIn(pushUserIdSet.toArray(new Integer[pushUserIdSet.size()]), sql, args, "\n and b.id in ?");
		}
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}





	/**
	 * 通过模块取得分享信息内容
	 * @param type 业务类型
	 * @param modId 业务主键
	 * @param comId 团队号
	 * @param traceType 工作类型 0分享 1轨迹
	 * @return
	 */
	public MsgShare getMsgShareByModId(String type, Integer modId, Integer comId,Integer traceType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" select * from msgshare where 1=1");
		this.addSqlWhere(comId, sql, args, " and comId=?");
		this.addSqlWhere(type, sql, args, " and type=?");
		this.addSqlWhere(modId, sql, args, " and modId=?");
		this.addSqlWhere(traceType, sql, args, " and traceType=?");
		return (MsgShare) this.objectQuery(sql.toString(), args.toArray(), MsgShare.class);
	}
	/**
	 * 查询模块下是否有相同的附件
	 * @param comId 企业号
	 * @param msgId 模块主键
	 * @param upfileId 附件主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareTalkUpfile> listMsgSimUpfiles(Integer comId,
			Integer msgId, Integer upfileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//留言附件
		sql.append("select  a.comId,a.msgId,a.upfileId from msgShareTalkUpfile a\n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(msgId, sql, args, " and a.msgId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		return this.listQuery(sql.toString(), args.toArray(), MsgShareTalkUpfile.class);
	}
	/**
	 * 获取团队所有分享信息
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShare> listMsgShareOfAll(UserInfo userInfo){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("\n select a.id from msgShare a where a.comId=? and traceType=0 and type=1");
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(),MsgShare.class);
	}
	/**
	 * 信息查看权限验证
	 * @param comId 团队主键
	 * @param msgId 信息主键
	 * @param userId 验证人员主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShare> authorCheck(Integer comId,Integer msgId,Integer userId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.creator as id from msgShare a where a.comId=? and a.scopetype = 2 and a.id=? and a.creator=? ");
		args.add(comId);
		args.add(msgId);
		args.add(userId);
		sql.append("\n union ");
		sql.append("\n select d.userinfoid as id from msgShare a inner join shareGroup b on a.comId = ? " +
				"and a.comId = b.comId and a.id = b.msgid and a.scopetype = 1 and a.id=? " +
				"inner join selfGroup c on b.comId = c.comId and b.grpid = c.id inner join groupPersion d on " +
				"c.comId = d.comId and c.id= d.grpid and d.userinfoid= ? ");
		args.add(comId);
		args.add(msgId);
		args.add(userId);
		sql.append("\n union ");
		sql.append("\n select b.userId as id from msgShare a inner join userOrganic b on a.comId = b.comId and a.comId = ? " +
				"and a.scopetype = 0 and a.id=? and b.enabled = 1 and b.userId =? ");
		args.add(comId);
		args.add(msgId);
		args.add(userId);

		return this.listQuery(sql.toString(), args.toArray(),MsgShare.class);
	}
	/**
	 * 留言显示更多
	 * @param msgId
	 * @param comId
	 * @param minId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShareTalk> nextPageSizeMsgTalks(Integer msgId, Integer comId, Integer minId, Integer pageSize) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select * from(select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comId,a.content,a.speaker,a.ptalker,a.msgId ,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from msgShareTalk a inner join userinfo b on a.speaker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join msgShareTalk e on a.parentid=e.id and a.comId=e.comId");
		sql.append("\n where 1=1 and a.id < ?");
		args.add(minId);
		this.addSqlWhere(msgId, sql, args, " and a.msgId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id)");
		sql.append("\n where rownum <= ?");
		args.add(pageSize);
		return this.listQuery(sql.toString() ,args.toArray(), MsgShareTalk.class);
	}
	/**
	 * 查询对应的分享信息
	 * @param orgNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgShare> listPagedMsgForDaily(Integer orgNum) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.creator,u.userName creatorName,a.content,a.scopeType");
		sql.append("\n from msgshare a inner join userinfo u on a.creator=u.id");
		sql.append("\n where a.tracetype=0 and type=1 and a.comId=? ");
		args.add(orgNum);
		return this.pagedQuery(sql.toString(), " a.creator,a.recordCreateTime ", args.toArray(), MsgShare.class);
	}
}
