<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
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
		"ewebMaxMin":"self",
		"ifreamName" : "${param.ifreamName}",
		"busId":"${busId}"
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
				validMsgV2(msg, o, cssctl);
			},
			datatype:{
				"input":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(str){
						var count = str.replace(/[^\x00-\xff]/g,"**").length;
						var len = $(obj).attr("defaultLen");
						if(count>len){
							return "制度标题太长";
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
		
		var title = $("#instituTitle").val();
		var count = title.replace(/[^\x00-\xff]/g,"**").length;
		$('#msgTitle').html("(" + count/2 + "/26)");
		
		//任务名称更新
		$("#instituTitle").change(function(){
			 title = $("#instituTitle").val();
			if(regex.test(title)){
				return false;
			}
			 count = title.replace(/[^\x00-\xff]/g,"**").length;
			var len = $("#instituTitle").attr("defaultLen");
			//关联项目长度超过指定的长度
			if(count>len){
				return false;
			}else{
				if(title){
					$.post("/institution/institutionTitleUpdate?sid=${param.sid}",{Action:"post",id:${institution.id},title:title},     
		 				function (msgObjs){
						showNotification(1,msgObjs);
						$("#instituTitle").attr("title",title);
						title = "--"+cutstr(title,32);
						$("#titleTitle").html(title);
					});
				}
			}
		});
		//制度类型更新
	$("#instituType").change(function(){
		//制度类型
		if($("#instituType").val()){
			$.post("/institution/updateInstituType?sid=${param.sid}",{Action:"post",id:${institution.id},instituType:$("#instituType").val()},     
				function (data){
				if(data.status == 'y'){
					showNotification(1,data.info);
				}else{
					showNotification(2,data.info);
				}
			},"json");
		}
	});
	
	});
	/**
 * 保存简介
 */
function saveRemark(){
	var instituRemark = document.getElementById("eWebEditor1").contentWindow.getHTML();
	if(instituRemark){
		$.post("/institution/updateInstitumRemark?sid=${param.sid}",{Action:"post",id:${institution.id},instituRemark:instituRemark},     
				function (msgObjs){
			showNotification(1,msgObjs);
		});
	}
}
	//双击移除
	function removeChoose(type, comId, busId, ts) {
		$(ts).remove();
		if(type == 'dep'){
			removeOption("scopeDep_select",busId);	
		}else{
			removeOption("scopeUser_select",busId);	
		}
		
		var params = {
			"sid" : EasyWin.sid,
			"busId" : busId,
			"type" : type,
			"institutionId" : "${institution.id}"
		}
		var url = "/institution/delScope";
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
							  depHtml +=	 '\n  ondblclick="removeChoose(\'dep\',\'dep\',\''+deps[i].value +'\',this)">'
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
						"institutionId" : "${institution.id}"
					};
					var url = "/institution/updateScope";
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
							"institutionId" : "${institution.id}"
						};
						var url = "/institution/updateScope";
						postUrl(url, params, function(data) {
							var status = data.status;
							if (status == 'y') {
								showNotification(1, "修改成功！");
							}
						});
			});
		});
});
	//菜单切换
$(function(){
	//制度详情
	$("#institubaseMenuLi").click(function(){
		$("#otherinstituAttrIframe").css("display","none");
		$("#instituBase").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$("#institubaseMenuLi").attr("class","active");
	});
	//讨论
	$("#instituTalkMenuLi").click(function(){
		$("#otherinstituAttrIframe").css("display","block");
		$("#instituBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#instituTalkMenuLi").attr("class","active");
		$("#otherinstituAttrIframe").attr("src","/institution/instituTalkPage?sid="+EasyWin.sid+"&pager.pageSize=10&institutionId="+EasyWin.busId);
	});
	//制度浏览记录
	$("#instituViewRecord").click(function(){
		$("#otherinstituAttrIframe").css("display","block");
		$("#instituBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#instituViewRecord").attr("class","active");
		$("#otherinstituAttrIframe").attr("src","/institution/listViewRecord?sid="+EasyWin.sid+"&instituId=${institution.id}&ifreamName=otherinstituAttrIframe&pager.pageSize=10");
	});
	//日志
	$("#instituLogs").click(function(){
		$("#otherinstituAttrIframe").css("display","block");
		$("#instituBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#instituLogs").attr("class","active");
		$("#otherinstituAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${institution.id}&busType=040&ifreamName=otherinstituAttrIframe");
	});
});

</script>
<style type="text/css">
.ps-listline {
	line-height: 30px !important
}
</style>
</head>
<body>
	<form id="subform" class="subform" method="post">
		<tags:token></tags:token>
		<input type="hidden" name="attentionState" id="attentionState" value="0" />
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							 <span class="widget-caption themeprimary" style="font-size: 20px!important;margin-top: 2px">制度</span>
							<span class="widget-caption themeprimary" style="font-size: 20px!important;margin-top: 2px" id="titleTitle"> <c:choose>
									<c:when test="${fn:length(institution.title)>20 }">
	                        	--${fn:substring(institution.title,0,20)}..
                       		</c:when>
									<c:otherwise>
                       			--${institution.title}
                       		</c:otherwise>
								</c:choose>
							</span>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
									<i class="fa fa-times themeprimary"></i>
								</a>
							</div>
						</div>
						<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
						<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
							<div class="widget-body no-shadow">
							<div class="widget-main ">
								<div class="tabbable">
									<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0" >
										<li class="active" id="institubaseMenuLi">
											<a data-toggle="tab" href="javascript:void(0)">制度详情</a>
										</li>
										<li id="instituTalkMenuLi">
											<a data-toggle="tab" href="javascript:void(0)">留言</a>
										</li>
										<li id="instituViewRecord">
											<a data-toggle="tab" href="javascript:void(0)">查看记录</a>
										</li>
										<li id="instituLogs">
											<a data-toggle="tab" href="javascript:void(0)">操作日志</a>
										</li>
									</ul>
								</div>

								<div class=" tabs-flat">
									<div id="instituBase">
										<jsp:include page="./instituBase_edit.jsp"></jsp:include>
									</div>
									<iframe id="otherinstituAttrIframe" style="display:none;" class="layui-layer-load" src="" border="0" frameborder="0" allowTransparency="true" noResize scrolling="no" width=100% height=100%
										vspale="0"></iframe>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);
	</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
