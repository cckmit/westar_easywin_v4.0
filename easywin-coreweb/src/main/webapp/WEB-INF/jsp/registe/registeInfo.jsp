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
	$(function() {
		//新用户
		var $passwdNewDiv = $("#passwdNewDiv").html();
		//老用户
		var $passwdOldDiv = $("#passwdOldDiv").html();
		//先前的账号
		var preAccount = "";
		var prePasswd = "";
		//$(".login-btn").remove();
		$(".zc-btn").remove();
		var vali = $(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			datatype:{
				"passwd":function(gets,obj,curform,regxp){
					var passwd = $(obj).val();
					if(!strIsNull(passwd)){
						var status = 'f';
						var account = $("#account").val();
						$.ajax({  
					        url : "/registe/checkRegistPasswd?t="+Math.random(),  
					        type : "POST",  
					        dataType : "json",
					        async:false,
					        data:{account:account,passwd:passwd},
					        success : function(data) { 
					        	status = data.status;
					        }  
					    });
						if(status=='y'){
			        		return true;
			        	}else{
			        		return "该账号已在系统中，请输入统一密码！";
			        	}
					}else{
						return false;
					}
				},"account":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(!strIsNull(str)){
						//是否需要输入两次密码
						var len = $("#passwdSetDiv").children("div").length;
						//邮箱验证
						var regE = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
						//数字验证
						var regNum = /^\d+$/
						var regTel = /^13[0-9]{9}$|14[0-9]{9}$|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/
						if(str.indexOf("@")>0){//判断包含特殊字符
							if(!regE.test(str)){//不是邮箱
								$("#userName").val('');
				        		$("#userName").removeAttr("disabled");
				        		if(len==1){
					        		$("#passwdSetDiv").html($passwdNewDiv);
					        		vali.addRule();
				        		}
								return "请输入正确的邮箱格式";
							}
						}else if(regNum.test(str)){//不包含特殊字符，有数字组成
							if(str.length!=11 || !regTel.test(str)){//不是手机号
								$("#userName").val('');
				        		$("#userName").removeAttr("disabled");
				        		if(len==1){
					        		$("#passwdSetDiv").html($passwdNewDiv);
					        		vali.addRule();
				        		}
								return "请输入正确的手机号";
							}
						}else{
							$("#userName").val('');
			        		$("#userName").removeAttr("disabled");
			        		if(len==1){
				        		$("#passwdSetDiv").html($passwdNewDiv);
				        		vali.addRule();
			        		}
							return "请输入正确的邮箱格式";
						}
						
						if(preAccount==str){//两次若是项目，则不调用后台数据
							return true; 
						}
						//两次数据不同则从后台取数据
						preAccount = str;
						//ajax获取用户信息
						$.ajax({  
					        url : "/registe/checkAccount?t="+Math.random(),  
					        type : "POST",  
					        dataType : "json", 
					        data:{account:str},
					        success : function(data) {  
					        	var user = data.user;
					        	if(user && user.userName){//用户存在，则设置用户名称
					        		$("#userName").val(user.userName);
					        		$("#userName").focus();
					        		$("#userName").blur();
					        		$("#userName").attr("disabled","disabled");
					        		if(len==2){
						        		$("#passwdSetDiv").html($passwdOldDiv);
						        		vali.addRule();
					        		}
					        	}else{
					        		$("#userName").removeAttr("disabled");
					        		if(len==1){
						        		$("#passwdSetDiv").html($passwdNewDiv);
						        		vali.addRule();
					        		}
					        	}
					        }  
					    });
						return true;
					}else{
						return false;
					}
				}
			},
			callback:function (form){
				registOrg();
				return false;
			},
			showAllError : true
		});
	})
	function submitForm(){
		$("#createForm").submit();
	}
	//注册
	function registOrg(){
		if($("#subState").val()==1){
			return false;
		}
		var result;
		 //异步提交表单
	    $("#createForm").ajaxSubmit({
	        type:"post",
	        url:"/registe/registeOrg/${requestURI}?t="+Math.random(),
	        dataType: "json",
	        async:false,
	        beforeSubmit:function (a,f,o){
	        	$("#subState").val(1)
			}, 
	        success:function(data){
	        	if(data.status=='f1'){
	        		showNotification(2,"密码输入错误!");
	        	}else if(data.status=='f2'){
	        		showNotification(2,"随机码过期!");
	        	}else{
	        		var account = data.account;
	        		var joinTempId = data.joinTempId;
	        		if(data.isSysUser){
		        		window.location.replace("/registe/registeWayPage?account="+encodeURIComponent(account)+"&joinTempId="+joinTempId);
	        		}else{
	        			window.location.replace("/registe/registeCheckPage?account="+encodeURIComponent(account)+"&joinTempId="+joinTempId);
	        		}
	        	}
	        }
	    });
	    $("#subState").val(0);
	}
	//重新填充数据
	function fillDiv(){
		var account = $("#account").val();
		if(!strIsNull(account)){
			$("#account").focus();
			$("#account").blur();
		}
		//$("#account").focus();
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
		color: #000;
		background: url(/static/images/web/regist-step-2-0.png) no-repeat;
	}
	.step3{
		color: #000;
		background: url(/static/images/web/regist-step-3-0.png) no-repeat;
	}
</style>
</head>
<body onload="fillDiv()">
	<!----头部开始---->
	<div class="top">
		<jsp:include page="/WEB-INF/jsp/web/head.jsp"></jsp:include>
	</div>
	<!--注册 S-->
    <div class="register-box">
    	<div class="flow-box">
    		<ul class="flow-menu">
    			<li class="stepLi step1">1、账号注册 </li>
    			<li class="stepLi step2">2、账号验证</li>
    			<li class="stepLi step3">3、创建或加入团队</li>
    		</ul>
    	</div>
    	
    	<%--账号验证 --%>
    	<div class="register-form" style="display: block">
    		<input type="hidden" id="subState" value="0">
    		<form id="createForm" class="subform" method="post">
			<input type="hidden" name="redirectPage"/>
	    		<div class="form-group" style="height: 42px">
	    			<label>注册账号:<span style="color: red;padding:0px 5px">*</span></label>
	    			<div style="float:left;">
		    			<input class="input-form" id="account" name="account" value="" placeholder="请输入邮箱或手机号"
		    			 datatype="account"  />
	    			</div>
	    		</div>
	    		<div class="form-group" style="height: 42px">
	    			<label>用户姓名:<font style="color: red;padding:0px 5px">*</font></label>
	    			<div style="float:left;">
		    			<input class="input-form" id="userName"  
		    			datatype="*"  name="userName" value="" placeholder="请输入姓名"/>
	    			</div>
	    		</div>
	    		 <div id="passwdSetDiv">
	    		 	<div class="form-group" style="height: 42px">
			   			<label>设置密码:<font style="color: red;padding:0px 5px">*</font></label>
			   			<div class="pull-left">
			    			<input class="input-form" datatype="s6-18" id="passwd" type="password" name="password" value=""  />
			   			</div>
			   		</div>
			  			<div class="form-group" style="height: 42px">
			   			<label>再次输入:<font style="color: red;padding:0px 5px">*</font></label>
			   			<div class="pull-left">
			    			<input class="input-form" datatype="*" type="password" recheck="password" name="password2" value="" />
			   			</div>
			   		</div>
	    		 </div>
	    		<div class="form-group" style="height: 42px">
	    			<label>随机码:<font style="color: red;padding:0px 5px">*</font></label>
	    			<div style="float:left;">
		    			<input class="input-form" type="text" style="width:250px;" 
		    			id="yzm" name="yzm" datatype="*" ajaxurl="/registe/checkYzm/${requestURI}"/>
		    			&nbsp;&nbsp;
		    			<img class="yzm" src="/registe/AuthImage/${requestURI}" id="yz" 
		    			onclick="this.src='/registe/AuthImage/${requestURI}?rnd=' + Math.random();$('#yzm').val('')"/>
	    			</div>
	    		</div>
	    		<a href="javascript:void(0)" class="next-btn" onclick="submitForm();">提交</a>
    		</form>
    		
    	</div>
    </div>
    
    <div id="passwdNewDiv" style="display: none">
   		<div class="form-group" style="height: 42px">
   			<label>设置密码:<font style="color: red;padding:0px 5px">*</font></label>
   			<div class="pull-left">
    			<input class="input-form" datatype="s6-18" id="passwd" type="password" name="password" value="" />
   			</div>
   		</div>
  			<div class="form-group" style="height: 42px">
   			<label>再次输入:<font style="color: red;padding:0px 5px">*</font></label>
   			<div class="pull-left">
    			<input class="input-form" datatype="*" type="password" recheck="password" name="password2" value="" />
   			</div>
   		</div>
  	</div>
    <div id="passwdOldDiv" style="display: none">
   		<div class="form-group" style="height: 42px">
   			<label>确认密码:<font style="color: red;padding:0px 5px">*</font></label>
   			<div class="pull-left">
    			<input class="input-form" datatype="s6-18,passwd" id="passwd" type="password" name="password" value="" />
   			</div>
   		</div>
  	</div>
	
	<!--登录框 E-->
	<div class="bottom">
		<jsp:include page="/WEB-INF/jsp/web/bottom.jsp"></jsp:include>
	</div>
</body>

</html>
