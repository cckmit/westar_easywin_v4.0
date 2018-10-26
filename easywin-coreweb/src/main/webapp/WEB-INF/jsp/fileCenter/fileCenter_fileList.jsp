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
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/fileJs/fileCenter_fileList.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">

var pageTag = 'fileCenter';
var EasyWin = {
		"sid" : "${param.sid}",
		"userInfo" : {
			"userId" : "${userInfo.id}",
			"userName" : "${userInfo.userName}",
			"comId" : "${userInfo.comId}",
			"orgName" : "${userInfo.orgName}",
			"isAdmin" : "${userInfo.admin}",
		}
	};
</script>
<style>
.btn5{
	padding:6px 7px;
}
</style>
</head>
<body>
	<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
				<!-- 列表-->
				<jsp:include page="/WEB-INF/jsp/fileCenter/fileCenter_middle.jsp"></jsp:include>
	        </div>
	        <!--主题颜色设置按钮-->
			<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	    </div>
</body>
</html>
