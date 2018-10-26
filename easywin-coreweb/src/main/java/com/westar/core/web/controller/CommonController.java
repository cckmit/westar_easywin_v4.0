package com.westar.core.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Area;
import com.westar.base.model.ConsumeType;
import com.westar.base.model.CustomerType;
import com.westar.base.model.DataDic;
import com.westar.base.model.Department;
import com.westar.base.model.FileClassify;
import com.westar.base.model.FileDetail;
import com.westar.base.model.FileViewScope;
import com.westar.base.model.Logs;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.pojo.CommonLog;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.ExcelExportUtil;
import com.westar.base.util.TableToXls;
import com.westar.core.service.ConsumeService;
import com.westar.core.service.CrmService;
import com.westar.core.service.DailyService;
import com.westar.core.service.DepartmentService;
import com.westar.core.service.FileCenterService;
import com.westar.core.service.FinancialService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.ItemService;
import com.westar.core.service.LogsService;
import com.westar.core.service.OnlineLearnService;
import com.westar.core.service.ProductService;
import com.westar.core.service.RoleService;
import com.westar.core.service.SelfGroupService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TaskService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.service.VoteService;
import com.westar.core.service.WeekReportService;
import com.westar.core.service.WorkFlowService;
import com.westar.core.web.DataDicContext;

@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	SelfGroupService selfGroupService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	FileCenterService fileCenterService;

	@Autowired
	WeekReportService weekReportService;

	@Autowired
	CrmService crmService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	OnlineLearnService onlineLearnService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	LogsService logsService;
	
	@Autowired
	VoteService voteService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	ViewRecordService viewRecordService;

	@Autowired
	DailyService dailyService;
	
	@Autowired
	FinancialService financialService;
	
	@Autowired
	ConsumeService consumeService;

    @Autowired
    RoleService roleService;

	@Autowired
    ProductService productService;

    /**
	 * 转向机构单选页面
	 */
	@RequestMapping("/orgOnePage")
	public ModelAndView orgOnePage() {
		return new ModelAndView("/common/orgOne").addObject("sessionUser",
				this.getSessionUser());
	}

	/**
	 * 部门单选 页面
	 * 
	 * @return
	 */
	@RequestMapping("/depOnePage")
	public ModelAndView depOnePage(Integer rootId) {
		ModelAndView mav = new ModelAndView("/common/depOne").addObject(
				"sessionUser", this.getSessionUser());
		mav.addObject("rootId",rootId);
		return mav;
	}

	/**
	 * 科室单选 页面
	 * 
	 * @return
	 */
	@RequestMapping("/officeOnePage")
	public ModelAndView officeOnePage() {
		return new ModelAndView("/common/officeOne").addObject("sessionUser",
				this.getSessionUser());
	}

	/**
	 * 人员多选机构树
	 * 
	 * @return
	 */
	@RequestMapping("/userMoreSelect/orgTreePage")
	public ModelAndView userMoreSelect_orgTreePage() {
		return new ModelAndView("/common/userMoreSelect/orgTree").addObject(
				"sessionUser", this.getSessionUser());
	}

	/**
	 * 人员多选 显示人员表
	 * 
	 * @return
	 */
	@RequestMapping("/userMoreSelect/userTable")
	public ModelAndView userMoreSelect_userTable(UserInfo userInfo) {
		UserInfo user = this.getSessionUser();
		userInfo.setComId(user.getComId());
		userInfo.setEnabled(ConstantInterface.SYS_ENABLED_YES);
		userInfo.setId(user.getId());
		List<UserInfo> list = userInfoService.listUser(userInfo);
		return new ModelAndView("/common/userMoreSelect/userTable", "list",
				list);
	}

	/**
	 * 根据分组查询人员 人员多选
	 * 
	 * @param groupId
	 *            为空则标需要最近使用的人员
	 * @param onlySubState
	 *            是否只查询下属
	 * @return
	 */
	@RequestMapping("/userMoreSelect/group/userTable")
	public ModelAndView userMoreSelect_group_userTable(Integer groupId,String onlySubState) {
		ModelAndView mav = new ModelAndView("/common/userMoreSelect/userTable");
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo",userInfo);
		UserInfo latestInfo = userInfoService.getUserInfo(userInfo.getComId(),userInfo.getId());
		mav.addObject("latestInfo",latestInfo);
		List<UserInfo> list = new ArrayList<UserInfo>();
		if (null != groupId) {
			// 列出分组的成员
			list = selfGroupService.listGroupUser(groupId, userInfo,onlySubState);
		} else {
			if(!CommonUtil.isNull(latestInfo.getDefShowGrpId())) {//如果设置有个人默认显示组，则显示默认显示组人员
				// 默认显示组
				list = selfGroupService.listGroupUser(latestInfo.getDefShowGrpId(), userInfo,onlySubState);
			}else {
				// 列出常用的人员
				list = userInfoService.listUsedUserV2(userInfo,onlySubState, 10);
			}
			mav.addObject("defaultSet", "yes");
			mav.addObject("ininState", "ininState");
		}
		mav.addObject("list", list);
		return mav;
	}

	/**
	 * 获得组织机构树JSON数据
	 */
	@ResponseBody
	@RequestMapping("/listTreeOrganization")
	public List<Department> listTreeOrganization(Department dep) {
		UserInfo userInfo = this.getSessionUser();
		// 查询所有启用的组织结构树 默认查询所有已启用的机构
		dep.setComId(userInfo.getComId());
		if (null != dep && null == dep.getParentId()) {
			dep.setParentId(-1);
		}
		dep.setEnabled(ConstantInterface.SYS_ENABLED_YES);
		List<Department> list = departmentService.listTreeOrganization(dep);

		return list;
	}
	
	/**
	 * 异步取得数据字典数据
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listDataDic")
	public List<DataDic> listDataDic(String type) {
		if(StringUtils.isEmpty(type)){
			return null;
		}
		List<DataDic> listDataDic=DataDicContext.getInstance().listTreeDataDicByType(type);
		return listDataDic;
	}

	/**
	 * 获得分组树JSON数据
	 */
	@ResponseBody
	@RequestMapping("/listUserGroup")
	public List<SelfGroup> listUserGroup(SelfGroup selfGroup) {
		UserInfo userInfo = this.getSessionUser();
		// 查询所有启用的组织结构树 默认查询所有已启用的机构
		selfGroup.setComId(userInfo.getComId());
		selfGroup.setOwner(userInfo.getId());
		List<SelfGroup> list = selfGroupService.listUserGroup(userInfo,selfGroup);
		return list;
	}

	/**
	 * 转向到人员多选页面
	 * 
	 * @return
	 */
	@RequestMapping("/userMorePage")
	public ModelAndView userMorePage() {
		return new ModelAndView("/common/userMoreSelect/userMore");
	}

	/**
	 * 转向到人员单选页面
	 * 
	 * @return
	 */
	@RequestMapping("/userOnePage")
	public ModelAndView userOnePage() {
		return new ModelAndView("/common/userOneSelect/userOne");
	}

	/**
	 * 人员多单选显示人员表
	 * 
	 * @return
	 */
	@RequestMapping("/userOneSelect/userTable")
	public ModelAndView userOneSelect_userTable(UserInfo userInfo) {
		UserInfo user = this.getSessionUser();
		userInfo.setComId(user.getComId());
		userInfo.setEnabled(ConstantInterface.SYS_ENABLED_YES);
		List<UserInfo> list = userInfoService.listUser(userInfo);
		return new ModelAndView("/common/userOneSelect/userTable", "list", list).addObject("userInfo", user);
	}

	/**
	 * 根据分组查询人员 人员单选
	 * 
	 * @param groupId
	 * @return
	 */
	@RequestMapping("/userOneSelect/group/userTable")
	public ModelAndView userTableOfGroup(Integer groupId,String onlySubState) {
		UserInfo userInfo = this.getSessionUser();

		ModelAndView mav = new ModelAndView("/common/userOneSelect/userTable");
		UserInfo latestInfo = userInfoService.getUserInfo(userInfo.getComId(),userInfo.getId());
		mav.addObject("latestInfo",latestInfo);
		List<UserInfo> list = new ArrayList<UserInfo>();

		if (null != groupId) {
			// 列出分组人员
			list = selfGroupService.listGroupUser(groupId, userInfo,onlySubState);
		} else {
            //如果设置有个人默认显示组，则显示默认显示组人员
			if(!CommonUtil.isNull(latestInfo.getDefShowGrpId())) {
				// 默认显示组
				list = selfGroupService.listGroupUser(latestInfo.getDefShowGrpId(), userInfo,onlySubState);
			}else {
				// 列出常用的人员
				list = userInfoService.listUsedUser(userInfo.getComId(),
						userInfo.getId(), 10);
			}
			mav.addObject("defaultSet", "yes");
			mav.addObject("ininState", "ininState");
		}
		mav.addObject("list", list);
		mav.addObject("userInfo", userInfo);

		return mav;
	}

    /**
     * 根据角色查询人员
     * @param roleId
     * @return
     */
    @RequestMapping("/role/userTable")
    public ModelAndView userTableOfRole(Integer roleId,Integer isMore) {
        UserInfo userInfo = this.getSessionUser();

        ModelAndView mav = null;
        if(null != isMore && isMore == 1){
            mav = new ModelAndView("/common/userMoreSelect/userTable");
        }else{
            mav = new ModelAndView("/common/userOneSelect/userTable");
        }
        UserInfo latestInfo = userInfoService.getUserInfo(userInfo.getComId(),userInfo.getId());
        mav.addObject("latestInfo",latestInfo);
        List<UserInfo> list = roleService.listUserByRoleId(roleId,userInfo);

        mav.addObject("defaultSet", "yes");
        mav.addObject("ininState", "ininState");
        mav.addObject("list", list);
        mav.addObject("userInfo", userInfo);

        return mav;
    }

	/**
	 * 人员多选机构树
	 * 
	 * @return
	 */
	@RequestMapping("/userOneSelect/orgTreePage")
	public ModelAndView userOneSelect_orgTreePage() {
		return new ModelAndView("/common/userOneSelect/orgTree").addObject(
				"sessionUser", this.getSessionUser());
	}

	/**
	 * 转向到机构多选页面显示页面
	 * 
	 * @return
	 */
	@RequestMapping("/orgMorePage")
	public ModelAndView orgMorePage() {
		return new ModelAndView("/common/orgMoreSelect/orgMore");
	}

	/**
	 * 转向到部门多选页面显示页面
	 * 
	 * @return
	 */
	@RequestMapping("/depMorePage")
	public ModelAndView depMorePage() {
		return new ModelAndView("/common/depMoreSelect/depMore");
	}

	/**
	 * 页面转向 按绩效方案分组多选部门
	 * 
	 * @return
	 */
	@RequestMapping("/perGroupDepMorePage")
	public ModelAndView perGroupDepMorePage() {
		return new ModelAndView("/common/perGroupepMoreSelect/perGroupDepMore");
	}

	/**
	 * 页面转向 按绩效方案分组 单选部门
	 * 
	 * @return
	 */
	@RequestMapping("/perGroupDepOnePage")
	public ModelAndView perGroupDepOnePage() {
		return new ModelAndView("/common/perGroupDepOne").addObject(
				"sessionUser", this.getSessionUser());
	}

	/**
	 * 转向到科室多选页面显示页面
	 * 
	 * @return
	 */
	@RequestMapping("/officeMorePage")
	public ModelAndView officeMorePage() {
		return new ModelAndView("/common/officeMoreSelect/officeMore");
	}

	/**
	 * 机构多选 机构树显示页面
	 * 
	 * @return
	 */
	@RequestMapping("/orgMoreSelect/orgTreePage")
	public ModelAndView orgMoreSelect_orgTreePage() {
		return new ModelAndView("/common/orgMoreSelect/orgTree").addObject(
				"sessionUser", this.getSessionUser());
	}

	/**
	 * 部门多选 机构树显示页面
	 * 
	 * @return
	 */
	@RequestMapping("/depMoreSelect/orgTreePage")
	public ModelAndView depMoreSelect_orgTreePage() {
		return new ModelAndView("/common/depMoreSelect/orgTree").addObject(
				"sessionUser", this.getSessionUser());
	}

	/**
	 * 按绩效方案部门多选 机构树显示页
	 * 
	 * @return
	 */
	@RequestMapping("/perGroupepMoreSelect/orgTreePage")
	public ModelAndView perGroupepMoreSelect_orgTreePage() {
		return new ModelAndView("/common/perGroupepMoreSelect/orgTree")
				.addObject("sessionUser", this.getSessionUser());
	}

	/**
	 * 科室多选 机构树显示页面
	 * 
	 * @return
	 */
	@RequestMapping("/officeMoreSelect/orgTreePage")
	public ModelAndView officeMoreSelect_orgTreePage() {
		return new ModelAndView("/common/officeMoreSelect/orgTree").addObject(
				"sessionUser", this.getSessionUser());
	}

	/**
	 * 转向到机构多选页面显示页面
	 * 
	 * @return
	 */
	@RequestMapping("/roleMorePage")
	public ModelAndView roleMorePage() {
		return new ModelAndView("/common/roleMoreSelect/roleMore");
	}

	/**
	 * 角色多选 角色树显示页面
	 * 
	 * @return
	 */
	@RequestMapping("/roleMoreSelect/roleTreePage")
	public ModelAndView roleMoreSelect_roleTreePage() {
		return new ModelAndView("/common/roleMoreSelect/roleTree");
	}

	/**
	 * 角色多选 角色树显示页面
	 * 
	 * @return
	 */
	@RequestMapping("/listOrgGroupCheck")
	public ModelAndView listOrgGroupCheck(SelfGroup group) {
		UserInfo user = this.getSessionUser();
		group.setOwner(user.getId());
		group.setComId(user.getComId());
		List<SelfGroup> list = selfGroupService.listUserGroup(user,group);
		return new ModelAndView("/common/listOrgGroupCheck").addObject("list",
				list);
	}

	/**
	 * 获取当前服务器时间
	 * 
	 * @param caseNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getNowTime")
	public Map<String, String> getNowTime(Integer caseNum) {
		if (caseNum == null) {
			caseNum = 1;
		}
		Map<String, String> map = new HashMap<String, String>();
		String time = DateTimeUtil.getNowDateStr(caseNum);
		map.put("time", time);
		return map;
	}

	/**
	 * 更改用户的最后在线时间
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateLastOnlineTime")
	public Map<String, Object> updateLastOnlineTime() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = this.getSessionUser();
		if (user != null) {
			userInfoService.updateLastOnlineTime(user.getId(), user.getComId());
			int countOnline = userInfoService.countOnlineUser();
			map.put("ifLogin", true);
			map.put("countOnline", countOnline);
		} else {
			map.put("ifLogin", false);
		}
		return map;
	}

	/**
	 * 导出excel
	 * 
	 * @param htmlContent
	 * @param response
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public void exportExcel(String htmlContent, String fileName,
			HttpServletResponse response, HttpServletRequest request) {
		new ExcelExportUtil().exportExcel(htmlContent, fileName, response,
				request);
	}
	
	/**
	 * 导出excel
	 * 
	 * @param tableHtml
	 * @param response
	 */
	@RequestMapping(value = "/tableToXls", method = RequestMethod.POST)
	public ModelAndView tableToXls(String tableHtml, String fileName,HttpServletResponse response, HttpServletRequest request) {
			StringBuilder table = new StringBuilder();	
			table.append(tableHtml);
			
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			String agent = (String) request.getHeader("USER-AGENT");
			try {
				if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {// 火狐
					String enableFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8"))))+ "?=";
					response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName+".xls");
				} else {
					String enableFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
					response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName+".xls");
				}
				response.getOutputStream().write(TableToXls.process(table));
			} catch (Exception  e) {
				System.err.println("获取导出Excel内部异常");
				e.printStackTrace();
			}
			return null;
	}
	/**
	 * 文件夹以及文件移动
	 * 
	 * @return
	 */
	@RequestMapping("/dirOnePage")
	public ModelAndView dirOnePage(String type) {
		ModelAndView mav = new ModelAndView("/common/dirOne");
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("sessionUser",sessionUser);
		mav.addObject("type", type);
		return mav;
	}

	/**
	 * 获得文件夹列表树
	 * 
	 * @param fileClassify
	 * @param preDirs
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listTreeDir")
	public List<FileClassify> listTreeDirForSelect(FileClassify fileClassify,String preDirs) {
		UserInfo userInfo = this.getSessionUser();
		// 查询所有启用的组织结构树 默认查询所有已启用的机构
		fileClassify.setComId(userInfo.getComId());
		fileClassify.setUserId(userInfo.getId());
		List<FileClassify> list = fileCenterService.listTreeDirForSelect(fileClassify,preDirs);
		return list;
	}
	/**
	 * 获得文件夹列表树
	 * 
	 * @param fileClassify
	 * @param preDirs
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listVideoTreeDir")
	public List<FileClassify> listVideoTreeDir(FileClassify fileClassify,String preDirs) {
		UserInfo userInfo = this.getSessionUser();
		// 查询所有启用的组织结构树 默认查询所有已启用的机构
		fileClassify.setComId(userInfo.getComId());
		fileClassify.setUserId(userInfo.getId());
		List<FileClassify> list = fileCenterService.listTreeDirForSelect(fileClassify,preDirs);
		return list;
	}
	/**
	 * 文件夹以及文件移动
	 * 
	 * @return
	 */
	@RequestMapping("/shareGroupPage")
	public ModelAndView shareGroupPage(Integer id, String busType) {
		ModelAndView mav = new ModelAndView("/common/shareGroup");
		UserInfo userInfo = this.getSessionUser();
		List<Integer> list = new ArrayList<Integer>();
		if (ConstantInterface.TYPE_FILE.equals(busType) ||ConstantInterface.TYPE_LEARN.equals(busType)) {// 是文件
			if (id > 0) {
				// 文件详情
				FileDetail fileDetail = fileCenterService.getFileDetailScope(id, userInfo.getComId());
				if (null != fileDetail) {
					if (null != fileDetail.getFileViewScopes()&& fileDetail.getFileViewScopes().size() > 0) {
						for (FileViewScope fileViewScope : fileDetail.getFileViewScopes()) {
							list.add(fileViewScope.getGrpId());
						}
					}
					Integer scopeType = fileDetail.getScopeType();
					// 转换分享类型，用于比较
					if (scopeType == 0) {// 是所有同事
						scopeType = 1;
					} else if (scopeType == 1) {// 是自定义的分组
						scopeType = 0;
					}
					mav.addObject("scopeType", scopeType);
				} else {
					mav.addObject("scopeType", -100);
				}
			} else {
				// 上次使用的分组
				List<UsedGroup> usedGroups = userInfoService.listUsedGroup(
						userInfo.getComId(), userInfo.getId());
				if (null != usedGroups && usedGroups.size() > 0) {
					Integer scopeType = 2;
					for (UsedGroup usedGroup : usedGroups) {
						if ("0".equals(usedGroup.getGroupType())) {
							scopeType = 1;
						} else if ("2".equals(usedGroup.getGroupType())) {
							scopeType = 2;
						} else {
							scopeType = 0;
							list.add(usedGroup.getGrpId());
						}
					}
					mav.addObject("scopeType", scopeType);
				} else {
					mav.addObject("scopeType", -100);
				}
			}
		}
		// 个人组群集合
		List<SelfGroup> listSelfGroup = userInfoService.listSelfGroup(
				userInfo.getComId(), userInfo.getId());
		String selfGroupStr = CommonUtil.selfGroup2Json(listSelfGroup);
		mav.addObject("selfGroupStr", selfGroupStr);
		mav.addObject("sessionUser", userInfo);
		mav.addObject("list", list);
		return mav;
	}
	
	/**
	 * 跳转到分享范围设定界面
	 * @return
	 */
	@RequestMapping("/listShareUsersPage")
	public ModelAndView listShareUsersPage() {
		return new ModelAndView("/common/listShareUsers").addObject("userInfo", this.getSessionUser());
	}
	/**
	 * 分享人员的数据显示
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListShareUsers")
	public Map<String,Object> ajaxListShareUsers(Integer busId,String busType) {
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(ConstantInterface.TYPE_FILE.equals(busType) 
				|| ConstantInterface.TYPE_LEARN.equals(busType)){
			map = fileCenterService.listAllShareUser(sessionUser,busId);
		}
		return map;
	}

	/**
	 * 客户区域单选页面
	 * 
	 * @return
	 */
	@RequestMapping("/areaOnePage")
	public ModelAndView areaOnePage() {
		return new ModelAndView("/common/areaOnePage");
	}

	/**
	 * 客户区域单选列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/areaOne")
	public List<Area> areaOne() {
		UserInfo user = this.getSessionUser();
		List<Area> list = crmService.listArea(user.getComId());
		return list;
	}

	/**
	 * 跳转到预览office附件页面
	 * 
	 * @return
	 */
	@RequestMapping("/viewOfficePage")
	public ModelAndView viewOfficePage(Upfiles file) {
		ModelAndView mav = new ModelAndView("/officeFile/viewOfficeFile");
		mav.addObject("file", file);
		return mav;
	}

	/**
	 * 预览图片附件页面
	 * 
	 * @param filepath
	 *            : 图片
	 * @return
	 */
	@RequestMapping("/showPic")
	public ModelAndView showPic(String filepath, String fileExt) {
		ModelAndView mav = new ModelAndView();
		if (fileExt.toLowerCase().equals("txt")) {
			mav.setViewName("/officeFile/showTxt");

		} else {
			mav.setViewName("/officeFile/showPic");
		}
		mav.addObject("filepath", filepath);
		return mav;
	}

	/**
	 * 查看记录
	 * 
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            业务主键
	 * @param ifreamName
	 *            所在的ifream
	 * @return
	 */
	@RequestMapping("/listViewRecord")
	public ModelAndView listViewRecord(String busType, Integer busId,
			String ifreamName) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/common/listViewRecord");
		// 浏览的人员
		List<ViewRecord> listViewRecord = viewRecordService.listViewRecord(
				userInfo, busType, busId);
		mav.addObject("listViewRecord", listViewRecord);
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
	/**
	 * 日志记录
	 * 
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            业务主键
	 * @param ifreamName
	 *            所在的ifream
	 * @return
	 */
	@RequestMapping("/listLog")
	public ModelAndView listOperateLog(String busType, Integer busId,String ifreamName) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/common/listLogs");
		if(!busType.equals(ConstantInterface.TYPE_SCHEDULE) && !busType.equals(ConstantInterface.TYPE_WORK_FLOW) 
				&& !busType.equals(ConstantInterface.TYPE_CONSUME)	&& !busType.equals(ConstantInterface.TYPE_OUTLINKMAN)
				&& !busType.equals(ConstantInterface.TYPE_DEMAND_PROCESS)){
			//查看日志，删除消息提醒
			todayWorksService.updateTodoWorkRead(busId,userInfo.getComId(), userInfo.getId(), busType,0);
		}
		if(busType.equals(ConstantInterface.TYPE_INSTITUTION) || busType.equals(ConstantInterface.TYPE_WORK_FLOW)){
			PageBean<Logs> pageBean = logsService.listPageLog(userInfo.getComId(), busId, busType);
			mav.addObject("pageBean", pageBean);
		}else{
			//日志记录
			PageBean<CommonLog> pageBean = systemLogService.listPageLog(userInfo.getComId(), busId, busType);
			mav.addObject("pageBean", pageBean);
		}
		
		return mav;
	}
	
	/**
	 * 验证用户角色
	 * @param busType 业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkUserRole")
	public Map<String,Object> checkUserRole(String busType) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info",CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Integer countSub = userInfo.getCountSub();
		map.put("status", "y");
		map.put("isLeader", countSub);
		if(null!=busType && !"".equals(busType)){
			Boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			map.put("isForceIn", isForceIn.toString());
		}
		return map;
	}
	/**
	 * 转向客户类型多选选页面
	 */
	@RequestMapping("/crmTypeMorePage")
	public ModelAndView crmTypeMorePage() {
		return new ModelAndView("/common/crmTypeMoreSelect/crmTypeMore").addObject("sessionUser",
				this.getSessionUser());
	}
	/**
	 * 转向客户类型多选选页面
	 */
	@RequestMapping("/crmTypeMoreTreePage")
	public ModelAndView crmTypeMoreTreePage() {
		return new ModelAndView("/common/crmTypeMoreSelect/crmTypeMoreTree").addObject("sessionUser",
				this.getSessionUser());
	}
	/**
	 * 获取客户类型集合
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listCrmType")
	public Map<String,Object> listCrmType() {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info",CommonConstant.OFF_LINE_INFO);
			return map;
		}
		List<CustomerType> list = crmService.listCustomerType(userInfo.getComId());
		map.put("list", list);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 模块的权限验证
	 * @param busId
	 * @param busType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/authorCheck")
	public Map<String,Object> authorCheck(Integer busId,String busType,Integer clockId,Integer baseId) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(CommonUtil.isNull(busType)){
			map.put("status", "f");
			map.put("info","验证类型不能空值！");
			return map;
		}
		UserInfo userInfo = this.getSessionUser();
		if(clockId==-1){
			clockId = null;
		}
		if(null == userInfo){
			map.put("status", "f");
			map.put("info",CommonConstant.OFF_LINE_INFO);
			return map;
		}
		if(busType.equals(ConstantInterface.TYPE_ITEM)){//项目查看权限验证
			Boolean authState = itemService.authorCheck(userInfo,busId);
			map.put("status", "y");
			map.put("authState", authState);
			
		}else if(busType.equals(ConstantInterface.TYPE_PRODUCT)){
			//产品查看权限验证
			Boolean authState = productService.authorCheck(userInfo,busId);
			map.put("status", "y");
			map.put("authState", authState);

		}else if(busType.equals(ConstantInterface.TYPE_CRM)){//客户查看权限验证
			Boolean authState = crmService.authorCheck(userInfo,busId);
			map.put("status", "y");
			map.put("authState", authState);
		}else if(busType.equals(ConstantInterface.TYPE_TASK)){//任务查看权限验证
			Boolean authState = taskService.authorCheck(userInfo,busId,clockId);
			map.put("status", "y");
			map.put("authState", authState);
		}else if(busType.equals(ConstantInterface.TYPE_WEEK)){//周报查看权限验证
			Boolean authState = weekReportService.authorCheck(userInfo,userInfo.getId(),clockId,busId);
			map.put("status", "y");
			map.put("authState", authState);
		}else if(busType.equals(ConstantInterface.TYPE_DAILY)){//分享查看权限验证
			Boolean authState = dailyService.authorCheck(userInfo,userInfo.getId(),clockId,busId);
			map.put("status", "y");
			map.put("authState", authState);
		}else if(busType.equals(ConstantInterface.TYPE_VOTE)){//投票查看权限验证
			Boolean authState = voteService.authorCheck(userInfo.getComId(),busId,userInfo.getId());
			map.put("status", "y");
			map.put("authState", authState);
		}else if(busType.equals(ConstantInterface.TYPE_FLOW_SP)){//审批查看权限验证
			if(null!=baseId && baseId>0){
				Boolean authState = workFlowService.authorBaseCheck(userInfo.getComId(),busId,userInfo.getId(),baseId);
				map.put("status", "y");
				map.put("authState", authState);
			}else{
				Boolean authState = workFlowService.authorCheck(userInfo.getComId(),busId,userInfo.getId());
				map.put("status", "y");
				map.put("authState", authState);
			}
		}else if(busType.equals(ConstantInterface.TYPE_DEMAND_PROCESS)){//需求的查看权限
			map.put("status", "y");
			map.put("authState", true);
		}else{
			map.put("status", "f");
			map.put("info","未验证该模块数据");
		}
		return map;
	}
	
	/**
	 * 办公用品选择
	 * @return
	 */
	@RequestMapping("/bgypMorePage")
	public ModelAndView bgypMorePage() {
		return new ModelAndView("/common/bgypSelect/bgypMore").addObject("sessionUser",
				this.getSessionUser());
	}
	/**
	 * 办公用品库存选择
	 * @return
	 */
	@RequestMapping("/bgypStoreMorePage")
	public ModelAndView bgypStoreMorePage() {
		return new ModelAndView("/common/bgypSelect/bgypStoreMore").addObject("sessionUser",
				this.getSessionUser());
	}
	/**
	 * 办公用品分类选择
	 * @return
	 */
	@RequestMapping("/bgflOnePage")
	public ModelAndView bgypOnePage() {
		return new ModelAndView("/common/bgypSelect/bgflOne").addObject("sessionUser",
				this.getSessionUser());
	}
	/**
	 * 跳转到字段选择,用于构建任务描述
	 * @return
	 */
	@RequestMapping("/selectDataForTask")
	public ModelAndView selectDataForTask() {
		return new ModelAndView("/common/selectDataForTask").addObject("sessionUser",this.getSessionUser());
	}
	
	
	/**
	 * 转向费用类型多选选页面
	 */
	@RequestMapping("/consumeTypeMorePage")
	public ModelAndView consumeTypeMorePage() {
		return new ModelAndView("/common/consumeTypeMoreSelect/consumeTypeMore").addObject("sessionUser",
				this.getSessionUser());
	}
	/**
	 * 转向费用类型多选选页面
	 */
	@RequestMapping("/consumeTypeMoreTreePage")
	public ModelAndView consumeTypeMoreTreePage() {
		return new ModelAndView("/common/consumeTypeMoreSelect/consumeTypeMoreTree").addObject("sessionUser",
				this.getSessionUser());
	}
	/**
	 * 获取费用类型集合
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listConsumeType")
	public Map<String,Object> listConsumeType() {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info",CommonConstant.OFF_LINE_INFO);
			return map;
		}
		List<ConsumeType> list = consumeService.listConsumeType(userInfo.getComId());
		map.put("list", list);
		map.put("status", "y");
		return map;
	}
}
