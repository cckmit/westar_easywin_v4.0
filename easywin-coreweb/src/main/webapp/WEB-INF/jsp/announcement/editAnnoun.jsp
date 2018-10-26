<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="utf-8" />
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<meta name="description" content="Dashboard" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
	var regex = /['|<|>|"]+/;
</script>
<script type="text/javascript">
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
	$(function() {
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
							return "公告标题太长";
						}else{
							return true;
						}
					}else{
						return false;
					}
				},
			},
			showAllError : true
		});
		
		var title = $("#announTitle").val();
		var count = title.replace(/[^\x00-\xff]/g,"**").length;
		$('#msgTitle').html("(" + count/2 + "/26)");
		
		//公告标题更新
		$("#announTitle").change(function(){
			 title = $("#announTitle").val();
			if(regex.test(title)){
				return false;
			}
			 count = title.replace(/[^\x00-\xff]/g,"**").length;
			var len = $("#announTitle").attr("defaultLen");
			//关联长度超过指定的长度
			if(count>len){
				return false;
			}else{
				if(title){
					$.post("/announcement/announTitleUpdate?sid=${param.sid}",{Action:"post",id:${announ.id},title:title},function (msgObjs){
						showNotification(1,msgObjs);
						$("#announTitle").attr("title",title);
						title = "--"+cutstr(title,32);
						$("#titleTitle").html(title);
					});
				}
			}
		});
		//公告类型更新
		$("#grade").change(function(){
			//制度类型
			if($("#grade").val()){
				$.post("/announcement/updateAnnounGrade?sid=${param.sid}",{Action:"post",id:${announ.id},grade:$("#grade").val()},function (msgObjs){
					showNotification(1,msgObjs);
				},"json");
			}
		});
		
		$('input[type=radio][name=type]').change(function() {
			$.post("/announcement/updateAnnounType?sid=${param.sid}",{Action:"post",id:${announ.id},grade:$(this).val()},function (msgObjs){
				showNotification(1,msgObjs);
			},"json");
		});
	});
	
	/**
 * 保存简介
 */
function saveRemark(){
	var announRemark = document.getElementById("eWebEditor1").contentWindow.getHTML();
	if(announRemark){
		$.post("/announcement/updateAnnounmRemark?sid=${param.sid}",{Action:"post",id:${announ.id},announRemark:announRemark},     
				function (msgObjs){
			showNotification(1,msgObjs);
		});
	}
}
		//删除附件
		function delAnnounFile(upfileId,ts){
		$.post("/announcement/delAnnounFile?sid=${param.sid}",{Action:"post",announId:${announ.id},upfileId:upfileId},function (msgObjs){
				showNotification(1,msgObjs);
				$(ts).parent().remove();
			});
		}
	//双击移除
	function removeChoose(type, comId, busId, ts) {
		ts.remove();
		if(type == 'dep'){
			removeOption("scopeDep_select",busId);	
		}else{
			removeOption("scopeUser_select",busId);	
		}
		var params = {
			"sid" : EasyWin.sid,
			"busId" : busId,
			"type" : type,
			"announId" : "${announ.id}"
		}
		var url = "/announcement/delScope";
		postUrl(url, params, function(data) {
			var status = data.status;
			if (data.changeScope) {
				$(".allOrgUser").show();
			}
			if (status == 'y') {
				showNotification(1, "移除成功！");
			}
		});
		if ($("#" + type + "Box").find("span").length == 0) {
			$("#" + type + "Li").hide();
		}
	}
	
	var sid = '${param.sid}';
	$(function(){
	//部门选择
		$("#dep").click(function() {
			var depArray = new Array();
			depMoreBack("scopeDep_select",null,sid,"yes",'',function(deps){
			    $("#depBox").html('');
				if(deps != null && deps.length>0){
					$("#depLi").show();
					for(var i=0;i<deps.length;i++){
					 	var depName = deps[i].text;//部门名称
					  
					  	var depHtml = '\n <span  style="cursor:pointer;"';
							  depHtml +=   '\n  title="双击移除" class="label label-default margin-right-5 margin-bottom-5"'
							  depHtml +=	 '\n  ondblclick="removeChoose(\'dep\',\'dep\',\''+deps[i].value+'\',this)">'
							  depHtml += depName+'</span>';
					
						  $("#depBox").append(depHtml); 
							depArray.push(deps[i].value);
						  $('#scopeDep_select').append("<option selected='selected' value='" + deps[i].value + "'>" + deps[i].text+ "</option>");	
					}
				}else{
					$("#depLi").hide();
				}
				var params = {
						"sid" : EasyWin.sid,
						"busId" : depArray.join(","),
						"type" : "dep",
						"announId" : "${announ.id}"
					};
					var url = "/announcement/updateScope";
					postUrl(url, params, function(data) {
						var status = data.status;
						if (status == 'y') {
							showNotification(1, "修改成功！");
						}
					});
			});
		});

		//人员选择
		$("#user").click(function(){
			userMore("scopeUser_select",null,sid,"yes",'',function(users){
				var userArray = new Array();
				//清除
				  $("#userBox").html('');
				  removeOptions("scopeUser_select");
				  if(users != null && users.length>0){
					  $("#userLi").show();
					  
					  for(var i= 0;i<users.length;i++){
						  	var userName = users[i].text;
						    var userHtml = '\n <span  style="cursor:pointer;"'
						    	userHtml +=   '\n  title="双击移除" class="label label-default margin-right-5 margin-bottom-5"'
							    userHtml +=	 '\n  ondblclick="removeChoose(\'user\',\'user\',\''+users[i].value+'\',this)">'
							    userHtml += userName+'</span>'
						  	$("#userBox").append(userHtml);
								userArray.push(users[i].value); 
						    $('#scopeUser_select').append("<option selected='selected' value='" + users[i].value + "'>" + users[i].text + "</option>");	
					  }
				  }else{
					  $("#userLi").hide();
				  }
				  var params = {
							"sid" : EasyWin.sid,
							"busId" : userArray.join(","),
							"type" : "user",
							"announId" : "${announ.id}"
						};
						var url = "/announcement/updateScope";
						postUrl(url, params, function(data) {
							var status = data.status;
							if (status == 'y') {
								showNotification(1, "修改成功！");
							}
						});
			});
		});
});
	
</script>
<style type="text/css">
.ps-listline{
line-height:30px !important}
</style>
</head>
<body>
<form class="subform" method="post" action="/announcement/addAnnoun">
<tags:token></tags:token>
<input type="hidden" name="attentionState" id="attentionState" value="0"/>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
						<div
						class="widget-header bordered-bottom bordered-themeprimary detailHead">
						 <span class="widget-caption themeprimary" style="font-size: 20px!important;margin-top: 2px">公告</span>
							<span class="widget-caption themeprimary" style="font-size: 20px!important;margin-top: 2px" id="titleTitle"> <c:choose>
									<c:when test="${fn:length(announ.title)>20 }">
	                        	--${fn:substring(announ.title,0,20)}..
                       		</c:when>
									<c:otherwise>
                       			--${announ.title}
                       		</c:otherwise>
								</c:choose>
							</span>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i> </a>
						</div>
					</div>
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                     <div class="widget-body margin-top-40" id="contentBody"  style="overflow: hidden;overflow-y:scroll;" >
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">基础配置</span>
                                <div class="widget-buttons btn-div-full">
                                	<a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-minus blue"></i>
                                   </a>
                                 </div>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	<i class="fa fa-bookmark blue"></i>&nbsp;标题
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="announTitle" datatype="input,sn" defaultLen="52" name="title" nullmsg="请填写公告标题" 
													class="colorpicker-default form-control" type="text" value="${announ.title }"
													onpropertychange="handleName()" onkeyup="handleName()"
													style="width: 400px;float: left">
													<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
												</div>
											</div>
											<script> 
												//当状态改变的时候执行的函数 
												function handleName(){
													var value = $('#announTitle').val();
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
													document.getElementById('announTitle').onpropertychange=handleName 
												}else {//非ie浏览器，比如Firefox 
													document.getElementById('announTitle').addEventListener("input",handleName,false); 
												} 
											</script>  
                                         </li>
                                           <li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text">
													<i class="fa fa-volume-off blue" style="font-size: 15px"></i>&nbsp;公告类型 
													<span style="color: red">*</span>
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<label class="padding-left-5">
													 	<input type="radio" class="colored-blue" name="type" ${announ.type eq 1 ?'checked="checked"':''}  value="1"/>
													 	<span class="text" style="color:inherit;">通知</span>
												    </label>
												    <label class="padding-left-5">
													 	<input type="radio" class="colored-blue" name="type" ${announ.type eq 2 ?'checked="checked"':''}   value="2"/>
													 	<span class="text" style="color:inherit;">通报</span>
												    </label>
												    <label class="padding-left-5">
													 	<input type="radio" class="colored-blue" name="type" ${announ.type eq 3 ?'checked="checked"':''}  value="3"/>
													 	<span class="text" style="color:inherit;">决定</span>
												    </label>
												    <label class="padding-left-5">
													 	<input type="radio" class="colored-blue" name="type" ${announ.type eq 4 ?'checked="checked"':''}   value="4"/>
													 	<span class="text" style="color:inherit;">其他</span>
												    </label>
												</div>
											</li>
                                         
                                          <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	<i class="fa fa-star blue"></i>&nbsp;重要程度
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											   <tags:dataDic type="grade" value="${announ.grade }" name="grade" ></tags:dataDic>
											</div>               
                                          </li>
                                          <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	<i class="fa fa-user blue"></i>&nbsp;公告范围
										    </div>
											<div class="ticket-user pull-left ps-right-box" id="scope" style="min-width: 135px">
											 	 <label class="padding-left-5">
												 	<button id="dep" class="btn btn-info btn-primary btn-xs"  type="button">按部门</button>
											    </label> 
											    
												<label class="padding-left-5">
												 	<button id="user" class="btn btn-info btn-primary btn-xs" type="button">按人员</button>
											    </label>
											    <span style="color: red">*</span>
											     <span >未设置公告范围则全体成员可见</span>
											    <!--  <span >(发布范围取单位、部门和人员的并集)</span> -->
											</div>

												<div style="float: left;width: 250px;display: none">
													<select list="listScopeDep" listkey="id" listvalue="depName" id="scopeDep_select" name="listScopeDep.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
													<c:forEach items="${scopeDep }" var="obj" varStatus="vs">
															<option selected="selected" value="${obj.id }">${obj.depName }</option>
														</c:forEach>
													</select>
												</div>
												<div style="float: left;width: 250px;display: none">
													<select list="listScopeUser" listkey="id" listvalue="userName" id="scopeUser_select" name="listScopeUser.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
													<c:forEach items="${scopeUser }" var="obj" varStatus="vs">
															<option selected="selected" value="${obj.id }">${obj.userName }</option>
														</c:forEach>
													</select>
												</div>
											</li>
                                          <li  id="depLi" class="ticket-item no-shadow ps-listline" style="clear:both;display:${empty scopeDep ? 'none':'block'}" >
										   	<div class="pull-left gray ps-left-text">
										    	&nbsp;部门
										    </div>
										    <div class="pull-left gray ps-left-text padding-left-5" style="width:70%"  id= "depBox">
										    <c:forEach items="${scopeDep }" var="dep">
														<c:set var="creator">
															title="双击移除" ondblclick="removeChoose('dep','${dep.comId}','${dep.id }',this)"
														</c:set>
														<span style="cursor:pointer;" class="label label-default margin-right-5 margin-bottom-5" ${creator }>${dep.depName }</span>
													</c:forEach>
										    </div>
										    <div style="clear:both;"></div> 
                                          </li>
                                            <li  id="userLi" class="ticket-item no-shadow ps-listline" style="clear:both;display:${empty scopeUser ? 'none':'block'};" >
										   	<div class="pull-left gray ps-left-text">
										    	&nbsp;人员
										    </div>
										    <div class="pull-left gray ps-left-text padding-left-5" style="width:70%" id= "userBox">
										    <c:forEach items="${scopeUser }" var="user">
														<c:set var="creator">
															title="双击移除" ondblclick="removeChoose('user','${user.comId}','${user.id }',this)"
														</c:set>
														<span style="cursor:pointer;" title="双击移除" class="label label-default margin-right-5 margin-bottom-5" ${creator }>${user.userName }</span>
													</c:forEach>
										    </div>
										    <div style="clear:both;"></div> 
                                          </li>
								
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-file blue"></i>&nbsp;相关附件</div>
											<div id="file">
												<c:choose>
													<c:when test="${not empty announ.listAnnounFiles}">
														<div class="file_div">
															<c:forEach items="${announ.listAnnounFiles}" var="upfiles" varStatus="vs">
																<p class="p_text">
																	附件(${vs.count})： ${upfiles.filename}
																	<c:choose>
																		<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx' }">
													 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
													 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览"
																				onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','039','${announ.id}')"></a>
																		</c:when>
																		<c:when test="${upfiles.fileExt=='txt' || upfiles.fileExt=='pdf'}">
											 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
													 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览"
																				onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','039','${announ.id}')"></a>
																		</c:when>
																		<c:otherwise>
													 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
																		</c:otherwise>
																	</c:choose>
																	<a href="javascript:void(0);"  onclick="delAnnounFile('${upfiles.upfileId}',this)" class="fa fa-trash-o" title="删除"></a>
																</p>
															</c:forEach>
														</div>
													</c:when>
												</c:choose>
											</div>
												<%-- <div class="ticket-user pull-left ps-right-box"
													style="width: 400px;height: auto;">
													<div class="margin-top-10">
														<tags:uploadMore name="listUpfiles.id" showName="fileName"
															ifream="" comId="${userInfo.comId}"></tags:uploadMore>
													</div>
												</div> --%>
												<div class="ps-clear"></div>
											</li>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                           <div class="widget radius-bordered  " id="remark" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue"><span style="color: red">*</span>公告内容</span>
                                   <div class="widget-buttons btn-div-full">
                                      <a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-plus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               	 <!--tip="请填写公告内容"   -->
                               <div class="widget-body no-shadow">
                               	<div class="tickets-container bg-white">
									<textarea class="form-control" id="announRemark" name="announRemark" rows="" cols=""
									style="width: 100%;height: 110px;display:none;">${announ.announRemark }</textarea> 
									<iframe id="eWebEditor1" src="/static/plugins/ewebeditor/ewebeditor.htm?id=announRemark&style=blue" 
									frameborder="0" scrolling="no" width="100%" height="350"></iframe>
									<button type="button" onclick="saveRemark()" class="btn btn-info ws-btnBlue pull-right">保存</button>
                                </div>
                              </div>
                           </div>
                           <div class="widget-body no-shadow">
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
