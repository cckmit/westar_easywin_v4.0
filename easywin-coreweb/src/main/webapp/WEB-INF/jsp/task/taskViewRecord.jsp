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
$(function(){
	//初始化名片
	initCard('${param.sid}');
	 //页面刷新
	$("#refreshImg").click(function(){
		window.self.location.reload();
	});
});
//任务查看权限验证
function viewTask(taskId){
	$.post("/task/authorCheck?sid=${param.sid}",{Action:"post",taskId:taskId},     
			function (msgObjs){
			if(!msgObjs.succ){
				showNotification(1,msgObjs.promptMsg);
			}else{
				parent.window.location.href="/task/viewTask?sid=${param.sid}&id="+taskId+"&redirectPage="+encodeURIComponent(window.location.href);
			}
	},"json");
}
</script>
<script> 
	$(function() { 
		$('.grid_tab tr').addClass('odd'); 
		$('.grid_tab tr:even').addClass('even');
	}); 
</script>
</head>
<body onload="resizeVoteH('otherTaskAttrIframe');" style="background-color:#FFFFFF;">
<table class="table table-hover general-table">
	<thead>
		<tr>
			<th width="10%" valign="middle">序号</th>
			<th width="22%" valign="middle">浏览时间</th>
			<th valign="middle">浏览人</th>
		</tr>
	</thead>
	<tbody>
	<c:choose>
	 	<c:when test="${not empty listViewRecord}">
	 		<c:forEach items="${listViewRecord}" var="viewObj" varStatus="status">
	 			<tr>
	 				<td>${ status.count}</td>
	 				<td align="left">${viewObj.recordCreateTime}</td>
	 				<td>
	 					<div class="online-head">
							<img src="/downLoad/userImg/${viewObj.comId}/${viewObj.userId}?sid=${param.sid}" />
						</div>
						<div class="online-name">
							<span class="ws-smallSize">${viewObj.userName }</span>
						</div>
	 				</td>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="25" colspan="9" align="center"><h3>没有相关信息！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
	</tbody>
</table>
<tags:pageBar url="/task/taskUpfilePage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
