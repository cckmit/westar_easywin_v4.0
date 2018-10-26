package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.MsgShare;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.util.DateTimeUtil;

@Repository
public class ViewRecordDao extends BaseDao {

	/**
	 * 取得查看记录
	 * @param userInfo
	 * @param viewRecord
	 * @return
	 */
	public ViewRecord getViewRecord(ViewRecord viewRecord) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*");
		sql.append("\n from viewRecord a ");
		sql.append("\n where 1=1  and a.comId=? and a.userId=? and a.busId=? and a.busType=?");
		args.add(viewRecord.getComId());
		args.add(viewRecord.getUserId());
		args.add(viewRecord.getBusId());
		args.add(viewRecord.getBusType());
		return (ViewRecord) this.objectQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}
	
	/**
	 * 修改查看记录
	 * @param viewRecord
	 */
	public void updateViewRecord(ViewRecord viewRecord){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update viewRecord a set a.recordcreatetime= ");
		sql.append("'"+DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss)+"'");
		sql.append(" where a.comId=? and a.userId=? and a.busId=? and a.busType=?");
		args.add(viewRecord.getComId());
		args.add(viewRecord.getUserId());
		args.add(viewRecord.getBusId());
		args.add(viewRecord.getBusType());
		this.excuteSql(sql.toString(),args.toArray());
	}
	/**
	 * 浏览的人员集合
	 * @param userInfo 当前操作人员
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ViewRecord> listViewRecord(UserInfo userInfo, String busType,
			Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select a.*,c.gender,c.userName,d.uuid imguuid,d.filename imgname");
		sql.append("\n from viewRecord a inner join userorganic b on a.comId=b.comId and a.userId=b.userId");
		sql.append("\n inner join userinfo c on a.userId=c.id");
		sql.append("\n left join upfiles d on b.comId=d.comId and b.mediumheadportrait=d.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and a.busId=?");
		this.addSqlWhere(busType, sql, args, " and a.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		sql.append("\n order  by a.recordcreatetime desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}
	
	/**
	 * 查询分享范围未读的人员
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @return
	 */
	public List<ViewRecord> listViewRecordMsg(UserInfo userInfo, String busType, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname ");
		sql.append("\n from GROUPPERSION g ");
		sql.append("\n LEFT JOIN SHAREGROUP s on s.GRPID = g.GRPID ");
		sql.append("\n LEFT JOIN userInfo u on u.id = g.userInfoID ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and uo.comId = g.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and s.MSGID=?");
		
		//@人员
		sql.append("UNION select uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from msgShare m  ");
		sql.append("\n right JOIN shareUser s on s.msgId = m.id  ");
		sql.append("\n LEFT JOIN userInfo u on u.id = s.userId ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and uo.comId = m.comId  ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id ");
		
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and m.id=?");
		sql.append("\n order  by userId desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}
	
	/**
	 * 查询周报汇报范围内未阅读的人
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @return
	 */
	public List<ViewRecord> listViewRecordRep(UserInfo userInfo, String busType, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//汇报范围
		sql.append("select w.comId,uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from WEEKREPORT w ");
		sql.append("\n RIGHT JOIN WEEKVIEWER wv on wv.userId = w.REPORTERID ");
		sql.append("\n LEFT JOIN userInfo u on u.id = wv.VIEWERID ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and w.comId = uo.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and w.id=?");
		//直属领导
		sql.append("UNION  select w.comId,uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname ");
		sql.append("\n from WEEKREPORT w  ");
		sql.append("\n left JOIN IMMEDIATESUPER i on i.CREATOR = w.REPORTERID  ");
		sql.append("\n LEFT JOIN userInfo u on u.id = i.leader ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and w.comId = uo.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and w.id=?");

		//@人员
		sql.append("UNION select s.comId,uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname from weekreport m "
				+ "right JOIN weekReportShareUser s on s.weekReportId = m.id "
				+ "LEFT JOIN userInfo u on u.id = s.userId "
				+ "LEFT JOIN userOrganic uo on uo.userId = u.id and uo.comId = m.comId "
				+ "LEFT JOIN upFiles up on uo.mediumheadportrait = up.id ");
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and m.id=?");
		sql.append("\n order  by userId desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}

	/**
	 * 查询分享汇报范围内未阅读的人
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @return
	 */
	public List<ViewRecord> listViewDaily(UserInfo userInfo, String busType, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//汇报范围
		sql.append("select g.comId,uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from GROUPPERSION g ");
		sql.append("\n LEFT JOIN DAILYSHAREGROUP s on s.GRPID = g.GRPID ");
		sql.append("\n LEFT JOIN userInfo u on u.id = g.userInfoID ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and uo.comId = g.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");

		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and s.dailyId=?");
		//直属领导
		sql.append("UNION  select  w.comId,uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname ");
		sql.append("\n from daily w  ");
		sql.append("\n left JOIN IMMEDIATESUPER i on i.CREATOR = w.REPORTERID  ");
		sql.append("\n LEFT JOIN userInfo u on u.id = i.leader ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and w.comId = uo.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");

		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and w.id=?");

		//@人员
		sql.append("UNION select s.comId,uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname from daily m "
				+ "right JOIN dailyShareUser s on s.dailyId = m.id "
				+ "LEFT JOIN userInfo u on u.id = s.userId "
				+ "LEFT JOIN userOrganic uo on uo.userId = u.id and uo.comId = m.comId "
				+ "LEFT JOIN upFiles up on uo.mediumheadportrait = up.id ");
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and m.id=?");
		sql.append("\n order  by userId desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}
	
	/**
	 * 查询项目分享范围内未读的人员
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @return
	 */
	public List<ViewRecord> listViewRecordItem(UserInfo userInfo, String busType, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享范围
		sql.append("select i.comId,uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from ITEMSHARER s ");
		sql.append("\n left JOIN item  i on i.id = s.itemId ");
		sql.append("\n LEFT JOIN userInfo u on u.id = s.userId ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and i.comId = uo.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and i.id=?");
		
		sql.append("\n order  by userId desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}


	/**
	 * 查询产品分享范围内未读的人员
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @return
	 */
	public List<ViewRecord> listViewRecordPro(UserInfo userInfo, String busType, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享范围
		sql.append("select pro.comId,uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from proShare s ");
		sql.append("\n left JOIN product pro on pro.id = s.itemId ");
		sql.append("\n LEFT JOIN userInfo u on u.id = s.userId ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and pro.comId = uo.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");

		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and pro.id=?");

		sql.append("\n order  by userId desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}
	
	/**
	 * 查询客户分享范围内未读的人员
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ViewRecord> listViewRecordCus(UserInfo userInfo, String busType, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select s.comId,uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from customerSharer s ");
		sql.append("\n left JOIN customer  i on i.id = s.customerId ");
		sql.append("\n LEFT JOIN userInfo u on u.id = s.userId ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and i.comId = uo.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and i.id=?");
		
		sql.append("\n order  by userId desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}
	
	
	/**
	 * 查询公告分享范围内未读的人员
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @return
	 */
	public List<ViewRecord> listViewRecordAn(UserInfo userInfo, String busType, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享范围
		sql.append("select uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from ANNOUNSCOPEBYUSER s ");
		sql.append("\n left JOIN ANNOUNCEMENT  i on i.id = s.ANNOUNID ");
		sql.append("\n LEFT JOIN userInfo u on u.id = s.userId ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and i.comId = uo.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and i.id=?");
		
		//共享组
		sql.append("union select uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from userOrganic uo ");
		sql.append("\n left join ANNOUNSCOPEBYDEP s on s.depId = uo.depId and s.comId = uo.comId");
		sql.append("\n left JOIN ANNOUNCEMENT  i on i.id = s.ANNOUNID ");
		sql.append("\n left JOIN userInfo u on u.id = uo.userId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and i.id=?");
		
		sql.append("\n order  by userId desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}
	
	
	/**
	 * 日程查询未读人员
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @param showLeader
	 * @return
	 */
	public List<ViewRecord> listViewRecordSch(UserInfo userInfo, String busType, Integer busId, Boolean showLeader) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享范围
		sql.append("select uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from SCHEUSER w ");
		sql.append("\n LEFT JOIN SCHEDULE wv on wv.id = w.SCHEDULEID ");
		sql.append("\n LEFT JOIN userInfo u on u.id = w.userId ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and w.comId = uo.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and wv.id=?");
		
		if(showLeader) {
			//直属领导
			sql.append("UNION   select uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname ");
			sql.append("\n from SCHEDULE w  ");
			sql.append("\n left JOIN IMMEDIATESUPER i on i.CREATOR = w.userId  ");
			sql.append("\n LEFT JOIN userInfo u on u.id = i.leader ");
			sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and w.comId = uo.comId ");
			sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
			
			sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
			this.addSqlWhere(busId, sql, args, " and v.busId=?");
			this.addSqlWhere(busType, sql, args, " and v.busType=?");
			this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
			sql.append(") ");
			this.addSqlWhere(busId, sql, args, "and w.id=?");
		}
		sql.append("\n order  by userId desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}
	
	/**
	 * 在线学习查询未读人员
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @return
	 */
	public List<ViewRecord> listViewRecordVi(UserInfo userInfo, String busType, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//共享部门
		sql.append("select uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from userOrganic uo ");
		sql.append("\n left join FILEVIEWSCOPEDEP s on s.depId = uo.depId and s.comId = uo.comId");
		sql.append("\n left JOIN FILEDETAIL  i on i.id = s.FILEDETAILID ");
		sql.append("\n left JOIN userInfo u on u.id = uo.userId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and i.fileId=?");
		
		//共享组
		sql.append("\n union select uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from userOrganic uo ");
		sql.append("\n left join GROUPPERSION g on g.userInfoID = uo.userId and g.comId = uo.comId");
		sql.append("\n left join FILEVIEWSCOPE s on s.grpId = g.grpId and s.comId = g.comId");
		sql.append("\n left JOIN FILEDETAIL  i on i.id = s.FILEDETAILID ");
		sql.append("\n left JOIN userInfo u on u.id = uo.userId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and i.fileId=?");
		
		//分享范围
		sql.append("\n union select uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from FILESHARE fs ");
		sql.append("\n left JOIN FILEDETAIL  i on i.id = fs.FILEDETAILID ");
		sql.append("\n left JOIN userInfo u on u.id = fs.SHAREID ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and i.comId = uo.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and i.fileId=?");
		
		sql.append("\n order  by userId desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}
	
	/**
	 * 会议查询未读人员
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @return
	 */
	public List<ViewRecord> listViewRecordMeetings(UserInfo userInfo, String busType, Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享范围
		sql.append("select uo.userId,u.userName,u.gender,up.uuid imguuid,up.filename imgname");
		sql.append("\n from meetLearn w ");
		sql.append("\n LEFT JOIN meeting wv on wv.id = w.meetingId ");
		sql.append("\n LEFT JOIN userInfo u on u.id = w.userId ");
		sql.append("\n LEFT JOIN userOrganic uo on uo.userId = u.id and w.comId = uo.comId ");
		sql.append("\n LEFT JOIN upFiles up on uo.mediumheadportrait = up.id  ");
		
		sql.append("\n where uo.userId not in ( SELECT v.userId from viewRecord v where 1=1 ");
		this.addSqlWhere(busId, sql, args, " and v.busId=?");
		this.addSqlWhere(busType, sql, args, " and v.busType=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and v.comId=?");
		sql.append(") ");
		this.addSqlWhere(busId, sql, args, "and wv.id=?");
		sql.append("\n order  by userId desc ");
		return this.listQuery(sql.toString(), args.toArray(), ViewRecord.class);
	}
}
