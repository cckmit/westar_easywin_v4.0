<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<title>登陆跳转</title>
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>
<script type="text/javascript">
function setWindow(winDoc,win){
	 //跳转到第一页
	 var url = win.location+'';
	 if(url.match(/pager.offset=(\d+)/)){//查询分页数
		 url = url.replace(/pager.offset=(\d+)/,'pager.offset=0');
	 }
	 win.self.location = url;
}

//顶部页面跳转到展示数据的第一页
//window.top.location = urlPageP2First();
//closeWindow();
</script>
</body>
</html>

