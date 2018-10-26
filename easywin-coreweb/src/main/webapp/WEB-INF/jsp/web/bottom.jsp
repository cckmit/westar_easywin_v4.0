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
<meta http-equiv="description" content="网站底部">
<!--
<link rel="stylesheet" type="text/css" href="styles.css">
-->
</head>
<body>
	<div class="bottom-con">
		<p>
			<a href="javascript:void(0)" target="_blank">成都西辰软件有限公司</a> &nbsp;&nbsp;联系电话：028-85139468
		</p>
		<p>地址:成都市高新区科园三路4号火炬时代B区7层</p>
		<!-- <p>ICP备案：蜀ICP备05002048号-1</p> -->
	</div>
</body>
</html>