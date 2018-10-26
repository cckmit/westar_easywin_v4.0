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
	var sid="${param.sid}"
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
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
		
		$("#updateRsdaInc").on("click",function(){
			$(".subform").submit();
		})
	})
</script>
</head>
<body>
	<div class="container" style="padding:0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">查看奖惩信息</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="updateRsdaInc">
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">奖惩信息</span>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
										<li class="ticket-item no-shadow ps-listline" style="clear:both">
											<div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-user blue"></i>&nbsp;奖惩对象
											    	
											    </div>
											    <div class="ticket-user pull-left ps-right-box">
											    	<img class="user-avatar userImg" title="${rsdaInc.userName}" 
																src="/downLoad/userImg/${userInfo.comId }/${rsdaInc.userId}"/>
													<span class="user-name">${rsdaInc.userName}</span>
												</div>
											</div>
											<div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-bookmark blue"></i>&nbsp;奖惩项目
											    	
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  	<div class="pull-left">
														${rsdaInc.incName}
													</div>
												</div>
											</div>
                                          </li>
                                         
                                          <li class="ticket-item no-shadow ps-listline" style="clear:both">
	                                          <div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-star blue"></i>&nbsp;奖惩类型
											    	
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												   ${rsdaInc.incTypeName }
												</div>               
	                                          </div>
	                                          <div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-clock-o blue"></i>&nbsp;奖惩日期
											    	
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left" style="line-height: 30px;vertical-align: middle;">
													   ${rsdaInc.incentiveDate}
													</div>
												</div>               
	                                          </div>
                                          </li>
										<li class="ticket-item no-shadow ps-listline" >
									    	<div class="pull-left gray ps-left-text">
									    		<i class="fa fa-file blue"></i>&nbsp;奖惩说明
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;">
												   ${rsdaInc.remark}
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-file blue"></i>&nbsp;相关附件</div>
												<div class="ticket-user pull-left ps-right-box"
													style="width: 400px;height: auto;">
													<div>
														<c:choose>
															<c:when test="${not empty rsdaInc.listRsdaIncFiles}">
																<div class="file_div">
																<c:forEach items="${rsdaInc.listRsdaIncFiles}" var="rsdaIncFile" varStatus="vs">
																	<c:choose>
																		<c:when test="${rsdaIncFile.isPic==1}">
																			<p class="p_text">
																			附件(${vs.count})：
																				<img onload="AutoResizeImage(300,0,this,'')"
																				src="/downLoad/down/${rsdaIncFile.fileUuid}/${rsdaIncFile.fileName}?sid=${param.sid}" />
													 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${rsdaIncFile.fileUuid}/${rsdaIncFile.fileName}?sid=${param.sid}" title="下载"></a>
													 						<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);"onclick="showPic('/downLoad/down/${rsdaIncFile.fileUuid}/${rsdaIncFile.fileName}','${param.sid}','${rsdaIncFile.upfileId}','02803','${rsdaInc.id }')"></a>
																			</p>
																		</c:when>
																		<c:otherwise>
																			<p class="p_text">
																			附件(${vs.count})：
																				${rsdaIncFile.fileName}
																			<c:choose>
															 					<c:when test="${rsdaIncFile.fileExt=='doc' || rsdaIncFile.fileExt=='docx' || rsdaIncFile.fileExt=='xls' || rsdaIncFile.fileExt=='xlsx' || rsdaIncFile.fileExt=='ppt' || rsdaIncFile.fileExt=='pptx' }">
																	 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${rsdaIncFile.fileUuid}','${rsdaIncFile.fileName}','${param.sid}')"></a>
																	 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${rsdaIncFile.upfileId}','${rsdaIncFile.fileUuid}','${rsdaIncFile.fileName}','${rsdaIncFile.fileExt}','${param.sid}','02803','${rsdaInc.id }')"></a>
															 					</c:when>
															 					<c:when test="${rsdaIncFile.fileExt=='txt' ||rsdaIncFile.fileExt=='pdf'}">
															 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${rsdaIncFile.fileUuid}/${rsdaIncFile.fileName}?sid=${param.sid}" title="下载"></a>
																	 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${rsdaIncFile.upfileId}','${rsdaIncFile.fileUuid}','${rsdaIncFile.fileName}','${rsdaIncFile.fileExt}','${param.sid}','02803','${rsdaInc.id}')"></a>
															 					</c:when>
															 					<c:otherwise>
																	 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${rsdaIncFile.fileUuid}','${rsdaIncFile.fileName}','${param.sid}')"></a>
															 					</c:otherwise>
															 				</c:choose>
																			</p>
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</div>
															</c:when>
															<c:otherwise>
																未上传附件
															</c:otherwise>
														</c:choose>
													</div>
												</div>
												<div class="ps-clear"></div>
											</li>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                           <div class="widget-body no-shadow">
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
