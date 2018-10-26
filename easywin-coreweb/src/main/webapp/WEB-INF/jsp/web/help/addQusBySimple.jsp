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
<script type="text/javascript">
	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height() - 40;
		$("#contentBody").css("height", height + "px");

		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			datatype : {
				"input" : function(gets, obj, curform, regxp) {
					var str = $(obj).val();
					if (str) {
						var count = str.replace(/[^\x00-\xff]/g, "**").length;
						var len = $(obj).attr("defaultLen");
						if (count > len) {
							return "名称太长";
						} else {
							return true;
						}
					} else {
						return false;
					}
				},"orderNum" : function(gets, obj, curform, regxp) {
					var num = $(obj).val();
					if (num) {
						if (!(/(^[1-9]\d*$)/.test(num))) { 
							return "请输入正整数";
					　　　　}
					} else {
						return false;
					}
				}
			},
			callback : function(form) {
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		//form表单提交
		$("#addQus").click(function(){
			$(".subform").submit();
		});
	})
	function formSub() {
		$(".subform").submit();
	}
	$(document).ready(function() {
		$("#name").focus();
	});
	//双击移除父级
	function removeP(){
		$("#pName").val('');
		$("#pId").val(0);
	}
	//父级关联
	function appendP() {
		window.top.layer.open({
			 type: 2,
			  title: ['项目列表', 'font-size:18px;'],
			  closeBtn:0,
			  area: ['550px', '550px'],
			  fix: true, //不固定
			  maxmin: false,
			  move: false,
			  content: ["/web/help/listQusForP?pager.pageSize=7","no"],
			  btn: ['选择', '取消'],
			  yes: function(index, layero){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  var result = iframeWin.helpSelected();
				  if(result){
					  $("#pName").val(result.helpName);
					  $("#pId").val(result.helpId);
					  //重新排序
					  order(result.helpId);
					  window.top.layer.close(index)
				  }
			  }
			  ,cancel: function(){ 
			    //右上角关闭回调
			  }
		});
	}
	//重新排序
	function order(pId){
		if(pId){
			$.post("/web/help/nextOrderNum",{Action:"post",pId:pId},     
				function (data){
				if(data){
				  $("#orderNum").val(data);
	 			}
			},"json");
		}
	}
</script>
</head>
<body>
	<form class="subform" method="post" action="/web/help/addQus">
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div
							class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">新增疑问</span>
							<div class="widget-buttons ps-toolsBtn">
								<a href="javascript:void(0)" class="blue" id="addQus"> <i
									class="fa fa-save"></i>提问
								</a>
							</div>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
									<i class="fa fa-times"></i>
								</a>
							</div>
						</div>
						<!--Widget Header-->
						<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
						<div class="widget-body margin-top-20" id="contentBody"
							style="overflow: hidden;overflow-y:scroll;">
							<div class="widget radius-bordered">
								<div class="no-shadow" style="padding:20px 12px 5px 12px;">
									<div class="tickets-container bg-white">
										<ul class="tickets-list">
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-bookmark blue"></i>&nbsp;标题 <span
														style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div>
														<input id="name" datatype="input,sn" name="name"
															nullmsg="请填写标题" class="colorpicker-default form-control"
															type="text" value="" style="width: 445px;float: left">
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline"
												style="height:165px;">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-file-text-o blue"></i>&nbsp;描述
												</div>
												<div
													class="ticket-user pull-left ps-right-box padding-bottom-5"
													style="width:80%;">
													<textarea class="form-control" id="describe"
														name="describe" rows="" cols=""
														style="width:80%;height: 150px;float:left" name="describe"></textarea>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline"
												style="clear:both">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-star blue"></i>&nbsp;父级
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<input type="hidden" id="pId" name="pId" value="${pId}">
													<input class="colorpicker-default form-control pull-left"
														type="text" id="pName" readonly="readonly" value="${pHelpVo.name}"
														ondblclick="removeP()" title="双击移除" placeholder="关联父级"
														style="cursor:auto;width: 445px;float:left;"> <a
														href="javascript:void(0);" class="fa fa-chain pull-left"
														title="父级关联" onclick="appendP();"
														style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px"></a>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-arrow-up blue"></i>&nbsp;排序 <span
														style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<div>
														<input name="orderNum" id="orderNum"
															class="colorpicker-default form-control" type="text"
															maxlength="5" value="${nextOrderNum}" datatype="orderNum"
															nullmsg="请填写排列顺序" style="width:70px;float:left">
													</div>
												</div>
											</li>
										</ul>
									</div>
								</div>
							</div>
							<div class="widget-body no-shadow"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">
		var jq11 = $.noConflict(true);
	</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
