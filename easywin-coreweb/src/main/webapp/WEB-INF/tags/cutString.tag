<%@tag import="com.westar.base.util.CommonUtil"%><%@tag import="com.westar.base.util.StringUtil"%>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<jsp:doBody var="value"/>
<%@attribute name="num" required="true" rtexprvalue="true"%>
<%@attribute name="escapeXml" required="false" rtexprvalue="true"%>
<%=StringUtil.cutString(jspContext.getAttribute("value").toString(),Integer.valueOf(jspContext.getAttribute("num").toString()),jspContext.getAttribute("escapeXml")+"")%>