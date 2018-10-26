package com.westar.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.westar.base.model.AnsFile;
import com.westar.base.model.AnsTalk;
import com.westar.base.model.Answer;
import com.westar.base.model.MeetTalkFile;
import com.westar.base.model.MsgShare;
import com.westar.base.model.QasTalkFile;
import com.westar.base.model.QuesFile;
import com.westar.base.model.QuesLog;
import com.westar.base.model.Question;
import com.westar.base.model.RecycleBin;
import com.westar.base.model.ShareGroup;
import com.westar.base.model.SummaryFile;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.QasDao;
import com.westar.core.thread.IndexUpdateThread;

@Service
public class QasService {

	@Autowired
	QasDao qasDao;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	IndexService indexService;
	
	@Autowired
	JiFenService jifenService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	AttentionService attentionService;
	
	@Autowired
	ChatService chatService;
	@Autowired
	UploadService uploadService;

	/**
	 * 分页查询问题中心的问题
	 * @param question
	 * @return
	 */
	public List<Question> listPagedQas(Question question) {
		List<Question> list = qasDao.listPagedQas(question);
		return list;
	}
	/**
	 * 获取团队所有问答
	 * @param userInfo
	 * @return
	 */
	public List<Question> listQasOfAll(UserInfo userInfo) {
		List<Question> list = qasDao.listQasOfAll(userInfo);
		return list;
	}

	/**
	 * 发布问题
	 * @param ques 问题详情
	 * @param userInfo 操作员
	 * @param msgShare 
	 * @return
	 * @throws Exception 
	 */
	public Integer addQues(Question ques, UserInfo userInfo, MsgShare msgShare) throws Exception {
		//问题开启
		ques.setState("1");
		//问题主键
		Integer quesId=qasDao.add(ques);
		//问题补充的附件
		List<QuesFile> quesFiles = ques.getListQuesFiles();
		if(null!=quesFiles && !quesFiles.isEmpty()){
			//缓存附件主键信息
			List<Integer> listFileId = new ArrayList<Integer>();
			
			Integer orderNo = 0;
			for (QuesFile quesFile : quesFiles) {
				//所属问题
				quesFile.setQuesId(quesId);
				quesFile.setUserId(userInfo.getId());
				//企业编号
				quesFile.setComId(userInfo.getComId());
				//附件的主键
				quesFile.setOrderNo(orderNo++);
				//关联附件
				qasDao.add(quesFile);
				//为附件创建索引
				uploadService.updateUpfileIndex(quesFile.getOriginal(),userInfo,"add",quesId,ConstantInterface.TYPE_QUES);
				
				listFileId.add(quesFile.getOriginal());
			}
			//归档到文档中心
			fileCenterService.addModFile(userInfo,listFileId,ques.getTitle());
		}
		//消息分享
		if(null!=ques.getShareMsg() && "yes".equals(ques.getShareMsg()) && null!=msgShare){
			msgShare.setModId(quesId);
			//是分享
			msgShare.setTraceType(0);
			//公开
			msgShare.setIsPub(1);
			//在删除提问的时候将其修改为工作轨迹
			msgShareService.addMsgShare(msgShare,userInfo);
			Integer scopeType =  msgShare.getScopeType();
			if(1==scopeType){//制订了分享范围
				List<ShareGroup> listShareGroup = msgShare.getShareGroup();
				String grpIds = "";
				if(null!=listShareGroup && listShareGroup.size()>0){
					for (ShareGroup shareGroup : listShareGroup) {
						grpIds +=shareGroup.getGrpId()+",";
					}
				}
				grpIds +="-1";
				//自定义的分组成员
				List<UserInfo> listUsers = msgShareService.listGroupUser(userInfo.getComId(),grpIds);
				if(null!=listUsers && listUsers.size()>0){
					//添加待办提醒通知
					todayWorksService.addTodayWorks(userInfo,null, quesId, "提出问题", ConstantInterface.TYPE_QUES, listUsers,null);
				}
				
			}else if(0==scopeType){//分享范围是所有的人
				//发布问题需要消息提醒的所有人
				List<UserInfo> shares = userInfoService.listUser(userInfo.getComId());
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo,null, quesId, "提出问题", ConstantInterface.TYPE_QUES, shares,null);
			}
		}else{
			//发布问题需要消息提醒的所有人
			List<UserInfo> shares = userInfoService.listUser(userInfo.getComId());
			//添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo,null, quesId, "提出问题", ConstantInterface.TYPE_QUES, shares,null);
			
		}
		//添加工作轨迹
		systemLogService.addSystemLogWithTrace(userInfo,userInfo.getId(), ConstantInterface.TYPE_QUES, 
				quesId, "提出问题:"+StringUtil.cutStrFace(ques.getTitle(), 26), "提出问题:"+StringUtil.cutStrFace(ques.getTitle(), 26));
		
		//模块日志添加
		QuesLog quesLog = new QuesLog(userInfo.getComId(), quesId, userInfo.getId(), "提出问题", userInfo.getUserName());
		qasDao.add(quesLog);
		
		String title = ques.getTitle();
		title = StringUtil.cutStrFace(title, 26);
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_QUES,
				"提出问题:"+title,quesId);
		
		//添加关注
		if(null!= ques.getAttentionState() && ques.getAttentionState().equals("1")){
			attentionService.addAtten(ConstantInterface.TYPE_QUES, quesId, userInfo);
		}
		//问答索引添加
		this.updateQasIndex(quesId,userInfo,"add");
		return quesId;
	}

	/**
	 * 所选问题,用于显示
	 * @param id
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Question getQuesById(Integer id, Integer comId,Integer userId) {
		//问题
		Question question = qasDao.getQuesById(id,comId,userId);
		
		if(null!=question){
			//设置附件总数
			Integer fileNum = qasDao.countFile(comId, id);
			question.setFileNum(fileNum);
			
			//问题对应的图片
			question.setListQuesFiles(qasDao.listQuesFile(id,comId));
		}
		return question;
	}
	/**
	 * 所选问题,用于显示
	 * @param id
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Question getQuesById(Integer id) {
		//问题
		Question question = (Question) qasDao.objectQuery(Question.class, id);
		
		return question;
	}
	/**
	 * 所选问题，用于回答
	 * @param quesId
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Question getAns4Ques(Integer quesId, Integer comId,Integer userId) {
		//问题
		Question question = qasDao.getQuesById(quesId,comId,userId);
		
		return question;
	}
	/**
	 * 所选问题，用于回答
	 * @param quesId
	 * @param comId
	 * @param userId
	 * @return
	 */
	public List<Answer> listPagedAnswer(Integer quesId, Integer comId,Integer userId) {
		//问题的回答
		List<Answer> listAns = qasDao.listPagedAns(quesId,comId);
		//问题回答加附件和讨论
		List<Answer> listAnswers = new ArrayList<Answer>();
		if(null!=listAns && listAns.size()>0){
			for (Answer answer : listAns) {
				//回答对应的附件
				answer.setListAnsFiles(qasDao.listAnsFile(quesId,comId,answer.getId()));
				
				//回答的讨论
				List<AnsTalk> listAnsTalk = qasDao.listAnsTalk(quesId,comId,answer.getId());
				//回答讨论加附件
				List<AnsTalk> listAnsTalkAddFile = new ArrayList<AnsTalk>();
				for (AnsTalk ansTalk : listAnsTalk) {
					//回答的讨论加附件
					ansTalk.setListQasTalkFile(qasDao.listQasTalkFile(comId,quesId,answer.getId(),ansTalk.getId()));
					listAnsTalkAddFile.add(ansTalk);
				}
				//回答对应的评论
				answer.setListAnsTalks(listAnsTalkAddFile);
				
				listAnswers.add(answer);
			}
		}
		return listAnswers;
	}

	/**
	 * 预删除提问
	 * @param quesId 问题主键
	 * @param userInfo 用户
	 * @return
	 */
	public Boolean delPreQues(Integer[] quesIds, UserInfo userInfo) {
		//删除结果状态
		Boolean flag = true;
		if(null!=quesIds && quesIds.length>0){
			for (Integer quesId : quesIds) {
				Question ques = (Question) qasDao.objectQuery(Question.class, quesId);
				if(null==ques || ques.getDelState()==1){//问题不存在或是已经预删除了
					return false;
				}
				
				Question question =new Question();
				//问题主键
				question.setId(quesId);
				//预删除标识
				question.setDelState(1);
				//修改问题信息
				qasDao.update(question);
				
				//删除数据更新记录
				qasDao.delByField("todayWorks", new String[]{"comId","busType","busId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_QUES,quesId});
				//删除回收箱数据
				qasDao.delByField("recycleBin", new String[]{"comId","busType","busId","userId"}, new Object[]{userInfo.getComId(),ConstantInterface.TYPE_QUES,quesId,userInfo.getId()});
				
				//回收箱
				RecycleBin recyleBin =  new RecycleBin();
				//业务主键
				recyleBin.setBusId(quesId);
				//业务类型
				recyleBin.setBusType(ConstantInterface.TYPE_QUES);
				//企业号
				recyleBin.setComId(userInfo.getComId());
				//创建人
				recyleBin.setUserId(userInfo.getId());
				qasDao.add(recyleBin);
			}
		}
		return flag;
	}
	/**
	 * 删除提问
	 * @param quesId
	 * @param comId 
	 * @return
	 * @throws Exception 
	 */
	public void delQues(Integer quesId, UserInfo userInfo) throws Exception {
		//更新问答索引库
		this.updateQasIndex(quesId, userInfo,"del");
		
		//删除问答日志
		qasDao.delByField("quesLog", new String[]{"comId","quesId"}, new Object[]{userInfo.getComId(),quesId});
		//删除回答讨论附件
		qasDao.delByField("qasTalkFile", new String[]{"comId","quesId"}, new Object[]{userInfo.getComId(),quesId});
		//删除回答讨论
		qasDao.delByField("ansTalk", new String[]{"comId","quesId"}, new Object[]{userInfo.getComId(),quesId});
		//删除问题回答图片
		qasDao.delByField("ansFile", new String[]{"comId","quesId"}, new Object[]{userInfo.getComId(),quesId});
		//删除回答
		qasDao.delByField("answer", new String[]{"comId","quesId"}, new Object[]{userInfo.getComId(),quesId});
		//删除问题补充图片
		qasDao.delByField("quesFile", new String[]{"comId","quesId"}, new Object[]{userInfo.getComId(),quesId});
		
		//删除浏览记录
		qasDao.delByField("viewRecord", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),quesId,ConstantInterface.TYPE_QUES});
		//关注信息
		qasDao.delByField("attention", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),quesId,ConstantInterface.TYPE_QUES});
		//最新动态
		qasDao.delByField("newsInfo", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),quesId,ConstantInterface.TYPE_QUES});
		
		//删除聊天记录
		chatService.delBusChat(userInfo.getComId(),quesId,ConstantInterface.TYPE_QUES);
		Question ques = (Question) qasDao.objectQuery(Question.class, quesId);
		if(null!=ques){
			
			String title = ques.getTitle();
			title = StringUtil.cutStrFace(title, 26);
			
			//修改积分
			jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_QUESDEL,
					"删除提问:"+title,quesId);
			
			//添加工作轨迹
			systemLogService.addSystemLogWithTrace(userInfo,userInfo.getId(), ConstantInterface.TYPE_QUES, 
					quesId, "删除问题:"+title, "删除问题:"+title);
			
		
			
			//添加工作轨迹
		}
		//将发布时候的分享信息修改为工作轨迹
		msgShareService.delShareMsg(ConstantInterface.TYPE_QUES,quesId, userInfo,0);
		
		//删除问题
		qasDao.delById(Question.class, quesId);
	}

	/**
	 * 关闭问题
	 * @param userInfo 
	 * @param quesId
	 * @param state
	 * @param comId
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void closeQues(Question ques, UserInfo userInfo) {
		qasDao.update(ques);
		
		String content = "关闭问题";
		if(null!=ques.getState()&& "1".equals(ques.getState())){
			content = "开放问题";
		}
		
		//发布问题需要消息提醒的所有人
		List<UserInfo> shares = userInfoService.listUser(userInfo.getComId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, ques.getId(), content, ConstantInterface.TYPE_QUES, shares,null);
		
		//模块日志添加
		QuesLog quesLog = new QuesLog(userInfo.getComId(), ques.getId(), userInfo.getId(), content, userInfo.getUserName());
		qasDao.add(quesLog);
	}

	/**
	 * 修改问题
	 * @param ques
	 * @param fileIds
	 * @param userInfo 
	 * @return
	 * @throws Exception 
	 */
	public void updateQues(Question ques, Integer[] fileIds, UserInfo userInfo) throws Exception {
		
		//问题主键
		Integer quesId=ques.getId();
		//删除问题补充图片
		qasDao.delByField("quesFile", new String[]{"comId","quesId"}, new Object[]{ques.getComId(),ques.getId()});
		//问题补充的附件
		if(null!=fileIds && fileIds.length>0){//有附件存在
			Integer orderNo = 0;
			for (Integer original : fileIds) {
				QuesFile quesFile = new QuesFile();
					//企业编号
					quesFile.setComId(ques.getComId());
					//所属问题
					quesFile.setQuesId(quesId);
					//附件主键
					quesFile.setOriginal(original);
					//上传人
					quesFile.setUserId(userInfo.getId());
					//排序
					quesFile.setOrderNo(orderNo);
					orderNo = orderNo+1;
					
					qasDao.add(quesFile);
			}
			//附件归档
			Question quesT = (Question) qasDao.objectQuery(Question.class, quesId);
			fileCenterService.addModFile(userInfo, Arrays.asList(fileIds), quesT.getTitle());
		}
		
		qasDao.update(ques);
		
		ques = (Question) qasDao.objectQuery(Question.class, ques.getId());
		//模块日志添加
		QuesLog quesLog = new QuesLog(userInfo.getComId(), quesId, userInfo.getId(), "修改问题", userInfo.getUserName());
		qasDao.add(quesLog);
		
		//发布问题需要消息提醒的所有人
		List<UserInfo> shares = userInfoService.listUser(userInfo.getComId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, ques.getId(), "修改补充问题:"+ques.getContent(), ConstantInterface.TYPE_QUES, shares,null);
		//更新问答索引库
//		this.updateQasIndex(quesId, userInfo,"update");
	}

	/**
	 * 回答问题
	 * @param answer
	 * @param userInfo 
	 * @param fileIds 
	 * @return
	 * @throws Exception 
	 */
	public Integer addAns(Answer answer, UserInfo userInfo, Integer[] fileIds) throws Exception {
		//问题回答主键
		Integer ansId=qasDao.add(answer);
		//问题回答补充的附件
		if(null!=fileIds && fileIds.length>0){
			Integer orderNo = 0;
			for (Integer original : fileIds) {
				AnsFile ansFile = new AnsFile();
				//企业编号
				ansFile.setComId(answer.getComId());
				//所属问题
				ansFile.setQuesId(answer.getQuesId());
				//所属回答
				ansFile.setAnswerId(ansId);
				//回答人
				ansFile.setUserId(userInfo.getId());
				//附件主键
				ansFile.setOriginal(original);
				//排序
				ansFile.setOrderNo(orderNo);
				orderNo = orderNo+1;
				
				qasDao.add(ansFile);
				//为附件创建索引
				uploadService.updateUpfileIndex(original,userInfo,"add",answer.getQuesId(),ConstantInterface.TYPE_QUES);
			}
			Question question = (Question) qasDao.objectQuery(Question.class, answer.getQuesId());
			fileCenterService.addModFile(userInfo, Arrays.asList(fileIds), question.getTitle());
			
		}
		//模块日志添加
		QuesLog quesLog = new QuesLog(userInfo.getComId(), answer.getQuesId(), userInfo.getId(), "回答问题", userInfo.getUserName());
		qasDao.add(quesLog);

		//发布问题需要消息提醒的所有人
		List<UserInfo> shares = userInfoService.listUser(userInfo.getComId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, answer.getQuesId(), "问题的一个回答:"+answer.getContent(), ConstantInterface.TYPE_QUES, shares,null);
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ANS,"回答问题",answer.getQuesId());
		//更新问答索引库
//		this.updateQasIndex(answer.getQuesId(), userInfo,"update");
		return ansId;
	}

	/**
	 * 修改回答
	 * @param answer
	 * @param ids
	 * @param userInfo 
	 * @return
	 * @throws Exception 
	 */
	public void updateAns(Answer answer, Integer[] fileIds, UserInfo userInfo) throws Exception {
		//删除问题补充图片
		qasDao.delByField("ansFile", new String[]{"comId","quesId","answerId"}, new Object[]{answer.getComId(),answer.getQuesId(),answer.getId()});
		if(null!=fileIds && fileIds.length>0){
			//问题回答补充的附件
			if(null!=fileIds && fileIds.length>0){
				Integer orderNo = 0;
				for (Integer original : fileIds) {
					AnsFile ansFile = new AnsFile();
					//企业编号
					ansFile.setComId(answer.getComId());
					//所属问题
					ansFile.setQuesId(answer.getQuesId());
					//所属回答
					ansFile.setAnswerId(answer.getId());
					//回答人
					ansFile.setUserId(userInfo.getId());
					//附件主键
					ansFile.setOriginal(original);
					//排序
					ansFile.setOrderNo(orderNo);
					orderNo = orderNo+1;
					
					qasDao.add(ansFile);
				}
				//删除归档信息
				Question question = (Question) qasDao.objectQuery(Question.class, answer.getQuesId());
				fileCenterService.addModFile(userInfo, Arrays.asList(fileIds), question.getTitle());
			}
			
		}
		
		//修改回答
		qasDao.update(answer);
		//模块日志添加
		QuesLog quesLog = new QuesLog(userInfo.getComId(), answer.getQuesId(), userInfo.getId(), "对回答进行修改", userInfo.getUserName());
		qasDao.add(quesLog);

		//发布问题需要消息提醒的所有人
		List<UserInfo> shares = userInfoService.listUser(userInfo.getComId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, answer.getQuesId(), "修改回答为:"+answer.getContent(), ConstantInterface.TYPE_QUES, shares,null);
	
		//更新问答索引库
//		this.updateQasIndex(answer.getQuesId(), userInfo,"update");
	}

	/**
	 * 删除回答
	 * @param ansId
	 * @param quesId
	 * @param userInfo 
	 * @throws Exception 
	 */
	public void delAns(Integer ansId, Integer quesId, UserInfo userInfo) throws Exception {
		//删除回答讨论附件
		qasDao.delByField("qasTalkFile", new String[]{"comId","quesId","ansId"}, new Object[]{userInfo.getComId(),quesId,ansId});
		//删除回答讨论
		qasDao.delByField("ansTalk", new String[]{"comId","quesId","ansId"}, new Object[]{userInfo.getComId(),quesId,ansId});
		//删除问题回答图片
		qasDao.delByField("ansFile", new String[]{"comId","quesId","answerId"}, new Object[]{userInfo.getComId(),quesId,ansId});
		//删除问题
		qasDao.delById(Answer.class, ansId);
		
		//模块日志添加
		QuesLog quesLog = new QuesLog(userInfo.getComId(), quesId, userInfo.getId(), "删除回答", userInfo.getUserName());
		qasDao.add(quesLog);

		//发布问题需要消息提醒的所有人
		List<UserInfo> shares = userInfoService.listUser(userInfo.getComId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, quesId, "删除回答", ConstantInterface.TYPE_QUES, shares,null);
		//更新问答索引库
//		this.updateQasIndex(quesId, userInfo,"update");
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_ANSDEL,"删除回答",quesId);
	}
	/**
	 * 模块日志添加
	 * @param comId
	 * @param voteId
	 * @param userId
	 * @param userName
	 * @param string
	 */
	public void addQuesLog(Integer comId, Integer quesId, Integer userId,
			String userName, String content) {
		QuesLog quesLog = new QuesLog(comId, quesId, userId,content, userName);
		qasDao.add(quesLog);
		
	}
	/**
	 * 问答日志
	 * @param comId
	 * @param quesId
	 * @return
	 */
	public List<QuesLog> listPagedQuesLog(Integer comId, Integer quesId) {
		//问答日志
		List<QuesLog> quesLogs = qasDao.listPagedQuesLog(quesId,comId);
		return quesLogs;
	}

	/**
	 * 评论回答
	 * @param ansTalk
	 * @param upfilesId 
	 * @return
	 * @throws Exception 
	 */
	public Integer addAnsTalk(AnsTalk ansTalk,UserInfo userInfo, Integer[] upfilesId) throws Exception {
		Integer id = qasDao.add(ansTalk);
		if(null!=upfilesId){
			for (Integer upfileId : upfilesId) {
				QasTalkFile qasTalkFile = new QasTalkFile();
				//企业编号
				qasTalkFile.setComId(ansTalk.getComId());
				//回答的主键
				qasTalkFile.setAnsId(ansTalk.getAnsId());
				//问题主键
				qasTalkFile.setQuesId(ansTalk.getQuesId());
				//讨论的主键
				qasTalkFile.setTalkId(id);
				//附件的主键
				qasTalkFile.setUpfileId(upfileId);
				//上传人
				qasTalkFile.setUserId(userInfo.getId());
				
				qasDao.add(qasTalkFile);
				//为附件创建索引
				uploadService.updateUpfileIndex(upfileId,userInfo,"add",ansTalk.getAnsId(),ConstantInterface.TYPE_QUES);
			}
			Question question = (Question) qasDao.objectQuery(Question.class, ansTalk.getQuesId());
			//归档到文档中心
			fileCenterService.addModFile(userInfo,Arrays.asList(upfilesId),question.getTitle());
			
			
		}
		//模块日志添加
		QuesLog quesLog = new QuesLog(userInfo.getComId(), ansTalk.getQuesId(), userInfo.getId(), "对问题回答进行评论", userInfo.getUserName());
		qasDao.add(quesLog);
		
		//发布问题需要消息提醒的所有人
		List<UserInfo> shares = userInfoService.listUser(userInfo.getComId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, ansTalk.getQuesId(), "参与回答的评论:"+ansTalk.getTalkContent(), ConstantInterface.TYPE_QUES, shares,null);

		//更新问答索引库
//		this.updateQasIndex(ansTalk.getQuesId(), userInfo,"update");
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_QASTALK,"参与回答的评论",ansTalk.getQuesId());
		return id;
	}

	/**
	 * 删除回答评论
	 * @param ansTalk
	 * @param delChildNode
	 * @param userInfo 
	 * @return
	 * @throws Exception 
	 */
	public List<Integer> delAnsTalk(AnsTalk ansTalk, String delChildNode, UserInfo userInfo) throws Exception {
		List<Integer> childIds = new ArrayList<Integer>();
		//删除自己
		if(null==delChildNode){
			childIds.add(ansTalk.getId());
			
			//删除附件
			qasDao.delByField("qasTalkFile", new String[]{"comId","talkId"}, 
					new Object[]{ansTalk.getComId(),ansTalk.getId()});
			
			qasDao.delById(AnsTalk.class, ansTalk.getId());
		}else if("yes".equals(delChildNode)){//删除自己和所有的子节点
			//待删除的讨论
			List<AnsTalk> listAnsTalk = qasDao.listAnsTalkForDel(ansTalk.getComId(), ansTalk.getId());
			for (AnsTalk talk : listAnsTalk) {
				childIds.add(talk.getId());
				
				//删除附件
				qasDao.delByField("qasTalkFile", new String[]{"comId","talkId"}, 
						new Object[]{talk.getComId(),talk.getId()});
			}
			//删除当前节点及其子节点回复
			qasDao.delQasTalk(ansTalk.getId(),ansTalk.getComId());
		}else if("no".equals(delChildNode)){//删除自己,将子节点提高一级
			childIds.add(ansTalk.getId());
			//删除附件
			qasDao.delByField("qasTalkFile", new String[]{"comId","talkId"}, 
					new Object[]{ansTalk.getComId(),ansTalk.getId()});
			
			qasDao.updateAnsTalkParentId(ansTalk.getId(),ansTalk.getComId());
			qasDao.delById(AnsTalk.class, ansTalk.getId());
		}
		//模块日志添加
		QuesLog quesLog = new QuesLog(userInfo.getComId(), ansTalk.getQuesId(), userInfo.getId(), "删除对问题回答的评论", userInfo.getUserName());
		qasDao.add(quesLog);

		//发布问题需要消息提醒的所有人
		List<UserInfo> shares = userInfoService.listUser(userInfo.getComId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, ansTalk.getQuesId(), "删除回答的评论", ConstantInterface.TYPE_QUES, shares,null);
		
		//更新问答索引库
//		this.updateQasIndex(ansTalk.getQuesId(), userInfo,"update");
		//修改积分
		jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TALKDEL,"删除回答的评论",ansTalk.getQuesId());
		return childIds;
	}

	/**
	 * 采纳回答
	 * @param ansId
	 * @param quesId
	 * @param userInfo
	 */
	public void updateQues4cnAns(Integer ansId, Integer quesId,
			UserInfo userInfo) {
		//实例化问题
		Question question = new Question();
		question.setId(quesId);
		question.setCnAns(ansId);
		//采纳回答
		qasDao.update(question);
		
		Answer ans = (Answer) qasDao.objectQuery(Answer.class, ansId);
		
		//发布问题需要消息提醒的所有人
		List<UserInfo> shares = userInfoService.listUser(userInfo.getComId());
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, quesId, "采纳回答:"+ans.getContent(), ConstantInterface.TYPE_QUES, shares,null);
		//模块日志添加
		QuesLog quesLog = new QuesLog(userInfo.getComId(), quesId, userInfo.getId(), "采纳回答", userInfo.getUserName());
		qasDao.add(quesLog);
		
	}

	/**
	 * 取得问题的详细信息
	 * @param quesId
	 * @param comId
	 * @param ansId
	 * @return
	 */
	public Answer getAnsById(Integer quesId, Integer comId, Integer ansId) {
		//回答
		Answer answer = qasDao.getAnsById(quesId, comId, ansId);
		if(null!=answer){
			//回答对应的图片
			answer.setListAnsFiles(qasDao.listAnsFile(quesId,comId,ansId));
			//回答的讨论
			List<AnsTalk> listAnsTalk = qasDao.listAnsTalk(quesId,comId,ansId);
			//回答讨论加附件
			List<AnsTalk> list = new ArrayList<AnsTalk>();
			for (AnsTalk ansTalk : listAnsTalk) {
				//回答的讨论加附件
				ansTalk.setTalkContent(StringUtil.toHtml(ansTalk.getTalkContent()));
				ansTalk.setListQasTalkFile(qasDao.listQasTalkFile(comId,quesId,answer.getId(),ansTalk.getId()));
				list.add(ansTalk);
			}
			//回答对应的评论
			answer.setListAnsTalks(list);
		}
		return answer;
	}

	/**
	 * 添加的回复
	 * @param id
	 * @param comId
	 * @return
	 */
	public AnsTalk getAnsTalkById(Integer id,Integer comId) {
		AnsTalk ansTalk = qasDao.getAnsTalkById(id,comId);
		ansTalk.setTalkContent(StringUtil.toHtml(ansTalk.getTalkContent()));
		//回答的讨论加附件
		ansTalk.setListQasTalkFile(qasDao.listQasTalkFile(comId,ansTalk.getQuesId(),ansTalk.getAnsId(),ansTalk.getId()));
		return ansTalk;
	}

	/**
	 * 问答日志
	 * @param userInfo
	 * @param quesId
	 * @return
	 */

	public List<QuesFile> listPagedQuesFile(UserInfo userInfo, Integer quesId) {
		List<QuesFile> list = qasDao.listPagedQuesFile(userInfo,quesId);
		return list;
	}
	/**
	 * 更新索引
	 * @param quesId
	 * @param userInfo
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception
	 */
	public void updateQasIndex(Integer quesId,UserInfo userInfo,String opType) throws Exception{
		//更新问答索引
		Question question = qasDao.getQuesById(quesId,userInfo.getComId(),userInfo.getId());
		if(null==question){return;}
		//连接索引字符串
//		StringBuffer attStr = new StringBuffer(question.getTitle()+","+question.getContent()+","+question.getUserName()+",");
		StringBuffer attStr = new StringBuffer(question.getTitle());
		//获取问答选项描述创建索引
//		List<Answer> listAnswer = qasDao.listAnswer4Index(userInfo.getComId(),quesId);
//		for(Answer vo : listAnswer){
//			attStr.append(vo.getContent()+","+vo.getUserName()+",");
//		}
		//获取问答讨论为其创建索引
//		List<AnsTalk> listVoteTalk = qasDao.listVoteTalk4Index(userInfo.getComId(),quesId);
//		for(AnsTalk vo : listVoteTalk){
//			attStr.append(vo.getTalkContent()+","+vo.getTalkerName()+",");
//		}
		//返回一个线程池（这个线程池只有一个线程）,这个线程池可以在线程死后（或发生异常时）重新启动一个线程来替代原来的线程继续执行下去！
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String indexKey = userInfo.getComId()+"_"+ConstantInterface.TYPE_QUES+"_"+quesId;
		//为问答创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				indexKey,userInfo.getComId(),quesId,ConstantInterface.TYPE_QUES,
				question.getTitle(),attStr.toString(),DateTimeUtil.parseDate(question.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,indexKey));
		}
		
	}
	/**
	 * 获取个人权限下排列前的N个问题集合数据
	 * @param userInfo
	 * @param rowNum
	 * @return
	 */
	public List<Question> firstNQasList(UserInfo userInfo,Integer rowNum)  {
		List<Question> list = qasDao.firstNQasList(userInfo,rowNum);
		return list;
	}
	/**
	 * 删除问答附件
	 * @param quesUpFileId
	 * @param type
	 * @param userInfo
	 * @param quesId
	 */
	public void delQasUpfile(Integer quesUpFileId, String type, UserInfo userInfo, Integer quesId) {
		if(type.equals("ques")){
			QuesFile file = (QuesFile) qasDao.objectQuery(QuesFile.class, quesUpFileId);
			qasDao.delById(QuesFile.class, quesUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) qasDao.objectQuery(Upfiles.class, file.getOriginal());
			this.addQuesLog(userInfo.getComId(),quesId,userInfo.getId(),userInfo.getUserName(),"删除了问题附件："+upfiles.getFilename());
		}else if(type.equals("ans")){
			AnsFile file = (AnsFile) qasDao.objectQuery(AnsFile.class, quesUpFileId);
			qasDao.delById(AnsFile.class, quesUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) qasDao.objectQuery(Upfiles.class, file.getOriginal());
			this.addQuesLog(userInfo.getComId(),quesId,userInfo.getId(),userInfo.getUserName(),"删除了回答附件："+upfiles.getFilename());
		}else if(type.equals("talk")){
			QasTalkFile file = (QasTalkFile) qasDao.objectQuery(QasTalkFile.class, quesUpFileId);
			qasDao.delById(QasTalkFile.class, quesUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) qasDao.objectQuery(Upfiles.class, file.getUpfileId());
			this.addQuesLog(userInfo.getComId(),quesId,userInfo.getId(),userInfo.getUserName(),"删除了问答留言附件："+upfiles.getFilename());
		}
		
	}
}
