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
<script src="/static/js/organicSpaceCfg_center/organicSpaceCfg_center.js" type="text/javascript" charset="utf-8"></script>
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
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
});
</script>
</head>
<body>
<form id="subform" class="subform" method="post">
	<div class="container" style="padding: 0px 0px;width:100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header borganicSpaceCfged-bottom borganicSpaceCfged-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">服务购买详情</span>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin();" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div>
                     <!--Widget Header-->
                     <div class="widget-body margin-top-20" id="contentBody" style="overflow-y:auto;position: relative;">
                         <div class="widget-body no-shadow">
							<div class="form-title themeprimary" style="padding:5px 0;">服务明细</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<span>购买团队</span>
										<span class="margin-left-10">${organicSpaceCfg.orgName}</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span>人员范围</span>
										<span class="margin-left-10">${organicSpaceCfg.usersNum}&nbsp;人</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span>存储空间</span>
										<span class="margin-left-10">${organicSpaceCfg.storageSpace}&nbsp;G</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span>升级费用</span>
										<span class="margin-left-10">${organicSpaceCfg.orderCost}&nbsp;元</span>
									</div>
								</div>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span>开始日期</span>
										<span class="margin-left-10">${organicSpaceCfg.startDate}</span>
									</div>
								</div>
								<c:choose>
									<c:when test="${organicSpaceCfg.orgServiceStatus eq 1}">
										<div class="col-sm-12 margin-top-5" style="background-color:green;">
											<div class="form-group" style="color:white;">
												<span>服务到期</span>
												<span class="margin-left-10">${organicSpaceCfg.endDate}</span>
											</div>
										</div>
									</c:when>
									<c:otherwise>
										<div class="col-sm-12 margin-top-5" style="background-color:red;">
											<div class="form-group" style="color:white;">
												<span>服务到期</span>
												<span class="margin-left-10">${organicSpaceCfg.endDate}</span>
											</div>
										</div>
									</c:otherwise>
								</c:choose>
								<div class="col-sm-12 margin-top-5">
									<div class="form-group">
										<span>团队结余</span>
										<span class="margin-left-10">${organicSpaceCfg.orgBalanceMoney}&nbsp;元</span>
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
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>