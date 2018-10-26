<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.PaginationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>

<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>

<script type="text/javascript" src="/static/plugins/artDialog/jquery.artDialog.js?skin=aero"></script>
<script type="text/javascript" src="/static/plugins/artDialog/plugins/iframeTools.js"></script>

<!-- Custom styles for this template -->
<link href="/static/css/style.css" rel="stylesheet">
<link href="/static/css/style-responsive.css" rel="stylesheet" />

<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	var sid="${param.sid}";
</script>
</head>
<body onload="resizeVoteH('${param.ifreamName}');" style="background-color:#FFFFFF;">
	<%--当前页面起始数 --%>
	<c:set var="offsetVal" value="<%=PaginationContext.getOffset() %>"></c:set>
	<%--查询结果总条数 --%>
	<c:set var="totalCountVal" value="<%=PaginationContext.getTotalCount() %>"></c:set>
	<%--每页显示多少条 --%>
	<c:set var="pageSizeVal" value="<%=PaginationContext.getPageSize() %>"></c:set>
	<%--是否为最后一页 --%>
	<c:set var="isLastPage" value="${(totalCountVal-totalCountVal mod pageSizeVal) eq offsetVal }"></c:set>
	<!--main content start-->
	<c:choose>
		<c:when test="${param.busType == 1 || param.busType == 013 || param.busType == 050}"><!-- 业务类型为分享的时候调整样式 -->
			<section id="main-content" style="width:100%;margin-left:0px;margin-top: -80px;padding: 0">
		</c:when>
		<c:otherwise>
			<section id="main-content" style="width:100%;margin-left:0px">
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test="${param.busType == 013 || param.busType == 050}"><!-- 业务类型为文件的时候调整样式 -->
			<section class="wrapper" style="margin-top: 0px;display:inline-block;width:100%;padding: 0px;padding-top: 15px">
		</c:when>
		<c:otherwise>
			<section class="wrapper" style="padding:15px;margin-top: 0px;display:inline-block;width:100%">
		</c:otherwise>
	</c:choose>
		
			<!-- page start-->
			<c:choose>
				<c:when test="${not empty pageBean.recordList}">
					<div class="log_border">
						<div class="log">
							<c:choose>
								<c:when test="${offsetVal eq 0}">
									<h2 class="first"></h2>
									<ul>
								</c:when>
								<c:otherwise>
									<ul style="margin-top: 30px;">
								</c:otherwise>
							</c:choose>
							<c:forEach items="${pageBean.recordList}" var="logVo" varStatus="vs">
								<c:choose>
									<c:when test="${isLastPage && vs.last}">
										<li class="last-li">
									</c:when>
									<c:otherwise>
										<li>
									</c:otherwise>
								</c:choose>
									
								<img src="/downLoad/userImg/${logVo.comId }/${logVo.userId}" title="${logVo.userName}" />
										
								<c:choose>
									<c:when test="${param.busType == 1 || param.busType == 013 || param.busType == 050}"><!-- 业务类型为分享的时候调整样式 -->
										<div class="log-content" style="width: 290px">
									</c:when>
									<c:otherwise>
										<div class="log-content">
									</c:otherwise>
								</c:choose>
								
									<span class="log-arrow"><img src="/static/images/titme-arrow.png" style="width: 20px; height: 20px;" /></span>
									<div class="row log-content-text">
										<div class="col-xs-12">
											<p>
												<span style="font-size:14px !important;color:#4F4F4F;">${fn:replace(logVo.content,'@sid',param.sid)}</span>
											</p>
										</div>
										<div class="col-xs-12">
											<p>
												<span class="log-time" style="float:left;">操作人：</span>
												<span class="log-time" style="font-size:10px;color:#1981EB;float:left;">${logVo.userName }&nbsp;</span>
												<span class="log-time" style="float:right;margin-right: 10px;">操作时间：<span class="log-time" style="float:right;color:#1981EB;">${logVo.recordCreateTime }</span></span>
											</p>
										</div>
									</div>
								</div>
								</li>
							</c:forEach>
							</ul>
						</div>
					</div>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>

		</section>
	</section>
	
	<c:choose>
		<c:when test="${param.busType == 1 || param.busType == 013 || param.busType == 050}"><!-- 业务类型为分享的时候调整样式 -->
			<tags:pageBar url="/common/listLog" maxIndexPages="3"></tags:pageBar>
		</c:when>
		<c:otherwise>
			<tags:pageBar url="/common/listLog"></tags:pageBar>
		</c:otherwise>
	</c:choose>
	
	<%--用与测量当前页面的高度 --%>
	<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
