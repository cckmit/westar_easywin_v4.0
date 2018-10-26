<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>捷成协办办公平台</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/static/plugins/Validform/css/validform.css" >
<script type="text/javascript" src="/static/plugins/Validform/js/Validform_v5.3.2_min.js"></script>
<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">
<script type="text/javascript" src="/static/js/cookieInfo.js"></script>
	<script type="text/javascript" src="/static/js/jquery.md5.js"></script>
<script type="text/javascript" src="/static/js/login.js"></script>
<script type="text/javascript" src="/static/js/jquery.JPlaceholder.js"></script>
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
$(function(){
	$(".login-btn").remove();
	//表单input回车提交
	$(".subform").find("input[type='text']").keydown(function(e) {
        if (e.keyCode == 13) {
        	$(".btn-login").click();
        }
	});
	$("#password").keydown(function(e) {
		if (e.keyCode == 13) {
			$(".btn-login").click();
        }
	});
})


</script>
</head>
<!-- /Head -->
<!-- Body -->
<!--[if lt IE 9]>
	<body onload="getInfoBs();getBrowserVersion('login','0');" style="display: block" id="body">
<![endif]-->
<!--[if gte IE 9]>
	<body onload="getInfoBs();getBrowserVersion('login','1');" style="display: block" id="body">
<![endif] -->
<!--[if !IE]><!--> 
	<body onload="getInfoBs();getBrowserVersion('login','0');" style="display: block" id="body">
<!--<![endif]-->	
	<!----头部开始---->
	<div class="top">
		<jsp:include page="/WEB-INF/jsp/web/head.jsp"></jsp:include>
	</div>
    <!----头部结束---->
    <!--登录框 E-->
	<div class="login-content" id="contentForm">
		<div class="login-content-in clearfix">
			<div class="pic pull-left padding-top-50 padding-left-50">
				<img src="/static/images/web/banner-pic.png"/>
			</div>
			<div class="login-container pull-left margin-top-50 no-margin-bottom margin-left-50">
				<form class="subform" action="/login" method="post" id="loginForm">
					<input type="hidden" name="comId" value="" id="comId"/>
					<input type="hidden" name="isSysUser" value="1" id="isSysUser"/>
					<input type="hidden" name="cookieType" value="" id="cookieType"/>
					<input type="hidden" name="passwordMD5" value="" id="passwordMD5"/>
			        <div class="loginbox ps-login-box-bg">
			            <div class="loginbox-title margin-bottom-10">Hi!欢迎登录账户 </div>
			            <div class="loginbox-textbox" style="height: 40px">    
							<input class="form-control ps-form-control"  name="loginName" 
							id="loginName" datatype="*" type="text" placeholder="邮箱/手机号/别名" >
			            </div>
			            <div class="loginbox-textbox" style="height: 40px">
							<input class="form-control ps-form-control" id="password" name="password" datatype="*"
							 type="password" placeholder="密码">
			            </div>
			            <div class="loginbox-textbox" style="text-align: left;height: 40px;">
							<input class="form-control ps-form-control" id="yzm" name="yzm" datatype="*"
							 type="text" placeholder="验证码" ajaxurl="/registe/checkYzm/login" 
							 class="ws-yzm pull-left" style="width: 200px;float:left" maxlength="4">
							 <img style="height: 34px;vertical-align: middle;border-radius: 5px;padding-left: 5px;float: left" 
							 src="/registe/AuthImage/login" id="yz" width="100" 
							onclick="this.src='/registe/AuthImage/login?rnd=' + Math.random();$('#yzm').val('')"/>
			            	<div style="clear: both"></div>
			            </div>
			            <div class="loginbox-forgot clearfix">
			            	<div class="checkbox no-margin pull-left">
								<label class="no-padding">
									<input type="checkbox" id="autoLogin" name="autoLogin" value="yes">
									<span class="text">记住密码</span>
								</label>
							</div>
			                <a href="javascript:void(0)" class="pull-right" id="findPassword">忘记密码?</a>
			            </div>
			            <div class="loginbox-submit">
			                <a href="javascript:void(0)" class="btn-login" onclick="checkInfo(this.form);">登录</a>
			            </div>
			            <!-- 
			            <div class="loginbox-or margin-top-10">
			                <div class="or-line"></div>
			                <div class="or">其他登录方式</div>
			            </div>
			            <div class="loginbox-social">
			                <div class="social-buttons animated fadeInDown">
			                    <a href="" class="ps-qq"></a>
			                    <a href="" class="ps-weibo"></a>
			                    <a href="" class="ps-weixin"></a>
			                </div>
			            </div>
			             -->
			        </div>
		        </form>
		    </div>
		</div>		
	</div>
	
	<div id="browsertip" style="padding-bottom: 50px;display: none">
		<div class="browsertip" style="width: 780px;text-align: center;">
			<p>检测您正在使用低版本的IE浏览器或兼容模式</p>
			<p>为保证最佳体验请使用<strong>IE9及以上版本IE浏览器（非兼容模式）</strong>或者下载以下浏览器：</p>
			<ul class="browserlist">
				<li><a class="chrome" href="https://www.baidu.com/s?wd=chrome" target="_blank">Chrome</a></li>
				<li><a class="firefox" href="http://www.firefox.com.cn/" target="_blank">FireFox</a></li>
				<li><a class="safari" href="http://support.apple.com/zh_CN/downloads/#safari" target="_blank">Safari</a></li>
			</ul>
		</div>
	</div>
	<!--登录框 E-->
	<div class="bottom">
		<jsp:include page="/WEB-INF/jsp/web/bottom.jsp"></jsp:include>
	</div>
</body>
<!--  /Body -->