package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.SelfGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.Vote;
import com.westar.base.model.VoteChoose;
import com.westar.base.model.VoteLog;
import com.westar.base.model.VoteTalk;
import com.westar.base.model.VoteTalkFile;
import com.westar.base.model.Voter;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;

@Repository
public class VoteDao extends BaseDao {

	/**
	 * 分页查询投票的情况
	 * @param vote
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Vote> listPagedVote(Vote vote) {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from (")
		.append("\n select a.*,b.userName as ownerName,b.gender ownerGender,d.uuid ownerSmlImgUuid,")
		
		.append("case when(\n")
		.append("select count(*) from \n")
		.append(" todayworks today where a.comid = today.comid and a.id = today.busid \n")
		.append(" and today.bustype='"+ConstantInterface.TYPE_VOTE+"' and today.userId="+vote.getSessionUser()+" and today.isclock=0\n")
		.append("and today.readState=0\n")
		.append(")=0 then 1 else 0 end as isread,\n")
		
		
		.append("\n case when atten.id is null then 0 else 1 end as attentionState,")
			.append("\n case when round(to_number(TO_DATE('"+nowTime+"','yyyy-mm-dd hh24:mi:ss')")
			.append("\n -TO_DATE(a.finishtime,'yyyy-mm-dd hh24:mi:ss'))*24*60*60)<0 then 1 else 0   end as enabled,")
			.append("\n(select count(*) from voteTalk b where b.comid=a.comid and b.voteid=a.id ) as sumtalks,")
			.append("\n(select count(*) from  voter c where a.comid=c.comid and a.id=c.voteid and c.voter="+vote.getSessionUser()+") as isvote,")
			.append("\n(select count(*) from  voter d where a.comid=d.comid and a.id=d.voteid ) as voteTotal")
			.append("\n from (");
		if(null!=vote.getSearchMe()&&!"".equals(vote.getSearchMe())){
			sql.append("\n  select a.*")
			.append("\n  from vote a")
			.append("\n  where a.delstate=0 and  a.scopeType=2 and a.comid="+vote.getComId()+" and a.owner="+vote.getSessionUser());
		}else{
			
			sql.append("\n  select a.*")
			.append("\n  from vote a")
			.append("\n  where a.delstate=0 and a.scopeType=2 and a.comid="+vote.getComId()+" and a.owner="+vote.getSessionUser());
			//查询创建时间段
			this.addSqlWhere(vote.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(vote.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
			sql.append("\n  union all")
			.append("\n  select a.*")
			.append("\n  from vote a")
			.append("\n  where a.delstate=0 and a.scopeType=0 and a.comid="+vote.getComId()+"");
			//查询创建时间段
			this.addSqlWhere(vote.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(vote.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
			sql.append("\n  union all")
			.append("\n  select a.*")
			.append("\n  from vote a")
			.append("\n  where a.delstate=0 and a.scopeType=1 and a.comid="+vote.getComId());
			//查询创建时间段
			this.addSqlWhere(vote.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
			this.addSqlWhere(vote.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
			//不是查询所有的
			sql.append("\n and  a.id in ( select c.voteid from selfgroup a inner join groupPersion b on a.comid=b.comid and a.id=b.grpid")
			.append("\n  inner join voteScope c on a.comid=c.comid and a.id=c.grpid")
			.append("\n  where a.comid="+vote.getComId());
			
			if("12".equals(vote.getSearchAll())){//我参与的
				this.addSqlWhere(vote.getSessionUser(), sql, args, " and (b.userinfoid=? and a.owner!=?)",2);
			}else {//我发起的
				this.addSqlWhere(vote.getSessionUser(), sql, args, " and b.userinfoid=?");
			}
			sql.append("\n  )");
		}
		sql.append("\n )a inner join userinfo b on a.owner=b.id");
		sql.append("\n inner join userOrganic c on a.owner =c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on c.mediumHeadPortrait = d.id");
		sql.append("\n left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_VOTE+"' and atten.userId=?");
		args.add(vote.getSessionUser());
		sql.append("\n where 1=1 ");
		
		 if(null!=vote.getSearchAll()&&"11".equals(vote.getSearchAll())){//我发起的
			 this.addSqlWhere(vote.getOwner(), sql, args, " and a.owner=?");
		 }else if(null!=vote.getSearchAll()&&"12".equals(vote.getSearchAll())){//我参与的
			sql.append("\n and exists( ");
			sql.append("\n select id from voter where voter.voteid=a.id and voter.comid=a.comid and voter.voter="+vote.getSessionUser());
			sql.append("\n ) ");
		 }
		 sql.append("\n  ) a where 1=1 ");
		 this.addSqlWhere(vote.getVoteContent(), sql, args, " and dbms_lob.instr(a.votecontent,?,1)>0");
		 this.addSqlWhere(vote.getAttentionState(), sql, args, " and a.attentionState=?");
		 //是否过期
		 this.addSqlWhere(vote.getEnabled(), sql, args, " and a.enabled=?");
		sqlCount.append("\n select count(*) from (");
		sqlCount.append(sql);
		sqlCount.append("\n)a");
		if(null!=vote.getOrderBy()&&"31".equals(vote.getOrderBy())){//回复最多
			return this.pagedQuery(sql.toString(), sqlCount.toString(), "isread,sumtalks desc, a.id desc", args.toArray(), Vote.class);
		}else if(null!=vote.getOrderBy()&&"32".equals(vote.getOrderBy())){//投票最多
			return this.pagedQuery(sql.toString(), sqlCount.toString(), "isread,voteTotal desc,a.id desc", args.toArray(), Vote.class);
		}else{
			return this.pagedQuery(sql.toString(), sqlCount.toString(), "isread,a.id desc", args.toArray(), Vote.class);
		}
	}
	/**
	 * 查询投票信息
	 * @param id
	 * @param voter
	 * @param comId 
	 * @return
	 */
	public Vote getVoteInfo(Integer id, Integer comId, Integer voter) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		sql.append("\n select a.*,c.uuid as ownerSmlImgUuid,c.filename as ownerSmlImgName,b.gender as ownerGender,b.userName as ownerName,");
		sql.append("\n case when atten.id is null then 0 else 1 end as attentionState,");
		sql.append("\n case when round(to_number(TO_DATE('"+nowTime+"','yyyy-mm-dd hh24:mi:ss')");
		sql.append("\n -TO_DATE(a.finishtime,'yyyy-mm-dd hh24:mi:ss'))*24*60*60)<0 then 1 else 0   end as enabled,");
		sql.append("\n(select count(*) from voteTalk b where b.comid=a.comid and b.voteid=a.id ) as sumtalks,");
		sql.append("\n  (select count(*) from  voter d where a.comid=d.comid and a.id=d.voteid and d.voter="+voter+") as voterChooseNum,");
		sql.append("\n  (select count(*) from  voter d where a.comid=d.comid and a.id=d.voteid ) as voteTotal");
		sql.append("\n  from vote a left join userinfo b on  a.owner=b.id");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on  c.id=bb.mediumHeadPortrait");
		sql.append("\n left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_VOTE+"' and atten.userId=?");
		args.add(voter);
		sql.append("\n  where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return (Vote) this.objectQuery(sql.toString(), args.toArray(), Vote.class);
	}
	/**
	 * 投票选项
	 * @param voteId
	 * @param comId
	 * @param integer 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteChoose> getVoteChoose(Integer voteId, Integer comId, Integer voter) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,b.uuid as orgImgUuid,b.filename as orgImgName,");
		sql.append("\n (select count(*) from voter e where e.comid=a.comid and a.voteid=e.voteId and a.id=e.chooseid)as total,");
		sql.append("\n c.uuid as largeImgUuid,c.filename as largeImgName,");
		sql.append("\n d.uuid as midImgUuid,d.filename as midImgName, ");
		sql.append("\n case when e.id is null then 0 else 1 end as chooseState ");
		sql.append("\n from votechoose a left join upfiles b on a.original=b.id and a.comid=b.comid");
		sql.append("\n left join upfiles c on a.large=c.id and a.comid=c.comid");
		sql.append("\n left join upfiles d on a.middle=d.id and a.comid=d.comid");
		sql.append("\n left join voter e on  a.comid=e.comid and a.id=e.chooseid and a.voteid=e.voteid and e.voter="+voter);
		sql.append("\n where 1=1");
		this.addSqlWhere(voteId, sql, args, " and a.voteId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  order by total desc, a.id ");
		return this.listQuery(sql.toString(), args.toArray(), VoteChoose.class);
	}
	/**
	 * 选项的投票人
	 * @param voteId
	 * @param comId
	 * @param chooseId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Voter> getVoters(Integer voteId, Integer comId, Integer chooseId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,c.uuid as smImgUuid,c.filename as smImgName,b.gender as voterGender,b.userName as voterName"); 
		sql.append("\n from Voter a inner join userinfo b on  a.voter=b.id");
		sql.append("\n inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n left join upfiles c on  c.id=bb.smallHeadPortrait");
		sql.append("\n  where 1=1");
		this.addSqlWhere(voteId, sql, args, " and a.voteId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(chooseId, sql, args, " and a.chooseId=?");
		sql.append("\n  order by  a.id ");
		return this.listQuery(sql.toString(), args.toArray(), Voter.class);
	}
	/**
	 * 投票权限验证
	 * @param comId
	 * @param voteId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Vote> authorCheck(Integer comId,Integer voteId,Integer userId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,c.grpname,c.owner as groupOwner,d.userinfoid as groupUser \n");
		sql.append("from vote a \n");
		sql.append("left join voteScope b on a.comid = b.comid and a.id = b.voteId \n");
		sql.append("left join selfGroup c on b.comid = c.comid and b.grpId = c.id \n");
		sql.append("left join groupPersion d on c.comid = d.comid and c.id = d.grpid \n");
		sql.append("where a.comid = ? and a.id =? and a.delstate=0 \n");
		args.add(comId);
		args.add(voteId);
		sql.append("and ((a.scopetype=0) or (a.scopetype=2 and a.owner = ?) or (a.scopetype=1 and d.userinfoid = ?) or a.owner=?)\n");
		args.add(userId);
		args.add(userId);
		args.add(userId);
		return this.listQuery(sql.toString(), args.toArray(),Vote.class);
	}
	/**
	 * 公司总的投票数
	 * @param comId
	 * @return
	 */
	public Integer getVoteTotals(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("select count(*) from vote where delstate=0");
		this.addSqlWhere(comId, sql, args, " and comId=? ");
		return this.countQuery(sql.toString(), args.toArray());
	}
	/**
	 * 投票的讨论
	 * @param voteId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteTalk> listPagedVoteTalk(Integer voteId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comid,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from voteTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join voteTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(voteId, sql, args, " and a.voteId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.pagedQuery(sql.toString(),null ,args.toArray(), VoteTalk.class);
	}
	/**
	 * 投票回复的回复信息
	 * @param voteTalk
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteTalk> getVoteTalkChild(VoteTalk voteTalk) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.id from voteTalk a");
		sql.append("\n where 1=1");
		this.addSqlWhere(voteTalk.getVoteId(), sql, args, " and a.voteId=?");
		this.addSqlWhere(voteTalk.getComId(), sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid="+voteTalk.getId()+" CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), VoteTalk.class);
	}
	/**
	 * 选项投票最多的
	 * @param voteId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteChoose> getMostChooses(Integer voteId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select id,content from ");
		sql.append("\n (select  a.chooseid as id,b.content,row_number() over(partition by a.chooseid order by a.chooseid ) rn");
		sql.append("\n from voter a right join votechoose b on a.comid=b.comid  and a.voteid=b.voteid and a.chooseid=b.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(voteId, sql, args, " and a.voteid=?");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		sql.append("\n )a where rn=1 ");
		return this.listQuery(sql.toString(), args.toArray(), VoteChoose.class);
	}
	/**
	 * 只取出投票截止时间
	 * @param id
	 * @param comId
	 * @return
	 */
	public Vote getVoteObj(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select voteContent,finishTime,delstate from vote where 1=1");
		this.addSqlWhere(comId, sql, args, " and comId=?");
		this.addSqlWhere(id, sql, args, " and id=?");
		return (Vote) this.objectQuery(sql.toString(), args.toArray(), Vote.class);
	}
	/**
	 * 投票讨论
	 * @param id 讨论主键
	 * @param comId 公司编号
	 * @return
	 */
	public VoteTalk getVoteTalk(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.voteId,a.recordCreateTime,a.id,a.parentid,a.comid,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from voteTalk a inner join userinfo b on a.talker=b.id");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join voteTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return (VoteTalk) this.objectQuery(sql.toString(), args.toArray(), VoteTalk.class);
	}
	/**
	 * 投票日志
	 * @param voteId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteLog> listPagedVoteLog(Integer voteId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from voteLog a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.voteid = ? \n");
		args.add(comId);
		args.add(voteId);
		return this.pagedQuery(sql.toString()," a.recordcreatetime desc", args.toArray(), VoteLog.class);
	}
	/**
	 * 删除自己,将子节点提高一级
	 * @param id
	 * @param comId
	 */
	public void updateVoteTalkParentId(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		VoteTalk votetalk = (VoteTalk) this.objectQuery(VoteTalk.class, id);
		if(null!=votetalk && -1==votetalk.getParentId()){//父节点为-1时将前一个说话人设为空
			sql.append("update votetalk set parentId=(select c.parentid \n");
			sql.append("from votetalk c \n");
			sql.append("where c.id=?),ptalker=null where parentid = ? and comId = ?\n");
			args.add(id);
			args.add(id);
			args.add(comId);
			
		}else{//删除自己,将子节点提高一级
			sql.append("update votetalk set parentId=(select c.parentid \n");
			sql.append("from votetalk c \n");
			sql.append("where c.id=?) where parentid = ? and comId = ? \n");
			args.add(id);
			args.add(id);
			args.add(comId);
		}
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 具体投票內容
	 * @param voteId
	 * @param comId
	 * @param chooseId
	 * @return
	 */
	public VoteChoose getVoteChoose(Integer voteId, Integer comId,
			String chooseId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append(" select a.content from voteChoose a  ");
		sql.append(" inner join vote b on a.comid=b.comid and a.voteid=b.id and b.votetype=0 where 1=1 ");
		this.addSqlWhere(voteId, sql, args, " and a.voteId=?");
		this.addSqlWhere(chooseId, sql, args, " and a.id=?");
		return (VoteChoose) this.objectQuery(sql.toString(), args.toArray(), VoteChoose.class);
	}
	/**
	 * 讨论的附件
	 * @param comId 企业编号
	 * @param voteId 投票的主键
	 * @param talkId 讨论的主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteTalkFile> listVoteTalkFile(Integer comId, Integer voteId,
			Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.voteId,a.talkId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, \n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from voteTalkFile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comid =? and a.voteId = ? and a.talkId=?\n");
		args.add(comId);
		args.add(voteId);
		args.add(talkId);
		sql.append("order by isPic asc, a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), VoteTalkFile.class);
	}
	/**
	 * 待删除的讨论
	 * @param comId 企业编号
	 * @param id 讨论主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteTalk> listVoteTalkForDel(Integer comId, Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from voteTalk where comid=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(id);
		return this.listQuery(sql.toString(), args.toArray(), VoteTalk.class);
	}
	/**
	 * 删除讨论
	 * @param id 讨论主键
	 * @param comId 企业编号
	 */
	public void delVoteTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from voteTalk a where a.comid =? and a.id in \n");
		sql.append("(select id from voteTalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 投票讨论附件(分页)
	 * @param comId
	 * @param voteId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteTalkFile> listPagedVoteTalkFiles(Integer comId, Integer voteId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.comid,a.voteId,b.filename,b.uuid ,c.userName as creatorName,a.recordCreateTime,c.gender,");
		sql.append("\n  a.upfileId,d.uuid as userUuid,d.fileName as userFileName,a.userId,b.fileExt ");
		sql.append("\n  from voteTalkFile a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id  ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(voteId, sql, args, " and a.voteId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.pagedQuery(sql.toString()," a.recordCreateTime desc", args.toArray(), VoteTalkFile.class);
	}
	
	/**
	 * 查询附件总数
	 * @param comId
	 * @param voteId
	 * @return
	 */
	public Integer countFile(Integer comId, Integer voteId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select count(1) from( ");
		sql.append("\n  select a.id,a.comid,a.voteId,b.filename,b.uuid ,c.userName as creatorName,a.recordCreateTime,c.gender,");
		sql.append("\n  a.upfileId,d.uuid as userUuid,d.fileName as userFileName,a.userId,b.fileExt ");
		sql.append("\n  from voteTalkFile a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id  ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(voteId, sql, args, " and a.voteId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n ) ");
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 投票讨论附件
	 * @param comId
	 * @param voteId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteTalkFile> listVoteTalkFiles(Integer comId, Integer voteId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.id,a.talkId,a.comid,a.voteId,a.upfileId,b.filename,b.uuid ,c.userName as creatorName,a.recordCreateTime,");
		sql.append("\n  c.gender,d.uuid as userUuid,d.fileName as userFileName from voteTalkFile a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id  ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on  cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(voteId, sql, args, " and a.voteId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.listQuery(sql.toString(), args.toArray(), VoteTalkFile.class);
	}
	/**
	 * 获取投票选择描述为其创建索引
	 * @param comId
	 * @param voteId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteChoose> listVoteChoose4Index(Integer comId,Integer voteId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.content from voteChoose a where a.comid =? and a.voteId = ? \n");
		args.add(comId);
		args.add(voteId);
		return this.listQuery(sql.toString(),args.toArray(),VoteChoose.class);
	}
	/**
	 * 获取投票讨论为其创建索引
	 * @param comId
	 * @param voteId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteTalk> listVoteTalk4Index(Integer comId,Integer voteId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.content,b.username as talkerName from voteTalk a \n");
		sql.append("inner join userInfo b on  a.talker = b.id \n");
		sql.append("where a.comid =? and a.voteId = ? \n");
		args.add(comId);
		args.add(voteId);
		return this.listQuery(sql.toString(),args.toArray(),VoteTalk.class);
	}
	/**
	 * 获取投票可以查看范围的所有人员集合
	 * @param comId
	 * @param voteId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listVoteViewers(Integer comId,Integer voteId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select d.id,d.username from voteScope a \n");
		sql.append("inner join vote v1 on a.comid = v1.comid and a.voteid = v1.id and v1.scopetype =1 \n");
		sql.append("inner join selfGroup b on a.comid = b.comid and a.grpid = b.id  \n");
		sql.append("inner join groupPersion c on b.comid = c.comid and b.id = c.grpid \n");
		sql.append("inner join Userinfo d on c.userinfoid  = d.id and d.enabled =1 and d.checkstate=1 \n");
		sql.append("where a.comid = ? and v1.id =? \n");
		args.add(comId);
		args.add(voteId);
		sql.append("union \n");
		sql.append("select f.id,f.username from vote v2 inner join userinfo f on v2.scopetype =0 \n");
		sql.append("where v2.comid = ? and v2.id =? and f.enabled = 1 and f.checkstate =1 \n");
		args.add(comId);
		args.add(voteId);
		sql.append("union \n");
		sql.append("select f.id,f.username from vote v3 inner join userinfo f on v3.owner = f.id and v3.scopetype=2 \n");
		sql.append("where v3.comid = ? and v3.id =? \n");
		args.add(comId);
		args.add(voteId);
		sql.append("union \n");
		sql.append("select h.id,h.username from vote v4 inner join userinfo h on  v4.owner = h.id \n");
		sql.append("where v4.comid = ? and v4.id =? \n");
		args.add(comId);
		args.add(voteId);
		return this.listQuery(sql.toString(),args.toArray(),UserInfo.class);
	}
	/**
	 * 获取投票范围组信息集合
	 * @param comId
	 * @param voteId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> listVoteGrpIdStr4Index(Integer comId,Integer voteId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select \n");
		sql.append("case a.scopetype \n");
		sql.append("when 0 then 'all' \n");
		sql.append("when 2 then CONCAT('self_',a.owner) \n");
		sql.append("when 1 then b.grpid||'' end as grpIdStr \n");
		sql.append("from vote a \n");
		sql.append("left join voteScope b on a.comid = b.comid and a.id = b.voteid \n");
		sql.append("where a.comid = ? and a.id =? \n");
		args.add(comId);
		args.add(voteId);
		return this.listQuery(sql.toString(),args.toArray(),SelfGroup.class);
	}
	/**
	 * 投票的参与人
	 * @param voteId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listVoteUser(Integer voteId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select c.id,c.email,c.wechat,c.qq,c.userName from groupPersion a inner join votescope b  ");
		sql.append("\n on a.comid=b.comid and a.grpid=b.grpid  ");
		sql.append("\n inner join userInfo c on a.userinfoid=c.id");
		sql.append("\n inner join userorganic d on d.userid=c.id and a.comid=d.comid and d.enabled=1");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, "  and a.comId=?");
		this.addSqlWhere(voteId, sql, args, "  and b.voteid=?");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	
	/**
	 * 获取自己权限下排列前N的投票数据集合
	 * @param userInfo
	 * @param rowNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Vote> firstNVoteList(UserInfo userInfo,Integer rowNum) {
		StringBuffer sql = new StringBuffer();
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from (")
		.append("\n select a.*,b.userName as ownerName,b.gender ownerGender,d.uuid ownerSmlImgUuid,")
		
		.append("case when(\n")
		.append("select count(*) from \n")
		.append(" todayworks today where a.comid = today.comid and a.id = today.busid \n")
		.append(" and today.bustype='"+ConstantInterface.TYPE_VOTE+"' and today.userId="+userInfo.getId()+" and today.isclock=0\n")
		.append("and today.readState=0\n")
		.append(")=0 then 1 else 0 end as isread,\n")
		
		.append("\n case when atten.id is null then 0 else 1 end attentionState,")
			.append("\n case when round(to_number(TO_DATE('"+nowTime+"','yyyy-mm-dd hh24:mi:ss')")
			.append("\n -TO_DATE(a.finishtime,'yyyy-mm-dd hh24:mi:ss'))*24*60*60)<0 then 1 else 0   end as enabled,")
			.append("\n(select count(*) from  voter c where a.comid=c.comid and a.id=c.voteid and c.voter="+userInfo.getId()+") as isvote")
			.append("\n from (");
		
		sql.append("\n  select a.*")
		.append("\n  from vote a")
		.append("\n  where a.delstate=0 and a.scopeType=2 and a.comid="+userInfo.getComId()+" and a.owner="+userInfo.getId());
		sql.append("\n  union all")
		.append("\n  select a.*")
		.append("\n  from vote a")
		.append("\n  where a.delstate=0 and a.scopeType=0 and a.comid="+userInfo.getComId()+"");
		sql.append("\n  union all")
		.append("\n  select a.*")
		.append("\n  from vote a")
		.append("\n  where a.delstate=0 and a.scopeType=1 and a.comid="+userInfo.getComId());
		//不是查询所有的
		sql.append("\n and  a.id in ( select c.voteid from selfgroup a inner join groupPersion b on a.comid=b.comid and a.id=b.grpid")
		.append("\n  inner join voteScope c on a.comid=c.comid and a.id=c.grpid")
		.append("\n  where a.comid="+userInfo.getComId());
		sql.append("\n  )");
		sql.append("\n )a inner join userinfo b on a.owner=b.id");
		sql.append("\n inner join userOrganic c on a.owner =c.userId and a.comId=c.comId");
		sql.append("\n left join upfiles d on c.mediumHeadPortrait = d.id");
		sql.append("\n left join attention atten on a.comid=atten.comid and a.id=atten.busId and atten.userId="+userInfo.getId()+" and atten.busType="+ConstantInterface.TYPE_VOTE);
		sql.append("\n where 1=1 order by isread asc");
		sql.append("\n  ) a ");
		sql.append(" where rownum < ? order by recordcreatetime desc");
		args.add(rowNum);
		return this.listQuery(sql.toString(), args.toArray(),Vote.class);
	}
	/**
	 * 查询模块是否相同的附件
	 * @param comId
	 * @param voteId
	 * @param upfileId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VoteTalkFile> listVoteSimUpfiles(Integer comId, Integer voteId,
			Integer upfileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//投票讨论附件
		sql.append("select a.comId,a.voteId,a.upfileId  from voteTalkfile a \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(voteId, sql, args, " and a.voteId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		return this.listQuery(sql.toString(), args.toArray(), VoteTalkFile.class);
	}
	/**
	 * 获取团队所有投票
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Vote> listVoteOfAll(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//投票讨论附件
		sql.append("\n select a.id from vote a where a.comid=?");
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(), Vote.class);
	}
}
