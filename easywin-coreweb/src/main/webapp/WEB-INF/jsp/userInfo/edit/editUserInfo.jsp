<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<%@taglib prefix="t" uri="/WEB-INF/tld/t.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12">
					<div class="profile-container">
						<div class="profile-body">
							<div class="tabbable">
								<div class="tab-content tabs-flat no-margin">
									<div class="tab-pane active">
										<form role="form" action="/userInfo/editUserInfo"
											method="post" class="subform" id="userInfoForm">
											<div id="leaderTempDiv"></div>
											<input type="hidden" name="sid" value="${param.sid }" /> <input
												type="hidden" name="email" value="${userInfo.email}" /> <input
												type="hidden" name="id" value="${userInfo.id}" /> <input
												type="hidden" id="redirectPage" name="redirectPage" />
											<div class="form-title themeprimary" style="padding:5px 0;position: relative;">
												<span class="strong">个人信息设置</span>
												<a href="javascript:void(0)" class="pull-right themeprimary viewRsdaBase" userId="${userInfo.id}">查看档案</a>
												<a href="javascript:void(0)" class="pull-right themeprimary updateRsdaBase" userId="${userInfo.id}">编辑档案|</a>
												
											</div>
											<div class="row">
												<div class="col-sm-6">
													<div class="form-group">
														<span class="input-icon icon-left"> <input
															name="userName" value="${userInfo.userName}" type="text"
															class="form-control bordered-themeprimary"
															placeholder="姓名" title="姓名"
															onblur="updateUserAttr('userName','${userInfo.userName}',this);">
															<i class="fa fa-user blue" title="姓名"></i>
														</span>
													</div>
												</div>
												<div class="col-sm-6">
													<div class="form-group">
														<span class="input-icon icon-left"
															onblur="updateUserAttr('gender');" style="padding-left:33px;padding-top:6px;"> <tags:radioDataDic
																type="gender" name="gender" style="ws-radio"
																value="${userInfo.gender}"></tags:radioDataDic> <i
															class="glyphicon glyphicon-question-sign azure"
															title="性别"></i>
														</span>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-6">
													<div class="form-group">
														<span class="input-icon icon-left"> <i
															class="fa fa-chain purple" title="登录别名"></i> <input
															value="${userInfo.nickname}" name="nickname"
															ajaxurl="/userInfo/checkNickName?sid=${param.sid}"
															type="text" class="form-control bordered-themeprimary"
															placeholder="登录别名" title="登录别名"
															onblur="updateUserAttr('nickname','${userInfo.nickname}',this);">
														</span>
													</div>
												</div>
												<div class="col-sm-6">
													<div class="form-group">
														<span class="input-icon icon-left"> <input
															name="birthday"
															readonly="readonly" value="${userInfo.birthday }"
															class="form-control date-picker bordered-themeprimary"
															id="id-date-picker-1" type="text" title="出生日期"placeholder="出生日期"
															onfocus="WdatePicker({dateFmt:'yyyy年MM月dd日',onpicked:function(dp){dateUpdate('birthday',this);}})">
															<i class="fa fa-calendar" title="出生日期"></i>
														</span>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-6">
													<div class="form-group">
														<span class="input-icon icon-left"> <input
															name="job" value="${userInfo.job}" type="text"
															class="form-control bordered-themeprimary"
															placeholder="职务"
															onblur="updateUserAttr('job','${userInfo.job}',this);">
															<i class="fa fa-bookmark azure" title="职务"></i>
														</span>
													</div>
												</div>
												<div class="col-sm-6">
													<div class="form-group">
														<input type="hidden"
															value="${fn:substring(hireMaxDate,0,10)}"
															id="hireMaxDate"> <span
															class="input-icon icon-left"> <div
															name="hireDate" readonly="readonly" style="cursor: default;"
															class="form-control bordered-themeprimary padding-left-40"
															title="入职时间">${fn:substring(userInfo.hireDate,0,10) }</div>
															<i class="fa fa-linkedin-square blue" title="入职时间"></i>
														</span>
													</div>
												</div>
											</div>
											<div style="max-width:460px;display:${(not empty leaderJson)?'none':'block'}" class="clearfix margin-top-10 margin-bottom-10" id="leaderSetDiv">
												<span class="strong">我是老板：</span>
												<label class="margin-right-10 no-margin-bottom">
													<input type="radio" name="isChief" value="1">
													<span class="text">是</span>
												</label>
												<label class="margin-right-10 no-margin-bottom">
													<input type="radio" checked="checked" name="isChief" value="0">
													<span class="text">否</span>
												</label>
											</div>
											<div id="needLeaerDiv">
												<div class="form-title themeprimary strong" style="padding:5px 0;">直属上级</div>
												<div class="row">
													<div class="col-sm-6">
														<div class="pull-left" style="width: 250px;display:none;">
															 <select list="listUserInfo" listkey="id" listvalue="userName" id="listUserInfo_id" name="listUserInfo.id" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
															 </select>
														</div>
														<div>
															<div id="userLeader_div" class="pull-left" style="max-width:460px;">
															</div>
															<div class="pull-left">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-bottom-10 margin-left-5" 
																title="人员单选" id="userLearChooseBtn" style="float: left;">
																	<i class="fa fa-plus"></i>选择
																</a> 
															</div>
														</div>
														<input type="hidden" id="leaderJson" name="leaderJson" value="${leaderJson}" />
													</div>
												</div>
											</div>
											<div class="form-title themeprimary margin-top-10" style="padding:5px 0;">
												<div class="row">
													<div class="col-sm-6 strong">
														所属部门
													</div>
													<div class="col-sm-6 strong">
														工作代理
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-3">
													<div class="form-group" style="position:relative;">
														<span class="input-icon icon-left" style="width:60%;"> 
														<input id="depId" name="depId" type="hidden" value="${userInfo.depId }">
														<input id="depName"  value="${userInfo.depName }" type="text" readonly="readonly"  disabled="disabled"
														 class="form-control bordered-themeprimary" placeholder="部门" >
														<i class="fa fa-sitemap azure" title="部门"></i>
														</span>
														 <a href="javascript:void(0);" id="depChooseBtn" 
														 class="btn btn-primary" style="position:absolute;right:0; top:0;">
															<i class="fa fa-plus"></i>部门选择</a>
													</div>
												</div>
												<div class="col-sm-3">&nbsp;</div>
												<div class="col-sm-4">
													<div class="form-group" style="position:relative;">
														<span class="input-icon icon-left" style="width:60%;">
															<input id="forMeDoId" name="forMeDoId" type="hidden" value="${forMeDo.userId}">
															<input id="forMeDoName" value="${forMeDo.userName }" type="text" readonly="readonly"
															class="form-control bordered-themeprimary" placeholder="代理设置" title="双击移除">
															<i class="fa fa-user azure" title="代理设置"></i>
														</span>
														<a href="javascript:void(0);" id="forMeDoBtn" class="btn btn-primary" style="position:absolute;right:0; top:0;">
															<i class="fa fa-plus"></i> 人员选择
														</a>
													</div>
												</div>
												<div class="col-sm-2">&nbsp;</div>
												<%-- <c:choose>
													<c:when test="${not empty forMeDoes}">
														<div class="col-sm-6">
															<span>代理人员：</span>
																<c:forEach items="${forMeDoes}" var="obj" varStatus="vs">
																	<span style='cursor:default;' class="label label-default margin-top-5 margin-left-5 margin-bottom-5">
																		${obj.userName }
																	</span>
																</c:forEach>
														</div>
													</c:when>
													<c:otherwise>
														
													</c:otherwise>
												</c:choose> --%>
											</div>
											<div class="form-title themeprimary strong" style="padding:5px 0;">邮箱绑定</div>
											<div class="row">
												<div class="col-sm-6">
													<div class="form-group" style="position:relative;">
														<span class="input-icon icon-left" style="width:80%;">
															<input value="${userInfo.email}" disabled="disabled"
															readonly="readonly" type="text" title="邮箱"
															class="form-control bordered-themeprimary"
															placeholder="邮箱"> <i
															class="fa fa-envelope orange" title="邮箱"></i>
														</span> <a href="javascript:void(0);" class="btn btn-primary"
															style="position:absolute;right:0; top:0;"
															onclick="updateUserAccount('email','${userInfo.email}',this)">
															<i class="fa fa-plus"></i>${(not empty userInfo.email)?'重新绑定':'邮箱绑定'}</a>
													</div>
												</div>
											</div>
											<div class="form-title themeprimary strong" style="padding:5px 0;">手机绑定</div>
											<div class="row">
												<div class="col-sm-6">
													<div class="form-group" style="position:relative;">
														<span class="input-icon icon-left" style="width:80%;">
															<input name="movePhone" value="${userInfo.movePhone}"
															disabled="disabled" type="text"
															class="form-control bordered-themeprimary"
															placeholder="手机" title="移动电话"> <i
															class="glyphicon glyphicon-phone palegreen" title="移动电话"></i>
														</span> <a href="javascript:void(0);" class="btn btn-primary"
															style="position:absolute;right:0; top:0;"
															onclick="updateUserAccount('phone','${userInfo.movePhone}',this)"><i
															class="fa fa-plus"></i>${(not empty userInfo.movePhone)?'重新绑定':'手机绑定'}</a>
													</div>
												</div>
											</div>
											<div class="form-title themeprimary strong" style="padding:5px 0;">更多联系方式</div>
											<div class="row">
												<div class="col-sm-6">
													<div class="form-group">
														<span class="input-icon icon-left"> <input
															name="linePhone" value="${userInfo.linePhone}"
															type="text" class="form-control bordered-themeprimary"
															placeholder="座机号" title="座机号"
															onblur="updateUserAttr('linePhone','${userInfo.linePhone}',this);">
															<i class="glyphicon glyphicon-earphone yellow"
															title="座机号"></i>
														</span>
													</div>
												</div>
												<div class="col-sm-6">
													<div class="form-group">
														<span class="input-icon icon-left"> <input
															name="qq" value="${userInfo.qq}" type="text"
															class="form-control bordered-themeprimary"
															placeholder="QQ号" title="QQ号"
															onblur="updateUserAttr('qq','${userInfo.qq}',this);">
															<i class="fa fa-linux purple" title="QQ号"></i>
														</span>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-6">
													<div class="form-group">
														<span class="input-icon icon-left"> <input
															name="wechat" value="${userInfo.wechat}" type="text"
															class="form-control bordered-themeprimary"
															placeholder="微信号" title="微信号"
															onblur="updateUserAttr('wechat','${userInfo.wechat}',this);">
															<i class="fa fa-stack-exchange azure" title="微信号"></i>
														</span>
													</div>
												</div>
											</div>
											<!-- <div class="panel-body">
												<button type="button" onclick="infoSave();"
													class="btn btn-primary pull-right">保 存</button>
											</div> -->
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->
	</div>
	<!-- /Page Container -->
	<script type="text/javascript">
		var EasyWin = {"sid":"${param.sid}",
   			"userInfo":{
   				"userId":"${userInfo.id}",
    			"userName":"${userInfo.userName}",
    			"comId":"${userInfo.comId}",
    			"orgName":"${userInfo.orgName}",
    			"isAdmin":"${userInfo.admin}",
   				}
   		};	
	
		/* 清除下拉框中选择的option */
		/* 清除下拉框中选择的option */
		function removeClickUser(id,selectedUserId) {
			var selectobj = document.getElementById(id);
			for ( var i = 0; i < selectobj.options.length; i++) {
				if (selectobj.options[i].value==selectedUserId) {
					selectobj.options[i] = null;
					break;
				}
			}
			$("#user_img_"+id+"_"+selectedUserId).remove();
			userMoreCallBack();//直属上级设置
			$("#leaderTempDiv").html("");
			updateUserAttr("leader", "old", this);
		}
		//日期点击更新事件
		function dateUpdate(type,obj){
			if("hireDate"==type){
				updateUserAttr("hireDate", "${userInfo.hireDate}", obj);//入职日期更新
			}else if("birthday"==type){
				updateUserAttr("birthday", "${userInfo.birthday}", obj);//生日更新
			}
		}
		$(function() {
			//性别点击事件注册
			$("#userInfoForm [name='gender']").click(function() {
				updateUserAttr("gender", "${userInfo.gender}", this);
			});
			
			//代理人员设定选择
		    $("#forMeDoBtn").on("click",function(){
		    	userOne("forMeDoId", "forMeDoName", "yes", "${param.sid}",function(options){
		    		if(options && options.length>0){
			    		var forMeDoId = options[0].value;
			    		var forMeDoName = options[0].text;
			    		if(EasyWin.userInfo.userId == forMeDoId){
			    			layer.tips("代理人员不能是自己",$("#forMeDoName"),{
								tips:[1,"#FFC125"],
								time:1800
							});
			    		}else{
			    			//设置代理人员
			    			postUrl("/userInfo/updateForMeDo",{"sid":"${param.sid}","forMeDoId":forMeDoId},function(data){
			    				if(data.status=='y'){
						    		$("#forMeDoId").val(forMeDoId);
						    		$("#forMeDoName").val(forMeDoName);
						    		showNotification(1,"设置成功");
			    				}else{
			    					showNotification(2,data.info);
			    				}
			    			});
			    		}
		    		}else{
		    			showNotification(2,"未选择代理人员");
		    		}
		    	})
			});
			//删除代理人员
			$("#forMeDoName").on("dblclick",function(){
				if($("#forMeDoName").val()){
					window.top.layer.confirm("确定删除代理人员",function(index,layeo){
						window.top.layer.close(index);
						postUrl("/userInfo/delForMeDo",{"sid":"${param.sid}"},function(data){
		    				if(data.status=='y'){
					    		$("#forMeDoId").val('');
					    		$("#forMeDoName").val('');
					    		showNotification(1,"操作成功");
		    				}else{
		    					showNotification(2,data.info);
		    				}
		    			});
					})
				}
			})
		});
	</script>
</body>
</html>
