<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<script type="text/javascript">
	function dailyDateSearch(obj){
		$("#searchForm [name='dailyDate']").val($(obj).val());
		$("#searchForm").submit();
	}
	//分享查看权限验证
    function viewDaily(dailyId,submitState){
		if(submitState & submitState>0){
	        $.post("/daily/authorCheck?sid=${param.sid}",{Action:"post",dailyId:dailyId},
                function (msgObjs){
                    if(!msgObjs.succ){
                        showNotification(2,msgObjs.promptMsg);
                    }else{
                        var url = "/daily/viewDaily?sid=${param.sid}&id="+dailyId;
                        openWinByRight(url);
                    }
                },"json");
		}else{
            showNotification(2,"日报未提交！");
		}
    }
</script>
<!-- Page Content -->
<div class="page-content">
    <!-- Page Body -->
    <div class="page-body">
        <div class="row">
            <div class="col-md-12 col-xs-12 ">
                <div class="widget">
                    <div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                        <div>
                            <div class="searchCond" style="display: block">
								<form action="/statistics/platform/dailyRepStatistics" id="searchForm" class="subform">
								<input type="hidden" name="sid" value="${param.sid}">
								<input type="hidden" name="activityMenu" value="${param.activityMenu}">
								<input type="hidden" id="submitState" name="submitState" value="${daily.submitState}"/>
                                <div class="table-toolbar ps-margin margin-right-10">
                                    <div class="btn-group">
                                    	<span style="font-weight:bold;">汇报日期：</span>
                                        <input class="form-control dpd2 no-padding condDate" type="text"
                                              readonly="readonly" value="${daily.dailyDate}" style="font-weight:bold;"
                                              id="dailyDate" name="dailyDate" placeholder="汇报日期"
											  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'dailyDate\',{d:-0});}',onpicked:function(dp){dailyDateSearch(this);}})" />
                                    </div>
                                </div>
								<div class="table-toolbar ps-margin margin-right-10">
									<div class="btn-group">
										<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
											<c:choose>
												<c:when test="${not empty daily.submitState}">
													<font style="font-weight:bold;"> <c:choose>
															<c:when test="${daily.submitState eq 0}">未提交</c:when>
															<c:when test="${daily.submitState eq 1}">已提交</c:when>
															<c:when test="${daily.submitState eq 2}">延迟提交</c:when>
														</c:choose>
													</font>
												</c:when>
												<c:otherwise>提交状态</c:otherwise>
											</c:choose>
											<i class="fa fa-angle-down"></i>
										</a>
										<ul class="dropdown-menu dropdown-default">
											<li><a href="javascript:void(0)" class="clearThisElement" relateElement="submitState">不限条件</a></li>
											<li><a href="javascript:void(0)" class="setElementValue" relateElement="submitState" dataValue="0">未提交</a></li>
											<li><a href="javascript:void(0)" class="setElementValue" relateElement="submitState" dataValue="1">已提交</a></li>
											<li><a href="javascript:void(0)" class="setElementValue" relateElement="submitState" dataValue="2">延迟提交</a></li>
										</ul>
									</div>
								</div>
                                <div class="btn-group pull-left" >
                                    <div class="table-toolbar ps-margin margin-right-10">
                                        <div class="btn-group">
                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="部门筛选">
                                                <c:choose>
                                                    <c:when test="${not empty daily.depName}">
                                                        <font style="font-weight:bold;">${daily.depName}</font>
                                                    </c:when>
                                                    <c:otherwise>部门筛选</c:otherwise>
                                                </c:choose>
                                                <i class="fa fa-angle-down"></i>
                                            </a>
                                            <ul class="dropdown-menu dropdown-default">
                                                <li>
                                                    <a href="javascript:void(0)" class="clearMoreElement" relateList="dep_select">不限条件</a>
                                                </li>
                                                <li>
                                                    <a href="javascript:void(0)" class="depMoreElementSelect" relateList="dep_select">部门选择</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
									<div style="float: left;width: 250px;display: none">
										<select list="listDep" listkey="id" listvalue="depName" id="dep_select" name="listDep.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
											<c:forEach items="${daily.listDep}" var="obj" varStatus="vs">
												<option selected="selected" value="${obj.id }">${obj.depName }</option>
											</c:forEach>
										</select>
									</div>
                                	<div class="btn-group pull-left" >
                                		<div class="table-toolbar ps-margin" >
	                                        <div class="btn-group">
	                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
	                                                	汇报人员
	                                                <i class="fa fa-angle-down"></i>
	                                            </a>
	                                            <ul class="dropdown-menu dropdown-default">
	                                                <li>
	                                                    <a href="javascript:void(0)" class="clearMoreElement" relateList="owner_select">不限条件</a>
	                                                </li>
	                                                <li>
	                                                    <a href="javascript:void(0)" class="userMoreElementSelect" relateList="owner_select">人员选择</a>
	                                                </li>
	                                            </ul>
	                                        </div>
	                                    </div>
										<div style="float: left;width: 250px;display: none">
											<select list="listOwner" listkey="id" listvalue="userName" id="owner_select" name="listOwner.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${daily.listOwner}" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.userName}</option>
												</c:forEach>
											</select>
										</div>
                                	</div>
                                </div>
                               </form>
                            </div>

                            <div class="batchOpt" style="display: none"></div>
                            <div class="ps-clear"></div>
                        </div>
                        <div class="padding-top-10 text-left moreUserListShow" style="display:none" id="owner_selectDiv">
                            <strong>汇报人筛选:</strong>
                        </div>
                        <div class=" padding-top-10 text-left" style="display:${empty daily.listDep ? 'none':'block'}">
							<strong>汇报部门筛选:</strong>
							<c:forEach items="${daily.listDep}" var="obj" varStatus="vs">
								<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('dep','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.depName }</span>
							</c:forEach>
						</div>
                        <div class=" padding-top-10 text-left" style="display:${empty daily.listOwner ? 'none':'block'}">
							<strong>汇报人筛选:</strong>
							<c:forEach items="${daily.listOwner}" var="obj" varStatus="vs">
								<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('owner','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
							</c:forEach>
						</div>
                    </div>
                    <div id="tableShow" style="display: ${(empty param.searchTab || param.searchTab eq 51)?'display':'none'}">
                        <div class="widget-body">
                            <form action="/clock/delClock" id="delForm">
								<input type="hidden" id="redirectPage" name="redirectPage" />
								<input type="hidden" name="sid" value="${param.sid}" />
								<table class="table table-bordered">
									<thead>
										<tr role="row">
											<th class="text-center">序号</th>
											<th class="text-center">汇报范围</th>
											<th class="text-center">部门</th>
											<th class="text-center">汇报人</th>
											<th class="text-center">汇报时间</th>
											<th class="text-center">汇报状态</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listDaily}" var="dailyVo" varStatus="status">
											<tr style="color:${dailyVo.state==0?'':'gray'}">
												<td class="text-center">${status.count}</td>
												<td class="text-center">
													<a href="javascript:void(0);" onclick="readMod(this,'daily',0,'050');viewDaily('${dailyVo.id}','${dailyVo.submitState}');">
														${dailyVo.dailyDate}
													</a>
												</td>
												<td class="text-center">${dailyVo.depName }</td>
												<td class="text-center">${dailyVo.userName}</td>
												<td class="text-center">
													<c:choose>
														<c:when test="${not empty dailyVo.recordCreateTime}">
																${dailyVo.recordCreateTime}
														</c:when>
														<c:otherwise>
															未发布
														</c:otherwise>
													</c:choose>
												</td>
												<td class="text-center"><c:choose>
														<c:when test="${dailyVo.submitState ==0 }">
															<span style="color: red;">未提交</span>
														</c:when>
														<c:when test="${dailyVo.submitState ==1 }">
															<span style="color: green;">已提交</span>
														</c:when>
														<c:when test="${dailyVo.submitState ==2 }">
															<span style="color:#CD00CD;">延迟提交</span>
														</c:when>
													</c:choose>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/statistics/platform/dailyRepStatistics"></tags:pageBar>
                        </div>
                    </div>
                    <div id="lineShow" style="display: ${(not empty param.searchTab and param.searchTab ne 51)?'display':'none'}">
                        <div class="box">
                            <ul class="event_year">
                            </ul>
                            <ul class="event_list">
                            </ul>

                            <div class="clearfix"></div>

                            <div style="text-align: center;">
                                <a href="javscript:void()" id="dailyMore">加载更多</a>
                            </div>
                        </div>

                    </div>

                </div>
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
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
</html>