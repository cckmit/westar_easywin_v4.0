<%@page import="com.westar.core.web.InitServlet"%>
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
<title>激活邮箱</title>
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="/static/plugins/Validform/css/validform.css" >
<script type="text/javascript" src="/static/plugins/Validform/js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>

<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>

<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
var time = 60;
function clock(){
	time--;
	if(time==0){
		$("#resend").html('');
		$("#resend").attr("class","sendBtn");
		$("#resend").append("<a onclick=\"send()\" style='color:inherit;'>重新发送</a>");
	}else if(time>0){
		$("#resend").html('');
		$("#resend").append("重新发送("+time+"s)");
	}
}
function send(){
  	time= 60;
	var confirmId = '${joinRecord.confirmId}';
	$.ajax({
		  type : "post",
		  url : "/registe/sendEmail?rnd="+Math.random(),
		  dataType:"json",
		  data:{confirmId:confirmId},
		  success : function(data){
			  if('y'==data.status){
				  layer.alert("发送成功",{icon:6});
				}else{
				  layer.alert(data.info,{icon:5});
				}
		  }
	});
}
setInterval("clock();",1000);

function direct(){
	window.location.href='/login.jsp';
}
$(function(){
	$(".zc-btn").remove();
})
</script>
</head>
<body>
<!----头部开始---->
	<div class="top">
		<jsp:include page="/WEB-INF/jsp/web/head.jsp"></jsp:include>
	</div>
	
	<div style="padding-bottom: 50px">
		<div class="browsertip" style="width: 780px;text-align: center;color:gray">
			<h1 style="font-size: 25px;">还差一步，即可完成注册</h1><br/>
			<div style="font-size: 15px;line-height: 30px">
				我们已经向您的邮箱(${joinRecord.email}),发送了一封激活邮件。<br/>请点击邮件中的链接完成注册!
			</div>
			<div style="font-size: 15px;margin: 10px 0px">
				<c:set var="hurl">
					<c:choose>
						<c:when test="${fn:endsWith(joinRecord.email, '@qq.com')}">
							http://mail.qq.com
						</c:when>
						<c:when test="${fn:endsWith(joinRecord.email, '@sina.com')}">
							http://mail.sina.com.cn
						</c:when>
						<c:when test="${fn:endsWith(joinRecord.email, '@sohu.com')}">
							http://mail.sohu.com
						</c:when>
						<c:when test="${fn:endsWith(joinRecord.email, '@163.com')}">
							http://mail.163.com
						</c:when>
						<c:when test="${fn:endsWith(joinRecord.email, '@126.com')}">
							http://mail.126.com
						</c:when>
					</c:choose>
				</c:set>
				<c:if test="${not empty hurl }">
					<a style="float: none;background-color:#31aafd;padding:5px 10px;color: #fff;border-radius: 5px;" href="${hurl}">进入邮箱</a> 
				</c:if>
			</div>
			<div style="padding-top: 10px">
				<span id="resend" class="resendBtn" style="padding: 5px 10px">
				重新发送(60s)
				</span>
				<span style="width: 100px;height: 30px;padding-left: 10px">
					<a href="/web/index" style="float: none;color:#31aafd;">返回首页</a>
				</span>
			</div>
		</div>
	</div>
		
		<!--登录框 E-->
	<div class="bottom">
		<jsp:include page="/WEB-INF/jsp/web/bottom.jsp"></jsp:include>
	</div>
</body>
</html>

