<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
	<!-- 框架样式 -->
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
	<script type="text/javascript" src="/static/js/jiFenCenterJs/jiFenCenter.js"></script>
	<title><%=SystemStrConstant.TITLE_NAME%></title>
</head>
<body>
<!-- 系统头部装载 -->
<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<!-- 数据展示区域 -->
   <div class="main-container container-fluid">
       <!-- Page Container -->
       <div class="page-container">
       	<!-- 大条件 -->
		<jsp:include page="/WEB-INF/jsp/userInfo/selfCenter_left.jsp"></jsp:include>
		<!--显示个人积分排行信息-->
		<c:if test="${(param.activityMenu=='self_m_2.1') ||(param.activityMenu=='self_m_2.2') ||(param.activityMenu=='self_m_2.3')}">
			<jsp:include page="listPagedJifenOrder.jsp"></jsp:include> 
		</c:if>  
		<!-- 个人积分历史 --> 
		<c:if test="${param.activityMenu=='self_m_2.4'}">
			<jsp:include page="listPagedJifenHistory.jsp"></jsp:include>
		</c:if>  
		<!-- 积分规则 --> 
		<c:if test="${param.activityMenu=='self_m_2.5'}">
			<jsp:include page="listPagedJifenConfig.jsp"></jsp:include>
		</c:if>  
		<!-- 等级说明 --> 
		<c:if test="${param.activityMenu=='self_m_2.6'}">
			<jsp:include page="listPagedJifenLev.jsp"></jsp:include>
		</c:if>   
	   </div>
   </div>
<!--主题颜色设置按钮-->
<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
</body>
</html>

