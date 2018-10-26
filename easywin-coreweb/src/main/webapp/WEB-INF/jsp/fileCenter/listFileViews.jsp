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
<body onload="resizeVoteH('otherFileIframe');">
<div class="orders-container bg-white">
	<c:choose>
		<c:when test="${not empty listViewRecord}">
			<c:forEach items="${listViewRecord}" var="obj" varStatus="vs">
				<div class="stream-box no-margin">
					<div class="stream-top">
						<a class="stream-user router usercard-toggle">${obj.userName }</a>
						<span class="stream-message">
                             	查阅附件
						</span>
						<span class="stream-cal">${fn:substring(obj.recordCreateTime,0,16)}</span>
						</div>
				</div>
			</c:forEach>
		</c:when>
	</c:choose>
	</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>

