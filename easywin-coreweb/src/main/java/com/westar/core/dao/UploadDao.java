package com.westar.core.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;

import com.westar.base.model.FileData;
import com.westar.base.model.Upfiles;
import com.westar.base.model.WebeditorFileData;
import com.westar.base.util.FileUtil;

@Repository
public class UploadDao extends BaseDao {
	/**
	 * 根据uuid查询附件信息
	 * @param uuid 附件表中的随机标识码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Upfiles getFileByUUid(String uuid) {
		Upfiles upfiles = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from upfiles a where a.uuid=?");
		List<Upfiles> list = this.listQuery(sql.toString(), new Object[] { uuid }, Upfiles.class);
		if (list != null && list.size() > 0) {
			upfiles = list.get(0);
		}
		return upfiles;
	}

	/**
	 * 保存附件二进制
	 * @param fileData
	 * @throws Exception 
	 */
	public void saveFileBlob(final FileData fileData) throws Exception {
		final StringBuffer sql = new StringBuffer();
		String path = FileUtil.getUploadBasePath() + fileData.getFilepath();
		final FileInputStream fis = new FileInputStream(path);
		sql.append("insert into filedata (comId,upfilesId,fileExt,filename,filepath,sizeb,sizem,uuid,md5,fileBinary) values (?,?,?,?,?,?,?,?,?,?)");
		this.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) {
				PreparedStatement ps = null;
				try {
					ps = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
					Integer i = 0;
					ps.setInt(++i, fileData.getComId());
					ps.setInt(++i, fileData.getUpfilesId());
					ps.setString(++i, fileData.getFileExt());
					ps.setString(++i, fileData.getFilename());
					ps.setString(++i, fileData.getFilepath());
					ps.setInt(++i, fileData.getSizeb());
					ps.setString(++i, fileData.getSizem());
					ps.setString(++i, fileData.getUuid());
					ps.setString(++i, fileData.getMd5());
					ps.setBinaryStream(++i, fis, fis.available());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ps;
			}
		});
	}

	/**
	 * 查询下载的附件
	 * @return 新闻信息 集合
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<FileData> listNoDwonFile() throws Exception {
		List<FileData> list = null;
		String ip = InetAddress.getLocalHost().getHostAddress();
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select x.id,x.upfilesId,x.filepath,x.comId from (");
		sql.append("\n select case when b.state is null then '0' else b.state end ss,a.id,a.upfilesId,a.filepath,a.comId from fileData a");
		sql.append("\n left join fileDataState b on a.upfilesId=b.upfilesId and b.ipaddress=?");
		args.add(ip);
		sql.append("\n ) x");
		sql.append("\n where x.ss='0'");
		list = this.listQuery(sql.toString(), args.toArray(), FileData.class);
		return list;
	}

	/**
	 * 从数据库下载指定附件
	 * @param upfilesId 表upfiles主键ID
	 * @return 附件信息
	 */
	@SuppressWarnings("unchecked")
	public FileData objectDwonFile(Integer upfilesId) {
		FileData fileData = null;
		try {
			StringBuffer sql = new StringBuffer();
			List<Object> args = new ArrayList<Object>();
			sql.append("\n select a.* from fileData a where a.upfilesId=?");
			args.add(upfilesId);
			fileData = (FileData) this.getJdbcTemplate().queryForObject(sql.toString(), args.toArray(), new RowMapper() {
				@Override
				public FileData mapRow(ResultSet rs, int k) throws SQLException {
					InputStream is = null;
					OutputStream os = null;
					FileData fileDate = new FileData();
					try {
						String path = FileUtil.getUploadBasePath() + rs.getString("filepath");
						File tFile = new File(path);
						if (!tFile.exists()) {
							is = UploadDao.this.getLobHandler().getBlobAsBinaryStream(rs, "fileBinary");
							FileUtil.creatDirs(path);
							os = new FileOutputStream(tFile);
							FileCopyUtils.copy(is, os);
						}
						fileDate.setId(rs.getInt("id"));
						fileDate.setUpfilesId(rs.getInt("upfilesId"));
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							if (is != null) {
								is.close();
							}
							if (os != null) {
								os.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return fileDate;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileData;
	}

	/**
	 * 从数据库下载附件
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<WebeditorFileData> listWebeditorNoDwonFile() throws Exception {
		List<WebeditorFileData> list = null;
		String ip = InetAddress.getLocalHost().getHostAddress();
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select x.* from (");
		sql.append("\n select case when b.state is null then '0' else b.state end ss,a.id,a.filename from webeditorFileData a");
		sql.append("\n left join webeditorFileDataState b on a.id=b.webeditorFileDataId and b.ipaddress=?");
		args.add(ip);
		sql.append("\n ) x");
		sql.append("\n where x.ss='0'");
		list = this.listQuery(sql.toString(), args.toArray(), WebeditorFileData.class);
		return list;
	}

	/**
	 * 查询指定业务关联的在线编辑器附件
	 * @param relationType 业务类别
	 * @param relationId 业务关联ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebeditorFileData> listWebeditorFileData(String relationType, Integer relationId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("select a.id,a.filename from webeditorFileData a where a.relationType=? and a.relationId=?");
		args.add(relationType);
		args.add(relationId);
		return this.listQuery(sql.toString(), args.toArray(), WebeditorFileData.class);
	}

	/**
	 * 从数据库下载指定附件
	 * @param id 主键ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public WebeditorFileData objectWebeditorNoDwonFile(Integer id) throws Exception {
		WebeditorFileData webeditorFileData = null;
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from webeditorFileData a");
		sql.append("\n where a.id=?");
		args.add(id);
		webeditorFileData = (WebeditorFileData) this.getJdbcTemplate().queryForObject(sql.toString(), args.toArray(), new RowMapper() {
			@Override
			public WebeditorFileData mapRow(ResultSet rs, int k) throws SQLException {
				InputStream is = null;
				OutputStream os = null;
				WebeditorFileData fileDate = new WebeditorFileData();
				try {
					String path = FileUtil.getRootPath() + "static" + File.separator + "plugins" + File.separator + "ewebeditor" + File.separator + "uploadfile" + File.separator + rs.getString("filename");
					File tFile = new File(path);
					if (!tFile.exists()) {
						is = UploadDao.this.getLobHandler().getBlobAsBinaryStream(rs, "fileBinary");
						FileUtil.creatDirs(path);
						os = new FileOutputStream(tFile);
						FileCopyUtils.copy(is, os);
					}
					fileDate.setId(rs.getInt("id"));
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (is != null) {
							is.close();
						}
						if (os != null) {
							os.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return fileDate;
			}
		});
		return webeditorFileData;
	}

	/**
	 * 保存在线编辑器上传的附件的二进制到数据库
	 * @param fileData
	 * @throws Exception
	 */
	public Integer saveWebeditorFileBlob(final WebeditorFileData fileData) throws Exception {
		final Integer id = this.add(fileData);

		// namedParameterJdbcTemplate 处理不了blob,jdbcTemplate在oracle中又不能返回主键
		// 因此先插入再修改保存二进制信息
		final StringBuffer sql = new StringBuffer();
		String path = FileUtil.getRootPath() + "static" + File.separator + "plugins" + File.separator + "ewebeditor" + File.separator + "uploadfile" + File.separator + fileData.getFilename();
		final FileInputStream fis = new FileInputStream(path);
		sql.append("update webeditorFileData set fileBinary=? where id=?");
		this.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) {
				PreparedStatement ps = null;
				try {
					ps = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
					Integer i = 0;
					ps.setBinaryStream(++i, fis, fis.available());
					ps.setInt(++i, id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ps;
			}
		});
		return id;
	}

	/**
	 * MD5查询文件
	 * @param fileMd5
	 * @param comId 
	 * @return
	 */
	public Upfiles getFileByMD5(String fileMd5, Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from upfiles ");
		sql.append("\n where 1=1 and comid=? and md5 = ?");
		args.add(comId);
		args.add(fileMd5);
		return (Upfiles) this.objectQuery(sql.toString(), args.toArray(), Upfiles.class);
	}
	/**
	 * 获取创建索引的附件信息
	 * @param upfileId
	 * @param comId
	 * @return
	 */
	public Upfiles queryUpfile4Index(Integer upfileId,Integer comId){
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("select a.recordcreatetime,a.filename,a.uuid,b.content as fileContent from Upfiles a \n");
		sql.append("left join filecontent b on a.comid = b.comid and a.id = b.upfilesid \n");
		sql.append("where a.comid =? and a.id = ? \n");
		args.add(comId);
		args.add(upfileId);
		return (Upfiles)this.objectQuery(sql.toString(),args.toArray(),Upfiles.class);
	}

	/**
	 * 查询用户头像信息
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Upfiles queryUserImgFile(String comId, String userId,String size) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("select a.id,a.filename,a.filepath,nvl(u.gender,'2') msg ");
		sql.append("\n from userOrganic userOrg ");
		sql.append("\n inner join userInfo u on  userOrg.userId=u.id");
		if("2".equals(size)){
			sql.append("\n left join upfiles a on userOrg.comId=a.comId and userOrg.smallHeadPortrait=a.id");
			
		}else if("1".equals(size)){
			sql.append("\n left join upfiles a on userOrg.comId=a.comId and userOrg.bigHeadPortrait=a.id");
		}
		sql.append("\n where userOrg.comId=? and userOrg.userId=?");
		args.add(comId);
		args.add(userId);
		return (Upfiles) this.objectQuery(sql.toString(), args.toArray(), Upfiles.class);
	}
}
