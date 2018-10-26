package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.Talk;
import com.westar.base.model.TalkUpfile;
import com.westar.base.util.ConstantInterface;

@Repository
public class TalkDao extends BaseDao {
	/************************************************讨论*****************************************/
	/**
	 * 获取前五条讨论
	 * @param busId
	 * @param comId
	 * @param busType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Talk> listFiveTalk(Integer busId, Integer comId,String busType){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("select * from (");
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.busId,a.parentid,a.comid,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from Talk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join Talk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(busId, sql, args, " and a.busId=?");
		this.addSqlWhere(busType, sql, args, " and a.busType=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		sql.append(") where rownum  <=5 ");
		return this.pagedQuery(sql.toString(),null ,args.toArray(), Talk.class);
	}
	
	/**
	 * 获取讨论
	 * @param busId
	 * @param comId
	 * @param busType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Talk> listPagedTalk(Integer busId, Integer comId,String busType){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.busId,a.parentid,a.comid,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from Talk a inner join userinfo b on a.talker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join Talk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(busId, sql, args, " and a.busId=?");
		this.addSqlWhere(busType, sql, args, " and a.busType=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.pagedQuery(sql.toString(),null ,args.toArray(), Talk.class);
	}
	
	/**
	 * 获取回复的回复信息
	 * @param Talk
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Talk> getTalkChild(Talk talk)  {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.id from talk a");
		sql.append("\n where 1=1");
		this.addSqlWhere(talk.getBusId(), sql, args, " and a.bustId=?");
		this.addSqlWhere(talk.getComId(), sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid="+talk.getId()+" CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), Talk.class);
	}
	
	/**
	 * 获取单一讨论
	 * @param talkId 讨论主键
	 * @param comId 公司编号
	 * @return
	 */
	public Talk getTalk(Integer talkId, Integer comId,String busType) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.busId,a.recordCreateTime,a.id,a.parentid,a.comid,a.content,a.talker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from talk a inner join userinfo b on a.talker=b.id");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join Talk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(talkId, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		this.addSqlWhere(busType, sql, args, " and a.busType=? ");
		return (Talk) this.objectQuery(sql.toString(), args.toArray(), Talk.class);
	}
	
	/**
	 * 删除当前,将子节点提高一级
	 * @param talkId
	 * @param comId
	 */
	public void updateTalkParentId(Integer talkId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		Talk talk = (Talk) this.objectQuery(Talk.class, talkId);
		if(null!=talk && -1==talk.getParentId()){//父节点为-1时将前一个说话人设为空
			sql.append("update talk set parentId=(select c.parentid \n");
			sql.append("from talk c \n");
			sql.append("where c.id=?),ptalker=null where parentid = ? and comId = ?\n");
			args.add(talkId);
			args.add(talkId);
			args.add(comId);
			
		}else{//删除自己,将子节点提高一级
			sql.append("update talk set parentId=(select c.parentid \n");
			sql.append("from talk c \n");
			sql.append("where c.id=?) where parentid = ? and comId = ? \n");
			args.add(talkId);
			args.add(talkId);
			args.add(comId);
		}
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 待删除的讨论
	 * @param comId 企业编号
	 * @param talkId 讨论主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Talk> listTalkForDel(Integer comId, Integer talkId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from talk where comid=? and bustype = ?  start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(busType);
		args.add(talkId);
		return this.listQuery(sql.toString(), args.toArray(), Talk.class);
	}
	/**
	 * 删除讨论
	 * @param talkId 讨论主键
	 * @param comId 企业编号
	 */
	public void delTalk(Integer talkId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from Talk a where a.comid =? and a.id in \n");
		sql.append("(select id from Talk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(talkId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 获取投票讨论为其创建索引
	 * @param comId
	 * @param Id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Talk> listTalk4Index(Integer comId,Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.content,b.username as talkerName from Talk a \n");
		sql.append("inner join userInfo b on  a.talker = b.id \n");
		sql.append("where a.comid =? and a.Id = ? \n");
		args.add(comId);
		args.add(talkId);
		return this.listQuery(sql.toString(),args.toArray(),Talk.class);
	}

	/************************************************讨论附件*****************************************/
	/**
	 * 查询模块是否相同的附件
	 * @param comId
	 * @param busId
	 * @param upfileId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TalkUpfile> listSimUpfiles(Integer comId, Integer busId,Integer upfileId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//投票讨论附件
		sql.append("select a.comId,a.busId,a.upfileId  from talkUpfile a \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.busId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		this.addSqlWhere(busType, sql, args, " and a.busType=?");
		return this.listQuery(sql.toString(), args.toArray(), TalkUpfile.class);
	}
	/**
	 * 讨论附件(分页)
	 * @param comId
	 * @param Id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TalkUpfile> listPagedTalkFiles(Integer comId, Integer busId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("\n  select a.id,a.comid,a.busId,b.filename,b.uuid ,c.userName as creatorName,a.recordCreateTime,c.gender,");
		sql.append("\n  a.upfileId,d.uuid as userUuid,d.fileName as userFileName,a.userId,b.fileExt ");
		sql.append("\n  from talkUpfile a  inner join upfiles b on a.upfileId=b.id ");
		sql.append("\n  inner join userInfo c on a.userId=c.id  ");
		sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
		sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
		sql.append("\n  where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and a.busId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busType, sql, args, " and a.busType=?");
		if(busType.equals(ConstantInterface.TYPE_ANNOUNCEMENT)){
			sql.append("\n union all");
			sql.append("\n  select a.id,a.comid,a.busId,b.filename,b.uuid ,c.userName as creatorName,a.recordCreateTime,c.gender,");
			sql.append("\n  a.upfileId,d.uuid as userUuid,d.fileName as userFileName,a.userId,b.fileExt ");
			sql.append("\n  from announUpfile a  inner join upfiles b on a.upfileId=b.id ");
			sql.append("\n  inner join userInfo c on a.userId=c.id  ");
			sql.append("\n  inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId");
			sql.append("\n  left join upfiles d on cc.mediumHeadPortrait=d.id");
			sql.append("\n  where 1=1 ");
			this.addSqlWhere(busId, sql, args, " and a.busId=?");
			this.addSqlWhere(comId, sql, args, " and a.comId=?");
		}
		sql.append(") where 1=1 "); 
		return this.pagedQuery(sql.toString()," recordCreateTime desc", args.toArray(), TalkUpfile.class);
	}
	/**
	 * 讨论附件
	 * @param comId
	 * @param Id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TalkUpfile> listTalkFiles(Integer comId, Integer busId,Integer talkId,String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.busId,a.userId,a.talkId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, \n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from talkUpfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comid =? and a.busId = ?  and  a.busType = ?\n");
		args.add(comId);
		args.add(busId);
		args.add(busType);
		this.addSqlWhere(talkId, sql, args, "\n and a.talkId=? ");
		sql.append("order by isPic asc, a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), TalkUpfile.class);
	}
}