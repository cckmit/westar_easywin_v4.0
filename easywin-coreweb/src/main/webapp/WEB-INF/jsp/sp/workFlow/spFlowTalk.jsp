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
<script type="text/javascript">

	$(function(){
		//初始化名片
		initCard('${param.sid}');
		$("#operaterReplyTextarea_-1").autoTextarea({minHeight:70,maxHeight:150});  
		//文本框绑定回车提交事件
		$("#operaterReplyTextarea_-1").bind("paste cut keydown keyup focus blur",function(event){
			resizeVoteH('otherSpAttrIframe');
	    });
		//聚焦留言
		var talkFocus = ${empty param.talkFocus?false:true};
		if(talkFocus){
			$("#operaterReplyTextarea_-1").focus();
		}
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
	function showArea(pTalkId){
		$("#operaterReplyTextarea_"+pTalkId).autoTextarea({minHeight:70,maxHeight:150});
		refreshIframe(pTalkId);
		if($("#reply_"+pTalkId).css('display')=="none"){
			$("[name='replyTalk']").hide(); 
			$("[name='replyImg']").attr("class","fa  fa-comment-o");
			$("[name='replyImg']").attr("title","回复"); 
		 	$("#reply_"+pTalkId).show();
			$("#operaterReplyTextarea_"+pTalkId).focus();
			$("#img_"+pTalkId).attr("class","fa fa-comment");
			$("#img_"+pTalkId).attr("title","隐藏");
	     }else{
	    	 $("#reply_"+pTalkId).hide();
			$("#img_"+pTalkId).attr("class","fa  fa-comment-o");
			$("[name='replyImg']").attr("title","回复");
    	 }
		//刷新付页面iframe高度
		resizeVoteH('otherSpAttrIframe');
	}

	//给回复textarea绑定刷新付页面iframe事件
	function refreshIframe(id){
		$(function(){
			$("#operaterReplyTextarea_"+id).bind("paste cut keydown keyup focus blur",function(event){
				resizeVoteH('otherSpAttrIframe');
		    });
		});
	}
	//讨论回复
	function replyTalk(busId,talkId,ts,ptalker){
		$(ts).attr("disabled","disabled");
		var params = {
            "content":$("#operaterReplyTextarea_"+talkId).val(),
            "busId":busId,
            "parentId":talkId,
            "pSpeaker":ptalker,
            "upfilesId":$("#listUpfiles_"+talkId+"_upfileId").val()
		}
		var relateTodoDiv = $("#"+$(ts).attr("data-relateTodoDiv"));
			var todoUsers = $(relateTodoDiv).find("span");
			if(todoUsers && todoUsers.get(0)){
				$.each(todoUsers,function(index,user){
					var user = $(this).data("userObj")
					params["listSpFlowNoticeUser["+index+"].noticeUserId"] = user.userId;
					params["listSpFlowNoticeUser["+index+"].noticeUserName"] = user.userName;
				})
			}
		$(".subform").ajaxSubmit({
	        type:"post",
	        url:"/workFlow/replyTalk?sid=${param.sid}&t="+Math.random(),
	        dataType: "json",
	        beforeSubmit:function (a,f,o){
			var content = $("#operaterReplyTextarea_"+talkId).val();
				if(strIsNull(content)){
	        		layer.tips('请编辑回复内容！', "#operaterReplyTextarea_"+talkId, {tips: 1});
					$("#operaterReplyTextarea_"+talkId).focus();
					$(ts).removeAttr("disabled");
					return false;
				}
			},
	        traditional :true,
	        data:params,
	        success:function(msgObjs){
	        	if(!msgObjs){
	        		showNotification(2,'服务已断开，请重新登陆！')
	        		 $(ts).removeAttr("disabled");
	        		return;
	        	}
	        	$("#noTalks").css("display","none");
	        	if(talkId==-1){//是回复
	        		$("#alltalks").prepend(msgObjs.spFlowTalkDivString);
			    }else{//是评论
			    	$("#reply_"+talkId).after(msgObjs.spFlowTalkDivString);
			    	$("#delOpt_"+talkId).attr("onclick","delTalk('"+talkId+"','0')")
				}
				$("#operaterReplyTextarea_"+talkId).attr("value","");
				//$("#reply_"+talkId+" .delVoteTalk a").attr("onclick","delTalk('"+talkId+"','0')")
		     	$("#reply_"+talkId).hide();
				$("#listUpfiles_"+talkId+"_upfileId").html(''); 
				//清除文件序列缓存
				$.each($("#thelistlistUpfiles_"+talkId+"_upfileId"),function(index,item){
					$(this).find(".cancel").click();
				})
				
				resizeVoteH("otherSpAttrIframe");
				$(ts).removeAttr("disabled");
				
				$("#todoUserDiv_"+talkId).empty();

				initCard('${param.sid}');
                showNotification(1,'操作成功！');
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	showNotification(2,'操作失败！');
		        $(ts).removeAttr("disabled");
	        }
	    });
		
	}
	
	//删除评论
	function delTalk(talkId,isLeaf){
		window.top.layer.confirm("确定要删除该回复吗?", {icon: 3, title:'确认对话框'}, function(index){
		  if(isLeaf==1){
				$.post("/workFlow/ajaxDelSpFlowTalk?sid=${param.sid}&delChildNode=no&random="+Math.random(),{Action:"post",id:talkId,busId:${spFlowTalk.busId}},     
				   function (msgObjs){ 
		    		$("#talk"+talkId).remove();
					showNotification(1,msgObjs);
					// 如果父页面重载或者关闭其子对话框全部会关闭
					window.location.reload();
				});
			}else{
				setTimeout(function(){
					window.top.layer.confirm("是否需要删除此节点下的回复信息?", {icon: 3, title:'确认对话框'}, function(index){
					  $.post("/workFlow/ajaxDelSpFlowTalk?sid=${param.sid}&delChildNode=yes&random="+Math.random(),{Action:"post",id:talkId,busId:${spFlowTalk.busId}},     
						   function (msgObjs){ 
				    		$("#talk"+talkId).remove();
							showNotification(1,msgObjs);
							// 如果父页面重载或者关闭其子对话框全部会关闭
							window.location.reload();
						});
					  layer.close(index);
					},function(){
						$.post("/workFlow/ajaxDelSpFlowTalk?sid=${param.sid}&delChildNode=no&random="+Math.random(),{Action:"post",id:talkId,busId:${spFlowTalk.busId}},     
						   function (msgObjs){ 
				    		$("#talk"+talkId).remove();
							showNotification(1,msgObjs);
							// 如果父页面重载或者关闭其子对话框全部会关闭
							window.location.reload();
						});
					});
				},200);
			}
		  layer.close(index);
		});
	}
	
	//页面刷新
	$(function(){
		$("#refreshImg").click(function(){
			window.location.reload();
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

	//@推送
    $(function(){
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
    });
</script>
<style>
.ws-wrap-width .ws-wrap-right{
width: 850px;
}
</style>
</head>
<body style="background-color:#FFFFFF" onload="resizeVoteH('otherSpAttrIframe')">
<form method="post" id="delForm" class="subform">
	<%--项目已经办结，不能进行讨论 --%>
	<c:if test="${ (spFlowInstance.flowState eq 1)}">
		<div class="ws-textareaBox" style="margin-top:10px;">
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
					<button type="button" class="btn btn-info ws-btnBlue" data-relateTodoDiv="todoUserDiv_-1" onclick="replyTalk(${spFlowTalk.busId},-1,this)">发表</button>
				</div>
				<div style="clear: both;"></div>
				<div id="todoUserDiv_-1" class="padding-top-10">

				</div>
				<div class="ws-notice">
					<tags:uploadMore name="listUpfiles_-1.upfileId" showName="filename" ifream="otherSpAttrIframe"></tags:uploadMore>
				</div>
			</div>
		</div>
		<div class="ws-border-line"></div>
	</c:if>
	<div id="alltalks">
	<input type="hidden" name="busId" value="${spFlowTalk.busId}"/>
		<c:if test="${not empty listSpFlowTalk}">
			<c:forEach items="${listSpFlowTalk}" var="listSpFlowTalkVo" varStatus="vs">
				<div id="talk_${listSpFlowTalkVo.id}" class="ws-shareBox ${listSpFlowTalkVo.parentId==-1?'':'ws-shareBox2'}">
					<div class="shareHead" data-container="body" data-toggle="popover"  
						data-user='${listSpFlowTalkVo.speaker}' data-busId='${spFlowTalk.busId}' data-busType='003'>
						<img src="/downLoad/userImg/${listSpFlowTalkVo.comId}/${listSpFlowTalkVo.speaker}?sid=${param.sid}" title="${listSpFlowTalkVo.speakerName}"></img>
					</div>
					<div class="shareText">
						<span class="ws-blue">${listSpFlowTalkVo.speakerName}</span>
						<c:if test="${listSpFlowTalkVo.parentId!=-1}">
							<r>回复</r>
							<span class="ws-blue">${listSpFlowTalkVo.pSpeakerName}</span>
						</c:if>
						<p class="ws-texts">
							<tags:viewTextArea>${listSpFlowTalkVo.content}</tags:viewTextArea>
						</p>
						<%--附件 --%>
						<c:choose>
							<c:when test="${not empty listSpFlowTalkVo.listSpFlowTalkUpfile}">
								<div class="file_div">
								<c:forEach items="${listSpFlowTalkVo.listSpFlowTalkUpfile}" var="upfiles" varStatus="vs">
									<c:choose>
										<c:when test="${upfiles.isPic==1}">
											<p class="p_text">
											附件（${vs.count}）：
												<img onload="AutoResizeImage(350,0,this,'otherSpAttrIframe')"
													src="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" />
					 						&nbsp;<a class="fa fa-download" title="下载" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
					 						&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${upfiles.uuid}/${upfiles.filename}','${param.sid}','${upfiles.upfileId}','003','${spFlowTalk.busId}')"></a>
											</p>
										</c:when>
										<c:otherwise>
											<p class="p_text">
											附件（${vs.count}）：
												${upfiles.filename}
											<c:choose>
							 					<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx' }">
									 				&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
									 				&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','003','${spFlowTalk.busId}')"></a>
							 					</c:when>
							 					<c:when test="${upfiles.fileExt=='txt' || upfiles.fileExt=='pdf'}">
							 						&nbsp;<a class="fa fa-download" title="下载" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
									 				&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','003','${spFlowTalk.busId}')"></a>
							 					</c:when>
							 					<c:otherwise>
									 				&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
							 					</c:otherwise>
							 				</c:choose>
											</p>
										</c:otherwise>
									</c:choose>
								</c:forEach>
								</div>
							</c:when>
						</c:choose>
						<div class="ws-type">
							<%--发言人可以删除自己的发言 --%>
							<c:if test="${userInfo.id==listSpFlowTalkVo.speaker && empty delete}">
								<a href="javascript:void(0);" id="delOpt_${listSpFlowTalkVo.id}" class="fa fa-trash-o" title="删除" onclick="delTalk('${listSpFlowTalkVo.id}','${listSpFlowTalkVo.isLeaf}')"></a>
							</c:if>
							<c:if test="${itemTalk.itemState!=4 && empty update}">
								<a id="img_${listSpFlowTalkVo.id}" name="replyImg" href="javascript:void(0);" class="fa fa-comment-o" title="回复" onclick="showArea('${listSpFlowTalkVo.id}')"></a>
							</c:if>
							<time>${listSpFlowTalkVo.recordCreateTime}</time>
						</div>
					</div>
					<div class="ws-clear"></div>
				</div>
				<!-- 回复层 -->
				<div id="reply_${listSpFlowTalkVo.id}" name="replyTalk" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3">
					<div class="shareText">
						<div class="ws-textareaBox" style="margin-top:10px;">
							<textarea id="operaterReplyTextarea_${listSpFlowTalkVo.id}" name="operaterReplyTextarea_${listSpFlowTalkVo.id}" rows="" cols="" class="form-control" placeholder="回复……"></textarea>
							<div class="ws-otherBox">
								<div class="ws-meh">
									<%--表情 --%>
									<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_${listSpFlowTalkVo.id}" onclick="addBiaoQingObj('biaoQingSwitch_${listSpFlowTalkVo.id}','biaoQingDiv_${listSpFlowTalkVo.id}','operaterReplyTextarea_${listSpFlowTalkVo.id}');"></a>
									<div id="biaoQingDiv_${listSpFlowTalkVo.id}" class="blk" style="display:none;position:absolute;width:200px;top:100px;z-index:99;left: 15px">
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
									<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_${listSpFlowTalkVo.id}','${param.sid}');" title="常用意见"></a>
								</div>
								<div class="ws-plugs">
									<a class="btn-icon" href="javascript:void(0)" data-todoUser="yes" data-relateDiv="todoUserDiv_${listSpFlowTalkVo.id}" title="告知人员">
										@
									</a>
								</div>
								<%--分享按钮 --%>
								<div class="ws-share">
									<button type="button" class="btn btn-info ws-btnBlue" data-relateTodoDiv="todoUserDiv_${listSpFlowTalkVo.id}" onclick="replyTalk(${spFlowTalk.busId},${listSpFlowTalkVo.id},this,${userInfo.id})">回复</button>
								</div>
								<%--相关附件 --%>
								<div style="clear: both;"></div>
								<div id="todoUserDiv_${listSpFlowTalkVo.id}" class="padding-top-10">

								</div>
								<div class="ws-notice">
									<tags:uploadMore name="listUpfiles_${listSpFlowTalkVo.id}.upfileId" showName="filename" ifream="otherSpAttrIframe"></tags:uploadMore>
								</div>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:if>
		<div align="center" style="display: ${empty listSpFlowTalk?'block':'none'}" id="noTalks"><h3 style="font-size:16px">没有留言！</h3></div>
	</div>
	<tags:pageBar url="/workFlow/spFlowTalkPage"></tags:pageBar>
</form>
 <div style="clear: both;padding-top: 100px;">
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
