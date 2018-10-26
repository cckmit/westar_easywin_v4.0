package com.westar.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.westar.base.enums.PubStateEnum;
import com.westar.base.model.Daily;
import com.westar.base.model.DailyQ;
import com.westar.base.model.DailyShareGroup;
import com.westar.base.model.FileDetail;
import com.westar.base.model.MsgShare;
import com.westar.base.model.MsgShareLog;
import com.westar.base.model.MsgShareTalk;
import com.westar.base.model.MsgShareTalkUpfile;
import com.westar.base.model.ShareGroup;
import com.westar.base.model.ShareUser;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.Vote;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.MsgShareDao;
import com.westar.core.thread.IndexUpdateThread;

@Service
public class MsgShareService {
	
	@SuppressWarnings("unused")
	private static final Log loger = LogFactory.getLog(MsgShareService.class);

	@Autowired
	MsgShareDao msgShareDao;

	@Autowired
	VoteService voteService;

	@Autowired
	JiFenService jifenService;

	@Autowired
	FileCenterService fileCenterService;

	@Autowired
	IndexService indexService;

	@Autowired
	UploadService uploadService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	AttentionService attentionService;

	@Autowired
	ClockService clockService;

	@Autowired
	ChatService chatService;

	@Autowired
	MeetingService meetingService;

	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	TodayWorksService todayWorksService;

	@Autowired
	UserConfService userConfService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	DailyService dailyService;


	/**
	 * 新增分享信息
	 * 
	 * @param msgShareParam
	 */
	public Integer addMsgShare(MsgShare msgShareParam,UserInfo sessionUser) {
		
		MsgShare share = msgShareDao.getMsgShareByModId(msgShareParam.getType(), msgShareParam.getModId(), sessionUser.getComId(), 0);
		Integer id = 0;
		if(null == share){
			// 新增分享信息
			id = msgShareDao.add(msgShareParam);
		}else{
			id = share.getId();
			msgShareParam.setId(id);
			String content = share.getContent() +  msgShareParam.getContent();
			msgShareParam.setContent(content);
			msgShareDao.update(msgShareParam);
		}
		// 配置信息分享范围
		List<ShareGroup> listShareGroup = msgShareParam.getShareGroup();
		if (null != listShareGroup && !listShareGroup.isEmpty()) {
			for (ShareGroup shareGroup : listShareGroup) {
				// 分享信息主键
				shareGroup.setMsgId(id);
				// 添加分享范围
				msgShareDao.add(shareGroup);
			}
		}
		
		//添加分享的推送对象
		List<ShareUser> listShareUser = msgShareParam.getListShareUser();
		if(null!=listShareUser && !listShareUser.isEmpty()){
			for (ShareUser shareUser : listShareUser) {
				shareUser.setComId(sessionUser.getComId());
				shareUser.setMsgId(id);
				msgShareDao.add(shareUser);
			}
		}
		
		// 保存最近使用的分组
		this.updateUsedGrp(msgShareParam);
		
		return id;
	}

	/**
	 * 更新最近使用过的分组
	 * 
	 * @param msgShare
	 */
	public void updateUsedGrp(MsgShare msgShare) {
		if (null == msgShare) {
			return;
		}
		// 配置信息分享范围
		List<ShareGroup> listShareGroup = msgShare.getShareGroup();
		String grpIds = "";
		if (null != listShareGroup && listShareGroup.size() > 0) {
			for (ShareGroup shareGroup : listShareGroup) {
				grpIds += shareGroup.getGrpId() + ",";
			}
		}
		grpIds += "-1";
		// 是否需要重新保存用过的分组，默认不需要
		boolean isResave = false;
		Integer leaveCount = msgShareDao.countDiffGroup(msgShare.getComId(), msgShare.getCreator(),
				msgShare.getScopeType(), grpIds);
		if (1 == msgShare.getScopeType()) {// 自定义分组
			if (leaveCount > 0) {// 本次分组与上次分组不同
				isResave = true;
			}
		} else {
			if (leaveCount == 0) {// 非自定义，历史数据中没有
				isResave = true;
			}
		}
		// 需要更新
		if (isResave) {
			// 删除上次分组
			msgShareDao.delByField("usedGroup", new String[] { "comId", "userId" },
					new Object[] { msgShare.getComId(), msgShare.getCreator() });
			// 有分组
			if (null != listShareGroup && listShareGroup.size() > 0) {
				for (ShareGroup shareGroup : listShareGroup) {
					// 存入用过的分组信息
					UsedGroup usedGrp = new UsedGroup();
					// 分组主键
					usedGrp.setGrpId(shareGroup.getGrpId());
					// 操作人员
					usedGrp.setUserId(msgShare.getCreator());
					// 企业编号
					usedGrp.setComId(shareGroup.getComId());
					// 分组类型
					usedGrp.setGroupType(msgShare.getScopeType().toString());
					msgShareDao.add(usedGrp);
				}
			} else {
				// 存入用过的分组信息
				UsedGroup usedGrp = new UsedGroup();
				// 操作人员
				usedGrp.setUserId(msgShare.getCreator());
				// 企业编号
				usedGrp.setComId(msgShare.getComId());
				usedGrp.setGrpId(0);
				// 分组类型
				usedGrp.setGroupType(msgShare.getScopeType().toString());
				msgShareDao.add(usedGrp);
			}
		}
	}

	/**
	 * 获取分享信息集合
	 * 
	 * @param userId
	 *            登录人
	 * @param pageSize
	 *            信息显示数量
	 * @param modList
	 * @param minId 
	 * @return
	 * @throws ParseException
	 */
	public List<MsgShare> listMsgShare(Integer comid,Integer userId, Integer pageSize,  MsgShare msgShare,
			List<String> modList, Integer minId) throws ParseException {
		List<MsgShare> list = msgShareDao.listMsgShare(comid,userId, pageSize,  msgShare, modList,minId);
		List<MsgShare> msgShares = new ArrayList<MsgShare>();
		for (MsgShare obj : list) {
			if (ConstantInterface.TYPE_VOTE.equals(obj.getType()) && "0".equals(obj.getIsDel())) {// 未删除的投票
				// 当前操作员的投票详情
				Vote vote = voteService.getVoteInfo(obj.getModId(), msgShare.getComId(), userId);
				obj.setVote(vote);

			}
			msgShares.add(obj);
			//去除html标签，因为前端截取了字符串，如果将标签截取掉一半则会出现样式错乱。
			obj.setContent(obj.getContent().replaceAll("<[.[^>]]*>",""));
		}
		return msgShares;
	}

	/**
	 * 个人分享信息数
	 *
	 * @param comId
	 * @param userId
	 * @param msgShare
	 * @param modList
	 * @return
	 */
	public Integer getCountMsg(Integer comId, Integer userId, MsgShare msgShare, List<String> modList) {
		Integer msgShares = msgShareDao.getCountMsg(comId, userId, msgShare, modList);
		return msgShares;
	}

	/**
	 * 根据ID获取分享信息内容
	 *
	 * @param comId
	 * @param id
	 * @param type
	 * @return
	 */
	public MsgShare viewMsgShareById(Integer userId, Integer comId, Integer id, String type) {
		MsgShare msgShare = msgShareDao.viewMsgShareById(userId, comId, id, type);
		return msgShare;
	}

	/**
	 * 根据ID获取分享信息内容
	 *
	 * @param comId
	 * @param id
	 * @return
	 */
	public MsgShare getMsgShareById(Integer comId, Integer id) {
		return msgShareDao.getMsgShareById(comId, id);
	}

	/**
	 * 信息讨论回复
	 *
	 * @param msgTalk
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public Integer addMsgShareTalk(MsgShareTalk msgTalk, UserInfo userInfo) throws Exception {
		Integer id = msgShareDao.add(msgTalk);
		// 讨论附件
		Integer[] upfilesId = msgTalk.getUpfilesId();
		if (null != upfilesId && upfilesId.length > 0) {
			for (Integer upfileId : upfilesId) {
				MsgShareTalkUpfile magSgareTalkFile = new MsgShareTalkUpfile();
				// 企业编号
				magSgareTalkFile.setComId(msgTalk.getComId());
				// 信息主键
				magSgareTalkFile.setMsgId(msgTalk.getMsgId());
				// 讨论的主键
				magSgareTalkFile.setMsgShareTalkId(id);
				// 附件主键
				magSgareTalkFile.setUpfileId(upfileId);
				// 上传人
				magSgareTalkFile.setUserId(msgTalk.getSpeaker());

				msgShareDao.add(magSgareTalkFile);
				// 为信息讨论附件创建索引
				uploadService.updateUpfileIndex(upfileId, userInfo, "add", msgTalk.getMsgId(), "1");
			}
		}

		//添加信息分享人员
		List<ShareUser> listShareUser = msgTalk.getListShareUser();
		Set<Integer> pushUserIdSet = new HashSet<Integer>();
		if(null != listShareUser && !listShareUser.isEmpty()){
			for (ShareUser shareUser : listShareUser) {
				//人员主键
				Integer userId = shareUser.getUserId();
				pushUserIdSet.add(userId);
				//删除上次的分享人员
				msgShareDao.delByField("shareUser", new String[]{"comId","msgId","userId"},
						new Object[]{userInfo.getComId(),msgTalk.getMsgId(),userId});
				shareUser.setMsgId(msgTalk.getMsgId());
				shareUser.setComId(userInfo.getComId());
				msgShareDao.add(shareUser);
			}
		}

		//分享信息查看
		List<UserInfo> shares = new ArrayList<UserInfo>();
		MsgShare msgShare = (MsgShare) msgShareDao.objectQuery(MsgShare.class, msgTalk.getMsgId());
		if (null != msgShare) {
			//查询消息的推送人员
			shares = msgShareDao.listPushTodoUser(msgTalk.getMsgId(), userInfo.getComId(),pushUserIdSet);

			Iterator<UserInfo> userids =  shares.iterator();
			for(;userids.hasNext();){
				UserInfo user = userids.next();
				if(user.getId().equals(userInfo.getId())){
					userids.remove();
					shares.remove(user);
				}
				//设置全部普通消息
				todayWorksService.updateTodayWorksBusSpecTo0(ConstantInterface.TYPE_DAILY, msgTalk.getMsgId(), user.getId());
			}
			// 添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_DAILY, msgTalk.getMsgId(), "参与分享讨论:" + msgTalk.getContent(),
					shares, userInfo.getId(), 1);

			//清除上次查看记录
			viewRecordService.delViewRecord(userInfo,shares,msgTalk.getMsgId(),ConstantInterface.TYPE_DAILY);
		}

		// 分享信息索引更新
		// this.updateMsgIndex(msgTalk.getMsgId(), userInfo, "update");

		// 修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), "1", "参与分享信息讨论", msgTalk.getMsgId());

		return id;
	}

	/**
	 * 模块日志添加
	 *
	 * @param comId
	 *            企业编号
	 * @param msgId
	 *            信息主键
	 * @param userId
	 *            发言人主键
	 * @param userName
	 *            发言人姓名
	 * @param content
	 *            日志内容
	 */
	public void addMagShareRepLog(Integer comId, Integer msgId, Integer userId, String userName, String content) {
		MsgShareLog msgShareLog = new MsgShareLog(comId, msgId, userId, content, userName);
		msgShareDao.add(msgShareLog);

	}

	/**
	 * 主键查找信息讨论内容
	 *
	 * @param id
	 *            讨论主键
	 * @param comId
	 *            企业号
	 * @return
	 */
	public MsgShareTalk getMagshareTalk(Integer id, Integer comId) {
		MsgShareTalk magShareTalk = msgShareDao.getMagShareTalk(id, comId);
		if (null != magShareTalk) {
			// 回复内容转换
			String content = StringUtil.toHtml(magShareTalk.getContent());
			// 讨论的附件
			magShareTalk.setMsgShareTalkUpfiles(
					msgShareDao.listMagShareTalkFile(comId, magShareTalk.getMsgId(), magShareTalk.getId()));
			magShareTalk.setContent(content);
		}
		return magShareTalk;
	}

	/**
	 * 信息讨论
	 *
	 * @param msgId
	 * @param comId
	 * @return
	 */
	public List<MsgShareTalk> listPagedMsgShareTalk(Integer msgId, Integer comId, String dest) {
		// 信息讨论分页
		List<MsgShareTalk> list = msgShareDao.listPagedMsgShareTalk(msgId, comId);
		List<MsgShareTalk> msgShareTalks = new ArrayList<MsgShareTalk>();
		for (MsgShareTalk msgShareTalk : list) {
			if (null!=dest && !dest.equals("app")) {// 手机端不需要字符转换
				// 回复内容转换
				msgShareTalk.setContent(StringUtil.toHtml(msgShareTalk.getContent()));
			}
			// 讨论的附件
			msgShareTalk.setMsgShareTalkUpfiles(msgShareDao.listMsgShareTalkFile(comId, msgId, msgShareTalk.getId()));
			msgShareTalks.add(msgShareTalk);
		}
		return list;
	}

	public List<MsgShareTalk> listMsgShareTalk(Integer msgId, Integer comId) {
		// 信息讨论分页
		List<MsgShareTalk> list = msgShareDao.listMsgShareTalk(msgId, comId);
		List<MsgShareTalk> msgShareTalks = new ArrayList<MsgShareTalk>();
		for (MsgShareTalk msgShareTalk : list) {
			// 讨论的附件
			msgShareTalk.setMsgShareTalkUpfiles(msgShareDao.listMsgShareTalkFile(comId, msgId, msgShareTalk.getId()));
			msgShareTalks.add(msgShareTalk);
		}
		return list;
	}



	/**
	 * 要删除的回复所有子节点和自己
	 *
	 * @param msgShareTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception
	 */
	public List<Integer> delMsgShareTalk(MsgShareTalk msgShareTalk, String delChildNode, UserInfo userInfo)
			throws Exception {
		List<Integer> childIds = new ArrayList<Integer>();
		// 删除自己
		if (null == delChildNode) {
			childIds.add(msgShareTalk.getId());
			// 讨论的附件
			List<MsgShareTalkUpfile> msgShareTalkUpfiles = msgShareDao.listMsgShareTalkFile(msgShareTalk.getComId(),
					msgShareTalk.getMsgId(), msgShareTalk.getId());
			for (MsgShareTalkUpfile msgShareTalkUpfile : msgShareTalkUpfiles) {
				// 文件详情
				FileDetail fileDetail = new FileDetail();
				// 企业编号
				fileDetail.setComId(msgShareTalk.getComId());
				// 所属模块
				fileDetail.setModuleType(ConstantInterface.TYPE_SHARETALK);
				// 文件主键
				fileDetail.setFileId(msgShareTalkUpfile.getUpfileId());
				fileCenterService.delFile(fileDetail, userInfo);
				// 删除附件讨论
				this.delMsgFileTalk(msgShareTalk.getComId(), msgShareTalkUpfile.getMsgId(),
						msgShareTalkUpfile.getUpfileId());
			}
			// 删除附件
			msgShareDao.delByField("msgShareTalkUpfile", new String[] { "comId", "msgShareTalkId" },
					new Object[] { msgShareTalk.getComId(), msgShareTalk.getId() });

			msgShareDao.delById(MsgShareTalk.class, msgShareTalk.getId());
		} else if ("yes".equals(delChildNode)) {// 删除自己和所有的子节点
			// 待删除的讨论
			List<MsgShareTalk> listMsgShareTalk = msgShareDao.listMsgShareTalkForDel(msgShareTalk.getComId(),
					msgShareTalk.getId());
			for (MsgShareTalk talk : listMsgShareTalk) {
				childIds.add(talk.getId());
				// 讨论的附件
				List<MsgShareTalkUpfile> msgShareTalkUpfiles = msgShareDao.listMsgShareTalkFile(talk.getComId(),
						talk.getMsgId(), talk.getId());
				for (MsgShareTalkUpfile msgShareTalkUpfile : msgShareTalkUpfiles) {
					// 文件详情
					FileDetail fileDetail = new FileDetail();
					// 企业编号
					fileDetail.setComId(msgShareTalk.getComId());
					// 所属模块
					fileDetail.setModuleType(ConstantInterface.TYPE_WEEKTALK);
					// 文件主键
					fileDetail.setFileId(msgShareTalkUpfile.getUpfileId());
					fileCenterService.delFile(fileDetail, userInfo);
					// 删除附件讨论
					this.delMsgFileTalk(msgShareTalk.getComId(), msgShareTalkUpfile.getMsgId(),
							msgShareTalkUpfile.getUpfileId());
				}

				// 删除附件
				msgShareDao.delByField("msgShareTalkUpfile", new String[] { "comId", "msgShareTalkId" },
						new Object[] { talk.getComId(), talk.getId() });
			}
			// 删除当前节点及其子节点回复
			msgShareDao.delMsgShareTalk(msgShareTalk.getId(), msgShareTalk.getComId());
		} else if ("no".equals(delChildNode)) {// 删除自己,将子节点提高一级
			childIds.add(msgShareTalk.getId());
			// 讨论的附件
			List<MsgShareTalkUpfile> msgShareTalkUpfiles = msgShareDao.listMsgShareTalkFile(msgShareTalk.getComId(),
					msgShareTalk.getMsgId(), msgShareTalk.getId());
			for (MsgShareTalkUpfile msgShareTalkUpfile : msgShareTalkUpfiles) {
				// 文件详情
				FileDetail fileDetail = new FileDetail();
				// 企业编号
				fileDetail.setComId(msgShareTalk.getComId());
				// 所属模块
				fileDetail.setModuleType(ConstantInterface.TYPE_SHARETALK);
				// 文件主键
				fileDetail.setFileId(msgShareTalkUpfile.getUpfileId());
				fileCenterService.delFile(fileDetail, userInfo);

				// 删除附件讨论
				this.delMsgFileTalk(msgShareTalk.getComId(), msgShareTalkUpfile.getMsgId(),
						msgShareTalkUpfile.getUpfileId());
			}

			// 删除附件
			msgShareDao.delByField("msgShareTalkUpfile", new String[] { "comId", "msgShareTalkId" },
					new Object[] { msgShareTalk.getComId(), msgShareTalk.getId() });

			msgShareDao.updateMsgShareTalkParentId(msgShareTalk.getId(), msgShareTalk.getComId());
			msgShareDao.delById(MsgShareTalk.class, msgShareTalk.getId());
		}

		// 分享信息详情
		MsgShare msgShare = (MsgShare) msgShareDao.objectQuery(MsgShare.class, msgShareTalk.getMsgId());
		if (null != msgShare) {// 分享信息不为空
			// 消息推送对象
			List<UserInfo> shares = new ArrayList<UserInfo>();
			if (msgShare.getScopeType() == 0) {// 所有人
				shares = userInfoService.listUser(userInfo.getComId());
			} else if (msgShare.getScopeType() == 1) {// 自定义
				shares = msgShareDao.listShareUser(msgShareTalk.getMsgId(), userInfo.getComId());
			}

			// 分享信息内容
			String content = msgShare.getContent();
			// 分享信息内容截取
			content = StringUtil.cutStrFace(content, 40);
			// 添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo, null, msgShareTalk.getMsgId(), "删除分享信息\"" + content + "\"的讨论", ConstantInterface.TYPE_DAILY, shares,
					null);

			// 修改积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_SHARETALKDEL,
					"删除分享信息(" + content + ")的讨论", msgShareTalk.getMsgId());
		}
		// 分享信息索引更新
		// this.updateMsgIndex(msgShareTalk.getMsgId(), userInfo, "update");
		return childIds;
	}

	/**
	 * 分页查询信息日志
	 *
	 * @param msgId
	 *            信息主键
	 * @param comId
	 *            企业号
	 * @return
	 */
	public List<MsgShareLog> listPagedMsgShareLog(Integer msgId, Integer comId) {
		// 信息日志
		List<MsgShareLog> logs = msgShareDao.listPagedMsgShareLog(msgId, comId);
		return logs;
	}
	/**
	 * 不分页查询信息日志
	 * @param msgId 分享主键
	 * @param comId 团队号
	 * @return
	 */
	public List<MsgShareLog> listMsgShareLog(Integer msgId, Integer comId) {
		// 信息日志
		List<MsgShareLog> logs = msgShareDao.listShareLog(msgId, comId);
		return logs;
	}

	/**
	 * 不分页查询信息日志
	 * @param msgId 分享主键
	 * @param comId 团队号
	 * @return
	 */
	public List<MsgShareLog> listMsgShareLogOfAll(Integer msgId, Integer comId) {
		// 信息日志
		List<MsgShareLog> logs = msgShareDao.listShareLogOfAll(msgId, comId);
		return logs;
	}

	/**
	 * 信息附件分页
	 *
	 * @param comId
	 * @param msgId
	 * @return
	 */
	public List<MsgShareTalkUpfile> listPagedMsgShareFiles(Integer comId, Integer msgId) {
		// 信息附件分页
		List<MsgShareTalkUpfile> list = msgShareDao.listPagedMsgShareFiles(comId, msgId);
		return list;
	}
	/**
	 * 自定义的分组成员
	 *
	 * @param comId
	 * @param grpIds
	 * @return
	 */
	public List<UserInfo> listGroupUser(Integer comId, String grpIds) {
		return msgShareDao.listGroupUser(comId, grpIds);
	}
	/**
	 * 删除分享信息信息
	 *
	 * @param id
	 *            信息分享主键
	 * @param userInfo
	 *            session用户
	 * @throws Exception
	 */
	public void delShareMsg(Integer id, UserInfo userInfo) throws Exception {
		// 分享信息索引更新
		this.updateMsgIndex(id, userInfo, "del");
		// 删除日志
		msgShareDao.delByField("msgShareLog", new String[] { "comId", "msgId" },
				new Object[] { userInfo.getComId(), id });
		// 删除讨论附件
		msgShareDao.delByField("msgShareTalkUpfile", new String[] { "comId", "msgId" },
				new Object[] { userInfo.getComId(), id });
		// 删除讨论
		msgShareDao.delByField("msgShareTalk", new String[] { "comId", "msgId" },
				new Object[] { userInfo.getComId(), id });
		// 删除讨论
		msgShareDao.delByField("shareGroup", new String[] { "comId", "msgId" },
				new Object[] { userInfo.getComId(), id });
		// 删除数据更新记录
		msgShareDao.delByField("todayWorks", new String[] { "comId", "busType", "busId" },
				new Object[] { userInfo.getComId(), "1", id });

		// 删除浏览记录
		msgShareDao.delByField("viewRecord", new String[] { "comId", "busId", "busType" },
				new Object[] { userInfo.getComId(), id, "1" });
		// 关注信息
		msgShareDao.delByField("attention", new String[] { "comId", "busId", "busType" },
				new Object[] { userInfo.getComId(), id, "1" });
		// 最新动态
		msgShareDao.delByField("newsInfo", new String[] { "comId", "busId", "busType" },
				new Object[] { userInfo.getComId(), id, "1" });

		// 删除聊天记录
		chatService.delBusChat(userInfo.getComId(), id, "1");

		// 分享信息的附件
		List<MsgShareTalkUpfile> files = msgShareDao.listMsgShareTalkFiles(userInfo.getComId(), id);
		if (null != files) {// 若是有附件，则删除附件讨论信息
			for (MsgShareTalkUpfile msgShareTalkUpfile : files) {
				this.delMsgFileTalk(userInfo.getComId(), id, msgShareTalkUpfile.getUpfileId());
			}
		}
		// 添加工作轨迹
		this.delShareMsg("1", id, userInfo, 0);

	}

	/**
	 * 保存上次使用的分组信息 与addMsgShare 相似
	 *
	 * @param msgShare
	 * @param userInfo
	 */
	public void addUserGrp(MsgShare msgShare, UserInfo userInfo) {
		if (null != msgShare) {
			// 删除上次分组
			msgShareDao.delByField("usedGroup", new String[] { "comId", "userId" },
					new Object[] { userInfo.getComId(), userInfo.getId() });
			// 分享范围设置
			if (null != msgShare.getShareGroup() && msgShare.getShareGroup().size() > 0) {// 指定了分组
				for (ShareGroup shareGrp : msgShare.getShareGroup()) {

					// 存入用过的分组信息
					UsedGroup usedGrp = new UsedGroup();
					// 分组主键
					usedGrp.setGrpId(shareGrp.getGrpId());
					// 操作人员
					usedGrp.setUserId(userInfo.getId());
					// 企业编号
					usedGrp.setComId(userInfo.getComId());
					// 分组类型
					usedGrp.setGroupType(msgShare.getScopeType().toString());
					msgShareDao.add(usedGrp);
				}
			} else {// 所有人或是自己
					// 存入用过的分组信息
				UsedGroup usedGrp = new UsedGroup();
				// 操作人员
				usedGrp.setUserId(userInfo.getId());
				// 企业编号
				usedGrp.setComId(userInfo.getComId());
				// 默认为0
				usedGrp.setGrpId(0);
				// 分组类型
				usedGrp.setGroupType(msgShare.getScopeType().toString());
				msgShareDao.add(usedGrp);
			}
		}
	}
	/**
	 * 指定分组的人员
	 *
	 * @param grpIds
	 *            拼接的分组主键
	 * @param comId
	 *            企业号
	 * @return
	 */
	public List<UserInfo> listScopeUser(String grpIds, Integer comId) {
		List<UserInfo> shares = msgShareDao.listScopeUser(grpIds, comId);
		return shares;
	}

	/**
	 * 通过模块取得分享信息内容
	 *
	 * @param type
	 *            业务类型
	 * @param modId
	 *            业务主键
	 * @param traceType
	 *            工作类型 0分享 1轨迹
	 * @return
	 */
	public void delShareMsg(String type, Integer modId, UserInfo userInfo, Integer traceType) {
		// 通过模块取得分享信息内容
		MsgShare msgShare = msgShareDao.getMsgShareByModId(type, modId, userInfo.getComId(), traceType);
		if (null != msgShare) {
			// 删除分组
			msgShareDao.delByField("shareGroup", new String[] { "comId", "msgId" },
					new Object[] { userInfo.getComId(), msgShare.getId() });
			// 删除信息分享
			msgShareDao.delById(MsgShare.class, msgShare.getId());
			if (msgShare.getType().equals(ConstantInterface.TYPE_DAILY) || msgShare.getType().equals("1") || msgShare.getType().equals(ConstantInterface.TYPE_SHARE)) {
				// 分享信息内容
				String content = msgShare.getContent();
				// 分享信息内容截取
				content = StringUtil.cutStrFace(content, 40);
				//添加工作轨迹
				systemLogService.addSystemLogWithTrace(userInfo,userInfo.getId(), ConstantInterface.TYPE_SHARE,
						msgShare.getId(), "删除分享信息\"" + content + "\"", "删除分享信息\"" + content + "\"");
			}

		}

	}


	/**
	 * 删除模块先附件讨论
	 *
	 * @param comId
	 *            企业号
	 * @param msgId
	 *            模块主键
	 * @param upfileId
	 *            附件主键
	 */
	private void delMsgFileTalk(Integer comId, Integer msgId, Integer upfileId) {
		// 查询该模块其他评论是否有相同的附件
		List<MsgShareTalkUpfile> list = msgShareDao.listMsgSimUpfiles(comId, msgId, upfileId);
		// 在其他地方没有使用该附件，则删除附件评论,；在其他地方有使用该附件，则不处理
		if (null != list && list.size() == 1) {
			msgShareDao.delByField("fileTalk", new String[] { "comId", "fileId", "busId", "busType" },
					new Object[] { comId, upfileId, msgId, "1" });
		}

	}

	/**
	 * 查询信息分享的人员
	 *
	 * @param comId
	 *            企业号
	 * @return
	 */
	public List<UserInfo> listShareUser(Integer busId, Integer comId) {
		List<UserInfo> list = msgShareDao.listShareUser(busId, comId);
		return list;
	}

	/**
	 * 查询信息分享的人员
	 *
	 * @param comId
	 *            企业号
	 * @return
	 */
	public List<ShareUser> listAllShareUser(Integer busId, Integer comId) {
		List<ShareUser> list = msgShareDao.listAllShareUser(busId, comId);
		return list;
	}

	/**
	 * 查询信息分享的人员
	 *
	 * @param comId
	 *            企业号
	 * @return
	 */
	public List<ShareGroup> listShareGroup(Integer busId, Integer comId) {
		List<ShareGroup> list = msgShareDao.listShareGroup(busId, comId);
		return list;
	}
	/**
	 * 更新信息索引
	 *
	 * @param msgId
	 *            信息主键
	 * @param userInfo
	 *            当前操作人员
	 * @param opType
	 *            操作类型;添加?更新?
	 * @throws Exception
	 */
	public void updateMsgIndex(Integer msgId, UserInfo userInfo, String opType) throws Exception {
		// 更新任务索引
		MsgShare msgShareT = msgShareDao.getMsgShareById(userInfo.getComId(), msgId);
		if (null == msgShareT) {
			return;
		}
		if (!msgShareT.getType().equals("1")) {
			return;
		}
		MsgShare msgShare = new MsgShare();
		msgShare.setRecordCreateTime(msgShareT.getRecordCreateTime());
		msgShare.setContent(msgShareT.getContent());
		msgShare.setCreatorName(msgShareT.getCreatorName());

		// StringBuffer attStr = new
		// StringBuffer(CommonUtil.objAttr2String(msgShare,null));
		StringBuffer attStr = new StringBuffer(msgShare.getContent());

		// 信息分享人员
		// List<UserInfo> shares = msgShareDao.listShareUser(msgId,
		// userInfo.getComId());
		// if(null!= shares && shares.size()>0){
		// for (UserInfo userObj : shares) {
		// attStr.append(userObj.getUserName()+",");
		// }
		// }
		// 留言
		// List<MsgShareTalk> talks =
		// msgShareDao.listMsgShareTalk4Index(userInfo.getComId(), msgId);
		// if(null!=talks && talks.size()>0){
		// for (MsgShareTalk vo : talks) {
		// attStr.append(vo.getContent()+","+vo.getTalkerName()+",");
		// }
		// }
		// 留言附件
		// List<MsgShareTalkUpfile> upfiles =
		// msgShareDao.listMsgShareTalkFiles(userInfo.getComId(), msgId);
		// if(null!= upfiles && upfiles.size()>0){
		// Upfiles upfile = null;
		// for (MsgShareTalkUpfile vo : upfiles) {
		// //附件内容添加
		// upfile =
		// uploadService.queryUpfile4Index(vo.getUpfileId(),userInfo.getComId());
		// //附件名称
		// attStr.append(upfile.getFilename()+",");
		// //附件内容
		// attStr.append(upfile.getFileContent()+",");
		// }
		// }

		// 单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String index_key = userInfo.getComId() + "_1_" + msgId;
		// 为任务创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(index_key, userInfo.getComId(), msgId, "1",
				msgShare.getContent(), attStr.toString(), DateTimeUtil.parseDate(msgShare.getRecordCreateTime(), 0));
		if (null != listIndexDoc) {
			// 根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType, indexService, userInfo, listIndexDoc, index_key));
		}

	}

	/**
	 * 获取团队所有分享信息
	 *
	 * @param userInfo
	 * @return
	 */
	public List<MsgShare> listMsgShareOfAll(UserInfo userInfo) {
		return msgShareDao.listMsgShareOfAll(userInfo);
	}

	/**
	 *
	 * 信息查看权限验证
	 *
	 * @param comId
	 *            团队主键
	 * @param msgId
	 *            信息主键
	 * @param userId
	 *            验证人员主键
	 * @return
	 */
	public boolean authorCheck(Integer comId, Integer msgId, Integer userId) {
		List<MsgShare> listMsgShare = msgShareDao.authorCheck(comId, msgId, userId);
		if (null != listMsgShare && !listMsgShare.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 留言显示更多
	 * @param msgId
	 * @param comId
	 * @param dest
	 * @return
	 */
	public List<MsgShareTalk> nextPageSizeMsgTalks(Integer msgId, Integer comId, Integer pageSize,Integer minId,String dest) {
		// 信息讨论分页
		List<MsgShareTalk> list = msgShareDao.nextPageSizeMsgTalks(msgId, comId,minId, pageSize);
		List<MsgShareTalk> msgShareTalks = new ArrayList<MsgShareTalk>();
		for (MsgShareTalk msgShareTalk : list) {
			if (!dest.equals("app")) {// 手机端不需要字符转换
				// 回复内容转换
				msgShareTalk.setContent(StringUtil.toHtml(msgShareTalk.getContent()));
			}
			// 讨论的附件
			msgShareTalk.setMsgShareTalkUpfiles(msgShareDao.listMsgShareTalkFile(comId, msgId, msgShareTalk.getId()));
			msgShareTalks.add(msgShareTalk);
		}
		return list;
	}

	/**
	 * 分页查询对应的分享信息
	 * @param orgNum 团队号
	 * @return
	 */
	public List<MsgShare> listPagedMsgForDaily(Integer orgNum) {
		return msgShareDao.listPagedMsgForDaily(orgNum);
	}
	
	/**
	 * 删除分享附件
	 * @param msgUpFileId
	 * @param userInfo
	 * @param msgId
	 */
	public void delMsgUpfile(Integer msgUpFileId, UserInfo userInfo, Integer msgId) {
		MsgShareTalkUpfile file = (MsgShareTalkUpfile) msgShareDao.objectQuery(MsgShareTalkUpfile.class, msgUpFileId);
		msgShareDao.delById(MsgShareTalkUpfile.class, msgUpFileId);
		//模块日志添加
		Upfiles upfiles = (Upfiles) msgShareDao.objectQuery(Upfiles.class, file.getUpfileId());
		this.addMagShareRepLog(userInfo.getComId(),msgId,userInfo.getId(),userInfo.getUserName(),"删除了信息附件："+upfiles.getFilename());
	}

	/**
	 * 通过日报添加分享信息
	 * @param listDaily
	 */
	public void addMsgShareByDaily(List<Daily> listDaily) {
		if(!CommonUtil.isNull(listDaily)){
			for (Daily daily : listDaily) {
				MsgShare msgShare = new MsgShare();
				msgShare.setComId(daily.getComId());
				StringBuffer content = new StringBuffer();
				List<DailyQ> qs = dailyService.listDailyQ(daily.getId(),daily.getComId(),daily.getReporterId());
				for(DailyQ dailyQ : qs){
					if(StringUtil.isBlank(dailyQ.getDailyVal())){
						content.append("<br/><b>" + dailyQ.getDailyName() + "：</b><br/><font color=\"red\">未填写</font>");
					}else{
						content.append("<br/><b>" + dailyQ.getDailyName() + "：</b><br/>" + dailyQ.getDailyVal());
					}
				}
				msgShare.setContent(content.toString());
				msgShare.setAction("post");
				msgShare.setType(ConstantInterface.TYPE_DAILY);
				msgShare.setCreator(daily.getReporterId());
				
				Integer scopeType = daily.getScopeType();
				msgShare.setScopeType(scopeType);
				msgShare.setModId(daily.getId());
				msgShare.setTraceType(0);
				if(scopeType==0){
					msgShare.setIsPub(PubStateEnum.YES.getValue());
				}else{
					msgShare.setIsPub(PubStateEnum.NO.getValue());
				}
				Integer msgId = msgShareDao.add(msgShare);
				
				msgShare = new MsgShare();
				msgShare.setId(msgId);
				msgShare.setRecordCreateTime(daily.getDailyDate() + " " + daily.getRecordCreateTime().substring(11,19));
				msgShareDao.update("update msgShare set recordCreateTime=:recordCreateTime where id=:id", msgShare);
				
				if(scopeType == 1){
					List<DailyShareGroup> dailyShareGroups = dailyService.listDailyShareGroup(daily.getId());
					this.addMsgshareGrp(msgId,dailyShareGroups);
				}
			}
		}
		
	}

	/**
	 *添加信息的分享组
	 * @param msgId 信息主键
	 * @param dailyShareGroups 日报分享组
	 */
	private void addMsgshareGrp(Integer msgId,
			List<DailyShareGroup> dailyShareGroups) {
		if(null!=dailyShareGroups && !dailyShareGroups.isEmpty()){
			for (DailyShareGroup dailyShareGroup : dailyShareGroups) {
				ShareGroup shareGroup = new ShareGroup();
				shareGroup.setComId(dailyShareGroup.getComId());
				shareGroup.setMsgId(msgId);
				shareGroup.setGrpId(dailyShareGroup.getGrpId());
				msgShareDao.add(shareGroup);
			}
		}
		
	}
}
