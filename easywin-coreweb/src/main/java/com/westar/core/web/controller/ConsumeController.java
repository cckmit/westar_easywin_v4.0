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
import com.westar.base.model.Consume;
import com.westar.base.model.ConsumeType;
import com.westar.base.model.ConsumeUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.ConsumeService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

/**
 * 
 * 描述:消费记录
 * @author zzq
 * @date 2018年7月13日 下午4:21:37
 */
@Controller
@RequestMapping("/consume")
public class ConsumeController extends BaseController{

	@Autowired
	ConsumeService consumeService;
	
	/**
	 * 转向费用类型多选选页面
	 */
	@RequestMapping("/consumeSelectPage")
	public ModelAndView consumeSelectPage() {
		ModelAndView mav = new ModelAndView("/financial/consume/consumeSelect");
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("userInfo",sessionUser);
		return mav;
	}
	/**
	 * 获取费用类型集合
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listPagedConsumeForSelect")
	public Map<String,Object> listPagedConsumeForSelect(Consume consume,Integer pageNum, Integer pageSize) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info",CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<Consume> pageBean = consumeService.listPagedConsumeForSelect(userInfo,consume);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 编辑页面新增发票
	 * @param consume
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addFiles")
	public String addFiles(String upfileIds ,Integer id) throws Exception {
		boolean succ = true;
		UserInfo userInfo = this.getSessionUser();
		if(!CommonUtil.isNull(upfileIds)) {
			String[] fileIds = upfileIds.split(",");
			consumeService.addFiles(id,fileIds, userInfo);
		}else {
			succ = false;
		}
		if (succ) {
			return "操作成功";
		} else {
			return "操作失败";
		}
	}
	
	/**
	 * 消费记录
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listConsume")
	public ModelAndView listConsume(HttpServletRequest request,Consume consume) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/financial/financialCenter");
		view.addObject("userInfo", userInfo);
	
		view.addObject("consume",consume);//筛选参数
		
		List<Consume> listConsumes = consumeService.listPagedConsume(consume,userInfo,null);
		view.addObject("listConsumes", listConsumes);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.MENU_FEE_APPLY);
		return view;
	}
	
	/**
	 * 分页查询费用类型
	 * @param consumeType
	 * @return
	 */
	@RequestMapping("/listConsumeType")
	public ModelAndView listConsumeType(ConsumeType consumeType) {
		ModelAndView view = new ModelAndView("/financial/consume/listConsumeType");
		List<ConsumeType> consumeTypes = consumeService.listConsumeType(this.getSessionUser().getComId());
		view.addObject("listConsumeType", consumeTypes);
		return view;
	}
	
	/**
	 * 更新费用类型属性值
	 * @param name 修改的字段
	 * @param val 修改的字段值
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateConsumNames")
	public String updateConsumNames(String name , String val,Integer id ) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		boolean succ = consumeService.updateConsumNames(name,val,id, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	
	/**
	 * 删除费用类型
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consumeTypeDel")
	public String consumeTypeDel(Integer id ){
		UserInfo userInfo = this.getSessionUser();
		Integer count = consumeService.countConsumeTypeById(id);
		if(count > 0) {
			return "该费用类型正在使用中，不能删除";
		}else {
		boolean succ = consumeService.deleteConsumeType(id, userInfo);
		if (succ) {
			return "删除成功";
		} else {
			return "删除失败";
		}
	}
	
	}
	
	/**
	 * 费用类型添加
	 * @param consumeType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxAddConsumeType")
	public Map<String, Object> ajaxAddConsumeType(ConsumeType consumeType)
			throws Exception {

		UserInfo userInfo = this.getSessionUser();
		consumeType.setComId(userInfo.getComId());
		consumeType.setCreator(userInfo.getId());
		Integer id = consumeService.addConsumeType(consumeType, userInfo);

		consumeType.setId(id);
		consumeType.setTypeOrder(consumeType.getTypeOrder() + 1);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consumeType", consumeType);
		map.put("status", "y");
		return map;
	}

	
	/**
	 * 费用类型序号
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetConsumeTypeOrder")
	public Map<String, Object> ajaxGetConsumeTypeOrder() {

		Map<String, Object> map = new HashMap<String, Object>();
		Integer orderNum = consumeService.queryConsumeTypeOrderMax(this
				.getSessionUser().getComId());
		// 取出 排序号
		map.put("orderNum", orderNum);

		map.put("status", "y");
		return map;
	}
	
	/**
	 * 添加消费记录
	 * @return
	 */
	@RequestMapping("/addConsumePage")
	public ModelAndView addConsumePage() {
		ModelAndView mav = new ModelAndView("/financial/consume/addConsume");
		UserInfo user = this.getSessionUser();
		mav.addObject("userInfo", user);
		List<ConsumeType> listConsumeType = consumeService.listConsumeType(user.getComId());
		mav.addObject("listConsumeType", listConsumeType);
		return mav;
	}
	
	/**
	 * 根据id获取费用类型
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getConsumeTypeById")
	public ConsumeType getConsumeTypeById( Integer id) {
		ConsumeType consumeType = consumeService.getConsumeTypeById(id);
		return consumeType;
	}
	
	/**
	 * 添加消费记录
	 * @param consume
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addConsume")
	public ModelAndView addCustomer(Consume consume) throws Exception {

		UserInfo userInfo = this.getSessionUser();
		consumeService.addConsume(consume, userInfo);
		this.setNotification(Notification.SUCCESS, "添加成功");

		ModelAndView view = new ModelAndView("/refreshParent");
		return view;
	}
	
	/**
	 * 删除消费记录
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/deleteConsumes")
	public ModelAndView deleteConsumes(Integer[] ids,String redirectPage){
		UserInfo userInfo = this.getSessionUser();
		try {
			//删除消费记录
			consumeService.deleteConsumes(userInfo,ids);
			this.setNotification(Notification.SUCCESS, "删除成功");
		} catch (Exception e) {
			this.setNotification(Notification.ERROR, "删除失败");
		}
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	/**
	 * 查看详情页面
	 * @param id
	 * @return
	 */
	@RequestMapping("/viewConsumePage")
	public ModelAndView viewConsumePage(Integer id,String showPage) {
		ModelAndView mav = null;
		if(showPage.equals("view")) {
			mav = new ModelAndView("/financial/consume/viewConsume");
		}else {
			mav = new ModelAndView("/financial/consume/editConsume");
		}
		UserInfo user = this.getSessionUser();
		mav.addObject("userInfo", user);
		Consume consume = consumeService.getConsumeById(id);
		mav.addObject("consume", consume);
		List<ConsumeType> listConsumeType = consumeService.listConsumeType(user.getComId());
		mav.addObject("listConsumeType", listConsumeType);
		return mav;
	}
	
	/**
	 * 修改消费记录
	 * @param consume
	 * @param attrName 修改的字段
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateConsumes")
	public String updateConsumes(Consume consume) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		consume.setComId(userInfo.getComId());
		boolean succ = consumeService.updateConsumes(consume, userInfo,null);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	
	/**
	 * 消费记录发票页面
	 * @param consumeUpfile
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/consumeUpfilePage")
	public ModelAndView consumeUpfilePage(ConsumeUpfile consumeUpfile)
			throws Exception {
		ModelAndView view = new ModelAndView("/financial/consume/consumeUpfile");
		UserInfo userInfo = this.getSessionUser();
		List<ConsumeUpfile> listConsumeUpfile = consumeService
				.listPagedConsumeUpfile(userInfo.getComId(),
						consumeUpfile.getConsumeId());
		Consume consume = consumeService.getConsumeById(consumeUpfile.getConsumeId());
		view.addObject("listConsumeUpfile", listConsumeUpfile);
		view.addObject("userInfo", userInfo);
		view.addObject("customerUpfile", consumeUpfile);
		if(consume != null) {
			view.addObject("consumeStatus", consume.getStatus());
		}
		return view;
	}
	
	/**
	 * 删除发票附件
	 * @param consumeId
	 * @param consumeUpFileId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delConsumeUpfile")
	public Map<String, Object> delConsumeUpfile(Integer consumeId,Integer consumeUpFileId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		consumeService.delConsumeUpfile(consumeUpFileId,userInfo,consumeId);
		map.put("status", "y");
		return map;
	}
	
	
	
}