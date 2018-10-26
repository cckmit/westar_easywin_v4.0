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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript">
var sid = "${param.sid}";
$(function(){
	$("body").on("click",".cancelHuiQian",function(){
		var actTaskId = $(this).attr("actTaskId");
		var huiQianId = $(this).attr("huiQianId");
		window.top.layer.confirm("确定撤回会签?", {icon: 3, title:'确认对话框'}, function(index){
			window.top.layer.close(index);
			$.post("/workFlow/cancelHuiQian?sid=${param.sid}",{Action:"post",actTaskId:actTaskId,huiQianId:huiQianId},     
					function (data){
					if(data.status=='y'){
						showNotification(1,data.info);
						parent.location.reload();
					}else{
						showNotification(2,data.info);
					}
			},"json");
		});
	});
});
</script>
</head>
<body onload="resizeVoteH('${param.ifreamName}');" style="background-color:#FFFFFF;">
<div class="widget-body">
     <c:choose>
	 	<c:when test="${not empty listSpFlowHuiQianInfo}">
    	<table class="table table-striped table-hover general-table">
         	<thead>
            	<tr>
                    <th width="8%" valign="middle">序号</th>
                    <th valign="middle">会签人</th>
					<th valign="middle" style="width:160px;">推送时间</th>
					<th valign="middle" style="width:100px;">操作</th>
                </tr>
           </thead>
           <tbody>
           	<c:choose>
			 	<c:when test="${not empty listSpFlowHuiQianInfo}">
			 		<c:forEach items="${listSpFlowHuiQianInfo}" var="vo" varStatus="status">
			           	<tr>
			            	<td>
			            		<label class="optRowNum" style="display: block;width: 20px">${status.count}</label>
			            	</td>
			            	<td class="hidden-phone">
				            	<div class="ticket-user pull-left other-user-box">
									<img class="user-avatar" alt="${vo.userName}"
									src="/downLoad/userImg/${userInfo.comId }/${vo.assignee }"/>
									<i class="user-name">${vo.userName}</i>
								</div>
			                </td>
			               	 <td>${vo.recordCreateTime}</td>
			               	 <td>
			               	 	<c:choose>
			               	 		<c:when test="${not empty SpFlowCurExecutor and SpFlowCurExecutor.executor eq userInfo.id}">
					               	 	<a href="javascript:void(0);" class="red cancelHuiQian" actTaskId="${vo.actTaskId}" huiQianId="${vo.id}">撤签</a>
			               	 		</c:when>
			               	 		<c:otherwise><span class="red">待反馈</span></c:otherwise>
			               	 	</c:choose>
			               	 </td>
			             </tr>
			 		</c:forEach>
			 	</c:when>
			 </c:choose>
        	</tbody>
    	</table>
	 	</c:when>
	 	<c:otherwise>
	 		<div style="text-align:center;font-size:18px;">无待会签记录！</div>
	 	</c:otherwise>
	 </c:choose>
	</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
