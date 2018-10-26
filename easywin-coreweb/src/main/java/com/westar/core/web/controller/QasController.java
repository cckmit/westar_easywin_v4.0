package com.westar.core.web.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.AnsTalk;
import com.westar.base.model.Answer;
import com.westar.base.model.MsgShare;
import com.westar.base.model.QuesFile;
import com.westar.base.model.QuesLog;
import com.westar.base.model.Question;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.StringUtil;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.QasService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UploadService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.web.FreshManager;

/**
 * 问答中心
 * @author H87
 *
 */
@Controller
@RequestMapping("/qas")
public class QasController extends BaseController{

	@Autowired
	QasService qasService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	
	/**
	 * 分页查询问题中心的问题
	 * @param question
	 * @return
	 */
	@RequestMapping("/listPagedQas")
	public ModelAndView listPagedQas(HttpServletRequest request,Question question) {
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		UserInfo userInfo = this.getSessionUser();
		question.setComId(userInfo.getComId());
		question.setSessionUser(userInfo.getId());
		question.setUserId(userInfo.getId());
		
		List<Question> list = qasService.listPagedQas(question);
		ModelAndView mav = new ModelAndView("/qas/listPagedQas", "list", list);
		mav.addObject("userInfo",userInfo);
		
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_QUES);
				
		return mav;
	}
	
	/**
	 * 提问页面跳转
	 * @param question
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/addQuesPage")
	public ModelAndView addQuesPage(Question question,String redirectPage) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/qas/addQues", "redirectPage", redirectPage);
		
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
		
		mav.addObject("userInfo",userInfo);
		return mav;
	}
	
	/**
	 *  发布问题
	 * @param ques  问题详情
	 * @param idType 
	 * @param fileIds 附件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addQues")
	public ModelAndView addQues(Question ques,String idType,String way,String sid) throws Exception {
		
		UserInfo userInfo = this.getSessionUser();
		//所在企业
		ques.setComId(userInfo.getComId());
		//创建人
		ques.setUserId(userInfo.getId());
		//删除标识(正常)
		ques.setDelState(0);
		//采纳的答案
		ques.setCnAns(0);
		
		//获取信息分享以及范围
		MsgShare msgShare = CommonUtil.getMsgShare(idType,ConstantInterface.TYPE_QUES,ques.getTitle(),userInfo);
				
		//发布问题
		qasService.addQues(ques,userInfo,msgShare);
		
		this.setNotification(Notification.SUCCESS, "添加成功");
		
		ModelAndView view = new ModelAndView("/refreshParent");
		
		return view;
	}

	/**
	 * 删除问题
	 * @param quesId
	 * @return
	 * @throws IOException 
	 * @throws CorruptIndexException 
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxDelQues" , method=RequestMethod.POST)
	public Map<String,String> ajaxDelQues(Integer quesId) throws CorruptIndexException, IOException, ParseException {
		Map<String,String> map = new HashMap<String, String>();
		UserInfo userInfo = this.getSessionUser();
		//删除提问
		Boolean flag = qasService.delPreQues(new Integer[]{quesId},userInfo);
		map.put("state", "y");
		if(!flag){
			this.setNotification(Notification.ERROR, "问题已被删除!");
		}else{
			this.setNotification(Notification.SUCCESS, "删除成功!");
		}
		

		return map;
	}
	/**
	 * 删除问答
	 * @param ids
	 * @param redirectPage
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/delBatchQues")
	public ModelAndView delBatchQues(Integer[] ids,String redirectPage) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		//删除提问
		Boolean flag = qasService.delPreQues(ids,userInfo);
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("redirect:"+redirectPage);
		if(null==flag){
			this.setNotification(Notification.ERROR, "问题已被删除!");
		}else{
			this.setNotification(Notification.SUCCESS, "删除成功!");
		}
		return mav;
	}
	
	/**
	 * 关闭问题
	 * @param quesId
	 * @param state
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/closeQues" , method=RequestMethod.POST)
	public Map<String,String> closeQues(Question ques) {
		Map<String,String> map = new HashMap<String, String>();
		UserInfo userInfo = this.getSessionUser();
		
		ques.setContent(null);
		//关闭问题
		qasService.closeQues(ques,userInfo);
		map.put("state", "y");
		return map;
	}
	
	/**
	 * 修改问题
	 * @param ques
	 * @param fileIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/updateQues" , method=RequestMethod.POST)
	public Map<String,String> updateQues(Question ques,Integer[] fileIds) throws Exception {
		Map<String,String> map = new HashMap<String, String>();
		
		UserInfo userInfo = this.getSessionUser();
		//所在企业
		ques.setComId(userInfo.getComId());
		//修改问题
		qasService.updateQues(ques,fileIds,userInfo);
		map.put("state", "y");
		this.setNotification(Notification.SUCCESS, "修改成功！");
		return map;
	}

	
	/**
	 * 查看问题页面
	 * @param question
	 * @return
	 */
	@RequestMapping("/viewQuesPage")
	public ModelAndView viewQuesPage(HttpServletRequest request,Integer id,String redirectPage){
		ModelAndView view = new ModelAndView("/qas/viewQues");
		UserInfo userInfo = this.getSessionUser();
		
		//所选问题
		Question ques = qasService.getQuesById(id,userInfo.getComId(),userInfo.getId());
		if(null==ques || ques.getDelState()==1){//问题是否存在
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR, "该问题已被删除！");
			return view;
		}
		
		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_QUES, id);
		//取得是否添加浏览记录
		boolean bool = FreshManager.checkOpt(request, viewRecord);
		if(bool){
			//添加查看记录
			viewRecordService.addViewRecord(userInfo,viewRecord);
		}
		view.addObject("ques", ques);
		view.addObject("userInfo", userInfo);
		view.addObject("redirectPage", redirectPage);
		
		//浏览的人员
		List<ViewRecord> listViewRecord = viewRecordService.listViewRecord(userInfo,ConstantInterface.TYPE_QUES,id);
		view.addObject("listViewRecord", listViewRecord);
		
		//查看问题，删除今日提醒
		todayWorksService.updateTodoWorkRead(id,userInfo.getComId(),userInfo.getId(), ConstantInterface.TYPE_QUES,0);
		
		return view;
	}

	/********************************以上是提问***************************************************/
	/********************************以下是回答***************************************************/
	/**
	 * 回答问题跳转页面
	 * @param question
	 * @return
	 */
	@RequestMapping("/ansQuesPage")
	public ModelAndView ansQues(Integer quesId){
		ModelAndView mav = new ModelAndView("/qas/ansQues");
		UserInfo userInfo = this.getSessionUser();
		//查看回答，删除今日提醒
		todayWorksService.updateTodoWorkRead(quesId,userInfo.getComId(),userInfo.getId(), ConstantInterface.TYPE_QUES,0);
		
		//所选问题的
		Question ques = qasService.getAns4Ques(quesId,userInfo.getComId(),userInfo.getId());
		if(null!=ques){
			//问题的回答
			List<Answer> list = qasService.listPagedAnswer(quesId,userInfo.getComId(),userInfo.getId());
			mav.addObject("ques", ques);
			mav.addObject("list", list);
			mav.addObject("sessionUser", userInfo);
		}else{
			mav.addObject("state", "n");
			this.setNotification(Notification.ERROR, "问题已被删除");
		}
		return mav;
	}
	
	
	/**
	 * 回答问题
	 * @param answer
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/addAns" , method=RequestMethod.POST)
	public Map<String,Object> addAns(Answer answer,Integer[] fileIds) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		//所在企业
		answer.setComId(userInfo.getComId());
		//回答人
		answer.setUserId(userInfo.getId());
		
		//回答问题
		Integer ansId= qasService.addAns(answer,userInfo,fileIds);
		//取得问题的详细信息
		answer = qasService.getAnsById(answer.getQuesId(),userInfo.getComId(),ansId);
		String content = StringUtil.toHtml(answer.getContent());
		
		map.put("state", "y");
		map.put("answer", answer);
		map.put("sessionUser", this.getSessionUser());
		map.put("content", content);
		return map;
	}
	
	/**
	 * 修改回答
	 * @param answer
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/updateAns" , method=RequestMethod.POST)
	public Map<String,Object> updateAns(Answer answer,Integer[] fileIds) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		//所在企业
		answer.setComId(userInfo.getComId());
		//创建人
		answer.setUserId(userInfo.getId());
		//修改回答
		qasService.updateAns(answer,fileIds,userInfo);
		//取得问题的详细信息
		answer = qasService.getAnsById(answer.getQuesId(),userInfo.getComId(),answer.getId());
		String content = StringUtil.toHtml(answer.getContent());
		map.put("state", "y");
		map.put("answer", answer);
		map.put("sessionUser", userInfo);
		map.put("content", content);
		map.put("state", "y");
		return map;
	}
	/**
	 * 删除回答
	 * @param ansId
	 * @param quesId
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/delAns" , method=RequestMethod.POST)
	public Map<String,String> delAns(Integer ansId,Integer quesId) throws Exception {
		Map<String,String> map = new HashMap<String, String>();
		UserInfo userInfo = this.getSessionUser();
		//删除回答
		qasService.delAns(ansId,quesId,userInfo);
		map.put("state", "y");
		return map;
	}
	/**
	 * 采纳回答
	 * @param ansId
	 * @param quesId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/cnAns" , method=RequestMethod.POST)
	public Map<String,String> cnAns(Integer ansId,Integer quesId) {
		Map<String,String> map = new HashMap<String, String>();
		UserInfo userInfo = this.getSessionUser();
		// 采纳回答
		qasService.updateQues4cnAns(ansId,quesId,userInfo);
		map.put("state", "y");
		return map;
	}
	/********************************以上是回答***************************************************/
	/**
	 * 问答日志页面
	 * @param quesId
	 * @return
	 */
	@RequestMapping("/quesLogPage")
	public ModelAndView quesLog(Integer quesId){
		ModelAndView mav = new ModelAndView("/qas/quesLog");
		UserInfo userInfo = this.getSessionUser();
		//查看问答日志，删除今日提醒
		todayWorksService.updateTodoWorkRead(quesId,userInfo.getComId(),userInfo.getId(), ConstantInterface.TYPE_QUES,0);
		
		//问答日志
		List<QuesLog> quesLogs = qasService.listPagedQuesLog(userInfo.getComId(),quesId);
		mav.addObject("quesLogs",quesLogs);
		mav.addObject("sessionUser",userInfo);
		return mav;
	}
	/**
	 * 问答附件
	 * @param quesId
	 * @return
	 */
	@RequestMapping("/quesFilePage")
	public ModelAndView quesFilePage(Integer quesId){
		ModelAndView mav = new ModelAndView("/qas/qasFiles");
		UserInfo userInfo = this.getSessionUser();
		//查看问答附件，删除今日提醒
		todayWorksService.updateTodoWorkRead(quesId,userInfo.getComId(),userInfo.getId(), ConstantInterface.TYPE_QUES,0);
		
		//问答附件
		List<QuesFile> quesFiles = qasService.listPagedQuesFile(userInfo,quesId);
		mav.addObject("quesFiles",quesFiles);
		mav.addObject("userInfo",userInfo);
		mav.addObject("quesId",quesId);
		return mav;
	}
	
	
	/********************************以下是回答评论***************************************************/
	/**
	 * 评论回答
	 * @param ansId
	 * @param quesId
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/addAnsTalk" , method=RequestMethod.POST)
	public Map<String, Object> addAnsTalk(AnsTalk ansTalk,Integer[] upfilesId) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//企业编号
		ansTalk.setComId(sessionUser.getComId());
		//发言人
		ansTalk.setTalker(sessionUser.getId());
		//评论回答
		Integer id = qasService.addAnsTalk(ansTalk,sessionUser,upfilesId);
		map.put("status", "y");
		map.put("id", id);
		map.put("sessionUser", sessionUser);
		//添加的回复
		ansTalk = qasService.getAnsTalkById(id,sessionUser.getComId());
		//是子节点
		ansTalk.setIsLeaf(1);
		map.put("ansTalk", ansTalk);
		return map;
	}
	/**
	 * 删除评论回答
	 * @param ansTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/delAnsTalk")
	public Map<String, Object> delAnsTalk(AnsTalk ansTalk,String delChildNode) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		ansTalk.setComId(sessionUser.getComId());
		//要删除的回复所有子节点和自己
		List<Integer> childIds = qasService.delAnsTalk(ansTalk,delChildNode,sessionUser);
		map.put("status", "y");
		map.put("childIds", childIds);
		return map;
	}
	/**
	 * 获取个人权限下排列前的N个问题集合数据
	 * @return
	 */
	@RequestMapping("/firstNQasList")
	public ModelAndView firstNQasList() {
		UserInfo userInfo = this.getSessionUser();
		List<Question> qasList = qasService.firstNQasList(userInfo,6);
		ModelAndView mav = new ModelAndView("/qas/firstNQasList", "qasList",qasList);
		return mav;
	}
	
	/**
	 * 删除问答附件
	 * @param quesId
	 * @param quesUpFileId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delQasUpfile")
	public Map<String, Object> delQasUpfile(Integer quesId,Integer quesUpFileId,String type) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		qasService.delQasUpfile(quesUpFileId,type,userInfo,quesId);
		map.put("status", "y");
		return map;
	}
	
}