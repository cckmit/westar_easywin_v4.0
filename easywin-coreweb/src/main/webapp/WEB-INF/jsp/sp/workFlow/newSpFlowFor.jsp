<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title><%=SystemStrConstant.TITLE_NAME%></title>
<meta name="description" content="Dashboard" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- 框架样式 -->
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8"
	src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" charset="utf-8" src="/static/js/formJs/formCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
   var sid="${param.sid}";//申明一个sid全局变量
   var searchTab = '${param.searchTab}';
   $(function(){
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
   });
   //关闭窗口
   function closeWin(){
   	var winIndex = window.top.layer.getFrameIndex(window.name);
   	closeWindow(winIndex);
   }
</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width:100%">
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-xs-12">
				<div class="widget">
					<div class="widget-header bordered-bottom bordered-themeprimary detailHead" style="height:40px;">
						<i class="widget-icon fa fa-gavel themeprimary"></i> 
						<span
							class="widget-caption themeprimary" style="font-size: 20px">新建审批</span>
						<div class="widget-buttons ps-toolsBtn">
							<!-- <a href="#" class="red"> <i class="fa fa-check-square-o"></i>完成</a> 
							<a href="#" class="blue"> <i class="fa fa-sitemap"></i>委托</a>  <i class="fa fa-th"></i>
							 -->
							 <!--
							 <a href="/form/formListForSelect?sid=${param.sid}&activityMenu=sp_m_2.1&searchTab=21" style="padding:5px 10px;">
								<i class="fa fa-plus"></i>自定义审批
							</a>
							 -->
							 <a href="javascript:void(0)">&nbsp;</a>
						</div>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin();"> <i class="fa fa-times themeprimary"></i></a>
						</div>
					</div>

					<div class="widget-body margin-top-40 panel-group accordion" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
						<!--流程分类显示-->
						<c:choose>
							<c:when test="${not empty listSpFlowType}">
								<c:forEach items="${listSpFlowType}" var="flowType"  varStatus="vsType">
									<c:choose>
										<c:when test="${not empty flowType.listSpFlowModel}">
											<div class="panel panel-default">
					                              <div class="panel-heading">
					                                  <h4 class="panel-title">
					                                      <a class="accordion-toggle ${(empty listHourlySpFlow and vsType.first)?'':'collapsed'} " data-toggle="collapse" data-parent="#contentBody" href="#collapseOnes_${flowType.id}">
					                                          <span class="widget-caption blue">${flowType.typeName}(${fn:length(flowType.listSpFlowModel)})</span>
					                                      </a>
					                                  </h4>
					                              </div>
					                              <div id="collapseOnes_${flowType.id}" class="panel-collapse collapse ${(empty listHourlySpFlow and vsType.first)?'in':''} ">
					                                  <div class="panel-body border-red">
					                                      <ul class="row no-padding form-list-box">
															<c:forEach items="${flowType.listSpFlowModel}" var="flowModel" varStatus="vs">
																<li class="col-xs-6">
																	<div class="sp-form">
																		<a href="#" class="sp-open"></a>
																		<div class="sp-form-content">
																			<span class="number-tag tag-blue">${vs.count}</span>
																			<div>
																				<a href="javascript:void(0);" onclick="startSpFlowAfterCheck(${flowModel.id});" class="sp-form-title">${flowModel.flowName}</a>
																			</div>
																			<p class="sp-form-notes">${empty flowModel.remark ?"暂无描述":flowModel.remark}</p>
																		</div>
																		<div class="sp-other-tag">
																			<a href="javascript:void(0);" onclick="startSpFlowAfterCheck(${flowModel.id});">新建审批</a>
																			<a href="javascript:void(0)" onclick="viewFormMod(${flowModel.formKey})">表单预览</a> <!-- <a href="#">流程查看</a> -->
																		</div>
																	</div>
																</li>
															</c:forEach>
														</ul>
					                                  </div>
					                              </div>
					                          </div>
										</c:when>
									</c:choose>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<div class="blank-content" style="left:50%;top:50%;position: absolute; margin:-150px 0 0 -245px;padding-top:0px;text-align:center;width:488px;">
									<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>您没有可创建的审批流程哦！</h2>
										<p class="description">
											审批能督促团队按照制度规范、提高团队的工作效率。
										</p>
									</div>
									</section>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
			<!--主题颜色设置按钮 end-->
			<script src="/static/assets/js/bootstrap.min.js"></script>
			<script src="/static/assets/js/demo.js" type="text/javascript"
				charset="utf-8"></script>
			<!--Beyond Scripts-->
			<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
