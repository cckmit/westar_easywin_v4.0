<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<link href="/static/2015/style/comm.css" rel="stylesheet" type="text/css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
</head>
<body style="height: 100%">
<div class="main w_width" style="background: green;margin-left: auto;margin-right: auto;height: 100%">
   <div class="main_bg w_width" style="height: 100%">
       <div class="wrap">
		<table style="margin-top: 0px;height: 100%" cellpadding="0" cellspacing="0" width="100%" height="99%" border="0">
		<tr>
			<td width="125px;" height="100%">
			<iframe src="/bodyLeft?sid=${param.sid}" width="100%" height="100%" border="0" frameborder="0" scrolling="yes" allowTransparency="true" onload="this.height=iframe_body_left.document.body.scrollHeight" id="iframe_body_left" name="iframe_body_left"></iframe>
			</td>
			<td height="100%">
			  <iframe src="/bodyRight?sid=${param.sid}" width="100%" height="100%" border="0" frameborder="0" scrolling="yes" allowTransparency="true" onload="this.height=iframe_body_right.document.body.scrollHeight" id="iframe_body_right" name="iframe_body_right"></iframe>
			</td>
		</tr>
		</table>
		</div>
	</div>
</div>
</body>
<script>
</script>
</html>

