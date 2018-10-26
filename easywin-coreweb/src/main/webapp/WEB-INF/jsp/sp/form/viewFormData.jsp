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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<!-- 框架样式 -->

<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/easywin.ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/font-icons.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/colpick.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-select.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-view.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-typeahead.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/org.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/plugins.min.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/easywin.min1.css" />
<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/easywin.min2.css" />

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
	
	var width = $(window).width();
	if(width>1000){
		var  winWidth= openWindowDoc.body.scrollWidth
		$("#layui-layer"+openTabIndex,openWindowDoc).css({"width":winWidth+"px","left":"0px"})
		$("a[data-optBtn='0']").html('<i class="fa  fa-arrows-alt"></i>最小化')
		$("#maxMin").val('1');
		$("#spDetailDiv").parent().attr("style","clear:both;margin: 20px 6% 20px 23%;")
	}
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
		
		$(openWindowDoc).find("#sidebar").css( "zIndex",0);
		
		var  winWidth= openWindowDoc.body.scrollWidth
		$("#layui-layer"+winIndex,openWindowDoc).css({"width":winWidth+"px","left":"0px"})
		$(ts).html('<i class="fa  fa-arrows-alt"></i>最小化')
		$("#maxMin").val('1');
		
		$("#spDetailDiv").parent().attr("style","clear:both;margin: 20px 6% 20px 23%;")
	}else{
		$(openWindowDoc).find("#sidebar").css( "zIndex",1000);
		
		var  winWidth= openWindowDoc.body.scrollWidth-800
		$("#layui-layer"+winIndex,openWindowDoc).css({"width":"800px","left":winWidth+"px"})
		$(ts).html('<i class="fa  fa-arrows-alt"></i>最大化')
		$("#maxMin").val('0');
		$("#spDetailDiv").parent().attr("style","")
	}
	
}
//autoCompleteCallBack回调对象标识
var autoCompleteCallBackVar =null;
var regex = /['|<|>|"]+/;
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//弹出会签信息
	alertHuiQianInfo();
	
	//表单详情
	$("#spDetail").click(function(){
		$("#spDetailDiv").show();
		$("#otherSpAttrIframe").hide();
		$("a[data-optBtn='1']").show();
		
		$(this).parent().find("li").removeAttr("class");
		$("#spDetail").attr("class","active");
	});
	//审批文档
	$("#spUpfileMenuLi").click(function(){
		$("#otherSpAttrIframe").show();
		$("#spDetailDiv").hide();
		$("a[data-optBtn='1']").show();
		
		$(this).parent().find("li").removeAttr("class");
		$("#spUpfileMenuLi").attr("class","active");
		$("#otherSpAttrIframe").attr("src","/workFlow/listSpFiles?sid=${param.sid}&instanceId=${spFlowInstance.id}");
	});
	//审批任务
	$("#relateTask").click(function(){
		
		$("#otherSpAttrIframe").show();
		$("#spDetailDiv").hide();
		$("a[data-optBtn='1']").show();
		
		$(this).parent().find("li").removeAttr("class");
		$("#relateTask").attr("class","active");
		$("#otherSpAttrIframe").attr("src","/task/busModTaskList?sid=${param.sid}&pager.pageSize=10&ifreamName=otherSpAttrIframe&busId=${spFlowInstance.id}&busType=022&redirectPage="+encodeURIComponent(window.location.href));
	});
	//审批记录
	$("#spFlowRecordLi").click(function(){
		
		$("#otherSpAttrIframe").show();
		$("#spDetailDiv").hide();
		$("a[data-optBtn='1']").show();
		
		$(this).parent().find("li").removeAttr("class");
		$("#spFlowRecordLi").attr("class","active");
		$("#otherSpAttrIframe").attr("src","");
	});
	//任务浏览记录
	$("#spHistory").click(function(){
		
		$("#otherSpAttrIframe").show();
		$("#spDetailDiv").hide();
		$("a[data-optBtn='1']").show();
		
		$(this).parent().find("li").removeAttr("class");
		$("#spHistory").attr("class","active");
		$("#otherSpAttrIframe").attr("src","/workFlow/listSpHistory?sid=${param.sid}&instanceId=${spFlowInstance.id}");
	});
	//任务浏览记录
	$("#spViewRecord").click(function(){
		
		$("#otherSpAttrIframe").show();
		$("#spDetailDiv").hide();
		$("a[data-optBtn='1']").show();
		
		$(this).parent().find("li").removeAttr("class");
		$("#spViewRecord").attr("class","active");
		$("#otherSpAttrIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${spFlowInstance.id}&busType=022&ifreamName=otherSpAttrIframe");
	});
	//审批会签进度
	$("#spHuiQianProcess").click(function(){
		$("#otherSpAttrIframe").show();
		$("#spDetailDiv").hide();
		$("a[data-optBtn='1']").show();
		
		$(this).parent().find("li").removeAttr("class");
		$("#spViewRecord").attr("class","active");
		$("#otherSpAttrIframe").attr("src","/workFlow/spHuiQianProcess?sid=${param.sid}&instanceId=${spFlowInstance.id}&ifreamName=otherSpAttrIframe");
	});
	//审批留言记录
	$("#spFlowTalk,#spFlowTalkByHead").click(function(){
		
		$("#otherSpAttrIframe").show();
		$("#spDetailDiv").hide();
		$("a[data-optBtn='1']").show();
		
		$("#spFlowTalk").parent().find("li").removeAttr("class");
		$("#spFlowTalk").attr("class","active");
		$("#otherSpAttrIframe").attr("src","/workFlow/spFlowTalkPage?sid=${param.sid}&busId=${spFlowInstance.id}");
	});
});

/**
 * 会签提示
 */
function alertHuiQianInfo(){
	var curUser = "${(spFlowInstance.executor>0 and spFlowInstance.executor==userInfo.id)?1:0}";
	var executeType = "${(spFlowInstance.executeType eq 'huiqian')?1:0}";
	var showHuiQianInfoFlag = accMul(curUser,executeType);
	if(showHuiQianInfoFlag === 1){
		var content = "${huiqianContent}";
		if(!content){
			content = "请会签";
		}
		window.top.layer.confirm(content,{
			title:"会签说明",
			icon:7,
			btn:["确定"]
		},function(index){
			window.top.layer.close(index)
		})
	}
}


</script>
<style type="text/css">
input[type="radio"],input[type="checkbox"] {
    top: 3px !important;
    left:1px !important;
    opacity: 1 !important;
    width: 15px !important;
    height: 15px !important;
}

#spFileDiv .uploadify-queue-item{
padding-left:-50px;
margin-left:-50px;
}

.ps-toolsBtn>a{
line-height: inherit;
}
.alert{
	border-color: #fbfbfb !important;
	background: #fbfbfb !important;
	color: #262626 !important;
}
.ps-toolsBtn>a{
	padding: 0 5px;
}
</style>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                		<a href="javascript:void(0)" class="widget-caption blue padding-right-5"
               				attentionState="${spFlowInstance.attentionState}" busType="022" busId="${spFlowInstance.id}" describe="0" iconSize="sizeMd"
               				onclick="changeAtten('${param.sid}',this)" title="${task.attentionState==0?"关注":"取消"}">
							<i class="fa ${spFlowInstance.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
						</a>
						
                        <span class="widget-caption themeprimary" style="font-size: 20px;width:35%;overflow:hidden;text-overflow: ellipsis;white-space: nowrap;">${spFlowInstance.formModName}</span>
                       <div class="widget-buttons ps-toolsBtn" id="spBtnDiv">
                       	<input type="hidden" id="maxMin" value="0">
                      		<c:if test="${spFlowInstance.executor>0 and spFlowInstance.executor==userInfo.id}">
								<c:if test="${spFlowInstance.executeType eq 'assignee'}">
									<a href="javascript:void(0)" class="green" data-optBtn="1" id="spFormAgree">
										<i class="fa fa-check-square-o"></i>下一步
									</a>
								</c:if>
								<c:if test="${spFlowInstance.executeType eq 'huiqian'}">
									<a href="javascript:void(0)" class="green" data-optBtn="1" id="subSpHuiQian">
										<i class="fa fa-pencil"></i>提交
									</a>
								</c:if>
	                       	</c:if>
	                       	<c:if test="${spFlowInstance.flowState eq 1}">
								<a href="javascript:void(0);" class="purple" id="spFlowTalkByHead" title="留言">
									<i class="fa fa-comments"></i>留言
								</a>
	                       	</c:if>
							<a href="javascript:void(0)" class="blue" data-optBtn="0" onclick="maxMinWin(this)">
								<i class="fa  fa-arrows-alt"></i>最大化
							</a>
	                       	<a class="green ps-point margin-right-0" data-optBtn="1" data-toggle="dropdown" title="更多操作" id = "moreA">
								  <i class="fa fa-th"></i>更多
							  </a>
							  <!--Notification Dropdown-->
							  <ul class="pull-right dropdown-menu dropdown-arrow ps-layerUl" id = "moreUl">
								  <c:if test="${spFlowInstance.executor>0 and spFlowInstance.executor==userInfo.id}">
									<c:if test="${spFlowInstance.executeType eq 'assignee'}">
										<li>
											<a href="javascript:void(0)" id="spFormReject">
												<i class="fa fa-circle-o"></i>终止
											</a>
										</li>
									  	<li>
											<a href="javascript:void(0)" id="spFormBack">
												<i class="fa fa-mail-reply"></i>自由回退
											</a>
									 	</li>
									 	<li>
											<a href="javascript:void(0)" id="updateSpHuiQian">
												<i class="fa fa-pencil"></i>审批会签
											</a>
									 	</li>
									  	<li>
											<a href="javascript:void(0)" id="updateSpInsAssign">
												<i class="fa fa-h-square"></i>审批转办
											</a>
									 	</li>
									</c:if>
								  </c:if>
							 	<li>
									<a href="javascript:void(0)" id="addBusTaskForSp">
										<i class="fa fa-flag"></i>发布任务
									</a>
							 	</li>
								  <li>
									<a href="javascript:void(0)" id="printBtn">
										<i class="fa fa-print"></i>打印表单
									</a>
								  </li>
							  </ul>
	                       	
					</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin();" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="margin-top-40"   id="contentBody" style="overflow: hidden;overflow-y:scroll;width:100%">
						<div class="widget-body no-shadow padding-top-20 ">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat no-padding-top" id="myTab11">
                                      	 	<li id="spDetail" class="active">
                                                 <a data-toggle="tab" href="javascript:void(0)">表单详情</a>
                                             </li>
                                             <li id="spHistory">
                                                 <a data-toggle="tab" href="javascript:void(0)">审批记录</a>
                                             </li>
                                             <li id="spFlowTalk">
                                                 <a data-toggle="tab" href="javascript:void(0)">审批留言<c:if test="${spFlowInstance.spFlowTalkNum > 0}"><span style="color:red;font-weight:bold;">（${spFlowInstance.spFlowTalkNum}）</span></c:if></a>
                                             </li>
                                             <li id="spUpfileMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">审批文档<c:if test="${spFlowInstance.upfileNum > 0}"><span style="color:red;font-weight:bold;">（${spFlowInstance.upfileNum}）</span></c:if></a>
                                             </li>
                                              <li id="relateTask">
                                                 <a data-toggle="tab" href="javascript:void(0)">关联任务<c:if test="${spFlowInstance.taskNum > 0}"><span style="color:red;font-weight:bold;">（${spFlowInstance.taskNum}）</span></c:if></a>
                                             </li>
                                             <li id="spHuiQianProcess" style="display:${spFlowInstance.flowState eq 1 ?'block':'none'}">
                                                 <a data-toggle="tab" href="javascript:void(0)">会签进度<c:if test="${spFlowInstance.huiQianNum > 0}"><span style="color:red;font-weight:bold;">（${spFlowInstance.huiQianNum}）</span></c:if></a>
                                             </li>
                                             <%--<li id="spViewRecord">--%>
                                                 <%--<a data-toggle="tab" href="javascript:void(0)">最近查看</a>--%>
                                             <%--</li>--%>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat no-padding-left">
                                    	 	<div id="spDetailDiv" style="display:block;">
												<div id="formInfo"></div>
												<div id="printBody" class="form-preview module-detail-view writeform-content">
													<div id="organicName" class="ps-clear hide" style="clear:both;text-align: center;width:95%;">
														<div class="textinput-b title" style="background: inherit;font-size: 20px">${userInfo.orgName }</div>
													</div>
													<div id="form-info-div" class="detail-block margin-top-20 no-margin-left" style="width:95%;clear: both;">
														<div class="wform-title ps-clear" style="clear:both">
															<div class="textinput-b title" style="background: inherit;">${spFlowInstance.flowName}</div>
															<input type="hidden" id="spId" value="${spFlowInstance.id}">
														</div>
													      
														<div id="applicants" class="wform-post-info clearfix">
													      	<div class="item">
							          							<label>填写人:</label>
							          							<div class="control ellipsis">
							           								<a class="usercard-toggle ellipsis  username applyUser_js" userid="${spFlowInstance.creator }">${spFlowInstance.creatorName }</a>
							          							</div>
							         						</div>
							         						<div class="item">
							          							<label>部门:</label>
							          							<div class="control ellipsis">
							           								<a class="department ellipsis applyDepa_js j_dept">${spFlowInstance.depName }</a>
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
													<div class="js-biaoge-form form-border-view form-view form-view_js no-margin-left" 
													id="formpreview" style="display: none;width:95%;margin-left: 18px">
															   
													</div>
													
													<%-- 暂时不开放流程关联功能 --%>
													<c:if test="${spFlowInstance.flowState==1 or spFlowInstance.flowState==2}">
														<div class="detail-block hide" style="width:95%;display: none">
															<div id="spRelate" class="wform-post-info clearfix" style="display: none;">
																<div class="item" style="width: 100%">
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
																<div class="item" style="width: 60%;display: none;">
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
																<div class="item" style="width: 40%;display: none;">
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
													</c:if>
													<div id="spHistoryDiv" class="detail-block margin-top-20 hide" style="width:95%;">
														<div class="clearfix margin-bottom-5" style="border-bottom: 1px solid #ddd;">
															<div class="widget-content" style="padding: 10px 10px;">
																<div class="input-instead j_readOnly textinput-b title" style="background: inherit;">审批记录</div>
															</div>
														</div>
														<div id="spHistorys">
															
														</div>
													</div>
												</div>
											</div>
                                    	 	<iframe id="otherSpAttrIframe" style="display:none;" class="layui-layer-load"
												border="0" frameborder="0" allowTransparency="true"
												noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
                                    	 </div>
                                	</div>
                            	</div>
                            </div> 
						
					</div>
					
				</div>
			</div>
		</div>
	<script type="text/javascript">
	var sid='${param.sid}';
	var formTag = 'editForm';
	var TEAMS={
			"currentUser":{"userId":"${userInfo.id}",
				"id":"${userInfo.id}",
				"username":"${userInfo.userName}",
				"department":{"name":"${userInfo.depName}","id":"${userInfo.depId}"}
			},
			"currentTenant":{"tenantKey":"t5y8pa3o0l"},
			"spFlow":{"creator":${spFlowInstance.creator},"preStageItemId":${spFlowInstance.stagedItemId},
				"busId":"${spFlowInstance.busId}","busType":"${spFlowInstance.busType}",
				"busName":"${spFlowInstance.busName}","stagedItemId":"${spFlowInstance.stagedItemId}",
				"stagedItemName":"${spFlowInstance.stagedItemName}"},
			"autoUpDateRelate":"${spFlowInstance.executor !=userInfo.id and spFlowInstance.creator==userInfo.id}"
	}
	FLOWINFO={"formKey":"${spFlowInstance.formKey}","instanceId":"${spFlowInstance.id}","preSpstate":"${spFlowInstance.spState}",
			"flowName":"${spFlowInstance.flowName}","creatorName":"${spFlowInstance.creatorName}","creatorDepName":"${spFlowInstance.depName}",
			"preFlowState":"${spFlowInstance.flowState}","actInstaceId":"${spFlowInstance.actInstaceId}",
			"creatorName":"${spFlowInstance.creatorName}","creatorDepName":"${spFlowInstance.depName}",
			"sonInstanceId":"${spFlowInstance.sonInstanceId}","sonFlowSerialNum":"${spFlowInstance.sonFlowSerialNum}","sonFlowName":"${spFlowInstance.sonFlowName}",
			"layoutId":"${spFlowInstance.layoutId}","flowId":"${spFlowInstance.flowId}","saveType":"update"};
	</script>
	<script type="text/javascript">
		seajs.config({base:"/static/plugins/form/js",preload:["workFlowForm.js?v="+Math.random()],charset:"utf-8",debug:!1}),seajs.use("easywin/workFlow/formView");  
	</script>
	<script src="/static/js/jquery.JPlaceholder.js"></script>
	<script src="/static/js/jquery.jqprint-0.3.js"></script>
</body>
</html>