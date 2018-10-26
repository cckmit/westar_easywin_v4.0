package com.westar.core.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Customer;
import com.westar.base.model.Item;
import com.westar.base.model.ItemHandOver;
import com.westar.base.model.ItemLog;
import com.westar.base.model.ItemProgress;
import com.westar.base.model.ItemProgressTemplate;
import com.westar.base.model.ItemTalk;
import com.westar.base.model.ItemTalkFile;
import com.westar.base.model.ItemUpfile;
import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.StagedInfo;
import com.westar.base.model.StagedItem;
import com.westar.base.model.Task;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.ItemStagedInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.RequestContextHolderUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.service.ClockService;
import com.westar.core.service.CrmService;
import com.westar.core.service.DataDicService;
import com.westar.core.service.DemandService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.ItemProgressService;
import com.westar.core.service.ItemService;
import com.westar.core.service.ModOptConfService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TaskService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UploadService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

@Controller
@RequestMapping("/item")
public class ItemController extends BaseController {

	@Autowired
	ItemService itemService;

	@Autowired
	private SystemLogService systemLogService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	TaskService taskService;

	@Autowired
	CrmService crmService;

	@Autowired
	DataDicService dataDicService;

	@Autowired
	TodayWorksService todayWorksService;

	@Autowired
	UploadService uploadService;

	@Autowired
	ClockService clockService;

	@Autowired
	ModOptConfService modOptConfService;

	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	ItemProgressService itemProgressService;
	
	@Autowired
	DemandService demandService;

	/**
	 * 获取项目列表
	 * 
	 * @param item
	 * @return
	 */
	@RequestMapping(value = "/listItemPage")
	public ModelAndView listItemPage(HttpServletRequest request, Item item) {
		// 清除缓存中所有的操作
		FreshManager.removePreOpt(request);

		ModelAndView view = new ModelAndView("/item/listItem");
		UserInfo userInfo = this.getSessionUser();

		// 负责人类别
		String ownerType = item.getOwnerType();
		// 若是没有下属，则没有负责人类别一说
		if (userInfo.getCountSub() <= 0 && null != ownerType && "1".equals(ownerType)) {
			item.setOwnerType(null);
		}

		List<Item> list = itemService.listItem(item, userInfo);
		view.addObject("list", list);
		// 初始化查询负责人名称
		if (null != item.getOwner() && item.getOwner() != 0) {
			item.setOwnerName(userInfoService.getUserInfo(userInfo.getComId(), item.getOwner()).getUserName());
		}
		// 取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(), userInfo.getId(), 6);
		view.addObject("listOwners", listOwners);

		view.addObject("item", item);
		view.addObject("userInfo", userInfo);
		// 不显示项目更新数标志
		view.addObject("itemNotice", "no");

		// 获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(
				userInfo.getComId(), ConstantInterface.TYPE_ITEM);
		if (null != listModuleOperateConfig) {
			for (ModuleOperateConfig vo : listModuleOperateConfig) {
				view.addObject(vo.getOperateType(), ConstantInterface.MOD_OPT_STATE_YES);
			}
		}

		// 头文件的显示
		view.addObject("homeFlag", ConstantInterface.TYPE_ITEM);

		return view;
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
	@RequestMapping("/ajaxListItemPage")
	public Map<String, Object> ajaxListItemPage(Item item, Integer pageNum, Integer pageSize) {
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
		// 负责人类别
		String ownerType = item.getOwnerType();
		// 若是没有下属，则没有负责人类别一说
		if (userInfo.getCountSub() <= 0 && null != ownerType && "1".equals(ownerType)) {
			item.setOwnerType(null);
		}
		List<Item> list = itemService.listItem(item, userInfo);
		map.put("list", list);

		// 除开页面已有数据的总数
		Integer total = PaginationContext.getTotalCount();
		// 页面已展示的项目主键
		String itemIds = item.getItemIds();
		if (null != itemIds && !"".equals(itemIds)) {
			// 页面展示的总数为本次查询数+页面已展示数
			total = item.getItemIds().split(",").length + total;
		}
		map.put("itemNum", total);
		map.put("status", "y");
		return map;
	}
	

	/**
	 * 获取项目列表FOR关联
	 * 
	 * @param item
	 * @return
	 */
	@RequestMapping(value = "/listItemForRelevance")
	public ModelAndView listItemForRelevance(Item item) {
		ModelAndView view = new ModelAndView("/item/listItemForRelevance");
		UserInfo userInfo = this.getSessionUser();
		// 查看范围内的项目
		List<Item> listItem = itemService.listItem(item, userInfo);
		// 初始化查询负责人名称
		if (null != item.getOwner() && item.getOwner() != 0) {
			item.setOwnerName(userInfoService.getUserInfo(userInfo.getComId(), item.getOwner()).getUserName());
		}
		// 取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(), userInfo.getId(),6);
		view.addObject("listOwners", listOwners);
		view.addObject("item", item);
		view.addObject("listItem", listItem);
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 获取项目列表FOR关联
	 * 
	 * @param item
	 * @return
	 */
	@RequestMapping(value = "/listMoreItemForRelevance")
	public ModelAndView listMoreItemForRelevance() {
		ModelAndView view = new ModelAndView("/item/listMoreItemForRelevance");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 异步取得项目分页数
	 * @param item
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListItemForSelect")
	public Map<String, Object> ajaxListItemForSelect(Item item, Integer pageNum, Integer pageSize) {
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
		// 负责人类别
		String ownerType = item.getOwnerType();
		// 若是没有下属，则没有负责人类别一说
		if (userInfo.getCountSub() <= 0 && null != ownerType && "1".equals(ownerType)) {
			item.setOwnerType(null);
		}
		
		PageBean<Item> pageBean = itemService.listPagedItem(item, userInfo);
		map.put("pageBean", pageBean);
		
		map.put("status", "y");
		return map;
	}
	/**
	 * 跳转查询用于需求选择的项目的页面
	 * @return
	 */
	@RequestMapping(value = "/listItemForDemand")
	public ModelAndView listItemForDemand() {
		ModelAndView view = new ModelAndView("/item/listItemForDemand");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 分页查询用于需求选择的项目
	 * @param item
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListItemForDemand")
	public HttpResult<PageBean<Item>> ajaxListItemForDemand(Item item, Integer pageNum, Integer pageSize) {
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			return new HttpResult<PageBean<Item>>().error(CommonConstant.OFF_LINE_INFO);
		}
		
		pageNum = null == pageNum ? 0 : pageNum;
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		// 负责人类别
		String ownerType = item.getOwnerType();
		// 若是没有下属，则没有负责人类别一说
		if (userInfo.getCountSub() <= 0 && null != ownerType && "1".equals(ownerType)) {
			item.setOwnerType(null);
		}
		
		PageBean<Item> pageBean = itemService.listPagedItemForDemand(item, userInfo);
		return new HttpResult<PageBean<Item>>().ok(pageBean);
	}

	/**
	 * 跳转项目添加页面
	 * 
	 * @return
	 */
	@RequestMapping("/addItemPage")
	public ModelAndView addItemPage() {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/item/addItem");
		view.addObject("userInfo", userInfo);

		// 取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(), userInfo.getId(),6);
		view.addObject("listUsed", listOwners);
		//项目进度模板
		List<ItemProgressTemplate> listTemplates = itemProgressService.listProgressTemplate(userInfo);
		view.addObject("listTemplates", listTemplates);
		return view;
	}

	/**
	 * 头部快速添加项目
	 * 
	 * @return
	 */
	@RequestMapping("/addItemBySimple")
	public ModelAndView addItemBySimple() {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/item/addItem");
		view.addObject("userInfo", userInfo);

		// 取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(), userInfo.getId(),6);
		view.addObject("listUsed", listOwners);
		//项目进度模板
		List<ItemProgressTemplate> listTemplates = itemProgressService.listProgressTemplate(userInfo);
		view.addObject("listTemplates", listTemplates);
		return view;
	}

	/**
	 * 创建新项目
	 * 
	 * @param item
	 *            项目信息
	 * @param redirectPage
	 *            页面跳转链接
	 * @param way
	 *            页面跳转方式
	 * @param atten
	 *            关注标识
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addItem")
	public ModelAndView addItem(Item item, String redirectPage, String way) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		// 企业号
		item.setComId(userInfo.getComId());
		// 创建人
		item.setCreator(userInfo.getId());
		// 删除标识(正常)
		item.setDelState(0);
		// 项目状态标识
		item.setState(1);
		itemService.addItem(item, userInfo);
		this.setNotification(Notification.SUCCESS, "添加成功");

		ModelAndView view = new ModelAndView("/refreshParent");
		return view;
	}

	/**
	 * 跳转项目分解页面
	 * 
	 * @param item
	 * @return
	 */
	@RequestMapping("/resolveItemPage")
	public ModelAndView resolveItemPage(Item item) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/item/resolveItem");
		Item items = itemService.getItem(item.getParentId(), userInfo);
		items.setParentId(item.getParentId());
		items.setpItemName(item.getpItemName());
		view.addObject("userInfo", userInfo);
		view.addObject("item", items);
		view.addObject("listItemSharer", items.getListItemSharer());
		// 取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(), userInfo.getId(), 6);
		view.addObject("listUsed", listOwners);
		//项目进度模板
		List<ItemProgressTemplate> listTemplates = itemProgressService.listProgressTemplate(userInfo);
		view.addObject("listTemplates", listTemplates);
		return view;
	}

	/**
	 * 项目分解
	 * 
	 * @param item
	 *            子项目信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/resolveItem", method = RequestMethod.POST)
	public ModelAndView resolveItem(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		// 企业号
		item.setComId(userInfo.getComId());
		// 创建人
		item.setCreator(userInfo.getId());
		// 删除标识(正常)
		item.setDelState(0);
		// 项目状态标识
		item.setState(1);

		itemService.addItem(item, userInfo);
		this.setNotification(Notification.SUCCESS, "分解成功");
		ModelAndView view = new ModelAndView("/refreshParent");
		return view;
	}

	/**
	 * 构建子任务Json
	 * 
	 * @param listSonTask
	 * @return
	 */
	@SuppressWarnings("unused")
	private String sonItemJson(List<Item> listSonItem) {
		if (null == listSonItem || listSonItem.isEmpty()) {
			return null;
		}
		// 生成子项目JSon字符串
		StringBuffer sonItemJson = null;
		if (null != listSonItem && !listSonItem.isEmpty()) {
			sonItemJson = new StringBuffer("[");
			for (Item vo : listSonItem) {
				sonItemJson.append("{'id':" + vo.getId() + ",'itemName':'" + vo.getItemName() + "','sonItemNum':"
						+ vo.getSonItemNum() + ",'state':" + vo.getState() + ",'owner':" + vo.getOwner());
				sonItemJson.append(",'gender':'" + vo.getGender() + "','uuid':'" + vo.getUuid() + "','filename':'"
						+ vo.getFilename() + "','ownerName':'" + vo.getOwnerName() + "'},");
			}
			sonItemJson = new StringBuffer(sonItemJson.substring(0, sonItemJson.lastIndexOf(",")));
			sonItemJson.append("]");
		}
		return sonItemJson.toString();
	}

	/**
	 * 获取项目信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/viewItemPage")
	public ModelAndView viewItem(HttpServletRequest request, Integer id, String redirectPage, Item parameters,
			Integer clockId) {
		ModelAndView view = null;
		UserInfo userInfo = this.getSessionUser();
		// 验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);

		// 验证查看人权限
		if (itemService.authorCheck(userInfo.getComId(), id, userInfo.getId()) || isForceIn) {

			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ITEM,
					id);
			// 取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if (bool) {
				// 添加查看记录
				viewRecordService.addViewRecord(userInfo, viewRecord);
			}

			Item item = itemService.getItem(id, userInfo);
			if (userInfo.getId().equals(item.getOwner())) {
				// 判断项目查看人是否是项目负责人
				view = new ModelAndView("/item/editItem");
				//判断项目是否完成
				if(ConstantInterface.COMMON_FINISH.equals(item.getState())){
					view = new ModelAndView("/item/viewItem");
					view.addObject("reStartSate", "1");
				}
				// 判断是否有编辑权限
				ModuleOperateConfig modOptConf = modOptConfService.getModuleOperateConfig(userInfo.getComId(),
						ConstantInterface.TYPE_ITEM, "update");
				if (null != modOptConf) {
					view.addObject("editItem", "no");
				}
			} else {
				view = new ModelAndView("/item/viewItem");
			}
			// 获取项目成员组
			List<SelfGroup> listShareGroup = itemService.listShareGroupOfItem(id, userInfo.getComId());
			view.addObject("listShareGroup", listShareGroup);

			view.addObject("parameters", parameters);
			view.addObject("item", item);
			view.addObject("listItemSharer", item.getListItemSharer());
			view.addObject("listSonItem", item.getListSonItem());
			view.addObject("userInfo", userInfo);

			// 浏览的人员
			List<ViewRecord> listViewRecord = viewRecordService.listViewRecord(userInfo, ConstantInterface.TYPE_ITEM, id);
			view.addObject("listViewRecord", listViewRecord);
		} else {
			view = new ModelAndView("/refreshSelf");
			view.addObject("redirectPage", redirectPage);
			this.setNotification(Notification.ERROR, "抱歉，你没有查看权限");
		}
		// 查看项目，删出消息提醒
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ITEM,
				clockId);
		return view;
	}
	/**
	 * 删除项目
	 * 
	 * @param ids
	 * @param redirectPage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delItem")
	public ModelAndView delItem(Integer[] ids, String redirectPage) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		
		// 添加日志
		if (null != ids && ids.length > 0) {
			Integer count = demandService.queryDemandCountByItemIds(ids);
			if(count > 0){
				this.setNotification(Notification.ERROR, "选择的项目中有其他模块关联，不能删除！");
			}else{
				for (Integer id : ids) {
					Item item = itemService.getItemById(id);
					// 添加系统日志记录
					systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除项目:\"" + item.getItemName()
							+ "\"", ConstantInterface.TYPE_ITEM, userInfo.getComId(),userInfo.getOptIP());
				}
				// 删除项目
				itemService.delPreItem(ids, userInfo);
				this.setNotification(Notification.SUCCESS, "删除成功");
			}
		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:" + redirectPage);

		return mav;
	}

	/**
	 * 汇报项目进度
	 * 
	 * @param itemId
	 * @param progress
	 */
	@ResponseBody
	@RequestMapping(value = "/itemProgressReport")
	public String itemProgressReport(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		String itemProgressDescribe = CommonUtil.dataDicZvalueByCode("progress", null != item.getItemProgress() ? item
				.getItemProgress().toString() : null);
		item.setItemProgressDescribe(itemProgressDescribe);
		boolean succ = itemService.updateItemProgress(item, userInfo);
		if (succ) {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"汇报进度:\"" + itemProgressDescribe + "\"");
			return "更新成功";
		} else {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"汇报进度:\"" + itemProgressDescribe + "\"失败");
			return "更新失败";
		}
	}

	/**
	 * 项目名称变更
	 * 
	 * @param item
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/itemNameUpdate")
	public String itemNameUpdate(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		boolean succ = itemService.updateItemName(item, userInfo);
		if (succ) {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目名称变更为\"" + item.getItemName() + "\"");
			return "更新成功";
		} else {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目名称变更为\"" + item.getItemName() + "\"失败");
			return "更新失败";
		}
	}

	/**
	 * 项目说明变更
	 * 
	 * @param itemId
	 * @param progress
	 */
	@ResponseBody
	@RequestMapping(value = "/itemItemRemarkUpdate")
	public String itemItemRemarkUpdate(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		boolean succ = itemService.updateItemItemRemark(item, userInfo);
		if (succ) {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目说明变更成功");
			return "更新成功";
		} else {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目说明变更失败");
			return "更新失败";
		}
	}
	
	/**
	 * 项目金额变更
	 * @author hcj 
	 * @date: 2018年10月12日 下午4:31:33
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateAmount")
	public String updateAmount(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		boolean succ = itemService.updateAmount(item, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	
	/**
	 * 服务期限变更
	 * @author hcj 
	 * @date: 2018年10月12日 下午4:31:33
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateServiceDate")
	public String updateServiceDate(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		boolean succ = itemService.updateServiceDate(item, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	
	/**
	 * 项目研发负责人变更
	 * @author hcj 
	 * @date: 2018年10月12日 下午4:31:33
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateDevelopLeader")
	public String updateDevelopLeader(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		boolean succ = itemService.updateDevelopLeader(item, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	
	
	/**
	 * 项目公开私有变更
	 * 
	 * @param itemId
	 * @param progress
	 */
	@ResponseBody
	@RequestMapping(value = "/itemPubStateUpdate")
	public String itemPubStateUpdate(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		boolean succ = itemService.updateItemPubState(item, userInfo);
		if (succ) {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目公开私有变更为:\""+(item.getPubState().equals(0)?"私有":"公开")+"\"");
			return "更新成功";
		} else {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目公开私有变更失败");
			return "更新失败";
		}
	}

	/**
	 * 项目母项目关联
	 * 
	 * @param item
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/itemParentIdUpdate")
	public Map<String, String> itemParentIdUpdate(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		Map<String, String> map = new HashMap<String, String>();
		// 查询项目当前设置父节点的父节点
		List<Item> parentItem = itemService.getParentItem(item.getParentId(), userInfo.getComId());
		if (null != parentItem && parentItem.size() > 0) {
			// 验证是否为自己的子项目
			for (Item itemTemp : parentItem) {
				if (itemTemp.getId().equals(item.getId())) {
					map.put("status", "f");
					map.put("info", "不能关联子项目");
					return map;
				}
			}
		}
		item.setComId(userInfo.getComId());
		boolean succ = itemService.updateItemParentId(item, userInfo);
		if (succ) {
			map.put("status", "y");
			map.put("info", "关联成功");
			return map;
		} else {
			map.put("status", "y");
			map.put("info", "关联失败");
			return map;
		}
	}

	/**
	 * 解除项目上级关联
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delpItemRelation")
	public Map<String, String> delpItemRelation(Item item) {
		Map<String, String> map = new HashMap<String, String>();
		UserInfo userInfo = this.getSessionUser();

		boolean succ = itemService.delpItemRelation(item, userInfo);
		if (succ) {
			map.put("status", "y");
			map.put("info", "解除关联成功");
		} else {
			map.put("status", "y");
			map.put("info", "解除关联失败");
		}

		return map;

	}

	/**
	 * 项目来源关联
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateItemPartner")
	public Map<String, String> updateItemPartner(Item item) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		boolean succ = itemService.updateItemPartner(item, userInfo);
		if (succ) {
			map.put("status", "y");
			map.put("info", "来源关联成功");
		} else {
			map.put("status", "n");
			map.put("info", "来源关联失败");
		}
		return map;
	}

	/**
	 * 解除项目上级关联
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delCrmRelation")
	public Map<String, String> delCrmRelation(Item item) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		UserInfo userInfo = this.getSessionUser();

		boolean succ = itemService.delCrmRelation(item, userInfo);
		if (succ) {
			map.put("status", "y");
			map.put("info", "解除客户关联成功");
		} else {
			map.put("status", "n");
			map.put("info", "解除客户关联失败");
		}

		return map;

	}

	/**
	 * 项目负责人变更
	 * 
	 * @param item
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/itemOwnerUpdate")
	public String itemOwnerUpdate(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		UserInfo ownerInfo = userInfoService.getUserInfo(userInfo.getComId(), item.getOwner());
		item.setOwnerName(ownerInfo.getUserName());
		boolean succ = itemService.updateItemOwner(item, userInfo);
		if (succ) {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目负责人变更为\"" + ownerInfo.getUserName() + "\"");
			this.setNotification(Notification.SUCCESS, "更新成功");
			return "更新成功";
		} else {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), item.getId(), userInfo.getId(), userInfo.getUserName(),
					"项目负责人变更为\"" + ownerInfo.getUserName() + "\"失败");
			this.setNotification(Notification.ERROR, "更新失败");
			return "更新失败";
		}
	}

	/**
	 * 项目批量移交页面
	 * 
	 * @param ItemHandOver
	 * @param itemIds
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itemHandOverPage")
	public ModelAndView itemHandOverPage(ItemHandOver itemHandOver, Integer[] ids) {
		ModelAndView view = new ModelAndView("/item/itemHandOver");
		view.addObject("itemHandOver", itemHandOver);
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		if (null != ids) {
			List<Item> listItem = new ArrayList<Item>();
			Item item = null;
			for (Integer id : ids) {
				item = itemService.getItemById(id);
				listItem.add(item);
			}
			view.addObject("listItem", listItem);
		}

		return view;
	}

	/**
	 * 项目批量移交
	 * 
	 * @param itemHandOver
	 * @param itemIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/batchItemHandOver", method = RequestMethod.POST)
	public Map<String, Object> batchItemHandOver(ItemHandOver itemHandOver, Integer[] itemIds) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		itemHandOver.setComId(userInfo.getComId());
		// 构建预交对象数组
		ItemHandOver[] itemHandOvers = new ItemHandOver[itemIds.length];
		if (null != itemIds && itemIds.length > 0) {
			for (int i = 0; i < itemIds.length; i++) {
				// 对象存入
				itemHandOver.setItemId(itemIds[i]);
				ItemHandOver newObj = new ItemHandOver();
				newObj.setComId(itemHandOver.getComId());
				newObj.setItemId(itemIds[i]);
				newObj.setFromUser(itemHandOver.getFromUser());
				newObj.setToUser(itemHandOver.getToUser());
				newObj.setReplayContent(itemHandOver.getReplayContent());
				itemHandOvers[i] = newObj;
			}
		}

		boolean succ = itemService.addItemHandOver(itemHandOvers, userInfo);
		if (succ) {
			map.put("status", "y");
		} else {
			map.put("status", "f");
			map.put("info", "移交失败!");
		}
		return map;
	}

	/**
	 * 跳转单客户移交页面
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/itemHandOverByOnePage")
	public ModelAndView itemHandOverByOnePage(ItemHandOver itemHandOver, Integer itemId) {
		ModelAndView view = new ModelAndView("/item/itemHandOverByOne");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		// 获取被移交客项目信息
		Item item = itemService.getItemById(itemId, userInfo);
		view.addObject("item", item);

		return view;
	}

	/**
	 * 项目参与人变更
	 * 
	 * @param itemId
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/itemSharerUpdate")
	public String itemSharerUpdate(Integer itemId, Integer[] userIds) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		StringBuffer sharerName = null;
		if (null != userIds && userIds.length > 0) {
			// 项目负责人更新
			sharerName = new StringBuffer();
			for (Integer userId : userIds) {
				UserInfo sharerInfo = userInfoService.getUserInfo(userInfo.getComId(), userId);
				sharerName.append(sharerInfo.getUserName() + ",");
			}
			sharerName = new StringBuffer(sharerName.subSequence(0, sharerName.lastIndexOf(",")));
		}
		boolean succ = itemService.updateItemSharer(userInfo, itemId, userIds, sharerName.toString());
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}

	/**
	 * 删除项目参与人
	 * 
	 * @param itemId
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delItemSharer")
	public String delItemSharer(Integer itemId, Integer userId) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		UserInfo sharerInfo = userInfoService.getUserInfo(userInfo.getComId(), userId);
		boolean succ = itemService.delItemSharer(userInfo.getComId(), itemId, userId, userInfo,
				sharerInfo.getUserName());
		if (succ) {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), itemId, userInfo.getId(), userInfo.getUserName(), "删除项目参与人\""
					+ sharerInfo.getUserName() + "\"");
			return "删除成功";
		} else {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), itemId, userInfo.getId(), userInfo.getUserName(), "删除项目参与人\""
					+ sharerInfo.getUserName() + "\"失败");
			return "删除失败";
		}
	}

	/**
	 * 项目删除分享组
	 * 
	 * @param itemId
	 *            项目主键
	 * @param grpId
	 *            分组主键
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delItemGroup")
	public Map<String, String> delItemGroup(Integer itemId, Integer grpId, String grpName) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		UserInfo userInfo = this.getSessionUser();
		itemService.delItemGroup(itemId, grpId, grpName, userInfo);
		map.put("status", "y");
		map.put("info", "删除成功");
		return map;

	}

	/**
	 * 项目标记
	 * 
	 * @param itemId
	 * @param progress
	 */
	@ResponseBody
	@RequestMapping(value = "/remarkItemState")
	public String remarkItemState(Item item) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		String state = null;
		if (4 == item.getState()) {
			state = "完成";
		} else if (3 == item.getState()) {
			state = "挂起";
		} else if (1 == item.getState()) {
			state = "执行";
		}
		// 默认操作成功
		boolean succ = true;
		if (4 == item.getState()) {// 标记完成
			succ = itemService.updateDoneItem(item, userInfo, state);
		} else if (1 == item.getState()) {// 重启
			succ = itemService.updateRestarItem(item, userInfo, state);
		}
		if (succ) {
			return "标记成功";
		} else {
			return "标记失败";
		}
	}

	/**
	 * 获取此项目以及此项目后代项目以外的项目JSON字符串
	 * 
	 * @param item
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/parentItemJson")
	public String parentItemJson(Item item) {
		UserInfo userInfo = this.getSessionUser();
		item.setComId(userInfo.getComId());
		String strJson = itemService.itemStrJson(item);
		return strJson;
	}

	/**
	 * 获取匹配客户JSON字符串
	 * 
	 * @param item
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/partnerJson")
	public String partnerJson(Customer customer) {
		UserInfo userInfo = this.getSessionUser();
		customer.setComId(userInfo.getComId());
		String strJson = itemService.partnerJson(customer);
		return strJson;
	}

	/**
	 * 取得项目最近的项目阶段
	 * 
	 * @param itemId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/itemLatestStagedItem")
	public Map<String, Object> itemLatestStagedItem(Integer itemId) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();

		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		StagedItem stagedItem = itemService.queryTheLatestStagedItem(userInfo.getComId(), itemId);
		map.put("stagedItem", stagedItem);

		return map;
	}

	/**
	 * 项目评论页面
	 * 
	 * @param itemTalk
	 * @return
	 */
	@RequestMapping(value = "/itemTalkPage")
	public ModelAndView itemTalk(ItemTalk itemTalk) {
		ModelAndView view = new ModelAndView("/item/talkItem");
		UserInfo userInfo = this.getSessionUser();
		// 查看项目讨论，删出消息提醒
		todayWorksService.updateTodoWorkRead(itemTalk.getItemId(), userInfo.getComId(), userInfo.getId(),
				ConstantInterface.TYPE_ITEM, 0);

		view.addObject("userInfo", userInfo);
		List<ItemTalk> listItemTalk = itemService.listItemTalk(itemTalk.getItemId(), userInfo.getComId(), "pc");
		view.addObject("listItemTalk", listItemTalk);

		// 获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(
				userInfo.getComId(), ConstantInterface.TYPE_ITEM);
		if (null != listModuleOperateConfig) {
			for (ModuleOperateConfig vo : listModuleOperateConfig) {
				view.addObject(vo.getOperateType(), ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		return view;
	}

	/**
	 * 项目评论回复
	 * 
	 * @param itemTalk
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/replyTalk")
	public ItemTalk replyTalk(ItemTalk itemTalk) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		itemTalk.setComId(userInfo.getComId());
		itemTalk.setUserId(userInfo.getId());
		Integer id = itemService.addItemTalk(itemTalk, userInfo);
		itemTalk = itemService.queryItemTalk(id, userInfo.getComId());
		// 自己添加的，当前自己肯定是叶子
		String itemTalkDivString = replyTalkDivString(itemTalk, "listUpfiles_" + itemTalk.getId() + ".upfileId",
				"filename", "otherItemAttrIframe", userInfo.getComId());
		itemTalk.setItemTalkDivString(itemTalkDivString);
		// 模块日志添加
		itemService.addItemLog(userInfo.getComId(), itemTalk.getItemId(), userInfo.getId(), userInfo.getUserName(),
				"添加新评论");
		return itemTalk;
	}

	/**
	 * 项目讨论回复DIV字符串生存
	 * 
	 * @param itemTalk
	 * @param uploadFileName
	 * @param uploadFileShowName
	 * @param pifreamId
	 * @param comId
	 * @return
	 */
	private String replyTalkDivString(ItemTalk itemTalk, String uploadFileName, String uploadFileShowName,
			String pifreamId, Integer comId) {
		if (null == itemTalk){
			return null;
		}
		// 是子节点
		itemTalk.setIsLeaf(1);
		String sid = RequestContextHolderUtil.getRequest().getParameter("sid");
		UserInfo userInfo = this.getSessionUser();
		StringBuffer divString = new StringBuffer();
		divString.append("\n <div class=\"comment\" id=\"talk_" + itemTalk.getId() + "\">");

		if (!itemTalk.getParentId().equals(-1)) {// 是评论

			divString.append("\n <div class=\"comment\">");
		}
		divString.append("		<img src=\"/downLoad/userImg/" + itemTalk.getComId() + "/" + itemTalk.getUserId()
				+ "?sid=" + sid + "\" title=\"" + itemTalk.getSpeakerName() + "\" class=\"comment-avatar\"></img>");

		divString.append("\n 		<div class='comment-body'>");
		divString.append("\n 			<div class='comment-text'>");
		divString.append("\n 				<div class='comment-header'>");
		divString.append("\n					<a>" + itemTalk.getSpeakerName() + "</a>");
		if (itemTalk.getParentId() != -1) {
			divString.append("					<r style='color:#000;margin:0px 5px'>回复</r>");
			divString.append("\n				<a>" + itemTalk.getpSpeakerName() + "</a>");
		}
		divString.append("						<span>" + itemTalk.getRecordCreateTime().substring(0, 16) + "</span>");
		divString.append("\n 				</div>");
		divString.append("\n				<p class='no-margin-bottom'>" + StringUtil.toHtml(itemTalk.getContent()) + "</p>");

		divString.append("\n				<p class='no-margin-bottom'>");
		// 附件
		List<ItemTalkFile> list = itemTalk.getListItemTalkFile();
		if (null != list && list.size() > 0) {
			divString.append("					<div class=\"file_div\">");
			for (int i = 0; i < list.size(); i++) {
				ItemTalkFile upfiles = list.get(i);
				if ("1".equals(upfiles.getIsPic())) {
					divString.append("				<p class=\"p_text\">");
					divString.append("附件（" + (i + 1) + "）：");
					divString.append("				<img onload=\"AutoResizeImage(350,0,this,'otherItemAttrIframe')\"");
					divString.append("					src=\"/downLoad/down/" + upfiles.getUuid() + "/" + upfiles.getFilename()
							+ "?sid=" + this.getSid() + "\" />");
					divString.append("					&nbsp;<a class=\"fa fa-download\" title=\"下载\" href=\"/downLoad/down/"
							+ upfiles.getUuid() + "/" + upfiles.getFilename() + "?sid=" + this.getSid() + "\"></a>");
					divString
							.append("					&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"showPic('/downLoad/down/"
									+ upfiles.getUuid()
									+ "/"
									+ upfiles.getFilename()
									+ "','"
									+ this.getSid()
									+ "','"
									+ upfiles.getUpfileId()
									+ "','"
									+ ConstantInterface.TYPE_ITEM
									+ "',"
									+ itemTalk.getItemId() + ")\"></a>");
					divString.append("				</p>");
				} else {
					divString.append("				<p class=\"p_text\">");
					divString.append("附件（" + (i + 1) + "）：");
					divString.append(upfiles.getFilename());
					if (upfiles.getFileExt().equals("doc") || upfiles.getFileExt().equals("docx")
							|| upfiles.getFileExt().equals("xls") || upfiles.getFileExt().equals("xlsx")
							|| upfiles.getFileExt().equals("ppt") || upfiles.getFileExt().equals("pptx")) {
						divString
								.append("				&nbsp;<a class=\"fa fa-download\" href=\"javascript:void(0);\" title=\"下载\" onclick=\"downLoad('"
										+ upfiles.getUuid()
										+ "','"
										+ upfiles.getFilename()
										+ "','"
										+ this.getSid()
										+ "')\"></a>");
						divString
								.append("				&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"viewOfficePage('"
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
										+ ConstantInterface.TYPE_ITEM
										+ "',"
										+ itemTalk.getItemId()
										+ ")\"></a>");
					} else if (upfiles.getFileExt().equals("pdf") || upfiles.getFileExt().equals("txt")) {
						divString
								.append("				&nbsp;<a class=\"fa fa-download\" title=\"下载\" href=\"/downLoad/down/"
										+ upfiles.getUuid() + "/" + upfiles.getFilename() + "?sid=" + this.getSid()
										+ "\"></a>");
						divString
								.append("				&nbsp;<a class=\"fa fa-eye\" href=\"javascript:void(0);\" title=\"预览\" onclick=\"viewOfficePage('"
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
										+ ConstantInterface.TYPE_ITEM
										+ "',"
										+ itemTalk.getItemId()
										+ ")\"></a>");
					} else {
						divString
								.append("				&nbsp;<a class=\"fa fa-download\" href=\"javascript:void(0);\" title=\"下载\" onclick=\"downLoad('"
										+ upfiles.getUuid()
										+ "','"
										+ upfiles.getFilename()
										+ "','"
										+ this.getSid()
										+ "')\"></a>");
					}
				}
				divString.append("					</p>");
			}
			divString.append("					</div>");
		}
		divString.append("\n				</p>");

		divString.append("\n 			</div>");
		divString.append("\n 			<div class='comment-footer'>");
		// 判断是否有编辑权限
		ModuleOperateConfig modOptConf = modOptConfService.getModuleOperateConfig(userInfo.getComId(),
				ConstantInterface.TYPE_ITEM, "delete");
		// 发言人可以删除自己的发言
		if (userInfo.getId().equals(itemTalk.getUserId()) && null == modOptConf) {
			divString.append("\n 			<a href='javascript:void(0);' id=\"delOpt_" + itemTalk.getId()
					+ "\" title='删除' onclick=\"delItemTalk('" + itemTalk.getId()
					+ "','1')\"><i class=\"fa fa-trash-o\"></i></a>");
		}
		// 项目没有办结可以讨论
		divString
				.append("\n 				<a id=\"img_"
						+ itemTalk.getId()
						+ "\" name=\"replyImg\" href=\"javascript:void(0);\" class=\"fa fa-comment-o\" title=\"回复\" onclick=\"showArea('"
						+ itemTalk.getId() + "')\"></a>");

		divString.append("\n  			</div>");
		// 回复层
		divString.append("\n 			<div id=\"reply_" + itemTalk.getId()
				+ "\" name=\"replyTalk\" style=\"display:none;\" class=\"panel-body\">");
		divString.append("\n 				<textarea class='form-control' id='operaterReplyTextarea_" + itemTalk.getId()
				+ "' name='operaterReplyTextarea_" + itemTalk.getId() + "'");
		divString.append("\n					rows='' cols='' placeholder='请输入内容……' style='height:55px;'></textarea>");
		divString.append("\n				<div class='panel-body' style='padding-right:0;'>");
		divString.append("\n					<div class='buttons-preview pull-left' style='position: relative;'>");
		// 表情
		divString
				.append("\n 						<a href='javascript:void(0);' class='btn-icon fa fa-meh-o fa-lg' id='biaoQingSwitch_"
						+ itemTalk.getId() + "' onclick=\"addBiaoQingObj('biaoQingSwitch_" + itemTalk.getId()
						+ "','biaoQingDiv_" + itemTalk.getId() + "','operaterReplyTextarea_" + itemTalk.getId()
						+ "');\"></a>");
		// 表情DIV层
		divString
				.append("\n						<div id=\"biaoQingDiv_"
						+ itemTalk.getId()
						+ "\" class=\"blk\" style=\"display:none;position:absolute;width:200px;top:10px;z-index:99;left: 15px\">");
		divString.append("\n							<div class=\"main\">");
		divString.append("\n								<ul style=\"padding: 0px\">");
		// 添加表情包字符串
		divString.append(CommonUtil.biaoQingStr());
		divString.append("\n								</ul>");
		divString.append("\n							</div>");
		divString.append("\n						</div>");
		divString
				.append("\n						<a href='javascript:void(0);' class='btn-icon fa fa-comments-o fa-lg' onclick=\"addIdea('operaterReplyTextarea_"
						+ itemTalk.getId() + "','" + sid + "');\" title='常用意见'></a>");
		divString.append("\n <a class=\"btn-icon fa-lg\" href=\"javascript:void(0)\" title=\"告知人员\" data-todoUser=\"yes\" data-relateDiv=\"todoUserDiv_"+itemTalk.getId()+"\">@</a>");
		divString.append("\n					</div>");
		// 消息通知
		// divString.append("\n					<div class='checkbox pull-left no-margin'>");
		// divString.append("\n						<label>通知方式：</label>");
		// divString.append("\n						<label class='no-padding'>");
		// divString.append("\n							<input type='checkbox'>");
		// divString.append("\n							<span class='text'>邮件</span>");
		// divString.append("\n						</label>");
		// divString.append("\n						<label class='no-padding'>");
		// divString.append("\n							<input type='checkbox'>");
		// divString.append("\n							<span class='text'>短信</span>");
		// divString.append("\n						</label>");
		// divString.append("\n					</div>");
		// 发表
		divString.append("\n					<div class='pull-right'>");
		divString.append("\n						<a href='javascript:void(0)' class='btn btn-info' onclick=\"replyTalk("
				+ itemTalk.getId() + ",this)\" data-relateTodoDiv=\"todoUserDiv_"+itemTalk.getId()+"\">发表</a>");
		divString.append("\n					</div>");
		divString.append("\n					<div style='clear: both;'></div>");
		divString.append("\n	<div id=\"todoUserDiv_"+itemTalk.getId()+"\" class=\"padding-top-10\"></div>");
		// 上传附件
		divString.append("\n					<div class=\"ws-notice\">");
		divString.append(CommonUtil.uploadFileTagStr(uploadFileName, uploadFileShowName, pifreamId,
				userInfo.getComId(), sid));
		divString.append("\n					</div>");

		divString.append("\n				</div>");
		divString.append("\n			</div>");
		divString.append("\n		</div>");

		if (itemTalk.getParentId() > 0) {
			divString.append("\n</div>");
		}

		divString.append("\n</div>");
		return divString.toString();
	}

	/**
	 * 删除项目评论
	 * 
	 * @param itemTalk
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/ajaxDelItemTalk")
	public String ajaxDelItemTalk(ItemTalk itemTalk) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		itemTalk.setComId(userInfo.getComId());
		itemService.delItemTalk(itemTalk, userInfo);
		// 模块日志添加
		itemService.addItemLog(userInfo.getComId(), itemTalk.getItemId(), userInfo.getId(), userInfo.getUserName(),
				"删除评论");
		return "删除成功！";
	}

	/**
	 * 查看项目日志
	 * 
	 * @param itemLog
	 * @return
	 */
	@RequestMapping("/itemLogPage")
	public ModelAndView itemLogPage(ItemLog itemLog) {
		ModelAndView view = new ModelAndView("/item/logItem");
		UserInfo userInfo = this.getSessionUser();
		// 查看项目日志，删出消息提醒
		todayWorksService.updateTodoWorkRead(itemLog.getItemId(), userInfo.getComId(), userInfo.getId(),
				ConstantInterface.TYPE_ITEM, 0);

		List<ItemLog> listItemLog = itemService.listItemLog(itemLog.getItemId(), userInfo.getComId());
		view.addObject("listItemLog", listItemLog);
		return view;
	}

	/**
	 * 查看项目移交记录
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/itemFlowRecord")
	public ModelAndView itemFlowRecord(Integer itemId) {
		ModelAndView view = new ModelAndView("/item/itemFlowRecord");
		UserInfo userInfo = this.getSessionUser();
		// 查看移交记录，删出消息提醒
		todayWorksService.updateTodoWorkRead(itemId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ITEM,
				0);

		// 移交记录
		List<FlowRecord> listFlowRecord = new ArrayList<FlowRecord>();
		listFlowRecord = itemService.listFlowRecord(itemId, userInfo.getComId());
		// 项目详情状态
		Item item = null;
		if (null == listFlowRecord || listFlowRecord.size() == 0) {
			item = itemService.getItemById(itemId, userInfo);

			FlowRecord flowRecord = new FlowRecord();
			flowRecord.setUserId(item.getCreator());
			flowRecord.setAcceptDate(item.getRecordCreateTime());
			flowRecord.setUserName(item.getOwnerName());
			flowRecord.setUuid(item.getUuid());
			flowRecord.setGender(item.getGender());
			listFlowRecord.add(flowRecord);
		} else {
			item = itemService.getItemById(itemId);
		}

		// 任务办结
		if (item.getState() == 4) {
			FlowRecord flowRecord = listFlowRecord.remove(0);
			flowRecord.setState(ConstantInterface.FINISHED_YES);
			listFlowRecord.add(0, flowRecord);
		}
		view.addObject("listFlowRecord", listFlowRecord);
		return view;
	}

	/**
	 * 获取项目附件
	 * 
	 * @param itemUpfile
	 * @return
	 */
	@RequestMapping("/itemUpfilePage")
	public ModelAndView itemUpfilePage(ItemUpfile itemUpfile) {
		ModelAndView view = new ModelAndView("/item/upfileItem");
		UserInfo userInfo = this.getSessionUser();
		// 查看项目附件，删出消息提醒
		todayWorksService.updateTodoWorkRead(itemUpfile.getItemId(), userInfo.getComId(), userInfo.getId(),
				ConstantInterface.TYPE_ITEM, 0);

		List<ItemUpfile> listItemUpfile = itemService.listPagedItemUpfile(itemUpfile, userInfo.getComId(),itemUpfile.getType());
		view.addObject("listItemUpfile", listItemUpfile);
		view.addObject("itemUpfile", itemUpfile);
		view.addObject("userInfo", userInfo);
		return view;
	}

	// /**
	// * 创建子项目
	// * @param item 项目配置信息
	// * @param sharerIds 项目参与人数组
	// * @return
	// * @throws Exception
	// */
	// @ResponseBody
	// @RequestMapping("/addSonItem")
	// public Item addSonItem(Item item,Integer[] sharerIds) throws Exception{
	// UserInfo userInfo = this.getSessionUser();
	// item.setComId(userInfo.getComId());
	// item.setCreator(userInfo.getId());
	// //项目状态标识
	// item.setState(1);
	// if(null!=sharerIds && sharerIds.length>0){
	// List<ItemSharer> listItemSharer = new ArrayList<ItemSharer>();
	// ItemSharer itemSharer = null;
	// for(Integer id : sharerIds){
	// itemSharer = new ItemSharer();
	// itemSharer.setUserId(id);
	// listItemSharer.add(itemSharer);
	// }
	// item.setListItemSharer(listItemSharer);
	// }
	// Integer itemId = itemService.addItem(item,null,userInfo);
	// Item ReturnItem = itemService.getItem(itemId, userInfo);
	// //添加系统日志记录
	// systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),
	// "创建项目：\""+item.getItemName()+"\"",BusinessTypeConstant.type_item,userInfo.getComId());
	// //模块日志添加
	// itemService.addItemLog(userInfo.getComId(), itemId, userInfo.getId(),
	// userInfo.getUserName(), "创建项目：\""+item.getItemName()+"\"");
	// //模块日志添加
	// itemService.addItemLog(userInfo.getComId(), item.getParentId(),
	// userInfo.getId(), userInfo.getUserName(),
	// "创建项目：\""+item.getItemName()+"\"");
	//
	// return ReturnItem;
	// }
	/**
	 * 项目阶段明细查看
	 * 
	 * @param stagedItem
	 * @return
	 */
	@RequestMapping("/stagedItemPage")
	public ModelAndView stagedItemPage(StagedItem stagedItem, String redirectPage) {
		ModelAndView view = new ModelAndView("/item/stagedItem");
		UserInfo userInfo = this.getSessionUser();
		// 查看项目阶段，删出消息提醒
		todayWorksService.updateTodoWorkRead(stagedItem.getItemId(), userInfo.getComId(), userInfo.getId(),
				ConstantInterface.TYPE_ITEM, 0);

		// 是否有删除权限
		ModuleOperateConfig modOptConfUpdate = modOptConfService.getModuleOperateConfig(userInfo.getComId(),
				ConstantInterface.TYPE_ITEM, "update");
		// 是否有删除权限
		ModuleOperateConfig modOptConfDel = modOptConfService.getModuleOperateConfig(userInfo.getComId(),
				ConstantInterface.TYPE_ITEM, "delete");
		if (null != modOptConfUpdate) {// 不能编辑
			view.addObject("editItem", "no");
		} else {
			view.addObject("editItem", "yes");
		}

		if (null != modOptConfDel) {// 不能删除
			view.addObject("delItem", "no");
		} else {
			view.addObject("delItem", "yes");
		}
		Item item = itemService.getItemById(stagedItem.getItemId());

		String itemStagedJsonStr = itemService.itemStagedJsonStr(userInfo.getComId(), stagedItem.getItemId(),
				this.getSid());
		stagedItem.setItemStagedJsonStr(itemStagedJsonStr);
		view.addObject("stagedItem", stagedItem);
		view.addObject("itemStagedJsonStr", itemStagedJsonStr);
		view.addObject("userInfo", userInfo);
		view.addObject("item", item);
		view.addObject("redirectPage", redirectPage);
		return view;
	}

	/**
	 * 跳转项目阶段选择页面
	 * 
	 * @param stagedItem
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/stagedItemForRelevance")
	public ModelAndView stagedItemForRelevance(StagedItem stagedItem) {
		ModelAndView view = new ModelAndView("/item/stagedItemForRelevance");
		UserInfo userInfo = this.getSessionUser();
		Item item = itemService.getItemById(stagedItem.getItemId());
		String itemStagedJsonStr = itemService.stagedItemForRelevanceJsonStr(userInfo.getComId(),
				stagedItem.getItemId());
		stagedItem.setItemStagedJsonStr(itemStagedJsonStr);
		// 判断是否有编辑权限
		ModuleOperateConfig modOptConf = modOptConfService.getModuleOperateConfig(userInfo.getComId(),
				ConstantInterface.TYPE_ITEM, "update");
		if (null != modOptConf) {
			view.addObject("editItem", "no");
		}
		stagedItem.setStagedEnable(((item.getOwner().equals(userInfo.getId()) && null == modOptConf) ? true : false));
		view.addObject("stagedItem", stagedItem);
		view.addObject("itemStagedJsonStr", itemStagedJsonStr);
		view.addObject("userInfo", userInfo);
		view.addObject("item", item);

		return view;
	}

	/**
	 * 项目阶段创建类型选择页面
	 * 
	 * @param stagedItem
	 * @return
	 */
	@RequestMapping("/addSelectPage")
	public ModelAndView addSelect(StagedItem stagedItem) {
		ModelAndView view = new ModelAndView("/item/addSelect");
		view.addObject("stagedItem", stagedItem);
		return view;
	}

	/**
	 * 模块数据和项目阶段节点关联
	 * 
	 * @param stagedInfo
	 * @throws Exception
	 */
	@RequestMapping("/addStagedInfo")
	public String addStagedInfo(StagedInfo stagedInfo) throws Exception {
		if (null == stagedInfo.getModuleType() || "".equals(stagedInfo.getModuleType())){
			return "业务类型不能为空！";
		}
		UserInfo userInfo = this.getSessionUser();
		stagedInfo.setComId(userInfo.getComId());
		stagedInfo.setCreator(userInfo.getId());
		// 阶段项目
		StagedItem stagedItem = itemService.queryStagedItem(stagedInfo.getItemId(), stagedInfo.getStagedItemId(),
				userInfo.getComId());
		// 任务
		Task task = taskService.getTaskById(stagedInfo.getModuleId());
		itemService.addStagedInfo(stagedInfo, userInfo, stagedItem.getStagedName(), task.getTaskName());
		// 模块日志添加
		itemService.addItemLog(userInfo.getComId(), stagedInfo.getItemId(), userInfo.getId(), userInfo.getUserName(),
				"在\"" + stagedItem.getStagedName() + "\"下创建任务\"" + task.getTaskName() + "\"");
		return "关联成功！";
	}

	/**
	 * 跳转项目阶段文件夹添加页面
	 * 
	 * @param stagedItem
	 */
	@RequestMapping("/addStagedFolderPage")
	public ModelAndView addStagedFolderPage(StagedItem stagedItem) {
		ModelAndView view = new ModelAndView("/item/addStagedFolder");
		UserInfo userInfo = this.getSessionUser();
		stagedItem.setComId(userInfo.getComId());
		stagedItem.setCreator(userInfo.getId());
		stagedItem = itemService.queryStagedItem(stagedItem.getItemId(), stagedItem.getId(), userInfo.getComId());
		view.addObject("stagedItem", stagedItem);
		StagedItem initStagedFolderOrder = itemService.initStagedFolderOrder(stagedItem);
		if (null == initStagedFolderOrder.getStagedOrder() || initStagedFolderOrder.getStagedOrder() == 0) {
			initStagedFolderOrder.setStagedOrder(1);
		}
		view.addObject("initStagedFolderOrder", initStagedFolderOrder);
		return view;
	}

	/**
	 * 跳转项目阶段文件添加页面
	 * 
	 * @param stagedInfo
	 */
	@RequestMapping("/addStagedFilePage")
	public ModelAndView addStagedFilePage(StagedInfo stagedInfo) {
		ModelAndView view = new ModelAndView("/item/addStagedFile");
		view.addObject("stagedInfo", stagedInfo);
		view.addObject("sessionUser", this.getSessionUser());
		return view;
	}

	/**
	 * 添加项目阶段文件
	 * 
	 * @param stagedInfo
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addStageInfoFile", method = RequestMethod.POST)
	public Map<String, Object> addStageInfoFile(StagedInfo stagedInfo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		itemService.addStagedFile(stagedInfo, userInfo);
		// 模块日志添加
		itemService.addItemLog(userInfo.getComId(), stagedInfo.getItemId(), userInfo.getId(), userInfo.getUserName(),
				"上传文件 ");
		map.put("status", "y");
		return map;
	}

	/**
	 * 项目树拖拽事件
	 * 
	 * @param nodeId
	 * @param pId
	 * @param nodeType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/zTreeOnDrop")
	public Item zTreeOnDrop(Integer nodeId, Integer pId, Integer itemId, String nodeType, Integer moduleId)
			throws Exception {
		Item item = new Item();
		if (null == nodeType || "".equals(nodeType)) {
			item.setSucc(false);
			item.setPromptMsg("nodeType不能为空！");
			return item;
		}
		UserInfo userInfo = this.getSessionUser();
		if ("folder".equals(nodeType)) {
			boolean succ = itemService.zTreeBeforeDropCheckForItem(itemId, nodeId, pId, userInfo);
			if (!succ) {
				item.setSucc(false);
				item.setPromptMsg("父节点不能向子节点拖拽！");
				return item;
			}
		}
		boolean succ = itemService.zTreeOnDrop(nodeId, pId, itemId, nodeType, userInfo.getComId());
		if (succ) {
			String logInfo = null;
			StagedItem stagedItem = itemService.queryStagedItem(itemId, pId, userInfo.getComId());
			String stagedName = "";
			if(null == stagedItem){
				stagedName = "根节点";
			}else{
				stagedName = stagedItem.getStagedName();
			}
			
			if ("folder".equals(nodeType)) {
				StagedItem dropObj = itemService.queryStagedItem(itemId, nodeId, userInfo.getComId());
				logInfo = "\"" + dropObj.getStagedName() + "\"被拖到\"" + stagedName + "\"文件夹下";
			} else if ("file".equals(nodeType) || "task".equals(nodeType)) {
				StagedInfo dropObj = itemService.queryStagedInfo(nodeId, itemId, userInfo.getComId(), nodeType);
				logInfo = "\"" + dropObj.getModuleName() + "\"被拖到\"" + stagedName + "\"文件夹下";
			} else if ("itemUpFile".equalsIgnoreCase(nodeType)) {
				Upfiles upfile = uploadService.getUpfileById(moduleId);
				logInfo = "项目附件\"" + upfile.getFilename() + "\"被拖到\"" + stagedName + "\"文件夹下";
			} else if ("itemTalkFile".equalsIgnoreCase(nodeType)) {
				Upfiles upfile = uploadService.getUpfileById(moduleId);
				logInfo = "留言附件\"" + upfile.getFilename() + "\"被拖到\"" + stagedName + "\"文件夹下";
			}
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), itemId, userInfo.getId(), userInfo.getUserName(), logInfo);
			item.setSucc(true);
			item.setPromptMsg("更新成功");
			return item;
		} else {
			item.setSucc(false);
			item.setPromptMsg("更新失败");
			return item;
		}
	}

	/**
	 * 项目阶段文件夹下一级数量
	 * 
	 * @param id
	 *            项目阶段主键
	 * @param itemId
	 *            项目主键
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/countItemStageChildren")
	public Map<String, Integer> countItemStageChildren(Integer id, Integer itemId) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		UserInfo userInfo = this.getSessionUser();
		Integer countChild = itemService.countItemStageChildren(id, itemId, userInfo.getComId());
		map.put("count", countChild);
		return map;
	}

	/**
	 * 删除项目阶段数据
	 * 
	 * @param nodeId
	 *            项目详情的主键
	 * @param itemId
	 *            项目主键
	 * @param nodeType
	 *            节点类型
	 * @param moduleId
	 *            关联模块主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/zTreeRemove")
	public String zTreeRemove(Integer nodeId, Integer itemId, String nodeType, Integer moduleId, String delChildren)
			throws Exception {
		if (null == nodeType || "".equals(nodeType)){
			return "nodeType不能为空！";
		}
		UserInfo userInfo = this.getSessionUser();
		// 日志
		String logInfo = null;
		// 将要删除的项目阶段
		StagedItem dropStagedItem = null;
		// 项目阶段的明细
		List<ItemStagedInfo> stagedItemChildren = null;
		if ("folder".equals(nodeType)) {
			// 将要删除的项目阶段
			dropStagedItem = itemService.queryStagedItem(itemId, nodeId, userInfo.getComId());
			// 项目阶段明细
			stagedItemChildren = itemService
					.listStagedItemChildren(userInfo.getComId(), itemId, dropStagedItem.getId());
			dropStagedItem.setStagedItemChildren(stagedItemChildren);
			if (null != delChildren && !"".equals(delChildren)) {
				if ("yes".equals(delChildren)) {
					logInfo = "删除文件夹\"" + dropStagedItem.getStagedName() + "\"以及文件下数据文件";
				} else {
					logInfo = "删除文件夹\"" + dropStagedItem.getStagedName() + "\"";
				}
			}
		} else if ("task".equals(nodeType)) {
			StagedInfo dropObj = itemService.queryStagedInfo(nodeId, itemId, userInfo.getComId(), nodeType);
			logInfo = "删除任务\"" + dropObj.getModuleName() + "\"";
		} else if ("file".equals(nodeType)) {
			StagedInfo dropObj = itemService.queryStagedInfo(nodeId, itemId, userInfo.getComId(), nodeType);
			logInfo = "删除附件\"" + dropObj.getModuleName() + "\"";
		} else if ("itemUpFile".equalsIgnoreCase(nodeType)) {// 项目附件
			Upfiles upfile = uploadService.getUpfileById(moduleId);
			logInfo = "删除项目附件\"" + upfile.getFilename() + "\"";
		} else if ("itemTalkFile".equalsIgnoreCase(nodeType)) {// 留言附件
			Upfiles upfile = uploadService.getUpfileById(moduleId);
			logInfo = "删除留言附件\"" + upfile.getFilename() + "\"";
		}
		boolean succ = itemService.delzTree(nodeId, itemId, nodeType, userInfo.getComId(), moduleId, delChildren,
				dropStagedItem, userInfo, logInfo);
		if (succ) {
			// TODO 删除阶段同时，是否需要删除阶段的任务？
			if ("folder".equals(nodeType)) {
				if (null != stagedItemChildren && !stagedItemChildren.isEmpty() && "yes".equals(delChildren)) {
					for (ItemStagedInfo dropObj : stagedItemChildren) {
						if ("task".equals(dropObj.getType())) {
							Task task = taskService.getTaskById(dropObj.getModuleId());
							taskService.delPreTask(new Integer[] { dropObj.getModuleId() }, userInfo);
							// 添加系统日志记录
							systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),
									"删除任务\"" + task.getTaskName() + "\"", ConstantInterface.TYPE_TASK,
									userInfo.getComId(),userInfo.getOptIP());
						}
					}
				}
			} else if ("task".equals(nodeType)) {
				Task task = taskService.getTaskById(moduleId);
				taskService.delPreTask(new Integer[] { moduleId }, userInfo);
				// 添加系统日志记录
				systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除任务\"" + task.getTaskName()
						+ "\"", ConstantInterface.TYPE_TASK, userInfo.getComId(),userInfo.getOptIP());
			}
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), itemId, userInfo.getId(), userInfo.getUserName(), logInfo);
			return "删除成功！";
		} else {
			return "删除失败！";
		}
	}

	/**
	 * 跳转项目阶段维护列表页面
	 * 
	 * @param stagedItem
	 * @return
	 */
	@RequestMapping("/itemStagedEditePage")
	public ModelAndView itemStagedEditePage(StagedItem stagedItem) {
		ModelAndView view = new ModelAndView("/item/itemStagedEdite");
		UserInfo userInfo = this.getSessionUser();
		stagedItem.setComId(userInfo.getComId());
		stagedItem.setCreator(userInfo.getId());
		List<StagedItem> listStagedItem = itemService.listStagedItem(userInfo.getComId(), stagedItem.getItemId(), -1);
		Item item = itemService.getItemById(stagedItem.getItemId());
		view.addObject("listStagedItem", listStagedItem);
		view.addObject("item", item);
		return view;
	}

	/**
	 * 跳转项目阶段添加页面
	 * 
	 * @param stagedItem
	 * @return
	 */
	@RequestMapping("/addItemStagedPage")
	public ModelAndView addItemStagedPage(StagedItem stagedItem) {
		ModelAndView view = new ModelAndView("/item/addItemStaged");
		UserInfo userInfo = this.getSessionUser();
		stagedItem.setComId(userInfo.getComId());
		stagedItem.setCreator(userInfo.getId());
		view.addObject("itemId", stagedItem.getItemId());
		StagedItem initStagedOrder = itemService.initStagedFolderOrder(stagedItem);
		if (null == initStagedOrder.getStagedOrder() || initStagedOrder.getStagedOrder() == 0) {
			initStagedOrder.setStagedOrder(1);
		}
		view.addObject("initStagedOrder", initStagedOrder);
		return view;
	}

	/**
	 * 添加项目阶段名称
	 * 
	 * @param stagedItem
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxAddStagedFolder")
	public Map<String, Object> ajaxAddStagedFolder(StagedItem stagedItem) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		stagedItem.setComId(userInfo.getComId());
		stagedItem.setCreator(userInfo.getId());
		// 添加项目阶段
		Integer id = itemService.addStagedFolder(stagedItem, userInfo);

		stagedItem.setId(id);
		stagedItem.setStagedOrder(stagedItem.getStagedOrder() + 1);
		// 模块日志添加
		itemService.addItemLog(userInfo.getComId(), stagedItem.getItemId(), userInfo.getId(), userInfo.getUserName(),
				"创建文件夹:\"" + stagedItem.getStagedName() + "\"");

		map.put("stagedItem", stagedItem);
		map.put("status", "y");
		return map;
	}

	/**
	 * 项目阶段序号
	 * 
	 * @param stagedItem
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetStageOrder")
	public Map<String, Object> ajaxGetStageOrder(StagedItem stagedItem) {

		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = this.getSessionUser();
		stagedItem.setComId(userInfo.getComId());
		stagedItem.setCreator(userInfo.getId());
		// 查询阶段项目信息
		StagedItem initStagedOrder = itemService.initStagedFolderOrder(stagedItem);
		// 取出 排序号
		if (null == initStagedOrder.getStagedOrder() || initStagedOrder.getStagedOrder() == 0) {// 没有默认1
			map.put("stageOrder", 1);
		} else {
			map.put("stageOrder", initStagedOrder.getStagedOrder());
		}
		map.put("status", "y");
		return map;
	}

	/**
	 * 项目阶段名称更新
	 * 
	 * @param stagedItem
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/stagedNameUpdate")
	public String stagedNameUpdate(StagedItem stagedItem) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		stagedItem.setComId(userInfo.getComId());
		StagedItem oldStaged = itemService.queryStagedItem(stagedItem.getItemId(), stagedItem.getId(),
				stagedItem.getComId());
		boolean succ = itemService.updateStagedName(stagedItem, userInfo, oldStaged);
		if (succ) {
			// 模块日志添加
			itemService.addItemLog(
					userInfo.getComId(),
					stagedItem.getItemId(),
					userInfo.getId(),
					userInfo.getUserName(),
					"更新项目\"" + oldStaged.getItemName() + "\"的项目阶段\"" + oldStaged.getStagedName() + "\"为\""
							+ stagedItem.getStagedName() + "\"");
			return "更新成功";
		} else {
			// 模块日志添加
			itemService.addItemLog(
					userInfo.getComId(),
					stagedItem.getItemId(),
					userInfo.getId(),
					userInfo.getUserName(),
					"更新项目\"" + oldStaged.getItemName() + "\"的项目阶段\"" + oldStaged.getStagedName() + "\"为\""
							+ stagedItem.getStagedName() + "\"失败");
			return "更新失败";
		}
	}

	/**
	 * 更新项目阶段排序
	 * 
	 * @param stagedItemOld
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/stagedOrderUpdate")
	public String stagedOrderUpdate(StagedItem stagedItem) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		stagedItem.setComId(userInfo.getComId());
		boolean succ = itemService.updateStagedOrder(stagedItem, userInfo);
		StagedItem stagedItemOld = itemService.queryStagedItem(stagedItem.getItemId(), stagedItem.getId(),
				stagedItem.getComId());
		if (succ) {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), stagedItemOld.getItemId(), userInfo.getId(),
					userInfo.getUserName(),
					"更新项目\"" + stagedItemOld.getItemName() + "\"的项目阶段\"" + stagedItemOld.getStagedName() + "\"的\""
							+ stagedItemOld.getStagedOrder() + "\"");
			return "更新成功";
		} else {
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), stagedItemOld.getItemId(), userInfo.getId(),
					userInfo.getUserName(),
					"更新项目\"" + stagedItemOld.getItemName() + "\"的项目阶段\"" + stagedItemOld.getStagedName() + "\"的\""
							+ stagedItemOld.getStagedOrder() + "\"失败");
			return "更新失败";
		}
	}

	/**
	 * 删除项目阶段
	 * 
	 * @param stagedItem
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/itemStagedDel")
	public StagedItem itemStagedDel(StagedItem stagedItem) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		stagedItem.setComId(userInfo.getComId());
		String logInfo = null;
		StagedItem dropStagedItem = null;
		List<ItemStagedInfo> stagedItemChildren = null;
		dropStagedItem = itemService.queryStagedItem(stagedItem.getItemId(), stagedItem.getId(), userInfo.getComId());
		stagedItemChildren = itemService.listStagedItemChildren(userInfo.getComId(), stagedItem.getItemId(),
				dropStagedItem.getId());
		dropStagedItem.setStagedItemChildren(stagedItemChildren);
		if (null != stagedItem.getDelChildren() && !"".equals(stagedItem.getDelChildren())) {
			if ("yes".equals(stagedItem.getDelChildren())) {
				logInfo = "删除文件夹以及文件下数据文件";
			} else {
				logInfo = "删除文件夹";
			}
		}
		boolean succ = itemService.delzTree(dropStagedItem.getId(), dropStagedItem.getItemId(), "folder",
				userInfo.getComId(), 0, stagedItem.getDelChildren(), dropStagedItem, userInfo, logInfo);
		if (succ) {
			if (null != stagedItemChildren && !stagedItemChildren.isEmpty()
					&& "yes".equals(stagedItem.getDelChildren())) {
				for (ItemStagedInfo dropObj : stagedItemChildren) {
					if ("task".equals(dropObj.getType())) {
						Task task = taskService.getTaskById(dropObj.getModuleId());
						taskService.delPreTask(new Integer[] { dropObj.getModuleId() }, userInfo);
						// 添加系统日志记录
						systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),
								"删除任务\"" + task.getTaskName() + "\"", ConstantInterface.TYPE_TASK, 
								userInfo.getComId(),userInfo.getOptIP());
					}
				}
			}
			// 模块日志添加
			itemService.addItemLog(userInfo.getComId(), stagedItem.getItemId(), userInfo.getId(),
					userInfo.getUserName(), logInfo);
			stagedItem.setSucc(succ);
			stagedItem.setPromptMsg("删除成功！");
			return stagedItem;
		} else {
			stagedItem.setSucc(succ);
			stagedItem.setPromptMsg("删除失败！");
			return stagedItem;
		}
	}

	/**
	 * 项目阶段树拖拽前验证，如果被托节点是目标节点的子节点，则验证不通过
	 * 
	 * @param itemId
	 * @param childId
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/zTreeBeforeDropCheckForItem")
	public Item zTreeBeforeDropCheckForItem(int itemId, int childId, int parentId) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		Item item = new Item();
		boolean succ = itemService.zTreeBeforeDropCheckForItem(itemId, childId, parentId, userInfo);
		if (succ) {
			item.setSucc(true);
		} else {
			item.setSucc(false);
		}
		return item;
	}

	/**
	 * 异步加载阶段详情列表
	 * 
	 * @param itemId
	 *            项目主键
	 * @param stagedId
	 *            项目阶段主键
	 * @param pageNum
	 *            当前页码
	 * @param pageSize
	 *            分页数
	 * @param taskState 任务状态筛选
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxListStaged")
	public Map<String, Object> ajaxListStaged(Integer itemId, Integer stagedId, Integer pageNum, Integer pageSize,
			String[] relateMod, String moduleName,Integer taskState) {
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

		StagedInfo stagedInfo = new StagedInfo();
		stagedInfo.setItemId(itemId);
		stagedInfo.setStagedItemId(stagedId);
		// 设置查询的模块名称
		stagedInfo.setModuleName(moduleName);
		stagedInfo.setTaskState(-1==taskState?null:taskState);//任务状态筛选

		// 分页查询项目阶段详情
		List<StagedInfo> stagedInfos = itemService.listPagedStagedInfo(userInfo, stagedInfo, relateMod);
		map.put("status", "y");
		map.put("total", PaginationContext.getTotalCount());
		map.put("stagedInfos", stagedInfos);
		return map;
	}

	/**
	 * 修改项目关联
	 * 
	 * @param stagedInfo
	 *            原有的项目关联信息
	 * @param newStagedId
	 *            现在关联的阶段主键
	 * @param newStagedName
	 *            当前关联的阶段名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxUpdateStagedItem")
	public Map<String, Object> ajaxUpdateStagedItem(StagedInfo stagedInfo, Integer newStagedId, String newStagedName) {
		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		itemService.updateStagedItem(userInfo, stagedInfo, newStagedId, newStagedName);
		map.put("status", "y");
		return map;
	}

	/**
	 * 删除项目关联
	 * 
	 * @param stagedInfo
	 *            原有的项目关联信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxDeleteStagedItem")
	public Map<String, Object> ajaxDeleteStagedItem(StagedInfo stagedInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		itemService.deleteStagedItem(userInfo, stagedInfo);
		map.put("status", "y");
		return map;
	}

	/**
	 * 删除项目关联数据
	 * 
	 * @param stagedInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxdeleteStagedItem")
	public Map<String, Object> ajaxdeleteStagedItem(StagedInfo stagedInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		return map;
	}

	/**
	 * 获取个人私有组集合 * @param itemId
	 * 
	 * @return
	 */
	@RequestMapping(value = "/querySelfGroup")
	public ModelAndView querySelfGroup(Integer itemId) {
		ModelAndView view = new ModelAndView("/item/listSelfGroup");
		UserInfo userInfo = this.getSessionUser();
		// 个人组群集合
		List<SelfGroup> listSelfGroup = itemService.listSelfGroupOfItem(itemId, userInfo.getId(), userInfo.getComId());
		view.addObject("listSelfGroup", listSelfGroup);
		return view;
	}

	/**
	 * 项目成员组关联
	 * 
	 * @param itemId
	 * @param grpId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editItemGroup")
	public Item editItemGroup(Integer itemId, Integer[] grpId) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		Item item = new Item();
		boolean succ = itemService.updateItemGroup(itemId, grpId, userInfo);
		if (succ) {
			item.setSucc(true);
			item.setPromptMsg("更新成功！");
		} else {
			item.setSucc(false);
			item.setPromptMsg("更新失败！");
		}
		return item;
	}

	/**
	 * 验证与验证项目名称相似的项目
	 * 
	 * @param itemName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkSimilarItemByItemName")
	public int checkSimilarItemByItemName(String itemName) {
		if (null == itemName || "".equals(itemName.trim())) {
			return 0;
		}
		@SuppressWarnings("unused")
		List<Item> listItem = itemService.listSimilarItemsByCheckItemName(itemName, this.getSessionUser().getComId());
		return PaginationContext.getTotalCount();
	}

	/**
	 * 跳转显示相似项目列表页面
	 * 
	 * @param itemName
	 * @return
	 */
	@RequestMapping("/similarItemsPage")
	public ModelAndView similarItemsPage(String itemName) {
		ModelAndView view = new ModelAndView("/item/listSimilarItem");
		UserInfo userInfo = this.getSessionUser();
		if (null != itemName && !"".equals(itemName.trim())) {
			List<Item> listItem = itemService.listSimilarItemsByCheckItemName(itemName, userInfo.getComId());
			view.addObject("listItem", listItem);
		}
		return view;
	}

	/**
	 * 合并项目信息前验证或是提示跳转页面
	 * 
	 * @param redirectPage
	 *            跳转页面
	 * @param ids
	 *            参与合并的项目
	 * @return
	 */
	@RequestMapping(value = "/itemCompressPage")
	public ModelAndView itemCompressPage(String redirectPage, Integer[] ids) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/item/itemCompress");
		Integer index = 0;
		// 存放需要合并的项目主键
		Set<Integer> itemIds = new HashSet<Integer>();
		if (null != ids) {
			for (Integer itemId : ids) {
				itemIds.add(itemId);
			}
			// 遍历待合并的项目主键
			for (Integer itemId : ids) {
				// 查询项目详情
				Item item = itemService.getItemById(itemId, userInfo);
				// 查询项目当前设置父节点的父节点
				List<Item> parentItems = itemService.getParentItem(itemId, userInfo.getComId());

				// 默认可以合并保留
				Integer flag = 1;
				// 该项目有父节点
				if (null != parentItems && parentItems.size() > 0) {
					for (Item parentItem : parentItems) {
						if (itemIds.contains(parentItem.getId())) {// 该项目是子项目,只能合并到其他项目中
							flag = 0;
							break;
						}
					}
				}
				view.addObject("itemComPress" + index, flag);
				view.addObject("item" + index, item);
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
	 * 合并项目信息前验证或是提示
	 * 
	 * @param redirectPage
	 *            跳转页面
	 * @param crm
	 *            合并后项目信息
	 * @param ids
	 *            参与合并的项目主键
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itemCompress")
	public ModelAndView itemCompress(String redirectPage, Item item, Integer[] ids) throws Exception {
		UserInfo userInfo = this.getSessionUser();

		itemService.updateItemForCompress(item, ids, userInfo);
		this.setNotification(Notification.SUCCESS, "合并成功");
		ModelAndView view = new ModelAndView("/refreshParent");
		return view;
	}

	/**
	 * 项目整理项目阶段明细数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateStagInfo")
	public Map<String, Object> updateStagInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		map.put("status", "y");
		itemService.updateStagInfo(userInfo);
		map.put("info", "整理完成！");
		return map;
	}

	/**
	 * 跳转客户添加项目页面
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/addCrmItemPage")
	public ModelAndView addCrmItemPage(Item item) {

		UserInfo userInfo = this.getSessionUser();

		ModelAndView view = new ModelAndView("/item/addCrmItem");
		if (null != item.getPartnerId()) {// 项目有设置关联客户
			Customer crm = crmService.getCrmById(item.getPartnerId());
			view.addObject("crm", crm);
		}
		view.addObject("userInfo", userInfo);
		return view;
	}

	/**
	 * 跳转客户添加项目页面
	 * 
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addCrmItem", method = RequestMethod.POST)
	public Map<String, Object> addCrmItem(Item item) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		// 企业号
		item.setComId(userInfo.getComId());
		// 创建人
		item.setCreator(userInfo.getId());
		// 删除标识(正常)
		item.setDelState(0);
		// 项目状态标识
		item.setState(1);
		itemService.addItem(item, userInfo);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 删除项目附件、留言附件
	 * @param itemId
	 * @param itemUpFileId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delItemUpfile")
	public Map<String, Object> delItemUpfile(Integer itemId,Integer itemUpFileId,String type) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		itemService.delItemUpfile(itemUpFileId,type,userInfo,itemId);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 更新项目进度
	 * @author hcj 
	 * @date: 2018年10月15日 下午1:34:32
	 * @param id
	 * @param startTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateItemProgress")
	public HttpResult<String> updateItemProgress(ItemProgress itemProgress,String startTime){
		UserInfo userInfo = this.getSessionUser();
		itemService.updateItemProgress(itemProgress, userInfo);
		return new HttpResult<String>().ok("更新成功");
	}
	
	/**
	 * 更新所属产品
	 * @author hcj 
	 * @date: 2018年10月16日 上午10:21:19
	 * @param itemProgress
	 * @param startTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateProduct")
	public HttpResult<String> updateProduct(Item item,String startTime){
		UserInfo userInfo = this.getSessionUser();
		itemService.updateProduct(item, userInfo);
		return new HttpResult<String>().ok("更新成功");
	}
	
	/**
	 * 维护记录页面
	 * @author hcj 
	 * @date: 2018年10月16日 下午3:34:55
	 * @param itemUpfile
	 * @return
	 */
	@RequestMapping("/maintenanceRecord")
	public ModelAndView maintenanceRecord(ItemUpfile itemUpfile) {
		ModelAndView view = new ModelAndView("/item/maintenanceRecord");
		UserInfo userInfo = this.getSessionUser();
		List<ItemUpfile> lists = itemService.listPagedMaintenanceRecord(itemUpfile, userInfo);
		view.addObject("lists", lists);
		view.addObject("itemUpfile", itemUpfile);
		view.addObject("userInfo", userInfo);
		return view;
	}
	
}
