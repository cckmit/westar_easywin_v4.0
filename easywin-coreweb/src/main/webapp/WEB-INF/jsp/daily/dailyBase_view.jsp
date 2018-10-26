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
    <c:when test="${not empty daily}">
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
                                    ${dailyQ.dailyVal}
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
    <c:otherwise>
        未设置任何内容，请联系管理员设置
    </c:otherwise>
</c:choose>
<%--上传附件部分 --%>
<div class="widget radius-bordered">
    <div class="widget-header bg-bluegray no-shadow">
        <c:set var="fileNums"
               value="${fn:length(daily.dailyFiles)}"></c:set>
        <span class="widget-caption blue">分享附件(${fileNums})</span>
        <div class="widget-buttons btn-div-full">
            <a class="ps-point btn-a-full" data-toggle="collapse"> <i
                    class="fa fa-minus blue"></i> </a>
        </div>
    </div>
    <div class="widget-body no-shadow">
        <table class="display table table-bordered table-striped">
            <thead>
            <tr>
                <th width="10%">序号</th>
                <th>附件名称</th>
                <th width="20%">上传时间</th>
                <th width="10%" style="text-align:center;">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty daily.dailyFiles}">
                    <c:forEach items="${daily.dailyFiles}" var="upfile"
                               varStatus="vs">
                        <tr class="gradeX">
                            <td>${vs.count}</td>
                            <td>${upfile.fileName }</td>
                            <td>${upfile.upTime}</td>
                            <td align="center"><c:choose>
                                <c:when
                                        test="${upfile.fileExt=='doc' || upfile.fileExt=='docx' || upfile.fileExt=='xls' || upfile.fileExt=='xlsx' || upfile.fileExt=='ppt' || upfile.fileExt=='pptx'}">
                                    <a class="fa fa-download" style="padding-right: 10px"
                                       href="javascript:void(0)"
                                       onclick="downLoad('${upfile.fileUuid}','${upfile.fileName}','${param.sid }')"
                                       title="下载"></a>
                                    <a class="fa fa-eye" href="javascript:void(0)"
                                       onclick="viewOfficePage('${upfile.upfileId}','${upfile.fileUuid}','${upfile.fileName}','${upfile.fileExt}','${param.sid}','050','${daily.id}')"
                                       title="预览"> </a>
                                </c:when>
                                <c:when
                                        test="${upfile.fileExt=='txt'|| upfile.fileExt=='pdf'}">
                                    <a class="fa fa-download" style="padding-right: 10px"
                                       href="/downLoad/down/${upfile.fileUuid}/${upfile.fileName}?sid=${param.sid}"
                                       title="下载"></a>
                                    <a class="fa fa-eye" href="javascript:void(0)"
                                       onclick="viewOfficePage('${upfile.upfileId}','${upfile.fileUuid}','${upfile.fileName}','${upfile.fileExt}','${param.sid}','050','${daily.id}')"
                                       title="预览"> </a>
                                </c:when>
                                <c:when
                                        test="${upfile.fileExt=='jpg'||upfile.fileExt=='bmp'||upfile.fileExt=='gif'||upfile.fileExt=='jpeg'||upfile.fileExt=='png'}">
                                    <a class="fa fa-download" style="padding-right: 10px"
                                       href="/downLoad/down/${upfile.fileUuid}/${upfile.fileName}?sid=${param.sid}"
                                       title="下载"></a>
                                    <a class="fa fa-eye" href="javascript:void(0)"
                                       onclick="showPic('/downLoad/down/${upfile.fileUuid}/${upfile.fileName}','${param.sid}','${upfile.upfileId}','050','${daily.id}')"
                                       title="预览"></a>
                                </c:when>
                                <c:otherwise>
                                    <a class="fa fa-download" style="padding-right: 10px"
                                       onclick="downLoad('${upfile.fileUuid}','${upfile.fileName}','${param.sid }')"
                                       title="下载"></a>
                                </c:otherwise>
                            </c:choose></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr class="gradeX" align="center">
                        <td colspan="4">未上传附件</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>

</div>
<%--上传附件部分 结束--%>
</body>
</html>
