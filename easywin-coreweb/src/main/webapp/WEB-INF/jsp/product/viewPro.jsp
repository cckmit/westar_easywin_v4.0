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
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
    <title><%=SystemStrConstant.TITLE_NAME%></title>
    <!-- 框架样式 -->
    <jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
    <link href="/static/product/assets/css/product.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/js/proJs/proCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
    <script src="/static/js/proJs/proOpt.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
        var sid="${param.sid}";//身份验证码
        var EasyWin = {
            "ewebMaxMin":"self"
        };
        //打开页面body
        var openWindowDoc;
        //打开页面,可调用父页面script
        var openWindow;
        //打开页面的标签
        var openPageTag;
        //打开页面的标签
        var openTabIndex;
        //注入父页面信息
        function setWindow(winDoc,win){
            openWindowDoc = winDoc;
            openWindow = win;
            openPageTag = openWindow.pageTag;
            openTabIndex = openWindow.tabIndex;
        }
        //是否为空判断
        function isNull(obj){
            if(obj!="" && obj!="null" && obj!=null && obj!="undefined" && obj!=undefined){
                return false;
            }else{
                return true;
            }
        }

        //关闭窗口
        function closeWin(){
            var winIndex = window.top.layer.getFrameIndex(window.name);
            closeWindow(winIndex);
        }
        //autoCompleteCallBack回调对象标识
        var regex = /['|<|>|"]+/;
        $(function(){
            //设置滚动条高度
            var height = $(window).height()-40;
            $("#contentBody").css("height",height+"px");

            //初始化名片
            initCard('${param.sid}')
            //$("#description").autoTextarea({minHeight:110,maxHeight:150});
            $(".subform").Validform({
                tiptype : function(msg, o, cssctl) {
                    validMsg(msg, o, cssctl);
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
            //产品名称更新
            $("#name").change(function(){
                //产品名称
                var name = $("#name").val();
                if(regex.test(name)){
                    return false;
                }
                //汉字算两个长度
                var count = name.replace(/[^\x00-\xff]/g,"**").length;
                var len = $("#name").attr("defaultLen");
                //超过指定的长度
                if(count>len){
                    return false;
                }else{
                    if(!strIsNull(name)){
                        postUrl("/product/proAjaxUpdate?sid=${param.sid}",{id:${product.id},name:name},
                            function (data){
                                $("#name").attr("title",name);
                                name = '--'+cutstr(name,32);
                                $("#titleName").html(name);
                                showNotification(1,data.msg);
                            });
                    }
                }
            });
            $("#type").change(function(){
                if(!strIsNull($("#type").val()) || $("#type").val() == 0){
                    $.post("/product/proAjaxUpdate?sid=${param.sid}",{Action:"post",id:${product.id},type:$("#type").val()},
                        function (data){
                            showNotification(1,data.msg)
                        },"json");
                }
            });
            //产品详情
            $("body").on("click","#productBaseMenuLi",function(){
                $("#otherProductAttrIframe").css("display","none");
                $("#productBase").css("display","block");
                $("#productTalkMenuLi").parent().find("li").removeAttr("class");
                $("#productBaseMenuLi").attr("class","active");
            });
            //产品功能
            $("body").on("click","#productFunMenuLi",function(){
                $("#otherProductAttrIframe").css("display","block");
                $("#productBase").css("display","none");
                $("#productFunMenuLi").parent().find("li").removeAttr("class");
                $("#productFunMenuLi").attr("class","active");
                $("#otherProductAttrIframe").attr("src","/functionList/functionListPage?sid=${param.sid}&fromType=view&pager.pageSize=10&busName=${product.name}&busId=${product.id}&busType=080&iframe=otherProductAttrIframe");
            });
            //产品讨论
            $("body").on("click","#productTalkMenuLi,#headProductTalk",function(){
                $("#otherProductAttrIframe").css("display","block");
                $("#productBase").css("display","none");
                $("#productTalkMenuLi").parent().find("li").removeAttr("class");
                $("#productTalkMenuLi").attr("class","active");
                $("#otherProductAttrIframe").attr("src","/productTalk/productTalkPage?sid=${param.sid}&pager.pageSize=10&busId=${product.id}&busType=080&ifreamName=otherProductAttrIframe");

            })
            //产品日志
            $("body").on("click","#productLogMenuLi",function(){
                $("#otherProductAttrIframe").css("display","block");
                $("#productBase").css("display","none");
                $(this).parent().find("li").removeAttr("class");
                $("#productLogMenuLi").attr("class","active");
                $("#otherProductAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${product.id}&busType=080&ifreamName=otherProductAttrIframe");
            })
            //产品附件
            $("body").on("click","#productUpfileMenuLi",function(){
                $("#otherProductAttrIframe").css("display","block");
                $("#productBase").css("display","none");
                $(this).parent().find("li").removeAttr("class");
                $("#productUpfileMenuLi").attr("class","active");
                $("#otherProductAttrIframe").attr("src","/product/productUpfilePage?sid=${param.sid}&pager.pageSize=10&proId=${product.id}");

            })
            //产品浏览记录
            $("body").on("click","#productViewRecord",function(){
                $("#otherProductAttrIframe").css("display","block");
                $("#productBase").css("display","none");
                $(this).parent().find("li").removeAttr("class");
                $("#productViewRecord").attr("class","active");
                $("#otherProductAttrIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${product.id}&busType=080&ifreamName=otherProductAttrIframe");

            })
            //输入文本框双击删除绑定
            $("body").on("dblclick",".colInput",function(){
                var actObj = $(this);
                var busType = $(actObj).attr("busType");
                var busId = $(actObj).attr("busId");
                var productId = "${product.id}";
                $.post("/product/delProductBusRelation?sid=${param.sid}",{Action:"post",id:productId,busId:busId,busType:busType},
                    function (data){
                        if(data.status=='y'){
                            showNotification(1,data.info);
                            $(actObj).parents(".relativeRow[busType='"+$(actObj).attr("busType")+"']").remove();
                        }else{
                            showNotification(2,data.info);
                        }
                    },"json");
            });
            //产品标记完成
            $("body").on("click",".finishProductBtn",function(){
                finishProduct(${product.id},${product.state},openTabIndex,openPageTag);//办结产品
            });

            //产品暂停
            $("body").on("click",".pauseProductBtn",function(){
                pauseProduct(${product.id},${product.state});//暂停产品
            });
        });

        //产品经理更新
        function managerUpdate(){
            if($("#manager").val()){
                $.post("/product/proAjaxUpdate?sid=${param.sid}",{Action:"post",id:${product.id},manager:$("#manager").val()},
                    function (data){
                        if(data.code==0){
                            showNotification(1,data.msg);
                        }else{
                            showNotification(2,data.msg);
                        }
                    },"json");
            }
        }
        //产品负责人更新
        function principalUpdate(){
            if($("#principal").val()){
                $.post("/product/proAjaxUpdate?sid=${param.sid}",{Action:"post",id:${product.id},principal:$("#principal").val()},
                    function (data){
                        if(data.code==0){
                            showNotification(1,data.msg);
                        }else{
                            showNotification(2,data.msg);
                        }
                    },"json");
            }
        }
        /**
         * 保存简介
         */
        function saveRemark(){
            var description = document.getElementById("eWebEditor1").contentWindow.getHTML();
            if(!strIsNull(description)){
                $.post("/product/proAjaxUpdate?sid=${param.sid}",{Action:"post",id:${product.id},description:description},
                    function (data){
                        showNotification(1,data.msg);
                    },"json");
            }
        }
    </script>
</head>
<body onload="${(empty editProduct &&  (1==product.state || 0==product.state) && product.creator==userInfo.id)?'handleName()':'' }">
<form id="subform" class="subform" method="post">
    <input type="hidden" name="id" value="${product.id}"/>
    <input type="hidden" id="busType" value="080"/>
    <div class="container" style="padding: 0px 0px;width:100%">
        <div class="row" style="margin: 0 0">
            <div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
                <div class="widget" style="margin-top: 0px;" >
                    <div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">产品</span>
                        <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px" id="titleName">
                        	<c:choose>
                                <c:when test="${fn:length(product.name)>16 }">
                                    --${fn:substring(product.name,0,16)}..
                                </c:when>
                                <c:otherwise>
                                    --${product.name}
                                </c:otherwise>
                            </c:choose>
                        </span>
                        <div class="widget-buttons ps-toolsBtn productOptMenu">
                            <a></a>
                            <!--产品的操作按钮信息 见 .productOptMenu-->
                        </div>
                        <div class="widget-buttons">
                            <a href="javascript:void(0)" onclick="closeWin();" title="关闭">
                                <i class="fa fa-times themeprimary"></i>
                            </a>
                        </div>
                    </div><!--Widget Header-->
                    <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
                    <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                        <div class="widget-body no-shadow">
                            <div class="widget-main ">
                                <div class="tabbable">
                                    <ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;">
                                        <li class="active" id="productBaseMenuLi">
                                            <a data-toggle="tab" href="javascript:void(0)">产品详情</a>
                                        </li>
                                        <li id="productFunMenuLi">
                                            <a data-toggle="tab" href="javascript:void(0)">功能清单</a>
                                        </li>
                                        <li id="productTalkMenuLi">
                                            <a data-toggle="tab" href="javascript:void(0)">产品留言<c:if test="${product.msgNum > 0}"><span style="color:red;font-weight:bold;">（${product.msgNum}）</span></c:if></a>
                                        </li>
                                        <li id="productUpfileMenuLi">
                                            <a data-toggle="tab" href="javascript:void(0)">产品文档<c:if test="${product.docNum > 0}"><span style="color:red;font-weight:bold;">（${product.docNum}）</span></c:if></a>
                                        </li>
                                        <li id="productLogMenuLi">
                                            <a data-toggle="tab" href="javascript:void(0)">操作日志</a>
                                        </li>
                                    </ul>
                                    <div class="tab-content tabs-flat">
                                        <div id="productBase" style="display:block;">
                                            <jsp:include page="./viewPro_base.jsp"></jsp:include>
                                        </div>
                                        <iframe id="otherProductAttrIframe" style="display:none;" class="layui-layer-load"
                                                src=""
                                                border="0" frameborder="0" allowTransparency="true"
                                                noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
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