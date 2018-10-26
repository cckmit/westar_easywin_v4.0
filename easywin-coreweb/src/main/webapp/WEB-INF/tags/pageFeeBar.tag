<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@tag import="java.util.Set"%>
<%@tag import="java.util.Map"%>
<%@tag import="com.westar.base.cons.CommonConstant"%>
<%@tag import="com.westar.core.web.PaginationContext"%>
<%@taglib prefix="pg" uri="/WEB-INF/tld/taglib.tld"%> 
<%@attribute name="url" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@attribute name="maxIndexPages" required="false" rtexprvalue="true" type="java.lang.Integer"%>
	<pg:pager items="<%=PaginationContext.getTotalCount() %>"  
			  maxPageItems="<%=PaginationContext.getPageSize() %>" 
			  maxIndexPages="<%=(null==maxIndexPages)?5:maxIndexPages%>" 
			  export="currentPageNumber=pageNumber"  
			  url="<%=url %>">
		<%
			Map paramMap=request.getParameterMap();
			for(Object key:paramMap.keySet()){
				String s_key=(String)key;
				String[] os = (String[])paramMap.get(s_key);
				if(!s_key.equals(CommonConstant.PAGINATION_OFFSET)){
		%>
		<pg:param name="<%=s_key %>"/>
		<% 
				}
			}
		%>
		<pg:index export="totalItems=itemCount">
		  <div class="pagination-wrapper">
		  	<span>
			 共<font><%=totalItems %></font>条记录&nbsp;
		  	</span>
		<pg:first><a href="<%=pageUrl %>">首页</a>&nbsp;</pg:first>
		<pg:prev><a href="<%=pageUrl %>">«</a>&nbsp;</pg:prev>
		<pg:pages>
		<%
			if(pageNumber == currentPageNumber){
		%>
		<a href="<%=pageUrl %>" class="active"><%=pageNumber %></a>&nbsp;
		<%
			}else{
		%>
		<a href="<%=pageUrl %>"><%=pageNumber %></a>&nbsp;
		<%
			}
		%>
		</pg:pages>
		<pg:next><a href="<%=pageUrl %>">»</a>&nbsp;</pg:next>
		<pg:last><a href="<%=pageUrl %>">末页</a>&nbsp;</pg:last>
		</div>
		</pg:index>
	</pg:pager>