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
               <c:choose>
				 	<c:when test="${not empty meetingRooms}">
            	<div class="row">
                	<div class="col-md-12 col-xs-12 ">
                    	<div class="widget">
                         	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                            	<div class="checkbox ps-margin pull-left">
									<label>
										<input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')">
										<span class="text" style="color: inherit;">全选</span>
									</label>
								</div>
								<form id="searchForm" action="/meetingRoom/listPagedMeetingRoom">
									<input type="hidden" name="searchTab" id="searchTab" value="${param.searchTab}">
									<input type="hidden" name="sid" value="${param.sid}"/>
												
									<div class="searchCond" style="display: block">
										<div class="btn-group pull-left">
										</div>
									</div>
									<div class="batchOpt" style="display: none">
										<div class="btn-group pull-left">
											<div class="table-toolbar ps-margin">
	                                        	<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" id="delMeetingRoom">
														批量删除
													</a>
	                                        	</div>
	                                    	</div>
										</div>
									</div>
								</form>
								<div class="widget-buttons ps-widget-buttons">
                                   	<button class="btn btn-primary btn-xs" type="button" id="newMeetingRoom">
                                   		<i class="fa fa-plus"></i>
                                   		添加会议室
                                   	</button>
                                   </div>
                            </div>
                            <div class="widget-body">
	                        	<form id="delForm" action="/meetingRoom/delMeetingRoom">
									<input type="hidden" name="redirectPage">
									<input type="hidden" name="sid" value="${param.sid }">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr role="row">
											  	<th width="10%" valign="middle">序号</th>
											  	<th valign="middle" class="hidden-phone">会议室名称</th>
											  	<th width="25%" valign="middle">会议室地址</th>
											  	<th width="10%" valign="middle">容纳人数</th>
											  	<th width="10%" valign="middle">会议室管理员</th>
											  	<th width="10%" valign="middle">是否默认</th>
											  	<th width="8%" valign="middle">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
									 		<c:forEach items="${meetingRooms}" var="obj" varStatus="vs">
										 		<tr class="optTr">
	                                                <td class="optTd" style="height: 47px">
											 			<label class="optCheckBox" style="display: none;width: 20px">
										 					<input class="colored-blue" type="checkbox" name="ids" value="${obj.id}"/>
										 					<span class="text"></span>
											 			</label>
	                                                	<label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
	                                                </td>
	                                                <td valign="middle">
														<a href="javascript:void(0)" onclick="editMeetingRoom('${obj.id}')">
														${obj.roomName}
														</a>
													</td>
													<td valign="middle">
														${obj.roomAddress}
													</td>
													<td valign="middle">
														<c:if test="${obj.containMax>0}">${obj.containMax}人</c:if>
													</td>
													<td valign="middle">
														<div class="ticket-user pull-left other-user-box" >
															<img class="user-avatar" src="/downLoad/userImg/${obj.comId}/${obj.mamager}?sid=${param.sid}"/>
														  	<span class="user-name">${obj.mamagerName}</span>
														  </div>
												  	</td>
													<td valign="middle">
														${obj.isDefault eq 1?'是':'否' }
													</td>
													<td valign="middle">
														<a href="javascript:void(0)" onclick="editMeetingRoom('${obj.id}')">
														编辑
														</a>
													</td>
	                                            </tr>
									 		</c:forEach>
                                        </tbody>
                                    </table>
                                    </form>
                                   	<tags:pageBar url="/meetingRoom/listPagedMeetingRoom"></tags:pageBar>
                                </div>
                            </div>
                        </div>             
                </div>
                </c:when>
                <c:otherwise>
                	<div class="container"
					style="left:50%;top:50%;position: absolute;
				margin:-90px 0 0 -180px;padding-top:200px;text-align:center;width:488px;">
					<section class="error-container text-center">
						<h1>
							<i class="fa fa-exclamation-triangle"></i>
						</h1>
						<div class="error-divider">
							<h2>还未添加团队会议室相关信息</h2>
							<p class="description">协同提高效率，分享拉近距离。</p>
							<a href="javascript:void(0);" id="newMeetingRoom"
								class="return-btn"><i class="fa fa-plus"></i>添加会议室</a>
						</div>
					</section>
				</div>
                </c:otherwise>
                </c:choose>
                <!-- /Page Body -->
            </div>
            <!-- /Page Content -->
            
        </div>
        <!-- /Page Container -->
        <!-- Main Container -->
</body>
</html>
