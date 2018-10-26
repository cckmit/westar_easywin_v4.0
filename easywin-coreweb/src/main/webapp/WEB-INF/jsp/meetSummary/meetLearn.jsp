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
<title><%=SystemStrConstant.TITLE_NAME%></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
$(function() {
	//显示会议学习人员
	if(!strIsNull($("#sharerJson").val())){
		var users = eval("("+$("#sharerJson").val()+")");
		  var img="";
		  var editCrm = '${editCrm}';
		  
		  if(strIsNull(editCrm)){
			  for (var i=0, l=users.length; i<l; i++) {
					//数据保持
					$("#listCustomerSharer_userId").append('<option selected="selected" value='+users[i].userID+'>'+users[i].userName+'</option>');
						img = img + "<div class=\"online-list margin-top-5 margin-left-5 margin-bottom-5\" style=\"float:left\" id=\"user_img_listCustomerSharer_userId_"+users[i].userID+"\" ondblclick=\"removeClickUser('listCustomerSharer_userId',"+users[i].userID+")\" title=\"双击移除\">";
						img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
						img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
						img = img + "</div>"
				}
			}else{
			  for (var i=0, l=users.length; i<l; i++) {
						img = img + "<div class=\"online-list\" style=\"float:left\">";
						img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
						img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
						img = img + "</div>"
				}
				
			}
			$("#crmSharor_div").html(img);
	}
});

//删除参与人
function removeClickUser(id,selectedUserId) {
	var selectobj = document.getElementById(id);
	for ( var i = 0; i < selectobj.options.length; i++) {
		if (selectobj.options[i].value==selectedUserId) {
			selectobj.options[i] = null;
			break;
		}
	}
	$("#user_img_"+id+"_"+selectedUserId).remove();
	$.post("/meetSummary/delMeetLearn?sid=${param.sid}",{Action:"post",meetingId:${meeting.id},userId:selectedUserId},     
			function (msgObjs){
			showNotification(1,msgObjs);
	});
	selected(id);
	resizeVoteH('otherAttrIframe');
}

//会议学习人员更新
function userMoreCallBack(){
	 var userIds =new Array();
	$("#listCustomerSharer_userId option").each(function() { 
		userIds.push($(this).val()); 
    });
	if(!strIsNull(userIds.toString())){
		$.post("/meetSummary/meetLearnUpdate?sid=${param.sid}",{Action:"post",meetingId:${meeting.id},userIds:userIds.toString()},     
			function (msgObjs){
			showNotification(1,msgObjs);
		});
	}
}

//选择会议学习人员
function choose() {
	userMore('listCustomerSharer_userId','yes','${param.sid}','yes','crmSharor_div',function(options){
	  
	  removeOptions('listCustomerSharer_userId');
	  var userIds =new Array();
	  for ( var i = 0; i < options.length; i++) {
		  userIds.push(options[i].value);
		  $('#' + 'listCustomerSharer_userId').append(
				  "<option selected='selected' value='"
				  + options[i].value + "'>" + options[i].text
				  + "</option>");
	  }
	  $('#' + 'listCustomerSharer_userId').focus();
	  $('#' + 'listCustomerSharer_userId').blur();
		
		if(options.length>0){
			  //ajax获取用户信息
			  $.post("/userInfo/selectedUsersInfo?sid="+'${param.sid}', { Action: "post", userIds:userIds.toString()},     
					  function (users){
				  var img="";
				  for (var i=0, l=users.length; i<l; i++) {
					  img = img + "<div class=\"online-list margin-left-5 margin-bottom-5\" " +
					  "style=\"float:left;cursor:pointer;\" id=\"user_img_"+'listCustomerSharer_userId'+"_"+users[i].id+"\" " +
					  "ondblclick=\"removeClickUser('"+'listCustomerSharer_userId'+"',"+users[i].id+")\" title=\"双击移除\">";
					  img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].id+"?sid="+'${param.sid}'+"\" " +
					  "class=\"user-avatar\"/>"
					  img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
					  img = img + "</div>"
				  }
				  $("#"+'crmSharor_div').html(img);
				  resizeVoteH('otherAttrIframe');
			  }, "json");
		  }else{
			  $("#"+'crmSharor_div').html('');
			  resizeVoteH('otherAttrIframe');
		  }
		
		 userMoreCallBack();
		
		});
}
</script>
</head>
<body onload="resizeVoteH('${param.ifreamName}');">
	<div class="widget-body">
		<li class="ticket-item no-shadow autoHeight no-padding">
			<div class="pull-left gray ps-left-text padding-top-10">&nbsp;指定人员</div>
			<div class="ticket-user pull-left ps-right-box">
				<div class="pull-left gray ps-left-text padding-top-10">
					<input type="hidden" id="sharerJson" name="sharerJson" value="${meeting.sharerJson}" />
					<div style="float: left; width: 250px;display:none;">
						<select list="listCustomerSharer" listkey="userId" listvalue="userName" id="listCustomerSharer_userId" name="listCustomerSharer.userId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
						</select>
					</div>
					<div id="crmSharor_div" class="pull-left" style="max-width:460px"></div>
					<%-- <tags:userMore name="listCustomerSharer.userId" showName="userName" disDivKey="crmSharor_div" callBackStart="yes" ></tags:userMore> --%>
					
					<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5" title="人员多选"
					 onclick="choose()" style="float: left;"><i class="fa fa-plus"></i>选择</a>
				</div>
			</div>
			<div class="ps-clear"></div>
		</li>
	</div>	
		
			
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
    
    
    <jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
