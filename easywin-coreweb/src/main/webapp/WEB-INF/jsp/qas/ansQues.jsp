<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
<title><%=SystemStrConstant.TITLE_NAME%></title>

<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/qasJs/ansQues.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script> 
$(document).ready(function() {
	if(${ques.state}==1){//问题没有关闭
		//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
		if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
			document.getElementById('ansContent4Add').onpropertychange=handleAnsContent 
		}else {//非ie浏览器，比如Firefox 
			document.getElementById('ansContent4Add').addEventListener("input",handleAnsContent,false); 
		}
	}
});
var vali;
var ansNum=0
$(function() {
	//初始化名片
	initCard('${param.sid}');
	vali=$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError:true
	});
	//文本框绑定回车提交事件
	$(".txtTalk").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherIframe')
	});
	$(".ptalk").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherIframe')
	});
	$("#ansContent4Add").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherIframe');
    });
	$("#ansContent4update").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherIframe');
    });
});
//窗体点击事件检测
document.onclick = function(e){
	   var evt = e?e:window.event;
	   var ele = evt.srcElement || evt.target;
	   //表情包失焦关闭
		for(var i=0;i<biaoQingObjs.length;i++){
			if(ele.id != biaoQingObjs[i].switchID){
				$("#"+biaoQingObjs[i].divID).hide();
			}
		}
}
//创建一个表情对象数组
var biaoQingObjs = new Array();
//初始化最新初始化表情对象
var activingBiaoQing;
//表情对象添加；switchID触发器开关，objID返回对象主键,表情显示div层主键
function addBiaoQingObj(switchID,divID,objID){
	//数组是否已经包含此元素标识符
	var haven = false;
	//判断数组是否已经包含此主键元素
	if(isBiaoQingEvent(switchID)){
		haven = true;
	}
	//对象构建
	var biaoQing ={"switchID":switchID,"objID":objID,"divID":divID}
	if(!haven){
		//主键存入数组
		biaoQingObjs[biaoQingObjs.length]=biaoQing;
	}
	//初始化最新初始化表情对象
	activingBiaoQing = biaoQing;
	//打开表情
	biaoQingOpen(biaoQing);
}
//判断页面点击事件事都是表情触发事件
function isBiaoQingEvent(eventId){
	//数组是否已经包含此元素标识符
	var haven = false;
	//判断数组是否已经包含此主键元素
	for(var i=0;i<biaoQingObjs.length;i++){
		if(biaoQingObjs[i].switchID==eventId){
			haven = true;
			break;
		}
	}
	return haven;
}
//表情打开
function biaoQingOpen(obj){
	$("#"+obj.divID).show();
}
//表情包关闭
function biaoQingClose(){
	$("#"+activingBiaoQing.divID).hide();
}
//关闭表情div，并赋值
function divClose(title,path){
	biaoQingClose();
	insertAtCursor(activingBiaoQing.objID,title)
	$("#"+activingBiaoQing.objID).focus();
}

function changeStyle(state,ansNum,cnAns){
	if(1==state){
		$(".hideOpt").hide();
		$(".preAnsQues").hide();	
		$(".talk4Ques").hide();	
		$(".wsOpt").hide();	
		$("#finishTalkMsg").show();
	}else{
		if(ansNum==0){
			//以前没有回答过的
			$(".preAnsQues").show();	
		}
		$(".talk4Ques").show();	
		$("#${sessionUser.id}WsOpt").show();	
		
		$(".hideOpt").show();
		$("#finishTalkMsg").hide();
	}
}

var fileTypes="*.rar;*.zip;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.pdf;*.mp3;*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.avi;*.wma;*.rmb;*.rm;*.flash;*.flv;*.mp4;*.mid;*.3gp";
var fileSize="200MB";
var	numberOfFiles = 200;

</script>
<style>
</style>
</head>
<body  onload="setStyle();resizeVoteH('otherIframe');" style="background-color: #fff">
	<form id="ansForm" class="subform" method="post">
	<input type="hidden" id="cnAns" value="${ques.cnAns}"/>
	<input type="hidden" id="comId" name="comId" value="${sessionUser.comId}"/>
	<div id="finishTalkMsg" style="display:${ques.state==0?'block':'none' };text-align:center;font-size:15px;color:gray;padding-top:10px" >
		问题已关闭，不再提供回答和讨论功能
	</div>
	<%--当前操作者回答 --%>
	<div style="${(ques.ansNum==0 && ques.state==1)?'display:block':'display:none' }" class="preAnsQues">
		<div class="ws-textareaBox" style="margin-top:10px;">
			<textarea id="ansContent4Add" class="form-control" style="height: 60px" placeholder="请输入内容……"></textarea>
			<div class="ws-otherBox">
				<div class="ws-meh" style="position: relative;">
					<%--表情 --%>
					<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch" onclick="addBiaoQingObj('biaoQingSwitch','biaoQingDiv','ansContent4Add');"></a>
					<div id="biaoQingDiv" class="blk" style="display:none;position:absolute;width:200px;top:10px;z-index:99;left: 1px">
						<!--表情DIV层-->
				        <div class="main">
				            <ul style="padding: 0px">
				            <jsp:include page="/biaoqing.jsp"></jsp:include>
				            </ul>
				        </div>
			    	</div>
				</div>
				<div class="ws-plugs">
					<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('ansContent4Add','${param.sid}');" title="常用意见"></a>
				</div>
				<!-- <div class="ws-remind">
					<span class="ws-remindTex">提醒方式：</span>
					<div class="ws-checkbox">
						<label class="checkbox-inline">
							<input id="inlineCheckbox1" type="checkbox" value="option1"/>短信</label>
						<label class="checkbox-inline">
							<input id="inlineCheckbox1" type="checkbox" value="option1"/>邮件</label>
						<label class="checkbox-inline">
							<input id="inlineCheckbox1" type="checkbox" value="option1"/>桌面推送</label>
					</div>
				</div> -->
				<div class="ws-share">
					<button type="button" class="btn btn-info ws-btnBlue" onclick="addAns(this,${ques.id},'${param.sid }',${ques.userId })">回答</button>
				</div>
				<div style="clear: both;"></div>
				<div class="ws-notice">
					<tags:uploadMore name="listAnsFiles.id" showName="filename" ifream="otherIframe" comId="${sessionUser.comId}"></tags:uploadMore>
				</div>
			</div>
		</div>
		<div class="ws-border-line"></div>
	</div>
	<%--当前操作者回答结束--%>
	<%--自己回答过了的 --%>
	<div class="allAns">
		<c:choose>
			<c:when test="${not empty list}">
				<c:forEach items="${list}" var="ans" varStatus="vs">
				<div class="ws-clear"></div>
					<c:choose>
						<c:when test="${vs.first && ques.cnAns >0}">
							<%--有采纳的答案 --%>
							<c:choose>
								<c:when test="${ans.cnFlag eq 1}">
									<%--第一个就是采纳的答案 --%>
									<div class="ws-tabTit cnedHead"  style="display: block">
										<ul class="ws-tabBar">
											<li>采纳的答案</li>
										</ul>
									</div>
								</c:when>
								<c:otherwise>
									<%--第一个不是采纳的答案 --%>
									<div class="ws-tabTit cnedHead"  style="display: none">
										<ul class="ws-tabBar">
											<li>采纳的答案</li>
										</ul>
									</div>
								
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${ques.cnAns >0 && vs.count==2}">
							<%--有采纳的答案 ,还有一备选答案--%>
							<div class="ws-tabTit cningHead"  style="display:block">
								<ul class="ws-tabBar">
									<li>其他的答案</li>
								</ul>
							</div>
						</c:when>
						<c:when test="${ques.cnAns eq 0 && vs.first}">
							<%--没有采纳的答案 --%>
							<div class="ws-tabTit cnedHead"  style="display: none">
								<ul class="ws-tabBar">
									<li>采纳的答案</li>
								</ul>
							</div>
							<div class="ws-tabTit cningHead"  style="display:none">
								<ul class="ws-tabBar">
									<li>其他的答案</li>
								</ul>
							</div>
						</c:when>
					</c:choose>
					
					
					<div id="singleAns${ans.id }" class="${ans.cnFlag eq 1?'singleAnsCned':'singleAnsCning'}">
						<div class="ws-shareBox">
							<div class="shareHead" data-container="body" data-toggle="popover" data-user='${ans.userId}'
							data-busId='${ans.quesId}' data-busType='011'>
								<%--头像信息 --%>
								<img src="/downLoad/userImg/${ans.comId}/${ans.userId}?sid=${param.sid}" title="${ans.userName}"></img>
							</div>
							<div class="shareText">
								<div style="position: relative;">
									<span class="ws-blue">${ans.userName}
										<font color="#000" style="padding-left:10px">[回答]</font>
									</span>
									<c:if test="${ques.userId==sessionUser.id}">
										<span style="position: absolute;right:0;top:0; display:none" class="${ans.cnFlag eq 1?'cnAnsYes':'cnAns' }">
											<a href="javascript:void(0)" style="color:green;" onclick="cnAnsFun(${ans.quesId},${ans.id},'${param.sid}')">采纳 </a>
										</span>
									</c:if>
								</div>
								
								<p class="ws-texts" style="clear: both;">
									<tags:viewTextArea>${ans.content}</tags:viewTextArea>
								</p>
								<%--附件 --%>
								<c:choose>
									<c:when test="${not empty ans.listAnsFiles}">
										<div class="file_div">
											<c:forEach items="${ans.listAnsFiles}" var="upfiles" varStatus="vsFile">
												<c:choose>
													<c:when test="${upfiles.isPic==1}">
														<p class="p_text">
														附件(${vsFile.count})：
															<img onload="AutoResizeImage(350,0,this,'otherIframe')"
																src="/downLoad/down/${upfiles.orgFileUuid}/${upfiles.orgFileName}?sid=${param.sid}" />
								 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${upfiles.orgFileUuid}/${upfiles.orgFileName}?sid=${param.sid}" title="下载"></a>
								 						<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${upfiles.orgFileUuid}/${upfiles.orgFileName}','${param.sid}','${upfiles.original}','011','${ques.id}')"></a>
													</p>
													</c:when>
													<c:otherwise>
														<p class="p_text">
														附件(${vsFile.count})：${upfiles.orgFileName }
														<c:choose>
											 					<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx'}">
													 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.orgFileUuid}','${upfiles.orgFileName}','${param.sid}')"></a>
													 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.original}','${upfiles.orgFileUuid}','${upfiles.orgFileName}','${upfiles.fileExt}','${param.sid}','011','${ques.id}')"></a>
											 					</c:when>
											 					<c:when test="${upfiles.fileExt=='txt'  || upfiles.fileExt=='pdf'}">
											 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${upfiles.orgFileUuid}/${upfiles.orgFileName}?sid=${param.sid}" title="下载"></a>
													 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.original}','${upfiles.orgFileUuid}','${upfiles.orgFileName}','${upfiles.fileExt}','${param.sid}','011','${ques.id}')"></a>
											 					</c:when>
											 					<c:otherwise>
													 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.orgFileUuid}','${upfiles.orgFileName}','${param.sid}')"></a>
											 					</c:otherwise>
											 				</c:choose>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</div>
									</c:when>
								</c:choose>
								
								<div class="ws-type" >
									<span class="wsOpt" id="${ans.userId}WsOpt" style="float:left; display:${(ques.state==1&& ans.cnFlag eq 0)?'block':'none'}">
										<%--发言人可以删除自己的发言 --%>
										<c:if test="${ans.userId==sessionUser.id}">
											<a href="javascript:void(0);" style="padding-right: 15px" onclick="appandAns(${ans.id})" class="fa fa-edit" title="修改回答"></a>
											<a href="javascript:void(0);" style="padding-right: 15px" onclick="delAns(this,${ans.id},${ans.quesId },'${param.sid}',${ques.userId})" class="fa fa-trash-o" title="删除回答"></a>
										</c:if>
									</span>
									<span>
										<a id="img_${ans.id}" name="replyImg" onclick="talk4Ques(${ans.id},this)" href="javascript:void(0);" 
										class="fa fa-comment-o" title="评论">[${fn:length(ans.listAnsTalks) }]</a>
										<time>${fn:substring(ans.recordCreateTime,0,16)}</time>
									</span>
								</div>
							
							</div>
							<%--只有问题的回答人，在非采纳的状态下才可以修改 --%>
							<c:if test="${ans.userId==sessionUser.id}">
								<div class="ws-clear"></div>
								<div class="shareText updateContent" style="display: none">
									<div class="ws-textareaBox" style="margin-top:10px;">
										<textarea id="ansContent4update" class="form-control" style="height: 60px">${ans.content }</textarea>
									</div>
									<div class="ws-otherBox">
										<div class="ws-meh" style="position: relative;">
											<%--表情 --%>
											<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitchUpdate" onclick="addBiaoQingObj('biaoQingSwitchUpdate','biaoQingDivUpdate','ansContent4update');"></a>
											<div id="biaoQingDivUpdate" class="blk" style="display:none;position:absolute;width:200px;top:10px;z-index:99;left: 1px">
												<!--表情DIV层-->
										        <div class="main">
										            <ul style="padding: 0px">
										            <jsp:include page="/biaoqing.jsp"></jsp:include>
										            </ul>
										        </div>
									    	</div>
										</div>
										<div class="ws-plugs">
											<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('ansContent4update','${param.sid}');" title="常用意见"></a>
										</div>
										<!-- <div class="ws-remind">
											<span class="ws-remindTex">提醒方式：</span>
											<div class="ws-checkbox">
												<label class="checkbox-inline">
													<input id="inlineCheckbox1" type="checkbox" value="option1"/>短信</label>
												<label class="checkbox-inline">
													<input id="inlineCheckbox1" type="checkbox" value="option1"/>邮件</label>
												<label class="checkbox-inline">
													<input id="inlineCheckbox1" type="checkbox" value="option1"/>桌面推送</label>
											</div>
										</div> -->
										<div style="clear: both;"></div>
										<%--附件开始 --%>
										<div class="ws-notice">
											<div style="clear:both;width: 300px" class="wu-example">
												<div style="width: 300px;" id="thelistlistAnsFilesUpdate_id">
													<c:if test="${not empty ans.listAnsFiles}">
														<c:forEach items="${ans.listAnsFiles}" var="ansFile" varStatus="vs">
														 <div id="wu_file_0_-${ansFile.original }" class="uploadify-queue-item">	
															<div class="cancel">
																<a href="javascript:void(0)" fileDone="1" fileId="${ansFile.original}">X</a>
															</div>	
																<span class="fileName" title="${ansFile.orgFileName}">
																	<tags:cutString num="25">${ansFile.orgFileName}</tags:cutString>(已有文件)
																</span>
																<span class="data"> - 完成</span>
															<div class="uploadify-progress">
																<div class="uploadify-progress-bar" style="width: 100%;"></div>
															</div>	
														</div>	
														</c:forEach>
													</c:if>
												</div>
												<div class="btns btn-sm">
														<div id="pickerlistAnsFilesUpdate_id">选择文件</div>
												</div>
												<script type="text/javascript">
													loadWebUpfiles('listAnsFilesUpdate_id','${param.sid}','otherIframe','pickerlistAnsFilesUpdate_id','thelistlistAnsFilesUpdate_id','filelistAnsFilesUpdate_id');
												</script>
												<div style="width: 350px; height: 90px; display: none; position: relative;">
													<div style="width: 250px; float: left;">
														<select style="width: 100%; height: 90px;" id="listAnsFilesUpdate_id" ondblclick="removeClick(this.id)" multiple="multiple" name="listAnsFilesUpdate.id" moreselect="true" listvalue="filename" listkey="id" list="listAnsFilesUpdate">
															<c:if test="${not empty ans.listAnsFiles}">
																<c:forEach items="${ans.listAnsFiles}" var="ansFile" varStatus="vs">
																	<option selected="selected" value="${ansFile.original}">${ansFile.orgFileName }</option>
																</c:forEach>
															</c:if>
														 </select>
													</div>
												</div>
											</div>
										</div>
										<%--附件结束 --%>
										<div style="text-align: center">
											<button type="button" class="btn btn-info ws-btnBlue" onclick="updateAns(this,${ques.id},${ans.id },'${param.sid }',${ques.userId})">修改</button>
											<button type="reset" class="btn btn-default" onclick="appandAns(${ans.id})">取消</button>
										</div>
										<div style="clear: both;"></div>
									</div>
								</div>
							</c:if>
							<%--只有问题的回答人，在非采纳的状态下才可以修改结束 --%>
						</div>
						<div class="ws-clear"></div>
						
						<div id="talk4Ques${ans.id}" style="display:none">
							<%--针对回答的评论 --%>
							<div style="display:${ques.state eq 1?'block':'none'}" class="ws-shareBox ws-shareBox2 talk4Ques">
								<div class="shareText">
									<div class="ws-textareaBox" style="margin-top:10px;">
										<textarea id="operaterReplyTextarea_${ans.id}_-1" class="form-control ptalk" placeholder="评论……"></textarea>
										<div class="ws-otherBox" style="position: relative;">
											<div class="ws-meh">
												<%--表情 --%>
												<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_${ans.id}" class="tigger" onclick="addBiaoQingObj('biaoQingSwitch_${ans.id}','biaoQingDiv_${ans.id}','operaterReplyTextarea_${ans.id}_-1');"></a>
												<div id="biaoQingDiv_${ans.id}" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 5px">
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
												<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_${ans.id}_-1','${param.sid}');" title="常用意见"></a>
											</div>
											<%--提醒方式 --%>
											<!-- <div class="ws-remind">
												<span class="ws-remindTex">提醒方式：</span>
												<div class="ws-checkbox">
													<label class="checkbox-inline">
														<input id="inlineCheckbox1" type="checkbox" value="option1"/>短信</label>
													<label class="checkbox-inline">
														<input id="inlineCheckbox1" type="checkbox" value="option1"/>邮件</label>
													<label class="checkbox-inline">
														<input id="inlineCheckbox1" type="checkbox" value="option1"/>桌面推送</label>
												</div>
											</div> -->
											<%--分享按钮 --%>
											<div class="ws-share">
												<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk(${ques.id},${ans.id},-1,this,'','${param.sid}')">评论</button>
											</div>
											<%--相关附件 --%>
											<div style="clear: both;"></div>
											<div class="ws-notice">
												<tags:uploadMore name="listUpfiles_${ans.id}_-1.upfileId" showName="filename" ifream="otherIframe" comId="${sessionUser.comId}"></tags:uploadMore>
											</div>
										</div>
									</div>
								</div>
								<%--针对回答的评论结束 --%>
								<div class="ws-clear"></div>
							</div>
							<%--针对评论的回复 --%>
							<div id="alltalks${ans.id}">
								<c:if test="${not empty ans.listAnsTalks}">
									<c:forEach items="${ans.listAnsTalks}" var="ansTalk" varStatus="vs">
										<div class="ws-shareBox ws-shareBox2 voteTalkInfo${ansTalk.ptalker>0?'P':''}"" id="talk${ansTalk.id}" parentId="${ansTalk.parentId}">
											<div class="shareHead" data-container="body" data-toggle="popover" data-user='${ansTalk.talker}'
											data-busId='${ans.quesId}' data-busType='011'>
												<img src="/downLoad/userImg/${ansTalk.comId}/${ansTalk.talker}?sid=${param.sid}" title="${ansTalk.talkerName}"></img>
											</div>
											<div class="shareText">
												<span class="ws-blue">${ansTalk.talkerName}</span>
												<c:if test="${ansTalk.ptalker>0}">
													<r>回复</r>
													<span class="ws-blue">${ansTalk.ptalkerName }</span>
												</c:if>
												<p class="ws-texts">
													<tags:viewTextArea>${ansTalk.talkContent}</tags:viewTextArea>
												</p>
												<%--附件 --%>
												<c:if test="${not empty ansTalk.listQasTalkFile}">
													<div class="file_div">
														<c:forEach items="${ansTalk.listQasTalkFile}" var="upfiles" varStatus="vs">
															<c:choose>
																<c:when test="${upfiles.isPic==1}">
																	<p class="p_text">
																	附件（${vs.count}）：
																		<img onload="AutoResizeImage(350,0,this,'otherIframe')"
																			src="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" />
											 						<a class="fa fa-download" style="padding-left: 15px" title="下载" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
											 						<a class="fa fa-eye"  style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${upfiles.uuid}/${upfiles.filename}','${param.sid}','${upfiles.upfileId}','011','${ques.id}')"></a>
																	</p>
																</c:when>
																<c:otherwise>
																	<p class="p_text">
																	附件（${vs.count}）：
																		${upfiles.filename}
																	<c:choose>
													 					<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx'}">
															 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
															 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','011','${ques.id}')"></a>
													 					</c:when>
													 					<c:when test="${upfiles.fileExt=='txt'  || upfiles.fileExt=='pdf'}">
													 						<a class="fa fa-download" style="padding-left: 15px" title="下载" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
															 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','011','${ques.id}')"></a>
													 					</c:when>
													 					<c:otherwise>
															 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
													 					</c:otherwise>
													 				</c:choose>
																	</p>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</div>
												</c:if>
												<div class="ws-type">
													<span class="hideOpt" style="float:left;display:${ques.state eq 1?'block':'none'}">
														<%--发言人可以删除自己的发言 --%>
														<c:if test="${sessionUser.id==ansTalk.talker}">
															<a href="javascript:void(0);" id="delTalk_${ans.id }_${ansTalk.id}" onclick="delAnsTalk(${ans.quesId},${ans.id },${ansTalk.id},${ansTalk.isLeaf },'${param.sid}')"  class="fa fa-trash-o" title="删除"></a>
														</c:if>
														<a id="img_${ans.id}_${ansTalk.id}"  href="javascript:void(0);" class="fa fa-comment-o" title="回复" onclick="showArea(${ans.id},${ansTalk.id})"></a>
													</span>
													<span style="float:left;">
														<time>${ansTalk.recordCreateTime}</time>
													</span>
												</div>
											</div>
											<div class="ws-clear"></div>
										</div>
										
										<div id="addTalk_${ans.id}_${ansTalk.id}"  class="ws-shareBox ws-shareBox2 addtalk" style="display:none;">
											<div class="shareText">
												<div class="ws-textareaBox" style="margin-top:10px;">
													<textarea id="operaterReplyTextarea_${ans.id}_${ansTalk.id}" class="form-control txtTalk" placeholder="回复……"></textarea>
													<div class="ws-otherBox" style="position: relative;">
														<div class="ws-meh">
															<%--表情 --%>
															<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_${ans.id}_${ansTalk.id}" class="tigger" onclick="addBiaoQingObj('biaoQingSwitch_${ans.id}_${ansTalk.id}','biaoQingDiv_${ans.id}_${ansTalk.id}','operaterReplyTextarea_${ans.id}_${ansTalk.id}');"></a>
															<div id="biaoQingDiv_${ans.id}_${ansTalk.id}" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 5px">
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
															<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_${ans.id}_${ansTalk.id}','${param.sid}');" title="常用意见"></a>
														</div>
														<%--提醒方式 --%>
														<!-- <div class="ws-remind">
															<span class="ws-remindTex">提醒方式：</span>
															<div class="ws-checkbox">
																<label class="checkbox-inline">
																	<input id="inlineCheckbox1" type="checkbox" value="option1"/>短信</label>
																<label class="checkbox-inline">
																	<input id="inlineCheckbox1" type="checkbox" value="option1"/>邮件</label>
																<label class="checkbox-inline">
																	<input id="inlineCheckbox1" type="checkbox" value="option1"/>桌面推送</label>
															</div>
														</div> -->
														<%--分享按钮 --%>
														<div class="ws-share">
															<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk(${ans.quesId},${ans.id},${ansTalk.id},this,${ansTalk.talker},'${param.sid}')">回复</button>
														</div>
														<%--相关附件 --%>
														<div style="clear: both;"></div>
														<div class="ws-notice">
															<tags:uploadMore name="listUpfiles_${ans.id}_${ansTalk.id}.upfileId" showName="filename" ifream="otherIframe" comId="${sessionUser.comId}"></tags:uploadMore>
																
														</div>
													</div>
												</div>
											</div>
											<%--针对回答的评论结束 --%>
											<div class="ws-clear"></div>
										</div>
									</c:forEach>
								</c:if>
							</div>
							<%--针对评论的回复结束 --%>
						</div>
						<div class="ws-clear"></div>
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
			<%--没有答案 --%>
				<div class="ws-tabTit cnedHead"  style="display: none">
					<ul class="ws-tabBar">
						<li>采纳的答案</li>
					</ul>
				</div>
				<div class="ws-tabTit cningHead"  style="display:none">
					<ul class="ws-tabBar">
						<li>其他的答案</li>
					</ul>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	
	</form>


<div style="clear: both;padding-top: 100px;" class="bg-white">
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
