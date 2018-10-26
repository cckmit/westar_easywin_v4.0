<%@page import="com.westar.core.web.InitServlet"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<%
	pageContext.setAttribute("requestURI", request.getRequestURI()
			.replace("/", "_").replace(".", "_"));
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>

<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="/static/plugins/Validform/css/validform.css">
<script type="text/javascript"
	src="/static/plugins/Validform/js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript"
	src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>

<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script src="/static/plugins/layer/layer.js" type="text/javascript"
	charset="utf-8"></script>

<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>

<script type="text/javascript">
	var index = 0;
	var valid;
	$(function() {
		//$(".login-btn").remove();
		//$(".zc-btn").remove();
		valid = $(".subform")
				.Validform(
						{
							tiptype : function(msg, o, cssctl) {
								validMsg(msg, o, cssctl);
								if (o.type == 3) {
									index = 0;
								}
							},
							datatype : {
								"account" : function(gets, obj, curform, regxp) {
									var str = $(obj).val();
									if (!strIsNull(str)) {
										//邮箱验证
										var regE = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
										//数字验证
										var regNum = /^\d+$/
										var regTel = /^13[0-9]{9}$|14[0-9]{9}$|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/
										if (str.indexOf("@") > 0) {//判断包含特殊字符
											if (!regE.test(str)) {//不是邮箱
												return "请输入正确的邮箱格式";
											} else {
												return true;
											}
										} else if (regNum.test(str)) {//不包含特殊字符，有数字组成
											if (str.length != 11
													|| !regTel.test(str)) {//不是手机号
												return "请输入正确的手机号";
											} else {
												return true;
											}
										} else {
											return true;
										}
									} else {
										//return "请填写已注册的帐号邮箱或手机号码";
										return false;
									}
								}
							},
							callback : function(form) {
								if (index == 1) {
									zhyz();//账号验证
								} else if (index == 2) {
									aqyz();//安全验证
								} else if (index == 3) {
									xgmm();//密码修改
								}
								return false;
							},
							showAllError : true
						});
	})
	var timeId;
	var time = 30;
	function clock() {
		time--;
		if (time == 0) {
			$("#getPassYzm").attr("onclick", "getPassYzm()");
			$("#getPassYzm").attr("class", "sendBtn yzm");
			$("#getPassYzm").html("重新获取");
			clearInterval(timeId);
		} else if (time > 0) {
			$("#getPassYzm").attr("class", "resendBtn yzm");
			$("#getPassYzm").html("重新获取(" + time + ")");
		}
	}
	
	//发送验证码
	function getPassYzm() {
		//接收帐号
		var account = $("#aqyzForm").find("#account").val();
		if (!strIsNull(account)) {
			//邮箱验证
			var regE = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
			//数字验证
			var regNum = /^\d+$/
			var regTel = /^13[0-9]{9}$|14[0-9]{9}$|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/
			if (account.indexOf("@") > 0 && !regE.test(account)) {//判断包含特殊字符
				layer.alert("帐号不是有效的邮箱地址，无法获取验证码。");
				return false;
			} else if (regNum.test(account) && (account.length != 11 || !regTel.test(account))) {//不包含特殊字符，有数字组成
				layer.alert("帐号不是有效的手机号码，无法获取验证码。");
				return false;
			} else {
				getPassYzmByAjax(account);//获取权限验证码
			}
		}else{
			layer.alert("帐号为空，无法获取验证码。");
			return false;
		}
	}
	
	//获取权限验证码
	function getPassYzmByAjax(account){
		$.ajax({
			type : "post",
			url : "/registe/sendPassYzmAccount?rnd=" + Math.random(),
			dataType : "json",
			data : {account : account},
			beforeSend : function(XMLHttpRequest) {
				time = 29;
				$("#getPassYzm").attr("class", "resendBtn yzm");
				$("#getPassYzm").html("发送中..");
				$("#getPassYzm").removeAttr("onclick")
			},
			success : function(data) {
				if ('y' == data.status) {
					layer.alert("发送成功", {
						icon : 6
					});
					timeId = setInterval("clock();", 1000);
				} else {
					$("#getPassYzm").attr("onclick", "getPassYzm()");
					$("#getPassYzm").attr("class", "sendBtn yzm");
					$("#getPassYzm").html("获取验证码");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				layer.alert("发送失败", {
					icon : 5
				});
				$("#getPassYzm").attr("onclick", "getPassYzm()");
				$("#getPassYzm").attr("class", "sendBtn");
				$("#getPassYzm").html("获取验证码");
			}
		});
	}
	
	function submitForm(num) {
		index = num;
		if (1 == index) {
			$("#zhyzForm").submit();
		} else if (2 == index) {
			$("#aqyzForm").submit();
		} else if (3 == index) {
			$("#xgmmForm").submit();
		}

	}
	
	//账号验证
	function zhyz() {
		var account = $("#zhyzForm").find("#account").val();
		$("#zhyzForm").ajaxSubmit(
				{
					type : "post",
					url : "/registe/checkAccount/${requestURI}?t="
							+ Math.random(),
					dataType : "json",
					beforeSubmit : function(a, f, o) {
						$("#zhyzBtn").attr("disabled", "disabled");
					},
					success : function(data) {
						index = 0;
						if (data.status == 'y') {
							$("#zhyz").css("display", "none");
							$("#aqyz").css("display", "block");
							$("#aqyzForm").find("#account").val(account);
							$("#jsAccount").val(account);
							$("#passYzm").attr("ajaxUrl",
									"/registe/checkPassYzm?account=" + account);
							valid.addRule();

							$("#step2").attr("src",
									"/static/images/web/mm-2-2.png")
						} else {
							showNotification(2, data.info)
						}
						$("#zhyzBtn").removeAttr("disabled");
					},
					error : function(XmlHttpRequest, textStatus, errorThrown) {
						//可以再次提交
						$("#zhyzBtn").removeAttr("disabled");
					}
				});
	}
	//安全验证
	function aqyz() {
		var account = $("#aqyzForm").find("#account").val();
		$("#aqyzForm").ajaxSubmit({
			type : "post",
			url : "/registe/sendAccountForAqyz?t=" + Math.random(),
			dataType : "json",
			beforeSubmit : function(a, f, o) {
				$("#aqyzBtn").attr("disabled", "disabled");
			},
			success : function(data) {
				index = 0;
				if (data.status == 'y') {
					$("#xgmmForm").find("#account").val(account);
					$("#zhyz").css("display", "none");
					$("#aqyz").css("display", "none");
					$("#mmxg").css("display", "block");

					$("#step3").attr("src", "/static/images/web/mm-3-2.png")
				} else {
					showNotification(1, data.info)
				}
				$("#aqyzBtn").removeAttr("disabled");
			},
			error : function(XmlHttpRequest, textStatus, errorThrown) {
				//可以再次提交
				$("#aqyzBtn").removeAttr("disabled");
			}
		});
	}
	//修改密码
	function xgmm() {
		var account = $("#xgmmForm").find("#account").val();
		$("#xgmmForm").ajaxSubmit({
			type : "post",
			url : "/registe/updatePassword?t=" + Math.random(),
			dataType : "json",
			beforeSubmit : function(a, f, o) {
				$("#mmxgBtn").attr("disabled", "disabled");
			},
			success : function(data) {
				index = 0;
				if (data.status == 'y') {
					$("#mmxg").css("display", "none");
					$("#upInfo").css("display", "block");

					$("#step4").attr("src", "/static/images/web/mm-4-2.png")
				} else {
					showNotification(2, data.info)
				}
				$("#mmxgBtn").removeAttr("disabled");
			},
			error : function(XmlHttpRequest, textStatus, errorThrown) {
				//可以再次提交
				$("#mmxgBtn").removeAttr("disabled");
			}
		});
	}
</script>
</head>
<body>
	<!----头部开始---->
	<div class="top">
		<jsp:include page="/WEB-INF/jsp/web/head.jsp"></jsp:include>
	</div>

	<!--注册 S-->
	<div class="register-box">
		<div class="flow-box">
			<ul class="flow-menu" style="padding-left: 55px">
				<li><a><img src="/static/images/web/mm-1.png" id="step1" /></a></li>
				<li><a><img src="/static/images/web/mm-2-1.png" id="step2" /></a></li>
				<li><a><img src="/static/images/web/mm-3-1.png" id="step3" /></a></li>
				<li><a><img src="/static/images/web/mm-4-1.png" id="step4" /></a></li>
			</ul>
		</div>

		<%--账号验证 --%>
		<div class="register-form" id="zhyz" style="display: block">
			<form id="zhyzForm" class="subform" method="post">
				<div class="form-group" style="height: 42px">
					<label>帐号：</label>
					<div style="float:left;">
						<input class="input-form" type="text" id="account" name="account"
							datatype="account" placeholder="请输入邮箱或手机号" nullmsg="请填写已注册的帐号邮箱或手机号码"
							ajaxurl='/registe/checkAccountForFindPass' />
					</div>
				</div>
				<div class="form-group" style="height: 42px">
					<label>验证码：</label>
					<div style="float:left;">
						<input class="input-form" type="text" style="width:250px;"
							id="yzm" name="yzm" datatype="*"
							ajaxurl="/registe/checkYzm/${requestURI}" /> &nbsp;&nbsp; <img
							class="yzm" src="/registe/AuthImage/${requestURI}" id="yz"
							onclick="this.src='/registe/AuthImage/${requestURI}?rnd=' + Math.random();$('#yzm').val('')" />
					</div>
				</div>
				<a href="javascript:void(0)" class="next-btn"
					onclick="submitForm(1);">下一步</a>
			</form>

		</div>
		<%--安全验证 --%>
		<div class="register-form" style="display: none" id="aqyz">
			<form id="aqyzForm" class="subform" method="get">
				<input type="hidden" id="jsAccount" />
				<div class="form-group" style="height: 42px">
					<label>账号：</label>
					<div style="float:left;" id="accountTd">
						<input class="input-form" type="text" id="account" name="account"
							readonly="readonly" style="color: gray!important;" value=""
							onfocus="this.blur()" />
					</div>
				</div>
				<div class="form-group" style="height: 42px">
					<label>权限验证码：</label>
					<div style="float:left;">
						<input class="input-form" type="text" style="width:250px;"
							id="passYzm" name="passYzm" datatype="*" /> &nbsp;&nbsp; <span
							class="sendBtn yzm" id="getPassYzm" onclick="getPassYzm()">
							获取验证码 </span>
					</div>
				</div>
				<a href="javascript:void(0)" class="next-btn"
					onclick="submitForm(2);">下一步</a>
			</form>
		</div>

		<%---密码修改 --%>
		<div class="register-form" style="display: none" id="mmxg">
			<form id="xgmmForm" class="subform" method="get">
				<input type="hidden" name="account" id="account" />
				<div class="form-group" style="height: 42px">
					<label>新密码：</label>
					<div style="float:left;">
						<input class="input-form" datatype="s6-18" id="password"
							type="password" name="password" value="" />
					</div>
				</div>
				<div class="form-group" style="height: 42px">
					<label>再次输入：</label>
					<div style="float:left;">
						<input class="input-form" datatype="s6-18" type="password"
							recheck="password" name="password2" value="" />
					</div>
				</div>
				<a href="javascript:void(0)" class="next-btn"
					onclick="submitForm(3);">修改</a>
			</form>
		</div>
		<%---密码修改 --%>
		<div class="register-form" style="display:none;" id="upInfo">
			<div class="browsertip" style="text-align: center;color:gray">
				<h1 style="font-size: 25px;">修改成功！</h1>
				<br />
				<div style="text-align: center;padding-top: 15px">
					<a href="/web/login" class="sendBtn" style="padding: 5px 15px">跳转到登录界面</a>
				</div>
			</div>
		</div>
	</div>
	<!--登录框 E-->
	<div class="bottom">
		<jsp:include page="/WEB-INF/jsp/web/bottom.jsp"></jsp:include>
	</div>



</body>
</html>
