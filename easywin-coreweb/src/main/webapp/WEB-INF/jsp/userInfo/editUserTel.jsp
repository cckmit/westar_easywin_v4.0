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
//设置等级
function setLev(){
	var jifenScore = ${sessionUser.jifenScore};
	//用户现有积分
	$(window.parent.jifenScore).html(jifenScore);
	//本阶段最少分数
	var minLevJifen = ${sessionUser.minLevJifen};
	//下一阶段的最低分数
	var nextLevJifen = ${sessionUser.nextLevJifen};
	//设计需要的积分
	$(window.parent.needJifen).html(nextLevJifen-jifenScore);
	//积分等级标准
	$(window.parent.s2).html(jifenScore+'/'+nextLevJifen);

	//等级名称
	var levName = '${sessionUser.levName}';
	$(window.parent.levName).html(levName);

	var percent = parseInt(((jifenScore-minLevJifen)/(nextLevJifen-minLevJifen)*100)+'');
	$(window.parent.s1).css("width",percent+"%");
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
<body style="background-color: #fff" onload="resizeVoteH('userCenter');setLocation();setLev()">
<div id="home" class="tab-pane active">
	<div id="tabs-1" class="panel-collapse collapse in">
		<!--nav-tabs style 1-->
		<div class="tc-tabs">
			<!-- Nav tabs style 2-->
			<ul class="nav nav-tabs tab-color-dark background-dark">
				<li><a href="/userInfo/editUserBaseInfo?sid=${param.sid}">基本信息</a></li>
				<li><a href="/userInfo/editUserHeadImg?sid=${param.sid}">头像设置</a></li>
				<li class="active"><a href="/userInfo/editUserTel?sid=${param.sid}">联系信息</a></li>
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
					   		<input type="hidden" name="id" value="${sessionUser.id }"/>
					   		<input type="hidden" id="redirectPage" name="redirectPage" />
							<div class="panel-body">
								<div class="ws-introduce2">
									<span>邮箱：</span>
									
									<div class="ws-form-group">
										<div class="form-control view-input" readonly="readonly" >${sessionUser.email}</div>
									</div>
									<div class="ws-clear"></div>
								</div>
								<div class="ws-introduce2">
									<span>qq：</span>
									<input class="colorpicker-default form-control" type="text" name="qq" value="${sessionUser.qq}">
									<div class="ws-clear"></div>
								</div>
								<div class="ws-introduce2">
									<span>手机：</span>
									<input class="colorpicker-default form-control" type="text" name="movePhone" datatype="m" ignore="ignore" value="${sessionUser.movePhone}">
									<div class="ws-clear"></div>
								</div>
								<div class="ws-introduce2">
									<span>微信：</span>
									<input class="colorpicker-default form-control" type="text" name="wechat" value="${sessionUser.wechat}">
									<div class="ws-clear"></div>
								</div>
								<div class="ws-introduce2">
									<span>座机号码：</span>
									<input class="colorpicker-default form-control pull-left" type="text" name="linePhone" datatype="zj" ignore="ignore" value="${sessionUser.linePhone}">
						   			<div class="pull-left">
						   				<font color="gray">(以'-'间隔)</font>
						   			</div>
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

