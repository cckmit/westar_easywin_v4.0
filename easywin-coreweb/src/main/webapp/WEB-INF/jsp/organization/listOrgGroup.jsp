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
	function add(){
		window.location.href='/organization/addOrgGroupPage?sid=${param.sid}&redirectPage='+encodeURIComponent(window.location.href);
	}
	
	function update(id){
		window.location.href='/organization/updateOrgGroupPage?id='+id+'&sid=${param.sid}&redirectPage='+encodeURIComponent(window.location.href);
	}

	//批量删除
	function del(){
		if(checkCkBoxStatus('ids')){
			$("#delForm input[name='redirectPage']").val(window.location.href);
			$('#delForm').submit();
		}else{
			art.dialog.alert('请先选择一条记录。');
		}
	}

</script>

</head>
<body>
<div class="tit"><span>当前位置：系统管理>>部门私有组</span></div>
<div class="block01">
<div class="oper_btn">
 <span>
 <a href="javascript:void(0)" onclick="add()" class="add">新增</a>
 <a href="javascript:void(0)" onclick="del()" class="del">删除</a>
 </span>
 </div>
 </div>
<div class="block01">
<form action="/organization/delOrgGroup" method="post" id="delForm">
<tags:token></tags:token>
 <input type="hidden" name="redirectPage"/>
    <table  width="100%" border="0" class="grid_tab"  cellpadding="0" cellspacing="0">
    <thead>
    <tr>
    <th><input type="checkbox" onclick="checkAll(this,'ids')" /></th>
    <th style="width: 5%">序号</th>
    <th style="width: 20%">使用机构</th>
    <th style="width: 50%">组名</th>
    <th>操作</th>
    </tr>
    </thead>
    <tbody>
 <c:choose>
 	<c:when test="${not empty list}">
 		<c:forEach items="${list}" var="group" varStatus="st">
 			<tr>
 				<td height="25"><input type="checkbox" name="ids" value="${group.id}"/> </td>
 				<td>${st.count}</td>
 				<td>${group.orgName }</td>
 				<td>${group.groupName }</td>
 				<td><span><a href="javascript:void(0)" onclick="update(${group.id})">修改</a></span></td>
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
</html>
