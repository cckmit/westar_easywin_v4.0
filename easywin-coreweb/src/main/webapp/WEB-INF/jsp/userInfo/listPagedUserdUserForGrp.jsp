<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script> 
	
	function clickUser(ts,id,email,name){
		var json="{id:\'"+id+"\',email:\'"+email+"\',name:\'"+name+"\'}";
		parent.appendUser(ts.checked,json);
	}
	
	$(document).ready(function(){
		var o = parent.document.getElementById("listUser_id").options;
		var oArray = new Array();
		for(var i=0;i<o.length;i++){
		    oArray[i]=o[i].value;
		}
		$(":checkbox[name='id']").each(function(index){
			if(oArray.in_array($(this).val())){
				$(this).attr("checked","checked");
			}
		});
	});
	/*取消选中*/
	function uncheck(id){
		$(":checkbox[name='id'][value="+id+"]").each(function(index){
			$(this).attr("checked",false);
		});
	}
	/*取消所有选中*/
	function uncheckAll(){
		$(":checkbox[name='id'][checked='checked']").each(function(index){
			$(this).attr("checked",false);
		});
	}
</script>
</head>
<body style="background-color: #fff" onload="resizeVoteH('allUser');">
<div class="panel">
<div class="panel-body">
	<table class="table table-hover general-table">
		<thead>
			<tr>
			  <th width="6%" valign="middle"><input type="checkbox" onclick="selectAll(this,'id')" /></th>
			  <th width="10%" valign="middle">序号</th>
			  <th width="15%" valign="middle">姓名</th>
			  <th valign="middle">Email</th>
			  <th width="15%" valign="middle">隶属部门</th>
			</tr>
		</thead>
		<tbody>
		<c:choose>
		 	<c:when test="${not empty list}">
		 		<c:forEach items="${list}" var="user" varStatus="status">
		 			<tr>
		 				<td valign="middle"><input type="checkbox" name="id" onclick="clickUser(this,${user.id},'${user.email}','${user.userName}')" value="${user.id}"/> </td>
		 				<td valign="middle">${ status.count}</td>
		 				<td valign="middle">${ user.userName}</td>
		 				<td valign="middle">${ user.email}</td>
		 				<td valign="middle">${user.depName}</td>
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
	 <tags:pageBar url="/userInfo/listPagedUserForGrp"></tags:pageBar>
</div>
</div>	
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
