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
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">

	$(document).ready(function() {
		resizeVoteH('otherTaskAttrIframe');
		$("#operaterSayTextarea").autoTextarea({maxHeight:300});  
	});
	$(function(){
		//文本框绑定回车提交事件
		$("#operaterSayTextarea").bind("paste cut keydown keyup focus blur",function(event){
			resizeVoteH('otherTaskAttrIframe');
	    });
		
	});
	
	$(function(){
		$("#taskTalkAjaxSubmit").click(function(){
			if(strIsNull($("#operaterSayTextarea").val())){
				showNotification(1,"请编辑分享内容！");
				$("#operaterSayTextarea").focus();
				return false;
			}
			$.post("/task/ajaxAddTaskTalk?sid=${param.sid}", {Action:"post",content:$("#operaterSayTextarea").val(),taskId:${taskTalk.taskId},parentId:-1},     
				   function (msgObjs){ 
					$("#alltalks").prepend(msgObjs.taskTalkDivString);
					$("#operaterSayTextarea").attr("value","");
					resizeVoteH("otherTaskAttrIframe");
				   }, "json");
		});
		$('.voteTime').mouseout(function(){
			$(this).find(".edit").hide();
		});
		<%--
		$('.voteTalkInfo').mouseover(function(){
			$(this).find(".voteTalkOpt .delVoteTalk").show();
		});
		$('.voteTalkInfo').mouseout(function(){
			$(this).find(".voteTalkOpt .delVoteTalk").hide();
		});
		$('.voteTalkInfoP').mouseover(function(){
			$(this).find(".voteTalkOpt .delVoteTalk").show();
		});
		$('.voteTalkInfoP').mouseout(function(){
			$(this).find(".voteTalkOpt .delVoteTalk").hide();
		});
		--%>
	});
	//讨论删除按钮隐藏
	function delVoteTalkHiden(divObj){
		$(divObj).find(".voteTalkOpt .delVoteTalk").hide();
	}
	//讨论删除按钮展现
	function delVoteTalkShow(divObj){
		$(divObj).find(".voteTalkOpt .delVoteTalk").show();
	}
	//字符串是否为空判断
	function strIsNull(str){
		if(str!="" && str!="null" && str!=null && str!="undefined" && str!=undefined){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 显示回复的回复
	 */
	function showArea(priId){
		$("#operaterReplyTextarea_"+priId).autoTextarea({maxHeight:200});
		refreshIframe(priId);
		 if($("#addTalk"+priId).css('display')=="none"){
			$(".addTalk").hide();
		 	$("#addTalk"+priId).show();
			$("#operaterReplyTextarea_"+priId).focus();
	     }else{
	     	$("#addTalk"+priId).hide();
	     }
		//刷新付页面iframe高度
		resizeVoteH('otherTaskAttrIframe');
	}

	//给回复textarea绑定刷新付页面iframe事件
	function refreshIframe(id){
		$(function(){
			$("#operaterReplyTextarea_"+id).bind("paste cut keydown keyup focus blur",function(event){
				resizeVoteH('otherTaskAttrIframe');
		    });
		});
	}
	//讨论回复
	function replyTalk(taskTalkId){
		var content = $("#operaterReplyTextarea_"+taskTalkId).val();
		if(strIsNull(content)){
			showNotification(1,"请编辑回复内容！");
			$("#operaterReplyTextarea_"+taskTalkId).focus();
			return false;
		}
		$.post("/task/ajaxAddTaskTalk?sid=${param.sid}&random="+Math.random(),{Action:"post",content:$("#operaterReplyTextarea_"+taskTalkId).val(),taskId:${taskTalk.taskId},parentId:taskTalkId},     
				   function (msgObjs){ 
					$("#talk"+taskTalkId).after(msgObjs.taskTalkDivString);
					$("#operaterReplyTextarea_"+taskTalkId).attr("value","");
					$("#talk"+taskTalkId+" .delVoteTalk a").attr("onclick","delTaskTalk('"+taskTalkId+"','0')")
			     	$("#addTalk"+taskTalkId).hide();
				   }, "json");
		
	}
	
	//删除评论
	function delTaskTalk(talkId,isLeaf){
		art.dialog.confirm("确定要删除该回复吗?", function(){
			if(isLeaf==1){
				$.post("/task/ajaxDelTaskTalk?sid=${param.sid}&delChildNode=no&random="+Math.random(),{Action:"post",id:talkId,taskId:${taskTalk.taskId}},     
				   function (msgObjs){ 
		    		$("#talk"+talkId).remove();
					showNotification(0,msgObjs);
					var win = art.dialog.open.origin;//来源页面
					// 如果父页面重载或者关闭其子对话框全部会关闭
					win.location.reload();
				});
			}else{
				setTimeout(function(){
					art.dialog.confirm("是否需要删除此节点下的回复信息?", function(){
						$.post("/task/ajaxDelTaskTalk?sid=${param.sid}&delChildNode=yes&random="+Math.random(),{Action:"post",id:talkId,taskId:${taskTalk.taskId}},     
						   function (msgObjs){ 
				    		$("#talk"+talkId).remove();
							showNotification(0,msgObjs);
							var win = art.dialog.open.origin;//来源页面
							// 如果父页面重载或者关闭其子对话框全部会关闭
							win.location.reload();
						});
					},function(){
						$.post("/task/ajaxDelTaskTalk?sid=${param.sid}&delChildNode=no&random="+Math.random(),{Action:"post",id:talkId,taskId:${taskTalk.taskId}},     
						   function (msgObjs){ 
				    		$("#talk"+talkId).remove();
							showNotification(0,msgObjs);
							var win = art.dialog.open.origin;//来源页面
							// 如果父页面重载或者关闭其子对话框全部会关闭
							win.location.reload();
						});
					});
				},200);
			}
		},function(){
			art.dialog.tips('已取消');
		});	
	}
	
	//页面刷新
	$(function(){
		$("#refreshImg").click(function(){
			var win = art.dialog.open.origin;//来源页面
			// 如果父页面重载或者关闭其子对话框全部会关闭
			win.location.reload();
		});
	});
</script>
<script> 
	$(function() { 
		$('.grid_tab tr').addClass('odd'); 
		$('.grid_tab tr:even').addClass('even');
	}); 
</script>

</head>
<body style="width:100%;">
	<div style="text-align:right;width:90%;margin-left:55px;"><a href="javascript:void(0);"><img id="refreshImg" src="/static/images/shuaxin.png"></img></a></div>
	<c:choose>
		<c:when test="${taskTalk.taskState!=4}">
		<%--当前操作员的回复 --%>
		<div style="clear: both;float:left" class="currentTalker">
			<img src="/downLoad/userImg/${sessionUser.comId}/${sessionUser.id}?sid=${param.sid}"
				title="${sessionUser.userName }"></img>
			${sessionUser.userName}：
		</div>
		<div style="clear:both;float:left;width:90%;margin-left:55px;">
			<textarea id="operaterSayTextarea" name="operaterSayTextarea"
				style="overflow-y:hidden;width:90%;height:25px;" rows="" cols=""></textarea>
		</div>
		<div style="float:left;width:90%;padding-top:5px;text-align:right">
			<input id="taskTalkAjaxSubmit" type="button" class="blue_btn" value="发&nbsp;表"/>
		</div>
		</c:when>
	</c:choose>
	<div class="b cl mtop" id="taskTalkContainer">
		<form action="/task/taskTalkPage" method="post" id="delForm">
			<tags:token></tags:token>
	 		<input type="hidden" name="redirectPage"/>
	 		<input type="hidden" name="taskId" value="${taskTalk.taskId}"/>
			<%--只有创建人和管理员可以删除投票 --%>
			<div id="alltalks">
				<%--列出有回复内容的 --%>
				<c:if test="${not empty listTaskTalk}">
					<c:forEach items="${listTaskTalk}" var="listTaskTalkVo" varStatus="vs">
						<div class="voteTalkInfo${listTaskTalkVo.parentId==-1?"P":""}" id="talk${listTaskTalkVo.id}" onmouseover="delVoteTalkShow(this);" onmouseout="delVoteTalkHiden(this);">
							<div class="voteTalkHead">
							<%--回复人头像 --%>
								<span id="talkerPic${listTaskTalkVo.id}">
									<img src="/downLoad/userImg/${listTaskTalkVo.comId}/${listTaskTalkVo.speaker}?sid=${param.sid}" title="${listTaskTalkVo.speakerName}"></img>
								</span>
								${listTaskTalkVo.speakerName }
								<%--讨论回复 --%>
								<c:if test="${listTaskTalkVo.parentId>0}">
									&nbsp;回复${listTaskTalkVo.pSpeakerName }<img src="/static/2015/img/message.png" title="${listTaskTalkVo.pContent }"/> 说
								</c:if>
								:<br/><tags:viewTextArea>${listTaskTalkVo.content}</tags:viewTextArea>
							</div>
							<c:choose>
								<c:when test="${taskTalk.taskState!=4}">
									<div style="clear: both;padding-right: 15px" class="voteTalkOpt">
										<span style="float: left">
											${listTaskTalkVo.recordCreateTime }
										</span>
											<span style="float:right">
												<a href="javascript:void(0)" onclick="showArea('${listTaskTalkVo.id}')">回复</a> 
											</span>
										<%--发言人可以删除自己的发言 --%>
										<c:if test="${sessionUser.id==listTaskTalkVo.speaker}">
											<span style="float:right;display: none;padding-right: 15px" class="delVoteTalk">
												<a href="javascript:void(0)" onclick="delTaskTalk('${listTaskTalkVo.id}','${listTaskTalkVo.isLeaf}')">删除</a>
											</span>
										</c:if>
									</div>
								</c:when>
							</c:choose>
							<%--针对回复的回复 --%>
							<div id="addTalk${listTaskTalkVo.id}" style="clear: both;padding-right: 15px;padding-top: 10px;display: none" class="addTalk">
								<div style="clear: both;float: right;width: 88%">
									<textarea id="operaterReplyTextarea_${listTaskTalkVo.id}" name="operaterReplyTextarea_${listTaskTalkVo.id}"
										style="overflow-y:hidden;width:90%;height:25px;" rows="" cols=""></textarea>
									<div style="float: right;padding-top: 5px">
										<input type="button" class="blue_btn" value="回复" onclick="replyTalk(${listTaskTalkVo.id})"/>
									</div>
								</div>
								<div style="float: right;">
									<img src="/downLoad/userImg/${sessionUser.comId}/${sessionUser.id}?sid=${param.sid}" title="${sessionUser.userName}"></img>
									：
								</div>
							</div>
							<div style="clear:both">
								<hr/>
							</div>
						</div>
					</c:forEach>
				</c:if>
				<c:if test="${empty listTaskTalk}">
				<div class="voteTalkInfoP"/>
				</c:if>
			</div>
		</form>
		<div class="ws-page">
			<tags:pageBar url="/task/taskTalkPage"></tags:pageBar>
		</div>
	</div>
	 <div style="clear: both;padding-top: 100px;" class="bg-white">
</div>
	<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
