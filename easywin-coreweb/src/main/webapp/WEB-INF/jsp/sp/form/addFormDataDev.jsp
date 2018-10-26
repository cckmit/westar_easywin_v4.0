<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<!-- 框架样式 -->

<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>

		
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8" src="/static/plugins/ueditor/form.layout.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" charset="utf-8" src="/static/plugins/form/js/workFlowFormDev.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" charset="utf-8" src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>

<script type="text/javascript">
var sid="${param.sid}";
//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
//注入父页面信息
function setWindow(winDoc,win){
	openWindowDoc = winDoc;
	openWindow = win;
	openPageTag = openWindow.pageTag;
	openTabIndex = openWindow.tabIndex;
}
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
//最大最小化
function maxMinWin(ts){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	if($("#maxMin").val()==0){
		var  winWidth= openWindowDoc.body.scrollWidth
		$("#layui-layer"+winIndex,openWindowDoc).css({"width":winWidth+"px","left":"0px"})
		$("#layui-layer"+winIndex,openWindowDoc).css("z-index","9999")
		$(ts).html('<i class="fa  fa-arrows-alt"></i>最小化')
		$("#maxMin").val('1')
		
		$("#divScope").attr("style","clear:both;margin: 20px 6% 20px 23%;")
		$("#divScope").prev().attr("style","clear:both;margin: 0px 6% 0px 23%;")
	}else{
		var  winWidth= openWindowDoc.body.scrollWidth-800
		$("#layui-layer"+winIndex,openWindowDoc).css({"width":"800px","left":winWidth+"px"})
		$(ts).html('<i class="fa  fa-arrows-alt"></i>最大化')
		$("#maxMin").val('0')
		$("#divScope").attr("style","")
		$("#divScope").prev().attr("style","")
	}
	
}

//autoCompleteCallBack回调对象标识
var autoCompleteCallBackVar =null;
var regex = /['|<|>|"]+/;
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	$(document).on('input propertychange', 'textarea', function() {
		$(this).off("paste cut keydown keyup focus blur")
		.bind("paste cut keydown keyup focus blur", function() {
			var height, style = this.style;
			this.style.height = '98%';
			if (this.scrollHeight > 50) {
				height = this.scrollHeight;
				style.overflowY = 'hidden';
				style.height = height + 'px';
			}else{
				style.overflowY = 'hidden';
			}
		});
	});
});




</script>
<style type="text/css">
input[type="radio"],input[type="checkbox"] {
    opacity: 0 !important;
    width: 15px !important;
    height: 15px !important;
}
.webuploader-pick{
	height: 30px
}
.alert{
	border-color: #fbfbfb !important;
	background: #fbfbfb !important;
	color: #262626 !important;
}
texearea{
	resize: none !important;
	border: 1px solid #DDD !important;
}
input[type="text"] {
	border: 1px solid #DDD !important;
}
.subTableHead .tdItem,.subTableBody .tdItem,.subTableTail .tdItem{
	display:table-cell;
	min-height:30px;
	border-left: 1px solid #000;
	border-bottom:1px solid #000;
	text-align:center;
	vertical-align: middle;
}
.subTableBody .tdItem{
	padding: 5px 5px
}

.subTableHead .tdItem:FIRST-CHILD,.subTableBody .tdItem:FIRST-CHILD,.subTableTail .tdItem{
	border-left:0;
}
p{
	margin: 5px 0px;
}
input,textarea{
	margin:3px 0 !important;
}
input[type="text"] {
	border: 1px solid #DDD !important;
	height:28px !important;
	min-width:30px;
	padding:0;
}
.loanApply2View{
	cursor:pointer;
}
.tempCrm,.tempItem{
	display:inline-block;
	margin-top:3px;
	border-radius: 2px!important;
    background-clip: padding-box!important;
}
.inlineblock{
	display:inline-block;
}
</style>
</head>
<body>
<form id="subform" class="subform" method="post">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                		<a href="javascript:void(0)" class="widget-caption blue padding-right-5"
	              				 describe="0" iconSize="sizeMd" onclick="setAtten(this)">
							<i class="fa fa-star-o"></i>
						</a>
                        <span class="widget-caption themeprimary" style="font-size: 20px;width:35%;overflow:hidden;text-overflow: ellipsis;white-space: nowrap">${spFlowInstance.formModName}</span>
                        
                        <div class="widget-buttons ps-toolsBtn" id="spBtnDiv">
                        	<input type="hidden" id="attentionState" value="0">
                        	<input type="hidden" id="maxMin" value="0">
                        	
                        	<a href="javascript:void(0)" class="blue" id="addFormData">
								<i class="fa fa-plus-square-o"></i>提交
							</a>
							<a href="javascript:void(0)" class="blue" id="saveFormData">
								<i class="fa fa-save"></i>保存
							</a>
							<a href="javascript:void(0)" class="blue" onclick="maxMinWin(this)">
								<i class="fa  fa-arrows-alt"></i> 最大化
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin();" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="form-preview module-detail-view writeform-content margin-top-40 bg-white" 
                     id="contentBody" style="overflow: hidden;overflow-y:scroll;width: 100%">
                     <div class="wform-title bg-white margin-top-10 margin-bottom-20">
							<span class="clear-fix">标题：</span> 
							<input id="form-name" type="text" class="clear-fix" style="width: 550px;padding: 5px 5px"value="${spFlowInstance.flowName}"
							v-message-name="上报名称" v-maxlength="200">
					</div>
                     <div id="divScope">
                     	<div id="formInfo" class="padding-left-10 padding-top-20"></div>
						<div id="sp-steps-div" class="detail-block margin-top-20" style="width:770px;display:none;">
							<p style="background-color:#e1e1e1;height:30px;"><span style="font-weight:bold;font-size:16px;">审批步骤</span></p>
							<table class="table table-striped table-hover" id="spStepTable">
								<thead>
									<tr role="row">
										<th style="width:8%;">序号</th>
										<th>审批配置</th>
										<th style="width:25%;">操作</th>
									</tr>
								</thead>
								<tbody>
									<tr class="stepTr">
										<td>1</td>
										<td class="other-user-box">
											<span class="pull-left">审批人：</span>
											<input type="hidden" name="spUser" id="spUser1" value="">
											<div id="userOneImgDivspUser1" class="ticket-user pull-left">
												<img id="userOneImgspUser1" style="display:none;float:left" class="user-avatar"  
													onclick="userOne('spUser1','','','${param.sid }','no');" 
													src='' title=''/>
												<span class="user-name" style="font-size:10px;" id="userOneName_spUser1"></span>
											</div>
											<a href="javascript:void(0);" class="fa fa-user selectUser" style="padding: 0px 10px;float: left;
											margin-top:4px;font-size:24px;" title="人员单选" 
											onclick="userOne('spUser1','','','${param.sid }','no');"></a>
										 </td>
										<td>
											<a href="javascript:void(0);" onclick="addSpFlowStep(this);">添加下一步</a>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						<!--固定表头-->
						<div id="form-info-div" class="detail-block padding-left-10 bg-white" style="width:770px;">
						</div>
						
						<div class="js-biaoge-form form-border-view form-view form-view_js" 
						id="formpreview" style="display: none;width: 95%;margin-left: 18px">
						</div>
                     </div>
						
					</div>
					
				</div>
			</div>
		</div>
	</form>
	<script type="text/javascript">
	var sid='${param.sid}';
	var formTag = 'addForm';
	var EasyWin={
			"userInfo":{
				"id":"${userInfo.id}",
				"username":"${userInfo.userName}",
				"department":{"name":"${userInfo.depName}","id":"${userInfo.depId}"}
			},
			"nowTimeLong":"${nowTimeLong}","sid":sid,
			"spFlow":{"creator":${spFlowInstance.creator},"preStageItemId":${spFlowInstance.stagedItemId},
				"busId":"${spFlowInstance.busId}","busType":"${spFlowInstance.busType}","preFlowName":"${spFlowInstance.flowName}",
				"busName":"${spFlowInstance.busName}","stagedItemId":${spFlowInstance.stagedItemId},
				"stagedItemName":"${spFlowInstance.stagedItemName}"}
	}
	var  formKey = "${spFlowInstance.formKey}";
	var FLOWINFO={"formKey":"${spFlowInstance.formKey}","instanceId":"${spFlowInstance.id}",
			"creatorName":"${userInfo.userName}","creatorDepName":"${userInfo.depName}",
			"layoutId":"${spFlowInstance.layoutId}","flowId":"${spFlowInstance.flowId}","saveType":"add"};
	</script>
	
</body>
</html>