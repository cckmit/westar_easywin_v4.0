package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.westar.base.model.FileClassify;
import com.westar.base.model.FileDetail;
import com.westar.base.model.FileTalk;
import com.westar.base.model.FileViewScopeDep;
import com.westar.base.model.MsgShareTalk;
import com.westar.base.model.MsgShareTalkUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.model.VideoTalk;
import com.westar.base.model.VideoTalkUpfile;
import com.westar.base.model.WeekRepTalk;
import com.westar.base.util.ConstantInterface;

@Repository
public class OnlineLearnDao extends BaseDao {
	/**
	 * 查询所有文件夹
	 * @param fileDetail
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileClassify> listVideoFileClassify(FileDetail fileDetail,UserInfo curUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select * from ( ");
		sql.append("\n select a.*,c.username,c.gender as userGender ,d.uuid as userSmlImgUuid,d.filename userSmlImgName");
		sql.append("\n from fileClassify a  left join userinfo c on  a.userid = c.id");
		sql.append("\n inner join userOrganic cc on c.id =cc.userId and a.comid=cc.comid");
		sql.append("\n left join upfiles d on cc.mediumHeadPortrait = d.id");
		sql.append("\n where 1=1  and a.type = '"+ConstantInterface.TYPE_LEARN+"'" );
		this.addSqlWhere(fileDetail.getClassifyId(), sql, args, " and a.parentId=?");
		this.addSqlWhere(fileDetail.getComId(), sql, args, " and a.comId=? and a.type = '"+ConstantInterface.TYPE_LEARN+"' ");
		sql.append("\n )a where 1=1 " );
		
		this.addSqlWhere(fileDetail.getFileName(), sql, args, " and instr(a.typeName,?,1)>0 ");
		//查询创建时间段
		this.addSqlWhere(fileDetail.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(fileDetail.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		sql.append("\n order by a.id  ");
		return this.listQuery(sql.toString(), args.toArray(), FileClassify.class);
	}
	/**
	 * 文件夹下所能查看的视频
	 * @param fileDetail
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileDetail> listVideoFile(FileDetail fileDetail,UserInfo userInfo){
		
		String searchAll = fileDetail.getSearchAll();
		String searchMe = fileDetail.getSearchMe();
		String fileName = fileDetail.getFileName();
		
		String startTime = fileDetail.getStartDate();
		String endTime = fileDetail.getEndDate();
		
		//有查询条件的时候不显示文件夹
		//查询条件全部为空则可以查询文件夹
		boolean searchFolderState = StringUtils.isEmpty(searchAll) 
									&& StringUtils.isEmpty(searchMe)
									&& StringUtils.isEmpty(fileName)
									&& StringUtils.isEmpty(startTime)
									&& StringUtils.isEmpty(endTime) ;
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("\n select a.id,a.recordCreateTime,a.fileId,a.recordCreateTime fileDate,a.fileDescribe,a.userId,a.moduleType");
		sql.append("\n	,b.uuid,b.fileName,b.filepath,b.sizeM,b.fileExt,c.username");
		sql.append("\n from  fileDetail a");
		sql.append("\n inner join upfiles b on a.fileId = b.id and a.comId = b.comid  ");
		sql.append("\n left join userinfo c on a.userId = c.id");
		
		sql.append("\n where 1=1  and a.comId = ? and a.moduleType = '"+ConstantInterface.TYPE_LEARN+"'");
		args.add(userInfo.getComId());
		
		sql.append("\n and ( a.pubState = 1 or ( ");//公开的
		sql.append("\n 		a.pubState = 0 and a.userId=? ");//私有的
		args.add(userInfo.getId());
		sql.append("\n 			or exists( ");//私有的
		sql.append("\n 		 		select id from fileShare where a.id=fileShare.fileDetailId and fileShare.shareId=? ");
		args.add(userInfo.getId());
		sql.append("\n 			)");
		sql.append("\n 		)");
		sql.append("\n 	) ");
		

		if(!searchFolderState){
			sql.append("\n and a.classifyId in (");
			sql.append("\n select a.id from fileClassify a");
			sql.append("\n start with a.parentid="+fileDetail.getClassifyId()+" connect by prior a.id = a.parentid");
			sql.append("\n )");
		}else{
			sql.append("\n and a.classifyId=?");
			args.add(fileDetail.getClassifyId());
		}
		
		
		//文件类型
		if(StringUtils.isNotEmpty(searchAll)){
			String fileType = "";
			switch (Integer.parseInt(searchAll)) {
			case 31://Office文档
				fileType +=",'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx'";
				break;
			case 32://文本文档
				fileType +=",'txt'";
				break;
			case 33://PDF文档
				fileType +=",'pdf'";
				break;
			case 34://图片文档
				fileType +=",'gif', 'jpg', 'jpeg', 'png', 'bmp'";
				break;
			case 35://音频文档
				fileType +=",'mp3'";
				break;
			case 36://视频文档
				fileType +=",'avi', 'wma', 'rmvb', 'rm', 'flash', 'flv', 'mp4', 'mid', '3gp'";
				break;
			case 37://其他类型
				fileType +=",'rar', 'zip'";
				break;
			default:
				break;
			}
			if(fileType.length()>0){
				fileType = fileType.substring(1,fileType.length());
				
			}
			sql.append("\n and  lower(b.fileExt) in ("+fileType+") ");
		}
		
		if(null!=fileDetail.getSearchMe()&& "22".equals(fileDetail.getSearchMe())){//他人上传
			this.addSqlWhere(userInfo.getId(), sql, args, " and a.userid<>?");
		}else if(null!=fileDetail.getSearchMe()&& "21".equals(fileDetail.getSearchMe())){//自己上传
			this.addSqlWhere(userInfo.getId(), sql, args, " and a.userid=?");
		}
		//创建日期
		this.addSqlWhere(fileDetail.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(fileDetail.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		//文件名
		this.addSqlWhere(fileDetail.getFileName(), sql, args, " and instr(b.fileName,?,1)>0 ");
		return this.pagedQuery(sql.toString(), " a.id desc",args.toArray(), FileDetail.class);
		
	}
	/**
	 * 文件部门查看范围
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public List<FileViewScopeDep> listScopeDep(Integer id, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select a.*,b.depName from FileViewScopeDep a ");
		sql.append("\n left join department b on a.depId = b.id  ");
		sql.append("\n where a.fileDetailId = ? and a.comId = ?  ");
		args.add(id);
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(), FileViewScopeDep.class);
	}
	
	
	/**
	 * 视频讨论分页
	 * @param fileId
	 * @param comId
	 * @return
	 */
	public List<VideoTalk> listPagedVideoTalk(Integer fileId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comid,a.content,a.speaker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from videoTalk a inner join userinfo b on a.speaker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join videoTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(fileId, sql, args, " and a.fileId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.pagedQuery(sql.toString(),null ,args.toArray(), VideoTalk.class);
	}
	
	/**
	 * 讨论附件
	 * @param comId
	 * @param fileId
	 * @param id
	 * @return
	 */
	public List<VideoTalkUpfile> listVideoTalkFile(Integer comId, Integer fileId, Integer videoTalkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.fileId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext, \n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from videoTalkUpfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comid =? and a.fileId = ? and a.videoTalkId=?\n");
		args.add(comId);
		args.add(fileId);
		args.add(videoTalkId);
		sql.append("order by isPic asc,a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), VideoTalkUpfile.class);
	}
	
	/**
	 * 根据id查询详情
	 * @param id
	 * @param comId
	 * @return
	 */
	public VideoTalk getVideoTalk(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.fileId,a.recordCreateTime,a.id,a.parentid,a.comid,a.content,a.speaker,a.ptalker,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from videoTalk a inner join userinfo b on a.speaker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comID=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join videoTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return (VideoTalk) this.objectQuery(sql.toString(), args.toArray(), VideoTalk.class);
	}
	
	/**
	 * 查询模块下是否有相同的附件
	 * @param comId
	 * @param fileId
	 * @param upfileId
	 * @return
	 */
	public List<VideoTalkUpfile> listVideoSimUpfiles(Integer comId,
			Integer fileId, Integer upfileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//留言附件
		sql.append("select  a.comId,a.fileId,a.upfileId from videoTalkUpfile a\n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(fileId, sql, args, " and a.fileId=?");
		this.addSqlWhere(upfileId, sql, args, " and a.upfileId=?");
		return this.listQuery(sql.toString(), args.toArray(),VideoTalkUpfile.class);
	}
	
	/**
	 * 待删除的讨论
	 * @param comId
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VideoTalk> listVideoTalkForDel(Integer comId, Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from videoTalk where comid=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(id);
		return this.listQuery(sql.toString(), args.toArray(), VideoTalk.class);
	}
	
	/**
	 * 删除当前节点及其子节点回复
	 * @param id
	 * @param comId
	 */
	public void delVideoTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from videoTalk a where a.comid =? and a.id in \n");
		sql.append("(select id from videoTalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	
	/**
	 * 讨论子节点提高一级
	 * @param id
	 * @param comId
	 */
	public void updateVideoTalkParentId(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		WeekRepTalk weekRepTalk = (WeekRepTalk) this.objectQuery(WeekRepTalk.class, id);
		if(null!=weekRepTalk && -1==weekRepTalk.getParentId()){//父节点为-1时将妻哪一个说话人设为空
			sql.append("update videoTalk set parentId=(select c.parentid \n");
			sql.append("from videoTalk c \n");
			sql.append("where c.id=?),ptalker=null where parentid = ? and comId = ?\n");
			args.add(id);
			args.add(id);
			args.add(comId);
			
		}else{//删除自己,将子节点提高一级
			sql.append("update videoTalk set parentId=(select c.parentid \n");
			sql.append("from videoTalk c \n");
			sql.append("where c.id=?) where parentid = ? and comId = ? \n");
			args.add(id);
			args.add(id);
			args.add(comId);
		}
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	
	/**
	 * 留言查看更多
	 * @param fileId
	 * @param comId
	 * @param minId
	 * @param pageSize
	 * @return
	 */
	public List<VideoTalk> nextPageSizeMsgTalks(Integer fileId, Integer comId, Integer minId, Integer pageSize) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select * from(select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comid,a.content,a.speaker,a.ptalker,a.fileId ,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName, e.content as pcontent");
		sql.append("\n  from videoTalk a inner join userinfo b on a.speaker=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id ");
		sql.append("\n  left join userinfo d on a.ptalker=d.id ");
		sql.append("\n  left join videoTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n where 1=1 and a.id < ?");
		args.add(minId);
		this.addSqlWhere(fileId, sql, args, " and a.fileId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id)");
		sql.append("\n where rownum <= ?");
		args.add(pageSize);
		return this.listQuery(sql.toString() ,args.toArray(), VideoTalk.class);
	}
	
}
