<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.PaginationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>

<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/plugins/artDialog/jquery.artDialog.js?skin=aero"></script>
<script type="text/javascript" src="/static/plugins/artDialog/plugins/iframeTools.js"></script>
 <!-- Custom styles for this template -->
 <link href="/static/css/style.css" rel="stylesheet">
 <link href="/static/css/style-responsive.css" rel="stylesheet" />
 <script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
 <script type="text/javascript">

	$(document).ready(function() {
		resizeVoteH('otherIframe1');
		//初始化名片
		initCard('${param.sid}');
	});
	</script>
	<style type="text/css">
	.timelineAdd:before {
	    bottom: 65px;
	}
	.timeline:before {
    top: 0px;
}
	</style>
</head>
<body style="background-color:#FFFFFF;">
<%--当前页面起始数 --%>
<c:set var="offsetVal" value="<%=PaginationContext.getOffset() %>"></c:set>
<%--查询结果总条数 --%>
<c:set var="totalCountVal" value="<%=PaginationContext.getTotalCount() %>"></c:set>
<%--每页显示多少条 --%>
<c:set var="pageSizeVal" value="<%=PaginationContext.getPageSize() %>"></c:set>
<%--是否为最后一页 --%>
<c:set var="isLastPage" value="${(totalCountVal-totalCountVal mod pageSizeVal) eq offsetVal }"></c:set>
<!--main content start-->
        <section id="main-content" style="width:100%;margin-left:0px">
            <section class="wrapper" style="padding:15px;margin-top: 0px;display:inline-block;width:100%">
                <!-- page start-->

                <div class="row">
                    <div class="col-sm-12" style="margin-top: -50px">
                        <div class="timeline ${isLastPage?'timelineAdd':''}" style="width: 60px">
                            <c:choose>
								<c:when test="${not empty listMsgShareLog}">
								
									<c:choose>
		                         		<c:when test="${offsetVal eq 0}">
		                         			<article class="timeline-item alt">
				                                <div class="text-right">
				                                    <div class="time-show first">
				                                        <a href="javascript:void(0)" class="btn btn-primary" style="background-color: #56C9F5;border: 0px;cursor: default;float: left;width: 70px;margin-top: -50px">今日</a>
				                                    </div>
				                                </div>
				                            </article>
		                         		</c:when>
		                         		<c:otherwise>
		                         			<article class="timeline-item alt">
				                                <div class="text-right">
				                                    <div class="time-show">
				                                        <a href="javascript:void(0)" class="btn btn-primary" style="background-color: #56C9F5;border: 0px;cursor: default;margin-top: 20px;float: left;width: 70px;margin-top: -50px">接上一页</a>
				                                    </div>
				                                </div>
				                            </article>
		                         		</c:otherwise>
		                         	</c:choose>
                         	
                         	
									<c:forEach items="${listMsgShareLog}" var="listMsgShareLogVo" varStatus="vs">
										<c:choose>
											<c:when test="false">
					                            <article class="timeline-item alt">
					                                <div class="timeline-desk">
					                                    <div class="panel">
					                                        <div class="panel-body" style="background-color: #f0f0f0">
					                                            <span class="arrow-alt" style="border-left-color:#f0f0f0 !important"></span>
					
					                                            <span class="timeline-icon online-head" data-container="body" data-toggle="popover" 
					                                            data-user='${listMsgShareLogVo.userId}' data-busId='${listMsgShareLogVo.msgId}' data-busType='1'>
																		<img src="/downLoad/userImg/${listMsgShareLogVo.comId}/${listMsgShareLogVo.speaker}?sid=${param.sid}" title="${listMsgShareLogVo.speakerName}"></img>
							                                    </span>
																	<p>
																		<span style="font-size:14px !important;color:#4F4F4F;">${listMsgShareLogVo.content}</span>
																	</p>
																	<p>
																		<span class="log-time" style="float:left;">操作人：</span>
																		<span class="log-time" style="font-size:10px;color:#1981EB;float:left;">${listMsgShareLogVo.speakerName }&nbsp;</span>
																		<span class="log-time" style="float:left;">操作时间：</span>
																		<span class="log-time" style="float:left;color:#1981EB;">${listMsgShareLogVo.recordCreateTime }</span>
																	</p>
																 </div>
					                                    </div>
					                                </div>
					                            </article>
											</c:when>
											<c:otherwise>
					                            <article class="timeline-item " style="width: 60px">
					                                <div class="timeline-desk">
					                                    <div class="panel">
					                                        <div class="panel-body" style="background-color: #f0f0f0;width: 300px">
					                                            <span class="arrow" style="border-right-color:#f0f0f0 !important"></span>
					                                            <span class="timeline-icon online-head" data-container="body" data-toggle="popover" 
					                                            data-user='${listMsgShareLogVo.userId}'  data-busId='${listMsgShareLogVo.msgId}' data-busType='1'>
																	<img src="/downLoad/userImg/${listMsgShareLogVo.comId}/${listMsgShareLogVo.speaker}?sid=${param.sid}" title="${listMsgShareLogVo.speakerName}"></img>
																	<p>
																		<span style="font-size:14px !important;color:#4F4F4F;">${listMsgShareLogVo.content}</span>
																	</p>
																	<p>
																		<span class="log-time" style="float:left;">操作人：</span>
																		<span class="log-time" style="font-size:10px;color:#1981EB;float:left;">${listMsgShareLogVo.speakerName }&nbsp;</span>
																		<span class="log-time" style="float:left;">操作时间：</span>
																		<span class="log-time" style="float:left;color:#1981EB;">${listMsgShareLogVo.recordCreateTime }</span>
																	</p>
					                                        </div>
					                                    </div>
					                                </div>
					                            </article>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:when>
							</c:choose>
									
                        </div>
                    </div>
                </div>
                <!-- page end-->
            </section>
        </section>
	<tags:pageBar url="/msgShare/msgShareLogPage" maxIndexPages="3"></tags:pageBar>
	<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
