package com.westar.core.web.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.BusRemind;
import com.westar.base.model.Task;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.BusModService;
import com.westar.core.service.BusRemindService;
import com.westar.core.service.LogsService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
/**
 * 
 * 描述:事项催办功能
 * @author zzq
 * @date 2018年6月7日 上午11:13:56
 */
@Controller
@RequestMapping("/busRemind")
public class BusRemindController extends BaseController{

	@Autowired
	BusRemindService busRemindService;
	
	@Autowired
	BusModService busModService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	LogsService logsService;
	
	/**
	 * 分页查询催办记录信息
	 * @param busRemind
	 * @return
	 */
	@RequestMapping("/listPagedBusRemindForMod")
	public ModelAndView listPagedBusRemindForMod(BusRemind busRemind){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/bus_remind/listPagedBusRemindForMod");
		PageBean<BusRemind> pageBean = busRemindService.listPagedBusRemindForMod(busRemind,userInfo);
		view.addObject("userInfo", userInfo);
		view.addObject("pageBean", pageBean);
		return view;
	}
	
	/**
	 * 查询催办次数的记录
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param userId 被催办的人员
	 * @return
	 */
	@RequestMapping("/listPagedBusRemindForUserBus")
	public ModelAndView listPagedBusRemindForUserBus(BusRemind busRemind){
		UserInfo userInfo = this.getSessionUser();
		List<BusRemind> busRemindList = busRemindService.listPagedBusRemindForBus(userInfo,busRemind).getRecordList();
		
		ModelAndView view = new ModelAndView("/bus_remind/listBusRemindForUserBus");
		view.addObject("busRemindList", busRemindList);
		return view;
	}
	
	
	
	/**
	 * 事项催办页面
	 * @param busRemind
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/addBusRemindPage")
	public ModelAndView busRemindPage(BusRemind busRemind, String redirectPage){
		ModelAndView view = new ModelAndView("/bus_remind/addBusRemind");
		UserInfo userInfo = this.getSessionUser();
		//业务主键
		Integer busId = busRemind.getBusId();
		//业务类型
		String busType = busRemind.getBusType();

		Map<String,Object> map = busModService.queryReminderConf(userInfo,busId,busType);
		if(map.get("status").toString().equals("f")){
			this.setNotification(Notification.ERROR,map.get("info").toString());
			view = new ModelAndView("/refreshParent");
		}else{
			//模块模拟过程
			String busModName = (String) map.get("busModName");
			busRemind.setBusModName(busModName);
			view.addObject("listRemindUser", map.get("listRemindUser"));
			view.addObject("defMsg", map.get("defMsg"));
		}
		view.addObject("busRemind", busRemind);
		return view;
	}
	/**
	 * 事项批量催办页面
	 * @param busType
	 * @param ids
	 * @return
	 */
	@RequestMapping("/addBusRemindsPage")
	public ModelAndView addBusRemindsPage(String busType,String ids, String redirectPage){
		ModelAndView view = new ModelAndView("/bus_remind/addBusReminds");
		view.addObject("busType",busType);
		view.addObject("ids", ids);
		return view;
	}
	/**
	 * 新增事项催办
	 * @param busRemind
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/addBusRemind")
	public ModelAndView addbusRemind(BusRemind busRemind) throws Exception{
		ModelAndView view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		UserInfo userInfo = this.getSessionUser();
		//添加催办
		busRemindService.addBusRemind(busRemind,userInfo);
		this.setNotification(Notification.SUCCESS, "催办成功!");
		return view;
	}

	/**
	 * 新增事项批量催办
	 * @param busRemind
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addBusReminds")
	public ModelAndView addBusReminds(BusRemind busRemind,String ids) throws Exception{
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		if(!CommonUtil.isNull(ids)) {
			String[] insIds = ids.split(",");
			busRemindService.addBusReminds(busRemind,userInfo,insIds);
			this.setNotification(Notification.SUCCESS, "催办成功!");
		}else {
			this.setNotification(Notification.ERROR, "参数错误!");
		}
		return view;
	}
	
	/**
	 * 查看催办
	 * @param redirectPage
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewBusRemindPage")
	public ModelAndView viewBusRemindPage(String redirectPage,Integer id) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		
		BusRemind busRemind = busRemindService.queryBusRemindById(userInfo,id);
		
		todayWorksService.updateTodoWorkRead(busRemind.getId(), userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_REMINDER, 0);
		
		ModelAndView mav = new ModelAndView("/bus_remind/viewBusRemind");
		mav.addObject("busRemind", busRemind);
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
	/**
	 * 分页查询个人被催办记录
	 * @param busRemind
	 * @return
	 */
	@RequestMapping("/listPagedSelfBusRemind")
	public ModelAndView listPagedSelfBusRemind(BusRemind busRemind){
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		UserInfo userInfo = this.getSessionUser();
		
		PageBean<BusRemind> pageBean = busRemindService.listPagedSelfBusRemind(busRemind,userInfo);
		
		mav.addObject("userInfo", userInfo);
		mav.addObject("busRemind", busRemind);
		mav.addObject("list", pageBean.getRecordList());
		return mav;
	}
	
	
}