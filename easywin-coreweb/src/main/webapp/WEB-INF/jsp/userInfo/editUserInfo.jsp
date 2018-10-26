<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<%@taglib prefix="t" uri="/WEB-INF/tld/t.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script>
$(function(){
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		callback:function(form){
			updatePassword();
			return false;
		},
		showAllError : true
	});
	
	$("#baseInfoTab").click(function(){
		$(".active").removeAttr("class");
		$(this).parent().attr("class","active")
		//个人中心
		$("#userCenter").attr("src", "/userInfo/editUserBaseInfo?sid=${param.sid}");
	});
	//我的组群管理点击事件
	$("#selfGroup").click(function(){
		$(".active").removeAttr("class");
		$(this).parent().attr("class","active")
		$("#userCenter").attr("src", "/selfGroup/listUserGroup?sid=${param.sid}&pager.pageSize=10");
	});
	$("#passTab").click(function(){
		$(".active").removeAttr("class");
		$(this).parent().attr("class","active")
		//密码修改
		$("#userCenter").attr("src", "/userInfo/updateUserPasswordPage?sid=${param.sid}");
	});
	$("#noticeTab").click(function(){
		$(".active").removeAttr("class");
		$(this).parent().attr("class","active")
		//通知设置
		$("#userCenter").attr("src", "/userInfo/msgShowSettingPage?sid=${param.sid}")
	});
	$("#mailTab").click(function(){
		$(".active").removeAttr("class");
		$(this).parent().attr("class","active")
		//通知设置
		$("#userCenter").attr("src", "/mailSet/listMailSet?sid=${param.sid}")
	});
	
});
//设置等级
function setLev(){
	var viewType = '${viewType}';
	if(strIsNull(viewType)){//默认是修改基本信息
		$(".active").removeAttr("class");
		$("#baseInfoTab").parent().attr("class","active");
		$("#userCenter").attr("src", "/userInfo/editUserBaseInfo?sid=${param.sid}")
	}else if('12'==viewType){//修改头像
		//$("#baseInfoTab").parent().find("li").removeAttr("class");
		//$("#baseInfoTab").attr("class","ws-active");
		//$("#userCenter").attr("src", "/userInfo/editUserHeadImg?sid=${param.sid}")
	}else if('selfGroupAdd'==viewType){//个人私有组添加
		$(".active").removeAttr("class");
		$("#selfGroup").parent().attr("class","active");
		$("#userCenter").attr("src", "/selfGroup/addUserGroupPage?sid=${param.sid}&redirectPage="+encodeURIComponent("/selfGroup/listUserGroup?sid=${param.sid}&pager.pageSize=10"))
	}
	
}
</script>
</head>
<body  onload="setLev();">
<section id="container">
<!-- 系统头部装载 -->
<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<!-- 左边菜单 -->
<jsp:include page="/WEB-INF/jsp/include/left_menu.jsp"></jsp:include>
<!--main content start-->
<section id="main-content">
	<!--任务按钮-->
	<section class="wrapper wrapper2">
		<div class="ws-wrapIn">
			<div class="ws-tabTit">
				<ul class="ws-tabBar">
					<li><a href="/userInfo/aboutUserInfoPage?sid=${param.sid}">个人中心</a></li>
				</ul>
			</div>
			<div class="ws-projectBox">

				<div class="row">
					<div class="col-sm-12">
						<!--任务概述-->
						<section class="panel">
							<header class="panel-heading tab-bg-dark-navy-blue ">
								<ul class="nav nav-tabs">
									<li class="active">
										<a data-toggle="tab" href="javascript:void(0);" id="baseInfoTab">基础设置</a>
									</li>
									<li>
										<a data-toggle="tab" href="javascript:void(0);" id="selfGroup">我的组群</a>
									</li>
									<li>
										<a data-toggle="tab" href="javascript:void(0);" id="mailTab">邮箱推送设置</a>
									</li>
									<li>
										<a data-toggle="tab" href="javascript:void(0);" id="passTab">密码修改</a>
									</li>
									<li>
										<a data-toggle="tab" href="javascript:void(0);" id="noticeTab">通知设置</a>
									</li>
								</ul>
							</header>
							<div class="panel-body">
								<div class="tab-content">
									<iframe id="userCenter" style="width: 100%;" frameborder="0" scrolling="no" src="/userInfo/editUserBaseInfo?sid=${param.sid}"></iframe>
								</div>
							</div>
						</section>
					</div>
				</div>
			</div>
		</div>
	</section>

</section>
<!--main content end-->
<!--right sidebar start-->
<div class="right-sidebar">
	<div class="ws-transactor">
		<div class="ws-bigheadImg" id="bigheadImg">
   			<img src="/downLoad/userImg/${userInfo.comId}/${userInfo.id}?sid=${param.sid}&size=1"></img>
		</div>
		<div class="ws-personalNews">
			<p>姓名：${userInfo.userName}</p>
			<p>部门：${userInfo.depName}</p>
			<p>移动电话：${userInfo.movePhone}</p>
		</div>
	</div>
	<section class="panel">
		<header class="panel-heading">
			其他信息
			<span class="tools pull-right">
               <a href="javascript:;" class="fa fa-chevron-down"></a>
            </span>
		</header>
		<div class="panel-body">
			<div class="online-list"><i class="fa fa-volume-down"></i>待办任务<span class="ws-color">+3</span></div>
			<div class="online-list"><i class="fa fa-volume-down"></i>待办任务<span class="ws-color">+3</span></div>
			<div class="online-list"><i class="fa fa-volume-down"></i>待办任务<span class="ws-color">+3</span></div>
		</div>
	</section>

</div>
	<!--right sidebar end-->
</section>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
