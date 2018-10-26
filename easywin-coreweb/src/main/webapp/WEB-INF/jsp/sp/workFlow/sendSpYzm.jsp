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
	var valid;
	$(function() {
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
										var account = $("#account").val();
										$("#spYzm").attr(
												"ajaxUrl",
												"/userInfo/checkYzmByNewAccount/${requestURI}?sid=${param.sid}&account="
														+ account);
										return true;
									} else {
										//return "请填写已注册的帐号邮箱或手机号码";
										return false;
									}
								}
							},
							showAllError : true
						});
		
	});
	$(document).ready(function() {
		$(document).keydown(function(e) {
			if (e.keyCode == 13) {
				return false;
			}
		});
		
		var title ="审批权限验证";
		$(".ps-layerTitle").html(title);
		
		//设置滚动条高度
		var height = $(window).height()-85;
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
						noticeType : "phone"
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
							$("#sendMail").html('获取密码');
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
	//验证审批验证码
	function checkSpYzm(){
		
		var spYzm = $("#spYzm").val();
		if(!spYzm){
			$("#spYzm").focus();
			$("#spYzm").blur();
			return false;
		}
		var result;
		var account = $("#account").val();
		$.ajax({
			type : "post",
			url : "/userInfo/checkYzmByNewAccount/${requestURI}?sid=${param.sid}&rnd="
					+ Math.random(),
			dataType : "json",
			 async:false,
			data : {
				account : account,
				param:spYzm
			},
			success : function(data) {
				if ('y' == data.status) {
					result={"status":"y","spYzm":spYzm};
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
		return result;
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
				
						<div id="updateDiv">
								<input type="hidden" id="subState" value="0"/> 
							<form class="subform" id="newAccountForm" method="post">
								<input type="hidden" name="account" id="account" value="${userInfo.movePhone}"/> 
								<input type="hidden" name="sid" value="${param.sid}">
									<div>
										<div class="form-group" style="height: 42px">
							    			<label>接收手机号：</label>
							    			<div style="float:left;">
								    			 <div class="form-control view-input" style="width:300px;border: 0;box-shadow:none;padding-top:2px"/>${userInfo.movePhone }</div>
							    			</div>
										</div>
										<div class="form-group" style="height: 42px">
							    			<label>验证码：</label>
							    			<div style="float:left;">
								    			<input type="text" id="spYzm" name="yzm"
												class="colorpicker-default form-control" datatype="*,account"
												style="float:left;width:160px;" nullmsg="请输入验证码。"/>
											<div style="clear: both"></div>
							    			</div>
							    			<button type="button" id="sendMail" class="sendBtn"
											onclick="sendPassYzm()"
											style="margin-left:10px;margin-top:2px;">获取验证码</button>
							    		</div>
							    		
									</div>
							</form>
						</div>
				
				</div>
				
				<div class="align-center clearfix" style="padding:6px 10px;">
				 	<div  class="pull-right" style="margin-left: 15px;" id="cancleDiv">
						<button type="button" class="btn btn-default" style="background-color: #ccc;" id="cancleBtn">取消</button>
				 	</div>
				 	<div class="pull-right">
						<button type="button" class="btn btn-info ws-btnBlue" id="checkBtn">确定</button>
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

