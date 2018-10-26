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
<body>
<c:choose>
    <c:when test="${empty daily}">
        <div class="error-container text-center" style="padding-top: 150px">
            <h1>
                <i class="fa fa-exclamation-triangle"></i>
            </h1>
            <div class="error-divider">
                <h2>请联系团队管理人员,设置分享模板</h2>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <!-- 已经汇报 -->
        <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
        <div class="widget-body margin-top-${daily.state==0?10:40}" id="contentBody"
             style="overflow: hidden;overflow-y:scroll;">
            <form method="post" id="dailyForm" class="subform"
                  action="/daily/addDaily">
                <input type="hidden" id="num" value="${fn:length(daily.dailyPlans)}" />
                <input type="hidden" id="id" name="id" value="${daily.id}" />
                <input type="hidden" name="sid" value="${param.sid}" />
                <input type="hidden" id="state" name="state" value="${daily.state}" />
                <input type="hidden" id="idType" name="idType" value="${idType}"/>
                <div class="addDaily"
                     style="${(daily.state!=0 || (daily.countVal==0 && daily.state==0))?'display:block':'display:none' }">
                            <c:choose>
                                <c:when test="${not empty daily}">
                                    <%--条目 --%>
                                    <c:forEach var="dailyQ" items="${daily.dailyQs}"
                                               varStatus="vs">
                                        <div class="widget radius-bordered">
                                            <div class="widget-header bg-bluegray no-shadow">
                                                <span class="widget-caption blue">
                                                    <label for="xsinput">
                                                        ${dailyQ.dailyName}
                                                    </label>
                                                    <c:if test="${dailyQ.isRequire eq '1'}">
                                                        <font style="color: red;">*</font>
                                                    </c:if>
                                                </span>
                                            </div>
                                            <div class="no-header ">
                                                <div class="bordered-radius">
                                                    <div class="tickets-container tickets-bg tickets-pd">
                                                        <ul class="tickets-list">
                                                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7" style="width: 100%">
                                                                    <div class="row">
                                                                        <input type="hidden" id="isRequire_${vs.count}" value="${dailyQ.isRequire}" />
				                                        				<textarea id="qVal_${vs.count}" name="qVal" rows="" cols="" class="form-control" style="height:80px;">${empty dailyQ.dailyVal || dailyQ.dailyVal eq '未填写' ? "" : dailyQ.dailyVal}</textarea>
                                                                    </div>
                                                                </div>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                    <%--分享填写结束 --%>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        <%--上传附件部分 --%>
                    <div class="widget radius-bordered">
                        <div class="widget-header bg-bluegray no-shadow">
                            <span class="widget-caption blue">分享附件</span>
                        </div>
                        <div class="widget-body no-shadow">
                            <div style="clear:both" class="ws-process">
                                <div id="thelistdailyFiles_upfileId" style="width: 300px">
                                    <c:choose>
                                        <c:when test="${not empty daily.dailyFiles}">
                                            <c:forEach items="${daily.dailyFiles}" var="upfile"
                                                       varStatus="vs">
                                                <div id="wu_file_0_-${upfile.upfileId}"
                                                     class="uploadify-queue-item">
                                                    <div class="cancel">
                                                        <a href="javascript:void(0)" fileDone="1"
                                                           fileId="${upfile.upfileId}">X</a>
                                                    </div>
                                                    <span class="fileName" title="${upfile.fileName}"> <tags:cutString
                                                            num="25">${upfile.fileName}</tags:cutString>(已有文件) </span> <span
                                                        class="data"> - 完成</span>
                                                    <div class="uploadify-progress">
                                                        <div class="uploadify-progress-bar" style="width: 100%;"></div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                    </c:choose>
                                </div>
                                <div class="btns btn-sm">
                                    <div id="pickerdailyFiles_upfileId">选择文件</div>
                                </div>
                                <script type="text/javascript">
                                    loadWebUpfiles('dailyFiles_upfileId','${param.sid}','','pickerdailyFiles_upfileId','thelistdailyFiles_upfileId','filedailyFiles_upfileId');
                                </script>
                                <div
                                        style="position: relative; width: 350px; height: 90px;display: none">
                                    <div style="float: left;">
                                        <select list="dailyFiles" listkey="upfileId"
                                                listvalue="fileName" id="dailyFiles_upfileId"
                                                name="dailyFiles.upfileId"
                                                ondblclick="removeClick(this.id)" multiple="multiple"
                                                moreselect="true" style="width: 100%; height: 90px;">
                                            <c:choose>
                                                <c:when test="${not empty daily.dailyFiles}">
                                                    <c:forEach items="${daily.dailyFiles}"
                                                               var="upfile" varStatus="vs">
                                                        <option selected="selected" value="${upfile.upfileId}">${upfile.fileName}</option>
                                                    </c:forEach>
                                                </c:when>
                                            </c:choose>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                        <%--上传附件部分 结束--%>
                </div>
                    <%--分享查看 --%>
                <div class="viewDaily" style="${(daily.countVal>0 && daily.state==0)?'display:block':'display:none' }">
                    <jsp:include page="./dailyShare_view.jsp"></jsp:include>
                    </div>
                    <%--分享结束 --%>
            </form>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>
