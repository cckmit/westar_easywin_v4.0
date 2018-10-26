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
			"carTypeId":function(gets,obj,curform,regxp){
				if($("#carTypeId").val()){
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
	$("#updateCar").click(function(){
		formSub();
	});
	
	//提交表单
	function formSub(){
		$(".subform").submit();
	}
});
//任务属性菜单切换
$(function(){
	//车辆信息
	$("#carbaseMenuLi").click(function(){
		$("#otherCarAttrIframe").css("display","none");
		$("#carBase").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$("#carbaseMenuLi").attr("class","active");
	});
	//保险记录
	$("#insuranMenuLi").click(function(){
		$("#otherCarAttrIframe").css("display","block");
		$("#carBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#insuranMenuLi").attr("class","active");
		$("#otherCarAttrIframe").attr("src","/car/insuranceRecordPage?sid=${param.sid}&carId=${car.id}&taskState=${task.state}");
	});
	//车辆维修记录
	$("#carMaintainRecordLi").click(function(){
		$("#otherCarAttrIframe").css("display","block");
		$("#carBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#carMaintainRecordLi").attr("class","active");
		$("#otherCarAttrIframe").attr("src","/car/carMaintainRecordPage?sid=${param.sid}&carId=${car.id}");
	});
	//车辆附件
	$("#carUpfileMenuLi").click(function(){
		$("#otherCarAttrIframe").css("display","block");
		$("#carBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#carUpfileMenuLi").attr("class","active");
		$("#otherCarAttrIframe").attr("src","/car/carUpfilePage?sid=${param.sid}&pager.pageSize=10&carId=${car.id}");
	});
	//车辆使用记录
	$("#carUseRecordMenuLi").click(function(){
		$("#otherCarAttrIframe").css("display","block");
		$("#carBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#carUseRecordMenuLi").attr("class","active");
		$("#otherCarAttrIframe").attr("src","/car/carUseRecordPage?sid=${param.sid}&pager.pageSize=10&carId=${car.id}");
	});
});
</script>
</head>
<body>
<form class="subform" method="post" action="/car/updateCar">
<input type="hidden" name = "id" value="${car.id }">
<tags:token></tags:token>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							
                        <span class="widget-caption themeprimary" style="font-size: 20px">车辆--</span>
                        <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px">
                        	${car.carNum }
                        </span>
                         <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="updateCar">
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
                                             <li class="active" id="carbaseMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">基本信息</a>
                                             </li>
                                             <li id="insuranMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">保险记录</a>
                                             </li>
                                             <li id="carUpfileMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">车辆附件<c:if test="${car.fileNum > 0}"><span style="color:red;font-weight:bold;">（${car.fileNum}）</span></c:if></a>
                                             </li>
                                             <li id="carMaintainRecordLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">维修记录</a>
                                             </li>
                                              <li id="carUseRecordMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">使用记录</a>
                                             </li>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<div id="carBase" style="display:block;">
												<jsp:include page="./carBase_edit.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherCarAttrIframe" style="display:none;" class="layui-layer-load"
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
