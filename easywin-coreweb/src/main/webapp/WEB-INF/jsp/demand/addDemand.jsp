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
<link href="/static/assets/css/task.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/static/js/demandJs/demandOpt.js"></script>

<script type="text/javascript">
	var sid="${param.sid}";
	var pageParam= {
			"sid":"${param.sid}"
	}
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
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
		 
		 $("body").on("click","#addDeamand",function(){
			 var startLevel = $("#startLevel").val();
			 if(!startLevel){
				scrollToLocation($("#contentBody"),$("#startLevelSpan"));
				layer.tips("请选择星级！",$("#startLevelSpan"),{"tips":1});
				
				return false;
			 }
			 $(".subform").submit();
		 })
	})

	
</script>
</head>
<body>
<form class="subform" method="post" action="/demand/addDemand">
	<tags:token></tags:token>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget" style="margin-top: 0px;">
					<div
						class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px">需求发布</span>
						<div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="addDeamand"> 
								<i class="fa fa-save"></i>发布 
							</a>
						</div>
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
			                                    <div class="pull-left task-left-text task-height">
			                                    	<span style="color: red">*</span>星级：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
			                                    		<input type="hidden" name="startLevel" id="startLevel" value="">
			                                    		<span class="rating" id="startLevelSpan">
				                                    		<span class="fa fa-star-o blue pull-left" style="cursor: pointer;"></span>
				                                    		<span class="fa fa-star-o blue pull-left" style="cursor: pointer;"></span>
				                                    		<span class="fa fa-star-o blue pull-left" style="cursor: pointer;"></span>
				                                    		<span class="fa fa-star-o blue pull-left" style="cursor: pointer;"></span>
				                                    		<span class="fa fa-star-o blue pull-left" style="cursor: pointer;"></span>
			                                    		</span>
			                                        </div>
			                                    </div>
			                                </li>
			                            </ul>
			                        </div>
			                    </div>
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list">
			                                 <li class="ticket-item no-shadow clearfix ticket-normal">
			                                    <div class="pull-left task-left-text">
			                                    	<span style="color: red">*</span>需求描述：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
				                                        <textarea id="describe" name="describe" rows="" cols="" class="form-control"
														style="width:650px;height: 110px;display:none;"></textarea> 
														<iframe ID="eWebEditorA" src="/static/plugins/ewebeditor/ewebeditor.htm?id=describe&style=expand600" 
														frameborder="0" scrolling="no" width="650" height="280"></iframe>
													</div>
			                                    </div>
			                                </li>
			                            </ul>
			                        </div>
			                    </div>
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list">
			                                 <li class="ticket-item no-shadow clearfix ticket-normal">
			                                    <div class="pull-left task-left-text">
			                                    	<span style="color: red">*</span>验收标准：
			                                    </div>
			                                    <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
			                                    	<div class="row">
				                                        <textarea id="standard" name="standard" rows="" cols="" class="form-control"
														style="width:650px;height: 110px;display:none;"></textarea> 
														<iframe ID="eWebEditorB" src="/static/plugins/ewebeditor/ewebeditor.htm?id=standard&style=expand600" 
														frameborder="0" scrolling="no" width="650" height="280"></iframe>
													</div>
			                                    </div>
			                                </li>
			                            </ul>
			                        </div>
			                    </div>
			                </div>
			                
			                <div class="widget no-header ">
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list">
			                                <li class="ticket-item no-shadow clearfix ticket-normal">
			                                    <div class="pull-left task-left-text task-height">
			                                                                                                 文档说明：
			                                    </div>
			                                    <div class="pull-left col-lg-4 col-sm-4 col-xs-4">
			                                        <div class="row">
			                                            <tags:uploadMore name="listDemandFile.upfileId" showName="fileName"
															ifream="" comId="${userInfo.comId}"></tags:uploadMore>
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
			                                <ul class="tickets-list clearfix">
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
			                                        <div class="pull-left task-left-text task-height">需求类型：</div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <tags:dataDic type="demandType" name="type" please="t"></tags:dataDic>
			                                            </div>
			                                        </div>
			                                    </li>
			                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-6 col-sm-6 col-xs-6">
			                                        <div class="pull-left task-left-text task-height">
			                                        	<span style="color: red">*</span>项期待完成时间：
			                                        </div>
			                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
			                                        	<div class="row">
				                                            <input type="text" class="form-control" placeholder="完成时限" readonly="readonly" id="expectFinishDate"
																	name="expectFinishDate" onClick="WdatePicker({minDate:'%y-%M-{%d}'})"/>
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
			                            <ul class="tickets-list clearfix">
		                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-8 col-sm-8 col-xs-8">
		                                        <div class="pull-left task-left-text task-height">
		                                        	<span style="color: red">*</span>项目关联：
		                                        </div>
		                                        <div class="pull-left col-lg-8 col-sm-8 col-xs-8">
		                                        	<div class="row">
		                                        		<input type="hidden" name="itemId"/>
			                                            <input type="text" id="itemName" class="form-control pull-left colInput" readonly="readonly" 
			                                            	title="双击移除" style="cursor:pointer;width:85%;"/>
			                                            <a href="javascript:void(0);" class="pull-left margin-top-10 padding-left-5" 
			                                            	style="font-size:10px;" opt-selectBtn="itemSelectForDemand">选择</a>
		                                            </div>
		                                        </div>
		                                    </li>
		                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
		                                        <div class="pull-left task-left-text task-height">
		                                        	项目负责人：
		                                        </div>
		                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
		                                        	<div class="row">
		                                        		<input type="hidden" name="handleUser"/>
			                                            <input type="text" class="form-control" readonly="readonly" id="itemOwner" />
													</div>
		                                        </div>
		                                    </li>
		                                </ul>
			                           
			                        </div>
			                    </div>
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list clearfix">
		                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-8 col-sm-8 col-xs-8">
		                                        <div class="pull-left task-left-text task-height">产品关联：</div>
		                                         <div class="pull-left col-lg-8 col-sm-8 col-xs-8">
		                                        	<div class="row">
		                                        		<input type="hidden" name="productId"/>
			                                       		<input type="text" id="productName" name="productName" class="form-control pull-left colInput" 
			                                       			readonly="readonly" style="width:85%;" />
		                                            </div>
		                                        </div>
		                                    </li>
		                                </ul>
			                        </div>
			                    </div>
			                    <div class="widget-body bordered-radius">
			                        <div class="tickets-container tickets-bg tickets-pd">
			                            <ul class="tickets-list clearfix">
		                                    <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-8 col-sm-8 col-xs-8">
		                                        <div class="pull-left task-left-text task-height">修改模块：</div>
		                                         <div class="pull-left col-lg-8 col-sm-8 col-xs-8">
		                                        	<div class="row">
		                                        		<input type="hidden" name="itemModId" id="itemModId"/>
			                                            <input type="text" id="itemModName" name="itemModName" class="form-control pull-left colInput" readonly="readonly" 
			                                            	 title="双击移除" style="cursor:pointer;width:85%;" ondblclick="removeProductMod()"/>
			                                            <a href="javascript:void(0);" class="pull-left margin-top-10 padding-left-5" style="font-size:10px;" opt-selectBtn="itemModSelectForDemand">选择</a>
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
