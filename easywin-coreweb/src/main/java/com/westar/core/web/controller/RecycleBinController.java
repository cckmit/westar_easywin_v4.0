package com.westar.core.web.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.RecycleBin;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.RecycleBinService;
/**
 * 回收箱
 * @author H87
 *
 */
@Controller
@RequestMapping("/recycleBin")
public class RecycleBinController extends BaseController{

	@Autowired
	RecycleBinService recycleBinService;
	
	/**
	 * 获取预删除的数据分页列表
	 * @param recycleBin 回收站属性条件
	 * @param modTypes 模块数组
	 * @return
	 */
	@RequestMapping("/listPagedPreDel")
	public ModelAndView listPagedPreDel(RecycleBin recycleBin,String[] modTypes){
		ModelAndView view = new ModelAndView();
		
		if(ConstantInterface.TYPE_CRM.equals(recycleBin.getBusType())){//客户
			view.setViewName("/crm/customerList");
		}else if(ConstantInterface.TYPE_ITEM.equals(recycleBin.getBusType())){//项目
			view.setViewName("/item/listItem");
		}else if(ConstantInterface.TYPE_TASK.equals(recycleBin.getBusType())){//任务
			view.setViewName("/task/taskCenter");
		}else if(ConstantInterface.TYPE_QUES.equals(recycleBin.getBusType())){//问答
			view.setViewName("/qas/listPagedQas");
		}else if(ConstantInterface.TYPE_VOTE.equals(recycleBin.getBusType())){//投票
			view.setViewName("/vote/listPagedVote");
		}else if(ConstantInterface.TYPE_ANNOUNCEMENT.equals(recycleBin.getBusType())){//公告
			view.setViewName("/announcement/announCenter");
		}
		
		//头文件的显示
		view.addObject("homeFlag",recycleBin.getBusType());
		UserInfo userInfo = this.getSessionUser();
		//回收箱所在企业
		recycleBin.setComId(userInfo.getComId());
		//回收箱的所有者
		recycleBin.setUserId(userInfo.getId());
		
		//模块数组化
		List<String> modList = new ArrayList<String>();
		modList.add(recycleBin.getBusType());
		
		List<RecycleBin> list = recycleBinService.listPagedPreDel(recycleBin,modList);
		view.addObject("userInfo",userInfo);
		view.addObject("list",list);
		view.addObject("modList",modList);
		
		view.addObject("recycleTab",ConstantInterface.TYPE_CRM);
		return view;
	}
	
	/**
	 * 操作删除对象
	 * @param ids
	 * @param state 1 删除 0还原
	 * @param redirectPage
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/optObject")
	public ModelAndView optObject(String[] ids,String state,String redirectPage) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		//操作删除对象
		recycleBinService.updateObject(ids,state,userInfo);
		this.setNotification(Notification.SUCCESS, "操作成功");
		return new ModelAndView("redirect:"+redirectPage);
	}
	/**
	 * 操作删除对象
	 * @param recycleBin
	 * @param state
	 * @param redirectPage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/optAllObject")
	public ModelAndView optAllObject(RecycleBin recycleBin,String state,String redirectPage,String[] modTypes) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		recycleBin.setUserId(userInfo.getId());
		recycleBin.setComId(userInfo.getComId());
		//模块数组化
		List<String> modList = new ArrayList<String>();
		modList.add(recycleBin.getBusType());
		//操作删除对象
		recycleBinService.updateAllObject(recycleBin,state,userInfo,modList);
		this.setNotification(Notification.SUCCESS, "操作成功");
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	
}