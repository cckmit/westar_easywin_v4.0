<%@page language="java" import="java.util.*" pageEncoding="UTF-8" session="false" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Pragma" content="No-cache"/>
<meta http-equiv="Cache-Control" content="No-cache"/>
<meta http-equiv="Expires" content="0"/>
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<link rel="stylesheet" type="text/css" href="/static/css/new_file.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/objectSwap.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/cookieInfo.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/login.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError:true
		});
		
		//验证服务器MAC
		 $.getJSON("/bandMAC/macValidatCheck").done(function (data) {
			 if(data.status=='y'){
			 }else {
				 setRegistInfo(data)
			 }
		 });
	});
</script>
</head>
<!--[if lt IE 9]>
	<body onload="getInfoBs();getBrowserVersion('login','0');" style="display: none" id="body">
<![endif]  -->
<!--[if gte IE 9]>
	<body onload="getInfoBs();getBrowserVersion('login','1');" style="display: none" id="body">
<![endif]  -->

<!--[if !IE]><!--> 
	<body onload="getInfoBs();getBrowserVersion('login','0');" style="display: none" id="body">
<!--<![endif]-->
	<!--
       	作者：13611266435@163.com
       	时间：2015-07-23
       	描述：head end
       -->
	<div class="ws-content" id="contentForm">
		<div class="ws-login">
			<form class="subform" action="/web/help/login" method="post" id="loginForm">
				  <input type="hidden" name="comId" value="" id="comId"/>
				  <input type="hidden" name="cookieType" value="" id="cookieType"/>
				  <div>
	                  <input type="text" name="loginName" id="loginName" datatype="*" placeholder="请输入用户名" nullmsg="请输入用户名" class="users"/>
				  </div>
				  <div>
	                  <input type="password" id="password" name="password" datatype="*" placeholder="请输入密码" nullmsg="请输入密码" class="ws-locked"/>
				  </div>
                  <button type="submit">登录</button>
                </form>
		</div>
		<p class="ws-company">版权所有：成都西辰有限公司</p>
	</div>
	<div id="browsertip">
		<div class="browsertip" style="width: 780px;text-align: center;">
			<p>检测您正在使用低版本的IE浏览器</p>
			<p>为保证最佳体验请使用<strong>IE9及以上版本IE浏览器</strong>或者下载以下浏览器：</p>
			<ul class="browserlist">
				<li><a class="chrome" href="https://www.baidu.com/s?wd=chrome" target="_blank">Chrome</a></li>
				<li><a class="firefox" href="http://www.firefox.com.cn/" target="_blank">FireFox</a></li>
				<li><a class="safari" href="http://support.apple.com/zh_CN/downloads/#safari" target="_blank">Safari</a></li>
			</ul>
		</div>
		<p class="ws-company">版权所有：成都西辰有限公司</p>
	</div>
</body>

</html>

