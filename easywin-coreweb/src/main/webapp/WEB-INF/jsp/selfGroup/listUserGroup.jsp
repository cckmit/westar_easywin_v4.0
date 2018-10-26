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
	<body>
		<!-- Page Content -->
		<div class="page-content">
			<!-- Page Body -->
			<div class="page-body">
			<c:choose>
			 	<c:when test="${not empty list}">
				<div class="row">
					<div class="col-md-12 col-xs-12">
						<div class="profile-container">
							<div class="profile-body">
									<div class="tabbable">
										<div class="tab-content tabs-flat no-margin">
											<div id="overview" class="tab-pane active">
												<div class="row profile-overview">
													<div class="col-md-3">
														<div class="tabbable border-right2">
															<ul class="nav nav-tabs tabs-flat no-shadow no-border-top"
																id="myTab11">
																<li class="active">
																	<a data-toggle="tab" href="#profile11">组群</a>
																</li>
															</ul>
															<div class="tab-content tabs-flat no-margin no-padding no-shadow">
																<div id="profile11" class="tab-pane active">
																	<div class="panel-body no-padding-left">
																		<a href="javascript:void(0);" onclick="add('${param.sid}');" class="ps-add-group">添加组群</a>
																		<div>
																			<ul class="upload-list-item margin-top-10 no-padding">
																				<c:choose>
																				 	<c:when test="${not empty list}">
																				 		<c:forEach items="${list}" var="group" varStatus="st">
																				 			<li class="upload-list clearfix no-border">
																								<a href="#" class="pull-left grpNameA" groupId="${group.id}">${group.grpName}</a>
																								<div class="pull-right"><!-- other-choice onclick="update(${group.id},'${param.sid}')"-->
																									<a href="javascript:void(0);" groupId="${group.id}" class="fa fa-users fa-lg selfGrpEdit" title="编辑群组成员"></a>
																									<a href="javascript:void(0);" onclick="del(${group.id});" class="fa fa-trash-o fa-lg margin-left-5" title="删除群组"></a>
																									<a href="javascript:void(0);" groupId="${group.id}" class="fa-lg fa ${(not empty latestInfo.defShowGrpId and latestInfo.defShowGrpId eq group.id)?'fa-toggle-on':'fa-toggle-off' } margin-left-5 defaultShow" title="默认显示"></a>
																								</div>
																							</li>
																				 		</c:forEach>
																				 	</c:when>
																				 </c:choose>
																			</ul>
																		</div>
																	</div>

																</div>
															</div>
														</div>
													</div>
													<form role="form" action="/selfGroup/delUserGroup" method="post" class="subform" id="delForm">
														<input type="hidden" name="sid" value="${param.sid }"/>
												   		<input type="hidden" name="email" value="${userInfo.email}"/>
												   		<input type="hidden" name="id" value="${userInfo.id}"/>
												   		<input type="hidden" id="redirectPage" name="redirectPage" />
												   		<input type="hidden" id="ids" name="ids" />
												   		<input type="hidden" id="groupId" name="groupId" />
												   		<input type="hidden" name="grpName" />
												   		<input type="hidden" id="delUserId" name="delUserId" />
												   		<select id="listUser_id" name="listUser.id" multiple="multiple" moreselect="true" style="display:none;">
														</select>
											   		</form>
											   		<!-- 隐藏数据 -->
											   		<c:choose>
													 	<c:when test="${not empty list}">
													 		<c:forEach items="${list}" var="group" varStatus="st">
													 			<select id="self_group_${group.id}" style="display:none;">
												 				<c:choose>
																 	<c:when test="${not empty group.listUser}">
																 		<c:forEach items="${group.listUser}" var="user" varStatus="st">
															 				<option selected="selected" value="${user.id}">${user.userName}</option>
															 			</c:forEach>
														 			</c:when>
													 			</c:choose>
													 			</select>
												 			</c:forEach>
											 			</c:when>
										 			</c:choose>
													<div class="col-md-9">
														<c:choose>
														 	<c:when test="${not empty list}">
														 		<c:forEach items="${list}" var="group" varStatus="st">
														 			<h6 class="row-title before-palegreen margin-top-5">
																		${group.grpName}
																	</h6>
														 			<div class="row grpUserList">
														 				<c:choose>
																		 	<c:when test="${not empty group.listUser}">
																		 		<c:forEach items="${group.listUser}" var="user" varStatus="st">
																		 			<div class="col-lg-3 col-sm-6 col-xs-12">
																						<div class="databox databox-graded">
																							<div class="databox-left no-padding">
																								<img style="width:65px; height:65px;"
																									src="/downLoad/userImg/${userInfo.comId}/${user.id}?sid=${param.sid}"
																									title="${user.userName}" />
																							</div>
																							<div class="databox-right padding-top-20 bg-whitesmoke">
																								<div class="databox-stat orange radius-bordered">
																									<a href="javascript:void(0)" onclick="delGroupUser(this,${group.id},${user.id});" class="orange"><i
																										class="stat-icon fa fa-times fa-lg"></i>
																									</a>
																								</div>
																								<div class="databox-text darkgray">
																									${user.userName}
																								</div>
																								<div class="databox-text darkgray">
																									${user.depName}
																								</div>
																							</div>
																						</div>
																					</div>
																		 		</c:forEach>
																		 	</c:when>
																		 </c:choose>
														 			</div>
														 		</c:forEach>
														 	</c:when>
														 	<c:otherwise>
														 		<h2>没有建立组群！</h2>
														 	</c:otherwise>
														 </c:choose>
														
													</div>
												</div>
										</div>
									</div>
								</div>
							</div>
						</div>

					</div>
				</div>
				</c:when>
				<c:otherwise>
					<div class="container"
						style="left:50%;top:50%;position: absolute;
					margin:-90px 0 0 -180px;padding-top:200px;text-align:center;width:488px;">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>您还没有创建属于自己的分组！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
								<a href="javascript:void(0);" onclick="add('${param.sid}');"
									class="return-btn"><i class="fa fa-plus"></i>添加分组</a>
							</div>
						</section>
					</div>
				</c:otherwise>
				</c:choose>
				<!-- /Page Body -->
			</div>
			<!-- /Page Content -->
		</div>
	</body>
	<script type="text/javascript">
		$(function(){
			if("${param.tips}"){
				window.top.layui.use('layer', function(){
					var layer = window.top.layui.layer;
					window.top.layer.tips("在这里设置！",$(".defaultShow"),{tips:1})
				})
			}
			$("body").on("click",".defaultShow",function(){
				var actObj = $(this);
				var show=$(actObj).hasClass("fa-toggle-off")?"yes":"no";
				var groupId=$(actObj).hasClass("fa-toggle-off")?$(actObj).attr("groupId"):0;
				//清除所有样式
				$(".defaultShow").removeClass("fa-toggle-on");
				$(".defaultShow").removeClass("fa-toggle-off");
				//默认开关
				$(".defaultShow").addClass("fa-toggle-off");
				postUrl("/selfGroup/initDefGrpToShow?sid=${param.sid}",{groupId:groupId},function(data){
					if(data.status=='y'){
						//开启/关闭显示样式
						if(show=='yes'){
							$(actObj).removeClass("fa-toggle-off");
							$(actObj).addClass("fa-toggle-on");
						}else{
							$(actObj).removeClass("fa-toggle-on");
							$(actObj).addClass("fa-toggle-off");
						}
					}
					showNotification(1,data["info"]);
				})
			});
			$("body").on("click",".selfGrpEdit",function(){
				var actObj = $(this);
				var groupId=$(actObj).attr("groupId");
	        	$(".subform input[name='id']").val(groupId);
				userMore("self_group_"+groupId,null,"${param.sid}",null,null,function(users){
					if(users){
						for ( var i = 0; i < users.length; i++) {
							  var input =$("<input type=\"hidden\" name=\"\" value=\"\">");
							  $(input).attr("name","listUser["+i+"].id");
							  $(input).val(users[i].value);
							  $(".subform").append(input);
					   }
					   var url="/selfGroup/updateGroupByAjax?sid=${param.sid}&t="+Math.random();
					   formSub(url);//表单提交
					}else{
						showNotification(2,"没有人被选！");
					}
				});
			});
			$("body").on("click",".grpNameA",function(){//点击事件
				var actObj = $(this);
				var groupId=$(actObj).attr("groupId");
				var input =$("<input type=\"text\" class=\"grpNameInput\" groupId=\""+groupId+"\" name=\"grpName\" value=\"\">");
				$(input).val($(actObj).text());
				$(actObj).after(input);
				$(actObj).css("display","none");
				$(input).focus();
			});
			$("body").on("change",".grpNameInput",function(){//值变事件
				var actObj = $(this);
				var groupId=$(actObj).attr("groupId");
	        	$(".subform input[name='id']").val(groupId);
	        	$(".subform input[name='grpName']").val($(actObj).val());
			    var url="/selfGroup/updateSelfGroupName?sid=${param.sid}&t="+Math.random();
			    formSub(url);//表单提交
			});
			$("body").on("blur",".grpNameInput",function(){//失效
				var actObj = $(this);
				$(actObj).prev().css("display","block");
				$(actObj).css("display","none");
			});
		});
		//表单提交
		function formSub(url){
			$(".subform").ajaxSubmit({
			        type:"post",
			        url:url,
			        dataType: "json",
			        async: false,
			        beforeSubmit:function (a,f,o){
					}, 
			        success:function(data){
				        if('y'==data.status){
				        	window.location.reload();
				        }else{
			        		showNotification(2,data.info);
				        }
			        },error:function(XmlHttpRequest,textStatus,errorThrown){
			        	showNotification(2,"系统错误，请联系管理人员");
			        }
			 });
		}
	</script>
</html>
