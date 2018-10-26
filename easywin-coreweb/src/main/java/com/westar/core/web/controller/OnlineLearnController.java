package com.westar.core.web.controller;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.FileClassify;
import com.westar.base.model.FileDetail;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.VideoTalk;
import com.westar.base.model.ViewRecord;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.FileCenterService;
import com.westar.core.service.OnlineLearnService;
import com.westar.core.service.UploadService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

@Controller
@RequestMapping("/onlineLearn")
public class OnlineLearnController extends BaseController{

	@Autowired
	OnlineLearnService onlineLearnService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	ViewRecordService viewRecordService;

	
	
	@RequestMapping("/listPagedVideo")
	public ModelAndView listPagedVideo() {
		ModelAndView mav = new ModelAndView("/onlineLearn/learnCenter");
		UserInfo userInfo = this.getSessionUser();
				
		//先进行文件夹初始化
		fileCenterService.initFileClassify(userInfo,ConstantInterface.TYPE_LEARN);
				
		mav.addObject("userInfo",userInfo);
		mav.addObject("homeFlag",ConstantInterface.TYPE_LEARN);
		return mav;
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
		Map<String, Object> result = onlineLearnService.listPagedFolderAndFile(fileDetail, userInfo,pageNum);
		map.putAll(result);
		return map;
	}
	
	/**
	 * 新建文档页面跳转
	 * @param classifyId 文件夹主键
	 * @return
	 */
	@RequestMapping("/addVideoFilePage")
	public ModelAndView addFilePage(Integer classifyId) {
		UserInfo userInfo = this.getSessionUser();
		
		ModelAndView mav = new ModelAndView("/onlineLearn/addVideoFile");
		mav.addObject("userInfo",userInfo);
		if(classifyId == -1) {
			FileClassify classify = new FileClassify();
			classify.setId(-1);
			classify.setTypeName("所有文件");
			mav.addObject("classify", classify);
		}else {
			FileClassify classify = fileCenterService.getFileClassify(classifyId);
			mav.addObject("classify", classify);
		}
		return mav;
	}
	
	/**
	 * 条咋胡视屏查看界面
	 * @param uuid
	 * @return
	 */
	@RequestMapping("/viewVideoPage")
	public ModelAndView viewVideoPage(String uuid) {
		UserInfo userInfo = this.getSessionUser();
		//页面跳转
		ModelAndView mav = new ModelAndView("/onlineLearn/viewVideo");
		mav.addObject("userInfo", userInfo);
		Upfiles upfiles = uploadService.getFileByUUid(uuid);
		String url = upfiles.getFilepath();
		
		mav.addObject("url",url);
		mav.addObject("file",upfiles);
		return mav;
	}
	
	/**
	 * 视频播放
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/viewVideo",method = RequestMethod.POST)
	public  Map<String, Object>  viewVideo(HttpServletRequest request,String uuid) throws IOException {
		Map<String,Object> map = new HashMap<String, Object>();
		Upfiles upfiles = uploadService.getFileByUUid(uuid);
		String url = upfiles.getFilepath();
		
		map.put("url",url);
		map.put("file",upfiles);
		return map;
	}
	
	
	/**
	 * 视频讨论页面
	 * @param msgShareTalk
	 * @return
	 */
	@RequestMapping(value="/videoTalkPage")
	public ModelAndView videoTalkPage(HttpServletRequest request,VideoTalk videoTalk){
		ModelAndView view = new ModelAndView("/onlineLearn/videoTalk");
		view.addObject("fileTalk",videoTalk);
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		
		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), "023", videoTalk.getFileId());
		//取得是否添加浏览记录
		boolean bool = FreshManager.checkOpt(request, viewRecord);
		if(bool){
			//添加查看记录
			viewRecordService.addViewRecord(userInfo,viewRecord);
		}
		//信息讨论
		List<VideoTalk> videoTalks = onlineLearnService.
				listPagedVideoTalk(videoTalk.getFileId(),userInfo.getComId());
		Integer minId = null;
		if(videoTalks != null && videoTalks.size() > 0) {
			minId = videoTalks.get(videoTalks.size()-1).getId();
		}
		view.addObject("list",videoTalks);	
		view.addObject("minId",minId);	
		return view;
	}
	
	
	/**
	 * 在线学习讨论回复
	 * @param msgTalk
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/addVideoTalk",method = RequestMethod.POST)
	public Map<String, Object> addMsgShareTalk(VideoTalk videoTalk) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		videoTalk.setComId(sessionUser.getComId());
		videoTalk.setSpeaker(sessionUser.getId());
		//信息讨论回复
		Integer id = onlineLearnService.addVideoTalk(videoTalk,sessionUser);
		
		map.put("status", "y");
		map.put("id", id);
		//用于返回页面拼接代码
		VideoTalk videoTalks = onlineLearnService.getVideoTalk(id, sessionUser.getComId());
		map.put("videoTalk", videoTalks);
		map.put("sessionUser", this.getSessionUser());
		return map;
	}
	
	/**
	 * 删除讨论信息
	 * @param videoTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/delVideoTalk",method = RequestMethod.POST)
	public Map<String, Object> delVideoTalk(VideoTalk videoTalk,String delChildNode) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		videoTalk.setComId(sessionUser.getComId());
		//要删除的回复所有子节点和自己
		List<Integer> childIds = onlineLearnService.delVideoTalk(videoTalk,delChildNode,sessionUser);
		map.put("status", "y");
		map.put("childIds", childIds);
		return map;
	}
	
	/**
	 * 查看更多
	 * @param pageSize
	 * @param minId
	 * @param videoTalk
	 * @param modTypes
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/nextPageSizeVideoTalks")
	public List<VideoTalk> nextPageSizeVideoTalks(Integer pageSize,Integer minId,VideoTalk videoTalk,String modTypes) throws IOException, ParseException{
		UserInfo userInfo = this.getSessionUser();
		List<VideoTalk> msgShareTalks = onlineLearnService.nextPageSizeVideoTalks(videoTalk.getFileId(),userInfo.getComId(),pageSize,minId,"pc");
		return msgShareTalks;
	}
}