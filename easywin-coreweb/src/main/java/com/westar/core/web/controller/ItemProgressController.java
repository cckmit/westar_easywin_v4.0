package com.westar.core.web.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.ItemProgressTemplate;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.ItemProgressService;
import com.westar.core.web.FreshManager;

@Controller
@RequestMapping("/itemProgress")
public class ItemProgressController extends BaseController{

	@Autowired
	ItemProgressService itemProgressService;
	
	/**
	 * 分页查询项目进度模板
	 * @author hcj 
	 * @date: 2018年10月10日 下午4:00:54
	 * @param request
	 * @param itemProgressTemplate
	 * @return
	 */
	@RequestMapping(value = "/listPagedProgressTemplate")
	public ModelAndView listItemPage(HttpServletRequest request,ItemProgressTemplate itemProgressTemplate) {
		// 清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/item/listItem");
		UserInfo userInfo = this.getSessionUser();
		List<ItemProgressTemplate> lists = itemProgressService.listPagedProgressTemplate(itemProgressTemplate,userInfo);
		view.addObject("userInfo",userInfo);
		view.addObject("lists",lists);
		view.addObject("itemProgressTemplate",itemProgressTemplate);
		// 头文件的显示
		view.addObject("homeFlag", ConstantInterface.TYPE_ITEM);
		return view;
	}
	
	/**
	 * 添加项目进度模板页面
	 * @author hcj 
	 * @date: 2018年10月11日 上午10:42:02
	 * @return
	 */
	@RequestMapping(value = "/addProgressTemplatePage")
	public ModelAndView addProgressTemplatePage() {
		ModelAndView view = new ModelAndView("/item/addProgressTemplate");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		return view;
	}
	
	/**
	 * 添加项目进度模板
	 * @author hcj 
	 * @date: 2018年10月11日 上午11:42:11
	 * @param itemProgressTemplate
	 * @return
	 */
	@RequestMapping("/addProgressTemplate")
	public ModelAndView addProgressTemplate(ItemProgressTemplate itemProgressTemplate){
		UserInfo userInfo = this.getSessionUser();
		itemProgressService.addProgressTemplate(itemProgressTemplate,userInfo);
		ModelAndView view = new ModelAndView("/refreshParent");
		this.setNotification(Notification.SUCCESS, "添加成功!");
		return view;
	}
	
	/**
	 * 编辑项目进度模板页面
	 * @author hcj 
	 * @date: 2018年10月11日 下午1:11:49
	 * @return
	 */
	@RequestMapping(value = "/editProgressTemplatePage")
	public ModelAndView editProgressTemplatePage(Integer id) {
		ModelAndView view = new ModelAndView("/item/editProgressTemplate");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		ItemProgressTemplate itemProgressTemplate = itemProgressService.queryItemProgressTemplateById(id);
		view.addObject("itemProgressTemplate",itemProgressTemplate);
		return view;
	}
	
	/**
	 * 添加项目进度模板
	 * @author hcj 
	 * @date: 2018年10月11日 上午11:42:11
	 * @param itemProgressTemplate
	 * @return
	 */
	@RequestMapping("/updateProgressTemplate")
	public ModelAndView updateProgressTemplate(ItemProgressTemplate itemProgressTemplate){
		UserInfo userInfo = this.getSessionUser();
		itemProgressService.updateProgressTemplate(itemProgressTemplate,userInfo);
		ModelAndView view = new ModelAndView("/refreshParent");
		this.setNotification(Notification.SUCCESS, "更新成功!");
		return view;
	}
	
	/**
	 * 批量删除项目进度模板
	 * @author hcj 
	 * @date: 2018年10月11日 下午2:32:35
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delProgressTemplates")
	public ModelAndView delProgressTemplates(Integer[] ids,String redirectPage){
		UserInfo userInfo = this.getSessionUser();
		itemProgressService.delProgressTemplates(ids,userInfo);
		ModelAndView view = new ModelAndView("redirect:"+redirectPage);
		this.setNotification(Notification.SUCCESS, "删除成功!");
		return view;
	}
	
	/**
	 * 克隆模板
	 * @author hcj 
	 * @date: 2018年10月11日 下午2:59:11
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addCopyProgressTemplate")
	public HttpResult<String> addCopyProgressTemplate(Integer id){
		UserInfo userInfo = this.getSessionUser();
		itemProgressService.addCopyProgressTemplate(id, userInfo);
		return new HttpResult<String>().ok("克隆成功");
	}
	
	/**
	 * 根据模板id查询模板进度
	 * @author hcj 
	 * @date: 2018年10月12日 上午9:37:08
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryItemProgressTemplateById")
	public HttpResult<ItemProgressTemplate> queryItemProgressTemplateById(Integer id){
		ItemProgressTemplate itemProgressTemplate = itemProgressService.queryItemProgressTemplateById(id);
		return new HttpResult<ItemProgressTemplate>().ok(itemProgressTemplate);
	}
	
}