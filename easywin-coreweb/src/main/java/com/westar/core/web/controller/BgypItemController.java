package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.BgypFl;
import com.westar.base.model.BgypItem;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.BgypItemService;

@Controller
@RequestMapping("/bgypItem")
public class BgypItemController extends BaseController{

	@Autowired
	BgypItemService bgypItemService;
	
	/**
	 * 查询办公用品分类的条目 
	 * @param bgypItem 办公用品的条目
	 * @param flName 分类名称
	 * @return
	 */
	@RequestMapping("/listPagedBgypItem4Fl")
	public ModelAndView listPagedBgypItem4Fl(BgypItem bgypItem,String flName){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/listPagedBgypItem4Fl");
		UserInfo sessionUser = this.getSessionUser();
		List<BgypItem> listBgypItem = bgypItemService.listPagedBgypItem4Fl(sessionUser,bgypItem);
		mav.addObject("listBgypItem", listBgypItem);
		
		mav.addObject("userInfo", sessionUser);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_BGYP);
		
		return mav;
	}
	/**
	 * 查询办公用品分类的条目 
	 * @param bgypItem 办公用品的条目
	 * @param flName 分类名称
	 * @return
	 */
	@RequestMapping("/listPagedBgypStore")
	public ModelAndView listPagedBgypStore(BgypItem bgypItem){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		List<BgypItem> listBgypItem = bgypItemService.listPagedBgypStore(sessionUser,bgypItem);
		mav.addObject("listBgypItem", listBgypItem);
		
		mav.addObject("userInfo", sessionUser);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_BGYP);
		return mav;
	}
	
	/**
	 * 异步取得需要查询的数据
	 * @param flId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListBgypItem4FlWithSub")
	public Map<String,Object> ajaxListBgypItem4FlWithSub(Integer flId){
		if(null==flId){
			flId = -1;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		List<BgypItem> listBgypItem = bgypItemService.listBgypItem4FlWithSub(sessionUser,flId);
		map.put("listBgypItem", listBgypItem);
		return map;
	}
	/**
	 * 
	 * @param flId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListBgypStore4FlWithSub")
	public Map<String,Object> ajaxListBgypStore4FlWithSub(Integer flId){
		if(null==flId){
			flId = -1;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		List<BgypItem> listBgypItemStore = bgypItemService.listBgypStore4FlWithSub(sessionUser,flId);
		map.put("listBgypStore", listBgypItemStore);
		return map;
	}
	
	/**
	 * 跳转到添加办公用品条目界面
	 * @param bgypItem
	 * @return
	 */
	@RequestMapping("/addBgypItemPage")
	public ModelAndView addBgypItemPage(BgypItem bgypItem){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/addBgypItem");
		UserInfo sessionUser = this.getSessionUser();
		return mav;
	}
	/**
	 * 添加办公用品条目
	 * @param bgypItem 办公用品条目
	 * @param redirectPage 添加成功后跳转页面
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddBgypItem")
	public Map<String,Object> ajaxAddBgypItem(BgypItem bgypItem){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		map = bgypItemService.addBgypItem(sessionUser,bgypItem);
		return map;
	}
	
	/**
	 * 跳转到编辑办公用品条目界面
	 * @param bgypItem
	 * @return
	 */
	@RequestMapping("/updateBgypItemPage")
	public ModelAndView updateBgypItemPage(Integer bgypItemId){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/updateBgypItem");
		UserInfo sessionUser = this.getSessionUser();
		BgypItem bgypItem = bgypItemService.queryBgypItemById(sessionUser.getComId(),bgypItemId);
		mav.addObject("bgypItem", bgypItem);
		return mav;
	}
	/**
	 * 异步修改办公用品信息
	 * @param bgypFl 办公用品信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxUpdateBgypItem")
	public Map<String,Object> ajaxUpdateBgypItem(BgypItem bgypItem){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//头文件的显示
		map = bgypItemService.updateBgypItem(sessionUser,bgypItem);
		return map;
	}
	
	/**
	 * 删除办公用品条目
	 * @param bgypItemIds 办公用品条目主键
	 * @return
	 */
	@RequestMapping("/updateBgypItemFl")
	public ModelAndView updateBgypItemFl(Integer[] bgypItemIds,String redirectPage,Integer bgflId,String bgFlName){
		ModelAndView mav = new ModelAndView();
		UserInfo sessionUser = this.getSessionUser();
		if(null!=bgypItemIds && bgypItemIds.length>0){
			BgypFl bgypFl = new BgypFl();
			bgypFl.setId(bgflId);
			bgypFl.setFlName(bgFlName);
			bgypItemService.updateBgypItemFl(sessionUser.getComId(),bgypFl,bgypItemIds);
		}
		this.setNotification(Notification.SUCCESS, "操作成功！");
		mav.setViewName("redirect:"+redirectPage);
		return mav;
	}
	/**
	 * 删除办公用品条目
	 * @param bgypItemIds 办公用品条目主键
	 * @return
	 */
	@RequestMapping("/delBgypItem")
	public ModelAndView delBgypItem(Integer[] bgypItemIds,String redirectPage){
		ModelAndView mav = new ModelAndView();
		UserInfo sessionUser = this.getSessionUser();
		if(null!=bgypItemIds && bgypItemIds.length>0){
			List<BgypItem> bgypItems = bgypItemService.delBgypItem(sessionUser.getComId(),bgypItemIds);
			if(null!=bgypItems && !bgypItems.isEmpty()){
				StringBuffer info = new StringBuffer("以下用品已被使用：");
				BgypItem bgypItemLast = bgypItems.remove(bgypItems.size()-1);
				for (BgypItem bgypItem : bgypItems) {
					info.append(bgypItem.getBgypName()+"、");
				}
				info.append(bgypItemLast.getBgypName());
				this.setNotification(Notification.ERROR, info.toString());
				mav.setViewName("redirect:"+redirectPage);
				return mav;
			}
		}
		this.setNotification(Notification.SUCCESS, "操作成功！");
		mav.setViewName("redirect:"+redirectPage);
		return mav;
	}
	
	
	
}