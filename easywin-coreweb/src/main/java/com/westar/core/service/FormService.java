package com.westar.core.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.westar.base.model.BusAttrMapFormColTemp;
import com.westar.base.model.FormCompon;
import com.westar.base.model.FormComponMaxField;
import com.westar.base.model.FormConf;
import com.westar.base.model.FormLayout;
import com.westar.base.model.FormLayoutHtml;
import com.westar.base.model.FormMod;
import com.westar.base.model.FormModSort;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.SpFlowRunStepFormControl;
import com.westar.base.model.SpStepFormControl;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FormDataOption;
import com.westar.base.pojo.FormModLayout;
import com.westar.base.pojo.FormOption;
import com.westar.base.pojo.LayoutDetail;
import com.westar.base.pojo.MonitorField;
import com.westar.base.pojo.TableInfoVo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.StringUtil;
import com.westar.core.dao.FormDao;

@Service
public class FormService {
	
	private static final Log loger = LogFactory.getLog(FormService.class);

	@Autowired
	FormDao formDao;
	
	@Autowired
	FlowDesignService flowDesignService;
	@Autowired
	WorkFlowService workFlowService;
	
	private static String[] fieldIds = new String[]{"fieldid","defoption","selectionid","tempid"};
	private static List<String> fieldlist = Arrays.asList(fieldIds);
	
	private static String[] componentKeyArray = new String[]{"CheckBox",
		"DateInterval","DateComponent","TextArea","NumberComponent",
		"RadioBox","Department","Select","RelateFile","Employee","RelateItem",
		"RelateCrm","Text","Monitor","MoneyComponent","SerialNum","RelateMod"};
	
	/**
	 * 分页查询表单信息
	 * @param formMod
	 * @param userInfo
	 * @return
	 */
	public List<FormMod> listPagedFormMod(FormMod formMod, UserInfo userInfo) {
		return formDao.listPagedFormMod(formMod,userInfo);
	}

	/**
	 * 添加企业表单模板
	 * @param formMod
	 * @return
	 */
	public Integer addFormMod(FormMod formMod) {
		return formDao.add(formMod);
	}

	/**
	 * 通过模板主键取得数据
	 * @param formModId
	 * @return
	 */
	public FormMod getFormModbyId(Integer formModId) {
		FormMod formMod = (FormMod) formDao.objectQuery(FormMod.class, formModId);
		Integer comId = formMod.getComId();
		FormLayout formLayout = formDao.queryFormLayoutByModId(formModId,comId);
		Integer formState = 0;
		if(null!=formLayout){
			formState = formLayout.getFormState();
			formState = formState == null?0:formState;
		}
		formMod.setFormState(formState);
		return formMod;
	}
	/**
	 * 取得表单最新布局
	 * @param formModId
	 * @param userInfo
	 * @return
	 */
	public FormLayout getFormLayoutByModId(Integer formModId,UserInfo userInfo) {
		
		//取得表单模板的团队号
		Integer comId = ((FormMod)formDao.objectQuery(FormMod.class, formModId)).getComId();
		if(comId != 0 && !comId.equals(userInfo.getComId())){//团队号验证
			return null;
		}
				
		FormLayout formLayout = formDao.queryFormLayoutByModId(formModId,comId);
		if(null!=formLayout){
			Integer formLayoutId = formLayout.getId();
			
			LayoutDetail layoutDetail = new LayoutDetail();
			List<FormCompon> listFormCompon = formDao.listTreeCompon(formModId,formLayoutId,comId,-1);
			if(null!=listFormCompon && listFormCompon.size()>0){
				FormCompon formCompon = listFormCompon.get(0);
				Integer fieldId = formCompon.getFieldId();
				
				layoutDetail.setFieldId(fieldId);
				layoutDetail.setTempId(fieldId.toString());
				
				List<FormConf> listFormConfig = formDao.listLayoutConf(comId, 
						formModId, formLayoutId, fieldId);
				
				Class clz = layoutDetail.getClass();
				
				if(null!=listFormConfig && listFormConfig.size()>0){
					for (FormConf formConf : listFormConfig) {
						String name = formConf.getConfName();
						String getMethodName = "set"+CommonUtil.toFirstLetterUpperCase(name); 
						Method m;
						try {
							m = clz.getMethod(getMethodName, String.class);
							m.invoke(layoutDetail, formConf.getConfValue());
						} catch (Exception e) {
//							loger.warn(getMethodName, e);
						}
					}
				}
				
				
				if(formCompon.getIsLeaf()==0){//是叶子节点
					List<LayoutDetail> layoutDetails = this.listFormCompon(formModId,formLayoutId,comId,formCompon.getId(),false,null);
					layoutDetail.setLayoutDetail(layoutDetails);
				}
			}
			
			Gson g = new Gson();
			String msgObj = g.toJson(layoutDetail);
			formLayout.setLayoutDetail(msgObj);
		}
		return formLayout;
	}
	/**
	 * 查询以及子组件
	 * @param formModId 表单模板主键
	 * @param formLayoutId 表单布局主键
	 * @param userInfo 当前操作人员
	 * @param parentId 父级组件主键
	 * @return
	 */
	public List<FormCompon> listTreeCompon(Integer formModId,Integer formLayoutId,
			UserInfo userInfo,Integer parentId){
		List<FormCompon> listFormCompon = formDao.listTreeCompon(formModId,formLayoutId,userInfo,parentId);
		return listFormCompon;
	}
	/**
	 * 查询表单组件信息
	 * @param formModId 表单模板主键
	 * @param formLayoutId 表单布局主键
	 * @param comId 当前操作人员
	 * @param parentId 父级组件主键
	 * @param author 是否需要授权
	 * @return
	 */
	public List<LayoutDetail> listFormCompon(Integer formModId,Integer formLayoutId,
			Integer comId,Integer parentId, boolean author,Set<Integer> authFields){
		
		//最外层布局信息
		List<FormCompon> topFormCompons = new ArrayList<FormCompon>();
		//自己的子节点信息
		Map<Integer, List<FormCompon>> subFormComponMap = new HashMap<Integer, List<FormCompon>>();
		//查询所有的控件集合
		List<FormCompon> listFormCompon = formDao.listAllTreeCompon(formModId,formLayoutId,comId);
		//遍历控件集合，设置关联关系
		if(null!=listFormCompon && !listFormCompon.isEmpty()){
			for (FormCompon formCompon : listFormCompon) {
				//组件的父节点主键
				Integer pId = formCompon.getParentId();
				//父节点的子组件
				List<FormCompon> subFormCompons = subFormComponMap.get(pId);
				if(null==subFormCompons){
					//首次的初始化
					subFormCompons = new ArrayList<FormCompon>();
				}
				//存放添加子组件
				subFormCompons.add(formCompon);
				//父节点的子组件
				subFormComponMap.put(pId, subFormCompons);
				if(pId == -1){
					//最外层的布局组件
					topFormCompons.add(formCompon);
				}
			}
		}
		
		//组件配置信息集合
		List<FormConf> listAllFormConfig = formDao.listLayoutConf(comId, 
				formModId, formLayoutId, null);
		//各个组件的配置集合
		Map<Integer,List<FormConf>> formConfMap = new HashMap<Integer,List<FormConf>>();
		if(null!=listAllFormConfig && !listAllFormConfig.isEmpty()){
			for (FormConf formConf : listAllFormConfig) {
				//组件标识
				Integer fieldId = formConf.getFieldId();
				//组件配置
				List<FormConf> fileConf = formConfMap.get(fieldId);
				if(null==fileConf){
					fileConf = new ArrayList<FormConf>();
				}
				//组件的配置
				fileConf.add(formConf);
				//添加配置与组件的关系
				formConfMap.put(fieldId, fileConf);
			}
		}
		
		//组件有配置文件
		List<LayoutDetail> layoutDetails = new ArrayList<LayoutDetail>();
		if(null!=topFormCompons && !topFormCompons.isEmpty()){
			for (FormCompon formCompon : topFormCompons) {
				//构建组件信息
				LayoutDetail layoutDetail = this.constrLayoutDetail(formCompon,
						subFormComponMap,formConfMap,author,authFields);
				//添加组件
				layoutDetails.add(layoutDetail);
				
			}	
		}
		return layoutDetails;
	}
	
	/**
	 * 构建组件信息
	 * @param formCompon 组件信息
	 * @param subFormComponMap 所有组件的子节点
	 * @param formConfMap 所有控件的配置信息
	 * @param author 是否授权
	 * @param authFields 授权的组件
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	private LayoutDetail constrLayoutDetail(FormCompon formCompon,
			Map<Integer, List<FormCompon>> subFormComponMap,
			Map<Integer, List<FormConf>> formConfMap, boolean author,
			Set<Integer> authFields) {
		//组件字段标识
		Integer fieldId = formCompon.getFieldId();
		//需要返回的布局信息
		LayoutDetail layoutDetail = new LayoutDetail();
		//设置组件标识
		layoutDetail.setFieldId(fieldId);
		//设置临时主键信息
		layoutDetail.setTempId(fieldId.toString());
		//组件类型标识
		String componentKey = formCompon.getComponentKey();
		if(componentKey.equals("DataTable")){//动态表单，设置子表单主键
			//设置子表单信息
			layoutDetail.setSubFormId(formCompon.getId().toString());
			if(author && null!= authFields && authFields.contains(fieldId)){//给子表单授权
				List<FormCompon> listSubFormCompon = subFormComponMap.get(formCompon.getId());
				if(null!=listSubFormCompon && !listSubFormCompon.isEmpty()){
					for (FormCompon subFormCompon : listSubFormCompon) {
						authFields.add(subFormCompon.getFieldId());
					}
				}
			}
		}
		if(componentKey.equals("CheckBox") 
			||componentKey.equals("RadioBox")
			||componentKey.equals("Select")){//单复选和下拉设置选项
			//设置选项信息
			List<FormOption> formOptions = this.listFormOption(formCompon.getId(),subFormComponMap,formConfMap);
			layoutDetail.setOptions(formOptions);
		}else if(componentKey.equals("DateInterval")){//日期区间设置起止组件
			//设置日期开始结束
			List<LayoutDetail> layoutDetailSon = this.listFormCompon(subFormComponMap,formConfMap,formCompon.getId(),author,authFields);
			layoutDetail.setStart(layoutDetailSon.get(0));
			layoutDetail.setEnd(layoutDetailSon.get(1));
		}else if(componentKey.equals("ComboSelect")){//多级下拉设置组件信息
			List<LayoutDetail> selects = this.listFormCompon(subFormComponMap,formConfMap,formCompon.getId(),author,authFields);
			layoutDetail.setSelects(selects);
			List<FormOption> Options = this.listFormOption(formCompon.getId(),subFormComponMap,formConfMap);
			layoutDetail.setOptions(Options);
		}else if(componentKey.equals("Option")){//选项信息，则不操作
			return null;
		}
		
		//组件配置信息集合
		List<FormConf> listFormConfig = formConfMap.get(fieldId);
		//计算控件
		List<String> type_valueList = new ArrayList<String>();
		//反编译填充配置信息
		Class clz = layoutDetail.getClass();
		if(null!=listFormConfig && listFormConfig.size()>0){
			for (FormConf formConf : listFormConfig) {
				String name = formConf.getConfName();
				String setMethodName = "set"+CommonUtil.toFirstLetterUpperCase(name); 
				
				if("type_value".equals(name)){
					type_valueList.add(formConf.getConfValue());
					continue;
				}
				if(componentKey.equals("TableLayout") && 
						"thArray".equals(name)){
					String thArrayStr = formConf.getConfValue();
					if(!StringUtil.isBlank(thArrayStr)){
						List<Integer> thArray = JSONArray.parseArray(thArrayStr, Integer.class);
						layoutDetail.setThArray(thArray);
					}
				}else{
					Method m;
					try {
						m = clz.getMethod(setMethodName, String.class);
						m.invoke(layoutDetail, formConf.getConfValue());
					} catch (Exception e) {
//						loger.warn(setMethodName+"-----------"+formConf.getConfValue(), e);
					}
				}
				
			}
		}
		
		if(componentKey.equals("Monitor") && !type_valueList.isEmpty()){
			List<MonitorField> monitorFields = new ArrayList<MonitorField>();
			for (String type_value : type_valueList) {
				MonitorField monitorField = new MonitorField();
				monitorField.setType(type_value.split("_")[0]);
				monitorField.setValue(type_value.split("_")[1]);
				monitorFields.add(monitorField);
			}
			layoutDetail.setMonitorFields(monitorFields);
		}
		
		if(formCompon.getIsLeaf()==0){//有子节点
			if(!componentKey.equals("CheckBox")
				&& !componentKey.equals("RadioBox")
				&&!componentKey.equals("DateInterval")
				&&!componentKey.equals("ComboSelect")
				&& !componentKey.equals("Select")){//没有选项信息的组件需配置子组件信息
				List<LayoutDetail> layoutDetailSon = this.listFormCompon(subFormComponMap,formConfMap,formCompon.getId(),author,authFields);
				layoutDetail.setLayoutDetail(layoutDetailSon);
			}
		}
		
		//TODO 在此处设置权限
		if(author){
			if(null == authFields || authFields.isEmpty() || !authFields.contains(fieldId)){//没有权限的
				if(componentKey.equals("Employee")){
					layoutDetail.setIsDefault("false");
					layoutDetail.setIsCurrentEmployee("false");
				}else if(componentKey.equals("Department")){
					layoutDetail.setIsDefault("fasle");
					layoutDetail.setIsCurrentDepartment("true");
					layoutDetail.setIsReadonly("true");
				}else if(componentKey.equals("Monitor")){//计算控件不能编辑
					layoutDetail.setIsEdit("false");
					layoutDetail.setMonitorFields(null);
				}else if(componentKey.equals("DateComponent")){
					layoutDetail.setIsSystemDate("false");//没有权限的不设定默认时间
				}
				layoutDetail.setIsReadOnly("true");
				layoutDetail.setRequired("false");
			}else{//有权限不能编辑
				if(componentKey.equals("Monitor")){//计算控件不能编辑
					layoutDetail.setIsEdit("false");
					layoutDetail.setIsReadOnly("false");
				}
			}
		}
		return layoutDetail;
	}

	/**
	 * 子布局信息
	 * @param subFormComponMap 所有子节点数据
	 * @param formConfMap 所有节点配置信息
	 * @param formComponId 组件主键
	 * @param author 是否授权
	 * @param authFields 授权的节点信息
	 * @return
	 */
	private List<LayoutDetail> listFormCompon(
			Map<Integer, List<FormCompon>> subFormComponMap,
			Map<Integer, List<FormConf>> formConfMap, Integer formComponId,
			boolean author, Set<Integer> authFields) {
		//组件有配置文件
		List<LayoutDetail> layoutDetails = new ArrayList<LayoutDetail>();
		if(null!=subFormComponMap.get(formComponId) && !subFormComponMap.get(formComponId).isEmpty()){
			for (FormCompon formCompon : subFormComponMap.get(formComponId)) {
				LayoutDetail layoutDetail = this.constrLayoutDetail(formCompon,
						subFormComponMap,formConfMap,author,authFields);
				layoutDetails.add(layoutDetail);
			}	
		}
		return layoutDetails;
	}

	/**
	 * 
	 * @param formModId
	 * @param formLayoutId
	 * @param comId
	 * @param parentId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private List<FormOption> listFormOption(Integer formModId,Integer formLayoutId,Integer comId,Integer parentId){
		//查询一级子组件信息
		List<FormCompon> listFormCompon = formDao.listTreeCompon(formModId,formLayoutId,comId,parentId);
		//组件有配置文件
		List<FormOption> formOptions = new ArrayList<FormOption>();
		
		if(null!=listFormCompon && listFormCompon.size()>0){
			for (FormCompon formCompon : listFormCompon) {
				//组件字段标志
				Integer fieldId = formCompon.getFieldId();
				//组件类型
				String componentKey = formCompon.getComponentKey();
				if(null== componentKey || componentKey.equals("Select")){
					continue;
				}
				FormOption formOption = new FormOption();
				formOption.setFieldId(fieldId.toString());
				formOption.setSelectionId(fieldId.toString());
				//组件配置信息集合
				List<FormConf> listFormConfig = formDao.listLayoutConf(comId, 
						formModId, formLayoutId, fieldId);
				//反编译填充配置信息
				Class clz = formOption.getClass();
				if(null!=listFormConfig && listFormConfig.size()>0){
					for (FormConf formConf : listFormConfig) {
						String name = formConf.getConfName();
						if(name.toLowerCase().equals("fieldId")
							|| name.toLowerCase().equals("selectionId")){
							continue;
						}
						String setMethodName = "set"+CommonUtil.toFirstLetterUpperCase(name); 
						Method m;
						try {
							m = clz.getMethod(setMethodName, String.class);
							m.invoke(formOption, formConf.getConfValue());
						} catch (Exception e) {
//							loger.warn(setMethodName+"-----------"+formConf.getConfValue(), e);
						}
					}
				}
				
				if(formCompon.getIsLeaf()==0){//有子节点的组件需配置子组件信息
					List<FormOption> formOptionSon = this.listFormOption(formModId,formLayoutId,comId,formCompon.getId());
					formOption.setChildren(formOptionSon);
				}
				
				formOptions.add(formOption);
			}
		}
		
		return formOptions;
		
	}
	/**
	 * 选项信息
	 * @param formComponPId 所属节点主键
	 * @param subFormComponMap 所有子节点集合
	 * @param formConfMap 所有节点的配置信息
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<FormOption> listFormOption(Integer formComponPId, Map<Integer, List<FormCompon>> subFormComponMap, 
			Map<Integer, List<FormConf>> formConfMap){
		//查询一级子组件信息
		List<FormCompon> listFormCompon = subFormComponMap.get(formComponPId);
		//组件有配置文件
		List<FormOption> formOptions = new ArrayList<FormOption>();
		
		if(null!=listFormCompon && listFormCompon.size()>0){
			for (FormCompon formCompon : listFormCompon) {
				//组件字段标志
				Integer fieldId = formCompon.getFieldId();
				//组件类型
				String componentKey = formCompon.getComponentKey();
				if(null== componentKey || componentKey.equals("Select")){
					continue;
				}
				FormOption formOption = new FormOption();
				formOption.setFieldId(fieldId.toString());
				formOption.setSelectionId(fieldId.toString());
				//组件配置信息集合
				List<FormConf> listFormConfig = formConfMap.get(fieldId);
				//反编译填充配置信息
				Class clz = formOption.getClass();
				if(null!=listFormConfig && listFormConfig.size()>0){
					for (FormConf formConf : listFormConfig) {
						String name = formConf.getConfName();
						if(name.toLowerCase().equals("fieldId")
								|| name.toLowerCase().equals("selectionId")){
							continue;
						}
						String setMethodName = "set"+CommonUtil.toFirstLetterUpperCase(name); 
						Method m;
						try {
							m = clz.getMethod(setMethodName, String.class);
							m.invoke(formOption, formConf.getConfValue());
						} catch (Exception e) {
//							loger.warn(setMethodName+"-----------"+formConf.getConfValue(),e);
						}
					}
				}
				
				if(formCompon.getIsLeaf()==0){//有子节点的组件需配置子组件信息
					List<FormOption> formOptionSon = this.listFormOption(formCompon.getId(),subFormComponMap,formConfMap);
					formOption.setChildren(formOptionSon);
				}
				
				formOptions.add(formOption);
			}
		}
		
		return formOptions;
		
	}
	
	/**
	 * 查询版本布局是否替换
	 * @param sessionUser 当前操作员
	 * @param formModId 表单主键
	 * @param formLayoutId 布局主键
	 * @return
	 */
	public FormLayout queryFormLayoutState(UserInfo sessionUser,Integer formModId,Integer formLayoutId){
		FormLayout formLayout = new FormLayout();
		if(null==formLayoutId || 0==formLayoutId ){//没有布局信息，则查询
			formLayout = formDao.queryFormLayoutByModId(formModId,sessionUser.getComId());
		}else{
			formLayout = (FormLayout) formDao.objectQuery(FormLayout.class, formLayoutId);
		}
		return formLayout;
	}
	
	/**
	 * 取得表单布局信息
	 * @param formModId 表单模板主键
	 * @param formLayoutId 布局主键
	 * @param userInfo 当前操作员
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FormLayout getWorkFlowComp(Integer formModId,Integer formLayoutId,Integer instanceId,UserInfo userInfo) {
		FormLayout formLayout = new FormLayout();
		if(null==formLayoutId || 0==formLayoutId ){//没有布局信息，则查询
			formLayout = formDao.queryFormLayoutByModId(formModId,userInfo.getComId());
			//布局主键
			formLayoutId = formLayout.getId();
		}else{
			formLayout = (FormLayout) formDao.objectQuery(FormLayout.class, formLayoutId);
		}
		
		if(null!=formLayout){
			//布局详情信息
			LayoutDetail layoutDetail = new LayoutDetail();
			//取得一级子组件信息
			List<FormCompon> listFormCompon = formDao.listTreeCompon(formModId,formLayoutId,userInfo,-1);
			if(null!=listFormCompon && !listFormCompon.isEmpty()){
				//顶层组件只有一个
				FormCompon formCompon = listFormCompon.get(0);
				//组件的字段标识
				Integer fieldId = formCompon.getFieldId();
				layoutDetail.setFieldId(fieldId);
				//组件配置信息
				List<FormConf> listFormConfig = formDao.listLayoutConf(userInfo.getComId(), 
						formModId, formLayoutId, fieldId);
				
				//反编译填充配置信息
				Class clz = layoutDetail.getClass();
				if(!listFormConfig.isEmpty()){
					for (FormConf formConf : listFormConfig) {
						String name = formConf.getConfName();
						String getMethodName = "set"+CommonUtil.toFirstLetterUpperCase(name); 
						Method m;
						try {
							m = clz.getMethod(getMethodName, String.class);
							m.invoke(layoutDetail, formConf.getConfValue());
						} catch (Exception e) {
//							e.printStackTrace();
						}
					}
				}
				
				
				if(formCompon.getIsLeaf()==0){//是叶子节点
					Set<Integer> authorFieldSet = new HashSet<Integer>();
					SpFlowInstance instance = workFlowService.getSpFlowInstanceById(instanceId, userInfo);//获取流程实例化对象信息
					Integer stepId = null;
					if(instance.getFlowState()==0 || instance.getFlowState()==2){//流程是开始或 草稿状态的时候，起始步骤默认为0
						stepId = 0;
					}
					
					
					
					if(instance.getFlowState() != 4){//没有办结
						
						if(null!=stepId && stepId == 0 && CommonUtil.isNull(instance.getFlowId())){//自由流程起始节点权限授权
							List<FormCompon> listAllFormCompont = this.listAllFormCompont(userInfo.getComId(), instance.getFormKey(),formLayoutId,null,null);
							if(!CommonUtil.isNull(listAllFormCompont)){
								for (FormCompon formCompon2 : listAllFormCompont) {
									authorFieldSet.add(formCompon2.getFieldId());
								}
							}
						}else{
							//获取开始步骤授权集合
							List<SpFlowRunStepFormControl> listCheckedFormCompon = workFlowService.
									listSpFlowRunStepFormControl(instance, userInfo, stepId);
							if(null!=listCheckedFormCompon && listCheckedFormCompon.size()>0){
								for (SpFlowRunStepFormControl spStepFormControl : listCheckedFormCompon) {
									authorFieldSet.add(Integer.parseInt(spStepFormControl.getFormControlKey()));
								}
							}
						}
					}
					
					List<LayoutDetail> layoutDetails = this.listFormCompon(
							formModId,formLayoutId,userInfo.getComId(),formCompon.getId(),true,authorFieldSet);
					
					layoutDetail.setLayoutDetail(layoutDetails);
				}
			}
			
			Gson g = new Gson();
			String msgObj = g.toJson(layoutDetail);
			formLayout.setLayoutDetail(msgObj);
		}
		return formLayout;
	}
	
	
	
	
	
	/**
	 * 修改表单组件
	 * @param userInfo 当前操作人员
	 * @param formMod 表单模板
	 * @param formLayout 布局版本
	 * @param layoutDetail 详情
	 */
	public void updateFormCompon(UserInfo userInfo,FormModLayout formModLayout) {
		
		//模板信息
		FormMod formMod = formModLayout.getFormMod();
		//布局信息
		FormLayout formLayout = formModLayout.getFormLayout();
		//布局详情
		String layoutDetailStr = formLayout.getLayoutDetail();
		formLayout.setLayoutDetail(null);
		
		LayoutDetail layoutDetail = JSONObject.parseObject(layoutDetailStr, LayoutDetail.class);
		
		//TODO 可以的话，判断两次布局是否相同
		//模块主键
		Integer formModId = formMod.getId();
		if(null==formModId){
			return;
		}
		//取得表单模板的团队号
		Integer comId = ((FormMod)formDao.objectQuery(FormMod.class, formModId)).getComId();
		if(comId != 0 && !comId.equals(userInfo.getComId())){//团队号验证
			return;
		}
		formDao.update(formMod);
		
		
		//取得模板的最新布局，用于版本号确定
		FormLayout formLayoutObj = formDao.queryFormLayoutByModId(formModId, comId);
		Integer version=0;
		if(null!=formLayoutObj){
			version = formLayoutObj.getVersion()+1;
		}
		//重新保存布局文件
		formLayout.setId(null);
		//模板主键
		formLayout.setFormModId(formModId);
		//设置版本号
		formLayout.setVersion(version);
		//设置创建人
		formLayout.setUserId(userInfo.getId());
		//设置团队号
		formLayout.setComId(comId);
		
		formLayout.setFormState(0);
		//添加布局
		Integer formLayoutId = formDao.add(formLayout);
		
		FormComponMaxField formComponMaxField = this.getFormMaxField(comId,formModId);
		
		
		//只统计日期区间的临时主键与正式主键
		Map<String,String> tempId_realDateFieldId = new HashMap<String, String>();
		//计算控件的临时主键与正式主键(用于添加配置)
		Map<String,List<MonitorField>> fieldId_list = new HashMap<String, List<MonitorField>>();
		
		Set<String> fieldIdSet = new HashSet<String>();
		
		//使用过的组件标识
		Set<Integer> usedFields = this.constrUsedFields(layoutDetailStr);
		
		//添加布局组件
		this.addFormCompon(comId,formModId,formLayoutId,-1,layoutDetail,
				formComponMaxField,usedFields,tempId_realDateFieldId,fieldId_list,fieldIdSet);
		
		if(null!=fieldId_list && !fieldId_list.isEmpty()){
			Set<String> ketSet = fieldId_list.keySet();
			for (String calFieldId : ketSet) {
				//取得计算控件的计算对象集合
				List<MonitorField> list = fieldId_list.get(calFieldId);
				if(null != list && !list.isEmpty()){
					for (MonitorField monitorField : list) {
						FormConf formConf = new FormConf();
						//设置企业号
						formConf.setComId(comId);
						//设置表单模板主键
						formConf.setFormModId(formModId);
						//设置表单布局主键
						formConf.setFormLayoutId(formLayoutId);
						//设置表单组件主键
						formConf.setFieldId(Integer.parseInt(calFieldId));
						//设置配置名称
						formConf.setConfName("type_value");
						
						String thisValue = tempId_realDateFieldId.get(monitorField.getValue());
						
						String type_value = monitorField.getType()+"_"+thisValue;
						//设置配置项值
						formConf.setConfValue(type_value);
						formDao.add(formConf);
						
						
					}
				}
			}
		}
		
		//修改表单的最大组件标识
		formDao.update(formComponMaxField);
		
	}
	/**
	 * 取得表单最大的fieldId标识
	 * @param comId 团队号
	 * @param formModId 表单主键
	 * @return
	 */
	private FormComponMaxField getFormMaxField(Integer comId, Integer formModId) {
		FormComponMaxField formComponMaxField = formDao.getFormMaxField(comId,formModId);
		if(null==formComponMaxField){
			formComponMaxField = new FormComponMaxField();
			formComponMaxField.setComId(comId);
			formComponMaxField.setFormModId(formModId);
			formComponMaxField.setMaxFieldId(ConstantInterface.FORM_INIT_FIELID+ConstantInterface.FILEID_DEP);
			formDao.add(formComponMaxField);
		}
		return formComponMaxField;
	}
	

	/**
	 * 添加组件配置
	 * @param comId 当前操作员
	 * @param formModId 表单主键
	 * @param formLayoutId 布局主键
	 * @param parentId 父类主键
	 * @param layout 布局详情
	 * @param formComponMaxField 当前系统最大fieldId
	 * @param tempId_realDateFieldId 日期区间的临时主键 与 fieldId
	 * @param fieldId_list 计算控件的filed 与 计算控件集合
	 * @param fieldIdSet 
	 */
	private void addFormCompon(Integer comId, Integer formModId,
			Integer formLayoutId,Integer parentId, LayoutDetail layout, FormComponMaxField formComponMaxField,Set<Integer> usedFileds,
			Map<String, String> tempId_realDateFieldId, Map<String, List<MonitorField>> fieldId_list, Set<String> fieldIdSet) {
		if(null!=layout && !"".equals(layout)){//有布局详情
			//组件类型
			String componentKey = layout.getComponentKey();
			if(null!=componentKey && !"".equals(componentKey)){//组件类型不为空
				layout.setSubFormId(null);
				//取出子布局
				List<LayoutDetail> layoutDetailList = layout.getLayoutDetail();
				layout.setLayoutDetail(null);
				//取出选项
				List<LayoutDetail> selects = layout.getSelects();
				layout.setSelects(null);
				//取出布局选项
				List<FormOption> formOptionlist = layout.getOptions();
				layout.setOptions(null);
				
				//暂时不知道
				List<LayoutDetail> selectIds = layout.getSelectIds();
				layout.setSelectIds(null);
				//日期区间始
				LayoutDetail start = layout.getStart();
				layout.setStart(null);
				//日期之间止
				LayoutDetail end = layout.getEnd();
				layout.setEnd(null);
				//暂时不知道
				LayoutDetail NOShow = layout.getNOShow();
				layout.setNOShow(null);
				//暂时不知道
				List<LayoutDetail> fieldReads = layout.getFieldReads();
				layout.setFieldReads(null);
				//暂时不知道
				List<LayoutDetail> fieldWrites = layout.getFieldWrites();
				layout.setFieldWrites(null);
				
				//添加组件配置
				Integer formComponId = this.addFormConf(comId, formModId, formLayoutId, parentId, 
						layout,formComponMaxField,usedFileds,tempId_realDateFieldId,fieldId_list,fieldIdSet);
				
				//添加时间区间开始布局的配置
				this.addFormCompon(comId,formModId,formLayoutId,formComponId,start,formComponMaxField,usedFileds,
						tempId_realDateFieldId,fieldId_list,fieldIdSet);
				//添加时间区间截止布局的配置
				this.addFormCompon(comId,formModId,formLayoutId,formComponId,end,formComponMaxField,usedFileds,
						tempId_realDateFieldId,fieldId_list,fieldIdSet);
				//添加NoShow布局的配置(暂时不知道是什么玩意)
				this.addFormCompon(comId,formModId,formLayoutId,formComponId,NOShow,formComponMaxField,usedFileds,
						tempId_realDateFieldId,fieldId_list,fieldIdSet);
				
				//子布局集合
				if(null!=layoutDetailList && layoutDetailList.size()>0){
					for (LayoutDetail layoutDetailObj : layoutDetailList) {
						//添加组件
						this.addFormCompon(comId,formModId,formLayoutId,formComponId,layoutDetailObj,
								formComponMaxField,usedFileds,tempId_realDateFieldId,fieldId_list,fieldIdSet);
					}
				}
				//有选项的时候，存组件和选项
				if(null!=selects && selects.size()>0){
					for (LayoutDetail selectsObj : selects) {
						//添加下拉组件
						this.addFormCompon(comId,formModId,formLayoutId,formComponId,selectsObj,
								formComponMaxField,usedFileds,tempId_realDateFieldId,fieldId_list,fieldIdSet);
					}
					if(null!=formOptionlist && formOptionlist.size()>0){
						for (FormOption formOptionObj : formOptionlist) {
							//添加下拉选项组件
							this.addFormConfOpt(comId,formModId,formLayoutId,formComponId,formOptionObj,formComponMaxField,fieldIdSet);
						}
					}

				}else{
					if(null!=formOptionlist && formOptionlist.size()>0){
						for (FormOption formOptionObj : formOptionlist) {
							//添加选项组件
							this.addFormConfOpt(comId,formModId,formLayoutId,formComponId,formOptionObj,formComponMaxField,fieldIdSet);
						}
					}
				}
				if(null!=selectIds && selectIds.size()>0){
					for (LayoutDetail selectsObj : selectIds) {
						//添加下拉组件
						this.addFormCompon(comId,formModId,formLayoutId,formComponId,selectsObj,
								formComponMaxField,usedFileds,tempId_realDateFieldId,fieldId_list,fieldIdSet);
					}
				}
				if(null!=fieldReads && fieldReads.size()>0){
					for (LayoutDetail selectsObj : fieldReads) {
						this.addFormCompon(comId,formModId,formLayoutId,formComponId,selectsObj,
								formComponMaxField,usedFileds,tempId_realDateFieldId,fieldId_list,fieldIdSet);
					}
				}
				if(null!=fieldWrites && fieldWrites.size()>0){
					for (LayoutDetail selectsObj : fieldWrites) {
						this.addFormCompon(comId,formModId,formLayoutId,formComponId,selectsObj,
								formComponMaxField,usedFileds,tempId_realDateFieldId,fieldId_list,fieldIdSet);
					}
				}
				
				
			}
			
		}
		
		
	}
	
	/**
	 * 添加普通的布局配置
	 * @param comId 当前操作人员
	 * @param formModId 模块主键
	 * @param formLayoutId 布局主键
	 * @param formComponId 组件主键
	 * @param layout 布局信息
	 * @param fieldId_list 
	 * @param tempId_realDateFieldId 
	 * @param fieldIdSet 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Integer addFormConf(Integer comId, Integer formModId,
			Integer formLayoutId, Integer formComponPId, LayoutDetail layout,FormComponMaxField formComponMaxField,Set<Integer> usedFields, 
			Map<String, String> tempId_realDateFieldId, Map<String, List<MonitorField>> fieldId_list, Set<String> fieldIdSet) {
		Integer formComponId  = 0;
		if(null!=layout && !"".equals(layout)){
			FormCompon formCompon = new FormCompon();
			String componentKey = layout.getComponentKey();
			//设置企业号
			formCompon.setComId(comId);
			//设置父类主键
			formCompon.setParentId(formComponPId);
			//设置模板主键
			formCompon.setFormModId(formModId);
			//设置所在布局主键
			formCompon.setFormLayoutId(formLayoutId);
			//设置布局类型
			formCompon.setComponentKey(componentKey);
			//设置标题
			formCompon.setTitle(layout.getTitle());
			//默认字段标识为主键
			Integer fieldId = layout.getFieldId();
			
			if(null != fieldId && fieldId >0 
					&& !fieldIdSet.contains(fieldId.toString())){//组件是否有指定字段
				formCompon.setFieldId(fieldId);
			}else{
				fieldId = null;
			}
			if(null!=layout.getOrder()){
				//设置所在布局的排序号
				formCompon.setOrderNo(layout.getOrder().toString());
			}
			
			formComponId = formDao.add(formCompon);
			
			formCompon.setId(formComponId);
			
			Integer maxFieldId = formComponMaxField.getMaxFieldId();
			
			if(null!=fieldId && fieldId>0){//原有字段标识
				formCompon.setFieldId(fieldId);
				fieldIdSet.add(fieldId.toString());
				
				if(maxFieldId < fieldId){
					formComponMaxField.setMaxFieldId(fieldId);
				}
			}else{
				fieldId = this.getNextFieldId(maxFieldId, usedFields);
				formComponMaxField.setMaxFieldId(fieldId);
				
				formCompon.setFieldId(fieldId);
				//设置字段标识
				formDao.update(formCompon);
				
				fieldIdSet.add(fieldId.toString());
			}
			
			if(componentKey.equals("Monitor")){
				List<MonitorField> monitorFields = layout.getMonitorFields();
				fieldId_list.put(fieldId.toString(), monitorFields);
				layout.setMonitorFields(null);
			}else if(componentKey.equals("DateInterval")){
				tempId_realDateFieldId.put(layout.getTempId(), fieldId.toString());
			}
			
			//字段标识不存放于配置文件
			layout.setFieldId(null);
			
			Class clz = layout.getClass();
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				String getMethodName = "get"+CommonUtil.toFirstLetterUpperCase(name); 
				Object value = "";
				if(componentKey.equals("TableLayout") && 
						"thArray".equals(name)){
					List<Integer> thArray = layout.getThArray();
					if(null!=thArray && thArray.size()>0){
						Gson gson = new Gson();
						value = gson.toJson(thArray);
					}
					
				}else{
					try{
						//取值
						value = clz.getMethod(getMethodName).invoke(layout); 
					}catch(Exception e){  
					} 
				}
				
				if(null!=value && !"".equals(value) && !fieldlist.contains(name.toLowerCase())){
					FormConf formConf = new FormConf();
					//设置企业号
					formConf.setComId(comId);
					//设置表单模板主键
					formConf.setFormModId(formModId);
					//设置表单布局主键
					formConf.setFormLayoutId(formLayoutId);
					//设置表单组件主键
					formConf.setFieldId(fieldId);
					//设置配置名称
					formConf.setConfName(name);
					//设置配置项值
					formConf.setConfValue(value.toString());
					formDao.add(formConf);
				}
			}
		}
		return formComponId;
		
	}
	/**
	 * 添加有选项的布局配置
	 * @param comId 当前操作人员
	 * @param formModId 
	 * @param formLayoutId
	 * @param formComponPId
	 * @param formComponMaxField 
	 * @param fieldIdSet 
	 * @param layout
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addFormConfOpt(Integer comId, Integer formModId,
			Integer formLayoutId, Integer formComponPId, FormOption formOption, FormComponMaxField formComponMaxField, Set<String> fieldIdSet) {
		if(null!=formOption && !"".equals(formOption)){
			FormCompon formCompon = new FormCompon();
			String componentKey = formOption.getComponentKey();
			//设置企业号
			formCompon.setComId(comId);
			//设置父类主键
			formCompon.setParentId(formComponPId);
			//设置模板主键
			formCompon.setFormModId(formModId);
			//设置所在布局主键
			formCompon.setFormLayoutId(formLayoutId);
			//设置布局类型
			formCompon.setComponentKey(componentKey);
			//设置标题
			formCompon.setTitle(formOption.getName());
			if(!StringUtil.isBlank(formOption.getOrder())){
				//设置所在布局的排序号
				formCompon.setOrderNo(formOption.getOrder().toString());
			}
			//添加组件
			Integer formComponId =formDao.add(formCompon);
			formCompon.setId(formComponId);
			
			//原有字段标识
			String fieldId = formOption.getFieldId();
			if(null!= fieldId && !"".equals(fieldId) && !fieldIdSet.contains(fieldId)){
				formCompon.setFieldId(Integer.parseInt(fieldId));
				
				fieldIdSet.add(fieldId);
				
				formComponMaxField.setMaxFieldId(Integer.parseInt(fieldId));
			}else{
				Integer maxField = formComponMaxField.getMaxFieldId()+1;
				formComponMaxField.setMaxFieldId(maxField);
				fieldId = maxField + "";
				formCompon.setFieldId(Integer.parseInt(fieldId));
				
				fieldIdSet.add(fieldId);
			}
			formDao.update(formCompon);
			
			List<FormOption> formOptionlist = formOption.getChildren();
			formOption.setChildren(null);
			
			Class clz = formOption.getClass();
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				String getMethodName = "get"+CommonUtil.toFirstLetterUpperCase(name); 
				try{  
					Object value = clz.getMethod(getMethodName).invoke(formOption); 
					if(null!=value && !"".equals(value) && !fieldlist.contains(name.toLowerCase())){
						FormConf formConf = new FormConf();
						//设置企业号
						formConf.setComId(comId);
						//设置表单模板主键
						formConf.setFormModId(formModId);
						//设置表单布局主键
						formConf.setFormLayoutId(formLayoutId);
						//设置表单组件主键
						formConf.setFieldId(Integer.parseInt(fieldId));
						//设置配置名称
						formConf.setConfName(name);
						//设置配置项值
						formConf.setConfValue(value.toString());
						formDao.add(formConf);
					}
				}catch(Exception e){  
				}  
			}
			
			if(null!=formOptionlist && formOptionlist.size()>0){
				for (FormOption formOptionObj : formOptionlist) {
					//添加选项
					this.addFormConfOpt(comId,formModId,formLayoutId,formComponId,formOptionObj,formComponMaxField,fieldIdSet);
				}
			}
		}
		
	}
	/**
	 * 列出用户权限范围内的表单
	 * @param formMod
	 * @param userInfo
	 * @return
	 */
	public List<FormMod> listPagedUserFormMod(FormMod formMod, UserInfo userInfo) {
		List<FormMod> formModList = formDao.listPagedUserFormMod(formMod,userInfo);
		return formModList;
	}
	/**
	 * 取得模板的最新版本
	 * @param formKey
	 * @param comId
	 * @return
	 */
	public FormMod getFormModVersion(Integer formKey, Integer comId) {
		return formDao.getFormModVersion(formKey,comId);
	}
	/**
	 * 取得组件的父级类型
	 * @param comId 团队号
	 * @param formModId 表单模板主键
	 * @param formLayoutId 布局主键
	 * @param fieldId 文本字段
	 * @return
	 */
	public FormCompon getParentCompon(Integer comId, Integer formModId,
			Integer formLayoutId,Integer fieldId){
		return formDao.getParentCompon(comId, formModId, formLayoutId, fieldId);
	}
	
	/**
	 * 取得表单所有可编辑组件
	 * @param comId 团队号
	 * @param formKey 表单模板主键
	 * @param layoutId 布局主键
	 * @param flowId 流程主键
	 * @param stepId 步骤主键
	 * @return
	 */
	public List<FormCompon> listAllFormCompont(Integer comId, Integer formKey,Integer layoutId,Integer flowId,
			Integer stepId){
		if(null==layoutId || layoutId==0){
			//取得模板信息
			FormMod formMod = formDao.getFormModVersion(formKey,comId);
			layoutId = formMod.getLayoutId();
		}
		List<FormCompon> listFormComponts = new ArrayList<FormCompon>();

		FormLayout formLayout = (FormLayout) formDao.objectQuery(FormLayout.class, layoutId);
		Integer formLayOutState =  formLayout.getFormState();

		List<FormCompon> listFormCompont = formDao.listAllFormComponeNoOption(comId,formKey,layoutId,-1);
		if(null!=listFormCompont && !listFormCompont.isEmpty()){
			List<String> componentKeys = Arrays.asList(componentKeyArray);
			List<SpStepFormControl> listCheckedFormCompon = null;

			//是否具有编辑权限
			Set<Integer> authorFieldSet = new HashSet<Integer>();
			//是否可以意见填充
			Map<Integer,Integer> isFillFieldMap = new HashMap<Integer,Integer>();
			if(null!=flowId && flowId>0 && null!=stepId && stepId>0){
				listCheckedFormCompon = flowDesignService.listFormCompon(comId, flowId, stepId);
				//验证控件是否被选中
				if(null!=listCheckedFormCompon && !listCheckedFormCompon.isEmpty()){
					for (SpStepFormControl spStepFormControl : listCheckedFormCompon) {
						//组件标识
						Integer fieldId = Integer.parseInt(spStepFormControl.getFormControlKey().split(";")[0]);
						//编辑权限
						authorFieldSet.add(fieldId);
						//填充信息
						isFillFieldMap.put(fieldId, spStepFormControl.getIsFill());
					}
				}

			}
			Integer subFormIndex = 1;

			//记录子表单子主键标识和索引号
			Map<Integer,Integer> fieldId_dataIndex = new HashMap<Integer,Integer>();
			//记录组件主键和组件类型
			Map<Integer, String> comPonKeyType = new HashMap<Integer, String>();
			//只保留
			for (FormCompon formCompon : listFormCompont) {
				Integer fieldId = formCompon.getFieldId();

				if(authorFieldSet.contains(fieldId)){//有则选中
					formCompon.setChecked(1);//设置为选中状态
				}
				//编辑意见覆盖状态
				if(isFillFieldMap.containsKey(fieldId)){
					formCompon.setIsFill(isFillFieldMap.get(fieldId));
				}

				//组件类型
				String componentKey = formCompon.getComponentKey();
				//组件名称
				String title = formCompon.getTitle();

				//组件父类主键
				Integer parentId = formCompon.getParentId();
				//存放组件主键----组件类型
				comPonKeyType.put(formCompon.getId(), formCompon.getComponentKey());
				//父组件类型
				String parentKeyType = comPonKeyType.get(parentId);

				if(null!=formLayOutState && formLayOutState==1){
					if(componentKeys.contains(componentKey) && null!=title && !"".equals(title)
							&& !"DataTable".equals(componentKey)){//有组件名称,且不是动态表单
						//父类是动态表单
						if(null!=parentKeyType && parentKeyType.equals("DataTable")){
							continue;
						}
						//父组件类型不为日期区间
						if(null!=parentKeyType && !parentKeyType.equals("DateInterval")){
							listFormComponts.add(formCompon);
						}else if(formCompon.getParentId()==-1){
							listFormComponts.add(formCompon);
						}
					}else if("DataTable".equals(componentKey)){//是子表单
						//查询子表单子主键
						List<FormCompon> subFormCompons = formDao.listAllFormComponeNoOption(comId,formKey,layoutId,formCompon.getId());
						if(null!=subFormCompons && !subFormCompons.isEmpty()){//有子组件
							//设置子表单号
							if(!fieldId_dataIndex.containsKey(fieldId)){//设置子表单序号
								formCompon.setSubFormIndex(subFormIndex);
								fieldId_dataIndex.put(fieldId,subFormIndex);
								subFormIndex++;
							}else{
								formCompon.setSubFormIndex(fieldId_dataIndex.get(fieldId));
							}

							listFormComponts.add(formCompon);
							for (FormCompon subFormCompon : subFormCompons) {
								if(authorFieldSet.contains(subFormCompon.getFieldId())){//子组件被选中了
									subFormCompon.setChecked(1);//设置为选中状态
								}

								//编辑意见覆盖状态
								if(isFillFieldMap.containsKey(subFormCompon.getFieldId())){
									formCompon.setIsFill(isFillFieldMap.get(subFormCompon.getFieldId()));
								}

								listFormComponts.add(subFormCompon);
							}
						}
					}
				}else{
					if(componentKeys.contains(componentKey) && null!=title && !"".equals(title)
							&& !"DataTable".equals(componentKey)){//有组件名称,且不是动态表单
						//父类是动态表单
						if(null!=parentKeyType && parentKeyType.equals("DataTable")){
							continue;
						}
						//父组件类型不为日期区间
						if(null!=parentKeyType && !parentKeyType.equals("DateInterval")){
							listFormComponts.add(formCompon);
						}
					}else if("DataTable".equals(componentKey)){//是子表单
						//查询子表单子主键
						List<FormCompon> subFormCompons = formDao.listAllFormComponeNoOption(comId,formKey,layoutId,formCompon.getId());
						if(null!=subFormCompons && !subFormCompons.isEmpty()){//有子组件
							//设置子表单号
							if(!fieldId_dataIndex.containsKey(fieldId)){//设置子表单序号
								formCompon.setSubFormIndex(subFormIndex);
								fieldId_dataIndex.put(fieldId,subFormIndex);
								subFormIndex++;
							}else{
								formCompon.setSubFormIndex(fieldId_dataIndex.get(fieldId));
							}
							//设置显示的名称
							String subTitle="子表单"+fieldId_dataIndex.get(fieldId);
							//子主键标题
							String subStr = "";
							for (FormCompon subFormCompon : subFormCompons) {
								if(null!=subFormCompon.getTitle() && subFormCompon.getTitle().length()>0){
									subStr = ","+subFormCompon.getTitle()+subStr;
								}
								if(authorFieldSet.contains(subFormCompon.getFieldId())){//子组件被选中了
									formCompon.setChecked(1);//设置为选中状态
								}
							}
							if(subStr.length()>=11){
								subStr = subStr.substring(1,11);
							}else{
								subStr = subStr.substring(1,subStr.length());
							}
							formCompon.setTitle(subTitle+"("+subStr+")");
							listFormComponts.add(formCompon);
						}
					}

				}

			}
		}
//		//上一个循环做了些判断导致无法完全清理之前的数据
//		for(FormCompon formCompon : listFormComponts){
//			//去除非TextArea的选项
//			if(!formCompon.getComponentKey().equals("TextArea")){
//				formCompon.setIsFill(-1);
//			}else{
//				//这一步判断是作为清理之前的数据，因为之前的所有isFill都设置的-1。
//				if(null!=formCompon.getIsFill() && formCompon.getIsFill()==-1){
//					formCompon.setIsFill(0);
//				}
//			}
//		}
		return listFormComponts;

	}
	
	/**
	 * 取得表单所有可编辑组件
	 * @param comId 团队号
	 * @param formKey 表单模板主键
	 * @param layoutId 布局主键
	 * @return
	 */
	public List<FormCompon> listAllFormCompont(Integer comId, Integer formKey,Integer layoutId){
		if(null==layoutId || layoutId==0){
			//取得模板信息
			FormMod formMod = formDao.getFormModVersion(formKey,comId);
			layoutId = formMod.getLayoutId();
		}
		List<FormCompon> listFormComponts = new ArrayList<FormCompon>();
		
		List<FormCompon> listFormCompont = formDao.listAllFormComponeNoOption(comId,formKey,layoutId,-1);
		if(null!=listFormCompont && listFormCompont.size()>0){
			List<String> componentKeys = Arrays.asList(componentKeyArray);
			Integer subFormIndex = 1;
			
			//记录子表单子主键标识和索引号
			Map<Integer,Integer> fieldId_dataIndex = new HashMap<Integer,Integer>();
			//记录组件主键和组件类型
			Map<Integer, String> comPonKeyType = new HashMap<Integer, String>();
			//只保留
			for (FormCompon formCompon : listFormCompont) {
				Integer fieldId = formCompon.getFieldId();
				//组件类型
				String componentKey = formCompon.getComponentKey();
				//组件名称
				String title = formCompon.getTitle();
				
				//组件父类主键
				Integer parentId = formCompon.getParentId();
				//存放组件主键----组件类型
				comPonKeyType.put(formCompon.getId(), formCompon.getComponentKey());
				//父组件类型
				String parentKeyType = comPonKeyType.get(parentId);
				if(componentKeys.contains(componentKey) && null!=title && !"".equals(title)
						&& !"DataTable".equals(componentKey)){//有组件名称,且不是动态表单
					//父类是动态表单
					if(null!=parentKeyType && parentKeyType.equals("DataTable")){
						continue;
					}
					//父组件类型不为日期区间
					if(null!=parentKeyType && !parentKeyType.equals("DateInterval")){
						listFormComponts.add(formCompon);
					}
				}else if("DataTable".equals(componentKey)){//是子表单
					//查询子表单子主键
					List<FormCompon> subFormCompons = formDao.listAllFormComponeNoOption(comId,formKey,layoutId,formCompon.getId());
					if(null!=subFormCompons && !subFormCompons.isEmpty()){//有子组件
						//设置子表单号
						if(!fieldId_dataIndex.containsKey(fieldId)){//设置子表单序号
							formCompon.setSubFormIndex(subFormIndex);
							fieldId_dataIndex.put(fieldId,subFormIndex);
							subFormIndex++;
						}else{
							formCompon.setSubFormIndex(fieldId_dataIndex.get(fieldId));
						}
						//设置显示的名称
						String subTitle="子表单"+fieldId_dataIndex.get(fieldId);
						//子主键标题
						String subStr = "";
						for (FormCompon subFormCompon : subFormCompons) {
							if(null!=subFormCompon.getTitle() && subFormCompon.getTitle().length()>0){
								subStr = ","+subFormCompon.getTitle()+subStr;
							}
						}
						if(subStr.length()>=11){
							subStr = subStr.substring(1,11);
						}else{
							subStr = subStr.substring(1,subStr.length());
						}
						formCompon.setTitle(subTitle+"("+subStr+")");
						listFormComponts.add(formCompon);
					}
				}
				
			}
		}
		return listFormComponts;
		
	}
	/**
	 * 取得表单所有可编辑组件
	 * @param comId 团队号
	 * @param formKey 表单模板主键
	 * @param layoutId 布局主键
	 * @return
	 */
	public List<FormCompon> listAllFormCompont4BusMap(Integer comId, Integer formKey,Integer layoutId){
		if(null==layoutId || layoutId==0){
			//取得模板信息
			FormMod formMod = formDao.getFormModVersion(formKey,comId);
			layoutId = formMod.getLayoutId();
		}
		List<FormCompon> listFormComponts = new ArrayList<FormCompon>();
		
		List<FormCompon> listFormCompont = formDao.listAllFormComponeNoOption(comId,formKey,layoutId,-1);
		if(null!=listFormCompont && !listFormCompont.isEmpty()){
			String[]  args=Arrays.copyOf(componentKeyArray,componentKeyArray.length+1);
			args[componentKeyArray.length]="DateInterval";
			List<String> componentKeys = Arrays.asList(componentKeyArray);
			
			
			Integer subFormIndex = 1;
			
			//记录子表单子主键标识和索引号
			Map<Integer,Integer> fieldId_dataIndex = new HashMap<Integer,Integer>();
			//记录组件主键和组件类型
			Map<Integer, String> comPonKeyType = new HashMap<Integer, String>();
			//只保留
			for (FormCompon formCompon : listFormCompont) {
				Integer fieldId = formCompon.getFieldId();
				//组件类型
				String componentKey = formCompon.getComponentKey();
				//组件名称
				String title = formCompon.getTitle();
				
				//组件父类主键
				Integer parentId = formCompon.getParentId();
				//存放组件主键----组件类型
				comPonKeyType.put(formCompon.getId(), formCompon.getComponentKey());
				//父组件类型
				String parentKeyType = comPonKeyType.get(parentId);
				if(componentKeys.contains(componentKey) 
						&& null!=title 
						&& !"".equals(title)
						&& !"DataTable".equals(componentKey)
						&& !"DateInterval".equals(componentKey)
						){//有组件名称,且不是动态表单
					//父类是动态表单
					if(null!=parentKeyType && parentKeyType.equals("DataTable")){
						continue;
					}
					//父组件类型不为日期区间
					if(null!=parentKeyType ){
						listFormComponts.add(formCompon);
						
					}
				}else if("DataTable".equals(componentKey)){//是子表单
					//查询子表单子主键
					List<FormCompon> subFormCompons = formDao.listAllFormComponeNoOption(comId,formKey,layoutId,formCompon.getId());
					if(null!=subFormCompons && !subFormCompons.isEmpty()){//有子组件
						//设置子表单号
						if(!fieldId_dataIndex.containsKey(fieldId)){//设置子表单序号
							formCompon.setSubFormIndex(subFormIndex);
							fieldId_dataIndex.put(fieldId,subFormIndex);
							subFormIndex++;
						}else{
							formCompon.setSubFormIndex(fieldId_dataIndex.get(fieldId));
						}
						//设置显示的名称
						String subTitle="子表单"+fieldId_dataIndex.get(fieldId);
						//子主键标题
						String subStr = "";
						for (FormCompon subFormCompon : subFormCompons) {
							if(null!=subFormCompon.getTitle() && subFormCompon.getTitle().length()>0){
								subStr = ","+subFormCompon.getTitle()+subStr;
							}
						}
						if(subStr.length()>=11){
							subStr = subStr.substring(1,11);
						}else{
							subStr = subStr.substring(1,subStr.length());
						}
						formCompon.setTitle(subTitle+"("+subStr+")");
						listFormComponts.add(formCompon);
					}
				}
				
			}
		}
		return listFormComponts;
		
	}
	/**
	 * 修改表单启用状态
	 * @param formMod
	 * @param userInfo
	 */
	public void updateFormModEnabled(FormMod formMod, UserInfo userInfo) {
		String enabled = formMod.getEnable();
		if(null!=enabled && enabled.equals("0")){//原先是禁用的，现在启用
			formMod.setEnable("1");
			formDao.update(formMod);
		}else{//原先是启用的，现在禁用
			formMod.setEnable("0");
			formDao.update(formMod);
			
			//TODO 删除流程步骤信息
			
		}
		
	}

	/**
	 * 添加表单模板分类
	 * @param formModSort
	 * @param userInfo
	 */
	public Integer addFormModSort(FormModSort formModSort) {
		return formDao.add(formModSort);
		
	}
	/**
	 * 删除表单模板分类
	 * @param formModSortId
	 * @param userInfo
	 */
	public boolean delFormModSort(Integer formModSortId, UserInfo userInfo) {
		boolean flag = false;
		FormModSort formModSort = (FormModSort) formDao.objectQuery(FormModSort.class, formModSortId);
		if(formModSort.getComId().equals(userInfo.getComId())){
			//先将表单模板已归类的重置为其他类
			formDao.delFormRelateModSort(formModSortId,userInfo.getComId());
			formDao.delById(FormModSort.class, formModSortId);
			flag = true;
		}
		return flag;
	}

	/**
	 * 初始化表单模板归类排序值
	 * @param comId
	 * @return
	 */
	public Integer queryFormModSortOrderMax(Integer comId) {
		FormModSort formModSort = formDao.queryFormModSortOrderMax(comId);
		return null==formModSort.getOrderNo()?1:formModSort.getOrderNo();
	}

	/**
	 * 查询所有表单分类
	 * @param formSort
	 * @param userInfo
	 * @return
	 */
	public List<FormModSort> listFormModSort(Integer comId) {
		List<FormModSort> listFormModSort = formDao.listFormModSort(comId);
		return listFormModSort;
	}

	/**
	 * 修改表单模板属性
	 * @param formModSort
	 * @param userInfo
	 */
	public void updateFormModSort(FormModSort formModSort) {
		formDao.update(formModSort);
	}

	/**
	 * 修改表单基本信息
	 * @param formMod
	 * @param userInfo
	 */
	public void updateFormMod(FormMod formMod) {
		formDao.update(formMod);
	}
	
	/**
	 * 取得动态表单主键
	 * @param layOutId
	 * @return
	 */
	public List<FormDataOption> listDataTables(Integer comId,Integer layOutId) {
		List<FormCompon> dataCompons = formDao.listDataTables(comId,layOutId);
		
		List<FormDataOption> result = new ArrayList<FormDataOption>();
		if(null!=dataCompons && !dataCompons.isEmpty()){
			for (FormCompon formComponObj : dataCompons) {
				Integer fieldId = formComponObj.getFieldId();
				List<FormConf> listFormConfig = formDao.listLayoutConf(comId, 
						formComponObj.getFormModId(), layOutId, fieldId);
				if(null!=listFormConfig && !listFormConfig.isEmpty()){
					for (FormConf formConf : listFormConfig) {
						String name = formConf.getConfName();
						if("defaultrows".equals(name.toLowerCase())){
							FormDataOption FormDataOption = new FormDataOption();
							FormDataOption.setSubFormId(formComponObj.getFieldId());
							FormDataOption.setMaxIndex(Integer.parseInt(formConf.getConfValue()));
							result.add(FormDataOption);
						}
					}
				}
			}
		}
		
		return result;
	}
	/**
	 * 步骤类型查询
	 * @param comId 团队号
	 * @param flowId 流程步骤关联主键
	 * @return
	 */
	public List<FormCompon> listSetpCompon(Integer comId, Integer flowId) {
		return formDao.listSetpCompon(comId,flowId);
	}

	/**
	 * 克隆表单模板
	 * @param userInfo 当前操作人员
	 * @param formModId 模块主键
	 */
	public void addFormModClone(UserInfo userInfo, Integer formModId) {
		//当前操作人员的主键
		Integer userId = userInfo.getId();
		//团队号
		Integer comId = userInfo.getComId();
		
		//取得需要克隆的表单模板
		FormMod formModClone = (FormMod) formDao.objectQuery(FormMod.class, formModId);
		formModClone.setId(null);
		formModClone.setRecordCreateTime(null);
		//设置名称
		String modName = formModClone.getModName()+"- 副本";
		formModClone.setModName(modName);
		if(formModClone.getComId()==0){//云表单默认克隆关闭
			//默认关闭
			formModClone.setEnable("0");
		}else{
			//默认开启
			formModClone.setEnable("1");
			
		}
		//添加表单模板
		Integer formModCloneId = formDao.add(formModClone);
		
		
		FormLayout formLayout = formDao.getLatestLayout(comId, formModId);
		if(formLayout!=null){
			//原有布局主键
			Integer formLayoutId = formLayout.getId();
			
			Integer formState = formLayout.getFormState();//是否替换过
			formState = formState == null?0:formState;
			
			//初始化克隆信息
			FormLayout formLayoutClone = new FormLayout();
			formLayoutClone.setFormModId(formModCloneId);
			formLayoutClone.setUserId(userId);
			formLayoutClone.setComId(comId);
			formLayoutClone.setVersion(0);
			
			formLayoutClone.setFormState(formState);
			
			//添加表单最新布局
			Integer formLayoutCloneId = formDao.add(formLayoutClone);
			//查询组件
			
			List<FormCompon> formCompones = formDao.listAllTreeFormCompone(comId, formModId, formLayoutId);
			if(null!= formCompones && !formCompones.isEmpty()){
				//旧的主键---新的主键
				Map<Integer, Integer> map = new HashMap<Integer, Integer>();
				for (FormCompon formComponClone : formCompones) {
					//原来的组件的主键
					Integer formComponId = formComponClone.getId();
					//原来的组件的父主键
					Integer formComponPId = formComponClone.getParentId();
					
					if(formComponPId>-1){//不是顶部的重新设置设置上级
						formComponClone.setParentId(map.get(formComponPId));
					}
					//设置关联的表单模板
					formComponClone.setFormModId(formModCloneId);
					//设置关联的布局主键
					formComponClone.setFormLayoutId(formLayoutCloneId);
					
					//清除原来主键
					formComponClone.setId(null);
					//清除原来创建时间
					formComponClone.setRecordCreateTime(null);
					
					//添加克隆数据
					Integer formComponCloneId = formDao.add(formComponClone);
					//保存原始主键---新的主键
					map.put(formComponId, formComponCloneId);	
				}
			}
			//配置文件克隆
			formDao.formConfClone(comId,formModId, formLayoutId,formModCloneId,formLayoutCloneId);
			if(formState == 1){
				formDao.formHtmlClone(comId,formModId, formLayoutId,formModCloneId,formLayoutCloneId);
			}
			
			//初始化克隆后的最大主键
			FormComponMaxField formComponMaxField = this.getFormMaxField(comId,formModId);
			formComponMaxField.setId(null);
			formComponMaxField.setComId(comId);
			formComponMaxField.setFormModId(formModCloneId);
			formDao.add(formComponMaxField);
		}
		
		
		
	}
	/**
	 * 克隆表单模板
	 * @param userInfo 当前操作人员
	 * @param formModId 模块主键
	 * @return 
	 */
	public Map<String, Object> updateBackFormLayout(UserInfo userInfo, Integer formModId) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//当前操作人员的主键
		Integer userId = userInfo.getId();
		//团队号
		Integer comId = userInfo.getComId();
		
		//添加表单模板
		Integer formModCloneId = formModId;
		
		FormLayout formLayout = formDao.getLatestSimpleLayout(comId, formModId);
		if( null == formLayout){
			resultMap.put("status", "f");
			resultMap.put("info", "表单没有简约版！");
			return resultMap;
		}
		
		//原有布局主键
		Integer formLayoutId = formLayout.getId();
		
		Integer formState = formLayout.getFormState();//是否替换过
		formState = formState == null?0:formState;
		
		//初始化克隆信息
		FormLayout formLayoutClone = new FormLayout();
		formLayoutClone.setFormModId(formModCloneId);
		formLayoutClone.setUserId(userId);
		formLayoutClone.setComId(comId);
		
		//取得模板的最新布局，用于版本号确定
		FormLayout formLayoutObj = formDao.queryFormLayoutByModId(formModId, comId);
		Integer version=0;
		if(null!=formLayoutObj){
			version = formLayoutObj.getVersion()+1;
		}
		formLayoutClone.setVersion(version);
		
		formLayoutClone.setFormState(formState);
		
		//添加表单最新布局
		Integer formLayoutCloneId = formDao.add(formLayoutClone);
		//查询组件
		
		
		FormComponMaxField formComponMaxField = this.getFormMaxField(comId,formModId);
		
		List<FormCompon> formCompones = formDao.listAllTreeFormCompone(comId, formModId, formLayoutId);
		if(null!= formCompones && !formCompones.isEmpty()){
			
			//旧的主键---新的主键
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (FormCompon formComponClone : formCompones) {
				//原来的组件的主键
				Integer formComponId = formComponClone.getId();
				//原来的组件的父主键
				Integer formComponPId = formComponClone.getParentId();
				
				if(formComponPId>-1){//不是顶部的重新设置设置上级
					formComponClone.setParentId(map.get(formComponPId));
				}
				//设置关联的表单模板
				formComponClone.setFormModId(formModCloneId);
				//设置关联的布局主键
				formComponClone.setFormLayoutId(formLayoutCloneId);
				
				//清除原来主键
				formComponClone.setId(null);
				//清除原来创建时间
				formComponClone.setRecordCreateTime(null);
				
				//添加克隆数据
				Integer formComponCloneId = formDao.add(formComponClone);
				//保存原始主键---新的主键
				map.put(formComponId, formComponCloneId);	
			}
		}
		//配置文件克隆
		formDao.formConfClone(comId,formModId, formLayoutId,formModCloneId,formLayoutCloneId);
		if(formState == 1){
			formDao.formHtmlClone(comId,formModId, formLayoutId,formModCloneId,formLayoutCloneId);
		}
		
		
		//修改表单的最大组件标识
		formDao.update(formComponMaxField);
		
		resultMap.put("status", "y");
		return resultMap;
		
		
	}

	/**
	 * 分页查询云表单列表
	 * @param formMod
	 * @return
	 */
	public List<FormMod> listPagedCloudFormMod(FormMod formMod) {
		return formDao.listPagedCloudFormMod(formMod);
	}

	/**
	 * 验证表单是否同名
	 * @param comId
	 * @param modName
	 * @return
	 */
	public List<FormMod> checkFormModName(Integer comId,
			String modName) {
		return formDao.checkFormModName(comId,modName);
	}

	/**
	 * 云表单下载
	 * @param userInfo 当前操作人员
	 * @param cloudFormModId 云表单主键
	 * @param modName 表单名称
	 * @param formSortId 
	 * @return
	 */
	public void addFormModFormCloud(UserInfo userInfo, Integer cloudFormModId,
			String modName, Integer formSortId) {
		
		//当前操作人员的主键
		Integer userId = userInfo.getId();
		//团队号
		Integer comId = userInfo.getComId();
		
		//取得需要克隆的表单模板
		FormMod formMod = (FormMod) formDao.objectQuery(FormMod.class, cloudFormModId);
		//清空主键
		formMod.setId(null);
		//清空时间
		formMod.setRecordCreateTime(null);
		//设置云表单主键
		formMod.setCloudModId(cloudFormModId);
		//设置归类
		formMod.setFormSortId(formSortId);
		//设置团队号
		formMod.setComId(comId);
		
		//设置名称
		formMod.setModName(modName);
		//默认开启
		formMod.setEnable("1");
		//添加表单模板
		Integer formModId = formDao.add(formMod);
		
		//取得最新布局（从云表单中）
		FormLayout cloudeFormLayout = formDao.getLatestLayout(0, cloudFormModId);
		if(cloudeFormLayout != null){
			//原有布局主键
			Integer cloudeFormLayoutId = cloudeFormLayout.getId();
			
			Integer formState = cloudeFormLayout.getFormState();//是否替换过
			formState = formState == null?0:formState;
			
			//初始化克隆信息
			FormLayout formLayout = new FormLayout();
			formLayout.setFormModId(formModId);
			formLayout.setUserId(userId);
			formLayout.setComId(comId);
			formLayout.setVersion(0);
			formLayout.setFormState(cloudeFormLayout.getFormState());
			//添加表单最新布局
			Integer formLayoutId = formDao.add(formLayout);
			//查询组件（从云表单中）
			List<FormCompon> formCompones = formDao.listAllTreeFormCompone(0, cloudFormModId, cloudeFormLayoutId);
			if(null!= formCompones && !formCompones.isEmpty()){
				//旧的主键---新的主键
				Map<Integer, Integer> map = new HashMap<Integer, Integer>();
				for (FormCompon formCompon : formCompones) {
					//原来的组件的主键
					Integer formComponPreId = formCompon.getId();
					//原来的组件的父主键
					Integer formComponPId = formCompon.getParentId();
					
					if(formComponPId>-1){//不是顶部的重新设置设置上级
						formCompon.setParentId(map.get(formComponPId));
					}
					//设置关联的表单模板
					formCompon.setFormModId(formModId);
					//设置关联的布局主键
					formCompon.setFormLayoutId(formLayoutId);
					
					//清除原来主键
					formCompon.setId(null);
					//清除原来创建时间
					formCompon.setRecordCreateTime(null);
					formCompon.setComId(comId);
					
					//添加克隆数据
					Integer formComponId = formDao.add(formCompon);
					//保存原始主键---新的主键
					map.put(formComponPreId, formComponId);	
				}
			}
			//配置文件克隆
			formDao.cloudFormConfClone(comId,cloudFormModId, cloudeFormLayoutId,formModId,formLayoutId);
			//克隆配置文件
			if(formState == 1){
				formDao.cloudFormHtmlClone(comId,cloudFormModId, cloudeFormLayoutId,formModId,formLayoutId);
			}
			
			//初始化克隆后的最大主键
			FormComponMaxField formComponMaxField = this.getFormMaxField(0,cloudFormModId);
			formComponMaxField.setId(null);
			formComponMaxField.setComId(comId);
			formComponMaxField.setFormModId(formModId);
			formDao.add(formComponMaxField);
			
		}
	}

	/**
	 * 取得单个组件信息
	 * @param componeId
	 * @return
	 */
	public FormCompon getFormComponById(Integer componeId) {
		return (FormCompon) formDao.objectQuery(FormCompon.class, componeId);
	}
	
	/**
	 * 修改表单布局信息
	 * @param userInfo 当前操作人员
	 * @param formModLayout 布局信息
	 */
	public void updateFormComponDev(UserInfo userInfo,FormModLayout formModLayout) {
		//模板信息
		FormMod formMod = formModLayout.getFormMod();
		//布局信息
		FormLayout formLayout = formModLayout.getFormLayout();
		//布局详情
		String layoutDetailStr = formLayout.getLayoutDetail();
		formLayout.setLayoutDetail(null);
		
		//使用过的组件标识
		Set<Integer> usedFields = this.constrUsedFields(layoutDetailStr);
		
		//TODO 可以的话，判断两次布局是否相同
		//模块主键
		Integer formModId = formLayout.getFormModId();
		if(null==formModId){
			return;
		}
		//取得表单模板的团队号
		Integer comId = ((FormMod)formDao.objectQuery(FormMod.class, formModId)).getComId();
		if(comId != 0 && !comId.equals(userInfo.getComId())){//团队号验证
			return;
		}
		formDao.update(formMod);
		
		//取得模板的最新布局，用于版本号确定
		FormLayout formLayoutObj = formDao.queryFormLayoutByModId(formModId, comId);
		Integer version=0;
		if(null!=formLayoutObj){
			version = formLayoutObj.getVersion()+1;
		}
		//重新保存布局文件
		formLayout.setId(null);
		//模板主键
		formLayout.setFormModId(formModId);
		//设置版本号
		formLayout.setVersion(version);
		//设置创建人
		formLayout.setUserId(userInfo.getId());
		//设置团队号
		formLayout.setComId(comId);
		//已替换
		formLayout.setFormState(1);
		//添加布局
		Integer formLayoutId = formDao.add(formLayout);
		
		//添加html信息
		FormLayoutHtml formLayoutHtml = formLayout.getFormLayoutHtml();
		if(null!=formLayoutHtml){
			//模板主键
			formLayoutHtml.setFormModId(formModId);
			//设置团队号
			formLayoutHtml.setComId(comId);
			//设置关联布局信息
			formLayoutHtml.setFormLayoutId(formLayoutId);
			//添加布局
			formDao.add(formLayoutHtml);
		}
		
		FormComponMaxField formComponMaxField = this.getFormMaxField(comId,formModId);
		
		//只统计日期区间的临时主键与正式主键+只统计数字的临时主键与正式主键
		Map<String,String> tempId_realDateFieldId = new HashMap<String, String>();
		//计算控件的临时主键与正式主键(用于添加配置)
		Map<String,List<MonitorField>> fieldId_list = new HashMap<String, List<MonitorField>>();
		//金额控件的临时主键与正式主键(用于添加配置)
		Map<String,String> moneyTempId_list = new HashMap<String, String>();
		
		Set<String> fieldIdSet = new HashSet<String>();
				
				
		
		com.alibaba.fastjson.JSONArray layoutDetailObjs = com.alibaba.fastjson.JSONArray.parseArray(layoutDetailStr);
		if(null!=layoutDetailObjs && !layoutDetailObjs.isEmpty()){
			for (Object object : layoutDetailObjs) {
				com.alibaba.fastjson.JSONObject layoutDetailObj = (com.alibaba.fastjson.JSONObject) object;
				LayoutDetail layoutDetail = com.alibaba.fastjson.JSONObject.toJavaObject(layoutDetailObj, LayoutDetail.class);
				
				//添加布局组件
				this.addFormComponDev(comId,formModId,formLayoutId,-1,layoutDetail,
						formComponMaxField,usedFields,tempId_realDateFieldId,fieldId_list,moneyTempId_list,fieldIdSet);
				
			}
		}
		if(null!=fieldId_list && !fieldId_list.isEmpty()){
			Set<String> ketSet = fieldId_list.keySet();
			for (String calFieldId : ketSet) {
				//取得计算控件的计算对象集合
				List<MonitorField> list = fieldId_list.get(calFieldId);
				if(null != list && !list.isEmpty()){
					for (MonitorField monitorField : list) {
						FormConf formConf = new FormConf();
						//设置企业号
						formConf.setComId(comId);
						//设置表单模板主键
						formConf.setFormModId(formModId);
						//设置表单布局主键
						formConf.setFormLayoutId(formLayoutId);
						//设置表单组件主键
						formConf.setFieldId(Integer.parseInt(calFieldId));
						//设置配置名称
						formConf.setConfName("type_value");
						
						String thisValue = tempId_realDateFieldId.get(monitorField.getValue());
						
						String type_value = monitorField.getType()+"_"+thisValue;
						//设置配置项值
						formConf.setConfValue(type_value);
						formDao.add(formConf);
					}
				}
			}
		}
		if(null!=moneyTempId_list && !moneyTempId_list.isEmpty()){
			Iterator<Map.Entry<String, String>> iterator = moneyTempId_list.entrySet().iterator();
			for(;iterator.hasNext();){
				Map.Entry<String, String> entry =iterator.next(); 
				String moneyFieldId = entry.getKey();
				String actVal = entry.getValue();
				
				FormConf formConf = new FormConf();
				//设置企业号
				formConf.setComId(comId);
				//设置表单模板主键
				formConf.setFormModId(formModId);
				//设置表单布局主键
				formConf.setFormLayoutId(formLayoutId);
				//设置表单组件主键
				formConf.setFieldId(Integer.parseInt(moneyFieldId));
				//设置配置名称
				formConf.setConfName("moneyColumn");
				
				String thisValue = tempId_realDateFieldId.get(actVal);
				//设置配置项值
				formConf.setConfValue(thisValue);
				formDao.add(formConf);
			}
		}
		
		//修改表单的最大组件标识
		formDao.update(formComponMaxField);
		
	}

	/**
	 * 查询已经使用的组件标识
	 * @param layoutDetailStr
	 * @return
	 */
	private Set<Integer> constrUsedFields(String layoutDetailStr) {
		Set<Integer> usedFields = new HashSet<>();
		String regx = "\"fieldId\":\"\\d+\"";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(layoutDetailStr);
		//查找符合规则的子串 
		while(matcher.find()){ 
			String result = "{"+matcher.group()+"}";
			JSONObject json = JSONObject.parseObject(result);
			Integer fieldId = json.getInteger("fieldId");
			usedFields.add(fieldId);
		} 
		return usedFields;
	}
	
	/**
	 * 添加组件配置
	 * @param comId 当前操作员
	 * @param formModId 表单主键
	 * @param formLayoutId 布局主键
	 * @param parentId 父类主键
	 * @param layout 布局详情
	 * @param formComponMaxField 当前系统最大fieldId
	 * @param usedFields 已经使用过的组件标识
	 * @param tempId_realDateFieldId 日期区间的临时主键 与 fieldId
	 * @param fieldId_list 计算控件的filed 与 计算控件集合
	 * @param moneyTempId_list 
	 * @param fieldIdSet 
	 */
	private void addFormComponDev(Integer comId, Integer formModId,
			Integer formLayoutId,Integer parentId, LayoutDetail layout, FormComponMaxField formComponMaxField,
			Set<Integer> usedFields, Map<String, String> tempId_realDateFieldId, Map<String, List<MonitorField>> fieldId_list, 
			Map<String, String> moneyTempId_list, Set<String> fieldIdSet) {
		if(null!=layout && !"".equals(layout)){//有布局详情
			//组件类型
			String componentKey = layout.getComponentKey();
			if(null!=componentKey && !"".equals(componentKey)){//组件类型不为空
				layout.setSubFormId(null);
				//取出子布局
				List<LayoutDetail> layoutDetailList = layout.getLayoutDetail();
				layout.setLayoutDetail(null);
				//取出布局选项
				List<FormOption> formOptionlist = layout.getOptions();
				layout.setOptions(null);
				
				
				//日期区间始
				LayoutDetail start = layout.getStart();
				layout.setStart(null);
				//日期之间止
				LayoutDetail end = layout.getEnd();
				layout.setEnd(null);
				
				//添加组件配置
				Integer formComponId = this.addFormConfDev(comId, formModId, formLayoutId, parentId, 
						layout,formComponMaxField,usedFields,tempId_realDateFieldId,fieldId_list,moneyTempId_list,fieldIdSet);
				
				//添加时间区间开始布局的配置
				this.addFormComponDev(comId,formModId,formLayoutId,formComponId,start,formComponMaxField,usedFields,
						tempId_realDateFieldId,fieldId_list,moneyTempId_list,fieldIdSet);
				//添加时间区间截止布局的配置
				this.addFormComponDev(comId,formModId,formLayoutId,formComponId,end,formComponMaxField,usedFields,
						tempId_realDateFieldId,fieldId_list,moneyTempId_list,fieldIdSet);
				
				//子布局集合
				if(null!=layoutDetailList && !layoutDetailList.isEmpty()){
					for (LayoutDetail layoutDetailObj : layoutDetailList) {
						//添加组件
						this.addFormComponDev(comId,formModId,formLayoutId,formComponId,layoutDetailObj,
								formComponMaxField,usedFields,tempId_realDateFieldId,fieldId_list,moneyTempId_list,fieldIdSet);
					}
				}
				if(null!=formOptionlist && !formOptionlist.isEmpty()){
					for (FormOption formOptionObj : formOptionlist) {
						//添加选项组件
						this.addFormConfOptDev(comId,formModId,formLayoutId,formComponId,formOptionObj,formComponMaxField,usedFields,fieldIdSet);
					}
				}
			}
			
		}
		
		
	}
	
	/**
	 * 添加普通的布局配置
	 * @param comId 当前操作人员
	 * @param formModId 模块主键
	 * @param formLayoutId 布局主键
	 * @param formComponId 组件主键
	 * @param layout 布局信息
	 * @param usedFields 已经使用过的组件标识
	 * @param fieldId_list 
	 * @param tempId_realDateFieldId 
	 * @param moneyTempId_list 
	 * @param fieldIdSet 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Integer addFormConfDev(
			Integer comId, 
			Integer formModId,
			Integer formLayoutId, 
			Integer formComponPId, 
			LayoutDetail layout,FormComponMaxField formComponMaxField, 
			Set<Integer> usedFields,
			Map<String, String> tempId_realDateFieldId, 
			Map<String, List<MonitorField>> fieldId_list, 
			Map<String, String> moneyTempId_list, 
			Set<String> fieldIdSet) {
		
		Integer formComponId  = 0;
		if(null!=layout && !"".equals(layout)){
			FormCompon formCompon = new FormCompon();
			String componentKey = layout.getComponentKey();
			//设置企业号
			formCompon.setComId(comId);
			//设置父类主键
			formCompon.setParentId(formComponPId);
			//设置模板主键
			formCompon.setFormModId(formModId);
			//设置所在布局主键
			formCompon.setFormLayoutId(formLayoutId);
			//设置布局类型
			formCompon.setComponentKey(componentKey);
			//设置标题
			formCompon.setTitle(layout.getTitle());
			//默认字段标识为主键
			Integer fieldId = layout.getFieldId();
			
			
			Integer maxFieldId = formComponMaxField.getMaxFieldId();
			
			if(null != fieldId && fieldId >0 
					&& !fieldIdSet.contains(fieldId.toString())){//组件是否有指定字段
				formCompon.setFieldId(fieldId);
				
				fieldIdSet.add(fieldId.toString());
				
				if(maxFieldId < fieldId){
					formComponMaxField.setMaxFieldId(fieldId);
				}
				
			}else{
				//取得组件标识
				fieldId = this.getNextFieldId(maxFieldId,usedFields);
				
				formComponMaxField.setMaxFieldId(fieldId);
				
				formCompon.setFieldId(fieldId);
				fieldIdSet.add(fieldId.toString());
			}
			
			formComponId = formDao.add(formCompon);
			
			if(componentKey.equals("Monitor")){
				String monitorType =  layout.getMonitorType();
				if("date".equals(monitorType)){
					List<MonitorField> monitorFields = new ArrayList<MonitorField>();
					MonitorField monitorField = new MonitorField();
					monitorField.setType("date");
					monitorField.setValue(layout.getMonitorTempFields());
					monitorFields.add(monitorField);
					fieldId_list.put(fieldId.toString(), monitorFields);
				}else if("number".equalsIgnoreCase(monitorType)){
					List<MonitorField> monitorFields = layout.getComponentIds();
					fieldId_list.put(fieldId.toString(), monitorFields);
				}
				
				tempId_realDateFieldId.put(layout.getTempId(), fieldId.toString());
				
			}else if(componentKey.equals("MoneyComponent")){
				moneyTempId_list.put(fieldId.toString(), layout.getMoneyTempColumn());
			}else if(componentKey.equals("DateInterval") 
					|| componentKey.equalsIgnoreCase("NumberComponent")){
				tempId_realDateFieldId.put(layout.getTempId(), fieldId.toString());
			}
			//字段标识不存放于配置文件
			layout.setFieldId(null);
			
			Class clz = layout.getClass();
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				String getMethodName = "get"+CommonUtil.toFirstLetterUpperCase(name); 
				Object value = "";
				
				try{
					if(name.equalsIgnoreCase("componentIds") && componentKey.equals("Monitor")){
						List<MonitorField> monitorFields = layout.getComponentIds();
						List<String> monitorField = new ArrayList<String>();
						if(null != monitorFields && !monitorFields.isEmpty()){
							for (MonitorField monitor : monitorFields) {
								monitorField.add(monitor.getValue());
							}
							value = monitorField.toString();
						}
					}else{
						//取值
						value = clz.getMethod(getMethodName).invoke(layout); 
					}
				}catch(Exception e){  
				} 
				
				if(null!=value && !"".equals(value)){
					FormConf formConf = new FormConf();
					//设置企业号
					formConf.setComId(comId);
					//设置表单模板主键
					formConf.setFormModId(formModId);
					//设置表单布局主键
					formConf.setFormLayoutId(formLayoutId);
					//设置表单组件主键
					formConf.setFieldId(fieldId);
					//设置配置名称
					formConf.setConfName(name);
					//设置配置项值
					formConf.setConfValue(value.toString());
					formDao.add(formConf);
				}
			}
		}
		return formComponId;
		
	}
	
	/**
	 * 取得下一个组件的唯一标识
	 * @param maxFieldId 当前最大的组件
	 * @param usedFields 已有组件标识
	 * @return
	 */
	private Integer getNextFieldId(Integer maxFieldId, Set<Integer> usedFields) {
		Integer fieldId = maxFieldId + 1;
		while(usedFields.contains(fieldId)){
			fieldId = fieldId + 1;
		}
		usedFields.add(fieldId);
		return fieldId;
	}

	/**
	 * 添加有选项的布局配置
	 * @param comId 当前操作人员
	 * @param formModId 
	 * @param formLayoutId
	 * @param formComponPId
	 * @param formComponMaxField 
	 * @param fieldIdSet 
	 * @param layout
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addFormConfOptDev(Integer comId, Integer formModId,
			Integer formLayoutId, Integer formComponPId, FormOption formOption, 
			FormComponMaxField formComponMaxField,Set<Integer> usedFields,
			Set<String> fieldIdSet) {
		if(null!=formOption && !"".equals(formOption)){
			FormCompon formCompon = new FormCompon();
			String componentKey = formOption.getComponentKey();
			//设置企业号
			formCompon.setComId(comId);
			//设置父类主键
			formCompon.setParentId(formComponPId);
			//设置模板主键
			formCompon.setFormModId(formModId);
			//设置所在布局主键
			formCompon.setFormLayoutId(formLayoutId);
			//设置布局类型
			formCompon.setComponentKey(componentKey);
			//设置标题
			formCompon.setTitle(formOption.getName());
			if(!StringUtil.isBlank(formOption.getOrder())){
				//设置所在布局的排序号
				formCompon.setOrderNo(formOption.getOrder().toString());
			}
			//添加组件
			Integer formComponId =formDao.add(formCompon);
			formCompon.setId(formComponId);
			
			Integer maxFieldId = formComponMaxField.getMaxFieldId();
			//原有字段标识
			String fieldId = formOption.getFieldId();
			if(null!= fieldId && !"".equals(fieldId) && !fieldIdSet.contains(fieldId)){
				Integer intFieldId = Integer.parseInt(fieldId);
				formCompon.setFieldId(intFieldId);
				
				fieldIdSet.add(fieldId);
				if(intFieldId > maxFieldId ){
					formComponMaxField.setMaxFieldId(intFieldId);
				}
			}else{
				maxFieldId = this.getNextFieldId(maxFieldId, usedFields);
				formComponMaxField.setMaxFieldId(maxFieldId);
				formCompon.setFieldId(maxFieldId);
				
				fieldId = maxFieldId+"";
				fieldIdSet.add(fieldId);
			}
			formDao.update(formCompon);
			
			List<FormOption> formOptionlist = formOption.getChildren();
			formOption.setChildren(null);
			
			Class clz = formOption.getClass();
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				String getMethodName = "get"+CommonUtil.toFirstLetterUpperCase(name); 
				try{  
					Object value = clz.getMethod(getMethodName).invoke(formOption); 
					if(null!=value && !"".equals(value) && !"fieldid".equals(name.toLowerCase())){
						FormConf formConf = new FormConf();
						//设置企业号
						formConf.setComId(comId);
						//设置表单模板主键
						formConf.setFormModId(formModId);
						//设置表单布局主键
						formConf.setFormLayoutId(formLayoutId);
						//设置表单组件主键
						formConf.setFieldId(Integer.parseInt(fieldId));
						//设置配置名称
						formConf.setConfName(name);
						//设置配置项值
						formConf.setConfValue(value.toString());
						formDao.add(formConf);
					}
				}catch(Exception e){  
				}  
			}
			
			if(null!=formOptionlist && !formOptionlist.isEmpty()){
				for (FormOption formOptionObj : formOptionlist) {
					//添加选项
					this.addFormConfOptDev(comId,formModId,formLayoutId,formComponId,formOptionObj,formComponMaxField,usedFields,fieldIdSet);
				}
			}
		}
		
	}
	
	/**
	 * 取得表单最新布局
	 * @param formModId
	 * @param userInfo
	 * @return
	 */
	public FormLayout queryLayoutByModIdDev(Integer formModId,UserInfo userInfo) {
		
		//取得表单模板的团队号
		Integer comId = ((FormMod)formDao.objectQuery(FormMod.class, formModId)).getComId();
		if(comId != 0 && !comId.equals(userInfo.getComId())){//团队号验证
			return null;
		}
				
		FormLayout formLayout = formDao.queryFormLayoutByModId(formModId,comId);
		if(null!=formLayout){
			Integer formLayoutId = formLayout.getId();
			
			List<LayoutDetail> layoutDetails = this.listFormComponDev(formModId,formLayoutId,comId,-1,false,null,null);;
			
			Gson g = new Gson();
			String msgObj = g.toJson(layoutDetails);
			formLayout.setLayoutDetail(msgObj);
			FormLayoutHtml formLayoutHtml = formDao.queryHtmlByLayoutId(formLayout.getId());
			formLayout.setFormLayoutHtml(formLayoutHtml);
		}
		
		return formLayout;
	}
	
	/**
	 * 查询表单组件信息
	 * @param formModId 表单模板主键
	 * @param formLayoutId 表单布局主键
	 * @param comId 当前操作人员
	 * @param parentId 父级组件主键
	 * @param author 是否需要授权
	 * @param requireFields 映射关系必填项
	 * @return
	 */
	public List<LayoutDetail> listFormComponDev(Integer formModId,Integer formLayoutId,
			Integer comId,Integer parentId, boolean author,Set<Integer> authFields,
			Set<Integer> requireFields){
		
		//最外层布局信息
		List<FormCompon> topFormCompons = new ArrayList<FormCompon>();
		//自己的子节点信息
		Map<Integer, List<FormCompon>> subFormComponMap = new HashMap<Integer, List<FormCompon>>();
		//查询所有的控件集合
		List<FormCompon> listFormCompon = formDao.listAllTreeCompon(formModId,formLayoutId,comId);
		//遍历控件集合，设置关联关系
		if(null!=listFormCompon && !listFormCompon.isEmpty()){
			for (FormCompon formCompon : listFormCompon) {
				//组件的父节点主键
				Integer pId = formCompon.getParentId();
				//父节点的子组件
				List<FormCompon> subFormCompons = subFormComponMap.get(pId);
				if(null==subFormCompons){
					//首次的初始化
					subFormCompons = new ArrayList<FormCompon>();
				}
				//存放添加子组件
				subFormCompons.add(formCompon);
				//父节点的子组件
				subFormComponMap.put(pId, subFormCompons);
				if(pId == -1){
					//最外层的布局组件
					topFormCompons.add(formCompon);
				}
			}
		}
		//组件配置信息集合
		List<FormConf> listAllFormConfig = formDao.listLayoutConf(comId, 
				formModId, formLayoutId, null);
		//各个组件的配置集合
		Map<Integer,List<FormConf>> formConfMap = new HashMap<Integer,List<FormConf>>();
		if(null!=listAllFormConfig && !listAllFormConfig.isEmpty()){
			for (FormConf formConf : listAllFormConfig) {
				//组件标识
				Integer fieldId = formConf.getFieldId();
				//组件配置
				List<FormConf> fileConf = formConfMap.get(fieldId);
				if(null==fileConf){
					fileConf = new ArrayList<FormConf>();
				}
				//组件的配置
				fileConf.add(formConf);
				//添加配置与组件的关系
				formConfMap.put(fieldId, fileConf);
			}
		}
		
		
		//组件有配置文件
		List<LayoutDetail> layoutDetails = new ArrayList<LayoutDetail>();
		if(null!=topFormCompons && !topFormCompons.isEmpty()){
			for (FormCompon formCompon : topFormCompons) {
				//构建组件信息
				LayoutDetail layoutDetail = this.constrLayoutDetailDev(formCompon,
						subFormComponMap,formConfMap,author,authFields,requireFields);
				//添加组件
				layoutDetails.add(layoutDetail);
				
			}	
		}
		
		return layoutDetails;
		
	}
	/**
	 * 构建布局信息
	 * @param formCompon 组件信息
	 * @param subFormComponMap 所有组件的子节点集合
	 * @param formConfMap 所有配置集合
	 * @param author 是否需要授权
	 * @param authFields 授权的组件集合
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	private LayoutDetail constrLayoutDetailDev(FormCompon formCompon,
			Map<Integer, List<FormCompon>> subFormComponMap,
			Map<Integer, List<FormConf>> formConfMap, boolean author,
			Set<Integer> authFields,Set<Integer> requireFields) {
		
		//组件字段标识
		Integer fieldId = formCompon.getFieldId();
		//返回的布局信息
		LayoutDetail layoutDetail = new LayoutDetail();
		//设置组件的标识
		layoutDetail.setFieldId(fieldId);
		//设置临时组件信息
		layoutDetail.setTempId(fieldId.toString());
		//组件类型标识
		String componentKey = formCompon.getComponentKey();
		//设置子表单
		layoutDetail.setSubFormId(formCompon.getId().toString());
		
		//组件配置信息集合
		List<FormConf> listFormConfig = formConfMap.get(fieldId);
		
		//用于计算控件
		List<String> type_valueList = new ArrayList<String>();
		
		//反编译填充配置信息
		Class clz = layoutDetail.getClass();
		if(null!=listFormConfig && listFormConfig.size()>0){
			for (FormConf formConf : listFormConfig) {
				String name = formConf.getConfName();
				String setMethodName = "set"+CommonUtil.toFirstLetterUpperCase(name); 
				
				if("type_value".equals(name)){
					type_valueList.add(formConf.getConfValue());
					continue;
				}
				if(componentKey.equals("TableLayout") && 
						"thArray".equals(name)){
					String thArrayStr = formConf.getConfValue();
					if(!StringUtil.isBlank(thArrayStr)){
						List<Integer> thArray = JSONArray.parseArray(thArrayStr, Integer.class);
						layoutDetail.setThArray(thArray);
					}
				}else if(componentKey.equalsIgnoreCase("Monitor") && name.equalsIgnoreCase("componentIds")){
					String monitorTempFields = formConf.getConfValue();
					layoutDetail.setMonitorTempFields(monitorTempFields);
				}else{
					Method m;
					try {
						m = clz.getMethod(setMethodName, String.class);
						m.invoke(layoutDetail, formConf.getConfValue());
					} catch (Exception e) {
//								loger.warn(setMethodName+"-----------"+formConf.getConfValue(),e);
					}
				}
				
			}
		}
		
		if(componentKey.equals("CheckBox")//复选框
			||componentKey.equals("RadioBox")//单选框
			||componentKey.equals("Select")){//下拉设置选项
			//选项信息
			List<FormOption> formOptions = this.listFormOptionDev(formCompon.getId(),
					subFormComponMap,formConfMap);
			layoutDetail.setOptions(formOptions);
		}else if(componentKey.equals("DateInterval")){//日期区间设置起止组件
			List<LayoutDetail> layoutDetailSon = this.listFormComponDev(
					subFormComponMap,formConfMap,formCompon.getId(),author,authFields,requireFields);
			//若子必填，则父必填
			String isSubRequire =  layoutDetailSon.get(0).getRequired();
			if(StringUtils.isNotEmpty(isSubRequire) && isSubRequire.equals("true")){
				layoutDetail.setRequired("true");
			}
			layoutDetail.setStart(layoutDetailSon.get(0));
			layoutDetail.setEnd(layoutDetailSon.get(1));
			
		}else if(componentKey.equals("ComboSelect")){//多级下拉设置组件信息
			List<LayoutDetail> selects = this.listFormComponDev(
					subFormComponMap,formConfMap,formCompon.getId(),author,authFields,requireFields);
			layoutDetail.setSelects(selects);
			List<FormOption> Options = this.listFormOptionDev(formCompon.getId(),
					subFormComponMap,formConfMap);
			layoutDetail.setOptions(Options);
		}else if(componentKey.equals("Option")){//选项信息，则不操作
			return null;
		}
		
		if(componentKey.equals("Monitor") && !type_valueList.isEmpty()){
			List<MonitorField> monitorFields = new ArrayList<MonitorField>();
			for (String type_value : type_valueList) {
				MonitorField monitorField = new MonitorField();
				monitorField.setType(type_value.split("_")[0]);
				monitorField.setValue(type_value.split("_")[1]);
				monitorFields.add(monitorField);
			}
			layoutDetail.setMonitorFields(monitorFields);
		}
		
		if(formCompon.getIsLeaf()==0){//有子节点
			if(!componentKey.equals("CheckBox")
				&& !componentKey.equals("RadioBox")
				&&!componentKey.equals("DateInterval")
				&&!componentKey.equals("ComboSelect")
				&& !componentKey.equals("Select")){//没有选项信息的组件需配置子组件信息
				List<LayoutDetail> layoutDetailSon = this.listFormComponDev(
						subFormComponMap,formConfMap,formCompon.getId(),author,authFields,requireFields);
				layoutDetail.setLayoutDetail(layoutDetailSon);
			}
		}
		
		
		//TODO 在此处设置权限
		if(author){
			//所有控件默认可以编辑			
			layoutDetail.setIsReadOnly("true");
			if(null == authFields || authFields.isEmpty() || !authFields.contains(fieldId)){//没有权限的
				if(componentKey.equals("Employee")){
					layoutDetail.setIsDefault("false");
					layoutDetail.setIsCurrentEmployee("false");
				}else if(componentKey.equals("Department")){
					layoutDetail.setIsDefault("fasle");
					layoutDetail.setIsCurrentDepartment("true");
					layoutDetail.setIsReadonly("true");
				}else if(componentKey.equals("RelateItem")){
					layoutDetail.setIsDefault("fasle");
					layoutDetail.setIsReadonly("true");
				}else if(componentKey.equals("Monitor")){//计算控件不能编辑
					layoutDetail.setIsEdit("false");
				}else if(componentKey.equals("DateComponent")){
					layoutDetail.setIsSystemDate("false");//没有权限的不设定默认时间
				}
				layoutDetail.setIsReadOnly("false");
				layoutDetail.setRequired("false");
			}else{//有权限不能编辑
				if(componentKey.equals("Monitor")){//计算控件不能编辑
					layoutDetail.setIsEdit("false");
				}
			}
			//映射关系设置的必填
			if(requireFields.contains(fieldId)){
				layoutDetail.setRequired("true");
			}
		}
		return layoutDetail;
	}

	/**
	 * 设置子布局信息
	 * @param subFormComponMap 所有控件的集合
	 * @param formConfMap 所有配置集合
	 * @param formComponId 组件主键
	 * @param author 是否授权
	 * @param authFields 授权的组件集合
	 * @return
	 */
	private List<LayoutDetail> listFormComponDev(Map<Integer, List<FormCompon>> subFormComponMap,
			Map<Integer, List<FormConf>> formConfMap, Integer formComponId,
			boolean author, Set<Integer> authFields,Set<Integer> requireFields) {
			//组件有配置文件
			List<LayoutDetail> layoutDetails = new ArrayList<LayoutDetail>();
			if(null!=subFormComponMap.get(formComponId) && !subFormComponMap.get(formComponId).isEmpty()){
				for (FormCompon formCompon : subFormComponMap.get(formComponId)) {
					LayoutDetail layoutDetail = this.constrLayoutDetailDev(formCompon,
							subFormComponMap,formConfMap,author,authFields,requireFields);
					layoutDetails.add(layoutDetail);
				}	
			}
		return layoutDetails;
	}
	/**
	 * 设置选项信息
	 * @param formComponPId 父节点主键
	 * @param subFormComponMap 所有子组件集合
	 * @param formConfMap 所有配置集合
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<FormOption> listFormOptionDev(Integer formComponPId, Map<Integer, List<FormCompon>> subFormComponMap, Map<Integer, List<FormConf>> formConfMap){
		//组件有配置文件
		List<FormOption> formOptions = new ArrayList<FormOption>();
		
		//查询一级子组件信息
		List<FormCompon> listFormCompon = subFormComponMap.get(formComponPId);
		if(null!=listFormCompon && !listFormCompon.isEmpty()){
			for (FormCompon formCompon : listFormCompon) {
				//组件字段标志
				Integer fieldId = formCompon.getFieldId();
				//组件类型
				String componentKey = formCompon.getComponentKey();
				if(null== componentKey || componentKey.equals("Select")){//单独去取
					continue;
				}
				FormOption formOption = new FormOption();
				//选项的组件标识
				formOption.setFieldId(fieldId.toString());
				formOption.setSelectionId(fieldId.toString());
				
				//组件配置信息集合
				List<FormConf> listFormConfig = formConfMap.get(fieldId);
				//反编译填充配置信息
				Class clz = formOption.getClass();
				if(null!=listFormConfig && !listFormConfig.isEmpty()){
					for (FormConf formConf : listFormConfig) {
						String name = formConf.getConfName();
						if(name.equalsIgnoreCase("fieldId")
								|| name.equalsIgnoreCase("selectionId")){
							continue;
						}
						String setMethodName = "set"+CommonUtil.toFirstLetterUpperCase(name); 
						Method m;
						try {
							m = clz.getMethod(setMethodName, String.class);
							m.invoke(formOption, formConf.getConfValue());
						} catch (Exception e) {
//							loger.warn(setMethodName+"-----------"+formConf.getConfValue(), e);
						}
					}
				}
				
				if(formCompon.getIsLeaf()==0){//有子节点的组件需配置子组件信息
					List<FormOption> formOptionSon = this.listFormOptionDev(formCompon.getId(),
							subFormComponMap,formConfMap);
					formOption.setChildren(formOptionSon);
				}
				
				formOptions.add(formOption);
			}
		}
		
		return formOptions;
		
	}
	
	/**
	 * 取得表单布局信息
	 * @param formModId 表单模板主键
	 * @param formLayoutId 布局主键
	 * @param userInfo 当前操作员
	 * @return
	 */
	public FormLayout queryWorkFlowCompDev(Integer formModId,Integer formLayoutId,Integer instanceId,UserInfo userInfo) {
		FormLayout formLayout = new FormLayout();
		if(null==formLayoutId || 0==formLayoutId ){//没有布局信息，则查询
			formLayout = formDao.queryFormLayoutByModId(formModId,userInfo.getComId());
			//布局主键
			formLayoutId = formLayout.getId();
		}else{
			formLayout = (FormLayout) formDao.objectQuery(FormLayout.class, formLayoutId);
		}
		
		if(null!=formLayout){
			
			FormLayoutHtml formLayoutHtml = formDao.queryHtmlByLayoutId(formLayout.getId());
			formLayout.setFormLayoutHtml(formLayoutHtml);
			Set<Integer> authorFieldSet = new HashSet<Integer>();
			SpFlowInstance instance = workFlowService.getSpFlowInstanceById(instanceId, userInfo);//获取流程实例化对象信息
			Integer stepId = null;
			if(instance.getFlowState()==0 || instance.getFlowState()==2){//流程是开始或 草稿状态的时候，起始步骤默认为0
				stepId = 0;
			}
			Set<Integer> requireFields = new HashSet<Integer>();
			if(instance.getFlowState() != 4){//没有办结
				
				if(null!=stepId && stepId == 0 && CommonUtil.isNull(instance.getFlowId())){//自由流程起始节点权限授权
					List<FormCompon> listAllFormCompont = this.listAllFormCompont(userInfo.getComId(), instance.getFormKey(),formLayoutId,null,null);
					if(!CommonUtil.isNull(listAllFormCompont)){
						for (FormCompon formCompon2 : listAllFormCompont) {
							authorFieldSet.add(formCompon2.getFieldId());
						}
					}
				}else{
					//获取开始步骤授权集合
					List<SpFlowRunStepFormControl> listCheckedFormCompon = workFlowService.
							listSpFlowRunStepFormControl(instance, userInfo, stepId);
					if(null!=listCheckedFormCompon && !listCheckedFormCompon.isEmpty()){
						for (SpFlowRunStepFormControl spStepFormControl : listCheckedFormCompon) {
							authorFieldSet.add(Integer.parseInt(spStepFormControl.getFormControlKey()));
						}
					}
					//取得需要映射的数据
					List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp = formDao.listRequireCol(instanceId);
					if(null!=listBusAttrMapFormColTemp && !listBusAttrMapFormColTemp.isEmpty()){
						for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
							requireFields.add(Integer.parseInt(busAttrMapFormColTemp.getFormCol()));
						}
					}
				}
			}
			List<LayoutDetail> layoutDetails = this.listFormComponDev(
					formModId,formLayoutId,userInfo.getComId(),-1,true,authorFieldSet,requireFields);
			Gson g = new Gson();
			String msgObj = g.toJson(layoutDetails);
			formLayout.setLayoutDetail(msgObj);
		}
		return formLayout;
	}

	/**
	 * 查询数据表信息
	 * @param tableName
	 * @param columns
	 * @return
	 */
	public List<TableInfoVo> listColumnInfo(String tableName) {
		//取得数据表的信息
		Set<String> columns = CommonUtil.getSysColumn(tableName).keySet();
		if(null==columns || columns.isEmpty()){
			return null;
		}
		return formDao.listColumnInfo(tableName,columns);
	}

	/**
	 * 查询字典表数据
	 * @param sessionUser 
	 * @param sysDicName 字典表名称
	 * @return
	 */
	public List<Object> listSysDicInfo(UserInfo sessionUser,String sysDicName) {
		List<Object> list = new ArrayList<>();
		try {
			Class clz = Class.forName("com.westar.base.model."+sysDicName);
			list = formDao.listSysDicInfo(sessionUser.getComId(),sysDicName,clz);
		} catch (ClassNotFoundException e) {
		}
		
		return list;
	}

	/**
	 * 查询
	 * @param userInfo
	 * @param instanceId
	 * @return
	 */
	public List<LayoutDetail> listLayoutDetailForConsume(UserInfo userInfo,
			Integer instanceId) {
		
		List<LayoutDetail> layoutDetails = new ArrayList<LayoutDetail>();
		//子表单数据
		List<FormCompon> compons = formDao.listDataTableCompone(userInfo,instanceId);
		if(null!=compons && !compons.isEmpty()){
			for (FormCompon formCompon : compons) {
				//字表单配置
				LayoutDetail layoutDetail = this.constrLayoutDetail(userInfo,formCompon);
				//子表单有关联模块
				String relateTable = layoutDetail.getRelateTable();
				if(StringUtils.isEmpty(relateTable)){
					continue;
				}
				//查询子表单组件
				List<FormCompon> subCompons = formDao.listAllFormComponeNoOption(userInfo.getComId(), 
						formCompon.getFormModId(), formCompon.getFormLayoutId(), formCompon.getId());
				if(null!=subCompons && !subCompons.isEmpty()){
					for (FormCompon subFormCompon : subCompons) {
						//子表单组件配置
						LayoutDetail subLayoutDetail = this.constrLayoutDetail(userInfo,subFormCompon);
						subLayoutDetail.setSubFormId(formCompon.getId().toString());
						subLayoutDetail.setFieldId(subFormCompon.getFieldId());
						layoutDetails.add(subLayoutDetail);
					}
				}
				
			}
		}
		return layoutDetails;
	}

	private LayoutDetail constrLayoutDetail(UserInfo userInfo,
			FormCompon formCompon) {
		List<FormConf> listFormConfig = formDao.listLayoutConf(userInfo.getComId(), formCompon.getFormModId(), formCompon.getFormLayoutId(), formCompon.getFieldId());
		LayoutDetail layoutDetail = new LayoutDetail();
		Class clz = layoutDetail.getClass();
		
		if(null!=listFormConfig && listFormConfig.size()>0){
			for (FormConf formConf : listFormConfig) {
				String name = formConf.getConfName();
				String getMethodName = "set"+CommonUtil.toFirstLetterUpperCase(name); 
				Method m;
				try {
					m = clz.getMethod(getMethodName, String.class);
					m.invoke(layoutDetail, formConf.getConfValue());
				} catch (Exception e) {
//					loger.warn(getMethodName, e);
				}
			}
		}
		return layoutDetail;
	}
	
}
