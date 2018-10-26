<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.BiaoQing"%>
<%@page import="com.westar.core.web.BiaoQingContext"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@page import="com.westar.base.util.DateTimeUtil"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title>${file.filename}</title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script> 
$(function() {
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//初始化页面留言
	$("#otherIframe").css("display","block");
	$("#videoTalkMenuLi").parent().find("li").removeAttr("class");
	$("#videoTalkMenuLi").attr("class","active");
	$("#otherIframe").attr("src","/onlineLearn/videoTalkPage?sid=${param.sid}&pager.pageSize=8&fileId=${param.fileId}");
	
	
	//留言
	$("#videoTalkMenuLi").click(function(){
		$("#otherIframe").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active");
		$("#otherIframe").attr("src","/onlineLearn/videoTalkPage?sid=${param.sid}&pager.pageSize=8&fileId=${param.fileId}");
	});
	
	//学习记录
	$("#videoViewRecord").click(function(){
		$("#otherIframe").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		$("#otherIframe").attr("src", "/common/listViewRecord?sid=${param.sid}&busId=${param.fileId}&busType=023&ifreamName=otherIframe")
	});
	
});
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
</script>
</head>
<body style="background-color: #fff">
              <div style="position:fixed;left:10px;top:20px;width: 70%;height: 90%;">
			    
			</script>
				<script type="text/javascript" src="/static/plugins/player/sewise.player.min.js"></script>
				<script type="text/javascript">
					SewisePlayer.setup({
			            server: "vod",
			            type:"mp4",
			            skin: "vodFoream",
			            lang: 'zh_CN',
			            title:'${file.filename}',
			            autoStart:"true",
			            videourl: '${url}',
			        });
			    </script>
	        </div>
		<div style="position:absolute;right:0px;top:10px;;width: 28.5%;height: 90%;overflow-y:auto" id="contentBody">
			 <div class="widget-body no-shadow padding-top-5 no-padding-bottom" id="viewerTab">
             	<div class="widget-main ">
                	<div class="tabbable">
                    	<ul class="nav nav-tabs tabs-flat" id="myTab11">
                        	<li class="active" id="videoTalkMenuLi"><a href="javascript:void(0)"
								data-toggle="tab">留言</a></li> 
							<li id="videoViewRecord"><a data-toggle="tab"
								href="javascript:void(0)">学习记录</a></li>
                        </ul>
                        
                         <div class="tab-content tabs-flat no-padding bg-white" id="ifreamSrc" >
                              <iframe style="width:100%;" id="otherIframe"
								border="0" frameborder="0" allowTransparency="true"
								noResize  scrolling="no" vspale="0"></iframe>
                         </div>
                    </div>
                 </div>
              </div>
         </div>
</body>
</html>
