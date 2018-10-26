<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static_core.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/jsV2/autoTextarea.js"></script>
<script type="text/javascript">
	$(function(){
		//初始化名片
		initCard('${param.sid}');
	});
</script>
<style>
.ws-wrap-width .ws-wrap-right{
width: 850px;
}
</style>
</head>
<body style="background-color:#FFFFFF" onload="resizeVoteH('${param.ifreamName}');window.parent.resizeWin();">
<form method="post" id="delForm" class="subform">
	<div>
		<div class="ws-clear"></div>
		<c:if test="${not empty reminders}">
			<c:forEach items="${reminders}" var="reminder" varStatus="vs">
				<div class="ws-shareBox ws-shareBox2">
					<div class="shareHead" data-container="body" data-toggle="popover"  
						data-user='${reminder.userId}' data-busId='${reminder.busId}' data-busType='003'>
						<img src="/downLoad/userImg/${reminder.comId}/${reminder.userId}?sid=${param.sid}" title="${reminder.userName }"></img>
					</div>
					<div class="shareText">
						<span class="ws-blue">${reminder.userName}</span>
						<p class="ws-texts">
							<tags:viewTextArea>${reminder.content}</tags:viewTextArea>
						</p>
						<div class="ws-type">
							<time>${reminder.recordCreateTime}</time>
						</div>
					</div>
					<div class="ws-clear"></div>
				</div>
			</c:forEach>
		</c:if>
		<div align="center" style="display: ${empty reminders?'block':'none'}" id="noTalks"><h3 style="font-size:16px">没有催办记录！</h3></div>
	</div>
	<tags:pageBar url="/taskReminder/taskReminderRecord"></tags:pageBar>
</form>
 <div style="clear: both;padding-top: 100px;">
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
