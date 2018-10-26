<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/zhbgJs/carJs/car.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
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
				validMsg(msg, o, cssctl);
			},
			datatype : {
				"carId" : function(gets, obj, curform, regxp) {
					if ($("#carId").val()) {
						return true;
					} else {
						return "请选择车辆！";
					}
				},
			},
			callback : function(form) {
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		//form表单提交
		$("#agree").click(function() {
			postUrl('/car/checkCarApply', {
				sid : '${param.sid}',
				carId : '${carApply.carId }',
				startDate : '${carApply.startDate }',
				endDate : '${carApply.endDate }',
			}, function(data) {
				if (data.status == 'y') {
					$("#state").val(1);
					formSub(); 
				}else{
					showNotification(2, "该申请已与其他申请冲突,,无法确认申请！");
				}
			});
		});
		$("#reject").click(function() {
			window.top.layer.prompt({
				  formType: 2,
				  area:'400px',
				  closeBtn:0,
				  move: false,
				  title: '拒绝车辆申请事由描述'
				}, function(reason, index, elem){
					if(reason){
						$("#voteReason").val(reason);
						$("#state").val(3);
						formSub();
					}
				})
		});
	});
	//提交表单
	function formSub() {
		$(".subform").submit();
	}
	
</script>
</head>
<body>
	<form class="subform" method="post" action="/car/doCarApply">
		<tags:token></tags:token>
		<input type="hidden" name="id" id ="id" value="${carApply.id }" />
		<input type="hidden" name="state" id = "state"/>
		<input type="hidden" name="voteReason" id = "voteReason"/>
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">车辆申请查看</span>
							<c:if test="${isModAdmin && carApply.state eq 0}">
								<div class="widget-buttons ps-toolsBtn">
									<a href="javascript:void(0)" class="green" id="agree">
								<i class="fa fa-check-square-o"></i>同意
							</a>
									<a href="javascript:void(0)" class="red" id="reject">
								<i class="fa fa-circle-o"></i>不同意
							</a>
								</div>
							</c:if>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭"> <i class="fa fa-times"></i>
								</a>
							</div>
						</div>
						<!--Widget Header-->
						<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
							<div class="widget radius-bordered">
								<div class="widget-header bg-bluegray no-shadow">
									<span class="widget-caption blue">基础信息</span>
									<div class="widget-buttons btn-div-full">
										<a class="ps-point btn-a-full" data-toggle="collapse"> <i class="fa fa-minus blue"></i>
										</a>
									</div>
								</div>
								<div class="widget-body no-shadow">
									<div class="tickets-container bg-white">
										<ul class="tickets-list">
											<li class="ticket-item no-shadow ps-listline" style="clear:both">
												<div class="pull-left gray ps-left-text">
													&nbsp;车辆
													
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div  class=" pull-left ">
													${carApply.carName }(${carApply.carNum })
													</div>
													<c:if test="${carApply.state eq 0}">
													<div  class=" pull-left ">
													<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-5  margin-left-5" onclick="carStatusPage('${param.sid }');">车辆预约情况</a>
													</div>
												</c:if>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline" style="clear:both">
												<div class="pull-left gray ps-left-text">
													&nbsp;开始时间
													
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
													${carApply.startDate }
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													&nbsp;结束时间
													
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
													${carApply.endDate }
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													&nbsp;目的地
													
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
													${carApply.destination }
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													&nbsp;预计里程(km)
													
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
													${carApply.predictJourney }
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow autoHeight no-padding">
												<div class="pull-left gray ps-left-text padding-top-10">
													&nbsp;申请事由
													
												</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
													<div class="pull-left">
														<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10"  readonly="readonly" style="height:100px;width:400px;float:left" id="reason" name="reason" dataType="s" nullmsg="请填写本次车辆使用事由!"
															placeholder="申请事由">${carApply.reason }</textarea>
													</div>
												</div>
												<div class="ps-clear"></div>
											</li>
											<c:if test="${carApply.state eq 3 }">
											<li class="ticket-item no-shadow autoHeight no-padding">
												<div class="pull-left gray ps-left-text padding-top-10">
													&nbsp;拒绝原因
													
												</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
													<div class="pull-left">
														<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:100px;width:400px;float:left" >${carApply.voteReason}</textarea>
													</div>
												</div>
												<div class="ps-clear"></div>
											</li>
											
											</c:if>
											<c:if test="${carApply.state eq 2 }">
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													&nbsp;实际里程(km)
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
													${carApply.realJourney }
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													&nbsp;实际耗油量(L)
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
													${carApply.oilConsumption }
													</div>
												</div>
											</li>
											
											</c:if>
										</ul>
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
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
