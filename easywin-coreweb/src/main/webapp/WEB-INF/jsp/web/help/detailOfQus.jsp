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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script src="/static/js/taskJs/taskCenter.js" type="text/javascript"
	charset="utf-8"></script>
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
	function setWindow(winDoc, win) {
		openWindowDoc = winDoc;
		openWindow = win;
		openPageTag = openWindow.pageTag;
		openTabIndex = openWindow.tabIndex;
	}
	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	//双击移除父级
	function removeP() {
		$("#pName").val('');
		$("#pId").val(0);
	}
	//父级关联
	function appendP() {
		window.top.layer
				.open({
					type : 2,
					title : [ '项目列表', 'font-size:18px;' ],
					closeBtn : 0,
					area : [ '550px', '550px' ],
					fix : true, //不固定
					maxmin : false,
					move : false,
					content : [ "/web/help/listQusForP?pager.pageSize=7&id=${helpVo.id}", "no" ],
					btn : [ '选择', '取消' ],
					yes : function(index, layero) {
						var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						var result = iframeWin.helpSelected();
						if (result) {
							$("#pName").val(result.helpName);
							$("#pId").val(result.helpId);
							//重新排序
							order(result.helpId);
							window.top.layer.close(index)
						}
					},
					cancel : function() {
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
	$(function(){
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
	});
	function updateHelp(){
		$("#subform").submit();
	}
</script>
</head>
<body>
	<form id="subform" class="subform" method="post" action="/web/help/updateHelp">
		<input type="hidden" name="id" value="${helpVo.id}">
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div
							class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">帮助维护</span>
							<div class="widget-buttons ps-toolsBtn">
								<a href="javascript:void(0)" class="blue"
									onclick="updateHelp();">
									<i class="fa fa-save"></i>更新
								</a>
								<!-- 判断显示按钮 -->
							</div>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
									<i class="fa fa-times"></i>
								</a>
							</div>
						</div>
						<!--Widget Header-->
						<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
						<div class="widget-body margin-top-40" id="contentBody"
							style="overflow: hidden;overflow-y:scroll;padding:0 12px;">
							<div class="widget radius-bordered" style="clear:both">
								<div class="widget-header bg-bluegray no-shadow">
									<span class="widget-caption blue">基础信息</span>
									<div class="widget-buttons btn-div-full">
										<a class="ps-point btn-a-full" data-toggle="collapse"> <i
											class="fa fa-minus blue"></i>
										</a>
									</div>
								</div>
								<div class="widget-body no-shadow" style="padding:0 12px;">
									<div class="tickets-container bg-white">
										<ul class="tickets-list">
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-bookmark blue"></i>&nbsp;标题
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<input id="name" datatype="input,sn" defaultLen="52"
														name="name" nullmsg="请填标题"
														class="colorpicker-default form-control" type="text"
														title="${helpVo.name}" value="${helpVo.name}"
														style="width: 400px;float: left">
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="gray ps-left-text"
													style="position:absolute;left:0;">
													<i class="fa fa-file-text-o blue"></i>&nbsp;描述
												</div>
												<div class="ps-right-box"
													style="margin-left:105px;line-height:25px;">
													<textarea class="form-control" id="describe" rows=""
														cols="" name="describe" style="width:90%">${helpVo.describe}</textarea>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-star blue"></i>&nbsp;父级
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<input type="hidden" id="pId" name="pId" value="${helpVo.pId}">
													<input class="colorpicker-default form-control pull-left"
														type="text" id="pName" readonly="readonly"
														value="${helpVo.pName}" ondblclick="removeP()"
														title="双击移除" placeholder="关联父级"
														style="cursor:auto;width: 445px;float:left;"> <a
														href="javascript:void(0);" class="fa fa-chain pull-left"
														title="父级关联" onclick="appendP();"
														style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px"></a>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-arrow-up blue"></i>&nbsp;排序
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<input name="orderNum" id="orderNum"
														class="colorpicker-default form-control" type="text"
														maxlength="5" value="${helpVo.orderNum}"
														datatype="orderNum" nullmsg="请填写排列顺序"
														style="width:70px;float:left">
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-ban blue"></i>&nbsp;显示状态
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<select name="openState">
														<option value="1" ${helpVo.openState=='1'?'selected=\"selected\"':''}>显示</option>
														<option value="0" ${helpVo.openState=='0'?'selected=\"selected\"':''}>隐藏</option>
													</select>
												</div>
											</li>
										</ul>
									</div>
								</div>
							</div>

							<div class="widget radius-bordered" style="clear:both;${helpVo.sunNum>0?'display:none':'display:block'}">
								<div class="widget-header bg-bluegray no-shadow">
									<span class="widget-caption blue">问题解答 </span>
									<div class="widget-buttons btn-div-full">
										<a class="ps-point btn-a-full" data-toggle="collapse"> <i
											class="fa fa-minus blue"></i>
										</a>
									</div>
								</div>
								<div class="widget-body no-shadow">
									<div class="tickets-container bg-white">
										<textarea class="form-control" id="content"
											name="content" rows="" cols=""
											style="width:685px;height: 110px;display:none;"
											name="content">${helpVo.content}</textarea>
										<iframe ID="eWebEditor1"
											src="/static/plugins/ewebeditor/ewebeditor.htm?id=content&style=blue"
											frameborder="0" scrolling="no" width="99%" height="350"></iframe>
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
	<script type="text/javascript">
		var jq11 = $.noConflict(true);
	</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
