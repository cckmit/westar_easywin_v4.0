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
<script type="text/javascript">
$(function(){
	//关联表单
	$("#formName").click(function(){
		formModListForSelect();//弹窗打开表单候选列表页面
	});
	//流程基本配置
	$("#flowModelbaseMenuLi").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$("#flowModelbaseMenuLi").attr("class","active");
		$("#otherFlowModelAttrIframe").attr("src","/flowDesign/editFlowBaseInfo?sid=${param.sid}&activityMenu=${activityMenu}&flowId=${flowConfig.id}");
	});
	//流程步骤配置
	$("#flowModelStepMenuLi").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$("#flowModelStepMenuLi").attr("class","active");
		$("#otherFlowModelAttrIframe").attr("src","/flowDesign/editFlowStepConfig?sid=${param.sid}&activityMenu=${activityMenu}&flowId=${flowConfig.id}");
	});
    //日志
    $("#logsMenuLi").click(function(){
        $(this).parent().find("li").removeAttr("class");
        $("#logsMenuLi").attr("class","active");
        $("#otherFlowModelAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${flowConfig.id}&busType=02202&ifreamName=otherFlowModelAttrIframe");
    });
    //数据映射配置
    $("#mappingMenuLi").click(function(){
        $(this).parent().find("li").removeAttr("class");
        $("#mappingMenuLi").attr("class","active");
        $("#otherFlowModelAttrIframe").attr("src","/adminCfg/spMapFlowCfg?sid=${param.sid}&pager.pageSize=10&flowId=${flowConfig.id}&ifreamName=otherFlowModelAttrIframe");
    });
});

$(document).ready(function() {
	//alert("${activeLi}");
});
</script>
<body>
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="container" style="padding: 0px 0px;width: 100%">
						<div class="row" style="margin: 0 0">
							<div class="col-lg-12 col-sm-12 col-xs-12"
								style="padding: 0px 0px">
								<div class="widget" style="margin-top: 0px;">
									<div
										class="widget-header bordered-bottom bordered-themeprimary detailHead"
										style="position:static;">
										<span class="widget-caption themeprimary"
											style="font-size: 20px">流程配置</span> <span
											class="widget-caption themeprimary"
											style="font-size: 15px;margin-top: 2px" id="titleflowName">
											<c:choose>
												<c:when test="${fn:length(flowConfig.flowName)>16 }">
						                        	--${fn:substring(flowConfig.flowName,0,16)}..
					                       		</c:when>
												<c:otherwise>
				                        			--${flowConfig.flowName}
				                        		</c:otherwise>
											</c:choose>
										</span>
										<div class="widget-buttons ps-toolsBtn">
											<!-- 判断显示按钮 -->
											<!-- <a href="javascript:void(0)" class="blue"
												id="nextExecutor" title="流程委托"> <i
												class="fa fa-h-square"></i>委托
											</a> -->
										</div>
									</div>
									<!--Widget Header-->
									<div class="widget-body no-shadow">
										<div class="widget-main ">
											<div class="tabbable">
												<div class="tab-content tabs-flat">
													<div class="widget radius-bordered">
														<div class="widget-body no-shadow">
															<div class="tickets-container bg-white">
																<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;">
						                                             <li id="flowModelStepMenuLi" class="active">
						                                                 <a data-toggle="tab" href="javascript:void(0)">步骤配置</a>
						                                             </li>
						                                             <li id="flowModelbaseMenuLi">
						                                                 <a data-toggle="tab" href="javascript:void(0)">流程配置</a>
						                                             </li>
																	<li id="logsMenuLi">
																		<a data-toggle="tab" href="javascript:void(0)">日志记录</a>
																	</li>
																	<li id="mappingMenuLi">
																		<a data-toggle="tab" href="javascript:void(0)">数据映射</a>
																	</li>
						                                    	</ul>
						                                    	 <div class="tab-content tabs-flat">
						                                    	 	<iframe id="otherFlowModelAttrIframe" class="layui-layer-load"
																		src="/flowDesign/editFlowStepConfig?sid=${param.sid}&activityMenu=${activityMenu}&flowId=${flowConfig.id}"
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
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->
	</div>
</body>
</html>