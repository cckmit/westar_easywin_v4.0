package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.DemandTalk;
import com.westar.base.model.TaskTalk;
import com.westar.base.model.TaskTalkUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.BaseTalk;
import com.westar.base.pojo.BaseUpfile;

/**
 * 
 * 描述:需求管理的数据层
 * @author zzq
 * @date 2018年10月12日 下午1:37:01
 */
@Repository
public class DemandTalkDao extends BaseDao {

	/**
	 * 查询用于删除的留言
	 * @param sessionUser 当前操作人员
	 * @param talkId 留言主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DemandTalk> listTreeTalkForDel(UserInfo sessionUser, Integer talkId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		sql.append("select * from demandTalk where comid=? start with id=? connect by parentid = prior id");
		args.add(sessionUser.getComId());
		args.add(talkId);
		return this.listQuery(sql.toString(), args.toArray(), DemandTalk.class);
	}
	
	/**
	 * 根据任务主键查询其下的讨论信息
	 * @param taskId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BaseTalk> listDemandTalk(Integer demandId,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select connect_by_isleaf as isLeaf,PRIOR a.speaker as pSpeaker,\n");
		sql.append("PRIOR b.username as pSpeakerName,a.*,b.username as speakerName,a.demandId busId \n");
		sql.append("from demandTalk a \n");
		sql.append("inner join userinfo b on a.speaker = b.id \n");
		sql.append("where a.comId=? and a.demandId = ? \n");
		sql.append("start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid \n");
		sql.append("order siblings by a.recordcreatetime desc,a.id");
		args.add(comId);
		args.add(demandId);
		return this.pagedQuery(sql.toString(), null, args.toArray(), BaseTalk.class);
	}
	/**
	 * 需求留言的附件
	 * @param comId 团队号
	 * @param demandId 需求主键
	 * @param talkId 留言主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BaseUpfile> listTalkFile(UserInfo sessionUser,Integer demandId, Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.demandId busId,a.talkId subBusId,a.userId,a.upfileId,b.filename,b.uuid \n");
		sql.append(",a.recordcreatetime,c.username ,b.fileext,\n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from demandTalkUpfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("where a.comid =? and a.demandId = ? \n");
		args.add(sessionUser.getComId());
		args.add(demandId);
		this.addSqlWhere(talkId, sql, args, " and a.talkId=?");
		sql.append("order by isPic asc, a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), TaskTalkUpfile.class);
	}
	/**
	 * 根据主键id查询讨论详情
	 * @param sessionUser 当前操作人员
	 * @param talkId
	 * @return
	 */
	public BaseTalk queryTalk(UserInfo sessionUser,Integer talkId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select d.speaker as pSpeaker,e.username as pSpeakerName\n");
		sql.append(",a.*,b.username as speakerName,a.demandId busId \n");
		sql.append("from demandTalk a  \n");
		sql.append("inner join userinfo b on a.speaker = b.id \n");
		sql.append("left join demandTalk d on a.parentid = d.id and a.comid = d.comid \n");
		sql.append("left join userinfo e on d.speaker = e.id \n");
		sql.append("where a.comId=? and a.id = ?");
		args.add(sessionUser.getComId());
		args.add(talkId);
		return (BaseTalk)this.objectQuery(sql.toString(), args.toArray(), BaseTalk.class);
	}

	

}
