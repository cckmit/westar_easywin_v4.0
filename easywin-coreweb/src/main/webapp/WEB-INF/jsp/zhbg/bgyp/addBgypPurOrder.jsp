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
	var sid = '${param.sid}'
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		//初始化数据
		addBgypPurDetailTable();
		//删除行数据
		$("body").on("click",".delBgypItem",function(){
			$(this).parents("tr").remove();
			//重置索引号
			resetIndex();
		})
		
		//提交表单
		$("body").on("click","#saveBtn,#submitBtn",function(){
			var spanId= $(this).attr("id");
			var formState = checkFormElement();
			if(formState){
				constrFormData();
				if(spanId=='saveBtn'){
					$("#purOrderState").val('0');
					$("#bgypPurOrderForm").submit();
				}else if(spanId=='submitBtn'){
					
					$("#purOrderState").val('1');
					//是否为办公用品管理人员（审核采购和申领）
					getSelfJSON("/modAdmin/ajaxListModAdmin",{sid:sid,busType:'02701'},function(data){
						if(data.status=='y'){
							var listModAdmin = data.listModAdmin;
							if(listModAdmin && listModAdmin.length>0){
								$("#bgypPurOrderForm").submit();
							}else{
								//处理没有管理员的状况
								parent.handleNoAdmin('027');
							}
						}else{
							showNotification(2,data.info);
						}
					});
				}
			}
			
		})
	})
</script>
</head>
<body>
<form class="subform" method="post" action="/bgypPurOrder/addBgypPurOrder" id="bgypPurOrderForm">
<tags:token></tags:token>
<input type="hidden" name="purOrderState" id="purOrderState">
<div id="formData">
	
</div>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">采购单录入</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="saveBtn">
								<i class="fa fa-save"></i>保存
							</a>
							<a href="javascript:void(0)" class="blue" id="submitBtn">
								<i class="fa  fa-gavel"></i>送审
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
                                         <li class="ticket-item no-shadow ps-listline" style="padding: 5px 0 !important;min-height: 40px">
                                         	<div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-bookmark blue"></i>&nbsp;单据编号
											    	<span style="color: red">*</span>
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  	<div class="pull-left">
														<input id="purOrderNum" defaultLen="52" name="purOrderNum" nullmsg="请填写单据编号" 
														class="colorpicker-default form-control" type="text" value=""
														style="width: 250px;float: left">
													</div>
												</div>
                                         	</div>
											<div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-user blue"></i>&nbsp;采购人
											    	<span style="color: red">*</span>
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
													<tags:userOne datatype="s" name="purUserId" defaultInit="true"
														showValue="${userInfo.userName}" value="${userInfo.id}" uuid="${userInfo.smImgUuid}"
														filename="${userInfo.smImgName}" gender="${userInfo.gender}" onclick="true"
														showName="purUserName"></tags:userOne>
												</div>  
											</div>
                                         </li>
                                          <li class="ticket-item no-shadow ps-listline" style="clear:both;padding: 5px 0 !important;min-height: 40px">
	                                          <div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-clock-o blue"></i>&nbsp;采购时间
											    	<span style="color: red">*</span>
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												   <input class="colorpicker-default form-control" readonly="readonly"
													id="bgypPurDate" name="bgypPurDate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text" 
													value="">
												</div>               
	                                          </div>
	                                          <div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-clock-o blue"></i>&nbsp;采购金额
											    	<span style="color: red">*</span>
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												   <input class="colorpicker-default form-control"
													id="bgypTotalPrice" name="bgypTotalPrice" type="text" 
													value="">
												</div><span class="margin-left-5">元 </span>     
	                                          </div>
                                          </li>
                                          <li class="ticket-item no-shadow autoHeight no-padding">
										    	<div class="pull-left gray ps-left-text padding-top-10">
										    		<i class="fa fa-file blue"></i>&nbsp;备注信息
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
											  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:80px;" 
											  		id="content" name="content">${bgypPurOrder.content}</textarea>
												</div> 
												<div class="ps-clear"></div>              
	                                         </li>
											<li class="ticket-item no-shadow ps-listline no-padding">
												<div class="pull-left gray ps-left-text">
													<i class="fa fa-file blue"></i>&nbsp;相关附件</div>
												<div class="ticket-user pull-left ps-right-box"
													style="width: 400px;height: auto;">
													<div class="margin-top-10">
														<div style="clear:both;width: 300px" class="wu-example">
														    <!--用来存放文件信息-->
														    <div id="thelistlistBgypPurFiles_upfileId" class="uploader-list">
														    </div>
														    <div class="btns btn-sm">
														        <div id="pickerlistBgypPurFiles_upfileId">
														        	选择文件
														        </div>
														    </div>
															<script type="text/javascript">
																loadWebUpfiles('listBgypPurFiles_upfileId','${param.sid}','',"pickerlistBgypPurFiles_upfileId","thelistlistBgypPurFiles_upfileId",'filelistBgypPurFiles_upfileId')
															</script>
															<div style="position: relative; width: 350px; height: 90px;display: none">
																<div style="float: left; width: 250px">
																	<select list="listBgypPurFiles" listkey="upfileId" listvalue="fileName"  id="listBgypPurFiles_upfileId"
																	multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">
																	</select>
																</div>
															</div>
														</div>
														</div>
												</div>
												<div class="ps-clear"></div>
											</li>
											
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
