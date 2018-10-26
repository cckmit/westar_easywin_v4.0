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
<div id="tab1" class="tab-pane in active">
    <div class="panel-body no-padding">
        <div class="widget no-header">
            <div class="widget-body bordered-radius">
                <div class="tickets-container tickets-bg tickets-pd clearfix">
                    <ul class="tickets-list clearfix">
                        <li class="ticket-item no-shadow clearfix ticket-normal">
                            <div class="pull-left productDetails-left-text ">
                                <div class="ticket-user other-user-box pull-left no-padding">
                                    <img src="/downLoad/userImg/${userInfo.comId}/${product.creator}"
                                         class="user-avatar" title="${product.creatorName}" />
                                    <span class="user-name">${product.creatorName}</span>
                                </div>
                            </div>
                            <div class="pull-left col-lg-7 col-sm-7 col-xs-7 margin-left-10">
                                <div class="row">
                                    <c:choose>
                                        <c:when test="${empty editProduct && (1==product.state || 0==product.state) && product.creator==userInfo.id}">
                                            <input id="name" datatype="input,sn" defaultLen="52" name="name" nullmsg="请填写产品名称" class="colorpicker-default form-control pull-left" type="text" title="${product.name}"
                                                   value="${product.name}" onpropertychange="handleName()" onkeyup="handleName()" style="width:85%">
                                            <span id="msgTitle" class="pull-left margin-top-5 padding-left-5" style="width:auto;">(0/26)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <strong>${product.name}</strong>
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="hidden" id="schname" value="${product.name}" >
                                    <c:if test="${empty editProduct && (1==product.state || 0==product.state) && product.creator==userInfo.id}">
                                        <script>
                                            //当状态改变的时候执行的函数
                                            function handleName() {
                                                var value = $('#name').val();
                                                var len = charLength(value.replace(/\s+/g,""));
                                                if (len % 2 == 1) {
                                                    len = (len + 1) / 2;
                                                } else {
                                                    len = len / 2;
                                                }
                                                if (len > 26) {
                                                    $('#msgTitle').html(
                                                        "(<font color='red'>" + len
                                                        + "</font>/26)");
                                                } else {
                                                    $('#msgTitle').html("(" + len + "/26)");
                                                }
                                            }
                                            //firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。
                                            if (/msie/i.test(navigator.userAgent)) { //ie浏览器
                                                document.getElementById('name').onpropertychange = handleName
                                            } else {//非ie浏览器，比如Firefox
                                                document.getElementById('name')
                                                    .addEventListener("input",
                                                        handleName, false);
                                            }
                                        </script>
                                    </c:if>
                                </div>
                            </div>
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
                                        <input type="hidden" id="manager" name="manager" value="${product.manager}">
                                    </div>
                                    <div class="pull-left" id="managers" style="max-width: 100px">
                                        <div class="online-list " ondblclick="userRemove(this,'manager')" style="float:left;padding-top:5px" title="双击移除">
                                            <img src="/downLoad/userImg/${product.comId}/${product.manager}" class="user-avatar" id="userImg_${product.manager}">
                                            <span class="user-name">${product.managerName}</span>
                                        </div>
                                    </div>
                                    <a href="javascript:void(0);" id="managerSelect" class="margin-top-7 btn btn-primary btn-xs userSelectBtn pull-left"
                                       relateSelect="manager" relateImgDiv="managers" title="人员选择" style="margin-left:15px;"><i class="fa fa-plus"></i>选择</a>
                                </div>
                            </li>
                            <li class="no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
                                <div class="pull-left task-left-text task-height">
                                    负责人<span style="color: red">*</span>：
                                </div>
                                <div class="ticket-user pull-left ps-right-box">
                                    <div style="width: 100px;display:none;">
                                        <input type="hidden" id="principal" name="principal" value="${product.principal}">
                                    </div>
                                    <div class="pull-left" id="principals" style="max-width: 100px">
                                        <div class="online-list " ondblclick="userRemove(this,'principal')" style="float:left;padding-top:5px" title="双击移除">
                                            <img src="/downLoad/userImg/${product.comId}/${product.principal}" class="user-avatar" id="userImg_${product.principal}">
                                            <span class="user-name">${product.principalName}</span>
                                        </div>
                                    </div>
                                    <a href="javascript:void(0);" id="principalSelect" class="margin-top-7 btn btn-primary btn-xs userSelectBtn"
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
                                        <tags:dataDic type="proType" name="type" id="proType" value="${product.type}"></tags:dataDic>
                                    </div>
                                </div>
                            </li>
                            <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
                                <div class="pull-left task-left-text task-height">版本号：</div>
                                <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
                                    <div class="row">
                                        <input id="version" datatype="input" name="version" type="text" defaultLen="52" class="form-control" placeholder="产品版本号"
                                              value="${product.version}" readonly="readonly " value="1.0"/>
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
                            <div class="pull-left productDetails-left-text">
                                描述：
                            </div>
                            <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                <div class="row" style="width:620px;">
                                    <c:choose>
                                        <c:when
                                                test="${(1==product.state || 0==product.state) && product.creator==userInfo.id && empty editProduct}">
												<textarea class="form-control" id="description" name="description"
                                                          rows="" cols="" style="width:620px;height: 110px;display:none;"
                                                          name="description">${product.description}</textarea>
                                            <iframe ID="eWebEditor1"
                                                    src="/static/plugins/ewebeditor/ewebeditor.htm?id=description&style=expand600"
                                                    frameborder="0" scrolling="no" width="620px" height="280"></iframe>
                                            <button type="button" onclick="saveRemark()"
                                                    class="btn btn-info ws-btnBlue pull-right">更新</button>
                                        </c:when>
                                        <c:otherwise>
                                            <div>
                                                <div class="comment" style="width: 100%" id="description">
                                                        ${product.description}</div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    
    var productObj = {
        "productId":"${product.id}",//产品主键
        "state":"${product.state}",//产品状态
        "ownState":"${userInfo.id==product.creator?'1':'0'}"//产品的负责人，编辑界面一定是负责人
    }

    var sid="${param.sid}";
    var pageParam= {
        "sid":"${param.sid}",
        "comId":"${userInfo.comId}"
    }
    var EasyWin = {
        "ewebMaxMin":"self"
    };
    $(function(){
        productOptForm.productOptMenu();

        //产品产品经理员信息设置
        $("body").on("click","#managerSelect",function(){
            taskOptForm.chooseUser($(this),1);
        });
        //产品负责人员信息设置
        $("body").on("click","#principalSelect",function(){
            taskOptForm.chooseUser($(this),0);
        })
    });
    var productOptForm = {
        productOptMenu: function () {
            //添加留言按钮
            productOptForm.addProductTalkMenu(productObj.state);
            //添加重启按钮
            productOptForm.addProductRestartMenu(productObj.state,productObj.ownState);
            //添加完成按钮
            productOptForm.addProductFinishMenu(productObj.state,productObj.ownState);
            //添加更多按钮
            productOptForm.addProMoreMenu(productObj.state,productObj.ownState);
        }, addProductTalkMenu: function (state) {
            //进行中、完成、暂停
            if (state == 0 || state == 1 || state == 2) {
                var _talkA = $('<a href="javascript:void(0)" class="purple" id="headProductTalk" title="产品留言"></a>');
                $(_talkA).html('<i class="fa fa-comments"></i>留言');
                $(".productOptMenu").append($(_talkA))
            }
        }, addProductRestartMenu: function (state,ownState) {
            //完成、暂停、废弃
            if ((state == 1 || state == 2 || state == 3) && ownState == 1) {
                var _talkA = $('<a href="javascript:void(0)" class="purple" id="restart" title="产品重启"></a>');
                $(_talkA).html('<i class="fa fa-start"></i>重启');
                $(".productOptMenu").append($(_talkA))
            }
        }, addProductFinishMenu: function (state,ownState) {
            //完成、暂停、废弃
            if (state == 0 && ownState == 1) {
                var _talkA = $('<a href="javascript:void(0)" class="purple" id="finish" title="产品完成"></a>');
                $(_talkA).html('<i class="fa fa-finish"></i>完成');
                $(".productOptMenu").append($(_talkA))
            }
        },addProMoreMenu:function(state,ownState){
            if(ownState == 1 && (state == 0 || state == 1)){
                var _moreA = $('<a class="green ps-point margin-right-0" data-toggle="dropdown" title="更多操作"></a>');
                $(_moreA).html(' <i class="fa fa-th"></i>更多');
                $(".productOptMenu").append($(_moreA));

                //更多的下拉
                var _moreUl = $('<ul class="pull-right dropdown-menu dropdown-arrow ps-layerUl"></ul>');

                //产品废弃
                if(state == 0 || state == 1 ){
                    var _resolveLi = $('<li></li>');
                    var _resolveA = $('<a href="javascript:void(0)" id="discard"></a>');
                    $(_resolveA).html('<div class="clearfix"><i class="fa fa-flag"></i><span class="title ps-topmargin">废弃</span></div>');

                    $(_resolveLi).append($(_resolveA))
                    $(_moreUl).append($(_resolveLi));
                }

                //产品暂停
                if(state == 0 || state == 1){
                    var _resolveLi = $('<li></li>');
                    var _resolveA = $('<a href="javascript:void(0)" id="suspend"></a>');
                    $(_resolveA).html('<div class="clearfix"><i class="fa fa-flag"></i><span class="title ps-topmargin">暂停</span></div>');

                    $(_resolveLi).append($(_resolveA))
                    $(_moreUl).append($(_resolveLi));
                }
                $(".productOptMenu").append($(_moreUl));
            }
        }
    }

    //人员选择初始化时的移除
    function userRemove(ts,select){
        $(ts).remove();
        $("#" + select).remove();
    }
</script>
</body>
</html>
