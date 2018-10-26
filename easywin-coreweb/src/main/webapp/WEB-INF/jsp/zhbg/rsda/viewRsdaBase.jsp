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
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script src="/static/js/taskJs/taskCenter.js" type="text/javascript" charset="utf-8"></script>
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
//autoCompleteCallBack回调对象标识
var autoCompleteCallBackVar =null;
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
});
$(document).ready(function() {
    $(document).keydown(function(e) {
        if (e.keyCode == 13) {
        	return false;
        }
    });
 });
//任务属性菜单切换
$(function(){
	$("#myTab11").on("click","li",function(){
		var index  = $(this).index();
		if(index == 0){
			$("#otherRsdaBaseIframe").css("display","none");
			$("#rsdaBase").css("display","block");
		}else{
			$("#otherRsdaBaseIframe").css("display","block");
			$("#rsdaBase").css("display","none");
		}
		$(this).parent().find("li").removeAttr("class");
		$(this).addClass("active");
		if(index == 1){//工作经历
			$("#otherRsdaBaseIframe").attr("src","/jobHistory/listPagedUserJobHistory?sid=${param.sid}&pager.pageSize=10&userId=${rsdaBase.userId}&ifreamName=otherRsdaBaseIframe");
		}else if(index == 2){//奖惩信息
			$("#otherRsdaBaseIframe").attr("src","/rsdaInc/listPagedUserRsdaInc?sid=${param.sid}&pager.pageSize=10&userId=${rsdaBase.userId}&ifreamName=otherRsdaBaseIframe");
		}else if(index == 3){//人事调动
			$("#otherRsdaBaseIframe").attr("src","/rsdaTrance/listPagedUserRsdaTrance?sid=${param.sid}&pager.pageSize=10&userId=${rsdaBase.userId}&ifreamName=otherRsdaBaseIframe");
		}else if(index == 4){//离职信息
			$("#otherRsdaBaseIframe").attr("src","/rsdaLeave/listPagedUserRsdaLeave?sid=${param.sid}&pager.pageSize=10&userId=${rsdaBase.userId}&ifreamName=otherRsdaBaseIframe");
		}else if(index == 5){//复职信息
			$("#otherRsdaBaseIframe").attr("src","/rsdaResume/listPagedUserRsdaResume?sid=${param.sid}&pager.pageSize=10&userId=${rsdaBase.userId}&ifreamName=otherRsdaBaseIframe");
		}
	})
});
</script>
<style type="text/css">
	.relateClz{
		cursor: pointer;
	}
	#comment img{
		max-width: 150px;
		height: auto;
	}
</style>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">人事档案--${rsdaBase.userName}</span>
                        <div class="widget-buttons ps-toolsBtn">
                      			<!-- 任务未办结，都可以留言 -->
							<a href="javascript:void(0)" class="blue" title="保存">
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-40" id="contentBody" 
                     	style="overflow: hidden;overflow-y:scroll;padding:0 0px;">
                           <div class="widget-body no-shadow" style="padding-right: 0">
                           		<div class="widget-main no-padding">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11">
                                             <li class="active" >
                                                 <a data-toggle="tab" href="javascript:void(0)">档案信息</a>
                                             </li>
                                             <li>
                                                 <a data-toggle="tab" href="javascript:void(0)">工作经历</a>
                                             </li>
                                             <li>
                                                 <a data-toggle="tab" href="javascript:void(0)">奖惩信息</a>
                                             </li>
                                             <li>
                                                 <a data-toggle="tab" href="javascript:void(0)">人事调动</a>
                                             </li>
                                             <li>
                                                 <a data-toggle="tab" href="javascript:void(0)">离职信息</a>
                                             </li>
                                             <li>
                                                 <a data-toggle="tab" href="javascript:void(0)">复职信息</a>
                                             </li>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat no-padding-left no-padding-right">
                                    	 	<div id="rsdaBase" style="display:block;">
												<jsp:include page="./rsdaBase_view.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherRsdaBaseIframe" style="display:none;" class="layui-layer-load"
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
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
