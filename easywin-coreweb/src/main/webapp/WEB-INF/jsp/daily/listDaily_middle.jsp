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
                                <div class="table-toolbar ps-margin margin-right-10">
                                    <div class="btn-group">
                                        <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="viewYear">
                                            <font style="font-weight:bold;">${nowYear}年</font>
                                            <i class="fa fa-angle-down"></i>
                                        </a>
                                        <ul class="dropdown-menu dropdown-default" id="dailyYearUl">
                                            <!--数据异步获得  #dailyYearUl-->
                                        </ul>
                                    </div>
                                </div>
                                <div class="btn-group pull-left" >
                                    <div class="table-toolbar ps-margin" style="display:${param.searchTab eq '52'?'none':'block'}">
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
                                                    <a href="javascript:void(0)" class="clearValue" relateElement="depId">不限条件</a>
                                                </li>
                                                <li>
                                                    <a href="javascript:void(0)" class="depOneElementSelect" relateElement="depId" relateElementName="depName">部门选择</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>

                                    <div class="table-toolbar ps-margin" style="display:${param.searchTab eq '52'?'none':'block'}">
                                        <div class="btn-group">
                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
                                                汇报人员
                                                <i class="fa fa-angle-down"></i>
                                            </a>
                                            <ul class="dropdown-menu dropdown-default">
                                                <li>
                                                    <a href="javascript:void(0)" class="clearMoreValue" relateList="owner_select">不限条件</a>
                                                </li>
                                                <li>
                                                    <a href="javascript:void(0)" class="userMoreSelect" relateList="owner_select">人员选择</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <div style="float: left;width: 250px;display: none">

                                    </div>
                                    <div class="table-toolbar ps-margin">
                                        <div class="btn-group cond" id="moreCondition_Div">
                                            <a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
                                                <c:choose>
                                                    <c:when test="${not empty daily.startDate || not empty daily.endDate}">
                                                        <font style="font-weight:bold;">筛选中</font>
                                                    </c:when>
                                                    <c:otherwise>
                                                        更多
                                                    </c:otherwise>
                                                </c:choose>
                                                <i class="fa fa-angle-down"></i>
                                            </a>
                                            <div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
                                                <div class="ps-margin ps-search padding-left-10">
                                                    <span class="btn-xs">起止时间：</span>
                                                    <input class="form-control dpd2 no-padding condDate" type="text"
                                                        readonly="readonly" value="${daily.startDate}"
                                                        id="startDate" name="startDate" placeholder="开始时间"/>
                                                    <span>~</span>
                                                    <input class="form-control dpd2 no-padding condDate" type="text"
                                                        readonly="readonly" value="${daily.endDate}" id="endDate"
                                                        name="endDate" placeholder="结束时间" />
                                                </div>
                                                <div class="ps-clear padding-top-10" style="text-align: center;">
                                                    <button type="button" class="btn btn-primary btn-xs moreSearchBtn">查询</button>
                                                    <button type="button" class="btn btn-default btn-xs margin-left-10 moreClearBtn">重置</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                                <div class="ps-margin ps-search">
											<span class="input-icon">
												<input id="dailyName" name="dailyName" value="${daily.dailyName}" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字">
												<a href="javascript:void(0)" class="ps-searchBtn">
													<i class="glyphicon glyphicon-search circular danger"></i>
												</a>
											</span>
                                </div>
                            </div>


                            <div class="batchOpt" style="display: none"></div>
                            <div class="widget-buttons ps-widget-buttons">
                                <button class="btn btn-primary btn-xs" type="button" onclick="addDailyNowDate()">
                                    <i class="fa fa-plus"></i>
                                    发布分享
                                </button>
                            </div>
                            <div class="ps-clear" id="formTempData">
                                <input type="hidden" name="dailyYear" id="dailyYear" value="${nowYear}"/>
                                <input type="hidden" id="dailierId" name="dailierId" value="${daily.dailierId}"/>
                                <input type="hidden" name="dailyMonth" id="dailyMonth" value="${nowMonth}"/>
                                <input type="hidden" id="depId" name="depId" value="${daily.depId}"/>
                                <input type="hidden" id="depName" name="depName" value="${daily.depName}"/>
                                <input type="hidden" id="dailierType" name="dailierType" value="${daily.dailierType}"/>
                                <input type="hidden" id="dailyDoneState" name="dailyDoneState" value="${daily.dailyDoneState}"/>

                                <select list="listDeps" listkey="id" listvalue="depName" id="sysDep_select"
                                        multiple="multiple" moreselect="true" style="display: none">
                                </select>
                                <select list="listOwner" listkey="id" listvalue="userName" id="owner_select"
                                        name="listOwner.id" multiple="multiple" moreselect="true"
                                        style="display: none">
                                    <c:forEach items="${daily.listOwner }" var="obj" varStatus="vs">
                                        <option selected="selected" value="${obj.id }">${obj.userName }</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="padding-top-10 text-left moreUserListShow" style="display:none" id="owner_selectDiv">
                            <strong>汇报人筛选:</strong>
                        </div>
                    </div>
                    <div id="tableShow" style="display: ${(empty param.searchTab || param.searchTab eq 51)?'display':'none'}">
                        <div class="widget-body">
                            <table class="table table-striped table-hover">
                                <thead>
                                <tr role="row">
                                    <th width="5%" valign="middle">序号</th>
                                    <th valign="middle">名称</th>
                                    <th width="25%" valign="middle">汇报范围</th>
                                    <th width="15%" valign="middle" class="hidden-phone">部门</th>
                                    <th width="15%" valign="middle">汇报时间</th>
                                    <th width="15%" valign="middle">汇报人</th>
                                </tr>
                                </thead>
                                <tbody id="allTodoBody">

                                </tbody>
                            </table>
                            <div class="panel-body ps-page bg-white" style="font-size: 12px">
                                <p class="pull-left ps-pageText">共<b class="badge" id="totalNum">0</b>条记录</p>
                                <ul class="pagination pull-right" id="pageDiv">
                                </ul>
                            </div>
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

