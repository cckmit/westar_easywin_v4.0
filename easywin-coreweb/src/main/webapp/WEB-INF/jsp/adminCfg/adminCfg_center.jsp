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
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title><%=SystemStrConstant.TITLE_NAME%></title>
<meta name="description" content="Dashboard" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- 框架样式 -->
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
	<script type="text/javascript" src="/static/js/weekJs/weekreport.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	<link rel="stylesheet" type="text/css" href="/static/css/weekreport.css" />
	<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	<script type="text/javascript">
        var pageTag = 'week';
        //周报模板设置
        function addWeekRepMod(){
            window.self.location = '/weekReport/addWeekRepModPage?sid=${param.sid}&searchTab=21';
        }
        //写周报
        function addWeekRep(){
            var url =  '/weekReport/addWeekRepPage?sid=${param.sid}';
            openWinByRight(url);
        }
        //周报查看权限验证
        function viewWeekReport(weekReportId){
            $.post("/weekReport/authorCheck?sid=${param.sid}",{Action:"post",weekReportId:weekReportId},
                function (msgObjs){
                    if(!msgObjs.succ){
                        showNotification(2,msgObjs.promptMsg);
                    }else{
                        var url = "/weekReport/viewWeekReport?sid=${param.sid}&id="+weekReportId;
                        openWinByRight(url);
                    }
                },"json");
        }
        //人员筛选
        function weekDoneFilter(ts,weekDoneState){
            var style=$(ts).attr("style");
            if(style){
                $("#weekDoneState").val('');
            }else{
                //选择了查询本周情况，就没有时间区间
                $("#startDate").val('');
                $("#endDate").val('');

                $("#weekDoneState").val(weekDoneState);
            }
            $("#weekForm").submit();
        }
        //人员筛选
        function userOneForUserIdCallBack(userId,tag){
            $("#owner_select").find("option").remove();
            var weekerType = $("#weekerType").val()
            if(weekerType && weekerType==0){
                $("#weekerType").val('');
            }
            $("#weekerId").val(userId);
            $("#weekForm").submit();
        }
        //部门筛选
        function depOneCallBack(orgIdElementId, orgPathNameElementId,orgId,orgName){
            $("#"+orgIdElementId).val(orgId);
            $("#"+orgPathNameElementId).val(orgName);
            $("#weekForm").submit();
        }
        //筛选栏筛选选项初始化
        $(document).ready(function(){
            //更多筛选字样显示
            if(${(not empty weekReport.startDate || not empty weekReport.endDate)}){
                $("#moreFilterCondition").html("隐藏");
            }
        });
        $(function(){
            //监督人员设置
            $("body").on("click","#forceInBtn",function(){
                forceIn('${param.sid}',$(this),'006')
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
            $("#weekName").bind("keydown",function(event){
                if(event.keyCode == "13")
                {
                    if(!strIsNull($("#weekName").val())){
                        $("#weekForm").submit();
                    }else{
                        $("#weekName").focus();
                    }
                }
            });
            $("#weekName").blur(function(){
                $("#weekForm").submit();
            });
            //文本框绑定回车提交事件
            $("#weekNum").bind("keydown",function(event){
                if(event.keyCode == "13")
                {
                    if($("#weekNum").val()){
                        if(isPInt($("#weekNum").val())){
                            $("#searchForm").submit();
                        }else{
                            layer.tips("请填写正确的数字",$("#weekNum"),{tips: [2, '#FF3030']});
                        }
                    }else{
                        $("#weekNum").focus();
                    }
                }
            });
            $("#weekNum").blur(function(){
                if(!$("#weekNum").val()||isPInt($("#weekNum").val())){
                    $("#searchForm").submit();
                }else{
                    layer.tips("请填写正确的数字",$("#weekNum"),{tips: [2, '#FF3030']});
                }
            });
        });
        //选择日期
        function selectDate(){
            //选择时间就没有查询本周汇报情况
            $("#weekDoneState").val('');
            $("#weekForm").submit();
        }

        //防止重复添加
        function addWeekReport(weekS){
            var url = '/weekReport/addWeekRepPage?sid=${param.sid}&chooseDate='+encodeURIComponent(weekS);
            openWinByRight(url);
        }


        function depMoreCallBack(orgid,img){
            $("#depMore"+ orgid).html('');
            $("#depMore"+ orgid).append(img);
        }
        //查询周报范围
        function searchWeekTab(index){
            layer.closeAll('loading');
            //启动加载页面效果
            layer.load(0, {shade: [0.6,"#fff"]});
            $("#searchTab").val(index);

            if(index==11){//全部范围
                $("#weekerType").val('');
            }else if(index==12){//自己的汇报
                $("#weekerType").val('0');
            }else if(index==13){//下属汇报
                $("#weekerType").val('1');
            }
            $("#weekForm").submit();
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
                content: ["/weekReport/addSubTimeSetPage?sid="+sid,'no'],
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
<c:choose>
	<c:when test="${param.activityMenu=='m_4.1'}">
		<!-- 考勤 -->
		<script type="text/javascript" src="/static/js/attenceJs/attence.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	</c:when>
	<c:when test="${param.activityMenu=='m_4.2'}">
		<!-- 节假日 -->
		<script type="text/javascript" src="/static/js/festModJs/festMod.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	</c:when>
</c:choose>
<script type="text/javascript">
	var sid="${param.sid}";//申明一个sid全局变量
	var PageData={"sid":"${param.sid}",
			"attenceRule":{"attenceRuleId":"${attenceRule.id}",
				"isSystem":"${attenceRule.isSystem}"},
				"activityMenu":"${param.activityMenu}"
			};
	$(function(){
	 //editFormMod(44)
	})
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
			<jsp:include page="adminCfg_menu.jsp"></jsp:include>
			<!-- 列表-->
			<jsp:include page="${toPage}"></jsp:include>
		</div>
	</div>
	<!--主题颜色设置按钮-->
	<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
</body>
</html>

