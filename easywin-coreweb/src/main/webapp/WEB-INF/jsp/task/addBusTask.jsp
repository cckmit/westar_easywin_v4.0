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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">

<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link href="/static/assets/css/task.css" rel="stylesheet" type="text/css">
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
	$(function(){
		
		//任务办理人员信息设置
		$("body").on("click",".exectorSelectBtn",function(){
			taskOptForm.chooseExectors($(this));
		})
		
		document.onkeydown = function(event) {  
            var target, code, tag;  
            if (!event) {  
                event = window.event; //针对ie浏览器  
                target = event.srcElement;  
                code = event.keyCode;  
                if (code == 13) {  
                    tag = target.tagName;  
                    if (tag == "TEXTAREA") { return true; }  
                    else { return false; }  
                }  
            }  
            else {  
                target = event.target; //针对遵循w3c标准的浏览器，如Firefox  
                code = event.keyCode;  
                if (code == 13) {  
                    tag = target.tagName;  
                    if (tag == "TEXTAREA") { return true; }  
                    else { return false; }  
                }  
            }  
        };
        
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
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
				},
				"busType":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(!str){
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
			}
		});
		
		//输入文本框双击删除绑定
		$("body").on("dblclick",".colInput",function(){
			$(".subform [name='busId']").val("");
			$(this).val("");
			//var actObj = $(this);
			//$(actObj).parents(".relativeRow[busType='"+$(actObj).attr("busType")+"']").remove();
		});
	});
	//关联项目
	function itemMoreSelectBack(items,busType){
		$(".subform [name='busId']").val(items[0].id);
		$("#busInput").val(items[0].itemName);
	}
	//关联客户
	function crmMoreSelectBack(crms,busType){
		$(".subform [name='busId']").val(crms[0].id);
		$("#busInput").val(crms[0].crmName);
	}
	//表单提交
	function formSub(){
		if($("#subState").val()==1){
			return false;
		}
    	$("#taskRemark").text(document.getElementById("eWebEditor1").contentWindow.getHTML());//任务说明赋值
		 $("#fileDivAjax").html('');
		 $("select[multiple=multiple]").each(function(){
			      var index = 0;
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
		var flag = false;
		$(".subform").ajaxSubmit({
		        type:"post",
		        url:"/task/addBusTask?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        	var taskName = $("#taskName").val();
		        	if(strIsNull(taskName)){
		        		scrollToLocation($("#contentBody"),$("#taskName"));
	                    layer.tips("请填写任务名称",$("#taskName"),{"tips":1});
		        		return false;
		        	}
		        	var count = taskName.replace(/[^\x00-\xff]/g,"**").length;
					if(count>52){
						scrollToLocation($("#contentBody"),$("#taskName"));
	                    layer.tips("任务名称太长！",$("#taskName"),{"tips":1});
		        		return false;
					}
					var executors = $("#listTaskExecutor_executor").find("option");
		    		if(!executors || !executors.get(0)){
		    			scrollToLocation($("#contentBody"),$("#listTaskExecutor_executorDiv"));
		    			layer.tips('请选择任务办理人员', "#listTaskExecutor_executorDiv", {tips: 1});
		    			return false;
		    		}
						var busId = $("[name='busId']").val();
					if(strIsNull(busId) || busId<=0){
						scrollToLocation($("#contentBody"),$("#busInput"));
						layer.tips('请选择关联业务！', "#busInput", {tips: 1});
		        		return false;
					}
                    //检查完成时限
                    var endTime = $("#dealTimeLimit").val();
                    var reg = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/;
                    if(endTime == null || !reg.test(endTime)){
                    	scrollToLocation($("#contentBody"),$("#dealTimeLimit"));
                        layer.tips("请选择完成时限",$("#dealTimeLimit"),{"tips":1});
                        return false;
                    }
                    //检查任务描述
                    var mark = document.getElementById("eWebEditor1").contentWindow.getHTML();
                    if(!mark || mark.trim() == ""){
                    	scrollToLocation($("#contentBody"),$("#eWebEditor1"));
                        layer.tips("请填写任务描述",$("#eWebEditor1"),{"tips":1});
                        return false;
                    }
		        	$("#subState").val(1);
		        	
				}, 
		        success:function(data){
			        if('y'==data.status){
			        	flag = true;
			        }else{
		        		showNotification(2,data.info);
			        }
		        },error:function(XmlHttpRequest,textStatus,errorThrown){
		        	showNotification(2,"系统错误，请联系管理人员");
		        }
		 });
		$("#subState").val(0)
		return flag;
	}
</script>
</head>
<body >
<input type="hidden" id="subState" value="0">
<form class="subform" method="post" action="/task/addBusTask">
	<tags:token></tags:token>
	<div id="fileDivAjax">
	</div>
	<input type="hidden" id="busId" name="busId" value="${task.busId}"/>
	<input type="hidden" id="busType" name="busType" value="${task.busType}"/>
	<input type="hidden" id="busName" name="busName" value="${task.busName}"/>
	<input type="hidden" name="attentionState" id="attentionState" value="0"/>
    <input type="hidden" id="owner" name="owner" value="${userInfo.id}"/>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget" style="margin-top: 0px;">
					<div
						class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px">任务发布</span>
						<div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue"
								onclick="setAtten(this)"> <i class="fa fa-star-o"></i>关注
							</a>
						</div>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i> 
							</a>
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
			                                    <div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
			                                        <span style="color: red">*</span>名称：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
				                                        <input id="taskName" datatype="input,sn" name="taskName" type="text" class="form-control" placeholder="任务名称"
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
			                <div class="widget no-header ">
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list">
			                                <li class="ticket-item no-shadow clearfix ticket-normal">
			                                	<c:set var="busName">
													<c:choose>
														<c:when test="${task.busType eq '005'}">项目</c:when>
														<c:when test="${task.busType eq '012'}">客户</c:when>
													</c:choose>
												</c:set>
			                                    <div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
			                                      	  <span style="color: red">*</span>关联${busName}：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                        <div class="row">
			                                            <input type="text" id="busInput" class="form-control pull-left colInput" readonly="readonly" value="${task.busName}" 
			                                            	placeholder="关联${busName}" title="双击移除" style="cursor:pointer;width:75%;" busType="${task.busType}" />
			                                            <a href="javascript:void(0);" class="pull-left margin-top-10 padding-left-5 colAdd" 
			                                            style="font-size:10px;" busType="${task.busType}">选择</a>
			                                        </div>
			                                    </div>
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
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
			                                        <div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">办理类型：</div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <tags:dataDic type="taskType" name="taskType" id="taskType" value="2"></tags:dataDic>
			                                            </div>
			                                        </div>
			                                    </li>
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
			                                        <div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
			                                             <span style="color: red">*</span>完成时限：
			                                        </div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <input type="text" class="form-control" placeholder="完成时限" readonly="readonly" id="dealTimeLimit"
																	name="dealTimeLimit" onClick="WdatePicker({minDate:'%y-%M-{%d}'})"/>
														</div>
			                                        </div>
			                                    </li>
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
			                                        <div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
			                                                                                                             紧急度：
			                                        </div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <tags:dataDic type="grade" name="grade" id="grade"></tags:dataDic>
			                                            </div>
			                                        </div>
			                                    </li>
			                                </ul>
			                                <ul class="tickets-list clearfix padding-top-10">
			                                    <li class="no-shadow clearfix ticket-normal pull-left col-lg-12 col-sm-12 col-xs-12">
			                                        <div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
			                                            <span style="color: red">*</span> 办理人：
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
			                                    <div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
			                                         <span style="color: red">*</span>描述：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
														<textarea class="form-control" id="taskRemark" name="taskRemark" rows="" cols="" 
														style="width:610px;height: 200px;display:none;"></textarea> 
														<iframe ID="eWebEditor1" src="/static/plugins/ewebeditor/ewebeditor.htm?id=taskRemark&style=expand600" 
														frameborder="0" scrolling="no" width="610" height="200"></iframe>
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
			                            <ul class="tickets-list">
			                                <li class="ticket-item no-shadow clearfix ticket-normal">
			                                    <div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
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
</body>
</html>
