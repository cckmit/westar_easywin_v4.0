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
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title><%=SystemStrConstant.TITLE_NAME%></title>
    <meta name="description" content="Dashboard" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
    <c:if test="${not empty recycleTab}">
        <script type="text/javascript" src="/static/js/recyleJs/recycleBin.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
    </c:if>
    <script type="text/javascript" src="/static/js/proJs/proCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
    <script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
</head>
<script type="text/javascript">

    //session标识
    var sid = '${param.sid}';
    //默认显示界面标识
    var pageTag = 'myPro';
    var searchTab = '${param.searchTab}';
    if(searchTab==12){
        pageTag="attPro";
    }else if(searchTab==13){
        pageTag="priPro";
    }else if(searchTab==14){
        pageTag="manPro";
    }else if(searchTab==15){
        pageTag="allPro";
    }

    //默认当前第1页
    var nowPageNum = 0;
    //默认总待办数为0
    var countTodo=0;

    var pageLoc = location.search.match(/pager.offset=(\d+)/);
    if(pageLoc){
        //计算当前页数
        nowPageNum = pageLoc[1]/10;
    }
    var tabIndex;
    var sessionUserId = ${userInfo.id};

    /**
     * 返回意见
     */
    //产品查看权限验证
    function viewTask(id){
        $.post("/product/authorCheck?sid=${param.sid}",{Action:"post",id:id},
            function (result){
                if(result.code == 0){
                    showNotification(2,result.msg);
                }else{
                    var url="/product/viewProPage?sid=${param.sid}&id="+id;
                    openWinByRight(url);
                }
            },"json");
    }
</script>
<body>
<!-- 系统头部装载 -->
<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<!-- 数据展示区域 -->
<div class="main-container container-fluid">
    <!-- Page Container -->
    <div class="page-container">
        <!-- 大条件 -->
        <jsp:include page="/WEB-INF/jsp/product/proCenter_left.jsp"></jsp:include>
        <c:choose>
            <c:when test="${not empty recycleTab }">
                <jsp:include page="/WEB-INF/jsp/recycleBin/listPagedPreDel.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <!-- 列表-->
                <jsp:include page="/WEB-INF/jsp/product/listPagedPro.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>

        <!-- 列表-->
    </div>
</div>
<!--主题颜色设置按钮-->
<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
</body>
</html>

