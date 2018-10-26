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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
	});
	

</script>
</style> 
</head>
<body >
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row">
			<div class="col-md-12 col-xs-12 ">
				<div class="widget" style="margin-top: 0px;">
					<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 25px">${organic.orgName}</span>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
					</div>
					<!--Widget Header-->
					
					   <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered" style="border: 0;">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">企业信息</span>
                                <div class="widget-buttons btn-div-full">
                                	<a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-minus blue"></i>
                                   </a>
                                 </div>
                         	</div>
                            <div class="widget-body no-shadow" >
									<input type="hidden" value="${organic.id}" name="id" /> 
									<input type="hidden" name="redirectPage" />
									<ul class="tickets-list">
										<li class="ticket-item no-shadow ps-listline "
											style="border: 0; ">
											<div class="pull-left gray ps-left-text"
												style="font-size: 15px">&nbsp;团队编号:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly"
													style="width: 300px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${organic.orgNum}</span>
												</div>
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline "
											style="border: 0; ">
											<div class="pull-left gray ps-left-text "
												style="font-size: 15px">&nbsp;团队名称:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly"
													style="width: 300px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${organic.orgName}</span>
												</div>
											</div>
										</li>
											<c:if test="${not empty organic.linkerName}">
										<li class="ticket-item no-shadow ps-listline "
											style="border: 0; ">
											<div class="pull-left gray ps-left-text "
												style="font-size: 15px">&nbsp;联系人:</div>
											<div class="ticket-user pull-left ps-right-box">
														<div readonly="readonly"
															style="width: 300px;border: 1px solid #d5d5d5;">
															<span class="padding-left-10">${organic.linkerName}</span>
														</div>
											</div>
										</li>
										</c:if>
										<c:if test="${not empty organic.linkerEmail}">
										<li class="ticket-item no-shadow ps-listline "
											style="border:0; ">
											<div class="pull-left gray ps-left-text"
												style="font-size: 15px">&nbsp;联系邮箱:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly"
													style="width: 300px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${organic.linkerEmail}</span>
												</div>
											</div>
										</li>
										</c:if>
										<c:if test="${not empty organic.address}">
										<li class="ticket-item no-shadow autoHeight no-padding "
											style="border: 0 ">
											<div class="pull-left gray ps-left-text padding-top-10"
												style="font-size: 15px">&nbsp;公司地址:</div>
											<div class="ticket-user pull-left ps-right-box"
												style="height: auto;">
														<div readonly="readonly"
															class="margin-top-10 padding-top-5 padding-bottom-10"
															style="width: 300px;border: 1px solid #d5d5d5;">
															<span class="padding-left-10">${organic.address}</span>
														</div>
											</div>
											<div class="ps-clear"></div>
										</li>
										</c:if>
									</ul>
                            </div>
                          </div>
                           <div class="widget radius-bordered collapsed" style="clear:both;border: 0;" >
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">注册人信息</span>
                                   <div class="widget-buttons btn-div-full">
                                      <a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-plus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               <div class="widget-body no-shadow">
                               	<ul class="tickets-list">
                               	<c:if test="${not empty userInfo.userName}">
										<li class="ticket-item no-shadow ps-listline "
											style="border: 0; ">
											<div class="pull-left gray ps-left-text"
												style="font-size: 15px">&nbsp;姓名:&nbsp;</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly"
													style="width: 300px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${userInfo.userName}</span>
												</div>
											</div>
										</li>
										</c:if>
										<c:if test="${not empty userInfo.linePhone}">
										<li class="ticket-item no-shadow ps-listline "
											style="border:0; ">
											<div class="pull-left gray ps-left-text"
												style="font-size: 15px">&nbsp;固定电话:&nbsp;</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly"
													style="width: 300px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${userInfo.linePhone}</span>
												</div>
											</div>
										</li>
										</c:if>
										<c:if test="${not empty userInfo.movePhone}">
										<li class="ticket-item no-shadow ps-listline "
											style="border: 0; ">
											<div class="pull-left gray ps-left-text "
												style="font-size: 15px">&nbsp;移动电话:&nbsp;</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly"
													style="width: 300px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${userInfo.movePhone}</span>
												</div>
											</div>
										</li>
										</c:if>
										<c:if test="${not empty userInfo.email}">
										<li class="ticket-item no-shadow ps-listline "
											style="border: 0; ">
											<div class="pull-left gray ps-left-text "
												style="font-size: 15px">&nbsp;电子邮件:&nbsp;</div>
											<div class="ticket-user pull-left ps-right-box">
														<div readonly="readonly"
															style="width: 300px;border: 1px solid #d5d5d5;">
															<span class="padding-left-10">${userInfo.email}</span>
														</div>
											</div>
										</li>
										</c:if>
										<c:if test="${not empty userInfo.wechat}">
										<li class="ticket-item no-shadow ps-listline "
											style="border: 0 ">
											<div class="pull-left gray ps-left-text "
												style="font-size: 15px">&nbsp;微信:&nbsp;</div>
											<div class="ticket-user pull-left ps-right-box">
														<div readonly="readonly"
															style="width: 300px;border: 1px solid #d5d5d5;">
															<span class="padding-left-10">${userInfo.wechat}</span>
														</div>
											</div>
										</li>
										</c:if>
										<c:if test="${not empty userInfo.qq}">
										<li class="ticket-item no-shadow ps-listline "
											style="border: 0 ">
											<div class="pull-left gray ps-left-text "
												style="font-size: 15px">&nbsp;QQ:&nbsp;</div>
											<div class="ticket-user pull-left ps-right-box">
														<div readonly="readonly"
															style="width: 300px;border: 1px solid #d5d5d5;">
															<span class="padding-left-10">${userInfo.qq}</span>
														</div>
											</div>
										</li>
										</c:if>
										<c:if test="${not empty userInfo.birthday}">
										<li class="ticket-item no-shadow ps-listline "
											style="border: 0 ">
											<div class="pull-left gray ps-left-text "
												style="font-size: 15px">&nbsp;生日:&nbsp;</div>
											<div class="ticket-user pull-left ps-right-box">
														<div readonly="readonly"
															style="width: 300px;border: 1px solid #d5d5d5;">
															<span class="padding-left-10">${userInfo.birthday}</span>
														</div>
											</div>
										</li>
										</c:if>
										<c:if test="${not empty userInfo.selfIntr}">
										<li class="ticket-item no-shadow autoHeight no-padding "
											style="border: 0 ">
											<div class="pull-left gray ps-left-text padding-top-10"
												style="font-size: 15px">&nbsp;个性签名:&nbsp;</div>
											<div class="ticket-user pull-left ps-right-box"
												style="height: auto;">
														<div readonly="readonly"
															class="margin-top-10 padding-top-5 padding-bottom-10"
															style="width: 300px;border: 1px solid #d5d5d5;">
															<span class="padding-left-10">${userInfo.selfIntr}</span>
														</div>
											</div>
											<div class="ps-clear"></div>
										</li>
										</c:if>
									</ul>
                              </div>
                           </div>
                        </div>
				</div>
			</div>
			<!-- /Page Body -->
		</div>

	</div>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
	<style>
.ps-left-text {
	min-width: 80px !important;
	text-align: right !important;
}
</style>
</body>
</html>
