<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script src="/static/js/taskJs/taskOpt.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
<link href="/static/assets/css/task.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	var sid="${param.sid}";
	var pageParam= {
			"sid":"${param.sid}"
	}
	 var EasyWin = {
            "ewebMaxMin":"self"
       };
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//任务办理人员信息设置
		$("body").on("click",".exectorSelectBtn",function(){
			taskOptForm.chooseExectors($(this));
		})
		
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$("#taskName").keydown(function(event){	
			if(event.keyCode==13) {
				return false;
			}
		});
		// 验证与验证项目名称相似的项目
		$("#taskName").keyup(function(){
			if(!strIsNull($("#taskName").val())){
				$.post("/task/checkSimiTaskByTaskName?sid=${param.sid}",{Action:"post",taskName:$("#taskName").val()},     
				 function (taskNum){
					if(taskNum > 0){
						$("#addTaskWarn").html("<a href='javascript:void(0);' style='background:none;width:80px;color:#ff0000;' onclick='similarTasksPage();'>相似任务("+taskNum+")</a>");
					}else{
						$("#addTaskWarn").text("");
					}
				});
			}else{
				$("#addTaskWarn").text("");
			}
		});
		//列出常用人员信息
		listUsedUser(5,function(data){
			if(data.status=='y'){
				var usedUser = data.usedUser;
				$.each(usedUser,function(index,userObj){
					//添加头像
					var headImgDiv = $('<div class="ticket-user pull-left other-user-box"></div>');
					$(headImgDiv).data("userObj",userObj);
					var imgObj = $('<img src="/downLoad/userImg/'+userObj.comId+'/'+userObj.id+'" class="margin-left-5 usedUserImg"/>')
					
					var headImgName = $('<span class="user-name2" style="font-size:6px;display:inline-block"></span>');
					$(headImgName).html(userObj.userName);
					
					$(headImgDiv).append($(imgObj));
					$(headImgDiv).append($(headImgName));
					
					$("#usedUserDiv").append($(headImgDiv));
					
					$(headImgDiv).on("click",function(){
						var relateSelect = $(".exectorSelectBtn").attr("relateSelect");
						var relateImgDiv = $(".exectorSelectBtn").attr("relateImgDiv");
						taskOptForm.appendUsedUser(userObj,relateSelect,relateImgDiv)
					})
					
				})
			}
		});
		
		 $(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
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
        $("#addTask").click(function(){
        	var taskName = $("#taskName").val();
        	if(strIsNull(taskName)){
        		layer.tips('请填写任务名称！', "#taskName", {tips: 1});
        		return false;
        	}
        	var count = taskName.replace(/[^\x00-\xff]/g,"**").length;
			if(count>52){
				layer.tips('任务名称太长！', "#taskName", {tips: 1});
        		return false;
			}
            //提交权限标识
            var sign = 0;
            //检查办理人
            var executes = $("#listTaskExecutor_executor").find("option");
            if(!executes || !executes.get(0)){
            	 layer.tips("请选择办理人员",$(".exectorSelectBtn"),{"tips":1});
            	 return false;
            }
            //检查任务描述
            var mark = document.getElementById("eWebEditor1").contentWindow.getHTML();
            if(!mark){
            	layer.tips("请填写任务描述",$("#eWebEditor1"),{"tips":1});
            	return false;
            }
	          //添加并查看
	          $(".subform").submit();
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
			$(".relativeRow").remove();//暂时先单选关联
			//var rowObj = initModRelateStyle($(this).attr("busType"));
			//$("#moreOpt").parent().before(rowObj);
			//$(this).hide();
			var actObj = $(this);
			var busType = $(actObj).attr("busType");
			if(busType=="012"){
				crmMoreSelect(1,null,function(crms){
					crmMoreSelectBack(crms,busType);
				})
			}else if(busType=="005"){
				itemMoreSelect(1, null,function(items){
					itemMoreSelectBack(items,busType);
				})
			}else if(busType=="022"){
				spFlowMoreSelect(1, null,function(spFlow){
					spFlowMoreSelectBack(spFlow,busType);
				})
			 }else if(busType=="070"){
             	demandSelect(1, null,function(demand){
             		demandMoreSelectBack(demand,busType);
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
					itemMoreSelectBack(items,busType)
				})
			}else if(busType=="022"){
				spFlowMoreSelect(1, null,function(spFlow){
					spFlowMoreSelectBack(spFlow,busType);
				})
			}else if(busType=="070"){
            	demandSelect(1, null,function(demand){
            		demandMoreSelectBack(demand,busType);
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
			$(".subform [name='busType']").val("");
			$(".subform [name='busId']").val("");
			var actObj = $(this);
			$(actObj).parents(".relativeRow[busType='"+$(actObj).attr("busType")+"']").remove();
		});
	})
	//关联项目
	function itemMoreSelectBack(items,busType){
		$(".relativeRow[busType='"+busType+"']").remove();
		
		$(".subform [name='busType']").val(busType);
		$(".subform [name='busId']").val(items[0].id);
		var rowObj = initModRelateStyle(busType);
		$(rowObj).find("input[busType='"+busType+"']").val(items[0].itemName);
		$("#moreOpt").parent().before(rowObj);
	}
	//关联客户
	function crmMoreSelectBack(crms,busType){
		$(".relativeRow[busType='"+busType+"']").remove();
		
		$(".subform [name='busType']").val(busType);
		$(".subform [name='busId']").val(crms[0].id);
		var rowObj = initModRelateStyle(busType);
		$(rowObj).find("input[busType='"+busType+"']").val(crms[0].crmName);
		$("#moreOpt").parent().before(rowObj);
	}
	//关联审批
	function spFlowMoreSelectBack(spFlow,busType){
		$(".relativeRow[busType='"+busType+"']").remove();
		
		$(".subform [name='busType']").val(busType);
		$(".subform [name='busId']").val(spFlow[0].id);
		var rowObj = initModRelateStyle(busType);
		$(rowObj).find("input[busType='"+busType+"']").val(spFlow[0].flowName);
		$("#moreOpt").parent().before(rowObj);
	}
	 //关联需求
    function demandMoreSelectBack(demand,busType){
        $(".relativeRow[busType='"+busType+"']").remove();

        $(".subform [name='busType']").val(busType);
        $(".subform [name='busId']").val(demand[0].id);
        var rowObj = initModRelateStyle(busType);
        $(rowObj).find("input[busType='"+busType+"']").val(demand[0].serialNum);
        $("#moreOpt").parent().before(rowObj);
    }
//弹窗显示相似任务
function similarTasksPage(){
	var url = "/task/similarTasksPage?pager.pageSize=8&sid=${param.sid}&taskName="+$("#taskName").val()+"&redirectPage="+encodeURIComponent(window.location.href);
	window.top.layer.open({
		 type: 2,
		  //title: ['相似任务列表', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['580px', '500px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: [url,'no'],
		  success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
	});
}	
	/*
	*设置模块
	*/
	function changeTab(ts){
		var busType = $(ts).val();
		if(busType=='0'){//没有选择模块
			$("#itemMod").css("display","none");
			$("#crmMod").css("display","none");

			$("#busId").val(0)
			$("#busName").val('')
		}else if(busType=='005'){//选择项目模块
			$("#itemMod").css("display","block");
			$("#crmMod").css("display","none");
			
			$("#busId").val($("#itemId").val());
			$("#busName").val($("#itemName").val());
		}else if(busType=='012'){
			$("#itemMod").css("display","none");
			$("#crmMod").css("display","block");
			
			$("#busId").val($("#crmId").val());
			$("#busName").val($("#crmName").val());
		}
	}

	//关联客户返回函数
	function crmSelectedReturn(crmId,name){
		var oldId = $("#crmId").val();
		if(oldId!=crmId){//不是同一个项目则阶段主键就不一样,且不是自己
			if(!strIsNull(crmId)){
				//项目来源关联
				$("#busName").val(name);
				$("#busId").val(crmId);
				
				$("#crmId").val(crmId);
				$("#crmName").val(name);
			}
		}
	}
//关联项目返回函数
function itemSelectedReturn(id,name){
	var oldId = $("#itemId").val();
	if(oldId!=id){//不是同一个项目则阶段主键就不一样
		$("#stagedItemName").val('');
		$("#stagedItemId").val('');
		
		$.ajax({
			 type: "post",
			 url:"/item/itemLatestStagedItem?sid=${param.sid}",
			 data:{itemId:id},
			 dataType: "json",
			 success:function(data){
				 if(data.status=='f'){
					 showNotification(data.info);
				 }else{
					stagedItem = data.stagedItem;
					$("#stagedItemName").val(stagedItem.stagedName);
					$("#stagedItemId").val(stagedItem.id);
				 }
			 }
		 })
	}
	
	$("#itemName").val(name);
	$("#itemId").val(id);

	$("#busName").val(name);
	$("#busId").val(id);
}
//项目阶段关联
function stagedItemSelected(){
	if(strIsNull($("#itemId").val())){
		showNotification(2,"请先选择需要关联的项目！");
	}else{
		stagedItemSelectedPage("${param.sid}",$("#itemId").val());
	}
}
//选择关联的项目阶段
function stagedItemSelectedReturn(node){
	$("#stagedItemName").val(node.name);
	$("#stagedItemId").val(node.id);
}
//双击移除项目
function removeBus(type){
	if(type=='005'){
		$("#itemName").val('');
		$("#itemId").val('');
		
		$("#stagedItemName").val('');
		$("#stagedItemId").val('');
	}else if(type=='012'){
		$("#crmName").val('');
		$("#crmId").val('');
		
	}

	$("#busId").val(0);
	$("#busName").val('');
}
	function formSub(){
		$(".subform").submit();
	}
	$(document).ready(function(){
		$("#taskName").focus();
	});
</script>
</head>
<body>
<form class="subform" method="post" action="/task/addTask">
	<tags:token></tags:token>
	<input type="hidden" name="attentionState" id="attentionState" value="0"/>
	<input type="hidden" name="way" id="way">
	<input type="hidden" name="busId"/>
	<input type="hidden" name="busType"/>
	<input type="hidden" name="version" value="${param.version}"/>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget" style="margin-top: 0px;">
					<div
						class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px">任务发布</span>
						<div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue"
								onclick="setAtten(this)"> <i class="fa fa-star-o"></i>关注 </a> <a
								href="javascript:void(0)" class="blue" id="addTask"> <i
								class="fa fa-save"></i>发布 </a>
						</div>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i> </a>
						</div>
					</div>
					<!--Widget Header-->
					<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
					<div class="widget margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
			            <div class="widget-body">
			                <div class="widget no-header ">
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list">
			                                <li class="ticket-item no-shadow clearfix ticket-normal">
			                                    <div class="pull-left task-left-text task-height">
			                                                                                                   名称<span style="color: red">*</span>：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
				                                        <input id="taskName" datatype="input" name="taskName" type="text" defaultLen="52" class="form-control" placeholder="任务名称"
				                                             nullmsg="请填写任务名称" onpropertychange="handleName()" onkeyup="handleName()"/>
			                                        </div>
			                                    </div>
			                                    <div class="pull-left">
			                                        <span id="msgTitle" class="task-height padding-left-20">(0/26)</span>
			                                    </div>
			                                    <script> 
													//当状态改变的时候执行的函数 
													function handleName(){
														var value = $('#taskName').val();
														var len = charLength(value);
														if(len%2==1){
															len = (len+1)/2;
														}else{
															len = len/2;
														}
														if(len>26){
															$('#msgTitle').html("(<font color='red'>"+len+"</font>/26)");
														}else{
															$('#msgTitle').html("("+len+"/26)");
														}
													} 
													//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
													if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
														document.getElementById('taskName').onpropertychange=handleName 
													}else {//非ie浏览器，比如Firefox 
														document.getElementById('taskName').addEventListener("input",handleName,false); 
													} 
												</script> 
			                                </li>
			                            </ul>
			                        </div>
			                    </div>
			                </div>
			                <div class="widget no-header">
			                    <div class="widget-body bordered-radius">
			                        <div class="task-describe clearfix">
			                            <div class="tickets-container tickets-bg tickets-pd clearfix">
			                                <ul class="tickets-list clearfix">
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
			                                        <div class="pull-left task-left-text task-height">办理类型：</div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <tags:dataDic type="taskType" name="taskType" id="taskType" value="2"></tags:dataDic>
			                                            </div>
			                                        </div>
			                                    </li>
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
			                                        <div class="pull-left task-left-text task-height">
			                                                                                                                    完成时限<span style="color: red">*</span>：
			                                        </div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <input type="text" class="form-control" placeholder="完成时限" 
				                                            	datatype="ff,pff" id="expectTime" name="expectTime" />
														</div>
														
			                                        </div>
				                                     <span style="line-height: 30px">小时</span>
			                                    </li>
			                                </ul>
			                                <ul class="tickets-list clearfix padding-top-10">
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
			                                        <div class="pull-left task-left-text task-height">
			                                                                                                             紧急度：
			                                        </div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <tags:dataDic type="grade" name="grade" id="grade"></tags:dataDic>
			                                            </div>
			                                        </div>
			                                    </li>
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
			                                        <div class="pull-left task-left-text task-height">
			                                                                                                                    节点标识<span style="color: red">*</span>：
			                                        </div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row pull-left">
			                                        		<tags:dataDic type="stepTag" name="stepTag" id="stepTag" datatype="*" please="t"></tags:dataDic>
														</div>
			                                        </div>
			                                    </li>
			                                </ul>
			                                <ul class="tickets-list clearfix padding-top-10">
			                                    <li class="no-shadow clearfix ticket-normal pull-left col-lg-12 col-sm-12 col-xs-12">
			                                        <div class="pull-left task-left-text task-height">
			                                                                                                                                                 办理人<span style="color: red">*</span>：
			                                        </div>
													<div class="ticket-user pull-left ps-right-box">
														<div style="width: 250px;display:none;">
															<select datatype="*" list="listTaskExecutor" listkey="executor" listvalue="executorName" 
																id="listTaskExecutor_executor" name="listTaskExecutor.executor" ondblclick="removeClick(this.id)" multiple="multiple" 
																moreselect="true" style="width: 100%; height: 100px;">
															</select>
														</div>
														<div class="pull-left" id="listTaskExecutor_executorDiv" style="max-width: 450px"></div>
														<div class="ps-clear"></div>
														<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 exectorSelectBtn" 
															relateSelect="listTaskExecutor_executor" relateImgDiv="listTaskExecutor_executorDiv"
															title="人员选择"  style="float: left;"><i class="fa fa-plus"></i>选择</a>
														<div id="usedUserDiv" style="width: 450px;display: inline-block;">
															<span class="pull-left" style="padding-top:8px;display: inline-block;padding-left: 10px">常用人员:</span>
														</div>
													</div>
			                                    </li>
			                                </ul>
			                            </div>
			                        </div>
			                    </div>
			                </div>
			                <div class="widget no-header ">
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list">
			                                <li class="ticket-item no-shadow clearfix ticket-normal">
			                                    <div class="pull-left task-left-text">
			                                                                                                   描述<span style="color: red">*</span>：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
				                                        <textarea id="taskRemark" name="taskRemark" rows="" cols="" class="form-control"
														style="width:650px;height: 110px;display:none;"></textarea> 
														<iframe ID="eWebEditor1" src="/static/plugins/ewebeditor/ewebeditor.htm?id=taskRemark&style=expand600" 
														frameborder="0" scrolling="no" width="650" height="280"></iframe>
													</div>
			                                    </div>
			                                </li>
			                            </ul>
			                        </div>
			                    </div>
			                </div>
			                <div class="widget no-header">
			                    <div id="moreOpt" class="task-shrink-body">
			                        <div class="clearfix">
			                            <ul class="pull-left task-rel">
			                                <li busType="012"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联客户</span></li>
			                                <li busType="005"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联项目</span></li>
			                                <li busType="022"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联审批</span></li>
			                                <li busType="070"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联需求</span></li>
			                            </ul>
			                        </div>
			                    </div>
			                    <div class="task-shrink-head">
			                        <div class="inner text-center">
			                            <span id="task-shrink-name">更多及事件关联</span><i class="fa padding-left-10 fa-angle-down task-shrink-icon" id="moreOptImg"></i>
			                        </div>
			                    </div>
			                </div>
			                <div class="widget no-header ">
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list">
			                                <li class="ticket-item no-shadow clearfix ticket-normal">
			                                    <div class="pull-left task-left-text task-height">
			                                                                                                    附件：
			                                    </div>
			                                    <div class="pull-left col-lg-4 col-sm-4 col-xs-4">
			                                        <div class="row">
			                                            <tags:uploadMore name="listUpfiles.id" showName="fileName"
															ifream="" comId="${userInfo.comId}"></tags:uploadMore>
			                                        </div>
			                                    </div>
			                                </li>
			                            </ul>
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
