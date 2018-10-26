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
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/talk/talkStr.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/institution/instituTalk.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
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
		"busId" : "${busId}"
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
	});

	var sid = '${param.sid}';
	var autoCompleteCallBackVar =null;
$(function(){
	initCard(EasyWin.sid);
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		datatype:{
			"input":function(gets,obj,curform,regxp){
				var str = $(obj).val();
				if(str){
					var count = str.replace(/[^\x00-\xff]/g,"**").length;
					var len = $(obj).attr("defaultLen");
					if(count>len){
						return false;
					}else{
						return true;
					}
				}else{
					return false;
				}
			}
		},
		callback:function (form){
			//提交前验证是否在上传附件
			return sumitPreCheck(null);
		},
		showAllError : true
	}); 

	//制度描述的图片查看
	var imgArray = $("#comment").find("img");
	$.each(imgArray,function(index,item){
		var src = $(this).attr("src");
		$(this).click(function(){
			window.top.viewOrgByPath(src);
		});
	});
});
//菜单切换
$(function(){
	//制度详情
	$("#institubaseMenuLi").click(function(){
		$("#myTab11").hide();
		$("#otherinstituAttrIframe").css("display","none");
		$("#instituBase").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$("#institubaseMenuLi").attr("class","active");
	});
	//讨论
	$("#instituTalkMenuLi").click(function(){
		$("#otherinstituAttrIframe").css("display","block");
		$("#instituBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#instituTalkMenuLi").attr("class","active");
		$("#otherinstituAttrIframe").attr("src","/institution/instituTalkPage?sid="+EasyWin.sid+"&pager.pageSize=10&institutionId="+EasyWin.busId);
	});
	//制度浏览记录
	$("#instituViewRecord").click(function(){
		$("#otherinstituAttrIframe").css("display","block");
		$("#instituBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#instituViewRecord").attr("class","active");
		$("#otherinstituAttrIframe").attr("src","/institution/listViewRecord?sid="+EasyWin.sid+"&instituId=${busId}&ifreamName=otherinstituAttrIframe&pager.pageSize=10");
	});
	//日志
	$("#instituLogs").click(function(){
		$("#otherinstituAttrIframe").css("display","block");
		$("#instituBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#instituLogs").attr("class","active");
		$("#otherinstituAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${institution.id}&busType=040&ifreamName=otherinstituAttrIframe");
	});
});
//查看评论
function viewAllTalk(){
	$("#myTab11").show();
	$("#otherinstituAttrIframe").css("display","block");
	$("#instituBase").css("display","none");
	$("#myTab11").find("li").removeAttr("class");
	$("#instituTalkMenuLi").attr("class","active");
	$("#otherinstituAttrIframe").attr("src","/institution/instituTalkPage?sid="+EasyWin.sid+"&pager.pageSize=10&institutionId="+EasyWin.busId);
}
//查看记录
function viewRecord(){
	$("#myTab11").show();
	$("#otherinstituAttrIframe").show();
	$("#instituBase").hide();
	$("#myTab11").find("li").removeAttr("class");
	$("#instituViewRecord").attr("class","active");
	$("#otherinstituAttrIframe").attr("src","/institution/listViewRecord?sid="+EasyWin.sid+"&instituId=${busId}&ifreamName=otherinstituAttrIframe&pager.pageSize=10");
}
function viewLogs(){
	$("#myTab11").show();
	$("#otherinstituAttrIframe").show();
	$("#instituBase").hide();
	$("#myTab11").find("li").removeAttr("class");
	$("#instituLogs").attr("class","active");
	$("#otherinstituAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${institution.id}&busType=040&ifreamName=otherinstituAttrIframe");
}
</script>
<style type="text/css">
p{
    line-height: 100%;

}
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
					 <span class="widget-caption themeprimary" style="font-size: 20px!important;margin-top: 2px">制度</span>
						<span class="widget-caption themeprimary" style="font-size: 18px!important;margin-top: 2px">
							<c:choose>
								<c:when test="${fn:length(institution.title)>20 }">
	                        	--${fn:substring(institution.title,0,20)}..
                       		</c:when>
								<c:otherwise>
                       			--${institution.title}
                       		</c:otherwise>
							</c:choose>
						</span>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭"> <i class="fa fa-times themeprimary"></i>
							</a>
						</div>
					</div>
					<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;padding:0 12px;">
						<div class="widget-body no-shadow">
							<div class="widget-main ">
								<div class="tabbable">
									<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;display:none" >
										<li class="active" id="institubaseMenuLi">
											<a data-toggle="tab" href="javascript:void(0)">制度详情</a>
										</li>
										<li id="instituTalkMenuLi">
											<a data-toggle="tab" href="javascript:void(0)">留言</a>
										</li>
										<li id="instituViewRecord">
											<a data-toggle="tab" href="javascript:void(0)">查看记录</a>
										</li>
										<li id="instituLogs">
											<a data-toggle="tab" href="javascript:void(0)">操作日志</a>
										</li>
									</ul>
								</div>

								<div class=" tabs-flat">
									<div id="instituBase">
										<jsp:include page="./instituBase_view.jsp"></jsp:include>
									</div>
									<iframe id="otherinstituAttrIframe" style="display:none;" class="layui-layer-load" src="" border="0" frameborder="0" allowTransparency="true" noResize scrolling="no" width=100% height=100%
										vspale="0"></iframe>
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
