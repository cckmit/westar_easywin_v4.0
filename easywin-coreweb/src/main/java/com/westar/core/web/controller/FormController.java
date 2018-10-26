package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.FormCompon;
import com.westar.base.model.FormLayout;
import com.westar.base.model.FormMod;
import com.westar.base.model.FormModSort;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FormModLayout;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.TableInfoVo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.FormService;

@Controller
@RequestMapping("/form")
public class FormController extends BaseController{

	@Autowired
	FormService formService;
	
	/**
	 * 到填写表单列表界面(列出用户可以填写的表单，后期需修改存位置)
	 * @param formMod
	 * @return
	 */
	@RequestMapping(value="/formFlowList")
	public ModelAndView formFlowList(FormMod formMod){
		ModelAndView view = new ModelAndView("/sp/form/formCenter");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		//分页查询表单信息
		List<FormMod> formModlist = formService.listPagedUserFormMod(formMod,userInfo);
		view.addObject("formModlist",formModlist);
		return view;
	}
	/**
	 * 到表单模板列表界面（用户表单模板编辑列表）
	 * @param formMod
	 * @return
	 */
	@RequestMapping(value="/formModList")
	public ModelAndView formModList(FormMod formMod,String activityMenu){
		ModelAndView view = new ModelAndView("/sp/sp_center");
		view.addObject("activityMenu",activityMenu);
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		//分页查询表单信息
		List<FormMod> formModlist = formService.listPagedFormMod(formMod,userInfo);
		view.addObject("formModlist",formModlist);
		
		List<FormModSort> listFormSort = formService.listFormModSort(userInfo.getComId());
		view.addObject("listFormSort",listFormSort);
		view.addObject("formMod",formMod);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_FLOW_SP);
		return view;
	}
	
	/**
	 * 跳转团队表单选择页面
	 * @param formMod
	 * @return
	 */
	@RequestMapping(value="/formListForSelect")
	public ModelAndView formListForSelect(FormMod formMod){
		ModelAndView view = new ModelAndView("/sp/form/formListForSelect");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		//分页查询表单信息
		List<FormMod> formModlist = formService.listPagedFormMod(formMod,userInfo);
		view.addObject("formModlist",formModlist);
		
		List<FormModSort> listFormSort = formService.listFormModSort(userInfo.getComId());
		view.addObject("listFormSort",listFormSort);
		view.addObject("formMod",formMod);
		return view;
	}
	
	/**
	 * 跳转表单页面页面
	 * @param formMod
	 * @return
	 */
	@RequestMapping(value="/formModListForSelect")
	public ModelAndView formModListForSelect(FormMod formMod){
		ModelAndView view = new ModelAndView("/sp/form/formModListForRadioSelect");
		UserInfo userInfo = this.getSessionUser();
		List<FormMod> formModlist = formService.listPagedFormMod(formMod,userInfo);//分页查询表单信息
		view.addObject("formModlist",formModlist);
		view.addObject("userInfo",userInfo);
		view.addObject("formModId",formMod.getId());
		List<FormModSort> listFormSort = formService.listFormModSort(userInfo.getComId());
		view.addObject("listFormSort",listFormSort);
		return view;
	}
	
	/**
	 * 
	 * 表单控件候选列表页面
	 * @param formModId 表单主键
	 * @param flowId 流程主键
	 * @param stepId 步骤主键
	 * @return
	 */
	@RequestMapping(value="/formComponListForSelect")
	public ModelAndView formControlListForSelect(Integer formModId,Integer flowId,Integer stepId){
		ModelAndView view = new ModelAndView("/sp/form/formComponListForSelect");
		UserInfo userInfo = this.getSessionUser();
		List<FormCompon> formComponlist = formService.listAllFormCompont(userInfo.getComId(),formModId,null,flowId,stepId);//取得表单所有可编辑组件
		view.addObject("formComponlist",formComponlist);
		view.addObject("userInfo",userInfo);
		return view;
	}
	
	/**
	 * 编辑表单模板界面
	 * @return
	 */
	@RequestMapping(value="/addFormModPage")
	public ModelAndView addFormModPage(){
		ModelAndView view = new ModelAndView("/sp/form/addFormMod");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		
		List<FormModSort> listFormSort = formService.listFormModSort(userInfo.getComId());
		view.addObject("formSortList",listFormSort);
		return view;
	}
	/**
	 *添加模板表单
	 * @param formMod
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="addFormMod")
	public Map<String, Object> addFormMod(FormMod formMod){
		
		UserInfo userInfo = this.getSessionUser();
		if(null==formMod.getComId()){
			//设置团队号
			formMod.setComId(userInfo.getComId());
		}
		//默认可用
		formMod.setEnable(ConstantInterface.SYS_ENABLED_YES);
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
	 * 取得布局的基本信息
	 * @param formModId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/checkFormState")
	public Map<String,Object> checkFormState(Integer formModId){
		
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		
		FormLayout formLayout = formService.queryFormLayoutState(sessionUser, formModId, null);
		map.put("status", "y");
		map.put("formLayout", formLayout);
		return map;
	}
	
	/**
	 * 编辑表单模板界面
	 * @return
	 */
	@RequestMapping(value="/editFormPage")
	public ModelAndView editFormPage(Integer formModId){
		ModelAndView view = new ModelAndView("/sp/form/editForm");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		//取得表单模板信息
		FormMod formMod = formService.getFormModbyId(formModId);
		view.addObject("formMod",formMod);
		return view;
	}
	/**
	 * 编辑表单模板界面
	 * @return
	 */
	@RequestMapping(value="/editFormDevPage")
	public ModelAndView editFormV2Page(Integer formModId){
		ModelAndView view = new ModelAndView("/sp/form/editFormDev");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 预览表单模板
	 * @param formModId 模板主键
	 * @return
	 */
	@RequestMapping(value="/viewFormModPage")
	public ModelAndView viewFormModPage(Integer formModId){
		ModelAndView view = new ModelAndView("/sp/form/viewForm");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		//取得表单模板信息
		FormMod formMod = formService.getFormModbyId(formModId);
		Integer formState = formMod.getFormState();
		if(null!=formState && formState == 1){
			view.setViewName("/sp/form/viewFormDev");
		}
		view.addObject("formMod",formMod);
		return view;
	}
	
	
	/**
	 * 查询表单布局
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findFormComp")
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
	@RequestMapping(value="/findFormCompDev")
	public FormMod findFormCompDev(FormMod formMod){
		UserInfo userInfo = new UserInfo();
		userInfo.setComId(formMod.getComId());
		Integer formId =formMod.getId();
		
		//取得表单模板信息
		String modName = formService.getFormModbyId(formId).getModName();
		formMod.setModName(modName);
		//查询表单布局
		FormLayout formLayout = formService.queryLayoutByModIdDev(formId,userInfo);
		formMod.setFormLayout(formLayout);
		return formMod;
	}
	/**
	 * 添加模板表单控件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/editFormMod")
	public Map<String, Object> editForm(FormModLayout formModLayout){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		//修改表单组件
		formService.updateFormCompon(userInfo,formModLayout);
		this.setNotification(Notification.SUCCESS, "操作成功！");
		return map;
	}
	
	/**
	 * 表单控件信息修改
	 * @param formModLayout
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/editFormModDev")
	public Map<String, Object> editFormModDev(FormModLayout formModLayout){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		
		formService.updateFormComponDev(userInfo,formModLayout);
	
		this.setNotification(Notification.SUCCESS, "操作成功！");
		return map;
	}
	
	/**
	 * 恢复表单模板
	 * @param formModId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/backFormLayout")
	public Map<String,Object> backFormLayout(Integer formModId) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//恢复表单模板
		map  = formService.updateBackFormLayout(userInfo,formModId);
		if(map.get("status").equals("y")){
			//设置消息
			this.setNotification(Notification.SUCCESS, "恢复成功！");
		}
		return map;
	}
	
	/**
	 * 修改表单启用状态
	 * @param formMod
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateFormModEnabled")
	public Map<String,Object> updateFormModEnabled(FormMod formMod){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//修改表单启用状态
		formService.updateFormModEnabled(formMod,userInfo);
		map.put("status", "y");
		return map;
	}
	/********************表单模板归类*****************************/
	
	/**
	 * 跳转反馈类型添加页面
	 * 
	 * @param feedBackType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetFormModSortOrder")
	public Map<String, Object> ajaxGetFormModSortOrder() {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Integer orderNum = formService.queryFormModSortOrderMax(sessionUser.getComId());
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
	@RequestMapping(value="/addFormModSort")
	public Map<String,Object> addFormSort(FormModSort formSort){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		formSort.setComId(userInfo.getComId());
		
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
	@RequestMapping(value="/editFormModSortPage")
	public ModelAndView listFormModSortPage(){
		ModelAndView mav = new ModelAndView("/sp/form/editFormModSort");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		return mav;
		
	}
	/**
	 * 添加表单模板分类
	 * @param formSort
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxListFormModSort")
	public Map<String,Object> ajaxListFormModSort(FormModSort formSort){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		List<FormModSort> listFormSort = formService.listFormModSort(userInfo.getComId());
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
	@RequestMapping(value = "/updateFormModSort")
	public Map<String,Object> updateFormModSort(FormModSort formModSort)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		formService.updateFormModSort(formModSort);
		map.put("status", "y");
		return map;
	}
	/**
	 * 修改表单所属分类以及名称
	 * @param formMod
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxUpdateFormMod")
	public Map<String,Object> ajaxUpdateFormMod(FormMod formMod)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		//验证表单是否同名
		List<FormMod> formMods = formService.checkFormModName(userInfo.getComId(),formMod.getModName());
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
	 * 修改表单类型属性
	 * @param formModSort
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delFormModSort")
	public Map<String,Object> delFormModSort(Integer formSortId)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		boolean falg = formService.delFormModSort(formSortId, userInfo);
		if(falg){
			map.put("status", "y");
		}else{
			map.put("status", "f");
			map.put("info", "表单不存在");
		}
		return map;
	}
	/********************表单模板归类****************************************/
	/********************表单模板克隆****************************************/
	
	
	
	/**
	 * 克隆表单模板
	 * @param formModId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addFormModClone")
	public Map<String,Object> addFormModClone(Integer formModId) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//克隆表单模板
		formService.addFormModClone(userInfo,formModId);
		map.put("status", "y");
		//设置消息
		this.setNotification(Notification.SUCCESS, "克隆成功！");
		return map;
	}
	
	/********************表单模板克隆****************************************/
	
	/********************云表单****************************************/
	
	/**
	 * 云表单列表
	 * @param formMod
	 * @return
	 */
	@RequestMapping(value="/listPagedCloudFormMod")
	public ModelAndView listPagedCloudFormMod(FormMod formMod){
		ModelAndView view = new ModelAndView("/sp/sp_center");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		//只查询可用的
		formMod.setEnable(ConstantInterface.SYS_ENABLED_YES);
		//分页查询云表单信息
		List<FormMod> formModlist = formService.listPagedCloudFormMod(formMod);
		view.addObject("formModlist",formModlist);
		
		List<FormModSort> listFormSort = formService.listFormModSort(0);
		view.addObject("listFormSort",listFormSort);
		return view;
	}
	/**
	 * 验证表单名称是存在
	 * @param formModId 模板表单主键
	 * @param modName 新的表单名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkFormModName")
	public Map<String,Object> checkFormModName(Integer formModId,String modName) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		if(null==modName || "".equals(modName)){
			modName = formService.getFormModbyId(formModId).getModName();
		}
		//验证表单是否同名
		List<FormMod> formMods = formService.checkFormModName(userInfo.getComId(),modName);
		if(null == formMods || formMods.isEmpty()){
			map.put("status", "y");
		}else{
			map.put("status", "f");
			map.put("info","已有同名表单");
		}
		return map;
	}
	/**
	 * 验证表单名称是存在
	 * @param formModId 模板表单主键
	 * @param modName 新的表单名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addFormModFormCloud")
	public Map<String,Object> addFormModFormCloud(Integer formModId,String modName,Integer formSortId) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//验证表单是否同名
		List<FormMod> formMods = formService.checkFormModName(userInfo.getComId(),modName);
		if(null == formMods || formMods.isEmpty()){
			//验证表单是否同名
			formService.addFormModFormCloud(userInfo,formModId,modName,formSortId);
			map.put("status", "y");
		}else{
			map.put("status", "f1");
			map.put("info","已有同名表单,请重新命名");
		}
		return map;
	}
	
	
	/********************云表单****************************************/
	
	/**
	 * 跳转子表单关联表的数据选择
	 * @return
	 */
	@RequestMapping(value="/chooseSysColumnPage")
	public ModelAndView chooseSysColumnPage(){
		ModelAndView view = new ModelAndView("/sp/form/chooseSysColumn");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 子表单关联表的数据选择
	 * @param tableName 数据表名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/chooseSysColumn")
	public Map<String,Object> chooseSysColumn(String tableName){
		Map<String,Object> map = new HashMap<String,Object>();
		List<TableInfoVo> infoVos = formService.listColumnInfo(tableName);
		map.put("list", infoVos);
		map.put("status", "y");
		return map;
	}
	/**
	 * 查询字典表数据信息
	 * @param sysDicName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/chooseSysDic")
	public Map<String,Object> chooseSysDic(String sysDicName){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		List<Object> infoVos = formService.listSysDicInfo(userInfo,sysDicName);
		map.put("list", infoVos);
		map.put("status", "y");
		return map;
	}
}