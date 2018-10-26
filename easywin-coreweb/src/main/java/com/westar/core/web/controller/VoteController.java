package com.westar.core.web.controller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.MsgShare;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.model.Vote;
import com.westar.base.model.VoteChoose;
import com.westar.base.model.VoteLog;
import com.westar.base.model.VoteTalk;
import com.westar.base.model.VoteTalkFile;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.FileUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UploadService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.service.VoteService;
import com.westar.core.web.FreshManager;
/**
 * 投票中心
 * @author zzq
 *
 */
@Controller
@RequestMapping("/vote")
public class VoteController extends BaseController{

	
	@Autowired
	private VoteService voteService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private SystemLogService  systemLogService;
	
	@Autowired
	private UploadService  uploadService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	/**
	 * 分页查询投票的情况
	 * @param vote
	 * @return
	 */
	@RequestMapping("/listPagedVote")
	public ModelAndView listPagedVote(HttpServletRequest request,Vote vote) {
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		UserInfo userInfo = this.getSessionUser();
		vote.setComId(userInfo.getComId());
		vote.setSessionUser(userInfo.getId());
		vote.setOwner(userInfo.getId());
		List<Vote> list = voteService.listPagedVote(vote);
		ModelAndView mav = new ModelAndView("/vote/listPagedVote", "list", list);
		//公司投票总数
		Integer totals =  voteService.getVoteTotals(userInfo.getComId());
		mav.addObject("totals", totals);
		mav.addObject("userInfo", userInfo);
		
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_VOTE);
		return mav;
	}
	/**
	 * 新增投票界面
	 * @param vote
	 * @return
	 */
	@RequestMapping("/addVotePage")
	public ModelAndView addVotePage(Vote vote) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/vote/addVote");
		
		//上次使用的分组
		List<UsedGroup> usedGroups = userInfoService.listUsedGroup(userInfo.getComId(),userInfo.getId());
		//个人组群集合
		List<SelfGroup> listSelfGroup = userInfoService.listSelfGroup(userInfo.getComId(),userInfo.getId());
		//最近一次使用的分组的类型，分组名称以及自定义所有的分组
		Map<String, String> grpMap = CommonUtil.usedGrpJson(usedGroups,listSelfGroup);
		//最近一次使用的分组的类型
		mav.addObject("idType", grpMap.get("idType"));
		//分组名称
		mav.addObject("scopeTypeSel", grpMap.get("scopeTypeSel"));
		//自定义所有的分组
		mav.addObject("selfGroupStr", grpMap.get("selfGroupStr"));
		
		
		
		mav.addObject("sessionUser",this.getSessionUser());
		return mav;
	}
	
	/**
	 * 新增投票
	 * @param vote 投票信息
	 * @param idType 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/addVote")
	public Map<String, Object> addVote(Vote vote,String idType) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		//所在企业
		vote.setComId(userInfo.getComId());
		//创建人
		vote.setOwner(userInfo.getId());
		
		//删除标识(正常)
		vote.setDelState(ConstantInterface.MOD_PREDEL_SATATE_NO);
		
		String voteType  = ConstantInterface.VOTE_TYPE_REALNAME.equals(vote.getVoteType())? 
				ConstantInterface.VOTE_TYPE_REALNAME : ConstantInterface.VOTE_TYPE_NONAME;
		//设置是否匿名
		vote.setVoteType(voteType);
		
		//获取信息分享以及范围
		MsgShare msgShare = CommonUtil.getMsgShare(idType,ConstantInterface.TYPE_VOTE,vote.getVoteContent(),userInfo);
		if(null!=msgShare){
			vote.setScopeType(msgShare.getScopeType());
		}else{//默认是所有人
			vote.setScopeType(ALL_COLLEAGUE);
			
		}
		//投票选项
		List<VoteChoose> voteChooses = vote.getVoteChooses();
		List<VoteChoose> voteChooselist =  this.getvoteChooselist(voteChooses,userInfo);
		
		vote.setVoteChooses(voteChooselist);
		Integer voteId = voteService.addVote(vote,msgShare,userInfo);
		
		//设置投票主键
		vote.setId(voteId);
		//设置创建时间
		vote.setRecordCreateTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd));
		//设置投票内容
		vote.setVoteContent(StringUtil.cutString(StringUtil.toHtml(vote.getVoteContent()),120,null));
		
		this.setNotification(Notification.SUCCESS, "添加成功！");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "y");
		map.put("voteId", voteId);
		map.put("vote", vote);
		map.put("userInfo", userInfo);
		return map;
	}
	/**
	 * 取得选项信息
	 * @param voteChooses
	 * @param userInfo 
	 * @return
	 */
	private List<VoteChoose> getvoteChooselist(List<VoteChoose> voteChooses, UserInfo userInfo) {
		List<VoteChoose> voteChooselist = new ArrayList<VoteChoose>();
		if(null!=voteChooses && voteChooses.size()>0){
			//文件路径
			String basepath = FileUtil.getRootPath();
			String parentPath = basepath+ File.separator+"static"+ File.separator + "temp"+
					 File.separator +"voteImg"+File.separator+userInfo.getComId()+File.separator +userInfo.getId();;
			for (VoteChoose voteChoose : voteChooses) {
				//原图
				File orgFile = new File(basepath+File.separator+voteChoose.getOrgImgPath());
				if(orgFile.isFile()){
					//从stataic的文件夹复制文件
					String path = File.separator + "uploads"+File.separator+userInfo.getComId();
					Upfiles orgUpfile = CommonUtil.copyFile(orgFile,uploadService,userInfo,path);
					if(null!=orgUpfile){
						voteChoose.setOriginal(orgUpfile.getId());
					}
				}
				//大图
				File largeFile = new File(basepath+File.separator+voteChoose.getLargeImgPath());
				if(largeFile.isFile()){
					//从stataic的文件夹复制文件
					String path = File.separator + "uploads"+File.separator+userInfo.getComId();
					Upfiles largeUpfile = CommonUtil.copyFile(largeFile,uploadService,userInfo,path);
					if(null!=largeUpfile){
						voteChoose.setLarge(largeUpfile.getId());
					}
				}
				//中图
				File midFile = new File(basepath+File.separator+voteChoose.getMidImgPath());
				if(midFile.isFile()){
					//从stataic的文件夹复制文件
					String path = File.separator + "uploads"+File.separator+userInfo.getComId();
					Upfiles midUpfile = CommonUtil.copyFile(midFile,uploadService,userInfo,path);
					if(null!=midUpfile){
						voteChoose.setMiddle(midUpfile.getId());
					}
				}
				
				voteChooselist.add(voteChoose);
			}
			//删除临时文件
			if(!"".equals(parentPath) && new File(parentPath).isDirectory()){
				try {
					FileUtils.deleteDirectory(new File(parentPath));
				} catch (IOException e) {
				}
			}
		}
		return voteChooselist;
	}
	/**
	 * 投票查看权限验证
	 * @param voteId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/authorCheck")
	public Vote authorCheck(Integer voteId){
		UserInfo userInfo = this.getSessionUser();
		Vote vote = new Vote();
		if(null==userInfo){
			vote.setSucc(false);
			vote.setPromptMsg("服务已关闭，请稍后重新操作!");
			return vote;
		}
		if(voteService.authorCheck(userInfo.getComId(),voteId,userInfo.getId())){
			vote.setSucc(true);
		}else{
			//查看验证，删除消息提醒
			todayWorksService.updateTodoWorkRead(voteId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTE,0);
			vote.setSucc(false);
			vote.setPromptMsg("抱歉，你没有投票权限");
		}
		return vote;
	}
	
	/**
	 * 当前操作员投票的状态
	 * @param id
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/voteDetail")
	public ModelAndView voteDetail(HttpServletRequest request,Integer id,String redirectPage){
		ModelAndView mav = new ModelAndView("/vote/voteDetail");
		UserInfo userInfo = this.getSessionUser();
		//投票查看权限验证
		if(!voteService.authorCheck(userInfo.getComId(),id,userInfo.getId())){
			mav = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有投票权限");
		}else{
			Vote vote = voteService.getVoteInfo(id,userInfo.getComId(),userInfo.getId());
			
			mav.addObject("vote", vote);
			mav.addObject("userInfo", userInfo);

			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTE, id);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				viewRecordService.addViewRecord(userInfo,viewRecord);
			}
			//浏览的人员
			List<ViewRecord> listViewRecord = viewRecordService.listViewRecord(userInfo,ConstantInterface.TYPE_VOTE,id);
			mav.addObject("listViewRecord", listViewRecord);
		}
		//查看投票详情，删除消息提醒
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTE,0);
		return mav;
	}
	
	
	/**
	 * 当前操作员投票的状态
	 * @param vote
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/updateVoteTime")
	public Map<String, String> updateVoteTime(Vote vote) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		Map<String,String> map = new HashMap<String, String>();
		voteService.updateVote(vote,userInfo);
		//模块日志添加
		voteService.addVoteLog(userInfo.getComId(), vote.getId(), userInfo.getId(), userInfo.getUserName(), "修改投票截止时间为("+vote.getFinishTime()+")");
		
		map.put("status", "y");
		return map;
	}
	/**
	 * 投票讨论回复
	 * @param voteTalk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/addVoteTalk")
	public Map<String, Object> addVoteTalk(VoteTalk voteTalk) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		voteTalk.setComId(sessionUser.getComId());
		voteTalk.setTalker(sessionUser.getId());
		Integer id = voteService.addVoteTalk(voteTalk,sessionUser);
		//模块日志添加
		if(-1==voteTalk.getParentId()){
			voteService.addVoteLog(sessionUser.getComId(), voteTalk.getVoteId(), sessionUser.getId(), sessionUser.getUserName(), "参与投票讨论");
		}else{
			voteService.addVoteLog(sessionUser.getComId(), voteTalk.getVoteId(), sessionUser.getId(), sessionUser.getUserName(), "回复投票讨论");
		}
		map.put("status", "y");
		map.put("id", id);
		//用于返回页面拼接代码
		VoteTalk voteTalk4Page = voteService.getVoteTalk(id, sessionUser.getComId());
		map.put("voteTalk", voteTalk4Page);
		map.put("sessionUser", this.getSessionUser());
		return map;
	}
	/**
	 * 删除投票讨论回复
	 * @param voteTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/delVoteTalk")
	public Map<String, Object> delVoteTalk(VoteTalk voteTalk,String delChildNode) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		voteTalk.setComId(sessionUser.getComId());
		//要删除的回复所有子节点和自己
		List<Integer> childIds = voteService.delVoteTalk(voteTalk,delChildNode,sessionUser);
		map.put("status", "y");
		map.put("childIds", childIds);
		//模块日志添加
		voteService.addVoteLog(sessionUser.getComId(), voteTalk.getVoteId(), sessionUser.getId(), sessionUser.getUserName(), "删除投票讨论");
		return map;
	}
	/**
	 * 投票
	 * @param voteChoose 所投票主键
	 * @param voteId 投票主键
	 * @param isVote 是否已投票
	 * @param backObj 是否需要向页面返回对象
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/voteChoose")
	public Map<String, Object> voteChoose(String[] voteChoose,Integer voteId,Integer isVote,String backObj){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		String choosesStr  = voteService.addVoter(voteChoose,voteId,sessionUser,isVote);
		//模块日志添加
		voteService.addVoteLog(sessionUser.getComId(), voteId, sessionUser.getId(), sessionUser.getUserName(),choosesStr);
		//是否需要返回对象
		if("yes".equalsIgnoreCase(backObj)){
			Vote vote = voteService.getVoteInfo(voteId, sessionUser.getComId(), sessionUser.getId());
			map.put("vote",vote);
		}
		return map;
	}
	/**
	 * 删除投票
	 * @param id 投票主键
	 * @return
	 * @throws IOException 
	 * @throws CorruptIndexException 
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping("/delVote")
	public Map<String, Object> delVote(Integer id) throws CorruptIndexException, IOException, ParseException{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Boolean flag =  voteService.delPreVote(new Integer[]{id},sessionUser);
		if(!flag){
			this.setNotification(Notification.ERROR, "投票已被删除！");
		}else{
			this.setNotification(Notification.SUCCESS, "删除成功！");
		}
		map.put("status", "y");
		return map;
	}
	/**
	 * 批量删除投票
	 * @param ids
	 * @param redirectPage
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("/delBatchVote")
	public ModelAndView delBatchVote(Integer[] ids,String redirectPage) throws CorruptIndexException, IOException, ParseException{
		UserInfo sessionUser = this.getSessionUser();
		Boolean falg = voteService.delPreVote(ids,sessionUser);
		if(!falg){
			this.setNotification(Notification.ERROR, "投票已被删除！");
		}else{
			//系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量删除投票",
					ConstantInterface.TYPE_VOTE, sessionUser.getComId(),sessionUser.getOptIP());
			this.setNotification(Notification.SUCCESS, "删除成功！");
		}
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		return mav;
	}
	
	/**
	 *局部刷新
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getVoteInfo")
	public Map<String, Object> getVoteInfo(Integer id){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		Vote vote = voteService.getVoteInfo(id,userInfo.getComId(),userInfo.getId());
		map.put("vote", vote);
		//取得投票信息，删除消息提醒
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTE,0);
		return map;
	}
	/**
	 * 投票时间验证
	 * @param id 投票主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkVoteTime")
	public Map<String, Object> checkVoteTime(Integer  id){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo user = this.getSessionUser();
		if(null==user){
			map.put("state", "n");
			map.put("info", "连接已断开，请重新登录");
		}
		Vote vote = voteService.getVoteObj(id,user.getComId());
		long finishTime = DateTimeUtil.parseDate(vote.getFinishTime()+":00:00",DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
		long nowTine = (new Date()).getTime();
		if(nowTine>finishTime){//过期了
			map.put("state", "n");
			map.put("info", "投票已截止");
		}else{
			map.put("state", "y");
		}
		return map;
	}
	
	/**
	 * 投票讨论
	 * @param voteId 投票主键
	 * @param enabled 投票是否过期
	 * @return
	 */
	@RequestMapping("/voteTalkPage")
	public ModelAndView voteTalkPage(Integer voteId,String enabled) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/vote/voteTalk");
		//查看投票讨论，删除消息提醒
		todayWorksService.updateTodoWorkRead(voteId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTE,0);
		//投票讨论
		List<VoteTalk> voteTalks = voteService.listPagedVoteTalk(voteId,userInfo.getComId());
		mav.addObject("voteTalks",voteTalks);
		mav.addObject("voteId",voteId);
		mav.addObject("enabled",enabled);
		mav.addObject("sessionUser",userInfo);
		return mav;
	}
	/**
	 * 投票日志
	 * @param voteId 投票主键
	 * @return
	 */
	@RequestMapping("/voteLogPage")
	public ModelAndView voteLogPage(Integer voteId) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/vote/voteLog");
		//查看投票日志，删除消息提醒
		todayWorksService.updateTodoWorkRead(voteId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTE,0);
		//投票日志
		List<VoteLog> voteLogs = voteService.listPagedVoteLog(userInfo.getComId(),voteId);
		mav.addObject("voteLogs",voteLogs);
		mav.addObject("sessionUser",userInfo);
		return mav;
	}
	
	/**
	 * 投票附件
	 * @param voteId 投票主键
	 * @return
	 */
	@RequestMapping("/voteFilePage")
	public ModelAndView voteFilePage(Integer voteId) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/vote/voteFiles");
		//查看投票附件，删除消息提醒
		todayWorksService.updateTodoWorkRead(voteId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_VOTE,0);
		//投票讨论的附件
		List<VoteTalkFile> voteTalkFiles = voteService.listPagedVoteFiles(userInfo.getComId(),voteId);
		mav.addObject("voteTalkFiles",voteTalkFiles);
		mav.addObject("userInfo",userInfo);
		mav.addObject("voteId",voteId);
		return mav;
	}
	/**
	 * 获取自己权限下排列前N的投票数据集合
	 * @param vote
	 * @return
	 */
	@RequestMapping("/firstNVoteList")
	public ModelAndView firstNVoteList(Vote vote) {
		UserInfo userInfo = this.getSessionUser();
		List<Vote> voteList = voteService.firstNVoteList(userInfo,7);
		ModelAndView mav = new ModelAndView("/vote/firstNVoteList", "voteList", voteList);
		return mav;
	}
	
	/**
	 * 删除投票附件
	 * @param voteId
	 * @param voteUpFileId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delVoteUpfile")
	public Map<String, Object> delVoteUpfile(Integer voteId,Integer voteUpFileId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		voteService.delVoteUpfile(voteUpFileId,userInfo,voteId);
		map.put("status", "y");
		return map;
	}
	
}