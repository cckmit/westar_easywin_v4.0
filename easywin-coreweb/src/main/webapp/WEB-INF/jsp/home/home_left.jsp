<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
	<body>
	 <!-- Page Content -->
            <div class="page-content col-lg-9 col-sm-12 col-xs-12">
                <!-- Page Body -->
                <div class="page-body" id="indexLeft">
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div class="row">
                                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12" id = "todoTask">
                                    <div class="databox databox-lg radius-bordered databox-shadowed">
                                        <div class="databox-left bg-themesecondary">
                                            <div class="databox-piechart">
                                                <i class="stat-icon icon-lg fa fa-tasks"></i>
                                            </div>
                                        </div>
                                        <div class="databox-right bg-white ps-center" >
                                        	<a href="javascript:void(0);" onclick="menuClick('/msgShare/toDoJobs/jobsCenter?sid=${param.sid}&pager.pageSize=10');" class="ps-linkText">
                                        	<span class="databox-number themesecondary" id="allTodoNums">${todoNums}</span>
                                            <span class="databox-text darkgray">待办事项</span>
                                        	</a>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12" id = "overDueTask">
                                    <div class="databox databox-lg radius-bordered databox-shadowed">
                                        <div class="databox-left bg-themeprimary">
                                            <div class="databox-piechart">
                                                <i class="fa  fa-exclamation-triangle"></i>
                                            </div>
                                        </div>
                                        <div class="databox-right bg-white ps-center">
                                        	<a href="javascript:void(0);" onclick="menuClick('/task/listTaskToDoPage?pager.pageSize=10&sid=${param.sid}&activityMenu=task_m_1.1&overdue=true');" class="ps-linkText"><span class="databox-number themeprimary">${overdueNums}</span>
                                            <span class="databox-text darkgray">逾期任务</span></a>
                                            
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12" id = "attenNums">
                                    <div class="databox databox-lg radius-bordered databox-shadowed">
                                        <div class="databox-left bg-themethirdcolor">
                                            <div class="databox-piechart">
                                                <i class="fa fa-star"></i>
                                            </div>
                                        </div>
                                        <div class="databox-right bg-white ps-center">
                                        	<a href="javascript:void(0);" onclick="menuClick('/attention/attCenter?sid=${param.sid}&pager.pageSize=10');" class="ps-linkText">
                                        	<span class="databox-number themethirdcolor">${attenNums }</span>
                                            <span class="databox-text darkgray">更新未看</span></a>
                                            
                                        </div>
                                    </div>
                                </div> 
                            </div>
                        </div>
                    </div>
                    <div class="row" id="allLanmu">
                        <!-- 主页模块显示 -->
                        <c:choose>
						 	<c:when test="${not empty listIndexVo}">
						 		<c:forEach items="${listIndexVo}" var="indexVo" varStatus="status">
						 			 <c:choose>
						 			 	<c:when test="${indexVo.busType=='00305'}">
						 			 		<jsp:include page="/WEB-INF/jsp/home/home_subTask.jsp">
						 			 		<jsp:param value="${param.sid}" name="sid"/>
						 			 		<jsp:param value="${indexVo.url}" name="url"/>
						 			 		<jsp:param value="${indexVo.width}" name="width"/>
						 			 		</jsp:include>
						 				 </c:when>
						 				<c:otherwise>
						 				<!-- 普通模块 -->
						 					<jsp:include page="/WEB-INF/jsp/home/home_middle.jsp">
						 					<jsp:param value="${indexVo.busName}" name="busName"/>
						 					<jsp:param value="${param.sid}" name="sid"/>
						 					<jsp:param value="${indexVo.busType}" name="busType"/>
						 					<jsp:param value="${indexVo.addName}" name="addName"/>
						 					<jsp:param value="${indexVo.url}" name="url"/>
						 					<jsp:param value="${indexVo.addUrl}" name="addUrl"/>
						 					<jsp:param value="${indexVo.width}" name="width"/>
						 					</jsp:include>
						 			 	</c:otherwise>
						 			 </c:choose>
						 		</c:forEach>
						 	</c:when>
						 	<c:otherwise>
						 	<!-- 没有配置数据 -->
						 		<div class="col-lg-12 col-sm-12 col-xs-12" id="noMenuHomeSet" >
						 			<div class="container">
								        <section class="error-container text-center">
								            <h1><i class="fa fa-exclamation-triangle"></i></h1>
								            <div class="error-divider">
								                <h2>您还没有配置展示栏目！</h2>
								            </div>
								            <a  href="javascript:void(0)" class="return-btn" onclick="menuSet('${param.sid}','home')"><i class="fa fa-plus"></i>配置展示模块</a>
								        </section>
								    </div>
						 		</div>
						 	</c:otherwise>
					 	</c:choose>
                   </div>                    
                </div>
                <!-- /Page Body -->
            </div>
            <!-- /Page Content -->

