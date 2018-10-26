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
			datatype:{
				"carTypeId":function(gets,obj,curform,regxp){
					if($("#carTypeId").val()){
						return true;
					}else{
						return false;
					}
				},
			"qx":function(gets,obj,curform,regxp){
				if(($("#qxStartDate").val() || $("#qxEndDate").val()
						|| $("#qxPrice").val() || $("#strongInsurance_listUpfiles_id").val()) && !$(obj).val()){
					return "请填入到期时间！";
				}else{
					return true;
				}
			}, 
			"syx":function(gets,obj,curform,regxp){
				if(($("#syxStartDate").val() || $("#syxEndDate").val()
						|| $("#qxPrice").val() || $("#syInsurance_listUpfiles_id").val()) && !$(obj).val()){
					return "请填入到期时间！";
				}else{
					return true;
				}
			}, 
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		$("#addCar").click(function(){
			formSub();
		});
	});
	//提交表单
	function formSub(){
		$(".subform").submit();
	}
</script>
</head>
<body>
<form class="subform" method="post" action="/car/addCar">
<tags:token></tags:token>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">车辆添加</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="addCar">
								<i class="fa fa-save"></i>添加
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
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
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;车牌号
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="carNum" datatype="*" name="carNum" nullmsg="请输入车牌号" 
													class="colorpicker-default form-control" type="text" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;车辆型号
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="carModel"  name="carModel" 	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;发动机号
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="engineNum"  name="engineNum" 	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;发动机编号-车架号后6位
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="engineNumAfterSix"  name="engineNumAfterSix" 	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                            <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;发动机编号-登记号后7位
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="engineNumAfterSeven"  name="engineNumAfterSeven" 	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;排量(L)
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="displacement"  name="displacement" 	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;颜色
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="color"  name="color" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;座位数
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="seatNum" dataType="/^\s*$/|n" name="seatNum" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;购置日期
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="buyDate"  name="buyDate" class="colorpicker-default form-control" readonly="readonly"
													onClick="WdatePicker()" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;购置价格(元)
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="buyPrice"  name="buyPrice"  dataType = "/^\s*$/|/^(([0-9]+)|([0-9]+\.[0-9]{1,2}))$/" errorMsg = "请填入正确的数字！" 
													 class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                          <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;购置税价格(元)
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="buyTaxPrice"  name="buyTaxPrice"  dataType = "/^\s*$/|/^(([0-9]+)|([0-9]+\.[0-9]{1,2}))$/" errorMsg = "请填入正确的数字！" 
													class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                          <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;年检日期
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="annualInspection"  name="annualInspection" class="colorpicker-default form-control" readonly="readonly"
													onClick="WdatePicker()" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                            <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;车辆类型
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											<div datatype="carTypeId"  class=" pull-left ">
											   <select class="populate"  id="carTypeId" name="carTypeId" style="cursor:auto;width: 200px">
													<optgroup label="选择车辆类型"></optgroup>
													<c:choose>
														<c:when test="${not empty listCarType}">
							 								<c:forEach items="${listCarType}" var="carType" varStatus="status">
							 								<option value="${carType.id}">${carType.typeName}</option>
							 								</c:forEach>
														</c:when>
													</c:choose>
												</select>
												</div>
											</div>               
                                           </li>
                                             </li>
                                          <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;车辆状态
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
											  		<select class="populate" id="stateType" name="stateType" >
					 								<option value="1">可用</option>
					 								<option value="2">损坏</option>
					 								<option value="3">维修</option>
					 								<option value="4">报废</option>
														</select>
												</div>
											</div>
                                         </li>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                             <div class="widget radius-bordered collapsed" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">更多配置</span>
                                   <div class="widget-buttons btn-div-full">
                                      <a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-plus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               <div class="widget-body no-shadow">
                               	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow autoHeight no-padding">
										    <div class="pull-left gray ps-left-text padding-top-10">
										    	&nbsp;申请人员范围
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div class="pull-left gray ps-left-text padding-top-10">
													<tags:userMore name="listScopeUser.userId" showName="userName" callBackStart="yes" disDivKey="carPersonDiv"></tags:userMore>
												</div>
											</div>
											<div class="ps-clear"></div>                 
                                        </li>
                                         <li class="ticket-item no-shadow autoHeight no-padding">
										    <div class="pull-left gray ps-left-text padding-top-10">
										    	&nbsp;申请部门范围
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div class="pull-left gray ps-left-text padding-top-10">
													<tags:depMore name="listScopeDep.depId" showName="depName" disDivKey="carDepDiv"></tags:depMore>
												</div>
											</div>
											<div class="ps-clear"></div>                 
                                        </li>
										<li class="ticket-item no-shadow autoHeight ps-listline" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;相关附件
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
											  		<tags:uploadMore name="listCarUpfiles.id" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
												</div>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                  	</ul>
                                </div>
                              </div>
                              
                           </div>
                           <div class="widget radius-bordered collapsed" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">强险信息</span>
                                   <div class="widget-buttons btn-div-full">
                                      <a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-plus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               <div class="widget-body no-shadow">
                               	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                        <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;缴纳日期
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input name="strongInsurance.startDate" class="colorpicker-default form-control" readonly="readonly"
													id= "qxStartDate" onClick="WdatePicker()" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;到期日期
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input name="strongInsurance.endDate" class="colorpicker-default form-control" readonly="readonly"
														id= "qxEndDate" dataType ="qx" onClick="WdatePicker()" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;缴纳金额(元)
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input   name="strongInsurance.insurancePrice" id= "qxPrice" dataType = "/^\s*$/|/^(([0-9]+)|([0-9]+\.[0-9]{1,2}))$/" errorMsg = "请填入正确的数字！" 
													 class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
										<li class="ticket-item no-shadow autoHeight ps-listline" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;相关附件
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
											  		<tags:uploadMore name="strongInsurance.listUpfiles.id" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
												</div>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                  	</ul>
                                </div>
                              </div>
                              
                           </div>
                           
                               <div class="widget radius-bordered collapsed" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">商业险信息</span>
                                   <div class="widget-buttons btn-div-full">
                                      <a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-plus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               <div class="widget-body no-shadow">
                               	<div class="tickets-container bg-white">
									<ul class="tickets-list">
										  <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;缴纳日期
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input name="syInsurance.startDate" class="colorpicker-default form-control" readonly="readonly"
														id= "syxStartDate" onClick="WdatePicker()" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;到期日期
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input   name="syInsurance.endDate" class="colorpicker-default form-control" readonly="readonly"
													id= "syxEndDate"  dataType ="syx"  onClick="WdatePicker()" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                            <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;缴纳金额(元)
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input name="syInsurance.insurancePrice" id= "syxPrice" 
													class="colorpicker-default form-control" type="text" dataType = "/^\s*$/|/^(([0-9]+)|([0-9]+\.[0-9]{1,2}))$/" errorMsg = "请填入正确的数字！" 
													value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li> 
										<li class="ticket-item no-shadow autoHeight ps-listline" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;相关附件
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
											  		<tags:uploadMore name="syInsurance.listUpfiles.id" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
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
	</form>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
