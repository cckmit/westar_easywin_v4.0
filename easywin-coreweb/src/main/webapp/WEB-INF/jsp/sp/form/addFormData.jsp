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

<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/easywin.ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/font-icons.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/colpick.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-select.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-view.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-typeahead.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/org.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/plugins.min.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/easywin.min1.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/easywin.min2.css" />
<script type="text/javascript" charset="utf-8" src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script src="/static/plugins/form/js/libs.js"></script>
<script src="/static/plugins/form/js/plugins.js"></script>

<script type="text/javascript" src="/static/plugins/form/js/colpick.js"></script>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
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
	}else{
		var  winWidth= openWindowDoc.body.scrollWidth-800
		$("#layui-layer"+winIndex,openWindowDoc).css({"width":"800px","left":winWidth+"px"})
		$(ts).html('<i class="fa  fa-arrows-alt"></i>最大化')
		$("#maxMin").val('0')
	}
	
}

//autoCompleteCallBack回调对象标识
var autoCompleteCallBackVar =null;
var regex = /['|<|>|"]+/;
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});

</script>
<style type="text/css">
input[type="radio"],input[type="checkbox"] {
    top: 3px !important;
    left:1px !important;
    opacity: 1 !important;
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
						
                        <span class="widget-caption themeprimary" style="font-size: 20px;width:35%;overflow:hidden;text-overflow: ellipsis;white-space: nowrap">
                        	${spFlowInstance.formModName}
                        </span>
                        <div class="widget-buttons ps-toolsBtn" id="spBtnDiv">
                        	<input type="hidden" id="attentionState" value="0">
                        	<input type="hidden" id="maxMin" value="0">
                        	
                        	<a href="javascript:void(0)" class="blue" id="submitFreeForm">
								<i class="fa fa-plus-square-o"></i>提交
							</a>
							<a href="javascript:void(0)" class="blue" id="saveFreeForm">
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
                     <div class="form-preview module-detail-view writeform-content margin-top-40" 
                     id="contentBody" style="overflow: hidden;overflow-y:scroll;width: 100%">
                     <div id="formInfo"></div>
                     
                     
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
						<div id="form-info-div" class="detail-block margin-top-20" style="width: 95%;">
							<div class="wform-title">
								<input id="form-name" class="textinput-b title" name="" placeholder="" value="${spFlowInstance.flowName}"
								v-message-name="上报名称" v-maxlength="100" v-required="true">
							</div>
						      
							<div id="applicants" class="wform-post-info clearfix">
						      	<div class="item">
          							<label>填写人:</label>
          							<div class="control ellipsis">
           								<a class="usercard-toggle ellipsis  username applyUser_js" userid="${usreInfo.userId }">${userInfo.userName }</a>
          							</div>
         						</div>
         						<div class="item">
          							<label>部门:</label>
          							<div class="control ellipsis">
           								<a class="department ellipsis applyDepa_js j_dept">${userInfo.depName }</a>
          							</div>
         						</div>
         						<div class="item">
          							<label>日期:</label>
          							<div class="control">
           								<a class="j_date ellipsis department applyDepa_js">${fn:substring(spFlowInstance.recordCreateTime,0,10) }</a>
          							</div>
        	 					</div>
							</div>
						</div>
						<div class="js-biaoge-form form-border-view form-view form-view_js" 
						id="formpreview" style="display: none;width: 95%;margin-left: 18px">
								   
						</div>
							<div class="detail-block" style="width: 95%;margin-bottom: 50px;display: none">
								<div id="spRelate" class="wform-post-info clearfix" style="display: none">
									<div class="item" style="width: 100%;min-height: 48px">
	          							<label>审批关联:</label>
	          							<div class="control ellipsis">
	           								<select class="populate" id="busType" name="busType">
				 								<option value="0">选择关联模块</option>
				 								<option value="003">任务模块</option>
				 								<option value="005">项目模块</option>
				 								<option value="012">客户模块</option>
											</select>
	          							</div>
	         						</div>
									<div class="item" style="width: 60%;display: none;min-height: 48px">
	          							<label>模块名称:</label>
	          							<div class="control ellipsis">
	          									<span>
		       										<a class="j_date department applyDepa_js busNameShow"></a>
		       										<a class="relateCloseBtn" title="删除" style="float: none;display: none">×</a>
	          									</span>
	          									<span style="padding-left: 15px">
		       										<a class="btn engine-search" id="relateBtn" style="clear: both">
		       											<i class="fa fa-plus js_search"></i>
		       										</a>
	          									</span>
	          							</div>
	         						</div>
									<div class="item" style="width: 40%;display: none;min-height: 48px">
	          							<label>项目阶段:</label>
	          							<div class="control ellipsis">
	           								<a class="j_date department applyDepa_js itemStageNameShow"></a>
	       									<a class="btn engine-search" id="itemStageBtn">
	       										<i class="fa fa-plus js_search"></i>
	       									</a>
	          							</div>
	         						</div>
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
	var TEAMS={
			"currentUser":{"userId":"${userInfo.id}",
				"id":"${userInfo.id}",
				"username":"${userInfo.userName}",
				"department":{"name":"${userInfo.depName}","id":"${userInfo.depId}"}
			},
			"currentTenant":{"tenantKey":"t5y8pa3o0l"},
			"spFlow":{"creator":${spFlowInstance.creator},"preStageItemId":${spFlowInstance.stagedItemId},
				"busId":${spFlowInstance.busId},"busType":"${spFlowInstance.busType}","preFlowName":"${spFlowInstance.flowName}",
				"busName":"${spFlowInstance.busName}","stagedItemId":${spFlowInstance.stagedItemId},
				"stagedItemName":"${spFlowInstance.stagedItemName}"}
	}
	var  formKey = "${spFlowInstance.formKey}";
	var FLOWINFO={"formKey":"${spFlowInstance.formKey}","instanceId":"${spFlowInstance.id}","layoutId":"${spFlowInstance.layoutId}","flowId":"${spFlowInstance.flowId}",
			"creatorName":"${userInfo.userName}","creatorDepName":"${userInfo.depName}",
			"saveType":"add"};
	</script>
	<script type="text/javascript">
		if(!strIsNull(formKey)){
			seajs.config({base:"/static/plugins/form/js",preload:["workFlowForm.js?v="+Math.random()],charset:"utf-8",debug:!1}),seajs.use("easywin/workFlow/formView");  
		}
	</script>
</body>
</html>