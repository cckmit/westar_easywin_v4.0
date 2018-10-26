<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="renderer" content="webkit">
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<!-- 框架样式 -->
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		
		<script type="text/javascript">
		var EasyWin = {
				"sid" : "${param.sid}",
				"userInfo" : {
					"userId" : "${userInfo.id}",
					"userName" : "${userInfo.userName}",
					"comId" : "${userInfo.comId}",
					"orgName" : "${userInfo.orgName}",
					"isAdmin" : "${userInfo.admin}",
				},
				"homeFlag" : "${homeFlag}",
				"ifreamName" : "${param.ifreamName}"
			};
		
		var sid='${param.sid}';
		
		//关闭窗口
		function closeWin(){
			var winIndex = window.top.layer.getFrameIndex(window.name);
			closeWindow(winIndex);
		}
		
		$(function(){
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
			//点击选择步骤人员设定
			$("body").on("click","#chooseStepUsers",function(){
				userOne("no_userId", "null", null, "${param.sid}",function(options){
					var preStepUsers = $("#flowNextStep_userId_div").parent().find("input[name='stepUser']");
					if(preStepUsers && preStepUsers.get(0)){
						$.each(preStepUsers,function(index,preStepUser){
							$(preStepUser).attr("checked",false);
						})
					}
					
					if(options && options.length>0){
					 var userIds =new Array();
					 $("#flowNextStep_userId_div").html('');
					 $.each(options,function(index,option){
						 var selectUserId = option.value;
						 var preObj = $("#flowNextStep_sysuserId_div").find("#user_img_flowNextStep_userId_"+selectUserId);
						 if(preObj && preObj.get(0)){
							 $("#user_img_flowNextStep_userId_"+selectUserId).find("input[name='stepUser']").attr("checked",true);
							 return true;
						 }
						 userIds.push(selectUserId);
						 var selectUserName = option.text;
						 
						 var img="";
						 var userImgUrl = "/downLoad/userImg/${userInfo.comId}/"+selectUserId
						  
						  img = img + "<div class=\"online-list margin-left-5 margin-bottom-5 stepUser\" " +
						  "style=\"float:left;cursor:pointer;\" id=\"user_img_flowNextStep_userId_"+selectUserId+"\" " +
						  " userId='"+selectUserId+"'>";
						  img = img + "<div style='position:relative;' class='pull-left'><input type='radio' name='stepUser' style='ws-radio' value='"+selectUserId+"' userName='"+selectUserName+"'/>"
						  img = img + "</div>"
						  img = img + "<img  src=\""+userImgUrl+"\" class=\"user-avatar margin-left-20\"/>"
						  img = img + "<span class=\"user-name\">"+selectUserName+"</span>"
						  img = img + "</div>"
						  img = img + "<div class='ps-clear'>"
						  img = img + "</div>"
						  
						  $("#flowNextStep_userId_div").append(img);
						  //选择的人员默认为下一步骤办理人
						  $("#user_img_flowNextStep_userId_"+selectUserId).find("input[name='stepUser']").attr("checked",true);
						  
					 })
					}else{
						layer.tips("请至少选择一个人员！",$("#chooseStepUsers"),{tips:1})
					}
					
				});
				
			});
			
			//告知人选择
			$("body").on("click","#chooseCsUsers",function(){
				userMore("csrySelect", null, "${param.sid}","yes",null,function(result){
					//人员多选开始
					if(result && result.length>0){
						//清空选择项
						$("#csrySelect").html('');
						$("#csry_div").html('');
						var img="";
						var selectedUsers =new Array();
						//遍历结果开始
						$.each(result,function(index,obj){
							 selectedUsers.push(obj.value);
							 
							 var userImgUrl = "/downLoad/userImg/${userInfo.comId}/"+obj.value;
							 img = img + "<div class=\"online-list margin-left-5 margin-bottom-5\" " +
							  "style=\"float:left;cursor:pointer;\" userId='"+obj.value+"' title='双击移除'>";
							  img = img + "		<img  src=\""+userImgUrl+"\" class=\"user-avatar\"/>"
							  img = img + "		<span class=\"user-name\">"+obj.text+"</span>"
							  img = img + "</div>";
							  
							  var optionObj = $("<option value='"+obj.value+"'>"+obj.text+"</option>");
							  $("#csrySelect").append(optionObj);
							 
						});
						 $("#csry_div").html(img);
						//遍历结果结束
					}else{
						//清空选择项
						 $("#csrySelect").html('');
						 $("#csry_div").html('');
					}
					
					//人员多选结束
				});
			});
			
			//点击选择步骤信息
			$("body").on("click","input[name='stepIds']",function(){
				var selectFlag = $(this).attr("checked");
				if(selectFlag){
					var nextStepInfoSelected = $(this).parent().data("stepInfo");
					//首选步骤类型
					var stepTypeSelected = nextStepInfoSelected.stepType;
					if(stepTypeSelected && stepTypeSelected=='end'){
						$("#xxtzDiv").hide();
						$("#hxbdlDiv").hide();
						$("#rycsDiv").hide();
						$("#blfsDiv").hide();
						$("#addTaskWayDiv").show();
					}else{
						$("#xxtzDiv").show();
						$("#hxbdlDiv").show();
						$("#rycsDiv").show();
						$("#blfsDiv").show();
						$("#addTaskWayDiv").hide();
						$("#flowNextStep_sysuserId_div").html('');
						//首选步骤默认办理人
						var listSpFlowHiExecutorSelected = nextStepInfoSelected.listSpFlowHiExecutor;
						if(listSpFlowHiExecutorSelected && listSpFlowHiExecutorSelected.length>0){
							//展示默认的审批人
							initSpFlowHiExecutor(listSpFlowHiExecutorSelected);
						}
					}
				}
			});
			
			//点击头像选择对应审批人
			$("body").on("click",".nextStepList .step_name",function(){
				var stepIdRadio = $(this).parent().find("input[name='stepIds']");
				if(!$(stepIdRadio).attr("checked")){
					$(stepIdRadio).attr("checked",true);
					$(stepIdRadio).trigger("click");
				}
			});
			//点击头像选择对应审批人
			$("body").on("click",".flow_next_step img,.flow_next_step .user-name",function(){
				var stepUserRadio = $(this).parent().find("input[name='stepUser']");
				if(!$(stepUserRadio).attr("checked")){
					$(stepUserRadio).attr("checked","true");
				}
			});
			//点击头像选择对应审批人
			$("body").on("dblclick",".stepUser",function(){
				$(this).remove();
			});
			//移除告知人
			$("body").on("dblclick","#csry_div img,#csry_div .user-name",function(){
				var userId = $(this).parent().attr("userId");
				$("#csrySelect").find("option[value='"+userId+"']").remove();
				$(this).parent().remove();
			})
		})
		var pDocument;
		var pWindow;
		//初始化信息
		function initSpFlowNextStep(stepInfo,pDocumentParam,pWwindowParam){
			pDocument = pDocumentParam;
			pWindow = pWwindowParam;
			
			initUpFileHtml();
			if(stepInfo){
				var curStepInfo = stepInfo.curStepInfo;
				$(".curStepName").html(curStepInfo.stepName);
				
				var nextStepInfoList = stepInfo.nextStepInfoList;
				$(".nextStepList").html('');
				//首选步骤类型
				var stepType;
				//首选步骤默认办理人
				var listSpFlowHiExecutor;
				if(nextStepInfoList && nextStepInfoList.length>0){
					if(nextStepInfoList.length==1){
						defStepInfo = nextStepInfoList[0];
						var nextStepInfo = defStepInfo;
						var stepLable = $('<div class="ps-clear margin-left-5" style="position:relative;height:25px"></div>');
						var stepRadio = $('<input style="top:-2px !important;" type="radio" name="stepIds" value="'+nextStepInfo.stepId+'"/><span class="padding-left-20 margin-top-5 step_name" style="cursor:pointer;">'+nextStepInfo.stepName+'</span>')
						
						$(stepLable).append(stepRadio);
						$(".nextStepList").append(stepLable);
						$(stepLable).data("stepInfo",nextStepInfo);
						
						$("input[name='stepIds']").eq(0).attr("checked","true");
					}else{
						var defStepInfo;
						//遍历候选步骤
						$.each(nextStepInfoList,function(index,nextStepInfo){
							var defaultStepState = nextStepInfo.defaultStepState;
							if(defaultStepState == 1){
								defStepInfo = nextStepInfo;
							}else{
								var conditionExp = nextStepInfo.conditionExp;
								if(conditionExp){
									conditionExp = conditionExp.replace(/and/g, '&&');
									conditionExp = conditionExp.replace(/or/g, '||');
									var listStepConditions = nextStepInfo.listStepConditions;
									if(listStepConditions && listStepConditions[0]){
										$.each(listStepConditions,function(index,condition){
											var conditionType = condition.conditionType;
											if(conditionType ==='='){
												conditionType ='==';
											}
											var conditionVar;
											if('10000' === condition.conditionVar){　//人员
												conditionVar = pWindow.FLOWINFO.creatorName;
											}else if('11000' === condition.conditionVar){//部门
												conditionVar = pWindow.FLOWINFO.creatorDepName;
											}else{
												conditionVar = pWindow.getConditionVarVal(condition.conditionVar);
											}
											
											var actExp;
											if(isNaN(condition.conditionValue)){
												conditionVar = "'"+conditionVar+"'"
												actExp = conditionVar + conditionType + "'"+condition.conditionValue+ "'";
											}else{
												actExp =  conditionVar + conditionType +condition.conditionValue;
											}
											conditionExp = conditionExp.replace(condition.conditionNum,actExp)
										});
										var conditionState = eval('(' + conditionExp + ')');
										if(conditionState===true){
											defStepInfo = nextStepInfo;
											return false;
										}
									}
								
								}
							}
						});
						if(defStepInfo){
							var nextStepInfo = defStepInfo;
							var stepLable = $('<div class="ps-clear margin-left-5" style="position:relative;height:25px"></div>');
							var stepRadio = $('<input style="top:-2px !important;" type="radio" name="stepIds" value="'+nextStepInfo.stepId+'"/><span class="padding-left-20 margin-top-5 step_name" style="cursor:pointer;">'+nextStepInfo.stepName+'</span>')
							
							$(stepLable).append(stepRadio);
							$(".nextStepList").append(stepLable);
							$(stepLable).data("stepInfo",nextStepInfo);
						}else{
							//遍历候选步骤
							$.each(nextStepInfoList,function(index,nextStepInfo){
								if(index === 0){
									defStepInfo = nextStepInfo;
								}
								var stepLable = $('<div class="ps-clear margin-left-5" style="position:relative;height:25px"></div>');
								var stepRadio = $('<input style="top:-2px !important;" type="radio" name="stepIds" value="'+nextStepInfo.stepId+'"/><span class="padding-left-20 margin-top-5 step_name" style="cursor:pointer;">'+nextStepInfo.stepName+'</span>')
								
								$(stepLable).append(stepRadio);
								$(".nextStepList").append(stepLable);
								$(stepLable).data("stepInfo",nextStepInfo);
							});
						}
						$("input[name='stepIds'][value='"+defStepInfo.stepId+"']").attr("checked","true");
					}
					
					//首选步骤类型
					stepType = defStepInfo.stepType;
					//首选步骤默认办理人
					listSpFlowHiExecutor = defStepInfo.listSpFlowHiExecutor;
				}
				
				if(stepType && stepType=='end'){
					$("#xxtzDiv").hide();
					$("#hxbdlDiv").hide();
					$("#rycsDiv").hide();
					$("#blfsDiv").hide();
					$("#addTaskWayDiv").show();
				}
				$("#flowNextStep_sysuserId_div").html('');
				if(listSpFlowHiExecutor && listSpFlowHiExecutor.length>0){
					//展示默认的审批人
					initSpFlowHiExecutor(listSpFlowHiExecutor);
				}
			}else{
				showNotification(2,"没有配置步骤信息");
				$("#xxtzDiv").hide();
				$("#hxbdlDiv").hide();
				$("#rycsDiv").hide();
				$("#blfsDiv").hide();
				$("#spIdeaDiv").hide();
				$("#fileDiv").hide();
				$("#addTaskWayDiv").hide();
			}
		}
		//展示默认的审批人
		function initSpFlowHiExecutor(listSpFlowHiExecutor){
			$.each(listSpFlowHiExecutor,function(index,exector){
				  var img="";
				  var imgSrc = "/downLoad/userImg/${userInfo.comId}/"+exector.id;
				  img = img + "<div class=\"online-list margin-left-5 margin-bottom-5\" " +
				  "style=\"float:left;\" id=\"user_img_flowNextStep_userId_"+exector.id+"\" " +
				  "userId='"+exector.id+"'>";
				  img = img + "<div style='position:relative;' class='pull-left'><input type='radio' name='stepUser' style='ws-radio' value='"+exector.id+"' userName='"+exector.userName+"'/>"
				  img = img + "</div>"
				  img = img + "<img  src=\""+imgSrc+"\" " +
				  "class=\"user-avatar margin-left-20\" />"
				  img = img + "<span class=\"user-name\">"+exector.userName+"</span>"
				  img = img + "</div>"
				  img = img + "<div class='ps-clear'></div>"
				  img = img + "</div>"
				  $("#flowNextStep_sysuserId_div").append(img);
				  //默认步骤设定
				  $("#flowNextStep_sysuserId_div").find("input[name='stepUser']").eq(0).attr("checked","true");
			})
		}
		//返回步骤信息
		function returnStepConfig(){
			//选中的步骤
			var selectStepId = $("input[name='stepIds']:checked");
			//步骤类型
			var resultStepType;
			//选中的步骤主键
			var resultStepId;
			//候选人员
			var selectUsers = $("input[name='stepUser']:checked");
			if($(selectStepId).length == 0){//没有选择步骤
				layer.tips("请选择流程步骤",$(".nextStepList"),{tips:1})
				return null;
			}else{//选择了步骤信息
				var nextStepInfoSelected = $(selectStepId).parent().data("stepInfo");
				//首选步骤类型
				resultStepType = nextStepInfoSelected.stepType ? nextStepInfoSelected.stepType:'';
				resultStepId = nextStepInfoSelected.stepId;
			}
			//候选人员
			var selectUsers = $("input[name='stepUser']:checked");
			if($(selectUsers).length==0){//没有审批人员
				if(resultStepType!='end'){//非办结步骤需要办理人员
					layer.tips("请选择下一步骤候选人",$("#chooseStepUsers"),{tips:1})
					return null;
				}else{//办结步骤返回结果
					var result={"stepType":resultStepType,"stepId":resultStepId};
				
					var spIdea = $("#spIdea").val();
					if(pWindow.FLOWINFO.saveType == 'update'){//审批意见必填
						if(!spIdea){
							layer.tips("请填写审批意见！",$("#spIdea"),{tips:1})
							return null;
						}
					}
					
					if(spIdea){
						result.spIdea = spIdea;
					}
					var spFilesObj = $("#spFileRelate").find("select");
					var spFilesArray = new Array();
					$.each($(spFilesObj).find("option"),function(i,vo){
						var file = {"upfileId":$(vo).val(),"filename":$(vo).text()}
						spFilesArray.push(file);
					});
					result.spFlowUpfiles = spFilesArray;
					//事项安排落实
					result.addTaskWay=$("input[name='addTaskWay']:checked").val();
					return result;
				}
			}else{//有审批人员
				
				//告知人是否有办理人员
				var csrySelectState = 1;
				
				//候选人员集合
				var users = new Array();
				$.each(selectUsers,function(index,selectUser){
					var userId = $(selectUser).val();
					var user = {"id":userId,"userName":$(selectUser).attr("userName")}
					users.push(user);
					
					//排除办理人员
					var csry = $("#csrySelect").find("option[value='"+userId+"']");
					if(csry.length==1){//告知人是办理人员
						layer.tips("告知人中需排除办理人员！",$("#chooseCsUsers"),{tips:1});
						csrySelectState = 0;
						return false;
					}
				})
				
				if(!csrySelectState){
					return null;
				}
				
				//排除当前操作人员
				var curUser = $("#csrySelect").find("option[value='${userInfo.id}']");
				if(curUser.length==1){
					layer.tips("不能抄送给自己",$("#chooseCsUsers"),{tips:1})
					return null;
				}
				//手机短信通知
				var msgSendFlag = $(":checkbox[name='msgSendFlag']").attr("checked");
				
				//抄送
				var noticeUserArray = new Array();
				var noticeUserSelected = $("#csrySelect").find("option");
				//设定告知人开始
				if(noticeUserSelected && $(noticeUserSelected).length>0){
					//遍历告知人开始
					$.each(noticeUserSelected,function(index,optionObj){
						var userObj = {"noticeUserId":optionObj.value,"noticeUserName":optionObj.text};
						noticeUserArray.push(userObj);
					});
					//遍历告知人结束
				}
				//返回结果
				var result={"nextStepUsers":users,"msgSendFlag":msgSendFlag?"1":"0","noticeUsers":noticeUserArray};
				result.stepType = resultStepType;
				result.stepId = resultStepId;
				
				var spIdea = $("#spIdea").val();
				if(pWindow.FLOWINFO.saveType == 'update'){//审批意见必填
					if(!spIdea){
						layer.tips("请填写审批意见！",$("#spIdea"),{tips:1})
						return null;
					}
				}
				
				if(spIdea){
					result.spIdea = spIdea;
				}
				var spFilesObj = $("#spFileRelate").find("select");
				var spFilesArray = new Array();
				$.each($(spFilesObj).find("option"),function(i,vo){
					var file = {"upfileId":$(vo).val(),"filename":$(vo).text()}
					spFilesArray.push(file);
				});
				result.spFlowUpfiles = spFilesArray;
				//事项安排落实
				result.addTaskWay="0";
				return result;
			}
		}
		//初始化界面上传附件的问题
		function initUpFileHtml(){
			if(pWindow.FLOWINFO.saveType == 'add'){
				$("#spIdeaDiv").remove();
				$("#fileDiv").show();
			}else{
				$("#spIdeaDiv").show();
				$("#spIdea").html('[同意]');
				$("#fileDiv").show();
				
			}
		}
		

		</script>
		<style type="text/css">
			.online-list{cursor: pointer;}
			input[type="radio"],input[type="checkbox"] {
			    top: 3px !important;
			    left:0px !important;
			    opacity: 1 !important;
			    width: 15px !important;
			    height: 15px !important;
			}
		</style>
	</head>
	<body>
	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	     <i class="widget-icon fa fa-cog txt-red menu-icons"></i>
	     <span class="widget-caption txt-red menu-topic ps-layerTitle">下一步步骤配置</span>
	     <div class="widget-buttons">
	         <a id="titleCloseBtn"  href="javascript:void(0)" onclick="closeWin()" title="关闭">
	             <i class="fa fa-times"></i>
	         </a>
	     </div>
	 </div>
	 <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
		<div class="widget-body no-shadow">
						<ul class="tickets-list">
							<li class="clearfix ticket-item no-shadow" style="border-bottom: 1px solid #ccc;">
							    <div class="pull-left gray ps-left-text" style="text-align: right;">
							    	当前步骤：
							    </div>
								<div class="ticket-user pull-left margin-left-5 margin-bottom-10">
								   <strong><span class="curStepName"></span></strong>
								</div>               
                             </li>
							<li class="clearfix ticket-item no-shadow autoHeight no-padding padding-top-5 padding-bottom-5">
							    <div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
							    	下一步步骤：
							    </div>
								<div class="ticket-user pull-left ps-right-box margin-top-5 nextStepList" style="height: auto;max-width: 380px;min-height: 30px" >
								</div> 
								<div class="ps-clear"></div>              
                             </li>
                             <li class="clearfix ticket-item no-shadow autoHeight no-padding padding-top-5 padding-bottom-5" id="hxbdlDiv">
						    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
						    		&nbsp;审批人：
						    	</div>
								<div class="ticket-user pull-left ps-right-box" style="height: auto;max-width: 380px" >
									<div class="pull-left gray ps-left-text padding-top-10">
										<div class="pull-left flow_next_step" style="width: 250px">
											<div id="flowNextStep_userId_div" class="clearfix" style="max-width:250px">
											 </div>
											<div id="flowNextStep_sysuserId_div" class="clearfix" style="max-width:250px">
											 </div>
										</div>
										<div class="pull-left padding-left-5" style="width: 50px">
											<a href="javascript:void(0);" class="btn btn-primary btn-xs no-margin-top margin-bottom-5" title="人员选择" id="chooseStepUsers">
												<i class="fa fa-plus"></i>选择</a>
										</div>
									</div>
								</div>
								<div class="ps-clear"></div>
                             </li>
                             <li class="clearfix ticket-item no-shadow autoHeight no-padding" id="rycsDiv">
						    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;" >
						    		&nbsp;抄送：
						    	</div>
								<div class="ticket-user pull-left ps-right-box" style="height: auto;max-width: 380px" >
									<div class="pull-left gray ps-left-text padding-top-10">
										<div class="pull-left" style="width: 250px">
											<div id="csry_div" class="clearfix" style="max-width:250px">
											</div>
											<select id="csrySelect" multiple="multiple" style="display: none">
											</select>
										</div>
										<div class="pull-left padding-left-5" style="width: 50px">
											<a href="javascript:void(0);" class="btn btn-primary btn-xs no-margin-top margin-bottom-5" title="人员多选" id="chooseCsUsers">
												<i class="fa fa-plus"></i>选择</a>
										</div>
									</div>
								</div>
								<div class="ps-clear"></div>
                              </li>
                             <li class="clearfix ticket-item no-shadow autoHeight no-padding padding-top-5 
							padding-bottom-5" id="xxtzDiv">
							    <div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
							    	通知方式：
							    </div>
								<div class="ticket-user pull-left ps-right-box margin-top-5" style="height: auto;max-width: 380px;min-height: 30px" >
									<label class="padding-left-5">
									 	<input type="checkbox" class="colored-blue" name="msgSendFlag" value="1" checked="checked"></input>
									 	<span class="text" style="color:inherit;">手机短信通知</span>
								    </label>
								</div> 
								<div class="ps-clear"></div>              
                             </li>
                             <li class="clearfix ticket-item no-shadow autoHeight no-padding padding-top-5 
							padding-bottom-5" id="spIdeaDiv">
							    <div class="pull-left gray ps-left-text" style="text-align: right;">
							    	<font color="red">*</font>审批意见：
							    </div>
								<div class="ticket-user pull-left ps-right-box margin-top-5 margin-left-5" style="height: auto;max-width: 380px;min-height: 30px" >
									<textarea class="colorpicker-default form-control  margin-bottom-10" id="spIdea"
										style="min-height:50px;width: 380px;" rows="" cols=""></textarea>
									<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('spIdea','${param.sid}');" title="常用意见"></a>
								</div> 
								<div class="ps-clear"></div>              
                             </li>
                             <li class="clearfix ticket-item no-shadow autoHeight no-padding padding-top-5 
							padding-bottom-5" id="fileDiv">
							    <div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
							    	审批附件：
							    </div>
								<div class="ticket-user pull-left ps-right-box margin-top-5" style="height: auto;max-width: 380px;min-height: 30px" >
									<div class="pull-left col-lg-4 col-sm-4 col-xs-4">
										<div class="row">
											<tags:uploadMore name="spFlowUpfiles.id" showName="fileName"
															 selectDivId="spFileRelate" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
										</div>
									</div>
								</div>
								<div class="ps-clear"></div>              
                             </li>
                             <!-- <li class="clearfix ticket-item no-shadow autoHeight no-padding padding-top-5 
							padding-bottom-5" id="addTaskWayDiv" style="display:none;">
							    <div class="pull-left gray ps-left-text padding-top-10">
							    	审批转任务：
							    </div>
								<div class="ticket-user pull-left ps-right-box margin-top-5" style="height: auto;max-width: 380px;min-height: 30px" >
									<label class="padding-left-5" title="需审批转任务">
									 	<input type="checkbox" name="addTaskWay" value="003">
									 	<span class="text" style="color:inherit;">审批转任务</span>
								    </label>
								</div> 
								<div class="ps-clear"></div>              
                             </li> -->
						</ul>
				</div>
    
		</div>	
		
	</body>
</html>

