<%@page language="java" import="java.util.*" pageEncoding="UTF-8" session="false" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<link rel="stylesheet" type="text/css" href="/static/css/new_file.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/objectSwap.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/login.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>

<script type="text/javascript">
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError:true
		});
	});
	$('#loginName').focus();
</script>
<script type="text/javascript">
$(function() {
	var support = getBrowserVersion('register',1);
	if(support){//浏览器支持
		 art.dialog.open("/registe/finishInfoPage?confirmId=${confirmId}&rnd=" + Math.random(), {
			    title:"信息完善",
				lock : true,
				max : false,
				min : false,
				width : 600,
				height : 400,
				close : function() {
				}
			});
		   $("#body").find("#browsertip").remove();
	}else{//浏览器不支持
	   $("#body").find(".ws-head-right").remove();
	   $("#body").find("#contentForm").remove();
	}
});

$(function(){
	//团队创建
	$("#newUser").click(function(){
		 art.dialog.open("/registe/registePage?rnd=" + Math.random(), {
			   id:'finishInfo',
			    title:"团队注册",
				lock : true,
				max : false,
				min : false,
				width : 600,
				height : 400,
				close : function() {
				}
			});
	});
	//加入团队
	$("#join").click(function(){
		 art.dialog.open("/registe/joinOrgPage?rnd=" + Math.random(), {
			    title:"加入团队",
				lock : true,
				max : false,
				min : false,
				width : 600,
				height : 400,
				close : function() {
				}
			});
	});
	//找回密码
	$("#findPassword").click(function(){
		 art.dialog.open("/registe/updatePasswordPage?rnd=" + Math.random(), {
			    title:"找回密码",
				lock : true,
				max : false,
				min : false,
				width : 600,
				height : 400,
				close : function() {
				}
			});
	});
});

function checkInfo(form){
	var email = $("#loginName").val();
	var password = $("#password").val();
	if(strIsNull(password)){
		$("#password").focus();
		$("#password").blur();
		return;
	}
	var yzm = $("#yzm").val();
	if(strIsNull(yzm)){
		$("#yzm").focus();
		$("#yzm").blur();
		return;
	}
	if(!strIsNull(email) && !strIsNull(password)){
		$.ajax({
			  type : "POST",
			  url : "/login/listOrg?rnd=" + Math.random(),
			  dataType:"json",
			  data:{email:email,password:password,yzm:yzm},
			  success : function(data){
				  if(data.status=='n'){
					  showNotification(1,data.info);
						if(data.input=='emailF'){
							 $("#loginName").focus()
						}else if(data.input=='passF'){
							$("#password").focus();
						}else if(data.input=='yzmF'){
							$("#yzm").focus();
							$("#yzm").blur();
						}
					  return;
				  }
				  if(data.list.length<1){
					  art.dialog.alert("该账号没有激活或不存在！").time(1);
				  }else  if(data.list.length==1){
					  //只有一个则直接登陆
					  $("#comId").val(data.list[0].orgNum);
					  $(form).submit();
				  }else{
					 var comId =  $("#comId").val();
					 if(!strIsNull(comId)){
						 $(form).submit();
					 }else{
						 art.dialog.data("list",data.list)
						  art.dialog.open("/login/listChooseOrg?rnd=" + Math.random(), {
							    title:"登陆团队选择",
								lock : true,
								max : false,
								min : false,
								width : 450,
								height : 400,
								close : function() {
								}
							});
					 }
				  }
			  }
			});
	}else{
		return false;
	}
}


function alterInfo(comId,userId,sid){
	art.dialog.open("/userInfo/addInfoPage?sid="+sid+"&comId="+comId+"&id="+userId, {
		title :"个人配置",
		lock : true,
		max : false,
		min : false,
		width : 600,
		height :460,
	    button: [
	  	        {
	  	            name: '不再提醒',
	  	            callback: function () {
	  	            	$.post("/userInfo/alterInfo?sid="+sid,{Action:"post"},     
  							function (data){
  						},"json");
	  	            	return true;
	  	            }
	  	        }
	  	 ],
	    cancelVal: '取消',
	    close:function(){
			window.location.href="/index?sid="+sid;
	    },
	    cancel: true
	});
}

</script>
</head>
<body id="body">
	<div class="ws-head">
		<div class="ws-head-in">
			<h1>logo</h1>
			<ul class="ws-head-right">
				<li>
					<button id="newUser">注册</button>
				</li>
				<li>
					<button id="join">加入团队</button>
				</li>

			</ul>
		</div>
	</div>
	<!--
       	作者：13611266435@163.com
       	时间：2015-07-23
       	描述：head end
       -->
	<div class="ws-content" id="contentForm">
		<div class="ws-login">
			<form class="subform" action="/login" method="post" id="loginForm">
				<input  type="hidden" name="comId" value="" id="comId"/>
				<div>
                  <input type="text" name="loginName" id="loginName" placeholder="请输入用户名" nullmsg="请输入用户名" class="users"/>
				</div>
				<div>
                  <input type="password" id="password" name="password" placeholder="请输入密码" nullmsg="请输入密码" class="ws-locked"/>
				</div>
                   <div>
	                  <input type="test" id="yzm" name="yzm" datatype="*" placeholder="请输入验证码"  nullmsg="请输入验证码"
	                  ajaxurl="/registe/checkYzm/login" class="locked" style="width: 230px"/>
	                  <img style="height: 30px;vertical-align: middle" src="/registe/AuthImage/login" id="yz" width="100" 
							onclick="this.src='/registe/AuthImage/login?rnd=' + Math.random();$('#yzm').val('')"/>
				  </div>
                  <button type="submit" onclick="checkInfo(this.form);return false;">登录</button>
                  <p>
                  	<input type="checkbox" id="autoLogin" name="autoLogin" value="yes" style="background-image:none;width: auto;height: auto;float: left;margin: 0px;margin-top: 3px;padding: 0px"/>
                  	<a id="rememberPass" href="javascript:void(0)" style="float: left;cursor: pointer;margin-left: 4px" onclick="$('#autoLogin').attr('checked','true')">记住密码</a>
                  	<a href="javascript:void(0)" id="findPassword">忘记密码？</a>
                  </p>
                </form>
		</div>
		<p class="ws-company">版权所有：成都西辰有限公司</p>
	</div>
	
	<div id="browsertip">
		<div class="browsertip" style="width: 780px;text-align: center;">
			<p>检测您正在使用低版本的IE浏览器</p>
			<p>为保证最佳体验请使用<strong>IE9及以上版本IE浏览器</strong>或者下载以下浏览器：</p>
			<ul class="browserlist">
				<li><a class="chrome" href="https://www.baidu.com/s?wd=chrome" target="_blank">Chrome</a></li>
				<li><a class="firefox" href="http://www.firefox.com.cn/" target="_blank">FireFox</a></li>
				<li><a class="safari" href="http://support.apple.com/zh_CN/downloads/#safari" target="_blank">Safari</a></li>
			</ul>
		</div>
		<p class="ws-company">版权所有：成都西辰有限公司</p>
	</div>
</body>
</html>


