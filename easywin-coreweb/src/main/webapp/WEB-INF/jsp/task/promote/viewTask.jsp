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
<link href="/static/assets/css/task.css" rel="stylesheet" type="text/css">
<script src="/static/js/taskJs/taskCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/taskJs/taskOpt.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
var sid="${param.sid}"
	var EasyWin = {
        "ewebMaxMin":"self"
        ,"sid":"${param.sid}"
        ,"redirectPage":"${param.redirectPage}"
        ,"userInfo":{
        	"id":"${userInfo.id}"
        	,"comId":"${userInfo.comId}"
        }
        ,"task":{
        	"id":"${task.id}"
        	,"state":"${task.state}"
        	,"executeState":"${task.executeState}"
            ,"version":"${task.version}"
        }
   };
//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
//注入父页面信息
function setWindow(winDoc,win){
	openWindowDoc = winDoc;
	openWindow = win;
	openPageTag = openWindow.pageTag;
	openTabIndex = openWindow.tabIndex;
}
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
//autoCompleteCallBack回调对象标识
var autoCompleteCallBackVar =null;
$(function(){
	
	taskEditForm.init();
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		datatype:{
			"input":function(gets,obj,curform,regxp){
				var str = $(obj).val();
				if(str){
					var count = str.replace(/[^\x00-\xff]/g,"**").length;
					var len = $(obj).attr("defaultLen");
					if(count>len){
						return false;
					}else{
						return true;
					}
				}else{
					return false;
				}
			}
		},
		callback:function (form){
			//提交前验证是否在上传附件
			return sumitPreCheck(null);
		},
		showAllError : true
	}); 

	//任务描述的图片查看
	var imgArray = $("#comment").find("img");
	$.each(imgArray,function(index,item){
		var src = $(this).attr("src");
		$(this).css("max-width","550px");	
		
		$(this).click(function(){
			window.top.viewOrgByPath(src);
		})
	})
	
	//任务暂停
	$("body").on("click",".pauseTaskBtn",function(){
		pauseTask(${task.id},${task.state});//暂停任务
	});
    //任务转为日程
    $("body").on("click",".conversionBtn",function(){
    	var paramObj = {};
    	paramObj.scheStartDate = new Date().Format("yyyy-MM-dd");
    	var d1 = new Date(paramObj.scheStartDate.replace(/\-/g, "\/"));
    	
    	var dealTimeLimit = $("#schDealTimeLimit").val();
    	if(dealTimeLimit){
	    	var d2 = new Date(dealTimeLimit.replace(/\-/g, "\/")); 
    		if( d1<=d2){
    			paramObj.scheEndDate = dealTimeLimit;
    		}
    	}
    	paramObj.title = $("#schTaskName").val();
    	paramObj.content = $("#taskRemark").val();
    	if(!paramObj.content){
    		paramObj.content = $("#comment").html();
    	}
        conversionSchedule('${param.sid}',paramObj,${task.state});//转为日程
    });
});
//任务属性菜单切换
$(function(){
	
	//任务详情
	$("body").on("click","#taskbaseMenuLi",function(){
		$("#otherTaskAttrIframe").css("display","none");
		$("#taskBase").css("display","block");
		$("#taskTalkMenuLi").parent().find("li").removeAttr("class");
		$("#taskbaseMenuLi").attr("class","active");
	})
	//任务讨论
	$("body").on("click","#taskTalkMenuLi,#headTaskTalk",function(){
		$("#otherTaskAttrIframe").css("display","block");
		$("#taskBase").css("display","none");
		$("#taskTalkMenuLi").parent().find("li").removeAttr("class");
		$("#taskTalkMenuLi").attr("class","active");
		$("#otherTaskAttrIframe").attr("src","/task/taskTalkPage?sid=${param.sid}&pager.pageSize=10&taskId=${task.id}&taskState=${task.state}");
		
	})
	
	//移交记录
	$("body").on("click","#taskFlowRecordLi",function(){
		$("#otherTaskAttrIframe").css("display","block");
		$("#taskBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#taskFlowRecordLi").attr("class","active");
		$("#otherTaskAttrIframe").attr("src","/task/taskFlowRecord?sid=${param.sid}&taskId=${task.id}");
		
	})
	//任务日志
	$("body").on("click","#taskLogMenuLi",function(){
		$("#otherTaskAttrIframe").css("display","block");
		$("#taskBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#taskLogMenuLi").attr("class","active");
		$("#otherTaskAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${task.id}&busType=003&ifreamName=otherTaskAttrIframe");
	})
	//任务附件
	$("body").on("click","#taskUpfileMenuLi",function(){
		$("#otherTaskAttrIframe").css("display","block");
		$("#taskBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#taskUpfileMenuLi").attr("class","active");
		$("#otherTaskAttrIframe").attr("src","/task/taskUpfilePage?sid=${param.sid}&pager.pageSize=10&taskId=${task.id}");
		
	})
	//任务浏览记录
	$("body").on("click","#taskViewRecord",function(){
		$("#otherTaskAttrIframe").css("display","block");
		$("#taskBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#taskViewRecord").attr("class","active");
		$("#otherTaskAttrIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${task.id}&busType=003&ifreamName=otherTaskAttrIframe");
		
	})
	//任务协同
	$("body").on("click","#nextExecutor,#turnBackTask",function(){
		var id = $(this).attr("id");
		$.post("/task/checkTaskStateForNextExecutor?sid=${param.sid}&id=${task.id}",{Action:"post"},
		 	function (msgObjs){
			if(!msgObjs["succ"]){
				showNotification(2,msgObjs.promptMsg);
			}else{
				var taskBackWay = 0;
				if(id =='turnBackTask'){
					taskBackWay = 1;
				}
				nextExcuter(taskBackWay);
			}
		},"json");
		
	})
	//任务协同
	$("body").on("click","#handOverTask",function(){
		$.post("/task/checkTaskStateForNextExecutor?sid=${param.sid}&id=${task.id}",{Action:"post"},
		 	function (msgObjs){
			if(!msgObjs["succ"]){
				showNotification(2,msgObjs.promptMsg);
			}else{
				handOverTask();
			}
		},"json");
		
	})
	//任务进度汇报
	$("body").on("mouseup",".progress-drag-btn",function(){
		tag = false;
		//任务进度汇报
		taskOptForm.updateTaskProgress();
	});
	//任务重启
	$("body").on("click",".restartTaskBtn",function(){
		restartTask(${task.id},${task.state});//重启任务
	});
	 //任务复制
    $("body").on("click",".copyBtn",function(){
        copyTask(${task.id},"${param.sid}",${task.busType});//任务复制
    });
});

//委托给别人执行
function nextExcuter(taskBackWay){
	var url = "/task/nextExecutorPage?sid=${param.sid}";
	if(taskBackWay ==1){
		url = "/task/turnBackPage?sid=${param.sid}";
	}
	url = url+"&id=${task.id}";
	url = url+"&redirectPage="+encodeURIComponent('${param.redirectPage}');
	window.top.layer.open({
		 type: 2,
		  //title: ['任务协同', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['600px', '450px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: [url,'no'],
		  btn: ['确定', '取消'],
		  success: function(layero, index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				//关闭窗口
				$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					  window.top.layer.close(index);
				  });
		  },
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  
			  if(iframeWin){
				  var info = iframeWin.formSub();
				  var status = info.status;
				  var nextExecutorId = info.executor;
				  
				  if(status=='f2'){
					  window.top.layer.msg(info.info,{icon: 2,skin:"showNotification",time:1800});
					  return;
				  }
				  if(status == 'y' || status=='f1'){
					window.top.layer.msg(info.info,{icon: (status == 'y'?1:2),skin:"showNotification",time:1800});
				   	window.top.layer.close(index);
				   	if(openPageTag=='index'){//首页
						window.top.layer.close(openTabIndex);
						if(nextExecutorId!='${userInfo.id}'){
							openWindow.removeTodoTask(openPageTag,${task.id},'003');
						}
				   	}else if(openPageTag=='taskTodo'){//任务待办
				   		window.top.layer.close(openTabIndex);
				   		if(nextExecutorId!='${userInfo.id}'){
				   			openWindow.removeTaskTodo(${task.id});
							openWindow.loadTaskTodo();
						}
				   	}else if(openPageTag=='allTodo'){//说所有待办
				   		window.top.layer.close(openTabIndex);
				   		if(nextExecutorId!='${userInfo.id}'){
							openWindow.removeTaskTodo(${task.id});
							openWindow.loadOtherTodo();
						}
					}else{
						window.top.layer.close(openTabIndex);
						openWindow.location.reload();
					}
				  }
			  }
		  },cancel: function(){ 
		    //右上角关闭回调
		  }
	});
}
//委托给别人执行
function handOverTask(){
	window.top.layer.open({
		 type: 2,
		  //title: ['任务协同', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['600px', '450px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/task/handOverTaskPage?sid=${param.sid}&id=${task.id}&redirectPage="+encodeURIComponent('${param.redirectPage}'),'no'],
		  btn: ['确定', '取消'],
		  success: function(layero, index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				//关闭窗口
				$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					  window.top.layer.close(index);
				  });
		  },
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  
			  if(iframeWin){
				  var info = iframeWin.formSub();
				  var status = info.status;
				  var nextExecutorId = info.executor;
				  if(status == 'y' || status=='f1'){
					window.top.layer.msg(info.info,{icon: (status == 'y'?1:2),skin:"showNotification",time:1800});
				   	window.top.layer.close(index);
				   	if(openPageTag=='index'){//首页
						window.top.layer.close(openTabIndex);
						if(nextExecutorId!='${userInfo.id}'){
							openWindow.removeTodoTask(openPageTag,${task.id},'003');
						}
				   	}else if(openPageTag=='taskTodo'){//任务待办
				   		window.top.layer.close(openTabIndex);
				   		if(nextExecutorId!='${userInfo.id}'){
				   			openWindow.removeTaskTodo(${task.id});
							openWindow.loadTaskTodo();
						}
				   	}else if(openPageTag=='allTodo'){//说所有待办
				   		window.top.layer.close(openTabIndex);
				   		if(nextExecutorId!='${userInfo.id}'){
							openWindow.removeTaskTodo(${task.id});
							openWindow.loadOtherTodo();
						}
					}else{
						window.top.layer.close(openTabIndex);
						openWindow.location.reload();
					}
				  }
			  }
		  },cancel: function(){ 
		    //右上角关闭回调
		  }
	});
}
//初始化任务参与人
$(document).ready(function(){
	if(!strIsNull($("#taskSharerJson").val())){
		var users = eval("("+$("#taskSharerJson").val()+")");
		var img="";
		for (var i=0, l=users.length; i<l; i++) {
			img = img + "<div class=\"online-list\" style=\"float:left\">";
			img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
			img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
			img = img + "</div>"
		}
		$("#taskSharor_div").html(img);
	}
	//取得闹铃列表信息
	ajaxListClockForOne('003','${task.id}','${param.sid}',function(data){
		var listClocks = data.listClocks;
		if(listClocks && listClocks.length>0){
			$.each(listClocks,function(index,clockObj){
				var  clockHtml= conStrClockHtml('003','${task.id}','${param.sid}',clockObj);
				$("#busClockList").append(clockHtml)
			})
		}
	});
});
</script>
</head>
<body>
<form id="subform" class="subform" method="post">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
               			<a href="javascript:void(0)" class="widget-caption blue padding-right-5"
               				attentionState="${task.attentionState}" busType="003" busId="${task.id}" describe="0" iconSize="sizeMd"
               				onclick="changeAtten('${param.sid}',this)" title="${task.attentionState==0?"关注":"取消"}">
							<i class="fa ${task.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
						</a>
							
                        <span class="widget-caption themeprimary" style="font-size: 20px">任务</span>
                        <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px">
                        	<c:choose>
                        		<c:when test="${fn:length(task.taskName)>20 }">
		                        	--${fn:substring(task.taskName,0,20)}..
                        		</c:when>
                        		<c:otherwise>
                        			--${task.taskName}
                        		</c:otherwise>
                        	</c:choose>
                        </span>
                        <div class="widget-buttons ps-toolsBtn taskOptMenu">
                        	<!--任务的操作按钮信息 见 .taskOptMenu-->
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                           <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;">
                                             <li class="active" id="taskbaseMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">任务详情</a>
                                             </li>
                                             <li id="taskTalkMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">任务留言<c:if test="${task.msgNum > 0}"><span style="color:red;font-weight:bold;">（${task.msgNum}）</span></c:if></a>
                                             </li>
                                             <li id="taskUpfileMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">任务文档<c:if test="${task.docNum > 0}"><span style="color:red;font-weight:bold;">（${task.docNum}）</span></c:if></a>
                                             </li>
                                             <li id="taskLogMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">操作日志</a>
                                             </li>
                                             <%--<li id="taskFlowRecordLi">--%>
                                                 <%--<a data-toggle="tab" href="javascript:void(0)">协办记录</a>--%>
                                             <%--</li>--%>
                                             <%--<li id="taskViewRecord">--%>
                                                 <%--<a data-toggle="tab" href="javascript:void(0)">最近查看</a>--%>
                                             <%--</li>--%>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<div id="taskBase" style="display:block;">
												<jsp:include page="./taskBase_view.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherTaskAttrIframe" style="display:none;" class="layui-layer-load"
												src="/task/taskTalkPage?sid=${param.sid}&pager.pageSize=10&taskId=${task.id}&taskState=${task.state}"
												border="0" frameborder="0" allowTransparency="true"
												noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
                                    	 </div>
                                	</div>
                            	</div>
                            </div> 
                        </div>
					</div>
				</div>
			</div>
		</div>
	</form>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
