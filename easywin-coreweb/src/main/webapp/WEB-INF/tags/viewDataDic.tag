<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@tag import="com.westar.core.web.DataDicContext"%>
<%@attribute name="type" required="true" rtexprvalue="true"%>
<%@attribute name="code" required="true" rtexprvalue="true"%>
<%
	String pathZvalue=DataDicContext.getInstance().getCurrentPathZvalue(type,code);
	out.print(pathZvalue);
%>
