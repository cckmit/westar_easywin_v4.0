package com.westar.core.web.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.westar.base.cons.CommonConstant;
import com.westar.base.cons.SessionKeyConstant;
import com.westar.base.util.MACUtil;
import com.westar.base.util.RequestContextHolderUtil;

/**
 * 
 * 描述: 用户唯一标识控制类
 * @author zzq
 * @date 2018年8月25日 上午11:52:48
 */
public class UserAuthInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 用户登录验证失败后跳转的URI
	 */
	private String redirectUri;

	/**
	 * 不需要登录即可访问的URI资源
	 */
	private List<String> excludedUris;

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public void setExcludedUris(List<String> excludedUris) {
		this.excludedUris = excludedUris;
	}

	/**
	 * 检查用户是否登录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
 		String requestURI = request.getRequestURI();
		String sid = request.getParameter("sid");
		if (!StringUtils.isEmpty(requestURI)) {
			if (!requestURI.startsWith(CommonConstant.STATIC_PATH) && !requestURI.startsWith(CommonConstant.WEB_CONTROLLER) 
					&& !requestURI.startsWith(CommonConstant.WEB_PAGE) && !requestURI.startsWith(CommonConstant.FLEX_PATH) 
					&& !requestURI.startsWith(CommonConstant.UPLOAD_PATH)) { // 排除所有对静态资源的访问
				Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) RequestContextHolderUtil.getSession().getAttribute(SessionKeyConstant.SESSION_CONTEXT);
				if (map == null) {
					map = new HashMap<String, Map<String, Object>>();
				}
				Map<String, Object> m = map.get(SessionKeyConstant.USER_CONTEXT);
				if (m == null) {
					m = new HashMap<String, Object>();
				}
				if (!StringUtils.isBlank(sid)) {
					/* AJAX提交访问不做处理 在程序里面判断即可 */
					if (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
						return true;
					}
					/* 对已经登录的访问不做处理 */
					if (m.get(sid) != null) {
						
						boolean flag = MACUtil.checkSysLic();
						if(!flag && !"/login/logout4MAC".equals(requestURI)){
							response.sendRedirect("/login/logout4MAC?sid="+sid);
							return false;
						}
						return true;
					}
				} else {
					for (final String uri : excludedUris) { // 排除不需要登录即可访问的URI资源
						if (requestURI.equals(uri)) {
							return true;
						}else if(requestURI.startsWith(uri)){//以XXX开头的URI资源
							return true;
						}
					}
				}
			} else {
				return true;
			}
			
		}
		response.sendRedirect(redirectUri+"?sid="+sid);
		return false;
	}
}
