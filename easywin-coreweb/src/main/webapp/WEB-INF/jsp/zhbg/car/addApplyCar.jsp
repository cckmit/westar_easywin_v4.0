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
				validMsgV2(msg, o, cssctl);
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
		$("#addCarApply").click(function() {
			postUrl('/car/checkCarApply', {
				sid : '${param.sid}',
				carId : $("#carId").val(),
				startDate : $("#startDate").val(),
				endDate : $("#endDate").val(),
			}, function(data) {
				if (data.status == 'y') {
					formSub(); 
				}else{
					showNotification(2, "该申请已与其他申请冲突,请修改申请时间或者车辆！");
				}
			});
		});
	});
	//提交表单
	function formSub() {
		$(".subform").submit();
	}
	//车辆预约情况弹窗
	function carStatusPage(sid){
		window.top.layer.open({
			  type: 2,
			  title: false,
			  closeBtn:0,
			  area: ['800px', '400px'],
			  fix: false, //不固定
			  maxmin: false,
			  scrollbar:false,
			  content: ["/car/carStatusPage?sid="+sid,'no'],
			  cancel: function(){ 
			    //右上角关闭回调
			  },end:function(index){
			  }
			});
	};
</script>
</head>
<body>
	<form class="subform" method="post" action="/car/addCarApply">
		<tags:token></tags:token>
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">车辆申请</span>
							<div class="widget-buttons ps-toolsBtn">
								<a href="javascript:void(0)" class="blue" id="addCarApply"> <i class="fa fa-save"></i>申请
								</a>
							</div>
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
													<span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div datatype="carId" class=" pull-left ">
														<select class="populate" id="carId" name="carId" style="cursor:auto;width: 200px">
															<optgroup label="选择使用车辆"></optgroup>
															<c:choose>
																<c:when test="${not empty listCar}">
																	<c:forEach items="${listCar}" var="car" varStatus="status">
																		<option value="${car.id}">${car.carNum}</option>
																	</c:forEach>
																</c:when>
															</c:choose>
														</select>
													</div>
													<div  class=" pull-left ">
													<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-5  margin-left-5" onclick="carStatusPage('${param.sid }');">车辆预约情况</a>
												</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline" style="clear:both">
												<div class="pull-left gray ps-left-text">
													&nbsp;开始时间
													<span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<input type="hidden" id="nowDate" value="${nowDate }">
														<input class="colorpicker-default form-control" dataType="*" nullMsg="请输入车辆使用开始时间!" type="text" value="${nowDate}" id="startDate" name="startDate" readonly="readonly"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endDate\',{d:-0});}',minDate: '#F{$dp.$D(\'nowDate\',{d:-0});}',readOnly:true})" style="width: 200px;float: left">
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													&nbsp;结束时间
													<span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<input class="colorpicker-default form-control" type="text" value="${nowDate }" id="endDate" name="endDate" readonly="readonly"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}',readOnly:true})" style="width: 200px;float: left">
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													&nbsp;目的地
													<span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<input class="colorpicker-default form-control" dataType="*" nullMsg="请输入本次目的地!" type="text" value="" id="destination" name="destination"  style="width: 200px;float: left">
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													&nbsp;预计里程(km)
													<span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<input class="colorpicker-default form-control" type="text" dataType="n" nullMsg="请输入本次行程预计里程!" value="" id="predictJourney" name="predictJourney" style="width: 200px;float: left">
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow autoHeight no-padding">
												<div class="pull-left gray ps-left-text padding-top-10">
													&nbsp;申请事由
													<span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
													<div class="pull-left">
														<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:100px;width:400px;float:left" id="reason" name="reason" dataType="s" nullmsg="请填写本次车辆使用事由!"
															placeholder="申请事由"></textarea>
													</div>
												</div>
												<div class="ps-clear"></div>
											</li>
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
