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
<script type="text/javascript" src="/static/js/zhbgJs/carJs/car.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
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
			<th width="8%" valign="middle">序号</th>
			<th width="30%"valign="middle" class="hidden-phone">维修事由</th>
			<th width="25%" valign="middle">维修日期</th>
			<th width="12%" valign="middle">维修价格</th>
			<th width="12%" valign="middle">委派人</th>
			<c:if test="${isModAdmin }">
			<th width="12%" valign="middle" style="text-align: center;">操作</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
	<c:choose>
	 	<c:when test="${not empty listMaintain}">
	 		<c:forEach items="${listMaintain}" var="obj" varStatus="status">
	 			<tr>
	 				<td class="orderNum">${ status.count}</td>
	 				<td align="left" style="font-size: 13px">
	 				<tags:cutString num="20">${ obj.reason}</tags:cutString></td>
	 				<td style="font-size: 13px">
	 				${ obj.startDate} ~~
	 				<c:choose>
	 				<c:when test="${not empty obj.endDate}">${ obj.endDate}</c:when>
	 				<c:otherwise>至今</c:otherwise>
	 				</c:choose>
	 				</td>
	 				<td>
	 				<c:choose>
	 				<c:when test="${not empty obj.maintainPrice}">${ obj.maintainPrice}</c:when>
	 				<c:otherwise>--</c:otherwise>
	 				</c:choose>
	 				</td>
	 				<td style="text-align: left;">
	 					<div class="ws-position" data-container="body" data-toggle="popover" data-placement="left">
						<img src="/downLoad/userImg/${obj.comId}/${obj.executor}?sid=${param.sid}"
							title="${obj.executorName}" class="user-avatar"/>
						<i class="ws-smallName">${obj.executorName}</i>
						</div>
	 				</td>
	 				<c:if test="${isModAdmin }">
	 				<td align="center">
	 				<a href="javascript:void(0)" onclick="updateCarMaintain('${param.sid}','${obj.id }')">修改</a>
	 				|<a href="javascript:void(0)" onclick="delCarMaintain('${obj.id }','${param.sid}',this)">删除</a>
		 			</td>
		 			</c:if>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="25" colspan="9" align="center"><h3>没有相关维修记录！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
	</tbody>
</table>
<tags:pageBar url="/car/carMaintainRecordPage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
