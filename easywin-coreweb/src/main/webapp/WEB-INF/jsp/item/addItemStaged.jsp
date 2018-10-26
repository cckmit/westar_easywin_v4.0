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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<link rel="stylesheet" type="text/css" href="/static/css/new_file.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
		$(document).ready(function(){
			$("#stagedName").focus();
		});
	})
	function formSub(){
		$(".subform").submit();
	}
</script>
</head>
<body>
	<div class="ws-question ws-question-in" style="width:350px;">
	<form class="subform" method="post" action="/item/addStagedFolder?sid=${param.sid}">
	<tags:token></tags:token>
	<input type="hidden" name="itemId" value="${itemId}"/>
	<input type="hidden" name="parentId" value="-1"/>
	<div class="ws-message" style="padding:10px;">
		<ul>
			<li class="ws-input-height">
				<label style="width:55px;"><span>*</span>名称： </label>
				<input type="text" id="stagedName" datatype="*" name="stagedName" value="" style="width:160px;" />
			</li>
			<li class="ws-input-height">
				<label style="width:55px;"><span>*</span>排序： </label>
				<input type="text" id="stagedOrder" datatype="n" name="stagedOrder" style="width:160px;" value="${initStagedOrder.stagedOrder}" />
			</li>
		</ul>
	</div>
	</form>
</div>
</body>
</html>
