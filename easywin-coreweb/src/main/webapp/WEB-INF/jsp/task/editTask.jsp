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
<script type="text/javascript">
var sid="${param.sid}";//身份验证码
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
var regex = /['|<|>|"]+/;
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//初始化名片
	initCard('${param.sid}')
	//$("#taskRemark").autoTextarea({minHeight:110,maxHeight:150}); 
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
						return "任务名称太长";
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
	//任务分解
	$("body").on("click",".resolveTask",function(){
		window.top.layer.open({
			 type: 2,
			 title:false,
			 closeBtn: 0,
			  area: ['800px', '550px'],
			  fix: true, //不固定
			  maxmin: false,
			  content: ["/task/resolveTaskPage?sid=${param.sid}&parentId=${task.id}",'no'],
			  btn: ['发布','取消'],
			  yes: function(index, layero){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  var result = iframeWin.formSub();
				  if(result){
					  window.self.location.reload();
					  window.top.layer.close(index);
					  window.top.layer.msg("创建成功",{icon:1})
				  }
			  },cancel: function(){
			  }
		});
	})
	//任务名称更新
	$("#taskName").change(function(){
		//任务名称
		var taskName = $("#taskName").val();
		if(regex.test(taskName)){
			return false;
		}
		//关联项目的长度，汉字算两个长度
		var count = taskName.replace(/[^\x00-\xff]/g,"**").length;
		var len = $("#taskName").attr("defaultLen");
		//关联项目长度超过指定的长度
		if(count>len){
			return false;
		}else{
			if(!strIsNull(taskName)){
				$.post("/task/taskNameUpdate?sid=${param.sid}",{Action:"post",id:${task.id},taskName:taskName},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
					$("#taskName").attr("title",taskName);
					taskName = '--'+cutstr(taskName,32);
					$("#titleTaskName").html(taskName);
				});
			}
		}
	});
	//任务紧急度更新
	$("#grade").change(function(){
		//任务紧急度
		if($("#grade").val()){
			$.post("/task/taskGradeUpdate?sid=${param.sid}",{Action:"post",id:${task.id},grade:$("#grade").val()},     
				function (data){
				if(data.status=='y'){
					showNotification(1,data.info);
	 			}else{
					showNotification(2,data.info);
		 		}
			},"json");
		}
	});
	//是否为空判断
	function isNull(obj){
		if(obj!="" && obj!="null" && obj!=null && obj!="undefined" && obj!=undefined){
			return false;
		}else{
			return true;
		}
	}
	//任务说明更新
	$("#taskRemark").change(function(){
		if(!strIsNull($("#taskRemark").val())){
			$.post("/task/taskTaskRemarkUpdate?sid=${param.sid}",{Action:"post",id:${task.id},taskRemark:$("#taskRemark").val()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	});
	
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
	$("body").on("click","#nextExecutor",function(){
		$.post("/task/checkTaskStateForNextExecutor?sid=${param.sid}&id=${task.id}",{Action:"post"},     
		 	function (msgObjs){
			if(!msgObjs["succ"]){
				showNotification(2,msgObjs.promptMsg);
			}else{
				nextExcuter();
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
	//查看关联
	$("body").on("click",".relateClz",function(){
		var actObj = $(this);
		var busId = $(actObj).attr("busId");
		var busType = $(actObj).attr("busType");
		var param = {
				busId:busId,
				busType:busType,
				clockId:-1,
				baseId:"${task.id}"
		}
		authBaseCheck(param,function(authState){
			var url = "";
			if(busType=='005'){
				url = "/item/viewItemPage?sid="+sid+"&id="+busId;
			}else if(busType=='012'){
				url = "/crm/viewCustomer?sid="+sid+"&id="+busId;
			}else if(busType=='003'){
				url = "/task/viewTask?sid="+sid+"&id="+busId;
			}else if(busType=='022'){
				url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+busId;
			}
			openWinWithPams(url,"800px",($(window).height()-90)+"px");
		})
	});

	//展开跟多的区域
	$(".inner").click(function(){
        var moreOptShow = $("#moreOpt").css("display");
        if(moreOptShow=='none'){
            $("#moreOpt").slideDown("fast");
            $("#moreOptImg").removeClass("fa-angle-down");
            $("#moreOptImg").addClass("fa-angle-up");
            document.getElementById('task-shrink-name').innerHTML="收起"
        }else{
            $("#moreOpt").slideUp("fast");
            $("#moreOptImg").removeClass("fa-angle-up");
            $("#moreOptImg").addClass("fa-angle-down");
            document.getElementById('task-shrink-name').innerHTML="更多及事件关联"
        }
    });
	//关联li点击事件定义
	$("#moreOpt li").click(function(){
		var actObj = $(this);
		var busType = $(actObj).attr("busType");
		if(busType=="012"){
			$(".relativeRow").remove();//暂时先单选关联
			crmMoreSelect(1,null,function(crms){
                crmMoreSelectBack(crms,busType);
			})
		}else if(busType=="005"){
			$(".relativeRow").remove();//暂时先单选关联
			itemMoreSelect(1, null,function(items){
                itemMoreSelectBack(items,busType);
			})
		}else if(busType=="022"){
			$(".relativeRow").remove();//暂时先单选关联
			spFlowMoreSelect(1, null,function(spFlow){
				spFlowMoreSelectBack(spFlow,busType);
			})
		}else if(busType=="003"){//任务关联
			var oldId = "${task.id}";
			listTaskForRelevance(sid,function(pTask){
                taskForRelevanceBack(pTask);
			})
		}
	});
	//关联控件点击绑定
	$("body").on("click",".colAdd",function(){
		var actObj = $(this);
		var busType = $(actObj).attr("busType");
		$(".subform [name='busType']").val(busType);
		if(busType=="012"){
			crmMoreSelect(1,null,function(crms){
                crmMoreSelectBack(crms,busType);
			})
		}else if(busType=="005"){
			itemMoreSelect(1, null,function(items){
                itemMoreSelectBack(items,busType);
			});
		}else if(busType=="022"){
			$(".relativeRow").remove();//暂时先单选关联
			spFlowMoreSelect(1, null,function(spFlow){
				spFlowMoreSelectBack(spFlow,busType);
			})
		}
	});
	//关联控件点击删除绑定
	$("body").on("click",".colDel",function(){
		$(".subform [name='busType']").val("");
		$(".subform [name='busId']").val("");
		var actObj = $(this);
		$(actObj).parents(".relativeRow[busType='"+$(actObj).attr("busType")+"']").remove();
	});
	//输入文本框双击删除绑定
	$("body").on("dblclick",".colInput",function(){
		var actObj = $(this);
		var busType = $(actObj).attr("busType");
		var busId = $(actObj).attr("busId");
		var taskId = "${task.id}";
		$.post("/task/delTaskBusRelation?sid=${param.sid}",{Action:"post",id:taskId,busId:busId,busType:busType},     
			function (data){
			if(data.status=='y'){
				showNotification(1,data.info);
				$(actObj).parents(".relativeRow[busType='"+$(actObj).attr("busType")+"']").remove();
			}else{
				showNotification(2,data.info);
			}
		},"json");
	});
	//父级任务关联删除
	$("body").on("dblclick",".pTask",function(){
		var actObj = $(this);
		var pTaskId = $(actObj).attr("taskId");
		var taskId = "${task.id}";
		if(pTaskId){
			$.post("/task/delpTaskRelation?sid=${param.sid}",{Action:"post",id:taskId,parentId:pTaskId},     
				function (msgObjs){
				if(msgObjs["succ"]){
					/* showNotification(1,msgObjs.promptMsg);
					$(actObj).attr("pTaskId","");
					$(actObj).val(""); */
					window.self.location.reload();//因此项是其它关联事项关联的前提，所以需要刷新页面
				}else{
					showNotification(2,msgObjs.promptMsg);
				}
			},"json");
		}
	});
	//父级任务关联
	$("body").on("click",".pTaskRalation",function(){
		var actObj = $(this);
		var oldId = "${task.id}";
		listTaskForRelevance(sid,function(pTask){
			if(pTask.id==oldId){
				showNotification(2,"不能与自己关联");
			}else if(oldId!=pTask.id){
				if(!strIsNull(pTask.id) && pTask.id>0){
					$.post("/task/taskParentIdUpdate?sid=${param.sid}",{Action:"post",id:${task.id},parentId:pTask.id},     
						function (data){
							if(data.status=='y'){
								window.self.location.reload();//因此项是其它关联事项关联的前提，所以需要刷新页面
							}else{
								showNotification(2,data.info);
							}
					},"json");
				}
			}
		})
	});
	//任务进度汇报
	$("body").on("mouseup",".progress-drag-btn",function(){
		tag = false;
		//任务进度汇报
		taskOptForm.updateTaskProgress();
	});
	//任务标记完成
	$("body").on("click",".finishTaskBtn",function(){
		finishTask(${task.id},${task.state},openTabIndex,openPageTag);//办结任务
	});
	
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
    //任务复制
    $("body").on("click",".copyBtn",function(){
        copyTask(${task.id},"${param.sid}",${task.busType});//任务复制
    });
	//任务报延申请
	$("body").on("click",".delayApplyBtn",function(){
		window.top.layer.open({
			 type: 2,
			 title:false,
			 closeBtn: 0,
			 area: ['750px', '550px'],
			 fix: true, //不固定
			 maxmin: false,
			 content: ["/task/delayApplyPage?sid=${param.sid}&taskId=${task.id}",'no'],
			 btn: ['申请','取消'],
			 yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.formSub();
			  if(result){
			 	  window.top.layer.close(index);
			 	  window.self.location.reload();
			  }
			 },cancel: function(){}
		});
	});
});

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

//关联项目
function itemMoreSelectBack(items,busType){
    var rowObj = initModRelateStyle(busType);
    var itemId=items[0].id;
    $(rowObj).find("input[busType='"+busType+"']").val(items[0].itemName);
    $(rowObj).find("input[busType='"+busType+"']").attr("busId",itemId);
    $(rowObj).find(".colDel").remove();
    $(rowObj).find("li").find("div:last").append("<a href=\"javascript:void(0);\" class=\"pull-left margin-top-10 padding-left-5 relateClz\" " +
        "busType=\""+busType+"\" busId=\""+itemId+"\" style=\"font-size:10px;\">查看</a>");
    if(itemId){//不是同一个项目则阶段主键就不一样
        $.post("/task/taskBusIdUpdate?sid=${param.sid}",{Action:"post",id:${task.id},busId:itemId,busType:"005"},
            function (data){
                if(data.status=='y'){
                    $("#moreOpt").parent().before(rowObj);
                    showNotification(1,data.info);
                }else{
                    showNotification(2,data.info);
                }
            },"json");
    }
}
//关联项目
function spFlowMoreSelectBack(spFlows,busType){
    var rowObj = initModRelateStyle(busType);
    var spFlowId=spFlows[0].id;
    $(rowObj).find("input[busType='"+busType+"']").val(spFlows[0].flowName);
    $(rowObj).find("input[busType='"+busType+"']").attr("busId",spFlowId);
    $(rowObj).find(".colDel").remove();
    $(rowObj).find("li").find("div:last").append("<a href=\"javascript:void(0);\" class=\"pull-left margin-top-10 padding-left-5 relateClz\" " +
        "busType=\""+busType+"\" busId=\""+spFlowId+"\" style=\"font-size:10px;\">查看</a>");
    if(spFlowId){//不是同一个项目则阶段主键就不一样
        $.post("/task/taskBusIdUpdate?sid=${param.sid}",{Action:"post",id:${task.id},busId:spFlowId,busType:"022"},
            function (data){
                if(data.status=='y'){
                    $("#moreOpt").parent().before(rowObj);
                    showNotification(1,data.info);
                }else{
                    showNotification(2,data.info);
                }
            },"json");
    }
}
//关联客户
function crmMoreSelectBack(crms,busType){
    var rowObj = initModRelateStyle(busType);
    var crmId = crms[0].id;
    $(rowObj).find("input[busType='"+busType+"']").val(crms[0].crmName);
    $(rowObj).find("input[busType='"+busType+"']").attr("busId",crmId);
    $(rowObj).find(".colDel").remove();
    $(rowObj).find("li").find("div:last").append("<a href=\"javascript:void(0);\" class=\"pull-left margin-top-10 padding-left-5 relateClz\" " +
        "busType=\""+busType+"\" busId=\""+crmId+"\" style=\"font-size:10px;\">查看</a>");
    if(crmId){
        $.post("/task/taskBusIdUpdate?sid=${param.sid}",{Action:"post",id:${task.id},busId:crmId,busType:"012"},
            function (data){
                if(data.status=='y'){
                    $("#moreOpt").parent().before(rowObj);
                    showNotification(1,data.info);
                }else{
                    showNotification(2,data.info);
                }
            },"json");
    }
}
function taskForRelevanceBack(pTask) {
	if(pTask.id=='${task.id}'){
		showNotification(2,"不能与自己关联");
	}else if('${task.id}'!=pTask.id){
		$.post("/task/taskParentIdUpdate?sid=${param.sid}",{Action:"post",id:${task.id},parentId:pTask.id},
			function (data){
				if(data.status=='y'){
					window.self.location.reload();//因此项是其它关联事项关联的前提，所以需要刷新页面
				}else{
					showNotification(2,data.info);
				}
			},"json");
	}
}

/**
 * 保存简介
 */
function saveRemark(){
	var taskRemark = document.getElementById("eWebEditor1").contentWindow.getHTML();
	if(!strIsNull(taskRemark)){
		$.post("/task/taskTaskRemarkUpdate?sid=${param.sid}",{Action:"post",id:${task.id},taskRemark:taskRemark},     
				function (msgObjs){
			showNotification(1,msgObjs);
		});
	}
}
//人员单选更新
function userOneCallBack(tag){
	if("owner"==tag && !strIsNull($("#owner").val())){
		//任务发起人更新
		$.post("/task/taskOwnerUpdate?sid=${param.sid}",{Action:"post",id:${task.id},owner:$("#owner").val()},     
			function (msgObjs){
			showNotification(1,msgObjs);
			setTimeout(function(){
				var win = art.dialog.open.origin;//来源页面
				// 如果父页面重载或者关闭其子对话框全部会关闭
				win.location.reload();
			},500);
		});
	}else if("executor"==tag && !strIsNull($("#executor").val())){
		$.post("/task/taskExecutorUpdate?sid=${param.sid}",{Action:"post",id:${task.id},executor:$("#executor").val()},     
				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
}
//任务参与人更新
function userMoreCallBack(){
	var userIds =new Array();
	$("#listTaskSharer_sharerId option").each(function() { 
		userIds.push($(this).val()); 
    });
	if(!strIsNull(userIds.toString())){
		$.post("/task/taskSharerUpdate?sid=${param.sid}",{Action:"post",taskId:${task.id},userIds:userIds.toString()},     
			function (msgObjs){
			showNotification(1,msgObjs);
		});
	}
}
/* 清除下拉框中选择的option */
function removeClickUser(id,selectedUserId,userName) {
	layer.confirm("删除参与人'"+userName+"'?", {icon:2, title:'确认对话框'}, function(index){
	  var selectobj = document.getElementById(id);
		for ( var i = 0; i < selectobj.options.length; i++) {
			if (selectobj.options[i].value==selectedUserId) {
				selectobj.options[i] = null;
				break;
			}
		}
		$("#user_img_"+selectedUserId).remove();
		$.post("/task/delTaskSharer?sid=${param.sid}",{Action:"post",taskId:${task.id},userId:selectedUserId},     
				function (msgObjs){
				showNotification(1,msgObjs);
		});
		selected(id);
	  layer.close(index);
	});
	
}

//委托给别人执行
function nextExcuter(){
	window.top.layer.open({
		 type: 2,
		  title: false,
		  closeBtn:0,
		  area: ['600px', '450px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/task/nextExecutorPage?sid=${param.sid}&id=${task.id}&redirectPage="+encodeURIComponent('${param.redirectPage}'),'no'],
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
		  }
		  ,cancel: function(){ 
		    //右上角关闭回调
		  }
	});
}

/*
*设置模块
*/
function changeTab(ts){
	var busType = $(ts).val();
	if(busType=='0'){//没有选择模块
		$("#busNameDiv").css("display","none");
		
		$("#itemMod").css("display","none");
		$("#stagedItemLi").css("display","none");
		$("#crmMod").css("display","none");

	}else if(busType=='005'){//选择项目模块
		$("#busNameDiv").css("display","block");

		$("#busNameSpan").html("关联项目：");
		
		$("#itemMod").css("display","block");
		$("#stagedItemLi").css("display","block");
		$("#crmMod").css("display","none");
		
	}else if(busType=='012'){
		$("#busNameDiv").css("display","block");

		$("#busNameSpan").html("关联客户：");
		
		$("#itemMod").css("display","none");
		$("#stagedItemLi").css("display","none");
		$("#crmMod").css("display","block");
	}
}
//选择日期
function selectDate(val){
	var dealTimeLimit = $("#dealTimeLimit").attr("preval");
	if(dealTimeLimit!=val){
		$.post("/task/taskDealTimeLimitUpdate?sid=${param.sid}",{Action:"post",id:${task.id},dealTimeLimit:val},     
			function (msgObjs){
			showNotification(1,msgObjs);
			$("#dealTimeLimit").attr("preval",val)
		});
	}
}
//初始化任务参与人
$(document).ready(function(){
	if(!strIsNull($("#taskSharerJson").val())){
		var users = eval("("+$("#taskSharerJson").val()+")");
		var img="";
		var editTask = '${editTask}';
		if(strIsNull(editTask)){
			 for (var i=0, l=users.length; i<l; i++) {
				//数据保持
				$("#listTaskSharer_sharerId").append('<option selected="selected" value='+users[i].userID+'>'+users[i].userName+'</option>');
				img = img + "<div class=\"online-list  margin-right-5 margin-bottom-5\" style=\"float:left\" id=\"user_img_"+users[i].userID+"\" ondblclick=\"removeClickUser('listTaskSharer_sharerId','"+users[i].userID+"','"+users[i].userName+"')\" title=\"双击移除\">";
				img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
				img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
				img = img + "</div>"
			 }
		}else{
			 for (var i=0, l=users.length; i<l; i++) {
				img = img + "<div class=\"online-list  margin-right-5 margin-bottom-5\" style=\"float:left\" id=\"user_img_"+users[i].userID+"\">";
				img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
				img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
				img = img + "</div>"
			}	  
		}
		$("#taskSharor_div").html(img);
	}
	
	
	if('${task.state}'=='1'){
		ajaxListClockForOne('003','${task.id}','${param.sid}',function(data){
			var listClocks = data.listClocks;
			if(listClocks && listClocks.length>0){
				$.each(listClocks,function(index,clockObj){
					var  clockHtml= conStrClockHtml('003','${task.id}','${param.sid}',clockObj);
					$("#busClockList").append(clockHtml)
				})
			}
		});
	}
});
</script>
</head>
<body onload="${(empty editTask &&  (1==task.state || 0==task.state) && task.owner==userInfo.id)?'handleName()':'' }">
<form id="subform" class="subform" method="post">
<input type="hidden" name="id" value="${task.id}"/>
<input type="hidden" id="busType" value="${task.busType}"/>
	<div class="container" style="padding: 0px 0px;width:100%">	
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
                        <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px" id="titleTaskName">
                        	<c:choose>
                        		<c:when test="${fn:length(task.taskName)>16 }">
		                        	--${fn:substring(task.taskName,0,16)}..
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
							<a href="javascript:void(0)" onclick="closeWin();" title="关闭">
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
												<jsp:include page="./taskBase_edit.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherTaskAttrIframe" style="display:none;" class="layui-layer-load"
												src=""
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