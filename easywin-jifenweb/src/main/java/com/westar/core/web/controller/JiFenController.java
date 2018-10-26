package com.westar.core.web.controller;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.Jifen;
import com.westar.base.model.JifenConfig;
import com.westar.base.model.JifenHistory;
import com.westar.base.model.JifenLev;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.HttpResult;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.MathExtend;
import com.westar.core.service.JiFenService;

/**
 * 积分
 * @author zzq
 *
 */
@Controller
@RequestMapping("/jiFen")
public class JiFenController extends BaseController{

	@Autowired
	JiFenService jiFenService;
	
	/**
	 * 积分项配置列表
	 * @return
	 */
	@RequestMapping("/listJifenConfigPage")
	public ModelAndView listJifenConfigPage(){
		List<JifenConfig> list = jiFenService.listJiFenConfigs();
		ModelAndView mav = new ModelAndView("jiFen/listJifenConfig", "list", list);
		return mav;
	}
	/**
	 * 积分项配置分页列表
	 * @return
	 */
	@RequestMapping("/listPagedJifenConfig")
	public ModelAndView listPagedJifenConfig(){
		List<JifenConfig> list = jiFenService.listPagedJiFenConfigs();
		ModelAndView mav = new ModelAndView("jiFen/jiFenCenter", "list", list);
		
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
	/**
	 * 跳转积分类型添加页面
	 * @return
	 */
	@RequestMapping("/addJifenTypePage")
	public ModelAndView addJifenTypePage(){
		ModelAndView view = new ModelAndView("jiFen/addJifenType");
		//积分排序最大值
		Integer orderMax =  jiFenService.queryJifenConfigOrderMax();
		view.addObject("orderMax",orderMax);
		return view;
	}
	/**
	 * 验证类别代码是否有重复
	 * @param param 填写的参数
	 * @param id 类别主键（添加为0 修改非0）
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validateJifenCode")
	public Map<String,String> validateJifenCode(String param,Integer id){
		Map<String,String> map = new HashMap<String, String>(3);
		//验证类别代码是否有重复
		boolean flag = jiFenService.validateJifenCode(param,id);
		if(flag){
			map.put("status", "y");
		}else{
			map.put("status", "n");
			map.put("info", "代码不能重复");
		}
		return map;
	}
	/**
	 * 添加积分类别
	 * @param jifenConfig
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddJifenType")
	public Map<String,Object> ajaxAddJifenType(JifenConfig jifenConfig){
		Map<String,Object> map = new HashMap<String, Object>();
		if(jifenConfig.getTypeScore()==0){//该项设置的分数为0
			jifenConfig.setType(ConstantInterface.JIFEN_ZDY);
		}else{
			if(jifenConfig.getDayMaxScore()==0 &&jifenConfig.getSysMaxScore()==0){//不是日常任务，也不是系统任务
				jifenConfig.setType(ConstantInterface.JIFEN_PERMANENT);
			}else if(jifenConfig.getDayMaxScore()==0 &&jifenConfig.getSysMaxScore()!=0){//系统任务
				jifenConfig.setType(ConstantInterface.JIFEN_SYS);
			}else if(jifenConfig.getDayMaxScore()!=0 &&jifenConfig.getSysMaxScore()==0){//日常任务
				jifenConfig.setType(ConstantInterface.JIFEN_DAY);
			}
		}
		
		
		Integer id = jiFenService.addJifenType(jifenConfig);
		jifenConfig.setId(id);
		Integer orderMax =  jiFenService.queryJifenConfigOrderMax();
		map.put("orderMax",orderMax);
		map.put("jifenConfig",jifenConfig);
		return map;
	}
	/**
	 * 修改积分类别
	 * @param jifenConfig
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxUpdateJifenConfig")
	public Map<String,Object> ajaxUpdateJifenConfig(JifenConfig jifenConfig){
		Map<String,Object> map = new HashMap<String, Object>();
		if(null!=jifenConfig.getJifenCode()){
			boolean flag = jiFenService.validateJifenCode(jifenConfig.getJifenCode(),jifenConfig.getId());
			if(!flag){
				map.put("status","n");
				map.put("info","代码不能重复");
				return map;
			}
		}
		
		if(null!=jifenConfig.getTypeScore()){//修改的是积分项
			//积分配置
			JifenConfig obj = jiFenService.getJifenConfigById(jifenConfig.getId());
			if(jifenConfig.getTypeScore()==0){//该项设置的分数为0
				if(obj.getSysMaxScore()!=0){
					map.put("status","n");
					map.put("info","类别分值为0,封顶分数不能非0!");
					return map;
				}else if(obj.getDayMaxScore()!=0){
					map.put("status","n");
					map.put("info","类别分值为0,每日封顶分数不能非0!");
					return map;
				}else{
					jifenConfig.setType(ConstantInterface.JIFEN_ZDY);
				}
			}else{
				if(obj.getDayMaxScore()==0 && obj.getSysMaxScore()==0){//不是日常任务，也不是系统任务
					jifenConfig.setType(ConstantInterface.JIFEN_PERMANENT);
				}else if(obj.getDayMaxScore()==0 && obj.getSysMaxScore()!=0){//系统任务
					jifenConfig.setType(ConstantInterface.JIFEN_SYS);
				}else if(obj.getDayMaxScore()!=0 && obj.getSysMaxScore()==0){//日常任务
					jifenConfig.setType(ConstantInterface.JIFEN_DAY);
				}else if(obj.getDayMaxScore()!=0 && obj.getSysMaxScore()!=0){
					map.put("status","n");
					map.put("info","每日封顶不能与封顶分数同时非0");
					return map;
				}
			}
		}else if(null!=jifenConfig.getDayMaxScore()){//修改的是每日积分项
			//积分配置
			JifenConfig obj = jiFenService.getJifenConfigById(jifenConfig.getId());
			if(obj.getTypeScore()==0){//该项设置的分数为0
				jifenConfig.setType(ConstantInterface.JIFEN_ZDY);
			}else{
				if(jifenConfig.getDayMaxScore()==0 && obj.getSysMaxScore()==0){//不是日常任务，也不是系统任务
					jifenConfig.setType(ConstantInterface.JIFEN_PERMANENT);
				}else if(jifenConfig.getDayMaxScore()==0 && obj.getSysMaxScore()!=0){//系统任务
					jifenConfig.setType(ConstantInterface.JIFEN_SYS);
				}else if(jifenConfig.getDayMaxScore()!=0 && obj.getSysMaxScore()==0){//日常任务
					jifenConfig.setType(ConstantInterface.JIFEN_DAY);
				}else if(jifenConfig.getDayMaxScore()!=0 && obj.getSysMaxScore()!=0){
					map.put("status","n");
					map.put("info","每日封顶不能与封顶等分数同时非0");
					return map;
				}
			}
		}else if(null!=jifenConfig.getSysMaxScore()){
			//积分配置
			JifenConfig obj = jiFenService.getJifenConfigById(jifenConfig.getId());
			if(obj.getTypeScore()==0){//该项设置的分数为0
				jifenConfig.setType(ConstantInterface.JIFEN_ZDY);
			}else{
				if(obj.getDayMaxScore()==0 && jifenConfig.getSysMaxScore()==0){//不是日常任务，也不是系统任务
					jifenConfig.setType(ConstantInterface.JIFEN_PERMANENT);
				}else if(obj.getDayMaxScore()==0 && jifenConfig.getSysMaxScore()!=0){//系统任务
					jifenConfig.setType(ConstantInterface.JIFEN_SYS);
				}else if(obj.getDayMaxScore()!=0 && jifenConfig.getSysMaxScore()==0){//日常任务
					jifenConfig.setType(ConstantInterface.JIFEN_DAY);
				}else if(obj.getDayMaxScore()!=0 && jifenConfig.getSysMaxScore()!=0){
					map.put("status","n");
					map.put("info","每日封顶不能与封顶分数同时非0");
					return map;
				}
			}
		}
		//修改积分项
		jiFenService.updateJifenConfig(jifenConfig);
		map.put("status","y");
		return map;
	}
	/**
	 *  删除积分类别
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/jifenConfigDel")
	public Map<String,Object> jifenConfigDel(Integer id){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			jiFenService.delJifenConfig(id);
			map.put("status","y");
		} catch (Exception e) {
			map.put("status","n");
			map.put("info","该类别已被使用,不能删除");
		}
		return map;
	}
	/************************************以上是积分项配置********************************************/
	/************************************以下是积分等级配置********************************************/
	
	/**
	 * 积分等级配置列表
	 */
	@RequestMapping("/listJifenLevConfigPage")
	public ModelAndView listJifenLevConfig() {
		List<JifenLev> list = jiFenService.listJifenLevConfig();
		ModelAndView mav = new ModelAndView("jiFen/listJifenLevConfig", "list", list);
		return mav;
	}
	/**
	 * 积分等级配置分页列表
	 */
	@RequestMapping("/listPagedJifenLev")
	public ModelAndView listPagedJifenLev() {
		List<JifenLev> list = jiFenService.listPagedJifenLevConfig();
		ModelAndView mav = new ModelAndView("jiFen/jiFenCenter", "list", list);
		
		UserInfo userInfo = this.getSessionUser();
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
	/**
	 * 跳转积分类型添加页面
	 * @return
	 */
	@RequestMapping("/addJifenLevPage")
	public ModelAndView addJifenLevPage(){
		ModelAndView view = new ModelAndView("jiFen/addJifenLev");
		return view;
	}
	/**
	 * 验证等级名称是否有重复
	 * @param param 填写的参数
	 * @param id 类别主键（添加为0 修改非0）
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validateJifenLevName")
	public Map<String,String> validateJifenLevName(String param,Integer id){
		Map<String,String> map = new HashMap<String, String>();
		//验证等级名称是否有重复
		boolean flag = jiFenService.validateJifenLevName(param,id);
		if(flag){
			map.put("status", "y");
		}else{
			map.put("status", "n");
			map.put("info", "等级名称重复");
		}
		return map;
	}
	/**
	 * 添加积分等级
	 * @param jifenLev
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddJifenLev")
	public Map<String,Object> ajaxAddJifenLev(JifenLev jifenLev){
		Map<String,Object> map = new HashMap<String, Object>();
		Integer id = jiFenService.addJifenLev(jifenLev);
		jifenLev.setId(id);
		map.put("jifenLev",jifenLev);
		return map;
	}
	
	/**
	 * 修改积分等级
	 * @param jifenLev
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxUpdateJifenLev")
	public Map<String,Object> ajaxUpdateJifenLev(JifenLev jifenLev){
		Map<String,Object> map = new HashMap<String, Object>();
		boolean flag = true;
		if(null!=jifenLev.getLevName()){
			flag = jiFenService.validateJifenLevName(jifenLev.getLevName(),jifenLev.getId());
		}
		if(!flag){
			map.put("status","n");
			map.put("info","等级名称不能重复");
		}else{
			jiFenService.updateJifenLev(jifenLev);
			map.put("status","y");
		}
		return map;
	}
	/**
	 * 删除积分等级
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/jifenLevDel")
	public Map<String,Object> jifenLevDel(Integer id){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			jiFenService.delJifenLev(id);
			map.put("status","y");
		} catch (Exception e) {
			map.put("status","n");
			map.put("info","该积分等级已被使用,不能删除");
		}
		return map;
	}
	
	/************************************以上是积分等级配置********************************************/
	
	/**
	 * 积分历史
	 * @return
	 */
	@RequestMapping("/listPagedJifenHistory")
	public ModelAndView listPagedJifenHistory(JifenHistory jifenHistory){
		UserInfo userInfo = this.getSessionUser();
		//所在企业
		jifenHistory.setComId(userInfo.getComId());
		//积分人
		jifenHistory.setUserId(userInfo.getId());
		//积分历史
		List<JifenHistory> list = jiFenService.listPagedJifenHistory(jifenHistory);
		ModelAndView mav = new ModelAndView("jiFen/jiFenCenter", "list", list);
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	/**
	 * 积分数据查看
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/jiFenCenter")
	public ModelAndView listPagedJifenOrder(String type) throws ParseException{
		UserInfo userInfo = this.getSessionUser();
		//积分名次
		List<Jifen> list = jiFenService.listPagedJifenOrder(userInfo.getComId(),type);
		ModelAndView mav = new ModelAndView("/jiFen/jiFenCenter", "list", list);
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
	/**
	 * 计算登录用户的积分排名
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping("/getJiFenOfUser")
	public HttpResult<UserInfo> getJiFenOfUser() throws ParseException{
		//获取当先登录人信息
		UserInfo userInfo = this.getSessionUser();
	
		//总积分排名
		Jifen vo = jiFenService.getUserOrder(userInfo.getComId(),userInfo.getId(),null);
		userInfo.setTotalJifen(vo.getJifenScore());
		userInfo.setTotalRank(vo.getJifenOrder());
		//最近的积分等级
		JifenLev jifenLevMin = jiFenService.getUserJifenLevMin(vo.getJifenScore());
		userInfo.setLevName(jifenLevMin.getLevName());
		userInfo.setMinLevJifen(jifenLevMin.getLevMinScore());
		//最近的下次积分等级
		JifenLev jifenLevNext = jiFenService.getUserJifenLevNext(vo.getJifenScore());
		userInfo.setNextLevJifen(jifenLevNext.getLevMinScore());
		
		//月积分排名
		vo = jiFenService.getUserOrder(userInfo.getComId(),userInfo.getId(),2);
		userInfo.setMonthJifen(vo.getJifenScore());
		userInfo.setMonthRank(vo.getJifenOrder());
		//周积分排名
		vo = jiFenService.getUserOrder(userInfo.getComId(),userInfo.getId(),1);
		userInfo.setWeekJifen(vo.getJifenScore());
		userInfo.setWeekRank(vo.getJifenOrder());
		//计算更人积分与下阶段积分百分比
		userInfo.setJiFenPercent(MathExtend.getPercent((userInfo.getTotalJifen()-userInfo.getMinLevJifen()),(userInfo.getNextLevJifen()-userInfo.getMinLevJifen()), 2));
		return new HttpResult<UserInfo>().ok(userInfo);
	}
}