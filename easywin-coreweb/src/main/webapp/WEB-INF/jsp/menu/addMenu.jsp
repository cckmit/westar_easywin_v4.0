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
	$(function() {
		$('.subform').Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
	})
</script>
</head>
<body>
<div class="block01">
<form class="subform" method="post" action="/menu/addMenu">
	<tags:token></tags:token>
	<table width="100%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
		<tr>
			<td class="td1">菜单名：<font style="color: red">*</font></td>
			<td class="td2"><input  datatype="*" name="menuName" type="text" /></td>
			<td class="td1">父菜单：<font style="color: red">*</font></td>
			<td class="td2"><input readonly="readonly" name="parentName" datatype="*" type="text" value="${menu.parentName }" /> <input type="hidden" name="parentId" value="${menu.parentId }" /></td>
		</tr>
		<tr>
			<td class="td1">操作方式：</td>
			<td class="td2"><tags:dataDic type="menuTarget" style="width:80px;" name="menuTarget"></tags:dataDic></td>
			<td class="td1">访问权限：</td>
			<td class="td2"><tags:dataDic type="menuRole" style="width:80px;" name="menuRole"></tags:dataDic></td>
		</tr>
		<tr>
			<td class="td1">菜单图片：</td>
			<td class="td2"><input name="image" type="text" /></td>
			<td class="td1">菜单排序：<font style="color: red">*</font></td>
			<td class="td2"><input  datatype="n" name="orderNo" type="text" /></td>
		</tr>
		<tr>
			<td class="td1">访问路径：</td>
			<td class="td2" colspan="3"><textarea name="action" rows="6" style="width: 550px;"></textarea></td>
		</tr>
	</table><br/>
	<center>
		<input type="submit" value="提交" class="green_btn"/>
	</center>
</form>
</div>
</body>
</html>
