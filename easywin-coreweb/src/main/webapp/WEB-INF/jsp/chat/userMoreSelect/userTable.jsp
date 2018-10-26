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
<style type="text/css">
body {
	FONT: 9pt Arial, Helvetica, sans-serif;
	cursor: default;
}

table {
	font-size: 9pt;
	cursor: default
}

.over {
	border-left: 1 solid #efefef;
	border-right: 1 solid #808080;
	border-top: 1 solid #efefef;
	border-bottom: 1 solid #808080;
	background-color: #9bacf4;
	color: #ffffff;
	cursor: pointer;
}

.out {
	border: 1 solid #ffffff;
}

.down {
	border-left: 1 solid #808080;
	border-right: 1 solid #e7e3de;
	border-top: 1 solid #808080;
	border-bottom: 1 solid #e7e3de;
}

.up {
	border: 1 solid #ffffff;
}
.ws-position img{
border:1px solid #dedede;border-radius: 5px;
}
</style>
</head>
<body style="background-color: #fff">
	<c:choose>
		<c:when test="${fn:length(list)==0 }">
			<c:if test="${empty ininState}">
				<font style='font-size:10pt' color=red>&nbsp;&nbsp;没有设置人员</font>
			</c:if>
		</c:when>
		<c:otherwise>
		<c:set var="num">6</c:set>
			<table border="0" cellspacing="5" cellpadding="3" width="100%">
				<tbody>
					<tr>
						<td class="out" width="55" height="50" align="center" valign="middle" 
						onmouseover="this.className='over';" onmouseout="this.className='out';"
						onmousedown="this.className='down'" title="点击选择所有人员" onclick="appendAllUser()">
						<img src="/static/images/peoples.gif" width="32" height="25" border="0">
						<br>${empty ininState?"所有":"常选"}人员</td>
					    <c:forEach begin="2" end="${num}" varStatus="st"><td>&nbsp;</td></c:forEach>
					</tr>
				</tbody>
			</table>
			<table border="0" width="100%" cellspacing="5" cellpadding="3">
				<tbody>
					<tr>
						<c:forEach items="${list }" var="user" varStatus="st">
							<td class="out" width="55" height="50" align="center" valign="middle" 
							onmouseover="this.className='over';" onmouseout="this.className='out'; " 
							onmousedown="this.className='down'" title="${user.depName}" 
							style="${user.ifOnline=='1'?'color:black;':'color:#cccccc;'}" 
							onclick="parent.appendUser('{userid:\'${user.id }\',username:\'${user.userName}\'}')">
							<div class="ticket-user pull-left other-user-box">
								<img src="/downLoad/userImg/${user.comId}/${user.id}?sid=${param.sid}" 
								width="28" height="28" border="5" class="user-avatar">
							</div>
							<span class="user-name">${user.userName }</span>
							</td>
							<c:if test="${st.count%num==0&&st.count!=fn:length(list) }">
					</tr>
					<tr>
						</c:if>
						<c:if test="${st.count==fn:length(list) }">
							<c:forEach begin="${st.count%num+1 }" end="${num }">
								<td>&nbsp;</td>
							</c:forEach>
						</c:if>
						</c:forEach>
					</tr>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>
</body>
<script type="text/javascript">
	function appendAllUser() {
		<c:forEach items="${list }" var="user" varStatus="st">
		var json = "{userid:\'${user.id }\',username:\'${user.userName}\'}";
		parent.appendUser(json);
		</c:forEach>
	}
</script>
</html>
