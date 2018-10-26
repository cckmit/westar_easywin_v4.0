package com.westar.core.web.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.FormLayout;
import com.westar.base.model.FormMod;
import com.westar.base.model.FormModSort;
import com.westar.base.model.HelpType;
import com.westar.base.model.Orders;
import com.westar.base.model.Organic;
import com.westar.base.model.SysUpgradeLog;
import com.westar.base.model.SystemLog;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FileInfo;
import com.westar.base.pojo.FormModLayout;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.Encodes;
import com.westar.base.util.FileUtil;
import com.westar.base.util.PublicConfig;
import com.westar.base.util.StringUtil;
import com.westar.core.service.AttenceService;
import com.westar.core.service.BgypFlService;
import com.westar.core.service.DepartmentService;
import com.westar.core.service.FinancialService;
import com.westar.core.service.FormService;
import com.westar.core.service.OrderService;
import com.westar.core.service.OrganicService;
import com.westar.core.service.SysUpgradeLogService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.UploadService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.WebService;


/**
 * 网站控制器
 * @author lj
 *
 */
@Controller
@RequestMapping("/web")
public class WebController extends BaseController {
	
	/**
	 * 帐号
	 */
	private static String USERNAME ="admin";
	/**
	 * 密码
	 */
	private static String PASSWORD ="westarsoft";
	
	private static UserInfo sessionUser = new UserInfo();
	
	@Autowired
	WebService webService;
	
	@Autowired
	FormService formService;
	
	@Autowired
	OrganicService organicService;
	
	@Autowired
	SystemLogService systemLogService;
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	SysUpgradeLogService sysUpgradeLogService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	BgypFlService  bgypFlService;
	
	@Autowired
	OrderService  orderService;
	
	@Autowired
	FinancialService  financialService;
	
	@Autowired
	DepartmentService  departmentService;
	
	
	@Autowired
	AttenceService  attenceService;
	
	static{
		USERNAME = PublicConfig.COMPANY_ACCOUNT.get("userName");  
		PASSWORD = PublicConfig.COMPANY_ACCOUNT.get("password");  
		
		sessionUser.setId(1);
		sessionUser.setUserName("后台管理员");
		sessionUser.setComId(0);
		
		sessionUser.setDepId(0);
		sessionUser.setDepName("部门");
		
	}
	
	private static final Log log = LogFactory.getLog(WebController.class);
	
	/**
	 * 天转销售网站主页
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(){
		ModelAndView view = new ModelAndView("/web/index");
		return view;
	}
	
	/**
	 * 登录
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView login(){
		ModelAndView view = new ModelAndView("redirect:/login.jsp");
		return view;
	}
	
	/**
	 * 帮助列表
	 * @return
	 */
	@RequestMapping("/helpList")
	public ModelAndView helpList(){
		ModelAndView view = new ModelAndView("/web/helpList");
		return view;
	}
	
	/**
	 * 帮助详情
	 * @return
	 */
	@RequestMapping("/helpContent")
	public ModelAndView helpContent(){
		ModelAndView view = new ModelAndView("/web/help-content");
		List<HelpType> typeList = webService.listQus(null, null);
		view.addObject("typeList",typeList);
		return view;
	}
	/**
	 * 获取分类下的解答
	 * @param pId 分类主键
	 * @return
	 */
	@RequestMapping("/helpQA")
	public ModelAndView helpQA(Integer pId){
		ModelAndView view = null;
		if(null==pId || 0==pId){
			this.setNotification(Notification.ERROR,"分类主键不能为空。");
			view = new ModelAndView("/refreshSelf");
			view.addObject("redirectPage","/web/helpList");
			return view;
		}
		HelpType helpVo = webService.helpQA(pId);
		view = new ModelAndView("/web/helpQA");
		view.addObject("helpVo", helpVo);
		view.addObject("listHelp", helpVo.getAnsList());
		return view;
	}
	
	/**
	 * 帮助信息筛选
	 * @param nameCheck
	 * @return
	 */
	@RequestMapping("/helpQASreach")
	public ModelAndView helpQASreach(String nameCheck){
		ModelAndView view = null;
		List<HelpType> listHelp = webService.helpQASreach(nameCheck);
		view = new ModelAndView("/web/helpQA");
		view.addObject("listHelp", listHelp);
		return view;
	}

	/**
	 * 帮助简洁说明
	 * @return
	 */
	@RequestMapping("/helpDescrible")
	public ModelAndView helpDescrible(){
		ModelAndView view = new ModelAndView("/web/helpDescrible");
		return view;
	}
	
	/**
	 * 跳转帮助维护登录页面
	 * @return
	 */
	@RequestMapping("/toLogin")
	public ModelAndView toLogin() {
		ModelAndView view = new ModelAndView("/web/help/login");
		return view;
	}
	
	/**
	 * 维护登录
	 * @param loginName
	 * @param password
	 * @return
	 */
	@RequestMapping("/help/login")
	public ModelAndView login(String loginName,String password,HttpServletRequest request) {
		ModelAndView view = null;
		//管理帐号验证
		if ( USERNAME.equals(loginName.toLowerCase()) && Encodes.encodeMd5(PASSWORD).equals(Encodes.encodeMd5(password))) {
			view = new ModelAndView("/web/help/helpCenter");
			List<HelpType> list = webService.listQus(null,null);	
			view.addObject("list",list);
			return view;
		} else {
			this.setNotification(Notification.ERROR, "登录失败，请检查你的账号和密码。");
			view = new ModelAndView("/web/help/login");
			return view;
		}
	}
	
	/**
	 * 添加疑问
	 * @return
	 */
	@RequestMapping("/help/addQusBySimple")
	public ModelAndView addQusBySimple(Integer pId,HttpServletRequest request) {
		ModelAndView view = new ModelAndView("/web/help/addQusBySimple");
		pId=(null==pId?0:pId);
		int nextOrderNum = webService.nextOrderNum(pId);
		view.addObject("nextOrderNum",nextOrderNum);
		HelpType pHelpVo = webService.queryQus(pId);
		view.addObject("pHelpVo",pHelpVo);
		view.addObject("pId",pId);
		return view;
	}
	
	/**
	 * 新增疑问
	 * @param qus
	 * @return
	 */
	@RequestMapping("/help/addQus")
	public ModelAndView addQus(HelpType qus,HttpServletRequest request) {
		ModelAndView view = new ModelAndView("/refreshParentByUrl");
		view.addObject("redirectPage","/web/help/listQus?pId="+qus.getpId());
		webService.addQus(qus);
		return view;
	}
	
	/**
	 * 获取疑问列表
	 * @param nameCheck 名称比对
	 * @param pId
	 * @return
	 */
	@RequestMapping("/help/listQus")
	public ModelAndView listQus(String nameCheck,Integer pId,HttpServletRequest request){
		ModelAndView view = new ModelAndView("/web/help/helpCenter");
		List<HelpType> list = webService.listQus(nameCheck,pId);	
		view.addObject("list",list);
		view.addObject("nameCheck",nameCheck);
		view.addObject("pId",pId);
		return view;
	}
	/**
	 * 获取可以作为父级的疑问列表
	 * @param nameCheck 名称比对
	 * @param id 当前对象主键
	 * @return
	 */
	@RequestMapping("/help/listQusForP")
	public ModelAndView listQusForP(String nameCheck,Integer id,HttpServletRequest request){
		ModelAndView view = new ModelAndView("/web/help/listQusForP");
		List<HelpType> list = webService.listQusForP(nameCheck,id);	
		view.addObject("list",list);
		view.addObject("nameCheck",nameCheck);
		view.addObject("ownerKey",id);
		return view;
	}
	
	/**
	 * 异步获取新排序序号
	 * @param pId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/help/nextOrderNum")
	public int nextOrderNum(int pId) throws Exception{
		int nextOrderNum = webService.nextOrderNum(pId);
		return nextOrderNum;
	}
	
	/**
	 * 疑问解答详情
	 * @param qusId 疑问主键
	 * @return
	 */
	@RequestMapping("/help/queryQus")
	public ModelAndView queryQus(Integer qusId,HttpServletRequest request){
		ModelAndView view = new ModelAndView("/web/help/detailOfQus");
		HelpType helpVo = webService.queryQus(qusId);
		view.addObject("helpVo",helpVo);
		return view;
	}
	/**
	 * 帮助更新
	 * @param helpVo 数据参数
	 * @return
	 */
	@RequestMapping("/help/updateHelp")
	public ModelAndView updateHelp(HelpType helpVo,HttpServletRequest request){
		ModelAndView view = new ModelAndView("/refreshParentByUrl");
		view.addObject("redirectPage","/web/help/listQus?pId="+helpVo.getpId());
		if(null==helpVo || null==helpVo.getId() || 0==helpVo.getId()){
			this.setNotification(Notification.ERROR,"主键为空，更新失败。");
			return view;
		}
		webService.updateHelp(helpVo);
		this.setNotification(Notification.SUCCESS,"更新成功！");
		return view;
	}
	/**
	 * 
	 * 批量删除帮助
	 * @param ids 主键集合
	 * @param redirectPage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/help/delHelp")
	public ModelAndView delHelp(Integer[] ids,HttpServletRequest request,Integer pId) throws Exception {
		ModelAndView view = null;
		//添加日志
		if(null!=ids && ids.length>0){
			//删除任务
			webService.delHelp(ids);
		}
		this.setNotification(Notification.SUCCESS, "删除成功!");
		view = new ModelAndView("/refreshSelf");
		pId=(null==pId?0:pId);
		view.addObject("redirectPage","/web/help/listQus?pId="+pId);
		return view;
	}
	/**
	 * 跳转下载显示页面
	 * @param pId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/appDownPage")
	public ModelAndView appDown() throws Exception{
		ModelAndView view = new ModelAndView("/web/appDown");
		String appLoadUrl = CommonUtil.getAppLoadUrl();
		view.addObject("appLoadUrl", appLoadUrl);
		return view;
	}
	
	/**********************表单设计************************/
	
	
	
	/**
	 * 云表单列表
	 * @param helpVo
	 * @param request
	 * @return
	 */
	@RequestMapping("/form/listPagedCloudFormMod")
	public ModelAndView listPagedCloudFormMod(FormMod formMod,HttpServletRequest request){
		ModelAndView view = new ModelAndView("/web/help/helpCenter");
		List<FormMod> formModlist = formService.listPagedCloudFormMod(formMod);
		view.addObject("formModlist",formModlist);
		
		List<FormModSort> listFormSort = formService.listFormModSort(0);
		view.addObject("listFormSort",listFormSort);
		
		return view;
	}
	
	/**
	 * 克隆表单模板
	 * @param formModId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/form/addFormModClone")
	public Map<String,Object> addFormModClone(Integer formModId) {
		Map<String,Object> map = new HashMap<String, Object>();
		//克隆表单模板
		formService.addFormModClone(sessionUser,formModId);
		map.put("status", "y");
		//设置消息
		this.setNotification(Notification.SUCCESS, "克隆成功！");
		return map;
	}
	
	/**
	 * 编辑表单模板界面
	 * @return
	 */
	@RequestMapping(value="/form/addFormModPage")
	public ModelAndView addFormModPage(){
		ModelAndView view = new ModelAndView("/web/form/addFormMod");
		
		List<FormModSort> listFormSort = formService.listFormModSort(0);
		view.addObject("formSortList",listFormSort);
		return view;
	}
	
	/**
	 *添加模板表单
	 * @param formMod
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/form/addFormMod",method = RequestMethod.POST)
	public Map<String, Object> addFormMod(FormMod formMod){
		//设置团队号
		formMod.setComId(0);
		//默认不可用
		formMod.setEnable("0");
		//默认云表单主键
		formMod.setCloudModId(-1);
		//添加模板表单
		Integer formModId = formService.addFormMod(formMod);
		formMod.setId(formModId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "y");
		map.put("id",formModId);
		return map;
	}
	
	/**
	 * 编辑表单模板界面
	 * @return
	 */
	@RequestMapping(value="/form/editFormPage")
	public ModelAndView editFormPage(Integer formModId){
		ModelAndView view = new ModelAndView("/web/form/editForm");
		view.addObject("userInfo",sessionUser);
		//取得表单模板信息
		FormMod formMod = formService.getFormModbyId(formModId);
		view.addObject("formMod",formMod);
		return view;
	}
	/**
	 * 编辑表单模板界面
	 * @return
	 */
	@RequestMapping(value="/form/editFormDevPage")
	public ModelAndView editFormV2Page(){
		ModelAndView view = new ModelAndView("/sp/form/editFormDev");
		view.addObject("userInfo",sessionUser);
		
		view.addObject("flag","web");
		
		return view;
	}
	
	/**
	 * 恢复表单模板
	 * @param formModId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/form/backFormLayout")
	public Map<String,Object> backFormLayout(Integer formModId) {
		Map<String,Object> map = new HashMap<String, Object>();
		//恢复表单模板
		map  = formService.updateBackFormLayout(sessionUser,formModId);
		if(map.get("status").equals("y")){
			//设置消息
			this.setNotification(Notification.SUCCESS, "恢复成功！");
		}
		return map;
	}
	
	/**
	 * 取得布局的基本信息
	 * @param formModId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/form/checkFormState")
	public Map<String,Object> checkFormState(Integer formModId){
		
		Map<String, Object> map = new HashMap<String, Object>();
		FormLayout formLayout = formService.queryFormLayoutState(sessionUser, formModId, null);
		map.put("status", "y");
		map.put("formLayout", formLayout);
		return map;
	}
	/**
	 * 查询表单布局
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/form/findFormComp")
	public FormMod findFormComp(FormMod formMod){
		UserInfo userInfo = new UserInfo();
		userInfo.setComId(formMod.getComId());
		Integer formId =formMod.getId();
		//查询表单布局
		FormLayout formLayout = formService.getFormLayoutByModId(formId,userInfo);
		formMod.setFormLayout(formLayout);
		return formMod;
	}
	
	/**
	 * 查询表单布局
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/form/findFormCompDev")
	public FormMod findFormCompDev(FormMod formMod){
		Integer formId =formMod.getId();
		
		//取得表单模板信息
		String modName = formService.getFormModbyId(formId).getModName();
		formMod.setModName(modName);
		//查询表单布局
		FormLayout formLayout = formService.queryLayoutByModIdDev(formId,sessionUser);
		formMod.setFormLayout(formLayout);
		return formMod;
	}
	
	/**
	 * 添加模板表单控件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/form/editFormMod")
	public Map<String, Object> editForm(FormModLayout formModLayout){
		Map<String, Object> map = new HashMap<String, Object>();
		//修改表单组件
		formService.updateFormCompon(sessionUser,formModLayout);
		this.setNotification(Notification.SUCCESS, "操作成功！");
		return map;
	}
	/**
	 * 表单控件信息修改
	 * @param formModLayout
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/form/editFormModDev")
	public Map<String, Object> editFormModDev(FormModLayout formModLayout){
		Map<String, Object> map = new HashMap<String, Object>();
		
		formService.updateFormComponDev(sessionUser,formModLayout);
	
		this.setNotification(Notification.SUCCESS, "操作成功！");
		return map;
	}
	
	
	
	/**
	 * 跳转反馈类型添加页面
	 * 
	 * @param feedBackType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/form/ajaxGetFormModSortOrder")
	public Map<String, Object> ajaxGetFormModSortOrder() {

		Map<String, Object> map = new HashMap<String, Object>();
		Integer orderNum = formService.queryFormModSortOrderMax(0);
		// 取出 排序号
		map.put("orderNum", orderNum);

		map.put("status", "y");
		return map;
	}
	/**
	 * 添加表单模板分类
	 * @param formSort
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/form/addFormModSort")
	public Map<String,Object> addFormSort(FormModSort formSort){
		Map<String,Object> map = new HashMap<String, Object>();
		formSort.setComId(0);
		Integer id = formService.addFormModSort(formSort);
		formSort.setId(id);
		map.put("status", "y");
		map.put("formSort", formSort);
		return map;
	}
	
	
	/**
	 * 跳转表单分类维护界面
	 * @return
	 */
	@RequestMapping(value="/form/editFormModSortPage")
	public ModelAndView listFormModSortPage(){
		ModelAndView mav = new ModelAndView("/web/form/editFormModSort");
		return mav;
		
	}
	
	/**
	 * 添加表单模板分类
	 * @param formSort
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/form/ajaxListFormModSort")
	public Map<String,Object> ajaxListFormModSort(FormModSort formSort){
		Map<String,Object> map = new HashMap<String, Object>();
		List<FormModSort> listFormSort = formService.listFormModSort(0);
		map.put("status", "y");
		map.put("listFormSort", listFormSort);
		return map;
	}
	/**
	 * 修改表单类型属性
	 * @param formModSort
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/form/updateFormModSort")
	public Map<String,Object> updateFormModSort(FormModSort formModSort)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		//修改表单类型属性
		formService.updateFormModSort(formModSort);
		map.put("status", "y");
		return map;
	}
	/**
	 * 修改表单类型属性
	 * @param formModSort
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/form/delFormModSort")
	public Map<String,Object> delFormModSort(Integer formSortId)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		
		boolean falg = formService.delFormModSort(formSortId, sessionUser);
		if(falg){
			map.put("status", "y");
		}else{
			map.put("status", "f");
			map.put("info", "表单不存在");
		}
		return map;
	}
	
	/**
	 * 修改表单所属分类以及名称
	 * @param formMod
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/form/ajaxUpdateFormMod")
	public Map<String,Object> ajaxUpdateFormMod(FormMod formMod)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		
		//验证表单是否同名
		List<FormMod> formMods = formService.checkFormModName(0,formMod.getModName());
		if(null==formMods || formMods.isEmpty() || formMods.size()==1){//判断是否有重名
			//重名的是自己的
			if(formMods.size()==1 && !formMod.getId().equals(formMods.get(0).getId())){
				map.put("status", "f1");
				map.put("info", "已有同名表单,请重新命名");
			}else{
				formService.updateFormMod(formMod);
				map.put("status", "y");
			}
		}else{
			map.put("status", "f1");
			map.put("info", "已有同名表单,请重新命名");
		}
		return map;
	}
	
	/**
	 * 修改表单启用状态
	 * @param formMod
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/form/updateFormModEnabled")
	public Map<String,Object> updateFormModEnabled(FormMod formMod){
		Map<String,Object> map = new HashMap<String, Object>();
		//修改表单启用状态
		formService.updateFormModEnabled(formMod,null);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 取得单个临时主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/form/generatorId")
	public Map<String,Object> generatorId(){
		Map<String,Object> map = new HashMap<String, Object>();
		Long time = System.currentTimeMillis();
		map.put("generatorId", time);
		return map;
	}
	
	
	
	/**********************团队统计************************/
	
	
	/**
	 * 获取企业信息列表
	 * @param organic
	 * @param request
	 * @return
	 */
	@RequestMapping("/count/listPagedOrgCount")
	public ModelAndView listPagedOrgCount(Organic organic,HttpServletRequest request){
		ModelAndView view = new ModelAndView("/web/help/helpCenter");
		List<Organic> listOrganic = organicService.listPagedOrganic(organic);
		//统计企业人数
		for(Organic o:listOrganic){
			o.setMembers(userInfoService.countUsers(o.getOrgNum()));
		}
		
		view.addObject("listOrganic", listOrganic);
		return view;
	}
	/**
	 * 初始化部门树形关系
	 * @return
	 */
	@RequestMapping("/initDepTree")
	public ModelAndView initDepTree(Integer comId){
		List<Organic> listOrganic = organicService.listAllOrganic();
		
		boolean pointCom = (null!=comId && comId>0);
		//统计企业人数
		for(Organic o:listOrganic){
			if(pointCom){
				if(o.getOrgNum().equals(comId)){
					departmentService.initDepTree(o.getOrgNum());
					log.info("["+o.getOrgNum()+"]初始化部门树形关系结束...");
				}
			}else{
				departmentService.initDepTree(o.getOrgNum());
				log.info("["+o.getOrgNum()+"]初始化部门树形关系结束...");
			}
		}
		log.info("初始化部门树形关系结束...");
		ModelAndView view = new ModelAndView("redirect:/login.jsp");
		return view;
	}
	
	/**
	 * 初始化人员树形关系
	 * @return
	 */
	@RequestMapping("/initUserTree")
	public ModelAndView initUserTree(Integer comId){
		List<Organic> listOrganic = organicService.listAllOrganic();
		
		boolean pointCom = (null!=comId && comId>0);
		//统计企业人数
		for(Organic o:listOrganic){
			if(pointCom){
				if(o.getOrgNum().equals(comId)){
					userInfoService.initUserTree(o.getOrgNum());
					log.info("["+o.getOrgNum()+"]初始化人员树形关系结束...");
				}
			}else{
				userInfoService.initUserTree(o.getOrgNum());
				log.info("["+o.getOrgNum()+"]初始化人员树形关系结束...");
			}
		}
		log.info("初始化人员树形关系结束...");
		ModelAndView view = new ModelAndView("redirect:/login.jsp");
		return view;
	}
	
	/**
	 * 初始化系统的请假和加班到天数
	 * @return
	 */
	@RequestMapping("/initAttence")
	public ModelAndView initAttence(Integer comId){
		List<Organic> listOrganic = organicService.listAllOrganic();
		
		boolean pointCom = (null != comId && comId > 0);
		//统计企业人数
		for(Organic o:listOrganic){
			if(pointCom){
				if(o.getOrgNum().equals(comId)){
					attenceService.initAttence(o.getOrgNum());
				}
			}else{
				attenceService.initAttence(o.getOrgNum());
			}
		}
		log.info("初始化系统的请假和加班到天数结束...");
		ModelAndView view = new ModelAndView("redirect:/login.jsp");
		return view;
	}
	
	/**
	 * 初始化系统的请假和加班到天数
	 * @return
	 */
	@RequestMapping("/initAttenceDetail")
	public ModelAndView initAttenceDetail(Integer comId){
		List<Organic> listOrganic = organicService.listAllOrganic();
		
		boolean pointCom = (null != comId && comId > 0);
		//统计企业人数
		for(Organic o:listOrganic){
			if(pointCom){
				if(o.getOrgNum().equals(comId)){
					attenceService.initAttenceDetail(o.getOrgNum());
				}
			}else{
				attenceService.initAttenceDetail(o.getOrgNum());
			}
		}
		log.info("初始化系统的请假和加班到天数结束...");
		ModelAndView view = new ModelAndView("redirect:/login.jsp");
		return view;
	}
	
	
	
	/**
	 * 获取该企业信息和注册人信息
	 * @param organic
	 * @param request
	 * @return
	 */
	@RequestMapping("/count/showOrganicInfo")
	public ModelAndView showOrganicInfo(Integer orgNum,Integer userId){
		Organic organic = organicService.getOrgInfo(orgNum);
		UserInfo userInfo = userInfoService.getUserInfo(organic.getOrgNum(),userId);
		
		ModelAndView view = new ModelAndView("/web/count/organicInfo");
		view.addObject("organic", organic);
		view.addObject("userInfo",userInfo);
		return view;
	}
	
	/**
	 * 获取该企业的日志信息
	 * @param organic
	 * @param request
	 * @return
	 */
	@RequestMapping("/count/listPagedOrgSysLog")
	public ModelAndView listPagedOrgSysLog(Integer orgNum,SystemLog systemLog){
		ModelAndView view = new ModelAndView("/web/count/listPagedOrgSysLog");
		//设置企业号
		systemLog.setComId(orgNum);
		//查询日志
		List<SystemLog> list = systemLogService.listPagedOrgSysLog(systemLog);
		view.addObject("list", list);
		view.addObject("orgNum", orgNum);
		
		return view;
	} 
	
	/**********************系统升级日志************************/
	
	
	/**
	 *跳转系统升级日志显示界面
	 */
	@RequestMapping("/upLog/toListSysUpLog")
	public ModelAndView toListSysUpLog(){
		ModelAndView view = new ModelAndView("/web/help/helpCenter");
		return view;
	} 
	/**
	 * 系统升级日志按类型查询
	 * @param type 平台类型
	 * @return
	 */
	@RequestMapping("/upLog/listSysUpLog")
	public ModelAndView listSysUpLog(Integer type){
		ModelAndView view = new ModelAndView("/web/upLog/listUpLogForType");
		List<SysUpgradeLog> listUpLog = sysUpgradeLogService.listSysUpLog(null,type);
		//保存到数据库的更新 内容转换成HTML显示
		for(SysUpgradeLog s :listUpLog){
			s.setContent(StringUtil.toHtml(s.getContent()));
		}
		view.addObject("listUpLog",listUpLog);
		return view;
	} 
	/**
	 * 系统升级日志按类型滚动查询
	 * @param type 平台类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listNextSysUpLog")
	public Map<String, Object> listNextSysUpLog(Integer maxId, Integer type){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<SysUpgradeLog> listUpLog = sysUpgradeLogService.listSysUpLog(maxId,type);
		//保存到数据库的更新 内容转换成HTML显示
		for(SysUpgradeLog s :listUpLog){
			s.setContent(StringUtil.toHtml(s.getContent()));
		}
		map.put("status", "y");
		map.put("listUpLog",listUpLog);
		
		return map;
	} 
	/**
	 * 跳转到订单信息列表界面
	 * @param orders
	 * @return
	 */
	@RequestMapping("/orders/listPagedWebOrder")
	public ModelAndView listPagedWebOrder(Orders orders){
		ModelAndView view = new ModelAndView("/web/help/helpCenter");
		List<Orders>  listOrders = orderService.listPagedWebOrder(orders);
		view.addObject("listOrders", listOrders);
		return view;
	}
	
	/**
	 * 查看订单
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/order/webOrderDetail")
	public ModelAndView webOrderDetail (Integer orderId){
		ModelAndView view = new ModelAndView("/web/orders/webOrderDetail");
		UserInfo userInfo = this.getSessionUser();
		Orders order = orderService.queryOrderById(null,orderId);//获取订单记录
		view.addObject("order",order);
		return view;
	}
	
	
	/**
	 * 跳转添加日志界面
	 * @return
	 */
	@RequestMapping("/upLog/toAddUpgradeLog")
	public ModelAndView toAddUpgradeLog(){
		ModelAndView view = new ModelAndView("/web/upLog/addUpgradeLog");
		
		InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream("android_version.xml");
		SAXReader reader = new SAXReader();
		reader.setEncoding("GB2312");
		org.dom4j.Document document;
		try {
			document = reader.read(inputStream);
			//读取节点信息
			Node version=document.selectSingleNode("//info/version");
			Node versionName=document.selectSingleNode("//info/versionName");
			//设值
			Integer vsion = Integer.parseInt(version.getStringValue())+1;
			String vsionName = versionName.getStringValue();
			view.addObject("version",vsion);
			view.addObject("versionName",vsionName);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return view;
	} 
	
	/**
	 *  添加系统升级日志
	 * @param upLog 系统升级日志
	 * @return
	 */
	@RequestMapping("/upLog/addUpgradeLog")
	public ModelAndView addUpgradeLog(SysUpgradeLog upLog,String tempFilePath){
		
		if(upLog.getTerraceType() == 2){//安卓
			//APP重命名路径
			String newPath = FileUtil.getUploadAppBasePath()+File.separator+"easywin-"+DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd)+".apk";
			if(FileUtil.renameOldApp(newPath)){
				if(FileUtil.renameApp(tempFilePath)){
					//修改android_version.xml文件
					String andoridPath=this.getClass().getClassLoader().getResource("android_version.xml").getPath();
					try
					{
					/**
					 * 获取对象
					 */
					SAXReader sax=new SAXReader();//创建一个SAXReader对象  
					sax.setEncoding("GB2312");
					File xmlFile=new File(andoridPath);//根据指定的路径创建file对象  
					org.dom4j.Document document=sax.read(xmlFile);//获取document对象,如果文档无节点，则会抛出Exception提前结束  
					Element root=document.getRootElement();//获取根节点  
					/**
					 * 修改
					 */
					//获取指定名字的节点，无此节点的会报NullPointerException,时间问题不做此情况的判断与处理了  
				    Element version =root.element("version");  
				    Element versionName =root.element("versionName");  
				    Element description =root.element("description"); 
				    //修改节点内容
				    // String descriptionXml = upLog.getContent().replaceAll("\n", "\n\t\t");
				    //descriptionXml = descriptionXml.replaceAll("\r\n", "\n\t\t");
				    
				    version.setText(upLog.getVersion());
				    versionName.setText(upLog.getVersionName());
				    description.setText(upLog.getContent());
				    /**
				     * 保存
				     */
				    Writer osWrite=new OutputStreamWriter(new FileOutputStream(xmlFile));//创建输出流  
				    OutputFormat format = OutputFormat.createPrettyPrint();  //获取输出的指定格式    
				    format.setEncoding("UTF-8");//设置编码 ，确保解析的xml为UTF-8格式  
				    //处理字符串换行
				    format.setIndentSize(2);	//缩进
			        format.setNewlines(true); //设置是否换行
		            format.setTrimText(false);   
		            format.setPadText(true);  
		            
				    XMLWriter writer = new XMLWriter(osWrite,format);//XMLWriter 指定输出文件以及格式    
				    writer.write(document);//把document写入xmlFile指定的文件
				    writer.flush();  
				    writer.close();  
				    
				    //文件操作成功 添加日志
					sysUpgradeLogService.addUpgradeLog(upLog);
				    this.setNotification(Notification.SUCCESS, "添加成功!");
					}catch (Exception e) {
						//还原新版本APP
						FileUtil.renameOldApp(tempFilePath);
						//还原旧版本APP
						FileUtil.renameApp(newPath);
						this.setNotification(Notification.ERROR, "修改配置文件出错!");
						e.printStackTrace();
					}
				}else{
					//还原旧版本APP
					FileUtil.renameApp(newPath);
					this.setNotification(Notification.ERROR, "添加失败,文件操作出错!");
				}
			}else{
				this.setNotification(Notification.ERROR, "添加失败,文件操作出错!");
			} 
		}else{//其他
			sysUpgradeLogService.addUpgradeLog(upLog);
			this.setNotification(Notification.SUCCESS, "添加成功!");
		}
		
		ModelAndView view = new ModelAndView("/refreshParent");
		return view;
	}
	
	/**
	 * 百度控件上传App
	 * @param status
	 * @param fileInfo
	 *            文件参数
	 * @param request
	 * @param fileVal 
	 * 		    
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addDepartApp", method = RequestMethod.POST)
	public Map<String, Object> addDepartApp(String status, FileInfo fileInfo,
			HttpServletRequest request, String fileVal) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (null == status || "".equals(status)) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile(fileVal);

			if (file != null && !file.isEmpty()) {
				
				// app存放的临时文件夹路径
				String baseTempPath = FileUtil.getUploadAppTempPath()
						+ File.separator;
				File parentDir = new File(baseTempPath);
				//不存在 创建
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}

				// 临时文件的路径
				String tempFilePath = baseTempPath + File.separator+ fileInfo.getName();

				File tempPartFile = new File(tempFilePath);
				FileUtils.copyInputStreamToFile(file.getInputStream(), tempPartFile);
				map.put("tempFilePath", tempFilePath);
			}
			map.put("status", "y");
			return map;
		}else if(status.equals("del")){
			//取消上传,删除文件
			
			// app存放的临时文件夹路径
			String baseTempPath = FileUtil.getUploadAppTempPath();
			
			FileUtils.deleteDirectory(new File(baseTempPath));
			map.put("status", "y");
			return map;
		}
		map.put("status", "y");
		return map;
		
	}
	
	/**
	 * 初始化办公用品分类
	 * @return
	 */
	@RequestMapping(value = "/initSysBgFl")
	public ModelAndView initSysBgFl(){
		List<Organic> organics = organicService.listAllOrganic();
		log.info("初始化办公用品分类开始...");
		if(null!=organics && !organics.isEmpty()){
			for (Organic organic : organics) {
				try {
					bgypFlService.checkBgFl4Init(organic.getOrgNum());
				} catch (DocumentException e) {
					log.error("数据解析错误");
					e.printStackTrace();
				} catch (IOException e) {
					log.error("文件读取错误");
					e.printStackTrace();
				}
			}
		}
		log.info("初始化办公用品分类结束...");
		ModelAndView view = new ModelAndView("redirect:/login.jsp");
		return view;
	}
	
	
	
//	/**
//	 * 
//	 * @return
//	 */
//	@RequestMapping(value = "/initFeeBudget")
//	public ModelAndView initFeeBudget(){
//		
//		Integer comId= 411867;
//		Map<String,Integer> map = new HashMap<String,Integer>();
//		map.put(ConstantInterface.MENU_FEE_APPLY, 0);
//		map.put(ConstantInterface.MENU_LOAN, 0);
//		map.put(ConstantInterface.MENU_LOANOFF, 0);
//		
//		log.info("初始化预算开始...");
//		this.initFeeBudgetData(comId,map);
//		log.info("初始化预算结束...");
//		
//		System.out.println(map);
//		
//		log.info("初始化借款开始...");
//		this.initFeeLoanData(comId,map);
//		log.info("初始化借款结束...");
//		
//		System.out.println(map);
//		
//		log.info("初始化报销开始...");
//		this.initFeeLoanOffData(comId,map);
//		log.info("初始化报销结束...");
//		
//		System.out.println(map);
//		
//		ModelAndView view = new ModelAndView("redirect:/login.jsp");
//		return view;
//	}
//
//	private void initFeeLoanOffData(Integer comId, Map<String, Integer> map) {
//		Integer pageNum = 0;
//		Integer pageSize = 100;
//		PaginationContext.setPageSize(pageSize);
//		PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//		
//		List<FeeLoanOff> feeLoanOffs = financialService.listPagedFeeLoanOffForInit(comId);
//		Integer count = PaginationContext.getTotalCount();
//		if(null!=feeLoanOffs && !feeLoanOffs.isEmpty()){
//			financialService.initFeeLoanOffData(feeLoanOffs,map);
//		}
//		count = count - pageSize;
//		Integer left = count % pageSize;
//		Integer totalPage;
//		if(left>0){
//			totalPage = (count - left)/100 + 1;
//		}else{
//			totalPage = count/100;
//		}
//		
//		for(int i=0;i<totalPage;i++){
//			pageNum ++ ;
//			//一次加载行数
//			PaginationContext.setPageSize(pageSize);
//			//列表数据起始索引位置
//			PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//			log.info("初始化分享开始..."+pageNum*PaginationContext.getPageSize());
//			
//			feeLoanOffs = financialService.listPagedFeeLoanOffForInit(comId);
//			if(null!=feeLoanOffs && !feeLoanOffs.isEmpty()){
//				financialService.initFeeLoanOffData(feeLoanOffs,map);
//			}
//		}
//		
//	}
//
//	private void initFeeLoanData(Integer comId, Map<String, Integer> map) {
//		Integer pageNum = 0;
//		Integer pageSize = 100;
//		PaginationContext.setPageSize(pageSize);
//		PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//		
//		List<FeeLoan> feeLoans = financialService.listPagedFeeLoanForInit(comId);
//		Integer count = PaginationContext.getTotalCount();
//		if(null!=feeLoans && !feeLoans.isEmpty()){
//			financialService.initFeeLoanData(feeLoans,map);
//		}
//		count = count - pageSize;
//		Integer left = count % pageSize;
//		Integer totalPage;
//		if(left>0){
//			totalPage = (count - left)/100 + 1;
//		}else{
//			totalPage = count/100;
//		}
//		
//		for(int i=0;i<totalPage;i++){
//			pageNum ++ ;
//			//一次加载行数
//			PaginationContext.setPageSize(pageSize);
//			//列表数据起始索引位置
//			PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//			log.info("初始化分享开始..."+pageNum*PaginationContext.getPageSize());
//			
//			feeLoans = financialService.listPagedFeeLoanForInit(comId);
//			
//			if(null!=feeLoans && !feeLoans.isEmpty()){
//				financialService.initFeeLoanData(feeLoans,map);
//			}
//		}
//	}

//	/**
//	 * 初始化
//	 * @param map 
//	 */
//	private void initFeeBudgetData(Integer comId, Map<String, Integer> map) {
//		
//		Integer pageNum = 0;
//		Integer pageSize = 100;
//		PaginationContext.setPageSize(pageSize);
//		PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//		
//		List<FeeBudget> feeBudgets = financialService.listPagedFeeBudgetForInit(comId);
//		Integer count = PaginationContext.getTotalCount();
//		if(null!=feeBudgets && !feeBudgets.isEmpty()){
//			financialService.initFeeBudgetData(feeBudgets,map);
//		}
//		count = count - pageSize;
//		Integer left = count % pageSize;
//		Integer totalPage;
//		if(left>0){
//			totalPage = (count - left)/100 + 1;
//		}else{
//			totalPage = count/100;
//		}
//		
//		for(int i=0;i<totalPage;i++){
//			pageNum ++ ;
//			//一次加载行数
//			PaginationContext.setPageSize(pageSize);
//			//列表数据起始索引位置
//			PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//			log.info("初始化分享开始..."+pageNum*PaginationContext.getPageSize());
//			
//			feeBudgets = financialService.listPagedFeeBudgetForInit(comId);
//			
//			if(null!=feeBudgets && !feeBudgets.isEmpty()){
//				financialService.initFeeBudgetData(feeBudgets,map);
//			}
//		}
//		
//	}
	
//	/**
//	 * 初始化分享信息到分享
//	 * @return
//	 */
//	@RequestMapping(value = "/initDailyReport")
//	public ModelAndView initDailyReport(){
//		List<Organic> organics = organicService.listAllOrganic();
//		log.info("初始化分享开始...");
//		if(null!=organics && !organics.isEmpty()){
//			for (Organic organic : organics) {
//				Integer pageNum = 0;
//				Integer pageSize = 100;
//				
//				//人员日期对应分享主键
//				Map<String, Integer> user_date_dailyIdMap = new HashMap<String, Integer>();
//				//分享主键对应问题主键
//				Map<Integer, Integer> daily_QIdMap = new HashMap<Integer, Integer>();
//				//分享主键对应分享内容
//				Map<Integer, DailyVal> dailyId_contentMap = new HashMap<Integer,DailyVal>();
//				//一次加载行数
//				PaginationContext.setPageSize(pageSize);
//				//列表数据起始索引位置
//				PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//				
//				//分页查询需要初始化的分享信息
//				List<MsgShare> msgShares = msgShareService.listPagedMsgForDaily(organic.getOrgNum());
//				Integer count = PaginationContext.getTotalCount();
//				if(null!=msgShares && !msgShares.isEmpty()){
//					//取得自动归档的文件夹
//					dailyService.addDailyReportbyInit(msgShares,user_date_dailyIdMap,daily_QIdMap,dailyId_contentMap);
//				}
//				count = count - pageSize;
//				Integer left = count % pageSize;
//				Integer totalPage;
//				if(left>0){
//					totalPage = (count - left)/100 + 1;
//				}else{
//					totalPage = count/100;
//				}
//				
//				for(int i=0;i<totalPage;i++){
//					pageNum ++ ;
//					//一次加载行数
//					PaginationContext.setPageSize(pageSize);
//					//列表数据起始索引位置
//					PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//					log.info("初始化分享开始..."+pageNum*PaginationContext.getPageSize());
//					
//					msgShares = msgShareService.listPagedMsgForDaily(organic.getOrgNum());
//					
//					if(null!=msgShares && !msgShares.isEmpty()){
//						//取得自动归档的文件夹
//						dailyService.addDailyReportbyInit(msgShares,user_date_dailyIdMap,daily_QIdMap,dailyId_contentMap);
//					}
//				}
//			}
//		}
//		log.info("初始化分享结束...");
//		ModelAndView view = new ModelAndView("redirect:/login.jsp");
//		return view;
//	}
	
//	/**
//	 * 初始化日报到分享中
//	 * @return
//	 */
//	@RequestMapping(value = "/initDailyToMsgShare")
//	public ModelAndView initDailyToMsgShare(){
//		List<Organic> organics = organicService.listAllOrganic();
//		log.info("初始化分享开始...");
//		if(null!=organics && !organics.isEmpty()){
//			for (Organic organic : organics) {
//				Integer pageNum = 0;
//				Integer pageSize = 100;
//				
//				List<Daily> listDaily =  dailyService.listPagedDailyForMsg(organic.getOrgNum());
//				Integer count = PaginationContext.getTotalCount();
//				if(null!=listDaily && !listDaily.isEmpty()){
//					//取得自动归档的文件夹
//					msgShareService.addMsgShareByDaily(listDaily);
//				}
//				count = count - pageSize;
//				Integer left = count % pageSize;
//				Integer totalPage;
//				if(left>0){
//					totalPage = (count - left)/100 + 1;
//				}else{
//					totalPage = count/100;
//				}
//				for(int i=0;i<totalPage;i++){
//					pageNum ++ ;
//					//一次加载行数
//					PaginationContext.setPageSize(pageSize);
//					//列表数据起始索引位置
//					PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//					log.info("初始化分享开始..."+pageNum*PaginationContext.getPageSize());
//					
//					listDaily =  dailyService.listPagedDailyForMsg(organic.getOrgNum());
//					
//					if(null!=listDaily && !listDaily.isEmpty()){
//						//取得自动归档的文件夹
//						msgShareService.addMsgShareByDaily(listDaily);
//					}
//				}
//				
//			}
//		}
//		log.info("初始化分享结束...");
//		ModelAndView view = new ModelAndView("redirect:/login.jsp");
//		return view;
//	}
	
//	/**
//	 * 用于客户联系人信息导入外部联系人表
//	 * 并添加外部联系人与客户的关系
//	 * @throws InvocationTargetException 
//	 * @throws IllegalAccessException 
//	 */
//	@RequestMapping("/initOutLinkMan")
//	public ModelAndView initOutLinkMan() throws IllegalAccessException, InvocationTargetException {
//		
//		List<Organic> organics = organicService.listAllOrganic();
//		log.info("初始化外部联系人开始...");
//		if(null!=organics && !organics.isEmpty()){
//			
//			for (Organic organic : organics) {
//				Integer pageNum = 0;
//				Integer pageSize = 100;
//				
//				//一次加载行数
//				PaginationContext.setPageSize(pageSize);
//				
//				//分享主键对应问题主键
//				Map<Integer, Integer> crmId_userIdMap = new HashMap<Integer, Integer>();
//				
//				//列表数据起始索引位置
//				PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//				
//				List<LinkMan> linkMans = outLinkManService.listPagedLinkMan(organic.getOrgNum());
//				Integer count = PaginationContext.getTotalCount();
//				if(null!=linkMans && !linkMans.isEmpty()){
//					outLinkManService.initOutLinkMan(linkMans,crmId_userIdMap);
//				}
//				
//				count = count - pageSize;
//				Integer left = count % pageSize;
//				Integer totalPage = 0;
//				if(left>0){
//					totalPage = (count - left)/100 + 1;
//				}else{
//					totalPage = count/100;
//				}
//				
//				for(int i=0;i<totalPage;i++){
//					pageNum ++ ;
//					//一次加载行数
//					PaginationContext.setPageSize(pageSize);
//					//列表数据起始索引位置
//					PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//					log.info("初始化联系人开始..."+pageNum*PaginationContext.getPageSize());
//					
//					linkMans = outLinkManService.listPagedLinkMan(organic.getOrgNum());
//					
//					if(null!=linkMans && !linkMans.isEmpty()){
//						outLinkManService.initOutLinkMan(linkMans,crmId_userIdMap);
//					}
//				}
//			}
//		}
//		log.info("初始化联系人结束...");
//		ModelAndView view = new ModelAndView("redirect:/login.jsp");
//		return view;
//	}
	
	
	
}
