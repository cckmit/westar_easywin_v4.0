package com.westar.core.web.controller;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.Clock;
import com.westar.base.model.ClockPerson;
import com.westar.base.model.DataDic;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.ClockService;
import com.westar.core.web.DataDicContext;

@Controller
@RequestMapping("/clock")
public class ClockController extends BaseController{

	@Autowired
	ClockService clockService;
	
	@RequestMapping("/listPagedClock")
	public ModelAndView listPagedClock(Clock clock,String[] modTypes){
		ModelAndView view = new ModelAndView("/clock/listPagedClock");
		//当前操作员
		UserInfo userInfo = this.getSessionUser();
		//操作员主键
		clock.setUserId(userInfo.getId());
		//操作员所在企业
		clock.setComId(userInfo.getComId());
		
		//模块数组化
		List<String> modList = null;
		if(null!=modTypes && modTypes.length>0){
			Arrays.sort(modTypes);
			modList = Arrays.asList(modTypes);
		}
		//分页查询闹铃
		List<Clock> listClocks = clockService.listPagedClock(clock,modList);
		
		view.addObject("userInfo", userInfo);
		view.addObject("listClocks", listClocks);
		view.addObject("modList", modList);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_CLOCK);
		return view;
	}
	/**
	 * 跳转添加定时提醒
	 * @param clock
	 * @return
	 */
	@RequestMapping("/ajaxAddClockPage")
	public ModelAndView ajaxAddClockPage(Clock clock){
		ModelAndView view = new ModelAndView("/clock/ajaxAddClock");
		view.addObject("clock", clock);
		view.addObject("nowDate", DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd));
		
		view.addObject("dayMonth", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		view.addObject("dayWeek", DateTimeUtil.getDay());
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		String nowTime = dateFormat.format(date);
		view.addObject("nowTime", nowTime);
		
		List<DataDic> listClockWay=DataDicContext.getInstance().listTreeDataDicByType("clockWay");
		view.addObject("listClockWay", listClockWay);
		view.addObject("userInfo", this.getSessionUser());
		return view;
	}
	/**
	 * 保存定时提醒
	 * @param clock 提醒信息
	 * @param sendWays 发送方式
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxAddClock")
	public Map<String, Object> ajaxAddClock(Clock clock,String[] sendWays,Integer[] users){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		Integer id = clockService.addClock(clock,userInfo,sendWays,users);
		map.put("state", "y");
		map.put("id", id);
		
		if(null!=clock.getBusId() && clock.getBusId()==0){//添加普通闹铃提示
			this.setNotification(Notification.SUCCESS, "操作成功!");
		}
		return map;
	}
	/**
	 * 删除定时提醒
	 * @param id
	 * @param busId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxDelClock")
	public Map<String, Object> ajaxDelClock(Integer id,Integer busId,String srcType){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		clockService.delClock(id,userInfo);
		map.put("state", "y");
		
		if(null!=busId && busId==0){//普通闹铃
			this.setNotification(Notification.SUCCESS, "操作成功!");
		}else if("0".equals(srcType)){//来源页面为闹铃列表页面
			this.setNotification(Notification.SUCCESS, "操作成功!");
		}
		
		return map;
	}
	/**
	 * 批量删除闹铃
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping(value="/delClock")
	public ModelAndView delClock(Integer[] ids,String redirectPage){
		ModelAndView view = new ModelAndView("redirect:"+redirectPage);
		UserInfo userInfo = this.getSessionUser();
		//批量删除闹铃
		clockService.delClocks(ids,userInfo);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		return view;
	}
	/**
	 * 跳转编辑定时提醒
	 * @param id 闹铃主键
	 * @param srcType 来源页面类型 0 闹铃列表页面 1模块详情页面
	 * @return
	 */
	@RequestMapping("/ajaxUpdateClockPage")
	public ModelAndView ajaxUpdateClockPage(Integer id,String srcType){
		ModelAndView view = new ModelAndView("/clock/ajaxUpdateClock");
		UserInfo userInfo = this.getSessionUser();
		//根据clock主键查询详情
		Clock clock = clockService.getClockById(id,userInfo);
		view.addObject("clock", clock);
		
		//不是周且不是月
		if(!clock.getClockRepType().equals("2") && !clock.getClockRepType().equals("3")){
			clock.setClockRepDate("");
		}
		
		view.addObject("dayMonth", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		view.addObject("dayWeek", DateTimeUtil.getDay());
		
		view.addObject("srcType",srcType);
		//闹铃对象
		List<ClockPerson> persons = clockService.listClockPerson(userInfo.getComId(),id);
		view.addObject("persons",persons);
		
		view.addObject("userInfo",userInfo);
		return view;
	}
	
	/**
	 * 修改定时提醒
	 * @param clock 提醒信息
	 * @param sendWays 发送方式
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxUpdateClock")
	public Map<String, Object> ajaxUpdateClock(Clock clock,String[] sendWays,String srcType,Integer[] users){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		//操作员主键
		clock.setUserId(userInfo.getId());
		//操作员所在企业
		clock.setComId(userInfo.getComId());
		
		Integer id = clockService.updateClock(clock,userInfo,sendWays,users);
		map.put("state", "y");
		
		if(null!=clock.getBusId() && clock.getBusId()==0){//普通闹铃
			this.setNotification(Notification.SUCCESS, "操作成功!");
		}else if("0".equals(srcType)){//来源页面为闹铃列表页面
			this.setNotification(Notification.SUCCESS, "操作成功!");
		}
		
		map.put("id", id);
		return map;
	}
	/**
	 * 取得模块的闹铃信息
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxListClockForOne")
	public Map<String, Object> ajaxListClockForOne(String busType,Integer busId){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null!=userInfo){
			//客户的闹铃集合
			List<Clock> listClocks = clockService.listClockForOne(userInfo,busId,busType);
			map.put("listClocks", listClocks);
		}
		return map;
	}
	
}