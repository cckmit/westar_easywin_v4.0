<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript">

	$(document).ready(function() {
		resizeVoteH('otherItemAttrIframe');
	});
</script>
</head>
<body style="background-color:#FFFFFF;">

<div class="orders-container bg-white no-margin-bottom">
	<c:choose>
		<c:when test="${not empty listFlowRecord}">
			<c:forEach items="${listFlowRecord}" var="obj" varStatus="vs">
				<div class="stream-box">
					<div class="stream-top" style="height:25px">
					
					<div class="ticket-user pull-left other-user-box" style="width:90px">
					<c:choose>
								<img class="user-avatar"  src="/downLoad/userImg/${obj.comId}/${obj.userId}?sid=${param.sid}" title="${obj.userName }"></img>
								<span class="user-name">
								<tags:cutString num="6">${obj.userName}</tags:cutString>
							</span>
					</div>
						
						<span class="stream-message" style="position:relative;top:15px">
                             	<c:choose>
                             		<c:when test="${vs.last}">
                             		创建项目
                             		</c:when>
                             		<c:when test="${obj.state==1}">办结</c:when>
                             		<c:otherwise>
                             		负责项目
                             		</c:otherwise>
                             	</c:choose>
						</span>
						<span class="stream-cal" style="position:relative;top:15px">${fn:substring(obj.acceptDate,0,16)}</span>
						</div>
				</div>
			</c:forEach>
		</c:when>
	</c:choose>
	</div>
	
<tags:pageBar url="/item/itemFlowRecord"></tags:pageBar>
	<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>