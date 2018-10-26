package com.westar.core.web.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.westar.base.model.FunctionList;
import com.westar.base.model.ProUpFiles;
import com.westar.base.util.CommonUtil;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.FunctionListService;
import org.apache.xpath.functions.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.Product;
import com.westar.base.model.Task;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.DataDicService;
import com.westar.core.service.ModOptConfService;
import com.westar.core.service.ProductService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.core.service.ProductService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.core.service.ProductService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/product")
@SuppressWarnings("unused")
public class ProductController extends BaseController{

	@Autowired
	ProductService productService;

	@Autowired
	ModOptConfService modOptConfService;

	@Autowired
	DataDicService dataDicService;

	@Autowired
	ForceInPersionService forceInPersionService;

	@Autowired
	FunctionListService functionListService;
	/**
	 * 分页显示产品
	 * @param request
	 * @param product
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/listPagedPro")
	public ModelAndView listPagedPro(HttpServletRequest request, Product product,Integer pageSize){
		if(null == pageSize || pageSize < 1){
			pageSize = 12;
			PaginationContext.setPageSize(pageSize);
		}
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView mav = new ModelAndView("/product/proCenter");
		mav.addObject("product",product);

		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo",userInfo);

		boolean isAuthorized = forceInPersionService.isForceInPersion(userInfo,ConstantInterface.TYPE_PRODUCT);
		List<Product> list = productService.listPagedPro(product,userInfo,isAuthorized);
		mav.addObject("list",list);

		// 获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(
				userInfo.getComId(), ConstantInterface.TYPE_PRODUCT);
		if (null != listModuleOperateConfig) {
			for (ModuleOperateConfig vo : listModuleOperateConfig) {
				mav.addObject(vo.getOperateType(), ConstantInterface.MOD_OPT_STATE_YES);
			}
		}

		//获取类型列表

		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_PRODUCT);
		return mav;
	}
	
	/**
	 * 获取产品关联
	 * @author hcj 
	 * @date: 2018年10月15日 下午6:56:04
	 * @return
	 */
	@RequestMapping(value = "productRelevancePage")
	public ModelAndView productRelevancePage(Product product) {
		ModelAndView view = new ModelAndView("/common/product/productRelevance");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	
	/**
	 * 异步取得需求分页数
	 * @param product 需求的查询条件
	 * @param pageNum 页码
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListPagedProductForSelect")
	public HttpResult<PageBean<Product>> ajaxListPagedProductForSelect(Product product,
			Integer pageNum, Integer pageSize) {

		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			return new HttpResult<PageBean<Product>>().error(CommonConstant.OFF_LINE_INFO);
		}
		pageNum = null == pageNum ? 0 : pageNum;
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		PageBean<Product> pageBean = productService.listPagedProductForSelect(product,userInfo);
		return new HttpResult<PageBean<Product>>().ok(pageBean);
	}

	/**
	 * 跳转产品添加页面
	 * @return
	 */
	@RequestMapping("/addProPage")
	public ModelAndView addProPage(){
		ModelAndView mav = new ModelAndView("/product/addPro","userInfo",this.getSessionUser());
		return mav;
	}

	/**
	 * 添加产品
	 * @param product
	 * @return
	 */
	@RequestMapping("/addPro")
	public ModelAndView addPro(Product product){
		ModelAndView mav = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		Integer id = productService.addPro(product,this.getSessionUser());
		if(null != id && id > 0){
			this.setNotification(Notification.SUCCESS,"添加成功！");
		}else{
			this.setNotification(Notification.ERROR,"添加失败！");
		}
		return mav;
	}

	/**
	 * 产品查看
	 * @param id
	 * @return
	 */
	@RequestMapping("/viewProPage")
	public ModelAndView viewProPage(Integer id){
		UserInfo userInfo = this.getSessionUser();
		Product product = productService.getProDetailById(id,userInfo.getComId());
		ModelAndView mav = new ModelAndView();

		if(product.getCreator().equals(userInfo.getId())){
			mav.setViewName("/product/editPro");
		}else{
			mav.setViewName("/product/viewPro");
		}
		mav.addObject("product",product);
		mav.addObject("userInfo",userInfo);
		return mav;
	}

	@ResponseBody
	@RequestMapping(value="/authorCheck",method=RequestMethod.POST)
	public HttpResult<Void> authorCheck(){
		HttpResult<Void> result = new HttpResult<>();
		result.setCode(1);
		return result;
	}

	/**
	 * 产品名称更新
	 * @param product
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/proAjaxUpdate",method=RequestMethod.POST)
	public HttpResult<Void> proAjaxUpdate(Product product){
		UserInfo userInfo = this.getSessionUser();
		HttpResult<Void> result = new HttpResult<>();

		productService.proAjaxUpdate(product,userInfo);

		result.setCode(HttpResult.CODE_OK);
		result.setMsg("修改成功！");
		return result;
	}

	/**
	 * 文档页面跳转
	 * @return
	 */
	@RequestMapping("/productUpfilePage")
	public ModelAndView productUpfilePage(ProUpFiles proUpFiles){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/product/productUpfiles");
		List<ProUpFiles> list = productService.listProUpFiles(proUpFiles,userInfo.getComId());
		mav.addObject("list",list);
		mav.addObject("proUpFiles",proUpFiles);
		mav.addObject("userInfo",userInfo);
		return mav;
	}

	/**
	 * 删除产品附件
	 * @param id
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delProUpFile",method=RequestMethod.POST)
	public HttpResult<Void> delProUpFile(Integer id,String type){
		UserInfo userInfo = this.getSessionUser();
		HttpResult<Void> result = new HttpResult<>();
		productService.delProUpFile(id,type,userInfo);
		result.setCode(HttpResult.CODE_OK);
		result.setMsg("删除成功！");
		return result;
	}

}