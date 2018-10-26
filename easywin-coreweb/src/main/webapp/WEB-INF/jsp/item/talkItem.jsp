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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">

	$(function(){
		//初始化名片
		$("#operaterReplyTextarea_-1").autoTextarea({minHeight:70,maxHeight:160});  
		$("#operaterReplyTextarea_-1").bind("paste cut keydown keyup focus blur",function(event){
			resizeVoteH('otherItemAttrIframe');
	    });
		//聚焦留言
		var talkFocus = ${empty param.talkFocus?false:true};
		if(talkFocus){
			$("#operaterReplyTextarea_-1").focus();
		}
		resizeVoteH('otherItemAttrIframe');
	});
	
	//页面刷新
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
	
	/**
	 * 显示回复的回复
	 */
	function showArea(priId){
		$("#operaterReplyTextarea_"+priId).autoTextarea({minHeight:55,maxHeight:160});
		refreshIframe(priId);
		if($("#reply_"+priId).css('display')=="none"){
			$("[name='replyTalk']").hide(); 
			$("[name='replyImg']").attr("class","fa  fa-comment-o");
			$("[name='replyImg']").attr("title","回复"); 
		 	$("#reply_"+priId).show();
			$("#operaterReplyTextarea_"+priId).focus();
			$("#img_"+priId).attr("class","fa fa-comment");
			$("#img_"+priId).attr("title","隐藏");
	     }else{
	    	 $("#reply_"+priId).hide();
			$("#img_"+priId).attr("class","fa  fa-comment-o");
			$("[name='replyImg']").attr("title","回复");
    	 }
		//刷新付页面iframe高度
		resizeVoteH('otherItemAttrIframe');
	}

	//给回复textarea绑定刷新付页面iframe事件
	function refreshIframe(id){
		$("#operaterReplyTextarea_"+id).bind("paste cut keydown keyup focus blur",function(event){
			resizeVoteH('otherItemAttrIframe');
	    });
	}
	//讨论回复
	function replyTalk(itemTalkId,ts){
		var params = {"content":$("#operaterReplyTextarea_"+itemTalkId).val(),
		        "itemId":${itemTalk.itemId},
		        "parentId":itemTalkId,
		        "upfilesId":$("#listUpfiles_"+itemTalkId+"_upfileId").val()}
		var relateTodoDiv = $("#"+$(ts).attr("data-relateTodoDiv"));
		if(relateTodoDiv && relateTodoDiv.get(0)){
			var todoUsers = $(relateTodoDiv).find("span");
			if(todoUsers && todoUsers.get(0)){
				$.each(todoUsers,function(index,user){
					var user = $(this).data("userObj")
					params["listItemSharers["+index+"].userId"] = user.userId;
					params["listItemSharers["+index+"].userName"] = user.userName;
				})
			}
		}
		$(".subform").ajaxSubmit({
	        type:"post",
	        url:"/item/replyTalk?sid=${param.sid}&t="+Math.random(),
	        dataType: "json",
	        beforeSubmit:function (a,f,o){
				var content = $("#operaterReplyTextarea_"+itemTalkId).val();
				if(strIsNull(content)){
					layer.tips('请编辑回复你的内容！', "#operaterReplyTextarea_"+itemTalkId, {tips: 1});
					$("#operaterReplyTextarea_"+itemTalkId).focus();
					return false;
				}else{
					$(ts).attr("disabled","disabled");
				}
			},
	        traditional :true,
	        data:params,
	        success:function(msgObjs){
			        if(itemTalkId==-1){//是回复
		        		$("#alltalks").prepend(msgObjs.itemTalkDivString);
				    }else{//是评论
				    	$("#talk_"+itemTalkId).after(msgObjs.itemTalkDivString);
				    	$("#delOpt_"+itemTalkId).attr("onclick","delItemTalk('"+itemTalkId+"','0')")
					}
					
					$("#operaterReplyTextarea_"+itemTalkId).attr("value","");
					
			     	$("#reply_"+itemTalkId).hide();
			     	$("#img_"+itemTalkId).attr("class","fa  fa-comment-o");
			     	$("[name='replyImg']").attr("title","回复");
					$("#listUpfiles_"+itemTalkId+"_upfileId").html(''); 
			     	//清除文件序列缓存
					$.each($("#thelistlistUpfiles_"+itemTalkId+"_upfileId"),function(index,item){
						$(this).find(".cancel").click();
					})
					$(relateTodoDiv).html("");
					$(ts).removeAttr("disabled"); 
					resizeVoteH("otherItemAttrIframe");
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	window.top.layer.msg("系统错误，请联系管理人员",{icon:2})
		        $(ts).removeAttr("disabled");
	        }
	    });
	}
	
	//删除评论
	function delItemTalk(talkId,isLeaf){
		window.top.layer.open({
			  content: '确定要删除该回复吗?'
			  ,btn: ['确定', '取消']
				,title:'询问框'
		  		,icon:3
			  ,yes: function(index, layero){
				  window.top.layer.close(index);
				  if(isLeaf==1){
					  $.post("/item/ajaxDelItemTalk?sid=${param.sid}&delChildNode=no&random="+Math.random(),{Action:"post",id:talkId,itemId:${itemTalk.itemId}},     
						   function (msgObjs){ 
				    		$("#talk"+talkId).remove();
				    		window.top.layer.msg(msgObjs,{title:false,icon:1,closeBtn:0});
							// 如果父页面重载或者关闭其子对话框全部会关闭
							window.self.location.reload();
						});
				  }else{
					  window.top.layer.open({
						  content: '是否需要删除此节点下的回复信息?'
						  ,btn: ['是', '否']
					  	,title:'询问框'
					  	,icon:3
						  ,yes: function(index, layero){
							  $.post("/item/ajaxDelItemTalk?sid=${param.sid}&delChildNode=yes&random="+Math.random(),{Action:"post",id:talkId,itemId:${itemTalk.itemId}},     
								   function (msgObjs){ 
						    		$("#talk"+talkId).remove();
						    		window.top.layer.msg(msgObjs,{title:false,icon:1,closeBtn:0});
									// 如果父页面重载或者关闭其子对话框全部会关闭
									window.self.location.reload();
								});
							  window.top.layer.close(index)
						  },btn2: function(){
							  $.post("/item/ajaxDelItemTalk?sid=${param.sid}&delChildNode=no&random="+Math.random(),{Action:"post",id:talkId,itemId:${itemTalk.itemId}},     
								   function (msgObjs){ 
						    		$("#talk"+talkId).remove();
						    		window.top.layer.msg(msgObjs,{title:false,icon:1,closeBtn:0});
									// 如果父页面重载或者关闭其子对话框全部会关闭
									window.self.location.reload();
								});
							  window.top.layer.close(index)
						  },cancel: function(){ 
						    //右上角关闭回调
						  }
						});
				  }
			  },cancel: function(){ 
			    //右上角关闭回调
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
	var fileTypes="*.rar;*.zip;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.pdf;*.mp3;*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.avi;*.wma;*.rmb;*.rm;*.flash;*.flv;*.mp4;*.mid;*.3gp";
	var fileSize="200MB";
	var	numberOfFiles = 200;
	
</script>
</head>
<body style="background-color:#FFFFFF" onload="resizeVoteH('otherItemAttrIframe')">
	<form method="post" id="delForm" class="subform">
		<div id="tab1" class="tab-pane in active bg-white">
	    	<div class="panel-body no-padding">
	    		<%--项目已经办结，不能进行讨论 --%>
				<c:if test="${itemTalk.itemState!=4 && empty update}">
				
					<!--直接反馈-->
		    		<div class="well padding-bottom-10">
		    			<span class="input-icon icon-right">
		                	<textarea class="form-control" id="operaterReplyTextarea_-1"
		                	placeholder="请输入内容……" style="height:70px;"></textarea>
		                </span>
		                <div class="panel-body" style="padding-right:0;">
		                	<div class="buttons-preview pull-left" style="position: relative;">
		                    	<a class="btn-icon fa fa-meh-o fa-lg" href="javascript:void(0)" id="biaoQingSwitch" 
		                    		onclick="addBiaoQingObj('biaoQingSwitch','biaoQingDiv','operaterReplyTextarea_-1');">
								</a>
								<div id="biaoQingDiv" class="blk" style="display:none;position:absolute;width:200px;top:15px;z-index:99;left: 15px">
									<!--表情DIV层-->
							        <div class="main">
							            <ul style="padding: 0px">
							            <jsp:include page="/biaoqing.jsp"></jsp:include>
							            </ul>
							        </div>
							    </div>
								<a href="javascript:void(0)" class="btn-icon fa fa-comments-o fa-lg" onclick="addIdea('operaterReplyTextarea_-1','${param.sid}');" title="常用意见"></a>
		                   		<a class="btn-icon fa-lg" href="javascript:void(0)" title="告知人员"
							   	 data-todoUser="yes" data-relateDiv="todoUserDiv_-1">
							    	@
								</a>
		                    </div>
		                   <!--  <div class="checkbox pull-left no-margin">
		                    	<label>通知方式：</label>
								<label class="no-padding">
									<input type="checkbox">
									<span class="text">邮件</span>
								</label>
								<label>
									<input type="checkbox">
									<span class="text">短信</span>
								</label>
							</div> -->
							<div class="pull-right">
								<a href="javascript:void(0)" class="btn btn-info" onclick="replyTalk(-1,this)" data-relateTodoDiv="todoUserDiv_-1">发表</a>
							</div>
							<div style="clear: both;"></div>
							<div id="todoUserDiv_-1" class="padding-top-10">
				             			
				            </div>
							<div class="ws-notice">
								<tags:uploadMore name="listUpfiles_-1.upfileId" showName="filename" ifream="otherItemAttrIframe" comId="${userInfo.comId}"></tags:uploadMore>
							</div>
		                </div>
		    		</div>
				</c:if>
				
				<!-- 留言信息 -->
				<div id="alltalks">
					<c:if test="${not empty listItemTalk}">
						<c:forEach items="${listItemTalk}" var="listItemTalkVo" varStatus="vs">
							<div class="comment" id="talk_${listItemTalkVo.id}">
	    		 				<c:if test="${listItemTalkVo.parentId!=-1 }">
	    		 					<div class="comment">
	    		 				</c:if>
								<img src="/downLoad/userImg/${listItemTalkVo.comId}/${listItemTalkVo.userId}?sid=${param.sid}" 
								title="${listItemTalkVo.speakerName}" class="comment-avatar"></img>
									
	    		 				
	    		 				
	    		 				<div class="comment-body">
	                            	<div class="comment-text">
	                                	<div class="comment-header">
	                                    	<a>${listItemTalkVo.speakerName}</a>
	                                    	<c:if test="${listItemTalkVo.parentId!=-1}">
												<r style="color:#000;margin:0px 5px">回复</r>
												<a>${listItemTalkVo.pSpeakerName}</a>
											</c:if>
	                                    	<span>${fn:substring(listItemTalkVo.recordCreateTime,0,16)}</span>
	                                     </div>
	                                     <p class="no-margin-bottom">${listItemTalkVo.content}</p>
	                                     <p class="no-margin-bottom">
	                                    	<%--附件 --%>
											<c:choose>
												<c:when test="${not empty listItemTalkVo.listItemTalkFile}">
													<div class="file_div">
													<c:forEach items="${listItemTalkVo.listItemTalkFile}" var="upfiles" varStatus="vs">
														<c:choose>
															<c:when test="${upfiles.isPic==1}">
																<p class="p_text">
																附件（${vs.count}）：
																	<img onload="AutoResizeImage(350,0,this,'otherItemAttrIframe')"
																		src="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" />
										 						&nbsp;<a class="fa fa-download" title="下载" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
										 						&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${upfiles.uuid}/${upfiles.filename}','${param.sid}','${upfiles.upfileId}','005','${itemTalk.itemId}')"></a>
																</p>
															</c:when>
															<c:otherwise>
																<p class="p_text">
																附件（${vs.count}）：
																	${upfiles.filename}
																<c:choose>
												 					<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx'}">
														 				&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
														 				&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','005','${itemTalk.itemId}')"></a>
												 					</c:when>
												 					<c:when test="${upfiles.fileExt=='txt' || upfiles.fileExt=='pdf'}">
												 						&nbsp;<a class="fa fa-download" title="下载" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}"  title="下载"></a>
														 				&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','005','${itemTalk.itemId}')"></a>
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
	                                     </p>
	                                </div>
	                                <div class="comment-footer">
	                                   	<%--模块操作权限验证 --%>
										<%--发言人可以删除自己的发言 --%>
										<c:if test="${empty delete  && userInfo.id==listItemTalkVo.userId}">
											<a href="javascript:void(0)" id="delOpt_${listItemTalkVo.id}" title="删除" onclick="delItemTalk('${listItemTalkVo.id}','${listItemTalkVo.isLeaf}')">
												<i class="fa fa-trash-o"></i>
											</a>
										</c:if>
										<c:if test="${empty update}">
											<a id="img_${listItemTalkVo.id}" name="replyImg" href="javascript:void(0);" class="fa fa-comment-o" title="回复" onclick="showArea('${listItemTalkVo.id}')"></a>
										</c:if>
	                                 </div>
	                                 <!-- 回复层 -->
	                                 <div class="panel-body" id="reply_${listItemTalkVo.id}" name="replyTalk" style="display:none;">
	                                    <textarea class="form-control" id="operaterReplyTextarea_${listItemTalkVo.id}" name="operaterReplyTextarea_${listItemTalkVo.id}"
	                                     rows="" cols="" placeholder="请输入内容……" style="height:55px;"></textarea>
	                                    <div class="panel-body" style="padding-right:0;">
	                                   		<div class="buttons-preview pull-left" style="position: relative;">
	                                    		<%--表情 --%>
												<a href="javascript:void(0);" class="btn-icon fa fa-meh-o fa-lg" id="biaoQingSwitch_${listItemTalkVo.id}" onclick="addBiaoQingObj('biaoQingSwitch_${listItemTalkVo.id}','biaoQingDiv_${listItemTalkVo.id}','operaterReplyTextarea_${listItemTalkVo.id}');"></a>
												<div id="biaoQingDiv_${listItemTalkVo.id}" class="blk" style="display:none;position:absolute;width:200px;top:10px;z-index:99;left: 15px">
													<!--表情DIV层-->
											        <div class="main">
											            <ul style="padding: 0px">
											            <jsp:include page="/biaoqing.jsp"></jsp:include>
											            </ul>
											        </div>
											    </div>
												<a href="javascript:void(0);" class="btn-icon fa fa-comments-o fa-lg" onclick="addIdea('operaterReplyTextarea_${listItemTalkVo.id}','${param.sid}');" title="常用意见"></a>
	                                       		 <a class="btn-icon fa-lg" href="javascript:void(0)" title="告知人员" data-todoUser="yes" data-relateDiv="todoUserDiv_${listItemTalkVo.id}">@</a>
	                                       </div>
	                                       <!-- <div class="checkbox pull-left no-margin">
	                                       		<label>通知方式：</label>
												<label class="no-padding">
													<input type="checkbox">
													<span class="text">邮件</span>
												</label>
												<label>
													<input type="checkbox">
													<span class="text">短信</span>
												</label>
											</div> -->
											<div class="pull-right">
												<a href="javascript:void(0)" class="btn btn-info" onclick="replyTalk(${listItemTalkVo.id},this)" data-relateTodoDiv="todoUserDiv_${listItemTalkVo.id}">发表</a>
											</div>
											<div style="clear: both;"></div>
											<div id="todoUserDiv_${listItemTalkVo.id}" class="padding-top-10"></div>
											
											<div class="ws-notice">
												<tags:uploadMore name="listUpfiles_${listItemTalkVo.id}.upfileId" showName="filename" ifream="otherCustomerAttrIframe" comId="${userInfo.comId}"></tags:uploadMore>
											</div>
	                                     </div>
	                                 </div>
	                             </div>
	    		 				<c:if test="${listItemTalkVo.parentId!=-1 }">
	    		 					</div>
	    		 				</c:if>
	    		 			</div>
						</c:forEach>
					</c:if>
				</div>
	    	</div>
	    </div>
		<tags:pageBar url="/item/itemTalkPage"></tags:pageBar>
	</form>
	 <div style="clear: both;padding-top: 100px;" class="bg-white">
</div>
	<%--用与测量当前页面的高度 --%>
	<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
