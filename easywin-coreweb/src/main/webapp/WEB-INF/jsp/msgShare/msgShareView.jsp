<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<title><%=SystemStrConstant.TITLE_NAME%></title>
</head>
<script type="text/javascript">
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}

//样式设置
function setStyle(){
	//文本框绑定回车提交事件
	$(".txtTalk").autoTextarea({minHeight:55,maxHeight:160});  
}



//任务属性菜单切换
$(function(){
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	$("#contentBody1").css("height",height+"px");
	$("#contentBody3").css("height",height + 40+"px");
	
	//初始化名片
	initCard('${param.sid}');
	//页面刷新
	$("#refreshImg").click(function(){
		var win = art.dialog.open.origin;//来源页面
		// 如果父页面重载或者关闭其子对话框全部会关闭
		win.location.reload();
	});
	
	//分享详情
	$("#msgDetailMenuLi").click(function(){
		$("#otherIframe1").css("display","none");
		$("#msgDetail").css("display","block");
		//$("#upDetail").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active");
	});
	
	//初始化分享详情
	$("#otherIframe1").css("display","none");
	$("#msgDetail").css("display","block");
	$("#upDetail").css("display","none");
	$(this).parent().find("li").removeAttr("class");
	//初始化页面留言
	$("#otherIframe").css("display","block");
	//$("#msgDetail").css("display","none");
	$(this).parent().find("li").removeAttr("class");
	$(this).attr("class","active");
	$("#otherIframe").attr("src","/msgShare/msgShareTalkPage?sid=${param.sid}&pager.pageSize=8&msgId=${msg.id}");
	
	
	//维护记录
	$("#msgShareTalkMenuLi").click(function(){
		$("#otherIframe").css("display","block");
		//$("#msgDetail").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active");
		$("#otherIframe").attr("src","/msgShare/msgShareTalkPage?sid=${param.sid}&pager.pageSize=8&msgId=${msg.id}");
	});
	//维护记录
	$("#headMsgTalk").click(function(){
		$("#otherIframe").css("display","block");
		//$("#msgDetail").css("display","none");
		$("#msgShareTalkMenuLi").parent().find("li").removeAttr("class");
		$("#msgShareTalkMenuLi").attr("class","active");
		$("#otherIframe").attr("src","/msgShare/msgShareTalkPage?sid=${param.sid}&pager.pageSize=8&msgId=${msg.id}&talkFocus='1'");
	});
	//分享信息日志
	$("#msgShareLogMenuLi").click(function(){
		$("#otherIframe1").css("display","block");
		$("#msgDetail").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active");
		$("#otherIframe1").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${msg.id}&busType=1&ifreamName=otherIframe1");
	});
	//分享信息文档
	$("#msgShareUpfileMenuLi").click(function(){
		$("#otherIframe1").css("display","block");
		$("#msgDetail").css("display","none");
		//$("#upDetail").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active");
		$("#otherIframe1").attr("src","/msgShare/msgShareUpfilePage?sid=${param.sid}&pager.pageSize=12&msgId=${msg.id}");
	});
	//分享信息文档
	$("#shareViewRecord").click(function(){
		$("#otherIframe").css("display","block");
		//$("#msgDetail").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//问答附件
		$("#otherIframe").attr("src", "/common/listViewRecord?sid=${param.sid}&busId=${msg.id}&busType=1&ifreamName=otherIframe")
	});
});
</script>
<body>


	<div class="container" style="padding: 0px 0px;width: 100%">

		<input id="ansNum" type="hidden" value="${ques.ansNum }" /> <input
			id="cnAns" type="hidden" value="${ques.cnAns}" />

		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px;">
				<div class="widget no-margin-bottom" style="margin-top: 0px;">
					<div
						class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px !important;float:left;">分享详情</span>
						<span class="widget-caption pull-left margin-left-10 ">
							${msg.creatorName}：${fn:substring(msg.recordCreateTime,0,16)}
						</span>
						<div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue"
							attentionState="${msg.attentionState}" busType="1"  busId="${msg.id}"  describe="1" iconSize="sizeMd"
								onclick="changeAtten('${param.sid}',this)">
								<i
								class="fa ${msg.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
								${msg.attentionState==0?'关注':'取消'} </a>
								
							<!-- <a href="javascript:void(0)" class="purple" id="headMsgTalk" title="留言">
								<i class="fa fa-comments"></i>留言
							</a> -->
						</div>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i> </a>
						</div>
					</div>
					
					<div class="widget-body margin-top-10" id="contentBody3"
						style="overflow: hidden;overflow-y:auto;width:100%;box-shadow: none;float: left;">
					<div class="widget-body margin-top-10" id="contentBody1"
						style="width:65%;box-shadow: none;float: left;">
						<ul class="nav nav-tabs tabs-flat">
							<li class="active" id="msgDetailMenuLi"><a
								href="javascript:void(0)" data-toggle="tab">分享详情</a></li>
							<!-- <li class="active" id="msgShareTalkMenuLi"><a href="javascript:void(0)"
								data-toggle="tab">留言</a></li>  -->
							<li id="msgShareUpfileMenuLi"><a
								href="javascript:void(0)" data-toggle="tab">信息文档
									<c:if test="${listMsgShareTalkUpfile.size() > 0}">
										<span style="color:red;font-weight:bold;">（${listMsgShareTalkUpfile.size()}）</span>
										<%-- <span class="badge" id="allNoReadNum" style="background-color: red;margin-top: -5px">${listMsgShareTalkUpfile.size()}</span> --%>
									</c:if>
								</a>
							</li>
							<li id="msgShareLogMenuLi"><a href="javascript:void(0)"
								data-toggle="tab">日志记录</a></li>
							<!-- <li id="shareViewRecord"><a data-toggle="tab"
								href="javascript:void(0)">阅读情况</a></li> -->
						</ul>
						<div class="tab-content tabs-flat" style="background-color: #FFFFFF;">
							<div id="msgDetail" style="display:block;">
								<jsp:include page="./msgDetail_view.jsp"></jsp:include>
							</div>
							<%-- <div id="upDetail" style="display:none;">
								<jsp:include page="./msgShareUpfile.jsp"></jsp:include>
							</div> --%>
							<iframe id="otherIframe1" src="" style="display:none;"
								border="0" frameborder="0" allowTransparency="true" noResize
								scrolling="no" width=100% height=100% vspale="0"></iframe>
						</div>
							
					</div>
					
					
					<!--Widget Header-->
					<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
					<div class="widget-body margin-top-10" id="contentBody"
						style=" width:35%;box-shadow: none;float: left;">
							<ul class="nav nav-tabs tabs-flat">
								<!-- <li class="active" id="msgDetailMenuLi"><a
									href="javascript:void(0)" data-toggle="tab">分享详情</a></li> -->
								<li class="active" id="msgShareTalkMenuLi"><a href="javascript:void(0)"
									data-toggle="tab">留言</a></li> 
								<!-- <li id="msgShareLogMenuLi"><a href="javascript:void(0)"
									data-toggle="tab">日志记录</a></li> -->
								<!-- <li id="msgShareUpfileMenuLi"><a
									href="javascript:void(0)" data-toggle="tab">信息文档</a></li> -->
								<li id="shareViewRecord"><a data-toggle="tab"
									href="javascript:void(0)">阅读情况</a></li>
							</ul>
							
							<div class="tab-content tabs-flat" style="padding: 0">
								<iframe id="otherIframe" src="" style="display:none;"
									border="0" frameborder="0" allowTransparency="true" noResize
									scrolling="no" width=100% height=100% vspale="0"></iframe>
							
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

