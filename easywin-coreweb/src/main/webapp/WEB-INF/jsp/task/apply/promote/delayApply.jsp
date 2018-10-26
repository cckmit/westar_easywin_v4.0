<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">

<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link href="/static/assets/css/task.css" rel="stylesheet" type="text/css">
<script src="/static/js/taskJs/taskCenter.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	var sid="${param.sid}";
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function(){
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
	});
	//表单提交
	function formSub(){
		if($("#subState").val()==1){
			return false;
		}
		var flag = false;
		$(".subform").ajaxSubmit({
		        type:"post",
		        url:"/task/delayApply?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        	var limitDate = $("#limitDate").val();
		        	if(strIsNull(limitDate)){
		        		layer.tips('请选择报延日期！', "#limitDate", {tips: 1});
		        		return false;
		        	}
		        	var reason = $("#reason").val();
		        	if(strIsNull(reason)){
		        		layer.tips('请填写报延原因！', "#reason", {tips: 1});
		        		return false;
		        	}
		        	var count = reason.replace(/[^\x00-\xff]/g,"**").length;
					if(count>250){
						layer.tips('报延原因太长！', "#reason", {tips: 1});
		        		return false;
					}
		        	$("#subState").val(1);
		        	
				}, 
		        success:function(data){
			        if('y'==data.status){
			        	flag = true;
			        }else{
		        		showNotification(2,data.info);
			        }
		        },error:function(XmlHttpRequest,textStatus,errorThrown){
		        	showNotification(2,"系统错误，请联系管理人员");
		        }
		 });
		$("#subState").val(0)
		return flag;
	}
</script>
</head>
<body >
<input type="hidden" id="subState" value="0">
<form class="subform" method="post">
	<tags:token></tags:token>
	<input type="hidden" name="taskVersion" value="${task.version}">
	<input type="hidden" name="taskId" value="${task.id}"/>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget" style="margin-top: 0px;">
					<div
						class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px">任务报延</span>
						<div class="widget-buttons ps-toolsBtn">
							&nbsp;
						</div>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i> 
							</a>
						</div>
					</div>
					<!--Widget Header-->
					<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
					<div class="widget margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
			            <div class="widget-body">
			                <div class="widget no-header ">
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list">
			                                <li class="ticket-item no-shadow clearfix ticket-normal">
			                                    <div class="pull-left task-left-text task-height">
			                                                                                                   报延任务：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
				                                        <a href="javascript:void(0);" class="pull-left taskView"
															style="font-size:10px;margin-top:8px;" taskId="${task.id}">
															${task.taskName}
														</a>
			                                        </div>
			                                    </div>
			                                </li>
			                            </ul>
			                        </div>
			                    </div>
			                </div>
			                <div class="widget no-header">
			                    <div class="widget-body bordered-radius">
			                        <div class="task-describe clearfix">
			                            <div class="tickets-container tickets-bg tickets-pd clearfix">
			                                <ul class="tickets-list clearfix">
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
			                                        <div class="pull-left task-left-text task-height">
			                                                                                                              审核人：
			                                        </div>
			                                        <div class="ticket-user pull-left ps-right-box other-user-box">
														<img class="user-avatar userImg" title="${task.ownerName}" 
														src="/downLoad/userImg/${task.comId }/${task.owner}"/>
														<i class="user-name">${task.ownerName}</i>
													</div>
			                                    </li>
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
			                                        <div class="pull-left task-left-text task-height">
			                                                                                                                    延期<span style="color: red">*</span>：
			                                        </div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
			                                        		<input type="text" class="form-control" placeholder="延期小时数" 
				                                            	datatype="ff,pff" id="limitDate" name="limitDate" />
														</div>
			                                        </div>
			                                        <span style="line-height: 30px">小时</span>
			                                    </li>
			                                </ul>
			                            </div>
			                        </div>
			                    </div>
			                </div>
			                <div class="widget no-header ">
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list">
			                                <li class="ticket-item no-shadow clearfix ticket-normal">
			                                    <div class="pull-left task-left-text">
			                                                                                                   原因<span style="color: red">*</span>：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
				                                        <textarea class="form-control pull-left" id="reason"
															name="reason" rows="" cols=""
															style="width:600px;height: 150px;"></textarea>
													</div>
			                                    </div>
			                                </li>
			                            </ul>
			                        </div>
			                    </div>
			                </div>
			            </div>
			        </div>
				</div>
			</div>
		</div>
	</div>
</form>
</body>
</html>
