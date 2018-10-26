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
		<c:if test="${param.activityMenu=='self_m_1.1'}">
			<link rel="stylesheet" type="text/css" href="/static/plugins/msgshareline/css/history.css" >
		</c:if>
		<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript" src="/static/js/selfCenterJs/selfCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript">
			$(function() {
				$(".subform").Validform({
					tiptype : function(msg, o, cssctl) {
						validMsgV2(msg, o, cssctl);
					},
					showAllError : true
				});
			})
			var sid="${param.sid}";//sid全局变量
			var pageTag = 'selfCenter';
			var EASYWIN = {
					"userInfo":{
						"id":"${userInfo.id}",
						"name":"${userInfo.userName}"
					},"sid":"${param.sid}"
			}
			$(document).ready(function() {
				//部门选择
			    $("#depChooseBtn").on("click",function(){
					depOne('depId','depName','',sid,'yes');
				});
				
				if("${param.activityMenu}"=="self_m_3.2"){
					//头像替换页面
					loadUpfilesForHead('${param.sid}','userImgPicker','userImgList','userImgFile','headImg',0);
				}else if("${param.activityMenu}"=="self_m_3.1"){
					//个人信息编辑页面，直属上级初始化
				    if(!strIsNull($("#leaderJson").val())){
						var users = eval("("+$("#leaderJson").val()+")");
						  var img="";
							for (var i=0, l=users.length; i<l; i++) {
								//数据保持
								$("#listUserInfo_id").append('<option selected="selected" value='+users[i].userID+'>'+users[i].userName+'</option>');
								//参与人显示构建
								img = img + "<div class=\"ticket-user pull-left other-user-box\" " +
								"style=\"cursor:pointer;\" id=\"user_img_listUserInfo_id_"+users[i].userID+"\" " +
								"ondblclick=\"removeClickUser('listUserInfo_id',"+users[i].userID+")\" title=\"双击移除\">";
								img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" " +
								"class=\"user-avatar\"/>"
								img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
								img = img + "</div>"
							}
							$("#userLeader_div").html(img);
					}
					//上级人员选择
					$("#userLearChooseBtn").on("click",function(){
						userOne("none", "none", "", '${param.sid}',function(options){
							 var userIds =new Array();
							 var leaders = "";
							 if(options.length>0){
								 $.each(options,function(i,vo){
									 userIds.push($(vo).val());
									 leaders += '\n<input type="hidden" name="listUserInfo['
											+ i + '].id" value="' + $(vo).val() + '">';
									leaders += '\n<input type="hidden" name="listUserInfo['
											+ i + '].userName" value="' + $(vo).text()
											+ '">';
								 })
							 }
							$("#leaderTempDiv").html(leaders);
							updateUserAttr("leader", "old", this,function(data){
								if(data.isSucc=="yes"){
									window.top.layer.msg(data.msg, {icon:2});
									if(userIds.length>0){
										 //ajax获取用户信息
										  $.post("/userInfo/selectedUsersInfo?sid=${param.sid}", { Action: "post", userIds:userIds.toString()},     
												  function (users){
											  var img="";
											  for (var i=0, l=users.length; i<l; i++) {
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
											  window.top.layer.msg(data.msg, {icon:1});
										  }, "json");
									 }else{
										 $("#userLeader_div").html("");
										 window.top.layer.msg(data.msg, {icon:1});
									 }
								}else if(data.isSucc=="no"){
									window.top.layer.msg(data.msg, {icon:2});
								}else if(data.isSucc=="noChanged"){
									//属性没变更
								}
								
							});
						})
					});
					
					var isChief = '${userInfo.isChief}';
					if(isChief=='1'){//不需要设定上级
						$("#needLeaerDiv").hide();
						$("#leaderSetDiv").find(":radio[name=\"isChief\"][value=\"1\"]").attr("checked",true);
					}else{
						$("#needLeaerDiv").show();
						$("#leaderSetDiv").find(":radio[name=\"isChief\"][value=\"0\"]").attr("checked",true);
					}
					
					$("#leaderSetDiv").find(":radio[name=\"isChief\"]").on("click",function(){
						var isChief = $(this).val();
						if(isChief=='1'){//不需要设定上级
							$("#needLeaerDiv").hide();
						
							var selectHtml = $("#listUserInfo_id").html();
							var tempHtml = $("#leaderTempDiv").html();
							var userLeader_div = $("#userLeader_div").html();
							
							$("#listUserInfo_id").html("");
							$("#leaderTempDiv").html("");
							$("#userLeader_div").html("");
							
							updateUserAttr("leader", "old", this,function(data){
								if(data.isSucc=="yes"){
									window.top.layer.msg(data.msg, {icon:1});
								}else if(data.isSucc=="no"){
									
									$("#needLeaerDiv").show();
									
									$("#leaderSetDiv").find(":radio[name=\"isChief\"][value=\"0\"]").attr("checked",true);
									
									$("#listUserInfo_id").html(selectHtml);
									$("#leaderTempDiv").html(tempHtml);
									$("#userLeader_div").html(userLeader_div);
									
									window.top.layer.msg(data.msg, {icon:2});
								
								}else if(data.isSucc=="noChanged"){
									//属性没变更
								}
							});
						
						}else if(isChief=='0'){//需要设定上级
							$("#needLeaerDiv").show();
							
							updateUserAttr("leader", "old", this,function(data){
								if(data.isSucc=="yes"){
									window.top.layer.msg(data.msg, {icon:1});
								}else if(data.isSucc=="no"){
									
									$("#needLeaerDiv").hide();
									
									$("#leaderSetDiv").find(":radio[name=\"isChief\"][value=\"1\"]").attr("checked",true);
									
									window.top.layer.msg(data.msg, {icon:2});
								
								}else if(data.isSucc=="noChanged"){
									//属性没变更
								}
							});
						}
					});
					
					//修改人事档案
					$("body").on("click",".updateRsdaBase",function(){
						var userId = $(this).attr("userId");
						var url = "/rsdaBase/updateRsdaBasePage?sid=${param.sid}&userId="+userId;
						openWinByRight(url);
					})
					//查看人事档案
					$("body").on("click",".viewRsdaBase",function(){
						var userId = $(this).attr("userId");
						var url = "/rsdaBase/viewRsdaBaseByUserId?sid=${param.sid}&userId="+userId;
						openWinByRight(url);
					})
				}
			});
//设置部门
function depOneCallBack(orgIdElementId, orgPathNameElementId,orgId,orgName){
	var preDepId = parseInt($("#"+orgIdElementId).val())
	if(parseInt(orgId)!=preDepId){
		$.post("/userInfo/updateUserDep?sid=${param.sid}",{Action:"post",depId:orgId,depName:orgName},     
			function (data){
			if(data.status=='y'){
				$("#"+orgIdElementId).val(orgId)
				$("#"+orgPathNameElementId).val(orgName);
				showNotification(1,"设置成功");
			}else{
				showNotification(2,data.info);
			}
		},"json");
	}else{
		showNotification(2,"选择的是相同的部门");
	}
}
//推出团队
function userOneCallBack(){
	var userId = $("#handoverUserId").val();
	var userName = $("#handoverUserName").val();
	var id = '${userInfo.id}';
	if(userId){
		if(userId==id){
			showNotification(2,"不能移交给自己!请重新选择");
		}else{
			layer.confirm("确定交接给"+userName+"?", {icon: 3, title:'工作交接对话框'}, function(index){
			  $.ajax({
				  type : "POST",
				  url : "/userInfo/dimission?sid=${param.sid}&rnd=" + Math.random(),
				  dataType:"json",
				  data:{userId:userId,userName:userName},
				  success:function(data){
					  layer.close(index);
					  if(data.status=='y'){
						  tabIndex = layer.open({
							  type: 0,
							  icon:6,
							  title: ['信息', 'font-size:18px;'],
							  maxmin: false,
							  content: "感谢你为公司的付出！",
							  btn: ['确定'],
							  yes: function(index, layero){
								  layer.close(index);
								  window.parent.location.href="/login.jsp";
							  },cancel:function(index){
								  layer.close(index);
								  window.parent.location.href="/login.jsp";
							  }
					      });
						  
						}else{
							showNotification(2,data.info);
						}
				  },
				  error:function(){
					  showNotification(2,"系统错误，请联系系统管理员！");
				  }
				});
			});
		}
	}
}
		</script>
	</head>
	<body>
		<input type="hidden" id="handoverUserId"/>
		<input type="hidden" id="handoverUserName"/>
		<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="selfCenter_left.jsp"></jsp:include>
				<!--显示个人分享信息-->
				<c:if test="${empty param.activityMenu || param.activityMenu=='self_m_1.1'}">
					<jsp:include page="query/listInfoAboutSelf.jsp"></jsp:include>
				</c:if>
				<!--显示个人操作日志-->
				<c:if test="${param.activityMenu=='self_m_1.3'}">
					<jsp:include page="/WEB-INF/jsp/systemLog/listPagedSelfSysLog.jsp"></jsp:include>
				</c:if>
				<!--显示个人操作日志-->
				<c:if test="${param.activityMenu=='self_m_1.10'}">
					<jsp:include page="/WEB-INF/jsp/bus_remind/listPagedSelfBusRemind.jsp"></jsp:include>
				</c:if>
				<%-- <!--显示个人出差记录-->
				<c:if test="${param.activityMenu=='self_m_1.4'}">
					<jsp:include page="/WEB-INF/jsp/financial/loanApply/listLoanApply.jsp"></jsp:include>
				</c:if>
				<!--显示个人出差记录-->
				<c:if test="${param.activityMenu=='self_m_1.9'}">
					<jsp:include page="/WEB-INF/jsp/financial/loanApply/listLoanDaylyApply.jsp"></jsp:include>
				</c:if>
				<!--显示个人借款记录-->
				<c:if test="${param.activityMenu=='self_m_1.5'}">
					<jsp:include page="/WEB-INF/jsp/financial/loan/listLoan.jsp"></jsp:include>
				</c:if>
				<!--显示个人报销记录-->
				<c:if test="${param.activityMenu=='self_m_1.6'}">
					<jsp:include page="/WEB-INF/jsp/financial/loanOff/listLoanOff.jsp"></jsp:include>
				</c:if> --%>

				<!--显示个人信息配置-->
				<c:if test="${param.activityMenu=='self_m_3.1'}">
					<jsp:include page="edit/editUserInfo.jsp"></jsp:include>
				</c:if>
				<!--个人头像设置-->
				<c:if test="${param.activityMenu=='self_m_3.2'}">
					<jsp:include page="edit/editUserHeadImg.jsp"></jsp:include>
				</c:if>
				<!--开关配置-->
				<c:if test="${param.activityMenu=='self_m_3.3'}">
					<jsp:include page="edit/msgShowSetting.jsp"></jsp:include>
				</c:if>
				<!--常用意见-->
				<c:if test="${param.activityMenu=='self_m_3.4'}">
					<jsp:include page="/WEB-INF/jsp/usagidea/listPagedUsagIdea.jsp"></jsp:include>
				</c:if>
				<!--邮件配置-->
				<c:if test="${param.activityMenu=='self_m_3.5'}">
					<jsp:include page="/WEB-INF/jsp/mail/listMailSet.jsp"></jsp:include>
				</c:if>
				<c:if test="${param.activityMenu=='self_m_3.5.1'}">
					<jsp:include page="/WEB-INF/jsp/mail/addMailSet.jsp"></jsp:include>
				</c:if>
				<c:if test="${param.activityMenu=='self_m_3.5.2'}">
					<jsp:include page="/WEB-INF/jsp/mail/updateMailSet.jsp"></jsp:include>
				</c:if>
				<!--我的群组-->
				<c:if test="${param.activityMenu=='self_m_3.6'}">
					<jsp:include page="/WEB-INF/jsp/selfGroup/listUserGroup.jsp"></jsp:include>
				</c:if>
				<c:if test="${param.activityMenu=='self_m_3.6.1'}">
					<jsp:include page="/WEB-INF/jsp/selfGroup/addUserGroup.jsp"></jsp:include>
				</c:if>
				<c:if test="${param.activityMenu=='self_m_3.6.2'}">
					<jsp:include page="/WEB-INF/jsp/selfGroup/updateUserGroup.jsp"></jsp:include>
				</c:if>
				
				<!--外部联系人-->
				<c:if test="${param.activityMenu=='self_m_3.7'}">
					<jsp:include page="/WEB-INF/jsp/outLinkMan/listPageOutLinkMan.jsp"></jsp:include>
				</c:if>
				<!--个人关注-->
				<c:if test="${param.activityMenu=='self_m_3.8'}">
					<jsp:include page="/WEB-INF/jsp/personAttention/listPersonAttention.jsp"></jsp:include>
				</c:if>
				<!--收件箱-->
				<c:if test="${param.activityMenu=='self_m_4.1'}">
					<jsp:include page="/WEB-INF/jsp/mail/listPagedReceiveMail.jsp"></jsp:include>
				</c:if>
				<!--已发邮件-->
				<c:if test="${param.activityMenu=='self_m_4.2'}">
					<jsp:include page="/WEB-INF/jsp/mail/listPagedSendMail.jsp"></jsp:include>
				</c:if>
	        </div>
	    </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	</body>
	
	<c:if test="${param.activityMenu=='self_m_1.1'}">
			<!--分享信息拼接-->
			<script type="text/javascript" src="/static/js/selfShareCode.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		<c:if test="${param.activityMenu=='self_m_3.2'}">
			<!--头像替换添加-->
			<script type="text/javascript" src="/static/js/selfCenterJs/upload.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			<script type="text/javascript">
				var loadImgIndex;
			</script>
		</c:if>
		<c:if test="${param.activityMenu=='self_m_3.4'}">
			<!--常用意见维护JS-->
			<script type="text/javascript" src="/static/js/usagIdeaCenterJs/usagIdeaCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		<c:if test="${fn:contains(param.activityMenu,'self_m_3.5')}">
			<!--邮件配置的JS-->
			<script type="text/javascript" src="/static/js/mailSetCenterJs/mailSetCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		<c:if test="${fn:contains(param.activityMenu,'self_m_3.6')}">
			<!--我的组群JS-->
			<script type="text/javascript" src="/static/js/selfGroupCenterJs/selfGroupCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		
		<c:if test="${param.activityMenu=='self_m_3.7'}">
			<!--外部联系人JS-->
			<script type="text/javascript" src="/static/js/outLinkManJs/outLinkMan.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		
		<c:if test="${param.activityMenu eq 'self_m_1.4' or param.activityMenu eq 'self_m_1.5' 
			or param.activityMenu eq 'self_m_1.6'or param.activityMenu eq 'self_m_1.7'or param.activityMenu eq 'self_m_1.8' or param.activityMenu eq 'self_m_1.9'}">
			<!-- 审批相关JS -->
			<script type="text/javascript" charset="utf-8" src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
			<!-- 财务模块JS -->
			<script type="text/javascript" charset="utf-8" src="/static/js/financial/financial.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		<c:if test="${param.activityMenu=='self_m_1.4' or param.activityMenu=='self_m_1.9'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/financial/loanApplyCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		<c:if test="${param.activityMenu=='self_m_1.5'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/financial/loanCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		<c:if test="${param.activityMenu=='self_m_1.6'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/financial/loanOffCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
</html>

