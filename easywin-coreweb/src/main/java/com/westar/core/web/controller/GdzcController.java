package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.GdzcMaintainRecord;
import com.westar.base.model.GdzcReduceRecord;
import com.westar.base.model.GdzcUpfile;
import com.westar.base.model.Gdzc;
import com.westar.base.model.GdzcType;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.GdzcService;
import com.westar.core.service.ModAdminService;

@Controller
@RequestMapping("/gdzc")
public class GdzcController extends BaseController{

	@Autowired
	GdzcService gdzcService;
	
	@Autowired
	ModAdminService modAdminService;
	/**
	 * 固定资产列表
	 * @param request
	 * @param gdzc
	 * @return
	 */
	@RequestMapping("/listGdzcPage")
	public ModelAndView listGdzcPage(HttpServletRequest request,Gdzc gdzc){
		ModelAndView view = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo userInfo = this.getSessionUser();
		
		List<Gdzc> listGdzc = gdzcService.listGdzc(gdzc,userInfo);
		view.addObject("listGdzc",listGdzc);
		
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_GDZC)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		List<GdzcType> listGdzcType = gdzcService.listGdzcType(userInfo.getComId(),ConstantInterface.TYPE_GDZC_SS);
		List<GdzcType> listAddGdzcType = gdzcService.listGdzcType(userInfo.getComId(),ConstantInterface.TYPE_GDZC_ADD);
		
		view.addObject("userInfo",userInfo);
		view.addObject("listGdzcType",listGdzcType);
		view.addObject("listAddType",listAddGdzcType);
		view.addObject("userInfo",userInfo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_GDZC);
		return view;
	}
	/**
	 * 获取指定类型的固定资产类型列表
	 * @param busType
	 * @return
	 */
	@RequestMapping("/listGdzcTypePage")
	public ModelAndView listGdzcTypePage(String busType){
		ModelAndView view = new ModelAndView("/zhbg/gdzc/listGdzcType");
		UserInfo userInfo = this.getSessionUser();
		List<GdzcType> listGdzcType = gdzcService.listGdzcType(userInfo.getComId(), busType);
		
		view.addObject("listGdzcType",listGdzcType);
		view.addObject("userInfo",userInfo);
		view.addObject("busType",busType);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_GDZC);
		return view;
	}
	/**
	 * 固定资产类型最大序号
	 * @param 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetGdzcTypeOrder")
	public Map<String, Object> ajaxGetGdzcTypeOrder(String busType) {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		// 获取最大序号
		Integer orderNum = gdzcService.queryGdzcTypeOrderMax(sessionUser.getComId(),busType);
		// 取出 排序号
		map.put("orderNum", orderNum);

		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}

	/**
	 * 添加固定资产类型
	 * @param gdzcType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddGdzcType")
	public Map<String, Object> ajaxAddGdzcType(GdzcType gdzcType) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		gdzcType.setComId(userInfo.getComId());
		gdzcType.setCreator(userInfo.getId());
		
		// 添加固定资产类型
		Integer id = gdzcService.addGdzcType(gdzcType, userInfo);

		gdzcType.setId(id);
		gdzcType.setOrderNum(gdzcType.getOrderNum() + 1);

		
		map.put("gdzcType", gdzcType);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;

	}
	/**
	 * 修改类型名称
	 * @param gdzcType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateGdzcTypeName")
	public String updateGdzcTypeName(GdzcType gdzcType)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		gdzcType.setComId(userInfo.getComId());
		boolean succ = gdzcService.updateGdzcTypeName(gdzcType, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	/**
	 * 修改类型序号
	 * @param gdzcType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateGdzcTypeOrder")
	public String updateGdzcTypeOrder(GdzcType gdzcType)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		gdzcType.setComId(userInfo.getComId());
		boolean succ = gdzcService.updateGdzcTypeOrder(gdzcType,userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	/**
	 * 删除类型
	 * @param gdzcType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delGdzcType")
	public GdzcType delGdzcType(GdzcType gdzcType)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		gdzcType.setComId(userInfo.getComId());
		boolean succ = gdzcService.delGdzcType(gdzcType, userInfo);
		gdzcType.setSucc(succ);
		if (succ) {
			gdzcType.setPromptMsg("删除成功");
		} else {
			gdzcType.setPromptMsg("该类型已使用,删除失败");
		}
		return gdzcType;
	}
	/***************************** 以上是固定资产类型配置 *****************************************/	
	/***************************** 以下是固定资产基本信息 *****************************************/
	/**
	 * 添加固定资产界面
	 * @return
	 */
	@RequestMapping("/addGdzcPage")
	public ModelAndView addGdzcPage(){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/zhbg/gdzc/addGdzc");
		List<GdzcType> listGdzcType = gdzcService.listGdzcType(userInfo.getComId(),ConstantInterface.TYPE_GDZC_SS);
		List<GdzcType> listAddGdzcType = gdzcService.listGdzcType(userInfo.getComId(),ConstantInterface.TYPE_GDZC_ADD);
		
		view.addObject("userInfo",userInfo);
		view.addObject("listGdzcType",listGdzcType);
		view.addObject("listAddGdzcType",listAddGdzcType);
		return view;
	}
	/**
	 * 添加固定资产
	 * @param gdzc
	 * @return
	 */
	@RequestMapping("/addGdzc")
	public ModelAndView addGdzc(Gdzc gdzc){
		UserInfo userInfo = this.getSessionUser();
		gdzcService.addGdzc(gdzc,userInfo);
		ModelAndView view = new ModelAndView("/refreshParent");
		this.setNotification(Notification.SUCCESS, "添加成功!");
		return view;
	}
	/**
	 * 修改固定资产基本信息
	 * @param gdzc
	 * @return
	 */
	@RequestMapping("/updateGdzc")
	public ModelAndView updateGdzc(Gdzc gdzc){
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		gdzcService.updateGdzc(gdzc,userInfo);
		this.setNotification(Notification.SUCCESS, "修改成功!");
		return view;
	}
	/**
	 * 删除固定资产
	 * @param gdzc
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delGdzc")
	public Map<String,Object> delGdzc(Gdzc gdzc){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		gdzc.setState(0);
		gdzcService.delGdzc(gdzc);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 查看固定资产详细信息界面
	 * @param gdzcId 固定资产主键
	 * @return
	 */
	@RequestMapping("/viewGdzcPage")
	public ModelAndView viewGdzcPage(Integer gdzcId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = null;
		//验证是否是当前模块管理人员
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_GDZC)){
			view = new ModelAndView("/zhbg/gdzc/editGdzc");
		}else{
			view = new ModelAndView("/zhbg/gdzc/viewGdzc");
		}
		//固定资产基本信息
		Gdzc gdzc = gdzcService.queryGdzcById(gdzcId, userInfo);
		view.addObject("gdzc", gdzc);
		//固定资产类型
		List<GdzcType> listGdzcType = gdzcService.listGdzcType(userInfo.getComId(),ConstantInterface.TYPE_GDZC_SS);
		List<GdzcType> listAddGdzcType = gdzcService.listGdzcType(userInfo.getComId(),ConstantInterface.TYPE_GDZC_ADD);
		view.addObject("listGdzcType",listGdzcType);
		view.addObject("listAddGdzcType",listAddGdzcType);
		return view;
	}
	
	/**
	 * 固定资产附件页面
	 * @param gdzcId
	 * @return
	 */
	@RequestMapping("/gdzcUpfilePage")
	public ModelAndView gdzcUpfilePage(Integer gdzcId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/zhbg/gdzc/listGdzcUpfile");
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_GDZC)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		List<GdzcUpfile> listGdzcUpfile = gdzcService.listGdzcUpfile(gdzcId, userInfo.getComId());
		view.addObject("listGdzcUpfile", listGdzcUpfile);
		return view;
	}
	/**
	 * 删除固定资产附件
	 * @param gdzcId 固定资产主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delGdzcUpfile")
	public Map<String, Object> delGdzcUpfile(Integer busId,Integer gdzcFileId,String busType){
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		try {
			gdzcService.delGdzcUpfile(gdzcFileId,userInfo,busId,busType);
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		} catch (Exception e) {
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
		}
		return map;
	}
	/***************************************维修记录**********************************/
	/**
	 * 固定资产维修记录列表
	 * @param gdzcId 固定资产主键
	 * @return
	 */
	@RequestMapping("/gdzcMaintainRecordPage")
	public ModelAndView gdzcMaintainRecordPage(Integer gdzcId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/gdzc/maintainRecord");
		//验证是否是当前模块管理人员
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_GDZC)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		List<GdzcMaintainRecord> listMaintain = gdzcService.listMaintainRecord(gdzcId, userInfo);
		view.addObject("listMaintain", listMaintain);
		return view;
	}
	/**
	 * 添加维修记录
	 * @param gdzcId 固定资产主键
	 * @return
	 */
	@RequestMapping("/addMaintainRecordPage")
	public ModelAndView addMaintainRecordPage(Integer gdzcId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/gdzc/addMaintainRecord");
		List<GdzcType> listWhType = gdzcService.listGdzcType(userInfo.getComId(),ConstantInterface.TYPE_GDZC_MAINTAIN);
		
		view.addObject("listWhType", listWhType);
		view.addObject("gdzcId", gdzcId);
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 添加固定资产维修记录
	 * @param gdzcMaintain
	 * @return
	 */
	@RequestMapping("/addMaintainRecord")
	public ModelAndView addMaintainRecord(GdzcMaintainRecord gdzcMaintain){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/refreshParent");
		gdzcService.addGdzcMaintainRecord(gdzcMaintain,userInfo);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		return view;
	}
	/**
	 * 修改维修记录界面
	 * @param gdzcId 固定资产主键
	 * @return
	 */
	@RequestMapping("/updateMaintainRecordPage")
	public ModelAndView updateMaintainRecordPage(Integer id){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/gdzc/editMaintainRecord");
		
		GdzcMaintainRecord maintainRecord = gdzcService.queryMaintainRecordById(id,userInfo);
		List<GdzcType> listWhType = gdzcService.listGdzcType(userInfo.getComId(),ConstantInterface.TYPE_GDZC_MAINTAIN);
		
		view.addObject("listWhType", listWhType);
		view.addObject("maintainRecord", maintainRecord);
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 修改固定资产维修记录
	 * @param gdzcMaintain
	 * @return
	 */
	@RequestMapping("/updateMaintainRecord")
	public ModelAndView updateMaintainRecord(GdzcMaintainRecord gdzcMaintain){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/refreshParent");
		gdzcService.updateMaintainRecord(gdzcMaintain,userInfo);
		this.setNotification(Notification.SUCCESS, "修改成功!");
		return view;
	}
	/**
	 * 删除维修记录
	 * @param insuranceRecord
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delGdzcMaintain")
	public Map<String,Object> delGdzcMaintain(GdzcMaintainRecord gdzcMaintainRecord){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		gdzcService.delGdzcMaintainRecord(gdzcMaintainRecord);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 固定资产维修完成
	 * @param gdzc
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/gdzcMaintainFinish")
	public Map<String,Object> gdzcMaintainFinish(Gdzc gdzc){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		gdzc.setState(1);
		gdzcService.gdzcMaintainFinish(gdzc,userInfo);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/***************************************维修记录**********************************/
	/***************************************减少记录**********************************/
	/**
	 * 固定资产减少记录界面
	 * @param gdzcId
	 * @return
	 */
	@RequestMapping("/gdzcReduceRecordPage")
	public ModelAndView gdzcReduceRecordPage(Integer gdzcId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/gdzc/reduceRecord");
		//验证是否是当前模块管理人员
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_GDZC)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		List<GdzcReduceRecord> listReduce = gdzcService.listGdzcReduceRecord(gdzcId, userInfo);
		view.addObject("listReduce", listReduce);
		return view;
	}
	/**
	 * 添加减少记录页面
	 * @param gdzcId
	 * @return
	 */
	@RequestMapping("/addReduceRecordPage")
	public ModelAndView addReduceRecordPage(Integer gdzcId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/gdzc/addReduceRecord");
		List<GdzcType> listReduceType = gdzcService.listGdzcType(userInfo.getComId(),ConstantInterface.TYPE_GDZC_REDUCE);
		
		view.addObject("listReduceType", listReduceType);
		view.addObject("gdzcId", gdzcId);
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 添加减少记录
	 * @param gdzcReduce
	 * @return
	 */
	@RequestMapping("/addReduceRecord")
	public ModelAndView addReduceRecord(GdzcReduceRecord gdzcReduce){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/refreshParent");
		gdzcService.addReduceRecord(gdzcReduce,userInfo);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		return view;
	}
	/**
	 * 修改减少记录界面
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateReduceRecordPage")
	public ModelAndView updateReduceRecordPage(Integer id){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/gdzc/editReduceRecord");
		
		GdzcReduceRecord reduceRecord = gdzcService.queryReduceRecordById(id,userInfo);
		List<GdzcType> listReduceType = gdzcService.listGdzcType(userInfo.getComId(),ConstantInterface.TYPE_GDZC_REDUCE);
		
		view.addObject("listReduceType", listReduceType);
		view.addObject("reduceRecord", reduceRecord);
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 修改减少记录
	 * @param gdzcReduce
	 * @return
	 */
	@RequestMapping("/updateReduceRecord")
	public ModelAndView updateReduceRecord(GdzcReduceRecord gdzcReduce){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/refreshParent");
		gdzcService.updateReduceRecord(gdzcReduce,userInfo);
		this.setNotification(Notification.SUCCESS, "修改成功!");
		return view;
	}
	/**
	 * 删除记录
	 * @param gdzcReduce
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delReduceRecord")
	public Map<String,Object> delReduceRecord(GdzcReduceRecord gdzcReduce){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		gdzcService.delReduceRecord(gdzcReduce);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/***************************************减少记录**********************************/
}