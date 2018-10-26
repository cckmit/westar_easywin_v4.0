<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@tag import="com.westar.core.web.TokenManager"%>
<input type="hidden" name="<%=TokenManager.TOKEN_PARAM %>" value="<%=TokenManager.newToken(request) %>" />
<input type="hidden" name="sid" value="${param.sid }" />
