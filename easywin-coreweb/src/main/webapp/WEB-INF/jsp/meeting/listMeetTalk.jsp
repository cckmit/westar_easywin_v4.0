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
<script type="text/javascript" src="/static/js/meetJs/meeting.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script> 
$(function(){
	//初始化名片
	$(".txtMeetTalk").autoTextarea({minHeight:55,maxHeight:160});  
	$(".txtMeetTalk").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherAttrIframe')
    });
	

	//信息推送
	$("body").on("click","a[data-todoUser]",function(){
		var relateDiv = $(this).attr("data-relateDiv");
		var params = {};
		//查询所有
		params.onlySubState = 0;
		userMore('null',params , '${param.sid}',"null","null",function(options){
			if(options && options[0]){
				$.each(options,function(index,option){
					var span = $('<span></span>');
					var userId = option.value;
					//去重
					var preUserSpan = $("#"+relateDiv).find("span[data-userId='"+userId+"']");
					if(preUserSpan && preUserSpan.get(0)){
						return true;
					}
					$(span).attr("data-userId",option.value);
					$(span).attr("title","双击移除");
					$(span).addClass("blue");
					$(span).css("cursor","pointer");
					$(span).css("padding-right","5px");
					$(span).html('@'+option.text);
					
					var userObj = {}
					userObj.userId = option.value;
					userObj.userName = option.text;
					$(span).data("userObj",userObj);
					
					$("#"+relateDiv).append($(span));
				})
			}else{
				$("#"+relateDiv).html("");
			}
		})
	})
	
	//信息推送移除
	$("body").on("dblclick","span[data-userId]",function(){
		$(this).remove();
	})
	
})
//样式设置
function setStyle(){
	$(".txtMeetTalk").autoTextarea({minHeight:55,maxHeight:160});  
}

/**
 * meetingId 投票主键
 * talkPId 回复的父节点
 */
function addTalk(meetingId,talkPId,ts,ptalker){
	var textarea = $("#operaterReplyTextarea_"+talkPId);
	var content = $(textarea).val();
	if(""==content){
		layer.tips("请填写内容",textarea,{tips:1});
		$(textarea).focus();
		return;
	}else{
		var params = {"meetingId":meetingId,
		        "parentId":talkPId,
		        "content":content,
		        "ptalker":ptalker,
		        "upfilesId":$("#listUpfiles_"+talkPId+"_upfileId").val()};
		var relateTodoDiv = $("#"+$(ts).attr("data-relateTodoDiv"));
		if(relateTodoDiv && relateTodoDiv.get(0)){
			var todoUsers = $(relateTodoDiv).find("span");
			if(todoUsers && todoUsers.get(0)){
				$.each(todoUsers,function(index,user){
					var user = $(this).data("userObj")
					params["listMeetLearn["+index+"].userId"] = user.userId;
					params["listMeetLearn["+index+"].userName"] = user.userName;
				})
			}
		}
		var onclick = $(ts).attr("onclick");
		//异步提交表单 投票讨论
	    $("#meetTalkForm").ajaxSubmit({
	        type:"post",
	        url:"/meeting/ajaxAddMeetTalk?sid=${param.sid}&t="+Math.random(),
	        beforeSubmit:function(a,o,f){
		        $(ts).removeAttr("onclick");
		    },
	        data:params,
	        dataType: "json", 
	        traditional :true,
	        success:function(data){
	        	var meetTalk = data.meetTalk;
			    var html = getMeetTalkStr(meetTalk,'${param.sid}',data.userInfo);
			    $(textarea).val('');
		       	if(talkPId==-1){
		       		$("#alltalks").prepend(html);
		       	}else{
		       		//父节点
			     	$("#delMeetTalk_"+talkPId).attr("onclick","delMeetTalk("+talkPId+",0)")
		       		$("#addTalk"+talkPId).after(html);
			     	$("#addTalk"+talkPId).hide();
			     	
			     	$("#img_"+talkPId).attr("title","回复");
			     	$("#img_"+talkPId).attr("class","fa fa-comment-o");
		       	}
		     	$("#listUpfiles_"+talkPId+"_upfileId").html('');

		     	//清除文件序列缓存
				$.each($("#thelistlistUpfiles_"+talkPId+"_upfileId"),function(index,item){
					$(this).find(".cancel").click();
				})
				$(relateTodoDiv).html("");
			    setStyle();
			   $(ts).attr("onclick",onclick);
			   resizeVoteH('otherAttrIframe');
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	showNotification(2,"操作失败")
				$(textarea).html('');
			    $(ts).attr("onclick",onclick);
	        }
	    });
	}
}
//删除评论
function delMeetTalk(id,isLeaf){
	window.top.layer.confirm("确定要删除该回复吗?",{title:"询问框",icon:3},function(index){
		window.top.layer.close(index);
		if(isLeaf==1){
			//异步提交表单删除评论
		    $("#meetTalkForm").ajaxSubmit({
		        type:"post",
		        url:"/meeting/delMeetTalk?sid=${param.sid}&t="+Math.random(),
		        data:{"id":id,"meetingId":${meetingId}},
		        dataType: "json", 
		        success:function(data){
		        	window.self.location.reload();
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        	showNotification(2,"操作失败")
		        }
		    });
		}else{
			window.top.layer.confirm("是否需要删除此节点下的回复信息?",{title:"询问框",icon:3,btn:['是','否']}
			,function(index){
				window.top.layer.close(index);
				//异步提交表单删除评论
			    $("#meetTalkForm").ajaxSubmit({
			        type:"post",
			        url:"/meeting/delMeetTalk?sid=${param.sid}&t="+Math.random(),
			        data:{"id":id,"meetingId":${meetingId},"delChildNode":"yes"},
			        dataType: "json", 
			        success:function(data){
			        	window.self.location.reload();
			        },
			        error:function(XmlHttpRequest,textStatus,errorThrown){
			        	showNotification(2,"操作失败")
			        }
			    });
			},function(index){
				window.top.layer.close(index);
				//异步提交表单删除评论
			    $("#meetTalkForm").ajaxSubmit({//删除自己
			        type:"post",
			        url:"/meeting/delMeetTalk?sid=${param.sid}&t="+Math.random(),
			        data:{"id":id,"meetingId":${meetingId},"delChildNode":"no"},
			        dataType: "json", 
			        success:function(data){
			        	window.self.location.reload();
			        },
			        error:function(XmlHttpRequest,textStatus,errorThrown){
			        	showNotification(2,"操作失败")
			        }
			    });
			});
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
	resizeVoteH('otherAttrIframe')
}
var fileTypes="*.rar;*.zip;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.pdf;*.mp3;*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.avi;*.wma;*.rmb;*.rm;*.flash;*.flv;*.mp4;*.mid;*.3gp";
var fileSize="200MB";
var	numberOfFiles = 200;
</script>
</head>
<body style="background-color: #ffffff"  onload="setStyle();resizeVoteH('otherAttrIframe');">
<form id="meetTalkForm" class="subform">
	<%--当前操作员的回复 --%>
	<div class="parentRep">
	
		<div class="ws-textareaBox" style="margin-top:10px;">
			<textarea id="operaterReplyTextarea_-1" style="height: 55px" class="form-control txtMeetTalk" placeholder="请输入内容……"></textarea>
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
				<div class="ws-plugs">
				    <a class="btn-icon" href="javascript:void(0)" title="学习人员"
				    data-todoUser="yes" data-relateDiv="todoUserDiv_-1">
				    	@
					</a>
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
					<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('${meetingId}',-1,this)" data-relateTodoDiv="todoUserDiv_-1">发表</button>
				</div>
				<div style="clear: both;"></div>
				<div id="todoUserDiv_-1" class="padding-top-10">
				</div>
				<div class="ws-notice">
					<tags:uploadMore name="listUpfiles_-1.upfileId" showName="filename" ifream="otherAttrIframe" comId="${userInfo.comId}"></tags:uploadMore>
				</div>
			</div>
		</div>
		<div class="ws-border-line" style="height: 15px"></div>
	</div>
	<%--当前讨论结束 --%>
	<%--列出有回复内容 --%>
	<div id="alltalks" style="clear:both">
		<c:if test="${not empty listMeetTalk}">
			<c:forEach items="${listMeetTalk}" var="meetTalk" varStatus="vs">
				<div id="talk${meetTalk.id}" class="ws-shareBox ${meetTalk.parentId==-1?'':'ws-shareBox2'} meetTalkInfo${meetTalk.parentId==-1?'P':''}">
					<div class="shareHead" data-container="body" data-toggle="popover" data-user='${meetTalk.talker}'
					data-busId='${meetingId}' data-busType='017'>
						<%--头像信息 --%>
						<img src="/downLoad/userImg/${meetTalk.comId}/${meetTalk.talker}?sid=${param.sid}" title="${meetTalk.talkerName}"></img>
					</div>
					<div class="shareText">
						<span class="ws-blue">${meetTalk.talkerName}</span>
						<c:if test="${meetTalk.parentId>-1}">
							<r>回复</r>
							<span class="ws-blue">${meetTalk.ptalkerName}</span>
						</c:if>
						<p class="ws-texts">
							${meetTalk.content}
						</p>
						<%--附件 --%>
						<c:choose>
							<c:when test="${not empty meetTalk.listMeetTalkFile}">
								<div class="file_div">
								<c:forEach items="${meetTalk.listMeetTalkFile}" var="upfiles" varStatus="vs">
									<c:choose>
										<c:when test="${upfiles.isPic==1}">
											<p class="p_text">
											附件(${vs.count})：
												<img onload="AutoResizeImage(350,0,this,'otherAttrIframe')"
													src="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" />
					 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
					 						&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${upfiles.uuid}/${upfiles.filename}','${param.sid}','${upfiles.upfileId}','017','${meetingId}')"></a>
											</p>
										</c:when>
										<c:otherwise>
											<p class="p_text">
											附件(${vs.count})：
												${upfiles.filename}
											<c:choose>
							 					<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx' }">
									 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
									 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','017','${meetingId}')"></a>
							 					</c:when>
							 					<c:when test="${upfiles.fileExt=='txt' || upfiles.fileExt=='pdf'}">
							 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
									 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','017','${meetingId}')"></a>
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
							<span class="hideOpt">
								<%--发言人可以删除自己的发言 --%>
								<c:if test="${userInfo.id==meetTalk.talker}">
									<a href="javascript:void(0);" id="delMeetTalk_${meetTalk.id}" onclick="delMeetTalk(${meetTalk.id},${meetTalk.isLeaf })" class="fa fa-trash-o" title="删除"></a>
								</c:if>
								<a id="img_${meetTalk.id}" name="replyImg" onclick="showArea('addTalk${meetTalk.id}')" href="javascript:void(0);" class="fa fa-comment-o" title="回复"></a>
							</span>
							<span>
								<time>${meetTalk.recordCreateTime}</time>
							</span>
						</div>
					</div>
					<div class="ws-clear"></div>
				</div>
				<!-- 回复层 -->
				<div id="addTalk${meetTalk.id}" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">
					<div class="shareText">
						<div class="ws-textareaBox" style="margin-top:10px;">
							<textarea id="operaterReplyTextarea_${meetTalk.id}" name="operaterReplyTextarea_${meetTalk.id}" style="height: 55px" rows="" cols="" class="form-control txtMeetTalk" placeholder="回复……"></textarea>
							<div class="ws-otherBox" style="position: relative;">
								<div class="ws-meh">
									<%--表情 --%>
									<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_${meetTalk.id}" onclick="addBiaoQingObj('biaoQingSwitch_${meetTalk.id}','biaoQingDiv_${meetTalk.id}','operaterReplyTextarea_${meetTalk.id}');"></a>
									<div id="biaoQingDiv_${meetTalk.id}" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 15px">
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
									<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_${meetTalk.id}','${param.sid}');" title="常用意见"></a>
								</div>
								<div class="ws-plugs">
									    <a class="btn-icon" href="javascript:void(0)" title="学习人员" data-todoUser="yes" data-relateDiv="todoUserDiv_${meetTalk.id}">
									    	@
										</a>
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
									<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('${meetingId}',${meetTalk.id },this,${meetTalk.talker})" data-relateTodoDiv="todoUserDiv_${meetTalk.id}">回复</button>
								</div>
								<%--相关附件 --%>
								<div style="clear: both;"></div>
								<div id="todoUserDiv_${meetTalk.id}" class="padding-top-10">
	             			
	           		 			</div>
								<div class="ws-notice">
									<tags:uploadMore
										name="listUpfiles_${meetTalk.id}.upfileId" showName="filename" ifream="otherAttrIframe" comId="${userInfo.comId}"></tags:uploadMore>
								</div>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:if>
	</div>
	<tags:pageBar url="/meeting/listPagedMeetTalk"></tags:pageBar>
</form>
<div style="clear: both;padding-top: 100px;" class="bg-white">
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
