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
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">

</script>

</head>
<body>
<div class="block01">
<form action="/organization/delOrgGroup" method="post" id="delForm">
<tags:token></tags:token>
 <input type="hidden" name="redirectPage"/>
    <table  width="100%" border="0" class="grid_tab"  cellpadding="0" cellspacing="0">
    <thead>
    <tr>
    <th style="width: 30px;"><input type="radio" name="aa" /></th>
    <th style="width: 50px;">序号</th>
    <th>组名</th>
    </tr>
    </thead>
    <tbody>
 <c:choose>
 	<c:when test="${not empty list}">
 		<c:forEach items="${list}" var="group" varStatus="st">
 			<tr style="cursor: pointer;" ondblclick="ok();" onclick="select('ids${st.index }')">
 				<td height="25"><input type="radio" id="ids${st.index }" name="ids" groupName="${group.grpName }" value="${group.id}"/> </td>
 				<td>${st.count}</td>
 				<td>${group.grpName }</td>
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
</div>
</body>
<script type="text/javascript">
 function select(id){
	 $('#'+id).attr("checked",true);
 }
 
 function ok(){
	 var obj = $('input[name="ids"][checked]');
	 if(obj.length>0){
		 art.dialog.data('groupId', obj.val());
		 art.dialog.data('groupName', obj.attr("groupName"));
	 }else{
		 art.dialog.data('groupId', '');
		 art.dialog.data('groupName', '');
	 }
	 //art.dialog.opener.showGroup(obj.val(),obj.attr("groupName"));
	 art.dialog.close();
	 
 }
 
</script>
</html>
