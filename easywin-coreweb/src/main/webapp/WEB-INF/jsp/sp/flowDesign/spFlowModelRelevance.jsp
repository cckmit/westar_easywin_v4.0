<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8"
	src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});

/**
 * 关联流程赋值
 */
function flowSelectedReturn(flowId,flowName){
	if(flowId=="${spFlowModelRelevanceCfg.id}"){
		window.top.layer.msg("不能关联流程本身！",{icon:2});
		return;
	}
	$("#sonFlowName").text(flowName);
	querySonFlowcomment(flowId);//初始化流程自表单选择控件
	$("#sonFlowId").val(flowId);//关联主键赋值
}

/**
 * 初始化流程自表单选择控件
 */
function querySonFlowcomment(flowId){
	$.ajax({  
        url : "/flowDesign/formCompons?sid=${param.sid}&flowId="+flowId,  
        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
        type : "POST",  
        dataType : "json", 
        success : function(result) {   
        	if(result.status=="y"){
        		initSonFlowcomment(result.formCompons);
        	}else{
        		window.top.layer.msg("获取表单控件失败！",{icon:2});
        	}
        }  
    });
}
//初始化流程自表单选择控件
function initSonFlowcomment(formCompons){
	if(formCompons){
		var trHtml = "";
		for(var i=0;i<formCompons.length;i++){
			trHtml = trHtml +"<tr>";
			trHtml = trHtml +"<td>"+(i+1)+"</td>";
			trHtml = trHtml +"<td>"+formCompons[i].title+"</td>";
			trHtml = trHtml +"<td><——</td>";
			var optHtml = initPformComponHtml(formCompons[i].fieldId);//初始化主流程控件选择下拉框
			trHtml = trHtml +"<td>"+optHtml+"</td>";
			trHtml = trHtml +"</tr>";
		}
		$("#formComponTr").html(trHtml);
	}
}

//初始化主流程控件选择下拉框
function initPformComponHtml(sonFormComponKey){
	var optHtml = "<select name=\"keyValueArray\">";
	optHtml = optHtml +"<option value=\"\" style=\"font-weight:bold;\">选择值来源控件</option>";
	$("#pFlowComponOpt option").each(function(){  //遍历所有option  
        optHtml = optHtml +"<option value=\""+$(this).val()+"&"+sonFormComponKey+"\">"+$(this).text()+"</option>";
   	})
	optHtml = optHtml +"</select>";
	return optHtml;
}

//表单提交
function formSubmit(){
	$("#addForm").submit();
}
//清除关联关系
function delFlowModelRelevance(){
	window.top.layer.confirm("确定清除关联关系？", {icon: 3, title:"确认对话框"}, function(index){
		$("#addForm").attr("action","/flowDesign/delFlowModelRelevance");
		$("#addForm").submit();
	  	window.top.layer.close(index);
	});
}
</script>
</head>
<body>
	<form class="subform" method="post" action="/flowDesign/initFlowModelRelevance" id="addForm">
		<tags:token></tags:token>
		<input type="hidden" name="pflowId" id="pflowId" value="${spFlowModelRelevanceCfg.id}">
		<input type="hidden" name="sonFlowId" id="sonFlowId" value="${spFlowModelRelevanceCfg.sonFlowId}">
		<c:choose>
			<c:when test="${not empty spFlowModelRelevanceCfg.listFlowFormCompons}">
				<select id="pFlowComponOpt" style="display:none;">
					<c:forEach items="${spFlowModelRelevanceCfg.listFlowFormCompons}" var="pFormCompon"
						varStatus="vs">
						<option value="${pFormCompon.fieldId}">${pFormCompon.title}</option>
					</c:forEach>
				</select>
			</c:when>
		</c:choose>
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div
							class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">流程关联</span>
						</div>
						<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
						<div class="widget-body margin-top-40" id="contentBody"
							style="overflow: hidden;overflow-y:scroll;">
							<div class="widget radius-bordered">
								<div class="widget-body no-shadow">
									<div class="tickets-container bg-white">
										<ul class="tickets-list">
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">&nbsp;主&nbsp;流&nbsp;程</div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left">
														<span>${spFlowModelRelevanceCfg.flowName}</span>
													</div>
													<div class="pull-left">
														<span id="addTaskWarn" style="float:left;margin-left:2px;"></span>
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline"
												style="clear:both">
												<div class="pull-left gray ps-left-text">
													&nbsp;关联流程</div>
												<div class="ticket-user pull-left ps-right-box">
													<span id="sonFlowName">${spFlowModelRelevanceCfg.sonFlowName}</span>
													<a href="javascript:void(0);" onclick="spFlowModelForSelect('${param.sid}');">
														<button class="btn btn-info btn-primary btn-xs" type="button">
															<i class="fa fa-plus"></i>关联流程选择
														</button>
													</a>
													<c:if test="${not empty spFlowModelRelevanceCfg.sonFlowId}">
														<a href="javascript:void(0);" onclick="delFlowModelRelevance();" class="margin-left-5">
															<button class="btn btn-info btn-primary btn-xs" type="button">
																<i class="fa fa-times"></i>清除关联
															</button>
														</a>
													</c:if>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline"
												style="clear:both">
												<table class="table table-striped table-hover general-table">
													<thead>
														<tr>
															<th width="10%">序号</th>
															<th>赋值对象</th>
															<th width="15%">映射关系</th>
															<th>值来源</th>
														</tr>
													</thead>
													<tbody id="formComponTr">
														<c:choose>
															<c:when test="${not empty listSonFlowFormCompons}">
																<c:forEach items="${listSonFlowFormCompons}" var="sonFormCompon"
																	varStatus="vs">
																	<tr>
																		<td>${vs.count}</td>
																		<td>${sonFormCompon.title}</td>
																		<td><——</td>
																		<td name="sonComponTd">
																			<c:choose>
																				<c:when test="${not empty spFlowModelRelevanceCfg.listFlowFormCompons}">
																					<select name="keyValueArray">
																						<option value="">选择值来源控件</option>
																						<c:forEach items="${spFlowModelRelevanceCfg.listFlowFormCompons}" var="pFormCompon"
																							varStatus="vs">
																							<option value="${pFormCompon.fieldId}&${sonFormCompon.fieldId}" ${sonFormCompon.fromFormControlKey==pFormCompon.fieldId?'selected':''}>${pFormCompon.title}</option>
																						</c:forEach>
																					</select>
																				</c:when>
																			</c:choose>
																		</td>
																	</tr>
																</c:forEach>
															</c:when>
														</c:choose>
													</tbody>
												</table>
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
		</div>
	</form>
</body>
</html>