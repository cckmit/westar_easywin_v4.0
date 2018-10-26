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
		<!--
		<ul class="pagination pagination-lg pull-left">
		<li>
		<pg:page>
			<li>共<font><%=totalItems %></font>条记录&nbsp;</li>
		</pg:page>
		</li>
		</ul>
		-->
		 <div class="panel-body ps-page bg-white">
		 <p class="pull-left ps-pageText">共<b class="badge"><%=totalItems %></b>条记录</p>
		 
		 <ul class="pagination pull-right">
		 
		<pg:first><li><a href="<%=pageUrl %>">首页</a></li>&nbsp;</pg:first>
		<pg:prev><li><a href="<%=pageUrl %>">«</a></li>&nbsp;</pg:prev>
		<pg:pages>
		<%
			if(pageNumber == currentPageNumber){
		%>
		<li class="active"><a href="<%=pageUrl %>"><%=pageNumber %></a></li>&nbsp;
		<%
			}else{
		%>
		<li><a href="<%=pageUrl %>"><%=pageNumber %></a></li>&nbsp;
		<%
			}
		%>
		</pg:pages>
		<pg:next><li><a href="<%=pageUrl %>">»</a>&nbsp;</li></pg:next>
		<pg:last><li><a href="<%=pageUrl %>">末页</a>&nbsp;</li></pg:last>
		</ul>
		</div>
		</pg:index>
	</pg:pager>