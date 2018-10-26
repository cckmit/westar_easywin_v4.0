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
	<script type="text/javascript" src="/static/js/zhbgJs/bgypApplyJs/bgypApply.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	var sid = '${param.sid}';
	var applyId = "${bgypApply.id}"
	var applyUserId = "${bgypApply.applyUserId}"
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
		getSelfJSON('/bgypApplyDetail/listBgypApplyDetail',{sid:sid,bgypApplyId:applyId},function(data){
			var listBgypApplyDetails = data.listBgypApplyDetails;
			//初始化数据
			initBgypApplyDetailTable(listBgypApplyDetails);
			
		})
		//提交表单
		$("body").on("click","#forceBackBtn,#agessPassBtn",function(){
			var spanId= $(this).attr("id");
			//默认审核通过
			var applyState = 2;
			if(spanId=='forceBackBtn'){
				//驳回
				applyState =3;
				window.top.layer.prompt({
					  formType: 2,
					  area:'400px',
					  closeBtn:0,
					  move: false,
					  title: '驳回用品申领的意见'
					}, function(reason, index, elem){
						if(reason){
							window.top.layer.close(index);
							updateBgypApplyState(applyState,reason)
						}
					});
			}else{
				updateBgypApplyState(applyState,null)
			}
			
			
			
		})
	})
	
	function updateBgypApplyState(applyState,spContent){
		//取得采购单详情信息
		getSelfJSON('/bgypApply/updateBgypApplyState',
					{sid:sid,
					applyId:applyId,
					applyState:applyState,
					spContent:spContent,
					applyUserId:applyUserId},function(data){
			if(data.status=='y'){
				showNotification(1,"操作成功")
				window.parent.location.reload();
				closeWindow();
			}else{
				var listLossApply = data.listLossApply;
				if(listLossApply && listLossApply.length>0){
					var msgInfo = "";
					$.each(listLossApply,function(index,obj){
						msgInfo +="["+obj.bgypName+"]库存不足,差:"+obj.applyNum+"<br>"
					});
					window.top.layer.alert(msgInfo,{icon:2,title:false,closeBtn:0,yes:function(index,layero){window.top.layer.close(index);window.self.location.reload()}});
				}
			}
			
		})
	}
</script>
</head>
<body>
<form class="subform" method="post" id="bgypPurOrderForm">
<input type="hidden" name="purOrderState" id="purOrderState">
<input type="hidden" name="id" value="${bgypApply.id }">
<div id="formData">
	
</div>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">
                        	<c:choose>
                        		<c:when test="${bgypApply.applyCheckState eq 1}">
		                       		申领单审核
                        		</c:when>
                        		<c:otherwise>
		                       		 查看申领单
                        		</c:otherwise>
                        	</c:choose>
                        </span>
                        	<c:choose>
                        		<c:when test="${bgypApply.applyCheckState eq 1}">
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
											    	<i class="fa fa-user blue"></i>&nbsp;申领人
											    	
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
													<div class="ticket-user pull-left">
														<img  style="display:block;float: left" class="user-avatar" 
														src="/downLoad/userImg/${bgypApply.comId}/${bgypApply.userId}?sid=${param.sid}" title="${bgypApply.userName }">
														<span class="user-name" style="font-size:10px;">${bgypApply.userName }</span>
													</div>
												</div>  
											</div>
	                                          <div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-clock-o blue"></i>&nbsp;申请时间
											    	
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  ${bgypApply.applyDate}
												</div>               
	                                          </div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	<i class="fa fa-file blue"></i>&nbsp;申领理由
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													${bgypApply.remark}
												</div>
											</div>
                                         </li>
                                         
                                         <c:if test="${bgypApply.applyCheckState eq 2 or bgypApply.applyCheckState eq 3}">
												<li class="ticket-item no-shadow ps-listline" style="padding: 5px 0 !important;min-height: 40px">
		                                         	<div class="pull-left" style="width: 50%">
													    <div class="pull-left gray ps-left-text">
													    	<i class="fa fa-bookmark blue"></i>&nbsp;审核状态
													    	
													    </div>
														<div class="ticket-user pull-left ps-right-box">
														  	<div class="pull-left">
																<c:choose>
																	<c:when test="${bgypApply.applyCheckState eq 0}">待提交</c:when>
																	<c:when test="${bgypApply.applyCheckState eq 1}">审核中</c:when>
																	<c:when test="${bgypApply.applyCheckState eq 2}">已申领</c:when>
																	<c:when test="${bgypApply.applyCheckState eq 3}">未通过</c:when>
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
																	  title="${bgypApply.spUserName}" 
																		src="/doanLoad/userImg/${bgypApply.comId }/${bgypApply.spUserId}"/>
																	<span class="user-name" style="font-size:10px;">${bgypApply.spUserName }</span>
																</div>
															</div>  
														</div>
		                                         </li>
		                                          <c:if test="${bgypApply.applyCheckState eq 3}">
			                                         <li class="ticket-item no-shadow ps-listline">
													    <div class="pull-left gray ps-left-text">
													    	<i class="fa fa-file blue"></i>&nbsp;审核意见
													    </div>
														<div class="ticket-user pull-left ps-right-box">
														  	<div class="pull-left">
																${bgypApply.spContent}
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
                                   <span class="widget-caption blue">办公用品条目</span>
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
												<th width="30%">办公用品名称</th>
												<th width="16%">单位</th>
												<th width="16%">规格</th>
												<th width="16%">现库存数</th>
												<th width="16%" style="border-right: 0">申领数</th>
												<th style="border-left: 0">&nbsp;</th>
											</tr>
                               		 	</thead>
                               		 	<tbody id="bgypApplyT0">
                               		 		
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
