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
<!--Basic Styles-->
<link href="/static/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="/static/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">

 <!--Beyond styles-->
<link href="/static/assets/css/beyond.min.css" rel="stylesheet"  type="text/css">
<link href="/static/assets/css/beyond_2.min.css" rel="stylesheet"  type="text/css">
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>

<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">

<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script> 
//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
var openAccount;
//注入父页面信息
function setWindow(winDoc,win,list,index,account){
	openWindowDoc = winDoc;
	openWindow = win;
	openPageTag = openWindow.pageTag;
	openTabIndex = index;
	openAccount = account;
	getBody(list);
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
}

	function getBody(list){
		var inviteNum = 0;
		for(var i=0;i<list.length;i++){
			var organic = list[i];
			if(organic.isSysUser==1){
				//判断当前用户在团队中的账号服务状态
				if(organic.inService==1){//正常服务状态
					var a = $('<a href="javascript:void(0)" class="btn-loginA"></a>');
					$(a).html('['+organic.orgNum+']'+organic.orgName);
					$(a).attr('comId',organic.orgNum);
					$("#orgUserList").append(a);
					$(a).on("click",function(){
						$("#comId",openWindowDoc).attr("value",$(this).attr('comId'))
						$("#isSysUser",openWindowDoc).attr("value","1")
						$("#loginForm",openWindowDoc).submit();
						window.top.layer.close(openTabIndex);
					})
				}else{//不在团队服务内
					var a = $('<a href="javascript:void(0)" class="btn-loginA"></a>');
					$(a).attr("title","此账号在本团队可以使用人数范围之外，请联系团队管理员。")
					$(a).css("background-color","rgba(0,0,0,.3)")
					$(a).css("color","#ffffff")
					$(a).html('['+organic.orgNum+']'+organic.orgName);
					$(a).append('<span style="color:red;">&nbsp;[登录受限]</span>');
					$(a).attr('comId',organic.orgNum);
					$("#orgUserList").append(a);
				}
			}else{
				var div = $("<div></div>")
				var aName = $('<a href="javascript:void(0)" class="btn-loginA pull-left"></a>');
				$(aName).css("background-color","rgba(0,0,0,.3)")
				$(aName).css("color","#ffffff")
				$(aName).css("width","255px");
				$(aName).css("cursor","default");
				$(aName).html('['+organic.orgNum+']'+organic.orgName);
				
				var aIN = $('<a href="javascript:void(0)" class="btn-loginA pull-left margin-left-5 margin-right-5"></a>');
				$(aIN).html('加入');
				$(aIN).css("background-color","rgba(40,160,1,.8)")
				$(aIN).css("width","60px")
				
				$(aIN).attr('comId',organic.orgNum);
				
				$(aIN).on("click",function(){
					$("#comId",openWindowDoc).attr("value",$(this).attr('comId'))
					$("#isSysUser",openWindowDoc).attr("value","0")
					$("#loginForm",openWindowDoc).submit();
					window.top.layer.close(openTabIndex);
				})
				
				var aOut = $('<a href="javascript:void(0)" class="btn-loginA pull-left rejectInOrg" comId="'+organic.orgNum+'"></a>');
				$(aOut).css("background-color","rgba(218,14,14,.7)")
				$(aOut).css("width","60px")
				$(aOut).html('拒绝');
				
				$(div).append(aName);
				$(div).append(aIN);
				$(div).append(aOut);
				$("#orgInvList").append(div);
				inviteNum++;
				
			}
		}
		
		if(inviteNum == 0){
			$("#orgInvList").parent().remove();
		}
	}
	
	$(document).on("click","#orgInvList .rejectInOrg",function(){
		var comId = $(this).attr("comId");
		var e=$(this);
		//ajax获取用户信息
		$.ajax({  
	        url : "/registe/rejectInOrg?t="+Math.random(),  
	        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
	        type : "POST",  
	        dataType : "json", 
	        data:{account:openAccount,comId:comId},
	        success : function(data) { 
	        	if(data.status=='y'){
					$(e).parent().remove();
					showNotification(1,"成功拒绝加入");
	        	}
	        },error:function(){
	        	showNotification(2,"操作失败,请联系管理人员");
	        }
	    });
		
	})
	
	function stop(){ 
		return false; 
	} 
	document.oncontextmenu=stop; 
</script>
<style type="text/css">
.dd-list li{
cursor: pointer;
}
.dd2-content{
	font-size: 15px
}

.btn-loginA{ width:100%; height: 40px; background:#00cfff; color:#fff; text-align: center; 
	line-height:40px; display: block; border-radius: 4px;margin-top: 5px}
.btn-loginA:hover{ background:#00bfff;}
.ps-layerTitle{
	font-size: 18px !important;
}
</style>
</head>
<body>

<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	<div class="widget-caption" style="text-align:center;width: 90%;">
		<span class="themeprimary ps-layerTitle">团队选择</span>
	</div>
	<div class="widget-buttons">
		<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
			<i class="fa fa-times themeprimary"></i>
		</a>
	</div>
</div>
<div id="contentBody" class="widget-body" style="overflow:hidden;overflow-y:auto;position: relative;">
     <div class="loginbox-submit no-padding-bottom" id="orgUserList">
     </div>
     <div class="loginbox-submit no-padding-top">
     	<div id="orgInvList">
     	</div>
     </div>
</div>
				
     
			            
</body>
</html>
