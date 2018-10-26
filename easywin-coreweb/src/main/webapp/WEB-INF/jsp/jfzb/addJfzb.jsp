<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title><%=SystemStrConstant.TITLE_NAME%></title>
<meta name="description" content="Dashboard" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script src="/static/js/jfzbJs/jfzb.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	var EasyWin = {
		"sid" : "${param.sid}"
	};
	
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
				validMsgV2(msg, o, cssctl);
			},
			datatype:{
				"input":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(str){
						var count = str.replace(/[^\x00-\xff]/g,"**").length;
						var len = $(obj).attr("defaultLen");
						if(count>len){
							return "积分指标太长";
						}else{
							return true;
						}
					}else{
						return false;
					}
				},
				"score":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(str){
						var regFloat = /^[-]?((0|([1-9]\d*))(\.\d{1})?)?$/
						if(regFloat.test(str)){
							return true
						}else{
							return "请填写至多一位小数"
						}
					}else{
						return false;
					}
				}
			},
			beforeSubmit : function(curform) {
				var deps = $("#listJfzbDepScope_select").find("option");
				if(!deps || !deps.get(0)){
					layer.tips("请选择适用范围！",$(".depSelectBtn"),{tips:3});
					return false;
				}
				var jfBottom = $("#jfBottom").val();
				jfBottom = Number(jfBottom);
				var jfTop = $("#jfTop").val();
				jfTop = Number(jfTop);
				if(jfBottom>jfTop){
					layer.tips("最低分不能超过最高分！",$("#jfBottom"),{tips:3});
					return false;
				}
				return true;
			},
			beforeCheck : function(curform) {
				
			},
			showAllError : true
		});
		jfzbOptForm.initForm();
		
		$("body").on("click","#addJfzb",function(){
			$(".subform").submit();
		})
	});
</script>
<style type="text/css">
.ps-listline {
	line-height: 30px !important
}
</style>
</head>
<body>
	<form class="subform" method="post" action="/jfzb/addJfzb">
		<tags:token></tags:token>
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">积分指标添加</span>
							<div class="widget-buttons ps-toolsBtn">
								<a href="javascript:void(0)" class="blue" id="addJfzb">
									<i class="fa fa-save"></i>
									添加
								</a>
							</div>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
									<i class="fa fa-times themeprimary"></i>
								</a>
							</div>
						</div>
						<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
						<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
							<div class="widget radius-bordered">
								<div class="widget-header bg-bluegray no-shadow">
									<span class="widget-caption blue">基础配置</span>
									<div class="widget-buttons btn-div-full">
										<a class="ps-point btn-a-full" data-toggle="collapse">
											<i class="fa fa-minus blue"></i>
										</a>
									</div>
								</div>
								<div class="widget-body no-shadow">
									<div class="tickets-container bg-white">
										<ul class="tickets-list">
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-bookmark blue"></i>
													&nbsp;积分类别 <span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<select class="populate" datatype="*"  name="jfzbTypeId" style="cursor:auto;width: 200px">
														<optgroup label="选择指标类型"></optgroup>
							 						</select>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-bookmark blue"></i>
													&nbsp;积分指标 <span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 500px">
				                                       <input type="text" name="leveTwo" style="width: 400px"
				                                        class="form-control pull-left" datatype="input,sn" 
				                                        	placeholder="二级指标 " nullmsg="请填写二级指标 "/>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline" >
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-bookmark blue"></i>
													&nbsp;得分范围<span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 500px">
				                                       <input type="text" id="jfBottom" name="jfBottom" style="width: 150px"
				                                        class="form-control pull-left" datatype="score" placeholder="最低分 " nullmsg="请填写最低分 "/>
				                                        <span class="pull-left" style="display: inline-block;width: 30px;text-align: center;">至</span>
				                                        <input type="text" id="jfTop" name="jfTop" style="width: 150px"
				                                        class="form-control pull-left" datatype="score"  placeholder="最高分 " nullmsg="请填写高分 "/>
												</div>
											</li>
											 <li class="ticket-item no-shadow autoHeight no-padding" style="clear: both;">
												<div class="pull-left gray ps-left-text padding-top-10">
													<i class="fa fa-user blue"></i>
													&nbsp;适用范围<span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box" id="scope" style="min-width: 135px;height: auto;">
													<div id="listJfzbDepScope_selectDiv" class="pull-left margin-top-10" style="max-width: 450px">
														
													</div>
													<label class="padding-left-5 margin-top-10">
														<button class="btn btn-info btn-primary btn-xs depSelectBtn"
														 relateSelect="listJfzbDepScope_select" relateDiv="listJfzbDepScope_selectDiv"
														 ismult="1" type="button">按部门</button>
													</label>
												</div>
												<div style="float: left;width:50px;display: none">
													<select list="listJfzbDepScope" listkey="depId" listvalue="depName" id="listJfzbDepScope_select" name="listJfzbDepScope.dep" 
														multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
													</select>
												</div>
												<div style="clear: both;"></div>
											</li>
											<li class="ticket-item no-shadow autoHeight no-padding" style="clear: both;" >
										    	<div class="pull-left gray ps-left-text padding-top-10">
										    		考核标准
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
											  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" 
											  			style="height:150px;" 
											  			id="describe" name="describe" rows="" cols=""></textarea>
												</div> 
												<div class="ps-clear"></div>              
	                                         </li>
	                                         <li class="ticket-item no-shadow autoHeight no-padding" >
										    	<div class="pull-left gray ps-left-text padding-top-10">
										    		备注
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
											  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;" 
											  			id="remark" name=remark rows="" cols=""></textarea>
												</div> 
												<div class="ps-clear"></div>              
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
	</form>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);
	</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
