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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
	<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	<c:choose>
		<c:when test="${empty param.activityMenu || param.activityMenu=='04201'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/itomJs/eventPmJs/eventPm.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:when>
		<c:when test="${param.activityMenu=='04202'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/itomJs/issuePmJs/issuePm.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:when>
		<c:when test="${param.activityMenu=='04203'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/itomJs/modifyPmJs/modifyPm.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:when>
		<c:when test="${param.activityMenu=='04204'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/itomJs/releasePmJs/releasePm.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:when>
		<c:when test="${param.activityMenu=='04301'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/itomJs/eventPmJs/analyseEventPm.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:when>
		<c:when test="${param.activityMenu=='04302'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/itomJs/issuePmJs/analyseIssuePm.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:when>
		<c:when test="${param.activityMenu=='04303'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/itomJs/modifyPmJs/analyseModifyPm.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:when>
		<c:when test="${param.activityMenu=='04304'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/itomJs/releasePmJs/analyseReleasePm.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:when>
	</c:choose>
		
<script type="text/javascript">
	    var sid="${param.sid}";//申明一个sid全局变量
		$(function(){
			
			$(".subform").Validform({
				tiptype : function(msg, o, cssctl) {
					validMsg(msg, o, cssctl);
				},
				showAllError : true
			});
		})

</script>	    
</head>
<body>
	<!-- 系统头部装载 -->
	<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
	<!-- 数据展示区域 -->
	<div class="main-container container-fluid">
		<!-- Page Container -->
		<div class="page-container">
			<jsp:include page="itom_center_left_menu.jsp"></jsp:include>
				<c:choose>
					<c:when test="${empty param.activityMenu || param.activityMenu=='04201' }">
						<!-- 事件管理 -->
						<jsp:include page="eventPm/listEventPm.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='04202' }">
						<!-- 问题管理 -->
						<jsp:include page="issuePm/listIssuePm.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='04203' }">
						<!-- 变更管理 -->
						<jsp:include page="modifyPm/listModifyPm.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='04204' }">
						<!-- 发布管理 -->
						<jsp:include page="releasePm/listReleasePm.jsp"></jsp:include>
					</c:when>
					<c:when test="${ param.activityMenu=='04301' }">
						<!-- 事件管理 -->
						<jsp:include page="eventPm/analyseEventPm.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='04302' }">
						<!-- 问题管理 -->
						<jsp:include page="issuePm/analyseIssuePm.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='04303' }">
						<!-- 变更管理 -->
						<jsp:include page="modifyPm/analyseModifyPm.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='04304' }">
						<!-- 发布管理 -->
						<jsp:include page="releasePm/analyseReleasePm.jsp"></jsp:include>
					</c:when>
				</c:choose>
		</div>
	</div>
	<!--主题颜色设置按钮-->
	<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
</body>
</html>

