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
    <script src="/static/js/taskJs/taskOpt.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
    <link href="/static/assets/css/task.css" rel="stylesheet" type="text/css">
    <script type="text/javascript">
        var sid="${param.sid}";
        var pageParam= {
            "sid":"${param.sid}"
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
            //属于该角色的人员信息设置
            $("body").on("click",".userIdSelectBtn",function(){
                taskOptForm.chooseExectors($(this));
            })

            //设置滚动条高度
            var height = $(window).height()-40;
            $("#contentBody").css("height",height+"px");

            $("#roleName").keydown(function(event){
                if(event.keyCode==13) {
                    return false;
                }
            });
            //列出常用人员信息
            listUsedUser(5,function(data){
                if(data.status=='y'){
                    var usedUser = data.usedUser;
                    $.each(usedUser,function(index,userObj){
                        //添加头像
                        var headImgDiv = $('<div class="ticket-user pull-left other-user-box"></div>');
                        $(headImgDiv).data("userObj",userObj);
                        var imgObj = $('<img src="/downLoad/userImg/'+userObj.comId+'/'+userObj.id+'" class="margin-left-5 usedUserImg"/>')
                        var headImgName = $('<span class="user-name2" style="font-size:6px;display:inline-block"></span>');
                        $(headImgName).html(userObj.userName);

                        $(headImgDiv).append($(imgObj));
                        $(headImgDiv).append($(headImgName));

                        $("#usedUserDiv").append($(headImgDiv));

                        $(headImgDiv).on("click",function(){
                            var relateSelect = $(".userIdSelectBtn").attr("relateSelect");
                            var relateImgDiv = $(".userIdSelectBtn").attr("relateImgDiv");
                            taskOptForm.appendUsedUser(userObj,relateSelect,relateImgDiv)
                        })

                    })
                }
            });

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
                                return "角色名称太长";
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
            $("#addRole").click(function(){
                var roleName = $("#roleName").val();
                if(strIsNull(roleName)){
                    layer.tips('请填写角色名称！', "#roleName", {tips: 1});
                    return false;
                }
                var count = roleName.replace(/[^\x00-\xff]/g,"**").length;
                if(count>52){
                    layer.tips('角色名称太长！', "#roleName", {tips: 1});
                    return false;
                }
                //提交权限标识
                var sign = 0;
                //检查角色人
                var executes = $("#roleBindingUsers_userId").find("option");
                if(executes && executes.get(0)){
                    sign = sign | 1;
                }
                //检查角色描述
                var mark = $("#roleRemark").val()
                if(mark && mark.trim() != ""){
                    sign = sign | 2;
                }
                //检查各自权限
                if(sign == 3){
                    //添加并查看
                    $(".subform").submit();
                }else{
                    if((sign & 1) != 1){
                        layer.tips("请选择角色人员",$(".userIdSelectBtn"),{"tips":1});
                    }else{
                        if((sign & 2) != 2){
                            layer.tips("请填写角色描述",$("#roleRemark"),{"tips":1});
                        }
                    }
                }
            });
        })
        $(document).ready(function(){
            $("#roleName").focus();
        });
    </script>
</head>
<body>
<form class="subform" method="post" action="/role/addRole">
    <tags:token></tags:token>
    <input type="hidden" name="attentionState" id="attentionState" value="0"/>
    <input type="hidden" name="way" id="way">
    <input type="hidden" name="busId"/>
    <input type="hidden" name="busType"/>
    <div class="container" style="padding: 0px 0px;width: 100%">
        <div class="row" style="margin: 0 0">
            <div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
                <div class="widget" style="margin-top: 0px;">
                    <div
                            class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">角色添加</span>
                        <div class="widget-buttons ps-toolsBtn">
                             <a href="javascript:void(0)" class="blue" id="addRole"> <i
                                class="fa fa-save"></i>添加 </a>
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
                                                    名称<span style="color: red">*</span>：
                                                </div>
                                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                                    <div class="row">
                                                        <input id="roleName" datatype="input" name="roleName" type="text" defaultLen="52" class="form-control" placeholder="角色名称"
                                                               nullmsg="请填写角色名称" onpropertychange="handleName()" onkeyup="handleName()"/>
                                                    </div>
                                                </div>
                                                <div class="pull-left">
                                                    <span id="msgTitle" class="task-height padding-left-20">(0/26)</span>
                                                </div>
                                                <script>
                                                    //当状态改变的时候执行的函数
                                                    function handleName(){
                                                        var value = $('#roleName').val();
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
                                                        document.getElementById('roleName').onpropertychange=handleName
                                                    }else {//非ie浏览器，比如Firefox
                                                        document.getElementById('roleName').addEventListener("input",handleName,false);
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
                                                <li class="no-shadow clearfix ticket-normal pull-left col-lg-12 col-sm-12 col-xs-12">
                                                    <div class="pull-left task-left-text task-height">
                                                        设定角色人员<span style="color: red">*</span>：
                                                    </div>
                                                    <div class="ticket-user pull-left ps-right-box">
                                                        <div style="width: 250px;display:none;">
                                                            <select datatype="*" list="roleBindingUsers" listkey="userId" listvalue="userName"
                                                                    id="roleBindingUsers_userId" name="roleBindingUsers" ondblclick="removeClick(this.id)" multiple="multiple"
                                                                    moreselect="true" style="width: 100%; height: 100px;">
                                                            </select>
                                                        </div>
                                                        <div class="pull-left" id="roleBindingUsers_userIdDiv" style="max-width: 450px"></div>
                                                        <div class="ps-clear"></div>
                                                        <a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userIdSelectBtn"
                                                           relateSelect="roleBindingUsers_userId" relateImgDiv="roleBindingUsers_userIdDiv"
                                                           title="人员选择"  style="float: left;"><i class="fa fa-plus"></i>选择</a>
                                                        <div id="usedUserDiv" style="width: 450px;display: inline-block;">
                                                            <span class="pull-left" style="padding-top:8px;display: inline-block;padding-left: 10px">常用人员:</span>
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
                                                    描述<span style="color: red">*</span>：
                                                </div>
                                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                                    <div class="row">
				                                        <textarea id="roleRemark" name="roleRemark" rows="" cols="" class="form-control"
                                                                  style="width:650px;height: 110px;"></textarea>
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
