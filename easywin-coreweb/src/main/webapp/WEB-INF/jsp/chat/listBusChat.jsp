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
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript">
function toChat(busId,busType,roomId){
	var url = "/chat/toChat?sid=${param.sid}&busId="+busId+"&busType="+busType+"&id="+roomId;
	var title ="聊天室";
	if(busType=='1'){
		title = "[信息分享]"+title;
	}else if(busType=='012'){
		title = "[客户]"+title;
	}else if(busType=='005'){
		title = "[项目]"+title;
	}else if(busType=='003'){
		title = "[任务]"+title;
	}else if(busType=='004'){
		title = "[投票]"+title;
	}else if(busType=='006'){
		title = "[周报]"+title;
	}else if(busType=='050'){
        title = "[分享]"+title;
    }else if(busType=='011'){
		title = "[问答]"+title;
	}
	art.dialog.open(url,{
		title:title,
		height:460,
		width:650,
		lock:true,
		fixed:true,
		close:function(){
			var iframe = this.iframe.contentWindow;
			iframe.closeChat();
			window.self.location.reload();
          }
		}
	)
}
</script>
<script> 
	$(function() { 
		 //页面刷新
		$("#refreshImg").click(function(){
			window.self.location.reload();
		});
	}); 
</script>
</head>
<body onload="resizeVoteH('${param.ifreamTag}')" style="background-color:#FFFFFF;">
<div style="clear: both"> </div>
		<table class="table table-hover general-table">
			<thead>
				<tr>
					<th width="8%" valign="middle">序号</th>
					<th width="25%" valign="middle">创建时间</th>
					<th width="15%" valign="middle">房间号</th>
					<th width="15%" valign="middle">未读消息数</th>
					<th width="15%" valign="middle">成员</th>
					<th valign="middle">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
				 	<c:when test="${not empty list}">
				 		<c:forEach items="${list}" var="chatRoom" varStatus="status">
				 			<tr>
				 				<td height="40">${ status.count}</td>
				 				<td>${chatRoom.recordCreateTime}</td>
				 				<td align="left">
				 					聊天室${chatRoom.id}
				 				</td>
				 				<td style="text-align:left;">
				 				${chatRoom.noReadNum}
				 				</td>
				 				<c:set var="chatUsers" value="${chatRoom.chaters}"></c:set>
				 				<td height="30" style="font-size: 12px">
					 				<c:forEach items="${chatUsers}" var="chatUser">
					 					${chatUser.userName}</br>
					 				</c:forEach>
					 			</td>
					 			<td>
					 				<a href="javascript:void(0)" onclick="toChat('${chatRoom.busId}','${chatRoom.busType}','${chatRoom.id}')">继续聊天</a>
					 			</td>
				 			</tr>
				 		</c:forEach>
				 	</c:when>
				 	<c:otherwise>
				 		<tr>
				 			<td height="30" colspan="7" align="center"><h3>没有创建聊天室！</h3></td>
				 		</tr>
				 	</c:otherwise>
				 </c:choose>
			</tbody>
		</table>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>