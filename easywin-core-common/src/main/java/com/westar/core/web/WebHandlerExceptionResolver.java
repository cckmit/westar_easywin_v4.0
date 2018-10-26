package com.westar.core.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class WebHandlerExceptionResolver extends SimpleMappingExceptionResolver {
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception ex) {
		
		//记录异常日志
		Logger logger=Logger.getRootLogger();
		logger.error(ex.getMessage(),ex);
//		if (ex.getClass().getSimpleName().equals("DataIntegrityViolationException")) {
//			return new ModelAndView("/error/500");
//		}else {
//			return super.doResolveException(request, response, object, ex);
//		}
		return new ModelAndView("/error/500");
	}
}
