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
		
		<!-- 考勤 -->
		<c:if test="${userInfo.admin>'0' }">
			<script type="text/javascript" src="/static/js/attenceJs/attence.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<script type="text/javascript">
		
		var page = '${param.page}';
		var showTabIndex = 0;
		var admin = '${userInfo.admin}';
		$(function(){
			if(page == 'regist'){//注册界面
				if(admin=='1'){//是超级管理员，不进行个人设置
					$("#userDepLi").parent().remove();
					$("#userDepDiv").remove();
				}
				
				var teamAttenceLi =  $("#teamAttenceLi").parent().clone();//显示团队设置
				 $("#teamAttenceLi").parent().remove();
				 $("#headImgLi").parent().before($(teamAttenceLi));
				 
				 $("#headImgLi").parent().remove();
			}else{
				 $("#headImgLi").parent().remove();
			}
			$(".subform").Validform({
				tiptype : function(msg, o, cssctl) {
					validMsg(msg, o, cssctl);
				},
				callback:function(form){
					if(showTabIndex==1 || showTabIndex==3){
						updateBaeInfo(showTabIndex);
					}
					return false;
				},
				showAllError : true
			});
			
			var index = '${tabIndex}';
			if(!index){
				if(page == 'index'){//非注册页面进来的需要设定个人配置
					index=4;//个人配置
				}else if(page == 'regist'){//注册界面
					if(admin=='1'){//是超级管理员
						index=5;//团队配置
					}else{
						index=4;//个人配置
					}
				}else{
					index=2;//其他的为个人配置
				}
			}
			showTab(index);
			
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
		});
		$(document).ready(function() {
		    $(document).keydown(function(e) {
		        if (e.keyCode == 13) {
		        	return false;
		        }
		    });
		    var userLeader = {};
		    
		    $("#isChiefDiv").data("userLeader",userLeader);
		    //初始化分享设置
		    if(!strIsNull($("#leaderJson").val())){
				var users = eval("("+$("#leaderJson").val()+")");
				  var img="";
					for (var i=0, l=users.length; i<l; i++) {
						if(i==0){
							$("#isChiefDiv").data("userLeader").leaderId=users[i].userID;
						}
						//数据保持
						$("#listUserInfo_id").append('<option selected="selected" value='+users[i].userID+'>'+users[i].userName+'</option>');
						//参与人显示构建
						img = img + "<div class=\"ticket-user pull-left other-user-box\" style=\"cursor:pointer;\" id=\"user_img_listUserInfo_id_"+users[i].userID+"\" ondblclick=\"removeClickUser('listUserInfo_id','"+users[i].userID+"')\" title=\"双击移除\">";
						img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
						img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
						img = img + "</div>"
					}
					$("#userLeader_div").html(img);
			}
		    
		    $("#userLearChooseBtn").on("click",function(){
		    	userOne("none", "none", "", '${param.sid}',function(options){
					 var userIds =new Array();
					 var leaders = "";
					 if(options.length>0){
						 $.each(options,function(i,vo){
							 userIds.push($(vo).val());
						 })
					 }
					$("#leaderTempDiv").html(leaders);
					
					if(!strIsNull(userIds.toString())){
						
						var isChief = $("#leaderSetDiv").find(":radio[name=\"isChief\"]:checked").val()
						
						$.post("/userInfo/updateImmediateSuper?sid=${param.sid}",{Action:"post",userIds:userIds.toString(),isChief:isChief},     
							function (msgObjs){
							if(msgObjs.succ){
								//ajax获取用户信息
								  $.post("/userInfo/selectedUsersInfo?sid=${param.sid}", { Action: "post", userIds:userIds.toString()},     
										  function (users){
									  var img="";
									  for (var i=0, l=users.length; i<l; i++) {
										  if(i==0){
											  
											  $("#isChiefDiv").data("userLeader").isChief='0'
											  $("#isChiefDiv").data("userLeader").leaderId=users[i].id;
										  }
										//参与人显示构建
											img = img + "<div class=\"ticket-user pull-left other-user-box\" " +
											"style=\"cursor:pointer;\" id=\"user_img_listUserInfo_id_"+users[i].id+"\" " +
											"ondblclick=\"removeClickUser('listUserInfo_id',"+users[i].id+")\" title=\"双击移除\">";
											img = img + "<img src=\"/downLoad/userImg/"+users[i].comId+"/"+users[i].id+"?sid=${param.sid}\" " +
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
		    var admin = '${userInfo.admin}';
		    if(admin!='0'){
				$.ajax({
					type:"post",
					url:"/organic/listOrgCfg?sid=${param.sid}&t="+Math.random(),
				    dataType: "json",
				    success:function(data){
				    	if(data.status=='y'){
				    		var list = data.list;
				    		console.log(list)
				    		$.each(list,function(index,vo){
				    			$("#attenceRuleDiv").find("input[name=\""+vo.cfgType+"\"]").attr("checked",vo.cfgValue=='1')
				    		})
				    	}else{
				    	}
				    },error:function(){
				    }
				})
		    }
		    
		    if(page == 'index'){
			    var isChief = '${userInfo.isChief}';
				if(isChief=='1'){//不需要设定上级
					$("#needLeaerDiv").hide();
					$("#leaderSetDiv").find(":radio[name=\"isChief\"][value=\"1\"]").attr("checked",true);
					
					$("#isChiefDiv").data("userLeader").isChief="1";
				}else{
					$("#needLeaerDiv").show();
					$("#leaderSetDiv").find(":radio[name=\"isChief\"][value=\"0\"]").attr("checked",true);
					
					
					$("#isChiefDiv").data("userLeader").isChief="0";
				}
		    }
			$("#leaderSetDiv").find(":radio[name=\"isChief\"]").on("click",function(){
				var isChief = $(this).val();
				if(isChief=='1'){//不需要设定上级
					$("#needLeaerDiv").hide();
				
					var selectHtml = $("#listUserInfo_id").html();
					
					$("#listUserInfo_id").html("");
					
					var img = $("#userLeader_div").html();
					$("#userLeader_div").html("")
					
					var chiefType = $("#isChiefDiv").data("userLeader").isChief;
					if(!chiefType || chiefType!='1'){
						$.post("/userInfo/updateLeader?sid=${param.sid}",{Action:"post",isChief:isChief},     
							function (data){
							if(data.status=='y'){
								showNotification(1,data.info);
								$("#isChiefDiv").data("userLeader").isChief='1';
								$("#isChiefDiv").data("userLeader").leaderId=null;
							}else{
								$("#listUserInfo_id").html(selectHtml);
								$("#userLeader_div").html(img);
								$("#leaderSetDiv").find(":radio[name=\"isChief\"][value=\"0\"]").attr("checked",true);
								$("#needLeaerDiv").show();
								showNotification(2,data.info);
							}
						},"json");
					}
					
				}else if(isChief=='0'){//需要设定上级
					$("#needLeaerDiv").show();
				
					$.post("/userInfo/updateLeader?sid=${param.sid}",{Action:"post",isChief:isChief},     
							function (data){
							 if(data.status=='y'){
								showNotification(1,data.info);
								$("#isChiefDiv").data("userLeader").isChief='0';
							}else{
								$("#leaderSetDiv").find(":radio[name=\"isChief\"][value=\"1\"]").attr("checked",true);
								$("#isChiefDiv").data("userLeader").leaderId=null;
								$("#needLeaerDiv").hide();
								showNotification(2,data.info);
							} 
						},"json");
				}
			});
		    
		    
		    
		 });
		//直属上级更新
		function userMoreCallBack(){
			var userIds =new Array();
			$("#listUserInfo_id option").each(function() { 
				userIds.push($(this).val()); 
		    });
			if(!strIsNull(userIds.toString())){
				$.post("/userInfo/updateImmediateSuper?sid=${param.sid}",{Action:"post",userIds:userIds.toString()},     
					function (msgObjs){
					if(msgObjs.succ){
						showNotification(1,msgObjs.promptMsg);
					}else{
						showNotification(2,msgObjs.promptMsg);
					}
				},"json");
			}
		}

		/* 清除下拉框中选择的option */
		function removeClickUser(id,selectedUserId) {
			$.post("/userInfo/delImmediateSuper?sid=${param.sid}",{Action:"post",userId:selectedUserId},     
				function (msgObjs){
				if(msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
					$("#isChiefDiv").data("userLeader").leaderId=null;
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
		//验证上级
		function checkLeader(){
			var a = 1;
			/*
			var isChief = $("#isChiefDiv").data("userLeader").isChief;
			var userAdmin = '${userInfo.admin}';
			if(isChief && isChief=='0' && userAdmin =='0'){
				var leaderId = $("#isChiefDiv").data("userLeader").leaderId;
				if(!leaderId){
					a = !1;
					showTab(4);
					showNotification(2,"请设定上级！");
				}
			}
			*/
			return a;
		}
		
		//修改用户基本信息
		function updateBaeInfo(index){
			if(1==index){
				 $("#baseInfoForm").ajaxSubmit({
				        type:"post",
				        url:"/userInfo/AjaxEditUserInfo?sid=${param.sid}&t="+Math.random(),
				        dataType: "json", 
				        success:function(data){
					        if('y'==data.status){
					        	//showTab(2);
				        		showNotification(1,"设置成功");
					        }else{
				        		showNotification(2,data.info);
					        }
				        },error:function(XmlHttpRequest,textStatus,errorThrown){
				        	showNotification(2,"系统错误，请联系管理人员！")
				        }
				 });
			}else if(3==index){
				 $("#userTelForm").ajaxSubmit({
				        type:"post",
				        url:"/userInfo/AjaxEditUserInfo?sid=${param.sid}&t="+Math.random(),
				        dataType: "json", 
				        success:function(data){
					        if('y'==data.status){
					        	//showTab(4);
				        		showNotification(1,"设置成功");
					        }else{
				        		showNotification(2,data.info);
					        }
				        },error:function(XmlHttpRequest,textStatus,errorThrown){
				        	showNotification(2,"系统错误，请联系管理人员！")
				        }
				 });
				
			}
		}
		
		/**
		 * 上传头像
		 * @param sid
		 * @param filePicker
		 * @param fileShowList
		 * @param fileVal
		 * @param tempDir
		 * @param meetingRoomId
		 * @return
		 */
		function loadUpfilesForHead(sid,filePicker,fileShowList,fileVal,tempDir,meetingRoomId){
			//上传文件的URL
			var backEndUrl = '/upload/addDepartImgFile?sid='+sid+'&t='+Math.random();
		    //各个不同的文件的MD5,用于上传
		    var destMD5Dir = {md5:"",fileVal:fileVal,tempDir:tempDir,meetingRoomId:meetingRoomId};   //附件MD5信息
			WebUploader.Uploader.register({//注册插件
		        "before-send-file": "beforeSendFile"
		    },{
			    beforeSendFile: function(file){
		    	loadImgIndex = window.top.layer.load(0);
			        //秒传验证
			        var task = new $.Deferred();
			        (new WebUploader.Uploader()).md5File(file).progress(function(percentage){
			        }).then(function(val){
			           //用于上传
			            destMD5Dir.md5 = val;
			            task.resolve();
			        });
			        return $.when(task);
			    }
			});
			var uploader = WebUploader.create({
			    swf: '/static/plugins/webuploader/Uploader.swf?t='+Math.random(),
			    server: backEndUrl,
			    pick: {id:'#'+filePicker,multiple: false},
			    duplicate: false,//不能选择重复的文件
			    resize: false,
			    auto: false,
			    fileNumLimit: null,
			    fileVal:fileVal,   //指明参数名称，后台也用这个参数接收文件
			    fileSingleSizeLimit: 200*1024*1024,
			    //fileType:'rar,zip,doc,xls,docx,xlsx,pdf'
			    accept: {
			    	title: 'Images',
		    	    extensions: 'gif,jpg,jpeg,bmp,png',
		    	    mimeTypes: 'image/*'
			    },
			    chunked:true,
			    chunkSize:5*1024*1024,
			    thread:2,
			    formData:function(){return $.extend(true, {}, destMD5Dir);}
			});
			uploader.on("error",function(type,reason){
		    	if (type=="Q_TYPE_DENIED"){
		    		window.top.layer.alert(reason.ext+"格式不支持")
		    	}else if(type=="Q_EXCEED_NUM_LIMIT"){
		    		window.top.layer.alert('上传的文件数量已经超出系统限制的'+reason+"个");
		    	}else if(type=="F_EXCEED_SIZE"){
		    		window.top.layer.alert('文件太大不支持');
		    	}
			});
		    uploader.on('fileQueued', function( file ) {  // webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
			  uploader.upload(file);
		   });
			 uploader.on('uploadSuccess', function (file, data) {
			      var chunksTotal = 0;
		            if((chunksTotal = Math.ceil(file.size/(5*1024*1024))) >= 1){
		                //合并请求
		                $.ajax({
		                    type: "POST"
		                    , url: backEndUrl
		                    , data: {
		                        status: "chunksMerge"
		                        , name: file.name
		                        , chunks: chunksTotal
		                        , ext: file.ext
		                        , md5: data.fileMd5
		                        , size:file.size
		                        ,tempDir:tempDir
		                        ,meetingRoomId:meetingRoomId
		                    }
		                    , cache: false
		                    , dataType: "json"
		                }).then(function(data, textStatus, jqXHR){
			   				 if(data.status=='y'){
			   					var $list = $("#"+fileShowList);
			   				   if($list.find("img").length==0){
			   					   var $li = $('<img id="' + file.id + ' style="width:210px;height:210px">');
			   					   $list.html( $li );
			   				   }
								// 原图
								$("#orgFilePath").val(data.orgImgPath);
								$("#orgFileName").val(data.orgImgName);
								$("#largeImgPath").val(data.largeImgPath);
								var org =data.largeImgPath+"?t="+Math.random();
								var $list = $("#"+fileShowList);
								$list.find("img").attr("src",org)
								$list.find("img").attr("style","width:210px;height:210px")
					         }
		                	
		                	uploader.removeFile(file, true);
		                	window.top.layer.close(loadImgIndex);
		                	
		                }, function(jqXHR, textStatus, errorThrown){
		                	uploader.removeFile(file, true);
		                	window.top.layer.close(loadImgIndex);
		                	
		                });
		            }
			      
		    });
		    uploader.on('uploadError', function (file,reason) {
		    	 window.top.layer.close(loadImgIndex);
		    	 uploader.removeFile(file, true);
			});
		    
		    //修复model内部点击不会触发选择文件的BUG
		    $("#"+filePicker+" .webuploader-pick").click(function () {
		        $("#"+filePicker+" :file").click();
		    });
		}
		//修改用户头像
		function updateMyImg(){
			 $("#imgForm").ajaxSubmit({
			        type:"post",
			        url:"/userInfo/editPic?sid=${param.sid}&t="+Math.random(),
			        dataType: "json", 
			        beforeSubmit:function(a,f,o){
				        if(!$("#largeImgPath").val()){
				        	showNotification(2,"请选择头像！")
				        	return false;
					    }
			    	},
			        success:function(data){
				        if('y'==data.status){
				        	showNotification(1,"修改成功！");
				        	$("#orgFilePath").val('');
							$("#orgFileName").val('');
							$("#largeImgPath").val('');
							//showTab(3);
						}
			        },
			        error:function(XmlHttpRequest,textStatus,errorThrown){
			        	showNotification(2,"系统错误，请联系管理人员！")
			        }
			    });
			}
			function changeTab(index,ts){
				
				showTabIndex = index;
				$(ts).parent().parent().find("li").removeAttr("class");
				$(ts).parent().attr("class","active")
				$(".tab-pane").removeClass("active");
				if(index==1){//基本信息
					$("#baseInfoDiv").addClass("active")
				}else if(index==2){//头像
					$("#headImgDiv").addClass("active")
				}else if(index==3){//联系信息
					$("#userTelDiv").addClass("active")
				}else if(index==4){//上下级以及部门
					$("#userDepDiv").addClass("active")
				}else if(index==5){//考勤设置
					$("#attenceRuleDiv").addClass("active")
				}else if(index==6){//考勤设置
					$("#teamCfgDiv").addClass("active")
				}
			}
			
			
			
			function showTab(index){
				if(1==index){
					$("#baseInfoLi").click();
				}else if(2==index){
					$("#headImgLi").click();
				}else if(3==index){
					$("#userTelLi").click();
				}else if(4==index){
					$("#userDepLi").click();
				}else if(index==5){
					$("#teamAttenceLi").click();
				}else if(index==6){
					$("#teamCfgLi").click();
				}
			}
			//设置部门
			function depOneCallBack(orgIdElementId, orgPathNameElementId,orgId,orgName){
				var preDepId = parseInt($("#"+orgIdElementId).val())
				if(parseInt(orgId)!=preDepId){
					$.post("/userInfo/updateUserDep?sid=${param.sid}",{Action:"post",depId:orgId,depName:orgName},     
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
			var pageTag = 'org';
			var sid='${param.sid}'
			var PageData={"sid":"${param.sid}",
					"attenceRule":{"attenceRuleId":"${attenceRule.id}",
						"isSystem":"${attenceRule.isSystem}"},
					"tab":"21"};
			
			function switchSet(ts,sid){
				var cfgType = $(ts).attr("name");
				var cfgValue = $(ts).attr("checked")?"1":"0";
				$.ajax({
					type:"post",
					url:"/organic/updateOrgCfg?sid=${param.sid}&t="+Math.random(),
				    dataType: "json",
				    data:{"cfgType":cfgType,"cfgValue":cfgValue},
				    success:function(data){
				    	if(data.status=='y'){
				    		showNotification(1,data.info);
				    	}else{
				    		showNotification(2,data.info);
				    		if(cfgValue=='0'){
				    			$(ts).attr("checked",true);
				    		}else{
				    			$(ts).removeAttr("checked");
				    		}
				    	}
				    },error:function(){
				    	if(cfgValue=='0'){
			    			$(ts).attr("checked",true);
			    		}else{
			    			$(ts).removeAttr("checked");
			    		}
				    }
				})
				
			}
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
			<span class="widget-caption themeprimary ps-layerTitle">核心配置</span>
		    <div class="widget-buttons">
				<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
					<i class="fa fa-times themeprimary"></i>
				</a>
			</div>
		</div>
		
		<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
		
		<div class="container" style="padding: 0px 0px;width: 100%">	
        	<div class="widget no-margin" >
        		<div class="widget-body no-shadow no-padding">
                	<div class="widget-main ">
                    	<div class="tabbable">
                        	<ul class="nav nav-tabs tabs-flat" style="padding-top: 0px" >
                        		<li>
                                	<a data-toggle="tab" id="userDepLi" href="javascript:void(0)" onclick="changeTab(4,this)">个人配置</a>
                            	</li>
                            	<!-- 
	                        		<li>
	                                	<a data-toggle="tab" id="baseInfoLi" href="javascript:void(0)" onclick="changeTab(1,this)">基本信息</a>
	                            	</li>
                            	 -->
                        		
                            	<!-- 
	                        		<li>
	                                	<a data-toggle="tab" id="userTelLi" href="javascript:void(0)" onclick="changeTab(3,this)">联系信息</a>
	                            	</li>
                            	 -->
                            	<c:if test="${userInfo.admin>'0' }">
	                        		<li>
	                                	<a data-toggle="tab" id="teamAttenceLi" href="javascript:void(0)" onclick="changeTab(5,this)">团队配置</a>
	                            	</li>
	                            	
                            	</c:if>
                            	<!-- 修改 头像放置最后 -->
                            	<li>
                                	<a data-toggle="tab" id="headImgLi" href="javascript:void(0)" onclick="changeTab(2,this)">头像设置</a>
                            	</li>
                            </ul>
                            <div class="tab-content tabs-flat no-padding-top no-padding-bottom">
                            	<div class="tab-pane" id="userDepDiv" style="height: 300px">
		                            <div class="widget-body no-shadow" id="leaderSetDiv">
		                            	<div style="max-width:460px" id="isChiefDiv" chiefType="0">
												<span>我是老板:</span>
												<label class="margin-right-10 no-margin-bottom">
													<input type="radio" name="isChief" value="1">
													<span class="text">是</span>
												</label>
												<label class="margin-right-10 no-margin-bottom">
													<input type="radio" checked="checked" name="isChief" value="0">
													<span class="text">否</span>
												</label>
											</div>
		                            </div>
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
												title="人员多选" id="userLearChooseBtn" style="float: left;">
													<i class="fa fa-plus"></i>选择
												</a> 
											<input type="hidden" id="leaderJson" name="leaderJson" value="${leaderJson}"/>&nbsp;
											<div class="ps-clear"></div>
										</div>
									</div>
									
									<div class="widget-header bg-bluegray no-shadow" style="clear:both">
										<span class="widget-caption blue">所属部门</span>
										<div class="widget-buttons btn-div-full">
										</div>
									</div>
									<div class="widget-body no-shadow">
										<input id="depId" name="depId" value="${userInfo.depId}" type="hidden">
										<div class="online-list" style="width: 300px;cursor: auto;">
											<span id="depName">${userInfo.depName }</span>
											<a title="部门选择" href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-5"
											  onclick="depOne('depId','depName','','${param.sid}','yes');"><i class="fa fa-plus"></i>选择</a>
										</div>
									</div>
								</div>
								
								<div class="tab-pane" id="baseInfoDiv" style="min-height: 320px">
									<div class="widget-body no-shadow">
										<form class="subform" id="baseInfoForm">
											<input type="hidden" name="sid" value="${param.sid}"/>
									   		<input type="hidden" name="email" value="${userInfo.email}"/>
									   		<input type="hidden" name="id" value="${userInfo.id}"/>
									   		
										   		<div class="form-group" style="height: 35px">
									    			<label>团队名称：</label>
									    			<div class="input-form pull-left">${userInfo.orgName}</div>
									    		</div>
										   		<div class="form-group" style="height: 35px">
									    			<label>账号：</label>
									    			<div class="input-form pull-left">${userInfo.email}</div>
									    		</div>
										   		<div class="form-group" style="height: 35px">
									    			<label>姓名：</label>
									    			<div class="pull-left">
									    				<input class="input-form" type="text" name="userName" 
									    				value="${userInfo.userName}"/>
									    			</div>
									    		</div>
										   		<div class="form-group" style="height: 35px">
									    			<label>登录别名：</label>
									    			<div class="pull-left">
									    				<input class="input-form pull-left" type="text" name="nickname" datatype="*"
														ignore="ignore" value="${userInfo.nickname}" ajaxurl="/userInfo/checkNickName?email=${userInfo.email}&sid=${param.sid}">
									    			</div>
									    		</div>
										   		<div class="form-group" style="height: 35px">
									    			<label>性别：</label>
									    			<div class="pull-left">
									    				<label class="no-margin-bottom pull-left" style="width: 80px">
															<input type="radio" class="pull-left" value="0" ${userInfo.gender=='0'?'checked':''}  name="gender" id="gender">
														        女<span class="text"></span>
														</label>
														<label class="no-margin-bottom pull-left" style="width: 80px">
															<input type="radio" class="pull-left" value="1" ${userInfo.gender=='1'?'checked':''}  name="gender" id="gender">
														        男<span class="text"></span>
														</label>
									    			</div>
									    		</div>
										   		<div class="clearfix form-group" style="height: 35px">
									    			<button class="next-btn" type="submit">填写</button>
									    		</div>
									   	</form>
									</div>
								</div>
								
								
								<div class="tab-pane" id="headImgDiv">
									<div class="widget-header bg-bluegray no-shadow margin-top-5">
										<span class="widget-caption blue">头像设置</span>
										<div class="widget-buttons btn-div-full">
										</div>
									</div>
									<div class="widget-body no-shadow" style="height: 270px">
										<div class="pull-left">
										    <!--用来存放item-->
										    <div class="ps-preview" id="viewPic" style="border:1px solid #ebebeb;width:210px;height:210px;">
												<img src='/downLoad/userImg/${userInfo.comId}/${userInfo.id}?sid=${param.sid}&size=1' style="width:210px;height:210px"/>
										    </div>
										</div>
										
										<div class="pull-left" style="width: 250px;padding-left: 50px">
						            		<p class="text-danger padding-30 pull-left">图片尺寸建议200*200</p>
						            		<div class="buttons-preview padding-top-10">
						            		 	<div id="userImgPicker" class="pull-left">选择图片</div>
						            		 	<script type="text/javascript">
						            		 		loadUpfilesForHead('${param.sid}','userImgPicker','viewPic','userImgFile','headImg',0);
						            		 	</script>
						            				<form id="imgForm">
											      		<input type="hidden" name="id" value="${userInfo.id }"/>
														<input type="hidden" name="orgFilePath" id="orgFilePath" value=""/>
											      		<input type="hidden" name="orgFileName" id="orgFileName" value=""/>
											      		<input type="hidden" name="largeImgPath" id="largeImgPath" value=""/>
														<button class="btn btn-info ws-btnBlue2 pull-left margin-left-20" type="button" onclick="updateMyImg()">确定</button>
													</form>
						            		</div>
										</div>
										
						            		
									</div>
								</div>
								
								
								<div class="tab-pane" id="userTelDiv" >
									<div class="widget-body no-shadow">
										<form class="subform" id="userTelForm">
											<input type="hidden" name="sid" value="${param.sid }"/>
									   		<input type="hidden" name="email" value="${userInfo.email}"/>
									   		<input type="hidden" name="id" value="${userInfo.id }"/>
									   		
										   		<div class="form-group" style="height: 35px">
									    			<label>邮箱：</label>
									    			<div class="input-form pull-left">${userInfo.email}</div>
									    		</div>
										   		<div class="form-group" style="height: 35px">
									    			<label>qq：</label>
									    			<div class="pull-left">
									    				<input class="input-form pull-left" type="text" name="qq" value="${userInfo.qq}" ignore="ignore" >
									    			</div>
									    		</div>
										   		<div class="form-group" style="height: 35px">
									    			<label>手机：</label>
									    			<div class="pull-left">
									    				<input class="input-form pull-left" type="text" name="movePhone" 
									    				datatype="m" ignore="ignore" value="${userInfo.movePhone}" >
									    			</div>
									    		</div>
										   		<div class="form-group" style="height: 35px">
									    			<label>微信：</label>
									    			<div class="pull-left">
									    				<input class="input-form pull-left" type="text" name="wechat" value="${userInfo.wechat}">
									    			</div>
									    		</div>
										   		<div class="form-group" style="height: 35px">
									    			<label>座机号码：</label>
									    			<div class="pull-left">
									    				<div class="padding-bottom-10">
										    				<input class="input-form pull-left" type="text" name="linePhone"
										    				 datatype="zj" ignore="ignore" value="${userInfo.linePhone}">
									    				</div>
										    			<div style="clear: both">
											   				<font color="gray">(以'-'间隔)</font>
											   			</div>
									    			</div>
									    		</div>
										   		<div class="clearfix form-group" style="height: 35px">
									    			<button class="next-btn" type="submit">填写</button>
									    		</div>
									   	</form>
									</div>
								</div>
								<c:if test="${userInfo.admin>'0' }">
									<div class="tab-pane" id="attenceRuleDiv">
										<div class="widget-body no-shadow">
											<div class="widget-header bg-bluegray no-shadow margin-bottom-10">
												<span class="widget-caption blue">开关配置</span>
												<div class="widget-buttons btn-div-full">
												</div>
											</div>
												<div class="clearfix" style="border:0; ">
												    <div class="pull-left gray ps-left-text" style="font-size: 15px">
												    	&nbsp;直属验证:
												    </div>
													<div class="ticket-user pull-left margin-left-20" id="ruleTypeDiv">
														<ul class="list-group no-margin" style="border: 0;">
															<li class="list-group-item no-padding" style="border: 0;">
																<span class="pull-right"> 
																	<label class="no-margin" style="border: 0;">
																	<input class="checkbox-slider slider-icon colored-blue" name="leaderCfg"
																		type="checkbox"  checked="checked" style="height: 15px;"
																		value="1" onclick="switchSet(this,'${param.sid}');">
																		<span class="text"></span>
																	</label> 
																</span>
															</li>
														</ul>
													</div>
		                                         </div>
		                                         <div class="widget-header bg-bluegray no-shadow margin-top-5 margin-bottom-5">
													<span class="widget-caption blue">考勤设置</span>
													<div class="widget-buttons btn-div-full">
													</div>
												</div>
												<div class="clearfix padding-top-5" style="border:0; ">
												    <div class="pull-left gray ps-left-text" style="font-size: 15px">
												    	&nbsp;考勤制度:
												    </div>
													<div class="ticket-user pull-left ps-right-box margin-left-20" id="ruleTypeDiv">
														<tags:radioDataDic type="ruleType" name="ruleType" value="${attenceRule.ruleType}"></tags:radioDataDic>
													</div>
		                                         </div>
		                                         <div class="clearfix" id="rule1" style="display: none">
			                                         <div class="clearfix" style="border: 0; ">
													    <div class="pull-left gray ps-left-text " style="font-size: 15px">
													    	&nbsp;考勤规则:
													    </div>
														<div class="ticket-user pull-left ps-right-box margin-left-10">
															<div readonly="readonly" style="width: 350px;border: 0px solid #d5d5d5;">
																<span class="padding-left-10">国家法定节假日</span>
															</div>
														</div>
			                                         </div>
			                                         <div class="clearfix" style="border: 0;">
													    <div class="pull-left gray ps-left-text " style="font-size: 15px">
													    	&nbsp;考勤时段:
													    </div>
														<div class="ticket-user pull-left ps-right-box padding-left-10 margin-left-20">
															<div class="attenceTimeDiv padding-bottom-10" style="height:40px;">
																<div class="dayTimeSSpan pull-left">
																	<span class="pull-left padding-top-5 padding-right-10">签到</span>
																	<input class="dpd2 dayTimeS padding-left-20 form-control pull-left" type="text" id="dayTimeS0" rowNum="0" readonly="readonly" style="width: 100px">
																</div>
																<div class="dayTimeESpan pull-left padding-left-20">
																	<span class="pull-left padding-top-5 padding-right-10">签退</span>
																	<input class="dpd2 dayTimeE padding-left-20 form-control pull-left" type="text" id="dayTimeE0" rowNum="0" readonly="readonly" style="width: 100px">
																</div>
																<div class="optSpan padding-top-5 pull-left padding-left-20">
																	<a href="javascript:void(0)" class="fa fa-times fa-lg padding-left-10 delBtn"></a>
																	<a href="javascript:void(0)" class="fa fa-plus fa-lg padding-left-10 addBtn"></a>
																</div>
															</div>
														</div>
			                                         </div>
		                                         </div>
		                                         <div id="rule3" style="display: none">
			                                         <div class="clearfix" style="border: 0;">
													    <div class="pull-left gray ps-left-text " style="font-size: 15px">
													    	&nbsp;考勤规则:
													    </div>
														<div class="ticket-user pull-left ps-right-box margin-left-20">
															<button type="button" class="btn btn-blue pull-left">设定规则</button>
															<div class="pull-left margin-left-20 margin-top-5">
																<font color="red">(需维护节假日)</font>
															</div>
														</div>
			                                         </div>
			                                         <div id="rule3Details" class="clearfix padding-top-10" style="border: 0;display: none">
													    <div class="pull-left gray ps-left-text " style="font-size: 15px">
													    	&nbsp;具体规则:
													    </div>
														<div class="ticket-user pull-left ps-right-box margin-left-20">
															<table class="display table table-bordered table-striped">
																<thead>
																	<tr>
																		<th>工作日</th>
																		<th>考勤时段</th>
																	</tr>
																</thead>
																<tbody>
																</tbody>
															</table>
														</div>
			                                         </div>
		                                         </div>
		                                         <div class="margin-top-20">
			                                         <div class="ticket-item no-shadow padding-top-10 ps-listline" style="border: 0">
														<button class="next-btn" type="button" id="saveAtttenceRule">保存</button>
			                                         </div>
		                                         </div>
										</div>
									</div>
								</c:if>
								
								
								
                            </div>
                        </div>
                    </div>
                </div>
        	</div>
        </div>
		
		</div>
		
	</body>
</html>

