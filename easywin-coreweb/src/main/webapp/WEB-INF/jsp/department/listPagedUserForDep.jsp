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
//设置条件
function setCondition(val){
	$("#condition").val(val);
	$("#searchForm").submit();
}
$(function() {
	parent.showSearch();
})
</script>
</head>
<body onload="resizeVoteH('rightFrame')" style="background:#fff;">
<div class="panel">
<form action="/department/listPagedUserTreeForDep" id="searchForm">	
<input type="hidden" name="depId" value="${userInfo.depId}"/>
<input type="hidden" name="depName" value="${userInfo.depName}"/>
<input type="hidden" name="sid" value="${param.sid }" />
<input type="hidden" class="form-control" name="condition" id="condition" value="${userInfo.condition}" placeholder="用户检索……"/>
<%-- <header class="panel-heading">${empty userInfo.depName?userInfo.orgName:userInfo.depName }
<div class="pull-right ws-search-box">
</div>
</header> --%>
<div class="panel-body">
	<table class="table table-hover general-table">
		<thead>
			<tr>
			  <th width="10%" valign="middle">序号</th>
			  <th width="20%" valign="middle">姓名</th>
			  <th valign="middle">Email</th>
			  <th width="20%" valign="middle"  style="text-align: center;">隶属部门</th>
			  <th width="20%" valign="middle"  style="text-align: center;">启用状态</th>
			</tr>
		</thead>
		<tbody>
		<c:choose>
		 	<c:when test="${not empty list}">
		 		<c:forEach items="${list}" var="user" varStatus="status">
		 			<tr>
		 				<td height="35">${ status.count}</td>
		 				<td>${ user.userName}</td>
		 				<td>${ user.email}</td>
		 				<td  style="text-align: center;">${user.depName}</td>
		 				<td valign="middle" style="text-align: center;">
		 					<c:choose>
		 						<c:when test="${user.enabled==1}">
		 							<font color="green">在职</font>
		 						</c:when>
		 						<c:otherwise>
		 						<font color="red">离职</font>
		 						</c:otherwise>
		 					</c:choose>
		 				</td>
		 			</tr>
		 		</c:forEach>
		 	</c:when>
		 	<c:otherwise>
		 		<tr>
		 			<td height="35" colspan="5" align="center"><h3>没有相关信息！</h3></td>
		 		</tr>
		 	</c:otherwise>
		 </c:choose>
		</tbody>
	</table>
	 <tags:pageBar url="/department/listPagedUserTreeForDep"></tags:pageBar>
</div>
</form>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
