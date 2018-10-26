<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<!-- 框架样式 -->
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		
		<script type="text/javascript">
		var EasyWin = {
				"sid" : "${param.sid}",
				"userInfo" : {
					"userId" : "${userInfo.id}",
					"userName" : "${userInfo.userName}",
					"comId" : "${userInfo.comId}",
					"orgName" : "${userInfo.orgName}",
					"isAdmin" : "${userInfo.admin}",
				},
				"homeFlag" : "${homeFlag}",
				"ifreamName" : "${param.ifreamName}"
			};
		
		var sid='${param.sid}';
		//关闭窗口
		function closeWin(){
			var winIndex = window.top.layer.getFrameIndex(window.name);
			closeWindow(winIndex);
		}
		
		$(function(){
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
			
		})
		var pDocument;
		var pWindow;
		var spState;
		//初始化信息
		function initSpFlowIdea(spStateparam,pDocumentParam,pWwindowParam){
			pDocument = pDocumentParam;
			pWindow = pWwindowParam;
			spState = spStateparam;
			if(spState ===0){
				$("#backStepSelect").parents("li").remove();
				$("#spIdea").html('[驳回]');
				$(".ps-layerTitle").html("审批终止");
			}else if(spState ===-1){
				$(".ps-layerTitle").html("审批回退");
				var params = {
	                    "instanceId": pWindow.FLOWINFO.instanceId,
	                    "sid": sid,
	                    "rnd": Math.random()
	                }
	                var optionDefault = $('<option value="0" selected>请选择回退步骤</option>')
	                $("#backStepSelect").append($(optionDefault));
	                getSelfJSON("/workFlow/ajaxListHistorySpStep", params, function (data) {
	                    if (data.status == 'y') {
	                        var listHistorySpStep = data.listHistorySpStep;
	                        $.each(listHistorySpStep, function (index, hisStep) {
	                            var option = $('<option value="' + hisStep.activitiTaskId + '">' + hisStep.stepName + '</option>')
	                            $("#backStepSelect").append($(option));
	                        })
	                    }
	                })
			}
		}
		
		//返回步骤信息
		function returnStepConfig(){
			//返回结果
			var result={};
			
			if(spState ===-1){
				var activitiTaskId = $("#backStepSelect").val();
                if (activitiTaskId == 0) {
                    layer.tips("请选择回退步骤", $("#backStepSelect"), {tips: 1});
                    return;
                }
                result.activitiTaskId = activitiTaskId;
			}
			var spIdea = $("#spIdea").val();
			if(pWindow.FLOWINFO.saveType == 'update' || pWindow.FLOWINFO.saveType == 'back' ){//审批意见必填
				if(!spIdea){
					layer.tips("请填写审批意见!", $("#spIdea"), {tips: 1});
					return null;
				}
			}
			if(spIdea){
				result.spIdea = spIdea;
			}
			
			
			var spFilesObj = $("#spFileRelate").find("select");
			var spFilesArray = new Array();
			$.each($(spFilesObj).find("option"),function(i,vo){
				var file = {"upfileId":$(vo).val(),"filename":$(vo).text()}
				spFilesArray.push(file);
			});
			result.spFlowUpfiles = spFilesArray;
			return result;
		}

		</script>
		<style type="text/css">
			.online-list{cursor: pointer;}
			input[type="radio"],input[type="checkbox"] {
			    top: 3px !important;
			    left:0px !important;
			    opacity: 1 !important;
			    width: 15px !important;
			    height: 15px !important;
			}
		</style>
	</head>
	<body>
	<div class="widget-header bordered-bottom bordered-red detailHead">
	     <i class="widget-icon fa fa-cog txt-red menu-icons"></i>
	     <span class="widget-caption txt-red menu-topic ps-layerTitle">审批意见填写</span>
	     <div class="widget-buttons">
	         <a id="titleCloseBtn"  href="javascript:void(0)" onclick="closeWin()" title="关闭">
	             <i class="fa fa-times"></i>
	         </a>
	     </div>
	 </div>
	 <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
		<div class="widget-body no-shadow">
						<ul class="tickets-list">
							<li class="ticket-item no-shadow ps-listline" style="clear:both">
							    <div class="pull-left gray ps-left-text" style="text-align: right;">
							    	<span style="color: red">*</span>回退步骤：
							    </div>
								<div class="ticket-user pull-left ps-right-box margin-left-5">
								   <select class="populate"  id="backStepSelect" style="cursor:auto;width: 200px">
										
								  </select>
								</div>               
                             </li>
                             <li class="clearfix ticket-item no-shadow autoHeight no-padding padding-top-5 
							padding-bottom-5" id="spIdeaDiv">
							    <div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
							    	<font color="red">*</font>审批意见：
							    </div>
								<div class="ticket-user pull-left ps-right-box margin-top-5 margin-left-5" style="height: auto;max-width: 380px;min-height: 30px" >
									<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" id="spIdea"
										style="min-height:50px;width: 380px;" rows="" cols=""></textarea>
										<div class="ws-plugs">
											<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('spIdea','${param.sid}');" title="常用意见"></a>
										</div>
								</div> 
								<div class="ps-clear"></div>              
                             </li>
                             <li class="clearfix ticket-item no-shadow autoHeight no-padding padding-top-5 
							padding-bottom-5" id="fileDiv">
							    <div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
							    	审批附件：
							    </div>
								<div class="ticket-user pull-left ps-right-box margin-top-5 " style="height: auto;max-width: 380px;min-height: 30px" >
									<div style="clear:both;width: 300px" class="wu-example">
										<!--用来存放文件信息-->
										<div id="thelistspFlowUpfiles_id" class="uploader-list" >
											
										</div>
										<div class="btns btn-sm">
											<div id="pickerspFlowUpfiles_id">选择文件</div>
										</div>
										<script type="text/javascript">
											loadWebUpfiles('spFlowUpfiles_id','${param.sid}','','pickerspFlowUpfiles_id','thelistspFlowUpfiles_id','filespFlowUpfiles_id')
										</script>
										<div style="position: relative; width: 350px; height: 90px;display: none">
											<div style="float: left; width: 250px" id="spFileRelate">
												<select  list="spFlowUpfiles" listkey="id" listvalue="fileName" id="spFlowUpfiles_id" name="spFlowUpfiles.id" 
													ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">
													
												 </select>
											</div>
										</div>
									</div>
								</div> 
								<div class="ps-clear"></div>              
                             </li>
                             <li class="clearfix ticket-item no-shadow autoHeight no-padding padding-top-5 
							padding-bottom-5" id="addTaskWayDiv" style="display:none;">
							    <div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
							    	审批转任务：
							    </div>
								<div class="ticket-user pull-left ps-right-box margin-top-5" style="height: auto;max-width: 380px;min-height: 30px" >
									<label class="padding-left-5" title="需审批转任务">
									 	<input type="checkbox" name="addTaskWay" value="003">
									 	<span class="text" style="color:inherit;">审批转任务</span>
								    </label>
								</div> 
								<div class="ps-clear"></div>              
                             </li>
						</ul>
				</div>
    
		</div>	
		
	</body>
</html>

