package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.graphics.predictor.Up;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.westar.base.enums.PubStateEnum;
import com.westar.base.model.FileClassify;
import com.westar.base.model.FileDetail;
import com.westar.base.model.FileLog;
import com.westar.base.model.FileShare;
import com.westar.base.model.FileTalk;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.dao.FileCenterDao;
import com.westar.core.web.PaginationContext;

@Service
public class FileCenterService {
	
	private static final Log loger = LogFactory.getLog(FileCenterService.class);

	@Autowired
	FileCenterDao fileCenterDao;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	IndexService indexService;
	
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	CrmService crmService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	WeekReportService weekReportService;
	
	@Autowired
	VoteService voteService;
	
	@Autowired
	QasService qasService;
	
	@Autowired
	MeetingService meetingService;
	
	@Autowired
	ForceInPersionService forceInService;

	@Autowired
    DailyService dailyService;

	@Autowired
	ProductService productService;


    /**
	 * 添加文件
	 * @param fileDetail 文档详情
	 * @param userInfo 用户信息
	 * @throws Exception
	 */
	public void addFile(FileDetail fileDetail, UserInfo userInfo) throws Exception {
		
		//先进行文件夹初始化
		this.initFileClassify(userInfo,ConstantInterface.TYPE_FILE);
		
		//上传的附件集合
		List<Upfiles> listUpfiles = fileDetail.getListUpfiles();
		if(null!=listUpfiles && !listUpfiles.isEmpty()){
			List<Integer> fileIds = new ArrayList<Integer>();
			for (Upfiles upfile : listUpfiles) {
				fileIds.add(upfile.getId());
			}
			//查询已有数据
			List<FileDetail> fileDetails = fileCenterDao.listFileDetailByMod(userInfo,fileIds);
			Set<Integer> preFieldSet = new HashSet<Integer>();
			//遍历有数据
			for (FileDetail fileDetailObj : fileDetails) {
				preFieldSet.add(fileDetailObj.getFileId());
			}
			//设置更新时间
			fileDetail.setModifyTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			//文档查看范围
			List<UserInfo> shares = null;
			//多个文件的消息只需要提醒一个
			Integer modId = 0;
			StringBuffer fileName = new StringBuffer();
			for (Upfiles upfile : listUpfiles) {
				
				if(preFieldSet.contains(upfile.getId())){
					continue;
				}
				
				FileDetail fileDetailT = new FileDetail();
				BeanUtils.copyProperties(fileDetail, fileDetailT);
				//文件主键
				fileDetailT.setFileId(upfile.getId());
				//文件上传人
				fileDetailT.setUserId(userInfo.getId());
				//文件所在公司
				fileDetailT.setComId(userInfo.getComId());
				//模块类型 （默认是知识模块）
				fileDetailT.setModuleType(fileDetail.getModuleType());
				//添加文件
				Integer fileDetailId = fileCenterDao.add(fileDetailT);
				//文件主键
				fileDetailT.setId(fileDetailId);
				fileCenterDao.update(fileDetailT);
				
				fileName.append(","+upfile.getFilename());
				
				//公开状态
				Integer pubState = fileDetail.getPubState();
				//私有的
				if(PubStateEnum.NO.getValue().equals(pubState)){
					//有分享范围
					List<FileShare> listFileShare = fileDetail.getListFileShare();
					List<Integer> listShareId = new ArrayList<>();
					if(null!=listFileShare && !listFileShare.isEmpty()){
						
						for (FileShare fileShare : listFileShare) {
							//添加分享范围
							fileShare.setComId(userInfo.getComId());
							fileShare.setFileDetailId(fileDetailId);
							fileCenterDao.add(fileShare);
							
							listShareId.add(fileShare.getShareId());
						}
						if(null==shares){
							shares = userInfoService.listScopeUserForMsg(userInfo.getComId(), listShareId);
						}
					}
				}else{
					if(null==shares){
						shares = userInfoService.listUser(userInfo.getComId());
					}
				}
				
				
				//为附件创建索引
				uploadService.updateUpfileIndex(upfile.getId(), userInfo, "add",fileDetailId,ConstantInterface.TYPE_FILE);
				//文档中心日志添加
				this.addFileLog(userInfo.getComId(), fileDetailId, userInfo.getId(),  userInfo.getUserName(), "上传了文件");
			}
			
			StringBuffer classifyName = new StringBuffer("");
			//上传文件的文件夹
			List<FileClassify> listClassify = fileCenterDao.listLeadLine(userInfo.getComId(),fileDetail.getClassifyId());
			if(null != listClassify && !listClassify.isEmpty()){
				classifyName.append("在文件夹\"");
				for (FileClassify fileClassify : listClassify) {
					classifyName.append(fileClassify.getTypeName()+"/");
				}
				classifyName.append("\"下");
			}
			Integer addFileNum = listUpfiles.size() - preFieldSet.size();
			if(addFileNum>0){
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo,null,modId, 
						classifyName.toString()+"上传"+(listUpfiles.size() - preFieldSet.size())+"个文件:"+fileName.substring(1, fileName.length()),
						ConstantInterface.TYPE_FILE, shares,null);
				String logContent = "在文档中心上传文档";
				String moduleType = fileDetail.getModuleType();
				if(StringUtils.isNotEmpty(moduleType)
						&& ConstantInterface.TYPE_LEARN.equals(moduleType)){
					logContent = "在线学习上传视频";
				}
				//日志添加
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),logContent, 
						ConstantInterface.TYPE_FILE, userInfo.getComId(),userInfo.getOptIP());
			}
		
		}
		
	}
	
	/**
	 * 添加模块附件信息
	 * @param sessionUser
	 * @param fileIds
	 * @param content
	 */
	public void addModFile(UserInfo sessionUser,List<Integer> fileIds,String content){
		if(null!=fileIds && !fileIds.isEmpty()){
			
			//启用附件
			uploadService.initForUse(fileIds.toArray(new Integer[fileIds.size()]));
			
			FileClassify fileClassify = fileCenterDao.queryDefDir(sessionUser.getComId());
			if(null == fileClassify){
				//先进行文件夹初始化
				this.initFileClassify(sessionUser,ConstantInterface.TYPE_FILE);
				fileClassify = fileCenterDao.queryDefDir(sessionUser.getComId());
				
			}
			Integer classifyId = fileClassify.getId();
			
			List<FileDetail> fileDetails = fileCenterDao.listFileDetailByMod(sessionUser,fileIds);
			Set<Integer> preFieldSet = new HashSet<Integer>();
			//遍历有数据
			for (FileDetail fileDetail : fileDetails) {
				preFieldSet.add(fileDetail.getFileId());
			}
			
			for (Integer fileId : fileIds) {
				
				if(preFieldSet.contains(fileId)){//已有附件信息
					continue;
				}
				
				FileDetail fileDetail = new  FileDetail();
				//设置更新时间
				fileDetail.setModifyTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
				//设置团队号
				fileDetail.setComId(sessionUser.getComId());
				//设置创建人员
				fileDetail.setUserId(sessionUser.getId());
				//设置文件主键
				fileDetail.setFileId(fileId);
				//设置文件描述
				fileDetail.setFileDescribe(content);
				//设置业务类型
				fileDetail.setModuleType(ConstantInterface.TYPE_FILE);
				//设置公共文件夹
				fileDetail.setClassifyId(classifyId);
				//设置私有
				fileDetail.setPubState(PubStateEnum.NO.getValue());
				//添加详情
				Integer fileDetailId = fileCenterDao.add(fileDetail);
				
				//为附件创建索引
				try {
					uploadService.updateUpfileIndex(fileId, sessionUser, "add",fileDetailId,ConstantInterface.TYPE_FILE);
				} catch (Exception e) {
					loger.info(e.getMessage());
				}
				
				//文档中心日志添加
				this.addFileLog(sessionUser.getComId(), fileDetailId, sessionUser.getId(),  sessionUser.getUserName(), "上传了文件");
			
			}
		}
	}
	/**
	 * 文件夹初始化
	 * @param userInfo 用户信息
	 */
	public void initFileClassify(UserInfo userInfo,String type) {
		boolean blnNeedDef= false;
		//已有的文件夹个数
		Integer classifyNum = fileCenterDao.getClassifyNum(userInfo.getComId(),null,type);
		if(classifyNum==0){//没有文件夹
			blnNeedDef = true;
		}else{
			FileClassify defFileClassify = fileCenterDao.queryDefDir(userInfo.getComId());
			if(null == defFileClassify){
				blnNeedDef = true;
			}
		}
		
		if(blnNeedDef){
			FileClassify fileClassify = new FileClassify();
			//企业编号
			fileClassify.setComId(userInfo.getComId());
			//创建人
			fileClassify.setUserId(userInfo.getId());
			//文件夹的父目录
			fileClassify.setParentId(-1);
			//文件夹名称
			fileClassify.setTypeName("公共文件夹");
			//文件夹的模块类别
			fileClassify.setType(type);
			fileClassify.setPubState(PubStateEnum.YES.getValue());
			//文件夹的是系统界别的不能删除
			fileClassify.setIsSys("1");
			
			fileCenterDao.add(fileClassify);
		}
		
	}
	/**
	 * 导航栏
	 * @param fileDetail 
	 * @return
	 */
	public List<FileClassify> listLeadLine(FileDetail fileDetail) {
		List<FileClassify> list = fileCenterDao.listLeadLine(fileDetail.getComId(),fileDetail.getClassifyId());
		return list;
	}
	/**
	 * 验证文件夹的名称
	 * @param userInfo 用户信息
	 * @param fileClassify 当前文件夹
	 * @return
	 */
	public boolean checkDirName(UserInfo userInfo, FileClassify fileClassify) {
		FileClassify fileClassifyT = fileCenterDao.checkDirName(userInfo,fileClassify.getParentId(),fileClassify.getTypeName());
		if(null== fileClassifyT){//名称可用
			return true;
		}else{//名称不可用
			boolean flag = (null != fileClassify.getId() && fileClassifyT.getId().equals(fileClassify.getId()));
			return flag;
		}
	}
	/**
	 * 添加文件夹
	 * @param fileClassify
	 * @param userInfo
	 */
	public FileClassify addFileClassify(FileClassify fileClassify, UserInfo userInfo) {
		//先进行文件夹初始化
		this.initFileClassify(userInfo,ConstantInterface.TYPE_FILE);
		
		//企业编号
		fileClassify.setComId(userInfo.getComId());
		//所有者
		fileClassify.setUserId(userInfo.getId());
		fileClassify.setUserName(userInfo.getUserName());
		//非系统文件夹
		fileClassify.setIsSys("0");
		Integer id = fileCenterDao.add(fileClassify);
		fileClassify.setId(id);
		fileClassify.setRecordCreateTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		return fileClassify;
		
	}
	/**
	 * 修改文件夹名称
	 * @param fileClassify
	 * @param userInfo
	 */
	public void updateFileClassify(FileClassify fileClassify, UserInfo userInfo) {
		fileCenterDao.update(fileClassify);
	}
	/**
	 * 删除文件夹
	 * @param id 文件夹主键
	 * @param parentId 父目录主键
	 * @param isAll 是否删除子文件
	 * @param userInfo 用户信息
	 * @throws Exception 
	 */
	public Integer delDir(Integer id, Integer parentId, String isAll,
			UserInfo userInfo) throws Exception {
		//只删除当前文件夹，移动的文件个数
		Integer delNum = 0;
		if("yes".equals(isAll)){//删除其下所有文件及文件夹
			List<FileClassify> list=fileCenterDao.listAllDir(userInfo.getId(),userInfo.getComId(),id);
			for (FileClassify fileClassify : list) {
				//分类主键
				Integer classifyId = fileClassify.getId();
				List<FileDetail> listFileDetail = fileCenterDao.listFileDetailByFileClassify(classifyId,userInfo.getComId());
				if(null!=listFileDetail){
					for(FileDetail vo:listFileDetail){
						//删除附件范围限制表fileViewScope
						fileCenterDao.delFileViewScope(vo.getId(),userInfo.getComId());
						
						//删除附件的评论
						this.delTalks(userInfo.getComId(),vo.getId(),ConstantInterface.TYPE_FILE,vo.getFileId());
						//删除索引库中对应附件索引
						uploadService.updateUpfileIndex(vo.getFileId(), userInfo, "del",vo.getId(),vo.getModuleType());
						//删除文件log
						fileCenterDao.delByField("fileLog", new String[]{"comId","fileDetailId"}, new Object[]{userInfo.getComId(),vo.getId()});
					}
				}
				//删除该分类的所有文件
				fileCenterDao.delAllFile(classifyId,userInfo.getComId());
				//删除分类
				fileCenterDao.delById(FileClassify.class, classifyId);
			}
			
		}else if(isAll == null || "no".equals(isAll)){//只删除当前文件夹
			fileCenterDao.updateChildName(userInfo.getComId(),parentId,id);
			//将文件夹的级别提高一级
			delNum += fileCenterDao.updateDirLev(userInfo.getComId(),parentId,id);
			//将文件的级别提高一级
			delNum += fileCenterDao.updateFileLev(userInfo.getComId(),parentId,id);
			
			//删除分类
			fileCenterDao.delById(FileClassify.class, id);
			
		}
		
		return delNum;
		
	}
	/**
	 * 文件夹列表树
	 * @param fileClassify
	 * @param preDirs 
	 * @return
	 */
	public List<FileClassify> listTreeDirForSelect(FileClassify fileClassify, String preDirs) {
		List<FileClassify> list = fileCenterDao.listTreeDirForSelect(fileClassify,preDirs);
		return list;
	}
	/**
	 * 查询当前所有的树形结构的信息
	 * @param fileClassify 分类
	 * @param sessionUser 当前操作人员
	 * @return
	 */
	public List<FileClassify> listTreeFolder(FileClassify fileClassify,UserInfo sessionUser) {
		List<FileClassify> list = fileCenterDao.listTreeFolder(fileClassify,sessionUser);
		return list;
	}
	
	/**
	 * 
	 * @param fileDetail
	 * @param userInfo
	 * @param pageNum 
	 * @return
	 */
	public Map<String,Object> listPagedFolderAndFile(FileDetail fileDetail, UserInfo userInfo, Integer pageNum){
		Map<String,Object> map = new HashMap<String, Object>();
		
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
									&& StringUtils.isEmpty(endTime);
		
		
		if(pageNum == 0 ){
			if(!fileDetail.getClassifyId().equals(-2)){
				List<FileClassify> folders = new  ArrayList<FileClassify>();
				if(searchFolderState){
					folders = fileCenterDao.listFileClassify(fileDetail,userInfo);
				}
				map.put("folders", folders);
			}else{
				fileDetail.setClassifyId(-1);
			}
			
		}
		
		List<FileDetail> files = fileCenterDao.listPagedFileDetail(fileDetail,userInfo);
		PageBean<FileDetail> pageBean = new PageBean<FileDetail>();
		pageBean.setRecordList(files);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		map.put("pageBean", pageBean);
				
		return map;
	}
	/**
	 * 查询附件信息
	 * @param fileDetail
	 * @param userInfo
	 * @return
	 */
	public List<FileDetail> listPagedFileDetail(FileDetail fileDetail, UserInfo userInfo){
		List<FileDetail> files = fileCenterDao.listPagedFileDetail(fileDetail,userInfo);
		return files;
	}
	/**
	 * 用于附件选择
	 * @param fileDetail
	 * @param userInfo
	 * @return
	 */
	public List<FileDetail> listPagedFileDetailForSelect(FileDetail fileDetail, UserInfo userInfo){
		List<FileDetail> files = fileCenterDao.listPagedFileDetailForSelect(fileDetail,userInfo);
		return files;
	}
	
	/**
	 * 查询指定目录下的文件夹
	 * @param fileDetail
	 * @param userInfo
	 * @return
	 */
	public List<FileClassify> listFileClassify(FileDetail fileDetail, UserInfo userInfo){
		List<FileClassify> folders = fileCenterDao.listFileClassify(fileDetail,userInfo);
		return folders;
	}
	/**
	 * 移动单个文件夹
	 * @param id
	 * @param parentId
	 * @param userInfo
	 */
	public void updateMovelDir(Integer id, Integer parentId, UserInfo userInfo) {
		fileCenterDao.updateMovelDir(id,parentId,userInfo.getComId());
	}
	/**
	 * 移动单个文件
	 * @param fileDetail
	 * @param userInfo
	 */
	public void updateMovelFile(FileDetail fileDetail, UserInfo userInfo) {
		//设置更新时间
		fileDetail.setModifyTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		fileCenterDao.update(fileDetail);//直接更新
		//文档中心日志添加
		this.addFileLog(userInfo.getComId(), fileDetail.getId(), userInfo.getId(),  userInfo.getUserName(), "移动了文件");
	}
	/**
	 * 删除已分类的文件
	 * @param fileDetail 文件详情
	 * @param userInfo 当前操作员
	 * @throws Exception 
	 */
	public Map<String,Object> delFile(FileDetail fileDetail,UserInfo userInfo) throws Exception{
		
		Map<String,Object> map = new HashMap<String,Object>();
		//删除索引库中对应附件索引
		uploadService.updateUpfileIndex(fileDetail.getFileId(), userInfo, "del",fileDetail.getId(),fileDetail.getModuleType());
//		//删除前先判断是否存在其他模块是否使用
		Integer countUsed = fileCenterDao.countOtherModSelf(userInfo,fileDetail.getFileId());
		if(countUsed > 0){
			map.put("status","f");
			map.put("info","在其他模块自己有使用该附件，不能删除！");
			return map;
		}
		//删除日志信息
		fileCenterDao.delByField("fileLog", new String[]{"comId","fileDetailId"}, new Object[]{userInfo.getComId(),fileDetail.getId()});
		//删除查看范围
		fileCenterDao.delByField("fileViewScope", new String[]{"comId","fileDetailId"}, new Object[]{userInfo.getComId(),fileDetail.getId()});
		fileCenterDao.delByField("fileShare", new String[]{"comId","fileDetailId"}, new Object[]{userInfo.getComId(),fileDetail.getId()});
		fileCenterDao.delByField("fileViewScopeDep", new String[]{"comId","fileDetailId"}, new Object[]{userInfo.getComId(),fileDetail.getId()});
		//删除附件的评论
		this.delTalks(userInfo.getComId(),fileDetail.getId(),ConstantInterface.TYPE_FILE,fileDetail.getFileId());
		fileCenterDao.delByField("fileTalk", new String[]{"comId","fileId","busId","busType"},
				new Object[]{userInfo.getComId(),fileDetail.getId(),fileDetail.getFileId(),ConstantInterface.TYPE_FILE});
		//删除别人的提醒
		fileCenterDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_FILE,fileDetail.getId()});
		
		FileDetail fileDetailObj =  fileCenterDao.getFileDetail(fileDetail.getId(),userInfo);
		//删除文件
		fileCenterDao.delById(FileDetail.class, fileDetail.getId());
		
		
		//日志记录
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除了文件："+fileDetailObj.getFileName(),
				ConstantInterface.TYPE_FILE, userInfo.getComId(), userInfo.getOptIP());
		map.put("status","y");
		return map;
	}
	/**
	 * 文件详情
	 * @param id 文件主键
	 * @param comId  企业编号
	 * @return
	 */
	public FileDetail getFileDetailScope(Integer id, Integer comId) {
		FileDetail fileDetail = fileCenterDao.getFileDetailScope(id,comId); 
		if(null!=fileDetail && fileDetail.getScopeType()==1){//是自定义的分组
			//查看范围
			fileDetail.setFileViewScopes(fileCenterDao.listFileViewScope(id,comId));
		}
		return fileDetail;
	}
	/**
	 * 修改范围信息
	 * @param fileDetail 文件归档信息
	 * @param sessionUser 当前操作人员
	 * @param fileShareScopeStr 分享范围信息
	 */
	public void updateShareFile(FileDetail fileDetail, UserInfo sessionUser, String fileShareScopeStr){
		
		JSONObject fileShareScope = JSONObject.parseObject(fileShareScopeStr);
		Integer pubState = fileShareScope.getInteger("pubState");
		fileDetail.setPubState(pubState);
		//设置更新时间
		fileDetail.setModifyTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		fileCenterDao.update(fileDetail);
		
		//删除文档查看范围
		fileCenterDao.delByField("fileViewScope", new String[]{"comId","fileDetailId"},
				new Object[]{sessionUser.getComId(),fileDetail.getId()});
		//删除文档查看范围
		fileCenterDao.delByField("fileShare", new String[]{"comId","fileDetailId"},
				new Object[]{sessionUser.getComId(),fileDetail.getId()});
		//非公开则需要设定范围
		if(PubStateEnum.NO.getValue().equals(pubState)){
			//批量分享文件
			List<UserInfo> listShares = JSONArray.parseArray(fileShareScope.getString("listShare"), UserInfo.class);
			if(null!=listShares && !listShares.isEmpty()){
				for (UserInfo shareUser : listShares) {
					FileShare fileShare = new FileShare();
					fileShare.setFileDetailId(fileDetail.getId());
					fileShare.setComId(sessionUser.getComId());
					fileShare.setShareId(shareUser.getId());
					fileCenterDao.add(fileShare);
				}
			}
		}
		//文档中心日志添加
		this.addFileLog(sessionUser.getComId(), fileDetail.getId(), sessionUser.getId(),  sessionUser.getUserName(), "分享了文件");
		
	}
	/**
	 * 批量移动文件夹以及文件
	 * @param dirsStr 选中的文件夹主键
	 * @param filesStr 选中的文件主键
	 * @param classifyId 所在文件夹的主键
	 * @param userInfo 
	 */
	public void updateBachMove(String dirsStr, String filesStr,
			Integer classifyId, UserInfo userInfo) {
		//文件夹
		List<FileClassify> listFileClassify = new ArrayList<FileClassify>();
		if(StringUtils.isNotEmpty(dirsStr)){
			listFileClassify = JSONArray.parseArray(dirsStr, FileClassify.class);
		}
		
		//批量移动文件夹
		if(null!=listFileClassify && !listFileClassify.isEmpty()){
			for (FileClassify fileClassify : listFileClassify) {
				//移动文件夹
				this.updateMovelDir(fileClassify.getId(), classifyId, userInfo);
			}
		}
		//文件信息
		List<FileDetail> listFileDetail = new ArrayList<FileDetail>();
		if(StringUtils.isNotEmpty(filesStr)){
			listFileDetail = JSONArray.parseArray(filesStr, FileDetail.class);
		}
		if(null!=listFileDetail && !listFileDetail.isEmpty()){
			for (FileDetail fileDetail : listFileDetail) {
				//文件分类
				fileDetail.setClassifyId(classifyId);
				//设置为批量操作
				fileDetail.setIsBatch("y");
				//移动文件
				this.updateMovelFile(fileDetail, userInfo);
				//文档中心日志添加
				this.addFileLog(userInfo.getComId(), fileDetail.getId(), userInfo.getId(),  userInfo.getUserName(), "移动了文件");
			}
		}
	}
	/**
	 * 批量删除文件夹以及文件
	 * @param dirsStr 选中的文件夹主键
	 * @param filesStr 选中的文件主键
	 * @param userInfo 用户信息
	 * @param parentId 所在文件夹主键
	 * @param isAll 是否删除所有
	 * @throws Exception 
	 */
	public void updateBachDel(String dirsStr,String filesStr, UserInfo userInfo, 
			Integer parentId, String isAll) throws Exception {
		
		//文件夹
		List<FileClassify> listFileClassify = new ArrayList<FileClassify>();
		if(StringUtils.isNotEmpty(dirsStr)){
			listFileClassify = JSONArray.parseArray(dirsStr, FileClassify.class);
		}
				
		//批量删除文件夹
		if(null!=listFileClassify && !listFileClassify.isEmpty()){
			for (FileClassify fileClassify : listFileClassify) {
				this.delDir(fileClassify.getId(), parentId, isAll, userInfo);
				
			}
		}
		//文件信息
		List<FileDetail> listFileDetail = new ArrayList<FileDetail>();
		if(StringUtils.isNotEmpty(filesStr)){
			listFileDetail = JSONArray.parseArray(filesStr, FileDetail.class);
		}
		//批量删除文件
		if(null!=listFileDetail && !listFileDetail.isEmpty()){
			for (FileDetail fileDetail : listFileDetail) {
				//企业编号
				fileDetail.setComId(userInfo.getComId());
				//文件分类
				fileDetail.setClassifyId(parentId);
				//设置为批量操作
				fileDetail.setIsBatch("y");
				//删除文件
				this.delFile(fileDetail,userInfo);
			}
		}
	}
	/**
	 * 批量分享文件
	 * @param fileDetailStr 文件归档信息
	 * @param sessionUser 当前操作人员
	 * @param fileShareScopeStr 分享范围信息
	 */
	public void updateBachShare(String fileDetailStr,
			UserInfo sessionUser, String fileShareScopeStr){
		//批量分享文件
		List<FileDetail> fileDetails = JSONArray.parseArray(fileDetailStr, FileDetail.class);
		if(null!=fileDetails && !fileDetails.isEmpty()){
			for (FileDetail fileDetail : fileDetails) {
				this.updateShareFile(fileDetail, sessionUser, fileShareScopeStr);
				//文档中心日志添加
				this.addFileLog(sessionUser.getComId(), fileDetail.getId(), sessionUser.getId(),  sessionUser.getUserName(), "分享了文件");
			}
		}
	}
	/**
	 * 查询指定的前n条数据
	 * @param fileDetail
	 * @param rowNum
	 * @return
	 */
	public List<FileDetail> firstNFileList(FileDetail fileDetail,Integer rowNum,UserInfo userInfo){
		fileDetail.setClassifyId(-1);
		List<FileDetail> list = fileCenterDao.firstNFileList(fileDetail,userInfo, rowNum);
		return list;
		
	}
	/**
	 * 取得附件信息
	 * @param id
	 * @return
	 */
	public FileDetail getFileDetail(Integer id, UserInfo userInfo) {
		FileDetail fileDetil = fileCenterDao.getFileDetail(id,userInfo);
		return fileDetil;
	}
	/**
	 * 查询附件评论
	 * @param comId 企业号
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param fileId 附件主键
	 * @return
	 */
	public List<FileTalk> listPagedFileTalks(Integer comId, Integer busId,
			String busType, Integer fileId) {
		List<FileTalk> list = fileCenterDao.listPagedFileTalks(comId,busId,busType,fileId);
		List<FileTalk> fileTalks = new ArrayList<FileTalk>();
		for (FileTalk fileTalk : list) {
			//处理回复的内容
			fileTalk.setTalkContent(StringUtil.toHtml(fileTalk.getTalkContent()));
			fileTalks.add(fileTalk);
		}
		return fileTalks;
	}
	/**
	 * 获取文档讨论集合
	 * @param comId
	 * @param fileId
	 * @return
	 */
	public List<FileTalk> listFileTalksOfAll(Integer comId,Integer fileId) {
		List<FileTalk> list = fileCenterDao.listFileTalksOfAll(comId,fileId);
		return list;
	}
	/**
	 * 添加附件的评论
	 * @param fileTalk
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public Integer addFileTalk(FileTalk fileTalk, UserInfo userInfo) throws Exception {
		//讨论的主键
		Integer id =fileCenterDao.add(fileTalk);
		//业务主键
		Integer busId = fileTalk.getBusId();
		//业务类型
		String busType = fileTalk.getBusType();
		//评论的附件
		Upfiles upfile = (Upfiles) fileCenterDao.objectQuery(Upfiles.class, fileTalk.getFileId());
		//评论内容
		String content = "附件\""+upfile.getFilename()+"\"添加评论:"+fileTalk.getTalkContent();
		//消息推送成员
		List<UserInfo> shares = new ArrayList<UserInfo>();
		//发送消息通知一下
		if(busType.equals("1")){//是信息分享
			//信息分享的成员
			shares = msgShareService.listShareUser(busId,userInfo.getComId());
		}else if(busType.equals(ConstantInterface.TYPE_CRM)){//是客户
			//客户查看的成员
			shares = crmService.listCrmOwnersNoForce(userInfo.getComId(), busId);
		}else if(busType.equals(ConstantInterface.TYPE_ITEM)){//是项目
			//项目查看的成员
			shares = itemService.listItemOwnersNoForce(userInfo.getComId(), busId);
		}else if(busType.equals(ConstantInterface.TYPE_PRODUCT)){//是产品
			//产品查看的成员
			shares = productService.listProCreatorsNoForce(userInfo.getComId(), busId);
		}else if(busType.equals(ConstantInterface.TYPE_TASK)){//是任务
			//任务的负责人和执行人
			shares = taskService.listTaskUserForMsg(userInfo.getComId(), busId);
		}else if(busType.equals(ConstantInterface.TYPE_WEEK)){//是周报
			//周报信息推送范围
			shares = weekReportService.listWeekViewers(userInfo.getComId(),userInfo.getId(), busId);
		}else if(busType.equals(ConstantInterface.TYPE_DAILY)){//是分享
			//周报信息推送范围
			shares = dailyService.listDailyViewers(userInfo.getComId(),userInfo.getId(), busId);
		}else if(busType.equals(ConstantInterface.TYPE_VOTE)){//是投票
			//投票的查看人员
			shares = voteService.listVoteViews(userInfo.getComId(),userInfo.getId(), busId);
		}else if(busType.equals(ConstantInterface.TYPE_QUES)){//是问答
			//发布问题需要消息提醒的所有人
			shares = userInfoService.listUser(userInfo.getComId());
		}
		todayWorksService.addTodayWorks(userInfo, null, busId,content , busType, shares, null);
		//附件留言更新
//		uploadService.updateUpfileIndex(upfile.getId(), userInfo, "update",busId,busType);
		//文档中心日志添加
		FileDetail fileDetail =  this.getFileDetailByFileId(fileTalk.getFileId(), userInfo);
		if(fileDetail != null) {
			this.addFileLog(userInfo.getComId(), fileDetail.getId(), userInfo.getId(),  userInfo.getUserName(), "评论了文件");
		}
		return id;
	}
	
	/**
	 * 删除附件的评论
	 * @param id 评论主键
	 * @param delChildNode 是否删除子留言
	 * @param userInfo 当前操作人员
	 * @return
	 * @throws Exception 
	 */
	public void delFileTalk(Integer id, String delChildNode,
			UserInfo userInfo) throws Exception {
		//查询附件讨论是否存在
		FileTalk fileTalk = (FileTalk) fileCenterDao.objectQuery(FileTalk.class, id);
		if(null!=fileTalk){
			//删除自己
			if(null==delChildNode){
				fileCenterDao.delById(FileTalk.class, id);
			}else if("yes".equals(delChildNode)){//删除自己和所有的子节点
				fileCenterDao.delFileTalk(id,userInfo.getComId());
			}else if("no".equals(delChildNode)){//删除自己,将子节点提高一级
				//将子节点提高一级
				fileCenterDao.updateFileTalkParentId(id,userInfo.getComId());
				//删除自己
				fileCenterDao.delById(FileTalk.class, id);
			}
			//业务主键
			Integer busId = fileTalk.getBusId();
			//业务类型
			String busType = fileTalk.getBusType();
			//评论的附件
			Upfiles upfile = (Upfiles) fileCenterDao.objectQuery(Upfiles.class, fileTalk.getFileId());
			//评论内容
			String content = "附件\""+upfile.getFilename()+"\"删除评论:"+fileTalk.getTalkContent();
			//消息推送成员
			List<UserInfo> shares = new ArrayList<UserInfo>();
			//发送消息通知一下
			if(busType.equals("1")){//是信息分享
				//信息分享的成员
				shares = msgShareService.listShareUser(busId,userInfo.getComId());
			}else if(busType.equals(ConstantInterface.TYPE_CRM)){//是客户
				//客户查看的成员
				shares = crmService.listCrmOwnersNoForce(userInfo.getComId(), busId);
			}else if(busType.equals(ConstantInterface.TYPE_ITEM)){//是项目
				//项目查看的成员
				shares = itemService.listItemOwnersNoForce(userInfo.getComId(), busId);
			}else if(busType.equals(ConstantInterface.TYPE_PRODUCT)){//是产品
				//项目查看的成员
				shares = productService.listProCreatorsNoForce(userInfo.getComId(), busId);
			}else if(busType.equals(ConstantInterface.TYPE_TASK)){//是任务
				//任务的负责人和执行人
				shares = taskService.listTaskUserForMsg(userInfo.getComId(), busId);
			}else if(busType.equals(ConstantInterface.TYPE_WEEK)){//是周报
				//周报信息推送范围
				shares = weekReportService.listWeekViewers(userInfo.getComId(),userInfo.getId(), busId);
			}else if(busType.equals(ConstantInterface.TYPE_DAILY)){//是周报
				//周报信息推送范围
				shares = dailyService.listDailyViewers(userInfo.getComId(),userInfo.getId(), busId);
			}else if(busType.equals(ConstantInterface.TYPE_VOTE)){//是投票
				//投票的查看人员
				shares = voteService.listVoteViews(userInfo.getComId(),userInfo.getId(), busId);
			}else if(busType.equals(ConstantInterface.TYPE_QUES)){//是问答
				//发布问题需要消息提醒的所有人
				shares = userInfoService.listUser(userInfo.getComId());
			}
			todayWorksService.addTodayWorks(userInfo, null, busId,content , busType, shares, null);
			//附件留言更新
//			uploadService.updateUpfileIndex(upfile.getId(), userInfo, "update",busId,busType);
			//文档中心日志添加
			FileDetail fileDetail =  this.getFileDetailByFileId(fileTalk.getFileId(), userInfo);
			if(fileDetail != null) {
				this.addFileLog(userInfo.getComId(), fileDetail.getId(), userInfo.getId(),  userInfo.getUserName(), "删除了文件评论");
			}
		}
	}
	/**
	 * 取得附件留言
	 * @param id 留言主键
	 * @param comId 企业号
	 * @return
	 */
	public FileTalk getFileTalk(Integer id, Integer comId) {
		FileTalk fileTalk = fileCenterDao.getFileTalk(id,comId);
		//转换
		if(null!=fileTalk){
			String content = StringUtil.toHtml(fileTalk.getTalkContent());
			fileTalk.setTalkContent(content);
		}
		return fileTalk;
	}
	
	/**
	 * 删除附件评论
	 * @param comId 企业号
	 * @param busId 模块主键
	 * @param fileId 附件主键
	 */
	private void delTalks(Integer comId, Integer busId,String busType, Integer fileId) {
		//查询模块下是否还有相同的附件
		List<FileDetail> list = fileCenterDao.listSimFile(comId,busId,busType,fileId);
		//在其他地方没有使用该附件，则删除附件评论,；在其他地方有使用该附件，则不处理
		if(null!=list && list.size()==1){
			fileCenterDao.delByField("fileTalk", new String[]{"comId","fileId","busId","busType"},
					new Object[]{comId,fileId,busId,busType});
			
		}
	}
	/**
	 * 合并附件留言
	 * @param comId 企业号
	 * @param oldBusId 业务主键（原来的）
	 * @param newBusId 业务主键（后来的）
	 * @param busType 业务类型
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void comPressFileTalk(Integer comId, Integer oldBusId,
			Integer newBusId, String busType) {
		fileCenterDao.comPressFileTalk(comId,oldBusId,newBusId,busType);
		
	}
	/**
	 * 获取团队下所有附件
	 * @param userInfo
	 * @return
	 */
	public List<Upfiles> listUpfilesOfAll(UserInfo userInfo){
		return fileCenterDao.listUpfilesOfAll(userInfo);
	}
	/**
	 * 文档查看权限验证
	 * @param userInfo
	 * @param busId
	 * @param busType
	 * @return
	 */
	public boolean authorCheck(UserInfo userInfo,Integer busId,String busType){
		
		if(ConstantInterface.TYPE_TASK.equals(busType)){
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			if(isForceIn){//是督察人员
				return true;
			}else if(taskService.authorCheck(userInfo,busId,0)){//不是督查人员，但有查看权限
				return true;
			}else{//没有查看权限
				return false;
			}
		}else if(ConstantInterface.TYPE_CRM.equals(busType)){
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			if(isForceIn){//是督察人员
				return true;
			}else if(crmService.authorCheck(userInfo.getComId(),busId,userInfo.getId())){//不是督查人员，但有查看权限
				return true;
			}else{//没有查看权限
				return false;
			}
		}else if(ConstantInterface.TYPE_ITEM.equals(busType)){
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			if(isForceIn){//是督察人员
				return true;
			}else if(itemService.authorCheck(userInfo.getComId(),busId,userInfo.getId())){//不是督查人员，但有查看权限
				return true;
			}else{//没有查看权限
				return false;
			}
		}else if(ConstantInterface.TYPE_PRODUCT.equals(busType)){
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			if(isForceIn){//是督察人员
				return true;
			}else if(productService.authorCheck(userInfo.getComId(),busId,userInfo.getId())){//不是督查人员，但有查看权限
				return true;
			}else{//没有查看权限
				return false;
			}
		}else if(ConstantInterface.TYPE_WEEK.equals(busType)){
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			if(isForceIn){//是督察人员
				return true;
			}else if(weekReportService.authorCheck(userInfo.getComId(),busId,userInfo.getId(),busId)){//不是督查人员，但有查看权限
				return true;
			}else{//没有查看权限
				return false;
			}
		}else if(ConstantInterface.TYPE_DAILY.equals(busType)){
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			if(isForceIn){//是督察人员
				return true;
			}else if(dailyService.authorCheck(userInfo.getComId(),busId,userInfo.getId(),busId)){//不是督查人员，但有查看权限
				return true;
			}else{//没有查看权限
				return false;
			}
		}else if(ConstantInterface.TYPE_VOTE.equals(busType)){
			return voteService.authorCheck(userInfo.getComId(),busId,userInfo.getId());
		}else if(ConstantInterface.TYPE_MEETING.equals(busType)){
			return meetingService.authorCheck(userInfo.getComId(),busId,userInfo.getId());
		}else if("1".equals(busType)){
			return msgShareService.authorCheck(userInfo.getComId(),busId,userInfo.getId());
		}else{
			List<FileDetail> listFileDetail = fileCenterDao.authorCheck(userInfo.getComId(),busId,userInfo.getId());
			if(null!=listFileDetail && !listFileDetail.isEmpty()){
				return true;
			}else{
				return false;
			}
		}
	}
	/**
	 * 获取文件夹信息
	 * @param id
	 * @return
	 */
	public FileClassify getFileClassify(Integer id ) {
		return (FileClassify) fileCenterDao.objectQuery(FileClassify.class, id);
	}
	/**
	 * 查询文件分享人员
	 * @param sessionUser 当前操作人员
	 * @param busId 文件归档主键
	 * @return
	 */
	public Map<String,Object> listAllShareUser(UserInfo sessionUser, Integer fileDetailId) {
		Map<String,Object> map = new HashMap<String,Object>();
		//查询归档文件详情
		FileDetail fileDetail = fileCenterDao.getFileDetail( fileDetailId,sessionUser);
		if(null!=fileDetail 
				&& PubStateEnum.YES.getValue().equals(fileDetail.getPubState())){//是公开的
			map.put("pubState", PubStateEnum.YES.getValue());
		}else{
			//分享的人员
			List<UserInfo> listShare = fileCenterDao.listAllShareUser(sessionUser,fileDetailId);
			map.put("pubState", PubStateEnum.NO.getValue());
			map.put("listShare", listShare);
		}
		map.put("modObj", fileDetail);
		return map;
	}
	
	/**
	 * 文档中心日至田家
	 * @param comId
	 * @param fileDetailId
	 * @param userId
	 * @param userName
	 * @param content
	 */
	public void addFileLog(Integer comId, Integer fileDetailId, Integer userId, String userName, String content) {
		FileLog fileLog = new FileLog(comId, fileDetailId, userId, content, userName);
		fileCenterDao.add(fileLog);

	}
	
	/**
	 * 根据fileId查询fileDetail
	 * @param fileId
	 * @param userInfo
	 * @return
	 */
	public FileDetail getFileDetailByFileId(Integer fileId, UserInfo userInfo) {
		return fileCenterDao.getFileDetailByFileId(fileId,userInfo);
	}

	/**
	 *  根据id获取文件的基本信息
	 * @param id
	 * @return
	 */
	public Upfiles getUpfilesById(Integer id){
		return (Upfiles) fileCenterDao.objectQuery(Upfiles.class,id);
	}
}
