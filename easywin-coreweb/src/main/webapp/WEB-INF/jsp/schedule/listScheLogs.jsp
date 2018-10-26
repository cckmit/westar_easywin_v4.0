<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.PaginationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">

	$(document).ready(function() {
		resizeVoteH('otherScheIframe');
	});
</script>
</head>
<body style="background-color:#FFF;">

<div class="orders-container bg-white">
	<c:choose>
		<c:when test="${not empty listScheLogs}">
			<c:forEach items="${listScheLogs}" var="listScheLogVo" varStatus="vs">
				<div class="comment ps-border-bottom">
			
					<img src="/downLoad/userImg/${listScheLogVo.comId}/${listScheLogVo.speaker}?sid=${param.sid}" title="${listScheLogVo.speakerName}" class="comment-avatar"></img>
                     <div class="comment-body">
                         <div class="comment-text ps-no-bordered">
                             <div class="comment-header">
                                 <a href="javascript:void(0)" title="">${listScheLogVo.speakerName}</a><span>${listScheLogVo.recordCreateTime}</span>
                             </div>
                             <p class="no-margin-bottom">${listScheLogVo.content}</p>
                         </div>
                 </div>
             </div>
			
			</c:forEach>
		</c:when>
	</c:choose>
	</div>
<tags:pageBar url="/schedule/listScheLogs"></tags:pageBar>	
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
