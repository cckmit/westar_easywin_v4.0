<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

</head>
<body>
<!-- Page Content -->
<div class="page-content">
    <!-- Page Body -->
    <div class="page-body">
        <div class="row">
            <div class="col-md-12 col-xs-12 ">
                <div class="widget">
                    <div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                        <div class="btn-group pull-left">
                            <div class="table-toolbar ps-margin">
                                <div class="btn-group">
                                    <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="roleEnable()"> 角色启用 </a>
                                </div>
                            </div>
                            <div class="table-toolbar ps-margin">
                                <div class="btn-group">
                                    <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="dis()"> 角色禁用 </a>
                                </div>
                            </div>
                            <div class="table-toolbar ps-margin">
                                <div class="btn-group">
                                    <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="deleteDis()"> 角色删除 </a>
                                </div>
                            </div>
                            <c:if test="${userInfo.admin==1}">
                                <div class="table-toolbar ps-margin">
                                    <div class="btn-group">
                                        <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="grant()"> 管理员授权 </a>
                                    </div>
                                </div>
                                <div class="table-toolbar ps-margin">
                                    <div class="btn-group">
                                        <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="revoke()"> 回收管理权限 </a>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        <form action="/role/listPagedRole" id="searchForm">
                            <input type="hidden" name="sid" value="${param.sid }" />
                            <input type="hidden" name="tab" value="${param.tab}" />
                            <input type="hidden" name="pager.pageSize" value="10" />
                            <div class="ps-margin ps-search">
									<span class="input-icon">
										<input id="roleName" name="roleName" value="${role.roleName}" class="form-control ps-input" type="text" placeholder="请输入关键字">
										<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
									</span>
                            </div>
                        </form>
                        <div class="widget-buttons ps-widget-buttons">
                            <button class="btn btn-info btn-primary btn-xs margin-left-5" type="button" name="busMapFlowCfg" onclick="addRole('${param.sid}')"><i class="fa fa-plus"></i>添加角色</button>
                        </div>
                    </div>
                    <c:choose>
                    <c:when test="${not empty list}">
                    <div class="widget-body">
                        <form method="post" id="delForm">
                            <input type="hidden" name="sid" value="${param.sid }" />
                            <input type="hidden" name="redirectPage" />
                            <input type="hidden" name="ids" value="" id="singleDis">
                            <table class="table table-striped table-hover" style="text-align: center;">
                                <thead>
                                <tr>
                                    <c:choose>
                                        <c:when test="${userInfo.admin>0}">
                                            <th width="5%" height="45">
                                                <label>
                                                    <input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')" />
                                                    <span class="text"></span>
                                                </label>
                                            </th>
                                        </c:when>
                                        <c:otherwise>
                                            <th width="8%" height="40">
                                                <h5>序号</h5>
                                            </th>
                                        </c:otherwise>
                                    </c:choose>
                                    <th width="10%" height="40" style="text-align: center;">
                                        <h5>团队号</h5>
                                    </th>
                                    <th width="15%" height="40" style="text-align: center;">
                                        <h5>角色名称</h5>
                                    </th>
                                    <th  height="40" style="text-align: center;">
                                        <h5>描述</h5>
                                    </th>
                                    <th width="10%" height="40" style="text-align: center;">
                                        <h5>启用禁用</h5>
                                    </th>
                                    <th width="10%" height="40" style="text-align: center;">
                                        <h5>删除</h5>
                                    </th>
                                    <th width="10%" height="40" style="text-align: center;">
                                        <h5>操作</h5>
                                    </th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach items="${list}" var="role" varStatus="vs">
                                    <tr class="optTr">
                                        <c:choose>
                                            <c:when test="${userInfo.admin > 0 }">
                                                <c:if test="${userInfo.admin==1}">
                                                    <%--超级管理员可以选中除自己的任何人 --%>
                                                    <td class="optTd" style="height: 47px">
                                                        <label class="optCheckBox" style="display: none;width: 20px">
                                                            <input class="colored-blue" type="checkbox" name="ids" value="${role.id}"
                                                                   enabled="${role.enable}" id="checkBox_${role.id}"/>
                                                            <span class="text"></span>
                                                        </label>
                                                        <label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
                                                    </td>
                                                </c:if>
                                                <c:if test="${userInfo.admin==2}">
                                                    <%--普通管理员可以选中普通人员 --%>
                                                    <td class="optTd" style="height: 47px">
                                                        <label class="optCheckBox" style="display: none;width: 20px">
                                                            <input class="colored-blue" type="checkbox" name="ids" value="${role.id}"
                                                                   enabled="${role.enable}" />
                                                            <span class="text"></span>
                                                        </label>
                                                        <label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
                                                    </td>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <td style="height: 47px">
                                                    <label style="display: block;width: 20px">${vs.count}</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                        <td>${role.comId}</td>
                                        <td>
                                            <a href="javascript:void(0)" onclick="view(${role.id})">
                                                    ${role.roleName}
                                            </a>
                                        </td>
                                        <td>
                                                ${role.roleRemark}
                                        </td>
                                        <td>
                                            <c:if test="${role.enable==1}">
                                                <span style="color:red;cursor: pointer;" onclick="singleDis(${role.id})">禁用</span>
                                            </c:if> <c:if test="${role.enable==0}">
                                                <span style="color:green;cursor: pointer;" onclick="doSingleRoleEnable(${role.id})">启用</span>
                                            </c:if>
                                        </td>
                                        <td class="td-pointer">
                                            <span style="color:red;cursor: pointer;" onclick="deleteSingleDis(${role.id})">删除</span>
                                        </td>
                                        <td>
                                            <button class="btn btn-primary btn-xs" type="button" onclick="updateRole('${role.id}')">修&nbsp;&nbsp;改</button>
                                            <%--<input type="button"  onclick="updateRole('${role.id}')" value="修改">--%>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
                                        <section class="error-container text-center">
                                            <h1>
                                                <i class="fa fa-exclamation-triangle"></i>
                                            </h1>
                                            <div class="error-divider">
                                                <h2>未查询到数据！</h2>
                                                <p class="description">协同提高效率，分享拉近距离。</p>
                                            </div>
                                        </section>
                                    </div>
                                </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </form>
                        <tags:pageBar url="/role/listPagedRole"></tags:pageBar>
                    </div>
                </div>
            </div>
        </div>
        <!-- /Page Body -->
    </div>
    <!-- /Page Content -->

</div>



<script type="text/javascript">
    $(function(){
        //文本框绑定回车提交事件
        $("#roleName").blur(function(){
            $("#searchForm").submit();
        });
    });

    //删除
    function deleteDis(){
        var checkboxObj = $(":checkbox[name='ids'][enabled='0']");
        $.each(checkboxObj,function(index){
            $(this).attr("checked",false);
            $(this).parent().parent().find(".optCheckBox").css("display","none");
            $(this).parent().parent().find(".optRowNum").css("display","block");
            $("#checkAllBox").attr('checked', false);
        });

        layui.use('layer', function(){
            if(checkCkBoxStatus('ids')){
                window.top.layer.confirm('确定要删除这些角色吗？', {
                    btn: ['确定','取消']//按钮
                    ,title:'询问框'
                    ,icon:3
                }, function(index){
                    $("#delForm").attr("action","/role/deleteRole?sid=${param.sid}");
                    $("#delForm input[name='redirectPage']").val(window.location.href);
                    $('#delForm').submit();
                });
            }else{
                window.top.layer.alert('未选择删除的角色！');
            }

        });
    }

    //删除单个角色
    function deleteSingleDis(id){
        window.top.layer.confirm('确定要删除该角色吗？', {
            btn: ['确定','取消']//按钮
            ,title:'询问框'
            ,icon:3
        }, function(index){
            $("#singleDis").val(id);
            $("#delForm").attr("action","/role/deleteRole?sid=${param.sid}");
            $("#delForm input[name='redirectPage']").val(window.location.href);
            $('#delForm').submit();
        });
    }

    //禁用角色
    function dis(){
        var checkboxObj = $(":checkbox[name='ids'][enabled='0']");
        $.each(checkboxObj,function(index){
            $(this).attr("checked",false);
            $(this).parent().parent().find(".optCheckBox").css("display","none");
            $(this).parent().parent().find(".optRowNum").css("display","block");
            $("#checkAllBox").attr('checked', false);
        });

        layui.use('layer', function(){
            if(checkCkBoxStatus('ids')){
                window.top.layer.confirm('确定要禁用这些角色吗？', {
                    btn: ['确定','取消']//按钮
                    ,title:'询问框'
                    ,icon:3
                }, function(index){
                    $("#delForm").attr("action","/role/disableRole?sid=${param.sid}");
                    $("#delForm input[name='redirectPage']").val(window.location.href);
                    $('#delForm').submit();
                });
            }else{
                window.top.layer.alert('未选择禁用的角色，或有角色已被禁用！');
            }

        });
    }

    //单一禁用
    function singleDis(id){
        layui.use('layer', function(){
            window.top.layer.confirm('确定要禁用该角色吗？', {
                btn: ['确定','取消']//按钮
                ,title:'询问框'
                ,icon:3
            }, function(index){
                $("#singleDis").val(id);
                $("#delForm").attr("action","/role/disableRole?sid=${param.sid}");
                $("#delForm input[name='redirectPage']").val(window.location.href);
                $('#delForm').submit();
            });
        });
    }

    //启用角色入口
    function roleEnable(){
        var checkboxObj = $(":checkbox[name='ids'][enabled='1']");
        $.each(checkboxObj,function(index){
            $(this).attr("checked",false);
            $(this).parent().parent().find(".optCheckBox").css("display","none");
            $(this).parent().parent().find(".optRowNum").css("display","block");
            $("#checkAllBox").attr('checked', false);
        });
        var toEnabledUsers = $(":checkbox[name='ids'][enabled='0']:checked").length;
        doRoleEnable();
    }
    //检查通过，启用角色
    function doRoleEnable(){
        layui.use('layer', function(){
            if(checkCkBoxStatus('ids')){
                window.top.layer.confirm('确定要启用这些角色吗？', {
                    btn: ['确定','取消']//按钮
                    ,title:'询问框'
                    ,icon:3
                }, function(index){
                    $("#delForm").attr("action","/role/enableRole?sid=${param.sid}");
                    $("#delForm input[name='redirectPage']").val(window.location.href);
                    $("#delForm").submit();
                });
            }else{
                window.top.layer.alert('未选择启用的角色，或有角色已被启用！');
            }
        });
    }

    //启用单个角色
    function doSingleRoleEnable(id){
        window.top.layer.confirm('确定要启用该角色吗？', {
            btn: ['确定','取消']//按钮
            ,title:'询问框'
            ,icon:3
        }, function(index){
            $("#singleDis").val(id);
            $("#delForm").attr("action","/role/enableRole?sid=${param.sid}");
            $("#delForm input[name='redirectPage']").val(window.location.href);
            $("#delForm").submit();
        });
    }

    //授权
    function grant(){
        var checkboxObj = $(":checkbox[name='ids'][adminState='2']");
        $.each(checkboxObj,function(index){
            $(this).attr("checked",false);
            $(this).parent().parent().find(".optCheckBox").css("display","none");
            $(this).parent().parent().find(".optRowNum").css("display","block");
            $("#checkAllBox").attr('checked', false);
        });

        layui.use('layer', function(){

            if(checkCkBoxStatus('ids')){
                window.top.layer.confirm('确定要授管理权限给这些角色吗？', {
                    btn: ['确定','取消']//按钮
                    ,title:'询问框'
                    ,icon:3
                }, function(index){
                    $("#delForm").attr("action","/userInfo/updateGrant?sid=${param.sid}");
                    $("#delForm input[name='redirectPage']").val(window.location.href);
                    $('#delForm').submit();
                });
            }else{
                window.top.layer.alert('请先勾选需要授管理权限的角色！');
            }
        });
    }
    //回收权限
    function revoke(){

        var checkboxObj = $(":checkbox[name='ids'][adminState='0']");
        $.each(checkboxObj,function(index){
            $(this).attr("checked",false);
            $(this).parent().parent().find(".optCheckBox").css("display","none");
            $(this).parent().parent().find(".optRowNum").css("display","block");
            $("#checkAllBox").attr('checked', false);
        });
        layui.use('layer', function(){

            if(checkCkBoxStatus('ids')){
                window.top.layer.confirm('确定要回收这些角色的权限吗？', {
                    btn: ['确定','取消']//按钮
                    ,title:'询问框'
                    ,icon:3
                }, function(){
                    $("#delForm").attr("action","/userInfo/updateRevoke?sid=${param.sid}");
                    $("#delForm input[name='redirectPage']").val(window.location.href);
                    $('#delForm').submit();
                });
            }else{
                window.top.layer.alert('请先勾选需要回收管理权限的角色！');
            }
        });
    }
    //能否激活验证
    function checkOrgUsersInService(ts,id,enable,inService,isadmin){
        $.ajax({
            type : "post",
            url : "/organic/checkOrgUsersInService?sid=${param.sid}&rnd="+Math.random(),
            async : false,//同步
            dataType:"json",
            data:{enable:enable,inService:inService},
            success : function(data){
                if(data.canDo){
                    if(data.enable==1 && data.inService==0){//登录受限的激活角色
                        if(data.msg){
                            window.top.layer.confirm(data.msg, {
                                btn: ['确定','取消']//按钮
                                ,title:'确认框'
                                ,icon:3
                            }, function(){
                                updateUserInService(ts,id,enable,inService,isadmin);//需禁用其他人服务状态
                            });
                        }else{
                            updateUserInService(ts,id,enable,inService,isadmin);//直接激活登录状态
                        }
                    }else{//一般角色的禁用与启用
                        doUpdateEnabled(ts,id,enable,inService,isadmin);
                    }
                }else{
                    showNotification(2,data.msg);
                }
            },
            error:  function(XMLHttpRequest, textStatus, errorThrown){
                showNotification(2,"系统错误,请联系管理人员！");
            }
        });
    }
    //更新团队人员的服务状态
    function updateUserInService(ts,id,enable,inService,isadmin){
        var onclick = $(ts).attr("onclick");
        $.ajax({
            type : "post",
            url : "/userInfo/updateUserInService?sid=${param.sid}&rnd="+Math.random(),
            dataType:"json",
            data:{userOrgId:id},
            beforeSend: function(XMLHttpRequest){
                $(ts).removeAttr("onclick");
            },
            success : function(data){
                //刷新页面
                window.location.reload();
            },
            error:  function(XMLHttpRequest, textStatus, errorThrown){
                $(ts).attr("onclick",onclick);
                showNotification(2,"系统错误,请联系管理人员！");
            }
        });
    }
    //更新角色状态
    function updateEnabled(ts,id,enable,inService,isadmin){
        var admin = ${userInfo.admin};
        if(isadmin>0 && admin != 1){
            showNotification(2,"对象是“管理员”；需要“系统管理员”才能操作！");
            return;
        }
        if(enable==1 && inService==1){//禁用角色；角色是激活状态，且是服务中状态的，才是禁用操作
            doUpdateEnabled(ts,id,enable,inService,isadmin);
        }else{//激活角色
            checkOrgUsersInService(ts,id,enable,inService,isadmin);//能否激活验证
        }
    }
    //执行更新操作
    function doUpdateEnabled(ts,id,enable,inService,isadmin){
        var onclick = $(ts).attr("onclick");
        $.ajax({
            type : "post",
            url : "/userInfo/updateEnabled?sid=${param.sid}&rnd="+Math.random(),
            dataType:"json",
            data:{id:id,enable:enable,inService:inService},
            beforeSend: function(XMLHttpRequest){
                $(ts).removeAttr("onclick");
            },
            success : function(data){
                if(data.status=='y'){
                    if(enable==1){
                        $(ts).attr("onclick","updateEnabled(this,"+id+",0,0,"+isadmin+")");
                        $(ts).css("color","red");
                        $(ts).html("禁用");
                    }else if(enable==0){
                        $(ts).attr("onclick","updateEnabled(this,"+id+",1,1,"+isadmin+")");
                        $(ts).css("color","green");
                        $(ts).html("启用");
                    }
                    showNotification(1,data.info);
                }else{
                    $(ts).attr("onclick",onclick);
                    showNotification(2,data.info);
                }
            },
            error:  function(XMLHttpRequest, textStatus, errorThrown){
                $(ts).attr("onclick",onclick);
                showNotification(2,"系统错误,请联系管理人员！");
            }
        });
    }
    //执行更新操作
    function updateEnrollNumber(sid,userId,roleName,ts){
        $.ajax({
            type : "post",
            url : "/userInfo/updateEnrollNumber?sid="+sid+"&rnd="+Math.random(),
            dataType:"json",
            data:{id:userId,roleName:roleName,enrollNumber:$(ts).val()},
            success : function(data){
                if(data.status=='y'){
                    showNotification(1,data.info);
                }else{
                    showNotification(2,data.info);
                }
            },
            error:  function(XMLHttpRequest, textStatus, errorThrown){
                showNotification(2,"系统错误,请联系管理人员！");
            }
        });
    }
    //添加,修改人员编号
    function chooseEnrollNumber(sid,userId){
        var url = "/attence/listAttenceUser?sid="+sid+"&pager.pageSize=8";
        window.top.layer.open({
            title:false,
            closeBtn:0,
            type: 2,
            shadeClose: true,
            shade: 0.1,
            shift:0,
            zIndex:299,
            fix: true, //固定
            maxmin: false,
            move: false,
            border:1,
            btn:['确定','关闭'],
            area: ['500px','480px'],
            content: [url,'no'],
            yes:function(index,layero){
                var iframeWin = window[layero.find('iframe')[0]['name']];
                var enrollNumber = iframeWin.returnEnrollNumber();
                if(enrollNumber){
                    window.top.layer.close(index);
                    //修改编号
                    updateEnrollNumer(sid,userId,enrollNumber);
                }
            },
            success: function(layero,index){
            },end:function(index){

            }
        });
        //修改编号
        function updateEnrollNumer(sid,userId,enrollNumber){
            $.ajax({
                type : "post",
                url : "/userInfo/updateEnrollNumber?sid="+sid+"&rnd="+Math.random(),
                dataType:"json",
                data:{id:userId,enrollNumber:enrollNumber},
                success : function(data){
                    if(data.status=='y'){
                        showNotification(1,data.info);
                        window.location.reload();
                    }else{
                        showNotification(2,data.info);
                    }
                },
                error:  function(XMLHttpRequest, textStatus, errorThrown){
                    showNotification(2,"系统错误,请联系管理人员！");
                }
            });
        }
    }
    //人员信息查看
    function view(id){
        var url='/role/listRoleBindingUsers?id='+id+'&sid=${param.sid}';
        window.top.layer.open({
            type: 2,
            title: false,
            closeBtn: 0,
            shade: 0.5,
            shift:0,
            scrollbar:false,
            fix: true, //固定
            maxmin: false,
            move: false,
            area: ['640px', '500px'],
            content: [url,'no'], //iframe的url
            btn:['关闭']
        });

    }
    $(function(){
        //操作删除和复选框
        $('.optTr').mouseover(function(){
            var display = $(this).find(".optTd .optCheckBox").css("display");
            if(display=='none'){
                $(this).find(".optTd .optCheckBox").css("display","block");
                $(this).find(".optTd .optRowNum").css("display","none");
            }
        });
        $('.optTr ').mouseout(function(){
            var optCheckBox = $(this).find(".optTd .optCheckBox");
            var check = $(optCheckBox).find("input").attr('checked');
            if(check){
                $(this).find(".optTd .optCheckBox").css("display","block");
                $(this).find(".optTd .optRowNum").css("display","none");
            }else{
                $(this).find(".optTd .optCheckBox").css("display","none");
                $(this).find(".optTd .optRowNum").css("display","block");
            }
        });

        $(":checkbox[name='ids'][disabled!='disabled']").click(function(){
            var checkLen = $(":checkbox[name='ids'][disabled!='disabled']:checked").length;
            var len = $(":checkbox[name='ids'][disabled!='disabled']").length;
            if(checkLen>0){
                if(checkLen==len){
                    $("#checkAllBox").attr('checked', true);
                }else{
                    $("#checkAllBox").attr('checked', false);
                }
            }else{

                $("#checkAllBox").attr('checked', false);
            }
        });
    });
</script>
</body>
</html>
