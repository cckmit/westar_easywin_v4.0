<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.pojo.Notification"%>
<%@page import="com.westar.base.cons.CommonConstant"%>
<%
	Notification notification=null;
	synchronized(session){
		if(session.getAttribute(CommonConstant.NOTIFICATION_CONTEXT)!=null){
			notification=(Notification)session.getAttribute(CommonConstant.NOTIFICATION_CONTEXT);
			session.removeAttribute(CommonConstant.NOTIFICATION_CONTEXT);
		}
	}
	if(notification!=null){
%>
<script type="text/javascript">

//页面初始化时获取后台消息提醒 
$(function(){
	showNotification(<%=notification.getType()%>,'<%=notification.getContent()%>');
});
</script>

<%
	}
%>

