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
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
var time = 60;
function clock(){
	time--;
	if(time==0){
		$("#resend").html('');
			$("#resend").append("<a onclick=\"send()\">重新发送</a>");
	}else if(time>0){
		$("#resend").html('');
		$("#resend").append(time+"秒后重新发送");
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
				  art.dialog.alert("发送成功");
				}else{
				  art.dialog.alert(data.info);
				}
		  }
	});
}
setInterval("clock();",1000);

function direct(){
	art.dialog.open.origin.location.href='/login.jsp';
}
</script>
</head>
<body>
	<div class="bolck1" style="padding-top: 50px;text-align: center;line-height: 40px">
		<h1 style="font-size: 25px;">您已被该团队邀请</h1></br>
		<div style="font-size: 15px;">
			已向您的邮箱(${joinRecord.email}),发送了一封激活邮件。</br>请点击邮件中的链接完成激活!
		</div>
			<c:choose>
				<c:when test="${fn:endsWith(joinRecord.email, '@qq.com')}">
					<div  style="font-size: 15px;">
						<input type="button" class="blue_btn" value="进入邮箱" onclick="art.dialog.open.origin.self.location='http://mail.qq.com'"> 
					</div>
				</c:when>
				<c:when test="${fn:endsWith(joinRecord.email, '@sina.com')}">
					<div  style="font-size: 15px;">
						<input type="button" class="blue_btn" value="进入邮箱" onclick="art.dialog.open.origin.self.location='http://mail.sina.com.cn'"> 
					</div>
				</c:when>
				<c:when test="${fn:endsWith(joinRecord.email, '@sohu.com')}">
					<div  style="font-size: 15px;">
						<input type="button" class="blue_btn" value="进入邮箱" onclick="art.dialog.open.origin.self.location='http://mail.sohu.com'"> 
					</div>
				</c:when>
				<c:when test="${fn:endsWith(joinRecord.email, '@163.com')}">
					<div  style="font-size: 15px;">
						<input type="button" class="blue_btn" value="进入邮箱" onclick="art.dialog.open.origin.self.location='http://mail.163.com'"> 
					</div>
				</c:when>
				<c:when test="${fn:endsWith(joinRecord.email, '@126.com')}">
					<div  style="font-size: 15px;">
						<input type="button" class="blue_btn" value="进入邮箱" onclick="art.dialog.open.origin.self.location='http://mail.126.com'"> 
					</div>
				</c:when>
			</c:choose>
		<div style="padding-top: 10px">
			<span style="background-color: #f0f0f0;width: 100px;height: 30px" id="resend">
			60秒后重新发送
			</span>
			<span style="background-color: #f0f0f0;width: 100px;height: 30px;padding-left: 10px">
				<a href="javascript:void(0)" onclick="direct()">返回首页</a>
			</span>
		</div>
		</div>
</body>
</html>

