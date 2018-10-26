package com.westar.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.westar.core.web.TokenManager;



public class TokenInterceptor extends HandlerInterceptorAdapter {
	

	/**
	 * 检查令牌 防止重复登录
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		boolean bool=true;
		String token=request.getParameter(TokenManager.TOKEN_PARAM);
		try {
			if(!StringUtils.isEmpty(token)){
				if(TokenManager.hasToken(request, token)){
					TokenManager.destroyToken(request, token);
				}else{
					bool=false;
					throw new Exception("令牌重复或者令牌已经期，令牌："+token);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return bool;
	}
}
