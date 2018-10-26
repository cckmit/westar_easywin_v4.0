package com.westar.core.web.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.westar.base.model.Car;
import com.westar.base.model.CarMaintainRecord;
import com.westar.base.model.CarScopeDep;
import com.westar.base.model.CarScopeUser;
import com.westar.base.model.CarType;
import com.westar.base.model.CarUpfile;
import com.westar.base.model.CarUseRecord;
import com.westar.base.model.InsuranceRecord;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.CarApplyByDate;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.CarService;
import com.westar.core.service.CarUpfileService;
import com.westar.core.service.ModAdminService;

@Controller
@RequestMapping("/car")
public class CarController extends BaseController{

	@Autowired
	CarService carService;
	
	@Autowired
	ModAdminService modAdminService;
	
	@Autowired
	CarUpfileService carUpfileService;
	/**
	 * 申请车辆列表
	 * @param request
	 * @param car
	 * @return
	 */
	@RequestMapping("/listCarApplyOfMinPage")
	public ModelAndView listCarApplyOfMinPage(HttpServletRequest request,CarUseRecord applyRecord){
		ModelAndView view = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo userInfo = this.getSessionUser();
		
		List<CarUseRecord> listCarApply = carService.listCarApplyOfMin(applyRecord,userInfo);
		view.addObject("listCarApply",listCarApply);
		
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_CLGL)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		view.addObject("userInfo",userInfo);
		view.addObject("applyRecord",applyRecord);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_ZHBG);
		return view;
	}
	/**
	 * 待批的车辆使用申请
	 * @param request
	 * @param car
	 * @return
	 */
	@RequestMapping("/listCarApplyToDoPage")
	public ModelAndView listCarApplyToDoPage(HttpServletRequest request,CarUseRecord applyRecord){
		ModelAndView view = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo userInfo = this.getSessionUser();
		
		List<CarUseRecord> listCarApply = carService.listCarApplyToDo(applyRecord,userInfo);
		view.addObject("listCarApply",listCarApply);
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_CLGL)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		view.addObject("userInfo",userInfo);
		view.addObject("applyRecord",applyRecord);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_ZHBG);
		return view;
	}
	/**
	 * 正在使用的车辆
	 * @param request
	 * @param car
	 * @return
	 */
	@RequestMapping("/listCarOfUsingPage")
	public ModelAndView listCarOfUsingPage(HttpServletRequest request,Car car){
		ModelAndView view = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo userInfo = this.getSessionUser();
		
		List<Car> listCar = carService.listCarOfUsing(car,userInfo);
		view.addObject("listCar",listCar);
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_CLGL)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		view.addObject("userInfo",userInfo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_ZHBG);
		return view;
	}
	/**
	 * 所有车辆
	 * @param request
	 * @param car
	 * @return
	 */
	@RequestMapping("/listCarOfAllPage")
	public ModelAndView listCarOfAllPage(HttpServletRequest request,Car car){
		ModelAndView view = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo userInfo = this.getSessionUser();
		boolean isModAdmin  = false;
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_CLGL)){
			isModAdmin = true;
		}else{
			isModAdmin  = false;
		}
		view.addObject("isModAdmin", isModAdmin);
		List<Car> listCar = carService.listCarOfAll(car,userInfo,isModAdmin);
		view.addObject("listCar",listCar);
		
		
		view.addObject("userInfo",userInfo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_ZHBG);
		return view;
	}
	/***************************** 以下是车辆类型配置 *****************************************/
	/**
	 * 车辆类型配置界面
	 * @return
	 */
	@RequestMapping("/listCarTypePage")
	public ModelAndView listCarTypePage(){
		ModelAndView view = new ModelAndView("/zhbg/car/listCarType");
		UserInfo userInfo = this.getSessionUser();
		List<CarType> listCarType = carService.listCarType(userInfo.getComId());
		
		view.addObject("listCarType",listCarType);
		view.addObject("userInfo",userInfo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_ZHBG);
		return view;
	}
	/**
	 * 车辆类型最大序号
	 * @param 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetCarTypeOrder")
	public Map<String, Object> ajaxGetCarTypeOrder() {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		// 获取最大序号
		Integer orderNum = carService.queryCarTypeOrderMax(sessionUser.getComId());
		// 取出 排序号
		map.put("orderNum", orderNum);

		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}

	/**
	 * 添加车辆类型
	 * @param carType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddCarType")
	public Map<String, Object> ajaxAddCarType(CarType carType) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		carType.setComId(userInfo.getComId());
		carType.setCreator(userInfo.getId());
		
		// 添加车辆类型
		Integer id = carService.addCarType(carType, userInfo);

		carType.setId(id);
		carType.setOrderNum(carType.getOrderNum() + 1);

		
		map.put("carType", carType);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;

	}
	/**
	 * 修改类型名称
	 * @param carType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCarTypeName")
	public String updateCarTypeName(CarType carType)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		carType.setComId(userInfo.getComId());
		boolean succ = carService.updateCarTypeName(carType, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	/**
	 * 修改类型序号
	 * @param carType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateCarTypeOrder")
	public String updateCarTypeOrder(CarType carType)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		carType.setComId(userInfo.getComId());
		boolean succ = carService.updateCarTypeOrder(carType,userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	/**
	 * 删除类型
	 * @param carType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delCarType")
	public CarType delCarType(CarType carType)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		carType.setComId(userInfo.getComId());
		boolean succ = carService.delCarType(carType, userInfo);
		carType.setSucc(succ);
		if (succ) {
			carType.setPromptMsg("删除成功");
		} else {
			carType.setPromptMsg("该类型已使用,删除失败");
		}
		return carType;
	}
	/***************************** 以上是车辆类型配置 *****************************************/	
	/***************************** 以下是车辆基本信息 *****************************************/
	/**
	 * 添加车辆界面
	 * @return
	 */
	@RequestMapping("/addCarPage")
	public ModelAndView addCarPage(){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/zhbg/car/addCar");
		List<CarType> listCarType = carService.listCarType(userInfo.getComId());
		
		view.addObject("userInfo",userInfo);
		view.addObject("listCarType",listCarType);
		return view;
	}
	/**
	 * 添加车辆
	 * @param car
	 * @return
	 */
	@RequestMapping("/addCar")
	public ModelAndView addCar(Car car){
		UserInfo userInfo = this.getSessionUser();
		carService.addCar(car,userInfo);
		ModelAndView view = new ModelAndView("/refreshParent");
		this.setNotification(Notification.SUCCESS, "添加成功!");
		return view;
	}
	/**
	 * 修改车辆基本信息
	 * @param car
	 * @return
	 */
	@RequestMapping("/updateCar")
	public ModelAndView updateCar(Car car){
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		carService.updateCar(car,userInfo);
		this.setNotification(Notification.SUCCESS, "修改成功!");
		return view;
	}
	/**
	 * 查看车辆详细信息界面
	 * @param carId 车辆主键
	 * @return
	 */
	@RequestMapping("/viewCarPage")
	public ModelAndView viewCarPage(Integer carId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = null;
		//验证是否是当前模块管理人员
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_CLGL)){
			view = new ModelAndView("/zhbg/car/editCar");
			//车辆申请人员范围
			List<CarScopeUser> listScopeUser= carService.listCarScopeUser(carId,userInfo);
			view.addObject("listScopeUser", listScopeUser);
			//车辆申请部门范围
			List<CarScopeDep> listScopeDep= carService.listCarScopeDep(carId,userInfo);
			view.addObject("listScopeDep", listScopeDep);
		}else{
			view = new ModelAndView("/zhbg/car/viewCar");
		}
		//车辆基本信息
		Car car = carService.queryCarById(carId, userInfo);
		view.addObject("car", car);
		//车辆类型
		List<CarType> listCarType = carService.listCarType(userInfo.getComId());
		view.addObject("listCarType",listCarType);
		
		
		return view;
	}
	/**
	 * 车辆的保险列表
	 * @param carId 车辆主键
	 * @return
	 */
	@RequestMapping("/insuranceRecordPage")
	public ModelAndView insuranceRecordPage(Integer carId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/car/insuranceRecord");
		//验证是否是当前模块管理人员
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_CLGL)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		//强险记录
		List<InsuranceRecord> listQxRecord = carService.listQxRecord(carId,userInfo);
		view.addObject("listQxRecord", listQxRecord);
		//商业险记录
		List<InsuranceRecord> listSyxRecord = carService.listSyxRecord(carId,userInfo);
		view.addObject("listSyxRecord", listSyxRecord);
		return view;
	}
	/**
	 * 修改保险记录
	 * @param insuranceRecord
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateInsurance")
	public Map<String,Object> updateInsurance(InsuranceRecord insuranceRecord){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		carService.updateInsuranceRecord(insuranceRecord);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 删除保险记录
	 * @param insuranceRecord
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delInsurance")
	public Map<String,Object> delInsurance(InsuranceRecord insuranceRecord){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		try {
			carService.delInsuranceRecord(insuranceRecord, userInfo);
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		} catch (Exception e) {
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
		}
		return map;
	}
	/**
	 * 车辆的附件列表
	 * @param carId 车辆主键
	 * @return
	 */
	@RequestMapping("/carUpfilePage")
	public ModelAndView carUpfilePage(Integer carId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/car/listCarUpfile");
		//验证是否是当前模块管理人员
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_CLGL)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		List<CarUpfile> listCarUpfile = carUpfileService.listCarUpfileByCar(carId, userInfo.getComId());
		view.addObject("listCarUpfile", listCarUpfile);
		return view;
	}
	/**
	 * 删除车辆附件
	 * @param carId 车辆主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delCarUpfile")
	public Map<String, Object> delCarUpfile(Integer busId,Integer carFileId,String busType){
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		try {
			carUpfileService.delCarUpfile(carFileId,userInfo,busId,busType);
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		} catch (Exception e) {
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
		}
		return map;
	}
	/**
	 * 车辆维修记录列表
	 * @param carId 车辆主键
	 * @return
	 */
	@RequestMapping("/carMaintainRecordPage")
	public ModelAndView carMaintainRecordPage(Integer carId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/car/maintainRecord");
		//验证是否是当前模块管理人员
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_CLGL)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		List<CarMaintainRecord> listMaintain = carService.listMaintainRecord(carId, userInfo);
		view.addObject("listMaintain", listMaintain);
		return view;
	}
	/**
	 * 添加维修记录
	 * @param carId 车辆主键
	 * @return
	 */
	@RequestMapping("/addMaintainRecordPage")
	public ModelAndView addMaintainRecordPage(Integer carId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/car/addMaintainRecord");
		view.addObject("carId", carId);
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 添加车辆维修记录
	 * @param carMaintain
	 * @return
	 */
	@RequestMapping("/addMaintainRecord")
	public ModelAndView addMaintainRecord(CarMaintainRecord carMaintain){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/refreshParent");
		carService.addCarMaintainRecord(carMaintain,userInfo);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		return view;
	}
	/**
	 * 修改维修记录界面
	 * @param carId 车辆主键
	 * @return
	 */
	@RequestMapping("/updateMaintainRecordPage")
	public ModelAndView updateMaintainRecordPage(Integer id){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/car/editMaintainRecord");
		
		CarMaintainRecord maintainRecord = carService.queryMaintainRecordById(id,userInfo);
		view.addObject("maintainRecord", maintainRecord);
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 修改车辆维修记录
	 * @param carMaintain
	 * @return
	 */
	@RequestMapping("/updateMaintainRecord")
	public ModelAndView updateMaintainRecord(CarMaintainRecord carMaintain){
		ModelAndView view = new ModelAndView("/refreshParent");
		carService.updateMaintainRecord(carMaintain);
		this.setNotification(Notification.SUCCESS, "修改成功!");
		return view;
	}
	/**
	 * 删除维修记录
	 * @param insuranceRecord
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delCarMaintain")
	public Map<String,Object> delCarMaintain(CarMaintainRecord carMaintainRecord){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		carService.delCarMaintainRecord(carMaintainRecord);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 添加车险记录页面
	 * @param carId
	 * @param busType
	 * @return
	 */
	@RequestMapping("/addInsuranceRecordPage")
	public ModelAndView addInsuranceRecordPage(Integer carId,String busType){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view =  new ModelAndView("/zhbg/car/addInsuranceRecord");
		view.addObject("carId", carId);
		view.addObject("busType", busType);
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 添加车险记录
	 * @param insuranceRecord
	 * @return
	 */
	@RequestMapping("/addInsuranceRecord")
	public ModelAndView addInsuranceRecord(InsuranceRecord insuranceRecord){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/refreshParent");
		carService.addInsuranceRecord(insuranceRecord,userInfo);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		return view;
	}
	
	/**
	 * 车辆使用记录
	 * @param insuranceRecord
	 * @return
	 */
	@RequestMapping("/carUseRecordPage")
	public ModelAndView carUseRecordPage(Integer carId){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/zhbg/car/listUseRecord");
		view.addObject("userInfo",userInfo);
		List<CarUseRecord> listUseRecord =  carService.listCarUserRecord(carId,userInfo);
		view.addObject("listUseRecord", listUseRecord);
		return view;
	}
	/**
	 * 删除车辆
	 * @param carId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delCar")
	public Map<String,Object>  delCar(Integer carId){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		List<CarUseRecord> listCarApply = carService.noUseCarApply(carId,userInfo);
		if(null != listCarApply && !listCarApply.isEmpty()){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, "删除失败,该车辆还有使用记录未完成！");
		}else{
			carService.delCar(carId,userInfo);
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		}
		return map;
	}
	/***************************** 以上是车辆基本信息 *****************************************/
	/***************************** 以下是车辆申请信息 *****************************************/
	/**
	 * 车辆预约，使用状态
	 * @return
	 */
	@RequestMapping("/carStatusPage")
	public ModelAndView carStatusPage(){
		ModelAndView view = new ModelAndView("/zhbg/car/carStatus");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		view.addObject("nowDate",nowDate);
		return view; 
	}
	/**
	 * 该日期后一周预约状况
	 * @param beginDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/carApplyStatus")
	public Map<String,Object> carApplyStatus(String beginDate){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		//开始日期格式化
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(beginDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		Calendar curr = Calendar.getInstance();
		curr.setTime(date);
		
		List<String> nextWeekDay = new ArrayList<String>();//未来一周名称（星期）
		List<String> nextWeek = new ArrayList<String>();//未来一周名称
		
		for(int i = 0;i<7;i++){
			Date date1 = curr.getTime();
			String dateTime = DateTimeUtil.formatDate(date1, DateTimeUtil.yyyy_MM_dd);
			String dayName = DateTimeUtil.getChineseDay(curr);
			curr.set(Calendar.DAY_OF_MONTH,curr.get(Calendar.DAY_OF_MONTH)+1);
			nextWeek.add(dateTime);
			String dateTimeName = dateTime +"("+dayName+")";
			nextWeekDay.add(dateTimeName);
		}
		
		List<Car> listCar = carService.listApplyCar(userInfo);
		
		for(Car c:listCar){
			List<CarApplyByDate> listCarApply = new ArrayList<CarApplyByDate>();
			for(String dateStr :nextWeek){
				CarApplyByDate carApply = new CarApplyByDate();
				List<CarUseRecord> listUseRecord = carService.listCarApplyRecod(dateStr, c.getId(), userInfo);
				carApply.setDateStr(dateStr);
				carApply.setListCarUseRecord(listUseRecord);
				listCarApply.add(carApply);
			}
			c.setListCarApply(listCarApply);
		}
		
		
		
		map.put("nextWeek",nextWeek);
		map.put("nextWeekDay",nextWeekDay);
		map.put("listCar",listCar);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 车辆申请
	 * @param carId
	 * @return
	 */
	@RequestMapping("/carApplyPage")
	public ModelAndView carApplyPage(Integer carId){
		ModelAndView view = new ModelAndView("/zhbg/car/addApplyCar");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		
		List<Car> listCar = carService.listApplyCar(userInfo);
		
		//当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		view.addObject("nowDate", nowDate);
		view.addObject("carId", carId);
		view.addObject("listCar", listCar);
		return view;
	}
	/**
	 * 撤回申请
	 * @param carApply
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/backoutApply")
	public Map<String,Object> backoutApply(CarUseRecord carApply){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		carService.backoutApply(carApply);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 添加车辆申请
	 * @param carId
	 * @return
	 */
	@RequestMapping("/addCarApply")
	public ModelAndView addCarApply(CarUseRecord carApply){
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		carService.addCarApply(carApply,userInfo);
		this.setNotification(Notification.SUCCESS, "申请成功!");
		return view;
	}
	/**
	 * 添加车辆申请
	 * @param carId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/agreeCarApply")
	public Map<String,Object>  agreeCarApply(CarUseRecord carApply){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		carService.agreeCarApply(carApply,userInfo);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 检查申请时间是否冲突
	 * @param carApply
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkCarApply")
	public Map<String,Object>  checkCarApply(CarUseRecord carApply){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		List<CarUseRecord> listCarApply = carService.checkCarApply(carApply,userInfo);
		if(null != listCarApply && !listCarApply.isEmpty()){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
		}else{
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		}
		return map;
	}
	/**
	 * 审核车辆申请
	 * @param carId
	 * @return
	 */
	@RequestMapping("/doCarApply")
	public ModelAndView  doCarApply(CarUseRecord carApply){
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		if(carApply.getState() == 1){
			carService.agreeCarApply(carApply,userInfo);
		}else{
			carService.voteCarApply(carApply,userInfo);
		}
		this.setNotification(Notification.SUCCESS, "审核成功!");
		return view;
	}
	/**
	 * 拒绝车辆申请
	 * @param carId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/voteCarApply")
	public Map<String,Object>  voteCarApply(CarUseRecord carApply){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		carService.voteCarApply(carApply,userInfo);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 归还车辆界面
	 * @param carApply
	 * @return
	 */
	@RequestMapping("/returnCarPage")
	public ModelAndView returnCarPage(Integer carApplyId){
		ModelAndView view = new ModelAndView("/zhbg/car/returnCar");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		CarUseRecord carApply = carService.queryCarApplyById(carApplyId,userInfo);
		view.addObject("carApply",carApply);
		return view;
	}
	/**
	 * 归还车辆
	 * @param carId
	 * @return
	 */
	@RequestMapping("/returnCar")
	public ModelAndView returnCar(CarUseRecord carApply){
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		carService.returnCar(carApply,userInfo);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		return view;
	}
	/**
	 * 查看车辆申请界面
	 * @param carApply
	 * @return
	 */
	@RequestMapping("/viewApplyPage")
	public ModelAndView viewApplyPage(Integer carApplyId){
		ModelAndView view = new ModelAndView("/zhbg/car/viewApplyCar");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		CarUseRecord carApply = carService.queryCarApplyById(carApplyId,userInfo);
		view.addObject("carApply",carApply);
		//验证是否是当前模块管理人员
		if(modAdminService.authCheck(userInfo, ConstantInterface.TYPE_CLGL)){
			view.addObject("isModAdmin", true);
		}else{
			view.addObject("isModAdmin", false);
		}
		return view;
	}
}