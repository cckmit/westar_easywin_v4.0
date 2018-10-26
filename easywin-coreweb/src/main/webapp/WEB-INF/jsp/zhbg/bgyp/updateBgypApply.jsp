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
	var bgypApplyId = "${bgypApply.id}"
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
		getSelfJSON('/bgypApplyDetail/listBgypApplyDetail',{sid:sid,bgypApplyId:bgypApplyId},function(data){
			var listBgypApplyDetails = data.listBgypApplyDetails;
			//初始化数据
			addBgypApplyDetailTable(listBgypApplyDetails);
			
		})
		//初始化数据
		addBgypApplyDetailTable();
		//删除行数据
		$("body").on("click",".delBgypItem",function(){
			
			var ptr = $(this).parents("tr");
			var applyDetailId = $(ptr).attr("bgypApplyDetailId");
			var bgypItemId = $(ptr).attr("bgypItemId");
			if(applyDetailId){
				
				window.top.layer.confirm("确定删除办公用品?",function(index,layero){
					window.top.layer.close(index);
					//取得采购单详情信息
					getSelfJSON('/bgypApplyDetail/delBgypApplyDetail',{sid:sid,applyDetailId:applyDetailId},function(data){
						if(data.status=='y'){
							$(ptr).remove();
							//重置索引号
							resetIndex();
						}
						
					})
				});
			}else if(bgypItemId){
				$(this).parents("tr").remove();
				//重置索引号
				resetIndex();
			}else{
				$(this).parents("tr").remove();
				//重置索引号
				resetIndex();
				
			}
			
			
		})
		
		//提交表单
		$("body").on("click","#saveBtn,#submitBtn",function(){
			var spanId= $(this).attr("id");
			var formState = checkFormElement();
			if(formState){
				constrFormData();
				if(spanId=='saveBtn'){
					if($("#applyCheckState").val()=='3'){
					}else{
						$("#applyCheckState").val('0');
					}
				}else if(spanId=='submitBtn'){
					$("#applyCheckState").val('1');
				}
				$("#bgypApplyForm").submit();
			}
			
		})
	})
</script>
</head>
<body>
<form class="subform" method="post" action="/bgypApply/updateBgypApply" id="bgypApplyForm">
<tags:token></tags:token>
<input type="hidden" name="applyCheckState" id="applyCheckState" value="${bgypApply.applyCheckState}">
<input type="hidden" name="id"value="${bgypApply.id }">
<div id="formData">
	
</div>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">编辑申领单</span>
                        <div class="widget-buttons ps-toolsBtn">
                        	<a href="javascript:void(0)" class="blue" id="saveBtn">
								<i class="fa fa-save"></i>保存
							</a>
							<a href="javascript:void(0)" class="blue" id="submitBtn">
								<i class="fa fa-gavel"></i>提交
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
											<li class="ticket-item no-shadow autoHeight no-padding">
										    	<div class="pull-left gray ps-left-text padding-top-10">
										    		<i class="fa fa-file blue"></i>&nbsp;申领理由
										    		<font color="red">*</font>
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
											  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:80px;" 
											  		id="remark" name="remark">${bgypApply.remark}</textarea>
												</div> 
												<div class="ps-clear"></div>              
	                                         </li>
	                                         <c:if test="${bgypApply.applyCheckState eq 3}">
												<li class="ticket-item no-shadow ps-listline" style="padding: 5px 0 !important;min-height: 40px">
		                                         	<div class="pull-left" style="width: 50%">
													    <div class="pull-left gray ps-left-text">
													    	<i class="fa fa-bookmark blue"></i>&nbsp;审核状态
													    	
													    </div>
														<div class="ticket-user pull-left ps-right-box">
														  	<div class="pull-left">
																未通过
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
																		src="/downLoad/userImg/${bgypApply.comId}/${bgypApply.spUserId}"/>
																	<span class="user-name" style="font-size:10px;">${bgypApply.spUserName }</span>
																</div>
															</div>  
														</div>
		                                         </li>
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
												<th width="23%">办公用品名称</th>
												<th width="14%">单位</th>
												<th width="14%">规格</th>
												<th width="14%">申领中</th>
												<th width="14%">库存数</th>
												<th width="14%" style="border-right: 0">申领数</th>
												<th style="border-left: 0">&nbsp;</th>
											</tr>
                               		 	</thead>
                               		 	<tbody id="bgypApplyT0">
                               		 		
                               		 	</tbody>
                               		 </table>
                               		 <div class="pull-right" style="margin: 10px 0px">
											<button type="button" class="btn btn-info ws-btnBlue btn-sm"  onclick="addBgypPurDetailTable()">添加行数</button>
										</div>
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
