<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
		<!-- 框架样式 -->
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		<script type="text/javascript">
		var showTabIndex = 0;
		var sid='';
		
		//打开页面body
		var openWindowDoc;
		//打开页面,可调用父页面script
		var openWindow;
		//打开页面的标签
		var openPageTag;
		//打开页面的标签
		var openTabIndex;
		//注入父页面信息
		function setWindow(winDoc,win,index){
			openWindowDoc = winDoc;
			openWindow = win;
			openTabIndex = index;
		}
		
		$(function(){
			 $(document).keydown(function(e) {
		        if (e.keyCode == 13) {
		        	return false;
		        }
		    });
			 //关闭
			 $(document).on("click","#titleCloseBtn",function(){
				 if(sid){
					 openWindow.location.replace('/index?sid='+sid);
					 layer.close(openTabIndex);
				 }else{
					 openWindow.location.replace('/web/login')
				 }
			 })
			 //完成
			 $(document).on("click","#finishBtn",function(){
				 if(sid){
					 openWindow.location.replace('/index?sid='+sid);
					 layer.close(openTabIndex);
				 }else{
					 openWindow.location.replace('/web/login')
				 }
			 })
			 
			 $(".subform").Validform({
					tiptype : function(msg, o, cssctl) {
						validMsg(msg, o, cssctl);
					},
					callback:function(form){
						if(showTabIndex==1 || showTabIndex==2){
							updateBaeInfo(showTabIndex);
						}
						return false;
					},
					showAllError : true
				});
			 
			 
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
			
			$("#depChooseBtn").on("click",function(){
				depOne('depId','depName','',sid,'yes');
			})
			$("#userLearChooseBtn").on("click",function(){
				userOne("none", "none", "", sid,function(options){
					 var userIds =new Array();
					 var leaders = "";
					 if(options.length>0){
						 $.each(options,function(i,vo){
							 userIds.push($(vo).val());
						 })
					 }
					$("#leaderTempDiv").html(leaders);
					
					if(!strIsNull(userIds.toString())){
						$.post("/userInfo/updateImmediateSuper?sid="+sid,{Action:"post",userIds:userIds.toString(),isChief:'0'},
								function (msgObjs){
							if(msgObjs.succ){
								//ajax获取用户信息
								  $.post("/userInfo/selectedUsersInfo?sid="+sid, { Action: "post", userIds:userIds.toString()},     
										  function (users){
									  var img="";
									  for (var i=0, l=users.length; i<l; i++) {
										//参与人显示构建
											img = img + "<div class=\"ticket-user pull-left other-user-box\" " +
											"style=\"cursor:pointer;\" id=\"user_img_listUserInfo_id_"+users[i].id+"\" " +
											"ondblclick=\"removeClickUser('listUserInfo_id',"+users[i].id+")\" title=\"双击移除\">";
											img = img + "<img src=\"/downLoad/userImg/"+users[i].comId+"/"+users[i].id+"?sid="+sid+"\" " +
											"class=\"user-avatar\"/>"
											img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
											img = img + "</div>"
									  }
									  $("#userLeader_div").html(img);
										showNotification(1,msgObjs.promptMsg);
								  }, "json");
							}else{
								showNotification(2,msgObjs.promptMsg);
							}
						},"json");
					}else{
						showNotification(2,"上级设定不能为空");
					}
					
					
				});
			});
			
			
			
			
			
			
			
		});
		/* 清除下拉框中选择的option */
		function removeClickUser(id,selectedUserId) {
			$.post("/userInfo/delImmediateSuper?sid="+sid,{Action:"post",userId:selectedUserId},     
				function (msgObjs){
				if(msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
					var selectobj = document.getElementById(id);
					for ( var i = 0; i < selectobj.options.length; i++) {
						if (selectobj.options[i].value==selectedUserId) {
							selectobj.options[i] = null;
							break;
						}
					}
					$("#user_img_listUserInfo_id_"+selectedUserId).remove();
				}else{
					showNotification(2,msgObjs.promptMsg);
				}
			},"json");
			selected(id);
		}
		
		$(document).on("click","#invDiv button",function(){
			var confirmCode = $("#invDiv").find("input[name='confirmCode']").val();
			if(!confirmCode){
				layer.tips('请填写邀请码！', $("#invDiv").find("input[name='confirmCode']"), {tips: 1});
				return;
			}else if(confirmCode.length!=6){
				layer.tips('请填写6位邀请码！', $("#invDiv").find("input[name='confirmCode']"), {tips: 1});
				return;
			}
			
			var comId = $("#invDiv").find("input[name='comId']").val();
			var joinTempId = $("#invDiv").find("input[name='joinTempId']").val();
			//ajax获取用户信息
			$.ajax({  
		        url : "/registe/agreeInOrg?t="+Math.random(),  
		        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
		        type : "POST",  
		        dataType : "json", 
		        data:{
					"confirmCodeParam":confirmCode,
					"comIdParam":comId,
					"joinTempId":joinTempId},
		        success : function(data) {
		        	if(data.status=='y'){
		        		$(".ps-layerTitle").html("个人配置");
		        		$("#invDiv").hide();
		        		$("#tabDiv").show();
		        		sid = data.sid;
		        		
		        		var userInfo = data.userInfo;
		        		$("#depId").val(userInfo.depId);
		        		$("#depName").html(userInfo.depName);
		        		
		        		var count = data.count;
		        		if(count==1){
		        			$("#baseInfoLi").click();
		        			$("#userDepLi").parent().hide();
		        		}else{
		        			$("#baseInfoLi").parent().remove();
		        			$("#baseInfoDiv").remove();
		        			$("#userDepLi").click();
		        		}
		        	}else{
		        		showNotification(2,data.info);
		        	}
		        },error:function(){
		        	showNotification(2,"操作失败,请联系管理人员");
		        }
		    });
		})
		
		function changeTab(index,ts){
			showTabIndex = index;
			$(ts).parent().parent().find("li").removeAttr("class");
			$(ts).parent().attr("class","active")
			$(".tab-pane").removeClass("active");
			if(index==1){//基本信息
				$("#baseInfoDiv").addClass("active")
			}else if(index==2){//上下级以及部门
				$("#userDepDiv").addClass("active")
			}
		}
		function updateBaeInfo(index){
			if(1==index){
				 $("#baseInfoForm").ajaxSubmit({
				        type:"post",
				        url:"/userInfo/AjaxUpdateUserPassAndName?sid="+sid+"&t="+Math.random(),
				        dataType: "json", 
				        success:function(data){
					        if('y'==data.status){
				        		showNotification(1,"设置成功");
			        			$("#userDepLi").parent().show();
				        		$("#userDepLi").click();
				        		$("#baseInfoLi").parent().hide();
					        }else{
				        		showNotification(2,data.info);
					        }
				        },error:function(XmlHttpRequest,textStatus,errorThrown){
				        	showNotification(2,"系统错误，请联系管理人员！")
				        }
				 });
			}
		}
		
		
		
		//设置部门
		function depOneCallBack(orgIdElementId, orgPathNameElementId,orgId,orgName){
			var preDepId = parseInt($("#"+orgIdElementId).val())
			if(parseInt(orgId)!=preDepId){
				$.post("/userInfo/updateUserDep?sid="+sid,{Action:"post",depId:orgId,depName:orgName},     
					function (data){
					if(data.status=='y'){
						$("#"+orgIdElementId).val(orgId)
						$("#"+orgPathNameElementId).html(orgName)
						showNotification(1,"设置成功");
					}else{
						showNotification(2,data.info);
					}
				},"json");
			}else{
				showNotification(2,"选择的是相同的部门");
			}
		}
		
		function stop(){ 
			return false; 
		} 
		document.oncontextmenu=stop;
		</script>
		<style type="text/css">
		/*注册*/
		.form-group{ padding:5px 0;}
		.form-group:after{ clear: left; display: table; content: "";}
		.form-group label{ float: left;width:100px; text-align: right; line-height: 35px;}
		.input-form{ border:1px solid #ccc; height: 35px;width:300px; line-height: 35px; padding-left:10px;}
		.next-btn{ display: block; height: 35px; background:#25A3EC; text-align: center; 
		line-height: 35px;color:#fff;width:150px; margin-left:150px; margin-top:10px;}
		.next-btn:hover{ background:#2298DB;}
		</style>
	</head>
	<body >
		<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
			<span class="widget-caption themeprimary ps-layerTitle">账户激活</span>
		    <div class="widget-buttons">
				<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
					<i class="fa fa-times themeprimary"></i>
				</a>
			</div>
		</div>
		
		<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
		
			<div class="container" style="padding: 0px 0px;width: 100%;display:none" id="tabDiv">	
	        	<div class="widget no-margin" >
	        		<div class="widget-body no-shadow no-padding">
	                	<div class="widget-main ">
	                    	<div class="tabbable">
	                        	<ul class="nav nav-tabs tabs-flat" style="padding-top: 0px" >
	                        		<li class="active">
	                                	<a data-toggle="tab" id="baseInfoLi" href="javascript:void(0)" onclick="changeTab(1,this)">核心配置</a>
	                            	</li>
	                        		<li>
	                                	<a data-toggle="tab" id="userDepLi" href="javascript:void(0)" onclick="changeTab(2,this)">其他配置</a>
	                            	</li>
	                            </ul>
	                            <div class="tab-content tabs-flat no-padding-top no-padding-bottom">
	                            	<div class="tab-pane active" id="baseInfoDiv" style="min-height: 320px">
										<div class="widget-body no-shadow">
											<form class="subform" id="baseInfoForm">
											   		<div class="form-group" style="height: 35px">
										    			<label>团队名称：</label>
										    			<div class="input-form pull-left" style="background-color:#ccc">${organic.orgName}</div>
										    		</div>
											   		<div class="form-group" style="height: 35px">
										    			<label>登录账号：</label>
										    			<div class="input-form pull-left" style="background-color:#ccc">${joinTemp.account}</div>
										    		</div>
											   		<div class="form-group" style="height: 35px">
										    			<label><font style="color: red">*</font>真实姓名：</label>
										    			<div class="pull-left">
										    				<input class="input-form" type="text" name="userName" 
										    				placeholder="${joinTemp.userName}"/>
										    			</div>
										    		</div>
										    		<div class="form-group" style="height: 35px">
										    			<label><font style="color: red">*</font>设置密码：</label>
										    			<div class="pull-left">
											    			<input class="input-form pull-left" datatype="s6-18" id="password" type="password" name="password"/>
											    			<div class="ps-clear"></div>
										    			</div>
										    		</div>
									    			<div class="form-group ps-clear" style="height: 35px">
										    			<label><font style="color: red">*</font>确认密码：</label>
										    			<div class="pull-left">
											    			<input class="input-form pull-left" datatype="*" type="password" recheck="password" name="password2" value="" />
											    			<div class="ps-clear"></div>
										    			</div>
										    		</div>
											   		<div class="clearfix form-group" style="height: 35px">
										    			<button class="next-btn" type="botton">下一步</button>
										    		</div>
										   	</form>
										</div>
									</div>
	                            	<div class="tab-pane" id="userDepDiv" style="height: 300px">
			                            <div id="needLeaerDiv" class="margin-top-5">
			                            	<div class="widget-header bg-bluegray no-shadow">
												<span class="widget-caption blue">直属上级</span>
												<div class="widget-buttons btn-div-full">
												</div>
												<span class="widget-caption pull-left red padding-left-20">
										           <small class="ws-active ws-color">建议：只设置直属上级</small>
										        </span>
											</div>
											<div class="widget-body no-shadow" id="leaderSetDiv">
												<div style="width: 250px;display:none;">
													 <select list="listUserInfo" listkey="id" listvalue="userName" id="listUserInfo_id" name="listUserInfo.id" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
													 </select>
												</div>
												
													<div id="userLeader_div" class="pull-left" style="max-width:300px;height: 40px">
													</div>
													<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-left-5 margin-top-10" 
													title="人员单选" id="userLearChooseBtn" style="float: left;">
														<i class="fa fa-plus"></i>选择
													</a> 
												<div class="ps-clear"></div>
											</div>
										</div>
										
										<div class="widget-header bg-bluegray no-shadow" style="clear:both">
											<span class="widget-caption blue">所属部门</span>
											<div class="widget-buttons btn-div-full">
											</div>
										</div>
										<div class="widget-body no-shadow">
											<input id="depId" name="depId" type="hidden">
											<div class="online-list" style="width: 300px;cursor: auto;">
												<span id="depName">${userInfo.depName }</span>
												<a title="部门选择" href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-5" id="depChooseBtn"
												  ><i class="fa fa-plus"></i>选择</a>
											</div>
										</div>
										<div class="clearfix form-group padding-top-50" style="height: 35px">
							    			<button class="next-btn" type="botton" id="finishBtn">完成</button>
							    		</div>
									</div>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	        	</div>
	        </div>
	        
	       <div class="container" style="padding: 0px 0px;width: 100%" id="invDiv">
	       		<div class="widget-body no-shadow" style="padding-left:70px;padding-top:40px">
		    			<input type="hidden" name="joinTempId" value="${joinTemp.id}"/>
		    			<input type="hidden" name="comId" value="${joinTemp.comId}"/>
				   		<div class="form-group" style="height: 35px">
			    			<label>团队名称：</label>
			    			<div class="input-form pull-left" style="background-color:#ccc">${organic.orgName}</div>
			    		</div>
				   		<div class="form-group" style="height: 35px">
			    			<label>登录账号：</label>
			    			<div class="input-form pull-left" style="background-color:#ccc">${joinTemp.account}</div>
			    		</div>
			    		<div class="form-group" style="height: 35px">
			    			<label><font style="color: red">*</font>激活码：</label>
			    			<div class="pull-left">
			    				<input class="input-form pull-left" type="text" name="confirmCode">
			    			</div>
			    		</div>
				   		<div class="clearfix form-group" style="height: 35px">
			    			<button class="next-btn" type="button">下一步</button>
			    		</div>
				</div>
	       </div>
		</div>
		
	</body>
</html>

