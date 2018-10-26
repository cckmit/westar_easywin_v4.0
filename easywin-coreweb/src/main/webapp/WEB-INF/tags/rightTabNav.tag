<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@tag import="com.westar.base.model.Menu"%>
<%@tag import="java.util.List"%>
<%@tag import="com.westar.core.service.MenuService"%>
<%@tag import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@tag import="org.springframework.context.ApplicationContext"%>
<%@attribute name="parentAuthCode" required="true" rtexprvalue="true"%>
<%@attribute name="currentAuthCode" required="true" rtexprvalue="true"%>
<%@attribute name="userId" required="true" rtexprvalue="true"%>
	<%
	ApplicationContext applicationContext=WebApplicationContextUtils.getWebApplicationContext(application);
	MenuService menuService=(MenuService)applicationContext.getBean(MenuService.class);
	List<Menu> list=menuService.listMenuByParentAuthCode(parentAuthCode,Integer.parseInt(userId));
	if(list!=null&&list.size()>0){
	%>
	<div class="right_menu">
 	<span>
	<% 
		for(int i=0;i<list.size();i++){
		Menu menu=list.get(i);
	%>
	<a href="javascript:void(0)" onclick="window.location.href='<%=menu.getUrl() %>?sid=${param.sid }'"  <%if(menu.getAuthCode().equals(currentAuthCode)){out.print("class=\"current\"");} %> ><%=menu.getMenuName() %></a>
	<%
		}
	%>
	 </span>
	</div>
	<%
	}
	%>