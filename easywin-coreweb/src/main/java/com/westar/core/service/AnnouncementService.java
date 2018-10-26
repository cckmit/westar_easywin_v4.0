package com.westar.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.AnnounScopeByDep;
import com.westar.base.model.AnnounScopeByUser;
import com.westar.base.model.AnnounUpfile;
import com.westar.base.model.Announcement;
import com.westar.base.model.Department;
import com.westar.base.model.MsgShare;
import com.westar.base.model.RecycleBin;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.AnnouncementDao;
import com.westar.core.thread.IndexUpdateThread;
import com.westar.core.thread.SendMsgThread;
import com.westar.core.thread.UpdateTodoWorksThread;

@Service
public class AnnouncementService {


	@Autowired
	AnnouncementDao announcementDao;

	@Autowired
	AttentionService attentionService;
	
	@Autowired
	IndexService indexService;
	
	@Autowired
	UploadService uploadService;

	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	LogsService logsService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	TalkService talkService;
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	/**
	 * 分页查询公告
	 * @param announ
	 * @return
	 */
	public List<Announcement> listPagedAnnoun(Announcement announ,UserInfo userInfo){
		List<Announcement> list = announcementDao.listPagedAnnoun(announ,userInfo);
		return list;
	}
	/**
	 * 获取权限下前N条数据
	 * @param userInfo
	 * @param num
	 * @return
	 */
	public List<Announcement> firstNAnnoun(UserInfo userInfo,Integer num){
		List<Announcement> list = announcementDao.firstNAnnoun(userInfo, num);
		return list;
	}
	
	/**
	 * 添加公告
	 * @param announ  公告
	 * @param msgShare 消息分享
	 * @param userInfo 
	 * @param sendWay
	 * @return
	 * @throws Exception
	 */
	public Integer addAnnoun(Announcement announ,MsgShare msgShare,UserInfo userInfo, String sendWay) throws Exception{
		//企业号
		announ.setComId(userInfo.getComId());
		//创建人
		announ.setCreator(userInfo.getId());
		//删除标识(正常)
		announ.setDelState(ConstantInterface.MOD_PREDEL_SATATE_NO);
		//阅读次数
		announ.setReadTime(0);
		//公告范围
		if((announ.getListScopeDep()!=null && announ.getListScopeDep().size()>0 )
				|| ( announ.getListScopeUser()!=null && announ.getListScopeUser().size()>0)){
			//设置范围
			announ.setScopeState(1);
		}else{
			//未设置范围
			announ.setScopeState(0);
		}
		//公告基本信息存入
		Integer announId = announcementDao.add(announ);
		announ.setId(announId);
		//公告的附件
		List<Upfiles> listUpfiles = announ.getListUpfiles();
		if(null!=listUpfiles && !listUpfiles.isEmpty()){
			AnnounUpfile announUpfile =null;
			List<Integer> listFiled = new ArrayList<Integer>();
			for(Upfiles file : listUpfiles){
				announUpfile = new AnnounUpfile();
				announUpfile.setComId(announ.getComId());
				announUpfile.setBusId(announId);
				announUpfile.setUpfileId(file.getId());
				announUpfile.setUserId(announ.getCreator());
				announcementDao.add(announUpfile);
				//为附件创建索引
				uploadService.updateUpfileIndex(file.getId(),userInfo,"add",announ.getId(),ConstantInterface.TYPE_ANNOUNCEMENT);
				listFiled.add(file.getId());
			}
			//归档到文档中心
			fileCenterService.addModFile(userInfo, listFiled, announ.getTitle());
			
		}
		
		//存部门范围
		List<AnnounScopeByDep> listScopeDep = announ.getListScopeDep();
		if(null != listScopeDep && !listScopeDep.isEmpty()){
			for (AnnounScopeByDep announScopeByDep : listScopeDep) {
				if(null == announScopeByDep.getDepId() || announScopeByDep.getDepId() <=0){
					continue;
				}else{
					announScopeByDep.setAnnounId(announId);
					announScopeByDep.setComId(userInfo.getComId());
					announcementDao.add(announScopeByDep);
				}
			}
		}
		//存人员范围
		List<AnnounScopeByUser> listScopeUser = announ.getListScopeUser();
		if(null != listScopeUser && !listScopeUser.isEmpty()){
			for (AnnounScopeByUser announScopeByUser : listScopeUser) {
				if(null == announScopeByUser.getUserId()|| announScopeByUser.getUserId() <=0){
					continue;
				}else{
					announScopeByUser.setComId(userInfo.getComId());
					announScopeByUser.setAnnounId(announId);
					announcementDao.add(announScopeByUser);
				}
			}
		}
		
		//存日志
		logsService.addLogs(userInfo.getComId(),  announId, userInfo.getId(),
				userInfo.getUserName(), "创建公告:\""+announ.getTitle()+"\"", ConstantInterface.TYPE_ANNOUNCEMENT);
		//标识关注
		if(null!=announ.getAttentionState() && announ.getAttentionState().equals("1")){
			attentionService.addAtten(ConstantInterface.TYPE_ANNOUNCEMENT, announ.getId(), userInfo);
		}
		//公告通知范围人
		List<UserInfo> shares = new ArrayList<UserInfo>();
		
		if(announ.getScopeState().equals(0)){
			shares = userInfoService.listAllEnabledUser(userInfo);
		}else{
			shares = userInfoService.listAnnounScopeUser(userInfo.getComId(), announId);
		}
		
		String content = "创建公告:\""+announ.getTitle()+"\"";
		if(null!= sendWay && sendWay.equals("todo")){//待办消息
			//添加待办提醒通知
			this.updateTodoWorksThread(userInfo, shares, announ.getId(), ConstantInterface.TYPE_ANNOUNCEMENT, content);
		}else{//普通消息
			this.sendMsg(userInfo, shares, announ.getId(), ConstantInterface.TYPE_ANNOUNCEMENT, content);
		}
		//添加工作轨迹
		systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_ANNOUNCEMENT, 
				announId, content, content);
		//创建索引
		this.updateAnnounIndex(announId, userInfo, "add");
		return announId;
	}
	/**
	 * 发送普通消息提示线程
	 * @param userInfo 当前操作人
	 * @param shares 接受方
	 * @param busId 业务主键
	 * @param busType 业务类别
	 * @param content 内容
	 */
	private void sendMsg(UserInfo userInfo,List<UserInfo> shares,Integer busId,String busType,String content) {
		//单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		//跟范围人员发送通知消息
		pool.execute(new SendMsgThread(todayWorksService, userInfo, shares, busId, busType, content));
	}
	/**
	 * 发送待办提示线程
	 * @param userInfo 当前操作人
	 * @param shares 接受方
	 * @param busId 业务主键
	 * @param busType 业务类别
	 * @param content 内容
	 */
	private void updateTodoWorksThread(UserInfo userInfo,List<UserInfo> shares,Integer busId,String busType,String content){
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		pool.execute(new UpdateTodoWorksThread(todayWorksService, userInfo, shares, busId, busType, content));
	}
	/**
	 * 更新公告索引
	 * @param AnnounId
	 * @param userInfo
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception 
	 */
	public void updateAnnounIndex(Integer announId,UserInfo userInfo,String opType) throws Exception{
		//更新公告索引
		Announcement announ = (Announcement) announcementDao.objectQuery(Announcement.class,announId);
		if(null==announ){return;}
		StringBuffer attStr = new StringBuffer(announ.getTitle());
		//单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String index_key = userInfo.getComId()+"_"+ConstantInterface.TYPE_ANNOUNCEMENT+"_"+announId;
		//为公告创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				index_key,userInfo.getComId(),announId,ConstantInterface.TYPE_ANNOUNCEMENT,
				announ.getTitle(),attStr.toString(),DateTimeUtil.parseDate(announ.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,index_key));
		}
	}
	/**
	 * 更新公告标题
	 * @param announcement
	 * @param userInfo
	 * @return
	 */
	public boolean updateAnnounTitle(Announcement announ,UserInfo userInfo){
		boolean succ = true;
		try {
			//标题变更
			announcementDao.updateTitle(announ);
			
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announ.getId(), userInfo.getId(), userInfo.getUserName(), "公告名称变更为\""+announ.getTitle()+"\"成功",ConstantInterface.TYPE_ANNOUNCEMENT);
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announ.getId(), userInfo.getId(), userInfo.getUserName(), "公告名称变更为\""+announ.getTitle()+"\"失败",ConstantInterface.TYPE_ANNOUNCEMENT);
		}
		return succ;
	}
	/**
	 * 删除公告附件
	 * @param announId
	 * @param upfileId
	 * @param userInfo
	 * @return
	 */
	public boolean delAnnounFile(Integer announId,Integer upfileId,UserInfo userInfo){
		boolean succ = true;
		try {
			//删除公告附件
			announcementDao.delByField("announUpfile", new String[]{"comId","busId","upfileId"}, new Object[]{userInfo.getComId(),announId,upfileId});
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announId, userInfo.getId(), userInfo.getUserName(), "公告删除附件成功",ConstantInterface.TYPE_ANNOUNCEMENT);
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announId, userInfo.getId(), userInfo.getUserName(), "公告删除附件失败",ConstantInterface.TYPE_ANNOUNCEMENT);
		}
		return succ;
	}
	/**
	 * 公告说明更新
	 * @param task
	 * @return
	 */
	public boolean updateAnnounmRemark(Announcement announ,UserInfo userInfo){
		boolean succ = true;
		try {
			//更新公告进度
			announcementDao.updateAnnounRemark(announ);
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announ.getId(), userInfo.getId(), userInfo.getUserName(), "公告内容变更成功",ConstantInterface.TYPE_ANNOUNCEMENT);
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announ.getId(), userInfo.getId(), userInfo.getUserName(), "公告内容变更失败",ConstantInterface.TYPE_ANNOUNCEMENT);
		}
		return succ;
	}
	/**
	 * 公告类型更新
	 * @param announ
	 * @param userInfo
	 * @return
	 */
	public boolean updateAnnounType(Announcement announ,UserInfo userInfo){
		boolean succ = true;
		try {
			//更新公告进度
			announcementDao.update(announ);
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announ.getId(), userInfo.getId(), userInfo.getUserName(), "公告类型变更成功",ConstantInterface.TYPE_ANNOUNCEMENT);
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announ.getId(), userInfo.getId(), userInfo.getUserName(), "公告类型变更失败",ConstantInterface.TYPE_ANNOUNCEMENT);
		}
		return succ;
	}
	/**
	 * 增加阅读次数
	 * @param announ
	 */
	public void updateAnnounRead(Integer announId){
		announcementDao.updateAnnounRead(announId);
	}
	/**
	 * 公告重要程度更新
	 * @param announ
	 * @param userInfo
	 * @return
	 */
	public boolean updateAnnounGrade(Announcement announ,UserInfo userInfo){
		boolean succ = true;
		try {
			//更新公告进度
			announcementDao.updateGrade(announ);
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announ.getId(), userInfo.getId(), userInfo.getUserName(), "公告重要程度变更成功",ConstantInterface.TYPE_ANNOUNCEMENT);
		} catch (Exception e) {
			succ = false ;
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announ.getId(), userInfo.getId(), userInfo.getUserName(), "公告重要程度变更失败",ConstantInterface.TYPE_ANNOUNCEMENT);
		}
		return succ;
	}
		
	/**
	 * 添加公告人员范围
	 * @param announId
	 * @param userId
	 * @return
	 */
	public boolean addAnnounScopeUser(Integer announId,Integer userId,UserInfo userInfo){
		boolean succ = true;
		try{
			this.addBeforeUpdateScope(announId,userInfo.getComId());
			
			AnnounScopeByUser announScopeUser = new AnnounScopeByUser();
			announScopeUser.setComId(userInfo.getComId());
			announScopeUser.setUserId(userId);
			announScopeUser.setAnnounId(announId);
			announcementDao.add(announScopeUser);
			logsService.addLogs(userInfo.getComId(),announId, userInfo.getId(), userInfo.getUserName(), "添加公告人员范围成功",ConstantInterface.TYPE_ANNOUNCEMENT);
		}catch(Exception e) {
			succ = false ;
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announId, userInfo.getId(), userInfo.getUserName(), "添加公告人员范围失败",ConstantInterface.TYPE_ANNOUNCEMENT);
		}
			
		return succ;
	}
	/**
	 * 添加公告部门范围
	 * @param announId
	 * @param depId
	 * @param userInfo
	 * @return
	 */
	public boolean addAnnounScopeDep(Integer announId,Integer depId,UserInfo userInfo){
		boolean succ = true;
		try{
			this.addBeforeUpdateScope(announId,userInfo.getComId());
			
			AnnounScopeByDep announScopeDep = new AnnounScopeByDep();
			announScopeDep.setComId(userInfo.getComId());
			announScopeDep.setDepId(depId);
			announScopeDep.setAnnounId(announId);
			announcementDao.add(announScopeDep);
			logsService.addLogs(userInfo.getComId(),announId, userInfo.getId(), userInfo.getUserName(), "添加公告部门范围成功",ConstantInterface.TYPE_ANNOUNCEMENT);
		}catch(Exception e) {
			succ = false ;
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announId, userInfo.getId(), userInfo.getUserName(), "添加公告部门范围失败",ConstantInterface.TYPE_ANNOUNCEMENT);
		}
			
		return succ;
	}
	
	/**
	 * 删除公告人员范围
	 * @param announId
	 * @param userId
	 * @return
	 */
	public boolean delAnnounScopeUser(Integer announId,Integer userId,UserInfo userInfo){
		boolean succ = true;
		try{
			announcementDao.delByField("announScopeByUser", new String[]{"comId","announId","userId"},
					new Object[]{userInfo.getComId(),announId,userId});
			this.delAfterUpdateScope(announId,userInfo.getComId());
			logsService.addLogs(userInfo.getComId(),announId, userInfo.getId(), userInfo.getUserName(), "删除公告人员范围成功",ConstantInterface.TYPE_ANNOUNCEMENT);
		}catch(Exception e) {
			succ = false ;
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announId, userInfo.getId(), userInfo.getUserName(), "删除公告人员范围失败",ConstantInterface.TYPE_ANNOUNCEMENT);
		}
			
		return succ;
	}
	/**
	 * 删除公告部门范围
	 * @param announId
	 * @param depId
	 * @param userInfo
	 * @return
	 */
	public boolean delAnnounScopeDep(Integer announId,Integer depId,UserInfo userInfo){
		boolean succ = true;
		try{
			announcementDao.delByField("announScopeByDep", new String[]{"comId","announId","depId"},
					new Object[]{userInfo.getComId(),announId,depId});
			 this.delAfterUpdateScope(announId,userInfo.getComId());
			logsService.addLogs(userInfo.getComId(),announId, userInfo.getId(), userInfo.getUserName(), "删除公告部门范围成功",ConstantInterface.TYPE_ANNOUNCEMENT);
		}catch(Exception e) {
			succ = false ;
			//模块日志添加
			logsService.addLogs(userInfo.getComId(),announId, userInfo.getId(), userInfo.getUserName(), "删除公告部门范围失败",ConstantInterface.TYPE_ANNOUNCEMENT);
		}
			
		return succ;
	}
	
	
	/**
	 * 公告查看权限验证
	 * @param comId
	 * @param announId
	 * @param userId
	 * @return
	 */
	public boolean authorCheck(Integer comId,Integer announId,Integer userId){
		List<Announcement> listAnnoun = announcementDao.authorCheck(comId,announId,userId);
		if(null!=listAnnoun && !listAnnoun.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 获取公告信息
	 * @param announId
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Announcement getAnnouncementInfo(Integer announId, Integer userId) {
		Announcement announ  = announcementDao.getAnnouncementInfo(announId,userId);
		//公告附件
		announ.setListAnnounFiles(this.listAnnounFiles(announ.getComId(), announId));
		return announ;
	}

	/**
	 * 批量删除公告
	 * @param ids
	 * @param sessionUser
	 * @return
	 * @throws Exception 
	 */
	public Boolean delPreAnnouncement(Integer[] ids, UserInfo userInfo) throws Exception {
		Boolean flag = true;
		if(null!=ids && ids.length>0){
			for (Integer id : ids) {
				
				Announcement announTemp = (Announcement) announcementDao.objectQuery(Announcement.class, id);
				if(null==announTemp || announTemp.getDelState()==1){//不存在或是已经预删除了
					return false;
				}
				
				Announcement announ =new Announcement();
				//主键
				announ.setId(id);
				//预删除标识
				announ.setDelState(1);
				//修改投票信息
				announcementDao.update(announ);
				
				//删除数据更新记录
				announcementDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_ANNOUNCEMENT,id});
				//删除回收箱数据
				announcementDao.delByField("recycleBin", new String[]{"comId","busType","busId","userId"}, 
						new Object[]{userInfo.getComId(),ConstantInterface.TYPE_ANNOUNCEMENT,id,userInfo.getId()});
				
				//回收箱
				RecycleBin recyleBin =  new RecycleBin();
				//业务主键
				recyleBin.setBusId(id);
				//业务类型
				recyleBin.setBusType(ConstantInterface.TYPE_ANNOUNCEMENT);
				//企业号
				recyleBin.setComId(userInfo.getComId());
				//创建人
				recyleBin.setUserId(userInfo.getId());
				announcementDao.add(recyleBin);
				
				this.delAnnoun(id, userInfo);
			}
		}
		
		
		return flag;
		
	}
	/**
	 * 永久删除公告
	 * @param announId
	 * @param userInfo
	 */
	public void delAnnoun(Integer announId,UserInfo userInfo) throws Exception {
		//更新投票索引
		//this.updateVoteIndex(id,sessionUser,"del");
		
		Integer comId = userInfo.getComId();
		
		//删除日志
		logsService.delLogs(comId, announId,ConstantInterface.TYPE_ANNOUNCEMENT);
		//删除讨论
		talkService.delTalkUpfiles(comId, announId, ConstantInterface.TYPE_ANNOUNCEMENT);
		//删除讨论
		talkService.delTalk(comId, announId, ConstantInterface.TYPE_ANNOUNCEMENT);
		//删除公告附件
		announcementDao.delByField("announUpfile", new String[]{"comId","busId"}, new Object[]{comId,announId});
		//删除公告人员
		announcementDao.delByField("announScopeByUser", new String[]{"comId","announId"}, new Object[]{comId,announId});
		//删除公告部门
		announcementDao.delByField("announScopeByDep", new String[]{"comId","announId"}, new Object[]{comId,announId});

		//删除浏览记录
		announcementDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, new Object[]{comId,announId,ConstantInterface.TYPE_ANNOUNCEMENT});
		//关注信息
		announcementDao.delByField("attention", new String[]{"comId","busId","busType"}, new Object[]{comId,announId,ConstantInterface.TYPE_ANNOUNCEMENT});
		
		//最新动态
		//announcementDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, new Object[]{comId,id,ConstantInterface.TYPE_VOTE});
		
		Announcement announ = announcementDao.getAnnouncementInfo(announId, userInfo.getId());
		if(null!=announ){
			
			//添加工作轨迹
			systemLogService.addSystemLogWithTrace(userInfo, userInfo.getId(), ConstantInterface.TYPE_ANNOUNCEMENT, 
					announId, "删除公告"+announ.getTitle(), "删除公告"+announ.getTitle());
		}
		
		//将发起投票的分享消息修改为工作轨迹
		//msgShareService.delShareMsg(ConstantInterface.TYPE_VOTE,id, sessionUser,0);
		
		
		//删除公告
		announcementDao.delById(Announcement.class, announId);

	}
	/**
	 * 公告附件
	 * @param comId
	 * @param announId
	 * @return
	 */
	public List<AnnounUpfile> listAnnounFiles(Integer comId,Integer announId){
		List<AnnounUpfile>  list = announcementDao.listAnnounFiles(comId, announId);
		return list;
	}
	/********************************公告范围****************************************/
	/**
	 * 获取公告的公告部门范围
	 * @param comId
	 * @param announId
	 * @return
	 */
	public List<Department> listAnnumnScopeDep(Integer comId,Integer announId){
		List<Department> list = announcementDao.listAnnumnScopeDep(comId, announId);
		return list;
	}
	
	/**
	 * 获取公告的公告人员范围
	 * @param comId
	 * @param announId
	 * @return
	 */
	public List<UserInfo> listAnnumnScopeUser(Integer comId,Integer announId){
		 List<UserInfo> list = announcementDao.listAnnumnScopeUser(comId, announId);
		return list;
	}
	/**
	 * 删除人员和范围后检查是否需要更新公告范围
	 * @param announId
	 * @param comId
	 */
	public boolean delAfterUpdateScope(Integer announId,Integer comId){
		//删除只有无公告范围 修改公告范围状态
		List<UserInfo> user = this.listAnnumnScopeUser(comId, announId);
		List<Department> dep = this.listAnnumnScopeDep(comId, announId);
		if( (null == dep ||dep.isEmpty()) && (null == user || user.isEmpty()) ){
			announcementDao.updateScopeState(comId, announId, 0);
			return true;
		}
		return false;
	}
	/**
	 * 添加人员和范围前检查是否需要更新公告范围
	 * @param announId
	 * @param comId
	 */
	public boolean addBeforeUpdateScope(Integer announId,Integer comId){
		//删除只有无公告范围 修改公告范围状态
		List<UserInfo> user = this.listAnnumnScopeUser(comId, announId);
		List<Department> dep = this.listAnnumnScopeDep(comId, announId);
		if( (null != dep && !dep.isEmpty()) || (null != user && !user.isEmpty())){
			announcementDao.updateScopeState(comId, announId, 1);
			return true;
		}
		return false;
	}
	/**
	 * 删除查看范围
	 * @param type
	 * @param comId 类型
	 * @param busId 主键
	 * @param announId 公告主键
	 */
	public void delScope(String type,Integer comId,Integer busId,Integer announId) {
		 if(type.equals("dep")){
			announcementDao.delByField("announScopeByDep", 
					new String[]{"comid","depId","announId"}, new Object[]{comId,busId,announId});
		}else if(type.equals("user")){
			announcementDao.delByField("announScopeByUser", 
					new String[]{"comid","userId","announId"}, new Object[]{comId,busId,announId});
		}
	}
	/**
	 * 修改查看范围
	 * @param type
	 * @param comId 类型
	 * @param busId 主键
	 * @param announId 公告主键
	 */
	public void updateScope(String type,Integer[] busId,Integer announId,Integer comId) {
	 if(type.equals("dep")){
			//清除部门范围
			announcementDao.delByField("announScopeByDep",new String[]{"announId"}, new Object[]{announId});
			if(null!= busId && busId.length>0){
				for(int i =0;i<busId.length;i++){
					 AnnounScopeByDep dep = new AnnounScopeByDep();
					 dep.setComId(comId);
					 dep.setAnnounId(announId);
					 dep.setDepId(busId[i]);
					 announcementDao.add(dep);
				}
			 }
		}else if(type.equals("user")){
			//清除人员范围
			announcementDao.delByField("announScopeByUser", new String[]{"announId"}, new Object[]{announId});
			if(null!= busId && busId.length>0){
				for(int i =0;i<busId.length;i++){
					 AnnounScopeByUser user = new AnnounScopeByUser();
					 user.setComId(comId);
					 user.setAnnounId(announId);
					 user.setUserId(busId[i]);
					 announcementDao.add(user);
				}
			 }
		}
	}
	/**
	 * 分页查询公告主键
	 * @param announId
	 * @param comId
	 * @return
	 */
	public List<AnnounUpfile> listPagedAnnounFiles(Integer announId, Integer comId) {
		List<AnnounUpfile> listFiles = announcementDao.listPagedMeetUpfiles(announId,comId);
		return listFiles;
	}
	
}
