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
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/msgshare.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">

$(document).ready(function() {
	resizeVoteH('otherIframe');
});

$(function(){
	setStyle()
	//初始化名片
	initCard('${param.sid}');
	//文本框绑定回车提交事件
	$(".txtTalk").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherIframe')
    });
	
	//聚焦留言
	var talkFocus = ${empty param.talkFocus?false:true};
	if(talkFocus){
		$("#operaterReplyTextarea_-1").focus();
	}
})
//样式设置
function setStyle(){
	//文本框绑定回车提交事件
	$(".txtTalk").autoTextarea({minHeight:55,maxHeight:160});  
}
/**
 * weekReportId 投票主键
 * talkPId 回复的父节点
 */
function addTalk(msgId,talkPId,ts,ptalker){
	var textarea = $("#operaterReplyTextarea_"+talkPId);
	var content = $(textarea).val();
	if(strIsNull(content)){
		layer.tips('请编辑回复你的内容！', "#operaterReplyTextarea_"+talkPId, {tips: 1});
		$(textarea).focus();
		return;
	}else{
		var params = {"msgId":msgId,
		        "parentId":talkPId,
		        "content":content,
		        "ptalker":ptalker,
		        "upfilesId":$("#listUpfiles_"+talkPId+"_upfileId").val()}
		var relateTodoDiv = $("#"+$(ts).attr("data-relateTodoDiv"));
		if(relateTodoDiv && relateTodoDiv.get(0)){
			var todoUsers = $(relateTodoDiv).find("span");
			if(todoUsers && todoUsers.get(0)){
				$.each(todoUsers,function(index,user){
					var user = $(this).data("userObj")
					params["listShareUser["+index+"].userId"] = user.userId;
					params["listShareUser["+index+"].userName"] = user.userName;
				})
			}
		}
		var onclick = $(ts).attr("onclick");
		//异步提交表单 投票讨论
	    $("#msgShareForm").ajaxSubmit({
	        type:"post",
	        url:"/msgShare/addMsgShareTalk?sid=${param.sid}&t="+Math.random(),
	        beforeSubmit:function(a,o,f){
	        	$(ts).removeAttr("onclick")
		    },
	        data:params,
	        dataType: "json", 
	        traditional :true,
	        success:function(data){
		        var msgTalk = data.msgTalk;
		       	var html = getMagShareTalkString(msgTalk,'${param.sid}',data.sessionUser);
		       	$(textarea).val('');
		       	if(talkPId==-1){
		       		$("#alltalks").prepend(html);
		       	}else{
			     	//父节点
			     	$("#delMsgTalk_"+talkPId).attr("onclick","delMsgTalk("+talkPId+",0)")
		       		$("#talk_"+talkPId).after(html);
			     	$("#addTalk"+talkPId).hide();
			     	
		       	}

		     	$(".fa-comment").attr("title","回复");
				$(".fa-comment").attr("class","fa fa-comment-o");
		     	$("#listUpfiles_"+talkPId+"_upfileId").html('');

		     	//清除文件序列缓存
				$.each($("#thelistlistUpfiles_"+talkPId+"_upfileId"),function(index,item){
					$(this).find(".cancel").click();
				})
				$(relateTodoDiv).html("");
			    setStyle();
			    resizeVoteH('otherIframe')
		       	$(ts).attr("onclick",onclick);
		       	initCard('${param.sid}')
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	showNotification(2,"操作失败")
	        	$(textarea).val('');
				$(ts).attr("onclick",onclick);
	        }
	    });
	}
}

/**
 * 显示回复的回复
 */
function showArea(priId){
	var addtalks = $(".addTalk");
	$(".fa-comment").attr("title","回复");
	$(".fa-comment").attr("class","fa fa-comment-o");
	for(var i=0;i<addtalks.length;i++){
		var talkId = $(addtalks[i]).attr("id");
		var imgId = "img_"+talkId.replace("addTalk","");
		if(talkId==priId){
			 if($("#"+priId).css('display')=="none"){
		     	$("#"+imgId).attr("title","隐藏");
		     	$("#"+imgId).attr("class","fa fa-comment");
			 	$("#"+priId).show();
		     }else{
		     	$("#"+priId).hide();
		     	$("#"+imgId).attr("title","回复");
		     	$("#"+imgId).attr("class","fa fa-comment-o");
		     }
		}else{
			$("#"+talkId).hide();
	     	$("#"+imgId).attr("title","回复");
	     	$("#"+imgId).attr("class","fa fa-comment-o");
		}
	}
	resizeVoteH('otherIframe')
}
//删除评论
function delMsgTalk(id,isLeaf){
	window.top.layer.confirm('确定要删除该回复吗?', {
		  btn: ['确定','取消']//按钮
		  ,title:'询问框'
		  ,icon:3
		},  function(index){//删除叶子节点
			window.top.layer.close(index);
		if(isLeaf==1){
			//异步提交表单删除评论
		    $("#msgShareForm").ajaxSubmit({
		        type:"post",
		        url:"/msgShare/delMsgShareTalk?sid=${param.sid}&t="+Math.random(),
		        data:{"id":id,"msgId":${msgShareTalk.msgId}},
		        dataType: "json", 
		        success:function(data){
		        	window.self.location.reload();
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        }
		    });
		}else{
			setTimeout(function(){
				window.top.layer.confirm("是否需要删除此节点下的回复信息?", {
					  btn: ['是','否']//按钮
				  ,title:'询问框'
				  ,icon:3
				},  function(index){//删除自己和子节点
					window.top.layer.close(index);
					//异步提交表单删除评论
				    $("#msgShareForm").ajaxSubmit({
				        type:"post",
				        url:"/msgShare/delMsgShareTalk?sid=${param.sid}&t="+Math.random(),
				        data:{"id":id,"msgId":${msgShareTalk.msgId},"delChildNode":"yes"},
				        dataType: "json", 
				        success:function(data){
				        	window.self.location.reload();
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        }
				    });
				},function(index){
					window.top.layer.close(index);
					//异步提交表单删除评论
				    $("#msgShareForm").ajaxSubmit({//删除自己
				        type:"post",
				        url:"/msgShare/delMsgShareTalk?sid=${param.sid}&t="+Math.random(),
				        data:{"id":id,"msgId":${msgShareTalk.msgId},"delChildNode":"no"},
				        dataType: "json", 
				        success:function(data){
				        	window.self.location.reload();
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        }
				    });
				});
			},200);
		}
	});	
}


	
	//页面刷新
	$(function(){
		$("#refreshImg").click(function(){
			var win = art.dialog.open.origin;//来源页面
			// 如果父页面重载或者关闭其子对话框全部会关闭
			win.location.reload();
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

	
	
	//查看更多
	function more(sid) {
		var minId = $("#minId").val();
		$.ajax({
			type : "post",
			url : "/msgShare/nextPageSizeMsgTalks?sid="+sid+"&msgId="+${param.msgId},
			dataType:"json",
			traditional :true, 
			data:{pageSize:8,
			minId:minId
			},
			  beforeSend: function(XMLHttpRequest){
	         },
			  success : function(data){
				  if(data.length==0){
		        		$("#moreMsg").hide();
		        		return;
		        	}
		        	if(data.length<8){
		     			$("#moreMsg").hide();
		     		}else{
		     			$("#moreMsg").show();
		     		}
		        	
		        	var html = ""
		        	for(var i=0;i<data.length;i++){
		        		 var msgTalk = data[i];
		        		 if( minId ==0 ){
		        			 $("#minId").val(msg.id);
		        		 }else if(msgTalk.id<minId ){
		        			 minId = msgTalk.id;
		        			 $("#minId").val(minId); 
		        		 }
		        		 html += getMagShareTalkString(msgTalk,sid);
		        	}
		        	
		        	 $("#moreMsg").before(html);
		        	 resizeVoteH('otherIframe');
			  },
			  error:  function(XMLHttpRequest, textStatus, errorThrown){
				  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
		      }
		}); 
		
	}

	
	
</script>
</head>
<body style="background-color:#FFFFFF" onload="resizeVoteH('otherIframe')">

	<%--页面刷新结束 --%>
	<form id="msgShareForm" class="subform">
		<div class="ws-textareaBox" style="margin-top:10px;">
			<textarea id="operaterReplyTextarea_-1" name="operaterReplyTextarea_-1" class="form-control txtTalk" placeholder="请输入内容……"></textarea>
			<div class="ws-otherBox">
				<div class="ws-meh" style="position: relative;">
					<%--表情 --%>
					<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch" onclick="addBiaoQingObj('biaoQingSwitch','biaoQingDiv','operaterReplyTextarea_-1');"></a>
					<div id="biaoQingDiv" class="blk" style="display:none;position:absolute;width:200px;top:0px;z-index:99;left: 20px">
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
				    <a class="btn-icon" href="javascript:void(0)" title="告知人员"
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
					<button type="button" class="btn btn-info ws-btnBlue" data-relateTodoDiv="todoUserDiv_-1"
					 onclick="addTalk('${msgShareTalk.msgId}',-1,this)">发表</button>
				</div>
				<div style="clear: both;"></div>
				<div id="todoUserDiv_-1" class="padding-top-10">
	             			
	            </div>
				<div class="ws-notice">
					<tags:uploadMore name="listUpfiles_-1.upfileId" showName="filename" ifream="otherIframe" comId="${userInfo.comId}"></tags:uploadMore>
				</div>
			</div>
			<div class="ws-border-line"></div>
			<%--当前操作者评论结束 --%>
			<%--列出有回复内容 --%>
			<div id="alltalks">
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="msgTalk" varStatus="vs">
						<div id="talk_${msgTalk.id}" class="ws-shareBox">
							<div class="shareHead" data-container="body" data-toggle="popover" data-user='${msgTalk.speaker}'
							data-busId='${msgShareTalk.msgId}' data-busType='1'>
								<img src="/downLoad/userImg/${msgTalk.comId}/${msgTalk.talker}?sid=${param.sid}" title="${msgTalk.talkerName}"></img>
							</div>
							<div class="shareText">
								<span class="ws-blue">${msgTalk.talkerName}</span>
								<c:if test="${msgTalk.parentId>-1}">
									<r>回复</r>
									<span class="ws-blue">${msgTalk.ptalkerName}</span>
								</c:if>
								<p class="ws-texts">
									${msgTalk.content}
								</p>
								<%--附件 --%>
								<c:choose>
									<c:when test="${not empty msgTalk.msgShareTalkUpfiles}">
										<div class="file_div">
										<c:forEach items="${msgTalk.msgShareTalkUpfiles}" var="upfiles" varStatus="vs">
											<c:choose>
												<c:when test="${upfiles.isPic==1}">
													<p class="p_text">
													附件（${vs.count}）：
														<img src="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" width="100%" height="100%"/>
							 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
							 						&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${upfiles.uuid}/${upfiles.filename}','${param.sid}','${upfiles.upfileId}','1','${msgShareTalk.msgId}')"></a>
													</p>
												</c:when>
												<c:otherwise>
													<p class="p_text">
													附件（${vs.count}）：
														${upfiles.filename}
													<c:choose>
									 					<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx' }">
											 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
											 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','1','${msgShareTalk.msgId}')"></a>
									 					</c:when>
									 					<c:when test="${upfiles.fileExt=='txt' || upfiles.fileExt=='pdf'}">
									 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
											 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','1','${msgShareTalk.msgId}')"></a>
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
								<div class="ws-type">
									<%--发言人可以删除自己的发言 --%>
									<c:if test="${userInfo.id==msgTalk.speaker}">
										<a href="javascript:void(0);" id="delMsgTalk_${msgTalk.id}" onclick="delMsgTalk(${msgTalk.id},${msgTalk.isLeaf })"  class="fa fa-trash-o" title="删除"></a>
									</c:if>
									<a id="img_${msgTalk.id}" name="replyImg" href="javascript:void(0);" class="fa fa-comment-o" title="回复" onclick="showArea('addTalk${msgTalk.id}')"></a>
									<br><time>${msgTalk.recordCreateTime}</time>
								</div>
							</div>
							<div class="ws-clear"></div>
						</div>
						
						<!-- 回复层 -->
						<div id="addTalk${msgTalk.id}" name="replyTalk" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">
							<div class="shareText">
								<div class="ws-textareaBox" style="margin-top:10px;">
									<textarea id="operaterReplyTextarea_${msgTalk.id}" name="operaterReplyTextarea_${msgTalk.id}" rows="" cols="" class="form-control txtTalk" placeholder="回复……"></textarea>
									<div class="ws-otherBox">
										<div class="ws-meh" style="position: relative;">
											<%--表情 --%>
											<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_${msgTalk.id}" onclick="addBiaoQingObj('biaoQingSwitch_${msgTalk.id}','biaoQingDiv_${msgTalk.id}','operaterReplyTextarea_${msgTalk.id}');"></a>
											<div id="biaoQingDiv_${msgTalk.id}" class="blk" style="display:none;position:absolute;width:200px;top:0px;z-index:99;left: 20px">
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
											<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_${msgTalk.id}','${param.sid}');" title="常用意见"></a>
										</div>
										<div class="ws-plugs">
										    <a class="btn-icon" href="javascript:void(0)" title="告知人员"
										    	data-todoUser="yes" data-relateDiv="todoUserDiv_${msgTalk.id}">
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
											<button type="button" class="btn btn-info ws-btnBlue" 
											data-relateTodoDiv="todoUserDiv_${msgTalk.id}"
											onclick="addTalk(${msgShareTalk.msgId},${msgTalk.id },this,${msgTalk.speaker})">回复</button>
										</div>
										<%--相关附件 --%>
										<div style="clear: both;"></div>
										<div id="todoUserDiv_${msgTalk.id}" class="padding-top-10">
	             			
	           		 					</div>
										<div class="ws-notice">
											<tags:uploadMore name="listUpfiles_${msgTalk.id}.upfileId" showName="filename" ifream="otherIframe" comId="${userInfo.comId}"></tags:uploadMore>
										</div>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
					<input type="hidden" id="minId" value="${minId}"/>
					<li style="margin-top:10px;display: ${list.size() >= 8? 'block':'none'}" align="center" id="moreMsg">
						<div style="margin:0 0;" align="center">
							<a href="javascript:void(0)" style="font-size:15px;" onclick="more('${param.sid}')">查看更多</a>
						</div>
					</li>
				</c:if>
				
			</div>
			<%--列出有回复内容 结束--%>
		</div>
	</form>
	
	<%-- <tags:pageBar url="/msgShare/msgShareTalkPage"></tags:pageBar> --%>
<div style="clear: both;padding-top: 110px;" class="bg-white">
</div>	

<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
