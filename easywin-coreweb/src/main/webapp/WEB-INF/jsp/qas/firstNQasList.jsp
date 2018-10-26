<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
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
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script type="text/javascript"> 
$(function(){
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
});
//查看问题
function viewQues(id){
	parent.window.location.href="/qas/viewQuesPage?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
}
</script>
</head>
<body style="background-color: #fff" onload="resizeVoteH('knowledgeCenter');">
<div class="tab-pane active" style="margin-top:10px;">
	<ul class="lists" style="margin-left:15px;">
		<c:choose>
   			<c:when test="${not empty qasList}">
   				<c:forEach items="${qasList}" var="question" varStatus="vs">
   					<li style="list-style:none;">
   						<a href="javascript:void(0)" style="margin: 6px 6px;width: 25px;margin-bottom: 0px" class="fa ${question.attentionState==0?'fa-star-o':'fa-star ws-star'}" title="${question.attentionState==0?'标识关注':'取消关注'}" onclick="changeAtten('011',${question.id},${question.attentionState},'${param.sid}',this)"></a>
   						<a href="javascript:void(0);" onclick="viewQues(${question.id})" data-toggle="tab">
   							<tags:viewTextArea><tags:cutString num="60">${question.title} </tags:cutString></tags:viewTextArea>
						</a>
   						<span class="ws-name2">${question.userName}</span>
						<time class="ws-time">${fn:substring(question.recordCreateTime,0,16)}</time>
					</li>
			 </c:forEach>
  			</c:when>
  		</c:choose>
	</ul>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>

