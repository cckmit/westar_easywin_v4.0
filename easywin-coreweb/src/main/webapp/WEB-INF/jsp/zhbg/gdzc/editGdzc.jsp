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
<script type="text/javascript">
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
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
//autoCompleteCallBack回调对象标识
var autoCompleteCallBackVar =null;
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		datatype:{
			"ssType":function(gets,obj,curform,regxp){
				if($("#ssType").val()){
					return true;
				}else{
					return false;
				}
			},
			"addType":function(gets,obj,curform,regxp){
				if($("#addType").val()){
					return true;
				}else{
					return false;
				}
			},
		},
		callback:function (form){
			//提交前验证是否在上传附件
			return sumitPreCheck(null);
		},
		showAllError : true
	});
	
	//form表单提交
	$("#updateGdzc").click(function(){
		formSub();
	});
	
	//提交表单
	function formSub(){
		$(".subform").submit();
	}
});
$(function(){
	//固定资产信息
	$("#gdzcbaseMenuLi").click(function(){
		$("#otherGdzcAttrIframe").css("display","none");
		$("#gdzcBase").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$("#gdzcbaseMenuLi").attr("class","active");
	});
	//保险记录
	$("#reduceMenuLi").click(function(){
		$("#otherGdzcAttrIframe").css("display","block");
		$("#gdzcBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#reduceMenuLi").attr("class","active");
		$("#otherGdzcAttrIframe").attr("src","/gdzc/gdzcReduceRecordPage?sid=${param.sid}&gdzcId=${gdzc.id}");
	});
	//固定资产维修记录
	$("#gdzcMaintainRecordLi").click(function(){
		$("#otherGdzcAttrIframe").css("display","block");
		$("#gdzcBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#gdzcMaintainRecordLi").attr("class","active");
		$("#otherGdzcAttrIframe").attr("src","/gdzc/gdzcMaintainRecordPage?sid=${param.sid}&gdzcId=${gdzc.id}");
	});
	//固定资产附件
	$("#gdzcUpfileMenuLi").click(function(){
		$("#otherGdzcAttrIframe").css("display","block");
		$("#gdzcBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#gdzcUpfileMenuLi").attr("class","active");
		$("#otherGdzcAttrIframe").attr("src","/gdzc/gdzcUpfilePage?sid=${param.sid}&pager.pageSize=10&gdzcId=${gdzc.id}");
	});
});
</script>
</head>
<body>
<form class="subform" method="post" action="/gdzc/updateGdzc">
<input type="hidden" name = "id" value="${gdzc.id }">
<tags:token></tags:token>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							
                        <span class="widget-caption themeprimary" style="font-size: 20px">固定资产--</span>
                        <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px">
                        	${gdzc.gdzcName }
                        </span>
                         <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="updateGdzc">
								<i class="fa fa-save"></i>保存
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;padding:0 12px;">
                           <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11">
                                             <li class="active" id="gdzcbaseMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">基本信息</a>
                                             </li>
                                             <li id="reduceMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">减少记录</a>
                                             </li>
                                             <li id="gdzcUpfileMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">固定资产附件<c:if test="${gdzc.fileNum > 0}"><span style="color:red;font-weight:bold;">（${gdzc.fileNum}）</span></c:if></a>
                                             </li>
                                             <li id="gdzcMaintainRecordLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">维修记录</a>
                                             </li>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<div id="gdzcBase" style="display:block;">
												<jsp:include page="./gdzcBase_edit.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherGdzcAttrIframe" style="display:none;" class="layui-layer-load"
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
