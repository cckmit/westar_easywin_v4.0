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
function viewTask(taskId){
	authCheck(taskId,'003',-1,function(){
		var url="/task/viewTask?sid=${param.sid}&id="+taskId
			+"&redirectPage="+encodeURIComponent('${param.redirectPage}')
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
                    <th width="10%" valign="middle">紧急度</th>
					<th valign="middle" class="hidden-phone">任务名称</th>
					<th width="14%" valign="middle">任务进度</th>
					<th width="15%" valign="middle">责任人</th>
					<th width="14%" valign="middle">创建时间</th>
                </tr>
           </thead>
           <tbody>
           	<c:choose>
			 	<c:when test="${not empty taskList}">
			 		<c:forEach items="${taskList}" var="task" varStatus="status">
			           	<tr>
			            	<td>
			            		<%-- <a href="javascript:void(0)" class="fa ${task.attentionState==0?'fa-star-o':'fa-star ws-star'}" 
			            		title="${task.attentionState==0?'标识关注':'取消关注'}" 
			            		onclick="changeAtten('003',${task.id},${task.attentionState},'${param.sid}',this)"></a> --%>
			            		<label class="optRowNum" style="display: block;width: 20px">${status.count}</label>
			            	</td>
			            	<c:set var="gradeColor">
					 			<c:choose>
					 				<c:when test="${task.grade==4}">label-danger</c:when>
					 				<c:when test="${task.grade==3}">label-danger</c:when>
					 				<c:when test="${task.grade==2}">label-orange</c:when>
					 				<c:when test="${task.grade==1}">label-success</c:when>
					 			</c:choose>
					 		</c:set>
					 		<td>
				            	<c:choose>
			 						<c:when test="${task.state==3 || task.state==4}">
			 							--
			 						</c:when>
			 						<c:otherwise>
			 							<span class="label ${gradeColor}">
		                                	<c:choose>
								 				<c:when test="${task.grade==4}">紧急且重要</c:when>
								 				<c:when test="${task.grade==3}">紧急</c:when>
								 				<c:when test="${task.grade==2}">重要</c:when>
								 				<c:when test="${task.grade==1}">普通</c:when>
								 			</c:choose>
		                               </span>
			 						</c:otherwise>
			 					</c:choose>
			            	</td>
			            	<td class="hidden-phone">
				            	<a href="javascript:void(0);" onclick="viewTask(${task.id});" title="${task.taskName}">
			 						<tags:cutString num="24">${task.taskName}</tags:cutString>
			 					</a>
			                </td>
			                 <td>
		 						<c:choose>
			 						<c:when test="${task.state==3}">已挂起</c:when>
			 						<c:when test="${task.state==4}"><font style="color:#FF0000;">已完成</font></c:when>
			 						<c:otherwise><font style="color:green;">${ not empty task.taskProgress?task.taskProgress:'0'}%</font></c:otherwise>
			 					</c:choose>
			                </td>
			                <td>
			               	 	<div class="ticket-user pull-left other-user-box">
									<img src="/downLoad/userImg/${task.comId}/${task.owner}?sid=${param.sid}"
										title="${task.ownerName}" class="user-avatar"/>
									<span class="user-name">${task.ownerName}</span>
								</div>
			                 </td>
			               	 <td>${fn:substring(task.recordCreateTime,0,10) }</td>
			             </tr>
			 		</c:forEach>
			 	</c:when>
			 	<c:otherwise>
			 		<tr>
			 			<td height="30" colspan="7" align="center"><h3>没有关联任务！</h3></td>
			 		</tr>
			 	</c:otherwise>
			 </c:choose>
        	</tbody>
    	</table>
	</div>
<tags:pageBar url="/task/busModTaskList"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
