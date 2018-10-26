<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/cookieInfo.js"></script>
<script type="text/javascript">
	$(function(){
		
		var c_i_p_m_l = localStorage.getItem('c_i_p_m_l');
    	var i_p_m_l_c = localStorage.getItem('i_p_m_l_c');
    	
        localStorage.removeItem('l_c_i_p_m'); 
    	localStorage.removeItem('c_i_p_m_l');
    	localStorage.removeItem('i_p_m_l_c');
    	localStorage.removeItem('a_i_p_m_l_c');
    	delCookie("p_m_l_c_i");
    	
    	delCookie('${param.sid}');
    	sessionStorage.setItem("i_p_m_l_c",i_p_m_l_c);
    	setCookie("c_i_p_m_l",c_i_p_m_l);

    	window.top.location.href='/login.jsp';
    	
    	sessionStorage.removeItem("headMenu");
	});
</script>
</head>
<body>

</body>
</html>
