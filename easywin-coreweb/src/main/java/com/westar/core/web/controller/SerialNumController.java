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
import com.westar.base.model.SerialNum;
import com.westar.base.model.SpSerialNumRel;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.SerialNumService;

/**
 * 序列编号
 * @author zzq
 *
 */
@Controller
@RequestMapping("/serialNum")
public class SerialNumController extends BaseController{

	@Autowired
	SerialNumService serialNumService;
	
	/**
	 * 分页查询序列编号
	 * @param serialNum 序列编号的查询条件
	 * @return
	 */
	@RequestMapping("/listPagedSerialNum")
	public ModelAndView listPagedSerialNum(SerialNum serialNum){
		
		ModelAndView view = new ModelAndView("/adminCfg/adminCfg_center");
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限
			view = new ModelAndView("/index");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return view;
		}
		view.addObject("toPage","../serialNum/listSerialNum.jsp");
		
		
		view.addObject("userInfo",userInfo);
		
		//当前操作人员
		List<SerialNum> serialNums = serialNumService.listPagedSerialNum(userInfo,serialNum);
		view.addObject("userInfo", userInfo);
		view.addObject("serialNums", serialNums);
		return view;
	}
	/**
	 * 查询用于选择的序列编号
	 * @param serialNum
	 * @return
	 */
	@RequestMapping("/listPagedSerialNumSelect")
	public ModelAndView listPagedSerialNumSelect(SerialNum serialNum){
		ModelAndView mav = new ModelAndView("/serialNum/listPagedSerialNumSelect");
		UserInfo sessionUser = this.getSessionUser();
		List<SerialNum> serialNums = serialNumService.listPagedSerialNumSelect(sessionUser,serialNum);
		mav.addObject("userInfo", sessionUser);
		mav.addObject("serialNums", serialNums);
		return mav;
	}
	
	/**
	 * 跳转到序列编号添加界面
	 * @return
	 */
	@RequestMapping("/addSerialNumPage")
	public ModelAndView addSerialNumPage(){
		ModelAndView mav = new ModelAndView("/serialNum/addSerialNum");
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	/**
	 * 添加序列编号
	 * @param serialNum 序列编号信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addSerialNum")
	public Map<String, Object>  addSerialNum(SerialNum serialNum){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		Integer serialNumId = serialNumService.addSerialNum(sessionUser,serialNum);
		map.put("state", "y");
		
		if(!CommonUtil.isNull(serialNumId)){//添加普通闹铃提示
			this.setNotification(Notification.SUCCESS, "新增成功!");
		}
		return map;
		
	}
	
	/**
	 * 查看自动编号规则
	 * @param serialNumKey 规则主键
	 * @return
	 */
	@RequestMapping("/viewSerialNumPage")
	public ModelAndView viewSerialNumPage(Integer serialNumKey){
		ModelAndView mav = new ModelAndView("/serialNum/serialNumInfo");
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("userInfo", sessionUser);
		SerialNum serialNum = serialNumService.querySerialNum(serialNumKey);
		mav.addObject("serialNum", serialNum);
		return mav;
	}
	
	/**
	 * 更新自动编号规则
	 * @param serialNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSerialNum")
	public Map<String, Object>  updateSerialNum(SerialNum serialNum){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		serialNumService.updateSerialNum(sessionUser,serialNum);
		map.put("state", "y");
		this.setNotification(Notification.SUCCESS, "更新成功!");
		return map;
		
	}
	
	/**
	 * 删除自动编号规则
	 * @param serialNumKey
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delSerialNum")
	public Map<String, Object>  delSerialNum(Integer serialNumKey){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		serialNumService.delSerialNum(sessionUser,serialNumKey);
		map.put("state", "y");
		this.setNotification(Notification.SUCCESS, "删除成功!");
		return map;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 构建序列编号
	 * @param serialNumId
	 * @param busId
	 * @param busType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/constrSerialNum")
	public Map<String,Object> constrSerialNum(Integer serialNumId,Integer busId,String busType){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Map<String,Object> result = serialNumService.constrSerialNum(sessionUser,serialNumId,busId,busType);
		if(result == null){
			map.put("status", "f1");
			map.put("info", "请先维护序编号!");
		}
		map.put("status", "y");
		map.putAll(result);
		return map;
		
	}
	
	/**
	 * 验证
	 * @param serialNumId
	 * @param serialNum
	 * @param busId
	 * @param busType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkSerialNum")
	public Map<String,Object> checkSerialNum(Integer serialNumId,String serialNum,Integer busId,String busType){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		Integer countSpSerialNum = serialNumService.querySpSerialNumRelForCheck(serialNumId,serialNum,busId,busType);
		if(countSpSerialNum>0){
			map.put("status", "f");
			map.put("info", "序列编号'"+serialNum+"'已被占用,请重新填写!");
			return map;
		}else{
			map.put("status", "y");
			return map;
		}
		
		
	}
	
}