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
<script type="text/javascript">
$(function(){
	//初始化名片
	initCard('${param.sid}');
	
	$(".searchForm").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		callback:function (form){
			//提交前验证是否在上传附件
			return sumitPreCheck(null);
		},
		showAllError : true
	});
	//发布任务
	$("#newTask").click(function(){
		window.location.href="/task/addTaskPage?sid=${param.sid}";
	});
	 //页面刷新
	$("#refreshImg").click(function(){
		window.self.location.reload();
	});
});

//任务查看权限验证
function viewTask(taskId){
	$.post("/task/authorCheck?sid=${param.sid}",{Action:"post",taskId:taskId},     
			function (msgObjs){
			if(!msgObjs.succ){
				showNotification(1,msgObjs.promptMsg);
			}else{
				var url="/task/viewTask?sid=${param.sid}&owner=${task.owner}"
					+"&operator=${task.operator}&taskName=${task.taskName}&state=${task.state}&id="+taskId
					+"&redirectPage="+encodeURIComponent(window.location.href)
					+"&startDate=${task.startDate}&endDate=${task.endDate}";
				window.location.href=url;
			}
	},"json");
}

//模块查看
function viewBus(busId,busType,busState){
	if(busType=='005'){
		$.post("/item/authorCheck?sid=${param.sid}",{Action:"post",itemId:busId},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					var url="/item/viewItemPage?sid=${param.sid}&id="+busId+"&state="+busState+"&redirectPage="+encodeURIComponent(window.location.href);
					window.location.href=url;
				}
		},"json");
	}else if(busType=='012'){
		$.post("/crm/authorCheck?sid=${param.sid}",{Action:"post",customerId:busId},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/crm/viewCustomer?sid=${param.sid}&id="+busId+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}
}
function formSub(){
	$(".searchForm").submit();
}
//人员筛选
function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
	if(!strIsNull(userIdtag) && "operator"==userIdtag){
		//经办人员筛选
		var url="/task/taskToDoListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&taskName=${task.taskName}"
			+"&state=${task.state}&operator="+userId+"&startDate=${task.startDate}&endDate=${task.endDate}";
		window.location.href=url;
	}else if(!strIsNull(userIdtag) && "owner"==userIdtag){
		//责任人筛选
		var url="/task/taskToDoListPage?pager.pageSize=10&sid=${param.sid}&operator=${task.operator}&taskName=${task.taskName}"
			+"&state=${task.state}&owner="+userId+"&startDate=${task.startDate}&endDate=${task.endDate}";
		window.location.href=url;
	}
}
//筛选栏筛选选项初始化
$(document).ready(function(){
	//更多筛选字样显示
    if(${(not empty task.startDate || not empty task.endDate)}){
        $("#moreFilterCondition").html("隐藏");
    }
});
$(function(){
	//更多筛选条件显示层
	$("#moreFilterCondition").click(function(){
        var display = $("#moreFilterCondition_div").css("display");
        if("none"==display){
            $(this).html('隐藏');
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }else if("block"==display){
            $(this).html('更多筛选')
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }

      });
	//任务名筛选
	$("#searchTaskName").blur(function(){
		var url="/task/taskToDoListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&operator=${task.operator}"
			+"&state=${task.state}&taskName="+$("#searchTaskName").val()+"&startDate=${task.startDate}&endDate=${task.endDate}";
		window.location.href=url;
	});
	//文本框绑定回车提交事件
	$("#searchTaskName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#searchTaskName").val())){
        		var url="/task/taskToDoListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&operator=${task.operator}"
        		+"&state=${task.state}&taskName="+$("#searchTaskName").val()+"&startDate=${task.startDate}&endDate=${task.endDate}";
        		window.location.href=url;
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#searchTaskName").focus();
        	}
        }
    });
	//督察人员维护(关闭后需要重新加载页面，用户权限可能改变，重新取得sessionUser)
	$("#forceIn").click(function(){
		art.dialog.open("/task/editForceInPersionPage?sid=${param.sid}", {
					title : "监督设置",
					lock : true,
					max : false,
					min : false,
					width : 500,
					height :200,
					ok: function () {
						var iframe = this.iframe.contentWindow;
						if (!iframe.document.body) {
							return false;
						}
						;
						iframe.formSub();
						return false;
				    },
				    cancelVal: '关闭',
				    cancel: true,
				    close:function(){
				    	var win = art.dialog.open.origin;
				    	win.location.reload();
				    }
				});
	});
});
//选择日期
function selectDate(){
	var url="/task/taskToDoListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&operator=${task.operator}";
	url+="&state=${task.state}&taskName="+$("#searchTaskName").val()+"&startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val();
	window.location.href=url;
}
</script>
<style>
	.noread{color: #000;font-weight:bold;font-size: 13px}
</style>
</head>
<body>
<section id="container">
<!-- 系统头部装载 -->
<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<!-- 左边菜单 -->
<jsp:include page="/WEB-INF/jsp/include/left_menu.jsp"></jsp:include>
<section id="main-content" style="margin-right:20px;">
<!--任务按钮-->
<div class="wrapper wrapper3">
<div class="panel-body">
	<div id="tabs-1" class="panel-collapse collapse in">
		<ul class="nav nav-tabs tab-color-dark background-dark">
			<li class="active"><a href="javascript:void(0)" data-toggle="tab" onclick="javascript:window.location.href='/workFlow/deploy?sid=${param.sid}'">流程部署</a></li>
			<li><a href="javascript:void(0)" data-toggle="tab" onclick="javascript:window.location.href='/workFlow/processList?sid=${param.sid}'">部署多少</a></li>
			<li><a href="javascript:void(0)" data-toggle="tab" onclick="javascript:window.location.href='/workFlow/startWrokFlow?sid=${param.sid}'">启动实例</a></li>
			<li><a href="javascript:void(0)" data-toggle="tab" onclick="javascript:window.location.href='/workFlow/listSpTodDo?sid=${param.sid}'">待办审批</a></li>
			<li><a href="javascript:void(0)" data-toggle="tab" onclick="javascript:window.location.href='/workFlow/complete/${taskID}?sid=${param.sid}'">审批办理</a></li>
			<li><a href="javascript:void(0)" data-toggle="tab" onclick="javascript:window.location.href='/workFlow/listHisActiviti/${processInstanceId}?sid=${param.sid}'">审批历史</a></li>
		</ul>
		<!--nav-tabs style 1-->
		<center><h1>工作流审批</h1></center>
		</div>
	</div>
</div>
</section>
<!--main content end-->
</section>
</body>
</html>