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
var sid = "${param.sid}"
//任务查看权限验证
function viewSpflow(instanceId){
	authCheck(instanceId,'022',-1,function(){
		var url = "/workFlow/viewSpFlow?sid=${param.sid}&instanceId=" + instanceId;
		var height = $(window.parent).height();
		openWinWithPams(url,"800px",(height-90)+"px");
	})
}
</script>
</head>
<body onload="resizeVoteH('${param.ifreamName}');" style="background-color:#FFFFFF;">
<div class="widget-body">
    	<table class="table table-striped table-hover general-table">
         	<thead>
            	<tr>
                    <th width="8%" valign="middle">序号</th>
					<th valign="middle" class="hidden-phone">流程名称</th>
					<th width="14%" valign="middle">流程进度</th>
					<th width="15%" valign="middle">发起人</th>
					<th width="14%" valign="middle">发起时间</th>
                </tr>
           </thead>
           <tbody>
           	<c:choose>
			 	<c:when test="${not empty pageBean.recordList}">
			 		<c:forEach items="${pageBean.recordList}" var="spflow" varStatus="status">
			           	<tr>
			            	<td>
			            		<label class="optRowNum" style="display: block;width: 20px">${status.count}</label>
			            	</td>
			            	<td class="hidden-phone">
				            	<a href="javascript:void(0);" onclick="viewSpflow(${spflow.id});" title="${spflow.flowName}">
			 						<tags:cutString num="24">${spflow.flowName}</tags:cutString>
			 					</a>
			                </td>
			                 <td>
		 						<c:choose>
		 							<c:when test="${spflow.flowState eq -1 }">驳回</c:when>
		 							<c:when test="${spflow.flowState eq 4 }">通过</c:when>
		 							<c:otherwise>
		 								审核中
		 							</c:otherwise>
		 						</c:choose>
			                </td>
			                <td>
			               	 	<div class="ticket-user pull-left other-user-box">
									<img src="/downLoad/userImg/${spflow.comId}/${spflow.creator}?sid=${param.sid}"
										title="${spflow.creatorName}" class="user-avatar"/>
									<span class="user-name">${spflow.creatorName}</span>
								</div>
			                 </td>
			               	 <td>${fn:substring(spflow.recordCreateTime,0,10) }</td>
			             </tr>
			 		</c:forEach>
			 	</c:when>
			 	<c:otherwise>
			 		<tr>
			 			<td height="30" colspan="7" align="center"><h3>没有关联流程！</h3></td>
			 		</tr>
			 	</c:otherwise>
			 </c:choose>
        	</tbody>
    	</table>
	</div>
<tags:pageBar url="/workFlow/busModSpflowList"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
