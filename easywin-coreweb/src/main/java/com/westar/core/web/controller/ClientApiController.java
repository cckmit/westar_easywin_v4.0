package com.westar.core.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Organic;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.UserAuth;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.ClientApiService;
import com.westar.core.web.PaginationContext;;

@Controller
@RequestMapping("clientApi")
public class ClientApiController extends BaseController {
	
	@Autowired
	ClientApiService clientApiService;
	
	  /**
     * 默认首先获取用户信息
     * 
     * @param request
     * @return UserInfo
     */
	@ModelAttribute("u_context")
	public UserInfo getUserContext(HttpServletRequest request) {
		return (UserInfo) request
				.getAttribute(CommonConstant.APP_USER_CONTEXT_KEY);
	}
	
	
	/**
	 * 验证登录人是否合理以及获取参加的所有团队集合
	 * 
	 * @param loginName
	 *            帐号
	 * @param password
	 *            密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listUserOrg", method = RequestMethod.POST)
	public HttpResult<List<Organic>> listUserOrg(String account,
			String password) {
		HttpResult<List<Organic>> httpResult = clientApiService.listUserOrg(account, password);// 返回对象
		return httpResult;
	}

    /**
     * 用户登录验证
     * 
     * @param account
     * @param password
     * @param request
     * @return
     * @throws Exception
     *             HttpResult<UserInfo>
     */
    @ResponseBody
    @RequestMapping(value = "/userAuth", method = RequestMethod.POST)
    public HttpResult<UserAuth> userAuth(String account, String password,String comId, HttpServletRequest request) throws Exception {
        // 验证参数的合法性|| !account.matches("[A-Za-z0-9_]{4,20}") 
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return new HttpResult<>("非法参数！");
        }
        return clientApiService.userAuth(account,password,comId);
    }

    /**
     * 获取IP
     * 
     * @param request
     * @return String
     */
    @SuppressWarnings("unused")
	private String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 根据用户id分页查询系统消息（未读且未过期系统消息）
     * 
     * @param userId
     * @return
     * @throws Exception
     *             HttpResult<List<TodayWorks>>
     */
    @ResponseBody
    @RequestMapping("/listTodayWorks")
    public HttpResult<List<TodayWorks>> listPagedMessage(@ModelAttribute("u_context") UserInfo userInfo)
            throws Exception {
        // 根据用户id查询未读且未过期系统消息列表
		//查询待办事项		
    	TodayWorks unReadWorks = new TodayWorks();
    	unReadWorks.setReadState(ConstantInterface.COMMON_DEF);
    	// 一次加载行数
		PaginationContext.setPageSize(5);
		// 列表数据起始索引位置
		List<TodayWorks> list = clientApiService.listPagedMsgTodo(unReadWorks,userInfo.getComId(), userInfo.getId(),null);
        if (list != null && list.size() > 0) {
            List<TodayWorks> listTodayWorks = new ArrayList<TodayWorks>();
            for (TodayWorks message : list) {
                TodayWorks todayWorks = new TodayWorks();
                BeanUtils.copyProperties(message,todayWorks);
                listTodayWorks.add(todayWorks);
            }
            return new HttpResult<List<TodayWorks>>(listTodayWorks);
        } else {
            return new HttpResult<List<TodayWorks>>(new ArrayList<TodayWorks>());
        }
    }
    
    /**
     * 标记指定消息为已读
     * 
     * @param messageId
     * @return HttpResult
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping("/updateMessageReaded")
    public HttpResult updateMessageReaded(@ModelAttribute("u_context") UserInfo userInfo,Integer messageId) {
    	clientApiService.updateMessageReaded(userInfo.getComId(),messageId);
        return new HttpResult();
    }

    /**
     * 后台首页跳转
     * 
     * @param request
     * @return HttpResult
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ResponseBody
    @RequestMapping("/homePage")
    public HttpResult homePage(HttpServletRequest request) {
        String encodeURL = "/loginFromCs";
        return new HttpResult(HttpResult.CODE_OK,null,encodeURL);
    }
    
}
