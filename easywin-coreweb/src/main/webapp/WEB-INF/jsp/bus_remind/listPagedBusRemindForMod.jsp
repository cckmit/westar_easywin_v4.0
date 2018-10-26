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
</head>
<body onload="resizeVoteH('${param.ifreamName}');" style="background-color:#FFFFFF;">
<table class="table table-hover general-table">
	<tr valign="middle">
		<th style="width: 50px;" valign="middle">序号</th>
		<th style="width: 150px" class="text-center">催办时间</th>
		<th style="width: 120px" valign="middle" class="text-center">催办人员</th>
		<th valign="middle">催办内容</th>
		<th style="width: 90px" valign="middle">操作</th>
	</tr>
	<c:choose>
	 	<c:when test="${not empty pageBean.recordList}">
	 		<c:forEach items="${pageBean.recordList}" var="object" varStatus="vs">
      				<tr>
      					<td>
      						${vs.count}
      					</td>
      					<td>
      						${object.recordCreateTime}
      					</td>
      					<td class="text-center">
      						${object.userName}
      					</td>
      					<td>
      						${ object.content}
      					</td>
      					<td>
      						<a href="javascript:void(0)" busRemind="${object.id}">查看</a>
      					</td>
      				</tr>
      			</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="25" colspan="7" align="center"><h3>没有相关信息！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
</table>
<tags:pageBar url="/demand/listPagedDemandUpfilePage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
<script type="text/javascript">
$(function(){
	$("body").on("click","a[busRemind]",function(){
		var busRemind = $(this).attr("busRemind");
    	var url = "/busRemind/viewBusRemindPage?sid=${param.sid}&id="+busRemind;
		var height = $(window.parent).height();
		openWinWithPams(url,"800px",(height-90)+"px");
	})
})
</script>
</html>
