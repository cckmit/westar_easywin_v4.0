package com.westar.core.service;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.westar.base.model.FileData;
import com.westar.base.model.FileDataState;
import com.westar.base.model.Filecontent;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WebeditorFileData;
import com.westar.base.model.WebeditorFileDataState;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.FileUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.base.util.TiKaUtil;
import com.westar.core.dao.UploadDao;
import com.westar.core.thread.IndexUpdateThread;

@Service
public class UploadService {

	@Autowired
	UploadDao uploadDao;
	@Autowired
	IndexService indexService;
	@Autowired
	FileCenterService fileCenterService;

	/**
	 * 新增附件信息
	 * @param upfiles
	 * @return
	 */
	public Integer addFile(Upfiles upfiles) {
		//设置过期时间
		String  nowDateTime= DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		String expireTime = DateTimeUtil.addDate(nowDateTime,DateTimeUtil.yyyy_MM_dd_HH_mm_ss,Calendar.DAY_OF_YEAR, 1);
		upfiles.setExpireTime(expireTime);
		
		Integer id = uploadDao.add(upfiles);
		upfiles.setId(id);
		return id;
	}

	/**
	 * 保存附件二进制信息
	 * @param fileData
	 * @throws Exception 
	 */
	public void addFileData(FileData fileData) throws Exception {
		uploadDao.saveFileBlob(fileData);
		// 记录该服务器上已经有附件二进制
		FileDataState fileDataState = new FileDataState();
		fileDataState.setComId(fileData.getComId());
		fileDataState.setUpfilesId(fileData.getUpfilesId());
		fileDataState.setState("1");
		String ip = InetAddress.getLocalHost().getHostAddress();
		fileDataState.setIpAddress(ip);
		uploadDao.add(fileDataState);
	}

	/**
	 * 保存在线编辑器上传的附件的二进制到数据库
	 * @param fileData
	 * @throws Exception
	 */
	public void addWebeditorFileData(WebeditorFileData fileData) throws Exception {
		Integer id = uploadDao.saveWebeditorFileBlob(fileData);
		// 记录该服务器上已经有附件二进制
		WebeditorFileDataState fileDataState = new WebeditorFileDataState();
		fileDataState.setWebeditorFileDataId(id);
		fileDataState.setState("1");
		String ip = InetAddress.getLocalHost().getHostAddress();
		fileDataState.setIpAddress(ip);
		uploadDao.add(fileDataState);
	}

	/**
	 * 删除附件信息
	 * @param id 附件表主键id
	 * @param basepath 项目路径
	 * @throws Exception 
	 */
	public synchronized void delFile(Integer id, String basepath,UserInfo userInfo) throws Exception {
		Upfiles upfiles = (Upfiles) uploadDao.objectQuery(Upfiles.class, id);
		File f = new File(basepath + upfiles.getFilepath());
		// 删除数据库中附件信息对应记录
		uploadDao.delByField("filecontent", "upfilesId", id);
		uploadDao.delByField("fileData", "upfilesId", id);
		uploadDao.delByField("fileDataState", "upfilesId", id);
		uploadDao.delById(Upfiles.class, id);
		// 删除附件文件
		if (f.exists()) {
			f.delete();
		}
	}

	/**
	 * 根据uuid查询附件信息
	 * @param uuid 附件表中的随机标识码
	 * @return
	 */
	public Upfiles getFileByUUid(String uuid) {
		return uploadDao.getFileByUUid(uuid);
	}

	/**
	 * 如果本地附件不存在，从服务器下载
	 * @param list
	 * @throws Exception 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void downFile(Upfiles upfiles) throws Exception {
		List<Upfiles> list = new ArrayList<Upfiles>();
		list.add(upfiles);
		this.downFile(list);
	}

	/**
	 * 如果本地附件不存在，从服务器下载
	 * @param list
	 * @throws Exception 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void downFile(List<Upfiles> list) throws Exception {
		String ip = InetAddress.getLocalHost().getHostAddress();
		if (list != null) {
			for (Upfiles upfiles : list) {
				String path = FileUtil.getUploadBasePath() + upfiles.getFilepath();
				File tFile = new File(path);
				if (!tFile.exists()) {
					// 如果文件不存在则从服务器上下载
					uploadDao.objectDwonFile(upfiles.getId());
					// 更新状态
					uploadDao.delByField("filedatastate", new String[] { "upfilesId", "ipAddress" }, new Object[] { upfiles.getId(), ip });
					FileDataState fileDataState = new FileDataState();
					fileDataState.setUpfilesId(upfiles.getId());
					fileDataState.setState("1");
					fileDataState.setIpAddress(ip);
					uploadDao.add(fileDataState);

				}
			}
		}
	}

	/**
	 * 从数据库下载附件
	 * @param relationType 业务类别
	 * @param relationId 业务关联ID
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void doWebeditorFile(String relationType, Integer relationId) throws Exception {
		String ip = InetAddress.getLocalHost().getHostAddress();
		// 在线编辑器上传的附件
		List<WebeditorFileData> listw = uploadDao.listWebeditorFileData(relationType, relationId);
		if (listw != null) {
			for (WebeditorFileData f : listw) {
				String path = FileUtil.getRootPath() + "static" + File.separator + "plugins" + File.separator + "ewebeditor" + File.separator + "uploadfile" + File.separator + f.getFilename();
				File tFile = new File(path);
				if (!tFile.exists()) {
					uploadDao.objectWebeditorNoDwonFile(f.getId());

					// 更新状态，设置本服务器上已下载
					uploadDao.delByField("WebeditorFiledatastate", new String[] { "webeditorFileDataId", "ipAddress" }, new Object[] { f.getId(), ip });

					WebeditorFileDataState fileDataState = new WebeditorFileDataState();
					fileDataState.setWebeditorFileDataId(f.getId());
					fileDataState.setState("1");
					fileDataState.setIpAddress(ip);
					uploadDao.add(fileDataState);
				}
			}
		}

	}

	/**
	 * 自动从服务器上下载附件
	 * @throws Exception 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void doAutoDownFiles() throws Exception {
		List<FileData> listNoDwonFile = uploadDao.listNoDwonFile();
		// 记录该服务器上已经有附件二进制
		if (listNoDwonFile != null) {
			for (FileData f : listNoDwonFile) {
				// 如果附件不存在，则从数据库下载
				String path = FileUtil.getUploadBasePath() + f.getFilepath();
				File tFile = new File(path);
				if (!tFile.exists()) {
					uploadDao.objectDwonFile(f.getUpfilesId());
				}

				FileDataState fileDataState = new FileDataState();
				fileDataState.setUpfilesId(f.getUpfilesId());
				fileDataState.setComId(f.getComId());
				fileDataState.setState("1");
				String ip = InetAddress.getLocalHost().getHostAddress();
				fileDataState.setIpAddress(ip);
				uploadDao.add(fileDataState);
			}
		}

		// 在线编辑器上传的附件
		List<WebeditorFileData> listw = uploadDao.listWebeditorNoDwonFile();
		if (listw != null) {
			for (WebeditorFileData f : listw) {
				String path = FileUtil.getRootPath() + "static" + File.separator + "plugins" + File.separator + "ewebeditor" + File.separator + "uploadfile" + File.separator + f.getFilename();
				File tFile = new File(path);
				if (!tFile.exists()) {
					uploadDao.objectWebeditorNoDwonFile(f.getId());
				}
				WebeditorFileDataState fileDataState = new WebeditorFileDataState();
				fileDataState.setComId(f.getComId());
				fileDataState.setWebeditorFileDataId(f.getId());
				fileDataState.setState("1");
				String ip = InetAddress.getLocalHost().getHostAddress();
				fileDataState.setIpAddress(ip);
				uploadDao.add(fileDataState);
			}
		}

	}

	/**
	 * MD5查询文件
	 * @param fileMd5
	 * @param comId 
	 * @return
	 * @throws UnknownHostException 
	 */
	public Upfiles getFileByMD5(String fileMd5, Integer comId) {
		Upfiles upfiles  = uploadDao.getFileByMD5(fileMd5,comId);
		return upfiles;
	}

	/**
	 * 添加文件内容
	 * @param filecontent
	 * @throws Exception 
	 */
	public void addFileContent(Filecontent filecontent) throws Exception {
		String content =TiKaUtil.parseFile(new File(filecontent.getFilePath()));
		filecontent.setContent(content);
		uploadDao.add(filecontent);
		
	}
	/**
	 * 为附件创建、更新索引
	 * @param upfileId 附件主键
	 * @param userInfo 当前登录人信息对象
	 * @throws Exception 
	 */
	public void updateUpfileIndex(Integer upfileId,UserInfo userInfo,String opType,
			Integer relatedBusId,String relatedBusType) throws Exception{
		//更新附件索引
		Upfiles upfile = uploadDao.queryUpfile4Index(upfileId,userInfo.getComId());
		if(null == upfile){
			return;
		}
		//再添加新索引数据
		//把bean非空属性值连接策划那个字符串
		//附件名称
		StringBuffer attStr = new StringBuffer(upfile.getFilename()+",");
		//附件内容
//		attStr.append(upfile.getFileContent()+",");
		//附件留言
//		List<FileTalk> listFileTalksOfAll = fileCenterService.listFileTalksOfAll(userInfo.getComId(),upfileId);
//		if(null!=listFileTalksOfAll && !listFileTalksOfAll.isEmpty()){
//			for(FileTalk vo:listFileTalksOfAll){
//				attStr.append(vo.getTalkContent()+","+vo.getTalkerName()+",");
//			}
//		}
		//返回一个线程池（这个线程池只有一个线程）,这个线程池可以在线程死后（或发生异常时）重新启动一个线程来替代原来的线程继续执行下去！
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String indexKey = userInfo.getComId()+"_"+relatedBusType+"_"+relatedBusId+"_"+ConstantInterface.TYPE_FILE+"_"+upfileId;
		//为附件创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				indexKey,userInfo.getComId(),upfileId,ConstantInterface.TYPE_FILE,
				upfile.getFilename(),attStr.toString(),DateTimeUtil.parseDate(upfile.getRecordCreateTime(),0),relatedBusId,relatedBusType);
		//根据主键跟新索引
		pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,indexKey));
		
	}
	/**
	 * 获取创建索引的附件信息
	 * @param upfileId
	 * @param comId
	 * @return
	 */
	public Upfiles queryUpfile4Index(Integer upfileId,Integer comId){
		return uploadDao.queryUpfile4Index(upfileId, comId);
	}
	/**
	 * 通过主键取得附件
	 * @param upfileId 附件主键
	 * @return
	 */
	public Upfiles getUpfileById(Integer upfileId){
		return (Upfiles) uploadDao.objectQuery(Upfiles.class, upfileId);
	}
	/**
	 * 更新附件名称
	 * @param upfiles
	 */
	public void updateFileName(Upfiles upfiles){
		uploadDao.update("update upfiles a set a.filename=:filename where a.comid=:comId and a.id=:id",upfiles);
	}

	/**
	 * 查询用户头像信息
	 * @param comId 团队号
	 * @param userId 人员主键
	 * @return
	 */
	public Upfiles queryUserImgFile(String comId, String userId,String size) {
		return uploadDao.queryUserImgFile(comId,userId,size);
	}

	/**
	 * 启用多个附件
	 * @param upfilesId
	 */
	public void initForUse(Integer[] upfilesId) {
		if(null!=upfilesId && upfilesId.length>0){
			for (Integer upfileId : upfilesId) {
				this.initForUse(upfileId);
			}
		}
	}
	/**
	 * 启用单个附件
	 * @param upfileId
	 */
	public void initForUse(Integer upfileId){
		Upfiles upfiles = new Upfiles();
		upfiles.setId(upfileId);
		upfiles.setExpireTime(null);
		uploadDao.update("update upfiles a set a.expireTime=:expireTime where a.id=:id",upfiles);
	}
	/**
	 * 启用多个附件
	 * @param upfilesId
	 */
	public void initForUse(List<Upfiles> upfiles) {
		if(null!=upfiles && !upfiles.isEmpty()){
			for (Upfiles upfile : upfiles) {
				upfile.setExpireTime(null);
				uploadDao.update("update upfiles a set a.expireTime=:expireTime where a.id=:id",upfiles);
			}
		}
	}
	
	

}
