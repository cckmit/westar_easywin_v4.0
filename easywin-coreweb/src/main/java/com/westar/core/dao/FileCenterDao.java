package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.westar.base.model.FileClassify;
import com.westar.base.model.FileDetail;
import com.westar.base.model.FileTalk;
import com.westar.base.model.FileViewScope;
import com.westar.base.model.GroupPersion;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.StringUtil;

@Repository
public class FileCenterDao extends BaseDao {

	/**
	 * 已有的文件夹个数
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Integer getClassifyNum(Integer comId, Integer userId,String type) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(*) from fileClassify where 1=1");
		this.addSqlWhere(comId, sql, args, " and comid=?");
		this.addSqlWhere(type, sql, args, " and type=?");
		this.addSqlWhere(userId, sql, args, " and userId=?");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 导航栏
	 * @param fileDetail
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileClassify> listLeadLine(Integer comId,Integer classifyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from  (select a.*,rownum neworder  from fileClassify a ");
		sql.append("\n where 1=1 ");
		//企业编号
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(classifyId, sql, args, " start with a.id=? connect by prior a.parentid = a.id");
		sql.append(" order siblings by a.id");
		sql.append(" )a  order by a.neworder desc");
		return this.listQuery(sql.toString(), args.toArray(), FileClassify.class);
	}

	/**
	 * 验证文件夹的名称
	 * @param userInfo
	 * @param classifyId
	 * @param typeName
	 * @return
	 */
	public FileClassify checkDirName(UserInfo userInfo, Integer classifyId,String typeName) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from fileClassify a ");
		sql.append("\n where 1=1 ");
		//用户
		this.addSqlWhere(userInfo.getId(), sql, args, " and a.userId=?");
		//模块类型
		this.addSqlWhere(typeName, sql, args, " and a.typeName=?");
		//企业编号
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(classifyId, sql, args, " and a.parentid=?");
		return (FileClassify) this.objectQuery(sql.toString(), args.toArray(), FileClassify.class);
	}

	/**
	 * 当前文件夹下所有文件夹
	 * @param userId
	 * @param comId
	 * @param classifyId
	 */
	@SuppressWarnings("unchecked")
	public List<FileClassify> listAllDir(Integer userId, Integer comId, Integer classifyId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from fileClassify a ");
		sql.append("\n where 1=1 ");
		//用户
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		//企业编号
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(classifyId, sql, args, " start with a.id=? connect by prior a.id = a.parentid");
		sql.append(" order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), FileClassify.class);
		
	}

	/**
	 * 删除该分类的所有文件
	 * @param classifyId
	 * @param comId
	 */
	public void delAllFile(Integer classifyId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from fileDetail where comId="+comId+" and classifyId="+classifyId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 删除附件范围限制表fileViewScope
	 * @param filedetailid
	 * @param comId
	 */
	public void delFileViewScope(Integer filedetailid, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n delete from fileViewScope where comId="+comId+" and filedetailid="+filedetailid);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 将文件夹的级别提高一级
	 * @param comId
	 * @param parentId
	 * @param id
	 */
	public Integer updateDirLev(Integer comId, Integer parentId, Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update fileClassify set parentId="+(parentId==null?"-1":parentId)+" where comId="+comId+" and parentId="+id);
		return this.excuteSql(sql.toString(), args.toArray());
		
	}

	/**
	 * 将文件的级别提高一级
	 * @param comId
	 * @param parentId
	 * @param id
	 */
	public Integer updateFileLev(Integer comId, Integer parentId, Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update fileDetail set classifyId="+parentId+" where comId="+comId+" and classifyId="+id);
		return this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 防止重名
	 * @param comId
	 * @param parentId
	 * @param id
	 */
	public void updateChildName(Integer comId, Integer parentId, Integer id) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n update fileClassify set typeName = typeName||'(1)' where  comid="+comId+" and parentid="+id);
		sql.append("\n and typeName in ( select typeName from fileClassify where  comid="+comId+" and parentid="+parentId+" )");
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 文件夹列表树
	 * @param fileClassify
	 * @param preDirs 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileClassify> listTreeDirForSelect(FileClassify fileClassify, String preDirs) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//所有文件夹
		sql.append("\n select * from (");
		sql.append("\n select a.*,level from fileClassify a ");
		sql.append("\n where 1=1 ");
		//企业编号
		this.addSqlWhere(fileClassify.getComId(), sql, args, " and a.comId=?");
		
		sql.append("\n and ( a.pubState = 1 or ( ");//公开的
		sql.append("\n 			a.pubState = 0 and a.userId=?");//私有的
		args.add(fileClassify.getUserId());
		sql.append("\n 		) ");
		sql.append("\n 	) ");
		
		
		if(fileClassify.getType() != null && (fileClassify.getType() instanceof String ? !StringUtil.isBlank(fileClassify.getType().toString()) : true)){
			sql.append("\n  and a.type= ? ");
			args.add(fileClassify.getType());
		}else{
			sql.append("\n  and a.type <> '023' ");
		}
		sql.append("\n start with a.parentid=-1 connect by prior a.id = a.parentid");
		
		sql.append("\n ) a where 1=1 and a.id not in ( ");
		//当前文件夹以及子文件夹
		sql.append("\n	select a.id from fileClassify a");
		sql.append("\n	where 1=1 ");
		//企业编号
		this.addSqlWhere(fileClassify.getComId(), sql, args, " and a.comId=?");

		sql.append("\n  start with a.id in("+preDirs+") connect by prior a.id = a.parentid");
		sql.append("\n )  ");
		if(fileClassify.getType() != null && (fileClassify.getType() instanceof String ? !StringUtil.isBlank(fileClassify.getType().toString()) : true)){
			sql.append("\n  and a.type= ? ");
			args.add(fileClassify.getType());
		}else{
			sql.append("\n  and a.type <> '023' ");
		}
		sql.append("\n order by a.issys desc, a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), FileClassify.class);
	}

	/**
	 * 移动单个文件夹
	 * @param id
	 * @param parentId
	 * @param comId
	 */
	public void updateMovelDir(Integer id, Integer parentId, Integer comId) {
		StringBuffer sql = new StringBuffer();
		sql.append("\n update fileClassify set typeName = typeName||'(1)' where  comid="+comId+" and id="+id);
		sql.append("\n and typeName in ( select typeName from fileClassify where  comid="+comId+" and id="+parentId+" )");
		this.excuteSql(sql.toString(), null);
		sql = new StringBuffer("\n update fileClassify set parentId="+parentId+" where  comid="+comId+" and id="+id);
		this.excuteSql(sql.toString(), null);
	}

	/**
	 * 查询在文档中是否已经存在该文件
	 * @param moduleType 模块类别
	 * @param fileId 文件主键
	 * @param shareId 文件查看人主键
	 * @param comId 企业号
	 * @return
	 */
	public FileDetail getFileDetail(String moduleType,Integer fileId ,Integer shareId,Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from fileDetail a ");
		sql.append("\n where 1=1  ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(shareId, sql, args, " and a.shareId=?");
		this.addSqlWhere(fileId, sql, args, " and a.fileId=?");
		this.addSqlWhere(moduleType, sql, args, " and a.moduleType=?");
		return (FileDetail) this.objectQuery(sql.toString(), args.toArray(), FileDetail.class);
	}

	/**
	 * 指定文件的查看范围
	 * @param id
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileViewScope> listFileViewScope(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.grpName  from fileViewScope a ");
		sql.append("\n left join SelfGroup b on a.grpId = b.id ");
		sql.append("\n where 1=1  ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(id, sql, args, " and a.fileDetailId=?");
		sql.append("\n order by a.id asc  ");
		
		return this.listQuery(sql.toString(), args.toArray(), FileViewScope.class);
	}
	/**
	 * 取得指定文件的查看范围类型
	 * @param id
	 * @param comId
	 * @return
	 */
	public FileDetail getFileDetailScope(Integer id,  Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.fileExt,b.fileName,b.uuid");
		sql.append("\n from fileDetail a inner join upfiles b on a.fileId=b.id and a.comid=b.comid");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return (FileDetail) this.objectQuery(sql.toString(), args.toArray(), FileDetail.class);
	}

	/**
	 * 查询该文件所有已分类的集合
	 * @param id
	 * @param fileId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileDetail> listFileDetail(Integer fileId,
			Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from fileDetail a where moduleType='"+ConstantInterface.TYPE_FILE+"'");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(fileId, sql, args, " and a.fileId=?");
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), FileDetail.class);
	}

	/**
	 * 需要删除的
	 * @param id
	 * @param fileId
	 * @param comId
	 * @param scopeGroups
	 * @param userId 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GroupPersion> listGroupPersion(Integer id, Integer fileId,
			Integer comId, Integer[] scopeGroups, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from ( ");
		sql.append("\n select b.userinfoid from fileViewScope a ");
		sql.append("\n inner join grouppersion b on a.comid=b.comid and a.grpid=b.grpid");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(id, sql, args, " and a.fileDetailId=?");
		
		sql.append("\n minus");
		//除开现在选择的
		sql.append("\n select b.userinfoid from fileViewScope a ");
		sql.append("\n inner join grouppersion b on a.comid=b.comid and a.grpid=b.grpid");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n and a.grpid in (0");
		if(null!=scopeGroups && scopeGroups.length>0){
			for (int i=0;i<scopeGroups.length;i++) {
				sql.append(","+scopeGroups[i]);
			}
		}
		sql.append("\n ) ");
		sql.append("\n minus");
		//除开自己
		sql.append("\n select "+userId+" from dual");
		
		sql.append("\n ) a where 1=1 ");
		return this.listQuery(sql.toString(), args.toArray(), GroupPersion.class);
	}
	/**
	 * 获取附件的分享范围组
	 * @param comId
	 * @param fileId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SelfGroup> listFileGrpIdStr4Index(Integer comId,Integer fileId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select \n");
		sql.append("case a.scopetype \n");
		sql.append("when 0 then 'all' \n");
		sql.append("when 2 then CONCAT('self_',a.shareId) \n");
		sql.append("when 1 then b.grpid||'' end as grpIdStr \n");
		sql.append("from fileDetail a \n");
		sql.append("left join fileViewScope b on a.comid = b.comid and a.id = b.filedetailid \n");
		sql.append("where a.comid = ? and a.id =? \n");
		args.add(comId);
		args.add(fileId);
		return this.listQuery(sql.toString(),args.toArray(),SelfGroup.class);
	}
	/**
	 * 更具文件夹主键查选文件夹下的所有附件详细集合
	 * @param classifyId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileDetail> listFileDetailByFileClassify(Integer classifyId, Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.* from fileDetail a where a.comid = ? and a.classifyid = ?");
		args.add(comId);
		args.add(classifyId);
		return this.listQuery(sql.toString(),args.toArray(),FileDetail.class);
	}
	/**
	 * 查询指定的前n条数据
	 * @param fileDetail
	 * @param sessionUser 
	 * @param rowNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileDetail> firstNFileList(FileDetail fileDetail,UserInfo sessionUser, Integer rowNum){

		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from (");
		//所有文件夹
		sql.append("\n select a.*,u.username,f.fileName,f.fileExt,f.uuid,f.sizem,a.recordCreateTime fileDate ");
		sql.append("\n from fileDetail a ");
		sql.append("\n inner join userinfo u on a.userId=u.id ");
		sql.append("\n inner join upfiles f on a.fileId=f.id ");
		sql.append("\n where 1=1 ");
		//企业编号
		this.addSqlWhere(sessionUser.getComId(), sql, args," and a.comId=?");
		
		sql.append("\n and a.classifyId in (");
		sql.append("\n select a.id from fileClassify a");
		sql.append("\n start with a.parentid="+fileDetail.getClassifyId()+" connect by prior a.id = a.parentid");
		sql.append("\n )");
				
		sql.append("\n and a.moduleType <> ?");
		args.add(ConstantInterface.TYPE_LEARN);
		
		sql.append("\n and ( a.pubState = 1 or ( ");//公开的
		sql.append("\n 		a.pubState = 0 and a.userId=? ");//私有的
		args.add(sessionUser.getId());
		sql.append("\n 			or exists( ");//私有的
		sql.append("\n 		 		select id from fileShare where a.id=fileShare.fileDetailId and fileShare.shareId=? ");
		args.add(sessionUser.getId());
		sql.append("\n 			)");
		sql.append("\n 		)");
		sql.append("\n 	) ");
		sql.append("\n 	order by a.id desc ");
				
		sql.append(") where rownum <= ?");
		args.add(rowNum);
		return this.listQuery(sql.toString(), args.toArray(), FileDetail.class);
	
	}

	/**
	 * 取得附件信息
	 * @param id
	 * @return
	 */
	public FileDetail getFileDetail(Integer id, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id,a.recordCreateTime,a.comid,a.fileDescribe,a.scopeType,a.fileId,a.pubState,");
		sql.append("\n a.moduletype,b.fileExt,b.sizem,b.fileName,b.uuid,a.userId,u.userName");
		sql.append("\n from fileDetail a inner join upfiles b on a.fileId=b.id and a.comid=b.comid");
		sql.append("\n inner join userinfo u on a.userId=u.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		return (FileDetail) this.objectQuery(sql.toString(), args.toArray(), FileDetail.class);
	}

	/**
	 * 查询附件评论
	 * @param comId
	 * @param busId
	 * @param busType
	 * @param fileId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileTalk> listPagedFileTalks(Integer comId, Integer busId,
			String busType, Integer fileId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select connect_by_isleaf as isLeaf, a.recordCreateTime,a.id,a.parentid,a.comid,a.talkContent,a.userId,c.uuid as talkerSmlImgUuid,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName");
		sql.append("\n  from fileTalk a inner join userinfo b on a.userid=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join fileTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n  left join userinfo d on e.userid=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(fileId, sql, args, " and a.fileId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		this.addSqlWhere(busId, sql, args, " and a.busId=? ");
		this.addSqlWhere(busType, sql, args, " and a.busType=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.pagedQuery(sql.toString(),null ,args.toArray(), FileTalk.class);
	}
	/**
	 * 获取文档讨论集合
	 * @param comId
	 * @param fileId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileTalk> listFileTalksOfAll(Integer comId,Integer fileId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.talkContent,b.username as talkerName");
		sql.append("\n  from fileTalk a inner join userinfo b on a.userid=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join fileTalk e on a.parentid=e.id and a.comid=e.comid");
		sql.append("\n  left join userinfo d on e.userid=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(fileId, sql, args, " and a.fileId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid");
		sql.append("\n order siblings by a.recordcreatetime desc,a.id");
		return this.listQuery(sql.toString(), args.toArray(),FileTalk.class);
	}

	/**
	 * 删除附件评论
	 * @param id
	 * @param comId
	 */
	public void delFileTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from fileTalk a where a.comid =? and a.id in \n");
		sql.append("(select id from fileTalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());
		
	}

	/**
	 * 将子节点提高一级
	 * @param id 附件讨论的主键
	 * @param comId 企业号
	 */
	public void updateFileTalkParentId(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		FileTalk filetalk = (FileTalk) this.objectQuery(FileTalk.class, id);
		//附件讨论的父节点主键
		Integer parentId = -1;
		if(null!=filetalk){
			parentId = filetalk.getParentId();
		}
		sql.append("update filetalk set parentId=? where parentid = ? and comId = ?\n");
		args.add(parentId);
		args.add(id);
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
		
	}
	/**
	 * 取得附件留言
	 * @param id 留言主键
	 * @param comId 企业号
	 * @return
	 */
	public FileTalk getFileTalk(Integer id, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n  select a.recordCreateTime,a.id,a.parentid,a.comid,a.talkContent,a.userId,c.uuid as talkerSmlImgUuid,a.busId,a.busType,");
		sql.append("\n  c.filename as talkerSmlImgName,b.username as talkerName, b.gender as talkerGender,d.username as ptalkerName");
		sql.append("\n  from fileTalk a inner join userinfo b on a.userid=b.id ");
		sql.append("\n  inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId");
		sql.append("\n  left join upfiles c on bb.mediumHeadPortrait=c.id");
		sql.append("\n  left join fileTalk e on a.parentid=e.id and a.comid=e.comid ");
		sql.append("\n  left join userinfo d on e.userid=d.id");
		sql.append("\n where 1=1");
		this.addSqlWhere(id, sql, args, " and a.id=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=? ");
		return (FileTalk) this.objectQuery(sql.toString(), args.toArray(), FileTalk.class);
	}
	
	/**
	 * 查询其他评论是否有相同的附件
	 * @param comId 企业号
	 * @param busId 业务主键
	 * @param fileId 附件主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileDetail> listSimFile(Integer comId, Integer busId,String busType,
			Integer fileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.comId,a.id,a.fileId  from filedetail a \n");
		sql.append("where 1=1 \n");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(busId, sql, args, " and a.id=?");
		this.addSqlWhere(busType, sql, args, " and a.moduleType=?");
		this.addSqlWhere(fileId, sql, args, " and a.fileId=?");
		return this.listQuery(sql.toString(), args.toArray(), FileDetail.class);
	}

	/**
	 * 合并附件留言
	 * @param comId 企业号
	 * @param oldBusId 业务主键（原来的）
	 * @param newBusId 业务主键（后来的）
	 * @param busType 业务类型
	 */
	public void comPressFileTalk(Integer comId, Integer oldBusId,
			Integer newBusId, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并附件留言
		sql.append("update fileTalk set busId=? where comid=? and busId=? and busType=?");
		args.add(newBusId);
		args.add(comId);
		args.add(oldBusId);
		args.add(busType);
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 获取团队下所有附件
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Upfiles> listUpfilesOfAll(UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//文档
		sql.append("\n select '013'||'_'||a.id||'_'||a.fileid as key,a.fileid as id,a.id as busId,'013' as busType from fileDetail a where a.comid = ?  and a.moduleType='013' ");
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString(), args.toArray(), Upfiles.class);
	}
	/**
	 * 文档查看权限验证
	 * @param comId 团队主键
	 * @param fileDetailId 文档详情主键
	 * @param userId 验证人员主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileDetail> authorCheck(Integer comId,Integer fileDetailId,Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.userid from fileDetail a where a.comid=? and a.scopetype = 2 and a.id=? ");
		args.add(comId);
		args.add(fileDetailId);
		sql.append("\n union ");
		sql.append("\n select d.userinfoid as userid from fileDetail a inner join fileViewScope b on " +
				"a.comid = ? and a.comid = b.comid and a.id = b.filedetailid and a.scopetype = 1 and a.id=? ");
		args.add(comId);
		args.add(fileDetailId);
		sql.append("\n inner join selfGroup c on b.comid = c.comid and b.grpid = c.id inner join groupPersion d on " +
				"c.comid = d.comid and c.id= d.grpid and d.userinfoid= ? ");
		args.add(userId);
		sql.append("\n union ");
		sql.append("\n select b.userId from fileDetail a inner join userOrganic b on a.comid = b.comid " +
				"and a.comid = ? and a.scopetype = 0 and a.id=? and b.enabled = 1 and b.userId =? ");
		args.add(comId);
		args.add(fileDetailId);
		args.add(userId);
		sql.append("\n union ");
		sql.append("\n select a.shareId as userid from fileDetail a where a.comid = ? and a.id=? and a.shareId=?");
		args.add(comId);
		args.add(fileDetailId);
		args.add(userId);
		return this.listQuery(sql.toString(), args.toArray(), FileDetail.class);
	}
	
	/************************************zzq新加的方法************************************************/
	
	/**
	 * 查询当前所有的数据信息
	 * @param classifyId 分类主键
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileClassify> listTreeFolder(FileClassify fileClassify,UserInfo sessionUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//所有文件夹
		sql.append("\n select * from (");
		sql.append("\n select a.*,level from fileClassify a ");
		sql.append("\n where 1=1 ");
		
		sql.append("\n and ( a.pubState = 1 or ( ");//公开的
		sql.append("\n 			a.pubState = 0 and a.userId=?");//私有的
		sql.append("\n 			or exists(");
		sql.append("\n          	select id from fileDetail detail ");
		sql.append("\n          	where detail.classifyid=a.id and detail.pubstate=1");
		sql.append("\n          	union all");
		sql.append("\n          	select id from fileDetail detail ");
		sql.append("\n          	where detail.classifyid=a.id and detail.pubstate=0 and detail.userId=?");
		args.add(sessionUser.getId());
		sql.append("\n          	union all");
		sql.append("\n          	select detail.id from fileDetail detail");
		sql.append("\n          	inner join fileShare on detail.id=fileShare.Filedetailid");
		sql.append("\n          	where detail.classifyid=a.id and detail.pubstate=0 and fileShare.shareId=?");
		args.add(sessionUser.getId());
		sql.append("\n 			)");
		sql.append("\n 		) ");
		sql.append("\n 	) ");
		
		args.add(sessionUser.getId());
		
		//企业编号
		this.addSqlWhere(sessionUser.getComId(), sql, args," and a.comId=?");
		
		String type = fileClassify.getType();
		if(StringUtils.isNotEmpty(type) && ConstantInterface.TYPE_LEARN.equals(type)){
			this.addSqlWhere(fileClassify.getType(), sql, args, " and a.type=?");
		}else{
			this.addSqlWhere(ConstantInterface.TYPE_LEARN, sql, args, " and a.type<>?");
		}
		sql.append("\n start with a.parentid="+fileClassify.getId()+" connect by prior a.id = a.parentid");
		sql.append("\n order siblings by a.isSys desc, a.id desc");
		
		sql.append("\n ) a where 1=1 ");
		return this.listQuery(sql.toString(), args.toArray(), FileClassify.class);
	}
	/**
	 * 查询文件夹列表信息
	 * @param fileDetail
	 * @param sessionUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileClassify> listFileClassify(FileDetail fileDetail,UserInfo sessionUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//所有文件夹
		sql.append("\n select a.*,u.username from fileClassify a ");
		sql.append("\n inner join userinfo u on a.userId=u.id ");
		sql.append("\n where 1=1 ");
		sql.append("\n and ( a.pubState = 1 or ( ");//公开的
		sql.append("\n 		a.pubState = 0 and a.userId=? ");//私有的
		args.add(sessionUser.getId());
		sql.append("\n 		or exists(");
		sql.append("\n          select id from fileDetail detail ");
		sql.append("\n          where detail.classifyid=a.id and detail.pubstate=1");
		sql.append("\n          union all");
		sql.append("\n          select id from fileDetail detail ");
		sql.append("\n          where detail.classifyid=a.id and detail.pubstate=0 and detail.userId=?");
		args.add(sessionUser.getId());
		sql.append("\n          union all");
		sql.append("\n          select detail.id from fileDetail detail");
		sql.append("\n          inner join fileShare on detail.id=fileShare.Filedetailid");
		sql.append("\n          where detail.classifyid=a.id and detail.pubstate=0 and fileShare.shareId=?");
		args.add(sessionUser.getId());
		sql.append("\n 			)");
		sql.append("\n 		) ");
		sql.append("\n 	) ");
		
		String type = fileDetail.getModuleType();
		if(StringUtils.isNotEmpty(type) && ConstantInterface.TYPE_LEARN.equals(type)){
			this.addSqlWhere(type, sql, args, " and a.type=?");
		}else{
			this.addSqlWhere(ConstantInterface.TYPE_LEARN, sql, args, " and a.type<>?");
		}
		
		//企业编号
		this.addSqlWhere(sessionUser.getComId(), sql, args," and a.comId=?");
		
		sql.append("\n and a.parentid=?");
		args.add(fileDetail.getClassifyId());
		sql.append("\n order by a.issys desc,a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), FileClassify.class);
	}
	/**
	 * 查询文件列表信息
	 * @param fileDetail
	 * @param sessionUser
	 * @param searchFolderState 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileDetail> listPagedFileDetail(FileDetail fileDetail,
			UserInfo sessionUser) {
		
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
		//所有文件夹
		sql.append("\n select a.*,u.username,f.fileName,f.fileExt,f.uuid,f.sizem,a.recordCreateTime fileDate ");
		sql.append("\n from fileDetail a ");
		sql.append("\n inner join userinfo u on a.userId=u.id ");
		sql.append("\n inner join upfiles f on a.fileId=f.id ");
		sql.append("\n where 1=1 ");
		//企业编号
		this.addSqlWhere(sessionUser.getComId(), sql, args," and a.comId=?");
		
		if(!searchFolderState){
			sql.append("\n and a.classifyId in (");
			sql.append("\n select a.id from fileClassify a");
			sql.append("\n where 1=1");
			this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
			sql.append("\n start with a.parentid="+fileDetail.getClassifyId()+" connect by  a.id = prior a.parentid");
			sql.append("\n union all");
			sql.append("\n select "+fileDetail.getClassifyId()+" id from dual");
			sql.append("\n )");
		}else{
			sql.append("\n and a.classifyId=?");
			args.add(fileDetail.getClassifyId());
		}
				
		sql.append("\n and a.moduleType <> ?");
		args.add(ConstantInterface.TYPE_LEARN);
		
		sql.append("\n and ( a.pubState = 1 or ( ");//公开的
		sql.append("\n 		a.pubState = 0 and a.userId=? ");//私有的
		args.add(sessionUser.getId());
		sql.append("\n 			or exists( ");//私有的
		sql.append("\n 		 		select id from fileShare where a.id=fileShare.fileDetailId and fileShare.shareId=? ");
		args.add(sessionUser.getId());
		sql.append("\n 			)");
		sql.append("\n 		)");
		sql.append("\n 	) ");
		
		
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
			sql.append("\n and  lower(f.fileExt) in ("+fileType+") ");
		}
		
		if(null!=fileDetail.getSearchMe()&& "22".equals(fileDetail.getSearchMe())){//他人上传
			this.addSqlWhere(sessionUser.getId(), sql, args, " and a.userid<>?");
		}else if(null!=fileDetail.getSearchMe()&& "21".equals(fileDetail.getSearchMe())){//自己上传
			this.addSqlWhere(sessionUser.getId(), sql, args, " and a.userid=?");
		}
		//创建日期
		this.addSqlWhere(fileDetail.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(fileDetail.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		//文件名
		this.addSqlWhere(fileDetail.getFileName(), sql, args, " and (instr(f.fileName,?,1)>0 or instr(a.fileDescribe,?,1)>0)",2);
		return this.pagedQuery(sql.toString(), " a.id desc ", args.toArray(), FileDetail.class);
	}
	/**
	 * 查询文件列表信息
	* @param fileDetail
	* @param sessionUser
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<FileDetail> listPagedFileDetailForSelect(FileDetail fileDetail,
			UserInfo sessionUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//所有文件夹
		sql.append("\n select a.*,u.username,f.fileName,f.fileExt,f.uuid,f.sizem,a.recordCreateTime fileDate ");
		sql.append("\n from fileDetail a ");
		sql.append("\n inner join userinfo u on a.userId=u.id ");
		sql.append("\n inner join upfiles f on a.fileId=f.id ");
		sql.append("\n where 1=1 ");
		//企业编号
		this.addSqlWhere(sessionUser.getComId(), sql, args," and a.comId=?");
		
		sql.append("\n and a.moduleType <> ?");
		args.add(ConstantInterface.TYPE_LEARN);
		
		sql.append("\n and ( a.pubState = 1 or ( ");//公开的
		sql.append("\n 		a.pubState = 0 and a.userId=? ");//私有的
		args.add(sessionUser.getId());
		sql.append("\n 			or exists( ");//私有的
		sql.append("\n 		 		select id from fileShare where a.id=fileShare.fileDetailId and fileShare.shareId=? ");
		args.add(sessionUser.getId());
		sql.append("\n 			)");
		sql.append("\n 		)");
		sql.append("\n 	) ");
		
		
		String  searchAll = fileDetail.getSearchAll();
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
			sql.append("\n and  lower(f.fileExt) in ("+fileType+") ");
		}
		
		if(null!=fileDetail.getSearchMe()&& "22".equals(fileDetail.getSearchMe())){//他人上传
			this.addSqlWhere(sessionUser.getId(), sql, args, " and a.userid<>?");
		}else if(null!=fileDetail.getSearchMe()&& "21".equals(fileDetail.getSearchMe())){//自己上传
			this.addSqlWhere(sessionUser.getId(), sql, args, " and a.userid=?");
		}
		//创建日期
		this.addSqlWhere(fileDetail.getStartDate(), sql, args, " and substr(a.recordcreatetime,0,10)>=?");
		this.addSqlWhere(fileDetail.getEndDate(), sql, args, " and substr(a.recordcreatetime,0,10)<=?");
		
		//文件名
		this.addSqlWhere(fileDetail.getFileName(), sql, args, " and (instr(f.fileName,?,1)>0 or instr(a.fileDescribe,?,1)>0)",2);
		return this.pagedQuery(sql.toString(), " a.id desc ", args.toArray(), FileDetail.class);
	}

	/**
	 * 查询文档的分享人员
	 * @param sessionUser 当前操作人员
	 * @param fileDetailId 业务主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listAllShareUser(UserInfo sessionUser,
			Integer fileDetailId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select u.id,u.userName ");
		sql.append("\n from fileShare a");
		sql.append("\n inner join userinfo u on a.shareId=u.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(sessionUser.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(fileDetailId, sql, args, "\n and a.fileDetailId=?");
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	
	/**
	 * 根据fileId查询fileDetail
	 * @param fileId
	 * @param userInfo
	 * @return
	 */
	public FileDetail getFileDetailByFileId(Integer fileId, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from fileDetail a where 1=1 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, "\n and a.comId=?");
		this.addSqlWhere(fileId, sql, args, "\n and a.fileId=? and rownum <= 1");
		return (FileDetail) this.objectQuery(sql.toString(), args.toArray(), FileDetail.class);
	}

	/**
	 * 查询默认的文件夹
	 * @param comId 团队号
	 * @return
	 */
	public FileClassify queryDefDir(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select * from fileClassify a where isSys=1 and type=?");
		args.add(ConstantInterface.TYPE_FILE);
		this.addSqlWhere(comId, sql, args, "\n and a.comId=?");
		return (FileClassify) this.objectQuery(sql.toString(), args.toArray(), FileClassify.class);
	}

	/**
	 * 查询文件归档信息集合
	 * @param sessionUser
	 * @param fileIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileDetail> listFileDetailByMod(UserInfo sessionUser,List<Integer> fileIds) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from fileDetail a ");
		sql.append("\n where 1=1 and a.comId=? and a.userId=? ");
		args.add(sessionUser.getComId());
		args.add(sessionUser.getId());
		this.addSqlWhereIn(fileIds.toArray(new Integer[]{fileIds.size()}), sql, args, "\n and a.fileId  in ?");
		return this.listQuery(sql.toString(), args.toArray(), FileDetail.class);
	}

	/**
	 * 查询其他模块是否使用该数据
	 * @param userInfo 当前操作人员
	 * @param fileId 文件主键
	 * @return
	 */
	public Integer countOtherModSelf(UserInfo userInfo, Integer fileId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(*) from ( ");
		//任务附件
		sql.append("\n select id from taskUpfile f where f.comId=? and f.userId=? and f.upfileid=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//任务留言附件
		sql.append("\n union all ");
		sql.append("\n select id from taskTalkUpfile f where f.comId=? and f.userId=? and f.upfileid=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//客户附件
		sql.append("\n union all ");
		sql.append("\n select id from customerUpfile f where f.comId=? and f.userId=? and f.upfileid=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//客户留言附件
		sql.append("\n union all ");
		sql.append("\n select id from feedInfoFile f where f.comId=? and f.userId=? and f.upfileid=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//周报附件
		sql.append("\n union all ");
		sql.append("\n select id from weekRepUpfiles f where f.comId=? and f.userId=? and f.upfileid=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//周报留言附件
		sql.append("\n union all ");
		sql.append("\n select id from weekRepTalkFile f where f.comId=? and f.userId=? and f.upfileid=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//投票附件
		sql.append("\n union all ");
		sql.append("\n select id from voteTalkFile f where f.comId=? and f.userId=? and f.upfileid=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//问题附件
		sql.append("\n union all ");
		sql.append("\n select id from quesFile f where f.comId=? and f.userId=? and f.original=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//答案附件
		sql.append("\n union all ");
		sql.append("\n select id from ansFile f where f.comId=? and f.userId=? and f.original=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//答案留言附件
		sql.append("\n union all ");
		sql.append("\n select id from qasTalkFile f where f.comId=? and f.userId=? and f.upfileId=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//项目附件
		sql.append("\n union all ");
		sql.append("\n select id from itemUpfile f where f.comId=? and f.userId=? and f.upfileId=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//项目阶段附件
		sql.append("\n union all ");
		sql.append("\n select id from stagedInfo f where f.comId=? and f.creator=? and f.moduleId=? and f.moduleType='file'");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//项目附件
		sql.append("\n union all ");
		sql.append("\n select id from itemTalkFile f where f.comId=? and f.userId=? and f.upfileId=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//制度和公告留言附件
		sql.append("\n union all ");
		sql.append("\n select id from talkupfile f where f.comId=? and f.userId=? and f.upfileId=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		//公告附件
		sql.append("\n union all ");
		sql.append("\n select id from announUpfile f where f.comId=? and f.userId=? and f.upfileId=?");
		args.add(userInfo.getComId());
		args.add(userInfo.getId());
		args.add(fileId);
		sql.append("\n )a where 1=1 ");
		return this.countQuery(sql.toString(),args.toArray());
	}
}
