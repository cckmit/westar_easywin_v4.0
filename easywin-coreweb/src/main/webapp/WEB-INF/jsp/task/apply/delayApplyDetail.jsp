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
		$("body").on("click","#agree",function(){//同意
			$(".subform [name='status']").val(1);
			var limitDate = $("#limitDate").val();
        	if(strIsNull(limitDate)){
        		layer.tips('请选择报延日期！', "#limitDate", {tips: 1});
        		return false;
        	}
        	var spOpinion = $("#spOpinion").val();
        	if(strIsNull(spOpinion)){
        		layer.tips('请填写审核意见！', "#spOpinion", {tips: 1});
        		return false;
        	}
        	var count = spOpinion.replace(/[^\x00-\xff]/g,"**").length;
			if(count>250){
				layer.tips('审核意见太长！', "#spOpinion", {tips: 1});
        		return false;
			}
			$(".subform").submit();
		});
		$("body").on("click","#refuse",function(){//不同意
			$(".subform [name='status']").val(0);
        	var spOpinion = $("#spOpinion").val();
        	if(strIsNull(spOpinion)){
        		layer.tips('请填写审核意见！', "#spOpinion", {tips: 1});
        		return false;
        	}
        	var count = spOpinion.replace(/[^\x00-\xff]/g,"**").length;
			if(count>250){
				layer.tips('审核意见太长！', "#spOpinion", {tips: 1});
        		return false;
			}
			$(".subform").submit();
		});
	});
</script>
</head>
<body >
<input type="hidden" id="subState" value="0">
<form class="subform" method="post" action="/task/updateDelayApply">
	<tags:token></tags:token>
	<input type="hidden" name="taskId" value="${delayApply.taskId}"/>
	<input type="hidden" name="fromUser" value="${delayApply.fromUser}"/>
	<input type="hidden" name="id" value="${delayApply.id}"/>
	<input type="hidden" name="sid" value="${param.sid}"/>
	<input type="hidden" name="status" value=""/>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget" style="margin-top: 0px;">
					<div
						class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px">任务报延审批</span>
						<div class="widget-buttons ps-toolsBtn">
							<c:if test="${delayApply.toUser eq userInfo.id and empty delayApply.status}">
								<a href="javascript:void(0)" class="green" id="agree">
									<i class="fa fa-check-square-o"></i>同意
								</a>
								<a href="javascript:void(0)" class="red" id="refuse">
									<i class="fa fa-times-circle-o"></i>不同意
								</a>
							</c:if>
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
			                <div class="widget no-header">
			                    <div class="widget-body bordered-radius">
			                        <div class="task-describe clearfix">
			                            <div class="tickets-container tickets-bg tickets-pd clearfix">
			                                <ul class="tickets-list clearfix">
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
			                                        <div class="pull-left task-left-text task-height">
			                                                                                                             申请人：
			                                        </div>
			                                        <div class="ticket-user pull-left ps-right-box other-user-box">
														<img class="user-avatar userImg" title="${delayApply.fromUserName}" 
															src="/downLoad/userImg/${delayApply.comId}/${delayApply.fromUser}"/>
														<i class="user-name">${delayApply.fromUserName}</i>
													</div>
			                                    </li>
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
			                                        <div class="pull-left task-left-text task-height">
			                                                                                                                    延期到<span style="color: red">*</span>：
			                                        </div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <input type="text" class="form-control" placeholder="延期时限" readonly="readonly" id="limitDate"
																	name="limitDate" onClick="WdatePicker({minDate:'%y-%M-{%d}'})" value="${delayApply.limitDate}"/>
														</div>
			                                        </div>
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
			                                                                                                   原因：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
				                                        ${delayApply.reason}
													</div>
			                                    </div>
			                                </li>
			                            </ul>
			                        </div>
			                    </div>
			                </div>
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
															style="font-size:10px;margin-top:8px;" taskId="${delayApply.taskId}">
															${delayApply.taskName}
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
														<img class="user-avatar userImg"  title="${delayApply.toUserName}" 
															src="/downLoad/userImg/${delayApply.comId }/${delayApply.toUser}"/>
														<i class="user-name">${delayApply.toUserName}</i>
													</div>
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
			                                                                                                   审核意见<span style="color: red">*</span>：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
				                                        <textarea class="form-control pull-left" id="spOpinion"
															name="spOpinion" rows="" cols=""
															style="width:600px;height: 150px;"></textarea>
													</div>
													<%--常用意见 --%>
													<div class="row">
														<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('spOpinion','${param.sid}');" title="常用意见"></a>
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
