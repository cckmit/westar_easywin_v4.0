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
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<!-- 自动补全js -->
<script type="text/javascript" src="/static/js/weekJs/weekreport.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script> 
//样式设置
function setStyle(){
	//文本框绑定回车提交事件
	$("textarea").autoTextarea({minHeight:55,maxHeight:160});  
	//文本框绑定回车提交事件
	$("textarea").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherIframe')
    });
	
	//聚焦留言
	var talkFocus = ${empty param.talkFocus?false:true};
	if(talkFocus){
		$("#operaterReplyTextarea_-1").focus();
	}
}

/**
 * weekReportId 投票主键
 * talkPId 回复的父节点
 */
function addTalk(weekReportId,talkPId,ts,ptalker){
	var textarea = $("#operaterReplyTextarea_"+talkPId);
    var content = $(textarea).val();
    if(strIsNull(content)){
        layer.tips('请编辑回复你的内容！', "#operaterReplyTextarea_"+talkPId, {tips: 1});
        $(textarea).focus();
        return;
    }else{
        var params = {
            "weekReportId":weekReportId,
            "parentId":talkPId,
            "content":content,
            "ptalker":ptalker,
            "upfilesId":$("#listUpfiles_"+talkPId+"_upfileId").val()
        }
        var relateTodoDiv = $("#"+$(ts).attr("data-relateTodoDiv"));
        if(relateTodoDiv && relateTodoDiv.get(0)){
            var todoUsers = $(relateTodoDiv).find("span");
            if(todoUsers && todoUsers.get(0)){
                $.each(todoUsers,function(index,user){
                    var user = $(this).data("userObj")
                    params["listWeekReportShareUser["+index+"].userId"] = user.userId;
                    params["listWeekReportShareUser["+index+"].userName"] = user.userName;
                })
            }
        }
        //异步提交表单 投票讨论
        $("#weekRepForm").ajaxSubmit({
            type:"post",
            url:"/weekReport/addWeekRepTalk?sid=${param.sid}&t="+Math.random(),
            beforeSubmit:function(a,o,f){
                if(content){
                 $(ts).attr("disabled","disabled");
                }else{
                    layer.tips("请填写内容", $("#operaterReplyTextarea_"+talkPId), {tips: 1});
                    $(textarea).focus();
                    return false;

                }
            },
            data:params,
            dataType: "json",
            traditional :true,
            success:function(data){
                var weekRepTalk = data.weekRepTalk;
                var html = getWeekRepTalkString(weekRepTalk,'${param.sid}',data.sessionUser,data.delWeek);
                $(textarea).val('');
                if(talkPId==-1){
                    $("#alltalks").prepend(html);
                }else{
                    //父节点
                    $("#delWeekTalk_"+talkPId).attr("onclick","delWeekTalk("+talkPId+",0)")
                    $("#talk_"+talkPId).after(html);
                    $("#addTalk"+talkPId).hide();
                }

                $("#listUpfiles_"+talkPId+"_upfileId").html('');

                //清除文件序列缓存
                $.each($("#thelistlistUpfiles_"+talkPId+"_upfileId"),function(index,item){
                    $(this).find(".cancel").click();
                })
				$("#todoUserDiv_"+talkPId).empty();
                setStyle();
                resizeVoteH('otherIframe');
                $(ts).removeAttr("disabled");
                $(".fa-comment").attr("title","回复");
                $(".fa-comment").attr("class","fa fa-comment-o");
                showNotification(1, "操作成功！");

            },
            error:function(XmlHttpRequest,textStatus,errorThrown){
                showNotification(2, "操作失败！");
                $(textarea).html('');
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
function delWeekTalk(id,isLeaf){
	//询问框
	window.top.layer.confirm("确定要删除该回复吗?", {
		  btn: ['确定','取消']//按钮
	  ,title:'询问框'
	  ,icon:3
	},  function(index){//删除叶子节点
		window.top.layer.close(index)
		if(isLeaf==1){
			//异步提交表单删除评论
		    $("#weekRepForm").ajaxSubmit({
		        type:"post",
		        url:"/weekReport/delWeekRepTalk?sid=${param.sid}&t="+Math.random(),
		        data:{"id":id,"weekReportId":${weekReportId}},
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
				}, function(index){//删除自己和子节点
					window.top.layer.close(index)
					//异步提交表单删除评论
				    $("#weekRepForm").ajaxSubmit({
				        type:"post",
				        url:"/weekReport/delWeekRepTalk?sid=${param.sid}&t="+Math.random(),
				        data:{"id":id,"weekReportId":${weekReportId},"delChildNode":"yes"},
				        dataType: "json", 
				        success:function(data){
				        	window.self.location.reload();
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        }
				    });
				},function(index){
					window.top.layer.close(index)
					//异步提交表单删除评论
				    $("#weekRepForm").ajaxSubmit({//删除自己
				        type:"post",
				        url:"/weekReport/delWeekRepTalk?sid=${param.sid}&t="+Math.random(),
				        data:{"id":id,"weekReportId":${weekReportId},"delChildNode":"no"},
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
            resizeVoteH('otherIframe')
        })
    })
    //信息推送移除
    $("body").on("dblclick","span[data-userId]",function(){
        $(this).remove();
        resizeVoteH('otherIframe')
    })
});
</script>
</head>
<body style="background-color:#FFFFFF" onload="resizeVoteH('otherIframe');setStyle();">
<form id="weekRepForm" class="subform">
	<c:if test="${empty update}">
		<div class="ws-textareaBox" style="margin-top:10px;">
			<textarea id="operaterReplyTextarea_-1" name="operaterReplyTextarea_-1" class="form-control" placeholder="请输入内容……"></textarea>
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
					<button type="button" class="btn btn-info ws-btnBlue" data-relateTodoDiv="todoUserDiv_-1" onclick="addTalk(${weekReportId},-1,this)">发表</button>
				</div>
				<div style="clear: both;"></div>
				<div id="todoUserDiv_-1" class="padding-top-10">

				</div>
				<div class="ws-notice">
					<tags:uploadMore name="listUpfiles_-1.upfileId" showName="filename" ifream="otherIframe" comId="${sessionUser.comId}"></tags:uploadMore>
				</div>
			</div>
		</div>
		<div class="ws-border-line"></div>
	</c:if>
	<div id="alltalks">
			<c:if test="${not empty weekTalks}">
				<c:forEach items="${weekTalks}" var="weekTalk" varStatus="vs">
					<div id="talk_${weekTalk.id}" class="ws-shareBox ${weekTalk.parentId==-1?'':'ws-shareBox2'}">
						<div class="shareHead" data-container="body" data-toggle="popover" 
						data-user='${weekTalk.talker}' data-busId='${weekReportId}' data-busType='006'>
							<img src="/downLoad/userImg/${weekTalk.comId}/${weekTalk.talker}?sid=${param.sid}" title="${weekTalk.talkerName }"></img>
						</div>
						<div class="shareText">
							<span class="ws-blue">${weekTalk.talkerName}</span>
							<c:if test="${weekTalk.parentId!=-1}">
								<r>回复</r>
								<span class="ws-blue">${weekTalk.ptalkerName}</span>
							</c:if>
							<p class="ws-texts">
								${weekTalk.content}
							</p>
							<%--附件 --%>
							<c:choose>
								<c:when test="${not empty weekTalk.listWeekRepTalkFile}">
									<div class="file_div">
									<c:forEach items="${weekTalk.listWeekRepTalkFile}" var="upfiles" varStatus="vs">
										<c:choose>
											<c:when test="${upfiles.isPic==1}">
												<p class="p_text">
												附件（${vs.count}）：
													<img onload="AutoResizeImage(350,0,this,'otherIframe')"
														src="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" />
						 						&nbsp;&nbsp;<a class="fa fa-download" title="下载" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
						 						&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${upfiles.uuid}/${upfiles.filename}','${param.sid}','${upfiles.upfileId}','006','${weekReportId}')"></a>
												</p>
											</c:when>
											<c:otherwise>
												<p class="p_text">
												附件（${vs.count}）：
													${upfiles.filename}
												<c:choose>
								 					<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx'}">
										 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
										 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','006','${weekReportId}')"></a>
								 					</c:when>
								 					<c:when test="${upfiles.fileExt=='txt' || upfiles.fileExt=='pdf'}">
								 						&nbsp;&nbsp;<a class="fa fa-download" title="下载" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
										 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','006','${weekReportId}')"></a>
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
								<c:if test="${sessionUser.id==weekTalk.talker && empty delete}">
									<a href="javascript:void(0);" id="delWeekTalk_${weekTalk.id}" onclick="delWeekTalk(${weekTalk.id},${weekTalk.isLeaf })"  class="fa fa-trash-o" title="删除"></a>
								</c:if>
								<c:if test="${empty update}">
									<a id="img_${weekTalk.id}" name="replyImg" href="javascript:void(0);" class="fa fa-comment-o" title="回复" onclick="showArea('addTalk${weekTalk.id}')"></a>
								</c:if>
								<time>${weekTalk.recordCreateTime}</time>
							</div>
						</div>
						<div class="ws-clear"></div>
					</div>
					<!-- 回复层 -->
					<div id="addTalk${weekTalk.id}" name="replyTalk" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">
						<div class="shareText">
							<div class="ws-textareaBox" style="margin-top:10px;">
								<textarea id="operaterReplyTextarea_${weekTalk.id}" name="operaterReplyTextarea_${weekTalk.id}" rows="" cols="" class="form-control" placeholder="回复……"></textarea>
								<div class="ws-otherBox">
									<div class="ws-meh">
										<%--表情 --%>
										<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_${weekTalk.id}" onclick="addBiaoQingObj('biaoQingSwitch_${weekTalk.id}','biaoQingDiv_${weekTalk.id}','operaterReplyTextarea_${weekTalk.id}');"></a>
										<div id="biaoQingDiv_${weekTalk.id}" class="blk" style="display:none;position:absolute;width:200px;top:100px;z-index:99;left: 15px">
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
										<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_${weekTalk.id}','${param.sid}');" title="常用意见"></a>
									</div>
									<div class="ws-plugs">
										<a class="btn-icon" href="javascript:void(0)" data-todoUser="yes" data-relateDiv="todoUserDiv_${weekTalk.id}" title="告知人员">
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
										<button type="button" class="btn btn-info ws-btnBlue" data-relateTodoDiv="todoUserDiv_${weekTalk.id}" onclick="addTalk(${weekReportId},${weekTalk.id },this,${weekTalk.talker})">回复</button>
									</div>
									<%--相关附件 --%>
									<div style="clear: both;"></div>
									<div id="todoUserDiv_${weekTalk.id}" class="padding-top-10">

									</div>
									<div class="ws-notice">
										<tags:uploadMore name="listUpfiles_${weekTalk.id}.upfileId" showName="filename" ifream="otherIframe" comId="${sessionUser.comId}"></tags:uploadMore>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</c:if>
		</div>
	<tags:pageBar url="/weekReport/weekRepTalkPage"></tags:pageBar>
</form>
<div style="clear: both;padding-top: 110px;" class="bg-white">
</div>	
	
	
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>

</body>
</html>

