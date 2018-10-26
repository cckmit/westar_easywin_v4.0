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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
    <title><%=SystemStrConstant.TITLE_NAME %></title>
    <!-- 框架样式 -->
    <jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
    <script type="text/javascript" src="/static/js/dailyJs/daily.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
    <c:if test="${param.searchTab==61}">
        <link rel="stylesheet" type="text/css" href="/static/css/daily.css" />
    </c:if>
    <c:if test="${param.searchTab==51 || param.searchTab==52 || param.searchTab==53}">
        <link rel="stylesheet" type="text/css" href="/static/plugins/dailyrepline/css/about.css" />
    </c:if>
    <script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
    <script type="text/javascript">
        var pageTag = 'daily';
        var pageParam = {
            "nowYear":"${nowYear}",
            "nowMonth":"${nowMonth}",
            "nowDailyNum":"${nowDailyNum}",
            "searchTab":"${empty param.searchTab?'51':param.searchTab}",
            "userInfo":{
                "id":${userInfo.id}
            }
        }
        //分享模板设置
        function addDailyMod(){
            window.self.location = '/daily/dailyModConf?sid=${param.sid}&searchTab=61';
        }
        //写分享
        function addDaily(){
            var url =  '/daily/addDailyPage?sid=${param.sid}';
            openWinByRight(url);
        }
        //分享查看权限验证
        function viewDaily(dailyId){
            $.post("/daily/authorCheck?sid=${param.sid}",{Action:"post",dailyId:dailyId},
                function (msgObjs){
                    if(!msgObjs.succ){
                        showNotification(2,msgObjs.promptMsg);
                    }else{
                        var url = "/daily/viewDaily?sid=${param.sid}&id="+dailyId;
                        openWinByRight(url);
                    }
                },"json");
        }
        //人员筛选
        function dailyDoneFilter(ts,dailyDoneState){
            var style=$(ts).attr("style");
            if(style){
                $("#dailyDoneState").val('');
            }else{
                //选择了查询本日情况，就没有时间区间
                $("#startDate").val('');
                $("#endDate").val('');

                $("#dailyDoneState").val(dailyDoneState);
            }
            $("#dailyForm").submit();
        }
        //人员筛选
        function userOneForUserIdCallBack(userId,tag){
            $("#owner_select").find("option").remove();
            var dailyerType = $("#dailyerType").val();
            if(dailyerType && dailyerType==0){
                $("#dailyerType").val('');
            }
            $("#dailyerId").val(userId);
            $("#dailyForm").submit();
        }
        //部门筛选
        function depOneCallBack(orgIdElementId, orgPathNameElementId,orgId,orgName){
            $("#"+orgIdElementId).val(orgId);
            $("#"+orgPathNameElementId).val(orgName);
            $("#dailyForm").submit();
        }
        //筛选栏筛选选项初始化
        $(document).ready(function(){
            //更多筛选字样显示
            if(${(not empty daily.startDate || not empty daily.endDate)}){
                $("#moreFilterCondition").html("隐藏");
            }
        });
        $(function(){
            //监督人员设置
            $("body").on("click","#forceInBtn",function(){
                forceIn('${param.sid}',$(this),'050')
            });
            //更多筛选条件显示层
            $("#moreFilterCondition").click(function(){
                var display = $("#moreFilterCondition_div").css("display");
                if("none"==display){
                    $(this).html('隐藏');
                    $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
                }else if("block"==display){
                    $(this).html('更多筛选')
                    $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
                }

            });
            //文本框绑定回车提交事件
            $("#dailyName").bind("keydown",function(event){
                if(event.keyCode == "53")
                {
                    if(!strIsNull($("#dailyName").val())){
                        $("#dailyForm").submit();
                    }else{
                        $("#dailyName").focus();
                    }
                }
            });
            $("#dailyName").blur(function(){
                $("#dailyForm").submit();
            });
            //文本框绑定回车提交事件
            $("#dailyNum").bind("keydown",function(event){
                if(event.keyCode == "53")
                {
                    if($("#dailyNum").val()){
                        if(isPInt($("#dailyNum").val())){
                            $("#searchForm").submit();
                        }else{
                            layer.tips("请填写正确的数字",$("#dailyNum"),{tips: [2, '#FF3030']});
                        }
                    }else{
                        $("#dailyNum").focus();
                    }
                }
            });
            $("#dailyNum").blur(function(){
                if(!$("#dailyNum").val()||isPInt($("#dailyNum").val())){
                    $("#searchForm").submit();
                }else{
                    layer.tips("请填写正确的数字",$("#dailyNum"),{tips: [2, '#FF3030']});
                }
            });
        });
        //选择日期
        function selectDate(){
            //选择时间就没有查询本日汇报情况
            $("#dailyDoneState").val('');
            $("#dailyForm").submit();
        }

        //防止重复添加
        function addDaily(dailyS){
            var url = '/daily/addDailyPage?sid=${param.sid}&chooseDate='+encodeURIComponent(dailyS);
            openWinByRight(url);
        }

        function depMoreCallBack(orgid,img){
            $("#depMore"+ orgid).html('');
            $("#depMore"+ orgid).append(img);
        }
        //查询分享范围
        function searchDailyTab(index){
            layer.closeAll('loading');
            //启动加载页面效果
            layer.load(0, {shade: [0.6,"#fff"]});
            $("#searchTab").val(index);

            if(index==51){//全部范围
                $("#dailyerType").val('');
            }else if(index==52){//自己的汇报
                $("#dailyerType").val('0');
            }else if(index==53){//下属汇报
                $("#dailyerType").val('1');
            }
            $("#dailyForm").submit();
        }
        function subTimeSet(sid,ts,preActiveObj){
            var preActive = preActiveObj;
            if(!preActive){
                //当前活动选项
                preActive = $(".submenu").find(".active");
                //当前活动选项移除背景色
                $(preActive).removeClass();
                //配置设置背景色
                $(ts).parent().addClass("active bg-themeprimary");
            }
            layer.open({
                type: 2,
                title:false,
                closeBtn:0,
                //title: [title, 'font-size:18px;'],
                area: ['500px', '300px'],
                fix: false, //不固定
                maxmin: false,
                scrollbar:false,
                content: ["/daily/addSubTimeSetPage?sid="+sid,'no'],
                btn: ['确定', '关闭'],
                yes: function(index, layero){
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    iframeWin.formSub();
                    return false;
                },btn2:function(index){
                },cancel: function(){
                },end:function(index){
                    //配置删除背景色
                    $(ts).parent().removeClass();//恢复前一个选项的背景色
                    $(preActive).addClass("active bg-themeprimary");
                }
            });
        }
    </script>
</head>
<body>
<!-- 系统头部装载 -->
<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<!-- 数据展示区域 -->
<div class="main-container container-fluid">
    <!-- Page Container -->
    <div class="page-container">
        <!-- 大条件 -->
        <jsp:include page="/WEB-INF/jsp/daily/listDaily_left.jsp"></jsp:include>
        <c:choose>
            <c:when test="${param.searchTab==61}">
                <!-- 配置-->
                <jsp:include page="/WEB-INF/jsp/daily/dailyModConf.jsp"></jsp:include>
            </c:when>
            <c:when test="${param.searchTab==54}">
                <!-- 统计-->
                <jsp:include page="/WEB-INF/jsp/daily/listDailyStatistics.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <!-- 列表-->
                <jsp:include page="/WEB-INF/jsp/daily/listDaily_middle.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>
    </div>
    <!--主题颜色设置按钮-->
    <jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
</div>
</body>
<c:if test="${empty param.searchTab || param.searchTab==51 || param.searchTab==52 || param.searchTab==53}">
    <script type="text/javascript" src="/static/js/dailyJs/daily_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
</c:if>
</html>

