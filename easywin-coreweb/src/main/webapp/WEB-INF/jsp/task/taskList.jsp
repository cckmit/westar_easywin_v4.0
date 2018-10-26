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
	//任务删除
	$("#delTask").click(function(){
		if(checkCkBoxStatus('ids')){
			art.dialog.confirm('确定要删除该任务吗？', function(){
				$("#delForm input[name='redirectPage']").val(window.location.href);
				$('#delForm').submit();
			}, function(){
				art.dialog.tips('已取消');
			});	
		}else{
			art.dialog.alert('请先选择需要删除的任务。');
		}
	});
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
	//客户合并
	$("#compressTask").click(function(){
		//多选
		var ckBoxs = $("#delForm").find(":checkbox[name='ids']:checked");
		var ids = new Array();
		for(var i=0;i<ckBoxs.length;i++){
			if($(ckBoxs[i]).attr("state")!=1){
				art.dialog.alert('请选择未办结任务');
				return;
			}
			ids.push($(ckBoxs[i]).val());
		}
		if(ckBoxs.length==0){
			art.dialog.alert('请先选择需要合并的任务。');
		}else if(ckBoxs.length==1){
			art.dialog.alert('请先选择至少两个任务');
		}else if(ckBoxs.length>3){
			art.dialog.alert('最多选择三个任务');
		}else{
			var url="/task/taskCompressPage?sid=${param.sid}&ids="+ids+"&redirectPage="+encodeURIComponent(window.location.href)
			window.location.href=url;
		}
	});
});
//任务查看权限验证
function viewTask(taskId){
	$.post("/task/authorCheck?sid=${param.sid}",{Action:"post",taskId:taskId},     
			function (msgObjs){
			if(!msgObjs.succ){
				showNotification(1,msgObjs.promptMsg);
			}else{
				var url="/task/viewTask?sid=${param.sid}&owner=${task.owner}&executor=${task.executor}"
					+"&operator=${task.operator}&taskName=${task.taskName}&state=${task.state}&id="+taskId
					+"&redirectPage="+encodeURIComponent(window.location.href)
					+"&startDate=${task.startDate}&endDate=${task.endDate}&execuType=${task.execuType}"
				window.location.href=url;
			}
	},"json");
}
function formSub(){
	$(".searchForm").submit();
}
//筛选栏筛选选项初始化
$(document).ready(function(){
	//更多筛选字样显示
    if(${(not empty task.startDate || not empty task.endDate)}){
        $("#moreFilterCondition").html("隐藏");
    }
});
//办理人类别筛选
function execuTypeFilter(ts,execuType){
	var style=$(ts).attr("style");
	if(style){//原来选中，现在清除
		var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&executor=${task.executor}"
			+"&taskName=${task.taskName}&state=${task.state}&startDate=${task.startDate}&endDate=${task.endDate}&execuType=";
		window.location.href=url;
	}else{//现在选中
		if(execuType==0){
			var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&executor="
				+"&taskName=${task.taskName}&state=${task.state}&startDate=${task.startDate}&endDate=${task.endDate}&execuType="+execuType;
			window.location.href=url;
		}else{
			var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&executor=${task.executor}"
				+"&taskName=${task.taskName}&state=${task.state}&startDate=${task.startDate}&endDate=${task.endDate}&execuType="+execuType;
			window.location.href=url;
		}
	}
}
//状态筛选
function taskStateFilter(stateId){
	var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&executor=${task.executor}"
		+"&taskName=${task.taskName}&state="+stateId+"&startDate=${task.startDate}&endDate=${task.endDate}&execuType=${task.execuType}";
	window.location.href=url;
}
//人员筛选
function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
	if(!strIsNull(userIdtag) && "executor"==userIdtag){
		var execuType = '${task.execuType}';
		if(execuType && execuType==0){//原来选中的是自己负责的
			//办理人员筛选
			var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&operator=${task.operator}&execuType="
				+"&taskName=${task.taskName}&state=${task.state}&executor="+userId+"&startDate=${task.startDate}&endDate=${task.endDate}";
			window.location.href=url;
		}else{
			//办理人员筛选
			var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&operator=${task.operator}&execuType=${task.execuType}"
				+"&taskName=${task.taskName}&state=${task.state}&executor="+userId+"&startDate=${task.startDate}&endDate=${task.endDate}";
			window.location.href=url;
		}
	}else if(!strIsNull(userIdtag) && "owner"==userIdtag){
		//发起人筛选
		var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&executor=${task.executor}&operator=${task.operator}&execuType=${task.execuType}"
			+"&taskName=${task.taskName}&state=${task.state}&owner="+userId+"&startDate=${task.startDate}&endDate=${task.endDate}";
		window.location.href=url;
	}else if(!strIsNull(userIdtag) && "operator"==userIdtag){
		//发起人筛选
		var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&executor=${task.executor}"
			+"&taskName=${task.taskName}&state=${task.state}&operator="+userId
			+"&startDate=${task.startDate}&endDate=${task.endDate}&execuType=${task.execuType}";
		window.location.href=url;
	}
}
$(function(){
	//任务名筛选
	$("#searchTaskName").blur(function(){
		var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&executor=${task.executor}"
			+"&state=${task.state}&taskName="+$("#searchTaskName").val()+"&operator=${task.operator}"
			+"&startDate=${task.startDate}&endDate=${task.endDate}&execuType=${task.execuType}";
		window.location.href=url;
	});
	//文本框绑定回车提交事件
	$("#searchTaskName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#searchTaskName").val())){
        		var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&executor=${task.executor}"
            		+"&state=${task.state}&taskName="+$("#searchTaskName").val()+"&operator=${task.operator}"
					+"&startDate=${task.startDate}&endDate=${task.endDate}&execuType=${task.execuType}";
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
	 //页面刷新
	$("#refreshImg").click(function(){
		window.self.location.reload();
	});
});
//选择日期
function selectDate(){
	var url="/task/taskListPage?pager.pageSize=10&sid=${param.sid}&owner=${task.owner}&executor=${task.executor}"
		+"&state=${task.state}&taskName="+$("#searchTaskName").val()+"&execuType=${task.execuType}&operator=${task.operator}"
		+"&startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val();
	window.location.href=url;
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
<form action="/task/delTask" method="post" id="delForm">
<input type="hidden" id="redirectPage" name="redirectPage"/>
<input type="hidden" name="sid" value="${param.sid}"/>
<input type="hidden" id="state" name="state" value="${task.state}"/>
<input type="hidden" id="owner" name="owner" value="${task.owner}"/>
<input type="hidden" id="executor" name="executor" value="${task.executor}"/>
<input type="hidden" id="taskName" name="taskName" value="${task.taskName}"/>
<section id="main-content" style="margin-right:20px;">
<!--任务按钮-->
<div class="wrapper wrapper3">
<div class="panel-body">
	<div id="tabs-1" class="panel-collapse collapse in">
		<!--nav-tabs style 1-->
		<div class="tc-tabs">
			<!-- Nav tabs style 2-->
			<ul class="nav nav-tabs tab-color-dark background-dark">
				<li><a href="javascript:void(0)" data-toggle="tab" onclick="javascript:window.location.href='/task/taskToDoListPage?pager.pageSize=10&sid=${param.sid}'">待办任务</a></li>
				<li><a href="javascript:void(0)" data-toggle="tab" onclick="javascript:window.location.href='/task/taskChargeListPage?pager.pageSize=10&sid=${param.sid}'">我负责的</a></li>
				<li class="active"><a href="javascript:void(0)" data-toggle="tab" onclick="javascript:window.location.href='/task/taskListPage?pager.pageSize=10&sid=${param.sid}'">所有任务</a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				<div class="tab-pane active" id="tab3">
					<div class="row">
						<div class="col-sm-12">
							<section class="panel">
								<header class="panel-heading">
									<div class="ws-headBox">
										<div class="pull-left ws-toolbar">
											<div class="dropdown pull-left">
												<a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0)">
												<c:choose>
													<c:when test="${task.state==1}"><font style="font-weight:bold;">进行中</font></c:when>
													<c:when test="${task.state==3}"><font style="font-weight:bold;">挂起中</font></c:when>
													<c:when test="${task.state==4}"><font style="font-weight:bold;">已完结</font></c:when>
													<c:otherwise>任务状态筛选</c:otherwise>
												</c:choose>
												<b class="caret"></b>
												</a>
												<ul class="dropdown-menu extended logout">
													<li><a href="javascript:void(0)" onclick="taskStateFilter('')">清空条件</a></li>
													<li><a href="javascript:void(0)" onclick="taskStateFilter('1')">进行中</a></li>
													<li><a href="javascript:void(0)" onclick="taskStateFilter('3')">挂起中</a></li>
													<li><a href="javascript:void(0)" onclick="taskStateFilter('4')">已完成</a></li>
												</ul>
											</div>
											<div class="dropdown pull-left" style="margin-right: 20px">
												<a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0)" 
												onclick="execuTypeFilter(this,'0')" ${task.execuType=='0'?"style='font-weight:bold' title='再次点击清空'":""} >
												我执行的
												</a>
											</div>
											<div class="dropdown pull-left" style="display:${userInfo.countSub>0?'block':'none'}">
												<a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0)"
												onclick="execuTypeFilter(this,'1')" ${task.execuType==1?"style='font-weight:bold' title='再次点击清空'":""}>
												下属执行
												</a>
											</div>
											<div class="dropdown pull-left">
												<a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0)">
												<c:choose>
													<c:when test="${not empty task.executorName}">
														<font style="font-weight:bold;">${task.executorName}</font>	
													</c:when>
													<c:otherwise>办理人筛选</c:otherwise>
												</c:choose>
												<b class="caret"></b>
												</a>
												<ul class="dropdown-menu extended logout">
													<li><a href="javascript:void(0)" onclick="userOneForUserIdCallBack('','executor','','')">清空条件</a></li>
													<li><a href="javascript:void(0)" onclick="userOneForUserId('${userInfo.id}','${userInfo.userName}','','${param.sid}','executor');">人员选择</a></li>
													<c:choose>
														<c:when test="${not empty listOwners}">
															<hr style="margin: 8px 0px" />
															<c:forEach items="${listOwners}" var="owner" varStatus="vs">
																<li><a href="javascript:void(0)"  onclick="userOneForUserIdCallBack('${owner.id}','executor','${owner.userName}','');">${owner.userName}</a></li>
															</c:forEach>
														</c:when>
													</c:choose>
												</ul>
											</div>
											<div class="dropdown pull-left">
												<a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0)">
												<c:choose>
													<c:when test="${not empty task.operatorName}">
														<font style="font-weight:bold;">${task.operatorName}</font>	
													</c:when>
													<c:otherwise>经办人筛选</c:otherwise>
												</c:choose>
												<b class="caret"></b>
												</a>
												<ul class="dropdown-menu extended logout">
													<li><a href="javascript:void(0)" onclick="userOneForUserIdCallBack('','operator','','')">清空条件</a></li>
													<li><a href="javascript:void(0)" onclick="userOneForUserId('${userInfo.id}','${userInfo.userName}','','${param.sid}','operator');">人员选择</a></li>
													<c:choose>
														<c:when test="${not empty listOwners}">
															<hr style="margin: 8px 0px" />
															<c:forEach items="${listOwners}" var="owner" varStatus="vs">
																<li><a href="javascript:void(0)"  onclick="userOneForUserIdCallBack('${owner.id}','operator','${owner.userName}','');">${owner.userName}</a></li>
															</c:forEach>
														</c:when>
													</c:choose>
												</ul>
											</div>
											<div class="dropdown pull-left">
												<a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0)">
												<c:choose>
													<c:when test="${not empty task.ownerName}">
														<font style="font-weight:bold;">${task.ownerName}</font>	
													</c:when>
													<c:otherwise>发起人筛选</c:otherwise>
												</c:choose>
												<b class="caret"></b>
												</a>
												<ul class="dropdown-menu extended logout">
													<li><a href="javascript:void(0)" onclick="userOneForUserIdCallBack('','owner','','')">清空条件</a></li>
													<li><a href="javascript:void(0)" onclick="userOneForUserId('${userInfo.id}','${userInfo.userName}','','${param.sid}','owner');">人员选择</a></li>
													<c:choose>
														<c:when test="${not empty listOwners}">
															<hr style="margin: 8px 0px" />
															<c:forEach items="${listOwners}" var="owner" varStatus="vs">
																<li><a href="javascript:void(0)"  onclick="userOneForUserIdCallBack('${owner.id}','owner','${owner.userName}','');">${owner.userName}</a></li>
															</c:forEach>
														</c:when>
													</c:choose>
												</ul>
											</div>
											<a href="javascript:void(0)" class="pull-left" id="moreFilterCondition">更多筛选</a>
										</div>
										<div class="ws-floatRight">
											<ul class="ws-icon">
												<li>
													<input id="searchTaskName" name="searchTaskName" value="${task.taskName}" type="text" class="form-control search ws-search-one" placeholder="任务名检索" title="任务名检索">
												</li>
												<c:if test="${empty add}">
													<li>
													<a href="javascript:void(0)" class="fa fa-plus-square-o fa-lg" id="newTask" title="发布任务"></a>
													</li>
												</c:if>
												<c:if test="${empty update}">
													<li>
														 <a href="javascript:void(0)" id="compressTask" class="fa  fa-compress fa-lg" title="合并任务"></a>
													</li>
												</c:if>
												<c:if test="${empty delete}">
													<li>
													<a href="javascript:void(0)" class="fa fa-trash-o fa-lg" id="delTask" title="任务删除"></a>
													</li>
												</c:if>
												<li>
													<div class="dropdown pull-right last-icon">
													<c:if test="${userInfo.admin>0}">
													<a data-toggle="dropdown" class="fa fa-bars" href="javascript:void(0)" title="更多操作">
													<b class="caret"></b>
													</a>
													<ul class="dropdown-menu extended logout">
														<li><a href="javascript:void(0)" id="forceIn">监督设置</a></li>
														<li><a href="javascript:void(0)" onclick="modOperateConfig('${param.sid}','003')">操作权限配置</a></li>
													</ul>
													</c:if>
													</div>
												</li>																
											</ul>
										</div>
										<div class="ws-clear"></div>
									</div>
									<div class="ws-none" id="moreFilterCondition_div" style="display:${(not empty task.startDate || not empty task.endDate)?'block':'none'};">
										<div class="form-inline">
										<div class="form-group">
											<div class="input-group input-large">
												<input class="form-control dpd1" type="text" readonly="readonly" value="${task.startDate}" id="startDate" name="startDate" placeholder="开始时间"  onFocus="WdatePicker({onpicked:function(){selectDate()},oncleared: function(){selectDate()},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})">
												<span class="input-group-addon">To</span>
												<input class="form-control dpd2" type="text" readonly="readonly" value="${task.endDate}" id="endDate"  name="endDate" placeholder="结束时间" onFocus="WdatePicker({onpicked:function(){selectDate()},oncleared: function(){selectDate()},dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})">
											</div>
										</div>
										</div>
									</div>
								</header>
								<div class="panel-body">
									<table class="table table-hover general-table">
										<thead>
											<tr>
											  <th width="4%" valign="middle">&nbsp;</th>
											  <th width="3%" valign="middle"><input type="checkbox" onclick="checkAll(this,'ids')" /></th>
											  <th width="5%" valign="middle" style="text-align: center;">序号</th>
											  <th width="10%" valign="middle" style="text-align: center;">紧急度</th>
											  <th valign="middle">任务名称</th>
											  <th width="15%" valign="middle" class="hidden-phone">关联模块</th>
											  <th width="11%" valign="middle">办理时限</th>
											  <th width="8%" valign="middle" style="text-align: center;">任务进度</th>
											  <th width="8%" valign="middle">办理人</th>
											  <th width="8%" valign="middle">发起人</th>
											  <th width="10%" valign="middle">发布时间</th>
											</tr>
										</thead>
										<tbody>
										<c:choose>
									 	<c:when test="${not empty list}">
									 		<c:forEach items="${list}" var="task" varStatus="status">
									 			<c:set var="gradeColor">
										 			<c:choose>
										 				<c:when test="${task.grade==4}">#FF0000</c:when>
										 				<c:when test="${task.grade==3}">#ffa500</c:when>
										 				<c:when test="${task.grade==2}">#00bfff</c:when>
										 				<c:when test="${task.grade==1}">#008000</c:when>
										 			</c:choose>
										 		</c:set>
									 			<tr>
									 				<td valign="middle" style="padding-left: 0px">
									 					<a href="javascript:void(0)" class="fa ${task.attentionState==0?'fa-star-o':'fa-star ws-star'}" title="${task.attentionState==0?'标识关注':'取消关注'}" onclick="changeAtten('003',${task.id},${task.attentionState},'${param.sid}',this)"></a>
									 				</td>
									 				<td valign="middle"><input type="checkbox" name="ids" value="${task.id}" ${(task.owner==userInfo.id)?'':'disabled="disabled"' } state="${task.state }"/> </td>
									 				<td valign="middle" style="text-align: center;">
									 					${ status.count}
									 				</td>
									 				<td valign="middle" style="text-align: center;color: ${gradeColor }">
									 					<c:choose>
									 						<c:when test="${task.state==3 || task.state==4}">
									 							--
									 						</c:when>
									 						<c:otherwise>
											 					<tags:viewDataDic type="grade" code="${task.grade}"></tags:viewDataDic>
									 						</c:otherwise>
									 					</c:choose>
									 				</td>
									 				<td valign="middle">
									 					<a href="javascript:void(0)" onclick="viewTask(${task.id});" class="${task.isRead==0?'noread':'' }" title="${task.taskName}">
									 					<tags:cutString num="33">${task.taskName}</tags:cutString>
									 					</a>
									 				</td>
									 				<td valign="middle">
									 					<c:if test="${not empty task.busId && task.busId>0 && task.busDelState==0}">
										 					<c:choose>
										 						<c:when test="${task.busType=='005'}">[项目]</c:when>
										 						<c:when test="${task.busType=='012'}">[客户]</c:when>
										 					</c:choose>
										 					<a href="javascript:void(0)" onclick="viewBus(${task.busId},'${task.busType}',${task.busState})" title="${task.busName}"> 
											 					<tags:cutString num="26">${task.busName}</tags:cutString>	
											 				</a>
										 				</c:if>
									 				</td>
									 				<td valign="middle">${task.dealTimeLimit}</td>
									 				<td valign="middle" style="text-align: center;">
									 					<c:choose>
									 						<c:when test="${task.state==3}">已挂起</c:when>
									 						<c:when test="${task.state==4}"><font style="color:#FF0000;">已完成</font></c:when>
									 						<c:otherwise>${task.taskprogressDescribe}</c:otherwise>
									 					</c:choose>
									 				</td>
									 				<td valign="middle">
									 					<c:choose>
									 						<c:when test="${task.state==3 || task.state==4}">
									 							--
									 						</c:when>
									 						<c:otherwise>
											 					<div class="ticket-user pull-left other-user-box">
																<img src="/downLoad/userImg/${task.comId}/${task.executor}?sid=${param.sid}"
																	title="${task.executorName}" class="user-avatar" />
																<i class="user-name">${task.executorName}</i>
																</div>
									 						</c:otherwise>
									 					</c:choose>
									 				</td>
									 				<td valign="middle">
									 					<div class="ticket-user pull-left other-user-box">
									 						<img src="/downLoad/userImg/${task.comId}/${task.owner}?sid=${param.sid}"
															title="${task.ownerName}" class="user-avatar"/>
														<i class="user-name">${task.ownerName}</i>
														</div>
									 				</td>
									 				<td valign="middle">
									 					${fn:substring(task.recordCreateTime,0,10) }
									 				</td>
									 			</tr>
									 		</c:forEach>
									 	</c:when>
									 	<c:otherwise>
									 		<tr>
									 			<td height="25" colspan="9" align="center"><h3>没有相关信息！</h3></td>
									 		</tr>
									 	</c:otherwise>
									 </c:choose>
										</tbody>
									</table>
									 <tags:pageBar url="/task/taskListPage"></tags:pageBar>
								</div>
							</section>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--nav-tabs style 2-->
		</div>
	</div>
</div>
</section>
</form>
<!--main content end-->
</section>
</body>
</html>
