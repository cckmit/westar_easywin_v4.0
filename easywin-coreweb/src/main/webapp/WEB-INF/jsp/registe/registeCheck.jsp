<%@page import="com.westar.core.web.InitServlet"%>
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
		$("#resend").attr("class","resendBtn");
		$("#resend").html('');
		$("#resend").append("重新发送("+time+"s)");
	}
}
//发送注册验证码
function send(){
  	time= 60;
	var account='${account}';
	var joinTempId = '${joinTempId}';
	$.ajax({
		  type : "post",
		  url : "/registe/sendRegistYzm?rnd="+Math.random(),
		  dataType:"json",
		  data:{account:account,joinTempId:joinTempId},
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

//跳转到第三步
function registOrgList(){
	var account='${account}';
	var joinTempId = '${joinTempId}';
	var yzm = $("#yzm").val();
	if(yzm){
		$.ajax({
			  type : "post",
			  url : "/registe/checkRegistYzm?rnd="+Math.random(),
			  dataType:"json",
			  data:{account:account,joinTempId:joinTempId,yzm:yzm},
			  success : function(data){
				  if(data.status=='f'){
					  layer.alert(data.info,{icon:5}); 
				  }else{
					  window.location.replace("/registe/registeWayPage?account="+encodeURIComponent(account)+"&joinTempId="+joinTempId);
				  }
			  }
		});
	}else{
		
	}
	
}
</script>
<style>
	.pop-content dl{
		line-height: 30px
	}
	.stepLi{
		width: 290px;
		height: 52px;
		text-align: center;
		line-height: 52px;
		color: #fff;
	}
	.step1{
		background: url(/static/images/web/regist-step-1-1.png) no-repeat;
	}
	.step2{
		background: url(/static/images/web/regist-step-2-1.png) no-repeat;
	}
	.step3{
		color: #000;
		background: url(/static/images/web/regist-step-3-0.png) no-repeat;
	}
</style>
</head>
<body>
	<!----头部开始---->
	<div class="top">
		<jsp:include page="/WEB-INF/jsp/web/head.jsp"></jsp:include>
	</div>
	<!--注册 S-->
    <div class="register-box" style="padding-bottom: 30px">
    	<div class="flow-box">
    		<ul class="flow-menu">
    			<li class="stepLi step1">1、账号注册 </li>
    			<li class="stepLi step2">2、账号验证</li>
    			<li class="stepLi step3">3、创建或加入团队</li>
    		</ul>
    	</div>
    	
    	<div class="browsertip" style="width: 780px;text-align: center;color:gray;">
    		<c:choose>
    			<c:when test="${fn:indexOf(account,'@')>0}">
					<h1 style="font-size: 25px;">邮箱账号验证</h1><br/>
					<div style="font-size: 15px;line-height: 30px">
						为保证邮箱地址可用，验证码已发送到您的邮箱(${account})。<br/>请输入邮箱中的验证码!
						<c:set var="hurl">
							<c:choose>
								<c:when test="${fn:endsWith(account, '@qq.com')}">
									http://mail.qq.com
								</c:when>
								<c:when test="${fn:endsWith(account, '@sina.com')}">
									http://mail.sina.com.cn
								</c:when>
								<c:when test="${fn:endsWith(account, '@sohu.com')}">
									http://mail.sohu.com
								</c:when>
								<c:when test="${fn:endsWith(account, '@163.com')}">
									http://mail.163.com
								</c:when>
								<c:when test="${fn:endsWith(account, '@126.com')}">
									http://mail.126.com
								</c:when>
							</c:choose>
						</c:set>
						<c:if test="${not empty hurl }">
							<a style="float: none;background-color:#31aafd;padding:5px 10px;color: #fff;border-radius: 5px;" 
							href="${hurl}" target="_blank">进入邮箱</a> 
						</c:if>
					</div>
					
					<div class="form-group" style="height: 42px;padding-left: 150px">
		    			<label>邮箱验证码:<font style="color: red;padding:0px 5px">*</font></label>
		    			<div style="float:left;">
			    			<input class="input-form" type="text" style="width:250px;" 
			    			id="yzm" name="yzm" />
			    			&nbsp;&nbsp;
							<span id="resend" class="resendBtn" style="padding: 5px 10px">
								重新发送(60s)
							</span>
		    			</div>
		    		</div>
    			</c:when>
    			<c:otherwise>
					<h1 style="font-size: 25px;">手机账号验证</h1><br/>
					<div style="font-size: 15px;line-height: 30px">
						为保证手机号可用，验证码已发送到您的手机(${account})。<br/>请输入手机验证码!
					</div>
					
					<div class="form-group" style="height: 42px;padding-left: 150px">
		    			<label>手机验证码:<font style="color: red;padding:0px 5px">*</font></label>
		    			<div style="float:left;">
			    			<input class="input-form" type="text" style="width:250px;" 
			    			id="yzm" name="yzm"/>
			    			&nbsp;&nbsp;
							<span id="resend" class="resendBtn" style="padding: 5px 10px">
								重新发送(60s)
							</span>
		    			</div>
		    		</div>
    			</c:otherwise>
    		</c:choose>
    		<div style="font-size: 15px;margin: 10px 0px;padding-left: 100px">
		    	<a href="javascript:void(0)" class="next-btn"  onclick="registOrgList();">下一步</a>
    		</div>
		</div>
 </div>
	<!--登录框 E-->
	<div class="bottom">
		<jsp:include page="/WEB-INF/jsp/web/bottom.jsp"></jsp:include>
	</div>
</body>

</html>
