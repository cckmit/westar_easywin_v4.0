package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.AnsFile;
import com.westar.base.model.AnsTalk;
import com.westar.base.model.Answer;
import com.westar.base.model.QasTalkFile;
import com.westar.base.model.QuesFile;
import com.westar.base.model.QuesLog;
import com.westar.base.model.Question;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;

@Repository
public class QasDao extends BaseDao {

	/**
	 * 分页查询问题中心的问题
	 * @param question
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Question> listPagedQas(Question question) {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from (")
		.append("\n select a.id,a.cnans,a.recordcreatetime,a.comid,a.title,a.state,a.userid,b.userName,c.content as cnAnsContent,e.uuid imgUuid,e.filename imgName,b.gender, ")
		.append("\n case when atten.id is null then 0 else 1 end as attentionState,")
		
		.append("case when(\n")
		.append("select count(*) from \n")
		.append(" todayworks today where a.comid = today.comid and a.id = today.busid \n")
		.append(" and today.bustype='"+ConstantInterface.TYPE_QUES+"' and today.userId="+question.getSessionUser()+" and today.isclock=0\n")
		.append("and today.readState=0\n")
		.append(")=0 then 1 else 0 end as isread,\n")
		
		.append("\n (select count(*) from answer c where c.comid=a.comid and a.id=c.quesid )as ansTotal,c.recordcreatetime as ansDate,d.userName as ansUserName,")
		.append("\n (select count(*) from answer c where c.comid=a.comid and a.id=c.quesid and c.userId="+question.getSessionUser()+") as ansNum from (")
		//范围是所有人
		.append("\n  select a.id,a.cnans,a.recordcreatetime,a.comid,a.userid,a.title,a.state from  question a")
		.append("\n  where a.delstate=0 and  a.comid="+question.getComId()+"");
		//问题状态
		this.addSqlWhere(question.getState(), sql, args, " and a.state=?");
		sql.append("\n )a inner join userinfo b on a.userid=b.id");
		sql.append("\n inner join userorganic bb on a.comId=bb.comid and a.userid=bb.userid");
		sql.append("\n  left join upfiles e on bb.mediumHeadPortrait = e.id");
		sql.append("\n  left join answer c on a.comid=c.comid and a.cnans=c.id");
		sql.append("\n  left join userinfo d on c.userid=d.id");
		sql.append("\n left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_QUES+"' and atten.userId=?");
		args.add(question.getSessionUser());
		sql.append("\n where 1=1 ");
		sql.append("\n  ) a where 1=1 ");
		//查询创建时间段
		this.addSqlWhere(question.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(question.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		this.addSqlWhere(question.getAttentionState(), sql, args, " and a.attentionState=?");
		if("12".equals(question.getSearchAll())){//我回答的
			sql.append("\n and a.ansNum>=1");
		}else if ("11".equals(question.getSearchAll())){
			this.addSqlWhere(question.getSessionUser(), sql, args, " and a.userid=? ");
		}else if ("13".equals(question.getSearchAll())){
			sql.append("\n and a.ansNum=0");
		}
		this.addSqlWhere(question.getTitle(), sql, args, " and instr(a.title,?,1)>0 ");
		sqlCount.append("\n select count(*) from (");
		sqlCount.append(sql);
		sqlCount.append("\n)a");
		if(null!=question.getOrderBy()&&"31".equals(question.getOrderBy())){//回复最多
			return this.pagedQuery(sql.toString(), sqlCount.toString(), "a.ansTotal desc,a.isread,a.id desc", args.toArray(), Question.class);
		}else{
			return this.pagedQuery(sql.toString(), sqlCount.toString(), "a.isread,a.id desc", args.toArray(), Question.class);
		}
	}
	/**
	 * 获取所有团队问答
	 * @param userinfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Question> listQasOfAll(UserInfo userinfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id from question a where a.delstate=0 and  a.comid=?");
		args.add(userinfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(),Question.class);
	}

	/**
	 * 所选问题
	 * @param id
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Question getQuesById(Integer id, Integer comId,Integer userId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.recordcreatetime,a.comid,a.delState,a.title,a.state,a.userid,a.content,b.userName,a.cnAns, ");
		sql.append("\n case when atten.id is null then 0 else 1 end as attentionState,");
		sql.append("\n (select count(*) from answer c where c.comid=a.comid and a.id=c.quesid and c.userId="+userId+") as ansNum,");
		sql.append("\n (select count(*) from answer c where c.comid=a.comid and a.id=c.quesid )as ansTotal,");
		sql.append("\n d.uuid as imgUuid,d.fileName as imgName,b.gender");
		sql.append("\n from question a inner join userinfo b on a.userid=b.id");
		sql.append("\n inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n left join upfiles d on bb.mediumHeadPortrait=d.id");
		sql.append("\n left join attention atten on a.comid = atten.comid and a.id = atten.busid and atten.bustype='"+ConstantInterface.TYPE_QUES+"' and atten.userId=? ");
		args.add(userId);
		sql.append("\n where 1=1"); 
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return (Question) this.objectQuery(sql.toString(), args.toArray(), Question.class);
	}
	/**
	 * 问题对应的附件
	 * @param id
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QuesFile> listQuesFile(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.quesId,a.original,b.uuid as orgFileUuid,b.filename as orgFileName,"); 
		sql.append("\n b.filepath as orgFilePath,b.fileext,"); 
		sql.append("\n case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic"); 
		sql.append("\n from QuesFile a");
		
		sql.append("\n inner join upfiles b on a.original=b.id and a.comid=b.comid"); 
		sql.append("\n where 1=1");  
		this.addSqlWhere(id, sql, args, " and a.quesId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by isPic asc,a.orderNo asc"); 
		return this.listQuery(sql.toString(), args.toArray(), QuesFile.class);
	}

	/**
	 * 问题的回答
	 * @param quesId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Answer> listPagedAns(Integer quesId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.comId,a.recordCreateTime,a.id,a.quesId,a.userId,a.content,b.userName,"); 
		sql.append("\n d.uuid as imgUuid,d.fileName as imgName,b.gender,"); 
		sql.append("\n case when a.id=q.cnans then 1 else 0 end cnFlag"); 
		sql.append("\n from answer  a inner join userinfo b on a.userid=b.id"); 
		sql.append("\n inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n left join upfiles d on bb.mediumHeadPortrait=d.id");
		sql.append("\n inner join question q on a.quesid=q.id and a.comid=q.comid");
		sql.append("\n where 1=1");  
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.pagedQuery(sql.toString(), " cnFlag desc, a.id desc ", args.toArray(), Answer.class);
	}
	
	/**
	 * 问题的回答
	 * @param quesId
	 * @param comId
	 * @return
	 */
	public Answer getAnsById(Integer quesId, Integer comId,Integer id) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.comId, a.recordCreateTime,a.id,a.quesId,a.userId,a.content,b.userName,"); 
		sql.append("\n c.uuid as imgUuid,c.fileName as imgName,b.gender"); 
		sql.append("\n from answer  a inner join userinfo b on a.userid=b.id"); 
		sql.append("\n inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n left join upfiles c on bb.mediumHeadPortrait = c.id");
		sql.append("\n where 1=1");  
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return  (Answer) this.objectQuery(sql.toString(), args.toArray(), Answer.class);
	}

	/**
	 * 回答对应的图片
	 * @param quesId
	 * @param comId
	 * @param answerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnsFile> listAnsFile(Integer quesId, Integer comId, Integer answerId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.id,a.original,a.answerId,a.quesId,b.uuid as orgFileUuid,b.filename as orgFileName,b.fileext,"); 
		sql.append("\n case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic"); 
		sql.append("\n from AnsFile a"); 
		sql.append("\n inner join upfiles b on a.original=b.id and a.comid=b.comid"); 
		sql.append("\n where 1=1");  
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(answerId, sql, args, " and a.answerId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by isPic asc, a.orderNo asc");  
		return this.listQuery(sql.toString(), args.toArray(), AnsFile.class);
	}

	/**
	 * 问答日志
	 * @param quesId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QuesLog> listPagedQuesLog(Integer quesId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from quesLog a \n");
		sql.append("inner join userinfo b on a.userId = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.quesid = ? \n");
		args.add(comId);
		args.add(quesId);
		return this.pagedQuery(sql.toString()," a.recordcreatetime desc", args.toArray(), QuesLog.class);
	}

	/**
	 * 回答对应的评论
	 * @param quesId
	 * @param comId
	 * @param ansId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnsTalk> listAnsTalk(Integer quesId, Integer comId, Integer ansId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comid,a.talkcontent,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.talkcontent as pcontent");
		sql.append("\n  from ansTalk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id");
		sql.append("\n  left join ansTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(ansId, sql, args, " and a.ansId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.listQuery(sql.toString(), args.toArray(), AnsTalk.class);
	}

	/**
	 * 回答评论的回复信息
	 * @param ansTalk
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnsTalk> getAnsTalkChild(AnsTalk ansTalk) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.id from ansTalk a");
		sql.append("\n where 1=1");
		this.addSqlWhere(ansTalk.getQuesId(), sql, args, " and a.quesId=?");
		this.addSqlWhere(ansTalk.getAnsId(), sql, args, " and a.ansId=?");
		this.addSqlWhere(ansTalk.getComId(), sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid="+ansTalk.getId()+" CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), AnsTalk.class);
	}

	/**
	 * 删除自己,将子节点提高一级
	 * @param id
	 * @param comId
	 */
	public void updateAnsTalkParentId(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		AnsTalk anstalk = (AnsTalk) this.objectQuery(AnsTalk.class, id);
		if(null!=anstalk && -1==anstalk.getParentId()){//父节点为-1时将前一个说话人设为空
			sql.append("update anstalk set parentId=(select c.parentid \n");
			sql.append("from anstalk c \n");
			sql.append("where c.id=?),ptalker=null where parentid = ? and comId = ?\n");
			args.add(id);
			args.add(id);
			args.add(comId);
			
		}else{//删除自己,将子节点提高一级
			sql.append("update anstalk set parentId=(select c.parentid \n");
			sql.append("from anstalk c \n");
			sql.append("where c.id=?) where parentid = ? and comId = ? \n");
			args.add(id);
			args.add(id);
			args.add(comId);
		}
		this.excuteSql(sql.toString(), args.toArray());
		
	}

	/**
	 * 添加的回复
	 * @param id
	 * @param comId
	 * @return
	 */
	public AnsTalk getAnsTalkById(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.quesId,a.ansId,a.recordCreateTime,a.id,a.parentid,a.comid,a.talkContent,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.talkContent as pcontent");
		sql.append("\n  from ansTalk a inner join userinfo b on a.talker=b.id");
		sql.append("\n inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join ansTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return (AnsTalk) this.objectQuery(sql.toString(), args.toArray(), AnsTalk.class);
	}

	/**
	 * 问答附件
	 * @param userInfo
	 * @param quesId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QuesFile> listPagedQuesFile(UserInfo userInfo, Integer quesId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select * from (");
		sql.append("\n  select a.comId,b.id,b.filename as orgFileName,b.uuid as orgFileUuid,a.recordCreateTime as upTime,'ques' type,a.id sourceId,a.quesId,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId,a.original ");
		sql.append("\n  from quesFile a  inner join upfiles b on a.original=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id  ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		sql.append("\n  union all ");
		sql.append("\n  select  a.comId,b.id, b.filename as orgFileName,b.uuid as orgFileUuid,a.recordCreateTime as upTime,'ans' type,a.id sourceId,a.quesId,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId,a.original ");
		sql.append("\n  from ansFile a inner join upfiles b on a.original=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		sql.append("\n  union all ");
		sql.append("\n  select  a.comId,b.id, b.filename as orgFileName,b.uuid as orgFileUuid,a.recordCreateTime as upTime,'talk' type,a.id sourceId,a.quesId,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId,a.upfileId original ");
		sql.append("\n  from qasTalkFile a inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		sql.append("\n  )b where 1=1 ");
		return this.pagedQuery(sql.toString(), " b.id desc", args.toArray(), QuesFile.class);
	}
	
	/**
	 * 文件总数
	 * @param userInfo
	 * @param quesId
	 * @return
	 */
	public Integer countFile(Integer comId, Integer quesId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select count(1) from (");
		sql.append("\n  select b.id,b.filename as orgFileName,b.uuid as orgFileUuid,a.recordCreateTime as upTime,'ques' type,a.id sourseId,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId,a.original ");
		sql.append("\n  from quesFile a  inner join upfiles b on a.original=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id  ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  union all ");
		sql.append("\n  select b.id, b.filename as orgFileName,b.uuid as orgFileUuid,a.recordCreateTime as upTime,'ans' type,a.id sourseId,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId,a.original ");
		sql.append("\n  from ansFile a inner join upfiles b on a.original=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  union all ");
		sql.append("\n  select b.id, b.filename as orgFileName,b.uuid as orgFileUuid,a.recordCreateTime as upTime,'talk' type,a.id sourseId,");
		sql.append("\n  c.userName,c.gender,d.uuid,d.fileName as imgName,b.fileExt,a.userId,a.upfileId original ");
		sql.append("\n  from qasTalkFile a inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id ");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  )b where 1=1 ");
		return this.countQuery(sql.toString(),  args.toArray());
	}

	/**
	 * 该问题的所有回答附件
	 * @param comId
	 * @param quesId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnsFile> listAllQuesAnsFile(Integer comId, Integer quesId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select a.* from ansFile a where 1=1 ");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n  order by a.id desc ");
		return this.listQuery(sql.toString(), args.toArray(), AnsFile.class);
	}

	/**
	 * 原来有，但是现在没有的文件
	 * @param id 问题主键
	 * @param comId 企业编号
	 * @param fileIds 现有文件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QuesFile> listRemoveQuesFile(Integer id, Integer comId,
			Integer[] fileIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n  select * from (");
		//原来的
		sql.append("\n  select a.*  from quesFile a  where 1=1 ");
		this.addSqlWhere(id, sql, args, " and a.quesId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		
		sql.append("\n minus ");
		//现在的
		sql.append("\n  select a.*  from quesFile a  where 1=1 ");
		this.addSqlWhere(id, sql, args, " and a.quesId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n and a.original in (0");
		if(null!=fileIds && fileIds.length>0){
			for (int i=0;i<fileIds.length;i++) {
				Integer fileId = fileIds[i];
				sql.append(","+fileId);
			}
		}
		sql.append("\n )");
		sql.append("\n  )b where 1=1 ");
		return this.listQuery(sql.toString(), args.toArray(), QuesFile.class);
	}

	

	/**
	 * 待删除的讨论
	 * @param comId 企业编号
	 * @param id 讨论主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnsTalk> listAnsTalkForDel(Integer comId, Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ansTalk where comid=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(id);
		return this.listQuery(sql.toString(), args.toArray(), AnsTalk.class);
	}
	/**
	 * 删除讨论
	 * @param id 讨论的主键
	 * @param comId 企业编号
	 */
	public void delQasTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ansTalk a where a.comid =? and a.id in \n");
		sql.append("(select id from ansTalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 回答的讨论加附件
	 * @param comId 企业编号
	 * @param quesId 问题的主键
	 * @param ansId 回答的主键
	 * @param ansTalkId 讨论的主键
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List<QasTalkFile> listQasTalkFile(Integer comId, Integer quesId,
			Integer ansId, Integer ansTalkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.comid,a.quesId,a.ansId,a.talkId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext ,  \n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from qasTalkFile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comid =?");
		this.addSqlWhere(quesId, sql, args, " and a.quesId =?");
		this.addSqlWhere(ansId, sql, args, " and a.ansId =?");
		this.addSqlWhere(ansTalkId, sql, args, " and a.talkId =?");
		sql.append("order by a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), QasTalkFile.class);
	}
	/**
	 * 获取问答答案描述为其创建索引
	 * @param comId
	 * @param quesId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Answer> listAnswer4Index(Integer comId,Integer quesId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.content,b.username from answer a \n");
		sql.append("inner join userInfo b on a.userid = b.id \n");
		sql.append("where a.comid =? and a.quesid = ? \n");
		args.add(comId);
		args.add(quesId);
		return this.listQuery(sql.toString(),args.toArray(),Answer.class);
	}
	/**
	 * 获取问答答案讨论为其创建索引
	 * @param comId
	 * @param quesId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnsTalk> listVoteTalk4Index(Integer comId,Integer quesId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.talkContent,b.username as talkerName from ansTalk a \n");
		sql.append("inner join userInfo b on a.talker = b.id \n");
		sql.append("where a.comid =? and a.quesid = ? \n");
		args.add(comId);
		args.add(quesId);
		return this.listQuery(sql.toString(),args.toArray(),AnsTalk.class);
	}
	/**
	 * 获取个人权限下排列前的N个问题集合数据
	 * @param userInfo
	 * @param rowNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Question> firstNQasList(UserInfo userInfo,Integer rowNum) {
		StringBuffer sql = new StringBuffer("select * from (\n");
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from (")
		.append("\n select a.id,a.cnans,a.recordcreatetime,a.comid,a.title,a.state,a.userid,b.userName,c.content as cnAnsContent,e.uuid imgUuid,e.filename imgName,b.gender, ")
		.append("\n case when atten.id is null then 0 else 1 end attentionState,")
		.append("\n c.recordcreatetime as ansDate,d.userName as ansUserName,")
		
		.append("case when(\n")
		.append("select count(*) from \n")
		.append(" todayworks today where a.comid = today.comid and a.id = today.busid \n")
		.append(" and today.bustype='"+ConstantInterface.TYPE_QUES+"' and today.userId="+userInfo.getId()+" and today.isclock=0\n")
		
		.append("and today.readState=0\n")
		.append(")=0 then 1 else 0 end as isread\n")
		
		.append("\n from (")
		//范围是所有人
		.append("\n  select a.id,a.cnans,a.recordcreatetime,a.comid,a.userid,a.title,a.state from  question a")
		.append("\n  where a.delstate=0 and  a.comid="+userInfo.getComId()+"");
		sql.append("\n )a inner join userinfo b on a.userid=b.id");
		sql.append("\n inner join userorganic bb on a.comId=bb.comid and a.userid=bb.userid");
		sql.append("\n  left join upfiles e on bb.mediumHeadPortrait = e.id");
		sql.append("\n  left join answer c on a.comid=c.comid and a.cnans=c.id");
		sql.append("\n  left join userinfo d on c.userid=d.id");
		
		sql.append("\n  left join attention atten on a.comid=atten.comid and a.id=atten.busId and atten.userId="+userInfo.getId()+" and atten.busType="+ConstantInterface.TYPE_QUES);
		
		sql.append("\n where 1=1 ");
		sql.append("\n  ) a where 1=1 order by isread,a.id desc");
		sql.append(") where rownum < ? order by isread,recordcreatetime desc ");
		args.add(rowNum);
		return this.listQuery(sql.toString(), args.toArray(),Question.class);
	}

	/**
	 * 查询该模块相同的附件
	 * @param comId 企业号
	 * @param quesId 模块主键
	 * @param upfileId 附件主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QuesFile> listQasSimUpfiles(Integer comId, Integer quesId,
			Integer upfileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//问答模块附件
		sql.append("select a.comId,a.quesId,a.original  from quesFile a \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.original=?");
		sql.append("\n union all \n");
		//回答附件
		sql.append("select  a.comId,a.quesId,a.original from ansFile a\n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.original=?");
		sql.append("\n union all \n");
		//评论附件
		sql.append("select  a.comId,a.quesId,a.upfileId original from qasTalkFile a\n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(quesId, sql, args, " and a.quesId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		return this.listQuery(sql.toString(), args.toArray(), QuesFile.class);
	}
}
