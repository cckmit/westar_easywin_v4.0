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
			showAllError:true
		});
	})
</script>
</head>
<body>
<div class="tit"><span>当前位置：系统管理>>人员维护</span></div>
<div class="block01">
<form class="subform" method="post" action="/userInfo/addUserInfo">
<input type="hidden" name="redirectPage" value="${param.redirectPage}"/>
<input type="hidden" name="orgId" id="orgId" value="${param.orgId}"/>
<tags:token></tags:token>
	<table  width="90%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
		<tr>
			<td class="td1">登录名：<font style="color: red">*</font></td>
			<td class="td2">
				<input datatype="*" ajaxurl="/userInfo/validateLoginName?sid=${param.sid }" name="loginName" type="text"/>
			</td>
			<td class="td1">密&nbsp;码：<font style="color: red">*</font></td>
			<td class="td2">
				<input datatype="*" name="password" type="password" value="1"/>
			</td>
		</tr>
		<tr>
			<td class="td1">用户名称：<font style="color: red">*</font></td>
			<td class="td2">
				<input datatype="*" name="userName" type="text"/>
			</td>
			<td class="td1">照片：</td>
			<td class="td2">
				<tags:uploadOne fileTypes="gif|jpe?g" fileSize="1" name="photoImgId" showName="photoImgName"></tags:uploadOne>
			</td>
		</tr>
		<tr>
			<td class="td1">所在机构：<font style="color: red">*</font></td>
			<td class="td2" colspan="3">
			    <tags:orgOne datatype="*" queryParam="{\'rootId\':\'${param.orgRootId }\'}" name="orgId" showValue="${param.orgPathName }" value="${param.orgId}" showName="orgPathName"></tags:orgOne>
			</td>
		</tr>
		<tr>
			<td class="td1">兼职机构：</td>
			<td class="td2">
				<tags:officeMore  name="listPartTimeOrg.id" showName="pathName"></tags:officeMore>
			</td>
			<td class="td1">所属角色：<font style="color: red">*</font></td>
			<td class="td2">
			<!-- 如果是部门管理员进入页面，则只查询部门级的角色可选 -->
				<tags:roleMore datatype="*" queryParam="{\'roleType\':\'${empty param.orgRootId?'':'1' }\'}" name="listRole.id" showName="roleName"></tags:roleMore>
			</td>
		</tr>
	</table><br/>
	<center>
		<input type="submit" value="提交" class="green_btn"/>
		<input type="button"  value="返回" class="blue_btn"  onclick="window.location.href='${param.redirectPage}'"/>&nbsp;&nbsp;
	</center>
</form>
</div>
</body>
</html>
