package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.FileClassify;
import com.westar.base.model.FileDetail;
import com.westar.base.model.FileTalk;
import com.westar.base.model.MsgShare;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.StringUtil;
import com.westar.core.service.FileCenterService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UploadService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.web.PaginationContext;
/**
 * 文档管理中心
 * @author zzq
 *
 */
@Controller
@RequestMapping("/fileCenter")
public class FileCenterController extends BaseController{

	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	UploadService uploadService;

	@Autowired
	ViewRecordService viewRecordService;
	

	/**
	 * 分页查询文档管理的附件
	 * @param fileDetail
	 * @return
	 */
	@RequestMapping("/listPagedFile")
	public ModelAndView listPagedFile() {
		
		ModelAndView mav = new ModelAndView("/fileCenter/fileCenter_fileList");
		
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo",userInfo);
		//先进行文件夹初始化
		fileCenterService.initFileClassify(userInfo,ConstantInterface.TYPE_FILE);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_FILE);
		return mav;
	}
	/**
	 * 异步取得文件夹信息
	 * @param classifyId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListTreeFolder")
	public List<FileClassify> ajaxListTreeFolder(FileClassify fileClassify) {
		UserInfo userInfo = this.getSessionUser();
		List<FileClassify> listTreeFolder = fileCenterService.listTreeFolder(fileClassify, userInfo);
		
		return listTreeFolder;
	}
	/**
	 * 异步获取文件和文件夹信息
	 * @param fileDetail
	 * @param pageNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListPagedFolderAndFile")
	public Map<String,Object> ajaxListPagedFolderAndFile(FileDetail fileDetail,Integer pageNum,Integer pageSize) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> result = fileCenterService.listPagedFolderAndFile(fileDetail, userInfo,pageNum);
		map.putAll(result);
		return map;
	}
	
	/**
	 * 分页查询文档管理的附件
	 * @param fileDetail
	 * @return
	 */
	@RequestMapping("/listPagedFileForSelect")
	public ModelAndView listPagedFileForSelect(FileDetail fileDetail,String[] modTypes,String[] fileTypes) {
		ModelAndView mav = new ModelAndView("/fileCenter/listPagedFileForSelect");
		UserInfo userInfo = this.getSessionUser();
		fileDetail.setComId(userInfo.getComId());
		fileDetail.setSessionUser(userInfo.getId());
		fileDetail.setUserId(userInfo.getId());

		fileDetail.setClassifyId(null);
		
		//查询文件
		List<FileDetail> listFiles = fileCenterService.listPagedFileDetailForSelect(fileDetail,userInfo);
		mav.addObject("listFiles", listFiles);
		mav.addObject("userInfo",userInfo);
		mav.addObject("fileDetail",fileDetail);
		return mav;
	}
	/**
	 * 新建文档页面跳转
	 * @param fileDetail
	 * @return
	 */
	@RequestMapping("/addFilePage")
	public ModelAndView addFilePage(Integer classifyId) {
		UserInfo userInfo = this.getSessionUser();
		
		ModelAndView mav = new ModelAndView("/fileCenter/addFile", "classifyId", classifyId);
		mav.addObject("userInfo",userInfo);
		return mav;
	}
	
	/**
	 * 新建文档
	 * @param fileDetail
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/addFile",method = RequestMethod.POST)
	public Map<String, String> addFile(FileDetail fileDetail) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		Map<String,String> map = new HashMap<String, String>();
		//添加文件
		fileCenterService.addFile(fileDetail,userInfo);
		map.put("status", "y");
		return map;
	}
	/**
	 * 添加文件夹
	 * @param classifyId
	 * @param typeName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addDir")
	public Map<String, Object> addDir(FileClassify fileClassify){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		//先验证名称的可用性
		boolean flag = fileCenterService.checkDirName(userInfo,fileClassify);
		if(!flag){//名称不可用
			map.put("status", "n");
			map.put("info", "该文件夹已存在");
		}else{
			
			//添加文件夹
			fileClassify = fileCenterService.addFileClassify(fileClassify, userInfo);
			
			map.put("status", "y");
			map.put("sessionUser", userInfo);
			map.put("fileClassify", fileClassify);
		}
		
		return map;
	}

	/**
	 * 修改文件夹名称
	 * @param classifyId
	 * @param typeName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateDir")
	public Map<String, Object> updateDir(FileClassify fileClassify){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		//先验证名称的可用性
		boolean flag = fileCenterService.checkDirName(userInfo,fileClassify);
		if(!flag){//名称不可用
			map.put("status", "n");
			map.put("info", "该文件夹已存在");
		}else{
			//修改文件夹名称
			fileCenterService.updateFileClassify(fileClassify, userInfo);
			
			map.put("status", "y");
		}
		
		return map;
	}
	/**
	 * 删除文件夹
	 * @param id 文件夹主键
	 * @param parentId 文件夹父目录
	 * @param isAll 是否删除子文件
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/delDir")
	public Map<String, Object> delDir(Integer id,Integer parentId,String isAll) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		
		Integer delNum = fileCenterService.delDir(id,parentId,isAll,userInfo);
		map.put("status", "y");
		map.put("delNum", delNum);
		
		return map;
	}
	
	/**
	 *  移动文件夹
	 * @param id
	 * @param parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/moveDir")
	public Map<String, Object> moveDir(Integer id,Integer parentId){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		//移动文件夹
		fileCenterService.updateMovelDir(id,parentId,userInfo);
		map.put("status", "y");
		
		return map;
	}
	
	/**
	 * 移动文件
	 * @param fileDetail
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/moveFile")
	public Map<String, Object> moveFile(FileDetail fileDetail){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		//移动文件
		fileCenterService.updateMovelFile(fileDetail,userInfo);
		map.put("status", "y");
		
		return map;
	}
	/**
	 * 删除文件
	 * @param fileDetail
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/delFile")
	public Map<String, Object> delFile(FileDetail fileDetail) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		//企业编号
		fileDetail.setComId(userInfo.getComId());
		//删除文件
		Map<String,Object> map  = fileCenterService.delFile(fileDetail,userInfo);
		return map;
	}
	/**
	 * 分享文件
	 * @param id
	 * @param fileId
	 * @param scopeGroups
	 * @param type 0自定义 1 所有人 2 自己
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/shareFile")
	public Map<String, Object> shareFile(FileDetail fileDetail,String fileShareScopeStr){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		fileCenterService.updateShareFile(fileDetail,userInfo,fileShareScopeStr);
		map.put("status", "y");
		
		return map;
	}
	/**
	 * 批量移动文件夹以及文件
	 * @param id
	 * @param fileId
	 * @param scopeGroups
	 * @param type 0自定义 1 所有人 2 自己
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/batchMove")
	public Map<String, Object> batchMove(String dirsStr,String filesStr,Integer classifyId){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		//批量移动文件夹以及文件
		fileCenterService.updateBachMove(dirsStr,filesStr,classifyId,userInfo);
		map.put("status", "y");
		
		return map;
	}
	/**
	 * 批量删除文件夹以及文件
	 * @param dirs
	 * @param files
	 * @param parentId
	 * @param isAll
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/batchDel")
	public Map<String, Object> batchDel(String dirsStr,String filesStr,Integer parentId,String isAll) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		//批量删除文件夹以及文件
		fileCenterService.updateBachDel(dirsStr,filesStr,userInfo,parentId,isAll);
		map.put("status", "y");
		
		return map;
	}
	
	/**
	 * 批量分享文件
	 * @param files
	 * @param scopeGroups
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/batchShare")
	public Map<String, Object> batchShare(String fileDetailStr,String fileShareScopeStr) {
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		//批量分享文件
		fileCenterService.updateBachShare(fileDetailStr,userInfo,fileShareScopeStr);
		map.put("status", "y");
		
		return map;
	}
	/**
	 * 查询指定的前n条数据
	 * @return
	 */
	@RequestMapping("/firstNFileList")
	public ModelAndView firstNFileList() {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/fileCenter/firstNFileList");
		List<FileDetail> fileList = fileCenterService.firstNFileList(new FileDetail(),6, userInfo);
		mav.addObject("fileList",fileList);
		return mav;
	}
	
	/**
	 * 取得附件信息(标识已读)
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getFileDetail")
	public Map<String, Object> getFileDetail(Integer id){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		FileDetail fileDetail = fileCenterService.getFileDetail(id,userInfo);
		map.put("status", "y");
		map.put("fileDetail",fileDetail);
		
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_FILE,0);
		return map;
	}
	/***********************************附件查看************************************************/
	/**
	 * 预览图片附件页面
	 * @param filepath: 图片
	 * @param busId 业务主键
	 * @param fileId:文件主键
	 * @param busType 业务类型
	 * @return
	 */
	@RequestMapping("/showPic")
	public ModelAndView showPic(String filepath,Integer fileId,Integer busId,String busType) {
		UserInfo userInfo = this.getSessionUser();
		
		ModelAndView mav = new ModelAndView();
		if(busType.indexOf(ConstantInterface.TYPE_TASK)>=0){
			//任务模块
			busType = ConstantInterface.TYPE_TASK;
		}else if(busType.indexOf(ConstantInterface.TYPE_ITEM)>=0){
			//项目模块
			busType = ConstantInterface.TYPE_ITEM;
		}else if(busType.indexOf(ConstantInterface.TYPE_WEEK)>=0){
			//周报模块
			busType = ConstantInterface.TYPE_WEEK;
		}else if(busType.indexOf(ConstantInterface.TYPE_CRM)>=0){
			//客户
			busType = ConstantInterface.TYPE_CRM;
		}else if(busType.indexOf(ConstantInterface.TYPE_FILE)>=0){
			//附件模块
			busType = ConstantInterface.TYPE_FILE;
		}else if(busType.indexOf(ConstantInterface.TYPE_QUES)>=0){
			//问答模块
			busType = ConstantInterface.TYPE_QUES;
		}else if(busType.indexOf(ConstantInterface.TYPE_VOTE)>=0){
			//投票模块
			busType = ConstantInterface.TYPE_VOTE;
		}
		//页面跳转
		mav.setViewName("/officeFile/showPic");
		//图片路径
		mav.addObject("filepath", filepath);
		//业务主键
		mav.addObject("busId", busId);
		//业务类型
		mav.addObject("busType",busType);
		//附件主键
		mav.addObject("fileId",fileId);
		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_FILE,fileId);
		//添加查看记录
		viewRecordService.addViewRecord(userInfo,viewRecord);
		//文档中心日志添加
		FileDetail fileDetail =  fileCenterService.getFileDetailByFileId(fileId, userInfo);
		if(fileDetail != null) {
			fileCenterService.addFileLog(userInfo.getComId(), fileDetail.getId(), userInfo.getId(),  userInfo.getUserName(), "查看了文件");
		}
		return mav;
	}
	/**
	 * 跳转到预览office附件页面
	 * @param file 附件基本信息
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	@RequestMapping("/viewOfficePage")
	public ModelAndView viewOfficePage(Upfiles file,Integer busId,String busType) {
		UserInfo userInfo = this.getSessionUser();
		//页面跳转
		ModelAndView mav = new ModelAndView("/officeFile/viewOfficeFile");
		
		if(busType.indexOf(ConstantInterface.TYPE_TASK)>=0){
			//任务模块
			busType = ConstantInterface.TYPE_TASK;
		}else if(busType.indexOf(ConstantInterface.TYPE_ITEM)>=0){
			//项目模块
			busType = ConstantInterface.TYPE_ITEM;
		}else if(busType.indexOf(ConstantInterface.TYPE_WEEK)>=0){
			//周报模块
			busType = ConstantInterface.TYPE_WEEK;
		}else if(busType.indexOf(ConstantInterface.TYPE_CRM)>=0){
			//客户
			busType = ConstantInterface.TYPE_CRM;
		}else if(busType.indexOf(ConstantInterface.TYPE_FILE)>=0){
			//附件模块
			busType = ConstantInterface.TYPE_FILE;
		}else if(busType.indexOf(ConstantInterface.TYPE_QUES)>=0){
			//问答模块
			busType = ConstantInterface.TYPE_QUES;
		}else if(busType.indexOf(ConstantInterface.TYPE_VOTE)>=0){
			//投票模块
			busType = ConstantInterface.TYPE_VOTE;
		}
		//附件基本属性条件
		mav.addObject("file", file);
		//业务主键
		mav.addObject("busId", busId);
		//业务类型
		mav.addObject("busType",busType);
		
		
		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_FILE, file.getId());
		//添加查看记录
		viewRecordService.addViewRecord(userInfo,viewRecord);
		//文档中心日志添加
		FileDetail fileDetail =  fileCenterService.getFileDetailByFileId(file.getId(), userInfo);
		if(fileDetail != null) {
			fileCenterService.addFileLog(userInfo.getComId(), fileDetail.getId(), userInfo.getId(),  userInfo.getUserName(), "查看了文件");
		}
		return mav;
	}
	/**
	 * 预览图片附件讨论页面
	 * @param filepath: 图片
	 * @param fileExt: 后缀
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	@RequestMapping("/listPagedFileTalks")
	public ModelAndView listPagedFileTalks(Integer fileId,Integer busId,String busType) {
		UserInfo userInfo = this.getSessionUser();
		//附件评论
		List<FileTalk> fileTalks = null;
		if(null!=busId && !StringUtil.isBlank(busType)){
			fileTalks = fileCenterService.listPagedFileTalks(userInfo.getComId(),busId,busType,fileId);
		}
		//页面跳转
		ModelAndView mav = new ModelAndView("/fileCenter/fileTalks");
		mav.addObject("fileTalks", fileTalks);
		mav.addObject("busId", busId);
		mav.addObject("busType", busType);
		mav.addObject("fileId", fileId);
		mav.addObject("sessionUser", userInfo);
		return mav;
	}
	/**
	 * 预览图片附件页面
	 * @param filepath: 图片
	 * @param fileExt: 后缀
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	@RequestMapping("/listFileViews")
	public ModelAndView listFileViews(Integer fileId) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/fileCenter/listFileViews");
		//浏览的人员
		List<ViewRecord> listViewRecord = viewRecordService.listViewRecord(userInfo,ConstantInterface.TYPE_FILE,fileId);
		mav.addObject("listViewRecord", listViewRecord);
		return mav;
	}
	/**
	 * 附件讨论回复
	 * @param fileTalk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/addFileTalk")
	public Map<String, Object> addFileTalk(FileTalk fileTalk) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		fileTalk.setComId(sessionUser.getComId());
		fileTalk.setUserId(sessionUser.getId());
		Integer id = fileCenterService.addFileTalk(fileTalk,sessionUser);
		map.put("status", "y");
		map.put("id", id);
		//用于返回页面拼接代码
		FileTalk fileTalk4Page = fileCenterService.getFileTalk(id, sessionUser.getComId());
		//用于返回页面拼接代码
		map.put("fileTalk", fileTalk4Page);
		map.put("sessionUser", this.getSessionUser());
		return map;
	}
	/**
	 * 删除讨论回复
	 * @param fileTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/delFileTalk")
	public Map<String, Object> delFileTalk(Integer id,String delChildNode) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//要删除的回复所有子节点和自己
		fileCenterService.delFileTalk(id,delChildNode,sessionUser);
		map.put("status", "y");
		return map;
	}

	/**
	 * 获取附件信息
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getFile")
	public Upfiles getFile(Integer fileId) throws Exception{
		Upfiles file = uploadService.getUpfileById(fileId);
		if(null==file){
			file = new Upfiles();
			file.setSucc("no");
		}else{
			file.setSucc("yes");
		}
		return file;
	}
	/**
	 * 文档查看权限验证
	 * @param fileId
	 * @param busId
	 * @param busType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/authorCheck")
	public MsgShare authorCheck(Integer busId,String busType){
		UserInfo userInfo = this.getSessionUser();
		MsgShare shareVo = new MsgShare();
		if(null==userInfo){
			shareVo.setSucc(false);
			shareVo.setPromptMsg("服务已关闭，请稍后重新操作！");
			return shareVo;
		}
		if(fileCenterService.authorCheck(userInfo,busId,busType)){
			shareVo.setSucc(true);
		}else{
			shareVo.setSucc(false);
			shareVo.setPromptMsg("抱歉，你没有查看权限");
		}
		return shareVo;
	}
}