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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>

<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
$(function() {
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});
function chooseAll(cls){
	$("."+cls).click()
}
function selectOne(){
	var len0 = $(".task0").length;
	var len1 = $(".task1").length;
	var len2 = $(".task2").length;
	if(len0>0){
		$(".task0").click();
	}else if(len1>0){
		$(".task1").click();
	}else if(len2>0){
		$(".task2").click();
	}
	
}
function formSub(){
	$("#subform").submit();
}
</script>
<style type="text/css">
	.title{background-color:#B2DFEE;
	text-align: right;
	}
	.discripe{
		padding:10px 0px; 
		padding-left: 50px;
		background-color: #B2DFEE;
	}
</style>
</head>
<body onload="selectOne()">


<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	<span class="widget-caption themeprimary ps-layerTitle">任务合并</span>
    <div class="widget-buttons">
		<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
			<i class="fa fa-times themeprimary"></i>
		</a>
	</div>
</div>
<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">

<section id="container">
<form id="subform" class="subform" method="post" action="/task/taskCompress">
	<input type="hidden" name="redirectPage" value="${redirectPage}">
	<input type="hidden" name="sourcePage" value="${sourcePage}">
	<tags:token></tags:token>
	<c:choose>
		<c:when test="${not empty ids}">
		<c:forEach 	items="${ids}" var="preId" varStatus="">
			<input type="hidden" name="ids" value="${preId}">
		</c:forEach>
		</c:when>
	</c:choose>
<div class="ws-projectBox">
	<div class="ws-projectIn">
		<div class="ws-titleBox">
			<h6 class="ws-title">
				合并任务信息(<font color="red">任务合并后将不能还原</font>)
			</h6>
			</div>
				<div class="panel-body">
                              <table class="table  table-bordered general-table">
                                     <tr>
                                         <td class="title" style="width: 19%;">任务名称 &nbsp;&nbsp;</td>
                                         <td style="width: 27%">
                                         	${task0.taskName}
                                         	<c:choose>
                                         		<c:when test="${taskComPress0 ==0}">(子任务)</c:when>
                                         		<c:otherwise>
                                           	<a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseAll('task0')">[全选]</a>
                                         		</c:otherwise>
                                         	</c:choose>
                                         </td>
                                         <td style="width: 27%">
                                         	${task1.taskName }
                                         		<c:choose>
                                          		<c:when test="${taskComPress1 ==0}">(子任务)</c:when>
                                          		<c:otherwise>
	                                           	<a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseAll('task1')">[全选]</a>
                                          		</c:otherwise>
                                          	</c:choose>
                                         </td>
                                         <c:if test="${index==3}">
                                         	<td style="width:27%">
                                         		${task2.taskName }
                                         		<c:choose>
                                          		<c:when test="${taskComPress1 ==0}">(子任务)</c:when>
                                          		<c:otherwise>
	                                           	<a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseAll('task2')">[全选]</a>
                                          		</c:otherwise>
                                          	</c:choose>
                                         	</td>
                                         </c:if>
                                     </tr>
                                     <tr>
                                         <td class="title" style="width: 19%;">合并对象 <font color="red">*</font></td>
                                         <td style="width: 27%">
                                         	<c:if test="${taskComPress0 ==1}">
                                         		<label>
		                                          	<input type="radio" name="id" class="task0" value="${task0.id}">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                         </td>
                                         <td style="width: 27%">
                                         	<c:if test="${taskComPress1 ==1}">
                                         		<label>
		                                          	<input type="radio" name="id" class="task1" value="${task1.id }">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                         </td>
                                         <c:if test="${index==3}">
                                         	<td style="width:27%">
                                       		<c:if test="${taskComPress2 ==1}">
                                       			<label>
	                                          		<input type="radio" name="id" class="task2" value="${task2.id }">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                       		</c:if>
                                         	</td>
                                         </c:if>
                                     </tr>
                                     <tr>
                                         <td class="title" style="width: 19%;">办理人 &nbsp;&nbsp;</td>
                                         <td style="width: 27%">
                                         	<c:if test="${taskComPress0 ==1}">
                                         		<label>
		                                          	<input type="radio" name="executor" class="task0" value="${task0.executor}">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                         	<c:choose>
                                         		<c:when test="${not empty task0.listTaskExecutor }">
                                         			<c:forEach items="${task0.listTaskExecutor}" var="taskExUser" varStatus="vs">
                                         				${taskExUser.executorName }
                                         				<c:if test="${not vs.last }">、</c:if>
                                         			</c:forEach>
                                         		</c:when>
                                         	</c:choose>
                                         	
                                         </td>
                                         <td style="width: 27%">
                                         	<c:if test="${taskComPress1 ==1}">
                                         		<label>
		                                          	<input type="radio" name="executor" class="task1" value="${task1.executor }">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                         	<c:choose>
                                         		<c:when test="${not empty task1.listTaskExecutor }">
                                         			<c:forEach items="${task1.listTaskExecutor}" var="taskExUser" varStatus="vs">
                                         				${taskExUser.executorName }
                                         				<c:if test="${not vs.last }">、</c:if>
                                         			</c:forEach>
                                         		</c:when>
                                         	</c:choose>
                                         </td>
                                         <c:if test="${index==3}">
                                         	<td style="width:27%">
                                       		<c:if test="${taskComPress2 ==1}">
                                       			<label>
	                                          		<input type="radio" name="executor" class="task2" value="${task2.executor }">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                       		</c:if>
                                       		<c:choose>
                                         		<c:when test="${not empty task2.listTaskExecutor }">
                                         			<c:forEach items="${task2.listTaskExecutor}" var="taskExUser" varStatus="vs">
                                         				${taskExUser.executorName }
                                         				<c:if test="${not vs.last }">、</c:if>
                                         			</c:forEach>
                                         		</c:when>
                                         	</c:choose>
                                         	</td>
                                         </c:if>
                                     </tr>
                                     <tr>
                                         <td class="title">任务状态&nbsp;&nbsp;</td>
                                         <td>	
                                          <c:if test="${taskComPress0 ==1}">
                                         	 <label>
	                                           	<input type="radio" name="state" class="task0" value="${task0.state}">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                          </c:if>
                                          	<c:set var="task0State">
											<c:choose>
												<c:when test="${task0.state==1}">进行中</c:when>
												<c:when test="${task0.state==3}">挂起中</c:when>
												<c:when test="${task0.state==4}">已办结</c:when>
												<c:otherwise>
													正常
												</c:otherwise>
											</c:choose>
										</c:set>
										${task0State }
                                      </td>
                                         <td>
                                         		<c:if test="${taskComPress1 ==1}">
	                                         		<label>
		                                          		<input type="radio" name="state" class="task1" value="${task1.state}">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
                                         		</c:if>
                                         			<c:set var="task1State">
											<c:choose>
												<c:when test="${task1.state==1}">进行中</c:when>
												<c:when test="${task1.state==3}">挂起中</c:when>
												<c:when test="${task1.state==4}">已办结</c:when>
												<c:otherwise>
													正常
												</c:otherwise>
											</c:choose>
										</c:set>
										${task1State }
                                         </td>
                                         <c:if test="${index==3}">
                                         		<td>
                                       				<c:if test="${taskComPress2 ==1}">
	                                       				<label>
	                                           				<input type="radio" name="state" class="task2" value="${task2.state}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
                                       				</c:if>
                                         			<c:set var="task2State">
											<c:choose>
												<c:when test="${task2.state==1}">进行中</c:when>
												<c:when test="${task2.state==3}">挂起中</c:when>
												<c:when test="${task2.state==4}">已办结</c:when>
												<c:otherwise>
													正常
												</c:otherwise>
											</c:choose>
										</c:set>
										${task2State}
                                         		</td>
                                         </c:if>
                                         
                                     </tr>
                                     <tr>
                                         <td class="title">紧急程度&nbsp;&nbsp;</td>
                                         <td>
                                         	<c:if test="${taskComPress0 ==1}">
                                         		<label>
		                                          	<input type="radio" name="grade" class="task0" value="${task0.grade}">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                          	<tags:viewDataDic type="grade" code="${task0.grade}"></tags:viewDataDic>
                                      </td>
                                         <td>
                                         	<c:if test="${taskComPress1 ==1}">
                                         		<label>
	                                         		<input type="radio" name="grade" class="task1" value="${task1.grade}">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                         			<tags:viewDataDic type="grade" code="${task1.grade}"></tags:viewDataDic>
                                         </td>
                                         <c:if test="${index==3}">
                                         		<td>
                                         			<c:if test="${taskComPress2 ==1}">
                                         				<label>
		                                           			<input type="radio" name="grade" class="task2" value="${task2.grade}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
                                         			</c:if>
                                         			<tags:viewDataDic type="grade" code="${task2.grade}"></tags:viewDataDic>
                                         		</td>
                                         </c:if>
                                         
                                     </tr>
                                     <tr>
                                         <td class="title">完成时限&nbsp;&nbsp;</td>
                                         <td>
                                         	<c:if test="${taskComPress0 ==1}">
                                         		<label>
		                                          	<input type="radio" name="dealTimeLimit" class="task0" value="${task0.dealTimeLimit}">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                          	${task0.dealTimeLimit}
                                      </td>
                                         <td>
                                         	<c:if test="${taskComPress1 ==1}">
                                         		<label>
	                                         		<input type="radio" name="dealTimeLimit" class="task1" value="${task1.dealTimeLimit}">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                         		${task1.dealTimeLimit}
                                         </td>
                                         <c:if test="${index==3}">
                                         		<td>
                                         			<c:if test="${taskComPress2 ==1}">
                                         				<label>
		                                          			<input type="radio" name="dealTimeLimit" class="task2" value="${task2.dealTimeLimit}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
                                        			</c:if>
                                         			${task2.dealTimeLimit}
                                         		</td>
                                         </c:if>
                                         
                                     </tr>
                                     <tr>
                                         <td class="title">关联模块&nbsp;&nbsp;</td>
                                         <td>
                                         	<c:if test="${taskComPress0 ==1}">
                                         		<label>
		                                          	<input type="radio" name="taskIdAndBusId" class="task0" value="${task0.id}@${task0.busId}@${task0.busType}">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                         		<c:choose>
                                         			<c:when test="${task0.busType=='005'}">[项目]</c:when>
                                         			<c:when test="${task0.busType=='012'}">[客户]</c:when>
                                         		</c:choose>
                                          	${task0.busName} 
                                      </td>
                                         <td>
                                         	<c:if test="${taskComPress1 ==1}">
                                         		<label>
	                                         		<input type="radio" name="taskIdAndBusId" class="task1" value="${task1.id}@${task1.busId}@${task1.busType}">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                       	  	<c:choose>
                                         			<c:when test="${task1.busType=='005'}">[项目]</c:when>
                                         			<c:when test="${task1.busType=='012'}">[客户]</c:when>
                                         		</c:choose>
                                         		${task1.busName }
                                         </td>
                                         <c:if test="${index==3}">
                                         		<td>
                                         			<c:if test="${taskComPress2 ==1}">
                                         			<label>
	                                           			<input type="radio" name="taskIdAndBusId" class="task2" value="${task2.id}@${task2.busId}@${task2.busType}">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           	
                                         			</c:if>
                                         				<c:choose>
                                           			<c:when test="${task1.busType=='005'}">[项目]</c:when>
                                           			<c:when test="${task1.busType=='012'}">[客户]</c:when>
                                           		</c:choose>
                                         			${task2.busName}
                                         		</td>
                                         </c:if>
                                         
                                     </tr>
                                     <tr>
                                         <td class="title">上级任务&nbsp;&nbsp;</td>
                                         <td>
                                         	<c:if test="${taskComPress0 ==1}">
                                         			<label>
	                                          	<input type="radio" name="parentId" class="task0" value="${task0.parentId }">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                          	${task0.pTaskName} 
                                      </td>
                                         <td>
                                         	<c:if test="${taskComPress1 ==1}">
                                         		<label>
	                                         		<input type="radio" name="parentId" class="task1" value="${task1.parentId }">
		                                           	<span class="text">&nbsp;</span>
	                                           	</label>
                                         	</c:if>
                                         		${task1.pTaskName }
                                         </td>
                                         <c:if test="${index==3}">
                                         		<td>
                                         				<c:if test="${taskComPress2 ==1}">
                                         				<label>
		                                           			<input type="radio" name="parentId" class="task2" value="${task2.parentId }">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                           	
                                         				</c:if>
                                         			${task2.pTaskName }
                                         		</td>
                                         </c:if>
                                         
                                     </tr>
                                     <tr>
                                     	<!-- 之所以选用任务的主键，是由于任务简介可能含有特殊字符，合并的时候在后台取值任务简介-->
                                         <td class="title">任务简介&nbsp;&nbsp;</td>
                                         <td style="word-break:break-all">
                                         		<c:if test="${taskComPress0 ==1}">
	                                         		<label>
			                                           	<input type="radio" name="taskRemark" class="task0" value="${task0.id}">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
                                         		</c:if>
                                         			${task0.taskRemark}
                                        </td>
                                         <td style="word-break:break-all">
                                         		<c:if test="${taskComPress1 ==1}">
                                         		<label>
                                          	 		<input type="radio" name="taskRemark" class="task1" value="${task1.id}">
                                          	 		 <span class="text">&nbsp;</span>
                                          	 	</label>
                                         		</c:if>
                                          			${task1.taskRemark}
                                         	</td>
                                          <c:if test="${index==3}">
                                         		<td style="word-break:break-all">
                                         			<c:if test="${taskComPress2 ==1}">
                                         			<label>
		                                           		<input type="radio" name="taskRemark" class="task2" value="${task2.id}">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
                                         			</c:if>
                                          		${task2.taskRemark}
                                         		</td>
                                          </c:if>
                                     </tr>
                              </table>
                            	  <div class="discripe">
								<p>
									注：合并后，任务信息只保留选中项，若没有指定合并信息，则默认保留选中的任务名称对应的信息。 参与合并的任务信息，最终只会保留选中的任务。<br/>
									任务留言、文档以及共享情况会自动合并到选中的合并对象中。
								</p>
                             	 </div>
                          </div>
		</div>
	</div>
<!--main content end-->
</form>
</div>
</body>
</html>
