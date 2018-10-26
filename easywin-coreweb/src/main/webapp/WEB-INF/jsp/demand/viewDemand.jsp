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
<link href="/static/assets/css/demand.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/static/js/demandJs/demandOpt.js"></script>

<script type="text/javascript">
	var sid="${param.sid}";
	var pageParam = {
			"sid":"${param.sid}",
			"userInfo":{
				"id":"${userInfo.id}",
				"name":"${userInfo.userName}",
				"comId":"${userInfo.comId}"
			},
			"tag":"updateOpt",
			"demand":{
				"id":"${demandProcess.id}",
				"serialNum":"${demandProcess.serialNum}",
				"expectFinishDate":"${demandProcess.expectFinishDate}",
				"state":"${demandProcess.state}",
				"creator":"${demandProcess.creator}",
				"handleUser":"${demandProcess.handleUser}"
			}
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
			$(".subform").submit();
		})
		
		$("body").on("click","#myTab11 li",function(){
			var liId = $(this).attr("id");
			var url = "/task/taskFlowRecord?sid=${param.sid}&taskId=${task.id}";
			if(liId == 'talkMenuLi'){
				//留言
				url = "/demandTalk/demandTalkPage?sid=${param.sid}&busId=${demandProcess.id}&busType=070&ifreamName=otherAttrIframe"
			}else if(liId == "spflowMenuLi"){
				//审核
				url = "/workFlow/busModSpflowList?sid=${param.sid}&busId=${demandProcess.id}&busType=070&ifreamName=otherAttrIframe"
			}else if(liId == "taskMenuLi"){
				//任务列表
				url = "/task/busModTaskList?sid=${param.sid}&pager.pageSize=10&ifreamName=otherAttrIframe&busId=${demandProcess.id}&busType=070&redirectPage="+encodeURIComponent(window.location.href);
			}else if(liId == "remindMenuLi"){
				//催办记录
				url="/busRemind/listPagedBusRemindForMod?sid=${param.sid}&pager.pageSize=10&busId=${demandProcess.id}&busType=070&ifreamName=otherAttrIframe";
			}else if(liId == "logMenuLi"){
				//操作日志
				url="/demand/listPagedDemandLog?sid=${param.sid}&pager.pageSize=10&busId=${demandProcess.id}&busType=070&ifreamName=otherAttrIframe";
			}else if(liId == "demandFileMenuLi"){
				//需求文档
				url="/demand/listPagedDemandUpfilePage?sid=${param.sid}&pager.pageSize=10&busId=${demandProcess.id}&busType=070&ifreamName=otherAttrIframe";
			}
			
			$(this).parent().find("li").removeAttr("class");
			$(this).attr("class","active");
			
			$("#otherAttrIframe").attr("src",url);
		})
	})

	
</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget" style="margin-top: 0px;">
					<div
						class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px">
						<img src="/static/assets/img/u631.png" width="28px">
						需求编号:${demandProcess.serialNum}</span>
						<div class="widget-buttons ps-toolsBtn" id="btnsDiv">
							<!-- 操作按钮见demandOpt.demandOptinitOptBtn -->
							<a href="javascript:void(0)"></a>
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
			                <div class="widget no-header">
			                    <div class="widget-body bordered-radius">
			                    
			                    	<div class="fm-left">
										<span>发布人：</span>
										<span>${demandProcess.creatorName }</span>
									</div>
									<div class="fm-right">
										<span>等级：</span>
										<span>
											<span class="rating">
												<c:forEach begin="1" end="${demandProcess.startLevel }">
													<span class="fa fa-star blue"></span>
												</c:forEach>
											</span>
										</span>
									</div>
									<div class="fm-left">
										<span>创建时间：</span>
										<span>${demandProcess.recordCreateTime }</span>
									</div>
									<div class="fm-right">
										<span>所属项目：</span>
										<span>${demandProcess.itemName }</span>
									</div>
									<div class="fm-left">
										<span>类型：</span>
										<span><tags:viewDataDic type="demandType" code="${demandProcess.type }"></tags:viewDataDic> </span>
									</div>
									<div class="fm-right">
										<span>项目负责人：</span>
										<span>${demandProcess.itemOwnerName }</span>
									</div>
									<div class="fm-left">
										<span>期待完成时间：</span>
										<span>${demandProcess.expectFinishDate }</span>
									</div>
									<div class="fm-right">
										<span>所属产品：</span>
										<span>${demandProcess.productName }</span>
									</div>
									<div class="fm-left">
										<span>进度：</span>
										<span>${demandProcess.stateName }</span>
									</div>
									<div class="fm-right">
										<span>修改模块：</span>
										<span>${demandProcess.itemModName }</span>
									</div>
									
									
									<div class="wedgt">
										<h5 class="child_title">事项进度</h5>
										<div class="fm_progress">
											<c:set var="preState" scope="page">yes</c:set>
											<c:forEach items="${demandProcess.listDemandHandleHis}" var="hisObj" varStatus="vs">
												<div class="pull-left itemDiv" ${vs.first?'style="margin-left:40px;"':'' } >
													
													<div class="quan pull-left">
														<span ${hisObj.curStep eq 1 or preState eq 'yes' ?'class="active"':'' } ></span>
													</div>
													<c:if test="${not vs.last }">
														<div class="line pull-left">
															<span ${hisObj.curStep eq 1 or preState eq 'yes' ?'class="active"':'' }></span>
														</div>
													</c:if>
													
													<c:set var="preState" scope="page">
														<c:choose>
															<c:when test="${hisObj.curStep eq 1}">no</c:when>
															<c:when test="${preState eq 'yes' }">yes</c:when>
															<c:otherwise>no</c:otherwise>
														</c:choose>
														<c:if test="${preState eq 'yes' }"></c:if>
													</c:set>
													
													<div class="ps-clear"></div>
													<div class="lc_name">
														<span class="active">${hisObj.stageName }</span>
													</div>
													<c:if test="${not empty hisObj.userName }">
														<div class="per_name">
															<span>${hisObj.userName }</span>
														</div>
													</c:if>
													<c:if test="${not empty hisObj.recordCreateTime }">
														<div class="lc_time">
															<span>${hisObj.recordCreateTime }</span>
														</div>
													</c:if>
												</div>
											</c:forEach>
											
										</div>
									</div>
									
									<div class="wedgt">
										<h5 class="child_title">需求描述</h5>
										<div class="fm_des">
											${demandProcess.describe}
										</div>
									</div>
									<div class="wedgt">
										<h5 class="child_title">验收标准</h5>
										<div class="fm_des">
											${demandProcess.standard}
										</div>
									</div>
			
			                    </div>
			                    
			                </div>
			            </div>
			            
			            <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;">
                                             <li class="active" id="talkMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">留言</a>
                                             </li>
                                             <li id="spflowMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">审核</a>
                                             </li>
                                             <li id="taskMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">任务列表</a>
                                             </li>
                                             <li id="remindMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">催办记录</a>
                                             </li>
                                             <li id="logMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">操作日志</a>
                                             </li>
                                             <li id="demandFileMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">需求文档</a>
                                             </li>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 <iframe id="otherAttrIframe" class="layui-layer-load"
												src="/demandTalk/demandTalkPage?sid=${param.sid}&busId=${demandProcess.id}&busType=070&ifreamName=otherAttrIframe"
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
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
