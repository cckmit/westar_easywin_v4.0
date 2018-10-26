package com.westar.base.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 在代码任何地方都能获取当前Request
 */
public class RequestContextHolderUtil {

	/**
	 * 获取request
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		request.getSession().getServletContext();
		return request;
	}
	
	/**
	 * 新建session 使用此方法
	 * 获取session
	 * @return
	 */
	public static HttpSession getSession() {
		HttpSession session = getRequest().getSession();//没有获取到对象时，会主动创建一个对象
		return session;
	}
	
	/**
	 * 在session 中获取数据时，使用此方法获取session对象
	 * @return
	 */
	public static HttpSession getSessionForGet() {
		HttpSession session = getRequest().getSession(false);//没获取到对象时，直接返回NULL
		return session;
	}
	
	/**
	 * 获取ServletContext
	 * @return
	 */
	public static ServletContext getServletContext(){
		ServletContext servletContext=getSession().getServletContext();
		return servletContext;
	}
	
}
