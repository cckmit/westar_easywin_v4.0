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
<script type="text/javascript">
var sid="${param.sid}";
</script>
<style type="text/css">
.stream-top span{
margin-left: 15px
}
</style>
</head>
<body onload="resizeVoteH('${param.ifreamName}');">
<div class="orders-container bg-white no-margin-bottom" style="min-height: 50px">
	<c:choose>
		<c:when test="${not empty listViewRecord}">
			<c:forEach items="${listViewRecord}" var="obj" varStatus="vs">
				<div class="stream-box">
					<div class="stream-top" style="height:25px">
					
					<div class="ticket-user pull-left other-user-box" style="width:90px">
						<img src="/downLoad/userImg/${userInfo.comId}/${obj.userId}?sid=${param.sid}"
							class="user-avatar userImg" title="${obj.userName}" />
							<span class="user-name">
							<tags:cutString num="6">${obj.userName}</tags:cutString>
							</span>
					</div>
						
					<%-- 	<c:if test="${param.busType!='1'}">
							<span class="stream-message" style="position:relative;top:15px">
                             	<c:choose>
									<c:when test="${param.busType=='012'}">查看客户</c:when>
									<c:when test="${param.busType=='005'}">查看项目</c:when>
									<c:when test="${param.busType=='003'}">查看任务</c:when>
									<c:when test="${param.busType=='006'}">查看周报</c:when>
									<c:when test="${param.busType=='004'}">查看投票</c:when>
									<c:when test="${param.busType=='011'}">查看问题</c:when>
									<c:when test="${param.busType=='016'}">查看日程</c:when>
									<c:when test="${param.busType=='017'}">查看会议</c:when>
									<c:when test="${param.busType=='022'}">查看审批</c:when>
									<c:when test="${param.busType=='1'}">查看分享</c:when>
								</c:choose>
							</span>
						</c:if>
 --%>						
						<c:choose>
							<c:when test="${ obj.recordCreateTime != null}">
								<span class="stream-cal" style="position:relative;top:15px">${fn:substring(obj.recordCreateTime,0,16)}</span>
							</c:when>
							<c:otherwise>
								<span class="stream-cal" style="position:relative;top:15px;color: red;">
									<c:if test="${param.busType=='023'}">未观看</c:if>
									<c:if test="${param.busType!='023'}">未读</c:if>
								</span>
							</c:otherwise>
						</c:choose>
						
						</div>
				</div>
			</c:forEach>
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

