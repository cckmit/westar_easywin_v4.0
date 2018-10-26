<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
</script>
<script> 
	$(function() { 
		$('.grid_tab tr').addClass('odd'); 
		$('.grid_tab tr:even').addClass('even');
	}); 
</script>
</head>
<body onload="resizeVoteH('otherCarAttrIframe');" style="background-color:#FFFFFF;">
<table class="table table-hover general-table">
	<thead>
		<tr>
			<th  >序号</th>
			<th  class="hidden-phone">事由</th>
			<th class="text-center">使用时间</th>
			<th class="text-center">归还时间</th>
			<th class="text-center">目的地</th>
			<th class="text-center">实用里程(KM)</th>
			<th class="text-center">油耗(L)</th>
			<th class="text-center">状态</th>
		</tr>
	</thead>
	<tbody>
	<c:choose>
	 	<c:when test="${not empty listUseRecord}">
	 		<c:forEach items="${listUseRecord}" var="obj" varStatus="status">
	 			<tr>
	 				<td class="fileOrder">${ status.count}</td>
	 				<td><tags:cutString num="28">${obj.reason}</tags:cutString></td>
	 				<td class="text-center">${fn:substring(obj.startDate,5,16) }</td>
	 				<td class="text-center">${fn:substring(obj.endDate,5,16) }</td>
	 				<td class="text-center">${ obj.destination}</td>
 					<td class="text-center">${ obj.realJourney}</td>
	 				<td class="text-center">${ obj.oilConsumption}</td>
	 				<td class="text-center">
		 				<c:if test="${ obj.state eq 1}">已预约</c:if>
		 				<c:if test="${ obj.state eq 2}">已归还</c:if>
	 				</td>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="25" colspan="9" align="center"><h3>没有相关使用记录！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
	</tbody>
</table>
<tags:pageBar url="/car/carUseRecordPage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
