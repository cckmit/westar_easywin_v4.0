<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<style type="text/css">
    .table-hover :hover a{ color:#0186e1;}
</style>
<body>
<div class="page-content">
    <div class="page-body">
        <div class="row">
            <div class="col-md-12 col-xs-12 ">
                <div class="widget">
                    <div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                        <div>
                            <form action="/statistics/platform/statisticAttence" id="searchForm" class="subform">
                                <input type="hidden" name="sid" value="${param.sid}">
                                <input type="hidden" name="pager.pageSize" value="15">
                                <input type="hidden" name="activityMenu" value="${param.activityMenu}">
                                <input type="hidden" name="searchTab" value="${param.searchTab}">
                                <div class="btn-group pull-left searchCond">

                                    <div class="table-toolbar ps-margin">
                                        <div class="btn-group">
                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">人员筛选<i class="fa fa-angle-down"></i>
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
                                        <select list="listCreator" listkey="id" listvalue="userName" id="creator_select" name="listCreator.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
                                            <c:forEach items="${attenceRecord.listCreator }" var="obj" varStatus="vs">
                                                <option selected="selected" value="${obj.id }">${obj.userName }</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="table-toolbar ps-margin">
                                        <div class="btn-group">
                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">部门筛选<i class="fa fa-angle-down"></i>
                                            </a>
                                            <ul class="dropdown-menu dropdown-default">
                                                <li>
                                                    <a href="javascript:void(0)" class="clearMoreElement" relateList="dep_select">不限条件</a>
                                                <li>
                                                    <a href="javascript:void(0)" class="depMoreElementSelect" relateList="dep_select">部门选择</a>
                                            </ul>
                                        </div>
                                    </div>
                                    <div style="float: left;width: 250px;display: none">
                                        <select list="listDep" listkey="id" listvalue="depName" id="dep_select" name="listDep.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
                                            <c:forEach items="${attenceRecord.listDep }" var="obj" varStatus="vs">
                                                <option selected="selected" value="${obj.id }">${obj.depName }</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="ps-margin ps-search searchCond padding-right-10">
                                        <span class="pull-left padding-top-5">查询日期：</span>
                                        <input class="form-control dpd2  Wdate" type="text" style="width:120px;height:30px" readonly="readonly" value="${attenceRecord.startDate}" id="startDate" name="startDate" placeholder="查询日期"
                                               onFocus="WdatePicker({onpicked:function(){getSubmit();},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
                                    </div>
                                    <div class="ps-margin ps-search searchCond padding-right-20">
                                        <span class="pull-left padding-top-5 padding-right-5">至</span>
                                        <input class="form-control dpd2  Wdate" type="text" style="width:120px;height:30px" readonly="readonly" value="${attenceRecord.endDate}" id="endDate" name="endDate" placeholder="查询日期"
                                               onFocus="WdatePicker({onpicked:function(){getSubmit();},dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startDate\',{d:+0});}',maxDate:'${nowDate}'})" />
                                    </div>
                                </div>
                            </form>
                            <div class="widget-buttons ps-widget-buttons">
                                <%-- <c:if test="${isForceIn }">
                                    <button class="btn btn-info btn-primary btn-xs" type="button" onclick="addAttence('${param.sid}');">同步考勤记录</button>
                                    <button class="btn btn-info btn-primary btn-xs" type="button" onclick="addAttenceUser('${param.sid}');">同步考勤人员</button>
                                </c:if> --%>
                            </div>
                            <div class="padding-top-10 text-left " style="display:${empty attenceRecord.listCreator ? 'none':'block'}">
                                <strong>考勤人筛选:</strong>
                                <c:forEach items="${attenceRecord.listCreator }" var="obj" varStatus="vs">
                                    <span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('creator','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
                                </c:forEach>
                            </div>

                            <div class=" padding-top-10 text-left " style="display:${empty attenceRecord.listDep ? 'none':'block'}">
                                <strong>考勤部门筛选:</strong>
                                <c:forEach items="${attenceRecord.listDep }" var="obj" varStatus="vs">
                                    <span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('dep','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.depName }</span>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                    <c:choose>
                        <c:when test="${not empty listUserInfo}">
                            <div class="widget-body">
                                <table class="table table-bordered" id="editabledatatable">
                                    <thead>
                                    <tr role="row">
                                        <th class="text-center">序号</th>
                                        <th class="text-center">所属部门</th>
                                        <th class="text-center">人员</th>
                                        <th class="text-center">编号</th>
                                        <th class="text-center">请假次数</th>
                                        <th class="text-center">请假时长(H)</th>
                                        <th class="text-center">加班次数</th>
                                        <th class="text-center">加班时长(H)</th>
                                        <th class="text-center">旷工次数</th>
                                        <th class="text-center">迟到次数</th>
                                        <th class="text-center">早退次数</th>
                                        <th class="text-center">异常次数</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${listUserInfo}" var="userInfoVo" varStatus="vs">
                                        <tr class="optTr">
                                            <td class="text-center">${vs.count}</td>
                                            <td class="text-center">${userInfoVo.depName}</td>
                                            <td class="text-center">${userInfoVo.userName}</td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${not empty userInfoVo.enrollNumber}">${userInfoVo.enrollNumber}</c:when>
                                                    <c:otherwise>
                                                        <span style="color:red;">无编号</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${userInfoVo.leaveTimeT >0}">
                                                        <a style="color:red;" href="javascript:void(0)" class="viewListLeave" userid='${userInfoVo.id}' username='${userInfoVo.userName}'>${userInfoVo.leaveTimeT}</a>
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${userInfoVo.leaveTotal != 0.0}">
                                                        <a style="color:red;" href="javascript:void(0)" class="viewListLeave" userid='${userInfoVo.id}' username='${userInfoVo.userName}'>${userInfoVo.leaveTotal}</a>
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${userInfoVo.overTime.overTimeT >0}">
                                                        <a style="color:green;" href="javascript:void(0)" class="viewListOverTime" userid='${userInfoVo.id}' username='${userInfoVo.userName}'>${userInfoVo.overTime.overTimeT}</a>
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${not empty userInfoVo.overTime.overTimeTotal}">
                                                        <a style="color:green;" href="javascript:void(0)" class="viewListOverTime" userid='${userInfoVo.id}' username='${userInfoVo.userName}'>${userInfoVo.overTime.overTimeTotal}</a>
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${userInfoVo.absentTime >0}">
                                                        <a style="color:red;" href="javascript:void(0)"  onclick="listAttenceRecord('${userInfoVo.id}')">${userInfoVo.absentTime}</a>
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${userInfoVo.lateTime >0}">
                                                        <a style="color:red;" href="javascript:void(0)"  onclick="listAttenceRecord('${userInfoVo.id}')">${userInfoVo.lateTime}</a>
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>

                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${userInfoVo.leaveEarlyTime >0}">
                                                        <a style="color:red;" href="javascript:void(0)"  onclick="listAttenceRecord('${userInfoVo.id}')">${userInfoVo.leaveEarlyTime}</a>
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${userInfoVo.unusualTime >0}">
                                                        <a style="color:red;" href="javascript:void(0)" onclick="listAttenceRecord('${userInfoVo.id}')">${userInfoVo.unusualTime}</a>
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <tags:pageBar url="/statistics/platform/statisticAttence"></tags:pageBar>
                                    <%--<input type="hidden" name="sid" value="${param.sid}">--%>
                                    <%--<input type="hidden" name="pager.pageSize" value="15">--%>
                                    <%--<input type="hidden" name="activityMenu" value="${param.activityMenu}">--%>
                                    <%--<input type="hidden" name="searchTab" value="${param.searchTab}">--%>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
                                <section class="error-container text-center">
                                    <h1>
                                        <i class="fa fa-exclamation-triangle"></i>
                                    </h1>
                                    <div class="error-divider">
                                        <h2>没有相关数据！</h2>
                                        <p class="description">协同提高效率，分享拉近距离。</p>
                                    </div>
                                </section>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
<style type="text/css">
    #infoList table{
        table-layout: fixed;
    }
    #infoList td,#infoList th{
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
    }
</style>
</body>
</html>
