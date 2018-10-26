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
	<script type="text/javascript" src="/static/js/zhbgJs/bgPurOrderJs/bgPurOrder.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	var sid = '${param.sid}';
	var purOrderId = "${bgypPurOrder.id}"
	var purUserId = "${bgypPurOrder.purUserId}"
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		//取得采购单详情信息
		getSelfJSON('/bgypPurDetail/listBgypPurDetail',{sid:sid,purOrderId:purOrderId},function(data){
			var listBgypPurDetails = data.listBgypPurDetails;
			//初始化数据
			initBgypPurDetailTable(listBgypPurDetails);
			
		})
		//提交表单
		$("body").on("click","#forceBackBtn,#agessPassBtn",function(){
			var spanId= $(this).attr("id");
			//默认审核通过
			var purOrderState = 2;
			if(spanId=='forceBackBtn'){
				//驳回
				purOrderState =3;
				window.top.layer.prompt({
					  formType: 2,
					  area:'400px',
					  closeBtn:0,
					  move: false,
					  title: '驳回采购单的意见'
					}, function(reason, index, elem){
						if(reason){
							window.top.layer.close(index);
							updateBgypPurOrderState(purOrderState,reason);
						}
					});
			}else{
				updateBgypPurOrderState(purOrderState,null)
			}
			
			
		})
	})
	
	function updateBgypPurOrderState(purOrderState,spContent){
		//取得采购单详情信息
		getSelfJSON('/bgypPurOrder/updateBgypPurOrderState',
					{sid:sid,
					purOrderId:purOrderId,
					purOrderState:purOrderState,
					spContent:spContent,
					purUserId:purUserId},function(data){
			if(data.status=='y'){
				showNotification(1,"操作成功")
				window.parent.location.reload();
				closeWindow();
			}
			
		})
	}
</script>
</head>
<body>
<form class="subform" method="post" id="bgypPurOrderForm">
<input type="hidden" name="purOrderState" id="purOrderState">
<input type="hidden" name="id" id="purOrderId "value="${bgypPurOrder.id }">
<div id="formData">
	
</div>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">
                        	<c:choose>
                        		<c:when test="${bgypPurOrder.purOrderState eq 1}">
		                       		审核采购单
                        		</c:when>
                        		<c:otherwise>
		                       		 查看采购单
                        		</c:otherwise>
                        	</c:choose>
                        </span>
                        	<c:choose>
                        		<c:when test="${bgypPurOrder.purOrderState eq 1}">
			                        <div class="widget-buttons ps-toolsBtn">
			                        	<a href="javascript:void(0)" class="blue" id="forceBackBtn">
											<i class="fa fa-circle-o"></i>驳回
										</a>
										<a href="javascript:void(0)" class="blue" id="agessPassBtn">
											<i class="fa fa-check-square-o"></i>通过
										</a>
									</div>
                        		</c:when>
                        	</c:choose>
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
                            	<span class="widget-caption blue">基础信息</span>
                                <div class="widget-buttons btn-div-full">
                                	<a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-minus blue"></i>
                                   </a>
                                 </div>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline" style="padding: 5px 0 !important;min-height: 40px">
                                         	<div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-bookmark blue"></i>&nbsp;单据编号
											    	
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  	<div class="pull-left">
														${bgypPurOrder.purOrderNum}
													</div>
												</div>
                                         	</div>
											<div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-user blue"></i>&nbsp;采购人
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
													<div class="ticket-user pull-left">
													
														<img class="user-avatar userImg" style="display:block;float: left"
														  title="${bgypPurOrder.purUserName}" 
															src="/downLoad/userImg/${bgypPurOrder.comId}/${bgypPurOrder.purUserId}"/>
														<span class="user-name" style="font-size:10px;">${bgypPurOrder.purUserName }</span>
													</div>
												</div>  
											</div>
                                         </li>
                                          <li class="ticket-item no-shadow ps-listline" style="clear:both;padding: 5px 0 !important;min-height: 40px">
	                                          <div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-clock-o blue"></i>&nbsp;采购时间
											    	
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  ${bgypPurOrder.bgypPurDate}
												</div>               
	                                          </div>
	                                          <div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-clock-o blue"></i>&nbsp;采购金额
											    	
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  ${bgypPurOrder.bgypTotalPrice}
												</div><span class="margin-left-5">元 </span>     
	                                          </div>
                                          </li>
                                          <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	<i class="fa fa-file blue"></i>&nbsp;备注信息
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													${bgypPurOrder.content}
												</div>
											</div>
                                         </li>
											<li class="ticket-item no-shadow ps-listline no-padding">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-file blue"></i>&nbsp;相关附件</div>
												<div class="ticket-user pull-left ps-right-box"
													style="width: 400px;height: auto;">
													<div class="margin-top-10">
														<div class="file_div">
															<c:forEach items="${bgypPurOrder.listBgypPurFiles}" var="purUpfiles" varStatus="vs">
																<c:choose>
																	<c:when test="${purUpfiles.isPic==1}">
																		<p class="p_text">
																		附件(${vs.count})：
																			<img onload="AutoResizeImage(300,0,this,'')"
																			src="/downLoad/down/${purUpfiles.fileUuid}/${purUpfiles.fileName}?sid=${param.sid}" />
												 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${purUpfiles.fileUuid}/${purUpfiles.fileName}?sid=${param.sid}" title="下载"></a>
												 						<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);"onclick="showPic('/downLoad/down/${purUpfiles.fileUuid}/${purUpfiles.fileName}','${param.sid}','${purUpfiles.upfileId}','027','${ques.id }')"></a>
																		</p>
																	</c:when>
																	<c:otherwise>
																		<p class="p_text">
																		附件(${vs.count})：
																			${purUpfiles.fileName}
																		<c:choose>
														 					<c:when test="${purUpfiles.fileExt=='doc' || purUpfiles.fileExt=='docx' || purUpfiles.fileExt=='xls' || purUpfiles.fileExt=='xlsx' || purUpfiles.fileExt=='ppt' || purUpfiles.fileExt=='pptx' }">
																 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${purUpfiles.fileUuid}','${purUpfiles.fileName}','${param.sid}')"></a>
																 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${purUpfiles.upfileId}','${purUpfiles.fileUuid}','${purUpfiles.fileName}','${purUpfiles.fileExt}','${param.sid}','027','${bgypPurOrder.id }')"></a>
														 					</c:when>
														 					<c:when test="${purUpfiles.fileExt=='txt' ||purUpfiles.fileExt=='pdf'}">
														 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${purUpfiles.fileUuid}/${purUpfiles.fileName}?sid=${param.sid}" title="下载"></a>
																 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${purUpfiles.upfileId}','${purUpfiles.fileUuid}','${purUpfiles.fileName}','${purUpfiles.fileExt}','${param.sid}','027','${bgypPurOrder.id}')"></a>
														 					</c:when>
														 					<c:otherwise>
																 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${purUpfiles.fileUuid}','${purUpfiles.fileName}','${param.sid}')"></a>
														 					</c:otherwise>
														 				</c:choose>
																		</p>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</div>
													</div>
												</div>
												<div class="ps-clear"></div>
											</li>
											<c:if test="${bgypPurOrder.purOrderState eq 2 or bgypPurOrder.purOrderState eq 3}">
												<li class="ticket-item no-shadow ps-listline" style="padding: 5px 0 !important;min-height: 40px">
		                                         	<div class="pull-left" style="width: 50%">
													    <div class="pull-left gray ps-left-text">
													    	<i class="fa fa-bookmark blue"></i>&nbsp;审核状态
													    	
													    </div>
														<div class="ticket-user pull-left ps-right-box">
														  	<div class="pull-left">
																<c:choose>
																	<c:when test="${bgypPurOrder.purOrderState eq 0}">待送审</c:when>
																	<c:when test="${bgypPurOrder.purOrderState eq 1}">待审核</c:when>
																	<c:when test="${bgypPurOrder.purOrderState eq 2}">已入库</c:when>
																	<c:when test="${bgypPurOrder.purOrderState eq 3}">未通过</c:when>
																</c:choose>
															</div>
														</div>
		                                         	</div>
														<div class="pull-left" style="width: 50%">
														    <div class="pull-left gray ps-left-text">
														    	<i class="fa fa-user blue"></i>&nbsp;审核人
														    </div>
															<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
																<div class="ticket-user pull-left">
																
																	<img class="user-avatar userImg" style="display:block;float: left"
																	 	title="${bgypPurOrder.spUserName}" 
																		src="/downLoad/userImg/${bgypPurOrder.comId}/${bgypPurOrder.spUser}"/>
																	<span class="user-name" style="font-size:10px;">${bgypPurOrder.spUserName }</span>
																</div>
															</div>  
														</div>
		                                         </li>
		                                         <c:if test="${bgypPurOrder.purOrderState eq 3}">
			                                         <li class="ticket-item no-shadow ps-listline">
													    <div class="pull-left gray ps-left-text">
													    	<i class="fa fa-file blue"></i>&nbsp;审核意见
													    </div>
														<div class="ticket-user pull-left ps-right-box">
														  	<div class="pull-left">
																${bgypPurOrder.spContent}
															</div>
														</div>
			                                         </li>
		                                         </c:if>
											</c:if>
                                   		</ul>
                                	</div>
                            	</div>
                          </div>
                           <div class="widget radius-bordered" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">采购单条目</span>
                                   <div class="widget-buttons btn-div-full">
                                      <a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-minus blue"></i>
                                       </a>
                                   </div>
                               </div>
                                <div class="widget-body no-shadow">
                               		<div class="tickets-container bg-white">
									<table class="table table-hover table-striped table-bordered table0" id="bgypPurDetailTable" style="width: 100%">
                               		 	<thead>
                               		 		<tr>
	                               		 		<th width="5%">序号</th>
												<th width="23%">办公用品名称</th>
												<th width="14%">单位</th>
												<th width="14%">规格</th>
												<th width="14%">单价(元)</th>
												<th width="14%">数量</th>
												<th width="14%" style="border-right: 0">金额</th>
												<th style="border-left: 0">&nbsp;</th>
											</tr>
                               		 	</thead>
                               		 	<tbody id="bgypPurDetailT0">
                               		 		
                               		 	</tbody>
                               		 	<tbody id="bgypPurDetailT1" style="border-top: 0">
                               		 		<tr>
                               		 			<td colspan="2">
                               		 				合计金额
                               		 			</td>
                               		 			<td colspan="4" style="text-align: right;" class="chinesePrice">
                               		 			</td>
                               		 			<td colspan="2"></td>
                               		 		</tr>
                               		 	</tbody>
                               		 </table>
                                </div>
                              </div>
                              <div class="ps-clear"></div>
                           </div>
                           <div class="widget-body no-shadow">
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
