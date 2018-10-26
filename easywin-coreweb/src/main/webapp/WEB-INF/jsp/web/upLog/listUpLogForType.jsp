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
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<style type="text/css">
body {
	background: #fff;
}

.sysUpLog {
	background: url(../../../static/images/web/line04.gif) repeat-y 187px
		0;
	overflow: hidden;
	position: relative;
	left: 50px;
	min-height:600px;
}

.up-date {
	overflow: hidden;
	position: relative;
}

.up-date h2 {
	height: 59px;
	font-size: 25px;
	font-family: 微软雅黑;
	font-weight: normal;
	padding-left: 45px;
	margin-bottom: 50px;
}

.up-date h2.first {
	position: absolute;
	left: 0;
	top: -20px;
	width: 800px;
	z-index: 99;
	background: url(../../../static/images/web/icon06.gif) no-repeat
		158px 0;
}

.up-date h2 a {
	color: #00bbff;
	display: inline-block;
	*display: inline;
	zoom: 1;
	background: url(../../../static/images/web/icon08.gif) no-repeat
		right 50%;
	padding-right: 17px;
	margin: 21px 97px 0 0;
}

.up-date h2 a:hover {
	text-decoration: none;
}

.up-date h2 img {
	vertical-align: -5px;
}

.up-date ul {
	margin-top: 100px;
}

.up-date ul li {
	background: url(../../../static/images/web/icon07.gif) no-repeat
		180px 0;
	padding-bottom: 10px;
	zoom: 1;
}

.up-date ul li:after {
	content: " ";
	display: block;
	height: 0;
	clear: both;
	visibility: hidden;
}

.up-date ul li h3 {
	float: left;
	width: 168px;
	text-align: right;
	padding-right: 19px;
	color: #1db702;
	font: normal 18px/16px Arial;
	-webkit-margin-before:0.2em; -webkit-margin-after: 0em; -webkit-margin-start: 0px;-webkit-margin-end: 0px;
}

.up-date ul li h3 span {
	display: block;
	color: #a8dda3;
	font-size: 12px;
}

.up-date ul li dl {
	float: left;
	padding-left: 41px;
	font-family: 微软雅黑;
}

.up-date ul li dl dt {
	font: 30px 微软雅黑;
	color: #737373;
	line-height: 28px;
}

.up-date ul li dl dt span {
	display: block;
	color: #787878;
	font-size: 12px;
}

.up-date ul li dl dd {
	line-height: 21px;
	color: #787878;
}
</style>

<script type="text/javascript">
$(function(){
	var finished = true;
	$(window.parent).scroll(function(){  
	    // 当滚动到最底部以上50像素时， 加载新内容  
	    if (finished && ($(window.parent.document).height() - $(this).scrollTop() - $(this).height()<100)) {
	    	finished = false;
	    	loadMore();  
	    }
	    
	});
	
	function loadMore(){
		 var maxId = $("#listUpLog").find("input[type='hidden'][name='id']").val();
		 var type = $("#listUpLog").find("input[type='hidden'][name='terracrType']").val();
		  
		 $.ajax({
	     	type:"POST",
	         url:"/web/listNextSysUpLog?t="+Math.random(),
	         data:{maxId:maxId,type:type},
	         dataType:"json",
	         success:function(data){
	       		  var upLog = data.listUpLog;
				 for(var i=0;i<upLog.length;i++){
					 var html = '';
					 html +='\n  <li class="green">';
					 html +='\n <h3>'+upLog[i].upgradeTime.substring(0,10)+'</h3>';
					 html +='\n <dl>';
					 html +='\n <dt>'+upLog[i].versionName+'</dt>';
					 html +='\n <dd class="margin-top-10">'+upLog[i].content+'</dd>';
					 html +='\n </dl></li>';
					 $("#listUpLog").append(html); 
					  if(i==(upLog.length-1)){
						 $("#listUpLog").find("input[type='hidden'][name='id']").val(upLog[i].id);
					 } 
				} 
				 resizeVoteH('otherLogAttrIframe');
				 if(data.size()<5){//数据已调取完
					 finished = false;
				 }else{
					 finished = true;
				 }
	         },
	         error:function(error){
	         }
	     });
	 }
});
 </script>
</head>
<body onload="resizeVoteH('otherLogAttrIframe')" style="background: #fff;">
	<!--main content start-->
	<section id="main-content" style="width:100%;margin-left:0px;background: #fff;">
		<section class="wrapper"
			style="padding:15px;margin-top: 0px;display:inline-block;width:100%">
			<!-- page start-->
			<c:choose>
				<c:when test="${not empty listUpLog}">
					<div class="sysUpLog">
						<div class="up-date">
							<h2 class="first"></h2>
							<ul id="listUpLog">
								<c:forEach items="${listUpLog}" var="upLog" varStatus="obj">
									<li class="green">
										<h3>${fn:substring(upLog.upgradeTime,0,10)}</h3>
										<dl>
											<dt>${upLog.versionName}</dt>
											<dd class="margin-top-10">${upLog.content}</dd>
										</dl>
									</li>
									<c:if test="${obj.last }"> 
									<input type="hidden" name="terracrType" value="${upLog.terraceType}"/>
									<input type="hidden" name="id" value="${upLog.id}"/>
									</c:if>
								</c:forEach>
							</ul>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="container" style="left:50%;top:50%;
					padding-top:180px;text-align:center;width:500px;height:600px;">
					<section class="error-container text-center">
						<h1>
							<i class="fa fa-exclamation-triangle"></i>
						</h1>
						<div class="error-divider">
							<h2>暂无系统更新数据！</h2>
						</div>
					</section>
				</div>
				</c:otherwise>
			</c:choose>
			<!-- page end-->
		</section>
	</section>
	<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>

