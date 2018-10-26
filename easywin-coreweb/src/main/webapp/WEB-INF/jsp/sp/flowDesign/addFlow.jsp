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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
    <script type="text/javascript" charset="utf-8" src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	var sid="${param.sid}";
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$("#flowName").keydown(function(event){	
			if(event.keyCode==13) {
				return false;
			}
		});
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			datatype:{
				"input":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(str){
						var count = str.replace(/[^\x00-\xff]/g,"**").length;
						var len = $(obj).attr("defaultLen");
						if(count>len){
							return "流程名称太长";
						}else{
							return true;
						}
					}else{
						return false;
					}
				},
				"formName":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(!str){
						var formKey = $("#formKey").val();
						var flowModBusType = $("#flowModBusType").val();
						if(flowModBusType && flowModBusType!='022'){
							return true;
						}else{
							return "请选择关联表单";
						}
					}else{
						return true;
					}
				}
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		//form表单提交
		$("#addFlow").click(function(){
			$(".subform").submit();
			//关闭当前页面
			//closeWindow();
		});
		//关联表单
		$("#formName").click(function(){
			formModListForSelect(-1);//弹窗打开表单候选列表页面
		});
		
		$('#flowModBusType').change(function () {
            if($(this).val() == '022'){
                $('#formReleLi').show();
            }else{
                $('#formReleLi').hide();
            }
        });
	})
$(document).ready(function(){
	$("#flowName").focus();
});
</script>
</head>
<body>
<form class="subform" method="post" action="/flowDesign/addFlow">
<tags:token></tags:token>
<input type="hidden" name="activityMenu" value="${activityMenu}"/>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">新建流程</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="addFlow">
								<i class="fa fa-save"></i>确认
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                     <div class="widget-body margin-top-30" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	流程名称
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div>
													<input id="flowName" datatype="input,sn" defaultLen="52" name="flowName" nullmsg="先给流程取个名！" 
													class="colorpicker-default form-control" type="text" value=""
													onpropertychange="handleName()" onkeyup="handleName()"
													style="width: 400px;float: left">
													<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
												</div>
												<span id="addFlowWarn" style="float:left;margin-left:2px;"></span>
											</div>
											<script> 
												//当状态改变的时候执行的函数 
												function handleName(){
													var value = $('#flowName').val();
													var len = charLength(value.replace(/\s+/g,""));
													if(len%2==1){
														len = (len+1)/2;
													}else{
														len = len/2;
													}
													if(len>26){
														$('#msgTitle').html("(<font color='red'>"+len+"</font>/26)");
													}else{
														$('#msgTitle').html("("+len+"/26)");
													}
												} 
												//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
												if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
													document.getElementById('flowName').onpropertychange=handleName 
												}else {//非ie浏览器，比如Firefox 
													document.getElementById('flowName').addEventListener("input",handleName,false); 
												} 
											</script>  
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	流程分类
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<select class="populate" name="spFlowTypeId" style="cursor:auto;width: 200px">
													<optgroup label="选择流程分类"></optgroup>
													<c:choose>
														<c:when test="${not empty listSpFlowType}">
							 								<c:forEach items="${listSpFlowType}" var="spFlowType" varStatus="status">
							 									<option value="${spFlowType.id}">${spFlowType.typeName}</option>
							 								</c:forEach>
														</c:when>
													</c:choose>
												</select>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	流程模块
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<select id="flowModBusType" class="populate" name="flowModBusType"
											  	 	style="cursor:auto;width: 200px">
					 								<option value="022" selected="selected">审批模块</option>
					 								<option value="046">会议模块</option>
					 								<option value="047">会议纪要</option>
												</select>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" id="formReleLi">
										    <div class="pull-left gray ps-left-text">
										    	关联表单<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div>
													<input id="formName" datatype="formName" readonly="readonly" placeholder="点击选取表单。"
													class="colorpicker-default form-control" type="text" value="">
													<input type="hidden" name="formKey" id="formKey"/>
												</div>
											</div>
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
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
