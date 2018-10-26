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
			<c:if test="${not empty ininState}">
				<table border="0" cellspacing="5" cellpadding="3" width="65">
					<tbody>
						<tr>
							<td class="out" width="55" height="50" align="center" valign="middle">
							<img src="/static/images/peoples.gif" width="32" height="25" border="0">
							<br>${empty ininState?"所有":"常选"}人员</td>
						    <c:forEach begin="2" end="${num}" varStatus="st"><td>&nbsp;</td></c:forEach>
						</tr>
					</tbody>
				</table>
			</c:if>
			<table border="0" cellspacing="5" cellpadding="3" width="100%">
				<tbody>
					<tr>
						<c:set var="num">6</c:set>
						<c:forEach items="${list }" var="user" varStatus="st">
							<c:set var="userid">${((not empty user.forMeDoUserId) and (param.forMeDoDis eq 'yes'))?(user.forMeDoUserId):(user.id)}</c:set>
							<c:set var="username">${((not empty user.forMeDoUserId) and (param.forMeDoDis eq 'yes'))?(user.forMeDoUserName):(user.userName)}</c:set>
							<c:choose>
								<c:when test="${(not empty user.forMeDoUserId) and (param.forMeDoDis eq 'yes')}">
									<c:set var="title">离岗状态；“${user.forMeDoUserName}”代理</c:set>
								</c:when>
								<c:otherwise>
									<c:set var="title">${user.depName}</c:set>
								</c:otherwise>
							</c:choose>
							<td class="out" width="55" height="50" align="center" valign="middle" 
							onmouseover="this.className='over';" onmouseout="this.className='out'; " 
							onmousedown="this.className='down'" title="${title}" 
							style="${user.ifOnline=='1'?'color:black;':'color:#cccccc;'}" 
							onclick="parent.appendUser('{userid:\'${userid}\',username:\'${username}\'}')">
							<div class="ticket-user pull-left other-user-box">
								<img src="/downLoad/userImg/${userInfo.comId}/${userid}" 
										width="28" height="28" border="0" class="user-avatar">
							</div>
							<span class="user-name" style="color:${(not empty user.forMeDoUserId) and (param.forMeDoDis eq 'yes')?'red':''};">${user.userName }</span>
							</td>
							<!--添加个人常用组-->
							<c:if test="${not empty defaultSet and (empty latestInfo.defShowGrpId or latestInfo.defShowGrpId==0) and st.last}">
							<td class="out oftenSelectUserByGrp" style="cursor:pointer;height:50px;width:55px;" align="center" valign="middle" title="常用组设置">
								<div class="ticket-user pull-left other-user-box">
									<img src="/static/images/f1.jpg" width="28" height="28" border="0" class="user-avatar">
								</div>
								<span class="user-name" style="color:#FF0000">常用设置</span>
							</td>
							</c:if>
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
	<c:set var="userid">${((not empty user.forMeDoUserId) and (param.forMeDoDis eq 'yes'))?(user.forMeDoUserId):(user.id)}</c:set>
	<c:set var="username">${((not empty user.forMeDoUserId) and (param.forMeDoDis eq 'yes'))?(user.forMeDoUserName):(user.userName)}</c:set>
	var json = "{userid:\'${userid}\',username:\'${username}\'}";
	parent.appendUser(json);
	</c:forEach>
}
$(function(){
	$("body").on("click",".oftenSelectUserByGrp",function(){
		window.top.layer.confirm('您可以把常选人员放在同一组内；<br>并把该组设置为默认显示。<br>确认跳转配置？', {icon: 3, title:'提示'}, function(index){
     	  window.parent.parent.location.href="/selfGroup/listUserGroup?sid=${param.sid}&pager.pageSize=12&activityMenu=self_m_3.6&tips=defaultShow";
		  window.top.layer.closeAll();
		});
	});
});
</script>
</html>
