<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@tag import="com.westar.core.service.MenuService"%>
<%@tag import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@tag import="org.springframework.context.ApplicationContext"%>
<%@attribute name="authCode" required="true" rtexprvalue="true"%>
<%@attribute name="userId" required="true" rtexprvalue="true"%>
<%
	ApplicationContext applicationContext=WebApplicationContextUtils.getWebApplicationContext(application);
	MenuService menuService=(MenuService)applicationContext.getBean(MenuService.class);
	if(menuService.authAccess(authCode,Integer.parseInt(userId))){
%>
<jsp:doBody></jsp:doBody>
<%
	}
%>