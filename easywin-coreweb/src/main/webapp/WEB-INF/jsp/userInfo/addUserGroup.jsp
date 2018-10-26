<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
	$('.subform').Validform({
		tiptype : function(msg, o, cssctl) {
			validMsgV2(msg, o, cssctl);
		},
		showAllError : true
	});
	//用户失焦筛选
	$("#condition").blur(function(){
		$("#userList").submit()
	});
	//文本框绑定回车提交事件
	$("#condition").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	$("#userList").submit()
        }
    });
});
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
	})
	
	//添加部门成员
	function appendUser(checked,str){
	  var json = eval('(' + str + ')'); 
	  if(checked==true){
	   if($("#listUser_id option[value='"+json.id+"']").length==0){
		   	if(null!=json.name&&""!=json.name){
			     $('#listUser_id').append("<option value='"+json.id+"'>"+json.name+"</option>");
		   	}else{
			     $('#listUser_id').append("<option value='"+json.id+"'>"+json.email+"</option>");
		   	}
	   }
	  }else{
	   var selectobj = document.getElementById("listUser_id");
	   for (var i = 0; i < selectobj.options.length; i++) {
			  if(selectobj.options[i].value==json.id){
			    selectobj.options[i] = null;
			    break;
			  }
	   }
	  }
	}
	//移除所选部门成员
	 function removeOne(ts){
	  var depid="";
	  $('#listUser_id').find("option:selected").each(function(index){
		  allUser.uncheck($(this).attr("value"));
	  });
	  removeClick(ts.id);
	}
	function tjGrpForm(){
		 var selectobj = document.getElementById("listUser_id");
		 var grpName = $("#grpName").val();
		 if(strIsNull(grpName)){
			 art.dialog({"content":"请填写组群名称"}).time(2);
			 $("#grpName").focus()
			 return false;
		}
		 if(selectobj.options.length==0){
			 art.dialog({"content":"分组成员未选择"}).time(2);
		 }else{
			$('#groupForm').submit();
		 }
	} 

</script>
</head>
<body style="background-color: #fff" onload="resizeVoteH('userCenter');">
<div class="panel">
<form action="/userInfo/listPagedUserForGrp" method="post" target="allUser" id="userList">
 <input type="hidden" name="sid" value="${param.sid}"/>
<header class="panel-heading">添加组群
<div class="pull-right ws-search-box">
	<input type="text" class="form-control" name="condition" id="condition" value="${userInfo.condition}" placeholder="用户检索……"/>
	<a href="#" class="fa  fa-search search-btn"></a>
</div>
</header>
</form>
<form class="subform" method="post" action="/userInfo/addGroup" id="groupForm">
<input type="hidden" name="redirectPage" value="${param.redirectPage}" />
<tags:token></tags:token>
<div class="panel-body">
	<div class="ws-introduce2">
		<span><b class="ws-color">*</b>群组名称：</span>
		<div class="ws-form-group">
			<div style="float: left">
				<input class="colorpicker-default form-control pull-left" type="text" id="grpName" datatype="*" name="grpName" nullmsg="请填群组名称" value=""/>
				<div style="clear: both">
				</div>
			</div>
			<div class="ws-share">
				<button type="submit" class="btn btn-info ws-btnBlue pull-left">添加</button>
			</div>
		</div>
		<div class="ws-clear"></div>
	</div>
</div>
<div style="display:none">
<%--人员选择 --%>
<select list="listUser" listkey="id" listvalue="listUser.name" id="listUser_id" name="listUser.id" ondblclick="removeOne(this)" multiple="multiple" moreselect="true">
	<option value="${userInfo.id }" selected="selected">${userInfo.userName}</option>
</select>
</div>
</form>
<div class="panel-body">
<iframe style="width:100%;" id="allUser" name="allUser"
						src="/userInfo/listPagedUserForGrp?sid=${param.sid }&grpId=-1"
						border="0" frameborder="0" allowTransparency="true"
						noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
</div>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>	
</body>
</html>
