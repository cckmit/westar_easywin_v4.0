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
</head>
<body>
<!-- Page Content -->
        <div class="page-content">
         	<!-- Page Body -->
            <div class="page-body">
	          
            	<div class="row">
                	<div class="col-md-12 col-xs-12 ">
                    	<div class="widget">
                         	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
								<form id="searchForm" action="/recycleBin/listPagedPreDel">
									 <input type="hidden" name="redirectPage"/>
									 <input type="hidden" id="state" name="state"/>
									 <input type="hidden" name="pager.pageSize" value="15"/>
									 <input type="hidden" id="busType" name="busType" value="${recycleBin.busType}"/>
									 <input type="hidden" name="sid" value="${param.sid}"/>
									 
										<div class="btn-group pull-left">
											<div class="table-toolbar ps-margin">
		                                       	<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" id="recoverChoose">
														批量还原
													</a>
		                                       	</div>
                                   			</div>
											<div class="table-toolbar ps-margin">
		                                       	<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" id="delChoose">
														批量删除
													</a>
		                                       	</div>
		                                   	</div>
	                                  		<div class="table-toolbar ps-margin">
			                                   	<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" id="recoverAll">
														全部还原
													</a>
			                                    </div>
	                                  		</div>
	                                  		<div class="table-toolbar ps-margin">
			                                    <div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" id="delAll">
														全部删除
													</a>
			                                     </div>
	                                  		</div>
										</div>
	                                    <div class="ps-margin ps-search">
											<span class="input-icon">
												<input id="content" name="content" value="${recycleBin.content}" class="form-control ps-input" type="text" placeholder="请输入关键字">
												<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
											</span>
										</div>
								</form>
                            </div>
                             <c:choose>
			 	<c:when test="${not empty list}">
                            <div class="widget-body">
	                        	 <form id="delForm" method="post">
	                        	  	<input type="hidden" name="redirectPage"/>
						 			<input type="hidden" name="sid" value="${param.sid}"/>
						 			 <input type="hidden" id="state" name="state"/>
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr role="row">
                                                <th style="width: 50px" valign="middle">
                                                	<label>
														<input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')">
														<span class="text" style="color: inherit;"></span>
													</label>
                                                </th>
											  	<th  valign="middle">
											  		<c:choose>
											  			<c:when test="${recycleBin.busType=='012'}">
											  				客户名称
											  			</c:when>
											  			<c:when test="${recycleBin.busType=='005'}">
											  				项目名称
											  			</c:when>
											  			<c:when test="${recycleBin.busType=='003'}">
											  				任务名称
											  			</c:when>
											  			<c:when test="${recycleBin.busType=='004'}">
											  				投票主题
											  			</c:when>
											  			<c:when test="${recycleBin.busType=='011'}">
											  				问答主题
											  			</c:when>
											  		</c:choose>
											  	</th>
											  	<th valign="middle" style="width: 150px">删除日期</th>
                                            </tr>
                                        </thead>
                                        <tbody>
									 		<c:forEach items="${list}" var="obj" varStatus="vs">
										 		<tr class="optTr">
	                                                <td class="optTd">
											 			<label class="optCheckBox" style="display: none;width: 20px;height: 20px">
										 					<input class="colored-blue" type="checkbox" name="ids" value="${obj.id}@${obj.busId}@${obj.busType}"/>
										 					<span class="text"></span>
											 			</label>
	                                                	<label class="optRowNum" style="display: block;width: 20px;height: 20px">${vs.count}</label>
	                                                </td>
	                                                <td valign="middle">
															<tags:cutString num="82">${obj.busTypeName}</tags:cutString></td>
									 				<td valign="middle">
									 					${fn:substring(obj.recordCreateTime,0,16)}
									 				</td>
	                                            </tr>
									 		</c:forEach>
                                        </tbody>
                                    </table>
                                    </form>
                                   <tags:pageBar url="/recycleBin/listPagedPreDel"></tags:pageBar>
                                </div>
                                </c:when>
                <c:otherwise>
	               <div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>没有相关删除数据！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
							</div>
						</section>
					</div>
                </c:otherwise>
               </c:choose>
                            </div>
                        </div>             
                </div>
                
                <!-- /Page Body -->
            </div>
            <!-- /Page Content -->
            
        </div>
        <!-- /Page Container -->
        <!-- Main Container -->
</body>
</html>
