<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
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
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		<!-- 财务模块JS -->
		<script type="text/javascript" charset="utf-8" src="/static/js/financial/financial.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<c:choose>
			<c:when test="${param.activityMenu=='m_1.2' or param.activityMenu=='m_1.5' or param.activityMenu=='self_m_1.4' or param.activityMenu=='self_m_1.9'}">
				<script type="text/javascript" charset="utf-8" src="/static/js/financial/loanApplyCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
			<c:when test="${param.activityMenu=='m_1.3' or param.activityMenu=='self_m_1.5'}">
				<script type="text/javascript" charset="utf-8" src="/static/js/financial/loanCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
			<c:when test="${param.activityMenu=='m_1.4' or param.activityMenu=='self_m_1.6'}">
				<script type="text/javascript" charset="utf-8" src="/static/js/financial/loanOffCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			</c:when>
		</c:choose>

		<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript">
			var sid="${param.sid}";//sid全局变量
			var EASYWIN = {
					"userInfo":{
						"id":"${userInfo.id}",
						"name":"${userInfo.userName}"
					},
					"sid":"${param.sid}"
			}
			$(function(){
				//财务办公菜单
				getSelfJSON("/modAdmin/authCheckModAdmin",{sid:'${param.sid}',busType:'066'},function(data){
				if(data.status =='y' && data.modAdminFlag){
					$("#financialOffice").show();
				}else{
					$("#financialOffice").remove();
				}
				});
			})
		</script>
	</head>
	<body>
		<input type="hidden" id="handoverUserId"/>
		<input type="hidden" id="handoverUserName"/>
		<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="financialCenter_menu.jsp"></jsp:include>
				<!--显示差旅一览-->
				<c:if test="${empty param.activityMenu or param.activityMenu=='m_1.1'}">
					<%-- <jsp:include page="/WEB-INF/jsp/statistics/financial/financialStatistics.jsp"></jsp:include> --%>
					<jsp:include page="/WEB-INF/jsp/statistics/financial/financialStatisticsV2.jsp"></jsp:include>
				</c:if>
				<!--显示个人出差记录-->
				<c:if test="${param.activityMenu=='m_1.2'}">
					<jsp:include page="loanApply/listLoanApplyOfAuth.jsp"></jsp:include>
				</c:if>
				<!--显示记录-->
				<c:if test="${param.activityMenu=='m_1.5'}">
					<jsp:include page="loanApply/listLoanDaylyApplyOfAuth.jsp"></jsp:include>
				</c:if>
				<!--显示个人借款记录-->
				<c:if test="${param.activityMenu=='m_1.3'}">
					<jsp:include page="loan/listLoanOfAuth.jsp"></jsp:include>
				</c:if>
				<!--显示个人报销记录-->
				<c:if test="${param.activityMenu=='m_1.4'}">
					<jsp:include page="loanOff/listLoanOffOfAuth.jsp"></jsp:include>
				</c:if>
				<!--显示个人消费记录-->
				<c:if test="${param.activityMenu=='m_1.7'}">
					<jsp:include page="consume/listConsume.jsp"></jsp:include>
				</c:if>
				<!--显示财务办公-->
				<c:if test="${param.activityMenu=='m_1.8'}">
					<jsp:include page="financialOffice/viewFinancialOffices.jsp"></jsp:include>
				</c:if>
				<!--个人费用-->
				<c:if test="${param.activityMenu=='m_1.9'}">
					<jsp:include page="/WEB-INF/jsp/financial/personalApply/personalApply.jsp"></jsp:include>
				</c:if>
				<c:if test="${param.activityMenu=='self_m_1.8'}">
					<jsp:include page="/WEB-INF/jsp/financial/financialOffice/viewFinancialOffices.jsp"></jsp:include>
				</c:if>
	        </div>
	    </div>
		<!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	</body>
</html>

