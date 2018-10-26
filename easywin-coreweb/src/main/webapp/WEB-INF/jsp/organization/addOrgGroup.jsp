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
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
	})
</script>
</head>
<body>
	<div class="tit">
		<span>当前位置：系统管理>>部门私有组</span>
	</div>
	<div class="block01">
		<form class="subform" method="post" action="/organization/addOrgGroup">
			<input type="hidden" name="redirectPage" value="${param.redirectPage}" />
			<tags:token></tags:token>
			<table width="80%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
				<tr>
					<td class="td1" style="width: 25%">组名：<font style="color: red">*</font></td>
					<td class="td2" style="width: 75%"><input datatype="*" name="groupName" type="text" /></td>
				</tr>
				<tr>
					<td class="td1">部门选择：<font style="color: red">*</font></td>
					<td class="td2">
					  <tags:depMore datatype="*" name="listOrg.id" showName="orgName"></tags:depMore>
					</td>
				</tr>
			</table>
			<br />
			<center>
				<input type="submit" value="提交" class="green_btn" /> <input type="button" value="返回" class="blue_btn" onclick="window.location.href='${param.redirectPage}'" />&nbsp;&nbsp;
			</center>
		</form>
	</div>
</body>
</html>
