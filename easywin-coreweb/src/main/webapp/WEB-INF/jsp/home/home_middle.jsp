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
	<!-- paramDiv 用于删除栏目找父节点 -->
	 			<div class="${param.width eq '1'?'col-lg-12':'col-lg-6'} col-sm-6 col-xs-12 paramDiv" id="paramDiv" >
				<div class="widget radius-bordered">
					<div class="widget-header bg-themeprimary">
						<i class="widget-icon fa fa-calendar"></i>
						<span class="widget-caption ">${param.busName}</span>
						<div class="widget-buttons" id = "indexModelSet">
							<a data-toggle="config" href="javascript:void(0)" onclick="menuSet('${param.sid}','home')">
								<i class="fa fa-cog"></i>
							</a>
							<a data-toggle="collapse" href="javascript:void(0)">
								<i class="fa fa-minus"></i>
							</a>
							<a data-toggle="dispose" href="javascript:void(0)" onclick="closeMenuHeadSet(this,'${param.sid}','${param.busType }')">
								<i class="fa fa-times "></i>
							</a>
						</div>
					</div>
					<div class="widget-body orders-container ps-height">
                          	<ul class="orders-list indexLanmu" style="min-height: 150px" lanmuType="${param.busType}">
			 				<!-- ajax 取得数据 -->
					 	</ul>
                           <div class="panel-body" style="text-align: center;">
							<c:choose>
								<c:when test="${param.busType=='013'}">
									<div class=" col-lg-6 col-sm-12" >
										<a class="btn btn-default btn-xs margin-left-50 margin-top-10"  id = "addBusMore" href="javascript:void(0);" 
										onclick="addFiles('${param.sid}');">
											<i class="fa fa-plus"></i>
											${param.addName}
										</a>
									</div>
								</c:when>
								<c:otherwise>
									<c:if test="${not empty param.addUrl }">
										<div class=" col-lg-6 col-sm-12">
											<a class="btn btn-default btn-xs margin-left-50 margin-top-10" id = "addBusMore"  href="javascript:void(0);" 
											onclick="openWinByRight('${param.addUrl}');">
											<i class="fa fa-plus"></i>
											${param.addName}
											</a>
										</div>
									</c:if>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${not empty param.addUrl}">
									<div class="col-lg-6 col-sm-12" >
										<a class="btn btn-default btn-xs margin-left-50 margin-top-10" id = "viewBusMore" href="javascript:void(0);" onclick="menuClick('${param.url}');">
										<i class="fa fa-eye"></i>
										查看更多
										</a>
									</div>
								</c:when>
								<c:otherwise>
									<div class="col-lg-12 col-sm-12" >
										<a class="btn btn-default btn-xs margin-top-10" id = "viewBusMore" href="javascript:void(0);" onclick="menuClick('${param.url}');">
										<i class="fa fa-eye"></i>
										查看更多
										</a>
									</div>
								</c:otherwise>
							</c:choose>
                           </div>
					</div>
				</div>
			</div>