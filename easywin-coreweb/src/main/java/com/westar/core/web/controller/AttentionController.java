package com.westar.core.web.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Attention;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.MsgNoRead;
import com.westar.core.service.AttentionService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.web.FreshManager;

/**
 * 
 * 描述:关注模块的控制层
 * @author zzq
 * @date 2018年9月19日 上午9:32:48
 */
@Controller
@RequestMapping("/attention")
public class AttentionController extends BaseController{

	@Autowired
	AttentionService attentionService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	/**
	 * 分页查询关注信息
	 * @param atten
	 * @param request
	 * @param modTypes
	 * @return
	 */
	@RequestMapping(value="/attCenter")
	public ModelAndView listpagedAtten(Attention atten,HttpServletRequest request,String[] modTypes){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		ModelAndView mav = new ModelAndView("/attention/attCenter");
		UserInfo userInfo = this.getSessionUser();
		//企业号
		atten.setComId(userInfo.getComId());
		//操作员Id
		atten.setUserId(userInfo.getId());
		
		//模块数组化
		List<String> modList = null;
		if(null!=modTypes && modTypes.length>0){
			Arrays.sort(modTypes);
			modList = Arrays.asList(modTypes);
		}
		//分页查询关注信息
		List<Attention> listAttention = attentionService.listpagedAtten(atten,modList);
		
		List<Attention> list = new ArrayList<Attention>();
		if(null!=listAttention && listAttention.size()>0){
			for (Attention obj : listAttention) {
				String modTitle = obj.getModTitle();
				modTitle = modTitle.replaceAll("\\n", "").replaceAll("\\t", "");
				obj.setModTitle(modTitle);
				
				list.add(obj);
			}
		}
		
		mav.addObject("list", list);
		//取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		mav.addObject("listOwners",listOwners);
		
		mav.addObject("userInfo", userInfo);
		mav.addObject("atten", atten);
		mav.addObject("modList", modList);
		
		//分模块更新未读
		List<MsgNoRead> modTodoList = todayWorksService.countAttenNoReadByType(userInfo.getComId(),userInfo.getId());
		if(null!=modTodoList && !modTodoList.isEmpty()){
			Gson gson = new Gson();
			mav.addObject("modTodoList",gson.toJson(modTodoList));
		}else{
			mav.addObject("modTodoList",null);
		}
		
		return mav;
		
	}
	/**
	 * 改变关注状态
	 * @param atten
	 * @param attentionState
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxchangeAtten" , method=RequestMethod.POST)
	public Map<String,Object> ajaxchangeAtten(Attention atten,Integer attentionState) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){//用户不存在，需要重新登录
			map.put("status", "f");
			map.put("info",CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//所在企业
		atten.setComId(userInfo.getComId());
		//关注的人员
		atten.setUserId(userInfo.getId());
		
		//修改数据
		attentionService.delAttention(atten,userInfo,attentionState);
		
		map.put("status", "y");
		return map;
		
	}
	/**
	 * 改变关注状态
	 * @param atten
	 * @param attentionState
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/batchDelAtten" , method=RequestMethod.POST)
	public Map<String,Object> batchDelAtten(String[] ids) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		List<Integer> list = new ArrayList<Integer>();
		if(null!=ids && ids.length>0){
			for(String data:ids){
				list.add(Integer.parseInt(data.split("@")[0]));
			}
			//修改数据
			attentionService.delAttention(list,userInfo);
		}
		
		map.put("state", "y");
		return map;
		
	}
	
	
}