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
		var sid = '${param.sid}';
		var c_i_p_m_l = localStorage.getItem('c_i_p_m_l');
    	var i_p_m_l_c = localStorage.getItem('i_p_m_l_c');
    	//被强制挤出来的直接删除本地数据
    	if(c_i_p_m_l && i_p_m_l_c ){
        	
    		//cookie中保留的用户加密主键
    		var onlineUserId = getCookie("i_p_m_l_c");
    		//当前强制退出的用户
   			var userId = getCookie(sid);
    		if(onlineUserId && userId){
   				//cookie中保留的用户主键与本地数据是否一致
   				if(onlineUserId==userId){//一致，则删除本地数据
   	   				
			        localStorage.removeItem('l_c_i_p_m'); 
			    	localStorage.removeItem('c_i_p_m_l');
			    	localStorage.removeItem('i_p_m_l_c');
			    	delCookie("p_m_l_c_i");
       			}
    		} 
        	
    	}
    	delCookie(sid);
    	sessionStorage.setItem("i_p_m_l_c",i_p_m_l_c);
    	setCookie("c_i_p_m_l",c_i_p_m_l);
    	
    	var timer;
    	window.top.layer.open({
    		title:"2秒后关闭",
    		icon:6,
    		shade:0.3,
    		content:'对不起，您还没有登录或者当前会话已经过期！请重新登录后在进行相关操作！',
    		success:function(layero,index){
    			var that = this, i = 2;
    			var fn = function () {
    				layer.title(i + '秒后关闭',index);
    	            !i && layer.close(index);
    	            i --;
    	        };
    	        
    	        timer = setInterval(fn, 1000);
    	        fn();
    		},end:function(index){
    			clearInterval(timer);
		    	window.top.location.href='/login.jsp';
    		}
    	})
    	
    	 sessionStorage.removeItem("headMenu");
    	
	});
	
	
</script>
</head>
<body>

</body>
</html>
