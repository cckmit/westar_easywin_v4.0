package com.westar.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.MsgShare;
import com.westar.base.model.RecycleBin;
import com.westar.base.model.ShareGroup;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.Vote;
import com.westar.base.model.VoteChoose;
import com.westar.base.model.VoteLog;
import com.westar.base.model.VoteScope;
import com.westar.base.model.VoteTalk;
import com.westar.base.model.VoteTalkFile;
import com.westar.base.model.Voter;
import com.westar.base.model.WeekRepTalkFile;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.VoteDao;
import com.westar.core.thread.IndexUpdateThread;

@Service
public class VoteService {

	@Autowired
	VoteDao voteDao;
	
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	IndexService indexService;

	@Autowired
	UploadService uploadService;
	
	@Autowired
	JiFenService jifenService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	AttentionService attentionService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	ChatService chatService;
	/**
	 * 分页查询投票的情况
	 * @param vote
	 * @return
	 */
	public List<Vote> listPagedVote(Vote vote) {
		List<Vote> votes = voteDao.listPagedVote(vote);
		for (Vote voteTemp : votes) {
			//有人投票则选出选项投票最多的
			if(voteTemp.getVoteTotal()>0){
				List<VoteChoose> mostChooses = voteDao.getMostChooses(voteTemp.getId(),voteTemp.getComId());
				voteTemp.setMostChooses(mostChooses);
			}
		}
		return votes;
	}

	/**
	 * 发起投票
	 * @param vote 投票描述
	 * @param msgShare
	 * @param userInfo 操作员
	 * @return
	 * @throws Exception
	 */
	public Integer addVote(Vote vote, MsgShare msgShare,UserInfo userInfo) throws Exception {
		//投票主键
		Integer voteId=voteDao.add(vote);
		//投票选项
		List<VoteChoose> voteChooses = vote.getVoteChooses();
		if(null!=voteChooses && voteChooses.size()>0){
			for (VoteChoose voteChoose : voteChooses) {
				if(null==voteChoose.getContent() || "".equals(voteChoose.getContent())){
					continue;
				}
				voteChoose.setComId(vote.getComId());
				voteChoose.setVoteId(voteId);
				
				voteDao.add(voteChoose);
			}
		}
		//推送信息
		if(null!=vote.getShareMsg() && "yes".equals(vote.getShareMsg()) && null!=msgShare){//在删除投票的时候，将分享信息设置成工作轨迹
			//模块主键
			msgShare.setModId(voteId);
			//是分享
			msgShare.setTraceType(0);
			//公开
			msgShare.setIsPub(1);
			msgShareService.addMsgShare(msgShare,userInfo);
		}
		//添加工作轨迹
		systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_VOTE, voteId,
				"发起投票:"+StringUtil.cutString(vote.getVoteContent(), 26,null), "发起投票:"+StringUtil.cutString(vote.getVoteContent(), 26,null));
		if(null!=msgShare){//投票范围
			List<ShareGroup> shareGroups = msgShare.getShareGroup();
			if(null!=shareGroups && shareGroups.size()>0){
				for (ShareGroup shareGroup : shareGroups) {
					VoteScope voteScope = new VoteScope();
					//投票范围
					voteScope.setComId(vote.getComId());
					//投票主键
					voteScope.setVoteId(voteId);
					//分组主键
					voteScope.setGrpId(shareGroup.getGrpId());
					voteDao.add(voteScope);
					
				}
			}
		}
		//投票的参与人
		List<UserInfo> shares = this.listVoteViews(userInfo.getComId(), userInfo.getId(), voteId);
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, voteId, "发起投票", ConstantInterface.TYPE_VOTE, shares,null);
		
		//添加积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTE,
				"发起投票:"+StringUtil.cutString(vote.getVoteContent(), 26,null),voteId);
		
		//添加关注
		if(null!=vote.getAttentionState() && vote.getAttentionState().equals("1")){
			attentionService.addAtten(ConstantInterface.TYPE_VOTE, voteId, userInfo);
		}
		//模块日志添加
		this.addVoteLog(userInfo.getComId(), voteId, userInfo.getId(), userInfo.getUserName(), "发起投票");
		//添加投票索引
		this.updateVoteIndex(voteId,userInfo,"add");
		return voteId;
	}

	/**
	 * 当前操作员的投票状态
	 * @param id 投票主键
	 * @param comId 企业号
	 * @param userId 当前操作人员
	 * @return
	 */
	public Vote getVoteInfo(Integer id, Integer comId,Integer userId) {
		Vote vote  = voteDao.getVoteInfo(id,comId,userId);
		if(null!=vote){
			//设置显示数量
			Integer fileNum = voteDao.countFile(comId, id);
			vote.setFileNum(fileNum);
			List<VoteChoose> voteChooses = voteDao.getVoteChoose(id,comId,userId);
			if(!"1".equals(vote.getVoteType())){//非匿名则选出投票人
				for (VoteChoose voteChoose : voteChooses) {
					List<Voter> voters = voteDao.getVoters(id,comId,voteChoose.getId());
					voteChoose.setVoters(voters);
				}
			}
			vote.setVoteChooses(voteChooses);
		}
		return vote;
	}

	/**
	 * 投票查看权限验证
	 * @param comId
	 * @param voteId
	 * @param userId
	 * @return
	 */
	public boolean authorCheck(Integer comId,Integer voteId,Integer userId){
		List<Vote> listVote = voteDao.authorCheck(comId,voteId,userId);
		if(null!=listVote && !listVote.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 修改投票截止时间
	 * @param vote
	 * @throws Exception 
	 */
	public void updateVote(Vote vote,UserInfo userInfo) throws Exception {
		voteDao.update(vote);
		//投票的参与人
		List<UserInfo> shares = this.listVoteViews(userInfo.getComId(), userInfo.getId(), vote.getId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, vote.getId(),"修改投票截止时间为:"+vote.getFinishTime(), ConstantInterface.TYPE_VOTE, shares,null);
		//更新投票索引
//		this.updateVoteIndex(vote.getId(),userInfo,"update");
	}

	/**
	 * 公司总的投票数
	 * @param comId
	 * @return
	 */
	public Integer getVoteTotals(Integer comId) {
		return voteDao.getVoteTotals(comId);
	}

	/**
	 * 投票讨论回复
	 * @param voteTalk
	 * @throws Exception 
	 */
	public Integer addVoteTalk(VoteTalk voteTalk,UserInfo userInfo) throws Exception {
		Integer id = voteDao.add(voteTalk);
		Integer[] upfilesId = voteTalk.getUpfilesId();
		Vote vote = (Vote) voteDao.objectQuery(Vote.class, voteTalk.getVoteId());
		if(null!=upfilesId && upfilesId.length>0){
			
			for (Integer upfileId : upfilesId) {
				VoteTalkFile voteTalkFile = new VoteTalkFile();
				//企业编号
				voteTalkFile.setComId(voteTalk.getComId());
				//投票主键
				voteTalkFile.setVoteId(voteTalk.getVoteId());
				//讨论主键
				voteTalkFile.setTalkId(id);
				//附件主键
				voteTalkFile.setUpfileId(upfileId);
				//上传人
				voteTalkFile.setUserId(voteTalk.getTalker());
				
				voteDao.add(voteTalkFile);
				//为投票讨论附件创建索引
				uploadService.updateUpfileIndex(upfileId, userInfo, "add",voteTalk.getVoteId(),ConstantInterface.TYPE_VOTE);
			}
			
			//添加到文档中心
			fileCenterService.addModFile(userInfo, Arrays.asList(upfilesId), vote.getVoteContent());
		}
		//投票的参与人
		List<UserInfo> shares =  this.listVoteViews(userInfo.getComId(), userInfo.getId(), voteTalk.getVoteId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, voteTalk.getVoteId(),"参与投票讨论:"+voteTalk.getContent(), ConstantInterface.TYPE_VOTE, shares,null);
		
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTETALK,
				"参与投票讨论",voteTalk.getVoteId());
		//更新投票索引
//		this.updateVoteIndex(voteTalk.getVoteId(),userInfo,"update");
		return id;
		
	}

	/**
	 *  删除投票讨论回复
	 * @param voteTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception 
	 */
	public List<Integer> delVoteTalk(VoteTalk voteTalk,String delChildNode,UserInfo userInfo) throws Exception {
		List<Integer> childIds = new ArrayList<Integer>();
		//删除自己
		if(null==delChildNode){
			childIds.add(voteTalk.getId());
			//删除附件
			voteDao.delByField("voteTalkFile", new String[]{"comId","talkId"}, 
					new Object[]{voteTalk.getComId(),voteTalk.getId()});
			voteDao.delById(VoteTalk.class, voteTalk.getId());
			
		}else if("yes".equals(delChildNode)){//删除自己和所有的子节点
			//待删除的讨论
			List<VoteTalk> listVoteTalk = voteDao.listVoteTalkForDel(voteTalk.getComId(), voteTalk.getId());
			for (VoteTalk talk: listVoteTalk) {
				childIds.add(talk.getId());
				//删除讨论的附件
				voteDao.delByField("voteTalkFile", new String[]{"comId","talkId"}, 
						new Object[]{talk.getComId(),talk.getId()});
			}
			//删除当前节点及其子节点回复
			voteDao.delVoteTalk(voteTalk.getId(),voteTalk.getComId());
		}else if("no".equals(delChildNode)){//删除自己,将子节点提高一级
			childIds.add(voteTalk.getId());
			
			//删除附件
			voteDao.delByField("voteTalkFile", new String[]{"comId","talkId"}, 
					new Object[]{voteTalk.getComId(),voteTalk.getId()});
			
			voteDao.updateVoteTalkParentId(voteTalk.getId(),voteTalk.getComId());
			voteDao.delById(VoteTalk.class, voteTalk.getId());
		}
		
		//投票的参与人
		List<UserInfo> shares = this.listVoteViews(userInfo.getComId(), userInfo.getId(), voteTalk.getVoteId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, voteTalk.getVoteId(),"删除投票讨论", ConstantInterface.TYPE_VOTE, shares,null);
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TALKDEL,
				"删除投票讨论",voteTalk.getVoteId());

		//更新投票索引
//		this.updateVoteIndex(voteTalk.getVoteId(),userInfo,"update");
		return childIds;
	}

	/**
	 * 投票
	 * @param voteChoose
	 * @param voteId
	 * @param userInfo
	 * @param isVote 
	 * @return
	 */
	public String addVoter(String[] voteChoose, Integer voteId,
			UserInfo userInfo, Integer isVote) {
		String choosesStr = "";
		//参与投票前把原来投过的票删除
		voteDao.delByField("voter", new String[]{"comId","voteId","voter"}, new Object[]{userInfo.getComId(),voteId,userInfo.getId()});
		for (String chooseId : voteChoose) {
			Voter voter = new Voter();
			//公司主键
			voter.setComId(userInfo.getComId());
			//投票人
			voter.setVoter(userInfo.getId());
			//所属投票
			voter.setVoteId(voteId);
			//所投票
			voter.setChooseId(Integer.parseInt(chooseId));
			voteDao.add(voter);
			
			//具体投票內容
			VoteChoose voteChooseVo = voteDao.getVoteChoose(voteId, userInfo.getComId(),chooseId);
			if(null!=voteChooseVo){
				choosesStr = choosesStr+","+voteChooseVo.getContent();
			}
		}
		if(isVote==0){//参与投票
			if(choosesStr.length()>0){//实名
				choosesStr = choosesStr.substring(1,choosesStr.length());
				choosesStr = "参与投票,投票为("+choosesStr+")";
				
			}else{//匿名
				choosesStr = "参与投票";
			}
			//添加积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_VOTECHOOSE,
					"参与投票",voteId);
		}else{//修改投票
			if(choosesStr.length()>0){//实名
				choosesStr = choosesStr.substring(1,choosesStr.length());
				choosesStr ="修改投票,投票为("+choosesStr+")";
			}else{//匿名
				choosesStr ="修改投票";
			}
		}
		
		//投票的参与人
		List<UserInfo> shares = this.listVoteViews(userInfo.getComId(), userInfo.getId(), voteId);
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, voteId,choosesStr, ConstantInterface.TYPE_VOTE, shares,null);
		
		return choosesStr;
	}
	/**
	 * 预删除投票
	 * @param id
	 * @param userInfo
	 */
	public Boolean delPreVote(Integer[] ids, UserInfo sessionUser)  {
		Boolean flag = true;
		if(null!=ids && ids.length>0){
			for (Integer id : ids) {
				
				Vote voteTemp = (Vote) voteDao.objectQuery(Vote.class, id);
				if(null==voteTemp || voteTemp.getDelState()==1){//投票不存在或是已经预删除了
					return false;
				}
				
				Vote vote =new Vote();
				//投票主键
				vote.setId(id);
				//预删除标识
				vote.setDelState(1);
				//修改投票信息
				voteDao.update(vote);
				
				//删除数据更新记录
				voteDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, new Object[]{sessionUser.getComId(),ConstantInterface.TYPE_VOTE,id});
				//删除回收箱数据
				voteDao.delByField("recycleBin", new String[]{"comId","busType","busId","userId"}, new Object[]{sessionUser.getComId(),ConstantInterface.TYPE_VOTE,id,sessionUser.getId()});
				
				//回收箱
				RecycleBin recyleBin =  new RecycleBin();
				//业务主键
				recyleBin.setBusId(id);
				//业务类型
				recyleBin.setBusType(ConstantInterface.TYPE_VOTE);
				//企业号
				recyleBin.setComId(sessionUser.getComId());
				//创建人
				recyleBin.setUserId(sessionUser.getId());
				voteDao.add(recyleBin);
			}
		}
		
		//系统日志
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "删除投票",
				ConstantInterface.TYPE_VOTE, sessionUser.getComId(),sessionUser.getOptIP());
		
		
		return flag;
		
	}
	/**
	 * 删除投票
	 * @param id
	 * @param sessionUser
	 * @throws Exception 
	 */
	public void delVote(Integer id, UserInfo sessionUser) throws Exception {
		//更新投票索引
		this.updateVoteIndex(id,sessionUser,"del");
		//投票说明
		Integer comId = sessionUser.getComId();
		
		//删除日志
		voteDao.delByField("voteLog", new String[]{"comId","voteId"}, new Object[]{comId,id});
		//删除讨论
		voteDao.delByField("voteTalkFile", new String[]{"comId","voteId"}, new Object[]{comId,id});
		//删除讨论
		voteDao.delByField("voteTalk", new String[]{"comId","voteId"}, new Object[]{comId,id});
		//删除人员投票信息
		voteDao.delByField("voter", new String[]{"comId","voteId"}, new Object[]{comId,id});
		//删除投票范围
		voteDao.delByField("voteScope", new String[]{"comId","voteId"}, new Object[]{comId,id});
		//删除投票选项
		voteDao.delByField("voteChoose", new String[]{"comId","voteId"}, new Object[]{comId,id});

		//删除浏览记录
		voteDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, new Object[]{comId,id,ConstantInterface.TYPE_VOTE});
		//关注信息
		voteDao.delByField("attention", new String[]{"comId","busId","busType"}, new Object[]{comId,id,ConstantInterface.TYPE_VOTE});
		//最新动态
		voteDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, new Object[]{comId,id,ConstantInterface.TYPE_VOTE});
		
		//删除聊天记录
		chatService.delBusChat(comId,id,ConstantInterface.TYPE_VOTE);
		
		Vote vote = voteDao.getVoteObj(id,comId);
		if(null!=vote){
			
			//修改积分
			jifenService.addJifen(sessionUser.getComId(), sessionUser.getId(), ConstantInterface.TYPE_VOTEDEL,
					"删除投票:"+vote.getVoteContent(),id);
			//添加工作轨迹
			systemLogService.addSystemLogWithTrace(sessionUser, sessionUser.getId(), ConstantInterface.TYPE_VOTE, 
					id, StringUtil.cutStrFace(vote.getVoteContent(), 26), StringUtil.cutStrFace(vote.getVoteContent(), 26));
			
		}
		
		//将发起投票的分享消息修改为工作轨迹
		msgShareService.delShareMsg(ConstantInterface.TYPE_VOTE,id, sessionUser,0);
		
		
		//删除投票
		voteDao.delById(Vote.class, id);
	}
	/**
	 * 只取出投票截止时间
	 * @param id
	 * @param comId 
	 * @return
	 */
	public Vote getVoteObj(Integer id, Integer comId) {
		Vote vote = voteDao.getVoteObj(id,comId);
		return vote;
	}

	/**
	 * 投票讨论
	 * @param comId 公司编号
	 * @param voteId 投票主键
	 * @return
	 */
	public List<VoteTalk> listPagedVoteTalk( Integer voteId,Integer comId) {
		//投票的讨论
		List<VoteTalk> list = voteDao.listPagedVoteTalk(voteId,comId);
		List<VoteTalk> voteTalks = new ArrayList<VoteTalk>();
		for (VoteTalk voteTalk : list) {
			//处理回复的内容
			voteTalk.setContent(StringUtil.toHtml(voteTalk.getContent()));
			//讨论的附件
			voteTalk.setListVoteTalkFile(voteDao.listVoteTalkFile(comId,voteId,voteTalk.getId()));
			voteTalks.add(voteTalk);
		}
		return voteTalks;
	}

	/**
	 * 模块日志添加
	 * @param comId
	 * @param voteId
	 * @param userId
	 * @param userName
	 * @param string
	 */
	public void addVoteLog(Integer comId, Integer voteId, Integer userId,
			String userName, String content) {
		VoteLog voteLog = new VoteLog(comId,voteId,userId,content,userName);
		voteDao.add(voteLog);
		
	}

	/**
	 * 投票日志
	 * @param comId
	 * @param voteId
	 * @return
	 */
	public List<VoteLog> listPagedVoteLog(Integer comId, Integer voteId) {
		//投票日志
		List<VoteLog> voteLogs = voteDao.listPagedVoteLog(voteId,comId);
		return voteLogs;
	}
	/**
	 * 投票讨论
	 * @param id
	 * @param comId 
	 * @return
	 */
	public VoteTalk getVoteTalk(Integer id, Integer comId) {
		VoteTalk voteTalk = voteDao.getVoteTalk(id,comId);
		//转换
		if(null!=voteTalk){
			String content = StringUtil.toHtml(voteTalk.getContent());
			//讨论的附件
			voteTalk.setListVoteTalkFile(voteDao.listVoteTalkFile(comId,voteTalk.getVoteId(),voteTalk.getId()));
			voteTalk.setContent(content);
		}
		return voteTalk;
	}

	/**
	 * 投票讨论的附件
	 * @param comId
	 * @param voteId
	 * @return
	 */
	public List<VoteTalkFile> listPagedVoteFiles(Integer comId, Integer voteId) {
		// 投票讨论附件
		List<VoteTalkFile> list = voteDao.listPagedVoteTalkFiles(comId,voteId);
		return list;
	}
	/**
	 * 更新投票索引
	 * @param voteId
	 * @param userInfo
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception 
	 */
	public void updateVoteIndex(Integer voteId,UserInfo userInfo,String opType) throws Exception{
		//更新投票索引
		Vote vote  = voteDao.getVoteInfo(voteId,userInfo.getComId(),userInfo.getId());
		if(null==vote){return;}
		//连接索引字符串
//		StringBuffer attStr = new StringBuffer(vote.getVoteContent()+","+vote.getOwnerName()+",");
		StringBuffer attStr = new StringBuffer(vote.getVoteContent());
		//获取投票选项描述创建索引
//		List<VoteChoose> listVoteChoose = voteDao.listVoteChoose4Index(userInfo.getComId(),voteId);
//		for(VoteChoose vo : listVoteChoose){
//			attStr.append(vo.getContent()+",");
//		}
		//获取投票讨论为其创建索引
//		List<VoteTalk> listVoteTalk = voteDao.listVoteTalk4Index(userInfo.getComId(),voteId);
//		for(VoteTalk vo : listVoteTalk){
//			attStr.append(vo.getContent()+","+vo.getTalkerName()+",");
//		}
//		////获取投票所有参与人
//		List<UserInfo> listVoteViewers = voteDao.listVoteViewers(userInfo.getComId(),voteId);
//		//参与人名称连接成字符串创建索引
//		for(UserInfo vo : listVoteViewers){
//			attStr.append(vo.getUserName()+",");
//		}
		//获取投票讨论附件集合
//		List<VoteTalkFile> listUpfile = voteDao.listVoteTalkFiles(userInfo.getComId(),voteId);
//		if(null!=listUpfile){
//			Upfiles upfile = null;
//			for(VoteTalkFile vo:listUpfile){
//				//附件内容添加
//				upfile = uploadService.queryUpfile4Index(vo.getUpfileId(),userInfo.getComId());
//				//附件名称
//				attStr.append(upfile.getFilename()+",");
//				//附件内容
//				attStr.append(upfile.getFileContent()+",");
//			}
//		}
		//单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String index_key = userInfo.getComId()+"_"+ConstantInterface.TYPE_VOTE+"_"+voteId;
		//为投票创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				index_key,userInfo.getComId(),voteId,ConstantInterface.TYPE_VOTE,
				vote.getVoteContent(),attStr.toString(),DateTimeUtil.parseDate(vote.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,index_key));
		}
		
	}
	
	/**
	 * 获取自己权限下排列前N的投票数据集合
	 * @param userInfo
	 * @param rowNum
	 * @return
	 */
	public List<Vote> firstNVoteList(UserInfo userInfo,Integer rowNum) {
		List<Vote> votes = voteDao.firstNVoteList(userInfo,rowNum);
		return votes;
	}
	/**
	 * 投票的查看人员
	 * @param comId 企业号
	 * @param userId 操作员主键
	 * @param busId 业务主键
	 * @return
	 */
	public List<UserInfo> listVoteViews(Integer comId, Integer userId, Integer busId) {
		//投票的参与人
			List<UserInfo> shares = new ArrayList<UserInfo>();
			Vote voteT = (Vote) voteDao.objectQuery(Vote.class, busId);
			if(voteT.getScopeType()==0){//所有人
				shares = userInfoService.listUser(comId);
			}else if(voteT.getScopeType()==1){//自定义
				//投票的参与人
				shares = voteDao.listVoteUser(busId,comId); 
			}
		return shares;
	}
	/**
	 * 获取团队所有投票
	 * @param userInfo
	 * @return
	 */
	public List<Vote> listVoteOfAll(UserInfo userInfo){
		return voteDao.listVoteOfAll(userInfo);
	}
	
	/**
	 * 删除投票附件
	 * @param voteUpFileId
	 * @param userInfo
	 * @param voteId
	 */
	public void delVoteUpfile(Integer voteUpFileId, UserInfo userInfo, Integer voteId) {
		VoteTalkFile file = (VoteTalkFile) voteDao.objectQuery(VoteTalkFile.class, voteUpFileId);
		voteDao.delById(VoteTalkFile.class, voteUpFileId);
		//模块日志添加
		Upfiles upfiles = (Upfiles) voteDao.objectQuery(Upfiles.class, file.getUpfileId());
		this.addVoteLog(userInfo.getComId(),voteId,userInfo.getId(),userInfo.getUserName(),"删除了投票留言附件："+upfiles.getFilename());
	}
}
