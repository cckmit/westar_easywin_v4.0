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
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title><%=SystemStrConstant.TITLE_NAME%></title>
<meta name="description" content="Dashboard" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- 框架样式 -->
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8"
	src="/static/js/order_center/order_center.js"></script>
<script type="text/javascript">
	     var sid="${param.sid}";//申明一个sid全局变量
		 var searchTab = '${param.searchTab}';
	    </script>
</head>
<body>
	<!-- 系统头部装载 -->
	<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
	<!-- 数据展示区域 -->
	<div class="main-container container-fluid">
		<!-- Page Container -->
		<div class="page-container">
			<!-- 大条件 -->
			<jsp:include page="order_center_left_menu.jsp"></jsp:include>
			<c:if test="${(empty activityMenu) || activityMenu=='order_m_1.1' }">
				<!--收费标准-->
				<jsp:include page="chargingStandards.jsp"></jsp:include>
			</c:if>
			<c:if test="${activityMenu eq 'payWays' }">
				<!--支付方式选择页面-->
				<jsp:include page="payWays.jsp"></jsp:include>
			</c:if>
			<c:if test="${activityMenu eq 'order_m_1.2' }">
				<!--交易记录列表页面-->
				<jsp:include page="listOrders.jsp"></jsp:include>
			</c:if>
		</div>
	</div>
	<!--主题颜色设置按钮-->
	<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
</body>
</html>

