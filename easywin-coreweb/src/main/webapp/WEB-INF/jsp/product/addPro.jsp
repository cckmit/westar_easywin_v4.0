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
    <jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
    <script src="/static/js/proJs/proOpt.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
    <link href="/static/product/assets/css/product.css" rel="stylesheet" type="text/css">
    <script type="text/javascript">
        var sid="${param.sid}";
        var pageParam= {
            "sid":"${param.sid}",
            "comId":"${userInfo.comId}"
        }
        var EasyWin = {
            "ewebMaxMin":"self"
        };
        //关闭窗口
        function closeWin(){
            var winIndex = window.top.layer.getFrameIndex(window.name);
            closeWindow(winIndex);
        }
        $(function() {
            //名称筛选
            // $("#name").blur(function(){
            //     //启动加载页面效果
            //     layer.load(0, {shade: [0.6,'#fff']});
            //     $("#searchForm").submit();
            // });
            //文本框绑定回车提交事件
            $("#name").bind("keydown",function(event){
                if(event.keyCode == "13")
                {
                    if(!strIsNull($("#name").val())){
                        //启动加载页面效果
                        layer.load(0, {shade: [0.6,'#fff']});
                        $("#searchForm").submit();
                    }else{
                        showNotification(1,"请输入检索内容！");
                        $("#name").focus();
                    }
                }
            });

            //产品产品经理员信息设置
            $("body").on("click",".userSelectBtn",function(){
                taskOptForm.chooseUser($(this));
            })
            // 验证与验证产品名称相似的产品
            <%--$("#name").keyup(function(){--%>
                <%--if(!strIsNull($("#name").val())){--%>
                    <%--$.post("/task/checkSimiTaskByname?sid=${param.sid}",{Action:"post",name:$("#name").val()},--%>
                        <%--function (taskNum){--%>
                            <%--if(taskNum > 0){--%>
                                <%--$("#addTaskWarn").html("<a href='javascript:void(0);' style='background:none;width:80px;color:#ff0000;' onclick='similarTasksPage();'>相似产品("+taskNum+")</a>");--%>
                            <%--}else{--%>
                                <%--$("#addTaskWarn").text("");--%>
                            <%--}--%>
                        <%--});--%>
                <%--}else{--%>
                    <%--$("#addTaskWarn").text("");--%>
                <%--}--%>
            <%--});--%>
            $(".subform").Validform({
                tiptype : function(msg, o, cssctl) {
                    validMsgV2(msg, o, cssctl);
                },
                datatype:{
                    "input":function(gets,obj,curform,regxp){
                        var str = $(obj).val();
                        if(str){
                            var count = str.replace(/[^\x00-\xff]/g,"**").length;
                            var len = $(obj).attr("defaultLen");
                            if(count>len){
                                return "产品名称太长";
                            }else{
                                return true;
                            }
                        }else{
                            return false;
                        }
                    }
                },
                callback:function (form){
                    //提交前验证是否在上传附件
                    return sumitPreCheck(null);
                },
                showAllError : true
            });
            $("#addTask").click(function(){
                var name = $("#name").val();
                if(strIsNull(name)){
                    layer.tips('请填写产品名称！', "#name", {tips: 1});
                    return false;
                }
                var count = name.replace(/[^\x00-\xff]/g,"**").length;
                if(count>52){
                    layer.tips('产品名称太长！', "#name", {tips: 1});
                    return false;
                }
                //提交权限标识
                var sign = 0;
                //检查产品经理
                var manager = $("#manager").val();
                if(null != manager && "" != manager){
                    sign = sign | 1;
                }

                //检查负责人
                var principal = $("#principal").val();
                if(null != principal && "" != principal){
                    sign = sign | 2;
                }

                //检查产品描述
                var mark = document.getElementById("eWebEditor1").contentWindow.getHTML();
                if(mark && mark.trim() != ""){
                    sign = sign | 4;
                }

                //检查各自权限
                if(sign == 7){
                    //添加并查看
                    $(".subform").submit();
                }else{
                    if((sign & 1) != 1){
                        layer.tips("请选择产品经理员",$("#managers"),{"tips":1});
                    }else{
                        if((sign & 2) != 2){
                            layer.tips("请选择负责人",$("#principals"),{"tips":1});
                        }else{
                            if((sign & 4) != 4){
                                layer.tips("请填写产品描述",$("#eWebEditor1"),{"tips":1});
                            }
                        }
                    }
                }
            });

            //输入文本框双击删除绑定
            $("body").on("dblclick",".colInput",function(){
                $(".subform [name='busType']").val("");
                $(".subform [name='busId']").val("");
                var actObj = $(this);
                $(actObj).parents(".relativeRow[busType='"+$(actObj).attr("busType")+"']").remove();
            });
        })
        /*
        *设置模块
        */
        function changeTab(ts){
            var busType = $(ts).val();
            if(busType=='0'){//没有选择模块
                $("#itemMod").css("display","none");
                $("#crmMod").css("display","none");

                $("#busId").val(0)
                $("#busName").val('')
            }else if(busType=='005'){//选择产品模块
                $("#itemMod").css("display","block");
                $("#crmMod").css("display","none");

                $("#busId").val($("#itemId").val());
                $("#busName").val($("#itemName").val());
            }else if(busType=='012'){
                $("#itemMod").css("display","none");
                $("#crmMod").css("display","block");

                $("#busId").val($("#crmId").val());
                $("#busName").val($("#crmName").val());
            }
        }
        function formSub(){
            $(".subform").submit();
        }
        $(document).ready(function(){
            $("#name").focus();
        });
    </script>
</head>
<body>
<form class="subform" method="post" action="/product/addPro">
    <tags:token></tags:token>
    <input type="hidden" name="attentionState" id="attentionState" value="0"/>
    <input type="hidden" name="way" id="way">
    <input type="hidden" name="busId"/>
    <input type="hidden" name="busType"/>
    <div class="container" style="padding: 0px 0px;width: 100%">
        <div class="row" style="margin: 0 0">
            <div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
                <div class="widget" style="margin-top: 0px;">
                    <div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">产品添加</span>
                        <div class="widget-buttons ps-toolsBtn">
                             <a href="javascript:void(0)" class="blue" id="addTask">
                                 <i class="fa fa-save"></i>添加
                             </a>
                        </div>
                        <div class="widget-buttons">
                            <a href="javascript:void(0)" onclick="closeWin()" title="关闭">
                                <i class="fa fa-times themeprimary"></i> </a>
                        </div>
                    </div>
                    <!--Widget Header-->
                    <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
                    <div class="widget margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                        <div class="widget-body">
                            <div class="widget no-header ">
                                <div class="widget-body bordered-radius">
                                    <div class="tickets-container tickets-bg tickets-pd">
                                        <ul class="tickets-list">
                                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                                <div class="pull-left task-left-text task-height">
                                                    产品名称<span style="color: red">*</span>：
                                                </div>
                                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                                    <div class="row">
                                                        <input id="name" datatype="input" name="name" type="text" defaultLen="52" class="form-control" placeholder="产品名称"
                                                               nullmsg="请填写产品名称" onpropertychange="handleName()" onkeyup="handleName()"/>
                                                    </div>
                                                </div>
                                                <div class="pull-left">
                                                    <span id="msgTitle" class="task-height padding-left-20">(0/26)</span>
                                                </div>
                                                <script>
                                                    //当状态改变的时候执行的函数
                                                    function handleName(){
                                                        var value = $('#name').val();
                                                        var len = charLength(value);
                                                        if(len%2==1){
                                                            len = (len+1)/2;
                                                        }else{
                                                            len = len/2;
                                                        }
                                                        if(len>26){
                                                            $('#msgTitle').html("(<font color='red'>"+len+"</font>/26)");
                                                        }else{
                                                            $('#msgTitle').html("("+len+"/26)");
                                                        }
                                                    }
                                                    //firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。
                                                    if(/msie/i.test(navigator.userAgent)){    //ie浏览器
                                                        document.getElementById('name').onpropertychange=handleName
                                                    }else {//非ie浏览器，比如Firefox
                                                        document.getElementById('name').addEventListener("input",handleName,false);
                                                    }
                                                </script>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="widget no-header">
                                <div class="widget-body bordered-radius">
                                    <div class="task-describe clearfix">
                                        <div class="tickets-container tickets-bg tickets-pd clearfix">
                                            <ul class="tickets-list clearfix padding-top-10">
                                                <li class="no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
                                                    <div class="pull-left task-left-text task-height">
                                                        产品经理<span style="color: red">*</span>：
                                                    </div>
                                                    <div class="ticket-user pull-left ps-right-box">
                                                        <div style="width: 100px;display:none;">
                                                            <input type="hidden" id="manager" name="manager" value="">
                                                        </div>
                                                        <div class="pull-left" id="managers" style="max-width: 100px"></div>
                                                        <a href="javascript:void(0);" class="margin-top-7 btn btn-primary btn-xs userSelectBtn pull-left"
                                                           relateSelect="manager" relateImgDiv="managers" title="人员选择" style="margin-left:15px;"><i class="fa fa-plus"></i>选择</a>
                                                    </div>
                                                </li>
                                                <li class="no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
                                                    <div class="pull-left task-left-text task-height">
                                                        负责人<span style="color: red">*</span>：
                                                    </div>
                                                    <div class="ticket-user pull-left ps-right-box">
                                                        <div style="width: 100px;display:none;">
                                                            <input type="hidden" id="principal" name="principal" value="">
                                                        </div>
                                                        <div class="pull-left" id="principals" style="max-width: 100px"></div>
                                                        <a href="javascript:void(0);" class="margin-top-7 btn btn-primary btn-xs userSelectBtn"
                                                           relateSelect="principal" relateImgDiv="principals"
                                                           title="人员选择"  style="margin-left:15px;"><i class="fa fa-plus"></i>选择</a>
                                                    </div>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="widget no-header">
                                <div class="widget-body bordered-radius">
                                    <div class="task-describe clearfix">
                                        <div class="tickets-container tickets-bg tickets-pd clearfix">
                                            <ul class="tickets-list clearfix padding-top-10">
                                                <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
                                                    <div class="pull-left task-left-text task-height">产品类型：</div>
                                                    <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
                                                        <div class="row">
                                                            <tags:dataDic type="proType" name="type" id="proType" value="1"></tags:dataDic>
                                                        </div>
                                                    </div>
                                                </li>
                                                <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
                                                    <div class="pull-left task-left-text task-height">版本号：</div>
                                                    <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
                                                        <div class="row">
                                                            <input id="version" datatype="input" name="version" type="text" defaultLen="52" class="form-control" placeholder="产品版本号"
                                                                  readonly="readonly " value="1.0"/>
                                                        </div>
                                                    </div>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="widget no-header ">
                                <div class="widget-body bordered-radius">
                                    <div class="tickets-container tickets-bg tickets-pd">
                                        <ul class="tickets-list">
                                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                                <div class="pull-left task-left-text">
                                                    产品说明<span style="color: red">*</span>：
                                                </div>
                                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                                    <div class="row">
				                                        <textarea id="description" name="description" rows="" cols="" class="form-control"
                                                                  style="width:650px;height: 110px;display:none;"></textarea>
                                                        <iframe ID="eWebEditor1" src="/static/plugins/ewebeditor/ewebeditor.htm?id=description&style=expand600"
                                                                frameborder="0" scrolling="no" width="650" height="280"></iframe>
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="widget no-header ">
                                <div class="widget-body bordered-radius">
                                    <div class="tickets-container tickets-bg tickets-pd">
                                        <ul class="tickets-list">
                                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                                <div class="pull-left task-left-text task-height">
                                                    附件：
                                                </div>
                                                <div class="pull-left col-lg-4 col-sm-4 col-xs-4">
                                                    <div class="row">
                                                        <tags:uploadMore name="listUpfiles.id" showName="fileName"
                                                                         ifream="" comId="${userInfo.comId}"></tags:uploadMore>
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript">var jq11 = $.noConflict(true);</script>
<script src="/static/assets/js/bootstrap.min.js"></script>

<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
