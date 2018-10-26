package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.MeetSummary;
import com.westar.base.model.SummaryFile;
import com.westar.base.model.UserInfo;

@Repository
public class MeetSummaryDao extends BaseDao {
	
	/**
	 * 查询会议纪要附件
	 * @param meetingId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SummaryFile> listSummaryFile(Integer meetingId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.meetingId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,");
		sql.append("\n c.username as creatorName,b.fileext,");
		sql.append("\n case when b.fileext in ('gif', 'jpg','jpeg', 'png', 'bmp')then 1 else 0 end as isPic");
		sql.append("\n from summaryFile a inner join upfiles b on a.comid = b.comid and a.upfileId = b.id");
		sql.append("\n left join userinfo c on  a.userid = c.id");
		sql.append("\n where a.comid =? and a.meetingId = ? \n");
		args.add(comId);
		args.add(meetingId);
		sql.append(" order by isPic asc, a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), SummaryFile.class);
	}

	/**
	 * 查询会议纪要信息
	 * @param userInfo 当前操作人员
	 * @param meetingId 会议主键
	 * @return
	 */
	public MeetSummary queryMeetSummary(UserInfo userInfo, Integer meetingId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.summary,a.flowId,a.actInstaceId,a.spState,a.meetingId");
		sql.append("\n from meetSummary a");
		sql.append("\n where 1=1 and a.comId=? and a.meetingId=?");
		args.add(userInfo.getComId());
		args.add(meetingId);
		return (MeetSummary) this.objectQuery(sql.toString(), args.toArray(), MeetSummary.class);
	}
	/**
	 * 查询会议纪要关联的会议主键信息
	 * @param userInfo
	 * @param summaryId
	 * @return
	 */
	public MeetSummary querySummaryForMeet(UserInfo userInfo, Integer summaryId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.meetingId,a.flowId,a.actInstaceId,a.spState");
		sql.append("\n from meetSummary a");
		sql.append("\n where a.comId=? and a.id=?");
		args.add(userInfo.getComId());
		args.add(summaryId);
		return (MeetSummary) this.objectQuery(sql.toString(), args.toArray(), MeetSummary.class);
	}
	

}
