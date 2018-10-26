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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script> 
$(function(){
	//初始化名片
	$(".txtFileTalk").autoTextarea({minHeight:55,maxHeight:160});  
	$(".txtFileTalk").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherFileIframe')
    });
})
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
//样式设置
function setStyle(){
	$(".txtFileTalk").autoTextarea({minHeight:55,maxHeight:160});  
}
/**
 * busId 业务主键
 * busType 业务类型
 * talkPId 回复的父节点
 * ts 当前节点
 */
function addTalk(busId,busType,talkPId,ts){
	var textarea = $("#operaterReplyTextarea_"+talkPId);
	var content = $(textarea).val();
	if(strIsNull(content)){
		layer.tips('请填写内容！', "#operaterReplyTextarea_"+talkPId, {tips: 1});
		$(textarea).focus();
		return;
	}else{
		var onclick = $(ts).attr("onclick");
		//异步提交表单 投票讨论
	    $("#fileForm").ajaxSubmit({
	        type:"post",
	        url:"/fileCenter/addFileTalk?sid=${param.sid}&t="+Math.random(),
	        beforeSubmit:function(a,o,f){
		        $(ts).removeAttr("onclick");
		    },
	        data:{"busId":busId,
		        "busType":busType,
		        "parentId":talkPId,
		        "fileId":${fileId},
		        "talkContent":content},
	        dataType: "json", 
	        success:function(data){
	        	var fileTalk = data.fileTalk;
			    var html = getFileTalkStr(fileTalk,'${param.sid}',data.sessionUser);
			    $(textarea).val('');
			    if(talkPId==-1){
		       		$("#alltalks").prepend(html);
		       	}else{
		       		//父节点
			     	$("#delFileTalk_"+talkPId).attr("onclick","delFileTalk("+talkPId+",0)")
		       		$("#talk"+talkPId).after(html);
			     	$("#addTalk"+talkPId).hide();
			     	$("#img_"+talkPId).attr("class","fa fa-comment-o");
		       	}
			    setStyle();
			   $(ts).attr("onclick",onclick);
			   resizeVoteH('otherFileIframe');
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	layer.msg("操作失败",{icon:2});
				$(textarea).html('');
			    $(ts).attr("onclick",onclick);
	        }
	    });
	}
}

function getFileTalkStr(fileTalk,sid,sessionUser){
	var html = '';
	html+='\n<div id="talk'+fileTalk.id+'" class="comment">';
	if(fileTalk.parentId>-1){
	html+='\n	<div class="comment">';
	}
	//头像信息
	html += '	<img src="/downLoad/userImg/'+fileTalk.comId+'/'+fileTalk.talker+'?sid='+sid+'" title="'+fileTalk.talkerName+'" class="comment-avatar"></img>\n';
	html+='\n		<div class="comment-body">';
	html+='\n			<div class="comment-text">';
	html+='\n				<div class="comment-header">';
	html+='\n					<a>'+fileTalk.talkerName+'</a>';
	if(fileTalk.parentId>-1){
		html+='\n				<r style="color:#000;margin:0px 5px">回复</r>';
		html+='\n				<a>'+fileTalk.ptalkerName+'</a>';
	}
	html+='\n 					<span>'+fileTalk.recordCreateTime.substring(0,16)+'</span>';
	html+='\n				</div>';
	html+='\n				<p class="no-margin-bottom">'+fileTalk.talkContent+'</p>';
	html+='\n 			</div>';
	html+='\n 			<div class="comment-footer">';
	//发言人可以删除自己的发言 
	html+='\n 				<a href="javascript:void(0)" title="删除" id="delFileTalk_'+fileTalk.id+'" onclick="delFileTalk('+fileTalk.id+',1,this)">';
	html+='\n 					<i class="fa fa-trash-o"></i></a>';
	html+='\n 				<a id="img_'+fileTalk.id+'" name="replyImg" onclick="showArea(\'addTalk'+fileTalk.id+'\')"  href="javascript:void(0);" class="fa fa-comment-o" title="回复"></a>';
	html+='\n 			</div>';
	//回复层
	html+='\n 			<div class="panel-body addTalk" id="addTalk'+fileTalk.id+'" style="display:none;">';
	html+='\n 				<textarea class="form-control txtFileTalk" id="operaterReplyTextarea_'+fileTalk.id+'" name="operaterReplyTextarea_'+fileTalk.id+'"  placeholder="请输入内容……" style="height:55px;"></textarea>';
	
	html+='\n	<div class="buttons-preview pull-left" style="position: relative;">';
	html+='\n	<div class="ws-meh" style="position: relative;">';
	html+='\n		<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_'+fileTalk.id+'" onclick="addBiaoQingObj(\'biaoQingSwitch_'+fileTalk.id+'\',\'biaoQingDiv_'+fileTalk.id+'\',\'operaterReplyTextarea_'+fileTalk.id+'\');"></a>';
	html+='\n		<div id="biaoQingDiv_'+fileTalk.id+'" class="blk" style="display:none;position:absolute;width:200px;top:0px;z-index:99;left: 20px">';
	html+='\n	        <div class="main">';
	html+='\n	            <ul style="padding: 0px">';
	html+='\n'	           +getBiaoqing();
	html+='\n	            </ul>';
	html+='\n	        </div>';
	html+='\n	    </div>';
	html+='\n	</div>';
	html+='\n	</div>';
	html+='\n	<div class="ws-plugs" style="width: 30%;float: left;margin-left: 7px;">';
	html+='\n	<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+fileTalk.id+'\',\'${param.sid}\');" title="常用意见"></a>';
	html+='\n	</div>';
	
	
	
	html+='\n				<div class="panel-body" style="padding-right:0;width: 30%;float: right;">';
	html+='\n					<div class="pull-right">';
	html+='\n						<a href="javascript:void(0)" class="btn btn-info" onclick="addTalk('+fileTalk.busId+',\''+fileTalk.busType+'\','+fileTalk.id+',this)">回复</a>';
	html+='\n					</div>';
	html+='\n					<div style="clear: both;"></div>';
	html+='\n				</div>';
	html+='\n			</div>';
	html+='\n		</div>';
	if(fileTalk.parentId>-1){
		html+='\n</div>';
	}
	html+='\n</div>';
  	return html;
}

//删除评论
function delFileTalk(id,isLeaf,ts){
		window.top.layer.confirm('确定要删除该回复吗?', {icon: 3, title:'提示',closeBtn: 0,btn: ['确定', '取消']}, function(index){
			if(isLeaf==1){
				//异步提交表单删除评论
			    $("#fileForm").ajaxSubmit({
			        type:"post",
			        url:"/fileCenter/delFileTalk?sid=${param.sid}&t="+Math.random(),
			        data:{"id":id},
			        dataType: "json", 
			        success:function(data){
			        	window.top.layer.close(index);
			        	window.self.location.reload();
			        },
			        error:function(XmlHttpRequest,textStatus,errorThrown){
			        	window.top.layer.msg("操作失败",{icon:2});
			        	window.top.layer.close(index);
			        }
			    });
			}else{
				window.top.layer.close(index);
				window.top.layer.confirm('是否需要删除此节点下的回复信息?', {icon: 3, title:'提示',closeBtn: 0,btn: ['是', '否']}, function(index){
					//异步提交表单删除评论
				    $("#fileForm").ajaxSubmit({
				        type:"post",
				        url:"/fileCenter/delFileTalk?sid=${param.sid}&t="+Math.random(),
				        data:{"id":id,"delChildNode":"yes"},
				        dataType: "json", 
				        success:function(data){
				        	window.top.layer.close(index);
				        	window.self.location.reload();
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        	window.top.layer.msg("操作失败",{icon:2});
				        	window.top.layer.close(index);
				        }
				    });
				},function(index){
					//异步提交表单删除评论
				    $("#fileForm").ajaxSubmit({//删除自己
				        type:"post",
				        url:"/fileCenter/delFileTalk?sid=${param.sid}&t="+Math.random(),
				        data:{"id":id,"delChildNode":"no"},
				        dataType: "json", 
				        success:function(data){
				        	window.top.layer.close(index);
				        	window.self.location.reload();
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        	window.top.layer.msg("操作失败",{icon:2});
				        	window.top.layer.close(index);
				        }
				    });
				});
			}
		},function(index){
			window.top.layer.close(index);
		});
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
	 resizeVoteH('otherFileIframe')
}
</script>
</head>
<body style="background-color: #fff"  onload="setStyle();resizeVoteH('otherFileIframe');">
<form id="fileForm" class="subform">
	<div id="tab1" class="tab-pane in active bg-white" >
    	<div class="panel-body no-padding">
    	<!--直接反馈-->
    		<div class="well">
    			<span class="input-icon icon-right">
                	<textarea class="form-control txtFileTalk" id="operaterReplyTextarea_-1"
                	placeholder="请输入内容……" style="height:55px;"></textarea>
                </span>
                <div class="ws-otherBox" >
                	<div class="buttons-preview pull-left" style="position: relative;">
                    	<a class="btn-icon fa fa-meh-o fa-lg" href="javascript:void(0)" id="biaoQingSwitch" 
                    		onclick="addBiaoQingObj('biaoQingSwitch','biaoQingDiv','operaterReplyTextarea_-1');">
						</a>
						<div id="biaoQingDiv" class="blk" style="display:none;position:absolute;width:200px;top:0px;z-index:99;left: 5px">
							<!--表情DIV层-->
					        <div class="main">
					            <ul class="no-padding">
					            <jsp:include page="/biaoqing.jsp"></jsp:include>
					            </ul>
					        </div>
					    </div>
                    </div>
                    <div class="ws-plugs" style="width: 30%;float: left;">
						<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_-1','${param.sid}');" title="常用意见"></a>
					</div>
					<div class="pull-right">
						<a href="javascript:void(0)" class="btn btn-info" onclick="addTalk('${busId}','${busType}',-1,this)">发表</a>
					</div>
					<div style="clear: both;"></div>
                </div>
    		</div>
    		
    		<!--已反馈的信息-->
    		 <div id="alltalks">
    		 	<c:if test="${not empty fileTalks}">
    		 		<c:forEach items="${fileTalks}" var="fileTalk" varStatus="vs">
    		 			<div class="comment" id="talk${fileTalk.id}">
    		 				<c:if test="${fileTalk.parentId!=-1 }">
    		 					<div class="comment">
    		 				</c:if>
							<img src="/downLoad/userImg/${fileTalk.comId}/${fileTalk.userId}?sid=${param.sid}" 
							title="${fileTalk.talkerName}" class="comment-avatar"></img>
                        	<div class="comment-body">
                            	<div class="comment-text">
                                	<div class="comment-header">
                                    	<a>${fileTalk.talkerName}</a>
                                    	<c:if test="${fileTalk.parentId!=-1}">
											<r style="color:#000;margin:0px 5px">回复</r>
											<a>${fileTalk.ptalkerName}</a>
										</c:if>
                                    	<span>${fn:substring(fileTalk.recordCreateTime,0,16)}</span>
                                     </div>
                                     <p class="no-margin-bottom">${fileTalk.talkContent}</p>
                                </div>
                                <div class="comment-footer padding-bottom-10">
                                   	<%--模块操作权限验证 --%>
									<%--发言人可以删除自己的发言 --%>
									<c:if test="${sessionUser.id==fileTalk.userId}">
										<a href="javascript:void(0)" title="删除" id="delFileTalk_${fileTalk.id}"
										onclick="delFileTalk(${fileTalk.id},${fileTalk.isLeaf},this)">
											<i class="fa fa-trash-o"></i>
										</a>
									</c:if>
									<c:if test="${empty update}">
										<a id="img_${fileTalk.id}" name="replyImg" onclick="showArea('addTalk${fileTalk.id}')"
										 href="javascript:void(0);" class="fa fa-comment-o" title="回复"></a>
									</c:if>
                                 </div>
                                 <!-- 回复层 -->
                                 <div class="panel-body addTalk" id="addTalk${fileTalk.id}" style="display:none;">
                                    <textarea class="form-control txtFileTalk" id="operaterReplyTextarea_${fileTalk.id}" name="operaterReplyTextarea_${fileTalk.id}"
                                     rows="" cols="" placeholder="请输入内容……" style="height:55px;"></textarea>
                                    <div class="buttons-preview pull-left" style="position: relative;">
                                    	<div class="ws-meh" style="position: relative;">
											<%--表情 --%>
											<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_${fileTalk.id}" onclick="addBiaoQingObj('biaoQingSwitch_${fileTalk.id}','biaoQingDiv_${fileTalk.id}','operaterReplyTextarea_${fileTalk.id}');"></a>
											<div id="biaoQingDiv_${fileTalk.id}" class="blk" style="display:none;position:absolute;width:200px;top:0px;z-index:99;left: 20px">
												<!--表情DIV层-->
										        <div class="main">
										            <ul style="padding: 0px">
										            <jsp:include page="/biaoqing.jsp"></jsp:include>
										            </ul>
										        </div>
										    </div>
										</div>
									</div>
										<%--常用意见 --%>
										<div class="ws-plugs" style="width: 30%;float: left;margin-left: 7px;">
											<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_${fileTalk.id}','${param.sid}');" title="常用意见"></a>
										</div>
                                    <div class="panel-body" style="padding-right:0;width: 30%;float: right;">
										<div class="pull-right">
											<a href="javascript:void(0)" class="btn btn-info" 
											onclick="addTalk('${busId}','${busType}',${fileTalk.id},this)">回复</a>
										</div>
										<div style="clear: both;"></div>
                                     </div>
                                 </div>
                             </div>
                             
                             <c:if test="${fileTalk.parentId!=-1 }">
    		 					</div>
    		 				</c:if>
                         </div>
    		 		</c:forEach>
    		 	</c:if>
    		 </div>
    	</div>
    </div>

	<tags:pageBar url="/fileCenter/listPagedFileTalks" maxIndexPages="3"></tags:pageBar>
</form>
<div style="clear: both;padding-top: 110px;" class="bg-white">
</div>	
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
