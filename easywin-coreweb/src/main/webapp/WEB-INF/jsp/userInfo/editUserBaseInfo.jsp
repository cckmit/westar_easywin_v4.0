<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script type="text/javascript"> 
$(function(){
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
});
//设置访问成功后的跳转地址
function setLocation(){
	$("#redirectPage").val(window.self.location);
}

$(document).ready(function() {
    $(document).keydown(function(e) {
        if (e.keyCode == 13) {
        	return false;
        }
    });
 });
</script>
</head>
<body style="background-color: #fff" onload="resizeVoteH('userCenter');setLocation();">
<div id="home" class="tab-pane active">
	<div id="tabs-1" class="panel-collapse collapse in">
		<!--nav-tabs style 1-->
		<div class="tc-tabs">
			<!-- Nav tabs style 2-->
			<ul class="nav nav-tabs tab-color-dark background-dark">
				<li class="active"><a href="/userInfo/editUserBaseInfo?sid=${param.sid}">基本信息</a></li>
				<li><a href="/userInfo/editUserHeadImg?sid=${param.sid}">头像设置</a></li>
				<li><a href="/userInfo/editUserTel?sid=${param.sid}">联系信息</a></li>
				<li><a href="/userInfo/immediateSuper?sid=${param.sid}">直属上级</a></li>
				<li><a href="/userInfo/handOverPage?sid=${param.sid}">离职交接</a></li>
				<li><a href="/usagIdea/listPagedUsagIdea?sid=${param.sid}">常用意见</a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="row">
						<div class="col-sm-12">
							<form action="/userInfo/editUserInfo" method="post" class="subform">
							<input type="hidden" name="sid" value="${param.sid }"/>
					   		<input type="hidden" name="email" value="${sessionUser.email}"/>
					   		<input type="hidden" name="id" value="${sessionUser.id}"/>
					   		<input type="hidden" id="redirectPage" name="redirectPage" />
							<div class="panel-body">
								<div class="ws-introduce2">
									<span>团队名称：</span>
									<div class="ws-form-group">
										<div class="form-control view-input" readonly="readonly" >${sessionUser.orgName}</div>
									</div>
									<div class="ws-clear"></div>
								</div>
								<div class="ws-introduce2">
									<span>账号：</span>
									<div class="ws-form-group">
										<div class="form-control view-input" readonly="readonly" >${sessionUser.email}</div>
									</div>
									<div class="ws-clear"></div>
								</div>
								<div class="ws-introduce2">
									<span>姓名：</span>
									<input class="colorpicker-default form-control" type="text" name="userName" value="${sessionUser.userName}">
									<div class="ws-clear"></div>
								</div>
								<div class="ws-introduce2">
									<span>登录别名：</span>
									<input class="colorpicker-default form-control" type="text" name="nickname" datatype="*" ignore="ignore" value="${sessionUser.nickname}" ajaxurl="/userInfo/checkNickName?email=${sessionUser.email}&sid=${param.sid}">
									<div class="ws-clear"></div>
								</div>
								<div class="ws-introduce2">
									<span>性别：</span>
									<tags:radioDataDic type="gender" name="gender" style="ws-radio" value="${sessionUser.gender}"></tags:radioDataDic>
									<div class="ws-clear"></div>
								</div>
								<div class="ws-introduce2">
									<span>职位：</span>
									<input class="colorpicker-default form-control" type="text" name="job" value="${sessionUser.job }">
									<div class="ws-clear"></div>
								</div>
								<div class="ws-introduce2">
									<span>生日：</span>
									<input class="colorpicker-default form-control" type="text"name="birthday" onclick="WdatePicker({dateFmt:'yyyy年MM月dd日'});" readonly="readonly" value="${sessionUser.birthday }">
									<div class="ws-clear"></div>
								</div>
								<div class="panel-body">
									<button class="btn btn-info ws-btnBlue2" style="margin-left:200px;" type="submit">修改</button>
								</div>
							</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--nav-tabs style 2-->
	</div>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>

