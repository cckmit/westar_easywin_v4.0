package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.FileClassify;
import com.westar.base.model.FileDetail;
import com.westar.base.model.FileViewScope;
import com.westar.base.model.FileViewScopeDep;
import com.westar.base.model.MsgShare;
import com.westar.base.model.MsgShareTalk;
import com.westar.base.model.ShareGroup;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.VideoTalk;
import com.westar.base.model.VideoTalkUpfile;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.StringUtil;
import com.westar.core.dao.OnlineLearnDao;
import com.westar.core.web.PaginationContext;

@Service
public class OnlineLearnService {

	@Autowired
	OnlineLearnDao onlineLearnDao;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	MsgShareService msgShareService;

	@Autowired
	UploadService uploadService;
	
	@Autowired
	SystemLogService systemLogService;

	@Autowired
	FileCenterService  fileCenterService;
	
	@Autowired
	ViewRecordService viewRecordService;
	/**
	 * 查询子文件夹
	 * @param fileDetail
	 * @return
	 */
	public List<FileClassify> listVideoFileClassify(FileDetail fileDetail,UserInfo curUser) {
		List<FileClassify> list = onlineLearnDao.listVideoFileClassify(fileDetail, curUser);
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
									&& StringUtils.isEmpty(endTime) ;
		
		
		if(pageNum == 0){
			List<FileClassify> folders = new  ArrayList<FileClassify>();
			if(searchFolderState){
				folders = fileCenterService.listFileClassify(fileDetail,userInfo);
			}
			map.put("folders", folders);
		}
		
		List<FileDetail> files = onlineLearnDao.listVideoFile(fileDetail, userInfo);
		PageBean<FileDetail> pageBean = new PageBean<FileDetail>();
		pageBean.setRecordList(files);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		map.put("pageBean", pageBean);
				
		return map;
	}
//	/**
//	 * 文件夹列表树
//	 * @param fileClassify
//	 * @param preDirs 
//	 * @return
//	 */
//	public List<FileClassify> listTreeDir(FileClassify fileClassify) {
//		return onlineLearnDao.listTreeDir(fileClassify);
//	}
	
	/**
	 * 视频讨论分页查询
	 * @param fileId
	 * @param comId
	 * @return
	 */
	public List<VideoTalk> listPagedVideoTalk(Integer fileId, Integer comId) {
		List<VideoTalk> list = onlineLearnDao.listPagedVideoTalk(fileId, comId);
		List<VideoTalk> msgShareTalks = new ArrayList<VideoTalk>();
		for (VideoTalk videoTalk : list) {
			// 回复内容转换
			videoTalk.setContent(StringUtil.toHtml(videoTalk.getContent()));
			// 讨论的附件
			videoTalk.setVideoTalkUpfiles(onlineLearnDao.listVideoTalkFile(comId, fileId, videoTalk.getId()));
			msgShareTalks.add(videoTalk);
		}
		return list;
	}
	
	/**
	 * 根据id查询详细信息
	 * @param id
	 * @param comId
	 * @return
	 */
	public VideoTalk getVideoTalk(Integer id, Integer comId) {
		VideoTalk videoTalk = onlineLearnDao.getVideoTalk(id, comId);
		if (null != videoTalk) {
			// 回复内容转换
			String content = StringUtil.toHtml(videoTalk.getContent());
			// 讨论的附件
			videoTalk.setVideoTalkUpfiles(
					onlineLearnDao.listVideoTalkFile(comId, videoTalk.getFileId(), videoTalk.getId()));
			videoTalk.setContent(content);
		}
		return videoTalk;
	}
	
	/**
	 * 添加讨论信息
	 * @param videoTalk
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public Integer addVideoTalk(VideoTalk videoTalk, UserInfo userInfo) throws Exception {
		Integer id = onlineLearnDao.add(videoTalk);
		// 讨论附件
		Integer[] upfilesId = videoTalk.getUpfilesId();
		if (null != upfilesId && upfilesId.length > 0) {
			for (Integer upfileId : upfilesId) {
				VideoTalkUpfile videoTalkUpfile = new VideoTalkUpfile();
				// 企业编号
				videoTalkUpfile.setComId(videoTalk.getComId());
				// 信息主键
				videoTalkUpfile.setFileId(videoTalk.getFileId());
				// 讨论的主键
				videoTalkUpfile.setVideoTalkId(id);
				// 附件主键
				videoTalkUpfile.setUpfileId(upfileId);
				// 上传人
				videoTalkUpfile.setUserId(videoTalk.getSpeaker());

				onlineLearnDao.add(videoTalkUpfile);
				// 为信息讨论附件创建索引
				uploadService.updateUpfileIndex(upfileId, userInfo, "add", videoTalk.getFileId(), "023");
			}
		}
		
		return id;
	}
	
	
	/**
	 * 删除讨论信息
	 * @param videoTalk
	 * @param delChildNode
	 * @param sessionUser
	 * @return
	 * @throws Exception 
	 */
	public List<Integer> delVideoTalk(VideoTalk videoTalk, String delChildNode, UserInfo userInfo) throws Exception {
		List<Integer> childIds = new ArrayList<Integer>();
		// 删除自己
		if (null == delChildNode) {
			childIds.add(videoTalk.getId());
			// 讨论的附件
			List<VideoTalkUpfile> videoTalkUpfiles = onlineLearnDao.listVideoTalkFile(videoTalk.getComId(),
					videoTalk.getFileId(), videoTalk.getId());
			for (VideoTalkUpfile videoTalkUpfile : videoTalkUpfiles) {
				// 文件详情
				FileDetail fileDetail = new FileDetail();
				// 企业编号
				fileDetail.setComId(videoTalk.getComId());
				// 所属模块
				fileDetail.setModuleType(ConstantInterface.TYPE_LEARN);
				// 文件主键
				fileDetail.setFileId(videoTalkUpfile.getUpfileId());
				
				fileCenterService.delFile(fileDetail, userInfo);
				// 删除附件讨论
				this.delVideoFileTalk(videoTalk.getComId(), videoTalkUpfile.getFileId(),
						videoTalkUpfile.getUpfileId());
			}
			// 删除附件
			onlineLearnDao.delByField("videoTalkUpfile", new String[] { "comId", "videoTalkId" },
					new Object[] { videoTalk.getComId(), videoTalk.getId() });

			onlineLearnDao.delById(VideoTalk.class, videoTalk.getId());
		} else if ("yes".equals(delChildNode)) {// 删除自己和所有的子节点
			// 待删除的讨论
			List<VideoTalk> listMsgShareTalk = onlineLearnDao.listVideoTalkForDel(videoTalk.getComId(),
					videoTalk.getId());
			for (VideoTalk talk : listMsgShareTalk) {
				childIds.add(talk.getId());
				// 讨论的附件
				List<VideoTalkUpfile> videoTalkUpfiles = onlineLearnDao.listVideoTalkFile(talk.getComId(),
						talk.getFileId(), talk.getId());
				for (VideoTalkUpfile videoTalkUpfile : videoTalkUpfiles) {
					// 文件详情
					FileDetail fileDetail = new FileDetail();
					// 企业编号
					fileDetail.setComId(videoTalk.getComId());
					// 所属模块
					fileDetail.setModuleType(ConstantInterface.TYPE_WEEKTALK);
					// 文件主键
					fileDetail.setFileId(videoTalkUpfile.getUpfileId());
					fileCenterService.delFile(fileDetail, userInfo);
					// 删除附件讨论
					this.delVideoFileTalk(videoTalk.getComId(), videoTalkUpfile.getFileId(),
							videoTalkUpfile.getUpfileId());
				}

				// 删除附件
				onlineLearnDao.delByField("videoTalkUpfile", new String[] { "comId", "videoTalkId" },
						new Object[] { talk.getComId(), talk.getId() });
			}
			// 删除当前节点及其子节点回复
			onlineLearnDao.delVideoTalk(videoTalk.getId(), videoTalk.getComId());
		} else if ("no".equals(delChildNode)) {// 删除自己,将子节点提高一级
			childIds.add(videoTalk.getId());
			// 讨论的附件
			List<VideoTalkUpfile> videoTalkUpfiles = onlineLearnDao.listVideoTalkFile(videoTalk.getComId(),
					videoTalk.getFileId(), videoTalk.getId());
			for (VideoTalkUpfile videoTalkUpfile : videoTalkUpfiles) {
				// 文件详情
				FileDetail fileDetail = new FileDetail();
				// 企业编号
				fileDetail.setComId(videoTalk.getComId());
				// 所属模块
				fileDetail.setModuleType(ConstantInterface.TYPE_SHARETALK);
				// 文件主键
				fileDetail.setFileId(videoTalkUpfile.getUpfileId());
				
				fileCenterService.delFile(fileDetail, userInfo);

				// 删除附件讨论
				this.delVideoFileTalk(videoTalk.getComId(), videoTalkUpfile.getFileId(),
						videoTalkUpfile.getUpfileId());
			}

			// 删除附件
			onlineLearnDao.delByField("videoTalkUpfile", new String[] { "comId", "videoTalkId" },
					new Object[] { videoTalk.getComId(), videoTalk.getId() });

			onlineLearnDao.updateVideoTalkParentId(videoTalk.getId(), videoTalk.getComId());
			onlineLearnDao.delById(VideoTalk.class, videoTalk.getId());
		}
		
		return childIds;
	}
	
	
	/**
	 * 删除模块附件讨论
	 * @param comId
	 * @param fileId
	 * @param upfileId
	 */
	private void delVideoFileTalk(Integer comId, Integer fileId, Integer upfileId) {
		// 查询该模块其他评论是否有相同的附件
		List<VideoTalkUpfile> list = onlineLearnDao.listVideoSimUpfiles(comId, fileId, upfileId);
		// 在其他地方没有使用该附件，则删除附件评论,；在其他地方有使用该附件，则不处理
		if (null != list && list.size() == 1) {
			onlineLearnDao.delByField("fileTalk", new String[] { "comId", "fileId", "busId", "busType" },
					new Object[] { comId, upfileId, fileId, "023" });
		}

	}
	
	/**
	 * 留言查看更多
	 * @param fileId
	 * @param comId
	 * @param pageSize
	 * @param minId
	 * @param dest
	 * @return
	 */
	public List<VideoTalk> nextPageSizeVideoTalks(Integer fileId, Integer comId, Integer pageSize, Integer minId,
			String dest) {
		// 信息讨论分页
		List<VideoTalk> list = onlineLearnDao.nextPageSizeMsgTalks(fileId, comId,minId, pageSize);
		List<VideoTalk> videoTalks = new ArrayList<VideoTalk>();
		for (VideoTalk videoTalk : list) {
			if (!dest.equals("app")) {// 手机端不需要字符转换
				// 回复内容转换
				videoTalk.setContent(StringUtil.toHtml(videoTalk.getContent()));
			}
			// 讨论的附件
			videoTalk.setVideoTalkUpfiles(onlineLearnDao.listVideoTalkFile(comId, fileId, videoTalk.getId()));
			videoTalks.add(videoTalk);
		}
		return list;
	}
		
}
