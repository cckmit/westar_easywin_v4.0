package com.westar.core.web.controller;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.AttenceRule;
import com.westar.base.model.ForMeDo;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.JoinRecord;
import com.westar.base.model.MsgShare;
import com.westar.base.model.Organic;
import com.westar.base.model.PassYzm;
import com.westar.base.model.SystemLog;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.UserOrganic;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.Notification;
import com.westar.base.util.BeanUtilEx;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.CusAccessObjectUtil;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.DifPic;
import com.westar.base.util.Encodes;
import com.westar.base.util.ExcelExportUtil;
import com.westar.base.util.FileMD5Util;
import com.westar.base.util.FileUtil;
import com.westar.base.util.MathExtend;
import com.westar.base.util.MessageSender;
import com.westar.base.util.PinyinToolkit;
import com.westar.base.util.PublicConfig;
import com.westar.base.util.StringUtil;
import com.westar.base.util.UUIDGenerator;
import com.westar.core.service.AttenceService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.JiFenService;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.OrganicService;
import com.westar.core.service.PhoneMsgService;
import com.westar.core.service.RegistService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TaskService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UploadService;
import com.westar.core.service.UserInfoService;
import com.westar.core.thread.MailSendThread;
import com.westar.core.web.FreshManager;
import com.westar.core.web.SessionContext;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController {

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	JiFenService jifenService;
	
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	OrganicService organicService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	RegistService registService;
	
	@Autowired
	PhoneMsgService phoneMsgService;
	
	@Autowired
	AttenceService attenceService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	/**
	 * 查询用户分页列表 
	 * @param userInfoT
	 * @return
	 */
	@RequestMapping("/listPagedUserInfo")
	public ModelAndView listPagedUserInfo(UserInfo userInfoT,HttpServletRequest request) {
		UserInfo userInfo = this.getSessionUser();
		
		userInfoT.setComId(userInfo.getComId());
		List<UserInfo> list = userInfoService.listPagedUserInfo(userInfoT);
		ModelAndView mav = new ModelAndView("/organic/organicCenter", "list", list);
		
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ATTENCE);
		mav.addObject("isForceIn", isForceIn);
		mav.addObject("userInfo", userInfo);
		mav.addObject("userInfoT", userInfoT);
		return mav;
	}

	/**
	 * 跳转到密码修改页面
	 * @return
	 */
	@RequestMapping("/updatePasswordPage")
	public ModelAndView updatePasswordPage() {
		UserInfo userInfo = this.getSessionUser();
		return new ModelAndView("/userInfo/updatePassword", "userInfo", userInfo);
	}

	/**
	 * 验证账号唯一性
	 * @param param 登录名
	 * @param name 暂时无用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/validateLoginName")
	public Map<String, Object> validateLoginName(String param, String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean validateResult = userInfoService.validateLoginName(param);
		if (validateResult) {
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		} else {
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "该登录名已经存在！");
		}
		return map;
	}
	/**
	 * 新增用户  新增数据
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/addUserInfo", method = RequestMethod.POST)
	public ModelAndView addUserInfo(UserInfo userInfo, String redirectPage) {
		UserInfo sessionUser = this.getSessionUser();
		// 密码MD5加密
		String password = userInfo.getPassword();
		String passwordMD5 = DigestUtils.md5Hex(password);
		userInfo.setPassword(passwordMD5);

		userInfoService.addUserInfo(userInfo);
		//添加日志记录 
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "新增用户\""+userInfo.getUserName()+"\"",
				ConstantInterface.TYPE_USER,userInfo.getComId(),sessionUser.getOptIP());
		this.setNotification(Notification.SUCCESS, "添加成功!");
		return new ModelAndView("redirect:" + redirectPage);
	}

	/**
	 * 禁用用户  禁用删除
	 * @param ids 要禁用的人员的主键ID
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/disableUserInfo")
	public ModelAndView disableUserInfo(Integer[] ids, String redirectPage) {
		UserInfo sessionUser = this.getSessionUser();
		if(sessionUser.getAdmin().equals("0")){//没有管理权限的
			this.setNotification(Notification.ERROR, "没有管理权限!");
		}else{
			//批量禁用用户
			userInfoService.disableUserInfo(ids,sessionUser.getComId(),sessionUser);
			this.setNotification(Notification.SUCCESS, "禁用成功!");
		}
		return new ModelAndView("redirect:" + redirectPage);
	}
	/**
	 * 启用用户  
	 * @param ids 要启用的人员的主键ID
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/enableUserInfo")
	public ModelAndView enableUserInfo(Integer[] ids, String redirectPage) {
		UserInfo sessionUser = this.getSessionUser();
		if(sessionUser.getAdmin().equals("0")){//没有管理权限的
			this.setNotification(Notification.ERROR, "没有管理权限!");
		}else{
			Organic organic = userInfoService.checkOrgUsersFreeSpace(ids.length,sessionUser.getComId());//检查团队使用空间情况
			if(!organic.isCanDo()){
				this.setNotification(Notification.ERROR,organic.getMsg());
			}else{
				userInfoService.enableUserInfo(ids,sessionUser.getComId(),sessionUser);
				this.setNotification(Notification.SUCCESS, "启用成功!");
			}
		}
		return new ModelAndView("redirect:" + redirectPage);
	}
	/**
	 * 用户授权 
	 * @param ids 要授权的人员的主键ID
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/updateGrant")
	public ModelAndView updateGrant(Integer[] ids, String redirectPage) {
		UserInfo sessionUser = this.getSessionUser();
		//用户授权 
		List<UserOrganic> list =  userInfoService.updateGrant(ids,sessionUser.getComId());
		if(null!=list && list.size()>0){
			for (UserOrganic userOrganic : list) {
				//授权管理员的权限
				SessionContext.grantAndRevokeUser(userOrganic.getUserId(),sessionUser.getComId(),true);
			}
			//添加日志记录 
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量授权用户",
					ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		}
		this.setNotification(Notification.SUCCESS, "授权成功!");
		return new ModelAndView("redirect:" + redirectPage);
	}
	/**
	 * 用户权限回收
	 * @param ids 要回收权限的人员的主键ID
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/updateRevoke")
	public ModelAndView updateRevoke(Integer[] ids, String redirectPage) {
		UserInfo sessionUser = this.getSessionUser();
		//用户权限回收
		List<UserOrganic> list = userInfoService.updateRevoke(ids,sessionUser.getComId());
		if(null!=list && list.size()>0){
			for (UserOrganic userOrganic : list) {
				//回收管理员的权限
				SessionContext.grantAndRevokeUser(userOrganic.getUserId(),sessionUser.getComId(),false);
			}
			//添加日志记录 
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量回收权限",
					ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		}
		this.setNotification(Notification.SUCCESS, "回收权限成功!");
		return new ModelAndView("redirect:" + redirectPage);
	}
	
	/**
	 * 启用或是禁用成员
	 * @param id 关系主键
	 * @param enabled 人员原来状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateEnabled")
	public Map<String, Object> updateEnabled(Integer id,String enabled) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "session已失效");
			return map;
		}
		
		if(ConstantInterface.ENABLED_YES.toString().equals(enabled)){//原先是启用的需要禁用
			userInfoService.disableUserInfo(new Integer[]{id},sessionUser.getComId(),sessionUser);//禁用用户
			map.put(ConstantInterface.TYPE_INFO, "禁用成功");
		}else if(ConstantInterface.ENABLED_NO.toString().equals(enabled)){
			userInfoService.enableUserInfo(new Integer[]{id},sessionUser.getComId(),sessionUser);//启用用户
			map.put(ConstantInterface.TYPE_INFO, "启用成功！");
		}
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	/**
	 * 更新团队人员的服务状态
	 * @param userOrgId 人员团队关系主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserInService")
	public Map<String, Object> updateUserInService(Integer userOrgId) {
		UserInfo curUser = this.getSessionUser();
		Map<String, Object> resultMap = userInfoService.updateUserInService(userOrgId,curUser);
		this.setNotification(Notification.SUCCESS, "操作成功，用户已激活！");
		return resultMap;
	}


	/**
	 * 查询用户详细信息
	 * @param id 要查询的人员的主键ID
	 * @return
	 */
	@RequestMapping("/viewUserInfo")
	public ModelAndView viewUserInfo(Integer id) {
		UserInfo userInfo = this.getSessionUser();
		UserInfo userInfoT = userInfoService.getUserInfo(userInfo.getComId(),id);
		ModelAndView mav = new ModelAndView("/userInfo/viewUserInfo", "userInfoT", userInfoT);
		mav.addObject("userInfo", userInfo);
		return mav;
	}

	/**
	 * 获取被选择用户信息
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectedUsersInfo")
	public List<UserInfo> selectedUsersInfo(final Integer[] userIds){
		if(null==userIds || userIds.length==0){
			return null;
		}
		List<UserInfo> list = new ArrayList<UserInfo>();
		//sesison中的操作员信息
		UserInfo sessionUser  = this.getSessionUser();
		//企业号
		final Integer comId = sessionUser.getComId();
		//操作人员主键
		final Integer userId = sessionUser.getId();
		for(Integer id : userIds){
			UserInfo userInfo = userInfoService.getUserInfo(comId,id);
			list.add(userInfo);
		}
		//保存最近选择的人员
		new Thread(new Runnable() {
			@Override
			public void run() {
				userInfoService.addUsedUser(comId,userId,userIds);
				
			}
		}).start();
		return list;
	} 
	/**
	 * 获取被选择用户信息
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addUsedUser")
	public HttpResult<String> addUsedUser(final Integer[] userIds){
		if(null==userIds || userIds.length==0){
			return new HttpResult<String>().ok("");
		}
		//sesison中的操作员信息
		UserInfo sessionUser  = this.getSessionUser();
		if(null == sessionUser){
			return new HttpResult<String>().error(CommonConstant.OFF_LINE_INFO);
		}
		//企业号
		final Integer comId = sessionUser.getComId();
		//操作人员主键
		final Integer userId = sessionUser.getId();
		//保存最近选择的人员
		new Thread(new Runnable() {
			@Override
			public void run() {
				userInfoService.addUsedUser(comId,userId,userIds);
				
			}
		}).start();
		return new HttpResult<String>().ok(sessionUser.getComId().toString());
	} 
	/**
	 * 用户信息修改页面
	 * @param id 要查询的人员的主键ID
	 * @return
	 */
	@RequestMapping("/updateUserInfoPage")
	public ModelAndView updateUserInfoPage(Integer id) {
		UserInfo userInfo = userInfoService.getUserInfo(this.getSessionUser().getComId(),id);
		ModelAndView mav = new ModelAndView("/userInfo/updateUserInfo");
		mav.addObject("userInfo", userInfo);
		return mav;
	}

	/**
	 * 修改用户  更新修改
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
	public ModelAndView updateUserInfo(UserInfo userInfo, String redirectPage) {
		userInfoService.updateUserInfo(userInfo);
		UserInfo sessionUser = this.getSessionUser();
		//添加日志记录 
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "修改用户 \""+userInfo.getUserName()+"\"的信息",
				ConstantInterface.TYPE_USER,userInfo.getComId(),sessionUser.getOptIP());
		this.setNotification(Notification.SUCCESS, "修改成功!");
		return new ModelAndView("redirect:" + redirectPage);
	}
	/**
	 * 修改头像
	 * @param id 用户主键
	 * @param largeImgPath 大头像原始地址
	 * @param orgFilePath 原图像地址
	 * @param orgFileName 原图像名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editPic")
	public Map<String,Object> editPic(Integer id,String largeImgPath,String orgFilePath,String orgFileName) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		try {
			//用于临时存放头像
			String basepath = FileUtil.getRootPath();
			File f = new File(basepath+"/"+largeImgPath);
			if (!f.exists()) {//大图像不存在
				//开始处理
				File originalImage = new File(basepath+orgFilePath);
				if(!originalImage.exists()){//原图像不存在
					map.put(ConstantInterface.TYPE_STATUS, "n");
					return map;
				}
				//大图片
				DifPic.resize(originalImage, new File(basepath+largeImgPath),210, 1f);
			}
			//每个人创建一个文件夹    /static/temp/headImg/公司主键/用户主键
			String path ="/static"+ "/temp/" + "headImg"+"/"+sessionUser.getComId()+"/" +id;
			f = new File(basepath + path);
			if (!f.exists()) {
				f.mkdirs();
			}
			//大图
			Upfiles upfileLarge = this.getUpfile(basepath+largeImgPath,sessionUser.getComId(),id);
			//临时用户头像信息
			UserInfo tempUser = userInfoService.getUserHeadImg(sessionUser.getComId(),id); 
			if(null!=upfileLarge){
				//修改头像
				userInfoService.updateUserOrganicImg(sessionUser.getComId(),id,
						upfileLarge.getId(), upfileLarge.getId() ,upfileLarge.getId());
				//积分
				jifenService.addJifen(sessionUser.getComId(),id, ConstantInterface.TYPE_USERHEAD,"个人资料，完善头像设置",0);
			}
			
			UserInfo user = userInfoService.getUserInfo(sessionUser.getComId(),id); 
			//设置下级个数
			user.setCountSub(userInfoService.countSubUser(id, sessionUser.getComId()));
			
			if(null!=tempUser){
				//删除数据库大头像
				if(null!=tempUser.getBigHeadPortrait()){
					uploadService.delFile(Integer.parseInt(tempUser.getBigHeadPortrait()), basepath, tempUser);
				}
			}
			
			this.updateSessionUser(user);
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put("sessionUser", user);
			
			map.put("largeImgPath",largeImgPath);
			map.put("bigImgUuid", upfileLarge.getUuid());
			map.put("bigImgName", upfileLarge.getFilename());
			
			//系统日志
			systemLogService.addSystemLog(user.getId(), user.getUserName(), "设置头像", 
					ConstantInterface.TYPE_USER, user.getComId(),user.getOptIP());
			
			//删除临时文件
			File dirFile = new File(basepath+path);
			if(dirFile.exists()){
				FileUtils.deleteDirectory(dirFile);
			}
			
		} catch (Exception e) {
		}
		return map;
	}
	/**
	 * 
	 * @param originalImage
	 * @param comId
	 * @param userId
	 * @return
	 */
	private Upfiles getUpfile(String originalImage, Integer comId,
			Integer userId) {
		Upfiles upfiles = null;
		FileInputStream fis = null;
		try {
			// 后缀
			String fileExt = FileUtil.getExtend(originalImage);
			
			String basepath = FileUtil.getUploadBasePath();
			/* 所有附件都保存到uploads 不存在则新增文件夹 */
			File f = new File(basepath);
			if (!f.exists()) {
				f.mkdir();
			}
			String path = "/" + "uploads"+"/"+comId;
			f = new File(basepath + path);
			if (!f.exists()) {
				f.mkdirs();
			}
			/* 每年一个文件夹 */
			path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
			f = new File(basepath + path);
			if (!f.exists()) {
				f.mkdir();
			}
			/* 每月一个文件夹 */
			path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM);
			f = new File(basepath + path);
			if (!f.exists()) {
				f.mkdir();
			}
			/* 每天一个文件夹 */
			path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			f = new File(basepath + path);
			if (!f.exists()) {
				f.mkdir();
			}
			
			String  uuid = UUIDGenerator.getUUID();
			//存放于数据库的型号图片
			String imgPath =basepath + path + "/" + uuid + "." + fileExt.toLowerCase();
			
			//存库的数据文件
			File imgPathFile = new File(imgPath);
			//源文件
			File originalImageFile = new File(originalImage);
			//存库的数据文件流
			FileOutputStream destTempfos = new FileOutputStream(imgPathFile);
			//复制文件
			FileUtils.copyFile(originalImageFile, destTempfos);
			//关闭数据流
			destTempfos.close();
			
			//像素缩小后的文件
			File newFile = new File(imgPath);
			//文件大小
			fis = new FileInputStream(newFile) ; 
			int newFileSize = fis.available();
			// 附件信息存库
			upfiles = new Upfiles();
			upfiles.setUuid(uuid);
			upfiles.setFilename(new File(originalImage).getName());
			upfiles.setFilepath(path + "/" + uuid + "." + fileExt.toLowerCase());
			upfiles.setFileExt(fileExt.toLowerCase());
			upfiles.setSizeb(newFileSize);
			String sizeM = MathExtend.divide(String.valueOf(upfiles
					.getSizeb()), String.valueOf(1024), 2);
			String dw = "K";
			if (Float.parseFloat(sizeM) > 1024) {
				sizeM = MathExtend.divide(sizeM, String.valueOf(1024), 2);
				dw = "M";
				if (Float.parseFloat(sizeM) > 1024) {
					sizeM = MathExtend.divide(sizeM, String.valueOf(1024),
							2);
					dw = "G";
				}
			}
			upfiles.setSizem(sizeM + dw);
			upfiles.setMd5(FileMD5Util.getHash(imgPath));
			upfiles.setComId(comId);
			Integer id = uploadService.addFile(upfiles);
			upfiles.setId(id);
			
		} catch (Exception e) {
		}finally{
			if(null!=fis){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return upfiles;
	}
	
	/**
	 * 查看关于用户相关信息
	 * @return
	 * @throws java.text.ParseException 
	 */
	@RequestMapping(value="/selfCenter")
	public ModelAndView aboutUserInfo(HttpServletRequest request,String[] modTypes,MsgShare msgShare) throws java.text.ParseException{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/userInfo/selfCenter");
		
		UserInfo sessionUser = this.getSessionUser();
		
		UserInfo userInfo =userInfoService.getUserInfo(sessionUser.getComId(), sessionUser.getId());
		//设置下级个数
		userInfo.setCountSub(userInfoService.countSubUser( sessionUser.getId(), sessionUser.getComId()));
		
		this.updateSessionUser(userInfo);
		
		msgShare.setComId(userInfo.getComId());
		//是否查询下属
		String creatorType = msgShare.getCreatorType();
		//若是没有下属
	    if(userInfo.getCountSub()<=0 && null!=creatorType && "1".equals(creatorType)){
	    	msgShare.setCreatorType(null);
	    }
	    //数组集合化
  		List<String>  modPage = new  ArrayList<String>();
  		List<String> modList = null;
  		if(null!=modTypes && modTypes.length>0){
  			modList = Arrays.asList(modTypes);
  			modPage.addAll(modList);
			if(modPage.indexOf("100")>=0){
				modPage.remove("100");
				modPage.add("1");
			}
			
			Collections.sort(modPage);
  		}
  		view.addObject("userInfo", userInfo);
//		//个人分享信息集合
//		List<MsgShare> list = msgShareService.ListMsgShare(userInfo.getId(),10,0,msgShare,modPage);
//		view.addObject("listMsgShare", list);
//		//个人分享信息数
//		Integer shareNum = msgShareService.getCountMsg(userInfo.getComId(),userInfo.getId(),msgShare,modPage);
//		view.addObject("shareNum", shareNum);
		
		//取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		view.addObject("listOwners",listOwners);
		
		view.addObject("msgShare", msgShare);
		view.addObject("modList", modList);
		
		//总待办数
		Integer todoNums = todayWorksService.countTodo(userInfo.getComId(),userInfo.getId());
		view.addObject("todoNums", todoNums);
		
		//总关注未读消息数
		Integer attenNums = todayWorksService.countAttenNoRead(userInfo.getComId(),userInfo.getId());
		view.addObject("attenNums", attenNums);
		
		//预期待办任务
		Integer overdueNums = taskService.overdueTaskNum(userInfo);
		view.addObject("overdueNums", overdueNums);
		
		return view;
	}
	/**
	 * 取得常用人员信息
	 * @param userNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listUsedUser")
	public Map<String,Object> listUsedUser(Integer userNum){
		Map<String,Object> map = new HashMap<String, Object>();
		if(userNum == null){
			userNum = 10;
		}
		UserInfo sessionUser = this.getSessionUser();
		//取得常用人员列表
		List<UserInfo> usedUsers = userInfoService.listUsedUser(sessionUser.getComId(),sessionUser.getId(),userNum);
		map.put("status", "y");
		map.put("usedUser", usedUsers);
		return map;
	}
	
	/**
	 * 修改个人配置信息页面
	 * @return
	 */
	@RequestMapping(value="/editUserInfoPage")
	public ModelAndView editUserInfoPage(String viewType){
		ModelAndView view = new ModelAndView("/userInfo/selfCenter");
		UserInfo sessionUser = userInfoService.getUserInfo(this.getSessionUser().getComId(),this.getSessionUser().getId());
		view.addObject("userInfo", sessionUser);
		//用户主键
		view.addObject("id", Encodes.encodeBase64(sessionUser.getId().toString()).trim());
		view.addObject("viewType", viewType);
		if(null!=sessionUser.getIsChief() && sessionUser.getIsChief().equals("0")){//不是首席，需要设定上级
			//获取个人直属上级集合
			List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(sessionUser);
			//生成任务参与人JSon字符串
			if(null!=listImmediateSuper && !listImmediateSuper.isEmpty()){
				StringBuffer leaderJson = new StringBuffer("[");
				for(ImmediateSuper vo:listImmediateSuper){
					leaderJson.append("{'userID':'"+vo.getLeader()+"','userName':'"+vo.getLeaderName()+"','gender':'"+vo.getGender()+"','uuid':'"+vo.getUuid()+"','fileName':'"+vo.getFileName()+"'},");	
				}
				leaderJson = new StringBuffer(leaderJson.substring(0,leaderJson.lastIndexOf(",")));
				leaderJson.append("]");
				view.addObject("leaderJson",leaderJson);
			}
		}
		//用于控制选择入职时间的最大值不超过首次使用时间
		SystemLog systemLog = systemLogService.getUserHireDate(sessionUser);
		if(null!=systemLog){
			String recorddatetime = systemLog.getRecordDateTime();
			view.addObject("hireMaxDate",recorddatetime);
		}
		
		//取得当前人员代理的离岗人员
		ForMeDo forMeDo = userInfoService.queryForMeDo(sessionUser.getComId(), sessionUser.getId());
		view.addObject("forMeDo", forMeDo);
		return view;
	}
	
	/**
	 * 修改个人基本配置信息页面
	 * @return
	 */
	@RequestMapping(value="/editUserBaseInfo")
	public ModelAndView editUserBaseInfo(){
		ModelAndView view = new ModelAndView("/userInfo/editUserBaseInfo");
		UserInfo sessionUser = userInfoService.getUserInfo(this.getSessionUser().getComId(),this.getSessionUser().getId());
		view.addObject("sessionUser", sessionUser);
		//用户主键
		view.addObject("id", Encodes.encodeBase64(sessionUser.getId().toString()).trim());
		return view;
	}
	/**
	 * 跳转用户密码修改页面
	 * @return
	 */
	@RequestMapping(value="/updateUserPasswordPage")
	public ModelAndView updateUserPasswordPage(HttpSession session){
		session.removeAttribute("userPass");
		ModelAndView view = new ModelAndView("/userInfo/updateUserPassword");
		UserInfo sessionUser = userInfoService.getUserInfo(this.getSessionUser().getComId(),this.getSessionUser().getId());
		view.addObject("sessionUser", sessionUser);
		return view;
	}
	/**
	 * 跳转到密码修改页面
	 * @return
	 */
	@RequestMapping("/nextStepPass")
	public ModelAndView nextStepPass(String redirectPage,String yzm,String noticeType) {
		//获取后台验证码
		if(null==noticeType || "".equals(noticeType.trim())){
			this.setNotification(Notification.ERROR, "验证类型标识为空了，请联系管理员。");
			return new ModelAndView("redirect:"+redirectPage);
		}
		PassYzm passYzm = null;
		UserInfo curUser = this.getSessionUser();
		if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getMovePhone());
		}else if(ConstantInterface.GET_BY_EMAIL.equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getEmail().toLowerCase());
		}
		if(null!=passYzm && yzm.equalsIgnoreCase(passYzm.getPassYzm())){
			ModelAndView mav = new ModelAndView("redirect:/userInfo/nextStepPassForward?sid="+this.getSid());
			mav.addObject("redirectPage", redirectPage);
			//删除验证码
			userInfoService.delPassYzmById(passYzm.getId());
			return mav;
		}else{
			this.setNotification(Notification.ERROR, "验证码失效");
			return new ModelAndView("redirect:"+redirectPage);
		}
		
	}
	/**
	 * 跳转到密码修改页面
	 * @return
	 */
	@RequestMapping("/nextStepPassForward")
	public ModelAndView nextStepPassForward() {
		//后台验证码
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/userInfo/updateUserPassword");
		mav.addObject("sessionUser", userInfo);
		mav.addObject("showUp", "showUp");
		return mav;
			
	}
	/**
	 * 发送验证码
	 * @param noticeType 发送方式
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sendPassYzm")
	public Map<String, Object> sendPassYzm(String noticeType){
		if(null==noticeType || "".equals(noticeType.trim())){
			Map<String, Object> map = new HashMap<String, Object>();
			//告知方式未指定
			map.put(ConstantInterface.TYPE_STATUS, "n");
			return map;
		}else{
			UserInfo curUser = this.getSessionUser();//获取当前操作人对象
			Map<String, Object> map = userInfoService.doSendPassYzm(curUser,noticeType);
			return map;
		}
	}

	/**
	 * 验证码
	 * @param param
	 * @param noticeType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkYzm/{requestURI}")
	public Map<String, Object> checkYzm(String param,String noticeType) {
		Map<String, Object> map = new HashMap<String, Object>();
		
  		if(null==noticeType || "".equals(noticeType.trim())){
			this.setNotification(Notification.ERROR, "验证类型标识为空了，请联系管理员。");
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "验证类型标识为空了，请联系管理员。");
			return map;
		}
		PassYzm passYzm = null;
		UserInfo curUser = this.getSessionUser();
		if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getMovePhone());
		}else if(ConstantInterface.GET_BY_EMAIL.equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getEmail());
		}
		
		if(null==passYzm ){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "验证码失效！");
			
		}else if(param.equalsIgnoreCase(passYzm.getPassYzm())) {
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		} else {
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "验证码输入错误！");
		}
		return map;
	}
	/**
	 * 验证账号是否在团队中存在
	 * @param param
	 * @param noticeType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkAccount")
	public Map<String, Object> checkAccount(String param,String noticeType) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(null==noticeType || "".equals(noticeType.trim())){
			this.setNotification(Notification.ERROR, "验证类型标识为空了，请联系管理员。");
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "验证类型标识为空了，请联系管理员。");
			return map;
		}
		UserInfo curUser = this.getSessionUser();
		if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
			UserInfo userInfo = userInfoService.getUserInfoByAccount(param.toLowerCase());
			if(null==userInfo){//没有被绑定
				map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			}else if(null==curUser.getMovePhone()){ //当前操作人员，没有设定手机号
				if(userInfo.getMovePhone().equals(param)){//当前人员手机号与填写的相同
					map.put(ConstantInterface.TYPE_STATUS, "f");
					map.put(ConstantInterface.TYPE_INFO, "该手机号已被绑定，请重新填写");
				}else{
					map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
				}
			}else{//已被绑定
				map.put(ConstantInterface.TYPE_STATUS, "f");
				map.put(ConstantInterface.TYPE_INFO, "该手机号已被绑定，请重新填写");
			}
		}else if(ConstantInterface.GET_BY_EMAIL.equals(noticeType.trim())){
			UserInfo userInfo = userInfoService.getUserInfoByAccount(param.toLowerCase());
			
			if(null==userInfo){//没有被绑定
				map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			}else if(null==curUser.getEmail() ){//当前操作人员，没有设定手机号
				if(userInfo.getEmail().equals(param.toLowerCase())){ //当前人员手机号与填写的相同
					map.put(ConstantInterface.TYPE_STATUS, "f");
					map.put(ConstantInterface.TYPE_INFO, "该邮箱已被绑定，请重新填写");
				}else{
					map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
				}
			}else{//已被绑定
				map.put(ConstantInterface.TYPE_STATUS, "f");
				map.put(ConstantInterface.TYPE_INFO, "该邮箱已被绑定，请重新填写");
			}
		}
		return map;
	}
	/**
	 * 修改密码
	 * @param userInfo
	 * @param sid
	 * @return
	 */
	@RequestMapping(value="/updatePassword",method=RequestMethod.POST)
	public ModelAndView updatePassword(HttpSession session,UserInfo userInfo,String passwordMD5) {
		
		String sid = this.getSid();
		UserInfo sessionUser = this.getSessionUser();
		
		userInfo.setId(sessionUser.getId());
		
		// 修改密码
		String password = userInfo.getPassword();
		//密码加密
		sessionUser.setPassword(passwordMD5);
		
		userInfo.setPassword(passwordMD5);
		userInfoService.updatePassword(userInfo,sessionUser);
		//系统日志
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "修改密码", 
				ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
		//map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		//map.put(ConstantInterface.TYPE_INFO, "修改成功");
		this.setNotification(Notification.SUCCESS, "密码更新成功！");
		
		this.logOut(sid);
		
		ModelAndView view = new ModelAndView("redirect:/logout.jsp");
		// 企业号
		view.addObject("comId", Encodes.encodeBase64(sessionUser.getComId().toString()).trim());
		// 用户主键
		view.addObject("id", Encodes.encodeBase64(sessionUser.getId().toString()).trim());
		view.addObject("sid", sid);
		
		return view;
	}
	/**
	 * 直属上级配置页面
	 * @return
	 */
	@RequestMapping(value="/immediateSuper")
	public ModelAndView immediateSuper(){
		ModelAndView view = new ModelAndView("/userInfo/immediateSuper");
		UserInfo sessionUser = userInfoService.getUserInfo(this.getSessionUser().getComId(),this.getSessionUser().getId());
		view.addObject("sessionUser", sessionUser);
		//获取个人直属上级集合
		List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(sessionUser);
		//生成任务参与人JSon字符串
		StringBuffer leaderJson = new StringBuffer("[");
		if(null!=listImmediateSuper && !listImmediateSuper.isEmpty()){
			for(ImmediateSuper vo:listImmediateSuper){
				leaderJson.append("{'userID':'"+vo.getLeader()+"','userName':'"+vo.getLeaderName()+"','gender':'"+vo.getGender()+"','uuid':'"+vo.getUuid()+"','fileName':'"+vo.getFileName()+"'},");	
			}
			leaderJson = new StringBuffer(leaderJson.substring(0,leaderJson.lastIndexOf(",")));
		}
		leaderJson.append("]");
		view.addObject("leaderJson",leaderJson);
		return view;
	}
	
	/**
	 * 验证自己是否有上级
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/checkLeader",method = RequestMethod.POST)
	public Map<String, Object> checkLeader(){
		//当前操作人员
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==userInfo){//服务器session用户数据丢失
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//获取个人直属上级集合
		List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(userInfo);
		if(null!=listImmediateSuper && !listImmediateSuper.isEmpty()){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		}else{
			map.put(ConstantInterface.TYPE_STATUS, "f1");
		}
		return map;
	}
	/**
	 * 列出人员上级
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxListImmediateSuper")
	public Map<String, Object> ajaxListImmediateSuper(){
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//获取个人直属上级集合
		List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(userInfo);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		map.put("listImmediateSuper", listImmediateSuper);
		return map;
	}
	
	/**
	 * 直属上级更新 并  添加工作轨迹（Service层添加的）
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/updateImmediateSuper")
	public ImmediateSuper updateImmediateSuper(Integer[] userIds,String isChief) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		ImmediateSuper immediateSuper = new ImmediateSuper();
		
		if(null==userInfo){
			immediateSuper.setSucc(false);
			immediateSuper.setPromptMsg("session已失效");
			return immediateSuper;
		}
		if(null!=userIds && userIds.length>0){
			//直接上级是否闭环验证
			for(Integer userId:userIds){
				if(userId.equals(userInfo.getId())){
					immediateSuper.setSucc(false);
					immediateSuper.setPromptMsg("不能分享给自己，这样没意义！");
					return immediateSuper;
				}else{
					boolean succ = userInfoService.superClosedLoopCheck(userId,userInfo);
					if(!succ){
						UserInfo user = userInfoService.getUserInfo(userInfo.getComId(),userId);
						immediateSuper.setSucc(false);
						immediateSuper.setPromptMsg("不能分享给\""+user.getUserName()+"\"；不然分享圈就成闭环了。");
						return immediateSuper;
					}
				}
			}
			//获取个人直属上级集合
			List<ImmediateSuper> listPreSup = userInfoService.listImmediateSuper(userInfo);
			boolean succ = userInfoService.updateImmediateSuper(userIds,isChief, userInfo);
			if(succ){
				if(null!=listPreSup && listPreSup.size()>0){//离职人员有上级
					for (ImmediateSuper immediateSuperss : listPreSup) {
						//减少原来上级的一个下级
						SessionContext.updateSub(immediateSuperss.getLeader(), userInfo.getComId(),-1);
					}
				}
				//获取个人直属上级集合
				for(Integer userId:userIds){
					//添加一个下级
					SessionContext.updateSub(userId, userInfo.getComId(),1);
				}
				
				immediateSuper.setSucc(succ);
				immediateSuper.setPromptMsg("更新成功");
			}else{
				immediateSuper.setSucc(succ);
				immediateSuper.setPromptMsg("更新失败");
			}
		}else{
			immediateSuper.setSucc(false);
			immediateSuper.setPromptMsg("请选择你的直属上级！");
		}
		return immediateSuper;
	}
	/**
	 * 是否有上级
	 * 否：删除直属上级,并设定为首席
	 * 是：不设为首席
	 * @param isChief
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/updateLeader")
	public Map<String,Object> updateLeader(String isChief) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		if("1".equals(isChief)){
			List<Integer> leaderIds = userInfoService.updateNoLeader(isChief,sessionUser);
			if(null!=leaderIds && !leaderIds.isEmpty()){
				for (Integer userId : leaderIds) {
					//减少原来上级的一个下级
					SessionContext.updateSub(userId, sessionUser.getComId(),-1);
				}
			}
		}else if("0".equals(isChief)){
			//有上级,设定不是首席
			userInfoService.updateHasLeader(isChief, sessionUser);
		}
		
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		map.put(ConstantInterface.TYPE_INFO, "更新成功");
		return map;
	}
	/**
	 * 删除直属上级
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/delImmediateSuper")
	public ImmediateSuper delImmediateSuper(Integer userId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		
		ImmediateSuper immediateSuper = new ImmediateSuper();
		if(null==userInfo){
			immediateSuper.setSucc(false);
			immediateSuper.setPromptMsg(CommonConstant.OFF_LINE_INFO);
			return immediateSuper;
		}
		boolean succ = userInfoService.delImmediateSuper(userId,userInfo);
		if(null!=userId){
			if(succ){
				//减少原来上级的一个下级
				SessionContext.updateSub(userId, userInfo.getComId(),-1);
				
				immediateSuper.setSucc(succ);
				immediateSuper.setPromptMsg("删除成功");
			}else{
				immediateSuper.setSucc(succ);
				immediateSuper.setPromptMsg("删除失败");
			}
		}else{
			immediateSuper.setSucc(false);
			immediateSuper.setPromptMsg("请选择你需要删除的直属上级！");
		}
		return immediateSuper;
	}
	/**
	 * 离职移交界面跳转
	 * @return
	 */
	@RequestMapping(value="/handOverPage")
	public ModelAndView handOverPage(){
		ModelAndView view = new ModelAndView("/userInfo/handOverPage");
		view.addObject("userInfo", this.getSessionUser());
		return view;
	}
	/**
	 * 离职移交
	 * @param sid 用户在系统中的session标志
	 * @param userId 移交对象主键
	 * @param userName 移交对象姓名
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/dimission")
	public Map<String, Object> dimission(String sid,Integer userId,String userName){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo =  this.getSessionUser();
		if(null!=userInfo){
			//离职移交
			List<ImmediateSuper> listSup = userInfoService.updateFordimission(userInfo,userId,userName);
			if(null!=listSup && listSup.size()>0){//离职人员有上级
				for (ImmediateSuper immediateSuper : listSup) {
					//减少一个下级
					SessionContext.updateSub(immediateSuper.getLeader(), userInfo.getComId(),-1);
				}
			}
			//返回状态
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			//系统注销用户
			this.logOut(sid);
		}else{
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "session已失效，请重新登录！");
			
		}
		return map;
	}
	
	/**
	 * 修改个人头像页面
	 * @return
	 */
	@RequestMapping(value="/editUserHeadImg")
	public ModelAndView editUserHeadImg(){
		ModelAndView view = new ModelAndView("/userInfo/selfCenter");
		UserInfo sessionUser = userInfoService.getUserInfo(this.getSessionUser().getComId(),this.getSessionUser().getId());
		view.addObject("userInfo", sessionUser);
		return view;
	}
	
	/**
	 * 修改个人联系信息页面
	 * @return
	 */
	@RequestMapping(value="/editUserTel")
	public ModelAndView editUserTel(){
		ModelAndView view = new ModelAndView("/userInfo/editUserTel");
		UserInfo sessionUser = userInfoService.getUserInfo(this.getSessionUser().getComId(),this.getSessionUser().getId());
		view.addObject("sessionUser", sessionUser);
		return view;
	}
	
	
	/**
	 * 修改个人配置信息
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/editUserInfo",method=RequestMethod.POST)
	public ModelAndView editUserInfo(UserInfo userInfo) throws Exception{
		ModelAndView view = new ModelAndView("redirect:/userInfo/editUserInfoPage?activityMenu=self_m_3.1&sid="+this.getSid());
		UserInfo sessionUser = this.getSessionUser();
		//系统日志内容
		String content = "修改用户基本信息";
		if(null!=sessionUser.getUserName()&&null!=userInfo.getUserName()&&!sessionUser.getUserName().equals(userInfo.getUserName())){
			content = "修改用户基本信息，用户名由("+sessionUser.getUserName()+")改为("+userInfo.getUserName()+")";
		}
		//用户信息单个修改
		userInfoService.updateUserDetail(userInfo,this.getSessionUser());
		this.setNotification(Notification.SUCCESS, "个人信息更新成功!");
		//直属上级设置
		List<UserInfo> listLeader = userInfo.getListUserInfo();
		List<Integer> userIds = new ArrayList<Integer>();
		boolean canSave = true;
		if(null!=listLeader){
			//直接上级是否闭环验证
			for(UserInfo leader:listLeader){
				userIds.add(leader.getId());
				if(leader.getId().equals(userInfo.getId())){
					this.setNotification(Notification.ERROR, "直属上级设置失败！不能分享给自己，这样没意义！");
					canSave = false;
				}else if(!userInfoService.superClosedLoopCheck(leader.getId(),sessionUser)){
					UserInfo user = userInfoService.getUserInfo(sessionUser.getComId(),leader.getId());
					this.setNotification(Notification.ERROR, "直属上级设置失败！不能分享给\""+user.getUserName()+"\"；不然分享圈就成闭环了。");
					canSave = false;
				}
			}
		}
		if(canSave){
			//获取个人直属上级集合
			List<ImmediateSuper> listPreSup = userInfoService.listImmediateSuper(this.getSessionUser());
			boolean succ = userInfoService.updateImmediateSuper((Integer[])userIds.toArray(new Integer[userIds.size()]),
					userInfo.getIsChief(),this.getSessionUser());
			if(succ){
				if(null!=listPreSup && listPreSup.size()>0){//离职人员有上级
					for (ImmediateSuper immediateSuperss : listPreSup) {
						//减少原来上级的一个下级
						SessionContext.updateSub(immediateSuperss.getLeader(), userInfo.getComId(),-1);
					}
				}
				//获取个人直属上级集合
				for(Integer userId:userIds){
					//添加一个下级
					SessionContext.updateSub(userId, userInfo.getComId(),1);
				}
			}else{
				this.setNotification(Notification.ERROR, "个人信息更新失败!");
			}
		}
		
		UserInfo user = userInfoService.getUserInfo(sessionUser.getComId(),sessionUser.getId()); 
		//设置下级个数
		user.setCountSub(userInfoService.countSubUser( sessionUser.getId(), sessionUser.getComId()));
		this.updateSessionUser(user);
		//系统日志
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), content,
				ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
		return view;
	}
	

	/**
	 * 个人信息更新ByAjax
	 * @param userInfo
	 * @param attrType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/updateUserAttr",method=RequestMethod.POST)
	public Map<String,Object> updateUserAttr(UserInfo userInfo,String attrType) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		if(StringUtil.isBlank(StringUtil.delNull(attrType))){
			map.put("isSucc", "no");
			map.put("msg", "更新属性标识符为空！attrType："+attrType);
			return map;
		}else if(null==userInfo){
			map.put("isSucc", "no");
			map.put("msg", "个人信息对象为空!");
			return map;
		}
		
		UserInfo curUser = this.getSessionUser();
		userInfo.setId(curUser.getId());//设置个人主键
		userInfo.setComId(curUser.getComId());//设置团队主键
		if("nickname".equals(attrType)){
			String nickName = userInfo.getNickname();
			List<UserInfo> users = userInfoService.checkNickName(nickName.toLowerCase(),curUser.getId());
			if(null!=users && users.size()>0){
				map.put("isSucc", "no");
				map.put("msg", "别名已被使用！");
				map.put("preValue", curUser.getNickname());
				return map;
			}
			
		}else if("leader".equals(attrType)){
			map =userInfoService.updateUserLeader(userInfo, curUser);//更新直属上级设置
			return map;
		}
		boolean isSucc = false;
		boolean isAttrChanged = CommonUtil.isAttrChanged(userInfo, attrType, curUser);
		if(isAttrChanged){
			isSucc = userInfoService.updateUserAttrs(attrType,userInfo);//更新用户属性
		}else{
			map.put("isSucc", "noChanged");
			return map;
		}
		if(isSucc){
			UserInfo user = userInfoService.getUserInfo(curUser.getComId(),curUser.getId()); 
			user.setCountSub(userInfoService.countSubUser(curUser.getId(),curUser.getComId()));//设置下级个数
			this.updateSessionUser(user);//更新session用户信息
			map.put("isSucc", "yes");
			map.put("msg", " 个人信息更新成功！");
			return map;
		}else{
			map.put("isSucc", "no");
			map.put("msg", "个人信息更新失败！");
			return map;
		}
	}
	/**
	 * 修改个人配置信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/AjaxEditUserInfo")
	public Map<String,Object> ajaxEditUserInfo(UserInfo userInfo){
		UserInfo sessionUser = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "连接已断开,请重新登录");
			return map;
		}
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		//系统日志内容
		String content = "修改用户基本信息";
		if(null!=sessionUser.getUserName()&&null!=userInfo.getUserName()&&!sessionUser.getUserName().equals(userInfo.getUserName())){
			content = "修改用户基本信息，用户名由("+sessionUser.getUserName()+")改为("+userInfo.getUserName()+")";
		}
		//用户信息单个修改
		userInfoService.updateUserDetail(userInfo,this.getSessionUser());
		
		if(null!=userInfo.getUserName()//姓名不为空
				&& null!=userInfo.getGender()//性别不为空
				&& null!=userInfo.getNickname()//绰号不为空
				&& null!=userInfo.getJob()//职位不为空
				&& null!=userInfo.getBirthday() ){//生日不为空
			//添加积分
			jifenService.addJifen(sessionUser.getComId(), sessionUser.getId(), ConstantInterface.TYPE_USERINFO,"个人资料，完善基本信息",0);
		}else if(null!=userInfo.getQq()//QQ不为空
				&& null!=userInfo.getLinePhone()//固定电话不为空
				&& null!=userInfo.getMovePhone()//移动电话不为空
				&& null!=userInfo.getWechat()//微信不为空
				){
			//添加积分
			jifenService.addJifen(sessionUser.getComId(), sessionUser.getId(),ConstantInterface.TYPE_USERTEL,"个人资料，完善联系信息",0);
		}
		
		UserInfo user = userInfoService.getUserInfo(sessionUser.getComId(),sessionUser.getId()); 
		//设置下级个数
		user.setCountSub(userInfoService.countSubUser( sessionUser.getId(), sessionUser.getComId()));
		this.updateSessionUser(user);
		//系统日志
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), content,
				ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
		return map;
	}
	

	/**
	 * 修改密码前验证密码
	 * @param param
	 * @param name
	 * @param id 当前用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/AjaxUpdateUserPassAndName")
	public Map<String, Object> ajaxUpdateUserPassAndName(String userName,String password) {
		UserInfo sessionUser = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "连接已断开,请重新登录");
			return map;
		}
		//用户信息单个修改
		userInfoService.updateUserPassAndName(userName,password,this.getSessionUser());
		
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		
		//设置用户名
		sessionUser.setUserName(userName);
		if (null !=userName
				&& !"".equals(StringUtil.delNull(userName))) {// 修改用户名
			sessionUser.setAllSpelling(PinyinToolkit.cn2Spell(userName));
			sessionUser.setFirstSpelling(PinyinToolkit.cn2FirstSpell(userName));
		}
		//密码加密
		String passwordMD5 = Encodes.encodeMd5(password);
		sessionUser.setPassword(passwordMD5);
		
		this.updateSessionUser(sessionUser);
		return map;
	}
	/**
	 * 修改密码前验证密码
	 * @param param
	 * @param name
	 * @param id 当前用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/validatePassword")
	public Map<String, Object> validatePassword(String param, String name, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		//验证密码加密
		String passwordMD5 = DigestUtils.md5Hex(param);
		//修改密码前验证密码
		boolean validateResult = userInfoService.validatePassword(id,passwordMD5);
		if (validateResult) {
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		} else {
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "密码输入错误！");
		}
		return map;
	}

	
	/**
	 * 查询部门的用户列表
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/listPagedUserForDep")
	public ModelAndView listPagedUserForDep(UserInfo userInfo) {
		//用户所在的公司
		userInfo.setComId(this.getSessionUser().getComId());
		//查询部门的用户列表
		List<UserInfo> list = userInfoService.listPagedUserForDep(userInfo);
		ModelAndView mav = new ModelAndView("/userInfo/listPagedUserForDep", "list", list);
		return mav;
	}
	/**
	 * 查询分组的用户列表
	 * @param userInfo
	 * @param grpId
	 * @return
	 */
	@RequestMapping("/listPagedUserForGrp")
	public ModelAndView listPagedUserForGrp(UserInfo userInfo,String grpId) {
		//用户所在的公司
		userInfo.setComId(this.getSessionUser().getComId());
		userInfo.setId(this.getSessionUser().getId());
		//查询部门的用户列表
		List<UserInfo> list = userInfoService.listPagedUserForGrp(userInfo,grpId);
		ModelAndView mav = new ModelAndView("/userInfo/listPagedUserForGrp", "list", list);
		return mav;
	}
	/**
	 * 查询分组的用户立即添加分组
	 * @param userInfo
	 * @param grpId
	 * @return
	 */
	@RequestMapping("/listPagedUsedUserForGrp")
	public ModelAndView listPagedUsedUserForGrp(UserInfo userInfo,String grpId) {
		//用户所在的公司
		userInfo.setComId(this.getSessionUser().getComId());
		userInfo.setId(this.getSessionUser().getId());
		//查询部门的用户列表
		List<UserInfo> list = userInfoService.listPagedUserForGrp(userInfo,grpId);
		ModelAndView mav = new ModelAndView("/userInfo/listPagedUsedUserForGrp", "list", list);
		return mav;
	}
	/**
	 * 取得人员名片
	 * @param userId 人员主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userDetail")
	public Map<String,Object> userInfoPage(Integer userId) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null!=userInfo){
			UserInfo user = userInfoService.getUserBaseInfo(userInfo.getComId(),userId);
			map.put("user", user);
		}
		return map;
	}
	/**
	 * 查询用户详细信息
	 * @param id 要查询的人员的主键ID
	 * @param busType
	 * @param busId
	 * @return
	 */
	@RequestMapping("/userInfoDetailPage")
	public ModelAndView userInfoDetailPage(Integer id,String busType,String busId) {
		UserInfo userInfo = this.getSessionUser();
		UserInfo userInfoT = userInfoService.getUserInfo(userInfo.getComId(),id);
		ModelAndView mav = new ModelAndView("/userInfo/userInfoDetailPage", "userInfoT", userInfoT);
		mav.addObject("userInfo", userInfo);
		mav.addObject("busType", busType);
		mav.addObject("busId", busId);
		return mav;
	}
	
	/**
	 * 完善用户信息，在登陆后显示
	 * @param id 用户主键
	 * @param comId 企业号
	 * @return
	 */
	@RequestMapping("/addInfoPage")
	public ModelAndView addInfoPage(Integer id,Integer comId) {
		UserInfo userInfo = userInfoService.getUserInfo(comId,id);
		ModelAndView mav = new ModelAndView("/userInfo/addInfo");
		mav.addObject("userInfo", userInfo);
		
		//获取个人直属上级集合
		List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(userInfo);
		//生成任务参与人JSon字符串
		StringBuffer leaderJson = new StringBuffer("[");
		if(null!=listImmediateSuper && !listImmediateSuper.isEmpty()){
			for(ImmediateSuper vo:listImmediateSuper){
				leaderJson.append("{'userID':'"+vo.getLeader()+"','userName':'"+vo.getLeaderName()+"','gender':'"+vo.getGender()+"','uuid':'"+vo.getUuid()+"','fileName':'"+vo.getFileName()+"'},");	
			}
			leaderJson = new StringBuffer(leaderJson.substring(0,leaderJson.lastIndexOf(",")));
		}
		leaderJson.append("]");
		mav.addObject("leaderJson",leaderJson);
		
		//用户姓名
		String userName = userInfo.getUserName();
		//登录别名
		String nickName = userInfo.getNickname();
		//性别
		String gender = userInfo.getGender();
		//职位
		String job = userInfo.getJob();
		
		//头像
		String bigHeadPortrait = userInfo.getBigHeadPortrait();
		
		//QQ
		String qq = userInfo.getQq();
		//手机
		String movePhone = userInfo.getMovePhone();
		//座机号码
		String linePhone = userInfo.getLinePhone();
		//微信
		String wechat = userInfo.getWechat();
		
		//部门
		Integer depId = userInfo.getDepId();
		if((null==listImmediateSuper || listImmediateSuper.isEmpty()
				|| null==depId || depId==0) && "0".equals(userInfo.getIsChief())){//非老板
			mav.addObject("tabIndex", 4);
		}else if(null==userName || "".equals(userName)//用户姓名为空
				||null==nickName || "".equals(nickName)//登录别名为空
				||null==gender || "".equals(gender)//性别为空
				||null==job || "".equals(job)//职位为空
				){
			//mav.addObject("tabIndex", 1);
		}else if(null==bigHeadPortrait || bigHeadPortrait.equals("0")){//没有设置头像
			mav.addObject("tabIndex", 2);
		}else if(null==qq || "".equals(qq)//QQ为空
				|| null==movePhone || "".equals(movePhone)//移动电话为空
				|| null==linePhone || "".equals(linePhone)//座机号码为空
				|| null==wechat || "".equals(wechat)){//微信为空
			//mav.addObject("tabIndex", 3);
		}
		if(Integer.parseInt(userInfo.getAdmin())>0){
			AttenceRule attenceRule = attenceService.getAttenceRule(null,userInfo.getComId());
			mav.addObject("attenceRule",attenceRule);
		}
		
		return mav;
	}
	
	/*********************以下是人员审核***************************************/
	/**
	 * 查询待审核用户分页列表 
	 * @param joinRecord
	 * @return
	 */
	@RequestMapping("/listPagedForCheck")
	public ModelAndView listPagedForCheck(JoinRecord joinRecord) {
		
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals(ConstantInterface.USER_ROLE_NORMAL)){//没有审核权限的直接跳转到主页面
			ModelAndView mav = new ModelAndView("/index");
			this.setNotification(Notification.ERROR, "抱歉，你没有审核权限！");
			return mav;
		}else{
			if(null!=joinRecord && null!=joinRecord.getId()){
				//标识为查看
				todayWorksService.updateTodoWorkRead(joinRecord.getId(), userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_APPLY,0);
			}
			
			//审核人所在的企业
			joinRecord.setComId(userInfo.getComId());
			joinRecord.setJoinType(ConstantInterface.JOIN_APPLY.toString());
			//待审核的或是审核不通过的
			List<JoinRecord> list = userInfoService.listPagedForJoin(joinRecord);
			ModelAndView mav = new ModelAndView("/organic/organicCenter", "list", list);
			mav.addObject("userInfo", userInfo);
			return mav;
		}
	}
	
	/**
	 * 批量拒绝用户
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	
	@RequestMapping("/rejectUserInfos")
	public ModelAndView rejectUserInfos(Integer[] ids, String redirectPage,HttpServletRequest request) {
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
			ModelAndView mav = new ModelAndView("redirect:/index?sid="+this.getSid());
			this.setNotification(Notification.ERROR, "抱歉，你没有审核权限！");
			return mav;
		}
		
		//批量拒绝用户
		List<JoinRecord> listJoinRecord = userInfoService.batchCheckUserInfos(ids,userInfo,ConstantInterface.USER_CHECK_REJECT);
		
		//链接地址
		String url = PublicConfig.SERVER_URL.get("serverOutIP");  
		String basePath = request.getScheme()+"://"+url+":"+request.getServerPort();
		//邮件发送准备
		MessageSender sender = MessageSender.getMessageSender();
        //标题
        String subject = "拒绝申请加入企业"+userInfo.getOrgName(); 
        for (JoinRecord joinRecord : listJoinRecord) {
        	//账号
			String account = joinRecord.getAccount().toLowerCase();
			if(account.indexOf("@")>0){
				//内容
				String body = "<div>"+joinRecord.getAccount()+"您好！<div><br>"
						+"您申请加入企业"+userInfo.getOrgName()+",未通过管理员("+userInfo.getUserName()+")的审核！ <br/>";
				body= body +"管理员是批量操作的 <br/>";
				body= body + "您可以点击以下链接登陆<br/>" 
						+ "<a href = "+basePath+">"+basePath+"</a><br/>"
						+"(如果点击链接无反应，请复制链接到浏览器里直接打开)";
				//发送邮件
				new Thread(new MailSendThread(sender, joinRecord.getAccount(), subject, body)).start();
				
	            
			}else{
				//TODO 是否发送消息
//				phoneMsgService.sendMsg(account, new Object[]{orgName,account,""}, BusinessTypeConstant.MSG_REGEIST_APPLY_NO,userInfo.getComId());
			}
		}
        
		this.setNotification(Notification.SUCCESS, "操作成功！");
		return new ModelAndView("redirect:" + redirectPage);
	}
	/**
	 * 批量同意用户
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	
	@RequestMapping("/agreeUserInfos")
	public ModelAndView agreeUserInfos(Integer[] ids, String redirectPage,HttpServletRequest request) {
		
		UserInfo user = this.getSessionUser();
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
			ModelAndView mav = new ModelAndView("redirect:/index?sid="+this.getSid());
			this.setNotification(Notification.ERROR, "抱歉，你没有审核权限！");
			return mav;
		}
		//批量同意用户
		List<JoinRecord> listJoinRecord = userInfoService.batchCheckUserInfos(ids,user,ConstantInterface.USER_CHECK_AGREE);
		//链接地址
		String url = PublicConfig.SERVER_URL.get("serverOutIP");  
		String basePath = request.getScheme()+"://"+url+":"+request.getServerPort();
		UserInfo sessionUser = this.getSessionUser();
		//邮件发送准备
		MessageSender sender = MessageSender.getMessageSender();
		 //标题
        String subject = "批准加入"+sessionUser.getOrgName(); 
        for (JoinRecord joinRecord : listJoinRecord) {
        	//账号
			String account = joinRecord.getAccount().toLowerCase();
			if(account.indexOf("@")>0){
				//内容
	            String body = "<div>"+joinRecord.getAccount()+"您好！<div><br>"
	            +"团队管理员("+sessionUser.getUserName()+")已审核通过您加入企业（"+sessionUser.getOrgName()+"）的申请！ <br/>" 
	            +"您可以点击登陆<br/>" 
				+ "<a href = "+basePath+">"+basePath+"</a><br/>" 
	            +"(如果点击链接无反应，请复制链接到浏览器里直接打开)";
	            
				//发送邮件
				new Thread(new MailSendThread(sender, joinRecord.getAccount(), subject, body)).start();
			}else{
				//TODO 是否发送消息
//				phoneMsgService.sendMsg(account, new Object[]{orgName,account}, BusinessTypeConstant.MSG_REGEIST_APPLY_OK,sessionUser.getComId());
			}
			
		}
       
		return new ModelAndView("redirect:" + redirectPage);
	}
	/**
	 * 审核用户信息页面
	 * @param id
	 * @return
	 */
	@RequestMapping("/checkUserInfoPage")
	public ModelAndView checkUserInfoPage(Integer id) {
		ModelAndView mav = new ModelAndView("/userInfo/checkUserInfo");
		
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
			mav = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR, "抱歉，你没有审核权限！");
			return mav;
		}else{
			//待办事项标识为查看
			todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_APPLY,0);
			//加入记录
			JoinRecord joinRecord = userInfoService.getJoinRecord(id);
			mav.addObject("joinRecord", joinRecord);
		}
		
		return mav;
	}
	/**
	 * 审核用户信息
	 * @param emails
	 * @param redirectPage
	 * @return
	 */
	
	@RequestMapping("/checkUserInfo")
	public ModelAndView checkUserInfo(JoinRecord joinRecord,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/userInfo/checkSuc");
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("admin", "yes");
		if(sessionUser.getAdmin().equals(ConstantInterface.USER_ROLE_NORMAL)){//没有审核权限的直接跳转到主页面
			mav.addObject("admin", "no");
			this.setNotification(Notification.ERROR, "抱歉，你没有审核权限！");
			return mav;
		}
		
		String url = PublicConfig.SERVER_URL.get("serverOutIP");  
		String basePath = request.getScheme()+"://"+url+":"+request.getServerPort();
		//操作IP
		String optIP = CusAccessObjectUtil.getIpAddress(request);
		
		//审核结果 1 同意加入 2 拒绝加入
		String checkState = joinRecord.getCheckState();
		
		joinRecord.setComId(sessionUser.getComId());
		
		//审核用户后发送消息
		userInfoService.updateCheckTempUser(joinRecord,sessionUser,checkState,optIP);
		
		//账号
		String account = joinRecord.getAccount().toLowerCase();
		//团队名称
		String orgName = sessionUser.getOrgName();
		//拒绝的理由
		String reason = joinRecord.getRejectReson()==null?"":joinRecord.getRejectReson();
		if(account.indexOf("@")>0){//发送邮件
			//邮件发送准备
			MessageSender sender = MessageSender.getMessageSender();
			if(ConstantInterface.USER_CHECK_AGREE.equals(checkState)){//审核通过
				//TODO 验证账号
				//标题
				String subject = "批准加入"+orgName; 
				//内容
	            String body = "<div>"+joinRecord.getAccount()+"您好！<div><br>"
			            +"团队管理员("+sessionUser.getUserName()+")已审核通过您加入企业（"+sessionUser.getOrgName()+"）的申请！ <br/>" 
			            +"您可以点击登陆<br/>" 
						+ "<a href = "+basePath+">"+basePath+"</a><br/>" 
			            +"(如果点击链接无反应，请复制链接到浏览器里直接打开)";
				//发送邮件
				new Thread(new MailSendThread(sender, joinRecord.getAccount(), subject, body)).start();
				
				
			}else if("2".equals(checkState)){
				//标题
				String subject = "拒绝加入企业"+orgName; 
				//内容
				String body = "<div>"+joinRecord.getAccount()+"您好！<div><br>"
						+"您申请加入企业"+sessionUser.getOrgName()+",未通过管理员("+sessionUser.getUserName()+")的审核！ <br/>";
				if(null!=reason&&!"".equals(reason)){
					body= body +"原因是："+reason+" <br/>";
				}
				body= body + "您可以点击以下链接登陆<br/>" 
						+ "<a href = "+basePath+">"+basePath+"</a><br/>"
						+"(如果点击链接无反应，请复制链接到浏览器里直接打开)";
				//发送邮件
				new Thread(new MailSendThread(sender, joinRecord.getAccount(), subject, body)).start();
			}
		}else{
			//TODO 是否需要发送短信有待验证
//			if("1".equals(checkState)){//审核通过
//				phoneMsgService.sendMsg(account, new Object[]{orgName,account}, BusinessTypeConstant.MSG_REGEIST_APPLY_OK,sessionUser.getComId());
//			}else if("2".equals(checkState)){//审核未通过
//				phoneMsgService.sendMsg(account, new Object[]{orgName,account,reason}, BusinessTypeConstant.MSG_REGEIST_APPLY_NO,sessionUser.getComId());
//			}
		}
		
        this.setNotification(Notification.SUCCESS, "审核成功");
		return mav;
	}
	/**
	 * 审核用户信息
	 * @param emails
	 * @param redirectPage
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/resendEmailForCheck")
	public Map<String, Object> resendEmailForCheck(String email,String confirmId,HttpServletRequest request) {
		String url = PublicConfig.SERVER_URL.get("serverOutIP");  
		String basePath = request.getScheme()+"://"+url+":"+request.getServerPort();
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//邮件发送准备
		MessageSender sender = MessageSender.getMessageSender();
		
		//标题
		String subject = "批准加入"+sessionUser.getOrgName(); 
		//内容
		String body = "<div>"+email+"您好！<div><br/>"
		+"网络管理员("+sessionUser.getUserName()+")已审核通过您加入企业"+sessionUser.getOrgName()+"的申请！ <br/>" 
		+"您可以点击以下链接激活账号<br/>" 
		+ "<a href = "+basePath+"/registe?confirmId="+confirmId+">"+basePath+"/registe?confirmId="+confirmId+"</a><br/>" 
		+ "</a>"; 
		//发送邮件
		new Thread(new MailSendThread(sender, email, subject, body)).start();
			
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/*********************以上是人员审核***************************************/
	/*********************以下是人员邀请***************************************/
	/**
	 * 人员邀请界面
	 * @return
	 */
	@RequestMapping("/inviteUserPage")
	public ModelAndView inviteUserPage() {
		ModelAndView mav = new ModelAndView("/organic/organicCenter");
		mav.addObject("userInfo", this.getSessionUser());
		return mav;
	}
	/**
	 * 人员邀请记录
	 * @return
	 */
	@RequestMapping("/listPagedInvUser")
	public ModelAndView listPagedInvUser(JoinRecord joinRecord) {
		ModelAndView mav = new ModelAndView("/userInfo/listPagedInvUser");
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){
			this.setNotification(Notification.ERROR, "没有管理权限");
			mav = new ModelAndView("/bodyRefresh");
		}else{
			//邀请记录的公司编码
			joinRecord.setComId(this.getSessionUser().getComId());
			List<JoinRecord> list = userInfoService.listPagedInvUser(joinRecord);
			mav.addObject("list", list);
			mav.addObject("sessionUser", this.getSessionUser());
		}
		return mav;
	}
	/**
	 * 删除人员邀请记录
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/delInvUser")
	public ModelAndView delInvUser(Integer[] ids,String redirectPage) {
		UserInfo sessionUser = this.getSessionUser();
		if(sessionUser.getAdmin().equals("0")){
			this.setNotification(Notification.ERROR, "没有管理权限");
			return new ModelAndView("/bodyRefresh");
		}else{
			userInfoService.delInvUser(ids);
			this.setNotification(Notification.SUCCESS, "操作成功");
			//系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量删除人员邀请记录",
					ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
			return new ModelAndView("redirect:"+redirectPage);
			
		}
	}
	/**
	 * 人员重新邀请
	 * @param emails 考虑到要发送邮件，就没有用Id
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/resendEmail")
	public ModelAndView resendEmail(Integer[] ids,String redirectPage,HttpServletRequest request) {
		UserInfo sessionUser = this.getSessionUser();
		if(sessionUser.getAdmin().equals("0")){
			this.setNotification(Notification.ERROR, "没有管理权限");
			return new ModelAndView("/bodyRefresh");
		}else{
			String url = PublicConfig.SERVER_URL.get("serverOutIP");  
			String basePath = request.getScheme()+"://"+url+":"+request.getServerPort();
			//邮件发送准备
			MessageSender sender = MessageSender.getMessageSender();
			//标题
			String subject ="企业"+sessionUser.getOrgName()+"邀请您加入"; 
			for (Integer id : ids) {
				//SERVER_URL
				JoinRecord joinRecord = userInfoService.getJoinRecord(id);
				//内容
				String body = "<div>"+joinRecord.getAccount()+"您好！<div><br>"
				+"企业 "+sessionUser.getOrgName() +" 邀请您加入<br/>"
				+"您可以点击以下链接激活邮箱<br/>" 
				+ "<a href = "+basePath+"/registe?confirmId="+joinRecord.getConfirmId()+">"+basePath+"/registe?confirmId="+joinRecord.getConfirmId()+"</a><br/>" 
				+ "</a>"; 
				
				//发送邮件
				new Thread(new MailSendThread(sender, joinRecord.getAccount(), subject, body)).start();
			}
			//系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量重新邀请人员加入企业",
					ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
			this.setNotification(Notification.SUCCESS, "操作成功");
			return new ModelAndView("redirect:"+redirectPage);
			
		}
	}
	
	/**
	 * 验证是否当前的用户是否已存在
	 * @param param
	 * @param comId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkInvUser")
	public Map<String, Object> checkInvUser(String param, String type) {
		//去掉空格
		param = StringUtil.trim(param);
		Map<String, Object> map = new HashMap<String, Object>();
		
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		UserInfo user = userInfoService.getUserInfoByType(param.toLowerCase(), sessionUser.getComId(),type);
		if(null!=user){//邀请的人已在系统中
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "该账号已在系统中！");
		}else{//邀请的人在申请审核通过的时候没有激活
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		}
		return map;
	}
	
	/**
	 * 人员邀请
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/inviteUser",method=RequestMethod.POST)
	public Map<String, Object> inviteUser(UserInfo userInfo,String redirectPage,HttpServletRequest request,String invtWay) {
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		//当前的操作人员
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//团队号
		Integer comId = sessionUser.getComId();
		//被邀请人员(去重处理)
		Set<String> accounts = CommonUtil.removeRepAccount(userInfo.getInvUsers());
		
		Organic organic = organicService.getOrgInfo(sessionUser.getComId());
		//团队激活人数
		int members = organic.getMembers();
		//团队人员上限
		int usersUpperLimit = organic.getUsersUpperLimit();
		if(members + accounts.size() > usersUpperLimit){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "团队使用人数已达上限"+usersUpperLimit+"人！请升级团队等级！");
			return map;
		}
		
		String url = PublicConfig.SERVER_URL.get("serverOutIP");  
		String basePath = request.getScheme()+"://"+url+":"+request.getServerPort();
		//邀请人员
		userInfoService.addInviteUser(sessionUser,invtWay,comId,accounts,url,basePath);
		
		//系统日志
        systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量邀请人员",
        		ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
        this.setNotification(Notification.SUCCESS, "已发送邀请");
        map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	
	
	/**
	 * 邀请文件中的人员
	 * @param filePath
	 * @return
	 */
	@RequestMapping("/handUserFile")
	public ModelAndView handUserFile(String filePath) {
		ModelAndView mav = new ModelAndView("/userInfo/handUserFile"); 
		//没有匹配成功的
		List<String> failEmails = new ArrayList<String>();
		//匹配成功的
		List<String> sucEmails = new ArrayList<String>();
		//文件路径
		String basepath = FileUtil.getRootPath();
		File logoFile = new File(basepath+"/"+filePath);
		if(logoFile.isFile()){
			String fileName = logoFile.getName().toLowerCase();
			Set<String> cells = null;
			if(fileName.endsWith("txt")){
				cells = ExcelExportUtil.getTxt(logoFile);
			}else if(fileName.endsWith("xls")||fileName.endsWith("xlsx")){
				cells = ExcelExportUtil.getCells(logoFile);
			}
			
			String patternStr = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
			Pattern pattern = Pattern.compile(patternStr);
			for (String cell : cells) {
				Matcher matcher = pattern.matcher(cell);
				if(!matcher.matches()){
					failEmails.add(cell);
				}else{
					sucEmails.add(cell);
					
				}
				
			}
		}
		mav.addObject("failEmails", failEmails);
		mav.addObject("sucEmails", sucEmails);
		return mav;
	}
	
	/**
	 * 验证是否当前的用户是否已存在
	 * @param param
	 * @param comId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/preCheckInvUser")
	public Map<String, Object> preCheckInvUser(String[] emails) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer comId = this.getSessionUser().getComId();
		List<String> haveIn = new ArrayList<String>();
		for (String email : emails) {
			//去掉空格
			email = StringUtil.trim(email);
			//当前账号是否已在系统中
			UserInfo user = userInfoService.getUserInfoByType(email.toLowerCase(), comId,ConstantInterface.GET_BY_EMAIL);
			if(null!=user){
				haveIn.add(email);
			}
		}
		map.put("list", haveIn);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	/**
	 * 验证当前的用户的登录别名
	 * @param param
	 * @param email
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkNickName")
	public Map<String, Object> checkNickName(String param,String email) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionuser = this.getSessionUser();
		if(null == sessionuser){
			map.put(ConstantInterface.TYPE_STATUS, "n");
			map.put(ConstantInterface.TYPE_INFO,CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		String patternStr = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		
		Pattern patternEmail = Pattern.compile(patternStr);
		Matcher matcherEmail = patternEmail.matcher(param.toLowerCase());
		
		patternStr = "^13[0-9]{9}$|14[0-9]{9}$|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$";
		Pattern patternPhone = Pattern.compile(patternStr);
		Matcher matcherPhone = patternPhone.matcher(param.toLowerCase());
		if(matcherEmail.matches()){
			map.put(ConstantInterface.TYPE_STATUS, "n");
			map.put(ConstantInterface.TYPE_INFO, "邮箱不能作为别名");
		}else if(matcherPhone.matches()){
			map.put(ConstantInterface.TYPE_STATUS, "n");
			map.put(ConstantInterface.TYPE_INFO, "手机号不能作为别名");
		}else{
			List<UserInfo> users = userInfoService.checkNickName(param.toLowerCase(),sessionuser.getId());
			if(null!=users && users.size()>0){
				map.put(ConstantInterface.TYPE_STATUS, "n");
				map.put(ConstantInterface.TYPE_INFO, "别名已被使用");
			}else{
				map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			}
			
		}
		return map;
	}
	
	/**
	 * 批量邀请用户加入企业
	 * @param session
	 * @param request
	 * @param invEmails
	 * @param yzm
	 * @param requestURI
	 * @param redirectPage
	 * @param filePath 用于删除文件
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/fileInvUser/{requestURI}",method=RequestMethod.POST)
	public ModelAndView fileInvUser(HttpSession session,HttpServletRequest request,String[] invEmails,
			String yzm,@PathVariable String requestURI,String redirectPage,String filePath) throws IOException {
		//后台验证码
		String rand = (String) session.getAttribute(requestURI+"_rand");
		String url = PublicConfig.SERVER_URL.get("serverOutIP");  
		String basePath = request.getScheme()+"://"+url+":"+request.getServerPort();
		//邮件发送准备
		MessageSender sender = MessageSender.getMessageSender();
		//清除验证码
		session.removeAttribute(requestURI+"_rand");
		
		UserInfo sessionUser = this.getSessionUser();
		
		ModelAndView mav = new ModelAndView();
		
		//后台再次验证
		if(yzm.equalsIgnoreCase(rand)){
			//文件路径
			String basepath = FileUtil.getRootPath();
			String baseTempPath ="static"+ "/temp/" + "invUserFile"+"/"+sessionUser.getComId()+"/"+sessionUser.getId()+"/";
			File logoFile = new File(basepath+baseTempPath);
			if(logoFile.exists()){
				FileUtils.deleteDirectory(logoFile);
			}
			
			for (String userEmail : invEmails) {
				userEmail = StringUtil.trim(userEmail);
				UserInfo user = userInfoService.getUserInfoByType(userEmail.toLowerCase(),sessionUser.getComId(),ConstantInterface.GET_BY_EMAIL);
				if(null!=user){//已是系统用户
					continue;
				}
				//判断用户是否有一个激活码，有就用原来的，没有就重新生成一个
				JoinRecord  obj =registService.getConfirmId(userEmail.toLowerCase(),sessionUser.getComId());
				//激活码
				String confirmId = UUIDGenerator.getUUID();
				//是否为申请加入的
				if(obj!=null){//若是申请加入的或是已经邀请的直接发邮件
					confirmId = obj.getConfirmId();
					obj.setRecordCreateTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
					//审核通过
					obj.setCheckState(ConstantInterface.USER_CHECK_AGREE);
					//邀请人员
					obj.setUserId(sessionUser.getId());
					registService.updateJoinRecord(obj,null);
				}else{
					//没有邀请的记录下来
					JoinRecord joinRecord = new JoinRecord(sessionUser.getComId(),userEmail,
							ConstantInterface.JOIN_INVITE.toString(),"0",confirmId);
					//审核通过
					joinRecord.setCheckState(ConstantInterface.USER_CHECK_AGREE);
					//邀请人员
					joinRecord.setUserId(sessionUser.getId());
					registService.addJoinRecord(joinRecord);
				}
				//标题
				String subject ="企业"+sessionUser.getOrgName() +"邀请您加入"; 
				
				//内容
				String body = "<div>"+userEmail+"您好！<div><br>"
				+"企业"+sessionUser.getOrgName()+"邀请您加入<br/>"
				+"您可以点击以下链接激活邮箱<br/>" 
				+ "<a href = "+basePath+"/registe?confirmId="+confirmId+">"+basePath+"/registe?confirmId="+confirmId+"</a><br/>" 
				+ "</a>"; 
				
				//发送邮件
				new Thread(new MailSendThread(sender, userEmail, subject, body)).start();
			}
			mav.setViewName("/userInfo/checkSuc");
			mav.addObject("admin", "yes");
			this.setNotification(Notification.SUCCESS, "邀请成功！");
			//系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量邀请用户加入团队",
					ConstantInterface.TYPE_USER, sessionUser.getComId(),sessionUser.getOptIP());
			
		}else{
			this.setNotification(Notification.ERROR, "验证码过期");
			mav.setViewName("redirect:"+redirectPage);
		}
		
		return mav;
	}
	/*********************以上是人员邀请***************************************/
	
	/**
	 * 把团队里个人头像下载到static目录文件下移动端使用
	 * @param request
	 * @throws IOException 
	 */
	@RequestMapping("/initUserHeadImg")
	public void initUserHeadImg(HttpServletRequest request) throws IOException{
		//当前操作员
		UserInfo userInfo = this.getSessionUser();
		userInfo.setEnabled(ConstantInterface.SYS_ENABLED_YES);
		List<UserInfo> listUser = userInfoService.listUser(userInfo);
		if(null!=listUser){
			for(int i=0;i<listUser.size();i++){
				if(null!=listUser.get(i).getSmImgUuid() && !"".equals(listUser.get(i).getSmImgUuid().trim())){
					String basepath = FileUtil.getRootPath();
					Upfiles upfiles = uploadService.getFileByUUid(listUser.get(i).getSmImgUuid());
					/* 所有附件都保存到uploads 不存在则新增文件夹 */
					File f = new File(basepath);
					if (!f.exists()) {
						f.mkdir();
					}
					//每个人创建一个文件夹    /static/headImg/公司主键/用户主键
					String path ="/static"+ "/" + "headImg"+"/"+userInfo.getComId()+"/" +listUser.get(i).getId();
					f = new File(basepath + path);
					if (!f.exists()) {
						f.mkdirs();
					}else{
						f.delete();
						f.mkdirs();
					}
					//删除前一个文件
					String[] tempList = f.list();
					 for (int j = 0; j < tempList.length; j++) {
						File temp = new File(basepath + path + "/" + tempList[j]);
						temp.delete();
					 }
					String picHead = userInfo.getComId()+"_"+ listUser.get(i).getId()+"_"+ UUIDGenerator.getUUID() + "." + upfiles.getFileExt();
					//原图像的路径
					String orgImgPath = path + "/" + picHead ;
					// 存放文件的绝对路径
					DataOutputStream out = new DataOutputStream(new FileOutputStream(basepath + orgImgPath));
					// 附件输入流
					InputStream is = new BufferedInputStream(new FileInputStream(FileUtil.getUploadBasePath() + upfiles.getFilepath()));
					byte[] buf = new byte[1024*1024*4];
					int len = 0;
					while ((len = is.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					is.close();
					out.close();
					upfiles.setFilename(picHead);
					uploadService.updateFileName(upfiles);
				}
			}
		}
		
	}
	
	/**
	 * 用户设置所在部门
	 * @param depId 设置的部门主键
	 * @param depName 设置的部门名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateUserDep")
	public Map<String, String> updateUserDep(Integer depId,String depName) {
		UserInfo userInfo = this.getSessionUser();
		Map<String, String> map = new HashMap<String, String>();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "连接已断开，请重新登陆");
			return map;
		}
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		userInfoService.updateUserDep(userInfo.getComId(),userInfo.getId(),depId);
		//设置所属部门主键
		userInfo.setDepId(depId);
		//设置所属部门名称
		userInfo.setDepName(depName);
		this.updateSessionUser(userInfo);
		return map;
	}
	/**
	 * 用户不需要提醒完善提醒
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/alterInfo")
	public void alterInfo() {
		UserInfo userInfo = this.getSessionUser();
		userInfoService.updateAlterInfo(userInfo.getComId(),userInfo.getId());
		//设置所属部门主键
		userInfo.setAlterInfo("0");
		this.updateSessionUser(userInfo);
	}
	
	/**
	 * 设置个性签名
	 * @param selfIntr 个性签名
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateUserIntr")
	public Map<String, String> updateUserIntr(String selfIntr) {
		UserInfo userInfo = this.getSessionUser();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		userInfoService.updateUserIntr(userInfo.getId(),selfIntr);
		//设置个性签名
		userInfo.setSelfIntr(selfIntr);
		this.updateSessionUser(userInfo);
		return map;
	}
	/**
	 * 帐号更新权限验证
	 * @param noticeType 验证方式标识符
	 * @return
	 */
	@RequestMapping(value="/updateAuthorityValidatePage")
	public ModelAndView updateAuthorityValidatePage(String noticeType){
		ModelAndView view = new ModelAndView("/userInfo/updateAuthorityValidate");
		view.addObject("sessionUser", this.getSessionUser());
		view.addObject("noticeType", noticeType);
		return view;
	}
	
	/**
	 * 帐号更新
	 * @param noticeType 验证方式标识符
	 * @return
	 */
	@RequestMapping(value="/updateUserAccountPage")
	public ModelAndView updateUserAccountPage(String noticeType){
		ModelAndView view = new ModelAndView("/userInfo/updateUserAccount");
		view.addObject("sessionUser", this.getSessionUser());
		view.addObject("noticeType", noticeType);
		return view;
	}
	
	@RequestMapping("/updateUserAccountNextStepPass")
	public ModelAndView updateUserAccountNextStepPass(String redirectPage,String yzm,String noticeType) {
		//获取后台验证码
		if(null==noticeType || "".equals(noticeType.trim())){
			this.setNotification(Notification.ERROR, "验证类型标识为空了，请联系管理员。");
			return new ModelAndView("redirect:"+redirectPage);
		}
		PassYzm passYzm = null;
		UserInfo curUser = this.getSessionUser();
		if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getMovePhone());
		}else if(ConstantInterface.GET_BY_EMAIL.equals(noticeType.trim())){
			passYzm = registService.getPassYzm(curUser.getEmail().toLowerCase());
		}
		if(null!=passYzm && yzm.equalsIgnoreCase(passYzm.getPassYzm())){
			ModelAndView mav = new ModelAndView("redirect:/userInfo/updateUserAccountNextStepPassForward?sid="+this.getSid()+"&noticeType="+noticeType);
			mav.addObject("redirectPage", redirectPage);
			//删除验证码
			userInfoService.delPassYzmById(passYzm.getId());
			return mav;
		}else{
			this.setNotification(Notification.ERROR, "验证码失效");
			return new ModelAndView("redirect:"+redirectPage);
		}
		
	}
	/**
	 * 跳转到密码修改页面
	 * @return
	 */
	@RequestMapping("/updateUserAccountNextStepPassForward")
	public ModelAndView updateUserAccountNextStepPassForward(String noticeType) {
		//后台验证码
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/userInfo/updateUserAccount");
		mav.addObject("sessionUser", userInfo);
		mav.addObject("showUp", "showUp");
		mav.addObject("noticeType", noticeType);
		return mav;
			
	}
	
	/**
	 * 更新用户帐号
	 * @param noticeType
	 * @param account
	 * @return
	 */
	@RequestMapping(value="/updateUserAccount")
	public ModelAndView updateUserAccount(String redirectPage,String yzm,String noticeType,String account){
		//获取后台验证码
		if(null==noticeType || "".equals(noticeType.trim())){
			this.setNotification(Notification.ERROR, "验证类型标识为空了，请联系管理员。");
			return new ModelAndView("redirect:"+redirectPage);
		}
		
		PassYzm passYzm = null;
		if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
			passYzm = registService.getPassYzm(account);
		}else if(ConstantInterface.GET_BY_EMAIL.equals(noticeType.trim())){
			passYzm = registService.getPassYzm(account);
		}
		if(null!=passYzm && yzm.equalsIgnoreCase(passYzm.getPassYzm())){
			ModelAndView view = new ModelAndView("/refreshParent");
//			view.addObject("redirectPage","/userInfo/editUserInfoPage?&activityMenu=self_m_3.1&sid="+this.getSid());
//			this.setNotification(Notification.SUCCESS,"绑定成功！");
			//删除验证码
			userInfoService.delPassYzmById(passYzm.getId());
			UserInfo curUser = this.getSessionUser();
			
			//首先验证该账号是否有账户
			UserInfo sysUser = userInfoService.getUserInfoByAccount(account);
			
			if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
				if(null==sysUser){//没有被绑定
				}else if(null==curUser.getMovePhone()){//当前操作人员，没有设定手机号
					if(sysUser.getMovePhone().equals(account.toLowerCase())){//系统有人员填写了该手机号
						this.setNotification(Notification.ERROR, "该手机号已被绑定，请重新填写。");
						return new ModelAndView("redirect:"+redirectPage);
					}
				}else{//已被绑定
					this.setNotification(Notification.ERROR, "该手机号已被绑定，请重新填写。");
					return new ModelAndView("redirect:"+redirectPage);
				}
			}else if(ConstantInterface.GET_BY_EMAIL.equals(noticeType.trim())){
				if(null==sysUser){//没有被绑定
				}else if(null==curUser.getEmail() ){//当前操作人员，没有设定邮箱
					if(sysUser.getEmail().equals(account.toLowerCase())){//该邮箱已被系统人员绑定
						this.setNotification(Notification.ERROR, "该邮箱已被绑定，请重新填写。");
						return new ModelAndView("redirect:"+redirectPage);
					}
				}else{//已被绑定
					this.setNotification(Notification.ERROR, "该邮箱已被绑定，请重新填写。");
					return new ModelAndView("redirect:"+redirectPage);
				}
			}
			
			
			userInfoService.updateUserAccount(noticeType,account,curUser);// 更新用户帐号
			curUser = userInfoService.getUserInfo(curUser.getComId(),curUser.getId());//获取用户更新后信息
			this.updateSessionUser(curUser);
			return view;
		}else{
			this.setNotification(Notification.ERROR, "验证码失效");
			return new ModelAndView("redirect:"+redirectPage);
		}
	}
	/**
	 * 发送验证码
	 * @param noticeType发送方式
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sendPassYzmByNewAccount")
	public Map<String, Object> sendPassYzmByNewAccount(String noticeType,String account){
		if(null==noticeType || "".equals(noticeType.trim())){
			Map<String, Object> map = new HashMap<String, Object>();
			//告知方式未指定
			map.put(ConstantInterface.TYPE_STATUS, "n");
			return map;
		}else{
			UserInfo curUser = this.getSessionUser();//获取当前操作人对象
			UserInfo userTemp = new UserInfo();
			BeanUtilEx.copyIgnoreNulls(curUser, userTemp);
			
			UserInfo sysUser = userInfoService.getUserInfoByAccount(account.toLowerCase());
			
			if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
				if(null==sysUser){//没有被绑定
				}else if(null==curUser.getMovePhone()){ //当前操作人员，没有设定手机号
					if(sysUser.getMovePhone().equals(account.toLowerCase())){//当前人员手机号与填写的相同
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(ConstantInterface.TYPE_STATUS, "f");// 电话号码不正确
						map.put(ConstantInterface.TYPE_INFO, "该手机号已被绑定，请重新填写。");
						return map;
					}
				}else{//已被绑定
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(ConstantInterface.TYPE_STATUS, "f");// 电话号码不正确
					map.put(ConstantInterface.TYPE_INFO, "该手机号已被绑定，请重新填写。");
					return map;
				}
			}else if(ConstantInterface.GET_BY_EMAIL.equals(noticeType.trim())){
				if(null==sysUser){//没有被绑定
				}else if(null==curUser.getEmail() ){//当前操作人员，没有设定手机号
					if(sysUser.getEmail().equals(account.toLowerCase())){//当前人员手机号与填写的相同
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(ConstantInterface.TYPE_STATUS, "f");// 电话号码不正确
						map.put(ConstantInterface.TYPE_INFO, "该邮箱已被绑定，请重新填写。");
						return map;
					}
				}else{//已被绑定
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(ConstantInterface.TYPE_STATUS, "f");// 电话号码不正确
					map.put(ConstantInterface.TYPE_INFO, "该邮箱已被绑定，请重新填写。");
					return map;
				}
			}
			
			if(ConstantInterface.GET_BY_PHONE.equals(noticeType.trim())){
				userTemp.setMovePhone(account);
			}else if(ConstantInterface.GET_BY_EMAIL.equals(noticeType.trim())){
				userTemp.setEmail(account);
			}
			Map<String, Object> map = userInfoService.doSendPassYzm(userTemp,noticeType);
			return map;
		}
	}
	
	/**
	 * 验证码
	 * @param param
	 * @param noticeType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkYzmByNewAccount/{requestURI}")
	public Map<String, Object> checkYzmByNewAccount(String param,String account) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(null==account || "".equals(account.trim())){
			this.setNotification(Notification.ERROR, "新绑定帐号为空了，请联系管理员。");
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "验证类型标识为空了，请联系管理员。");
			return map;
		}
		PassYzm passYzm = registService.getPassYzm(account);
		if(null==passYzm ){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "验证码失效！");

		}else if(param.equalsIgnoreCase(passYzm.getPassYzm())) {
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		} else {
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "验证码输入错误！");
		}
		return map;
	}
	
	/**
	 * 跳转直属上级配置页面
	 * @return
	 */
	@RequestMapping(value="/setDirectLeaderPage")
	public ModelAndView setDirectLeaderPage(){
		//获取后台验证码
		ModelAndView mav = new ModelAndView("/userInfo/edit/setDirectLeader");
		UserInfo curUser = this.getSessionUser();
		//获取个人直属上级集合
		List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(curUser);
		//生成任务参与人JSon字符串
		StringBuffer leaderJson = new StringBuffer("[");
		if(null!=listImmediateSuper && !listImmediateSuper.isEmpty()){
			for(ImmediateSuper vo:listImmediateSuper){
				leaderJson.append("{'userID':'"+vo.getLeader()+"','userName':'"+vo.getLeaderName()+"','gender':'"+vo.getGender()+"','uuid':'"+vo.getUuid()+"','fileName':'"+vo.getFileName()+"'},");	
			}
			leaderJson = new StringBuffer(leaderJson.substring(0,leaderJson.lastIndexOf(",")));
		}
		leaderJson.append("]");
		mav.addObject("leaderJson",leaderJson);
		return mav;
	}
	/**
	 * 弹窗设置用户手机号界面
	 * @return
	 */
	@RequestMapping(value="/updateUserMovePhonePage")
	public ModelAndView updateUserMovePhonePage(){
		ModelAndView mav = new ModelAndView("/userInfo/edit/updateUserMovePhone");
		return mav;
		
	}
	/**
	 * 设置用户手机号
	 * @param passYzm 验证码
	 * @param pageSource 来源页面
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateUserMovePhone")
	public Map<String,Object> updateUserMovePhone(String account,String yzm,String pageSource){
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(!CommonUtil.isPhone(account)){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "手机号格式不对");
			return map;
		}
		
		UserInfo curUser = this.getSessionUser();
		if(null==curUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO,CommonConstant.OFF_LINE_INFO);
			return map;
		}
		UserInfo userInfo = userInfoService.getUserInfoByAccount(account.toLowerCase());
		if(null==userInfo){//没有被绑定
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		}else if(null==curUser.getMovePhone()){ //当前操作人员，没有设定手机号
			if(userInfo.getMovePhone().equals(account)){//当前人员手机号与填写的相同
				map.put(ConstantInterface.TYPE_STATUS, "f");
				map.put(ConstantInterface.TYPE_INFO, "该手机号已被绑定，请重新填写");
			}else{
				map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			}
		}else{//已被绑定
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, "该手机号已被绑定，请重新填写");
		}
		
		if(map.get(ConstantInterface.TYPE_STATUS).equals(ConstantInterface.TYPE_STATUS_Y)){//账号有效
			PassYzm passYzm = registService.getPassYzm(account);
			if(null==passYzm ){
				map.put(ConstantInterface.TYPE_STATUS, "f");
				map.put(ConstantInterface.TYPE_INFO, "验证码失效！");
				
			}else if(yzm.equalsIgnoreCase(passYzm.getPassYzm())) {
				map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			} else {
				map.put(ConstantInterface.TYPE_STATUS, "f");
				map.put(ConstantInterface.TYPE_INFO, "验证码输入错误！");
			}
			
			if(map.get(ConstantInterface.TYPE_STATUS).equals(ConstantInterface.TYPE_STATUS_Y)){//验证码有效
				//删除验证码
				userInfoService.delPassYzmById(passYzm.getId());
				userInfoService.updateUserAccount(ConstantInterface.GET_BY_PHONE ,account,curUser);// 更新用户帐号
				curUser = userInfoService.getUserInfo(curUser.getComId(),curUser.getId());//获取用户更新后信息
				this.updateSessionUser(curUser);
				
			}
		}
		return map;
		
	}
	/**
	 * 设置人员编号
	 * @param userOrganic
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateEnrollNumber")
	public Map<String,Object> updateEnrollNumber(UserInfo userOrganic){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo curUser = this.getSessionUser();
		if(null==curUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO,CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//检查该编号是否占用
		UserOrganic checkUser = userInfoService.queryUserByEnrollNumber(curUser,userOrganic.getEnrollNumber());
		if(null != checkUser){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, "该编号已经使用,请检查人员编号！");

		}else{
			userInfoService.updateEnrollNumber(curUser, userOrganic);
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put(ConstantInterface.TYPE_INFO, "更新成功！");
		}
		return map;
		
	}
	
	/**
	 * 删除代理人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delForMeDo")
	public Map<String,Object> delForMeDo(){
		Map<String, Object> map =  new HashMap<>();
		UserInfo userInfo = this.getSessionUser();
		userInfoService.delForMeDo(userInfo);
		map.put("status", "y");
		return map;
	}
	/**
	 * 设置离岗人员的代理人员
	 * @param forMeDoId 代理人员主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateForMeDo")
	public Map<String,Object> updateForMeDo(Integer forMeDoId){
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = userInfoService.updateForMeDo(userInfo,forMeDoId);
		return map;
	}
	/**
	 * 查询是否是否为顶层领导（仅用于费用管理选择借款方式）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryTopLeader")
	public Map<String,Object> queryTopLeader(){
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = userInfoService.checkTopLeaderState(userInfo);
		return map;
	}
	
}
