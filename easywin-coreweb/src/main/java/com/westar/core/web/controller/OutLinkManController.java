package com.westar.core.web.controller;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Area;
import com.westar.base.model.OlmAddress;
import com.westar.base.model.OlmContactWay;
import com.westar.base.model.OlmTalk;
import com.westar.base.model.OutLinkMan;
import com.westar.base.model.OutLinkManRange;
import com.westar.base.model.OlmTalkUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.RequestContextHolderUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.service.OutLinkManService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.web.PaginationContext;


@Controller
@RequestMapping("/outLinkMan")
public class OutLinkManController extends BaseController{

	@Autowired
	OutLinkManService outLinkManService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	/**
	 * 分页查询外部联系人
	 * @param outLinkMan
	 * @return
	 */
	@RequestMapping("/listOutLinkMan")
	public ModelAndView listOutLinkMan(OutLinkMan outLinkMan) {
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		UserInfo user = this.getSessionUser();
		List<OutLinkMan> list = outLinkManService.listPagedOutLinkMan(outLinkMan,user);
		mav.addObject("list", list);
		mav.addObject("userInfo", user);
		return mav;
	}
	
	/**
	 * 添加外部联系人页面
	 * @param outLinkMan
	 * @return
	 */
	@RequestMapping("/addOutLinkManPage")
	public ModelAndView addOutLinkManPage(String isCrm) {
		ModelAndView mav = new ModelAndView("/outLinkMan/addOutLinkMan");
		UserInfo user = this.getSessionUser();
		mav.addObject("userInfo", user);
		mav.addObject("isCrm", isCrm);
		return mav;
	}
	
	/**
	 * 添加外部联系人
	 * @param outLinkMan
	 * @return
	 */
	@RequestMapping("/addOutLinkMan")
	public ModelAndView addOutLinkMan(OutLinkMan outLinkMan,String isCrm){
		UserInfo userInfo = this.getSessionUser();
		outLinkManService.addOutLinkMan(outLinkMan,userInfo);
		if(!CommonUtil.isNull(isCrm) && "1".equals(isCrm)) {
			return new ModelAndView();
		}else {
			ModelAndView view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.SUCCESS, "添加成功!");
			return view;
		}
		
		
	}
	
	/**
	 * 删除外部联系人
	 * @param olmId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delOlm")
	public Map<String,Object>  delOlm(Integer olmId){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		outLinkManService.delOlm(olmId,userInfo);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	/**
	 * 批量删除外部联系人
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delOlms")
	public ModelAndView delOlms(Integer[] ids,String redirectPage) {
		UserInfo userInfo = this.getSessionUser();
		boolean succ = outLinkManService.delOlms(ids,userInfo);
		if (succ) {
			this.setNotification(Notification.SUCCESS, "删除成功");
		} else {
			this.setNotification(Notification.ERROR, "删除失败");
		}
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	/**
	 * 修改外部联系人页面
	 */
	@RequestMapping("/editOlmPage")
	public ModelAndView editOlmPage(OutLinkMan outLinkMan,String isCrm) {
		ModelAndView mav = new ModelAndView("/outLinkMan/editOutLinkMan");
		UserInfo user = this.getSessionUser();
		outLinkMan = outLinkManService.queryOutLinkManById(outLinkMan.getId());
		if(!CommonUtil.isNull(outLinkMan)){
			//查询外部联系人范围
			List<OutLinkManRange> listsRange = outLinkManService.listOutLinkManRanges(outLinkMan.getId(),user);
			outLinkMan.setListRangeUser(listsRange);
			mav.addObject("olm", outLinkMan);
		}
		mav.addObject("userInfo", user);
		mav.addObject("isCrm", isCrm);
		return mav;
	}
	
	/**
	 * 查看外部联系人页面
	 */
	@RequestMapping("/viewOlmPage")
	public ModelAndView viewOlmPage(OutLinkMan outLinkMan,String isCrm) {
		ModelAndView mav = new ModelAndView("/outLinkMan/editOutLinkMan");
		UserInfo user = this.getSessionUser();
		outLinkMan = outLinkManService.queryOutLinkManById(outLinkMan.getId());
		todayWorksService.updateTodoWorkRead(outLinkMan.getId(), user.getComId(), user.getId(), ConstantInterface.TYPE_OUTLINKMAN, 0);
		if(!CommonUtil.isNull(outLinkMan)){
			//查询外部联系人范围
			List<OutLinkManRange> listsRange = outLinkManService.listOutLinkManRanges(outLinkMan.getId(),user);
			outLinkMan.setListRangeUser(listsRange);
			//联系方式
			List<OlmContactWay> olmContactWays = outLinkManService.listOlmContactWay(outLinkMan.getId(),user);
			outLinkMan.setListContactWay(olmContactWays);
			//联系地址
			List<OlmAddress> olmAddresses = outLinkManService.listOlmAddress(outLinkMan.getId(),user);
			outLinkMan.setListAddress(olmAddresses);
			//查看外部联系人页面
			if(!outLinkMan.getCreator().equals(user.getId())) {
				mav = new ModelAndView("/outLinkMan/viewOutLinkMan");
				//判断是否有自己创建的记录
				int countContactWay = 0;
				int countAddress = 0;
				if(!CommonUtil.isNull(olmContactWays)) {
					for (OlmContactWay olmContactWay : olmContactWays) {
						if(olmContactWay.getCreator().equals(user.getId())) {
							countContactWay ++;
							break;
						}
					}
				}
				if(!CommonUtil.isNull(olmAddresses)) {
					for (OlmAddress olmAddress: olmAddresses) {
						if(olmAddress.getCreator().equals(user.getId())) {
							countAddress ++;
							break;
						}
					}
				}
				if(countContactWay == 0) {
					mav.addObject("needAddContactWay", "y");
				}
				if(countAddress == 0) {
					mav.addObject("needAddress", "y");
				}
			}
			mav.addObject("olm", outLinkMan);
			Integer talkCount = outLinkManService.countTalks(outLinkMan.getId(),user);
			mav.addObject("talkCount", talkCount);
		}
		mav.addObject("userInfo", user);
		mav.addObject("isCrm", isCrm);
		return mav;
	}
	
	
	/**
	 * 修改外部联系人
	 * @param outLinkMan
	 * @return
	 */
	@RequestMapping("/updateOlm")
	public ModelAndView updateOlm(OutLinkMan outLinkMan,String isCrm){
		UserInfo userInfo = this.getSessionUser();
		outLinkManService.updateOlm(outLinkMan,userInfo);
		if(!CommonUtil.isNull(isCrm) && "1".equals(isCrm)) {
			return null;
		}else {
			ModelAndView view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.SUCCESS, "修改成功!");
			return view;
		}
	}
	
	/**
	 * 修改外部联系人单个字段
	 * @param outLinkMan
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxUpdateOlm")
	public String ajaxUpdateOlm(OutLinkMan outLinkMan){
		UserInfo userInfo = this.getSessionUser();
		boolean succ = outLinkManService.updateOlmByOne(outLinkMan,userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
		
	}
	
	/**
	 * 修改外部联系人分享范围
	 * @param outLinkManId
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxUpdateOlmRange")
	public String ajaxUpdateOlmRange(Integer outLinkManId, Integer[] userIds){
		UserInfo userInfo = this.getSessionUser();
		boolean succ = outLinkManService.updateOlmRange(outLinkManId,userIds,userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
		
	}
	
	/**
	 * 删除外部联系人分享人员
	 * @param outLinkManId
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delOlmRange")
	public String delOlmRange(Integer outLinkManId, Integer userId){
		UserInfo userInfo = this.getSessionUser();
		boolean succ = outLinkManService.delOlmRange(outLinkManId,userId,userInfo);
		if (succ) {
			return "删除成功";
		} else {
			return "删除失败";
		}
		
	}
	
	
	/**
	 * 客户页面选择外部联系人
	 * @param outLinkMan
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListForSelect")
	public Map<String, Object> ajaxListForSelect(OutLinkMan outLinkMan, Integer pageNum, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
				
		PageBean<OutLinkMan> pageBean = outLinkManService.ajaxListForSelect(outLinkMan, userInfo);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 修改联系方式
	 * @param olmContactWay
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateContactWay")
	public String updateContactWay(OlmContactWay olmContactWay){
		UserInfo userInfo = this.getSessionUser();
		boolean succ = outLinkManService.updateContactWay(olmContactWay,userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
		
	}
	
	/**
	 * 修改联系地址
	 * @param olmAddress
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAddress")
	public String updateAddress(OlmAddress olmAddress){
		UserInfo userInfo = this.getSessionUser();
		boolean succ = outLinkManService.updateAddress(olmAddress,userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
		
	}
	
	/**
	 * 添加联系方式
	 * @param olmContactWay
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addContactWay")
	public Map<String, Object> addContactWay(OlmContactWay olmContactWay) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Integer id = outLinkManService.addContactWay(olmContactWay,userInfo);
		olmContactWay.setId(id);
		if(!CommonUtil.isNull(olmContactWay)) {
			map.put("status", "t");
			map.put("olmContactWay", olmContactWay);
			map.put("msg", "添加成功");
		}else {
			map.put("status", "f");
			map.put("msg", "添加失败");
		}
		
		return map;
	}
	
	/**
	 * 添加联系地址
	 * @param olmAddress
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addAddress")
	public Map<String, Object> addAddress(OlmAddress olmAddress) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Integer id = outLinkManService.addAddress(olmAddress,userInfo);
		olmAddress.setId(id);
		if(!CommonUtil.isNull(olmAddress)) {
			map.put("status", "t");
			map.put("olmAddress", olmAddress);
			map.put("msg", "添加成功");
		}else {
			map.put("status", "f");
			map.put("msg", "添加失败");
		}
		
		return map;
	}
	
	/**
	 * 删除联系地址
	 * @param olmAddress
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delAddress")
	public String delAddress(OlmAddress olmAddress){
		UserInfo userInfo = this.getSessionUser();
		boolean succ = outLinkManService.delAddress(olmAddress,userInfo);
		if (succ) {
			return "删除成功";
		} else {
			return "删除失败";
		}
		
	}
	
	/**
	 * 删除联系方式
	 * @param olmContactWay
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delContactWay")
	public String delContactWay(OlmContactWay olmContactWay){
		UserInfo userInfo = this.getSessionUser();
		boolean succ = outLinkManService.delContactWay(olmContactWay,userInfo);
		if (succ) {
			return "删除成功";
		} else {
			return "删除失败";
		}
		
	}
	
	/**
	 * 客户页面选择外部联系人
	 * @param outLinkMan
	 * @return
	 */
	@RequestMapping("/listForCustomer")
	public ModelAndView listForCustomer(OutLinkMan outLinkMan) {
		ModelAndView mav = new ModelAndView("/outLinkMan/listForCustomer");
		UserInfo user = this.getSessionUser();
		List<OutLinkMan> list = outLinkManService.listPagedOutLinkMan(outLinkMan,user);
		mav.addObject("list", list);
		mav.addObject("userInfo", user);
		return mav;
	}
	
	/**
	 * 外部联系人留言
	 * @param olmTalk
	 * @return
	 */
	@RequestMapping(value="/olmTalkPage")
	public ModelAndView olmTalkPage(OlmTalk olmTalk){
		ModelAndView view = new ModelAndView("/outLinkMan/olmTalk");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		List<OlmTalk> listOlmTalk = outLinkManService.listPagedOlmTalk(olmTalk.getOutLinkManId(), userInfo.getComId());
		view.addObject("listOlmTalk",listOlmTalk);
		view.addObject("olmTalk",olmTalk);
		return view;
	}
	
	/**
	 * ajax添加外部联系人留言
	 * @param olmTalk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddOlmTalk")
	public OlmTalk ajaxAddOlmTalk(OlmTalk olmTalk) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		olmTalk.setComId(userInfo.getComId());
		olmTalk.setSpeaker(userInfo.getId());
		Integer id = outLinkManService.addOlmTalk(olmTalk,userInfo);
		olmTalk = outLinkManService.queryOlmTalk(id, userInfo.getComId());
		//自己添加的，当前自己肯定是叶子
		olmTalk.setIsLeaf(1);
		String olmTalkDivString = replyTalkDivString(olmTalk,"listUpfiles_"+olmTalk.getId()+".upfileId","filename","otherAttrIframe",userInfo.getComId());
		olmTalk.setOlmTalkDivString(olmTalkDivString);
		//模块日志添加
		outLinkManService.addOlmLog(userInfo.getComId(),olmTalk.getOutLinkManId(), userInfo.getId(),"添加留言");
		return olmTalk;
	}
	/**
	 * 回复留言
	 * @param olmTalk
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/replyTalk")
	public OlmTalk replyTalk(OlmTalk olmTalk) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			return null;
		}
		olmTalk.setComId(userInfo.getComId());
		olmTalk.setSpeaker(userInfo.getId());
		Integer id = outLinkManService.addOlmTalk(olmTalk,userInfo);
		olmTalk = outLinkManService.queryOlmTalk(id, userInfo.getComId());
		//自己添加的，当前自己肯定是叶子
		olmTalk.setIsLeaf(1);
		String olmTalkDivString = replyTalkDivString(olmTalk,"listUpfiles_"+olmTalk.getId()+".upfileId","filename","otherAttrIframe",userInfo.getComId());
		olmTalk.setOlmTalkDivString(olmTalkDivString);
		//模块日志添加
		outLinkManService.addOlmLog(userInfo.getComId(),olmTalk.getOutLinkManId(), userInfo.getId(), "添加留言");
		return olmTalk;
	}
	/**
	 * 讨论回复DIV字符串生存
	 * @param olmTalk
	 * @param uploadFileName
	 * @param uploadFileShowName
	 * @param pifreamId
	 * @param comId
	 * @return
	 */
	private String replyTalkDivString(OlmTalk olmTalk,String uploadFileName,String uploadFileShowName,String pifreamId,Integer comId){

		if(null == olmTalk){
			return null;
		}
		//是子节点
		olmTalk.setIsLeaf(1);
		String sid = RequestContextHolderUtil.getRequest().getParameter("sid");
		UserInfo userInfo = this.getSessionUser();
		StringBuffer divString = new StringBuffer();
		
		if(olmTalk.getParentId().equals(-1)){//是留言
			divString.append("\n <div id='talk_"+olmTalk.getId()+"' class='ws-shareBox'>");
		}else{//是回复
			divString.append("\n <div id='talk_"+olmTalk.getId()+"' class='ws-shareBox ws-shareBox2'>");
		}
		//头像
		divString.append("\n <div class='shareHead' data-container='body' data-toggle='popover'data-user='"+userInfo.getId()+"' data-busId='"+olmTalk.getOutLinkManId()+"' data-busType='"+ConstantInterface.TYPE_OUTLINKMAN+"'>");
		
		if(null==olmTalk.getUuid() || "".equals(olmTalk.getUuid())){
			divString.append("<img src=\"/static/headImg/2"+((null==olmTalk.getGender() || "".equals(olmTalk.getGender()))?2:olmTalk.getGender())+".jpg\" title=\""+olmTalk.getSpeakerName()+"\"></img>");
		}else{
			divString.append("<img src=\"/downLoad/down/"+olmTalk.getUuid()+"/"+olmTalk.getFilename()+"?sid="+sid+"\" title=\""+olmTalk.getSpeakerName()+"\"></img>");
		}
		divString.append("\n </div>");
		//头像结束
		
		divString.append("\n <div class='shareText'>");
		divString.append("\n 	<span class='ws-blue'>"+olmTalk.getSpeakerName()+"</span>");
		if(!olmTalk.getParentId().equals(-1)){//是留言
			divString.append("\n<r>回复</r>");
			divString.append("\n<span class='ws-blue'>"+olmTalk.getpSpeakerName()+"</span>");
			
		}
		divString.append("\n<p class='ws-texts'>"+StringUtil.toHtml(olmTalk.getContent())+"</p>");
		
		//附件
		List<OlmTalkUpfile> list = olmTalk.getListOlmTalkFile();
		if(null!=list && list.size()>0){
			divString.append("<div class=\"file_div\">");
			for (int i=0;i<list.size();i++) {
				OlmTalkUpfile upfiles = list.get(i);
				if("1".equals(upfiles.getIsPic())){
					divString.append("<p class=\"p_text\">");
					divString.append("附件（"+(i+1)+"）：");
					divString.append("<img onload=\"AutoResizeImage(350,0,this,'otherAttrIframe')\"");
					divString.append("src=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid="+this.getSid()+"\" />");
					divString.append("&nbsp;<a class=\"fa fa-download\" title=\"下载\" href=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid="+this.getSid()+"\"></a>");
					divString.append("&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"showPic('/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"','"+this.getSid()+"','"+upfiles.getUpfileId()+"','"+ConstantInterface.TYPE_OUTLINKMAN+"','"+olmTalk.getOutLinkManId()+"')\"></a>");
					divString.append("</p>");
				}else{
					divString.append("<p class=\"p_text\">");
					divString.append("附件（"+(i+1)+"）：");
					divString.append(upfiles.getFilename());
					if(upfiles.getFileExt().equals("doc")||
						upfiles.getFileExt().equals("docx")||
						upfiles.getFileExt().equals("xls")||
						upfiles.getFileExt().equals("xlsx")||
						upfiles.getFileExt().equals("ppt")||
						upfiles.getFileExt().equals("pptx")){
						divString.append("&nbsp;<a class=\"fa fa-download\" href=\"javascript:void(0);\" title=\"下载\" onclick=\"downLoad('"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+this.getSid()+"')\"></a>");
						divString.append("&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"viewOfficePage('"+upfiles.getUpfileId()+"','"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+upfiles.getFileExt()+"','"+this.getSid()+"','"+ConstantInterface.TYPE_OUTLINKMAN+"','"+olmTalk.getOutLinkManId()+"')\"></a>");
					}else if(upfiles.getFileExt().equals("pdf")||upfiles.getFileExt().equals("txt")){
						divString.append("&nbsp;<a class=\"fa fa-download\" title=\"下载\" href=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid="+this.getSid()+"\"></a>");
						divString.append("&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"viewOfficePage('"+upfiles.getUpfileId()+"','"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+upfiles.getFileExt()+"','"+this.getSid()+"','"+ConstantInterface.TYPE_OUTLINKMAN+"','"+olmTalk.getOutLinkManId()+"')\"></a>");
					}else{
						divString.append("&nbsp;<a class=\"fa fa-download\" href=\"javascript:void(0);\" title=\"下载\" onclick=\"downLoad('"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+this.getSid()+"')\"></a>");
					}
				}
				divString.append("</p>");
			}
			divString.append("</div>");
		}
		divString.append("\n 	<div class='ws-type'>");
		//发言人可以删除自己的发言
		if(userInfo.getId().equals(olmTalk.getSpeaker())){
			divString.append("\n 	<a href='javascript:void(0);' id=\"delOpt_"+olmTalk.getId()+"\" class='fa fa-trash-o' title='删除' onclick=\"delTalk('"+olmTalk.getId()+"','1')\"></a>");
		}
		divString.append("\n 	<a id=\"img_"+olmTalk.getId()+"\" name=\"replyImg\" href=\"javascript:void(0);\" class=\"fa fa-comment-o\" title=\"回复\" onclick=\"showArea('"+olmTalk.getId()+"')\"></a>");
		divString.append("\n 		<time>"+olmTalk.getRecordCreateTime()+"</time>");
		divString.append("\n 		</div>");
		divString.append("\n 	</div>");
		divString.append("\n 	<div class=\"ws-clear\"></div>");
		divString.append("\n </div>");
		//回复层
		divString.append("\n <div id=\"reply_"+olmTalk.getId()+"\" name=\"replyTalk\" style=\"display:none;\" class=\"ws-shareBox ws-shareBox2 ws-shareBox3\">");
		divString.append("\n 	<div class=\"shareText\">");
		divString.append("\n 		<div class=\"ws-textareaBox\" style=\"margin-top:10px;\">");
		divString.append("\n 			<textarea id=\"operaterReplyTextarea_"+olmTalk.getId()+"\" name=\"operaterReplyTextarea_"+olmTalk.getId()+"\" class=\"form-control\" placeholder=\"回复……\"></textarea>");
		divString.append("\n 			<div class=\"ws-otherBox\">");
		divString.append("\n 				<div class=\"ws-meh\">");
		//表情
		divString.append("\n 					<a href=\"javascript:void(0);\" class=\"fa fa-meh-o tigger\" id=\"biaoQingSwitch_"+olmTalk.getId()+"\" onclick=\"addBiaoQingObj('biaoQingSwitch_"+olmTalk.getId()+"','biaoQingDiv_"+olmTalk.getId()+"','operaterReplyTextarea_"+olmTalk.getId()+"');\"></a>");
		//表情DIV层
		divString.append("\n 					<div id=\"biaoQingDiv_"+olmTalk.getId()+"\" class=\"blk\" style=\"display:none;position:absolute;width:200px;top:100px;z-index:99;left: 15px\">");
		divString.append("\n 						<div class=\"main\">");
		divString.append("\n 							<ul style=\"padding: 0px\">");
		divString.append(CommonUtil.biaoQingStr());	
		divString.append("\n 							</ul>");
		divString.append("\n 						</div>");
		divString.append("\n 					</div>");
		divString.append("\n 				</div>");
		//常用意见
		divString.append("\n 				<div class=\"ws-plugs\">");
		divString.append("\n 					<a href=\"javascript:void(0);\" class=\"fa fa-comments-o\" onclick=\"addIdea('operaterReplyTextarea_"+olmTalk.getId()+"','"+sid+"');\" title=\"常用意见\"></a>");
		divString.append("\n 				</div>");
		//@机制
		divString.append("\n				<div class=\"ws-plugs\">");
		divString.append("\n					<a class=\"btn-icon\" title=\"告知人员\"  href=\"javascript:void(0)\" data-todoUser=\"yes\" data-relateDiv=\"todoUserDiv_" +olmTalk.getId()+ "\">@</a>");
		divString.append("\n				</div>");
		//分享按钮
		divString.append("\n				<div class=\"ws-share\">");
		divString.append("\n					<button type=\"button\" class=\"btn btn-info ws-btnBlue\" data-relateTodoDiv=\"todoUserDiv_" + olmTalk.getId() + "\" onclick=\"replyTalk(" + olmTalk.getOutLinkManId() + "," + olmTalk.getId()+",this)\">回复</button>");
		divString.append("\n				</div>");
		divString.append("\n				<div style=\"clear: both;\"></div>");
		//@机制
		divString.append("\n				<div id=\"todoUserDiv_" + olmTalk.getId() + "\" class=\"padding-top-10\"> ");
		divString.append("\n        		</div>");
		divString.append("\n				<div class=\"ws-notice\">");
		divString.append("\n 			<div class=\"ws-notice\">");
		//构建附件上传控件
		divString.append(CommonUtil.uploadFileTagStr(uploadFileName,uploadFileShowName,pifreamId,userInfo.getComId(),sid));
		divString.append("\n 			</div>");
		divString.append("\n 		</div>");
		divString.append("\n 	</div>");
		divString.append("\n </div>");
		divString.append("\n</div>");
		return divString.toString();
	}
	/**
	 * 删除外部联系人留言
	 * @param olmTalk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/ajaxDelOlmTalk")
	public String ajaxDelOlmTalk(OlmTalk olmTalk) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		olmTalk.setComId(userInfo.getComId());
		outLinkManService.delOlmTalk(olmTalk,userInfo);
		//模块日志添加
		outLinkManService.addOlmLog(userInfo.getComId(),olmTalk.getOutLinkManId(), userInfo.getId(), "删除留言");
		return "删除成功！";
	}
	
	/**
	 * 删除外部联系人留言和外部联系人附件
	 * @param outLinkManId 外部联系人
	 * @param outLinkManFileId 附件关联主键
	 * @param type 类型 outLinkMan talk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/delOlmUpfile")
	public Map<String, Object> delOlmUpfile(Integer outLinkManId,Integer outLinkManFileId,String type) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		outLinkManService.delOutLinkManUpfile(outLinkManFileId,type,userInfo,outLinkManId);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 查询外部联系人用于客户选择后添加到客户页面
	 * @author hcj 
	 * @param id
	 * @return 
	 * @date 2018年8月25日 下午4:28:17
	 */
	@ResponseBody
	@RequestMapping(value = "/queryOlmForShow", method = RequestMethod.POST)
	public Map<String, Object> queryOlmForShow(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
		} else {
			OutLinkMan outLinkMan = outLinkManService.queryOutLinkManById(id);
			List<OutLinkMan> lists = new ArrayList<>();
			if(outLinkMan != null) {
				//查询联系方式
				List<OlmContactWay> contactWays = outLinkManService.listContactWayForShow(id,userInfo);
				if(!CommonUtil.isNull(contactWays)) {
					for (OlmContactWay contactWay : contactWays) {
						OutLinkMan result = new OutLinkMan();
						BeanUtils.copyProperties(outLinkMan,result);
						result.setContactWay(contactWay.getContactWay());
						result.setContactWayValue(contactWay.getContactWayValue());
						lists.add(result);
					}
				}else {
					OutLinkMan result = new OutLinkMan();
					BeanUtils.copyProperties(outLinkMan,result);
					lists.add(result);
				}
				map.put("status", "y");
				map.put("lists",lists);
			}
			
		}
		return map;
	}
	
}