<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
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
<script src="/static/js/jfzbJs/jfzb.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	var EasyWin = {
		"sid" : "${param.sid}",
		"jfzbTypeId":"${jfzb.jfzbTypeId}"
	};
	
	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height() - 40;
		$("#contentBody").css("height", height + "px");
		jfzbOptForm.initForm();
	});
	
</script>
<style type="text/css">
.ps-listline {
	line-height: 30px !important
}
</style>
</head>
<body>
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">查看积分指标</span>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
									<i class="fa fa-times themeprimary"></i>
								</a>
							</div>
						</div>
						<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
						<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
							<div class="widget radius-bordered">
								<div class="widget-header bg-bluegray no-shadow">
									<span class="widget-caption blue">基础配置</span>
									<div class="widget-buttons btn-div-full">
										<a class="ps-point btn-a-full" data-toggle="collapse">
											<i class="fa fa-minus blue"></i>
										</a>
									</div>
								</div>
								<div class="widget-body no-shadow">
									<div class="tickets-container bg-white">
										<ul class="tickets-list">
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-bookmark blue"></i>
													&nbsp;积分类别 
												</div>
												<div class="ticket-user pull-left ps-right-box">
													${jfzb.jfzbTypeName }
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-bookmark blue"></i>
													&nbsp;积分指标 
												</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 500px">
				                                       ${jfzb.leveTwo }
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline" >
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-bookmark blue"></i>
													&nbsp;得分范围
												</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 500px">
													<span class="pull-left" style="display: inline-block;width: 50px;text-align: center;">${jfzb.jfBottom }分</span>
				                                    <span class="pull-left" style="display: inline-block;width: 30px;text-align: center;">至</span>
													<span class="pull-left" style="display: inline-block;width: 50px;text-align: center;">${jfzb.jfTop}分</span>
												</div>
											</li>
											 <li class="ticket-item no-shadow autoHeight no-padding" style="clear: both;">
												<div class="pull-left gray ps-left-text padding-top-10">
													<i class="fa fa-user blue"></i>
													&nbsp;适用范围
												</div>
												<div class="ticket-user pull-left ps-right-box" id="scope" style="min-width: 135px;height: auto;">
													<div id="listJfzbDepScope_selectDiv" class="pull-left margin-top-10" style="max-width: 450px">
														<c:choose>
															<c:when test="${not empty jfzb.listJfzbDepScope }">
																<c:forEach items="${jfzb.listJfzbDepScope}" var="scopeDep" varStatus="vs">
																	<span  style="cursor:pointer;" ondblclick="removeChoose('listJfzbDepScope_select',${scopeDep.depId},this)"
																		class="label label-default margin-right-5 margin-bottom-5 pull-left" title="双击移除">${scopeDep.depName}</span>
																</c:forEach>
															</c:when>
														</c:choose>
													</div>
												</div>
												<div style="clear: both;"></div>
											</li>
											<li class="ticket-item no-shadow autoHeight no-padding" style="clear: both;" >
										    	<div class="pull-left gray ps-left-text padding-top-10">
										    		考核标准
										    	</div>
												<div class="ticket-user pull-left ps-right-box padding-top-10" style="width: 400px;height: auto;">
											  		${jfzb.describe }
												</div> 
												<div class="ps-clear"></div>              
	                                         </li>
	                                         <li class="ticket-item no-shadow autoHeight no-padding" >
										    	<div class="pull-left gray ps-left-text padding-top-10">
										    		备注
										    	</div>
												<div class="ticket-user pull-left ps-right-box padding-top-10" style="width: 400px;height: auto;">
											  		${jfzb.remark}
												</div> 
												<div class="ps-clear"></div>              
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
	</form>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);
	</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
