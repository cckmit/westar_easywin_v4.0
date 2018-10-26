package com.westar.core.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.Announcement;
import com.westar.base.model.Attention;
import com.westar.base.model.Customer;
import com.westar.base.model.FileDetail;
import com.westar.base.model.MenuHome;
import com.westar.base.model.MsgShare;
import com.westar.base.model.Question;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.Task;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.Vote;
import com.westar.base.pojo.IndexVo;
import com.westar.base.pojo.ModStaticVo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.AnnouncementService;
import com.westar.core.service.AttentionService;
import com.westar.core.service.CrmService;
import com.westar.core.service.FileCenterService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.ItemService;
import com.westar.core.service.MenuService;
import com.westar.core.service.ModOptConfService;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.QasService;
import com.westar.core.service.TaskService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.VoteService;
import com.westar.core.web.FreshManager;

@Controller
public class HomePageController extends BaseController {

	@Autowired
	MenuService menuService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	MsgShareService msgShareService;
	
	@Autowired
	AnnouncementService announcementService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	CrmService crmService;
	
	@Autowired
	AttentionService attenService;
	
	
	@Autowired
	ModOptConfService modOptConfService;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	QasService qasService;
	
	@Autowired
	VoteService voteService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@RequestMapping("/index")
	public ModelAndView index1(HttpServletRequest request) throws ParseException {
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		ModelAndView view = new ModelAndView("/index");
		UserInfo userInfo = this.getSessionUser();
		//页面的个人信息
		view.addObject("userInfo",userInfo);
		
	    String sid = this.getSid();
	    //主页面显示模块集合
	    ArrayList<Object> listIndexVo = this.getListIndexVo(sid,userInfo);
		view.addObject("listIndexVo",listIndexVo);
		
		//总待办数
		Integer todoNums = todayWorksService.countTodo(userInfo.getComId(),userInfo.getId());
		view.addObject("todoNums", todoNums);
		
		//总关注未读消息数
		Integer attenNums = todayWorksService.countAttenNoRead(userInfo.getComId(),userInfo.getId());
		view.addObject("attenNums", attenNums);
		
		//预期待办任务
		Integer overdueNums = taskService.overdueTaskNum(userInfo);
		view.addObject("overdueNums", overdueNums);
		
		//主页通讯录筛选
		//团队所有人
		List<UserInfo> listUserOfTeam = userInfoService.listUser(userInfo);
		view.addObject("listUserOfTeam",listUserOfTeam);
		
		//页面标识
		view.addObject("page","index");
		
		//头文件的显示
		view.addObject("homeFlag","1");
		
		//上次使用的分组
		List<UsedGroup> usedGroups = userInfoService.listUsedGroup(userInfo.getComId(),userInfo.getId());
		//个人组群集合
		List<SelfGroup> listSelfGroup = userInfoService.listSelfGroup(userInfo.getComId(),userInfo.getId());
		//最近一次使用的分组的类型，分组名称以及自定义所有的分组
		Map<String, String> grpMap = CommonUtil.usedGrpJson(usedGroups,listSelfGroup);
		//最近一次使用的分组的类型
		view.addObject("idType", grpMap.get("idType"));
		//分组名称
		view.addObject("scopeTypeSel", grpMap.get("scopeTypeSel"));
		//自定义所有的分组
		view.addObject("selfGroupStr", grpMap.get("selfGroupStr"));
		
		MsgShare msgShare = new MsgShare();
		msgShare.setComId(userInfo.getComId());
		//个人分享信息集合
		List<MsgShare> list = msgShareService.listMsgShare(userInfo.getComId(),userInfo.getId(),10,msgShare,null,null);
		view.addObject("userInfo", userInfo);
		view.addObject("listMsgShare", list);
		
		//个人分享信息数
		Integer shareNum = msgShareService.getCountMsg(userInfo.getComId(),userInfo.getId(),msgShare,null);
		view.addObject("shareNum", shareNum);
		return view;
	}
	
	/**
	 * 取得首页栏目展示数据
	 * @param sid session标识
	 * @param userInfo 当前操作人员
	 * @return
	 */
	private ArrayList<Object> getListIndexVo(String sid, UserInfo userInfo) {
		ArrayList<Object> listIndexVo = new ArrayList<Object>();
		//取得首页需要展示的模块
  		List<MenuHome> listMenuHome = menuService.listMenuHome(userInfo.getComId(),userInfo.getId(),userInfo.getCountSub());
  		if(null != listMenuHome && !listMenuHome.isEmpty()){
  			for (MenuHome menuHome : listMenuHome) {
  				if("0".equals(menuHome.getOpenState())){
  					continue;
  				}
  				//模块类型
  				String busType = menuHome.getBusType();
  				@SuppressWarnings("rawtypes")
				IndexVo indexVo = null;
  				
  				if(busType.equals(ConstantInterface.TYPE_WORK)){
  					//排在待办事项列表前五条数据集合
  					indexVo = new IndexVo<List<TodayWorks>>();
  				}else if(busType.equals(ConstantInterface.TYPE_CRM)){
  					//验证当前登录人是否是客户中心督察人员
  					indexVo = new IndexVo<List<Customer>>();
  				}else if(busType.equals(ConstantInterface.TYPE_ATTEN)){
  					//关注更新
  					indexVo = new IndexVo<List<Attention>>();
  				}else if(busType.equals(ConstantInterface.TYPE_FILE)){
  					//最新文件
  					indexVo = new IndexVo<List<FileDetail>>();
  				}else if(busType.equals(ConstantInterface.TYPE_QUES)){
  					//最新问答
  					indexVo = new IndexVo<List<Question>>();
  				}else if(busType.equals(ConstantInterface.TYPE_VOTE)){
  					//最新投票
  					indexVo = new IndexVo<List<Vote>>();
  				}else if(busType.equals(ConstantInterface.TYPE_SUBTASK)){
  					//下属任务统计
  					indexVo = new IndexVo<List<Task>>();
  				}else if(busType.equals(ConstantInterface.TYPE_ANNOUNCEMENT)){
  					//公告
  					indexVo = new IndexVo<List<Announcement>>();
  				}
  				
  				//标题
  				String title = menuHome.getTitle();
  				//查看更多的url
  				String actionMore = menuHome.getActionMore();
  				actionMore = actionMore + (actionMore.indexOf("?")>0?"&sid=":"?sid=")+sid;
  				//添加url
  				String  actionAdd = menuHome.getActionAdd();
  				if(null!=actionAdd && !"".equals(actionAdd)){
  					actionAdd = actionAdd + (actionAdd.indexOf("?")>0?"&sid=":"?sid=")+sid;
  				}
  				//添加模块
  				String addName = menuHome.getAddName();
  				indexVo.setBusType(busType);
				indexVo.setBusName(title);
				indexVo.setUrl(actionMore);
				indexVo.setAddUrl(actionAdd);
				indexVo.setAddName(addName);
				indexVo.setWidth(menuHome.getWidth());
				listIndexVo.add(indexVo);
  				
  			}
  		}
		return listIndexVo;
	}
	@RequestMapping("/head")
	public ModelAndView head() {
		return new ModelAndView("/head");
	}
	@RequestMapping("/body")
	public ModelAndView body() {
		return new ModelAndView("/body");
	}
	@RequestMapping("/bodyLeft")
	public ModelAndView bodyLeft() {
		return new ModelAndView("/body_left");
	}

	@RequestMapping("/blank")
	public ModelAndView blank() {
		return new ModelAndView("redirect:/blank.jsp");
	}
	/**
	 * 查询栏目信息
	 * @param busType 栏目类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loadIndexLanmu")
	public Map<String,Object> loadIndexLanmu(String busType) {
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo  = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			return map;
		}
		if(busType.equals(ConstantInterface.TYPE_WORK)){//待办
			//排在待办事项列表前五条数据集合
			List<TodayWorks> workList = todayWorksService.firstNWorkList(userInfo,5);
			map.put("list", workList);
		}else if(busType.equals(ConstantInterface.TYPE_ATTEN)){//关注
			//关注更新
			List<Attention> attenList =attenService.firstNAttenList(userInfo, 5);
			map.put("list", attenList);
		}else if(busType.equals(ConstantInterface.TYPE_CRM)){//客户
			//验证当前登录人是否是督察人员
			boolean isForceIn = forceInService.isForceInPersion(userInfo, busType);
			List<Customer> crmList = crmService.firstNCustomerList(userInfo,isForceIn,5);
			map.put("list", crmList);
		}else if(busType.equals(ConstantInterface.TYPE_FILE)){//附件
			//最新文件
			List<FileDetail> fileList = fileCenterService.firstNFileList(new FileDetail(),5, userInfo);
			map.put("list", fileList);
		}else if(busType.equals(ConstantInterface.TYPE_VOTE)){//投票
			//最新投票
			List<Vote> voteList = voteService.firstNVoteList(userInfo,6);
			map.put("list", voteList);
		}else if(busType.equals(ConstantInterface.TYPE_QUES)){//问答
			//最新问答
			List<Question> qasList = qasService.firstNQasList(userInfo,6);
			map.put("list", qasList);
		}else if(busType.equals(ConstantInterface.TYPE_SUBTASK)){//下属逾期任务
			
			//下属逾期任务统计
			List<ModStaticVo> result = taskService.listSubTaskOver(userInfo);
			
			map.put("status", "y");
			map.put("listBusReport", result);
		}else if(busType.equals(ConstantInterface.TYPE_ANNOUNCEMENT)){//公告
			//下属逾期任务统计
			List<Announcement> result =  announcementService.firstNAnnoun(userInfo, 6);
			map.put("status", "y");
			map.put("list", result);
		}
		
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 首页待办点击后补充数据
	 * @param todoIds 页面点击前的待办主键
	 * @param leftNum 页面点击后剩下的数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loadIndexMoreTodo")
	public Map<String,Object> loadIndexMoreTodo(String todoIds,Integer leftNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo  = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			return map;
		}
		//排在待办事项列表前五条数据集合
		List<TodayWorks> workList = todayWorksService.otherNWorkList(userInfo,5,todoIds,leftNum);
		map.put("list", workList);
		map.put("status", "y");
		return map;
	}
	/**
	 * 查询常用人员
	 * @param anyNameLike
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loadIndexUser")
	public Map<String,Object> loadIndexUser(String anyNameLike) {
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo  = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			return map;
		}
		if(null==anyNameLike || "".equals(anyNameLike.trim())){
			//查询当前操作人的最近联系人前10
			List<UserInfo> listRecentlyUser = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),10);
			map.put("list", listRecentlyUser);
		}else{
			userInfo.setAnyNameLike(anyNameLike);
			List<UserInfo> listRecentlyUser = userInfoService.listUser(userInfo);
			map.put("list", listRecentlyUser);
		}
		map.put("status", "y");
		return map;
	}

}
