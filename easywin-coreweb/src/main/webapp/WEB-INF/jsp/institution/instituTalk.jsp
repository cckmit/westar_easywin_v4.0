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
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
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
		"homeFlag" : "${homeFlag}",
		"ifreamName" : "${param.ifreamName}",
		"busId" : "${busId}"
	};
</script>
</head>
<body style="background-color: #ffffff" onload="resizeVoteH('otherinstituAttrIframe')" >
<form id="instituForm" class="subform">
	<%--当前操作员的回复 --%>
	<div class="parentRep">
		<div class="ws-textareaBox" style="margin-top:10px;">
			<textarea id="operaterReplyTextarea_-1" style="height: 55px" class="form-control " placeholder="请输入内容……"></textarea>
			<div class="ws-otherBox" style="position: relative;">
				<div class="ws-meh">
					<%--表情 --%>
					<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch" onclick="addBiaoQingObj('biaoQingSwitch','biaoQingDiv','operaterReplyTextarea_-1');"></a>
					<div id="biaoQingDiv" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 15px">
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
				<div class="ws-share">
					<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('${busId}',-1,this,'')">发表</button>
				</div>
				<div style="clear: both;"></div>
				<div class="ws-notice">
					<tags:uploadMore name="listUpfiles_-1.upfileId" showName="filename" ifream="otherinstituAttrIframe" comId="${sessionUser.comId}"></tags:uploadMore>
				</div>
			</div>
		</div>
		<div class="ws-border-line" style="height: 15px"></div>
	</div>
	<%--当前讨论结束 --%>
	<%--列出有回复内容 --%>
	<div id="alltalks" style="clear:both">
		<c:if test="${not empty talks}">
			<c:forEach items="${talks}" var="talk" varStatus="vs">
				<div id="talk${talk.id}" class="ws-shareBox ${talk.parentId==-1?'':'ws-shareBox2'}">
					<div class="shareHead" data-container="body" data-toggle="popover" data-user='${talk.talker}'
					data-busId='${busId}' data-busType='039'>
						<%--头像信息 --%>
						<img src="/downLoad/userImg/${talk.comId}/${talk.talker}?sid=${param.sid}" title="${talk.talkerName}"></img>
					</div>
					<div class="shareText">
						<span class="ws-blue">${talk.talkerName}</span>
						<c:if test="${talk.parentId>-1}">
							<r>回复</r>
							<span class="ws-blue">${talk.ptalkerName}</span>
						</c:if>
						<p class="ws-texts">
							${talk.content}
						</p>
						<%--附件 --%>
						<c:choose>
							<c:when test="${not empty talk.listTalkUpfile}">
								<div class="file_div">
								<c:forEach items="${talk.listTalkUpfile}" var="upfiles" varStatus="vs">
									<c:choose>
										<c:when test="${upfiles.isPic==1}">
											<p class="p_text">
											附件(${vs.count})：
												<img onload="AutoResizeImage(350,0,this,'otherinstituAttrIframe')" src="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" />
					 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
					 						&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${upfiles.uuid}/${upfiles.filename}','${param.sid}','${upfiles.upfileId}','039','${busId}')"></a>
											</p>
										</c:when>
										<c:otherwise>
											<p class="p_text">
											附件(${vs.count})：
												${upfiles.filename}
											<c:choose>
							 					<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx' }">
									 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
									 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','039','${busId}')"></a>
							 					</c:when>
							 					<c:when test="${upfiles.fileExt=='txt' || upfiles.fileExt=='pdf'}">
							 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
									 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','039','${busId}')"></a>
							 					</c:when>
							 					<c:otherwise>
									 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
							 					</c:otherwise>
							 				</c:choose>
											</p>
										</c:otherwise>
									</c:choose>
								</c:forEach>
								</div>
							</c:when>
						</c:choose>
						<div class="ws-type" >
							<span class="hideOpt" >
								<%--发言人可以删除自己的发言 --%>
								<c:if test="${sessionUser.id==talk.talker}">
									<a href="javascript:void(0);" id="delAnnounTalk_${talk.id}" onclick="delTalk(${talk.id},${talk.isLeaf })" class="fa fa-trash-o" title="删除"></a>
								</c:if>
								<a id="img_${talk.id}" name="replyImg" onclick="showArea('addTalk${talk.id}','otherinstituAttrIframe')"  href="javascript:void(0);" class="fa fa-comment-o" title="回复"></a>
							</span>
							<span>
								<time>${talk.recordCreateTime}</time>
							</span>
						</div>
					</div>
					<div class="ws-clear"></div>
				</div>
				<!-- 回复层 -->
				<div id="addTalk${talk.id}" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">
					<div class="shareText">
						<div class="ws-textareaBox" style="margin-top:10px;">
							<textarea id="operaterReplyTextarea_${talk.id}" name="operaterReplyTextarea_${talk.id}" style="height: 55px" rows="" cols="" class="form-control txtVoteTalk" placeholder="回复……"></textarea>
							<div class="ws-otherBox" style="position: relative;">
								<div class="ws-meh">
									<%--表情 --%>
									<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_${talk.id}" onclick="addBiaoQingObj('biaoQingSwitch_${talk.id}','biaoQingDiv_${talk.id}','operaterReplyTextarea_${talk.id}');"></a>
									<div id="biaoQingDiv_${talk.id}" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 15px">
										<!--表情DIV层-->
								        <div class="main">
								            <ul style="padding: 0px">
								            <jsp:include page="/biaoqing.jsp"></jsp:include>
								            </ul>
								        </div>
								    </div>
								</div>
								<%--常用意见 --%>
								<div class="ws-plugs">
									<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_${voteTalk.id}','${param.sid}');" title="常用意见"></a>
								</div>
								<%--分享按钮 --%>
								<div class="ws-share">
									<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('${busId}',${talk.id },this,${talk.talker})">回复</button>
								</div>
								<%--相关附件 --%>
								<div style="clear: both;"></div>
								<div class="ws-notice">
								<tags:uploadMore
										name="listUpfiles_${talk.id}.upfileId" showName="filename" ifream="otherinstituAttrIframe" comId="${sessionUser.comId}"></tags:uploadMore>
								</div>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:if>
	</div>
	<tags:pageBar url="/institution/instituTalkPage"></tags:pageBar>
</form>
<div style="clear: both;margin-top: 100px"></div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
