<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
        contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
        errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@page import="com.westar.core.web.InitServlet"%>
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
        //关闭窗口
        function closeWin() {
            var winIndex = window.top.layer.getFrameIndex(window.name);
            closeWindow(winIndex);
        }
        function formSub(){
            $(".subform").submit();
        }
        $(function(){
            //任务描述的图片查看
            var imgArray = $("#comment").find("img");
            $.each(imgArray,function(index,item){
                var src = $(this).attr("src");
                $(this).css("max-width","550px");

                $(this).click(function(){
                    window.top.viewOrgByPath(src);
                })
            });

            $(".subform").Validform({
                tiptype : function(msg, o, cssctl) {
                    validMsgV2(msg, o, cssctl);
                },
                callback:function (form){
                    //提交前验证是否在上传附件
                    return sumitPreCheck(null);
                },
                showAllError : true
            });

            $("input[name='redirectPage']").val(window.self.location);

            if(!strIsNull("${upMsg}")){
                window.top.layer.alert("${upMsg}",{icon:7,title:false,closeBtn:0,btn:["关闭"]});
            }
        });
    </script>
</head>
<body>
<form class="subform" method="post" action="/functionList/functionImport">
    <tags:token></tags:token>
    <input type="hidden" name="redirectPage">
    <input type="hidden" name="busId" value="${busId}">
    <input type="hidden" name="busType" value="${busType}">
    <div id="fileDivAjax"></div>
    <div class="container" style="padding: 0px 0px;width: 100%">
        <div class="row" style="margin: 0 0">
            <div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
                <div class="widget" style="margin-top: 0px;">
                    <div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">功能导入</span>
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
                                                <div style="float:left;width:57%;">
                                                    <div class="pull-left  ps-left-text padding-top-10" style="text-align: left;">
                                                        <span style="color: red;font-weight: bolder;">注意：</span>
                                                    </div>
                                                    <div class="pull-left col-lg-12 col-sm-12 col-xs-12 margin-left-10">
                                                        <div class="row">
                                                            <div style="float:left;overflow:auto;margin-left:10px;">
                                                                <p style="color:red;">
                                                                    1、该功能只能作批量导入，修改以及删除请到功能清单列表；<br/>
                                                                    2、第一次导入请点击下方图片查看格式，请严格按照提示填写；<br/>
                                                                    3、第一行是必须行，并且每个功能必须有说明，没有说明则保留空行；<br/>
                                                                    4、如果上传功能名称已存在，则视为更新该功能。<br/>
                                                                    5、子功能与父功能名称不可重复；单元格可合并；<br/>
                                                                    7、可以上传多个表格，也可以放在同一个表格的多sheet中上传，格式相同；<br/>
                                                                    8、上传成功后，请关掉本页面，刷新功能清单进行查看；<br/>
                                                                </p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="pull-left col-lg-12 col-sm-12 col-xs-12 margin-left-10">
                                                        <div class="row">
                                                            <div class="comment" id="comment" style="width: 100%;float: left;margin-left: 10px;">
                                                                <img border="0" style="float:left;width:95%;height:100px;" src="/static/images/itemModel1.png" /><br/>
                                                                <div style="color:#8e8f8c;text-align: center;width:95%;">合并单元格前</div>
                                                                <img border="0" style="float:left;width:95%;height:100px;" src="/static/images/itemModel2.png" /><br/>
                                                                <div style="color:#8e8f8c;text-align: center;width:95%;">合并单元格后</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div style="float:left;width:1px;height:390px;background-color: #cbccc7;margin-left:10px;"></div>
                                                <div style="float:left;margin-left: 20px;height:400px;width:38%;" class="no-header">
                                                    <div class="pull-left col-lg-12 col-sm-12 col-xs-12" style="width:100%;float:left;overflow: hidden;overflow-y:scroll;height:390px;">
                                                        <div class="row">
                                                            <tags:uploadMore name="listUpfiles.id" showName="fileName"
                                                                             ifream="" comId="${userInfo.comId}"></tags:uploadMore>
                                                        </div>
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
