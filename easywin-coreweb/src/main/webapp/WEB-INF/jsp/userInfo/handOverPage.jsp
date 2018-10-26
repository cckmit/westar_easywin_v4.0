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
	//离职交接
	$("#dimission").click(function(){
		art.dialog.confirm("确定进行离职交接?", function(){
		});
	});
});

//人员单选回调函数
function userOneCallBack(){
	var userId = $("#userId").val();
	var userName = $("#userName").val();
	var id = '${userInfo.id}';
	$("#userOneImguserId").remove();
	$("#userId").val('');
	$("#userName").val('')
	if(userId){
		if(userId==id){
			showNotification(1,"不能移交给自己!请重新选择");
		}else{
			art.dialog.confirm("确定交接给"+userName+"?", function(){
				$.ajax({
				  type : "POST",
				  url : "/userInfo/dimission?sid=${param.sid}&rnd=" + Math.random(),
				  dataType:"json",
				  data:{userId:userId,userName:userName},
				  success:function(data){
					  if(data.status=='y'){
						  window.parent.art.dialog({
							    title: '交接成功',
							    content: '感谢你为公司的付出！',
							    icon: 'succeed',
							    ok: function(){
								    window.parent.location.href="/login.jsp";
							    },
							    close:function(){
								    window.parent.location.href="/login.jsp";
								}
							});
						}else{
							showNotification(1,data.info);
						}
				  },
				  error:function(){
					  showNotification(1,"系统错误，请联系系统管理员！");
				  }
				});
			});
		}
	}
}
</script>

</head>
<body style="background-color: #fff" onload="resizeVoteH('userCenter');">
<div id="home" class="tab-pane active">
	<div id="tabs-1" class="panel-collapse collapse in">
		<!--nav-tabs style 1-->
		<div class="tc-tabs">
			<!-- Nav tabs style 2-->
			<ul class="nav nav-tabs tab-color-dark background-dark">
				<li><a href="/userInfo/editUserBaseInfo?sid=${param.sid}">基本信息</a></li>
				<li><a href="/userInfo/editUserHeadImg?sid=${param.sid}">头像设置</a></li>
				<li><a href="/userInfo/editUserTel?sid=${param.sid}">联系信息</a></li>
				<li><a href="/userInfo/immediateSuper?sid=${param.sid}">直属上级</a></li>
				<li class="active"><a href="/userInfo/handOverPage?sid=${param.sid}">离职交接</a></li>
				<li><a href="/usagIdea/listPagedUsagIdea?sid=${param.sid}">常用意见</a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body">
						<div class="ws-other-group">
							<span class="ws-pull-left">离职交接：</span>
							<tags:userOne name="userId" showValue="" showName="userName" value="0" callBackStart="yes"></tags:userOne>
							<div class="ws-clear"></div>
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

