<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="utf-8" />
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<meta name="description" content="Dashboard" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<!-- 框架样式 -->
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<script type="text/javascript" src="/static/assets/js/skins.min.js"></script>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<script type="text/javascript" src="/static/js/indexJs/index.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript" src="/static/js/introStep.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript" src="/static/js/shareCode.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		<style type="text/css">
	    	.page-content{ margin-left:0; }
	    </style>
	    <script type="text/javascript">
	    	var sid = '${param.sid}';
	    	var pageTag = 'index';
	    	
	    	var step = 1;
	    	if($.cookie("index_step")){
	    		step = $.cookie("index_step");
	    	}
	    </script>
	</head>
	<body >
		<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
				<!-- 配置区域 -->
				<jsp:include page="/WEB-INF/jsp/home/home_left.jsp"></jsp:include>
				<!--通讯录 -->
				<jsp:include page="/WEB-INF/jsp/home/home_right.jsp"></jsp:include>
	        </div>
	   		 <!--主题颜色设置按钮-->
			<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	    </div>
	</body>
</html>

