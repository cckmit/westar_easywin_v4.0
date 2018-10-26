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
<script type="text/javascript" src="/static/js/voteJs/voteTalkStr.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script> 
$(function(){
	//初始化名片
	$(".txtVoteTalk").autoTextarea({minHeight:55,maxHeight:160});  
	$(".txtVoteTalk").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('voteInfo')
    });
})
//样式设置
function setStyle(){
	$(".txtVoteTalk").autoTextarea({minHeight:55,maxHeight:160});  
}

/**
 * voteId 投票主键
 * talkPId 回复的父节点
 */
function addTalk(voteId,talkPId,ts,ptalker){
	var textarea = $("#operaterReplyTextarea_"+talkPId);
	var content = $(textarea).val();
	if(""==content){
		layer.tips('请填写内容！', $("#operaterReplyTextarea_"+talkPId), {tips: 1});
		$(textarea).focus();
		return;
	}else{
		
		var onclick = $(ts).attr("onclick");
		//异步提交表单 投票讨论
	    $("#voteForm").ajaxSubmit({
	        type:"post",
	        url:"/vote/addVoteTalk?sid=${param.sid}&t="+Math.random(),
	        beforeSubmit:function(a,o,f){
		        $(ts).removeAttr("onclick");
		    },
	        data:{"voteId":voteId,
		        "parentId":talkPId,
		        "content":content,
		        "ptalker":ptalker,
		        "upfilesId":$("#listUpfiles_"+talkPId+"_upfileId").val()},
	        dataType: "json", 
	        traditional :true,
	        success:function(data){
	        	var voteTalk = data.voteTalk;
			    var html = getVoteTalkStr(voteTalk,'${param.sid}',data.sessionUser);
			    $(textarea).val('');
		       	if(talkPId==-1){
		       		$("#alltalks").prepend(html);
		       	}else{
		       		//父节点
			     	$("#delvoteTalk_"+talkPId).attr("onclick","delVoteTalk("+talkPId+",0)")
		       		$("#talk"+talkPId).after(html);
			     	$("#addTalk"+talkPId).hide();
			     	$("#img_"+talkPId).attr("src","/static/images/say.png");
		       	}
		     	$("#listUpfiles_"+talkPId+"_upfileId").html('');

		     	//清除文件序列缓存
				$.each($("#thelistlistUpfiles_"+talkPId+"_upfileId"),function(index,item){
					$(this).find(".cancel").click();
				})
				 
			    setStyle();
			   $(ts).attr("onclick",onclick);
			   resizeVoteH('voteInfo');
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	showNotification(2,"操作失败");
				$(textarea).html('');
			    $(ts).attr("onclick",onclick);
	        }
	    });
	}
}
//删除评论
function delVoteTalk(id,isLeaf){
	window.top.layer.confirm("确定要删除该回复吗?", {
		  btn: ['确定','取消']//按钮
	  ,title:'询问框'
	  ,icon:3
	}, function(index){//删除叶子节点
		window.top.layer.close(index);
		if(isLeaf==1){
			//异步提交表单删除评论
		    $("#voteForm").ajaxSubmit({
		        type:"post",
		        url:"/vote/delVoteTalk?sid=${param.sid}&t="+Math.random(),
		        data:{"id":id,"voteId":${voteId}},
		        dataType: "json", 
		        success:function(data){
		        	window.self.location.reload();
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        	showNotification(2,"操作失败");
		        }
		    });
		}else{
			setTimeout(function(){
				window.top.layer.confirm("是否需要删除此节点下的回复信息?",{
					  btn: ['是','否']//按钮
				  ,title:'询问框'
				  ,icon:3
				},  function(index){//删除自己和子节点
					window.top.layer.close(index);
					//异步提交表单删除评论
				    $("#voteForm").ajaxSubmit({
				        type:"post",
				        url:"/vote/delVoteTalk?sid=${param.sid}&t="+Math.random(),
				        data:{"id":id,"voteId":${voteId},"delChildNode":"yes"},
				        dataType: "json", 
				        success:function(data){
				        	window.self.location.reload();
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        	showNotification(2,"操作失败");
				        }
				    });
				},function(index){
					window.top.layer.close(index);
					//异步提交表单删除评论
				    $("#voteForm").ajaxSubmit({//删除自己
				        type:"post",
				        url:"/vote/delVoteTalk?sid=${param.sid}&t="+Math.random(),
				        data:{"id":id,"voteId":${voteId},"delChildNode":"no"},
				        dataType: "json", 
				        success:function(data){
				        	window.self.location.reload();
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        	showNotification(2,"操作失败");
				        }
				    });
				});
			},200);
		}
	});	
}



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
//显示回复
function showInfos(){
	$("#finishTalkMsg").css("display","none");
	$(".hideOpt").css("display","block");
	$(".parentRep").css("display","block");
	resizeVoteH('voteInfo')
}
var fileTypes="*.rar;*.zip;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.pdf;*.mp3;*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.avi;*.wma;*.rmb;*.rm;*.flash;*.flv;*.mp4;*.mid;*.3gp";
var fileSize="200MB";
var	numberOfFiles = 200;
</script>
</head>
<body style="background-color: #ffffff"  onload="setStyle();resizeVoteH('voteInfo');">
<form id="voteForm" class="subform">
	<div id="finishTalkMsg" style="${enabled eq 0?'display:block':'display:none' };text-align:center;font-size:15px;color:gray;padding-top:10px" >
		投票已截止，不再提供评论讨论功能
	</div>
	<%--当前操作员的回复 --%>
	<div class="parentRep" style="${enabled eq 0?'display:none':'display:block' }">
	
		<div class="ws-textareaBox" style="margin-top:10px;">
			<textarea id="operaterReplyTextarea_-1" style="height: 55px" class="form-control txtVoteTalk" placeholder="请输入内容……"></textarea>
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
					<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('${voteId}',-1,this)">发表</button>
				</div>
				<div style="clear: both;"></div>
				<div class="ws-notice">
					<tags:uploadMore name="listUpfiles_-1.upfileId" showName="filename" ifream="voteInfo" comId="${sessionUser.comId}"></tags:uploadMore>
				</div>
			</div>
		</div>
		<div class="ws-border-line" style="height: 15px"></div>
	</div>
	<%--当前讨论结束 --%>
	<%--列出有回复内容 --%>
	<div id="alltalks" style="clear:both">
		<c:if test="${not empty voteTalks}">
			<c:forEach items="${voteTalks}" var="voteTalk" varStatus="vs">
				<div id="talk${voteTalk.id}" class="ws-shareBox ${voteTalk.parentId==-1?'':'ws-shareBox2'} voteTalkInfo${voteTalk.parentId==-1?'P':''}">
					<div class="shareHead" data-container="body" data-toggle="popover" data-user='${voteTalk.talker}'
					data-busId='${voteId}' data-busType='004'>
						<%--头像信息 --%>
						<img src="/downLoad/userImg/${voteTalk.comId}/${voteTalk.talker}?sid=${param.sid}" title="${voteTalk.talkerName}"></img>
					</div>
					<div class="shareText">
						<span class="ws-blue">${voteTalk.talkerName}</span>
						<c:if test="${voteTalk.parentId>-1}">
							<r>回复</r>
							<span class="ws-blue">${voteTalk.ptalkerName}</span>
						</c:if>
						<p class="ws-texts">
							${voteTalk.content}
						</p>
						<%--附件 --%>
						<c:choose>
							<c:when test="${not empty voteTalk.listVoteTalkFile}">
								<div class="file_div">
								<c:forEach items="${voteTalk.listVoteTalkFile}" var="upfiles" varStatus="vs">
									<c:choose>
										<c:when test="${upfiles.isPic==1}">
											<p class="p_text">
											附件(${vs.count})：
												<img onload="AutoResizeImage(350,0,this,'voteInfo')"
													src="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" />
					 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
					 						&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${upfiles.uuid}/${upfiles.filename}','${param.sid}','${upfiles.upfileId}','004','${voteId}')"></a>
											</p>
										</c:when>
										<c:otherwise>
											<p class="p_text">
											附件(${vs.count})：
												${upfiles.filename}
											<c:choose>
							 					<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx' }">
									 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
									 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','004','${voteId}')"></a>
							 					</c:when>
							 					<c:when test="${upfiles.fileExt=='txt' || upfiles.fileExt=='pdf'}">
							 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
									 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','004','${voteId}')"></a>
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
							<span class="hideOpt" style="display:${enabled eq 1?'block':'none'}">
								<%--发言人可以删除自己的发言 --%>
								<c:if test="${sessionUser.id==voteTalk.talker}">
									<a href="javascript:void(0);" id="delvoteTalk_${voteTalk.id}" onclick="delVoteTalk(${voteTalk.id},${voteTalk.isLeaf })" class="fa fa-trash-o" title="删除"></a>
								</c:if>
								<a id="img_${voteTalk.id}" name="replyImg" onclick="showArea('addTalk${voteTalk.id}')" href="javascript:void(0);" class="fa fa-comment-o" title="回复"></a>
							</span>
							<span>
								<time>${voteTalk.recordCreateTime}</time>
							</span>
						</div>
					</div>
					<div class="ws-clear"></div>
				</div>
				<!-- 回复层 -->
				<div id="addTalk${voteTalk.id}" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">
					<div class="shareText">
						<div class="ws-textareaBox" style="margin-top:10px;">
							<textarea id="operaterReplyTextarea_${voteTalk.id}" name="operaterReplyTextarea_${voteTalk.id}" style="height: 55px" rows="" cols="" class="form-control txtVoteTalk" placeholder="回复……"></textarea>
							<div class="ws-otherBox" style="position: relative;">
								<div class="ws-meh">
									<%--表情 --%>
									<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_${voteTalk.id}" onclick="addBiaoQingObj('biaoQingSwitch_${voteTalk.id}','biaoQingDiv_${voteTalk.id}','operaterReplyTextarea_${voteTalk.id}');"></a>
									<div id="biaoQingDiv_${voteTalk.id}" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 15px">
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
								<%--提醒方式 --%>
							<!-- 	<div class="ws-remind">
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
									<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('${voteId}',${voteTalk.id },this,${voteTalk.talker})">回复</button>
								</div>
								<%--相关附件 --%>
								<div style="clear: both;"></div>
								<div class="ws-notice">
									<tags:uploadMore
										name="listUpfiles_${voteTalk.id}.upfileId" showName="filename" ifream="voteInfo" comId="${sessionUser.comId}"></tags:uploadMore>
								</div>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:if>
	</div>
	<tags:pageBar url="/vote/voteTalkPage"></tags:pageBar>
</form>
<div style="clear: both;margin-top: 100px"></div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
