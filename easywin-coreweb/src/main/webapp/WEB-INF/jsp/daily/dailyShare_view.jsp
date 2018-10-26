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
    <c:when test="${not empty daily && daily.countQues != 1}">
        <%--条目 --%>
        <c:forEach var="dailyQ" items="${daily.dailyQs}" varStatus="vs">
            <div class="widget radius-bordered">
                <div class="widget-header bg-bluegray no-shadow">
                    <span class="widget-caption blue"><label for="xsinput">${dailyQ.dailyName}</label></span>
                </div>
                <div class="widget-body no-shadow">
                    <div class="form-group">
                        <div class="ws-zbContent" style="padding-left: 15px">
                            <c:choose>
                                <c:when test="${not empty dailyQ.dailyVal}">
                                   <tags:viewTextArea> ${dailyQ.dailyVal}</tags:viewTextArea>
                                </c:when>
                                <c:otherwise>
                                    <font color="red">未填写</font>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
        <%--分享内容结束 --%>
    </c:when>
    <c:when test="${daily.countQues == 1}">
        <c:forEach items="${daily.dailyQs}" var="dailyQ">
            <c:choose>
                <c:when test="${dailyQ.sysState != 1}">
                    <div class="widget radius-bordered">
                        <div class="widget-header bg-bluegray no-shadow">
                            <span class="widget-caption blue"><label for="xsinput">${dailyQ.dailyName}</label></span>
                        </div>
                        <div class="widget-body no-shadow">
                            <div class="form-group">
                                <div class="ws-zbContent" style="padding-left: 15px">
                                    <c:choose>
                                        <c:when test="${not empty dailyQ.dailyVal}">
                                            <tags:viewTextArea>${dailyQ.dailyVal}</tags:viewTextArea>
                                        </c:when>
                                        <c:otherwise>
                                            <font color="red">未填写</font>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="widget radius-bordered">
                        <div class="widget-body no-shadow">
                            <div class="collapse in">
                                <div class="form-group">
                                        <%--<label for="xsinput" style="font-weight: bold;"> 分享内容 </label>
                                        --%>
                                    <div class="padding-left-20" style="word-wrap: break-word; word-break: break-all;">
                                        <c:forEach var="dailyQ" items="${daily.dailyQs}" varStatus="vs">
                                            <tags:viewTextArea>${dailyQ.dailyVal}</tags:viewTextArea>
                                        </c:forEach>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:when>
    <c:otherwise>
        未设置任何内容，请联系管理员设置
    </c:otherwise>
</c:choose>
</body>
</html>
