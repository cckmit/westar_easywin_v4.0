<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="网站头部">
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
</head>
<body>
	<div class="top-con">
		<div class="logo"></div>
		<div class="top-nav">
			<ul>
				<li><a href="/web/index?sorcePage=mine">首页</a>
				<li><a href="/web/index?sorcePage=other" id="index-module">功能</a></li>
				</li>

				<li><a href="/web/helpContent">帮助</a></li>
				<li class="nav-btn login-btn"><a href="/web/login">登录</a></li>
				<li class="nav-btn zc-btn"><a href="/registe/registeInfoPage">注册</a></li>
			</ul>
		</div>
	</div>
</body>
</html>