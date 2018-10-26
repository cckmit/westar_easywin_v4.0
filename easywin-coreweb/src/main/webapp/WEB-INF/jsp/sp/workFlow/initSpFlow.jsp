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
	
	//form表单提交
	$("#saveFlow").click(function(){
		$(".subform").submit();
	});
	//form表单提交
	$("#startFlow").click(function(){
		$(".subform").submit();
	});
});

var fileTypes="*.rar;*.zip;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.pdf;*.mp3;*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.avi;*.wma;*.rmb;*.rm;*.flash;*.flv;*.mp4;*.mid;*.3gp";
var fileSize="200MB";
var	numberOfFiles = 200;
</script>
</head>
<body>
<form class="subform" method="post" action="/workFlow/startWrokFlow">
<tags:token></tags:token>
<input type="hidden" name="attentionState" id="attentionState" value="0"/>
<input type="hidden" name="sp_flowModel_id" value="${sp_flowModel.id}">
<input type="hidden" name="sid" value="${param.sid}">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">${sp_flowModel.processName}</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" onclick="setAtten(this)">
								<i class="fa fa-star-o"></i>关注
							</a>
							<a href="javascript:void(0)" class="blue" id="saveFlow">
								<i class="fa fa-save"></i>保存
							</a>
							<a href="javascript:void(0)" class="blue" id="startFlow">
								<i class="fa fa-save"></i>启动
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                                
                     <div class="widget-body margin-top-40">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">审批内容展示</span>
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
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div>
													<input id="customerName" datatype="input,sn" defaultLen="52" name="customerName" nullmsg="请填写客户名称" 
													class="colorpicker-default form-control" type="text" value="" style="width: 400px;float: left">
													<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
												</div>
												<span id="saveFlowWarm" style="float:left;margin-left:2px;"></span>
											</div>
											<script> 
												//当状态改变的时候执行的函数 
												function handleName(){
													var value = $('#customerName').val();
													var len = charLength(value.replace(/\s+/g,""));
													if(len%2==1){
														len = (len+1)/2;
													}else{
														len = len/2;
													}
													if(len>26){
														$('#msgTitle').html("(<font color='red'>"+len+"</font>/26)");
													}else{
														$('#msgTitle').html("("+len+"/26)");
													}
												} 
												//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
												if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
													document.getElementById('customerName').onpropertychange=handleName 
												}else {//非ie浏览器，比如Firefox 
													document.getElementById('customerName').addEventListener("input",handleName,false); 
												} 
											</script>  
                                         </li>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                          <div class="widget radius-bordered">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">联系人</span>
                                   <div class="widget-buttons">
                                      <a class="ps-point" data-toggle="collapse">
                                    	<i class="fa fa-minus blue"></i>
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
												<th>&nbsp;</th>
											</tr>
                               		 	</thead>
                               		 	<tbody>
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
                               		 			<a href="javascript:void(0)" class="fa fa-times-circle-o" onclick="delTr(this)" title="删除本行"></a>
                               		 			</td>
                               		 		</tr>
                               		 	</tbody>
                               		 </table>
                               			 <div class="pull-right" style="margin: 10px 0px">
											<button type="button" class="btn btn-info ws-btnBlue"  onclick="addLinkMan()">添加联系人</button>
										</div>
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
											  		<tags:userMore name="listCustomerSharer.userId" showName="userName" disDivKey="crmSharor_div"></tags:userMore>
												</div>
	                                         </li>
											<li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text">
										    		&nbsp;共享组
										    	</div>
												<div class="ticket-user pull-left ps-right-box">
											  		<div id="customerGroup_div" style="float: left">
													</div>
											  		<a href="javascript:void(0)" onclick="querySelfGroupForCustomer(0,'${param.sid}')" class="btn btn-blue btn-xs" title="组选择"><i class="fa fa-plus"></i>添加</a>
													<%--用于保持数据 --%>
													<div style="display:none" id="tempSelectDiv">
														<select style="display:none" id="selfGrouplist">
														</select>
													</div>
												</div>
	                                         </li>
                                        </ul>
                               		</div>
                               </div>
                           </div>
                           <div class="widget radius-bordered" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">更多配置</span>
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
									    		&nbsp;联系电话
									    	</div>
											<div class="ticket-user pull-left ps-right-box">
										  		<input class="colorpicker-default form-control " type="text" value="" ignore="ignore" id="linePhone" name="linePhone">
											</div>               
                                         </li>
										<li class="ticket-item no-shadow ps-listline">
									    	<div class="pull-left gray ps-left-text">
									    		&nbsp;传真
									    	</div>
											<div class="ticket-user pull-left ps-right-box">
										  		<input class="form-control" type="text" value="" id="fax" name="fax">
											</div>               
                                         </li>
										<li class="ticket-item no-shadow ps-listline">
									    	<div class="pull-left gray ps-left-text">
									    		&nbsp;邮编
									    	</div>
											<div class="ticket-user pull-left ps-right-box">
										  		<input class="colorpicker-default form-control" type="text" value="" ignore="ignore" id="postCode" name="postCode">
											</div>               
                                         </li>
										<li class="ticket-item no-shadow ps-listline" style="clear: both" >
									    	<div class="pull-left gray ps-left-text">
									    		&nbsp;联系地址
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px">
										  		<input class="colorpicker-default form-control" id="address" name="address" type="text" value="">
											</div>               
                                         </li>
										<li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;客户备注
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;" id="customerRemark" name="customerRemark" rows="" cols=""></textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
										<li class="ticket-item no-shadow autoHeight" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;相关附件
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
											  		<tags:uploadMore name="listUpfiles.upfileId" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
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
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
