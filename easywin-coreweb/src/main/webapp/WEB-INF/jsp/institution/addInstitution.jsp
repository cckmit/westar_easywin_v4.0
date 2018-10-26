<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title><%=SystemStrConstant.TITLE_NAME%></title>
<meta name="description" content="Dashboard" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	var EasyWin = {
		"sid" : "${param.sid}",
		"userInfo" : {
			"userId" : "${userInfo.id}",
			"userName" : "${userInfo.userName}",
			"comId" : "${userInfo.comId}",
			"orgName" : "${userInfo.orgName}",
			"isAdmin" : "${userInfo.admin}",
		},
		"homeFlag" : "${homeFlag}",
		"ewebMaxMin":"self",
		"ifreamName" : "${param.ifreamName}"
	};
</script>
<script type="text/javascript">
	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height() - 40;
		$("#contentBody").css("height", height + "px");

		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			datatype : {
				"input" : function(gets, obj, curform, regxp) {
					var str = $(obj).val();
					if (str) {
						var count = str.replace(/[^\x00-\xff]/g, "**").length;
						var len = $(obj).attr("defaultLen");
						if (count > len) {
							return "制度标题太长";
						} else {
							return true;
						}
					} else {
						return false;
					}
				},
				"instituType" : function(gets, obj, curform, regxp) {
					if ($("#instituType").val()) {
						return true;
					} else {
						return false;
					}
				},
			},
			beforeCheck : function(curform) {
				var instituRemark = document.getElementById("eWebEditor1").contentWindow.getHTML();
				if (!instituRemark) {
					layer.tips("请填写制度内容", $("#remark"), {
						tips : [ 1, '#FF3030' ]
					});
					return false;
				}
			},
			callback : function(form) {
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});

		$("#addInstitu").click(function() {
			//添加并查看
			$(".subform").submit();
		});
	});
	//双击移除
	function removeChoose(type, name, ts) {
		ts.remove();
		$(".subform").find("input[scope='" + type + "'][name='" + name + ".acceptComId']").remove();
		if (type == "dep") {
			$(".subform").find("input[scope='" + type + "'][name='" + name + ".depId']").remove();
			if ($("#depBox span").length < 1) {
				$("#depLi").hide();
			}
		} else if (type == "user") {
			$(".subform").find("input[scope='" + type + "'][name='" + name + ".userId']").remove();
			if ($("#userBox span").length < 1) {
				$("#userLi").hide();
			}
		} else {
			if ($("#orgBox span").length < 1) {
				$("#orgLi").hide();
			}
		}

	}
	var sid = '${param.sid}';
	$(function() {
		//部门选择
		$("#dep").click(function() {
			depMoreBack("scopeDep_select", null, sid, "yes", '', function(deps) {
				$("#depBox").html('');
				if (deps != null && deps.length > 0) {
					$("#depLi").show();
					for (var i = 0; i < deps.length; i++) {
						var depName = deps[i].text; //部门名称

						var depHtml = '\n <span  style="cursor:pointer;"';
						depHtml += '\n  title="双击移除" class="label label-default margin-right-5 margin-bottom-5"'
						depHtml += '\n  ondblclick="removeChoose(\'dep\',\'listScopeDep[' + i + ']\',this)">'
						depHtml += depName + '</span>';

						$("#depBox").append(depHtml);

						$('#scopeDep_select').append("<option selected='selected' value='" + deps[i].value + "'>" + deps[i].text + "</option>");
					}
				} else {
					$("#depLi").hide();
				}
			});
		});

		//人员选择
		$("#user").click(function() {
			userMore("scopeUser_select", null, sid, "yes", '', function(users) {
				//清除
				$("#userBox").html('');
				removeOptions("scopeUser_select");
				if (users != null && users.length > 0) {
					$("#userLi").show();

					for (var i = 0; i < users.length; i++) {
						var userName = users[i].text;
						var userHtml = '\n <span  style="cursor:pointer;"'
						userHtml += '\n  title="双击移除" class="label label-default margin-right-5 margin-bottom-5"'
						userHtml += '\n  ondblclick="removeChoose(\'user\',\'listScopeUser[' + i + ']\',this)">'
						userHtml += userName + '</span>'
						$("#userBox").append(userHtml);

						$('#scopeUser_select').append("<option selected='selected' value='" + users[i].value + "'>" + users[i].text + "</option>");
					}
				} else {
					$("#userLi").hide();
				}
			});
		});
	});
</script>
<style type="text/css">
.ps-listline {
	line-height: 30px !important
}
</style>
</head>
<body>
	<form class="subform" method="post" action="/institution/addInstitu">
		<tags:token></tags:token>
		<input type="hidden" name="attentionState" id="attentionState" value="0" />
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">制度发布</span>
							<div class="widget-buttons ps-toolsBtn">
								<a href="javascript:void(0)" class="blue" id="addInstitu">
									<i class="fa fa-save"></i>
									发布
								</a>
							</div>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
									<i class="fa fa-times themeprimary"></i>
								</a>
							</div>
						</div>
						<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
						<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
							<div class="widget radius-bordered">
								<div class="widget-header bg-bluegray no-shadow">
									<span class="widget-caption blue">基础配置</span>
									<div class="widget-buttons btn-div-full">
										<a class="ps-point btn-a-full" data-toggle="collapse">
											<i class="fa fa-minus blue"></i>
										</a>
									</div>
								</div>
								<div class="widget-body no-shadow">
									<div class="tickets-container bg-white">
										<ul class="tickets-list">
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-bookmark blue"></i>
													&nbsp;标题 <span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<input id="instituTitle" datatype="input,sn" defaultLen="52" name="title" nullmsg="请填写制度标题" class="colorpicker-default form-control" type="text" value="" onpropertychange="handleName()"
															onkeyup="handleName()" style="width: 400px;float: left">
														<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
													</div>
													<div class="pull-left">
														<span id="addWarn" style="float:left;margin-left:2px;"></span>
													</div>
												</div>
												<script>
													//当状态改变的时候执行的函数 
													function handleName() {
														var value = $('#instituTitle').val();
														var len = charLength(value);
														if (len % 2 == 1) {
															len = (len + 1) / 2;
														} else {
															len = len / 2;
														}
														if (len > 26) {
															$('#msgTitle').html("(<font color='red'>" + len + "</font>/26)");
														} else {
															$('#msgTitle').html("(" + len + "/26)");
														}
													}
													//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
													if (/msie/i.test(navigator.userAgent)) { //ie浏览器 
														document.getElementById('instituTitle').onpropertychange = handleName
													} else { //非ie浏览器，比如Firefox 
														document.getElementById('instituTitle').addEventListener("input", handleName, false);
													}
												</script>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-volume-off blue" style="font-size: 15px"></i>
													&nbsp;制度类型 <span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div datatype="instituType" class=" pull-left ">
														<select class="populate" id="instituType" name="instituType" style="cursor:auto;width: 200px">
															<optgroup label="选择制度类型"></optgroup>
															<c:choose>
																<c:when test="${not empty listInstituType}">
																	<c:forEach items="${listInstituType}" var="instituType" varStatus="status">
																		<option value="${instituType.id}">${instituType.typeName}</option>
																	</c:forEach>
																</c:when>
															</c:choose>
														</select>
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline" style="clear:both">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-user blue"></i>
													&nbsp;查看范围
												</div>
												<div class="ticket-user pull-left ps-right-box" id="scope" style="min-width: 135px">
													<label class="padding-left-5">
														<button id="dep" class="btn btn-info btn-primary btn-xs" type="button">按部门</button>
													</label>

													<label class="padding-left-5">
														<button id="user" class="btn btn-info btn-primary btn-xs" type="button">按人员</button>
													</label>
													<span style="color: red">*</span> <span>未设置查看范围则全体成员可见</span>
												</div>

												<div style="float: left;width: 250px;display: none">
													<select list="listScopeDep" listkey="id" listvalue="depName" id="scopeDep_select" name="listScopeDep.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
													</select>
												</div>
												<div style="float: left;width: 250px;display: none">
													<select list="listScopeUser" listkey="userId" listvalue="userName" id="scopeUser_select" name="listScopeUser.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
													</select>
												</div>
											</li>
											<li id="depLi" class="ticket-item no-shadow ps-listline" style="clear:both;display:none;">
												<div class="pull-left gray ps-left-text">&nbsp;部门</div>
												<div class="pull-left gray ps-left-text padding-left-5" style="width:70%" id="depBox"></div>
												<div style="clear:both;"></div>
											</li>
											<li id="userLi" class="ticket-item no-shadow ps-listline" style="clear:both;display:none;">
												<div class="pull-left gray ps-left-text">&nbsp;人员</div>
												<div class="pull-left gray ps-left-text padding-left-5" style="width:70%" id="userBox"></div>
												<div style="clear:both;"></div>
											</li>

											<li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text">
													提醒方式：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
														<label class="padding-left-5">
														 	<input type="checkbox" class="colored-blue" name="sendWay" value="todo"/>
														 	<span class="text" style="color:inherit;">待办事项</span>
													    </label>
													    <label class="padding-left-5">
														 	<input type="checkbox" class="colored-blue" name="sendWay" checked="checked" value="msg"/>
														 	<span class="text" style="color:inherit;">普通提醒</span>
													    </label>
												</div>
											</li> 
										</ul>
									</div>
								</div>
							</div>

							<div class="widget radius-bordered  " id="remark" style="clear:both">
								<div class="widget-header bg-bluegray no-shadow">
									<span class="widget-caption blue"><span style="color: red">*</span>制度内容</span>
									<div class="widget-buttons btn-div-full">
										<a class="ps-point btn-a-full" data-toggle="collapse">
											<i class="fa fa-plus blue"></i>
										</a>
									</div>
								</div>
								<!--tip="请填写制度内容"   -->
								<div class="widget-body no-shadow">
									<div class="tickets-container bg-white">
										<textarea class="form-control" id="instituRemark" name="instituRemark" rows="" cols="" style="width: 100%;height: 110px;display:none;"></textarea>
										<iframe id="eWebEditor1" src="/static/plugins/ewebeditor/ewebeditor.htm?id=instituRemark&style=blue" frameborder="0" scrolling="no" width="100%" height="350"></iframe>
									</div>
								</div>
							</div>
							<div class="widget-body no-shadow"></div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</form>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);
	</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
