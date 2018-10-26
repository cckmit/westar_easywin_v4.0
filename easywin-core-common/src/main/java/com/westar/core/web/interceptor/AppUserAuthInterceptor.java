package com.westar.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.UserInfo;
import com.westar.core.web.AppAuthKeyManager;

/**
 * 手机客户端访问权限验证
 */
public class AppUserAuthInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * 验证authKey
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		String authKey=request.getHeader(CommonConstant.APP_AUTH_KEY);
		if(StringUtils.isNotBlank(authKey)){
			if(AppAuthKeyManager.valAuthKey(authKey)){
				UserInfo u=AppAuthKeyManager.getUserInfo(authKey);
				UserInfo userForCopy=new UserInfo();
				BeanUtils.copyProperties(u,userForCopy);
				request.setAttribute(CommonConstant.APP_USER_CONTEXT_KEY,userForCopy);
				request.setAttribute("authKey",authKey);
				return true;
			}
		}
		//设置状态码为401  表示请求未被授权
		response.setStatus(401);
		return false;
	}
	
}
