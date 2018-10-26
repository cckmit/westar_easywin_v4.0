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

<title>捷成移动协作APP下载页面</title>

<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta http-equiv="description" content="捷成移动协作平台">
<!--
<link rel="stylesheet" type="text/css" href="styles.css">
-->
<style type="text/css">
* {
	margin: 0;
	padding: 0;
	list-style: none;
}

.download-head {
	height: 35px;
	border-bottom: 1px solid #e7e7e7;
	padding: 10px;
}

.logo {
	width: 120px;
	height: auto;
}

.logo img {
	width: 100%;
}

.download-box {
	border-bottom: 1px solid #eee;
}

.download-logo {
	width: 40%;
	margin: 20px auto 0;
}

.download-logo img {
	width: 100%;
}

.download-btn {
	display: block;
	width: 50%;
	margin: 10px auto 20px;
	border: 1px solid #ddd;
	background: #f5f5f5;
	padding: 3% 0;
	text-align: center;
	color: #939799;
}

.download-text {
	width: 50%;
	margin: 20px auto;
	text-align: center;
	color: #939799;
}

.download-footer {
	text-align: center;
	color: #999;
	font-size: 14px;
	padding: 15px 0;
}
</style>
</head>

<body>
	<div class="download-head">
		<h1 class="logo">
			<img src="/static/images/logo.png" />
		</h1>
	</div>
	<div class="apple-download download-box" style="display:none">
		<div class="download-logo">
			<img src="/static/images/apple.png" />
		</div>
		<a class="download-btn" href="#">APP Store下载</a>
	</div>
	<div class="apple-download download-box">
		<div class="download-logo">
			<img src="/static/images/android.png" />
		</div>
		<a class="download-btn" href="${appLoadUrl}">Android下载</a>
	</div>
	<div class="apple-download download-box" style="display:none">
		<div class="download-logo">
			<img src="/static/images/ewm.png" />
		</div>
		<div class="download-text">
			<h4>扫描二维码</h4>
			<p>直接下载</p>
		</div>
	</div>
	<div class="download-footer">版权所有：成都西辰软件</div>
</body>
</html>
