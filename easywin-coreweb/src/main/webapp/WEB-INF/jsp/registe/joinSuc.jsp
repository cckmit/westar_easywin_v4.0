<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<%pageContext.setAttribute("requestURI",request.getRequestURI().replace("/","_").replace(".","_"));%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<link rel="stylesheet" type="text/css" href="/static/css/new_file.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
var time = 3;

function clock(){

time--;

document.getElementById("info").innerHTML = "<font size='5px'>申请成功！公司管理员会尽快审核！</font><br><font size='3px'>窗口将在"+time+"秒后自动关闭 </font>";
	if(time==0){
		art.dialog.close();
	}
}

setInterval("clock();",1000);
</script>
</head>
<body>
	<div style="padding-top: 100px;padding-left: 50px;text-align: center;line-height: 50px">
		<span id="info">
			
		</span>
	</div>
</body>
</html>
