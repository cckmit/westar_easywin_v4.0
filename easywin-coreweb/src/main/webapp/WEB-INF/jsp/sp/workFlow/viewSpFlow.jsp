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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
var sid = '${param.sid}'
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
var valid;
$(function() {
	valid = $(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		callback:function (form){
			//提交前验证是否在上传附件
			return sumitPreCheck(null);
		},
		datatype:{
			"area":function(gets,obj,curform,regxp){
				if(!strIsNull($("#areaIdAndType").val())){
					return true;
				}else{
					return false;
				}
			},
			"input":function(gets,obj,curform,regxp){
				var str = $(obj).val();
				
				if(str){
					var count = str.replace(/[^\x00-\xff]/g,"**").length;
					var len = $(obj).attr("defaultLen");
					if(count>len){
						return "客户名称太长";
					}else{
						return true;
					}
				}else{
					return false;
				}
			},
			"customerType":function(gets,obj,curform,regxp){
				if(!strIsNull($("#customerTypeId").val())){
					return true;
				}else{
					return false;
				}
			}
		},
		showAllError : true
	});
	$("#sp_agree").click(function(){
		//alert("同意了哦！");
		$(".subform").submit();
		//window.location.href="/workFlow/complete/${param.flowTaskId}?sid=${param.sid}"
	});
});



//添加关联模块
</script>
</head>
<body>
<form id="subform" action="/workFlow/complete/${param.flowTaskId}?sid=${param.sid}" class="subform" method="post">
<input type="hidden" name="processInstanceId" value="${param.processInstanceId}">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">查看客户</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" onclick="changeAtten('012',${customer.id},${customer.attentionState},'${param.sid}',this)">
								<i class="fa ${customer.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
								${customer.attentionState==0?'关注':'取消'}
							</a>
							<a href="javascript:void(0)" class="red" id="sp_agree">
								<i class="fa fa-ellipsis-h"></i>下一步
							</a>
                              <a class="green ps-point margin-right-0" data-toggle="dropdown"  id="sp_disagree">
                                  <i class="fa fa-th"></i>终止
                              </a>
                              <!--Notification Dropdown-->
                              <ul class="pull-right dropdown-menu dropdown-arrow ps-layerUl">
                                  <li>
                                      <a href="javascript:void(0)" onclick="addRelMod('005')">
                                          <div class="clearfix">
                                           	<i class="fa fa-pencil-square"></i>
                                            <span class="title ps-topmargin">添加关联项目</span>
                                          </div>
                                      </a>
                                  </li>
                                  <li>
                                      <a href="javascript:void(0)" onclick="addRelMod('003')">
                                          <div class="clearfix">
                                           	<i class="fa fa-flag"></i>
                                              <span class="title ps-topmargin">添加关联任务</span>
                                          </div>
                                      </a>
                                  </li>
                                </ul>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                                
                     <div class="widget-body margin-top-40">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">审批类容展示</span>
                                <div class="widget-buttons">
                                	<a class="ps-point" data-toggle="collapse">
                                    	<i class="fa fa-minus blue"></i>
                                   </a>
                                 </div>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	<i class="fa fa-group blue"></i>&nbsp;客户名称
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div>
											  	<c:choose>
													<c:when test="${empty editCrm}">
														<input id="customerName" defaultLen="52" name="customerName" nullmsg="请填写客户名称" 
														class="colorpicker-default form-control" type="text" title="${customer.customerName}" 
														value="${customer.customerName}"  onpropertychange="handleName()" onkeyup="handleName()"
														style="width: 400px;float: left">
														<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
													</c:when>
													<c:otherwise>
														 <strong>${customer.customerName}</strong>
													</c:otherwise>
												</c:choose>
									
												</div>
												<span id="addCrmWarm" style="float:left;margin-left:2px;"></span>
											</div>
                                         </li>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                          <div class="widget radius-bordered collapsed">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">联系人
	                                    <span id="linkManNum" style="font-size: 15px !important;">
											<c:if test="${not empty listLinkMan}">
												(${fn:length(listLinkMan)}) 
											</c:if>
										</span>
                                   </span>
                                  
                                   <div class="widget-buttons">
                                      <a class="ps-point" data-toggle="collapse">
                                    	<i class="fa fa-plus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               <div class="widget-body no-shadow">
                               		<div class="tickets-container bg-white">
                               		 <table class="table table-hover table-striped table-bordered table0" id="linkManTable" style="width: 100%">
                               		 	<thead>
                               		 		<tr>
	                               		 		<th width="30%">姓名</th>
												<th width="30%">职务</th>
												<th width="20%">电话</th>
												<th width="20%">手机</th>
												<c:choose>
													<c:when test="${empty editCrm}">
														<th>&nbsp;</th>
													</c:when>
												</c:choose>
											</tr>
                               		 	</thead>
                               		 	<tbody>
                               		 		<c:choose>
                               		 			<c:when test="${empty editCrm}">
                               		 				<!-- 允许进行编辑 -->
                               		 				<c:choose>
											     		<c:when test="${not empty listLinkMan}">
												     		<c:forEach items="${listLinkMan}" var="linkMan" varStatus="status">
													     		<tr>
													     			<td><a href="javascript:void(0);" onclick="viewOlm(${linkMan.outLinkManId});">${linkMan.linkManName }</a></td>
																    <td><input type="text" id="listLinkMan[${status.count-1}].post" name="listLinkMan[${status.count-1}].post" value="${linkMan.post}"/></td>
																    <td><input type="text" ignore="ignore" id="listLinkMan[${status.count-1}].linePhone" name="listLinkMan[${status.count-1}].linePhone" value="${linkMan.linePhone}"/></td>
																    <td><input type="text" ignore="ignore" nullmsg="请输入正确的手机号码" id="listLinkMan[${status.count-1}].movePhone" name="listLinkMan[${status.count-1}].movePhone" value="${linkMan.movePhone}"/></td>
																    <td><a href="javascript:void(0)" class="fa fa-times-circle-o" onclick="delLinkManTr(this,'${linkMan.id}')" title="删除本行"></a></td>
													     		</tr>
												     		</c:forEach>
											     		</c:when>
											     		<c:otherwise>
											     			<tr>
				                               		 			<td>
					                               		 			<input class="table_input" type="text" nullmsg="请输入联系人" 
					                               		 			id="listLinkMan[0].linkManName" name="listLinkMan[0].linkManName" />
				                               		 			</td>
				                               		 			<td>
				                               		 				<input class="table_input" type="text" id="listLinkMan[0].post" name="listLinkMan[0].post" />
				                               		 			</td>
				                               		 			<td>
				                               		 				<input class="table_input" type="text" ignore='ignore' id="listLinkMan[0].linePhone" name="listLinkMan[0].linePhone" />
				                               		 			</td>
				                               		 			<td>
				                               		 				<input class="table_input" type="text" ignore='ignore' nullmsg="请输入正确的手机号码" id="listLinkMan[0].movePhone" name="listLinkMan[0].movePhone"/>
				                               		 			</td>
				                               		 			<td>
				                               		 			<a href="javascript:void(0)" class="fa fa-times-circle-o" onclick="delLinkManTr(this,0)" title="删除本行"></a>
				                               		 			</td>
				                               		 		</tr>
											     		</c:otherwise>
											     	</c:choose>
                               		 			</c:when>
                               		 			<c:otherwise>
                               		 				<!--不允许编辑 -->
                               		 				<c:choose>
											     		<c:when test="${not empty listLinkMan}">
		                               		 				<c:forEach items="${listLinkMan}" var="linkMan" varStatus="status">
													     		<tr>
													     			<td><a href="javascript:void(0);" onclick="viewOlm(${linkMan.outLinkManId});">${linkMan.linkManName }</a></td>
																    <td><input type="text" value="${linkMan.post}" disabled /></td>
																    <td><input type="text" value="${linkMan.linePhone}" disabled /></td>
																    <td><input type="text" value="${linkMan.movePhone}" disabled /></td>
													     		</tr>
													     	</c:forEach>
											     		</c:when>
											     	</c:choose>
                               		 			</c:otherwise>
                               		 		</c:choose>
                               		 	</tbody>
                               		 </table>
                               		 <c:if test="${empty editCrm}">
                               			 <div class="pull-right" style="margin: 10px 0px">
											<button type="button" class="btn btn-info ws-btnBlue"  onclick="addLinkMan()">添加</button>
											<button type="button" class="btn btn-info ws-btnBlue"  onclick="customerLinkManUpdate()">保存</button>
										</div>
                               		 </c:if>
                                	</div>
                               </div>
                           </div>
                            <div class="widget radius-bordered" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">共享范围设置</span>
                                   <div class="widget-buttons">
                                      <a class="ps-point" data-toggle="collapse">
                                    	<i class="fa fa-minus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               <div class="widget-body no-shadow">
                               		<div class="tickets-container bg-white">
                               			<ul class="tickets-list">
											<li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text">
										    		&nbsp;分享人
										    	</div>
												<div class="ticket-user pull-left ps-right-box">
													<input type="hidden" id="sharerJson" name="sharerJson" value="${customer.sharerJson}"/>
													<c:choose>
														<c:when test="${empty editCrm}">
													  		<tags:userMore name="listCustomerSharer.userId" showName="userName" disDivKey="crmSharor_div" callBackStart="yes"></tags:userMore>
														</c:when>
														<c:otherwise>
															<div id="crmSharor_div"></div>
														</c:otherwise>
													</c:choose>
												</div>
	                                         </li>
											<li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text">
										    		&nbsp;共享组
										    	</div>
										    	<c:choose>
										    		<c:when test="${empty editCrm}">
														<div class="ticket-user pull-left ps-right-box">
													  		<div id="customerGroup_div" style="float: left">
													  			<c:choose>
																	<c:when test="${not empty listShareGroup}">
																		<c:forEach items="${listShareGroup}" var="grp" varStatus="vs">
																			<span id="crmGrp_${grp.id}" style="cursor:pointer;margin-right:5px;" title="双击移除" class="label label-default" ondblclick="delCrmShareGroup('${grp.id}')">${grp.grpName}</span>
																		</c:forEach>
																	</c:when>
																</c:choose>
															</div>
													  		<a href="javascript:void(0)" onclick="querySelfGroupForCustomer(0,'${param.sid}')" class="btn btn-blue btn-xs" title="组选择"><i class="fa fa-plus"></i>添加</a>
															<%--用于保持数据 --%>
															<div style="display:none" id="tempSelectDiv">
																<select style="display:none" id="selfGrouplist">
																	<c:choose>
																		<c:when test="${not empty listShareGroup}">
																			<c:forEach items="${listShareGroup}" var="grp" varStatus="vs">
																				<option value="${grp.id}">${grp.grpName}</option>
																			</c:forEach>
																		</c:when>
																	</c:choose>
																</select>
															</div>
														</div>
										    		</c:when>
										    		<c:otherwise>
														<div class="ticket-user pull-left ps-right-box}">
													  		<div id="customerGroup_div" style="float: left">
													  			<c:choose>
																	<c:when test="${not empty listShareGroup}">
																		<c:forEach items="${listShareGroup}" var="grp" varStatus="vs">
																			<span id="crmGrp_${grp.id}" style="cursor:pointer;margin-right:5px;" class="label label-default">${grp.grpName}</span>
																		</c:forEach>
																	</c:when>
																</c:choose>
															</div>
														</div>
										    		
										    		</c:otherwise>
										    	</c:choose>
	                                         </li>
                                        </ul>
                               		</div>
                               </div>
                           </div>
                           <div class="widget radius-bordered collapsed" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">更多配置</span>
                                   <div class="widget-buttons">
                                      <a class="ps-point" data-toggle="collapse">
                                    	<i class="fa fa-plus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               <div class="widget-body no-shadow">
                               	<div class="tickets-container bg-white">
									<ul class="tickets-list">
										<li class="ticket-item no-shadow ps-listline">
									    	<div class="pull-left gray ps-left-text">
									    		&nbsp;联系电话
									    	</div>
											<div class="ticket-user pull-left ps-right-box">
												<c:choose>
													<c:when test="${empty editCrm }">
												  		<input class="colorpicker-default form-control" type="text" value="${customer.linePhone}" 
												  		ignore="ignore" id="linePhone" name="linePhone">
													</c:when>
													<c:otherwise>
														<span class="user-name">${customer.linePhone}</span>
													</c:otherwise>
												</c:choose>
											</div>               
                                         </li>
										<li class="ticket-item no-shadow ps-listline">
									    	<div class="pull-left gray ps-left-text">
									    		&nbsp;传真
									    	</div>
											<div class="ticket-user pull-left ps-right-box">
												<c:choose>
													<c:when test="${empty editCrm}">
												  		<input class="colorpicker-default form-control" type="text" value="${customer.fax}" id="fax" name="fax">
													</c:when>
													<c:otherwise>
														<span class="user-name">${customer.fax}</span>
													</c:otherwise>
												</c:choose>
											</div>               
                                         </li>
										<li class="ticket-item no-shadow ps-listline">
									    	<div class="pull-left gray ps-left-text">
									    		&nbsp;邮编
									    	</div>
											<div class="ticket-user pull-left ps-right-box">
										  		<c:choose>
													<c:when test="${empty editCrm}">
														<input class="colorpicker-default form-control" type="text" value="${customer.postCode}" ignore="ignore" id="postCode" name="postCode">
													</c:when>
													<c:otherwise>
														<span class="user-name">${customer.postCode}</span>
													</c:otherwise>
												</c:choose>
											</div>               
                                         </li>
										<li class="ticket-item no-shadow ps-listline">
									    	<div class="pull-left gray ps-left-text">
									    		&nbsp;联系地址
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px">
												<c:choose>
													<c:when test="${empty editCrm}">
														<input class="colorpicker-default form-control" id="address" name="address" type="text" value="${customer.address}">
													</c:when>
													<c:otherwise>
														<span class="user-name">${customer.address}</span>	
													</c:otherwise>
												</c:choose>
											</div>               
                                         </li>
                                         <li class="ticket-item no-shadow autoHeight no-padding">
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;客户备注
									    	</div>
									    	<c:choose>
									    		<c:when test="${empty editCrm}">
													<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;" id="customerRemark" 
												  		name="customerRemark" rows="" cols="">${customer.customerRemark }</textarea>
													</div> 
									    		</c:when>
									    		<c:otherwise>
													<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
														<div class="margin-top-10 margin-bottom-10 margin-left-10"> 
															<tags:viewTextArea>${customer.customerRemark }</tags:viewTextArea>
														</div>
													</div> 
									    		</c:otherwise>
									    	</c:choose>
											<div class="ps-clear"></div>              
                                         </li>
                                  	</ul>
                                </div>
                              </div>
                           </div>
                           <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11">
                                             <li class="active" id="customerFeedBackInfoMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">反馈记录</a>
                                             </li>
                                             <li id="crmItem">
                                                 <a data-toggle="tab" href="javascript:void(0)">关联项目</a>
                                             </li>
                                             <li id="crmTask">
                                                 <a data-toggle="tab" href="javascript:void(0)">关联任务</a>
                                             </li>
                                             <li id="crmFlowRecordLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">移交记录</a>
                                             </li>
                                             <li id="customerLogMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">操作日志</a>
                                             </li>
                                             <li id="customerUpfileMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">客户文档</a>
                                             </li>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<iframe id="otherCustomerAttrIframe"
												src="/crm/customerFeedBackInfoPage?sid=${param.sid}&pager.pageSize=10&customerId=${customer.id}"
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
	</form>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
