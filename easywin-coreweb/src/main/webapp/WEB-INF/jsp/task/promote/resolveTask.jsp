<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<link href="/static/assets/css/task.css" rel="stylesheet" type="text/css">
<script src="/static/js/taskJs/taskCenter.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/taskJs/taskOpt.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
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
		$(".autoTextArea").autoTextarea({minHeight:80});
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
		//初始化母任务选择数据
		$("#itemName").keyup(function(){
			if(!strIsNull($("#itemName").val())){
				$.post("/task/itemJson?sid=${param.sid}",{Action:"post",id:0,itemName:$("#itemName").val()},     
				 function (msgObjs){
					var autoComplete=new AutoComplete("itemName","itemId","itemNameDiv",msgObjs);
					autoComplete.start(event);
				});
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
                    var imgObj = $('<img src="/downLoad/userImg/${userInfo.comId}/'+userObj.id+'" class="margin-left-5 usedUserImg"/>')
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
	})
	//form提交
	function formSub(){
		//$(".subform").submit();
		if($("#subState").val()==1){
			return false;
		}
    	$("#taskRemark").text(document.getElementById("eWebEditor1").contentWindow.getHTML());//任务说明赋值
		$("#fileDivAjax").html('');
	      var index = 0;
		$("select[multiple=multiple]").each(function(){
			      var pp = $(this);
			      $(this).children().each(function(i){
			        var input = $('<input>');  
	                 input.attr("name",pp.attr("list")+"["+index+"]."+pp.attr("listkey"));  
	                 input.attr("type","hidden");  
	                 input.attr("value",$(this).val());  
	                 $("#fileDivAjax").append(input); 
	                 
	                 var inputname = $('<input>');  
	                 inputname.attr("name",pp.attr("list")+"["+index+"]."+pp.attr("listvalue"));  
	                 inputname.attr("type","hidden");  
	                 inputname.attr("value",$(this).text());  
	                $("#fileDivAjax").append(inputname); 
	                 index ++;  
			      });
			    });
		if(checkCkBoxStatus("pTaskUpfileId")){
			var ckBoxs = $(":checkbox[name='pTaskUpfileId']");
			if (ckBoxs != null) {
				for ( var i = 0; i < ckBoxs.length; i++) {
					var fileId = $(ckBoxs[i]).val();
					var option = $("#listUpfiles_id").find("option[value='"+fileId+"']")
					if(option && option[0]){
						continue;
					}
					if ($(ckBoxs[i]).attr('checked') ) {
						var input = $('<input>');  
			            input.attr("name","listUpfiles["+index+"].id");  
			            input.attr("type","hidden");  
			            input.attr("value",fileId);  
			            $("#fileDivAjax").append(input); 
			            
			            var inputname = $('<input>');  
			            inputname.attr("name","listUpfiles["+index+"].filename");   
			            inputname.attr("type","hidden");  
			            inputname.attr("value",$(ckBoxs[i]).attr("data-fileName"));  
			           $("#fileDivAjax").append(inputname); 
			            index ++;  
					}
				}
			}
			
		}
		
		var result;
		$(".subform").ajaxSubmit({
		        type:"post",
		        url:"/task/resolveTask?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        	var taskName = $("#taskName").val();
		        	if(strIsNull(taskName)){
		        		layer.tips('请填写任务名称', "#taskName", {tips: 1});
		        		return false;
		        	}
		        	var count = taskName.replace(/[^\x00-\xff]/g,"**").length;
					if(count>52){
						layer.tips('任务名称太长', "#taskName", {tips: 1});
		        		return false;
					}
					var executors = $("#listTaskExecutor_executor").find("option");
		    		if(!executors || !executors[0]){
		    			layer.tips('请选择任务办理人员', "#listTaskExecutor_executorDiv", {tips: 1});
		    			return false;
		    		}
                    //检查完成时限
                    var expectTime = $("#expectTime").val();
                    if(!expectTime ){
                        layer.tips("请选择完成时限",$("#expectTime"),{"tips":1});
                        return false;
                    }
                    //检查任务描述
                    var mark = document.getElementById("eWebEditor1").contentWindow.getHTML();
                    if(mark.trim() == ""){
                        layer.tips("请填写任务描述",$("#eWebEditor1"),{"tips":1});
                        return false;
                    }

		        	$("#subState").val(1)
				}, 
		        success:function(data){
			        if('y'==data.status){
			        	result = eval('(' + data.sonTaskJson + ')');
			        }else{
		        		showNotification(2,data.info);
			        }
		        },error:function(XmlHttpRequest,textStatus,errorThrown){
		        	showNotification(2,"系统错误，请联系管理人员");
		        }
		 });
		$("#subState").val(0)
		 return result;
	}
	$(document).ready(function(){
			$("#taskName").focus();
	});
	//关联项目返回函数
	function itemSelectedReturn(id,name){
		var oldId = $("#itemId").val();
		if(oldId!=id){//不是同一个项目则阶段主键就不一样
			$("#stagedItemName").val('');
			$("#stagedItemId").val('');
		}
		
		$("#itemName").val(name);
		$("#itemId").val(id);

		$("#busName").val(name);
		$("#busId").val(id);
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
			
			$("#busId").val($("#itemId").val())
			$("#busName").val($("#itemName").val())
		}else if(busType=='012'){
			$("#itemMod").css("display","none");
			$("#crmMod").css("display","block");
			
			$("#busId").val($("#crmId").val())
			$("#busName").val($("#crmName").val())
		}
	}
	//项目阶段关联
	function stagedItemSelected(){
		if(strIsNull($("#itemId").val())){
			showNotification(1,"请先选择需要关联的项目！");
		}else{
			stagedItemSelectedPage("${param.sid}",$("#itemId").val());
		}
	}
	//选择关联的项目阶段
	function stegedSelectedReturn(id,name){
		$("#stagedItemName").val(name);
		$("#stagedItemId").val(id);
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
	//双击移除项目阶段
	function removeItemStage(){
		$("#stagedItemName").val('');
		$("#stagedItemId").val('');
	}
</script>
</head>
<body onload="handleName();">
	<form class="subform" id="subformOfTask" method="post" action="/task/resolveTask">
		<input type="hidden" id="subState" value="0">
		<div id="fileDivAjax"></div>
		<input type="hidden" name="version" value="${task.version}"/>
		<tags:token></tags:token>
		<input type="hidden" name="parentId" value="${task.parentId}" /> <input type="hidden" name="attentionState" id="attentionState" value="0" /> <input type="hidden" name="busId" id="busId"
			value="${ptask.busId}" /> <input type="hidden" name="busType" id="busType" value="${ptask.busType}" />
		<div class="container" style="padding: 0px 0px; width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">任务分解</span>
							<div class="widget-buttons ps-toolsBtn">
								<a href="javascript:void(0)" class="blue" onclick="setAtten(this)"> <i class="fa fa-star-o"></i>关注
								</a>
							</div>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭"> <i class="fa fa-times themeprimary"></i>
								</a>
							</div>
						</div>
						<!--Widget Header-->
						<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
						<div class="widget margin-top-40" id="contentBody" style="overflow: hidden; overflow-y: scroll;">
							<div class="widget-body">
								<div class="widget no-header ">
									<div class="widget-body bordered-radius">
										<div class="tickets-container tickets-bg tickets-pd">
											<ul class="tickets-list">
												<li class="ticket-item no-shadow clearfix ticket-normal">
													<div class="pull-left ps-left-text padding-top-10" style="text-align: right;">
														<span style="color: red">*</span>名称：
													</div>
													<div class="pull-left col-lg-7 col-sm-7 col-xs-7">
														<div class="row">
															<input id="taskName" datatype="input,sn" name="taskName" type="text" class="form-control" placeholder="任务名称" nullmsg="请填写任务名称" onpropertychange="handleName()" onkeyup="handleName()"
																value="${task.taskName}" />
														</div>
													</div>
													<div class="pull-left">
														<span id="msgTitle" class="task-height padding-left-20">(0/26)</span>
													</div> <script> 
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
			                                        <div class="pull-left task-left-text task-height" style="text-align: right;">办理类型：</div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <tags:dataDic type="taskType" name="taskType" id="taskType" value="2"></tags:dataDic>
			                                            </div>
			                                        </div>
			                                    </li>
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
			                                        <div class="pull-left task-left-text task-height" style="text-align: right;">
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
			                                        <div class="pull-left task-left-text task-height" style="text-align: right;">
			                                                                                                             紧急度：
			                                        </div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <tags:dataDic type="grade" name="grade" id="grade"></tags:dataDic>
			                                            </div>
			                                        </div>
			                                    </li>
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
			                                        <div class="pull-left task-left-text task-height" style="text-align: right;">
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
														<div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
															<span style="color: red">*</span>办理人：
														</div>
														<div class="ticket-user pull-left ps-right-box">
															<div style="width: 250px; display: none;">
																<select datatype="*" list="listTaskExecutor" listkey="executor" listvalue="executorName" id="listTaskExecutor_executor" name="listTaskExecutor.executor"
																	ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div class="pull-left" id="listTaskExecutor_executorDiv" style="max-width: 450px"></div>
															<div class="ps-clear"></div>
															<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 exectorSelectBtn" relateSelect="listTaskExecutor_executor"
																relateImgDiv="listTaskExecutor_executorDiv" title="人员选择" style="float: left;"><i class="fa fa-plus"></i>选择</a>
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
													<div class="pull-left  ps-left-text padding-top-10" style="text-align: right;"><span style="color: red">*</span>描述：</div>
													<div class="pull-left col-lg-7 col-sm-7 col-xs-7">
														<div class="row">
															<!-- <textarea class="form-control pull-left autoTextArea" name="taskRemark" rows="" cols=""
													style="width:600px;height:80px;"></textarea> -->
															<textarea class="form-control" id="taskRemark" name="taskRemark" rows="" cols="" style="width: 610px; height: 200px; display: none;"></textarea>
															<iframe ID="eWebEditor1" src="/static/plugins/ewebeditor/ewebeditor.htm?id=taskRemark&style=expand600" frameborder="0" scrolling="no" width="610" height="200"></iframe>
														</div>
													</div>
												</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="widget no-header ">
									<div class="widget-body bordered-radius">
										<div class="tickets-container tickets-bg tickets-pd">
										<c:choose>
													<c:when test="${not empty taskUpfileList }">
														<ul class="tickets-list clearfix padding-top-10">
															<li class="ticket-item no-shadow clearfix ticket-normal pull-left ">
																<div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
														    		附件关联：
														    	</div>
																<div class="pull-left task-left-text task-height">
																	<label class="padding-left-5"> 
																		<input type="checkbox" class="colored-blue" onclick="checkAll(this,'pTaskUpfileId')" /> <span class="text" style="color: inherit;">全选</span>
																	</label>
																 </div>
																 <div class="ps-clear"></div>
																 <c:forEach items="${taskUpfileList }" var="taksUpfile" varStatus="vst">
																	 <div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
															    		&nbsp;
															    	</div>
																	<div class="pull-left task-left-text task-height">
																		<label class="padding-left-5"> 
																			<input type="checkbox" class="colored-blue" name="pTaskUpfileId" value="${taksUpfile.upfileId}" data-fileName="${taksUpfile.filename}" id="file_${taksUpfile.upfileId}"/> <span class="text" style="color: inherit;">${taksUpfile.filename}</span>
																		</label>
																	 </div>
																	 <div class="ps-clear"></div>
																 </c:forEach>
															</li>
														</ul>
													</c:when>
												</c:choose>
											<ul class="tickets-list">
												<li class="ticket-item no-shadow clearfix ticket-normal">
													<div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">附件：</div>
													<div class="pull-left col-lg-4 col-sm-4 col-xs-4">
														<div class="row">
															<tags:uploadMore name="listUpfiles.id" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
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
</body>
</html>
