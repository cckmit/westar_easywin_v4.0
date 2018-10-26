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
<!DOCTYPE html >
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
                        <div>
                            <form id="searchForm" action="/product/listPagedPro" class="subform">
                                <input type="hidden" name="redirectPage" />
                                <input type="hidden" id="pageSize" name="pager.pageSize" value="12" />
                                <input type="hidden" name="sid" value="${param.sid}" />
                                <input type="hidden" name="state" id="state" value="${product.state}" />
                                <input type="hidden" id="ownerType" name="ownerType" value="${product.ownerType}" />
                                <input type="hidden" id="principal" name="principal" value="${product.principal}" />
                                <input type="hidden" id="manager" name="manager" value="${product.manager}" />
                                <input type="hidden" name="searchTab" id="searchTab" value="${param.searchTab}">
                                <input type="hidden" name="orderBy" id="orderBy" value="${product.orderBy}">
                                <input type="hidden" name="type" value="${product.type}">
                                <input type="hidden" name="attention" id="attention" value="${param.attention}">

                                <div class="searchCond" style="display: block">
                                    <div class="table-toolbar ps-margin margin-right-20">
                                        <div class="btn-group">
                                            <a class="btn btn-default dropdown-toggle btn-xs"
                                               data-toggle="dropdown"> <c:choose>
                                                <c:when test="${not empty product.orderBy}">
                                                    <font style="font-weight:bold;">
                                                        <c:choose>
                                                            <c:when test="${product.orderBy=='timeDesc'}">按创建时间(降序)</c:when>
                                                            <c:when test="${product.orderBy=='timeAsc'}">按创建时间(升序)</c:when>
                                                            <c:when test="${product.orderBy=='typeDesc'}">按类型(降序)</c:when>
                                                            <c:when test="${product.orderBy=='typeAsc'}">按类型(升序)</c:when>
                                                        </c:choose>
                                                    </font>
                                                </c:when>
                                                <c:otherwise>排序</c:otherwise>
                                            </c:choose> <i class="fa fa-angle-down"></i>
                                            </a>
                                            <ul class="dropdown-menu dropdown-default">
                                                <li><a href="javascript:void(0)" class="clearThisElement" relateElement="orderBy">不限条件</a>
                                                </li>
                                                <li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="timeDesc">按创建时间(降序)</a>
                                                </li>
                                                <li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="timeAsc">按创建时间(升序)</a>
                                                </li>
                                                <li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="typeDesc">按类型(降序)</a>
                                                </li>
                                                <li><a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="typeAsc">按类型(升序)</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <div class="btn-group pull-left">
                                        <div class="table-toolbar ps-margin">
                                            <div class="btn-group">
                                                <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
                                                    <c:choose>
                                                        <c:when test="${not empty product.type}">
                                                            <tags:viewDataDic type="proType" code="${product.type}"></tags:viewDataDic>
                                                        </c:when>
                                                        <c:otherwise>类型</c:otherwise>
                                                    </c:choose>
                                                    <i class="fa fa-angle-down"></i>
                                                </a>
                                                <ul class="dropdown-menu dropdown-default dataDicClz" dataDic="proType" relateElement="type" relateElementName="proTypeName">

                                                </ul>
                                            </div>
                                        </div>
                                        <c:if test="${param.searchTab!=11 && not empty param.searchTab}">
                                            <div class="table-toolbar ps-margin">
                                                <div class="btn-group">
                                                    <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
                                                        <c:choose>
                                                            <c:when test="${not empty pro.creatorName}">
                                                                <font style="font-weight:bold;">${pro.creatorName}</font>
                                                            </c:when>
                                                            <c:otherwise>创建人</c:otherwise>
                                                        </c:choose>
                                                        <i class="fa fa-angle-down"></i>
                                                    </a>
                                                    <ul class="dropdown-menu dropdown-default">
                                                        <li>
                                                            <a href="javascript:void(0)" class="clearMoreElement" relateList="creator_select">不限条件</a>
                                                        </li>
                                                        <li>
                                                            <a href="javascript:void(0)" class="userMoreElementSelect" relateList="creator_select">人员选择</a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div style="float: left;width: 250px;display: none">
                                                <select list="creators" listkey="id" listvalue="userName" id="creator_select" name="creators.id" multiple="multiple"
                                                        moreselect="true" style="width: 100%; height: 100px;">
                                                    <c:forEach items="${product.creators }" var="obj" varStatus="vs">
                                                        <option selected="selected" value="${obj.id }">${obj.userName }</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </c:if>
                                        <c:if test="${param.searchTab!=13 || empty param.searchTab}">
                                            <div class="table-toolbar ps-margin">
                                                <div class="btn-group">
                                                    <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
                                                        <c:choose>
                                                            <c:when test="${not empty pro.principalName}">
                                                                <font style="font-weight:bold;">${pro.principalName}</font>
                                                            </c:when>
                                                            <c:otherwise>责任人</c:otherwise>
                                                        </c:choose>
                                                        <i class="fa fa-angle-down"></i>
                                                    </a>
                                                    <ul class="dropdown-menu dropdown-default">
                                                        <li>
                                                            <a href="javascript:void(0)" class="clearMoreElement" relateList="principal_select">不限条件</a>
                                                        </li>
                                                        <li>
                                                            <a href="javascript:void(0)" class="userMoreElementSelect" relateList="principal_select">人员选择</a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div style="float: left;width: 250px;display: none">
                                                <select list="principals" listkey="id" listvalue="userName" id="principal_select" name="principals.id" multiple="multiple"
                                                        moreselect="true" style="width: 100%; height: 100px;">
                                                    <c:forEach items="${product.principals }" var="obj" varStatus="vs">
                                                        <option selected="selected" value="${obj.id }">${obj.userName }</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </c:if>
                                        <c:if test="${param.searchTab!=14 || empty param.searchTab}">
                                            <div class="table-toolbar ps-margin">
                                                <div class="btn-group">
                                                    <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
                                                        <c:choose>
                                                            <c:when test="${not empty product.managerName}">
                                                                <font style="font-weight:bold;">${product.managerName}</font>
                                                            </c:when>
                                                            <c:otherwise>产品经理</c:otherwise>
                                                        </c:choose>
                                                        <i class="fa fa-angle-down"></i>
                                                    </a>
                                                    <ul class="dropdown-menu dropdown-default">
                                                        <li>
                                                            <a href="javascript:void(0)" class="clearMoreElement" relateList="manager_select">不限条件</a>
                                                        </li>
                                                        <li>
                                                            <a href="javascript:void(0)" class="userMoreElementSelect" relateList="manager_select">人员选择</a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div style="float: left;width: 250px;display: none">
                                                <select list="managers" listkey="id" listvalue="userName" id="manager_select" name="managers.id" multiple="multiple"
                                                        moreselect="true" style="width: 100%; height: 100px;">
                                                    <c:forEach items="${product.managers }" var="obj" varStatus="vs">
                                                        <option selected="selected" value="${obj.id }">${obj.userName }</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </c:if>
                                        <div class="table-toolbar ps-margin margin-right-5">
                                            <div class="btn-group cond" id="moreCondition_Div">
                                                <a class="btn btn-default dropdown-toggle btn-xs"
                                                   onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
                                                    <c:when test="${
                                                        not empty product.startDate
														|| not empty product.endDate
														|| not empty product.state}">
                                                        <font style="font-weight:bold;">筛选中</font>
                                                    </c:when>
                                                    <c:otherwise>
                                                        更多
                                                    </c:otherwise>
                                                </c:choose> <i class="fa fa-angle-down"></i>
                                                </a>
                                                <div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
                                                    <div class="ps-margin ps-search padding-left-10">
                                                        <span class="btn-xs">产品状态：</span>
                                                        <button type="button" class="btn ${product.state==0?'btn-primary':'btn-default'} btn-xs  stateBtn"
                                                                onclick="stateFilter(0,this)">进行中</button>
                                                        <button type="button" class="btn ${product.state==1?'btn-primary':'btn-default'} btn-xs margin-left-7 stateBtn"
                                                                onclick="stateFilter(1,this)">已完成</button>
                                                        <button type="button" class="btn ${product.state==2?'btn-primary':'btn-default'} btn-xs margin-left-7 stateBtn"
                                                                onclick="stateFilter(2,this)">已暂停</button>
                                                        <button type="button"
                                                                class="btn ${product.state==3?'btn-primary':'btn-default'} btn-xs margin-left-7 stateBtn"
                                                                onclick="stateFilter(3,this)">已废弃</button>
                                                    </div>
                                                    <div class="dropdown-default padding-bottom-10" style="min-width: 330px;">
                                                        <div class="ps-margin ps-search padding-left-10">
                                                            <span class="btn-xs">起止时间：</span>
                                                            <input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${pro.startDate}" id="startDate" name="startDate" placeholder="开始时间"
                                                                   onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
                                                            <span>~</span>
                                                            <input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${pro.endDate}" id="endDate" name="endDate" placeholder="结束时间"
                                                                   onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
                                                        </div>
                                                        <div class="ps-clear padding-top-10" style="text-align: center;">
                                                            <button type="submit" class="btn btn-primary btn-xs">查询</button>
                                                            <button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="ps-margin ps-search">
											<span class="input-icon">
												<input id="name" class="form-control ps-input" name="name" type="text" placeholder="请输入关键字" value="${product.name}">
												<a href="javascript:void(0)" class="ps-searchBtn" onclick="$('#searchForm').submit()">
													<i class="glyphicon glyphicon-search circular danger"></i>
												</a>
											</span>
                                    </div>
                                </div>
                            </form>
                            <c:if test="${empty add}">
                                <div class="widget-buttons ps-widget-buttons">
                                    <button class="btn btn-info btn-primary btn-xs" type="button" onclick="addPro()">
                                        <i class="fa fa-plus"></i>
                                        新建产品
                                    </button>
                                </div>
                            </c:if>
                        </div>
                        <div class=" padding-top-10 text-left " style="display:${empty product.creators ? 'none':'block'}">
                            <strong>创建人筛选:</strong>
                            <c:forEach items="${product.creators }" var="obj" varStatus="vs">
                                <span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('creator','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
                            </c:forEach>
                        </div>
                        <div class=" padding-top-10 text-left " style="display:${empty product.principals ? 'none':'block'}">
                            <strong>责任人筛选:</strong>
                            <c:forEach items="${product.principals }" var="obj" varStatus="vs">
                                <span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('principal','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
                            </c:forEach>
                        </div>
                        <div class=" padding-top-10 text-left " style="display:${empty product.managers ? 'none':'block'}">
                            <strong>产品经理筛选:</strong>
                            <c:forEach items="${product.managers }" var="obj" varStatus="vs">
                                <span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('manager','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
                            </c:forEach>
                        </div>
                    </div>
                    <c:choose>
                        <c:when test="${not empty list}">
                            <div class="widget-body">
                                <form action="/product/delPro" method="post" id="delForm">
                                    <input type="hidden" name="sid" value="${param.sid}" />
                                    <input type="hidden" id="redirectPage" name="redirectPage" />
                                    <div class="content_right" style="margin-left: 0;">
                                        <div class="widget">
                                            <div class="container-fluid ">
                                                <div class="row">
                                                    <c:forEach items="${list}" var="pro" varStatus="index">
                                                        <div class="col-lg-3 col-md-4 col-sm-6 child_product" >
                                                            <div class="product_des">
                                                                <img src="/static/assets/img/u631.png" width="48px">
                                                                <h4 style="font-weight: bolder;font-size: 18px;margin-bottom: 8px;">${pro.name}</h4>
                                                                <p title="${pro.description}" style="margin-top: 20px;">${pro.description}</p>
                                                            </div>
                                                            <div class="cz" style="padding-top: 10px;">
                                                                <p><span style="cursor: pointer;" onclick="viewTask('${pro.id}');">查看</span></p>
                                                                <span class="linY"></span>
                                                                <p style="font-size:20px;">
                                                                    <a href="javascript:void(0)" class="widget-caption blue padding-right-5"
                                                                      attentionState="${pro.attentionState}" busType="080" busId="${pro.id}" describe="0" iconSize="sizeLg"
                                                                      onclick="changeAtten('${param.sid}',this)" title="${pro.attentionState==0?"关注":"取消"}">
                                                                        <i class="fa ${pro.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
                                                                    </a>
                                                                </p>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <tags:pageBar url="/product/listPagedPro"></tags:pageBar>
                                </form>
                            </div>
                            <div style="clear:both"></div>
                        </c:when>
                        <c:otherwise>
                            <div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
                                <section class="error-container text-center">
                                    <h1>
                                        <i class="fa fa-exclamation-triangle"></i>
                                    </h1>
                                    <div class="error-divider">
                                        <c:choose>
                                            <c:when test="${param.searchTab == 11}">
                                                <h2>您还没有自己的产品哦！</h2>
                                                <a href="javascript:void(0);" onclick="addPro();" class="return-btn">
                                                    <i class="fa fa-plus"></i>
                                                    新建产品
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <h2>未查询到数据！</h2>
                                                <p class="description">协同提高效率，分享拉近距离。</p>
                                            </c:otherwise>
                                        </c:choose>
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
</body>
<script type="text/javascript">
    $(function() {

        $(".subform").Validform({
            tiptype : function(msg, o, cssctl) {
                validMsg(msg, o, cssctl);
            },
            showAllError : true
        });
    })
</script>
<style>
    .child_product{
        width:22.5%;
        margin-left:2%;
        margin-top:15px;
        margin-bottom:15px;
    }
</style>
</html>

