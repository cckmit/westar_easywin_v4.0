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
	<script type="text/javascript" src="/static/js/update.password.md5.js"></script>
	<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">
	<script type="text/javascript" src="/static/js/cookieInfo.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	<script type="text/javascript">
        $(function() {
            $(".subform").Validform({
                tiptype : function(msg, o, cssctl) {
                    validMsgV2(msg, o, cssctl);
                    if (o.type == 3) {
                        setTimeout(function() {
                            resizeVoteH('userCenter')
                        }, 110);
                    }
                },
                //callback:function(form){
                //updatePassword();
                //return false;
                //},
                showAllError : true
            });
            $("#btn_updatePass").click(
                function() {
                    $("#passform input[name='redirectPage']").val(
                        window.location.href);

                    /*因为validform似乎不能去掉name属性，
                    导致无论如何都会向后台发送明文密码，
                    所以在这里密码改为相同长度的‘x’组合以遮掩明文，
                    实际加密密码由passwordMD5传输*/
                    var password = $("input[name='password']").val();
                    var systemPassword = "";
                    var systemPassword2 = "";
                    for(var i=0;i<password.length;i++){
                        systemPassword = systemPassword + "x";
                        systemPassword2 = systemPassword2 + "x";
                    }
                    $("#passwordMD5").val($.md5(password));
                    $("input[name='password']").val(systemPassword);
                    $("input[name='password2']").val(systemPassword2);

                    $("#passform").submit();
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
            //设置滚动条高度
            var height = $(window).height()-40;
            $("#contentBody").css("height",height+"px");

            //empty sessionUser.movePhone
            if(!${empty sessionUser.movePhone} ){
                $("#email").css("display", "none");
            }
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
            /*
            var regE = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
            //接收的邮箱
            var email = '${sessionUser.email}';
		if(!regE.test(email)){//匹配就添加到成功中
			window.top.layer.alert("你的邮箱格式不正确！");
		}else{

		} */
            $
                .ajax({
                    type : "post",
                    url : "/userInfo/sendPassYzm?sid=${param.sid}&rnd="
                    + Math.random(),
                    dataType : "json",
                    data : {
                        noticeType : $("[name='noticeType']:checked").val()
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
			width: 130px;
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
			width: 130px;
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
	<script type="text/javascript">
        $(function() {
            $("[name='noticeType']").click(function() {
                if ($("[name='noticeType']:checked").val() == "phone") {
                    $("#phone").css("display", "block");
                    $("#email").css("display", "none");
                } else if ($("[name='noticeType']:checked").val() == "email") {
                    $("#phone").css("display", "none");
                    $("#email").css("display", "block");
                }
                //改变验证码的验证参数
                $("#yzm").attr("ajaxurl","/userInfo/checkYzm/${requestURI}?sid=${param.sid}&noticeType="+$("[name='noticeType']:checked").val());
            });
        })
	</script>
</head>
<body>

<div class="container no-padding" style="width: 100%">
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
				<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
					<span class="widget-caption themeprimary ps-layerTitle">密码修改</span>
					<div class="widget-buttons">
						<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
				</div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">

					<div id="about" class="tab-pane padding-top-20">
						<div id="sendDiv" style="display: ${empty showUp?'block':'none'}">
							<form class="subform" id="stepform"
								  action="/userInfo/nextStepPass?sid=${param.sid}" method="post">
								<input type="hidden" name="requestURI" value="${requestURI}" /> <input
									type="hidden" id="redirectPage" name="redirectPage" />

								<div>
									<div class="form-group" style="height: 42px">
										<c:choose>
											<c:when test="${not empty sessionUser.movePhone}">
												<label>获取方式：</label>
												<div class="pull-left">
													<c:if test="${not empty sessionUser.movePhone}">
														<label class=" no-margin-bottom">
															<input type="radio" value="phone" ${not empty sessionUser.movePhone?'checked=\"checked\"':''} name="noticeType" class="ws-radio">
															手机<span class="text"></span>
														</label>
													</c:if>
													<c:if test="${not empty sessionUser.email}">
														<label class=" no-margin-bottom">
															<input type="radio" value="email" ${empty sessionUser.movePhone?'checked=\"checked\"':''} class="ws-radio" name="noticeType">
															邮件<span class="text"></span>
														</label>
													</c:if>
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
								</div>

								<c:if test="${not empty sessionUser.movePhone}">
									<div class="form-group" style="height: 42px" id="phone">
										<label>手机号码：</label>
										<div style="float:left;">
											<div class="input-form colorpicker-default" readonly="readonly" style="width:300px;">${sessionUser.movePhone}</div>
										</div>
									</div>
								</c:if>
								<c:if test="${not empty sessionUser.email}">
									<div class="form-group" style="height: 42px" id="email">
										<label>接收邮箱：</label>
										<div style="float:left;">
											<div class="input-form colorpicker-default" readonly="readonly" style="width:300px;">${sessionUser.email}</div>
										</div>
									</div>
								</c:if>

								<div class="form-group" style="height: 42px">
									<label>验证码：</label>
									<div style="float:left;">
										<input type="text" id="yzm" name="yzm"
											   class="colorpicker-default form-control" datatype="*"
											   ajaxurl="/userInfo/checkYzm/${requestURI}?sid=${param.sid}&noticeType=${empty sessionUser.movePhone?'email':'phone'}"
											   style="float:left;width:120px;" />
										<div style="clear: both"></div>
									</div>
									<button type="button" id="sendMail" class="sendBtn"
											onclick="sendPassYzm()" style="margin-left:10px;margin-top:2px;">获取验证码</button>
								</div>

								<div class="panel-body">
									<button class="btn btn-info ws-btnBlue2" id="btn_nextStep"
											type="button" style="margin-left:200px;">下一步</button>
								</div>
							</form>
						</div>

						<div id="updateDiv" style="display: ${empty showUp?'none':'block'}">
							<form class="subform" id="passform"
								  action="/userInfo/updatePassword?sid=${param.sid}" method="post">
								<input type="hidden" name="id" value="${sessionUser.id }" /> <input
									type="hidden" name="requestURI" value="${requestURI}" /> <input
									type="hidden" id="redirectPage" name="redirectPage" />
								<input type="hidden" id="passwordMD5" name="passwordMD5" />

								<div>
									<div class="form-group" style="height: 42px">
										<label>新密码：</label>
										<div style="float:left;">
											<input class="colorpicker-default form-control pull-left"
												   nullmsg="请输入新密码" datatype="s6-18" type="password"
												   name="password" value="" style="width:300px;"> <small
												class="pull-left ws-prompt ws-color">(6~18位密码)</small>
											<div style="clear: both"></div>
										</div>
									</div>

									<div class="form-group" style="height: 42px">
										<label>再次输入：</label>
										<div style="float:left;">
											<input class="colorpicker-default form-control" nullmsg="请再次输入新密码"
												   datatype="*" type="password" recheck="password" name="password2"
												   value="" style="width:300px;">
											<div style="clear: both"></div>
										</div>
									</div>
								</div>


								<div class="panel-body">
									<button class="btn btn-info ws-btnBlue2" id="btn_updatePass"
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
</body>
</html>

