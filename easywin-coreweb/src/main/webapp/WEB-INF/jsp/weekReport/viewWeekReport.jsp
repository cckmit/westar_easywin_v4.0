<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link href="/static/css/timeline.css" rel="stylesheet">
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script>
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
$(function(){
	
	//设置滚动条高度
	var height = $(window).height() - 40;
	$("#contentBody").css("height", height + "px");

	//汇报内容
	$("#reportContentMenuLi").click(function(){
		$("#otherIframe").css("display","none");
		$("#reportContent").css("display","block");
		$(this).parent().parent().find("li").removeAttr("class");;
		$(this).parent().attr("class","active");
	});
	
	$("#weekTalk").click(function(){
		$("#otherIframe").css("display","block");
		$("#reportContent").css("display","none");
		$(this).parent().parent().find("li").removeAttr("class");;
		$(this).parent().attr("class","active");
		//留言
		$("#otherIframe").attr("src", "/weekReport/weekRepTalkPage?sid=${param.sid}&pager.pageSize=10&weekReportId=${weekReport.id}")
	});
	$("#headWeekTalk").click(function(){
		$("#otherIframe").css("display","block");
		$("#reportContent").css("display","none");
		$("#weekTalk").parent().parent().find("li").removeAttr("class");;
		$("#weekTalk").parent().attr("class","active");
		//留言
		$("#otherIframe").attr("src", "/weekReport/weekRepTalkPage?sid=${param.sid}&pager.pageSize=10&weekReportId=${weekReport.id}&talkFocus='1'")
	});
	$("#weekLog").click(function(){
		$("#otherIframe").css("display","block");
		$("#reportContent").css("display","none");
		$(this).parent().parent().find("li").removeAttr("class");;
		$(this).parent().attr("class","active");
		//周报日志
		$("#otherIframe").attr("src", "/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${weekReport.id}&busType=006&ifreamName=otherIframe");
	});
	$("#weekFile").click(function(){
		$("#otherIframe").css("display","block");
		$("#reportContent").css("display","none");
		$(this).parent().parent().find("li").removeAttr("class");;
		$(this).parent().attr("class","active");
		//周报附件
		$("#otherIframe").attr("src", "/weekReport/weekRepFilePage?sid=${param.sid}&pager.pageSize=10&weekReportId=${weekReport.id}")
	});
	//查看记录
	$("#weekViewRecord").click(function(){
		$("#otherIframe").css("display","block");
		$("#reportContent").css("display","none");
		$(this).parent().parent().find("li").removeAttr("class");;
		$(this).parent().attr("class","active");
		$("#otherIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${weekReport.id}&busType=006&ifreamName=otherIframe");
	});
	
});
</script>
</head>
<body>
<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget no-margin-bottom" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption">
	                       	 <strong class="ps-font-size padding-right-10">
	                       	 	<a>${weekReport.userName} </a>
	                        	${weekReport.year}年第${weekReport.weekNum}周周报
                        	</strong>
                        	<span>${fn:substring(firstDayOfWeek,5,fn:length(firstDayOfWeek)) } ~ 
							${fn:substring(lastDayOfWeek,5,fn:length(lastDayOfWeek))}</span>
                        </span>
                        <div class="widget-buttons ps-toolsBtn">
                        	<!-- <input type="reset" class="btn btn-default btn-sm"
												id="headWeekTalk" value="留言" /> -->
							<a href="javascript:void(0);" class="purple" id="headWeekTalk" title="留言"><i class="fa fa-comments"></i>留言</a>
                        </div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
										
						</div>
                     </div>
                     <!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->       
                      <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;padding:0 12px;">
                           <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat">
                                            <li class="active">
                                            	<a href="javascript:void(0)" id="reportContentMenuLi" data-toggle="tab">汇报内容</a>
											</li>
                                            <li>
                                            	<a href="javascript:void(0)" id="weekTalk" data-toggle="tab">留言<c:if test="${weekReport.talkNum > 0}"><span style="color:red;font-weight:bold;">（${weekReport.talkNum}）</span></c:if></a>
											</li>
											<li>
												<a href="javascript:void(0)" data-toggle="tab" id="weekLog">周报日志</a>
											</li>
											<li>
												<a href="javascript:void(0)" data-toggle="tab" id="weekFile">周报文档<c:if test="${weekReport.fileNum > 0}"><span style="color:red;font-weight:bold;">（${weekReport.fileNum}）</span></c:if></a>
											</li>
											<li>
                                                 <a data-toggle="tab" href="javascript:void(0)" id="weekViewRecord">阅读情况</a>
                                             </li> 
											<%--<li>--%>
                                                 <%--<a data-toggle="tab" href="javascript:void(0)" id="weekViewRecord">最近查看</a>--%>
                                             <%--</li> --%>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<div id="reportContent" style="display:block;">
												<jsp:include page="./reportBase_view.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherIframe" style="display:none;" class="layui-layer-load"
												border="0" frameborder="0" allowTransparency="true"
												noResize  scrolling="yes" width=100% height=100% vspale="0"></iframe>
                                    	 </div>
                                	</div>
                            	</div>
                            </div> 
                        </div>
					</div>
				</div>
			</div>
		</div>
    <script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>

