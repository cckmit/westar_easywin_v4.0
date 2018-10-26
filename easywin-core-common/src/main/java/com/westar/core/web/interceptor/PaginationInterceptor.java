package com.westar.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.westar.base.cons.CommonConstant;
import com.westar.core.web.PaginationContext;


/**
 * 
 * 描述:分页过滤器
 * @author zzq
 * @date 2018年8月25日 上午11:53:18
 */
public class PaginationInterceptor extends HandlerInterceptorAdapter {
	

	/**
	 * 设置本次请求的分页参数
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		String offset=request.getParameter(CommonConstant.PAGINATION_OFFSET);
		String pageSize=request.getParameter(CommonConstant.PAGINATION_PAGESIZE);
		if(offset!=null){
			PaginationContext.setOffset(Integer.parseInt(offset));
		}
		if (pageSize!=null) {
			PaginationContext.setPageSize(Integer.parseInt(pageSize));
		}
		return true;
	}
	
	/**
	 * 销毁本次请求的分页参数
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		PaginationContext.removeOffset();
		PaginationContext.removePageSize();
		PaginationContext.removeTotalCount();
		super.afterCompletion(request, response, handler, ex);
	}
}
