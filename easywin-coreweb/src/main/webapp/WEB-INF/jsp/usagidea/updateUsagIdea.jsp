<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.BiaoQing"%>
<%@page import="com.westar.core.web.BiaoQingContext"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@page import="com.westar.base.util.DateTimeUtil"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script> 
var vali;
$(function() {
	$("#idea").autoTextarea({minHeight:80,maxHeight:80});
	//设置滚动条高度
	var height = $(window).height();
	$("#contentBody").css("height",height+"px");
	
});

//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
		
/**
 * 添加附件验证
 */
function updateIdeaForm(){
	var value = $('#idea').val();
	if(strIsNull(value)){
		layer.tips('请编辑意见内容！', "#idea", {tips: 1});
		return false;
	}
	var len = charLength(value.replace(/\s+/g,""));
	if(len%2==1){
		len = (len+1)/2;
	}else{
		len = len/2;
	}
	if(len>30){
		layer.tips('意见内容不要超过30', "#idea", {tips: 1});
       return false;
	}
	if($("#subState").val()==1){
		return false;
	}
	var flag  = false;
	 //异步提交表单
   $("#ideaForm").ajaxSubmit({
       type:"post",
       url:"/usagIdea/ajaxUpdateUsagIdea?sid=${param.sid}&t="+Math.random(),
       dataType: "json",
       async:false,
       beforeSubmit:function (a,f,o){
       	$("#subState").val(1)
		}, 
       success:function(data){
	         if(data.status=='y'){
	        	 flag = true;
	         }
       },error:function(XmlHttpRequest,textStatus,errorThrown){
       	window.top.layer.alert("系统错误，请联系管理人员",{icon:7,title:false,closeBtn:0,btn:["关闭"]})
       	return false;
       	
       }
   });
	 return flag;
	
	
  
}
</script>
</head>
<body onload="handleTitle()" >

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">修改常用意见</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
				
					<input type="hidden" id="subState" value="0">
					<form id="ideaForm" action="/usagIdea/ajaxUpdateUsagIdea" method="post">
					<input type="hidden" name="id" value="${usagIdea.id}"/>
					<input type="hidden" name="sid" value="${param.sid}"/>
					
					<div class="widget-body no-shadow" id="contentBody" style="overflow: hidden;">
						<textarea class="form-control"   datatype="*"  id="idea" name="idea" style="overflow-y:hidden;width:98%;height:80px;color: #000"
						 	  onpropertychange="handleTitle()" onkeyup="handleTitle()">${usagIdea.idea}</textarea>
						 	  <div id="msgTitle" style="clear:both;float:right;padding-right: 20px">0/30</div> 
								<script> 
									//当状态改变的时候执行的函数 
									function handleTitle(){
										var value = $('#idea').val();
										var len = charLength(value.replace(/\s+/g,""));
										if(len%2==1){
											len = (len+1)/2;
										}else{
											len = len/2;
										}
										if(len>30){
											$('#msgTitle').html("<span style='color: red'>"+len+"</font>/30");
										}else{
											$('#msgTitle').html(len+"/30");
										}
									} 
									//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
									if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
										document.getElementById('idea').onpropertychange=handleTitle 
									}else {//非ie浏览器，比如Firefox 
										document.getElementById('idea').addEventListener("input",handleTitle,false); 
									} 
								</script> 
					</div>
					</form>

				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
