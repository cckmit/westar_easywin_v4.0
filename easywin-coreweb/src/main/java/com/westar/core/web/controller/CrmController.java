package com.westar.core.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Area;
import com.westar.base.model.Customer;
import com.westar.base.model.CustomerHandOver;
import com.westar.base.model.CustomerLog;
import com.westar.base.model.CustomerStage;
import com.westar.base.model.CustomerType;
import com.westar.base.model.CustomerUpfile;
import com.westar.base.model.FeedBackInfo;
import com.westar.base.model.FeedBackType;
import com.westar.base.model.FeedInfoFile;
import com.westar.base.model.Item;
import com.westar.base.model.LinkMan;
import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.OlmContactWay;
import com.westar.base.model.Region;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.ModStaticVo;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.ExcelExportUtil;
import com.westar.base.util.PinyinToolkit;
import com.westar.base.util.RequestContextHolderUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.service.ClockService;
import com.westar.core.service.CrmService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.ItemService;
import com.westar.core.service.ModOptConfService;
import com.westar.core.service.OutLinkManService;
import com.westar.core.service.TaskService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

/**
 * 客户中心
 * 
 * @author lj
 * 
 */
@Controller
@RequestMapping("/crm")
public class CrmController extends BaseController {

	@Autowired
	CrmService crmService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	ModOptConfService modOptConfService;

	@Autowired
	TodayWorksService todayWorksService;

	@Autowired
	ClockService clockService;

	@Autowired
	ItemService itemService;

	@Autowired
	TaskService taskService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	OutLinkManService outLinkManService;

	/**
	 * 查询客户列表导出数据
	 * @param request
	 * @param response
	 * @param fileName 导出文件名
	 * @return
	 */
	@RequestMapping("/excelExportCrmList")
	public ModelAndView excelExportCrmList(HttpServletRequest request, HttpServletResponse response,String fileName,Customer customer,
			String listOwnerStr,String listCrmTypeStr){
		UserInfo userInfo = this.getSessionUser();
		if(null != listOwnerStr && !listOwnerStr.isEmpty()){
			Gson gson = new Gson();
			Customer owner = gson.fromJson(listOwnerStr, Customer.class);
			if(null != owner.getListOwner() && !owner.getListOwner().isEmpty()){
				customer.setListOwner(owner.getListOwner());
			}
		}
		if(null != listCrmTypeStr && !listCrmTypeStr.isEmpty()){
			Gson gson = new Gson();
			Customer owner = gson.fromJson(listCrmTypeStr, Customer.class);
			if(null != owner.getListCrmType() && !owner.getListCrmType().isEmpty()){
				customer.setListCrmType(owner.getListCrmType());
			}
		}
		// 客户区域ID初始化
		if (null != customer.getAreaIdAndType()&& !"".equals(customer.getAreaIdAndType().trim())) {
			customer.setAreaId(Integer.parseInt(customer.getAreaIdAndType().split("@")[0]));
		} else {
			customer.setAreaId(0);
		}
		customer.setComId(userInfo.getComId());
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_CRM);
		List<Customer> listCustomer = crmService.listCustomerForExport(userInfo.getId(), isForceIn,customer);
		
		ExcelExportUtil.exportExcel(listCustomer, fileName, response, request);
		return null;
	}
	/**
	 * 获取客户列表数据
	 * 
	 * @param customer
	 * @return
	 */
	@RequestMapping("/customerListPage")
	public ModelAndView customerList(HttpServletRequest request,Customer customer,
			String listOwnerStr,String listCrmTypeStr) {
		// 清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/crm/customerList");
		UserInfo userInfo = this.getSessionUser();

		if(null != listOwnerStr && !listOwnerStr.isEmpty()){
			Gson gson = new Gson();
			Customer owner = gson.fromJson(listOwnerStr, Customer.class);
			if(null != owner.getListOwner() && !owner.getListOwner().isEmpty()){
				customer.setListOwner(owner.getListOwner());
			}
		}
		if(null != listCrmTypeStr && !listCrmTypeStr.isEmpty()){
			Gson gson = new Gson();
			Customer owner = gson.fromJson(listCrmTypeStr, Customer.class);
			if(null != owner.getListCrmType() && !owner.getListCrmType().isEmpty()){
				//客户类型穿透条件
				if(null!= customer.getCustomerTypeId() && customer.getCustomerTypeId()>0){
					CustomerType crmType  = crmService.queryCustomerType(userInfo.getComId(), customer.getCustomerTypeId());
					Set<Integer> typeId = new HashSet<Integer>();
					for(CustomerType type :owner.getListCrmType()){
						typeId.add(type.getId());
					}
					if(!typeId.contains(crmType.getId())){
						owner.getListCrmType().add(crmType);
					}
				}
				
				customer.setListCrmType(owner.getListCrmType());
			}
		}else{
			if(null!= customer.getCustomerTypeId() && customer.getCustomerTypeId()>0){
				CustomerType crmType  = crmService.queryCustomerType(userInfo.getComId(), customer.getCustomerTypeId());
				List<CustomerType> listCrmType = new ArrayList<CustomerType>();
				listCrmType.add(crmType);
				customer.setListCrmType(listCrmType);
			}
		}
		
		view.addObject("userInfo", userInfo);
		customer.setComId(userInfo.getComId());
		
		// 客户区域ID初始化
		if (null != customer.getAreaIdAndType()&& !"".equals(customer.getAreaIdAndType().trim())) {
			customer.setAreaId(Integer.parseInt(customer.getAreaIdAndType().split("@")[0]));
			customer.setAreaName(crmService.getAreaById(customer.getAreaId()).getAreaName());
		} else {
			customer.setAreaId(0);
		}
		// 初始化客户负责人姓名
		if (null != customer.getOwner() && customer.getOwner() != 0) {
			customer.setOwnerName(userInfoService.getUserInfo(
					userInfo.getComId(), customer.getOwner()).getUserName());
		}

		view.addObject("customer", customer);

		// 负责人类别
		String ownerType = customer.getOwnerType();
		// 若是没有下属，则没有负责人类别一说
		if (userInfo.getCountSub() <= 0 && null != ownerType && "1".equals(ownerType)) {
			customer.setOwnerType(null);
		}

		view.addObject("countSub", userInfo.getCountSub());
		// 取得模块的数据
		List<Customer> listCustomer = crmService.listCustomerForPage(customer, userInfo);
		view.addObject("listCustomer", listCustomer);
		// 获取客户阶段数据源
		List<CustomerStage> listCrmStage = crmService.listCustomerStage(userInfo.getComId());
		view.addObject("listCrmStage", listCrmStage);
		// 获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),
						ConstantInterface.TYPE_CRM);
		if (null != listModuleOperateConfig) {
			for (ModuleOperateConfig vo : listModuleOperateConfig) {
				view.addObject(vo.getOperateType(), ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		// 头文件的显示
		view.addObject("homeFlag", ConstantInterface.TYPE_CRM);
		return view;
	}
	/**
	 * 获取各类型客户资金预算统计
	 * @param customer
	 * @param listOwnerStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listCrmStatisByBudget")
	public Map<String, Object>  listCrmStatisByBudget(Customer customer,String listOwnerStr){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		
		if(null != listOwnerStr && !listOwnerStr.isEmpty()){
			Gson gson = new Gson();
			Customer owner = gson.fromJson(listOwnerStr, Customer.class);
			if(null != owner.getListOwner() && !owner.getListOwner().isEmpty()){
				customer.setListOwner(owner.getListOwner());
			}
		}
		// 客户区域ID初始化
		if (null != customer.getAreaIdAndType() && !"".equals(customer.getAreaIdAndType().trim())) {
			customer.setAreaId(Integer.parseInt(customer.getAreaIdAndType().split("@")[0]));
			customer.setAreaName(crmService.getAreaById(customer.getAreaId()).getAreaName());
		} else {
			customer.setAreaId(0);
		}
		customer.setComId(userInfo.getComId());
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_CRM);
		
		List<ModStaticVo> listStatis = crmService.listCrmStatisByBudget(customer,userInfo.getId(), isForceIn);
		
		map.put("listStatis", listStatis);
		return map;
	}
	/**
	 * 异步取得客户的分页数据
	 * 
	 * @param customer
	 *            条件
	 * @param pageNum
	 *            当前页码
	 * @param pageSize
	 *            分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxCustomerList")
	public Map<String, Object> ajaxCustomerList(Customer customer,
			Integer pageNum, Integer pageSize) {

		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0
				: pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		customer.setComId(userInfo.getComId());

		// 客户区域ID初始化
		if (null != customer.getAreaIdAndType()
				&& !"".equals(customer.getAreaIdAndType().trim())) {
			customer.setAreaId(Integer.parseInt(customer.getAreaIdAndType()
					.split("@")[0]));
		} else {
			customer.setAreaId(0);
		}

		// 负责人类别
		String ownerType = customer.getOwnerType();
		// 若是没有下属，则没有负责人类别一说
		if (userInfo.getCountSub() <= 0 && null != ownerType
				&& "1".equals(ownerType)) {
			customer.setOwnerType(null);
		}

		// 取得模块的数据
		List<Customer> listCustomer = crmService.listCustomerForPage(customer, userInfo);
		List<Customer> list = new ArrayList<Customer>();
		if (null != listCustomer && listCustomer.size() > 0) {
			for (Customer crm : listCustomer) {
				String content = crm.getModifyContent();
				if (null != content && !"".equals(content)) {
					// 截取内容
					content = StringUtil.cutString(content, 302,null);
					// 格式转换
					content = StringUtil.toHtml(content);
				} else {
					content = "";
				}
				crm.setModifyContent(content);
				list.add(crm);

			}
		}
		map.put("list", list);
		
		//除开页面已有数据的总数
		Integer total = PaginationContext.getTotalCount();
		//页面已展示的项目主键
		String crmIds = customer.getCrmIds();
		if(null!=crmIds && !"".equals(crmIds)){
			//页面展示的总数为本次查询数+页面已展示数
			total = customer.getCrmIds().split(",").length + total;
		}
		map.put("crmNum", total);

		map.put("status", "y");
		return map;

	}

	/**
	 * 跳转区域维护页面
	 * 
	 * @param area
	 * @return
	 */
	@RequestMapping("/editAreaPage")
	public ModelAndView editArea(Area area) {
		ModelAndView view = new ModelAndView("/crm/editArea");
		UserInfo userInfo = this.getSessionUser();
		area.setEnable(true);
		//String areaJsonStr = crmService.areaJsonStr(userInfo.getComId());
		view.addObject("area", area);
		//view.addObject("areaJsonStr", areaJsonStr);
		view.addObject("userInfo", userInfo);
		return view;
	}
	
	/**
	 * 异步取得数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetArea4Edit", method = RequestMethod.POST)
	public Map<String, Object> ajaxGetArea4Edit() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
		} else {
			List<Area> listArea = crmService.listArea(userInfo.getComId());
			map.put("status", "y");
			map.put("listArea",listArea);
		}
		return map;
	}

	/**
	 * 跳转区域添加页面
	 * 
	 * @param area
	 * @return
	 */
	@RequestMapping("/addAreaPage")
	public ModelAndView addAreaPage(Area area) {
		ModelAndView view = new ModelAndView("/crm/addArea");
		UserInfo userInfo = this.getSessionUser();
		area.setEnable(true);
		Integer areaOrder = crmService.queryAreaOrderMax(userInfo.getComId(),
				area.getParentId());
		area.setAreaOrder(areaOrder);
		view.addObject("area", area);
		return view;
	}

	/**
	 * 区域添加
	 * 
	 * @param area
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addArea", method = RequestMethod.POST)
	public Map<String, Object> addArea(Area area) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
		} else {
			area.setComId(userInfo.getComId());
			area.setCreator(userInfo.getId());
			// 用户名称不为空时此时添加用户名称全拼和简拼
			if (null != area.getAreaName()) {
				area.setAllSpelling(PinyinToolkit.cn2Spell(area.getAreaName()));
				area.setFirstSpelling(PinyinToolkit.cn2FirstSpell(area
						.getAreaName()));
			}
			// 模板的主键
			area.setRegionId(0);

			crmService.addArea(area, userInfo);
			map.put("status", "y");

		}
		return map;
	}

	/**
	 * 从模板添加区域
	 * 
	 * @param areas
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddArea")
	public Map<String, Object> ajaxAddArea(@RequestBody List<Area> areas) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 从模板添加区域
		crmService.addAreaFromMod(areas, this.getSessionUser());
		this.setNotification(Notification.SUCCESS, "添加成功！");
		map.put("status", "y");
		return map;
	}

	/**
	 * 更新区域树形数据结构
	 * 
	 * @param nodeId
	 * @param pId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/zTreeOnDrop")
	public String zTreeOnDrop(Integer nodeId, Integer pId) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		boolean succ = crmService.zTreeOnDrop(nodeId, pId, userInfo);
		if (succ) {
			// StagedItem customerType = crmService.queryStagedItem(itemId, pId,
			// userInfo.getComId());
			// String logInfo = null;
			// if("folder".equals(nodeType)){
			// StagedItem dropObj = crmService.queryStagedItem(itemId, nodeId,
			// userInfo.getComId());
			// logInfo =
			// "\""+dropObj.getStagedName()+"\"被拖到\""+customerType.getStagedName()+"\"文件夹下";
			// }else{
			// StagedInfo dropObj = crmService.queryStagedInfo(nodeId, itemId,
			// userInfo.getComId(), nodeType);
			// logInfo =
			// "\""+dropObj.getModuleName()+"\"被拖到\""+customerType.getStagedName()+"\"文件夹下";
			// }
			// //模块日志添加
			// crmService.addItemLog(userInfo.getComId(),itemId,
			// userInfo.getId(), userInfo.getUserName(), logInfo);
			return "更新成功！";
		} else {
			return "更新失败！";
		}
	}

	/**
	 * 删除客户区域
	 * @param nodeId 节点主键
	 * @param delChildren 删除子节点标示
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/zTreeRemove",method=RequestMethod.POST)
	public Map<String,Object> zTreeRemove(Integer nodeId, String delChildren)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		if(null == userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		@SuppressWarnings("unused")
		String logInfo = null;
		Area dropArea = crmService.getAreaById(nodeId);
		List<Area> areaChildren = crmService.areaChildren(userInfo.getComId(),
				dropArea.getId());
		dropArea.setChildren(areaChildren);
		if (null != delChildren && !"".equals(delChildren)) {
			if ("yes".equals(delChildren)) {
				logInfo = "删除区域以及区域下数据";
			} else {
				logInfo = "删除区域";
			}
		}
		boolean succ = crmService.zTreeRemove(userInfo.getComId(), delChildren,
				dropArea, userInfo);
		if (succ) {
			// 模块日志添加
			// crmService.addItemLog(userInfo.getComId(),itemId,
			// userInfo.getId(), userInfo.getUserName(), logInfo);
			map.put("status", "y");
			map.put("info", "删除成功！");
			return map;
		} else {
			map.put("status", "f");
			map.put("info","该区域或子区域已被使用，不能删除");
			return map;
		}
	}

	/**
	 * 跳转从模板导入区域数据页面
	 * 
	 * @return
	 */
	@RequestMapping("/modAreaPage")
	public ModelAndView modAreaPage() {
		ModelAndView view = new ModelAndView("/crm/modAreaPage");
		return view;
	}

	/**
	 * 跳转从模板导入区域数据页面
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/modArea")
	public List<Region> modArea(Area area) {
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			return null;
		}
		// 模板区域集合
		List<Region> listRegion = crmService.listRegion(area,userInfo.getComId());
		return listRegion;
	}
	/*********************** 以上是区域维护 *************************************/
	/*********************** 以下是类型维护 *************************************/

	/**
	 * 跳转客户类型维护列表页面
	 * 
	 * @param customerType
	 * @return
	 */
	@RequestMapping("/listCustomerTypePage")
	public ModelAndView listCustomerType(CustomerType customerType) {
		ModelAndView view = new ModelAndView("/crm/listCustomerType");
		List<CustomerType> listCustomerType = crmService.listCustomerType(this
				.getSessionUser().getComId());
		view.addObject("listCustomerType", listCustomerType);
		return view;
	}

	/**
	 * 客户类型序号
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetCrmTypeOrder")
	public Map<String, Object> ajaxGetCrmTypeOrder() {

		Map<String, Object> map = new HashMap<String, Object>();
		Integer orderNum = crmService.queryCustomerTypeOrderMax(this
				.getSessionUser().getComId());
		// 取出 排序号
		map.put("orderNum", orderNum);

		map.put("status", "y");
		return map;
	}

	/**
	 * 客户类型添加
	 * 
	 * @param customerType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxAddCrmType")
	public Map<String, Object> ajaxAddCrmType(CustomerType customerType)
			throws Exception {

		UserInfo userInfo = this.getSessionUser();
		customerType.setComId(userInfo.getComId());
		customerType.setCreator(userInfo.getId());
		Integer id = crmService.addCustomerType(customerType, userInfo);

		customerType.setId(id);
		customerType.setTypeOrder(customerType.getTypeOrder() + 1);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerType", customerType);
		map.put("status", "y");
		return map;
	}

	/**
	 * 更新客户类型名称
	 * 
	 * @param customerType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/typeNameUpdate")
	public String typeNameUpdate(CustomerType customerType) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customerType.setComId(userInfo.getComId());
		boolean succ = crmService.updateTypeName(customerType, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 更新客户类型排序
	 * 
	 * @param customerType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/typeOrderUpdate")
	public String typeOrderUpdate(CustomerType customerType) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customerType.setComId(userInfo.getComId());
		boolean succ = crmService.updateTypeOrder(customerType, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	/**
	 * 更新维护周期
	 * @param customerType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateModifyPeriod")
	public String updateModifyPeriod(CustomerType customerType) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customerType.setComId(userInfo.getComId());
		boolean succ = crmService.updateModifyPeriod(customerType, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 删除客户类型
	 * @param customerType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/customerTypeDel")
	public CustomerType customerTypeDel(CustomerType customerType)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customerType.setComId(userInfo.getComId());
		boolean succ = crmService.delCustomerType(customerType, userInfo);
		customerType.setSucc(succ);
		if (succ) {
			customerType.setPromptMsg("删除成功");
		} else {
			customerType.setPromptMsg("该类型已被使用，不能删除！");
		}
		return customerType;
	}

	/************************* 以上是类型维护 ************************************************/
	/***************************** 以下是反馈类型 *****************************************/
	/**
	 * 跳转反馈类型列表页面
	 * 
	 * @param feedBackType
	 * @return
	 */
	@RequestMapping("/listFeedBackTypePage")
	public ModelAndView listFeedBackType(FeedBackType feedBackType) {
		ModelAndView view = new ModelAndView("/crm/listFeedBackType");
		List<FeedBackType> listFeedBackType = crmService.listFeedBackType(this
				.getSessionUser().getComId());
		view.addObject("listFeedBackType", listFeedBackType);
		return view;
	}

	/**
	 * 跳转反馈类型添加页面
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetFeedBackTypeOrder")
	public Map<String, Object> ajaxGetFeedBackTypeOrder() {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Integer orderNum = crmService.queryFeedBackTypeOrderMax(sessionUser.getComId());
		// 取出 排序号
		map.put("orderNum", orderNum);

		map.put("status", "y");
		return map;
	}

	/**
	 * 添加客户反馈类型
	 * 
	 * @param feedBackType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddFeedBackType")
	public Map<String, Object> addFeedBackType(FeedBackType feedBackType) {
		UserInfo userInfo = this.getSessionUser();
		feedBackType.setComId(userInfo.getComId());
		feedBackType.setCreator(userInfo.getId());
		Integer id = crmService.addFeedBackType(feedBackType, userInfo);

		feedBackType.setId(id);
		feedBackType.setTypeOrder(feedBackType.getTypeOrder() + 1);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("feedBackType", feedBackType);
		map.put("status", "y");
		return map;

	}

	/**
	 * 更新客户反馈类型名称
	 * 
	 * @param feedBackType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateFeedBackTypeName")
	public String updateFeedBackTypeName(FeedBackType feedBackType)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		feedBackType.setComId(userInfo.getComId());
		boolean succ = crmService
				.updateFeedBackTypeName(feedBackType, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 更新客户反馈类型排序
	 * 
	 * @param feedBackType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateFeedBackTypeOrder")
	public String updateFeedBackTypeOrder(FeedBackType feedBackType)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		feedBackType.setComId(userInfo.getComId());
		boolean succ = crmService.updateFeedBackTypeOrder(feedBackType,
				userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 删除客户反馈类型
	 * 
	 * @param feedBackType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delFeedBackType")
	public FeedBackType delFeedBackType(FeedBackType feedBackType)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		feedBackType.setComId(userInfo.getComId());
		boolean succ = crmService.delFeedBackType(feedBackType, userInfo);
		feedBackType.setSucc(succ);
		if (succ) {
			feedBackType.setPromptMsg("删除成功");
		} else {
			feedBackType.setPromptMsg("该类型已被使用，不能删除！");
		}
		return feedBackType;
	}

	/***************************** 以上是反馈类型 *****************************************/
	/***************************** 以下是客户阶段 *****************************************/
	
	/**
	 * 跳转客户阶段列表页面
	 * @param crmStage
	 * @return
	 */
	@RequestMapping("/listCrmStage")
	public ModelAndView listCrmStage(CustomerStage crmStage) {
		ModelAndView view = new ModelAndView("/crm/listCrmStage");
		List<CustomerStage> listCrmStage = crmService.listCustomerStage(this.getSessionUser().getComId());
		view.addObject("listCrmStage", listCrmStage);
		return view;
	}

	/**
	 * 客户阶段最大序号
	 * @param 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetCrmStageOrder")
	public Map<String, Object> ajaxGetCrmStageOrder() {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		// 获取最大序号
		Integer orderNum = crmService.queryCrmStageOrderMax(sessionUser.getComId());
		// 取出 排序号
		map.put("orderNum", orderNum);

		map.put("status", "y");
		return map;
	}

	/**
	 * 添加客户阶段
	 * 
	 * @param crmStage
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddCrmStage")
	public Map<String, Object> ajaxAddCrmStage(CustomerStage crmStage) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		crmStage.setComId(userInfo.getComId());
		crmStage.setCreator(userInfo.getId());
		
		// 添加客户阶段
		Integer id = crmService.addCrmStage(crmStage, userInfo);

		crmStage.setId(id);
		crmStage.setOrderNum(crmStage.getOrderNum() + 1);

		
		map.put("crmStage", crmStage);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;

	}

	/**
	 * 更新客户阶段名称
	 * 
	 * @param crmStage
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateStageName")
	public String updateFeedBackTypeName(CustomerStage crmStage)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		crmStage.setComId(userInfo.getComId());
		boolean succ = crmService.updateStageName(crmStage, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 更新客户阶段排序
	 * 
	 * @param crmStage
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateStageOrder")
	public String updateStageOrder(CustomerStage crmStage)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		crmStage.setComId(userInfo.getComId());
		boolean succ = crmService.updateStageOrder(crmStage,
				userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 删除客户阶段类型
	 * 
	 * @param crmStage
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delCustomerStage")
	public CustomerStage delCustomerStage(CustomerStage crmStage)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		crmStage.setComId(userInfo.getComId());
		boolean succ = crmService.delCustomerStage(crmStage, userInfo);
		crmStage.setSucc(succ);
		if (succ) {
			crmStage.setPromptMsg("删除成功");
		} else {
			crmStage.setPromptMsg("客户阶段已使用,删除失败");
		}
		return crmStage;
	}
	/***************************** 以上是客户阶段 *****************************************/
	/************************* 以下是客户维护 ************************************************/
	/**
	 * 跳转客户添加页面
	 * 
	 * @param customer
	 * @return
	 */
	@RequestMapping(value = "/addCustomerPage")
	public ModelAndView addCustomerPage(Customer customer) {
		ModelAndView view = new ModelAndView("/crm/addCustomer");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);

		// 获取客户类型数据源
		List<CustomerType> listCustomerType = crmService.listCustomerType(this.getSessionUser().getComId());
		view.addObject("listCustomerType", listCustomerType);

		// 获取客户阶段数据源
		List<CustomerStage> listCrmStage = crmService.listCustomerStage(this.getSessionUser().getComId());
		view.addObject("listCrmStage", listCrmStage);
		
		// 取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(), userInfo.getId(), 6);
		view.addObject("listUsed", listOwners);

		return view;
	}

	/**
	 * 头部快速新增客户
	 * 
	 * @param customer
	 * @return
	 */
	@RequestMapping(value = "/addCustomerBySimple")
	public ModelAndView addCustomerBySimple(Customer customer) {
		ModelAndView view = new ModelAndView("/crm/addCustomerBySimple");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);

		// 获取客户类型数据源
		List<CustomerType> listCustomerType = crmService.listCustomerType(this.getSessionUser().getComId());
		view.addObject("listCustomerType", listCustomerType);

		// 获取客户阶段数据源
		List<CustomerStage> listCrmStage = crmService.listCustomerStage(this.getSessionUser().getComId());
		view.addObject("listCrmStage", listCrmStage);
		
		// 取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(), userInfo.getId(), 6);
		view.addObject("listUsed", listOwners);

		return view;
	}


	/**
	 * 删除客户分享组
	 * 
	 * @param customerId
	 *            客户主键
	 * @param groupId
	 *            分享组主键
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delCrmShareGroup")
	public Customer delCrmShareGroup(Integer customerId, Integer groupId)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		Customer customer = new Customer();
		boolean succ = crmService.delCrmShareGroup(customerId, groupId,userInfo);
		if (succ) {
			customer.setSucc(true);
			customer.setPromptMsg("删除成功");
		} else {
			customer.setSucc(false);
			customer.setPromptMsg("删除失败");
		}
		return customer;
	}

	/**
	 * 客户添加
	 * 
	 * @param customer
	 *            客户信息
	 * @param redirectPage
	 *            页面跳转
	 * @param way
	 *            跳转方式
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addCustomer")
	public ModelAndView addCustomer(Customer customer, String redirectPage,
			String way) throws Exception {

		UserInfo userInfo = this.getSessionUser();
		// 客户所在公司
		customer.setComId(userInfo.getComId());
		// 客户创建人
		customer.setCreator(userInfo.getId());
		// 删除标识(正常)
		customer.setDelState(0);

		// 初始化话客户所属区域ID
		if (null != customer.getAreaIdAndType()
				&& !"".equals(customer.getAreaIdAndType())) {
			customer.setAreaId(Integer.parseInt(customer.getAreaIdAndType()
					.split("@")[0]));
		}
		// 添加客户并保存
		crmService.addCustomer(customer, userInfo);
		this.setNotification(Notification.SUCCESS, "添加成功");

		ModelAndView view = new ModelAndView("/refreshParent");
		return view;
	}

	/**
	 * 验证与验证项目名称相似的项目
	 * 
	 * @param crmName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkSimilarCrmByName")
	public int checkSimilarCrmByName(String crmName) {
		if (null == crmName || "".equals(crmName.trim())) {
			return 0;
		}
		@SuppressWarnings("unused")
		List<Customer> listCrm = crmService.listSimilarCrmsByCheckCrmName(
				crmName, this.getSessionUser().getComId());
		return PaginationContext.getTotalCount();
	}

	/**
	 * 跳转显示相似客户列表页面
	 * 
	 * @param crmName
	 * @return
	 */
	@RequestMapping("/similarCrmsPage")
	public ModelAndView similarCrmsPage(String crmName) {
		ModelAndView view = new ModelAndView("/crm/listSimilarCrm");
		UserInfo userInfo = this.getSessionUser();
		if (null != crmName && !"".equals(crmName.trim())) {
			List<Customer> listCrm = crmService.listSimilarCrmsByCheckCrmName(
					crmName, userInfo.getComId());
			view.addObject("listCrm", listCrm);
		}
		return view;
	}

	/**
	 * 批量删除客户
	 * 
	 * @param ids
	 * @param redirectPage
	 * @return
	 * @throws IOException
	 * @throws CorruptIndexException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/delCustomer")
	public ModelAndView delCustomer(Integer[] ids, String redirectPage)
			throws CorruptIndexException, IOException, ParseException {
		ModelAndView mav = new ModelAndView();
		UserInfo userInfo = this.getSessionUser();
		// 是否有删除权限
		ModuleOperateConfig modOptConf = modOptConfService.getModuleOperateConfig(
				userInfo.getComId(),ConstantInterface.TYPE_CRM, "delete");

		// 模块操作权限验证
		if (null == modOptConf) {
			if (null != ids && ids.length > 0) {
				// 批量预删除客户
				crmService.delPreCustomer(ids, userInfo);
			}
			this.setNotification(Notification.SUCCESS, "删除成功!");
		} else {
			this.setNotification(Notification.ERROR, "删除失败，此模块不具备删除权限！");
		}
		mav.setViewName("redirect:" + redirectPage);
		return mav;
	}

	/**
	 * 客户信息查看
	 * 
	 * @param customer
	 * @return
	 */
	@RequestMapping(value = "/viewCustomer")
	public ModelAndView viewCustomer(HttpServletRequest request,Customer customer,
			 String redirectPage, Customer parameters, Integer clockId) {
		ModelAndView view = null;
		UserInfo userInfo = this.getSessionUser();

		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_CRM,customer.getId());
		// 取得是否添加浏览记录
		boolean bool = FreshManager.checkOpt(request, viewRecord);

		customer = crmService.queryCustomer(userInfo, customer.getId());
		if (null == customer) {
			// 验证查看人权限
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR, "抱歉，客户已被删除");
			return view;
		}
		// 客户查看权限验证
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_CRM);
		if (isForceIn) {//是监督人员
			if (bool) {
				// 添加查看记录
				viewRecordService.addViewRecord(userInfo, viewRecord);
			}

			if (customer.getOwner().equals(userInfo.getId())) {
				// 管理者页面跳转
				view = new ModelAndView("/crm/editCustomer");
				// 获取客户类型数据源
				List<CustomerType> listCustomerType = crmService.listCustomerType(userInfo.getComId());
				view.addObject("listCustomerType", listCustomerType);

				// 获取客户阶段数据源
				List<CustomerStage> listCrmStage = crmService.listCustomerStage(userInfo.getComId());
				view.addObject("listCrmStage", listCrmStage);
				
				// 判断是否有编辑权限
				ModuleOperateConfig modOptConf = modOptConfService.getModuleOperateConfig(userInfo.getComId(),
								ConstantInterface.TYPE_CRM, "update");
				if (null != modOptConf) {
					view.addObject("editCrm", "no");
				}
			} else {
				// 查看页面跳转
				view = new ModelAndView("/crm/viewCustomer");
			}
			view.addObject("customer", customer);
			view.addObject("listCustomerSharer",
					customer.getListCustomerSharer());
			view.addObject("listLinkMan", customer.getListLinkMan());
			// 客户区域JSON字符串
			String areaJsonStr = crmService.areaJsonStr(userInfo.getComId());
			view.addObject("areaJsonStr", areaJsonStr);
			view.addObject("userInfo", userInfo);

			// 获取客户成员组
			List<SelfGroup> listShareGroup = crmService.listShareGroupOfCrm(
					customer.getId(), userInfo.getComId());
			view.addObject("listShareGroup", listShareGroup);

			view.addObject("parameters", parameters);


		} else if (!crmService.authorCheck(userInfo.getComId(),
				customer.getId(), userInfo.getId())) {
			// 验证查看人权限
			view = new ModelAndView("/refreshSelf");
			view.addObject("redirectPage", redirectPage);
			this.setNotification(Notification.ERROR, "抱歉，你没有查看权限");
		} else {

			if (bool) {
				// 添加查看记录
				viewRecordService.addViewRecord(userInfo, viewRecord);
			}

			if (customer.getOwner().equals(userInfo.getId())) {
				// 管理者页面跳转
				view = new ModelAndView("/crm/editCustomer");

				// 获取客户类型数据源
				List<CustomerType> listCustomerType = crmService.listCustomerType(userInfo.getComId());
				view.addObject("listCustomerType", listCustomerType);

				// 获取客户阶段数据源
				List<CustomerStage> listCrmStage = crmService.listCustomerStage(userInfo.getComId());
				view.addObject("listCrmStage", listCrmStage);
				
				// 判断是否有编辑权限
				ModuleOperateConfig modOptConf = modOptConfService.getModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_CRM, "update");
				if (null != modOptConf) {
					view.addObject("editCrm", "no");
				}

			} else {
				// 查看页面跳转
				view = new ModelAndView("/crm/viewCustomer");
			}
			view.addObject("customer", customer);
			view.addObject("listCustomerSharer",
					customer.getListCustomerSharer());
			view.addObject("listLinkMan", customer.getListLinkMan());
			view.addObject("userInfo", userInfo);

			// 获取客户成员组
			List<SelfGroup> listShareGroup = crmService.listShareGroupOfCrm(
					customer.getId(), userInfo.getComId());
			view.addObject("listShareGroup", listShareGroup);

			view.addObject("parameters", parameters);
		}
		// 查看客户信息，删除客户提醒
		todayWorksService.updateTodoWorkRead(customer.getId(),
				userInfo.getComId(), userInfo.getId(),
				ConstantInterface.TYPE_CRM, clockId);

		return view;
	}
	/**
	 * 客户名称更新
	 * 
	 * @param customer
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/customerNameUpdate")
	public String customerNameUpdate(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customer.setComId(userInfo.getComId());
		boolean succ = crmService.updateCustomerName(customer, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 更新客户所属区域
	 * 
	 * @param customer
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/areaIdUpdate")
	public String areaIdUpdate(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		if (null != customer.getAreaIdAndType()
				&& !"".equals(customer.getAreaIdAndType())) {
			customer.setComId(userInfo.getComId());
			Integer areaId = Integer.parseInt(customer.getAreaIdAndType()
					.split("@")[0]);
			customer.setAreaId(areaId);

			boolean succ = crmService.updateCustomerAreaId(customer, userInfo);
			if (succ) {
				return "操作成功";
			} else {
				return "操作失败";
			}
		} else {
			return "客户区域为空";
		}
	}

	/**
	 * 客户类型更新
	 * 
	 * @param customer
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/customerTypeIdUpdate")
	public String customerTypeIdUpdate(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		if (null != customer.getCustomerTypeId() && !"".equals(customer.getCustomerTypeId())) {
			customer.setComId(userInfo.getComId());
			CustomerType customerType = crmService.queryCustomerType(userInfo.getComId(), customer.getCustomerTypeId());
			customer.setTypeName(customerType.getTypeName());
			boolean succ = crmService.updateCustomerTypeId(customer, userInfo);
			if (succ) {
				return "更新成功";
			} else {
				return "更新失败";
			}
		} else {
			return "客户类型为空";
		}
	}
	/**
	 * 客户阶段更新
	 * 
	 * @param customer
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCustomerStage")
	public String updateCustomerStage(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		if (null != customer.getStage() && !"".equals(customer.getStage())) {
			customer.setComId(userInfo.getComId());
			boolean succ = crmService.updateCustomerStage(customer, userInfo);
			if (succ) {
				return "更新成功";
			} else {
				return "更新失败";
			}
		} else {
			return "客户阶段为空";
		}
	}
	/**
	 * 客户类型更新
	 * 
	 * @param customer
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCustomerBudget")
	public String updateCustomerBudget(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		if (null != customer.getBudget() && !"".equals(customer.getBudget())) {
			customer.setComId(userInfo.getComId());
			boolean succ = crmService.updateCustomerBudget(customer, userInfo);
			if (succ) {
				return "更新成功";
			} else {
				return "更新失败";
			}
		} else {
			return "预算资金为空";
		}
	}
	/**
	 * 客户负责人更新
	 * 
	 * @param customer
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/customerOwnerUpdate")
	public String customerOwnerUpdate(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customer.setComId(userInfo.getComId());
		UserInfo ownerInfo = userInfoService.getUserInfo(userInfo.getComId(),
				customer.getOwner());
		customer.setOwnerName(ownerInfo.getUserName());
		boolean succ = crmService.updateCustomerOwner(customer, userInfo);
		if (succ) {
			this.setNotification(Notification.SUCCESS, "变更成功");
			return "变更成功";
		} else {
			this.setNotification(Notification.ERROR, "变更失败");
			return "变更失败";
		}
	}

	/**
	 * 客户参与人更新
	 * 
	 * @param customerId
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/customerSharerUpdate")
	public String customerSharerUpdate(Integer customerId, Integer[] userIds)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		boolean succ = crmService.updateCustomerSharer(userInfo.getComId(),
				customerId, userIds, userInfo);
		// StringBuffer sharerName =null;
		// if(null!=userIds && userIds.length>0){
		// //客户参与人更新
		// sharerName = new StringBuffer();
		// for(Integer userId:userIds){
		// UserInfo sharerInfo =
		// userInfoService.getUserInfo(userInfo.getComId(),userId);
		// sharerName.append(sharerInfo.getUserName()+",");
		// }
		// sharerName = new
		// StringBuffer(sharerName.subSequence(0,sharerName.lastIndexOf(",")));
		// }
		if (succ) {
			return "变更成功";
		} else {
			return "变更失败";
		}
	}

	/**
	 * 删除客户参与人
	 * 
	 * @param customerId
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delCustomerSharer")
	public String delCustomerSharer(Integer customerId, Integer userId)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		UserInfo sharerInfo = userInfoService.getUserInfo(userInfo.getComId(),
				userId);
		boolean succ = crmService.delCustomerSharer(userInfo.getComId(),
				customerId, userId, userInfo, sharerInfo.getUserName());
		if (succ) {
			return "删除成功";
		} else {
			return "删除失败";
		}
	}

	/**
	 * 客户联系人维护
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/customerLinkManUpdate")
	public Customer customerLinkManUpdate(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			customer.setSucc(false);
			customer.setPromptMsg(CommonConstant.OFF_LINE_INFO);
			return null;
		}
		customer.setComId(userInfo.getComId());
		boolean succ = crmService.updateCustomerLinkMan(customer, userInfo);
		if (succ) {
			customer.setSucc(succ);
			customer.setPromptMsg("更新成功");
			List<LinkMan> listCrmLinkMan = crmService.listCrmLinkMan(
					userInfo.getComId(), customer.getId());
			List<LinkMan> results = new ArrayList<>();
			if(!CommonUtil.isNull(listCrmLinkMan)) {
				for (LinkMan lm : listCrmLinkMan) {
					//查询联系方式
					List<OlmContactWay> contactWays = outLinkManService.listContactWayForShow(lm.getOutLinkManId(),userInfo);
					if(!CommonUtil.isNull(contactWays)) {
						for (OlmContactWay contactWay : contactWays) {
							LinkMan result = new LinkMan();
							BeanUtils.copyProperties(lm,result);
							result.setContactWay(contactWay.getContactWay());
							result.setContactWayValue(contactWay.getContactWayValue());
							results.add(result);
						}
					}else {
						LinkMan result = new LinkMan();
						BeanUtils.copyProperties(lm,result);
						results.add(result);
					}
				}
			}
			String linkManStr = this.crmLinkManDisString(results);
			customer.setLinkManStr(linkManStr);
			customer.setLinkManSum(listCrmLinkMan.size());
			return customer;
		} else {
			customer.setSucc(succ);
			customer.setPromptMsg("更新失败");
			return customer;
		}
	}

	/**
	 * 构建前端显示html
	 * 
	 * @param listCrmLinkMan
	 * @return
	 */
	private String crmLinkManDisString(List<LinkMan> listCrmLinkMan) {
		// 构建前端显示html
		StringBuffer linkManStr = new StringBuffer();
		if (null != listCrmLinkMan) {
			LinkMan vo = null;
			// 列表显示数据构建
			for (int i = 0; i < listCrmLinkMan.size(); i++) {
				vo = listCrmLinkMan.get(i);
				linkManStr.append("<tr style=\"margin-top:5px;\" olmId=\""+vo.getOutLinkManId()+"\">");
				linkManStr
						.append("<td height=\"30\"><a href=\"javascript:void(0);\" onclick=\"viewOlm("+vo.getOutLinkManId()+");\">"+vo.getLinkManName()+"</a></td>");
				linkManStr.append("<td><input style=\"border: 0;\" disabled type=\"text\" id=\"listLinkMan["
						+ i + "].post\" name=\"listLinkMan[" + i
						+ "].post\" value=\""
						+ (null == vo.getPost() ? "" : vo.getPost())
						+ "\"/></td>");
				linkManStr
						.append("<td>"+(vo.getContactWayValue()==null?"":(vo.getContactWayValue()+"："+vo.getContactWay())) +"</td>");
				
				linkManStr
						.append("<td><a href='javascript:void(0)' style=\"color: red;\" class='fa fa-times-circle-o' onclick=\"delLinkManTr(this,'"
								+ vo.getId() + "',"+ vo.getOutLinkManId() +")\" title='删除本行'></a></td>");
				linkManStr.append("</tr>");
			}
		}
		return linkManStr.toString();
	}

	/**
	 * 逐一删除客户联系人
	 * 
	 * @param linkMan
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delLinkMan")
	public LinkMan delLinkMan(LinkMan linkMan) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		linkMan.setComId(userInfo.getComId());
		// 获取联系人删除对象
		// LinkMan delObj =
		// crmService.queryLinkMan(userInfo.getComId(),linkMan.getCustomerId(),linkMan.getId());
		boolean succ = crmService.delLinkMan(linkMan, userInfo);
		if (succ) {
			linkMan.setDelSucc(succ);
			linkMan.setPromptMsg("删除成功");
		} else {
			linkMan.setDelSucc(succ);
			linkMan.setPromptMsg("删除失败");
		}
		return linkMan;
	}

	/**
	 * 客户联系电话更新
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCustomerLinePhone")
	public String updateCustomerLinePhone(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customer.setComId(userInfo.getComId());
		boolean succ = crmService.updateCustomerLinePhone(customer, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 客户传真更新
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCustomerFax")
	public String updateCustomerFax(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customer.setComId(userInfo.getComId());
		boolean succ = crmService.updateCustomerFax(customer, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败！";
		}
	}

	/**
	 * 客户联系地址更新
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCustomerAddress")
	public String updateCustomerAddress(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customer.setComId(userInfo.getComId());
		boolean succ = crmService.updateCustomerAddress(customer, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 客户邮编更新
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCustomerPostCode")
	public String updateCustomerPostCode(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customer.setComId(userInfo.getComId());
		boolean succ = crmService.updateCustomerPostCode(customer, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 客户备注更新
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCustomerRemark")
	public String updateCustomerRemark(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customer.setComId(userInfo.getComId());
		boolean succ = crmService.updateCustomerRemark(customer, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	
	/**
	 * 客户公开私有更新
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCustomerPubState")
	public String updateCustomerPubState(Customer customer) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		customer.setComId(userInfo.getComId());
		boolean succ = crmService.updateCustomerPubState(customer, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	
	

	/**
	 * 获取客户日志
	 * 
	 * @param customerLog
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customerLogPage")
	public ModelAndView customerLogPage(CustomerLog customerLog)
			throws Exception {
		ModelAndView view = new ModelAndView("/crm/customerLog");
		UserInfo userInfo = this.getSessionUser();
		List<CustomerLog> listCustomerLog = crmService.listCustomerLog(
				userInfo.getComId(), customerLog.getCustomerId());
		// 日志查看，删除客户提醒
		todayWorksService.updateTodoWorkRead(customerLog.getCustomerId(),
				userInfo.getComId(), userInfo.getId(),
				ConstantInterface.TYPE_CRM, 0);

		view.addObject("listCustomerLog", listCustomerLog);
		return view;
	}

	/**
	 * 查看项目移交记录
	 * 
	 * @param crmId
	 * @return
	 */
	@RequestMapping("/crmFlowRecord")
	public ModelAndView crmFlowRecord(Integer crmId) {
		ModelAndView view = new ModelAndView("/crm/crmFlowRecord");
		UserInfo userInfo = this.getSessionUser();
		// 移交记录，删除客户提醒
		todayWorksService.updateTodoWorkRead(crmId, userInfo.getComId(),
				userInfo.getId(), ConstantInterface.TYPE_CRM, 0);

		// 移交记录
		List<FlowRecord> listFlowRecord = new ArrayList<FlowRecord>();
		listFlowRecord = crmService.listFlowRecord(crmId, userInfo.getComId());
		// 客户详情
		Customer crm = null;
		if (null == listFlowRecord || listFlowRecord.size() == 0) {
			crm = crmService.getCrmById(userInfo, crmId);

			FlowRecord flowRecord = new FlowRecord();
			flowRecord.setUserId(crm.getCreator());
			flowRecord.setAcceptDate(crm.getRecordCreateTime());
			flowRecord.setUserName(crm.getOwnerName());
			flowRecord.setUuid(crm.getUuid());
			flowRecord.setGender(crm.getGender());
			listFlowRecord.add(flowRecord);
		}
		view.addObject("listFlowRecord", listFlowRecord);
		return view;
	}

	/**
	 * 获取客户附件文档
	 * 
	 * @param customerUpfile
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customerUpfilePage")
	public ModelAndView customerUpfilePage(CustomerUpfile customerUpfile)
			throws Exception {
		ModelAndView view = new ModelAndView("/crm/customerUpfile");
		UserInfo userInfo = this.getSessionUser();
		List<CustomerUpfile> listCustomerUpfile = crmService
				.listPagedCustomerUpfile(userInfo.getComId(),
						customerUpfile);

		// 客户附件，删除客户提醒
		todayWorksService.updateTodoWorkRead(customerUpfile.getCustomerId(),
				userInfo.getComId(), userInfo.getId(),
				ConstantInterface.TYPE_CRM, 0);

		view.addObject("listCustomerUpfile", listCustomerUpfile);
		view.addObject("userInfo", userInfo);
		view.addObject("customerUpfile", customerUpfile);
		return view;
	}

	/**
	 * 客户维护记录
	 * 
	 * @param feedBackInfo
	 * @return
	 */
	@RequestMapping(value = "/customerFeedBackInfoPage")
	public ModelAndView customerFeedBackInfoPage(FeedBackInfo feedBackInfo) {
		ModelAndView view = new ModelAndView("/crm/customerFeedBackInfo");
		view.addObject("feedBackInfo", feedBackInfo);
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);

		// 查看反馈，删除客户提醒
		todayWorksService.updateTodoWorkRead(feedBackInfo.getCustomerId(),
				userInfo.getComId(), userInfo.getId(),
				ConstantInterface.TYPE_CRM, 0);

		// 获取反馈类型集合
		List<FeedBackType> listFeedBackType = crmService.listFeedBackType(userInfo.getComId());
		view.addObject("listFeedBackType", listFeedBackType);
		// 获取客户维护记录
		List<FeedBackInfo> listFeedBackInfo = crmService.listFeedBackInfo(
				userInfo.getComId(), feedBackInfo.getCustomerId(), "pc");
		view.addObject("listFeedBackInfo", listFeedBackInfo);

		// 获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService
				.listModuleOperateConfig(userInfo.getComId(),
						ConstantInterface.TYPE_CRM);
		if (null != listModuleOperateConfig) {
			for (ModuleOperateConfig vo : listModuleOperateConfig) {
				view.addObject(vo.getOperateType(), ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		return view;
	}

	/**
	 * 客户维护信息回复
	 * 
	 * @param feedBackInfo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/replyFeedBackInfo")
	public FeedBackInfo replyFeedBackInfo(FeedBackInfo feedBackInfo)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		feedBackInfo.setComId(userInfo.getComId());
		feedBackInfo.setUserId(userInfo.getId());
		// 存储维护信息
		Integer feedBackInfoId = crmService.addFeedBackInfo(feedBackInfo, null,userInfo);
		// 获取维护记录信息
		feedBackInfo = crmService.queryFeedBackInfo(userInfo.getComId(),feedBackInfoId);
		// 获取反馈类型集合
		List<FeedBackType> listFeedBackType = crmService.listFeedBackType(userInfo.getComId());
		// 是子节点
		feedBackInfo.setIsLeaf(1);
		// 构建客户维护信息记录DIV、构建附件上传所需参数
		feedBackInfo.setFeedBackInfoDivString(this.dis_addCrmRecond(feedBackInfo, listFeedBackType,
				"listUpfiles_" + feedBackInfo.getId() + ".upfileId","filename", "otherCustomerAttrIframe"));

		return feedBackInfo;
	}

	/**
	 * 构建添加维护记录显示DIV
	 * 
	 * @param feedBackInfo
	 *            客户添加记录实体
	 * @param listFeedBackType
	 *            反馈类型集合
	 * @param uploadFileName
	 *            上传控件主键ID
	 * @param uploadFileShowName
	 *            上传控件属性name
	 * @param pifreamId
	 *            父级iframe主键
	 * @return
	 */
	private String dis_addCrmRecond(FeedBackInfo feedBackInfo,
			List<FeedBackType> listFeedBackType, String uploadFileName,
			String uploadFileShowName, String pifreamId) {
		if (null == feedBackInfo){
			return null;
		}
		String sid = RequestContextHolderUtil.getRequest().getParameter("sid");
		UserInfo userInfo = this.getSessionUser();
		StringBuffer divString = new StringBuffer();
		divString.append("\n <div class='comment' id='talk_"
				+ feedBackInfo.getId() + "'>");
		if (feedBackInfo.getParentId() > 0) {
			divString.append("\n <div class='comment'>");
		}
		divString.append("	<img src=\"/downLoad/userImg/"
				+ feedBackInfo.getComId() + "/" + feedBackInfo.getUserId()
				+ "?sid=" + sid + "\" title=\""
				+ feedBackInfo.getSpeakerName()
				+ "\" class='comment-avatar'></img>");
		divString.append("\n	<div class='comment-body'>");
		divString.append("\n 		<div class='comment-text'>");
		divString.append("\n			<div class='comment-header'>");
		divString.append("\n				<a>" + feedBackInfo.getSpeakerName() + "</a>");
		if (feedBackInfo.getParentId() != -1) {
			divString.append("				<r style='color:#000;margin:0px 5px'>回复</r>");
			divString.append("\n			<a>" + feedBackInfo.getpSpeakerName()
					+ "</a>");
		}
		divString.append("					<span>"
				+ feedBackInfo.getRecordCreateTime().substring(0, 16)
				+ "</span>");
		divString.append("					<span>【" + feedBackInfo.getTypeName()
				+ "】</span>");
		divString.append("\n			</div>");
		divString.append("\n			<p class='no-margin-bottom'>"
				+ StringUtil.toHtml(feedBackInfo.getContent()) + "</p>");

		divString.append("\n			<p class='no-margin-bottom'>");
		// 附件
		List<FeedInfoFile> list = feedBackInfo.getListfeedInfoFiles();
		if (null != list && list.size() > 0) {
			divString.append("\n			<div class=\"file_div\">");
			for (int i = 0; i < list.size(); i++) {
				FeedInfoFile upfiles = list.get(i);
				if ("1".equals(upfiles.getIsPic())) {
					divString.append("\n		<p class=\"p_text\">");
					divString.append("\n附件（" + (i + 1) + "）：");
					divString
							.append("\n			<img onload=\"AutoResizeImage(350,0,this,'otherCustomerAttrIframe')\"");
					divString.append("\n				src=\"/downLoad/down/"
							+ upfiles.getUuid() + "/" + upfiles.getFilename()
							+ "?sid=" + this.getSid() + "\" />");
					divString
							.append("\n		&nbsp;<a class=\"fa fa-download\" title=\"下载\" href=\"/downLoad/down/"
									+ upfiles.getUuid()
									+ "/"
									+ upfiles.getFilename()
									+ "?sid="
									+ this.getSid() + "\"></a>");
					divString
							.append("\n		&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"showPic('/downLoad/down/"
									+ upfiles.getUuid()
									+ "/"
									+ upfiles.getFilename()
									+ "','"
									+ this.getSid()
									+ "','"
									+ upfiles.getUpfileId()
									+ "','"
									+ ConstantInterface.TYPE_CRM
									+ "','"
									+ feedBackInfo.getCustomerId()
									+ "')\"></a>");
					divString.append("\n		</p>");
				} else {
					divString.append("\n		<p class=\"p_text\">");
					divString.append("\n件（" + (i + 1) + "）：");
					divString.append(upfiles.getFilename());
					if (upfiles.getFileExt().equals("doc")
							|| upfiles.getFileExt().equals("docx")
							|| upfiles.getFileExt().equals("xls")
							|| upfiles.getFileExt().equals("xlsx")
							|| upfiles.getFileExt().equals("ppt")
							|| upfiles.getFileExt().equals("pptx")) {
						divString
								.append("\n		&nbsp;<a class=\"fa fa-download\" href=\"javascript:void(0);\" title=\"下载\" onclick=\"downLoad('"
										+ upfiles.getUuid()
										+ "','"
										+ upfiles.getFilename()
										+ "','"
										+ this.getSid() + "')\"></a>");
						divString
								.append("\n		&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"viewOfficePage('"
										+ upfiles.getUpfileId()
										+ "','"
										+ upfiles.getUuid()
										+ "','"
										+ upfiles.getFilename()
										+ "','"
										+ upfiles.getFileExt()
										+ "','"
										+ this.getSid()
										+ "','"
										+ ConstantInterface.TYPE_CRM
										+ "','"
										+ feedBackInfo.getCustomerId()
										+ "')\"></a>");
					} else if (upfiles.getFileExt().equals("pdf")
							|| upfiles.getFileExt().equals("txt")) {
						divString
								.append("\n		&nbsp;<a class=\"fa fa-download\" title=\"下载\" href=\"/downLoad/down/"
										+ upfiles.getUuid()
										+ "/"
										+ upfiles.getFilename()
										+ "?sid="
										+ this.getSid() + "\"></a>");
						divString
								.append("\n		&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"viewOfficePage('"
										+ upfiles.getUpfileId()
										+ "','"
										+ upfiles.getUuid()
										+ "','"
										+ upfiles.getFilename()
										+ "','"
										+ upfiles.getFileExt()
										+ "','"
										+ this.getSid()
										+ "','"
										+ ConstantInterface.TYPE_CRM
										+ "','"
										+ feedBackInfo.getCustomerId()
										+ "')\"></a>");
					} else {
						divString
								.append("\n		&nbsp;<a class=\"fa fa-download\" href=\"javascript:void(0);\" title=\"下载\" onclick=\"downLoad('"
										+ upfiles.getUuid()
										+ "','"
										+ upfiles.getFilename()
										+ "','"
										+ this.getSid() + "')\"></a>");
					}
				}
				divString.append("\n			</p>");
			}
			divString.append("\n			</div>");
		}
		divString.append("\n			</p>");

		divString.append("\n 		</div>");
		divString.append("\n 		<div class='comment-footer'>");
		// 模块操作权限验证
		ModuleOperateConfig modOptConf = modOptConfService
				.getModuleOperateConfig(userInfo.getComId(),
						ConstantInterface.TYPE_CRM, "delete");
		if (null == modOptConf) {
			// 发言人可以删除自己的发言
			divString.append("			<a href=\"javascript:void(0);\" id=\"delOpt_"
					+ feedBackInfo.getId()
					+ "\" title=\"删除\" onclick=\"delFeedBackInfo('"
					+ feedBackInfo.getId()
					+ "','1')\"><i class='fa fa-trash-o'></i></a>");
		}
		divString
				.append("\n			<a id=\"img_"
						+ feedBackInfo.getId()
						+ "\" name=\"replyImg\" href=\"javascript:void(0);\" class=\"fa fa-comment-o\" title=\"回复\" onclick=\"showArea('"
						+ feedBackInfo.getId() + "')\"></a>");
		divString.append("\n		</div>");
		// 回复层
		divString.append("\n 		<div class='panel-body' id='reply_"
				+ feedBackInfo.getId()
				+ "' name='replyTalk' style='display:none;'>");
		divString
				.append("\n 			<textarea class='form-control' id='operaterReplyTextarea_"
						+ feedBackInfo.getId()
						+ "' name='operaterReplyTextarea_"
						+ feedBackInfo.getId() + "'");
		divString
				.append("\n				rows='' cols='' placeholder='请输入内容……' style='height:55px;'></textarea>");
		divString
				.append("\n			<div class='panel-body' style='padding-right:0;'>");
		divString
				.append("\n				<div class='buttons-preview pull-left' style='position: relative;'>");
		// 表情
		divString
				.append("\n 					<a href='javascript:void(0);' class='btn-icon fa fa-meh-o fa-lg' id='biaoQingSwitch_"
						+ feedBackInfo.getId()
						+ "' onclick=\"addBiaoQingObj('biaoQingSwitch_"
						+ feedBackInfo.getId()
						+ "','biaoQingDiv_"
						+ feedBackInfo.getId()
						+ "','operaterReplyTextarea_"
						+ feedBackInfo.getId() + "');\"></a>");
		// 表情DIV层
		divString
				.append("\n					<div id=\"biaoQingDiv_"
						+ feedBackInfo.getId()
						+ "\" class=\"blk\" style=\"display:none;position:absolute;width:200px;top:10px;z-index:99;left: 15px\">");
		divString.append("\n						<div class=\"main\">");
		divString.append("\n							<ul style=\"padding: 0px\">");
		// 添加表情包字符串
		divString.append(CommonUtil.biaoQingStr());
		divString.append("\n							</ul>");
		divString.append("\n						</div>");
		divString.append("\n					</div>");
		divString
				.append("\n					<a href='javascript:void(0);' class='btn-icon fa fa-comments-o fa-lg' onclick=\"addIdea('operaterReplyTextarea_"
						+ feedBackInfo.getId()
						+ "','"
						+ sid
						+ "');\" title='常用意见'></a>");
		divString.append("\n				</div>");
		//@机制
        divString.append("\n            <div class=\"pull-left\">" );
        divString.append("\n                <a class=\"btn-icon fa-lg\" href=\"javascript:void(0)\" title=\"告知人员\" data-todoUser=\"yes\" data-relateDiv=\"todoUserDiv_" + feedBackInfo.getId() + "\">" );
        divString.append("\n                    @" );
        divString.append("\n                </a>" );
        divString.append("\n             </div>");
		divString
				.append("\n				<div class='pull-left' style='margin-right:10px; margin-left:10px;'>");
		divString
				.append("\n					<span class='pull-left' style='margin:0;'>反馈类型：</span>");
		divString.append("\n					<div class='ws-form-group pull-left'>");
		divString
				.append("\n 						<select class=\"populate\" id=\"feedBackTypeId_"
						+ feedBackInfo.getId() + "\">");
		divString.append("\n 							<option value=\"0\">反馈类型</option>");
		if (null != listFeedBackType) {
			for (FeedBackType vo : listFeedBackType) {
				divString.append("\n					<option value=\"" + vo.getId() + "\">"
						+ vo.getTypeName() + "</option>");
			}
		}
		divString.append("\n						</select>");
		divString.append("\n 					</div>");
		divString.append("\n					<div class='ws-clear'></div>");
		divString.append("\n				</div>");

//		divString.append("\n				<div class='checkbox pull-left no-margin'>");
//		divString.append("\n					<label>通知方式：</label>");
//		divString.append("\n					<label class='no-padding'>");
//		divString.append("\n						<input type='checkbox'>");
//		divString.append("\n						<span class='text'>邮件</span>");
//		divString.append("\n					</label>");
//		divString.append("\n					<label class='no-padding'>");
//		divString.append("\n						<input type='checkbox'>");
//		divString.append("\n						<span class='text'>短信</span>");
//		divString.append("\n					</label>");
//		divString.append("\n				</div>");

		divString.append("\n				<div class='pull-right'>");
		//@机制
		divString
				.append("\n					<a href='javascript:void(0)' class='btn btn-info' data-relateTodoDiv=\"todoUserDiv_" + feedBackInfo.getId() + "\" " +
                        "onclick=\"replyFeedBackInfo(" + feedBackInfo.getCustomerId() + "," + feedBackInfo.getId() + ",this)\">发表</a>");
		divString.append("\n				</div>");
		divString.append("\n				<div style='clear: both;'></div>");
		divString.append("\n                <div id=\"todoUserDiv_" + feedBackInfo.getId() + "\" class=\"padding-top-10\">");
        divString.append("\n	            </div>");
		divString.append("\n				<div class=\"ws-notice\">");
		// 构建附件上传控件
		divString.append(CommonUtil.uploadFileTagStr(uploadFileName,
				uploadFileShowName, pifreamId, userInfo.getComId(), sid));
		divString.append("\n				</div>");

		divString.append("\n			</div>");
		divString.append("\n		</div>");
		divString.append("\n	</div>");

		if (feedBackInfo.getParentId() > 0) {
			divString.append("\n</div>");
		}

		divString.append("\n</div>");
		return divString.toString();
	}

	/**
	 * 删除客户维护记录
	 * 
	 * @param feedBackInfo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delFeedBackInfo")
	public FeedBackInfo delFeedBackInfo(FeedBackInfo feedBackInfo)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();

		// 是否有删除权限
		ModuleOperateConfig modOptConf = modOptConfService
				.getModuleOperateConfig(userInfo.getComId(),
						ConstantInterface.TYPE_CRM, "delete");
		// 模块操作权限验证
		if (null == modOptConf) {
			feedBackInfo.setComId(userInfo.getComId());
			boolean succ = crmService.delFeedBackInfo(feedBackInfo, userInfo);
			feedBackInfo.setSucc(succ);
			if (succ) {
				feedBackInfo.setPromptMsg("删除成功");
			} else {
				feedBackInfo.setPromptMsg("删除失败");
			}
		} else {
			feedBackInfo.setSucc(false);
			feedBackInfo.setPromptMsg("删除失败，此模块不具备删除权限！");
		}
		return feedBackInfo;
	}

	/**
	 * 跳转客户移交页面
	 * 
	 * @param customerHandOver
	 * @return
	 */
	@RequestMapping(value = "/customerHandOverPage")
	public ModelAndView customerHandOverPage(CustomerHandOver customerHandOver,
			Integer[] ids) {
		ModelAndView view = new ModelAndView("/crm/customerHandOver");
		view.addObject("customerHandOver", customerHandOver);
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		if (null != ids) {
			List<Customer> listCustomer = new ArrayList<Customer>();
			List<Integer> listId = new ArrayList<Integer>();
			StringBuffer customerName = new StringBuffer();
			Customer customer = null;
			for (Integer id : ids) {
				listId.add(id);
				customer = crmService.getCrmById(id);
				customerName.append(customer.getCustomerName() + "、");
				listCustomer.add(customer);
			}
			view.addObject("customerName",
					customerName.substring(0, customerName.lastIndexOf("、")));
			view.addObject("listId", listId);
			view.addObject("listCustomer", listCustomer);
		}
		// 获取反馈类型集合
		List<FeedBackType> listFeedBackType = crmService
				.listFeedBackType(userInfo.getComId());
		view.addObject("listFeedBackType", listFeedBackType);
		return view;
	}

	/**
	 * 跳转单客户移交页面
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/customerHandOverByOnePage")
	public ModelAndView customerHandOverByOnePage(Integer customerId,
			String redirectPage) {
		ModelAndView view = new ModelAndView("/crm/customerHandOverByOne");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		// 获取被移交客户信息
		Customer customer = crmService.getCrmById(userInfo, customerId);
		view.addObject("customer", customer);
		// 获取反馈类型集合
		List<FeedBackType> listFeedBackType = crmService
				.listFeedBackType(userInfo.getComId());
		view.addObject("listFeedBackType", listFeedBackType);
		return view;
	}

	/**
	 * 客户移交
	 * 
	 * @param customerHandOver
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/customerHandOver", method = RequestMethod.POST)
	public Map<String, Object> customerHandOver(
			CustomerHandOver customerHandOver, String redirectPage)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆");
			return map;
		}

		customerHandOver.setFromUser(userInfo.getId());
		customerHandOver.setComId(userInfo.getComId());

		Customer customer = crmService.getCrmById(customerHandOver
				.getCustomerId());
		if (customer.getOwner().equals(customerHandOver.getToUser())) {
			map.put("status", "f1");
			map.put("info", "移交失败，不能移交给自己!");
		} else {
			boolean succ = crmService.addCustomerHandOver(new CustomerHandOver[] { customerHandOver },userInfo);
			if (succ) {
				map.put("status", "y");
			} else {
				map.put("status", "f2");
				map.put("info", "客户移交失败!");
			}
		}
		return map;
	}

	/**
	 * 客户批量移交
	 * 
	 * @param customerHandOver
	 * @param customerIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/batchCustomerHandOver", method = RequestMethod.POST)
	public Map<String, Object> batchCustomerHandOver(
			CustomerHandOver customerHandOver, Integer[] customerIds)
			throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		customerHandOver.setFromUser(userInfo.getId());
		customerHandOver.setComId(userInfo.getComId());
		// 验证客户移交对象是否是自己标识符
		boolean isMine = false;
		// 构建预交对象数组
		CustomerHandOver[] customerHandOvers = new CustomerHandOver[customerIds.length];
		// 获取移交客户名称字符串
		StringBuffer customerName = new StringBuffer();
		// 验证对象
		Customer customer = null;
		if (null != customerIds && customerIds.length > 0) {
			Customer handObj = null;
			for (int i = 0; i < customerIds.length; i++) {
				handObj = crmService.getCrmById(customerIds[i]);
				customerName.append(handObj.getCustomerName() + "、");
				// 对象存入
				customerHandOver.setCustomerId(customerIds[i]);
				CustomerHandOver newObj = new CustomerHandOver();
				newObj.setComId(customerHandOver.getComId());
				newObj.setCustomerId(customerIds[i]);
				newObj.setFromUser(customerHandOver.getFromUser());
				newObj.setToUser(customerHandOver.getToUser());
				newObj.setReplayContent(customerHandOver.getReplayContent());
				newObj.setFeedBackTypeId(customerHandOver.getFeedBackTypeId());
				customerHandOvers[i] = newObj;
				// 验证客户移交对象是否是自己
				customer = crmService.getCrmById(customerHandOver
						.getCustomerId());
				if (customer.getOwner().equals(customerHandOver.getToUser())) {
					isMine = true;
					break;
				}
			}
		}
		if (isMine) {
			map.put("status", "f");
			map.put("info", "移交失败，不能移交给自己!");
		} else {
			boolean succ = crmService.addCustomerHandOver(customerHandOvers,userInfo);
			if (succ) {
				this.setNotification(Notification.SUCCESS, "移交成功!");
				map.put("status", "y");
			} else {
				map.put("status", "f");
				map.put("info", "移交失败!");
			}
		}
		return map;
	}

	/**
	 * 获取个人私有组集合 * @param itemId
	 * 
	 * @return
	 */
	@RequestMapping(value = "/querySelfGroup")
	public ModelAndView querySelfGroup(Integer customerId) {
		ModelAndView view = new ModelAndView("/crm/listSelfGroup");
		UserInfo userInfo = this.getSessionUser();
		// 个人组群集合
		List<SelfGroup> listSelfGroup = crmService.listSelfGroupOfCrm(
				customerId, userInfo.getId(), userInfo.getComId());
		view.addObject("listSelfGroup", listSelfGroup);
		return view;
	}

	/**
	 * 更新客户成员组
	 * 
	 * @param customerId
	 * @param grpId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editCustomerGroup")
	public Customer editItemGroup(Integer customerId, Integer[] grpId)
			throws Exception {
		UserInfo userInfo = this.getSessionUser();
		Customer customer = new Customer();
		boolean succ = crmService.updateCustomerGroup(customerId, grpId,
				userInfo);
		if (succ) {
			customer.setSucc(true);
			customer.setPromptMsg("更新成功！");
		} else {
			customer.setSucc(false);
			customer.setPromptMsg("更新失败！");
		}
		return customer;
	}

	/**
	 * 获取你权限范围下客户务列表
	 * 
	 * @param customer
	 * @return
	 */
	@RequestMapping(value = "/listCrmForRelevance")
	public ModelAndView listCrmForRelevance(Customer customer) {
		ModelAndView view = new ModelAndView("/crm/listCrmForRelevance");
		UserInfo userInfo = this.getSessionUser();
		customer.setComId(userInfo.getComId());
		// 客户区域ID初始化
		if (null != customer.getAreaIdAndType()
				&& !"".equals(customer.getAreaIdAndType().trim())) {
			customer.setAreaId(Integer.parseInt(customer.getAreaIdAndType()
					.split("@")[0]));
			customer.setAreaName(crmService.getAreaById(
					customer.getAreaId()).getAreaName());
		} else {
			customer.setAreaId(0);
		}
		// 初始化客户负责人姓名
		if (null != customer.getOwner() && customer.getOwner() != 0) {
			customer.setOwnerName(userInfoService.getUserInfo(
					userInfo.getComId(), customer.getOwner()).getUserName());
		}
		// 取得客户的数据
		List<Customer> listCustomer = crmService.listCustomerForPage(customer,userInfo);
		view.addObject("listCustomer", listCustomer);
		// 取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(
				userInfo.getComId(), userInfo.getId(), 5);
		view.addObject("listOwners", listOwners);
		view.addObject("customer", customer);
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 跳转客户选择界面
	 * @return
	 */
	@RequestMapping(value = "/listMoreCrmForRelevance")
	public ModelAndView listMoreCrmForRelevance() {
		ModelAndView view = new ModelAndView("/crm/listMoreCrmForRelevance");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 异步取得项目分页数
	 * @param customer
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListCrmForSelect")
	public Map<String, Object> ajaxListCrmForSelect(Customer customer, Integer pageNum, Integer pageSize) {
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
		
		customer.setComId(userInfo.getComId());
		// 客户区域ID初始化
		if (null != customer.getAreaIdAndType()
				&& !"".equals(customer.getAreaIdAndType().trim())) {
			customer.setAreaId(Integer.parseInt(customer.getAreaIdAndType()
					.split("@")[0]));
		} else {
			customer.setAreaId(0);
		}
				
		PageBean<Customer> pageBean = crmService.listPagedCrm(customer, userInfo);
		map.put("pageBean", pageBean);
		
		map.put("status", "y");
		return map;
	}

	/**
	 * 合并客户信息前验证或是提示跳转页面
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/crmCompressPage")
	public ModelAndView crmCompressPage(String redirectPage, Integer[] ids) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/crm/crmCompress");
		Integer index = 0;
		if (null != ids) {
			for (Integer crmId : ids) {
				Customer crm = crmService.getCrmById(userInfo, crmId);
				view.addObject("crm" + index, crm);
				index++;
			}
		}
		view.addObject("redirectPage", redirectPage);
		view.addObject("userInfo", userInfo);
		view.addObject("index", index);
		view.addObject("ids", ids);
		return view;
	}

	/**
	 * 合并客户信息前验证或是提示
	 * 
	 * @param redirectPage
	 *            跳转页面
	 * @param crm
	 *            合并后客户信息
	 * @param ids
	 *            参与合并的客户主键
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/crmCompress")
	public ModelAndView crmCompress(String redirectPage, Customer crm,
			Integer[] ids) throws Exception {
		UserInfo userInfo = this.getSessionUser();

		crmService.updateCrmForCompress(crm, ids, userInfo);
		this.setNotification(Notification.SUCCESS, "合并成功");

		ModelAndView view = new ModelAndView("/refreshParent");

		return view;
	}

	/**
	 * 客户关联的项目
	 * 
	 * @param item
	 * @return
	 */
	@RequestMapping(value = "/crmItemListPage")
	public ModelAndView crmItemListPage(Item item, String redirectPage) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/crm/crmItemListPage");
		// 列出客户关联的项目
		List<Item> itemList = itemService.listPagedCrmItem(item, userInfo);
		// 获取客户类型数据源
		List<CustomerType> listCustomerType = crmService.listCustomerType(this
				.getSessionUser().getComId());
		view.addObject("listCustomerType", listCustomerType);
		view.addObject("itemList", itemList);
		view.addObject("userInfo", userInfo);
		return view;
	}
	
	/**
	 * 删除客户附件
	 * @param crmId
	 * @param crmUpFileId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delCrmUpfile")
	public Map<String, Object> delCrmUpfile(Integer crmId,Integer crmUpFileId,String type) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		crmService.delCrmUpfile(crmUpFileId,type,userInfo,crmId);
		map.put("status", "y");
		return map;
	}
	
}
