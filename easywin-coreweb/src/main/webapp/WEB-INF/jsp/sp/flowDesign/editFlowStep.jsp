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
	var sid="${param.sid}";//sid全局变量
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-60;
		$("#contentBody").css("height",height+"px");
		
		$("#stepName").keydown(function(event){	
			if(event.keyCode==13) {
				return false;
			}
		});
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			datatype:{
				"input":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(str){
						var count = str.replace(/[^\x00-\xff]/g,"**").length;
						var len = $(obj).attr("defaultLen");
						if(count%2==1){
							count = (count+1)/2;
						}else{
							count = count/2;
						}
						if(count>len){
							return "步骤名称太长";
						}else{
							return true;
						}
					}else{
						return false;
					}
				},
				"executorWay":function(gets,obj,curform,regxp){
					var executorWay  = $("input[name='executorWay']:checked").val();
					if($("#stepName").val() && "appointMan"==executorWay && $("#listExecutor_id option:selected").length==0){
						window.top.layer.msg("请选择\"审批人\"！",{icon:2});
						return false;
					}else{
						return true;
					}
				},
				"nextStepWay":function(gets,obj,curform,regxp){
					var nextStepWay  = $("input[name='nextStepWay']:checked").val();
					if($("#stepName").val() && $("input[name='nextStepIds']:checked").length==0){
						window.top.layer.msg("请选择\"下步步骤\"！",{icon:2});
						return false;
					}else if($("input[name='nextStepIds']:checked").length>1 && strIsNull(nextStepWay)){
						window.top.layer.msg("请选择\"下步扭转方式\"！",{icon:2});
						return false;
					}else if($("input[name='nextStepIds']:checked").length>1 && "branch"==nextStepWay && $("input[name='defaultStepId']:checked").length==0){
						window.top.layer.msg("请选择\"默认步骤\"！",{icon:2});
						return false;
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
		//步骤数决定下步扭转方式
		$("[name='nextStepIds']").click(function(){
			var steps  = $("input[name='nextStepIds']:checked").length;
			//下步步骤行
			if(steps>1){
				$("#nextStepWaySelectLi").css("display","block");
				defaultStepSelect();//构建默认步骤候选项
				defaultStepForSelectLiDis();//是否显示默认步骤
			}else{
				$("#nextStepWaySelectLi").css("display","none");//隐藏扭转方式li
				$("#defaultStepForSelectLi").css("display","none");//隐藏默认步骤li
				$("#defaultStepTableDiv").html("")//清空默认步骤html
			}
		});
		//步骤扭转方式选择
		$("[name='nextStepWay']").click(function(){
			defaultStepForSelectLiDis();//是否显示默认步骤
		});
		//办理人指定
		$("[name='executorWay']").click(function(){
			var executorWay  = $("input[name='executorWay']:checked").val();
			if("appointMan"==executorWay){
				$("#stepExecutorSelectLi").css("display","block");
			}else{
				$("#stepExecutorSelectLi").css("display","none");
			}
		});
		//表单授权点击事件定义
		$("#spStepFormControl").click(function(){
			formComponListForSelect("${spFlowModel.formKey}",${stepVo.flowId},${stepVo.id});//弹窗打开表单候选列表页面
		});
	})
$(document).ready(function(){
	$("#stepName").focus();
	$("#nextStepForSelectLi").css("height",(${fn:length(listNextStepForSelect)}*40)+"px");//根据候选步骤个数设置候选步骤li高度
	//在步骤编辑页面上初始化默认步骤
	initDefaultStepSelect(${nextStepForDefaultJson});
	//构建步骤审批人显示html
	if(!strIsNull(${executorJson})){
		var users = ${executorJson};
		  var img="";
		  if(users){
			  for (var i=0, l=users.length; i<l; i++) {
					//数据保持
					$("#listExecutor_id").append('<option selected="selected" value='+users[i].id+'>'+users[i].userName+'</option>');
					img = img + "<div class=\"online-list margin-top-5 margin-left-5 margin-bottom-5\" style=\"float:left\" id=\"user_img_listExecutor_id_"+users[i].id+"\" ondblclick=\"removeClickUser('listExecutor_id',"+users[i].id+")\" title=\"双击移除\">";
					img = img + "<img src=\"/downLoad/userImg/"+users[i].comId+"/"+users[i].id+"?sid=${param.sid}\" class=\"user-avatar\"/>"
					img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
					img = img + "</div>"
				}
			}else{
			  for (var i=0, l=users.length; i<l; i++) {
					img = img + "<div class=\"online-list\" style=\"float:left\">";
					img = img + "<img src=\"/downLoad/userImg/"+users[i].comId+"/"+users[i].id+"?sid=${param.sid}\" class=\"user-avatar\"/>"
					img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
					img = img + "</div>"
				}
			}
			$("#executor_div").html(img);
	}
});
</script>
</head>
<body>
<form class="subform" method="post" action="/flowDesign/editFlowStep">
<tags:token></tags:token>
<input type="hidden" name="activityMenu" value="${activityMenu}"/>
<input type="hidden" name="flowId" value="${stepVo.flowId}"/>
<input type="hidden" name="id" value="${stepVo.id}"/>
<!-- 隐藏已经授权的表单控件 -->
<c:choose>
	<c:when test="${ not empty stepVo.listFormCompon }">
		<c:forEach items="${stepVo.listFormCompon}" var="formCol">
			<input type="hidden" name="formComponIds" value="${formCol.formControlKey}"/>
		</c:forEach>
	</c:when>
</c:choose>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">编辑步骤</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="addFlow">
								<i class="fa fa-save"></i>更新
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
										<c:if test="${not empty pstepName}">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	上一步骤
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											   “<strong><span style="color:red;">${pstepName}</span></strong>”
											</div>               
                                         </li>
                                         </c:if>
                                         <li class="ticket-item no-shadow ps-listline" ${(not empty pstepName)?'style="clear:both"':''}>
										    <div class="pull-left gray ps-left-text">
										    	步骤名称
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<c:if test="${'exe' ne stepVo.stepType}">
													<input  name="stepName" type="hidden" value="${stepVo.stepName}"/>
												</c:if>
											  	<div>
													<input id="stepName" datatype="input,sn" defaultLen="12" name="stepName" nullmsg="名称不能空！" 
													class="colorpicker-default form-control" type="text" value="${stepVo.stepName}"
													onpropertychange="handleName()" onkeyup="handleName()"
													style="width: 200px;float: left"
													${('exe' eq stepVo.stepType)?'':'disabled="disabled"'}>
													<span id="msgTitle" style="float:left;width: auto;">(0/12)</span>
												</div>
												<span id="addFlowWarn" style="float:left;margin-left:2px;"></span>
											</div>
											<script> 
												//当状态改变的时候执行的函数 
												function handleName(){
													var value = $('#stepName').val();
													var len = charLength(value.replace(/\s+/g,""));
													if(len%2==1){
														len = (len+1)/2;
													}else{
														len = len/2;
													}
													if(len>12){
														$('#msgTitle').html("(<font color='red'>"+len+"</font>/12)");
													}else{
														$('#msgTitle').html("("+len+"/12)");
													}
												} 
												//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
												if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
													document.getElementById('stepName').onpropertychange=handleName 
												}else {//非ie浏览器，比如Firefox 
													document.getElementById('stepName').addEventListener("input",handleName,false); 
												} 
											</script>  
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both;${('exe' eq stepVo.stepType)?'':'display:none;'}">
										    <div class="pull-left gray ps-left-text">
										    	审批方式
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											   <tags:radioDataDic type="executorWay" name="executorWay" style="ws-radio"
													value="${(empty stepVo.executorWay)?'directLeader':stepVo.executorWay}"></tags:radioDataDic>
											</div>               
                                         </li>
                                         <li id="stepExecutorSelectLi" class="ticket-item no-shadow ps-listline" style="clear:both;
                                         	${('appointMan' eq stepVo.executorWay)?'':'display:none;'}">
										    <div class="pull-left gray ps-left-text">
										    	审批人
										    	<!-- <span style="color: red">*</span> -->
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											   <tags:userMore name="listExecutor.id"
												showName="userName" disDivKey="executor_div" 
												callBackStart="no"></tags:userMore>
												<!-- datatype="executorWay" -->
											</div>               
                                         </li>
                                         <li id="nextStepForSelectLi" class="ticket-item no-shadow ps-listline" style="clear:both;">
										    <div class="pull-left gray ps-left-text">
										    	下步步骤
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											   <c:choose>
											   	<c:when test="${not empty listNextStepForSelect}">
												   <table style="float:left;">
											   		<c:forEach var="step" items="${listNextStepForSelect}">
													   	<tr>
													   	<td>
													   		<label class="padding-left-5">
															 	<input type="checkbox" class="colored-blue" name="nextStepIds" 
															 	value="${step.id}" stepName="${step.stepName}" ${(step.isMine==1)?'checked="checked"':''}/>
															 	<span class="text" style="color:inherit;">${step.stepName}</span>
														    </label>
													   	</td>
													   	</tr>
											   		</c:forEach>
												   </table>
											   	</c:when>
											   </c:choose>
												<input type="hidden" datatype="nextStepWay" nullmsg="请选择下步步骤" style="float:left;">
											</div>               
                                         </li>
                                         <li id="nextStepWaySelectLi" class="ticket-item no-shadow ps-listline" style="clear:both;
                                         	display:${(empty stepVo.nextStepWay)?'none':'block'};">
										    <div class="pull-left gray ps-left-text">
										    	下步扭转方式
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											   <tags:radioDataDic
													type="stepWay" name="nextStepWay" style="ws-radio"
													value="${(empty stepVo.nextStepWay)?'single':stepVo.nextStepWay}"></tags:radioDataDic>
											</div>               
                                         </li>
                                         <li id="defaultStepForSelectLi" class="ticket-item no-shadow ps-listline" style="clear:both;
                                         	display:${('branch' eq stepVo.nextStepWay)?'block':'none'};">
										    <div class="pull-left gray ps-left-text">
										    	默认步骤
										    	<span style="color: red">*</span>
										    </div>
											<div id="defaultStepTableDiv" class="ticket-user pull-left ps-right-box">
											   <tags:radioDataDic
													type="defaultStep" name="defaultStep" style="ws-radio"
													value="0"></tags:radioDataDic>
											</div>               
                                         </li>
                                         <c:if test="${spFlowModel.flowModBusType eq '022'}">
	                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
											    <div class="pull-left gray ps-left-text">
											    	表单权限
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												   <button id="spStepFormControl" class="btn btn-info btn-primary btn-xs" type="button">
												   	${empty stepVo.listFormCompon?'表单授权':'已授权'}
												   </button>
												</div>               
	                                         </li>
	                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
											    <div class="pull-left gray ps-left-text">
											    	审批权限验证
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<label class="padding-top-5">
														<input class="checkbox-slider slider-icon colored-blue" type="checkbox" ${stepVo.spCheckCfg eq '1' ?"checked":"" } name="spCheckCfg" value="1" >
														<span class="text"></span>
													</label>
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
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
