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

	function sendMsg(id,name){
		art.dialog.open('/message/addOneMsgPage?receiver='+id+'&receiverName='+name+'&sid=${param.sid}', {
			title : '消息发送',
			lock : true,
			max : false,
			min : false,
			width : 540,
			height : 350,
			close : function() {
				
			}
		});
	}
</script>
<script> 
	$(function() { 
		$('.grid_tab tr').addClass('odd'); 
		$('.grid_tab tr:even').addClass('even');
	}); 
</script>
</head>
<body>
<div class="block01">
<form action="/userInfo/showOnlineUserPage">
  <input type="hidden" name="sid" value="${param.sid }" />
   <table width="100%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
    <tr>
      <td class="td1">用户名称：</td>
      <td class="td2"><input name="userName" type="text" value="${userInfo.userName }"/></td>
      <td class="td1">所在机构：</td>
      <td class="td2">
     	<tags:orgOne name="anyOrgId" showName="anyNameLike" value="${userInfo.anyOrgId }" showValue="${userInfo.anyNameLike }" readOnly="false"></tags:orgOne>
      </td>
      <td align="center">
      <input type="submit"  value="查询" class="green_btn"/>
      </td>
    </tr>
   </table>
</form>
</div>
 <div class="block01">
 <form action="/userInfo/delUserInfo" method="post" id="delForm">
     <tags:token></tags:token>
	 <input type="hidden" name="redirectPage"/>
     <table width="100%" border="0" class="grid_tab"  cellpadding="0" cellspacing="0">
     <thead>
     <tr>
     <th>序号</th>
     <th>用户名称</th>
     <th>所在机构</th>
     <th>操作</th>
     </tr>
     </thead>
     <tbody>
	 <c:choose>
	 	<c:when test="${not empty list}">
	 		<c:forEach items="${list}" var="userInfo" varStatus="status">
	 			<tr>
	 				<td height="25">${ status.count}</td>
	 				<td>${ userInfo.userName}</td>
	 				<td>${ userInfo.orgPathName}</td>
	 				<td>
	 				  <span><a href="javascript:void(0)" onclick="sendMsg(${userInfo.id},'${userInfo.userName }')">发送消息</a></span>
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
	 </tbody>
	</table>
	</form>
	<div class="page"><tags:pageBar url="/userInfo/showOnlineUserPage"></tags:pageBar></div>
</div>
</body>
</html>
