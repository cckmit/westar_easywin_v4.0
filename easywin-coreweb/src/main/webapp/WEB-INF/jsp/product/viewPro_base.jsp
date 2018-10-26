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
                            <div class="pull-left taskDetails-left-text ">
                                <div class="ticket-user other-user-box pull-left no-padding">
                                    <img src="/downLoad/userImg/${userInfo.comId}/${product.creator}"
                                         class="user-avatar" title="${product.creatorName}" />
                                    <span class="user-name">${product.creatorName}</span>
                                </div>
                            </div>
                            <div class="pull-left col-lg-7 col-sm-7 col-xs-7 margin-left-10 margin-top-10">
                                <div class="row">
                                    ${product.name}
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
                                    产品经理：
                                </div>
                                <div class="ticket-user pull-left ps-right-box">
                                    <div style="width: 100px;display:none;">
                                        <input type="hidden" id="manager" name="manager" value="${product.manager}">
                                    </div>
                                    <div class="pull-left" id="managers" style="max-width: 100px">
                                        <div class="online-list " style="float:left;padding-top:5px" title="双击移除">
                                            <img src="/downLoad/userImg/${product.comId}/${product.manager}" class="user-avatar" id="userImg_${product.manager}">
                                            <span class="user-name">${product.managerName}</span>
                                        </div>
                                    </div>
                                </div>
                            </li>
                            <li class="no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
                                <div class="pull-left task-left-text task-height">
                                    负责人：
                                </div>
                                <div class="ticket-user pull-left ps-right-box">
                                    <div style="width: 100px;display:none;">
                                        <input type="hidden" id="principal" name="principal" value="${product.principal}">
                                    </div>
                                    <div class="pull-left" id="principals" style="max-width: 100px">
                                        <div class="online-list " style="float:left;padding-top:5px" title="双击移除">
                                            <img src="/downLoad/userImg/${product.comId}/${product.principal}" class="user-avatar" id="userImg_${product.principal}">
                                            <span class="user-name">${product.principalName}</span>
                                        </div>
                                    </div>
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
                                <div class="pull-left taskDetails-left-text task-height">产品类型：</div>
                                <div class="pull-left col-lg-5 col-sm-5 col-xs-5 margin-top-7">
                                    <span><tags:viewDataDic type="proType" code="${product.type}" ></tags:viewDataDic></span>
                                </div>
                            </li>
                            <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
                                <div class="pull-left taskDetails-left-text task-height">版本号：</div>
                                <div class="pull-left col-lg-5 col-sm-5 col-xs-5 margin-top-7">
                                    <span>${product.version}</span>
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
                            <div class="pull-left col-lg-7 col-sm-7 col-xs-7" style="margin-top: -2px;">
                                <div class="row" style="width:620px;">
                                    <p>${product.description}</p>
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
        }, addProductTalkMenu: function (state) {
            //进行中、完成、暂停
            if (state == 0 || state == 1 || state == 2) {
                var _talkA = $('<a href="javascript:void(0)" class="purple" id="headProductTalk" title="产品留言"></a>');
                $(_talkA).html('<i class="fa fa-comments"></i>留言');
                $(".productOptMenu").append($(_talkA))
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
