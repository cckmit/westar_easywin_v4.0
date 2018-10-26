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
				validMsgV2(msg, o, cssctl);
			},
			datatype:{
				"whType":function(gets,obj,curform,regxp){
					if($("#whType").val()){
						return true;
					}else{
						return false;
					}
				},
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		//form表单提交
		$("#addGdzcMaintain").click(function(){
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
	<form class="subform" method="post" action="/gdzc/addMaintainRecord">
		<tags:token></tags:token>
		<input type="hidden" name="gdzcId" value="${gdzcId }" id="gdzcId">
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">固定资产维修记录添加</span>
							<div class="widget-buttons ps-toolsBtn">
								<a href="javascript:void(0)" class="blue" id="addGdzcMaintain"> <i class="fa fa-save"></i>添加
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
												<div class="pull-left gray ps-left-text padding-top-10">&nbsp;维修事由
												<span style="color: red">*</span></div>
												<div class="ticket-user pull-left ps-right-box" style="width: 200px;height: auto;">
													<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:55px;width:200px;" id="reason" name="maintainReason" dataType="s"  nullmsg="请填写维修事由!"  placeholder="维修事由……"></textarea>
												</div>
												<div class="ps-clear"></div>
											</li>
											<li class="ticket-item no-shadow ps-listline" style="clear:both">
												<div class="pull-left gray ps-left-text">
													&nbsp;维修类型
													<span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div datatype="whType" class=" pull-left ">
														<select class="populate" datatype="whType" id="whType" name="maintainType" style="cursor:auto;width: 200px">
															<optgroup label="选择维修类型"></optgroup>
															<c:choose>
																<c:when test="${not empty listWhType}">
																	<c:forEach items="${listWhType}" var="gdzcType" varStatus="status">
																		<option value="${gdzcType.id}">${gdzcType.typeName}</option>
																	</c:forEach>
																</c:when>
															</c:choose>
														</select>
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">&nbsp;委派人
												<span style="color: red">*</span></div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
													<tags:userOne datatype="s" name="executor" defaultInit="true" showValue="${userInfo.userName}" value="${userInfo.id}" uuid="${userInfo.smImgUuid}" filename="${userInfo.smImgName}"
														gender="${userInfo.gender}" onclick="true" showName="executorName" nullmsg="请选择委派人!"></tags:userOne>
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">&nbsp;维修日期起
												<span style="color: red">*</span></div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<input id="startDate" name="startDate" dataType="*"  class="colorpicker-default form-control" readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" nullmsg="请填写维修开始日期!" type="text" value="" style="width: 200px;float: left">
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">&nbsp;维修日期止</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<input id="endDate" name="endDate" class="colorpicker-default form-control" readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"  type="text" value="" style="width: 200px;float: left">
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow autoHeight ps-listline">
											<div class="pull-left gray ps-left-text padding-top-10">&nbsp;相关附件</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
													<tags:uploadMore name="listGdzcUpfiles.id" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
												</div>
											</div>
											<div class="ps-clear"></div>
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
