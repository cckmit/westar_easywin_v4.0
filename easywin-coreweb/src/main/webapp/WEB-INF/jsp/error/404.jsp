<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" isErrorPage="true"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="java.io.PrintWriter"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
	<style type="text/css">
		.error-bg{ background:url(../static/images/error/error-bg.png) no-repeat center top #e1f8ff;}
		.error-message{width:50%;margin:180px auto 0; min-height:550px;text-align:center;}
		.error-orange{ font-size: 30px;color:#fa6e24; padding:30px 0;}
		.other-error-text{ font-size: 16px;color:#555;}
		.other-error-text p{font-size: 16px;margin:0; line-height: 25px;color:blue;}
	</style>
</head>
<body class="error-bg">
	<div class="container error-message text-center">
		<div class="sad">
			<img src="<%=basePath%>static/images/error/404.png"/ style="width:186px;height:161px;">
		</div>
		<div class="text-center error-orange">
			访问资源不存在，或资源已被删除。
		</div>
		<div class="text-center other-error-text">
			<a href="javascript:void(0);" onclick="history.go(-1);"><p>返回上级页面</p></a>
		</div>
	</div>
</body>
</html>
