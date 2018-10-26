<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
	$(".searchForm").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		callback:function (form){
			//提交前验证是否在上传附件
			return sumitPreCheck(null);
		},
		showAllError : true
	});
});
//项目查看权限验证
function viewIt(url,id){
	window.location.href=url+"?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
}
function formSub(){
	$(".searchForm").submit();
}
</script>
<script> 
	$(function() { 
		$('.grid_tab tr').addClass('odd'); 
		$('.grid_tab tr:even').addClass('even');
	}); 
</script>
</head>
<body style="width: 95%;padding-left: 20px" onload="scrollPage()">
<div class="tit"><span>当前位置：全展检索</span></div>
 <div class="block01">
 <form action="/item/delItem" method="post" id="delForm">
     <tags:token></tags:token>
	 <input type="hidden" id="redirectPage" name="redirectPage"/>
     <table width="100%" border="0" class="grid_tab"  cellpadding="0" cellspacing="0">
     <thead>
     <tr>
     <th width="5%"><input type="checkbox" onclick="checkAll(this,'ids')" /></th>
     <th width="5%">序号</th>
     <th width="10%">comId</th>
     <th align="left">名称</th>
     <th width="10%">docId</th>
     <th width="10%">busType</th>
     <th width="20%">createDate</th>
     </tr>
     </thead>
     <tbody>
	 <c:choose>
	 	<c:when test="${not empty listIndexVo}">
	 		<c:forEach items="${listIndexVo}" var="indexVo" varStatus="status">
	 			<tr>
	 				<td height="25"><input type="checkbox" name="ids" value="${indexVo.id}" ${(item.owner==userInfo.id)?'':'disabled="disabled"' }/> </td>
	 				<td>${ status.count}</td>
	 				<td>${ indexVo.comId}</td>
	 				<td align="left"><a href="javascript:void(0);" onclick="viewIt('${indexVo.url}','${indexVo.busId}');">${indexVo.busName}</a></td>
	 				<td>${ indexVo.id}</td>
	 				<td>${ indexVo.busType}</td>
	 				<td>${ indexVo.recordCreateTime}</td>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="25" colspan="7" align="center"><h3>没有相关信息！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
	 </tbody>
	</table>
	</form>
	<div class="page"><tags:pageBar url="/item/listItemPage"></tags:pageBar></div>
</div>

<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
