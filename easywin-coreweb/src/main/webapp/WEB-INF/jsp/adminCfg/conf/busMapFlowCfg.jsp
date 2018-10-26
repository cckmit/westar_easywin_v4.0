<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title><%=SystemStrConstant.TITLE_NAME%></title>
<meta name="description" content="Dashboard" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/static/cssV2/iframe.css" />
<script type="text/javascript">
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			datatype : {
				"input" : function(gets, obj, curform, regxp) {
					var str = $(obj).val();
					if (str) {
						var count = str.replace(/[^\x00-\xff]/g, "**").length;
						var len = $(obj).attr("defaultLen");
						if (count > len) {
							return "名称太长";
						} else {
							return true;
						}
					} else {
						return false;
					}
				}
			},
			callback : function(form) {
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		/* $("#editForm [name='keyValueArray']").change(function() {
			//$("#editForm").submit();
			var kvs = $(this).val();
			var url ="/adminCfg/initBusAttrMapFormColByAjax?sid=${param.sid}";
			var params={busMapFlowId:"${busMapFlow.id}",
					busType:"${param.busType}",
					formCol:kvs.split("&")[0],
					busAttr:kvs.split("&")[1],
					isRequire:kvs.split("&")[2]};
			postUrl(url,params,function(data){
				showNotification(data.status=='y'?1:2,data.info);
			});
		}); */
	 	$(parent.document,"#tabInfo").find(".index-active").find(".tabTitle").html("配置发文关联");
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
	});

	//表单提交
	function formSubmit(action,needCheck) {
		
		var isRequireds = $("#editForm").find("select[isRequired='1']");
		var flag = true;
		if(isRequireds && isRequireds.get(0) && needCheck!=0){
			$.each(isRequireds,function(index,selectObj){
				var val = $(selectObj).val();
				if(!val){
					flag = false;
					scrollToLocation($("#contentBody"),selectObj)
					layer.tips("必填映射！",$(selectObj),{tips:1});
					return false;
				}
			})
		}
		if(flag){
			if ($("#editForm [name='flowId']").val()) {
				$("#editForm").attr("action", action);
				$("#editForm").submit();
			} else {
				window.top.layer.msg("请先选择关联流程！", {
					icon : 2
				});
			}
		}
		
	}
	//关联流程选择
	function spFlowModelForSelect(sid) {
		window.top.layer
				.open({
					type : 2,
					title : false,
					closeBtn : 0,
					area : [ '600px', '500px' ],
					fix : true, //不固定
					maxmin : false,
					move : false,
					scrollbar:false,
					content : ['/flowDesign/listFlowModelForSelect?pager.pageSize=9&status=1&sid='+ sid, 'no' ],
					btn : [ '选择', '取消' ],
					yes : function(index, layero) {
						var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						var result = iframeWin.flowSelected();
						if (result) {
							flowSelectedReturn(result.flowId, result.flowName);//页面回调赋值
							window.top.layer.close(index)
						}
					},success: function(layero,index){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
							 window.top.layer.close(index);
						 })
					}
				});
	}
	/**
	 * 关联流程赋值
	 */
	function flowSelectedReturn(flowId, flowName) {
		if (flowId) {
			$("#editForm [name='flowId']").val(flowId);
			$.each($("#editForm [name='keyValueArray']"), function(index,
					selectObj) {
				$(selectObj).find("option").first().attr("selected", true);
			});
			formSubmit("/adminCfg/initBusMapFlow",0);
		} else {
			window.top.layer.msg("请先选择关联流程！", {
				icon : 2
			});
			return;
		}
	}

	//删除关联关系
	function delBusMapFlow() {
		formSubmit("/adminCfg/delBusMapFlow",0);
	}

	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	//删除范围限制关系
	function delBusMapFlowAuthDep() {
		formSubmit("/adminCfg/delBusMapFlowAuthDep",0);
	}
</script>
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
		"ifreamName" : "${param.ifreamName}",
	};
</script>
</head>
<body>
	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
		<span class="widget-caption themeprimary" style="font-size: 20px">数据映射配置</span>
		   <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px">
			<c:choose>
				<c:when test="${param.busType eq '029'}">
					--出差业务
				</c:when>
				<c:when test="${param.busType eq '035'}">
					--一般借款申请业务
				</c:when>
				<c:when test="${param.busType eq '030'}">
					--借款业务
				</c:when>
				<c:when test="${param.busType eq '03101'}">
					--出差报销业务
				</c:when>
				<c:when test="${param.busType eq '03102'}">
					--一般费用报销业务
				</c:when>
				<c:when test="${param.busType eq '03401'}">
					--出差汇报业务
				</c:when>
				<c:when test="${param.busType eq '03402'}">
					--一般借款说明
				</c:when>
				<c:when test="${param.busType eq '036'}">
					--请假业务
				</c:when>
				<c:when test="${param.busType eq '037'}">
					--加班业务
				</c:when>
				<c:when test="${param.busType eq '070'}">
					--需求管理
				</c:when>
				<c:when test="${param.busType eq '04201'}">
					--IT运维管理-事件管理过程
				</c:when>
				<c:when test="${param.busType eq '04202'}">
					--IT运维管理-问题管理过程
				</c:when>
				<c:when test="${param.busType eq '04203'}">
					--IT运维管理-变更管理过程
				</c:when>
				<c:when test="${param.busType eq '04204'}">
					--IT运维管理-发布管理过程
				</c:when>
				<c:otherwise>
					--
				</c:otherwise>
			</c:choose>
		   </span>
		   <div class="widget-buttons">
			<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
				<i class="fa fa-times themeprimary"></i>
			</a>
		</div>
	</div><!--Widget Header-->						

	<!--Widget Header-->
		<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;padding:0 12px;">
			<div class="widget radius-bordered margin-top-10">
				<div class="no-shadow">
					<div class="tickets-container bg-white">
						<ul class="tickets-list">
							<form action="/adminCfg/initBusAttrMapFormCol" id="editForm" method="post" class="subform">
								<input type="hidden" name="id" value="${busMapFlow.id}">
								<input type="hidden" name="sid" value="${param.sid}">
								<input type="hidden" name="busType" value="${param.busType}">
								<input type="hidden" name="flowId" value="${busMapFlow.flowId}">
								<li class=" no-shadow ps-listline" style="clear:both;height:140px;">
									<div class="pull-left gray ps-left-text">
										<span class="margin-left-10" style="font-weight:bold;color:black;">配置描述</span>
										<span style="color: red">*</span>
									</div>
									<div class="ticket-user pull-left ps-right-box">
										<textarea class="form-control" id="taskRemark" name="description" rows="" cols="" style="width:500px;height: 120px;float:left" datatype="*" nullmsg="请描述配置用途">${busMapFlow.description }</textarea>
									</div>
								</li>
								<li class=" no-shadow ps-listline">
									<div class="pull-left gray ps-left-text">
										<span class="margin-left-10" style="font-weight:bold;color:black;">授权部门</span>
									</div>
									<div class="ticket-user pull-left ps-right-box" style="height: auto;">
										<div class="pull-left gray ps-left-text">
											<div style="width: 250px; float: left; display: none;">
												<select style="width: 100%; height: 100px;" id="listBusMapFlowAuthDep_depId" ondblclick="removeClick(this.id)" multiple="multiple" name="listBusMapFlowAuthDep.depId" list="listBusMapFlowAuthDep"
													listkey="depId" listvalue="depName" moreselect="true">
													<c:choose>
														<c:when test="${not empty busMapFlow.listBusMapFlowAuthDep}">
															<c:forEach items="${busMapFlow.listBusMapFlowAuthDep}" var="dep" varStatus="vs">
																<option value="${dep.depId}" selected="selected">${dep.depName}</option>
															</c:forEach>
														</c:when>
													</c:choose>
												</select>
											</div>
											<div style="max-width: 460px;" id="MeetDepDiv" class="pull-left">
												<c:choose>
													<c:when test="${not empty busMapFlow.listBusMapFlowAuthDep}">
														<c:forEach items="${busMapFlow.listBusMapFlowAuthDep}" var="dep" varStatus="vs">
															<span style="cursor: pointer;" id="dep_span_${dep.depId }" class="label label-default margin-top-5 margin-right-5 margin-bottom-5" title="双击移除"
																ondblclick="removeClickDep('listBusMapFlowAuthDep_depId',${dep.depId })">${dep.depName}</span>
														</c:forEach>
													</c:when>
												</c:choose>
											</div>
											<a class="btn btn-primary btn-xs" style="padding: 2px 4px" title="部门多选" onclick="depMore('listBusMapFlowAuthDep_depId','','${param.sid}','no','MeetDepDiv');" href="javascript:void(0);">
												<i class="fa fa-plus"></i>选择
											</a>
											<c:if test="${not empty busMapFlow.listBusMapFlowAuthDep}">
												<a href="javascript:void(0);" onclick="delBusMapFlowAuthDep();" class="btn btn-primary btn-xs margin-left-5" style="padding: 2px 4px">
													<i class="fa fa-times"></i>清除限制
												</a>
											</c:if>
										</div>
									</div>
									<div class="ps-clear"></div>
								</li>
								<li class=" no-shadow ps-listline" style="clear:both">
									<div class="pull-left gray ps-left-text">
										<span class="margin-left-10" style="font-weight:bold;color:black;">关联流程</span>
										<span style="color: red">*</span>
									</div>
									<div class="ticket-user pull-left ps-right-box">
										<span id="sendOfficalFlowName">${busMapFlow.flowName}</span>
										<a class="btn btn-primary btn-xs" onclick="spFlowModelForSelect('${param.sid}');" style="padding: 2px 4px"  href="javascript:void(0)">
											<i class="fa fa-plus"></i>流程关联
										</a>
										<c:if test="${not empty busMapFlow.flowId}">
											<a href="javascript:void(0);" onclick="delBusMapFlow();" class="btn btn-primary btn-xs margin-left-5" style="padding: 2px 4px">
												<i class="fa fa-times"></i>清除关联
											</a>
										</c:if>
									</div>
								</li>
								<li class=" no-shadow ps-listline" style="clear:both">
									<div class="official-table">
										<div style="font-size:18px;color:red;font-weight:bold;padding-bottom:15px;padding-left:10px;">“业务表”&lt;&lt;——“审批表”</div>
										<div class="table-content">
											<table class="table table-striped table-hover general-table">
												<thead>
													<tr>
														<th width="10%">序号</th>
														<th>赋值对象(业务对象)</th>
														<th width="15%">映射关系</th>
														<th>值来源</th>
														<th style="width:150px">必填设置</th>
													</tr>
												</thead>
												<tbody id="formComponTr">
													<c:choose>
														<c:when test="${not empty attrsMap}">
															<c:forEach items="${attrsMap}" var="attr" varStatus="vs">
																<tr>
																	<td>${vs.count}</td>
																	<td>${attr.value}</td>
																	<td>&lt;&lt;——</td>
																	<td name="sonComponTd">
																		<c:choose>
																			<c:when test="${not empty listFlowFormCompons}">
																				<select name="keyValueArray" isRequired="${attr.isRequired eq '1'?'1':'0'}">
																					<option value="">选择值来源控件</option>
																					<c:forEach items="${listFlowFormCompons}" var="formCompon" varStatus="vs">
																						<option value="${formCompon.fieldId}&${attr.key}&${attr.isRequired}" ${(attr.formControlKey eq formCompon.fieldId)?'selected="selected"':''}>${formCompon.title}</option>
																					</c:forEach>
																				</select>
																			</c:when>
																		</c:choose>
																	</td>
																	<td>${attr.isRequired eq '1'?"是":"否"}</td>
																</tr>
															</c:forEach>
														</c:when>
													</c:choose>
												</tbody>
											</table>
										</div>
									</div>
								</li>
								<li class=" no-shadow ps-listline" style="clear:both;display:${(param.busType eq '03101' or param.busType eq '03401')?'block':'none'}">
									<div class="official-table">
										<div style="font-size:18px;color:red;font-weight:bold;padding-bottom:15px;padding-left:10px;">“业务表”——&gt;&gt;“审批表”</div>
										<div class="table-content">
											<table class="table table-striped table-hover general-table">
												<thead>
													<tr>
														<th width="10%">序号</th>
														<th>值来源(业务属性值)</th>
														<th width="15%">映射关系</th>
														<th>赋值对象</th>
													</tr>
												</thead>
												<tbody>
													<c:choose>
														<c:when test="${not empty listFormColMapBusAttr}">
															<c:forEach items="${listFormColMapBusAttr}" var="busAttr" varStatus="vs">
																<tr>
																	<td>${vs.count}</td>
																	<td>${busAttr.value}</td>
																	<td>——&gt;&gt;</td>
																	<td>
																		<c:choose>
																			<c:when test="${not empty listFlowFormCompons}">
																				<select name="formColMapBusAttrs">
																					<option value="">选择值来源控件</option>
																					<c:forEach items="${listFlowFormCompons}" var="formCompon" varStatus="vs">
																						<option value="${formCompon.fieldId}&${busAttr.key}" ${(busAttr.formControlKey eq formCompon.fieldId)?'selected="selected"':''}>${formCompon.title}</option>
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
										</div>
									</div>
								</li>
							</form>
						</ul>
					</div>
				</div>
			</div>
		</div>
	<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>