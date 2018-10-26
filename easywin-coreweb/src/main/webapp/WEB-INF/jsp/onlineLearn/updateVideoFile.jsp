<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.BiaoQing"%>
<%@page import="com.westar.core.web.BiaoQingContext"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@page import="com.westar.base.util.DateTimeUtil"%>
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
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>

<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css">
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/static/js/selfGroupTree.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>

<script> 
function chenckDep(){
	if($("#scopeTypeSel").val()=="我自己"||$("#scopeTypeSel").val()=="所有同事"){
		$(".dep").hide();
	}else{
		$(".dep").show();
	}
}
$(function(){
	chenckDep();
	setInterval(chenckDep,1000); 
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
	
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
})
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
	function onSubmit(){
		$("#fileForm").submit();
	}
</script>
</head>
<body style="background-color: #fff">
	<input type="hidden" id="subState" value="0">
	<form id="fileForm" class="subform" method="post" action="/onlineLearn/updateVideoFile?sid=${param.sid}">
		<input type="hidden" name="classifyId" id="classifyId" value="${classify.id}" />
		<input type="hidden" name="id" value="${fileDetail.id }" />
		<div id="fileDiv"></div>
		<div class="container" style="padding: 0px 0px; width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary padding-top-5" style="font-size: 20px">视频上传</span>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
									<i class="fa fa-times themeprimary"></i>
								</a>
							</div>
						</div>
						<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden; overflow-y: scroll;">
							<div class="widget radius-bordered">
								<div class="widget-header bg-bluegray no-shadow">
									<span class="widget-caption blue">视频描述</span>
								</div>
								<div class="widget-body no-shadow">
									<div class="tickets-container bg-white">
										<textarea id="fileDescribe" name="fileDescribe" style="height: 60px; color: #000;" class="form-control">${fileDetail.fileDescribe }</textarea>
									</div>
									<div class="pull-left blue padding-left-10 padding-top-20 padding-right-10 ">
										文件名：
									</div>
									<div class="ps-right-box padding-top-20" style="height: auto;">
									${fileDetail.fileName }
									</div>
									<div class="pull-left blue padding-left-10 padding-top-20 padding-right-10 dirChoose">
										存档位置:
										<c:choose>
											<c:when test="${classify.id eq -1 || empty classify.typeName }">
												<span style="cursor: pointer;" class="label label-default margin-right-5 margin-bottom-5">所有文件</span>
											</c:when>
											<c:otherwise>
												<span style="cursor: pointer;" class="label label-default margin-right-5 margin-bottom-5">${classify.typeName }</span>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="ticket-user  ps-right-box" style="height: auto;">
										<div class="pull-left gray ps-left-text padding-top-10">
											<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-5" onclick="chooseDir('${param.sid}')">
												<i class="fa fa-plus"></i>存档位置
											</a>
										</div>
									</div>
									<div class="blue padding-left-10 padding-top-20 padding-right-10" style="position: relative;">
										分组范围:
										<input class="margin-left-10" id="scopeTypeSel" type="text" readonly="readonly" value="${scopeTypeSel}" style="width: 150px; height: 30px" onclick="showMenu();" />
										<div id="menuContent" style="display: none; position: absolute; top: -50px; left: 250px; z-index: 999">
											<input type="hidden" id="idType" name="idType" value="${idType}" />
											<ul id="treeDemo" class="ztree" style="clear: both; margin-top: 0; z-index: 1; background-color: #f0f0f0; width: 150px; padding-bottom: 0px"></ul>
											<ul id="addGrpUl" class="ztree" style="z-index: 1; background-color: #f0f0f0; width: 150px; padding-bottom: 10px;">
												<li style="text-align: center; margin-top: 5px; color: #1c98dc; cursor: pointer;" onclick="addGrpForTree('scopeTypeSel','menuContent','idType','treeDemo','${param.sid}')">+添加分组</li>
											</ul>
										</div>
									</div>
									<div class="dep">
										<div class="pull-left blue padding-left-10 padding-top-20 padding-right-10">部门范围:</div>

										<div class="ticket-user pull-left ps-right-box" style="height: auto;">
											<div class="pull-left gray ps-left-text padding-top-10">
												<div style="float: left; width: 250px; display: none">
													<select list="scopeDep" listkey="depId" listvalue="depName" id="scopeDep_depId" name="scopeDep.depId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true"
														style="width: 100%; height: 100px;">
														<c:forEach items="${listScopeDep }" var="depVo" varStatus="vas">
															<option selected="selected" value="${depVo.depId}">${depVo.depName}</option>
														</c:forEach>
													</select>
												</div>
												<div id="scopeDepDiv" class="pull-left" style="max-width: 460px">
														<c:forEach items="${listScopeDep }" var="depVo" varStatus="vax">
															<span id="dep_span_${depVo.depId }" style="cursor: pointer;" title="双击移除" class="label label-default margin-right-5 margin-bottom-5" ondblclick="removeClickDep('scopeDep_depId',${depVo.depId })">${depVo.depName }</span>
														</c:forEach>
												</div>
												<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-5" title="部门多选" onclick="depMore('scopeDep_depId','','${param.sid}','null','scopeDepDiv');">
													<i class="fa fa-plus"></i>选择
												</a>
											</div>
										</div>
									</div>
									<div class="ps-clear"></div>
								</div>
							</div>
							<div class="widget-body no-shadow"></div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
