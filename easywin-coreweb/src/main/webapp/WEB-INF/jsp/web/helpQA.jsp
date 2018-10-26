<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>捷成协同办公平台</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="协同  OA 云平台">
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<!--
<link rel="stylesheet" type="text/css" href="styles.css">
-->
<style type="text/css">
html,body {font-size:14px; font-size-adjust:none; font-stretch:normal; font-style:normal; font-variant:normal; 
font-weight:normal;text-align:left; font-family:"微软雅黑"; color:#404040; list-style: none;
background-color: #fff}
.FAQ-box {
	width: 99%;
	margin: 0 auto 30px;
	padding-top: 20px;
	color: #5f626d;
}

.FAQ-header {
	border-bottom: 2px solid #e8eef3;
	height: 40px;
	line-height: 40px;
}

.pull-left {
	float: left;
}

.FAQ-title {
	border-bottom: 2px solid #2298DB;
	height: 40px;
	font-size: 18px;
	padding: 0 10px;
	color: #25A3EC;
}

.QA dl {
	border-bottom: 1px solid #ededed;
	margin-top: 20px;
	padding-bottom: 20px;
}

.QA dl dt {
	color: #737373;
	font-size: 18px;
	cursor: pointer;
}

.QA dl dd {
	display: none;
	margin-top: 16px;
	overflow: hidden;
}

.QA dl img {
	margin-left: 5px;
	margin-right: 15px;
	vertical-align: middle;
}

.QA dl dd img {
	float: left;
}

.QA dl dd p {
	color: #9a9a9a;
	font-size: 14px;
	line-height: 26px;
	margin-left: 40px;
}
</style>
</head>
<body onload="resizeVoteH('helpAns');">
	<div class="FAQ-box" style="padding-top:0px;">
		<div class="FAQ-header">
			<div class="pull-left FAQ-title">${helpVo.name}</div>
		</div>
		<div class="QA">
			<c:choose>
				<c:when test="${not empty listHelp}">
					<c:forEach items="${listHelp}" var="vo" varStatus="vs">
						<dl data-maidian="">
							<dt>
								<img src="static/images/web/Q.png" alt="" width="20px"
									height="21px">${vo.name}
							</dt>
							<dd style="margin-left:1px;">
								<img src="static/images/web/A.png" alt="" width="20px"
									height="21px">
								<div style="margin-left:40px;">${vo.content}</div>
							</dd>
						</dl>
					</c:forEach>
				</c:when>
				<c:when test="${empty helpVo.ansList}">
					<h3>暂无描述</h4>
				</c:when>
			</c:choose>
		</div>
	</div>
	<%--用与测量当前页面的高度 --%>
	<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
<script type="text/javascript">
	$(document).ready(function() {
		var i = -1;
		$("dl").on("click", function() {
			//var maidian = $(this).attr('data-maidian')
			//window.goldlog ? goldlog.record('/ding.10000', maidian, '', 'H46747591') : setTimeout(arguments.callee, 200);
			if (i == $(this).index()) {
				$("dl dd").eq(i).slideUp("slow", function() {
					resizeVoteH('helpAns');
				});
				i = -1;
				return;
			} else if (i > -1) {
				$("dl dd").eq(i).slideUp("slow", function() {
					resizeVoteH('helpAns');
				});
			}
			$("dl dd").eq($(this).index()).slideDown("slow", function() {
				resizeVoteH('helpAns');
			});
			i = $(this).index();
			$.each($(this).find("p"), function(index, item) {
				if (index > 0) {
					$(this).css("clear", "both")
				}
			});
		})
	});
</script>