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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<style type="text/css">
.stream-top span{
margin-left: 15px
}
</style>
</head>
<body onload="resizeVoteH('${param.ifreamName}');">
<div class="orders-container bg-white no-margin-bottom" style="min-height: 50px">
	<c:choose>
		<c:when test="${not empty listUser}">
			<c:forEach items="${listUser}" var="obj" varStatus="vs">
				<div class="stream-box">
					<div class="stream-top" style="height:25px">
					
					<div class="ticket-user pull-left other-user-box" style="width:90px">
						<img class="user-avatar"  src="/downLoad/userImg/${obj.comId}/${obj.id}?sid=${param.sid}" title="${obj.userName }"></img>
							<span class="user-name">
							<tags:cutString num="6">${obj.userName}</tags:cutString>
							</span>
					</div>
					
						<span class="stream-message" style="position:relative;top:15px">
                             	<c:choose>
								<c:when test="${not empty obj.isView}"><span style="color:green">已查看</span></c:when>
								<c:otherwise><span style="color:red">未查看</span></c:otherwise>
							</c:choose>
						</span>
						<span class="stream-cal" style="position:relative;top:15px">${fn:substring(obj.viewTime,0,16)}</span>
						</div>
				</div>
			</c:forEach>
			
			<tags:pageBar url="/institution/listViewRecord"></tags:pageBar>
		</c:when>
		<c:otherwise>
			<div style="text-align: center;">暂无人查看</div>
		</c:otherwise>
	</c:choose>
	</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>

