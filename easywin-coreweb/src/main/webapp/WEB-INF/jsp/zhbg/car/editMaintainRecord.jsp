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
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$("#taskName").keydown(function(event){	
			if(event.keyCode==13) {
				return false;
			}
		});
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
		//form表单提交
		$("#addCarMaintain").click(function(){
			formSub();
			//关闭当前页面
			//closeWindow();
		});
	});
	//提交表单
	function formSub(){
		$(".subform").submit();
	}
</script>
</head>
<body>
	<form class="subform" method="post" action="/car/updateMaintainRecord">
		<tags:token></tags:token>
		<input type="hidden" name="id" value="${maintainRecord.id }">
		<input type="hidden" name="carId" value="${maintainRecord.carId }">
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">车辆维修记录修改</span>
							<div class="widget-buttons ps-toolsBtn">
								<a href="javascript:void(0)" class="blue" id="addCarMaintain"> <i class="fa fa-save"></i>保存
								</a>
							</div>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭"> <i class="fa fa-times"></i>
								</a>
							</div>
						</div>
						<!--Widget Header-->
						<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
							<div class="widget radius-bordered " style="clear:both">
								<div class="widget-body no-shadow">
									<div class="tickets-container bg-white">
										<ul class="tickets-list">
											<li class="ticket-item no-shadow autoHeight no-padding">
												<div class="pull-left gray ps-left-text padding-top-10">&nbsp;维修事由<span style="color: red">*</span></div>
												<div class="ticket-user pull-left ps-right-box" style="width: 200px;height: auto;">
													<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:55px;width:200px;" 
													 id="reason" name="reason" dataType="s"  nullmsg="请填写维修事由!" >${maintainRecord.reason}</textarea>
												</div>
												<div class="ps-clear"></div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">&nbsp;委派人<span style="color: red">*</span></div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<tags:userOne datatype="s" name="executor" defaultInit="true"
														 showValue="${maintainRecord.executorName}" value="${maintainRecord.executor}" uuid="${maintainRecord.userUuid}"
														 filename="" gender="${maintainRecord.gender}" onclick="true"
														 nullmsg="请选择下一步执行人!" showName="executorName"></tags:userOne> 
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">&nbsp;维修日期起<span style="color: red">*</span></div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<input id="startDate" dataType="*"  name="startDate" class="colorpicker-default form-control" readonly="readonly"
														  value="${maintainRecord.startDate}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" nullmsg="请填写维修开始日期!" type="text" value="" style="width: 200px;float: left">
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">&nbsp;维修日期止</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<input id="endDate" name="endDate" class="colorpicker-default form-control" readonly="readonly" 
														 value="${maintainRecord.endDate}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"  type="text" value="" style="width: 200px;float: left">
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">&nbsp;维修完成</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
													<label class="margin-right-10 no-margin-bottom">
													<input type="radio" checked="checked" name="maintainState" value="1">
													<span class="text">是</span>
												</label>
												<label class="margin-right-10 no-margin-bottom">
													<input type="radio"  name="maintainState" value="0">
													<span class="text">否</span>
												</label>
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">&nbsp;维修价格</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<input name="maintainPrice" class="colorpicker-default form-control" type="text" dataType = "/^\s*$/|/^(([0-9]+)|([0-9]+\.[0-9]{1,2}))$/" errorMsg = "请填入正确的数字！" value="${maintainRecord.maintainPrice}"  style="width: 200px;float: left">
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
	</form>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
