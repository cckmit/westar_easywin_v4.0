package com.westar.core.web.controller;
import java.util.Arrays;
import java.util.List;

import com.westar.base.cons.SystemStrConstant;
import com.westar.base.model.Item;
import com.westar.base.model.Upfiles;
import com.westar.base.util.Encodes;
import com.westar.base.util.FileUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.service.FileCenterService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.FunctionList;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.core.service.FunctionListService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/functionList")
public class FunctionListController extends BaseController{

	@Autowired
	FunctionListService functionListService;

	@Autowired
	FileCenterService fileCenterService;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Encodes.class);
	/**
	 * 功能清单页面
	 * @author hcj 
	 * @date: 2018年10月17日 上午11:48:25
	 * @param functionList
	 * @return
	 */
	@RequestMapping(value = "/functionListPage")
	public ModelAndView functionListPage(FunctionList functionList) {
		ModelAndView view = new ModelAndView("/common/functionList/functionList");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		view.addObject("functionList",functionList);
		return view;
	}
	
	/**
	 * 功能清单树页面
	 * @author hcj 
	 * @date: 2018年10月17日 上午11:48:56
	 * @param functionList
	 * @return
	 */
	@RequestMapping(value = "/treeFunPage")
	public ModelAndView treeFunPage(FunctionList functionList,String fromType) {
		ModelAndView view = new ModelAndView("/common/functionList/treeFun");
		if(!CommonUtil.isNull(fromType) && "view".equals(fromType)){
			view = new ModelAndView("/common/functionList/treeFunView");
		}
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		view.addObject("functionList",functionList);
		return view;
	}
	
	/**
	 * 获取功能清单树
	 * @author hcj 
	 * @date: 2018年10月17日 下午1:18:26
	 * @param functionList
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listTreeFun")
	public List<FunctionList> listTreeFun(FunctionList functionList) {
		UserInfo userInfo = this.getSessionUser();
		List<FunctionList> list = functionListService.listTreeFun(userInfo,functionList);
		return list;
	}
	
	/**
	 * 新增功能页面
	 * @author hcj 
	 * @date: 2018年10月17日 下午2:23:52
	 * @param functionList
	 * @return
	 */
	@RequestMapping(value = "/addFunPage")
	public ModelAndView addFunPage(FunctionList functionList) {
		ModelAndView view = new ModelAndView("/common/functionList/addFun");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		view.addObject("functionList",functionList);
		return view;
	}
	
	/**
	 * 新增功能
	 * @author hcj 
	 * @date: 2018年10月17日 下午3:22:19
	 * @param functionList
	 * @return
	 */
	@RequestMapping(value = "/addFunction")
	public ModelAndView addFunction(FunctionList functionList) {
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		functionListService.addFunction(functionList,userInfo);
		this.setNotification(Notification.SUCCESS, "添加成功");
		return view;
	}
	
	/**
	 * 更新功能信息页面
	 * @author hcj 
	 * @date: 2018年10月17日 下午4:11:38
	 * @param functionList
	 * @return
	 */
	@RequestMapping(value = "/updateFunPage")
	public ModelAndView updateFunPage(FunctionList functionList) {
		ModelAndView view = new ModelAndView("/common/functionList/editFun");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		FunctionList functionListT = functionListService.queryFunById(functionList.getId());
		functionListT.setIframe(functionList.getIframe());
		view.addObject("functionList",functionListT);
		return view;
	}
	/**
	 * 更新功能
	 * @author hcj 
	 * @date: 2018年10月17日 下午4:29:44
	 * @param functionList
	 * @return
	 */
	@RequestMapping(value = "/updateFunction")
	public ModelAndView updateFunction(FunctionList functionList) {
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		functionListService.updateFunction(functionList,userInfo);
		this.setNotification(Notification.SUCCESS, "更新成功");
		return view;
	}
	
	/**
	 * 删除选中功能
	 * @author hcj 
	 * @date: 2018年10月17日 下午4:49:14
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delFunction")
	public HttpResult<String> delFunction(Integer[] ids){
		UserInfo userInfo = this.getSessionUser();
		functionListService.delFunction(ids, userInfo);
		return new HttpResult<String>().ok("删除成功");
	}
	
	/**
	 * 功能单选页面
	 * @author hcj 
	 * @date: 2018年10月17日 下午5:17:06
	 * @param functionList
	 * @return
	 */
	@RequestMapping(value = "/funOnePage")
	public ModelAndView funOnePage(FunctionList functionList) {
		ModelAndView view = new ModelAndView("/common/functionList/funOne");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		view.addObject("functionList",functionList);
		return view;
	}

	/**
	 * 批量导入功能页面跳转
	 * @param busType
	 * @return
	 */
	@RequestMapping("/functionImportPage")
	public ModelAndView functionImportPage(String busType,Integer busId,String upMsg){
		ModelAndView mav = new ModelAndView("/common/functionList/functionImport");
		mav.addObject("busType",busType);
		mav.addObject("busId",busId);
		mav.addObject("upMsg",upMsg);
		return mav;
	}


	/**
	 * 功能导入清单
	 * @param functionList
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/functionImport")
	public ModelAndView functionImport(FunctionList functionList,String redirectPage){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView(LAYER_REFRESH_SELF);
		mav.addObject("userInfo",userInfo);
		mav.addObject("redirectPage",redirectPage);
        HttpResult<Integer> result = new HttpResult();
		if(null != functionList && null != functionList.getListUpfiles() && functionList.getListUpfiles().size() > 0){
			String basepath = FileUtil.getUploadBasePath();
			basepath = basepath.replaceAll("\\\\", "/");
			basepath = basepath.replaceAll("//", "/");
            Upfiles upfiles = new Upfiles();
			for(int i=0;i<functionList.getListUpfiles().size();i++){
				upfiles = fileCenterService.getUpfilesById(functionList.getListUpfiles().get(i).getId());
				if(!upfiles.getFileExt().equals("xls") && !upfiles.getFileExt().equals("xlsx")){
					this.setNotification(Notification.ERROR,"文件格式不正确，请上传xls或xlsx表格。");
					return mav;
				}

				upfiles.setFilepath(upfiles.getFilepath().replaceAll("\\\\", "/"));
				result = functionListService.readExcel(basepath + upfiles.getFilepath(),userInfo,functionList.getBusId(),functionList.getBusType());
				result.setData(i + 1);
				if(result.getCode() == -1){
                    logger.error("读取文件[" + upfiles.getFilename() + "]失败-" + result.getMsg());
                    mav.addObject("redirectPage",replaceValue("upMsg",
                            "读取文件[" + upfiles.getFilename() + "]失败-" + result.getMsg(),redirectPage));
				    break;
                }
			}

            if(result.getCode() == 0){
                this.setNotification(Notification.SUCCESS,result.getMsg());
            }
		}
		return mav;
    }

    /**
     * 替换url中的参数值
     * @param key 参数名
     * @param value 需要替换的值
     * @param recordPage 处理的url
     * @return
     */
    public static String replaceValue(String key,String value,String recordPage){
	    String s = "";
	    if(null != recordPage && !"".equals(recordPage)){
	        String[] map = recordPage.split("&");
	        if(null != map && map.length > 0){
	            boolean isExists = false;
	            for(int i = 0;i<map.length;i++){
	                if(map[i].split("=")[0].equals(key)){
	                    s = s + "&key=" + value;
                        isExists = true;
                    }else{
	                    s = s + "&" + map[i];
                    }
                }
                if(!isExists){
	                s = recordPage + "&" + key + "=" + value;
                    return s;
                }
            }else{
	            return recordPage;
            }
            return recordPage;
        }
        return s;
    }

	/**
	 * 导入功能清单
	 * @author hcj 
	 * @date: 2018年10月18日 上午10:31:55
	 * @param functionList
	 * @param chooseBusType
	 * @param chooseBusId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importFunctionList")
	public HttpResult<String> importFunctionList(FunctionList functionList,String chooseBusType,Integer chooseBusId){
		UserInfo userInfo = this.getSessionUser();
		functionListService.addImportFunctionList(functionList.getBusId(),functionList.getBusType(),chooseBusType,chooseBusId, userInfo);
		return new HttpResult<String>().ok("导入成功");
	}
	
}