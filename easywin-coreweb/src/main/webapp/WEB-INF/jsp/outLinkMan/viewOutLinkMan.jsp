<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}

//任务属性菜单切换
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//详情
	$("#olmDetail").click(function(){
		$("#otherAttrIframe").css("display","none");
		$("#olmBase").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$("#olmDetail").attr("class","active");
	});
	//留言
	$("#olmTalk").click(function(){
		$("#otherAttrIframe").css("display","block");
		$("#olmBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#olmTalk").attr("class","active");
		$("#otherAttrIframe").attr("src","/outLinkMan/olmTalkPage?sid=${param.sid}&pager.pageSize=10&outLinkManId=${olm.id}");
	});
	//操作日志
	$("#olmLog").click(function(){
		$("#otherAttrIframe").css("display","block");
		$("#olmBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#olmLog").attr("class","active");
		$("#otherAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${olm.id}&busType=068&ifreamName=otherAttrIframe");
	});
	
	
});
</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
						
                        <span class="widget-caption themeprimary" style="font-size: 20px">外部联系人</span>
                        <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px">
                        	<c:choose>
                        		<c:when test="${fn:length(olm.linkManName)>20 }">
		                        	--${fn:substring(olm.linkManName,0,20)}..
                        		</c:when>
                        		<c:otherwise>
                        			--${olm.linkManName}
                        		</c:otherwise>
                        	</c:choose>
                        </span>
                      
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->        
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                           <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;">
                                             <li id="olmDetail" class="active">
                                                 <a data-toggle="tab" href="javascript:void(0)">联系人详情</a>
                                             </li>
                                             <li id="olmTalk">
                                                 <a data-toggle="tab" href="javascript:void(0)">留言<c:if test="${talkCount > 0}"><span style="color:red;font-weight:bold;">（${talkCount}）</span></c:if></a>
                                             </li>
                                             <li id="olmLog">
                                                 <a data-toggle="tab" href="javascript:void(0)">操作日志</a>
                                             </li>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<div id="olmBase" style="display:block;">
												<jsp:include page="./outLinkManBase_view.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherAttrIframe" style="display:none;" class="layui-layer-load"
												src=""
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
	</form>
    <script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
