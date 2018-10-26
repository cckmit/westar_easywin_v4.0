<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<jsp:doBody var="value"/>
<%=StringUtil.toHtml((String)jspContext.getAttribute("value"))%>