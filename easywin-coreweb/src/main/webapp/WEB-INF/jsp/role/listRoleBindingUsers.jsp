<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
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
    <title><%=SystemStrConstant.TITLE_NAME %></title>
    <jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
    <script src="/static/js/role/role.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
        //关闭窗口
        function closeWin(){
            var winIndex = window.top.layer.getFrameIndex(window.name);
            closeWindow(winIndex);
        }
        $(function() {
            //设置滚动条高度
            var height = $(window).height()-40;
            $("#contentBody").css("height",height+"px");
        });

        $(function() {
            //属于该角色的人员信息设置
            $("body").on("click",".userIdSelectBtn",function(){
                taskOptForm.chooseExectors($(this),'${param.sid}',${id});
            })
        })
        $(document).ready(function(){
            $("#roleName").focus();
            var relateSelect = $(".userIdSelectBtn").attr("relateSelect");
            $(".online-list").on("dblclick",function(){
                var userId = $(this).attr("userId");
                $(this).remove();
                $("#"+relateSelect).find("option[value='"+userId+"']").remove();

                $.ajax({
                    type : "post",
                    url : "/role/deleteBindingUser",
                    data : {userId:userId,roleId:${id},sid:'${param.sid}'},
                    success : function (data) {
                        showNotification(1,"已删除!");
                    }
                });
            })
        });
    </script>
</head>
<body style="background-color:#fff;">
<div class="container" style="padding: 0px 0px;width: 100%">
    <div class="row" style="margin: 0 0">
        <div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            <div class="widget no-margin-bottom" style="margin-top: 0px;">
                <div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                    <span class="widget-caption themeprimary" style="font-size: 20px">用户列表</span>
                    <div class="widget-buttons">
                        <a href="javascript:void(0)" onclick="closeWin()" title="关闭"> <i class="fa fa-times themeprimary"></i>
                        </a>
                    </div>
                </div>
                <!--Widget Header-->
                <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
                <div class="widget-body margin-top-40 padding-left-30" id="contentBody" style="overflow: hidden;">
                    <div class="widget radius-bordered">
                        <div class="row no-margin">
                            <div class="ticket-user pull-left ps-right-box">
                                <div style="width: 250px;display:none;">
                                    <select datatype="*" list="roleBindingUsers" listkey="userId" listvalue="userName"
                                            id="roleBindingUsers_userId" name="roleBindingUsers" ondblclick="removeClick(this.id)" multiple="multiple"
                                            moreselect="true" style="width: 100%; height: 100px;">
                                        <c:forEach items="${users}" var="u">
                                            <option value="${u.userId}" selected="selected">${u.userName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="pull-left" id="roleBindingUsers_userIdDiv" style="max-width: 450px">
                                    <c:forEach items="${users}" var="u">
                                        <div class="online-list " style="float:left;padding-top:5px" title="双击移除" userId="${u.userId}">
                                            <img src="/downLoad/userImg/${u.comId}/${u.userId}" class="user-avatar">
                                            <span class="user-name">${u.userName}</span>
                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="ps-clear"></div>
                                <a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userIdSelectBtn"
                                   relateSelect="roleBindingUsers_userId" relateImgDiv="roleBindingUsers_userIdDiv"
                                   title="人员选择"  style="float: left;"><i class="fa fa-plus"></i>选择</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
