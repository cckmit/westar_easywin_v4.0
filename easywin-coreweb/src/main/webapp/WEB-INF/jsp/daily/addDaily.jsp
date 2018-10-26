<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
        contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
        errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <title><%=SystemStrConstant.TITLE_NAME%></title>
    <!-- 框架样式 -->
    <jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
    <script type="text/javascript" src="js/ewebeditor_core.js?v=110"> </script>
    <link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
    <script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript" src="/static/js/selfGroupTree.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
    <script type="text/javascript" src="/static/js/jquery.form.js"></script>
    <!-- 自动补全js -->
    <script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>

    <script type="text/javascript">
        //var winWidth = 0;
        var winHeight = 0;
        //获取窗口宽度
        //if (window.innerWidth)
        //winWidth = window.innerWidth;
        //else if ((document.body) && (document.body.clientWidth))
        //winWidth = document.body.clientWidth;
        //获取窗口高度
        if (window.innerHeight)
            winHeight = window.innerHeight;
        else if ((document.body) && (document.body.clientHeight))
            winHeight = document.body.clientHeight;
        //通过深入Document内部对body进行检测，获取窗口大小
        if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth)
        {
            winHeight = document.documentElement.clientHeight;
//winWidth = document.documentElement.clientWidth;
        }
        var leftHeight = $("#indexLeft").height();
        $("#indexRight").css("height",((winHeight>leftHeight?winHeight:leftHeight)-30)+"px")
        /**
         * 初始化分享范围
         */
        function initSelfGroupTree(selfGroupStr){
            var setting = {
                check: {
                    enable: true,
                    chkboxType: {"Y":"", "N":""}
                },
                view: {
                    dblClickExpand: false
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    beforeClick: beforeClick,
                    onCheck: onCheck
                }
            };
            initZTree(setting,"treeDemo","scopeTypeSel","idType","menuContent",selfGroupStr);
        }

        window.onload = function(){
            console.log(${selfGroupStr});
            initSelfGroupTree(${selfGroupStr});
            //聚焦时设置设置宽一点
            $("#content").focus(function(){
                $(this).css("height","150px");
            })
            //失焦时恢复宽度设置
            $("#content").blur(function(){
                if(strIsNull($(this).val())){
                    $(this).css("height","30px");
                }
            })

        }


        //打开页面body
        var openWindowDoc;
        //打开页面,可调用父页面script
        var openWindow;
        //打开页面的标签
        var openPageTag;
        //注入父页面信息
        function setWindow(winDoc, win) {
            openWindowDoc = winDoc;
            openWindow = win;
            openPageTag = openWindow.pageTag;
        }

        //关闭窗口
        function closeWin(){
            var winIndex = window.top.layer.getFrameIndex(window.name);
            closeWindow(winIndex);
        }

        var val;
        $(function() {

            //设置滚动条高度
            var height = $(window).height() - 40;
            $("#contentBody").css("height", height + "px");

            $(".subform").Validform({
                tiptype : function(msg, o, cssctl) {
                    validMsg(msg, o, cssctl);
                },
                callback : function(form) {
                    return sumitPreCheck(null);
                },
                showAllError : true
            });

            //汇报内容
            $("#dailyContentMenuLi").click(function(){
                $("#otherIframe").css("display","none");
                $("#dailyContent").css("display","block");
                $(this).parent().parent().find("li").removeAttr("class");;
                $(".optViewDaily").parent().show()
                $(this).parent().attr("class","active");
            });
            $("#dailyTalk").click(function() {
                $("#otherIframe").css("display","block");
                $("#dailyContent").css("display","none");
                $(this).parent().parent().find("li").removeAttr(
                    "class");
                $(this).parent().attr("class", "active");
                $(".optViewDaily").parent().hide()
                //留言
                $("#otherIframe").attr("src","/daily/dailyTalkPage?sid=${param.sid}&pager.pageSize=10&dailyId=${daily.id}")
            });
            $("#dailyLog").click(function() {
                        $("#otherIframe").css("display","block");
                        $("#dailyContent").css("display","none");
                        $(this).parent().parent().find("li").removeAttr(
                            "class");
                        $(this).parent().attr("class", "active")
                        $(".optViewDaily").parent().hide()
                        //分享日志
                        $("#otherIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${daily.id}&busType=050&ifreamName=otherIframe");
                    });
            $("#dailyFile")
                .click(
                    function() {
                        $("#otherIframe").css("display","block");
                        $("#dailyContent").css("display","none");
                        $(this).parent().parent().find("li").removeAttr(
                            "class");
                        $(this).parent().attr("class", "active")
                        $(".optViewDaily").parent().hide()
                        //分享附件
                        $("#otherIframe")
                            .attr(
                                "src",
                                "/daily/dailyFilePage?sid=${param.sid}&pager.pageSize=10&dailyId=${daily.id}")
                    });

            //查看记录
            $("#dailyViewRecord")
                .click(
                    function() {
                        $("#otherIframe").css("display","block");
                        $("#dailyContent").css("display","none");
                        $(this).parent().parent().find("li").removeAttr(
                            "class");
                        ;
                        $("#dailyViewRecord").parent().attr("class",
                            "active");
                        $(".optViewDaily").parent().hide()
                        $("#otherIframe")
                            .attr(
                                "src",
                                "/common/listViewRecord?sid=${param.sid}&busId=${daily.id}&busType=050&ifreamName=otherIframe");
                    });
        })
        $(document).ready(function() {
            $(".taskarea").autoTextarea({
                minHeight : 55,
            });
            $(".taskareaAdd").autoTextarea({
                minHeight : 55,
            });
            //验证选择日期与入职日期是否冲突
            $("#chooseDate").data("isDayOk",{"status":"f","info":"正在后台验证分享发布时间"}).status='f'
            $("#chooseDate").data("isDayOk").info='正在后台验证分享发布时间';
            var chooseDate = '${chooseDate}';
            checkDailyDate(chooseDate,function(data){
                if(data.status=='y'){
                    var isDayOk = {"status":"yes"};
                    $("#chooseDate").data("isDayOk",isDayOk);
                }else{
                    showNotification(2,data.info);
                    var isDayOk = {"status":"no","info":data.info};
                    $("#chooseDate").data("isDayOk",isDayOk);
                }
            })


            checkLeader('${param.sid}',function(data){
                if(data.status=='f'){
                    showNotification(2,data.info);
                }else if(data.status=='f1'){
                    window.top.layer.confirm("直属上级未设定", {
                        title : "警告",
                        closeBtn:0,
                        move:false,
                        btn : [ "设定", "取消" ],
                        icon : 0
                    }, function(index) {
                        window.top.layer.close(index);
                        if(openPageTag=='daily'){
                            $(openWindowDoc).find("#dailySetBtn").click();
                        }else{
                            openWindow.setDailyViewScope('${param.sid}');
                        }
                    },function(index){
                        window.top.layer.close(index);
                        var winIndex = window.top.layer.getFrameIndex(window.name);
                        closeWindow(winIndex);
                    });

                }
            })
        });
        //编辑分享
        function editDaily(state) {
            if (state == 1) {//修改界面
                $(".addDaily").show();
                $(".iframe-width").css("width","650");
                $(".viewDaily").hide();
                $("#workTrace").css("display", "block")

                $(".optAddDaily").css("display", "block");
                $(".optViewDaily").css("display", "none");

                //显示范围选择
                $("#groupSelect").show();

                //隐藏范围显示
                $("#groupListView").hide();

            } else if (state == 0) {//不修改了
                $(".addDaily").hide();
                $(".iframe-width").css("width","730");
                $(".viewDaily").show();
                $("#workTrace").css("display", "none")

                $(".optAddDaily").css("display", "none");
                $(".optViewDaily").css("display", "block");

                //隐藏范围选择
                $("#groupSelect").hide();

                //显示使用的范围
                $("#groupListView").show();
            }
        }
        //保存分享 1是存草稿 0是发布
        function save(state, ts) {
            //若是一个都没有填写，不能发布
            if (state == 0) {
                //是否能发布，默认不能
                var flag = true;

                //检查必填选项是否有内容
                $.each(document.getElementsByClassName("form-control"),function(index,obj){
                    //获取元素id
                    var id = $(obj).attr("id");
                    //获取隐藏了必填标识的inputid
                    var requireId = id.substring(5);

                    //获取该元素是否有内容
                    var text = $("#"+id).val();
                    var isRequire = $("#isRequire_" + requireId).val();

                    //判断是否必填并且没有填写内容
                    if(isRequire == 1 && strIsNull(text)){
                        //清理滚动条，并再滚动
                        window.location.hash = "";
                        window.location.hash = "#" + id;
                        layer.tips("该条目为必填，请填写内容！",$("#" + id),{tips:1});
                        flag = false ;
                        return false;
                    }
                });

                //是否选择了范围
                var scope = $("#scopeTypeSel").val();
                if(!scope){
                    layer.tips("请选择查看范围！",$("#scopeTypeSel"),{tips:1});
                    flag = false ;
                    return false;
                }

                var areas = $(".addDaily").find("textarea[isRequire='1']");
                if(areas && areas.get(0)){
                    $.each(areas,function(index,obj){
                        var val = $(obj).val();
                        if(!val || val.replace(/\s+/g, "").length == 0){
                            scrollToLocation($("#contentBody"),$(obj))
                            layer.tips("请填写内容！",$(obj),{tips:1});
                            $(obj).focus();
                            flag = false ;
                            return false;
                        }
                    })
                }
                //今日计划信息
                var plans = $("#dailyPlan").find("textarea");
                if(plans && plans.get(0) && flag){
                    $.each(plans,function(index,obj){
                        var val = $(obj).val();
                        if(!val || val.replace(/\s+/g, "").length == 0){
                            scrollToLocation($("#contentBody"),$(obj))
                            layer.tips("请填写今日计划！",$(obj),{tips:1});
                            $(obj).focus();
                            flag = false ;
                            return false;
                        }
                    })
                }
                var isDayOk = $("#chooseDate").data("isDayOk");
                if(isDayOk.status=='yes'){
                }else if(isDayOk.status=='no'){
                    showNotification(2,isDayOk.info);
                    return;
                }else{
                    showNotification(2,isDayOk.info);
                    return;
                }
                if (!flag) {
                    return false;
                    window.top.layer.confirm("分享未填写！<br>不能发布！<br>确定保存？", {
                        title : "询问框",
                        btn : [ "确定", "取消" ],
                        icon : 3
                    }, function(index) {
                        window.top.layer.close(index);
                        $("#state").attr("value", 1);
                        // $(ts).attr("disabled", "disabled")
                        $("#dailyForm").submit();
                    });
                } else {
                    checkLeader('${param.sid}',function(data){
                        if(data.status=='f'){
                            showNotification(2,data.info);
                        }else if(data.status=='f1'){
                            window.top.layer.confirm("直属上级未设定", {
                                title : "警告",
                                btn : [ "设定", "取消" ],
                                icon : 0
                            }, function(index) {
                                window.top.layer.close(index);
                                if(openPageTag=='daily'){
                                    $(openWindowDoc).find("#dailySetBtn").click();
                                }else{
                                    openWindow.setDailyViewScope('${param.sid}');
                                }
                            });

                        }else{
                            $("#state").attr("value", 0);
                            // $(ts).attr("disabled", "disabled");
                            $("#dailyForm").submit();
                        }
                    });
                }
            } else {//存草稿无所谓填没填
                $("#state").attr("value", 1);
                $(ts).attr("disabled", "disabled")
                $("#dailyForm").submit();
            }

        }
        function addDaily(ts) {
            if(openPageTag=='daily'){
                openWindow.addDailyS($(ts).val());
            }else{
                window.location.replace('/daily/addDailyPage?sid=${param.sid}&chooseDate='+encodeURIComponent($(ts).val()));
            }
        }
        //验证分享日期与入职日期是否冲突
        function checkDailyDate(chooseDate,callback){
            $.ajax({
                type: "POST",
                dataType: "json",
                url: "/daily/checkDailyDate?sid=${param.sid}",
                data:{"chooseDateStr":chooseDate},
                success: function(data){
                    //若是有回调
                    if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
                        callback(data);
                    }
                }
            })
        }
    </script>
</head>
<body>
<div class="container" style="padding: 0px 0px;width: 100%">
    <div class="row" style="margin: 0 0">
        <div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            <div class="widget no-margin-bottom" style="margin-top: 0px;">
                <div class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption">
                            <strong style="float:left;font-size:15px;margin-right:10px;"> <a>${daily.userName} </a> ${chooseDate} 分享 </strong>
                            <input type="hidden" id="nowDate" value="${nowDate}">
                            <input id="chooseDate" type="text" name="chooseDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'nowDate\',{d:-0});}',onpicked:function(){addDaily(this)}})"
                                   style="cursor:pointer;width: 86px;height:23px;padding-left: 8px;float:left;margin-top: 6px;" readonly="readonly" value="${chooseDate}"/>
                            <div id="groupSelect" style="float:left;margin-top: 6px;margin-left:20px;${(daily.countVal<=0 || daily.state==1)?'display:block':'display:none' }">
                                <p style="float:left;" class="blue">范围：</p>
                                <input id="scopeTypeSel" type="text" readonly="readonly" value="${scopeTypeSel}" style="width:150px;height:23px;float:left;" onclick="showMenu();" />
                                <div id="menuContent" style="display:none; position: absolute;top:30px;left: 280px;z-index: 999">
                                    <ul id="treeDemo" class="ztree" style="clear:both;margin-top:0;z-index:1;background-color:#f0f0f0;width: 150px;padding-bottom: 0px"></ul>
                                    <ul id="addGrpUl" class="ztree" style="z-index:1;background-color:#f0f0f0;width: 150px;padding-bottom: 10px;">
                                        <li style="text-align: center;margin-top: 5px;color: #1c98dc;cursor: pointer;" onclick="addGrpForTree('scopeTypeSel','menuContent','idType','treeDemo','${param.sid}')">
                                            +添加分组
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <div id="groupListView" style="float:left;margin-top: 6px;margin-left:20px;${(daily.countVal>0 && daily.state==0)?'display:block':'display:none' }">
                                <p style="float:left;" class="blue">范围：</p>
                                <p style="float:left;">${empty scopeTypeSel ? ((empty daily.grpNameStr) ? daily.scopeStr : daily.grpNameStr) : scopeTypeSel}</p>
                            </div>
                        </span>
                    <div class="widget-buttons ps-toolsBtn">
                        <c:choose>
                            <c:when test="${daily.countVal>0 && daily.state==0}">
                                <!--添加 -->
                                <div class="optAddDaily"
                                     style="clear:both;text-align: center;display: none">
                                    <input type="button" class="btn btn-primary btn-sm "
                                           onclick="save(0,this);" value="发布分享" />
                                    <c:if test="${daily.state==1}">
                                        <input type="button" class="btn btn-warning btn-sm"
                                               onclick="save(1,this);" value="存草稿" />
                                    </c:if>
                                    <c:if test="${daily.state==0}">
                                        <input type="reset" class="btn btn-default btn-sm"
                                               onclick="editDaily(0);" value="取消" />
                                    </c:if>


                                    <c:set var="display">
                                        <c:choose>
                                            <c:when
                                                    test="${not empty daily && (daily.state!=0 || (daily.countVal==0 && daily.state==0))}">
                                                block
                                            </c:when>
                                            <c:otherwise>
                                                none
                                            </c:otherwise>
                                        </c:choose>
                                    </c:set>
                                </div>
                                <c:if test="${empty editAuthor}">
                                    <div class="optViewDaily"
                                         style="clear:both;text-align: center;">
                                        <input type="button" class="btn btn-warning btn-sm"
                                               onclick="editDaily(1)" value="修改分享" />
                                    </div>
                                </c:if>


                            </c:when>
                            <c:otherwise>
                                <!-- 查看 -->
                                <div class="optAddDaily"
                                     style="clear:both;text-align: center;">
                                    <input type="button" class="btn btn-primary btn-sm "
                                           onclick="save(0,this);" value="发布分享" />
                                    <c:if test="${daily.state==1}">
                                        <input type="button" class="btn btn-warning btn-sm"
                                               onclick="save(1,this);" value="存草稿" />
                                    </c:if>
                                    <c:if test="${daily.state==0}">
                                        <input type="reset" class="btn btn-default btn-sm"
                                               onclick="editDaily(0);" value="取消" />
                                    </c:if>

                                    <c:set var="display">
                                        <c:choose>
                                            <c:when
                                                    test="${not empty daily && (daily.state!=0 || (daily.countVal==0 && daily.state==0))}">
                                                block
                                            </c:when>
                                            <c:otherwise>
                                                none
                                            </c:otherwise>
                                        </c:choose>
                                    </c:set>
                                </div>
                                <c:if test="${empty editAuthor}">
                                    <div class="optViewDaily"
                                         style="clear:both;text-align: center;display: none">
                                        <input type="button" class="btn btn-warning date-set btn-sm"
                                               onclick="editDaily(1)" value="修改分享" />
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="widget-buttons">
                        <a href="javascript:void(0)" onclick="closeWin()" title="关闭">
                            <i class="fa fa-times themeprimary"></i> </a>

                    </div>
                </div>
                <!--Widget Header-->
                <c:choose>
                    <c:when test="${daily.state==0}">
                        <!-- 已经汇报 -->
                        <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
                        <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;padding:0 12px;">
                            <div class="widget-body no-shadow"
                                 style="display:${daily.state==0?'block':'none' }">
                                <div class="widget-main ">
                                    <div class="tabbable">
                                        <ul class="nav nav-tabs tabs-flat">
                                            <li class="active">
                                                <a href="javascript:void(0)" id="dailyContentMenuLi" data-toggle="tab">汇报内容</a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)" id="dailyTalk" data-toggle="tab">留言<c:if test="${daily.talkNum > 0}"><span style="color:red;font-weight:bold;">（${daily.talkNum}）</span></c:if></a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)" data-toggle="tab" id="dailyLog">分享日志</a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)" data-toggle="tab" id="dailyFile">分享文档<c:if test="${daily.fileNum > 0}"><span style="color:red;font-weight:bold;">（${daily.fileNum}）</span></c:if></a>
                                            </li>
                                            <li>
                                                <a data-toggle="tab" href="javascript:void(0)" id="dailyViewRecord">阅读情况</a>
                                            </li>
                                        </ul>
                                        <div class="tab-content tabs-flat">
                                            <div id="dailyContent" style="display:block;">
                                                <jsp:include page="./daily_edit.jsp"></jsp:include>
                                            </div>
                                            <iframe id="otherIframe" style="display:none;" class="layui-layer-load"
                                                    src="/daily/dailyTalkPage?sid=${param.sid}&pager.pageSize=10&dailyId=${daily.id}"
                                                    border="0" frameborder="0" allowTransparency="true" noResize
                                                    scrolling="no" width=100% height=100% vspale="0"></iframe>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- 未汇报 -->
                        <jsp:include page="./daily_edit.jsp"></jsp:include>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>

