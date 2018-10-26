<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
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
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>


<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/announ/viewAnnoun.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/talk/talkStr.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/announ/announTalk.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
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
		"busId" : "${announ.id}"
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
		//$("#contentBody1").css("height", height + "px");
		$("#contentBody2").css("height", height + "px");
	});
	var sid = '${param.sid}';

	function viewRecord(){
		$("#otherannounAttrIframe").css("display","block");
		//$("#announBase").css("display","block");
		$("#announViewRecord").parent().find("li").removeAttr("class");
		$("#announViewRecord").attr("class","active");
		$("#otherannounAttrIframe").attr("src","/common/listViewRecord?sid="+EasyWin.sid+"&busId="+EasyWin.busId+"&busType=039&ifreamName=otherannounAttrIframe&pager.pageSize=10");
		$("#contentBody2").scrollTop($("#contentBody1").height() + 40)
		
	}
</script>
<style type="text/css">
.relateClz {
	cursor: pointer;
}

#comment img {
	max-width: 150px;
	height: auto;
}

.ps-listline {
	line-height: 30px;
}
.ws-share{float: right;}
.ws-btnBlue{background:#1c98dc; border:0;}
.ws-otherBox{padding:10px 0;}
.ws-plugs,.ws-meh,.ws-range,.ws-remind{float: left;margin:5px 10px 0;}
.btn {
	display:inline-block;
	padding:6px 12px;
	margin-bottom:0;
	font-weight:400;
	line-height:1.42857143;
	text-align:center;
	white-space:nowrap;
	vertical-align:middle;
	cursor:pointer;
	-webkit-user-select:none;
	-moz-user-select:none;
	-ms-user-select:none;
	user-select:none;
	background-image:none;
	border:1px solid transparent;
	border-radius:4px
}
.right-sidebar {background: #fff;border:1px solid #dde1e7;width:300px;z-index: 1000;position: fixed;
right:0;top:75px;padding:10px;height:90%;overflow-y: scroll;}
.ws-otherBox{padding:10px 0;}
.ws-plugs,.ws-meh,.ws-range,.ws-remind{float: left;margin:5px 10px 0;}
.ws-share{float: right;}
.fa-chain,.fa-meh-o{font-size: 15px;}
.ws-shareContent{padding-top:10px;}
.ws-shareBox{margin-top:10px; margin-bottom: 10px; border-bottom: 1px solid #dedede; position: relative;}
.ws-shareBox2{margin-left:50px;}
.ws-shareBox3 .shareText{margin-left:0;}
.ws-clear{clear: both;}
.ws-newsTit{height:30px; overflow: hidden;border-bottom:1px solid #dedede;}
.shareHead{width:40px; position: absolute;top:5px;left:10px;}
.shareHead img{width:40px;height:40px;border-radius:50%;}
.shareText{margin-left:60px;margin-right:8%}
.shareText .p_text{color:#979797;font-size: 12px;}
.shareText r{color:#979797;font-size: 12px;}
.shareText .file_div{margin-top:10px;margin-left:10px;}
.ws-box-list{width:100%;}
.ws-blue{color:#8cbfdc;font-size: 12px;}
.ws-texts{font-size: 12px; margin:0;line-height: 20px;}
.ws-texts a{width:100%;display: inline-block;word-wrap:break-word}
.ws-texts {width:100%;display: inline-block;word-wrap:break-word}
.ws-type{color:#979797;font-size: 12px;padding:5px 0;line-height: 25px;}
.ws-type a{margin-right:20px;}
.ws-voteBox{padding-bottom:5px;}
.ws-voteImg{width:150px;height:90px;border:1px solid #dedede;margin:5px 0;}
.ws-voteImg img{width:100%;height:100%;}
label.ws-check{padding-left:0;}
label.ws-check input{margin-right:5px;}
.radio{margin-top:0;}

</style>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget" style="margin-top: 0px;">

					<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<a href="javascript:void(0)" class="widget-caption blue padding-right-5" attentionState="${announ.attentionState}" busType="039" busId="${announ.id}" describe="0" iconSize="sizeMd"
							onclick="changeAtten('${param.sid}',this)" title="${announ.attentionState==0?'关注':'取消'}"> <i class="fa ${announ.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
						</a>
						<span class="widget-caption themeprimary" style="font-size: 20px!important;margin-top: 2px">
							<c:choose>
								<c:when test="${fn:length(announ.title)>20 }">
		                        	<c:if test="${announ.type == 1}">[通知]</c:if>
		                        	<c:if test="${announ.type == 2}">[通报]</c:if>
		                        	<c:if test="${announ.type == 3}">[决定]</c:if>
		                        	<c:if test="${announ.type == 4}">[其他]</c:if>
		                        	${fn:substring(announ.title,0,20)}..
	                       		</c:when>
								<c:otherwise>
									<c:if test="${announ.type == 1}">[通知]</c:if>
		                        	<c:if test="${announ.type == 2}">[通报]</c:if>
		                        	<c:if test="${announ.type == 3}">[决定]</c:if>
		                        	<c:if test="${announ.type == 4}">[其他]</c:if>
	                       			${announ.title}
	                       		</c:otherwise>
							</c:choose>
						</span>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭"> <i class="fa fa-times themeprimary"></i>
							</a>
						</div>
					</div>
				<div class="widget-body margin-top-40" id="contentBody2" style="overflow: hidden;overflow-y:scroll;padding:0 12px;width: 100%;box-shadow: none;">
					<div class="widget-body margin-top-40" id="contentBody1" style="padding:0 12px;width: 100%;box-shadow: none;">
						<div class="widget-body no-shadow">
							<div class="widget-main ">
								<%-- <div class="tabbable">
									<ul class="nav nav-tabs tabs-flat" id="myTab111" style="padding-top:0;display:block;" >
										<li class="active" id="announbaseMenuLi">
											<a data-toggle="tab" href="javascript:void(0)">公告详情</a>
										</li>
										<!-- <li id="announTalkMenuLi">
											<a data-toggle="tab" href="javascript:void(0)">公告留言</a>
										</li>
										<li id="announViewRecord">
											<a data-toggle="tab" href="javascript:void(0)">阅读情况</a>
										</li> -->
										<li id="announViewRecord">
											<a data-toggle="tab" href="javascript:void(0)">最近查看</a>
										</li>
									</ul>
								</div> --%>

								<div class=" tabs-flat">
									<div id="announBase">
										<jsp:include page="./announBase_view.jsp"></jsp:include>
									</div>
									<!-- <iframe id="otherannounAttrIframe1" style="display:block;" class="layui-layer-load" src="" border="0" frameborder="0" allowTransparency="true" noResize scrolling="no" width=100% height=100%
										vspale="0"></iframe> -->
								</div>
							</div>
						</div>
					</div>
					
					<div class="widget-body margin-top-40" id="contentBody" style="padding:0 12px;width: 100%;box-shadow: none;">
						<div class="widget-body no-shadow">
							<div class="widget-main ">
								<div class="tabbable">
									<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;display:block" >
										<!-- <li class="active" id="announbaseMenuLi">
											<a data-toggle="tab" href="javascript:void(0)">公告详情</a>
										</li> -->
										<li id="announTalkMenuLi">
											<a data-toggle="tab" href="javascript:void(0)">公告留言</a>
										</li>
										<li id="announViewRecord">
											<a data-toggle="tab" href="javascript:void(0)">阅读情况</a>
										</li>
										<%--<li id="announViewRecord">--%>
											<%--<a data-toggle="tab" href="javascript:void(0)">最近查看</a>--%>
										<%--</li>--%>
									</ul>
								</div>

								<div class=" tabs-flat">
									<iframe id="otherannounAttrIframe" style="display:block;" class="layui-layer-load" src="" border="0" frameborder="0" allowTransparency="true" noResize scrolling="no" width=100% height=100%
										vspale="0"></iframe>
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
	<script type="text/javascript">
		var jq11 = $.noConflict(true);
	</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
