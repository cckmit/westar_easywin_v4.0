package com.westar.core.web.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.SpFlowHiStep;
import com.westar.base.model.SpFlowModel;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.ModFlowService;

/**
 * 
 * 描述:非审批模块的流程处理
 * @author zzq
 * @date 2018年3月22日 上午9:58:48
 */
@Controller
@RequestMapping("/modFlow")
public class ModFlowController extends BaseController {
	
	@Autowired
	ModFlowService modFlowService;
	/**
	 * 异步取得模块需要选择的流程
	 * @param busType 模块类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listModSpFlowByAjax")
	public Map<String, Object> listModSpFlowByAjax(String busType){
		 Map<String, Object> map = new HashMap<String, Object>();
		 //当前操作人员
		 UserInfo sessionUser = this.getSessionUser();
		 if(null == sessionUser){
			 map.put("status", "f");
			 map.put("info", CommonConstant.OFF_LINE_INFO);
			 return map;
		 }
		 //查询模块使用的固定流程
		 List<SpFlowModel> spFlowModels = modFlowService.listModSpFlow(sessionUser,busType);
		 if(!CommonUtil.isNull(spFlowModels)){
			 map.put("status", ConstantInterface.STATUS_Y);
		 }else{
			 map.put("status", ConstantInterface.STATUS_N);
		 }
		 map.put("list", spFlowModels);
		 return map;
	}
	/**
	 * 跳转到模块审批流程选择界面
	 * @return
	 */
	@RequestMapping(value="/listModFlowForSelect")
	public ModelAndView listModFlowForSelect(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/modflow/listModFlowForSelect");
		return mav;
	}
	
	/**
	 * 模块流程发起的流程展示
	 * @param flowId 采用的流程
	 * @return
	 */
	@RequestMapping(value="/startModFlowStepCfg")
	public ModelAndView startModFlowStepCfg(Integer flowId){
		ModelAndView view = new ModelAndView("/modflow/spFlowStepCfg");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
//		//流程实例配置信息
//		String flowXml = modFlowService.queryModFlowXml(userInfo,flowId);
//		view.addObject("flowxml",flowXml);
		//当前时间
		view.addObject("nowtime", DateTimeUtil.getNowDateStr(DateTimeUtil.yyyyMMddHHmmssSSS));
		
		return view;
	}
	
	/**
	 * 跳转流程提交展示界面
	 * @return
	 */
	@RequestMapping(value="/spFlowNextStepPage")
	public ModelAndView spFLowNextStepPage() {
		ModelAndView view = new ModelAndView("/modflow/spFlowNextStep");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 查询流程模板的开始节点的下一步骤
	 * @param flowId 使用的流程模块主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listModFlowStartNextStep")
	public Map<String, Object> listModFlowStartNextStep(Integer flowId){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//在添加业务模块界面，显示下一步配置信息，此时还没有复制配置信息
		SpFlowHiStep stepInfo =  modFlowService.queryFlowStartNextStep(sessionUser,flowId);
		map.put("status", "y");
		map.put("stepInfo", stepInfo);
		return map;
	}
	
	/**
	 * 异步取得流程数据信息
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param actInstaceId 流程引擎数理化主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxSpFlowNextStep",method = RequestMethod.POST)
	public Map<String,Object> ajaxSpFlowNextStep(Integer busId,String busType,String actInstaceId){
	
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//流程发起后，此时提交审批数据，显示下一步配置信息
		SpFlowHiStep stepInfo = modFlowService.querySpFlowNextStepInfo(userInfo,busId,busType,actInstaceId);//获取流程下一步步骤信息
		map.put("stepInfo",stepInfo);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 修改审批的办理人员
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param actInstaceId 流程部署实例化主键
	 * @param modFormStepDataStr 本次转办参数信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSpInsAssign")
	public Map<String,Object> updateSpInsAssign(Integer busId,String busType,String actInstaceId,String modFormStepDataStr){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map =  modFlowService.updateSpInsAssign(userInfo,busId,busType,actInstaceId,modFormStepDataStr);
		return map;
	}
	/**
	 * 自由回退
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param actInstaceId
	 * @param modFormStepDataStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSpInsBack")
	public Map<String,Object> updateSpInsBack(Integer busId,String busType,String actInstaceId,
			String modFormStepDataStr){
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = modFlowService.updateSpInsBack(userInfo,busId,busType,actInstaceId,modFormStepDataStr);
		} catch (Exception e) {
			map.put("status", "f");
			map.put("info", "流程错误！");
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 提交流程
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param actInstaceId 流程部署主键
	 * @param modFormStepDataStr 提交流程信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSpModFlow")
	public Map<String,Object> updateSpModFlow(Integer busId,String busType,String actInstaceId,
			String modFormStepDataStr){
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = modFlowService.updateSpModFlow(userInfo,busId,busType,actInstaceId,modFormStepDataStr);
		} catch (Exception e) {
			map.put("status", "f");
			map.put("info", "流程错误！");
		}
		return map;
	}
	
	/**
	 * 拾取审批事项
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param actInstaceId 流程部署主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pickSpFlowTask")
	public Map<String,Object> pickSpFlowTask(Integer busId,String busType,String actInstaceId){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		map = modFlowService.updatePickSpFlowTask(userInfo,busId,busType,actInstaceId);
		return map;
	}
	
	/**
	 * 获取已执行的审批步骤集合
	 * @param instanceId 流程实例主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListHistorySpStep")
	public Map<String,Object> ajaxListHistorySpStep(Integer busId,String busType,String actInstaceId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		List<SpFlowHiStep> listHistorySpStep = modFlowService.listHistorySpStep(userInfo,busId,busType,actInstaceId);
		map.put("listHistorySpStep", listHistorySpStep);
		map.put("status", "y");
		return map;
	}
	/**
	 * 流程审批记录
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param doneState 是否办结
	 * @return
	 */
	@RequestMapping(value="/listSpHistory")
	public ModelAndView listSpHistory(Integer busId,String busType,Integer doneState){
		ModelAndView view = new ModelAndView("/modflow/listSpHistory");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		
		//流程审批历史信息
		List<SpFlowHiStep> listSpFlowHiStep = modFlowService.listSpFlowHiStep(userInfo,busId,busType);
		
		if(null!= listSpFlowHiStep && !listSpFlowHiStep.isEmpty() && doneState == 0 ){//未完结,倒叙
			Collections.reverse(listSpFlowHiStep);
		}
		view.addObject("listSpFlowHiStep",listSpFlowHiStep);
		
		return view;
	}
	/**
	 * 查询流程关联附件
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	@RequestMapping(value="/listSpFiles")
	public ModelAndView listSpFiles(Integer busId,String busType){
		ModelAndView view = new ModelAndView("/modflow/listSpFiles");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		//查询流程关联附件
		List<SpFlowUpfile> listSpFiles = modFlowService.listPagedSpFiles(userInfo,busId,busType);
		view.addObject("listSpFiles",listSpFiles);
		return view;
	}
	/**
	 * 审批人员转办跳转界面
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param actInstaceId 流程实例化主键
	 * @return
	 */
	@RequestMapping("/spInsUserSetPage")
	public ModelAndView spInsUserSetPage(Integer busId,String busType,String actInstaceId){
		ModelAndView view = new ModelAndView("/modflow/spInsUserSet");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		return view;
	}
	
	/**
	 * 会签人员设置界面
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param actInstaceId 流程实例化主键
	 * @return
	 */
	@RequestMapping("/jointProcessUserSetPage")
	public ModelAndView jointProcessUserSetPage(Integer busId,String busType,String actInstaceId){
		ModelAndView view = new ModelAndView("/modflow/jointProcessUserSet");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 跳转流程提交展示界面
	 * @return
	 */
	@RequestMapping(value="/spFlowIdeaPage")
	public ModelAndView spFlowIdeaPage() {
		ModelAndView view = new ModelAndView("/modflow/spFlowIdea");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	
}
