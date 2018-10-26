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
										var account = $("#newPhone").val();
										$("#newPhone").attr(
												"ajaxUrl","/userInfo/checkAccount?sid=${param.sid}&noticeType=phone")
									
										//数字验证
										var regNum = /^\d+$/
										var regTel = /^13[0-9]{9}$|14[0-9]{9}$|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/
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
		
		var title ="完善手机账号";
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
		var account = $("#newPhone").val();
		//数字验证
		var regNum = /^\d+$/
		var regTel = /^13[0-9]{9}$|14[0-9]{9}$|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/
		if (account.length != 11
				|| !regTel.test(account)) {//不是手机号
			return false;
		}
		alert(account)
		$.ajax({
			type : "post",
			url : "/userInfo/sendPassYzmByNewAccount?sid=${param.sid}&rnd="
					+ Math.random(),
			dataType : "json",
			data : {
				noticeType : "phone",
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
	
	function updateUserMovePhone(){
		
		if($("#subState").val()==1){
			return false;
		}
		var newPhone = $("#newPhone").val();
		if(!newPhone){
			$("#newPhone").focus();
			$("#newAccounYzm").focus();
			
			$("#newPhone").blur();
			$("#newAccounYzm").blur();
			
			return false;
		}
		var newAccounYzm = $("#newAccounYzm").val();
		if(!newAccounYzm){
			$("#newPhone").focus();
			$("#newAccounYzm").focus();
			
			$("#newPhone").blur();
			$("#newAccounYzm").blur();
			return false;
		}
		
		var result;
		 //异步提交表单
	    $("#newAccountForm").ajaxSubmit({
	        type:"post",
	        url:"/userInfo/updateUserMovePhone?t="+Math.random(),
	        dataType: "json",
	        async:false,
	        beforeSubmit:function (a,f,o){
	        	$("#subState").val(1)
			}, 
			traditional :true,
	        success:function(data){
		         var status = data.status;
		         if(status=='y'){
		        	var pageSource = '${param.pageSource}';
	        		result={"status":"y","pageSource":pageSource};
			     }
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	window.top.layer.alert("系统错误，请联系管理人员",{icon:7,title:false,closeBtn:0,btn:["关闭"]})
	        	return false;
	        	
	        }
	    });
       	 $("#subState").val(0)
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
								<input type="hidden" name="id" value="${sessionUser.id}"/> 
								<input type="hidden" name="sid" value="${param.sid}">
									<div>
										<div class="form-group" style="height: 42px">
							    			<label>新手机号码：</label>
							    			<div style="float:left;">
								    			 <input class="form-control view-input" type="text" id="newPhone"
													name="account" datatype="account" placeholder="请输入手机号"
													nullmsg="请填写正确的手机号码" style="width:300px;" 
													/>
							    			</div>
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
							</form>
						</div>
				
				</div>
				
				<div class="align-center clearfix" style="padding:6px 10px;">
				 	<div  class="pull-right" style="margin-left: 15px;" id="cancleDiv">
						<button type="button" class="btn btn-default" style="background-color: #ccc;" id="cancleBtn">取消</button>
				 	</div>
				 	<div class="pull-right">
						<button type="button" class="btn btn-info ws-btnBlue" id="addBtn">设定</button>
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

