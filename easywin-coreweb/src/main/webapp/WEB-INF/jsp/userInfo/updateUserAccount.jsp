<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
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
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">
<script type="text/javascript" src="/static/js/cookieInfo.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	$(function() {
		var valid = $(".subform")
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
										var account = "";
										if ("phone" == "${noticeType}") {
											account = $("#newPhone").val();
											$("#newPhone").attr(
													"ajaxUrl","/userInfo/checkAccount?sid=${param.sid}&noticeType=${noticeType}")
										} else {
											account = $("#newEmail").val();
											$("#newEmail").attr(
													"ajaxUrl","/userInfo/checkAccount?sid=${param.sid}&noticeType=${noticeType}")
										}
										
										//邮箱验证
										var regE = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
										//数字验证
										var regNum = /^\d+$/
										var regTel = /^13[0-9]{9}$|14[0-9]{9}$|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/
										
										if ("phone" == "${noticeType}") {
											if (str.length != 11
													|| !regTel.test(str)) {//不是手机号
												return "请输入正确的手机号";
											}else{
												$("#newAccounYzm").attr(
														"ajaxUrl",
														"/userInfo/checkYzmByNewAccount/${requestURI}?sid=${param.sid}&account="
																+ account);
												return true;
											}
										}else{
											
											if (!regE.test(str)) {//不是邮箱
												return "请输入正确的邮箱格式";	
											}else{
												$("#newAccounYzm").attr(
														"ajaxUrl",
														"/userInfo/checkYzmByNewAccount/${requestURI}?sid=${param.sid}&account="
																+ account);
												return true;
											}
										}
									} else {
										//return "请填写已注册的帐号邮箱或手机号码";
										return false;
									}
								}
							},
							showAllError : true
						});
		$("#btn_updateAccount").click(
				function() {
					$("#newAccountForm input[name='redirectPage']").val(
							window.location.href);
					$("#newAccountForm").submit();
				});
		$("#btn_nextStep").click(
				function() {
					$("#stepform input[name='redirectPage']").val(
							window.location.href);
					$("#stepform").submit();
				});
	});
	$(document).ready(function() {
		$(document).keydown(function(e) {
			if (e.keyCode == 13) {
				return false;
			}
		});
		
		var noticeType = '${noticeType}'
		var title ="";
		if("phone"==noticeType){
			title ="手机验证绑定";
		}else if("email"==noticeType){
			title ="邮箱验证绑定";
		}
		$(".ps-layerTitle").html(title);
		
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
	});
	//设置访问成功后的跳转地址
	function setLocation() {
		$("#redirectPage").val(window.self.location);
	}
	var timeId;
	var time = 30;
	function clock() {
		time--;
		if (time == 0) {
			$("#sendMail").attr("onclick", "sendPassYzm()");
			$("#sendMail").attr("class", "sendBtn");
			$("#sendMail").html('重新获取验证码');
			clearInterval(timeId);
		} else {
			$("#sendMail").attr("class", "resendBtn");
			$("#sendMail").html(time + "秒后重新发送");
		}
	}
	//发送验证码
	function sendPassYzm() {
		$
				.ajax({
					type : "post",
					url : "/userInfo/sendPassYzm?sid=${param.sid}&rnd="
							+ Math.random(),
					dataType : "json",
					data : {
						noticeType : "${noticeType}"
					},
					beforeSend : function(XMLHttpRequest) {
						time = 29;
						$("#sendMail").attr("class", "resendBtn");
						$("#sendMail").html('发送中..');
						$("#sendMail").removeAttr("onclick")
					},
					success : function(data) {
						if ('y' == data.status) {
							window.top.layer.alert("发送成功");
							timeId = setInterval("clock()", 1000);
						} else {
							$("#sendMail").attr("onclick", "sendPassYzm()");
							$("#sendMail").attr("class", "sendBtn");
							$("#sendMail").html('获取验证码');
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						window.top.layer.alert("发送失败");
						$("#sendMail").attr("onclick", "sendPassYzm()");
						$("#sendMail").attr("class", "sendBtn");
						$("#sendMail").html('获取验证码');
					}
				});
	}

	function newClock() {
		time--;
		if (time == 0) {
			$("#newSendMail").attr("onclick", "sendPassYzmByNewAccount()");
			$("#newSendMail").attr("class", "sendBtn");
			$("#newSendMail").html('重新获取验证码');
			clearInterval(timeId);
		} else {
			$("#newSendMail").attr("class", "resendBtn");
			$("#newSendMail").html(time + "秒后重新发送");
		}
	}
	//通过新账户发送验证码
	function sendPassYzmByNewAccount() {
		
		//邮箱验证
		var regE = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
		//数字验证
		var regNum = /^\d+$/
		var regTel = /^13[0-9]{9}$|14[0-9]{9}$|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/
		var account = "";
		if ("phone" == "${noticeType}") {
			account = $("#newPhone").val();
			if (account.length != 11
					|| !regTel.test(account)) {//不是手机号
				return false;
			}
		} else {
			account = $("#newEmail").val();
			if (!regE.test(account)) {//不是邮箱
				return false;	
			}
		}
		$.ajax({
			type : "post",
			url : "/userInfo/sendPassYzmByNewAccount?sid=${param.sid}&rnd="
					+ Math.random(),
			dataType : "json",
			data : {
				noticeType : "${noticeType}",
				account : account
			},
			beforeSend : function(XMLHttpRequest) {
				time = 29;
				$("#newSendMail").attr("class", "resendBtn");
				$("#newSendMail").html('发送中..');
				$("#newSendMail").removeAttr("onclick")
			},
			success : function(data) {
				if ('y' == data.status) {
					window.top.layer.alert("发送成功");
					timeId = setInterval("newClock()", 1000);
				} else {
					$("#newSendMail").attr("onclick",
							"sendPassYzmByNewAccount()");
					$("#newSendMail").attr("class", "sendBtn");
					$("#newSendMail").html('获取验证码');
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				window.top.layer.alert("发送失败");
				time = 0;
				$("#newSendMail").attr("onclick", "sendPassYzmByNewAccount()");
				$("#newSendMail").attr("class", "sendBtn");
				$("#newSendMail").html('获取验证码');
			}
		});
	}
</script>
<style type="text/css">
.sendBtn {
	display: inline-block;
	background: #D0EEFF;
	border: 1px solid #99D3F5;
	border-radius: 4px;
	padding: 4px 12px;
	overflow: hidden;
	color: #1E88C7;
	text-decoration: none;
	text-indent: 0;
	line-height: 20px;
	min-width: 130px;
	text-align: center;
	cursor: pointer;
}

.resendBtn {
	display: inline-block;
	background: #F5F5F5;
	border: 1px solid #DDD;
	border-radius: 4px;
	padding: 4px 12px;
	overflow: hidden;
	color: #ACA899;
	text-decoration: none;
	text-indent: 0;
	line-height: 20px;
	min-width: 130px;
	text-align: center;
	cursor: auto;
}
/**定义一个单选按钮显示样式*/
.inputradio {
	width: 16px !important;
	height: 16px !important;
	float: left !important;
	margin-top: 8px !important;
	margin-left: 5px !important;
}

.radiotext {
	float: left !important;
	margin-top: 8px !important;
	font-size: 12px !important;
}
</style>
</head>
<body>

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle"></span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
				
				
					<div id="about" class="tab-pane">
						<div id="sendDiv" 
							style="display: ${(empty showUp && ((noticeType eq 'email' && not empty sessionUser.email ) || 
												(noticeType eq 'phone' && not empty sessionUser.movePhone)))?'block':'none'}">
							<form class="subform" id="stepform"
								action="/userInfo/updateUserAccountNextStepPass?sid=${param.sid}"
								method="post">
								<input type="hidden" name="requestURI" value="${requestURI}" /> <input
									type="hidden" id="redirectPage" name="redirectPage" /> <input
									type="hidden" name="noticeType" id="noticeType"
									value="${noticeType}">
									
									<div>
										<div class="form-group" style="height: 42px">
											<c:choose>
												<c:when test="${noticeType eq 'phone'}">
									    			<label>手机号码：</label>
									    			<div style="float:left;">
										    			 <div class="input-form colorpicker-default" readonly="readonly" style="width:300px;">${sessionUser.movePhone}</div>
									    			</div>
												</c:when>
												<c:when test="${noticeType eq 'email'}">
									    			<label>接收邮箱：</label>
									    			<div style="float:left;">
										    			 <div class="input-form colorpicker-default" readonly="readonly" style="width:300px;">${sessionUser.email}</div>
									    			</div>
												</c:when>
											</c:choose>
							    		</div>
							    		
							    		<div class="form-group" style="height: 42px">
							    			<label>验证码：</label>
							    			<div style="float:left;">
								    			<input type="text" id="yzm" name="yzm"
												class="colorpicker-default form-control" datatype="*"
												ajaxurl="/userInfo/checkYzm/${requestURI}?sid=${param.sid}&noticeType=${noticeType}"
												style="float:left;width:160px;" />
												<div class="clearfix"></div>
							    			</div>
							    			<button type="button" id="sendMail" class="sendBtn"
											onclick="sendPassYzm()" style="margin-left:10px;margin-top:2px;">获取验证码</button>
							    		</div>
							    	</div>
								<div class="panel-body">
									<button class="btn btn-info ws-btnBlue2" id="btn_nextStep"
										type="button" style="margin-left:200px;">下一步</button>
								</div>
							</form>
						</div>
				
						<div id="updateDiv" style="display: ${(not empty showUp || ((noticeType eq 'email' && empty sessionUser.email ) || 
												(noticeType eq 'phone' && empty sessionUser.movePhone)))?'block':'none'}">
							<form class="subform" id="newAccountForm"
								action="/userInfo/updateUserAccount?sid=${param.sid}" method="post">
								<input type="hidden" name="id" value="${sessionUser.id }" /> <input
									type="hidden" name="requestURI" value="${requestURI}" /> <input
									type="hidden" id="redirectPage" name="redirectPage" /> <input
									type="hidden" name="noticeType" id="noticeType"
									value="${noticeType}">
									<div>
										<div class="form-group" style="height: 42px">
											<c:choose>
												<c:when test="${noticeType eq 'phone'}">
									    			<label>新手机号码：</label>
									    			<div style="float:left;">
										    			 <input class="form-control view-input" type="text" id="newPhone"
															name="account" datatype="account" placeholder="请输入手机号"
															nullmsg="请填写正确的手机号码" style="width:300px;" 
															/>
									    			</div>
												</c:when>
												<c:when test="${noticeType eq 'email'}">
									    			<label>新邮箱地址：</label>
									    			<div style="float:left;">
										    			 <input class="form-control view-input" type="text" id="newEmail"
															name="account" datatype="account" placeholder="请输入邮箱"
															nullmsg="请填写正确的邮箱地址" style="width:300px;" 
															/>
									    			</div>
												</c:when>
											</c:choose>
										</div>
										<div class="form-group" style="height: 42px">
							    			<label>验证码：</label>
							    			<div style="float:left;">
								    			<input type="text" id="newAccounYzm" name="yzm"
												class="colorpicker-default form-control" datatype="*"
												style="float:left;width:160px;" />
											<div style="clear: both"></div>
							    			</div>
							    			<button type="button" id="newSendMail" class="sendBtn"
											onclick="sendPassYzmByNewAccount()"
											style="margin-left:10px;margin-top:2px;">获取验证码</button>
							    		</div>
							    		
									</div>
								<div class="panel-body">
									<button class="btn btn-info ws-btnBlue2" id="btn_updateAccount"
										type="button" style="margin-left:200px;">更新</button>
								</div>
							</form>
						</div>
					</div>
				
				
				
				
				
				</div>
			</div>
		</div>
	</div>
</div>

	

	<%--用与测量当前页面的高度 --%>
	<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>

