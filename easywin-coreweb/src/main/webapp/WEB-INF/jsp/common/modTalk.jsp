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
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/mod_talk.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	var pageParam = {
			"sid":"${param.sid}",
			"busId":"${param.busId}",
			"busType":"${param.busType}",
			"ifreamName":"${param.ifreamName}",
			"userInfo":{
				"id":"${userInfo.id}",
				"name":"${userInfo.userName}",
				"comId":"${userInfo.comId}"
			},
			"shareState":"false"
	}

	$(function(){
		modTalkOpt.initEvent();
	});
	
</script>
<style>
.ws-wrap-width .ws-wrap-right{
width: 850px;
}
</style>
</head>
<body style="background-color:#FFFFFF" onload="resizeVoteH('${param.ifreamName}')">
	<%--项目已经办结，不能进行讨论 --%>
		<div class="ws-textareaBox" style="margin-top:10px;" talkParentDiv="-1">
			<textarea id="operaterReplyTextarea_-1" name="operaterReplyTextarea_-1" rows="" cols="" class="form-control" style="height:70px;" placeholder="请输入内容……"></textarea>
			<div class="ws-otherBox">
				<div class="ws-meh">
					<%--表情 --%>
					<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch" onclick="addBiaoQingObj('biaoQingSwitch','biaoQingDiv','operaterReplyTextarea_-1');"></a>
					<div id="biaoQingDiv" class="blk" style="display:none;position:absolute;width:200px;top:100px;z-index:99;left: 15px">
						<!--表情DIV层-->
				        <div class="main">
				            <ul style="padding: 0px">
				            <jsp:include page="/biaoqing.jsp"></jsp:include>
				            </ul>
				        </div>
			    	</div>
				</div>
				<div class="ws-plugs">
					<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_-1','${param.sid}');" title="常用意见"></a>
				</div>
				<div class="ws-plugs">
					<a class="btn-icon" href="javascript:void(0)" data-todoUser="yes" data-relateDiv="todoUserDiv_-1" title="告知人员">
						@
					</a>
				</div>
				<%--分享按钮--%>
				<div class="ws-share">
					<button type="button" class="btn btn-info ws-btnBlue replyTalk" data-relateTodoDiv="todoUserDiv_-1" >发表</button>
				</div>
				<div style="clear: both;"></div>
				<div id="todoUserDiv_-1" class="padding-top-10">

				</div>
				<div class="ws-notice">
					<tags:uploadMore name="listUpfiles_-1.upfileId" showName="filename" ifream="otherTaskAttrIframe"></tags:uploadMore>
				</div>
			</div>
		</div>
		<div class="ws-border-line"></div>
	<div id="alltalks">
	</div>
	
	<!-- 分页展示 -->
	<div class="panel-body ps-page bg-white" style="font-size: 12px">
		 <p class="pull-left ps-pageText">共<b class="badge" id="totalNum">0</b>条记录</p>
		 <ul class="pagination pull-right" id="pageDiv">
		 </ul>
	</div>
	
 <div style="clear: both;padding-top: 100px;">
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
