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

$(document).ready(function() {
    $(document).keydown(function(e) {
        if (e.keyCode == 13) {
        	return false;
        }
    });
    //初始化分享设置
    if(!strIsNull($("#leaderJson").val())){
		var users = eval("("+$("#leaderJson").val()+")");
		  var img="";
			for (var i=0, l=users.length; i<l; i++) {
				//数据保持
				$("#listUserInfo_id").append('<option selected="selected" value='+users[i].userID+'>'+users[i].userName+'</option>');
				//参与人显示构建
				img = img + "<div class=\"online-list\" id=\"user_img_"+users[i].userID+"\" ondblclick=\"removeClickUser('listUserInfo_id',"+users[i].userID+")\" title=\"双击移除\">";
				img = img + "<div class=\"online-head\">"
				img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" title=\"双击移除\"/>"
				img = img + "</div>"
				img = img + "<div class=\"online-name\">"
				img = img + "<span class=\"ws-smallSize\">"+users[i].userName+"</span>"
				img = img + "</div>"
				img = img + "<div class=\"ws-clear\"></div>"
				img = img + "</div>"
			}
			$("#userLeader_div").html(img);
			resizeVoteH('userCenter');
	}
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
				<li><a href="/userInfo/editUserTel?sid=${param.sid}">联系信息</a></li>
				<li class="active"><a href="/userInfo/immediateSuper?sid=${param.sid}">直属上级</a></li>
				<li><a href="/userInfo/handOverPage?sid=${param.sid}">离职交接</a></li>
				<li><a href="/usagIdea/listPagedUsagIdea?sid=${param.sid}">常用意见</a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body" style="width:300px;">
						<section class="panel">
							<header class="panel-heading">
								直属上级：
								<tags:userMore name="listUserInfo.id" showName="userName" callBackStart="yes"  disDivKey="userLeader_div"></tags:userMore>
								<input type="hidden" id="leaderJson" name="leaderJson" value="${leaderJson}"/>&nbsp;
								<span class="tools pull-right">
						           <small class="ws-active ws-color">建议：只设置直属上级</small>
						        </span>
							</header>
							<div class="panel-body" id="userLeader_div">
							</div>
						</section>
					</div>
				</div>
			</div>
		</div>
		<!--nav-tabs style 2-->
	</div>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
<script type="text/javascript">
//直属上级更新
function userMoreCallBack(){
	var userIds =new Array();
	$("#listUserInfo_id option").each(function() { 
		userIds.push($(this).val()); 
    });
	if(!strIsNull(userIds.toString())){
		$.post("/userInfo/updateImmediateSuper?sid=${param.sid}",{Action:"post",userIds:userIds.toString()},     
			function (msgObjs){
			if(msgObjs.succ){
				showNotification(0,msgObjs.promptMsg);
				setTimeout(function(){
					resizeVoteH('userCenter');
				},200);
			}else{
				showNotification(1,msgObjs.promptMsg);
			}
		},"json");
	}
}
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
	//用户姓名
	var userName = '${empty sessionUser.userName?sessionUser.email:sessionUser.userName}';
	$(window.parent.userNameTda).html(userName);

	var percent = parseInt(((jifenScore-minLevJifen)/(nextLevJifen-minLevJifen)*100)+'');
	$(window.parent.s1).css("width",percent+"%");
}

/* 清除下拉框中选择的option */
function removeClickUser(id,selectedUserId) {
	var selectobj = document.getElementById(id);
	for ( var i = 0; i < selectobj.options.length; i++) {
		if (selectobj.options[i].value==selectedUserId) {
			selectobj.options[i] = null;
			break;
		}
	}
	$("#user_img_"+selectedUserId).remove();
	$.post("/userInfo/delImmediateSuper?sid=${param.sid}",{Action:"post",userId:selectedUserId},     
		function (msgObjs){
		if(msgObjs.succ){
			showNotification(0,msgObjs.promptMsg);
			resizeVoteH('userCenter');
		}else{
			showNotification(1,msgObjs.promptMsg);
		}
	},"json");
	selected(id);
}
</script>
</body>
</html>

